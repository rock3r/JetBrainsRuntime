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

Magic Jewel broad screenshot parity is current after adding skew replay and vertices fallback coverage:

- Full screenshot parity passed across 65 parity rows, including button chrome, point dots, embedded resource fonts,
  system fonts, shader descriptors, RuntimeEffect rows, and graphics-layer variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-110425/suite.tsv`.
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

Screenshot parity rows now assert JBR-owned shader/effect descriptor handle use, and stable/cache-focused rows assert
cache hits, so old/new pixel parity cannot pass on descriptor definition alone. Focused parity subsets passed:

- `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-151737/suite.tsv`.
- `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-152630/suite.tsv`.

Full screenshot parity also passed with those tightened gates:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-153439/suite.tsv`.

The current local artifact matrix also passed for the ABI 106 bundle and missing-public-API fallback:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260505-161806/matrix.tsv`.

Magic Jewel's supported path-effect command row is now named `commands-path-effect`; the historical
`commands-path-effect-fallback` name remains a compatibility alias. Focused validation passed:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-162211/suite.tsv`.

The focused `parity-button-chrome` row passed on current artifacts and catches the Pulse primary-button content color
and centering issue against old SwingGraphics:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-163010/suite.tsv`.

Descriptor shader rect recording now marks unsupported non-fill paint styles as structural fallback instead of silently
omitting the draw from a command frame. CMP focused tests passed, and Magic Jewel `commands-descriptor-stroke-shader-fallback`
passed with `paintStyle` unsupported and picture fallback:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-163839/suite.tsv`.

The full default Magic Jewel command-probe sweep also passed after adding that row:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-164215/suite.tsv`.

Magic Jewel then broadened the same row to exercise both generic shader descriptors and image-shader rects with
unsupported stroke paint. Focused validation passed:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-181114/suite.tsv`.

CMP's full `JbrSkiaCommandRecorderTest` class passed after the descriptor/image shader fallback hardening and
recorder-test isolation cleanup:
`./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
in `/Users/rock3r/src/jbr-skia-zero-copy/cmp`.

Path-effect descriptor replay now has a focused unsupported color-filter fallback sentinel. CMP strict recorder tests
passed for `rejectsPathEffectDescriptorColorFilterInStrictMode` plus the existing corner path-effect replay test, and
Magic Jewel `commands-path-effect-color-filter-fallback` passed with `colorFilter` unsupported, JBR picture fallback,
and no JBR command frames:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-184635/suite.tsv`.

The full default Magic Jewel command-probe sweep also passed with that new row included:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-184958/suite.tsv`.

CMP now reports concrete unsupported image-paint reasons before `drawImageRect` falls back. Focused recorder tests passed
for unsupported image path effects while preserving image ARGB and tint color-filter replay, and Magic Jewel
`commands-image-path-effect-fallback` passed with `pathEffect` unsupported, picture fallback, and no JBR command frames:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-212515/suite.tsv`.

The full default Magic Jewel command-probe sweep also passed with that image path-effect fallback sentinel included:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-212817/suite.tsv`.

Magic Jewel's `commands-descriptor-eviction` lifecycle row now requires both define and use markers before eviction for
effect and shader handles. The local rebuild script also recreates the minimal generated native headers needed after
`/tmp` is cleaned. Focused validation passed after rebuilding `/tmp/jbr-api-shim.jar`, `/tmp/jbr-skia-run/desktop`, and
`/tmp/jbr-skia-native/libjbrskiainterop.dylib`:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-085227/suite.tsv`.

The full default Magic Jewel command-probe sweep also passed with the tightened descriptor-eviction gates:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-085720/suite.tsv`.

The local artifact matrix passed after the rebuild-script hardening, validating the rebuilt current bundle and
missing-public-API fallback:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260506-102206/matrix.tsv`.

The compatibility matrix passed on the rebuilt ABI 106 artifacts, covering happy path plus ABI, native ABI,
low/high-word capability, exact capability-removal, and public-API-missing fallback rows:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260506-102434/matrix.tsv`.

The full screenshot parity suite passed on the rebuilt ABI 106 artifacts after descriptor-eviction hardening. It covered
66 rows including button chrome, point dots, embedded resource fonts, system fonts, shader descriptors, RuntimeEffect
rows, and graphics-layer variants. All rows passed with `fallback_new_count=0`; command rows reported
`jbr_picture_frames=0` and nonzero `jbr_command_frames`. The `parity-button-chrome` row retained the Pulse primary
button guard with `primaryButtonWhiteText=405`, `primaryButtonDarkText=0`, and
`header_buttons_bad_pixel_ratio=0.00381`; resource/system font rows also replayed through command frames:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-103937/suite.tsv`.

