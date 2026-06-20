# Unsupported Command Census

This file is the working ledger for driving Magic Jewel/JBR Skia command-probe coverage to zero unsupported rows on
macOS/Metal. Keep it updated from full command-probe evidence, and move rows out only after focused validation proves
they no longer report unsupported markers.

## Target Goal

- End state: full command-probe pass with `unsupported_rows=0`, no picture fallback rows for supported command coverage,
  and no unexpected fallback.
- Cadence: clear unsupported rows in narrow batches; validate focused batches of up to 10 affected rows; run at most one
  full broad pass per local calendar day; fix regressions immediately as they appear.
- Original full-sweep evidence source:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-144955/suite.tsv`.
- Evidence summary from that full/default sweep: `549/549` rows passed, but `unsupported_rows=79`,
  `picture_frames=78827` across unsupported rows, and `jbr_command_frames=0` across unsupported rows.
- Current default command-probe surface:
  `LIST_CASE_COUNT=true ./scripts/jbr-skia-command-probe-suite.sh` reports `696` rows. The final proof must cover this
  current default surface, not just the older `549`-row full sweep. See
  [`COMMAND_PROBE_CURRENT_DEFAULT_SURFACE.md`](COMMAND_PROBE_CURRENT_DEFAULT_SURFACE.md) for the list-only delta.
- Current focused progress: all `79` original unsupported rows and all `151` rows added to the current default surface
  since the latest completed full/default census have focused validation evidence with `unsupported=none`,
  `fallback_new_count=0`, `jbr_picture_frames=0`, and non-zero `jbr_command_frames`.
- Latest focused evidence:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260620-160156/suite.tsv`.
  This covered the next ten default-order rows after the save-layer invalid-reference continuation: corrupt save-layer
  scalar/blend-mode fixtures. The focused suite passed `10/10` with `unsupported_rows=0` and `jbr_picture_frames=0`;
  its `fallback_sum=10` is expected parser fallback, not active unsupported command coverage.
- Current interpretation: there are zero known active unsupported rows by focused evidence. The zero-unsupported goal
  still requires the next capped full/default sweep to complete and prove `unsupported_rows=0` across the whole suite.
  The 2026-06-20 daily broad run stopped after `390` of `696` rows with `unsupported_rows=0` and
  `jbr_picture_frames=0` so far, but it is partial evidence only; the daily broad cap prevents relaunching another
  full/default sweep on 2026-06-20.
- 2026-06-20 narrow continuation after the interrupted broad run found a transient local artifact-state failure
  (`public-api-missing`) rather than an unsupported command. Rebuilding local artifacts restored the bridge, then the
  bridge smoke plus the next ten default-order rows passed focused validation with `unsupported=none` and
  `jbr_picture_frames=0`. Five corrupt invalid-stroke-width rows intentionally emitted structured command-stream
  fallback and remain expected-fallback coverage, not active unsupported rows.
- 2026-06-20 malformed linear-gradient descriptor continuation covered the next ten default-order rows. The focused
  suite passed `10/10` with `unsupported_rows=0` and `jbr_picture_frames=0`; all ten rows intentionally emitted
  structured fallback for malformed gradient stream/descriptor payloads.
- 2026-06-20 malformed gradient path continuation covered the next ten default-order rows. The focused suite passed
  `10/10` with `unsupported_rows=0` and `jbr_picture_frames=0`; all ten rows intentionally emitted structured fallback
  for malformed gradient/path payloads.
- 2026-06-20 malformed radial/sweep gradient path continuation covered the next ten default-order rows. The focused
  suite passed `10/10` with `unsupported_rows=0` and `jbr_picture_frames=0`; all ten rows intentionally emitted
  structured fallback for malformed radial/sweep gradient path payloads.
- 2026-06-20 malformed sweep/radial gradient descriptor continuation covered the next ten default-order rows. The
  focused suite passed `10/10` with `unsupported_rows=0` and `jbr_picture_frames=0`; all ten rows intentionally emitted
  structured fallback for malformed sweep/radial gradient payloads.
- 2026-06-20 malformed radial-gradient descriptor continuation covered the next ten default-order rows. The focused
  suite passed `10/10` with `unsupported_rows=0` and `jbr_picture_frames=0`; all ten rows intentionally emitted
  structured fallback for malformed radial-gradient payloads.
- 2026-06-20 radial-gradient/image-filter mixed continuation covered the next ten default-order rows. The focused suite
  passed `10/10` with `unsupported_rows=0` and `jbr_picture_frames=0`; the three supported image-filter rows replayed
  through JBR command frames and the seven malformed/invalid-reference rows intentionally emitted structured fallback.
