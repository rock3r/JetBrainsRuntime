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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.Transparency;
import java.awt.geom.Area;
import java.awt.geom.Arc2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Iterator;
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
    private static final long COMMAND_CAPABILITIES =
            (long) COMMAND_CAP_CLEAR
                    | (long) COMMAND_CAP_FILL_RECT
                    | (long) COMMAND_CAP_STROKE_LINE
                    | (long) COMMAND_CAP_FILL_OVAL
                    | (long) COMMAND_CAP_STROKE_OVAL
                    | (long) COMMAND_CAP_CLEAR_RECT
                    | (long) COMMAND_CAP_SAVE_RESTORE
                    | (long) COMMAND_CAP_CLIP_RECT
                    | (long) COMMAND_CAP_USER_SPACE_COORDINATES
                    | (long) COMMAND_CAP_RECORD_ANTIALIAS
                    | (long) COMMAND_CAP_STROKE_METADATA
                    | (long) COMMAND_CAP_BASIC_TRANSFORMS
                    | (long) COMMAND_CAP_CLIP_RECT_OP
                    | (long) COMMAND_CAP_SAVE_LAYER
                    | (long) COMMAND_CAP_DRAW_IMAGE_ARGB
                    | (long) COMMAND_CAP_IMAGE_CACHE
                    | (long) COMMAND_CAP_DRAW_TEXT_UTF16
                    | (long) COMMAND_CAP_CLEAR_IMAGE_CACHE
                    | (long) COMMAND_CAP_DRAW_PARAGRAPH_UTF16
                    | (long) COMMAND_CAP_PARAGRAPH_FONT_STYLE
                    | (long) COMMAND_CAP_PARAGRAPH_LAYOUT
                    | (long) COMMAND_CAP_PARAGRAPH_LINE_HEIGHT
                    | (long) COMMAND_CAP_PARAGRAPH_OVERFLOW
                    | (long) COMMAND_CAP_PARAGRAPH_DECORATION
                    | (long) COMMAND_CAP_PARAGRAPH_LETTER_SPACING
                    | (long) COMMAND_CAP_PARAGRAPH_BACKGROUND
                    | (long) COMMAND_CAP_CLIP_PATH
                    | (long) COMMAND_CAP_DRAW_PATH
                    | (long) COMMAND_CAP_DRAW_ARC
                    | (long) COMMAND_CAP_DRAW_ROUND_RECT
                    | COMMAND_CAP64_FILL_RECT_LINEAR_GRADIENT
                    | COMMAND_CAP64_FILL_ROUND_RECT_LINEAR_GRADIENT
                    | COMMAND_CAP64_FILL_RECT_RADIAL_GRADIENT
                    | COMMAND_CAP64_FILL_ROUND_RECT_RADIAL_GRADIENT
                    | COMMAND_CAP64_FILL_PATH_LINEAR_GRADIENT
                    | COMMAND_CAP64_FILL_PATH_RADIAL_GRADIENT
                    | COMMAND_CAP64_FILL_RECT_SWEEP_GRADIENT
                    | COMMAND_CAP64_FILL_ROUND_RECT_SWEEP_GRADIENT
                    | COMMAND_CAP64_FILL_PATH_SWEEP_GRADIENT
                    | COMMAND_CAP64_EVICT_IMAGE_CACHE_KEY
                    | COMMAND_CAP64_TEXT_FONT_FAMILY
                    | COMMAND_CAP64_FILL_RECT_IMAGE_SHADER
                    | COMMAND_CAP64_STROKE_RECT_LINEAR_GRADIENT
                    | COMMAND_CAP64_STROKE_ROUND_RECT_LINEAR_GRADIENT
                    | COMMAND_CAP64_STROKE_RECT_RADIAL_GRADIENT;
    private static final boolean NATIVE_BRIDGE_AVAILABLE = loadNativeBridge();
    private static final AtomicLong NEXT_SCOPE_ID = new AtomicLong(1);
    private static final int MAX_CACHED_IMAGES = 256;
    private static final Map<ImageCacheKey, BufferedImage> IMAGE_CACHE = Collections.synchronizedMap(
            new LinkedHashMap<ImageCacheKey, BufferedImage>(MAX_CACHED_IMAGES, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<ImageCacheKey, BufferedImage> eldest) {
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
        return (int) COMMAND_CAPABILITIES;
    }

    @Override
    public long getCommandCapabilities64() {
        return COMMAND_CAPABILITIES;
    }

    @Override
    public int getNativeAbiVersion() {
        return NATIVE_ABI_VERSION;
    }

    @Override
    public int getNativeCommandStreamAbiId() {
        return ABI_ID;
    }

    @Override
    public String getNativeBuildId() {
        return BUILD_ID;
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
        if (op == COMMAND_EVICT_IMAGE_CACHE_KEY) return 5;
        if (op == COMMAND_ROTATE) return 4;
        if (op == COMMAND_TRANSLATE || op == COMMAND_SCALE) return 5;
        if (op == COMMAND_SAVE_LAYER) return 8;
        if (op == COMMAND_DRAW_IMAGE_ARGB) return -2;
        if (op == COMMAND_DEFINE_IMAGE_ARGB) return -3;
        if (op == COMMAND_DRAW_TEXT_UTF16) return -4;
        if (op == COMMAND_DRAW_PARAGRAPH_UTF16) return -5;
        if (op == COMMAND_CLIP_PATH) return -6;
        if (op == COMMAND_DRAW_PATH) return -7;
        if (op == COMMAND_DRAW_IMAGE_REF) return 17;
        if (op == COMMAND_DRAW_ARC) return 16;
        if (op == COMMAND_DRAW_ROUND_RECT) return 15;
        if (op == COMMAND_FILL_RECT_LINEAR_GRADIENT) return -8;
        if (op == COMMAND_FILL_ROUND_RECT_LINEAR_GRADIENT) return -9;
        if (op == COMMAND_FILL_RECT_RADIAL_GRADIENT) return -10;
        if (op == COMMAND_FILL_ROUND_RECT_RADIAL_GRADIENT) return -11;
        if (op == COMMAND_FILL_PATH_LINEAR_GRADIENT) return -12;
        if (op == COMMAND_FILL_PATH_RADIAL_GRADIENT) return -13;
        if (op == COMMAND_FILL_RECT_SWEEP_GRADIENT) return -14;
        if (op == COMMAND_FILL_ROUND_RECT_SWEEP_GRADIENT) return -15;
        if (op == COMMAND_FILL_PATH_SWEEP_GRADIENT) return -16;
        if (op == COMMAND_STROKE_RECT_LINEAR_GRADIENT) return -17;
        if (op == COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT) return -18;
        if (op == COMMAND_STROKE_RECT_RADIAL_GRADIENT) return -19;
        if (op == COMMAND_FILL_RECT_IMAGE_SHADER) return 14;
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
            return record.recordLength() >= 9;
        }
        if (expectedLength == -5 && record.op() == COMMAND_DRAW_PARAGRAPH_UTF16) {
            return record.recordLength() >= 22;
        }
        if (expectedLength == -6 && record.op() == COMMAND_CLIP_PATH) {
            return record.recordLength() >= 6;
        }
        if (expectedLength == -7 && record.op() == COMMAND_DRAW_PATH) {
            return record.recordLength() >= 11;
        }
        if (expectedLength == -8 && record.op() == COMMAND_FILL_RECT_LINEAR_GRADIENT) {
            return record.recordLength() >= 17;
        }
        if (expectedLength == -9 && record.op() == COMMAND_FILL_ROUND_RECT_LINEAR_GRADIENT) {
            return record.recordLength() >= 19;
        }
        if (expectedLength == -10 && record.op() == COMMAND_FILL_RECT_RADIAL_GRADIENT) {
            return record.recordLength() >= 16;
        }
        if (expectedLength == -11 && record.op() == COMMAND_FILL_ROUND_RECT_RADIAL_GRADIENT) {
            return record.recordLength() >= 18;
        }
        if (expectedLength == -12 && record.op() == COMMAND_FILL_PATH_LINEAR_GRADIENT) {
            return record.recordLength() >= 15;
        }
        if (expectedLength == -13 && record.op() == COMMAND_FILL_PATH_RADIAL_GRADIENT) {
            return record.recordLength() >= 14;
        }
        if (expectedLength == -14 && record.op() == COMMAND_FILL_RECT_SWEEP_GRADIENT) {
            return record.recordLength() >= 14;
        }
        if (expectedLength == -15 && record.op() == COMMAND_FILL_ROUND_RECT_SWEEP_GRADIENT) {
            return record.recordLength() >= 16;
        }
        if (expectedLength == -16 && record.op() == COMMAND_FILL_PATH_SWEEP_GRADIENT) {
            return record.recordLength() >= 12;
        }
        if (expectedLength == -17 && record.op() == COMMAND_STROKE_RECT_LINEAR_GRADIENT) {
            return record.recordLength() >= 21;
        }
        if (expectedLength == -18 && record.op() == COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT) {
            return record.recordLength() >= 23;
        }
        if (expectedLength == -19 && record.op() == COMMAND_STROKE_RECT_RADIAL_GRADIENT) {
            return record.recordLength() >= 20;
        }
        return expectedLength == record.recordLength();
    }

    private static boolean validateRecordArguments(int[] commands, CommandRecord record) {
        if ((record.op() == COMMAND_TRANSLATE || record.op() == COMMAND_SCALE || record.op() == COMMAND_ROTATE)
                && record.recordFlags() != COMMAND_RECORD_FLAGS_NONE) {
            return false;
        }
        if (record.op() == COMMAND_CLEAR_IMAGE_CACHE || record.op() == COMMAND_EVICT_IMAGE_CACHE_KEY) {
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
        if (record.op() == COMMAND_DRAW_PATH) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int paintStyle = commands[record.argsStart()];
            int strokeWidth = commands[record.argsStart() + 2];
            int strokeCap = commands[record.argsStart() + 3];
            int strokeJoin = commands[record.argsStart() + 4];
            int strokeMiter = commands[record.argsStart() + 5];
            int fillType = commands[record.argsStart() + 6];
            int pathDataLength = commands[record.argsStart() + 7];
            return (paintStyle == COMMAND_PAINT_STYLE_FILL || paintStyle == COMMAND_PAINT_STYLE_STROKE)
                    && (paintStyle == COMMAND_PAINT_STYLE_FILL || isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter))
                    && (fillType == COMMAND_PATH_FILL_NON_ZERO || fillType == COMMAND_PATH_FILL_EVEN_ODD)
                    && pathDataLength >= 0
                    && pathDataLength <= 4096
                    && record.argsStart() + 8 + pathDataLength == record.recordEnd()
                    && validatePathData(commands, record.argsStart() + 8, record.recordEnd());
        }
        if (record.op() == COMMAND_DRAW_ARC) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int paintStyle = commands[record.argsStart()];
            int left1000 = commands[record.argsStart() + 2];
            int top1000 = commands[record.argsStart() + 3];
            int right1000 = commands[record.argsStart() + 4];
            int bottom1000 = commands[record.argsStart() + 5];
            int useCenter = commands[record.argsStart() + 8];
            int strokeWidth = commands[record.argsStart() + 9];
            int strokeCap = commands[record.argsStart() + 10];
            int strokeJoin = commands[record.argsStart() + 11];
            int strokeMiter = commands[record.argsStart() + 12];
            return (paintStyle == COMMAND_PAINT_STYLE_FILL || paintStyle == COMMAND_PAINT_STYLE_STROKE)
                    && right1000 >= left1000
                    && bottom1000 >= top1000
                    && (useCenter == 0 || useCenter == 1)
                    && (paintStyle == COMMAND_PAINT_STYLE_FILL || isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter));
        }
        if (record.op() == COMMAND_DRAW_ROUND_RECT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int paintStyle = commands[record.argsStart()];
            int left1000 = commands[record.argsStart() + 2];
            int top1000 = commands[record.argsStart() + 3];
            int right1000 = commands[record.argsStart() + 4];
            int bottom1000 = commands[record.argsStart() + 5];
            int radiusX1000 = commands[record.argsStart() + 6];
            int radiusY1000 = commands[record.argsStart() + 7];
            int strokeWidth = commands[record.argsStart() + 8];
            int strokeCap = commands[record.argsStart() + 9];
            int strokeJoin = commands[record.argsStart() + 10];
            int strokeMiter = commands[record.argsStart() + 11];
            return (paintStyle == COMMAND_PAINT_STYLE_FILL || paintStyle == COMMAND_PAINT_STYLE_STROKE)
                    && right1000 >= left1000
                    && bottom1000 >= top1000
                    && radiusX1000 >= 0
                    && radiusY1000 >= 0
                    && (paintStyle == COMMAND_PAINT_STYLE_FILL || isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter));
        }
        if (record.op() == COMMAND_FILL_RECT_IMAGE_SHADER) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int left1000 = commands[record.argsStart()];
            int top1000 = commands[record.argsStart() + 1];
            int right1000 = commands[record.argsStart() + 2];
            int bottom1000 = commands[record.argsStart() + 3];
            int imageWidth = commands[record.argsStart() + 6];
            int imageHeight = commands[record.argsStart() + 7];
            int tileModeX = commands[record.argsStart() + 8];
            int tileModeY = commands[record.argsStart() + 9];
            int alpha1000 = commands[record.argsStart() + 10];
            return right1000 >= left1000
                    && bottom1000 >= top1000
                    && imageWidth > 0
                    && imageHeight > 0
                    && imageWidth <= 4096
                    && imageHeight <= 4096
                    && tileModeX >= 0
                    && tileModeX <= 3
                    && tileModeY >= 0
                    && tileModeY <= 3
                    && alpha1000 >= 0
                    && alpha1000 <= 1000;
        }
        if (record.op() == COMMAND_FILL_RECT_LINEAR_GRADIENT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int left1000 = commands[record.argsStart()];
            int top1000 = commands[record.argsStart() + 1];
            int right1000 = commands[record.argsStart() + 2];
            int bottom1000 = commands[record.argsStart() + 3];
            int tileMode = commands[record.argsStart() + 8];
            int colorCount = commands[record.argsStart() + 9];
            if (right1000 < left1000 || bottom1000 < top1000 || tileMode < 0 || tileMode > 3
                    || colorCount < 2 || colorCount > 16
                    || record.argsStart() + 10 + colorCount * 2 != record.recordEnd()) {
                return false;
            }
            int previousStop = -1;
            for (int i = 0; i < colorCount; i++) {
                int stop1000 = commands[record.argsStart() + 11 + i * 2];
                if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                    return false;
                }
                previousStop = stop1000;
            }
            return true;
        }
        if (record.op() == COMMAND_STROKE_RECT_LINEAR_GRADIENT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int left1000 = commands[record.argsStart()];
            int top1000 = commands[record.argsStart() + 1];
            int right1000 = commands[record.argsStart() + 2];
            int bottom1000 = commands[record.argsStart() + 3];
            int strokeWidth1000 = commands[record.argsStart() + 4];
            int strokeCap = commands[record.argsStart() + 5];
            int strokeJoin = commands[record.argsStart() + 6];
            int strokeMiter1000 = commands[record.argsStart() + 7];
            int tileMode = commands[record.argsStart() + 12];
            int colorCount = commands[record.argsStart() + 13];
            if (right1000 < left1000 || bottom1000 < top1000
                    || strokeWidth1000 <= 0 || strokeCap < 0 || strokeCap > 2
                    || strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0
                    || tileMode < 0 || tileMode > 3
                    || colorCount < 2 || colorCount > 16
                    || record.argsStart() + 14 + colorCount * 2 != record.recordEnd()) {
                return false;
            }
            int previousStop = -1;
            for (int i = 0; i < colorCount; i++) {
                int stop1000 = commands[record.argsStart() + 15 + i * 2];
                if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                    return false;
                }
                previousStop = stop1000;
            }
            return true;
        }
        if (record.op() == COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int left1000 = commands[record.argsStart()];
            int top1000 = commands[record.argsStart() + 1];
            int right1000 = commands[record.argsStart() + 2];
            int bottom1000 = commands[record.argsStart() + 3];
            int radiusX1000 = commands[record.argsStart() + 4];
            int radiusY1000 = commands[record.argsStart() + 5];
            int strokeWidth1000 = commands[record.argsStart() + 6];
            int strokeCap = commands[record.argsStart() + 7];
            int strokeJoin = commands[record.argsStart() + 8];
            int strokeMiter1000 = commands[record.argsStart() + 9];
            int tileMode = commands[record.argsStart() + 14];
            int colorCount = commands[record.argsStart() + 15];
            if (right1000 < left1000 || bottom1000 < top1000
                    || radiusX1000 < 0 || radiusY1000 < 0
                    || strokeWidth1000 <= 0 || strokeCap < 0 || strokeCap > 2
                    || strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0
                    || tileMode < 0 || tileMode > 3
                    || colorCount < 2 || colorCount > 16
                    || record.argsStart() + 16 + colorCount * 2 != record.recordEnd()) {
                return false;
            }
            int previousStop = -1;
            for (int i = 0; i < colorCount; i++) {
                int stop1000 = commands[record.argsStart() + 17 + i * 2];
                if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                    return false;
                }
                previousStop = stop1000;
            }
            return true;
        }
        if (record.op() == COMMAND_FILL_ROUND_RECT_LINEAR_GRADIENT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int left1000 = commands[record.argsStart()];
            int top1000 = commands[record.argsStart() + 1];
            int right1000 = commands[record.argsStart() + 2];
            int bottom1000 = commands[record.argsStart() + 3];
            int radiusX1000 = commands[record.argsStart() + 4];
            int radiusY1000 = commands[record.argsStart() + 5];
            int tileMode = commands[record.argsStart() + 10];
            int colorCount = commands[record.argsStart() + 11];
            if (right1000 < left1000 || bottom1000 < top1000
                    || radiusX1000 < 0 || radiusY1000 < 0
                    || tileMode < 0 || tileMode > 3
                    || colorCount < 2 || colorCount > 16
                    || record.argsStart() + 12 + colorCount * 2 != record.recordEnd()) {
                return false;
            }
            int previousStop = -1;
            for (int i = 0; i < colorCount; i++) {
                int stop1000 = commands[record.argsStart() + 13 + i * 2];
                if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                    return false;
                }
                previousStop = stop1000;
            }
            return true;
        }
        if (record.op() == COMMAND_FILL_RECT_RADIAL_GRADIENT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int left1000 = commands[record.argsStart()];
            int top1000 = commands[record.argsStart() + 1];
            int right1000 = commands[record.argsStart() + 2];
            int bottom1000 = commands[record.argsStart() + 3];
            int radius1000 = commands[record.argsStart() + 6];
            int tileMode = commands[record.argsStart() + 7];
            int colorCount = commands[record.argsStart() + 8];
            if (right1000 < left1000 || bottom1000 < top1000
                    || radius1000 <= 0
                    || tileMode < 0 || tileMode > 3
                    || colorCount < 2 || colorCount > 16
                    || record.argsStart() + 9 + colorCount * 2 != record.recordEnd()) {
                return false;
            }
            int previousStop = -1;
            for (int i = 0; i < colorCount; i++) {
                int stop1000 = commands[record.argsStart() + 10 + i * 2];
                if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                    return false;
                }
                previousStop = stop1000;
            }
            return true;
        }
        if (record.op() == COMMAND_STROKE_RECT_RADIAL_GRADIENT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int left1000 = commands[record.argsStart()];
            int top1000 = commands[record.argsStart() + 1];
            int right1000 = commands[record.argsStart() + 2];
            int bottom1000 = commands[record.argsStart() + 3];
            int strokeWidth1000 = commands[record.argsStart() + 4];
            int strokeCap = commands[record.argsStart() + 5];
            int strokeJoin = commands[record.argsStart() + 6];
            int strokeMiter1000 = commands[record.argsStart() + 7];
            int radius1000 = commands[record.argsStart() + 10];
            int tileMode = commands[record.argsStart() + 11];
            int colorCount = commands[record.argsStart() + 12];
            if (right1000 < left1000 || bottom1000 < top1000
                    || strokeWidth1000 <= 0 || strokeCap < 0 || strokeCap > 2
                    || strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0
                    || radius1000 <= 0
                    || tileMode < 0 || tileMode > 3
                    || colorCount < 2 || colorCount > 16
                    || record.argsStart() + 13 + colorCount * 2 != record.recordEnd()) {
                return false;
            }
            int previousStop = -1;
            for (int i = 0; i < colorCount; i++) {
                int stop1000 = commands[record.argsStart() + 14 + i * 2];
                if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                    return false;
                }
                previousStop = stop1000;
            }
            return true;
        }
        if (record.op() == COMMAND_FILL_ROUND_RECT_RADIAL_GRADIENT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int left1000 = commands[record.argsStart()];
            int top1000 = commands[record.argsStart() + 1];
            int right1000 = commands[record.argsStart() + 2];
            int bottom1000 = commands[record.argsStart() + 3];
            int radiusX1000 = commands[record.argsStart() + 4];
            int radiusY1000 = commands[record.argsStart() + 5];
            int radius1000 = commands[record.argsStart() + 8];
            int tileMode = commands[record.argsStart() + 9];
            int colorCount = commands[record.argsStart() + 10];
            if (right1000 < left1000 || bottom1000 < top1000
                    || radiusX1000 < 0 || radiusY1000 < 0 || radius1000 <= 0
                    || tileMode < 0 || tileMode > 3
                    || colorCount < 2 || colorCount > 16
                    || record.argsStart() + 11 + colorCount * 2 != record.recordEnd()) {
                return false;
            }
            int previousStop = -1;
            for (int i = 0; i < colorCount; i++) {
                int stop1000 = commands[record.argsStart() + 12 + i * 2];
                if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                    return false;
                }
                previousStop = stop1000;
            }
            return true;
        }
        if (record.op() == COMMAND_FILL_PATH_LINEAR_GRADIENT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int fillType = commands[record.argsStart()];
            int pathDataLength = commands[record.argsStart() + 1];
            int gradientStart = record.argsStart() + 2 + pathDataLength;
            if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
                    || pathDataLength < 0
                    || pathDataLength > 4096
                    || gradientStart + 10 > record.recordEnd()
                    || !validatePathData(commands, record.argsStart() + 2, gradientStart)) {
                return false;
            }
            int tileMode = commands[gradientStart + 4];
            int colorCount = commands[gradientStart + 5];
            if (tileMode < 0 || tileMode > 3
                    || colorCount < 2 || colorCount > 16
                    || gradientStart + 6 + colorCount * 2 != record.recordEnd()) {
                return false;
            }
            int previousStop = -1;
            for (int i = 0; i < colorCount; i++) {
                int stop1000 = commands[gradientStart + 7 + i * 2];
                if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                    return false;
                }
                previousStop = stop1000;
            }
            return true;
        }
        if (record.op() == COMMAND_FILL_PATH_RADIAL_GRADIENT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int fillType = commands[record.argsStart()];
            int pathDataLength = commands[record.argsStart() + 1];
            int gradientStart = record.argsStart() + 2 + pathDataLength;
            if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
                    || pathDataLength < 0
                    || pathDataLength > 4096
                    || gradientStart + 9 > record.recordEnd()
                    || !validatePathData(commands, record.argsStart() + 2, gradientStart)) {
                return false;
            }
            int radius1000 = commands[gradientStart + 2];
            int tileMode = commands[gradientStart + 3];
            int colorCount = commands[gradientStart + 4];
            if (radius1000 <= 0
                    || tileMode < 0 || tileMode > 3
                    || colorCount < 2 || colorCount > 16
                    || gradientStart + 5 + colorCount * 2 != record.recordEnd()) {
                return false;
            }
            int previousStop = -1;
            for (int i = 0; i < colorCount; i++) {
                int stop1000 = commands[gradientStart + 6 + i * 2];
                if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                    return false;
                }
                previousStop = stop1000;
            }
            return true;
        }
        if (record.op() == COMMAND_FILL_PATH_SWEEP_GRADIENT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int fillType = commands[record.argsStart()];
            int pathDataLength = commands[record.argsStart() + 1];
            int gradientStart = record.argsStart() + 2 + pathDataLength;
            if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
                    || pathDataLength < 0
                    || pathDataLength > 4096
                    || gradientStart + 7 > record.recordEnd()
                    || !validatePathData(commands, record.argsStart() + 2, gradientStart)) {
                return false;
            }
            int colorCount = commands[gradientStart + 2];
            if (colorCount < 2 || colorCount > 16
                    || gradientStart + 3 + colorCount * 2 != record.recordEnd()) {
                return false;
            }
            int previousStop = -1;
            for (int i = 0; i < colorCount; i++) {
                int stop1000 = commands[gradientStart + 4 + i * 2];
                if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                    return false;
                }
                previousStop = stop1000;
            }
            return true;
        }
        if (record.op() == COMMAND_FILL_RECT_SWEEP_GRADIENT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int left1000 = commands[record.argsStart()];
            int top1000 = commands[record.argsStart() + 1];
            int right1000 = commands[record.argsStart() + 2];
            int bottom1000 = commands[record.argsStart() + 3];
            int colorCount = commands[record.argsStart() + 6];
            if (right1000 < left1000 || bottom1000 < top1000
                    || colorCount < 2 || colorCount > 16
                    || record.argsStart() + 7 + colorCount * 2 != record.recordEnd()) {
                return false;
            }
            int previousStop = -1;
            for (int i = 0; i < colorCount; i++) {
                int stop1000 = commands[record.argsStart() + 8 + i * 2];
                if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                    return false;
                }
                previousStop = stop1000;
            }
            return true;
        }
        if (record.op() == COMMAND_FILL_ROUND_RECT_SWEEP_GRADIENT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int left1000 = commands[record.argsStart()];
            int top1000 = commands[record.argsStart() + 1];
            int right1000 = commands[record.argsStart() + 2];
            int bottom1000 = commands[record.argsStart() + 3];
            int radiusX1000 = commands[record.argsStart() + 4];
            int radiusY1000 = commands[record.argsStart() + 5];
            int colorCount = commands[record.argsStart() + 8];
            if (right1000 < left1000 || bottom1000 < top1000
                    || radiusX1000 < 0 || radiusY1000 < 0
                    || colorCount < 2 || colorCount > 16
                    || record.argsStart() + 9 + colorCount * 2 != record.recordEnd()) {
                return false;
            }
            int previousStop = -1;
            for (int i = 0; i < colorCount; i++) {
                int stop1000 = commands[record.argsStart() + 10 + i * 2];
                if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                    return false;
                }
                previousStop = stop1000;
            }
            return true;
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
            int fontFamilyCount = commands[record.argsStart() + 4];
            if (fontFamilyCount < 0 || fontFamilyCount > 256
                    || record.argsStart() + 5 + fontFamilyCount >= record.recordEnd()) {
                return false;
            }
            int charCount = commands[record.argsStart() + 5 + fontFamilyCount];
            return fontSize1000 > 0
                    && charCount >= 0
                    && charCount <= 4096
                    && record.recordLength() == 9 + fontFamilyCount + charCount;
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
            int fontFamilyCount = commands[record.argsStart() + 8];
            if (fontFamilyCount < 0 || fontFamilyCount > 256
                    || record.argsStart() + 9 + fontFamilyCount + 9 >= record.recordEnd()) {
                return false;
            }
            int metadataStart = record.argsStart() + 9 + fontFamilyCount;
            int textAlign = commands[metadataStart];
            int textDirection = commands[metadataStart + 1];
            int lineHeightMultiplier1000 = commands[metadataStart + 2];
            int maxLines = commands[metadataStart + 3];
            int ellipsisMode = commands[metadataStart + 4];
            int decorationMask = commands[metadataStart + 5];
            int letterSpacing1000 = commands[metadataStart + 6];
            int backgroundSpecified = commands[metadataStart + 7];
            int charCount = commands[metadataStart + 9];
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
                    && record.recordLength() == 22 + fontFamilyCount + charCount;
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

    private static boolean isValidStrokeMetadata(int strokeWidth, int strokeCap, int strokeJoin, int strokeMiter1000) {
        return strokeWidth >= 1
                && strokeCap >= 0
                && strokeCap <= 2
                && strokeJoin >= 0
                && strokeJoin <= 2
                && strokeMiter1000 >= 0;
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

    private record MetalSurfaceMetadata(long nativeOpsPtr, long contextPtr, long texturePtr) {
        private static final MetalSurfaceMetadata EMPTY = new MetalSurfaceMetadata(0, 0, 0);
    }

    private record ImageCacheKey(long contextId, long imageId) {
    }

    private static final class PocScopedSkiaCanvas extends ScopedSkiaCanvas {
        private final long scopeId;
        private final Graphics2D graphics;
        private final Rectangle userSpaceClip;
        private final Rectangle deviceSpaceClip;
        private final long nativeOpsPtr;
        private final long contextPtr;
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
            this.contextPtr = metadata.contextPtr();
            this.metalTexturePtr = metadata.texturePtr();
        }

        @Override
        public long getScopeId() {
            return scopeId;
        }

        @Override
        public long getSurfaceId() {
            return nativeOpsPtr;
        }

        @Override
        public long getContextId() {
            return contextPtr;
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
                return renderJava2DCommands(commandGraphics, commands, contextPtr);
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
                return renderJava2DCommands(commandGraphics, decodeCommandBuffer(commands), contextPtr);
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
                return renderJava2DCommands(commandGraphics, decodeCommandBuffer(commandBuffer), contextPtr);
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

        private static boolean renderJava2DCommands(Graphics2D g, int[] commands, long contextPtr) {
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
                    } else if (op == COMMAND_DRAW_PATH) {
                        if (offset + 8 > recordEnd) return false;
                        int paintStyle = commands[offset++];
                        int argb = commands[offset++];
                        int strokeWidth = commands[offset++];
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        int strokeMiter = commands[offset++];
                        int fillType = commands[offset++];
                        int pathDataLength = commands[offset++];
                        if ((paintStyle != COMMAND_PAINT_STYLE_FILL && paintStyle != COMMAND_PAINT_STYLE_STROKE)
                                || (paintStyle == COMMAND_PAINT_STYLE_STROKE && !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter))
                                || (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
                                || pathDataLength < 0
                                || offset + pathDataLength != recordEnd) {
                            return false;
                        }
                        Path2D path = pathFromCommandData(commands, offset, recordEnd, fillType);
                        if (path == null) return false;
                        offset = recordEnd;
                        applyAntialiasing(current, antiAlias);
                        current.setColor(new Color(argb, true));
                        if (paintStyle == COMMAND_PAINT_STYLE_FILL) {
                            current.fill(path);
                        } else {
                            current.setStroke(new BasicStroke(
                                    strokeWidth,
                                    strokeCap == 1 ? BasicStroke.CAP_ROUND : strokeCap == 2 ? BasicStroke.CAP_SQUARE : BasicStroke.CAP_BUTT,
                                    strokeJoin == 1 ? BasicStroke.JOIN_ROUND : strokeJoin == 2 ? BasicStroke.JOIN_BEVEL : BasicStroke.JOIN_MITER,
                                    Math.max(1f, strokeMiter / 1000f)
                            ));
                            current.draw(path);
                        }
                    } else if (op == COMMAND_FILL_PATH_LINEAR_GRADIENT) {
                        if (offset + 2 > recordEnd) return false;
                        int fillType = commands[offset++];
                        int pathDataLength = commands[offset++];
                        int pathEnd = offset + pathDataLength;
                        if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
                                || pathDataLength < 0
                                || pathDataLength > 4096
                                || pathEnd + 10 > recordEnd) {
                            return false;
                        }
                        Path2D path = pathFromCommandData(commands, offset, pathEnd, fillType);
                        if (path == null) return false;
                        offset = pathEnd;
                        int fromX1000 = commands[offset++];
                        int fromY1000 = commands[offset++];
                        int toX1000 = commands[offset++];
                        int toY1000 = commands[offset++];
                        int tileMode = commands[offset++];
                        int colorCount = commands[offset++];
                        if (tileMode < 0 || tileMode > 3 || colorCount < 2 || colorCount > 16
                                || offset + colorCount * 2 != recordEnd) {
                            return false;
                        }
                        Color[] colors = new Color[colorCount];
                        float[] fractions = new float[colorCount];
                        int previousStop = -1;
                        for (int i = 0; i < colorCount; i++) {
                            colors[i] = new Color(commands[offset++], true);
                            int stop1000 = commands[offset++];
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                                return false;
                            }
                            previousStop = stop1000;
                            fractions[i] = stop1000 / 1000f;
                        }
                        applyAntialiasing(current, antiAlias);
                        current.setPaint(new LinearGradientPaint(
                                new Point2D.Float(fromX1000 / 1000f, fromY1000 / 1000f),
                                new Point2D.Float(toX1000 / 1000f, toY1000 / 1000f),
                                fractions,
                                colors,
                                gradientCycleMethod(tileMode)
                        ));
                        current.fill(path);
                    } else if (op == COMMAND_FILL_PATH_RADIAL_GRADIENT) {
                        if (offset + 2 > recordEnd) return false;
                        int fillType = commands[offset++];
                        int pathDataLength = commands[offset++];
                        int pathEnd = offset + pathDataLength;
                        if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
                                || pathDataLength < 0
                                || pathDataLength > 4096
                                || pathEnd + 9 > recordEnd) {
                            return false;
                        }
                        Path2D path = pathFromCommandData(commands, offset, pathEnd, fillType);
                        if (path == null) return false;
                        offset = pathEnd;
                        int centerX1000 = commands[offset++];
                        int centerY1000 = commands[offset++];
                        int radius1000 = commands[offset++];
                        int tileMode = commands[offset++];
                        int colorCount = commands[offset++];
                        if (radius1000 <= 0 || tileMode < 0 || tileMode > 3 || colorCount < 2 || colorCount > 16
                                || offset + colorCount * 2 != recordEnd) {
                            return false;
                        }
                        Color[] colors = new Color[colorCount];
                        float[] fractions = new float[colorCount];
                        int previousStop = -1;
                        for (int i = 0; i < colorCount; i++) {
                            colors[i] = new Color(commands[offset++], true);
                            int stop1000 = commands[offset++];
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                                return false;
                            }
                            previousStop = stop1000;
                            fractions[i] = stop1000 / 1000f;
                        }
                        applyAntialiasing(current, antiAlias);
                        current.setPaint(new RadialGradientPaint(
                                new Point2D.Float(centerX1000 / 1000f, centerY1000 / 1000f),
                                radius1000 / 1000f,
                                fractions,
                                colors,
                                gradientCycleMethod(tileMode)
                        ));
                        current.fill(path);
                    } else if (op == COMMAND_FILL_PATH_SWEEP_GRADIENT) {
                        if (offset + 2 > recordEnd) return false;
                        int fillType = commands[offset++];
                        int pathDataLength = commands[offset++];
                        int pathEnd = offset + pathDataLength;
                        if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
                                || pathDataLength < 0
                                || pathDataLength > 4096
                                || pathEnd + 7 > recordEnd) {
                            return false;
                        }
                        Path2D path = pathFromCommandData(commands, offset, pathEnd, fillType);
                        if (path == null) return false;
                        offset = pathEnd;
                        int centerX1000 = commands[offset++];
                        int centerY1000 = commands[offset++];
                        int colorCount = commands[offset++];
                        if (colorCount < 2 || colorCount > 16 || offset + colorCount * 2 != recordEnd) {
                            return false;
                        }
                        Color[] colors = new Color[colorCount];
                        float[] fractions = new float[colorCount];
                        int previousStop = -1;
                        for (int i = 0; i < colorCount; i++) {
                            colors[i] = new Color(commands[offset++], true);
                            int stop1000 = commands[offset++];
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                                return false;
                            }
                            previousStop = stop1000;
                            fractions[i] = stop1000 / 1000f;
                        }
                        applyAntialiasing(current, antiAlias);
                        current.setPaint(new SweepGradientPaint(
                                centerX1000 / 1000f,
                                centerY1000 / 1000f,
                                fractions,
                                colors
                        ));
                        current.fill(path);
                    } else if (op == COMMAND_DRAW_ARC) {
                        if (offset + 13 != recordEnd) return false;
                        int paintStyle = commands[offset++];
                        int argb = commands[offset++];
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int startAngle1000 = commands[offset++];
                        int sweepAngle1000 = commands[offset++];
                        int useCenter = commands[offset++];
                        int strokeWidth = commands[offset++];
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        int strokeMiter = commands[offset++];
                        if ((paintStyle != COMMAND_PAINT_STYLE_FILL && paintStyle != COMMAND_PAINT_STYLE_STROKE)
                                || right1000 < left1000
                                || bottom1000 < top1000
                                || (useCenter != 0 && useCenter != 1)
                                || (paintStyle == COMMAND_PAINT_STYLE_STROKE && !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter))) {
                            return false;
                        }
                        Arc2D.Float arc = new Arc2D.Float(
                                left1000 / 1000f,
                                top1000 / 1000f,
                                (right1000 - left1000) / 1000f,
                                (bottom1000 - top1000) / 1000f,
                                startAngle1000 / 1000f,
                                sweepAngle1000 / 1000f,
                                useCenter == 1 ? Arc2D.PIE : Arc2D.OPEN
                        );
                        applyAntialiasing(current, antiAlias);
                        current.setColor(new Color(argb, true));
                        if (paintStyle == COMMAND_PAINT_STYLE_FILL) {
                            current.fill(arc);
                        } else {
                            current.setStroke(new BasicStroke(
                                    strokeWidth,
                                    strokeCap == 1 ? BasicStroke.CAP_ROUND : strokeCap == 2 ? BasicStroke.CAP_SQUARE : BasicStroke.CAP_BUTT,
                                    strokeJoin == 1 ? BasicStroke.JOIN_ROUND : strokeJoin == 2 ? BasicStroke.JOIN_BEVEL : BasicStroke.JOIN_MITER,
                                    Math.max(1f, strokeMiter / 1000f)
                            ));
                            current.draw(arc);
                        }
                    } else if (op == COMMAND_DRAW_ROUND_RECT) {
                        if (offset + 12 != recordEnd) return false;
                        int paintStyle = commands[offset++];
                        int argb = commands[offset++];
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int radiusX1000 = commands[offset++];
                        int radiusY1000 = commands[offset++];
                        int strokeWidth = commands[offset++];
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        int strokeMiter = commands[offset++];
                        if ((paintStyle != COMMAND_PAINT_STYLE_FILL && paintStyle != COMMAND_PAINT_STYLE_STROKE)
                                || right1000 < left1000
                                || bottom1000 < top1000
                                || radiusX1000 < 0
                                || radiusY1000 < 0
                                || (paintStyle == COMMAND_PAINT_STYLE_STROKE && !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter))) {
                            return false;
                        }
                        RoundRectangle2D.Float roundRect = new RoundRectangle2D.Float(
                                left1000 / 1000f,
                                top1000 / 1000f,
                                (right1000 - left1000) / 1000f,
                                (bottom1000 - top1000) / 1000f,
                                radiusX1000 / 1000f,
                                radiusY1000 / 1000f
                        );
                        applyAntialiasing(current, antiAlias);
                        current.setColor(new Color(argb, true));
                        if (paintStyle == COMMAND_PAINT_STYLE_FILL) {
                            current.fill(roundRect);
                        } else {
                            current.setStroke(new BasicStroke(
                                    strokeWidth,
                                    strokeCap == 1 ? BasicStroke.CAP_ROUND : strokeCap == 2 ? BasicStroke.CAP_SQUARE : BasicStroke.CAP_BUTT,
                                    strokeJoin == 1 ? BasicStroke.JOIN_ROUND : strokeJoin == 2 ? BasicStroke.JOIN_BEVEL : BasicStroke.JOIN_MITER,
                                    Math.max(1f, strokeMiter / 1000f)
                            ));
                            current.draw(roundRect);
                        }
                    } else if (op == COMMAND_FILL_RECT_LINEAR_GRADIENT) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                                && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                            return false;
                        }
                        if (offset + 10 > recordEnd) return false;
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int fromX1000 = commands[offset++];
                        int fromY1000 = commands[offset++];
                        int toX1000 = commands[offset++];
                        int toY1000 = commands[offset++];
                        int tileMode = commands[offset++];
                        int colorCount = commands[offset++];
                        if (right1000 < left1000 || bottom1000 < top1000 || tileMode < 0 || tileMode > 3
                                || colorCount < 2 || colorCount > 16 || offset + colorCount * 2 != recordEnd) {
                            return false;
                        }
                        Color[] colors = new Color[colorCount];
                        float[] fractions = new float[colorCount];
                        int previousStop = -1;
                        for (int i = 0; i < colorCount; i++) {
                            colors[i] = new Color(commands[offset++], true);
                            int stop1000 = commands[offset++];
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                                return false;
                            }
                            previousStop = stop1000;
                            fractions[i] = stop1000 / 1000f;
                        }
                        applyAntialiasing(current, antiAlias);
                        current.setPaint(new LinearGradientPaint(
                                new Point2D.Float(fromX1000 / 1000f, fromY1000 / 1000f),
                                new Point2D.Float(toX1000 / 1000f, toY1000 / 1000f),
                                fractions,
                                colors,
                                gradientCycleMethod(tileMode)
                        ));
                        current.fill(new java.awt.geom.Rectangle2D.Float(
                                left1000 / 1000f,
                                top1000 / 1000f,
                                (right1000 - left1000) / 1000f,
                                (bottom1000 - top1000) / 1000f
                        ));
                    } else if (op == COMMAND_STROKE_RECT_LINEAR_GRADIENT) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                                && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                            return false;
                        }
                        if (offset + 14 > recordEnd) return false;
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int strokeWidth1000 = commands[offset++];
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        int strokeMiter1000 = commands[offset++];
                        int fromX1000 = commands[offset++];
                        int fromY1000 = commands[offset++];
                        int toX1000 = commands[offset++];
                        int toY1000 = commands[offset++];
                        int tileMode = commands[offset++];
                        int colorCount = commands[offset++];
                        if (right1000 < left1000 || bottom1000 < top1000
                                || strokeWidth1000 <= 0 || strokeCap < 0 || strokeCap > 2
                                || strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0
                                || tileMode < 0 || tileMode > 3
                                || colorCount < 2 || colorCount > 16 || offset + colorCount * 2 != recordEnd) {
                            return false;
                        }
                        Color[] colors = new Color[colorCount];
                        float[] fractions = new float[colorCount];
                        int previousStop = -1;
                        for (int i = 0; i < colorCount; i++) {
                            colors[i] = new Color(commands[offset++], true);
                            int stop1000 = commands[offset++];
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                                return false;
                            }
                            previousStop = stop1000;
                            fractions[i] = stop1000 / 1000f;
                        }
                        applyAntialiasing(current, antiAlias);
                        current.setPaint(new LinearGradientPaint(
                                new Point2D.Float(fromX1000 / 1000f, fromY1000 / 1000f),
                                new Point2D.Float(toX1000 / 1000f, toY1000 / 1000f),
                                fractions,
                                colors,
                                gradientCycleMethod(tileMode)
                        ));
                        current.setStroke(new BasicStroke(
                                strokeWidth1000 / 1000f,
                                strokeCap == 1 ? BasicStroke.CAP_ROUND : strokeCap == 2 ? BasicStroke.CAP_SQUARE : BasicStroke.CAP_BUTT,
                                strokeJoin == 1 ? BasicStroke.JOIN_ROUND : strokeJoin == 2 ? BasicStroke.JOIN_BEVEL : BasicStroke.JOIN_MITER,
                                Math.max(1f, strokeMiter1000 / 1000f)
                        ));
                        current.draw(new java.awt.geom.Rectangle2D.Float(
                                left1000 / 1000f,
                                top1000 / 1000f,
                                (right1000 - left1000) / 1000f,
                                (bottom1000 - top1000) / 1000f
                        ));
                    } else if (op == COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                                && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                            return false;
                        }
                        if (offset + 16 > recordEnd) return false;
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int radiusX1000 = commands[offset++];
                        int radiusY1000 = commands[offset++];
                        int strokeWidth1000 = commands[offset++];
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        int strokeMiter1000 = commands[offset++];
                        int fromX1000 = commands[offset++];
                        int fromY1000 = commands[offset++];
                        int toX1000 = commands[offset++];
                        int toY1000 = commands[offset++];
                        int tileMode = commands[offset++];
                        int colorCount = commands[offset++];
                        if (right1000 < left1000 || bottom1000 < top1000
                                || radiusX1000 < 0 || radiusY1000 < 0
                                || strokeWidth1000 <= 0 || strokeCap < 0 || strokeCap > 2
                                || strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0
                                || tileMode < 0 || tileMode > 3
                                || colorCount < 2 || colorCount > 16 || offset + colorCount * 2 != recordEnd) {
                            return false;
                        }
                        Color[] colors = new Color[colorCount];
                        float[] fractions = new float[colorCount];
                        int previousStop = -1;
                        for (int i = 0; i < colorCount; i++) {
                            colors[i] = new Color(commands[offset++], true);
                            int stop1000 = commands[offset++];
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                                return false;
                            }
                            previousStop = stop1000;
                            fractions[i] = stop1000 / 1000f;
                        }
                        applyAntialiasing(current, antiAlias);
                        current.setPaint(new LinearGradientPaint(
                                new Point2D.Float(fromX1000 / 1000f, fromY1000 / 1000f),
                                new Point2D.Float(toX1000 / 1000f, toY1000 / 1000f),
                                fractions,
                                colors,
                                gradientCycleMethod(tileMode)
                        ));
                        current.setStroke(new BasicStroke(
                                strokeWidth1000 / 1000f,
                                strokeCap == 1 ? BasicStroke.CAP_ROUND : strokeCap == 2 ? BasicStroke.CAP_SQUARE : BasicStroke.CAP_BUTT,
                                strokeJoin == 1 ? BasicStroke.JOIN_ROUND : strokeJoin == 2 ? BasicStroke.JOIN_BEVEL : BasicStroke.JOIN_MITER,
                                Math.max(1f, strokeMiter1000 / 1000f)
                        ));
                        current.draw(new java.awt.geom.RoundRectangle2D.Float(
                                left1000 / 1000f,
                                top1000 / 1000f,
                                (right1000 - left1000) / 1000f,
                                (bottom1000 - top1000) / 1000f,
                                radiusX1000 * 2f / 1000f,
                                radiusY1000 * 2f / 1000f
                        ));
                    } else if (op == COMMAND_FILL_ROUND_RECT_LINEAR_GRADIENT) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                                && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                            return false;
                        }
                        if (offset + 12 > recordEnd) return false;
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int radiusX1000 = commands[offset++];
                        int radiusY1000 = commands[offset++];
                        int fromX1000 = commands[offset++];
                        int fromY1000 = commands[offset++];
                        int toX1000 = commands[offset++];
                        int toY1000 = commands[offset++];
                        int tileMode = commands[offset++];
                        int colorCount = commands[offset++];
                        if (right1000 < left1000 || bottom1000 < top1000
                                || radiusX1000 < 0 || radiusY1000 < 0
                                || tileMode < 0 || tileMode > 3
                                || colorCount < 2 || colorCount > 16 || offset + colorCount * 2 != recordEnd) {
                            return false;
                        }
                        Color[] colors = new Color[colorCount];
                        float[] fractions = new float[colorCount];
                        int previousStop = -1;
                        for (int i = 0; i < colorCount; i++) {
                            colors[i] = new Color(commands[offset++], true);
                            int stop1000 = commands[offset++];
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                                return false;
                            }
                            previousStop = stop1000;
                            fractions[i] = stop1000 / 1000f;
                        }
                        applyAntialiasing(current, antiAlias);
                        current.setPaint(new LinearGradientPaint(
                                new Point2D.Float(fromX1000 / 1000f, fromY1000 / 1000f),
                                new Point2D.Float(toX1000 / 1000f, toY1000 / 1000f),
                                fractions,
                                colors,
                                gradientCycleMethod(tileMode)
                        ));
                        current.fill(new RoundRectangle2D.Float(
                                left1000 / 1000f,
                                top1000 / 1000f,
                                (right1000 - left1000) / 1000f,
                                (bottom1000 - top1000) / 1000f,
                                radiusX1000 / 1000f,
                                radiusY1000 / 1000f
                        ));
                    } else if (op == COMMAND_FILL_RECT_RADIAL_GRADIENT) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                                && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                            return false;
                        }
                        if (offset + 9 > recordEnd) return false;
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int centerX1000 = commands[offset++];
                        int centerY1000 = commands[offset++];
                        int radius1000 = commands[offset++];
                        int tileMode = commands[offset++];
                        int colorCount = commands[offset++];
                        if (right1000 < left1000 || bottom1000 < top1000
                                || radius1000 <= 0
                                || tileMode < 0 || tileMode > 3
                                || colorCount < 2 || colorCount > 16 || offset + colorCount * 2 != recordEnd) {
                            return false;
                        }
                        Color[] colors = new Color[colorCount];
                        float[] fractions = new float[colorCount];
                        int previousStop = -1;
                        for (int i = 0; i < colorCount; i++) {
                            colors[i] = new Color(commands[offset++], true);
                            int stop1000 = commands[offset++];
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                                return false;
                            }
                            previousStop = stop1000;
                            fractions[i] = stop1000 / 1000f;
                        }
                        applyAntialiasing(current, antiAlias);
                        current.setPaint(new RadialGradientPaint(
                                new Point2D.Float(centerX1000 / 1000f, centerY1000 / 1000f),
                                radius1000 / 1000f,
                                fractions,
                                colors,
                                gradientCycleMethod(tileMode)
                        ));
                        current.fill(new java.awt.geom.Rectangle2D.Float(
                                left1000 / 1000f,
                                top1000 / 1000f,
                                (right1000 - left1000) / 1000f,
                                (bottom1000 - top1000) / 1000f
                        ));
                    } else if (op == COMMAND_STROKE_RECT_RADIAL_GRADIENT) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                                && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                            return false;
                        }
                        if (offset + 13 > recordEnd) return false;
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int strokeWidth1000 = commands[offset++];
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        int strokeMiter1000 = commands[offset++];
                        int centerX1000 = commands[offset++];
                        int centerY1000 = commands[offset++];
                        int radius1000 = commands[offset++];
                        int tileMode = commands[offset++];
                        int colorCount = commands[offset++];
                        if (right1000 < left1000 || bottom1000 < top1000
                                || strokeWidth1000 <= 0 || strokeCap < 0 || strokeCap > 2
                                || strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0
                                || radius1000 <= 0
                                || tileMode < 0 || tileMode > 3
                                || colorCount < 2 || colorCount > 16 || offset + colorCount * 2 != recordEnd) {
                            return false;
                        }
                        Color[] colors = new Color[colorCount];
                        float[] fractions = new float[colorCount];
                        int previousStop = -1;
                        for (int i = 0; i < colorCount; i++) {
                            colors[i] = new Color(commands[offset++], true);
                            int stop1000 = commands[offset++];
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                                return false;
                            }
                            previousStop = stop1000;
                            fractions[i] = stop1000 / 1000f;
                        }
                        applyAntialiasing(current, antiAlias);
                        current.setPaint(new RadialGradientPaint(
                                new Point2D.Float(centerX1000 / 1000f, centerY1000 / 1000f),
                                radius1000 / 1000f,
                                fractions,
                                colors,
                                gradientCycleMethod(tileMode)
                        ));
                        current.setStroke(new BasicStroke(
                                strokeWidth1000 / 1000f,
                                strokeCap == 1 ? BasicStroke.CAP_ROUND : strokeCap == 2 ? BasicStroke.CAP_SQUARE : BasicStroke.CAP_BUTT,
                                strokeJoin == 1 ? BasicStroke.JOIN_ROUND : strokeJoin == 2 ? BasicStroke.JOIN_BEVEL : BasicStroke.JOIN_MITER,
                                Math.max(1f, strokeMiter1000 / 1000f)
                        ));
                        current.draw(new java.awt.geom.Rectangle2D.Float(
                                left1000 / 1000f,
                                top1000 / 1000f,
                                (right1000 - left1000) / 1000f,
                                (bottom1000 - top1000) / 1000f
                        ));
                    } else if (op == COMMAND_FILL_RECT_SWEEP_GRADIENT) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                                && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                            return false;
                        }
                        if (offset + 7 > recordEnd) return false;
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int centerX1000 = commands[offset++];
                        int centerY1000 = commands[offset++];
                        int colorCount = commands[offset++];
                        if (right1000 < left1000 || bottom1000 < top1000
                                || colorCount < 2 || colorCount > 16 || offset + colorCount * 2 != recordEnd) {
                            return false;
                        }
                        Color[] colors = new Color[colorCount];
                        float[] fractions = new float[colorCount];
                        int previousStop = -1;
                        for (int i = 0; i < colorCount; i++) {
                            colors[i] = new Color(commands[offset++], true);
                            int stop1000 = commands[offset++];
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                                return false;
                            }
                            previousStop = stop1000;
                            fractions[i] = stop1000 / 1000f;
                        }
                        applyAntialiasing(current, antiAlias);
                        current.setPaint(new SweepGradientPaint(centerX1000 / 1000f, centerY1000 / 1000f, fractions, colors));
                        current.fill(new java.awt.geom.Rectangle2D.Float(
                                left1000 / 1000f,
                                top1000 / 1000f,
                                (right1000 - left1000) / 1000f,
                                (bottom1000 - top1000) / 1000f
                        ));
                    } else if (op == COMMAND_FILL_ROUND_RECT_SWEEP_GRADIENT) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                                && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                            return false;
                        }
                        if (offset + 9 > recordEnd) return false;
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int radiusX1000 = commands[offset++];
                        int radiusY1000 = commands[offset++];
                        int centerX1000 = commands[offset++];
                        int centerY1000 = commands[offset++];
                        int colorCount = commands[offset++];
                        if (right1000 < left1000 || bottom1000 < top1000
                                || radiusX1000 < 0 || radiusY1000 < 0
                                || colorCount < 2 || colorCount > 16 || offset + colorCount * 2 != recordEnd) {
                            return false;
                        }
                        Color[] colors = new Color[colorCount];
                        float[] fractions = new float[colorCount];
                        int previousStop = -1;
                        for (int i = 0; i < colorCount; i++) {
                            colors[i] = new Color(commands[offset++], true);
                            int stop1000 = commands[offset++];
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                                return false;
                            }
                            previousStop = stop1000;
                            fractions[i] = stop1000 / 1000f;
                        }
                        applyAntialiasing(current, antiAlias);
                        current.setPaint(new SweepGradientPaint(centerX1000 / 1000f, centerY1000 / 1000f, fractions, colors));
                        current.fill(new RoundRectangle2D.Float(
                                left1000 / 1000f,
                                top1000 / 1000f,
                                (right1000 - left1000) / 1000f,
                                (bottom1000 - top1000) / 1000f,
                                radiusX1000 / 1000f,
                                radiusY1000 / 1000f
                        ));
                    } else if (op == COMMAND_FILL_ROUND_RECT_RADIAL_GRADIENT) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                                && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                            return false;
                        }
                        if (offset + 11 > recordEnd) return false;
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int radiusX1000 = commands[offset++];
                        int radiusY1000 = commands[offset++];
                        int centerX1000 = commands[offset++];
                        int centerY1000 = commands[offset++];
                        int radius1000 = commands[offset++];
                        int tileMode = commands[offset++];
                        int colorCount = commands[offset++];
                        if (right1000 < left1000 || bottom1000 < top1000
                                || radiusX1000 < 0 || radiusY1000 < 0 || radius1000 <= 0
                                || tileMode < 0 || tileMode > 3
                                || colorCount < 2 || colorCount > 16 || offset + colorCount * 2 != recordEnd) {
                            return false;
                        }
                        Color[] colors = new Color[colorCount];
                        float[] fractions = new float[colorCount];
                        int previousStop = -1;
                        for (int i = 0; i < colorCount; i++) {
                            colors[i] = new Color(commands[offset++], true);
                            int stop1000 = commands[offset++];
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                                return false;
                            }
                            previousStop = stop1000;
                            fractions[i] = stop1000 / 1000f;
                        }
                        applyAntialiasing(current, antiAlias);
                        current.setPaint(new RadialGradientPaint(
                                new Point2D.Float(centerX1000 / 1000f, centerY1000 / 1000f),
                                radius1000 / 1000f,
                                fractions,
                                colors,
                                gradientCycleMethod(tileMode)
                        ));
                        current.fill(new RoundRectangle2D.Float(
                                left1000 / 1000f,
                                top1000 / 1000f,
                                (right1000 - left1000) / 1000f,
                                (bottom1000 - top1000) / 1000f,
                                radiusX1000 / 1000f,
                                radiusY1000 / 1000f
                        ));
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
                        int cleared = clearImageCacheForContext(contextPtr);
                        System.err.println("JBR_SKIA_INTEROP_IMAGE_CACHE_CLEAR backend=java2d contextId=0x"
                                + Long.toHexString(contextPtr) + " cleared=" + cleared);
                    } else if (op == COMMAND_EVICT_IMAGE_CACHE_KEY) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) return false;
                        long cacheKey = cacheKey(commands[offset++], commands[offset++]);
                        BufferedImage removed = IMAGE_CACHE.remove(new ImageCacheKey(contextPtr, cacheKey));
                        System.err.println("JBR_SKIA_INTEROP_IMAGE_CACHE_EVICT backend=java2d contextId=0x"
                                + Long.toHexString(contextPtr) + " key=0x" + Long.toHexString(cacheKey)
                                + " removed=" + (removed != null));
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
                        IMAGE_CACHE.put(new ImageCacheKey(contextPtr, cacheKey), image);
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
                        BufferedImage image = IMAGE_CACHE.get(new ImageCacheKey(contextPtr, cacheKey));
                        if (image == null || image.getWidth() != imageWidth || image.getHeight() != imageHeight
                                || alpha1000 < 0 || alpha1000 > 1000 || filterQuality < 0 || filterQuality > 3) {
                            return false;
                        }
                        drawImage(current, image, filtered, srcLeft1000, srcTop1000, srcRight1000, srcBottom1000,
                                dstLeft1000, dstTop1000, dstRight1000, dstBottom1000, alpha1000);
                    } else if (op == COMMAND_FILL_RECT_IMAGE_SHADER) {
                        if (offset + 11 != recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        long cacheKey = cacheKey(commands[offset++], commands[offset++]);
                        int imageWidth = commands[offset++];
                        int imageHeight = commands[offset++];
                        int tileModeX = commands[offset++];
                        int tileModeY = commands[offset++];
                        int alpha1000 = commands[offset++];
                        BufferedImage image = IMAGE_CACHE.get(new ImageCacheKey(contextPtr, cacheKey));
                        if (image == null || image.getWidth() != imageWidth || image.getHeight() != imageHeight
                                || right1000 < left1000 || bottom1000 < top1000
                                || tileModeX < 0 || tileModeX > 3 || tileModeY < 0 || tileModeY > 3
                                || alpha1000 < 0 || alpha1000 > 1000) {
                            return false;
                        }
                        Paint previousPaint = current.getPaint();
                        Composite previousComposite = current.getComposite();
                        current.setPaint(new TexturePaint(image, new Rectangle(0, 0, imageWidth, imageHeight)));
                        if (alpha1000 < 1000) {
                            current.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha1000 / 1000f));
                        }
                        current.fill(new Rectangle(
                                Math.round(left1000 / 1000f),
                                Math.round(top1000 / 1000f),
                                Math.round((right1000 - left1000) / 1000f),
                                Math.round((bottom1000 - top1000) / 1000f)
                        ));
                        current.setComposite(previousComposite);
                        current.setPaint(previousPaint);
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
                        if (offset + 6 > recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        int x1000 = commands[offset++];
                        int baseline1000 = commands[offset++];
                        int fontSize1000 = commands[offset++];
                        int argb = commands[offset++];
                        String fontFamily = readUtf16String(commands, recordEnd, offset, 256);
                        if (fontFamily == null) return false;
                        offset += 1 + fontFamily.length();
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
                        current.setFont(deriveCommandFont(previousFont, fontFamily, java.awt.Font.PLAIN, fontSize1000));
                        current.drawString(text.toString(), x1000 / 1000f, baseline1000 / 1000f);
                        current.setFont(previousFont);
                    } else if (op == COMMAND_DRAW_PARAGRAPH_UTF16) {
                        if (offset + 7 > recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        int x1000 = commands[offset++];
                        int y1000 = commands[offset++];
                        int width1000 = commands[offset++];
                        int fontSize1000 = commands[offset++];
                        int argb = commands[offset++];
                        int fontWeight = commands[offset++];
                        int fontWidth = commands[offset++];
                        int fontSlant = commands[offset++];
                        String fontFamily = readUtf16String(commands, recordEnd, offset, 256);
                        if (fontFamily == null) return false;
                        offset += 1 + fontFamily.length();
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
                        int fontStyle = (fontWeight >= 600 ? java.awt.Font.BOLD : java.awt.Font.PLAIN)
                                | (fontSlant == 0 ? java.awt.Font.PLAIN : java.awt.Font.ITALIC);
                        current.setFont(deriveCommandFont(previousFont, fontFamily, fontStyle, fontSize1000));
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

        private static int clearImageCacheForContext(long contextId) {
            int cleared = 0;
            synchronized (IMAGE_CACHE) {
                Iterator<ImageCacheKey> iterator = IMAGE_CACHE.keySet().iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().contextId() == contextId) {
                        iterator.remove();
                        cleared++;
                    }
                }
            }
            return cleared;
        }

        private static String readUtf16String(int[] commands, int recordEnd, int offset, int maxChars) {
            if (offset >= recordEnd) return null;
            int charCount = commands[offset++];
            if (charCount < 0 || charCount > maxChars || offset + charCount > recordEnd) {
                return null;
            }
            StringBuilder value = new StringBuilder(charCount);
            for (int index = 0; index < charCount; index++) {
                int codeUnit = commands[offset++];
                if (codeUnit < Character.MIN_VALUE || codeUnit > Character.MAX_VALUE) {
                    return null;
                }
                value.append((char) codeUnit);
            }
            return value.toString();
        }

        private static java.awt.Font deriveCommandFont(
                java.awt.Font previousFont,
                String fontFamily,
                int fontStyle,
                int fontSize1000
        ) {
            String family = fontFamily.isEmpty() ? previousFont.getFamily() : fontFamily;
            return new java.awt.Font(family, fontStyle, Math.max(1, Math.round(fontSize1000 / 1000f)))
                    .deriveFont(fontSize1000 / 1000f);
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

        private static MultipleGradientPaint.CycleMethod gradientCycleMethod(int tileMode) {
            if (tileMode == 1) return MultipleGradientPaint.CycleMethod.REPEAT;
            if (tileMode == 2) return MultipleGradientPaint.CycleMethod.REFLECT;
            return MultipleGradientPaint.CycleMethod.NO_CYCLE;
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

    private static final class SweepGradientPaint implements Paint {
        private final float centerX;
        private final float centerY;
        private final float[] stops;
        private final Color[] colors;

        private SweepGradientPaint(float centerX, float centerY, float[] stops, Color[] colors) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.stops = stops.clone();
            this.colors = colors.clone();
        }

        @Override
        public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, java.awt.geom.Rectangle2D userBounds,
                                          AffineTransform xform, RenderingHints hints) {
            Point2D center = xform.transform(new Point2D.Float(centerX, centerY), null);
            return new SweepGradientPaintContext(center.getX(), center.getY(), stops, colors);
        }

        @Override
        public int getTransparency() {
            for (Color color : colors) {
                if (color.getAlpha() != 255) {
                    return Transparency.TRANSLUCENT;
                }
            }
            return Transparency.OPAQUE;
        }
    }

    private static final class SweepGradientPaintContext implements PaintContext {
        private final double centerX;
        private final double centerY;
        private final float[] stops;
        private final int[] argb;
        private final ColorModel colorModel = ColorModel.getRGBdefault();

        private SweepGradientPaintContext(double centerX, double centerY, float[] stops, Color[] colors) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.stops = stops.clone();
            this.argb = new int[colors.length];
            for (int i = 0; i < colors.length; i++) {
                argb[i] = colors[i].getRGB();
            }
        }

        @Override
        public void dispose() {
        }

        @Override
        public ColorModel getColorModel() {
            return colorModel;
        }

        @Override
        public java.awt.image.Raster getRaster(int x, int y, int w, int h) {
            WritableRaster raster = colorModel.createCompatibleWritableRaster(w, h);
            int[] data = new int[w * h * 4];
            int index = 0;
            for (int row = 0; row < h; row++) {
                for (int col = 0; col < w; col++) {
                    int color = colorAt(x + col + 0.5, y + row + 0.5);
                    data[index++] = (color >> 16) & 0xff;
                    data[index++] = (color >> 8) & 0xff;
                    data[index++] = color & 0xff;
                    data[index++] = (color >>> 24) & 0xff;
                }
            }
            raster.setPixels(0, 0, w, h, data);
            return raster;
        }

        private int colorAt(double x, double y) {
            double angle = Math.atan2(y - centerY, x - centerX);
            float position = (float) ((angle < 0 ? angle + Math.PI * 2 : angle) / (Math.PI * 2));
            if (position <= stops[0]) {
                return argb[0];
            }
            for (int i = 1; i < stops.length; i++) {
                if (position <= stops[i]) {
                    float t = (position - stops[i - 1]) / Math.max(0.000001f, stops[i] - stops[i - 1]);
                    return interpolate(argb[i - 1], argb[i], t);
                }
            }
            return argb[argb.length - 1];
        }

        private static int interpolate(int start, int end, float t) {
            int a = lerp((start >>> 24) & 0xff, (end >>> 24) & 0xff, t);
            int r = lerp((start >> 16) & 0xff, (end >> 16) & 0xff, t);
            int g = lerp((start >> 8) & 0xff, (end >> 8) & 0xff, t);
            int b = lerp(start & 0xff, end & 0xff, t);
            return (a << 24) | (r << 16) | (g << 8) | b;
        }

        private static int lerp(int start, int end, float t) {
            return Math.round(start + (end - start) * t);
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

        long[] metadata = new long[3];
        MTLRenderQueue rq = MTLRenderQueue.getInstance();
        rq.lock();
        try {
            rq.flushAndInvokeNow(() -> {
                metadata[0] = accelSurface.getNativeOps();
                metadata[1] = NATIVE_BRIDGE_AVAILABLE ? nativeGetContextId(metadata[0]) : 0;
                metadata[2] = accelSurface.getNativeResource(AccelSurface.TEXTURE);
            });
        } finally {
            rq.unlock();
        }
        return new MetalSurfaceMetadata(metadata[0], metadata[1], metadata[2]);
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

    private static native long nativeGetContextId(long nativeOpsPtr);

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
