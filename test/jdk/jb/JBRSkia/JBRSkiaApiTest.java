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
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @requires os.family == "mac"
 * @summary Verifies the experimental JBR Skia service ABI surface.
 * @modules java.base/com.jetbrains.exported
 *          java.desktop/com.jetbrains.desktop
 * @compile --add-exports java.base/com.jetbrains.exported=ALL-UNNAMED
 *          --add-exports java.desktop/com.jetbrains.desktop=ALL-UNNAMED JBRSkiaApiTest.java
 * @run main/othervm --add-exports java.base/com.jetbrains.exported=ALL-UNNAMED
 *          --add-exports java.desktop/com.jetbrains.desktop=ALL-UNNAMED JBRSkiaApiTest
 */

import com.jetbrains.desktop.JBRSkia;
import com.jetbrains.desktop.JBRSkiaService;
import com.jetbrains.exported.JBRApi;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

public class JBRSkiaApiTest {
    @JBRApi.Provided("JBRSkia")
    private interface TestJBRSkia {
        TestJBRSkia INSTANCE = JBRApi.internalService();
    }

    public static void main(String[] args) throws Exception {
        assertEquals(1, JBRSkia.ABI_ID, "ABI_ID");
        assertEquals("skia-interop-poc:1", JBRSkia.BUILD_ID, "BUILD_ID");

        assertReflectiveStaticEquals(1, JBRSkia.class.getDeclaredField("ABI_ID"));
        assertReflectiveStaticEquals("skia-interop-poc:1", JBRSkia.class.getDeclaredField("BUILD_ID"));

        if (TestJBRSkia.INSTANCE != null) {
            throw new AssertionError("JBRSkia service must be unavailable before native runtime is wired");
        }

        try {
            new JBRSkiaService();
            throw new AssertionError("JBRSkiaService constructor must signal unavailability without runtime flag");
        } catch (JBRApi.ServiceNotAvailableException expected) {
            // expected
        }

        System.setProperty("sun.java2d.skia.interop", "true");
        var service = new JBRSkiaService();
        var image = new BufferedImage(32, 24, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setClip(new Rectangle(2, 3, 11, 13));
            JBRSkia.ScopedSkiaCanvas scope = service.acquireCanvas(graphics);
            assertEquals(JBRSkia.ScopedSkiaCanvas.BACKEND_METAL, scope.getBackend(), "backend");
            assertEquals(0L, scope.getCanvasPtr(), "canvas pointer placeholder");
            assertEquals(0L, scope.getDirectContextPtr(), "direct context pointer placeholder");
            assertEquals(0L, scope.getMetalTexturePtr(), "metal texture pointer for software test surface");
            assertEquals(1, scope.getSampleCount(), "sample count");
            assertEquals(new Rectangle(2, 3, 11, 13), scope.getUserSpaceClip(), "clip");
            scope.flush();
            scope.close();
            try {
                scope.flush();
                throw new AssertionError("flush after close must fail");
            } catch (IllegalStateException expected) {
                // expected
            }
        } finally {
            graphics.dispose();
            System.clearProperty("sun.java2d.skia.interop");
        }
    }

    private static void assertReflectiveStaticEquals(Object expected, Field field) throws IllegalAccessException {
        assertEquals(expected, field.get(null), field.getName());
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        if (!expected.equals(actual)) {
            throw new AssertionError(name + ": expected=" + expected + ", actual=" + actual);
        }
    }
}
