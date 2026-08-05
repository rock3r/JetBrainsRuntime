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

package sun.awt;

import com.jetbrains.exported.JBRApi;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.concurrent.locks.LockSupport;

/**
 * macOS backend: one FramePacing-owned {@code CVDisplayLink} per subscribed
 * display, reporting {@code QUALITY_DISPLAY_LINK}. The link is created with
 * the first subscriber of a display and released with the last, so an idle
 * process keeps no link running. Falls back to the shared timer (and
 * {@code QUALITY_ESTIMATED}) when display links are unavailable.
 */
@JBRApi.Service
@JBRApi.Provides("FramePacing")
public class FramePacingMac extends FramePacing {

    private final boolean displayLinkAvailable;

    public FramePacingMac() {
        this.displayLinkAvailable = probeDisplayLink();
    }

    private boolean probeDisplayLink() {
        if (FORCE_ESTIMATED) return false;

        try {
            GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice();
            long id = deviceId(device);
            return id != -1 && nativeProbe((int) id);
        } catch (UnsatisfiedLinkError | RuntimeException e) {
            return false;
        }
    }

    @Override
    public int getQuality() {
        return displayLinkAvailable ? QUALITY_DISPLAY_LINK : QUALITY_ESTIMATED;
    }

    @Override
    protected DisplayClock createClock(long displayId, long periodNanos) {
        if (displayLinkAvailable) {
            return new DisplayLinkClock(this, displayId, periodNanos);
        }
        return super.createClock(displayId, periodNanos);
    }

    @Override
    protected long deviceId(GraphicsDevice device) {
        if (device instanceof CGraphicsDevice cgDevice) {
            // CGDirectDisplayID, zero-extended.
            return makePositive(cgDevice.getDisplayID());
        }

        return -1;
    }

    /**
     * CVDisplayLink-driven tick source. The native callback thread delivers
     * ticks directly; if link creation fails for this particular display, the
     * clock degrades to a timer thread at the nominal period.
     */
    private static final class DisplayLinkClock extends DisplayClock {
        private volatile long ptr;

        DisplayLinkClock(FramePacingMac service, long displayId, long periodNanos) {
            super(service, displayId, periodNanos);
        }

        @Override
        protected void onStart() {
            long created = 0;
            try {
                created = nativeCreate((int) displayId, this);
            } catch (UnsatisfiedLinkError ignored) {
            }

            ptr = created;
            if (created != 0) {
                nativeStart(created);
            } else {
                Thread thread = new Thread(this::timerLoop, "JBR-FramePacing-" + displayId);
                thread.setDaemon(true);
                thread.start();
            }
        }

        @Override
        protected void onStop() {
            long p = ptr;
            ptr = 0;
            if (p != 0) {
                nativeStop(p);
                nativeRelease(p);
            }
            // The timer fallback thread, if any, observes the stopped flag.
        }

        /** Called from the CVDisplayLink output callback thread. */
        void onNativeTick(long timeNanos) {
            deliver(timeNanos);
        }

        private void timerLoop() {
            long deadline = System.nanoTime() + periodNanos;

            while (!stopped) {
                long now = System.nanoTime();
                if (now < deadline) {
                    LockSupport.parkNanos(deadline - now);
                    continue;
                }

                deadline += ((now - deadline) / periodNanos + 1) * periodNanos;
                deliver(now);
            }
        }
    }

    private static native boolean nativeProbe(int displayId);

    private static native long nativeCreate(int displayId, DisplayLinkClock clock);

    private static native void nativeStart(long ptr);

    private static native void nativeStop(long ptr);

    private static native void nativeRelease(long ptr);
}
