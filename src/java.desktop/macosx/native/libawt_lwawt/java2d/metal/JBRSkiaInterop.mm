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

#import <Metal/Metal.h>
#include <jni.h>
#include <malloc/malloc.h>
#include <algorithm>
#include <array>
#include <chrono>
#include <cmath>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <functional>
#include <memory>
#include <mutex>
#include <string>
#include <unordered_map>
#include <vector>

#include "SkBlendMode.h"
#include "SkBitmap.h"
#include "SkCanvas.h"
#include "SkColor.h"
#include "SkColorFilter.h"
#include "SkColorSpace.h"
#include "SkData.h"
#include "SkImage.h"
#include "SkImageInfo.h"
#include "SkFont.h"
#include "SkFontMgr.h"
#include "SkMatrix.h"
#include "SkPaint.h"
#include "SkPath.h"
#include "SkPathBuilder.h"
#include "SkPicture.h"
#include "SkPixmap.h"
#include "SkRRect.h"
#include "SkRuntimeEffect.h"
#include "SkSamplingOptions.h"
#include "SkShader.h"
#include "SkString.h"
#include "SkSurface.h"
#include "SkTileMode.h"
#include "SkPoint3.h"
#include "SkShadowUtils.h"
#include "SkVertices.h"
#include "ganesh/GrBackendSurface.h"
#include "ganesh/GrDirectContext.h"
#include "ganesh/mtl/GrMtlBackendContext.h"
#include "ganesh/mtl/GrMtlBackendSurface.h"
#include "ganesh/mtl/GrMtlDirectContext.h"
#include "ganesh/mtl/GrMtlTypes.h"
#include "include/gpu/ganesh/SkSurfaceGanesh.h"
#include "include/effects/Sk1DPathEffect.h"
#include "include/effects/SkCornerPathEffect.h"
#include "include/effects/SkDashPathEffect.h"
#include "include/effects/SkGradient.h"
#include "include/effects/SkImageFilters.h"
#include "include/effects/SkPerlinNoiseShader.h"
#include "modules/skparagraph/include/FontCollection.h"
#include "modules/skparagraph/include/Paragraph.h"
#include "modules/skparagraph/include/ParagraphBuilder.h"
#include "modules/skparagraph/include/ParagraphStyle.h"
#include "modules/skparagraph/include/TextStyle.h"
#include "modules/skunicode/include/SkUnicode_icu.h"
#include "ports/SkFontMgr_mac_ct.h"

#include "MTLSurfaceDataBase.h"

static constexpr jint ABI_ID = 111;
static constexpr jint NATIVE_ABI_VERSION = 3;
static constexpr const char* BUILD_ID = "skia=m147-64a2414108;flags=macos-release-metal-poc:1;abi=111;native=3";
static constexpr size_t MAX_CACHED_RUNTIME_EFFECTS = 1024;
static constexpr jint COMMAND_STREAM_MAGIC = 1246972723;
static constexpr jint COMMAND_STREAM_HEADER_SIZE = 6;
static constexpr jint COMMAND_STREAM_FLAGS_NONE = 0;

static float commandBitsToFloat(jint bits) {
    float value;
    static_assert(sizeof(value) == sizeof(bits));
    std::memcpy(&value, &bits, sizeof(value));
    return value;
}
static constexpr jint COMMAND_COORDINATE_SPACE_SWING_USER = 1;
static constexpr jint COMMAND_PAINT_FORMAT_SOLID_ARGB = 1;
static constexpr jint COMMAND_RECORD_HEADER_SIZE_BYTES = 12;
static constexpr jint COMMAND_RECORD_FLAGS_NONE = 0;
static constexpr jint COMMAND_RECORD_FLAG_ANTIALIAS = 1;
static constexpr jint COMMAND_CLEAR = 1;
static constexpr jint COMMAND_FILL_RECT = 2;
static constexpr jint COMMAND_STROKE_LINE = 3;
static constexpr jint COMMAND_FILL_OVAL = 4;
static constexpr jint COMMAND_STROKE_OVAL = 5;
static constexpr jint COMMAND_CLEAR_RECT = 6;
static constexpr jint COMMAND_SAVE = 7;
static constexpr jint COMMAND_RESTORE = 8;
static constexpr jint COMMAND_CLIP_RECT = 9;
static constexpr jint COMMAND_CLIP_OP_INTERSECT = 0;
static constexpr jint COMMAND_CLIP_OP_DIFFERENCE = 1;
static constexpr jint COMMAND_TRANSLATE = 10;
static constexpr jint COMMAND_SCALE = 11;
static constexpr jint COMMAND_ROTATE = 12;
static constexpr jint COMMAND_SAVE_LAYER = 13;
static constexpr jint COMMAND_DRAW_IMAGE_ARGB = 14;
static constexpr jint COMMAND_DEFINE_IMAGE_ARGB = 15;
static constexpr jint COMMAND_DRAW_IMAGE_REF = 16;
static constexpr jint COMMAND_DRAW_TEXT_UTF16 = 17;
static constexpr jint COMMAND_CLEAR_IMAGE_CACHE = 18;
static constexpr jint COMMAND_DRAW_PARAGRAPH_UTF16 = 19;
static constexpr jint COMMAND_CLIP_PATH = 20;
static constexpr jint COMMAND_DRAW_PATH = 21;
static constexpr jint COMMAND_DRAW_ARC = 22;
static constexpr jint COMMAND_DRAW_ROUND_RECT = 23;
static constexpr jint COMMAND_FILL_RECT_LINEAR_GRADIENT = 24;
static constexpr jint COMMAND_FILL_ROUND_RECT_LINEAR_GRADIENT = 25;
static constexpr jint COMMAND_FILL_RECT_RADIAL_GRADIENT = 26;
static constexpr jint COMMAND_FILL_ROUND_RECT_RADIAL_GRADIENT = 27;
static constexpr jint COMMAND_FILL_PATH_LINEAR_GRADIENT = 28;
static constexpr jint COMMAND_FILL_PATH_RADIAL_GRADIENT = 29;
static constexpr jint COMMAND_FILL_RECT_SWEEP_GRADIENT = 30;
static constexpr jint COMMAND_FILL_ROUND_RECT_SWEEP_GRADIENT = 31;
static constexpr jint COMMAND_FILL_PATH_SWEEP_GRADIENT = 32;
static constexpr jint COMMAND_EVICT_IMAGE_CACHE_KEY = 33;
static constexpr jint COMMAND_FILL_RECT_IMAGE_SHADER = 34;
static constexpr jint COMMAND_STROKE_RECT_LINEAR_GRADIENT = 35;
static constexpr jint COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT = 36;
static constexpr jint COMMAND_STROKE_RECT_RADIAL_GRADIENT = 37;
static constexpr jint COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT = 38;
static constexpr jint COMMAND_STROKE_RECT_SWEEP_GRADIENT = 39;
static constexpr jint COMMAND_STROKE_ROUND_RECT_SWEEP_GRADIENT = 40;
static constexpr jint COMMAND_FILL_RECT_BLEND_MODE = 41;
static constexpr jint COMMAND_FILL_RECT_COLOR_FILTER = 42;
static constexpr jint COMMAND_STROKE_LINE_DASH_PATH_EFFECT = 43;
static constexpr jint COMMAND_SAVE_LAYER_COLOR_FILTER = 44;
static constexpr jint COMMAND_DRAW_IMAGE_REF_COLOR_FILTER = 45;
static constexpr jint COMMAND_DEFINE_COLOR_FILTER_TINT = 46;
static constexpr jint COMMAND_FILL_RECT_COLOR_FILTER_REF = 47;
static constexpr jint COMMAND_EVICT_COLOR_FILTER_HANDLE = 48;
static constexpr jint COMMAND_DEFINE_EFFECT_DESCRIPTOR = 49;
static constexpr jint COMMAND_SAVE_LAYER_BLEND_MODE = 50;
static constexpr jint COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER = 51;
static constexpr jint COMMAND_SAVE_LAYER_COLOR_FILTER_REF = 52;
static constexpr jint COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF = 53;
static constexpr jint COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF = 54;
static constexpr jint COMMAND_SAVE_LAYER_IMAGE_FILTER_REF = 55;
static constexpr jint COMMAND_DEFINE_SHADER_DESCRIPTOR = 56;
static constexpr jint COMMAND_EVICT_SHADER_HANDLE = 57;
static constexpr jint COMMAND_FILL_RECT_SHADER_REF = 58;
static constexpr jint COMMAND_STROKE_RECT_DASH_PATH_EFFECT = 59;
static constexpr jint COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT = 60;
static constexpr jint COMMAND_STROKE_PATH_DASH_PATH_EFFECT = 61;
static constexpr jint COMMAND_DRAW_PATH_PATH_EFFECT_REF = 62;
static constexpr jint COMMAND_CONCAT_MATRIX33 = 63;
static constexpr jint COMMAND_DRAW_SHADOW_PATH = 64;
static constexpr jint COMMAND_DRAW_POINTS = 65;
static constexpr jint COMMAND_DEFINE_FONT_DATA = 66;
static constexpr jint COMMAND_DRAW_VERTICES = 67;
static constexpr jint COMMAND_STROKE_PATH_LINEAR_GRADIENT = 68;
static constexpr jint COMMAND_STROKE_PATH_RADIAL_GRADIENT = 69;
static constexpr jint COMMAND_STROKE_PATH_SWEEP_GRADIENT = 70;
static constexpr jint COMMAND_STROKE_RECT_SHADER_REF = 71;
static constexpr jint COMMAND_STROKE_RECT_IMAGE_SHADER = 72;
static constexpr jint COMMAND_DEFINE_IMAGE_BITMAP = 73;
static constexpr jint COMMAND_SAVE_TRANSLATE = 74;
static constexpr jint COMMAND_RESTORE_N = 75;
static constexpr jint COMMAND_SAVE_TRANSLATE_LAYER = 76;
static constexpr jint COMMAND_DRAW_IMAGE_REF_FULL = 77;
static constexpr jint COMMAND_FILL_ROUND_RECT = 78;
static constexpr jint COMMAND_CLEAR_DRAW_IMAGE_REF_FULL = 79;
static constexpr jint COMMAND_DRAW_IMAGE_REF_FULL_DRAW_ROUND_RECT = 80;
static constexpr jint COMMAND_DRAW_IMAGE_REF_FULL_RUN = 81;
static constexpr jint COMMAND_SAVE_LAYER_CLIP_RECT = 82;
static constexpr jint COMMAND_DRAW_IMAGE_REF_FULL_FILL_RECT = 83;
static constexpr jint COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN = 84;
static constexpr jint COMMAND_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE = 85;
static constexpr jint COMMAND_DRAW_IMAGE_REF_FULL_RESTORE = 86;
static constexpr jint COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N = 87;
static constexpr jint COMMAND_DRAW_ROUND_RECT_RESTORE_N = 88;
static constexpr jint COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN_RESTORE_N = 89;
static constexpr jint COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE = 90;
static constexpr jint COMMAND_FILL_RECT_SAVE = 91;
static constexpr jint COMMAND_SAVE_FILL_RECT_SAVE = 92;
static constexpr jint COMMAND_SAVE_LAYER_SAVE_TRANSLATE = 93;
static constexpr jint COMMAND_SAVE_SAVE_LAYER_SAVE_TRANSLATE = 94;
static constexpr jint COMMAND_FILL_RECT_SAVE_LAYER_CLIP_RECT = 95;
static constexpr jint COMMAND_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE_DRAW_IMAGE_REF_FULL_RESTORE_N = 96;
static constexpr jint COMMAND_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE_DRAW_IMAGE_REF_FULL_RESTORE_N_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE = 97;
static constexpr jint COMMAND_FILL_RECT_SAVE_LAYER_CLIP_RECT_SAVE_SAVE_LAYER_SAVE_TRANSLATE = 98;
static constexpr jint COMMAND_SAVE_TRANSLATE_ROTATE = 99;
static constexpr jint COMMAND_SAVE_TRANSLATE_ROTATE_TRANSLATE_FILL_OVAL_RESTORE = 100;
static constexpr jint COMMAND_STROKE_CLOSED_POLYLINE = 101;
static constexpr jint COMMAND_STROKE_CLOSED_POLYLINE_DELTA = 102;
static constexpr jint COMMAND_STROKE_OVAL_RUN = 103;
static constexpr jint COMMAND_SAVE_TRANSLATE_ROTATE_TRANSLATE_FILL_OVAL_RESTORE_RUN = 106;
static constexpr jint COMMAND_SAVE_TRANSLATE_ROTATE_TRANSLATE_STROKE_CLOSED_POLYLINE_DELTA_RESTORE = 107;
static constexpr jint COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER = 1;
static constexpr jint COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER = 2;
static constexpr jint COMMAND_EFFECT_DESCRIPTOR_LIGHTING_FILTER = 3;
static constexpr jint COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER = 4;
static constexpr jint COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER = 5;
static constexpr jint COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT = 6;
static constexpr jint COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT = 7;
static constexpr jint COMMAND_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER = 8;
static constexpr jint COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT = 9;
static constexpr jint COMMAND_EFFECT_DESCRIPTOR_STAMPED_PATH_EFFECT = 10;
static constexpr jint COMMAND_EFFECT_DESCRIPTOR_CHAIN_PATH_EFFECT = 11;
static constexpr jint COMMAND_EFFECT_DESCRIPTOR_VERSION_1 = 1;
static constexpr jint COMMAND_SHADER_DESCRIPTOR_LINEAR_GRADIENT = 1;
static constexpr jint COMMAND_SHADER_DESCRIPTOR_RADIAL_GRADIENT = 2;
static constexpr jint COMMAND_SHADER_DESCRIPTOR_SWEEP_GRADIENT = 3;
static constexpr jint COMMAND_SHADER_DESCRIPTOR_IMAGE = 4;
static constexpr jint COMMAND_SHADER_DESCRIPTOR_COMPOSITE = 5;
static constexpr jint COMMAND_SHADER_DESCRIPTOR_RUNTIME_EFFECT = 6;
static constexpr jint COMMAND_SHADER_DESCRIPTOR_COLOR_FILTER = 7;
static constexpr jint COMMAND_SHADER_DESCRIPTOR_TRANSFORM = 8;
static constexpr jint COMMAND_SHADER_DESCRIPTOR_COLOR = 9;
static constexpr jint COMMAND_SHADER_DESCRIPTOR_PERLIN_NOISE = 10;
static constexpr jint COMMAND_SHADER_DESCRIPTOR_VERSION_1 = 1;
static constexpr jint COMMAND_BLEND_MODE_PLUS = 1;
static constexpr jint COMMAND_BLEND_MODE_SRC_IN = 2;
static constexpr jint COMMAND_BLEND_MODE_MULTIPLY = 3;
static constexpr jint COMMAND_BLEND_MODE_SCREEN = 4;
static constexpr jint COMMAND_BLEND_MODE_OVERLAY = 5;
static constexpr jint COMMAND_BLEND_MODE_DARKEN = 6;
static constexpr jint COMMAND_BLEND_MODE_LIGHTEN = 7;
static constexpr jint COMMAND_BLEND_MODE_DIFFERENCE = 8;
static constexpr jint COMMAND_BLEND_MODE_EXCLUSION = 9;
static constexpr jint COMMAND_BLEND_MODE_COLOR_DODGE = 10;
static constexpr jint COMMAND_BLEND_MODE_COLOR_BURN = 11;
static constexpr jint COMMAND_BLEND_MODE_HARDLIGHT = 12;
static constexpr jint COMMAND_BLEND_MODE_SOFTLIGHT = 13;
static constexpr jint COMMAND_BLEND_MODE_HUE = 14;
static constexpr jint COMMAND_BLEND_MODE_SATURATION = 15;
static constexpr jint COMMAND_BLEND_MODE_COLOR = 16;
static constexpr jint COMMAND_BLEND_MODE_LUMINOSITY = 17;
static constexpr jint COMMAND_BLEND_MODE_SRC_OVER = 18;
static constexpr jint COMMAND_PAINT_STYLE_FILL = 0;
static constexpr jint COMMAND_PAINT_STYLE_STROKE = 1;
static constexpr jint COMMAND_PATH_FILL_NON_ZERO = 0;
static constexpr jint COMMAND_PATH_FILL_EVEN_ODD = 1;
static constexpr jint COMMAND_PATH_VERB_MOVE = 0;
static constexpr jint COMMAND_PATH_VERB_LINE = 1;
static constexpr jint COMMAND_PATH_VERB_QUAD = 2;
static constexpr jint COMMAND_PATH_VERB_CUBIC = 3;
static constexpr jint COMMAND_PATH_VERB_CLOSE = 4;

static std::mutex gDirectContextMutex;
static std::unordered_map<void*, sk_sp<GrDirectContext>> gDirectContextsByMtlContext;

struct ImageCacheScopedKey {
    void* context;
    uint64_t image;

    bool operator==(const ImageCacheScopedKey& other) const {
        return context == other.context && image == other.image;
    }
};

struct ImageCacheScopedKeyHash {
    size_t operator()(const ImageCacheScopedKey& key) const {
        size_t contextHash = std::hash<void*>{}(key.context);
        size_t imageHash = std::hash<uint64_t>{}(key.image);
        return contextHash ^ (imageHash + 0x9e3779b97f4a7c15ULL + (contextHash << 6) + (contextHash >> 2));
    }
};

struct ColorFilterDescriptor {
    jint type = 0;
    SkColor argb;
    jint blendMode;
    std::array<SkScalar, 20> matrix;
    SkScalar sigmaX = 0;
    SkScalar sigmaY = 0;
    jint tileMode = 0;
    SkScalar dx = 0;
    SkScalar dy = 0;
    std::vector<jint> payload;
    std::shared_ptr<ColorFilterDescriptor> child;
    std::vector<std::shared_ptr<ColorFilterDescriptor>> children;
};

struct ShaderDescriptor {
    jint type = 0;
    std::vector<jint> payload;
    std::shared_ptr<ShaderDescriptor> dst;
    std::shared_ptr<ShaderDescriptor> src;
    std::shared_ptr<ShaderDescriptor> child;
    std::shared_ptr<ColorFilterDescriptor> colorFilter;
    std::vector<std::shared_ptr<ShaderDescriptor>> children;
};

struct ColorFilterScopedKey {
    void* context;
    uint64_t filter;

    bool operator==(const ColorFilterScopedKey& other) const {
        return context == other.context && filter == other.filter;
    }
};

struct ColorFilterScopedKeyHash {
    size_t operator()(const ColorFilterScopedKey& key) const {
        size_t contextHash = std::hash<void*>{}(key.context);
        size_t filterHash = std::hash<uint64_t>{}(key.filter);
        return contextHash ^ (filterHash + 0x9e3779b97f4a7c15ULL + (contextHash << 6) + (contextHash >> 2));
    }
};

static std::mutex gImageCacheMutex;
static std::unordered_map<ImageCacheScopedKey, sk_sp<SkImage>, ImageCacheScopedKeyHash> gImagesByKey;
static std::mutex gColorFilterCacheMutex;
static std::unordered_map<ColorFilterScopedKey, ColorFilterDescriptor, ColorFilterScopedKeyHash> gColorFiltersByKey;
static std::mutex gShaderCacheMutex;
static std::unordered_map<ColorFilterScopedKey, ShaderDescriptor, ColorFilterScopedKeyHash> gShadersByKey;
static std::mutex gRuntimeEffectCacheMutex;
static std::unordered_map<std::string, sk_sp<SkRuntimeEffect>> gRuntimeShaderEffectsBySource;
static std::unordered_map<std::string, sk_sp<SkRuntimeEffect>> gRuntimeColorFilterEffectsBySource;
static std::mutex gFontDataCacheMutex;
static std::unordered_map<uint64_t, sk_sp<SkTypeface>> gFontDataTypefacesByKey;
static std::mutex gParagraphDependenciesMutex;
static sk_sp<skia::textlayout::FontCollection> gParagraphFontCollection;
static sk_sp<SkUnicode> gParagraphUnicode;
static sk_sp<SkFontMgr> gCoreTextFontMgr;

@class AWTView;
@class MTLLayer;
@class MTLContext;
@class EncoderManager;

@interface EncoderManager : NSObject
- (void)endEncoder;
@end

@interface MTLContext : NSObject
@property (readonly, strong) id<MTLDevice> device;
@property (strong) id<MTLCommandQueue> commandQueue;
@property (readonly) EncoderManager* encoderManager;
@end

typedef struct _JBRSkiaMTLGraphicsConfigInfo {
    MTLContext* context;
    jint displayID;
} JBRSkiaMTLGraphicsConfigInfo;

static uint64_t imageCacheKey(jint high, jint low);
static bool parseFontDataHandle(const std::string& fontFamily, uint64_t* handle);

static sk_sp<SkFontMgr> coreTextFontMgr() {
    std::scoped_lock lock(gParagraphDependenciesMutex);
    if (gCoreTextFontMgr == nullptr) {
        gCoreTextFontMgr = SkFontMgr_New_CoreText(nullptr);
    }
    return gCoreTextFontMgr;
}

static std::vector<SkString> commandFontFamilyCandidates(const std::string& fontFamily) {
    if (fontFamily.empty()) {
        return {};
    }
    if (fontFamily == "sans-serif") {
        return {SkString(".AppleSystemUIFont"), SkString("Helvetica Neue"), SkString("Helvetica")};
    }
    if (fontFamily == "serif") {
        return {SkString(".AppleSystemUIFontSerif"), SkString("Times"), SkString("Times New Roman")};
    }
    if (fontFamily == "monospace") {
        return {SkString(".AppleSystemUIFontMonospaced"), SkString("Menlo"), SkString("Courier")};
    }
    if (fontFamily == "cursive") {
        return {SkString("Apple Chancery"), SkString("Snell Roundhand")};
    }
    return {SkString(fontFamily.c_str())};
}

static sk_sp<SkTypeface> matchCommandTypeface(const std::string& fontFamily, const SkFontStyle& fontStyle) {
    uint64_t fontDataHandle = 0;
    if (parseFontDataHandle(fontFamily, &fontDataHandle)) {
        std::scoped_lock lock(gFontDataCacheMutex);
        auto found = gFontDataTypefacesByKey.find(fontDataHandle);
        if (found != gFontDataTypefacesByKey.end() && found->second != nullptr) {
            return found->second;
        }
    }
    sk_sp<SkFontMgr> fontMgr = coreTextFontMgr();
    if (!fontFamily.empty()) {
        std::vector<SkString> candidates = commandFontFamilyCandidates(fontFamily);
        for (const SkString& candidate : candidates) {
            sk_sp<SkTypeface> typeface = fontMgr->matchFamilyStyle(candidate.c_str(), fontStyle);
            if (typeface != nullptr) {
                return typeface;
            }
        }
    }
    return fontMgr->legacyMakeTypeface(nullptr, fontStyle);
}

static sk_sp<skia::textlayout::FontCollection> paragraphFontCollection() {
    sk_sp<SkFontMgr> fontMgr = coreTextFontMgr();
    std::scoped_lock lock(gParagraphDependenciesMutex);
    if (gParagraphFontCollection == nullptr) {
        auto fontCollection = sk_make_sp<skia::textlayout::FontCollection>();
        fontCollection->setDefaultFontManager(fontMgr);
        gParagraphFontCollection = fontCollection;
    }
    return gParagraphFontCollection;
}

static sk_sp<SkUnicode> paragraphUnicode() {
    std::scoped_lock lock(gParagraphDependenciesMutex);
    if (gParagraphUnicode == nullptr) {
        gParagraphUnicode = SkUnicodes::ICU::Make();
    }
    return gParagraphUnicode;
}

static long long monotonicNanos() {
    return std::chrono::duration_cast<std::chrono::nanoseconds>(
            std::chrono::steady_clock::now().time_since_epoch()).count();
}

static bool mallocDiagnosticEnabled() {
    const char* value = std::getenv("JBR_SKIA_MALLOC_DIAGNOSTIC");
    return value != nullptr && std::strcmp(value, "0") != 0 && std::strcmp(value, "false") != 0;
}

static bool directDiagnosticModeIs(const char* expected) {
    const char* value = std::getenv("JBR_SKIA_DIRECT_DIAGNOSTIC_MODE");
    return value != nullptr && std::strcmp(value, expected) == 0;
}

static size_t defaultMallocBytesInUse() {
    malloc_statistics_t stats = {};
    malloc_zone_statistics(malloc_default_zone(), &stats);
    return stats.size_in_use;
}

static void logMallocPhase(const char* phase, size_t baseBytes, int commandCount) {
    if (!mallocDiagnosticEnabled()) {
        return;
    }
    const size_t bytes = defaultMallocBytesInUse();
    std::fprintf(stderr,
                 "JBR_SKIA_INTEROP_MALLOC phase=%s commandCount=%d defaultBytes=%zu deltaBytes=%lld\n",
                 phase,
                 commandCount,
                 bytes,
                 static_cast<long long>(bytes) - static_cast<long long>(baseBytes));
}

static void purgeUnlockedResourcesAfterFrame(GrDirectContext* directContext) {
    const char* value = std::getenv("JBR_SKIA_PURGE_UNLOCKED");
    if (directContext == nullptr || value == nullptr || std::strcmp(value, "0") == 0 ||
            std::strcmp(value, "false") == 0) {
        return;
    }
    int beforeCount = 0;
    size_t beforeBytes = 0;
    directContext->getResourceCacheUsage(&beforeCount, &beforeBytes);
    if (std::strcmp(value, "all") == 0) {
        directContext->purgeUnlockedResources(GrPurgeResourceOptions::kAllResources);
    } else {
        directContext->purgeUnlockedResources(GrPurgeResourceOptions::kScratchResourcesOnly);
    }
    int afterCount = 0;
    size_t afterBytes = 0;
    directContext->getResourceCacheUsage(&afterCount, &afterBytes);
    if (mallocDiagnosticEnabled()) {
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_RESOURCE_PURGE mode=%s beforeCount=%d beforeBytes=%zu afterCount=%d afterBytes=%zu\n",
                     value,
                     beforeCount,
                     beforeBytes,
                     afterCount,
                     afterBytes);
    }
}

typedef struct _JBRSkiaMTLSDOps {
    AWTView* peerData;
    MTLLayer* layer;
    jint argb[4];
    JBRSkiaMTLGraphicsConfigInfo* configInfo;
} JBRSkiaMTLSDOps;

static MTLContext* getContextFromNativeOps(jlong nativeOpsPtr) {
    if (nativeOpsPtr == 0) {
        return nil;
    }

    BMTLSDOps* baseOps = reinterpret_cast<BMTLSDOps*>(static_cast<uintptr_t>(nativeOpsPtr));
    if (baseOps == nullptr || baseOps->privOps == nullptr) {
        return nil;
    }

    JBRSkiaMTLSDOps* mtlOps = static_cast<JBRSkiaMTLSDOps*>(baseOps->privOps);
    if (mtlOps->configInfo == nullptr) {
        return nil;
    }
    return mtlOps->configInfo->context;
}

