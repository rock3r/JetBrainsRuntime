# Skia Compose Zero-Copy PoC Plan

This is the compact current-state plan. The full historical checkpoint log was archived to
[`docs/history/SKIA_COMPOSE_ZERO_COPY_POC_PLAN.full.md`](docs/history/SKIA_COMPOSE_ZERO_COPY_POC_PLAN.full.md).

## Goal

Render Compose from `ComposePanel(RenderSettings.SwingGraphics)` through a JBR-owned Skia surface during Swing painting,
avoiding the Skiko GPU to CPU bitmap to Swing re-upload path. The command stream must replay directly into the Java2D
Metal destination when ABI/capability checks match, and must fall back cleanly on mismatch.

## Current Snapshot

- ABI 106 artifacts are current across JBR private API, JBR API mirror, Skiko, CMP, and Magic Jewel.
- Command replay supports the current broad scene set: primitives, gradients, images, text/font-data, point dots,
  shader/effect descriptors, RuntimeEffect shaders/color filters, image filters, path effects, blend modes, shadows,
  and graphics-layer variants.
- Stable descriptor rows assert JBR handle definitions, uses, cache hits, and context invalidation behavior.
- The default path-effect command row now also gates the stable descriptor-definition contract: five JBR-owned
  path-effect descriptor definitions and no picture fallback.
- Raw Skiko-owned shader/effect/path-effect families remain explicit fallback sentinels.
- Harness windows are non-focus-stealing by default via `MAGIC_JEWEL_BACKGROUND_WINDOW=true`.
- `MagicLabel` is a test harness switch: when Compose text is disabled, it renders fixed white boxes to isolate geometry
  parity from text rasterization drift. It is not a replacement for Jewel `Text`.
- The working docs are intentionally split: this plan and `ROADMAP.md` stay compact, validation details live in
  `docs/current/VALIDATION_LOG.md`, and verbose historical checkpoints live in `docs/history/`.
- Latest JBR parser hardening now rejects wrong-type descriptor handles at command-stream validation time: image/path
  effects cannot satisfy color-filter uses, color filters cannot satisfy image-filter refs, and shader color-filter
  descriptors require a real color-filter descriptor handle. Local artifact rebuild and parser-only `JBRSkiaApiTest`
  validation passed against the rebuilt `/tmp/jbr-skia-run/desktop` patch.
- Latest live command sentinel now corrupts a color-filter handle use so it references an image-filter descriptor; JBR
  rejects the stream with structured `command-stream-invalid` fallback before replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-144306/suite.tsv`.
- Latest live command sentinel now covers the opposite wrong-type direction: an image-filter handle use is rewritten to
  reference a color-filter descriptor and JBR rejects it before replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-144841/suite.tsv`.
- Latest live wrong-type descriptor subset also covers shader color-filter child handles, with target-specific Skiko
  corruption markers for `fillRectColorFilter`, `shaderColorFilter`, and `saveLayerImageFilter`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-161400/suite.tsv`.
- Magic Jewel report validation now records and enforces `EXPECT_COMMAND_FALLBACK_MARKER`, so the wrong-type handle rows
  require the exact target-specific corruption marker rather than any generic invalid-stream fallback. The focused
  marker-gated subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-173315/suite.tsv`.
- Latest wrong-type descriptor hardening adds a path-effect-to-color-filter live sentinel. Skiko can now rewrite one
  fill color-filter handle use to a path-effect descriptor, Magic Jewel requires
  `target=fillRectColorFilterPathEffect`, and JBR parser tests cover the same handle-family mismatch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-231235/suite.tsv`.
- Previous full command-probe sweep covered 148 rows plus the header and passed after adding all three wrong-type handle
  sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-161539/suite.tsv`.
- Latest full command-probe sweep covered 149 rows plus the header and passed after adding the path-effect wrong-type
  handle sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-231315/suite.tsv`.
- Latest focused path-effect descriptor-gate validation passed after tightening `commands-path-effect` to require
  exactly five JBR effect-handle descriptor definitions:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-092433/suite.tsv`.
- Latest focused descriptor lifecycle subset passed after adding exact max definition gates to resize and forced-context
  rows across effect, shader, RuntimeEffect stable color-filter, composite-noise shader, and graphics-layer descriptor
  families:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-092758/suite.tsv`.
- Latest full command-probe sweep passed after the descriptor-definition gate tightening. It covered 149 rows plus the
  header: all rows passed, with 110 command replay rows, 26 intentional JBR picture fallback rows, and 13 expected
  explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-093809/suite.tsv`.
