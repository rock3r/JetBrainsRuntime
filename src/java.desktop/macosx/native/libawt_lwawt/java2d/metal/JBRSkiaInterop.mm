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
#include <functional>
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
#include "SkFontMgr.h"
#include "SkPaint.h"
#include "SkPath.h"
#include "SkPathBuilder.h"
#include "SkPicture.h"
#include "SkPixmap.h"
#include "SkRRect.h"
#include "SkSamplingOptions.h"
#include "SkString.h"
#include "SkSurface.h"
#include "SkTileMode.h"
#include "ganesh/GrBackendSurface.h"
#include "ganesh/GrDirectContext.h"
#include "ganesh/mtl/GrMtlBackendContext.h"
#include "ganesh/mtl/GrMtlBackendSurface.h"
#include "ganesh/mtl/GrMtlDirectContext.h"
#include "ganesh/mtl/GrMtlTypes.h"
#include "include/gpu/ganesh/SkSurfaceGanesh.h"
#include "include/effects/SkGradient.h"
#include "modules/skparagraph/include/FontCollection.h"
#include "modules/skparagraph/include/Paragraph.h"
#include "modules/skparagraph/include/ParagraphBuilder.h"
#include "modules/skparagraph/include/ParagraphStyle.h"
#include "modules/skparagraph/include/TextStyle.h"
#include "modules/skunicode/include/SkUnicode_icu.h"
#include "ports/SkFontMgr_mac_ct.h"

#include "MTLSurfaceDataBase.h"

static constexpr jint ABI_ID = 48;
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
static constexpr jint COMMAND_CLIP_PATH = 20;
static constexpr jint COMMAND_DRAW_PATH = 21;
static constexpr jint COMMAND_DRAW_ARC = 22;
static constexpr jint COMMAND_DRAW_ROUND_RECT = 23;
static constexpr jint COMMAND_FILL_RECT_LINEAR_GRADIENT = 24;
static constexpr jint COMMAND_FILL_ROUND_RECT_LINEAR_GRADIENT = 25;
static constexpr jint COMMAND_FILL_RECT_RADIAL_GRADIENT = 26;
static constexpr jint COMMAND_FILL_ROUND_RECT_RADIAL_GRADIENT = 27;
static constexpr jint COMMAND_FILL_PATH_LINEAR_GRADIENT = 28;
static constexpr jint COMMAND_FILL_PATH_RADIAL_GRADIENT = 29;
static constexpr jint COMMAND_FILL_RECT_SWEEP_GRADIENT = 30;
static constexpr jint COMMAND_FILL_ROUND_RECT_SWEEP_GRADIENT = 31;
static constexpr jint COMMAND_FILL_PATH_SWEEP_GRADIENT = 32;
static constexpr jint COMMAND_EVICT_IMAGE_CACHE_KEY = 33;
static constexpr jint COMMAND_FILL_RECT_IMAGE_SHADER = 34;
static constexpr jint COMMAND_STROKE_RECT_LINEAR_GRADIENT = 35;
static constexpr jint COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT = 36;
static constexpr jint COMMAND_STROKE_RECT_RADIAL_GRADIENT = 37;
static constexpr jint COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT = 38;
static constexpr jint COMMAND_PAINT_STYLE_FILL = 0;
static constexpr jint COMMAND_PAINT_STYLE_STROKE = 1;
static constexpr jint COMMAND_PATH_FILL_NON_ZERO = 0;
static constexpr jint COMMAND_PATH_FILL_EVEN_ODD = 1;
static constexpr jint COMMAND_PATH_VERB_MOVE = 0;
static constexpr jint COMMAND_PATH_VERB_LINE = 1;
static constexpr jint COMMAND_PATH_VERB_QUAD = 2;
static constexpr jint COMMAND_PATH_VERB_CUBIC = 3;
static constexpr jint COMMAND_PATH_VERB_CLOSE = 4;

static std::mutex gDirectContextMutex;
static std::unordered_map<void*, sk_sp<GrDirectContext>> gDirectContextsByMtlContext;

struct ImageCacheScopedKey {
    void* context;
    uint64_t image;