static void drawDiagnosticPattern(SkCanvas* canvas, int width, int height, jlong frameTimeNanos) {
    canvas->clear(SkColorSetARGB(255, 20, 12, 42));

    SkPaint linePaint;
    linePaint.setAntiAlias(true);
    linePaint.setColor(SkColorSetARGB(210, 48, 215, 186));
    linePaint.setStrokeWidth(3.0f);

    int phase = static_cast<int>((frameTimeNanos / 12000000L) % 56L);
    for (int x = -height + phase; x < width + height; x += 56) {
        canvas->drawLine(static_cast<SkScalar>(x),
                         static_cast<SkScalar>(height),
                         static_cast<SkScalar>(x + height),
                         0.0f,
                         linePaint);
    }

    SkPaint boxPaint;
    boxPaint.setAntiAlias(true);
    boxPaint.setColor(SkColorSetARGB(235, 255, 87, 120));

    int box = std::max(40, std::min(width, height) / 4);
    SkRect rect = SkRect::MakeXYWH(static_cast<SkScalar>((width - box) / 2),
                                  static_cast<SkScalar>((height - box) / 2),
                                  static_cast<SkScalar>(box),
                                  static_cast<SkScalar>(box));
    canvas->drawRRect(SkRRect::MakeRectXY(rect, 18.0f, 18.0f), boxPaint);
}

static SkColor skColorFromArgb(jint argb) {
    return SkColorSetARGB(static_cast<U8CPU>((argb >> 24) & 0xff),
                          static_cast<U8CPU>((argb >> 16) & 0xff),
                          static_cast<U8CPU>((argb >> 8) & 0xff),
                          static_cast<U8CPU>(argb & 0xff));
}

static SkScalar skScalarFromRawBits(jint bits) {
    float value;
    static_assert(sizeof(value) == sizeof(bits));
    std::memcpy(&value, &bits, sizeof(value));
    return static_cast<SkScalar>(value);
}

template <typename CommandWords>
static bool pathFromCommandData(CommandWords commands, jsize offset, jsize recordEnd, jint fillType, SkPath* path);

static bool skBlendModeForColorFilter(jint commandBlendMode, SkBlendMode* blendMode);
static sk_sp<SkColorFilter> makeDescriptorRuntimeColorFilter(const ColorFilterDescriptor& descriptor);

static sk_sp<SkColorFilter> makeDescriptorColorFilter(const ColorFilterDescriptor& descriptor) {
    if (descriptor.type == COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER) {
        SkBlendMode blendMode;
        if (!skBlendModeForColorFilter(descriptor.blendMode, &blendMode)) {
            return nullptr;
        }
        return SkColorFilters::Blend(descriptor.argb, blendMode);
    }
    if (descriptor.type == COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER) {
        return SkColorFilters::Matrix(descriptor.matrix.data());
    }
    if (descriptor.type == COMMAND_EFFECT_DESCRIPTOR_LIGHTING_FILTER) {
        return SkColorFilters::Lighting(descriptor.argb, static_cast<SkColor>(descriptor.blendMode));
    }
    if (descriptor.type == COMMAND_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER) {
        return makeDescriptorRuntimeColorFilter(descriptor);
    }
    return nullptr;
}

static bool setDescriptorColorFilter(SkPaint* paint, const ColorFilterDescriptor& descriptor) {
    sk_sp<SkColorFilter> colorFilter = makeDescriptorColorFilter(descriptor);
    if (!colorFilter) return false;
    paint->setColorFilter(std::move(colorFilter));
    return true;
}

static SkTileMode skTileModeFromCommand(jint tileMode) {
    if (tileMode == 1) return SkTileMode::kRepeat;
    if (tileMode == 2) return SkTileMode::kMirror;
    if (tileMode == 3) return SkTileMode::kDecal;
    return SkTileMode::kClamp;
}

static bool isImageFilterDescriptorType(jint type) {
    return type == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER ||
            type == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER ||
            type == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT ||
            type == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT;
}

static bool isPathEffectDescriptorType(jint type) {
    return type == COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT ||
            type == COMMAND_EFFECT_DESCRIPTOR_STAMPED_PATH_EFFECT ||
            type == COMMAND_EFFECT_DESCRIPTOR_CHAIN_PATH_EFFECT;
}

static sk_sp<SkPathEffect> makeDescriptorPathEffect(const ColorFilterDescriptor& descriptor) {
    if (descriptor.type == COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT) {
        if (descriptor.payload.size() != 1) return nullptr;
        SkScalar radius = skScalarFromRawBits(descriptor.payload[0]);
        if (!std::isfinite(radius) || radius < 0) return nullptr;
        return SkCornerPathEffect::Make(radius);
    }
    if (descriptor.type == COMMAND_EFFECT_DESCRIPTOR_STAMPED_PATH_EFFECT) {
        if (descriptor.payload.size() < 5) return nullptr;
        const SkScalar advance = skScalarFromRawBits(descriptor.payload[0]);
        const SkScalar phase = skScalarFromRawBits(descriptor.payload[1]);
        const jint style = descriptor.payload[2];
        const jint fillType = descriptor.payload[3];
        const jint pathDataLength = descriptor.payload[4];
        if (!std::isfinite(advance) || advance <= 0 ||
                !std::isfinite(phase) || phase < 0 ||
                style < 0 || style > 2 ||
                (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                pathDataLength < 0 ||
                descriptor.payload.size() != static_cast<size_t>(5 + pathDataLength)) {
            return nullptr;
        }
        SkPath path;
        if (!pathFromCommandData(descriptor.payload, 5, static_cast<jsize>(descriptor.payload.size()), fillType, &path)) {
            return nullptr;
        }
        return SkPath1DPathEffect::Make(
                path,
                advance,
                phase,
                static_cast<SkPath1DPathEffect::Style>(style));
    }
    if (descriptor.type == COMMAND_EFFECT_DESCRIPTOR_CHAIN_PATH_EFFECT) {
        if (descriptor.children.size() != 2 || !descriptor.children[0] || !descriptor.children[1]) {
            return nullptr;
        }
        sk_sp<SkPathEffect> outer = makeDescriptorPathEffect(*descriptor.children[0]);
        sk_sp<SkPathEffect> inner = makeDescriptorPathEffect(*descriptor.children[1]);
        if (!outer || !inner) {
            return nullptr;
        }
        return SkPathEffect::MakeCompose(std::move(outer), std::move(inner));
    }
    return nullptr;
}

static sk_sp<SkImageFilter> makeDescriptorImageFilter(const ColorFilterDescriptor& descriptor, int depth) {
    if (depth > 8) {
        return nullptr;
    }
    sk_sp<SkImageFilter> child;
    if (descriptor.child) {
        child = makeDescriptorImageFilter(*descriptor.child, depth + 1);
        if (!child) {
            return nullptr;
        }
    }
    if (descriptor.type == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER ||
            descriptor.type == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT) {
        if (descriptor.sigmaX < 0 || descriptor.sigmaY < 0 || descriptor.tileMode < 0 || descriptor.tileMode > 3) {
            return nullptr;
        }
        return SkImageFilters::Blur(
                descriptor.sigmaX,
                descriptor.sigmaY,
                skTileModeFromCommand(descriptor.tileMode),
                child,
                nullptr);
    }
    if (descriptor.type == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER ||
            descriptor.type == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT) {
        if (!std::isfinite(descriptor.dx) || !std::isfinite(descriptor.dy)) {
            return nullptr;
        }
        return SkImageFilters::Offset(
                descriptor.dx,
                descriptor.dy,
                child,
                nullptr);
    }
    return nullptr;
}

static bool setDescriptorImageFilter(SkPaint* paint, const ColorFilterDescriptor& descriptor) {
    sk_sp<SkImageFilter> filter = makeDescriptorImageFilter(descriptor, 0);
    if (filter) {
        paint->setImageFilter(filter);
        return true;
    }
    return false;
}

static bool skBlendMode(jint commandBlendMode, SkBlendMode* blendMode);

static bool readGradientStops(const std::vector<jint>& payload,
                              size_t offset,
                              jint colorCount,
                              SkColor4f* colors,
                              float* positions) {
    if (colorCount < 2 || colorCount > 16 || offset + static_cast<size_t>(colorCount) * 2 != payload.size()) {
        return false;
    }
    jint previousStop = -1;
    for (jint i = 0; i < colorCount; i++) {
        colors[i] = SkColor4f::FromColor(skColorFromArgb(payload[offset++]));
        const jint stop1000 = payload[offset++];
        if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
            return false;
        }
        previousStop = stop1000;
        positions[i] = static_cast<float>(stop1000) / 1000.0f;
    }
    return true;
}

static uint64_t shaderSourceHash(const std::vector<jint>& payload, size_t skslStart, jint skslLength);
static uint64_t asciiStringHash(const char* data, size_t length);
static int runtimeEffectUniformSchemaEnd(const std::vector<jint>& payload,
                                         int offset,
                                         int schemaEnd,
                                         jint namedUniformCount,
                                         jint uniformFloatCount);
static int runtimeEffectChildSchemaEnd(const std::vector<jint>& payload,
                                       int offset,
                                       int schemaEnd,
                                       jint namedChildCount,
                                       jint childCount);
static size_t runtimeEffectCacheLimitForTesting();

static sk_sp<SkRuntimeEffect> cachedRuntimeShaderEffect(
        const std::string& sksl,
        uint64_t sourceHash,
        jint uniformFloatCount,
        jint childCount) {
    {
        std::lock_guard<std::mutex> lock(gRuntimeEffectCacheMutex);
        auto cached = gRuntimeShaderEffectsBySource.find(sksl);
        if (cached != gRuntimeShaderEffectsBySource.end()) {
            std::fprintf(stderr,
                         "JBR_SKIA_INTEROP_RUNTIME_EFFECT_CACHE_HIT type=shader hash=0x%016llx skslLength=%zu uniforms=%d children=%d\n",
                         static_cast<unsigned long long>(sourceHash),
                         sksl.size(),
                         uniformFloatCount,
                         childCount);
            return cached->second;
        }
    }

    SkRuntimeEffect::Result result = SkRuntimeEffect::MakeForShader(SkString(sksl.c_str(), sksl.size()));
    if (!result.effect || !result.errorText.isEmpty()) {
        const uint64_t errorHash = asciiStringHash(result.errorText.c_str(), result.errorText.size());
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_RUNTIME_EFFECT_COMPILE_FAILED hash=0x%016llx skslLength=%zu uniforms=%d children=%d errorLength=%zu errorHash=0x%016llx\n",
                     static_cast<unsigned long long>(sourceHash),
                     sksl.size(),
                     uniformFloatCount,
                     childCount,
                     result.errorText.size(),
                     static_cast<unsigned long long>(errorHash));
        return nullptr;
    }

    std::lock_guard<std::mutex> lock(gRuntimeEffectCacheMutex);
    const size_t cacheLimit = runtimeEffectCacheLimitForTesting();
    if (gRuntimeShaderEffectsBySource.size() >= cacheLimit) {
        const auto evicted = gRuntimeShaderEffectsBySource.begin();
        const uint64_t evictedHash = asciiStringHash(evicted->first.data(), evicted->first.size());
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_RUNTIME_EFFECT_CACHE_EVICT type=shader hash=0x%016llx skslLength=%zu limit=%zu\n",
                     static_cast<unsigned long long>(evictedHash),
                     evicted->first.size(),
                     cacheLimit);
        gRuntimeShaderEffectsBySource.erase(evicted);
    }
    std::fprintf(stderr,
                 "JBR_SKIA_INTEROP_RUNTIME_EFFECT_CACHE_MISS type=shader hash=0x%016llx skslLength=%zu uniforms=%d children=%d\n",
                 static_cast<unsigned long long>(sourceHash),
                 sksl.size(),
                 uniformFloatCount,
                 childCount);
    auto cached = gRuntimeShaderEffectsBySource.emplace(sksl, result.effect).first;
    return cached->second;
}

static sk_sp<SkRuntimeEffect> cachedRuntimeColorFilterEffect(
        const std::string& sksl,
        uint64_t sourceHash,
        jint uniformFloatCount,
        jint childCount) {
    {
        std::lock_guard<std::mutex> lock(gRuntimeEffectCacheMutex);
        auto cached = gRuntimeColorFilterEffectsBySource.find(sksl);
        if (cached != gRuntimeColorFilterEffectsBySource.end()) {
            std::fprintf(stderr,
                         "JBR_SKIA_INTEROP_RUNTIME_EFFECT_CACHE_HIT type=colorFilter hash=0x%016llx skslLength=%zu uniforms=%d children=%d\n",
                         static_cast<unsigned long long>(sourceHash),
                         sksl.size(),
                         uniformFloatCount,
                         childCount);
            return cached->second;
        }
    }

    SkRuntimeEffect::Result result = SkRuntimeEffect::MakeForColorFilter(SkString(sksl.c_str(), sksl.size()));
    if (!result.effect || !result.errorText.isEmpty()) {
        const uint64_t errorHash = asciiStringHash(result.errorText.c_str(), result.errorText.size());
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_RUNTIME_COLOR_FILTER_COMPILE_FAILED hash=0x%016llx skslLength=%zu uniforms=%d errorLength=%zu errorHash=0x%016llx\n",
                     static_cast<unsigned long long>(sourceHash),
                     sksl.size(),
                     uniformFloatCount,
                     result.errorText.size(),
                     static_cast<unsigned long long>(errorHash));
        return nullptr;
    }

    std::lock_guard<std::mutex> lock(gRuntimeEffectCacheMutex);
    const size_t cacheLimit = runtimeEffectCacheLimitForTesting();
    if (gRuntimeColorFilterEffectsBySource.size() >= cacheLimit) {
        const auto evicted = gRuntimeColorFilterEffectsBySource.begin();
        const uint64_t evictedHash = asciiStringHash(evicted->first.data(), evicted->first.size());
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_RUNTIME_EFFECT_CACHE_EVICT type=colorFilter hash=0x%016llx skslLength=%zu limit=%zu\n",
                     static_cast<unsigned long long>(evictedHash),
                     evicted->first.size(),
                     cacheLimit);
        gRuntimeColorFilterEffectsBySource.erase(evicted);
    }
    std::fprintf(stderr,
                 "JBR_SKIA_INTEROP_RUNTIME_EFFECT_CACHE_MISS type=colorFilter hash=0x%016llx skslLength=%zu uniforms=%d children=%d\n",
                 static_cast<unsigned long long>(sourceHash),
                 sksl.size(),
                 uniformFloatCount,
                 childCount);
    auto cached = gRuntimeColorFilterEffectsBySource.emplace(sksl, result.effect).first;
    return cached->second;
}

