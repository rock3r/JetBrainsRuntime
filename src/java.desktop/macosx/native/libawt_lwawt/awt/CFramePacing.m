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

#import <AppKit/AppKit.h>
#import <QuartzCore/QuartzCore.h>

#include "jni.h"

/*
 * FramePacing-owned CADisplayLink (NSScreen.displayLink, macOS 14+): one link
 * per subscribed display, driving sun.awt.FramePacingMac.DisplayLinkClock
 * .onNativeTick from a dedicated per-clock runloop thread (attached to the VM
 * as a daemon). CVDisplayLink would offer the same shape on older systems but
 * is deprecated since macOS 15; on macOS 13 and older the probe reports
 * unavailable and the Java side paces with the shared timer instead.
 *
 * Timestamps: CADisplayLink.timestamp is CACurrentMediaTime()-based, which is
 * mach_absolute_time-derived — the same monotonic base as System.nanoTime() —
 * so seconds * 1e9 converts directly.
 *
 * The link's preferredFrameRateRange is left at its default, which follows the
 * display's current refresh behavior — including adaptive rates on ProMotion
 * panels. Pinning a range here is the tuning point if a client ever wants a
 * fixed cadence on an adaptive display.
 */

static JavaVM *jvm = NULL;
static jmethodID onNativeTickMID = NULL;

/*
 * NSScreen for a CGDirectDisplayID. NSScreen.screens is class-level state and
 * is read here from the calling (non-AppKit) thread deliberately: resolving it
 * via the AppKit thread from inside the FramePacing service lock would invite
 * an AppKit/EDT lock inversion for a value that is only used to create the
 * link.
 */
static NSScreen *screenForDisplayID(CGDirectDisplayID displayID)
{
    for (NSScreen *screen in [NSScreen screens]) {
        NSNumber *screenNumber = [[screen deviceDescription] objectForKey:@"NSScreenNumber"];
        if (screenNumber != nil && (CGDirectDisplayID)[screenNumber unsignedIntValue] == displayID) {
            return screen;
        }
    }
    return nil;
}

@interface CFramePacingClock : NSObject
- (instancetype)initWithDisplayID:(CGDirectDisplayID)displayID clockRef:(jobject)clockRef;
- (void)start;
- (void)stop;
- (void)joinAndReleaseRef:(JNIEnv *)env;
@end

@implementation CFramePacingClock {
    CGDirectDisplayID _displayID;
    jobject _clockRef; // global ref, deleted in joinAndReleaseRef
    NSCondition *_doneCondition;
    CFRunLoopRef _runLoop; // owned by the clock thread; guarded by _doneCondition
    BOOL _stopped;         // guarded by _doneCondition
    BOOL _threadExited;    // guarded by _doneCondition
}

- (instancetype)initWithDisplayID:(CGDirectDisplayID)displayID clockRef:(jobject)clockRef {
    self = [super init];
    if (self) {
        _displayID = displayID;
        _clockRef = clockRef;
        _doneCondition = [NSCondition new];
        _runLoop = NULL;
        _stopped = NO;
        _threadExited = NO;
    }
    return self;
}

- (void)dealloc {
    [_doneCondition release];
    [super dealloc];
}

- (void)start {
    @autoreleasepool {
        NSThread *thread = [[NSThread alloc] initWithTarget:self
                                                   selector:@selector(threadMain)
                                                     object:nil];
        thread.name = [NSString stringWithFormat:@"JBR-FramePacing-CADisplayLink-%u", _displayID];
        [thread start];
        [thread release]; // the running thread retains itself and its target
    }
}

- (void)signalThreadExited {
    [_doneCondition lock];
    _threadExited = YES;
    [_doneCondition signal];
    [_doneCondition unlock];
}

- (void)threadMain {
    @autoreleasepool {
        CADisplayLink *link = nil;
        if (@available(macOS 14.0, *)) {
            NSScreen *screen = screenForDisplayID(_displayID);
            if (screen != nil) {
                link = [screen displayLinkWithTarget:self selector:@selector(onTick:)];
            }
        }

        [_doneCondition lock];
        if (_stopped || link == nil) {
            [_doneCondition unlock];
            [link invalidate];
            [self signalThreadExited];
            return;
        }
        _runLoop = CFRunLoopGetCurrent();
        [_doneCondition unlock];

        [link addToRunLoop:[NSRunLoop currentRunLoop] forMode:NSDefaultRunLoopMode];
        CFRunLoopRun(); // exits when stop() calls CFRunLoopStop

        // Invalidation must happen on the runloop thread; it also breaks the
        // link's retain of this target.
        [link invalidate];
        [self signalThreadExited];
    }
}

