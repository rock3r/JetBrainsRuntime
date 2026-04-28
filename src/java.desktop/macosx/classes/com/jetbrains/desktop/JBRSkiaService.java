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

import com.jetbrains.exported.JBRApi;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.metal.MTLRenderQueue;
import sun.java2d.pipe.hw.AccelSurface;

import java.awt.Color;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@JBRApi.Service
@JBRApi.Provides("JBRSkia")
public class JBRSkiaService extends JBRSkia {
    private static final String PROPERTY = "sun.java2d.skia.interop";
    private static final String NATIVE_DIAGNOSTIC_PROPERTY = "sun.java2d.skia.interop.nativeDiagnostic";
    private static final String NATIVE_LIBRARY_PROPERTY = "sun.java2d.skia.interop.library";
    private static final boolean NATIVE_BRIDGE_AVAILABLE = loadNativeBridge();
    private static final AtomicLong NEXT_SCOPE_ID = new AtomicLong(1);

    public JBRSkiaService() {
        if (!Boolean.getBoolean(PROPERTY)) {
            throw new JBRApi.ServiceNotAvailableException("JBR Skia interop is disabled");
        }
    }

    @Override
    public ScopedSkiaCanvas acquireCanvas(Graphics2D graphics) {
        Objects.requireNonNull(graphics, "graphics");
        return new PocScopedSkiaCanvas(
                NEXT_SCOPE_ID.getAndIncrement(),
                graphics,
                graphics.getClipBounds(),
                getMetalSurfaceMetadata(graphics)
        );
    }

    private record MetalSurfaceMetadata(long nativeOpsPtr, long texturePtr) {
        private static final MetalSurfaceMetadata EMPTY = new MetalSurfaceMetadata(0, 0);
    }

    private static final class PocScopedSkiaCanvas extends ScopedSkiaCanvas {
        private final long scopeId;
        private final Graphics2D graphics;
        private final Rectangle userSpaceClip;
        private final Rectangle deviceSpaceClip;
        private final long nativeOpsPtr;
        private final long metalTexturePtr;
        private boolean closed;
        private boolean flushed;

        private PocScopedSkiaCanvas(long scopeId, Graphics2D graphics, Rectangle userSpaceClip,
                                    MetalSurfaceMetadata metadata) {
            this.scopeId = scopeId;
            this.graphics = graphics;
            this.userSpaceClip = userSpaceClip == null ? null : new Rectangle(userSpaceClip);
            this.deviceSpaceClip = toDeviceSpaceClip(graphics, userSpaceClip);
            this.nativeOpsPtr = metadata.nativeOpsPtr();
            this.metalTexturePtr = metadata.texturePtr();
        }

        @Override
        public long getScopeId() {
            return scopeId;
        }

        @Override
        public int getBackend() {
            return BACKEND_METAL;
        }

        @Override
        public long getCanvasPtr() {
            return 0;
        }

        @Override
        public long getDirectContextPtr() {
            return 0;
        }

        @Override
        public long getMetalTexturePtr() {
            return metalTexturePtr;
        }

        @Override
        public int getPixelFormat() {
            return 0;
        }

        @Override
        public int getColorSpaceId() {
            return 0;
        }

        @Override
        public int getSampleCount() {
            return 1;
        }

        @Override
        public Rectangle getUserSpaceClip() {
            return userSpaceClip == null ? null : new Rectangle(userSpaceClip);
        }

        @Override
        public boolean renderDiagnosticFrame(int width, int height, long frameTimeNanos) {
            ensureOpen();
            if (width <= 0 || height <= 0) {
                return false;
            }
            if (Boolean.getBoolean(NATIVE_DIAGNOSTIC_PROPERTY)
                    && NATIVE_BRIDGE_AVAILABLE
                    && nativeOpsPtr != 0
                    && metalTexturePtr != 0
                    && nativeRenderDiagnosticFrame(nativeOpsPtr, metalTexturePtr, width, height, frameTimeNanos)) {
                return true;
            }

            Graphics2D diagnosticGraphics = (Graphics2D) graphics.create();
            try {
                diagnosticGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                diagnosticGraphics.setColor(new Color(8, 24, 31));
                diagnosticGraphics.fillRect(0, 0, width, height);

                int phase = (int) ((frameTimeNanos / 12_000_000L) % 48L);
                diagnosticGraphics.setColor(new Color(14, 180, 143, 190));
                for (int x = -height + phase; x < width + height; x += 48) {
                    diagnosticGraphics.drawLine(x, height, x + height, 0);
                }

                diagnosticGraphics.setColor(new Color(255, 205, 72, 230));
                int box = Math.max(32, Math.min(width, height) / 4);
                int x = Math.max(12, (width - box) / 2);
                int y = Math.max(12, (height - box) / 2);
                diagnosticGraphics.fillRoundRect(x, y, box, box, 14, 14);

                diagnosticGraphics.setColor(new Color(255, 255, 255, 230));
                diagnosticGraphics.drawString("JBR-owned paint scope", 16, Math.min(height - 16, 28));
                diagnosticGraphics.drawString("texture=0x" + Long.toHexString(metalTexturePtr), 16, Math.min(height - 16, 46));
            } finally {
                diagnosticGraphics.dispose();
            }
            return true;
        }