- Latest shader-family hardening adds a live wrong-type shader handle sentinel. Skiko can now rewrite one fill shader
  handle use to a color-filter descriptor, Magic Jewel requires
  `SKIKO_JBR_INTEROP_SHADER_HANDLE_TYPE_CORRUPTED target=fillRectShader`, and JBR parser tests cover the same
  cross-cache mismatch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-110215/suite.tsv`.
- Latest transformed shader child hardening extends the same wrong-type shader handle hook to transformed shader
  descriptor children. Skiko can rewrite the child shader handle to a color-filter descriptor, Magic Jewel requires
  `SKIKO_JBR_INTEROP_SHADER_HANDLE_TYPE_CORRUPTED target=transformedShaderChild`, and JBR parser tests cover the
  transformed-child cross-cache mismatch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-122433/suite.tsv`.
- Latest composite shader child hardening extends the same wrong-type shader handle hook to composite shader dst-child
  descriptors. Skiko can rewrite the child shader handle to a color-filter descriptor, Magic Jewel requires
  `SKIKO_JBR_INTEROP_SHADER_HANDLE_TYPE_CORRUPTED target=compositeShaderDstChild`, and JBR parser tests cover the
  composite-child cross-cache mismatch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-145418/suite.tsv`.
- Latest RuntimeEffect shader child hardening extends the same wrong-type shader handle hook to RuntimeEffect shader
  descriptor children. Skiko can rewrite the child shader handle to a color-filter descriptor, Magic Jewel requires
  `SKIKO_JBR_INTEROP_SHADER_HANDLE_TYPE_CORRUPTED target=runtimeEffectShaderChild`, and JBR parser tests cover the
  RuntimeEffect child cross-cache mismatch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-161650/suite.tsv`.
- Latest full command-probe sweep covered 153 rows plus the header and passed after adding that RuntimeEffect shader
  child wrong-type row: all rows passed, with 110 command replay rows, 26 intentional JBR picture fallback rows, and 17
  expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-161739/suite.tsv`.
- Latest RuntimeEffect color-filter child hardening extends the wrong-type color-filter handle hook to child descriptor
  handles. Skiko can rewrite the first RuntimeEffect color-filter child handle to an image-filter descriptor, Magic
  Jewel requires
  `SKIKO_JBR_INTEROP_COLOR_FILTER_HANDLE_TYPE_CORRUPTED target=runtimeEffectColorFilterChild`, and existing JBR parser
  tests cover the RuntimeEffect color-filter child cross-cache mismatch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-174429/suite.tsv`.
- Latest full command-probe sweep covered 154 rows plus the header and passed after adding that RuntimeEffect
  color-filter child wrong-type row: all rows passed, with 110 command replay rows, 26 intentional JBR picture fallback
  rows, and 18 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-174517/suite.tsv`.
- Latest image-filter child hardening extends the wrong-type image-filter handle hook to chained render-effect child
  descriptors. Skiko can rewrite the offset image-filter child handle to a color-filter descriptor, Magic Jewel
  requires `SKIKO_JBR_INTEROP_IMAGE_FILTER_HANDLE_TYPE_CORRUPTED target=offsetImageFilterChild`, and existing JBR
  parser tests cover the offset image-filter child cross-cache mismatch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-192441/suite.tsv`.
- Latest full command-probe sweep covered 155 rows plus the header and passed after adding that offset image-filter
  child wrong-type row: all rows passed, with 110 command replay rows, 26 intentional JBR picture fallback rows, and 19
  expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-192524/suite.tsv`.
- Latest path-effect child hardening adds a dedicated wrong-type path-effect handle hook. Skiko can rewrite the first
  chained path-effect child handle to a color-filter descriptor, Magic Jewel requires
  `SKIKO_JBR_INTEROP_PATH_EFFECT_HANDLE_TYPE_CORRUPTED target=chainPathEffectChild`, and existing JBR parser tests
  cover the chained path-effect child cross-cache mismatch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-205629/suite.tsv`.
