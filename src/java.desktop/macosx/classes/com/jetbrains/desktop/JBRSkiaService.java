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
import java.awt.Font;
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
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@JBRApi.Service
@JBRApi.Provides("JBRSkia")
public class JBRSkiaService extends JBRSkia {
    private static final String PROPERTY = "sun.java2d.skia.interop";
    private static final String NATIVE_DIAGNOSTIC_PROPERTY = "sun.java2d.skia.interop.nativeDiagnostic";
    private static final String NATIVE_LIBRARY_PROPERTY = "sun.java2d.skia.interop.library";
    private static final String COMMAND_CAPABILITIES_MASK_PROPERTY =
            "sun.java2d.skia.interop.commandCapabilitiesMaskForTest";
    private static final String COMMAND_CAPABILITIES_HIGH_MASK_PROPERTY =
            "sun.java2d.skia.interop.commandCapabilitiesHighMaskForTest";
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
                    | COMMAND_CAP64_STROKE_RECT_RADIAL_GRADIENT
                    | COMMAND_CAP64_STROKE_ROUND_RECT_RADIAL_GRADIENT
                    | COMMAND_CAP64_STROKE_RECT_SWEEP_GRADIENT
                    | COMMAND_CAP64_STROKE_ROUND_RECT_SWEEP_GRADIENT
                    | COMMAND_CAP64_FILL_RECT_BLEND_MODE
                    | COMMAND_CAP64_FILL_RECT_COLOR_FILTER
                    | COMMAND_CAP64_STROKE_LINE_DASH_PATH_EFFECT
                    | COMMAND_CAP64_SAVE_LAYER_COLOR_FILTER
                    | COMMAND_CAP64_DRAW_IMAGE_REF_COLOR_FILTER
                    | COMMAND_CAP64_DEFINE_COLOR_FILTER_TINT
                    | COMMAND_CAP64_FILL_RECT_COLOR_FILTER_REF
                    | COMMAND_CAP64_EVICT_COLOR_FILTER_HANDLE
                    | COMMAND_CAP64_DEFINE_EFFECT_DESCRIPTOR
                    | COMMAND_CAP64_SAVE_LAYER_BLEND_MODE
                    | COMMAND_CAP64_SAVE_LAYER_BLEND_COLOR_FILTER
                    | COMMAND_CAP64_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER
                    | COMMAND_CAP64_EFFECT_DESCRIPTOR_LIGHTING_FILTER
                    | COMMAND_CAP64_SAVE_LAYER_COLOR_FILTER_REF
                    | COMMAND_CAP64_DRAW_IMAGE_REF_COLOR_FILTER_REF
                    | COMMAND_CAP64_SAVE_LAYER_BLEND_COLOR_FILTER_REF;
    private static final long COMMAND_CAPABILITIES_HIGH =
            COMMAND_CAP64_HIGH_SAVE_LAYER_IMAGE_FILTER_REF
                    | COMMAND_CAP64_HIGH_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER
                    | COMMAND_CAP64_HIGH_EFFECT_DESCRIPTOR_CHAIN_IMAGE_FILTER
                    | COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_REF
                    | COMMAND_CAP64_HIGH_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER
                    | COMMAND_CAP64_HIGH_STROKE_RECT_DASH_PATH_EFFECT
                    | COMMAND_CAP64_HIGH_STROKE_ROUND_RECT_DASH_PATH_EFFECT
                    | COMMAND_CAP64_HIGH_STROKE_PATH_DASH_PATH_EFFECT
                    | COMMAND_CAP64_HIGH_PATH_EFFECT_DESCRIPTOR_REF
                    | COMMAND_CAP64_HIGH_CONCAT_MATRIX33
                    | COMMAND_CAP64_HIGH_DRAW_SHADOW_PATH
                    | COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_COLOR_FILTER
                    | COMMAND_CAP64_HIGH_DRAW_POINTS
                    | COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_TRANSFORM
                    | COMMAND_CAP64_HIGH_DEFINE_FONT_DATA
                    | COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_COLOR
                    | COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_PERLIN_NOISE
                    | COMMAND_CAP64_HIGH_DRAW_VERTICES
                    | COMMAND_CAP64_HIGH_STROKE_PATH_LINEAR_GRADIENT
                    | COMMAND_CAP64_HIGH_STROKE_PATH_RADIAL_GRADIENT
                    | COMMAND_CAP64_HIGH_STROKE_PATH_SWEEP_GRADIENT
                    | COMMAND_CAP64_HIGH_STROKE_RECT_SHADER_REF
                    | COMMAND_CAP64_HIGH_STROKE_RECT_IMAGE_SHADER
                    | COMMAND_CAP64_HIGH_SAVE_TRANSLATE
                    | COMMAND_CAP64_HIGH_RESTORE_N
                    | COMMAND_CAP64_HIGH_SAVE_TRANSLATE_LAYER
                    | COMMAND_CAP64_HIGH_DRAW_IMAGE_REF_FULL
                    | COMMAND_CAP64_HIGH_FILL_ROUND_RECT
                    | COMMAND_CAP64_HIGH_CLEAR_DRAW_IMAGE_REF_FULL
                    | COMMAND_CAP64_HIGH_DRAW_IMAGE_REF_FULL_DRAW_ROUND_RECT
                    | COMMAND_CAP64_HIGH_DRAW_IMAGE_REF_FULL_RUN
                    | COMMAND_CAP64_HIGH_SAVE_LAYER_CLIP_RECT
                    | COMMAND_CAP64_HIGH_DRAW_IMAGE_REF_FULL_FILL_RECT
                    | COMMAND_CAP64_HIGH_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN
                    | COMMAND_CAP64_HIGH_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE
                    | COMMAND_CAP64_HIGH_DRAW_IMAGE_REF_FULL_RESTORE
                    | COMMAND_CAP64_HIGH_DRAW_IMAGE_REF_FULL_RESTORE_N
                    | COMMAND_CAP64_HIGH_DRAW_ROUND_RECT_RESTORE_N
                    | COMMAND_CAP64_HIGH_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN_RESTORE_N
                    | COMMAND_CAP64_HIGH_DRAW_IMAGE_REF_FULL_RESTORE_N_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE
                    | COMMAND_CAP64_HIGH_FILL_RECT_SAVE
                    | COMMAND_CAP64_HIGH_SAVE_FILL_RECT_SAVE
                    | COMMAND_CAP64_HIGH_SAVE_LAYER_SAVE_TRANSLATE
                    | COMMAND_CAP64_HIGH_SAVE_SAVE_LAYER_SAVE_TRANSLATE
                    | COMMAND_CAP64_HIGH_FILL_RECT_SAVE_LAYER_CLIP_RECT;
    private static final boolean NATIVE_BRIDGE_AVAILABLE = loadNativeBridge();
    private static final AtomicLong NEXT_SCOPE_ID = new AtomicLong(1);
    private static final int MAX_CACHED_IMAGES = 256;
    private static final int MAX_CACHED_COLOR_FILTERS = 1024;
    private static final int MAX_CACHED_SHADERS = 1024;
    private static final Map<ImageCacheKey, BufferedImage> IMAGE_CACHE = Collections.synchronizedMap(
            new LinkedHashMap<ImageCacheKey, BufferedImage>(MAX_CACHED_IMAGES, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<ImageCacheKey, BufferedImage> eldest) {
                    return size() > MAX_CACHED_IMAGES;
                }
            });
    private static final Map<ColorFilterCacheKey, ColorFilterDescriptor> COLOR_FILTER_CACHE = Collections.synchronizedMap(
            new LinkedHashMap<ColorFilterCacheKey, ColorFilterDescriptor>(MAX_CACHED_COLOR_FILTERS, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<ColorFilterCacheKey, ColorFilterDescriptor> eldest) {
                    return size() > MAX_CACHED_COLOR_FILTERS;
                }
            });
    private static final Map<ColorFilterCacheKey, ShaderDescriptor> SHADER_CACHE = Collections.synchronizedMap(
            new LinkedHashMap<ColorFilterCacheKey, ShaderDescriptor>(MAX_CACHED_SHADERS, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<ColorFilterCacheKey, ShaderDescriptor> eldest) {
                    return size() > MAX_CACHED_SHADERS;
                }
            });
    private static final Set<MarkerHandleKey> EFFECT_HANDLE_MARKER_CACHE =
            Collections.synchronizedSet(new HashSet<>());
    private static final Set<MarkerHandleKey> SHADER_HANDLE_MARKER_CACHE =
            Collections.synchronizedSet(new HashSet<>());

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
        return maskedCommandCapabilities();
    }

    @Override
    public long getCommandCapabilities64High() {
        return maskedCommandCapabilitiesHigh();
    }

    @Override
    public int getNativeAbiVersion() {
        if (!NATIVE_BRIDGE_AVAILABLE) {
            return NATIVE_ABI_VERSION;
        }
        try {
            return nativeGetNativeAbiVersion();
        } catch (UnsatisfiedLinkError e) {
            return -1;
        }
    }

    private static long maskedCommandCapabilitiesHigh() {
        String mask = System.getProperty(COMMAND_CAPABILITIES_HIGH_MASK_PROPERTY);
        if (mask == null || mask.isBlank()) {
            return COMMAND_CAPABILITIES_HIGH;
        }
        try {
            return COMMAND_CAPABILITIES_HIGH & Long.decode(mask);
        } catch (NumberFormatException ignored) {
            return COMMAND_CAPABILITIES_HIGH;
        }
    }

    private static long maskedCommandCapabilities() {
        String mask = System.getProperty(COMMAND_CAPABILITIES_MASK_PROPERTY);
        if (mask == null || mask.isBlank()) {
            return COMMAND_CAPABILITIES;
        }
        try {
            return COMMAND_CAPABILITIES & Long.decode(mask);
        } catch (NumberFormatException ignored) {
            return COMMAND_CAPABILITIES;
        }
    }

    @Override
    public int getNativeCommandStreamAbiId() {
        if (!NATIVE_BRIDGE_AVAILABLE) {
            return ABI_ID;
        }
        try {
            return nativeGetCommandStreamAbiId();
        } catch (UnsatisfiedLinkError e) {
            return -1;
        }
    }

    @Override
    public String getNativeBuildId() {
        if (!NATIVE_BRIDGE_AVAILABLE) {
            return BUILD_ID;
        }
        try {
            return nativeGetBuildId();
        } catch (UnsatisfiedLinkError e) {
            return "native-metadata-unavailable";
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

    public static boolean isValidCommandStreamForTesting(int[] commands) {
        int commandEnd = commandPayloadEnd(commands);
        if (commandEnd < 0) {
            return false;
        }
        int offset = COMMAND_STREAM_HEADER_SIZE;
        Set<Long> colorFilterHandles = new HashSet<>();
        Map<Long, Integer> effectDescriptorTypes = new HashMap<>();
        Set<Long> shaderHandles = new HashSet<>();
        Map<Long, Long> imageDimensions = new HashMap<>();
        while (offset < commandEnd) {
            CommandRecord record = readCommandRecord(commands, offset, commandEnd);
            if (record == null
                    || !hasExpectedRecordLength(record)
                    || !validateRecordArguments(commands, record)) {
                return false;
            }
            if (record.op() == COMMAND_CLEAR_IMAGE_CACHE) {
                imageDimensions.clear();
            } else if (record.op() == COMMAND_DEFINE_IMAGE_ARGB || record.op() == COMMAND_DEFINE_IMAGE_BITMAP) {
                long cacheKey = commandHandle(commands[record.argsStart()], commands[record.argsStart() + 1]);
                imageDimensions.put(cacheKey, imageDimensions(commands[record.argsStart() + 2], commands[record.argsStart() + 3]));
            } else if (record.op() == COMMAND_EVICT_IMAGE_CACHE_KEY) {
                imageDimensions.remove(commandHandle(commands[record.argsStart()], commands[record.argsStart() + 1]));
            } else if ((record.op() == COMMAND_DRAW_IMAGE_REF_FULL
                    || record.op() == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE
                    || record.op() == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N
                    || record.op() == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE)
                    && !imageDimensions.containsKey(commandHandle(
                    commands[record.argsStart() + 4],
                    commands[record.argsStart() + 5]))) {
                return false;
            } else if (record.op() == COMMAND_DRAW_IMAGE_REF_FULL_RUN) {
                for (int keyOffset = record.argsStart() + 5; keyOffset + 1 < record.recordEnd(); keyOffset += 6) {
                    if (!imageDimensions.containsKey(commandHandle(commands[keyOffset], commands[keyOffset + 1]))) {
                        return false;
                    }
                }
            } else if (record.op() == COMMAND_DRAW_IMAGE_REF_FULL_DRAW_ROUND_RECT
                    && !imageDimensions.containsKey(commandHandle(
                    commands[record.argsStart() + 5],
                    commands[record.argsStart() + 6]))) {
                return false;
            } else if (record.op() == COMMAND_DRAW_IMAGE_REF_FULL_FILL_RECT
                    && !imageDimensions.containsKey(commandHandle(
                    commands[record.argsStart() + 5],
                    commands[record.argsStart() + 6]))) {
                return false;
            } else if (record.op() == COMMAND_CLEAR_DRAW_IMAGE_REF_FULL
                    && !imageDimensions.containsKey(commandHandle(
                    commands[record.argsStart() + 8],
                    commands[record.argsStart() + 9]))) {
                return false;
            } else if ((record.op() == COMMAND_DRAW_IMAGE_REF
                    || record.op() == COMMAND_DRAW_IMAGE_REF_COLOR_FILTER
                    || record.op() == COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF)
                    && !hasImageDimensions(
                    imageDimensions,
                    commands[record.argsStart() + 8],
                    commands[record.argsStart() + 9],
                    commands[record.argsStart() + 10],
                    commands[record.argsStart() + 11])) {
                return false;
            } else if (record.op() == COMMAND_FILL_RECT_IMAGE_SHADER
                    && !hasImageDimensions(
                    imageDimensions,
                    commands[record.argsStart() + 4],
                    commands[record.argsStart() + 5],
                    commands[record.argsStart() + 6],
                    commands[record.argsStart() + 7])) {
                return false;
            } else if (record.op() == COMMAND_DEFINE_COLOR_FILTER_TINT || record.op() == COMMAND_DEFINE_EFFECT_DESCRIPTOR) {
                if (record.op() == COMMAND_DEFINE_EFFECT_DESCRIPTOR
                        && !validateEffectDescriptorReferences(commands, record, effectDescriptorTypes)) {
                    return false;
                }
                long handle = commandHandle(commands[record.argsStart()], commands[record.argsStart() + 1]);
                colorFilterHandles.add(handle);
                effectDescriptorTypes.put(handle, record.op() == COMMAND_DEFINE_COLOR_FILTER_TINT
                        ? COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER
                        : commands[record.argsStart() + 2]);
            } else if (record.op() == COMMAND_EVICT_COLOR_FILTER_HANDLE) {
                long handle = commandHandle(commands[record.argsStart()], commands[record.argsStart() + 1]);
                colorFilterHandles.remove(handle);
                effectDescriptorTypes.remove(handle);
            } else if (record.op() == COMMAND_DEFINE_SHADER_DESCRIPTOR) {
                if (!validateShaderDescriptorReferences(commands, record, shaderHandles, effectDescriptorTypes)) {
                    return false;
                }
                shaderHandles.add(commandHandle(commands[record.argsStart()], commands[record.argsStart() + 1]));
            } else if (record.op() == COMMAND_EVICT_SHADER_HANDLE) {
                shaderHandles.remove(commandHandle(commands[record.argsStart()], commands[record.argsStart() + 1]));
            } else if (record.op() == COMMAND_FILL_RECT_COLOR_FILTER_REF
                    && !hasColorFilterDescriptorType(
                    effectDescriptorTypes,
                    commands[record.argsStart() + 1],
                    commands[record.argsStart() + 2])) {
                return false;
            } else if (record.op() == COMMAND_SAVE_LAYER_COLOR_FILTER_REF
                    && !hasColorFilterDescriptorType(
                    effectDescriptorTypes,
                    commands[record.argsStart() + 5],
                    commands[record.argsStart() + 6])) {
                return false;
            } else if (record.op() == COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF
                    && !hasColorFilterDescriptorType(
                    effectDescriptorTypes,
                    commands[record.argsStart() + 6],
                    commands[record.argsStart() + 7])) {
                return false;
            } else if (record.op() == COMMAND_SAVE_LAYER_IMAGE_FILTER_REF
                    && !hasImageFilterDescriptorType(
                    effectDescriptorTypes,
                    commands[record.argsStart() + 5],
                    commands[record.argsStart() + 6])) {
                return false;
            } else if (record.op() == COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF
                    && !hasColorFilterDescriptorType(
                    effectDescriptorTypes,
                    commands[record.argsStart() + 14],
                    commands[record.argsStart() + 15])) {
                return false;
            } else if (record.op() == COMMAND_DRAW_PATH_PATH_EFFECT_REF
                    && !hasPathEffectDescriptorType(
                    effectDescriptorTypes,
                    commands[record.argsStart() + 6],
                    commands[record.argsStart() + 7])) {
                return false;
            } else if ((record.op() == COMMAND_FILL_RECT_SHADER_REF || record.op() == COMMAND_STROKE_RECT_SHADER_REF)
                    && !shaderHandles.contains(commandHandle(commands[record.argsStart()], commands[record.argsStart() + 1]))) {
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

    private static boolean hasImageDimensions(
            Map<Long, Long> imageDimensions,
            int cacheKeyHigh,
            int cacheKeyLow,
            int width,
            int height
    ) {
        Long dimensions = imageDimensions.get(commandHandle(cacheKeyHigh, cacheKeyLow));
        return dimensions != null && dimensions == imageDimensions(width, height);
    }

    private static long imageDimensions(int width, int height) {
        return ((long) width << 32) | (height & 0xffffffffL);
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
        if (op == COMMAND_RESTORE_N) return 4;
        if (op == COMMAND_TRANSLATE || op == COMMAND_SCALE || op == COMMAND_SAVE_TRANSLATE) return 5;
        if (op == COMMAND_SAVE_TRANSLATE_LAYER) return 10;
        if (op == COMMAND_SAVE_LAYER) return 8;
        if (op == COMMAND_SAVE_LAYER_SAVE_TRANSLATE || op == COMMAND_SAVE_SAVE_LAYER_SAVE_TRANSLATE) return 10;
        if (op == COMMAND_SAVE_LAYER_COLOR_FILTER) return 10;
        if (op == COMMAND_SAVE_LAYER_BLEND_MODE) return 9;
        if (op == COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER) return 11;
        if (op == COMMAND_SAVE_LAYER_COLOR_FILTER_REF) return 10;
        if (op == COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF) return 11;
        if (op == COMMAND_SAVE_LAYER_IMAGE_FILTER_REF) return 10;
        if (op == COMMAND_DRAW_IMAGE_ARGB) return -2;
        if (op == COMMAND_DEFINE_IMAGE_ARGB) return -3;
        if (op == COMMAND_DRAW_TEXT_UTF16) return -4;
        if (op == COMMAND_DRAW_PARAGRAPH_UTF16) return -5;
        if (op == COMMAND_CLIP_PATH) return -6;
        if (op == COMMAND_DRAW_PATH) return -7;
        if (op == COMMAND_DEFINE_IMAGE_BITMAP) return 11;
        if (op == COMMAND_DRAW_IMAGE_REF_FULL) return 9;
        if (op == COMMAND_CLEAR_DRAW_IMAGE_REF_FULL) return 13;
        if (op == COMMAND_DRAW_IMAGE_REF_FULL_DRAW_ROUND_RECT) return 22;
        if (op == COMMAND_DRAW_IMAGE_REF_FULL_RUN) return -36;
        if (op == COMMAND_SAVE_LAYER_CLIP_RECT) return 13;
        if (op == COMMAND_FILL_RECT_SAVE_LAYER_CLIP_RECT) return 20;
        if (op == COMMAND_DRAW_IMAGE_REF_FULL_FILL_RECT) return 16;
        if (op == COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN) return -37;
        if (op == COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN_RESTORE_N) return -38;
        if (op == COMMAND_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE) return 12;
        if (op == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE) return 9;
        if (op == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N) return 10;
        if (op == COMMAND_DRAW_ROUND_RECT_RESTORE_N) return 16;
        if (op == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE) return 19;
        if (op == COMMAND_DRAW_IMAGE_REF) return 17;
        if (op == COMMAND_DRAW_IMAGE_REF_COLOR_FILTER) return 19;
        if (op == COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF) return 19;
        if (op == COMMAND_DRAW_ARC) return 16;
        if (op == COMMAND_DRAW_ROUND_RECT) return 15;
        if (op == COMMAND_FILL_ROUND_RECT) return 10;
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
        if (op == COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT) return -20;
        if (op == COMMAND_STROKE_RECT_SWEEP_GRADIENT) return -21;
        if (op == COMMAND_STROKE_ROUND_RECT_SWEEP_GRADIENT) return -22;
        if (op == COMMAND_FILL_RECT_BLEND_MODE) return 9;
        if (op == COMMAND_FILL_RECT_COLOR_FILTER) return 10;
        if (op == COMMAND_DEFINE_COLOR_FILTER_TINT) return 7;
        if (op == COMMAND_FILL_RECT_COLOR_FILTER_REF) return 10;
        if (op == COMMAND_EVICT_COLOR_FILTER_HANDLE) return 5;
        if (op == COMMAND_DEFINE_EFFECT_DESCRIPTOR) return -24;
        if (op == COMMAND_DEFINE_SHADER_DESCRIPTOR) return -25;
        if (op == COMMAND_EVICT_SHADER_HANDLE) return 5;
        if (op == COMMAND_FILL_RECT_SHADER_REF) return 10;
        if (op == COMMAND_STROKE_LINE_DASH_PATH_EFFECT) return -23;
        if (op == COMMAND_STROKE_RECT_DASH_PATH_EFFECT) return -23;
        if (op == COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT) return -26;
        if (op == COMMAND_STROKE_PATH_DASH_PATH_EFFECT) return -27;
        if (op == COMMAND_DRAW_PATH_PATH_EFFECT_REF) return -28;
        if (op == COMMAND_DRAW_SHADOW_PATH) return -29;
        if (op == COMMAND_DRAW_POINTS) return -30;
        if (op == COMMAND_DEFINE_FONT_DATA) return -31;
        if (op == COMMAND_DRAW_VERTICES) return -32;
        if (op == COMMAND_STROKE_PATH_LINEAR_GRADIENT) return -33;
        if (op == COMMAND_STROKE_PATH_RADIAL_GRADIENT) return -34;
        if (op == COMMAND_STROKE_PATH_SWEEP_GRADIENT) return -35;
        if (op == COMMAND_STROKE_RECT_SHADER_REF) return 14;
        if (op == COMMAND_STROKE_RECT_IMAGE_SHADER) return 18;
        if (op == COMMAND_FILL_RECT_IMAGE_SHADER) return 14;
        if (op == COMMAND_CLEAR) return 4;
        if (op == COMMAND_CLEAR_RECT) return 7;
        if (op == COMMAND_CLIP_RECT) return 8;
        if (op == COMMAND_FILL_OVAL) return 8;
        if (op == COMMAND_FILL_RECT || op == COMMAND_FILL_RECT_SAVE || op == COMMAND_SAVE_FILL_RECT_SAVE) return 9;
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
        if (expectedLength == -20 && record.op() == COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT) {
            return record.recordLength() >= 22;
        }
        if (expectedLength == -21 && record.op() == COMMAND_STROKE_RECT_SWEEP_GRADIENT) {
            return record.recordLength() >= 18;
        }
        if (expectedLength == -22 && record.op() == COMMAND_STROKE_ROUND_RECT_SWEEP_GRADIENT) {
            return record.recordLength() >= 20;
        }
        if (expectedLength == -23
                && (record.op() == COMMAND_STROKE_LINE_DASH_PATH_EFFECT
                || record.op() == COMMAND_STROKE_RECT_DASH_PATH_EFFECT)) {
            return record.recordLength() >= 16;
        }
        if (expectedLength == -24 && record.op() == COMMAND_DEFINE_EFFECT_DESCRIPTOR) {
            return record.recordLength() >= 8;
        }
        if (expectedLength == -25 && record.op() == COMMAND_DEFINE_SHADER_DESCRIPTOR) {
            return record.recordLength() >= 8;
        }
        if (expectedLength == -26 && record.op() == COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT) {
            return record.recordLength() >= 18;
        }
        if (expectedLength == -27 && record.op() == COMMAND_STROKE_PATH_DASH_PATH_EFFECT) {
            return record.recordLength() >= 14;
        }
        if (expectedLength == -28 && record.op() == COMMAND_DRAW_PATH_PATH_EFFECT_REF) {
            return record.recordLength() >= 13;
        }
        if (expectedLength == -29 && record.op() == COMMAND_DRAW_SHADOW_PATH) {
            return record.recordLength() >= 17;
        }
        if (expectedLength == -30 && record.op() == COMMAND_DRAW_POINTS) {
            return record.recordLength() >= 11;
        }
        if (expectedLength == -31 && record.op() == COMMAND_DEFINE_FONT_DATA) {
            return record.recordLength() >= 7;
        }
        if (expectedLength == -32 && record.op() == COMMAND_DRAW_VERTICES) {
            return record.recordLength() >= 16;
        }
        if (expectedLength == -33 && record.op() == COMMAND_STROKE_PATH_LINEAR_GRADIENT) {
            return record.recordLength() >= 31;
        }
        if (expectedLength == -34 && record.op() == COMMAND_STROKE_PATH_RADIAL_GRADIENT) {
            return record.recordLength() >= 30;
        }
        if (expectedLength == -35 && record.op() == COMMAND_STROKE_PATH_SWEEP_GRADIENT) {
            return record.recordLength() >= 28;
        }
        if (expectedLength == -36 && record.op() == COMMAND_DRAW_IMAGE_REF_FULL_RUN) {
            return record.recordLength() >= 16;
        }
        if (expectedLength == -37 && record.op() == COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN) {
            return record.recordLength() >= 26;
        }
        if (expectedLength == -38 && record.op() == COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN_RESTORE_N) {
            return record.recordLength() >= 27;
        }
        return expectedLength == record.recordLength();
    }

    private static boolean validateRecordArguments(int[] commands, CommandRecord record) {
        if ((record.op() == COMMAND_TRANSLATE || record.op() == COMMAND_SCALE || record.op() == COMMAND_ROTATE
                || record.op() == COMMAND_SAVE_TRANSLATE || record.op() == COMMAND_SAVE_TRANSLATE_LAYER)
                && record.recordFlags() != COMMAND_RECORD_FLAGS_NONE) {
            return false;
        }
        if (record.op() == COMMAND_RESTORE_N
                && (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || commands[record.argsStart()] <= 0)) {
            return false;
        }
        if (record.op() == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N
                && commands[record.recordEnd() - 1] <= 0) {
            return false;
        }
        if (record.op() == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE
                && commands[record.argsStart() + 6] <= 0) {
            return false;
        }
        if (record.op() == COMMAND_DRAW_IMAGE_REF_FULL_RUN) {
            int count = commands[record.argsStart()];
            return (record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    || record.recordFlags() == COMMAND_RECORD_FLAG_ANTIALIAS)
                    && count > 1
                    && record.recordLength() == 4 + count * 6;
        }
        if (record.op() == COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN
                || record.op() == COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN_RESTORE_N) {
            int imageFlags = commands[record.argsStart()];
            int strokeWidth = commands[record.argsStart() + 6];
            int strokeCap = commands[record.argsStart() + 7];
            int strokeJoin = commands[record.argsStart() + 8];
            int strokeMiter = commands[record.argsStart() + 9];
            int count = commands[record.argsStart() + 10];
            int restoreCount = record.op() == COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN_RESTORE_N
                    ? commands[record.recordEnd() - 1] : 0;
            return (record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    || record.recordFlags() == COMMAND_RECORD_FLAG_ANTIALIAS)
                    && (imageFlags == COMMAND_RECORD_FLAGS_NONE || imageFlags == COMMAND_RECORD_FLAG_ANTIALIAS)
                    && isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter)
                    && count > 1
                    && (record.op() != COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN_RESTORE_N || restoreCount > 0)
                    && record.recordLength() == 14 + count * 6
                            + (record.op() == COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN_RESTORE_N ? 1 : 0);
        }
        if (record.op() == COMMAND_CLEAR_IMAGE_CACHE
                || record.op() == COMMAND_EVICT_IMAGE_CACHE_KEY
                || record.op() == COMMAND_EVICT_COLOR_FILTER_HANDLE
                || record.op() == COMMAND_EVICT_SHADER_HANDLE) {
            return record.recordFlags() == COMMAND_RECORD_FLAGS_NONE;
        }
        if (record.op() == COMMAND_SAVE_LAYER) {
            return record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    && commands[record.recordEnd() - 1] >= 0
                    && commands[record.recordEnd() - 1] <= 1000;
        }
        if (record.op() == COMMAND_SAVE_LAYER_SAVE_TRANSLATE || record.op() == COMMAND_SAVE_SAVE_LAYER_SAVE_TRANSLATE) {
            return record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    && commands[record.argsStart() + 2] >= 0
                    && commands[record.argsStart() + 3] >= 0
                    && commands[record.argsStart() + 4] >= 0
                    && commands[record.argsStart() + 4] <= 1000;
        }
        if (record.op() == COMMAND_SAVE_LAYER_CLIP_RECT) {
            int alpha1000 = commands[record.argsStart() + 4];
            int clipOp = commands[record.recordEnd() - 1];
            return (record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    || record.recordFlags() == COMMAND_RECORD_FLAG_ANTIALIAS)
                    && alpha1000 >= 0
                    && alpha1000 <= 1000
                    && (clipOp == COMMAND_CLIP_OP_INTERSECT || clipOp == COMMAND_CLIP_OP_DIFFERENCE);
        }
        if (record.op() == COMMAND_SAVE_TRANSLATE_LAYER) {
            return record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    && commands[record.argsStart() + 4] >= 0
                    && commands[record.argsStart() + 5] >= 0
                    && commands[record.argsStart() + 6] >= 0
                    && commands[record.argsStart() + 6] <= 1000;
        }
        if (record.op() == COMMAND_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE) {
            return record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    && commands[record.argsStart() + 4] >= 0
                    && commands[record.argsStart() + 5] >= 0
                    && commands[record.argsStart() + 6] >= 0
                    && commands[record.argsStart() + 6] <= 1000;
        }
        if (record.op() == COMMAND_SAVE_LAYER_COLOR_FILTER) {
            int width = commands[record.argsStart() + 2];
            int height = commands[record.argsStart() + 3];
            int alpha1000 = commands[record.argsStart() + 4];
            int colorFilterBlendMode = commands[record.argsStart() + 6];
            return record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    && width >= 0
                    && height >= 0
                    && alpha1000 >= 0
                    && alpha1000 <= 1000
                    && colorFilterBlendMode == COMMAND_BLEND_MODE_SRC_IN;
        }
        if (record.op() == COMMAND_SAVE_LAYER_BLEND_MODE) {
            int width = commands[record.argsStart() + 2];
            int height = commands[record.argsStart() + 3];
            int alpha1000 = commands[record.argsStart() + 4];
            int blendMode = commands[record.argsStart() + 5];
            return record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    && width >= 0
                    && height >= 0
                    && alpha1000 >= 0
                    && alpha1000 <= 1000
                    && isSupportedBlendMode(blendMode);
        }
        if (record.op() == COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER) {
            int width = commands[record.argsStart() + 2];
            int height = commands[record.argsStart() + 3];
            int alpha1000 = commands[record.argsStart() + 4];
            int blendMode = commands[record.argsStart() + 5];
            int colorFilterBlendMode = commands[record.argsStart() + 7];
            return record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    && width >= 0
                    && height >= 0
                    && alpha1000 >= 0
                    && alpha1000 <= 1000
                    && isSupportedBlendMode(blendMode)
                    && colorFilterBlendMode == COMMAND_BLEND_MODE_SRC_IN;
        }
        if (record.op() == COMMAND_SAVE_LAYER_COLOR_FILTER_REF) {
            int width = commands[record.argsStart() + 2];
            int height = commands[record.argsStart() + 3];
            int alpha1000 = commands[record.argsStart() + 4];
            return record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    && width >= 0
                    && height >= 0
                    && alpha1000 >= 0
                    && alpha1000 <= 1000;
        }
        if (record.op() == COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE) return false;
            int width = commands[record.argsStart() + 2];
            int height = commands[record.argsStart() + 3];
            int alpha1000 = commands[record.argsStart() + 4];
            int blendMode = commands[record.argsStart() + 5];
            return width >= 0
                    && height >= 0
                    && alpha1000 >= 0
                    && alpha1000 <= 1000
                    && isSupportedBlendMode(blendMode);
        }
        if (record.op() == COMMAND_SAVE_LAYER_IMAGE_FILTER_REF) {
            int width = commands[record.argsStart() + 2];
            int height = commands[record.argsStart() + 3];
            int alpha1000 = commands[record.argsStart() + 4];
            return record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    && width >= 0
                    && height >= 0
                    && alpha1000 >= 0
                    && alpha1000 <= 1000;
        }
        if (record.op() == COMMAND_FILL_RECT_BLEND_MODE) {
            int blendMode = commands[record.argsStart() + 1];
            int width = commands[record.argsStart() + 4];
            int height = commands[record.argsStart() + 5];
            return isSupportedFillBlendMode(blendMode) && width >= 0 && height >= 0;
        }
        if (record.op() == COMMAND_FILL_RECT_COLOR_FILTER) {
            int colorFilterBlendMode = commands[record.argsStart() + 2];
            int width = commands[record.argsStart() + 5];
            int height = commands[record.argsStart() + 6];
            return colorFilterBlendMode == COMMAND_BLEND_MODE_SRC_IN && width >= 0 && height >= 0;
        }
        if (record.op() == COMMAND_DEFINE_COLOR_FILTER_TINT) {
            int colorFilterBlendMode = commands[record.argsStart() + 3];
            return record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    && colorFilterBlendMode == COMMAND_BLEND_MODE_SRC_IN;
        }
        if (record.op() == COMMAND_DEFINE_EFFECT_DESCRIPTOR) {
            int descriptorType = commands[record.argsStart() + 2];
            int descriptorVersion = commands[record.argsStart() + 3];
            int payloadIntCount = commands[record.argsStart() + 4];
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    || descriptorVersion != COMMAND_EFFECT_DESCRIPTOR_VERSION_1
                    || record.recordLength() != 8 + payloadIntCount) {
                return false;
            }
            if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER) {
                if (payloadIntCount != 2) return false;
                int colorFilterBlendMode = commands[record.argsStart() + 6];
                return isSupportedColorFilterBlendMode(colorFilterBlendMode);
            }
            if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER) {
                if (payloadIntCount != 20) return false;
                for (int index = 0; index < payloadIntCount; index++) {
                    if (!Float.isFinite(Float.intBitsToFloat(commands[record.argsStart() + 5 + index]))) {
                        return false;
                    }
                }
                return true;
            }
            if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_LIGHTING_FILTER) {
                return payloadIntCount == 2;
            }
            if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER
                    || descriptorType == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT) {
                int payloadOffset = record.argsStart() + 5;
                if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT) {
                    if (payloadIntCount != 5) return false;
                    payloadOffset += 2;
                } else if (payloadIntCount != 3) return false;
                float sigmaX = Float.intBitsToFloat(commands[payloadOffset]);
                float sigmaY = Float.intBitsToFloat(commands[payloadOffset + 1]);
                int tileMode = commands[payloadOffset + 2];
                return Float.isFinite(sigmaX)
                        && Float.isFinite(sigmaY)
                        && sigmaX >= 0f
                        && sigmaY >= 0f
                        && tileMode >= 0
                        && tileMode <= 3;
            }
            if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER
                    || descriptorType == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT) {
                int payloadOffset = record.argsStart() + 5;
                if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT) {
                    if (payloadIntCount != 4) return false;
                    payloadOffset += 2;
                } else if (payloadIntCount != 2) return false;
                float dx = Float.intBitsToFloat(commands[payloadOffset]);
                float dy = Float.intBitsToFloat(commands[payloadOffset + 1]);
                return Float.isFinite(dx) && Float.isFinite(dy);
            }
            if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER) {
                return validateRuntimeColorFilterDescriptorPayload(commands, record, payloadIntCount);
            }
            if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT) {
                if (payloadIntCount != 1) return false;
                float radius = Float.intBitsToFloat(commands[record.argsStart() + 5]);
                return Float.isFinite(radius) && radius >= 0f;
            }
            if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_STAMPED_PATH_EFFECT) {
                if (payloadIntCount < 5) return false;
                int payloadOffset = record.argsStart() + 5;
                float advance = Float.intBitsToFloat(commands[payloadOffset]);
                float phase = Float.intBitsToFloat(commands[payloadOffset + 1]);
                int style = commands[payloadOffset + 2];
                int fillType = commands[payloadOffset + 3];
                int pathDataLength = commands[payloadOffset + 4];
                return Float.isFinite(advance)
                        && advance > 0f
                        && Float.isFinite(phase)
                        && phase >= 0f
                        && style >= 0
                        && style <= 2
                        && (fillType == COMMAND_PATH_FILL_NON_ZERO || fillType == COMMAND_PATH_FILL_EVEN_ODD)
                        && pathDataLength >= 0
                        && pathDataLength <= 4096
                        && payloadIntCount == 5 + pathDataLength
                        && validatePathData(commands, payloadOffset + 5, record.recordEnd());
            }
            if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_CHAIN_PATH_EFFECT) {
                return payloadIntCount == 4;
            }
            return false;
        }
        if (record.op() == COMMAND_DEFINE_SHADER_DESCRIPTOR) {
            int descriptorType = commands[record.argsStart() + 2];
            int descriptorVersion = commands[record.argsStart() + 3];
            int payloadIntCount = commands[record.argsStart() + 4];
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    || descriptorVersion != COMMAND_SHADER_DESCRIPTOR_VERSION_1
                    || record.recordLength() != 8 + payloadIntCount) {
                return false;
            }
            return validateShaderDescriptorPayload(commands, record, descriptorType, payloadIntCount);
        }
        if (record.op() == COMMAND_FILL_RECT_SHADER_REF) {
            int left1000 = commands[record.argsStart() + 2];
            int top1000 = commands[record.argsStart() + 3];
            int right1000 = commands[record.argsStart() + 4];
            int bottom1000 = commands[record.argsStart() + 5];
            int alpha1000 = commands[record.argsStart() + 6];
            return (record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    || record.recordFlags() == COMMAND_RECORD_FLAG_ANTIALIAS)
                    && right1000 >= left1000
                    && bottom1000 >= top1000
                    && alpha1000 >= 0
                    && alpha1000 <= 1000;
        }
        if (record.op() == COMMAND_STROKE_RECT_SHADER_REF) {
            int left1000 = commands[record.argsStart() + 2];
            int top1000 = commands[record.argsStart() + 3];
            int right1000 = commands[record.argsStart() + 4];
            int bottom1000 = commands[record.argsStart() + 5];
            int strokeWidth1000 = commands[record.argsStart() + 6];
            int strokeCap = commands[record.argsStart() + 7];
            int strokeJoin = commands[record.argsStart() + 8];
            int strokeMiter1000 = commands[record.argsStart() + 9];
            int alpha1000 = commands[record.argsStart() + 10];
            return (record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    || record.recordFlags() == COMMAND_RECORD_FLAG_ANTIALIAS)
                    && right1000 >= left1000
                    && bottom1000 >= top1000
                    && isValidStrokeMetadata(strokeWidth1000, strokeCap, strokeJoin, strokeMiter1000)
                    && alpha1000 >= 0
                    && alpha1000 <= 1000;
        }
        if (record.op() == COMMAND_FILL_RECT_COLOR_FILTER_REF) {
            int width = commands[record.argsStart() + 5];
            int height = commands[record.argsStart() + 6];
            return width >= 0 && height >= 0;
        }
        if (record.op() == COMMAND_DRAW_POINTS) {
            int strokeWidth = commands[record.argsStart() + 1];
            int strokeCap = commands[record.argsStart() + 2];
            int strokeJoin = commands[record.argsStart() + 3];
            int strokeMiter = commands[record.argsStart() + 4];
            int pointCount = commands[record.argsStart() + 5];
            return (record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    || record.recordFlags() == COMMAND_RECORD_FLAG_ANTIALIAS)
                    && pointCount >= 1
                    && pointCount <= 4096
                    && record.recordLength() == 9 + pointCount * 2
                    && isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter);
        }
        if (record.op() == COMMAND_DRAW_VERTICES) {
            int vertexMode = commands[record.argsStart()];
            int blendMode = commands[record.argsStart() + 1];
            int vertexCount = commands[record.argsStart() + 3];
            int indexCount = commands[record.argsStart() + 4];
            return (record.recordFlags() == COMMAND_RECORD_FLAGS_NONE
                    || record.recordFlags() == COMMAND_RECORD_FLAG_ANTIALIAS)
                    && vertexMode >= 0
                    && vertexMode <= 2
                    && isSupportedBlendMode(blendMode)
                    && vertexCount >= 3
                    && vertexCount <= 4096
                    && indexCount >= 0
                    && indexCount <= 8192
                    && record.recordLength() == 8 + vertexCount * 5 + indexCount;
        }
        if (record.op() == COMMAND_STROKE_LINE_DASH_PATH_EFFECT
                || record.op() == COMMAND_STROKE_RECT_DASH_PATH_EFFECT) {
            int intervalCount = commands[record.argsStart() + 10];
            if (intervalCount < 2 || intervalCount > 16 || record.recordLength() != 14 + intervalCount) {
                return false;
            }
            if (record.op() == COMMAND_STROKE_RECT_DASH_PATH_EFFECT) {
                int width = commands[record.argsStart() + 3];
                int height = commands[record.argsStart() + 4];
                if (width < 0 || height < 0) {
                    return false;
                }
            }
            int strokeWidth = commands[record.argsStart() + 5];
            int phase = commands[record.argsStart() + 9];
            if (strokeWidth <= 0 || phase < 0) {
                return false;
            }
            for (int index = 0; index < intervalCount; index++) {
                if (commands[record.argsStart() + 11 + index] <= 0) {
                    return false;
                }
            }
            return true;
        }
        if (record.op() == COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT) {
            int intervalCount = commands[record.argsStart() + 12];
            if (intervalCount < 2 || intervalCount > 16 || record.recordLength() != 16 + intervalCount) {
                return false;
            }
            int left1000 = commands[record.argsStart() + 1];
            int top1000 = commands[record.argsStart() + 2];
            int right1000 = commands[record.argsStart() + 3];
            int bottom1000 = commands[record.argsStart() + 4];
            int radiusX1000 = commands[record.argsStart() + 5];
            int radiusY1000 = commands[record.argsStart() + 6];
            int strokeWidth = commands[record.argsStart() + 7];
            int strokeCap = commands[record.argsStart() + 8];
            int strokeJoin = commands[record.argsStart() + 9];
            int strokeMiter = commands[record.argsStart() + 10];
            int phase = commands[record.argsStart() + 11];
            if (right1000 < left1000 || bottom1000 < top1000 || radiusX1000 < 0 || radiusY1000 < 0
                    || !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter) || phase < 0) {
                return false;
            }
            for (int index = 0; index < intervalCount; index++) {
                if (commands[record.argsStart() + 13 + index] <= 0) {
                    return false;
                }
            }
            return true;
        }
        if (record.op() == COMMAND_STROKE_PATH_DASH_PATH_EFFECT) {
            int intervalCount = commands[record.argsStart() + 6];
            int pathHeaderOffset = record.argsStart() + 7 + intervalCount;
            if (intervalCount < 2 || intervalCount > 16 || pathHeaderOffset + 2 > record.recordEnd()) {
                return false;
            }
            int strokeWidth = commands[record.argsStart() + 1];
            int strokeCap = commands[record.argsStart() + 2];
            int strokeJoin = commands[record.argsStart() + 3];
            int strokeMiter = commands[record.argsStart() + 4];
            int phase = commands[record.argsStart() + 5];
            int fillType = commands[pathHeaderOffset];
            int pathDataLength = commands[pathHeaderOffset + 1];
            if (!isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter)
                    || phase < 0
                    || (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
                    || pathDataLength < 0
                    || pathDataLength > 4096
                    || pathHeaderOffset + 2 + pathDataLength != record.recordEnd()) {
                return false;
            }
            for (int index = 0; index < intervalCount; index++) {
                if (commands[record.argsStart() + 7 + index] <= 0) {
                    return false;
                }
            }
            return validatePathData(commands, pathHeaderOffset + 2, record.recordEnd());
        }
        if (record.op() == COMMAND_DRAW_PATH_PATH_EFFECT_REF) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int paintStyle = commands[record.argsStart()];
            int strokeWidth = commands[record.argsStart() + 2];
            int strokeCap = commands[record.argsStart() + 3];
            int strokeJoin = commands[record.argsStart() + 4];
            int strokeMiter = commands[record.argsStart() + 5];
            int fillType = commands[record.argsStart() + 8];
            int pathDataLength = commands[record.argsStart() + 9];
            return (paintStyle == COMMAND_PAINT_STYLE_FILL || paintStyle == COMMAND_PAINT_STYLE_STROKE)
                    && (paintStyle == COMMAND_PAINT_STYLE_FILL || isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter))
                    && (fillType == COMMAND_PATH_FILL_NON_ZERO || fillType == COMMAND_PATH_FILL_EVEN_ODD)
                    && pathDataLength >= 0
                    && pathDataLength <= 4096
                    && record.argsStart() + 10 + pathDataLength == record.recordEnd()
                    && validatePathData(commands, record.argsStart() + 10, record.recordEnd());
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
        if (record.op() == COMMAND_DRAW_SHADOW_PATH) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            float zPlaneX = Float.intBitsToFloat(commands[record.argsStart() + 2]);
            float zPlaneY = Float.intBitsToFloat(commands[record.argsStart() + 3]);
            float zPlaneZ = Float.intBitsToFloat(commands[record.argsStart() + 4]);
            float lightPosX = Float.intBitsToFloat(commands[record.argsStart() + 5]);
            float lightPosY = Float.intBitsToFloat(commands[record.argsStart() + 6]);
            float lightPosZ = Float.intBitsToFloat(commands[record.argsStart() + 7]);
            float lightRadius = Float.intBitsToFloat(commands[record.argsStart() + 8]);
            int shadowFlags = commands[record.argsStart() + 9];
            int fillType = commands[record.argsStart() + 10];
            int pathDataLength = commands[record.argsStart() + 11];
            return Float.isFinite(zPlaneX)
                    && Float.isFinite(zPlaneY)
                    && Float.isFinite(zPlaneZ)
                    && Float.isFinite(lightPosX)
                    && Float.isFinite(lightPosY)
                    && Float.isFinite(lightPosZ)
                    && Float.isFinite(lightRadius)
                    && lightRadius >= 0f
                    && (shadowFlags & ~0x3) == 0
                    && (fillType == COMMAND_PATH_FILL_NON_ZERO || fillType == COMMAND_PATH_FILL_EVEN_ODD)
                    && pathDataLength >= 0
                    && pathDataLength <= 4096
                    && record.argsStart() + 12 + pathDataLength == record.recordEnd()
                    && validatePathData(commands, record.argsStart() + 12, record.recordEnd());
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
        if (record.op() == COMMAND_DRAW_ROUND_RECT || record.op() == COMMAND_DRAW_ROUND_RECT_RESTORE_N) {
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
                    && (paintStyle == COMMAND_PAINT_STYLE_FILL || isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter))
                    && (record.op() == COMMAND_DRAW_ROUND_RECT || commands[record.argsStart() + 12] > 0);
        }
        if (record.op() == COMMAND_FILL_ROUND_RECT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int left1000 = commands[record.argsStart() + 1];
            int top1000 = commands[record.argsStart() + 2];
            int right1000 = commands[record.argsStart() + 3];
            int bottom1000 = commands[record.argsStart() + 4];
            int radiusX1000 = commands[record.argsStart() + 5];
            int radiusY1000 = commands[record.argsStart() + 6];
            return right1000 >= left1000
                    && bottom1000 >= top1000
                    && radiusX1000 >= 0
                    && radiusY1000 >= 0;
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
        if (record.op() == COMMAND_STROKE_RECT_IMAGE_SHADER) {
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
            int strokeWidth1000 = commands[record.argsStart() + 11];
            int strokeCap = commands[record.argsStart() + 12];
            int strokeJoin = commands[record.argsStart() + 13];
            int strokeMiter1000 = commands[record.argsStart() + 14];
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
                    && alpha1000 <= 1000
                    && isValidStrokeMetadata(strokeWidth1000, strokeCap, strokeJoin, strokeMiter1000);
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
        if (record.op() == COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT) {
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
            int radius1000 = commands[record.argsStart() + 12];
            int tileMode = commands[record.argsStart() + 13];
            int colorCount = commands[record.argsStart() + 14];
            if (right1000 < left1000 || bottom1000 < top1000
                    || radiusX1000 < 0 || radiusY1000 < 0
                    || strokeWidth1000 <= 0 || strokeCap < 0 || strokeCap > 2
                    || strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0
                    || radius1000 <= 0
                    || tileMode < 0 || tileMode > 3
                    || colorCount < 2 || colorCount > 16
                    || record.argsStart() + 15 + colorCount * 2 != record.recordEnd()) {
                return false;
            }
            int previousStop = -1;
            for (int i = 0; i < colorCount; i++) {
                int stop1000 = commands[record.argsStart() + 16 + i * 2];
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
        if (record.op() == COMMAND_STROKE_RECT_SWEEP_GRADIENT) {
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
            int colorCount = commands[record.argsStart() + 10];
            if (right1000 < left1000 || bottom1000 < top1000
                    || strokeWidth1000 <= 0 || strokeCap < 0 || strokeCap > 2
                    || strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0
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
        if (record.op() == COMMAND_STROKE_ROUND_RECT_SWEEP_GRADIENT) {
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
            int colorCount = commands[record.argsStart() + 12];
            if (right1000 < left1000 || bottom1000 < top1000
                    || radiusX1000 < 0 || radiusY1000 < 0
                    || strokeWidth1000 <= 0 || strokeCap < 0 || strokeCap > 2
                    || strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0
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
        if (record.op() == COMMAND_STROKE_PATH_LINEAR_GRADIENT) {
            return validateStrokeGradientPath(commands, record, 6, 4, 5);
        }
        if (record.op() == COMMAND_STROKE_PATH_RADIAL_GRADIENT) {
            return validateStrokeGradientPath(commands, record, 5, 3, 4);
        }
        if (record.op() == COMMAND_STROKE_PATH_SWEEP_GRADIENT) {
            return validateStrokeGradientPath(commands, record, 3, -1, 2);
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
        if (record.op() == COMMAND_DEFINE_IMAGE_BITMAP) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE) {
                return false;
            }
            int imageWidth = commands[record.argsStart() + 2];
            int imageHeight = commands[record.argsStart() + 3];
            long bitmapPtr = commandHandle(commands[record.argsStart() + 4], commands[record.argsStart() + 5]);
            int generationId = commands[record.argsStart() + 6];
            int imageHasAlpha = commands[record.argsStart() + 7];
            return imageWidth > 0
                    && imageHeight > 0
                    && imageWidth <= 4096
                    && imageHeight <= 4096
                    && bitmapPtr != 0
                    && generationId != 0
                    && imageHasAlpha >= 0
                    && imageHasAlpha <= 1;
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
        if (record.op() == COMMAND_DRAW_IMAGE_REF_FULL
                || record.op() == COMMAND_CLEAR_DRAW_IMAGE_REF_FULL
                || record.op() == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE
                || record.op() == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N
                || record.op() == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            return true;
        }
        if (record.op() == COMMAND_DRAW_IMAGE_REF_FULL_DRAW_ROUND_RECT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int imageFlags = commands[record.argsStart()];
            if (imageFlags != COMMAND_RECORD_FLAGS_NONE && imageFlags != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int roundArgsStart = record.argsStart() + 7;
            int paintStyle = commands[roundArgsStart];
            int left1000 = commands[roundArgsStart + 2];
            int top1000 = commands[roundArgsStart + 3];
            int right1000 = commands[roundArgsStart + 4];
            int bottom1000 = commands[roundArgsStart + 5];
            int radiusX1000 = commands[roundArgsStart + 6];
            int radiusY1000 = commands[roundArgsStart + 7];
            int strokeWidth = commands[roundArgsStart + 8];
            int strokeCap = commands[roundArgsStart + 9];
            int strokeJoin = commands[roundArgsStart + 10];
            int strokeMiter = commands[roundArgsStart + 11];
            return (paintStyle == COMMAND_PAINT_STYLE_FILL || paintStyle == COMMAND_PAINT_STYLE_STROKE)
                    && right1000 >= left1000
                    && bottom1000 >= top1000
                    && radiusX1000 >= 0
                    && radiusY1000 >= 0
                    && (paintStyle == COMMAND_PAINT_STYLE_FILL || isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter));
        }
        if (record.op() == COMMAND_DRAW_IMAGE_REF_FULL_FILL_RECT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int imageFlags = commands[record.argsStart()];
            if (imageFlags != COMMAND_RECORD_FLAGS_NONE && imageFlags != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int width = commands[record.argsStart() + 10];
            int height = commands[record.argsStart() + 11];
            int radius = commands[record.argsStart() + 12];
            return width >= 0 && height >= 0 && radius >= 0;
        }
        if (record.op() == COMMAND_FILL_RECT_SAVE_LAYER_CLIP_RECT) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int fillWidth = commands[record.argsStart() + 3];
            int fillHeight = commands[record.argsStart() + 4];
            int radius = commands[record.argsStart() + 5];
            int clipFlags = commands[record.argsStart() + 6];
            int layerWidth = commands[record.argsStart() + 9];
            int layerHeight = commands[record.argsStart() + 10];
            int alpha1000 = commands[record.argsStart() + 11];
            int clipWidth = commands[record.argsStart() + 14];
            int clipHeight = commands[record.argsStart() + 15];
            int clipOp = commands[record.argsStart() + 16];
            return fillWidth >= 0
                    && fillHeight >= 0
                    && radius >= 0
                    && (clipFlags == COMMAND_RECORD_FLAGS_NONE || clipFlags == COMMAND_RECORD_FLAG_ANTIALIAS)
                    && layerWidth >= 0
                    && layerHeight >= 0
                    && alpha1000 >= 0
                    && alpha1000 <= 1000
                    && clipWidth >= 0
                    && clipHeight >= 0
                    && (clipOp == COMMAND_CLIP_OP_INTERSECT || clipOp == COMMAND_CLIP_OP_DIFFERENCE);
        }
        if (record.op() == COMMAND_DRAW_IMAGE_REF_COLOR_FILTER) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int imageWidth = commands[record.argsStart() + 10];
            int imageHeight = commands[record.argsStart() + 11];
            int alpha1000 = commands[record.argsStart() + 12];
            int filterQuality = commands[record.argsStart() + 13];
            int colorFilterBlendMode = commands[record.argsStart() + 15];
            return imageWidth > 0
                    && imageHeight > 0
                    && imageWidth <= 4096
                    && imageHeight <= 4096
                    && alpha1000 >= 0
                    && alpha1000 <= 1000
                    && filterQuality >= 0
                    && filterQuality <= 3
                    && isSupportedColorFilterBlendMode(colorFilterBlendMode);
        }
        if (record.op() == COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF) {
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
        if (record.op() == COMMAND_DEFINE_FONT_DATA) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE) {
                return false;
            }
            int byteCount = commands[record.argsStart() + 2];
            if (byteCount <= 0 || byteCount > 1048576
                    || record.recordLength() != 6 + byteCount) {
                return false;
            }
            for (int index = 0; index < byteCount; index++) {
                int value = commands[record.argsStart() + 3 + index];
                if (value < 0 || value > 255) {
                    return false;
                }
            }
            return true;
        }
        if (record.op() == COMMAND_DRAW_TEXT_UTF16) {
            if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                    && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                return false;
            }
            int fontSize1000 = commands[record.argsStart() + 2];
            int fontWeight = commands[record.argsStart() + 4];
            int fontWidth = commands[record.argsStart() + 5];
            int fontSlant = commands[record.argsStart() + 6];
            int fontFamilyCount = commands[record.argsStart() + 7];
            if (fontFamilyCount < 0 || fontFamilyCount > 256
                    || record.argsStart() + 8 + fontFamilyCount >= record.recordEnd()) {
                return false;
            }
            int charCount = commands[record.argsStart() + 8 + fontFamilyCount];
            return fontSize1000 > 0
                    && fontWeight >= 1
                    && fontWeight <= 1000
                    && fontWidth >= 1
                    && fontWidth <= 9
                    && fontSlant >= 0
                    && fontSlant <= 2
                    && charCount >= 0
                    && charCount <= 4096
                    && record.recordLength() == 12 + fontFamilyCount + charCount;
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

    private static boolean validateStrokeGradientPath(
            int[] commands,
            CommandRecord record,
            int gradientHeaderLength,
            int tileModeOffset,
            int colorCountOffset) {
        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
            return false;
        }
        int strokeWidth1000 = commands[record.argsStart()];
        int strokeCap = commands[record.argsStart() + 1];
        int strokeJoin = commands[record.argsStart() + 2];
        int strokeMiter1000 = commands[record.argsStart() + 3];
        int fillType = commands[record.argsStart() + 4];
        int pathDataLength = commands[record.argsStart() + 5];
        int gradientStart = record.argsStart() + 6 + pathDataLength;
        if (!isValidStrokeMetadata(strokeWidth1000, strokeCap, strokeJoin, strokeMiter1000)
                || (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
                || pathDataLength < 0
                || pathDataLength > 4096
                || gradientStart + gradientHeaderLength > record.recordEnd()
                || !validatePathData(commands, record.argsStart() + 6, gradientStart)) {
            return false;
        }
        if (tileModeOffset >= 0) {
            int tileMode = commands[gradientStart + tileModeOffset];
            if (tileMode < 0 || tileMode > 3) {
                return false;
            }
        }
        if (record.op() == COMMAND_STROKE_PATH_RADIAL_GRADIENT && commands[gradientStart + 2] <= 0) {
            return false;
        }
        int colorCount = commands[gradientStart + colorCountOffset];
        if (colorCount < 2 || colorCount > 16
                || gradientStart + gradientHeaderLength + colorCount * 2 != record.recordEnd()) {
            return false;
        }
        int previousStop = -1;
        int stopOffset = gradientStart + gradientHeaderLength + 1;
        for (int i = 0; i < colorCount; i++) {
            int stop1000 = commands[stopOffset + i * 2];
            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                return false;
            }
            previousStop = stop1000;
        }
        return true;
    }

    private static boolean isValidStrokeMetadata(int strokeWidth, int strokeCap, int strokeJoin, int strokeMiter1000) {
        return strokeWidth >= 1
                && strokeCap >= 0
                && strokeCap <= 2
                && strokeJoin >= 0
                && strokeJoin <= 2
                && strokeMiter1000 >= 0;
    }

    private static boolean isSupportedFillBlendMode(int blendMode) {
        return blendMode == COMMAND_BLEND_MODE_PLUS
                || blendMode == COMMAND_BLEND_MODE_MULTIPLY
                || blendMode == COMMAND_BLEND_MODE_SCREEN
                || blendMode == COMMAND_BLEND_MODE_OVERLAY
                || blendMode == COMMAND_BLEND_MODE_DARKEN
                || blendMode == COMMAND_BLEND_MODE_LIGHTEN
                || blendMode == COMMAND_BLEND_MODE_DIFFERENCE
                || blendMode == COMMAND_BLEND_MODE_EXCLUSION
                || blendMode == COMMAND_BLEND_MODE_COLOR_DODGE
                || blendMode == COMMAND_BLEND_MODE_COLOR_BURN
                || blendMode == COMMAND_BLEND_MODE_HARDLIGHT
                || blendMode == COMMAND_BLEND_MODE_SOFTLIGHT
                || blendMode == COMMAND_BLEND_MODE_HUE
                || blendMode == COMMAND_BLEND_MODE_SATURATION
                || blendMode == COMMAND_BLEND_MODE_COLOR
                || blendMode == COMMAND_BLEND_MODE_LUMINOSITY;
    }

    private static boolean isSupportedBlendMode(int blendMode) {
        return blendMode == COMMAND_BLEND_MODE_SRC_OVER || isSupportedFillBlendMode(blendMode);
    }

    private static boolean isSupportedColorFilterBlendMode(int blendMode) {
        return blendMode == COMMAND_BLEND_MODE_SRC_IN || isSupportedBlendMode(blendMode);
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

    private static long commandHandle(int high, int low) {
        return ((long) high << 32) ^ (low & 0xffffffffL);
    }

    private static boolean validateShaderDescriptorReferences(
            int[] commands,
            CommandRecord record,
            Set<Long> shaderHandles,
            Map<Long, Integer> effectDescriptorTypes
    ) {
        int descriptorType = commands[record.argsStart() + 2];
        int payloadIntCount = commands[record.argsStart() + 4];
        int payloadStart = record.argsStart() + 5;
        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_COMPOSITE && payloadIntCount == 5) {
            long dstHandle = commandHandle(commands[payloadStart], commands[payloadStart + 1]);
            long srcHandle = commandHandle(commands[payloadStart + 2], commands[payloadStart + 3]);
            return shaderHandles.contains(dstHandle) && shaderHandles.contains(srcHandle);
        }
        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_COLOR_FILTER && payloadIntCount == 4) {
            long shaderHandle = commandHandle(commands[payloadStart], commands[payloadStart + 1]);
            long colorFilterHandle = commandHandle(commands[payloadStart + 2], commands[payloadStart + 3]);
            return shaderHandles.contains(shaderHandle)
                    && isColorFilterDescriptorType(effectDescriptorTypes.get(colorFilterHandle));
        }
        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_TRANSFORM && payloadIntCount == 11) {
            long childHandle = commandHandle(commands[payloadStart], commands[payloadStart + 1]);
            return shaderHandles.contains(childHandle);
        }
        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_RUNTIME_EFFECT && payloadIntCount >= 7) {
            int childCount = commands[payloadStart + 2];
            if (childCount < 0 || childCount > 8 || payloadIntCount < 7 + childCount * 2) {
                return false;
            }
            for (int index = 0; index < childCount; index++) {
                long childHandle = commandHandle(
                        commands[payloadStart + 7 + index * 2],
                        commands[payloadStart + 8 + index * 2]
                );
                if (!shaderHandles.contains(childHandle)) return false;
            }
        }
        return true;
    }

    private static boolean validateEffectDescriptorReferences(
            int[] commands,
            CommandRecord record,
            Map<Long, Integer> effectDescriptorTypes
    ) {
        int descriptorType = commands[record.argsStart() + 2];
        int payloadIntCount = commands[record.argsStart() + 4];
        int payloadStart = record.argsStart() + 5;
        if ((descriptorType == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT
                || descriptorType == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT)
                && payloadIntCount >= 2) {
            long childHandle = commandHandle(commands[payloadStart], commands[payloadStart + 1]);
            return isImageFilterDescriptorType(effectDescriptorTypes.get(childHandle));
        }
        if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER && payloadIntCount >= 7) {
            int childCount = commands[payloadStart + 2];
            if (childCount < 0 || childCount > 8 || payloadIntCount < 7 + childCount * 2) {
                return false;
            }
            for (int index = 0; index < childCount; index++) {
                long childHandle = commandHandle(
                        commands[payloadStart + 7 + index * 2],
                        commands[payloadStart + 8 + index * 2]
                );
                Integer childType = effectDescriptorTypes.get(childHandle);
                if (!isColorFilterDescriptorType(childType)) return false;
            }
        }
        if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_CHAIN_PATH_EFFECT && payloadIntCount == 4) {
            long outerHandle = commandHandle(commands[payloadStart], commands[payloadStart + 1]);
            long innerHandle = commandHandle(commands[payloadStart + 2], commands[payloadStart + 3]);
            return isPathEffectDescriptorType(effectDescriptorTypes.get(outerHandle))
                    && isPathEffectDescriptorType(effectDescriptorTypes.get(innerHandle));
        }
        return true;
    }

    private static boolean hasColorFilterDescriptorType(
            Map<Long, Integer> effectDescriptorTypes,
            int handleHigh,
            int handleLow
    ) {
        return isColorFilterDescriptorType(effectDescriptorTypes.get(commandHandle(handleHigh, handleLow)));
    }

    private static boolean hasImageFilterDescriptorType(
            Map<Long, Integer> effectDescriptorTypes,
            int handleHigh,
            int handleLow
    ) {
        return isImageFilterDescriptorType(effectDescriptorTypes.get(commandHandle(handleHigh, handleLow)));
    }

    private static boolean hasPathEffectDescriptorType(
            Map<Long, Integer> effectDescriptorTypes,
            int handleHigh,
            int handleLow
    ) {
        return isPathEffectDescriptorType(effectDescriptorTypes.get(commandHandle(handleHigh, handleLow)));
    }

    private static boolean isImageFilterDescriptorType(Integer descriptorType) {
        return descriptorType != null
                && (descriptorType == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER
                || descriptorType == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER
                || descriptorType == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT
                || descriptorType == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT);
    }

    private static boolean isColorFilterDescriptorType(Integer descriptorType) {
        return descriptorType != null
                && (descriptorType == COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER
                || descriptorType == COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER
                || descriptorType == COMMAND_EFFECT_DESCRIPTOR_LIGHTING_FILTER
                || descriptorType == COMMAND_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER);
    }

    private static boolean isPathEffectDescriptorType(Integer descriptorType) {
        return descriptorType != null
                && (descriptorType == COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT
                || descriptorType == COMMAND_EFFECT_DESCRIPTOR_STAMPED_PATH_EFFECT
                || descriptorType == COMMAND_EFFECT_DESCRIPTOR_CHAIN_PATH_EFFECT);
    }

    private static boolean validateShaderDescriptorPayload(
            int[] commands,
            CommandRecord record,
            int descriptorType,
            int payloadIntCount
    ) {
        int payloadStart = record.argsStart() + 5;
        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_LINEAR_GRADIENT) {
            if (payloadIntCount < 6) return false;
            int tileMode = commands[payloadStart + 4];
            int colorCount = commands[payloadStart + 5];
            return tileMode >= 0 && tileMode <= 3
                    && validateGradientStops(commands, payloadStart + 6, record.recordEnd(), colorCount);
        }
        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_RADIAL_GRADIENT) {
            if (payloadIntCount < 5) return false;
            int radius1000 = commands[payloadStart + 2];
            int tileMode = commands[payloadStart + 3];
            int colorCount = commands[payloadStart + 4];
            return radius1000 > 0
                    && tileMode >= 0
                    && tileMode <= 3
                    && validateGradientStops(commands, payloadStart + 5, record.recordEnd(), colorCount);
        }
        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_SWEEP_GRADIENT) {
            if (payloadIntCount < 3) return false;
            int colorCount = commands[payloadStart + 2];
            return validateGradientStops(commands, payloadStart + 3, record.recordEnd(), colorCount);
        }
        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_IMAGE) {
            if (payloadIntCount != 6) return false;
            int imageWidth = commands[payloadStart + 2];
            int imageHeight = commands[payloadStart + 3];
            int tileModeX = commands[payloadStart + 4];
            int tileModeY = commands[payloadStart + 5];
            return imageWidth > 0
                    && imageHeight > 0
                    && imageWidth <= 4096
                    && imageHeight <= 4096
                    && tileModeX >= 0
                    && tileModeX <= 3
                    && tileModeY >= 0
                    && tileModeY <= 3;
        }
        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_COLOR) {
            return payloadIntCount == 1;
        }
        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_PERLIN_NOISE) {
            if (payloadIntCount != 7) return false;
            int kind = commands[payloadStart];
            int baseFrequencyX1000000 = commands[payloadStart + 1];
            int baseFrequencyY1000000 = commands[payloadStart + 2];
            int numOctaves = commands[payloadStart + 3];
            int tileWidth = commands[payloadStart + 5];
            int tileHeight = commands[payloadStart + 6];
            return kind >= 0
                    && kind <= 1
                    && baseFrequencyX1000000 > 0
                    && baseFrequencyY1000000 > 0
                    && numOctaves >= 1
                    && numOctaves <= 16
                    && tileWidth >= 0
                    && tileHeight >= 0
                    && tileWidth <= 4096
                    && tileHeight <= 4096;
        }
        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_COMPOSITE) {
            int blendMode = commands[payloadStart + 4];
            return payloadIntCount == 5 && isSupportedBlendMode(blendMode);
        }
        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_COLOR_FILTER) {
            return payloadIntCount == 4;
        }
        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_TRANSFORM) {
            return payloadIntCount == 11;
        }
        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_RUNTIME_EFFECT) {
            if (payloadIntCount < 7) return false;
            int skslLength = commands[payloadStart];
            int uniformFloatCount = commands[payloadStart + 1];
            int childCount = commands[payloadStart + 2];
            int namedUniformCount = commands[payloadStart + 3];
            int namedChildCount = commands[payloadStart + 4];
            if (skslLength <= 0
                    || skslLength > 4096
                    || uniformFloatCount < 0
                    || uniformFloatCount > 256
                    || childCount < 0
                    || childCount > 8
                    || namedUniformCount < 0
                    || namedUniformCount > 16
                    || namedChildCount < 0
                    || namedChildCount > childCount
                    || payloadIntCount < 7 + childCount * 2 + skslLength + uniformFloatCount) {
                return false;
            }
            int schemaEnd = record.recordEnd() - skslLength - uniformFloatCount;
            int childSchemaStart = validateRuntimeEffectUniformSchema(
                    commands,
                    payloadStart + 7 + childCount * 2,
                    schemaEnd,
                    namedUniformCount,
                    uniformFloatCount
            );
            int skslStart = validateRuntimeEffectChildSchema(commands, childSchemaStart, schemaEnd, namedChildCount, childCount);
            if (skslStart < 0 || skslStart + skslLength + uniformFloatCount != record.recordEnd()) return false;
            long expectedHash = commandHandle(commands[payloadStart + 5], commands[payloadStart + 6]);
            if (shaderSourceHash(commands, skslStart, skslLength) != expectedHash) return false;
            for (int index = 0; index < skslLength; index++) {
                int code = commands[skslStart + index];
                if (code <= 0 || code > 127) return false;
            }
            return true;
        }
        return false;
    }

    private static boolean validateRuntimeColorFilterDescriptorPayload(
            int[] commands,
            CommandRecord record,
            int payloadIntCount
    ) {
        int payloadStart = record.argsStart() + 5;
        if (payloadIntCount < 7) return false;
        int skslLength = commands[payloadStart];
        int uniformFloatCount = commands[payloadStart + 1];
        int childCount = commands[payloadStart + 2];
        int namedUniformCount = commands[payloadStart + 3];
        int namedChildCount = commands[payloadStart + 4];
        if (skslLength <= 0
                || skslLength > 4096
                || uniformFloatCount < 0
                || uniformFloatCount > 256
                || childCount < 0
                || childCount > 8
                || namedUniformCount < 0
                || namedUniformCount > 16
                || namedChildCount < 0
                || namedChildCount > 8
                || payloadIntCount < 7 + childCount * 2 + skslLength + uniformFloatCount) {
            return false;
        }
        int schemaEnd = record.recordEnd() - skslLength - uniformFloatCount;
        int childSchemaStart = validateRuntimeEffectUniformSchema(
                commands,
                payloadStart + 7 + childCount * 2,
                schemaEnd,
                namedUniformCount,
                uniformFloatCount
        );
        int skslStart = validateRuntimeEffectChildSchema(
                commands,
                childSchemaStart,
                schemaEnd,
                namedChildCount,
                childCount
        );
        if (skslStart < 0 || skslStart + skslLength + uniformFloatCount != record.recordEnd()) return false;
        long expectedHash = commandHandle(commands[payloadStart + 5], commands[payloadStart + 6]);
        if (shaderSourceHash(commands, skslStart, skslLength) != expectedHash) return false;
        for (int index = 0; index < skslLength; index++) {
            int code = commands[skslStart + index];
            if (code <= 0 || code > 127) return false;
        }
        return true;
    }

    private static int validateRuntimeEffectUniformSchema(
            int[] commands,
            int offset,
            int schemaEnd,
            int namedUniformCount,
            int uniformFloatCount
    ) {
        for (int uniformIndex = 0; uniformIndex < namedUniformCount; uniformIndex++) {
            if (offset + 3 > schemaEnd) return -1;
            int floatOffset = commands[offset++];
            int floatCount = commands[offset++];
            int nameLength = commands[offset++];
            if (floatOffset < 0
                    || floatCount <= 0
                    || floatOffset > uniformFloatCount - floatCount
                    || nameLength <= 0
                    || nameLength > 64
                    || offset + nameLength > schemaEnd
                    || !isValidRuntimeEffectUniformName(commands, offset, nameLength)) {
                return -1;
            }
            offset += nameLength;
        }
        return offset;
    }

    private static int validateRuntimeEffectChildSchema(
            int[] commands,
            int offset,
            int schemaEnd,
            int namedChildCount,
            int childCount
    ) {
        if (offset < 0) return -1;
        boolean[] seen = new boolean[childCount];
        for (int childIndex = 0; childIndex < namedChildCount; childIndex++) {
            if (offset + 2 > schemaEnd) return -1;
            int referencedChildIndex = commands[offset++];
            int nameLength = commands[offset++];
            if (referencedChildIndex < 0
                    || referencedChildIndex >= childCount
                    || seen[referencedChildIndex]
                    || nameLength <= 0
                    || nameLength > 64
                    || offset + nameLength > schemaEnd
                    || !isValidRuntimeEffectUniformName(commands, offset, nameLength)) {
                return -1;
            }
            seen[referencedChildIndex] = true;
            offset += nameLength;
        }
        return offset == schemaEnd ? offset : -1;
    }

    private static boolean isValidRuntimeEffectUniformName(int[] commands, int offset, int length) {
        for (int index = 0; index < length; index++) {
            int code = commands[offset + index];
            boolean valid = code == '_'
                    || (code >= 'A' && code <= 'Z')
                    || (code >= 'a' && code <= 'z')
                    || (index > 0 && code >= '0' && code <= '9');
            if (!valid) return false;
        }
        return true;
    }

    private static long shaderSourceHash(int[] commands, int skslStart, int skslLength) {
        long hash = -3750763034362895579L;
        for (int index = 0; index < skslLength; index++) {
            hash ^= commands[skslStart + index] & 0xffL;
            hash *= 1099511628211L;
        }
        return hash;
    }

    private static boolean validateGradientStops(int[] commands, int payloadOffset, int recordEnd, int colorCount) {
        if (colorCount < 2 || colorCount > 16 || payloadOffset + colorCount * 2 != recordEnd) {
            return false;
        }
        int previousStop = -1;
        for (int index = 0; index < colorCount; index++) {
            int stop1000 = commands[payloadOffset + 1 + index * 2];
            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                return false;
            }
            previousStop = stop1000;
        }
        return true;
    }

    private record MetalSurfaceMetadata(long nativeOpsPtr, long contextPtr, long texturePtr) {
        private static final MetalSurfaceMetadata EMPTY = new MetalSurfaceMetadata(0, 0, 0);
    }

    private record ImageCacheKey(long contextId, long imageId) {
    }

    private record ColorFilterCacheKey(long contextId, long filterId) {
    }

    private record MarkerHandleKey(String backend, long contextId, long handleId) {
    }

    private record ShaderDescriptor(
            int type,
            int[] payload,
            ShaderDescriptor dst,
            ShaderDescriptor src,
            ShaderDescriptor child,
            ColorFilterDescriptor colorFilter,
            ShaderDescriptor[] children
    ) {
    }

    private record ColorFilterDescriptor(
            int type,
            int argb,
            int blendMode,
            int[] matrixBits,
            ColorFilterDescriptor child
    ) {
        static ColorFilterDescriptor tint(int argb, int blendMode) {
            return new ColorFilterDescriptor(COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER, argb, blendMode, null, null);
        }

        static ColorFilterDescriptor colorMatrix(int[] matrixBits) {
            return new ColorFilterDescriptor(COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER, 0, 0, matrixBits.clone(), null);
        }

        static ColorFilterDescriptor lighting(int multiplyArgb, int addArgb) {
            return new ColorFilterDescriptor(COMMAND_EFFECT_DESCRIPTOR_LIGHTING_FILTER, multiplyArgb, addArgb, null, null);
        }

        static ColorFilterDescriptor runtimeColorFilter(int[] payload) {
            return new ColorFilterDescriptor(COMMAND_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER, 0, 0, payload.clone(), null);
        }

        static ColorFilterDescriptor pathEffect(int type, int[] payload) {
            return new ColorFilterDescriptor(type, 0, 0, payload.clone(), null);
        }

        static ColorFilterDescriptor blurImageFilter(
                int type,
                int sigmaXBits,
                int sigmaYBits,
                int tileMode,
                ColorFilterDescriptor child
        ) {
            return new ColorFilterDescriptor(
                    type,
                    0,
                    0,
                    new int[] { sigmaXBits, sigmaYBits, tileMode },
                    child
            );
        }

        static ColorFilterDescriptor offsetImageFilter(
                int type,
                int dxBits,
                int dyBits,
                ColorFilterDescriptor child
        ) {
            return new ColorFilterDescriptor(
                    type,
                    0,
                    0,
                    new int[] { dxBits, dyBits },
                    child
            );
        }
    }

    private static boolean isColorFilterDescriptor(ColorFilterDescriptor descriptor) {
        return descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER
                || descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER
                || descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_LIGHTING_FILTER
                || descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER;
    }

    private static boolean isImageFilterDescriptor(ColorFilterDescriptor descriptor) {
        return descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER
                || descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER
                || descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT
                || descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT;
    }

    private static boolean isPathEffectDescriptor(ColorFilterDescriptor descriptor) {
        return descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT
                || descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_STAMPED_PATH_EFFECT
                || descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_CHAIN_PATH_EFFECT;
    }

    private static int applyColorMatrix(int argb, int[] matrixBits) {
        float r = ((argb >>> 16) & 0xff) / 255f;
        float g = ((argb >>> 8) & 0xff) / 255f;
        float b = (argb & 0xff) / 255f;
        float a = ((argb >>> 24) & 0xff) / 255f;
        int outR = colorMatrixChannel(matrixBits, 0, r, g, b, a);
        int outG = colorMatrixChannel(matrixBits, 5, r, g, b, a);
        int outB = colorMatrixChannel(matrixBits, 10, r, g, b, a);
        int outA = colorMatrixChannel(matrixBits, 15, r, g, b, a);
        return (outA << 24) | (outR << 16) | (outG << 8) | outB;
    }

    private static int colorMatrixChannel(int[] matrixBits, int offset, float r, float g, float b, float a) {
        float value = Float.intBitsToFloat(matrixBits[offset]) * r
                + Float.intBitsToFloat(matrixBits[offset + 1]) * g
                + Float.intBitsToFloat(matrixBits[offset + 2]) * b
                + Float.intBitsToFloat(matrixBits[offset + 3]) * a
                + Float.intBitsToFloat(matrixBits[offset + 4]);
        return Math.max(0, Math.min(255, Math.round(value * 255f)));
    }

    private static int applyLightingFilter(int argb, int multiplyArgb, int addArgb) {
        int outR = lightingChannel((argb >>> 16) & 0xff, (multiplyArgb >>> 16) & 0xff, (addArgb >>> 16) & 0xff);
        int outG = lightingChannel((argb >>> 8) & 0xff, (multiplyArgb >>> 8) & 0xff, (addArgb >>> 8) & 0xff);
        int outB = lightingChannel(argb & 0xff, multiplyArgb & 0xff, addArgb & 0xff);
        int outA = (argb >>> 24) & 0xff;
        return (outA << 24) | (outR << 16) | (outG << 8) | outB;
    }

    private static int lightingChannel(int source, int multiply, int add) {
        return Math.max(0, Math.min(255, (source * multiply + 127) / 255 + add));
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
                    && metalTexturePtr != 0) {
                logHandleLifecycleMarkers(commands, contextPtr, "native");
                return nativeRenderCommandFrame(nativeOpsPtr, metalTexturePtr,
                        deviceSpaceClip.x, deviceSpaceClip.y, deviceSpaceClip.width, deviceSpaceClip.height,
                        width, height, frameTimeNanos, commands);
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
                    && metalTexturePtr != 0) {
                logHandleLifecycleMarkers(decodeCommandBuffer(commands), contextPtr, "native");
                return nativeRenderCommandBufferFrame(nativeOpsPtr, metalTexturePtr,
                        deviceSpaceClip.x, deviceSpaceClip.y, deviceSpaceClip.width, deviceSpaceClip.height,
                        width, height, frameTimeNanos, commands);
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
                    && commandBuffer.isDirect()) {
                logHandleLifecycleMarkers(decodeCommandBuffer(commandBuffer), contextPtr, "native");
                return nativeRenderCommandDirectFrame(nativeOpsPtr, metalTexturePtr,
                        deviceSpaceClip.x, deviceSpaceClip.y, deviceSpaceClip.width, deviceSpaceClip.height,
                        width, height, frameTimeNanos, commandBuffer, commandByteCount);
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
            Map<Long, Font> fontDataFonts = new HashMap<>();
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
                    } else if (op == COMMAND_RESTORE_N) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 1 != recordEnd) return false;
                        int count = commands[offset++];
                        if (count <= 0 || stack.size() < count) return false;
                        for (int restoreIndex = 0; restoreIndex < count; restoreIndex++) {
                            current.dispose();
                            current = stack.removeLast();
                        }
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
                    } else if (op == COMMAND_DRAW_PATH_PATH_EFFECT_REF) {
                        if (offset + 10 > recordEnd) return false;
                        int paintStyle = commands[offset++];
                        int argb = commands[offset++];
                        int strokeWidth = commands[offset++];
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        int strokeMiter = commands[offset++];
                        long handle = cacheKey(commands[offset++], commands[offset++]);
                        int fillType = commands[offset++];
                        int pathDataLength = commands[offset++];
                        if ((paintStyle != COMMAND_PAINT_STYLE_FILL && paintStyle != COMMAND_PAINT_STYLE_STROKE)
                                || (paintStyle == COMMAND_PAINT_STYLE_STROKE && !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter))
                                || (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
                                || pathDataLength < 0
                                || offset + pathDataLength != recordEnd) {
                            return false;
                        }
                        ColorFilterDescriptor descriptor = COLOR_FILTER_CACHE.get(new ColorFilterCacheKey(contextPtr, handle));
                        if (descriptor == null || !isPathEffectDescriptor(descriptor)) return false;
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
                    } else if (op == COMMAND_STROKE_PATH_LINEAR_GRADIENT) {
                        if (offset + 6 > recordEnd) return false;
                        int strokeWidth1000 = commands[offset++];
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        int strokeMiter1000 = commands[offset++];
                        int fillType = commands[offset++];
                        int pathDataLength = commands[offset++];
                        int pathEnd = offset + pathDataLength;
                        if (!isValidStrokeMetadata(strokeWidth1000, strokeCap, strokeJoin, strokeMiter1000)
                                || (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
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
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) return false;
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
                        current.draw(path);
                    } else if (op == COMMAND_STROKE_PATH_RADIAL_GRADIENT) {
                        if (offset + 6 > recordEnd) return false;
                        int strokeWidth1000 = commands[offset++];
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        int strokeMiter1000 = commands[offset++];
                        int fillType = commands[offset++];
                        int pathDataLength = commands[offset++];
                        int pathEnd = offset + pathDataLength;
                        if (!isValidStrokeMetadata(strokeWidth1000, strokeCap, strokeJoin, strokeMiter1000)
                                || (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
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
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) return false;
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
                        current.draw(path);
                    } else if (op == COMMAND_STROKE_PATH_SWEEP_GRADIENT) {
                        if (offset + 6 > recordEnd) return false;
                        int strokeWidth1000 = commands[offset++];
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        int strokeMiter1000 = commands[offset++];
                        int fillType = commands[offset++];
                        int pathDataLength = commands[offset++];
                        int pathEnd = offset + pathDataLength;
                        if (!isValidStrokeMetadata(strokeWidth1000, strokeCap, strokeJoin, strokeMiter1000)
                                || (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
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
                            if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) return false;
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
                        current.setStroke(new BasicStroke(
                                strokeWidth1000 / 1000f,
                                strokeCap == 1 ? BasicStroke.CAP_ROUND : strokeCap == 2 ? BasicStroke.CAP_SQUARE : BasicStroke.CAP_BUTT,
                                strokeJoin == 1 ? BasicStroke.JOIN_ROUND : strokeJoin == 2 ? BasicStroke.JOIN_BEVEL : BasicStroke.JOIN_MITER,
                                Math.max(1f, strokeMiter1000 / 1000f)
                        ));
                        current.draw(path);
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
                    } else if (op == COMMAND_DRAW_ROUND_RECT || op == COMMAND_DRAW_ROUND_RECT_RESTORE_N) {
                        if (offset + (op == COMMAND_DRAW_ROUND_RECT_RESTORE_N ? 13 : 12) != recordEnd) return false;
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
                        int restoreCount = op == COMMAND_DRAW_ROUND_RECT_RESTORE_N ? commands[offset++] : 0;
                        if ((paintStyle != COMMAND_PAINT_STYLE_FILL && paintStyle != COMMAND_PAINT_STYLE_STROKE)
                                || right1000 < left1000
                                || bottom1000 < top1000
                                || radiusX1000 < 0
                                || radiusY1000 < 0
                                || (paintStyle == COMMAND_PAINT_STYLE_STROKE && !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter))
                                || restoreCount < 0
                                || (op == COMMAND_DRAW_ROUND_RECT_RESTORE_N && restoreCount == 0)) {
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
                        if (restoreCount > 0) {
                            if (stack.size() < restoreCount) {
                                return false;
                            }
                            for (int restoreIndex = 0; restoreIndex < restoreCount; restoreIndex++) {
                                current.dispose();
                                current = stack.removeLast();
                            }
                        }
                    } else if (op == COMMAND_FILL_ROUND_RECT) {
                        if (offset + 7 != recordEnd) return false;
                        int argb = commands[offset++];
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int radiusX1000 = commands[offset++];
                        int radiusY1000 = commands[offset++];
                        if (right1000 < left1000 || bottom1000 < top1000 || radiusX1000 < 0 || radiusY1000 < 0) {
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
                        current.fill(roundRect);
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
                    } else if (op == COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                                && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                            return false;
                        }
                        if (offset + 15 > recordEnd) return false;
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
                        int centerX1000 = commands[offset++];
                        int centerY1000 = commands[offset++];
                        int radius1000 = commands[offset++];
                        int tileMode = commands[offset++];
                        int colorCount = commands[offset++];
                        if (right1000 < left1000 || bottom1000 < top1000
                                || radiusX1000 < 0 || radiusY1000 < 0
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
                        current.draw(new java.awt.geom.RoundRectangle2D.Float(
                                left1000 / 1000f,
                                top1000 / 1000f,
                                (right1000 - left1000) / 1000f,
                                (bottom1000 - top1000) / 1000f,
                                radiusX1000 * 2f / 1000f,
                                radiusY1000 * 2f / 1000f
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
                    } else if (op == COMMAND_STROKE_RECT_SWEEP_GRADIENT) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                                && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                            return false;
                        }
                        if (offset + 11 > recordEnd) return false;
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
                        int colorCount = commands[offset++];
                        if (right1000 < left1000 || bottom1000 < top1000
                                || strokeWidth1000 <= 0 || strokeCap < 0 || strokeCap > 2
                                || strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0
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
                    } else if (op == COMMAND_STROKE_ROUND_RECT_SWEEP_GRADIENT) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE
                                && record.recordFlags() != COMMAND_RECORD_FLAG_ANTIALIAS) {
                            return false;
                        }
                        if (offset + 13 > recordEnd) return false;
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
                        int centerX1000 = commands[offset++];
                        int centerY1000 = commands[offset++];
                        int colorCount = commands[offset++];
                        if (right1000 < left1000 || bottom1000 < top1000
                                || radiusX1000 < 0 || radiusY1000 < 0
                                || strokeWidth1000 <= 0 || strokeCap < 0 || strokeCap > 2
                                || strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0
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
                    } else if (op == COMMAND_SAVE_TRANSLATE) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) return false;
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.translate(commands[offset++] / 1000.0, commands[offset++] / 1000.0);
                    } else if (op == COMMAND_SAVE_TRANSLATE_LAYER) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 7 != recordEnd) return false;
                        int dx1000 = commands[offset++];
                        int dy1000 = commands[offset++];
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int alpha1000 = commands[offset++];
                        if (width < 0 || height < 0 || alpha1000 < 0 || alpha1000 > 1000) return false;
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.translate(dx1000 / 1000.0, dy1000 / 1000.0);
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.clipRect(x, y, width, height);
                    } else if (op == COMMAND_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 9 != recordEnd) return false;
                        int layerDx1000 = commands[offset++];
                        int layerDy1000 = commands[offset++];
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int alpha1000 = commands[offset++];
                        int nestedDx1000 = commands[offset++];
                        int nestedDy1000 = commands[offset++];
                        if (width < 0 || height < 0 || alpha1000 < 0 || alpha1000 > 1000) return false;
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.translate(layerDx1000 / 1000.0, layerDy1000 / 1000.0);
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.clipRect(x, y, width, height);
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.translate(nestedDx1000 / 1000.0, nestedDy1000 / 1000.0);
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
                    } else if (op == COMMAND_SAVE_LAYER_SAVE_TRANSLATE || op == COMMAND_SAVE_SAVE_LAYER_SAVE_TRANSLATE) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 7 != recordEnd) return false;
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int alpha1000 = commands[offset++];
                        int dx1000 = commands[offset++];
                        int dy1000 = commands[offset++];
                        if (width < 0 || height < 0 || alpha1000 < 0 || alpha1000 > 1000) return false;
                        if (op == COMMAND_SAVE_SAVE_LAYER_SAVE_TRANSLATE) {
                            stack.addLast(current);
                            current = (Graphics2D) current.create();
                        }
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.clipRect(x, y, width, height);
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.translate(dx1000 / 1000.0, dy1000 / 1000.0);
                    } else if (op == COMMAND_SAVE_LAYER_CLIP_RECT) {
                        if (offset + 10 != recordEnd) return false;
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int alpha1000 = commands[offset++];
                        int clipX = commands[offset++];
                        int clipY = commands[offset++];
                        int clipWidth = commands[offset++];
                        int clipHeight = commands[offset++];
                        int clipOp = commands[offset++];
                        if (alpha1000 < 0 || alpha1000 > 1000) return false;
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.clipRect(x, y, width, height);
                        if (clipOp == COMMAND_CLIP_OP_INTERSECT) {
                            current.clipRect(clipX, clipY, clipWidth, clipHeight);
                        } else if (clipOp == COMMAND_CLIP_OP_DIFFERENCE) {
                            Shape previousClip = current.getClip();
                            if (previousClip == null) return false;
                            Area clip = new Area(previousClip);
                            clip.subtract(new Area(new Rectangle(clipX, clipY, clipWidth, clipHeight)));
                            current.setClip(clip);
                        } else {
                            return false;
                        }
                    } else if (op == COMMAND_SAVE_LAYER_COLOR_FILTER) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 7 != recordEnd) return false;
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int alpha1000 = commands[offset++];
                        offset++; // filter color: native Skia replay applies this on restore.
                        int colorFilterBlendMode = commands[offset++];
                        if (width < 0 || height < 0 || alpha1000 < 0 || alpha1000 > 1000
                                || colorFilterBlendMode != COMMAND_BLEND_MODE_SRC_IN) return false;
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.clipRect(x, y, width, height);
                    } else if (op == COMMAND_SAVE_LAYER_BLEND_MODE) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 6 != recordEnd) return false;
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int alpha1000 = commands[offset++];
                        int blendMode = commands[offset++];
                        if (width < 0 || height < 0 || alpha1000 < 0 || alpha1000 > 1000
                                || !isSupportedBlendMode(blendMode)) return false;
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.clipRect(x, y, width, height);
                    } else if (op == COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 8 != recordEnd) return false;
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int alpha1000 = commands[offset++];
                        int blendMode = commands[offset++];
                        offset++; // filter color: native Skia replay applies this on restore.
                        int colorFilterBlendMode = commands[offset++];
                        if (width < 0 || height < 0 || alpha1000 < 0 || alpha1000 > 1000
                                || !isSupportedBlendMode(blendMode)
                                || colorFilterBlendMode != COMMAND_BLEND_MODE_SRC_IN) return false;
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.clipRect(x, y, width, height);
                    } else if (op == COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 8 != recordEnd) return false;
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int alpha1000 = commands[offset++];
                        int blendMode = commands[offset++];
                        long handle = cacheKey(commands[offset++], commands[offset++]);
                        ColorFilterDescriptor descriptor = COLOR_FILTER_CACHE.get(new ColorFilterCacheKey(contextPtr, handle));
                        if (descriptor == null
                                || !isColorFilterDescriptor(descriptor)
                                || width < 0 || height < 0 || alpha1000 < 0 || alpha1000 > 1000
                                || !isSupportedBlendMode(blendMode)) return false;
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.clipRect(x, y, width, height);
                    } else if (op == COMMAND_SAVE_LAYER_COLOR_FILTER_REF) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 7 != recordEnd) return false;
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int alpha1000 = commands[offset++];
                        long handle = cacheKey(commands[offset++], commands[offset++]);
                        ColorFilterDescriptor descriptor = COLOR_FILTER_CACHE.get(new ColorFilterCacheKey(contextPtr, handle));
                        if (descriptor == null
                                || !isColorFilterDescriptor(descriptor)
                                || width < 0 || height < 0 || alpha1000 < 0 || alpha1000 > 1000) return false;
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.clipRect(x, y, width, height);
                    } else if (op == COMMAND_SAVE_LAYER_IMAGE_FILTER_REF) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 7 != recordEnd) return false;
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        int alpha1000 = commands[offset++];
                        long handle = cacheKey(commands[offset++], commands[offset++]);
                        ColorFilterDescriptor descriptor = COLOR_FILTER_CACHE.get(new ColorFilterCacheKey(contextPtr, handle));
                        if (descriptor == null
                                || !isImageFilterDescriptor(descriptor)
                                || width < 0 || height < 0 || alpha1000 < 0 || alpha1000 > 1000) return false;
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
                    } else if (op == COMMAND_EVICT_COLOR_FILTER_HANDLE) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) return false;
                        long handle = cacheKey(commands[offset++], commands[offset++]);
                        ColorFilterDescriptor removed = COLOR_FILTER_CACHE.remove(new ColorFilterCacheKey(contextPtr, handle));
                        logEffectHandleEvict("java2d", contextPtr, handle, removed != null);
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
                    } else if (op == COMMAND_DEFINE_IMAGE_BITMAP) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 8 != recordEnd) return false;
                        long cacheKey = cacheKey(commands[offset++], commands[offset++]);
                        int imageWidth = commands[offset++];
                        int imageHeight = commands[offset++];
                        long bitmapPtr = cacheKey(commands[offset++], commands[offset++]);
                        int generationId = commands[offset++];
                        int imageHasAlpha = commands[offset++];
                        if (imageWidth <= 0 || imageHeight <= 0 || bitmapPtr == 0 || generationId == 0
                                || imageHasAlpha < 0 || imageHasAlpha > 1) {
                            return false;
                        }
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
                    } else if (op == COMMAND_DRAW_IMAGE_REF_FULL
                            || op == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE
                            || op == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N) {
                        if (offset + (op == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N ? 7 : 6) != recordEnd) return false;
                        boolean filtered = (record.recordFlags() & COMMAND_RECORD_FLAG_ANTIALIAS) != 0;
                        int dstLeft1000 = commands[offset++];
                        int dstTop1000 = commands[offset++];
                        int dstRight1000 = commands[offset++];
                        int dstBottom1000 = commands[offset++];
                        long cacheKey = cacheKey(commands[offset++], commands[offset++]);
                        int extraRestoreCount = op == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N ? commands[offset++] : 0;
                        BufferedImage image = IMAGE_CACHE.get(new ImageCacheKey(contextPtr, cacheKey));
                        if (image == null) {
                            return false;
                        }
                        drawImage(current, image, filtered, 0, 0, image.getWidth() * 1000, image.getHeight() * 1000,
                                dstLeft1000, dstTop1000, dstRight1000, dstBottom1000, 1000);
                        if (op == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE || op == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N) {
                            int restoreCount = 1 + extraRestoreCount;
                            if (extraRestoreCount < 0 || stack.size() < restoreCount) {
                                return false;
                            }
                            for (int restoreIndex = 0; restoreIndex < restoreCount; restoreIndex++) {
                                current.dispose();
                                current = stack.removeLast();
                            }
                        }
                    } else if (op == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE) {
                        if (offset + 16 != recordEnd) return false;
                        boolean filtered = (record.recordFlags() & COMMAND_RECORD_FLAG_ANTIALIAS) != 0;
                        int dstLeft1000 = commands[offset++];
                        int dstTop1000 = commands[offset++];
                        int dstRight1000 = commands[offset++];
                        int dstBottom1000 = commands[offset++];
                        long cacheKey = cacheKey(commands[offset++], commands[offset++]);
                        int extraRestoreCount = commands[offset++];
                        BufferedImage image = IMAGE_CACHE.get(new ImageCacheKey(contextPtr, cacheKey));
                        if (image == null) {
                            return false;
                        }
                        drawImage(current, image, filtered, 0, 0, image.getWidth() * 1000, image.getHeight() * 1000,
                                dstLeft1000, dstTop1000, dstRight1000, dstBottom1000, 1000);
                        int restoreCount = 1 + extraRestoreCount;
                        if (extraRestoreCount < 0 || stack.size() < restoreCount) {
                            return false;
                        }
                        for (int restoreIndex = 0; restoreIndex < restoreCount; restoreIndex++) {
                            current.dispose();
                            current = stack.removeLast();
                        }
                        double layerDx = commands[offset++] / 1000.0;
                        double layerDy = commands[offset++] / 1000.0;
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int layerWidth = commands[offset++];
                        int layerHeight = commands[offset++];
                        int alpha1000 = commands[offset++];
                        double nestedDx = commands[offset++] / 1000.0;
                        double nestedDy = commands[offset++] / 1000.0;
                        if (layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000) {
                            return false;
                        }
                        stack.add(current);
                        current = (Graphics2D) current.create();
                        current.translate(layerDx, layerDy);
                        stack.add(current);
                        current = (Graphics2D) current.create();
                        current.clipRect(x, y, layerWidth, layerHeight);
                        stack.add(current);
                        current = (Graphics2D) current.create();
                        current.translate(nestedDx, nestedDy);
                    } else if (op == COMMAND_DRAW_IMAGE_REF_FULL_RUN) {
                        boolean filtered = (record.recordFlags() & COMMAND_RECORD_FLAG_ANTIALIAS) != 0;
                        int count = commands[offset++];
                        if (count <= 1 || offset + count * 6 != recordEnd) {
                            return false;
                        }
                        for (int i = 0; i < count; i++) {
                            int dstLeft1000 = commands[offset++];
                            int dstTop1000 = commands[offset++];
                            int dstRight1000 = commands[offset++];
                            int dstBottom1000 = commands[offset++];
                            long cacheKey = cacheKey(commands[offset++], commands[offset++]);
                            BufferedImage image = IMAGE_CACHE.get(new ImageCacheKey(contextPtr, cacheKey));
                            if (image == null) {
                                return false;
                            }
                            drawImage(current, image, filtered, 0, 0, image.getWidth() * 1000, image.getHeight() * 1000,
                                    dstLeft1000, dstTop1000, dstRight1000, dstBottom1000, 1000);
                        }
                    } else if (op == COMMAND_DRAW_IMAGE_REF_FULL_DRAW_ROUND_RECT) {
                        if (offset + 19 != recordEnd) return false;
                        int imageFlags = commands[offset++];
                        boolean filtered = (imageFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0;
                        int dstLeft1000 = commands[offset++];
                        int dstTop1000 = commands[offset++];
                        int dstRight1000 = commands[offset++];
                        int dstBottom1000 = commands[offset++];
                        long cacheKey = cacheKey(commands[offset++], commands[offset++]);
                        BufferedImage image = IMAGE_CACHE.get(new ImageCacheKey(contextPtr, cacheKey));
                        if (image == null) {
                            return false;
                        }
                        drawImage(current, image, filtered, 0, 0, image.getWidth() * 1000, image.getHeight() * 1000,
                                dstLeft1000, dstTop1000, dstRight1000, dstBottom1000, 1000);
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
                        applyAntialiasing(current, filtered);
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
                    } else if (op == COMMAND_DRAW_IMAGE_REF_FULL_FILL_RECT) {
                        if (offset + 13 != recordEnd) return false;
                        int imageFlags = commands[offset++];
                        boolean filtered = (imageFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0;
                        int dstLeft1000 = commands[offset++];
                        int dstTop1000 = commands[offset++];
                        int dstRight1000 = commands[offset++];
                        int dstBottom1000 = commands[offset++];
                        long cacheKey = cacheKey(commands[offset++], commands[offset++]);
                        BufferedImage image = IMAGE_CACHE.get(new ImageCacheKey(contextPtr, cacheKey));
                        if (image == null) {
                            return false;
                        }
                        drawImage(current, image, filtered, 0, 0, image.getWidth() * 1000, image.getHeight() * 1000,
                                dstLeft1000, dstTop1000, dstRight1000, dstBottom1000, 1000);
                        applyAntialiasing(current, antiAlias);
                        current.setColor(new Color(commands[offset++], true));
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int rectWidth = commands[offset++];
                        int rectHeight = commands[offset++];
                        int radius = commands[offset++];
                        if (rectWidth < 0 || rectHeight < 0 || radius < 0) return false;
                        if (radius > 0) {
                            current.fillRoundRect(x, y, rectWidth, rectHeight, radius, radius);
                        } else {
                            current.fillRect(x, y, rectWidth, rectHeight);
                        }
                    } else if (op == COMMAND_CLEAR_DRAW_IMAGE_REF_FULL) {
                        if (offset + 10 != recordEnd) return false;
                        boolean filtered = (record.recordFlags() & COMMAND_RECORD_FLAG_ANTIALIAS) != 0;
                        int clearX = commands[offset++];
                        int clearY = commands[offset++];
                        int clearWidth = commands[offset++];
                        int clearHeight = commands[offset++];
                        int dstLeft1000 = commands[offset++];
                        int dstTop1000 = commands[offset++];
                        int dstRight1000 = commands[offset++];
                        int dstBottom1000 = commands[offset++];
                        long cacheKey = cacheKey(commands[offset++], commands[offset++]);
                        BufferedImage image = IMAGE_CACHE.get(new ImageCacheKey(contextPtr, cacheKey));
                        if (image == null) {
                            return false;
                        }
                        Composite previousComposite = current.getComposite();
                        current.setComposite(AlphaComposite.Clear);
                        current.fillRect(clearX, clearY, clearWidth, clearHeight);
                        current.setComposite(previousComposite);
                        drawImage(current, image, filtered, 0, 0, image.getWidth() * 1000, image.getHeight() * 1000,
                                dstLeft1000, dstTop1000, dstRight1000, dstBottom1000, 1000);
                    } else if (op == COMMAND_DRAW_IMAGE_REF_COLOR_FILTER) {
                        if (offset + 16 != recordEnd) return false;
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
                        int filterColor = commands[offset++];
                        int colorFilterBlendMode = commands[offset++];
                        BufferedImage image = IMAGE_CACHE.get(new ImageCacheKey(contextPtr, cacheKey));
                        if (image == null || image.getWidth() != imageWidth || image.getHeight() != imageHeight
                                || alpha1000 < 0 || alpha1000 > 1000 || filterQuality < 0 || filterQuality > 3
                                || !isSupportedColorFilterBlendMode(colorFilterBlendMode)) {
                            return false;
                        }
                        BufferedImage filteredImage = applyBlendColorFilter(image, filterColor, colorFilterBlendMode);
                        if (filteredImage == null) return false;
                        drawImage(current, filteredImage, filtered,
                                srcLeft1000, srcTop1000, srcRight1000, srcBottom1000,
                                dstLeft1000, dstTop1000, dstRight1000, dstBottom1000, alpha1000);
                    } else if (op == COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF) {
                        if (offset + 16 != recordEnd) return false;
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
                        long handle = cacheKey(commands[offset++], commands[offset++]);
                        BufferedImage image = IMAGE_CACHE.get(new ImageCacheKey(contextPtr, cacheKey));
                        ColorFilterDescriptor descriptor = COLOR_FILTER_CACHE.get(new ColorFilterCacheKey(contextPtr, handle));
                        if (image == null || descriptor == null || !isColorFilterDescriptor(descriptor)
                                || image.getWidth() != imageWidth || image.getHeight() != imageHeight
                                || alpha1000 < 0 || alpha1000 > 1000 || filterQuality < 0 || filterQuality > 3) {
                            return false;
                        }
                        BufferedImage filteredImage = applyColorFilter(image, descriptor);
                        if (filteredImage == null) return false;
                        drawImage(current, filteredImage, filtered,
                                srcLeft1000, srcTop1000, srcRight1000, srcBottom1000,
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
                    } else if (op == COMMAND_STROKE_RECT_IMAGE_SHADER) {
                        if (offset + 15 != recordEnd) return false;
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
                        int strokeWidth1000 = commands[offset++];
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        int strokeMiter1000 = commands[offset++];
                        BufferedImage image = IMAGE_CACHE.get(new ImageCacheKey(contextPtr, cacheKey));
                        if (image == null || image.getWidth() != imageWidth || image.getHeight() != imageHeight
                                || right1000 < left1000 || bottom1000 < top1000
                                || tileModeX < 0 || tileModeX > 3 || tileModeY < 0 || tileModeY > 3
                                || alpha1000 < 0 || alpha1000 > 1000
                                || !isValidStrokeMetadata(strokeWidth1000, strokeCap, strokeJoin, strokeMiter1000)) {
                            return false;
                        }
                        Paint previousPaint = current.getPaint();
                        Composite previousComposite = current.getComposite();
                        current.setPaint(new TexturePaint(image, new Rectangle(0, 0, imageWidth, imageHeight)));
                        current.setStroke(new BasicStroke(
                                strokeWidth1000 / 1000f,
                                strokeCap == 1 ? BasicStroke.CAP_ROUND : strokeCap == 2 ? BasicStroke.CAP_SQUARE : BasicStroke.CAP_BUTT,
                                strokeJoin == 1 ? BasicStroke.JOIN_ROUND : strokeJoin == 2 ? BasicStroke.JOIN_BEVEL : BasicStroke.JOIN_MITER,
                                Math.max(1f, strokeMiter1000 / 1000f)
                        ));
                        if (alpha1000 < 1000) {
                            current.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha1000 / 1000f));
                        }
                        current.draw(new java.awt.geom.Rectangle2D.Float(
                                left1000 / 1000f,
                                top1000 / 1000f,
                                (right1000 - left1000) / 1000f,
                                (bottom1000 - top1000) / 1000f
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
                    } else if (op == COMMAND_DEFINE_FONT_DATA) {
                        if (offset + 3 > recordEnd) return false;
                        long handle = cacheKey(commands[offset++], commands[offset++]);
                        int byteCount = commands[offset++];
                        if (byteCount <= 0 || byteCount > 1048576 || offset + byteCount != recordEnd) {
                            return false;
                        }
                        byte[] data = new byte[byteCount];
                        for (int index = 0; index < byteCount; index++) {
                            int value = commands[offset++];
                            if (value < 0 || value > 255) {
                                return false;
                            }
                            data[index] = (byte) value;
                        }
                        try {
                            fontDataFonts.put(handle, Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(data)));
                            System.err.println("JBR_SKIA_INTEROP_FONT_DATA_DEFINE backend=java2d contextId=0x"
                                    + Long.toHexString(contextPtr) + " handle=0x"
                                    + Long.toHexString(handle) + " bytes=" + byteCount);
                        } catch (Exception ignored) {
                            return false;
                        }
                    } else if (op == COMMAND_DRAW_TEXT_UTF16) {
                        if (offset + 9 > recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        int x1000 = commands[offset++];
                        int baseline1000 = commands[offset++];
                        int fontSize1000 = commands[offset++];
                        int argb = commands[offset++];
                        int fontWeight = commands[offset++];
                        int fontWidth = commands[offset++];
                        int fontSlant = commands[offset++];
                        String fontFamily = readUtf16String(commands, recordEnd, offset, 256);
                        if (fontFamily == null) return false;
                        offset += 1 + fontFamily.length();
                        int charCount = commands[offset++];
                        if (fontSize1000 <= 0
                                || fontWeight < 1 || fontWeight > 1000
                                || fontWidth < 1 || fontWidth > 9
                                || fontSlant < 0 || fontSlant > 2
                                || charCount < 0 || charCount > 4096 || offset + charCount != recordEnd) {
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
                        Font font = deriveFontDataCommandFont(fontDataFonts, fontFamily,
                                fontStyleFromCommand(fontWeight, fontSlant), fontSize1000);
                        if (font == null) {
                            font = deriveCommandFont(previousFont, fontFamily,
                                    fontStyleFromCommand(fontWeight, fontSlant), fontSize1000);
                        }
                        current.setFont(font);
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
                        current.setFont(deriveCommandFont(previousFont, fontFamily,
                                fontStyleFromCommand(fontWeight, fontSlant), fontSize1000));
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
                    } else if (op == COMMAND_FILL_RECT_SAVE_LAYER_CLIP_RECT) {
                        if (offset + 17 != recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        current.setColor(new Color(commands[offset++], true));
                        int fillX = commands[offset++];
                        int fillY = commands[offset++];
                        int fillWidth = commands[offset++];
                        int fillHeight = commands[offset++];
                        int radius = commands[offset++];
                        if (fillWidth < 0 || fillHeight < 0 || radius < 0) {
                            return false;
                        }
                        if (radius > 0) {
                            current.fillRoundRect(fillX, fillY, fillWidth, fillHeight, radius, radius);
                        } else {
                            current.fillRect(fillX, fillY, fillWidth, fillHeight);
                        }
                        boolean clipAntialias = (commands[offset++] & COMMAND_RECORD_FLAG_ANTIALIAS) != 0;
                        int layerX = commands[offset++];
                        int layerY = commands[offset++];
                        int layerWidth = commands[offset++];
                        int layerHeight = commands[offset++];
                        int alpha1000 = commands[offset++];
                        int clipX = commands[offset++];
                        int clipY = commands[offset++];
                        int clipWidth = commands[offset++];
                        int clipHeight = commands[offset++];
                        int clipOp = commands[offset++];
                        if (layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000
                                || clipWidth < 0 || clipHeight < 0
                                || (clipOp != COMMAND_CLIP_OP_INTERSECT && clipOp != COMMAND_CLIP_OP_DIFFERENCE)) {
                            return false;
                        }
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                        current.clipRect(layerX, layerY, layerWidth, layerHeight);
                        applyAntialiasing(current, clipAntialias);
                        if (clipOp == COMMAND_CLIP_OP_INTERSECT) {
                            current.clipRect(clipX, clipY, clipWidth, clipHeight);
                        } else {
                            Shape previousClip = current.getClip();
                            if (previousClip == null) return false;
                            Area area = new Area(previousClip);
                            area.subtract(new Area(new Rectangle(clipX, clipY, clipWidth, clipHeight)));
                            current.setClip(area);
                        }
                    } else if (op == COMMAND_FILL_RECT_SAVE) {
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
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                    } else if (op == COMMAND_SAVE_FILL_RECT_SAVE) {
                        if (offset + 6 != recordEnd) return false;
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
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
                        stack.addLast(current);
                        current = (Graphics2D) current.create();
                    } else if (op == COMMAND_FILL_RECT_BLEND_MODE) {
                        if (offset + 6 != recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        current.setColor(new Color(commands[offset++], true));
                        int blendMode = commands[offset++];
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        if (!isSupportedFillBlendMode(blendMode) || width < 0 || height < 0) return false;
                        current.fillRect(x, y, width, height);
                    } else if (op == COMMAND_FILL_RECT_COLOR_FILTER) {
                        if (offset + 7 != recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        int argb = commands[offset++];
                        int filterArgb = commands[offset++];
                        int filterBlendMode = commands[offset++];
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        if (filterBlendMode != COMMAND_BLEND_MODE_SRC_IN || width < 0 || height < 0) return false;
                        int sourceAlpha = (argb >>> 24) & 0xff;
                        int filterAlpha = (filterArgb >>> 24) & 0xff;
                        int combinedAlpha = (sourceAlpha * filterAlpha + 127) / 255;
                        current.setColor(new Color((combinedAlpha << 24) | (filterArgb & 0x00ffffff), true));
                        current.fillRect(x, y, width, height);
                    } else if (op == COMMAND_DEFINE_COLOR_FILTER_TINT) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 4 != recordEnd) return false;
                        long handle = cacheKey(commands[offset++], commands[offset++]);
                        int filterArgb = commands[offset++];
                        int filterBlendMode = commands[offset++];
                        if (filterBlendMode != COMMAND_BLEND_MODE_SRC_IN) return false;
                        COLOR_FILTER_CACHE.put(
                                new ColorFilterCacheKey(contextPtr, handle),
                                ColorFilterDescriptor.tint(filterArgb, filterBlendMode)
                        );
                        logEffectHandleDefine("java2d", contextPtr, handle, COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER, 0, 2, true);
                    } else if (op == COMMAND_DEFINE_EFFECT_DESCRIPTOR) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 5 > recordEnd) return false;
                        long handle = cacheKey(commands[offset++], commands[offset++]);
                        int descriptorType = commands[offset++];
                        int descriptorVersion = commands[offset++];
                        int payloadIntCount = commands[offset++];
                        if (descriptorVersion != COMMAND_EFFECT_DESCRIPTOR_VERSION_1
                                || offset + payloadIntCount != recordEnd) return false;
                        if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER) {
                            if (payloadIntCount != 2) return false;
                            int filterArgb = commands[offset++];
                            int filterBlendMode = commands[offset++];
                            if (!isSupportedColorFilterBlendMode(filterBlendMode)) return false;
                            COLOR_FILTER_CACHE.put(
                                    new ColorFilterCacheKey(contextPtr, handle),
                                    ColorFilterDescriptor.tint(filterArgb, filterBlendMode)
                            );
                            logEffectHandleDefine("java2d", contextPtr, handle, descriptorType, descriptorVersion, payloadIntCount, false);
                        } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER) {
                            if (payloadIntCount != 20) return false;
                            int[] matrixBits = new int[20];
                            for (int index = 0; index < matrixBits.length; index++) {
                                int bits = commands[offset++];
                                if (!Float.isFinite(Float.intBitsToFloat(bits))) return false;
                                matrixBits[index] = bits;
                            }
                            COLOR_FILTER_CACHE.put(
                                    new ColorFilterCacheKey(contextPtr, handle),
                                    ColorFilterDescriptor.colorMatrix(matrixBits)
                            );
                            logEffectHandleDefine("java2d", contextPtr, handle, descriptorType, descriptorVersion, payloadIntCount, false);
                        } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_LIGHTING_FILTER) {
                            if (payloadIntCount != 2) return false;
                            int multiplyArgb = commands[offset++];
                            int addArgb = commands[offset++];
                            COLOR_FILTER_CACHE.put(
                                    new ColorFilterCacheKey(contextPtr, handle),
                                    ColorFilterDescriptor.lighting(multiplyArgb, addArgb)
                            );
                            logEffectHandleDefine("java2d", contextPtr, handle, descriptorType, descriptorVersion, payloadIntCount, false);
                        } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER
                                || descriptorType == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT) {
                            ColorFilterDescriptor child = null;
                            if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT) {
                                if (payloadIntCount != 5) return false;
                                long childHandle = cacheKey(commands[offset++], commands[offset++]);
                                child = COLOR_FILTER_CACHE.get(new ColorFilterCacheKey(contextPtr, childHandle));
                                if (child == null || !isImageFilterDescriptor(child)) return false;
                            } else if (payloadIntCount != 3) return false;
                            int sigmaXBits = commands[offset++];
                            int sigmaYBits = commands[offset++];
                            int tileMode = commands[offset++];
                            float sigmaX = Float.intBitsToFloat(sigmaXBits);
                            float sigmaY = Float.intBitsToFloat(sigmaYBits);
                            if (!Float.isFinite(sigmaX) || !Float.isFinite(sigmaY)
                                    || sigmaX < 0f || sigmaY < 0f || tileMode < 0 || tileMode > 3) return false;
                            COLOR_FILTER_CACHE.put(
                                    new ColorFilterCacheKey(contextPtr, handle),
                                    ColorFilterDescriptor.blurImageFilter(descriptorType, sigmaXBits, sigmaYBits, tileMode, child)
                            );
                            logEffectHandleDefine("java2d", contextPtr, handle, descriptorType, descriptorVersion, payloadIntCount, false);
                        } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER
                                || descriptorType == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT) {
                            ColorFilterDescriptor child = null;
                            if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT) {
                                if (payloadIntCount != 4) return false;
                                long childHandle = cacheKey(commands[offset++], commands[offset++]);
                                child = COLOR_FILTER_CACHE.get(new ColorFilterCacheKey(contextPtr, childHandle));
                                if (child == null || !isImageFilterDescriptor(child)) return false;
                            } else if (payloadIntCount != 2) return false;
                            int dxBits = commands[offset++];
                            int dyBits = commands[offset++];
                            float dx = Float.intBitsToFloat(dxBits);
                            float dy = Float.intBitsToFloat(dyBits);
                            if (!Float.isFinite(dx) || !Float.isFinite(dy)) return false;
                            COLOR_FILTER_CACHE.put(
                                    new ColorFilterCacheKey(contextPtr, handle),
                                    ColorFilterDescriptor.offsetImageFilter(descriptorType, dxBits, dyBits, child)
                            );
                            logEffectHandleDefine("java2d", contextPtr, handle, descriptorType, descriptorVersion, payloadIntCount, false);
                        } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER) {
                            if (!validateRuntimeColorFilterDescriptorPayload(commands, record, payloadIntCount)) return false;
                            int childCount = commands[offset + 2];
                            for (int index = 0; index < childCount; index++) {
                                long childHandle = cacheKey(
                                        commands[offset + 7 + index * 2],
                                        commands[offset + 8 + index * 2]
                                );
                                ColorFilterDescriptor child = COLOR_FILTER_CACHE.get(new ColorFilterCacheKey(contextPtr, childHandle));
                                if (child == null || !isColorFilterDescriptor(child)) return false;
                            }
                            int[] payload = new int[payloadIntCount];
                            System.arraycopy(commands, offset, payload, 0, payloadIntCount);
                            offset += payloadIntCount;
                            COLOR_FILTER_CACHE.put(
                                    new ColorFilterCacheKey(contextPtr, handle),
                                    ColorFilterDescriptor.runtimeColorFilter(payload)
                            );
                            logEffectHandleDefine("java2d", contextPtr, handle, descriptorType, descriptorVersion, payloadIntCount, false);
                        } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT) {
                            if (payloadIntCount != 1) return false;
                            int radiusBits = commands[offset++];
                            float radius = Float.intBitsToFloat(radiusBits);
                            if (!Float.isFinite(radius) || radius < 0f) return false;
                            COLOR_FILTER_CACHE.put(
                                    new ColorFilterCacheKey(contextPtr, handle),
                                    ColorFilterDescriptor.pathEffect(descriptorType, new int[] { radiusBits })
                            );
                            logEffectHandleDefine("java2d", contextPtr, handle, descriptorType, descriptorVersion, payloadIntCount, false);
                        } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_STAMPED_PATH_EFFECT) {
                            if (payloadIntCount < 5) return false;
                            int[] payload = new int[payloadIntCount];
                            System.arraycopy(commands, offset, payload, 0, payloadIntCount);
                            float advance = Float.intBitsToFloat(payload[0]);
                            float phase = Float.intBitsToFloat(payload[1]);
                            int style = payload[2];
                            int fillType = payload[3];
                            int pathDataLength = payload[4];
                            if (!Float.isFinite(advance) || advance <= 0f
                                    || !Float.isFinite(phase) || phase < 0f
                                    || style < 0 || style > 2
                                    || (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
                                    || pathDataLength < 0 || pathDataLength > 4096
                                    || payloadIntCount != 5 + pathDataLength
                                    || !validatePathData(payload, 5, payload.length)) return false;
                            offset += payloadIntCount;
                            COLOR_FILTER_CACHE.put(
                                    new ColorFilterCacheKey(contextPtr, handle),
                                    ColorFilterDescriptor.pathEffect(descriptorType, payload)
                            );
                            logEffectHandleDefine("java2d", contextPtr, handle, descriptorType, descriptorVersion, payloadIntCount, false);
                        } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_CHAIN_PATH_EFFECT) {
                            if (payloadIntCount != 4) return false;
                            long outerHandle = cacheKey(commands[offset], commands[offset + 1]);
                            long innerHandle = cacheKey(commands[offset + 2], commands[offset + 3]);
                            ColorFilterDescriptor outer = COLOR_FILTER_CACHE.get(new ColorFilterCacheKey(contextPtr, outerHandle));
                            ColorFilterDescriptor inner = COLOR_FILTER_CACHE.get(new ColorFilterCacheKey(contextPtr, innerHandle));
                            if (outer == null || inner == null
                                    || !isPathEffectDescriptor(outer) || !isPathEffectDescriptor(inner)) return false;
                            int[] payload = new int[payloadIntCount];
                            System.arraycopy(commands, offset, payload, 0, payloadIntCount);
                            offset += payloadIntCount;
                            COLOR_FILTER_CACHE.put(
                                    new ColorFilterCacheKey(contextPtr, handle),
                                    ColorFilterDescriptor.pathEffect(descriptorType, payload)
                            );
                            logEffectHandleDefine("java2d", contextPtr, handle, descriptorType, descriptorVersion, payloadIntCount, false);
                        } else {
                            return false;
                        }
                    } else if (op == COMMAND_DEFINE_SHADER_DESCRIPTOR) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 5 > recordEnd) return false;
                        long handle = cacheKey(commands[offset++], commands[offset++]);
                        int descriptorType = commands[offset++];
                        int descriptorVersion = commands[offset++];
                        int payloadIntCount = commands[offset++];
                        if (descriptorVersion != COMMAND_SHADER_DESCRIPTOR_VERSION_1
                                || offset + payloadIntCount != recordEnd) return false;
                        int payloadStart = offset;
                        if (!validateShaderDescriptorPayload(commands, record, descriptorType, payloadIntCount)) return false;
                        ShaderDescriptor dst = null;
                        ShaderDescriptor src = null;
                        ShaderDescriptor child = null;
                        ColorFilterDescriptor colorFilter = null;
                        ShaderDescriptor[] children = null;
                        if (descriptorType == COMMAND_SHADER_DESCRIPTOR_COMPOSITE) {
                            long dstHandle = cacheKey(commands[offset++], commands[offset++]);
                            long srcHandle = cacheKey(commands[offset++], commands[offset++]);
                            dst = SHADER_CACHE.get(new ColorFilterCacheKey(contextPtr, dstHandle));
                            src = SHADER_CACHE.get(new ColorFilterCacheKey(contextPtr, srcHandle));
                            if (dst == null || src == null || !isSupportedBlendMode(commands[offset++])) return false;
                        } else if (descriptorType == COMMAND_SHADER_DESCRIPTOR_COLOR_FILTER) {
                            long childHandle = cacheKey(commands[offset++], commands[offset++]);
                            long colorFilterHandle = cacheKey(commands[offset++], commands[offset++]);
                            child = SHADER_CACHE.get(new ColorFilterCacheKey(contextPtr, childHandle));
                            colorFilter = COLOR_FILTER_CACHE.get(new ColorFilterCacheKey(contextPtr, colorFilterHandle));
                            if (child == null || colorFilter == null || !isColorFilterDescriptor(colorFilter)) return false;
                        } else if (descriptorType == COMMAND_SHADER_DESCRIPTOR_TRANSFORM) {
                            long childHandle = cacheKey(commands[offset++], commands[offset++]);
                            child = SHADER_CACHE.get(new ColorFilterCacheKey(contextPtr, childHandle));
                            if (child == null) return false;
                            offset = recordEnd;
                        } else if (descriptorType == COMMAND_SHADER_DESCRIPTOR_RUNTIME_EFFECT) {
                            int childCount = commands[offset + 2];
                            int namedUniformCount = commands[offset + 3];
                            int namedChildCount = commands[offset + 4];
                            children = new ShaderDescriptor[childCount];
                            offset += 7;
                            for (int index = 0; index < childCount; index++) {
                                long childHandle = cacheKey(commands[offset++], commands[offset++]);
                                children[index] = SHADER_CACHE.get(new ColorFilterCacheKey(contextPtr, childHandle));
                                if (children[index] == null) return false;
                            }
                            for (int index = 0; index < namedUniformCount; index++) {
                                offset += 2;
                                int nameLength = commands[offset++];
                                offset += nameLength;
                            }
                            for (int index = 0; index < namedChildCount; index++) {
                                offset++;
                                int nameLength = commands[offset++];
                                offset += nameLength;
                            }
                            offset = recordEnd;
                        } else {
                            offset = recordEnd;
                        }
                        int[] payload = new int[payloadIntCount];
                        System.arraycopy(commands, payloadStart, payload, 0, payloadIntCount);
                        SHADER_CACHE.put(new ColorFilterCacheKey(contextPtr, handle), new ShaderDescriptor(descriptorType, payload, dst, src, child, colorFilter, children));
                        logShaderHandleDefine("java2d", contextPtr, handle, descriptorType, descriptorVersion, payloadIntCount);
                    } else if (op == COMMAND_EVICT_SHADER_HANDLE) {
                        if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) return false;
                        long handle = cacheKey(commands[offset++], commands[offset++]);
                        ShaderDescriptor removed = SHADER_CACHE.remove(new ColorFilterCacheKey(contextPtr, handle));
                        logShaderHandleEvict("java2d", contextPtr, handle, removed != null);
                    } else if (op == COMMAND_FILL_RECT_SHADER_REF) {
                        if (offset + 7 != recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        long handle = cacheKey(commands[offset++], commands[offset++]);
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int alpha1000 = commands[offset++];
                        ShaderDescriptor descriptor = SHADER_CACHE.get(new ColorFilterCacheKey(contextPtr, handle));
                        if (descriptor == null || right1000 < left1000 || bottom1000 < top1000
                                || alpha1000 < 0 || alpha1000 > 1000) return false;
                        logShaderHandleUse("java2d", contextPtr, handle, op);
                        current.setColor(new Color((alpha1000 * 255 / 1000) << 24 | 0x3388ff, true));
                        current.fillRect(
                                left1000 / 1000,
                                top1000 / 1000,
                                (right1000 - left1000) / 1000,
                                (bottom1000 - top1000) / 1000
                        );
                    } else if (op == COMMAND_STROKE_RECT_SHADER_REF) {
                        if (offset + 11 != recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        long handle = cacheKey(commands[offset++], commands[offset++]);
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int strokeWidth1000 = commands[offset++];
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        int strokeMiter1000 = commands[offset++];
                        int alpha1000 = commands[offset++];
                        ShaderDescriptor descriptor = SHADER_CACHE.get(new ColorFilterCacheKey(contextPtr, handle));
                        if (descriptor == null || right1000 < left1000 || bottom1000 < top1000
                                || !isValidStrokeMetadata(strokeWidth1000, strokeCap, strokeJoin, strokeMiter1000)
                                || alpha1000 < 0 || alpha1000 > 1000) return false;
                        logShaderHandleUse("java2d", contextPtr, handle, op);
                        current.setColor(new Color((alpha1000 * 255 / 1000) << 24 | 0x3388ff, true));
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
                    } else if (op == COMMAND_FILL_RECT_COLOR_FILTER_REF) {
                        if (offset + 7 != recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        int argb = commands[offset++];
                        long handle = cacheKey(commands[offset++], commands[offset++]);
                        int x = commands[offset++];
                        int y = commands[offset++];
                        int width = commands[offset++];
                        int height = commands[offset++];
                        ColorFilterDescriptor descriptor = COLOR_FILTER_CACHE.get(new ColorFilterCacheKey(contextPtr, handle));
                        if (descriptor == null || !isColorFilterDescriptor(descriptor) || width < 0 || height < 0) return false;
                        logEffectHandleUse("java2d", contextPtr, handle, op);
                        if (descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER) {
                            Integer filteredColor = applyBlendColorFilter(argb, descriptor.argb(), descriptor.blendMode());
                            if (filteredColor == null) return false;
                            current.setColor(new Color(filteredColor, true));
                        } else if (descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER) {
                            current.setColor(new Color(applyColorMatrix(argb, descriptor.matrixBits()), true));
                        } else if (descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_LIGHTING_FILTER) {
                            current.setColor(new Color(applyLightingFilter(argb, descriptor.argb(), descriptor.blendMode()), true));
                        } else {
                            return false;
                        }
                        current.fillRect(x, y, width, height);
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
                    } else if (op == COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN
                            || op == COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN_RESTORE_N) {
                        boolean imageFiltered = (commands[offset++] & COMMAND_RECORD_FLAG_ANTIALIAS) != 0;
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
                        int count = commands[offset++];
                        if (count <= 1 || offset + count * 6
                                + (op == COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN_RESTORE_N ? 1 : 0) != recordEnd) {
                            return false;
                        }
                        java.awt.Stroke previous = current.getStroke();
                        java.awt.BasicStroke stroke = basicStroke(strokeWidth, strokeCap, strokeJoin, strokeMiter);
                        if (stroke == null) return false;
                        try {
                            current.setStroke(stroke);
                            current.drawLine(x1, y1, x2, y2);
                        } finally {
                            current.setStroke(previous);
                        }
                        for (int i = 0; i < count; i++) {
                            int dstLeft1000 = commands[offset++];
                            int dstTop1000 = commands[offset++];
                            int dstRight1000 = commands[offset++];
                            int dstBottom1000 = commands[offset++];
                            long cacheKey = cacheKey(commands[offset++], commands[offset++]);
                            BufferedImage image = IMAGE_CACHE.get(new ImageCacheKey(contextPtr, cacheKey));
                            if (image == null) {
                                return false;
                            }
                            drawImage(current, image, imageFiltered, 0, 0, image.getWidth() * 1000, image.getHeight() * 1000,
                                    dstLeft1000, dstTop1000, dstRight1000, dstBottom1000, 1000);
                        }
                        if (op == COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN_RESTORE_N) {
                            int restoreCount = commands[offset++];
                            if (restoreCount <= 0 || stack.size() < restoreCount) {
                                return false;
                            }
                            for (int i = 0; i < restoreCount; i++) {
                                current.dispose();
                                current = stack.removeLast();
                            }
                        }
                    } else if (op == COMMAND_STROKE_LINE_DASH_PATH_EFFECT) {
                        if (offset + 13 > recordEnd) return false;
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
                        float phase = commands[offset++] / 1000f;
                        int intervalCount = commands[offset++];
                        if (intervalCount < 2 || intervalCount > 16 || offset + intervalCount != recordEnd) return false;
                        float[] intervals = new float[intervalCount];
                        for (int index = 0; index < intervalCount; index++) {
                            int interval = commands[offset++];
                            if (interval <= 0) return false;
                            intervals[index] = interval / 1000f;
                        }
                        java.awt.Stroke previous = current.getStroke();
                        java.awt.BasicStroke stroke = basicStroke(strokeWidth, strokeCap, strokeJoin, strokeMiter, intervals, phase);
                        if (stroke == null) return false;
                        try {
                            current.setStroke(stroke);
                            current.drawLine(x1, y1, x2, y2);
                        } finally {
                            current.setStroke(previous);
                        }
                    } else if (op == COMMAND_STROKE_RECT_DASH_PATH_EFFECT) {
                        if (offset + 13 > recordEnd) return false;
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
                        float phase = commands[offset++] / 1000f;
                        int intervalCount = commands[offset++];
                        if (width < 0 || height < 0 || intervalCount < 2 || intervalCount > 16
                                || offset + intervalCount != recordEnd) return false;
                        float[] intervals = new float[intervalCount];
                        for (int index = 0; index < intervalCount; index++) {
                            int interval = commands[offset++];
                            if (interval <= 0) return false;
                            intervals[index] = interval / 1000f;
                        }
                        java.awt.Stroke previous = current.getStroke();
                        java.awt.BasicStroke stroke = basicStroke(strokeWidth, strokeCap, strokeJoin, strokeMiter, intervals, phase);
                        if (stroke == null) return false;
                        try {
                            current.setStroke(stroke);
                            current.drawRect(x, y, width, height);
                        } finally {
                            current.setStroke(previous);
                        }
                    } else if (op == COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT) {
                        if (offset + 15 > recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        current.setColor(new Color(commands[offset++], true));
                        int left1000 = commands[offset++];
                        int top1000 = commands[offset++];
                        int right1000 = commands[offset++];
                        int bottom1000 = commands[offset++];
                        int radiusX1000 = commands[offset++];
                        int radiusY1000 = commands[offset++];
                        int strokeWidth = Math.max(1, commands[offset++]);
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        float strokeMiter = commands[offset++] / 1000f;
                        float phase = commands[offset++] / 1000f;
                        int intervalCount = commands[offset++];
                        if (right1000 < left1000 || bottom1000 < top1000 || radiusX1000 < 0 || radiusY1000 < 0
                                || intervalCount < 2 || intervalCount > 16 || offset + intervalCount != recordEnd) return false;
                        float[] intervals = new float[intervalCount];
                        for (int index = 0; index < intervalCount; index++) {
                            int interval = commands[offset++];
                            if (interval <= 0) return false;
                            intervals[index] = interval / 1000f;
                        }
                        java.awt.Stroke previous = current.getStroke();
                        java.awt.BasicStroke stroke = basicStroke(strokeWidth, strokeCap, strokeJoin, strokeMiter, intervals, phase);
                        if (stroke == null) return false;
                        RoundRectangle2D.Float roundRect = new RoundRectangle2D.Float(
                                left1000 / 1000f,
                                top1000 / 1000f,
                                (right1000 - left1000) / 1000f,
                                (bottom1000 - top1000) / 1000f,
                                radiusX1000 / 1000f,
                                radiusY1000 / 1000f
                        );
                        try {
                            current.setStroke(stroke);
                            current.draw(roundRect);
                        } finally {
                            current.setStroke(previous);
                        }
                    } else if (op == COMMAND_STROKE_PATH_DASH_PATH_EFFECT) {
                        if (offset + 9 > recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        current.setColor(new Color(commands[offset++], true));
                        int strokeWidth = Math.max(1, commands[offset++]);
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        float strokeMiter = commands[offset++] / 1000f;
                        float phase = commands[offset++] / 1000f;
                        int intervalCount = commands[offset++];
                        if (intervalCount < 2 || intervalCount > 16 || offset + intervalCount + 2 > recordEnd) return false;
                        float[] intervals = new float[intervalCount];
                        for (int index = 0; index < intervalCount; index++) {
                            int interval = commands[offset++];
                            if (interval <= 0) return false;
                            intervals[index] = interval / 1000f;
                        }
                        int fillType = commands[offset++];
                        int pathDataLength = commands[offset++];
                        if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
                                || pathDataLength < 0
                                || offset + pathDataLength != recordEnd) {
                            return false;
                        }
                        Path2D path = pathFromCommandData(commands, offset, recordEnd, fillType);
                        if (path == null) return false;
                        offset = recordEnd;
                        java.awt.Stroke previous = current.getStroke();
                        java.awt.BasicStroke stroke = basicStroke(strokeWidth, strokeCap, strokeJoin, strokeMiter, intervals, phase);
                        if (stroke == null) return false;
                        try {
                            current.setStroke(stroke);
                            current.draw(path);
                        } finally {
                            current.setStroke(previous);
                        }
                    } else if (op == COMMAND_DRAW_SHADOW_PATH) {
                        if (offset + 14 > recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        int ambientArgb = commands[offset++];
                        int spotArgb = commands[offset++];
                        offset += 8;
                        int fillType = commands[offset++];
                        int pathDataLength = commands[offset++];
                        if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD)
                                || pathDataLength < 0
                                || offset + pathDataLength != recordEnd) {
                            return false;
                        }
                        Path2D path = pathFromCommandData(commands, offset, recordEnd, fillType);
                        if (path == null) return false;
                        offset = recordEnd;
                        Composite previousComposite = current.getComposite();
                        try {
                            current.setComposite(AlphaComposite.SrcOver);
                            current.setColor(new Color(ambientArgb, true));
                            current.fill(path);
                            current.setColor(new Color(spotArgb, true));
                            current.fill(path);
                        } finally {
                            current.setComposite(previousComposite);
                        }
                    } else if (op == COMMAND_DRAW_POINTS) {
                        if (offset + 6 > recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        current.setColor(new Color(commands[offset++], true));
                        int strokeWidth = Math.max(1, commands[offset++]);
                        int strokeCap = commands[offset++];
                        int strokeJoin = commands[offset++];
                        float strokeMiter = commands[offset++] / 1000f;
                        int pointCount = commands[offset++];
                        if (pointCount < 1 || pointCount > 4096 || offset + pointCount * 2 != recordEnd) {
                            return false;
                        }
                        java.awt.Stroke previous = current.getStroke();
                        java.awt.BasicStroke stroke = basicStroke(strokeWidth, strokeCap, strokeJoin, strokeMiter);
                        if (stroke == null) return false;
                        try {
                            current.setStroke(stroke);
                            for (int pointIndex = 0; pointIndex < pointCount; pointIndex++) {
                                int x = commands[offset++];
                                int y = commands[offset++];
                                current.drawLine(x, y, x, y);
                            }
                        } finally {
                            current.setStroke(previous);
                        }
                    } else if (op == COMMAND_DRAW_VERTICES) {
                        if (offset + 5 > recordEnd) return false;
                        applyAntialiasing(current, antiAlias);
                        int vertexMode = commands[offset++];
                        int blendMode = commands[offset++];
                        offset++; // paint ARGB is kept for native parity and future fallback refinement.
                        int vertexCount = commands[offset++];
                        int indexCount = commands[offset++];
                        if (vertexMode < 0 || vertexMode > 2
                                || !isSupportedBlendMode(blendMode)
                                || vertexCount < 3
                                || vertexCount > 4096
                                || indexCount < 0
                                || indexCount > 8192
                                || offset + vertexCount * 5 + indexCount != recordEnd) {
                            return false;
                        }
                        int positionsStart = offset;
                        int texCoordsStart = positionsStart + vertexCount * 2;
                        int colorsStart = texCoordsStart + vertexCount * 2;
                        int indicesStart = colorsStart + vertexCount;
                        Composite previousComposite = current.getComposite();
                        try {
                            current.setComposite(AlphaComposite.SrcOver);
                            int triangleCount = vertexMode == 0
                                    ? ((indexCount > 0 ? indexCount : vertexCount) / 3)
                                    : Math.max(0, (indexCount > 0 ? indexCount : vertexCount) - 2);
                            for (int triangleIndex = 0; triangleIndex < triangleCount; triangleIndex++) {
                                int i0;
                                int i1;
                                int i2;
                                if (indexCount > 0) {
                                    if (vertexMode == 0) {
                                        i0 = commands[indicesStart + triangleIndex * 3];
                                        i1 = commands[indicesStart + triangleIndex * 3 + 1];
                                        i2 = commands[indicesStart + triangleIndex * 3 + 2];
                                    } else if (vertexMode == 1) {
                                        i0 = commands[indicesStart + triangleIndex];
                                        i1 = commands[indicesStart + triangleIndex + 1];
                                        i2 = commands[indicesStart + triangleIndex + 2];
                                    } else {
                                        i0 = commands[indicesStart];
                                        i1 = commands[indicesStart + triangleIndex + 1];
                                        i2 = commands[indicesStart + triangleIndex + 2];
                                    }
                                } else if (vertexMode == 0) {
                                    i0 = triangleIndex * 3;
                                    i1 = triangleIndex * 3 + 1;
                                    i2 = triangleIndex * 3 + 2;
                                } else if (vertexMode == 1) {
                                    i0 = triangleIndex;
                                    i1 = triangleIndex + 1;
                                    i2 = triangleIndex + 2;
                                } else {
                                    i0 = 0;
                                    i1 = triangleIndex + 1;
                                    i2 = triangleIndex + 2;
                                }
                                if (i0 < 0 || i0 >= vertexCount || i1 < 0 || i1 >= vertexCount || i2 < 0 || i2 >= vertexCount) {
                                    return false;
                                }
                                Path2D triangle = new Path2D.Float();
                                triangle.moveTo(commands[positionsStart + i0 * 2], commands[positionsStart + i0 * 2 + 1]);
                                triangle.lineTo(commands[positionsStart + i1 * 2], commands[positionsStart + i1 * 2 + 1]);
                                triangle.lineTo(commands[positionsStart + i2 * 2], commands[positionsStart + i2 * 2 + 1]);
                                triangle.closePath();
                                current.setColor(new Color(commands[colorsStart + i0], true));
                                current.fill(triangle);
                            }
                        } finally {
                            current.setComposite(previousComposite);
                        }
                        offset = recordEnd;
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
            String family = commandAwtFontFamily(fontFamily, previousFont.getFamily());
            return new java.awt.Font(family, fontStyle, Math.max(1, Math.round(fontSize1000 / 1000f)))
                    .deriveFont(fontSize1000 / 1000f);
    }

    private static Font deriveFontDataCommandFont(
            Map<Long, Font> fontDataFonts,
            String fontFamily,
            int fontStyle,
            int fontSize1000
    ) {
            Long handle = fontDataHandle(fontFamily);
            if (handle == null) {
                return null;
            }
            Font font = fontDataFonts.get(handle);
            if (font == null) {
                return null;
            }
            return font.deriveFont(fontStyle, fontSize1000 / 1000f);
    }

    private static Long fontDataHandle(String fontFamily) {
            String prefix = "jbr-font-data:";
            if (!fontFamily.startsWith(prefix)) {
                return null;
            }
            int separator = fontFamily.indexOf(':', prefix.length());
            if (separator < 0) {
                return null;
            }
            try {
                int high = Integer.parseInt(fontFamily.substring(prefix.length(), separator));
                int low = Integer.parseInt(fontFamily.substring(separator + 1));
                return cacheKey(high, low);
            } catch (NumberFormatException ignored) {
                return null;
            }
    }

    private static String commandAwtFontFamily(String fontFamily, String defaultFamily) {
            return switch (fontFamily) {
                case "" -> defaultFamily;
                case "sans-serif" -> java.awt.Font.SANS_SERIF;
                case "serif" -> java.awt.Font.SERIF;
                case "monospace" -> java.awt.Font.MONOSPACED;
                default -> fontFamily;
            };
    }

    private static int fontStyleFromCommand(int fontWeight, int fontSlant) {
            return (fontWeight >= 600 ? java.awt.Font.BOLD : java.awt.Font.PLAIN)
                    | (fontSlant == 0 ? java.awt.Font.PLAIN : java.awt.Font.ITALIC);
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
            if (previousInterpolation == null) {
                current.getRenderingHints().remove(RenderingHints.KEY_INTERPOLATION);
            } else {
                current.setRenderingHint(RenderingHints.KEY_INTERPOLATION, previousInterpolation);
            }
        }

        private static BufferedImage tintImageSrcIn(BufferedImage image, int filterColor) {
            BufferedImage tinted = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            int filterAlpha = (filterColor >>> 24) & 0xff;
            int filterRgb = filterColor & 0x00ffffff;
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int sourceAlpha = (image.getRGB(x, y) >>> 24) & 0xff;
                    int alpha = sourceAlpha * filterAlpha / 255;
                    tinted.setRGB(x, y, (alpha << 24) | filterRgb);
                }
            }
            return tinted;
        }

        private static Integer applyBlendColorFilter(int argb, int filterColor, int blendMode) {
            if (blendMode == COMMAND_BLEND_MODE_SRC_IN) {
                int sourceAlpha = (argb >>> 24) & 0xff;
                int filterAlpha = (filterColor >>> 24) & 0xff;
                int alpha = (sourceAlpha * filterAlpha + 127) / 255;
                return (alpha << 24) | (filterColor & 0x00ffffff);
            }
            if (blendMode == COMMAND_BLEND_MODE_MULTIPLY) {
                int alpha = ((argb >>> 24) & 0xff) * ((filterColor >>> 24) & 0xff) / 255;
                int red = ((argb >>> 16) & 0xff) * ((filterColor >>> 16) & 0xff) / 255;
                int green = ((argb >>> 8) & 0xff) * ((filterColor >>> 8) & 0xff) / 255;
                int blue = (argb & 0xff) * (filterColor & 0xff) / 255;
                return (alpha << 24) | (red << 16) | (green << 8) | blue;
            }
            return null;
        }

        private static BufferedImage applyBlendColorFilter(BufferedImage image, int filterColor, int blendMode) {
            if (blendMode == COMMAND_BLEND_MODE_SRC_IN) {
                return tintImageSrcIn(image, filterColor);
            }
            BufferedImage filtered = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Integer color = applyBlendColorFilter(image.getRGB(x, y), filterColor, blendMode);
                    if (color == null) return null;
                    filtered.setRGB(x, y, color);
                }
            }
            return filtered;
        }

        private static BufferedImage applyColorFilter(BufferedImage image, ColorFilterDescriptor descriptor) {
            if (descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER) {
                return applyBlendColorFilter(image, descriptor.argb(), descriptor.blendMode());
            }
            BufferedImage filtered = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int argb = image.getRGB(x, y);
                    if (descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER) {
                        filtered.setRGB(x, y, applyColorMatrix(argb, descriptor.matrixBits()));
                    } else if (descriptor.type() == COMMAND_EFFECT_DESCRIPTOR_LIGHTING_FILTER) {
                        filtered.setRGB(x, y, applyLightingFilter(argb, descriptor.argb(), descriptor.blendMode()));
                    } else {
                        return null;
                    }
                }
            }
            return filtered;
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

        private static java.awt.BasicStroke basicStroke(
                int width,
                int cap,
                int join,
                float miter,
                float[] dash,
                float dashPhase
        ) {
            if (width < 1 || cap < 0 || cap > 2 || join < 0 || join > 2 || miter < 0 || dashPhase < 0) {
                return null;
            }
            return new java.awt.BasicStroke(width, cap, join, Math.max(1f, miter), dash, dashPhase);
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

    private static void logHandleLifecycleMarkers(int[] commands, long contextPtr, String backend) {
        int commandEnd = commandPayloadEnd(commands);
        if (commandEnd < 0) {
            return;
        }
        Set<ColorFilterCacheKey> frameEffectDefines = new HashSet<>();
        Set<ColorFilterCacheKey> frameShaderDefines = new HashSet<>();
        int offset = COMMAND_STREAM_HEADER_SIZE;
        while (offset < commandEnd) {
            CommandRecord record = readCommandRecord(commands, offset, commandEnd);
            if (record == null) {
                return;
            }
            int argsStart = record.argsStart();
            if (record.op() == COMMAND_DEFINE_COLOR_FILTER_TINT) {
                if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || !hasRecordArgs(record, 4)) {
                    return;
                }
                long handle = commandHandle(commands[argsStart], commands[argsStart + 1]);
                markEffectHandleDefined(backend, contextPtr, handle, frameEffectDefines);
                logEffectHandleDefine(
                        backend,
                        contextPtr,
                        handle,
                        COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                        0,
                        2,
                        true
                );
            } else if (record.op() == COMMAND_DEFINE_EFFECT_DESCRIPTOR) {
                if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || !hasRecordArgs(record, 5)) {
                    return;
                }
                long handle = commandHandle(commands[argsStart], commands[argsStart + 1]);
                markEffectHandleDefined(backend, contextPtr, handle, frameEffectDefines);
                logEffectHandleDefine(
                        backend,
                        contextPtr,
                        handle,
                        commands[argsStart + 2],
                        commands[argsStart + 3],
                        commands[argsStart + 4],
                        false
                );
            } else if (record.op() == COMMAND_EVICT_COLOR_FILTER_HANDLE) {
                if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || !hasRecordArgs(record, 2)) {
                    return;
                }
                long handle = commandHandle(commands[argsStart], commands[argsStart + 1]);
                markEffectHandleEvicted(backend, contextPtr, handle);
                logEffectHandleEvict(backend, contextPtr, handle, true);
            } else if (record.op() == COMMAND_DEFINE_SHADER_DESCRIPTOR) {
                if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || !hasRecordArgs(record, 5)) {
                    return;
                }
                long handle = commandHandle(commands[argsStart], commands[argsStart + 1]);
                int descriptorType = commands[argsStart + 2];
                int payloadIntCount = commands[argsStart + 4];
                markShaderHandleDefined(backend, contextPtr, handle, frameShaderDefines);
                logShaderHandleDefine(
                        backend,
                        contextPtr,
                        handle,
                        descriptorType,
                        commands[argsStart + 3],
                        payloadIntCount
                );
                if (descriptorType == COMMAND_SHADER_DESCRIPTOR_COLOR_FILTER
                        && payloadIntCount == 4
                        && hasRecordArgs(record, 9)) {
                    long childHandle = commandHandle(commands[argsStart + 5], commands[argsStart + 6]);
                    long colorFilterHandle = commandHandle(commands[argsStart + 7], commands[argsStart + 8]);
                    logShaderHandleCacheHitIfKnown(backend, contextPtr, childHandle, record.op(), frameShaderDefines);
                    logShaderHandleUse(backend, contextPtr, childHandle, record.op());
                    logEffectHandleCacheHitIfKnown(backend, contextPtr, colorFilterHandle, record.op(), frameEffectDefines);
                    logEffectHandleUse(backend, contextPtr, colorFilterHandle, record.op());
                }
            } else if (record.op() == COMMAND_EVICT_SHADER_HANDLE) {
                if (record.recordFlags() != COMMAND_RECORD_FLAGS_NONE || !hasRecordArgs(record, 2)) {
                    return;
                }
                long handle = commandHandle(commands[argsStart], commands[argsStart + 1]);
                markShaderHandleEvicted(backend, contextPtr, handle);
                logShaderHandleEvict(backend, contextPtr, handle, true);
            } else if (record.op() == COMMAND_FILL_RECT_SHADER_REF || record.op() == COMMAND_STROKE_RECT_SHADER_REF) {
                if (!hasRecordArgs(record, 2)) {
                    return;
                }
                long handle = commandHandle(commands[argsStart], commands[argsStart + 1]);
                logShaderHandleCacheHitIfKnown(backend, contextPtr, handle, record.op(), frameShaderDefines);
                logShaderHandleUse(backend, contextPtr, handle, record.op());
            } else if (record.op() == COMMAND_FILL_RECT_COLOR_FILTER_REF) {
                if (!hasRecordArgs(record, 3)) {
                    return;
                }
                long handle = commandHandle(commands[argsStart + 1], commands[argsStart + 2]);
                logEffectHandleCacheHitIfKnown(backend, contextPtr, handle, record.op(), frameEffectDefines);
                logEffectHandleUse(backend, contextPtr, handle, record.op());
            } else if (record.op() == COMMAND_SAVE_LAYER_COLOR_FILTER_REF
                    || record.op() == COMMAND_SAVE_LAYER_IMAGE_FILTER_REF) {
                if (!hasRecordArgs(record, 7)) {
                    return;
                }
                long handle = commandHandle(commands[argsStart + 5], commands[argsStart + 6]);
                logEffectHandleCacheHitIfKnown(backend, contextPtr, handle, record.op(), frameEffectDefines);
                logEffectHandleUse(backend, contextPtr, handle, record.op());
            } else if (record.op() == COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF) {
                if (!hasRecordArgs(record, 8)) {
                    return;
                }
                long handle = commandHandle(commands[argsStart + 6], commands[argsStart + 7]);
                logEffectHandleCacheHitIfKnown(backend, contextPtr, handle, record.op(), frameEffectDefines);
                logEffectHandleUse(backend, contextPtr, handle, record.op());
            } else if (record.op() == COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF) {
                if (!hasRecordArgs(record, 16)) {
                    return;
                }
                long handle = commandHandle(commands[argsStart + 14], commands[argsStart + 15]);
                logEffectHandleCacheHitIfKnown(backend, contextPtr, handle, record.op(), frameEffectDefines);
                logEffectHandleUse(backend, contextPtr, handle, record.op());
            }
            offset = record.recordEnd();
        }
    }

    private static boolean hasRecordArgs(CommandRecord record, int argCount) {
        return record.argsStart() + argCount <= record.recordEnd();
    }

    private static void markEffectHandleDefined(
            String backend,
            long contextPtr,
            long handle,
            Set<ColorFilterCacheKey> frameDefines
    ) {
        frameDefines.add(new ColorFilterCacheKey(contextPtr, handle));
        EFFECT_HANDLE_MARKER_CACHE.add(new MarkerHandleKey(backend, contextPtr, handle));
    }

    private static void markEffectHandleEvicted(String backend, long contextPtr, long handle) {
        EFFECT_HANDLE_MARKER_CACHE.remove(new MarkerHandleKey(backend, contextPtr, handle));
    }

    private static void markShaderHandleDefined(
            String backend,
            long contextPtr,
            long handle,
            Set<ColorFilterCacheKey> frameDefines
    ) {
        frameDefines.add(new ColorFilterCacheKey(contextPtr, handle));
        SHADER_HANDLE_MARKER_CACHE.add(new MarkerHandleKey(backend, contextPtr, handle));
    }

    private static void markShaderHandleEvicted(String backend, long contextPtr, long handle) {
        SHADER_HANDLE_MARKER_CACHE.remove(new MarkerHandleKey(backend, contextPtr, handle));
    }

    private static void logEffectHandleCacheHitIfKnown(
            String backend,
            long contextPtr,
            long handle,
            int op,
            Set<ColorFilterCacheKey> frameDefines
    ) {
        if (!frameDefines.contains(new ColorFilterCacheKey(contextPtr, handle))
                && EFFECT_HANDLE_MARKER_CACHE.contains(new MarkerHandleKey(backend, contextPtr, handle))) {
            System.err.println("JBR_SKIA_INTEROP_EFFECT_HANDLE_CACHE_HIT backend=" + backend + " contextId=0x"
                    + Long.toHexString(contextPtr) + " handle=0x" + Long.toHexString(handle)
                    + " op=" + op);
        }
    }

    private static void logShaderHandleCacheHitIfKnown(
            String backend,
            long contextPtr,
            long handle,
            int op,
            Set<ColorFilterCacheKey> frameDefines
    ) {
        if (!frameDefines.contains(new ColorFilterCacheKey(contextPtr, handle))
                && SHADER_HANDLE_MARKER_CACHE.contains(new MarkerHandleKey(backend, contextPtr, handle))) {
            System.err.println("JBR_SKIA_INTEROP_SHADER_HANDLE_CACHE_HIT backend=" + backend + " contextId=0x"
                    + Long.toHexString(contextPtr) + " handle=0x" + Long.toHexString(handle)
                    + " op=" + op);
        }
    }

    private static void logEffectHandleDefine(
            String backend,
            long contextPtr,
            long handle,
            int descriptorType,
            int descriptorVersion,
            int payloadIntCount,
            boolean legacy
    ) {
        System.err.println("JBR_SKIA_INTEROP_EFFECT_HANDLE_DEFINE backend=" + backend + " contextId=0x"
                + Long.toHexString(contextPtr) + " handle=0x" + Long.toHexString(handle)
                + " type=" + descriptorType + " version=" + descriptorVersion
                + " payloadInts=" + payloadIntCount + " legacy=" + legacy);
    }

    private static void logEffectHandleEvict(String backend, long contextPtr, long handle, boolean removed) {
        System.err.println("JBR_SKIA_INTEROP_EFFECT_HANDLE_EVICT backend=" + backend + " contextId=0x"
                + Long.toHexString(contextPtr) + " handle=0x" + Long.toHexString(handle)
                + " removed=" + removed);
    }

    private static void logEffectHandleUse(String backend, long contextPtr, long handle, int op) {
        System.err.println("JBR_SKIA_INTEROP_EFFECT_HANDLE_USE backend=" + backend + " contextId=0x"
                + Long.toHexString(contextPtr) + " handle=0x" + Long.toHexString(handle)
                + " op=" + op);
    }

    private static void logShaderHandleDefine(
            String backend,
            long contextPtr,
            long handle,
            int descriptorType,
            int descriptorVersion,
            int payloadIntCount
    ) {
        System.err.println("JBR_SKIA_INTEROP_SHADER_HANDLE_DEFINE backend=" + backend + " contextId=0x"
                + Long.toHexString(contextPtr) + " handle=0x" + Long.toHexString(handle)
                + " type=" + descriptorType + " version=" + descriptorVersion
                + " payloadInts=" + payloadIntCount);
    }

    private static void logShaderHandleEvict(String backend, long contextPtr, long handle, boolean removed) {
        System.err.println("JBR_SKIA_INTEROP_SHADER_HANDLE_EVICT backend=" + backend + " contextId=0x"
                + Long.toHexString(contextPtr) + " handle=0x" + Long.toHexString(handle)
                + " removed=" + removed);
    }

    private static void logShaderHandleUse(String backend, long contextPtr, long handle, int op) {
        System.err.println("JBR_SKIA_INTEROP_SHADER_HANDLE_USE backend=" + backend + " contextId=0x"
                + Long.toHexString(contextPtr) + " handle=0x" + Long.toHexString(handle)
                + " op=" + op);
    }

    private static boolean loadNativeBridge() {
        String library = System.getProperty(NATIVE_LIBRARY_PROPERTY);
        try {
            if (library == null || library.isBlank()) {
                System.loadLibrary("jbrskiainterop");
            } else {
                System.load(library);
            }
            return true;
        } catch (RuntimeException | UnsatisfiedLinkError e) {
            System.err.println("JBR Skia interop native bridge unavailable: " + e.getMessage());
            return false;
        }
    }

    private static native boolean nativeRenderDiagnosticFrame(long nativeOpsPtr, long metalTexturePtr,
                                                             int width, int height, long frameTimeNanos);

    private static native int nativeGetNativeAbiVersion();

    private static native int nativeGetCommandStreamAbiId();

    private static native String nativeGetBuildId();

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