static sk_sp<SkShader> makeDescriptorShader(const ShaderDescriptor& descriptor, void* imageCacheContextKey, int depth) {
    if (depth > 8) {
        return nullptr;
    }
    if (descriptor.type == COMMAND_SHADER_DESCRIPTOR_LINEAR_GRADIENT) {
        if (descriptor.payload.size() < 6) return nullptr;
        SkPoint points[2] = {
                {static_cast<SkScalar>(descriptor.payload[0]) / 1000.0f,
                 static_cast<SkScalar>(descriptor.payload[1]) / 1000.0f},
                {static_cast<SkScalar>(descriptor.payload[2]) / 1000.0f,
                 static_cast<SkScalar>(descriptor.payload[3]) / 1000.0f}
        };
        const jint tileMode = descriptor.payload[4];
        const jint colorCount = descriptor.payload[5];
        if (tileMode < 0 || tileMode > 3) return nullptr;
        SkColor4f colors[16];
        float positions[16];
        if (!readGradientStops(descriptor.payload, 6, colorCount, colors, positions)) return nullptr;
        SkGradient gradient(
                SkGradient::Colors(
                        SkSpan<const SkColor4f>(colors, colorCount),
                        SkSpan<const float>(positions, colorCount),
                        skTileModeFromCommand(tileMode)),
                SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
        return SkShaders::LinearGradient(points, gradient);
    }
    if (descriptor.type == COMMAND_SHADER_DESCRIPTOR_RADIAL_GRADIENT) {
        if (descriptor.payload.size() < 5) return nullptr;
        const SkPoint center = {
                static_cast<SkScalar>(descriptor.payload[0]) / 1000.0f,
                static_cast<SkScalar>(descriptor.payload[1]) / 1000.0f
        };
        const SkScalar radius = static_cast<SkScalar>(descriptor.payload[2]) / 1000.0f;
        const jint tileMode = descriptor.payload[3];
        const jint colorCount = descriptor.payload[4];
        if (radius <= 0 || tileMode < 0 || tileMode > 3) return nullptr;
        SkColor4f colors[16];
        float positions[16];
        if (!readGradientStops(descriptor.payload, 5, colorCount, colors, positions)) return nullptr;
        SkGradient gradient(
                SkGradient::Colors(
                        SkSpan<const SkColor4f>(colors, colorCount),
                        SkSpan<const float>(positions, colorCount),
                        skTileModeFromCommand(tileMode)),
                SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
        return SkShaders::RadialGradient(center, radius, gradient);
    }
    if (descriptor.type == COMMAND_SHADER_DESCRIPTOR_SWEEP_GRADIENT) {
        if (descriptor.payload.size() < 3) return nullptr;
        const SkPoint center = {
                static_cast<SkScalar>(descriptor.payload[0]) / 1000.0f,
                static_cast<SkScalar>(descriptor.payload[1]) / 1000.0f
        };
        const jint colorCount = descriptor.payload[2];
        SkColor4f colors[16];
        float positions[16];
        if (!readGradientStops(descriptor.payload, 3, colorCount, colors, positions)) return nullptr;
        SkGradient gradient(
                SkGradient::Colors(
                        SkSpan<const SkColor4f>(colors, colorCount),
                        SkSpan<const float>(positions, colorCount),
                        SkTileMode::kClamp),
                SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
        return SkShaders::SweepGradient(center, 0.0f, 360.0f, gradient);
    }
    if (descriptor.type == COMMAND_SHADER_DESCRIPTOR_IMAGE) {
        if (descriptor.payload.size() != 6) return nullptr;
        const uint64_t key = imageCacheKey(descriptor.payload[0], descriptor.payload[1]);
        const jint imageWidth = descriptor.payload[2];
        const jint imageHeight = descriptor.payload[3];
        const jint tileModeX = descriptor.payload[4];
        const jint tileModeY = descriptor.payload[5];
        if (imageWidth <= 0 || imageHeight <= 0 || imageWidth > 4096 || imageHeight > 4096 ||
                tileModeX < 0 || tileModeX > 3 || tileModeY < 0 || tileModeY > 3) {
            return nullptr;
        }
        sk_sp<SkImage> image;
        {
            std::lock_guard<std::mutex> lock(gImageCacheMutex);
            auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
            if (found == gImagesByKey.end()) return nullptr;
            image = found->second;
        }
        if (image->width() != imageWidth || image->height() != imageHeight) return nullptr;
        return image->makeShader(
                skTileModeFromCommand(tileModeX),
                skTileModeFromCommand(tileModeY),
                SkSamplingOptions(SkFilterMode::kLinear, SkMipmapMode::kNone),
                nullptr);
    }
    if (descriptor.type == COMMAND_SHADER_DESCRIPTOR_COLOR) {
        if (descriptor.payload.size() != 1) return nullptr;
        return SkShaders::Color(static_cast<SkColor>(descriptor.payload[0]));
    }
    if (descriptor.type == COMMAND_SHADER_DESCRIPTOR_PERLIN_NOISE) {
        if (descriptor.payload.size() != 7) return nullptr;
        const jint kind = descriptor.payload[0];
        const SkScalar baseFrequencyX = static_cast<SkScalar>(descriptor.payload[1]) / 1000000.0f;
        const SkScalar baseFrequencyY = static_cast<SkScalar>(descriptor.payload[2]) / 1000000.0f;
        const jint numOctaves = descriptor.payload[3];
        const SkScalar seed = static_cast<SkScalar>(descriptor.payload[4]) / 1000.0f;
        const jint tileWidth = descriptor.payload[5];
        const jint tileHeight = descriptor.payload[6];
        if (kind < 0 || kind > 1 ||
                baseFrequencyX <= 0.0f || baseFrequencyY <= 0.0f ||
                numOctaves < 1 || numOctaves > 16 ||
                tileWidth < 0 || tileHeight < 0 || tileWidth > 4096 || tileHeight > 4096) {
            return nullptr;
        }
        const SkISize tileSize = SkISize::Make(tileWidth, tileHeight);
        if (kind == 0) {
            return SkShaders::MakeFractalNoise(baseFrequencyX, baseFrequencyY, numOctaves, seed, &tileSize);
        }
        return SkShaders::MakeTurbulence(baseFrequencyX, baseFrequencyY, numOctaves, seed, &tileSize);
    }
    if (descriptor.type == COMMAND_SHADER_DESCRIPTOR_COMPOSITE) {
        if (descriptor.payload.size() != 5 || !descriptor.dst || !descriptor.src) return nullptr;
        SkBlendMode blendMode;
        if (!skBlendMode(descriptor.payload[4], &blendMode)) return nullptr;
        sk_sp<SkShader> dst = makeDescriptorShader(*descriptor.dst, imageCacheContextKey, depth + 1);
        sk_sp<SkShader> src = makeDescriptorShader(*descriptor.src, imageCacheContextKey, depth + 1);
        if (!dst || !src) return nullptr;
        return SkShaders::Blend(blendMode, dst, src);
    }
    if (descriptor.type == COMMAND_SHADER_DESCRIPTOR_COLOR_FILTER) {
        if (descriptor.payload.size() != 4 || !descriptor.child || !descriptor.colorFilter) return nullptr;
        sk_sp<SkShader> shader = makeDescriptorShader(*descriptor.child, imageCacheContextKey, depth + 1);
        sk_sp<SkColorFilter> colorFilter = makeDescriptorColorFilter(*descriptor.colorFilter);
        if (!shader || !colorFilter) return nullptr;
        return shader->makeWithColorFilter(colorFilter);
    }
    if (descriptor.type == COMMAND_SHADER_DESCRIPTOR_TRANSFORM) {
        if (descriptor.payload.size() != 11 || !descriptor.child) return nullptr;
        sk_sp<SkShader> child = makeDescriptorShader(*descriptor.child, imageCacheContextKey, depth + 1);
        if (!child) return nullptr;
        const SkMatrix matrix = SkMatrix::MakeAll(
                static_cast<SkScalar>(descriptor.payload[2]) / 1000.0f,
                static_cast<SkScalar>(descriptor.payload[3]) / 1000.0f,
                static_cast<SkScalar>(descriptor.payload[4]) / 1000.0f,
                static_cast<SkScalar>(descriptor.payload[5]) / 1000.0f,
                static_cast<SkScalar>(descriptor.payload[6]) / 1000.0f,
                static_cast<SkScalar>(descriptor.payload[7]) / 1000.0f,
                static_cast<SkScalar>(descriptor.payload[8]) / 1000.0f,
                static_cast<SkScalar>(descriptor.payload[9]) / 1000.0f,
                static_cast<SkScalar>(descriptor.payload[10]) / 1000.0f);
        return child->makeWithLocalMatrix(matrix);
    }
    if (descriptor.type == COMMAND_SHADER_DESCRIPTOR_RUNTIME_EFFECT) {
        if (descriptor.payload.size() < 7) return nullptr;
        const jint skslLength = descriptor.payload[0];
        const jint uniformFloatCount = descriptor.payload[1];
        const jint childCount = descriptor.payload[2];
        const jint namedUniformCount = descriptor.payload[3];
        const jint namedChildCount = descriptor.payload[4];
        if (skslLength <= 0 ||
                skslLength > 4096 ||
                uniformFloatCount < 0 ||
                uniformFloatCount > 256 ||
                childCount < 0 ||
                childCount > 8 ||
                namedUniformCount < 0 ||
                namedUniformCount > 16 ||
                namedChildCount < 0 ||
                namedChildCount > childCount ||
                descriptor.children.size() != static_cast<size_t>(childCount) ||
                descriptor.payload.size() < static_cast<size_t>(7 + childCount * 2 + skslLength + uniformFloatCount)) {
            return nullptr;
        }
        std::vector<sk_sp<SkShader>> childShaders;
        childShaders.reserve(static_cast<size_t>(childCount));
        for (const auto& child : descriptor.children) {
            if (!child) return nullptr;
            sk_sp<SkShader> shader = makeDescriptorShader(*child, imageCacheContextKey, depth + 1);
            if (!shader) return nullptr;
            childShaders.push_back(shader);
        }
        const int schemaStart = 7 + childCount * 2;
        const int schemaEnd = static_cast<int>(descriptor.payload.size()) - skslLength - uniformFloatCount;
        const int childSchemaStart = runtimeEffectUniformSchemaEnd(
                descriptor.payload, schemaStart, schemaEnd, namedUniformCount, uniformFloatCount);
        const int skslStartInt = runtimeEffectChildSchemaEnd(
                descriptor.payload, childSchemaStart, schemaEnd, namedChildCount, childCount);
        if (skslStartInt < 0 || skslStartInt + skslLength + uniformFloatCount != static_cast<int>(descriptor.payload.size())) {
            return nullptr;
        }
        const size_t skslStart = static_cast<size_t>(skslStartInt);
        const uint64_t expectedHash = imageCacheKey(descriptor.payload[5], descriptor.payload[6]);
        if (shaderSourceHash(descriptor.payload, skslStart, skslLength) != expectedHash) return nullptr;
        std::string sksl;
        sksl.reserve(static_cast<size_t>(skslLength));
        for (jint i = 0; i < skslLength; i++) {
            const jint code = descriptor.payload[skslStart + i];
            if (code <= 0 || code > 127) return nullptr;
            sksl.push_back(static_cast<char>(code));
        }
        sk_sp<SkRuntimeEffect> effect = cachedRuntimeShaderEffect(sksl, expectedHash, uniformFloatCount, childCount);
        if (!effect) return nullptr;
        sk_sp<SkData> uniformData;
        if (uniformFloatCount == 0) {
            uniformData = SkData::MakeEmpty();
        } else {
            uniformData = SkData::MakeWithCopy(
                    descriptor.payload.data() + skslStart + skslLength,
                    static_cast<size_t>(uniformFloatCount) * sizeof(jint));
        }
        jint namedUniformFloatTotal = 0;
        int uniformSchemaOffset = schemaStart;
        for (jint i = 0; i < namedUniformCount; i++) {
            uniformSchemaOffset++;
            namedUniformFloatTotal += descriptor.payload[static_cast<size_t>(uniformSchemaOffset++)];
            const jint nameLength = descriptor.payload[static_cast<size_t>(uniformSchemaOffset++)];
            uniformSchemaOffset += nameLength;
        }
        const bool canUseBuilder = (namedUniformCount > 0 || namedChildCount > 0) &&
                (namedUniformCount == 0 || namedUniformFloatTotal == uniformFloatCount) &&
                (childCount == 0 || namedChildCount == childCount);
        if (canUseBuilder) {
            SkRuntimeEffectBuilder builder(effect);
            int uniformOffset = schemaStart;
            for (jint i = 0; i < namedUniformCount; i++) {
                const jint floatOffset = descriptor.payload[static_cast<size_t>(uniformOffset++)];
                const jint floatCount = descriptor.payload[static_cast<size_t>(uniformOffset++)];
                const jint nameLength = descriptor.payload[static_cast<size_t>(uniformOffset++)];
                std::string name;
                name.reserve(static_cast<size_t>(nameLength));
                for (jint charIndex = 0; charIndex < nameLength; charIndex++) {
                    name.push_back(static_cast<char>(descriptor.payload[static_cast<size_t>(uniformOffset++)]));
                }
                const float* values = reinterpret_cast<const float*>(
                        descriptor.payload.data() + skslStart + skslLength + floatOffset);
                if (!builder.uniform(name).set<float>(values, floatCount)) {
                    std::fprintf(stderr,
                                 "JBR_SKIA_INTEROP_RUNTIME_EFFECT_BUILD_FAILED hash=0x%016llx stage=uniform-set nameHash=0x%016llx skslLength=%d uniforms=%d children=%d namedUniforms=%d namedChildren=%d\n",
                                 static_cast<unsigned long long>(expectedHash),
                                 static_cast<unsigned long long>(asciiStringHash(name.data(), name.size())),
                                 skslLength,
                                 uniformFloatCount,
                                 childCount,
                                 namedUniformCount,
                                 namedChildCount);
                    return nullptr;
                }
            }
            int childOffset = childSchemaStart;
            for (jint i = 0; i < namedChildCount; i++) {
                const jint childIndex = descriptor.payload[static_cast<size_t>(childOffset++)];
                const jint nameLength = descriptor.payload[static_cast<size_t>(childOffset++)];
                std::string name;
                name.reserve(static_cast<size_t>(nameLength));
                for (jint charIndex = 0; charIndex < nameLength; charIndex++) {
                    name.push_back(static_cast<char>(descriptor.payload[static_cast<size_t>(childOffset++)]));
                }
                const SkRuntimeEffect::Child* child = effect->findChild(name);
                if (child == nullptr) {
                    std::fprintf(stderr,
                                 "JBR_SKIA_INTEROP_RUNTIME_EFFECT_BUILD_FAILED hash=0x%016llx stage=missing-child nameHash=0x%016llx skslLength=%d uniforms=%d children=%d namedUniforms=%d namedChildren=%d\n",
                                 static_cast<unsigned long long>(expectedHash),
                                 static_cast<unsigned long long>(asciiStringHash(name.data(), name.size())),
                                 skslLength,
                                 uniformFloatCount,
                                 childCount,
                                 namedUniformCount,
                                 namedChildCount);
                    return nullptr;
                }
                if (child->type != SkRuntimeEffect::ChildType::kShader) {
                    std::fprintf(stderr,
                                 "JBR_SKIA_INTEROP_RUNTIME_EFFECT_BUILD_FAILED hash=0x%016llx stage=child-type nameHash=0x%016llx skslLength=%d uniforms=%d children=%d namedUniforms=%d namedChildren=%d\n",
                                 static_cast<unsigned long long>(expectedHash),
                                 static_cast<unsigned long long>(asciiStringHash(name.data(), name.size())),
                                 skslLength,
                                 uniformFloatCount,
                                 childCount,
                                 namedUniformCount,
                                 namedChildCount);
                    return nullptr;
                }
                builder.child(name) = childShaders[static_cast<size_t>(childIndex)];
            }
            sk_sp<SkShader> shader = builder.makeShader(nullptr);
            if (!shader) {
                std::fprintf(stderr,
                             "JBR_SKIA_INTEROP_RUNTIME_EFFECT_BUILD_FAILED hash=0x%016llx stage=make-shader skslLength=%d uniforms=%d children=%d namedUniforms=%d namedChildren=%d\n",
                             static_cast<unsigned long long>(expectedHash),
                             skslLength,
                             uniformFloatCount,
                             childCount,
                             namedUniformCount,
                             namedChildCount);
            }
            return shader;
        }
        auto effectChildren = effect->children();
        if (effectChildren.size() != childShaders.size()) {
            std::fprintf(stderr,
                         "JBR_SKIA_INTEROP_RUNTIME_EFFECT_BUILD_FAILED hash=0x%016llx stage=child-count skslLength=%d uniforms=%d children=%d namedUniforms=%d namedChildren=%d effectChildren=%zu\n",
                         static_cast<unsigned long long>(expectedHash),
                         skslLength,
                         uniformFloatCount,
                         childCount,
                         namedUniformCount,
                         namedChildCount,
                         effectChildren.size());
            return nullptr;
        }
        for (size_t i = 0; i < effectChildren.size(); i++) {
            if (effectChildren[i].type != SkRuntimeEffect::ChildType::kShader) {
                std::fprintf(stderr,
                             "JBR_SKIA_INTEROP_RUNTIME_EFFECT_BUILD_FAILED hash=0x%016llx stage=positional-child-type childIndex=%zu skslLength=%d uniforms=%d children=%d namedUniforms=%d namedChildren=%d\n",
                             static_cast<unsigned long long>(expectedHash),
                             i,
                             skslLength,
                             uniformFloatCount,
                             childCount,
                             namedUniformCount,
                             namedChildCount);
                return nullptr;
            }
        }
        sk_sp<SkShader> shader = effect->makeShader(
                uniformData,
                childShaders.empty() ? nullptr : childShaders.data(),
                childShaders.size(),
                nullptr);
        if (!shader) {
            std::fprintf(stderr,
                         "JBR_SKIA_INTEROP_RUNTIME_EFFECT_BUILD_FAILED hash=0x%016llx stage=make-shader-positional skslLength=%d uniforms=%d children=%d namedUniforms=%d namedChildren=%d\n",
                         static_cast<unsigned long long>(expectedHash),
                         skslLength,
                         uniformFloatCount,
                         childCount,
                         namedUniformCount,
                         namedChildCount);
        }
        return shader;
    }
    return nullptr;
}

static sk_sp<SkColorFilter> makeDescriptorRuntimeColorFilter(const ColorFilterDescriptor& descriptor) {
    if (descriptor.type != COMMAND_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER || descriptor.payload.size() < 7) {
        return nullptr;
    }
    const jint skslLength = descriptor.payload[0];
    const jint uniformFloatCount = descriptor.payload[1];
    const jint childCount = descriptor.payload[2];
    const jint namedUniformCount = descriptor.payload[3];
    const jint namedChildCount = descriptor.payload[4];
    if (skslLength <= 0 ||
            skslLength > 4096 ||
            uniformFloatCount < 0 ||
            uniformFloatCount > 256 ||
            childCount < 0 ||
            childCount > 8 ||
            namedUniformCount < 0 ||
            namedUniformCount > 16 ||
            namedChildCount < 0 ||
            namedChildCount > 8 ||
            descriptor.children.size() != static_cast<size_t>(childCount) ||
            descriptor.payload.size() < static_cast<size_t>(7 + childCount * 2 + skslLength + uniformFloatCount)) {
        return nullptr;
    }
    const int schemaEnd = static_cast<int>(descriptor.payload.size()) - skslLength - uniformFloatCount;
    const int childSchemaStart = runtimeEffectUniformSchemaEnd(
            descriptor.payload, 7 + childCount * 2, schemaEnd, namedUniformCount, uniformFloatCount);
    const int skslStartInt = runtimeEffectChildSchemaEnd(
            descriptor.payload, childSchemaStart, schemaEnd, namedChildCount, childCount);
    if (skslStartInt < 0 ||
            skslStartInt + skslLength + uniformFloatCount != static_cast<int>(descriptor.payload.size())) {
        return nullptr;
    }
    const size_t skslStart = static_cast<size_t>(skslStartInt);
    const uint64_t expectedHash = imageCacheKey(descriptor.payload[5], descriptor.payload[6]);
    if (shaderSourceHash(descriptor.payload, skslStart, skslLength) != expectedHash) return nullptr;
    std::string sksl;
    sksl.reserve(static_cast<size_t>(skslLength));
    for (jint i = 0; i < skslLength; i++) {
        const jint code = descriptor.payload[skslStart + i];
        if (code <= 0 || code > 127) return nullptr;
        sksl.push_back(static_cast<char>(code));
    }
    sk_sp<SkRuntimeEffect> effect = cachedRuntimeColorFilterEffect(sksl, expectedHash, uniformFloatCount, childCount);
    if (!effect) return nullptr;
    sk_sp<SkData> uniformData;
    if (uniformFloatCount == 0) {
        uniformData = SkData::MakeEmpty();
    } else {
        uniformData = SkData::MakeWithCopy(
                descriptor.payload.data() + skslStart + skslLength,
                static_cast<size_t>(uniformFloatCount) * sizeof(jint));
    }
    std::vector<sk_sp<SkColorFilter>> children;
    children.reserve(static_cast<size_t>(childCount));
    for (const auto& childDescriptor : descriptor.children) {
        if (!childDescriptor || isImageFilterDescriptorType(childDescriptor->type)) {
            return nullptr;
        }
        sk_sp<SkColorFilter> child = makeDescriptorColorFilter(*childDescriptor);
        if (!child) return nullptr;
        children.push_back(std::move(child));
    }
    auto effectChildren = effect->children();
    if (effectChildren.size() != children.size()) {
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_RUNTIME_COLOR_FILTER_BUILD_FAILED hash=0x%016llx stage=child-count skslLength=%d uniforms=%d children=%d namedUniforms=%d namedChildren=%d effectChildren=%zu\n",
                     static_cast<unsigned long long>(expectedHash),
                     skslLength,
                     uniformFloatCount,
                     childCount,
                     namedUniformCount,
                     namedChildCount,
                     effectChildren.size());
        return nullptr;
    }
    for (size_t i = 0; i < effectChildren.size(); i++) {
        if (effectChildren[i].type != SkRuntimeEffect::ChildType::kColorFilter) {
            std::fprintf(stderr,
                         "JBR_SKIA_INTEROP_RUNTIME_COLOR_FILTER_BUILD_FAILED hash=0x%016llx stage=positional-child-type childIndex=%zu skslLength=%d uniforms=%d children=%d namedUniforms=%d namedChildren=%d\n",
                         static_cast<unsigned long long>(expectedHash),
                         i,
                         skslLength,
                         uniformFloatCount,
                         childCount,
                         namedUniformCount,
                         namedChildCount);
            return nullptr;
        }
    }
    sk_sp<SkColorFilter> colorFilter = effect->makeColorFilter(uniformData, children.data(), static_cast<size_t>(childCount));
    if (!colorFilter) {
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_RUNTIME_COLOR_FILTER_BUILD_FAILED hash=0x%016llx stage=make-color-filter skslLength=%d uniforms=%d children=%d namedUniforms=%d namedChildren=%d\n",
                     static_cast<unsigned long long>(expectedHash),
                     skslLength,
                     uniformFloatCount,
                     childCount,
                     namedUniformCount,
                     namedChildCount);
    }
    return colorFilter;
}

static jsize recordLengthFromBytes(jint recordByteLength) {
    if (recordByteLength < COMMAND_RECORD_HEADER_SIZE_BYTES || recordByteLength % static_cast<jint>(sizeof(jint)) != 0) {
        return -1;
    }
    return recordByteLength / static_cast<jint>(sizeof(jint));
}

template <typename CommandWords>
static bool pathFromCommandData(CommandWords commands, jsize offset, jsize recordEnd, jint fillType, SkPath* path) {
    SkPathBuilder builder(fillType == COMMAND_PATH_FILL_EVEN_ODD ? SkPathFillType::kEvenOdd : SkPathFillType::kWinding);
    while (offset < recordEnd) {
        jint verb = commands[offset++];
        if (verb == COMMAND_PATH_VERB_MOVE) {
            if (offset + 2 > recordEnd) return false;
            SkScalar x = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar y = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            builder.moveTo(x, y);
        } else if (verb == COMMAND_PATH_VERB_LINE) {
            if (offset + 2 > recordEnd) return false;
            SkScalar x = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar y = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            builder.lineTo(x, y);
        } else if (verb == COMMAND_PATH_VERB_QUAD) {
            if (offset + 4 > recordEnd) return false;
            SkScalar x1 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar y1 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar x2 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar y2 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            builder.quadTo(x1, y1, x2, y2);
        } else if (verb == COMMAND_PATH_VERB_CUBIC) {
            if (offset + 6 > recordEnd) return false;
            SkScalar x1 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar y1 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar x2 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar y2 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar x3 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            SkScalar y3 = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
            builder.cubicTo(x1, y1, x2, y2, x3, y3);
        } else if (verb == COMMAND_PATH_VERB_CLOSE) {
            builder.close();
        } else {
            return false;
        }
    }
    if (offset != recordEnd) {
        return false;
    }
    *path = builder.detach();
    return true;
}

static jint decodeLittleEndianInt(const jbyte* bytes, jsize offset) {
    return static_cast<jint>(static_cast<unsigned char>(bytes[offset]))
            | (static_cast<jint>(static_cast<unsigned char>(bytes[offset + 1])) << 8)
            | (static_cast<jint>(static_cast<unsigned char>(bytes[offset + 2])) << 16)
            | (static_cast<jint>(bytes[offset + 3]) << 24);
}

static bool isValidStrokeMetadata(jint strokeWidth, jint strokeCap, jint strokeJoin, jint strokeMiter1000) {
    return strokeWidth >= 1
            && strokeCap >= 0
            && strokeCap <= 2
            && strokeJoin >= 0
            && strokeJoin <= 2
            && strokeMiter1000 >= 0;
}

static uint64_t imageCacheKey(jint high, jint low) {
    return (static_cast<uint64_t>(static_cast<uint32_t>(high)) << 32)
            | static_cast<uint32_t>(low);
}

static bool parseFontDataHandle(const std::string& fontFamily, uint64_t* handle) {
    static constexpr const char* prefix = "jbr-font-data:";
    static constexpr size_t prefixLength = 14;
    if (fontFamily.rfind(prefix, 0) != 0) {
        return false;
    }
    size_t separator = fontFamily.find(':', prefixLength);
    if (separator == std::string::npos || separator + 1 >= fontFamily.size()) {
        return false;
    }
    char* end = nullptr;
    long high = std::strtol(fontFamily.substr(prefixLength, separator - prefixLength).c_str(), &end, 10);
    if (end == nullptr || *end != '\0') {
        return false;
    }
    long low = std::strtol(fontFamily.substr(separator + 1).c_str(), &end, 10);
    if (end == nullptr || *end != '\0') {
        return false;
    }
    *handle = imageCacheKey(static_cast<jint>(high), static_cast<jint>(low));
    return true;
}

static uint64_t shaderSourceHash(const std::vector<jint>& payload, size_t skslStart, jint skslLength) {
    uint64_t hash = 14695981039346656037ULL;
    for (jint i = 0; i < skslLength; i++) {
        hash ^= static_cast<uint8_t>(payload[skslStart + i]);
        hash *= 1099511628211ULL;
    }
    return hash;
}

static uint64_t asciiStringHash(const char* data, size_t length) {
    uint64_t hash = 14695981039346656037ULL;
    for (size_t i = 0; i < length; i++) {
        hash ^= static_cast<uint8_t>(data[i]);
        hash *= 1099511628211ULL;
    }
    return hash;
}

static size_t runtimeEffectCacheLimitForTesting() {
    const char* value = std::getenv("JBR_SKIA_RUNTIME_EFFECT_CACHE_LIMIT_FOR_TEST");
    if (value == nullptr || value[0] == '\0') {
        return MAX_CACHED_RUNTIME_EFFECTS;
    }
    char* end = nullptr;
    const unsigned long parsed = std::strtoul(value, &end, 10);
    if (end == value || end == nullptr || *end != '\0' || parsed == 0 || parsed > MAX_CACHED_RUNTIME_EFFECTS) {
        return MAX_CACHED_RUNTIME_EFFECTS;
    }
    return static_cast<size_t>(parsed);
}

static bool isValidRuntimeEffectUniformName(const std::vector<jint>& payload, int offset, jint length) {
    for (jint i = 0; i < length; i++) {
        const jint code = payload[static_cast<size_t>(offset + i)];
        const bool valid = code == '_' ||
                (code >= 'A' && code <= 'Z') ||
                (code >= 'a' && code <= 'z') ||
                (i > 0 && code >= '0' && code <= '9');
        if (!valid) return false;
    }
    return true;
}

static int runtimeEffectUniformSchemaEnd(const std::vector<jint>& payload,
                                         int offset,
                                         int schemaEnd,
                                         jint namedUniformCount,
                                         jint uniformFloatCount) {
    for (jint i = 0; i < namedUniformCount; i++) {
        if (offset + 3 > schemaEnd) return -1;
        const jint floatOffset = payload[static_cast<size_t>(offset++)];
        const jint floatCount = payload[static_cast<size_t>(offset++)];
        const jint nameLength = payload[static_cast<size_t>(offset++)];
        if (floatOffset < 0 ||
                floatCount <= 0 ||
                floatOffset > uniformFloatCount - floatCount ||
                nameLength <= 0 ||
                nameLength > 64 ||
                offset + nameLength > schemaEnd ||
                !isValidRuntimeEffectUniformName(payload, offset, nameLength)) {
            return -1;
        }
        offset += nameLength;
    }
    return offset;
}

static int runtimeEffectChildSchemaEnd(const std::vector<jint>& payload,
                                       int offset,
                                       int schemaEnd,
                                       jint namedChildCount,
                                       jint childCount) {
    if (offset < 0) return -1;
    std::vector<bool> seen(static_cast<size_t>(childCount), false);
    for (jint i = 0; i < namedChildCount; i++) {
        if (offset + 2 > schemaEnd) return -1;
        const jint childIndex = payload[static_cast<size_t>(offset++)];
        const jint nameLength = payload[static_cast<size_t>(offset++)];
        if (childIndex < 0 ||
                childIndex >= childCount ||
                seen[static_cast<size_t>(childIndex)] ||
                nameLength <= 0 ||
                nameLength > 64 ||
                offset + nameLength > schemaEnd ||
                !isValidRuntimeEffectUniformName(payload, offset, nameLength)) {
            return -1;
        }
        seen[static_cast<size_t>(childIndex)] = true;
        offset += nameLength;
    }
    return offset == schemaEnd ? offset : -1;
}

static int clearImageCacheForContext(void* contextKey) {
    int cleared = 0;
    for (auto it = gImagesByKey.begin(); it != gImagesByKey.end();) {
        if (it->first.context == contextKey) {
            it = gImagesByKey.erase(it);
            cleared++;
        } else {
            ++it;
        }
    }
    return cleared;
}

static bool appendUtf8CodePoint(std::string& text, uint32_t codePoint) {
    if (codePoint <= 0x7f) {
        text.push_back(static_cast<char>(codePoint));
        return true;
    }
    if (codePoint <= 0x7ff) {
        text.push_back(static_cast<char>(0xc0 | (codePoint >> 6)));
        text.push_back(static_cast<char>(0x80 | (codePoint & 0x3f)));
        return true;
    }
    if (codePoint >= 0xd800 && codePoint <= 0xdfff) {
        return false;
    }
    if (codePoint <= 0xffff) {
        text.push_back(static_cast<char>(0xe0 | (codePoint >> 12)));
        text.push_back(static_cast<char>(0x80 | ((codePoint >> 6) & 0x3f)));
        text.push_back(static_cast<char>(0x80 | (codePoint & 0x3f)));
        return true;
    }
    if (codePoint <= 0x10ffff) {
        text.push_back(static_cast<char>(0xf0 | (codePoint >> 18)));
        text.push_back(static_cast<char>(0x80 | ((codePoint >> 12) & 0x3f)));
        text.push_back(static_cast<char>(0x80 | ((codePoint >> 6) & 0x3f)));
        text.push_back(static_cast<char>(0x80 | (codePoint & 0x3f)));
        return true;
    }
    return false;
}

template<typename CommandWords>
static bool appendUtf16CommandText(std::string& text, CommandWords commands, jsize& offset, jint charCount) {
    text.reserve(static_cast<size_t>(charCount) * 3);
    for (jint index = 0; index < charCount; index++) {
        jint codeUnit = commands[offset++];
        if (codeUnit < 0 || codeUnit > 0xffff) {
            return false;
        }
        if (codeUnit >= 0xd800 && codeUnit <= 0xdbff) {
            if (index + 1 >= charCount) {
                return false;
            }
            jint lowSurrogate = commands[offset++];
            index++;
            if (lowSurrogate < 0xdc00 || lowSurrogate > 0xdfff) {
                return false;
            }
            uint32_t codePoint = 0x10000
                    + ((static_cast<uint32_t>(codeUnit) - 0xd800) << 10)
                    + (static_cast<uint32_t>(lowSurrogate) - 0xdc00);
            if (!appendUtf8CodePoint(text, codePoint)) {
                return false;
            }
        } else if (!appendUtf8CodePoint(text, static_cast<uint32_t>(codeUnit))) {
            return false;
        }
    }
    return true;
}

struct IntCommandWords {
    const jint* words;

    jint operator[](jsize index) const {
        return words[index];
    }
};

struct LittleEndianByteCommandWords {
    const jbyte* bytes;

    jint operator[](jsize index) const {
        return decodeLittleEndianInt(bytes, index * static_cast<jsize>(sizeof(jint)));
    }
};

template<typename CommandWords>
static sk_sp<SkImage> makeRasterImage(CommandWords commands, jsize pixelOffset, jint imageWidth, jint imageHeight, jint pixelCount) {
    SkImageInfo imageInfo = SkImageInfo::Make(
            imageWidth,
            imageHeight,
            kN32_SkColorType,
            kUnpremul_SkAlphaType,
            SkColorSpace::MakeSRGB());
    std::vector<jint> pixels(static_cast<size_t>(pixelCount));
    for (jint pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++) {
        pixels[static_cast<size_t>(pixelIndex)] = commands[pixelOffset + pixelIndex];
    }
    SkPixmap pixmap(imageInfo, pixels.data(), static_cast<size_t>(imageWidth) * sizeof(jint));
    return SkImages::RasterFromPixmapCopy(pixmap);
}

static bool skBlendModeForFill(jint commandBlendMode, SkBlendMode* blendMode) {
    switch (commandBlendMode) {
        case COMMAND_BLEND_MODE_PLUS:
            *blendMode = SkBlendMode::kPlus;
            return true;
        case COMMAND_BLEND_MODE_MULTIPLY:
            *blendMode = SkBlendMode::kMultiply;
            return true;
        case COMMAND_BLEND_MODE_SCREEN:
            *blendMode = SkBlendMode::kScreen;
            return true;
        case COMMAND_BLEND_MODE_OVERLAY:
            *blendMode = SkBlendMode::kOverlay;
            return true;
        case COMMAND_BLEND_MODE_DARKEN:
            *blendMode = SkBlendMode::kDarken;
            return true;
        case COMMAND_BLEND_MODE_LIGHTEN:
            *blendMode = SkBlendMode::kLighten;
            return true;
        case COMMAND_BLEND_MODE_DIFFERENCE:
            *blendMode = SkBlendMode::kDifference;
            return true;
        case COMMAND_BLEND_MODE_EXCLUSION:
            *blendMode = SkBlendMode::kExclusion;
            return true;
        case COMMAND_BLEND_MODE_COLOR_DODGE:
            *blendMode = SkBlendMode::kColorDodge;
            return true;
        case COMMAND_BLEND_MODE_COLOR_BURN:
            *blendMode = SkBlendMode::kColorBurn;
            return true;
        case COMMAND_BLEND_MODE_HARDLIGHT:
            *blendMode = SkBlendMode::kHardLight;
            return true;
        case COMMAND_BLEND_MODE_SOFTLIGHT:
            *blendMode = SkBlendMode::kSoftLight;
            return true;
        case COMMAND_BLEND_MODE_HUE:
            *blendMode = SkBlendMode::kHue;
            return true;
        case COMMAND_BLEND_MODE_SATURATION:
            *blendMode = SkBlendMode::kSaturation;
            return true;
        case COMMAND_BLEND_MODE_COLOR:
            *blendMode = SkBlendMode::kColor;
            return true;
        case COMMAND_BLEND_MODE_LUMINOSITY:
            *blendMode = SkBlendMode::kLuminosity;
            return true;
        default:
            return false;
    }
}

static bool skBlendMode(jint commandBlendMode, SkBlendMode* blendMode) {
    if (commandBlendMode == COMMAND_BLEND_MODE_SRC_OVER) {
        *blendMode = SkBlendMode::kSrcOver;
        return true;
    }
    return skBlendModeForFill(commandBlendMode, blendMode);
}

static bool skBlendModeForColorFilter(jint commandBlendMode, SkBlendMode* blendMode) {
    if (commandBlendMode == COMMAND_BLEND_MODE_SRC_IN) {
        *blendMode = SkBlendMode::kSrcIn;
        return true;
    }
    return skBlendMode(commandBlendMode, blendMode);
}

static bool drawImage(SkCanvas* canvas,
                      const sk_sp<SkImage>& image,
                      jint recordFlags,
                      SkScalar srcLeft,
                      SkScalar srcTop,
                      SkScalar srcRight,
                      SkScalar srcBottom,
                      SkScalar dstLeft,
                      SkScalar dstTop,
                      SkScalar dstRight,
                      SkScalar dstBottom,
                      jint alpha1000,
                      SkColor colorFilter = SK_ColorTRANSPARENT,
                      jint colorFilterBlendMode = 0) {
    if (image == nullptr || alpha1000 < 0 || alpha1000 > 1000) {
        return false;
    }
    SkPaint imagePaint;
    imagePaint.setAlphaf(static_cast<float>(alpha1000) / 1000.0f);
    if (colorFilterBlendMode != 0) {
        SkBlendMode blendMode;
        if (!skBlendModeForColorFilter(colorFilterBlendMode, &blendMode)) {
            return false;
        }
        imagePaint.setColorFilter(SkColorFilters::Blend(colorFilter, blendMode));
    }
    SkSamplingOptions sampling = (recordFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0
            ? SkSamplingOptions(SkFilterMode::kLinear, SkMipmapMode::kNone)
            : SkSamplingOptions(SkFilterMode::kNearest, SkMipmapMode::kNone);
    canvas->drawImageRect(image,
                          SkRect::MakeLTRB(srcLeft, srcTop, srcRight, srcBottom),
                          SkRect::MakeLTRB(dstLeft, dstTop, dstRight, dstBottom),
                          sampling,
                          &imagePaint,
                          SkCanvas::kStrict_SrcRectConstraint);
    return true;
}

static bool drawImageWithDescriptorColorFilter(SkCanvas* canvas,
                                               const sk_sp<SkImage>& image,
                                               jint recordFlags,
                                               SkScalar srcLeft,
                                               SkScalar srcTop,
                                               SkScalar srcRight,
                                               SkScalar srcBottom,
                                               SkScalar dstLeft,
                                               SkScalar dstTop,
                                               SkScalar dstRight,
                                               SkScalar dstBottom,
                                               jint alpha1000,
                                               const ColorFilterDescriptor& descriptor) {
    if (image == nullptr || alpha1000 < 0 || alpha1000 > 1000) {
        return false;
    }
    SkPaint imagePaint;
    imagePaint.setAlphaf(static_cast<float>(alpha1000) / 1000.0f);
    if (!setDescriptorColorFilter(&imagePaint, descriptor)) {
        return false;
    }
    SkSamplingOptions sampling = (recordFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0
            ? SkSamplingOptions(SkFilterMode::kLinear, SkMipmapMode::kNone)
            : SkSamplingOptions(SkFilterMode::kNearest, SkMipmapMode::kNone);
    canvas->drawImageRect(image,
                          SkRect::MakeLTRB(srcLeft, srcTop, srcRight, srcBottom),
                          SkRect::MakeLTRB(dstLeft, dstTop, dstRight, dstBottom),
                          sampling,
                          &imagePaint,
                          SkCanvas::kStrict_SrcRectConstraint);
    return true;
}

struct CommandReplayMetrics {
    int paragraphCommands = 0;
    long long paragraphNanos = 0;
    int shadowCommands = 0;
};

template<typename CommandWords>
static bool drawCommandList(SkCanvas* canvas,
                            CommandWords commands,
                            jsize commandCount,
                            int width,
                            int height,
                            void* imageCacheContextKey,
                            CommandReplayMetrics* metrics = nullptr) {
    if (commandCount < COMMAND_STREAM_HEADER_SIZE ||
            commands[0] != COMMAND_STREAM_MAGIC ||
            commands[1] != ABI_ID ||
            commands[2] != COMMAND_STREAM_FLAGS_NONE ||
            commands[4] != COMMAND_COORDINATE_SPACE_SWING_USER ||
            commands[5] != COMMAND_PAINT_FORMAT_SOLID_ARGB) {
        return false;
    }
    jint payloadLength = commands[3];
    if (payloadLength < 0 || payloadLength != commandCount - COMMAND_STREAM_HEADER_SIZE) {
        return false;
    }

    jsize offset = COMMAND_STREAM_HEADER_SIZE;
    jsize commandEnd = COMMAND_STREAM_HEADER_SIZE + payloadLength;
    while (offset < commandEnd) {
        jsize recordStart = offset;
        jint op = commands[offset++];
        if (offset >= commandEnd) {
            return false;
        }
        jint recordByteLength = commands[offset++];
        if (offset >= commandEnd) {
            return false;
        }
        jint recordFlags = commands[offset++];
        jsize recordLength = recordLengthFromBytes(recordByteLength);
        jsize recordEnd = recordStart + recordLength;
        if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || recordLength < 3 || recordEnd > commandEnd) {
            return false;
        }
        bool antiAlias = (recordFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0;
        switch (op) {
            case COMMAND_SAVE: {
                if (offset != recordEnd) {
                    return false;
                }
                canvas->save();
                break;
            }
            case COMMAND_RESTORE: {
                if (offset != recordEnd) {
                    return false;
                }
                if (canvas->getSaveCount() <= 1) {
                    return false;
                }
                canvas->restore();
                break;
            }
            case COMMAND_RESTORE_N: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 1 != recordEnd) {
                    return false;
                }
                const jint count = commands[offset++];
                if (count <= 0 || canvas->getSaveCount() <= count) {
                    return false;
                }
                for (jint restoreIndex = 0; restoreIndex < count; restoreIndex++) {
                    canvas->restore();
                }
                break;
            }
            case COMMAND_CLEAR_IMAGE_CACHE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset != recordEnd) {
                    return false;
                }
                std::lock_guard<std::mutex> lock(gImageCacheMutex);
                int cleared = clearImageCacheForContext(imageCacheContextKey);
                std::fprintf(stderr,
                             "JBR_SKIA_INTEROP_IMAGE_CACHE_CLEAR backend=native contextId=%p cleared=%d\n",
                             imageCacheContextKey,
                             cleared);
                break;
            }
            case COMMAND_EVICT_IMAGE_CACHE_KEY: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) {
                    return false;
                }
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                size_t removed;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    removed = gImagesByKey.erase(ImageCacheScopedKey{imageCacheContextKey, key});
                }
                std::fprintf(stderr,
                             "JBR_SKIA_INTEROP_IMAGE_CACHE_EVICT backend=native contextId=%p key=0x%016llx removed=%s\n",
                             imageCacheContextKey,
                             static_cast<unsigned long long>(key),
                             removed > 0 ? "true" : "false");
                break;
            }
            case COMMAND_EVICT_COLOR_FILTER_HANDLE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) {
                    return false;
                }
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                std::lock_guard<std::mutex> lock(gColorFilterCacheMutex);
                gColorFiltersByKey.erase(ColorFilterScopedKey{imageCacheContextKey, key});
                break;
            }
            case COMMAND_EVICT_SHADER_HANDLE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) {
                    return false;
                }
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                std::lock_guard<std::mutex> lock(gShaderCacheMutex);
                gShadersByKey.erase(ColorFilterScopedKey{imageCacheContextKey, key});
                break;
            }
            case COMMAND_CLIP_RECT: {
                if (offset + 5 != recordEnd) {
                    return false;
                }
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint rectWidth = commands[offset++];
                jint rectHeight = commands[offset++];
                jint clipOp = commands[offset++];
                if (clipOp != COMMAND_CLIP_OP_INTERSECT && clipOp != COMMAND_CLIP_OP_DIFFERENCE) {
                    return false;
                }
                canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                  static_cast<SkScalar>(y),
                                                  static_cast<SkScalar>(rectWidth),
                                                  static_cast<SkScalar>(rectHeight)),
                                 clipOp == COMMAND_CLIP_OP_DIFFERENCE ? SkClipOp::kDifference : SkClipOp::kIntersect,
                                 antiAlias);
                break;
            }
            case COMMAND_CLIP_PATH: {
                if (offset + 3 > recordEnd) {
                    return false;
                }
                jint clipOp = commands[offset++];
                jint fillType = commands[offset++];
                jint pathDataLength = commands[offset++];
                if ((clipOp != COMMAND_CLIP_OP_INTERSECT && clipOp != COMMAND_CLIP_OP_DIFFERENCE) ||
                        (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                        pathDataLength < 0 ||
                        offset + pathDataLength != recordEnd) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, recordEnd, fillType, &path)) {
                    return false;
                }
                offset = recordEnd;
                canvas->clipPath(path,
                                 clipOp == COMMAND_CLIP_OP_DIFFERENCE ? SkClipOp::kDifference : SkClipOp::kIntersect,
                                 antiAlias);
                break;
            }
            case COMMAND_DRAW_PATH: {
                if (offset + 8 > recordEnd) {
                    return false;
                }
                const jint paintStyle = commands[offset++];
                const jint argb = commands[offset++];
                const jint strokeWidth = commands[offset++];
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                const jint fillType = commands[offset++];
                const jint pathDataLength = commands[offset++];
                if ((paintStyle != COMMAND_PAINT_STYLE_FILL && paintStyle != COMMAND_PAINT_STYLE_STROKE) ||
                        (paintStyle == COMMAND_PAINT_STYLE_STROKE && !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000)) ||
                        (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                        pathDataLength < 0 ||
                        offset + pathDataLength != recordEnd) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, recordEnd, fillType, &path)) {
                    return false;
                }
                offset = recordEnd;
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(argb));
                if (paintStyle == COMMAND_PAINT_STYLE_STROKE) {
                    paint.setStyle(SkPaint::kStroke_Style);
                    paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                    paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                    paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                    paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                }
                canvas->drawPath(path, paint);
                break;
            }
            case COMMAND_STROKE_CLOSED_POLYLINE: {
                if (offset + 6 > recordEnd) {
                    return false;
                }
                const jint argb = commands[offset++];
                const jint strokeWidth = commands[offset++];
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                const jint pointCount = commands[offset++];
                if (!isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000) ||
                        pointCount < 2 ||
                        pointCount > 4096 ||
                        offset + pointCount * 2 != recordEnd) {
                    return false;
                }
                SkPathBuilder builder(SkPathFillType::kWinding);
                SkScalar x = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar y = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                builder.moveTo(x, y);
                for (jint i = 1; i < pointCount; ++i) {
                    x = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    y = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    builder.lineTo(x, y);
                }
                builder.close();
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setColor(skColorFromArgb(argb));
                paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                canvas->drawPath(builder.detach(), paint);
                break;
            }
            case COMMAND_STROKE_CLOSED_POLYLINE_DELTA: {
                if (offset + 8 > recordEnd) {
                    return false;
                }
                const jint argb = commands[offset++];
                const jint strokeWidth = commands[offset++];
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                const jint pointCount = commands[offset++];
                jint x1000 = commands[offset++];
                jint y1000 = commands[offset++];
                if (!isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000) ||
                        pointCount < 2 ||
                        pointCount > 4096 ||
                        offset + pointCount - 1 != recordEnd) {
                    return false;
                }
                SkPathBuilder builder(SkPathFillType::kWinding);
                builder.moveTo(static_cast<SkScalar>(x1000) / 1000.0f,
                               static_cast<SkScalar>(y1000) / 1000.0f);
                for (jint i = 1; i < pointCount; ++i) {
                    const jint packedDelta = commands[offset++];
                    x1000 += static_cast<int16_t>((packedDelta >> 16) & 0xffff);
                    y1000 += static_cast<int16_t>(packedDelta & 0xffff);
                    builder.lineTo(static_cast<SkScalar>(x1000) / 1000.0f,
                                   static_cast<SkScalar>(y1000) / 1000.0f);
                }
                builder.close();
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setColor(skColorFromArgb(argb));
                paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                canvas->drawPath(builder.detach(), paint);
                break;
            }
            case COMMAND_SAVE_TRANSLATE_ROTATE_TRANSLATE_STROKE_CLOSED_POLYLINE_DELTA_RESTORE: {
                if (offset + 13 > recordEnd) {
                    return false;
                }
                const SkScalar dx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar degrees = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar nestedDx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar nestedDy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint argb = commands[offset++];
                const jint strokeWidth = commands[offset++];
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                const jint pointCount = commands[offset++];
                jint x1000 = commands[offset++];
                jint y1000 = commands[offset++];
                if (!isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000) ||
                        pointCount < 2 ||
                        pointCount > 4096 ||
                        offset + pointCount - 1 != recordEnd) {
                    return false;
                }
                SkPathBuilder builder(SkPathFillType::kWinding);
                builder.moveTo(static_cast<SkScalar>(x1000) / 1000.0f,
                               static_cast<SkScalar>(y1000) / 1000.0f);
                for (jint i = 1; i < pointCount; ++i) {
                    const jint packedDelta = commands[offset++];
                    x1000 += static_cast<int16_t>((packedDelta >> 16) & 0xffff);
                    y1000 += static_cast<int16_t>(packedDelta & 0xffff);
                    builder.lineTo(static_cast<SkScalar>(x1000) / 1000.0f,
                                   static_cast<SkScalar>(y1000) / 1000.0f);
                }
                builder.close();
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setColor(skColorFromArgb(argb));
                paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                canvas->save();
                canvas->translate(dx, dy);
                canvas->rotate(degrees);
                canvas->translate(nestedDx, nestedDy);
                canvas->drawPath(builder.detach(), paint);
                canvas->restore();
                break;
            }
            case COMMAND_DRAW_PATH_PATH_EFFECT_REF: {
                if (offset + 10 > recordEnd) {
                    return false;
                }
                const jint paintStyle = commands[offset++];
                const jint argb = commands[offset++];
                const jint strokeWidth = commands[offset++];
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                const uint64_t handle = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint fillType = commands[offset++];
                const jint pathDataLength = commands[offset++];
                if ((paintStyle != COMMAND_PAINT_STYLE_FILL && paintStyle != COMMAND_PAINT_STYLE_STROKE) ||
                        (paintStyle == COMMAND_PAINT_STYLE_STROKE && !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000)) ||
                        (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                        pathDataLength < 0 ||
                        offset + pathDataLength != recordEnd) {
                    return false;
                }
                ColorFilterDescriptor descriptor;
                {
                    std::lock_guard<std::mutex> lock(gColorFilterCacheMutex);
                    auto cached = gColorFiltersByKey.find(ColorFilterScopedKey{imageCacheContextKey, handle});
                    if (cached == gColorFiltersByKey.end() || !isPathEffectDescriptorType(cached->second.type)) {
                        return false;
                    }
                    descriptor = cached->second;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, recordEnd, fillType, &path)) {
                    return false;
                }
                offset = recordEnd;
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(argb));
                sk_sp<SkPathEffect> pathEffect = makeDescriptorPathEffect(descriptor);
                if (!pathEffect) {
                    return false;
                }
                paint.setPathEffect(std::move(pathEffect));
                if (paintStyle == COMMAND_PAINT_STYLE_STROKE) {
                    paint.setStyle(SkPaint::kStroke_Style);
                    paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                    paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                    paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                    paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                }
                canvas->drawPath(path, paint);
                break;
            }
            case COMMAND_FILL_PATH_LINEAR_GRADIENT: {
                if (offset + 2 > recordEnd) {
                    return false;
                }
                const jint fillType = commands[offset++];
                const jint pathDataLength = commands[offset++];
                const jsize pathEnd = offset + pathDataLength;
                if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                        pathDataLength < 0 ||
                        pathDataLength > 4096 ||
                        pathEnd + 10 > recordEnd) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, pathEnd, fillType, &path)) {
                    return false;
                }
                offset = pathEnd;
                SkPoint points[2] = {
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f},
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f}
                };
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::LinearGradient(points, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawPath(path, paint);
                break;
            }
            case COMMAND_FILL_PATH_RADIAL_GRADIENT: {
                if (offset + 2 > recordEnd) {
                    return false;
                }
                const jint fillType = commands[offset++];
                const jint pathDataLength = commands[offset++];
                const jsize pathEnd = offset + pathDataLength;
                if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                        pathDataLength < 0 ||
                        pathDataLength > 4096 ||
                        pathEnd + 9 > recordEnd) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, pathEnd, fillType, &path)) {
                    return false;
                }
                offset = pathEnd;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const SkScalar radius = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (radius <= 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::RadialGradient(center, radius, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawPath(path, paint);
                break;
            }
            case COMMAND_FILL_PATH_SWEEP_GRADIENT: {
                if (offset + 2 > recordEnd) {
                    return false;
                }
                const jint fillType = commands[offset++];
                const jint pathDataLength = commands[offset++];
                const jsize pathEnd = offset + pathDataLength;
                if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                        pathDataLength < 0 ||
                        pathDataLength > 4096 ||
                        pathEnd + 7 > recordEnd) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, pathEnd, fillType, &path)) {
                    return false;
                }
                offset = pathEnd;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const jint colorCount = commands[offset++];
                if (colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                SkTileMode::kClamp),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::SweepGradient(center, 0.0f, 360.0f, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawPath(path, paint);
                break;
            }
            case COMMAND_STROKE_PATH_LINEAR_GRADIENT: {
                if (offset + 6 > recordEnd) {
                    return false;
                }
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                const jint fillType = commands[offset++];
                const jint pathDataLength = commands[offset++];
                const jsize pathEnd = offset + pathDataLength;
                if (strokeWidth <= 0.0f || strokeCap < 0 || strokeCap > 2 ||
                        strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0 ||
                        (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                        pathDataLength < 0 ||
                        pathDataLength > 4096 ||
                        pathEnd + 10 > recordEnd) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, pathEnd, fillType, &path)) {
                    return false;
                }
                offset = pathEnd;
                SkPoint points[2] = {
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f},
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f}
                };
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                const SkScalar strokeMiter = static_cast<SkScalar>(strokeMiter1000) / 1000.0f;
                paint.setStrokeMiter(strokeMiter < 1.0f ? 1.0f : strokeMiter);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::LinearGradient(points, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawPath(path, paint);
                break;
            }
            case COMMAND_STROKE_PATH_RADIAL_GRADIENT: {
                if (offset + 6 > recordEnd) {
                    return false;
                }
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                const jint fillType = commands[offset++];
                const jint pathDataLength = commands[offset++];
                const jsize pathEnd = offset + pathDataLength;
                if (strokeWidth <= 0.0f || strokeCap < 0 || strokeCap > 2 ||
                        strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0 ||
                        (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                        pathDataLength < 0 ||
                        pathDataLength > 4096 ||
                        pathEnd + 9 > recordEnd) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, pathEnd, fillType, &path)) {
                    return false;
                }
                offset = pathEnd;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const SkScalar radius = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (radius <= 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                const SkScalar strokeMiter = static_cast<SkScalar>(strokeMiter1000) / 1000.0f;
                paint.setStrokeMiter(strokeMiter < 1.0f ? 1.0f : strokeMiter);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::RadialGradient(center, radius, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawPath(path, paint);
                break;
            }
            case COMMAND_STROKE_PATH_SWEEP_GRADIENT: {
                if (offset + 6 > recordEnd) {
                    return false;
                }
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                const jint fillType = commands[offset++];
                const jint pathDataLength = commands[offset++];
                const jsize pathEnd = offset + pathDataLength;
                if (strokeWidth <= 0.0f || strokeCap < 0 || strokeCap > 2 ||
                        strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0 ||
                        (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                        pathDataLength < 0 ||
                        pathDataLength > 4096 ||
                        pathEnd + 7 > recordEnd) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, pathEnd, fillType, &path)) {
                    return false;
                }
                offset = pathEnd;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const jint colorCount = commands[offset++];
                if (colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                const SkScalar strokeMiter = static_cast<SkScalar>(strokeMiter1000) / 1000.0f;
                paint.setStrokeMiter(strokeMiter < 1.0f ? 1.0f : strokeMiter);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                SkTileMode::kClamp),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::SweepGradient(center, 0.0f, 360.0f, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawPath(path, paint);
                break;
            }
            case COMMAND_DRAW_ARC: {
                if (offset + 13 != recordEnd) {
                    return false;
                }
                const jint paintStyle = commands[offset++];
                const jint argb = commands[offset++];
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar startAngle = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar sweepAngle = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint useCenter = commands[offset++];
                const jint strokeWidth = commands[offset++];
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                if ((paintStyle != COMMAND_PAINT_STYLE_FILL && paintStyle != COMMAND_PAINT_STYLE_STROKE) ||
                        right < left ||
                        bottom < top ||
                        (useCenter != 0 && useCenter != 1) ||
                        (paintStyle == COMMAND_PAINT_STYLE_STROKE && !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000))) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(argb));
                if (paintStyle == COMMAND_PAINT_STYLE_STROKE) {
                    paint.setStyle(SkPaint::kStroke_Style);
                    paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                    paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                    paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                    paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                }
                canvas->drawArc(SkRect::MakeLTRB(left, top, right, bottom),
                                startAngle,
                                sweepAngle,
                                useCenter == 1,
                                paint);
                break;
            }
            case COMMAND_DRAW_ROUND_RECT:
            case COMMAND_DRAW_ROUND_RECT_RESTORE_N: {
                if (offset + (op == COMMAND_DRAW_ROUND_RECT_RESTORE_N ? 13 : 12) != recordEnd) {
                    return false;
                }
                const jint paintStyle = commands[offset++];
                const jint argb = commands[offset++];
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeWidth = commands[offset++];
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                const jint restoreCount = op == COMMAND_DRAW_ROUND_RECT_RESTORE_N ? commands[offset++] : 0;
                if ((paintStyle != COMMAND_PAINT_STYLE_FILL && paintStyle != COMMAND_PAINT_STYLE_STROKE) ||
                        right < left ||
                        bottom < top ||
                        radiusX < 0 ||
                        radiusY < 0 ||
                        (paintStyle == COMMAND_PAINT_STYLE_STROKE && !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000)) ||
                        restoreCount < 0 ||
                        (op == COMMAND_DRAW_ROUND_RECT_RESTORE_N && restoreCount == 0)) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(argb));
                if (paintStyle == COMMAND_PAINT_STYLE_STROKE) {
                    paint.setStyle(SkPaint::kStroke_Style);
                    paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                    paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                    paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                    paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                }
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                if (restoreCount > 0) {
                    if (canvas->getSaveCount() <= restoreCount) {
                        return false;
                    }
                    for (jint restoreIndex = 0; restoreIndex < restoreCount; restoreIndex++) {
                        canvas->restore();
                    }
                }
                break;
            }
            case COMMAND_FILL_ROUND_RECT: {
                if (offset + 7 != recordEnd) {
                    return false;
                }
                const jint argb = commands[offset++];
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                if (right < left || bottom < top || radiusX < 0 || radiusY < 0) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(argb));
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                break;
            }
            case COMMAND_FILL_RECT_LINEAR_GRADIENT: {
                if (offset + 10 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkPoint points[2] = {
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f},
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f}
                };
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::LinearGradient(points, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_STROKE_RECT_LINEAR_GRADIENT: {
                if (offset + 14 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                SkPoint points[2] = {
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f},
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f}
                };
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || strokeWidth <= 0.0f ||
                        strokeCap < 0 || strokeCap > 2 || strokeJoin < 0 || strokeJoin > 2 ||
                        strokeMiter1000 < 0 || tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                const SkScalar strokeMiter = static_cast<SkScalar>(strokeMiter1000) / 1000.0f;
                paint.setStrokeMiter(strokeMiter < 1.0f ? 1.0f : strokeMiter);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::LinearGradient(points, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT: {
                if (offset + 16 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                SkPoint points[2] = {
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f},
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f}
                };
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || radiusX < 0 || radiusY < 0 ||
                        strokeWidth <= 0.0f || strokeCap < 0 || strokeCap > 2 ||
                        strokeJoin < 0 || strokeJoin > 2 || strokeMiter1000 < 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                const SkScalar strokeMiter = static_cast<SkScalar>(strokeMiter1000) / 1000.0f;
                paint.setStrokeMiter(strokeMiter < 1.0f ? 1.0f : strokeMiter);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::LinearGradient(points, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                break;
            }
            case COMMAND_FILL_ROUND_RECT_LINEAR_GRADIENT: {
                if (offset + 12 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkPoint points[2] = {
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f},
                        {static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                         static_cast<SkScalar>(commands[offset++]) / 1000.0f}
                };
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || radiusX < 0 || radiusY < 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::LinearGradient(points, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                break;
            }
            case COMMAND_FILL_RECT_RADIAL_GRADIENT: {
                if (offset + 9 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const SkScalar radius = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || radius <= 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::RadialGradient(center, radius, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_STROKE_RECT_RADIAL_GRADIENT: {
                if (offset + 13 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const SkScalar strokeMiter = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const SkScalar radius = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top ||
                        strokeWidth <= 0 || strokeCap < 0 || strokeCap > 2 ||
                        strokeJoin < 0 || strokeJoin > 2 || strokeMiter < 0 ||
                        radius <= 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(std::max<SkScalar>(1.0f, strokeMiter));
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::RadialGradient(center, radius, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT: {
                if (offset + 15 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const SkScalar strokeMiter = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const SkScalar radius = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || radiusX < 0 || radiusY < 0 ||
                        strokeWidth <= 0 || strokeCap < 0 || strokeCap > 2 ||
                        strokeJoin < 0 || strokeJoin > 2 || strokeMiter < 0 ||
                        radius <= 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(std::max<SkScalar>(1.0f, strokeMiter));
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::RadialGradient(center, radius, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                break;
            }
            case COMMAND_FILL_RECT_SWEEP_GRADIENT: {
                if (offset + 7 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                SkTileMode::kClamp),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::SweepGradient(center, 0.0f, 360.0f, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_STROKE_RECT_SWEEP_GRADIENT: {
                if (offset + 11 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const SkScalar strokeMiter = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top ||
                        strokeWidth <= 0 || strokeCap < 0 || strokeCap > 2 ||
                        strokeJoin < 0 || strokeJoin > 2 || strokeMiter < 0 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(std::max<SkScalar>(1.0f, strokeMiter));
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                SkTileMode::kClamp),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::SweepGradient(center, 0.0f, 360.0f, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_STROKE_ROUND_RECT_SWEEP_GRADIENT: {
                if (offset + 13 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const SkScalar strokeMiter = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || radiusX < 0 || radiusY < 0 ||
                        strokeWidth <= 0 || strokeCap < 0 || strokeCap > 2 ||
                        strokeJoin < 0 || strokeJoin > 2 || strokeMiter < 0 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(std::max<SkScalar>(1.0f, strokeMiter));
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                SkTileMode::kClamp),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::SweepGradient(center, 0.0f, 360.0f, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                break;
            }
            case COMMAND_FILL_ROUND_RECT_SWEEP_GRADIENT: {
                if (offset + 9 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || radiusX < 0 || radiusY < 0 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                SkTileMode::kClamp),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::SweepGradient(center, 0.0f, 360.0f, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                break;
            }
            case COMMAND_FILL_ROUND_RECT_RADIAL_GRADIENT: {
                if (offset + 11 > recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkPoint center = {
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f,
                        static_cast<SkScalar>(commands[offset++]) / 1000.0f
                };
                const SkScalar radius = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint tileMode = commands[offset++];
                const jint colorCount = commands[offset++];
                if (right < left || bottom < top || radiusX < 0 || radiusY < 0 || radius <= 0 ||
                        tileMode < 0 || tileMode > 3 ||
                        colorCount < 2 || colorCount > 16 ||
                        offset + colorCount * 2 != recordEnd) {
                    return false;
                }
                SkColor4f colors[16];
                float positions[16];
                jint previousStop = -1;
                for (jint i = 0; i < colorCount; i++) {
                    colors[i] = SkColor4f::FromColor(skColorFromArgb(commands[offset++]));
                    const jint stop1000 = commands[offset++];
                    if (stop1000 < 0 || stop1000 > 1000 || stop1000 <= previousStop) {
                        return false;
                    }
                    previousStop = stop1000;
                    positions[i] = static_cast<float>(stop1000) / 1000.0f;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                SkGradient gradient(
                        SkGradient::Colors(
                                SkSpan<const SkColor4f>(colors, colorCount),
                                SkSpan<const float>(positions, colorCount),
                                skTileModeFromCommand(tileMode)),
                        SkGradient::Interpolation{SkGradient::Interpolation::InPremul::kYes});
                paint.setShader(SkShaders::RadialGradient(center, radius, gradient));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                break;
            }
            case COMMAND_TRANSLATE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) {
                    return false;
                }
                SkScalar dx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar dy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                canvas->translate(dx, dy);
                break;
            }
            case COMMAND_SAVE_TRANSLATE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) {
                    return false;
                }
                SkScalar dx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar dy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                canvas->save();
                canvas->translate(dx, dy);
                break;
            }
            case COMMAND_SAVE_TRANSLATE_ROTATE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 3 != recordEnd) {
                    return false;
                }
                SkScalar dx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar dy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar degrees = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                canvas->save();
                canvas->translate(dx, dy);
                canvas->rotate(degrees);
                break;
            }
            case COMMAND_SAVE_TRANSLATE_ROTATE_TRANSLATE_FILL_OVAL_RESTORE: {
                if (offset + 10 != recordEnd) {
                    return false;
                }
                SkScalar dx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar dy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar degrees = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar nestedDx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar nestedDy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint ovalWidth = commands[offset++];
                jint ovalHeight = commands[offset++];
                SkRect rect = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                              static_cast<SkScalar>(y),
                                              static_cast<SkScalar>(ovalWidth),
                                              static_cast<SkScalar>(ovalHeight));
                canvas->save();
                canvas->translate(dx, dy);
                canvas->rotate(degrees);
                canvas->translate(nestedDx, nestedDy);
                canvas->drawOval(rect, paint);
                canvas->restore();
                break;
            }
            case COMMAND_SAVE_TRANSLATE_ROTATE_TRANSLATE_FILL_OVAL_RESTORE_RUN: {
                const jint count = commands[offset++];
                if (count <= 1 || offset + count * 10 != recordEnd) {
                    return false;
                }
                for (jint i = 0; i < count; i++) {
                    SkScalar dx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    SkScalar dy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    SkScalar degrees = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    SkScalar nestedDx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    SkScalar nestedDy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    SkPaint paint;
                    paint.setAntiAlias(antiAlias);
                    paint.setColor(skColorFromArgb(commands[offset++]));
                    jint x = commands[offset++];
                    jint y = commands[offset++];
                    jint ovalWidth = commands[offset++];
                    jint ovalHeight = commands[offset++];
                    if (ovalWidth < 0 || ovalHeight < 0) {
                        return false;
                    }
                    SkRect rect = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                  static_cast<SkScalar>(y),
                                                  static_cast<SkScalar>(ovalWidth),
                                                  static_cast<SkScalar>(ovalHeight));
                    canvas->save();
                    canvas->translate(dx, dy);
                    canvas->rotate(degrees);
                    canvas->translate(nestedDx, nestedDy);
                    canvas->drawOval(rect, paint);
                    canvas->restore();
                }
                break;
            }
            case COMMAND_SAVE_TRANSLATE_LAYER: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 7 != recordEnd) {
                    return false;
                }
                SkScalar dx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar dy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint layerWidth = commands[offset++];
                jint layerHeight = commands[offset++];
                jint alpha1000 = commands[offset++];
                if (layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000) {
                    return false;
                }
                canvas->save();
                canvas->translate(dx, dy);
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                canvas->saveLayerAlphaf(&bounds, static_cast<float>(alpha1000) / 1000.0f);
                break;
            }
            case COMMAND_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 9 != recordEnd) {
                    return false;
                }
                SkScalar layerDx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar layerDy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint layerWidth = commands[offset++];
                jint layerHeight = commands[offset++];
                jint alpha1000 = commands[offset++];
                SkScalar nestedDx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar nestedDy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                if (layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000) {
                    return false;
                }
                canvas->save();
                canvas->translate(layerDx, layerDy);
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                canvas->saveLayerAlphaf(&bounds, static_cast<float>(alpha1000) / 1000.0f);
                canvas->save();
                canvas->translate(nestedDx, nestedDy);
                break;
            }
            case COMMAND_SCALE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 2 != recordEnd) {
                    return false;
                }
                SkScalar sx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar sy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                canvas->scale(sx, sy);
                break;
            }
            case COMMAND_ROTATE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 1 != recordEnd) {
                    return false;
                }
                SkScalar degrees = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                canvas->rotate(degrees);
                break;
            }
            case COMMAND_CONCAT_MATRIX33: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 9 != recordEnd) {
                    return false;
                }
                SkScalar values[9];
                for (int i = 0; i < 9; i++) {
                    values[i] = skScalarFromRawBits(commands[offset++]);
                    if (!std::isfinite(values[i])) {
                        return false;
                    }
                }
                SkMatrix matrix;
                matrix.setAll(
                        values[0], values[1], values[2],
                        values[3], values[4], values[5],
                        values[6], values[7], values[8]);
                canvas->concat(matrix);
                break;
            }
            case COMMAND_SAVE_LAYER: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 5 != recordEnd) {
                    return false;
                }
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint layerWidth = commands[offset++];
                jint layerHeight = commands[offset++];
                jint alpha1000 = commands[offset++];
                if (alpha1000 < 0 || alpha1000 > 1000) {
                    return false;
                }
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                canvas->saveLayerAlphaf(&bounds, static_cast<float>(alpha1000) / 1000.0f);
                break;
            }
            case COMMAND_SAVE_LAYER_SAVE_TRANSLATE:
            case COMMAND_SAVE_SAVE_LAYER_SAVE_TRANSLATE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 7 != recordEnd) {
                    return false;
                }
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint layerWidth = commands[offset++];
                jint layerHeight = commands[offset++];
                jint alpha1000 = commands[offset++];
                SkScalar dx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar dy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                if (layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000) {
                    return false;
                }
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                if (op == COMMAND_SAVE_SAVE_LAYER_SAVE_TRANSLATE) {
                    canvas->save();
                }
                canvas->saveLayerAlphaf(&bounds, static_cast<float>(alpha1000) / 1000.0f);
                canvas->save();
                canvas->translate(dx, dy);
                break;
            }
            case COMMAND_SAVE_LAYER_CLIP_RECT: {
                if (offset + 10 != recordEnd) {
                    return false;
                }
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint layerWidth = commands[offset++];
                jint layerHeight = commands[offset++];
                jint alpha1000 = commands[offset++];
                jint clipX = commands[offset++];
                jint clipY = commands[offset++];
                jint clipWidth = commands[offset++];
                jint clipHeight = commands[offset++];
                jint clipOp = commands[offset++];
                if (alpha1000 < 0 || alpha1000 > 1000 ||
                        (clipOp != COMMAND_CLIP_OP_INTERSECT && clipOp != COMMAND_CLIP_OP_DIFFERENCE)) {
                    return false;
                }
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                canvas->saveLayerAlphaf(&bounds, static_cast<float>(alpha1000) / 1000.0f);
                canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(clipX),
                                                  static_cast<SkScalar>(clipY),
                                                  static_cast<SkScalar>(clipWidth),
                                                  static_cast<SkScalar>(clipHeight)),
                                 clipOp == COMMAND_CLIP_OP_DIFFERENCE ? SkClipOp::kDifference : SkClipOp::kIntersect,
                                 antiAlias);
                break;
            }
            case COMMAND_SAVE_LAYER_COLOR_FILTER: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 7 != recordEnd) {
                    return false;
                }
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint layerWidth = commands[offset++];
                jint layerHeight = commands[offset++];
                jint alpha1000 = commands[offset++];
                SkColor filterColor = skColorFromArgb(commands[offset++]);
                jint filterBlendMode = commands[offset++];
                if (layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000
                        || filterBlendMode != COMMAND_BLEND_MODE_SRC_IN) {
                    return false;
                }
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                SkPaint layerPaint;
                layerPaint.setAlphaf(static_cast<float>(alpha1000) / 1000.0f);
                layerPaint.setColorFilter(SkColorFilters::Blend(filterColor, SkBlendMode::kSrcIn));
                canvas->saveLayer(&bounds, &layerPaint);
                break;
            }
            case COMMAND_SAVE_LAYER_BLEND_MODE: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 6 != recordEnd) {
                    return false;
                }
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint layerWidth = commands[offset++];
                jint layerHeight = commands[offset++];
                jint alpha1000 = commands[offset++];
                jint blendMode = commands[offset++];
                SkBlendMode skBlendMode;
                if (layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000
                        || !skBlendModeForFill(blendMode, &skBlendMode)) {
                    return false;
                }
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                SkPaint layerPaint;
                layerPaint.setAlphaf(static_cast<float>(alpha1000) / 1000.0f);
                layerPaint.setBlendMode(skBlendMode);
                canvas->saveLayer(&bounds, &layerPaint);
                break;
            }
            case COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 8 != recordEnd) {
                    return false;
                }
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint layerWidth = commands[offset++];
                jint layerHeight = commands[offset++];
                jint alpha1000 = commands[offset++];
                jint blendMode = commands[offset++];
                SkColor filterColor = skColorFromArgb(commands[offset++]);
                jint filterBlendMode = commands[offset++];
                SkBlendMode skBlendMode;
                if (layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000
                        || !skBlendModeForFill(blendMode, &skBlendMode)
                        || filterBlendMode != COMMAND_BLEND_MODE_SRC_IN) {
                    return false;
                }
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                SkPaint layerPaint;
                layerPaint.setAlphaf(static_cast<float>(alpha1000) / 1000.0f);
                layerPaint.setBlendMode(skBlendMode);
                layerPaint.setColorFilter(SkColorFilters::Blend(filterColor, SkBlendMode::kSrcIn));
                canvas->saveLayer(&bounds, &layerPaint);
                break;
            }
            case COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 8 != recordEnd) {
                    return false;
                }
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint layerWidth = commands[offset++];
                jint layerHeight = commands[offset++];
                jint alpha1000 = commands[offset++];
                jint commandBlendMode = commands[offset++];
                const uint64_t handle = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                SkBlendMode blendMode;
                if (layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000 ||
                        !skBlendMode(commandBlendMode, &blendMode)) {
                    return false;
                }
                ColorFilterDescriptor descriptor;
                {
                    std::lock_guard<std::mutex> lock(gColorFilterCacheMutex);
                    auto cached = gColorFiltersByKey.find(ColorFilterScopedKey{imageCacheContextKey, handle});
                    if (cached == gColorFiltersByKey.end()) {
                        return false;
                    }
                    descriptor = cached->second;
                }
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                SkPaint layerPaint;
                layerPaint.setAlphaf(static_cast<float>(alpha1000) / 1000.0f);
                layerPaint.setBlendMode(blendMode);
                if (!setDescriptorColorFilter(&layerPaint, descriptor)) {
                    return false;
                }
                canvas->saveLayer(&bounds, &layerPaint);
                break;
            }
            case COMMAND_SAVE_LAYER_COLOR_FILTER_REF: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 7 != recordEnd) {
                    return false;
                }
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint layerWidth = commands[offset++];
                jint layerHeight = commands[offset++];
                jint alpha1000 = commands[offset++];
                const uint64_t handle = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                if (layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000) {
                    return false;
                }
                ColorFilterDescriptor descriptor;
                {
                    std::lock_guard<std::mutex> lock(gColorFilterCacheMutex);
                    auto cached = gColorFiltersByKey.find(ColorFilterScopedKey{imageCacheContextKey, handle});
                    if (cached == gColorFiltersByKey.end()) {
                        return false;
                    }
                    descriptor = cached->second;
                }
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                SkPaint layerPaint;
                layerPaint.setAlphaf(static_cast<float>(alpha1000) / 1000.0f);
                if (!setDescriptorColorFilter(&layerPaint, descriptor)) {
                    return false;
                }
                canvas->saveLayer(&bounds, &layerPaint);
                break;
            }
            case COMMAND_SAVE_LAYER_IMAGE_FILTER_REF: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 7 != recordEnd) {
                    return false;
                }
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint layerWidth = commands[offset++];
                jint layerHeight = commands[offset++];
                jint alpha1000 = commands[offset++];
                const uint64_t handle = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                if (layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000) {
                    return false;
                }
                ColorFilterDescriptor descriptor;
                {
                    std::lock_guard<std::mutex> lock(gColorFilterCacheMutex);
                    auto cached = gColorFiltersByKey.find(ColorFilterScopedKey{imageCacheContextKey, handle});
                    if (cached == gColorFiltersByKey.end()) {
                        return false;
                    }
                    descriptor = cached->second;
                }
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                SkPaint layerPaint;
                layerPaint.setAlphaf(static_cast<float>(alpha1000) / 1000.0f);
                if (!setDescriptorImageFilter(&layerPaint, descriptor)) {
                    return false;
                }
                canvas->saveLayer(&bounds, &layerPaint);
                break;
            }
            case COMMAND_DEFINE_IMAGE_ARGB: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 5 > recordEnd) {
                    return false;
                }
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint imageWidth = commands[offset++];
                const jint imageHeight = commands[offset++];
                const jint pixelCount = commands[offset++];
                if (imageWidth <= 0 || imageHeight <= 0 || imageWidth > 4096 || imageHeight > 4096 ||
                        (pixelCount != 0 && pixelCount != imageWidth * imageHeight) ||
                        offset + pixelCount != recordEnd) {
                    return false;
                }
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found != gImagesByKey.end() &&
                            found->second != nullptr &&
                            found->second->width() == imageWidth &&
                            found->second->height() == imageHeight) {
                        offset += pixelCount;
                        break;
                    }
                }
                if (pixelCount == 0) {
                    return false;
                }
                sk_sp<SkImage> image = makeRasterImage(commands, offset, imageWidth, imageHeight, pixelCount);
                offset += pixelCount;
                if (image == nullptr) {
                    return false;
                }
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    gImagesByKey[ImageCacheScopedKey{imageCacheContextKey, key}] = image;
                }
                break;
            }
            case COMMAND_DEFINE_IMAGE_BITMAP: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 8 != recordEnd) {
                    return false;
                }
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint imageWidth = commands[offset++];
                const jint imageHeight = commands[offset++];
                const uint64_t bitmapPtrValue = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint generationId = commands[offset++];
                const jint imageHasAlpha = commands[offset++];
                if (imageWidth <= 0 || imageHeight <= 0 || imageWidth > 4096 || imageHeight > 4096 ||
                        bitmapPtrValue == 0 || generationId == 0 || imageHasAlpha < 0 || imageHasAlpha > 1) {
                    return false;
                }
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found != gImagesByKey.end() &&
                            found->second != nullptr &&
                            found->second->width() == imageWidth &&
                            found->second->height() == imageHeight) {
                        break;
                    }
                }
                SkBitmap* bitmap = reinterpret_cast<SkBitmap*>(static_cast<uintptr_t>(bitmapPtrValue));
                SkPixmap pixmap;
                if (!bitmap->peekPixels(&pixmap)) {
                    return false;
                }
                SkImageInfo imageInfo = pixmap.info();
                if (imageHasAlpha == 1 && imageInfo.alphaType() == kOpaque_SkAlphaType) {
                    imageInfo = imageInfo.makeAlphaType(kPremul_SkAlphaType);
                }
                SkPixmap cachedPixmap(imageInfo, pixmap.addr(), pixmap.rowBytes());
                sk_sp<SkImage> image = SkImages::RasterFromPixmapCopy(cachedPixmap);
                if (image == nullptr || image->width() != imageWidth || image->height() != imageHeight) {
                    return false;
                }
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    gImagesByKey[ImageCacheScopedKey{imageCacheContextKey, key}] = image;
                }
                break;
            }
            case COMMAND_DRAW_IMAGE_REF: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 14 != recordEnd) {
                    return false;
                }
                const SkScalar srcLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint imageWidth = commands[offset++];
                const jint imageHeight = commands[offset++];
                const jint alpha1000 = commands[offset++];
                const jint filterQuality = commands[offset++];
                if (imageWidth <= 0 || imageHeight <= 0 || imageWidth > 4096 || imageHeight > 4096 ||
                        alpha1000 < 0 || alpha1000 > 1000 || filterQuality < 0 || filterQuality > 3) {
                    return false;
                }
                sk_sp<SkImage> image;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found == gImagesByKey.end()) {
                        return false;
                    }
                    image = found->second;
                }
                if (image->width() != imageWidth || image->height() != imageHeight) {
                    return false;
                }
                if (!drawImage(canvas, image, recordFlags, srcLeft, srcTop, srcRight, srcBottom,
                               dstLeft, dstTop, dstRight, dstBottom, alpha1000)) {
                    return false;
                }
                break;
            }
            case COMMAND_DRAW_IMAGE_REF_FULL:
            case COMMAND_DRAW_IMAGE_REF_FULL_RESTORE:
            case COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 ||
                    offset + (op == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N ? 7 : 6) != recordEnd) {
                    return false;
                }
                const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint extraRestoreCount = op == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N ? commands[offset++] : 0;
                sk_sp<SkImage> image;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found == gImagesByKey.end()) {
                        return false;
                    }
                    image = found->second;
                }
                if (!drawImage(canvas, image, recordFlags, 0.0f, 0.0f,
                               static_cast<SkScalar>(image->width()), static_cast<SkScalar>(image->height()),
                               dstLeft, dstTop, dstRight, dstBottom, 1000)) {
                    return false;
                }
                if (op == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE || op == COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N) {
                    const jint restoreCount = 1 + extraRestoreCount;
                    if (extraRestoreCount < 0 || canvas->getSaveCount() <= restoreCount) {
                        return false;
                    }
                    for (jint restoreIndex = 0; restoreIndex < restoreCount; restoreIndex++) {
                        canvas->restore();
                    }
                }
                break;
            }
            case COMMAND_DRAW_IMAGE_REF_FULL_RESTORE_N_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 16 != recordEnd) {
                    return false;
                }
                const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint extraRestoreCount = commands[offset++];
                sk_sp<SkImage> image;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found == gImagesByKey.end()) {
                        return false;
                    }
                    image = found->second;
                }
                if (!drawImage(canvas, image, recordFlags, 0.0f, 0.0f,
                               static_cast<SkScalar>(image->width()), static_cast<SkScalar>(image->height()),
                               dstLeft, dstTop, dstRight, dstBottom, 1000)) {
                    return false;
                }
                const jint restoreCount = 1 + extraRestoreCount;
                if (extraRestoreCount < 0 || canvas->getSaveCount() <= restoreCount) {
                    return false;
                }
                for (jint restoreIndex = 0; restoreIndex < restoreCount; restoreIndex++) {
                    canvas->restore();
                }
                const SkScalar layerDx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar layerDy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint x = commands[offset++];
                const jint y = commands[offset++];
                const jint layerWidth = commands[offset++];
                const jint layerHeight = commands[offset++];
                const jint alpha1000 = commands[offset++];
                const SkScalar nestedDx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar nestedDy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                if (layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000) {
                    return false;
                }
                canvas->save();
                canvas->translate(layerDx, layerDy);
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                canvas->saveLayerAlphaf(&bounds, static_cast<float>(alpha1000) / 1000.0f);
                canvas->save();
                canvas->translate(nestedDx, nestedDy);
                break;
            }
            case COMMAND_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE_DRAW_IMAGE_REF_FULL_RESTORE_N: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 16 != recordEnd) {
                    return false;
                }
                const SkScalar layerDx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar layerDy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint x = commands[offset++];
                const jint y = commands[offset++];
                const jint layerWidth = commands[offset++];
                const jint layerHeight = commands[offset++];
                const jint alpha1000 = commands[offset++];
                const SkScalar nestedDx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar nestedDy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                if (layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000) {
                    return false;
                }
                canvas->save();
                canvas->translate(layerDx, layerDy);
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                canvas->saveLayerAlphaf(&bounds, static_cast<float>(alpha1000) / 1000.0f);
                canvas->save();
                canvas->translate(nestedDx, nestedDy);
                const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint extraRestoreCount = commands[offset++];
                sk_sp<SkImage> image;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found == gImagesByKey.end()) {
                        return false;
                    }
                    image = found->second;
                }
                if (!drawImage(canvas, image, recordFlags, 0.0f, 0.0f,
                               static_cast<SkScalar>(image->width()), static_cast<SkScalar>(image->height()),
                               dstLeft, dstTop, dstRight, dstBottom, 1000)) {
                    return false;
                }
                const jint restoreCount = 1 + extraRestoreCount;
                if (extraRestoreCount < 0 || canvas->getSaveCount() <= restoreCount) {
                    return false;
                }
                for (jint restoreIndex = 0; restoreIndex < restoreCount; restoreIndex++) {
                    canvas->restore();
                }
                break;
            }
            case COMMAND_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE_DRAW_IMAGE_REF_FULL_RESTORE_N_SAVE_TRANSLATE_LAYER_SAVE_TRANSLATE: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 25 != recordEnd) {
                    return false;
                }
                const SkScalar layerDx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar layerDy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint x = commands[offset++];
                const jint y = commands[offset++];
                const jint layerWidth = commands[offset++];
                const jint layerHeight = commands[offset++];
                const jint alpha1000 = commands[offset++];
                const SkScalar nestedDx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar nestedDy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                if (layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000) {
                    return false;
                }
                canvas->save();
                canvas->translate(layerDx, layerDy);
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                 static_cast<SkScalar>(y),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                canvas->saveLayerAlphaf(&bounds, static_cast<float>(alpha1000) / 1000.0f);
                canvas->save();
                canvas->translate(nestedDx, nestedDy);
                const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint extraRestoreCount = commands[offset++];
                sk_sp<SkImage> image;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found == gImagesByKey.end()) {
                        return false;
                    }
                    image = found->second;
                }
                if (!drawImage(canvas, image, recordFlags, 0.0f, 0.0f,
                               static_cast<SkScalar>(image->width()), static_cast<SkScalar>(image->height()),
                               dstLeft, dstTop, dstRight, dstBottom, 1000)) {
                    return false;
                }
                const jint restoreCount = 1 + extraRestoreCount;
                if (extraRestoreCount < 0 || canvas->getSaveCount() <= restoreCount) {
                    return false;
                }
                for (jint restoreIndex = 0; restoreIndex < restoreCount; restoreIndex++) {
                    canvas->restore();
                }
                const SkScalar nextLayerDx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar nextLayerDy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint nextX = commands[offset++];
                const jint nextY = commands[offset++];
                const jint nextLayerWidth = commands[offset++];
                const jint nextLayerHeight = commands[offset++];
                const jint nextAlpha1000 = commands[offset++];
                const SkScalar nextNestedDx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar nextNestedDy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                if (nextLayerWidth < 0 || nextLayerHeight < 0 || nextAlpha1000 < 0 || nextAlpha1000 > 1000) {
                    return false;
                }
                canvas->save();
                canvas->translate(nextLayerDx, nextLayerDy);
                SkRect nextBounds = SkRect::MakeXYWH(static_cast<SkScalar>(nextX),
                                                     static_cast<SkScalar>(nextY),
                                                     static_cast<SkScalar>(nextLayerWidth),
                                                     static_cast<SkScalar>(nextLayerHeight));
                canvas->saveLayerAlphaf(&nextBounds, static_cast<float>(nextAlpha1000) / 1000.0f);
                canvas->save();
                canvas->translate(nextNestedDx, nextNestedDy);
                break;
            }
            case COMMAND_DRAW_IMAGE_REF_FULL_RUN: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset >= recordEnd) {
                    return false;
                }
                const jint count = commands[offset++];
                if (count <= 1 || offset + count * 6 != recordEnd) {
                    return false;
                }
                for (jint i = 0; i < count; i++) {
                    const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                    offset += 2;
                    sk_sp<SkImage> image;
                    {
                        std::lock_guard<std::mutex> lock(gImageCacheMutex);
                        auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                        if (found == gImagesByKey.end()) {
                            return false;
                        }
                        image = found->second;
                    }
                    if (!drawImage(canvas, image, recordFlags, 0.0f, 0.0f,
                                   static_cast<SkScalar>(image->width()), static_cast<SkScalar>(image->height()),
                                   dstLeft, dstTop, dstRight, dstBottom, 1000)) {
                        return false;
                    }
                }
                break;
            }
            case COMMAND_DRAW_IMAGE_REF_FULL_DRAW_ROUND_RECT: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 19 != recordEnd) {
                    return false;
                }
                const jint imageFlags = commands[offset++];
                if ((imageFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0) {
                    return false;
                }
                const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                sk_sp<SkImage> image;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found == gImagesByKey.end()) {
                        return false;
                    }
                    image = found->second;
                }
                if (!drawImage(canvas, image, imageFlags, 0.0f, 0.0f,
                               static_cast<SkScalar>(image->width()), static_cast<SkScalar>(image->height()),
                               dstLeft, dstTop, dstRight, dstBottom, 1000)) {
                    return false;
                }
                const jint paintStyle = commands[offset++];
                const jint argb = commands[offset++];
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeWidth = commands[offset++];
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                if ((paintStyle != COMMAND_PAINT_STYLE_FILL && paintStyle != COMMAND_PAINT_STYLE_STROKE) ||
                        right < left ||
                        bottom < top ||
                        radiusX < 0 ||
                        radiusY < 0 ||
                        (paintStyle == COMMAND_PAINT_STYLE_STROKE && !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000))) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias((recordFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0);
                paint.setColor(skColorFromArgb(argb));
                if (paintStyle == COMMAND_PAINT_STYLE_STROKE) {
                    paint.setStyle(SkPaint::kStroke_Style);
                    paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                    paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                    paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                    paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                }
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                break;
            }
            case COMMAND_DRAW_IMAGE_REF_FULL_FILL_RECT: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 13 != recordEnd) {
                    return false;
                }
                const jint imageFlags = commands[offset++];
                if ((imageFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0) {
                    return false;
                }
                const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                sk_sp<SkImage> image;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found == gImagesByKey.end()) {
                        return false;
                    }
                    image = found->second;
                }
                if (!drawImage(canvas, image, imageFlags, 0.0f, 0.0f,
                               static_cast<SkScalar>(image->width()), static_cast<SkScalar>(image->height()),
                               dstLeft, dstTop, dstRight, dstBottom, 1000)) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias((recordFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0);
                paint.setColor(skColorFromArgb(commands[offset++]));
                const jint x = commands[offset++];
                const jint y = commands[offset++];
                const jint rectWidth = commands[offset++];
                const jint rectHeight = commands[offset++];
                const jint radius = commands[offset++];
                if (rectWidth < 0 || rectHeight < 0 || radius < 0) {
                    return false;
                }
                SkRect rect = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                              static_cast<SkScalar>(y),
                                              static_cast<SkScalar>(rectWidth),
                                              static_cast<SkScalar>(rectHeight));
                if (radius > 0) {
                    canvas->drawRRect(SkRRect::MakeRectXY(rect,
                                                          static_cast<SkScalar>(radius),
                                                          static_cast<SkScalar>(radius)),
                                      paint);
                } else {
                    canvas->drawRect(rect, paint);
                }
                break;
            }
            case COMMAND_CLEAR_DRAW_IMAGE_REF_FULL: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 10 != recordEnd) {
                    return false;
                }
                const jint clearX = commands[offset++];
                const jint clearY = commands[offset++];
                const jint clearWidth = commands[offset++];
                const jint clearHeight = commands[offset++];
                const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                sk_sp<SkImage> image;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found == gImagesByKey.end()) {
                        return false;
                    }
                    image = found->second;
                }
                SkPaint clearPaint;
                clearPaint.setAntiAlias((recordFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0);
                clearPaint.setBlendMode(SkBlendMode::kClear);
                canvas->drawRect(SkRect::MakeXYWH(static_cast<SkScalar>(clearX),
                                                  static_cast<SkScalar>(clearY),
                                                  static_cast<SkScalar>(clearWidth),
                                                  static_cast<SkScalar>(clearHeight)),
                                 clearPaint);
                if (!drawImage(canvas, image, recordFlags, 0.0f, 0.0f,
                               static_cast<SkScalar>(image->width()), static_cast<SkScalar>(image->height()),
                               dstLeft, dstTop, dstRight, dstBottom, 1000)) {
                    return false;
                }
                break;
            }
            case COMMAND_DRAW_IMAGE_REF_COLOR_FILTER: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 16 != recordEnd) {
                    return false;
                }
                const SkScalar srcLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint imageWidth = commands[offset++];
                const jint imageHeight = commands[offset++];
                const jint alpha1000 = commands[offset++];
                const jint filterQuality = commands[offset++];
                SkColor filterColor = skColorFromArgb(commands[offset++]);
                const jint colorFilterBlendMode = commands[offset++];
                SkBlendMode colorFilterSkBlendMode;
                if (imageWidth <= 0 || imageHeight <= 0 || imageWidth > 4096 || imageHeight > 4096 ||
                        alpha1000 < 0 || alpha1000 > 1000 || filterQuality < 0 || filterQuality > 3 ||
                        !skBlendModeForColorFilter(colorFilterBlendMode, &colorFilterSkBlendMode)) {
                    return false;
                }
                sk_sp<SkImage> image;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found == gImagesByKey.end()) {
                        return false;
                    }
                    image = found->second;
                }
                if (image->width() != imageWidth || image->height() != imageHeight) {
                    return false;
                }
                if (!drawImage(canvas, image, recordFlags, srcLeft, srcTop, srcRight, srcBottom,
                               dstLeft, dstTop, dstRight, dstBottom, alpha1000, filterColor, colorFilterBlendMode)) {
                    return false;
                }
                break;
            }
            case COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 16 != recordEnd) {
                    return false;
                }
                const SkScalar srcLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint imageWidth = commands[offset++];
                const jint imageHeight = commands[offset++];
                const jint alpha1000 = commands[offset++];
                const jint filterQuality = commands[offset++];
                const uint64_t handle = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                if (imageWidth <= 0 || imageHeight <= 0 || imageWidth > 4096 || imageHeight > 4096 ||
                        alpha1000 < 0 || alpha1000 > 1000 || filterQuality < 0 || filterQuality > 3) {
                    return false;
                }
                sk_sp<SkImage> image;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found == gImagesByKey.end()) {
                        return false;
                    }
                    image = found->second;
                }
                ColorFilterDescriptor descriptor;
                {
                    std::lock_guard<std::mutex> lock(gColorFilterCacheMutex);
                    auto cached = gColorFiltersByKey.find(ColorFilterScopedKey{imageCacheContextKey, handle});
                    if (cached == gColorFiltersByKey.end()) {
                        return false;
                    }
                    descriptor = cached->second;
                }
                if (image->width() != imageWidth || image->height() != imageHeight) {
                    return false;
                }
                if (!drawImageWithDescriptorColorFilter(canvas, image, recordFlags, srcLeft, srcTop, srcRight, srcBottom,
                                                        dstLeft, dstTop, dstRight, dstBottom, alpha1000, descriptor)) {
                    return false;
                }
                break;
            }
            case COMMAND_FILL_RECT_IMAGE_SHADER: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 11 != recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint imageWidth = commands[offset++];
                const jint imageHeight = commands[offset++];
                const jint tileModeX = commands[offset++];
                const jint tileModeY = commands[offset++];
                const jint alpha1000 = commands[offset++];
                if (right < left || bottom < top ||
                        imageWidth <= 0 || imageHeight <= 0 || imageWidth > 4096 || imageHeight > 4096 ||
                        tileModeX < 0 || tileModeX > 3 || tileModeY < 0 || tileModeY > 3 ||
                        alpha1000 < 0 || alpha1000 > 1000) {
                    return false;
                }
                sk_sp<SkImage> image;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found == gImagesByKey.end()) {
                        return false;
                    }
                    image = found->second;
                }
                if (image->width() != imageWidth || image->height() != imageHeight) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setAlphaf(static_cast<float>(alpha1000) / 1000.0f);
                paint.setShader(image->makeShader(
                        skTileModeFromCommand(tileModeX),
                        skTileModeFromCommand(tileModeY),
                        SkSamplingOptions(SkFilterMode::kLinear, SkMipmapMode::kNone),
                        nullptr));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_STROKE_RECT_IMAGE_SHADER: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 15 != recordEnd) {
                    return false;
                }
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint imageWidth = commands[offset++];
                const jint imageHeight = commands[offset++];
                const jint tileModeX = commands[offset++];
                const jint tileModeY = commands[offset++];
                const jint alpha1000 = commands[offset++];
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                if (right < left || bottom < top ||
                        imageWidth <= 0 || imageHeight <= 0 || imageWidth > 4096 || imageHeight > 4096 ||
                        tileModeX < 0 || tileModeX > 3 || tileModeY < 0 || tileModeY > 3 ||
                        alpha1000 < 0 || alpha1000 > 1000 || strokeWidth <= 0.0f ||
                        strokeCap < 0 || strokeCap > 2 || strokeJoin < 0 || strokeJoin > 2 ||
                        strokeMiter1000 < 0) {
                    return false;
                }
                sk_sp<SkImage> image;
                {
                    std::lock_guard<std::mutex> lock(gImageCacheMutex);
                    auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                    if (found == gImagesByKey.end()) {
                        return false;
                    }
                    image = found->second;
                }
                if (image->width() != imageWidth || image->height() != imageHeight) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                const SkScalar strokeMiter = static_cast<SkScalar>(strokeMiter1000) / 1000.0f;
                paint.setStrokeMiter(strokeMiter < 1.0f ? 1.0f : strokeMiter);
                paint.setAlphaf(static_cast<float>(alpha1000) / 1000.0f);
                paint.setShader(image->makeShader(
                        skTileModeFromCommand(tileModeX),
                        skTileModeFromCommand(tileModeY),
                        SkSamplingOptions(SkFilterMode::kLinear, SkMipmapMode::kNone),
                        nullptr));
                if (paint.getShader() == nullptr) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_DRAW_IMAGE_ARGB: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 13 > recordEnd) {
                    return false;
                }
                const SkScalar srcLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar srcBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint imageWidth = commands[offset++];
                const jint imageHeight = commands[offset++];
                const jint alpha1000 = commands[offset++];
                const jint filterQuality = commands[offset++];
                const jint pixelCount = commands[offset++];
                if (imageWidth <= 0 || imageHeight <= 0 || imageWidth > 4096 || imageHeight > 4096 ||
                        pixelCount != imageWidth * imageHeight || offset + pixelCount != recordEnd ||
                        alpha1000 < 0 || alpha1000 > 1000 || filterQuality < 0 || filterQuality > 3) {
                    return false;
                }
                sk_sp<SkImage> image = makeRasterImage(commands, offset, imageWidth, imageHeight, pixelCount);
                offset += pixelCount;
                if (!drawImage(canvas, image, recordFlags, srcLeft, srcTop, srcRight, srcBottom,
                               dstLeft, dstTop, dstRight, dstBottom, alpha1000)) {
                    return false;
                }
                break;
            }
            case COMMAND_DEFINE_FONT_DATA: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 3 > recordEnd) {
                    return false;
                }
                const uint64_t handle = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint byteCount = commands[offset++];
                if (byteCount <= 0 || byteCount > 1048576 || offset + byteCount != recordEnd) {
                    return false;
                }
                std::vector<uint8_t> data(static_cast<size_t>(byteCount));
                for (jint index = 0; index < byteCount; index++) {
                    const jint value = commands[offset++];
                    if (value < 0 || value > 255) {
                        return false;
                    }
                    data[static_cast<size_t>(index)] = static_cast<uint8_t>(value);
                }
                sk_sp<SkData> skData = SkData::MakeWithCopy(data.data(), data.size());
                sk_sp<SkTypeface> typeface = coreTextFontMgr()->makeFromData(skData);
                if (typeface == nullptr) {
                    return false;
                }
                std::scoped_lock lock(gFontDataCacheMutex);
                gFontDataTypefacesByKey[handle] = typeface;
                std::fprintf(stderr,
                             "JBR_SKIA_INTEROP_FONT_DATA_DEFINE backend=native contextId=%p handle=0x%016llx bytes=%d\n",
                             imageCacheContextKey,
                             static_cast<unsigned long long>(handle),
                             byteCount);
                break;
            }
            case COMMAND_DRAW_TEXT_UTF16: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 9 > recordEnd) {
                    return false;
                }
                const SkScalar x = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar baseline = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar fontSize = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkColor color = skColorFromArgb(commands[offset++]);
                const jint fontWeight = commands[offset++];
                const jint fontWidth = commands[offset++];
                const jint fontSlant = commands[offset++];
                const jint familyCharCount = commands[offset++];
                if (familyCharCount < 0 || familyCharCount > 256 || offset + familyCharCount >= recordEnd) {
                    return false;
                }
                std::string fontFamily;
                if (!appendUtf16CommandText(fontFamily, commands, offset, familyCharCount)) {
                    return false;
                }
                const jint charCount = commands[offset++];
                if (fontSize <= 0.0f ||
                        fontWeight < 1 || fontWeight > 1000 ||
                        fontWidth < 1 || fontWidth > 9 ||
                        fontSlant < 0 || fontSlant > 2 ||
                        charCount < 0 || charCount > 4096 || offset + charCount != recordEnd) {
                    return false;
                }
                std::string text;
                if (!appendUtf16CommandText(text, commands, offset, charCount)) {
                    return false;
                }
                SkFontStyle fontStyle(fontWeight, fontWidth, static_cast<SkFontStyle::Slant>(fontSlant));
                sk_sp<SkTypeface> typeface = matchCommandTypeface(fontFamily, fontStyle);
                SkFont font(typeface, fontSize);
                font.setEdging((recordFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0
                        ? SkFont::Edging::kAntiAlias
                        : SkFont::Edging::kAlias);
                SkPaint paint;
                paint.setAntiAlias((recordFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0);
                paint.setColor(color);
                canvas->drawSimpleText(text.data(), text.size(), SkTextEncoding::kUTF8, x, baseline, font, paint);
                break;
            }
            case COMMAND_DRAW_PARAGRAPH_UTF16: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 7 > recordEnd) {
                    return false;
                }
                const SkScalar x = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar y = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar paragraphWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar fontSize = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkColor color = skColorFromArgb(commands[offset++]);
                const jint fontWeight = commands[offset++];
                const jint fontWidth = commands[offset++];
                const jint fontSlant = commands[offset++];
                const jint familyCharCount = commands[offset++];
                if (familyCharCount < 0 || familyCharCount > 256 || offset + familyCharCount + 10 > recordEnd) {
                    return false;
                }
                std::string fontFamily;
                if (!appendUtf16CommandText(fontFamily, commands, offset, familyCharCount)) {
                    return false;
                }
                const jint textAlign = commands[offset++];
                const jint textDirection = commands[offset++];
                const jint lineHeightMultiplier1000 = commands[offset++];
                const jint maxLines = commands[offset++];
                const jint ellipsisMode = commands[offset++];
                const jint decorationMask = commands[offset++];
                const jint letterSpacing1000 = commands[offset++];
                const jint backgroundSpecified = commands[offset++];
                const SkColor backgroundColor = skColorFromArgb(commands[offset++]);
                const jint charCount = commands[offset++];
                if (paragraphWidth <= 0.0f || fontSize <= 0.0f ||
                        fontWeight < 1 || fontWeight > 1000 ||
                        fontWidth < 1 || fontWidth > 9 ||
                        fontSlant < 0 || fontSlant > 2 ||
                        textAlign < 0 || textAlign > 5 ||
                        textDirection < 0 || textDirection > 1 ||
                        lineHeightMultiplier1000 < 0 || lineHeightMultiplier1000 > 100000 ||
                        maxLines < 0 || maxLines > 4096 ||
                        ellipsisMode < 0 || ellipsisMode > 1 ||
                        decorationMask < 0 || decorationMask > 3 ||
                        letterSpacing1000 < -100000 || letterSpacing1000 > 100000 ||
                        backgroundSpecified < 0 || backgroundSpecified > 1 ||
                        charCount < 0 || charCount > 4096 || offset + charCount != recordEnd) {
                    return false;
                }
                std::string text;
                if (!appendUtf16CommandText(text, commands, offset, charCount)) {
                    return false;
                }
                const long long paragraphStartNanos = metrics != nullptr ? monotonicNanos() : 0;
                skia::textlayout::ParagraphStyle paragraphStyle;
                paragraphStyle.setTextAlign(static_cast<skia::textlayout::TextAlign>(textAlign));
                paragraphStyle.setTextDirection(static_cast<skia::textlayout::TextDirection>(textDirection));
                if (maxLines > 0) {
                    paragraphStyle.setMaxLines(static_cast<size_t>(maxLines));
                    if (ellipsisMode == 1) {
                        paragraphStyle.setEllipsis(SkString("..."));
                    }
                }
                skia::textlayout::TextStyle textStyle;
                textStyle.setColor(color);
                textStyle.setFontSize(fontSize);
                if (backgroundSpecified == 1) {
                    SkPaint backgroundPaint;
                    backgroundPaint.setColor(backgroundColor);
                    textStyle.setBackgroundPaint(backgroundPaint);
                }
                textStyle.setFontStyle(SkFontStyle(
                        fontWeight,
                        fontWidth,
                        static_cast<SkFontStyle::Slant>(fontSlant)));
                if (!fontFamily.empty()) {
                    textStyle.setFontFamilies(commandFontFamilyCandidates(fontFamily));
                }
                if (lineHeightMultiplier1000 > 0) {
                    textStyle.setHeight(static_cast<SkScalar>(lineHeightMultiplier1000) / 1000.0f);
                    textStyle.setHeightOverride(true);
                }
                if (letterSpacing1000 != 0) {
                    textStyle.setLetterSpacing(static_cast<SkScalar>(letterSpacing1000) / 1000.0f);
                }
                skia::textlayout::TextDecoration decoration = skia::textlayout::TextDecoration::kNoDecoration;
                if ((decorationMask & 1) != 0) {
                    decoration = static_cast<skia::textlayout::TextDecoration>(
                            decoration | skia::textlayout::TextDecoration::kUnderline);
                }
                if ((decorationMask & 2) != 0) {
                    decoration = static_cast<skia::textlayout::TextDecoration>(
                            decoration | skia::textlayout::TextDecoration::kLineThrough);
                }
                if (decoration != skia::textlayout::TextDecoration::kNoDecoration) {
                    textStyle.setDecoration(decoration);
                    textStyle.setDecorationColor(color);
                    textStyle.setDecorationStyle(skia::textlayout::TextDecorationStyle::kSolid);
                    textStyle.setDecorationThicknessMultiplier(1.0f);
                }
                auto builder = skia::textlayout::ParagraphBuilder::make(
                        paragraphStyle,
                        paragraphFontCollection(),
                        paragraphUnicode());
                builder->pushStyle(textStyle);
                builder->addText(text.data(), text.size());
                std::unique_ptr<skia::textlayout::Paragraph> paragraph = builder->Build();
                if (paragraph == nullptr) {
                    return false;
                }
                paragraph->layout(paragraphWidth);
                paragraph->paint(canvas, x, y);
                if (metrics != nullptr) {
                    metrics->paragraphCommands++;
                    metrics->paragraphNanos += monotonicNanos() - paragraphStartNanos;
                }
                break;
            }
            case COMMAND_CLEAR: {
                if (offset + 1 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(commands[offset++]));
                canvas->drawRect(SkRect::MakeWH(static_cast<SkScalar>(width), static_cast<SkScalar>(height)), paint);
                break;
            }
            case COMMAND_FILL_RECT: {
                if (offset + 6 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint rectWidth = commands[offset++];
                jint rectHeight = commands[offset++];
                jint radius = commands[offset++];
                SkRect rect = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                              static_cast<SkScalar>(y),
                                              static_cast<SkScalar>(rectWidth),
                                              static_cast<SkScalar>(rectHeight));
                if (radius > 0) {
                    canvas->drawRRect(SkRRect::MakeRectXY(rect,
                                                          static_cast<SkScalar>(radius),
                                                          static_cast<SkScalar>(radius)),
                                      paint);
                } else {
                    canvas->drawRect(rect, paint);
                }
                break;
            }
            case COMMAND_FILL_RECT_SAVE_LAYER_CLIP_RECT: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 17 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint fillX = commands[offset++];
                jint fillY = commands[offset++];
                jint fillWidth = commands[offset++];
                jint fillHeight = commands[offset++];
                jint radius = commands[offset++];
                jint clipFlags = commands[offset++];
                jint layerX = commands[offset++];
                jint layerY = commands[offset++];
                jint layerWidth = commands[offset++];
                jint layerHeight = commands[offset++];
                jint alpha1000 = commands[offset++];
                jint clipX = commands[offset++];
                jint clipY = commands[offset++];
                jint clipWidth = commands[offset++];
                jint clipHeight = commands[offset++];
                jint clipOp = commands[offset++];
                if ((clipFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 ||
                        fillWidth < 0 || fillHeight < 0 || radius < 0 ||
                        layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000 ||
                        clipWidth < 0 || clipHeight < 0 ||
                        (clipOp != COMMAND_CLIP_OP_INTERSECT && clipOp != COMMAND_CLIP_OP_DIFFERENCE)) {
                    return false;
                }
                SkRect fillRect = SkRect::MakeXYWH(static_cast<SkScalar>(fillX),
                                                   static_cast<SkScalar>(fillY),
                                                   static_cast<SkScalar>(fillWidth),
                                                   static_cast<SkScalar>(fillHeight));
                if (radius > 0) {
                    canvas->drawRRect(SkRRect::MakeRectXY(fillRect,
                                                          static_cast<SkScalar>(radius),
                                                          static_cast<SkScalar>(radius)),
                                      paint);
                } else {
                    canvas->drawRect(fillRect, paint);
                }
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(layerX),
                                                 static_cast<SkScalar>(layerY),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                canvas->saveLayerAlphaf(&bounds, static_cast<float>(alpha1000) / 1000.0f);
                canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(clipX),
                                                  static_cast<SkScalar>(clipY),
                                                  static_cast<SkScalar>(clipWidth),
                                                  static_cast<SkScalar>(clipHeight)),
                                 clipOp == COMMAND_CLIP_OP_DIFFERENCE ? SkClipOp::kDifference : SkClipOp::kIntersect,
                                 (clipFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0);
                break;
            }
            case COMMAND_FILL_RECT_SAVE_LAYER_CLIP_RECT_SAVE_SAVE_LAYER_SAVE_TRANSLATE: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 24 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint fillX = commands[offset++];
                jint fillY = commands[offset++];
                jint fillWidth = commands[offset++];
                jint fillHeight = commands[offset++];
                jint radius = commands[offset++];
                jint clipFlags = commands[offset++];
                jint layerX = commands[offset++];
                jint layerY = commands[offset++];
                jint layerWidth = commands[offset++];
                jint layerHeight = commands[offset++];
                jint alpha1000 = commands[offset++];
                jint clipX = commands[offset++];
                jint clipY = commands[offset++];
                jint clipWidth = commands[offset++];
                jint clipHeight = commands[offset++];
                jint clipOp = commands[offset++];
                jint nextLayerX = commands[offset++];
                jint nextLayerY = commands[offset++];
                jint nextLayerWidth = commands[offset++];
                jint nextLayerHeight = commands[offset++];
                jint nextAlpha1000 = commands[offset++];
                SkScalar dx = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                SkScalar dy = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                if ((clipFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 ||
                        fillWidth < 0 || fillHeight < 0 || radius < 0 ||
                        layerWidth < 0 || layerHeight < 0 || alpha1000 < 0 || alpha1000 > 1000 ||
                        clipWidth < 0 || clipHeight < 0 ||
                        (clipOp != COMMAND_CLIP_OP_INTERSECT && clipOp != COMMAND_CLIP_OP_DIFFERENCE) ||
                        nextLayerWidth < 0 || nextLayerHeight < 0 || nextAlpha1000 < 0 || nextAlpha1000 > 1000) {
                    return false;
                }
                SkRect fillRect = SkRect::MakeXYWH(static_cast<SkScalar>(fillX),
                                                   static_cast<SkScalar>(fillY),
                                                   static_cast<SkScalar>(fillWidth),
                                                   static_cast<SkScalar>(fillHeight));
                if (radius > 0) {
                    canvas->drawRRect(SkRRect::MakeRectXY(fillRect,
                                                          static_cast<SkScalar>(radius),
                                                          static_cast<SkScalar>(radius)),
                                      paint);
                } else {
                    canvas->drawRect(fillRect, paint);
                }
                SkRect bounds = SkRect::MakeXYWH(static_cast<SkScalar>(layerX),
                                                 static_cast<SkScalar>(layerY),
                                                 static_cast<SkScalar>(layerWidth),
                                                 static_cast<SkScalar>(layerHeight));
                canvas->saveLayerAlphaf(&bounds, static_cast<float>(alpha1000) / 1000.0f);
                canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(clipX),
                                                  static_cast<SkScalar>(clipY),
                                                  static_cast<SkScalar>(clipWidth),
                                                  static_cast<SkScalar>(clipHeight)),
                                 clipOp == COMMAND_CLIP_OP_DIFFERENCE ? SkClipOp::kDifference : SkClipOp::kIntersect,
                                 (clipFlags & COMMAND_RECORD_FLAG_ANTIALIAS) != 0);
                canvas->save();
                SkRect nextBounds = SkRect::MakeXYWH(static_cast<SkScalar>(nextLayerX),
                                                     static_cast<SkScalar>(nextLayerY),
                                                     static_cast<SkScalar>(nextLayerWidth),
                                                     static_cast<SkScalar>(nextLayerHeight));
                canvas->saveLayerAlphaf(&nextBounds, static_cast<float>(nextAlpha1000) / 1000.0f);
                canvas->save();
                canvas->translate(dx, dy);
                break;
            }
            case COMMAND_FILL_RECT_SAVE: {
                if (offset + 6 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint rectWidth = commands[offset++];
                jint rectHeight = commands[offset++];
                jint radius = commands[offset++];
                SkRect rect = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                              static_cast<SkScalar>(y),
                                              static_cast<SkScalar>(rectWidth),
                                              static_cast<SkScalar>(rectHeight));
                if (radius > 0) {
                    canvas->drawRRect(SkRRect::MakeRectXY(rect,
                                                          static_cast<SkScalar>(radius),
                                                          static_cast<SkScalar>(radius)),
                                      paint);
                } else {
                    canvas->drawRect(rect, paint);
                }
                canvas->save();
                break;
            }
            case COMMAND_SAVE_FILL_RECT_SAVE: {
                if (offset + 6 != recordEnd) {
                    return false;
                }
                canvas->save();
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint rectWidth = commands[offset++];
                jint rectHeight = commands[offset++];
                jint radius = commands[offset++];
                SkRect rect = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                              static_cast<SkScalar>(y),
                                              static_cast<SkScalar>(rectWidth),
                                              static_cast<SkScalar>(rectHeight));
                if (radius > 0) {
                    canvas->drawRRect(SkRRect::MakeRectXY(rect,
                                                          static_cast<SkScalar>(radius),
                                                          static_cast<SkScalar>(radius)),
                                      paint);
                } else {
                    canvas->drawRect(rect, paint);
                }
                canvas->save();
                break;
            }
            case COMMAND_FILL_RECT_BLEND_MODE: {
                if (offset + 6 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint blendMode = commands[offset++];
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint rectWidth = commands[offset++];
                jint rectHeight = commands[offset++];
                SkBlendMode skBlendMode;
                if (!skBlendModeForFill(blendMode, &skBlendMode) || rectWidth < 0 || rectHeight < 0) {
                    return false;
                }
                paint.setBlendMode(skBlendMode);
                canvas->drawRect(SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                  static_cast<SkScalar>(y),
                                                  static_cast<SkScalar>(rectWidth),
                                                  static_cast<SkScalar>(rectHeight)),
                                 paint);
                break;
            }
            case COMMAND_FILL_RECT_COLOR_FILTER: {
                if (offset + 7 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(commands[offset++]));
                SkColor filterColor = skColorFromArgb(commands[offset++]);
                jint filterBlendMode = commands[offset++];
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint rectWidth = commands[offset++];
                jint rectHeight = commands[offset++];
                if (filterBlendMode != COMMAND_BLEND_MODE_SRC_IN || rectWidth < 0 || rectHeight < 0) {
                    return false;
                }
                paint.setColorFilter(SkColorFilters::Blend(filterColor, SkBlendMode::kSrcIn));
                canvas->drawRect(SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                  static_cast<SkScalar>(y),
                                                  static_cast<SkScalar>(rectWidth),
                                                  static_cast<SkScalar>(rectHeight)),
                                 paint);
                break;
            }
            case COMMAND_DEFINE_COLOR_FILTER_TINT: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 4 != recordEnd) {
                    return false;
                }
                const uint64_t handle = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const SkColor filterColor = skColorFromArgb(commands[offset++]);
                const jint filterBlendMode = commands[offset++];
                if (filterBlendMode != COMMAND_BLEND_MODE_SRC_IN) {
                    return false;
                }
                {
                    std::lock_guard<std::mutex> lock(gColorFilterCacheMutex);
                    gColorFiltersByKey[ColorFilterScopedKey{imageCacheContextKey, handle}] =
                            ColorFilterDescriptor{
                                    COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER,
                                    filterColor,
                                    filterBlendMode,
                                    {}
                            };
                }
                break;
            }
            case COMMAND_DEFINE_EFFECT_DESCRIPTOR: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 5 > recordEnd) {
                    return false;
                }
                const uint64_t handle = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint descriptorType = commands[offset++];
                const jint descriptorVersion = commands[offset++];
                const jint payloadIntCount = commands[offset++];
                if (descriptorVersion != COMMAND_EFFECT_DESCRIPTOR_VERSION_1 ||
                        offset + payloadIntCount != recordEnd) {
                    return false;
                }
                ColorFilterDescriptor descriptor{};
                descriptor.type = descriptorType;
                if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER) {
                    if (payloadIntCount != 2) {
                        return false;
                    }
                    descriptor.argb = skColorFromArgb(commands[offset++]);
                    descriptor.blendMode = commands[offset++];
                    SkBlendMode colorFilterSkBlendMode;
                    if (!skBlendModeForColorFilter(descriptor.blendMode, &colorFilterSkBlendMode)) {
                        return false;
                    }
                } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER) {
                    if (payloadIntCount != 20) {
                        return false;
                    }
                    for (size_t i = 0; i < descriptor.matrix.size(); i++) {
                        const SkScalar value = skScalarFromRawBits(commands[offset++]);
                        if (!std::isfinite(value)) {
                            return false;
                        }
                        descriptor.matrix[i] = value;
                    }
                } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_LIGHTING_FILTER) {
                    if (payloadIntCount != 2) {
                        return false;
                    }
                    descriptor.argb = skColorFromArgb(commands[offset++]);
                    descriptor.blendMode = static_cast<jint>(skColorFromArgb(commands[offset++]));
                } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER ||
                        descriptorType == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT) {
                    if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT) {
                        if (payloadIntCount != 5) {
                            return false;
                        }
                        const uint64_t childHandle = imageCacheKey(commands[offset], commands[offset + 1]);
                        offset += 2;
                        {
                            std::lock_guard<std::mutex> lock(gColorFilterCacheMutex);
                            auto child = gColorFiltersByKey.find(ColorFilterScopedKey{imageCacheContextKey, childHandle});
                            if (child == gColorFiltersByKey.end() || !isImageFilterDescriptorType(child->second.type)) {
                                return false;
                            }
                            descriptor.child = std::make_shared<ColorFilterDescriptor>(child->second);
                        }
                    } else if (payloadIntCount != 3) {
                        return false;
                    }
                    descriptor.sigmaX = skScalarFromRawBits(commands[offset++]);
                    descriptor.sigmaY = skScalarFromRawBits(commands[offset++]);
                    descriptor.tileMode = commands[offset++];
                    if (!std::isfinite(descriptor.sigmaX) || !std::isfinite(descriptor.sigmaY) ||
                            descriptor.sigmaX < 0 || descriptor.sigmaY < 0 ||
                            descriptor.tileMode < 0 || descriptor.tileMode > 3) {
                        return false;
                    }
                } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER ||
                        descriptorType == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT) {
                    if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT) {
                        if (payloadIntCount != 4) {
                            return false;
                        }
                        const uint64_t childHandle = imageCacheKey(commands[offset], commands[offset + 1]);
                        offset += 2;
                        {
                            std::lock_guard<std::mutex> lock(gColorFilterCacheMutex);
                            auto child = gColorFiltersByKey.find(ColorFilterScopedKey{imageCacheContextKey, childHandle});
                            if (child == gColorFiltersByKey.end() || !isImageFilterDescriptorType(child->second.type)) {
                                return false;
                            }
                            descriptor.child = std::make_shared<ColorFilterDescriptor>(child->second);
                        }
                    } else if (payloadIntCount != 2) {
                        return false;
                    }
                    descriptor.dx = skScalarFromRawBits(commands[offset++]);
                    descriptor.dy = skScalarFromRawBits(commands[offset++]);
                    if (!std::isfinite(descriptor.dx) || !std::isfinite(descriptor.dy)) {
                        return false;
                    }
                } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER) {
                    if (payloadIntCount < 7) {
                        return false;
                    }
                    descriptor.payload.reserve(static_cast<size_t>(payloadIntCount));
                    for (jint i = 0; i < payloadIntCount; i++) {
                        descriptor.payload.push_back(commands[offset++]);
                    }
                    const jint skslLength = descriptor.payload[0];
                    const jint uniformFloatCount = descriptor.payload[1];
                    const jint childCount = descriptor.payload[2];
                    const jint namedUniformCount = descriptor.payload[3];
                    const jint namedChildCount = descriptor.payload[4];
                    if (skslLength <= 0 ||
                            skslLength > 4096 ||
                            uniformFloatCount < 0 ||
                            uniformFloatCount > 256 ||
                            childCount < 0 ||
                            childCount > 8 ||
                            namedUniformCount < 0 ||
                            namedUniformCount > 16 ||
                            namedChildCount < 0 ||
                            namedChildCount > 8 ||
                            descriptor.payload.size() < static_cast<size_t>(7 + childCount * 2 + skslLength + uniformFloatCount)) {
                        return false;
                    }
                    descriptor.children.reserve(static_cast<size_t>(childCount));
                    for (jint index = 0; index < childCount; index++) {
                        const size_t handleOffset = static_cast<size_t>(7 + index * 2);
                        const uint64_t childHandle = imageCacheKey(
                                descriptor.payload[handleOffset],
                                descriptor.payload[handleOffset + 1]);
                        std::lock_guard<std::mutex> lock(gColorFilterCacheMutex);
                        auto child = gColorFiltersByKey.find(ColorFilterScopedKey{imageCacheContextKey, childHandle});
                        if (child == gColorFiltersByKey.end() || isImageFilterDescriptorType(child->second.type)) {
                            return false;
                        }
                        descriptor.children.push_back(std::make_shared<ColorFilterDescriptor>(child->second));
                    }
                    const int schemaEnd = static_cast<int>(descriptor.payload.size()) - skslLength - uniformFloatCount;
                    const int childSchemaStart = runtimeEffectUniformSchemaEnd(
                            descriptor.payload, 7 + childCount * 2, schemaEnd, namedUniformCount, uniformFloatCount);
                    const int skslStart = runtimeEffectChildSchemaEnd(
                            descriptor.payload, childSchemaStart, schemaEnd, namedChildCount, childCount);
                    if (skslStart < 0 || skslStart + skslLength + uniformFloatCount != static_cast<int>(descriptor.payload.size())) {
                        return false;
                    }
                    const uint64_t expectedHash = imageCacheKey(descriptor.payload[5], descriptor.payload[6]);
                    if (shaderSourceHash(descriptor.payload, static_cast<size_t>(skslStart), skslLength) != expectedHash) {
                        return false;
                    }
                    for (jint i = 0; i < skslLength; i++) {
                        const jint code = descriptor.payload[static_cast<size_t>(skslStart + i)];
                        if (code <= 0 || code > 127) return false;
                    }
                } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT) {
                    if (payloadIntCount != 1) {
                        return false;
                    }
                    descriptor.payload.push_back(commands[offset++]);
                    const SkScalar radius = skScalarFromRawBits(descriptor.payload[0]);
                    if (!std::isfinite(radius) || radius < 0) {
                        return false;
                    }
                } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_STAMPED_PATH_EFFECT) {
                    if (payloadIntCount < 5) {
                        return false;
                    }
                    descriptor.payload.reserve(static_cast<size_t>(payloadIntCount));
                    for (jint i = 0; i < payloadIntCount; i++) {
                        descriptor.payload.push_back(commands[offset++]);
                    }
                    const SkScalar advance = skScalarFromRawBits(descriptor.payload[0]);
                    const SkScalar phase = skScalarFromRawBits(descriptor.payload[1]);
                    const jint style = descriptor.payload[2];
                    const jint fillType = descriptor.payload[3];
                    const jint pathDataLength = descriptor.payload[4];
                    SkPath descriptorPath;
                    if (!std::isfinite(advance) || advance <= 0 ||
                            !std::isfinite(phase) || phase < 0 ||
                            style < 0 || style > 2 ||
                            (fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                            pathDataLength < 0 ||
                            pathDataLength > 4096 ||
                            payloadIntCount != 5 + pathDataLength ||
                            !pathFromCommandData(descriptor.payload, 5, static_cast<jsize>(descriptor.payload.size()), fillType, &descriptorPath)) {
                        return false;
                    }
                } else if (descriptorType == COMMAND_EFFECT_DESCRIPTOR_CHAIN_PATH_EFFECT) {
                    if (payloadIntCount != 4) {
                        return false;
                    }
                    const uint64_t outerHandle = imageCacheKey(commands[offset], commands[offset + 1]);
                    const uint64_t innerHandle = imageCacheKey(commands[offset + 2], commands[offset + 3]);
                    offset += 4;
                    {
                        std::lock_guard<std::mutex> lock(gColorFilterCacheMutex);
                        auto outer = gColorFiltersByKey.find(ColorFilterScopedKey{imageCacheContextKey, outerHandle});
                        auto inner = gColorFiltersByKey.find(ColorFilterScopedKey{imageCacheContextKey, innerHandle});
                        if (outer == gColorFiltersByKey.end() ||
                                inner == gColorFiltersByKey.end() ||
                                !isPathEffectDescriptorType(outer->second.type) ||
                                !isPathEffectDescriptorType(inner->second.type)) {
                            return false;
                        }
                        descriptor.children.push_back(std::make_shared<ColorFilterDescriptor>(outer->second));
                        descriptor.children.push_back(std::make_shared<ColorFilterDescriptor>(inner->second));
                    }
                } else {
                    return false;
                }
                {
                    std::lock_guard<std::mutex> lock(gColorFilterCacheMutex);
                    gColorFiltersByKey[ColorFilterScopedKey{imageCacheContextKey, handle}] = descriptor;
                }
                break;
            }
            case COMMAND_DEFINE_SHADER_DESCRIPTOR: {
                if (recordFlags != COMMAND_RECORD_FLAGS_NONE || offset + 5 > recordEnd) {
                    return false;
                }
                const uint64_t handle = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const jint descriptorType = commands[offset++];
                const jint descriptorVersion = commands[offset++];
                const jint payloadIntCount = commands[offset++];
                if (descriptorVersion != COMMAND_SHADER_DESCRIPTOR_VERSION_1 ||
                        payloadIntCount < 0 ||
                        offset + payloadIntCount != recordEnd) {
                    return false;
                }
                ShaderDescriptor descriptor{};
                descriptor.type = descriptorType;
                descriptor.payload.reserve(static_cast<size_t>(payloadIntCount));
                for (jint i = 0; i < payloadIntCount; i++) {
                    descriptor.payload.push_back(commands[offset + i]);
                }
                if (descriptorType == COMMAND_SHADER_DESCRIPTOR_LINEAR_GRADIENT) {
                    if (payloadIntCount < 6) return false;
                    const jint tileMode = descriptor.payload[4];
                    const jint colorCount = descriptor.payload[5];
                    SkColor4f colors[16];
                    float positions[16];
                    if (tileMode < 0 || tileMode > 3 ||
                            !readGradientStops(descriptor.payload, 6, colorCount, colors, positions)) return false;
                } else if (descriptorType == COMMAND_SHADER_DESCRIPTOR_RADIAL_GRADIENT) {
                    if (payloadIntCount < 5) return false;
                    const jint radius1000 = descriptor.payload[2];
                    const jint tileMode = descriptor.payload[3];
                    const jint colorCount = descriptor.payload[4];
                    SkColor4f colors[16];
                    float positions[16];
                    if (radius1000 <= 0 || tileMode < 0 || tileMode > 3 ||
                            !readGradientStops(descriptor.payload, 5, colorCount, colors, positions)) return false;
                } else if (descriptorType == COMMAND_SHADER_DESCRIPTOR_SWEEP_GRADIENT) {
                    if (payloadIntCount < 3) return false;
                    const jint colorCount = descriptor.payload[2];
                    SkColor4f colors[16];
                    float positions[16];
                    if (!readGradientStops(descriptor.payload, 3, colorCount, colors, positions)) return false;
                } else if (descriptorType == COMMAND_SHADER_DESCRIPTOR_IMAGE) {
                    if (payloadIntCount != 6) return false;
                    const jint imageWidth = descriptor.payload[2];
                    const jint imageHeight = descriptor.payload[3];
                    const jint tileModeX = descriptor.payload[4];
                    const jint tileModeY = descriptor.payload[5];
                    if (imageWidth <= 0 || imageHeight <= 0 || imageWidth > 4096 || imageHeight > 4096 ||
                            tileModeX < 0 || tileModeX > 3 || tileModeY < 0 || tileModeY > 3) return false;
                } else if (descriptorType == COMMAND_SHADER_DESCRIPTOR_COLOR) {
                    if (payloadIntCount != 1) return false;
                } else if (descriptorType == COMMAND_SHADER_DESCRIPTOR_PERLIN_NOISE) {
                    if (payloadIntCount != 7) return false;
                    const jint kind = descriptor.payload[0];
                    const jint baseFrequencyX1000000 = descriptor.payload[1];
                    const jint baseFrequencyY1000000 = descriptor.payload[2];
                    const jint numOctaves = descriptor.payload[3];
                    const jint tileWidth = descriptor.payload[5];
                    const jint tileHeight = descriptor.payload[6];
                    if (kind < 0 || kind > 1 ||
                            baseFrequencyX1000000 <= 0 || baseFrequencyY1000000 <= 0 ||
                            numOctaves < 1 || numOctaves > 16 ||
                            tileWidth < 0 || tileHeight < 0 || tileWidth > 4096 || tileHeight > 4096) return false;
                } else if (descriptorType == COMMAND_SHADER_DESCRIPTOR_COMPOSITE) {
                    if (payloadIntCount != 5) return false;
                    const uint64_t dstHandle = imageCacheKey(descriptor.payload[0], descriptor.payload[1]);
                    const uint64_t srcHandle = imageCacheKey(descriptor.payload[2], descriptor.payload[3]);
                    SkBlendMode blendMode;
                    if (!skBlendMode(descriptor.payload[4], &blendMode)) return false;
                    {
                        std::lock_guard<std::mutex> lock(gShaderCacheMutex);
                        auto dst = gShadersByKey.find(ColorFilterScopedKey{imageCacheContextKey, dstHandle});
                        auto src = gShadersByKey.find(ColorFilterScopedKey{imageCacheContextKey, srcHandle});
                        if (dst == gShadersByKey.end() || src == gShadersByKey.end()) return false;
                        descriptor.dst = std::make_shared<ShaderDescriptor>(dst->second);
                        descriptor.src = std::make_shared<ShaderDescriptor>(src->second);
                    }
                } else if (descriptorType == COMMAND_SHADER_DESCRIPTOR_COLOR_FILTER) {
                    if (payloadIntCount != 4) return false;
                    const uint64_t childHandle = imageCacheKey(descriptor.payload[0], descriptor.payload[1]);
                    const uint64_t colorFilterHandle = imageCacheKey(descriptor.payload[2], descriptor.payload[3]);
                    {
                        std::lock_guard<std::mutex> lock(gShaderCacheMutex);
                        auto child = gShadersByKey.find(ColorFilterScopedKey{imageCacheContextKey, childHandle});
                        if (child == gShadersByKey.end()) return false;
                        descriptor.child = std::make_shared<ShaderDescriptor>(child->second);
                    }
                    {
                        std::lock_guard<std::mutex> lock(gColorFilterCacheMutex);
                        auto colorFilter = gColorFiltersByKey.find(ColorFilterScopedKey{imageCacheContextKey, colorFilterHandle});
                        if (colorFilter == gColorFiltersByKey.end()) return false;
                        descriptor.colorFilter = std::make_shared<ColorFilterDescriptor>(colorFilter->second);
                    }
                } else if (descriptorType == COMMAND_SHADER_DESCRIPTOR_TRANSFORM) {
                    if (payloadIntCount != 11) return false;
                    const uint64_t childHandle = imageCacheKey(descriptor.payload[0], descriptor.payload[1]);
                    {
                        std::lock_guard<std::mutex> lock(gShaderCacheMutex);
                        auto child = gShadersByKey.find(ColorFilterScopedKey{imageCacheContextKey, childHandle});
                        if (child == gShadersByKey.end()) return false;
                        descriptor.child = std::make_shared<ShaderDescriptor>(child->second);
                    }
                } else if (descriptorType == COMMAND_SHADER_DESCRIPTOR_RUNTIME_EFFECT) {
                    if (payloadIntCount < 7) return false;
                    const jint skslLength = descriptor.payload[0];
                    const jint uniformFloatCount = descriptor.payload[1];
                    const jint childCount = descriptor.payload[2];
                    const jint namedUniformCount = descriptor.payload[3];
                    const jint namedChildCount = descriptor.payload[4];
                    if (skslLength <= 0 ||
                            skslLength > 4096 ||
                            uniformFloatCount < 0 ||
                            uniformFloatCount > 256 ||
                            childCount < 0 ||
                            childCount > 8 ||
                            namedUniformCount < 0 ||
                            namedUniformCount > 16 ||
                            namedChildCount < 0 ||
                            namedChildCount > childCount ||
                            payloadIntCount < 7 + childCount * 2 + skslLength + uniformFloatCount) {
                        return false;
                    }
                    const jint schemaStart = 7 + childCount * 2;
                    const jint schemaEnd = payloadIntCount - skslLength - uniformFloatCount;
                    const jint childSchemaStart = runtimeEffectUniformSchemaEnd(
                            descriptor.payload, schemaStart, schemaEnd, namedUniformCount, uniformFloatCount);
                    const jint skslStart = runtimeEffectChildSchemaEnd(
                            descriptor.payload, childSchemaStart, schemaEnd, namedChildCount, childCount);
                    if (skslStart < 0 || skslStart + skslLength + uniformFloatCount != payloadIntCount) {
                        return false;
                    }
                    const uint64_t expectedHash = imageCacheKey(descriptor.payload[5], descriptor.payload[6]);
                    if (shaderSourceHash(descriptor.payload, static_cast<size_t>(skslStart), skslLength) != expectedHash) {
                        return false;
                    }
                    {
                        std::lock_guard<std::mutex> lock(gShaderCacheMutex);
                        for (jint i = 0; i < childCount; i++) {
                            const uint64_t childHandle = imageCacheKey(
                                    descriptor.payload[7 + i * 2],
                                    descriptor.payload[8 + i * 2]);
                            auto child = gShadersByKey.find(ColorFilterScopedKey{imageCacheContextKey, childHandle});
                            if (child == gShadersByKey.end()) return false;
                            descriptor.children.push_back(std::make_shared<ShaderDescriptor>(child->second));
                        }
                    }
                    for (jint i = 0; i < skslLength; i++) {
                        const jint code = descriptor.payload[skslStart + i];
                        if (code <= 0 || code > 127) return false;
                    }
                } else {
                    return false;
                }
                offset = recordEnd;
                {
                    std::lock_guard<std::mutex> lock(gShaderCacheMutex);
                    gShadersByKey[ColorFilterScopedKey{imageCacheContextKey, handle}] = descriptor;
                }
                break;
            }
            case COMMAND_FILL_RECT_SHADER_REF: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 7 != recordEnd) {
                    return false;
                }
                const uint64_t handle = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint alpha1000 = commands[offset++];
                if (right < left || bottom < top || alpha1000 < 0 || alpha1000 > 1000) {
                    return false;
                }
                ShaderDescriptor descriptor;
                {
                    std::lock_guard<std::mutex> lock(gShaderCacheMutex);
                    auto cached = gShadersByKey.find(ColorFilterScopedKey{imageCacheContextKey, handle});
                    if (cached == gShadersByKey.end()) {
                        return false;
                    }
                    descriptor = cached->second;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setAlphaf(static_cast<float>(alpha1000) / 1000.0f);
                paint.setShader(makeDescriptorShader(descriptor, imageCacheContextKey, 0));
                if (!paint.getShader()) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_STROKE_RECT_SHADER_REF: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 11 != recordEnd) {
                    return false;
                }
                const uint64_t handle = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar strokeWidth = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                const jint alpha1000 = commands[offset++];
                if (right < left || bottom < top || strokeWidth <= 0.0f ||
                        strokeCap < 0 || strokeCap > 2 || strokeJoin < 0 || strokeJoin > 2 ||
                        strokeMiter1000 < 0 || alpha1000 < 0 || alpha1000 > 1000) {
                    return false;
                }
                ShaderDescriptor descriptor;
                {
                    std::lock_guard<std::mutex> lock(gShaderCacheMutex);
                    auto cached = gShadersByKey.find(ColorFilterScopedKey{imageCacheContextKey, handle});
                    if (cached == gShadersByKey.end()) {
                        return false;
                    }
                    descriptor = cached->second;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setStrokeWidth(strokeWidth);
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                const SkScalar strokeMiter = static_cast<SkScalar>(strokeMiter1000) / 1000.0f;
                paint.setStrokeMiter(strokeMiter < 1.0f ? 1.0f : strokeMiter);
                paint.setAlphaf(static_cast<float>(alpha1000) / 1000.0f);
                paint.setShader(makeDescriptorShader(descriptor, imageCacheContextKey, 0));
                if (!paint.getShader()) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeLTRB(left, top, right, bottom), paint);
                break;
            }
            case COMMAND_FILL_RECT_COLOR_FILTER_REF: {
                if (offset + 7 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(commands[offset++]));
                const uint64_t handle = imageCacheKey(commands[offset], commands[offset + 1]);
                offset += 2;
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint rectWidth = commands[offset++];
                jint rectHeight = commands[offset++];
                ColorFilterDescriptor descriptor;
                {
                    std::lock_guard<std::mutex> lock(gColorFilterCacheMutex);
                    auto cached = gColorFiltersByKey.find(ColorFilterScopedKey{imageCacheContextKey, handle});
                    if (cached == gColorFiltersByKey.end()) {
                        return false;
                    }
                    descriptor = cached->second;
                }
                if (rectWidth < 0 || rectHeight < 0) {
                    return false;
                }
                if (!setDescriptorColorFilter(&paint, descriptor)) {
                    return false;
                }
                canvas->drawRect(SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                  static_cast<SkScalar>(y),
                                                  static_cast<SkScalar>(rectWidth),
                                                  static_cast<SkScalar>(rectHeight)),
                                 paint);
                break;
            }
            case COMMAND_CLEAR_RECT: {
                if (offset + 4 != recordEnd) {
                    return false;
                }
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint rectWidth = commands[offset++];
                jint rectHeight = commands[offset++];
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setBlendMode(SkBlendMode::kClear);
                canvas->drawRect(SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                  static_cast<SkScalar>(y),
                                                  static_cast<SkScalar>(rectWidth),
                                                  static_cast<SkScalar>(rectHeight)),
                                 paint);
                break;
            }
            case COMMAND_STROKE_LINE: {
                if (offset + 9 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x1 = commands[offset++];
                jint y1 = commands[offset++];
                jint x2 = commands[offset++];
                jint y2 = commands[offset++];
                jint strokeWidth = commands[offset++];
                jint strokeCap = commands[offset++];
                jint strokeJoin = commands[offset++];
                jint strokeMiter1000 = commands[offset++];
                if (!isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000)) {
                    return false;
                }
                paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                canvas->drawLine(static_cast<SkScalar>(x1),
                                 static_cast<SkScalar>(y1),
                                 static_cast<SkScalar>(x2),
                                 static_cast<SkScalar>(y2),
                                 paint);
                break;
            }
            case COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN:
            case COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN_RESTORE_N: {
                if ((recordFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0 || offset + 23 > recordEnd) {
                    return false;
                }
                const jint imageFlags = commands[offset++];
                if ((imageFlags & ~COMMAND_RECORD_FLAG_ANTIALIAS) != 0) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x1 = commands[offset++];
                jint y1 = commands[offset++];
                jint x2 = commands[offset++];
                jint y2 = commands[offset++];
                jint strokeWidth = commands[offset++];
                jint strokeCap = commands[offset++];
                jint strokeJoin = commands[offset++];
                jint strokeMiter1000 = commands[offset++];
                if (!isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000)) {
                    return false;
                }
                paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                canvas->drawLine(static_cast<SkScalar>(x1),
                                 static_cast<SkScalar>(y1),
                                 static_cast<SkScalar>(x2),
                                 static_cast<SkScalar>(y2),
                                 paint);
                const jint count = commands[offset++];
                if (count <= 1 ||
                    offset + count * 6 +
                        (op == COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN_RESTORE_N ? 1 : 0) != recordEnd) {
                    return false;
                }
                for (jint i = 0; i < count; i++) {
                    const SkScalar dstLeft = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    const SkScalar dstTop = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    const SkScalar dstRight = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    const SkScalar dstBottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                    const uint64_t key = imageCacheKey(commands[offset], commands[offset + 1]);
                    offset += 2;
                    sk_sp<SkImage> image;
                    {
                        std::lock_guard<std::mutex> lock(gImageCacheMutex);
                        auto found = gImagesByKey.find(ImageCacheScopedKey{imageCacheContextKey, key});
                        if (found == gImagesByKey.end()) {
                            return false;
                        }
                        image = found->second;
                    }
                    if (!drawImage(canvas, image, imageFlags, 0.0f, 0.0f,
                                   static_cast<SkScalar>(image->width()), static_cast<SkScalar>(image->height()),
                                   dstLeft, dstTop, dstRight, dstBottom, 1000)) {
                        return false;
                    }
                }
                if (op == COMMAND_STROKE_LINE_DRAW_IMAGE_REF_FULL_RUN_RESTORE_N) {
                    const jint restoreCount = commands[offset++];
                    if (restoreCount <= 0 || canvas->getSaveCount() <= restoreCount) {
                        return false;
                    }
                    for (jint i = 0; i < restoreCount; i++) {
                        canvas->restore();
                    }
                }
                break;
            }
            case COMMAND_STROKE_LINE_DASH_PATH_EFFECT: {
                if (offset + 13 > recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x1 = commands[offset++];
                jint y1 = commands[offset++];
                jint x2 = commands[offset++];
                jint y2 = commands[offset++];
                jint strokeWidth = commands[offset++];
                jint strokeCap = commands[offset++];
                jint strokeJoin = commands[offset++];
                jint strokeMiter1000 = commands[offset++];
                jint phase1000 = commands[offset++];
                jint intervalCount = commands[offset++];
                if (!isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000) ||
                    phase1000 < 0 || intervalCount < 2 || intervalCount > 16 ||
                    offset + intervalCount != recordEnd) {
                    return false;
                }
                std::vector<SkScalar> intervals;
                intervals.reserve(static_cast<size_t>(intervalCount));
                for (jint index = 0; index < intervalCount; index++) {
                    jint interval1000 = commands[offset++];
                    if (interval1000 <= 0) {
                        return false;
                    }
                    intervals.push_back(static_cast<SkScalar>(interval1000) / 1000.0f);
                }
                paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                paint.setPathEffect(SkDashPathEffect::Make(
                        SkSpan<const SkScalar>(intervals.data(), static_cast<size_t>(intervalCount)),
                        static_cast<SkScalar>(phase1000) / 1000.0f));
                canvas->drawLine(static_cast<SkScalar>(x1),
                                 static_cast<SkScalar>(y1),
                                 static_cast<SkScalar>(x2),
                                 static_cast<SkScalar>(y2),
                                 paint);
                break;
            }
            case COMMAND_STROKE_RECT_DASH_PATH_EFFECT: {
                if (offset + 13 > recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint rectWidth = commands[offset++];
                jint rectHeight = commands[offset++];
                jint strokeWidth = commands[offset++];
                jint strokeCap = commands[offset++];
                jint strokeJoin = commands[offset++];
                jint strokeMiter1000 = commands[offset++];
                jint phase1000 = commands[offset++];
                jint intervalCount = commands[offset++];
                if (rectWidth < 0 || rectHeight < 0 ||
                    !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000) ||
                    phase1000 < 0 || intervalCount < 2 || intervalCount > 16 ||
                    offset + intervalCount != recordEnd) {
                    return false;
                }
                std::vector<SkScalar> intervals;
                intervals.reserve(static_cast<size_t>(intervalCount));
                for (jint index = 0; index < intervalCount; index++) {
                    jint interval1000 = commands[offset++];
                    if (interval1000 <= 0) {
                        return false;
                    }
                    intervals.push_back(static_cast<SkScalar>(interval1000) / 1000.0f);
                }
                paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                paint.setPathEffect(SkDashPathEffect::Make(
                        SkSpan<const SkScalar>(intervals.data(), static_cast<size_t>(intervalCount)),
                        static_cast<SkScalar>(phase1000) / 1000.0f));
                canvas->drawRect(SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                  static_cast<SkScalar>(y),
                                                  static_cast<SkScalar>(rectWidth),
                                                  static_cast<SkScalar>(rectHeight)),
                                 paint);
                break;
            }
            case COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT: {
                if (offset + 15 > recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setColor(skColorFromArgb(commands[offset++]));
                const SkScalar left = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar top = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar right = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar bottom = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusX = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                const SkScalar radiusY = static_cast<SkScalar>(commands[offset++]) / 1000.0f;
                jint strokeWidth = commands[offset++];
                jint strokeCap = commands[offset++];
                jint strokeJoin = commands[offset++];
                jint strokeMiter1000 = commands[offset++];
                jint phase1000 = commands[offset++];
                jint intervalCount = commands[offset++];
                if (right < left || bottom < top || radiusX < 0 || radiusY < 0 ||
                    !isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000) ||
                    phase1000 < 0 || intervalCount < 2 || intervalCount > 16 ||
                    offset + intervalCount != recordEnd) {
                    return false;
                }
                std::vector<SkScalar> intervals;
                intervals.reserve(static_cast<size_t>(intervalCount));
                for (jint index = 0; index < intervalCount; index++) {
                    jint interval1000 = commands[offset++];
                    if (interval1000 <= 0) {
                        return false;
                    }
                    intervals.push_back(static_cast<SkScalar>(interval1000) / 1000.0f);
                }
                paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                paint.setPathEffect(SkDashPathEffect::Make(
                        SkSpan<const SkScalar>(intervals.data(), static_cast<size_t>(intervalCount)),
                        static_cast<SkScalar>(phase1000) / 1000.0f));
                canvas->drawRRect(SkRRect::MakeRectXY(SkRect::MakeLTRB(left, top, right, bottom),
                                                      radiusX,
                                                      radiusY),
                                  paint);
                break;
            }
            case COMMAND_STROKE_PATH_DASH_PATH_EFFECT: {
                if (offset + 9 > recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setColor(skColorFromArgb(commands[offset++]));
                const jint strokeWidth = commands[offset++];
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                const jint phase1000 = commands[offset++];
                const jint intervalCount = commands[offset++];
                if (!isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000) ||
                    phase1000 < 0 || intervalCount < 2 || intervalCount > 16 ||
                    offset + intervalCount + 2 > recordEnd) {
                    return false;
                }
                std::vector<SkScalar> intervals;
                intervals.reserve(static_cast<size_t>(intervalCount));
                for (jint index = 0; index < intervalCount; index++) {
                    jint interval1000 = commands[offset++];
                    if (interval1000 <= 0) {
                        return false;
                    }
                    intervals.push_back(static_cast<SkScalar>(interval1000) / 1000.0f);
                }
                const jint fillType = commands[offset++];
                const jint pathDataLength = commands[offset++];
                if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                    pathDataLength < 0 || offset + pathDataLength != recordEnd) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, recordEnd, fillType, &path)) {
                    return false;
                }
                offset = recordEnd;
                paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                paint.setPathEffect(SkDashPathEffect::Make(
                        SkSpan<const SkScalar>(intervals.data(), static_cast<size_t>(intervalCount)),
                        static_cast<SkScalar>(phase1000) / 1000.0f));
                canvas->drawPath(path, paint);
                break;
            }
            case COMMAND_DRAW_SHADOW_PATH: {
                if (offset + 14 > recordEnd) {
                    return false;
                }
                const SkColor ambientColor = skColorFromArgb(commands[offset++]);
                const SkColor spotColor = skColorFromArgb(commands[offset++]);
                const SkScalar zPlaneX = commandBitsToFloat(commands[offset++]);
                const SkScalar zPlaneY = commandBitsToFloat(commands[offset++]);
                const SkScalar zPlaneZ = commandBitsToFloat(commands[offset++]);
                const SkScalar lightPosX = commandBitsToFloat(commands[offset++]);
                const SkScalar lightPosY = commandBitsToFloat(commands[offset++]);
                const SkScalar lightPosZ = commandBitsToFloat(commands[offset++]);
                const SkScalar lightRadius = commandBitsToFloat(commands[offset++]);
                const jint shadowFlags = commands[offset++];
                const jint fillType = commands[offset++];
                const jint pathDataLength = commands[offset++];
                if ((fillType != COMMAND_PATH_FILL_NON_ZERO && fillType != COMMAND_PATH_FILL_EVEN_ODD) ||
                    pathDataLength < 0 || offset + pathDataLength != recordEnd ||
                    !std::isfinite(zPlaneX) || !std::isfinite(zPlaneY) || !std::isfinite(zPlaneZ) ||
                    !std::isfinite(lightPosX) || !std::isfinite(lightPosY) || !std::isfinite(lightPosZ) ||
                    !std::isfinite(lightRadius) || lightRadius < 0 || (shadowFlags & ~0x3) != 0) {
                    return false;
                }
                SkPath path;
                if (!pathFromCommandData(commands, offset, recordEnd, fillType, &path)) {
                    return false;
                }
                offset = recordEnd;
                if (metrics) {
                    metrics->shadowCommands++;
                }
                SkShadowUtils::DrawShadow(
                        canvas,
                        path,
                        SkPoint3::Make(zPlaneX, zPlaneY, zPlaneZ),
                        SkPoint3::Make(lightPosX, lightPosY, lightPosZ),
                        lightRadius,
                        ambientColor,
                        spotColor,
                        static_cast<uint32_t>(shadowFlags));
                break;
            }
            case COMMAND_DRAW_POINTS: {
                if (offset + 6 > recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setColor(skColorFromArgb(commands[offset++]));
                const jint strokeWidth = commands[offset++];
                const jint strokeCap = commands[offset++];
                const jint strokeJoin = commands[offset++];
                const jint strokeMiter1000 = commands[offset++];
                const jint pointCount = commands[offset++];
                if (!isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000) ||
                    pointCount < 1 || pointCount > 4096 ||
                    offset + pointCount * 2 != recordEnd) {
                    return false;
                }
                std::vector<SkPoint> points;
                points.reserve(static_cast<size_t>(pointCount));
                for (jint pointIndex = 0; pointIndex < pointCount; pointIndex++) {
                    const SkScalar x = static_cast<SkScalar>(commands[offset++]);
                    const SkScalar y = static_cast<SkScalar>(commands[offset++]);
                    points.push_back(SkPoint::Make(x, y));
                }
                paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                canvas->drawPoints(
                        SkCanvas::kPoints_PointMode,
                        SkSpan<const SkPoint>(points.data(), points.size()),
                        paint);
                break;
            }
            case COMMAND_DRAW_VERTICES: {
                if (offset + 5 > recordEnd) {
                    return false;
                }
                const jint vertexMode = commands[offset++];
                const jint commandBlendMode = commands[offset++];
                const SkColor paintColor = skColorFromArgb(commands[offset++]);
                const jint vertexCount = commands[offset++];
                const jint indexCount = commands[offset++];
                SkBlendMode blendMode;
                if (vertexMode < 0 || vertexMode > 2 ||
                    !skBlendMode(commandBlendMode, &blendMode) ||
                    vertexCount < 3 || vertexCount > 4096 ||
                    indexCount < 0 || indexCount > 8192 ||
                    offset + vertexCount * 5 + indexCount != recordEnd) {
                    return false;
                }
                std::vector<SkPoint> positions;
                positions.reserve(static_cast<size_t>(vertexCount));
                for (jint vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                    const SkScalar x = static_cast<SkScalar>(commands[offset++]);
                    const SkScalar y = static_cast<SkScalar>(commands[offset++]);
                    positions.push_back(SkPoint::Make(x, y));
                }
                std::vector<SkPoint> texCoords;
                texCoords.reserve(static_cast<size_t>(vertexCount));
                for (jint vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                    const SkScalar x = commandBitsToFloat(commands[offset++]);
                    const SkScalar y = commandBitsToFloat(commands[offset++]);
                    if (!std::isfinite(x) || !std::isfinite(y)) {
                        return false;
                    }
                    texCoords.push_back(SkPoint::Make(x, y));
                }
                std::vector<SkColor> colors;
                colors.reserve(static_cast<size_t>(vertexCount));
                for (jint vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                    colors.push_back(skColorFromArgb(commands[offset++]));
                }
                std::vector<uint16_t> indices;
                indices.reserve(static_cast<size_t>(indexCount));
                for (jint index = 0; index < indexCount; index++) {
                    const jint vertexIndex = commands[offset++];
                    if (vertexIndex < 0 || vertexIndex >= vertexCount) {
                        return false;
                    }
                    indices.push_back(static_cast<uint16_t>(vertexIndex));
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(paintColor);
                sk_sp<SkVertices> vertices = SkVertices::MakeCopy(
                        static_cast<SkVertices::VertexMode>(vertexMode),
                        vertexCount,
                        positions.data(),
                        texCoords.data(),
                        colors.data(),
                        indexCount,
                        indexCount > 0 ? indices.data() : nullptr);
                if (!vertices) {
                    return false;
                }
                canvas->drawVertices(vertices, blendMode, paint);
                break;
            }
            case COMMAND_FILL_OVAL: {
                if (offset + 5 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint ovalWidth = commands[offset++];
                jint ovalHeight = commands[offset++];
                SkRect rect = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                              static_cast<SkScalar>(y),
                                              static_cast<SkScalar>(ovalWidth),
                                              static_cast<SkScalar>(ovalHeight));
                canvas->drawOval(rect, paint);
                break;
            }
            case COMMAND_STROKE_OVAL: {
                if (offset + 9 != recordEnd) {
                    return false;
                }
                SkPaint paint;
                paint.setAntiAlias(antiAlias);
                paint.setStyle(SkPaint::kStroke_Style);
                paint.setColor(skColorFromArgb(commands[offset++]));
                jint x = commands[offset++];
                jint y = commands[offset++];
                jint ovalWidth = commands[offset++];
                jint ovalHeight = commands[offset++];
                jint strokeWidth = commands[offset++];
                jint strokeCap = commands[offset++];
                jint strokeJoin = commands[offset++];
                jint strokeMiter1000 = commands[offset++];
                if (!isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000)) {
                    return false;
                }
                paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                SkRect rect = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                              static_cast<SkScalar>(y),
                                              static_cast<SkScalar>(ovalWidth),
                                              static_cast<SkScalar>(ovalHeight));
                canvas->drawOval(rect, paint);
                break;
            }
            case COMMAND_STROKE_OVAL_RUN: {
                if (offset + 1 > recordEnd) {
                    return false;
                }
                const jint ovalCount = commands[offset++];
                if (ovalCount < 2 ||
                        ovalCount > 4096 ||
                        offset + ovalCount * 9 != recordEnd) {
                    return false;
                }
                for (jint i = 0; i < ovalCount; ++i) {
                    SkPaint paint;
                    paint.setAntiAlias(antiAlias);
                    paint.setStyle(SkPaint::kStroke_Style);
                    paint.setColor(skColorFromArgb(commands[offset++]));
                    const jint x = commands[offset++];
                    const jint y = commands[offset++];
                    const jint ovalWidth = commands[offset++];
                    const jint ovalHeight = commands[offset++];
                    const jint strokeWidth = commands[offset++];
                    const jint strokeCap = commands[offset++];
                    const jint strokeJoin = commands[offset++];
                    const jint strokeMiter1000 = commands[offset++];
                    if (!isValidStrokeMetadata(strokeWidth, strokeCap, strokeJoin, strokeMiter1000)) {
                        return false;
                    }
                    paint.setStrokeWidth(static_cast<SkScalar>(strokeWidth));
                    paint.setStrokeCap(static_cast<SkPaint::Cap>(strokeCap));
                    paint.setStrokeJoin(static_cast<SkPaint::Join>(strokeJoin));
                    paint.setStrokeMiter(static_cast<SkScalar>(strokeMiter1000) / 1000.0f);
                    const SkRect rect = SkRect::MakeXYWH(static_cast<SkScalar>(x),
                                                         static_cast<SkScalar>(y),
                                                         static_cast<SkScalar>(ovalWidth),
                                                         static_cast<SkScalar>(ovalHeight));
                    canvas->drawOval(rect, paint);
                }
                break;
            }
            default:
                return false;
        }
        if (offset != recordEnd) {
            return false;
        }
    }
    return true;
}

