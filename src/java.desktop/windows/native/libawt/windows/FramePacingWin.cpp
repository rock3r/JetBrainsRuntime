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

#include "awt.h"

#include <dwmapi.h>
#include <dxgi.h>
#include <stdlib.h>
#include <wchar.h>

#include "awt_Win32GraphicsDevice.h"
#include "jni.h"

/*
 * Pacing clocks, delivering ticks to sun.awt.FramePacingWin.NativeClock.onNativeTick
 * from a daemon-attached native thread. QPC timestamps are converted to the
 * System.nanoTime() time base (also QPC derived).
 *
 * Preferred source is IDXGIOutput::WaitForVBlank on the output belonging to the
 * clock's display: a genuine per-display hardware vblank, so each display is
 * paced at its own true refresh rate.
 *
 * Fallback source is DWM composition timing, which carries a single
 * desktop-wide cadence (DWM composes at the fastest connected display's rate),
 * waited out with a high-resolution waitable timer. Deliberately never calls
 * DwmFlush: in-process it can block for hundreds of milliseconds when the
 * client gates presents on the same tick.
 */

typedef HRESULT (WINAPI *DwmGetCompositionTimingInfoType)(HWND, DWM_TIMING_INFO *);
typedef HRESULT (WINAPI *DwmIsCompositionEnabledType)(BOOL *);
typedef HRESULT (WINAPI *CreateDXGIFactory1Type)(REFIID, void **);

static DwmGetCompositionTimingInfoType pDwmGetCompositionTimingInfo = NULL;
static DwmIsCompositionEnabledType pDwmIsCompositionEnabled = NULL;
static CreateDXGIFactory1Type pCreateDXGIFactory1 = NULL;
static JavaVM *jvm = NULL;
static jmethodID onNativeTickMID = NULL;
static LONGLONG qpcFrequency = 0;

