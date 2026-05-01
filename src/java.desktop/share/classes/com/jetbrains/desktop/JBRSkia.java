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

package com.jetbrains.desktop;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.nio.ByteBuffer;

/**
 * Experimental JBR-owned Skia interop service.
 *
 * <p>The ABI and build identifiers intentionally use non-constant initializers
 * so downstream compile-only clients cannot accidentally inline stale values.
 */
public abstract class JBRSkia {
    public static final int ABI_ID = Integer.parseInt("84");
    public static final int NATIVE_ABI_VERSION = Integer.parseInt("3");
    public static final String SKIA_REVISION = "m147-" + "64a2414108";
    public static final String SKIA_FLAGS_HASH = "macos-release-metal-poc:" + Integer.parseInt("1");
    public static final String BUILD_ID = buildId();
    public static final int COMMAND_STREAM_MAGIC = Integer.parseInt("1246972723");
    public static final int COMMAND_STREAM_HEADER_SIZE = Integer.parseInt("6");
    public static final int COMMAND_STREAM_FLAGS_NONE = Integer.parseInt("0");
    public static final int COMMAND_COORDINATE_SPACE_SWING_USER = Integer.parseInt("1");
    public static final int COMMAND_PAINT_FORMAT_SOLID_ARGB = Integer.parseInt("1");
    public static final int COMMAND_RECORD_HEADER_SIZE_BYTES = Integer.parseInt("12");
    public static final int COMMAND_RECORD_FLAGS_NONE = Integer.parseInt("0");
    public static final int COMMAND_RECORD_FLAG_ANTIALIAS = Integer.parseInt("1");
    public static final int COMMAND_CAP_CLEAR = Integer.parseInt("1");
    public static final int COMMAND_CAP_FILL_RECT = Integer.parseInt("2");
    public static final int COMMAND_CAP_STROKE_LINE = Integer.parseInt("4");
    public static final int COMMAND_CAP_FILL_OVAL = Integer.parseInt("8");
    public static final int COMMAND_CAP_STROKE_OVAL = Integer.parseInt("16");
    public static final int COMMAND_CAP_CLEAR_RECT = Integer.parseInt("32");
    public static final int COMMAND_CAP_SAVE_RESTORE = Integer.parseInt("64");
    public static final int COMMAND_CAP_CLIP_RECT = Integer.parseInt("128");
    public static final int COMMAND_CAP_USER_SPACE_COORDINATES = Integer.parseInt("256");
    public static final int COMMAND_CAP_RECORD_ANTIALIAS = Integer.parseInt("512");
    public static final int COMMAND_CAP_STROKE_METADATA = Integer.parseInt("1024");
    public static final int COMMAND_CAP_BASIC_TRANSFORMS = Integer.parseInt("2048");
    public static final int COMMAND_CAP_CLIP_RECT_OP = Integer.parseInt("4096");
    public static final int COMMAND_CAP_SAVE_LAYER = Integer.parseInt("8192");
    public static final int COMMAND_CAP_DRAW_IMAGE_ARGB = Integer.parseInt("16384");
    public static final int COMMAND_CAP_IMAGE_CACHE = Integer.parseInt("32768");
    public static final int COMMAND_CAP_DRAW_TEXT_UTF16 = Integer.parseInt("65536");
    public static final int COMMAND_CAP_CLEAR_IMAGE_CACHE = Integer.parseInt("131072");
    public static final int COMMAND_CAP_DRAW_PARAGRAPH_UTF16 = Integer.parseInt("262144");
    public static final int COMMAND_CAP_PARAGRAPH_FONT_STYLE = Integer.parseInt("524288");
    public static final int COMMAND_CAP_PARAGRAPH_LAYOUT = Integer.parseInt("1048576");
    public static final int COMMAND_CAP_PARAGRAPH_LINE_HEIGHT = Integer.parseInt("2097152");
    public static final int COMMAND_CAP_PARAGRAPH_OVERFLOW = Integer.parseInt("4194304");
    public static final int COMMAND_CAP_PARAGRAPH_DECORATION = Integer.parseInt("8388608");
    public static final int COMMAND_CAP_PARAGRAPH_LETTER_SPACING = Integer.parseInt("16777216");
    public static final int COMMAND_CAP_PARAGRAPH_BACKGROUND = Integer.parseInt("33554432");
    public static final int COMMAND_CAP_CLIP_PATH = Integer.parseInt("67108864");
    public static final int COMMAND_CAP_DRAW_PATH = Integer.parseInt("134217728");
    public static final int COMMAND_CAP_DRAW_ARC = Integer.parseInt("268435456");
    public static final int COMMAND_CAP_DRAW_ROUND_RECT = Integer.parseInt("536870912");
    public static final int COMMAND_CAP_FILL_RECT_LINEAR_GRADIENT = Integer.parseInt("1073741824");
    public static final long COMMAND_CAP64_FILL_RECT_LINEAR_GRADIENT = Long.parseLong("1073741824");
    public static final long COMMAND_CAP64_FILL_ROUND_RECT_LINEAR_GRADIENT = Long.parseLong("2147483648");
    public static final long COMMAND_CAP64_FILL_RECT_RADIAL_GRADIENT = Long.parseLong("4294967296");
    public static final long COMMAND_CAP64_FILL_ROUND_RECT_RADIAL_GRADIENT = Long.parseLong("8589934592");
    public static final long COMMAND_CAP64_FILL_PATH_LINEAR_GRADIENT = Long.parseLong("17179869184");
    public static final long COMMAND_CAP64_FILL_PATH_RADIAL_GRADIENT = Long.parseLong("34359738368");
    public static final long COMMAND_CAP64_FILL_RECT_SWEEP_GRADIENT = Long.parseLong("68719476736");
    public static final long COMMAND_CAP64_FILL_ROUND_RECT_SWEEP_GRADIENT = Long.parseLong("137438953472");
    public static final long COMMAND_CAP64_FILL_PATH_SWEEP_GRADIENT = Long.parseLong("274877906944");
    public static final long COMMAND_CAP64_EVICT_IMAGE_CACHE_KEY = Long.parseLong("549755813888");
    public static final long COMMAND_CAP64_TEXT_FONT_FAMILY = Long.parseLong("1099511627776");
    public static final long COMMAND_CAP64_FILL_RECT_IMAGE_SHADER = Long.parseLong("2199023255552");
    public static final long COMMAND_CAP64_STROKE_RECT_LINEAR_GRADIENT = Long.parseLong("4398046511104");
    public static final long COMMAND_CAP64_STROKE_ROUND_RECT_LINEAR_GRADIENT = Long.parseLong("8796093022208");
    public static final long COMMAND_CAP64_STROKE_RECT_RADIAL_GRADIENT = Long.parseLong("17592186044416");
    public static final long COMMAND_CAP64_STROKE_ROUND_RECT_RADIAL_GRADIENT = Long.parseLong("35184372088832");
    public static final long COMMAND_CAP64_STROKE_RECT_SWEEP_GRADIENT = Long.parseLong("70368744177664");
    public static final long COMMAND_CAP64_STROKE_ROUND_RECT_SWEEP_GRADIENT = Long.parseLong("140737488355328");
    public static final long COMMAND_CAP64_FILL_RECT_BLEND_MODE = Long.parseLong("281474976710656");
    public static final long COMMAND_CAP64_FILL_RECT_COLOR_FILTER = Long.parseLong("562949953421312");
    public static final long COMMAND_CAP64_STROKE_LINE_DASH_PATH_EFFECT = Long.parseLong("1125899906842624");
    public static final long COMMAND_CAP64_SAVE_LAYER_COLOR_FILTER = Long.parseLong("2251799813685248");
    public static final long COMMAND_CAP64_DRAW_IMAGE_REF_COLOR_FILTER = Long.parseLong("4503599627370496");
    public static final long COMMAND_CAP64_DEFINE_COLOR_FILTER_TINT = Long.parseLong("9007199254740992");
    public static final long COMMAND_CAP64_FILL_RECT_COLOR_FILTER_REF = Long.parseLong("18014398509481984");
    public static final long COMMAND_CAP64_EVICT_COLOR_FILTER_HANDLE = Long.parseLong("36028797018963968");
    public static final long COMMAND_CAP64_DEFINE_EFFECT_DESCRIPTOR = Long.parseLong("72057594037927936");
    public static final long COMMAND_CAP64_SAVE_LAYER_BLEND_MODE = Long.parseLong("144115188075855872");
    public static final long COMMAND_CAP64_SAVE_LAYER_BLEND_COLOR_FILTER = Long.parseLong("288230376151711744");
    public static final long COMMAND_CAP64_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER = Long.parseLong("576460752303423488");
    public static final long COMMAND_CAP64_EFFECT_DESCRIPTOR_LIGHTING_FILTER = Long.parseLong("1152921504606846976");
    public static final long COMMAND_CAP64_SAVE_LAYER_COLOR_FILTER_REF = Long.parseLong("2305843009213693952");
    public static final long COMMAND_CAP64_DRAW_IMAGE_REF_COLOR_FILTER_REF = Long.parseLong("4611686018427387904");
    public static final long COMMAND_CAP64_SAVE_LAYER_BLEND_COLOR_FILTER_REF = Long.parseLong("-9223372036854775808");
    public static final long COMMAND_CAP64_HIGH_SAVE_LAYER_IMAGE_FILTER_REF = Long.parseLong("1");
    public static final long COMMAND_CAP64_HIGH_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER = Long.parseLong("2");
    public static final long COMMAND_CAP64_HIGH_EFFECT_DESCRIPTOR_CHAIN_IMAGE_FILTER = Long.parseLong("4");
    public static final int COMMAND_CLEAR = Integer.parseInt("1");
    public static final int COMMAND_FILL_RECT = Integer.parseInt("2");
    public static final int COMMAND_STROKE_LINE = Integer.parseInt("3");
    public static final int COMMAND_FILL_OVAL = Integer.parseInt("4");
    public static final int COMMAND_STROKE_OVAL = Integer.parseInt("5");
    public static final int COMMAND_CLEAR_RECT = Integer.parseInt("6");
    public static final int COMMAND_SAVE = Integer.parseInt("7");
    public static final int COMMAND_RESTORE = Integer.parseInt("8");
    public static final int COMMAND_CLIP_RECT = Integer.parseInt("9");
    public static final int COMMAND_CLIP_OP_INTERSECT = Integer.parseInt("0");
    public static final int COMMAND_CLIP_OP_DIFFERENCE = Integer.parseInt("1");
    public static final int COMMAND_TRANSLATE = Integer.parseInt("10");
    public static final int COMMAND_SCALE = Integer.parseInt("11");
    public static final int COMMAND_ROTATE = Integer.parseInt("12");
    public static final int COMMAND_SAVE_LAYER = Integer.parseInt("13");
    public static final int COMMAND_DRAW_IMAGE_ARGB = Integer.parseInt("14");
    public static final int COMMAND_DEFINE_IMAGE_ARGB = Integer.parseInt("15");
    public static final int COMMAND_DRAW_IMAGE_REF = Integer.parseInt("16");
    public static final int COMMAND_DRAW_TEXT_UTF16 = Integer.parseInt("17");
    public static final int COMMAND_CLEAR_IMAGE_CACHE = Integer.parseInt("18");
    public static final int COMMAND_DRAW_PARAGRAPH_UTF16 = Integer.parseInt("19");
    public static final int COMMAND_CLIP_PATH = Integer.parseInt("20");
    public static final int COMMAND_DRAW_PATH = Integer.parseInt("21");
    public static final int COMMAND_DRAW_ARC = Integer.parseInt("22");
    public static final int COMMAND_DRAW_ROUND_RECT = Integer.parseInt("23");
    public static final int COMMAND_FILL_RECT_LINEAR_GRADIENT = Integer.parseInt("24");
    public static final int COMMAND_FILL_ROUND_RECT_LINEAR_GRADIENT = Integer.parseInt("25");
    public static final int COMMAND_FILL_RECT_RADIAL_GRADIENT = Integer.parseInt("26");
    public static final int COMMAND_FILL_ROUND_RECT_RADIAL_GRADIENT = Integer.parseInt("27");
    public static final int COMMAND_FILL_PATH_LINEAR_GRADIENT = Integer.parseInt("28");
    public static final int COMMAND_FILL_PATH_RADIAL_GRADIENT = Integer.parseInt("29");
    public static final int COMMAND_FILL_RECT_SWEEP_GRADIENT = Integer.parseInt("30");
    public static final int COMMAND_FILL_ROUND_RECT_SWEEP_GRADIENT = Integer.parseInt("31");
    public static final int COMMAND_FILL_PATH_SWEEP_GRADIENT = Integer.parseInt("32");
    public static final int COMMAND_EVICT_IMAGE_CACHE_KEY = Integer.parseInt("33");
    public static final int COMMAND_FILL_RECT_IMAGE_SHADER = Integer.parseInt("34");
    public static final int COMMAND_STROKE_RECT_LINEAR_GRADIENT = Integer.parseInt("35");
    public static final int COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT = Integer.parseInt("36");
    public static final int COMMAND_STROKE_RECT_RADIAL_GRADIENT = Integer.parseInt("37");
    public static final int COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT = Integer.parseInt("38");
    public static final int COMMAND_STROKE_RECT_SWEEP_GRADIENT = Integer.parseInt("39");
    public static final int COMMAND_STROKE_ROUND_RECT_SWEEP_GRADIENT = Integer.parseInt("40");
    public static final int COMMAND_FILL_RECT_BLEND_MODE = Integer.parseInt("41");
    public static final int COMMAND_FILL_RECT_COLOR_FILTER = Integer.parseInt("42");
    public static final int COMMAND_STROKE_LINE_DASH_PATH_EFFECT = Integer.parseInt("43");
    public static final int COMMAND_SAVE_LAYER_COLOR_FILTER = Integer.parseInt("44");
    public static final int COMMAND_DRAW_IMAGE_REF_COLOR_FILTER = Integer.parseInt("45");
    public static final int COMMAND_DEFINE_COLOR_FILTER_TINT = Integer.parseInt("46");
    public static final int COMMAND_FILL_RECT_COLOR_FILTER_REF = Integer.parseInt("47");
    public static final int COMMAND_EVICT_COLOR_FILTER_HANDLE = Integer.parseInt("48");
    public static final int COMMAND_DEFINE_EFFECT_DESCRIPTOR = Integer.parseInt("49");
    public static final int COMMAND_SAVE_LAYER_BLEND_MODE = Integer.parseInt("50");
    public static final int COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER = Integer.parseInt("51");
    public static final int COMMAND_SAVE_LAYER_COLOR_FILTER_REF = Integer.parseInt("52");
    public static final int COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF = Integer.parseInt("53");
    public static final int COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF = Integer.parseInt("54");
    public static final int COMMAND_SAVE_LAYER_IMAGE_FILTER_REF = Integer.parseInt("55");
    public static final int COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER = Integer.parseInt("1");
    public static final int COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER = Integer.parseInt("2");
    public static final int COMMAND_EFFECT_DESCRIPTOR_LIGHTING_FILTER = Integer.parseInt("3");
    public static final int COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER = Integer.parseInt("4");
    public static final int COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER = Integer.parseInt("5");
    public static final int COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT = Integer.parseInt("6");
    public static final int COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT = Integer.parseInt("7");
    public static final int COMMAND_EFFECT_DESCRIPTOR_VERSION_1 = Integer.parseInt("1");
    public static final int COMMAND_BLEND_MODE_PLUS = Integer.parseInt("1");
    public static final int COMMAND_BLEND_MODE_SRC_IN = Integer.parseInt("2");
    public static final int COMMAND_BLEND_MODE_MULTIPLY = Integer.parseInt("3");
    public static final int COMMAND_BLEND_MODE_SCREEN = Integer.parseInt("4");
    public static final int COMMAND_BLEND_MODE_OVERLAY = Integer.parseInt("5");
    public static final int COMMAND_BLEND_MODE_DARKEN = Integer.parseInt("6");
    public static final int COMMAND_BLEND_MODE_LIGHTEN = Integer.parseInt("7");
    public static final int COMMAND_BLEND_MODE_DIFFERENCE = Integer.parseInt("8");
    public static final int COMMAND_BLEND_MODE_EXCLUSION = Integer.parseInt("9");
    public static final int COMMAND_BLEND_MODE_COLOR_DODGE = Integer.parseInt("10");
    public static final int COMMAND_BLEND_MODE_COLOR_BURN = Integer.parseInt("11");
    public static final int COMMAND_BLEND_MODE_HARDLIGHT = Integer.parseInt("12");
    public static final int COMMAND_BLEND_MODE_SOFTLIGHT = Integer.parseInt("13");
    public static final int COMMAND_BLEND_MODE_HUE = Integer.parseInt("14");
    public static final int COMMAND_BLEND_MODE_SATURATION = Integer.parseInt("15");
    public static final int COMMAND_BLEND_MODE_COLOR = Integer.parseInt("16");
    public static final int COMMAND_BLEND_MODE_LUMINOSITY = Integer.parseInt("17");
    public static final int COMMAND_PAINT_STYLE_FILL = Integer.parseInt("0");
    public static final int COMMAND_PAINT_STYLE_STROKE = Integer.parseInt("1");
    public static final int COMMAND_PATH_FILL_NON_ZERO = Integer.parseInt("0");
    public static final int COMMAND_PATH_FILL_EVEN_ODD = Integer.parseInt("1");
    public static final int COMMAND_PATH_VERB_MOVE = Integer.parseInt("0");
    public static final int COMMAND_PATH_VERB_LINE = Integer.parseInt("1");
    public static final int COMMAND_PATH_VERB_QUAD = Integer.parseInt("2");
    public static final int COMMAND_PATH_VERB_CUBIC = Integer.parseInt("3");
    public static final int COMMAND_PATH_VERB_CLOSE = Integer.parseInt("4");