- Latest full command-probe sweep covered 167 rows plus the header and passed after promoting focused invalid-handle
  sentinels into the default suite: all rows passed, with 110 command replay rows, 26 intentional JBR picture fallback rows, and 31
  expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-171413/suite.tsv`.
- Latest focused RuntimeEffect source-cache eviction subset uses the test-only
  `JBR_SKIA_RUNTIME_EFFECT_CACHE_LIMIT_FOR_TEST=2` override and passed with typed shader and color-filter evict gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-122623/suite.tsv`.
- Latest full screenshot parity suite covered 106 rows plus the header and passed after adding RuntimeEffect
  source-cache eviction parity:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-214152/suite.tsv`.
- Latest focused compatibility matrix covers the exact dash path-effect high-word capability removals:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260507-115729/matrix.tsv`.
- Latest full compatibility matrix includes those rows and passed across 30 rows plus the header:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260507-120041/matrix.tsv`.
- Latest full compatibility matrix also includes exact low-word gradient fill/stroke removals and passed across
  45 rows plus the header:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260507-123106/matrix.tsv`.
- Latest full compatibility matrix now covers the complete current exact low-word row set and passed across
  57 rows plus the header:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260507-131028/matrix.tsv`.
- Skiko unit coverage now rejects every missing low-word command-capability bit, mirroring the existing high-word loop.
- JBR API test source now asserts ABI 106 consistently with `JBRSkia.BUILD_ID`.
- Local artifact rebuild plus headless direct `JBRSkiaApiTest` execution passed against the patched classes/native bridge.
- Latest local artifact matrix passed on rebuilt ABI 106 artifacts:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260507-145018/matrix.tsv`.
- Latest focused fallback sentinel covers raw Skia-backed graphics-layer color filters:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-150427/suite.tsv`.
- Latest focused button chrome screenshot parity reconfirmed primary-button white text and centering on command replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-162055/suite.tsv`.
- Latest focused graphics-layer color-matrix lifecycle probe covers same-context resize and forced destination context
  migration with effect-handle redefinition/use/cache-hit gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-162709/suite.tsv`.
- Latest focused graphics-layer color-matrix screenshot parity covers the same resize and forced-context rows against old
  SwingGraphics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-175623/suite.tsv`.
- Latest full screenshot parity sweep now includes those graphics-layer color-matrix lifecycle rows and passed across
  77 rows plus header with all rows on command replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-180029/suite.tsv`.
- Latest focused RuntimeEffect color-filter lifecycle probe covers stable color-filter handles across same-context resize
  and forced destination-context migration:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-184642/suite.tsv`.
- Latest full command-probe sweep includes those RuntimeEffect color-filter lifecycle rows and passed across
  133 rows plus header:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-184927/suite.tsv`.
- Latest focused screenshot parity covers the same RuntimeEffect color-filter lifecycle rows against old SwingGraphics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-195934/suite.tsv`.
- Latest full screenshot parity sweep includes the RuntimeEffect color-filter lifecycle rows and passed across
  79 rows plus header with all rows on command replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-200201/suite.tsv`.
- Explicit graphics-layer scale/translation coverage is now in the Magic Jewel command and screenshot default suites.
  Focused command and parity rows passed, and the full screenshot parity sweep passed across 80 rows plus header:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-205517/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-090559/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-103324/suite.tsv`.
- Graphics-layer `ModulateAlpha` now has focused screenshot parity in the default screenshot suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-112203/suite.tsv`.
- Rectangular, rounded, and generic-path graphics-layer clips now have focused screenshot parity in the default
  screenshot suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-112646/suite.tsv`.
- Standalone graphics-layer blend mode, tint color filter, and color-matrix filter now have focused screenshot parity
  in the default screenshot suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-113210/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-113353/suite.tsv`.
- Plain graphics-layer replay plus combined blend+tint and blend+color-matrix graphics-layer rows now have focused
  screenshot parity in the default screenshot suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-113917/suite.tsv`.
- The expanded default screenshot parity sweep now covers 90 rows plus header, including the latest graphics-layer
  parity rows. All 90 rows passed, all 90 rows stayed on command replay, and zero rows reported structural or JBR
  picture fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-114354/suite.tsv`.
- RuntimeEffect invalid uniform-schema and named-child-schema fallbacks now have live Magic Jewel command sentinels.
  The focused rows passed with `shaderDescriptor` unsupported, JBR picture fallback, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-124449/suite.tsv`.