typedef struct {
    HANDLE thread;
    HANDLE stopEvent;
    jobject clockRef;
    jlong fallbackPeriodNanos;
    IDXGIOutput *output; // NULL for the DWM composition clock
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

static BOOL loadDxgi()
{
    if (pCreateDXGIFactory1 != NULL) {
        return TRUE;
    }
    HMODULE dxgi = LoadLibraryW(L"dxgi.dll");
    if (dxgi == NULL) {
        return FALSE;
    }
    pCreateDXGIFactory1 = (CreateDXGIFactory1Type)GetProcAddress(dxgi, "CreateDXGIFactory1");
    return pCreateDXGIFactory1 != NULL;
}

/*
 * Win32 display device name ("\\.\DISPLAY1") for a toolkit screen index. This
 * is what DXGI_OUTPUT_DESC.DeviceName carries, and matching on it avoids
 * comparing desktop rectangles, which would have to account for per-monitor
 * DPI scaling to be correct.
 */
static BOOL monitorDeviceName(int screen, WCHAR *name, size_t nameChars)
{
    Devices::InstanceAccess devices;
    AwtWin32GraphicsDevice *device = devices->GetDevice(screen, FALSE);
    if (device == NULL) {
        return FALSE;
    }

    MONITORINFOEXW info;
    ZeroMemory(&info, sizeof(info));
    info.cbSize = sizeof(info);
    if (!::GetMonitorInfoW(device->GetMonitor(), (LPMONITORINFO)&info)) {
        return FALSE;
    }

    return wcscpy_s(name, nameChars, info.szDevice) == 0;
}

/* Returns the output with a reference held, or NULL. */
static IDXGIOutput *findOutput(const WCHAR *deviceName)
{
    if (!loadDxgi()) {
        return NULL;
    }

    IDXGIFactory1 *factory = NULL;
    if (FAILED(pCreateDXGIFactory1(__uuidof(IDXGIFactory1), (void **)&factory))) {
        return NULL;
    }

    IDXGIOutput *found = NULL;
    IDXGIAdapter1 *adapter = NULL;
    for (UINT ai = 0;
            found == NULL && factory->EnumAdapters1(ai, &adapter) != DXGI_ERROR_NOT_FOUND;
            ai++) {
        IDXGIOutput *output = NULL;
        for (UINT oi = 0; adapter->EnumOutputs(oi, &output) != DXGI_ERROR_NOT_FOUND; oi++) {
            DXGI_OUTPUT_DESC desc;
            if (SUCCEEDED(output->GetDesc(&desc)) && desc.AttachedToDesktop &&
                    wcscmp(desc.DeviceName, deviceName) == 0) {
                found = output; // keep this one's reference
                break;
            }
            output->Release();
        }
        adapter->Release();
    }

    factory->Release();
    return found;
}

static HANDLE createHighResolutionTimer()
{
    HANDLE timer = CreateWaitableTimerExW(NULL, NULL,
            CREATE_WAITABLE_TIMER_HIGH_RESOLUTION, TIMER_ALL_ACCESS);
    if (timer == NULL) {
        timer = CreateWaitableTimerExW(NULL, NULL, 0, TIMER_ALL_ACCESS);
    }
    return timer;
}

static void deliverTick(JNIEnv *env, FramePacingClock *clock, LONGLONG tickQpc)
{
    jlong nanos = (jlong)((double)tickQpc * 1000000000.0 / (double)qpcFrequency);
    env->CallVoidMethod(clock->clockRef, onNativeTickMID, nanos);
    if (env->ExceptionCheck()) {
        env->ExceptionClear();
    }
}

/* Waits waitQpc counts, or until stopped. Returns FALSE when stopped. */
static BOOL waitQpcOrStop(FramePacingClock *clock, HANDLE timer, LONGLONG waitQpc)
{
    LARGE_INTEGER due;
    due.QuadPart = -(waitQpc * 10000000LL / qpcFrequency); // relative, 100 ns
    if (due.QuadPart >= 0) {
        due.QuadPart = -1;
    }
    SetWaitableTimer(timer, &due, 0, NULL, NULL, FALSE);

    HANDLE handles[2] = { clock->stopEvent, timer };
    return WaitForMultipleObjects(2, handles, FALSE, INFINITE) == WAIT_OBJECT_0 + 1;
}

static DWORD WINAPI vblankThreadProc(LPVOID param)
{
    FramePacingClock *clock = (FramePacingClock *)param;
    JNIEnv *env;
    if (jvm->AttachCurrentThreadAsDaemon((void **)&env, NULL) != JNI_OK) {
        return 0;
    }

    HANDLE timer = createHighResolutionTimer();
    if (timer == NULL) {
        return 0;
    }

    LONGLONG fallbackQpc = clock->fallbackPeriodNanos * qpcFrequency / 1000000000LL;
    if (fallbackQpc <= 0) {
        fallbackQpc = qpcFrequency / 60;
    }
    /*
     * A display in power save stays attached to the desktop but stops scanning
     * out, and WaitForVBlank then returns immediately and successfully rather
     * than failing — left alone the loop spins at over a million ticks a
     * second, burning a core to save GPU watts. No real display ticks at twice
     * its nominal rate, so anything faster is not a vblank; pace off the
     * nominal period until real ones resume. The threshold has to be this
     * loose because a true refresh rate legitimately runs a little faster than
     * the nominal one (a "59 Hz" panel scans out at 59.95 Hz).
     */
    const LONGLONG minIntervalQpc = fallbackQpc / 2;

    LARGE_INTEGER lastTick;
    QueryPerformanceCounter(&lastTick);

    /*
     * The reference stays owned by the clock struct and is released in
     * nativeRelease, after this thread has been joined. Releasing it here
     * instead would double-release whenever that join times out.
     */
    IDXGIOutput *output = clock->output;
    BOOL vblankUsable = output != NULL;

    for (;;) {
        if (WaitForSingleObject(clock->stopEvent, 0) == WAIT_OBJECT_0) {
            break;
        }

        if (vblankUsable) {
            if (FAILED(output->WaitForVBlank())) {
                /*
                 * The output is gone (display removed, adapter reset). Drop to
                 * the nominal period rather than stopping: the Java side checks
                 * for a vanished display on a tick count, so a clock that stops
                 * ticking is a clock that never gets retired.
                 */
                vblankUsable = FALSE;
                continue;
            }
            // WaitForVBlank is not interruptible, so re-check before delivering.
            if (WaitForSingleObject(clock->stopEvent, 0) == WAIT_OBJECT_0) {
                break;
            }

            LARGE_INTEGER now;
            QueryPerformanceCounter(&now);
            LONGLONG elapsed = now.QuadPart - lastTick.QuadPart;
            if (elapsed < minIntervalQpc &&
                    !waitQpcOrStop(clock, timer, fallbackQpc - elapsed)) {
                break;
            }
        } else if (!waitQpcOrStop(clock, timer, fallbackQpc)) {
            break;
        }

        QueryPerformanceCounter(&lastTick);
        deliverTick(env, clock, lastTick.QuadPart);
    }

    CloseHandle(timer);
    return 0;
}

static DWORD WINAPI compositionThreadProc(LPVOID param)
{
    FramePacingClock *clock = (FramePacingClock *)param;
    JNIEnv *env;
    if (jvm->AttachCurrentThreadAsDaemon((void **)&env, NULL) != JNI_OK) {
        return 0;
    }

    HANDLE timer = createHighResolutionTimer();
    if (timer == NULL) {
        return 0;
    }

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

        if (!waitQpcOrStop(clock, timer, waitQpc)) {
            break; // stop event or failure
        }

        LARGE_INTEGER tickTime;
        QueryPerformanceCounter(&tickTime);
        deliverTick(env, clock, tickTime.QuadPart);
    }

    CloseHandle(timer);
    return 0;
}

