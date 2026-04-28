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

/**
 * Experimental JBR-owned Skia interop service.
 *
 * <p>The ABI and build identifiers intentionally use non-constant initializers
 * so downstream compile-only clients cannot accidentally inline stale values.
 */
public abstract class JBRSkia {
    public static final int ABI_ID = Integer.parseInt("3");
    public static final String BUILD_ID = buildId();
    public static final int COMMAND_STREAM_MAGIC = Integer.parseInt("1246972723");
    public static final int COMMAND_STREAM_HEADER_SIZE = Integer.parseInt("4");
    public static final int COMMAND_STREAM_FLAGS_NONE = Integer.parseInt("0");
    public static final int COMMAND_CLEAR = Integer.parseInt("1");
    public static final int COMMAND_FILL_RECT = Integer.parseInt("2");
    public static final int COMMAND_STROKE_LINE = Integer.parseInt("3");
    public static final int COMMAND_FILL_OVAL = Integer.parseInt("4");
    public static final int COMMAND_STROKE_OVAL = Integer.parseInt("5");
    public static final int COMMAND_CLEAR_RECT = Integer.parseInt("6");
    public static final int COMMAND_SAVE = Integer.parseInt("7");
    public static final int COMMAND_RESTORE = Integer.parseInt("8");
    public static final int COMMAND_CLIP_RECT = Integer.parseInt("9");

    private static String buildId() {
        return "skia-interop-poc:" + ABI_ID;
    }

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

        public abstract boolean renderPictureFrame(int width, int height, long frameTimeNanos, byte[] pictureData);

        public abstract void flush();

        @Override
        public abstract void close();
    }
}