- 2026-06-20 invalid image color-filter reference continuation covered the next ten default-order rows. The focused
  suite passed `10/10` with `unsupported_rows=0` and `jbr_picture_frames=0`; all ten rows intentionally emitted
  structured fallback for invalid image color-filter reference/descriptor payloads.
- 2026-06-20 image color-matrix/raw color-filter continuation covered the next ten default-order rows. The focused
  suite passed `10/10` with `unsupported_rows=0` and `jbr_picture_frames=0`; six supported color-filter rows replayed
  through JBR command frames and four invalid descriptor/filter scalar rows intentionally emitted structured fallback.
- 2026-06-20 color-filter replay continuation covered the next ten default-order rows. The focused suite passed `10/10`
  with `unsupported_rows=0` and `jbr_picture_frames=0`; eight supported color-filter rows replayed through JBR command
  frames and two invalid fill-rect color-filter reference rows intentionally emitted structured fallback.
- 2026-06-20 color-matrix/lighting/descriptor replay continuation covered the next ten default-order rows. The focused
  suite passed `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows replayed
  through JBR command frames, including the former nonfinite color-matrix fallback fixture.
- 2026-06-20 shader descriptor redefine continuation covered the next ten default-order rows. The focused suite passed
  `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows replayed through JBR
  command frames.
- 2026-06-20 path-effect/vertices replay continuation covered the next ten default-order rows. The focused suite passed
  `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows replayed through JBR
  command frames, including the legacy-named fallback fixtures.
- 2026-06-20 vertices-invalid/blend-mode continuation covered the next ten default-order rows. The focused suite passed
  `10/10` with `unsupported_rows=0` and `jbr_picture_frames=0`; three rows replayed through JBR command frames and
  seven malformed draw-vertices parser fixtures intentionally emitted structured fallback.
- 2026-06-20 graphics-layer invalid replay continuation covered the next ten default-order rows. The focused suite
  passed `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows replayed
  through JBR command frames, including the legacy-named invalid graphics-layer fixtures.
- 2026-06-20 graphics-layer modulate/offscreen continuation covered the next ten default-order rows. The focused suite
  passed `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows replayed
  through JBR command frames, including the remaining legacy-named invalid graphics-layer transform fixtures.
- 2026-06-20 graphics-layer clip/blend continuation covered the next ten default-order rows. The focused suite passed
  `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows replayed through JBR
  command frames.
- 2026-06-20 graphics-layer blend/color-filter continuation covered the next ten default-order rows. The focused suite
  passed `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows replayed
  through JBR command frames, including the legacy-named raw color-filter fixtures.
- 2026-06-20 graphics-layer render-effect continuation covered the next ten default-order rows. The focused suite
  passed `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows replayed
  through JBR command frames, including the legacy-named unsupported-child and raw image-filter-effect fixtures.
- 2026-06-20 graphics-layer render-effect filter/blend continuation covered the next ten default-order rows. The
  focused suite passed `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows
  replayed through JBR command frames.
- 2026-06-20 graphics-layer render-effect blend/filter continuation covered the next ten default-order rows. The
  focused suite passed `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows
  replayed through JBR command frames.
- 2026-06-20 graphics-layer near-camera/shadow continuation covered the next ten default-order rows. The focused suite
  passed `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows replayed
  through JBR command frames, including the legacy-named invalid shadow fixtures.
- 2026-06-20 graphics-layer shadow/rotation continuation covered the next ten default-order rows. The focused suite
  passed `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows replayed
  through JBR command frames.
- 2026-06-20 graphics-layer transform/near-camera continuation covered the next ten default-order rows. The focused
  suite passed `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows replayed
  through JBR command frames.
- 2026-06-20 graphics-layer pivot/filter continuation covered the next ten default-order rows. The focused suite
  passed `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows replayed
  through JBR command frames, including the legacy-named invalid camera-distance fixture.
- 2026-06-20 save-layer filter/blend continuation covered the next ten default-order rows. The focused suite passed
  `10/10` with `fallback_sum=0`, `unsupported_rows=0`, and `jbr_picture_frames=0`; all ten rows replayed through JBR
  command frames.
- 2026-06-20 save-layer invalid-record continuation covered the next ten default-order rows. The focused suite passed
  `10/10` with `unsupported_rows=0` and `jbr_picture_frames=0`; three supported rows replayed through JBR command
  frames and seven corrupt save-layer parser fixtures intentionally emitted structured fallback.
- 2026-06-20 save-layer invalid-reference continuation covered the next ten default-order rows. The focused suite
  passed `10/10` with `unsupported_rows=0` and `jbr_picture_frames=0`; all ten corrupt save-layer reference/record
  fixtures intentionally emitted structured fallback.
- 2026-06-20 save-layer invalid-scalar continuation covered the next ten default-order rows. The focused suite passed
  `10/10` with `unsupported_rows=0` and `jbr_picture_frames=0`; all ten corrupt save-layer scalar/blend-mode fixtures
  intentionally emitted structured fallback.

