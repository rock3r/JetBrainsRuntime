# Current Validation Log

This file keeps the rolling validation ledger out of the top-level roadmap and plan. Keep the newest high-signal
entries here, and move older narrative detail to `docs/history/` only when this file starts getting noisy.

## Latest Broad Sweeps

- Full default command-probe sweep after the current shader/effect/font/graphics-layer parity hardening. It covered 128
  rows plus the header. Supported rows stayed on command replay with `fallback_new_count=0`, `unsupported=none`,
  `jbr_picture_frames=0`, and positive `jbr_command_frames`; intentional fallback sentinels stayed structurally
  isolated with zero command frames. Key rows: `commands-point-dots` `jbr_command_frames=1822`,
  `commands-gradient-surfaces` `jbr_command_frames=834`, `commands-gradient-paths` `jbr_command_frames=1472`,
  `commands-gradient-shaders` `jbr_command_frames=1846`, `commands-native-resource-font-text`
  `jbr_command_frames=1206`, `commands-native-system-font-text` `jbr_command_frames=1409`,
  `commands-runtime-effect-stable-color-filter` `jbr_command_frames=1251`,
  `commands-graphics-layer-render-effect` `jbr_command_frames=1342`, and `commands-save-layer-filter`
  `jbr_command_frames=850`. The expected `commands-runtime-effect-compile-fallback` row passed with
  `fallback_new_count=1` and `jbr_command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-102836/suite.tsv`.
- Full default screenshot parity suite after adding the dedicated `parity-gradient-shaders` row. It covered 75 rows,
  including button chrome, point dots, embedded resource fonts, system fonts, draw shapes, clipRect/clip-out, clipPath,
  blend modes, gradient surfaces, gradient paths, explicit gradient shader brushes, stroked gradients, shader
  descriptors, RuntimeEffect rows, and graphics-layer variants. All rows passed with `fallback_new_count=0`; the suite
  TSV has 76 lines including the header. Key rows: `parity-button-chrome` `jbr_command_frames=1011`,
  `parity-native-resource-font-text` `jbr_command_frames=765`, `parity-native-system-font-text`
  `jbr_command_frames=637`, `parity-gradient-surfaces` `jbr_command_frames=673`, `parity-gradient-paths`
  `jbr_command_frames=1673`, and `parity-gradient-shaders` `jbr_command_frames=593`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-093958/suite.tsv`.
- Focused `parity-gradient-shaders` screenshot parity after adding a dedicated explicit
  `ShaderBrush(LinearGradientShader/RadialGradientShader/SweepGradientShader)` row to the default parity suite. The row
  stayed on command replay with `fallback_new_count=0`, `jbr_picture_frames=0`, and `jbr_command_frames=901`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-093913/suite.tsv`.
- Full default screenshot parity suite after adding the dedicated `parity-gradient-paths` row. It covered 74 rows,
  including button chrome, point dots, embedded resource fonts, system fonts, draw shapes, clipRect/clip-out, clipPath,
  blend modes, gradient surfaces, gradient paths, stroked gradients, shader descriptors, RuntimeEffect rows, and
  graphics-layer variants. All rows passed with `fallback_new_count=0`; the suite TSV has 75 lines including the
  header. Key rows: `parity-button-chrome` `jbr_command_frames=698`, `parity-native-resource-font-text`
  `jbr_command_frames=581`, `parity-native-system-font-text` `jbr_command_frames=628`, `parity-blend-modes`
  `jbr_command_frames=569`, `parity-gradient-surfaces` `jbr_command_frames=1179`, and `parity-gradient-paths`
  `jbr_command_frames=1639`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-220734/suite.tsv`.
- Focused `parity-gradient-paths` screenshot parity after adding a dedicated linear/radial/sweep gradient-filled path
  row to the default parity suite. The row stayed on command replay with `fallback_new_count=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=890`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-220635/suite.tsv`.