- The compact RuntimeEffect command subset passed after those sentinels were added, covering supported uniform/child
  shader replay plus invalid-schema, compile, build, and child-type fallback rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-124746/suite.tsv`.
- The full default command-probe sweep passed with those rows in the default set: 136 rows passed, with 108 command
  replay rows, 22 intentional JBR picture fallback rows, and 6 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-125344/suite.tsv`.

## Latest Completed Slice

Descriptor-backed paint color-filter, shader, RuntimeEffect source-cache eviction and build-failure fallback,
graphics-layer renderEffect, and handle-eviction parity now covers tint, color-matrix, lighting, image, composite,
standalone offset image-filter, chained image-filter, descriptor churn, RuntimeEffect source-cache churn, and
RuntimeEffect color-filter child-count failure:

- Magic Jewel adds `parity-color-filter-handle`, `parity-resize-color-filter-handle`, and
  `parity-forced-context-color-filter-handle` to the default screenshot parity suite.
- Magic Jewel also adds static `parity-color-matrix-filter` and `parity-lighting-filter` rows so the existing command
  descriptor coverage has old/new visual tripwires.
- Magic Jewel adds static `parity-image-shader` and `parity-composite-shader` rows; the composite row mirrors the
  command-side shader-handle define/use/cache-hit gates.
- Magic Jewel adds standalone `parity-graphics-layer-offset-effect` and
  `parity-graphics-layer-chained-render-effect` rows; these mirror the command-side effect-handle define/use/cache-hit
  gates without relying only on blended combination rows for visual coverage.
- Magic Jewel adds `parity-descriptor-eviction`, which draws enough unique effect and composite-shader descriptors to
  force JBR handle eviction while staying on command replay.
- Magic Jewel adds `parity-runtime-effect-shader-source-cache-eviction` and
  `parity-runtime-effect-source-cache-eviction`, which force the RuntimeEffect source cache below the row's source set
  and require typed shader/color-filter source-cache eviction markers while staying visually aligned with old
  SwingGraphics.
- Magic Jewel adds `commands-runtime-effect-color-filter-build-fallback`, which records a RuntimeEffect color-filter
  source that declares a child color filter but omits the child descriptor handle, requiring JBR to report
  `runtime-effect-build-failed` with `stage=child-count`.
- These rows isolate the descriptor-backed tint color-filter path from unrelated effect families and require JBR
  effect-handle definition, use, and cache-hit markers.
- The resize and forced-context rows require surface-change, command-cache-clear, and effect-handle redefinition
  markers, proving descriptor handles are rebuilt against the right destination context.
- Focused screenshot parity passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-154845/suite.tsv`
  and `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-165647/suite.tsv`
  and `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-180914/suite.tsv`
  and `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-191929/suite.tsv`
  and `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-202925/suite.tsv`
  and `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-214028/suite.tsv`.
- Full default screenshot parity passed across 106 rows plus header with all rows on command replay and zero fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-214152/suite.tsv`.
- Focused RuntimeEffect fallback subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-225248/suite.tsv`.
- Full default command-probe sweep passed across 145 rows plus header with 110 command replay rows, 26 intentional
  JBR picture fallback rows, and 9 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-225942/suite.tsv`.

## Previous Slice

Magic Jewel broad screenshot parity is current after adding graphics-layer color-matrix lifecycle rows:

- Full screenshot parity passed across 77 parity rows, including button chrome, point dots, embedded resource fonts,
  system fonts, shader descriptors, RuntimeEffect rows, and graphics-layer variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-180029/suite.tsv`.
- All rows passed with `fallback_new_count=0`; command-replay rows reported `jbr_picture_frames=0` and active
  `jbr_command_frames`.

## Previous Fallback Slice

Magic Jewel now has a structured fallback sentinel for `Canvas.drawVertices`:

- `commands-vertices` originally enabled a small `Canvas.drawVertices` triangle as a structured fallback sentinel.
- ABI 106 replaced this sentinel with direct serialized vertices replay; the historical fallback result remains useful as
  pre-ABI evidence.
- Focused fallback probe passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-093457/suite.tsv`.
- Full default command-probe sweep passed after adding the skew replay and vertices fallback rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-093702/suite.tsv`.

## Previous Skew Slice

`Canvas.skew` now lowers through the existing ABI 105 3x3 concat-matrix command instead of marking the command stream
unsupported:

- CMP records `Canvas.skew(sx, sy)` as `COMMAND_CONCAT_MATRIX33` with skew terms in the matrix.
- Magic Jewel added `commands-skew-transform` and `parity-skew-transform` rows.
- CMP focused recorder tests passed:
  `:compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesSkewAsConcatMatrix33Record --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.recordsCanvasSkewTransform`.
- Focused command replay passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-091628/suite.tsv`.
- Focused screenshot parity passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-092511/suite.tsv`.

## Previous RuntimeEffect Slice

Stable RuntimeEffect color-filter coverage and full screenshot parity are current:

- Added a stable RuntimeEffect color-filter row with no changing uniforms so effect-handle reuse can be asserted
  separately from the animated RuntimeEffect color-filter source-cache row. Focused command replay passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-221036/suite.tsv`.
