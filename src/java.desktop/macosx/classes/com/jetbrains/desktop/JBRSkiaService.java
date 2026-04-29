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
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@JBRApi.Service
@JBRApi.Provides("JBRSkia")
public class JBRSkiaService extends JBRSkia {
    private static final String PROPERTY = "sun.java2d.skia.interop";
    private static final String NATIVE_DIAGNOSTIC_PROPERTY = "sun.java2d.skia.interop.nativeDiagnostic";
    private static final String NATIVE_LIBRARY_PROPERTY = "sun.java2d.skia.interop.library";
    private static final int COMMAND_CAPABILITIES =
            COMMAND_CAP_CLEAR
                    | COMMAND_CAP_FILL_RECT
                    | COMMAND_CAP_STROKE_LINE
                    | COMMAND_CAP_FILL_OVAL
                    | COMMAND_CAP_STROKE_OVAL
                    | COMMAND_CAP_CLEAR_RECT
                    | COMMAND_CAP_SAVE_RESTORE
                    | COMMAND_CAP_CLIP_RECT
                    | COMMAND_CAP_USER_SPACE_COORDINATES
                    | COMMAND_CAP_RECORD_ANTIALIAS
                    | COMMAND_CAP_STROKE_METADATA
                    | COMMAND_CAP_BASIC_TRANSFORMS
                    | COMMAND_CAP_CLIP_RECT_OP
                    | COMMAND_CAP_SAVE_LAYER
                    | COMMAND_CAP_DRAW_IMAGE_ARGB
                    | COMMAND_CAP_IMAGE_CACHE
                    | COMMAND_CAP_DRAW_TEXT_UTF16
                    | COMMAND_CAP_CLEAR_IMAGE_CACHE
                    | COMMAND_CAP_DRAW_PARAGRAPH_UTF16
                    | COMMAND_CAP_PARAGRAPH_FONT_STYLE
                    | COMMAND_CAP_PARAGRAPH_LAYOUT
                    | COMMAND_CAP_PARAGRAPH_LINE_HEIGHT
                    | COMMAND_CAP_PARAGRAPH_OVERFLOW
                    | COMMAND_CAP_PARAGRAPH_DECORATION
                    | COMMAND_CAP_PARAGRAPH_LETTER_SPACING
                    | COMMAND_CAP_PARAGRAPH_BACKGROUND
                    | COMMAND_CAP_CLIP_PATH;
    private static final boolean NATIVE_BRIDGE_AVAILABLE = loadNativeBridge();
    private static final AtomicLong NEXT_SCOPE_ID = new AtomicLong(1);
    private static final int MAX_CACHED_IMAGES = 256;
    private static final Map<Long, BufferedImage> IMAGE_CACHE = Collections.synchronizedMap(
            new LinkedHashMap<Long, BufferedImage>(MAX_CACHED_IMAGES, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<Long, BufferedImage> eldest) {
                    return size() > MAX_CACHED_IMAGES;
                }
            });

    public JBRSkiaService() {
        if (!Boolean.getBoolean(PROPERTY)) {
            throw new JBRApi.ServiceNotAvailableException("JBR Skia interop is disabled");
        }
    }

    @Override
    public int getCommandCapabilities() {
        return COMMAND_CAPABILITIES;
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

    public static boolean isValidCommandStreamForTesting(int[] commands) {
        int commandEnd = commandPayloadEnd(commands);
        if (commandEnd < 0) {
            return false;
        }
        int offset = COMMAND_STREAM_HEADER_SIZE;
        while (offset < commandEnd) {
            CommandRecord record = readCommandRecord(commands, offset, commandEnd);
            if (record == null
                    || !hasExpectedRecordLength(record)
                    || !validateRecordArguments(commands, record)) {
                return false;
            }
            offset = record.recordEnd();
        }
        return offset == commandEnd;
    }

    private static int commandPayloadEnd(int[] commands) {
        if (commands == null
                || commands.length < COMMAND_STREAM_HEADER_SIZE
                || commands[0] != COMMAND_STREAM_MAGIC
                || commands[1] != ABI_ID
                || commands[2] != COMMAND_STREAM_FLAGS_NONE
                || commands[4] != COMMAND_COORDINATE_SPACE_SWING_USER
                || commands[5] != COMMAND_PAINT_FORMAT_SOLID_ARGB) {
            return -1;
        }
        int payloadLength = commands[3];
        if (payloadLength < 0 || payloadLength != commands.length - COMMAND_STREAM_HEADER_SIZE) {
            return -1;
        }
        return COMMAND_STREAM_HEADER_SIZE + payloadLength;
    }

    private static int recordLengthFromBytes(int recordByteLength) {
        if (recordByteLength < COMMAND_RECORD_HEADER_SIZE_BYTES || recordByteLength % Integer.BYTES != 0) {
            return -1;
        }
        return recordByteLength / Integer.BYTES;
    }

    private static int expectedRecordLength(int op) {
        if (op == COMMAND_SAVE || op == COMMAND_RESTORE) return 3;
        if (op == COMMAND_CLEAR_IMAGE_CACHE) return 3;
        if (op == COMMAND_ROTATE) return 4;
        if (op == COMMAND_TRANSLATE || op == COMMAND_SCALE) return 5;
        if (op == COMMAND_SAVE_LAYER) return 8;
        if (op == COMMAND_DRAW_IMAGE_ARGB) return -2;
        if (op == COMMAND_DEFINE_IMAGE_ARGB) return -3;
        if (op == COMMAND_DRAW_TEXT_UTF16) return -4;
        if (op == COMMAND_DRAW_PARAGRAPH_UTF16) return -5;
        if (op == COMMAND_CLIP_PATH) return -6;
        if (op == COMMAND_DRAW_IMAGE_REF) return 17;
        if (op == COMMAND_CLEAR) return 4;
        if (op == COMMAND_CLEAR_RECT) return 7;
        if (op == COMMAND_CLIP_RECT) return 8;
        if (op == COMMAND_FILL_OVAL) return 8;
        if (op == COMMAND_FILL_RECT) return 9;
        if (op == COMMAND_STROKE_LINE || op == COMMAND_STROKE_OVAL) return 12;
        return -1;
    }

    private static boolean hasExpectedRecordLength(CommandRecord record) {
        int expectedLength = expectedRecordLength(record.op());
        if (expectedLength == -2 && record.op() == COMMAND_DRAW_IMAGE_ARGB) {
            return record.recordLength() >= 16;
        }
        if (expectedLength == -3 && record.op() == COMMAND_DEFINE_IMAGE_ARGB) {
            return record.recordLength() >= 8;
        }
        if (expectedLength == -4 && record.op() == COMMAND_DRAW_TEXT_UTF16) {
            return record.recordLength() >= 8;
        }
        if (expectedLength == -5 && record.op() == COMMAND_DRAW_PARAGRAPH_UTF16) {
            return record.recordLength() >= 21;
        }
        if (expectedLength == -6 && record.op() == COMMAND_CLIP_PATH) {
            return record.recordLength() >= 6;
        }
        return expectedLength == record.recordLength();
    }

    private static boolean validateRecordArguments(int[] commands, CommandRecord record) {
        if ((record.op() == COMMAND_TRANSLATE || record.op() == COMMAND_SCALE || record.op() == COMMAND_ROTATE)
                && record.recordFlags() != COMMAND_RECORD_FLAGS_NONE) {
            return false;
        }
        if (record.op() == COMMAND_CLEAR_IMAGE_CACHE) {
            return record.recordFlags() == COMMAND_RECORD_FLAGS_NONE;
        }
        if (record.op() == COMMAND_SAVE_LAYER) {
            return record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    && commands[record.recordEnd() - 1] >= 0
                    && commands[record.recordEnd() - 1] <= 1000;
        }
        if (record.op() == COMMAND_CLIP_RECT) {
            int clipOp = commands[record.recordEnd() - 1];
            return clipOp == COMMAND_CLIP_OP_INTERSECT || clipOp == COMMAND_CLIP_OP_DIFFERENCE;
        }
        if (record.op() == COMMAND_CLIP_PATH) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int clipOp = commands[record.argsStart()];
            int fillType = commands[record.argsStart() + 1];
            int pathDataLength = commands[record.argsStart() + 2];
            return (clipOp == COMMAND_CLIP_OP_INTERSECT || clipOp == COMMAND_CLIP_OP_DIFFERENCE)
                    && (fillType == COMMAND_PATH_FILL_NON_ZERO || fillType == COMMAND_PATH_FILL_EVEN_ODD)
                    && pathDataLength >= 0
                    && pathDataLength <= 4096
                    && record.argsStart() + 3 + pathDataLength == record.recordEnd()
                    && validatePathData(commands, record.argsStart() + 3, record.recordEnd());
        }
        if (record.op() == COMMAND_DRAW_IMAGE_ARGB) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int imageWidth = commands[record.argsStart() + 8];
            int imageHeight = commands[record.argsStart() + 9];
            int alpha1000 = commands[record.argsStart() + 10];
            int filterQuality = commands[record.argsStart() + 11];
            int pixelCount = commands[record.argsStart() + 12];
            return imageWidth > 0
                    && imageHeight > 0
                    && imageWidth <= 4096
                    && imageHeight <= 4096
                    && pixelCount == imageWidth * imageHeight
                    && record.recordLength() == 16 + pixelCount
                    && alpha1000 >= 0
                    && alpha1000 <= 1000
                    && filterQuality >= 0
                    && filterQuality <= 3;
        }
        if (record.op() == COMMAND_DEFINE_IMAGE_ARGB) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE) {
                return false;
            }
            int imageWidth = commands[record.argsStart() + 2];
            int imageHeight = commands[record.argsStart() + 3];
            int pixelCount = commands[record.argsStart() + 4];
            return imageWidth > 0
                    && imageHeight > 0
                    && imageWidth <= 4096
                    && imageHeight <= 4096
                    && pixelCount == imageWidth * imageHeight
                    && record.recordLength() == 8 + pixelCount;
        }
        if (record.op() == COMMAND_DRAW_IMAGE_REF) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int imageWidth = commands[record.argsStart() + 10];
            int imageHeight = commands[record.argsStart() + 11];
            int alpha1000 = commands[record.argsStart() + 12];
            int filterQuality = commands[record.argsStart() + 13];
            return imageWidth > 0
                    && imageHeight > 0
                    && imageWidth <= 4096
                    && imageHeight <= 4096
                    && alpha1000 >= 0
                    && alpha1000 <= 1000
                    && filterQuality >= 0
                    && filterQuality <= 3;
        }
        if (record.op() == COMMAND_DRAW_TEXT_UTF16) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int fontSize1000 = commands[record.argsStart() + 2];
            int charCount = commands[record.argsStart() + 4];
            return fontSize1000 > 0
                    && charCount >= 0
                    && charCount <= 4096
                    && record.recordLength() == 8 + charCount;
        }
        if (record.op() == COMMAND_DRAW_PARAGRAPH_UTF16) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int width1000 = commands[record.argsStart() + 2];
            int fontSize1000 = commands[record.argsStart() + 3];
            int fontWeight = commands[record.argsStart() + 5];
            int fontWidth = commands[record.argsStart() + 6];
            int fontSlant = commands[record.argsStart() + 7];
            int textAlign = commands[record.argsStart() + 8];
            int textDirection = commands[record.argsStart() + 9];
            int lineHeightMultiplier1000 = commands[record.argsStart() + 10];
            int maxLines = commands[record.argsStart() + 11];
            int ellipsisMode = commands[record.argsStart() + 12];
            int decorationMask = commands[record.argsStart() + 13];
            int letterSpacing1000 = commands[record.argsStart() + 14];
            int backgroundSpecified = commands[record.argsStart() + 15];
            int charCount = commands[record.argsStart() + 17];
            return width1000 > 0
                    && fontSize1000 > 0
                    && fontWeight >= 1
                    && fontWeight <= 1000
                    && fontWidth >= 1
                    && fontWidth <= 9
                    && fontSlant >= 0
                    && fontSlant <= 2
                    && textAlign >= 0
                    && textAlign <= 5
                    && textDirection >= 0
                    && textDirection <= 1
                    && lineHeightMultiplier1000 >= 0
                    && lineHeightMultiplier1000 <= 100000
                    && maxLines >= 0
                    && maxLines <= 4096
                    && ellipsisMode >= 0
                    && ellipsisMode <= 1
                    && decorationMask >= 0
                    && decorationMask <= 3
                    && letterSpacing1000 >= -100000
                    && letterSpacing1000 <= 100000
                    && backgroundSpecified >= 0
                    && backgroundSpecified <= 1
                    && charCount >= 0
                    && charCount <= 4096
                    && record.recordLength() == 21 + charCount;
        }
        if (record.op() != COMMAND_STROKE_LINE && record.op() != COMMAND_STROKE_OVAL) {
            return true;
        }
        int strokeWidthIndex = record.recordEnd() - 4;
        int strokeCapIndex = record.recordEnd() - 3;
        int strokeJoinIndex = record.recordEnd() - 2;
        int strokeMiterIndex = record.recordEnd() - 1;
        return commands[strokeWidthIndex] >= 1
                && commands[strokeCapIndex] >= 0
                && commands[strokeCapIndex] <= 2
                && commands[strokeJoinIndex] >= 0
                && commands[strokeJoinIndex] <= 2
                && commands[strokeMiterIndex] >= 0;
    }

    private static boolean validatePathData(int[] commands, int offset, int recordEnd) {
        while (offset < recordEnd) {
            int verb = commands[offset++];
            if (verb == COMMAND_PATH_VERB_MOVE || verb == COMMAND_PATH_VERB_LINE) {
                offset += 2;
            } else if (verb == COMMAND_PATH_VERB_QUAD) {
                offset += 4;
            } else if (verb == COMMAND_PATH_VERB_CUBIC) {
                offset += 6;
            } else if (verb != COMMAND_PATH_VERB_CLOSE) {
                return false;
            }
            if (offset > recordEnd) {
                return false;
            }
        }
        return offset == recordEnd;
    }

    private static CommandRecord readCommandRecord(int[] commands, int offset, int commandEnd) {
        int recordStart = offset;
        int op = commands[offset++];
        if (offset >= commandEnd) {
            return null;
        }
        int recordByteLength = commands[offset++];
        if (offset >= commandEnd) {
            return null;
        }
        int recordFlags = commands[offset++];
        int recordLength = recordLengthFromBytes(recordByteLength);
        int recordEnd = recordStart + recordLength;
        if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || recordLength < 3 || recordEnd > commandEnd) {
            return null;
        }
        return new CommandRecord(op, offset, recordEnd, recordLength, recordFlags);
    }

    private record CommandRecord(int op, int argsStart, int recordEnd, int recordLength, int recordFlags) {
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
        public boolean renderCommandBufferFrame(int width, int height, long frameTimeNanos, byte[] commands) {
            ensureOpen();
            Objects.requireNonNull(commands, "commands");
            if (commands.length == 0 || commands.length % Integer.BYTES != 0) {
                return false;
            }
            if (NATIVE_BRIDGE_AVAILABLE
                    && nativeOpsPtr != 0
                    && metalTexturePtr != 0
                    && nativeRenderCommandBufferFrame(nativeOpsPtr, metalTexturePtr,
                            deviceSpaceClip.x, deviceSpaceClip.y, deviceSpaceClip.width, deviceSpaceClip.height,
                            width, height, frameTimeNanos, commands)) {
                return true;
            }
            if (width <= 0 || height <= 0) {
                return false;
            }
            Graphics2D commandGraphics = (Graphics2D) graphics.create();
            try {
                commandGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                return renderJava2DCommands(commandGraphics, decodeCommandBuffer(commands));
            } finally {
                commandGraphics.dispose();
            }
        }

        @Override
        public boolean renderCommandDirectFrame(int width, int height, long frameTimeNanos, ByteBuffer commands) {
            ensureOpen();
            Objects.requireNonNull(commands, "commands");
            ByteBuffer commandBuffer = commands.slice().order(ByteOrder.LITTLE_ENDIAN);
            int commandByteCount = commandBuffer.remaining();
            if (commandByteCount == 0 || commandByteCount % Integer.BYTES != 0) {
                return false;
            }
            if (NATIVE_BRIDGE_AVAILABLE
                    && nativeOpsPtr != 0
                    && metalTexturePtr != 0
                    && commandBuffer.isDirect()
                    && nativeRenderCommandDirectFrame(nativeOpsPtr, metalTexturePtr,
                            deviceSpaceClip.x, deviceSpaceClip.y, deviceSpaceClip.width, deviceSpaceClip.height,
                            width, height, frameTimeNanos, commandBuffer, commandByteCount)) {
                return true;
            }
            if (width <= 0 || height <= 0) {
                return false;
            }
            Graphics2D commandGraphics = (Graphics2D) graphics.create();
            try {
                commandGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                return renderJava2DCommands(commandGraphics, decodeCommandBuffer(commandBuffer));
            } finally {
                commandGraphics.dispose();
            }
        }

        private static int[] decodeCommandBuffer(byte[] commands) {
            int[] decoded = new int[commands.length / Integer.BYTES];
            for (int index = 0; index < decoded.length; index++) {
                int offset = index * Integer.BYTES;
                decoded[index] = (commands[offset] & 0xff)
                        | ((commands[offset + 1] & 0xff) << 8)
                        | ((commands[offset + 2] & 0xff) << 16)
                        | (commands[offset + 3] << 24);
            }
            return decoded;
        }

        private static int[] decodeCommandBuffer(ByteBuffer commands) {
            ByteBuffer duplicate = commands.slice().order(ByteOrder.LITTLE_ENDIAN);
            int[] decoded = new int[duplicate.remaining() / Integer.BYTES];
            for (int index = 0; index < decoded.length; index++) {
                decoded[index] = duplicate.getInt();
            }
            return decoded;
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
            int commandEnd = commandPayloadEnd(commands);
            if (commandEnd < 0) {
                return false;
            }

            ArrayDeque<Graphics2D> stack = new ArrayDeque<>();
            Graphics2D current = g;
            int offset = COMMAND_STREAM_HEADER_SIZE;
            try {
                while (offset < commandEnd) {
                    CommandRecord record = readCommandRecord(commands, offset, commandEnd);
                    if (record == null) return false;
                    int op = record.op();
                    int recordEnd = record.recordEnd();
                    boolean antiAlias = (record.recordFlags() & COMMAND_RECORD_FLAG_ANTIALIAS) != 0;
                    offset = record.argsStart();
                    if (op == COMMAND_SAVE) {
                        if (offset != recordEnd) return false;
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                    } else if (op == COMMAND_RESTORE) {
                        if (offset != recordEnd) return false;
                        if (stack.isEmpty()) return false;
                        current.dispose();
                        current = stack.removeLast();
                    } else if (op == COMMAND_CLIP_RECT) {
                        if (offset + 5 != recordEnd) return false;
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int clipOp = commands[offset++];
                        if (clipOp == COMMAND_CLIP_OP_INTERSECT) {
                            current.clipRect(x, y, width, height);
                        } else if (clipOp == COMMAND_CLIP_OP_DIFFERENCE) {
                            Shape previousClip = current.getClip();
                            if (previousClip == null) return false;
                            Area clip = new Area(previousClip);
                            clip.subtract(new Area(new Rectangle(x, y, width, height)));
                            current.setClip(clip);
                        } else {
                            return false;
                        }
                    } else if (op == COMMAND_CLIP_PATH) {
                        if (offset + 3 > recordEnd) return false;
                        int clipOp = commands[offset++];
                        int fillType = commands[offset++];
                        int pathDataLength = commands[offset++];
                        if ((clipOp != COMMAND_CLIP_OP_INTERSECT && clipOp != COMMAND_CLIP_OP_DIFFERENCE)
                                || (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
                                || pathDataLength < 0
                                || offset + pathDataLength != recordEnd) {
                            return false;
                        }
                        Path2D path = pathFromCommandData(commands, offset, recordEnd, fillType);
                        if (path == null) return false;
                        offset = recordEnd;
                        if (clipOp == COMMAND_CLIP_OP_INTERSECT) {
                            current.clip(path);
                        } else {
                            Shape previousClip = current.getClip();
                            if (previousClip == null) return false;
                            Area clip = new Area(previousClip);
                            clip.subtract(new Area(path));
                            current.setClip(clip);
                        }
                    } else if (op == COMMAND_TRANSLATE) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) return false;
                        current.translate(commands[offset++] / 1000.0, commands[offset++] / 1000.0);
                    } else if (op == COMMAND_SCALE) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) return false;
                        current.scale(commands[offset++] / 1000.0, commands[offset++] / 1000.0);
                    } else if (op == COMMAND_ROTATE) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 1 != recordEnd) return false;
                        current.rotate(Math.toRadians(commands[offset++] / 1000.0));
                    } else if (op == COMMAND_SAVE_LAYER) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 5 != recordEnd) return false;
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int alpha1000 = commands[offset++];
                        if (alpha1000 < 0 || alpha1000 > 1000) return false;
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.clipRect(x, y, width, height);
                    } else if (op == COMMAND_CLEAR_IMAGE_CACHE) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset != recordEnd) return false;
                        System.err.println("JBR_SKIA_INTEROP_IMAGE_CACHE_CLEAR backend=java2d");
                        IMAGE_CACHE.clear();
                    } else if (op == COMMAND_DEFINE_IMAGE_ARGB) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 5 > recordEnd) return false;
                        long cacheKey = cacheKey(commands[offset++], commands[offset++]);
                        int imageWidth = commands[offset++];
                        int imageHeight = commands[offset++];
                        int pixelCount = commands[offset++];
                        if (imageWidth <= 0 || imageHeight <= 0 || pixelCount != imageWidth * imageHeight
                                || offset + pixelCount != recordEnd) {
                            return false;
                        }
                        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
                        image.setRGB(0, 0, imageWidth, imageHeight, commands, offset, imageWidth);
                        offset += pixelCount;
                        IMAGE_CACHE.put(cacheKey, image);
                    } else if (op == COMMAND_DRAW_IMAGE_REF) {
                        if (offset + 14 != recordEnd) return false;
                        boolean filtered = (record.recordFlags() & COMMAND_RECORD_FLAG_ANTIALIAS) != 0;
                        int srcLeft1000 = commands[offset++];
                        int srcTop1000 = commands[offset++];
                        int srcRight1000 = commands[offset++];
                        int srcBottom1000 = commands[offset++];
                        int dstLeft1000 = commands[offset++];
                        int dstTop1000 = commands[offset++];
                        int dstRight1000 = commands[offset++];
                        int dstBottom1000 = commands[offset++];
                        long cacheKey = cacheKey(commands[offset++], commands[offset++]);
                        int imageWidth = commands[offset++];
                        int imageHeight = commands[offset++];
                        int alpha1000 = commands[offset++];
                        int filterQuality = commands[offset++];
                        BufferedImage image = IMAGE_CACHE.get(cacheKey);
                        if (image == null || image.getWidth() != imageWidth || image.getHeight() != imageHeight
                                || alpha1000 < 0 || alpha1000 > 1000 || filterQuality < 0 || filterQuality > 3) {
                            return false;
                        }
                        drawImage(current, image, filtered, srcLeft1000, srcTop1000, srcRight1000, srcBottom1000,
                                dstLeft1000, dstTop1000, dstRight1000, dstBottom1000, alpha1000);
                    } else if (op == COMMAND_DRAW_IMAGE_ARGB) {
                        if (offset + 13 > recordEnd) return false;
                        boolean filtered = (record.recordFlags() & COMMAND_RECORD_FLAG_ANTIALIAS) != 0;
                        int srcLeft1000 = commands[offset++];
                        int srcTop1000 = commands[offset++];
                        int srcRight1000 = commands[offset++];
                        int srcBottom1000 = commands[offset++];
                        int dstLeft1000 = commands[offset++];
                        int dstTop1000 = commands[offset++];
                        int dstRight1000 = commands[offset++];
                        int dstBottom1000 = commands[offset++];
                        int imageWidth = commands[offset++];
                        int imageHeight = commands[offset++];
                        int alpha1000 = commands[offset++];
                        int filterQuality = commands[offset++];
                        int pixelCount = commands[offset++];
                        if (imageWidth <= 0 || imageHeight <= 0 || pixelCount != imageWidth * imageHeight
                                || offset + pixelCount != recordEnd || alpha1000 < 0 || alpha1000 > 1000
                                || filterQuality < 0 || filterQuality > 3) {
                            return false;
                        }
                        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
                        image.setRGB(0, 0, imageWidth, imageHeight, commands, offset, imageWidth);
                        offset += pixelCount;
                        drawImage(current, image, filtered, srcLeft1000, srcTop1000, srcRight1000, srcBottom1000,
                                dstLeft1000, dstTop1000, dstRight1000, dstBottom1000, alpha1000);
                    } else if (op == COMMAND_DRAW_TEXT_UTF16) {
                        if (offset + 5 > recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        int x1000 = commands[offset++];
                        int baseline1000 = commands[offset++];
                        int fontSize1000 = commands[offset++];
                        int argb = commands[offset++];
                        int charCount = commands[offset++];
                        if (fontSize1000 <= 0 || charCount < 0 || charCount > 4096 || offset + charCount != recordEnd) {
                            return false;
                        }
                        StringBuilder text = new StringBuilder(charCount);
                        for (int index = 0; index < charCount; index++) {
                            int codeUnit = commands[offset++];
                            if (codeUnit < Character.MIN_VALUE || codeUnit > Character.MAX_VALUE) {
                                return false;
                            }
                            text.append((char) codeUnit);
                        }
                        java.awt.Font previousFont = current.getFont();
                        current.setColor(new Color(argb, true));
                        current.setFont(previousFont.deriveFont(fontSize1000 / 1000f));
                        current.drawString(text.toString(), x1000 / 1000f, baseline1000 / 1000f);
                        current.setFont(previousFont);
                    } else if (op == COMMAND_DRAW_PARAGRAPH_UTF16) {
                        if (offset + 6 > recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        int x1000 = commands[offset++];
                        int y1000 = commands[offset++];
                        int width1000 = commands[offset++];
                        int fontSize1000 = commands[offset++];
                        int argb = commands[offset++];
                        int fontWeight = commands[offset++];
                        int fontWidth = commands[offset++];
                        int fontSlant = commands[offset++];
                        int textAlign = commands[offset++];
                        int textDirection = commands[offset++];
                        int lineHeightMultiplier1000 = commands[offset++];
                        int maxLines = commands[offset++];
                        int ellipsisMode = commands[offset++];
                        int decorationMask = commands[offset++];
                        int letterSpacing1000 = commands[offset++];
                        int backgroundSpecified = commands[offset++];
                        int backgroundArgb = commands[offset++];
                        int charCount = commands[offset++];
                        if (width1000 <= 0 || fontSize1000 <= 0
                                || fontWeight < 1 || fontWeight > 1000
                                || fontWidth < 1 || fontWidth > 9
                                || fontSlant < 0 || fontSlant > 2
                                || textAlign < 0 || textAlign > 5
                                || textDirection < 0 || textDirection > 1
                                || lineHeightMultiplier1000 < 0 || lineHeightMultiplier1000 > 100000
                                || maxLines < 0 || maxLines > 4096
                                || ellipsisMode < 0 || ellipsisMode > 1
                                || decorationMask < 0 || decorationMask > 3
                                || letterSpacing1000 < -100000 || letterSpacing1000 > 100000
                                || backgroundSpecified < 0 || backgroundSpecified > 1
                                || charCount < 0 || charCount > 4096
                                || offset + charCount != recordEnd) {
                            return false;
                        }
                        StringBuilder text = new StringBuilder(charCount);
                        for (int index = 0; index < charCount; index++) {
                            int codeUnit = commands[offset++];
                            if (codeUnit < Character.MIN_VALUE || codeUnit > Character.MAX_VALUE) {
                                return false;
                            }
                            text.append((char) codeUnit);
                        }
                        java.awt.Font previousFont = current.getFont();
                        current.setColor(new Color(argb, true));
                        current.setFont(previousFont.deriveFont(fontSize1000 / 1000f));
                        current.drawString(text.toString(), x1000 / 1000f, y1000 / 1000f + current.getFontMetrics().getAscent());
                        current.setFont(previousFont);
                    } else if (op == COMMAND_CLEAR) {
                        if (offset + 1 != recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        current.setColor(new Color(commands[offset++], true));
                        Rectangle clip = current.getClipBounds();
                        if (clip == null) {
                            return false;
                        }
                        current.fillRect(clip.x, clip.y, clip.width, clip.height);
                    } else if (op == COMMAND_CLEAR_RECT) {
                        if (offset + 4 != recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        Composite previousComposite = current.getComposite();
                        current.setComposite(AlphaComposite.Clear);
                        current.fillRect(x, y, width, height);
                        current.setComposite(previousComposite);
                    } else if (op == COMMAND_FILL_RECT) {
                        if (offset + 6 != recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
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
                        if (offset + 9 != recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        current.setColor(new Color(commands[offset++], true));
                        int x1 = commands[offset++];
                        int y1 = commands[offset++];
                        int x2 = commands[offset++];
                        int y2 = commands[offset++];
                        int strokeWidth = Math.max(1, commands[offset++]);
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        float strokeMiter = commands[offset++] / 1000f;
                        java.awt.Stroke previous = current.getStroke();
                        java.awt.BasicStroke stroke = basicStroke(strokeWidth, strokeCap, strokeJoin, strokeMiter);
                        if (stroke == null) return false;
                        try {
                            current.setStroke(stroke);
                            current.drawLine(x1, y1, x2, y2);
                        } finally {
                            current.setStroke(previous);
                        }
                    } else if (op == COMMAND_FILL_OVAL) {
                        if (offset + 5 != recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        current.setColor(new Color(commands[offset++], true));
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        current.fillOval(x, y, width, height);
                    } else if (op == COMMAND_STROKE_OVAL) {
                        if (offset + 9 != recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        current.setColor(new Color(commands[offset++], true));
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int strokeWidth = Math.max(1, commands[offset++]);
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        float strokeMiter = commands[offset++] / 1000f;
                        java.awt.Stroke previous = current.getStroke();
                        java.awt.BasicStroke stroke = basicStroke(strokeWidth, strokeCap, strokeJoin, strokeMiter);
                        if (stroke == null) return false;
                        try {
                            current.setStroke(stroke);
                            current.drawOval(x, y, width, height);
                        } finally {
                            current.setStroke(previous);
                        }
                    } else {
                        return false;
                    }
                    if (offset != recordEnd) return false;
                }
                return stack.isEmpty();
            } finally {
                while (current != g) {
                    current.dispose();
                    current = stack.removeLast();
                }
            }
        }

        private static long cacheKey(int high, int low) {
            return ((long) high << 32) ^ (low & 0xffffffffL);
        }

        private static void drawImage(Graphics2D current, BufferedImage image, boolean filtered,
                                      int srcLeft1000, int srcTop1000, int srcRight1000, int srcBottom1000,
                                      int dstLeft1000, int dstTop1000, int dstRight1000, int dstBottom1000,
                                      int alpha1000) {
            Composite previousComposite = current.getComposite();
            Object previousInterpolation = current.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
            current.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha1000 / 1000.0f));
            current.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    filtered
                            ? RenderingHints.VALUE_INTERPOLATION_BILINEAR
                            : RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
            );
            current.drawImage(
                    image,
                    Math.round(dstLeft1000 / 1000.0f),
                    Math.round(dstTop1000 / 1000.0f),
                    Math.round(dstRight1000 / 1000.0f),
                    Math.round(dstBottom1000 / 1000.0f),
                    Math.round(srcLeft1000 / 1000.0f),
                    Math.round(srcTop1000 / 1000.0f),
                    Math.round(srcRight1000 / 1000.0f),
                    Math.round(srcBottom1000 / 1000.0f),
                    null
            );
            current.setComposite(previousComposite);
            current.setRenderingHint(RenderingHints.KEY_INTERPOLATION, previousInterpolation);
        }

        private static Path2D pathFromCommandData(int[] commands, int offset, int recordEnd, int fillType) {
            Path2D.Float path = new Path2D.Float(
                    fillType == COMMAND_PATH_FILL_EVEN_ODD ? Path2D.WIND_EVEN_ODD : Path2D.WIND_NON_ZERO
            );
            while (offset < recordEnd) {
                int verb = commands[offset++];
                if (verb == COMMAND_PATH_VERB_MOVE) {
                    if (offset + 2 > recordEnd) return null;
                    path.moveTo(commands[offset++] / 1000f, commands[offset++] / 1000f);
                } else if (verb == COMMAND_PATH_VERB_LINE) {
                    if (offset + 2 > recordEnd) return null;
                    path.lineTo(commands[offset++] / 1000f, commands[offset++] / 1000f);
                } else if (verb == COMMAND_PATH_VERB_QUAD) {
                    if (offset + 4 > recordEnd) return null;
                    path.quadTo(
                            commands[offset++] / 1000f,
                            commands[offset++] / 1000f,
                            commands[offset++] / 1000f,
                            commands[offset++] / 1000f
                    );
                } else if (verb == COMMAND_PATH_VERB_CUBIC) {
                    if (offset + 6 > recordEnd) return null;
                    path.curveTo(
                            commands[offset++] / 1000f,
                            commands[offset++] / 1000f,
                            commands[offset++] / 1000f,
                            commands[offset++] / 1000f,
                            commands[offset++] / 1000f,
                            commands[offset++] / 1000f
                    );
                } else if (verb == COMMAND_PATH_VERB_CLOSE) {
                    path.closePath();
                } else {
                    return null;
                }
            }
            return offset == recordEnd ? path : null;
        }

        private static void applyAntialiasing(Graphics2D g, boolean antiAlias) {
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF
            );
        }

        private static java.awt.BasicStroke basicStroke(int width, int cap, int join, float miter) {
            if (width < 1 || cap < 0 || cap > 2 || join < 0 || join > 2 || miter < 0) {
                return null;
            }
            return new java.awt.BasicStroke(width, cap, join, Math.max(1f, miter));
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

    private static native boolean nativeRenderCommandBufferFrame(long nativeOpsPtr, long metalTexturePtr,
                                                                int destinationX, int destinationY,
                                                                int destinationWidth, int destinationHeight,
                                                                int width, int height, long frameTimeNanos,
                                                                byte[] commands);

    private static native boolean nativeRenderCommandDirectFrame(long nativeOpsPtr, long metalTexturePtr,
                                                                int destinationX, int destinationY,
                                                                int destinationWidth, int destinationHeight,
                                                                int width, int height, long frameTimeNanos,
                                                                ByteBuffer commands, int commandByteCount);

    private static native boolean nativeRenderPictureFrame(long nativeOpsPtr, long metalTexturePtr,
                                                          int destinationX, int destinationY,
                                                          int destinationWidth, int destinationHeight,
                                                          int width, int height, long frameTimeNanos,
                                                          byte[] pictureData);
}
