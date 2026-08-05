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

#include <windows.h>
#include <dwmapi.h>
#include <stdlib.h>

#include "jni.h"

/*
 * Composition-aligned pacing clock: reads DWM composition timing and waits to
 * the next vblank boundary with a high-resolution waitable timer, delivering
 * ticks to sun.awt.FramePacingWin.DwmClock.onNativeTick from a
 * daemon-attached native thread. Deliberately never calls DwmFlush:
 * in-process it can block for hundreds of milliseconds when the client gates
 * presents on the same tick.
 *
 * DWM composition timing follows the primary display cadence; QPC timestamps
 * are converted to the System.nanoTime() time base (also QPC derived).
 */

typedef HRESULT (WINAPI *DwmGetCompositionTimingInfoType)(HWND, DWM_TIMING_INFO *);
typedef HRESULT (WINAPI *DwmIsCompositionEnabledType)(BOOL *);

static DwmGetCompositionTimingInfoType pDwmGetCompositionTimingInfo = NULL;
static DwmIsCompositionEnabledType pDwmIsCompositionEnabled = NULL;
static JavaVM *jvm = NULL;
static jmethodID onNativeTickMID = NULL;
static LONGLONG qpcFrequency = 0;

typedef struct {
    HANDLE thread;
    HANDLE stopEvent;
    jobject clockRef;
    jlong fallbackPeriodNanos;
} FramePacingClock;

static BOOL loadDwm()
{
    if (pDwmGetCompositionTimingInfo != NULL) {
        return TRUE;
    }
    HMODULE dwm = LoadLibraryW(L"dwmapi.dll");
    if (dwm == NULL) {
        return FALSE;
    }
    pDwmIsCompositionEnabled =
            (DwmIsCompositionEnabledType)GetProcAddress(dwm, "DwmIsCompositionEnabled");
    pDwmGetCompositionTimingInfo =
            (DwmGetCompositionTimingInfoType)GetProcAddress(dwm, "DwmGetCompositionTimingInfo");
    return pDwmGetCompositionTimingInfo != NULL;
}

static DWORD WINAPI pacingThreadProc(LPVOID param)
{
    FramePacingClock *clock = (FramePacingClock *)param;
    JNIEnv *env;
    if (jvm->AttachCurrentThreadAsDaemon((void **)&env, NULL) != JNI_OK) {
        return 0;
    }

    HANDLE timer = CreateWaitableTimerExW(NULL, NULL,
            CREATE_WAITABLE_TIMER_HIGH_RESOLUTION, TIMER_ALL_ACCESS);
    if (timer == NULL) {
        timer = CreateWaitableTimerExW(NULL, NULL, 0, TIMER_ALL_ACCESS);
    }
    if (timer == NULL) {
        return 0;
    }
    HANDLE handles[2] = { clock->stopEvent, timer };

    LONGLONG fallbackQpc = clock->fallbackPeriodNanos * qpcFrequency / 1000000000LL;
    if (fallbackQpc <= 0) {
        fallbackQpc = qpcFrequency / 60;
    }

    for (;;) {
        LARGE_INTEGER now;
        QueryPerformanceCounter(&now);

        LONGLONG waitQpc;
        DWM_TIMING_INFO timing;
        ZeroMemory(&timing, sizeof(timing));
        timing.cbSize = sizeof(timing);
        if (pDwmGetCompositionTimingInfo(NULL, &timing) == S_OK &&
                timing.qpcRefreshPeriod > 0) {
            // Wait to the next vblank boundary after "now".
            LONGLONG period = (LONGLONG)timing.qpcRefreshPeriod;
            LONGLONG sinceVBlank = now.QuadPart - (LONGLONG)timing.qpcVBlank;
            LONGLONG intoPeriod = sinceVBlank % period;
            if (intoPeriod < 0) {
                intoPeriod += period;
            }
            waitQpc = period - intoPeriod;
        } else {
            // DWM unavailable mid-run: keep an estimated cadence.
            waitQpc = fallbackQpc;
        }

        LARGE_INTEGER due;
        due.QuadPart = -(waitQpc * 10000000LL / qpcFrequency); // relative, 100 ns
        if (due.QuadPart >= 0) {
            due.QuadPart = -1;
        }
        SetWaitableTimer(timer, &due, 0, NULL, NULL, FALSE);
        DWORD waited = WaitForMultipleObjects(2, handles, FALSE, INFINITE);
        if (waited != WAIT_OBJECT_0 + 1) {
            break; // stop event or failure
        }

        LARGE_INTEGER tickTime;
        QueryPerformanceCounter(&tickTime);
        jlong nanos = (jlong)((double)tickTime.QuadPart * 1000000000.0 / (double)qpcFrequency);
        env->CallVoidMethod(clock->clockRef, onNativeTickMID, nanos);
        if (env->ExceptionCheck()) {
            env->ExceptionClear();
        }
    }

    CloseHandle(timer);
    return 0;
}

