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
    public static final int ABI_ID = Integer.parseInt("20");
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

    private static String buildId() {
        return "skia-interop-poc:" + ABI_ID;
    }

    public abstract int getCommandCapabilities();

    public abstract ScopedSkiaCanvas acquireCanvas(Graphics2D graphics);

    public abstract static class ScopedSkiaCanvas implements AutoCloseable {
        public static final int BACKEND_METAL = Integer.parseInt("1");

        public abstract long getScopeId();

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
