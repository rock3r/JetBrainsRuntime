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
import java.util.concurrent.locks.LockSupport;

/**
 * Windows backend: a composition-aligned clock built from DWM composition
 * timing ({@code DwmGetCompositionTimingInfo} plus a high-resolution waitable
 * timer to the next vblank boundary), reporting
 * {@code QUALITY_COMPOSITION_CLOCK}. In-process {@code DwmFlush} is
 * deliberately not used: it can block for hundreds of milliseconds when the
 * client gates presents on the same tick.
 *
 * <p>DWM composition timing follows the primary display's cadence; per-display
 * clocks all pace at that cadence (documented v2 limitation). When DWM
 * composition is unavailable (remote sessions), the service falls back to the
 * shared timer and {@code QUALITY_ESTIMATED}.
 */
@JBRApi.Service
@JBRApi.Provides("FramePacing")
public class FramePacingWin extends FramePacing {

    private final boolean compositionClockAvailable;

    public FramePacingWin() {
        this.compositionClockAvailable = probeCompositionClock();
    }

    private boolean probeCompositionClock() {
        if (FORCE_ESTIMATED) return false;

        try {
            return nativeProbe();
        } catch (UnsatisfiedLinkError | RuntimeException e) {
            return false;
        }
    }

    @Override
    public int getQuality() {
        return compositionClockAvailable ? QUALITY_COMPOSITION_CLOCK : QUALITY_ESTIMATED;
    }

    @Override
    protected DisplayClock createClock(long displayId, long periodNanos) {
        if (compositionClockAvailable) {
            return new DwmClock(this, displayId, periodNanos);
        }
        return super.createClock(displayId, periodNanos);
    }

    @Override
    protected long deviceId(GraphicsDevice device) {
        if (device instanceof Win32GraphicsDevice win32Device) {
            // Stable hash of the display device name (e.g. "\\.\DISPLAY1"),
            // which round-trips with the GraphicsConfiguration. A HMONITOR
            // is not stable across mode changes; the device name is.
            String name = win32Device.getIDstring();
            return name == null ? -1 : makePositive(name.hashCode());
        }

        return -1;
    }

    /**
     * DWM-composition-driven tick source. The native thread waits to each
     * vblank boundary with a high-resolution waitable timer and delivers from
     * that thread; if native setup fails, the clock behaves like the shared
     * timer at the nominal period.
     */
    private static final class DwmClock extends DisplayClock {
        private volatile long ptr;

        DwmClock(FramePacingWin service, long displayId, long periodNanos) {
            super(service, displayId, periodNanos);
        }

        @Override
        protected void onStart() {
            long created = 0;
            try {
                created = nativeCreate(this, periodNanos);
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

        /** Called from the native composition clock thread. */
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

    private static native boolean nativeProbe();

    private static native long nativeCreate(DwmClock clock, long fallbackPeriodNanos);

    private static native void nativeStart(long ptr);

    private static native void nativeStop(long ptr);

    private static native void nativeRelease(long ptr);
}
