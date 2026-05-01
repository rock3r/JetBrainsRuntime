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
- [x] CMP's full `JbrSkiaCommandRecorderTest` desktop suite is green for ABI 90, so command-stream golden expectations are a usable regression gate again.

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
- [x] RuntimeEffect builder failures emit parseable JBR markers, and Magic Jewel has a bad-child-name probe/report assertion.
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
  - [ ] Extend Skia runtime effects via descriptor payloads: remaining shader-family fallback markers and parity coverage for child color-filter descriptors.
  - [ ] Add shader/effect lifecycle commands for create, use, context-scoped cache hit, compile failure, eviction, and context migration invalidation; never pass raw Skiko `SkShader*`, `SkImageFilter*`, or `SkRuntimeEffect*` pointers across the ABI.
  - [x] Add RuntimeEffect conformance probes in Magic Jewel: one pure color shader, one child-shader composition, one uniform animation, one builder/compile-failure fallback, and one old-runtime capability fallback.
  - [x] Add Magic Jewel probes that force invalid descriptor-handle fallback without relying on raw Skiko `SkShader*` or `SkRuntimeEffect*` pointers.
  - [ ] Add Magic Jewel probes that force handle creation, reuse, context migration, and eviction without relying on raw Skiko `SkShader*` or `SkRuntimeEffect*` pointers.
  - [ ] Add ABI/version tests for shader/effect handle creation, use-after-free rejection, context migration invalidation, and old/new fallback markers.
  - [ ] Long term: revisit true generic shader support only after Skiko's fast path no longer creates Skia C++ objects in a separate bundled runtime.
- [x] Strict recorder fallback for invalid gradient stops and excessive gradient color counts.
- [x] Strict recorder fallback for invalid rounded-rectangle radii with gradient paints.
- [x] Strict recorder fallback for transformed gradient shaders.
- [x] Strict recorder fallback for composite/opaque shader wrappers.
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
- [x] Rebuild ABI 43 local artifacts and pass a native-text Magic Jewel command smoke with paragraph commands and screenshot assertion.
- [x] Add narrow image-shader rectangle support without sharing raw `SkShader*` pointers.
- [x] Add narrow linear-gradient stroked-rectangle support without sharing raw `SkShader*` pointers.
- [x] Add narrow linear-gradient stroked rounded-rectangle support without sharing raw `SkShader*` pointers.
- [x] Add narrow radial-gradient stroked-rectangle support without sharing raw `SkShader*` pointers.
- [x] Add narrow radial-gradient stroked rounded-rectangle support without sharing raw `SkShader*` pointers.
- [x] Add narrow sweep-gradient stroked-rectangle support without sharing raw `SkShader*` pointers.
- [x] Add narrow sweep-gradient stroked rounded-rectangle support without sharing raw `SkShader*` pointers.
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
- [ ] Continue blend-mode command coverage mode-by-mode only after each mode has exact Skia-vs-Java2D semantics documented.
- [ ] Command-recorded graphics layers:
  - [x] Mark Skiko `GraphicsLayer`/`RenderNode` draws as an explicit strict fallback while command replay cannot encode layer contents/effects.
  - [x] Replace the simple 2D graphics-layer fallback with nested command recording for layer-local content plus alpha, translation, scale, rotationZ, and balanced save/saveLayer/restore replay.
  - [x] Support rectangular graphics-layer clips by replaying the layer outline through existing `COMMAND_CLIP_RECT` inside the saved layer scope.
  - [x] Support rounded graphics-layer clips by serializing rounded/conic outline paths as quadratic path commands and replaying them through `COMMAND_CLIP_PATH`.
  - [x] Support generic path outline clips by replaying `Outline.Generic` through existing `COMMAND_CLIP_PATH`.
  - [x] Support directly mapped non-SrcOver graphics-layer blend modes through a bounded save-layer blend command.
  - [x] Support tint/SrcIn graphics-layer color filters by reusing the existing save-layer color-filter command.
  - [x] Support graphics layers that combine a directly mapped blend mode with tint/SrcIn color filtering through a bounded save-layer blend/color-filter command.
  - [x] Support color-matrix and lighting graphics-layer color filters by referencing typed descriptor handles from saveLayer paints.
  - [ ] Extend graphics-layer command replay beyond the current 2D subset: shadows, 3D rotation/camera, offscreen strategy semantics, and broader image-filter surfaces.
  - [x] Add render-effect descriptors for graphics-layer `RenderEffect` once the JBR-owned effect-handle ABI can construct the needed Skia image filters.