## Original Full-Sweep Unsupported Marker Families

| Row count | Marker family |
| ---: | --- |
| 176 | `graphicsLayer` |
| 11 | `shader` |
| 6 | `colorFilter` |
| 4 | `unsupportedScope` |
| 4 | `path` |
| 3 | `shaderDescriptor` |
| 3 | `colorFilterDescriptor` |
| 2 | `vertices` |
| 2 | `saveLayer` |
| 2 | `pathEffect` |
| 2 | `image` |
| 2 | `clipPath` |
| 1 | `transform` |
| 1 | `points` |
| 1 | `paintStyle` |
| 1 | `imageShaderImage` |
| 1 | `colorMatrixNonfinite` |
| 1 | `blendMode_Clear` |
| 1 | `blendLayerBounds` |
| 1 | `linearGradientStops` |
| 1 | `linearGradientPoints` |
| 1 | `linearGradientColorCount` |
| 1 | `linearGradientPath` |
| 1 | `linearGradientPathPaint` |
| 1 | `linearGradientStrokeWidth` |
| 1 | `linearGradientRoundRectRadius` |
| 1 | `linearGradientStrokeRoundRectRadius` |
| 1 | `radialGradientStops` |
| 1 | `radialGradientGeometry` |
| 1 | `radialGradientColorCount` |
| 1 | `radialGradientPath` |
| 1 | `radialGradientPathPaint` |
| 1 | `radialGradientStrokeWidth` |
| 1 | `radialGradientRoundRectRadius` |
| 1 | `radialGradientStrokeRoundRectRadius` |
| 1 | `sweepGradientStops` |
| 1 | `sweepGradientGeometry` |
| 1 | `sweepGradientColorCount` |
| 1 | `sweepGradientPath` |
| 1 | `sweepGradientPathPaint` |
| 1 | `sweepGradientStrokeWidth` |
| 1 | `sweepGradientRoundRectRadius` |
| 1 | `sweepGradientStrokeRoundRectRadius` |

## Unsupported Rows

| Row | Unsupported markers | fallback_new_count | picture frames | command frames |
| --- | --- | ---: | ---: | ---: |
| _None currently known by focused evidence._ | | | | |

## Cleared Rows

