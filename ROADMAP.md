# JBR Skia Compose Zero-Copy Roadmap

This is the quick-open checklist for the local PoC. The detailed design and checkpoint log lives in
`SKIA_COMPOSE_ZERO_COPY_POC_PLAN.md`.

## Current Status

- [x] Worktrees created for JBR, Runtime API, Skiko, CMP, and Magic Jewel.
- [x] Magic Jewel standalone validation app created.
- [x] JBR private `com.jetbrains.desktop.JBRSkia` service and public Runtime API mirror added.
- [x] Skiko discovery/fallback path added with structured `SKIKO_JBR_INTEROP_FALLBACK reason=...` markers.
- [x] CMP routes ComposePanel to the JBR Skia command path when the strict compatibility gate passes.
- [x] Magic Jewel report compares old/new modes and records command/picture/fallback counters.
- [x] Magic Jewel summary exposes machine-readable FPS fields for app, Swing, popup, picture, and command markers.
- [x] Command mode validated with zero picture replay and zero fallback markers.
- [x] Skiko command mode preserves the last meaningful Compose command stream across Swing-driven interop-only repaint passes, avoiding blank/flashing frames while real Compose animation frames still update.
- [x] Skiko command mode no longer lets suspiciously tiny `FullScene` command frames evict the last meaningful animated frame, which reduces transient blank/flashing frames during fragile repaint ordering.
- [x] Magic Jewel reports can assert a minimum number of non-frozen Compose frame markers via `EXPECT_MIN_APP_NEW_FRAMES`, and the command-probe suite includes a passing `commands-live-animation` case.
- [x] The `commands-live-animation` case can force one test-only tiny `FullScene` command stream and assert the `SKIKO_JBR_INTEROP_TINY_FULL_SCENE_INJECTED` marker.
- [x] CMP now tags command frames as `FullScene` or `InteropOnly`, and Skiko uses that explicit frame kind for preservation replay instead of relying only on command-stream size.
- [x] CMP's full `JbrSkiaCommandRecorderTest` desktop suite is green for ABI 99, so command-stream golden expectations are a usable regression gate again.
- [x] CMP's JBR command-frame adapter is runtime-reflective, so `compose.ui` compiles against the normal Skiko coordinate while patched Skiko still receives the optional command delegate when present.

## Command ABI Coverage

- [x] ABI 1-13: basic command framing, solid rect/oval/line, transforms, clip, saveLayer.
- [x] ABI 14: inline image drawing.
- [x] ABI 15: image cache refs.
- [x] ABI 16: image cache clearing.
- [x] ABI 17: text-command rendering semantics.
- [x] ABI 18-25: paragraph text and paragraph style/layout metadata.
- [x] ABI 26: clip path.
- [x] ABI 27: draw path.
- [x] ABI 28: draw arc.
- [x] ABI 29: solid round-rect drawing.
- [x] ABI 30: linear-gradient rectangle fill.
- [x] ABI 31: 64-bit command capability gate.
- [x] ABI 32: linear-gradient rounded-rectangle fill.
- [x] ABI 33: radial-gradient rectangle fill.
- [x] ABI 34: radial-gradient rounded-rectangle fill.
- [x] ABI 35: linear-gradient path fill.
- [x] ABI 36: radial-gradient path fill.
- [x] ABI 37: sweep-gradient rectangle fill.
- [x] ABI 38: sweep-gradient rounded-rectangle fill.
- [x] ABI 39: sweep-gradient path fill.
- [x] ABI 40/native 2: scoped destination surface identity.
- [x] ABI 41/native 3: scoped destination context identity.
- [x] ABI 42: single-key image cache eviction.
- [x] ABI 43: native text commands carry JBR-resolved font-family names.
- [x] ABI 44: image-shader rectangle fills use serialized image refs and JBR-owned shader reconstruction.
- [x] ABI 45: linear-gradient stroked rectangles use serialized gradient and stroke metadata.
- [x] ABI 46: linear-gradient stroked rounded rectangles use serialized gradient, radii, and stroke metadata.
- [x] ABI 47: radial-gradient stroked rectangles use serialized gradient and stroke metadata.
- [x] ABI 48: radial-gradient stroked rounded rectangles use serialized gradient, radii, and stroke metadata.
- [x] ABI 49: sweep-gradient stroked rectangles use serialized gradient and stroke metadata.
- [x] ABI 50: sweep-gradient stroked rounded rectangles use serialized gradient, radii, and stroke metadata.
- [x] ABI 51: `BlendMode.Plus` solid fill rectangles use an explicit blend-mode fill command.
- [x] ABI 52: `ColorFilter.tint(..., BlendMode.SrcIn)` solid fill rectangles use an explicit color-filter fill command.
- [x] ABI 53: dash path-effect stroked lines use serialized dash intervals and phase.
- [x] ABI 54: `saveLayer` with `ColorFilter.tint(..., BlendMode.SrcIn)` uses an explicit color-filter layer command.
- [x] ABI 55: cached image refs with `ColorFilter.tint(..., BlendMode.SrcIn)` use an explicit color-filter image command.
- [x] ABI 56: tint color-filter descriptor handles can be defined in-frame and referenced by fill-rect commands.
- [x] ABI 57: tint color-filter handles are cached per destination context, reused across frames, and explicitly evictable.
- [x] ABI 58: effect descriptors use a generic typed/versioned envelope; tint/SrcIn color filters are descriptor type 1 version 1.
- [x] ABI 59: solid fill rectangles support `BlendMode.Multiply` through the structured blend-mode command.
- [x] ABI 60: solid fill rectangles support `BlendMode.Screen` through the structured blend-mode command.
- [x] ABI 61: solid fill rectangles support `BlendMode.Overlay` through the structured blend-mode command.
- [x] ABI 62: solid fill rectangles support `BlendMode.Darken` through the structured blend-mode command.
- [x] ABI 63: solid fill rectangles support `BlendMode.Lighten` through the structured blend-mode command.
- [x] ABI 64: solid fill rectangles support `BlendMode.Difference` through the structured blend-mode command.
- [x] ABI 65: solid fill rectangles support `BlendMode.Exclusion` through the structured blend-mode command.
- [x] ABI 66: solid fill rectangles support `BlendMode.ColorDodge` through the structured blend-mode command.
- [x] ABI 67: solid fill rectangles support `BlendMode.ColorBurn` through the structured blend-mode command.
- [x] ABI 68: solid fill rectangles support `BlendMode.Hardlight` through the structured blend-mode command.
- [x] ABI 69: solid fill rectangles support `BlendMode.Softlight` through the structured blend-mode command.
- [x] ABI 70: solid fill rectangles support `BlendMode.Hue` through the structured blend-mode command.
- [x] ABI 71: solid fill rectangles support `BlendMode.Saturation` through the structured blend-mode command.
- [x] ABI 72: solid fill rectangles support `BlendMode.Color` through the structured blend-mode command.
- [x] ABI 73: solid fill rectangles support `BlendMode.Luminosity` through the structured blend-mode command.
- [x] ABI 74: saveLayer/graphics-layer replay supports direct Skia blend modes through a structured save-layer blend command.
- [x] ABI 75: saveLayer/graphics-layer replay supports combining a direct Skia blend mode with a tint/SrcIn color filter through a structured save-layer command.
- [x] ABI 76: color-matrix color filters use a typed effect descriptor handle and solid fill-rect reference command.
- [x] ABI 77: lighting color filters use a typed effect descriptor handle and solid fill-rect reference command.
- [x] ABI 78: saveLayer/graphics-layer replay can apply typed color-filter descriptor handles through a structured save-layer reference command.
- [x] ABI 79: cached image refs can apply typed color-filter descriptor handles through a structured image reference command.
- [x] ABI 80: saveLayer/graphics-layer replay can combine direct blend modes with typed color-filter descriptor handles.
- [x] ABI 81: command capability negotiation has a second 64-bit word so future shader/effect commands have strict room to grow.
- [x] ABI 82: graphics-layer `BlurEffect` can be serialized as a JBR-owned blur image-filter descriptor and applied through saveLayer.
- [x] ABI 83: graphics-layer `OffsetEffect` can be serialized as a JBR-owned offset image-filter descriptor and applied through saveLayer.
- [x] ABI 84: nested render effects can snapshot child image-filter descriptors for chains such as `OffsetEffect(BlurEffect(...), ...)`.
- [x] CMP now preserves structured `CompositeShader` metadata when both child shaders already have JBR-compatible metadata.
- [x] ABI 85: shader descriptor handles rebuild known shader trees inside JBR-owned Skia and draw rectangles by shader handle.
- [x] ABI 86: RuntimeEffect/SKSL shader descriptors can carry ASCII source plus raw float uniforms for a first JBR-owned generic shader MVP.
- [x] ABI 87: RuntimeEffect/SKSL shader descriptors can reference child shader handles, enabling descriptor-tree shader composition inside JBR-owned Skia.
- [x] ABI 88: RuntimeEffect/SKSL descriptors include a stable source hash that CMP writes and JBR Java/native validation verifies before compilation.
- [x] RuntimeEffect compile failures emit parseable JBR markers and Magic Jewel reports expose/assert the marker count.
- [x] ABI 89: RuntimeEffect/SKSL descriptors carry named uniform schema metadata that CMP writes and JBR Java/native validation checks before compilation.
- [x] ABI 90: RuntimeEffect/SKSL descriptors carry named child shader schema metadata, and JBR can use `SkRuntimeEffectBuilder` when all children are named.
- [x] ABI 91: RuntimeEffect color-filter descriptors carry ASCII SKSL, source hash, raw float uniforms, and named uniform schema metadata through the typed color-filter handle path.
- [x] ABI 92: RuntimeEffect color-filter descriptors can reference child color-filter handles and named child schema metadata, so JBR builds child-backed `SkRuntimeEffect` color filters inside its own Skia runtime.
- [x] ABI 93: dash path-effect metadata can stroke rectangles through a structured command, extending dash support beyond line-only replay.
- [x] ABI 94: dash path-effect metadata can stroke rounded rectangles through a structured command.
- [x] ABI 95-98: path-effect metadata extends structured replay through dashed path strokes and path-effect draw-path refs.
- [x] ABI 99: graphics-layer shadows add a high-word-gated
  `COMMAND_DRAW_SHADOW_PATH` op backed by JBR-owned `SkShadowUtils::DrawShadow` for rectangular, rounded, and
  generic path outlines without requiring a stream ABI bump.
- [x] High-word capability `COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_COLOR_FILTER`: shader descriptors can wrap typed
  color-filter descriptor handles through `COMMAND_SHADER_DESCRIPTOR_COLOR_FILTER`, so shader fills preserve Compose
  paint color filters inside JBR-owned Skia without requiring a stream ABI bump.
- [x] ABI 100: `drawPoints(PointMode.Points)` records `COMMAND_DRAW_POINTS` with strict high-word capability
  negotiation and JBR-owned Skia point-mode replay; `PointMode.Lines`/`Polygon` continue to lower to line commands.
- [x] ABI 101: simple native text commands carry font weight/width/slant metadata so JBR resolves styled typefaces
  inside its own Skia runtime for family-backed `COMMAND_DRAW_TEXT_UTF16` replay.
- [x] ABI 102: transformed shader wrappers serialize as `COMMAND_SHADER_DESCRIPTOR_TRANSFORM`, preserving a child
  shader handle plus fixed1000 local 3x3 matrix for JBR-owned `SkShader::makeWithLocalMatrix` replay.
- [x] RuntimeEffect builder failures emit parseable JBR markers, and Magic Jewel has a bad-child-name probe/report assertion.
  - [x] Native RuntimeEffect shader replay rejects child type mismatches before assigning `SkRuntimeEffectBuilder`
    children, and Magic Jewel has a post-recording child-type corruption probe that asserts `stage=child-type` fallback
    instead of a native abort.
  - [x] RuntimeEffect child-type crash regression subset passed at
    `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-120945/suite.tsv`.
  - [x] Native RuntimeEffect positional child replay now validates Skia child types before assignment, preventing
    `SkRuntimeEffectBuilder::BuilderChild` aborts; the focused crash regression passed at
    `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-223409/suite.tsv`.
  - [x] Descriptor handle use markers now distinguish "handle was consumed by replay" from "handle was defined in the command stream"; RuntimeEffect rows assert use markers for shader and color-filter refs.
  - [x] Descriptor handle cache-hit markers distinguish same-frame define/use from steady-state reuse across frames; stable
    shader/effect rows assert cache-hit markers.
  - [x] RuntimeEffect compile/build failure markers now include hashed diagnostic fields, and the bad-child probe asserts the
    specific `stage=missing-child` builder path.
  - [x] Invalid descriptor-use fallback probe corrupts a known-good effect-handle reference after CMP recording and asserts
    structured `command-stream-invalid` fallback instead of silent stale/partial rendering.
  - [x] Magic Jewel RuntimeEffect conformance probes cover pure color, uniform-only animation, child-only composition, combined child+uniform, and builder-failure fallback cases.
  - [x] Live RuntimeEffect conformance subset passed for ABI 90: pure color, uniform-only, child-only, combined child+uniform, and the bad-child builder-failure fallback.
  - [x] Add a live animation preservation regression job that asserts non-frozen command-mode frame markers continue advancing.
  - [x] Extend the live animation preservation job to force a transient tiny/full-scene frame and assert the preservation-path marker.
  - [x] Refresh ABI 90 runtime artifacts and pass `commands-live-animation` live command replay with `fallback_new_count=0`, `unsupported=none`, `jbr_command_frames=432`, `app_new_frames=432`, and one tiny-frame injection marker.