extern "C" {

JNIEXPORT jboolean JNICALL
Java_sun_awt_FramePacingWin_nativeProbe(JNIEnv *env, jclass cls)
{
    if (!loadDwm()) {
        return JNI_FALSE;
    }
    BOOL enabled = FALSE;
    if (pDwmIsCompositionEnabled != NULL &&
            pDwmIsCompositionEnabled(&enabled) == S_OK && !enabled) {
        return JNI_FALSE;
    }
    DWM_TIMING_INFO timing;
    ZeroMemory(&timing, sizeof(timing));
    timing.cbSize = sizeof(timing);
    return (pDwmGetCompositionTimingInfo(NULL, &timing) == S_OK &&
            timing.qpcRefreshPeriod > 0) ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jlong JNICALL
Java_sun_awt_FramePacingWin_nativeCreate(JNIEnv *env, jclass cls,
                                         jobject clockObj, jlong fallbackPeriodNanos)
{
    if (!loadDwm()) {
        return 0;
    }
    if (jvm == NULL) {
        if (env->GetJavaVM(&jvm) != JNI_OK) {
            return 0;
        }
        LARGE_INTEGER freq;
        QueryPerformanceFrequency(&freq);
        qpcFrequency = freq.QuadPart;
    }
    if (onNativeTickMID == NULL) {
        jclass clockClass = env->GetObjectClass(clockObj);
        onNativeTickMID = env->GetMethodID(clockClass, "onNativeTick", "(J)V");
        if (onNativeTickMID == NULL) {
            env->ExceptionClear();
            return 0;
        }
    }

    FramePacingClock *clock = (FramePacingClock *)malloc(sizeof(FramePacingClock));
    if (clock == NULL) {
        return 0;
    }
    clock->thread = NULL;
    clock->stopEvent = CreateEventW(NULL, TRUE, FALSE, NULL);
    clock->fallbackPeriodNanos = fallbackPeriodNanos;
    if (clock->stopEvent == NULL) {
        free(clock);
        return 0;
    }
    clock->clockRef = env->NewGlobalRef(clockObj);
    if (clock->clockRef == NULL) {
        CloseHandle(clock->stopEvent);
        free(clock);
        return 0;
    }
    return (jlong)(intptr_t)clock;
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingWin_nativeStart(JNIEnv *env, jclass cls, jlong ptr)
{
    FramePacingClock *clock = (FramePacingClock *)(intptr_t)ptr;
    clock->thread = CreateThread(NULL, 0, pacingThreadProc, clock, 0, NULL);
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingWin_nativeStop(JNIEnv *env, jclass cls, jlong ptr)
{
    FramePacingClock *clock = (FramePacingClock *)(intptr_t)ptr;
    SetEvent(clock->stopEvent);
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingWin_nativeRelease(JNIEnv *env, jclass cls, jlong ptr)
{
    FramePacingClock *clock = (FramePacingClock *)(intptr_t)ptr;
    if (clock->thread != NULL) {
        // The thread exits promptly once the stop event is set (at most one
        // frame wait); bound the join so release stays effectively brief.
        WaitForSingleObject(clock->thread, 1000);
        CloseHandle(clock->thread);
    }
    CloseHandle(clock->stopEvent);
    env->DeleteGlobalRef(clock->clockRef);
    free(clock);
}

} // extern "C"
