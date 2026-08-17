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
import sun.awt.wl.WLGraphicsDevice;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.concurrent.locks.LockSupport;

/**
 * Linux backend, preferring a per-CRTC hardware vblank clock built on the
 * kernel DRM {@code DRM_IOCTL_WAIT_VBLANK} ioctl and reporting
 * {@code QUALITY_DISPLAY_LINK}: it blocks on the hardware vblank interrupt of
 * one specific CRTC and delivers the kernel's own vblank timestamp, so ticks
 * carry hardware cadence (measured jitter is microseconds) at well under 1% of
 * a core, and — unlike every surface-tied mechanism on Wayland
 * ({@code wp_presentation} feedback, XWayland Present) — the clock free-runs
 * whether or not the subscriber is submitting frames, which the subscription
 * contract requires.
 *
 * <p>The CRTC is chosen by matching each active CRTC's mode period against the
 * display's advertised refresh period, across every DRM card node. On a
 * single-display system that binds the only active CRTC. On X11 the toolkit
 * exposes one device per X screen (the whole desktop under XWayland or
 * Xinerama), so a finer binding is not expressible through the toolkit id;
 * per-monitor binding for the Wayland toolkit's per-output ids is follow-up
 * work.
 *
 * <p>When no DRM display device is accessible (remote X, Xvfb, containers) the
 * service falls back to the shared timer and {@code QUALITY_ESTIMATED}.
 */
@JBRApi.Service
@JBRApi.Provides("FramePacing")
public class FramePacingUnix extends FramePacing {

    private final boolean vblankClockAvailable;

    public FramePacingUnix() {
        this.vblankClockAvailable = probe();
    }

    private boolean probe() {
        if (FORCE_ESTIMATED) return false;

        try {
            // The natives live in the X toolkit library, which loads with the
            // graphics environment (libawt's OnLoad picks the headful
            // library). Service creation can precede any other AWT use, so
            // force that initialization before resolving the probe.
            GraphicsEnvironment.getLocalGraphicsEnvironment();
            return nativeProbe();
        } catch (UnsatisfiedLinkError | RuntimeException e) {
            // A toolkit that does not load these natives (e.g. the Wayland
            // toolkit's library set) or a failed probe leaves the timer
            // backend.
            return false;
        }
    }

    @Override
    public int getQuality() {
        return vblankClockAvailable ? QUALITY_DISPLAY_LINK : QUALITY_ESTIMATED;
    }

    @Override
    protected DisplayClock createClock(long displayId, long periodNanos) {
        if (vblankClockAvailable) {
            return new DrmVBlankClock(this, displayId, periodNanos);
        }
        return super.createClock(displayId, periodNanos);
    }

    @Override
    protected long deviceId(GraphicsDevice device) {
        if (device instanceof X11GraphicsDevice x11Device) {
            // X screen number; stable for the lifetime of the connection.
            return makePositive(x11Device.getScreen());
        }

        if (device instanceof WLGraphicsDevice wlDevice) {
            // Wayland output id from the registry.
            return makePositive(wlDevice.getID());
        }

        // Other headed toolkits (e.g., remote displays): stable id-string hash.
        String id = device.getIDstring();
        return id == null ? -1 : makePositive(id.hashCode());
    }

    /**
     * Per-CRTC vblank tick source: the native thread blocks in the DRM vblank
     * wait ioctl and delivers from that thread. If native setup fails for this
     * display, the clock behaves like the shared timer at the nominal period.
     */
    private static final class DrmVBlankClock extends DisplayClock {
        private volatile long ptr;

        DrmVBlankClock(FramePacingUnix service, long displayId, long periodNanos) {
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

    private static native boolean nativeProbe();

    private static native long nativeCreate(DrmVBlankClock clock, long fallbackPeriodNanos);

    private static native void nativeStart(long ptr);

    private static native void nativeStop(long ptr);

    private static native void nativeRelease(long ptr);
}