    bool operator==(const ImageCacheScopedKey& other) const {
        return context == other.context && image == other.image;
    }
};

struct ImageCacheScopedKeyHash {
    size_t operator()(const ImageCacheScopedKey& key) const {
        size_t contextHash = std::hash<void*>{}(key.context);
        size_t imageHash = std::hash<uint64_t>{}(key.image);
        return contextHash ^ (imageHash + 0x9e3779b97f4a7c15ULL + (contextHash << 6) + (contextHash >> 2));
    }
};

static std::mutex gImageCacheMutex;
static std::unordered_map<ImageCacheScopedKey, sk_sp<SkImage>, ImageCacheScopedKeyHash> gImagesByKey;
static std::mutex gParagraphDependenciesMutex;
static sk_sp<skia::textlayout::FontCollection> gParagraphFontCollection;
static sk_sp<SkUnicode> gParagraphUnicode;
static sk_sp<SkFontMgr> gCoreTextFontMgr;

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

static sk_sp<SkFontMgr> coreTextFontMgr() {
    std::scoped_lock lock(gParagraphDependenciesMutex);
    if (gCoreTextFontMgr == nullptr) {
        gCoreTextFontMgr = SkFontMgr_New_CoreText(nullptr);
    }
    return gCoreTextFontMgr;
}