## Near-Term Rendering Work

- [x] Complete serialized sweep-gradient payloads for rectangles, rounded rectangles, and paths.
- [ ] Generic shader strategy:
  - [x] Short term: keep rejecting opaque/unknown shader pointers and add serialized command payloads for known shader families.
  - [x] Medium term design sketch: document a JBR-owned shader factory ABI so Skiko can request shader construction inside JBR's Skia runtime. See `doc/skia-shader-factory.md`.
  - [ ] End-state requirement: generic shaders/effects are implemented, not merely documented; unsupported descriptors must fall back only when a runtime genuinely lacks the negotiated capability.
  - [x] Define the first shader/effect descriptor schema with stable type ids, payload lengths, lifecycle operations, and fallback reasons.
  - [x] First descriptor-shaped ABI slice: in-frame tint color-filter handles with define/use validation and native replay.
  - [x] Second descriptor-shaped ABI slice: destination-context-scoped tint handle reuse plus explicit handle eviction.
  - [x] Third descriptor-shaped ABI slice: generic `COMMAND_DEFINE_EFFECT_DESCRIPTOR` envelope with descriptor type/version/payload validation and tint/SrcIn as the first schema.
  - [x] Fourth descriptor-shaped ABI slice: color-matrix color-filter descriptor handles with row-major 4x5 matrix payloads and JBR-owned `SkColorFilters::Matrix` reconstruction.
  - [x] Fifth descriptor-shaped ABI slice: lighting color-filter descriptor handles with multiply/add ARGB payloads and JBR-owned `SkColorFilters::Lighting` reconstruction.
  - [x] Sixth descriptor-shaped ABI slice: saveLayer/graphics-layer paints can reference typed color-filter descriptor handles without raw Skia pointers.
  - [x] Seventh descriptor-shaped ABI slice: cached image draws can reference typed color-filter descriptor handles without raw Skia pointers.
  - [x] Eighth descriptor-shaped ABI slice: saveLayer/graphics-layer paints can combine direct blend modes with typed descriptor color filters.
  - [x] Ninth descriptor-shaped ABI slice: graphics-layer blur render effects use a typed image-filter descriptor and high-word capability gate.
  - [x] Tenth descriptor-shaped ABI slice: graphics-layer offset render effects use a typed image-filter descriptor and high-word capability gate.
  - [x] Preserve CMP metadata for `CompositeShader` children as the first descriptor-tree prerequisite.
  - [x] Strict JBR validator coverage for malformed effect descriptors: unknown type, unsupported version, bad payload count/length, unsupported blend mode, and evicted-handle use.
  - [x] Add a shader descriptor-handle ABI so composite shader trees can be rebuilt inside JBR-owned Skia.
  - [x] JBR emits structured effect/shader handle lifecycle markers for native and Java2D command submissions, and Magic Jewel reports/asserts descriptor define markers from the full log.
  - [x] Magic Jewel has a descriptor eviction probe that overfills CMP's effect/shader handle caches and asserts JBR-side define/evict markers.
  - [x] Stable Magic Jewel descriptor rows assert reuse with max-count gates so unchanged effect/shader descriptors are not redefined every frame.
  - [x] JBR emits explicit shader/effect cache-hit markers for descriptor uses that reuse handles from previous frames, and Magic Jewel can assert those markers on stable descriptor rows.
  - [x] Skiko clears CMP-owned command descriptor/image caches on JBR surface changes, with a resize probe proving descriptors are redefined for the new surface.
  - [x] Resize descriptor-redefine probe now also asserts cache-hit recovery after the post-resize fresh define.
  - [x] Forced context-change descriptor probe exercises the `contextChanged` cache-clear path without relying on physical
    multi-monitor migration.
  - [x] Shader descriptor redefine probes now mirror the effect-handle resize/context-change rows, requiring RuntimeEffect
    shader handles to be redefined after same-context resize and forced context migration.
  - [x] Graphics-layer render-effect descriptors now have a forced context-change probe that requires command-cache
    clearing, effect-handle redefinition, and cache-hit recovery after the new destination context is established.
  - [x] Graphics-layer render-effect descriptors also have a same-context resize probe that requires command-cache
    clearing, effect-handle redefinition, and cache-hit recovery after the resized surface is established.
  - [x] Missing CMP command-cache clear hook is a structured Skiko fallback (`command-cache-clear-unavailable`) instead of a silent stale-handle risk.
  - [x] Graphics-layer blur, offset, and chained render-effect rows now assert JBR effect-handle define/use/cache-hit
    markers, proving ABI 82-84 image-filter descriptors are consumed by replay and reused across frames.
  - [ ] Implement JBR-owned shader/effect handles: Skiko/CMP serializes descriptors or create requests, JBR constructs objects inside its Skia runtime, draw commands reference versioned handles, and handles are scoped/evicted by destination context.
  - [x] Implement a concrete generic-shader MVP: CMP serializes a `RuntimeEffect` descriptor with ASCII SKSL source and raw float uniforms; JBR compiles/caches it inside the destination context and draw commands reference the JBR-owned handle.
  - [x] Skiko prerequisite for RuntimeEffect color-filter support: `RuntimeEffect.makeColorFilter(Data?)` now wraps Skia
    `SkRuntimeEffect::makeColorFilter`.
  - [x] CMP prerequisite for RuntimeEffect color-filter support: desktop/skiko `RuntimeEffectColorFilter(...)` keeps
    JBR-serializable SKSL/uniform metadata next to the normal Skiko color filter.
  - [x] RuntimeEffect color-filter descriptor MVP: CMP serializes a named-uniform color-filter descriptor, Skiko requires the ABI 91 capability bit, and JBR compiles/applies it inside the destination context.
  - [x] Skiko prerequisite for RuntimeEffect color-filter child support: `RuntimeEffect.makeColorFilter(Data?, Array<ColorFilter?>?)` now wraps Skia's child color-filter overload.
  - [x] RuntimeEffect color-filter child handles: CMP serializes child color-filter descriptor handles plus named child schema, Skiko requires ABI 92, and JBR reconstructs the child-backed color filter inside the destination context.
  - [x] Magic Jewel compatibility matrix can mask JBR's advertised high command capabilities and now has a launch-level row that removes only `COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_COLOR_FILTER`.
  - [ ] Extend Skia runtime effects via descriptor payloads: remaining shader-family fallback markers.
  - [x] RuntimeEffect child color-filter descriptors have old/new screenshot parity coverage.
  - [x] RuntimeEffect shader + color-filter descriptor command row passes through CMP -> Skiko -> JBR native replay with no picture fallback.
  - [x] Transform-aware shader descriptors pass through CMP -> Skiko -> JBR native replay with no picture fallback,
    and the compatibility matrix has an exact high-word missing row for `COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_TRANSFORM`.
  - [x] Solid-color shader descriptors pass through CMP -> Skiko -> JBR native replay with no picture fallback, and
    the compatibility matrix has an exact high-word missing row for `COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_COLOR`.
  - [x] Perlin/noise shader descriptors pass through CMP -> Skiko -> JBR native replay for `FractalNoiseShader(...)`
    and `TurbulenceShader(...)`, replacing the former raw Skia fallback rows with JBR-owned
    `COMMAND_SHADER_DESCRIPTOR_PERLIN_NOISE` replay.
  - [x] JBR command-stream validator fixtures reject malformed Perlin/noise shader descriptor payloads for invalid
    kind, base frequency, octave count, and tile size.
  - [x] Magic Jewel raw linear/radial/sweep gradient shader fallback sentinels prove raw Skiko-owned gradient shaders
    still stay on structured `shader` fallback instead of crossing the command ABI.
  - [x] Magic Jewel raw image-shader fallback sentinel proves raw Skiko-owned image shaders stay picture-backed while
    descriptor-backed Compose `ImageShader` stays on JBR command replay.
  - [x] Add old/new screenshot parity coverage for shader + color-filter descriptor composition.
  - [ ] Add shader/effect lifecycle commands for create, use, context-scoped cache hit, compile failure, eviction, and context migration invalidation; never pass raw Skiko `SkShader*`, `SkImageFilter*`, or `SkRuntimeEffect*` pointers across the ABI.
  - [x] Add RuntimeEffect conformance probes in Magic Jewel: one pure color shader, one child-shader composition, one uniform animation, one builder/compile-failure fallback, and one old-runtime capability fallback.
  - [x] Add Magic Jewel probes that force invalid descriptor-handle fallback without relying on raw Skiko `SkShader*` or `SkRuntimeEffect*` pointers.
  - [x] Add Magic Jewel probes that force handle creation, reuse, context migration, and eviction without relying on raw Skiko `SkShader*` or `SkRuntimeEffect*` pointers.
  - [x] Add ABI/version tests for shader/effect handle creation, use-after-free rejection, context migration invalidation, and old/new fallback markers.
    - [x] Add a descriptor-version corruption probe that mutates a shader descriptor version after recording and asserts
      structured `command-stream-invalid` fallback instead of partial rendering.
    - [x] Add a descriptor use-after-evict probe that removes a shader handle immediately before use and asserts
      structured `command-stream-invalid` fallback instead of stale-handle replay.
    - [x] Add JBR validator fixtures for shader descriptor type/version/payload rejection plus undefined and evicted
      shader-handle use.
    - [x] Add JBR validator fixtures for shader+color-filter wrapper descriptors, including missing shader/effect
      handle rejection.
    - [x] Harden JBR command-stream validator coverage for effect descriptors with child handles: input image filters,
      RuntimeEffect color-filter children, and chained path effects.
    - [x] Add positive JBR validator fixtures for supported input image-filter and chained path-effect child references.
    - [x] Add JBR validator fixtures for evicted effect handles reused as later image-filter/path-effect descriptor
      children.
    - [x] Full old/new packaged artifact matrix covers real old JBR API/native/desktop, old Skiko, and old CMP fallback
      markers:
      `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-183840/matrix.tsv`.
  - [ ] Long term: revisit true generic shader support only after Skiko's fast path no longer creates Skia C++ objects in a separate bundled runtime.
- [x] Strict recorder fallback for invalid gradient stops and excessive gradient color counts.
- [x] Strict recorder fallback for invalid rounded-rectangle radii with gradient paints.
- [x] Strict recorder fallback for transformed gradient shaders.
- [x] Strict recorder fallback for composite/opaque shader wrappers.
- [x] Live Magic Jewel fallback probe for opaque raw Skia shaders, asserting the strict recorder reports `shader`
  unsupported markers and uses picture replay instead of guessing descriptor semantics.
- [x] Live Magic Jewel fallback probe for composite shader trees with an opaque/raw Skia child, asserting the strict
  recorder reports `shader` unsupported markers and uses picture replay instead of serializing only the known child.
- [x] Live Magic Jewel fallback probe for Skia Perlin/noise shaders, asserting the strict recorder reports `shader`
  unsupported markers until JBR owns an explicit descriptor for that family.
- [x] Live Magic Jewel fallback probe for Skia turbulence shaders, asserting the strict recorder reports `shader`
  unsupported markers until JBR owns an explicit descriptor for that Perlin variant.
- [x] Live Magic Jewel fallback probe for Skia picture shaders, asserting the strict recorder reports `shader`
  unsupported markers until JBR owns an explicit descriptor for recorded-picture shader content.
- [x] Live Magic Jewel fallback probe for raw Skia RuntimeEffect shaders, asserting the strict recorder reports `shader`
  unsupported markers unless the shader was created through CMP's metadata-backed `RuntimeEffectShader` descriptor path.
- [x] Live Magic Jewel fallback probe for raw Skia RuntimeEffect color filters, asserting the strict recorder reports
  `colorFilter` unsupported markers unless the filter was created through CMP's metadata-backed
  `RuntimeEffectColorFilter` descriptor path.
- [x] Live Magic Jewel fallback probe for raw Skia blend color filters, asserting
  `ColorFilter.makeBlend(...).asComposeColorFilter()` reports `colorFilter` unsupported markers while metadata-backed
  Compose tint/color-matrix/lighting/RuntimeEffect descriptors continue through command replay.
- [x] Live Magic Jewel fallback probe for raw Skia-backed graphics-layer `RenderEffect`, asserting the strict recorder
  reports `graphicsLayer:renderEffect` unsupported markers until JBR owns an explicit descriptor for that image-filter
  family.
- [x] Live Magic Jewel fallback probe for raw Skia discrete path effects, asserting
  `PathEffect.makeDiscrete(...).asComposePathEffect()` reports `pathEffect` unsupported markers while
  Compose-created dash/corner/stamped/chained path-effect descriptors continue through command replay.