        @Override
        public boolean renderCommandFrame(int width, int height, long frameTimeNanos, int[] commands) {
            ensureOpen();
            Objects.requireNonNull(commands, "commands");
            if (width <= 0 || height <= 0 || commands.length == 0) {
                return false;
            }
            if (NATIVE_BRIDGE_AVAILABLE
                    && nativeOpsPtr != 0
                    && metalTexturePtr != 0
                    && nativeRenderCommandFrame(nativeOpsPtr, metalTexturePtr,
                            deviceSpaceClip.x, deviceSpaceClip.y, deviceSpaceClip.width, deviceSpaceClip.height,
                            width, height, frameTimeNanos, commands)) {
                return true;
            }

            Graphics2D commandGraphics = (Graphics2D) graphics.create();
            try {
                commandGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                return renderJava2DCommands(commandGraphics, commands);
            } finally {
                commandGraphics.dispose();
            }
        }

        @Override
        public boolean renderPictureFrame(int width, int height, long frameTimeNanos, byte[] pictureData) {
            ensureOpen();
            Objects.requireNonNull(pictureData, "pictureData");
            return width > 0
                    && height > 0
                    && pictureData.length > 0
                    && NATIVE_BRIDGE_AVAILABLE
                    && nativeOpsPtr != 0
                    && metalTexturePtr != 0
                    && nativeRenderPictureFrame(nativeOpsPtr, metalTexturePtr,
                            deviceSpaceClip.x, deviceSpaceClip.y, deviceSpaceClip.width, deviceSpaceClip.height,
                            width, height, frameTimeNanos, pictureData);
        }

        @Override
        public void flush() {
            ensureOpen();
            flushed = true;
        }

        @Override
        public void close() {
            if (!closed) {
                if (!flushed) {
                    flush();
                }
                closed = true;
            }
        }

        private void ensureOpen() {
            if (closed) {
                throw new IllegalStateException("JBR Skia scope is already closed");
            }
        }

