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
#include "SkPaint.h"
#include "SkRRect.h"
#include "SkSurface.h"
#include "ganesh/GrBackendSurface.h"
#include "ganesh/GrDirectContext.h"
#include "ganesh/mtl/GrMtlBackendContext.h"
#include "ganesh/mtl/GrMtlBackendSurface.h"
#include "ganesh/mtl/GrMtlDirectContext.h"
#include "ganesh/mtl/GrMtlTypes.h"
#include "include/gpu/ganesh/SkSurfaceGanesh.h"

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

extern "C" JNIEXPORT jboolean JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeRenderDiagnosticFrame
        (JNIEnv* env, jclass cls, jlong metalTexturePtr, jint width, jint height, jlong frameTimeNanos) {
    @autoreleasepool {
        if (metalTexturePtr == 0 || width <= 0 || height <= 0) {
            return JNI_FALSE;
        }

        id<MTLTexture> texture = (__bridge id<MTLTexture>) reinterpret_cast<void*>(static_cast<uintptr_t>(metalTexturePtr));
        if (texture == nil || texture.device == nil) {
            return JNI_FALSE;
        }

        id<MTLCommandQueue> queue = [texture.device newCommandQueue];
        if (queue == nil) {
            return JNI_FALSE;
        }

        GrMtlBackendContext backendContext = {};
        backendContext.fDevice.retain((__bridge GrMTLHandle) texture.device);
        backendContext.fQueue.retain((__bridge GrMTLHandle) queue);
        sk_sp<GrDirectContext> directContext = GrDirectContexts::MakeMetal(backendContext);
        [queue release];
        if (directContext == nullptr) {
            return JNI_FALSE;
        }

        GrMtlTextureInfo textureInfo;
        textureInfo.fTexture.retain((__bridge GrMTLHandle) texture);
        GrBackendRenderTarget renderTarget = GrBackendRenderTargets::MakeMtl(width, height, textureInfo);
        sk_sp<SkSurface> surface = SkSurfaces::WrapBackendRenderTarget(
                directContext.get(),
                renderTarget,
                kTopLeft_GrSurfaceOrigin,
                kBGRA_8888_SkColorType,
                nullptr,
                nullptr);
        if (surface == nullptr) {
            return JNI_FALSE;
        }

        drawDiagnosticPattern(surface->getCanvas(), width, height, frameTimeNanos);
        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        return JNI_TRUE;
    }
}