- [x] Transformed shader wrappers graduated from strict fallback to transform-aware shader descriptor command replay.
- [x] Linear-gradient stroke paint graduated from strict fallback to serialized command replay.
- [x] More strict shader fallback tests for nonfinite gradient metadata that survives shader construction.
- [x] Live Magic Jewel recorder-level fallback probes for color filters and path effects.
- [x] Live Magic Jewel recorder-level fallback probe for unsupported saveLayer layer paint.
- [x] Live Magic Jewel recorder-level fallback probe for unsupported image paint.
- [x] Live Magic Jewel rendering probe for linear-gradient stroked rectangles.
- [x] Remaining near-term generic shader fallback probes beyond transformed/composite/opaque shader wrappers.
- [x] Expand screenshot assertions to check core primitive and gradient probe colors/regions explicitly.
- [x] Improve Magic Jewel visual fidelity so labels use Jewel `Text` and `JewelTheme.defaultTextStyle`.
- [x] Prefer fidelity-first text image replay by default so command mode preserves resolved Jewel font/size/alignment until JBR-owned typefaces exist.
- [x] Add font-family metadata to native text commands without sharing Skia `SkTypeface*` pointers.
- [x] Add simple native text font style metadata without sharing Skia `SkTypeface*` pointers.
- [x] Resolve default-family simple native text through JBR's font manager so empty-family records still honor style
  metadata without a Skiko-owned typeface.
- [x] Prefer Compose text style and generic-family metadata when recording native text commands, only consulting the
  resolved Skia typeface when metadata is otherwise unavailable.
- [x] Tighten the Magic Jewel native-text command probe so it requires both simple text commands and paragraph text
  commands, keeping ABI 101 simple font-style metadata covered by live command replay.
- [x] Add JBR command-stream validator fixtures for valid paragraph text plus invalid simple/paragraph font size,
  weight, width, slant, and family length metadata.
- [x] Add CMP ui-text desktop tests that verify generic family, font weight, and italic style metadata in recorded
  simple-text and paragraph-text command payloads.
- [x] Resolve Compose generic `sans-serif`/`serif`/`monospace`/`cursive` family names through JBR-owned macOS font
  candidates for native simple and paragraph text; Magic Jewel's generic-family command row passed with zero fallback at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-092536/suite.tsv`.
- [x] Short broad Magic Jewel command-probe sweep passed after adding the generic-family native-text row:
  90/90 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-092836/suite.tsv`;
  `commands-native-generic-font-text` stayed on command replay with `jbr_command_frames=389`.
- [x] Generic-family native-text screenshot parity row passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-101144/suite.tsv`
  with zero fallback, zero picture frames, and `jbr_command_frames=1569`.
- [x] Generic-family native-text command and screenshot probes now cover all four Compose generic names
  (`sans-serif`, `serif`, `monospace`, `cursive`); focused command and parity rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-101926/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-102017/suite.tsv`.
- [x] Forced-context generic-family native-text command and screenshot rows passed with command-cache clearing and no
  fallback at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-103534/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-103617/suite.tsv`.
- [x] Same-context resize generic-family native-text command and screenshot rows passed with command-cache clearing and
  no fallback at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-111927/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-112005/suite.tsv`.
- [x] Focused native-text screenshot parity subset passed after adding generic-family resolution:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-101448/suite.tsv`.
  Baseline native text, generic-family native text, and forced-context native text all kept zero fallback and zero
  picture frames.
- [x] Add a Magic Jewel forced-context native-text command probe that requires simple/paragraph text commands,
  `contextChanged=true`, and command-cache clearing without falling back to picture replay.
- [x] Add a Magic Jewel forced-context dynamic image-cache command probe that requires cached image refs, single-key
  CMP/JBR image evictions, `contextChanged=true`, command-cache clearing, and zero whole-cache clears.
- [x] Add a Magic Jewel forced-context cached-image screenshot parity row that freezes image refs for deterministic
  visual comparison while requiring command replay, `contextChanged=true`, command-cache clearing, and zero whole-cache
  image clears.
- [x] Rebuild ABI 43 local artifacts and pass a native-text Magic Jewel command smoke with paragraph commands and screenshot assertion.
- [x] Add narrow image-shader rectangle support without sharing raw `SkShader*` pointers.
- [x] Add narrow linear-gradient stroked-rectangle support without sharing raw `SkShader*` pointers.
- [x] Add narrow linear-gradient stroked rounded-rectangle support without sharing raw `SkShader*` pointers.
- [x] Add narrow radial-gradient stroked-rectangle support without sharing raw `SkShader*` pointers.
- [x] Add narrow radial-gradient stroked rounded-rectangle support without sharing raw `SkShader*` pointers.
- [x] Add narrow sweep-gradient stroked-rectangle support without sharing raw `SkShader*` pointers.
- [x] Add narrow sweep-gradient stroked rounded-rectangle support without sharing raw `SkShader*` pointers.
- [x] Add dedicated `drawPoints(PointMode.Points)` command replay without falling back to picture mode.
- [x] Add narrow `BlendMode.Plus` solid fill-rectangle support through a versioned command instead of picture fallback.
- [x] Expand blend-mode command coverage beyond `Plus` fill rectangles with the first additional exact Skia mapping: `BlendMode.Multiply`.
- [x] Add `BlendMode.Screen` fill-rectangle command replay and screenshot-region validation.
- [x] Add `BlendMode.Overlay` fill-rectangle command replay and screenshot-region validation.
- [x] Add `BlendMode.Darken` fill-rectangle command replay and screenshot-region validation.
- [x] Add `BlendMode.Lighten` fill-rectangle command replay and screenshot-region validation.
- [x] Add `BlendMode.Difference` fill-rectangle command replay and screenshot-region validation.
- [x] Add `BlendMode.Exclusion` fill-rectangle command replay and screenshot-region validation.
- [x] Add `BlendMode.ColorDodge` fill-rectangle command replay and screenshot-region validation.
- [x] Add `BlendMode.ColorBurn` fill-rectangle command replay and screenshot-region validation.
- [x] Add `BlendMode.Hardlight` fill-rectangle command replay and screenshot-region validation.
- [x] Add `BlendMode.Softlight` fill-rectangle command replay and screenshot-region validation.
- [x] Add `BlendMode.Hue` fill-rectangle command replay and screenshot-region validation.
- [x] Add `BlendMode.Saturation` fill-rectangle command replay and screenshot-region validation.
- [x] Add `BlendMode.Color` fill-rectangle command replay and screenshot-region validation.
- [x] Add `BlendMode.Luminosity` fill-rectangle command replay and screenshot-region validation.
- [x] Continue blend-mode command coverage mode-by-mode only after each mode has exact Skia-vs-Java2D semantics documented:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-185229/suite.tsv`.
- [x] Command-recorded graphics layers:
  - [x] Mark Skiko `GraphicsLayer`/`RenderNode` draws as an explicit strict fallback while command replay cannot encode layer contents/effects.
  - [x] Replace the simple 2D graphics-layer fallback with nested command recording for layer-local content plus alpha, translation, scale, rotationZ, and balanced save/saveLayer/restore replay.
  - [x] Support rectangular graphics-layer clips by replaying the layer outline through existing `COMMAND_CLIP_RECT` inside the saved layer scope.
  - [x] Support rounded graphics-layer clips by serializing rounded/conic outline paths as quadratic path commands and replaying them through `COMMAND_CLIP_PATH`.
  - [x] Support generic path outline clips by replaying `Outline.Generic` through existing `COMMAND_CLIP_PATH`.
  - [x] Support directly mapped non-SrcOver graphics-layer blend modes through a bounded save-layer blend command.
  - [x] Support tint/SrcIn graphics-layer color filters by reusing the existing save-layer color-filter command.
  - [x] Support graphics layers that combine a directly mapped blend mode with tint/SrcIn color filtering through a bounded save-layer blend/color-filter command.
  - [x] Support color-matrix and lighting graphics-layer color filters by referencing typed descriptor handles from saveLayer paints.
  - [x] Support a first rectangular graphics-layer shadow slice by replaying a JBR-owned blur image-filter descriptor and layer-local shadow fill before the layer content.
  - [x] Support rounded-outline graphics-layer shadows by clipping the offset shadow source path inside the JBR-owned blur image-filter layer.
  - [x] Support generic-path graphics-layer shadows by reusing the offset shadow source path replay inside the JBR-owned blur image-filter layer.
  - [x] Improve graphics-layer shadow fidelity by replaying separate ambient and spot blur passes from Compose's ambient/spot shadow colors.
  - [x] Replace blur-approximation graphics-layer shadows with a high-word-gated direct shadow command that lets JBR
    call `SkShadowUtils::DrawShadow` against the destination Skia canvas for rectangular, rounded, and generic outlines.
  - [x] Thread Compose's dynamic root-light geometry into command recording so direct shadows use the same light center,
    z adjustment, radius, and ambient/spot alpha factors as Skiko render-node replay.
  - [x] JBR timing markers now report `shadowCommands`, and Magic Jewel's shadow rows assert at least one direct
    native shadow replay per frame sample so the tests prove `SkShadowUtils::DrawShadow` is being exercised.
  - [x] Validate `CompositingStrategy.ModulateAlpha` graphics-layer replay stays on the command path.
  - [x] Support simple `CompositingStrategy.Offscreen` graphics-layer replay by clipping layer contents to bounds inside the command saveLayer.
  - [x] Support first 3D/camera graphics-layer replay by flattening Compose's layer transform into `COMMAND_CONCAT_MATRIX33`.
  - [x] Add Magic Jewel command and screenshot parity coverage for off-center-pivot near-camera 3D graphics-layer
    transforms.
  - [x] Extend graphics-layer command replay beyond the current subset: dynamic root-lighting parity, broader image-filter surfaces, and combined/edge-case 3D camera transform coverage:
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-185632/suite.tsv`.
  - [x] Add render-effect descriptors for graphics-layer `RenderEffect` once the JBR-owned effect-handle ABI can construct the needed Skia image filters.
  - [x] Add an explicit expected-fallback row for graphics-layer `RenderEffect` combined with paint color-filter metadata.
  - [x] Support graphics-layer `RenderEffect` combined with tint/SrcIn color-filter metadata through nested image-filter and color-filter saveLayer command replay.
  - [x] Extend nested graphics-layer `RenderEffect` replay to blend-mode and descriptor color-filter combinations if parity is acceptable.