- Full default command-probe sweep passed after adding stable RuntimeEffect color-filter coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-221229/suite.tsv`.
- Added focused screenshot parity for the stable RuntimeEffect color-filter row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-081922/suite.tsv`.
- Full screenshot parity passed, covering button chrome, point dots, resource/system fonts, shader descriptors,
  RuntimeEffect rows, and graphics-layer effects against old SwingGraphics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-082130/suite.tsv`.
- Rolling validation details live in [`docs/current/VALIDATION_LOG.md`](docs/current/VALIDATION_LOG.md).

## Next Work

1. Continue shader-family hardening and transform/graphics-layer edge cleanup from the current roadmap.
2. Prefer small, high-signal validation slices with focused command rows first, then default sweep or compatibility
   matrix when the touched surface warrants it.
3. Keep updating this compact plan; move verbose historical details to archive or focused docs, not back into this file.
4. Commit and push each major slice.

## Current Validation Hardening

Current validation gates are intentionally broad but summarized here to keep this file small:

- Screenshot parity asserts old/new pixel parity plus JBR-owned shader/effect handle definition, use, cache-hit, and
  context-invalidation markers on descriptor rows.
- `parity-button-chrome` explicitly guards the Pulse primary-button white text and centering regression seen in manual
  screenshots. The latest focused run reported `screenshot_primaryButtonWhiteText=405`,
  `screenshot_primaryButtonDarkText=0`, and `screenshot_parity_region_headerButtons_badPixelRatio=0.00381`.
- Resource-font and system-font rows cover JAR-embedded font loading and locally available system font loading.
- Command, compatibility, artifact, and screenshot harnesses default to non-focusable macOS windows via
  `MAGIC_JEWEL_BACKGROUND_WINDOW=true`, with per-row `background_window` report gates.
- Graphics-layer color-matrix descriptor lifecycle now has command rows for both same-context resize and forced
  destination-context migration, plus matching focused screenshot parity rows against old SwingGraphics.
- Graphics-layer transform coverage now includes explicit scale/translation command and screenshot parity rows, in
  addition to rotation, near-camera, and off-center-pivot rows.
- Graphics-layer compositing strategy coverage now has both command and screenshot coverage for ModulateAlpha and
  Offscreen.
- Graphics-layer clip coverage now has command and screenshot parity rows for rectangular, rounded, and generic-path
  clipping.
- Standalone graphics-layer blend mode and color-filter fields now have old/new screenshot parity rows separate from
  the render-effect combination rows.
- Plain graphics-layer replay and combined blend+color-filter fields now have direct old/new screenshot parity rows.
- The full default screenshot parity suite has been rerun after adding those lifecycle rows; the latest expanded run
  covered 90 rows, all with `fallback_new_count=0`, `jbr_picture_frames=0`, and nonzero `jbr_command_frames`.
- The full default command-probe suite has been rerun after adding RuntimeEffect color-filter schema sentinels; the
  latest sweep covered 138 rows, all passed, with the new color-filter schema rows reporting `colorFilterDescriptor`
  fallback and zero JBR command frames.
- The full default command-probe suite has since been rerun after adding the recursive nested-child color-filter
  sentinel; the latest sweep covered 139 rows, all passed, with 108 command replay rows, 25 intentional JBR picture
  fallback rows, and 6 expected explicit fallback-marker rows.
- The full default command-probe suite has since been rerun again after adding the recursive nested-child shader
  sentinel; the latest sweep covered 140 rows, all passed, with 108 command replay rows, 26 intentional JBR picture
  fallback rows, and 6 expected explicit fallback-marker rows.
- The full default command-probe suite has since been rerun after tightening the stable RuntimeEffect color-filter row
  to require JBR RuntimeEffect source-cache hits and at most one source-cache miss. The latest sweep again covered 140
  rows, all passed, with 108 command replay rows, 26 intentional JBR picture fallback rows, and 6 expected explicit
  fallback-marker rows.
- The stable RuntimeEffect color-filter lifecycle command and parity rows now require source-cache hits and at most one
  source-cache miss. The latest command sweep covered 140 rows, and the latest screenshot parity sweep covered 90 rows;
  both passed with the lifecycle rows on command replay and without structural fallback.
- RuntimeEffect shader and color-filter source-cache eviction now have native JBR observability plus focused Magic Jewel
  command sentinels with typed `shader`/`colorFilter` evict gates. The latest command sweep covered 144 rows, all
  passed, with 110 command replay rows, 26 intentional JBR picture fallback rows, and 8 expected explicit
  fallback-marker rows.
- Stable RuntimeEffect color-filter descriptors now have command lifecycle rows for same-context resize and forced
  destination-context migration, with effect-handle redefinition/use/cache-hit and RuntimeEffect source-cache-hit gates.
- Matching focused screenshot parity rows now cover those RuntimeEffect color-filter lifecycle paths against old
  SwingGraphics.
- RuntimeEffect invalid uniform-schema and named-child-schema metadata now have live fallback sentinels so descriptor
  validation failures stay structural (`shaderDescriptor`) rather than producing incomplete command streams.
- RuntimeEffect color-filter invalid uniform-schema and named-child-schema metadata now have matching live fallback
  sentinels, with focused validation proving `colorFilterDescriptor` fallback and zero JBR command frames.
- RuntimeEffect color-filter recursive child descriptors now have a focused invalid nested-child fallback sentinel so
  parent color-filter descriptors cannot smuggle invalid child metadata into command replay.
- RuntimeEffect shader recursive child descriptors now have the same focused invalid nested-child fallback sentinel for
  parent shader handles.
- RuntimeEffect color-filter, offset image-filter, chained path-effect, and shader-color-filter effect children now have
  focused missing-child sentinels that rewrite a recorded child slot to an undefined effect handle and require
  pre-replay `command-stream-invalid` fallback.
- Top-level descriptor use-after-evict coverage now includes both shader refs and color-filter refs with live marker
  gates, proving stale handle rejection before any JBR picture or command replay.
- Top-level undefined descriptor use coverage now includes both color-filter refs in the default suite and a focused
  shader-ref sentinel, both with live marker gates and zero JBR replay.
- Transformed, composite, and shader-color-filter descriptor children now have focused missing-child sentinels that
  rewrite a recorded child slot to an undefined shader handle and require pre-replay `command-stream-invalid` fallback.
- Offset image-filter and chained path-effect descriptors now have focused evicted-child sentinels. The live probes
  insert a child-handle eviction immediately before the parent descriptor, then assert `command-stream-invalid`,
  `unsupported=none`, and zero JBR picture/command frames.
- RuntimeEffect color-filter build failures now have a focused child-type sentinel that corrupts a recorded
  color-filter child descriptor into a shader child and asserts JBR reports
  `JBR_SKIA_INTEROP_RUNTIME_COLOR_FILTER_BUILD_FAILED ... stage=positional-child-type` before Skiko falls back.
- Detailed validation paths and row-level counts live in [`docs/current/VALIDATION_LOG.md`](docs/current/VALIDATION_LOG.md).

## Key Files

- Current roadmap: [`ROADMAP.md`](ROADMAP.md)
- Document index: [`docs/INDEX.md`](docs/INDEX.md)
- Current validation log: [`docs/current/VALIDATION_LOG.md`](docs/current/VALIDATION_LOG.md)
- Full checkpoint archive: [`docs/history/SKIA_COMPOSE_ZERO_COPY_POC_PLAN.full.md`](docs/history/SKIA_COMPOSE_ZERO_COPY_POC_PLAN.full.md)
- Full roadmap archive: [`docs/history/ROADMAP.full.md`](docs/history/ROADMAP.full.md)
- Shader/effect design: [`doc/skia-shader-factory.md`](doc/skia-shader-factory.md)

## Validation Commands

Run from `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel`:

```sh
./scripts/rebuild-jbr-skia-local-artifacts.sh
SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh
SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-compatibility-matrix.sh
SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh
```

Use focused `CASES=...` subsets before broad sweeps.
