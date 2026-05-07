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

- Full default screenshot parity suite passed after adding graphics-layer color-matrix resize and forced-context
  lifecycle parity rows. The sweep covered 77 rows plus header; every row passed, every row stayed on command replay,
  and no rows reported JBR picture fallback or structural fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-180029/suite.tsv`.
- Focused screenshot parity for graphics-layer color-matrix descriptor lifecycle passed across same-context resize and
  forced destination-context rows. Both rows stayed on command replay, required surface/cache/effect-handle markers, and
  matched old SwingGraphics within the configured parity gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-175623/suite.tsv`.
- Full default command-probe sweep passed after adding graphics-layer color-matrix resize and forced-context lifecycle
  rows. The sweep covered 131 rows plus header; 105 rows reported command replay, and fallback sentinels remained
  isolated:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-163006/suite.tsv`.
- Focused command probe for graphics-layer color-matrix descriptor lifecycle passed after adding resize and forced
  destination-context rows. Both rows stayed on command replay and required surface-change, command-cache-clear,
  effect-handle redefinition, use, and cache-hit markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-162709/suite.tsv`.
- Focused `parity-button-chrome` screenshot parity passed on current artifacts after the full command sweep. It kept the
  new renderer on command replay and rechecked the Pulse button white-text/centering guard:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-162055/suite.tsv`.
- Full default command-probe sweep passed after adding the graphics-layer raw color-filter fallback sentinel. The sweep
  covered 129 rows plus header; supported rows stayed on command replay, intentional picture-fallback sentinels stayed
  isolated, and expected structural fallback rows remained bounded:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-151511/suite.tsv`.
- Focused command probe for the new graphics-layer raw color-filter fallback sentinel passed. The row reported
  `graphicsLayer:colorFilter` unsupported, JBR picture replay, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-150427/suite.tsv`.
- Current local artifact matrix passed on rebuilt ABI 106 artifacts. `current-all` replayed commands
  (`jbr_command_frames=654`), `missing-public-api` fell back structurally, and both rows kept `background_window=true`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260507-145018/matrix.tsv`.
- Rebuilt local artifacts with `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/scripts/rebuild-jbr-skia-local-artifacts.sh`,
  then compiled and ran `test/jdk/jb/JBRSkia/JBRSkiaApiTest.java` against the patched classes and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib` using headless mode and explicit module patches. The run exited 0.
- Full Skiko `JbrSkiaInteropTest` class passed after adding per-bit low-word capability rejection:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  in `/Users/rock3r/src/jbr-skia-zero-copy/skiko/skiko`.
- Full compatibility matrix covers the complete current exact low-word row set and passed across 57 rows plus header:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260507-131028/matrix.tsv`.
- Full default command-probe sweep covered 128 rows plus header; supported rows stayed on command replay and intentional
  fallback sentinels stayed structurally isolated:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-102836/suite.tsv`.
- Full default screenshot parity suite covered 77 rows including button chrome, point dots, embedded resource fonts,
  system fonts, gradient families, shader descriptors, RuntimeEffect rows, and graphics-layer variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-180029/suite.tsv`.
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