static BOOL initShared(JNIEnv *env, jobject clockObj)
{
    if (jvm == NULL) {
        if (env->GetJavaVM(&jvm) != JNI_OK) {
            return FALSE;
        }
        LARGE_INTEGER freq;
        QueryPerformanceFrequency(&freq);
        qpcFrequency = freq.QuadPart;
    }
    if (onNativeTickMID == NULL) {
        /*
         * onNativeTick is declared on the shared NativeClock base class, so one
         * cached id stays valid for every clock kind.
         */
        jclass clockClass = env->GetObjectClass(clockObj);
        onNativeTickMID = env->GetMethodID(clockClass, "onNativeTick", "(J)V");
        if (onNativeTickMID == NULL) {
            env->ExceptionClear();
            return FALSE;
        }
    }
    return TRUE;
}

static FramePacingClock *allocClock(JNIEnv *env, jobject clockObj,
                                    jlong fallbackPeriodNanos, IDXGIOutput *output)
{
    // safe_Malloc throws std::bad_alloc rather than returning NULL; the JNI
    // entry points below catch it.
    FramePacingClock *clock = (FramePacingClock *)safe_Malloc(sizeof(FramePacingClock));
    clock->thread = NULL;
    clock->output = output;
    clock->fallbackPeriodNanos = fallbackPeriodNanos;
    clock->stopEvent = CreateEventW(NULL, TRUE, FALSE, NULL);
    if (clock->stopEvent == NULL) {
        free(clock);
        return NULL;
    }
    clock->clockRef = env->NewGlobalRef(clockObj);
    if (clock->clockRef == NULL) {
        CloseHandle(clock->stopEvent);
        free(clock);
        return NULL;
    }
    return clock;
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

JNIEXPORT jboolean JNICALL
Java_sun_awt_FramePacingWin_nativeProbeVBlank(JNIEnv *env, jclass cls)
{
    if (!loadDxgi()) {
        return JNI_FALSE;
    }

    IDXGIFactory1 *factory = NULL;
    if (FAILED(pCreateDXGIFactory1(__uuidof(IDXGIFactory1), (void **)&factory))) {
        return JNI_FALSE;
    }

    // Available means at least one output is attached to the desktop; a remote
    // session enumerates adapters but no attached outputs.
    jboolean available = JNI_FALSE;
    IDXGIAdapter1 *adapter = NULL;
    for (UINT ai = 0;
            !available && factory->EnumAdapters1(ai, &adapter) != DXGI_ERROR_NOT_FOUND;
            ai++) {
        IDXGIOutput *output = NULL;
        for (UINT oi = 0; adapter->EnumOutputs(oi, &output) != DXGI_ERROR_NOT_FOUND; oi++) {
            DXGI_OUTPUT_DESC desc;
            if (SUCCEEDED(output->GetDesc(&desc)) && desc.AttachedToDesktop) {
                available = JNI_TRUE;
            }
            output->Release();
            if (available) {
                break;
            }
        }
        adapter->Release();
    }

    factory->Release();
    return available;
}

JNIEXPORT jlong JNICALL
Java_sun_awt_FramePacingWin_nativeCreate(JNIEnv *env, jclass cls,
                                         jobject clockObj, jlong fallbackPeriodNanos)
{
    TRY;

    if (!loadDwm() || !initShared(env, clockObj)) {
        return 0;
    }
    return (jlong)(intptr_t)allocClock(env, clockObj, fallbackPeriodNanos, NULL);

    CATCH_BAD_ALLOC_RET(0);
}

JNIEXPORT jlong JNICALL
Java_sun_awt_FramePacingWin_nativeCreateVBlank(JNIEnv *env, jclass cls, jobject clockObj,
                                               jint screen, jlong fallbackPeriodNanos)
{
    TRY;

    if (!initShared(env, clockObj)) {
        return 0;
    }

    WCHAR deviceName[CCHDEVICENAME];
    if (!monitorDeviceName((int)screen, deviceName, CCHDEVICENAME)) {
        return 0;
    }

    // Allocated before the output is acquired so that a failed allocation
    // cannot strand the output's reference.
    FramePacingClock *clock = allocClock(env, clockObj, fallbackPeriodNanos, NULL);
    if (clock == NULL) {
        return 0;
    }

    clock->output = findOutput(deviceName);
    if (clock->output == NULL) {
        CloseHandle(clock->stopEvent);
        env->DeleteGlobalRef(clock->clockRef);
        free(clock);
        return 0;
    }

    return (jlong)(intptr_t)clock;

    CATCH_BAD_ALLOC_RET(0);
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingWin_nativeStart(JNIEnv *env, jclass cls, jlong ptr)
{
    FramePacingClock *clock = (FramePacingClock *)(intptr_t)ptr;
    clock->thread = CreateThread(NULL, 0,
            clock->output != NULL ? vblankThreadProc : compositionThreadProc,
            clock, 0, NULL);
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
    if (clock->output != NULL) {
        clock->output->Release();
    }
    CloseHandle(clock->stopEvent);
    env->DeleteGlobalRef(clock->clockRef);
    free(clock);
}

} // extern "C"
