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

#ifdef __linux__
// For pthread_timedjoin_np; must precede every glibc header.
#define _GNU_SOURCE
#endif

#include "jni.h"

/*
 * Per-CRTC vblank pacing clock for sun.awt.FramePacingUnix.DrmVBlankClock,
 * built directly on the kernel DRM uAPI (no libdrm dependency): the clock
 * thread blocks in DRM_IOCTL_WAIT_VBLANK on one CRTC and delivers the kernel's
 * vblank timestamp to onNativeTick. Unlike the compositor-tied mechanisms
 * (wp_presentation feedback, XWayland Present), the vblank wait free-runs
 * whether or not anything on screen is changing, which is what a subscription
 * clock needs; it is also a genuine blocking wait, well under 1% of a core.
 *
 * The CRTC is bound by connector name when the toolkit can supply one (the
 * Wayland toolkit's outputs carry the compositor's connector name, e.g.
 * "HDMI-2" or "DP-1"), and otherwise by matching each active CRTC's mode
 * period against the display's advertised refresh period, across all DRM card
 * nodes. Device access relies on the logind seat ACL every local desktop
 * session has (remote and headless environments have no accessible display
 * device, and the probe reports the backend unavailable).
 */

#ifdef __linux__

#include <errno.h>
#include <fcntl.h>
#include <pthread.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ioctl.h>
#include <time.h>
#include <unistd.h>

#include <drm/drm.h>
#include <drm/drm_mode.h>

typedef struct {
    pthread_t thread;
    int hasThread;
    volatile int stop;
    int fd;
    int crtcIndex;
    int monotonicTimestamps;
    jlong fallbackPeriodNanos;
    jobject clockRef;
} FramePacingClock;

static JavaVM *jvm = NULL;
static jmethodID onNativeTickMID = NULL;

static int stopRequested(FramePacingClock *clock)
{
    return __atomic_load_n(&clock->stop, __ATOMIC_ACQUIRE);
}

static int64_t nowNanos(void)
{
    struct timespec ts;
    clock_gettime(CLOCK_MONOTONIC, &ts);
    return (int64_t)ts.tv_sec * 1000000000LL + ts.tv_nsec;
}

/* Sleeps, or returns 0 early when the clock was stopped. */
static int sleepOrStop(FramePacingClock *clock, int64_t nanos)
{
    if (nanos > 0) {
        struct timespec ts = { nanos / 1000000000LL, nanos % 1000000000LL };
        while (nanosleep(&ts, &ts) == -1 && errno == EINTR) {
            if (stopRequested(clock)) {
                return 0;
            }
        }
    }
    return !stopRequested(clock);
}

/*
 * Finds the active CRTC whose mode period best matches wantPeriodNanos,
 * scanning every card node. wantPeriodNanos <= 0 accepts the first active CRTC
 * (the availability probe). On success the card stays open and ownership of
 * the descriptor passes to the caller.
 */
static int findBestCrtc(int64_t wantPeriodNanos, int *outFd, int *outCrtcIndex)
{
    int bestFd = -1;
    int bestIndex = -1;
    int64_t bestScore = INT64_MAX;

    for (int card = 0; card < 16; card++) {
        char path[32];
        snprintf(path, sizeof(path), "/dev/dri/card%d", card);
        int fd = open(path, O_RDWR | O_CLOEXEC);
        if (fd < 0) {
            continue;
        }

        struct drm_mode_card_res res;
        uint32_t crtcs[64];
        memset(&res, 0, sizeof(res));
        if (ioctl(fd, DRM_IOCTL_MODE_GETRESOURCES, &res) == 0 && res.count_crtcs > 0) {
            uint32_t count = res.count_crtcs > 64 ? 64 : res.count_crtcs;
            memset(&res, 0, sizeof(res));
            res.crtc_id_ptr = (uint64_t)(uintptr_t)crtcs;
            res.count_crtcs = count;
            if (ioctl(fd, DRM_IOCTL_MODE_GETRESOURCES, &res) == 0) {
                if (res.count_crtcs < count) {
                    count = res.count_crtcs;
                }
                for (uint32_t i = 0; i < count; i++) {
                    struct drm_mode_crtc crtc;
                    memset(&crtc, 0, sizeof(crtc));
                    crtc.crtc_id = crtcs[i];
                    if (ioctl(fd, DRM_IOCTL_MODE_GETCRTC, &crtc) != 0 ||
                            !crtc.mode_valid || crtc.mode.clock == 0) {
                        continue;
                    }
                    // Pixel clock is in kHz: period = htotal * vtotal / clock.
                    int64_t period = (int64_t)crtc.mode.htotal * crtc.mode.vtotal
                            * 1000000LL / crtc.mode.clock;
                    int64_t score = wantPeriodNanos > 0
                            ? llabs(period - wantPeriodNanos) : 0;
                    if (score < bestScore) {
                        bestScore = score;
                        bestIndex = (int)i;
                        if (bestFd != fd) {
                            if (bestFd >= 0) {
                                close(bestFd);
                            }
                            bestFd = fd;
                        }
                    }
                }
            }
        }

        if (bestFd != fd) {
            close(fd);
        }
        if (wantPeriodNanos <= 0 && bestFd >= 0) {
            break;
        }
    }

    if (bestFd < 0) {
        return 0;
    }
    *outFd = bestFd;
    *outCrtcIndex = bestIndex;
    return 1;
}