- (void)onTick:(CADisplayLink *)link {
    JNIEnv *env;
    if ((*jvm)->AttachCurrentThreadAsDaemon(jvm, (void **)&env, NULL) != JNI_OK) {
        return;
    }
    jlong nanos = (jlong)(link.timestamp * 1000000000.0);
    (*env)->CallVoidMethod(env, _clockRef, onNativeTickMID, nanos);
    if ((*env)->ExceptionCheck(env)) {
        (*env)->ExceptionClear(env);
    }
}

- (void)stop {
    [_doneCondition lock];
    _stopped = YES;
    if (_runLoop != NULL) {
        CFRunLoopStop(_runLoop);
    }
    [_doneCondition unlock];
}

- (void)joinAndReleaseRef:(JNIEnv *)env {
    // The thread exits promptly once stopped (at most one frame callback);
    // bound the wait so release stays effectively brief even if it wedges.
    @autoreleasepool {
        NSDate *deadline = [NSDate dateWithTimeIntervalSinceNow:1.0];
        [_doneCondition lock];
        while (!_threadExited && [_doneCondition waitUntilDate:deadline]) {
            // Re-check _threadExited; waitUntilDate returning NO means timeout.
        }
        [_doneCondition unlock];
    }
    (*env)->DeleteGlobalRef(env, _clockRef);
    _clockRef = NULL;
}

@end

JNIEXPORT jboolean JNICALL
Java_sun_awt_FramePacingMac_nativeProbe(JNIEnv *env, jclass cls, jint displayID)
{
    if (@available(macOS 14.0, *)) {
        @autoreleasepool {
            return screenForDisplayID((CGDirectDisplayID)displayID) != nil ? JNI_TRUE : JNI_FALSE;
        }
    }
    return JNI_FALSE;
}

JNIEXPORT jlong JNICALL
Java_sun_awt_FramePacingMac_nativeCreate(JNIEnv *env, jclass cls,
                                         jint displayID, jobject clock)
{
    if (jvm == NULL) {
        if ((*env)->GetJavaVM(env, &jvm) != JNI_OK) {
            return 0;
        }
    }
    if (onNativeTickMID == NULL) {
        jclass clockClass = (*env)->GetObjectClass(env, clock);
        onNativeTickMID = (*env)->GetMethodID(env, clockClass, "onNativeTick", "(J)V");
        if (onNativeTickMID == NULL) {
            (*env)->ExceptionClear(env);
            return 0;
        }
    }

    if (!Java_sun_awt_FramePacingMac_nativeProbe(env, cls, displayID)) {
        return 0;
    }

    jobject clockRef = (*env)->NewGlobalRef(env, clock);
    if (clockRef == NULL) {
        return 0;
    }
    // The alloc +1 is the reference the jlong carries; nativeRelease drops it.
    CFramePacingClock *pacingClock =
            [[CFramePacingClock alloc] initWithDisplayID:(CGDirectDisplayID)displayID
                                                clockRef:clockRef];
    return (jlong)(intptr_t)pacingClock;
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingMac_nativeStart(JNIEnv *env, jclass cls, jlong ptr)
{
    CFramePacingClock *pacingClock = (CFramePacingClock *)(intptr_t)ptr;
    [pacingClock start];
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingMac_nativeStop(JNIEnv *env, jclass cls, jlong ptr)
{
    CFramePacingClock *pacingClock = (CFramePacingClock *)(intptr_t)ptr;
    [pacingClock stop];
}

JNIEXPORT void JNICALL
Java_sun_awt_FramePacingMac_nativeRelease(JNIEnv *env, jclass cls, jlong ptr)
{
    CFramePacingClock *pacingClock = (CFramePacingClock *)(intptr_t)ptr;
    [pacingClock joinAndReleaseRef:env];
    [pacingClock release];
}
