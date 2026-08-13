/*
 * Copyright 2026 JetBrains s.r.o.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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
 */

import sun.awt.FramePacing;

import jdk.test.lib.Asserts;

/**
 * @test
 * @key headful
 * @summary Each platform must report its expected native backend tier, so a
 * silent fallback to the timer (which the contract tests tolerate by
 * design) is caught. macOS and Windows both expect DISPLAY_LINK — a
 * per-display hardware vblank, CVDisplayLink and IDXGIOutput respectively
 * — and Linux expects ESTIMATED until v3. Environments where the
 * per-display clock is legitimately unavailable should exclude this test;
 * on Windows that means a remote session, where no output is attached to
 * the desktop and the service drops to the DWM composition clock.
 * @library /test/lib
 * @compile --add-exports java.desktop/sun.awt=ALL-UNNAMED
 * --add-exports java.base/com.jetbrains.exported=ALL-UNNAMED
 * FramePacingTestUtil.java FramePacingQualityTest.java
 * @run main/othervm
 * --add-exports java.desktop/sun.awt=ALL-UNNAMED
 * --add-exports java.base/com.jetbrains.exported=ALL-UNNAMED
 * FramePacingQualityTest
 */
public class FramePacingQualityTest {

    public static void main(String[] args) throws Exception {
        FramePacing service = FramePacingTestUtil.createPlatformService();
        int quality = service.getQuality();

        String os = System.getProperty("os.name").toLowerCase();
        int expected;
        if (os.contains("mac") || os.contains("windows")) {
            expected = FramePacing.QUALITY_DISPLAY_LINK;
        } else {
            expected = FramePacing.QUALITY_ESTIMATED;
        }

        Asserts.assertEquals(quality, expected,
                "unexpected backend tier on " + os + " — a native clock silently "
                        + "failed to initialize, or a backend regressed to the timer");
    }
}
