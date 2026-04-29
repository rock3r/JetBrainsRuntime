/*
 * Copyright 2026 JetBrains s.r.o.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

#import <Metal/Metal.h>
#include <jni.h>
#include <algorithm>
#include <chrono>
#include <cstdio>
#include <mutex>
#include <unordered_map>
#include <vector>

#include "SkBlendMode.h"
#include "SkCanvas.h"
#include "SkColor.h"
#include "SkColorSpace.h"
#include "SkData.h"
#include "SkImage.h"
#include "SkImageInfo.h"
#include "SkFont.h"
#include "SkPaint.h"
#include "SkPicture.h"
#include "SkPixmap.h"
#include "SkRRect.h"
#include "SkSamplingOptions.h"
#include "SkString.h"
#include "SkSurface.h"
#include "ganesh/GrBackendSurface.h"
#include "ganesh/GrDirectContext.h"
#include "ganesh/mtl/GrMtlBackendContext.h"
#include "ganesh/mtl/GrMtlBackendSurface.h"
#include "ganesh/mtl/GrMtlDirectContext.h"
#include "ganesh/mtl/GrMtlTypes.h"
#include "include/gpu/ganesh/SkSurfaceGanesh.h"
#include "modules/skparagraph/include/FontCollection.h"
#include "modules/skparagraph/include/Paragraph.h"
#include "modules/skparagraph/include/ParagraphBuilder.h"
#include "modules/skparagraph/include/ParagraphStyle.h"
#include "modules/skparagraph/include/TextStyle.h"
#include "modules/skunicode/include/SkUnicode_icu.h"
#include "ports/SkFontMgr_mac_ct.h"

#include "MTLSurfaceDataBase.h"

static constexpr jint ABI_ID = 22;
static constexpr jint COMMAND_STREAM_MAGIC = 1246972723;
static constexpr jint COMMAND_STREAM_HEADER_SIZE = 6;
static constexpr jint COMMAND_STREAM_FLAGS_NONE = 0;
static constexpr jint COMMAND_COORDINATE_SPACE_SWING_USER = 1;
static constexpr jint COMMAND_PAINT_FORMAT_SOLID_ARGB = 1;
static constexpr jint COMMAND_RECORD_HEADER_SIZE_BYTES = 12;
static constexpr jint COMMAND_RECORD_FLAGS_NONE = 0;
static constexpr jint COMMAND_RECORD_FLAG_ANTIALIAS = 1;
static constexpr jint COMMAND_CLEAR = 1;
static constexpr jint COMMAND_FILL_RECT = 2;
static constexpr jint COMMAND_STROKE_LINE = 3;
static constexpr jint COMMAND_FILL_OVAL = 4;
static constexpr jint COMMAND_STROKE_OVAL = 5;
static constexpr jint COMMAND_CLEAR_RECT = 6;
static constexpr jint COMMAND_SAVE = 7;
static constexpr jint COMMAND_RESTORE = 8;
static constexpr jint COMMAND_CLIP_RECT = 9;
static constexpr jint COMMAND_CLIP_OP_INTERSECT = 0;
static constexpr jint COMMAND_CLIP_OP_DIFFERENCE = 1;
static constexpr jint COMMAND_TRANSLATE = 10;
static constexpr jint COMMAND_SCALE = 11;
static constexpr jint COMMAND_ROTATE = 12;
static constexpr jint COMMAND_SAVE_LAYER = 13;
static constexpr jint COMMAND_DRAW_IMAGE_ARGB = 14;
static constexpr jint COMMAND_DEFINE_IMAGE_ARGB = 15;
static constexpr jint COMMAND_DRAW_IMAGE_REF = 16;
static constexpr jint COMMAND_DRAW_TEXT_UTF16 = 17;
static constexpr jint COMMAND_CLEAR_IMAGE_CACHE = 18;
static constexpr jint COMMAND_DRAW_PARAGRAPH_UTF16 = 19;

static std::mutex gDirectContextMutex;
static std::unordered_map<void*, sk_sp<GrDirectContext>> gDirectContextsByMtlContext;
static std::mutex gImageCacheMutex;
static std::unordered_map<uint64_t, sk_sp<SkImage>> gImagesByKey;
static std::mutex gParagraphDependenciesMutex;
static sk_sp<skia::textlayout::FontCollection> gParagraphFontCollection;
static sk_sp<SkUnicode> gParagraphUnicode;

@class AWTView;
@class MTLLayer;
@class MTLContext;
@class EncoderManager;

@interface EncoderManager : NSObject
- (void)endEncoder;
@end

@interface MTLContext : NSObject
@property (readonly, strong) id<MTLDevice> device;
@property (strong) id<MTLCommandQueue> commandQueue;
@property (readonly) EncoderManager* encoderManager;
@end

typedef struct _JBRSkiaMTLGraphicsConfigInfo {
    MTLContext* context;
    jint displayID;
} JBRSkiaMTLGraphicsConfigInfo;

static sk_sp<skia::textlayout::FontCollection> paragraphFontCollection() {
    std::scoped_lock lock(gParagraphDependenciesMutex);
    if (gParagraphFontCollection == nullptr) {
        auto fontCollection = sk_make_sp<skia::textlayout::FontCollection>();
        fontCollection->setDefaultFontManager(SkFontMgr_New_CoreText(nullptr));
        gParagraphFontCollection = fontCollection;
    }
    return gParagraphFontCollection;
}

static sk_sp<SkUnicode> paragraphUnicode() {
    std::scoped_lock lock(gParagraphDependenciesMutex);
    if (gParagraphUnicode == nullptr) {
        gParagraphUnicode = SkUnicodes::ICU::Make();
    }
    return gParagraphUnicode;
}

static long long monotonicNanos() {
    return std::chrono::duration_cast<std::chrono::nanoseconds>(
            std::chrono::steady_clock::now().time_since_epoch()).count();
}

typedef struct _JBRSkiaMTLSDOps {
    AWTView* peerData;
    MTLLayer* layer;
    jint argb[4];
    JBRSkiaMTLGraphicsConfigInfo* configInfo;
} JBRSkiaMTLSDOps;

static MTLContext* getContextFromNativeOps(jlong nativeOpsPtr) {
    if (nativeOpsPtr == 0) {
        return nil;
    }

    BMTLSDOps* baseOps = reinterpret_cast<BMTLSDOps*>(static_cast<uintptr_t>(nativeOpsPtr));
    if (baseOps == nullptr || baseOps->privOps == nullptr) {
        return nil;
    }

    JBRSkiaMTLSDOps* mtlOps = static_cast<JBRSkiaMTLSDOps*>(baseOps->privOps);
    if (mtlOps->configInfo == nullptr) {
        return nil;
    }
    return mtlOps->configInfo->context;
}

static void drawDiagnosticPattern(SkCanvas* canvas, int width, int height, jlong frameTimeNanos) {
    canvas->clear(SkColorSetARGB(255, 20, 12, 42));

    SkPaint linePaint;
    linePaint.setAntiAlias(true);
    linePaint.setColor(SkColorSetARGB(210, 48, 215, 186));
    linePaint.setStrokeWidth(3.0f);

    int phase = static_cast<int>((frameTimeNanos / 12000000L) % 56L);
    for (int x = -height + phase; x < width + height; x += 56) {
        canvas->drawLine(static_cast<SkScalar>(x),
                         static_cast<SkScalar>(height),
                         static_cast<SkScalar>(x + height),
                         0.0f,
                         linePaint);
    }

    SkPaint boxPaint;
    boxPaint.setAntiAlias(true);
    boxPaint.setColor(SkColorSetARGB(235, 255, 87, 120));

    int box = std::max(40, std::min(width, height) / 4);
    SkRect rect = SkRect::MakeXYWH(static_cast<SkScalar>((width - box) / 2),
                                  static_cast<SkScalar>((height - box) / 2),
                                  static_cast<SkScalar>(box),
                                  static_cast<SkScalar>(box));
    canvas->drawRRect(SkRRect::MakeRectXY(rect, 18.0f, 18.0f), boxPaint);
}

static SkColor skColorFromArgb(jint argb) {
    return SkColorSetARGB(static_cast<U8CPU>((argb >> 24) & 0xff),
                          static_cast<U8CPU>((argb >> 16) & 0xff),
                          static_cast<U8CPU>((argb >> 8) & 0xff),
                          static_cast<U8CPU>(argb & 0xff));
}

static jsize recordLengthFromBytes(jint recordByteLength) {
    if (recordByteLength < COMMAND_RECORD_HEADER_SIZE_BYTES || recordByteLength % static_cast<jint>(sizeof(jint)) != 0) {
        return -1;
    }
    return recordByteLength / static_cast<jint>(sizeof(jint));
}

static jint decodeLittleEndianInt(const jbyte* bytes, jsize offset) {
    return static_cast<jint>(static_cast<unsigned char>(bytes[offset]))
            | (static_cast<jint>(static_cast<unsigned char>(bytes[offset + 1])) << 8)
            | (static_cast<jint>(static_cast<unsigned char>(bytes[offset + 2])) << 16)
            | (static_cast<jint>(bytes[offset + 3]) << 24);
}

static bool isValidStrokeMetadata(jint strokeWidth, jint strokeCap, jint strokeJoin, jint strokeMiter1000) {
    return strokeWidth >= 1
            && strokeCap >= 0
            && strokeCap <= 2
            && strokeJoin >= 0
            && strokeJoin <= 2
            && strokeMiter1000 >= 0;
}

static uint64_t imageCacheKey(jint high, jint low) {
    return (static_cast<uint64_t>(static_cast<uint32_t>(high)) << 32)
            | static_cast<uint32_t>(low);
}

static bool appendUtf8CodePoint(std::string& text, uint32_t codePoint) {
    if (codePoint <= 0x7f) {
        text.push_back(static_cast<char>(codePoint));
        return true;
    }
    if (codePoint <= 0x7ff) {
        text.push_back(static_cast<char>(0xc0 | (codePoint >> 6)));
        text.push_back(static_cast<char>(0x80 | (codePoint & 0x3f)));
        return true;
    }
    if (codePoint >= 0xd800 && codePoint <= 0xdfff) {
        return false;
    }
    if (codePoint <= 0xffff) {
        text.push_back(static_cast<char>(0xe0 | (codePoint >> 12)));
        text.push_back(static_cast<char>(0x80 | ((codePoint >> 6) & 0x3f)));
        text.push_back(static_cast<char>(0x80 | (codePoint & 0x3f)));
        return true;
    }
    if (codePoint <= 0x10ffff) {
        text.push_back(static_cast<char>(0xf0 | (codePoint >> 18)));
        text.push_back(static_cast<char>(0x80 | ((codePoint >> 12) & 0x3f)));
        text.push_back(static_cast<char>(0x80 | ((codePoint >> 6) & 0x3f)));
        text.push_back(static_cast<char>(0x80 | (codePoint & 0x3f)));
        return true;
    }
    return false;
}

template<typename CommandWords>
static bool appendUtf16CommandText(std::string& text, CommandWords commands, jsize& offset, jint charCount) {
    text.reserve(static_cast<size_t>(charCount) * 3);
    for (jint index = 0; index < charCount; index++) {
        jint codeUnit = commands[offset++];
        if (codeUnit < 0 || codeUnit > 0xffff) {
            return false;
        }
        if (codeUnit >= 0xd800 && codeUnit <= 0xdbff) {
            if (index + 1 >= charCount) {
                return false;
            }
            jint lowSurrogate = commands[offset++];
            index++;
            if (lowSurrogate < 0xdc00 || lowSurrogate > 0xdfff) {
                return false;
            }
            uint32_t codePoint = 0x10000
                    + ((static_cast<uint32_t>(codeUnit) - 0xd800) << 10)
                    + (static_cast<uint32_t>(lowSurrogate) - 0xdc00);
            if (!appendUtf8CodePoint(text, codePoint)) {
                return false;
            }
        } else if (!appendUtf8CodePoint(text, static_cast<uint32_t>(codeUnit))) {
            return false;
        }
    }
    return true;
}

struct IntCommandWords {
    const jint* words;

    jint operator[](jsize index) const {
        return words[index];
    }
};

struct LittleEndianByteCommandWords {
    const jbyte* bytes;

    jint operator[](jsize index) const {
        return decodeLittleEndianInt(bytes, index * static_cast<jsize>(sizeof(jint)));
    }
};

template<typename CommandWords>
static sk_sp<SkImage> makeRasterImage(CommandWords commands, jsize pixelOffset, jint imageWidth, jint imageHeight, jint pixelCount) {
    SkImageInfo imageInfo = SkImageInfo::Make(
            imageWidth,
            imageHeight,
            kN32_SkColorType,
            kUnpremul_SkAlphaType,
            SkColorSpace::MakeSRGB());
    std::vector<jint> pixels(static_cast<size_t>(pixelCount));
    for (jint pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++) {
        pixels[static_cast<size_t>(pixelIndex)] = commands[pixelOffset + pixelIndex];
    }
    SkPixmap pixmap(imageInfo, pixels.data(), static_cast<size_t>(imageWidth) * sizeof(jint));
    return SkImages::RasterFromPixmapCopy(pixmap);
}

static bool drawImage(SkCanvas* canvas,
                      const sk_sp<SkImage>& image,
                      jint recordFlags,
                      SkScalar srcLeft,
                      SkScalar srcTop,
                      SkScalar srcRight,
                      SkScalar srcBottom,
                      SkScalar dstLeft,
                      SkScalar dstTop,
                      SkScalar dstRight,
                      SkScalar dstBottom,
                      jint alpha1000) {
    if (image == nullptr || alpha1000 < 0 || alpha1000 > 1000) {
        return false;
    }
    SkPaint imagePaint;
    imagePaint.setAlphaf(static_cast<float>(alpha1000) / 1000.0f);
    SkSamplingOptions sampling = (recordFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0
            ? SkSamplingOptions(SkFilterMode::kLinear, SkMipmapMode::kNone)
            : SkSamplingOptions(SkFilterMode::kNearest, SkMipmapMode::kNone);
    canvas->drawImageRect(image,
                          SkRect::MakeLTRB(srcLeft, srcTop, srcRight, srcBottom),
                          SkRect::MakeLTRB(dstLeft, dstTop, dstRight, dstBottom),
                          sampling,
                          &imagePaint,
                          SkCanvas::kStrict_SrcRectConstraint);
    return true;
}

struct CommandReplayMetrics {
    int paragraphCommands = 0;
    long long paragraphNanos = 0;
};

template<typename CommandWords>
static bool drawCommandList(SkCanvas* canvas,
                            CommandWords commands,
                            jsize commandCount,
                            int width,
                            int height,
                            CommandReplayMetrics* metrics = nullptr) {
    if (commandCount < COMMAND_STREAM_HEADER_SIZE ||
            commands[0] != COMMAND_STREAM_MAGIC ||
            commands[1] != ABI_ID ||
            commands[2] != COMMAND_STREAM_FLAGS_NONE ||
            commands[4] != COMMAND_COORDINATE_SPACE_SWING_USER ||
            commands[5] != COMMAND_PAINT_FORMAT_SOLID_ARGB) {
        return false;
    }
    jint payloadLength = commands[3];
    if (payloadLength < 0 || payloadLength != commandCount - COMMAND_STREAM_HEADER_SIZE) {
        return false;
    }

    jsize offset = COMMAND_STREAM_HEADER_SIZE;
    jsize commandEnd = COMMAND_STREAM_HEADER_SIZE + payloadLength;
    while (offset < commandEnd) {
        jsize recordStart = offset;
        jint op = commands[offset++];
        if (offset >= commandEnd) {
            return false;
        }
        jint recordByteLength = commands[offset++];
        if (offset >= commandEnd) {
            return false;
        }
        jint recordFlags = commands[offset++];
        jsize recordLength = recordLengthFromBytes(recordByteLength);
        jsize recordEnd = recordStart + recordLength;
        if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || recordLength < 3 || recordEnd > commandEnd) {
            return false;
        }
        bool antiAlias = (recordFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0;
        switch (op) {
            case COMMAND_SAVE: {
                if (offset != recordEnd) {
                    return false;
                }
                canvas->save();
                break;
            }
            case COMMAND_RESTORE: {
                if (offset != recordEnd) {
                    return false;
                }
                if (canvas->getSaveCount() <= 1) {
                    return false;
                }
                canvas->restore();
                break;
            }
            case COMMAND_CLEAR_IMAGE_CACHE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset != recordEnd) {
                    return false;
                }
                std::lock_guard<std::mutex> lock(gImageCacheMutex);
                gImagesByKey.clear();
                std::fprintf(stderr, "JBR_SKIA_INTEROP_IMAGE_CACHE_CLEAR backend=native\n");
                break;
            }
            case COMMAND_CLIP_RECT: {
                if (offset + 5 != recordEnd) {
                    return false;
                }
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint rectWidth = commands[offset++];
                jint rectHeight = commands[offset++];
                jint clipOp = commands[offset++];
                if (clipOp != COMMAND_CLIP_OP_INTERSECT && clipOp != COMMAND_CLIP_OP_DIFFERENCE) {
                    return false;
                }
                canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                  static_cast<SkScalar>(y),
                                                  static_cast<SkScalar>(rectWidth),
                                                  static_cast<SkScalar>(rectHeight)),
                                 clipOp == COMMAND_CLIP_OP_DIFFERENCE ? SkClipOp::kDifference : SkClipOp::kIntersect,
                                 antiAlias);
                break;
            }
            case COMMAND_TRANSLATE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) {
                    return false;
                }
                SkScalar dx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar dy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                canvas->translate(dx, dy);
                break;
            }
            case COMMAND_SCALE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) {
                    return false;
                }
                SkScalar sx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar sy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                canvas->scale(sx, sy);
                break;
            }
            case COMMAND_ROTATE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 1 != recordEnd) {
                    return false;
                }
                SkScalar degrees = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                canvas->rotate(degrees);
                break;
            }
            case COMMAND_SAVE_LAYER: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 5 != recordEnd) {
                    return false;
                }
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint layerWidth = commands[offset++];
                jint layerHeight = commands[offset++];
                jint alpha1000 = commands[offset++];
                if (alpha1000 < 0 || alpha1000 > 1000) {
                    return false;
                }
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                canvas->saveLayerAlphaf(&bounds, static_cast<float>(alpha1000) / 1000.0f);
                break;
            }
            case COMMAND_DEFINE_IMAGE_ARGB: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 5 > recordEnd) {
                    return false;
                }
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint imageWidth = commands[offset++];
                const jint imageHeight = commands[offset++];
                const jint pixelCount = commands[offset++];
                if (imageWidth <= 0 || imageHeight <= 0 || imageWidth > 4096 || imageHeight > 4096 ||
                        pixelCount != imageWidth * imageHeight || offset + pixelCount != recordEnd) {
                    return false;
                }
                sk_sp<SkImage> image = makeRasterImage(commands, offset, imageWidth, imageHeight, pixelCount);
                offset += pixelCount;
                if (image == nullptr) {
                    return false;
                }
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    gImagesByKey[key] = image;
                }
                break;
            }
            case COMMAND_DRAW_IMAGE_REF: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 14 != recordEnd) {
                    return false;
                }
                const SkScalar srcLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint imageWidth = commands[offset++];
                const jint imageHeight = commands[offset++];
                const jint alpha1000 = commands[offset++];
                const jint filterQuality = commands[offset++];
                if (imageWidth <= 0 || imageHeight <= 0 || imageWidth > 4096 || imageHeight > 4096 ||
                        alpha1000 < 0 || alpha1000 > 1000 || filterQuality < 0 || filterQuality > 3) {
                    return false;
                }
                sk_sp<SkImage> image;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(key);
                    if (found == gImagesByKey.end()) {
                        return false;
                    }
                    image = found->second;
                }
                if (image->width() != imageWidth || image->height() != imageHeight) {
                    return false;
                }
                if (!drawImage(canvas, image, recordFlags, srcLeft, srcTop, srcRight, srcBottom,
                               dstLeft, dstTop, dstRight, dstBottom, alpha1000)) {
                    return false;
                }
                break;
            }
            case COMMAND_DRAW_IMAGE_ARGB: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 13 > recordEnd) {
                    return false;
                }
                const SkScalar srcLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint imageWidth = commands[offset++];
                const jint imageHeight = commands[offset++];
                const jint alpha1000 = commands[offset++];
                const jint filterQuality = commands[offset++];
                const jint pixelCount = commands[offset++];
                if (imageWidth <= 0 || imageHeight <= 0 || imageWidth > 4096 || imageHeight > 4096 ||
                        pixelCount != imageWidth * imageHeight || offset + pixelCount != recordEnd ||
                        alpha1000 < 0 || alpha1000 > 1000 || filterQuality < 0 || filterQuality > 3) {
                    return false;
                }
                sk_sp<SkImage> image = makeRasterImage(commands, offset, imageWidth, imageHeight, pixelCount);
                offset += pixelCount;
                if (!drawImage(canvas, image, recordFlags, srcLeft, srcTop, srcRight, srcBottom,
                               dstLeft, dstTop, dstRight, dstBottom, alpha1000)) {
                    return false;
                }
                break;
            }
            case COMMAND_DRAW_TEXT_UTF16: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 5 > recordEnd) {
                    return false;
                }
                const SkScalar x = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar baseline = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar fontSize = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkColor color = skColorFromArgb(commands[offset++]);
                const jint charCount = commands[offset++];
                if (fontSize <= 0.0f || charCount < 0 || charCount > 4096 || offset + charCount != recordEnd) {
                    return false;
                }
                std::string text;
                if (!appendUtf16CommandText(text, commands, offset, charCount)) {
                    return false;
                }
                SkFont font(nullptr, fontSize);
                font.setEdging((recordFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0
                        ? SkFont::Edging::kAntiAlias
                        : SkFont::Edging::kAlias);
                SkPaint paint;
                paint.setAntiAlias((recordFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0);
                paint.setColor(color);
                canvas->drawSimpleText(text.data(), text.size(), SkTextEncoding::kUTF8, x, baseline, font, paint);
                break;
            }
            case COMMAND_DRAW_PARAGRAPH_UTF16: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 6 > recordEnd) {
                    return false;
                }
                const SkScalar x = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar y = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar paragraphWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar fontSize = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkColor color = skColorFromArgb(commands[offset++]);
                const jint fontWeight = commands[offset++];
                const jint fontWidth = commands[offset++];
                const jint fontSlant = commands[offset++];
                const jint textAlign = commands[offset++];
                const jint textDirection = commands[offset++];
                const jint lineHeightMultiplier1000 = commands[offset++];
                const jint maxLines = commands[offset++];
                const jint ellipsisMode = commands[offset++];
                const jint charCount = commands[offset++];
                if (paragraphWidth <= 0.0f || fontSize <= 0.0f ||
                        fontWeight < 1 || fontWeight > 1000 ||
                        fontWidth < 1 || fontWidth > 9 ||
                        fontSlant < 0 || fontSlant > 2 ||
                        textAlign < 0 || textAlign > 5 ||
                        textDirection < 0 || textDirection > 1 ||
                        lineHeightMultiplier1000 < 0 || lineHeightMultiplier1000 > 100000 ||
                        maxLines < 0 || maxLines > 4096 ||
                        ellipsisMode < 0 || ellipsisMode > 1 ||
                        charCount < 0 || charCount > 4096 || offset + charCount != recordEnd) {
                    return false;
                }
                std::string text;
                if (!appendUtf16CommandText(text, commands, offset, charCount)) {
                    return false;
                }
                const long long paragraphStartNanos = metrics != nullptr ? monotonicNanos() : 0;
                skia::textlayout::ParagraphStyle paragraphStyle;
                paragraphStyle.setTextAlign(static_cast<skia::textlayout::TextAlign>(textAlign));
                paragraphStyle.setTextDirection(static_cast<skia::textlayout::TextDirection>(textDirection));
                if (maxLines > 0) {
                    paragraphStyle.setMaxLines(static_cast<size_t>(maxLines));
                    if (ellipsisMode == 1) {
                        paragraphStyle.setEllipsis(SkString("..."));
                    }
                }
                skia::textlayout::TextStyle textStyle;
                textStyle.setColor(color);
                textStyle.setFontSize(fontSize);
                textStyle.setFontStyle(SkFontStyle(
                        fontWeight,
                        fontWidth,
                        static_cast<SkFontStyle::Slant>(fontSlant)));
                if (lineHeightMultiplier1000 > 0) {
                    textStyle.setHeight(static_cast<SkScalar>(lineHeightMultiplier1000) / 1000.0f);
                    textStyle.setHeightOverride(true);
                }
                auto builder = skia::textlayout::ParagraphBuilder::make(
                        paragraphStyle,
                        paragraphFontCollection(),
                        paragraphUnicode());
                builder->pushStyle(textStyle);
                builder->addText(text.data(), text.size());
                std::unique_ptr<skia::textlayout::Paragraph> paragraph = builder->Build();
                if (paragraph == nullptr) {
                    return false;
                }
                paragraph->layout(paragraphWidth);
                paragraph->paint(canvas, x, y);
                if (metrics != nullptr) {
                    metrics->paragraphCommands++;
                    metrics->paragraphNanos += monotonicNanos() - paragraphStartNanos;
                }
                break;
            }
            case COMMAND_CLEAR: {
                if (offset + 1 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(commands[offset++]));
                canvas->drawRect(SkRect::MakeWH(static_cast<SkScalar>(width), static_cast<SkScalar>(height)), paint);
                break;
            }
            case COMMAND_FILL_RECT: {
                if (offset + 6 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint rectWidth = commands[offset++];
                jint rectHeight = commands[offset++];
                jint radius = commands[offset++];
                SkRect rect = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                              static_cast<SkScalar>(y),
                                              static_cast<SkScalar>(rectWidth),
                                              static_cast<SkScalar>(rectHeight));
                if (radius > 0) {
                    canvas->drawRRect(SkRRect::MakeRectXY(rect,
                                                          static_cast<SkScalar>(radius),
                                                          static_cast<SkScalar>(radius)),
                                      paint);
                } else {
                    canvas->drawRect(rect, paint);
                }
                break;
            }
            case COMMAND_CLEAR_RECT: {
                if (offset + 4 != recordEnd) {
                    return false;
                }
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint rectWidth = commands[offset++];
                jint rectHeight = commands[offset++];
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setBlendMode(SkBlendMode::kClear);
                canvas->drawRect(SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                  static_cast<SkScalar>(y),
                                                  static_cast<SkScalar>(rectWidth),
                                                  static_cast<SkScalar>(rectHeight)),
                                 paint);
                break;
            }
            case COMMAND_STROKE_LINE: {
                if (offset + 9 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x1 = commands[offset++];
                jint y1 = commands[offset++];
                jint x2 = commands[offset++];
                jint y2 = commands[offset++];
                jint strokeWidth = commands[offset++];
                jint strokeCap = commands[offset++];
                jint strokeJoin = commands[offset++];
                jint strokeMiter1000 = commands[offset++];
                if (!isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000)) {
                    return false;
                }
                paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                canvas->drawLine(static_cast<SkScalar>(x1),
                                 static_cast<SkScalar>(y1),
                                 static_cast<SkScalar>(x2),
                                 static_cast<SkScalar>(y2),
                                 paint);
                break;
            }
            case COMMAND_FILL_OVAL: {
                if (offset + 5 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint ovalWidth = commands[offset++];
                jint ovalHeight = commands[offset++];
                SkRect rect = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                              static_cast<SkScalar>(y),
                                              static_cast<SkScalar>(ovalWidth),
                                              static_cast<SkScalar>(ovalHeight));
                canvas->drawOval(rect, paint);
                break;
            }
            case COMMAND_STROKE_OVAL: {
                if (offset + 9 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint ovalWidth = commands[offset++];
                jint ovalHeight = commands[offset++];
                jint strokeWidth = commands[offset++];
                jint strokeCap = commands[offset++];
                jint strokeJoin = commands[offset++];
                jint strokeMiter1000 = commands[offset++];
                if (!isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000)) {
                    return false;
                }
                paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                SkRect rect = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                              static_cast<SkScalar>(y),
                                              static_cast<SkScalar>(ovalWidth),
                                              static_cast<SkScalar>(ovalHeight));
                canvas->drawOval(rect, paint);
                break;
            }
            default:
                return false;
        }
        if (offset != recordEnd) {
            return false;
        }
    }
    return true;
}

static sk_sp<SkSurface> wrapTextureSurface(GrDirectContext* directContext,
                                           id<MTLTexture> texture,
                                           int width,
                                           int height) {
    GrMtlTextureInfo textureInfo;
    textureInfo.fTexture.retain((__bridge GrMTLHandle) texture);
    GrBackendRenderTarget renderTarget = GrBackendRenderTargets::MakeMtl(width, height, textureInfo);
    return SkSurfaces::WrapBackendRenderTarget(
            directContext,
            renderTarget,
            kTopLeft_GrSurfaceOrigin,
            kBGRA_8888_SkColorType,
            nullptr,
            nullptr);
}

static sk_sp<GrDirectContext> makeDirectContextForSurface(jlong nativeOpsPtr, id<MTLTexture> texture) {
    MTLContext* mtlc = getContextFromNativeOps(nativeOpsPtr);
    if (mtlc == nil || mtlc.device == nil || mtlc.commandQueue == nil) {
        return nullptr;
    }
    if (mtlc.device != texture.device) {
        return nullptr;
    }

    [mtlc.encoderManager endEncoder];

    void* contextKey = (__bridge void*) mtlc;
    {
        std::lock_guard<std::mutex> lock(gDirectContextMutex);
        auto existing = gDirectContextsByMtlContext.find(contextKey);
        if (existing != gDirectContextsByMtlContext.end()) {
            return existing->second;
        }
    }

    GrMtlBackendContext backendContext = {};
    backendContext.fDevice.retain((__bridge GrMTLHandle) mtlc.device);
    backendContext.fQueue.retain((__bridge GrMTLHandle) mtlc.commandQueue);
    sk_sp<GrDirectContext> directContext = GrDirectContexts::MakeMetal(backendContext);
    if (directContext != nullptr) {
        std::lock_guard<std::mutex> lock(gDirectContextMutex);
        gDirectContextsByMtlContext.emplace(contextKey, directContext);
    }
    return directContext;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeRenderDiagnosticFrame
        (JNIEnv* env, jclass cls, jlong nativeOpsPtr, jlong metalTexturePtr,
         jint width, jint height, jlong frameTimeNanos) {
    @autoreleasepool {
        if (metalTexturePtr == 0 || width <= 0 || height <= 0) {
            return JNI_FALSE;
        }

        id<MTLTexture> texture = (__bridge id<MTLTexture>) reinterpret_cast<void*>(static_cast<uintptr_t>(metalTexturePtr));
        if (texture == nil || texture.device == nil) {
            return JNI_FALSE;
        }

        sk_sp<GrDirectContext> directContext = makeDirectContextForSurface(nativeOpsPtr, texture);
        if (directContext == nullptr) {
            return JNI_FALSE;
        }

        sk_sp<SkSurface> surface = wrapTextureSurface(directContext.get(), texture, width, height);
        if (surface == nullptr) {
            return JNI_FALSE;
        }

        drawDiagnosticPattern(surface->getCanvas(), width, height, frameTimeNanos);
        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        return JNI_TRUE;
    }
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeRenderPictureFrame
        (JNIEnv* env, jclass cls, jlong nativeOpsPtr, jlong metalTexturePtr,
         jint destinationX, jint destinationY, jint destinationWidth, jint destinationHeight,
         jint width, jint height, jlong frameTimeNanos, jbyteArray pictureArray) {
    @autoreleasepool {
        if (metalTexturePtr == 0 || destinationWidth <= 0 || destinationHeight <= 0 ||
                width <= 0 || height <= 0 || pictureArray == nullptr) {
            return JNI_FALSE;
        }

        id<MTLTexture> texture = (__bridge id<MTLTexture>) reinterpret_cast<void*>(static_cast<uintptr_t>(metalTexturePtr));
        if (texture == nil || texture.device == nil) {
            return JNI_FALSE;
        }

        jsize pictureSize = env->GetArrayLength(pictureArray);
        if (pictureSize <= 0) {
            return JNI_FALSE;
        }

        jboolean isCopy = JNI_FALSE;
        jbyte* pictureBytes = env->GetByteArrayElements(pictureArray, &isCopy);
        if (pictureBytes == nullptr) {
            return JNI_FALSE;
        }
        sk_sp<SkData> pictureData = SkData::MakeWithCopy(pictureBytes, static_cast<size_t>(pictureSize));
        env->ReleaseByteArrayElements(pictureArray, pictureBytes, JNI_ABORT);
        if (pictureData == nullptr) {
            return JNI_FALSE;
        }

        sk_sp<SkPicture> picture = SkPicture::MakeFromData(pictureData.get());
        if (picture == nullptr) {
            return JNI_FALSE;
        }

        sk_sp<GrDirectContext> directContext = makeDirectContextForSurface(nativeOpsPtr, texture);
        if (directContext == nullptr) {
            return JNI_FALSE;
        }

        sk_sp<SkSurface> surface = wrapTextureSurface(
                directContext.get(),
                texture,
                static_cast<int>(texture.width),
                static_cast<int>(texture.height));
        if (surface == nullptr) {
            return JNI_FALSE;
        }

        SkCanvas* canvas = surface->getCanvas();
        canvas->save();
        canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(destinationX),
                                          static_cast<SkScalar>(destinationY),
                                          static_cast<SkScalar>(destinationWidth),
                                          static_cast<SkScalar>(destinationHeight)));
        canvas->translate(static_cast<SkScalar>(destinationX),
                          static_cast<SkScalar>(destinationY));
        picture->playback(canvas);
        canvas->restore();
        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_PICTURE_FRAME destinationX=%d destinationY=%d destinationWidth=%d destinationHeight=%d width=%d height=%d bytes=%d rendered=true\n",
                     destinationX,
                     destinationY,
                     destinationWidth,
                     destinationHeight,
                     width,
                     height,
                     pictureSize);
        return JNI_TRUE;
    }
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeRenderCommandFrame
        (JNIEnv* env, jclass cls, jlong nativeOpsPtr, jlong metalTexturePtr,
         jint destinationX, jint destinationY, jint destinationWidth, jint destinationHeight,
         jint width, jint height, jlong frameTimeNanos, jintArray commandArray) {
    @autoreleasepool {
        if (metalTexturePtr == 0 || destinationWidth <= 0 || destinationHeight <= 0 ||
                width <= 0 || height <= 0 || commandArray == nullptr) {
            return JNI_FALSE;
        }

        id<MTLTexture> texture = (__bridge id<MTLTexture>) reinterpret_cast<void*>(static_cast<uintptr_t>(metalTexturePtr));
        if (texture == nil || texture.device == nil) {
            return JNI_FALSE;
        }

        jsize commandCount = env->GetArrayLength(commandArray);
        if (commandCount <= 0) {
            return JNI_FALSE;
        }
        const long long frameStartNanos = monotonicNanos();

        sk_sp<GrDirectContext> directContext = makeDirectContextForSurface(nativeOpsPtr, texture);
        if (directContext == nullptr) {
            return JNI_FALSE;
        }

        sk_sp<SkSurface> surface = wrapTextureSurface(
                directContext.get(),
                texture,
                static_cast<int>(texture.width),
                static_cast<int>(texture.height));
        if (surface == nullptr) {
            return JNI_FALSE;
        }

        jboolean isCopy = JNI_FALSE;
        jint* commands = env->GetIntArrayElements(commandArray, &isCopy);
        if (commands == nullptr) {
            return JNI_FALSE;
        }

        SkCanvas* canvas = surface->getCanvas();
        CommandReplayMetrics metrics;
        const long long drawStartNanos = monotonicNanos();
        canvas->save();
        canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(destinationX),
                                          static_cast<SkScalar>(destinationY),
                                          static_cast<SkScalar>(destinationWidth),
                                          static_cast<SkScalar>(destinationHeight)));
        canvas->translate(static_cast<SkScalar>(destinationX),
                          static_cast<SkScalar>(destinationY));
        bool rendered = drawCommandList(canvas, IntCommandWords{commands}, commandCount, width, height, &metrics);
        canvas->restore();
        const long long drawNanos = monotonicNanos() - drawStartNanos;
        env->ReleaseIntArrayElements(commandArray, commands, JNI_ABORT);
        if (!rendered) {
            return JNI_FALSE;
        }

        const long long flushStartNanos = monotonicNanos();
        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        const long long flushNanos = monotonicNanos() - flushStartNanos;
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_FRAME destinationX=%d destinationY=%d destinationWidth=%d destinationHeight=%d width=%d height=%d commands=%d rendered=true\n",
                     destinationX,
                     destinationY,
                     destinationWidth,
                     destinationHeight,
                     width,
                     height,
                     commandCount);
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_TIMING totalNanos=%lld drawNanos=%lld flushNanos=%lld paragraphCommands=%d paragraphNanos=%lld\n",
                     monotonicNanos() - frameStartNanos,
                     drawNanos,
                     flushNanos,
                     metrics.paragraphCommands,
                     metrics.paragraphNanos);
        return JNI_TRUE;
    }
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeRenderCommandBufferFrame
        (JNIEnv* env, jclass cls, jlong nativeOpsPtr, jlong metalTexturePtr,
         jint destinationX, jint destinationY, jint destinationWidth, jint destinationHeight,
         jint width, jint height, jlong frameTimeNanos, jbyteArray commandArray) {
    @autoreleasepool {
        if (metalTexturePtr == 0 || destinationWidth <= 0 || destinationHeight <= 0 ||
                width <= 0 || height <= 0 || commandArray == nullptr) {
            return JNI_FALSE;
        }

        id<MTLTexture> texture = (__bridge id<MTLTexture>) reinterpret_cast<void*>(static_cast<uintptr_t>(metalTexturePtr));
        if (texture == nil || texture.device == nil) {
            return JNI_FALSE;
        }

        jsize commandByteCount = env->GetArrayLength(commandArray);
        if (commandByteCount <= 0 || commandByteCount % static_cast<jsize>(sizeof(jint)) != 0) {
            return JNI_FALSE;
        }

        jboolean isCopy = JNI_FALSE;
        jbyte* commandBytes = env->GetByteArrayElements(commandArray, &isCopy);
        if (commandBytes == nullptr) {
            return JNI_FALSE;
        }

        jsize commandCount = commandByteCount / static_cast<jsize>(sizeof(jint));
        const long long frameStartNanos = monotonicNanos();

        sk_sp<GrDirectContext> directContext = makeDirectContextForSurface(nativeOpsPtr, texture);
        if (directContext == nullptr) {
            env->ReleaseByteArrayElements(commandArray, commandBytes, JNI_ABORT);
            return JNI_FALSE;
        }

        sk_sp<SkSurface> surface = wrapTextureSurface(
                directContext.get(),
                texture,
                static_cast<int>(texture.width),
                static_cast<int>(texture.height));
        if (surface == nullptr) {
            env->ReleaseByteArrayElements(commandArray, commandBytes, JNI_ABORT);
            return JNI_FALSE;
        }

        SkCanvas* canvas = surface->getCanvas();
        CommandReplayMetrics metrics;
        const long long drawStartNanos = monotonicNanos();
        canvas->save();
        canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(destinationX),
                                          static_cast<SkScalar>(destinationY),
                                          static_cast<SkScalar>(destinationWidth),
                                          static_cast<SkScalar>(destinationHeight)));
        canvas->translate(static_cast<SkScalar>(destinationX),
                          static_cast<SkScalar>(destinationY));
        bool rendered = drawCommandList(canvas, LittleEndianByteCommandWords{commandBytes}, commandCount, width, height, &metrics);
        canvas->restore();
        const long long drawNanos = monotonicNanos() - drawStartNanos;
        env->ReleaseByteArrayElements(commandArray, commandBytes, JNI_ABORT);
        if (!rendered) {
            return JNI_FALSE;
        }

        const long long flushStartNanos = monotonicNanos();
        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        const long long flushNanos = monotonicNanos() - flushStartNanos;
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_FRAME destinationX=%d destinationY=%d destinationWidth=%d destinationHeight=%d width=%d height=%d commands=%d rendered=true\n",
                     destinationX,
                     destinationY,
                     destinationWidth,
                     destinationHeight,
                     width,
                     height,
                     commandCount);
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_TIMING totalNanos=%lld drawNanos=%lld flushNanos=%lld paragraphCommands=%d paragraphNanos=%lld\n",
                     monotonicNanos() - frameStartNanos,
                     drawNanos,
                     flushNanos,
                     metrics.paragraphCommands,
                     metrics.paragraphNanos);
        return JNI_TRUE;
    }
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeRenderCommandDirectFrame
        (JNIEnv* env, jclass cls, jlong nativeOpsPtr, jlong metalTexturePtr,
         jint destinationX, jint destinationY, jint destinationWidth, jint destinationHeight,
         jint width, jint height, jlong frameTimeNanos, jobject commandBuffer, jint commandByteCount) {
    @autoreleasepool {
        if (metalTexturePtr == 0 || destinationWidth <= 0 || destinationHeight <= 0 ||
                width <= 0 || height <= 0 || commandBuffer == nullptr ||
                commandByteCount <= 0 || commandByteCount % static_cast<jint>(sizeof(jint)) != 0) {
            return JNI_FALSE;
        }

        jbyte* commandBytes = static_cast<jbyte*>(env->GetDirectBufferAddress(commandBuffer));
        if (commandBytes == nullptr) {
            return JNI_FALSE;
        }

        id<MTLTexture> texture = (__bridge id<MTLTexture>) reinterpret_cast<void*>(static_cast<uintptr_t>(metalTexturePtr));
        if (texture == nil || texture.device == nil) {
            return JNI_FALSE;
        }

        jsize commandCount = commandByteCount / static_cast<jint>(sizeof(jint));
        const long long frameStartNanos = monotonicNanos();

        sk_sp<GrDirectContext> directContext = makeDirectContextForSurface(nativeOpsPtr, texture);
        if (directContext == nullptr) {
            return JNI_FALSE;
        }

        sk_sp<SkSurface> surface = wrapTextureSurface(
                directContext.get(),
                texture,
                static_cast<int>(texture.width),
                static_cast<int>(texture.height));
        if (surface == nullptr) {
            return JNI_FALSE;
        }

        SkCanvas* canvas = surface->getCanvas();
        CommandReplayMetrics metrics;
        const long long drawStartNanos = monotonicNanos();
        canvas->save();
        canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(destinationX),
                                          static_cast<SkScalar>(destinationY),
                                          static_cast<SkScalar>(destinationWidth),
                                          static_cast<SkScalar>(destinationHeight)));
        canvas->translate(static_cast<SkScalar>(destinationX),
                          static_cast<SkScalar>(destinationY));
        bool rendered = drawCommandList(canvas, LittleEndianByteCommandWords{commandBytes}, commandCount, width, height, &metrics);
        canvas->restore();
        const long long drawNanos = monotonicNanos() - drawStartNanos;
        if (!rendered) {
            return JNI_FALSE;
        }

        const long long flushStartNanos = monotonicNanos();
        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        const long long flushNanos = monotonicNanos() - flushStartNanos;
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_FRAME destinationX=%d destinationY=%d destinationWidth=%d destinationHeight=%d width=%d height=%d commands=%d rendered=true\n",
                     destinationX,
                     destinationY,
                     destinationWidth,
                     destinationHeight,
                     width,
                     height,
                     commandCount);
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_TIMING totalNanos=%lld drawNanos=%lld flushNanos=%lld paragraphCommands=%d paragraphNanos=%lld\n",
                     monotonicNanos() - frameStartNanos,
                     drawNanos,
                     flushNanos,
                     metrics.paragraphCommands,
                     metrics.paragraphNanos);
        return JNI_TRUE;
    }
}