- [x] Add narrow tint color-filter solid fill-rectangle support through a versioned command instead of picture fallback.
- [x] Add narrow tint color-filter `saveLayer` support through a versioned command instead of picture fallback.
- [x] Add narrow tint color-filter cached-image support through a versioned command instead of picture fallback.
- [x] Expand color-matrix/lighting color-filter command coverage to solid rectangles, saveLayer/graphics-layer paints, and cached images through typed descriptors.
- [x] Expand color-filter command coverage to image filters, runtime effects, and generic shaders.
  - [x] Image draw tint/color-matrix color-filter rows replay through command mode with no picture fallback and now have old/new screenshot parity rows.
  - [x] RuntimeEffect shader paints with typed color-filter descriptors replay through a wrapped shader descriptor.
  - [x] Expand the same wrapped-shader path to screenshot parity rows and broader shader families.
  - [x] Focused color-filter command matrix passed for image filters, RuntimeEffect color filters, shader-plus-filter
    wrappers, and graphics-layer renderEffect/filter stacks:
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-184719/suite.tsv`.
- [x] Add narrow dash path-effect stroked-line support through a versioned command instead of picture fallback.
- [x] Extend dash path-effect command replay to stroked rectangles with ABI 93 validation and native/Java2D replay.
- [x] Extend dash path-effect command replay to stroked rounded rectangles with ABI 94 validation and native/Java2D replay.
- [x] Expand path-effect command coverage beyond dash stroked lines through dashed arbitrary paths plus corner, stamped,
  and chained path-effect descriptors.
- [x] Add screenshot-level text-presence assertions using stable pixel regions for top/bottom Magic Jewel labels.
- [x] Add screenshot-level text placement assertions using stable dark-pixel bounding boxes.
- [x] Add Magic Jewel primary-button screenshot assertions for white text and centered placement, so `Pulse`-style
  provided-content-color regressions are caught by command-mode screenshots and old/new parity.
- [x] Add paragraph-row screenshot assertions for native-text layout probes.
- [x] Investigate observed text alignment drift in Magic Jewel/Jewel labels on the command path.
- [x] Add screenshot-region assertions for the Plus/Multiply blend-mode probe.
- [x] `drawPoints(PointMode.Lines/Polygon)` now reuses existing stroke-line command replay, with CMP recorder tests and a Magic Jewel `commands-point-lines` live row.
- [x] Add a dedicated command for `drawPoints(PointMode.Points)` dot/cap semantics, with a Magic Jewel `commands-point-dots` live row.

## Validation Harness

- [x] Focused CMP recorder tests for command encodings.
- [x] Full CMP command-recorder regression gate: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest` passed for ABI 99 after dynamic shadow-lighting context threading.
- [x] Focused direct-shadow command metric gate: `CASES="commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-232255/suite.tsv`, with `jbr_shadow_commands_max=1` for every row.
- [x] Descriptor invalid-stream regression gate: `CASES="commands-invalid-descriptor-use-fallback commands-invalid-descriptor-version-fallback commands-runtime-effect-child-type-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-233318/suite.tsv`.
- [x] Descriptor stale-handle regression gate: `CASES="commands-invalid-descriptor-use-fallback commands-invalid-descriptor-use-after-evict-fallback commands-invalid-descriptor-version-fallback commands-runtime-effect-child-type-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-234018/suite.tsv`.
- [x] Broad command-probe suite passed after adding descriptor-version and stale-handle rows:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-234428/suite.tsv`. The only picture replay remains the intentional `commands-invalid-gradient-fallback` row.
- [x] Live animation regression gate: `CASES=commands-live-animation DURATION_SECONDS=6 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed for ABI 90 at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-045238/commands-live-animation/report.md`.
- [x] RuntimeEffect conformance gate: `CASES="commands-runtime-effect-pure-color commands-runtime-effect-uniform-only commands-runtime-effect-child-only commands-runtime-effect-shader commands-runtime-effect-build-fallback" DURATION_SECONDS=6 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed for ABI 90 at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-050347/suite.tsv`.
- [x] RuntimeEffect color-filter descriptor gate: `CASES="commands-runtime-effect-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed for ABI 91 at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-083405/suite.tsv`.
- [x] ABI 91 broader descriptor regression gate: RuntimeEffect shader/color-filter, image color-matrix, typed color filters, and graphics-layer color-filter rows passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-083644/suite.tsv`.
- [x] RuntimeEffect color-filter child descriptor gate: `CASES="commands-runtime-effect-color-filter-child" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed for ABI 92 at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-090456/suite.tsv`.
- [x] ABI 92 broader descriptor regression gate: RuntimeEffect shader/color-filter/child-color-filter, image color-matrix, typed color filters, and graphics-layer color-filter rows passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-090711/suite.tsv`.
- [x] RuntimeEffect descriptor handle-use gate: shader, color-filter, and child color-filter RuntimeEffect rows passed with JBR handle-use assertions at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-092522/suite.tsv`.
- [x] RuntimeEffect shader + color-filter descriptor gate: `CASES="commands-runtime-effect-shader-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-003307/suite.tsv`.
- [x] RuntimeEffect shader + color-filter screenshot parity row: `CASES="parity-runtime-effect-shader-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh` passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-003744/suite.tsv`.
  - Note: the current diff is useful as a smoke/parity guard but is not pixel-perfect. The remaining deltas are dominated by animation phase/AA/text-sensitive regions, while the command path reports zero fallback, zero unsupported commands, and JBR command replay for the row.
- [x] Skiko exact-bit compatibility guard: `JbrSkiaInteropTest.rejectsMissingShaderDescriptorColorFilterCommandCapability` models an old JBR that advertises every previous high-word capability but omits `COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_COLOR_FILTER`, and verifies `command-capability-mismatch` fallback.
  - Validation: `./gradlew --no-daemon --no-configuration-cache awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` passed in `/Users/rock3r/src/skiko-jbr-skia-poc/skiko`.
- [x] Broaden wrapped shader + color-filter coverage to a normal gradient shader family.
  - CMP recorder regression: linear-gradient shader + tint color-filter records a child linear-gradient descriptor, a color-filter descriptor, a wrapper shader descriptor, and a shader-ref rect command.
  - Magic Jewel live probe: `CASES="commands-linear-gradient-shader-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-004855/suite.tsv`.
  - Validation note: `:compose:ui:ui-graphics:compileKotlinDesktop` passed; `compileTestKotlinDesktop` remains blocked by the existing local `compose.ui` command-delegate wiring failure before the new recorder test can execute.
- [x] Broaden wrapped shader + color-filter coverage to composite shader trees.
  - CMP recorder regression: composite linear/radial shader + tint color-filter records child descriptors, the composite descriptor, the color-filter descriptor, a wrapper shader descriptor, and a shader-ref rect command.
  - Magic Jewel live probe: `CASES="commands-composite-shader-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-005357/suite.tsv`.
- [x] Broaden wrapped shader + color-filter coverage to image shader trees.
  - CMP recorder regression: image shader + tint color-filter records the image ref, image shader descriptor, color-filter descriptor, wrapper shader descriptor, and shader-ref rect command.
  - Magic Jewel live probe: `CASES="commands-image-shader-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-010440/suite.tsv`.
- [x] Add focused screenshot parity rows for stable wrapped shader + color-filter cases.
  - `parity-image-shader-color-filter`, `parity-composite-shader-color-filter`, and `parity-linear-gradient-shader-color-filter` passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-010715/suite.tsv`.
  - Caveat: these share the same current shader-parity noise floor as the RuntimeEffect row; they are smoke parity guards, while geometry/color subregion gates remain the stricter signal.
- [x] Tighten wrapped-shader screenshot parity with local probe-region gates.
  - Added `composeShaderImage`, `composeShaderComposite`, and `composeShaderLinear` comparator regions plus per-row thresholds.
  - Validation passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-011323/suite.tsv`.
- [x] Golden/diff screenshot harness for the full mixed Swing/Jewel/Compose Magic Jewel scene.
- [x] Capture the app window only in old/new renderer modes, with deterministic sizing, theme, font inputs, animation phase, and seeded content.
- [x] Add configurable per-region screenshot parity gates for header controls, Compose canvas, Swing island, and right probe strip.
- [x] Add metric-only Compose subregions for left backdrop, center animation, and bottom labels to prepare tighter visual masks.
- [x] Tighten Compose/Jewel-owned region thresholds after splitting text/font raster drift from geometry/pixel ownership drift.
- [x] Add screenshot parity jobs that run both old and new renderers for the same rich scene, emit old/new images, and fail on unexpected Compose/Jewel geometry/color drift. Expected Swing text rendering differences must be isolated to Swing-owned regions and documented in the report.
- [x] Add a rich-content old/new parity suite with Jewel controls/text, Compose primitives/text/images/effects, Swing islands, popups/menus, and always-on animation sampled at deterministic phases.
- [x] Make the macOS parity capture shadowless and log selected window id/bounds, so old/new comparisons use stable
  window contents instead of variable `screencapture` shadow extents.
- [x] Persist diff images and per-region parity metrics in the Magic Jewel report and `summary.properties`.
- [x] Preserve parity metrics in `summary.properties`, `report.md`, and suite TSV rows even when the comparator fails a
  threshold, so failures remain actionable.
- [x] Add named old/new screenshot parity suite rows for the rich baseline, RuntimeEffect descriptors, and graphics-layer effects.
- [x] Add `parity-geometry-clean`, a text/Swing-island-free parity row for stricter geometry-baseline experiments.
- [x] Add `parity-native-text`, an opt-in native-text parity row with text-aware thresholds and paragraph-command assertions.
- [x] RuntimeEffect screenshot parity rows passed for pure-color, uniform-only, and child-shader descriptors:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-055435/suite.tsv`.
- [x] Combined RuntimeEffect screenshot parity row passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-060341/suite.tsv`.
- [x] RuntimeEffect screenshot parity subset now runs pure-color, uniform-only, child-only, and combined child+uniform rows by default:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-080056/suite.tsv`.
- [x] RuntimeEffect color-filter screenshot parity row passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-084511/suite.tsv`.
- [x] RuntimeEffect child color-filter screenshot parity row passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-091255/suite.tsv`.
- [x] Expanded RuntimeEffect screenshot parity subset now runs pure-color, uniform-only, child-only, combined child+uniform,
  color-filter, and child color-filter rows:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-091420/suite.tsv`.
- [x] Native-text screenshot parity row passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-062651/suite.tsv`.
- [x] Clean-geometry screenshot parity row passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-061323/suite.tsv`.
- [x] Clean-geometry screenshot parity row passed with tighter small-region gates for `composePurpleRect`,
  `composeTopProgress`, and `composeBottomSwatches`:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-064534/suite.tsv`.
- [x] Default named screenshot parity suite passed end to end:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-061514/suite.tsv`.
- [x] Default named screenshot parity suite passed with native-text row included:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-062821/suite.tsv`.
- [x] Focused native-text screenshot parity passed after ABI 101 simple text style metadata and JBR default-family style
  resolution:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-194040/suite.tsv`.
- [x] Forced-context native-text screenshot parity passed with context-change and command-cache-clear markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-225104/suite.tsv`.
- [x] Focused native-text and forced-context native-text screenshot parity rows passed on the latest ABI 102 artifacts
  after the raw color/path fallback probes:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-085916/suite.tsv`.
  Both rows reported `avg_delta=3.229`, `bad_pixel_ratio=0.06048`, `compose_bad_pixel_ratio=0.08925`, and exact
  bottom-swatch parity, keeping native text in the current text-aware parity envelope.
- [x] Native-text screenshot parity now hard-gates the text-heavy `composeBottomLabels` region at 0.17; the focused
  native-text and forced-context native-text rows passed with `composeBottomLabels_badPixelRatio=0.15308` at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-090242/suite.tsv`.
- [x] Magic Jewel screenshot parity `suite.tsv` now surfaces `compose_bottom_labels_bad_pixel_ratio`; the focused
  native-text rows passed with the new TSV column at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-090707/suite.tsv`.
- [x] Magic Jewel screenshot parity `suite.tsv` now surfaces transport counters (`fallbacks`, `jbr_picture_frames`,
  `jbr_command_frames`) alongside visual metrics; focused native-text rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-091021/suite.tsv`.
- [x] Native-text screenshot parity now emits and hard-gates the `composeParagraphProbes` region at 0.19; the focused
  native-text and forced-context native-text rows passed with `compose_paragraph_probes_bad_pixel_ratio=0.17241` at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-091848/suite.tsv`.
- [x] Graphics-layer effects screenshot parity row passed with focused probe scoping:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-055842/suite.tsv`.
- [x] Shadowless screenshot parity subset passed for clean geometry, native text, RuntimeEffect pure/uniform/child/shader
  variants, RuntimeEffect color-filter variants, and graphics-layer effects:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-130835/suite.tsv`.
- [x] Default shadowless screenshot parity suite passed end to end:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-131506/suite.tsv`.
- [x] Rectangular graphics-layer shadow screenshot parity row passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-143422/suite.tsv`.
- [x] Rounded and generic-path graphics-layer shadow screenshot parity rows passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-151108/suite.tsv`.
- [x] Rectangular, rounded, and generic-path graphics-layer shadow screenshot parity rows passed with two-pass
  ambient/spot shadow replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-172910/suite.tsv`.