| Row | Cleared by evidence | Notes |
| --- | --- | --- |
| `commands-forced-context-radial-gradient-shader-color-filter` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260619-165400/suite.tsv` | report-parser artifact cleared; `cmp_unsupported_reasons=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=6368` |
| `commands-forced-context-runtime-effect-pure-color` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260619-165400/suite.tsv` | report-parser artifact cleared; `cmp_unsupported_reasons=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=8463` |
| `commands-linear-gradient-path-invalid-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-185527/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1935` |
| `commands-radial-gradient-path-invalid-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-185527/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=6254` |
| `commands-sweep-gradient-path-invalid-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-185527/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=246` |
| `commands-runtime-effect-invalid-uniform-schema-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-185527/suite.tsv` | promoted to valid schema-name fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1300` |
| `commands-runtime-effect-invalid-child-schema-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-185527/suite.tsv` | promoted to valid schema-name fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1310` |
| `commands-runtime-effect-invalid-nested-child-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-185527/suite.tsv` | promoted to valid nested schema fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=244` |
| `commands-runtime-effect-color-filter-invalid-uniform-schema-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-185527/suite.tsv` | promoted to valid schema-name fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=528` |
| `commands-runtime-effect-color-filter-invalid-child-schema-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-185527/suite.tsv` | promoted to valid schema-name fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1207` |
| `commands-runtime-effect-color-filter-invalid-nested-child-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-185527/suite.tsv` | promoted to valid nested schema fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=508` |
| `commands-graphics-layer-invalid-camera-distance-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-185527/suite.tsv` | promoted to finite camera-distance fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=512` |
| `commands-invalid-gradient-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-190501/suite.tsv` | promoted to finite sweep-gradient stops fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1387` |
| `commands-graphics-layer-invalid-rotation-z-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-184312/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=999` |
| `commands-graphics-layer-invalid-translation-x-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-184312/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=555` |
| `commands-graphics-layer-invalid-translation-y-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-184312/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1454` |
| `commands-graphics-layer-invalid-rotation-x-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-184312/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=498` |
| `commands-graphics-layer-invalid-rotation-y-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-184312/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=463` |
| `commands-graphics-layer-invalid-blend-mode-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-184312/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1352` |
| `commands-graphics-layer-unrecorded-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-184312/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2160` |
| `commands-graphics-layer-unsupported-child-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-184312/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=766` |
| `commands-graphics-layer-invalid-shadow-elevation-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-184312/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1202` |
| `commands-graphics-layer-invalid-shadow-path-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-184312/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=200` |
| `commands-radial-gradient-stroke-round-rect-invalid-radius-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-182900/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=736` |
| `commands-sweep-gradient-stroke-round-rect-invalid-radius-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-182900/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=464` |
| `commands-color-matrix-filter-nonfinite-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-182900/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=248` |
| `commands-path-effect-color-filter-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-182900/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1530` |
| `commands-vertices-invalid-blend-mode-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-182900/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=999` |
| `commands-graphics-layer-invalid-size-width-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-183611/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1833` |
| `commands-graphics-layer-invalid-size-height-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-183611/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=398` |
| `commands-graphics-layer-invalid-alpha-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-183611/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=592` |
| `commands-graphics-layer-invalid-scale-x-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-183611/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=369` |
| `commands-graphics-layer-invalid-scale-y-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-183611/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1317` |
| `commands-linear-gradient-invalid-color-count-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-181608/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2390` |
| `commands-radial-gradient-invalid-color-count-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-181608/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=316` |
| `commands-sweep-gradient-invalid-color-count-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-181608/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=6977` |
| `commands-linear-gradient-invalid-stroke-width-public-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-181608/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1642` |
| `commands-radial-gradient-invalid-stroke-width-public-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-181608/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1841` |
| `commands-sweep-gradient-invalid-stroke-width-public-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-181608/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=859` |
| `commands-linear-gradient-round-rect-invalid-radius-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-181608/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2226` |
| `commands-radial-gradient-round-rect-invalid-radius-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-181608/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2870` |
| `commands-sweep-gradient-round-rect-invalid-radius-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-181608/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2062` |
| `commands-linear-gradient-stroke-round-rect-invalid-radius-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-181608/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2249` |
| `commands-invalid-blend-layer-bounds-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-180317/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=911` |
| `commands-invalid-concat-transform-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-180317/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=753` |
| `commands-clip-path-invalid-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-180317/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1664` |
| `commands-draw-path-invalid-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-180317/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=518` |
| `commands-invalid-point-dots-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-180317/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2392` |
| `commands-linear-gradient-invalid-stops-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-180317/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=5053` |
| `commands-linear-gradient-invalid-points-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-180317/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=5367` |
| `commands-radial-gradient-invalid-stops-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-180317/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2052` |
| `commands-radial-gradient-invalid-geometry-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-180317/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=463` |
| `commands-sweep-gradient-invalid-geometry-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-180317/suite.tsv` | promoted to finite command fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=4371` |
| `commands-linear-gradient-path-stroke-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-165037/suite.tsv` | `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=3700` |
| `commands-radial-gradient-path-stroke-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-165037/suite.tsv` | `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2207` |
| `commands-sweep-gradient-path-stroke-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-165037/suite.tsv` | `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2123` |
| `commands-descriptor-stroke-shader-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-171051/suite.tsv` | `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1728` |
| `commands-image-path-effect-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-171756/suite.tsv` | `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1245` |
| `commands-image-shader-invalid-image-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-172205/suite.tsv` | `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1589` |
| `commands-raw-image-shader-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-172711/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=265` |
| `commands-raw-linear-gradient-shader-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-172711/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=444` |
| `commands-raw-radial-gradient-shader-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-172711/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1849` |
| `commands-raw-sweep-gradient-shader-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-172711/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=565` |
| `commands-raw-noise-shader-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-172711/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=652` |
| `commands-raw-turbulence-shader-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-172711/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1828` |
| `commands-raw-runtime-effect-shader-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-173458/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=327` |
| `commands-raw-runtime-effect-color-filter-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-173458/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=515` |
| `commands-raw-blend-color-filter-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-173458/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1121` |
| `commands-raw-discrete-path-effect-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-174003/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=314` |
| `commands-image-raw-table-color-filter-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-174344/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2113` |
| `commands-raw-table-color-filter-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-174344/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2200` |
| `commands-graphics-layer-raw-color-filter-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-174344/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2187` |
| `commands-graphics-layer-raw-table-color-filter-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-174344/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=843` |
| `commands-graphics-layer-raw-image-filter-effect-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-174344/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=932` |
| `commands-save-layer-raw-color-filter-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-174344/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=559` |
| `commands-save-layer-raw-table-color-filter-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-174344/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1472` |
| `commands-vertices-raw-color-filter-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-175425/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=1997` |
| `commands-raw-conical-gradient-shader-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-175425/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2234` |
| `commands-opaque-shader-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-175425/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=2368` |
| `commands-composite-opaque-shader-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-175425/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=720` |
| `commands-picture-shader-fallback` | `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260618-175425/suite.tsv` | promoted to descriptor-backed fixture; `unsupported=none`, `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=700` |
