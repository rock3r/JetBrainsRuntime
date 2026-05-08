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
- Raw Skiko-owned shader/effect/path-effect families remain explicit fallback sentinels.
- Harness windows are non-focus-stealing by default via `MAGIC_JEWEL_BACKGROUND_WINDOW=true`.
- `MagicLabel` is a test harness switch: when Compose text is disabled, it renders fixed white boxes to isolate geometry
  parity from text rasterization drift. It is not a replacement for Jewel `Text`.
- The working docs are intentionally split: this plan and `ROADMAP.md` stay compact, validation details live in
  `docs/current/VALIDATION_LOG.md`, and verbose historical checkpoints live in `docs/history/`.
- Latest full command-probe sweep covered 131 rows plus the header and passed on current artifacts:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-163006/suite.tsv`.
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

ABI 106 adds direct `Canvas.drawVertices` command replay for serialized vertex payloads:

- JBR/JBR API expose `COMMAND_DRAW_VERTICES = 67` and `COMMAND_CAP64_HIGH_DRAW_VERTICES = 131072`.
- JBR validates vertex mode, blend mode, vertex/index counts, record length, and serialized positions/texture coordinates,
  colors, and indices before replaying through native Skia vertices.
- CMP records supported `Canvas.drawVertices` calls instead of marking `vertices` unsupported; unsupported paint/blend
  families still fall back structurally.
- Magic Jewel `commands-vertices` and `parity-vertices` probe the colored triangle replay path.
- Focused command replay passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-120216/suite.tsv`.
- Focused screenshot parity passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-120655/suite.tsv`.
- Focused missing-capability fallback passed:
  `/tmp/jbr-skia-vertices-cap-missing/summary.properties`.
- Full default command-probe sweep passed, including `commands-vertices`, font, shader/effect descriptor,
  graphics-layer, and intentional fallback-sentinel rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-121247/suite.tsv`.
- Compatibility matrix passed after the ABI 106 high-word mask update, including the exact missing draw-vertices
  capability row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260505-133924/matrix.tsv`.

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
