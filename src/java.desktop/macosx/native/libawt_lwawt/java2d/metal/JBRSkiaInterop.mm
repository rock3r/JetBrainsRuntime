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

static constexpr jint COMMAND_CLEAR = 1;
static constexpr jint COMMAND_FILL_RECT = 2;
static constexpr jint COMMAND_STROKE_LINE = 3;

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

static bool drawCommandList(SkCanvas* canvas, const jint* commands, jsize commandCount, int width, int height) {
    jsize offset = 0;
    while (offset < commandCount) {
        jint op = commands[offset++];
        switch (op) {
            case COMMAND_CLEAR: {
                if (offset + 1 > commandCount) {
                    return false;
                }
                SkPaint paint;
                paint.setColor(skColorFromArgb(commands[offset++]));
                canvas->drawRect(SkRect::MakeWH(static_cast<SkScalar>(width), static_cast<SkScalar>(height)), paint);
                break;
            }
            case COMMAND_FILL_RECT: {
                if (offset + 6 > commandCount) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(true);
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
            case COMMAND_STROKE_LINE: {
                if (offset + 6 > commandCount) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(true);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x1 = commands[offset++];
                jint y1 = commands[offset++];
                jint x2 = commands[offset++];
                jint y2 = commands[offset++];
                paint.setStrokeWidth(static_cast<SkScalar>(std::max(1, commands[offset++])));
                canvas->drawLine(static_cast<SkScalar>(x1),
                                 static_cast<SkScalar>(y1),
                                 static_cast<SkScalar>(x2),
                                 static_cast<SkScalar>(y2),
                                 paint);
                break;
            }
            default:
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

    GrMtlBackendContext backendContext = {};
    backendContext.fDevice.retain((__bridge GrMTLHandle) mtlc.device);
    backendContext.fQueue.retain((__bridge GrMTLHandle) mtlc.commandQueue);
    return GrDirectContexts::MakeMetal(backendContext);
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
         jint width, jint height, jlong frameTimeNanos, jbyteArray pictureArray) {
    @autoreleasepool {
        if (metalTexturePtr == 0 || width <= 0 || height <= 0 || pictureArray == nullptr) {
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

        sk_sp<SkSurface> surface = wrapTextureSurface(directContext.get(), texture, width, height);
        if (surface == nullptr) {
            return JNI_FALSE;
        }

        picture->playback(surface->getCanvas());
        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        return JNI_TRUE;
    }
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeRenderCommandFrame
        (JNIEnv* env, jclass cls, jlong nativeOpsPtr, jlong metalTexturePtr,
         jint width, jint height, jlong frameTimeNanos, jintArray commandArray) {
    @autoreleasepool {
        if (metalTexturePtr == 0 || width <= 0 || height <= 0 || commandArray == nullptr) {
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

        sk_sp<SkSurface> surface = wrapTextureSurface(directContext.get(), texture, width, height);
        if (surface == nullptr) {
            return JNI_FALSE;
        }

        jboolean isCopy = JNI_FALSE;
        jint* commands = env->GetIntArrayElements(commandArray, &isCopy);
        if (commands == nullptr) {
            return JNI_FALSE;
        }

        bool rendered = drawCommandList(surface->getCanvas(), commands, commandCount, width, height);
        env->ReleaseIntArrayElements(commandArray, commands, JNI_ABORT);
        if (!rendered) {
            return JNI_FALSE;
        }

        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        return JNI_TRUE;
    }
}