static const char *connectorTypeName(uint32_t type)
{
    switch (type) {
        case DRM_MODE_CONNECTOR_VGA:         return "VGA";
        case DRM_MODE_CONNECTOR_DVII:        return "DVI-I";
        case DRM_MODE_CONNECTOR_DVID:        return "DVI-D";
        case DRM_MODE_CONNECTOR_DVIA:        return "DVI-A";
        case DRM_MODE_CONNECTOR_Composite:   return "Composite";
        case DRM_MODE_CONNECTOR_SVIDEO:      return "SVIDEO";
        case DRM_MODE_CONNECTOR_LVDS:        return "LVDS";
        case DRM_MODE_CONNECTOR_Component:   return "Component";
        case DRM_MODE_CONNECTOR_9PinDIN:     return "DIN";
        case DRM_MODE_CONNECTOR_DisplayPort: return "DP";
        case DRM_MODE_CONNECTOR_HDMIA:       return "HDMI-A";
        case DRM_MODE_CONNECTOR_HDMIB:       return "HDMI-B";
        case DRM_MODE_CONNECTOR_TV:          return "TV";
        case DRM_MODE_CONNECTOR_eDP:         return "eDP";
        case DRM_MODE_CONNECTOR_VIRTUAL:     return "Virtual";
        case DRM_MODE_CONNECTOR_DSI:         return "DSI";
        case DRM_MODE_CONNECTOR_DPI:         return "DPI";
        default:                             return NULL;
    }
}

/*
 * Matches a compositor-supplied output name against a kernel connector.
 * Kernel names are "<type>-<id>" ("HDMI-A-2"); Mutter drops the HDMI bus
 * letter and calls the same connector "HDMI-2", so that spelling is accepted
 * as well.
 */
static int connectorNameMatches(const char *wanted, const char *typeName, uint32_t typeId)
{
    char name[40];
    snprintf(name, sizeof(name), "%s-%u", typeName, typeId);
    if (strcmp(wanted, name) == 0) {
        return 1;
    }
    if (strncmp(typeName, "HDMI-", 5) == 0) {
        snprintf(name, sizeof(name), "HDMI-%u", typeId);
        if (strcmp(wanted, name) == 0) {
            return 1;
        }
    }
    return 0;
}

/*
 * Finds the CRTC currently driving the named connector. On success the card
 * stays open and ownership of the descriptor passes to the caller.
 */
static int findCrtcByConnector(const char *wanted, int *outFd, int *outCrtcIndex)
{
    for (int card = 0; card < 16; card++) {
        char path[32];
        snprintf(path, sizeof(path), "/dev/dri/card%d", card);
        int fd = open(path, O_RDWR | O_CLOEXEC);
        if (fd < 0) {
            continue;
        }

        struct drm_mode_card_res res;
        uint32_t crtcs[64];
        uint32_t connectors[64];
        memset(&res, 0, sizeof(res));
        if (ioctl(fd, DRM_IOCTL_MODE_GETRESOURCES, &res) != 0 ||
                res.count_crtcs == 0 || res.count_connectors == 0) {
            close(fd);
            continue;
        }
        uint32_t crtcCount = res.count_crtcs > 64 ? 64 : res.count_crtcs;
        uint32_t connCount = res.count_connectors > 64 ? 64 : res.count_connectors;
        memset(&res, 0, sizeof(res));
        res.crtc_id_ptr = (uint64_t)(uintptr_t)crtcs;
        res.count_crtcs = crtcCount;
        res.connector_id_ptr = (uint64_t)(uintptr_t)connectors;
        res.count_connectors = connCount;
        if (ioctl(fd, DRM_IOCTL_MODE_GETRESOURCES, &res) != 0) {
            close(fd);
            continue;
        }
        if (res.count_crtcs < crtcCount) {
            crtcCount = res.count_crtcs;
        }
        if (res.count_connectors < connCount) {
            connCount = res.count_connectors;
        }

        for (uint32_t i = 0; i < connCount; i++) {
            struct drm_mode_get_connector conn;
            memset(&conn, 0, sizeof(conn));
            conn.connector_id = connectors[i];
            // With no array pointers supplied this fills only the scalar
            // fields, which is all the match needs.
            if (ioctl(fd, DRM_IOCTL_MODE_GETCONNECTOR, &conn) != 0 ||
                    conn.connection != 1 /* connected */ || conn.encoder_id == 0) {
                continue;
            }
            const char *typeName = connectorTypeName(conn.connector_type);
            if (typeName == NULL ||
                    !connectorNameMatches(wanted, typeName, conn.connector_type_id)) {
                continue;
            }

            struct drm_mode_get_encoder enc;
            memset(&enc, 0, sizeof(enc));
            enc.encoder_id = conn.encoder_id;
            if (ioctl(fd, DRM_IOCTL_MODE_GETENCODER, &enc) != 0 || enc.crtc_id == 0) {
                continue;
            }
            for (uint32_t k = 0; k < crtcCount; k++) {
                if (crtcs[k] == enc.crtc_id) {
                    *outFd = fd;
                    *outCrtcIndex = (int)k;
                    return 1;
                }
            }
        }
        close(fd);
    }
    return 0;
}