- [x] Simple Offscreen graphics-layer screenshot parity row passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-160638/suite.tsv`.
- [x] RotationX graphics-layer screenshot parity row passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-163641/suite.tsv`.
- [x] RotationY graphics-layer screenshot parity row passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-164917/suite.tsv`.
- [x] Combined rotationX + rotationY graphics-layer screenshot parity row passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-165948/suite.tsv`.
- [x] Near-camera combined-rotation graphics-layer screenshot parity row passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-171119/suite.tsv`.
- [x] Magic Jewel graphics-layer command probe runs without fallback markers and validates the replayed layer region in the screenshot assertion.
- [x] Magic Jewel clipped graphics-layer command probe runs without fallback markers and validates clipped cyan/purple layer content.
- [x] Magic Jewel rounded clipped graphics-layer command probe runs without fallback markers and validates clipped cyan/purple layer content.
- [x] Magic Jewel generic-path clipped graphics-layer command probe runs without fallback markers and validates clipped cyan/purple layer content.
- [x] Magic Jewel graphics-layer tint color-filter probe is wired into the command-probe suite and screenshot assertion; live execution waits for refreshed ABI 74 JBR/Skiko artifacts.
- [x] Magic Jewel graphics-layer combined blend/color-filter probe is wired into the command-probe suite; live execution waits for refreshed ABI 75 JBR/Skiko artifacts.
- [x] Magic Jewel color-matrix descriptor probe is wired into the command-probe suite; live execution waits for refreshed ABI 76 JBR/Skiko artifacts.
- [x] Magic Jewel lighting descriptor probe is wired into the command-probe suite; live execution waits for refreshed ABI 77 JBR/Skiko artifacts.
- [x] Magic Jewel graphics-layer blur render-effect probe is wired into the command-probe suite; live execution waits for refreshed ABI 82 JBR/Skiko artifacts.
- [x] Magic Jewel graphics-layer offset render-effect probe is wired into the command-probe suite; live execution waits for refreshed ABI 83 JBR/Skiko artifacts.
- [x] Magic Jewel graphics-layer chained render-effect probe is wired into the command-probe suite; live execution waits for refreshed ABI 84 JBR/Skiko artifacts.
- [x] Magic Jewel rectangular graphics-layer shadow probe is wired into the command-probe suite and screenshot assertion; focused row passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-134341/suite.tsv`.
- [x] Graphics-layer render-effect plus shadow regression subset passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-134631/suite.tsv`.
- [x] Remaining command-probe tail from image filters through graphics-layer shadow and invalid-gradient fallback passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-141933/suite.tsv`.
- [x] Magic Jewel rounded graphics-layer shadow probe is wired into the command-probe suite; focused row passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-144839/suite.tsv`.
- [x] Graphics-layer shadow/rounded-clip regression subset passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-144925/suite.tsv`.
- [x] Magic Jewel generic-path graphics-layer shadow probe is wired into the command-probe suite; focused row passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-145744/suite.tsv`.
- [x] Graphics-layer rectangular/rounded/path shadow regression subset passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-145831/suite.tsv`.
- [x] Graphics-layer rectangular/rounded/path shadow regression subset passed with two-pass ambient/spot replay at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-172736/suite.tsv`.
- [x] Graphics-layer renderEffect + tint color-filter expected-fallback row passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-174104/suite.tsv`.
- [x] Graphics-layer renderEffect + tint color-filter command row passed through nested saveLayer replay at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-174650/suite.tsv`.
- [x] Graphics-layer renderEffect + tint color-filter screenshot parity row passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-174829/suite.tsv`.
- [x] Magic Jewel graphics-layer rotationX and Offscreen fallback rows passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-150748/suite.tsv`.
- [x] Magic Jewel graphics-layer ModulateAlpha command row passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-151636/suite.tsv`.
- [x] Compact graphics-layer command/fallback matrix passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-151820/suite.tsv`.
- [x] Full default command-probe suite passed at short duration with the latest graphics-layer rows:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-152730/suite.tsv`.
- [x] Magic Jewel graphics-layer Offscreen command row and rotationX fallback row passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-155623/suite.tsv`.
- [x] Compact graphics-layer matrix with Offscreen in the supported set passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-155924/suite.tsv`.
- [x] Magic Jewel concat-transform command row passed with ABI 99 artifacts at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-162333/suite.tsv`.
- [x] Magic Jewel graphics-layer rotationX command row passed through matrix replay at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-163107/suite.tsv`.
- [x] Compact graphics-layer matrix with rotationX in the supported command set passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-163854/suite.tsv`.
- [x] Magic Jewel graphics-layer rotationY command row passed through matrix replay at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-164836/suite.tsv`.
- [x] Magic Jewel combined rotationX + rotationY graphics-layer command row passed through matrix replay at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-165908/suite.tsv`.
- [x] Magic Jewel near-camera combined-rotation graphics-layer command row passed through matrix replay at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-171044/suite.tsv`.
- [x] Compact graphics-layer matrix with rotationX and rotationY in the supported command set passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-165236/suite.tsv`.
- [x] Compact graphics-layer matrix with combined rotation in the supported command set passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-170137/suite.tsv`.
- [x] Compact graphics-layer matrix with near-camera perspective in the supported command set passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-171313/suite.tsv`.
- [x] Compact graphics-layer matrix passed after the two-pass ambient/spot shadow replay change at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-173302/suite.tsv`.
- [x] Persist old/new screenshots and pass/fail thresholds in the Magic Jewel report.
- [x] Make the parity suite window-only end to end: launch old/new renderers, capture the Magic Jewel window by id/title, crop no whole-screen screenshots, and keep the capture metadata in the report.
- [x] Add deterministic animation-phase controls for parity runs so animated progress, blend probes, gradients, and Swing islands can be compared at repeatable frame phases.
- [x] Add a live non-frozen command-mode animation smoke after the preservation guard so FPS and frame counters prove the sample still animates outside parity mode.
- [x] Replace the command-stream size heuristic for interop-only repaint preservation with an explicit CMP/Skiko frame-kind marker once the recorder can tag full-scene vs. interop-only paints.
- [x] Magic Jewel reports summarize `CMP_JBR_COMMAND_FRAME_KIND` counts in `report.md` and `summary.properties`.
- [x] Focused CMP recorder tests for unsupported saveLayer layer-paint fallback.
- [x] Focused CMP recorder tests for unsupported image paint fallback.
- [x] Focused Skiko compatibility/fallback tests.
- [x] JBR Java service compile smoke.
- [x] JBR native dylib compile smoke.
- [x] Runtime API shim rebuild.
- [x] Magic Jewel report smoke for each ABI checkpoint.
- [x] Magic Jewel command-mode smoke for ABI 40 surface identity.
- [x] Quiet-machine benchmark pass using existing SKP/report paths.
- [x] Magic Jewel benchmark-suite wrapper for SKP picture, command, stable-image, dynamic-image, and resize workloads.
- [x] Magic Jewel benchmark suite supports selectable `CASES` for targeted reruns.
- [x] Benchmark suite writes a machine-readable `suite.tsv` with sample counts, CPU, FPS, and report paths.
- [x] Magic Jewel reports include host CPU count and load-average metadata for noisy-machine benchmark context.
- [x] ABI 43 quiet-machine benchmark refresh: `/tmp/magic-jewel-quiet-benchmark-abi43-20260430-110714/suite.tsv`.
- [x] ABI 44 quiet-machine benchmark refresh: `/tmp/magic-jewel-quiet-benchmark-abi44-20260430-122457/suite.tsv`.
- [x] ABI 45 quiet-machine benchmark refresh: `/tmp/magic-jewel-quiet-benchmark-abi45-20260430-131201/suite.tsv`.
- [x] Async-profiler integration in Magic Jewel report.
- [x] CI-friendly parser for Magic Jewel report summary.
- [x] Machine-readable Skiko surface-change marker count in Magic Jewel reports.
- [x] Live resize/surface-change Magic Jewel smoke requiring at least one Skiko surface-change marker.
- [x] Popup/layered Swing-over-Compose Magic Jewel smoke with screenshot pixels and popup paint markers.
- [x] Parser-level coverage for popup paint marker thresholds and machine-summary fields.
- [x] Parser-level screenshot count fields in `summary.properties`.
- [x] Selectable Magic Jewel command-probe suite for core primitives, gradients, text, popup, and fallback cases.
- [x] Command-probe suite writes a machine-readable `suite.tsv`.
- [x] ABI 43 command-probe suite refresh: `/tmp/magic-jewel-command-probe-abi43-20260430-111549/suite.tsv`.
- [x] ABI 44 image-shader command smoke: `/tmp/magic-jewel-abi44-image-shader-smoke-3/report.md`.
- [x] ABI 44 command-probe suite refresh: `/tmp/magic-jewel-command-probe-abi44-full-20260430-115943/suite.tsv`.
- [x] ABI 45 gradient-stroke command smoke: `/tmp/magic-jewel-abi45-gradient-stroke-smoke/report.md`.
- [x] ABI 45 gradient-stroke command-probe row: `/tmp/magic-jewel-command-probe-abi45-gradient-stroke-20260430-125550/suite.tsv`.
- [x] ABI 45 command-probe suite refresh: `/tmp/magic-jewel-command-probe-abi45-full-20260430-130148/suite.tsv`.
- [x] ABI 46 gradient stroke rounded-rectangle smoke: `/tmp/magic-jewel-abi46-gradient-stroke-round-rect-smoke-2/report.md`.
- [x] ABI 46 gradient-surfaces command-probe row: `/tmp/magic-jewel-command-probe-abi46-gradient-surfaces/suite.tsv`.
- [x] ABI 47 radial-gradient stroke smoke: `/tmp/magic-jewel-abi47-radial-gradient-stroke-smoke-3/report.md`.
- [x] ABI 47 gradient-surfaces command-probe row: `/tmp/magic-jewel-command-probe-abi47-gradient-surfaces/suite.tsv`.
- [x] ABI 48 radial-gradient stroke rounded-rectangle smoke: `/tmp/magic-jewel-abi48-radial-gradient-stroke-round-rect-smoke-2/report.md`.
- [x] ABI 48 gradient-surfaces command-probe row: `/tmp/magic-jewel-command-probe-abi48-gradient-surfaces/suite.tsv`.
- [x] ABI 49 sweep-gradient stroke smoke: `/tmp/magic-jewel-abi49-sweep-gradient-stroke-smoke-2/report.md`.
- [x] ABI 49 gradient-surfaces command-probe row: `/tmp/magic-jewel-command-probe-abi49-gradient-surfaces/suite.tsv`.
- [x] ABI 50 sweep-gradient stroke rounded-rectangle smoke: `/tmp/magic-jewel-abi50-sweep-gradient-stroke-round-rect-smoke-2/report.md`.
- [x] ABI 50 gradient-surfaces command-probe row: `/tmp/magic-jewel-command-probe-abi50-gradient-surfaces/suite.tsv`.
- [x] ABI 51 Plus blend-mode fill-rect smoke: `/tmp/magic-jewel-abi51-fill-rect-plus-blend-smoke/report.md`.
- [x] ABI 51 blend-mode command-probe row: `/tmp/magic-jewel-command-probe-abi51-blend-mode/suite.tsv`.
- [x] ABI 52 tint color-filter fill-rect smoke: `/tmp/magic-jewel-abi52-tint-color-filter-smoke/report.md`.
- [x] ABI 52 color-filter command-probe row: `/tmp/magic-jewel-command-probe-abi52-color-filter/suite.tsv`.
- [x] ABI 53 dashed path-effect stroke-line smoke: `/tmp/magic-jewel-abi53-dashed-path-effect-smoke/report.md`.
- [x] ABI 53 path-effect command-probe row: `/tmp/magic-jewel-command-probe-abi53-path-effect/suite.tsv`.
- [x] ABI 54 saveLayer tint color-filter smoke: `/tmp/magic-jewel-abi54-save-layer-color-filter-smoke-3/report.md`.
- [x] ABI 54 saveLayer tint color-filter command-probe row: `/tmp/magic-jewel-command-probe-abi54-save-layer-filter/suite.tsv`.
- [x] ABI 55 image tint color-filter smoke: `/tmp/magic-jewel-abi55-image-color-filter-smoke/report.md`.
- [x] ABI 55 image tint color-filter command-probe row: `/tmp/magic-jewel-command-probe-abi55-image-color-filter/suite.tsv`.
- [x] ABI 56 color-filter descriptor-handle command-probe row: `/tmp/magic-jewel-command-probe-abi56-color-filter-handle/suite.tsv`.
- [x] ABI 57 persistent color-filter descriptor-handle command-probe row: `/tmp/magic-jewel-command-probe-abi57-color-filter-handle/suite.tsv`.
- [x] ABI 61 Overlay blend-mode command-probe row: `/tmp/magic-jewel-command-probe-abi61-overlay-blend-4/suite.tsv`.
- [x] ABI 62 Darken blend-mode command-probe row: `/tmp/magic-jewel-command-probe-abi62-darken-blend/suite.tsv`.
- [x] ABI 63 Lighten blend-mode command-probe row: `/tmp/magic-jewel-command-probe-abi63-lighten-blend/suite.tsv`.
- [x] ABI 64 Difference blend-mode command-probe row: `/tmp/magic-jewel-command-probe-abi64-difference-blend-2/suite.tsv`.
- [x] ABI 65 Exclusion blend-mode command-probe row: `/tmp/magic-jewel-command-probe-abi65-exclusion-blend/suite.tsv`.
- [x] ABI 66 ColorDodge blend-mode command-probe row: `/tmp/magic-jewel-command-probe-abi66-color-dodge-blend/suite.tsv`.
- [x] ABI 67 ColorBurn blend-mode command-probe row: `/tmp/magic-jewel-command-probe-abi67-color-burn-blend/suite.tsv`.
- [x] ABI 68 Hardlight blend-mode command-probe row: `/tmp/magic-jewel-command-probe-abi68-hardlight-blend/suite.tsv`.
- [x] ABI 69 Softlight blend-mode command-probe row: `/tmp/magic-jewel-command-probe-abi69-softlight-blend/suite.tsv`.
- [x] ABI 70 Hue blend-mode command-probe row: `/tmp/magic-jewel-command-probe-abi70-hue-blend/suite.tsv`.
- [x] ABI 71 Saturation blend-mode command-probe row: `/tmp/magic-jewel-command-probe-abi71-saturation-blend/suite.tsv`.
- [x] ABI 72 Color blend-mode command-probe row: `/tmp/magic-jewel-command-probe-abi72-color-blend/suite.tsv`.
- [x] ABI 73 Luminosity blend-mode command-probe row: `/tmp/magic-jewel-command-probe-abi73-luminosity-blend-2/suite.tsv`.
- [x] Explicit graphics-layer fallback command-probe row after narrowing benign layer fallback: `/tmp/magic-jewel-command-probe-graphics-layer-fallback-2/suite.tsv`.
- [x] Real undecorated Swing popup-window smoke captured separately by window id.
- [x] RuntimeEffect source/uniform/child/build-fallback conformance subset through JBR command replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-050347/suite.tsv`.
- [x] RuntimeEffect bad-child build-fallback diagnostic row with `stage=missing-child` assertion:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-075650/suite.tsv`.
- [x] RuntimeEffect color-filter descriptor row through JBR command replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-083405/suite.tsv`.
- [x] RuntimeEffect color-filter child descriptor row through JBR command replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-090456/suite.tsv`.
- [x] ABI 91 descriptor regression subset through JBR command replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-083644/suite.tsv`.
- [x] ABI 92 descriptor regression subset through JBR command replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-090711/suite.tsv`.
- [x] RuntimeEffect descriptor handle-use assertions through JBR command replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-092522/suite.tsv`.
- [x] RuntimeEffect child-type crash regression subset through JBR command replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-120945/suite.tsv`.
- [x] Full Magic Jewel command-probe sweep after RuntimeEffect child-type crash hardening: 46/46 rows passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-121521/suite.tsv`.
- [x] Graphics-layer RenderEffect descriptor assertions through JBR command replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-100545/suite.tsv`.
- [x] ABI 93 dashed path-effect line/rectangle probe through JBR command replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-102456/suite.tsv`.
- [x] ABI 94 dashed path-effect line/rectangle/rounded-rectangle probe through JBR command replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-103512/suite.tsv`.
- [x] ABI 95 dashed arbitrary-path probe through JBR command replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-104709/suite.tsv`.
- [x] ABI 96 corner path-effect descriptor probe through JBR command replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-105953/suite.tsv`.
- [x] ABI 97 stamped path-effect descriptor probe through JBR command replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-110918/suite.tsv`.
- [x] ABI 98 chained path-effect descriptor probe through JBR command replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-111807/suite.tsv`.
- [x] ABI 99: `COMMAND_CONCAT_MATRIX33` records arbitrary 3x3 canvas transforms with raw-float SkMatrix payloads.
- [x] Full Magic Jewel command-probe sweep after ABI 98 path-effect descriptors: 45/45 rows passed, including live
  animation, mixed Swing popups/menus, text/images, image/composite shaders, RuntimeEffects, color filters, blend modes,
  graphics layers, render effects, saveLayer filters, path-effect descriptors, descriptor lifecycle probes, and explicit
  fallback rows:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-112035/suite.tsv`.
- [x] Compact graphics-layer matrix after nested renderEffect + tint color-filter replay: 15/15 rows passed with
  `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-175049/suite.tsv`.
- [x] Focused graphics-layer renderEffect + blend/filter command rows: 4/4 passed with `fallbacks=0`,
  `unsupported=none`, and `jbr_picture_frames=0`:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-180307/suite.tsv`.