static sk_sp<SkSurface> wrapTextureSurface(GrDirectContext* directContext,
                                           id<MTLTexture> texture,
                                           int width,
                                           int height) {
    GrMtlTextureInfo textureInfo;
    textureInfo.fTexture.retain((__bridge GrMTLHandle) texture);
    GrBackendRenderTarget renderTarget = GrBackendRenderTargets::MakeMtl(width, height, textureInfo);
    return SkSurfaces::WrapBackendRenderTarget(
            directContext,
            renderTarget,
            kTopLeft_GrSurfaceOrigin,
            kBGRA_8888_SkColorType,
            nullptr,
            nullptr);
}

static sk_sp<GrDirectContext> makeDirectContextForSurface(jlong nativeOpsPtr, id<MTLTexture> texture) {
    MTLContext* mtlc = getContextFromNativeOps(nativeOpsPtr);
    if (mtlc == nil || mtlc.device == nil || mtlc.commandQueue == nil) {
        return nullptr;
    }
    if (mtlc.device != texture.device) {
        return nullptr;
    }

    [mtlc.encoderManager endEncoder];

    void* contextKey = (__bridge void*) mtlc;
    {
        std::lock_guard<std::mutex> lock(gDirectContextMutex);
        auto existing = gDirectContextsByMtlContext.find(contextKey);
        if (existing != gDirectContextsByMtlContext.end()) {
            return existing->second;
        }
    }

    GrMtlBackendContext backendContext = {};
    backendContext.fDevice.retain((__bridge GrMTLHandle) mtlc.device);
    backendContext.fQueue.retain((__bridge GrMTLHandle) mtlc.commandQueue);
    sk_sp<GrDirectContext> directContext = GrDirectContexts::MakeMetal(backendContext);
    if (directContext != nullptr) {
        std::lock_guard<std::mutex> lock(gDirectContextMutex);
        gDirectContextsByMtlContext.emplace(contextKey, directContext);
    }
    return directContext;
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeGetContextId
        (JNIEnv* env, jclass cls, jlong nativeOpsPtr) {
    MTLContext* mtlc = getContextFromNativeOps(nativeOpsPtr);
    return mtlc == nil ? 0 : static_cast<jlong>(reinterpret_cast<uintptr_t>((__bridge void*) mtlc));
}

extern "C" JNIEXPORT jint JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeGetNativeAbiVersion
        (JNIEnv* env, jclass cls) {
    return NATIVE_ABI_VERSION;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeGetCommandStreamAbiId
        (JNIEnv* env, jclass cls) {
    return ABI_ID;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeGetBuildId
        (JNIEnv* env, jclass cls) {
    return env->NewStringUTF(BUILD_ID);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeRenderDiagnosticFrame
        (JNIEnv* env, jclass cls, jlong nativeOpsPtr, jlong metalTexturePtr,
         jint width, jint height, jlong frameTimeNanos) {
    @autoreleasepool {
        if (metalTexturePtr == 0 || width <= 0 || height <= 0) {
            return JNI_FALSE;
        }

        id<MTLTexture> texture = (__bridge id<MTLTexture>) reinterpret_cast<void*>(static_cast<uintptr_t>(metalTexturePtr));
        if (texture == nil || texture.device == nil) {
            return JNI_FALSE;
        }

        sk_sp<GrDirectContext> directContext = makeDirectContextForSurface(nativeOpsPtr, texture);
        if (directContext == nullptr) {
            return JNI_FALSE;
        }

        sk_sp<SkSurface> surface = wrapTextureSurface(directContext.get(), texture, width, height);
        if (surface == nullptr) {
            return JNI_FALSE;
        }

        drawDiagnosticPattern(surface->getCanvas(), width, height, frameTimeNanos);
        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        return JNI_TRUE;
    }
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeRenderPictureFrame
        (JNIEnv* env, jclass cls, jlong nativeOpsPtr, jlong metalTexturePtr,
         jint destinationX, jint destinationY, jint destinationWidth, jint destinationHeight,
         jint width, jint height, jlong frameTimeNanos, jbyteArray pictureArray) {
    @autoreleasepool {
        if (metalTexturePtr == 0 || destinationWidth <= 0 || destinationHeight <= 0 ||
                width <= 0 || height <= 0 || pictureArray == nullptr) {
            return JNI_FALSE;
        }

        id<MTLTexture> texture = (__bridge id<MTLTexture>) reinterpret_cast<void*>(static_cast<uintptr_t>(metalTexturePtr));
        if (texture == nil || texture.device == nil) {
            return JNI_FALSE;
        }

        jsize pictureSize = env->GetArrayLength(pictureArray);
        if (pictureSize <= 0) {
            return JNI_FALSE;
        }

        jboolean isCopy = JNI_FALSE;
        jbyte* pictureBytes = env->GetByteArrayElements(pictureArray, &isCopy);
        if (pictureBytes == nullptr) {
            return JNI_FALSE;
        }
        sk_sp<SkData> pictureData = SkData::MakeWithCopy(pictureBytes, static_cast<size_t>(pictureSize));
        env->ReleaseByteArrayElements(pictureArray, pictureBytes, JNI_ABORT);
        if (pictureData == nullptr) {
            return JNI_FALSE;
        }

        sk_sp<SkPicture> picture = SkPicture::MakeFromData(pictureData.get());
        if (picture == nullptr) {
            return JNI_FALSE;
        }

        sk_sp<GrDirectContext> directContext = makeDirectContextForSurface(nativeOpsPtr, texture);
        if (directContext == nullptr) {
            return JNI_FALSE;
        }
        sk_sp<SkSurface> surface = wrapTextureSurface(
                directContext.get(),
                texture,
                static_cast<int>(texture.width),
                static_cast<int>(texture.height));
        if (surface == nullptr) {
            return JNI_FALSE;
        }

        SkCanvas* canvas = surface->getCanvas();
        canvas->save();
        canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(destinationX),
                                          static_cast<SkScalar>(destinationY),
                                          static_cast<SkScalar>(destinationWidth),
                                          static_cast<SkScalar>(destinationHeight)));
        canvas->translate(static_cast<SkScalar>(destinationX),
                          static_cast<SkScalar>(destinationY));
        picture->playback(canvas);
        canvas->restore();
        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_PICTURE_FRAME destinationX=%d destinationY=%d destinationWidth=%d destinationHeight=%d width=%d height=%d bytes=%d rendered=true\n",
                     destinationX,
                     destinationY,
                     destinationWidth,
                     destinationHeight,
                     width,
                     height,
                     pictureSize);
        return JNI_TRUE;
    }
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeRenderCommandFrame
        (JNIEnv* env, jclass cls, jlong nativeOpsPtr, jlong metalTexturePtr,
         jint destinationX, jint destinationY, jint destinationWidth, jint destinationHeight,
         jint width, jint height, jlong frameTimeNanos, jintArray commandArray) {
    @autoreleasepool {
        if (metalTexturePtr == 0 || destinationWidth <= 0 || destinationHeight <= 0 ||
                width <= 0 || height <= 0 || commandArray == nullptr) {
            return JNI_FALSE;
        }

        id<MTLTexture> texture = (__bridge id<MTLTexture>) reinterpret_cast<void*>(static_cast<uintptr_t>(metalTexturePtr));
        if (texture == nil || texture.device == nil) {
            return JNI_FALSE;
        }

        jsize commandCount = env->GetArrayLength(commandArray);
        if (commandCount <= 0) {
            return JNI_FALSE;
        }
        const long long frameStartNanos = monotonicNanos();

        sk_sp<GrDirectContext> directContext = makeDirectContextForSurface(nativeOpsPtr, texture);
        if (directContext == nullptr) {
            return JNI_FALSE;
        }
        sk_sp<SkSurface> surface = wrapTextureSurface(
                directContext.get(),
                texture,
                static_cast<int>(texture.width),
                static_cast<int>(texture.height));
        if (surface == nullptr) {
            return JNI_FALSE;
        }

        jboolean isCopy = JNI_FALSE;
        jint* commands = env->GetIntArrayElements(commandArray, &isCopy);
        if (commands == nullptr) {
            return JNI_FALSE;
        }

        SkCanvas* canvas = surface->getCanvas();
        CommandReplayMetrics metrics;
        const long long drawStartNanos = monotonicNanos();
        canvas->save();
        canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(destinationX),
                                          static_cast<SkScalar>(destinationY),
                                          static_cast<SkScalar>(destinationWidth),
                                          static_cast<SkScalar>(destinationHeight)));
        canvas->translate(static_cast<SkScalar>(destinationX),
                          static_cast<SkScalar>(destinationY));
        bool rendered = drawCommandList(canvas, IntCommandWords{commands}, commandCount, width, height,
                                        (__bridge void*) getContextFromNativeOps(nativeOpsPtr), &metrics);
        canvas->restore();
        const long long drawNanos = monotonicNanos() - drawStartNanos;
        env->ReleaseIntArrayElements(commandArray, commands, JNI_ABORT);
        if (!rendered) {
            return JNI_FALSE;
        }

        const long long flushStartNanos = monotonicNanos();
        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        const long long flushNanos = monotonicNanos() - flushStartNanos;
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_FRAME destinationX=%d destinationY=%d destinationWidth=%d destinationHeight=%d width=%d height=%d commands=%d rendered=true\n",
                     destinationX,
                     destinationY,
                     destinationWidth,
                     destinationHeight,
                     width,
                     height,
                     commandCount);
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_TIMING totalNanos=%lld drawNanos=%lld flushNanos=%lld paragraphCommands=%d paragraphNanos=%lld shadowCommands=%d\n",
                     monotonicNanos() - frameStartNanos,
                     drawNanos,
                     flushNanos,
                     metrics.paragraphCommands,
                     metrics.paragraphNanos,
                     metrics.shadowCommands);
        return JNI_TRUE;
    }
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeRenderCommandBufferFrame
        (JNIEnv* env, jclass cls, jlong nativeOpsPtr, jlong metalTexturePtr,
         jint destinationX, jint destinationY, jint destinationWidth, jint destinationHeight,
         jint width, jint height, jlong frameTimeNanos, jbyteArray commandArray) {
    @autoreleasepool {
        if (metalTexturePtr == 0 || destinationWidth <= 0 || destinationHeight <= 0 ||
                width <= 0 || height <= 0 || commandArray == nullptr) {
            return JNI_FALSE;
        }

        id<MTLTexture> texture = (__bridge id<MTLTexture>) reinterpret_cast<void*>(static_cast<uintptr_t>(metalTexturePtr));
        if (texture == nil || texture.device == nil) {
            return JNI_FALSE;
        }

        jsize commandByteCount = env->GetArrayLength(commandArray);
        if (commandByteCount <= 0 || commandByteCount % static_cast<jsize>(sizeof(jint)) != 0) {
            return JNI_FALSE;
        }

        jboolean isCopy = JNI_FALSE;
        jbyte* commandBytes = env->GetByteArrayElements(commandArray, &isCopy);
        if (commandBytes == nullptr) {
            return JNI_FALSE;
        }

        jsize commandCount = commandByteCount / static_cast<jsize>(sizeof(jint));
        const long long frameStartNanos = monotonicNanos();

        sk_sp<GrDirectContext> directContext = makeDirectContextForSurface(nativeOpsPtr, texture);
        if (directContext == nullptr) {
            env->ReleaseByteArrayElements(commandArray, commandBytes, JNI_ABORT);
            return JNI_FALSE;
        }

        sk_sp<SkSurface> surface = wrapTextureSurface(
                directContext.get(),
                texture,
                static_cast<int>(texture.width),
                static_cast<int>(texture.height));
        if (surface == nullptr) {
            env->ReleaseByteArrayElements(commandArray, commandBytes, JNI_ABORT);
            return JNI_FALSE;
        }

        SkCanvas* canvas = surface->getCanvas();
        CommandReplayMetrics metrics;
        const long long drawStartNanos = monotonicNanos();
        canvas->save();
        canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(destinationX),
                                          static_cast<SkScalar>(destinationY),
                                          static_cast<SkScalar>(destinationWidth),
                                          static_cast<SkScalar>(destinationHeight)));
        canvas->translate(static_cast<SkScalar>(destinationX),
                          static_cast<SkScalar>(destinationY));
        bool rendered = drawCommandList(canvas, LittleEndianByteCommandWords{commandBytes}, commandCount,
                                        width, height, (__bridge void*) getContextFromNativeOps(nativeOpsPtr), &metrics);
        canvas->restore();
        const long long drawNanos = monotonicNanos() - drawStartNanos;
        env->ReleaseByteArrayElements(commandArray, commandBytes, JNI_ABORT);
        if (!rendered) {
            return JNI_FALSE;
        }

        const long long flushStartNanos = monotonicNanos();
        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        const long long flushNanos = monotonicNanos() - flushStartNanos;
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_FRAME destinationX=%d destinationY=%d destinationWidth=%d destinationHeight=%d width=%d height=%d commands=%d rendered=true\n",
                     destinationX,
                     destinationY,
                     destinationWidth,
                     destinationHeight,
                     width,
                     height,
                     commandCount);
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_TIMING totalNanos=%lld drawNanos=%lld flushNanos=%lld paragraphCommands=%d paragraphNanos=%lld shadowCommands=%d\n",
                     monotonicNanos() - frameStartNanos,
                     drawNanos,
                     flushNanos,
                     metrics.paragraphCommands,
                     metrics.paragraphNanos,
                     metrics.shadowCommands);
        return JNI_TRUE;
    }
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_jetbrains_desktop_JBRSkiaService_nativeRenderCommandDirectFrame
        (JNIEnv* env, jclass cls, jlong nativeOpsPtr, jlong metalTexturePtr,
         jint destinationX, jint destinationY, jint destinationWidth, jint destinationHeight,
         jint width, jint height, jlong frameTimeNanos, jobject commandBuffer, jint commandByteCount) {
    @autoreleasepool {
        if (metalTexturePtr == 0 || destinationWidth <= 0 || destinationHeight <= 0 ||
                width <= 0 || height <= 0 || commandBuffer == nullptr ||
                commandByteCount <= 0 || commandByteCount % static_cast<jint>(sizeof(jint)) != 0) {
            return JNI_FALSE;
        }

        jbyte* commandBytes = static_cast<jbyte*>(env->GetDirectBufferAddress(commandBuffer));
        if (commandBytes == nullptr) {
            return JNI_FALSE;
        }

        id<MTLTexture> texture = (__bridge id<MTLTexture>) reinterpret_cast<void*>(static_cast<uintptr_t>(metalTexturePtr));
        if (texture == nil || texture.device == nil) {
            return JNI_FALSE;
        }

        jsize commandCount = commandByteCount / static_cast<jint>(sizeof(jint));
        const long long frameStartNanos = monotonicNanos();
        const size_t mallocBaseBytes = defaultMallocBytesInUse();
        logMallocPhase("direct-start", mallocBaseBytes, commandCount);

        sk_sp<GrDirectContext> directContext = makeDirectContextForSurface(nativeOpsPtr, texture);
        if (directContext == nullptr) {
            return JNI_FALSE;
        }
        if (directDiagnosticModeIs("context")) {
            logMallocPhase("direct-context-return", mallocBaseBytes, commandCount);
            std::fprintf(stderr,
                         "JBR_SKIA_INTEROP_COMMAND_FRAME destinationX=%d destinationY=%d destinationWidth=%d destinationHeight=%d width=%d height=%d commands=%d rendered=true\n",
                         destinationX,
                         destinationY,
                         destinationWidth,
                         destinationHeight,
                         width,
                         height,
                         commandCount);
            std::fprintf(stderr,
                         "JBR_SKIA_INTEROP_COMMAND_TIMING totalNanos=%lld drawNanos=0 flushNanos=0 paragraphCommands=0 paragraphNanos=0 shadowCommands=0\n",
                         monotonicNanos() - frameStartNanos);
            return JNI_TRUE;
        }

        sk_sp<SkSurface> surface = wrapTextureSurface(
                directContext.get(),
                texture,
                static_cast<int>(texture.width),
                static_cast<int>(texture.height));
        if (surface == nullptr) {
            return JNI_FALSE;
        }
        logMallocPhase("direct-surface", mallocBaseBytes, commandCount);
        if (directDiagnosticModeIs("surface")) {
            std::fprintf(stderr,
                         "JBR_SKIA_INTEROP_COMMAND_FRAME destinationX=%d destinationY=%d destinationWidth=%d destinationHeight=%d width=%d height=%d commands=%d rendered=true\n",
                         destinationX,
                         destinationY,
                         destinationWidth,
                         destinationHeight,
                         width,
                         height,
                         commandCount);
            std::fprintf(stderr,
                         "JBR_SKIA_INTEROP_COMMAND_TIMING totalNanos=%lld drawNanos=0 flushNanos=0 paragraphCommands=0 paragraphNanos=0 shadowCommands=0\n",
                         monotonicNanos() - frameStartNanos);
            return JNI_TRUE;
        }

        SkCanvas* canvas = surface->getCanvas();
        CommandReplayMetrics metrics;
        const long long drawStartNanos = monotonicNanos();
        canvas->save();
        canvas->clipRect(SkRect::MakeXYWH(static_cast<SkScalar>(destinationX),
                                          static_cast<SkScalar>(destinationY),
                                          static_cast<SkScalar>(destinationWidth),
                                          static_cast<SkScalar>(destinationHeight)));
        canvas->translate(static_cast<SkScalar>(destinationX),
                          static_cast<SkScalar>(destinationY));
        bool rendered = drawCommandList(canvas, LittleEndianByteCommandWords{commandBytes}, commandCount,
                                        width, height, (__bridge void*) getContextFromNativeOps(nativeOpsPtr), &metrics);
        canvas->restore();
        const long long drawNanos = monotonicNanos() - drawStartNanos;
        logMallocPhase("direct-draw", mallocBaseBytes, commandCount);
        if (!rendered) {
            return JNI_FALSE;
        }
        if (directDiagnosticModeIs("draw")) {
            std::fprintf(stderr,
                         "JBR_SKIA_INTEROP_COMMAND_FRAME destinationX=%d destinationY=%d destinationWidth=%d destinationHeight=%d width=%d height=%d commands=%d rendered=true\n",
                         destinationX,
                         destinationY,
                         destinationWidth,
                         destinationHeight,
                         width,
                         height,
                         commandCount);
            std::fprintf(stderr,
                         "JBR_SKIA_INTEROP_COMMAND_TIMING totalNanos=%lld drawNanos=%lld flushNanos=0 paragraphCommands=%d paragraphNanos=%lld shadowCommands=%d\n",
                         monotonicNanos() - frameStartNanos,
                         drawNanos,
                         metrics.paragraphCommands,
                         metrics.paragraphNanos,
                         metrics.shadowCommands);
            return JNI_TRUE;
        }

        const long long flushStartNanos = monotonicNanos();
        directContext->flushAndSubmit(surface.get(), GrSyncCpu::kYes);
        const long long flushNanos = monotonicNanos() - flushStartNanos;
        purgeUnlockedResourcesAfterFrame(directContext.get());
        logMallocPhase("direct-flush", mallocBaseBytes, commandCount);
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_FRAME destinationX=%d destinationY=%d destinationWidth=%d destinationHeight=%d width=%d height=%d commands=%d rendered=true\n",
                     destinationX,
                     destinationY,
                     destinationWidth,
                     destinationHeight,
                     width,
                     height,
                     commandCount);
        std::fprintf(stderr,
                     "JBR_SKIA_INTEROP_COMMAND_TIMING totalNanos=%lld drawNanos=%lld flushNanos=%lld paragraphCommands=%d paragraphNanos=%lld shadowCommands=%d\n",
                     monotonicNanos() - frameStartNanos,
                     drawNanos,
                     flushNanos,
                     metrics.paragraphCommands,
                     metrics.paragraphNanos,
                     metrics.shadowCommands);
        return JNI_TRUE;
    }
}