static void *vblankThreadProc(void *param)
{
    FramePacingClock *clock = (FramePacingClock *)param;
    JNIEnv *env = NULL;
    if ((*jvm)->AttachCurrentThreadAsDaemon(jvm, (void **)&env, NULL) != JNI_OK) {
        return NULL;
    }

    int64_t fallback = clock->fallbackPeriodNanos;
    if (fallback <= 0) {
        fallback = 1000000000LL / 60;
    }
    /*
     * A display in power save can complete vblank waits immediately instead of
     * failing them. No real display ticks at twice its nominal rate, so
     * anything faster is not a vblank; pace off the nominal period until real
     * ones resume. Half the period, not the whole one: a true refresh rate
     * legitimately runs slightly faster than its nominal figure.
     */
    const int64_t minInterval = fallback / 2;
    const unsigned int highCrtc = ((unsigned int)clock->crtcIndex
            << _DRM_VBLANK_HIGH_CRTC_SHIFT) & _DRM_VBLANK_HIGH_CRTC_MASK;

    int vblankUsable = 1;
    int64_t last = nowNanos();

    while (!stopRequested(clock)) {
        int64_t tickTime;

        if (vblankUsable) {
            union drm_wait_vblank vbl;
            memset(&vbl, 0, sizeof(vbl));
            vbl.request.type = _DRM_VBLANK_RELATIVE | highCrtc;
            vbl.request.sequence = 1;

            int rc;
            do {
                rc = ioctl(clock->fd, DRM_IOCTL_WAIT_VBLANK, &vbl);
            } while (rc == -1 && errno == EINTR && !stopRequested(clock));
            if (stopRequested(clock)) {
                break;
            }
            if (rc == -1) {
                /*
                 * The CRTC is gone (display removed, adapter reconfigured).
                 * Drop to the nominal period rather than stopping: the Java
                 * side retires a vanished display on a tick count, so a clock
                 * that stops ticking is a clock that never gets retired.
                 */
                vblankUsable = 0;
                continue;
            }

            tickTime = clock->monotonicTimestamps
                    ? (int64_t)vbl.reply.tval_sec * 1000000000LL
                            + (int64_t)vbl.reply.tval_usec * 1000LL
                    : nowNanos();
            int64_t elapsed = tickTime - last;
            if (elapsed < minInterval) {
                if (!sleepOrStop(clock, fallback - (elapsed > 0 ? elapsed : 0))) {
                    break;
                }
                tickTime = nowNanos();
            }
        } else {
            if (!sleepOrStop(clock, fallback)) {
                break;
            }
            tickTime = nowNanos();
        }

        last = tickTime;
        (*env)->CallVoidMethod(env, clock->clockRef, onNativeTickMID, (jlong)tickTime);
        if ((*env)->ExceptionCheck(env)) {
            (*env)->ExceptionClear(env);
        }
    }

    (*jvm)->DetachCurrentThread(jvm);
    return NULL;
}

static int initShared(JNIEnv *env, jobject clockObj)
{
    if (jvm == NULL) {
        if ((*env)->GetJavaVM(env, &jvm) != JNI_OK) {
            return 0;
        }
    }
    if (onNativeTickMID == NULL) {
        /*
         * There is a single native clock class on this platform, so one cached
         * id resolved from the first instance stays valid for all of them.
         */
        jclass clockClass = (*env)->GetObjectClass(env, clockObj);
        onNativeTickMID = (*env)->GetMethodID(env, clockClass, "onNativeTick", "(J)V");
        if (onNativeTickMID == NULL) {
            (*env)->ExceptionClear(env);
            return 0;
        }
    }
    return 1;
}

JNIEXPORT jboolean JNICALL
Java_sun_awt_FramePacingUnix_nativeProbe(JNIEnv *env, jclass cls)
{
    (void)env;
    (void)cls;
    int fd = -1;
    int index = -1;
    if (!findBestCrtc(0, &fd, &index)) {
        return JNI_FALSE;
    }
    close(fd);
    return JNI_TRUE;
}