    private static String buildId() {
        return "skia=" + SKIA_REVISION
                + ";flags=" + SKIA_FLAGS_HASH
                + ";abi=" + ABI_ID
                + ";native=" + NATIVE_ABI_VERSION;
    }

    public abstract int getCommandCapabilities();

    public abstract long getCommandCapabilities64();

    public abstract long getCommandCapabilities64High();

    public abstract int getNativeAbiVersion();

    public abstract int getNativeCommandStreamAbiId();

    public abstract String getNativeBuildId();

    public abstract ScopedSkiaCanvas acquireCanvas(Graphics2D graphics);

    public abstract static class ScopedSkiaCanvas implements AutoCloseable {
        public static final int BACKEND_METAL = Integer.parseInt("1");

        public abstract long getScopeId();

        public abstract long getSurfaceId();

        public abstract long getContextId();

        public abstract int getBackend();

        public abstract long getCanvasPtr();

        public abstract long getDirectContextPtr();

        public abstract long getMetalTexturePtr();

        public abstract int getPixelFormat();

        public abstract int getColorSpaceId();

        public abstract int getSampleCount();

        public abstract Rectangle getUserSpaceClip();

        public abstract boolean renderDiagnosticFrame(int width, int height, long frameTimeNanos);

        public abstract boolean renderCommandFrame(int width, int height, long frameTimeNanos, int[] commands);

        public abstract boolean renderCommandBufferFrame(int width, int height, long frameTimeNanos, byte[] commands);

        public abstract boolean renderCommandDirectFrame(int width, int height, long frameTimeNanos, ByteBuffer commands);

        public abstract boolean renderPictureFrame(int width, int height, long frameTimeNanos, byte[] pictureData);

        public abstract void flush();

        @Override
        public abstract void close();
    }
}