- [x] Focused graphics-layer renderEffect + blend/filter screenshot parity rows: 4/4 passed, with exact bottom-swatches
  color/geometry parity in every row:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-180743/suite.tsv`.
- [x] Expanded graphics-layer command matrix after nested renderEffect + blend/filter rows: 26/26 rows passed with
  `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-181133/suite.tsv`.
- [x] Broad Magic Jewel command-probe sweep after adding renderEffect + blend/filter rows: 61/61 rows passed. The new
  rows stayed on JBR command replay; only the explicit invalid-gradient fallback row used picture replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-182335/suite.tsv`.
- [x] Broad Magic Jewel screenshot parity sweep after adding renderEffect + blend/filter rows: 23/23 rows passed,
  including rich mixed Swing/Compose, native text, RuntimeEffects, graphics-layer effects/shadows/Offscreen/3D, and exact
  bottom-swatches parity in every row:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-185303/suite.tsv`.
- [x] Focused offset/chained renderEffect + blend/color-matrix rows: 2/2 command rows and 2/2 screenshot parity rows
  passed with no command fallback and exact bottom-swatches parity:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-190948/suite.tsv`,
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-191109/suite.tsv`.
- [x] Expanded graphics-layer matrix with offset/chained renderEffect + blend/color-matrix rows: 28/28 rows passed with
  `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-191346/suite.tsv`.
- [x] Broad Magic Jewel screenshot parity sweep with offset/chained renderEffect + blend/color-matrix rows: 25/25 rows
  passed with exact bottom-swatches parity in every row:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-192628/suite.tsv`.
- [x] Broad Magic Jewel command-probe sweep with offset/chained renderEffect + blend/color-matrix rows: 63/63 rows passed.
  The new rows stayed on JBR command replay; only the explicit invalid-gradient fallback row used picture replay:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-194132/suite.tsv`.
- [x] Focused near-camera 3D + chained renderEffect + blend/color-matrix stress row passed command replay and screenshot
  parity:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-201050/suite.tsv`,
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-201126/suite.tsv`.
- [x] Expanded graphics-layer matrix with the near-camera 3D + chained renderEffect + blend/color-matrix stress row:
  29/29 rows passed with `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-201311/suite.tsv`.
- [x] Broad Magic Jewel screenshot parity sweep with the near-camera 3D + chained renderEffect + blend/color-matrix
  stress row: 26/26 rows passed with exact bottom-swatches parity in every row. The report validator now allows tiny
  Skiko/JBR command-frame marker boundary skew while keeping fallback, unsupported, and picture-frame invariants strict:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-203740/suite.tsv`.
- [x] Broad Magic Jewel command-probe sweep with the near-camera 3D + chained renderEffect + blend/color-matrix stress
  row: 64/64 rows passed. Strict rows stayed on command replay; only `commands-invalid-gradient-fallback` used picture
  replay with the expected `sweepGradientStops` unsupported marker:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-205417/suite.tsv`.
- [x] Focused path-effect screenshot parity row covering dash, corner, stamped, and chained path effects passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-213130/suite.tsv`.
- [x] Default Magic Jewel screenshot parity suite with the path-effect row included: 27/27 rows passed with exact
  bottom-swatches parity in every row:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-213341/suite.tsv`.
- [x] Full Magic Jewel command-probe sweep after RuntimeEffect child-type crash hardening: 46/46 rows passed, adding the
  `commands-runtime-effect-child-type-fallback` row:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-121521/suite.tsv`.
- [x] Full Magic Jewel command-probe sweep: 39/39 rows passed with ABI 90 refreshed artifacts:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-053403/suite.tsv`.
- [x] Broad command sweep includes live animation, mixed Swing popups/menus, text/images, image/composite shaders,
  RuntimeEffects, color filters, blend modes, graphics layers, render effects, saveLayer filters, and the explicit
  invalid-gradient fallback.
- [x] Expected invalid-gradient fallback reports nested unsupported reasons, including `sweepGradientStops`, while staying
  off JBR command replay for that frame family.
- [x] Screenshot assertion harness distinguishes cyan color-filter probes from purple color-matrix probes.

## Compatibility And ABI Hardening

- [x] Exact ABI/build compatibility gate.
- [x] Reflective ABI/BUILD reads to avoid compile-time constant inlining.
- [x] 64-bit command capability accessor and Skiko gate.
- [x] Structured native C ABI version block beyond Java-level command constants.
- [x] BUILD_ID includes pinned Skia revision plus compile-flags hash.
- [x] New Skiko / pre-native-metadata JBR fallback unit coverage.
- [x] Native ABI mismatch Magic Jewel launch/report validation.
- [x] Explicit Magic Jewel shader-boundary fallback validation.
- [x] CI-style parser coverage for ABI/capability/native/public-API handshake fallbacks.
- [x] Skiko reads JBR scoped paint ids and includes them in acquisition diagnostics.
- [x] Parser-level tests for old Skiko/new JBR and new Skiko/old JBR fallback combinations.
- [x] Launch-level forced compatibility matrix runner for happy path plus ABI/native-ABI/capability/public-API fallbacks.
- [x] Launch-level compatibility matrix now validates high-word command capability mismatch. The low 64-bit capability
  word is saturated in this PoC, so the high-word mismatch row is the useful forward-compatibility guard:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-compatibility-matrix/20260501-215807`.
- [x] Launch-level artifact matrix scaffold for named current/old JBR API, JBR native, desktop patch, Skiko, and CMP artifacts.
- [x] Artifact matrix can require old-artifact rows in CI and fail if optional bundles are missing.
- [x] Artifact bundle packaging helper can capture a current artifact set and feed it back into the launch-level artifact matrix.
- [x] Bundle-backed artifact matrix self-check passed with all optional rows required:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-artifact-matrix/20260501-063815/matrix.tsv`.
- [x] Report parser includes nested CMP unsupported-command markers in `cmp_unsupported_reasons`.
- [x] Local artifact rebuild helper recreates the public API shim, patched `java.desktop` classes, and native bridge dylib.
- [x] Post-rebuild live animation command smoke passed:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-062207/suite.tsv`.
- [x] Skiko invalidates temporary cached surface state when scoped destination identity changes.
- [x] Magic Jewel parser coverage for `SKIKO_JBR_INTEROP_SURFACE_CHANGED`.
- [x] Magic Jewel launch-level resize validation for surface identity changes.
- [x] Unit-tested Skiko surface identity tracker.
- [x] Full launch-level old/new packaged artifact matrix with real old bundles supplied for every optional row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-183840/matrix.tsv`.
- [x] Real ABI 99 JBR-side old-artifact matrix rows passed for old public API, old native dylib, and old desktop patch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-175232/matrix.tsv`.
- [x] Real ABI 99 CMP old-artifact matrix row passed after Skiko command-stream ABI preflight:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-183016/matrix.tsv`.
- [x] Real ABI 99 Skiko old-artifact matrix row passed with `OLD_SKIKO_VERSION=0.0.0-abi99-SNAPSHOT`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-183840/matrix.tsv`.
- [x] Stronger JBR context identity separate from per-surface identity for production cache keys.
- [x] Skiko distinguishes same-context surface replacement from context migration.
- [x] Magic Jewel strict resize validation for `contextChanged=false surfaceChanged=true`.
- [x] JBR image cache entries are scoped by destination context id.
- [x] Magic Jewel report counts scoped JBR image-cache-clear markers.
- [x] Combined resize + image-cache churn smoke proving stable context-scoped cache ownership across same-context surface replacement.
- [x] Strict report assertion for scoped JBR image-cache-clear markers.
- [x] Reduce redundant image-cache clear/define churn for stable fallback images.
- [x] Replace whole-cache churn with oldest-entry eviction in CMP recorder.
- [x] Add long-running quiet-machine benchmark pass for stable/dynamic image-cache workloads.
- [x] CMP compile gate after removing the direct patched-Skiko dependency:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui:compileKotlinDesktop :compose:ui:ui-graphics:compileTestKotlinDesktop` passed in `/Users/rock3r/src/cmp-jbr-skia-poc`.
- [x] Magic Jewel runtime gate after the reflective CMP bridge change:
  `CASES=commands-live-animation DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-022941/suite.tsv`.
- [x] Launch-level compatibility matrix now checks single-missing high-word capabilities for image-filter refs, offset/chained image-filter descriptors, shader descriptors, RuntimeEffect color filters, path effects, concat matrices, direct shadows, and shader+color-filter wrappers. All rows passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-compatibility-matrix/20260502-023224/matrix.tsv`.
- [x] JBR and Magic Jewel now support a low-word capability mask test hook, and the compatibility matrix checks single-missing low-word rows for color-matrix/lighting descriptors plus saveLayer/image color-filter ref families. The expanded low/high matrix passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-compatibility-matrix/20260502-024402/matrix.tsv`.
- [x] Point-line replay gate: focused CMP recorder tests passed, and `CASES=commands-point-lines DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-030049/suite.tsv`.
- [x] Point-dot replay gate: focused Skiko/CMP tests passed, local artifacts rebuilt, and `CASES=commands-point-dots DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed at `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-130938/suite.tsv`.
- [x] ABI 100 exact high-word compatibility matrix now includes `COMMAND_CAP64_HIGH_DRAW_POINTS`; the expanded matrix passed at `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260502-132955/matrix.tsv`.
- [x] Point-dot old/new screenshot parity row: `CASES=parity-point-dots SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=4 WARMUP_SECONDS=1 ./scripts/jbr-skia-screenshot-parity-suite.sh` passed at `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-134126/suite.tsv`.
- [x] Off-center-pivot 3D graphics-layer command/parity rows passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-134726/suite.tsv`
  and `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-134810/suite.tsv`.
- [x] Expanded graphics-layer command matrix with the off-center-pivot row included passed 30/30 rows at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-135031/suite.tsv`.
- [x] JBR shader descriptor validator fixtures compile and pass in an isolated local smoke against
  `JBRSkiaService.isValidCommandStreamForTesting(...)`; the full command-stream validator fixture set also passes by
  reflected `assertCommandStreamValidation()` after fixing a stale image-ref color-matrix command-count fixture.
- [x] JBR effect descriptor child-reference validator hardening passes reflected `assertCommandStreamValidation()` after
  rebuilding `/tmp/jbr-skia-run/desktop`.
- [x] Focused Magic Jewel descriptor/effect live rows passed after effect child-reference validator hardening at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-162858/suite.tsv`.
- [x] Full launch-level compatibility matrix passed after descriptor validator hardening at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260502-163211/matrix.tsv`.
- [x] Full launch-level compatibility matrix passed after ABI 101 native-text style metadata at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260502-201302/matrix.tsv`.
- [x] Required current-artifact matrix rows passed with the umbrella CMP output root at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-164541/matrix.tsv`.
- [x] Artifact matrix optional-row self-check passed using the freshly packaged current ABI 100 bundle at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-164746/matrix.tsv`.
- [x] Artifact matrix optional-row self-check passed using a freshly packaged current ABI 101 bundle at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-201003/matrix.tsv`.
- [x] Refreshed ABI 101 artifact matrix self-check passed after the CMP Compose-first text metadata cleanup at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-204103/matrix.tsv`.
- [x] Magic Jewel artifact matrix/bundle scripts now default to the sibling umbrella CMP output root; dry-run and bundle
  creation passed without `CURRENT_CMP_OUT` / `CMP_OUT` overrides.
- [x] Magic Jewel report-validation unit harness passed after artifact harness cleanup.
- [x] Broad Magic Jewel command-probe sweep passed after descriptor validator hardening and artifact harness cleanup:
  75/75 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-165516/suite.tsv`.
  Supported rows stayed on command replay; five intentional fallback rows remained structured, and only the
  invalid-gradient row used picture replay as expected.
