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

#import <CoreVideo/CoreVideo.h>
#include <mach/mach_time.h>
#include <stdlib.h>

#include "jni.h"

/*
 * FramePacing-owned CVDisplayLink: one link per subscribed display, driving
 * sun.awt.FramePacingMac.DisplayLinkClock.onNativeTick from the display link
 * callback thread (attached as a daemon). Timestamps are the callback's host
 * time converted to the System.nanoTime() time base (both are
 * mach_absolute_time derived).
 */

typedef struct {
    CVDisplayLinkRef link;
    jobject clockRef;
} FramePacingLink;

static JavaVM *jvm = NULL;
static jmethodID onNativeTickMID = NULL;
static mach_timebase_info_data_t timebase;

static CVReturn framePacingCallback(CVDisplayLinkRef displayLink, const CVTimeStamp *inNow,
                                    const CVTimeStamp *inOutputTime, CVOptionFlags flagsIn,
                                    CVOptionFlags *flagsOut, void *ctx)
{
    FramePacingLink *fpl = (FramePacingLink *)ctx;
    JNIEnv *env;
    if ((*jvm)->AttachCurrentThreadAsDaemon(jvm, (void **)&env, NULL) != JNI_OK) {
        return kCVReturnSuccess;
    }
    jlong nanos = (jlong)(inNow->hostTime * timebase.numer / timebase.denom);
    (*env)->CallVoidMethod(env, fpl->clockRef, onNativeTickMID, nanos);
    if ((*env)->ExceptionCheck(env)) {
        (*env)->ExceptionClear(env);
    }
    return kCVReturnSuccess;
}

JNIEXPORT jboolean JNICALL
Java_sun_awt_FramePacingMac_nativeProbe(JNIEnv *env, jclass cls, jint displayID)
{
    CVDisplayLinkRef link = NULL;
    if (CVDisplayLinkCreateWithCGDisplay((CGDirectDisplayID)displayID, &link) != kCVReturnSuccess) {
        return JNI_FALSE;
    }
    CVDisplayLinkRelease(link);
    return JNI_TRUE;
}

JNIEXPORT jlong JNICALL
Java_sun_awt_FramePacingMac_nativeCreate(JNIEnv *env, jclass cls,
                                         jint displayID, jobject clock)
{
    if (jvm == NULL) {
        if ((*env)->GetJavaVM(env, &jvm) != JNI_OK) {
            return 0;
        }
        mach_timebase_info(&timebase);
    }
    if (onNativeTickMID == NULL) {
        jclass clockClass = (*env)->GetObjectClass(env, clock);
        onNativeTickMID = (*env)->GetMethodID(env, clockClass, "onNativeTick", "(J)V");
        if (onNativeTickMID == NULL) {
            (*env)->ExceptionClear(env);
            return 0;
        }
    }

    CVDisplayLinkRef link = NULL;
    if (CVDisplayLinkCreateWithCGDisplay((CGDirectDisplayID)displayID, &link) != kCVReturnSuccess) {
        return 0;
    }
    FramePacingLink *fpl = malloc(sizeof(FramePacingLink));
    if (fpl == NULL) {
        CVDisplayLinkRelease(link);
        return 0;
    }
    fpl->link = link;
    fpl->clockRef = (*env)->NewGlobalRef(env, clock);
    if (fpl->clockRef == NULL) {
        CVDisplayLinkRelease(link);
        free(fpl);
        return 0;
    }
    if (CVDisplayLinkSetOutputCallback(link, &framePacingCallback, fpl) != kCVReturnSuccess) {
        (*env)->DeleteGlobalRef(env, fpl->clockRef);
        CVDisplayLinkRelease(link);
        free(fpl);
        return 0;
    }
    return (jlong)(intptr_t)fpl;
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingMac_nativeStart(JNIEnv *env, jclass cls, jlong ptr)
{
    FramePacingLink *fpl = (FramePacingLink *)(intptr_t)ptr;
    CVDisplayLinkStart(fpl->link);
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingMac_nativeStop(JNIEnv *env, jclass cls, jlong ptr)
{
    FramePacingLink *fpl = (FramePacingLink *)(intptr_t)ptr;
    CVDisplayLinkStop(fpl->link);
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingMac_nativeRelease(JNIEnv *env, jclass cls, jlong ptr)
{
    FramePacingLink *fpl = (FramePacingLink *)(intptr_t)ptr;
    // CVDisplayLinkRelease waits for an in-flight callback to return, so the
    // global ref is safe to delete afterwards.
    CVDisplayLinkRelease(fpl->link);
    (*env)->DeleteGlobalRef(env, fpl->clockRef);
    free(fpl);
}
