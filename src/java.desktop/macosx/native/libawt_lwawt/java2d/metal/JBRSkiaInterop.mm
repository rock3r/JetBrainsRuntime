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
#include <cstdio>
#include <mutex>
#include <unordered_map>

#include "SkBlendMode.h"
#include "SkCanvas.h"
#include "SkColor.h"
#include "SkColorSpace.h"
#include "SkData.h"
#include "SkPaint.h"
#include "SkPicture.h"
#include "SkRRect.h"
#include "SkSurface.h"
#include "ganesh/GrBackendSurface.h"
#include "ganesh/GrDirectContext.h"
#include "ganesh/mtl/GrMtlBackendContext.h"
#include "ganesh/mtl/GrMtlBackendSurface.h"
#include "ganesh/mtl/GrMtlDirectContext.h"
#include "ganesh/mtl/GrMtlTypes.h"
#include "include/gpu/ganesh/SkSurfaceGanesh.h"

#include "MTLSurfaceDataBase.h"

static constexpr jint ABI_ID = 12;
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

static std::mutex gDirectContextMutex;
static std::unordered_map<void*, sk_sp<GrDirectContext>> gDirectContextsByMtlContext;

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
static bool drawCommandList(SkCanvas* canvas, CommandWords commands, jsize commandCount, int width, int height) {
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
        canvas->save();
        canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(destinationX),
                                          static_cast<SkScalar>(destinationY),
                                          static_cast<SkScalar>(destinationWidth),
                                          static_cast<SkScalar>(destinationHeight)));
        canvas->translate(static_cast<SkScalar>(destinationX),
                          static_cast<SkScalar>(destinationY));
        bool rendered = drawCommandList(canvas, IntCommandWords{commands}, commandCount, width, height);
        canvas->restore();
        env->ReleaseIntArrayElements(commandArray, commands, JNI_ABORT);
        if (!rendered) {
            return JNI_FALSE;
        }

        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_FRAME destinationX=%d destinationY=%d destinationWidth=%d destinationHeight=%d width=%d height=%d commands=%d rendered=true\n",
                     destinationX,
                     destinationY,
                     destinationWidth,
                     destinationHeight,
                     width,
                     height,
                     commandCount);
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
        canvas->save();
        canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(destinationX),
                                          static_cast<SkScalar>(destinationY),
                                          static_cast<SkScalar>(destinationWidth),
                                          static_cast<SkScalar>(destinationHeight)));
        canvas->translate(static_cast<SkScalar>(destinationX),
                          static_cast<SkScalar>(destinationY));
        bool rendered = drawCommandList(canvas, LittleEndianByteCommandWords{commandBytes}, commandCount, width, height);
        canvas->restore();
        env->ReleaseByteArrayElements(commandArray, commandBytes, JNI_ABORT);
        if (!rendered) {
            return JNI_FALSE;
        }

        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_FRAME destinationX=%d destinationY=%d destinationWidth=%d destinationHeight=%d width=%d height=%d commands=%d rendered=true\n",
                     destinationX,
                     destinationY,
                     destinationWidth,
                     destinationHeight,
                     width,
                     height,
                     commandCount);
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
        bool rendered = drawCommandList(canvas, LittleEndianByteCommandWords{commandBytes}, commandCount, width, height);
        canvas->restore();
        if (!rendered) {
            return JNI_FALSE;
        }

        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_FRAME destinationX=%d destinationY=%d destinationWidth=%d destinationHeight=%d width=%d height=%d commands=%d rendered=true\n",
                     destinationX,
                     destinationY,
                     destinationWidth,
                     destinationHeight,
                     width,
                     height,
                     commandCount);
        return JNI_TRUE;
    }
}