Magic Jewel now has a focused `commands-save-layer-raw-color-filter-fallback` row. It asserts that a raw Skia
`ColorFilter.makeBlend(...).asComposeColorFilter()` on `Canvas.saveLayer` stays on structured `saveLayer` fallback,
while metadata-backed saveLayer tint continues through command replay. CMP's focused recorder test passed, and the
paired Magic Jewel rows passed with `commands-save-layer-filter` replaying commands and the raw row producing picture
fallback with zero JBR command frames:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-112750/suite.tsv`.

The full default command-probe sweep also passed with the new saveLayer fallback row included:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-113037/suite.tsv`.

Magic Jewel also added focused screenshot parity for the supported saveLayer tint-filter replay path:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-130209/suite.tsv`.

The full screenshot parity suite passed with the supported saveLayer tint-filter row included by default. It covered
67 rows including button chrome, point dots, embedded resource fonts, system fonts, saveLayer tint filters,
shader/effect descriptors, RuntimeEffect rows, and graphics-layer variants. Command rows stayed on JBR replay with
`jbr_picture_frames=0`, and the `parity-button-chrome` row retained the Pulse white-text guard:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-130350/suite.tsv`.

Magic Jewel then tightened deterministic shader/effect screenshot parity rows so visual parity also requires JBR handle
cache-hit markers where replay already proves descriptor reuse. Focused parity passed for solid-color, noise,
turbulence, transformed shader, RuntimeEffect shader, RuntimeEffect shader+filter, uniform-only, color-filter, and
color-filter-child rows:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-135456/suite.tsv`.

The full screenshot parity suite then passed with those stricter default gates across 67 rows. The hardened
`parity-color-shader` row reported `jbr_shader_handle_cache_hit_frames=1094`, and the hardened
`parity-runtime-effect-color-filter` row reported `jbr_effect_handle_cache_hit_frames=2072`:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-140238/suite.tsv`.

RuntimeEffect compile-failure lifecycle coverage now has a focused live sentinel. Skiko can corrupt one RuntimeEffect
SKSL source after recording while recomputing the descriptor source hash, so JBR accepts the descriptor shape and fails
at native `SkRuntimeEffect` compilation. Magic Jewel `commands-runtime-effect-compile-fallback` passed with one
structured fallback, `jbr_runtime_effect_compile_failures=1`, and `jbr_command_frames=0`:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-150134/suite.tsv`.

The full default command-probe sweep then passed with the compile-failure row included by default. It covered the ABI
106 command replay surface, font/resource rows, shader/effect descriptor rows, graphics layers, saveLayer, and existing
fallback sentinels; `commands-runtime-effect-compile-fallback` reported one structured fallback and no JBR command
frames:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-150452/suite.tsv`.

Magic Jewel report summaries now expose `magic_jewel_background_window`, and report-validation tests assert the harness
defaults it to `true`. This keeps command, matrix, and parity automation on non-focusable macOS windows unless an
interactive debugging run opts out:
`./scripts/test-jbr-skia-report-validation.sh` in `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel`.

The compatibility matrix now carries a `background_window` column and fails rows unless the summary matches
`EXPECT_BACKGROUND_WINDOW` (default `true`). A short matrix sweep passed with every row reporting
`background_window=true`:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260506-163829/matrix.tsv`.

The artifact matrix now applies the same `background_window` column and guard. A short current-artifact run passed with
`current-all` and `missing-public-api` reporting `background_window=true`; optional old-artifact rows were skipped as
expected:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260506-165134/matrix.tsv`.

Magic Jewel screenshot parity now includes `parity-gradient-stroke`, isolating the stroked linear-gradient rect command
path with a right-probe-strip gate. Focused parity passed with `fallback_new_count=0`, `jbr_picture_frames=0`, and
`jbr_command_frames=753`:
`/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-165828/suite.tsv`.

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
