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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class JBRSkiaApiTest {
    @JBRApi.Provided("JBRSkia")
    private interface TestJBRSkia {
        TestJBRSkia INSTANCE = JBRApi.internalService();
    }

    public static void main(String[] args) throws Exception {
        assertEquals(62, JBRSkia.ABI_ID, "ABI_ID");
        assertEquals(3, JBRSkia.NATIVE_ABI_VERSION, "NATIVE_ABI_VERSION");
        assertEquals("skia=m147-64a2414108;flags=macos-release-metal-poc:1;abi=62;native=3", JBRSkia.BUILD_ID, "BUILD_ID");

        assertReflectiveStaticEquals(62, JBRSkia.class.getDeclaredField("ABI_ID"));
        assertReflectiveStaticEquals(3, JBRSkia.class.getDeclaredField("NATIVE_ABI_VERSION"));
        assertReflectiveStaticEquals("skia=m147-64a2414108;flags=macos-release-metal-poc:1;abi=62;native=3", JBRSkia.class.getDeclaredField("BUILD_ID"));

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
        assertEquals(expectedCommandCapabilities(), service.getCommandCapabilities(), "command capabilities");
        assertEquals(expectedCommandCapabilities64(), service.getCommandCapabilities64(), "64-bit command capabilities");
        assertCommandStreamValidation();
        var image = new BufferedImage(32, 24, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setClip(new Rectangle(2, 3, 11, 13));
            JBRSkia.ScopedSkiaCanvas scope = service.acquireCanvas(graphics);
            assertEquals(JBRSkia.ScopedSkiaCanvas.BACKEND_METAL, scope.getBackend(), "backend");
            assertEquals(0L, scope.getCanvasPtr(), "canvas pointer placeholder");
            assertEquals(0L, scope.getDirectContextPtr(), "direct context pointer placeholder");
            assertEquals(0L, scope.getSurfaceId(), "surface id for software test surface");
            assertEquals(0L, scope.getContextId(), "context id for software test surface");
            assertEquals(0L, scope.getMetalTexturePtr(), "metal texture pointer for software test surface");
            assertEquals(1, scope.getSampleCount(), "sample count");
            assertEquals(new Rectangle(2, 3, 11, 13), scope.getUserSpaceClip(), "clip");
            assertEquals(true, scope.renderCommandBufferFrame(32, 24, 1L, commandBuffer(validClearStream())), "command byte buffer");
            assertEquals(true, scope.renderCommandDirectFrame(32, 24, 1L, directCommandBuffer(validClearStream())), "direct command buffer");
            assertEquals(false, scope.renderCommandBufferFrame(32, 24, 1L, new byte[] { 1, 2, 3 }), "unaligned command byte buffer");
            assertEquals(false, scope.renderCommandDirectFrame(32, 24, 1L, ByteBuffer.wrap(new byte[] { 1, 2, 3 })), "unaligned direct command buffer");
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

    private static int expectedCommandCapabilities() {
        return (int) expectedCommandCapabilities64();
    }

    private static long expectedCommandCapabilities64() {
        return JBRSkia.COMMAND_CAP_CLEAR
                | JBRSkia.COMMAND_CAP_FILL_RECT
                | JBRSkia.COMMAND_CAP_STROKE_LINE
                | JBRSkia.COMMAND_CAP_FILL_OVAL
                | JBRSkia.COMMAND_CAP_STROKE_OVAL
                | JBRSkia.COMMAND_CAP_CLEAR_RECT
                | JBRSkia.COMMAND_CAP_SAVE_RESTORE
                | JBRSkia.COMMAND_CAP_CLIP_RECT
                | JBRSkia.COMMAND_CAP_USER_SPACE_COORDINATES
                | JBRSkia.COMMAND_CAP_RECORD_ANTIALIAS
                | JBRSkia.COMMAND_CAP_STROKE_METADATA
                | JBRSkia.COMMAND_CAP_BASIC_TRANSFORMS
                | JBRSkia.COMMAND_CAP_CLIP_RECT_OP
                | JBRSkia.COMMAND_CAP_SAVE_LAYER
                | JBRSkia.COMMAND_CAP_DRAW_IMAGE_ARGB
                | JBRSkia.COMMAND_CAP_IMAGE_CACHE
                | JBRSkia.COMMAND_CAP_DRAW_TEXT_UTF16
                | JBRSkia.COMMAND_CAP_CLEAR_IMAGE_CACHE
                | JBRSkia.COMMAND_CAP_DRAW_PARAGRAPH_UTF16
                | JBRSkia.COMMAND_CAP_PARAGRAPH_FONT_STYLE
                | JBRSkia.COMMAND_CAP_PARAGRAPH_LAYOUT
                | JBRSkia.COMMAND_CAP_PARAGRAPH_LINE_HEIGHT
                | JBRSkia.COMMAND_CAP_PARAGRAPH_OVERFLOW
                | JBRSkia.COMMAND_CAP_PARAGRAPH_DECORATION
                | JBRSkia.COMMAND_CAP_PARAGRAPH_LETTER_SPACING
                | JBRSkia.COMMAND_CAP_PARAGRAPH_BACKGROUND
                | JBRSkia.COMMAND_CAP_CLIP_PATH
                | JBRSkia.COMMAND_CAP_DRAW_PATH
                | JBRSkia.COMMAND_CAP_DRAW_ARC
                | JBRSkia.COMMAND_CAP_DRAW_ROUND_RECT
                | JBRSkia.COMMAND_CAP64_FILL_RECT_LINEAR_GRADIENT
                | JBRSkia.COMMAND_CAP64_FILL_ROUND_RECT_LINEAR_GRADIENT
                | JBRSkia.COMMAND_CAP64_FILL_RECT_RADIAL_GRADIENT
                | JBRSkia.COMMAND_CAP64_FILL_ROUND_RECT_RADIAL_GRADIENT
                | JBRSkia.COMMAND_CAP64_FILL_PATH_LINEAR_GRADIENT
                | JBRSkia.COMMAND_CAP64_FILL_PATH_RADIAL_GRADIENT
                | JBRSkia.COMMAND_CAP64_FILL_RECT_SWEEP_GRADIENT
                | JBRSkia.COMMAND_CAP64_FILL_ROUND_RECT_SWEEP_GRADIENT
                | JBRSkia.COMMAND_CAP64_FILL_PATH_SWEEP_GRADIENT
                | JBRSkia.COMMAND_CAP64_EVICT_IMAGE_CACHE_KEY
                | JBRSkia.COMMAND_CAP64_TEXT_FONT_FAMILY
                | JBRSkia.COMMAND_CAP64_FILL_RECT_IMAGE_SHADER
                | JBRSkia.COMMAND_CAP64_STROKE_RECT_LINEAR_GRADIENT
                | JBRSkia.COMMAND_CAP64_STROKE_ROUND_RECT_LINEAR_GRADIENT
                | JBRSkia.COMMAND_CAP64_STROKE_RECT_RADIAL_GRADIENT
                | JBRSkia.COMMAND_CAP64_STROKE_ROUND_RECT_RADIAL_GRADIENT
                | JBRSkia.COMMAND_CAP64_STROKE_RECT_SWEEP_GRADIENT
                | JBRSkia.COMMAND_CAP64_STROKE_ROUND_RECT_SWEEP_GRADIENT
                | JBRSkia.COMMAND_CAP64_FILL_RECT_BLEND_MODE
                | JBRSkia.COMMAND_CAP64_FILL_RECT_COLOR_FILTER
                | JBRSkia.COMMAND_CAP64_STROKE_LINE_DASH_PATH_EFFECT
                | JBRSkia.COMMAND_CAP64_SAVE_LAYER_COLOR_FILTER
                | JBRSkia.COMMAND_CAP64_DRAW_IMAGE_REF_COLOR_FILTER
                | JBRSkia.COMMAND_CAP64_DEFINE_COLOR_FILTER_TINT
                | JBRSkia.COMMAND_CAP64_FILL_RECT_COLOR_FILTER_REF
                | JBRSkia.COMMAND_CAP64_EVICT_COLOR_FILTER_HANDLE
                | JBRSkia.COMMAND_CAP64_DEFINE_EFFECT_DESCRIPTOR;
    }

    private static void assertCommandStreamValidation() {
        assertValidCommandStream(validClearStream(), "valid clear stream");
        assertValidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 4,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_CLEAR, 16, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS, 0xff000000
        }, "valid antialiased clear stream");
        assertValidCommandStream(validStrokeLineStream(), "valid stroke metadata stream");
        assertValidCommandStream(validTransformStream(), "valid transform stream");
        assertValidCommandStream(validClipOpStream(), "valid clip operation stream");
        assertValidCommandStream(validSaveLayerStream(), "valid saveLayer stream");
        assertValidCommandStream(validImageArgbStream(), "valid ARGB image stream");
        assertValidCommandStream(validImageCacheStream(), "valid image cache stream");
        assertValidCommandStream(validTextStream(), "valid text stream");
        assertValidCommandStream(validLatin1TextStream(), "valid Latin-1 text stream");
        assertValidCommandStream(validLinearGradientStrokeStream(), "valid linear-gradient stroke stream");
        assertValidCommandStream(validLinearGradientStrokeRoundRectStream(), "valid linear-gradient stroke round-rect stream");
        assertValidCommandStream(validRadialGradientStrokeStream(), "valid radial-gradient stroke stream");
        assertValidCommandStream(validRadialGradientStrokeRoundRectStream(), "valid radial-gradient stroke round-rect stream");
        assertValidCommandStream(validSweepGradientStrokeStream(), "valid sweep-gradient stroke stream");
        assertValidCommandStream(validSweepGradientStrokeRoundRectStream(), "valid sweep-gradient stroke round-rect stream");
        assertValidCommandStream(validFillRectPlusBlendModeStream(), "valid fill rect plus blend-mode stream");
        assertValidCommandStream(validFillRectMultiplyBlendModeStream(), "valid fill rect multiply blend-mode stream");
        assertValidCommandStream(validFillRectScreenBlendModeStream(), "valid fill rect screen blend-mode stream");
        assertValidCommandStream(validFillRectOverlayBlendModeStream(), "valid fill rect overlay blend-mode stream");
        assertValidCommandStream(validFillRectDarkenBlendModeStream(), "valid fill rect darken blend-mode stream");
        assertValidCommandStream(validFillRectTintColorFilterStream(), "valid fill rect tint color-filter stream");
        assertValidCommandStream(validFillRectTintColorFilterHandleStream(), "valid fill rect tint color-filter handle stream");
        assertValidCommandStream(validColorFilterHandleEvictStream(), "valid color-filter handle evict stream");
        assertValidCommandStream(validDashedStrokeLineStream(), "valid dashed stroke line stream");
        assertValidCommandStream(validSaveLayerTintColorFilterStream(), "valid saveLayer tint color-filter stream");
        assertValidCommandStream(validImageRefTintColorFilterStream(), "valid image-ref tint color-filter stream");
        assertValidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 3,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_CLEAR_IMAGE_CACHE, 12, JBRSkia.COMMAND_RECORD_FLAGS_NONE
        }, "valid image cache clear stream");
        assertInvalidCommandStream(new int[] { JBRSkia.COMMAND_CLEAR, 0xff000000 }, "missing header");
        assertInvalidCommandStream(new int[] {
                0, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 0,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB
        }, "wrong magic");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID - 1, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 0,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB
        }, "wrong abi");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, 1, 0,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB
        }, "unsupported flags");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 0,
                0, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB
        }, "unsupported coordinate space");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 0,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, 0
        }, "unsupported paint format");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, -1,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB
        }, "negative payload length");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 2,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_CLEAR
        }, "truncated payload");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 1,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_CLEAR, 0xff000000
        }, "extra payload");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 3,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_CLEAR, 12, JBRSkia.COMMAND_RECORD_FLAGS_NONE
        }, "wrong command record length");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 4,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_CLEAR, 16, 2, 0xff000000
        }, "unsupported command record flags");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 12,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_STROKE_LINE, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0xffffffff, 1, 2, 11, 12, 3, 9, 0, 4000
        }, "invalid stroke cap");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 5,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_TRANSLATE, 20, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS, 1000, 2000
        }, "transform record flags");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 8,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_CLIP_RECT, 32, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS, 1, 2, 10, 10, 3
        }, "invalid clip operation");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 8,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_SAVE_LAYER, 32, JBRSkia.COMMAND_RECORD_FLAGS_NONE, 1, 2, 10, 10, 1001
        }, "invalid saveLayer alpha");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_SAVE_LAYER_COLOR_FILTER, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_PLUS
        }, "invalid saveLayer color-filter blend mode");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_COLOR_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xffff00ff, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN, 3, 4, 10, 20
        }, "undefined color-filter handle");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                99, JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN
        }, "unknown effect descriptor type");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER, 2,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN
        }, "unsupported effect descriptor version");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                1, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN
        }, "effect descriptor payload count mismatch");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN
        }, "effect descriptor record length mismatch");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_PLUS
        }, "unsupported effect descriptor blend mode");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 25,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_EVICT_COLOR_FILTER_HANDLE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_FILL_RECT_COLOR_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xffff00ff, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN, 3, 4, 10, 20
        }, "evicted effect descriptor handle");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 20,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_IMAGE_ARGB, 80, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0, 0, 2000, 2000, 10000, 20000, 30000, 40000, 2, 2, 1001, 1, 4,
                0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffffff
        }, "invalid image alpha");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_TEXT_UTF16, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0, 12000, 0, 0xff000000, 0, 0
        }, "invalid text font size");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 21,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_STROKE_RECT_LINEAR_GRADIENT, 84, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 11, 12, 0, 1, 0, 4000,
                1, 2, 11, 12, 1, 2, 0xff22d3ee, 0, 0xfff97316, 1000
        }, "invalid linear-gradient stroke width");
    }

    private static void assertValidCommandStream(int[] commands, String name) {
        if (!JBRSkiaService.isValidCommandStreamForTesting(commands)) {
            throw new AssertionError(name + " should be valid");
        }
    }

    private static int[] validClearStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 4,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_CLEAR, 16, JBRSkia.COMMAND_RECORD_FLAGS_NONE, 0xff000000
        };
    }

    private static int[] validStrokeLineStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 12,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_STROKE_LINE, 48, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xffffffff, 1, 2, 11, 12, 3, 1, 0, 4000
        };
    }

    private static int[] validTransformStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 20,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_SAVE, 12, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                JBRSkia.COMMAND_TRANSLATE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE, 1250, 2500,
                JBRSkia.COMMAND_SCALE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE, 1500, 500,
                JBRSkia.COMMAND_ROTATE, 16, JBRSkia.COMMAND_RECORD_FLAGS_NONE, 18000,
                JBRSkia.COMMAND_RESTORE, 12, JBRSkia.COMMAND_RECORD_FLAGS_NONE
        };
    }

    private static int[] validClipOpStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 8,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_CLIP_RECT, 32, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                1, 2, 10, 10, JBRSkia.COMMAND_CLIP_OP_DIFFERENCE
        };
    }

    private static int[] validSaveLayerStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 11,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_SAVE_LAYER, 32, JBRSkia.COMMAND_RECORD_FLAGS_NONE, 1, 2, 10, 10, 600,
                JBRSkia.COMMAND_RESTORE, 12, JBRSkia.COMMAND_RECORD_FLAGS_NONE
        };
    }

    private static int[] validImageArgbStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 20,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_IMAGE_ARGB, 80, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0, 0, 2000, 2000, 10000, 20000, 30000, 40000, 2, 2, 600, 1, 4,
                0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffffff
        };
    }

    private static int[] validImageCacheStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 29,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_IMAGE_ARGB, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x12345678, 0x0abcdef0, 2, 2, 4,
                0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffffff,
                JBRSkia.COMMAND_DRAW_IMAGE_REF, 68, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0, 0, 2000, 2000, 10000, 20000, 30000, 40000,
                0x12345678, 0x0abcdef0, 2, 2, 600, 1
        };
    }

    private static int[] validTextStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 16,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_TEXT_UTF16, 64, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                1250, 18500, 13000, 0xff000000, 5, 'I', 'n', 't', 'e', 'r', 2, 'H', 'i'
        };
    }

    private static int[] validLatin1TextStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 13,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_TEXT_UTF16, 52, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                1250, 18500, 13000, 0xff000000, 0, 4, 'C', 'a', 'f', '\u00e9'
        };
    }

    private static int[] validLinearGradientStrokeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 21,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_STROKE_RECT_LINEAR_GRADIENT, 84, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                1, 2, 11, 12, 12000, 1, 0, 4000,
                1, 2, 11, 12, 1, 2, 0xff22d3ee, 0, 0xfff97316, 1000
        };
    }

    private static int[] validLinearGradientStrokeRoundRectStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 23,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT, 92, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                1, 2, 11, 12, 3000, 4000, 12000, 1, 0, 4000,
                1, 2, 11, 12, 1, 2, 0xff22d3ee, 0, 0xfff97316, 1000
        };
    }

    private static int[] validRadialGradientStrokeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 20,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_STROKE_RECT_RADIAL_GRADIENT, 80, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                1, 2, 11, 12, 12000, 1, 0, 4000,
                6, 7, 8000, 1, 2, 0xff22d3ee, 0, 0xfff97316, 1000
        };
    }

    private static int[] validRadialGradientStrokeRoundRectStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 22,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT, 88, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                1, 2, 11, 12, 3000, 4000, 12000, 1, 0, 4000,
                6, 7, 8000, 1, 2, 0xff22d3ee, 0, 0xfff97316, 1000
        };
    }

    private static int[] validSweepGradientStrokeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 18,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_STROKE_RECT_SWEEP_GRADIENT, 72, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                1, 2, 11, 12, 12000, 1, 0, 4000,
                6, 7, 2, 0xff22d3ee, 0, 0xfff97316, 1000
        };
    }

    private static int[] validSweepGradientStrokeRoundRectStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 20,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_STROKE_ROUND_RECT_SWEEP_GRADIENT, 80, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                1, 2, 11, 12, 3000, 4000, 12000, 1, 0, 4000,
                6, 7, 2, 0xff22d3ee, 0, 0xfff97316, 1000
        };
    }

    private static int[] validFillRectPlusBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_PLUS, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectMultiplyBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_MULTIPLY, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectScreenBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_SCREEN, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectOverlayBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_OVERLAY, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectDarkenBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_DARKEN, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectTintColorFilterStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_COLOR_FILTER, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xffff00ff, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN, 3, 4, 10, 20
        };
    }

    private static int[] validFillRectTintColorFilterHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 20,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_FILL_RECT_COLOR_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xffff00ff, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN, 3, 4, 10, 20
        };
    }

    private static int[] validColorFilterHandleEvictStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 15,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_EVICT_COLOR_FILTER_HANDLE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN
        };
    }

    private static int[] validDashedStrokeLineStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 16,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_STROKE_LINE_DASH_PATH_EFFECT, 64, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xffffffff, 1, 2, 11, 12, 8, 0, 1, 0, 3000, 2, 16000, 10000
        };
    }

    private static int[] validSaveLayerTintColorFilterStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_SAVE_LAYER_COLOR_FILTER, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN
        };
    }

    private static int[] validImageRefTintColorFilterStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 28,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_IMAGE_ARGB, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 1, 1, 1, 0xffffffff,
                JBRSkia.COMMAND_DRAW_IMAGE_REF_COLOR_FILTER, 76, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0, 0, 1000, 1000, 10, 20, 30, 40, 1, 2, 1, 1, 600, 1,
                0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN
        };
    }

    private static byte[] commandBuffer(int[] commands) {
        byte[] encoded = new byte[commands.length * Integer.BYTES];
        for (int index = 0; index < commands.length; index++) {
            int value = commands[index];
            int offset = index * Integer.BYTES;
            encoded[offset] = (byte) value;
            encoded[offset + 1] = (byte) (value >>> 8);
            encoded[offset + 2] = (byte) (value >>> 16);
            encoded[offset + 3] = (byte) (value >>> 24);
        }
        return encoded;
    }

    private static ByteBuffer directCommandBuffer(int[] commands) {
        ByteBuffer encoded = ByteBuffer.allocateDirect(commands.length * Integer.BYTES).order(ByteOrder.LITTLE_ENDIAN);
        for (int command : commands) {
            encoded.putInt(command);
        }
        return encoded.flip();
    }

    private static void assertInvalidCommandStream(int[] commands, String name) {
        if (JBRSkiaService.isValidCommandStreamForTesting(commands)) {
            throw new AssertionError(name + " should be invalid");
        }
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        if (!expected.equals(actual)) {
            throw new AssertionError(name + ": expected=" + expected + ", actual=" + actual);
        }
    }
}