- [x] Broad Magic Jewel screenshot parity sweep passed after descriptor validator hardening and artifact harness cleanup:
  35/35 rows passed with exact bottom-swatch parity at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-172551/suite.tsv`.
- [x] Broad Magic Jewel screenshot parity sweep passed after ABI 101 native-text style metadata:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-202158/suite.tsv`.
- [x] Broad Magic Jewel screenshot parity sweep passed after adding the forced-context native-text parity default row:
  36/36 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-232933/suite.tsv`.
  The forced-context native-text row kept command replay active with `contextChanged=true`, command-cache clearing, and
  exact bottom-swatch parity.
- [x] Native-text parity rows now require simple text commands as well as paragraph text commands; focused
  `parity-native-text` and `parity-forced-context-native-text` rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-234852/suite.tsv`.
- [x] Launch-level compatibility matrix now checks exact missing low-word `COMMAND_CAP64_TEXT_FONT_FAMILY` fallback for
  ABI 101 native text metadata; 22/22 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260502-235145/matrix.tsv`.
- [x] Skiko unit coverage now rejects an exact missing low-word `COMMAND_CAP64_TEXT_FONT_FAMILY` capability; full
  `JbrSkiaInteropTest` passed after adding the row.
- [x] Skiko exact high-word command capability unit gate now rejects each missing high-word bit independently; focused
  `JbrSkiaInteropTest` passed after adding the coverage.
- [x] Native metadata now comes from the loaded JBR bridge when a native library is present, so an older dylib without
  current metadata symbols trips `native-abi-mismatch` during discovery instead of producing command frames with
  `rendered=false`.
- [x] Broad Magic Jewel command-probe sweep passed after the native metadata bridge change:
  75/75 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-175743/suite.tsv`.
- [x] Short broad Magic Jewel command-probe sweep passed after ABI 101 native-text style metadata:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-194216/suite.tsv`.
- [x] Short broad Magic Jewel command-probe sweep passed after ABI 102 transformed shader descriptor replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-015241/suite.tsv`.
- [x] Composite-with-opaque-child shader fallback probe passed, and the compact fallback subset kept raw opaque shader,
  composite opaque-child shader, and invalid-gradient fallback rows green at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-025656/suite.tsv`.
- [x] Short broad Magic Jewel command-probe sweep passed after adding the composite opaque-child shader fallback row:
  80/80 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-025935/suite.tsv`.
  Supported rows stayed on command replay; the opaque shader, composite opaque-child shader, and invalid-gradient rows
  remained intentional picture-fallback guards.
- [x] Noise shader fallback probe passed, and the compact shader fallback subset kept raw opaque, composite opaque-child,
  noise shader, and invalid-gradient rows green at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-033152/suite.tsv`.
- [x] Short broad Magic Jewel command-probe sweep passed after adding the noise shader fallback row:
  81/81 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-033433/suite.tsv`.
  Supported rows stayed on command replay; the raw opaque, composite opaque-child, noise shader, and invalid-gradient
  rows remained intentional picture-fallback guards.
- [x] Picture shader fallback probe passed, and the compact shader fallback subset kept raw opaque, composite
  opaque-child, noise shader, picture shader, and invalid-gradient rows green at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-040709/suite.tsv`.
- [x] Short broad Magic Jewel command-probe sweep passed after adding the picture shader fallback row:
  82/82 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-041008/suite.tsv`.
  Supported rows stayed on command replay; the raw opaque, composite opaque-child, noise shader, picture shader, and
  invalid-gradient rows remained intentional picture-fallback guards.
- [x] Raw Skia-backed graphics-layer RenderEffect fallback probe passed, and the compact fallback subset kept raw
  RenderEffect, raw opaque shader, composite opaque-child shader, noise shader, picture shader, and invalid-gradient
  rows green at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-044731/suite.tsv`.
- [x] Short broad Magic Jewel command-probe sweep passed after adding the raw graphics-layer RenderEffect fallback row:
  83/83 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-044947/suite.tsv`.
  Supported graphics-layer blur/offset/chained render effects stayed on command replay, while
  `commands-graphics-layer-raw-image-filter-effect-fallback` reported
  `unsupported=graphicsLayer:childCommands:382,graphicsLayer:renderEffect:382,graphicsLayer:382`,
  `jbr_picture_frames=381`, and `jbr_command_frames=0`.
- [x] Raw Skia RuntimeEffect shader fallback probe passed, and the compact shader subset kept metadata-backed
  RuntimeEffect command replay separate from raw RuntimeEffect, raw opaque shader, composite opaque-child shader, noise
  shader, and picture shader fallback rows at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-052415/suite.tsv`.
- [x] Short broad Magic Jewel command-probe sweep passed after adding the raw RuntimeEffect shader fallback row:
  84/84 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-052633/suite.tsv`.
  `commands-runtime-effect-shader` stayed on command replay with `jbr_command_frames=608`, while
  `commands-raw-runtime-effect-shader-fallback` reported
  `unsupported=shader:386,graphicsLayer:childCommands:386,graphicsLayer:386`, `jbr_picture_frames=386`, and
  `jbr_command_frames=0`.
- [x] Raw Skia RuntimeEffect color-filter fallback probe passed, and the compact color-filter subset kept
  metadata-backed RuntimeEffect color-filter descriptors separate from the raw fallback at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-060130/suite.tsv`.
- [x] Short broad Magic Jewel command-probe sweep passed after adding the raw RuntimeEffect color-filter fallback row:
  85/85 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-060406/suite.tsv`.
  `commands-runtime-effect-color-filter` stayed on command replay with `jbr_command_frames=786`, while
  `commands-raw-runtime-effect-color-filter-fallback` reported
  `unsupported=colorFilter:414,graphicsLayer:childCommands:414,graphicsLayer:414`, `jbr_picture_frames=414`, and
  `jbr_command_frames=0`.
- [x] Raw blend color-filter fallback probe passed, and the compact color-filter subset kept descriptor-backed
  tint/handle/color-matrix/lighting/RuntimeEffect rows on command replay at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-073512/suite.tsv`.
- [x] Short broad Magic Jewel command-probe sweep passed after adding the raw blend color-filter fallback row:
  88/88 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-073756/suite.tsv`.
  `commands-raw-blend-color-filter-fallback` reported
  `unsupported=colorFilter:207,graphicsLayer:childCommands:207,graphicsLayer:207`, `jbr_picture_frames=208`, and
  `jbr_command_frames=0`; adjacent metadata-backed color-filter rows stayed on command replay.
- [x] Raw discrete path-effect fallback probe passed, and the compact path-effect subset kept descriptor-backed
  dash/corner/stamped/chained path effects on command replay at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-081347/suite.tsv`.
- [x] Short broad Magic Jewel command-probe sweep passed after adding the raw discrete path-effect fallback row:
  89/89 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-081524/suite.tsv`.
  `commands-path-effect-fallback` reported `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=772`,
  while `commands-raw-discrete-path-effect-fallback` reported
  `unsupported=graphicsLayer:childCommands:164,pathEffect:164,graphicsLayer:164`, `jbr_picture_frames=164`, and
  `jbr_command_frames=0`.
- [x] Forced-context descriptor subset passed for effect descriptors, shader descriptors, and graphics-layer
  render-effect descriptors at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-063838/suite.tsv`.
  The new `commands-forced-context-graphics-layer-render-effect` row reported `unsupported=none`,
  `jbr_picture_frames=0`, and `jbr_command_frames=331`.
- [x] Resize/context descriptor subset passed for effect descriptors, shader descriptors, and graphics-layer
  render-effect descriptors at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-064338/suite.tsv`.
  The new `commands-resize-graphics-layer-render-effect` row reported `unsupported=none`, `jbr_picture_frames=0`,
  and `jbr_command_frames=335`.
- [x] Short broad Magic Jewel command-probe sweep passed after adding the graphics-layer render-effect resize row:
  87/87 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-064722/suite.tsv`.
  `commands-resize-graphics-layer-render-effect` reported `unsupported=none`, `jbr_picture_frames=0`, and
  `jbr_command_frames=729`; `commands-forced-context-graphics-layer-render-effect` also remained on command replay.
- [x] Launch-level compatibility matrix passed after ABI 102 transformed shader descriptor replay, including the exact
  `shader-transform-capability-missing` high-word row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260503-022142/matrix.tsv`.
- [x] Launch-level compatibility matrix passed after the latest raw shader/effect fallback and graphics-layer
  render-effect resize/context probes:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260503-071952/matrix.tsv`.
  All 23 rows passed; every negative row reported one structured fallback and zero JBR command frames, including
  `draw-points-capability-missing` and `shader-transform-capability-missing`.
- [x] Launch-level compatibility matrix passed after raw blend color-filter and raw discrete path-effect fallback probes:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260503-084917/matrix.tsv`.
  All 23 rows passed; every negative row reported one structured fallback and zero JBR command frames, including
  `path-effect-capability-missing`, `draw-points-capability-missing`, and `shader-transform-capability-missing`.
- [x] Launch-level compatibility matrix passed after JBR generic-family native text resolution:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260503-102247/matrix.tsv`.
  All 23 rows passed; the happy row stayed on command replay and every mismatch row reported one structured fallback
  with zero command frames.
- [x] CMP now keeps file-backed/custom loaded font families on text-image command replay when native text is enabled,
  while allowing default, generic, and single-identity system font families onto JBR native text commands.
- [x] Magic Jewel custom-font/native-text command rows now assert image refs, and generic-family native-text rows now
  assert mixed frames with image refs plus 3 simple native text commands and 1 paragraph native text command.
- [x] Focused custom-font image and generic-family native-text command subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-113359/suite.tsv`.
  All four rows reported `fallback_new_count=0`, `unsupported=none`, and `jbr_picture_frames=0`.
- [x] Focused custom-font image and generic-family native-text screenshot parity subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-113538/suite.tsv`.
  Both rows stayed on JBR command replay with zero fallback and zero picture frames.
- [x] Short broad Magic Jewel command-probe sweep passed after the file-backed font native-text guard:
  92/92 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-113919/suite.tsv`.
  The renamed custom-font text rows stayed image-backed on command replay with zero fallback and zero picture frames;
  generic-family native-text rows stayed mixed image/native command replay; intentional raw shader/effect fallback rows
  retained structured unsupported reasons and picture replay.
- [x] ABI 103: added a JBR-owned font-data descriptor command for simple native text using Skiko/CMP loaded
  byte-array fonts, without exposing Skiko `SkTypeface*` pointers across the boundary.
- [x] ABI 104: solid color shaders serialize as `COMMAND_SHADER_DESCRIPTOR_COLOR`, allowing descriptor-backed
  `ColorShader(Color)` paints to replay in JBR-owned Skia while raw `SkShader.makeColor(...).asComposeShader()`
  remains an intentional opaque-shader fallback.
- [x] ABI 105: fractal-noise and turbulence shaders serialize as `COMMAND_SHADER_DESCRIPTOR_PERLIN_NOISE`, allowing
  JBR-owned `SkShaders::MakeFractalNoise` / `SkShaders::MakeTurbulence` replay while still rejecting unknown raw
  `SkShader*` objects.
- [x] CMP now emits `COMMAND_DEFINE_FONT_DATA` before simple native text records whose family is a single
  `LoadedFont`, and keeps paragraph text plus desktop resource/file-backed fonts image-backed until broader
  font/layout parity is proven.
- [x] Focused ABI 103 validation passed:
  Skiko `JbrSkiaInteropTest`, CMP graphics `writesFontDataRecord`/metadata tests, CMP text loaded-font and
  file-backed guard tests, local JBR API/desktop/native artifact rebuild, and Magic Jewel focused text command rows.
- [x] Focused Magic Jewel text command subset passed after publishing ABI 103 Skiko locally:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-123653/suite.tsv`.
  `commands-native-custom-font-text-image` reported `fallback_new_count=0`, `unsupported=none`,
  `jbr_picture_frames=0`, and `jbr_command_frames=724`; `commands-native-generic-font-text` reported
  `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=497`.
- [x] Launch-level compatibility matrix passed after ABI 103 font-data descriptor replay, including the exact
  `font-data-capability-missing` high-word row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260503-124228/matrix.tsv`.
  All 24 rows passed; every negative row reported one structured fallback and zero JBR command frames.
- [x] Focused ABI 104 color shader validation passed:
  Skiko `JbrSkiaInteropTest`, CMP color-shader descriptor and raw-color fallback recorder tests, local JBR
  API/desktop/native artifact rebuild, and Magic Jewel `commands-color-shader`.