- Full default screenshot parity suite after adding the dedicated `parity-gradient-surfaces` row. It covered 73 rows,
  including button chrome, point dots, embedded resource fonts, system fonts, draw shapes, clipRect/clip-out, clipPath,
  blend modes, gradient surfaces, stroked gradients, shader descriptors, RuntimeEffect rows, and graphics-layer
  variants. All rows passed with `fallback_new_count=0`; the suite TSV has 74 lines including the header. Key rows:
  `parity-button-chrome` `jbr_command_frames=2020`, `parity-native-resource-font-text` `jbr_command_frames=968`,
  `parity-native-system-font-text` `jbr_command_frames=438`, `parity-draw-shapes` `jbr_command_frames=1575`,
  `parity-clip-path` `jbr_command_frames=786`, `parity-blend-modes` `jbr_command_frames=847`, and
  `parity-gradient-surfaces` `jbr_command_frames=978`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-211425/suite.tsv`.
- Focused `parity-gradient-surfaces` screenshot parity after adding a dedicated linear/radial/sweep gradient
  rect/round-rect row to the default parity suite. The row stayed on command replay with `fallback_new_count=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=1119`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-211342/suite.tsv`.
- Full default screenshot parity suite after adding the dedicated `parity-blend-modes` row. It covered 72 rows,
  including button chrome, point dots, embedded resource fonts, system fonts, draw shapes, clipRect/clip-out, clipPath,
  blend modes, stroked gradients, shader descriptors, RuntimeEffect rows, and graphics-layer variants. All rows passed
  with `fallback_new_count=0`; command rows reported `jbr_picture_frames=0` and nonzero `jbr_command_frames`. Key rows:
  `parity-button-chrome` `jbr_command_frames=1506`, `parity-native-resource-font-text` `jbr_command_frames=514`,
  `parity-native-system-font-text` `jbr_command_frames=733`, `parity-draw-shapes` `jbr_command_frames=1431`,
  `parity-clip-path` `jbr_command_frames=432`, and `parity-blend-modes` `jbr_command_frames=869`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-202700/suite.tsv`.
- Full default screenshot parity suite after adding the dedicated `parity-draw-shapes` row. It covered 71 rows,
  including button chrome, point dots, embedded resource fonts, system fonts, drawPath/drawArc/drawRoundRect shapes,
  clipRect/clip-out, clipPath, stroked gradients, shader descriptors, RuntimeEffect rows, and graphics-layer variants.
  All rows passed with `fallback_new_count=0`; command rows reported `jbr_picture_frames=0` and nonzero
  `jbr_command_frames`. Key rows: `parity-button-chrome` `jbr_command_frames=1507`,
  `parity-native-resource-font-text` `jbr_command_frames=794`, `parity-native-system-font-text`
  `jbr_command_frames=820`, `parity-draw-shapes` `jbr_command_frames=1379`, `parity-clip-rects`
  `jbr_command_frames=1022`, and `parity-clip-path` `jbr_command_frames=996`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-193855/suite.tsv`.
- Full default screenshot parity suite after adding the dedicated `parity-clip-rects` clipRect/clip-out row. It covered
  70 rows, including button chrome, point dots, embedded resource fonts, system fonts, clipRect/clip-out, clipPath,
  stroked gradients, shader descriptors, RuntimeEffect rows, and graphics-layer variants. All rows passed with
  `fallback_new_count=0`; command rows reported `jbr_picture_frames=0` and nonzero `jbr_command_frames`. Key rows:
  `parity-button-chrome` `jbr_command_frames=934`, `parity-native-resource-font-text` `jbr_command_frames=762`,
  `parity-native-system-font-text` `jbr_command_frames=1012`, `parity-point-dots` `jbr_command_frames=979`,
  `parity-clip-rects` `jbr_command_frames=994`, `parity-clip-path` `jbr_command_frames=632`, and
  `parity-gradient-stroke` `jbr_command_frames=1020`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-185228/suite.tsv`.
- Short artifact matrix after adding the per-row non-focusable window guard to artifact validation. The required
  `current-all` and `missing-public-api` rows passed with `background_window=true`; optional old-artifact rows were
  skipped because no old bundle paths were supplied:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260506-165134/matrix.tsv`.