- [x] Add narrow tint color-filter solid fill-rectangle support through a versioned command instead of picture fallback.
- [x] Add narrow tint color-filter `saveLayer` support through a versioned command instead of picture fallback.
- [x] Add narrow tint color-filter cached-image support through a versioned command instead of picture fallback.
- [x] Expand color-matrix/lighting color-filter command coverage to solid rectangles, saveLayer/graphics-layer paints, and cached images through typed descriptors.
- [ ] Expand color-filter command coverage to image filters, runtime effects, and generic shaders.
- [x] Add narrow dash path-effect stroked-line support through a versioned command instead of picture fallback.
- [x] Extend dash path-effect command replay to stroked rectangles with ABI 93 validation and native/Java2D replay.
- [x] Extend dash path-effect command replay to stroked rounded rectangles with ABI 94 validation and native/Java2D replay.
- [ ] Expand path-effect command coverage beyond dash stroked lines only through serialized descriptors or JBR-owned effect handles.
- [x] Add screenshot-level text-presence assertions using stable pixel regions for top/bottom Magic Jewel labels.
- [x] Add screenshot-level text placement assertions using stable dark-pixel bounding boxes.
- [x] Add paragraph-row screenshot assertions for native-text layout probes.
- [x] Investigate observed text alignment drift in Magic Jewel/Jewel labels on the command path.
- [x] Add screenshot-region assertions for the Plus/Multiply blend-mode probe.

## Validation Harness

- [x] Focused CMP recorder tests for command encodings.
- [x] Full CMP command-recorder regression gate: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest` passed for ABI 90.
- [x] Live animation regression gate: `CASES=commands-live-animation DURATION_SECONDS=6 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed for ABI 90 at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-045238/commands-live-animation/report.md`.
- [x] RuntimeEffect conformance gate: `CASES="commands-runtime-effect-pure-color commands-runtime-effect-uniform-only commands-runtime-effect-child-only commands-runtime-effect-shader commands-runtime-effect-build-fallback" DURATION_SECONDS=6 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed for ABI 90 at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-050347/suite.tsv`.
- [x] RuntimeEffect color-filter descriptor gate: `CASES="commands-runtime-effect-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed for ABI 91 at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-083405/suite.tsv`.
- [x] ABI 91 broader descriptor regression gate: RuntimeEffect shader/color-filter, image color-matrix, typed color filters, and graphics-layer color-filter rows passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-083644/suite.tsv`.
- [x] RuntimeEffect color-filter child descriptor gate: `CASES="commands-runtime-effect-color-filter-child" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh` passed for ABI 92 at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-090456/suite.tsv`.
- [x] ABI 92 broader descriptor regression gate: RuntimeEffect shader/color-filter/child-color-filter, image color-matrix, typed color filters, and graphics-layer color-filter rows passed at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-090711/suite.tsv`.
- [x] RuntimeEffect descriptor handle-use gate: shader, color-filter, and child color-filter RuntimeEffect rows passed with JBR handle-use assertions at `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-092522/suite.tsv`.
- [x] Golden/diff screenshot harness for the full mixed Swing/Jewel/Compose Magic Jewel scene.
- [x] Capture the app window only in old/new renderer modes, with deterministic sizing, theme, font inputs, animation phase, and seeded content.
- [x] Add configurable per-region screenshot parity gates for header controls, Compose canvas, Swing island, and right probe strip.
- [x] Add metric-only Compose subregions for left backdrop, center animation, and bottom labels to prepare tighter visual masks.
- [x] Tighten Compose/Jewel-owned region thresholds after splitting text/font raster drift from geometry/pixel ownership drift.
- [x] Add screenshot parity jobs that run both old and new renderers for the same rich scene, emit old/new images, and fail on unexpected Compose/Jewel geometry/color drift. Expected Swing text rendering differences must be isolated to Swing-owned regions and documented in the report.
- [x] Add a rich-content old/new parity suite with Jewel controls/text, Compose primitives/text/images/effects, Swing islands, popups/menus, and always-on animation sampled at deterministic phases.
- [x] Persist diff images and per-region parity metrics in the Magic Jewel report and `summary.properties`.
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
- [x] Graphics-layer effects screenshot parity row passed with focused probe scoping:
  `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-055842/suite.tsv`.
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
- [ ] Full launch-level old/new packaged artifact matrix with real old bundles supplied for every optional row.
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

## Productionization Later

- [ ] Replace local patched-class/dylib launch wiring with real JBR build integration.
- [ ] Decide final Skiko artifact shape for Skia-less JBR interop.
- [ ] Font/typeface ownership through the JBR Skia runtime.
- [ ] JBR-owned generic shader factory and handles for non-serialized shader families.
- [ ] Extend path-effect descriptors beyond corner to stamped and chained path effects.
- [ ] Screen migration/context invalidation hardening.
- [x] Real menu stress tests beyond popup-window coverage.
- [ ] JCEF/shared-texture exploration after Compose is solid.
- [ ] Windows/Linux backend adapter investigation after macOS MVP.