        private static boolean renderJava2DCommands(Graphics2D g, int[] commands) {
            ArrayDeque<Graphics2D> stack = new ArrayDeque<>();
            Graphics2D current = g;
            int offset = 0;
            try {
                while (offset < commands.length) {
                    int op = commands[offset++];
                    if (op == COMMAND_SAVE) {
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                    } else if (op == COMMAND_RESTORE) {
                        if (stack.isEmpty()) return false;
                        current.dispose();
                        current = stack.removeLast();
                    } else if (op == COMMAND_CLIP_RECT) {
                        if (offset + 4 > commands.length) return false;
                        current.clipRect(commands[offset++], commands[offset++], commands[offset++], commands[offset++]);
                    } else if (op == COMMAND_CLEAR) {
                        if (offset + 1 > commands.length) return false;
                        current.setColor(new Color(commands[offset++], true));
                        Rectangle clip = current.getClipBounds();
                        if (clip == null) {
                            return false;
                        }
                        current.fillRect(clip.x, clip.y, clip.width, clip.height);
                    } else if (op == COMMAND_CLEAR_RECT) {
                        if (offset + 4 > commands.length) return false;
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        Composite previousComposite = current.getComposite();
                        current.setComposite(AlphaComposite.Clear);
                        current.fillRect(x, y, width, height);
                        current.setComposite(previousComposite);
                    } else if (op == COMMAND_FILL_RECT) {
                        if (offset + 6 > commands.length) return false;
                        current.setColor(new Color(commands[offset++], true));
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int radius = commands[offset++];
                        if (radius > 0) {
                            current.fillRoundRect(x, y, width, height, radius, radius);
                        } else {
                            current.fillRect(x, y, width, height);
                        }
                    } else if (op == COMMAND_STROKE_LINE) {
                        if (offset + 6 > commands.length) return false;
                        current.setColor(new Color(commands[offset++], true));
                        int x1 = commands[offset++];
                        int y1 = commands[offset++];
                        int x2 = commands[offset++];
                        int y2 = commands[offset++];
                        int strokeWidth = Math.max(1, commands[offset++]);
                        java.awt.Stroke previous = current.getStroke();
                        try {
                            current.setStroke(new java.awt.BasicStroke(strokeWidth));
                            current.drawLine(x1, y1, x2, y2);
                        } finally {
                            current.setStroke(previous);
                        }
                    } else if (op == COMMAND_FILL_OVAL) {
                        if (offset + 5 > commands.length) return false;
                        current.setColor(new Color(commands[offset++], true));
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        current.fillOval(x, y, width, height);
                    } else if (op == COMMAND_STROKE_OVAL) {
                        if (offset + 6 > commands.length) return false;
                        current.setColor(new Color(commands[offset++], true));
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int strokeWidth = Math.max(1, commands[offset++]);
                        java.awt.Stroke previous = current.getStroke();
                        try {
                            current.setStroke(new java.awt.BasicStroke(strokeWidth));
                            current.drawOval(x, y, width, height);
                        } finally {
                            current.setStroke(previous);
                        }
                    } else {
                        return false;
                    }
                }
                return stack.isEmpty();
            } finally {
                while (current != g) {
                    current.dispose();
                    current = stack.removeLast();
                }
            }
        }

        private static Rectangle toDeviceSpaceClip(Graphics2D graphics, Rectangle userSpaceClip) {
            Shape clip = userSpaceClip == null ? graphics.getClip() : userSpaceClip;
            if (clip == null) {
                return new Rectangle(0, 0, 0, 0);
            }
            Rectangle bounds = graphics.getTransform().createTransformedShape(clip).getBounds();
            return new Rectangle(
                    bounds.x,
                    bounds.y,
                    Math.max(1, bounds.width),
                    Math.max(1, bounds.height)
            );
        }
    }

    private static MetalSurfaceMetadata getMetalSurfaceMetadata(Graphics2D graphics) {
        if (!(graphics instanceof SunGraphics2D sunGraphics)) {
            return MetalSurfaceMetadata.EMPTY;
        }
        SurfaceData surfaceData = sunGraphics.getSurfaceData();
        if (!(surfaceData instanceof AccelSurface accelSurface)) {
            return MetalSurfaceMetadata.EMPTY;
        }

        long[] metadata = new long[2];
        MTLRenderQueue rq = MTLRenderQueue.getInstance();
        rq.lock();
        try {
            rq.flushAndInvokeNow(() -> {
                metadata[0] = accelSurface.getNativeOps();
                metadata[1] = accelSurface.getNativeResource(AccelSurface.TEXTURE);
            });
        } finally {
            rq.unlock();
        }
        return new MetalSurfaceMetadata(metadata[0], metadata[1]);
    }

    private static boolean loadNativeBridge() {
        String library = System.getProperty(NATIVE_LIBRARY_PROPERTY);
        if (library == null || library.isBlank()) {
            return false;
        }
        try {
            System.load(library);
            return true;
        } catch (RuntimeException | UnsatisfiedLinkError e) {
            System.err.println("JBR Skia interop native bridge unavailable: " + e.getMessage());
            return false;
        }
    }

    private static native boolean nativeRenderDiagnosticFrame(long nativeOpsPtr, long metalTexturePtr,
                                                             int width, int height, long frameTimeNanos);

    private static native boolean nativeRenderCommandFrame(long nativeOpsPtr, long metalTexturePtr,
                                                          int destinationX, int destinationY,
                                                          int destinationWidth, int destinationHeight,
                                                          int width, int height, long frameTimeNanos,
                                                          int[] commands);

    private static native boolean nativeRenderPictureFrame(long nativeOpsPtr, long metalTexturePtr,
                                                          int destinationX, int destinationY,
                                                          int destinationWidth, int destinationHeight,
                                                          int width, int height, long frameTimeNanos,
                                                          byte[] pictureData);
}