- Short compatibility matrix after adding the per-row non-focusable window guard. The matrix TSV now includes a
  `background_window` column, and every row in this run reported `true` while preserving the existing happy-path and
  fallback expectations:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260506-163829/matrix.tsv`.
- Full default command-probe sweep after adding RuntimeEffect compile-failure fallback coverage. It covered the current
  ABI 106 replay surface, font/resource rows, shader/effect descriptors, graphics layers, saveLayer, and fallback
  sentinels. `commands-runtime-effect-compile-fallback` passed with `fallback_new_count=1`,
  `jbr_runtime_effect_compile_failures=1`, and `jbr_command_frames=0`; supported command rows stayed on JBR replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-150452/suite.tsv`.
- Full screenshot parity suite after tightening deterministic shader/effect descriptor rows to require JBR handle
  cache-hit markers. It covered 67 rows with `fallback_new_count=0`, command replay rows kept
  `jbr_picture_frames=0`, `parity-color-shader` reported `jbr_shader_handle_cache_hit_frames=1094`, and
  `parity-runtime-effect-color-filter` reported `jbr_effect_handle_cache_hit_frames=2072`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-140238/suite.tsv`.
- Full screenshot parity suite after adding supported saveLayer tint-filter parity coverage. It covered 67 rows,
  including `parity-button-chrome`, point dots, embedded resource fonts, system fonts, saveLayer tint filters,
  shader descriptors, RuntimeEffect rows, and graphics-layer variants. All rows passed with `fallback_new_count=0`;
  command rows reported `jbr_picture_frames=0` and nonzero `jbr_command_frames`. The button-chrome row retained the
  Pulse primary-button guard with `primaryButtonWhiteText=405`, `primaryButtonDarkText=0`, and
  `header_buttons_bad_pixel_ratio=0.00381`; the new `parity-save-layer-filter` row replayed through commands with
  `jbr_command_frames=516`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-130350/suite.tsv`.
- Full default command-probe sweep after adding the saveLayer raw color-filter fallback sentinel. The new
  `commands-save-layer-raw-color-filter-fallback` default row passed with
  `unsupportedScope:1054,saveLayer:1054,graphicsLayer:childCommands:1054,graphicsLayer:1054`,
  `jbr_picture_frames=1055`, and `jbr_command_frames=0`; supported command rows and existing fallback sentinels
  remained green:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-113037/suite.tsv`.
- Full screenshot parity suite on rebuilt ABI 106 artifacts after descriptor-eviction hardening. It covered 66 rows,
  including `parity-button-chrome`, point dots, embedded resource fonts, system fonts, shader descriptors,
  RuntimeEffect rows, and graphics-layer variants. All rows passed with `fallback_new_count=0`; command rows reported
  `jbr_picture_frames=0` and nonzero `jbr_command_frames`. The button-chrome row kept the Pulse primary-button guard
  at `primaryButtonWhiteText=405`, `primaryButtonDarkText=0`, `jbr_command_frames=752`, and
  `header_buttons_bad_pixel_ratio=0.00381`; the embedded resource-font and system-font rows passed with
  `jbr_command_frames=633` and `jbr_command_frames=806` respectively:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-103937/suite.tsv`.
- Compatibility matrix on the rebuilt ABI 106 artifacts after descriptor-eviction hardening; happy path replayed
  commands and every mismatch/removal/public-API row produced the expected structured fallback with no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260506-102434/matrix.tsv`.
- Local artifact matrix after hardening the rebuild script for clean `/tmp` recovery. `current-all` passed with
  `fallback_new_count=0` and `jbr_command_frames=616`; `missing-public-api` passed with one
  `public-api-missing` fallback and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260506-102206/matrix.tsv`.