static sk_sp<skia::textlayout::FontCollection> paragraphFontCollection() {
    sk_sp<SkFontMgr> fontMgr = coreTextFontMgr();
    std::scoped_lock lock(gParagraphDependenciesMutex);
    if (gParagraphFontCollection == nullptr) {
        auto fontCollection = sk_make_sp<skia::textlayout::FontCollection>();
        fontCollection->setDefaultFontManager(fontMgr);
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

static SkTileMode skTileModeFromCommand(jint tileMode) {
    if (tileMode == 1) return SkTileMode::kRepeat;
    if (tileMode == 2) return SkTileMode::kMirror;
    if (tileMode == 3) return SkTileMode::kDecal;
    return SkTileMode::kClamp;
}

static jsize recordLengthFromBytes(jint recordByteLength) {
    if (recordByteLength < COMMAND_RECORD_HEADER_SIZE_BYTES || recordByteLength % static_cast<jint>(sizeof(jint)) != 0) {
        return -1;
    }
    return recordByteLength / static_cast<jint>(sizeof(jint));
}

template <typename CommandWords>
static bool pathFromCommandData(CommandWords commands, jsize offset, jsize recordEnd, jint fillType, SkPath* path) {
    SkPathBuilder builder(fillType == COMMAND_PATH_FILL_EVEN_ODD ? SkPathFillType::kEvenOdd : SkPathFillType::kWinding);
    while (offset < recordEnd) {
        jint verb = commands[offset++];
        if (verb == COMMAND_PATH_VERB_MOVE) {
            if (offset + 2 > recordEnd) return false;
            SkScalar x = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar y = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            builder.moveTo(x, y);
        } else if (verb == COMMAND_PATH_VERB_LINE) {
            if (offset + 2 > recordEnd) return false;
            SkScalar x = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar y = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            builder.lineTo(x, y);
        } else if (verb == COMMAND_PATH_VERB_QUAD) {
            if (offset + 4 > recordEnd) return false;
            SkScalar x1 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar y1 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar x2 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar y2 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            builder.quadTo(x1, y1, x2, y2);
        } else if (verb == COMMAND_PATH_VERB_CUBIC) {
            if (offset + 6 > recordEnd) return false;
            SkScalar x1 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar y1 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar x2 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar y2 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar x3 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar y3 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            builder.cubicTo(x1, y1, x2, y2, x3, y3);
        } else if (verb == COMMAND_PATH_VERB_CLOSE) {
            builder.close();
        } else {
            return false;
        }
    }
    if (offset != recordEnd) {
        return false;
    }
    *path = builder.detach();
    return true;
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

static int clearImageCacheForContext(void* contextKey) {
    int cleared = 0;
    for (auto it = gImagesByKey.begin(); it != gImagesByKey.end();) {
        if (it->first.context == contextKey) {
            it = gImagesByKey.erase(it);
            cleared++;
        } else {
            ++it;
        }
    }
    return cleared;
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
                            void* imageCacheContextKey,
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
                int cleared = clearImageCacheForContext(imageCacheContextKey);
                std::fprintf(stderr,
                             "JBR_SKIA_INTEROP_IMAGE_CACHE_CLEAR backend=native contextId=%p cleared=%d\n",
                             imageCacheContextKey,
                             cleared);
                break;
            }
            case COMMAND_EVICT_IMAGE_CACHE_KEY: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) {
                    return false;
                }
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                size_t removed;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    removed = gImagesByKey.erase(ImageCacheScopedKey{imageCacheContextKey, key});
                }
                std::fprintf(stderr,
                             "JBR_SKIA_INTEROP_IMAGE_CACHE_EVICT backend=native contextId=%p key=0x%016llx removed=%s\n",
                             imageCacheContextKey,
                             static_cast<unsigned long long>(key),
                             removed > 0 ? "true" : "false");
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
            case COMMAND_CLIP_PATH: {
                if (offset + 3 > recordEnd) {
                    return false;
                }
                jint clipOp = commands[offset++];
                jint fillType = commands[offset++];
                jint pathDataLength = commands[offset++];
                if ((clipOp != COMMAND_CLIP_OP_INTERSECT && clipOp != COMMAND_CLIP_OP_DIFFERENCE) ||
                        (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                        pathDataLength < 0 ||
                        offset + pathDataLength != recordEnd) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, recordEnd, fillType, &path)) {
                    return false;
                }
                offset = recordEnd;
                canvas->clipPath(path,
                                 clipOp == COMMAND_CLIP_OP_DIFFERENCE ? SkClipOp::kDifference : SkClipOp::kIntersect,
                                 antiAlias);
                break;
            }
            case COMMAND_DRAW_PATH: {
                if (offset + 8 > recordEnd) {
                    return false;
                }
                const jint paintStyle = commands[offset++];
                const jint argb = commands[offset++];
                const jint strokeWidth = commands[offset++];
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                const jint fillType = commands[offset++];
                const jint pathDataLength = commands[offset++];
                if ((paintStyle != COMMAND_PAINT_STYLE_FILL && paintStyle != COMMAND_PAINT_STYLE_STROKE) ||
                        (paintStyle == COMMAND_PAINT_STYLE_STROKE && !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000)) ||
                        (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                        pathDataLength < 0 ||
                        offset + pathDataLength != recordEnd) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, recordEnd, fillType, &path)) {
                    return false;
                }
                offset = recordEnd;
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(argb));
                if (paintStyle == COMMAND_PAINT_STYLE_STROKE) {
                    paint.setStyle(SkPaint::kStroke_Style);
                    paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                    paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                    paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                    paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                }
                canvas->drawPath(path, paint);
                break;
            }
            case COMMAND_FILL_PATH_LINEAR_GRADIENT: {
                if (offset + 2 > recordEnd) {
                    return false;
                }
                const jint fillType = commands[offset++];
                const jint pathDataLength = commands[offset++];
                const jsize pathEnd = offset + pathDataLength;
                if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                        pathDataLength < 0 ||
                        pathDataLength > 4096 ||
                        pathEnd + 10 > recordEnd) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, pathEnd, fillType, &path)) {
                    return false;
                }
                offset = pathEnd;
                SkPoint points[2] = {
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f},
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f}
                };
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::LinearGradient(points, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawPath(path, paint);
                break;
            }
            case COMMAND_FILL_PATH_RADIAL_GRADIENT: {
                if (offset + 2 > recordEnd) {
                    return false;
                }
                const jint fillType = commands[offset++];
                const jint pathDataLength = commands[offset++];
                const jsize pathEnd = offset + pathDataLength;
                if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                        pathDataLength < 0 ||
                        pathDataLength > 4096 ||
                        pathEnd + 9 > recordEnd) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, pathEnd, fillType, &path)) {
                    return false;
                }
                offset = pathEnd;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const SkScalar radius = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (radius <= 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::RadialGradient(center, radius, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawPath(path, paint);
                break;
            }
            case COMMAND_FILL_PATH_SWEEP_GRADIENT: {
                if (offset + 2 > recordEnd) {
                    return false;
                }
                const jint fillType = commands[offset++];
                const jint pathDataLength = commands[offset++];
                const jsize pathEnd = offset + pathDataLength;
                if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                        pathDataLength < 0 ||
                        pathDataLength > 4096 ||
                        pathEnd + 7 > recordEnd) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, pathEnd, fillType, &path)) {
                    return false;
                }
                offset = pathEnd;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const jint colorCount = commands[offset++];
                if (colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                SkTileMode::kClamp),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::SweepGradient(center, 0.0f, 360.0f, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawPath(path, paint);
                break;
            }
            case COMMAND_DRAW_ARC: {
                if (offset + 13 != recordEnd) {
                    return false;
                }
                const jint paintStyle = commands[offset++];
                const jint argb = commands[offset++];
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar startAngle = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar sweepAngle = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint useCenter = commands[offset++];
                const jint strokeWidth = commands[offset++];
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                if ((paintStyle != COMMAND_PAINT_STYLE_FILL && paintStyle != COMMAND_PAINT_STYLE_STROKE) ||
                        right < left ||
                        bottom < top ||
                        (useCenter != 0 && useCenter != 1) ||
                        (paintStyle == COMMAND_PAINT_STYLE_STROKE && !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000))) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(argb));
                if (paintStyle == COMMAND_PAINT_STYLE_STROKE) {
                    paint.setStyle(SkPaint::kStroke_Style);
                    paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                    paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                    paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                    paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                }
                canvas->drawArc(SkRect::MakeLTRB(left, top, right, bottom),
                                startAngle,
                                sweepAngle,
                                useCenter == 1,
                                paint);
                break;
            }
            case COMMAND_DRAW_ROUND_RECT: {
                if (offset + 12 != recordEnd) {
                    return false;
                }
                const jint paintStyle = commands[offset++];
                const jint argb = commands[offset++];
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeWidth = commands[offset++];
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                if ((paintStyle != COMMAND_PAINT_STYLE_FILL && paintStyle != COMMAND_PAINT_STYLE_STROKE) ||
                        right < left ||
                        bottom < top ||
                        radiusX < 0 ||
                        radiusY < 0 ||
                        (paintStyle == COMMAND_PAINT_STYLE_STROKE && !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000))) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(argb));
                if (paintStyle == COMMAND_PAINT_STYLE_STROKE) {
                    paint.setStyle(SkPaint::kStroke_Style);
                    paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                    paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                    paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                    paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                }
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                break;
            }
            case COMMAND_FILL_RECT_LINEAR_GRADIENT: {
                if (offset + 10 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkPoint points[2] = {
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f},
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f}
                };
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::LinearGradient(points, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_STROKE_RECT_LINEAR_GRADIENT: {
                if (offset + 14 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                SkPoint points[2] = {
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f},
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f}
                };
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || strokeWidth <= 0.0f ||
                        strokeCap < 0 || strokeCap > 2 || strokeJoin < 0 || strokeJoin > 2 ||
                        strokeMiter1000 < 0 || tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                const SkScalar strokeMiter = static_cast<SkScalar>(strokeMiter1000) / 1000.0f;
                paint.setStrokeMiter(strokeMiter < 1.0f ? 1.0f : strokeMiter);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::LinearGradient(points, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT: {
                if (offset + 16 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                SkPoint points[2] = {
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f},
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f}
                };
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || radiusX < 0 || radiusY < 0 ||
                        strokeWidth <= 0.0f || strokeCap < 0 || strokeCap > 2 ||
                        strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                const SkScalar strokeMiter = static_cast<SkScalar>(strokeMiter1000) / 1000.0f;
                paint.setStrokeMiter(strokeMiter < 1.0f ? 1.0f : strokeMiter);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::LinearGradient(points, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                break;
            }
            case COMMAND_FILL_ROUND_RECT_LINEAR_GRADIENT: {
                if (offset + 12 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkPoint points[2] = {
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f},
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f}
                };
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || radiusX < 0 || radiusY < 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::LinearGradient(points, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                break;
            }
            case COMMAND_FILL_RECT_RADIAL_GRADIENT: {
                if (offset + 9 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const SkScalar radius = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || radius <= 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::RadialGradient(center, radius, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_STROKE_RECT_RADIAL_GRADIENT: {
                if (offset + 13 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const SkScalar strokeMiter = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const SkScalar radius = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top ||
                        strokeWidth <= 0 || strokeCap < 0 || strokeCap > 2 ||
                        strokeJoin < 0 || strokeJoin > 2 || strokeMiter < 0 ||
                        radius <= 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(std::max<SkScalar>(1.0f, strokeMiter));
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::RadialGradient(center, radius, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT: {
                if (offset + 15 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const SkScalar strokeMiter = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const SkScalar radius = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || radiusX < 0 || radiusY < 0 ||
                        strokeWidth <= 0 || strokeCap < 0 || strokeCap > 2 ||
                        strokeJoin < 0 || strokeJoin > 2 || strokeMiter < 0 ||
                        radius <= 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(std::max<SkScalar>(1.0f, strokeMiter));
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::RadialGradient(center, radius, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                break;
            }
            case COMMAND_FILL_RECT_SWEEP_GRADIENT: {
                if (offset + 7 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                SkTileMode::kClamp),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::SweepGradient(center, 0.0f, 360.0f, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_FILL_ROUND_RECT_SWEEP_GRADIENT: {
                if (offset + 9 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || radiusX < 0 || radiusY < 0 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                SkTileMode::kClamp),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::SweepGradient(center, 0.0f, 360.0f, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                break;
            }
            case COMMAND_FILL_ROUND_RECT_RADIAL_GRADIENT: {
                if (offset + 11 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const SkScalar radius = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || radiusX < 0 || radiusY < 0 || radius <= 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::RadialGradient(center, radius, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
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
                    gImagesByKey[ImageCacheScopedKey{imageCacheContextKey, key}] = image;
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
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
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
            case COMMAND_FILL_RECT_IMAGE_SHADER: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 11 != recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint imageWidth = commands[offset++];
                const jint imageHeight = commands[offset++];
                const jint tileModeX = commands[offset++];
                const jint tileModeY = commands[offset++];
                const jint alpha1000 = commands[offset++];
                if (right < left || bottom < top ||
                        imageWidth <= 0 || imageHeight <= 0 || imageWidth > 4096 || imageHeight > 4096 ||
                        tileModeX < 0 || tileModeX > 3 || tileModeY < 0 || tileModeY > 3 ||
                        alpha1000 < 0 || alpha1000 > 1000) {
                    return false;
                }
                sk_sp<SkImage> image;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found == gImagesByKey.end()) {
                        return false;
                    }
                    image = found->second;
                }
                if (image->width() != imageWidth || image->height() != imageHeight) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setAlphaf(static_cast<float>(alpha1000) / 1000.0f);
                paint.setShader(image->makeShader(
                        skTileModeFromCommand(tileModeX),
                        skTileModeFromCommand(tileModeY),
                        SkSamplingOptions(SkFilterMode::kLinear, SkMipmapMode::kNone),
                        nullptr));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
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
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 6 > recordEnd) {
                    return false;
                }
                const SkScalar x = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar baseline = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar fontSize = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkColor color = skColorFromArgb(commands[offset++]);
                const jint familyCharCount = commands[offset++];
                if (familyCharCount < 0 || familyCharCount > 256 || offset + familyCharCount >= recordEnd) {
                    return false;
                }
                std::string fontFamily;
                if (!appendUtf16CommandText(fontFamily, commands, offset, familyCharCount)) {
                    return false;
                }
                const jint charCount = commands[offset++];
                if (fontSize <= 0.0f || charCount < 0 || charCount > 4096 || offset + charCount != recordEnd) {
                    return false;
                }
                std::string text;
                if (!appendUtf16CommandText(text, commands, offset, charCount)) {
                    return false;
                }
                sk_sp<SkTypeface> typeface;
                if (!fontFamily.empty()) {
                    typeface = coreTextFontMgr()->matchFamilyStyle(fontFamily.c_str(), SkFontStyle());
                }
                SkFont font(typeface, fontSize);
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
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 7 > recordEnd) {
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
                const jint familyCharCount = commands[offset++];
                if (familyCharCount < 0 || familyCharCount > 256 || offset + familyCharCount + 10 > recordEnd) {
                    return false;
                }
                std::string fontFamily;
                if (!appendUtf16CommandText(fontFamily, commands, offset, familyCharCount)) {
                    return false;
                }
                const jint textAlign = commands[offset++];
                const jint textDirection = commands[offset++];
                const jint lineHeightMultiplier1000 = commands[offset++];
                const jint maxLines = commands[offset++];
                const jint ellipsisMode = commands[offset++];
                const jint decorationMask = commands[offset++];
                const jint letterSpacing1000 = commands[offset++];
                const jint backgroundSpecified = commands[offset++];
                const SkColor backgroundColor = skColorFromArgb(commands[offset++]);
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
                        decorationMask < 0 || decorationMask > 3 ||
                        letterSpacing1000 < -100000 || letterSpacing1000 > 100000 ||
                        backgroundSpecified < 0 || backgroundSpecified > 1 ||
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
                if (backgroundSpecified == 1) {
                    SkPaint backgroundPaint;
                    backgroundPaint.setColor(backgroundColor);
                    textStyle.setBackgroundPaint(backgroundPaint);
                }
                textStyle.setFontStyle(SkFontStyle(
                        fontWeight,
                        fontWidth,
                        static_cast<SkFontStyle::Slant>(fontSlant)));
                if (!fontFamily.empty()) {
                    std::vector<SkString> fontFamilies;
                    fontFamilies.emplace_back(fontFamily.c_str());
                    textStyle.setFontFamilies(fontFamilies);
                }
                if (lineHeightMultiplier1000 > 0) {
                    textStyle.setHeight(static_cast<SkScalar>(lineHeightMultiplier1000) / 1000.0f);
                    textStyle.setHeightOverride(true);
                }
                if (letterSpacing1000 != 0) {
                    textStyle.setLetterSpacing(static_cast<SkScalar>(letterSpacing1000) / 1000.0f);
                }
                skia::textlayout::TextDecoration decoration = skia::textlayout::TextDecoration::kNoDecoration;
                if ((decorationMask & 1) != 0) {
                    decoration = static_cast<skia::textlayout::TextDecoration>(
                            decoration | skia::textlayout::TextDecoration::kUnderline);
                }
                if ((decorationMask & 2) != 0) {
                    decoration = static_cast<skia::textlayout::TextDecoration>(
                            decoration | skia::textlayout::TextDecoration::kLineThrough);
                }
                if (decoration != skia::textlayout::TextDecoration::kNoDecoration) {
                    textStyle.setDecoration(decoration);
                    textStyle.setDecorationColor(color);
                    textStyle.setDecorationStyle(skia::textlayout::TextDecorationStyle::kSolid);
                    textStyle.setDecorationThicknessMultiplier(1.0f);
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

extern "C" JNIEXPORT jlong JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeGetContextId
        (JNIEnv* env, jclass cls, jlong nativeOpsPtr) {
    MTLContext* mtlc = getContextFromNativeOps(nativeOpsPtr);
    return mtlc == nil ? 0 : static_cast<jlong>(reinterpret_cast<uintptr_t>((__bridge void*) mtlc));
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
        bool rendered = drawCommandList(canvas, IntCommandWords{commands}, commandCount, width, height,
                                        (__bridge void*) getContextFromNativeOps(nativeOpsPtr), &metrics);
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
        bool rendered = drawCommandList(canvas, LittleEndianByteCommandWords{commandBytes}, commandCount,
                                        width, height, (__bridge void*) getContextFromNativeOps(nativeOpsPtr), &metrics);
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
        bool rendered = drawCommandList(canvas, LittleEndianByteCommandWords{commandBytes}, commandCount,
                                        width, height, (__bridge void*) getContextFromNativeOps(nativeOpsPtr), &metrics);
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
