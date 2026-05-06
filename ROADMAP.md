# JBR Skia Compose Zero-Copy Roadmap

This is the small working roadmap for the current PoC. The full historical checklist was archived to
[`docs/history/ROADMAP.full.md`](docs/history/ROADMAP.full.md) to keep future agent context small.

## Current State

- Current negotiated stream ABI: 106.
- Current native ABI: 3.
- The fast path is macOS-first: `ComposePanel(RenderSettings.SwingGraphics)` records Compose drawing into a strict
  command stream that Skiko submits to JBR for replay into a JBR-owned Skia surface during Swing painting.
- Strict ABI/capability/public-API gating is mandatory. Any mismatch must fall back to old SwingGraphics behavior.
- Raw Skiko-owned pointers must not cross the ABI. Known shader/effect/font/image families use serialized descriptors
  or JBR-owned handles; unknown/raw families fall back structurally.

## Worktrees

- JBR: `/Users/rock3r/src/jbr-skia-zero-copy/jbr`
- JBR API: `/Users/rock3r/src/jbr-skia-zero-copy/jbr-api`
- Skiko: `/Users/rock3r/src/jbr-skia-zero-copy/skiko/skiko`
- CMP: `/Users/rock3r/src/jbr-skia-zero-copy/cmp`
- Magic Jewel: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel`

## Current Priorities

- Continue remaining shader-family hardening and fallback sentinels.
- Continue shader/effect lifecycle coverage: create, use, context-scoped cache hit, compile/build failure, eviction,
  resize, and forced destination context migration.
- Tighten stable effect-handle reuse gates on supported rows that still only assert descriptor definition.
- Continue closing transform/graphics-layer edge gaps as they appear in real recorder ground truth.
- Keep old/new screenshot parity coverage broad enough to catch text/color/placement regressions, including button
  chrome, embedded resource fonts, system fonts, point dots, shader descriptors, RuntimeEffect rows, and graphics layers.
- Keep compatibility matrix coverage current after each ABI/capability-affecting slice.
- Keep branches committed and pushed to the user's GitHub forks at each major step.
- Keep the top-level plan/roadmap compact. Move verbose historical narrative into `docs/history/` or focused
  `docs/current/` ledgers when these files start to crowd agent context.

## Latest Validations

- Full default screenshot parity suite after adding the dedicated clipRect/clip-out row, covering 70 rows including
  button chrome, point dots, embedded resource fonts, system fonts, clipRect/clip-out, clipPath, shader descriptors,
  RuntimeEffect rows, stroked gradients, and graphics-layer variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-185228/suite.tsv`.
- Focused `parity-clip-rects` screenshot parity after adding a dedicated clipRect/clip-out row to the default parity
  suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-185024/suite.tsv`.
- Full default screenshot parity suite after adding the dedicated clipPath row, covering 69 rows including button
  chrome, point dots, embedded resource fonts, system fonts, clipPath, shader descriptors, RuntimeEffect rows, stroked
  gradients, and graphics-layer variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-180342/suite.tsv`.
- Focused `parity-clip-path` screenshot parity after adding a dedicated clipPath row to the default parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-180124/suite.tsv`.
- Focused `parity-button-chrome` screenshot parity after making the Magic Jewel button label explicit, covering the
  Pulse primary-button white text and centering guard:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-174825/suite.tsv`.
- Full screenshot parity suite after adding stroked linear-gradient rect coverage, covering 68 rows including button
  chrome, point dots, embedded resource fonts, system fonts, shader descriptors, RuntimeEffect rows, stroked gradients,
  and graphics-layer variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-170019/suite.tsv`.
- Focused `parity-gradient-stroke` screenshot parity for the stroked linear-gradient rect command path:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-165828/suite.tsv`.
- Short artifact matrix after enforcing required-row `background_window=true` in `matrix.tsv`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260506-165134/matrix.tsv`.
- Short compatibility matrix after enforcing per-row `background_window=true` in `matrix.tsv`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260506-163829/matrix.tsv`.
- Magic Jewel report-validation unit tests after adding the machine-readable `magic_jewel_background_window=true`
  summary guard for non-focus-stealing matrix/probe windows:
  `./scripts/test-jbr-skia-report-validation.sh` in `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel`.
- Full default command-probe sweep after adding RuntimeEffect compile-failure fallback coverage, covering command replay
  rows, font/resource rows, shader/effect descriptors, graphics layers, saveLayer, and fallback sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-150452/suite.tsv`.
- Focused RuntimeEffect compile-failure fallback sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-150134/suite.tsv`.
- Full screenshot parity suite after tightening deterministic shader/effect descriptor rows to require JBR handle
  cache-hit markers, covering 67 rows with the stricter default gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-140238/suite.tsv`.
- Focused screenshot parity subset after tightening deterministic shader/effect descriptor rows to require JBR handle
  cache-hit markers as well as define/use markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-135456/suite.tsv`.