- Full default command-probe sweep after tightening `commands-descriptor-eviction` to require JBR effect/shader handle
  use markers as well as definitions and evictions:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-085720/suite.tsv`.
- Full default command-probe sweep after adding the image draw path-effect fallback sentinel; the new
  `commands-image-path-effect-fallback` row passed with `pathEffect` unsupported, JBR picture fallback, and no command
  frames while the existing shader/effect/font/layer command rows still replayed through JBR commands:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-212817/suite.tsv`.
- Full default command-probe sweep after adding the path-effect/color-filter fallback sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-184958/suite.tsv`.
- Full default command-probe sweep after descriptor-stroke shader fallback hardening, including the new
  `commands-descriptor-stroke-shader-fallback` row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-164215/suite.tsv`.
- Full screenshot parity suite with tightened descriptor handle gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-153439/suite.tsv`.
- Current ABI 106 local artifact matrix, validating the rebuilt JBR/API/native/Skiko/CMP bundle and missing-public-API
  fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260505-161806/matrix.tsv`.
- Focused path-effect command-row rename/alias validation:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-162211/suite.tsv`.
- Focused screenshot parity handle-gate hardening for shader/effect descriptors and graphics-layer effect rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-151737/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-152630/suite.tsv`.
- Full default command-probe sweep after ABI 106 `Canvas.drawVertices` replay, including `commands-vertices`, font,
  shader/effect descriptor, graphics-layer, and intentional fallback-sentinel rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-121247/suite.tsv`.
- Compatibility matrix after ABI 106 high-word mask update, including the missing draw-vertices capability row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260505-133924/matrix.tsv`.
- Focused ABI 106 `Canvas.drawVertices` command replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-120216/suite.tsv`.
- Focused ABI 106 `Canvas.drawVertices` screenshot parity:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-120655/suite.tsv`.
- Focused missing draw-vertices high-capability fallback:
  `/tmp/jbr-skia-vertices-cap-missing/summary.properties`.
- Full screenshot parity suite after adding skew replay and vertices fallback rows, covering 65 parity rows including
  button chrome, point dots, resource/system fonts, shader descriptors, RuntimeEffect rows, and graphics-layer variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-110425/suite.tsv`.
- Full default command-probe sweep after adding skew replay and vertices fallback rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-093702/suite.tsv`.
- Full screenshot parity suite after stable RuntimeEffect color-filter coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-082130/suite.tsv`.
- Full default command-probe sweep after adding stable RuntimeEffect color-filter coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-221229/suite.tsv`.
- Compatibility matrix after shader cache/effect gate hardening:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260504-182117/matrix.tsv`.
- Current artifact bundle self-check with all optional rows expected to replay commands:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260504-193518/matrix.tsv`.
- Artifact matrix for current local artifacts and missing-public-API fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260504-193131/matrix.tsv`.

## Focused Recent Rows

- Focused `parity-blend-modes` screenshot parity passed after adding a dedicated blend-mode grid row to the default
  parity suite. The row stayed on command replay with `fallback_new_count=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=931`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-202616/suite.tsv`.
- Focused `parity-draw-shapes` screenshot parity passed after adding a dedicated drawPath/drawArc/drawRoundRect row to
  the default parity suite. The row stayed on command replay with `fallback_new_count=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=657`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-193802/suite.tsv`.
- Focused `parity-clip-rects` screenshot parity passed after adding a dedicated clipRect/clip-out row to the default
  parity suite. The row stayed on command replay with `fallback_new_count=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=1043`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-185024/suite.tsv`.
- Full default screenshot parity suite passed after adding `parity-clip-path`, covering 69 rows with button chrome,
  point dots, embedded resource fonts, system fonts, clipPath, shader descriptors, RuntimeEffect rows, stroked
  gradients, and graphics-layer variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-180342/suite.tsv`.
- In that full suite, `parity-clip-path` passed with `fallback_new_count=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=979`; `parity-button-chrome`, `parity-native-resource-font-text`, and
  `parity-native-system-font-text` also stayed on command replay.
- Focused `parity-clip-path` screenshot parity passed after adding a dedicated clipPath row to the default parity suite.
  The row stayed on command replay with `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=399`, and
  `probeRightCyan=3689` against the row's focused 3000-pixel clipPath threshold:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-180124/suite.tsv`.
