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

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @test
 * @key headful
 * @summary -Djbr.framePacing.forceEstimated=true must force the shared timer
 * backend on every platform: quality reports ESTIMATED and ticks
 * are delivered by the timer clock even where a native clock exists.
 * @library /test/lib
 * @compile --add-exports java.desktop/sun.awt=ALL-UNNAMED
 * --add-exports java.base/com.jetbrains.exported=ALL-UNNAMED
 * FramePacingTestUtil.java FramePacingForceEstimatedTest.java
 * @run main/othervm -Djbr.framePacing.forceEstimated=true
 * --add-exports java.desktop/sun.awt=ALL-UNNAMED
 * --add-exports java.base/com.jetbrains.exported=ALL-UNNAMED
 * FramePacingForceEstimatedTest
 */
public class FramePacingForceEstimatedTest {

    public static void main(String[] args) throws Exception {
        FramePacing service = FramePacingTestUtil.createPlatformService();

        Asserts.assertEquals(service.getQuality(), FramePacing.QUALITY_ESTIMATED,
                "forceEstimated must report ESTIMATED quality on every platform");

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
        long displayId = service.displayId(gc);
        Asserts.assertNotEquals(displayId, -1L, "default screen must resolve to a display id");

        // The forced timer must actually tick.
        CountDownLatch ticks = new CountDownLatch(5);
        FramePacing.Subscription s = service.subscribe(displayId, (id, t) -> ticks.countDown());
        Asserts.assertNotNull(s);
        Asserts.assertTrue(ticks.await(10, TimeUnit.SECONDS),
                "forced timer backend did not deliver ticks");
        s.close();
    }
}