- [x] Focused Magic Jewel shader subset passed after ABI 104:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-141053/suite.tsv`.
  `commands-color-shader`, `commands-composite-shader`, and `commands-transformed-shader` stayed on JBR command
  replay with `unsupported=none`; `commands-opaque-shader-fallback` stayed on intentional picture fallback with
  structured `shader` unsupported markers.
- [x] Launch-level compatibility matrix passed after ABI 104, including the exact
  `shader-color-capability-missing` high-word row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260503-141240/matrix.tsv`.
  All rows passed; the happy row reported positive JBR command frames and every negative row reported one structured
  fallback with zero JBR command frames.
- [x] Short broad Magic Jewel command-probe sweep passed after ABI 104:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-142844/suite.tsv`.
  The default suite passed end to end; supported descriptor/graphics-layer rows stayed on command replay, while raw
  shader/effect fallback rows retained structured unsupported reasons and picture replay.
- [x] Focused ABI 105 Perlin/noise descriptor validation passed:
  Skiko `JbrSkiaInteropTest`, CMP `writesPerlinNoiseShaderDescriptorRectInStrictMode`/color descriptor recorder tests,
  local JBR API/desktop/native artifact rebuild, Skiko local snapshot publish, and Magic Jewel
  `commands-noise-shader` / `commands-turbulence-shader`.
- [x] Focused Magic Jewel Perlin/noise descriptor rows passed after ABI 105:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-205930/suite.tsv`.
  Both rows reported `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and positive
  `jbr_command_frames`.
- [x] Launch-level compatibility matrix passed after ABI 105, including the exact
  `shader-perlin-noise-capability-missing` high-word row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260503-210129/matrix.tsv`.
  The happy row stayed on command replay; every negative row reported one structured fallback and zero JBR command
  frames.
- [x] Focused Perlin/noise screenshot parity passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-211813/suite.tsv`.
  `parity-noise-shader` and `parity-turbulence-shader` stayed on command replay with zero fallback and zero picture
  frames, and their focused shader probe regions stayed under the initial parity gates.
- [x] Focused Perlin/noise descriptor lifecycle rows passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-212142/suite.tsv`.
  Resize and forced-context probes required command-cache clearing, second shader-handle definitions, cache-hit recovery,
  zero fallback, and zero picture frames.
- [x] Focused turbulence descriptor lifecycle rows passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-220107/suite.tsv`.
  The resize and forced-context turbulence probes exercise the second ABI 105 Perlin descriptor kind with the same
  cache-clear/redefine/cache-hit expectations.
- [x] Raw Perlin/noise shader fallback sentinels restored:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-220540/suite.tsv`.
  Raw Skia fractal-noise and turbulence shaders stayed on structured `shader` unsupported markers with picture replay,
  while descriptor-backed helpers remain on JBR command replay.
- [x] Short broad Magic Jewel command-probe sweep passed after restoring raw Perlin/noise fallback sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-220815/suite.tsv`.
  The expanded default suite covered descriptor-backed Perlin replay plus raw Perlin fallback in one green run.
- [x] Short broad Magic Jewel command-probe sweep passed after adding the raw linear-gradient shader fallback sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-231216/suite.tsv`.
  The new raw-gradient row stayed on structured `shader` fallback with picture replay and zero JBR command frames.
- [x] Focused raw gradient-family fallback subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-235846/suite.tsv`.
  Raw linear, radial, and sweep Skia gradients all stayed picture-backed with structured `shader` unsupported markers.
- [x] Compact shader fallback cluster passed after adding the raw gradient-family rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-000159/suite.tsv`.
  Descriptor-backed color/noise/turbulence stayed on JBR command replay, while raw gradient/noise/opaque/picture shader
  rows stayed picture-backed with zero JBR command frames.
- [x] Focused image-shader ownership subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-001237/suite.tsv`.
  Descriptor-backed Compose `ImageShader` replay stayed on JBR commands; raw Skia image shader stayed on structured
  `shader` fallback with picture replay.
- [x] Current artifact matrix passed after ABI 105 Perlin/noise work:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260503-224852/matrix.tsv`.
  The current artifact row stayed on command replay, and the missing public API row reported the expected structured
  fallback.
- [x] ABI 105 artifact bundle packaged for future old/new artifact matrix rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-bundles/20260503-225027/manifest.properties`.
- [x] Short broad Magic Jewel command-probe sweep passed after adding Perlin/noise parity and lifecycle rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-212324/suite.tsv`.
  The expanded default suite passed end to end; supported rows stayed on command replay and raw shader/effect sentinel
  rows stayed on intentional picture fallback.
- [x] Focused solid color shader screenshot parity passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-150944/suite.tsv`.
  The `parity-color-shader` row reported `fallback_new_count=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=291`, and `compose_shader_color_bad_pixel_ratio=0.00000`.
- [x] Added same-context resize and forced-context redefine probes for ABI 104 solid color shader descriptors.
  Focused subset passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-151610/suite.tsv`;
  both rows reported zero fallback, zero picture frames, fresh shader handle defines, cache hits, and command-cache
  clearing after the surface identity change.
- [x] Short broad Magic Jewel command-probe sweep passed after adding the ABI 104 color-shader redefine rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-151907/suite.tsv`.
  The default suite passed end to end, including both new color-shader resize/context rows.
- [x] Add focused screenshot parity coverage for Magic Jewel toolbar/button rendering, including primary button text
  color and text centering, so command replay is compared against the old SwingGraphics renderer for UI chrome
  fidelity regressions.
- [x] Focused Magic Jewel button chrome screenshot parity passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-130251/suite.tsv`.
  The `parity-button-chrome` row reported `fallback_new_count=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=432`, and `header_buttons_bad_pixel_ratio=0.00381` under the strict 0.02 button gate.
- [x] Focused transformed shader screenshot parity passed after ABI 102 descriptor replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-023351/suite.tsv`.
- [x] Broad Magic Jewel screenshot parity sweep passed after adding the transformed shader descriptor row:
  38/38 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-023508/suite.tsv`.
  The transformed shader row stayed visually aligned with `avg_delta=2.110`, `bad_pixel_ratio=0.05007`,
  `compose_bad_pixel_ratio=0.07463`, and exact bottom-swatch parity.
- [x] Focused native-text command probe passed with the stricter simple-text and paragraph-text command gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-220852/suite.tsv`.
- [x] Forced-context native-text command probe passed with command-cache invalidation and continued JBR command replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-221924/suite.tsv`.
- [x] Short broad Magic Jewel command-probe sweep passed after adding the forced-context native-text default row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-222056/suite.tsv`.
- [x] Short broad Magic Jewel command-probe sweep passed after adding the forced-context dynamic image-cache default row:
  77/77 rows passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-225719/suite.tsv`.
  The new image-cache row stayed on command replay with `contextChanged=true`, command-cache clearing, JBR image
  evictions, and zero JBR whole-cache clears.
- [x] Skiko command-stream ABI preflight now turns old-CMP ABI 99 command buffers into structured `abi-mismatch`
  fallback before JBR native replay sees them.
- [x] JBR now attempts to load a bundled `libjbrskiainterop` with `System.loadLibrary("jbrskiainterop")` when no
  explicit native library property is set, while Magic Jewel still defaults to the local out-of-build dylib for patched
  harness runs. Explicit-dylib command replay passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-191032/suite.tsv`.

## Productionization Later

- [ ] Replace local patched-class/dylib launch wiring with real JBR build integration.
  - [x] Add the runtime load path for a bundled native bridge and a Magic Jewel opt-out for the explicit local dylib.
  - [x] Add a guarded `libjbrskiainterop` Java.desktop native-library make target for macOS external Skia roots.
  - [x] Validate the external `--with-skia-interop=<Skia release root>` shape during configure before native linking.
  - [x] Add a Magic Jewel command row that omits `sun.java2d.skia.interop.library` and validates the boot-loaded
    `System.loadLibrary("jbrskiainterop")` path via `sun.boot.library.path` in the local harness.
  - [x] Short broad Magic Jewel command sweep passed with the no-explicit-library row included:
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-131421/suite.tsv`.
  - [ ] Wire `libjbrskiainterop` into the JBR image so no explicit `sun.java2d.skia.interop.library` property is needed.
- [x] Decide final Skiko artifact shape for Skia-less JBR interop.
- [ ] Font/typeface ownership through the JBR Skia runtime.
  - [x] Keep Skiko-owned loaded/file-backed fonts on image replay until JBR owns font descriptors or data handles.
  - [x] Add ABI 103 JBR-owned font-data descriptors for simple native text backed by CMP loaded font bytes.
  - [x] Add explicit Magic Jewel command and screenshot coverage for a loaded byte-array font, proving
    `COMMAND_DEFINE_FONT_DATA` reaches JBR-owned native replay with parseable `FONT_DATA_DEFINE` markers.
  - [x] Add explicit Magic Jewel command and screenshot coverage for fonts loaded from packaged JAR resources.
  - [x] Add explicit Magic Jewel command and screenshot coverage for a concrete installed system font family, separate
    from generic `sans-serif`/`serif`/`monospace` family coverage.
  - [x] Focused packaged-resource and named-system-font command probes passed:
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-160555/suite.tsv`.
    The resource-font row stayed image-backed on command replay with zero fallback; the Menlo system-font row emitted
    one native text command per frame with zero fallback.
  - [x] Focused packaged-resource and named-system-font screenshot parity passed:
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-160804/suite.tsv`.
  - [x] Short broad Magic Jewel command-probe sweep passed with the resource/system font rows included:
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-161138/suite.tsv`.
  - [x] Short broad Magic Jewel command-probe sweep passed with the loaded-font-data row included:
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-171850/suite.tsv`.
  - [x] CMP now deduplicates `COMMAND_DEFINE_FONT_DATA` by handle until the recorder's interop caches are cleared,
    avoiding repeated byte-array font definitions on every frame while preserving context/surface invalidation behavior.
    Focused loaded-font-data command replay passed at
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-180117/suite.tsv`
    with `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=300`, and
    `jbr_font_data_define_frames=1`.
  - [x] Magic Jewel report/matrix/probe harnesses now launch automation windows as non-focusable macOS agent windows by
    default, with `MAGIC_JEWEL_BACKGROUND_WINDOW=false` available for interactive debugging. Focused popup-window command
    replay passed at
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-181305/suite.tsv`.
  - [x] Short broad Magic Jewel command-probe sweep passed with background windows enabled:
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-181507/suite.tsv`.
  - [x] Launch-level Magic Jewel compatibility matrix passed with background windows enabled:
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260503-185038/matrix.tsv`.
  - [x] Added forced-context loaded-font-data command and screenshot rows proving CMP/JBR re-define byte-array font handles
    after destination context migration. Focused command replay passed at
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-190205/suite.tsv`
    with `jbr_font_data_define_frames=2`; focused screenshot parity passed at
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-190258/suite.tsv`.
  - [x] Added same-context resize loaded-font-data command and screenshot rows proving CMP/JBR also re-define byte-array
    font handles after destination surface replacement. Focused command replay passed at
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-190729/suite.tsv`
    with `jbr_font_data_define_frames=2`; focused screenshot parity passed at
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260503-190951/suite.tsv`.
  - [x] Rebuilt local `/tmp` harness artifacts and reran the compact loaded-font-data normal/resize/forced-context
    command subset:
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-191346/suite.tsv`.
  - [x] CMP recorder tests now cover font-data handle LRU eviction after the 1,024-entry cache threshold, proving an
    evicted byte-array font handle is redefined while an in-cache duplicate remains suppressed.
  - [x] Compact Magic Jewel cache-lifecycle subset passed across loaded font-data normal/resize/forced-context rows and
    descriptor eviction:
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-191922/suite.tsv`.
  - [x] Magic Jewel report validation now supports `EXPECT_MAX_JBR_FONT_DATA_DEFINES`, and the steady-state loaded
    font-data row requires exactly one JBR font-data define. Focused command replay passed at
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-192439/suite.tsv`.
- [ ] JBR-owned generic shader factory and handles for non-serialized shader families.
  - [x] Add a first solid-color shader factory descriptor (`COMMAND_SHADER_DESCRIPTOR_COLOR`) with strict high-word
    capability gating and live fallback contrast against raw Skia color shaders.
  - [x] Add explicit Magic Jewel turbulence-shader fallback coverage so raw Skia turbulence shaders remain picture-backed
    until JBR owns a descriptor family for them. Focused fallback replay passed at
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-192910/suite.tsv`;
    the short broad command sweep with turbulence included passed at
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-193253/suite.tsv`.
  - [x] Harden stable shader descriptor rows with exact max JBR shader-handle define gates for solid color,
    image+color-filter, and transformed shader descriptors. Focused command replay passed at
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-201312/suite.tsv`.
  - [x] Harden stable shader+color-filter and graphics-layer render-effect rows with exact max JBR effect-handle define
    gates, while leaving animated RuntimeEffect rows uncapped. Compact command replay passed at
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260503-202638/suite.tsv`.
- [x] Extend path-effect descriptors beyond corner to stamped path effects.
- [x] Extend path-effect descriptors to chained path effects.
- [ ] Screen migration/context invalidation hardening beyond the current synthetic descriptor context-change probes.
- [x] Real menu stress tests beyond popup-window coverage.
- [ ] JCEF/shared-texture exploration after Compose is solid.
- [ ] Windows/Linux backend adapter investigation after macOS MVP.