- Focused `parity-button-chrome` screenshot parity passed after making the Magic Jewel primary button label explicit
  and centered. The row stayed on command replay with `fallback_new_count=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=753`, `primaryButtonWhiteText=405`, `primaryButtonDarkText=0`, and
  `screenshot_parity_region_headerButtons_badPixelRatio=0.00381`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-174825/suite.tsv`.
- Full screenshot parity suite passed after adding the stroked linear-gradient rect row to the default set, covering 68
  rows with button chrome, point dots, embedded resource fonts, system fonts, shader descriptors, RuntimeEffect rows,
  stroked gradients, and graphics-layer variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-170019/suite.tsv`.
- In that full suite, `parity-gradient-stroke` stayed on command replay with `fallback_new_count=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=1120`, and `compose_bad_pixel_ratio=0.07502`.
- Focused `parity-gradient-stroke` screenshot parity passed after adding the stroked linear-gradient rect row to the
  default parity suite. The row stayed on command replay with `fallback_new_count=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=753`, and `compose_bad_pixel_ratio=0.07502`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-165828/suite.tsv`.
- Magic Jewel report-validation unit tests passed after adding the machine-readable
  `magic_jewel_background_window=true` summary guard. This pins the non-focus-stealing default used by command, matrix,
  and parity automation:
  `./scripts/test-jbr-skia-report-validation.sh` in `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel`.
- Focused RuntimeEffect compile-failure fallback sentinel. Skiko's new test-only source corruption hook changed one
  RuntimeEffect SKSL source after recording and recomputed the descriptor source hash, forcing JBR native compile
  failure rather than schema rejection. `commands-runtime-effect-compile-fallback` passed with `fallback_new_count=1`,
  `skiko_command_frames=928`, `jbr_runtime_effect_compile_failures=1`, and `jbr_command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-150134/suite.tsv`.
- Magic Jewel report validation tests passed after switching RuntimeEffect compile/build failure summary counts to full
  logs so early failure markers are not hidden by sampled-log summaries:
  `./scripts/test-jbr-skia-report-validation.sh` in `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel`.
- Focused screenshot parity subset after tightening deterministic shader/effect descriptor rows to require JBR handle
  cache-hit markers in addition to define/use markers. The subset covered `parity-color-shader`,
  `parity-noise-shader`, `parity-turbulence-shader`, `parity-transformed-shader`,
  `parity-runtime-effect-uniform-only`, `parity-runtime-effect-shader`,
  `parity-runtime-effect-shader-color-filter`, `parity-runtime-effect-color-filter`, and
  `parity-runtime-effect-color-filter-child`; all rows passed with `fallback_new_count=0` and
  `jbr_picture_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-135456/suite.tsv`.
- Focused `parity-save-layer-filter` screenshot parity for the supported saveLayer tint-filter command replay path.
  The row passed with `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=341`,
  `avg_delta=2.131`, and `compose_bad_pixel_ratio=0.07517`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-130209/suite.tsv`.
- Focused saveLayer raw color-filter fallback sentinel. CMP's recorder test
  `JbrSkiaCommandRecorderTest.saveLayerRejectsRawColorFilter` passed, then Magic Jewel's paired
  `commands-save-layer-filter commands-save-layer-raw-color-filter-fallback` subset passed. The supported tint row
  replayed with `jbr_command_frames=609`, while the raw color-filter row reported
  `unsupportedScope:366,saveLayer:366,graphicsLayer:childCommands:366,graphicsLayer:366`,
  `jbr_picture_frames=366`, and `jbr_command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-112750/suite.tsv`.