- Full screenshot parity suite after adding supported saveLayer tint-filter parity coverage, covering 67 rows including
  button chrome, point dots, embedded resource fonts, system fonts, saveLayer tint filters, shader descriptors,
  RuntimeEffect rows, and graphics-layer variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-130350/suite.tsv`.
- Focused saveLayer tint-filter screenshot parity:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-130209/suite.tsv`.
- Full default command-probe sweep after adding the saveLayer raw color-filter fallback sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-113037/suite.tsv`.
- Focused saveLayer raw color-filter fallback sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-112750/suite.tsv`.
- CMP focused recorder test for the same raw color-filter `saveLayer` fallback:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.saveLayerRejectsRawColorFilter`
  in `/Users/rock3r/src/jbr-skia-zero-copy/cmp`.
- Full screenshot parity suite on rebuilt ABI 106 artifacts after descriptor-eviction hardening, covering 66 rows
  including button chrome, point dots, embedded resource fonts, system fonts, shader descriptors, RuntimeEffect rows,
  and graphics-layer variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260506-103937/suite.tsv`.
- Compatibility matrix on the rebuilt ABI 106 artifacts after descriptor-eviction hardening:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260506-102434/matrix.tsv`.
- Local artifact matrix after clean `/tmp` rebuild-script hardening:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260506-102206/matrix.tsv`.
- Full default command-probe sweep after tightening descriptor eviction handle-use gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-085720/suite.tsv`.
- Focused descriptor eviction lifecycle probe after tightening effect/shader handle use gates and rebuilding clean
  `/tmp` artifacts:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260506-085227/suite.tsv`.
- Full default command-probe sweep after adding the image draw path-effect fallback sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-212817/suite.tsv`.
- Focused image draw plus unsupported path-effect fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-212515/suite.tsv`.
- CMP focused image recorder tests for concrete unsupported image-paint reasons:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.rejectsImagePathEffectInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesImageArgbRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesImageTintColorFilterRecord`
  in `/Users/rock3r/src/jbr-skia-zero-copy/cmp`.
- Full default command-probe sweep after adding the path-effect/color-filter fallback sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-184958/suite.tsv`.
- Focused path-effect descriptor plus unsupported color-filter fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-184635/suite.tsv`.
- CMP focused recorder tests for path-effect/color-filter strict fallback and corner path-effect descriptor replay:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.rejectsPathEffectDescriptorColorFilterInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesCornerPathEffectDescriptorPathRecord`
  in `/Users/rock3r/src/jbr-skia-zero-copy/cmp`.
- CMP full `JbrSkiaCommandRecorderTest` class passed after descriptor/image shader stroke fallback hardening and
  recorder-test isolation cleanup:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
  in `/Users/rock3r/src/jbr-skia-zero-copy/cmp`.
- Full default command-probe sweep after descriptor-stroke shader fallback hardening:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-164215/suite.tsv`.
- Focused descriptor/image-shader stroke fallback after broadening the Magic Jewel row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-181114/suite.tsv`.
- Focused descriptor-stroke shader fallback, covering unsupported descriptor shader paint style:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-163839/suite.tsv`.
- Focused button chrome screenshot parity re-check, covering Pulse primary-button white text and centering:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-163010/suite.tsv`.
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
- Full default command-probe sweep after ABI 106 `Canvas.drawVertices` replay, including shader/effect descriptor,
  text/font, graphics-layer, fallback-sentinel, and vertices rows:
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
- Full screenshot parity suite after stable RuntimeEffect color-filter coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-082130/suite.tsv`.
- Focused `Canvas.skew` command replay and screenshot parity:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-091628/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260505-092511/suite.tsv`.
- Focused `Canvas.drawVertices` structured fallback sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-093457/suite.tsv`.
- Full default command-probe sweep after adding skew replay and vertices fallback rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260505-093702/suite.tsv`.
- Full default command-probe sweep after adding stable RuntimeEffect color-filter coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260504-221229/suite.tsv`.
- Current compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260504-182117/matrix.tsv`.
- Current artifact bundle self-check with all optional rows expected to replay commands:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260504-193518/matrix.tsv`.
- Rolling validation ledger: [`docs/current/VALIDATION_LOG.md`](docs/current/VALIDATION_LOG.md).

## Document Index

- Current plan/checkpoint index: [`SKIA_COMPOSE_ZERO_COPY_POC_PLAN.md`](SKIA_COMPOSE_ZERO_COPY_POC_PLAN.md)
- Documentation index: [`docs/INDEX.md`](docs/INDEX.md)
- Current validation log: [`docs/current/VALIDATION_LOG.md`](docs/current/VALIDATION_LOG.md)
- Full archived roadmap: [`docs/history/ROADMAP.full.md`](docs/history/ROADMAP.full.md)
- Full archived checkpoint log: [`docs/history/SKIA_COMPOSE_ZERO_COPY_POC_PLAN.full.md`](docs/history/SKIA_COMPOSE_ZERO_COPY_POC_PLAN.full.md)
- Shader/effect design doc: [`doc/skia-shader-factory.md`](doc/skia-shader-factory.md)

## Operating Rules

- Read this file first, then the small plan index.
- Load archived history only when investigating older decisions or exact validation paths.
- Update this roadmap and the plan index after every major validation or implementation slice.
