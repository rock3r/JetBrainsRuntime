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
        assertEquals(106, JBRSkia.ABI_ID, "ABI_ID");
        assertEquals(3, JBRSkia.NATIVE_ABI_VERSION, "NATIVE_ABI_VERSION");
        assertEquals("skia=m147-64a2414108;flags=macos-release-metal-poc:1;abi=106;native=3", JBRSkia.BUILD_ID, "BUILD_ID");

        assertReflectiveStaticEquals(106, JBRSkia.class.getDeclaredField("ABI_ID"));
        assertReflectiveStaticEquals(3, JBRSkia.class.getDeclaredField("NATIVE_ABI_VERSION"));
        assertReflectiveStaticEquals("skia=m147-64a2414108;flags=macos-release-metal-poc:1;abi=106;native=3", JBRSkia.class.getDeclaredField("BUILD_ID"));

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
        assertEquals(expectedCommandCapabilities64High(), service.getCommandCapabilities64High(), "high 64-bit command capabilities");
        assertCommandStreamValidation();
        if (Boolean.getBoolean("jbrskia.parserOnly")) {
            return;
        }
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
                | JBRSkia.COMMAND_CAP64_DEFINE_EFFECT_DESCRIPTOR
                | JBRSkia.COMMAND_CAP64_SAVE_LAYER_BLEND_MODE
                | JBRSkia.COMMAND_CAP64_SAVE_LAYER_BLEND_COLOR_FILTER
                | JBRSkia.COMMAND_CAP64_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER
                | JBRSkia.COMMAND_CAP64_EFFECT_DESCRIPTOR_LIGHTING_FILTER
                | JBRSkia.COMMAND_CAP64_SAVE_LAYER_COLOR_FILTER_REF
                | JBRSkia.COMMAND_CAP64_DRAW_IMAGE_REF_COLOR_FILTER_REF
                | JBRSkia.COMMAND_CAP64_SAVE_LAYER_BLEND_COLOR_FILTER_REF;
    }

    private static long expectedCommandCapabilities64High() {
        return JBRSkia.COMMAND_CAP64_HIGH_SAVE_LAYER_IMAGE_FILTER_REF
                | JBRSkia.COMMAND_CAP64_HIGH_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER
                | JBRSkia.COMMAND_CAP64_HIGH_EFFECT_DESCRIPTOR_CHAIN_IMAGE_FILTER
                | JBRSkia.COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_REF
                | JBRSkia.COMMAND_CAP64_HIGH_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER
                | JBRSkia.COMMAND_CAP64_HIGH_STROKE_RECT_DASH_PATH_EFFECT
                | JBRSkia.COMMAND_CAP64_HIGH_STROKE_ROUND_RECT_DASH_PATH_EFFECT
                | JBRSkia.COMMAND_CAP64_HIGH_STROKE_PATH_DASH_PATH_EFFECT
                | JBRSkia.COMMAND_CAP64_HIGH_PATH_EFFECT_DESCRIPTOR_REF
                | JBRSkia.COMMAND_CAP64_HIGH_CONCAT_MATRIX33
                | JBRSkia.COMMAND_CAP64_HIGH_DRAW_SHADOW_PATH
                | JBRSkia.COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_COLOR_FILTER
                | JBRSkia.COMMAND_CAP64_HIGH_DRAW_POINTS
                | JBRSkia.COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_TRANSFORM
                | JBRSkia.COMMAND_CAP64_HIGH_DEFINE_FONT_DATA
                | JBRSkia.COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_COLOR
                | JBRSkia.COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_PERLIN_NOISE
                | JBRSkia.COMMAND_CAP64_HIGH_DRAW_VERTICES;
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
        assertValidCommandStream(validSaveLayerBlendModeStream(), "valid saveLayer blend-mode stream");
        assertValidCommandStream(validSaveLayerBlendColorFilterStream(), "valid saveLayer blend/color-filter stream");
        assertValidCommandStream(validImageArgbStream(), "valid ARGB image stream");
        assertValidCommandStream(validImageCacheStream(), "valid image cache stream");
        assertInvalidCommandStream(invalidDrawImageRefMissingImageStream(), "undefined image ref handle");
        assertInvalidCommandStream(invalidDrawImageRefEvictedImageStream(), "evicted image ref handle");
        assertInvalidCommandStream(invalidDrawImageRefClearedImageStream(), "cleared image ref handle");
        assertInvalidCommandStream(invalidDrawImageRefDimensionMismatchStream(), "image ref dimension mismatch");
        assertInvalidCommandStream(invalidFillRectImageShaderMissingImageStream(), "undefined image shader handle");
        assertValidCommandStream(validFontDataStream(), "valid font-data stream");
        assertValidCommandStream(validTextStream(), "valid text stream");
        assertValidCommandStream(validLatin1TextStream(), "valid Latin-1 text stream");
        assertValidCommandStream(validParagraphTextStream(), "valid paragraph text stream");
        assertValidCommandStream(validLinearGradientStrokeStream(), "valid linear-gradient stroke stream");
        assertValidCommandStream(validDrawPointsStream(), "valid draw-points stream");
        assertValidCommandStream(validDrawVerticesStream(), "valid draw-vertices stream");
        assertInvalidCommandStream(invalidDrawPointsPointCountStream(), "invalid draw-points point count");
        assertInvalidCommandStream(invalidDrawPointsMaxPointCountStream(), "invalid draw-points max point count");
        assertInvalidCommandStream(invalidDrawPointsRecordLengthStream(), "invalid draw-points record length");
        assertInvalidCommandStream(invalidDrawVerticesVertexCountStream(), "invalid draw-vertices vertex count");
        assertInvalidCommandStream(invalidDrawVerticesMaxVertexCountStream(), "invalid draw-vertices max vertex count");
        assertInvalidCommandStream(invalidDrawVerticesRecordLengthStream(), "invalid draw-vertices record length");
        assertInvalidCommandStream(invalidDrawVerticesVertexModeStream(), "invalid draw-vertices vertex mode");
        assertInvalidCommandStream(invalidDrawVerticesBlendModeStream(), "invalid draw-vertices blend mode");
        assertInvalidCommandStream(invalidDrawVerticesIndexCountStream(), "invalid draw-vertices index count");
        assertInvalidCommandStream(invalidDrawVerticesMaxIndexCountStream(), "invalid draw-vertices max index count");
        assertValidCommandStream(validLinearGradientStrokeRoundRectStream(), "valid linear-gradient stroke round-rect stream");
        assertValidCommandStream(validRadialGradientStrokeStream(), "valid radial-gradient stroke stream");
        assertValidCommandStream(validRadialGradientStrokeRoundRectStream(), "valid radial-gradient stroke round-rect stream");
        assertValidCommandStream(validSweepGradientStrokeStream(), "valid sweep-gradient stroke stream");
        assertValidCommandStream(validSweepGradientStrokeRoundRectStream(), "valid sweep-gradient stroke round-rect stream");
        assertValidCommandStream(validLinearGradientPathStream(), "valid linear-gradient path stream");
        assertInvalidCommandStream(invalidLinearGradientPathTileModeStream(), "invalid linear-gradient path tile mode");
        assertInvalidCommandStream(invalidLinearGradientPathColorCountStream(), "invalid linear-gradient path color count");
        assertInvalidCommandStream(invalidLinearGradientPathStopOrderStream(), "invalid linear-gradient path stop order");
        assertInvalidCommandStream(invalidLinearGradientPathFillTypeStream(), "invalid linear-gradient path fill type");
        assertInvalidCommandStream(invalidLinearGradientPathDataLengthStream(), "invalid linear-gradient path data length");
        assertInvalidCommandStream(invalidLinearGradientPathVerbStream(), "invalid linear-gradient path verb");
        assertValidCommandStream(validRadialGradientPathStream(), "valid radial-gradient path stream");
        assertInvalidCommandStream(invalidRadialGradientPathRadiusStream(), "invalid radial-gradient path radius");
        assertInvalidCommandStream(invalidRadialGradientPathTileModeStream(), "invalid radial-gradient path tile mode");
        assertInvalidCommandStream(invalidRadialGradientPathColorCountStream(), "invalid radial-gradient path color count");
        assertInvalidCommandStream(invalidRadialGradientPathStopOrderStream(), "invalid radial-gradient path stop order");
        assertInvalidCommandStream(invalidRadialGradientPathFillTypeStream(), "invalid radial-gradient path fill type");
        assertInvalidCommandStream(invalidRadialGradientPathDataLengthStream(), "invalid radial-gradient path data length");
        assertInvalidCommandStream(invalidRadialGradientPathVerbStream(), "invalid radial-gradient path verb");
        assertValidCommandStream(validSweepGradientPathStream(), "valid sweep-gradient path stream");
        assertInvalidCommandStream(invalidSweepGradientPathColorCountStream(), "invalid sweep-gradient path color count");
        assertInvalidCommandStream(invalidSweepGradientPathStopOrderStream(), "invalid sweep-gradient path stop order");
        assertInvalidCommandStream(invalidSweepGradientPathFillTypeStream(), "invalid sweep-gradient path fill type");
        assertInvalidCommandStream(invalidSweepGradientPathDataLengthStream(), "invalid sweep-gradient path data length");
        assertInvalidCommandStream(invalidSweepGradientPathVerbStream(), "invalid sweep-gradient path verb");
        assertValidCommandStream(validFillRectPlusBlendModeStream(), "valid fill rect plus blend-mode stream");
        assertValidCommandStream(validFillRectMultiplyBlendModeStream(), "valid fill rect multiply blend-mode stream");
        assertValidCommandStream(validFillRectScreenBlendModeStream(), "valid fill rect screen blend-mode stream");
        assertValidCommandStream(validFillRectOverlayBlendModeStream(), "valid fill rect overlay blend-mode stream");
        assertValidCommandStream(validFillRectDarkenBlendModeStream(), "valid fill rect darken blend-mode stream");
        assertValidCommandStream(validFillRectLightenBlendModeStream(), "valid fill rect lighten blend-mode stream");
        assertValidCommandStream(validFillRectDifferenceBlendModeStream(), "valid fill rect difference blend-mode stream");
        assertValidCommandStream(validFillRectExclusionBlendModeStream(), "valid fill rect exclusion blend-mode stream");
        assertValidCommandStream(validFillRectColorDodgeBlendModeStream(), "valid fill rect color-dodge blend-mode stream");
        assertValidCommandStream(validFillRectColorBurnBlendModeStream(), "valid fill rect color-burn blend-mode stream");
        assertValidCommandStream(validFillRectHardlightBlendModeStream(), "valid fill rect hardlight blend-mode stream");
        assertValidCommandStream(validFillRectSoftlightBlendModeStream(), "valid fill rect softlight blend-mode stream");
        assertValidCommandStream(validFillRectHueBlendModeStream(), "valid fill rect hue blend-mode stream");
        assertValidCommandStream(validFillRectSaturationBlendModeStream(), "valid fill rect saturation blend-mode stream");
        assertValidCommandStream(validFillRectColorBlendModeStream(), "valid fill rect color blend-mode stream");
        assertValidCommandStream(validFillRectLuminosityBlendModeStream(), "valid fill rect luminosity blend-mode stream");
        assertInvalidCommandStream(invalidFillRectBlendModeWidthStream(), "invalid fill rect blend-mode width");
        assertInvalidCommandStream(invalidFillRectBlendModeHeightStream(), "invalid fill rect blend-mode height");
        assertValidCommandStream(validFillRectTintColorFilterStream(), "valid fill rect tint color-filter stream");
        assertValidCommandStream(validFillRectTintColorFilterHandleStream(), "valid fill rect tint color-filter handle stream");
        assertInvalidCommandStream(invalidFillRectColorFilterWidthStream(), "invalid fill rect color-filter width");
        assertInvalidCommandStream(invalidFillRectColorFilterHeightStream(), "invalid fill rect color-filter height");
        assertInvalidCommandStream(invalidFillRectColorFilterRefWidthStream(), "invalid fill rect color-filter ref width");
        assertInvalidCommandStream(invalidFillRectColorFilterRefHeightStream(), "invalid fill rect color-filter ref height");
        assertValidCommandStream(validFillRectColorMatrixFilterHandleStream(), "valid fill rect color-matrix filter handle stream");
        assertValidCommandStream(validFillRectLightingFilterHandleStream(), "valid fill rect lighting filter handle stream");
        assertValidCommandStream(validRuntimeColorFilterChildDescriptorStream(), "valid runtime color-filter child descriptor stream");
        assertValidCommandStream(validOffsetImageFilterWithInputDescriptorStream(), "valid offset image-filter child descriptor stream");
        assertValidCommandStream(validChainedPathEffectDescriptorStream(), "valid chained path-effect descriptor stream");
        assertValidCommandStream(validSaveLayerColorMatrixFilterHandleStream(), "valid saveLayer color-matrix filter handle stream");
        assertValidCommandStream(validSaveLayerBlendColorMatrixFilterHandleStream(), "valid saveLayer blend/color-matrix filter handle stream");
        assertValidCommandStream(validColorFilterHandleEvictStream(), "valid color-filter handle evict stream");
        assertValidCommandStream(validDashedStrokeLineStream(), "valid dashed stroke line stream");
        assertInvalidCommandStream(invalidDashedStrokeLineIntervalCountStream(), "invalid dashed stroke line interval count");
        assertInvalidCommandStream(invalidDashedStrokeLinePhaseStream(), "invalid dashed stroke line phase");
        assertInvalidCommandStream(invalidDashedStrokeLineIntervalStream(), "invalid dashed stroke line interval");
        assertValidCommandStream(validDashedStrokeRectStream(), "valid dashed stroke rect stream");
        assertInvalidCommandStream(invalidDashedStrokeRectIntervalCountStream(), "invalid dashed stroke rect interval count");
        assertInvalidCommandStream(invalidDashedStrokeRectWidthStream(), "invalid dashed stroke rect width");
        assertInvalidCommandStream(invalidDashedStrokeRectHeightStream(), "invalid dashed stroke rect height");
        assertValidCommandStream(validDashedStrokeRoundRectStream(), "valid dashed stroke round-rect stream");
        assertInvalidCommandStream(invalidDashedStrokeRoundRectIntervalCountStream(), "invalid dashed stroke round-rect interval count");
        assertInvalidCommandStream(invalidDashedStrokeRoundRectRightStream(), "invalid dashed stroke round-rect right");
        assertInvalidCommandStream(invalidDashedStrokeRoundRectBottomStream(), "invalid dashed stroke round-rect bottom");
        assertInvalidCommandStream(invalidDashedStrokeRoundRectRadiusXStream(), "invalid dashed stroke round-rect radius x");
        assertInvalidCommandStream(invalidDashedStrokeRoundRectRadiusYStream(), "invalid dashed stroke round-rect radius y");
        assertInvalidCommandStream(invalidDashedStrokeRoundRectStrokeWidthStream(), "invalid dashed stroke round-rect stroke width");
        assertInvalidCommandStream(invalidDashedStrokeRoundRectStrokeCapStream(), "invalid dashed stroke round-rect stroke cap");
        assertInvalidCommandStream(invalidDashedStrokeRoundRectStrokeJoinStream(), "invalid dashed stroke round-rect stroke join");
        assertInvalidCommandStream(invalidDashedStrokeRoundRectStrokeMiterStream(), "invalid dashed stroke round-rect stroke miter");
        assertInvalidCommandStream(invalidDashedStrokeRoundRectPhaseStream(), "invalid dashed stroke round-rect phase");
        assertInvalidCommandStream(invalidDashedStrokeRoundRectIntervalStream(), "invalid dashed stroke round-rect interval");
        assertValidCommandStream(validDashedStrokePathStream(), "valid dashed stroke path stream");
        assertInvalidCommandStream(invalidDashedStrokePathIntervalCountStream(), "invalid dashed stroke path interval count");
        assertInvalidCommandStream(invalidDashedStrokePathStrokeWidthStream(), "invalid dashed stroke path stroke width");
        assertInvalidCommandStream(invalidDashedStrokePathStrokeCapStream(), "invalid dashed stroke path stroke cap");
        assertInvalidCommandStream(invalidDashedStrokePathStrokeJoinStream(), "invalid dashed stroke path stroke join");
        assertInvalidCommandStream(invalidDashedStrokePathStrokeMiterStream(), "invalid dashed stroke path stroke miter");
        assertInvalidCommandStream(invalidDashedStrokePathPhaseStream(), "invalid dashed stroke path phase");
        assertInvalidCommandStream(invalidDashedStrokePathIntervalStream(), "invalid dashed stroke path interval");
        assertInvalidCommandStream(invalidDashedStrokePathFillTypeStream(), "invalid dashed stroke path fill type");
        assertInvalidCommandStream(invalidDashedStrokePathDataLengthStream(), "invalid dashed stroke path data length");
        assertInvalidCommandStream(invalidDashedStrokePathVerbStream(), "invalid dashed stroke path verb");
        assertValidCommandStream(validDrawShadowPathStream(), "valid draw-shadow path stream");
        assertInvalidCommandStream(invalidDrawShadowPathPlaneStream(), "invalid draw-shadow path plane");
        assertInvalidCommandStream(invalidDrawShadowPathRadiusStream(), "invalid draw-shadow path radius");
        assertInvalidCommandStream(invalidDrawShadowPathFlagsStream(), "invalid draw-shadow path flags");
        assertInvalidCommandStream(invalidDrawShadowPathFillTypeStream(), "invalid draw-shadow path fill type");
        assertInvalidCommandStream(invalidDrawShadowPathDataLengthStream(), "invalid draw-shadow path data length");
        assertInvalidCommandStream(invalidDrawShadowPathVerbStream(), "invalid draw-shadow path verb");
        assertValidCommandStream(validClipPathStream(), "valid clip path stream");
        assertInvalidCommandStream(invalidClipPathOpStream(), "invalid clip path operation");
        assertInvalidCommandStream(invalidClipPathFillTypeStream(), "invalid clip path fill type");
        assertInvalidCommandStream(invalidClipPathDataLengthStream(), "invalid clip path data length");
        assertInvalidCommandStream(invalidClipPathVerbStream(), "invalid clip path verb");
        assertValidCommandStream(validDrawPathStream(), "valid draw path stream");
        assertInvalidCommandStream(invalidDrawPathStyleStream(), "invalid draw path style");
        assertInvalidCommandStream(invalidDrawPathStrokeWidthStream(), "invalid draw path stroke width");
        assertInvalidCommandStream(invalidDrawPathFillTypeStream(), "invalid draw path fill type");
        assertInvalidCommandStream(invalidDrawPathDataLengthStream(), "invalid draw path data length");
        assertInvalidCommandStream(invalidDrawPathVerbStream(), "invalid draw path verb");
        assertValidCommandStream(validDrawPathPathEffectRefStream(), "valid draw path path-effect-ref stream");
        assertInvalidCommandStream(invalidDrawPathPathEffectRefVerbStream(), "invalid draw path path-effect-ref verb");
        assertValidCommandStream(validSaveLayerTintColorFilterStream(), "valid saveLayer tint color-filter stream");
        assertValidCommandStream(validImageRefTintColorFilterStream(), "valid image-ref tint color-filter stream");
        assertValidCommandStream(validImageRefColorMatrixFilterHandleStream(), "valid image-ref color-matrix filter handle stream");
        assertInvalidCommandStream(invalidImageRefColorFilterPathEffectHandleStream(), "path-effect image-ref color-filter handle");
        assertValidCommandStream(validCompositeShaderDescriptorStream(), "valid composite shader descriptor stream");
        assertValidCommandStream(validRuntimeEffectShaderDescriptorStream(), "valid runtime-effect shader descriptor stream");
        assertValidCommandStream(validShaderColorFilterDescriptorStream(), "valid shader color-filter descriptor stream");
        assertValidCommandStream(validTransformedShaderDescriptorStream(), "valid transformed shader descriptor stream");
        assertValidCommandStream(validColorShaderDescriptorStream(), "valid color shader descriptor stream");
        assertInvalidCommandStream(invalidFillRectShaderRefHorizontalBoundsStream(), "invalid fill rect shader-ref horizontal bounds");
        assertInvalidCommandStream(invalidFillRectShaderRefVerticalBoundsStream(), "invalid fill rect shader-ref vertical bounds");
        assertInvalidCommandStream(invalidFillRectShaderRefAlphaStream(), "invalid fill rect shader-ref alpha");
        assertValidCommandStream(validPerlinNoiseShaderDescriptorStream(), "valid Perlin noise shader descriptor stream");
        assertInvalidCommandStream(invalidUnknownShaderDescriptorTypeStream(), "unknown shader descriptor type");
        assertInvalidCommandStream(invalidShaderDescriptorRecordFlagsStream(), "shader descriptor record flags");
        assertInvalidCommandStream(invalidShaderDescriptorVersionStream(), "unsupported shader descriptor version");
        assertInvalidCommandStream(invalidShaderDescriptorPayloadCountStream(), "shader descriptor payload count mismatch");
        assertInvalidCommandStream(invalidColorShaderDescriptorPayloadCountStream(), "color shader descriptor payload count mismatch");
        assertInvalidCommandStream(invalidShaderColorFilterDescriptorPayloadCountStream(), "shader color-filter descriptor payload count mismatch");
        assertInvalidCommandStream(invalidShaderDescriptorRecordLengthStream(), "shader descriptor record length mismatch");
        assertInvalidCommandStream(invalidCompositeShaderDescriptorBlendModeStream(), "composite shader descriptor blend mode");
        assertInvalidCommandStream(invalidLinearGradientShaderTileModeStream(), "linear gradient shader tile mode");
        assertInvalidCommandStream(invalidLinearGradientShaderStopOrderStream(), "linear gradient shader stop order");
        assertInvalidCommandStream(invalidRadialGradientShaderRadiusStream(), "radial gradient shader radius");
        assertInvalidCommandStream(invalidRadialGradientShaderTileModeStream(), "radial gradient shader tile mode");
        assertInvalidCommandStream(invalidRadialGradientShaderStopOrderStream(), "radial gradient shader stop order");
        assertInvalidCommandStream(invalidSweepGradientShaderColorCountStream(), "sweep gradient shader color count");
        assertInvalidCommandStream(invalidSweepGradientShaderStopOrderStream(), "sweep gradient shader stop order");
        assertInvalidCommandStream(invalidImageShaderWidthStream(), "image shader width");
        assertInvalidCommandStream(invalidImageShaderMaxWidthStream(), "image shader max width");
        assertInvalidCommandStream(invalidImageShaderHeightStream(), "image shader height");
        assertInvalidCommandStream(invalidImageShaderMaxHeightStream(), "image shader max height");
        assertInvalidCommandStream(invalidImageShaderTileModeXStream(), "image shader tile mode x");
        assertInvalidCommandStream(invalidImageShaderTileModeYStream(), "image shader tile mode y");
        assertInvalidCommandStream(invalidUndefinedShaderFillStream(), "undefined shader handle fill");
        assertInvalidCommandStream(invalidFillRectShaderColorFilterHandleStream(), "color-filter fill shader handle");
        assertInvalidCommandStream(invalidCompositeShaderChildHandleStream(), "undefined composite shader child handle");
        assertInvalidCommandStream(invalidCompositeShaderEvictedChildHandleStream(), "evicted composite shader child handle");
        assertInvalidCommandStream(invalidCompositeShaderColorFilterChildHandleStream(), "color-filter composite shader child handle");
        assertInvalidCommandStream(invalidShaderColorFilterMissingShaderHandleStream(), "undefined shader color-filter shader handle");
        assertInvalidCommandStream(invalidShaderColorFilterMissingEffectHandleStream(), "undefined shader color-filter effect handle");
        assertInvalidCommandStream(invalidShaderColorFilterImageFilterHandleStream(), "image-filter shader color-filter handle");
        assertInvalidCommandStream(invalidShaderColorFilterEvictedShaderHandleStream(), "evicted shader color-filter shader handle");
        assertInvalidCommandStream(invalidTransformedShaderMissingChildHandleStream(), "undefined transformed shader child handle");
        assertInvalidCommandStream(invalidTransformedShaderEvictedChildHandleStream(), "evicted transformed shader child handle");
        assertInvalidCommandStream(invalidTransformedShaderColorFilterChildHandleStream(), "color-filter transformed shader child handle");
        assertInvalidCommandStream(invalidTransformedShaderPayloadCountStream(), "transformed shader payload count mismatch");
        assertInvalidCommandStream(invalidEvictedShaderHandleStream(), "evicted shader handle fill");
        assertInvalidCommandStream(invalidShaderEvictRecordFlagsStream(), "shader evict record flags");
        assertInvalidCommandStream(invalidRuntimeEffectShaderHashStream(), "invalid runtime-effect shader source hash stream");
        assertInvalidCommandStream(invalidRuntimeEffectShaderSkslLengthStream(), "invalid runtime-effect shader SKSL length");
        assertInvalidCommandStream(invalidRuntimeEffectShaderUniformFloatCountStream(), "invalid runtime-effect shader uniform count");
        assertInvalidCommandStream(invalidRuntimeEffectShaderNegativeUniformFloatCountStream(), "invalid runtime-effect shader negative uniform count");
        assertInvalidCommandStream(invalidRuntimeEffectShaderChildCountStream(), "invalid runtime-effect shader child count");
        assertInvalidCommandStream(invalidRuntimeEffectShaderNegativeChildCountStream(), "invalid runtime-effect shader negative child count");
        assertInvalidCommandStream(invalidRuntimeEffectShaderNamedUniformCountStream(), "invalid runtime-effect shader named uniform count");
        assertInvalidCommandStream(invalidRuntimeEffectShaderNegativeNamedUniformCountStream(), "invalid runtime-effect shader negative named uniform count");
        assertInvalidCommandStream(invalidRuntimeEffectShaderNamedChildCountStream(), "invalid runtime-effect shader named child count");
        assertInvalidCommandStream(invalidRuntimeEffectShaderNegativeNamedChildCountStream(), "invalid runtime-effect shader negative named child count");
        assertInvalidCommandStream(invalidRuntimeEffectShaderSourceCodeStream(), "invalid runtime-effect shader source code");
        assertInvalidCommandStream(invalidRuntimeEffectShaderEvictedChildHandleStream(), "evicted runtime-effect shader child handle");
        assertInvalidCommandStream(invalidRuntimeEffectShaderColorFilterChildHandleStream(), "color-filter runtime-effect shader child handle");
        assertInvalidCommandStream(invalidRuntimeEffectUniformSchemaStream(), "invalid runtime-effect uniform schema stream");
        assertInvalidCommandStream(invalidRuntimeEffectChildSchemaStream(), "invalid runtime-effect child schema stream");
        assertInvalidCommandStream(invalidPerlinNoiseShaderKindStream(), "invalid Perlin noise shader kind stream");
        assertInvalidCommandStream(invalidPerlinNoiseShaderFrequencyStream(), "invalid Perlin noise shader frequency stream");
        assertInvalidCommandStream(invalidPerlinNoiseShaderFrequencyYStream(), "invalid Perlin noise shader y-frequency stream");
        assertInvalidCommandStream(invalidPerlinNoiseShaderOctavesStream(), "invalid Perlin noise shader octave stream");
        assertInvalidCommandStream(invalidPerlinNoiseShaderZeroOctavesStream(), "invalid Perlin noise shader zero-octave stream");
        assertInvalidCommandStream(invalidPerlinNoiseShaderTileSizeStream(), "invalid Perlin noise shader tile-size stream");
        assertInvalidCommandStream(invalidPerlinNoiseShaderTileHeightStream(), "invalid Perlin noise shader tile-height stream");
        assertInvalidCommandStream(invalidPerlinNoiseShaderNegativeTileSizeStream(), "invalid Perlin noise shader negative tile-size stream");
        assertInvalidCommandStream(invalidPerlinNoiseShaderNegativeTileHeightStream(), "invalid Perlin noise shader negative tile-height stream");
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
        assertInvalidCommandStream(invalidSaveLayerRecordFlagsStream(), "invalid saveLayer record flags");
        assertInvalidCommandStream(invalidSaveLayerRecordLengthStream(), "invalid saveLayer record length");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_SAVE_LAYER_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, 9999
        }, "invalid saveLayer blend mode");
        assertInvalidCommandStream(invalidSaveLayerBlendModeRecordFlagsStream(), "invalid saveLayer blend-mode record flags");
        assertInvalidCommandStream(invalidSaveLayerBlendModeRecordLengthStream(), "invalid saveLayer blend-mode record length");
        assertInvalidCommandStream(invalidSaveLayerBlendModeWidthStream(), "invalid saveLayer blend-mode width");
        assertInvalidCommandStream(invalidSaveLayerBlendModeHeightStream(), "invalid saveLayer blend-mode height");
        assertInvalidCommandStream(invalidSaveLayerBlendModeAlphaStream(), "invalid saveLayer blend-mode alpha");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_SAVE_LAYER_COLOR_FILTER, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_PLUS
        }, "invalid saveLayer color-filter blend mode");
        assertInvalidCommandStream(invalidSaveLayerColorFilterRecordFlagsStream(), "invalid saveLayer color-filter record flags");
        assertInvalidCommandStream(invalidSaveLayerColorFilterRecordLengthStream(), "invalid saveLayer color-filter record length");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 11,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER, 44, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, JBRSkia.COMMAND_BLEND_MODE_PLUS, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_PLUS
        }, "invalid saveLayer blend/color-filter filter blend mode");
        assertInvalidCommandStream(invalidSaveLayerBlendColorFilterRecordFlagsStream(), "invalid saveLayer blend/color-filter record flags");
        assertInvalidCommandStream(invalidSaveLayerBlendColorFilterRecordLengthStream(), "invalid saveLayer blend/color-filter record length");
        assertInvalidCommandStream(invalidSaveLayerBlendColorFilterWidthStream(), "invalid saveLayer blend/color-filter width");
        assertInvalidCommandStream(invalidSaveLayerBlendColorFilterHeightStream(), "invalid saveLayer blend/color-filter height");
        assertInvalidCommandStream(invalidSaveLayerColorFilterWidthStream(), "invalid saveLayer color-filter width");
        assertInvalidCommandStream(invalidSaveLayerColorFilterHeightStream(), "invalid saveLayer color-filter height");
        assertInvalidCommandStream(invalidSaveLayerColorFilterAlphaStream(), "invalid saveLayer color-filter alpha");
        assertInvalidCommandStream(invalidSaveLayerBlendColorFilterAlphaStream(), "invalid saveLayer blend/color-filter alpha");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_COLOR_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xffff00ff, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN, 3, 4, 10, 20
        }, "undefined color-filter handle");
        assertInvalidCommandStream(invalidFillRectColorFilterImageFilterHandleStream(), "image-filter fill color-filter handle");
        assertInvalidCommandStream(invalidFillRectColorFilterPathEffectHandleStream(), "path-effect fill color-filter handle");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_SAVE_LAYER_COLOR_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, 0x00000001, 0x00000002
        }, "undefined saveLayer color-filter handle");
        assertValidCommandStream(validSaveLayerImageFilterRefStream(), "valid saveLayer image-filter-ref stream");
        assertInvalidCommandStream(invalidSaveLayerColorFilterRefRecordFlagsStream(), "invalid saveLayer color-filter-ref record flags");
        assertInvalidCommandStream(invalidSaveLayerColorFilterRefRecordLengthStream(), "invalid saveLayer color-filter-ref record length");
        assertInvalidCommandStream(invalidSaveLayerColorFilterRefWidthStream(), "invalid saveLayer color-filter-ref width");
        assertInvalidCommandStream(invalidSaveLayerColorFilterRefHeightStream(), "invalid saveLayer color-filter-ref height");
        assertInvalidCommandStream(invalidSaveLayerColorFilterRefAlphaStream(), "invalid saveLayer color-filter-ref alpha");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 11,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF, 44, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, JBRSkia.COMMAND_BLEND_MODE_PLUS, 0x00000001, 0x00000002
        }, "undefined saveLayer blend/color-filter handle");
        assertInvalidCommandStream(invalidSaveLayerBlendColorFilterRefRecordFlagsStream(), "invalid saveLayer blend/color-filter-ref record flags");
        assertInvalidCommandStream(invalidSaveLayerBlendColorFilterRefRecordLengthStream(), "invalid saveLayer blend/color-filter-ref record length");
        assertInvalidCommandStream(invalidSaveLayerBlendColorFilterRefWidthStream(), "invalid saveLayer blend/color-filter-ref width");
        assertInvalidCommandStream(invalidSaveLayerBlendColorFilterRefHeightStream(), "invalid saveLayer blend/color-filter-ref height");
        assertInvalidCommandStream(invalidSaveLayerBlendColorFilterRefAlphaStream(), "invalid saveLayer blend/color-filter-ref alpha");
        assertInvalidCommandStream(invalidSaveLayerBlendColorFilterRefBlendModeStream(), "invalid saveLayer blend/color-filter-ref blend mode");
        assertInvalidCommandStream(invalidSaveLayerImageFilterRefRecordFlagsStream(), "invalid saveLayer image-filter-ref record flags");
        assertInvalidCommandStream(invalidSaveLayerImageFilterRefRecordLengthStream(), "invalid saveLayer image-filter-ref record length");
        assertInvalidCommandStream(invalidSaveLayerImageFilterRefWidthStream(), "invalid saveLayer image-filter-ref width");
        assertInvalidCommandStream(invalidSaveLayerImageFilterRefHeightStream(), "invalid saveLayer image-filter-ref height");
        assertInvalidCommandStream(invalidSaveLayerImageFilterRefAlphaStream(), "invalid saveLayer image-filter-ref alpha");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 31,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_IMAGE_ARGB, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 1, 1, 1, 0xffffffff,
                JBRSkia.COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF, 76, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0, 0, 1000, 1000, 10, 20, 30, 40, 1, 2, 1, 1, 600, 1,
                0x00000001, 0x00000002
        }, "undefined image-ref color-filter handle");
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
        assertInvalidCommandStream(invalidTintEffectDescriptorRecordFlagsStream(), "effect descriptor record flags");
        assertInvalidCommandStream(invalidLightingFilterDescriptorPayloadCountStream(), "lighting descriptor payload count");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_PLUS
        }, "unsupported effect descriptor blend mode");
        assertInvalidCommandStream(invalidColorMatrixFilterDescriptorStream(), "color-matrix descriptor rejects nonfinite values");
        assertInvalidCommandStream(invalidBlurImageFilterDescriptorSigmaStream(), "blur image-filter descriptor sigma");
        assertInvalidCommandStream(invalidBlurImageFilterDescriptorNegativeSigmaStream(), "blur image-filter descriptor negative sigma");
        assertInvalidCommandStream(invalidBlurImageFilterDescriptorTileModeStream(), "blur image-filter descriptor tile mode");
        assertInvalidCommandStream(invalidOffsetImageFilterDescriptorDeltaStream(), "offset image-filter descriptor delta");
        assertInvalidCommandStream(invalidCornerPathEffectDescriptorRadiusStream(), "corner path-effect descriptor radius");
        assertInvalidCommandStream(invalidCornerPathEffectDescriptorNegativeRadiusStream(), "corner path-effect descriptor negative radius");
        assertInvalidCommandStream(invalidStampedPathEffectDescriptorAdvanceStream(), "stamped path-effect descriptor advance");
        assertInvalidCommandStream(invalidStampedPathEffectDescriptorZeroAdvanceStream(), "stamped path-effect descriptor zero advance");
        assertInvalidCommandStream(invalidStampedPathEffectDescriptorPhaseStream(), "stamped path-effect descriptor phase");
        assertInvalidCommandStream(invalidStampedPathEffectDescriptorNegativePhaseStream(), "stamped path-effect descriptor negative phase");
        assertInvalidCommandStream(invalidStampedPathEffectDescriptorStyleStream(), "stamped path-effect descriptor style");
        assertInvalidCommandStream(invalidStampedPathEffectDescriptorFillTypeStream(), "stamped path-effect descriptor fill type");
        assertInvalidCommandStream(invalidStampedPathEffectDescriptorPathDataLengthStream(), "stamped path-effect descriptor path-data length");
        assertInvalidCommandStream(invalidStampedPathEffectDescriptorNegativePathDataLengthStream(), "stamped path-effect descriptor negative path-data length");
        assertInvalidCommandStream(invalidStampedPathEffectDescriptorPathVerbStream(), "stamped path-effect descriptor path verb");
        assertInvalidCommandStream(invalidChainPathEffectDescriptorPayloadCountStream(), "chain path-effect descriptor payload count");
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
        assertInvalidCommandStream(invalidRuntimeColorFilterMissingChildHandleStream(), "undefined runtime color-filter child handle");
        assertInvalidCommandStream(invalidRuntimeColorFilterEvictedChildHandleStream(), "evicted runtime color-filter child handle");
        assertInvalidCommandStream(invalidRuntimeColorFilterImageFilterChildStream(), "image-filter runtime color-filter child handle");
        assertInvalidCommandStream(invalidRuntimeColorFilterPathEffectChildStream(), "path-effect runtime color-filter child handle");
        assertInvalidCommandStream(invalidRuntimeColorFilterSourceHashStream(), "invalid runtime color-filter source hash");
        assertInvalidCommandStream(invalidRuntimeColorFilterSourceCodeStream(), "invalid runtime color-filter source code");
        assertInvalidCommandStream(invalidRuntimeColorFilterSkslLengthStream(), "invalid runtime color-filter SKSL length");
        assertInvalidCommandStream(invalidRuntimeColorFilterUniformFloatCountStream(), "invalid runtime color-filter uniform count");
        assertInvalidCommandStream(invalidRuntimeColorFilterNegativeUniformFloatCountStream(), "invalid runtime color-filter negative uniform count");
        assertInvalidCommandStream(invalidRuntimeColorFilterChildCountStream(), "invalid runtime color-filter child count");
        assertInvalidCommandStream(invalidRuntimeColorFilterNegativeChildCountStream(), "invalid runtime color-filter negative child count");
        assertInvalidCommandStream(invalidRuntimeColorFilterNamedUniformCountStream(), "invalid runtime color-filter named uniform count");
        assertInvalidCommandStream(invalidRuntimeColorFilterNegativeNamedUniformCountStream(), "invalid runtime color-filter negative named uniform count");
        assertInvalidCommandStream(invalidRuntimeColorFilterNamedChildCountStream(), "invalid runtime color-filter named child count");
        assertInvalidCommandStream(invalidRuntimeColorFilterNegativeNamedChildCountStream(), "invalid runtime color-filter negative named child count");
        assertInvalidCommandStream(invalidBlurImageFilterMissingChildHandleStream(), "undefined blur image-filter child handle");
        assertInvalidCommandStream(invalidBlurImageFilterEvictedChildHandleStream(), "evicted blur image-filter child handle");
        assertInvalidCommandStream(invalidBlurImageFilterColorFilterChildHandleStream(), "color-filter blur image-filter child handle");
        assertInvalidCommandStream(invalidOffsetImageFilterMissingChildHandleStream(), "undefined offset image-filter child handle");
        assertInvalidCommandStream(invalidOffsetImageFilterEvictedChildHandleStream(), "evicted offset image-filter child handle");
        assertInvalidCommandStream(invalidOffsetImageFilterColorFilterChildHandleStream(), "color-filter offset image-filter child handle");
        assertInvalidCommandStream(invalidChainPathEffectMissingChildHandleStream(), "undefined chained path-effect child handle");
        assertInvalidCommandStream(invalidChainPathEffectEvictedChildHandleStream(), "evicted chained path-effect child handle");
        assertInvalidCommandStream(invalidChainPathEffectColorFilterChildHandleStream(), "color-filter chained path-effect child handle");
        assertInvalidCommandStream(invalidDrawPathPathEffectMissingHandleStream(), "undefined draw-path path-effect handle");
        assertInvalidCommandStream(invalidDrawPathPathEffectEvictedHandleStream(), "evicted draw-path path-effect handle");
        assertInvalidCommandStream(invalidDrawPathPathEffectColorFilterHandleStream(), "color-filter draw-path path-effect handle");
        assertInvalidCommandStream(invalidSaveLayerImageFilterMissingHandleStream(), "undefined saveLayer image-filter handle");
        assertInvalidCommandStream(invalidSaveLayerImageFilterColorFilterHandleStream(), "color-filter saveLayer image-filter handle");
        assertInvalidCommandStream(invalidSaveLayerColorFilterPathEffectHandleStream(), "path-effect saveLayer color-filter handle");
        assertInvalidCommandStream(invalidSaveLayerBlendColorFilterPathEffectHandleStream(), "path-effect saveLayer blend/color-filter handle");
        assertInvalidCommandStream(invalidSaveLayerColorFilterEvictedHandleStream(), "evicted saveLayer color-filter handle");
        assertInvalidCommandStream(invalidSaveLayerBlendColorFilterEvictedHandleStream(), "evicted saveLayer blend/color-filter handle");
        assertInvalidCommandStream(invalidSaveLayerImageFilterEvictedHandleStream(), "evicted saveLayer image-filter handle");
        assertInvalidCommandStream(invalidColorFilterEvictRecordFlagsStream(), "color-filter evict record flags");
        assertInvalidCommandStream(new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 20,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_IMAGE_ARGB, 80, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0, 0, 2000, 2000, 10000, 20000, 30000, 40000, 2, 2, 1001, 1, 4,
                0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffffff
        }, "invalid image alpha");
        assertInvalidCommandStream(textStreamWith(11, 0), "invalid text font size");
        assertInvalidCommandStream(textStreamWith(13, 0), "invalid text font weight");
        assertInvalidCommandStream(textStreamWith(14, 0), "invalid text font width");
        assertInvalidCommandStream(textStreamWith(15, 3), "invalid text font slant");
        assertInvalidCommandStream(textStreamWith(16, 257), "invalid text font family length");
        assertInvalidCommandStream(paragraphStreamWith(12, 0), "invalid paragraph font size");
        assertInvalidCommandStream(paragraphStreamWith(14, 0), "invalid paragraph font weight");
        assertInvalidCommandStream(paragraphStreamWith(15, 0), "invalid paragraph font width");
        assertInvalidCommandStream(paragraphStreamWith(16, 3), "invalid paragraph font slant");
        assertInvalidCommandStream(paragraphStreamWith(17, 257), "invalid paragraph font family length");
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

    private static int[] validSaveLayerBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 12,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_SAVE_LAYER_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, JBRSkia.COMMAND_BLEND_MODE_PLUS,
                JBRSkia.COMMAND_RESTORE, 12, JBRSkia.COMMAND_RECORD_FLAGS_NONE
        };
    }

    private static int[] validSaveLayerBlendColorFilterStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 14,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER, 44, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, JBRSkia.COMMAND_BLEND_MODE_PLUS, 0xff00ffff,
                JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_RESTORE, 12, JBRSkia.COMMAND_RECORD_FLAGS_NONE
        };
    }

    private static int[] invalidSaveLayerColorFilterWidthStream() {
        int[] commands = validSaveLayerTintColorFilterStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 5] = -1;
        return commands;
    }

    private static int[] invalidSaveLayerColorFilterRecordFlagsStream() {
        int[] commands = validSaveLayerTintColorFilterStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 2] = JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS;
        return commands;
    }

    private static int[] invalidSaveLayerColorFilterRecordLengthStream() {
        int[] commands = validSaveLayerTintColorFilterStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 1] = 36;
        return commands;
    }

    private static int[] invalidSaveLayerColorFilterHeightStream() {
        int[] commands = validSaveLayerTintColorFilterStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 6] = -1;
        return commands;
    }

    private static int[] invalidSaveLayerColorFilterAlphaStream() {
        int[] commands = validSaveLayerTintColorFilterStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 7] = 1001;
        return commands;
    }

    private static int[] invalidSaveLayerRecordFlagsStream() {
        int[] commands = validSaveLayerStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 2] = JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS;
        return commands;
    }

    private static int[] invalidSaveLayerRecordLengthStream() {
        int[] commands = validSaveLayerStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 1] = 28;
        return commands;
    }

    private static int[] invalidSaveLayerBlendModeRecordFlagsStream() {
        int[] commands = validSaveLayerBlendModeStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 2] = JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS;
        return commands;
    }

    private static int[] invalidSaveLayerBlendModeRecordLengthStream() {
        int[] commands = validSaveLayerBlendModeStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 1] = 32;
        return commands;
    }

    private static int[] invalidSaveLayerBlendModeWidthStream() {
        int[] commands = validSaveLayerBlendModeStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 5] = -1;
        return commands;
    }

    private static int[] invalidSaveLayerBlendModeHeightStream() {
        int[] commands = validSaveLayerBlendModeStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 6] = -1;
        return commands;
    }

    private static int[] invalidSaveLayerBlendModeAlphaStream() {
        int[] commands = validSaveLayerBlendModeStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 7] = 1001;
        return commands;
    }

    private static int[] invalidSaveLayerBlendColorFilterRecordFlagsStream() {
        int[] commands = validSaveLayerBlendColorFilterStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 2] = JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS;
        return commands;
    }

    private static int[] invalidSaveLayerBlendColorFilterRecordLengthStream() {
        int[] commands = validSaveLayerBlendColorFilterStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 1] = 40;
        return commands;
    }

    private static int[] invalidSaveLayerBlendColorFilterWidthStream() {
        int[] commands = validSaveLayerBlendColorFilterStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 5] = -1;
        return commands;
    }

    private static int[] invalidSaveLayerBlendColorFilterHeightStream() {
        int[] commands = validSaveLayerBlendColorFilterStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 6] = -1;
        return commands;
    }

    private static int[] invalidSaveLayerBlendColorFilterAlphaStream() {
        int[] commands = validSaveLayerBlendColorFilterStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 7] = 1001;
        return commands;
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

    private static int[] invalidDrawImageRefMissingImageStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 17,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_IMAGE_REF, 68, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0, 0, 2000, 2000, 10000, 20000, 30000, 40000,
                0x12345678, 0x0abcdef0, 2, 2, 600, 1
        };
    }

    private static int[] invalidDrawImageRefEvictedImageStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 34,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_IMAGE_ARGB, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x12345678, 0x0abcdef0, 2, 2, 4,
                0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffffff,
                JBRSkia.COMMAND_EVICT_IMAGE_CACHE_KEY, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x12345678, 0x0abcdef0,
                JBRSkia.COMMAND_DRAW_IMAGE_REF, 68, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0, 0, 2000, 2000, 10000, 20000, 30000, 40000,
                0x12345678, 0x0abcdef0, 2, 2, 600, 1
        };
    }

    private static int[] invalidDrawImageRefClearedImageStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 32,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_IMAGE_ARGB, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x12345678, 0x0abcdef0, 2, 2, 4,
                0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffffff,
                JBRSkia.COMMAND_CLEAR_IMAGE_CACHE, 12, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                JBRSkia.COMMAND_DRAW_IMAGE_REF, 68, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0, 0, 2000, 2000, 10000, 20000, 30000, 40000,
                0x12345678, 0x0abcdef0, 2, 2, 600, 1
        };
    }

    private static int[] invalidDrawImageRefDimensionMismatchStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 29,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_IMAGE_ARGB, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x12345678, 0x0abcdef0, 2, 2, 4,
                0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffffff,
                JBRSkia.COMMAND_DRAW_IMAGE_REF, 68, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0, 0, 2000, 2000, 10000, 20000, 30000, 40000,
                0x12345678, 0x0abcdef0, 2, 1, 600, 1
        };
    }

    private static int[] invalidFillRectImageShaderMissingImageStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 14,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_IMAGE_SHADER, 56, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0, 0, 10000, 10000, 0x12345678, 0x0abcdef0, 2, 2, 1, 1, 1000
        };
    }

    private static int[] validTextStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 19,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_TEXT_UTF16, 76, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                1250, 18500, 13000, 0xff000000, 700, 5, 1, 5, 'I', 'n', 't', 'e', 'r', 2, 'H', 'i'
        };
    }

    private static int[] validFontDataStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_FONT_DATA, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x12345678, 0x0abcdef0, 4, 0, 1, 254, 255
        };
    }

    private static int[] validLatin1TextStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 16,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_TEXT_UTF16, 64, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                1250, 18500, 13000, 0xff000000, 400, 5, 0, 0, 4, 'C', 'a', 'f', '\u00e9'
        };
    }

    private static int[] textStreamWith(int index, int value) {
        int[] commands = validTextStream();
        commands[index] = value;
        return commands;
    }

    private static int[] validParagraphTextStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 32,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_PARAGRAPH_UTF16, 128, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                1250, 2500, 120000, 13000, 0xff000000, 700, 5, 1,
                5, 'I', 'n', 't', 'e', 'r',
                2, 1, 1500, 1, 1, 3, 2500, 1, 0xffff0000, 5,
                'H', 'i', ' ', 0xd83d, 0xde80
        };
    }

    private static int[] paragraphStreamWith(int index, int value) {
        int[] commands = validParagraphTextStream();
        commands[index] = value;
        return commands;
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

    private static int[] validDrawPointsStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 13,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_POINTS, 52, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xffffffff, 4, 1, 2, 4500, 2, 1, 3, 12, 12
        };
    }

    private static int[] validDrawVerticesStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 26,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_VERTICES, 104, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0, JBRSkia.COMMAND_BLEND_MODE_SRC_OVER, 0xff000000, 3, 3,
                1, 2, 11, 3, 4, 12,
                0, 0, 1065353216, 0, 0, 1065353216,
                0xffff0000, 0xff00ff00, 0xff0000ff,
                0, 1, 2
        };
    }

    private static int[] invalidDrawPointsPointCountStream() {
        int[] commands = validDrawPointsStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 8] = 0;
        return commands;
    }

    private static int[] invalidDrawPointsMaxPointCountStream() {
        int[] commands = validDrawPointsStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 8] = 4097;
        return commands;
    }

    private static int[] invalidDrawPointsRecordLengthStream() {
        int[] commands = validDrawPointsStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 1] -= Integer.BYTES;
        return commands;
    }

    private static int[] invalidDrawVerticesVertexCountStream() {
        int[] commands = validDrawVerticesStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 6] = 2;
        return commands;
    }

    private static int[] invalidDrawVerticesMaxVertexCountStream() {
        int[] commands = validDrawVerticesStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 6] = 4097;
        return commands;
    }

    private static int[] invalidDrawVerticesRecordLengthStream() {
        int[] commands = validDrawVerticesStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 1] -= Integer.BYTES;
        return commands;
    }

    private static int[] invalidDrawVerticesVertexModeStream() {
        int[] commands = validDrawVerticesStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 3] = 3;
        return commands;
    }

    private static int[] invalidDrawVerticesBlendModeStream() {
        int[] commands = validDrawVerticesStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 4] = 9999;
        return commands;
    }

    private static int[] invalidDrawVerticesIndexCountStream() {
        int[] commands = validDrawVerticesStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 7] = -1;
        return commands;
    }

    private static int[] invalidDrawVerticesMaxIndexCountStream() {
        int[] commands = validDrawVerticesStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 7] = 8193;
        return commands;
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

    private static int[] validLinearGradientPathStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 18,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_PATH_LINEAR_GRADIENT, 72, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                JBRSkia.COMMAND_PATH_FILL_NON_ZERO, 3, JBRSkia.COMMAND_PATH_VERB_MOVE, 1000, 2000,
                1000, 2000, 11000, 12000, 0, 2,
                0xffff0000, 250,
                0xff0000ff, 750
        };
    }

    private static int[] invalidLinearGradientPathTileModeStream() {
        int[] commands = validLinearGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = 4;
        return commands;
    }

    private static int[] invalidLinearGradientPathColorCountStream() {
        int[] commands = validLinearGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 13] = 1;
        return commands;
    }

    private static int[] invalidLinearGradientPathStopOrderStream() {
        int[] commands = validLinearGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 15] = 750;
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 17] = 250;
        return commands;
    }

    private static int[] invalidLinearGradientPathFillTypeStream() {
        int[] commands = validLinearGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 3] = -1;
        return commands;
    }

    private static int[] invalidLinearGradientPathDataLengthStream() {
        int[] commands = validLinearGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 4] = 4097;
        return commands;
    }

    private static int[] invalidLinearGradientPathVerbStream() {
        int[] commands = validLinearGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 5] = 99;
        return commands;
    }

    private static int[] validRadialGradientPathStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 17,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_PATH_RADIAL_GRADIENT, 68, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                JBRSkia.COMMAND_PATH_FILL_NON_ZERO, 3, JBRSkia.COMMAND_PATH_VERB_MOVE, 1000, 2000,
                6000, 7000, 8000, 0, 2,
                0xff00ff00, 200,
                0xffffffff, 800
        };
    }

    private static int[] invalidRadialGradientPathRadiusStream() {
        int[] commands = validRadialGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10] = 0;
        return commands;
    }

    private static int[] invalidRadialGradientPathTileModeStream() {
        int[] commands = validRadialGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 11] = 4;
        return commands;
    }

    private static int[] invalidRadialGradientPathColorCountStream() {
        int[] commands = validRadialGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = 1;
        return commands;
    }

    private static int[] invalidRadialGradientPathStopOrderStream() {
        int[] commands = validRadialGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 14] = 800;
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 16] = 200;
        return commands;
    }

    private static int[] invalidRadialGradientPathFillTypeStream() {
        int[] commands = validRadialGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 3] = -1;
        return commands;
    }

    private static int[] invalidRadialGradientPathDataLengthStream() {
        int[] commands = validRadialGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 4] = 4097;
        return commands;
    }

    private static int[] invalidRadialGradientPathVerbStream() {
        int[] commands = validRadialGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 5] = 99;
        return commands;
    }

    private static int[] validSweepGradientPathStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 15,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_PATH_SWEEP_GRADIENT, 60, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                JBRSkia.COMMAND_PATH_FILL_NON_ZERO, 3, JBRSkia.COMMAND_PATH_VERB_MOVE, 1000, 2000,
                5000, 6000, 2,
                0xff00ff00, 200,
                0xffffffff, 800
        };
    }

    private static int[] invalidSweepGradientPathColorCountStream() {
        int[] commands = validSweepGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10] = 1;
        return commands;
    }

    private static int[] invalidSweepGradientPathStopOrderStream() {
        int[] commands = validSweepGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = 800;
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 14] = 200;
        return commands;
    }

    private static int[] invalidSweepGradientPathFillTypeStream() {
        int[] commands = validSweepGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 3] = -1;
        return commands;
    }

    private static int[] invalidSweepGradientPathDataLengthStream() {
        int[] commands = validSweepGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 4] = 4097;
        return commands;
    }

    private static int[] invalidSweepGradientPathVerbStream() {
        int[] commands = validSweepGradientPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 5] = 99;
        return commands;
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

    private static int[] validFillRectLightenBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_LIGHTEN, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectDifferenceBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_DIFFERENCE, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectExclusionBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_EXCLUSION, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectColorDodgeBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_COLOR_DODGE, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectColorBurnBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_COLOR_BURN, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectHardlightBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_HARDLIGHT, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectSoftlightBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_SOFTLIGHT, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectHueBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_HUE, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectSaturationBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_SATURATION, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectColorBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_COLOR, 1, 2, 10, 20
        };
    }

    private static int[] validFillRectLuminosityBlendModeStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_BLEND_MODE, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x40ff0000, JBRSkia.COMMAND_BLEND_MODE_LUMINOSITY, 1, 2, 10, 20
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

    private static int[] invalidFillRectBlendModeWidthStream() {
        int[] commands = validFillRectPlusBlendModeStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 7] = -1;
        return commands;
    }

    private static int[] invalidFillRectBlendModeHeightStream() {
        int[] commands = validFillRectPlusBlendModeStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 8] = -1;
        return commands;
    }

    private static int[] invalidFillRectColorFilterWidthStream() {
        int[] commands = validFillRectTintColorFilterStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 8] = -1;
        return commands;
    }

    private static int[] invalidFillRectColorFilterHeightStream() {
        int[] commands = validFillRectTintColorFilterStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 9] = -1;
        return commands;
    }

    private static int[] invalidFillRectColorFilterRefWidthStream() {
        int[] commands = validFillRectTintColorFilterHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10 + 8] = -1;
        return commands;
    }

    private static int[] invalidFillRectColorFilterRefHeightStream() {
        int[] commands = validFillRectTintColorFilterHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10 + 9] = -1;
        return commands;
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

    private static int[] validFillRectColorMatrixFilterHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 38,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 112, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000001, 0x00000002,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                20,
                f(1f), f(0f), f(0f), f(0f), f(0.125f),
                f(0f), f(1f), f(0f), f(0f), f(0f),
                f(0f), f(0f), f(1f), f(0f), f(0f),
                f(0f), f(0f), f(0f), f(1f), f(0f),
                JBRSkia.COMMAND_FILL_RECT_COLOR_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xffff00ff, 0x00000001, 0x00000002, 3, 4, 10, 20
        };
    }

    private static int[] validTintEffectDescriptorRecordOnlyStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000001, 0x00000002,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN
        };
    }

    private static int[] invalidTintEffectDescriptorRecordFlagsStream() {
        int[] commands = validTintEffectDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 2] = JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS;
        return commands;
    }

    private static int[] validLightingFilterDescriptorRecordOnlyStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000001, 0x00000002,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_LIGHTING_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xffffffff, 0xff000000
        };
    }

    private static int[] invalidLightingFilterDescriptorPayloadCountStream() {
        int[] commands = validLightingFilterDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 7] = 1;
        return commands;
    }

    private static int[] invalidColorMatrixFilterDescriptorStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 28,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 112, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000001, 0x00000002,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                20,
                f(1f), f(0f), f(0f), f(0f), Float.floatToRawIntBits(Float.NaN),
                f(0f), f(1f), f(0f), f(0f), f(0f),
                f(0f), f(0f), f(1f), f(0f), f(0f),
                f(0f), f(0f), f(0f), f(1f), f(0f)
        };
    }

    private static int[] validBlurImageFilterDescriptorRecordOnlyStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 11,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 44, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                3,
                f(1f), f(1f), 0
        };
    }

    private static int[] invalidBlurImageFilterDescriptorSigmaStream() {
        int[] commands = validBlurImageFilterDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 8] = f(Float.NaN);
        return commands;
    }

    private static int[] invalidBlurImageFilterDescriptorNegativeSigmaStream() {
        int[] commands = validBlurImageFilterDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 9] = f(-1f);
        return commands;
    }

    private static int[] invalidBlurImageFilterDescriptorTileModeStream() {
        int[] commands = validBlurImageFilterDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10] = 4;
        return commands;
    }

    private static int[] validOffsetImageFilterDescriptorRecordOnlyStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2,
                f(2f), f(3f)
        };
    }

    private static int[] invalidOffsetImageFilterDescriptorDeltaStream() {
        int[] commands = validOffsetImageFilterDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 9] = f(Float.POSITIVE_INFINITY);
        return commands;
    }

    private static int[] invalidCornerPathEffectDescriptorRadiusStream() {
        int[] commands = validCornerPathEffectDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 8] = f(Float.NaN);
        return commands;
    }

    private static int[] invalidCornerPathEffectDescriptorNegativeRadiusStream() {
        int[] commands = validCornerPathEffectDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 8] = f(-1f);
        return commands;
    }

    private static int[] validCornerPathEffectDescriptorRecordOnlyStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                1,
                f(2f)
        };
    }

    private static int[] validStampedPathEffectDescriptorRecordOnlyStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 13,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 52, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_STAMPED_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                5,
                f(2f), f(1f), 0, JBRSkia.COMMAND_PATH_FILL_NON_ZERO, 0
        };
    }

    private static int[] invalidStampedPathEffectDescriptorAdvanceStream() {
        int[] commands = validStampedPathEffectDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 8] = f(Float.NaN);
        return commands;
    }

    private static int[] invalidStampedPathEffectDescriptorZeroAdvanceStream() {
        int[] commands = validStampedPathEffectDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 8] = f(0f);
        return commands;
    }

    private static int[] invalidStampedPathEffectDescriptorPhaseStream() {
        int[] commands = validStampedPathEffectDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 9] = f(Float.NaN);
        return commands;
    }

    private static int[] invalidStampedPathEffectDescriptorNegativePhaseStream() {
        int[] commands = validStampedPathEffectDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 9] = f(-1f);
        return commands;
    }

    private static int[] invalidStampedPathEffectDescriptorStyleStream() {
        int[] commands = validStampedPathEffectDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10] = 3;
        return commands;
    }

    private static int[] invalidStampedPathEffectDescriptorFillTypeStream() {
        int[] commands = validStampedPathEffectDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 11] = 99;
        return commands;
    }

    private static int[] invalidStampedPathEffectDescriptorPathDataLengthStream() {
        int[] commands = validStampedPathEffectDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = 4097;
        return commands;
    }

    private static int[] invalidStampedPathEffectDescriptorNegativePathDataLengthStream() {
        int[] commands = validStampedPathEffectDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = -1;
        return commands;
    }

    private static int[] validStampedPathEffectDescriptorWithPathStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 16,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 64, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_STAMPED_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                8,
                f(2f), f(1f), 0, JBRSkia.COMMAND_PATH_FILL_NON_ZERO, 3,
                JBRSkia.COMMAND_PATH_VERB_MOVE, 1000, 2000
        };
    }

    private static int[] invalidStampedPathEffectDescriptorPathVerbStream() {
        int[] commands = validStampedPathEffectDescriptorWithPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 13] = 99;
        return commands;
    }

    private static int[] invalidChainPathEffectDescriptorPayloadCountStream() {
        int[] commands = invalidChainPathEffectMissingChildHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 7] = 3;
        return commands;
    }

    private static int[] invalidFillRectColorFilterImageFilterHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 21,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 44, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                3,
                f(1f), f(1f), 0,
                JBRSkia.COMMAND_FILL_RECT_COLOR_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xffff00ff, 0x00000031, 0x00000032, 3, 4, 10, 20
        };
    }

    private static int[] invalidFillRectColorFilterPathEffectHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 19,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                1, f(4f),
                JBRSkia.COMMAND_FILL_RECT_COLOR_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xffff00ff, 0x00000031, 0x00000032, 3, 4, 10, 20
        };
    }

    private static int[] validCompositeShaderDescriptorStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 58,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 72, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000001, 0x00000002,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_LINEAR_GRADIENT,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                10,
                1000, 2000, 11000, 12000, 0, 2,
                0xffff0000, 250,
                0xff0000ff, 750,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 68, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000003, 0x00000004,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_RADIAL_GRADIENT,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                9,
                6000, 7000, 8000, 0, 2,
                0xff00ff00, 200,
                0xffffffff, 800,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 52, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000005, 0x00000006,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_COMPOSITE,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                5,
                0x00000001, 0x00000002,
                0x00000003, 0x00000004,
                JBRSkia.COMMAND_BLEND_MODE_SRC_OVER,
                JBRSkia.COMMAND_FILL_RECT_SHADER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x00000005, 0x00000006,
                1000, 2000, 11000, 12000, 1000
        };
    }

    private static int[] validRuntimeEffectShaderDescriptorStream() {
        String sksl = "half4 main(float2 p){return half4(1);}";
        long sourceHash = shaderSourceHash(sksl);
        int payloadIntCount = 7 + sksl.length() + 1;
        int defineRecordLength = 8 + payloadIntCount;
        int commandIntCount = defineRecordLength + 10;
        int[] commands = new int[JBRSkia.COMMAND_STREAM_HEADER_SIZE + commandIntCount];
        int offset = 0;
        commands[offset++] = JBRSkia.COMMAND_STREAM_MAGIC;
        commands[offset++] = JBRSkia.ABI_ID;
        commands[offset++] = JBRSkia.COMMAND_STREAM_FLAGS_NONE;
        commands[offset++] = commandIntCount;
        commands[offset++] = JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER;
        commands[offset++] = JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB;
        commands[offset++] = JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR;
        commands[offset++] = defineRecordLength * Integer.BYTES;
        commands[offset++] = JBRSkia.COMMAND_RECORD_FLAGS_NONE;
        commands[offset++] = 0x00000011;
        commands[offset++] = 0x00000012;
        commands[offset++] = JBRSkia.COMMAND_SHADER_DESCRIPTOR_RUNTIME_EFFECT;
        commands[offset++] = JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1;
        commands[offset++] = payloadIntCount;
        commands[offset++] = sksl.length();
        commands[offset++] = 1;
        commands[offset++] = 0;
        commands[offset++] = 0;
        commands[offset++] = 0;
        commands[offset++] = (int) (sourceHash >> 32);
        commands[offset++] = (int) sourceHash;
        for (int index = 0; index < sksl.length(); index++) {
            commands[offset++] = sksl.charAt(index);
        }
        commands[offset++] = Float.floatToRawIntBits(1f);
        commands[offset++] = JBRSkia.COMMAND_FILL_RECT_SHADER_REF;
        commands[offset++] = 40;
        commands[offset++] = JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS;
        commands[offset++] = 0x00000011;
        commands[offset++] = 0x00000012;
        commands[offset++] = 1000;
        commands[offset++] = 2000;
        commands[offset++] = 11000;
        commands[offset++] = 12000;
        commands[offset++] = 1000;
        return commands;
    }

    private static int[] validLinearGradientShaderDescriptorRecordOnlyStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 18,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 72, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000021, 0x00000022,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_LINEAR_GRADIENT,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                10,
                1000, 2000, 11000, 12000, 0, 2,
                0xffff0000, 250,
                0xff0000ff, 750
        };
    }

    private static int[] invalidUnknownShaderDescriptorTypeStream() {
        int[] commands = validLinearGradientShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 5] = 99;
        return commands;
    }

    private static int[] invalidShaderDescriptorVersionStream() {
        int[] commands = validLinearGradientShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 6] = 2;
        return commands;
    }

    private static int[] invalidShaderDescriptorRecordFlagsStream() {
        int[] commands = validLinearGradientShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 2] = JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS;
        return commands;
    }

    private static int[] invalidShaderDescriptorPayloadCountStream() {
        int[] commands = validLinearGradientShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 7] = 9;
        return commands;
    }

    private static int[] invalidShaderDescriptorRecordLengthStream() {
        int[] commands = validLinearGradientShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 1] = 68;
        return commands;
    }

    private static int[] invalidColorShaderDescriptorPayloadCountStream() {
        int[] commands = validColorShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 7] = 2;
        return commands;
    }

    private static int[] invalidShaderColorFilterDescriptorPayloadCountStream() {
        int[] commands = validShaderColorFilterDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 35] = 3;
        return commands;
    }

    private static int[] invalidCompositeShaderDescriptorBlendModeStream() {
        int[] commands = validCompositeShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 47] = 9999;
        return commands;
    }

    private static int[] invalidLinearGradientShaderTileModeStream() {
        int[] commands = validLinearGradientShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = 4;
        return commands;
    }

    private static int[] invalidLinearGradientShaderStopOrderStream() {
        int[] commands = validLinearGradientShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 15] = 750;
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 17] = 250;
        return commands;
    }

    private static int[] validRadialGradientShaderDescriptorRecordOnlyStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 17,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 68, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000021, 0x00000022,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_RADIAL_GRADIENT,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                9,
                6000, 7000, 8000, 0, 2,
                0xff00ff00, 200,
                0xffffffff, 800
        };
    }

    private static int[] invalidRadialGradientShaderRadiusStream() {
        int[] commands = validRadialGradientShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10] = 0;
        return commands;
    }

    private static int[] invalidRadialGradientShaderTileModeStream() {
        int[] commands = validRadialGradientShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 11] = 4;
        return commands;
    }

    private static int[] invalidRadialGradientShaderStopOrderStream() {
        int[] commands = validRadialGradientShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 14] = 800;
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 16] = 200;
        return commands;
    }

    private static int[] validSweepGradientShaderDescriptorRecordOnlyStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 15,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 60, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000021, 0x00000022,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_SWEEP_GRADIENT,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                7,
                5000, 6000, 2,
                0xff00ff00, 200,
                0xffffffff, 800
        };
    }

    private static int[] invalidSweepGradientShaderColorCountStream() {
        int[] commands = validSweepGradientShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10] = 1;
        return commands;
    }

    private static int[] invalidSweepGradientShaderStopOrderStream() {
        int[] commands = validSweepGradientShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = 800;
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 14] = 200;
        return commands;
    }

    private static int[] validImageShaderDescriptorRecordOnlyStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 14,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 56, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000021, 0x00000022,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_IMAGE,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                6,
                0x00000031, 0x00000032, 64, 48, 0, 1
        };
    }

    private static int[] invalidImageShaderWidthStream() {
        int[] commands = validImageShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10] = 0;
        return commands;
    }

    private static int[] invalidImageShaderMaxWidthStream() {
        int[] commands = validImageShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10] = 4097;
        return commands;
    }

    private static int[] invalidImageShaderHeightStream() {
        int[] commands = validImageShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 11] = 0;
        return commands;
    }

    private static int[] invalidImageShaderMaxHeightStream() {
        int[] commands = validImageShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 11] = 4097;
        return commands;
    }

    private static int[] invalidImageShaderTileModeXStream() {
        int[] commands = validImageShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = 4;
        return commands;
    }

    private static int[] invalidImageShaderTileModeYStream() {
        int[] commands = validImageShaderDescriptorRecordOnlyStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 13] = 4;
        return commands;
    }

    private static int[] invalidUndefinedShaderFillStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_FILL_RECT_SHADER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x00000021, 0x00000022,
                1000, 2000, 11000, 12000, 1000
        };
    }

    private static int[] invalidFillRectShaderColorFilterHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 20,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_FILL_RECT_SHADER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x00000031, 0x00000032,
                1000, 2000, 11000, 12000, 1000
        };
    }

    private static int[] invalidCompositeShaderChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 13,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 52, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000025, 0x00000026,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_COMPOSITE,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                5,
                0x00000021, 0x00000022,
                0x00000023, 0x00000024,
                JBRSkia.COMMAND_BLEND_MODE_SRC_OVER
        };
    }

    private static int[] invalidCompositeShaderEvictedChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 53,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 72, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000001, 0x00000002,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_LINEAR_GRADIENT,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                10,
                1000, 2000, 11000, 12000, 0, 2,
                0xffff0000, 250,
                0xff0000ff, 750,
                JBRSkia.COMMAND_EVICT_SHADER_HANDLE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000001, 0x00000002,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 68, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000003, 0x00000004,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_RADIAL_GRADIENT,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                9,
                6000, 7000, 8000, 0, 2,
                0xff00ff00, 200,
                0xffffffff, 800,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 52, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000005, 0x00000006,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_COMPOSITE,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                5,
                0x00000001, 0x00000002,
                0x00000003, 0x00000004,
                JBRSkia.COMMAND_BLEND_MODE_SRC_OVER
        };
    }

    private static int[] invalidCompositeShaderColorFilterChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 23,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 52, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000025, 0x00000026,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_COMPOSITE,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                5,
                0x00000031, 0x00000032,
                0x00000023, 0x00000024,
                JBRSkia.COMMAND_BLEND_MODE_SRC_OVER
        };
    }

    private static int[] validShaderColorFilterDescriptorStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 50,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 72, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000021, 0x00000022,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_LINEAR_GRADIENT,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                10,
                1000, 2000, 11000, 12000, 0, 2,
                0xffff0000, 250,
                0xff0000ff, 750,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_COLOR_FILTER,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                4,
                0x00000021, 0x00000022,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_FILL_RECT_SHADER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x00000041, 0x00000042,
                1000, 2000, 11000, 12000, 1000
        };
    }

    private static int[] invalidShaderColorFilterMissingShaderHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 22,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_COLOR_FILTER,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                4,
                0x00000021, 0x00000022,
                0x00000031, 0x00000032
        };
    }

    private static int[] invalidShaderColorFilterMissingEffectHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 30,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 72, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000021, 0x00000022,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_LINEAR_GRADIENT,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                10,
                1000, 2000, 11000, 12000, 0, 2,
                0xffff0000, 250,
                0xff0000ff, 750,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_COLOR_FILTER,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                4,
                0x00000021, 0x00000022,
                0x00000031, 0x00000032
        };
    }

    private static int[] invalidShaderColorFilterImageFilterHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 41,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 72, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000021, 0x00000022,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_LINEAR_GRADIENT,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                10,
                1000, 2000, 11000, 12000, 0, 2,
                0xffff0000, 250,
                0xff0000ff, 750,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 44, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                3,
                f(1f), f(1f), 0,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_COLOR_FILTER,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                4,
                0x00000021, 0x00000022,
                0x00000031, 0x00000032
        };
    }

    private static int[] invalidShaderColorFilterEvictedShaderHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 45,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 72, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000021, 0x00000022,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_LINEAR_GRADIENT,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                10,
                1000, 2000, 11000, 12000, 0, 2,
                0xffff0000, 250,
                0xff0000ff, 750,
                JBRSkia.COMMAND_EVICT_SHADER_HANDLE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000021, 0x00000022,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_COLOR_FILTER,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                4,
                0x00000021, 0x00000022,
                0x00000031, 0x00000032
        };
    }

    private static int[] validTransformedShaderDescriptorStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 47,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 72, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000021, 0x00000022,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_LINEAR_GRADIENT,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                10,
                1000, 2000, 11000, 12000, 0, 2,
                0xffff0000, 250,
                0xff0000ff, 750,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 76, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_TRANSFORM,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                11,
                0x00000021, 0x00000022,
                1000, 0, 3000,
                0, 1000, 4000,
                0, 0, 1000,
                JBRSkia.COMMAND_FILL_RECT_SHADER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x00000041, 0x00000042,
                1000, 2000, 11000, 12000, 1000
        };
    }

    private static int[] invalidTransformedShaderEvictedChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 42,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 72, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000021, 0x00000022,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_LINEAR_GRADIENT,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                10,
                1000, 2000, 11000, 12000, 0, 2,
                0xffff0000, 250,
                0xff0000ff, 750,
                JBRSkia.COMMAND_EVICT_SHADER_HANDLE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000021, 0x00000022,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 76, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_TRANSFORM,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                11,
                0x00000021, 0x00000022,
                1000, 0, 3000,
                0, 1000, 4000,
                0, 0, 1000
        };
    }

    private static int[] invalidTransformedShaderMissingChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 19,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 76, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_TRANSFORM,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                11,
                0x00000021, 0x00000022,
                1000, 0, 3000,
                0, 1000, 4000,
                0, 0, 1000
        };
    }

    private static int[] invalidTransformedShaderColorFilterChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 29,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 76, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_TRANSFORM,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                11,
                0x00000031, 0x00000032,
                1000, 0, 3000,
                0, 1000, 4000,
                0, 0, 1000
        };
    }

    private static int[] invalidTransformedShaderPayloadCountStream() {
        int[] commands = validTransformedShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 18 + 7] = 10;
        return commands;
    }

    private static int[] invalidEvictedShaderHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 33,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 72, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000021, 0x00000022,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_LINEAR_GRADIENT,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                10,
                1000, 2000, 11000, 12000, 0, 2,
                0xffff0000, 250,
                0xff0000ff, 750,
                JBRSkia.COMMAND_EVICT_SHADER_HANDLE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000021, 0x00000022,
                JBRSkia.COMMAND_FILL_RECT_SHADER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x00000021, 0x00000022,
                1000, 2000, 11000, 12000, 1000
        };
    }

    private static int[] invalidShaderEvictRecordFlagsStream() {
        int[] commands = invalidEvictedShaderHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 18 + 2] = JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS;
        return commands;
    }

    private static int[] validColorShaderDescriptorStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 19,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_COLOR,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                1,
                0xff3366cc,
                JBRSkia.COMMAND_FILL_RECT_SHADER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x00000031, 0x00000032,
                1000, 2000, 11000, 12000, 1000
        };
    }

    private static int[] validPerlinNoiseShaderDescriptorStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 25,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR, 60, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_PERLIN_NOISE,
                JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1,
                7,
                1, 35000, 55000, 3, 7250, 0, 0,
                JBRSkia.COMMAND_FILL_RECT_SHADER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x00000041, 0x00000042,
                1000, 2000, 11000, 12000, 1000
        };
    }

    private static int[] invalidFillRectShaderRefHorizontalBoundsStream() {
        int[] commands = validColorShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 16] = 999;
        return commands;
    }

    private static int[] invalidFillRectShaderRefVerticalBoundsStream() {
        int[] commands = validColorShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 17] = 1999;
        return commands;
    }

    private static int[] invalidFillRectShaderRefAlphaStream() {
        int[] commands = validColorShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 18] = 1001;
        return commands;
    }

    private static int[] invalidPerlinNoiseShaderKindStream() {
        int[] commands = validPerlinNoiseShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 8] = 2;
        return commands;
    }

    private static int[] invalidPerlinNoiseShaderFrequencyStream() {
        int[] commands = validPerlinNoiseShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 9] = 0;
        return commands;
    }

    private static int[] invalidPerlinNoiseShaderFrequencyYStream() {
        int[] commands = validPerlinNoiseShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10] = 0;
        return commands;
    }

    private static int[] invalidPerlinNoiseShaderOctavesStream() {
        int[] commands = validPerlinNoiseShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 11] = 17;
        return commands;
    }

    private static int[] invalidPerlinNoiseShaderZeroOctavesStream() {
        int[] commands = validPerlinNoiseShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 11] = 0;
        return commands;
    }

    private static int[] invalidPerlinNoiseShaderTileSizeStream() {
        int[] commands = validPerlinNoiseShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 13] = 4097;
        return commands;
    }

    private static int[] invalidPerlinNoiseShaderTileHeightStream() {
        int[] commands = validPerlinNoiseShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 14] = 4097;
        return commands;
    }

    private static int[] invalidPerlinNoiseShaderNegativeTileSizeStream() {
        int[] commands = validPerlinNoiseShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 13] = -1;
        return commands;
    }

    private static int[] invalidPerlinNoiseShaderNegativeTileHeightStream() {
        int[] commands = validPerlinNoiseShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 14] = -1;
        return commands;
    }

    private static int[] invalidRuntimeEffectShaderHashStream() {
        int[] commands = validRuntimeEffectShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 13] ^= 1;
        return commands;
    }

    private static int[] invalidRuntimeEffectShaderSkslLengthStream() {
        int[] commands = validRuntimeEffectShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 8] = 0;
        return commands;
    }

    private static int[] invalidRuntimeEffectShaderUniformFloatCountStream() {
        int[] commands = validRuntimeEffectShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 9] = 257;
        return commands;
    }

    private static int[] invalidRuntimeEffectShaderNegativeUniformFloatCountStream() {
        int[] commands = validRuntimeEffectShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 9] = -1;
        return commands;
    }

    private static int[] invalidRuntimeEffectShaderChildCountStream() {
        int[] commands = validRuntimeEffectShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10] = 9;
        return commands;
    }

    private static int[] invalidRuntimeEffectShaderNegativeChildCountStream() {
        int[] commands = validRuntimeEffectShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10] = -1;
        return commands;
    }

    private static int[] invalidRuntimeEffectShaderNamedUniformCountStream() {
        int[] commands = validRuntimeEffectShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 11] = 17;
        return commands;
    }

    private static int[] invalidRuntimeEffectShaderNegativeNamedUniformCountStream() {
        int[] commands = validRuntimeEffectShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 11] = -1;
        return commands;
    }

    private static int[] invalidRuntimeEffectShaderNamedChildCountStream() {
        int[] commands = validRuntimeEffectShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = 1;
        return commands;
    }

    private static int[] invalidRuntimeEffectShaderNegativeNamedChildCountStream() {
        int[] commands = validRuntimeEffectShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = -1;
        return commands;
    }

    private static int[] invalidRuntimeEffectShaderSourceCodeStream() {
        int[] commands = validRuntimeEffectShaderDescriptorStream();
        int skslStart = JBRSkia.COMMAND_STREAM_HEADER_SIZE + 15;
        int skslLength = commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 8];
        commands[skslStart] = 0;
        long sourceHash = shaderSourceHash(commands, skslStart, skslLength);
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 13] = (int) (sourceHash >> 32);
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 14] = (int) sourceHash;
        return commands;
    }

    private static int[] invalidRuntimeEffectShaderColorFilterChildHandleStream() {
        String sksl = "uniform shader content;half4 main(float2 p){return content.eval(p);}";
        long sourceHash = shaderSourceHash(sksl);
        int payloadIntCount = 7 + 2 + sksl.length();
        int effectRecordLength = 10;
        int defineRecordLength = 8 + payloadIntCount;
        int commandIntCount = effectRecordLength + defineRecordLength;
        int[] commands = new int[JBRSkia.COMMAND_STREAM_HEADER_SIZE + commandIntCount];
        int offset = 0;
        commands[offset++] = JBRSkia.COMMAND_STREAM_MAGIC;
        commands[offset++] = JBRSkia.ABI_ID;
        commands[offset++] = JBRSkia.COMMAND_STREAM_FLAGS_NONE;
        commands[offset++] = commandIntCount;
        commands[offset++] = JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER;
        commands[offset++] = JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB;
        commands[offset++] = JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR;
        commands[offset++] = effectRecordLength * Integer.BYTES;
        commands[offset++] = JBRSkia.COMMAND_RECORD_FLAGS_NONE;
        commands[offset++] = 0x00000031;
        commands[offset++] = 0x00000032;
        commands[offset++] = JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER;
        commands[offset++] = JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1;
        commands[offset++] = 2;
        commands[offset++] = 0xff00ffff;
        commands[offset++] = JBRSkia.COMMAND_BLEND_MODE_SRC_IN;
        commands[offset++] = JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR;
        commands[offset++] = defineRecordLength * Integer.BYTES;
        commands[offset++] = JBRSkia.COMMAND_RECORD_FLAGS_NONE;
        commands[offset++] = 0x00000033;
        commands[offset++] = 0x00000034;
        commands[offset++] = JBRSkia.COMMAND_SHADER_DESCRIPTOR_RUNTIME_EFFECT;
        commands[offset++] = JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1;
        commands[offset++] = payloadIntCount;
        commands[offset++] = sksl.length();
        commands[offset++] = 0;
        commands[offset++] = 1;
        commands[offset++] = 0;
        commands[offset++] = 0;
        commands[offset++] = (int) (sourceHash >> 32);
        commands[offset++] = (int) sourceHash;
        commands[offset++] = 0x00000031;
        commands[offset++] = 0x00000032;
        for (int index = 0; index < sksl.length(); index++) {
            commands[offset++] = sksl.charAt(index);
        }
        return commands;
    }

    private static int[] invalidRuntimeEffectShaderEvictedChildHandleStream() {
        String sksl = "uniform shader content;half4 main(float2 p){return content.eval(p);}";
        long sourceHash = shaderSourceHash(sksl);
        int payloadIntCount = 7 + 2 + sksl.length();
        int childRecordLength = 9;
        int evictRecordLength = 5;
        int defineRecordLength = 8 + payloadIntCount;
        int commandIntCount = childRecordLength + evictRecordLength + defineRecordLength;
        int[] commands = new int[JBRSkia.COMMAND_STREAM_HEADER_SIZE + commandIntCount];
        int offset = 0;
        commands[offset++] = JBRSkia.COMMAND_STREAM_MAGIC;
        commands[offset++] = JBRSkia.ABI_ID;
        commands[offset++] = JBRSkia.COMMAND_STREAM_FLAGS_NONE;
        commands[offset++] = commandIntCount;
        commands[offset++] = JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER;
        commands[offset++] = JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB;
        commands[offset++] = JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR;
        commands[offset++] = childRecordLength * Integer.BYTES;
        commands[offset++] = JBRSkia.COMMAND_RECORD_FLAGS_NONE;
        commands[offset++] = 0x00000031;
        commands[offset++] = 0x00000032;
        commands[offset++] = JBRSkia.COMMAND_SHADER_DESCRIPTOR_COLOR;
        commands[offset++] = JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1;
        commands[offset++] = 1;
        commands[offset++] = 0xff3366cc;
        commands[offset++] = JBRSkia.COMMAND_EVICT_SHADER_HANDLE;
        commands[offset++] = evictRecordLength * Integer.BYTES;
        commands[offset++] = JBRSkia.COMMAND_RECORD_FLAGS_NONE;
        commands[offset++] = 0x00000031;
        commands[offset++] = 0x00000032;
        commands[offset++] = JBRSkia.COMMAND_DEFINE_SHADER_DESCRIPTOR;
        commands[offset++] = defineRecordLength * Integer.BYTES;
        commands[offset++] = JBRSkia.COMMAND_RECORD_FLAGS_NONE;
        commands[offset++] = 0x00000033;
        commands[offset++] = 0x00000034;
        commands[offset++] = JBRSkia.COMMAND_SHADER_DESCRIPTOR_RUNTIME_EFFECT;
        commands[offset++] = JBRSkia.COMMAND_SHADER_DESCRIPTOR_VERSION_1;
        commands[offset++] = payloadIntCount;
        commands[offset++] = sksl.length();
        commands[offset++] = 0;
        commands[offset++] = 1;
        commands[offset++] = 0;
        commands[offset++] = 0;
        commands[offset++] = (int) (sourceHash >> 32);
        commands[offset++] = (int) sourceHash;
        commands[offset++] = 0x00000031;
        commands[offset++] = 0x00000032;
        for (int index = 0; index < sksl.length(); index++) {
            commands[offset++] = sksl.charAt(index);
        }
        return commands;
    }

    private static int[] invalidRuntimeEffectUniformSchemaStream() {
        int[] commands = validRuntimeEffectShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 11] = 1;
        return commands;
    }

    private static int[] invalidRuntimeEffectChildSchemaStream() {
        int[] commands = validRuntimeEffectShaderDescriptorStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = 1;
        return commands;
    }

    private static long shaderSourceHash(String sksl) {
        long hash = -3750763034362895579L;
        for (int index = 0; index < sksl.length(); index++) {
            hash ^= sksl.charAt(index) & 0xffL;
            hash *= 1099511628211L;
        }
        return hash;
    }

    private static long shaderSourceHash(int[] commands, int offset, int length) {
        long hash = -3750763034362895579L;
        for (int index = 0; index < length; index++) {
            hash ^= commands[offset + index] & 0xffL;
            hash *= 1099511628211L;
        }
        return hash;
    }

    private static int[] validFillRectLightingFilterHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 20,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000003, 0x00000004,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_LIGHTING_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xffb0d0ff, 0xff101820,
                JBRSkia.COMMAND_FILL_RECT_COLOR_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xffff00ff, 0x00000003, 0x00000004, 3, 4, 10, 20
        };
    }

    private static int[] validRuntimeColorFilterChildDescriptorStream() {
        return runtimeColorFilterChildDescriptorStream(true, false, false);
    }

    private static int[] invalidRuntimeColorFilterMissingChildHandleStream() {
        return runtimeColorFilterChildDescriptorStream(false, false, false);
    }

    private static int[] invalidRuntimeColorFilterEvictedChildHandleStream() {
        return runtimeColorFilterChildDescriptorStream(true, false, true);
    }

    private static int[] invalidRuntimeColorFilterImageFilterChildStream() {
        return runtimeColorFilterChildDescriptorStream(true, true, false);
    }

    private static int[] invalidRuntimeColorFilterPathEffectChildStream() {
        return runtimeColorFilterChildDescriptorStream(true, false, true, false);
    }

    private static int[] invalidRuntimeColorFilterSourceHashStream() {
        int[] commands = validRuntimeColorFilterChildDescriptorStream();
        commands[runtimeColorFilterPayloadStart(commands) + 5] ^= 1;
        return commands;
    }

    private static int[] invalidRuntimeColorFilterSourceCodeStream() {
        int[] commands = validRuntimeColorFilterChildDescriptorStream();
        int payloadStart = runtimeColorFilterPayloadStart(commands);
        int skslStart = runtimeColorFilterSkslStart(commands);
        int skslLength = commands[payloadStart];
        commands[skslStart] = 0;
        long sourceHash = shaderSourceHash(commands, skslStart, skslLength);
        commands[payloadStart + 5] = (int) (sourceHash >> 32);
        commands[payloadStart + 6] = (int) sourceHash;
        return commands;
    }

    private static int[] invalidRuntimeColorFilterSkslLengthStream() {
        int[] commands = validRuntimeColorFilterChildDescriptorStream();
        commands[runtimeColorFilterPayloadStart(commands)] = 0;
        return commands;
    }

    private static int[] invalidRuntimeColorFilterUniformFloatCountStream() {
        int[] commands = validRuntimeColorFilterChildDescriptorStream();
        commands[runtimeColorFilterPayloadStart(commands) + 1] = 257;
        return commands;
    }

    private static int[] invalidRuntimeColorFilterNegativeUniformFloatCountStream() {
        int[] commands = validRuntimeColorFilterChildDescriptorStream();
        commands[runtimeColorFilterPayloadStart(commands) + 1] = -1;
        return commands;
    }

    private static int[] invalidRuntimeColorFilterChildCountStream() {
        int[] commands = validRuntimeColorFilterChildDescriptorStream();
        commands[runtimeColorFilterPayloadStart(commands) + 2] = 9;
        return commands;
    }

    private static int[] invalidRuntimeColorFilterNegativeChildCountStream() {
        int[] commands = validRuntimeColorFilterChildDescriptorStream();
        commands[runtimeColorFilterPayloadStart(commands) + 2] = -1;
        return commands;
    }

    private static int[] invalidRuntimeColorFilterNamedUniformCountStream() {
        int[] commands = validRuntimeColorFilterChildDescriptorStream();
        commands[runtimeColorFilterPayloadStart(commands) + 3] = 17;
        return commands;
    }

    private static int[] invalidRuntimeColorFilterNegativeNamedUniformCountStream() {
        int[] commands = validRuntimeColorFilterChildDescriptorStream();
        commands[runtimeColorFilterPayloadStart(commands) + 3] = -1;
        return commands;
    }

    private static int[] invalidRuntimeColorFilterNamedChildCountStream() {
        int[] commands = validRuntimeColorFilterChildDescriptorStream();
        commands[runtimeColorFilterPayloadStart(commands) + 4] = 9;
        return commands;
    }

    private static int[] invalidRuntimeColorFilterNegativeNamedChildCountStream() {
        int[] commands = validRuntimeColorFilterChildDescriptorStream();
        commands[runtimeColorFilterPayloadStart(commands) + 4] = -1;
        return commands;
    }

    private static int runtimeColorFilterPayloadStart(int[] commands) {
        int offset = JBRSkia.COMMAND_STREAM_HEADER_SIZE;
        while (offset < commands.length) {
            if (commands[offset] == JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR
                    && commands[offset + 5] == JBRSkia.COMMAND_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER) {
                return offset + 8;
            }
            offset += commands[offset + 1] / Integer.BYTES;
        }
        throw new AssertionError("runtime color-filter descriptor not found");
    }

    private static int runtimeColorFilterSkslStart(int[] commands) {
        int payloadStart = runtimeColorFilterPayloadStart(commands);
        int childCount = commands[payloadStart + 2];
        return payloadStart + 7 + childCount * 2;
    }

    private static int[] runtimeColorFilterChildDescriptorStream(boolean defineChild, boolean imageFilterChild, boolean evictChild) {
        return runtimeColorFilterChildDescriptorStream(defineChild, imageFilterChild, false, evictChild);
    }

    private static int[] runtimeColorFilterChildDescriptorStream(
            boolean defineChild,
            boolean imageFilterChild,
            boolean pathEffectChild,
            boolean evictChild
    ) {
        String sksl = "half4 main(half4 c){return c;}";
        long sourceHash = shaderSourceHash(sksl);
        int payloadIntCount = 7 + 2 + sksl.length();
        int childRecordLength = 10;
        int childImageFilterRecordLength = 11;
        int childPathEffectRecordLength = 9;
        int childDefineRecordLength = childRecordLength;
        int childDescriptorType = JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER;
        if (imageFilterChild) {
            childDefineRecordLength = childImageFilterRecordLength;
            childDescriptorType = JBRSkia.COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER;
        } else if (pathEffectChild) {
            childDefineRecordLength = childPathEffectRecordLength;
            childDescriptorType = JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT;
        }
        int evictRecordLength = 5;
        int defineRecordLength = 8 + payloadIntCount;
        int fillRecordLength = 10;
        int commandIntCount = (defineChild ? childDefineRecordLength : 0)
                + (evictChild ? evictRecordLength : 0)
                + defineRecordLength
                + fillRecordLength;
        int[] commands = new int[JBRSkia.COMMAND_STREAM_HEADER_SIZE + commandIntCount];
        int offset = 0;
        commands[offset++] = JBRSkia.COMMAND_STREAM_MAGIC;
        commands[offset++] = JBRSkia.ABI_ID;
        commands[offset++] = JBRSkia.COMMAND_STREAM_FLAGS_NONE;
        commands[offset++] = commandIntCount;
        commands[offset++] = JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER;
        commands[offset++] = JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB;
        if (defineChild) {
            commands[offset++] = JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR;
            commands[offset++] = childDefineRecordLength * Integer.BYTES;
            commands[offset++] = JBRSkia.COMMAND_RECORD_FLAGS_NONE;
            commands[offset++] = 0x00000031;
            commands[offset++] = 0x00000032;
            commands[offset++] = childDescriptorType;
            commands[offset++] = JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1;
            if (imageFilterChild) {
                commands[offset++] = 3;
                commands[offset++] = f(1f);
                commands[offset++] = f(1f);
                commands[offset++] = 0;
            } else if (pathEffectChild) {
                commands[offset++] = 1;
                commands[offset++] = f(4f);
            } else {
                commands[offset++] = 2;
                commands[offset++] = 0xff00ffff;
                commands[offset++] = JBRSkia.COMMAND_BLEND_MODE_SRC_IN;
            }
        }
        if (evictChild) {
            commands[offset++] = JBRSkia.COMMAND_EVICT_COLOR_FILTER_HANDLE;
            commands[offset++] = evictRecordLength * Integer.BYTES;
            commands[offset++] = JBRSkia.COMMAND_RECORD_FLAGS_NONE;
            commands[offset++] = 0x00000031;
            commands[offset++] = 0x00000032;
        }
        commands[offset++] = JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR;
        commands[offset++] = defineRecordLength * Integer.BYTES;
        commands[offset++] = JBRSkia.COMMAND_RECORD_FLAGS_NONE;
        commands[offset++] = 0x00000033;
        commands[offset++] = 0x00000034;
        commands[offset++] = JBRSkia.COMMAND_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER;
        commands[offset++] = JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1;
        commands[offset++] = payloadIntCount;
        commands[offset++] = sksl.length();
        commands[offset++] = 0;
        commands[offset++] = 1;
        commands[offset++] = 0;
        commands[offset++] = 0;
        commands[offset++] = (int) (sourceHash >> 32);
        commands[offset++] = (int) sourceHash;
        commands[offset++] = 0x00000031;
        commands[offset++] = 0x00000032;
        for (int index = 0; index < sksl.length(); index++) {
            commands[offset++] = sksl.charAt(index);
        }
        commands[offset++] = JBRSkia.COMMAND_FILL_RECT_COLOR_FILTER_REF;
        commands[offset++] = fillRecordLength * Integer.BYTES;
        commands[offset++] = JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS;
        commands[offset++] = 0xffff00ff;
        commands[offset++] = 0x00000033;
        commands[offset++] = 0x00000034;
        commands[offset++] = 3;
        commands[offset++] = 4;
        commands[offset++] = 10;
        commands[offset++] = 20;
        return commands;
    }

    private static int[] invalidOffsetImageFilterMissingChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 12,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                4,
                0x00000043, 0x00000044,
                f(2f), f(3f)
        };
    }

    private static int[] invalidBlurImageFilterMissingChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 13,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 52, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000043, 0x00000044,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                5,
                0x00000041, 0x00000042,
                f(1f), f(1f), 0
        };
    }

    private static int[] invalidBlurImageFilterEvictedChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 29,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 44, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                3,
                f(1f), f(1f), 0,
                JBRSkia.COMMAND_EVICT_COLOR_FILTER_HANDLE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 52, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000043, 0x00000044,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                5,
                0x00000041, 0x00000042,
                f(1f), f(1f), 0
        };
    }

    private static int[] invalidBlurImageFilterColorFilterChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 23,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 52, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000043, 0x00000044,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                5,
                0x00000041, 0x00000042,
                f(1f), f(1f), 0
        };
    }

    private static int[] validOffsetImageFilterWithInputDescriptorStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 23,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 44, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                3,
                f(1f), f(1f), 0,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000043, 0x00000044,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                4,
                0x00000041, 0x00000042,
                f(2f), f(3f)
        };
    }

    private static int[] invalidOffsetImageFilterEvictedChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 28,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 44, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                3,
                f(1f), f(1f), 0,
                JBRSkia.COMMAND_EVICT_COLOR_FILTER_HANDLE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000043, 0x00000044,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                4,
                0x00000041, 0x00000042,
                f(2f), f(3f)
        };
    }

    private static int[] invalidOffsetImageFilterColorFilterChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 22,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000041, 0x00000042,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000043, 0x00000044,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                4,
                0x00000041, 0x00000042,
                f(2f), f(3f)
        };
    }

    private static int[] invalidChainPathEffectMissingChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 12,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000051, 0x00000052,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CHAIN_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                4,
                0x00000053, 0x00000054,
                0x00000055, 0x00000056
        };
    }

    private static int[] invalidChainPathEffectEvictedChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 26,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000051, 0x00000052,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                1, f(2f),
                JBRSkia.COMMAND_EVICT_COLOR_FILTER_HANDLE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000051, 0x00000052,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000055, 0x00000056,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CHAIN_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                4,
                0x00000051, 0x00000052,
                0x00000053, 0x00000054
        };
    }

    private static int[] invalidChainPathEffectColorFilterChildHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 31,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000051, 0x00000052,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000053, 0x00000054,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                1, f(4f),
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000055, 0x00000056,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CHAIN_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                4,
                0x00000051, 0x00000052,
                0x00000053, 0x00000054
        };
    }

    private static int[] validChainedPathEffectDescriptorStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 30,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000051, 0x00000052,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                1, f(2f),
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000053, 0x00000054,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                1, f(4f),
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 48, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000055, 0x00000056,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CHAIN_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                4,
                0x00000051, 0x00000052,
                0x00000053, 0x00000054
        };
    }

    private static int[] invalidDrawPathPathEffectMissingHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 13,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_PATH_PATH_EFFECT_REF, 52, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                JBRSkia.COMMAND_PAINT_STYLE_STROKE, 0xff3366cc, 1000, 0, 0, 4000,
                0x00000051, 0x00000052,
                JBRSkia.COMMAND_PATH_FILL_NON_ZERO, 0
        };
    }

    private static int[] invalidDrawPathPathEffectEvictedHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 27,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000051, 0x00000052,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                1, f(4f),
                JBRSkia.COMMAND_EVICT_COLOR_FILTER_HANDLE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000051, 0x00000052,
                JBRSkia.COMMAND_DRAW_PATH_PATH_EFFECT_REF, 52, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                JBRSkia.COMMAND_PAINT_STYLE_STROKE, 0xff3366cc, 1000, 0, 0, 4000,
                0x00000051, 0x00000052,
                JBRSkia.COMMAND_PATH_FILL_NON_ZERO, 0
        };
    }

    private static int[] invalidDrawPathPathEffectColorFilterHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 23,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000051, 0x00000052,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_DRAW_PATH_PATH_EFFECT_REF, 52, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                JBRSkia.COMMAND_PAINT_STYLE_STROKE, 0xff3366cc, 1000, 0, 0, 4000,
                0x00000051, 0x00000052,
                JBRSkia.COMMAND_PATH_FILL_NON_ZERO, 0
        };
    }

    private static int[] validSaveLayerColorMatrixFilterHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 38,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 112, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000005, 0x00000006,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                20,
                f(1f), f(0f), f(0f), f(0f), f(0.125f),
                f(0f), f(1f), f(0f), f(0f), f(0f),
                f(0f), f(0f), f(1f), f(0f), f(0f),
                f(0f), f(0f), f(0f), f(1f), f(0f),
                JBRSkia.COMMAND_SAVE_LAYER_COLOR_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, 0x00000005, 0x00000006
        };
    }

    private static int[] invalidSaveLayerColorFilterRefRecordFlagsStream() {
        int[] commands = validSaveLayerColorMatrixFilterHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 30] = JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS;
        return commands;
    }

    private static int[] invalidSaveLayerColorFilterRefRecordLengthStream() {
        int[] commands = validSaveLayerColorMatrixFilterHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 29] = 36;
        return commands;
    }

    private static int[] invalidSaveLayerColorFilterRefWidthStream() {
        int[] commands = validSaveLayerColorMatrixFilterHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 33] = -1;
        return commands;
    }

    private static int[] invalidSaveLayerColorFilterRefHeightStream() {
        int[] commands = validSaveLayerColorMatrixFilterHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 34] = -1;
        return commands;
    }

    private static int[] invalidSaveLayerColorFilterRefAlphaStream() {
        int[] commands = validSaveLayerColorMatrixFilterHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 35] = 1001;
        return commands;
    }

    private static int[] validSaveLayerImageFilterRefStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 21,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 44, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000005, 0x00000006,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                3, f(1f), f(1f), 0,
                JBRSkia.COMMAND_SAVE_LAYER_IMAGE_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, 0x00000005, 0x00000006
        };
    }

    private static int[] invalidSaveLayerImageFilterRefRecordFlagsStream() {
        int[] commands = validSaveLayerImageFilterRefStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 13] = JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS;
        return commands;
    }

    private static int[] invalidSaveLayerImageFilterRefRecordLengthStream() {
        int[] commands = validSaveLayerImageFilterRefStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = 36;
        return commands;
    }

    private static int[] invalidSaveLayerImageFilterRefWidthStream() {
        int[] commands = validSaveLayerImageFilterRefStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 16] = -1;
        return commands;
    }

    private static int[] invalidSaveLayerImageFilterRefHeightStream() {
        int[] commands = validSaveLayerImageFilterRefStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 17] = -1;
        return commands;
    }

    private static int[] invalidSaveLayerImageFilterRefAlphaStream() {
        int[] commands = validSaveLayerImageFilterRefStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 18] = 1001;
        return commands;
    }

    private static int[] invalidSaveLayerImageFilterMissingHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 10,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_SAVE_LAYER_IMAGE_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, 0x00000005, 0x00000006
        };
    }

    private static int[] invalidSaveLayerImageFilterColorFilterHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 20,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000005, 0x00000006,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_SAVE_LAYER_IMAGE_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, 0x00000005, 0x00000006
        };
    }

    private static int[] validSaveLayerBlendColorMatrixFilterHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 39,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 112, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000009, 0x0000000a,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                20,
                f(1f), f(0f), f(0f), f(0f), f(0.125f),
                f(0f), f(1f), f(0f), f(0f), f(0f),
                f(0f), f(0f), f(1f), f(0f), f(0f),
                f(0f), f(0f), f(0f), f(1f), f(0f),
                JBRSkia.COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF, 44, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, JBRSkia.COMMAND_BLEND_MODE_PLUS, 0x00000009, 0x0000000a
        };
    }

    private static int[] invalidSaveLayerBlendColorFilterRefRecordFlagsStream() {
        int[] commands = validSaveLayerBlendColorMatrixFilterHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 30] = JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS;
        return commands;
    }

    private static int[] invalidSaveLayerBlendColorFilterRefRecordLengthStream() {
        int[] commands = validSaveLayerBlendColorMatrixFilterHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 29] = 40;
        return commands;
    }

    private static int[] invalidSaveLayerBlendColorFilterRefWidthStream() {
        int[] commands = validSaveLayerBlendColorMatrixFilterHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 33] = -1;
        return commands;
    }

    private static int[] invalidSaveLayerBlendColorFilterRefHeightStream() {
        int[] commands = validSaveLayerBlendColorMatrixFilterHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 34] = -1;
        return commands;
    }

    private static int[] invalidSaveLayerBlendColorFilterRefAlphaStream() {
        int[] commands = validSaveLayerBlendColorMatrixFilterHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 35] = 1001;
        return commands;
    }

    private static int[] invalidSaveLayerBlendColorFilterRefBlendModeStream() {
        int[] commands = validSaveLayerBlendColorMatrixFilterHandleStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 36] = 9999;
        return commands;
    }

    private static int[] invalidSaveLayerColorFilterPathEffectHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 19,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                1, f(4f),
                JBRSkia.COMMAND_SAVE_LAYER_COLOR_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, 0x00000031, 0x00000032
        };
    }

    private static int[] invalidSaveLayerBlendColorFilterPathEffectHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 20,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000031, 0x00000032,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                1, f(4f),
                JBRSkia.COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF, 44, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, JBRSkia.COMMAND_BLEND_MODE_PLUS, 0x00000031, 0x00000032
        };
    }

    private static int f(float value) {
        return Float.floatToRawIntBits(value);
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

    private static int[] invalidColorFilterEvictRecordFlagsStream() {
        int[] commands = validColorFilterHandleEvictStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10 + 2] = JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS;
        return commands;
    }

    private static int[] invalidSaveLayerColorFilterEvictedHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 25,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000005, 0x00000006,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_EVICT_COLOR_FILTER_HANDLE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000005, 0x00000006,
                JBRSkia.COMMAND_SAVE_LAYER_COLOR_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, 0x00000005, 0x00000006
        };
    }

    private static int[] invalidSaveLayerBlendColorFilterEvictedHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 26,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000009, 0x0000000a,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                2, 0xff00ffff, JBRSkia.COMMAND_BLEND_MODE_SRC_IN,
                JBRSkia.COMMAND_EVICT_COLOR_FILTER_HANDLE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000009, 0x0000000a,
                JBRSkia.COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF, 44, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, JBRSkia.COMMAND_BLEND_MODE_PLUS, 0x00000009, 0x0000000a
        };
    }

    private static int[] invalidSaveLayerImageFilterEvictedHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 26,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 44, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x0000000b, 0x0000000c,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                3, f(1f), f(1f), 0,
                JBRSkia.COMMAND_EVICT_COLOR_FILTER_HANDLE, 20, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x0000000b, 0x0000000c,
                JBRSkia.COMMAND_SAVE_LAYER_IMAGE_FILTER_REF, 40, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 10, 10, 600, 0x0000000b, 0x0000000c
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

    private static int[] invalidDashedStrokeLineIntervalCountStream() {
        int[] commands = validDashedStrokeLineStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 13] = 1;
        return commands;
    }

    private static int[] invalidDashedStrokeLinePhaseStream() {
        int[] commands = validDashedStrokeLineStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = -1;
        return commands;
    }

    private static int[] invalidDashedStrokeLineIntervalStream() {
        int[] commands = validDashedStrokeLineStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 14] = 0;
        return commands;
    }

    private static int[] validDashedStrokeRectStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 16,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_STROKE_RECT_DASH_PATH_EFFECT, 64, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xffffffff, 1, 2, 11, 12, 8, 0, 1, 4000, 3000, 2, 16000, 10000
        };
    }

    private static int[] invalidDashedStrokeRectIntervalCountStream() {
        int[] commands = validDashedStrokeRectStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 13] = 1;
        return commands;
    }

    private static int[] invalidDashedStrokeRectWidthStream() {
        int[] commands = validDashedStrokeRectStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 6] = -1;
        return commands;
    }

    private static int[] invalidDashedStrokeRectHeightStream() {
        int[] commands = validDashedStrokeRectStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 7] = -1;
        return commands;
    }

    private static int[] validDashedStrokeRoundRectStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 18,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT, 72, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xffffffff, 1, 2, 11, 12, 3, 4, 8, 0, 1, 4000, 3000, 2, 16000, 10000
        };
    }

    private static int[] invalidDashedStrokeRoundRectIntervalCountStream() {
        int[] commands = validDashedStrokeRoundRectStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 15] = 1;
        return commands;
    }

    private static int[] invalidDashedStrokeRoundRectRightStream() {
        int[] commands = validDashedStrokeRoundRectStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 6] = 0;
        return commands;
    }

    private static int[] invalidDashedStrokeRoundRectBottomStream() {
        int[] commands = validDashedStrokeRoundRectStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 7] = 1;
        return commands;
    }

    private static int[] invalidDashedStrokeRoundRectRadiusXStream() {
        int[] commands = validDashedStrokeRoundRectStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 8] = -1;
        return commands;
    }

    private static int[] invalidDashedStrokeRoundRectRadiusYStream() {
        int[] commands = validDashedStrokeRoundRectStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 9] = -1;
        return commands;
    }

    private static int[] invalidDashedStrokeRoundRectStrokeWidthStream() {
        int[] commands = validDashedStrokeRoundRectStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10] = 0;
        return commands;
    }

    private static int[] invalidDashedStrokeRoundRectStrokeCapStream() {
        int[] commands = validDashedStrokeRoundRectStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 11] = 3;
        return commands;
    }

    private static int[] invalidDashedStrokeRoundRectStrokeJoinStream() {
        int[] commands = validDashedStrokeRoundRectStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = 3;
        return commands;
    }

    private static int[] invalidDashedStrokeRoundRectStrokeMiterStream() {
        int[] commands = validDashedStrokeRoundRectStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 13] = -1;
        return commands;
    }

    private static int[] invalidDashedStrokeRoundRectPhaseStream() {
        int[] commands = validDashedStrokeRoundRectStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 14] = -1;
        return commands;
    }

    private static int[] invalidDashedStrokeRoundRectIntervalStream() {
        int[] commands = validDashedStrokeRoundRectStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 16] = 0;
        return commands;
    }

    private static int[] validDashedStrokePathStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 17,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_STROKE_PATH_DASH_PATH_EFFECT, 68, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0xff3366cc, 8, 0, 1, 4000, 3000, 2, 16000, 10000,
                JBRSkia.COMMAND_PATH_FILL_NON_ZERO, 3, JBRSkia.COMMAND_PATH_VERB_MOVE, 1000, 2000
        };
    }

    private static int[] invalidDashedStrokePathIntervalCountStream() {
        int[] commands = validDashedStrokePathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 9] = 1;
        return commands;
    }

    private static int[] invalidDashedStrokePathStrokeWidthStream() {
        int[] commands = validDashedStrokePathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 4] = 0;
        return commands;
    }

    private static int[] invalidDashedStrokePathStrokeCapStream() {
        int[] commands = validDashedStrokePathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 5] = 3;
        return commands;
    }

    private static int[] invalidDashedStrokePathStrokeJoinStream() {
        int[] commands = validDashedStrokePathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 6] = 3;
        return commands;
    }

    private static int[] invalidDashedStrokePathStrokeMiterStream() {
        int[] commands = validDashedStrokePathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 7] = -1;
        return commands;
    }

    private static int[] invalidDashedStrokePathPhaseStream() {
        int[] commands = validDashedStrokePathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 8] = -1;
        return commands;
    }

    private static int[] invalidDashedStrokePathIntervalStream() {
        int[] commands = validDashedStrokePathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10] = 0;
        return commands;
    }

    private static int[] invalidDashedStrokePathFillTypeStream() {
        int[] commands = validDashedStrokePathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = -1;
        return commands;
    }

    private static int[] invalidDashedStrokePathDataLengthStream() {
        int[] commands = validDashedStrokePathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 13] = 4097;
        return commands;
    }

    private static int[] invalidDashedStrokePathVerbStream() {
        int[] commands = validDashedStrokePathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 14] = 99;
        return commands;
    }

    private static int[] validDrawShadowPathStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 18,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_SHADOW_PATH, 72, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0x33000000, 0x55000000,
                f(0f), f(0f), f(8f), f(10f), f(12f), f(20f), f(30f), 0,
                JBRSkia.COMMAND_PATH_FILL_NON_ZERO, 3, JBRSkia.COMMAND_PATH_VERB_MOVE, 1000, 2000
        };
    }

    private static int[] invalidDrawShadowPathPlaneStream() {
        int[] commands = validDrawShadowPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 5] = f(Float.NaN);
        return commands;
    }

    private static int[] invalidDrawShadowPathRadiusStream() {
        int[] commands = validDrawShadowPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 11] = f(-1f);
        return commands;
    }

    private static int[] invalidDrawShadowPathFlagsStream() {
        int[] commands = validDrawShadowPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 12] = 4;
        return commands;
    }

    private static int[] invalidDrawShadowPathFillTypeStream() {
        int[] commands = validDrawShadowPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 13] = -1;
        return commands;
    }

    private static int[] invalidDrawShadowPathDataLengthStream() {
        int[] commands = validDrawShadowPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 14] = 4097;
        return commands;
    }

    private static int[] invalidDrawShadowPathVerbStream() {
        int[] commands = validDrawShadowPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 15] = 99;
        return commands;
    }

    private static int[] validClipPathStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 9,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_CLIP_PATH, 36, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                JBRSkia.COMMAND_CLIP_OP_INTERSECT, JBRSkia.COMMAND_PATH_FILL_NON_ZERO, 3,
                JBRSkia.COMMAND_PATH_VERB_MOVE, 1000, 2000
        };
    }

    private static int[] invalidClipPathOpStream() {
        int[] commands = validClipPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 3] = -1;
        return commands;
    }

    private static int[] invalidClipPathFillTypeStream() {
        int[] commands = validClipPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 4] = -1;
        return commands;
    }

    private static int[] invalidClipPathDataLengthStream() {
        int[] commands = validClipPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 5] = 4097;
        return commands;
    }

    private static int[] invalidClipPathVerbStream() {
        int[] commands = validClipPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 6] = 99;
        return commands;
    }

    private static int[] validDrawPathStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 14,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DRAW_PATH, 56, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                JBRSkia.COMMAND_PAINT_STYLE_STROKE, 0xff3366cc, 8, 0, 1, 4000,
                JBRSkia.COMMAND_PATH_FILL_NON_ZERO, 3, JBRSkia.COMMAND_PATH_VERB_MOVE, 1000, 2000
        };
    }

    private static int[] invalidDrawPathStyleStream() {
        int[] commands = validDrawPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 3] = -1;
        return commands;
    }

    private static int[] invalidDrawPathStrokeWidthStream() {
        int[] commands = validDrawPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 5] = 0;
        return commands;
    }

    private static int[] invalidDrawPathFillTypeStream() {
        int[] commands = validDrawPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 9] = -1;
        return commands;
    }

    private static int[] invalidDrawPathDataLengthStream() {
        int[] commands = validDrawPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 10] = 4097;
        return commands;
    }

    private static int[] invalidDrawPathVerbStream() {
        int[] commands = validDrawPathStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 11] = 99;
        return commands;
    }

    private static int[] validDrawPathPathEffectRefStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 25,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000051, 0x00000052,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                1, f(4f),
                JBRSkia.COMMAND_DRAW_PATH_PATH_EFFECT_REF, 64, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                JBRSkia.COMMAND_PAINT_STYLE_STROKE, 0xff3366cc, 8, 0, 1, 4000,
                0x00000051, 0x00000052,
                JBRSkia.COMMAND_PATH_FILL_NON_ZERO, 3, JBRSkia.COMMAND_PATH_VERB_MOVE, 1000, 2000
        };
    }

    private static int[] invalidDrawPathPathEffectRefVerbStream() {
        int[] commands = validDrawPathPathEffectRefStream();
        commands[JBRSkia.COMMAND_STREAM_HEADER_SIZE + 22] = 99;
        return commands;
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

    private static int[] validImageRefColorMatrixFilterHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 56,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 112, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000007, 0x00000008,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                20,
                f(1f), f(0f), f(0f), f(0f), f(0.125f),
                f(0f), f(1f), f(0f), f(0f), f(0f),
                f(0f), f(0f), f(1f), f(0f), f(0f),
                f(0f), f(0f), f(0f), f(1f), f(0f),
                JBRSkia.COMMAND_DEFINE_IMAGE_ARGB, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 1, 1, 1, 0xffffffff,
                JBRSkia.COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF, 76, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0, 0, 1000, 1000, 10, 20, 30, 40, 1, 2, 1, 1, 600, 1,
                0x00000007, 0x00000008
        };
    }

    private static int[] invalidImageRefColorFilterPathEffectHandleStream() {
        return new int[] {
                JBRSkia.COMMAND_STREAM_MAGIC, JBRSkia.ABI_ID, JBRSkia.COMMAND_STREAM_FLAGS_NONE, 37,
                JBRSkia.COMMAND_COORDINATE_SPACE_SWING_USER, JBRSkia.COMMAND_PAINT_FORMAT_SOLID_ARGB,
                JBRSkia.COMMAND_DEFINE_EFFECT_DESCRIPTOR, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                0x00000007, 0x00000008,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT,
                JBRSkia.COMMAND_EFFECT_DESCRIPTOR_VERSION_1,
                1, f(4f),
                JBRSkia.COMMAND_DEFINE_IMAGE_ARGB, 36, JBRSkia.COMMAND_RECORD_FLAGS_NONE,
                1, 2, 1, 1, 1, 0xffffffff,
                JBRSkia.COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF, 76, JBRSkia.COMMAND_RECORD_FLAG_ANTIALIAS,
                0, 0, 1000, 1000, 10, 20, 30, 40, 1, 2, 1, 1, 600, 1,
                0x00000007, 0x00000008
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