JNIEXPORT jlong JNICALL
Java_sun_awt_FramePacingUnix_nativeCreate(JNIEnv *env, jclass cls, jobject clockObj,
                                          jlong fallbackPeriodNanos, jstring connectorName)
{
    (void)cls;
    if (!initShared(env, clockObj)) {
        return 0;
    }

    int fd = -1;
    int crtcIndex = -1;
    int bound = 0;
    if (connectorName != NULL) {
        const char *wanted = (*env)->GetStringUTFChars(env, connectorName, NULL);
        if (wanted != NULL) {
            bound = findCrtcByConnector(wanted, &fd, &crtcIndex);
            if (getenv("JBR_FRAMEPACING_DEBUG") != NULL) {
                fprintf(stderr, "FramePacing: connector \"%s\" %s\n", wanted,
                        bound ? "bound to its CRTC" : "not matched, using period fallback");
            }
            (*env)->ReleaseStringUTFChars(env, connectorName, wanted);
        }
    }
    if (!bound && !findBestCrtc(fallbackPeriodNanos, &fd, &crtcIndex)) {
        return 0;
    }
    if (getenv("JBR_FRAMEPACING_DEBUG") != NULL) {
        fprintf(stderr, "FramePacing: DRM clock on crtc index %d (%s binding)\n",
                crtcIndex, bound ? "connector" : "period");
    }

    FramePacingClock *clock = (FramePacingClock *)calloc(1, sizeof(FramePacingClock));
    if (clock == NULL) {
        close(fd);
        return 0;
    }
    clock->fd = fd;
    clock->crtcIndex = crtcIndex;
    clock->fallbackPeriodNanos = fallbackPeriodNanos;

    struct drm_get_cap cap;
    memset(&cap, 0, sizeof(cap));
    cap.capability = DRM_CAP_TIMESTAMP_MONOTONIC;
    clock->monotonicTimestamps =
            ioctl(fd, DRM_IOCTL_GET_CAP, &cap) == 0 && cap.value != 0;

    clock->clockRef = (*env)->NewGlobalRef(env, clockObj);
    if (clock->clockRef == NULL) {
        close(fd);
        free(clock);
        return 0;
    }
    return (jlong)(intptr_t)clock;
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingUnix_nativeStart(JNIEnv *env, jclass cls, jlong ptr)
{
    (void)env;
    (void)cls;
    FramePacingClock *clock = (FramePacingClock *)(intptr_t)ptr;
    clock->hasThread = pthread_create(&clock->thread, NULL, vblankThreadProc, clock) == 0;
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingUnix_nativeStop(JNIEnv *env, jclass cls, jlong ptr)
{
    (void)env;
    (void)cls;
    FramePacingClock *clock = (FramePacingClock *)(intptr_t)ptr;
    __atomic_store_n(&clock->stop, 1, __ATOMIC_RELEASE);
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingUnix_nativeRelease(JNIEnv *env, jclass cls, jlong ptr)
{
    (void)cls;
    FramePacingClock *clock = (FramePacingClock *)(intptr_t)ptr;
    if (clock->hasThread) {
        // The thread exits promptly once the stop flag is set (at most one
        // frame wait); bound the join so release stays effectively brief.
        struct timespec deadline;
        clock_gettime(CLOCK_REALTIME, &deadline);
        deadline.tv_sec += 1;
        pthread_timedjoin_np(clock->thread, NULL, &deadline);
    }
    close(clock->fd);
    (*env)->DeleteGlobalRef(env, clock->clockRef);
    free(clock);
}

#else /* !__linux__ */

JNIEXPORT jboolean JNICALL
Java_sun_awt_FramePacingUnix_nativeProbe(JNIEnv *env, jclass cls)
{
    (void)env;
    (void)cls;
    return JNI_FALSE;
}

JNIEXPORT jlong JNICALL
Java_sun_awt_FramePacingUnix_nativeCreate(JNIEnv *env, jclass cls, jobject clockObj,
                                          jlong fallbackPeriodNanos, jstring connectorName)
{
    (void)env;
    (void)cls;
    (void)clockObj;
    (void)fallbackPeriodNanos;
    (void)connectorName;
    return 0;
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingUnix_nativeStart(JNIEnv *env, jclass cls, jlong ptr)
{
    (void)env;
    (void)cls;
    (void)ptr;
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingUnix_nativeStop(JNIEnv *env, jclass cls, jlong ptr)
{
    (void)env;
    (void)cls;
    (void)ptr;
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingUnix_nativeRelease(JNIEnv *env, jclass cls, jlong ptr)
{
    (void)env;
    (void)cls;
    (void)ptr;
}

#endif /* __linux__ */