- Focused descriptor eviction lifecycle probe after tightening Magic Jewel to require JBR effect/shader handle uses as
  well as definitions and evictions. The row passed with `jbr_command_frames=47`,
  `jbr_effect_handle_define_frames=92859`, `jbr_effect_handle_use_frames=92859`,
  `jbr_effect_handle_evict_frames=91836`, `jbr_shader_handle_define_frames=275544`,
  `jbr_shader_handle_use_frames=91848`, and `jbr_shader_handle_evict_frames=274520`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-085227/suite.tsv`.
- Focused image draw plus unsupported path-effect fallback after CMP began reporting concrete unsupported image-paint
  reasons instead of only the outer generic `image` marker:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-212515/suite.tsv`.
- CMP focused image recorder tests passed for unsupported image path-effect strict fallback plus existing image ARGB and
  tint color-filter replay:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.rejectsImagePathEffectInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesImageArgbRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesImageTintColorFilterRecord`
  in `/Users/rock3r/src/jbr-skia-zero-copy/cmp`.
- Focused path-effect descriptor plus unsupported color-filter fallback, proving descriptor path effects do not emit
  partial command frames when paint color filters are outside the supported path-effect replay surface:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-184635/suite.tsv`.
- CMP focused recorder tests for the same path-effect/color-filter strict fallback and the existing corner path-effect
  descriptor replay passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.rejectsPathEffectDescriptorColorFilterInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesCornerPathEffectDescriptorPathRecord`
  in `/Users/rock3r/src/jbr-skia-zero-copy/cmp`.
- CMP full recorder regression class after descriptor/image shader stroke fallback hardening and test-isolation cleanup:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
  in `/Users/rock3r/src/jbr-skia-zero-copy/cmp` passed on 2026-05-05.
- Focused descriptor-stroke shader fallback, proving unsupported descriptor-paint style produces structured
  `paintStyle` picture fallback instead of an incomplete command frame:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-163839/suite.tsv`.
- Focused descriptor/image-shader stroke fallback after broadening the Magic Jewel row to cover both recorder helpers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-181114/suite.tsv`.
- Focused button chrome screenshot parity, covering Pulse primary-button white text and centering with
  `primaryButtonWhiteText=405`, `primaryButtonDarkText=0`, `jbr_command_frames=666`, and
  `screenshot_parity_region_headerButtons_badPixelRatio=0.00381`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-163010/suite.tsv`.
- Focused `Canvas.drawVertices` structured fallback sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-093457/suite.tsv`.
- Focused `Canvas.skew` command replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-091628/suite.tsv`.
- Focused `Canvas.skew` screenshot parity:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-092511/suite.tsv`.
- Focused stable RuntimeEffect color-filter screenshot parity:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-081922/suite.tsv`.
- Focused stable RuntimeEffect color-filter handle reuse probe:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-221036/suite.tsv`.
- Focused descriptor handle-use gate subset for RuntimeEffect uniform-only and resize/forced-context descriptor redefine
  rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-212538/suite.tsv`.
- Focused ShaderBrush gradient command replay probe:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-200226/suite.tsv`.
- Button chrome screenshot parity, covering Pulse primary-button white text and centering:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260504-141220/suite.tsv`.
- Embedded resource-font and system-font screenshot parity subset:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260504-141435/suite.tsv`.

## Earlier Current-Cycle Sweeps

- Full default command-probe sweep after adding ShaderBrush gradient coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-200434/suite.tsv`.
- Full screenshot parity suite after command/matrix validation:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260504-185055/suite.tsv`.
- Full default command-probe sweep after shader cache-hit gate hardening:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-161650/suite.tsv`.
- Compact shader/effect regression subset with solid color/transformed shader cache-hit gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-160144/suite.tsv`.
- Solid color and transformed shader cache-hit gate subset:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-155354/suite.tsv`.
- Full default command-probe sweep after effect gate tightening:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-143313/suite.tsv`.
- Compact shader/effect gate regression subset:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-142858/suite.tsv`.
- Graphics-layer color-matrix and blend+color-matrix effect-handle gate subset:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-142640/suite.tsv`.
- RuntimeEffect shader+color-filter and image color-matrix effect-handle gate subset:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-142258/suite.tsv`.
- Compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260504-134603/matrix.tsv`.
- Full default Magic Jewel command-probe sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-124837/suite.tsv`.
