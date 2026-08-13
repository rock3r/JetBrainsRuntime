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
 * Windows backend, preferring a per-display vblank clock built on
 * {@code IDXGIOutput::WaitForVBlank} and reporting
 * {@code QUALITY_DISPLAY_LINK}: it waits on the hardware vblank of one specific
 * output, so each display is paced at its own cadence, and it tracks the true
 * refresh rate rather than the integer the display mode reports (a nominal
 * 59 Hz panel actually runs at 59.95 Hz, which a nominal-period timer drifts a
 * full frame against every few seconds).
 *
 * <p>When DXGI is unavailable the service falls back to a composition-aligned
 * clock built from DWM composition timing
 * ({@code DwmGetCompositionTimingInfo} plus a high-resolution waitable timer to
 * the next vblank boundary), reporting {@code QUALITY_COMPOSITION_CLOCK}. That
 * clock carries a single desktop-wide cadence — DWM composes at the rate of the
 * fastest connected display — so on a mixed-refresh setup it paces a window on
 * a slower display too fast. It never paces one too slow on Windows 10 version
 * 2004 and later; before that DWM composed at the <em>slowest</em> display's
 * rate instead, which does cap a fast display. In-process {@code DwmFlush} is
 * deliberately not used: it can block for hundreds of milliseconds when the
 * client gates presents on the same tick.
 *
 * <p>With neither available (remote sessions) the service falls back to the
 * shared timer and {@code QUALITY_ESTIMATED}.
 */
@JBRApi.Service
@JBRApi.Provides("FramePacing")
public class FramePacingWin extends FramePacing {

    private final boolean vblankClockAvailable;
    private final boolean compositionClockAvailable;

    public FramePacingWin() {
        this.vblankClockAvailable = probe(true);
        this.compositionClockAvailable = probe(false);
    }

    private boolean probe(boolean dxgi) {
        if (FORCE_ESTIMATED) return false;

        try {
            return dxgi ? nativeProbeVBlank() : nativeProbe();
        } catch (UnsatisfiedLinkError | RuntimeException e) {
            return false;
        }
    }

    @Override
    public int getQuality() {
        if (vblankClockAvailable) return QUALITY_DISPLAY_LINK;
        if (compositionClockAvailable) return QUALITY_COMPOSITION_CLOCK;
        return QUALITY_ESTIMATED;
    }

    @Override
    protected DisplayClock createClock(long displayId, long periodNanos) {
        if (vblankClockAvailable) {
            // A DXGI output is matched by Win32 display device name, which the
            // display id cannot supply: it hashes the toolkit's index-based
            // name ("\Display0"), a different string from the device name
            // ("\\.\DISPLAY1") and numbered differently besides. The screen
            // index is what native code can turn into an HMONITOR and so into
            // the device name.
            int screen = screenIndex(displayId);
            if (screen >= 0) {
                return new VBlankClock(this, displayId, periodNanos, screen);
            }
        }
        if (compositionClockAvailable) {
            return new DwmClock(this, displayId, periodNanos);
        }
        return super.createClock(displayId, periodNanos);
    }

    private int screenIndex(long displayId) {
        GraphicsDevice[] devices =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        for (GraphicsDevice device : devices) {
            if (device.getType() != GraphicsDevice.TYPE_RASTER_SCREEN) continue;
            if (device instanceof Win32GraphicsDevice win32Device
                    && deviceId(device) == displayId) {
                return win32Device.getScreen();
            }
        }

        return -1;
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
     * A tick source backed by a native thread. The native side calls
     * {@link #onNativeTick(long)}; if native setup fails, the clock behaves
     * like the shared timer at the nominal period.
     *
     * <p>{@code onNativeTick} is declared here rather than on each subclass on
     * purpose: the native code caches one {@code jmethodID} for it, and a
     * method id resolved against one class is not valid for an unrelated one.
     * Declaring it once keeps the single cached id correct for every subclass.
     */
    private abstract static class NativeClock extends DisplayClock {
        private volatile long ptr;

        NativeClock(FramePacingWin service, long displayId, long periodNanos) {
            super(service, displayId, periodNanos);
        }

        /** Creates the native tick source, or returns 0 to use the timer fallback. */
        protected abstract long createNative();

        @Override
        protected void onStart() {
            long created = 0;
            try {
                created = createNative();
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

        /** Called from the native clock thread. */
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

    /**
     * Per-display vblank tick source: the native thread blocks in
     * {@code IDXGIOutput::WaitForVBlank} on this display's output and delivers
     * from that thread. The wait is a genuine blocking wait — measured at under
     * 1% of a core on both NVIDIA and Intel — not the spin its reputation
     * suggests.
     */
    private static final class VBlankClock extends NativeClock {
        private final int screen;

        VBlankClock(FramePacingWin service, long displayId, long periodNanos, int screen) {
            super(service, displayId, periodNanos);
            this.screen = screen;
        }

        @Override
        protected long createNative() {
            return nativeCreateVBlank(this, screen, periodNanos);
        }
    }

    /**
     * DWM-composition-driven tick source. The native thread waits to each
     * vblank boundary with a high-resolution waitable timer and delivers from
     * that thread.
     */
    private static final class DwmClock extends NativeClock {

        DwmClock(FramePacingWin service, long displayId, long periodNanos) {
            super(service, displayId, periodNanos);
        }

        @Override
        protected long createNative() {
            return nativeCreate(this, periodNanos);
        }
    }

    private static native boolean nativeProbe();

    private static native boolean nativeProbeVBlank();

    private static native long nativeCreate(NativeClock clock, long fallbackPeriodNanos);

    private static native long nativeCreateVBlank(NativeClock clock, int screen,
                                                  long fallbackPeriodNanos);

    private static native void nativeStart(long ptr);

    private static native void nativeStop(long ptr);

    private static native void nativeRelease(long ptr);
}
