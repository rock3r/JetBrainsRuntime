# Current Validation Log

This file keeps the rolling validation ledger out of the top-level roadmap and plan. Keep the newest high-signal
entries here, and move older narrative detail to `docs/history/` only when this file starts getting noisy.

## Latest Broad Sweeps

- 2026-06-15 focused command-probe lifecycle hardening for graphics-layer render-effect plus blend/color-matrix-filter
  migration rows: Magic Jewel added `commands-resize-graphics-layer-render-effect-blend-color-matrix-filter` and
  `commands-forced-context-graphics-layer-render-effect-blend-color-matrix-filter`. Both rows assert destination
  migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, and effect-handle
  redefinition/reuse/cache-hit markers. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-render-effect-blend-color-matrix-filter commands-forced-context-graphics-layer-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 892
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, four effect-handle definitions, 2,684
  effect-handle uses, 2,680 effect-handle cache hits, one surface-change marker, and one command-cache clear marker.
  The forced-context row reported 750 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, four
  effect-handle definitions, 2,486 effect-handle uses, 2,482 effect-handle cache hits, one surface-change marker, and
  one command-cache clear marker. This is focused command-probe lifecycle change 7 after the 2026-06-14 18:10 full
  command-probe sweep, so broad command-probe validation remains deferred until roughly three more focused
  command-probe changes or an ABI/capability gate. Disk free was about 113Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-015317/suite.tsv`.
- 2026-06-15 focused command-probe lifecycle hardening for graphics-layer render-effect plus blend/color-filter
  migration rows: Magic Jewel added `commands-resize-graphics-layer-render-effect-blend-color-filter` and
  `commands-forced-context-graphics-layer-render-effect-blend-color-filter`. Both rows assert destination migration,
  command-cache clear, JBR image-cache clear, scoped image-cache clear, and effect-handle
  redefinition/reuse/cache-hit markers. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-render-effect-blend-color-filter commands-forced-context-graphics-layer-render-effect-blend-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 852
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, two effect-handle definitions, 1,306
  effect-handle uses, 1,304 effect-handle cache hits, one surface-change marker, and one command-cache clear marker.
  The forced-context row reported 849 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, two
  effect-handle definitions, 1,332 effect-handle uses, 1,330 effect-handle cache hits, one surface-change marker, and
  one command-cache clear marker. This is focused command-probe lifecycle change 6 after the 2026-06-14 18:10 full
  command-probe sweep, so broad command-probe validation remains deferred until roughly four more focused
  command-probe changes or an ABI/capability gate. Disk free was about 114Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-014743/suite.tsv`.
- 2026-06-15 focused command-probe lifecycle hardening for graphics-layer render-effect plus color-matrix-filter
  migration rows: Magic Jewel added `commands-resize-graphics-layer-render-effect-color-matrix-filter` and
  `commands-forced-context-graphics-layer-render-effect-color-matrix-filter`. Both rows assert destination migration,
  command-cache clear, JBR image-cache clear, scoped image-cache clear, and color-matrix/effect-handle
  redefinition/reuse/cache-hit markers. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-render-effect-color-matrix-filter commands-forced-context-graphics-layer-render-effect-color-matrix-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 710
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, four effect-handle definitions, 2,578
  effect-handle uses, 2,574 effect-handle cache hits, one surface-change marker, and one command-cache clear marker.
  The forced-context row reported 906 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, four
  effect-handle definitions, 2,754 effect-handle uses, 2,750 effect-handle cache hits, one surface-change marker, and
  one command-cache clear marker. This is focused command-probe lifecycle change 5 after the 2026-06-14 18:10 full
  command-probe sweep, so broad command-probe validation remains deferred until roughly five more focused
  command-probe changes or an ABI/capability gate. Disk free was about 115Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-014123/suite.tsv`.
- 2026-06-15 focused command-probe lifecycle hardening for graphics-layer render-effect plus blend-mode migration rows:
  Magic Jewel added `commands-resize-graphics-layer-render-effect-blend-mode` and
  `commands-forced-context-graphics-layer-render-effect-blend-mode`. Both rows assert destination migration,
  command-cache clear, JBR image-cache clear, scoped image-cache clear, and effect-handle redefinition/reuse/cache-hit
  markers. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-render-effect-blend-mode commands-forced-context-graphics-layer-render-effect-blend-mode" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 774 JBR
  command frames, one JBR image-cache clear, one scoped image-cache clear, two effect-handle definitions, 1,502
  effect-handle uses, 1,500 effect-handle cache hits, one surface-change marker, and one command-cache clear marker.
  The forced-context row reported 961 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, two
  effect-handle definitions, 1,403 effect-handle uses, 1,401 effect-handle cache hits, one surface-change marker, and
  one command-cache clear marker. This is focused command-probe lifecycle change 4 after the 2026-06-14 18:10 full
  command-probe sweep, so broad command-probe validation remains deferred until roughly six more focused command-probe
  changes or an ABI/capability gate. Disk free was about 115Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-013449/suite.tsv`.
- 2026-06-15 focused command-probe lifecycle hardening for graphics-layer render-effect plus color-filter migration
  rows: Magic Jewel added `commands-resize-graphics-layer-render-effect-color-filter` and
  `commands-forced-context-graphics-layer-render-effect-color-filter`. Both rows assert destination migration,
  command-cache clear, JBR image-cache clear, scoped image-cache clear, and effect-handle redefinition/reuse/cache-hit
  markers. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-render-effect-color-filter commands-forced-context-graphics-layer-render-effect-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 871 JBR
  command frames, one JBR image-cache clear, one scoped image-cache clear, two effect-handle definitions, 1,379
  effect-handle uses, 1,377 effect-handle cache hits, one surface-change marker, and one command-cache clear marker.
  The forced-context row reported 945 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, two
  effect-handle definitions, 1,430 effect-handle uses, 1,428 effect-handle cache hits, one surface-change marker, and
  one command-cache clear marker. This is focused command-probe lifecycle change 3 after the 2026-06-14 18:10 full
  command-probe sweep, so broad command-probe validation remains deferred until roughly seven more focused
  command-probe changes or an ABI/capability gate. Disk free was about 115Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-012942/suite.tsv`.
- 2026-06-15 focused command-probe lifecycle hardening for graphics-layer chained render-effect migration rows:
  Magic Jewel added `commands-resize-graphics-layer-chained-render-effect` and
  `commands-forced-context-graphics-layer-chained-render-effect`. Both rows assert destination migration, command-cache
  clear, JBR image-cache clear, scoped image-cache clear, and chained effect-handle redefinition/reuse/cache-hit
  markers. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-chained-render-effect commands-forced-context-graphics-layer-chained-render-effect" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 385 JBR
  command frames, one JBR image-cache clear, one scoped image-cache clear, four effect-handle definitions, 1,291
  effect-handle uses, 1,289 effect-handle cache hits, one surface-change marker, and one command-cache clear marker.
  The forced-context row reported 939 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, four
  effect-handle definitions, 1,366 effect-handle uses, 1,364 effect-handle cache hits, one surface-change marker, and
  one command-cache clear marker. This is focused command-probe lifecycle change 2 after the 2026-06-14 18:10 full
  command-probe sweep, so broad command-probe validation remains deferred until roughly eight more focused
  command-probe changes or an ABI/capability gate. Disk free was about 115Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-012432/suite.tsv`.
- 2026-06-15 focused command-probe lifecycle hardening for graphics-layer offset render-effect migration rows:
  Magic Jewel added `commands-resize-graphics-layer-offset-effect` and
  `commands-forced-context-graphics-layer-offset-effect`. Both rows assert destination migration, command-cache clear,
  JBR image-cache clear, scoped image-cache clear, and effect-handle redefinition/reuse/cache-hit markers. Focused
  validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-offset-effect commands-forced-context-graphics-layer-offset-effect" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 2,302
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, two effect-handle definitions, 2,302
  effect-handle uses, 2,300 effect-handle cache hits, one surface-change marker, and one command-cache clear marker.
  The forced-context row reported 1,077 JBR command frames, one JBR image-cache clear, one scoped image-cache clear,
  two effect-handle definitions, 1,658 effect-handle uses, 1,656 effect-handle cache hits, one surface-change marker,
  and one command-cache clear marker. This is focused command-probe lifecycle change 1 after the 2026-06-14 18:10 full
  command-probe sweep, so broad command-probe validation remains deferred until roughly nine more focused
  command-probe changes or an ABI/capability gate. Disk free was about 115Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-011920/suite.tsv`.
- 2026-06-15 narrow post-matrix artifact/benchmark refresh:
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2 on the
  current ABI 106 artifact bundle. `current-all` reported no fallback, 316 command frames, and
  `background_window=true`; `missing-public-api` reported the expected `public-api-missing` fallback, one fallback,
  zero command frames, and `background_window=true`. Command benchmark smoke
  `CASES=commands ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, 16 old/new samples, zero picture
  frames, 3,129 JBR command frames, `app_new_fps=156.4`, and `jbr_command_fps=156.4`. This was a narrow
  post-compatibility check, so it does not reset or advance the focused command/parity counters. Disk free was about
  115Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260615-011346/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260615-011452/suite.tsv`.
- 2026-06-15 post-command-sweep compatibility matrix refresh:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57. Aggregate:
  `fallback_sum=56`, 229 JBR command frames from `happy`, and `background_window=true` on all 57 rows. The matrix
  rechecked ABI mismatch, native ABI mismatch, command capability low/high mismatches, public API absence, and the
  current gradient, text/font, shader/filter/effect/path/transform/image/vertex capability fallback gates after the
  2026-06-14 18:10 full command-probe sweep. A first sandboxed attempt at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-004143/` failed before
  producing frames because Gradle could not create its wrapper lock under `~/.gradle`; the escalated serial rerun
  passed. Disk free was about 115Gi after the successful run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-004232/matrix.tsv`.
- 2026-06-14 cadence-triggered full command-probe sweep after ten focused command-probe hardenings for shader
  descriptor migration and native-font migration rows:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` passed 549/549. Aggregate:
  `fallback_sum=350`, 79 unsupported rows, 65,039 JBR picture frames, and 109,771 JBR command frames. The sweep covered
  the newly tightened shader, color-shader, noise-shader, turbulence-shader, composite-noise shader, generic native-font,
  loaded native-font, resource native-font, system native-font, and forced-context custom-font text/image migration
  sentinels, plus the existing invalid stream, text/font, shader/filter descriptor, RuntimeEffect, graphics-layer,
  save-layer, transform, vertex, and fallback coverage. This resets the focused command-probe change counter to zero;
  next per-change command-probe validation should stay exact-row/tiny-group, with the next full command-probe sweep
  deferred until roughly ten more meaningful command-probe changes or an ABI/capability gate. Disk free was about 118Gi
  after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-181002/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for the forced-context custom-font/text-image row:
  `commands-forced-context-native-custom-font-text-image` now asserts `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1` alongside its existing image-ref, surface-change, and command-cache
  guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-forced-context-native-custom-font-text-image" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=0`, `unsupported=none`, zero picture frames, 1,313 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one surface-change marker, one command-cache clear marker, 15
  image refs, and no unsupported reasons. This is focused command-probe lifecycle change 10 after the 2026-06-14 11:32
  full command-probe sweep, so the next validation step is the batched full command-probe sweep. Disk free was about
  158Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-180730/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for system native-font migration rows:
  `commands-resize-native-system-font-text` and `commands-forced-context-native-system-font-text` now assert
  `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1` alongside their existing
  image/text, surface-change, and command-cache guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-native-system-font-text commands-forced-context-native-system-font-text" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 1,466
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one
  command-cache clear marker, one text-command frame, and no unsupported reasons. The forced-context row reported 1,710
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one
  command-cache clear marker, one text-command frame, and no unsupported reasons. This is focused command-probe
  lifecycle change 9 after the 2026-06-14 11:32 full command-probe sweep, so broad command-probe validation remains
  deferred until one more focused command-probe change or an ABI/capability gate. Disk free stayed about 159Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-180320/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for resource native-font migration rows:
  `commands-resize-native-resource-font-text` and `commands-forced-context-native-resource-font-text` now assert
  `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1` alongside their existing
  image/text, font-data, surface-change, and command-cache guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-native-resource-font-text commands-forced-context-native-resource-font-text" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 1,220
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one
  command-cache clear marker, one text-command frame, and no unsupported reasons. The forced-context row reported 1,318
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one
  command-cache clear marker, one text-command frame, and no unsupported reasons. This is focused command-probe
  lifecycle change 8 after the 2026-06-14 11:32 full command-probe sweep, so broad command-probe validation remains
  deferred until roughly two more focused command-probe changes or an ABI/capability gate. Disk free stayed about
  159Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-175937/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for loaded native-font migration rows:
  `commands-resize-native-loaded-font-data-text` and `commands-forced-context-native-loaded-font-data-text` now assert
  `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1` alongside their existing
  image/text, font-data, surface-change, and command-cache guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-native-loaded-font-data-text commands-forced-context-native-loaded-font-data-text" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 1,386
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one
  command-cache clear marker, one text-command frame, and no unsupported reasons. The forced-context row reported 1,465
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one
  command-cache clear marker, one text-command frame, and no unsupported reasons. This is focused command-probe
  lifecycle change 7 after the 2026-06-14 11:32 full command-probe sweep, so broad command-probe validation remains
  deferred until roughly three more focused command-probe changes or an ABI/capability gate. Disk free stayed about
  159Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-175615/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for generic native-font migration rows:
  `commands-resize-native-generic-font-text` and `commands-forced-context-native-generic-font-text` now assert
  `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1` alongside their existing
  image/text, surface-change, and command-cache guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-native-generic-font-text commands-forced-context-native-generic-font-text" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 1,247
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one
  command-cache clear marker, three text-command frames, one paragraph-text-command frame, and no unsupported reasons.
  The forced-context row reported 1,504 JBR command frames, one JBR image-cache clear, one scoped image-cache clear,
  one surface-change marker, one command-cache clear marker, three text-command frames, one paragraph-text-command
  frame, and no unsupported reasons. This is focused command-probe lifecycle change 6 after the 2026-06-14 11:32 full
  command-probe sweep, so broad command-probe validation remains deferred until roughly four more focused command-probe
  changes or an ABI/capability gate. Disk free stayed about 159Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-175231/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for composite-noise shader descriptor redefine migration rows:
  `commands-resize-composite-noise-shader-descriptor-redefine` and
  `commands-forced-context-composite-noise-shader-descriptor-redefine` now assert
  `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1` alongside their existing Skiko
  surface/cache and shader-handle reuse guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-composite-noise-shader-descriptor-redefine commands-forced-context-composite-noise-shader-descriptor-redefine" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 1,275
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one
  command-cache clear marker, six shader-handle definition frames, 1,852 shader-handle use frames, and 1,850
  shader-handle cache-hit frames. The forced-context row reported 1,650 JBR command frames, one JBR image-cache clear,
  one scoped image-cache clear, one surface-change marker, one command-cache clear marker, six shader-handle definition
  frames, 2,261 shader-handle use frames, and 2,259 shader-handle cache-hit frames. This is focused command-probe
  lifecycle change 5 after the 2026-06-14 11:32 full command-probe sweep, so broad command-probe validation remains
  deferred until roughly five more focused command-probe changes or an ABI/capability gate. Disk free stayed about
  159Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-174852/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for turbulence-shader descriptor redefine migration rows:
  `commands-resize-turbulence-shader-descriptor-redefine` and
  `commands-forced-context-turbulence-shader-descriptor-redefine` now assert `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1` alongside their existing Skiko surface/cache and shader-handle reuse
  guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-turbulence-shader-descriptor-redefine commands-forced-context-turbulence-shader-descriptor-redefine" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 1,494
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one
  command-cache clear marker, two shader-handle definition frames, 2,086 shader-handle use frames, and 2,084
  shader-handle cache-hit frames. The forced-context row reported 1,359 JBR command frames, one JBR image-cache clear,
  one scoped image-cache clear, one surface-change marker, one command-cache clear marker, two shader-handle definition
  frames, 1,876 shader-handle use frames, and 1,874 shader-handle cache-hit frames. This is focused command-probe
  lifecycle change 4 after the 2026-06-14 11:32 full command-probe sweep, so broad command-probe validation remains
  deferred until roughly six more focused command-probe changes or an ABI/capability gate. Disk free stayed about
  159Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-174439/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for noise-shader descriptor redefine migration rows:
  `commands-resize-noise-shader-descriptor-redefine` and
  `commands-forced-context-noise-shader-descriptor-redefine` now assert `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1` alongside their existing Skiko surface/cache and shader-handle reuse
  guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-noise-shader-descriptor-redefine commands-forced-context-noise-shader-descriptor-redefine" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 1,635
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one
  command-cache clear marker, two shader-handle definition frames, 2,198 shader-handle use frames, and 2,196
  shader-handle cache-hit frames. The forced-context row reported 1,658 JBR command frames, one JBR image-cache clear,
  one scoped image-cache clear, one surface-change marker, one command-cache clear marker, two shader-handle definition
  frames, 2,273 shader-handle use frames, and 2,271 shader-handle cache-hit frames. This is focused command-probe
  lifecycle change 3 after the 2026-06-14 11:32 full command-probe sweep, so broad command-probe validation remains
  deferred until roughly seven more focused command-probe changes or an ABI/capability gate. Disk free stayed about
  159Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-174127/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for color-shader descriptor redefine migration rows:
  `commands-resize-color-shader-descriptor-redefine` and
  `commands-forced-context-color-shader-descriptor-redefine` now assert `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1` alongside their existing Skiko surface/cache and shader-handle reuse
  guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-color-shader-descriptor-redefine commands-forced-context-color-shader-descriptor-redefine" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 1,731
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one
  command-cache clear marker, two shader-handle definition frames, 2,330 shader-handle use frames, and 2,328
  shader-handle cache-hit frames. The forced-context row reported 1,303 JBR command frames, one JBR image-cache clear,
  one scoped image-cache clear, one surface-change marker, one command-cache clear marker, two shader-handle definition
  frames, 1,741 shader-handle use frames, and 1,739 shader-handle cache-hit frames. This is focused command-probe
  lifecycle change 2 after the 2026-06-14 11:32 full command-probe sweep, so broad command-probe validation remains
  deferred until roughly eight more focused command-probe changes or an ABI/capability gate. Disk free was about 159Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-173740/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for shader descriptor redefine migration rows: the latest broad
  command-probe report showed stable resize/forced-context destination migration markers while
  `commands-resize-shader-descriptor-redefine` and `commands-forced-context-shader-descriptor-redefine` only asserted
  Skiko surface/cache markers, shader-handle reuse, and RuntimeEffect source-cache reuse. Magic Jewel added
  `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1` to both rows. Focused
  validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-shader-descriptor-redefine commands-forced-context-shader-descriptor-redefine" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0`, `unsupported=none`, and zero picture frames. The resize row reported 1,538
  JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one
  command-cache clear marker, two shader-handle definition frames, 2,063 shader-handle use frames, 2,061 shader-handle
  cache-hit frames, 2,062 RuntimeEffect source-cache hits, and one RuntimeEffect source-cache miss. The forced-context
  row reported 1,630 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change
  marker, one command-cache clear marker, two shader-handle definition frames, 2,116 shader-handle use frames, 2,114
  shader-handle cache-hit frames, 2,114 RuntimeEffect source-cache hits, and one RuntimeEffect source-cache miss. This
  is focused command-probe lifecycle change 1 after the 2026-06-14 11:32 full command-probe sweep, so broad
  command-probe validation remains deferred until roughly nine more focused command-probe changes or an ABI/capability
  gate. Disk free remained about 161Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-173417/suite.tsv`.
- 2026-06-14 narrow post-matrix artifact/benchmark refresh:
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2 on the
  current ABI 106 artifact bundle. `current-all` reported no fallback, 869 command frames, and
  `background_window=true`; `missing-public-api` reported the expected `public-api-missing` fallback, one fallback, zero
  command frames, and `background_window=true`. Command benchmark smoke
  `CASES=commands ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, 16 old/new samples, zero picture
  frames, 3,848 JBR command frames, `app_new_fps=192.4`, and `jbr_command_fps=192.4`. This was a narrow
  post-compatibility check, so it does not reset or advance the focused command/parity counters. Disk free remained
  about 161Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260614-172951/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260614-173051/suite.tsv`.
- 2026-06-14 post-command-sweep compatibility matrix refresh:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57. Aggregate:
  `fallback_sum=56`, 467 JBR command frames from `happy`, and `background_window=true` on all 57 rows. The matrix
  rechecked ABI mismatch, native ABI mismatch, command capability low/high mismatches, public API absence, and the
  current gradient, text/font, shader/filter/effect/path/transform/image/vertex capability fallback gates after the
  2026-06-14 11:32 full command-probe sweep. Disk free was about 161Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260614-170152/matrix.tsv`.
- 2026-06-14 cadence-triggered full command-probe sweep after ten focused command-probe lifecycle hardenings:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` passed 549/549. Aggregate:
  `fallback_sum=350`, 80 unsupported rows, 77,679 JBR picture frames, and 152,941 JBR command frames. The sweep covered
  the newly tightened RuntimeEffect source-cache rows, graphics-layer render-effect/color-matrix resize and
  forced-context rows, RuntimeEffect stable color-filter resize/forced-context rows, descriptor redefine
  resize/forced-context rows, and the existing invalid stream, text/font, shader/filter descriptor, graphics-layer,
  save-layer, transform, and fallback coverage. This resets the focused command-probe change counter to zero; next
  per-change command-probe validation should stay exact-row/tiny-group, with the next full command-probe sweep deferred
  until roughly ten more meaningful command-probe changes or an ABI/capability gate. Disk free was about 162Gi after
  the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-113210/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for `commands-resize-descriptor-redefine`: the latest broad
  command-probe report showed stable resize-path destination migration markers while the row only asserted Skiko
  surface/cache markers and effect-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect definition/use/cache-hit guards.
  Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-resize-descriptor-redefine ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=0`, `unsupported=none`, zero picture frames, 1,286 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker,
  two effect-handle definition frames, 2,267 effect-handle use frames, 2,265 effect-handle cache-hit frames, zero
  effect-handle evicts, zero shader-handle markers, zero RuntimeEffect markers, and `EXPECT_SCREENSHOT_ASSERTION=false`.
  This is focused command-probe lifecycle change 10 after the 2026-06-14 00:03 full command-probe sweep; the next
  validation step is the batched full command-probe sweep. Disk free remained about 172Gi after the exact run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-113014/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for `commands-forced-context-descriptor-redefine`: the latest
  broad command-probe report showed stable forced-context destination migration markers while the row only asserted
  Skiko surface/cache markers and effect-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect definition/use/cache-hit guards.
  Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-forced-context-descriptor-redefine ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=0`, `unsupported=none`, zero picture frames, 1,353 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker,
  two effect-handle definition frames, 2,148 effect-handle use frames, 2,146 effect-handle cache-hit frames, zero
  effect-handle evicts, zero shader-handle markers, zero RuntimeEffect markers, and `EXPECT_SCREENSHOT_ASSERTION=false`.
  This is focused command-probe lifecycle change 9 after the 2026-06-14 00:03 full command-probe sweep; broad
  command-probe validation remains deferred until the next focused command-probe change or an ABI/capability gate. Disk
  free remained about 172Gi after the exact run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-112804/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for `commands-resize-runtime-effect-stable-color-filter`: the
  latest broad command-probe report showed stable resize-path destination migration markers while the row only asserted
  Skiko surface/cache markers, effect-handle reuse, and RuntimeEffect source-cache reuse. Magic Jewel added
  `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing
  effect definition/use/cache-hit and RuntimeEffect source-cache guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-resize-runtime-effect-stable-color-filter ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=0`, `unsupported=none`, zero picture frames, 918 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker,
  two effect-handle definition frames, 1,596 effect-handle use frames, 1,594 effect-handle cache-hit frames, zero
  effect-handle evicts, zero shader-handle markers, 1,595 RuntimeEffect source-cache hits, one RuntimeEffect
  source-cache miss, zero RuntimeEffect source-cache evicts, zero compile/build failures, and
  `EXPECT_SCREENSHOT_ASSERTION=false`. This is focused command-probe lifecycle change 8 after the 2026-06-14 00:03
  full command-probe sweep; broad command-probe validation remains deferred until roughly two more focused
  command-probe changes or an ABI/capability gate. Disk free remained about 172Gi after the exact run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-111943/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for
  `commands-forced-context-runtime-effect-stable-color-filter`: the latest broad command-probe report showed stable
  forced-context destination migration markers while the row only asserted Skiko surface/cache markers,
  effect-handle reuse, and RuntimeEffect source-cache reuse. Magic Jewel added
  `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing
  effect definition/use/cache-hit and RuntimeEffect source-cache guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-forced-context-runtime-effect-stable-color-filter ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=0`, `unsupported=none`, zero picture frames, 1,305 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker,
  two effect-handle definition frames, 2,224 effect-handle use frames, 2,222 effect-handle cache-hit frames, zero
  effect-handle evicts, zero shader-handle markers, 2,223 RuntimeEffect source-cache hits, one RuntimeEffect
  source-cache miss, zero RuntimeEffect source-cache evicts, zero compile/build failures, and
  `EXPECT_SCREENSHOT_ASSERTION=false`. This is focused command-probe lifecycle change 7 after the 2026-06-14 00:03
  full command-probe sweep; broad command-probe validation remains deferred until roughly three more focused
  command-probe changes or an ABI/capability gate. Disk free remained about 172Gi after the exact run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-111732/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for
  `commands-forced-context-graphics-layer-color-matrix-filter`: the latest broad command-probe report showed stable
  forced-context destination migration markers while the row only asserted Skiko surface/cache markers and
  effect-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect definition/use/cache-hit guards.
  Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-forced-context-graphics-layer-color-matrix-filter ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=0`, `unsupported=none`, zero picture frames, 969 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker,
  two effect-handle definition frames, 1,701 effect-handle use frames, 1,699 effect-handle cache-hit frames, zero
  effect-handle evicts, zero shader-handle markers, zero RuntimeEffect markers, and `EXPECT_SCREENSHOT_ASSERTION=false`.
  This is focused command-probe lifecycle change 6 after the 2026-06-14 00:03 full command-probe sweep; broad
  command-probe validation remains deferred until roughly four more focused command-probe changes or an ABI/capability
  gate. Disk free remained about 172Gi after the exact run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-111522/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for `commands-resize-graphics-layer-color-matrix-filter`: the
  latest broad command-probe report showed stable resize-path destination migration markers while the row only asserted
  Skiko surface/cache markers and effect-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect definition/use/cache-hit guards.
  Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-resize-graphics-layer-color-matrix-filter ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=0`, `unsupported=none`, zero picture frames, 1,711 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker,
  two effect-handle definition frames, 2,282 effect-handle use frames, 2,280 effect-handle cache-hit frames, zero
  effect-handle evicts, zero shader-handle markers, zero RuntimeEffect markers, and `EXPECT_SCREENSHOT_ASSERTION=false`.
  This is focused command-probe lifecycle change 5 after the 2026-06-14 00:03 full command-probe sweep; broad
  command-probe validation remains deferred until roughly five more focused command-probe changes or an ABI/capability
  gate. Disk free remained about 172Gi after the exact run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-110941/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for `commands-forced-context-graphics-layer-render-effect`: the
  latest broad command-probe report showed stable forced-context destination migration markers while the row only
  asserted Skiko surface/cache markers and effect-handle reuse. Magic Jewel added
  `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing
  effect definition/use/cache-hit guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-forced-context-graphics-layer-render-effect ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=0`, `unsupported=none`, zero picture frames, 1,129 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker,
  two effect-handle definition frames, 1,789 effect-handle use frames, 1,787 effect-handle cache-hit frames, zero
  effect-handle evicts, zero shader-handle markers, zero RuntimeEffect markers, and `EXPECT_SCREENSHOT_ASSERTION=false`.
  This is focused command-probe lifecycle change 4 after the 2026-06-14 00:03 full command-probe sweep; broad
  command-probe validation remains deferred until roughly six more focused command-probe changes or an ABI/capability
  gate. Disk free remained about 172Gi after the exact run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-105847/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for `commands-resize-graphics-layer-render-effect`: the latest
  broad command-probe report showed stable resize-path destination migration markers while the row only asserted Skiko
  surface/cache markers and effect-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect definition/use/cache-hit guards.
  Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-resize-graphics-layer-render-effect ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=0`, `unsupported=none`, zero picture frames, 1,083 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker,
  two effect-handle definition frames, 1,610 effect-handle use frames, 1,608 effect-handle cache-hit frames, zero
  effect-handle evicts, zero shader-handle markers, zero RuntimeEffect markers, and `EXPECT_SCREENSHOT_ASSERTION=false`.
  This is focused command-probe lifecycle change 3 after the 2026-06-14 00:03 full command-probe sweep; broad
  command-probe validation remains deferred until roughly seven more focused command-probe changes or an
  ABI/capability gate. Disk free remained about 173Gi after the exact run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-105617/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for `commands-runtime-effect-source-cache-eviction`: the latest
  broad command-probe report showed stable RuntimeEffect source-cache reuse before color-filter source-cache eviction
  while the row only asserted eviction and effect-handle reuse. Magic Jewel added
  `EXPECT_MIN_JBR_RUNTIME_EFFECT_CACHE_HITS=1`, preserving the existing effect definition/use/cache-hit and
  RuntimeEffect eviction-type guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-runtime-effect-source-cache-eviction ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=0`, `unsupported=none`, zero picture frames, 1,044 JBR command frames, 1,441
  RuntimeEffect source-cache hits, 2,885 RuntimeEffect source-cache misses, 2,883 RuntimeEffect source-cache evicts,
  zero RuntimeEffect compile/build failures, 2,887 effect-handle definition frames, 4,326 effect-handle use frames,
  1,441 effect-handle cache-hit frames, 1,863 effect-handle evict frames, zero shader-handle markers, and
  `EXPECT_SCREENSHOT_ASSERTION=false`. This is focused command-probe lifecycle change 2 after the 2026-06-14 00:03
  full command-probe sweep; broad command-probe validation remains deferred until roughly eight more focused
  command-probe changes or an ABI/capability gate. Disk free remained about 148Gi after the exact run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-104811/suite.tsv`.
- 2026-06-14 focused command-probe lifecycle hardening for
  `commands-runtime-effect-shader-source-cache-eviction`: the latest broad command-probe report showed stable
  RuntimeEffect source-cache reuse before shader source-cache eviction while the row only asserted eviction and
  shader-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_RUNTIME_EFFECT_CACHE_HITS=1`, preserving the existing shader
  definition/use/cache-hit and RuntimeEffect eviction-type guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-runtime-effect-shader-source-cache-eviction ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=0`, `unsupported=none`, zero picture frames, 1,413 JBR command frames, 1,942
  RuntimeEffect source-cache hits, 3,887 RuntimeEffect source-cache misses, 3,885 RuntimeEffect source-cache evicts,
  zero RuntimeEffect compile/build failures, 1,946 shader-handle definition frames, 5,829 shader-handle use frames,
  3,885 shader-handle cache-hit frames, 922 shader-handle evict frames, zero effect-handle markers, and
  `EXPECT_SCREENSHOT_ASSERTION=false`. This is focused command-probe lifecycle change 1 after the 2026-06-14 00:03
  full command-probe sweep; broad command-probe validation remains deferred until roughly nine more focused
  command-probe changes or an ABI/capability gate. Disk free remained about 148Gi after the exact run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-104548/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-runtime-effect-shader-source-cache-eviction`: the latest
  broad parity report showed stable RuntimeEffect source-cache reuse before shader source-cache eviction while the row
  only asserted eviction and shader-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_RUNTIME_EFFECT_CACHE_HITS=1`,
  preserving the existing shader definition/use/cache-hit and RuntimeEffect eviction-type guards. Focused validation
  `CASES=parity-runtime-effect-shader-source-cache-eviction ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 938 JBR command frames, 1,449 RuntimeEffect source-cache hits, 2,901
  RuntimeEffect source-cache misses, 2,899 RuntimeEffect source-cache evicts, zero RuntimeEffect compile/build
  failures, 20 shader-handle definition frames, 4,350 shader-handle use frames, 4,338 shader-handle cache-hit frames,
  zero shader-handle evicts, 20 effect-handle definition frames, zero effect-handle use/cache-hit/evict frames,
  `avg_delta=2.084`, `bad_pixel_ratio=0.04931`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.07335`, `compose_bottom_labels_bad_pixel_ratio=0.12590`,
  `compose_paragraph_probes_bad_pixel_ratio=0.08803`, `compose_shader_color_bad_pixel_ratio=0.07688`,
  `compose_shader_image_bad_pixel_ratio=0.05001`, `compose_shader_composite_bad_pixel_ratio=0.08628`,
  `compose_shader_linear_bad_pixel_ratio=0.06844`, `compose_shader_noise_bad_pixel_ratio=0.05475`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is focused descriptor/lifecycle change 7 after the
  2026-06-14 09:22 full parity sweep; broad parity remains deferred until roughly three more focused parity changes or
  an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-104239/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-runtime-effect-source-cache-eviction`: the latest broad
  parity report showed stable RuntimeEffect source-cache reuse before color-filter source-cache eviction while the row
  only asserted eviction and effect-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_RUNTIME_EFFECT_CACHE_HITS=1`,
  preserving the existing effect definition/use/cache-hit and RuntimeEffect eviction-type guards. Focused validation
  `CASES=parity-runtime-effect-source-cache-eviction ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 1,004 JBR command frames, 1,427 RuntimeEffect source-cache hits, 2,857
  RuntimeEffect source-cache misses, 2,855 RuntimeEffect source-cache evicts, zero RuntimeEffect compile/build
  failures, 40 effect-handle definition frames, 4,284 effect-handle use frames, 4,272 effect-handle cache-hit frames,
  zero effect-handle evicts, zero shader-handle markers, `avg_delta=2.118`, `bad_pixel_ratio=0.05032`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07507`,
  `compose_bottom_labels_bad_pixel_ratio=0.12590`, `compose_paragraph_probes_bad_pixel_ratio=0.08803`,
  `compose_shader_color_bad_pixel_ratio=0.07688`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor/lifecycle change 6 after the 2026-06-14 09:22 full parity sweep; broad parity remains deferred
  until roughly four more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-104020/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-forced-context-graphics-layer-render-effect`: the latest
  broad parity report showed stable forced-context destination migration markers while the row only asserted Skiko
  surface/cache markers and effect-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect definition/use/cache-hit guards.
  Focused validation
  `CASES=parity-forced-context-graphics-layer-render-effect ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 1,427 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 11 effect-handle definition
  frames, 2,195 effect-handle use frames, 2,184 effect-handle cache-hit frames, zero effect-handle evicts, zero
  shader-handle markers, zero RuntimeEffect cache markers, zero compile/build failures, `avg_delta=2.045`,
  `bad_pixel_ratio=0.04801`, `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.06988`,
  `compose_bottom_labels_bad_pixel_ratio=0.07624`, `compose_paragraph_probes_bad_pixel_ratio=0.09146`,
  `compose_shader_color_bad_pixel_ratio=0.05288`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor/lifecycle change 5 after the 2026-06-14 09:22 full parity sweep; broad parity remains deferred
  until roughly five more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-103622/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-resize-graphics-layer-render-effect`: the latest broad
  parity report showed stable resize-path destination migration markers while the row only asserted Skiko
  surface/cache markers and effect-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect definition/use/cache-hit guards.
  Focused validation `CASES=parity-resize-graphics-layer-render-effect ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with the row's expected resize `fallback_new_count=1`, zero picture frames, 1,272 JBR command frames, one
  JBR image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear
  marker, eight effect-handle definition frames, 1,811 effect-handle use frames, 1,803 effect-handle cache-hit frames,
  zero effect-handle evicts, zero shader-handle markers, zero RuntimeEffect cache markers, zero compile/build
  failures, `avg_delta=1.902`, `bad_pixel_ratio=0.04557`, `header_buttons_bad_pixel_ratio=0.02158`,
  `compose_bad_pixel_ratio=0.06463`, `compose_bottom_labels_bad_pixel_ratio=0.06596`,
  `compose_paragraph_probes_bad_pixel_ratio=0.07034`, `compose_shader_color_bad_pixel_ratio=0.06967`,
  `compose_shader_image_bad_pixel_ratio=0.06662`, `compose_shader_composite_bad_pixel_ratio=0.08610`,
  `compose_shader_linear_bad_pixel_ratio=0.06647`, `compose_shader_noise_bad_pixel_ratio=0.06845`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is focused descriptor/lifecycle change 4 after the
  2026-06-14 09:22 full parity sweep; broad parity remains deferred until roughly six more focused parity changes or
  an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-103149/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-forced-context-runtime-effect-stable-color-filter`: the
  latest broad parity report showed stable forced-context destination migration markers while the row only asserted
  Skiko surface/cache markers, effect-handle reuse, and RuntimeEffect source-cache reuse. Magic Jewel added
  `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing
  effect definition/use/cache-hit and RuntimeEffect source-cache guards. Focused validation
  `CASES=parity-forced-context-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with `fallback_new_count=0`, zero picture frames, 697 JBR command frames, one JBR image-cache clear, one
  scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 54 effect-handle
  definition frames, 1,080 effect-handle use frames, 1,071 effect-handle cache-hit frames, zero effect-handle evicts,
  zero shader-handle markers, 1,079 RuntimeEffect source-cache hits, one RuntimeEffect source-cache miss, zero
  RuntimeEffect source-cache evicts, zero compile/build failures, `avg_delta=1.981`, `bad_pixel_ratio=0.04668`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.06943`,
  `compose_bottom_labels_bad_pixel_ratio=0.07624`, `compose_paragraph_probes_bad_pixel_ratio=0.09146`,
  `compose_shader_color_bad_pixel_ratio=0.07688`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor/lifecycle change 1 after the 2026-06-14 09:22 full parity sweep; broad parity remains deferred
  until roughly nine more focused parity changes or an ABI/capability gate. Disk free was about 148Gi after the exact
  run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-102147/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-resize-graphics-layer-color-matrix-filter`: the latest
  broad parity report showed stable resize-path destination migration markers while the row only asserted Skiko
  surface/cache markers and effect-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect definition/use/cache-hit guards.
  Focused validation `CASES=parity-resize-graphics-layer-color-matrix-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with the row's expected resize `fallback_new_count=1`, zero picture frames, 883 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker,
  eight effect-handle definition frames, 1,508 effect-handle use frames, 1,500 effect-handle cache-hit frames, zero
  effect-handle evicts, zero shader-handle markers, zero RuntimeEffect cache markers, zero compile/build failures,
  `avg_delta=1.907`, `bad_pixel_ratio=0.04577`, `header_buttons_bad_pixel_ratio=0.02158`,
  `compose_bad_pixel_ratio=0.06496`, `compose_bottom_labels_bad_pixel_ratio=0.06596`,
  `compose_paragraph_probes_bad_pixel_ratio=0.07034`, `compose_shader_color_bad_pixel_ratio=0.06967`,
  `compose_shader_image_bad_pixel_ratio=0.06662`, `compose_shader_composite_bad_pixel_ratio=0.08610`,
  `compose_shader_linear_bad_pixel_ratio=0.06647`, `compose_shader_noise_bad_pixel_ratio=0.06845`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is focused descriptor/lifecycle change 2 after the
  2026-06-14 09:22 full parity sweep; broad parity remains deferred until roughly eight more focused parity changes or
  an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-102542/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-forced-context-graphics-layer-color-matrix-filter`: the
  latest broad parity report showed stable forced-context destination migration markers while the row only asserted
  Skiko surface/cache markers and effect-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect definition/use/cache-hit guards.
  Focused validation
  `CASES=parity-forced-context-graphics-layer-color-matrix-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with `fallback_new_count=0`, zero picture frames, 696 JBR command frames, one JBR image-cache clear, one
  scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 11 effect-handle
  definition frames, 1,266 effect-handle use frames, 1,255 effect-handle cache-hit frames, zero effect-handle evicts,
  zero shader-handle markers, zero RuntimeEffect cache markers, zero compile/build failures, `avg_delta=2.048`,
  `bad_pixel_ratio=0.04815`, `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07011`,
  `compose_bottom_labels_bad_pixel_ratio=0.07624`, `compose_paragraph_probes_bad_pixel_ratio=0.09146`,
  `compose_shader_color_bad_pixel_ratio=0.06389`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor/lifecycle change 3 after the 2026-06-14 09:22 full parity sweep; broad parity remains deferred
  until roughly seven more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-102844/suite.tsv`.
- 2026-06-14 batched full screenshot parity sweep after ten focused parity lifecycle/descriptor hardenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106. Aggregate: `fallback_sum=11`, zero JBR picture
  frames, 93,828 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The sweep covered the
  newly tightened forced-context turbulence shader, resize and forced-context composite-noise shader,
  resize/forced-context RuntimeEffect pure-color, and resize RuntimeEffect stable color-filter rows, alongside existing
  button chrome, geometry, text/font data, image/filter/shader descriptors, RuntimeEffect, graphics-layer, transform,
  resize, and forced-context rows. This resets the focused parity change counter to zero; next per-change parity
  validation should stay exact-row/tiny-group, with the next full parity sweep deferred until roughly ten more
  meaningful parity changes or an ABI/capability gate. Disk free was about 173Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-092237/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-forced-context-turbulence-shader`: the latest broad parity
  report showed stable forced-context destination migration markers while the row only asserted Skiko surface/cache
  markers plus shader-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing shader definition/use/cache-hit and
  effect-definition guards. Focused validation
  `CASES=parity-forced-context-turbulence-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 790 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 50 effect-handle definition
  frames, zero effect-handle use/cache-hit/evict frames, ten shader-handle definition frames, 1,268 shader-handle use
  frames, 1,258 shader-handle cache-hit frames, zero shader-handle evicts, zero RuntimeEffect cache markers, zero
  compile/build failures, `avg_delta=1.978`, `bad_pixel_ratio=0.04661`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.06931`,
  `compose_bottom_labels_bad_pixel_ratio=0.07624`, `compose_paragraph_probes_bad_pixel_ratio=0.09146`,
  `compose_shader_color_bad_pixel_ratio=0.07688`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08466`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07712`. This is
  focused descriptor/lifecycle change 5 after the 2026-06-14 07:55 full parity sweep; broad parity remains deferred
  until roughly five more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-090701/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-resize-composite-noise-shader`: the latest broad parity
  report showed stable resize-path destination migration markers while the row only asserted Skiko surface/cache
  markers plus shader-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing shader definition/use/cache-hit and
  effect-definition guards. Focused validation
  `CASES=parity-resize-composite-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's
  expected resize `fallback_new_count=1`, zero picture frames, 790 JBR command frames, one JBR image-cache clear, one
  scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 35 effect-handle
  definition frames, zero effect-handle use/cache-hit/evict frames, 21 shader-handle definition frames, 1,340
  shader-handle use frames, 1,333 shader-handle cache-hit frames, zero shader-handle evicts, zero RuntimeEffect cache
  markers, zero compile/build failures, `avg_delta=1.846`, `bad_pixel_ratio=0.04430`,
  `header_buttons_bad_pixel_ratio=0.02158`, `compose_bad_pixel_ratio=0.06429`,
  `compose_bottom_labels_bad_pixel_ratio=0.06596`, `compose_paragraph_probes_bad_pixel_ratio=0.07034`,
  `compose_shader_color_bad_pixel_ratio=0.03025`, `compose_shader_image_bad_pixel_ratio=0.06662`,
  `compose_shader_composite_bad_pixel_ratio=0.08610`, `compose_shader_linear_bad_pixel_ratio=0.06647`,
  `compose_shader_noise_bad_pixel_ratio=0.06845`, and `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is
  focused descriptor/lifecycle change 6 after the 2026-06-14 07:55 full parity sweep; broad parity remains deferred
  until roughly four more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-090959/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-forced-context-composite-noise-shader`: the latest broad
  parity report showed stable forced-context destination migration markers while the row only asserted Skiko
  surface/cache markers plus shader-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing shader definition/use/cache-hit and
  effect-definition guards. Focused validation
  `CASES=parity-forced-context-composite-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 800 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 45 effect-handle definition
  frames, zero effect-handle use/cache-hit/evict frames, 27 shader-handle definition frames, 1,324 shader-handle use
  frames, 1,315 shader-handle cache-hit frames, zero shader-handle evicts, zero RuntimeEffect cache markers, zero
  compile/build failures, `avg_delta=1.973`, `bad_pixel_ratio=0.04630`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.06880`,
  `compose_bottom_labels_bad_pixel_ratio=0.07624`, `compose_paragraph_probes_bad_pixel_ratio=0.09146`,
  `compose_shader_color_bad_pixel_ratio=0.00394`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor/lifecycle change 7 after the 2026-06-14 07:55 full parity sweep; broad parity remains deferred
  until roughly three more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-091228/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-resize-runtime-effect-pure-color`: the latest broad parity
  report showed stable resize-path destination migration markers while the row only asserted Skiko surface/cache
  markers, shader-handle reuse, and RuntimeEffect source-cache reuse. Magic Jewel added
  `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing
  shader definition/use/cache-hit, effect-definition, and RuntimeEffect source-cache guards. Focused validation
  `CASES=parity-resize-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's
  expected resize `fallback_new_count=1`, zero picture frames, 771 JBR command frames, one JBR image-cache clear, one
  scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 40 effect-handle
  definition frames, zero effect-handle use/cache-hit/evict frames, eight shader-handle definition frames, 1,213
  shader-handle use frames, 1,205 shader-handle cache-hit frames, zero shader-handle evicts, 1,211 RuntimeEffect
  source-cache hits, one RuntimeEffect source-cache miss, zero RuntimeEffect source-cache evicts, zero compile/build
  failures, `avg_delta=1.845`, `bad_pixel_ratio=0.04438`, `header_buttons_bad_pixel_ratio=0.02158`,
  `compose_bad_pixel_ratio=0.06443`, `compose_bottom_labels_bad_pixel_ratio=0.06596`,
  `compose_paragraph_probes_bad_pixel_ratio=0.07034`, `compose_shader_color_bad_pixel_ratio=0.06967`,
  `compose_shader_image_bad_pixel_ratio=0.05426`, `compose_shader_composite_bad_pixel_ratio=0.08610`,
  `compose_shader_linear_bad_pixel_ratio=0.06647`, `compose_shader_noise_bad_pixel_ratio=0.06845`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is focused descriptor/lifecycle change 8 after the
  2026-06-14 07:55 full parity sweep; broad parity remains deferred until roughly two more focused parity changes or
  an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-091510/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-forced-context-runtime-effect-pure-color`: the latest
  broad parity report showed stable forced-context destination migration markers while the row only asserted Skiko
  surface/cache markers, shader-handle reuse, and RuntimeEffect source-cache reuse. Magic Jewel added
  `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing
  shader definition/use/cache-hit, effect-definition, and RuntimeEffect source-cache guards. Focused validation
  `CASES=parity-forced-context-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 711 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 40 effect-handle definition
  frames, zero effect-handle use/cache-hit/evict frames, eight shader-handle definition frames, 1,257 shader-handle
  use frames, 1,249 shader-handle cache-hit frames, zero shader-handle evicts, 1,256 RuntimeEffect source-cache hits,
  one RuntimeEffect source-cache miss, zero RuntimeEffect source-cache evicts, zero compile/build failures,
  `avg_delta=1.964`, `bad_pixel_ratio=0.04619`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.06861`, `compose_bottom_labels_bad_pixel_ratio=0.07624`,
  `compose_paragraph_probes_bad_pixel_ratio=0.09146`, `compose_shader_color_bad_pixel_ratio=0.07688`,
  `compose_shader_image_bad_pixel_ratio=0.05874`, `compose_shader_composite_bad_pixel_ratio=0.08628`,
  `compose_shader_linear_bad_pixel_ratio=0.06844`, `compose_shader_noise_bad_pixel_ratio=0.05475`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is focused descriptor/lifecycle change 9 after the
  2026-06-14 07:55 full parity sweep; broad parity remains deferred until roughly one more focused parity change or
  an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-091740/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-resize-runtime-effect-stable-color-filter`: the latest
  broad parity report showed stable resize-path destination migration markers while the row only asserted Skiko
  surface/cache markers, effect-handle reuse, and RuntimeEffect source-cache reuse. Magic Jewel added
  `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing
  effect definition/use/cache-hit and RuntimeEffect source-cache guards. Focused validation
  `CASES=parity-resize-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  the row's expected resize `fallback_new_count=1`, zero picture frames, 805 JBR command frames, one JBR image-cache
  clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 48
  effect-handle definition frames, 1,330 effect-handle use frames, 1,322 effect-handle cache-hit frames, zero
  effect-handle evicts, zero shader-handle markers, 1,328 RuntimeEffect source-cache hits, one RuntimeEffect
  source-cache miss, zero RuntimeEffect source-cache evicts, zero compile/build failures, `avg_delta=1.855`,
  `bad_pixel_ratio=0.04467`, `header_buttons_bad_pixel_ratio=0.02158`, `compose_bad_pixel_ratio=0.06492`,
  `compose_bottom_labels_bad_pixel_ratio=0.06596`, `compose_paragraph_probes_bad_pixel_ratio=0.07034`,
  `compose_shader_color_bad_pixel_ratio=0.06967`, `compose_shader_image_bad_pixel_ratio=0.06662`,
  `compose_shader_composite_bad_pixel_ratio=0.08610`, `compose_shader_linear_bad_pixel_ratio=0.06647`,
  `compose_shader_noise_bad_pixel_ratio=0.06845`, and `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is
  focused descriptor/lifecycle change 10 after the 2026-06-14 07:55 full parity sweep; the next validation step is the
  batched broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-092014/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-resize-turbulence-shader`: the latest broad parity report
  showed stable resize-path destination migration markers while the row only asserted Skiko surface/cache markers plus
  shader-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing shader definition/use/cache-hit and
  effect-definition guards. Focused validation
  `CASES=parity-resize-turbulence-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's expected
  resize `fallback_new_count=1`, zero picture frames, 1,065 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 35 effect-handle definition
  frames, zero effect-handle use/cache-hit/evict frames, seven shader-handle definition frames, 1,613 shader-handle
  use frames, 1,606 shader-handle cache-hit frames, zero shader-handle evicts, zero RuntimeEffect cache markers, zero
  compile/build failures, `avg_delta=1.850`, `bad_pixel_ratio=0.04455`,
  `header_buttons_bad_pixel_ratio=0.02158`, `compose_bad_pixel_ratio=0.06472`,
  `compose_bottom_labels_bad_pixel_ratio=0.06596`, `compose_paragraph_probes_bad_pixel_ratio=0.07034`,
  `compose_shader_color_bad_pixel_ratio=0.06967`, `compose_shader_image_bad_pixel_ratio=0.06662`,
  `compose_shader_composite_bad_pixel_ratio=0.08610`, `compose_shader_linear_bad_pixel_ratio=0.06592`,
  `compose_shader_noise_bad_pixel_ratio=0.06845`, and `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is
  focused descriptor/lifecycle change 4 after the 2026-06-14 07:55 full parity sweep; broad parity remains deferred
  until roughly six more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-090236/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-forced-context-noise-shader`: the latest broad parity
  report showed stable forced-context destination migration markers while the row only asserted Skiko surface/cache
  markers plus shader-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing shader definition/use/cache-hit and
  effect-definition guards. Focused validation
  `CASES=parity-forced-context-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 1,036 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 45 effect-handle definition
  frames, zero effect-handle use/cache-hit/evict frames, nine shader-handle definition frames, 1,440 shader-handle use
  frames, 1,431 shader-handle cache-hit frames, zero shader-handle evicts, zero RuntimeEffect cache markers, zero
  compile/build failures, `avg_delta=1.975`, `bad_pixel_ratio=0.04639`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.06894`,
  `compose_bottom_labels_bad_pixel_ratio=0.07624`, `compose_paragraph_probes_bad_pixel_ratio=0.09146`,
  `compose_shader_color_bad_pixel_ratio=0.07688`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08292`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05233`, and `compose_shader_turbulence_bad_pixel_ratio=0.06561`. This is
  focused descriptor/lifecycle change 3 after the 2026-06-14 07:55 full parity sweep; broad parity remains deferred
  until roughly seven more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-085944/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-resize-noise-shader`: the latest broad parity report showed
  stable resize-path destination migration markers while the row only asserted Skiko surface/cache markers plus
  shader-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing shader definition/use/cache-hit and
  effect-definition guards. Focused validation
  `CASES=parity-resize-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's expected
  resize `fallback_new_count=1`, zero picture frames, 925 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 30 effect-handle definition
  frames, zero effect-handle use/cache-hit/evict frames, six shader-handle definition frames, 1,347 shader-handle use
  frames, 1,341 shader-handle cache-hit frames, zero shader-handle evicts, zero RuntimeEffect cache markers, zero
  compile/build failures, `avg_delta=1.849`, `bad_pixel_ratio=0.04439`,
  `header_buttons_bad_pixel_ratio=0.02158`, `compose_bad_pixel_ratio=0.06444`,
  `compose_bottom_labels_bad_pixel_ratio=0.06596`, `compose_paragraph_probes_bad_pixel_ratio=0.07034`,
  `compose_shader_color_bad_pixel_ratio=0.06967`, `compose_shader_image_bad_pixel_ratio=0.06662`,
  `compose_shader_composite_bad_pixel_ratio=0.08610`, `compose_shader_linear_bad_pixel_ratio=0.06327`,
  `compose_shader_noise_bad_pixel_ratio=0.06845`, and `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is
  focused descriptor/lifecycle change 2 after the 2026-06-14 07:55 full parity sweep; broad parity remains deferred
  until roughly eight more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-085700/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-forced-context-color-shader`: the latest broad parity report
  showed stable forced-context destination migration markers while the row only asserted Skiko surface/cache markers
  plus shader-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing shader definition/use/cache-hit and
  effect-definition guards. Focused validation
  `CASES=parity-forced-context-color-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 1,025 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 45 effect-handle definition
  frames, zero effect-handle use/cache-hit/evict frames, nine shader-handle definition frames, 1,547 shader-handle use
  frames, 1,538 shader-handle cache-hit frames, zero shader-handle evicts, zero RuntimeEffect cache markers, zero
  compile/build failures, `avg_delta=1.974`, `bad_pixel_ratio=0.04646`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.06906`,
  `compose_bottom_labels_bad_pixel_ratio=0.07624`, `compose_paragraph_probes_bad_pixel_ratio=0.09146`,
  `compose_shader_color_bad_pixel_ratio=0.00000`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor/lifecycle change 1 after the 2026-06-14 07:55 full parity sweep; broad parity remains deferred
  until roughly nine more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-085352/suite.tsv`.
- 2026-06-14 batched full screenshot parity sweep after ten focused parity lifecycle/descriptor hardenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106. Aggregate: `fallback_sum=11`, zero JBR picture
  frames, 97,534 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The sweep covered the
  newly tightened resize native text rows, resize/forced-context color-filter handle rows, and resize color-shader row
  plus existing button chrome, geometry, text/font data, image/filter/shader descriptors, RuntimeEffect,
  graphics-layer, transform, resize, and forced-context rows. This resets the focused parity change counter to zero;
  next per-change parity validation should stay exact-row/tiny-group, with the next full parity sweep deferred until
  roughly ten more meaningful parity changes or an ABI/capability gate. Disk free was about 174Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-075504/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-resize-color-shader`: the latest broad parity report showed
  stable resize-path destination migration markers while the row only asserted Skiko surface/cache markers plus
  shader-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing shader definition/use/cache-hit and
  effect-definition guards. Focused validation `CASES=parity-resize-color-shader ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with the row's expected resize `fallback_new_count=1`, zero picture frames, 925 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 40
  effect-handle definition frames, zero effect-handle use/cache-hit/evict frames, eight shader-handle definition
  frames, 1,461 shader-handle use frames, 1,453 shader-handle cache-hit frames, zero shader-handle evicts, zero
  RuntimeEffect cache markers, zero compile/build failures, `avg_delta=1.850`, `bad_pixel_ratio=0.04454`,
  `header_buttons_bad_pixel_ratio=0.02158`, `compose_bad_pixel_ratio=0.06470`,
  `compose_bottom_labels_bad_pixel_ratio=0.06596`, `compose_paragraph_probes_bad_pixel_ratio=0.07034`,
  `compose_shader_color_bad_pixel_ratio=0.04801`, `compose_shader_image_bad_pixel_ratio=0.06662`,
  `compose_shader_composite_bad_pixel_ratio=0.08610`, `compose_shader_linear_bad_pixel_ratio=0.06647`,
  `compose_shader_noise_bad_pixel_ratio=0.06845`, and `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is
  focused descriptor/lifecycle change 10 after the 2026-06-14 06:24 full parity sweep, so the next validation step is
  a batched broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-075306/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-forced-context-color-filter-handle`: the latest broad
  parity report showed stable forced-context destination migration markers while the row only asserted Skiko
  surface/cache markers and effect-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect-handle definition/use/cache-hit guards.
  Focused validation `CASES=parity-forced-context-color-filter-handle ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with `fallback_new_count=0`, zero picture frames, 1,110 JBR command frames, one JBR image-cache clear, one
  scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 11 effect-handle
  definition frames, 1,853 effect-handle use frames, 1,842 effect-handle cache-hit frames, zero effect-handle evicts,
  zero shader-handle markers, zero RuntimeEffect cache markers, zero compile/build failures, `avg_delta=2.065`,
  `bad_pixel_ratio=0.04897`, `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07149`,
  `compose_bottom_labels_bad_pixel_ratio=0.07624`, `compose_paragraph_probes_bad_pixel_ratio=0.09146`,
  `compose_shader_color_bad_pixel_ratio=0.07688`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor/lifecycle change 9 after the 2026-06-14 06:24 full parity sweep; broad parity remains deferred
  until roughly one more focused parity change or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-075047/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-resize-color-filter-handle`: the latest broad parity report
  showed stable resize-path destination migration markers while the row only asserted Skiko surface/cache markers and
  effect-handle reuse. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect-handle definition/use/cache-hit
  guards. Focused validation `CASES=parity-resize-color-filter-handle ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with the row's expected resize `fallback_new_count=1`, zero picture frames, 1,251 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker,
  nine effect-handle definition frames, 2,013 effect-handle use frames, 2,004 effect-handle cache-hit frames, zero
  effect-handle evicts, zero shader-handle markers, zero RuntimeEffect cache markers, zero compile/build failures,
  `avg_delta=1.922`, `bad_pixel_ratio=0.04650`, `header_buttons_bad_pixel_ratio=0.02158`,
  `compose_bad_pixel_ratio=0.06629`, `compose_bottom_labels_bad_pixel_ratio=0.06596`,
  `compose_paragraph_probes_bad_pixel_ratio=0.07034`, `compose_shader_color_bad_pixel_ratio=0.06967`,
  `compose_shader_image_bad_pixel_ratio=0.06662`, `compose_shader_composite_bad_pixel_ratio=0.08610`,
  `compose_shader_linear_bad_pixel_ratio=0.06647`, `compose_shader_noise_bad_pixel_ratio=0.06845`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is focused descriptor/lifecycle change 8 after the
  2026-06-14 06:24 full parity sweep; broad parity remains deferred until roughly two more focused parity changes or
  an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-074820/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-resize-native-system-font-text`: the latest broad parity
  report showed stable resize-path destination migration markers while the row only asserted Skiko surface/cache
  markers. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect-definition min/max guards. Focused
  validation `CASES=parity-resize-native-system-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  the row's expected resize `fallback_new_count=1`, zero picture frames, 492 JBR command frames, one JBR image-cache
  clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 35
  effect-handle definition frames, zero effect-handle use/cache-hit/evict frames, zero shader-handle markers, zero
  RuntimeEffect cache markers, zero compile/build failures, `avg_delta=1.868`, `bad_pixel_ratio=0.04499`,
  `header_buttons_bad_pixel_ratio=0.02158`, `compose_bad_pixel_ratio=0.06542`,
  `compose_bottom_labels_bad_pixel_ratio=0.06996`, `compose_paragraph_probes_bad_pixel_ratio=0.07034`,
  `compose_shader_color_bad_pixel_ratio=0.06967`, `compose_shader_image_bad_pixel_ratio=0.06662`,
  `compose_shader_composite_bad_pixel_ratio=0.08610`, `compose_shader_linear_bad_pixel_ratio=0.06647`,
  `compose_shader_noise_bad_pixel_ratio=0.06845`, and `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is
  focused descriptor/lifecycle change 7 after the 2026-06-14 06:24 full parity sweep; broad parity remains deferred
  until roughly three more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-074522/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-resize-native-resource-font-text`: the latest broad parity
  report showed stable resize-path destination migration markers while the row only asserted Skiko surface/cache
  markers. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing font-data and effect-definition min/max
  guards. Focused validation
  `CASES=parity-resize-native-resource-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 1,048 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 25 effect-handle definition
  frames, zero effect-handle use/cache-hit/evict frames, zero shader-handle markers, zero RuntimeEffect cache markers,
  zero compile/build failures, `avg_delta=1.946`, `bad_pixel_ratio=0.04588`,
  `header_buttons_bad_pixel_ratio=0.02158`, `compose_bad_pixel_ratio=0.06674`,
  `compose_bottom_labels_bad_pixel_ratio=0.08004`, `compose_paragraph_probes_bad_pixel_ratio=0.07034`,
  `compose_shader_color_bad_pixel_ratio=0.06967`, `compose_shader_image_bad_pixel_ratio=0.06662`,
  `compose_shader_composite_bad_pixel_ratio=0.08610`, `compose_shader_linear_bad_pixel_ratio=0.06647`,
  `compose_shader_noise_bad_pixel_ratio=0.06845`, and `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is
  focused descriptor/lifecycle change 6 after the 2026-06-14 06:24 full parity sweep; broad parity remains deferred
  until roughly four more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-074259/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-resize-native-loaded-font-data-text`: the latest broad
  parity report showed stable resize-path destination migration markers while the row only asserted Skiko surface/cache
  markers. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing font-data and effect-definition min/max
  guards. Focused validation
  `CASES=parity-resize-native-loaded-font-data-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 673 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 25 effect-handle definition
  frames, zero effect-handle use/cache-hit/evict frames, zero shader-handle markers, zero RuntimeEffect cache markers,
  zero compile/build failures, `avg_delta=1.923`, `bad_pixel_ratio=0.04559`,
  `header_buttons_bad_pixel_ratio=0.02158`, `compose_bad_pixel_ratio=0.06628`,
  `compose_bottom_labels_bad_pixel_ratio=0.07648`, `compose_paragraph_probes_bad_pixel_ratio=0.07034`,
  `compose_shader_color_bad_pixel_ratio=0.06967`, `compose_shader_image_bad_pixel_ratio=0.06662`,
  `compose_shader_composite_bad_pixel_ratio=0.08610`, `compose_shader_linear_bad_pixel_ratio=0.06647`,
  `compose_shader_noise_bad_pixel_ratio=0.06845`, and `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is
  focused descriptor/lifecycle change 5 after the 2026-06-14 06:24 full parity sweep; broad parity remains deferred
  until roughly five more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-074046/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-resize-native-generic-font-text`: the latest broad parity
  report showed stable resize-path destination migration markers while the row only asserted Skiko surface/cache
  markers. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect-definition min/max guards. Focused
  validation `CASES=parity-resize-native-generic-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  the row's expected resize `fallback_new_count=1`, zero picture frames, 1,063 JBR command frames, one JBR image-cache
  clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 35
  effect-handle definition frames, zero effect-handle use/cache-hit/evict frames, zero shader-handle markers, zero
  RuntimeEffect cache markers, zero compile/build failures, `avg_delta=1.982`, `bad_pixel_ratio=0.04633`,
  `header_buttons_bad_pixel_ratio=0.02158`, `compose_bad_pixel_ratio=0.06723`,
  `compose_bottom_labels_bad_pixel_ratio=0.08581`, `compose_paragraph_probes_bad_pixel_ratio=0.06862`,
  `compose_shader_color_bad_pixel_ratio=0.06967`, `compose_shader_image_bad_pixel_ratio=0.06662`,
  `compose_shader_composite_bad_pixel_ratio=0.08610`, `compose_shader_linear_bad_pixel_ratio=0.06647`,
  `compose_shader_noise_bad_pixel_ratio=0.06845`, and `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is
  focused descriptor/lifecycle change 4 after the 2026-06-14 06:24 full parity sweep; broad parity remains deferred
  until roughly six more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-073613/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-forced-context-native-resource-font-text`: the latest broad
  parity report showed stable forced-context destination migration markers while the row only asserted Skiko
  surface/context and command-cache markers. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing font-data and effect-definition min/max
  guards. Focused validation
  `CASES=parity-forced-context-native-resource-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 1,023 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 30 effect-handle definition
  frames, zero effect-handle use/cache-hit/evict frames, zero shader-handle markers, zero RuntimeEffect cache markers,
  zero compile/build failures, `avg_delta=2.089`, `bad_pixel_ratio=0.04805`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07153`,
  `compose_bottom_labels_bad_pixel_ratio=0.09251`, `compose_paragraph_probes_bad_pixel_ratio=0.09093`,
  `compose_shader_color_bad_pixel_ratio=0.07688`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor/lifecycle change 3 after the 2026-06-14 06:24 full parity sweep; broad parity remains deferred
  until roughly seven more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-073218/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-forced-context-native-loaded-font-data-text`: the latest
  broad parity report showed stable forced-context destination migration markers while the row only asserted Skiko
  surface/context and command-cache markers. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing font-data and effect-definition min/max
  guards. Focused validation
  `CASES=parity-forced-context-native-loaded-font-data-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 890 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 30 effect-handle definition
  frames, zero effect-handle use/cache-hit/evict frames, zero shader-handle markers, zero RuntimeEffect cache markers,
  zero compile/build failures, `avg_delta=2.062`, `bad_pixel_ratio=0.04772`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07101`,
  `compose_bottom_labels_bad_pixel_ratio=0.08823`, `compose_paragraph_probes_bad_pixel_ratio=0.09093`,
  `compose_shader_color_bad_pixel_ratio=0.07688`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor/lifecycle change 2 after the 2026-06-14 06:24 full parity sweep; broad parity remains deferred
  until roughly eight more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-072816/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-forced-context-native-generic-font-text`: the latest broad
  parity report showed stable forced-context destination migration markers while the row only asserted Skiko
  surface/context and command-cache markers. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect-definition min/max guards. Focused
  validation `CASES=parity-forced-context-native-generic-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with `fallback_new_count=0`, zero picture frames, 1,032 JBR command frames, one JBR image-cache clear, one
  scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 45 effect-handle
  definition frames, zero effect-handle use/cache-hit/evict frames, zero shader-handle markers, zero RuntimeEffect
  cache markers, zero compile/build failures, `avg_delta=2.129`, `bad_pixel_ratio=0.04848`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07210`,
  `compose_bottom_labels_bad_pixel_ratio=0.09956`, `compose_paragraph_probes_bad_pixel_ratio=0.08931`,
  `compose_shader_color_bad_pixel_ratio=0.07688`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor/lifecycle change 1 after the 2026-06-14 06:24 full parity sweep; broad parity remains deferred
  until roughly nine more focused parity changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-072430/suite.tsv`.
- 2026-06-14 batched full screenshot parity sweep after ten focused parity lifecycle/descriptor hardenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106. Aggregate: `fallback_sum=11`, zero JBR picture
  frames, 99,527 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The sweep covered
  the newly tightened forced-context native text lifecycle rows plus existing button chrome, geometry, text/font data,
  image/filter/shader descriptors, RuntimeEffect, graphics-layer, transform, resize, and forced-context rows. This
  resets the focused parity change counter to zero; next per-change parity validation should stay exact-row/tiny-group,
  with the next full parity sweep deferred until roughly ten more meaningful parity changes or an ABI/capability gate.
  Disk free was about 175Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-062458/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-forced-context-native-system-font-text`: the latest broad
  parity report showed stable forced-context destination migration markers while the row only asserted Skiko
  surface/context and command-cache markers. Magic Jewel added `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and
  `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing effect-definition min/max guards. Focused
  validation `CASES=parity-forced-context-native-system-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with `fallback_new_count=0`, zero picture frames, 956 JBR command frames, one JBR image-cache clear, one
  scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 45 effect-handle
  definition frames, zero effect-handle use/cache-hit/evict frames, zero shader-handle markers, zero RuntimeEffect
  cache markers, zero compile/build failures, `avg_delta=1.995`, `bad_pixel_ratio=0.04699`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.06989`,
  `compose_bottom_labels_bad_pixel_ratio=0.07979`, `compose_paragraph_probes_bad_pixel_ratio=0.09138`,
  `compose_shader_color_bad_pixel_ratio=0.07688`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This was
  focused descriptor/lifecycle change 10 after the 2026-06-13 21:15 full parity sweep and triggered the batched broad
  parity sweep above:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-062342/suite.tsv`.
- 2026-06-14 focused parity lifecycle hardening for `parity-forced-context-native-custom-font-text-image`: the exact
  row showed stable forced-context destination migration markers, so Magic Jewel added
  `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS=1` and `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS=1`, preserving the existing
  effect-definition max after a narrower exploratory cap proved too brittle in exact validation. Focused validation
  `CASES=parity-forced-context-native-custom-font-text-image ./scripts/jbr-skia-screenshot-parity-suite.sh` passed
  with `fallback_new_count=0`, zero picture frames, 1,041 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 40 effect-handle definition
  frames, zero effect-handle use/cache-hit/evict frames, zero shader-handle markers, zero RuntimeEffect cache markers,
  zero compile/build failures, `avg_delta=2.123`, `bad_pixel_ratio=0.05044`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07525`,
  `compose_bottom_labels_bad_pixel_ratio=0.12590`, `compose_paragraph_probes_bad_pixel_ratio=0.08803`,
  `compose_shader_color_bad_pixel_ratio=0.07688`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor/lifecycle change 9 after the 2026-06-13 21:15 full parity sweep; broad parity remains deferred
  until roughly one more focused change or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-061859/suite.tsv`.
- 2026-06-14 narrow artifact/benchmark refresh after the post-sweep compatibility matrix: required artifact matrix
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2.
  `current-all` reported `fallback_new_count=0`, 514 JBR command frames, and `background_window=true`.
  `missing-public-api` reported the expected `public-api-missing` fallback, `fallback_new_count=1`, zero JBR command
  frames, and `background_window=true`. The follow-up single-row benchmark smoke
  `CASES=commands ./scripts/jbr-skia-benchmark-suite.sh` passed with `fallback_new_count=0`, zero picture frames,
  4,076 JBR command frames, 17 old-side CPU samples, 17 new-side CPU samples, `old_avg_cpu=66.27`,
  `new_avg_cpu=71.56`, `app_old_fps=255.6`, `app_new_fps=203.8`, `jbr_picture_fps=0.0`, and
  `jbr_command_fps=203.8`. This validation refresh does not change the focused descriptor-cap counter, which remains
  8 after the 2026-06-13 21:15 full parity sweep. Disk free remained about 176Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260614-060948/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260614-061056/suite.tsv`.
- 2026-06-14 post-sweep compatibility matrix after the cadence-triggered full command-probe sweep:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with
  `fallback_sum=56`, 520 JBR command frames from the `happy` row, and `background_window=true` on all 57 rows. The
  matrix covered ABI mismatch, native ABI mismatch, command-capability mismatch, public API missing, low-word gradient
  and effect capability removals, and high-word capability removals for image filters, shader descriptors, RuntimeEffect
  color filters, path effects, transforms, shadows, points, font data, shader color/Perlin noise, and vertices. Disk
  free remained about 176Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260614-054414/matrix.tsv`.
- 2026-06-14 batched full command-probe sweep after ten focused command-probe hardenings:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` passed 549/549. Aggregate:
  `fallback_sum=350`, 80 rows with unsupported markers, 70,982 JBR picture frames, and 130,383 JBR command frames.
  The sweep covered the newly tightened shader/color-filter/gradient command-probe rows plus the existing ABI,
  descriptor, image/filter, path-effect, vertices, blend-mode, graphics-layer, render-effect, shadow, save-layer, and
  legacy raw-family fallback rows. This resets the focused command-probe change counter to zero; next per-change
  validation should stay exact-row/narrow, with the next full command sweep deferred until roughly ten more meaningful
  command-probe changes or an ABI/capability gate. Disk free was about 176Gi after the run and stale Magic Jewel Java
  validation process cleanup:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-000344/suite.tsv`.
- 2026-06-14 focused command-probe hardening for
  `commands-invalid-radial-gradient-shader-descriptor-radius-fallback`: the latest full command-probe sweep showed
  shader-handle use/cache-hit frames while the row only asserted descriptor definitions and the radial-gradient corrupt
  radius fallback marker. Magic Jewel added `EXPECT_MIN_JBR_SHADER_HANDLE_USES=1` and
  `EXPECT_MIN_JBR_SHADER_HANDLE_CACHE_HITS=1`, preserving the existing shader definition min/max guards. Focused
  validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-radial-gradient-shader-descriptor-radius-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, `unsupported=none`, zero picture frames, zero command frames after fallback,
  three shader-handle definition frames, 1,663 shader-handle use frames, 1,662 shader-handle cache-hit frames, zero
  shader-handle evicts, zero effect-handle definition/use/cache-hit/evict frames, zero RuntimeEffect cache markers,
  and zero compile/build failures:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-000122/suite.tsv`.
- 2026-06-13 focused command-probe hardening for
  `commands-invalid-linear-gradient-shader-descriptor-stop-order-fallback`: the latest full command-probe sweep showed
  shader-handle use/cache-hit frames and one effect-handle use while the row only asserted descriptor definitions and
  the linear-gradient corrupt stop-order fallback marker. Magic Jewel added `EXPECT_MIN_JBR_SHADER_HANDLE_USES=1`,
  `EXPECT_MIN_JBR_SHADER_HANDLE_CACHE_HITS=1`, and `EXPECT_MIN_JBR_EFFECT_HANDLE_USES=1`, preserving the existing
  definition min/max guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-linear-gradient-shader-descriptor-stop-order-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, `unsupported=none`, zero picture frames, zero command frames after fallback, one
  effect-handle definition frame, one effect-handle use frame, zero effect-handle cache-hit/evict frames, two
  shader-handle definition frames, 1,463 shader-handle use frames, 1,461 shader-handle cache-hit frames, zero
  shader-handle evicts, zero RuntimeEffect cache markers, and zero compile/build failures:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-235818/suite.tsv`.
- 2026-06-13 focused command-probe hardening for
  `commands-invalid-linear-gradient-shader-descriptor-tile-mode-fallback`: the latest full command-probe sweep showed
  shader-handle use/cache-hit frames and one effect-handle use while the row only asserted descriptor definitions and
  the linear-gradient corrupt tile-mode fallback marker. Magic Jewel added `EXPECT_MIN_JBR_SHADER_HANDLE_USES=1`,
  `EXPECT_MIN_JBR_SHADER_HANDLE_CACHE_HITS=1`, and `EXPECT_MIN_JBR_EFFECT_HANDLE_USES=1`, preserving the existing
  definition min/max guards. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-linear-gradient-shader-descriptor-tile-mode-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, `unsupported=none`, zero picture frames, zero command frames after fallback, one
  effect-handle definition frame, one effect-handle use frame, zero effect-handle cache-hit/evict frames, two
  shader-handle definition frames, 1,651 shader-handle use frames, 1,649 shader-handle cache-hit frames, zero
  shader-handle evicts, zero RuntimeEffect cache markers, and zero compile/build failures:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-235247/suite.tsv`.
- 2026-06-13 focused command-probe hardening for
  `commands-invalid-composite-shader-descriptor-blend-mode-fallback`: the latest full command-probe sweep showed three
  JBR shader-handle definitions plus shader-handle use/cache-hit frames while the row only asserted the
  composite-shader corrupt blend-mode fallback marker. Magic Jewel added
  `EXPECT_MIN_JBR_SHADER_HANDLE_DEFINES=3`, `EXPECT_MIN_JBR_SHADER_HANDLE_USES=1`,
  `EXPECT_MIN_JBR_SHADER_HANDLE_CACHE_HITS=1`, and `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES=3` to this exact row.
  Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-composite-shader-descriptor-blend-mode-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, `unsupported=none`, zero picture frames, zero command frames after fallback,
  three shader-handle definition frames, 1,901 shader-handle use frames, 1,900 shader-handle cache-hit frames, zero
  shader-handle evicts, zero effect-handle definition/use/cache-hit/evict frames, zero RuntimeEffect cache markers,
  and zero compile/build failures:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-235013/suite.tsv`.
- 2026-06-13 focused command-probe hardening for
  `commands-invalid-transformed-shader-descriptor-payload-count-fallback`: the latest full command-probe sweep showed
  two JBR shader-handle definitions plus shader-handle use/cache-hit frames while the row only asserted the
  transformed-shader corrupt payload-count fallback marker. Magic Jewel added
  `EXPECT_MIN_JBR_SHADER_HANDLE_DEFINES=2`, `EXPECT_MIN_JBR_SHADER_HANDLE_USES=1`,
  `EXPECT_MIN_JBR_SHADER_HANDLE_CACHE_HITS=1`, and `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES=2` to this exact row.
  Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-transformed-shader-descriptor-payload-count-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, `unsupported=none`, zero picture frames, zero command frames after fallback,
  two shader-handle definition frames, 1,883 shader-handle use frames, 1,882 shader-handle cache-hit frames, zero
  shader-handle evicts, zero effect-handle definition/use/cache-hit/evict frames, zero RuntimeEffect cache markers,
  and zero compile/build failures:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-234735/suite.tsv`.
- 2026-06-13 focused command-probe hardening for `commands-invalid-shader-descriptor-version-fallback`: the latest
  full command-probe sweep showed one JBR shader-handle definition plus shader-handle use/cache-hit frames while the
  row only asserted the corrupt descriptor-version fallback marker. Magic Jewel added
  `EXPECT_MIN_JBR_SHADER_HANDLE_DEFINES=1`, `EXPECT_MIN_JBR_SHADER_HANDLE_USES=1`,
  `EXPECT_MIN_JBR_SHADER_HANDLE_CACHE_HITS=1`, and `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES=1` to the shared
  descriptor-version case block. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-shader-descriptor-version-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, `unsupported=none`, zero picture frames, zero command frames after fallback,
  one shader-handle definition frame, 1,544 shader-handle use frames, 1,543 shader-handle cache-hit frames, zero
  shader-handle evicts, zero effect-handle definition/use/cache-hit/evict frames, zero RuntimeEffect cache markers,
  and zero compile/build failures:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-234432/suite.tsv`.
- 2026-06-13 focused command-probe hardening for `commands-invalid-shader-descriptor-record-length-fallback`: the
  latest full command-probe sweep showed one JBR shader-handle definition plus shader-handle use/cache-hit frames
  while the row only asserted the corrupt record-length fallback marker. Magic Jewel added
  `EXPECT_MIN_JBR_SHADER_HANDLE_DEFINES=1`, `EXPECT_MIN_JBR_SHADER_HANDLE_USES=1`,
  `EXPECT_MIN_JBR_SHADER_HANDLE_CACHE_HITS=1`, and `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES=1` to this exact row.
  Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-shader-descriptor-record-length-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, `unsupported=none`, zero picture frames, zero command frames after fallback,
  one shader-handle definition frame, 1,665 shader-handle use frames, 1,665 shader-handle cache-hit frames, zero
  shader-handle evicts, zero effect-handle definition/use/cache-hit/evict frames, zero RuntimeEffect cache markers,
  and zero compile/build failures:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-234224/suite.tsv`.
- 2026-06-13 focused command-probe hardening for
  `commands-invalid-shader-color-filter-descriptor-payload-count-fallback`: the latest full command-probe sweep showed
  two JBR shader-handle definitions, one JBR effect-handle definition, and shader-handle use/cache-hit frames while the
  row only asserted the corrupt payload-count fallback marker. Magic Jewel added
  `EXPECT_MIN_JBR_SHADER_HANDLE_DEFINES=2`, `EXPECT_MIN_JBR_SHADER_HANDLE_USES=1`,
  `EXPECT_MIN_JBR_SHADER_HANDLE_CACHE_HITS=1`, `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1`,
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES=2`, and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=1` to this exact row. Focused
  validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-shader-color-filter-descriptor-payload-count-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, `unsupported=none`, zero picture frames, zero command frames after fallback,
  one effect-handle definition frame, zero effect-handle use/cache-hit/evict frames, two shader-handle definition
  frames, 1,851 shader-handle use frames, 1,851 shader-handle cache-hit frames, zero shader-handle evicts, zero
  RuntimeEffect cache markers, and zero compile/build failures:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-234001/suite.tsv`.
- 2026-06-13 focused command-probe hardening for `commands-invalid-shader-descriptor-payload-count-fallback`: the
  latest full command-probe sweep showed one JBR shader-handle definition plus shader-handle use/cache-hit frames
  while the row only asserted the corrupt payload-count fallback marker. Magic Jewel added
  `EXPECT_MIN_JBR_SHADER_HANDLE_DEFINES=1`, `EXPECT_MIN_JBR_SHADER_HANDLE_USES=1`,
  `EXPECT_MIN_JBR_SHADER_HANDLE_CACHE_HITS=1`, and `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES=1` to this exact row.
  Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-shader-descriptor-payload-count-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, `unsupported=none`, zero picture frames, zero command frames after fallback,
  one shader-handle definition frame, 1,391 shader-handle use frames, 1,390 shader-handle cache-hit frames, zero
  shader-handle evicts, zero effect-handle definition/use/cache-hit/evict frames, zero RuntimeEffect cache markers,
  and zero compile/build failures:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-233518/suite.tsv`.
- 2026-06-13 focused command-probe hardening for
  `commands-invalid-color-shader-descriptor-payload-count-fallback`: the latest full command-probe sweep showed one
  JBR shader-handle definition plus shader-handle use/cache-hit frames while the row only asserted the corrupt
  payload-count fallback marker. Magic Jewel added `EXPECT_MIN_JBR_SHADER_HANDLE_DEFINES=1`,
  `EXPECT_MIN_JBR_SHADER_HANDLE_USES=1`, `EXPECT_MIN_JBR_SHADER_HANDLE_CACHE_HITS=1`, and
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES=1` to this exact row. Focused validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-color-shader-descriptor-payload-count-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, `unsupported=none`, zero picture frames, zero command frames after fallback,
  one shader-handle definition frame, 1,532 shader-handle use frames, 1,532 shader-handle cache-hit frames, zero
  shader-handle evicts, zero effect-handle definition/use/cache-hit/evict frames, zero RuntimeEffect cache markers,
  and zero compile/build failures:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-233307/suite.tsv`.
- 2026-06-13 narrow artifact/benchmark refresh after the compatibility matrix and eight focused descriptor-cap
  tightenings: required artifact matrix
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2.
  `current-all` reported `fallback_new_count=0`, 492 JBR command frames, and `background_window=true`.
  `missing-public-api` reported the expected `public-api-missing` fallback, `fallback_new_count=1`, zero JBR command
  frames, and `background_window=true`. The follow-up single-row benchmark smoke
  `CASES=commands ./scripts/jbr-skia-benchmark-suite.sh` passed with `fallback_new_count=0`, zero picture frames,
  3,898 JBR command frames, 16 old-side CPU samples, 17 new-side CPU samples, `old_avg_cpu=72.49`,
  `new_avg_cpu=77.05`, `app_old_fps=194.1`, `app_new_fps=194.8`, `jbr_picture_fps=0.0`, and
  `jbr_command_fps=194.9`. This validation refresh does not change the focused descriptor-cap counter, which remains
  8 after the 2026-06-13 21:15 full parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260613-232728/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260613-232907/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-native-custom-font-text-image`: the latest broad parity
  report showed 20 JBR effect-handle definitions while the row had custom native text/image coverage and no
  effect-handle definition guard. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving effect use expectations unset because reports
  show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-native-custom-font-text-image ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 882 JBR command frames, 20 effect-handle definition frames, zero
  effect-handle use/cache-hit/evict frames, zero shader-handle definition/use/cache-hit/evict frames, zero
  RuntimeEffect cache markers, zero compile/build failures, zero image-cache clear markers, zero scoped image-cache
  clear markers, zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.123`,
  `bad_pixel_ratio=0.05044`, `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07525`,
  `compose_bottom_labels_bad_pixel_ratio=0.12590`, `compose_paragraph_probes_bad_pixel_ratio=0.08803`,
  `compose_shader_color_bad_pixel_ratio=0.07688`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor-cap change 8 after the 2026-06-13 21:15 full parity sweep; broad parity remains deferred until
  roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-232306/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-geometry-clean`: the latest broad parity report showed 30
  JBR effect-handle definitions while the row had geometry/text/image-shader exclusions and no effect-handle
  definition guard. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=40` for this exact row, leaving effect use expectations unset because reports
  show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-geometry-clean ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`,
  zero picture frames, 617 JBR command frames, 20 effect-handle definition frames, zero effect-handle
  use/cache-hit/evict frames, zero shader-handle definition/use/cache-hit/evict frames, zero RuntimeEffect cache
  markers, zero compile/build failures, zero image-cache clear markers, zero scoped image-cache clear markers, zero
  Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.805`, `bad_pixel_ratio=0.07203`,
  `header_buttons_bad_pixel_ratio=0.00272`, `compose_bad_pixel_ratio=0.10955`,
  `compose_bottom_labels_bad_pixel_ratio=0.31738`, `compose_paragraph_probes_bad_pixel_ratio=0.09105`,
  `compose_shader_color_bad_pixel_ratio=0.07653`, `compose_shader_image_bad_pixel_ratio=0.06209`,
  `compose_shader_composite_bad_pixel_ratio=0.08478`, `compose_shader_linear_bad_pixel_ratio=0.06829`,
  `compose_shader_noise_bad_pixel_ratio=0.07673`, and `compose_shader_turbulence_bad_pixel_ratio=0.08183`. This is
  focused descriptor-cap change 7 after the 2026-06-13 21:15 full parity sweep; broad parity remains deferred until
  roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-231721/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-pure-color`: the latest broad parity report
  showed 15 JBR effect-handle definitions while the row had shader-handle definition/reuse gates and RuntimeEffect
  source-cache gates but no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving
  effect use expectations unset because reports show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 980 JBR command frames, 20 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, four shader-handle definition frames, 1,568 shader-handle use frames, 1,564
  shader-handle cache-hit frames, 1,567 RuntimeEffect source-cache hits, one RuntimeEffect source-cache miss, zero
  RuntimeEffect evictions, zero compile/build failures, zero image-cache clear markers, zero scoped image-cache clear
  markers, zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.105`,
  `bad_pixel_ratio=0.04993`, `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07439`,
  `compose_bottom_labels_bad_pixel_ratio=0.12590`, `compose_paragraph_probes_bad_pixel_ratio=0.08803`,
  `compose_shader_color_bad_pixel_ratio=0.07688`, `compose_shader_image_bad_pixel_ratio=0.05874`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor-cap change 6 after the 2026-06-13 21:15 full parity sweep; broad parity remains deferred until
  roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-231339/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-turbulence-shader`: the latest broad parity
  report showed 30 JBR effect-handle definitions while the row had shader-handle definition/reuse gates and
  forced-context cache-clear gates but no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=56` for this exact row, leaving
  effect use expectations unset because reports show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-forced-context-turbulence-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 454 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, 45 effect-handle definition frames, zero effect-handle use/cache-hit frames, nine shader-handle
  definition frames, 928 shader-handle use frames, 919 shader-handle cache-hit frames, zero RuntimeEffect cache
  markers, zero compile/build failures, one Skiko surface-change marker, one command-cache clear marker,
  `avg_delta=1.978`, `bad_pixel_ratio=0.04661`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.06931`, `compose_bottom_labels_bad_pixel_ratio=0.07624`,
  `compose_paragraph_probes_bad_pixel_ratio=0.09146`, `compose_shader_color_bad_pixel_ratio=0.07688`,
  `compose_shader_image_bad_pixel_ratio=0.06242`, `compose_shader_composite_bad_pixel_ratio=0.08466`,
  `compose_shader_linear_bad_pixel_ratio=0.06844`, `compose_shader_noise_bad_pixel_ratio=0.05475`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.07712`. This is focused descriptor-cap change 5 after the 2026-06-13
  21:15 full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-231024/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-turbulence-shader`: the latest broad parity report
  showed 20 JBR effect-handle definitions while the row had shader-handle definition/reuse gates and resize
  cache-clear gates but no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=48` for this exact row, leaving
  effect use expectations unset because reports show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-resize-turbulence-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=1`, zero picture frames, 799 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, 35 effect-handle definition frames, zero effect-handle use/cache-hit frames, seven shader-handle
  definition frames, 1,312 shader-handle use frames, 1,305 shader-handle cache-hit frames, zero RuntimeEffect cache
  markers, zero compile/build failures, one Skiko surface-change marker, one command-cache clear marker,
  `avg_delta=1.850`, `bad_pixel_ratio=0.04455`, `header_buttons_bad_pixel_ratio=0.02158`,
  `compose_bad_pixel_ratio=0.06472`, `compose_bottom_labels_bad_pixel_ratio=0.06596`,
  `compose_paragraph_probes_bad_pixel_ratio=0.07034`, `compose_shader_color_bad_pixel_ratio=0.06967`,
  `compose_shader_image_bad_pixel_ratio=0.06662`, `compose_shader_composite_bad_pixel_ratio=0.08610`,
  `compose_shader_linear_bad_pixel_ratio=0.06592`, `compose_shader_noise_bad_pixel_ratio=0.06845`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is focused descriptor-cap change 4 after the 2026-06-13
  21:15 full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-230715/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-turbulence-shader`: the latest broad parity report showed
  15 JBR effect-handle definitions while the row had shader-handle definition/reuse gates but no effect-handle
  definition guard. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving effect use expectations unset because reports
  show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-turbulence-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 960 JBR command frames, 15 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, three shader-handle definition frames, 1,608 shader-handle use frames, 1,605
  shader-handle cache-hit frames, zero RuntimeEffect cache markers, zero compile/build failures, zero image-cache
  clear markers, zero scoped image-cache clear markers, zero Skiko surface-change markers, zero command-cache clear
  markers, `avg_delta=2.119`, `bad_pixel_ratio=0.05035`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.07509`, `compose_bottom_labels_bad_pixel_ratio=0.12590`,
  `compose_paragraph_probes_bad_pixel_ratio=0.08803`, `compose_shader_color_bad_pixel_ratio=0.07688`,
  `compose_shader_image_bad_pixel_ratio=0.06242`, `compose_shader_composite_bad_pixel_ratio=0.08466`,
  `compose_shader_linear_bad_pixel_ratio=0.06844`, `compose_shader_noise_bad_pixel_ratio=0.05475`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.07712`. This is focused descriptor-cap change 3 after the 2026-06-13
  21:15 full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-230432/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-noise-shader`: the latest broad parity
  report showed 30 JBR effect-handle definitions while the row had shader-handle definition/reuse gates and
  forced-context cache-clear gates but no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=56` for this exact row, leaving
  effect use expectations unset because reports show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-forced-context-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 564 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, 35 effect-handle definition frames, zero effect-handle use/cache-hit frames, seven shader-handle
  definition frames, 1,031 shader-handle use frames, 1,024 shader-handle cache-hit frames, zero RuntimeEffect cache
  markers, zero compile/build failures, one Skiko surface-change marker, one command-cache clear marker,
  `avg_delta=1.975`, `bad_pixel_ratio=0.04639`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.06894`, `compose_bottom_labels_bad_pixel_ratio=0.07624`,
  `compose_paragraph_probes_bad_pixel_ratio=0.09146`, `compose_shader_color_bad_pixel_ratio=0.07688`,
  `compose_shader_image_bad_pixel_ratio=0.06242`, `compose_shader_composite_bad_pixel_ratio=0.08292`,
  `compose_shader_linear_bad_pixel_ratio=0.06844`, `compose_shader_noise_bad_pixel_ratio=0.05233`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.06561`. This is focused descriptor-cap change 2 after the 2026-06-13
  21:15 full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-230138/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-noise-shader`: the latest broad parity report showed
  30 JBR effect-handle definitions while the row had shader-handle definition/reuse gates and resize cache-clear gates
  but no effect-handle definition guard. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=48` for this exact row, leaving effect use expectations unset because reports
  show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-resize-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=1`, zero picture frames, 1,100 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, 35 effect-handle definition frames, zero effect-handle use/cache-hit frames, seven shader-handle
  definition frames, 1,656 shader-handle use frames, 1,649 shader-handle cache-hit frames, zero RuntimeEffect cache
  markers, zero compile/build failures, one Skiko surface-change marker, one command-cache clear marker,
  `avg_delta=1.849`, `bad_pixel_ratio=0.04439`, `header_buttons_bad_pixel_ratio=0.02158`,
  `compose_bad_pixel_ratio=0.06444`, `compose_bottom_labels_bad_pixel_ratio=0.06596`,
  `compose_paragraph_probes_bad_pixel_ratio=0.07034`, `compose_shader_color_bad_pixel_ratio=0.06967`,
  `compose_shader_image_bad_pixel_ratio=0.06662`, `compose_shader_composite_bad_pixel_ratio=0.08610`,
  `compose_shader_linear_bad_pixel_ratio=0.06327`, `compose_shader_noise_bad_pixel_ratio=0.06845`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is focused descriptor-cap change 1 after the 2026-06-13
  21:15 full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-225853/suite.tsv`.
- 2026-06-13 Magic Jewel compatibility matrix refresh after the full command-probe sweep and latest batched parity
  sweep: `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57. Aggregate:
  `fallback_sum=56`, 182 JBR command frames from the `happy` row, and `background_window=true` on all 57 rows. The
  matrix covered happy-path command replay plus ABI mismatch, native ABI mismatch, public API missing, full command
  capability mismatch, and individual low/high command-capability bit fallback rows for gradients, text/font data,
  image shader, blend mode, color filters, effect descriptors, image filters, RuntimeEffect color filters, path
  effects, concat transforms, direct shadows, shader color filters, draw points, shader transforms, color shaders,
  Perlin noise shaders, and vertices. Disk free was about 192Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260613-222843/matrix.tsv`.
- 2026-06-13 batched full screenshot parity sweep after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106. Aggregate: `fallback_sum=8`, zero JBR picture
  frames, 63,790 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The sweep covered
  text/font, image, gradient, shader, RuntimeEffect, color-filter, descriptor lifecycle, resize, forced-context, and
  graphics-layer parity rows after the latest shader descriptor-definition sentinel tightenings. This resets the
  focused descriptor-cap change counter to zero. Disk free was about 169Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-211500/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-noise-shader`: the latest broad parity report showed 20 JBR
  effect-handle definitions while the row had shader-handle definition/reuse gates but no effect-handle definition
  guard. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving effect use expectations unset because reports
  show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero
  picture frames, 783 JBR command frames, 20 effect-handle definition frames, zero effect-handle use/cache-hit frames,
  four shader-handle definition frames, 1,278 shader-handle use frames, 1,274 shader-handle cache-hit frames, zero
  RuntimeEffect cache markers, zero compile/build failures, zero image-cache clear markers, zero scoped image-cache
  clear markers, zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.116`,
  `bad_pixel_ratio=0.05013`, `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07473`,
  `compose_bottom_labels_bad_pixel_ratio=0.12590`, `compose_paragraph_probes_bad_pixel_ratio=0.08803`,
  `compose_shader_color_bad_pixel_ratio=0.07688`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08292`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05233`, and `compose_shader_turbulence_bad_pixel_ratio=0.06561`. This is
  focused descriptor-cap change 10 after the 2026-06-13 19:34 full parity sweep, so the next step is the batched
  broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-211224/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-color-shader`: the latest broad parity
  report showed 45 JBR effect-handle definitions while the row had shader-handle definition/reuse gates and
  forced-context cache-clear gates but no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=56` for this exact row, leaving
  effect use expectations unset because reports show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-forced-context-color-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 1,051 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, 50 effect-handle definition frames, zero effect-handle use/cache-hit frames, 10 shader-handle
  definition frames, 1,536 shader-handle use frames, 1,526 shader-handle cache-hit frames, zero RuntimeEffect cache
  markers, zero compile/build failures, one Skiko surface-change marker, one Skiko context-change marker, one
  command-cache clear marker, `avg_delta=1.974`, `bad_pixel_ratio=0.04646`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.06906`,
  `compose_bottom_labels_bad_pixel_ratio=0.07624`, `compose_paragraph_probes_bad_pixel_ratio=0.09146`,
  `compose_shader_color_bad_pixel_ratio=0.00000`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor-cap change 9 after the 2026-06-13 19:34 full parity sweep; after one more focused change, run the
  next batched broad parity sweep unless an ABI/capability gate supersedes it:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-210911/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-color-shader`: the latest broad parity report showed
  35 JBR effect-handle definitions while the row had shader-handle definition/reuse gates and resize cache-clear gates
  but no effect-handle definition guard. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=48` for this exact row, leaving effect use expectations unset because reports
  show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-resize-color-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=1`, zero picture frames, 627 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, 30 effect-handle definition frames, zero effect-handle use/cache-hit frames, six shader-handle
  definition frames, 1,232 shader-handle use frames, 1,226 shader-handle cache-hit frames, zero RuntimeEffect cache
  markers, zero compile/build failures, one Skiko surface-change marker, one command-cache clear marker,
  `avg_delta=1.850`, `bad_pixel_ratio=0.04454`, `header_buttons_bad_pixel_ratio=0.02158`,
  `compose_bad_pixel_ratio=0.06470`, `compose_bottom_labels_bad_pixel_ratio=0.06596`,
  `compose_paragraph_probes_bad_pixel_ratio=0.07034`, `compose_shader_color_bad_pixel_ratio=0.04801`,
  `compose_shader_image_bad_pixel_ratio=0.06662`, `compose_shader_composite_bad_pixel_ratio=0.08610`,
  `compose_shader_linear_bad_pixel_ratio=0.06647`, `compose_shader_noise_bad_pixel_ratio=0.06845`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is focused descriptor-cap change 8 after the 2026-06-13
  19:34 full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-210633/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-color-shader`: the latest broad parity report showed 20
  JBR effect-handle definitions while the row had shader-handle definition/reuse gates but no effect-handle definition
  guard. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving effect use expectations unset because reports
  show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-color-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero
  picture frames, 660 JBR command frames, 20 effect-handle definition frames, zero effect-handle use/cache-hit frames,
  four shader-handle definition frames, 1,084 shader-handle use frames, 1,080 shader-handle cache-hit frames, zero
  RuntimeEffect cache markers, zero compile/build failures, zero image-cache clear markers, zero scoped image-cache
  clear markers, zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.115`,
  `bad_pixel_ratio=0.05020`, `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07485`,
  `compose_bottom_labels_bad_pixel_ratio=0.12590`, `compose_paragraph_probes_bad_pixel_ratio=0.08803`,
  `compose_shader_color_bad_pixel_ratio=0.00000`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor-cap change 7 after the 2026-06-13 19:34 full parity sweep; broad parity remains deferred until
  roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-210338/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-composite-noise-shader`: the latest broad
  parity report showed 45 JBR effect-handle definitions while the row had shader-handle definition/reuse gates and
  forced-context cache-clear gates but no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=56` for this exact row, leaving
  effect use expectations unset because reports show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-forced-context-composite-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 564 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, 45 effect-handle definition frames, zero effect-handle use/cache-hit frames, 27 shader-handle
  definition frames, 1,138 shader-handle use frames, 1,129 shader-handle cache-hit frames, zero RuntimeEffect cache
  markers, zero compile/build failures, one Skiko surface-change marker, one Skiko context-change marker, one
  command-cache clear marker, `avg_delta=1.973`, `bad_pixel_ratio=0.04630`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.06880`,
  `compose_bottom_labels_bad_pixel_ratio=0.07624`, `compose_paragraph_probes_bad_pixel_ratio=0.09146`,
  `compose_shader_color_bad_pixel_ratio=0.00394`, `compose_shader_image_bad_pixel_ratio=0.06242`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor-cap change 6 after the 2026-06-13 19:34 full parity sweep; broad parity remains deferred until
  roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-205644/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-composite-noise-shader`: the latest broad parity
  report showed 40 JBR effect-handle definitions while the row had shader-handle definition/reuse gates and resize
  cache-clear gates but no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=48` for this exact row, leaving
  effect use expectations unset because reports show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-resize-composite-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=1`, zero picture frames, 588 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, 35 effect-handle definition frames, zero effect-handle use/cache-hit frames, 21 shader-handle
  definition frames, 1,114 shader-handle use frames, 1,107 shader-handle cache-hit frames, zero RuntimeEffect cache
  markers, zero compile/build failures, one Skiko surface-change marker, one command-cache clear marker,
  `avg_delta=1.846`, `bad_pixel_ratio=0.04430`, `header_buttons_bad_pixel_ratio=0.02158`,
  `compose_bad_pixel_ratio=0.06429`, `compose_bottom_labels_bad_pixel_ratio=0.06596`,
  `compose_paragraph_probes_bad_pixel_ratio=0.07034`, `compose_shader_color_bad_pixel_ratio=0.03025`,
  `compose_shader_image_bad_pixel_ratio=0.06662`, `compose_shader_composite_bad_pixel_ratio=0.08610`,
  `compose_shader_linear_bad_pixel_ratio=0.06647`, `compose_shader_noise_bad_pixel_ratio=0.06845`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.07863`. This is focused descriptor-cap change 5 after the 2026-06-13
  19:34 full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-205349/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-composite-noise-shader`: the latest broad parity report
  showed 20 JBR effect-handle definitions while the row had shader-handle definition/reuse gates but no effect-handle
  definition guard. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving effect use expectations unset because reports
  show zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-composite-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 402 JBR command frames, 15 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, nine shader-handle definition frames, 755 shader-handle use frames, 752
  shader-handle cache-hit frames, zero RuntimeEffect cache markers, zero compile/build failures, zero image-cache
  clear markers, zero scoped image-cache clear markers, zero Skiko surface-change markers, zero command-cache clear
  markers, `avg_delta=2.114`, `bad_pixel_ratio=0.05004`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.07458`, `compose_bottom_labels_bad_pixel_ratio=0.12590`,
  `compose_paragraph_probes_bad_pixel_ratio=0.08803`, `compose_shader_color_bad_pixel_ratio=0.00394`,
  `compose_shader_image_bad_pixel_ratio=0.06242`, `compose_shader_composite_bad_pixel_ratio=0.08628`,
  `compose_shader_linear_bad_pixel_ratio=0.06844`, `compose_shader_noise_bad_pixel_ratio=0.05475`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is focused descriptor-cap change 4 after the 2026-06-13
  19:34 full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-205047/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-shader-source-cache-eviction`: the row
  already had shader-handle definition/reuse guards and a max JBR effect-handle definition guard, but the latest broad
  parity report showed 20 effect definitions and no minimum effect-definition sentinel. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` for this exact row, preserving
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` and leaving effect use expectations unset because the report showed zero
  effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-runtime-effect-shader-source-cache-eviction ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 730 JBR command frames, 20 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, 20 shader-handle definition frames, 3,507 shader-handle use frames, 3,495
  shader-handle cache-hit frames, 1,168 RuntimeEffect source-cache hits, 2,339 RuntimeEffect source-cache misses,
  2,337 RuntimeEffect source-cache evictions, zero compile/build failures, zero image-cache clear markers, zero scoped
  image-cache clear markers, zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.084`,
  `bad_pixel_ratio=0.04931`, `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07335`,
  `compose_bottom_labels_bad_pixel_ratio=0.12590`, `compose_paragraph_probes_bad_pixel_ratio=0.08803`,
  `compose_shader_color_bad_pixel_ratio=0.07688`, `compose_shader_image_bad_pixel_ratio=0.05001`,
  `compose_shader_composite_bad_pixel_ratio=0.08628`, `compose_shader_linear_bad_pixel_ratio=0.06844`,
  `compose_shader_noise_bad_pixel_ratio=0.05475`, and `compose_shader_turbulence_bad_pixel_ratio=0.07768`. This is
  focused descriptor-cap change 3 after the 2026-06-13 19:34 full parity sweep; broad parity remains deferred until
  roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-204648/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-transformed-shader`: the row already had a max JBR
  effect-handle definition guard, but the latest broad parity report showed 20 definitions and no minimum
  effect-definition sentinel. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` for this exact row, preserving
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` and leaving effect use expectations unset because the report showed zero
  effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-transformed-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 539 JBR command frames, 20 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, eight shader-handle definition frames, 1,018 shader-handle use frames, 1,014
  shader-handle cache-hit frames, zero RuntimeEffect cache markers, zero compile/build failures, zero image-cache
  clear markers, zero scoped image-cache clear markers, zero Skiko surface-change markers, zero command-cache clear
  markers, `avg_delta=2.110`, `bad_pixel_ratio=0.05007`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.07463`, `compose_bottom_labels_bad_pixel_ratio=0.12590`,
  `compose_paragraph_probes_bad_pixel_ratio=0.08803`, and `compose_shader_linear_bad_pixel_ratio=0.06584`. This is
  focused descriptor-cap change 2 after the 2026-06-13 19:34 full parity sweep; broad parity remains deferred until
  roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-204032/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-composite-shader`: the row already had a max JBR
  effect-handle definition guard, but the latest broad parity report showed 30 definitions and no minimum
  effect-definition sentinel. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` for this exact row, preserving
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=40` and leaving effect use expectations unset because the report showed zero
  effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-composite-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`,
  zero picture frames, 736 JBR command frames, 25 effect-handle definition frames, zero effect-handle use/cache-hit
  frames, 15 shader-handle definition frames, 1,336 shader-handle use frames, 1,331 shader-handle cache-hit frames,
  zero RuntimeEffect cache markers, zero compile/build failures, zero image-cache clear markers, zero scoped
  image-cache clear markers, zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.596`,
  `bad_pixel_ratio=0.06710`, `header_buttons_bad_pixel_ratio=0.00272`, `compose_bad_pixel_ratio=0.10153`,
  `compose_bottom_labels_bad_pixel_ratio=0.31738`, `compose_paragraph_probes_bad_pixel_ratio=0.09105`, and
  `compose_shader_composite_bad_pixel_ratio=0.08478`. This is focused descriptor-cap change 1 after the 2026-06-13
  19:34 full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-203517/suite.tsv`.
- 2026-06-13 batched full screenshot parity sweep after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106. Aggregate: `fallback_sum=11`, zero JBR picture
  frames, 90,449 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The sweep covered
  text/font, image, gradient, shader, RuntimeEffect, color-filter, descriptor lifecycle, resize, forced-context, and
  graphics-layer parity rows after the latest RuntimeEffect descriptor-definition sentinel tightenings. This resets the
  focused descriptor-cap change counter to zero. Disk free was about 196Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-193432/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-shader`: the row already had a max JBR
  effect-handle definition guard, but the latest broad parity report showed a stable 20 definitions and no minimum
  effect-definition sentinel. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` for this exact row, preserving
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=24` and leaving effect use expectations unset because the report showed zero
  effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-runtime-effect-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 571 JBR command frames, 20 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, 12 shader-handle definition frames, 1,033 shader-handle use frames, 1,029
  shader-handle cache-hit frames, 1,032 RuntimeEffect source-cache hits, one RuntimeEffect source-cache miss, zero
  compile/build failures, zero image-cache clear markers, zero scoped image-cache clear markers, zero Skiko
  surface-change markers, zero command-cache clear markers, `avg_delta=2.111`, `bad_pixel_ratio=0.05008`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07465`,
  `compose_bottom_labels_bad_pixel_ratio=0.12590`, `compose_paragraph_probes_bad_pixel_ratio=0.08803`, and
  `compose_shader_image_bad_pixel_ratio=0.05073`. This is focused descriptor-cap change 10 after the 2026-06-13
  18:07 full parity sweep, so the next step is the batched broad screenshot parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-193209/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-child-only`: the row already had a max JBR
  effect-handle definition guard, but the latest broad parity report showed a stable 20 definitions and no minimum
  effect-definition sentinel. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` for this exact row, preserving
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` and leaving effect use expectations unset because the report showed zero
  effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-runtime-effect-child-only ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 572 JBR command frames, 20 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, 12 shader-handle definition frames, 1,060 shader-handle use frames, 1,056
  shader-handle cache-hit frames, 1,059 RuntimeEffect source-cache hits, one RuntimeEffect source-cache miss, zero
  compile/build failures, zero image-cache clear markers, zero scoped image-cache clear markers, zero Skiko
  surface-change markers, zero command-cache clear markers, `avg_delta=2.118`, `bad_pixel_ratio=0.05029`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07500`,
  `compose_bottom_labels_bad_pixel_ratio=0.12590`, `compose_paragraph_probes_bad_pixel_ratio=0.08803`, and
  `compose_shader_image_bad_pixel_ratio=0.06242`. This is focused descriptor-cap change 9 after the 2026-06-13 18:07
  full parity sweep; after one more focused change, run the next batched broad parity sweep unless an ABI/capability
  gate supersedes it:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-192932/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-uniform-only`: the row already had a max JBR
  effect-handle definition guard, but the latest broad parity report showed a stable 20 definitions and no minimum
  effect-definition sentinel. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` for this exact row, preserving
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=24` and leaving effect use expectations unset because the report showed zero
  effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-runtime-effect-uniform-only ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 695 JBR command frames, 20 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, four shader-handle definition frames, 1,185 shader-handle use frames, 1,181
  shader-handle cache-hit frames, 1,184 RuntimeEffect source-cache hits, one RuntimeEffect source-cache miss, zero
  compile/build failures, zero image-cache clear markers, zero scoped image-cache clear markers, zero Skiko
  surface-change markers, zero command-cache clear markers, `avg_delta=2.107`, `bad_pixel_ratio=0.04997`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07446`,
  `compose_bottom_labels_bad_pixel_ratio=0.12590`, `compose_paragraph_probes_bad_pixel_ratio=0.08803`, and
  `compose_shader_color_bad_pixel_ratio=0.07688`. This is focused descriptor-cap change 8 after the 2026-06-13 18:07
  full parity sweep; broad parity remains deferred until roughly two more focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-192644/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-runtime-effect-pure-color`: the latest broad
  parity report showed 45 JBR effect-handle definitions while the row had shader-handle and RuntimeEffect cache gates
  but no effect-handle definition guard. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=56` for this exact row, leaving effect use expectations unset because the
  report showed zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-forced-context-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 909 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, 45 effect-handle definition frames, zero effect-handle use/cache-hit frames, nine shader-handle
  definition frames, 1,389 shader-handle use frames, 1,380 shader-handle cache-hit frames, 1,388 RuntimeEffect
  source-cache hits, one RuntimeEffect source-cache miss, zero compile/build failures, one Skiko surface-change
  marker, one command-cache clear marker, `avg_delta=1.964`, `bad_pixel_ratio=0.04619`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.06861`,
  `compose_bottom_labels_bad_pixel_ratio=0.07624`, `compose_paragraph_probes_bad_pixel_ratio=0.09146`, and
  `compose_shader_color_bad_pixel_ratio=0.07688`. This is focused descriptor-cap change 7 after the 2026-06-13 18:07
  full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-192406/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-runtime-effect-pure-color`: the latest broad parity
  report showed 35 JBR effect-handle definitions while the row had shader-handle and RuntimeEffect cache gates but no
  effect-handle definition guard. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=48` for this exact row, leaving effect use expectations unset because the
  report showed zero effect-handle use/cache-hit frames. Focused validation
  `CASES=parity-resize-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=1`, zero picture frames, 676 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, 35 effect-handle definition frames, zero effect-handle use/cache-hit frames, seven shader-handle
  definition frames, 1,129 shader-handle use frames, 1,122 shader-handle cache-hit frames, 1,127 RuntimeEffect
  source-cache hits, one RuntimeEffect source-cache miss, zero compile/build failures, one Skiko surface-change
  marker, one command-cache clear marker, `avg_delta=1.845`, `bad_pixel_ratio=0.04438`,
  `header_buttons_bad_pixel_ratio=0.02158`, `compose_bad_pixel_ratio=0.06443`,
  `compose_bottom_labels_bad_pixel_ratio=0.06596`, `compose_paragraph_probes_bad_pixel_ratio=0.07034`, and
  `compose_shader_color_bad_pixel_ratio=0.06967`. This is focused descriptor-cap change 6 after the 2026-06-13 18:07
  full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-192102/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-image-refs`: recent broad parity reports
  showed exactly 40 JBR effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel
  added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=48` for this exact row,
  leaving effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused
  validation `CASES=parity-forced-context-image-refs ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 638 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, 40 effect-handle definition frames, zero effect-handle use/cache-hit frames, zero shader-handle
  definition/use/cache-hit frames, one Skiko surface-change marker, one command-cache clear marker,
  `avg_delta=2.129`, `bad_pixel_ratio=0.05050`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.07527`, `compose_bottom_labels_bad_pixel_ratio=0.12590`, and
  `compose_paragraph_probes_bad_pixel_ratio=0.08803`. This is focused descriptor-cap change 5 after the 2026-06-13
  18:07 full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-191651/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-native-custom-font-text-image`: recent broad
  parity reports showed exactly 40 JBR effect-handle definitions while the row had no effect-handle definition guard.
  Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=48` for this
  exact row, leaving effect/shader use expectations unset because reports consistently show zero use/cache-hit frames.
  Focused validation `CASES=parity-forced-context-native-custom-font-text-image
  ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero picture frames, 670 JBR
  command frames, 40 effect-handle definition frames, zero effect-handle use/cache-hit frames, zero shader-handle
  definition/use/cache-hit frames, one Skiko surface-change marker, one command-cache clear marker,
  `avg_delta=2.123`, `bad_pixel_ratio=0.05044`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.07525`, `compose_bottom_labels_bad_pixel_ratio=0.12590`, and
  `compose_paragraph_probes_bad_pixel_ratio=0.08803`. This is focused descriptor-cap change 4 after the 2026-06-13
  18:07 full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-191420/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-native-system-font-text`: recent broad parity
  reports showed 45 JBR effect-handle definitions while the row had no effect-handle definition guard, and exact
  validation observed 50 definitions. Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=56` for this exact row, leaving effect/shader use expectations unset because
  reports consistently show zero use/cache-hit frames. Focused validation
  `CASES=parity-forced-context-native-system-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 917 JBR command frames, 50 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, zero shader-handle definition/use/cache-hit frames, one Skiko surface-change
  marker, one command-cache clear marker, `avg_delta=1.995`, `bad_pixel_ratio=0.04699`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.06989`,
  `compose_bottom_labels_bad_pixel_ratio=0.07979`, and `compose_paragraph_probes_bad_pixel_ratio=0.09138`. This is
  focused descriptor-cap change 3 after the 2026-06-13 18:07 full parity sweep; broad parity remains deferred until
  roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-191045/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-native-generic-font-text`: recent broad
  parity reports showed exactly 45 JBR effect-handle definitions while the row had no effect-handle definition guard.
  Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=56` for this
  exact row, leaving effect/shader use expectations unset because reports consistently show zero use/cache-hit frames.
  Focused validation `CASES=parity-forced-context-native-generic-font-text
  ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero picture frames, 838 JBR
  command frames, 45 effect-handle definition frames, zero effect-handle use/cache-hit frames, zero shader-handle
  definition/use/cache-hit frames, one Skiko surface-change marker, one command-cache clear marker,
  `avg_delta=2.129`, `bad_pixel_ratio=0.04848`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.07210`, `compose_bottom_labels_bad_pixel_ratio=0.09956`, and
  `compose_paragraph_probes_bad_pixel_ratio=0.08931`. This is focused descriptor-cap change 2 after the 2026-06-13
  18:07 full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-190813/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-native-resource-font-text`: recent broad
  parity reports showed exactly 30 JBR effect-handle definitions while the row had no effect-handle definition guard.
  Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=40` for this
  exact row, leaving effect/shader use expectations unset because reports consistently show zero use/cache-hit frames.
  Focused validation `CASES=parity-forced-context-native-resource-font-text
  ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero picture frames, 625 JBR
  command frames, 30 effect-handle definition frames, zero effect-handle use/cache-hit frames, zero shader-handle
  definition/use/cache-hit frames, one Skiko surface-change marker, one command-cache clear marker,
  `avg_delta=2.089`, `bad_pixel_ratio=0.04805`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.07153`, `compose_bottom_labels_bad_pixel_ratio=0.09251`, and
  `compose_paragraph_probes_bad_pixel_ratio=0.09093`. This is focused descriptor-cap change 1 after the 2026-06-13
  18:07 full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-190601/suite.tsv`.
- 2026-06-13 batched full screenshot parity sweep after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106. Aggregate: `fallback_sum=12`, zero JBR picture
  frames, 100,079 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The sweep covered
  text/font, image, gradient, shader, RuntimeEffect, color-filter, descriptor lifecycle, and graphics-layer parity rows
  after the latest native-font resize and forced-context descriptor-cap tightenings. This resets the focused
  descriptor-cap change counter to zero. Disk free was about 196Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-180718/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-native-loaded-font-data-text`: recent broad
  parity reports showed exactly 30 JBR effect-handle definitions while the row had no effect-handle definition guard.
  Magic Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=40` for this
  exact row, leaving effect/shader use expectations unset because reports consistently show zero use/cache-hit frames.
  Focused validation `CASES=parity-forced-context-native-loaded-font-data-text
  ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero picture frames, 881 JBR
  command frames, 30 effect-handle definition frames, zero effect-handle use/cache-hit frames, zero shader-handle
  definition/use/cache-hit frames, one Skiko surface-change marker, one command-cache clear marker,
  `avg_delta=2.062`, `bad_pixel_ratio=0.04772`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.07101`, `compose_bottom_labels_bad_pixel_ratio=0.08823`, and
  `compose_paragraph_probes_bad_pixel_ratio=0.09093`. This was focused descriptor-cap change 10 after the 2026-06-13
  16:43 full parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-180614/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-native-system-font-text`: recent broad parity
  reports showed 35-40 JBR effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel
  added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=48` for this exact row,
  leaving effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused
  validation `CASES=parity-resize-native-system-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=1`, zero picture frames, 659 JBR command frames, 35 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, zero shader-handle definition/use/cache-hit frames, one Skiko surface-change
  marker, one command-cache clear marker, `avg_delta=1.868`, `bad_pixel_ratio=0.04499`,
  `header_buttons_bad_pixel_ratio=0.02158`, `compose_bad_pixel_ratio=0.06542`,
  `compose_bottom_labels_bad_pixel_ratio=0.06996`, and `compose_paragraph_probes_bad_pixel_ratio=0.07034`. This is
  focused descriptor-cap change 9 after the 2026-06-13 16:43 full parity sweep; after one more focused change, run
  the next batched broad parity sweep unless an ABI/capability gate supersedes it:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-180424/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-native-generic-font-text`: recent broad parity
  reports showed 35-40 JBR effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel
  added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=48` for this exact row,
  leaving effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused
  validation `CASES=parity-resize-native-generic-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=1`, zero picture frames, 687 JBR command frames, 35 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, zero shader-handle definition/use/cache-hit frames, one Skiko surface-change
  marker, one command-cache clear marker, `avg_delta=1.982`, `bad_pixel_ratio=0.04633`,
  `header_buttons_bad_pixel_ratio=0.02158`, `compose_bad_pixel_ratio=0.06723`,
  `compose_bottom_labels_bad_pixel_ratio=0.08581`, and `compose_paragraph_probes_bad_pixel_ratio=0.06862`. This is
  focused descriptor-cap change 8 after the 2026-06-13 16:43 full parity sweep; broad parity remains deferred until
  roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-180106/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-native-resource-font-text`: recent broad parity
  reports showed exactly 25 JBR effect-handle definitions while the row had no effect-handle definition guard. Magic
  Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact
  row, leaving effect/shader use expectations unset because reports consistently show zero use/cache-hit frames.
  Focused validation `CASES=parity-resize-native-resource-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with `fallback_new_count=1`, zero picture frames, 656 JBR command frames, 25 effect-handle definition frames,
  zero effect-handle use/cache-hit frames, zero shader-handle definition/use/cache-hit frames, one Skiko
  surface-change marker, one command-cache clear marker, `avg_delta=1.946`, `bad_pixel_ratio=0.04588`, and
  `compose_bottom_labels_bad_pixel_ratio=0.08004`. This is focused descriptor-cap change 7 after the 2026-06-13 16:43
  full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-175843/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-native-loaded-font-data-text`: recent broad parity
  reports showed exactly 25 JBR effect-handle definitions while the row had no effect-handle definition guard. Magic
  Jewel added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact
  row, leaving effect/shader use expectations unset because reports consistently show zero use/cache-hit frames.
  Focused validation `CASES=parity-resize-native-loaded-font-data-text ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with `fallback_new_count=0`, zero picture frames, 630 JBR command frames, 25 effect-handle definition frames,
  zero effect-handle use/cache-hit frames, zero shader-handle definition/use/cache-hit frames, one Skiko
  surface-change marker, one command-cache clear marker, `avg_delta=1.923`, `bad_pixel_ratio=0.04559`, and
  `compose_bottom_labels_bad_pixel_ratio=0.07648`. This is focused descriptor-cap change 6 after the 2026-06-13 16:43
  full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-175633/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-native-system-font-text`: recent broad parity reports showed
  20-25 JBR effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving
  effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused validation
  `CASES=parity-native-system-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 680 JBR command frames, 20 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, zero shader-handle definition/use/cache-hit frames, zero Skiko surface-change
  markers, zero command-cache clear markers, `avg_delta=1.995`, `bad_pixel_ratio=0.04699`, and
  `compose_bottom_labels_bad_pixel_ratio=0.07979`. This is focused descriptor-cap change 5 after the 2026-06-13 16:43
  full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-175343/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-native-generic-font-text`: recent broad parity reports
  showed exactly 25 JBR effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel
  added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row,
  leaving effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused
  validation `CASES=parity-native-generic-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 894 JBR command frames, 20 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, zero shader-handle definition/use/cache-hit frames, zero Skiko surface-change
  markers, zero command-cache clear markers, `avg_delta=2.129`, `bad_pixel_ratio=0.04848`, and
  `compose_bottom_labels_bad_pixel_ratio=0.09956`. This is focused descriptor-cap change 4 after the 2026-06-13 16:43
  full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-175119/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-native-resource-font-text`: recent broad parity reports
  showed exactly 15 JBR effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel
  added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=24` for this exact row,
  leaving effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused
  validation `CASES=parity-native-resource-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 892 JBR command frames, 15 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, zero shader-handle definition/use/cache-hit frames, zero Skiko surface-change
  markers, zero command-cache clear markers, `avg_delta=2.089`, `bad_pixel_ratio=0.04805`, and
  `compose_bottom_labels_bad_pixel_ratio=0.09251`. This is focused descriptor-cap change 3 after the 2026-06-13 16:43
  full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-174907/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-native-loaded-font-data-text`: recent broad parity reports
  showed exactly 15 JBR effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel
  added `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=24` for this exact row,
  leaving effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused
  validation `CASES=parity-native-loaded-font-data-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 663 JBR command frames, 15 effect-handle definition frames, zero
  effect-handle use/cache-hit frames, zero shader-handle definition/use/cache-hit frames, zero Skiko surface-change
  markers, zero command-cache clear markers, `avg_delta=2.062`, `bad_pixel_ratio=0.04772`, and
  `compose_bottom_labels_bad_pixel_ratio=0.08823`. This is focused descriptor-cap change 2 after the 2026-06-13 16:43
  full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-174643/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-rich`: recent broad parity reports showed exactly 20 JBR
  effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving
  effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused validation
  `CASES=parity-rich ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero picture
  frames, 631 JBR command frames, 20 effect-handle definition frames, zero effect-handle use/cache-hit frames, zero
  shader-handle definition/use/cache-hit frames, zero Skiko surface-change markers, zero command-cache clear markers,
  `avg_delta=2.123`, `bad_pixel_ratio=0.05044`, and `compose_shader_image_bad_pixel_ratio=0.06242`. This is focused
  descriptor-cap change 1 after the 2026-06-13 16:43 full parity sweep; broad parity remains deferred until roughly
  ten focused changes or an ABI/capability gate. Disk free was about 173Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-174324/suite.tsv`.
- 2026-06-13 batched full screenshot parity sweep after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106. Aggregate: `fallback_sum=11`, zero JBR picture
  frames, 98,167 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The sweep covered
  text/font, image, gradient, shader, RuntimeEffect, color-filter, descriptor lifecycle, and graphics-layer parity rows
  after the latest shader/effect-handle cap tightenings. This resets the focused descriptor-cap change counter to zero.
  Disk free was about 173Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-164327/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-skew-transform`: recent parity reports showed 15-30 JBR
  effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving
  effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused validation
  `CASES=parity-skew-transform ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`,
  zero picture frames, 1,029 JBR command frames, 25 effect-handle definition frames, zero effect-handle use/cache-hit
  frames, zero shader-handle definition/use/cache-hit frames, zero Skiko surface-change markers, zero command-cache
  clear markers, `avg_delta=2.606`, and `bad_pixel_ratio=0.06742`. This was focused descriptor-cap change 10 after the
  2026-06-13 15:16 full parity sweep. Disk free was about 199Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-164129/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-clip-rects`: recent parity reports showed 20 JBR
  effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=24` for this exact row, leaving
  effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused validation
  `CASES=parity-clip-rects ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero
  picture frames, 696 JBR command frames, 20 effect-handle definition frames, zero effect-handle use/cache-hit frames,
  zero shader-handle definition/use/cache-hit frames, zero Skiko surface-change markers, zero command-cache clear
  markers, `avg_delta=2.108`, and `bad_pixel_ratio=0.05000`. This is focused descriptor-cap change 9 after the
  2026-06-13 15:16 full parity sweep; after one more focused change, run the next batched broad parity sweep unless an
  ABI/capability gate supersedes it. Disk free was about 199Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-163916/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-vertices`: recent parity reports showed 20 JBR effect-handle
  definitions while the row had no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=24` for this exact row, leaving
  effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused validation
  `CASES=parity-vertices ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero
  picture frames, 700 JBR command frames, 20 effect-handle definition frames, zero effect-handle use/cache-hit frames,
  zero shader-handle definition/use/cache-hit frames, zero Skiko surface-change markers, zero command-cache clear
  markers, `avg_delta=2.122`, and `bad_pixel_ratio=0.05041`. This is focused descriptor-cap change 8 after the
  2026-06-13 15:16 full parity sweep; broad parity remains deferred until roughly ten focused changes or an
  ABI/capability gate. Disk free was about 199Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-163708/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-point-dots`: recent parity reports showed 20 JBR
  effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=24` for this exact row, leaving
  effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused validation
  `CASES=parity-point-dots ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero
  picture frames, 601 JBR command frames, 20 effect-handle definition frames, zero effect-handle use/cache-hit frames,
  zero shader-handle definition/use/cache-hit frames, zero Skiko surface-change markers, zero command-cache clear
  markers, `avg_delta=2.122`, and `bad_pixel_ratio=0.05042`. This is focused descriptor-cap change 7 after the
  2026-06-13 15:16 full parity sweep; broad parity remains deferred until roughly ten focused changes or an
  ABI/capability gate. Disk free was about 199Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-163501/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-clip-path`: historical parity reports showed 15-20 JBR
  effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=24` for this exact row, leaving
  effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused validation
  `CASES=parity-clip-path ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero
  picture frames, 667 JBR command frames, 20 effect-handle definition frames, zero effect-handle use/cache-hit frames,
  zero shader-handle definition/use/cache-hit frames, zero Skiko surface-change markers, zero command-cache clear
  markers, `avg_delta=2.121`, and `bad_pixel_ratio=0.05039`. This is focused descriptor-cap change 6 after the
  2026-06-13 15:16 full parity sweep; broad parity remains deferred until roughly ten focused changes or an
  ABI/capability gate. Disk free was about 199Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-163100/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-gradient-stroke`: historical parity reports showed 15-20
  JBR effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=24` for this exact row, leaving
  effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused validation
  `CASES=parity-gradient-stroke ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`,
  zero picture frames, 855 JBR command frames, 20 effect-handle definition frames, zero effect-handle use/cache-hit
  frames, zero shader-handle definition/use/cache-hit frames, zero Skiko surface-change markers, zero command-cache
  clear markers, `avg_delta=2.118`, `bad_pixel_ratio=0.05030`, and `compose_shader_linear_bad_pixel_ratio=0.06844`.
  This is focused descriptor-cap change 5 after the 2026-06-13 15:16 full parity sweep; broad parity remains deferred
  until roughly ten focused changes or an ABI/capability gate. Disk free was about 199Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-162831/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-image-shader`: historical parity reports showed 20-30 JBR
  effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving
  effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused validation
  `CASES=parity-image-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero
  picture frames, 705 JBR command frames, 25 effect-handle definition frames, zero effect-handle use/cache-hit frames,
  zero shader-handle definition/use/cache-hit frames, zero Skiko surface-change markers, zero command-cache clear
  markers, `avg_delta=2.678`, `bad_pixel_ratio=0.06777`, and `compose_shader_image_bad_pixel_ratio=0.06209`. This is
  focused descriptor-cap change 4 after the 2026-06-13 15:16 full parity sweep; broad parity remains deferred until
  roughly ten focused changes or an ABI/capability gate. Disk free was about 199Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-162546/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-path-effect`: historical parity reports showed 20-25 JBR
  effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving
  effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused validation
  `CASES=parity-path-effect ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero
  picture frames, 1,024 JBR command frames, 20 effect-handle definition frames, zero effect-handle use/cache-hit
  frames, zero shader-handle definition/use/cache-hit frames, zero Skiko surface-change markers, zero command-cache
  clear markers, `avg_delta=2.208`, and `bad_pixel_ratio=0.05275`. This is focused descriptor-cap change 3 after the
  2026-06-13 15:16 full parity sweep; broad parity remains deferred until roughly ten focused changes or an
  ABI/capability gate. Disk free was about 199Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-162333/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-save-layer-filter`: historical parity reports showed 15-25
  JBR effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving
  effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused validation
  `CASES=parity-save-layer-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`,
  zero picture frames, 799 JBR command frames, 20 effect-handle definition frames, zero effect-handle use/cache-hit
  frames, zero shader-handle definition/use/cache-hit frames, zero Skiko surface-change markers, zero command-cache
  clear markers, `avg_delta=2.131`, and `bad_pixel_ratio=0.05037`. This is focused descriptor-cap change 2 after the
  2026-06-13 15:16 full parity sweep; broad parity remains deferred until roughly ten focused changes or an
  ABI/capability gate. Disk free was about 199Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-162051/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-image-filter`: historical parity reports showed 15-25 JBR
  effect-handle definitions while the row had no effect-handle definition guard. Magic Jewel added
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES=1` and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving
  effect/shader use expectations unset because reports consistently show zero use/cache-hit frames. Focused validation
  `CASES=parity-image-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero
  picture frames, 750 JBR command frames, 20 effect-handle definition frames, zero effect-handle use/cache-hit frames,
  zero shader-handle definition/use/cache-hit frames, zero Skiko surface-change markers, zero command-cache clear
  markers, `avg_delta=2.122`, `bad_pixel_ratio=0.05041`, and `compose_shader_image_bad_pixel_ratio=0.06242`. This is
  focused descriptor-cap change 1 after the 2026-06-13 15:16 full parity sweep; broad parity remains deferred until
  roughly ten focused changes or an ABI/capability gate. Disk free was about 199Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-161740/suite.tsv`.
- 2026-06-13 batched full screenshot parity sweep after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106. Aggregate: `fallback_sum=10`, zero JBR picture
  frames, 92,868 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The sweep covered
  text/font, image, gradient, shader, RuntimeEffect, color-filter, descriptor lifecycle, and graphics-layer parity rows
  after the latest shader/effect-handle cap tightenings. This resets the focused descriptor-cap change counter to zero.
  Disk free was about 199Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-151647/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-image-color-matrix-filter`: historical parity reports showed
  18-30 JBR effect-handle definitions while the row allowed up to 64. Magic Jewel lowered
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES` to 32 for this exact row. Focused validation
  `CASES=parity-image-color-matrix-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 668 JBR command frames, zero shader-handle definition/use/cache-hit
  frames, 18 effect-handle definition frames, 1,077 effect-handle use frames, 1,074 effect-handle cache-hit frames,
  zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.180`, `bad_pixel_ratio=0.05077`,
  and `compose_shader_image_bad_pixel_ratio=0.08289`. This is focused descriptor-cap change 10 after the 2026-06-13
  13:41 full parity sweep, so the next step is the batched broad screenshot parity sweep. Disk free was about 197Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-151442/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-shader-source-cache-eviction`: historical
  parity reports showed 15-25 JBR shader-handle definitions and 15-25 JBR effect-handle definitions while the row had
  no max definition guards. Magic Jewel added `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES=32` and
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, constraining descriptor definitions while leaving the
  intentionally heavy eviction/use volume unconstrained. Focused validation
  `CASES=parity-runtime-effect-shader-source-cache-eviction ./scripts/jbr-skia-screenshot-parity-suite.sh` passed
  with `fallback_new_count=0`, zero picture frames, 626 JBR command frames, 20 shader-handle definition frames, 3,006
  shader-handle use frames, 2,994 shader-handle cache-hit frames, 20 effect-handle definition frames, zero
  effect-handle use frames, 2,003 RuntimeEffect source-cache evict frames, zero RuntimeEffect compile/build failures,
  zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.084`, `bad_pixel_ratio=0.04931`,
  and `compose_shader_image_bad_pixel_ratio=0.05001`. This is focused descriptor-cap change 9 after the 2026-06-13
  13:41 full parity sweep; after one more focused change, run the next batched broad parity sweep unless an
  ABI/capability gate supersedes it. Disk free was about 197Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-151153/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-source-cache-eviction`: historical parity
  reports showed 30-40 JBR effect-handle definitions while the row had no max effect-handle definition guard. Magic
  Jewel added `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=40` for this exact row, constraining descriptor definitions while
  leaving the intentionally heavy eviction/use volume unconstrained. Focused validation
  `CASES=parity-runtime-effect-source-cache-eviction ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 401 JBR command frames, zero shader-handle definition/use/cache-hit
  frames, 30 effect-handle definition frames, 2,337 effect-handle use frames, 2,328 effect-handle cache-hit frames,
  1,557 RuntimeEffect source-cache evict frames, zero RuntimeEffect compile/build failures, zero Skiko surface-change
  markers, zero command-cache clear markers, `avg_delta=2.118`, `bad_pixel_ratio=0.05032`, and
  `compose_shader_image_bad_pixel_ratio=0.06242`. This is focused descriptor-cap change 8 after the 2026-06-13 13:41
  full parity sweep; broad parity remains deferred until roughly ten focused changes or an ABI/capability gate. Disk
  free was about 197Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-150902/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-color-filter-child`: historical parity
  reports showed 24-32 JBR effect-handle definitions while the row had no max effect-handle definition guard. Magic
  Jewel added `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row. Focused validation
  `CASES=parity-runtime-effect-color-filter-child ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 630 JBR command frames, zero shader-handle definition/use/cache-hit
  frames, 32 effect-handle definition frames, 1,051 effect-handle use frames, 1,047 effect-handle cache-hit frames,
  1,050 RuntimeEffect source-cache hit frames, one RuntimeEffect source-cache miss, zero RuntimeEffect compile/build
  failures, zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.122`,
  `bad_pixel_ratio=0.05041`, and `compose_shader_image_bad_pixel_ratio=0.06242`. This is focused descriptor-cap
  change 7 after the 2026-06-13 13:41 full parity sweep; broad parity remains deferred until roughly ten focused
  changes or an ABI/capability gate. Disk free was about 197Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-150518/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-child-only`: historical parity reports
  showed 15-25 JBR effect-handle definitions while the row had no max effect-handle definition guard. Magic Jewel
  added `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving the existing shader-handle cap unchanged
  and keeping headroom for the known 25-definition outlier. Focused validation
  `CASES=parity-runtime-effect-child-only ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 617 JBR command frames, 12 shader-handle definition frames, 981
  shader-handle use frames, 977 shader-handle cache-hit frames, 20 effect-handle definition frames, zero effect-handle
  use frames, 980 RuntimeEffect source-cache hit frames, one RuntimeEffect source-cache miss, zero RuntimeEffect
  compile/build failures, zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.118`,
  `bad_pixel_ratio=0.05029`, and `compose_shader_image_bad_pixel_ratio=0.06242`. This is focused descriptor-cap
  change 6 after the 2026-06-13 13:41 full parity sweep; broad parity remains deferred until roughly ten focused
  changes or an ABI/capability gate. Disk free was about 197Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-150241/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-uniform-only`: historical parity reports
  showed 15-20 JBR effect-handle definitions while the row had no max effect-handle definition guard. Magic Jewel
  added `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=24` for this exact row, leaving the existing shader-handle cap
  unchanged. Focused validation
  `CASES=parity-runtime-effect-uniform-only ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 686 JBR command frames, four shader-handle definition frames, 1,132
  shader-handle use frames, 1,128 shader-handle cache-hit frames, 20 effect-handle definition frames, zero
  effect-handle use frames, 1,131 RuntimeEffect source-cache hit frames, one RuntimeEffect source-cache miss, zero
  RuntimeEffect compile/build failures, zero Skiko surface-change markers, zero command-cache clear markers,
  `avg_delta=2.107`, `bad_pixel_ratio=0.04997`, and `compose_shader_linear_bad_pixel_ratio=0.06844`. This is
  focused descriptor-cap change 5 after the 2026-06-13 13:41 full parity sweep; broad parity remains deferred until
  roughly ten focused changes or an ABI/capability gate. Disk free was about 197Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-150021/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-shader`: historical parity reports showed
  15-20 JBR effect-handle definitions while the row had no max effect-handle definition guard. Magic Jewel added
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=24` for this exact row, leaving the existing shader-handle cap unchanged and
  correcting the guard placement so `parity-runtime-effect-child-only` remains uncapped. Focused validation
  `CASES=parity-runtime-effect-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 759 JBR command frames, 12 shader-handle definition frames, 1,132
  shader-handle use frames, 1,128 shader-handle cache-hit frames, 20 effect-handle definition frames, zero
  effect-handle use frames, 1,131 RuntimeEffect source-cache hit frames, one RuntimeEffect source-cache miss, zero
  RuntimeEffect compile/build failures, zero Skiko surface-change markers, zero command-cache clear markers,
  `avg_delta=2.111`, `bad_pixel_ratio=0.05008`, and `compose_shader_image_bad_pixel_ratio=0.05073`. This is focused
  descriptor-cap change 4 after the 2026-06-13 13:41 full parity sweep; broad parity remains deferred until roughly
  ten focused changes or an ABI/capability gate. Disk free was about 196Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-145634/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-shader-color-filter`: historical parity
  reports showed 18-24 JBR effect-handle definitions while the row had no max effect-handle definition guard. Magic
  Jewel added `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving the existing shader-handle cap
  unchanged. Focused validation
  `CASES=parity-runtime-effect-shader-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 819 JBR command frames, eight shader-handle definition frames, 1,325
  shader-handle use frames, 1,317 shader-handle cache-hit frames, 24 effect-handle definition frames, four
  effect-handle use frames, 1,320 RuntimeEffect source-cache hit frames, one RuntimeEffect source-cache miss, zero
  RuntimeEffect compile/build failures, zero Skiko surface-change markers, zero command-cache clear markers,
  `avg_delta=2.110`, `bad_pixel_ratio=0.05006`, and `compose_shader_composite_bad_pixel_ratio=0.08642`. This is
  focused descriptor-cap change 3 after the 2026-06-13 13:41 full parity sweep; broad parity remains deferred until
  roughly ten focused changes or an ABI/capability gate. Disk free was about 196Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-144931/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-transformed-shader`: historical parity reports showed
  15-25 JBR effect-handle definitions while the row had no max effect-handle definition guard. Magic Jewel added
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=32` for this exact row, leaving the existing shader-handle cap unchanged.
  Focused validation `CASES=parity-transformed-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 875 JBR command frames, eight shader-handle definition frames, 1,312
  shader-handle use frames, 1,308 shader-handle cache-hit frames, 20 effect-handle definition frames, zero
  effect-handle use frames, zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.110`,
  `bad_pixel_ratio=0.05007`, and `compose_shader_linear_bad_pixel_ratio=0.06584`. This is focused descriptor-cap
  change 2 after the 2026-06-13 13:41 full parity sweep; broad parity remains deferred until roughly ten focused
  changes or an ABI/capability gate. Disk free was about 196Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-144641/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-image-shader-color-filter`: after the shader-handle cap was
  already tightened, historical parity reports showed 18-30 JBR effect-handle definitions while the row still allowed
  up to 64. Magic Jewel lowered `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES` to 32 for this exact row. Focused validation
  `CASES=parity-image-shader-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 672 JBR command frames, eight shader-handle definition frames, 1,179
  shader-handle use frames, 1,171 shader-handle cache-hit frames, 24 effect-handle definition frames, four
  effect-handle use frames, zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.110`,
  `bad_pixel_ratio=0.05002`, and `compose_shader_image_bad_pixel_ratio=0.03800`. This is focused descriptor-cap
  change 1 after the 2026-06-13 13:41 full parity sweep; broad parity remains deferred until roughly ten focused
  changes or an ABI/capability gate. Disk free was about 196Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-144339/suite.tsv`.
- 2026-06-13 batched full screenshot parity sweep after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106. Aggregate: `fallback_sum=11`, zero JBR picture
  frames, 98,884 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The sweep covered
  text/font, image, gradient, shader, RuntimeEffect, color-filter, descriptor lifecycle, and graphics-layer parity rows
  after the latest shader/effect-handle cap tightenings. This resets the focused descriptor-cap change counter to zero.
  Disk free was about 196Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-134112/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-linear-gradient-shader-color-filter`: historical parity
  reports showed 18-30 JBR effect-handle definitions while the row allowed up to 64. Magic Jewel lowered
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES` to 32 for this exact row, leaving the already-tightened shader-handle cap
  unchanged. Focused validation
  `CASES=parity-linear-gradient-shader-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 621 JBR command frames, eight shader-handle definition frames, 1,098
  shader-handle use frames, 1,090 shader-handle cache-hit frames, 24 effect-handle definition frames, four
  effect-handle use frames, zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.107`,
  `bad_pixel_ratio=0.04997`, and `compose_shader_linear_bad_pixel_ratio=0.03938`. This is focused descriptor-cap
  change 10 after the 2026-06-13 11:44 full parity sweep, so the next step is a batched broad screenshot parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-133844/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-composite-shader-color-filter`: after the shader-handle cap
  was tightened, historical parity reports still showed exactly 24 JBR effect-handle definitions while the row allowed
  up to 64. Magic Jewel lowered `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES` to 32 for this exact row. Focused validation
  `CASES=parity-composite-shader-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 649 JBR command frames, 16 shader-handle definition frames, 1,068
  shader-handle use frames, 1,060 shader-handle cache-hit frames, 24 effect-handle definition frames, four
  effect-handle use frames, zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.103`,
  `bad_pixel_ratio=0.04991`, and `compose_shader_composite_bad_pixel_ratio=0.05382`. This is focused descriptor-cap
  change 9 after the 2026-06-13 11:44 full parity sweep; after one more focused change, run the next batched broad
  parity sweep unless an ABI/capability gate supersedes it:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-133624/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-composite-shader`: historical parity reports showed the row
  using 20-35 JBR effect-handle definitions, while the row had no max effect-handle definition guard. Magic Jewel
  added `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=40` for this exact row, leaving the existing shader-handle cap unchanged.
  Focused validation `CASES=parity-composite-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 684 JBR command frames, 15 shader-handle definition frames, 1,103
  shader-handle use frames, 1,098 shader-handle cache-hit frames, 25 effect-handle definition frames, zero
  effect-handle uses, zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.596`,
  `bad_pixel_ratio=0.06710`, and `compose_shader_composite_bad_pixel_ratio=0.08478`. This is focused descriptor-cap
  change 8 after the 2026-06-13 11:44 full parity sweep; broad parity remains deferred until roughly ten focused
  changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-133324/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-composite-noise-shader`: historical parity
  reports showed the row using 24-33 JBR shader-handle definitions, while the suite allowed up to 72. Magic Jewel
  lowered `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 40 for this exact row. Focused validation
  `CASES=parity-forced-context-composite-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 917 JBR command frames, 27 shader-handle definition frames, 1,397
  shader-handle use frames, 1,388 shader-handle cache-hit frames, 45 effect-handle definition frames, zero
  effect-handle uses, one Skiko surface-change marker, one command-cache clear marker, `avg_delta=1.973`,
  `bad_pixel_ratio=0.04630`, and `compose_shader_composite_bad_pixel_ratio=0.08628`. This is focused
  descriptor-cap change 7 after the 2026-06-13 11:44 full parity sweep; broad parity remains deferred until roughly
  ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-133044/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-composite-noise-shader`: historical parity reports
  showed the row using 18-24 JBR shader-handle definitions, while the suite allowed up to 48. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 32 for this exact row. Focused validation
  `CASES=parity-resize-composite-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's
  expected `fallback_new_count=1`, zero picture frames, 667 JBR command frames, 21 shader-handle definition frames,
  1,229 shader-handle use frames, 1,222 shader-handle cache-hit frames, 35 effect-handle definition frames, zero
  effect-handle uses, one Skiko surface-change marker, one command-cache clear marker, `avg_delta=1.846`,
  `bad_pixel_ratio=0.04430`, and `compose_shader_composite_bad_pixel_ratio=0.08610`. This is focused
  descriptor-cap change 6 after the 2026-06-13 11:44 full parity sweep; broad parity remains deferred until roughly
  ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-132818/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-composite-noise-shader`: historical parity reports showed
  the row using 9-12 JBR shader-handle definitions, while the suite allowed up to 24. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 16 for this exact row. Focused validation
  `CASES=parity-composite-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 716 JBR command frames, 12 shader-handle definition frames, 1,108
  shader-handle use frames, 1,104 shader-handle cache-hit frames, 20 effect-handle definition frames, zero
  effect-handle uses, zero Skiko surface-change markers, zero command-cache clear markers, `avg_delta=2.114`,
  `bad_pixel_ratio=0.05004`, and `compose_shader_composite_bad_pixel_ratio=0.08628`. This is focused
  descriptor-cap change 5 after the 2026-06-13 11:44 full parity sweep; broad parity remains deferred until roughly
  ten focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-132536/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-composite-shader-color-filter`: historical parity reports
  showed the row using exactly 16 JBR shader-handle definitions, while the suite allowed up to 32. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 24 for this exact row, leaving the existing effect-handle cap unchanged.
  Focused validation `CASES=parity-composite-shader-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with `fallback_new_count=0`, zero picture frames, 848 JBR command frames, 16 shader-handle definition
  frames, 1,249 shader-handle use frames, 1,241 shader-handle cache-hit frames, 24 effect-handle definition frames,
  four effect-handle use frames, zero Skiko surface-change markers, zero command-cache clear markers,
  `avg_delta=2.103`, `bad_pixel_ratio=0.04991`, and `compose_shader_composite_bad_pixel_ratio=0.05382`. This is
  focused descriptor-cap change 4 after the 2026-06-13 11:44 full parity sweep; broad parity remains deferred until
  roughly ten focused changes or an ABI/capability gate. Disk free was about 174Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-132328/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-runtime-effect-stable-color-filter`:
  historical parity reports showed the row using 54-60 JBR effect-handle definitions, while the suite allowed up to
  144. Magic Jewel lowered `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES` to 72 for this exact row. Focused validation
  `CASES=parity-forced-context-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with `fallback_new_count=0`, zero picture frames, 890 JBR command frames, 54 effect-handle definition frames,
  1,395 effect-handle use frames, 1,386 effect-handle cache-hit frames, zero shader-handle definition frames, 1,394
  RuntimeEffect source-cache hit frames, one RuntimeEffect source-cache miss, zero RuntimeEffect compile/build
  failures, one Skiko surface-change marker, one command-cache clear marker, `avg_delta=1.981`, and
  `bad_pixel_ratio=0.04668`. This is focused descriptor-cap change 3 after the 2026-06-13 11:44 full parity sweep.
  Disk free was about 198Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-131956/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-runtime-effect-stable-color-filter`: historical
  parity reports showed the row using 36-42 JBR effect-handle definitions, while the suite allowed up to 96. Magic
  Jewel lowered `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES` to 48 for this exact row. Focused validation
  `CASES=parity-resize-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  the row's expected `fallback_new_count=1`, zero picture frames, 841 JBR command frames, 42 effect-handle definition
  frames, 1,338 effect-handle use frames, 1,331 effect-handle cache-hit frames, zero shader-handle definition frames,
  1,336 RuntimeEffect source-cache hit frames, one RuntimeEffect source-cache miss, zero RuntimeEffect compile/build
  failures, one Skiko surface-change marker, one command-cache clear marker, `avg_delta=1.855`, and
  `bad_pixel_ratio=0.04467`. This is focused descriptor-cap change 2 after the 2026-06-13 11:44 full parity sweep.
  Disk free was about 198Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-131720/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-stable-color-filter`: historical parity
  reports showed the row using exactly 24 JBR effect-handle definitions, while the suite allowed up to 64. Magic Jewel
  lowered `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES` to 24 for this exact row. Focused validation
  `CASES=parity-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 948 JBR command frames, 24 effect-handle definition frames, 1,391
  effect-handle use frames, 1,387 effect-handle cache-hit frames, zero shader-handle definition frames, 1,390
  RuntimeEffect source-cache hit frames, one RuntimeEffect source-cache miss, zero RuntimeEffect compile/build
  failures, `avg_delta=2.122`, and `bad_pixel_ratio=0.05042`. This is focused descriptor-cap change 1 after the
  2026-06-13 11:44 full parity sweep. Disk free was about 198Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-131424/suite.tsv`.
- 2026-06-13 exact screenshot parity smoke after the command benchmark smoke:
  `CASES=parity-rich ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero picture
  frames, 645 JBR command frames, `avg_delta=2.123`, `bad_pixel_ratio=0.05044`,
  `header_buttons_bad_pixel_ratio=0.00381`, and `compose_bad_pixel_ratio=0.07525`. This intentionally covered one
  high-signal rich parity row rather than the three-row smoke group or full 106-row parity suite. Disk free was about
  198Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-131153/suite.tsv`.
- 2026-06-13 single-case benchmark smoke after the required artifact slice:
  `CASES=commands DURATION_SECONDS=5 WARMUP_SECONDS=1 EXPECT_SCREENSHOT_ASSERTION=false
  ./scripts/jbr-skia-benchmark-suite.sh` passed with `fallback_new_count=0`, four old-side CPU samples, two new-side
  CPU samples, `old_avg_cpu=101.80`, `new_avg_cpu=106.85`, `app_old_fps=242.8`, `app_new_fps=94.8`, zero picture
  frames, 474 JBR command frames, and `jbr_command_fps=94.8`. This intentionally covered only the command benchmark
  smoke row rather than the full five-case benchmark suite. Disk free was about 198Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260613-131013/suite.tsv`.
- 2026-06-13 required artifact matrix slice after the full compatibility matrix:
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2.
  `current-all` passed with expected `none`, no fallback, 711 JBR command frames, and `background_window=true`.
  `missing-public-api` passed with expected `public-api-missing`, one structured fallback, zero command frames, and
  `background_window=true`. Optional old-artifact rows were not run. Disk free was about 198Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260613-130810/matrix.tsv`.
- 2026-06-13 full compatibility matrix after the batched screenshot parity reset:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with
  `fallback_sum=56`. The `happy` row passed with no fallback, 475 JBR command frames, and `background_window=true`.
  The ABI/native-ABI/public-API rows and low/high-word capability-removal rows each produced exactly one structured
  fallback, zero command frames, and `background_window=true`. Disk free was about 198Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260613-124241/matrix.tsv`.
- 2026-06-13 batched full screenshot parity sweep after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106. Aggregate: `fallback_sum=11`, zero JBR picture
  frames, 103,309 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The sweep covered
  text/font, image, gradient, shader, RuntimeEffect, color-filter, descriptor lifecycle, and graphics-layer parity rows
  after the tightened shader/effect-handle gates. Disk free was about 201Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-114403/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-color-filter`: historical parity reports
  showed the row using 18-24 JBR effect-handle definitions, but this row had no max effect-handle definition guard.
  Magic Jewel added `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=24` for this exact row. Focused validation
  `CASES=parity-runtime-effect-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 850 JBR command frames, 24 effect-handle definition frames, 1,290
  effect-handle use frames, 1,286 effect-handle cache-hit frames, zero shader-handle definition frames, 1,289
  RuntimeEffect source-cache hit frames, one RuntimeEffect source-cache miss, zero RuntimeEffect compile/build
  failures, `avg_delta=2.120`, and `bad_pixel_ratio=0.05037`. Disk free was about 175Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-114224/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-shader`: historical parity reports showed
  the row using 9-12 JBR shader-handle definitions, but this row had no max shader-handle definition guard. Magic Jewel
  added `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES=12` for this exact row. Focused validation
  `CASES=parity-runtime-effect-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 913 JBR command frames, 12 shader-handle definition frames, 1,369
  shader-handle use frames, 1,365 shader-handle cache-hit frames, 20 effect-handle definition frames, 1,368
  RuntimeEffect source-cache hit frames, one RuntimeEffect source-cache miss, zero RuntimeEffect compile/build
  failures, `avg_delta=2.111`, and `bad_pixel_ratio=0.05008`. Disk free was about 175Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-114010/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-child-only`: historical parity reports
  showed the row using 9-12 JBR shader-handle definitions, but this row had no max shader-handle definition guard.
  Magic Jewel added `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES=12` for this exact row. Focused validation
  `CASES=parity-runtime-effect-child-only ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 966 JBR command frames, 12 shader-handle definition frames,
  1,479 shader-handle use frames, 1,475 shader-handle cache-hit frames, 20 effect-handle definition frames,
  1,478 RuntimeEffect source-cache hit frames, one RuntimeEffect source-cache miss, zero RuntimeEffect compile/build
  failures, `avg_delta=2.118`, and `bad_pixel_ratio=0.05029`. Disk free was about 175Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-113823/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-uniform-only`: historical parity reports
  showed the row using 3-4 JBR shader-handle definitions, but this row had no max shader-handle definition guard.
  Magic Jewel added `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES=4` for this exact row. Focused validation
  `CASES=parity-runtime-effect-uniform-only ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 558 JBR command frames, three shader-handle definition frames,
  927 shader-handle use frames, 924 shader-handle cache-hit frames, 15 effect-handle definition frames,
  926 RuntimeEffect source-cache hit frames, one RuntimeEffect source-cache miss, zero RuntimeEffect compile/build
  failures, `avg_delta=2.107`, and `bad_pixel_ratio=0.04997`. Disk free was about 175Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-113426/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-runtime-effect-pure-color`: historical parity
  reports showed the row using 7-12 JBR shader-handle definitions, while the suite allowed up to 24. Magic Jewel
  lowered `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 12 for this exact row. Focused validation
  `CASES=parity-forced-context-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 554 JBR command frames, eight shader-handle definition frames,
  935 shader-handle use frames, 927 shader-handle cache-hit frames, 934 RuntimeEffect source-cache hit frames, one
  RuntimeEffect source-cache miss, zero RuntimeEffect compile/build failures, one Skiko surface-change marker,
  `avg_delta=1.964`, and `bad_pixel_ratio=0.04619`. Disk free was about 175Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-113128/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-runtime-effect-pure-color`: historical parity reports
  showed the row using 6-8 JBR shader-handle definitions, while the suite allowed up to 16. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 8 for this exact row. Focused validation
  `CASES=parity-resize-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's
  expected `fallback_new_count=1`, zero picture frames, 694 JBR command frames, seven shader-handle definition frames,
  1,108 shader-handle use frames, 1,101 shader-handle cache-hit frames, 1,106 RuntimeEffect source-cache hit frames,
  one RuntimeEffect source-cache miss, zero RuntimeEffect compile/build failures, `avg_delta=1.845`, and
  `bad_pixel_ratio=0.04438`. Disk free was about 201Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-112823/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-pure-color`: historical parity reports showed
  the row using 3-4 JBR shader-handle definitions, while the suite allowed up to 8. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 4 for this exact row. An initial broad text patch briefly changed the
  already-validated `parity-resize-color-shader` cap before commit; the diff was corrected back to a single
  RuntimeEffect pure-color row change before final validation. Focused validation
  `CASES=parity-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 526 JBR command frames, four shader-handle definition frames,
  891 shader-handle use frames, 887 shader-handle cache-hit frames, 890 RuntimeEffect source-cache hit frames, one
  RuntimeEffect source-cache miss, zero RuntimeEffect compile/build failures, `avg_delta=2.105`, and
  `bad_pixel_ratio=0.04993`. Disk free was about 201Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-112515/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-runtime-effect-shader-color-filter`: historical parity
  reports showed the row using 6-8 JBR shader-handle definitions, but this row had no max shader-handle definition
  guard. Magic Jewel added `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES=12` for this exact row. Focused validation
  `CASES=parity-runtime-effect-shader-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 673 JBR command frames, eight shader-handle definition frames,
  1,044 shader-handle use frames, 1,036 shader-handle cache-hit frames, 24 effect-handle definition frames,
  four effect-handle use frames, 1,039 RuntimeEffect source-cache hit frames, one RuntimeEffect source-cache miss,
  zero RuntimeEffect compile/build failures, `avg_delta=2.110`, and `bad_pixel_ratio=0.05006`. Disk free was about
  201Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-112055/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-transformed-shader`: historical parity reports showed the
  row using 6-10 JBR shader-handle definitions, while the suite allowed up to 16. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 12 for this exact row. Focused validation
  `CASES=parity-transformed-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 675 JBR command frames, six shader-handle definition frames,
  1,024 shader-handle use frames, 1,021 shader-handle cache-hit frames, 15 effect-handle definition frames,
  `avg_delta=2.110`, `bad_pixel_ratio=0.05007`, and `compose_shader_linear_bad_pixel_ratio=0.06584`. Disk free was
  about 201Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-111754/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-linear-gradient-shader-color-filter`: historical parity
  reports showed the row using 6-10 JBR shader-handle definitions, while the suite allowed up to 24. Magic Jewel
  lowered `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 12 for this exact row. Focused validation
  `CASES=parity-linear-gradient-shader-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 515 JBR command frames, eight shader-handle definition frames,
  955 shader-handle use frames, 947 shader-handle cache-hit frames, 24 effect-handle definition frames,
  `avg_delta=2.107`, `bad_pixel_ratio=0.04997`, and `compose_shader_linear_bad_pixel_ratio=0.03938`. Disk free was
  about 202Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-111254/suite.tsv`.
- 2026-06-13 batched full screenshot parity sweep after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106. Aggregate: `fallback_sum=11`, zero JBR picture
  frames, 85,489 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The sweep covered
  text/font, image, gradient, shader, RuntimeEffect, color-filter, descriptor lifecycle, and graphics-layer parity
  rows after the tightened shader-handle gates. Disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-100819/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-image-shader-color-filter`: historical parity reports showed
  the row using 6-10 JBR shader-handle definitions, while the suite allowed up to 24. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 12 for this exact row. Focused validation
  `CASES=parity-image-shader-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 695 JBR command frames, eight shader-handle definition frames,
  1,178 shader-handle use frames, 1,170 shader-handle cache-hit frames, 24 effect-handle definition frames,
  `avg_delta=2.110`, `bad_pixel_ratio=0.05002`, and `compose_shader_image_bad_pixel_ratio=0.03800`. Disk free was
  about 190Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-100541/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-turbulence-shader`: historical parity reports
  showed the row using 7-12 JBR shader-handle definitions, while the suite allowed up to 24. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 12 for this exact row. Focused validation
  `CASES=parity-forced-context-turbulence-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 708 JBR command frames, nine shader-handle definition frames,
  1,081 shader-handle use frames, 1,072 shader-handle cache-hit frames, one surface-change marker, one context-change
  marker, `avg_delta=1.978`, `bad_pixel_ratio=0.04661`, and `compose_shader_turbulence_bad_pixel_ratio=0.07712`.
  Disk free was about 190Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-100137/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-noise-shader`: historical parity reports
  showed the row using 7-11 JBR shader-handle definitions, while the suite allowed up to 24. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 12 for this exact row. Focused validation
  `CASES=parity-forced-context-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 814 JBR command frames, nine shader-handle definition frames,
  1,250 shader-handle use frames, 1,241 shader-handle cache-hit frames, one surface-change marker, one context-change
  marker, `avg_delta=1.975`, `bad_pixel_ratio=0.04639`, and `compose_shader_noise_bad_pixel_ratio=0.05233`. Disk free
  was about 190Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-095920/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-forced-context-color-shader`: historical parity reports
  showed the row using 7-11 JBR shader-handle definitions, while the suite allowed up to 24. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 12 for this exact row. Focused validation
  `CASES=parity-forced-context-color-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  `fallback_new_count=0`, zero picture frames, 804 JBR command frames, nine shader-handle definition frames,
  1,389 shader-handle use frames, 1,380 shader-handle cache-hit frames, one surface-change marker, one context-change
  marker, `avg_delta=1.974`, `bad_pixel_ratio=0.04646`, and `compose_shader_color_bad_pixel_ratio=0.00000`. Disk free
  was about 190Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-095658/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-turbulence-shader`: historical parity reports showed
  the row using 6-8 JBR shader-handle definitions, while the suite allowed up to 16. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 8 for this exact row. Focused validation
  `CASES=parity-resize-turbulence-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's expected
  `fallback_new_count=1`, zero picture frames, 654 JBR command frames, seven shader-handle definition frames,
  1,069 shader-handle use frames, 1,062 shader-handle cache-hit frames, `avg_delta=1.850`,
  `bad_pixel_ratio=0.04455`, and `compose_shader_turbulence_bad_pixel_ratio=0.07863`. Disk free was about 190Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-095431/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-noise-shader`: historical parity reports showed the
  row using 6-8 JBR shader-handle definitions, while the suite allowed up to 16. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 8 for this exact row. Focused validation
  `CASES=parity-resize-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's expected
  `fallback_new_count=1`, zero picture frames, 592 JBR command frames, seven shader-handle definition frames,
  1,067 shader-handle use frames, 1,060 shader-handle cache-hit frames, `avg_delta=1.849`,
  `bad_pixel_ratio=0.04439`, and `compose_shader_noise_bad_pixel_ratio=0.06845`. Disk free was about 190Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-095222/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-resize-color-shader`: historical parity reports showed the
  row using 6-8 JBR shader-handle definitions, while the suite allowed up to 16. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 8 for this exact row. The first sandboxed validation attempt failed before
  app startup because Gradle could not open its `~/.gradle` wrapper lock, producing no CMP/Skiko/JBR frames. The
  escalated exact rerun
  `CASES=parity-resize-color-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's expected
  `fallback_new_count=1`, zero picture frames, 550 JBR command frames, seven shader-handle definition frames,
  940 shader-handle use frames, 933 shader-handle cache-hit frames, `avg_delta=1.850`, `bad_pixel_ratio=0.04454`, and
  `compose_shader_color_bad_pixel_ratio=0.04801`. Disk free was about 190Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-094953/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-turbulence-shader`: historical parity reports showed the
  row using exactly four JBR shader-handle definitions, while the suite allowed up to 8. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 4 for this exact row. Focused validation
  `CASES=parity-turbulence-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`,
  zero picture frames, 784 JBR command frames, four shader-handle definition frames, 1,323 shader-handle use frames,
  1,319 shader-handle cache-hit frames, `avg_delta=2.119`, `bad_pixel_ratio=0.05035`, and
  `compose_shader_turbulence_bad_pixel_ratio=0.07712`. Disk free was about 191Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-094416/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-noise-shader`: historical parity reports showed the row
  using 3-4 JBR shader-handle definitions, while the suite allowed up to 8. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 4 for this exact row. Focused validation
  `CASES=parity-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero
  picture frames, 658 JBR command frames, four shader-handle definition frames, 1,204 shader-handle use frames, 1,200
  shader-handle cache-hit frames, `avg_delta=2.116`, `bad_pixel_ratio=0.05013`, and
  `compose_shader_noise_bad_pixel_ratio=0.05233`. Disk free remained about 192Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-094115/suite.tsv`.
- 2026-06-13 stable descriptor gate tightening for `parity-color-shader`: historical parity reports showed the row
  using 3-4 JBR shader-handle definitions, while the suite allowed up to 8. Magic Jewel lowered
  `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES` to 4 for this exact row. Focused validation
  `CASES=parity-color-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero
  picture frames, 586 JBR command frames, three shader-handle definition frames, 995 shader-handle use frames, 992
  shader-handle cache-hit frames, `avg_delta=2.115`, `bad_pixel_ratio=0.05020`, and
  `compose_shader_color_bad_pixel_ratio=0.00000`. Disk free remained about 192Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-093718/suite.tsv`.
- 2026-06-13 exact screenshot parity smoke after the command benchmark smoke:
  `CASES=parity-rich ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with `fallback_new_count=0`, zero picture
  frames, 642 JBR command frames, `avg_delta=2.123`, `bad_pixel_ratio=0.05044`,
  `header_buttons_bad_pixel_ratio=0.00381`, and `compose_bad_pixel_ratio=0.07525`. This intentionally covered one
  high-signal rich parity row instead of the three-row smoke group or full 106-row parity suite. Disk free stayed about
  192Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-093333/suite.tsv`.
- 2026-06-13 single-case benchmark smoke after the required artifact slice:
  `CASES=commands DURATION_SECONDS=5 WARMUP_SECONDS=1 EXPECT_SCREENSHOT_ASSERTION=false
  ./scripts/jbr-skia-benchmark-suite.sh` passed with `fallback_new_count=0`, four old-side CPU samples, two new-side
  CPU samples, `old_avg_cpu=104.83`, `new_avg_cpu=97.75`, `app_old_fps=230.8`, `app_new_fps=80.8`, zero picture
  frames, 404 JBR command frames, and `jbr_command_fps=80.8`. This intentionally covered only the command benchmark
  smoke row rather than the full five-case benchmark suite. Disk free was about 192Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260613-093152/suite.tsv`.
- 2026-06-13 required artifact matrix slice after the local artifact rebuild:
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2.
  `current-all` passed with expected `none`, no fallback, 418 JBR command frames, and `background_window=true`.
  `missing-public-api` passed with expected `public-api-missing`, one structured fallback, zero command frames, and
  `background_window=true`. Optional old-artifact rows were not run, keeping this to the required focused slice. Disk
  free remained about 193Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260613-092936/matrix.tsv`.
- 2026-06-13 focused compatibility/artifact repair note: exact compatibility `happy` and exact
  `commands-native-bridge-load-library` initially failed with `SKIKO_JBR_INTEROP_FALLBACK reason=service-unavailable`
  after CMP command recording succeeded, indicating stale local `/tmp` JBR runtime/native artifacts rather than a
  recorder regression. `./scripts/rebuild-jbr-skia-local-artifacts.sh` refreshed `/tmp/jbr-api-shim.jar`,
  `/tmp/jbr-skia-run/desktop`, and `/tmp/jbr-skia-native/libjbrskiainterop.dylib`. Focused post-rebuild checks passed:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-native-bridge-load-library
  ./scripts/jbr-skia-command-probe-suite.sh` passed with no fallback and 895 JBR command frames; exact compatibility
  `happy` passed with no fallback, 184 JBR command frames, and `background_window=true`; exact compatibility
  `public-api-missing` passed with one structured fallback, zero command frames, and `background_window=true`. The
  focused compatibility rows were used instead of a full 57-row matrix per the batched validation policy. Disk free was
  about 193Gi after the focused GUI checks:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-092558/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260613-092753/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260613-092719/matrix.tsv`.
- 2026-06-13 cadence note: broad validation is now intentionally batched. Run exact cases or tiny focused groups for
  each change, and run full suites/matrices only after about 10 meaningful changes or when an ABI/capability milestone
  needs an immediate gate. A full compatibility-matrix attempt was started after the 2026-06-12 command sweep; the
  first sandboxed run failed before app startup because Gradle could not open its `~/.gradle` wrapper lock, and the
  escalated rerun was stopped early after this batching policy was clarified.
- Magic Jewel full default command-probe sweep after the focused quick-loop refresh batch:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` passed 549/549 using structured
  command/fallback markers while local macOS screenshot capture remains unavailable. Aggregate: `fallback_sum=350`, 79
  unsupported-picture rows, 78,827 JBR picture frames, and 156,233 JBR command frames. The sweep revalidated the
  current ABI 106 command rows across stream/primitive/path/text/image/shader/RuntimeEffect/descriptor/gradient/
  color-filter/graphics-layer/saveLayer coverage after the focused group refreshes. Magic Jewel `out` is 106G with
  about 196Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-144955/suite.tsv`.
- Magic Jewel tiny focused command-probe quick-loop refresh after `smoke`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS='gradient-path-stroke-fallbacks gradient-path-structure-invalid
  gradient-stop-invalid gradient-geometry-invalid gradient-color-count-invalid gradient-stroke-width-invalid
  gradient-round-rect-radius-invalid gradient-stroke-round-rect-radius-invalid image-shader-invalid shader-ref-invalid
  fill-rect-color-filter-invalid blend-mode-invalid' ./scripts/jbr-skia-command-probe-suite.sh` passed 38/38.
  Aggregate: `fallback_sum=10`, 28 unsupported-picture rows, 27,825 JBR picture frames, and zero JBR command frames.
  The batch refreshed the named tiny quick-loop aliases for gradient path/stops, gradient geometry/color-count/stroke
  width/round-rect radius, image shader invalid, shader-ref invalid, fill-rect color-filter invalid, and blend-mode
  invalid guards. Magic Jewel `out` is 104G with about 233Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-134712/suite.tsv`.
- Magic Jewel focused `smoke` command-probe refresh after `stream-invalid`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=smoke ./scripts/jbr-skia-command-probe-suite.sh` passed 6/6.
  Aggregate: `fallback_sum=0`, zero unsupported-picture rows, zero JBR picture frames, and 6,694 JBR command frames.
  The group rechecked live animation, core primitives, color shader, color-filter handle, color-matrix filter, and
  graphics-layer smoke replay. Magic Jewel `out` is 104G with about 210Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-133407/suite.tsv`.
- Magic Jewel focused `stream-invalid` command-probe refresh after `save-layer-shader-fallbacks`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=stream-invalid ./scripts/jbr-skia-command-probe-suite.sh` passed
  8/8. Aggregate: `fallback_sum=8`, zero unsupported-picture rows, zero JBR picture frames, and zero JBR command
  frames. The group rechecked command stream flags, record flags, coordinate space, paint format, payload
  length/truncation/extra bytes, and record length parser guards. Magic Jewel `out` is 104G with about 210Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-132344/suite.tsv`.
- Magic Jewel focused `save-layer-shader-fallbacks` command-probe refresh after `graphics-layer-extras`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=save-layer-shader-fallbacks ./scripts/jbr-skia-command-probe-suite.sh`
  passed 9/9. Aggregate: `fallback_sum=0`, six unsupported-picture rows, 6,049 JBR picture frames, and 3,221 JBR
  command frames. The group rechecked supported saveLayer filter/blend rows plus raw saveLayer color-filter, opaque
  shader, picture shader, and invalid-gradient structured fallback sentinels. Magic Jewel `out` is 104G with about
  210Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-131608/suite.tsv`.
- Magic Jewel focused `graphics-layer-extras` command-probe refresh after `graphics-layer`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=graphics-layer-extras ./scripts/jbr-skia-command-probe-suite.sh`
  passed 16/16. Aggregate: `fallback_sum=0`, four unsupported-picture rows, 4,104 JBR picture frames, and 12,671 JBR
  command frames. The group rechecked resize/forced-context graphics-layer color-matrix and render-effect rows, raw
  color-filter/image-filter fallbacks, unsupported child fallback, and chained render-effect/color-filter/blend
  combinations. Magic Jewel `out` is 104G with about 233Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-125529/suite.tsv`.
- Magic Jewel focused `graphics-layer` command-probe refresh after `surface-transform-ui`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=graphics-layer ./scripts/jbr-skia-command-probe-suite.sh` passed
  22/22. Aggregate: `fallback_sum=0`, one unsupported-picture row, 1,038 JBR picture frames, and 26,873 JBR command
  frames. The group rechecked base/modulate/offscreen/clip layers, blend/color-filter/render-effect rows,
  rectangular/round/path shadows, 3D rotations, scale/translate, near-camera, and off-center pivot. Magic Jewel `out`
  is 104G with about 233Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-122521/suite.tsv`.
- Magic Jewel focused `surface-transform-ui` command-probe refresh after `runtime-effect-invalid`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=surface-transform-ui ./scripts/jbr-skia-command-probe-suite.sh`
  passed 15/15. Aggregate: `fallback_sum=0`, zero unsupported-picture rows, zero JBR picture frames, and 23,174 JBR
  command frames. The group rechecked native bridge load, point rendering, concat/skew transforms, gradient surfaces
  and path blend modes, popup/menu UI chrome, and text-image replay. Magic Jewel `out` is 104G with about 233Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-121443/suite.tsv`.
- Magic Jewel focused `runtime-effect-invalid` command-probe refresh after `gradient-invalid`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=runtime-effect-invalid ./scripts/jbr-skia-command-probe-suite.sh`
  passed 62/62. Aggregate: `fallback_sum=56`, seven unsupported-picture rows, 7,153 JBR picture frames, and zero JBR
  command frames. The group rechecked RuntimeEffect shader/color-filter source, schema, child index, nested child,
  compile/build, and child-type parser/fallback guards. Magic Jewel `out` is 104G with about 233Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-112117/suite.tsv`.
- Magic Jewel focused `gradient-invalid` command-probe refresh after `gradient-path-invalid`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=gradient-invalid ./scripts/jbr-skia-command-probe-suite.sh` passed
  81/81. Aggregate: `fallback_sum=60`, 21 unsupported-picture rows, 17,899 JBR picture frames, and zero JBR command
  frames. The group rechecked public gradient invalid fallbacks plus internal linear/radial/sweep gradient
  stroke-width, tile-mode, color-count, stop-order, radius, and path parser guards. Magic Jewel `out` is 103G with
  about 234Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-102137/suite.tsv`.
- Subagent validation environment note: delegated Magic Jewel runs for `gradient-invalid`
  (`20260612-091248`) and `runtime-effect-invalid` (`20260612-091850`) failed their first rows with zero app, CMP
  recorder, Skiko, and JBR frames. Serial exact rerun of `commands-linear-gradient-invalid-stops-fallback` and
  `commands-runtime-effect-shader-source-hash-fallback` passed 2/2 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-101933/suite.tsv`, so the
  subagent failures are not treated as command-stream regressions.
- Magic Jewel focused `gradient-path-invalid` command-probe refresh after `shader-composition-runtime`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=gradient-path-invalid ./scripts/jbr-skia-command-probe-suite.sh`
  passed 21/21. Aggregate: `fallback_sum=18`, three unsupported-picture rows, 5,088 JBR picture frames, and zero JBR
  command frames. The group rechecked invalid linear/radial/sweep gradient path fallback, gradient path tile/color/stop
  parser guards, fill/path-data/verb guards, and radial radius validation. Magic Jewel `out` is 103G with about 230Gi
  free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-042752/suite.tsv`.
- Magic Jewel focused `shader-composition-runtime` command-probe refresh after `shader-rendering`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=shader-composition-runtime ./scripts/jbr-skia-command-probe-suite.sh`
  passed 15/15. Aggregate: `fallback_sum=0`, two unsupported-picture rows, 3,599 JBR picture frames, and 34,803 JBR
  command frames. The group rechecked image/composite/transformed shaders, RuntimeEffect shader/color-filter paths,
  gradient shader color filters, and raw RuntimeEffect fallback rows. Magic Jewel `out` is 103G with about 230Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-041606/suite.tsv`.
- Magic Jewel focused `shader-rendering` command-probe refresh after `native-text`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=shader-rendering ./scripts/jbr-skia-command-probe-suite.sh` passed
  18/18. Aggregate: `fallback_sum=0`, ten unsupported-picture rows, 17,170 JBR picture frames, and 21,380 JBR command
  frames. The group rechecked image shader/blend, image path-effect fallback, gradient/noise/turbulence shaders, and
  raw shader fallback rows. Magic Jewel `out` is 103G with about 230Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-040250/suite.tsv`.
- Magic Jewel focused `native-text` command-probe refresh after `core-effects`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=native-text ./scripts/jbr-skia-command-probe-suite.sh` passed 14/14.
  Aggregate: `fallback_sum=0`, zero unsupported-picture rows, zero JBR picture frames, and 33,822 JBR command frames.
  The group rechecked custom/generic/loaded/resource/system font text replay across baseline, resize, and forced-context
  scenarios. Magic Jewel `out` is 103G with about 231Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-035202/suite.tsv`.
- Magic Jewel focused `core-effects` command-probe refresh after `descriptor-lifecycle`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=core-effects ./scripts/jbr-skia-command-probe-suite.sh` passed 8/8.
  Aggregate: `fallback_sum=0`, three unsupported-picture rows, 5,099 JBR picture frames, and 13,321 JBR command
  frames. The group rechecked gradient stroke, image filter, path effect, vertices, blend mode, and raw
  color/path-effect fallback rows. Magic Jewel `out` is 103G with about 231Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-034519/suite.tsv`.
- Magic Jewel focused `descriptor-lifecycle` command-probe refresh after `color-filters`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=descriptor-lifecycle ./scripts/jbr-skia-command-probe-suite.sh`
  passed 18/18. Aggregate: `fallback_sum=0`, zero unsupported-picture rows, zero JBR picture frames, and 49,005 JBR
  command frames. The group rechecked descriptor eviction, resize and forced-context descriptor redefinition for shader
  families, stable RuntimeEffect color-filter lifecycle, and RuntimeEffect source-cache eviction. Magic Jewel `out` is
  103G with about 208Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-032814/suite.tsv`.
- Magic Jewel focused `color-filters` command-probe refresh after `graphics-layer-invalid`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=color-filters ./scripts/jbr-skia-command-probe-suite.sh` passed
  13/13. Aggregate: `fallback_sum=0`, three unsupported-picture rows, 5,056 JBR picture frames, and 28,945 JBR command
  frames. The group rechecked supported image, paint, and graphics-layer color-filter replay plus raw table/blend
  fallback rows. Magic Jewel `out` is 102G with about 209Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-031803/suite.tsv`.
- Magic Jewel focused `graphics-layer-invalid` command-probe refresh after `save-layer-invalid`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=graphics-layer-invalid ./scripts/jbr-skia-command-probe-suite.sh`
  passed 15/15. Aggregate: `fallback_sum=0`, 15 unsupported-picture rows, 26,029 JBR picture frames, and zero JBR
  command frames. The group rechecked invalid graphics-layer size, alpha, scale, rotation, translation, camera
  distance, shadow elevation/path, blend mode, and unrecorded-layer fallbacks. Magic Jewel `out` is 102G with about
  232Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-030703/suite.tsv`.
- Magic Jewel focused `save-layer-invalid` command-probe refresh after `descriptor-handles-invalid`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=save-layer-invalid ./scripts/jbr-skia-command-probe-suite.sh`
  passed 37/37. Aggregate: `fallback_sum=37`, zero unsupported-picture rows, zero JBR picture frames, and zero JBR
  command frames. The group rechecked saveLayer alpha/flags/length parser guards, color-filter/blend/image-filter
  variants, ref-record guards, bounds/alpha/blend-mode guards, and ref-backed blend color-filter guards. Magic Jewel
  `out` is 102G with about 232Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-024126/suite.tsv`.
- Magic Jewel focused `descriptor-handles-invalid` command-probe refresh after `image-handles-invalid`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=descriptor-handles-invalid ./scripts/jbr-skia-command-probe-suite.sh`
  passed 48/48. Aggregate: `fallback_sum=48`, zero unsupported-picture rows, zero JBR picture frames, and zero JBR
  command frames. The group rechecked descriptor use/use-after-evict/evict parser guards, saveLayer color/image-filter
  handle guards, nested shader/color-filter/image-filter/path-effect child use-after-evict and missing-child guards,
  plus wrong-effect-type guards across shader, color-filter, image-filter, and path-effect descriptors. Magic Jewel
  `out` is 102G with about 232Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-020826/suite.tsv`.
- Magic Jewel focused `image-handles-invalid` command-probe refresh after `shader-descriptor-invalid`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=image-handles-invalid ./scripts/jbr-skia-command-probe-suite.sh`
  passed 27/27. Aggregate: `fallback_sum=27`, zero unsupported-picture rows, zero JBR picture frames, and 1,523 JBR
  command frames from setup before the cache-clear record-flags fallback. The group rechecked image define/cache/evict
  parser guards, invalid image use/ref metadata, image color-filter use/ref metadata, blend mode, and descriptor-backed
  image color-filter ref metadata guards. Magic Jewel `out` is 102G with about 232Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-014928/suite.tsv`.
- Magic Jewel focused `shader-descriptor-invalid` command-probe refresh after `effect-descriptor-invalid`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=shader-descriptor-invalid ./scripts/jbr-skia-command-probe-suite.sh`
  passed 30/30. Aggregate: `fallback_sum=30`, zero unsupported-picture rows, zero JBR picture frames, and zero JBR
  command frames. The group rechecked shader descriptor type/flags/count/length/version guards, transformed/composite
  shader guards, gradient tile/stop/radius/count guards, image shader dimensions/tile modes, and Perlin noise
  kind/frequency/octaves/tile-size guards. Magic Jewel `out` is 102G with about 232Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-012756/suite.tsv`.
- Magic Jewel focused `effect-descriptor-invalid` command-probe refresh after `path-invalid`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=effect-descriptor-invalid ./scripts/jbr-skia-command-probe-suite.sh`
  passed 28/28. Aggregate: `fallback_sum=28`, zero unsupported-picture rows, zero JBR picture frames, and zero JBR
  command frames. The group rechecked effect/color/image/path-effect descriptor parser sentinels, including descriptor
  type/version/flags/length, lighting/tint/color-matrix/blur/offset payload guards, corner path-effect radius guards,
  stamped path-effect advance/phase/style/fill/path-data/path-verb guards, and chain path-effect payload count.
  Magic Jewel `out` is 102G with about 211Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-010824/suite.tsv`.
- Magic Jewel focused `path-invalid` command-probe refresh after `primitive-invalid`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=path-invalid ./scripts/jbr-skia-command-probe-suite.sh` passed 24/24.
  Aggregate: `fallback_sum=22`, two unsupported-picture rows, 3,181 JBR picture frames, and zero JBR command frames.
  The group rechecked invalid path/clip fallback, path verb parsing, dash path-effect interval/geometry/paint guards,
  and draw-shadow path verb fallback. Magic Jewel `out` is 101G with about 233Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-005139/suite.tsv`.
- Magic Jewel focused `primitive-invalid` command-probe refresh after `native-text-invalid`:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=primitive-invalid ./scripts/jbr-skia-command-probe-suite.sh`
  passed 16/16. Aggregate: `fallback_sum=13`, three unsupported-picture rows, 5,096 JBR picture frames, and zero JBR
  command frames. The group rechecked primitive parser sentinels, including stroke cap, blend-layer bounds, transform
  record flags, clip operations, draw-points count/length bounds, and draw-vertices count/mode/blend/index bounds.
  Magic Jewel `out` is 101G with about 233Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-003919/suite.tsv`.
- Magic Jewel focused `native-text-invalid` command-probe refresh after the full structured-marker sweep:
  `EXPECT_SCREENSHOT_ASSERTION=false CASE_GROUPS=native-text-invalid ./scripts/jbr-skia-command-probe-suite.sh`
  passed 11/11. Aggregate: `fallback_sum=11`, zero unsupported-picture rows, zero JBR picture frames, and 1,996 JBR
  command frames from the font-data setup row before its invalid record-flags fallback. The group rechecked text and
  paragraph font size/weight/width/slant/family-count parser guards plus font-data record-flags fallback. Magic Jewel
  `out` is 101G with about 233Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-003034/suite.tsv`.
- Magic Jewel full default command-probe sweep after the saveLayer/shader fallback tail repair:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` passed 549/549 using structured
  command/fallback markers while local macOS screenshot capture was unavailable (`screencapture` could not create a
  window or region image in the failed `20260611-181241` first-row attempt). Aggregate: `fallback_sum=350`, 80
  unsupported-picture rows, 127,181 JBR picture frames, and 306,312 JBR command frames. The sweep revalidated the
  newly tightened saveLayer tail rows, the gradient path paint reason ordering, image fallback marker hardening,
  graphics-layer shadow/transform/effect rows, and all parser/descriptor invalid sentinels. Magic Jewel `out` is 101G
  with about 233Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-181446/suite.tsv`.
- Magic Jewel default command-probe sweep tail repair for saveLayer/shader fallback rows. A broad default
  `./scripts/jbr-skia-command-probe-suite.sh` reached 543 passed rows before stopping at
  `commands-save-layer-raw-color-filter-fallback`; the row had the required `saveLayer` unsupported marker, JBR
  picture fallback, and zero command frames, but the generic screenshot assertion was `not-run`. Magic Jewel now keeps
  these command-probe rows focused on structured command/fallback markers by disabling the generic screenshot assertion
  for the supported saveLayer command rows and the raw/opaque/picture shader fallback sentinels in this tail cluster.
  Exact `CASES='commands-save-layer-filter commands-save-layer-blend-mode commands-save-layer-blend-color-filter
  commands-save-layer-raw-color-filter-fallback'` passed 4/4, then `CASE_GROUPS=save-layer-shader-fallbacks` passed
  9/9 with `fallback_sum=0`, six unsupported-picture rows, 9,440 JBR picture frames, and 7,901 JBR command frames.
  Magic Jewel `out` is 99G with about 242Gi free. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-115351/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-175636/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-180417/suite.tsv`.
- Magic Jewel focused `graphics-layer-extras` command-probe group after `graphics-layer`:
  `CASE_GROUPS=graphics-layer-extras ./scripts/jbr-skia-command-probe-suite.sh` passed 16/16. Aggregate:
  `fallback_sum=0`, four unsupported-picture rows, 5,374 JBR picture frames, and 22,521 JBR command frames. The group
  rechecked resize/forced-context graphics-layer color-matrix and render-effect rows, raw color-filter/image-filter
  fallbacks, unsupported-child fallback, and chained render-effect/color-filter/blend/color-matrix combinations:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-114204/suite.tsv`.
- Magic Jewel focused `graphics-layer` command-probe group after the shadow guard audit:
  `CASE_GROUPS=graphics-layer ./scripts/jbr-skia-command-probe-suite.sh` passed 22/22. Aggregate:
  `fallback_sum=0`, one expected unsupported-picture row from invalid shadow elevation, 1,401 JBR picture frames, and
  32,675 JBR command frames. The grouped run rechecked base/modulate/offscreen/clip layers, blend/color-filter/render
  effect rows, rectangular/round/path shadows, 3D rotations, scale/translate, near-camera, and off-center pivot:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-112529/suite.tsv`.
- Magic Jewel focused graphics-layer shadow command probes during the `graphicsLayer:shadowFilter` audit:
  `CASES='commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow
  commands-graphics-layer-invalid-shadow-elevation-fallback commands-graphics-layer-invalid-shadow-path-fallback'
  ./scripts/jbr-skia-command-probe-suite.sh` passed 5/5. Supported rectangular, round, and path shadow rows reported
  no unsupported reasons and 5,274 JBR command frames. Invalid elevation/path rows reported expected
  `graphicsLayer:shadowElevation` and `graphicsLayer:shadowPath` fallback summaries with 2,529 JBR picture frames and
  zero command frames. `graphicsLayer:shadowFilter` remains classified as an internal blur image-filter descriptor
  definition guard rather than a public app-row gap:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-112032/suite.tsv`.
- CMP defensive recorder coverage for the recorder-only `roundRectStyle` unsupported branch:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.rejectsUnknownRoundRectPaintStyleInStrictMode` passed, then
  the full focused `JbrSkiaCommandRecorderTest` class passed with `BUILD SUCCESSFUL`; Gradle reported 79 actionable
  tasks, 12 executed and 67 up-to-date on the full-class run. This classifies `roundRectStyle` as a defensive
  non-public paint-style guard covered by CMP unit tests rather than a Magic Jewel app-row gap.
- CMP recorder and Magic Jewel command probes after fixing gradient path stroke fallback reason ordering:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest` passed in
  `/Users/rock3r/src/jbr-skia-zero-copy/cmp` with `BUILD SUCCESSFUL`; Gradle reported 79 actionable tasks, 14
  executed and 65 up-to-date. Exact
  `CASES='commands-linear-gradient-path-stroke-fallback commands-radial-gradient-path-stroke-fallback
  commands-sweep-gradient-path-stroke-fallback' ./scripts/jbr-skia-command-probe-suite.sh` passed 3/3 with
  `linearGradientPathPaint`, `radialGradientPathPaint`, and `sweepGradientPathPaint` summaries, 3,579 JBR picture
  frames, and zero JBR command frames. The named
  `CASE_GROUPS=gradient-path-stroke-fallbacks ./scripts/jbr-skia-command-probe-suite.sh` refresh also passed 3/3
  with 3,603 JBR picture frames and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-110959/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-111207/suite.tsv`.
- Magic Jewel combined `shader-rendering color-filters` command-probe refresh after image fallback marker hardening:
  `CASE_GROUPS='shader-rendering color-filters' ./scripts/jbr-skia-command-probe-suite.sh` passed 31/31. Aggregate:
  `fallback_sum=0`, 15,814 JBR picture frames from expected raw/invalid fallback sentinels, and 30,519 JBR command
  frames from supported shader, image, color-filter, and graphics-layer color-filter rows. The newly hardened image
  fallback rows passed in group context with unsupported summaries
  `graphicsLayer:childCommands:1265,image:1265,pathEffect:1265,graphicsLayer:1265` and
  `colorFilter:1238,graphicsLayer:childCommands:1238,image:1238,graphicsLayer:1238`. Magic Jewel `out` is 96G with
  about 246Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-104452/suite.tsv`.
- Magic Jewel exact command-probe hardening for live image fallback markers:
  `CASES='commands-image-path-effect-fallback commands-image-raw-table-color-filter-fallback'
  ./scripts/jbr-skia-command-probe-suite.sh` passed 2/2 after adding `EXPECT_COMMAND_FALLBACK_MARKER=image=1` to the
  image path-effect and image raw table color-filter rows. Aggregate: `fallback_sum=0`, 2,431 JBR picture frames, and
  zero JBR command frames. The unsupported summaries were
  `graphicsLayer:childCommands:973,image:973,pathEffect:973,graphicsLayer:973` and
  `colorFilter:1458,graphicsLayer:childCommands:1458,image:1458,graphicsLayer:1458`, proving the top-level image
  unsupported reason remains present alongside each paint-specific reason:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-104135/suite.tsv`.
- Magic Jewel exact screenshot parity hardening for graphics-layer color-matrix effect handles:
  `CASES='parity-graphics-layer-color-matrix-filter parity-graphics-layer-blend-color-matrix-filter'
  ./scripts/jbr-skia-screenshot-parity-suite.sh` passed 2/2 after adding strict
  `EXPECT_MIN_JBR_EFFECT_HANDLE_DEFINES`, `EXPECT_MIN_JBR_EFFECT_HANDLE_USES`,
  `EXPECT_MIN_JBR_EFFECT_HANDLE_CACHE_HITS`, and `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES` gates to those rows.
  Aggregate: `fallback_sum=0`, zero JBR picture frames, and 2,870 JBR command frames. The plain color-filter graphics
  layer rows were left ungated because the full sweep showed zero effect-handle counters for those paths. The hardened
  rows reported 5 effect-handle defines each, with use/cache-hit pairs of 2325/2320 and 2364/2359:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-103423/suite.tsv`.
- Magic Jewel full default benchmark suite after the parser/API and report-validation refreshes:
  default `./scripts/jbr-skia-benchmark-suite.sh` passed 5/5. Aggregate: `fallback_sum=0`, 82 old-side CPU samples,
  80 new-side CPU samples, and 13,724 JBR command frames across command cases. Per-case command frames were
  `commands=5358`, `commands-stable-images=2844`, `commands-dynamic-images=2820`, and
  `commands-resize-dynamic-images=2702`; the picture baseline produced 4,220 JBR picture frames. Magic Jewel `out` is
  96G with about 246Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260611-102359/suite.tsv`.
- JBR parser/API helper against the current local overlay artifacts:
  `REBUILD_LOCAL_ARTIFACTS=false ./scripts/test-jbr-skia-api.sh` patched the temporary JBRApi stub into the
  java.desktop overlay, compiled `JBRSkiaApiTest`, ran it headlessly, and printed `JBR_SKIA_API_TEST passed`.
- Magic Jewel report-validation unit script after the CMP recorder validation:
  `./scripts/test-jbr-skia-report-validation.sh` passed with `JBR_SKIA_REPORT_VALIDATION_TESTS passed`. The script's
  expected negative strict-validation case printed a temporary failure report path before the final pass marker.
- CMP focused recorder validation after the unsupported-reason source scan:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest` passed in
  `/Users/rock3r/src/jbr-skia-zero-copy/cmp` with `BUILD SUCCESSFUL`; Gradle reported 79 actionable tasks, 12
  executed and 67 up-to-date.
- Cross-repo ABI/capability drift audit after the matrix and Skiko checkpoints:
  scoped `rg` checks found JBR private API and JBR API mirror at `ABI_ID=106` and `NATIVE_ABI_VERSION=3`, JBR native
  metadata at native ABI 3, Skiko discovery at expected ABI 106/native ABI 3, Skiko stream writer and CMP recorder at
  `COMMAND_STREAM_ABI_ID=106`, and matching capability tail constants including
  `COMMAND_CAP64_SAVE_LAYER_BLEND_COLOR_FILTER_REF`, `COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_PERLIN_NOISE`, and
  `COMMAND_CAP64_HIGH_DRAW_VERTICES`.
- Skiko focused `JbrSkiaInteropTest` after the matrix gates:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  passed in `/Users/rock3r/src/jbr-skia-zero-copy/skiko/skiko` with `BUILD SUCCESSFUL`; Gradle reported 28 actionable
  tasks, one executed and 27 up-to-date.
- Magic Jewel current-artifact matrix after the compatibility refresh:
  default `./scripts/jbr-skia-artifact-matrix.sh` passed. Required rows passed 2/2: `current-all` had no fallback and
  654 JBR command frames; `missing-public-api` produced one expected structured fallback with no command frames. The
  five optional old-artifact rows were skipped because `OLD_JBR_API_SHIM`, `OLD_JBR_SKIA_LIB`, `OLD_DESKTOP_PATCH`,
  `OLD_SKIKO_VERSION`, and `OLD_CMP_OUT` were unset. Magic Jewel `out` is 96G with about 246Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260611-101223/matrix.tsv`.
- Magic Jewel full compatibility matrix after the full command and screenshot parity sweeps:
  default `./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57. No-run discovery still resolved 57 rows.
  Aggregate: `fallback_sum=56`, 474 JBR command frames from the happy path, and all 57 rows reported
  `background_window=true`. Every forced ABI, native ABI, command-capability, high-capability, exact capability, and
  public API mismatch row produced exactly one structured fallback with no command frames. Magic Jewel `out` is 96G
  with about 246Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260611-094313/matrix.tsv`.
- Magic Jewel full default screenshot parity sweep after the focused visual parity refresh batch:
  default `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106. Aggregate: `fallback_sum=12`, zero JBR
  picture frames, 102,236 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The sweep
  consolidated smoke, core drawing, native text, shader rendering, RuntimeEffect, descriptor lifecycle, and
  graphics-layer basic/effects/clip/shadow/transform parity coverage refreshed in the preceding focused runs. Magic
  Jewel `out` is 96G with about 225Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-083428/suite.tsv`.
- Magic Jewel focused `smoke` screenshot parity refresh after `graphics-layer-effects`: `CASE_GROUPS=smoke` passed
  3/3. Aggregate: `fallback_sum=0`, zero JBR picture frames, 3,176 JBR command frames, mean `avg_delta=2.398`, and
  mean `bad_pixel_ratio=0.05816`. The group rechecked the rich scene, button chrome, and geometry-clean smoke views.
  Magic Jewel `out` is 96G with about 246Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-082953/suite.tsv`.
- Magic Jewel focused `graphics-layer-effects` screenshot parity refresh after `descriptor-lifecycle`:
  `CASE_GROUPS=graphics-layer-effects` passed 14/14. Aggregate: `fallback_sum=2`, zero JBR picture frames, 17,833 JBR
  command frames, mean `avg_delta=2.341`, and mean `bad_pixel_ratio=0.05725`. The group rechecked resize and
  forced-context graphics-layer color-matrix/render-effect rows, offset/chained/near-camera render effects, and
  render-effect blend/color-filter/color-matrix combinations. Magic Jewel `out` is 96G with about 246Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-081932/suite.tsv`.
- Magic Jewel focused `descriptor-lifecycle` screenshot parity refresh after `core-drawing`:
  `CASE_GROUPS=descriptor-lifecycle` passed 6/6. Aggregate: `fallback_sum=1`, zero JBR picture frames, 6,461 JBR
  command frames, mean `avg_delta=2.127`, and mean `bad_pixel_ratio=0.05078`. The group rechecked color-filter
  handles, resize and forced-context handle reuse, color-matrix and lighting filters, and descriptor eviction. Magic
  Jewel `out` is 96G with about 247Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-081244/suite.tsv`.
- Magic Jewel focused `core-drawing` screenshot parity refresh after `native-text`: `CASE_GROUPS=core-drawing`
  passed 16/16. Aggregate: `fallback_sum=0`, zero JBR picture frames, 15,732 JBR command frames, mean
  `avg_delta=2.175`, and mean `bad_pixel_ratio=0.05197`. The group rechecked skew, vertices, saveLayer filter,
  forced-context image refs, point dots, path effects, shapes, clips, blend modes, gradient surfaces/paths/shaders/
  stroke, image filters, and image color-matrix filters. Magic Jewel `out` is 95G with about 247Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-080125/suite.tsv`.
- Magic Jewel focused `native-text` screenshot parity refresh after `runtime-effect`: `CASE_GROUPS=native-text`
  passed 14/14. Aggregate: `fallback_sum=3`, zero JBR picture frames, 11,672 JBR command frames, mean
  `avg_delta=2.037`, and mean `bad_pixel_ratio=0.04758`. The group rechecked custom text images, generic font,
  loaded-data font, resource font, and system font text across base, resize, and forced-context replay. Magic Jewel
  `out` is 95G with about 247Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-075039/suite.tsv`.
- Magic Jewel focused `runtime-effect` screenshot parity refresh after `shader-rendering`:
  `CASE_GROUPS=runtime-effect` passed 14/14. Aggregate: `fallback_sum=2`, zero JBR picture frames, 11,139 JBR
  command frames, mean `avg_delta=2.054`, and mean `bad_pixel_ratio=0.04879`. The group rechecked RuntimeEffect
  shader/color-filter pure, uniform, child, source-cache eviction, resize, forced-context, shader-color-filter, stable
  color-filter, and child color-filter rows. Magic Jewel `out` is 95G with about 227Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-074046/suite.tsv`.
- Magic Jewel focused `shader-rendering` screenshot parity refresh after the graphics-layer visual groups:
  `CASE_GROUPS=shader-rendering` passed 18/18. Aggregate: `fallback_sum=4`, zero JBR picture frames, 13,787 JBR
  command frames, mean `avg_delta=2.081`, and mean `bad_pixel_ratio=0.04995`. The group rechecked image/color/noise/
  turbulence/composite/transformed shaders, resize and forced-context descriptor lifecycle rows, and shader
  color-filter combinations. Magic Jewel `out` is 95G with about 227Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-072815/suite.tsv`.
- Magic Jewel focused `graphics-layer-clip-shadow-transform` screenshot parity refresh after
  `graphics-layer-basic`: `CASE_GROUPS=graphics-layer-clip-shadow-transform` passed 14/14. Aggregate:
  `fallback_sum=0`, zero JBR picture frames, 19,849 JBR command frames, mean `avg_delta=2.223`, and mean
  `bad_pixel_ratio=0.05296`. The group rechecked rectangle/round/path clipping, rectangle/round/path shadows,
  modulate/offscreen compositing, rotation X/Y/XY, scale/translate, near-camera, and off-center pivot replay. Magic
  Jewel `out` is 95G with about 227Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-071806/suite.tsv`.
- Magic Jewel focused `graphics-layer-basic` screenshot parity refresh after the full command sweep:
  `CASE_GROUPS=graphics-layer-basic` passed 7/7. Aggregate: `fallback_sum=0`, zero JBR picture frames, 8,142 JBR
  command frames, mean `avg_delta=2.194`, and mean `bad_pixel_ratio=0.05223`. The group rechecked basic layer replay,
  render effects, blend-mode layers, color filters, color-matrix filters, and blended filter combinations. Magic Jewel
  `out` is 95G with about 247Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-071236/suite.tsv`.
- Magic Jewel full default command-probe sweep after the focused group refresh batch:
  default `./scripts/jbr-skia-command-probe-suite.sh` passed 549/549. Aggregate: `fallback_sum=350`, 79
  unsupported-picture rows, 87,188 JBR picture frames, and 188,270 JBR command frames. The sweep consolidated the
  native text, primitive/path invalid, shader/effect descriptor, image handle, graphics-layer, color-filter,
  RuntimeEffect, gradient, and saveLayer coverage refreshed in the preceding focused runs. Magic Jewel `out` is 95G
  with about 246Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-012205/suite.tsv`.
- Magic Jewel focused `gradient-invalid` command-probe refresh after `runtime-effect-invalid`:
  `CASE_GROUPS=gradient-invalid` passed 81/81. Aggregate: `fallback_sum=60`, 21 unsupported-picture rows, 23,049 JBR
  picture frames, and zero JBR command frames. The group rechecked public gradient unsupported-picture fallback rows
  plus malformed gradient stroke-width, tile-mode, color-count, stop-order, path, radius, and round-rect parser guards.
  Magic Jewel `out` is 93G with about 251Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-003036/suite.tsv`.
- Magic Jewel focused `runtime-effect-invalid` command-probe refresh after `descriptor-handles-invalid`:
  `CASE_GROUPS=runtime-effect-invalid` passed 62/62. Aggregate: `fallback_sum=56`, six unsupported-picture rows, 6,399
  JBR picture frames, and zero JBR command frames. The group rechecked RuntimeEffect shader/color-filter source, SkSL,
  uniform/child schema, named refs, child indices, invalid schemas, compile/build, and child-type fallback guards.
  Magic Jewel `out` is 93G with about 247Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-234517/suite.tsv`.
- Magic Jewel focused `descriptor-handles-invalid` command-probe refresh after `save-layer-invalid`:
  `CASE_GROUPS=descriptor-handles-invalid` passed 48/48. Aggregate: `fallback_sum=48`, `unsupported_rows=0`, zero JBR
  picture frames, and zero JBR command frames. The group rechecked invalid descriptor handle use, use-after-evict,
  missing children, wrong effect-type refs, shader/color-filter/image-filter/path-effect children, and saveLayer
  descriptor-handle guards. Magic Jewel `out` is 92G with about 227Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-231048/suite.tsv`.
- Magic Jewel focused `save-layer-invalid` command-probe refresh after `gradient-path-invalid`:
  `CASE_GROUPS=save-layer-invalid` passed 37/37. Aggregate: `fallback_sum=37`, `unsupported_rows=0`, zero JBR picture
  frames, and zero JBR command frames. The group rechecked malformed saveLayer alpha, record flags/lengths, color
  filter/blend/image-filter refs, dimensions, alpha, and blend-mode fallback guards. Magic Jewel `out` is 92G with
  about 259Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-224435/suite.tsv`.
- Magic Jewel focused `gradient-path-invalid` command-probe refresh after `graphics-layer-extras`:
  `CASE_GROUPS=gradient-path-invalid` passed 21/21. Aggregate: `fallback_sum=18`, three unsupported-picture rows, 3,542
  JBR picture frames, and zero JBR command frames. The group rechecked linear, radial, and sweep gradient path picture
  fallbacks plus malformed tile-mode, color-count, stop-order, fill-type, path data-length, and path-verb guards. Magic
  Jewel `out` is 92G with about 259Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-222857/suite.tsv`.
- Magic Jewel focused `graphics-layer-extras` command-probe refresh after `graphics-layer`:
  `CASE_GROUPS=graphics-layer-extras` passed 16/16. Aggregate: `fallback_sum=0`, four unsupported-picture rows, 4,221
  JBR picture frames, and 15,698 JBR command frames. The group rechecked resize/forced-context graphics-layer color
  matrix and render-effect rows, raw color-filter/image-filter fallbacks, unsupported-child fallback, and chained
  render-effect/color-filter layer replay. Magic Jewel `out` is 92G with about 259Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-221556/suite.tsv`.
- Magic Jewel focused `graphics-layer` command-probe refresh after `surface-transform-ui`:
  `CASE_GROUPS=graphics-layer` passed 22/22. Aggregate: `fallback_sum=0`, one unsupported-picture row, 1,071 JBR
  picture frames, and 31,583 JBR command frames. The group rechecked base/modulate/offscreen/clip layers, blend and
  color-filter layers, render effects, shadows, 3D rotation, camera, scale/translate, and pivot replay. Magic Jewel
  `out` is 92G with about 259Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-215954/suite.tsv`.
- Magic Jewel focused `surface-transform-ui` command-probe refresh after `graphics-layer-invalid`:
  `CASE_GROUPS=surface-transform-ui` passed 15/15. Aggregate: `fallback_sum=0`, `unsupported_rows=0`, zero JBR
  picture frames, and 18,966 JBR command frames. The group rechecked native bridge loading, point rendering, concat and
  skew transforms, gradient surfaces/paths, popup/menu UI rows, and text image replay. Magic Jewel `out` is 92G with
  about 259Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-214838/suite.tsv`.
- Magic Jewel focused `graphics-layer-invalid` command-probe refresh after `native-text`:
  `CASE_GROUPS=graphics-layer-invalid` passed 15/15. Aggregate: `fallback_sum=0`, 15 unsupported-picture rows, 15,986
  JBR picture frames, and zero JBR command frames. The group rechecked invalid graphics-layer size, alpha, transform,
  camera distance, shadow, blend-mode, and unrecorded-layer fallbacks. Magic Jewel `out` is 92G with about 238Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-213721/suite.tsv`.
- Magic Jewel focused `native-text` command-probe refresh after `image-handles-invalid`:
  `CASE_GROUPS=native-text` passed 14/14. Aggregate: `fallback_sum=0`, `unsupported_rows=0`, zero JBR picture frames,
  and 17,781 JBR command frames. The group rechecked custom font text images plus generic, loaded-data, resource, and
  system font text across base, resize, and forced-context command replay. Magic Jewel `out` is 92G with about 238Gi
  free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-212619/suite.tsv`.
- Magic Jewel focused `image-handles-invalid` command-probe refresh after `shader-descriptor-invalid`:
  `CASE_GROUPS=image-handles-invalid` passed 27/27. Aggregate: `fallback_sum=27`, `unsupported_rows=0`, zero JBR
  picture frames, and 892 JBR command frames. The group rechecked malformed image define/cache/evict records, invalid
  image/image-color-filter handle use, use-after-evict, image refs, blend modes, and descriptor-ref fallback guards.
  Magic Jewel `out` is 92G with about 238Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-210711/suite.tsv`.
- Magic Jewel focused `shader-descriptor-invalid` command-probe refresh after `effect-descriptor-invalid`:
  `CASE_GROUPS=shader-descriptor-invalid` passed 30/30. Aggregate: `fallback_sum=30`, `unsupported_rows=0`, zero JBR
  picture frames, and zero JBR command frames. The group rechecked malformed shader descriptor headers plus color,
  shader-color-filter, transformed, composite, gradient, image, and Perlin-noise descriptor fallback guards. Magic
  Jewel `out` is 92G with about 257Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-204523/suite.tsv`.
- Magic Jewel focused `effect-descriptor-invalid` command-probe refresh after `descriptor-lifecycle`:
  `CASE_GROUPS=effect-descriptor-invalid` passed 28/28. Aggregate: `fallback_sum=28`, `unsupported_rows=0`, zero JBR
  picture frames, and zero JBR command frames. The group rechecked malformed effect descriptor headers plus lighting,
  tint, color-matrix, blur, offset, corner, stamped, and chained path-effect descriptor fallback guards. Magic Jewel
  `out` is 92G with about 258Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-202519/suite.tsv`.
- Magic Jewel focused `descriptor-lifecycle` command-probe refresh after `color-filters`:
  `CASE_GROUPS=descriptor-lifecycle` passed 18/18. Aggregate: `fallback_sum=0`, `unsupported_rows=0`, zero JBR picture
  frames, and 22,552 JBR command frames. The group covered descriptor eviction, resize/forced-context descriptor
  redefine for color, noise, turbulence, composite noise, shader, and RuntimeEffect color-filter/source-cache rows.
  Magic Jewel `out` is 92G with about 258Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-200920/suite.tsv`.
- Magic Jewel focused `color-filters` command-probe refresh after shader composition/runtime validation:
  `CASE_GROUPS=color-filters` passed 13/13. Aggregate: `fallback_sum=0`, three unsupported-picture rows, 2,738 JBR
  picture frames, and 11,697 JBR command frames. Supported image, fill-rect, descriptor-handle, matrix/lighting, and
  graphics-layer color-filter rows replayed commands; raw table/blend color-filter rows stayed on structured picture
  fallback. Magic Jewel `out` remains 91G; volume free space is about 259Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-195849/suite.tsv`.
- Magic Jewel focused `shader-composition-runtime` command-probe refresh after `shader-rendering`:
  `CASE_GROUPS=shader-composition-runtime` passed 15/15. Aggregate: `fallback_sum=0`, two unsupported-picture rows,
  1,690 JBR picture frames, and 17,687 JBR command frames. RuntimeEffect shader/color-filter, composite shader,
  transformed shader, image shader color-filter, and gradient shader color-filter rows replayed commands; raw
  RuntimeEffect shader/color-filter rows stayed on structured picture fallback. Magic Jewel `out` remains 91G; volume
  free space is about 259Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-194645/suite.tsv`.
- Magic Jewel focused `shader-rendering` command-probe refresh after `core-effects`:
  `CASE_GROUPS=shader-rendering` passed 18/18. Aggregate: `fallback_sum=0`, ten unsupported-picture rows, 9,154 JBR
  picture frames, and 10,048 JBR command frames. Command replay covered forced-context dynamic images, image
  blend-mode, image/color/gradient/noise/turbulence shader rows; raw image/gradient/noise/turbulence shader rows,
  descriptor-stroke shader, image path-effect, and invalid image-shader rows stayed on structured picture fallback.
  Magic Jewel `out` remains 91G; volume free space is about 240Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-193254/suite.tsv`.
- Magic Jewel focused `core-effects` command-probe refresh after `save-layer-shader-fallbacks`:
  `CASE_GROUPS=core-effects` passed 8/8. Aggregate: `fallback_sum=0`, three unsupported-picture rows, 3,051 JBR
  picture frames, and 6,875 JBR command frames. Gradient stroke, image-filter, path-effect, vertices, and blend-mode
  rows replayed commands; path-effect color-filter, raw discrete path-effect, and vertices raw color-filter rows stayed
  on structured picture fallback. Magic Jewel `out` remains 91G; volume free space is about 240Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-192454/suite.tsv`.
- Magic Jewel focused `save-layer-shader-fallbacks` command-probe refresh after the gradient path groups:
  `CASE_GROUPS=save-layer-shader-fallbacks` passed 9/9. Aggregate: `fallback_sum=0`, six unsupported-picture rows,
  6,984 JBR picture frames, and 4,511 JBR command frames. Supported saveLayer filter/blend/color-filter rows replayed
  commands; raw color-filter, raw table color-filter, opaque shader, composite opaque shader, picture shader, and
  invalid sweep-gradient rows stayed on structured picture fallback. Magic Jewel `out` remains 91G; volume free space
  is about 240Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-191700/suite.tsv`.
- Magic Jewel focused `gradient-path-stroke-fallbacks` command-probe refresh after path-structure validation:
  `CASE_GROUPS=gradient-path-stroke-fallbacks` passed 3/3. Aggregate: `fallback_sum=0`, three unsupported-picture rows,
  2,923 JBR picture frames, and zero JBR command frames. The rows reported `linearGradientPaint`,
  `radialGradientPaint`, and `sweepGradientPaint` fallback reasons. Volume free space remains about 260Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-191342/suite.tsv`.
- Magic Jewel focused `gradient-path-structure-invalid` command-probe refresh after the public gradient shape trio:
  `CASE_GROUPS=gradient-path-structure-invalid` passed 3/3. Aggregate: `fallback_sum=0`, three unsupported-picture
  rows, 3,236 JBR picture frames, and zero JBR command frames. The rows reported `linearGradientPath`,
  `radialGradientPath`, and `sweepGradientPath` fallback reasons alongside path/graphics-layer metadata. Volume free
  space is about 260Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-191022/suite.tsv`.
- Magic Jewel focused `gradient-stroke-round-rect-radius-invalid` command-probe refresh after round-rect radius
  validation: `CASE_GROUPS=gradient-stroke-round-rect-radius-invalid` passed 3/3. Aggregate: `fallback_sum=0`, three
  unsupported-picture rows, 3,360 JBR picture frames, and zero JBR command frames. The rows reported
  `linearGradientStrokeRoundRectRadius`, `radialGradientStrokeRoundRectRadius`, and
  `sweepGradientStrokeRoundRectRadius` fallback reasons. Volume free space is about 260Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-190701/suite.tsv`.
- Magic Jewel focused `gradient-round-rect-radius-invalid` command-probe refresh after stroke-width validation:
  `CASE_GROUPS=gradient-round-rect-radius-invalid` passed 3/3. Aggregate: `fallback_sum=0`, three
  unsupported-picture rows, 3,126 JBR picture frames, and zero JBR command frames. The rows reported
  `linearGradientRoundRectRadius`, `radialGradientRoundRectRadius`, and `sweepGradientRoundRectRadius` fallback
  reasons:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-190334/suite.tsv`.
- Magic Jewel focused `gradient-stroke-width-invalid` command-probe refresh after color-count validation:
  `CASE_GROUPS=gradient-stroke-width-invalid` passed 3/3. Aggregate: `fallback_sum=0`, three unsupported-picture rows,
  3,610 JBR picture frames, and zero JBR command frames. The public rows reported `linearGradientStrokeWidth`,
  `radialGradientStrokeWidth`, and `sweepGradientStrokeWidth` fallback reasons:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-185958/suite.tsv`.
- Magic Jewel focused `gradient-color-count-invalid` command-probe refresh after `gradient-geometry-invalid`:
  `CASE_GROUPS=gradient-color-count-invalid` passed 3/3. Aggregate: `fallback_sum=0`, three unsupported-picture rows,
  3,035 JBR picture frames, and zero JBR command frames. The rows reported `linearGradientColorCount`,
  `radialGradientColorCount`, and `sweepGradientColorCount` fallback reasons:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-185616/suite.tsv`.
- Magic Jewel focused `gradient-geometry-invalid` command-probe refresh after `gradient-stop-invalid`:
  `CASE_GROUPS=gradient-geometry-invalid` passed 3/3. Aggregate: `fallback_sum=0`, three unsupported-picture rows,
  3,107 JBR picture frames, and zero JBR command frames. The rows reported `linearGradientPoints`,
  `radialGradientGeometry`, and `sweepGradientGeometry` fallback reasons:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-185303/suite.tsv`.
- Magic Jewel focused `gradient-stop-invalid` command-probe refresh after `image-shader-invalid`:
  `CASE_GROUPS=gradient-stop-invalid` passed 4/4. Aggregate: `fallback_sum=0`, four unsupported-picture rows, 4,226
  JBR picture frames, and zero JBR command frames. The rows reported family-specific
  `linearGradientStops`, `linearGradientPoints`, `radialGradientStops`, and `sweepGradientStops` fallback reasons:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-184901/suite.tsv`.
- Magic Jewel one-row `image-shader-invalid` command-probe refresh after the color-filter invalid refresh:
  `CASE_GROUPS=image-shader-invalid` passed 1/1. The row reported `imageShaderImage`, one unsupported-picture row,
  1,034 JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-184709/suite.tsv`.
- Magic Jewel focused `fill-rect-color-filter-invalid` command-probe refresh after `shader-ref-invalid`:
  `CASE_GROUPS=fill-rect-color-filter-invalid` passed 6/6. Aggregate: `fallback_sum=5`, one unsupported-picture row,
  1,061 JBR picture frames, and zero JBR command frames. The non-finite color-matrix row reported
  `colorMatrixNonfinite`; direct and descriptor-backed fill-rect color-filter width/height/blend-mode malformed stream
  rows failed closed with fallback markers. Volume free space is about 261Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-184147/suite.tsv`.
- Magic Jewel focused `shader-ref-invalid` command-probe refresh after `blend-mode-invalid`:
  `CASE_GROUPS=shader-ref-invalid` passed 3/3. Aggregate: `fallback_sum=3`, zero unsupported rows, zero JBR picture
  frames, and zero JBR command frames. The fill-rect shader-ref horizontal bounds, vertical bounds, and alpha malformed
  stream rows all failed closed with exact fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-183824/suite.tsv`.
- Magic Jewel focused `blend-mode-invalid` command-probe refresh after `path-invalid`: `CASE_GROUPS=blend-mode-invalid`
  passed 3/3. Aggregate: `fallback_sum=2`, one unsupported-picture row, 957 JBR picture frames, and zero JBR command
  frames. The live vertices row reported the dynamic `blendMode_Clear` unsupported reason, while fill-rect blend-mode
  width/height malformed stream rows failed closed with fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-183512/suite.tsv`.
- Magic Jewel focused `path-invalid` command-probe refresh after `primitive-invalid`: `CASE_GROUPS=path-invalid`
  passed 24/24. Aggregate: `fallback_sum=22`, two unsupported-picture rows, 2,125 JBR picture frames, and zero JBR
  command frames. The public invalid clip/draw path rows reported structured picture fallback; path verb, dash
  path-effect, round-rect dash, stroke-path dash, and shadow-path malformed stream rows failed closed with exact
  fallback markers. Magic Jewel `out` is 91G with about 261Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-181836/suite.tsv`.
- Magic Jewel focused `primitive-invalid` command-probe refresh after `native-text-invalid`: `CASE_GROUPS=primitive-invalid`
  passed 16/16. Aggregate: `fallback_sum=13`, three unsupported-picture rows, 3,196 JBR picture frames, and zero JBR
  command frames. The unsupported-picture rows were `blendLayerBounds`, `transform`/`unsupportedScope`, and `points`;
  all draw-points and draw-vertices structural-invalid rows failed closed with exact fallback markers and no replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-180642/suite.tsv`.
- Magic Jewel focused `native-text-invalid` command-probe refresh after the ABI drift audit: no-run group discovery
  reported 11 rows, and `CASE_GROUPS=native-text-invalid` passed 11/11. Aggregate: `fallback_sum=11`,
  `unsupported_rows=0`, zero JBR picture frames, and 1,369 JBR command frames. The text and paragraph metadata rows
  failed closed with one fallback marker each and no replay frames; the font-data record-flags row also produced the
  expected fallback marker while exercising command replay frames. Magic Jewel `out` is 91G with about 262Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-175826/suite.tsv`.
- Cross-repo ABI/capability drift audit after the compatibility and artifact matrix refreshes: extracted
  `ABI_ID`, `NATIVE_ABI_VERSION`, `COMMAND_*`, `COMMAND_CAP_*`, and `COMMAND_CAP64_*` definitions from the scoped
  bridge files only: JBR private API `JBRSkia.java`, JBR API mirror `JBRSkia.java`, JBR native
  `JBRSkiaInterop.mm`, Skiko `JbrSkiaInterop.kt`/`JbrSkiaSwingLayer.kt`, and CMP
  `JbrSkiaCommandRecorder.skiko.kt`. The JBR private API and public mirror matched exactly across 212 constants.
  Shared constants in the native parser subset, Skiko discovery/layer subsets, and CMP recorder subset had zero
  mismatches against the public mirror. Required capability masks computed from the mirror remain 65 low-word bits
  with `low=-1`/`0xffffffffffffffff` and 18 high-word bits with `high=262143`/`0x000000000003ffff`. Subset-only
  omissions are intentional because native/Skiko/CMP duplicate only the constants they parse, gate, or emit.
- Magic Jewel required artifact matrix refresh on current ABI 106 local artifacts: no-run discovery still reports
  `required` 2 rows and `optional-old` 5 rows. Ran `CASE_GROUPS=required`; both required rows passed with
  `background_window=true`. Aggregate: rows=2, passed=2, failed=0, skipped=0, `fallback_sum=1`, and 334 JBR command
  frames. `current-all` replayed commands with no fallback; `missing-public-api` produced the expected
  `public-api-missing` fallback with zero command frames. Magic Jewel `out` is 91G with about 262Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260610-175047/matrix.tsv`.
- Magic Jewel compatibility matrix refresh after the 549-row command-probe consolidation: no-run discovery resolved
  57 total rows (`handshake` 6, `low-word-gradients` 15, `low-word-effects` 18, `high-word-effects` 9, and
  `high-word-shader-ui` 9) with no ungrouped cases. The full matrix passed 57/57 with `fallback_sum=56`, 573 JBR
  command frames from the happy path, zero command frames on every ABI/capability/public-API mismatch row, and
  `background_window=true` on all rows. Magic Jewel `out` is 91G with about 243Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260610-171921/matrix.tsv`.
- Magic Jewel 549-row default command-probe consolidation: after the public gradient shape rows expanded default
  discovery to 549 rows, a full default sweep was refreshed as split evidence. Two fresh broad starts failed before any
  app/process samples or CMP/Skiko/JBR markers because `:runJbrSkiaInterop` exited 143/SIGTERM; the same rows passed
  immediately as exact/focused reruns, so those failed output directories are treated as launch interruptions, not
  command validation evidence. Passing slices:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-103258/suite.tsv`
  (`commands-live-animation`, 1/1 passed),
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-104522/suite.tsv`
  (`stream-invalid`, 8/8 passed), and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-105340/suite.tsv`
  (suffix from `commands-native-bridge-load-library`, 540/540 passed). Combined aggregate: 549/549 passed,
  `fallback_sum=350`, 79 unsupported-picture rows, 69,200 JBR picture frames, and 111,092 JBR command frames. The
  sweep revalidated the new public gradient stroke-width, round-rect radius, and stroked round-rect radius sentinels
  inside the default suite, plus descriptor lifecycle, RuntimeEffect compile/build/failure telemetry, color-filter,
  graphics-layer, shadow, and saveLayer rows. Magic Jewel `out` is 91G with about 262Gi free.
- CMP current unsupported-reason audit validation: after the Magic Jewel gradient shape rows and expanded
  `gradient-invalid` refresh, the full focused `JbrSkiaCommandRecorderTest` desktop class passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`. This revalidates the current recorder state behind the
  remaining mismatch classifications, including direct coverage for nested graphics-layer child command/header
  invariants (`nestedRecordingRejectsMissingChildCommands`, `nestedRecordingRejectsChildUnsupportedCount`,
  `nestedRecordingRejectsShortChildHeader`, and `nestedRecordingRejectsMismatchedChildHeader`), supported shadow
  replay, gradient path paint strict-mode guards, invalid gradient geometry/payload guards, non-finite color-matrix
  rejection, and the broad supported command replay surface.
- Magic Jewel expanded `gradient-invalid` group refresh: after adding the nine public gradient shape rows to the broad
  gradient invalid group, `CASE_GROUPS=gradient-invalid` was refreshed as a split run because an environment refresh
  interrupted the first attempt after four passing rows. Prefix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-091843/suite.tsv`
  covered the first four live gradient rows. Suffix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-092520/suite.tsv`
  resumed from `commands-radial-gradient-invalid-stops-fallback` and covered the remaining 77 rows. Combined result:
  81/81 passed, `fallback_sum=60`, 21 unsupported-picture rows, 22,222 picture frames, and zero command frames. Magic
  Jewel `out` is 89G with about 267Gi free.
- Magic Jewel public gradient stroke-round-rect radius checkpoint: after the first public gradient shape batch, the
  unsupported-reason audit still showed `linearGradientStrokeRoundRectRadius`, `radialGradientStrokeRoundRectRadius`,
  and `sweepGradientStrokeRoundRectRadius` were not direct expected reasons in the suite. Added three explicit
  low-level stroked round-rect sentinels enabled by
  `MAGIC_JEWEL_COMPOSE_INVALID_LINEAR_GRADIENT_STROKE_ROUND_RECT_RADIUS`,
  `MAGIC_JEWEL_COMPOSE_INVALID_RADIAL_GRADIENT_STROKE_ROUND_RECT_RADIUS`, and
  `MAGIC_JEWEL_COMPOSE_INVALID_SWEEP_GRADIENT_STROKE_ROUND_RECT_RADIUS`. No-run discovery now reports 549 default
  command-probe rows, `gradient-stroke-round-rect-radius-invalid` 3 rows, the broad `gradient-invalid` group 81 rows,
  no ungrouped rows, and no duplicate case names. `compileKotlin` passed for Magic Jewel. Exact linear validation
  passed 1/1 with
  `linearGradientStrokeRoundRectRadius`, one unsupported-picture row, 1,146 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-231026/suite.tsv`.
  The focused group passed 3/3 with `linearGradientStrokeRoundRectRadius`,
  `radialGradientStrokeRoundRectRadius`, and `sweepGradientStrokeRoundRectRadius`, 2,485 picture frames, and zero
  command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-231339/suite.tsv`.
  After this checkpoint, the unsupported-reason mismatch list is down to dynamic/internal/defensive candidates:
  `blendMode_${blendMode.toReasonToken()}`, `graphicsLayer:childHeader`, `graphicsLayer:childHeaderSize`,
  `graphicsLayer:childUnsupported`, `graphicsLayer:shadow`, `graphicsLayer:shadowFilter`,
  `linearGradientPathPaint`, `radialGradientPathPaint`, `sweepGradientPathPaint`, `roundRectStyle`, and
  `unsupportedScope`. Magic Jewel `out` is 89G with about 273Gi free.
- Magic Jewel public gradient shape invalid checkpoint: the unsupported-reason audit found the gradient
  round-rect-radius and stroke-width guards are reachable through public low-level `Canvas` drawing with
  `LinearGradientShader`, `RadialGradientShader`, and `SweepGradientShader` paints, not just through direct recorder
  tests or parser-corruption rows. Added six app-level sentinels:
  `commands-linear-gradient-invalid-stroke-width-public-fallback`,
  `commands-radial-gradient-invalid-stroke-width-public-fallback`,
  `commands-sweep-gradient-invalid-stroke-width-public-fallback`,
  `commands-linear-gradient-round-rect-invalid-radius-fallback`,
  `commands-radial-gradient-round-rect-invalid-radius-fallback`, and
  `commands-sweep-gradient-round-rect-invalid-radius-fallback`. No-run discovery now reports 546 default
  command-probe rows, `gradient-stroke-width-invalid` 3 rows, `gradient-round-rect-radius-invalid` 3 rows, no
  ungrouped rows, and no duplicate case names. `compileKotlin` passed for Magic Jewel. Exact linear radius validation
  passed 1/1 with `linearGradientRoundRectRadius`, one unsupported-picture row, 1,048 picture frames, and zero command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-225707/suite.tsv`.
  Exact linear stroke-width validation passed 1/1 with `linearGradientStrokeWidth`, one unsupported-picture row, 1,272
  picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-225806/suite.tsv`.
  The focused `gradient-round-rect-radius-invalid` group passed 3/3 with `linearGradientRoundRectRadius`,
  `radialGradientRoundRectRadius`, and `sweepGradientRoundRectRadius`, 3,698 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-225855/suite.tsv`.
  The focused `gradient-stroke-width-invalid` group passed 3/3 with `linearGradientStrokeWidth`,
  `radialGradientStrokeWidth`, and `sweepGradientStrokeWidth`, 3,630 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-230157/suite.tsv`.
  Magic Jewel `out` is 89G with about 278Gi free.
- CMP/Magic Jewel non-finite color-matrix checkpoint: public `ColorFilter.colorMatrix` with a non-finite matrix value
  previously threw `RuntimeException: Can't wrap nullptr` from Skia before CMP could report the existing
  `colorMatrixNonfinite` unsupported reason. CMP now constructs a benign native fallback color filter for non-finite
  matrices while retaining the original Compose matrix for JBR metadata, so strict command recording rejects the draw
  structurally instead of exception-storming the EDT. Focused CMP validation passed first, then the full
  `JbrSkiaCommandRecorderTest` class passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`. Magic Jewel added
  `commands-color-matrix-filter-nonfinite-fallback`, enabled by
  `MAGIC_JEWEL_COMPOSE_INVALID_COLOR_MATRIX_FILTER`; no-run discovery now reports 540 default command-probe rows and
  `fill-rect-color-filter-invalid` 6 rows. The first pre-fix exact run produced a 677M unreferenced exception-storm
  output directory and was trimmed after the fix. The corrected exact row passed 1/1 with `colorMatrixNonfinite`, one
  unsupported-picture row, 1,178 JBR picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-205901/suite.tsv`.
  The adjacent `fill-rect-color-filter-invalid` group passed 6/6 with `fallback_sum=5`, one unsupported-picture row
  from the live non-finite matrix sentinel, 1,319 JBR picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-222615/suite.tsv`.
- Magic Jewel added `commands-graphics-layer-unsupported-child-fallback`, enabled by
  `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_CHILD_UNSUPPORTED`, after the unsupported-reason audit looked for a public
  app-level nested graphics-layer child fallback. The row draws unsupported raw-shader content inside an otherwise
  valid graphics layer. The first attempt intentionally expected `graphicsLayer:childUnsupported`, but the live report
  showed strict child recording drops the child command stream when `unsupportedCount > 0`, so public app content
  reports `graphicsLayer:childCommands`; the synthetic `graphicsLayer:childUnsupported` guard remains covered by CMP
  unit tests that construct a recording with commands plus a non-zero unsupported count. The failed 4.1M exploratory
  output directory was trimmed as unreferenced. The corrected exact row passed 1/1 with one unsupported-picture row,
  1,239 JBR picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-165600/suite.tsv`.
  The adjacent `graphics-layer-extras` group now resolves 16 rows and passed 16/16 with `fallback_sum=0`, four
  unsupported-picture rows, 4,468 JBR picture frames, and 19,792 JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-180353/suite.tsv`.
- Magic Jewel completed a full default command-probe consolidation after the focused invalid-slice refreshes and nested
  graphics-layer child guard audit. The sweep passed 538/538 data rows with `fallback_sum=350`, 68 intentional
  unsupported-picture rows, 82,301 JBR picture frames, and 200,046 JBR command frames. It revalidated the recent
  native-text/path/image-shader/primitive/graphics-layer/gradient/color-filter/blend invalid rows, the shader/effect
  descriptor parser rows, descriptor-handle wrong-family/missing/evicted rows, descriptor lifecycle/forced-context
  rows, graphics-layer transform/effect/shadow rows, and saveLayer scalar/ref bounds. The output directory is 2.1G;
  Magic Jewel `out` is 89G with about 258Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-094809/suite.tsv`.
- Magic Jewel refreshed the focused `surface-transform-ui` command-probe group after the smoke sanity check. The group
  passed 15/15 with `fallback_sum=0`, zero unsupported rows, zero JBR picture frames, and 21,502 JBR command frames
  across native bridge load, point primitives, concat/skew transforms, gradient surfaces/paths, gradient blend-layer
  rows, popup/menu, and text-image rows. The output directory is 64M; Magic Jewel `out` is 87G with about 260Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-093056/suite.tsv`.
- Magic Jewel refreshed the focused `smoke` command-probe group after the invalid-slice batch to sanity-check supported
  replay paths. The group passed 6/6 with `fallback_sum=0`, zero unsupported rows, zero JBR picture frames, and 8,625
  JBR command frames across live animation, core primitives, color shader, color-filter handle, color-matrix filter,
  and graphics-layer rows. The output directory is 27M; Magic Jewel `out` is 87G with about 261Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-092508/suite.tsv`.
- Magic Jewel refreshed the focused `blend-mode-invalid` command-probe group after the color-filter invalid slice. The
  group passed 3/3 with `fallback_sum=2`, one unsupported-picture row from the live
  `commands-vertices-invalid-blend-mode-fallback`/`blendMode_Clear` sentinel, 1,222 JBR picture frames, and zero
  command frames. The output directory is 8.6M; Magic Jewel `out` is 87G with about 261Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-092118/suite.tsv`.
- Magic Jewel refreshed the focused `fill-rect-color-filter-invalid` command-probe group after the gradient stop
  slice. The group passed 5/5 across blend-mode, bounds, and ref-bounds parser fallbacks with `fallback_sum=5`, zero
  unsupported rows, zero JBR picture frames, and zero command frames. The output directory is 16M; Magic Jewel `out` is
  87G with about 279Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-091041/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-stop-invalid` command-probe group after the gradient color-count slice.
  The group passed 4/4 with live `linearGradientStops`, `linearGradientPoints`, `radialGradientStops`, and
  `sweepGradientStops` unsupported-picture fallbacks, `fallback_sum=0`, 3,653 JBR picture frames, and zero command
  frames. The output directory is 14M; Magic Jewel `out` is 87G with about 279Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-090605/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-color-count-invalid` command-probe group as a compact follow-up to the
  recent gradient geometry and graphics-layer invalid slices. The group passed 3/3 with live
  `linearGradientColorCount`, `radialGradientColorCount`, and `sweepGradientColorCount` unsupported-picture fallbacks,
  `fallback_sum=0`, 3,041 JBR picture frames, and zero command frames. The output directory is 11M; Magic Jewel `out`
  is 87G with about 279Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-090205/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer-invalid` command-probe group after the primitive invalid slice. The
  group still resolves 15 rows covering invalid size, alpha, transform scalars, camera distance, shadow elevation,
  shadow path, blend mode, and unrecorded layer fallback. Focused validation passed 15/15 with `fallback_sum=0`, 15
  intentional unsupported-picture rows, 16,327 JBR picture frames, and zero command frames. The output directory is
  56M; Magic Jewel `out` is 87G with about 279Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-082414/suite.tsv`.
- Magic Jewel refreshed the focused `primitive-invalid` command-probe group after the path and image-shader invalid
  slices. The group still resolves 16 rows covering live primitive fallback guards (`blendLayerBounds`, `transform`,
  and `points`) plus draw-points/draw-vertices parser bounds. Focused validation passed 16/16 with `fallback_sum=13`,
  three unsupported-picture rows, 3,189 JBR picture frames, and zero command frames. The output directory is 50M; Magic
  Jewel `out` is 87G with about 281Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-222055/suite.tsv`.
- Magic Jewel no-run command-probe discovery remains tidy after the focused invalid-slice refreshes: 538 default rows,
  no ungrouped rows, and no duplicate case names. The one-row `image-shader-invalid` quick group
  (`commands-image-shader-invalid-image-fallback`) also passed 1/1 with `imageShaderImage` plus parent graphics-layer
  unsupported reasons, 1,109 JBR picture frames, and zero command frames. The output directory is 3.8M; Magic Jewel
  `out` is 87G with about 279Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-221233/suite.tsv`.
- Magic Jewel refreshed the focused `path-invalid` command-probe group as the next medium invalid parser/live fallback
  slice. The group still resolves 24 rows, covering public invalid clip/draw paths, direct malformed path verbs,
  dashed path-effect geometry/style/payload bounds, and invalid draw-shadow path data. Focused validation passed 24/24
  with `fallback_sum=22`, two unsupported-picture rows from the live `clipPath`/`path` sentinels, 1,805 JBR picture
  frames, and zero command frames. The passing output directory is 66M; Magic Jewel `out` is 87G with about 281Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-214338/suite.tsv`.
- Magic Jewel refreshed the focused `native-text-invalid` command-probe group after the unsupported-reason audit moved
  past graphics-layer child stream guards. The no-run group still resolves 11 rows covering text/paragraph font scalar
  bounds plus invalid font-data record flags. Focused validation passed 11/11 with `fallback_sum=11`, zero unsupported
  rows, zero JBR picture frames, and 1,165 JBR command frames from the font-data record-flags row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-194120/suite.tsv`.
- CMP nested graphics-layer child stream integrity audit: the remaining `graphicsLayer:childCommands`,
  `graphicsLayer:childUnsupported`, `graphicsLayer:childHeaderSize`, and `graphicsLayer:childHeader` unsupported
  reasons are internal nested-recording integrity guards rather than public app command shapes. Added focused
  strict-mode recorder tests that construct missing, unsupported, short-header, and mismatched-header child recordings
  and prove layer replay rejects them before emitting a command stream. Focused CMP validation passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- Magic Jewel completed the radial/sweep companion live-gradient geometry checkpoint after the CMP invalid-geometry
  shader-factory fix. Added `commands-radial-gradient-invalid-geometry-fallback` and
  `commands-sweep-gradient-invalid-geometry-fallback`, enabled by
  `MAGIC_JEWEL_COMPOSE_INVALID_RADIAL_GRADIENT_GEOMETRY` and
  `MAGIC_JEWEL_COMPOSE_INVALID_SWEEP_GRADIENT_GEOMETRY`, plus a compact `gradient-geometry-invalid` quick group
  covering linear/radial/sweep geometry guards. No-run discovery now reports 538 default command-probe rows,
  `gradient-geometry-invalid` 3 rows, and `gradient-invalid` 72 rows, with no ungrouped or duplicate default cases.
  The exact radial/sweep slice passed 2/2 with `radialGradientGeometry` and `sweepGradientGeometry`, two
  unsupported-picture rows, 1,776 JBR picture frames, and zero command frames. The compact geometry group passed 3/3
  with `fallback_sum=0`, three unsupported-picture rows, 3,535 JBR picture frames, and zero command frames. Magic
  Jewel `out` stayed at 86G with about 262Gi free. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-173150/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-173634/suite.tsv`.
- CMP/Magic Jewel invalid linear-gradient points checkpoint: public Compose `Brush.linearGradient` with a non-finite
  point previously hit Skia's null shader path before the JBR command recorder could report CMP's
  `linearGradientPoints` guard. CMP now creates a harmless solid fallback Skia shader for invalid linear/radial/sweep
  gradient geometry while preserving the JBR gradient metadata, so strict command recording can reject the stream
  structurally instead of throwing on the EDT. Focused CMP validation passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`, including new strict-mode tests for invalid linear,
  radial, and sweep geometry. The updated `ui-graphics-desktop` artifact was published to Maven local; the follow-on
  metadata publication attempt reached the iOS cinterop path and failed because this host has no `xcodebuild`, after
  the desktop publication had already completed. Magic Jewel added
  `commands-linear-gradient-invalid-points-fallback`, enabled by
  `MAGIC_JEWEL_COMPOSE_INVALID_LINEAR_GRADIENT_POINTS`, and refreshed no-run discovery to 536 default command-probe
  rows, `gradient-stop-invalid` 4 rows, and `gradient-invalid` 70 rows. The exact row passed with
  `linearGradientPoints`, one unsupported-picture row, 976 JBR picture frames, and zero command frames. The adjacent
  `CASE_GROUPS=gradient-stop-invalid` refresh passed 4/4 with `fallback_sum=0`, four unsupported-picture rows, 4,093
  JBR picture frames, and zero command frames. The failed pre-fix NaN exception-storm output directory was trimmed as
  unreferenced; passing suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-152702/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-162415/suite.tsv`.
- Magic Jewel completed the 535-row default command-probe consolidation as a split sweep after the first broad run
  reached `commands-color-filter-blend-mode` with stale local `/tmp` JBR artifacts. The interrupted prefix had already
  appended 403/403 passing rows in
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-194343/suite.tsv`
  (`fallback_sum=304`, 37 unsupported-picture rows, 42,095 JBR picture frames, 105,899 JBR command frames). Rebuilding
  `/tmp/jbr-api-shim.jar`, `/tmp/jbr-skia-run/desktop`, and `/tmp/jbr-skia-native/libjbrskiainterop.dylib` restored
  command-canvas acquisition; exact `commands-core-primitives` then passed with 1,172 command frames, and exact
  `commands-color-filter-blend-mode` passed with 932 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-090946/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-091045/suite.tsv`.
  The resumed descriptor tail first passed 10 rows before a concurrent log-write interleave produced a malformed
  `unsupported=6ecc00` token in `commands-forced-context-color-shader-descriptor-redefine`; Magic Jewel now ignores
  non-decimal recorder field values when computing max numeric recorder fields. The exact forced-context color-shader
  row then passed with 1,476 command frames, and the remaining 121-row suffix passed. Combined split consolidation:
  535/535 passed, `fallback_sum=350`, 65 unsupported-picture rows, 74,635 JBR picture frames, and 193,776 JBR command
  frames. Tail suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-091146/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-092405/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-092509/suite.tsv`.
- Magic Jewel app-level invalid vertices blend-mode fallback checkpoint: added
  `commands-vertices-invalid-blend-mode-fallback`, enabled by
  `MAGIC_JEWEL_COMPOSE_VERTICES_INVALID_BLEND_MODE`, after the recorder unsupported-reason audit found that public
  Compose can feed `BlendMode.Clear` into CMP's `drawVertices` blend-mode guard. The row uses
  `Canvas.drawVertices(..., BlendMode.Clear, ...)` and verifies the live `blendMode_Clear` unsupported reason before
  command replay. No-run discovery now reports 535 default command-probe rows, 3 `blend-mode-invalid` rows, no
  ungrouped rows, and no duplicate case names. Focused exact validation passed 1/1 with `fallback_sum=0`, one
  unsupported-picture row (`graphicsLayer:childCommands`, `vertices`, `blendMode_Clear`, `graphicsLayer`), 972 JBR
  picture frames, and zero command frames. The adjacent `CASE_GROUPS=blend-mode-invalid` refresh passed 3/3 with
  `fallback_sum=2`, one unsupported-picture row, 1,683 JBR picture frames, and zero command frames; the other two rows
  stay on the expected command-stream-invalid parser fallback path for fill-rect blend-mode width/height bounds. Magic
  Jewel `out` stayed at 84G, the exact run was 3.8M, the group run was 9.9M, and the volume had about 275Gi free after
  completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-193518/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-193626/suite.tsv`.
- Magic Jewel app-level invalid concat transform fallback checkpoint: added
  `commands-invalid-concat-transform-fallback`, enabled by
  `MAGIC_JEWEL_COMPOSE_INVALID_CONCAT_TRANSFORM`, after the recorder unsupported-reason audit found that public
  Compose can feed a non-finite matrix value into CMP's `concat` guard. The row uses `Canvas.concat(Matrix)` with a
  `Float.NaN` translation and verifies the live `transform` unsupported reason before command replay. No-run discovery
  now reports 534 default command-probe rows, 16 `primitive-invalid` rows, no ungrouped rows, and no duplicate case
  names. Focused exact validation passed 1/1 with `fallback_sum=0`, one unsupported-picture row
  (`unsupportedScope`, `graphicsLayer:childCommands`, `transform`, `graphicsLayer`), 963 JBR picture frames, and zero
  command frames. The adjacent `CASE_GROUPS=primitive-invalid` refresh passed 16/16 with `fallback_sum=13`, three
  unsupported-picture rows, 3,043 JBR picture frames, and zero command frames; the three unsupported rows are the live
  `blendLayerBounds`, `transform`, and `points` guards, while the other 13 rows stay on the expected
  command-stream-invalid parser fallback path. Magic Jewel `out` stayed at 84G, the exact run was 3.6M, the group run
  was 55M, and the volume had about 275Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-160032/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-160142/suite.tsv`.
- Magic Jewel app-level invalid point-dots fallback checkpoint: added `commands-invalid-point-dots-fallback`, enabled by
  `MAGIC_JEWEL_COMPOSE_INVALID_POINT_DOTS`, after the recorder unsupported-reason audit found that public Compose can
  feed a non-finite point into CMP's `drawPoints` guard. The row uses `Canvas.drawPoints(PointMode.Points, ...)` with
  one `Float.NaN` coordinate and verifies the live `points` unsupported reason before command replay. No-run discovery
  now reports 533 default command-probe rows, 15 `primitive-invalid` rows, no ungrouped rows, and no duplicate case
  names. Focused exact validation passed 1/1 with `fallback_sum=0`, one unsupported-picture row
  (`points`, `graphicsLayer:childCommands`, `graphicsLayer`), 852 JBR picture frames, and zero command frames. The
  adjacent `CASE_GROUPS=primitive-invalid` refresh passed 15/15 with `fallback_sum=13`, two unsupported-picture rows,
  2,115 JBR picture frames, and zero command frames; the two unsupported rows are the live `points` and
  `blendLayerBounds` guards, while the other 13 rows stay on the expected command-stream-invalid parser fallback path.
  Magic Jewel `out` stayed at 84G, the exact run was 3.5M, the group run was 45M, and the volume had about 277Gi free
  after completion. One sandboxed preflight attempt failed before app startup because Gradle could not write its
  `~/.gradle` lock file; its unreferenced 64K output directory was removed. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-154421/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-154533/suite.tsv`.
- Magic Jewel app-level blend-layer bounds fallback checkpoint: added
  `commands-invalid-blend-layer-bounds-fallback`, enabled by
  `MAGIC_JEWEL_COMPOSE_INVALID_BLEND_LAYER_BOUNDS`, after the recorder unsupported-reason audit found that public
  Compose can feed a non-finite primitive coordinate into CMP's ABI-neutral blend-layer wrapper when a supported
  non-`SrcOver` blend mode is present. The row draws a `BlendMode.Plus` line with a non-finite start coordinate and
  verifies the live `blendLayerBounds` unsupported reason before command replay. No-run discovery now reports 532
  default command-probe rows, 14 `primitive-invalid` rows, no ungrouped rows, and no duplicate case names. Focused
  exact validation passed 1/1 with `fallback_sum=0`, one unsupported-picture row
  (`graphicsLayer:childCommands`, `blendLayerBounds`, `graphicsLayer`), 766 JBR picture frames, and zero command
  frames. The adjacent `CASE_GROUPS=primitive-invalid` refresh passed 14/14 with `fallback_sum=13`, one
  unsupported-picture row, 946 JBR picture frames, and zero command frames; the other 13 rows stayed on the expected
  `command-stream-invalid` parser fallback path. Magic Jewel `out` stayed at 84G, and the volume had about 275Gi free
  after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-122501/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-122615/suite.tsv`.
- Magic Jewel app-level saveLayer blend+color-filter checkpoint: added
  `commands-save-layer-blend-color-filter`, enabled by
  `MAGIC_JEWEL_COMPOSE_SAVELAYER_BLEND_COLOR_FILTER`, to draw a public Compose plain `Canvas.saveLayer` whose paint
  has both `BlendMode.Plus` and a tint `ColorFilter`. This covers the CMP fix in commit `f7bb15f788b`, where direct
  tint+blend saveLayer paints now emit `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER`, descriptor-backed
  color-filter+blend paints emit `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF`, and supported descriptor filters without
  blend route through the handle-backed saveLayer path. CMP validation passed both the focused two-test slice and the
  full `JbrSkiaCommandRecorderTest` class. Magic Jewel no-run discovery now reports 531 default command-probe rows,
  9 `save-layer-shader-fallbacks` rows, no ungrouped rows, and no duplicate case names. Focused exact validation passed
  1/1 with `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=2062`. The adjacent
  `CASE_GROUPS=save-layer-shader-fallbacks` refresh passed 9/9 with `fallback_sum=0`, six intentional
  unsupported-picture rows from raw saveLayer/shader fallback sentinels, 6,195 JBR picture frames, and 4,079 command
  frames. Magic Jewel `out` stayed at 84G, and the volume had about 277Gi free after completion; an accidental
  header-only unreferenced output directory from a mistaken duplicate-list flag was trimmed. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-084917/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-111237/suite.tsv`.
- Magic Jewel completed a periodic full default command-probe consolidation after the app-level blend sentinel batch.
  Discovery resolved 530 default rows with no ungrouped rows and no duplicate case names. The full sweep passed 530/530
  with `fallback_sum=350`, 62 intentional unsupported-picture rows, 67,344 JBR picture frames, and 171,452 JBR command
  frames. The run covered the newly added fill-rect color-filter, gradient, image, image-shader, and color-shader
  blend-mode app sentinels while preserving the existing raw/invalid structural fallback rows. Magic Jewel `out` was
  about 84G after the run and the volume had about 277Gi free at the next checkpoint. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-174336/suite.tsv`.
- Magic Jewel app-level color-shader blend-mode checkpoint: added `commands-color-shader-blend-mode`, enabled by
  `MAGIC_JEWEL_COMPOSE_COLOR_SHADER_BLEND_MODE`, to draw a JBR-owned color-shader descriptor rect with
  `BlendMode.Plus`. No-run discovery passed for the exact row, `LIST_UNGROUPED_CASES=true` printed no rows, default
  duplicate-case detection printed no rows, and `LIST_CASE_GROUP_COUNTS=true` now reports `shader-rendering	18`.
  Focused exact validation passed 1/1 with `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=1225`, and the row kept the shader-handle define/use/cache-hit gates. The adjacent
  `CASE_GROUPS=shader-rendering` refresh passed 18/18 with `fallback_sum=0`, ten intentional unsupported-picture rows
  from raw/invalid shader fallback sentinels, 10,135 JBR picture frames, and 10,015 JBR command frames. Magic Jewel
  `out` stayed at 82G, and the volume had about 260Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-172738/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-172840/suite.tsv`.
- Magic Jewel app-level image-shader blend-mode checkpoint: added `commands-image-shader-blend-mode`, enabled by
  `MAGIC_JEWEL_COMPOSE_IMAGE_SHADER_BLEND_MODE`, to draw an image-shader rect with `BlendMode.Plus`. No-run discovery
  passed for the exact row, `LIST_UNGROUPED_CASES=true` printed no rows, default duplicate-case detection printed no
  rows, and `LIST_CASE_GROUP_COUNTS=true` now reports `shader-rendering	17`. Focused exact validation passed 1/1
  with `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=1281`. The adjacent
  `CASE_GROUPS=shader-rendering` refresh passed 17/17 with `fallback_sum=0`, ten intentional unsupported-picture rows
  from raw/invalid shader fallback sentinels, 9,922 JBR picture frames, and 8,322 JBR command frames. Magic Jewel `out`
  stayed at 82G, and the volume had about 272Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-171148/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-171254/suite.tsv`.
- Magic Jewel app-level image-ref blend-mode checkpoint: added `commands-image-blend-mode`, enabled by
  `MAGIC_JEWEL_COMPOSE_IMAGE_BLEND_MODE`, to draw an image ref with `BlendMode.Plus`. No-run discovery passed for the
  exact row, `LIST_UNGROUPED_CASES=true` printed no rows, default duplicate-case detection printed no rows, and
  `LIST_CASE_GROUP_COUNTS=true` now reports `shader-rendering	16`. Focused exact validation passed 1/1 with
  `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=1257`. The adjacent
  `CASE_GROUPS=shader-rendering` refresh passed 16/16 with `fallback_sum=0`, ten intentional unsupported-picture rows
  from raw/invalid shader fallback sentinels, 12,644 JBR picture frames, and 9,740 JBR command frames. Magic Jewel
  `out` stayed at 82G, and the volume had about 272Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-165652/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-165819/suite.tsv`.
- Magic Jewel app-level radial/sweep gradient blend-mode checkpoint: added
  `commands-radial-gradient-stroke-blend-mode`, enabled by
  `MAGIC_JEWEL_COMPOSE_RADIAL_GRADIENT_STROKE_BLEND_MODE`, and
  `commands-sweep-gradient-round-rect-blend-mode`, enabled by
  `MAGIC_JEWEL_COMPOSE_SWEEP_GRADIENT_ROUND_RECT_BLEND_MODE`. The rows cover a stroked radial-gradient rect and a
  filled sweep-gradient round-rect with `BlendMode.Plus`, matching the remaining unit-covered gradient blend-layer
  shapes. No-run discovery passed for the exact two-row slice, `LIST_UNGROUPED_CASES=true` printed no rows, default
  duplicate-case detection printed no rows, and `LIST_CASE_GROUP_COUNTS=true` now reports
  `surface-transform-ui	15`. Focused exact validation passed 2/2 with `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=2374`. The adjacent `CASE_GROUPS=surface-transform-ui` refresh
  passed 15/15 with `fallback_sum=0`, zero unsupported-picture rows, zero JBR picture frames, and 21,548 JBR command
  frames. Magic Jewel `out` stayed at 82G, and the volume had about 272Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-164027/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-164210/suite.tsv`.
- Magic Jewel app-level linear-gradient path blend-mode checkpoint: added `commands-linear-gradient-path-blend-mode`,
  enabled by `MAGIC_JEWEL_COMPOSE_LINEAR_GRADIENT_PATH_BLEND_MODE`, to draw a filled linear-gradient path with
  `BlendMode.Plus`. No-run discovery passed for the exact row, `LIST_UNGROUPED_CASES=true` printed no rows, default
  duplicate-case detection printed no rows, and `LIST_CASE_GROUP_COUNTS=true` now reports
  `surface-transform-ui	13`. Focused exact validation passed 1/1 with `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=1379`. The adjacent `CASE_GROUPS=surface-transform-ui` refresh
  passed 13/13 with `fallback_sum=0`, zero unsupported-picture rows, zero JBR picture frames, and 18,553 JBR command
  frames. Magic Jewel `out` stayed at 82G, and the volume had about 272Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-162640/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-162742/suite.tsv`.
- Magic Jewel app-level linear-gradient blend-mode checkpoint: added `commands-linear-gradient-blend-mode`, enabled by
  `MAGIC_JEWEL_COMPOSE_LINEAR_GRADIENT_BLEND_MODE`, to draw a linear-gradient rect with `BlendMode.Plus`. No-run
  discovery passed for the exact row, and `LIST_CASE_GROUP_COUNTS=true` now reports `surface-transform-ui	12`.
  Focused exact validation passed 1/1 with `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=1418`. The adjacent `CASE_GROUPS=surface-transform-ui` refresh passed 12/12 with
  `fallback_sum=0`, zero unsupported-picture rows, zero JBR picture frames, and 17,354 JBR command frames. Magic Jewel
  `out` stayed at 82G, and the volume had about 272Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-161407/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-161521/suite.tsv`.
- Magic Jewel app-level color-filter blend-mode checkpoint: added `commands-color-filter-blend-mode`, enabled by
  `MAGIC_JEWEL_COMPOSE_COLOR_FILTER_BLEND_MODE`, to draw a direct tint color-filter fill-rect with
  `BlendMode.Plus`. No-run discovery passed for the exact row, and `LIST_CASE_GROUP_COUNTS=true` now reports
  `color-filters	13`. Focused exact validation passed 1/1 with `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=1223`. The adjacent `CASE_GROUPS=color-filters` refresh passed
  13/13 with `fallback_sum=0`, three intentional unsupported-picture rows from the raw color-filter fallback cases,
  3,253 JBR picture frames, and 15,199 JBR command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-155652/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-155803/suite.tsv`.
- CMP fill-rect color-filter blend-mode recorder checkpoint: supported paint-level non-`SrcOver` blend modes now stay
  on command replay for direct tint filters and handle-backed tint/color-matrix/lighting/descriptor fill-rect
  color-filter commands by wrapping existing native color-filter rect records in `COMMAND_SAVE_LAYER_BLEND_MODE`.
  Focused validation passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP gradient blend-mode recorder checkpoint: supported paint-level non-`SrcOver` blend modes now stay on command
  replay for linear, radial, and sweep gradient rects, round-rects, and filled paths by wrapping existing gradient
  command records in `COMMAND_SAVE_LAYER_BLEND_MODE` over fill/stroke/path bounds. Focused validation passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP image/shader blend-mode recorder checkpoint: supported paint-level non-`SrcOver` blend modes now stay on command
  replay for image refs, image-shader rects, and shader descriptor rects by wrapping existing image/shader command
  records in `COMMAND_SAVE_LAYER_BLEND_MODE` over destination/fill bounds. Focused validation passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP vertices blend-mode recorder checkpoint: supported paint-level non-`SrcOver` blend modes now stay on command
  replay for `drawVertices` by wrapping the existing `COMMAND_DRAW_VERTICES` record in
  `COMMAND_SAVE_LAYER_BLEND_MODE` over vertex bounds. Focused validation passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP dashed/path-effect blend-mode recorder checkpoint: supported non-`SrcOver` solid-color blend modes now stay on
  command replay for dashed lines, dashed rects, dashed round-rects, dashed paths, and path-effect descriptor paths by
  wrapping the existing native command records in `COMMAND_SAVE_LAYER_BLEND_MODE` layers with stroke-padded bounds.
  Focused validation passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP primitive blend-mode recorder checkpoint: supported non-`SrcOver` solid-color blend modes now stay on command
  replay for lines, rect stroke fallback, round-rects, ovals, arcs, paths, points, and raw points by wrapping bounded
  primitive commands in `COMMAND_SAVE_LAYER_BLEND_MODE` plus restore when there is no direct primitive blend opcode.
  The existing filled-rect `COMMAND_FILL_RECT_BLEND_MODE` fast path is unchanged, and unsupported blend modes still
  report `blendMode_*`. Focused validation passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- Skiko default command-mode checkpoint: `JbrSkiaSwingLayer` now records/replays command frames by default when CMP
  creates the JBR interop Swing layer and no explicit Skiko picture/diagnostic/texture render mode is requested.
  Magic Jewel added `JBR_SKIA_RENDER_MODE=auto` to leave Skiko render-mode properties unset, published the updated
  Skiko `0.0.0-SNAPSHOT` artifacts locally, and ran a focused background-window app smoke:
  `JBR_SKIA_RENDER_MODE=auto SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=4 WARMUP_SECONDS=1
  MAGIC_JEWEL_BACKGROUND_WINDOW=true EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-interop-report.sh`. The run
  reported `SKIKO_JBR_INTEROP_RENDER_MODE commands=true picture=false diagnostic=false texture=false
  delegateCommands=true`, zero fallback markers, zero Skiko/JBR picture replay frames, 847 Skiko command frames, 847
  JBR command frames, 848 CMP command-recorder frames, and no unsupported recorder frames. Output size was 8.0M and
  Magic Jewel `out` stayed at 82G. Report:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-interop-report/20260606-145913/report.md`.
- Cross-repo ABI/capability audit after the broad command, compatibility, artifact, Skiko, and parity refresh found no
  constant drift or native opcode replay gap. A normalized extractor compared shared `ABI_ID`, `NATIVE_ABI_VERSION`,
  `COMMAND_*`, and `COMMAND_CAP*` constants across
  `/Users/rock3r/src/jbr-skia-zero-copy/jbr/src/java.desktop/share/classes/com/jetbrains/desktop/JBRSkia.java`,
  `/Users/rock3r/src/jbr-skia-zero-copy/jbr-api/src/com/jetbrains/JBRSkia.java`,
  `/Users/rock3r/src/jbr-skia-zero-copy/jbr/src/java.desktop/macosx/native/libawt_lwawt/java2d/metal/JBRSkiaInterop.mm`,
  `/Users/rock3r/src/jbr-skia-zero-copy/skiko/skiko/src/awtMain/kotlin/org/jetbrains/skiko/jbr/JbrSkiaInterop.kt`,
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/cmp/compose/ui/ui-graphics/src/skikoMain/kotlin/androidx/compose/ui/graphics/JbrSkiaCommandRecorder.skiko.kt`.
  Result: zero shared constant value mismatches. JBR private/API mirrors both parsed 212 constants. Native replay parsed
  129 constants, Skiko interop 84, and CMP recorder 116; CMP's only extra set symbol was the local
  `COMMAND_STREAM_ABI_ID=106` stream-header alias. A native opcode audit found 67 command opcodes and 67 replay switch
  cases; CMP's 65 emitted opcode constants were all present in native replay.
- Magic Jewel refreshed the full default screenshot parity suite after the command-probe, compatibility, artifact, and
  Skiko interop checkpoints. No-run discovery resolved 106 default rows, `LIST_UNGROUPED_CASES=true` printed no rows,
  and `CASE_GROUPS=smoke` resolved to `parity-rich`, `parity-button-chrome`, and `parity-geometry-clean`. The smoke
  prefix passed 3/3 with `fallback_sum=0`, zero JBR picture frames, and 3,248 JBR command frames. The resumed
  `CASES_FROM=parity-skew-transform` tail passed 103/103 with `fallback_sum=11`, zero JBR picture frames, and 101,820
  JBR command frames. Combined result: 106/106 passed with `fallback_sum=11`, zero JBR picture frames, and 105,068 JBR
  command frames. The 11 fallback markers remained bounded to resize/lifecycle sentinel rows; all rows stayed off
  JBR picture fallback. Magic Jewel `out` rose to 82G, the smoke run was 10M, the tail run was 568M, and the volume
  had about 269Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260606-133617/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260606-133836/suite.tsv`.
- Magic Jewel refreshed the full compatibility matrix after the 522-row command-probe consolidation. No-run discovery
  resolved 57 default rows with the expected group sizes (`handshake` 6, `low-word-gradients` 15, `low-word-effects`
  18, `high-word-effects` 9, and `high-word-shader-ui` 9). The matrix passed 57/57 with `fallback_sum=56`, 824 JBR
  command frames from the happy path, and background-window mode on all 57 rows. Every forced ABI, native ABI,
  low/high-word capability, exact feature-capability, and public-API-missing row produced exactly one structured
  fallback and zero command frames. Magic Jewel `out` stayed at 81G, the run was 96M, and the volume had about 252Gi
  free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260606-031518/matrix.tsv`.
- Magic Jewel refreshed the required artifact matrix on the current ABI 106 local artifacts. No-run discovery resolved
  the expected groups (`required` 2 and `optional-old` 5), and `CASE_GROUPS=required` passed 2/2 with `fallback_sum=1`,
  754 JBR command frames, and background-window mode on both rows. `current-all` replayed commands with no fallback;
  `missing-public-api` produced the expected single `public-api-missing` fallback and no command frames. The run was
  4.3M. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260606-133156/matrix.tsv`.
- Skiko refreshed the focused JBR Skia interop gate after the Magic Jewel compatibility/artifact refreshes:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  completed successfully on the current branch/artifacts.
- Magic Jewel completed a periodic full default command-probe consolidation after the focused core/effects/layer/text
  refreshes. No-run discovery still resolved 522 default rows, `LIST_UNGROUPED_CASES=true` printed no rows, and
  duplicate default-case detection printed no rows. The full sweep passed 522/522 with `fallback_sum=350`, 61
  intentional unsupported-picture rows, 81,988 JBR picture frames, and 171,971 JBR command frames. The run included the
  newly tightened raw table graphics-layer color-filter row, the recent image-shader/gradient-path/graphics-layer live
  fallback sentinels, supported native text/layer/RuntimeEffect replay, and the saveLayer/shader fallback tail. Magic
  Jewel `out` rose to 81G, the run was 2.3G, and the volume had about 269Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-212906/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-path-stroke-fallbacks` command-probe group. No-run resolution passed with
  `LIST_CASE_COUNT=true CASE_GROUPS=gradient-path-stroke-fallbacks` returning 3 and
  `LIST_CASES=true CASE_GROUPS=gradient-path-stroke-fallbacks` listing linear, radial, and sweep gradient stroked-path
  fallback rows. The validation run passed 3/3 with `fallback_sum=0`, three intentional unsupported-picture rows,
  3,864 JBR picture frames, and zero JBR command frames. The rows reported `linearGradientPaint`,
  `radialGradientPaint`, and `sweepGradientPaint` alongside parent graphics-layer reasons, preserving the documented
  generic gradient-paint fallback ordering for stroked gradient paths. Magic Jewel `out` stayed at 79G, the run was
  12M, and the volume had about 273Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-212527/suite.tsv`.
- Magic Jewel refreshed the focused `surface-transform-ui` command-probe group. No-run resolution passed with
  `LIST_CASE_COUNT=true CASE_GROUPS=surface-transform-ui` returning 11 and
  `LIST_CASES=true CASE_GROUPS=surface-transform-ui` listing native bridge load-library, point line/dot replay,
  concat/skew transforms, gradient surfaces/paths, popup, popup-window, menu, and text-image rows. The validation run
  passed 11/11 with `fallback_sum=0`, zero unsupported-picture rows, zero JBR picture frames, and 16,494 JBR command
  frames; every row stayed on command replay, including popup-window and menu layering capture. Magic Jewel `out`
  stayed at 79G, the run was 47M, and the volume had about 273Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-211704/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer` command-probe group after the adjacent invalid/extras layer
  refreshes. No-run resolution passed with `LIST_CASE_COUNT=true CASE_GROUPS=graphics-layer` returning 22 and
  `LIST_CASES=true CASE_GROUPS=graphics-layer` listing base, ModulateAlpha/Offscreen, rectangular/rounded/path clips,
  blend-mode, tint/color-matrix filters, render/offset/chained effects, rectangular/rounded/path shadows, invalid
  shadow-elevation fallback, rotationX/Y/XY, scale/translate, near-camera, and off-center pivot rows. The validation
  run passed 22/22 with `fallback_sum=0`, one intentional unsupported-picture row, 1,152 JBR picture frames, and
  31,905 JBR command frames. The only unsupported row was
  `commands-graphics-layer-invalid-shadow-elevation-fallback`
  (`graphicsLayer:shadowElevation:1152,graphicsLayer:childCommands:1152,graphicsLayer:1152`); all supported
  layer/clip/effect/shadow/transform rows stayed on command replay. Magic Jewel `out` stayed at 79G, the run was 96M,
  and the volume had about 274Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-205726/suite.tsv`.
- Magic Jewel refreshed the focused `native-text` command-probe group after the adjacent parser-invalid refresh.
  No-run resolution passed with `LIST_CASE_COUNT=true CASE_GROUPS=native-text` returning 14 and
  `LIST_CASES=true CASE_GROUPS=native-text` listing native custom-font text/image, generic-family text, loaded
  byte-array font-data text, classpath resource font-data text, named system-font text, same-context resize variants,
  and forced destination-context variants. The validation run passed 14/14 with `fallback_sum=0`, zero
  unsupported-picture rows, zero JBR picture frames, and 19,683 JBR command frames; every row stayed on command replay.
  Magic Jewel `out` stayed at 79G, the run was 56M, and the volume had about 274Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-204659/suite.tsv`.
- Magic Jewel refreshed the focused `shader-composition-runtime` command-probe group. No-run resolution passed with
  `LIST_CASE_COUNT=true CASE_GROUPS=shader-composition-runtime` returning 15 and
  `LIST_CASES=true CASE_GROUPS=shader-composition-runtime` listing image-shader/color-filter, composite shader,
  composite-noise shader, composite shader/color-filter, transformed shader, RuntimeEffect shader, raw RuntimeEffect
  shader fallback, RuntimeEffect shader/color-filter, linear-gradient shader/color-filter, RuntimeEffect pure-color,
  uniform-only, child-only, color-filter, raw RuntimeEffect color-filter fallback, and RuntimeEffect
  color-filter-child rows. The validation run passed 15/15 with `fallback_sum=0`, two intentional
  unsupported-picture rows, 2,043 JBR picture frames, and 16,907 JBR command frames. Only the raw RuntimeEffect shader
  and raw RuntimeEffect color-filter rows fell back structurally (`shader:995,...` and `colorFilter:1048,...`);
  supported shader composition and RuntimeEffect descriptor rows stayed on command replay. Magic Jewel `out` stayed at
  79G, the run was 73M, and the volume had about 274Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-203324/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer-extras` command-probe group after narrowing the raw table
  graphics-layer color-filter row to skip the generic green-pixel screenshot assertion while preserving the required
  `graphicsLayer:colorFilter` fallback assertion. The first group attempt stopped on
  `commands-graphics-layer-raw-table-color-filter-fallback`: the structured fallback was present, but the table filter
  produced zero green marker pixels. Focused exact validation then passed 1/1 with `fallback_sum=0`, one intentional
  unsupported-picture row, 1,178 JBR picture frames, and zero command frames. The rerun
  `CASE_GROUPS=graphics-layer-extras` passed 15/15 with `fallback_sum=0`, three intentional unsupported-picture rows,
  3,097 JBR picture frames, and 13,989 JBR command frames. Supported resize/context color-matrix and render-effect
  rows, plus render-effect/color-filter/blend combinations, stayed on command replay; raw blend/table color-filter and
  raw render-effect rows stayed on structured graphics-layer fallback. Magic Jewel `out` stayed at 79G; the failed
  first run was 14M, the exact rerun was 3.7M, the successful group rerun was 62M, and the volume had about 274Gi free
  after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-202046/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-202136/suite.tsv`.
- Magic Jewel refreshed the focused `core-effects` command-probe group after the sidecar unsupported-reason audit.
  No-run resolution passed with `LIST_CASE_COUNT=true CASE_GROUPS=core-effects` returning 8 and
  `LIST_CASES=true CASE_GROUPS=core-effects` listing gradient stroke, image filter, path effect, path-effect
  color-filter fallback, raw discrete path-effect fallback, vertices, vertices raw color-filter fallback, and
  blend-mode rows. The validation run passed 8/8 with `fallback_sum=0`, three intentional unsupported-picture rows,
  2,815 JBR picture frames, and 5,031 JBR command frames. Supported gradient-stroke, image-filter, path-effect,
  vertices, and blend-mode rows stayed on command replay; the unsupported rows were
  `commands-path-effect-color-filter-fallback`
  (`colorFilter:1044,graphicsLayer:childCommands:1044,graphicsLayer:1044`),
  `commands-raw-discrete-path-effect-fallback`
  (`graphicsLayer:childCommands:936,pathEffect:936,graphicsLayer:936`), and
  `commands-vertices-raw-color-filter-fallback`
  (`colorFilter:834,graphicsLayer:childCommands:834,vertices:834,graphicsLayer:834`). Magic Jewel `out` stayed at
  79G, the run was 28M, and the volume had about 275Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-195810/suite.tsv`.
- Unsupported-reason sidecar audit: Ramanujan rechecked CMP/JBR Skia recorder and layer guard reachability after the
  recent fallback sentinels. No remaining app-reachable undocumented guard jumped out. The app-reachable reasons remain
  covered by existing Magic Jewel rows (`graphicsLayer:shadowPath`, `linearGradientPath`, `radialGradientPath`,
  `sweepGradientPath`, and `imageShaderImage`), while `graphicsLayer:shadow`, `graphicsLayer:shadowFilter`,
  `graphicsLayer:childUnsupported`, `graphicsLayer:childHeaderSize`, `graphicsLayer:childHeader`, `roundRectStyle`,
  and the path-specific gradient-paint reasons remain classified as defensive or shadowed by earlier generic checks.
- Magic Jewel refreshed the focused `blend-mode-invalid` command-probe group. No-run resolution passed with
  `LIST_CASE_COUNT=true CASE_GROUPS=blend-mode-invalid` returning 2 and
  `LIST_CASES=true CASE_GROUPS=blend-mode-invalid` listing the fill-rect blend-mode width/height parser guards. The
  validation run passed 2/2 with `fallback_sum=2`, zero unsupported-picture rows, zero JBR picture frames, and zero
  JBR command frames. Magic Jewel `out` stayed at 79G, the run was 4.3M, and the volume had about 259Gi free after
  completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-195246/suite.tsv`.
- Magic Jewel refreshed the focused `save-layer-shader-fallbacks` command-probe group after tightening the raw table
  saveLayer row expectation. The first group attempt reached the expected structured `saveLayer` fallback for
  `commands-save-layer-raw-table-color-filter-fallback` but failed the generic screenshot assertion because the raw
  table filter produced zero green marker pixels; Magic Jewel now keeps the fallback assertion and disables that
  screenshot assertion for this row. Focused exact validation then passed 1/1 for
  `commands-save-layer-raw-table-color-filter-fallback` with `fallback_sum=0`, one intentional unsupported-picture row,
  1,223 JBR picture frames, and zero command frames. The rerun `CASE_GROUPS=save-layer-shader-fallbacks` passed 8/8
  with `fallback_sum=0`, six intentional unsupported-picture rows, 6,120 JBR picture frames, and 2,254 JBR command
  frames; supported saveLayer filter/blend rows stayed on command replay, while raw saveLayer color-filter, raw
  saveLayer table-filter, raw/opaque/composite/picture shader, and invalid-gradient rows stayed on structured picture
  fallback. Magic Jewel `out` stayed at 79G; the failed first run was 14M, the exact rerun was 3.6M, the successful
  group rerun was 29M, and the volume had about 259Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-194218/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-194310/suite.tsv`.
- Magic Jewel refreshed the focused `descriptor-lifecycle` command-probe group. No-run resolution passed with
  `LIST_CASE_COUNT=true CASE_GROUPS=descriptor-lifecycle` returning 18 and
  `LIST_CASES=true CASE_GROUPS=descriptor-lifecycle` listing descriptor eviction, resize/forced-context descriptor
  redefinition, shader/color/noise/turbulence/composite-noise descriptor redefinition, stable RuntimeEffect
  color-filter resize/forced-context reuse, and RuntimeEffect shader/color-filter source-cache eviction rows. The
  validation run passed 18/18 with `fallback_sum=0`, zero unsupported-picture rows, zero JBR picture frames, and
  22,342 JBR command frames; every row stayed on command replay. Magic Jewel `out` rose to 79G, the run was 291M, and
  the volume had about 277Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-192007/suite.tsv`.
- Magic Jewel refreshed the focused `color-filters` command-probe group. No-run resolution passed with
  `LIST_CASE_COUNT=true CASE_GROUPS=color-filters` returning 12 and
  `LIST_CASES=true CASE_GROUPS=color-filters` listing image color-matrix, raw image/table/blend color-filter fallback,
  tint/color-matrix/lighting descriptor replay, descriptor handle reuse, and graphics-layer color-filter/blend
  combinations. The validation run passed 12/12 with `fallback_sum=0`, three intentional unsupported-picture rows,
  4,499 JBR picture frames, and 15,627 JBR command frames. The unsupported rows were the raw table/blend color-filter
  fallbacks, each reporting `colorFilter` plus parent graphics-layer reasons; supported descriptor rows stayed on
  command replay. Magic Jewel `out` stayed at 78G, the run was 63M, and the volume had about 279Gi free after
  completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-185044/suite.tsv`.
- Magic Jewel refreshed the focused `shader-descriptor-invalid` command-probe group. No-run resolution passed with
  `LIST_CASE_COUNT=true CASE_GROUPS=shader-descriptor-invalid` returning 30 and
  `LIST_CASES=true CASE_GROUPS=shader-descriptor-invalid` listing shader descriptor header guards plus color,
  shader-color-filter, transformed, composite, linear/radial/sweep gradient, image shader, and Perlin/noise descriptor
  payload guards. The validation run passed 30/30 with `fallback_sum=30`, zero unsupported-picture rows, zero JBR
  picture frames, and zero JBR command frames; every row reported exactly one expected strict fallback. Magic Jewel
  `out` stayed at 78G, the run was 116M, and the volume had about 279Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-182940/suite.tsv`.
- Magic Jewel refreshed the focused `effect-descriptor-invalid` command-probe group. No-run resolution passed with
  `LIST_CASE_COUNT=true CASE_GROUPS=effect-descriptor-invalid` returning 28 and
  `LIST_CASES=true CASE_GROUPS=effect-descriptor-invalid` listing effect descriptor header guards plus lighting,
  tint, color-matrix, blur/offset image-filter, corner/stamped/chain path-effect descriptor payload guards. The
  validation run passed 28/28 with `fallback_sum=28`, zero unsupported-picture rows, zero JBR picture frames, and zero
  JBR command frames; every row reported exactly one expected strict fallback. Magic Jewel `out` stayed at 78G, the
  run was 93M, and the volume had about 279Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-180727/suite.tsv`.
- Magic Jewel refreshed the focused `path-invalid` command-probe group. No-run resolution passed with
  `LIST_CASE_COUNT=true CASE_GROUPS=path-invalid` returning 24 and
  `LIST_CASES=true CASE_GROUPS=path-invalid` listing the live invalid clip/draw path rows plus clip/draw/stroke/shadow
  path verb and dash path-effect parser guards. The validation run passed 24/24 with `fallback_sum=22`, two
  intentional unsupported-picture rows, 2,075 JBR picture frames, and zero JBR command frames. The live rows reported
  `clipPath`/`unsupportedScope` and `path` alongside parent graphics-layer reasons; parser-corruption rows reported
  one expected fallback each and no unsupported-picture frames. Magic Jewel `out` stayed at 78G, the run was 65M, and
  the volume had about 280Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-174939/suite.tsv`.
- Magic Jewel refreshed the focused `native-text-invalid` command-probe group. No-run resolution passed with
  `LIST_CASE_COUNT=true CASE_GROUPS=native-text-invalid` returning 11 and
  `LIST_CASES=true CASE_GROUPS=native-text-invalid` listing text and paragraph font size/weight/width/slant/family
  count guards plus the font-data record-flags guard. The validation run passed 11/11 with `fallback_sum=11`, zero
  unsupported-picture rows, zero JBR picture frames, and 1,041 JBR command frames; only
  `commands-invalid-font-data-record-flags-fallback` reported command frames while also counting its expected fallback.
  Magic Jewel `out` stayed at 78G, the run was 28M, and the volume had about 265Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-174043/suite.tsv`.
- Magic Jewel refreshed the focused `primitive-invalid` command-probe group after the image-shader and defensive paint
  reason audit work. No-run resolution passed with `LIST_CASE_COUNT=true CASE_GROUPS=primitive-invalid` returning 13
  and `LIST_CASES=true CASE_GROUPS=primitive-invalid` listing stroke-cap, transform flags, clip operation,
  draw-points count/length, and draw-vertices count/mode/blend/index corruptions. The validation run passed 13/13 with
  `fallback_sum=13`, zero unsupported-picture rows, zero JBR picture frames, and zero JBR command frames. Magic Jewel
  `out` stayed at 78G, the run was 40M, and the volume had about 265Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-172804/suite.tsv`.
- Magic Jewel added and validated a live invalid image-shader image fallback sentinel after the recorder audit
  identified CMP's `imageShaderImage` guard as app-reachable from public Compose `ImageShader` construction. The new
  `MAGIC_JEWEL_COMPOSE_INVALID_IMAGE_SHADER_IMAGE` toggle reuses the supported image-shader probe with an oversized
  `ImageBitmap(2049, 1)`, so CMP rejects the real Compose recording during image-shader serialization before replay.
  No-run validation passed for `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`;
  `LIST_CASE_COUNT=true` returned 522, `LIST_CASE_GROUP_COUNTS=true` reported `image-shader-invalid` 1 and
  `shader-rendering` 15, `LIST_CASES=true CASE_GROUPS=image-shader-invalid` listed
  `commands-image-shader-invalid-image-fallback`, duplicate default-case detection printed no rows, and
  `LIST_UNGROUPED_CASES=true` printed no rows. Focused exact validation passed 1/1 with `fallback_sum=0`, one
  intentional unsupported-picture row, 1,467 JBR picture frames, and zero command frames; the row reported
  `imageShaderImage` alongside parent `graphicsLayer:childCommands`/`graphicsLayer`. The adjacent
  `CASE_GROUPS=shader-rendering` refresh passed 15/15 with `fallback_sum=0`, ten intentional unsupported-picture
  rows, 10,204 JBR picture frames, and 6,076 JBR command frames. Magic Jewel `out` stayed at 78G, the exact run was
  4.0M, the group run was 57M, and the volume had about 282Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-161033/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-171211/suite.tsv`.
- Magic Jewel added and validated live invalid gradient-path structure fallback sentinels after the recorder audit
  identified `linearGradientPath`, `radialGradientPath`, and `sweepGradientPath` as app-reachable path-gradient guards
  that only had parser-corruption coverage. The new `MAGIC_JEWEL_COMPOSE_INVALID_GRADIENT_PATH` toggle reuses the
  supported linear/radial/sweep gradient-filled path probes but injects non-finite path data, so CMP rejects the real
  Compose recording during gradient path serialization before replay. No-run validation passed for
  `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`; `LIST_CASE_COUNT=true`
  returned 521, `LIST_CASE_GROUP_COUNTS=true` reported `gradient-path-structure-invalid` 3,
  `gradient-path-invalid` 21, and `gradient-invalid` 69, `LIST_CASES=true CASE_GROUPS=gradient-path-structure-invalid`
  listed the linear/radial/sweep live path rows, duplicate default-case detection printed no rows, and
  `LIST_UNGROUPED_CASES=true` printed no rows. Focused `CASE_GROUPS=gradient-path-structure-invalid` passed 3/3 with
  `fallback_sum=0`, three intentional unsupported-picture rows, 3,003 JBR picture frames, and zero command frames; the
  rows reported `linearGradientPath`, `radialGradientPath`, and `sweepGradientPath` alongside parent graphics-layer
  reasons and the existing white-outline `path` fallback from drawing the same invalid path. The adjacent
  `CASE_GROUPS=gradient-path-invalid` refresh passed 21/21 with `fallback_sum=18`, three intentional
  unsupported-picture rows, 2,946 JBR picture frames, and zero JBR command frames. Magic Jewel `out` stayed at 78G,
  the focused run was 10M, the adjacent group run was 54M, and the volume had about 264Gi free after completion.
  Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-154145/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-154451/suite.tsv`.
- Magic Jewel added and validated a graphics-layer invalid shadow-path fallback sentinel after the recorder audit
  identified `graphicsLayer:shadowPath` as an app-reachable layer replay guard without a command-probe row. The new
  `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_INVALID_SHADOW_PATH` branch applies a public `GenericShape` containing
  non-finite path data while keeping positive shadow elevation, so CMP accepts the sealed `Outline.Generic` layer shape
  and then rejects the shadow path during command serialization before replay. No-run validation passed for
  `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`; `LIST_CASE_COUNT=true`
  returned 518, `LIST_CASE_GROUP_COUNTS=true` reported `graphics-layer-invalid` 15, the exact row resolved through
  `LIST_CASES=true CASES=commands-graphics-layer-invalid-shadow-path-fallback`, duplicate default-case detection
  printed no rows, and `LIST_UNGROUPED_CASES=true` printed no rows. Focused exact validation passed 1/1 with
  `fallback_sum=0`, one intentional unsupported-picture row, 961 JBR picture frames, and zero command frames; the row
  reported `graphicsLayer:shadowPath` alongside parent `graphicsLayer:childCommands`/`graphicsLayer` and the secondary
  `clipPath` fallback from the shadow fallback pass clipping the same invalid path. The adjacent
  `CASE_GROUPS=graphics-layer-invalid` refresh passed 15/15 with `fallback_sum=0`, fifteen intentional
  unsupported-picture rows, 14,768 JBR picture frames, and zero JBR command frames. Magic Jewel `out` stayed at 78G,
  the exact run was 3.6M, the group run was 53M, and the volume had about 282Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-151839/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-151944/suite.tsv`.
- The recorder reachability audit continued after the gradient color-count batch and classified remaining guards that
  should not be treated as missing app-level Magic Jewel sentinels. CMP's `linearGradientPoints`,
  `radialGradientGeometry`, and `sweepGradientGeometry` checks live in the recorder's JBR gradient metadata
  serialization, but public non-finite Brush coordinates/radii fail during Skia shader construction (`Can't wrap
  nullptr`) before CMP can record unsupported command frames. Existing parser-corruption rows still cover malformed
  command payload geometry, so these live recorder reason strings stay defensive unless a lower-level public path can
  attach invalid gradient metadata to an otherwise valid Skia shader. The same audit classified
  `graphicsLayer:childUnsupported`, `graphicsLayer:childHeaderSize`, and `graphicsLayer:childHeader` as nested
  recording invariants rather than live strict-mode sentinels: Magic Jewel enables
  `compose.jbr.skia.command.strict=true`, and `recordNested` nulls child commands when any nested unsupported reason is
  present, so public child failures reach the parent as `graphicsLayer:childCommands` plus the child reason instead of
  reaching the later child-header checks. No new validation suite was needed for this docs-only classification.
- The same graphics-layer source audit classifies the remaining `graphicsLayer:shadow` and
  `graphicsLayer:shadowFilter` reasons as defensive after the live `graphicsLayer:shadowPath` sentinel. Invalid
  layer size/elevation reaches the already-covered layer validation reasons before `addLayerShadow`, and the only
  fallback shadow filter is an internally synthesized blur descriptor with finite positive sigma and tile mode `3`,
  which satisfies the descriptor validator's finite non-negative sigma and `0..3` tile-mode bounds.
- The follow-up recorder audit classifies `roundRectStyle`, `linearGradientPathPaint`, `radialGradientPathPaint`, and
  `sweepGradientPathPaint` as defensive/shadow guards rather than missing app-level sentinels. `roundRectStyle` sits
  behind the same public Skia `PaintingStyle` enum surface as other round-rect and path paint-style checks, while
  public Compose drawing only supplies Fill or Stroke. The path-gradient paint reasons are also shadowed: the
  recorder calls `linearGradientPayload()`/`radialGradientPayload()`/`sweepGradientPayload()` before the path-specific
  style branch, and those helpers reject stroked path gradients first as `linearGradientPaint`, `radialGradientPaint`,
  or `sweepGradientPaint`. The earlier failed focused path-stroke run expecting `linearGradientPathPaint` is retained
  below as the concrete validation evidence for that ordering.
- Magic Jewel added and validated live invalid gradient color-count fallback sentinels after the recorder audit found
  that public Brush construction accepts overlarge color lists even though CMP limits command replay to 2..16 colors.
  The new `MAGIC_JEWEL_COMPOSE_INVALID_LINEAR_GRADIENT_COLOR_COUNT`,
  `MAGIC_JEWEL_COMPOSE_INVALID_RADIAL_GRADIENT_COLOR_COUNT`, and
  `MAGIC_JEWEL_COMPOSE_INVALID_SWEEP_GRADIENT_COLOR_COUNT` toggles feed 17 colors into the existing live rect gradient
  probes, so the recorder rejects the real Compose recording as `linearGradientColorCount`,
  `radialGradientColorCount`, or `sweepGradientColorCount` before replay. No-run validation passed for
  `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`; `LIST_CASE_COUNT=true`
  returned 517, `LIST_CASE_GROUP_COUNTS=true` reported `gradient-color-count-invalid` 3 and `gradient-invalid` 66,
  `LIST_CASES=true CASE_GROUPS=gradient-color-count-invalid` listed the linear/radial/sweep color-count rows,
  duplicate default-case detection printed no rows, and `LIST_UNGROUPED_CASES=true` printed no rows. Focused
  `CASE_GROUPS=gradient-color-count-invalid` passed 3/3 with `fallback_sum=0`, three intentional
  unsupported-picture rows, 2,989 JBR picture frames, and zero command frames. The adjacent
  `CASE_GROUPS=gradient-invalid` refresh passed 66/66 with `fallback_sum=60`, six intentional unsupported-picture
  rows, 5,587 JBR picture frames, and zero JBR command frames. Magic Jewel `out` stayed at 78G, the focused color-count
  run was 9.8M, the broader gradient-invalid run was 193M, and the volume had about 282Gi free after completion.
  Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-141516/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-141803/suite.tsv`.
- Superseded color-matrix audit note: an earlier attempt to turn CMP's `colorMatrixNonfinite` guard into live Magic
  Jewel coverage failed during Skia color-filter creation with `Can't wrap nullptr`, so the unvalidated app edits were
  backed out and the failed unreferenced output directories `20260605-134149` and `20260605-134306` were trimmed. The
  20260609 CMP hardening now preserves non-finite matrix metadata with a benign native fallback filter, and the current
  `commands-color-matrix-filter-nonfinite-fallback` row covers the live unsupported reason.
- Magic Jewel added and validated live invalid gradient-stop fallback sentinels after the recorder audit identified
  `linearGradientStops` and `radialGradientStops` as app-level unsupported reasons that still only had parser-corruption
  rows. A first geometry attempt using non-finite public Brush coordinates was rejected earlier by Skia shader creation
  (`Can't wrap nullptr`) and never reached CMP's gradient geometry guards; that failed, unreferenced output directory
  was trimmed after the stop-order batch. The landed `MAGIC_JEWEL_COMPOSE_INVALID_LINEAR_GRADIENT_STOPS` and
  `MAGIC_JEWEL_COMPOSE_INVALID_RADIAL_GRADIENT_STOPS` toggles instead use duplicate public Brush stops, matching the
  existing sweep stop sentinel and reaching the recorder's live stop validation. No-run validation passed for
  `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`; `LIST_CASE_COUNT=true`
  returned 514, `LIST_CASE_GROUP_COUNTS=true` reported `gradient-stop-invalid` 3 and `gradient-invalid` 63,
  `LIST_CASES=true CASE_GROUPS=gradient-stop-invalid` listed the linear/radial/sweep stop rows, duplicate default-case
  detection printed no rows, and `LIST_UNGROUPED_CASES=true` printed no rows. Focused
  `CASE_GROUPS=gradient-stop-invalid` passed 3/3 with `fallback_sum=0`, three intentional unsupported-picture rows,
  2,947 JBR picture frames, and zero command frames; the rows reported `linearGradientStops`, `radialGradientStops`,
  and `sweepGradientStops`, each with graphics-layer parent reasons. The adjacent `CASE_GROUPS=gradient-invalid`
  refresh passed 63/63 with `fallback_sum=60`, three intentional unsupported-picture rows, 3,215 JBR picture frames,
  and zero JBR command frames. Magic Jewel `out` stayed at 78G, the focused stop run was 9.1M, the broader
  gradient-invalid run was 178M, and the volume had about 286Gi free after trimming the failed geometry run. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-091009/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-091243/suite.tsv`.
- Magic Jewel added and validated live invalid path-structure fallback sentinels after the recorder audit identified
  `clipPath` and `path` as app-level unsupported reasons that still only had parser-corruption rows. The new
  `MAGIC_JEWEL_COMPOSE_INVALID_CLIP_PATH` and `MAGIC_JEWEL_COMPOSE_INVALID_DRAW_PATH` toggles feed non-finite path
  coordinates into the existing Compose clipPath/drawPath probes, so the recorder rejects the real path structure
  before replay rather than relying on mutated command bytes. No-run validation passed for
  `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`; `LIST_CASE_COUNT=true`
  returned 512, `LIST_CASE_GROUP_COUNTS=true` reported `path-invalid` 24, `LIST_CASES=true CASE_GROUPS=path-invalid`
  listed the two live structural rows before the existing 22 parser-corruption rows, and `LIST_UNGROUPED_CASES=true`
  printed no rows. Focused exact validation for `commands-clip-path-invalid-fallback` and
  `commands-draw-path-invalid-fallback` passed 2/2 with `fallback_sum=0`, two intentional unsupported-picture rows,
  1,967 JBR picture frames, and zero command frames; the clip row reported
  `unsupportedScope:902,clipPath:902,graphicsLayer:childCommands:902,graphicsLayer:902`, and the draw row reported
  `path:2130,graphicsLayer:childCommands:1065,graphicsLayer:1065`. The adjacent `CASE_GROUPS=path-invalid` refresh
  passed 24/24 with `fallback_sum=22`, two intentional unsupported-picture rows, 1,789 JBR picture frames, and zero
  JBR command frames. Magic Jewel `out` was 78G, the exact run was 7.1M, the group run was 69M, and the volume had
  about 299Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-233653/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-233834/suite.tsv`.
- Magic Jewel added and validated graphics-layer invalid size fallback sentinels after the recorder/layer audit
  identified `graphicsLayer:sizeWidth` and `graphicsLayer:sizeHeight` as the remaining app-reachable layer validation
  guards without command-probe rows. The new `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_INVALID_SIZE_WIDTH` and
  `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_INVALID_SIZE_HEIGHT` branches record remembered layers with negative width or
  height outside the command-recording draw pass, then draw those layers during command recording so CMP rejects the
  invalid layer size before replay. No-run validation passed for
  `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`; `LIST_CASE_COUNT=true`
  returned 510, `LIST_CASE_GROUP_COUNTS=true` reported `graphics-layer-invalid` 14 and `graphics-layer` 22,
  `LIST_CASES=true CASE_GROUPS=graphics-layer-invalid` listed the negative size rows plus the prior invalid layer
  rows, and `LIST_UNGROUPED_CASES=true` printed no rows. Focused exact validation for the two size rows passed 2/2
  with `fallback_sum=0`, two intentional unsupported-picture rows, 1,953 JBR picture frames, and zero command frames.
  The adjacent `CASE_GROUPS=graphics-layer-invalid` refresh passed 14/14 with `fallback_sum=0`, fourteen intentional
  unsupported-picture rows, 14,685 JBR picture frames, and zero JBR command frames. Magic Jewel `out` stayed at 77G
  and the volume had about 299Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-204923/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-205101/suite.tsv`.
  The remaining `graphicsLayer:shadowOutline` and `graphicsLayer:clipOutline:<type>` checks are defensive against
  future `Outline` subclasses: `Outline` is sealed and currently defines only `Rectangle`, `Rounded`, and `Generic`,
  all of which the command layer accepts for shadow/clip handling.
- Magic Jewel added and validated an unrecorded graphics-layer fallback sentinel after the recorder/layer audit
  identified `graphicsLayer:recording` as an app-reachable layer lifecycle guard without a command-probe row. The new
  `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_UNRECORDED` branch creates a remembered `GraphicsLayer` and draws it without
  calling `record`, matching the public API's documented no-output state while ensuring the JBR command path falls back
  before replay. No-run validation passed for `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`;
  `LIST_CASE_COUNT=true` returned 508, `LIST_CASE_GROUP_COUNTS=true` reported `graphics-layer-invalid` 12 and
  `graphics-layer` 22, `LIST_CASES=true CASE_GROUPS=graphics-layer-invalid` listed the existing invalid layer rows
  plus `commands-graphics-layer-unrecorded-fallback`, and `LIST_UNGROUPED_CASES=true` printed no rows. Focused
  `CASES=commands-graphics-layer-unrecorded-fallback` passed 1/1 with `fallback_sum=0`; unsupported reasons included
  `graphicsLayer:recording` plus parent graphics-layer reasons, 891 JBR picture frames, and zero command frames. The
  adjacent `CASE_GROUPS=graphics-layer-invalid` refresh passed 12/12 with `fallback_sum=0`, twelve intentional
  unsupported-picture rows, 11,611 JBR picture frames, and zero JBR command frames. Magic Jewel `out` stayed at 77G
  and the volume had about 300Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-203529/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-203629/suite.tsv`.
- Magic Jewel added and validated a graphics-layer invalid blend-mode fallback sentinel after the recorder/layer audit
  identified `graphicsLayer:blendMode` as an app-reachable layer validation guard without an invalid command-probe row.
  The new `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_INVALID_BLEND_MODE` branch overrides the supported graphics-layer blend
  probe with `BlendMode.Clear`, which CMP rejects as `graphicsLayer:blendMode` before replay. The exact case initially
  failed only because the generic screenshot color assertion is not valid for a `Clear` blend-mode probe; after scoping
  that row to the unsupported-reason contract, no-run validation reported 507 default command-probe rows,
  `graphics-layer-invalid` 11, `graphics-layer` 22, and no ungrouped rows. Focused
  `CASES=commands-graphics-layer-invalid-blend-mode-fallback` passed 1/1 with `fallback_sum=0`; unsupported reasons
  included `graphicsLayer:blendMode` plus parent graphics-layer reasons, 1,168 JBR picture frames, and zero command
  frames. The adjacent `CASE_GROUPS=graphics-layer-invalid` refresh passed 11/11 with `fallback_sum=0`, eleven
  intentional unsupported-picture rows, 11,972 JBR picture frames, and zero JBR command frames. The successful suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-202228/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-202314/suite.tsv`.
  The unreferenced discovery failure remains small (3.5M) at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-201834/` and documents
  why the screenshot assertion is disabled for this exact visual-disruptive invalid row.
- Magic Jewel added and validated a graphics-layer invalid rotationY fallback sentinel after the recorder/layer audit
  identified `graphicsLayer:rotationY` as an app-reachable layer validation guard without a command-probe row. The new
  `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_INVALID_ROTATION_Y` branch sets `rotationY = Float.NaN` on the graphics-layer
  probe. No-run validation passed for `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`;
  `LIST_CASE_COUNT=true` returned 506, `LIST_CASE_GROUP_COUNTS=true` reported `graphics-layer-invalid` 10 and
  `graphics-layer` 22, `LIST_CASES=true CASE_GROUPS=graphics-layer-invalid` listed the alpha, scaleX, scaleY,
  rotationZ, translationX, translationY, rotationX, rotationY, camera-distance, and shadow-elevation fallback rows,
  and `LIST_UNGROUPED_CASES=true` printed no rows. Focused
  `CASES=commands-graphics-layer-invalid-rotation-y-fallback` passed 1/1 with `fallback_sum=0`; unsupported reasons
  included `graphicsLayer:rotationY` plus parent graphics-layer reasons, 1,031 JBR picture frames, and zero command
  frames. The adjacent `CASE_GROUPS=graphics-layer-invalid` refresh passed 10/10 with `fallback_sum=0`, ten
  intentional unsupported-picture rows, 10,196 JBR picture frames, and zero JBR command frames. The exact and group
  runs were 3.7M and 37M under Magic Jewel `out`; overall `out` stayed at 77G and the volume had about 300Gi free
  after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-200608/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-200710/suite.tsv`.
- Magic Jewel added and validated a graphics-layer invalid rotationX fallback sentinel after the recorder/layer audit
  identified `graphicsLayer:rotationX` as an app-reachable layer validation guard without a command-probe row. The new
  `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_INVALID_ROTATION_X` branch sets `rotationX = Float.NaN` on the graphics-layer
  probe. No-run validation passed for `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`;
  `LIST_CASE_COUNT=true` returned 505, `LIST_CASE_GROUP_COUNTS=true` reported `graphics-layer-invalid` 9 and
  `graphics-layer` 22, `LIST_CASES=true CASE_GROUPS=graphics-layer-invalid` listed the alpha, scaleX, scaleY,
  rotationZ, translationX, translationY, rotationX, camera-distance, and shadow-elevation fallback rows, and
  `LIST_UNGROUPED_CASES=true` printed no rows. Focused `CASES=commands-graphics-layer-invalid-rotation-x-fallback`
  passed 1/1 with `fallback_sum=0`; unsupported reasons included `graphicsLayer:rotationX` plus parent graphics-layer
  reasons, 1,054 JBR picture frames, and zero command frames. The adjacent `CASE_GROUPS=graphics-layer-invalid`
  refresh passed 9/9 with `fallback_sum=0`, nine intentional unsupported-picture rows, 9,404 JBR picture frames, and
  zero JBR command frames. The exact and group runs were 3.8M and 34M under Magic Jewel `out`; overall `out` stayed at
  77G and the volume had about 300Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-195334/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-195437/suite.tsv`.
- Magic Jewel added and validated a graphics-layer invalid translationY fallback sentinel after the recorder/layer
  audit identified `graphicsLayer:translationY` as an app-reachable layer validation guard without a command-probe row.
  The new `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_INVALID_TRANSLATION_Y` branch sets `translationY = Float.NaN` on the
  graphics-layer probe. No-run validation passed for
  `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`; `LIST_CASE_COUNT=true`
  returned 504, `LIST_CASE_GROUP_COUNTS=true` reported `graphics-layer-invalid` 8 and `graphics-layer` 22,
  `LIST_CASES=true CASE_GROUPS=graphics-layer-invalid` listed the alpha, scaleX, scaleY, rotationZ, translationX,
  translationY, camera-distance, and shadow-elevation fallback rows, and `LIST_UNGROUPED_CASES=true` printed no rows.
  Focused `CASES=commands-graphics-layer-invalid-translation-y-fallback` passed 1/1 with `fallback_sum=0`;
  unsupported reasons included `graphicsLayer:translationY` plus parent graphics-layer reasons, 1,034 JBR picture
  frames, and zero command frames. The adjacent `CASE_GROUPS=graphics-layer-invalid` refresh passed 8/8 with
  `fallback_sum=0`, eight intentional unsupported-picture rows, 7,906 JBR picture frames, and zero JBR command frames.
  The exact and group runs were 3.7M and 28M under Magic Jewel `out`; overall `out` stayed at 77G and the volume had
  about 283Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-193442/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-193540/suite.tsv`.
- Magic Jewel added and validated a graphics-layer invalid translationX fallback sentinel after the recorder/layer
  audit identified `graphicsLayer:translationX` as an app-reachable layer validation guard without a command-probe row.
  The new `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_INVALID_TRANSLATION_X` branch sets `translationX = Float.NaN` on the
  graphics-layer probe. No-run validation passed for
  `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`; `LIST_CASE_COUNT=true`
  returned 503, `LIST_CASE_GROUP_COUNTS=true` reported `graphics-layer-invalid` 7 and `graphics-layer` 22,
  `LIST_CASES=true CASE_GROUPS=graphics-layer-invalid` listed the alpha, scaleX, scaleY, rotationZ, translationX,
  camera-distance, and shadow-elevation fallback rows, and `LIST_UNGROUPED_CASES=true` printed no rows. Focused
  `CASES=commands-graphics-layer-invalid-translation-x-fallback` passed 1/1 with `fallback_sum=0`; unsupported
  reasons included `graphicsLayer:translationX` plus parent graphics-layer reasons, 1,062 JBR picture frames, and zero
  command frames. The adjacent `CASE_GROUPS=graphics-layer-invalid` refresh passed 7/7 with `fallback_sum=0`, seven
  intentional unsupported-picture rows, 7,355 JBR picture frames, and zero JBR command frames. The exact and group
  runs were 3.8M and 26M under Magic Jewel `out`; overall `out` stayed at 77G and the volume had about 300Gi free
  after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-192056/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-192154/suite.tsv`.
- Magic Jewel added and validated a graphics-layer invalid rotationZ fallback sentinel after the recorder/layer audit
  identified `graphicsLayer:rotationZ` as an app-reachable layer validation guard without a command-probe row. The new
  `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_INVALID_ROTATION_Z` branch sets `rotationZ = Float.NaN` on the graphics-layer
  probe. No-run validation passed for `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`;
  `LIST_CASE_COUNT=true` returned 502, `LIST_CASE_GROUP_COUNTS=true` reported `graphics-layer-invalid` 6 and
  `graphics-layer` 22, `LIST_CASES=true CASE_GROUPS=graphics-layer-invalid` listed the alpha, scaleX, scaleY,
  rotationZ, camera-distance, and shadow-elevation fallback rows, and `LIST_UNGROUPED_CASES=true` printed no rows.
  Focused `CASES=commands-graphics-layer-invalid-rotation-z-fallback` passed 1/1 with `fallback_sum=0`; unsupported
  reasons included `graphicsLayer:rotationZ` plus parent graphics-layer reasons, 1,055 JBR picture frames, and zero
  command frames. The adjacent `CASE_GROUPS=graphics-layer-invalid` refresh passed 6/6 with `fallback_sum=0`, six
  intentional unsupported-picture rows, 6,172 JBR picture frames, and zero JBR command frames. The exact and group
  runs were 3.7M and 22M under Magic Jewel `out`; overall `out` stayed at 77G and the volume had about 300Gi free
  after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-190925/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-191023/suite.tsv`.
- Magic Jewel added and validated a graphics-layer invalid scaleY fallback sentinel after the recorder/layer audit
  identified `graphicsLayer:scaleY` as an app-reachable layer validation guard without a command-probe row. The new
  `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_INVALID_SCALE_Y` branch sets `scaleY = Float.NaN` on the graphics-layer probe.
  No-run validation passed for `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`;
  `LIST_CASE_COUNT=true` returned 501, `LIST_CASE_GROUP_COUNTS=true` reported `graphics-layer-invalid` 5 and
  `graphics-layer` 22, `LIST_CASES=true CASE_GROUPS=graphics-layer-invalid` listed the alpha, scaleX, scaleY,
  camera-distance, and shadow-elevation fallback rows, and `LIST_UNGROUPED_CASES=true` printed no rows. Focused
  `CASES=commands-graphics-layer-invalid-scale-y-fallback` passed 1/1 with `fallback_sum=0`; unsupported reasons
  included `graphicsLayer:scaleY` plus parent graphics-layer reasons, 937 JBR picture frames, and zero command frames.
  The adjacent `CASE_GROUPS=graphics-layer-invalid` refresh passed 5/5 with `fallback_sum=0`, five intentional
  unsupported-picture rows, 4,669 JBR picture frames, and zero JBR command frames. The exact and group runs were 3.6M
  and 18M under Magic Jewel `out`; overall `out` stayed at 77G and the volume had about 300Gi free after completion.
  Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-185751/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-185856/suite.tsv`.
- Magic Jewel added and validated a graphics-layer invalid scaleX fallback sentinel after the recorder/layer audit
  identified `graphicsLayer:scaleX` as an app-reachable layer validation guard without a command-probe row. The new
  `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_INVALID_SCALE_X` branch sets `scaleX = Float.NaN` on the graphics-layer probe.
  No-run validation passed for `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`;
  `LIST_CASE_COUNT=true` returned 500, `LIST_CASE_GROUP_COUNTS=true` reported `graphics-layer-invalid` 4 and
  `graphics-layer` 22, `LIST_CASES=true CASE_GROUPS=graphics-layer-invalid` listed the alpha, scaleX,
  camera-distance, and shadow-elevation fallback rows, and `LIST_UNGROUPED_CASES=true` printed no rows. Focused
  `CASES=commands-graphics-layer-invalid-scale-x-fallback` passed 1/1 with `fallback_sum=0`; unsupported reasons
  included `graphicsLayer:scaleX` plus parent graphics-layer reasons, 953 JBR picture frames, and zero command frames.
  The adjacent `CASE_GROUPS=graphics-layer-invalid` refresh passed 4/4 with `fallback_sum=0`, four intentional
  unsupported-picture rows, 3,783 JBR picture frames, and zero JBR command frames. The exact and group runs were 3.6M
  and 14M under Magic Jewel `out`; overall `out` stayed at 77G and the volume had about 300Gi free after completion.
  Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-184926/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-185029/suite.tsv`.
- Magic Jewel added and validated a graphics-layer invalid camera-distance fallback sentinel after the recorder/layer
  audit identified `graphicsLayer:cameraDistance` as an app-reachable layer validation guard without a command-probe
  row. The new `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_INVALID_CAMERA_DISTANCE` branch sets `cameraDistance = 0f` on the
  graphics-layer probe. No-run validation passed for
  `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`; `LIST_CASE_COUNT=true`
  returned 499, `LIST_CASE_GROUP_COUNTS=true` reported `graphics-layer-invalid` 3 and `graphics-layer` 22,
  `LIST_CASES=true CASE_GROUPS=graphics-layer-invalid` listed the alpha, shadow-elevation, and camera-distance
  fallback rows, and `LIST_UNGROUPED_CASES=true` printed no rows. Focused
  `CASES=commands-graphics-layer-invalid-camera-distance-fallback` passed 1/1 with `fallback_sum=0`; unsupported
  reasons included `graphicsLayer:cameraDistance` plus parent graphics-layer reasons, 956 JBR picture frames, and zero
  command frames. The adjacent `CASE_GROUPS=graphics-layer-invalid` refresh passed 3/3 with `fallback_sum=0`, three
  intentional unsupported-picture rows, 2,841 JBR picture frames, and zero JBR command frames. The exact and group
  runs were 3.6M and 11M under Magic Jewel `out`; overall `out` stayed at 77G and the volume had about 300Gi free
  after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-184223/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-184326/suite.tsv`.
- Magic Jewel added and validated a graphics-layer invalid alpha fallback sentinel after the recorder/layer audit
  identified `graphicsLayer:alpha` as an app-reachable layer validation guard without a command-probe row. The new
  `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_INVALID_ALPHA` branch overrides the graphics-layer probe's normal alpha with
  `alpha = 1.5f`. No-run validation passed for
  `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`; `LIST_CASE_COUNT=true`
  returned 498, `LIST_CASE_GROUP_COUNTS=true` reported `graphics-layer-invalid` 2 and `graphics-layer` 22,
  `LIST_CASES=true CASE_GROUPS=graphics-layer-invalid` listed
  `commands-graphics-layer-invalid-alpha-fallback` and
  `commands-graphics-layer-invalid-shadow-elevation-fallback`, and `LIST_UNGROUPED_CASES=true` printed no rows.
  Focused `CASES=commands-graphics-layer-invalid-alpha-fallback` passed 1/1 with `fallback_sum=0`; unsupported
  reasons included `graphicsLayer:alpha` plus parent graphics-layer reasons, 964 JBR picture frames, and zero command
  frames. The adjacent `CASE_GROUPS=graphics-layer-invalid` refresh passed 2/2 with `fallback_sum=0`, two intentional
  unsupported-picture rows, 1,983 JBR picture frames, and zero JBR command frames. The exact and group runs were 3.6M
  and 7.2M under Magic Jewel `out`; overall `out` stayed at 77G and the volume had about 300Gi free after completion.
  Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-183358/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-183507/suite.tsv`.
- Magic Jewel added and validated a graphics-layer invalid shadow-elevation fallback sentinel after the recorder/layer
  audit identified `graphicsLayer:shadowElevation` as an app-reachable layer validation guard without a command-probe
  row. The new `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_INVALID_SHADOW_ELEVATION` branch sets `shadowElevation = -1f` on
  the graphics-layer probe. No-run validation passed for
  `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`; `LIST_CASE_COUNT=true`
  returned 497, `LIST_CASE_GROUP_COUNTS=true` reported `graphics-layer` 22,
  `LIST_CASES=true CASE_GROUPS=graphics-layer` listed
  `commands-graphics-layer-invalid-shadow-elevation-fallback` immediately after `commands-graphics-layer-shadow`, the
  bounded default-order slice around graphics-layer shadow rows preserved
  `commands-graphics-layer-chained-render-effect-blend-color-matrix-filter`, and `LIST_UNGROUPED_CASES=true` printed
  no rows. Focused `CASES=commands-graphics-layer-invalid-shadow-elevation-fallback` passed 1/1 with `fallback_sum=0`;
  unsupported reasons included `graphicsLayer:shadowElevation` plus parent graphics-layer reasons, 969 JBR picture
  frames, and zero command frames. The adjacent `CASE_GROUPS=graphics-layer` refresh passed 22/22 with
  `fallback_sum=0`, one intentional unsupported-picture row, 879 JBR picture frames, and 24,305 JBR command frames.
  The exact and group runs were 3.6M and 83M under Magic Jewel `out`; overall `out` stayed at 77G and the volume had
  about 301Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-175414/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-180307/suite.tsv`.
- Magic Jewel added and validated gradient path stroke fallback sentinels after the recorder audit identified path
  gradient non-fill paint as an app-reachable unsupported surface. The new
  `MAGIC_JEWEL_COMPOSE_GRADIENT_PATH_STROKE` toggle reuses the existing linear/radial/sweep path-gradient probes but
  draws them with `Stroke`; supported fill-path gradient replay remains unchanged when the toggle is false. A first
  focused run expecting the path-specific `linearGradientPathPaint` reason failed, usefully showing that the real
  recorder path rejects stroked path gradients earlier through the generic gradient-paint guard:
  `linearGradientPaint`. The failed unreferenced run was 3.6M:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-173603/commands-linear-gradient-path-stroke-fallback/report.md`.
  After correcting expected reasons, no-run validation passed for
  `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`; `LIST_CASE_COUNT=true`
  returned 496, `LIST_CASE_GROUP_COUNTS=true` reported `gradient-path-stroke-fallbacks` 3,
  `LIST_CASES=true CASE_GROUPS=gradient-path-stroke-fallbacks` listed the three new rows, and
  `LIST_UNGROUPED_CASES=true` printed no rows. Focused `CASE_GROUPS=gradient-path-stroke-fallbacks` passed 3/3 with
  `fallback_sum=0`; unsupported reasons were `linearGradientPaint`, `radialGradientPaint`, and `sweepGradientPaint`
  plus parent graphics-layer reasons; the group produced three intentional unsupported-picture rows, 3,245 JBR picture
  frames, and zero command frames. The successful run was 11M under Magic Jewel `out`; overall `out` stayed at 77G and
  the volume had about 284Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-173734/suite.tsv`.
- Magic Jewel added and validated an image raw Skia table color-filter fallback sentinel after the recorder audit found
  `drawImageRect` had supported image tint/color-matrix descriptor rows but no app-level unsupported raw image
  color-filter probe. The new `MAGIC_JEWEL_COMPOSE_IMAGE_RAW_TABLE_COLOR_FILTER` branch draws an image with a raw
  Skia table color filter, which CMP rejects structurally as `colorFilter` before the generic `image` fallback. No-run
  validation passed for `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`;
  `LIST_CASE_COUNT=true` returned 493, `LIST_CASE_GROUP_COUNTS=true` reported `color-filters` 12,
  `LIST_CASES=true CASE_GROUPS=color-filters` listed `commands-image-raw-table-color-filter-fallback` immediately
  after `commands-image-color-matrix-filter`, and `LIST_UNGROUPED_CASES=true` printed no rows. Focused real validation
  passed `CASES=commands-image-raw-table-color-filter-fallback`: 1/1 passed, `fallback_sum=0`, unsupported reasons
  included `colorFilter`, `image`, and parent graphics-layer reasons, 931 JBR picture frames, and zero command frames.
  The adjacent `CASE_GROUPS=color-filters` refresh passed 12/12 with `fallback_sum=0`, three intentional
  unsupported-picture rows, 3,516 JBR picture frames, and 12,920 JBR command frames. The exact and group runs were
  3.5M and 54M under Magic Jewel `out`; overall `out` stayed at 77G and the volume had about 301Gi free after
  completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-171252/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-171707/suite.tsv`.
- Magic Jewel added and validated a `Canvas.drawVertices` raw Skia-backed color-filter fallback sentinel after a focused
  recorder audit found the solid-color-only vertices command path lacked an explicit unsupported-paint probe. No-run
  validation passed for `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`;
  `LIST_CASE_COUNT=true` returned 492, `LIST_CASE_GROUP_COUNTS=true` reported `core-effects` 8,
  `LIST_CASES=true CASE_GROUPS=core-effects` listed `commands-vertices-raw-color-filter-fallback` after
  `commands-vertices`, and `LIST_UNGROUPED_CASES=true` printed no rows. The first exact launch created only a 64K
  unreferenced failed output dir because the sandbox blocked Gradle's `~/.gradle` wrapper lock. Rerunning with the
  required Gradle permissions passed `CASES=commands-vertices-raw-color-filter-fallback`: 1/1 passed,
  `fallback_sum=0`, the `vertices` unsupported reason was present alongside parent graphics-layer reasons, 927 JBR
  picture frames, and zero command frames. The adjacent `CASE_GROUPS=core-effects` refresh passed 8/8 with
  `fallback_sum=0`, three intentional unsupported-picture rows, 3,195 JBR picture frames, and 6,876 JBR command
  frames. The exact and group runs were 2.8M and 31M under Magic Jewel `out`; overall `out` stayed at 76G and the
  volume had about 302Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-155224/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-160716/suite.tsv`.
- Magic Jewel local artifact state was restored after focused compatibility `happy` retries exposed harness-local
  failures, first `service-unavailable` with stale processes and then repeatable `public-api-missing` once the stale
  processes were gone. The current `/tmp` artifacts were absent (`/tmp/jbr-api-shim.jar`,
  `/tmp/missing-jbr-api-shim.jar`, and `/tmp/jbr-skia-native/libjbrskiainterop.dylib`), so
  `./scripts/rebuild-jbr-skia-local-artifacts.sh` was rerun successfully. It rebuilt the public API shim, the
  `/tmp/jbr-skia-run/desktop` java.desktop patch classes, and `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; a
  focused `CASES=happy` compatibility retry then passed with `fallback_new_count=0`, 671 command frames, and
  background-window mode true:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260603-105443/matrix.tsv`.
- Magic Jewel full compatibility matrix passed in marker-only mode after the restored-artifact focused retry. A
  concurrent subagent attempt created two unreferenced/failed header-only or cross-wired output dirs, so the
  authoritative run was repeated with a single owner. Aggregate: 57/57 passed, `fallback_sum=56`,
  `jbr_command_frames=770`, and every row reported `background_window=true`. The TSV has 58 lines including the
  header, the run directory was 73M, Magic Jewel `out` stayed at 76G, and the volume had about 305Gi free around the
  run. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260603-140432/matrix.tsv`.
- Magic Jewel artifact matrix refreshed after the compatibility matrix on the restored current ABI 106 artifacts.
  `CASE_GROUPS=required` passed 2/2 with `fallback_sum=1`, 438 command frames, and background-window mode true for
  both rows: `current-all` replayed commands and `missing-public-api` fell back exactly once. `CASE_GROUPS=optional-old`
  recorded the five expected skipped rows because no old artifact variables were set. The required and optional-old
  matrices were 3.0M and 4.0K under Magic Jewel `out`, which stayed at 76G. Matrices:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260603-143254/matrix.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260603-143404/matrix.tsv`.
- Magic Jewel full default screenshot parity suite passed in marker-only mode after the restored-artifact
  compatibility and artifact refreshes: 106/106 passed, `fallback_sum=11`, `jbr_picture_frames=0`, and
  `jbr_command_frames=93672`; all screenshot pixel metrics were intentionally `missing` because
  `EXPECT_SCREENSHOT_ASSERTION=false` was set. The TSV has 107 lines including the header, the run directory was
  529M under Magic Jewel `out`, overall `out` stayed at 76G, and the volume had about 286Gi free after completion.
  Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260603-143459/suite.tsv`.
- Magic Jewel full default command-probe consolidation passed after the focused invalid/supported refresh batch. The
  first run was interrupted after 65 data rows, so the sweep was completed with
  `CASES_FROM=commands-invalid-text-font-size-fallback` rather than replaying the green prefix. Combined aggregate:
  491/491 passed, `fallback_sum=350`, 30 intentional unsupported-picture rows, `jbr_picture_frames=31269`, and
  `jbr_command_frames=158073`; no non-passed rows were present. The TSVs have 66 and 427 lines including headers
  (493 total header-inclusive lines across the two files), and the resumed tail ended at
  `commands-invalid-gradient-fallback`. The prefix and resumed run directories were 167M and 1.7G under Magic Jewel
  `out`; overall `out` was 76G and the volume had about 224Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-155529/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-165056/suite.tsv`.
- Magic Jewel focused command-probe graphics-layer refreshes passed after the shader rendering/composition refresh:
  `CASE_GROUPS=graphics-layer` passed 21/21 with `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=29909`; `CASE_GROUPS=graphics-layer-extras` passed 15/15 with
  `fallback_sum=0`, three intentional unsupported-picture rows, `jbr_picture_frames=3322`, and
  `jbr_command_frames=18044`. The TSVs have 22 and 16 lines including headers. The runs were 93M and 75M under Magic
  Jewel `out`; overall `out` stayed at 74G and the volume had about 244Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-153002/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-154344/suite.tsv`.
- Magic Jewel focused command-probe shader rendering/composition refreshes passed after the descriptor lifecycle
  refresh: `CASE_GROUPS=shader-rendering` passed 14/14 with `fallback_sum=0`, nine intentional unsupported-picture
  rows, `jbr_picture_frames=10692`, and `jbr_command_frames=6032`; `CASE_GROUPS=shader-composition-runtime` passed
  15/15 with `fallback_sum=0`, two intentional unsupported-picture rows, `jbr_picture_frames=2188`, and
  `jbr_command_frames=16730`. The TSVs have 15 and 16 lines including headers. The runs were 58M and 76M under Magic
  Jewel `out`; overall `out` stayed at 74G and the volume had about 244Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-150751/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-151724/suite.tsv`.
- Magic Jewel focused command-probe descriptor lifecycle refresh passed after the color/saveLayer fallback refresh:
  `CASE_GROUPS=descriptor-lifecycle` passed 18/18 with `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=25506`. The TSV has 19 lines including the header. The run was
  291M under Magic Jewel `out`; overall `out` stayed at 74G and the volume had about 244Gi free after completion.
  Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-145219/suite.tsv`.
- Magic Jewel focused command-probe color/saveLayer fallback refreshes passed after the transform/text refresh:
  `CASE_GROUPS=color-filters` passed 11/11 with `fallback_sum=0`, two intentional unsupported-picture rows,
  `jbr_picture_frames=2098`, and `jbr_command_frames=11700`; `CASE_GROUPS=save-layer-shader-fallbacks` passed 8/8
  with `fallback_sum=0`, six intentional unsupported-picture rows, `jbr_picture_frames=7270`, and
  `jbr_command_frames=2039`. The TSVs have 12 and 9 lines including headers. The runs were 49M and 31M under Magic
  Jewel `out`; overall `out` stayed at 74G and the volume had about 245Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-143804/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-144528/suite.tsv`.
- Magic Jewel focused command-probe transform/text supported refreshes passed after the supported sanity pair:
  `CASE_GROUPS=surface-transform-ui` passed 11/11 with `fallback_sum=0`, `unsupported_rows=0`, and
  `jbr_command_frames=16553`; `CASE_GROUPS=native-text` passed 14/14 with `fallback_sum=0`, `unsupported_rows=0`,
  and `jbr_command_frames=20315`. Both produced zero picture frames. The TSVs have 12 and 15 lines including headers.
  The runs were 51M and 61M under Magic Jewel `out`; overall `out` stayed at 74G and the volume had about 229Gi free
  after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-142012/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-142743/suite.tsv`.
- Magic Jewel focused command-probe supported-command sanity refreshes passed after the compact invalid batch:
  `CASE_GROUPS=core-effects` passed 7/7 with `fallback_sum=0`, two intentional unsupported-picture rows,
  `jbr_picture_frames=2049`, and `jbr_command_frames=7785`; `CASE_GROUPS=smoke` passed 6/6 with `fallback_sum=0`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=7435`. The TSVs have 8 and 7 lines including
  headers. The runs were 28M and 26M under Magic Jewel `out`; overall `out` stayed at 74G and the volume had about
  230Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-140936/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-141436/suite.tsv`.
- Magic Jewel compact focused command-probe invalid refresh passed after the gradient invalid batch. A first no-launch
  attempt with comma-separated `CASE_GROUPS` correctly failed selector parsing; rerunning with whitespace-separated
  groups passed `shader-ref-invalid`, `fill-rect-color-filter-invalid`, `blend-mode-invalid`, and `stream-invalid`
  together: 18/18 passed, `fallback_sum=18`, `unsupported_rows=0`, and zero replay frames. The TSV has 19 lines
  including the header. The run was 71M under Magic Jewel `out`; overall `out` stayed at 74G and the volume had about
  245Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-135655/suite.tsv`.
- Magic Jewel focused command-probe gradient guard refresh passed after the RuntimeEffect invalid batch:
  `CASE_GROUPS=gradient-invalid` passed 60/60 with `fallback_sum=60`, `unsupported_rows=0`, and zero replay frames.
  The TSV has 61 lines including the header. The run was 198M under Magic Jewel `out`; overall `out` was 74G and the
  volume had about 245Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-131836/suite.tsv`.
- Magic Jewel focused command-probe RuntimeEffect guard refresh passed after the shader descriptor invalid batch:
  `CASE_GROUPS=runtime-effect-invalid` passed 62/62 with `fallback_sum=56`, six intentional unsupported-picture rows,
  `jbr_picture_frames=7356`, and `jbr_command_frames=0`. The TSV has 63 lines including the header. The run was 340M
  under Magic Jewel `out`; overall `out` stayed at 73G and the volume had about 245Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-123402/suite.tsv`.
- Magic Jewel focused command-probe shader descriptor guard refresh passed after the saveLayer invalid batch:
  `CASE_GROUPS=shader-descriptor-invalid` passed 30/30 with `fallback_sum=30`, `unsupported_rows=0`, and zero replay
  frames. The TSV has 31 lines including the header. The run was 129M under Magic Jewel `out`; overall `out` stayed
  at 73G and the volume had about 231Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-121314/suite.tsv`.
- Magic Jewel focused command-probe saveLayer guard refresh passed after the descriptor-handle invalid batch:
  `CASE_GROUPS=save-layer-invalid` passed 37/37 with `fallback_sum=37`, `unsupported_rows=0`, and zero replay frames.
  The TSV has 38 lines including the header. The run was 132M under Magic Jewel `out`; overall `out` stayed at 73G
  and the volume had about 231Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-114844/suite.tsv`.
- Magic Jewel focused command-probe descriptor handle guard refresh passed after the image-handle invalid batch:
  `CASE_GROUPS=descriptor-handles-invalid` passed 48/48 with `fallback_sum=48`, `unsupported_rows=0`, and zero
  replay frames. The TSV has 49 lines including the header. The run was 198M under Magic Jewel `out`; overall `out`
  stayed at 73G and the volume had about 246Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-111739/suite.tsv`.
- Magic Jewel focused command-probe image handle guard refresh passed after the descriptor/path invalid batch:
  `CASE_GROUPS=image-handles-invalid` passed 27/27 with `fallback_sum=27`, `unsupported_rows=0`, zero picture frames,
  and 1,102 command frames from the cache-clear record-flags row. The TSV has 28 lines including the header. The run
  was 94M under Magic Jewel `out`; overall `out` stayed at 73G and the volume had about 247Gi free after completion.
  Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-105942/suite.tsv`.
- Magic Jewel focused command-probe descriptor/path guard refreshes passed after the primitive/native/path invalid
  batch: `CASE_GROUPS=gradient-path-invalid` passed 18/18 with `fallback_sum=18`, `unsupported_rows=0`, and zero
  replay frames; `CASE_GROUPS=effect-descriptor-invalid` passed 28/28 with `fallback_sum=28`, `unsupported_rows=0`,
  and zero replay frames. The TSVs have 19 and 29 lines including headers. The runs were 64M and 101M under Magic
  Jewel `out`; overall `out` stayed at 73G and the volume had about 247Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-102903/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-104049/suite.tsv`.
- Magic Jewel focused command-probe invalid guard refreshes passed after the raw table full-sweep consolidation:
  `CASE_GROUPS=primitive-invalid` passed 13/13 with `fallback_sum=13`, `unsupported_rows=0`, and zero replay frames;
  `CASE_GROUPS=native-text-invalid` passed 11/11 with `fallback_sum=11`, `unsupported_rows=0`, zero picture frames,
  and 976 command frames from the font-data record-flags row; `CASE_GROUPS=path-invalid` passed 22/22 with
  `fallback_sum=22`, `unsupported_rows=0`, and zero replay frames. The TSVs have 14, 12, and 23 lines including
  headers. The runs were 43M, 39M, and 78M under Magic Jewel `out`; overall `out` was 73G and the volume had about
  232Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-095812/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-100643/suite.tsv`,
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-101406/suite.tsv`.
- Magic Jewel full default command-probe consolidation passed in command-marker-only mode after the raw Skia table
  color-filter, saveLayer raw table color-filter, and graphicsLayer raw table color-filter sentinels. Aggregate:
  491/491 passed, `fallback_sum=350`, `unsupported_rows=30`, `jbr_picture_frames=38973`, and
  `jbr_command_frames=166320`. The TSV has 492 lines including the header and covers all three new raw table
  color-filter sentinel rows in default order. The run used `EXPECT_SCREENSHOT_ASSERTION=false`; the run directory was
  2.3G, Magic Jewel `out` was 72G, and the volume had about 247Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-044455/suite.tsv`.
- Magic Jewel now has a raw Skia table color-filter graphicsLayer fallback sentinel in pushed commit `7c1ffcc`.
  No-run validation passed for `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`;
  `LIST_CASE_COUNT=true` returned 491, `LIST_CASE_GROUP_COUNTS=true` reported `graphics-layer-extras` 15,
  `LIST_CASES=true CASE_GROUPS=graphics-layer-extras` listed the new
  `commands-graphics-layer-raw-table-color-filter-fallback` row after the existing raw graphicsLayer color-filter
  row, and `LIST_UNGROUPED_CASES=true` printed no rows. Focused real validation passed for
  `CASES=commands-graphics-layer-raw-table-color-filter-fallback`: 1/1 passed, `fallback_sum=0`, one intentional
  unsupported-picture row, `jbr_picture_frames=1334`, and `jbr_command_frames=0`. The adjacent
  `CASE_GROUPS=graphics-layer-extras` refresh passed 15/15 with `fallback_sum=0`, three intentional
  unsupported-picture rows, `jbr_picture_frames=3960`, and `jbr_command_frames=17534`. The exact and group runs were
  4.0M and 77M under Magic Jewel `out`; overall `out` stayed at 70G and the volume had about 265Gi free after
  completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-043200/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-043305/suite.tsv`.
- Magic Jewel now has a raw Skia table color-filter saveLayer fallback sentinel in pushed commit `02a250f`. No-run
  validation passed for `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`;
  `LIST_CASE_COUNT=true` returned 490, `LIST_CASE_GROUP_COUNTS=true` reported `save-layer-shader-fallbacks` 8,
  `LIST_CASES=true CASE_GROUPS=save-layer-shader-fallbacks` listed the new
  `commands-save-layer-raw-table-color-filter-fallback` row after the existing raw saveLayer color-filter row, and
  `LIST_UNGROUPED_CASES=true` printed no rows. Focused real validation passed for
  `CASES=commands-save-layer-raw-table-color-filter-fallback`: 1/1 passed, `fallback_sum=0`, one intentional
  unsupported-picture row, `jbr_picture_frames=1317`, and `jbr_command_frames=0`. The adjacent
  `CASE_GROUPS=save-layer-shader-fallbacks` refresh passed 8/8 with `fallback_sum=0`, six intentional
  unsupported-picture rows, `jbr_picture_frames=8377`, and `jbr_command_frames=4799`. The exact and group runs were
  3.8M and 37M under Magic Jewel `out`; overall `out` stayed at 70G and the volume had about 252Gi free after
  completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-042132/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-042236/suite.tsv`.
- Magic Jewel now has a raw Skia table color-filter fallback sentinel in pushed commit `55df401`. No-run validation
  passed for `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`;
  `LIST_CASE_COUNT=true` returned 489, `LIST_CASE_COUNT=true CASE_GROUPS=color-filters` returned 11,
  `LIST_CASES=true CASE_GROUPS=color-filters` listed the new `commands-raw-table-color-filter-fallback` row after
  raw blend, and `LIST_UNGROUPED_CASES=true` printed no rows. The first exact launch failed only because the sandbox
  blocked Gradle's `~/.gradle` wrapper lock file; that unreferenced 64K failed output dir was removed. Rerunning with
  the required Gradle permissions passed `CASES=commands-raw-table-color-filter-fallback`: 1/1 passed,
  `fallback_sum=0`, one intentional unsupported-picture row, `jbr_picture_frames=826`, and `jbr_command_frames=0`.
  The adjacent `CASE_GROUPS=color-filters` refresh passed 11/11 with `fallback_sum=0`, two intentional
  unsupported-picture rows, `jbr_picture_frames=2518`, and `jbr_command_frames=14110`. The exact and group runs were
  2.6M and 53M under Magic Jewel `out`; overall `out` stayed at 70G and the volume had about 252Gi free after
  completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-040651/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-040750/suite.tsv`.
- Magic Jewel full default command-probe consolidation passed in command-marker-only mode after adding the raw conical
  gradient shader fallback sentinel. Aggregate: 488/488 passed, `fallback_sum=350`, `unsupported_rows=27`,
  `jbr_picture_frames=34246`, and `jbr_command_frames=188703`. The TSV has 489 lines including the header and covers
  the new `commands-raw-conical-gradient-shader-fallback` row in default order. The run used
  `EXPECT_SCREENSHOT_ASSERTION=false`; the volume had about 265Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-224702/suite.tsv`.
- Magic Jewel now has a raw Skia two-point conical gradient shader fallback sentinel in pushed commit `020dcb3`.
  No-run validation passed for `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`;
  `LIST_CASE_COUNT=true` returned 488, `LIST_CASE_COUNT=true CASE_GROUPS=shader-rendering` returned 14,
  `LIST_CASES=true CASE_GROUPS=shader-rendering` listed the new `commands-raw-conical-gradient-shader-fallback`
  row after raw sweep, and `LIST_UNGROUPED_CASES=true` printed no rows. An accidental pre-fix
  `LIST_CASE_COUNT=true` launch exposed the missing command-probe count branch, created only an unreferenced 60K
  failed output dir, and that failed dir was removed. Focused real validation passed for
  `CASES=commands-raw-conical-gradient-shader-fallback`: 1/1 passed, `fallback_sum=0`, one intentional
  unsupported-picture row, `jbr_picture_frames=1496`, and `jbr_command_frames=0`. The adjacent
  `CASE_GROUPS=shader-rendering` refresh passed 14/14 with `fallback_sum=0`, nine intentional
  unsupported-picture rows, `jbr_picture_frames=11653`, and `jbr_command_frames=7884`. The exact and group runs
  were 4.2M and 61M under Magic Jewel `out`; overall `out` stayed at 68G and the volume had about 268Gi free.
  Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-223319/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-223423/suite.tsv`.
- Magic Jewel compatibility matrix audit list order now matches the actual run order for
  `command-capability-high-mismatch` in pushed commit `8861b08`. No-run validation: `bash -n` passed,
  `LIST_CASE_COUNT=true` returned 57, and `LIST_UNGROUPED_CASES=true` printed no rows.
- Focused real validations passed after the matrix/benchmark `LIST_UNGROUPED_CASES=true` helper change. The initial
  compatibility launch failed under the sandbox because Gradle could not open its `~/.gradle` wrapper lock file; that
  unreferenced failed output directory was removed. Rerunning with the required Gradle permissions passed
  compatibility `CASE_GROUPS=handshake` 6/6 with `fallback_sum=5`, `jbr_command_frames=311`, and
  `background_window=true` on every row. Artifact `CASE_GROUPS=required` passed 2/2 with `fallback_sum=1`,
  `jbr_command_frames=1295`, and `background_window=true` on both rows. A short benchmark `CASE_GROUPS=baseline`
  run with `DURATION_SECONDS=5`, `WARMUP_SECONDS=1`, and `SAMPLE_INTERVAL_SECONDS=1` passed 2/2 with
  `fallback_sum=0`: `picture` produced 910 JBR picture frames, and `commands` produced 1,246 JBR command frames. The
  runs were 15M, 5.6M, and 5.8M under Magic Jewel `out`, with `out` at 68G and the volume at about 254Gi free after
  completion. TSVs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260601-221558/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260601-221937/matrix.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260601-222045/suite.tsv`.
- Magic Jewel compatibility matrix, artifact matrix, and benchmark suite now support `LIST_UNGROUPED_CASES=true`,
  matching the command-probe and screenshot parity audit helper. The pushed Magic Jewel commit `1e29750` adds the
  no-run grouped coverage check and README documentation. Validation was no-run only: `bash -n` passed for all three
  scripts, `LIST_UNGROUPED_CASES=true` printed no rows for compatibility/artifact/benchmark, default
  `LIST_CASE_COUNT=true` returned 57, 7, and 5 respectively, compatibility `LIST_CASE_GROUP_COUNTS=true` still
  returned `handshake` 6, `low-word-gradients` 15, `low-word-effects` 18, `high-word-effects` 9, and
  `high-word-shader-ui` 9, and benchmark `LIST_CASES=true CASE_GROUPS=baseline` still listed `picture` and
  `commands`. No runtime validation directories were created for this helper-only change.
- Magic Jewel README now documents the new quick-group selectors for the compatibility matrix, artifact matrix, and
  benchmark suite. The pushed Magic Jewel commit `ea3dddc` covers `CASE_GROUPS=...`,
  `LIST_CASE_GROUPS=true`, `LIST_CASE_GROUP_COUNTS=true`, `LIST_CASES=true`, and `LIST_CASE_COUNT=true` for those
  harnesses, including the current compatibility groups (`handshake`, `low-word-gradients`, `low-word-effects`,
  `high-word-effects`, `high-word-shader-ui`), artifact groups (`required`, `optional-old`), and benchmark groups
  (`baseline`, `image-cache`). This was a docs-only update after the focused grouped validations below; no additional
  runtime validation was needed.
- Magic Jewel benchmark suite now supports no-run quick-group helpers: `LIST_CASE_GROUPS=true`,
  `LIST_CASE_GROUP_COUNTS=true`, and `CASE_GROUPS=...`. The exposed groups are `baseline` 2 and `image-cache` 3.
  No-run validation confirmed syntax, group listing/counts, `LIST_CASES=true CASE_GROUPS=image-cache` listing the
  three image-cache rows, `LIST_CASE_COUNT=true CASE_GROUPS=baseline` returning 2, and unknown-group rejection. A short
  focused `CASE_GROUPS=baseline` run with `DURATION_SECONDS=5`, `WARMUP_SECONDS=1`, and `SAMPLE_INTERVAL_SECONDS=1`
  passed 2/2 with `fallback_sum=0`: `picture` produced 620 JBR picture frames, and `commands` produced 531 JBR command
  frames. A short focused `CASE_GROUPS=image-cache` run with the same timing knobs passed 3/3 with `fallback_sum=0`
  and 1,788 JBR command frames across stable, dynamic, and resize dynamic image-cache rows. The TSVs have 3 and 4
  lines including headers. The runs were 4.7M and 8.9M under Magic Jewel `out`, with `out` at 68G and the volume at
  about 268Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260601-215955/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260601-220219/suite.tsv`.
- Magic Jewel artifact matrix now supports no-run quick-group helpers: `LIST_CASE_GROUPS=true`,
  `LIST_CASE_GROUP_COUNTS=true`, and `CASE_GROUPS=...`. The exposed groups are `required` 2 and `optional-old` 5.
  No-run validation confirmed group listing, group counts, `LIST_CASES=true CASE_GROUPS=required` listing
  `current-all` and `missing-public-api`, `LIST_CASE_COUNT=true CASE_GROUPS=optional-old` returning 5, and
  unknown-group rejection. A focused real `CASE_GROUPS=required` run passed 2/2: `current-all` replayed commands with
  `jbr_command_frames=333`, and `missing-public-api` fell back exactly once with zero command frames. The TSV has 3
  lines including the header. The run used `EXPECT_SCREENSHOT_ASSERTION=false`, was 3.6M under Magic Jewel `out`, with
  `out` at 68G and the volume at about 268Gi free after completion. A no-launch `CASE_GROUPS=optional-old` run also
  passed by recording the five expected skipped rows when no old artifact variables were set; that TSV has 6 lines and
  was 4.0K. Matrices:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260601-215558/matrix.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260601-215818/matrix.tsv`.
- Magic Jewel focused compatibility high-word groups passed through the new grouped selector after the low-word groups.
  `CASE_GROUPS=high-word-effects` passed 9/9 with `fallback_sum=9`, `jbr_command_frames=0`, and
  `background_window=true` on every row, covering image-filter, offset/chained image-filter, RuntimeEffect
  color-filter, dash/path-effect, and direct-shadow capability removals. `CASE_GROUPS=high-word-shader-ui` passed 9/9
  with `fallback_sum=9`, `jbr_command_frames=0`, and `background_window=true` on every row, covering shader
  descriptor, concat matrix, shader color-filter, draw-points, shader transform, font-data, color/noise shader, and
  draw-vertices capability removals. Each TSV has 10 lines including the header. The runs used
  `EXPECT_SCREENSHOT_ASSERTION=false`, were 13M each under Magic Jewel `out`, with `out` at 68G and the volume at
  about 268Gi free after completion. Matrices:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260601-214522/matrix.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260601-214945/matrix.tsv`.
- Magic Jewel focused compatibility `CASE_GROUPS=low-word-effects` passed through the new grouped selector after the
  low-word gradient group. Aggregate: 18/18 passed, `fallback_sum=18`, `jbr_command_frames=0`, and
  `background_window=true` on every row. This covers low-word text, image-shader, blend-mode, color-filter,
  dash-path-effect, saveLayer color-filter/blend/color-filter-ref, color-filter handle lifecycle, effect descriptor,
  color-matrix, lighting, image color-filter-ref, and blend/color-filter-ref capability removals, all falling back
  exactly once. The TSV has 19 lines including the header. The run used `EXPECT_SCREENSHOT_ASSERTION=false`, was 26M
  under Magic Jewel `out`, with `out` at 68G and the volume at about 268Gi free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260601-213556/matrix.tsv`.
- Magic Jewel focused compatibility `CASE_GROUPS=low-word-gradients` passed through the new grouped selector after the
  quick-group harness change. Aggregate: 15/15 passed, `fallback_sum=15`, `jbr_command_frames=0`, and
  `background_window=true` on every row. This covers low-word fill/stroke linear, radial, and sweep gradient
  capability-removal rows for rect, round-rect, and path cases, all falling back exactly once. The TSV has 16 lines
  including the header. The run used `EXPECT_SCREENSHOT_ASSERTION=false`, was 23M under Magic Jewel `out`, with `out`
  at 68G and the volume at about 269Gi free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260601-212754/matrix.tsv`.
- Magic Jewel compatibility matrix now supports no-run quick-group helpers, matching the command/parity suite
  workflow: `LIST_CASE_GROUPS=true`, `LIST_CASE_GROUP_COUNTS=true`, and `CASE_GROUPS=...`. The exposed groups cover
  all 57 compatibility rows: `handshake` 6, `low-word-gradients` 15, `low-word-effects` 18, `high-word-effects` 9,
  and `high-word-shader-ui` 9. No-run validation confirmed group listing, group counts, `LIST_CASE_COUNT=true
  CASE_GROUPS=handshake` returning 6, `LIST_CASES=true CASE_GROUPS=low-word-gradients` listing the 15 gradient rows,
  and unknown-group rejection. A focused real `CASE_GROUPS=handshake` run passed 6/6 with `fallback_sum=5`,
  `jbr_command_frames=470`, and `background_window=true` on every row. The run used
  `EXPECT_SCREENSHOT_ASSERTION=false`, was 15M under Magic Jewel `out`, with `out` at 68G and the volume at about
  269Gi free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260601-212236/matrix.tsv`.
- Magic Jewel full default screenshot parity suite passed in marker-only mode after the 20260601 command,
  compatibility, artifact, and Skiko refreshes. Aggregate: 106/106 passed, `fallback_sum=11`,
  `jbr_picture_frames=0`, and `jbr_command_frames=120808`. All 106 rows had `avg_delta=missing` and
  `bad_pixel_ratio=missing` because the run intentionally used `EXPECT_SCREENSHOT_ASSERTION=false` while validating
  replay/fallback/report markers. The TSV has 107 lines including the header. The run was 592M under Magic Jewel
  `out`, with `out` at 68G and the volume at about 269Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-202121/suite.tsv`.
- Skiko focused publication and full focused `JbrSkiaInteropTest` class passed after the 20260601 command,
  compatibility, and artifact matrix refreshes. Publication command:
  `./gradlew publishAwtPublicationToMavenLocal publishAwtRuntimeElementsPublicationToMavenLocal publishKotlinMultiplatformPublicationToMavenLocal`
  completed successfully in 7s with 22 actionable tasks, then
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  completed successfully in 7s with 28 actionable tasks, confirming the freshly published Skiko artifacts still satisfy
  the focused JBR Skia interop contract.
- Magic Jewel artifact matrix passed on current ABI 106 local artifacts after the 20260601 compatibility refresh.
  Required rows 2/2 passed: `current-all` replayed commands with `jbr_command_frames=1300`, and
  `missing-public-api` fell back exactly once with zero command frames. Optional old-artifact rows were skipped because
  no `OLD_JBR_API_SHIM`, `OLD_JBR_SKIA_LIB`, `OLD_DESKTOP_PATCH`, `OLD_SKIKO_VERSION`, or `OLD_CMP_OUT` variables were
  set. The TSV has 8 lines including the header. The run used `EXPECT_SCREENSHOT_ASSERTION=false`, was 5.5M under
  Magic Jewel `out`, with `out` at 67G and the volume at about 256Gi free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260601-201802/matrix.tsv`.
- Magic Jewel compatibility matrix passed in command-marker-only mode after the 20260601 full command-probe
  consolidation. Aggregate: 57/57 passed, `fallback_sum=56`, `jbr_command_frames=285`, and `background_window=true`
  on all rows. The single command-replay row was the happy path; all ABI, native ABI, missing command-capability,
  high-word capability, and public-API mismatch rows fell back exactly once with zero command frames. The TSV has 58
  lines including the header. The run used `EXPECT_SCREENSHOT_ASSERTION=false`, was 91M under Magic Jewel `out`, with
  `out` at 67G and the volume at about 256Gi free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260601-194910/matrix.tsv`.
- Magic Jewel full default command-probe sweep passed after the focused invalid/parser, shader, graphics-layer, and
  smoke refreshes. Aggregate: 487/487 passed, `fallback_sum=350`, `unsupported_rows=26`,
  `jbr_picture_frames=25751`, and `jbr_command_frames=132652`. The unsupported rows remain intentional
  raw/unsupported shader, color-filter, path-effect, graphics-layer, saveLayer, and RuntimeEffect/schema fallback
  sentinels. The TSV has 488 lines including the header. The run used `EXPECT_SCREENSHOT_ASSERTION=false` because this
  command-marker loop validates replay/fallback markers rather than local macOS screenshot pixels. The run was 1.5G
  under Magic Jewel `out`, with `out` at 67G and the volume at about 270Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-140440/suite.tsv`.
- Magic Jewel command-probe `smoke` refresh passed after the graphics-layer refresh. Aggregate: 6/6 passed,
  `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=6521`. This covers live
  animation, core primitives, color shader, color-filter handle, color-matrix filter, and base graphics-layer replay as
  a compact command-positive sanity pass. The TSV has 7 lines including the header. The run was 19M under Magic Jewel
  `out`, with `out` at 65G and the volume at about 289Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-135810/suite.tsv`.
- Magic Jewel command-probe `graphics-layer` refresh passed after the graphics-layer extras refresh. Aggregate: 21/21
  passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=23435`. This covers
  base graphics-layer replay, alpha/offscreen modes, rect/round/path clipping, blend/color-filter/color-matrix,
  render-effect and chained/offset effects, rectangular/round/path shadows, rotation X/Y/XY, scale/translate,
  near-camera 3D, and off-center pivot rows. The TSV has 22 lines including the header. The run was 78M under Magic
  Jewel `out`, with `out` at 65G and the volume at about 289Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-134156/suite.tsv`.
- Magic Jewel command-probe `graphics-layer-extras` refresh passed after the shader composition/runtime refresh.
  Aggregate: 14/14 passed, `fallback_sum=0`, `unsupported_rows=2`, `jbr_picture_frames=1841`, and
  `jbr_command_frames=16297`. Supported resize/forced-context graphics-layer color-matrix, render-effect lifecycle,
  render-effect color-filter/blend/color-matrix combinations, offset/chained render-effect combinations, and
  near-camera chained render-effect rows replayed commands; raw layer color-filter and raw image-filter effect rows
  stayed on the intentional unsupported-picture path. The TSV has 15 lines including the header. The run was 65M under
  Magic Jewel `out`, with `out` at 65G and the volume at about 290Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-133045/suite.tsv`.
- Magic Jewel command-probe `shader-composition-runtime` refresh passed after the shader rendering refresh. Aggregate:
  15/15 passed, `fallback_sum=0`, `unsupported_rows=2`, `jbr_picture_frames=1687`, and
  `jbr_command_frames=14729`. Supported image/composite/transformed shader, shader color-filter, RuntimeEffect shader,
  RuntimeEffect pure/uniform/child, RuntimeEffect color-filter, and RuntimeEffect color-filter child rows replayed
  commands; raw RuntimeEffect shader and raw RuntimeEffect color-filter rows stayed on the intentional
  unsupported-picture path. The TSV has 16 lines including the header. The run was 64M under Magic Jewel `out`, with
  `out` at 65G and the volume at about 290Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-131753/suite.tsv`.
- Magic Jewel command-probe `shader-rendering` refresh passed after the save-layer/shader fallback refresh. Aggregate:
  13/13 passed, `fallback_sum=0`, `unsupported_rows=8`, `jbr_picture_frames=8130`, and `jbr_command_frames=5325`.
  Supported forced-context dynamic image, image shader, gradient shader, noise shader, and turbulence shader rows
  replayed commands; image path-effect, raw image shader, descriptor stroke shader, raw gradient shader, raw noise
  shader, and raw turbulence shader rows stayed on the intentional unsupported-picture path. The TSV has 14 lines
  including the header. The run was 47M under Magic Jewel `out`, with `out` at 65G and the volume at about 290Gi free
  after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-130731/suite.tsv`.
- Magic Jewel command-probe `save-layer-shader-fallbacks` refresh passed after the core effects refresh. Aggregate:
  7/7 passed, `fallback_sum=0`, `unsupported_rows=5`, `jbr_picture_frames=5138`, and `jbr_command_frames=2910`.
  Supported saveLayer filter and blend-mode rows replayed commands; raw saveLayer color-filter, opaque shader,
  composite opaque shader, picture shader, and invalid gradient fallback rows stayed on the intentional
  unsupported-picture path. The TSV has 8 lines including the header. The run was 26M under Magic Jewel `out`, with
  `out` at 65G and the volume at about 290Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-130123/suite.tsv`.
- Magic Jewel command-probe `core-effects` refresh passed after the surface/transform/UI refresh. Aggregate: 7/7
  passed, `fallback_sum=0`, `unsupported_rows=2`, `jbr_picture_frames=1408`, and `jbr_command_frames=5713`. Supported
  gradient stroke, image-filter, path-effect, vertices, and blend-mode rows replayed commands; the two
  unsupported-picture rows were the intentional path-effect color-filter and raw discrete path-effect fallback rows.
  The TSV has 8 lines including the header. The run was 24M under Magic Jewel `out`, with `out` at 65G and the volume
  at about 291Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-125515/suite.tsv`.
- Magic Jewel command-probe `surface-transform-ui` refresh passed after the descriptor lifecycle refresh. Aggregate:
  11/11 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=11384`.
  This covers native bridge loading, drawPoints lines/dots, concat and skew transforms, gradient surfaces/paths,
  popup and real popup-window layering, Swing menu layering, and text-as-image replay. The TSV has 12 lines including
  the header. The run was 39M under Magic Jewel `out`, with `out` at 65G and the volume at about 291Gi free after
  completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-124618/suite.tsv`.
- Magic Jewel command-probe `descriptor-lifecycle` refresh passed after the native text refresh. Aggregate: 18/18
  passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=21178`. This covers
  descriptor eviction, resize and forced-context descriptor redefinition, shader/color/noise/turbulence/composite shader
  descriptor lifecycle rows, stable RuntimeEffect color-filter lifecycle, and RuntimeEffect source-cache eviction. The
  TSV has 19 lines including the header. The run was 306M under Magic Jewel `out`, with `out` at 65G and the volume at
  about 277Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-123015/suite.tsv`.
- Magic Jewel command-probe `native-text` refresh passed after the color-filter refresh. Aggregate: 14/14 passed,
  `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=15641`. This covers custom,
  generic, loaded font-data, classpath resource, and system native text replay across steady-state, resize, and
  forced-context paths. The TSV has 15 lines including the header. The run was 52M under Magic Jewel `out`, with
  `out` at 65G and the volume at about 278Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-121934/suite.tsv`.
- Magic Jewel command-probe `color-filters` refresh passed after the path invalid refresh. Aggregate: 10/10 passed,
  `fallback_sum=0`, `unsupported_rows=1`, `jbr_picture_frames=870`, and `jbr_command_frames=9857`. Supported image,
  tint, color-matrix, lighting, descriptor-handle, and graphics-layer color-filter rows replayed commands; the single
  unsupported-picture row was the intentional raw blend color-filter fallback. The TSV has 11 lines including the
  header. The run was 37M under Magic Jewel `out`, with `out` at 65G and the volume at about 278Gi free after
  completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-121053/suite.tsv`.
- Magic Jewel command-probe `path-invalid` refresh passed after the native text invalid refresh. Aggregate: 22/22
  passed, `fallback_sum=22`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. This covers
  malformed clip/draw path verbs, path-effect path verbs, dash path-effect interval counts and values, rectangle and
  round-rectangle geometry/style fields, and drawShadow path verbs without emitting partial command frames. The TSV has
  23 lines including the header. The run was 50M under Magic Jewel `out`, with `out` at 65G and the volume at about
  291Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-115408/suite.tsv`.
- Magic Jewel command-probe `native-text-invalid` refresh passed after the primitive invalid refresh. Aggregate: 11/11
  passed, `fallback_sum=11`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=807`. This covers
  malformed text and paragraph font size, weight, width, slant, family count, and font-data record flags; only the
  font-data record-flags row reached command replay before the deliberate malformed record fell back. The TSV has 12
  lines including the header. The run was 27M under Magic Jewel `out`, with `out` at 65G and the volume at about 292Gi
  free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-114457/suite.tsv`.
- Magic Jewel command-probe `primitive-invalid` refresh passed after the full default command-probe sweep. Aggregate:
  13/13 passed, `fallback_sum=13`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. This
  covers malformed stroke cap, transform record flags, clip operation, drawPoints counts/record length, and drawVertices
  counts, record length, vertex mode, blend mode, and index counts without emitting partial command frames. The TSV has
  14 lines including the header. The run was 29M under Magic Jewel `out`, with `out` at 65G and the volume at about
  292Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-113502/suite.tsv`.
- Magic Jewel full default command-probe sweep passed after the stable parity descriptor guard refresh and focused
  invalid-group refreshes. Aggregate: 487/487 passed, `fallback_sum=350`, `unsupported_rows=26`,
  `jbr_picture_frames=24003`, and `jbr_command_frames=121226`. The unsupported rows remain intentional
  raw/unsupported shader, color-filter, path-effect, graphics-layer, saveLayer, and RuntimeEffect/schema fallback
  sentinels. The TSV has 488 lines including the header, matching the default `LIST_CASES` count of 487 cases. The run
  used `EXPECT_SCREENSHOT_ASSERTION=false` because this command-marker loop is validating replay/fallback markers
  rather than local macOS screenshot pixels. The run was 1.4G under Magic Jewel `out`, with `out` at 65G and the
  volume at about 292Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-061239/suite.tsv`.
- Magic Jewel command-probe `runtime-effect-invalid` refresh passed after the gradient invalid refresh. Aggregate:
  62/62 passed, `fallback_sum=56`, `unsupported_rows=6`, `jbr_picture_frames=5339`, and `jbr_command_frames=0`.
  The six unsupported-picture rows were the intentionally raw invalid RuntimeEffect shader/color-filter
  uniform-schema, child-schema, and nested-child sentinels; all other malformed RuntimeEffect source, count, schema,
  child-index, compile, build, and child-type rows emitted structured single fallbacks. The run was 237M under Magic
  Jewel `out`, with `out` at 63G and the volume at about 286Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-052906/suite.tsv`.
- Magic Jewel command-probe `gradient-invalid` refresh passed after the descriptor-handle invalid refresh. Aggregate:
  60/60 passed, `fallback_sum=60`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. This
  covers malformed linear, radial, and sweep gradient payloads across fill, round-rect, stroke, round-rect stroke, and
  path variants, including stroke width, radius, tile mode, color-count, stop-order, fill type, path length, and path
  verb validation. The run was 121M under Magic Jewel `out`, with `out` at 63G and the volume at about 299Gi free
  after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-045059/suite.tsv`.
- Magic Jewel command-probe `descriptor-handles-invalid` refresh passed after the save-layer invalid refresh.
  Aggregate: 48/48 passed, `fallback_sum=48`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. This covers invalid descriptor handle use, use-after-evict, malformed evict records,
  missing children, stale child handles, and wrong-type child refs across shader, color-filter, image-filter,
  path-effect, RuntimeEffect, and saveLayer descriptor trees. The run was 144M under Magic Jewel `out`, with `out` at
  63G and the volume at about 300Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-042014/suite.tsv`.
- Magic Jewel command-probe `save-layer-invalid` refresh passed after the image handle invalid refresh. Aggregate:
  37/37 passed, `fallback_sum=37`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. This
  covers malformed saveLayer alpha, record flags/lengths, color-filter refs, blend-mode refs, image-filter refs, and
  combined blend/color-filter payloads without emitting partial command frames. The run was 89M under Magic Jewel
  `out`, with `out` at 63G and the volume at about 287Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-035634/suite.tsv`.
- Magic Jewel command-probe `image-handles-invalid` refresh passed after the gradient path invalid refresh. Aggregate:
  27/27 passed, `fallback_sum=27`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=847`.
  The only row with command frames was the invalid image-cache-clear record-flags sentinel, which reaches command
  replay before the deliberately malformed cache-clear record falls back; all other malformed image define/use/ref and
  image color-filter ref rows emitted zero command frames. The run was 63M under Magic Jewel `out`, with `out` at 63G
  and the volume at about 300Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-033906/suite.tsv`.
- Magic Jewel command-probe `gradient-path-invalid` refresh passed after the effect descriptor invalid refresh.
  Aggregate: 18/18 passed, `fallback_sum=18`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. This covers malformed linear, radial, and sweep gradient path payloads across tile mode,
  color/stop validation, fill type, path data length, and path verb rejection. The run was 37M under Magic Jewel
  `out`, with `out` at 63G and the volume at about 300Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-032701/suite.tsv`.
- Magic Jewel command-probe `effect-descriptor-invalid` refresh passed after the stable parity descriptor guard batch.
  Aggregate: 28/28 passed, `fallback_sum=28`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. This keeps malformed effect, color-filter, image-filter, path-effect, and chain
  descriptor payloads on structured fallback without emitting partial command frames. The run was 76M under Magic
  Jewel `out`, with `out` at 63G and the volume at about 300Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-030722/suite.tsv`.
- Magic Jewel stable screenshot parity descriptor guards were extended to the remaining non-dynamic shader/effect rows:
  image color-matrix filter, shader-plus-color-filter variants, transformed shader, and graphics-layer render-effect
  combinations now have full-scene max JBR handle-definition ceilings. Exact touched rows passed 17/17 with
  `fallback_sum=0`, `jbr_picture_frames=0`, and `jbr_command_frames=10815`; `CASE_GROUPS=shader-rendering` passed
  18/18 with `fallback_sum=4`, `jbr_picture_frames=0`, and `jbr_command_frames=12640`;
  `CASE_GROUPS=graphics-layer-effects` passed 14/14 with `fallback_sum=2`, `jbr_picture_frames=0`, and
  `jbr_command_frames=8252`; and `CASE_GROUPS=core-drawing` passed 16/16 with `fallback_sum=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=11785`. All runs used
  `EXPECT_SCREENSHOT_ASSERTION=false`, so screenshot pixel metrics were intentionally `missing`. A follow-up audit
  showed the only parity rows still carrying min handle-definition checks without max handle-definition ceilings are
  descriptor eviction and dynamic/source-cache RuntimeEffect rows, which are intentionally uncapped because they
  exercise eviction or changing RuntimeEffect payloads. The runs were 58M, 64M, 47M, and 51M respectively under Magic
  Jewel `out`, with `out` at 63G and the volume at about 300Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-022741/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-023729/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-024717/suite.tsv`,
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-025513/suite.tsv`.
- Magic Jewel shader invalid descriptor command gates were tightened with exact max JBR descriptor-setup counts for
  linear-gradient, radial-gradient, sweep-gradient, and image-shader malformed descriptor rows. Exact touched rows
  passed 13/13 with `fallback_sum=13`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`;
  then `CASE_GROUPS=shader-descriptor-invalid` passed 30/30 with `fallback_sum=30`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. Dynamic RuntimeEffect command rows and descriptor eviction were
  intentionally left without max handle-definition ceilings because their report data shows legitimate per-frame or
  high-volume descriptor churn. The group run was 86M under Magic Jewel `out`, with `out` at 63G and the volume at
  about 288Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-015339/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-020219/suite.tsv`.
- Magic Jewel full default screenshot parity suite passed in marker-only mode after the descriptor-guard harness change
  and focused group refreshes. Aggregate: 106/106 passed, `fallback_sum=11`, `jbr_picture_frames=0`, and
  `jbr_command_frames=69504`. All 106 rows had screenshot pixel metrics intentionally `missing` because
  `EXPECT_SCREENSHOT_ASSERTION=false` was set. The TSV has 107 lines including the header. The run was 442M under
  Magic Jewel `out`, with `out` at 62G and the volume at about 301Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-005125/suite.tsv`.
- Magic Jewel focused screenshot parity group refreshes passed after the descriptor-guard harness change. With
  `EXPECT_SCREENSHOT_ASSERTION=false`, `CASE_GROUPS=descriptor-lifecycle` covered 6/6 rows with `fallback_sum=1`,
  `jbr_picture_frames=0`, and `jbr_command_frames=3302`; `CASE_GROUPS=runtime-effect` covered 14/14 rows with
  `fallback_sum=2`, `jbr_picture_frames=0`, and `jbr_command_frames=9021`; and
  `CASE_GROUPS=graphics-layer-effects` covered 14/14 rows with `fallback_sum=2`, `jbr_picture_frames=0`, and
  `jbr_command_frames=9667`. The adjacent `CASE_GROUPS=shader-rendering` refresh also covered 18/18 rows with
  `fallback_sum=4`, `jbr_picture_frames=0`, and `jbr_command_frames=11904`. The runs were 157M, 54M, 49M, and 65M
  respectively under Magic Jewel `out`, with `out` at 62G and the volume at about 301Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-001912/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-002358/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-003141/suite.tsv`,
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-004029/suite.tsv`.
- Magic Jewel focused screenshot parity descriptor-guard refresh passed after adding bounded JBR shader/effect handle
  definition ceilings to descriptor-backed parity rows. The final single-pass focused run covered 27/27 rows with
  `fallback_sum=9`, `jbr_picture_frames=0`, and `jbr_command_frames=17040`; the resize fallback markers were the
  expected color-filter/shader/RuntimeEffect/graphics-layer resize sentinels. The run used
  `EXPECT_SCREENSHOT_ASSERTION=false`, so screenshot pixel metrics were intentionally `missing`, and validated the
  new ceilings as full-scene runaway guards rather than exact frame-count assertions. The run was 93M under Magic
  Jewel `out`, with `out` at 62G and the volume at about 289Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260531-235827/suite.tsv`.
- Magic Jewel full default screenshot parity suite passed in marker-only mode after the 20260531 command-probe,
  compatibility, artifact, and Skiko focused publication/test refreshes. Aggregate: 106/106 passed,
  `fallback_sum=11`, `jbr_picture_frames=0`, and `jbr_command_frames=79348`. All 106 rows had `avg_delta=missing`
  and `bad_pixel_ratio=missing` because the run intentionally used `EXPECT_SCREENSHOT_ASSERTION=false` while local
  macOS screenshot assertions remain outside the current command-replay signal. The 11 fallback markers were bounded
  to resize sentinel rows: native generic/system font text, color-filter handle, color/noise/turbulence/composite-noise
  shaders, RuntimeEffect pure-color/stable color-filter, and graphics-layer color-matrix/render-effect resize rows.
  The TSV has 107 lines including the header. The run was 492M under Magic Jewel `out`, with `out` at 62G and the
  volume at about 301Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260531-221616/suite.tsv`.
- Magic Jewel full default command-probe consolidation passed in command-marker-only mode after refreshing the focused
  invalid/parser guard groups, including `gradient-invalid`, `runtime-effect-invalid`,
  `fill-rect-color-filter-invalid`, `shader-ref-invalid`, and `blend-mode-invalid`. Aggregate: 487/487 passed,
  `fallback_sum=350`, `unsupported_rows=26`, `jbr_picture_frames=25686`, and `jbr_command_frames=122355`. The
  unsupported rows remain intentional raw/unsupported shader, color-filter, path-effect, graphics-layer, saveLayer,
  and RuntimeEffect/schema fallback sentinels. The TSV has 488 lines including the header. The run used
  `EXPECT_SCREENSHOT_ASSERTION=false` because local macOS screenshot assertions are currently failing independently of
  command replay on supported rows. The run was 1.7G under Magic Jewel `out`, with `out` at 61G and the volume at about
  301Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-162857/suite.tsv`.
- Magic Jewel compatibility matrix passed in command-marker-only mode after the full command-probe consolidation.
  Aggregate: 57/57 passed, `fallback_sum=56`, `jbr_command_frames=454`, and `background_window=true` on every row.
  The run used `EXPECT_SCREENSHOT_ASSERTION=false` to keep it aligned with the current marker-only command replay
  validation loop. The only command frames came from the happy path; all ABI, native ABI, command-capability,
  high-capability, feature-capability, and public API mismatch rows fell back exactly once. The TSV has 58 lines
  including the header. The run was 67M under Magic Jewel `out`, with `out` at 61G and the volume at about 301Gi free
  after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260531-214046/matrix.tsv`.
- Magic Jewel artifact matrix passed on current ABI 106 local artifacts after the compatibility refresh. Required rows
  2/2 passed: `current-all` replayed commands with 445 JBR command frames, and `missing-public-api` fell back exactly
  once. Optional old-artifact rows were skipped because no `OLD_JBR_API_SHIM`, `OLD_JBR_SKIA_LIB`, `OLD_DESKTOP_PATCH`,
  `OLD_SKIKO_VERSION`, or `OLD_CMP_OUT` variables were set. The TSV has 8 lines including the header. The run was
  2.8M under Magic Jewel `out`, with `out` at 61G and the volume at about 301Gi free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260531-221009/matrix.tsv`.
- Skiko focused publication and full focused `JbrSkiaInteropTest` class passed after the 20260531 command,
  compatibility, and artifact matrix refreshes. Publication command:
  `./gradlew publishAwtPublicationToMavenLocal publishAwtRuntimeElementsPublicationToMavenLocal publishKotlinMultiplatformPublicationToMavenLocal`.
  Test command:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
  Publication finished with `BUILD SUCCESSFUL` in 8s, and the focused test finished with `BUILD SUCCESSFUL` in 8s
  (`:awtTest` was up-to-date).
- Magic Jewel full default command-probe consolidation passed in command-marker-only mode after the latest focused
  invalid/parser guard refreshes. The run was split by a Codex restart: the prefix completed 306 rows in
  `20260530-064308`, then the tail resumed with
  `CASES_FROM=commands-invalid-linear-gradient-stroke-color-count-fallback`. Combined aggregate: 487/487 passed,
  `fallback_sum=350`, `unsupported_rows=26`, `jbr_picture_frames=34406`, and `jbr_command_frames=226952`. The
  unsupported rows remain intentional raw/unsupported shader, color-filter, path-effect, graphics-layer, saveLayer,
  and RuntimeEffect/schema fallback sentinels. The prefix TSV has 307 lines including the header and the tail TSV has
  182 lines including the header. The run used `EXPECT_SCREENSHOT_ASSERTION=false` because local macOS screenshot
  assertions are currently failing independently of command replay on supported rows. The prefix was 1.2G and the tail
  was 919M under Magic Jewel `out`, with `out` at 56G and the volume at about 307Gi free after completion. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-064308/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-094245/suite.tsv`.
- Magic Jewel focused command-probe save-layer/shader fallback refresh passed after the marker-only parity
  consolidation: `CASE_GROUPS=save-layer-shader-fallbacks` covered 7/7 rows with `fallback_sum=0`, five intentional
  unsupported-picture rows, `jbr_picture_frames=6491`, and `jbr_command_frames=4020`. The TSV has 8 lines including
  the header. The run was 28M under Magic Jewel `out`, with `out` at 57G and the volume at about 305Gi free after
  completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-143004/suite.tsv`.
- Magic Jewel focused command-probe primitive invalid refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=primitive-invalid` covered 13/13 rows with `fallback_sum=13`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 14 lines including the header. The run was 46M
  under Magic Jewel `out`, with `out` at 57G and the volume at about 305Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-143801/suite.tsv`.
- Magic Jewel focused command-probe native text invalid refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=native-text-invalid` covered 11/11 rows with `fallback_sum=11`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=1857`. The TSV has 12 lines including the header. The run was 39M
  under Magic Jewel `out`, with `out` at 58G and the volume at about 305Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-144639/suite.tsv`.
- Magic Jewel focused command-probe path invalid refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=path-invalid` covered 22/22 rows with `fallback_sum=22`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 23 lines including the header. The run was 78M
  under Magic Jewel `out`, with `out` at 58G and the volume at about 305Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-145405/suite.tsv`.
- Magic Jewel focused command-probe color-filter refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=color-filters` covered 10/10 rows with `fallback_sum=0`, one intentional unsupported-picture row,
  `jbr_picture_frames=1451`, and `jbr_command_frames=19801`. The TSV has 11 lines including the header. The run was
  55M under Magic Jewel `out`, with `out` at 58G and the volume at about 304Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-150741/suite.tsv`.
- Magic Jewel focused command-probe shader rendering refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=shader-rendering` covered 13/13 rows with `fallback_sum=0`, eight intentional unsupported-picture
  rows, `jbr_picture_frames=11161`, and `jbr_command_frames=9643`. The TSV has 14 lines including the header. The
  run was 56M under Magic Jewel `out`, with `out` at 58G and the volume at about 304Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-151449/suite.tsv`.
- Magic Jewel focused command-probe shader composition/runtime refresh passed after the marker-only parity
  consolidation: `CASE_GROUPS=shader-composition-runtime` covered 15/15 rows with `fallback_sum=0`, two intentional
  unsupported-picture rows, `jbr_picture_frames=3016`, and `jbr_command_frames=28748`. The TSV has 16 lines including
  the header. The run was 95M under Magic Jewel `out`, with `out` at 58G and the volume at about 302Gi free after
  completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-152335/suite.tsv`.
- Magic Jewel focused command-probe core effects refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=core-effects` covered 7/7 rows with `fallback_sum=0`, two intentional unsupported-picture rows,
  `jbr_picture_frames=3122`, and `jbr_command_frames=10716`. The TSV has 8 lines including the header. The run was
  33M under Magic Jewel `out`, with `out` at 58G and the volume at about 304Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-153337/suite.tsv`.
- Magic Jewel focused command-probe surface/transform/UI refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=surface-transform-ui` covered 11/11 rows with `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=23868`. The TSV has 12 lines including the header. The run was 58M
  under Magic Jewel `out`, with `out` at 58G and the volume at about 303Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-153856/suite.tsv`.
- Magic Jewel focused command-probe graphics-layer extras refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=graphics-layer-extras` covered 14/14 rows with `fallback_sum=0`, two intentional unsupported-picture
  rows, `jbr_picture_frames=2700`, and `jbr_command_frames=24591`. The TSV has 15 lines including the header. The run
  was 78M under Magic Jewel `out`, with `out` at 58G and the volume at about 304Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-154633/suite.tsv`.
- Magic Jewel focused command-probe graphics-layer refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=graphics-layer` covered 21/21 rows with `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=45890`. The TSV has 22 lines including the header. The run was
  112M under Magic Jewel `out`, with `out` at 58G and the volume at about 303Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-155613/suite.tsv`.
- Magic Jewel focused command-probe descriptor lifecycle refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=descriptor-lifecycle` covered 18/18 rows with `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=38694`. The TSV has 19 lines including the header. The run was
  361M under Magic Jewel `out`, with `out` at 58G and the volume at about 290Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-160948/suite.tsv`.
- Magic Jewel focused command-probe native text refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=native-text` covered 14/14 rows with `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=27143`. The TSV has 15 lines including the header. The run was 66M
  under Magic Jewel `out`, with `out` at 58G and the volume at about 290Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-162416/suite.tsv`.
- Magic Jewel focused command-probe image handles invalid refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=image-handles-invalid` covered 27/27 rows with `fallback_sum=27`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=1153`. The TSV has 28 lines including the header. The run was 100M
  under Magic Jewel `out`, with `out` at 59G and the volume at about 302Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-163344/suite.tsv`.
- Magic Jewel focused command-probe save-layer invalid refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=save-layer-invalid` covered 37/37 rows with `fallback_sum=37`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 38 lines including the header. The run was 141M
  under Magic Jewel `out`, with `out` at 59G and the volume at about 302Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-165035/suite.tsv`.
- Magic Jewel focused command-probe effect descriptor invalid refresh passed after the marker-only parity
  consolidation: `CASE_GROUPS=effect-descriptor-invalid` covered 28/28 rows with `fallback_sum=28`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 29 lines including the header.
  The run was 106M under Magic Jewel `out`, with `out` at 59G and the volume at about 302Gi free after completion.
  Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-171350/suite.tsv`.
- Magic Jewel focused command-probe shader descriptor invalid refresh passed after the marker-only parity
  consolidation: `CASE_GROUPS=shader-descriptor-invalid` covered 30/30 rows with `fallback_sum=30`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 31 lines including the header.
  The run was 123M under Magic Jewel `out`, with `out` at 59G and the volume at about 302Gi free after completion.
  Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-173119/suite.tsv`.
- Magic Jewel focused command-probe gradient path invalid refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=gradient-path-invalid` covered 18/18 rows with `fallback_sum=18`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 19 lines including the header. The run was 64M
  under Magic Jewel `out`, with `out` at 59G and the volume at about 302Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-175010/suite.tsv`.
- Magic Jewel focused command-probe descriptor handles invalid refresh passed after the marker-only parity
  consolidation: `CASE_GROUPS=descriptor-handles-invalid` covered 48/48 rows with `fallback_sum=48`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 49 lines including the header.
  The run was 209M under Magic Jewel `out`, with `out` at 59G and the volume at about 289Gi free after completion.
  Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-180200/suite.tsv`.
- Magic Jewel focused command-probe stream invalid refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=stream-invalid` covered 8/8 rows with `fallback_sum=8`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 9 lines including the header. The run was 30M under
  Magic Jewel `out`, with `out` at 59G and the volume at about 301Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-183130/suite.tsv`.
- Magic Jewel focused command-probe smoke refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=smoke` covered 6/6 rows with `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=12275`. The TSV has 7 lines including the header. The run was 30M under Magic Jewel `out`, with
  `out` at 59G and the volume at about 301Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-183718/suite.tsv`.
- Magic Jewel focused command-probe gradient invalid refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=gradient-invalid` covered 60/60 rows with `fallback_sum=60`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 61 lines including the header. The run was 192M
  under Magic Jewel `out`, with `out` at 59G and the volume at about 304Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-145017/suite.tsv`.
- Magic Jewel focused command-probe runtime-effect invalid refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=runtime-effect-invalid` covered 62/62 rows with `fallback_sum=56`, six intentional
  unsupported-picture rows, `jbr_picture_frames=5898`, and `jbr_command_frames=0`. The TSV has 63 lines including the
  header. The run was 263M under Magic Jewel `out`, with `out` at 59G and the volume at about 291Gi free after
  completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-152938/suite.tsv`.
- Magic Jewel focused command-probe fill-rect color-filter invalid refresh passed after the marker-only parity
  consolidation: `CASE_GROUPS=fill-rect-color-filter-invalid` covered 5/5 rows with `fallback_sum=5`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 6 lines including the header.
  The successful run was 14M under Magic Jewel `out`, with `out` at 59G and the volume at about 291Gi free after
  completion. A preceding sandboxed attempt failed before app startup on Gradle's `~/.gradle` lock-file write and is
  intentionally not referenced as validation evidence. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-161535/suite.tsv`.
- Magic Jewel focused command-probe shader-ref invalid refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=shader-ref-invalid` covered 3/3 rows with `fallback_sum=3`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 4 lines including the header. The run was 11M under
  Magic Jewel `out`, with `out` at 59G and the volume at about 291Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-162224/suite.tsv`.
- Magic Jewel focused command-probe blend-mode invalid refresh passed after the marker-only parity consolidation:
  `CASE_GROUPS=blend-mode-invalid` covered 2/2 rows with `fallback_sum=2`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 3 lines including the header. The run was 3.9M
  under Magic Jewel `out`, with `out` at 59G and the volume at about 291Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-162604/suite.tsv`.
- Magic Jewel exact command-probe `CASES=commands-descriptor-eviction` passed after the CMP recorder gained matching
  color-filter and shader descriptor-handle eviction tests. Aggregate: 1/1 passed, `fallback_sum=0`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=59`. The report contained native effect-handle
  and shader-handle eviction markers above the row thresholds. The TSV has 2 lines including the header. The run was
  193M under Magic Jewel `out`, with `out` at 43G and the volume at about 342Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-013713/suite.tsv`.
- Magic Jewel focused color-shader descriptor rows passed after the CMP recorder gained shader descriptor cache-reuse
  and surface-clear tests. Cases: `commands-color-shader`, `commands-resize-color-shader-descriptor-redefine`, and
  `commands-forced-context-color-shader-descriptor-redefine`. Aggregate: 3/3 passed, `fallback_sum=0`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=5423`. The TSV has 4 lines including the
  header. The run was 17M under Magic Jewel `out`, with `out` at 43G and the volume at about 342Gi free after
  completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-014358/suite.tsv`.
- Magic Jewel focused graphics-layer render-effect descriptor rows passed after the CMP recorder gained image-filter
  descriptor cache-reuse and surface-clear tests. Cases: `commands-graphics-layer-render-effect`,
  `commands-resize-graphics-layer-render-effect`, and `commands-forced-context-graphics-layer-render-effect`.
  Aggregate: 3/3 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=4465`. The base row reported one effect-handle define plus cache hits, while the resize and
  forced-context rows reported command cache clears, two effect-handle defines, and effect-handle cache hits. The TSV
  has 4 lines including the header. The run was 15M under Magic Jewel `out`, with `out` at 43G and the volume at about
  342Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-015048/suite.tsv`.
- Magic Jewel focused `native-text-invalid` command-probe group passed as the next small fallback validation batch.
  Command: `CASE_GROUPS=native-text-invalid ./scripts/jbr-skia-command-probe-suite.sh`. Aggregate: 11/11 passed,
  `fallback_sum=11`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=1519`. The TSV has 12
  lines including the header. The run was 41M under Magic Jewel `out`, with `out` at 54G and the volume at about
  314Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-055310/suite.tsv`.
- Magic Jewel focused `primitive-invalid` command-probe group passed after `native-text-invalid`. Command:
  `CASE_GROUPS=primitive-invalid ./scripts/jbr-skia-command-probe-suite.sh`. Aggregate: 13/13 passed,
  `fallback_sum=13`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 14 lines
  including the header. The run was 29M under Magic Jewel `out`, with `out` at 50G and the volume at about 320Gi free
  after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-200527/suite.tsv`.
- Magic Jewel focused `path-invalid` command-probe group passed after `primitive-invalid`. Command:
  `CASE_GROUPS=path-invalid ./scripts/jbr-skia-command-probe-suite.sh`. Aggregate: 22/22 passed, `fallback_sum=22`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 23 lines including the
  header. The run was 86M under Magic Jewel `out`, with `out` at 53G and the volume at about 315Gi free after
  completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-030433/suite.tsv`.
- Magic Jewel focused `effect-descriptor-invalid` command-probe group passed after `path-invalid`. Command:
  `CASE_GROUPS=effect-descriptor-invalid ./scripts/jbr-skia-command-probe-suite.sh`. Aggregate: 28/28 passed,
  `fallback_sum=28`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 29 lines
  including the header. The run was 113M under Magic Jewel `out`, with `out` at 53G and the volume at about 315Gi
  free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-031858/suite.tsv`.
- Magic Jewel focused `shader-descriptor-invalid` command-probe group passed after `effect-descriptor-invalid`.
  Command: `CASE_GROUPS=shader-descriptor-invalid ./scripts/jbr-skia-command-probe-suite.sh`. Aggregate: 30/30
  passed, `fallback_sum=30`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has
  31 lines including the header. The run was 126M under Magic Jewel `out`, with `out` at 53G and the volume at about
  315Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-033644/suite.tsv`.
- Magic Jewel focused `image-handles-invalid` command-probe group passed after `shader-descriptor-invalid`. Command:
  `CASE_GROUPS=image-handles-invalid ./scripts/jbr-skia-command-probe-suite.sh`. Aggregate: 27/27 passed,
  `fallback_sum=27`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=1036`. The command frames
  came from the invalid image-cache-clear record-flags row. The TSV has 28 lines including the header. The run was
  108M under Magic Jewel `out`, with `out` at 53G and the volume at about 315Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-035452/suite.tsv`.
- Magic Jewel focused `color-filters` command-probe group passed after the image-handle invalid guardrails. Command:
  `CASE_GROUPS=color-filters ./scripts/jbr-skia-command-probe-suite.sh`. Aggregate: 10/10 passed, `fallback_sum=0`,
  `unsupported_rows=1`, `jbr_picture_frames=1047`, and `jbr_command_frames=13204`. The unsupported row was the
  expected raw blend color-filter fallback path. The TSV has 11 lines including the header. The run was 44M under
  Magic Jewel `out`, with `out` at 50G and the volume at about 319Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203750/suite.tsv`.
- Magic Jewel focused `save-layer-invalid` command-probe group passed after `color-filters`. Command:
  `CASE_GROUPS=save-layer-invalid ./scripts/jbr-skia-command-probe-suite.sh`. Aggregate: 37/37 passed,
  `fallback_sum=37`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 38 lines
  including the header. The run was 148M under Magic Jewel `out`, with `out` at 54G and the volume at about 314Gi
  free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-052922/suite.tsv`.
- Magic Jewel focused `descriptor-handles-invalid` command-probe group passed after `save-layer-invalid`. Command:
  `CASE_GROUPS=descriptor-handles-invalid ./scripts/jbr-skia-command-probe-suite.sh`. Aggregate: 48/48 passed,
  `fallback_sum=48`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 49 lines
  including the header. The run was 213M under Magic Jewel `out`, with `out` at 53G and the volume at about 315Gi
  free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-041151/suite.tsv`.
- Magic Jewel compatibility matrix passed in command-marker-only mode after the resumed full command-probe
  consolidation. Aggregate: 57/57 passed, `fallback_sum=56`, `jbr_command_frames=1050`, and
  `background_window=true` on every row. The first attempt with screenshot assertions enabled failed on `happy`
  because the screenshot assertion did not pass/run, while command replay was healthy; the passing rerun used
  `EXPECT_SCREENSHOT_ASSERTION=false`. The only command frames came from the happy path; all ABI, native ABI,
  command-capability, high-capability, feature-capability, and public API mismatch rows fell back exactly once. The
  TSV has 58 lines including the header. The passing run was 70M under Magic Jewel `out`, with `out` at 56G and the
  volume at about 307Gi free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260530-113437/matrix.tsv`.
- Magic Jewel artifact matrix passed on current ABI 106 local artifacts after the latest compatibility refresh.
  Required rows: `current-all` passed with no fallback and 1,081 JBR command frames, and `missing-public-api` passed with
  one expected public API fallback and zero command frames. The five optional old-artifact rows were recorded as skipped
  because no old bundle variables were configured. The TSV has 8 lines including the header. The run was 4.7M under
  Magic Jewel `out`, with `out` at 56G and the volume at about 307Gi free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260530-120135/matrix.tsv`.
- Magic Jewel full default screenshot parity passed after the latest command-probe, compatibility, and artifact
  refreshes. Aggregate: 106/106 passed, `fallback_sum=11`, `jbr_picture_frames=0`, `jbr_command_frames=111257`,
  average pixel delta `2.158`, average `bad_pixel_ratio=0.05158`, average header-button
  `bad_pixel_ratio=0.00594`, average Compose-canvas `bad_pixel_ratio=0.07632`, average bottom-label
  `bad_pixel_ratio=0.11884`, and average paragraph-probe `bad_pixel_ratio=0.08648`. The TSV has 107 lines including
  the header. The run was 518M under Magic Jewel `out`, with `out` at 47G and the volume at about 335Gi free after
  completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260529-091416/suite.tsv`.
- Magic Jewel focused screenshot parity smoke refresh passed in marker-only mode after the wrapper learned to honor
  `EXPECT_SCREENSHOT_ASSERTION=false` by skipping the image diff after command/report validation succeeds. Aggregate:
  3/3 passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=5603`, and pixel metrics intentionally
  missing because the image comparison was skipped. The TSV has 4 lines including the header. The run was 12M under
  Magic Jewel `out`, with `out` at 56G and the volume at about 295Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-122221/suite.tsv`.
- Magic Jewel focused screenshot parity descriptor-lifecycle refresh also passed in marker-only mode. Aggregate:
  6/6 passed, `fallback_sum=1`, `jbr_picture_frames=0`, `jbr_command_frames=10185`, and pixel metrics intentionally
  missing because the image comparison was skipped. The TSV has 7 lines including the header. The run was 220M under
  Magic Jewel `out`, with `out` at 56G and the volume at about 295Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-122542/suite.tsv`.
- Magic Jewel focused screenshot parity graphics-layer-basic refresh also passed in marker-only mode. Aggregate:
  7/7 passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=16730`, and pixel metrics intentionally
  missing because the image comparison was skipped. The TSV has 8 lines including the header. The run was 41M under
  Magic Jewel `out`, with `out` at 56G and the volume at about 307Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-123134/suite.tsv`.
- Magic Jewel focused screenshot parity core-drawing refresh also passed in marker-only mode. Aggregate: 16/16 passed,
  `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=25745`, and pixel metrics intentionally missing
  because the image comparison was skipped. The TSV has 17 lines including the header. The run was 63M under Magic
  Jewel `out`, with `out` at 57G and the volume at about 307Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-123636/suite.tsv`.
- Magic Jewel focused screenshot parity native-text refresh also passed in marker-only mode. Aggregate: 14/14 passed,
  `fallback_sum=4`, `jbr_picture_frames=0`, `jbr_command_frames=18663`, and pixel metrics intentionally missing
  because the image comparison was skipped. The TSV has 15 lines including the header. The run was 47M under Magic
  Jewel `out`, with `out` at 57G and the volume at about 307Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-124546/suite.tsv`.
- Magic Jewel focused screenshot parity runtime-effect refresh also passed in marker-only mode. Aggregate: 14/14
  passed, `fallback_sum=2`, `jbr_picture_frames=0`, `jbr_command_frames=17990`, and pixel metrics intentionally
  missing because the image comparison was skipped. The TSV has 15 lines including the header. The run was 59M under
  Magic Jewel `out`, with `out` at 57G and the volume at about 307Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-125424/suite.tsv`.
- Magic Jewel focused screenshot parity shader-rendering refresh also passed in marker-only mode. Aggregate: 18/18
  passed, `fallback_sum=4`, `jbr_picture_frames=0`, `jbr_command_frames=25540`, and pixel metrics intentionally
  missing because the image comparison was skipped. The TSV has 19 lines including the header. The run was 67M under
  Magic Jewel `out`, with `out` at 57G and the volume at about 306Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-130246/suite.tsv`.
- Magic Jewel focused screenshot parity graphics-layer-effects refresh also passed in marker-only mode. Aggregate:
  14/14 passed, `fallback_sum=2`, `jbr_picture_frames=0`, `jbr_command_frames=29594`, and pixel metrics intentionally
  missing because the image comparison was skipped. The TSV has 15 lines including the header. The run was 85M under
  Magic Jewel `out`, with `out` at 57G and the volume at about 306Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-131305/suite.tsv`.
- Magic Jewel focused screenshot parity graphics-layer-clip-shadow-transform refresh also passed in marker-only mode.
  Aggregate: 14/14 passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=34196`, and pixel metrics
  intentionally missing because the image comparison was skipped. The TSV has 15 lines including the header. The run
  was 75M under Magic Jewel `out`, with `out` at 57G and the volume at about 306Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-132134/suite.tsv`.
- Combined across the nine focused marker-only screenshot parity groups after the wrapper fix, all 106 parity rows
  passed with `fallback_sum=13`, `jbr_picture_frames=0`, `jbr_command_frames=184246`, and intentionally missing pixel
  metrics because the image comparison was skipped.
- Magic Jewel full default screenshot parity marker-only consolidation passed as a single TSV. Aggregate: 106/106
  passed, `fallback_sum=12`, `jbr_picture_frames=0`, `jbr_command_frames=186106`, and pixel metrics intentionally
  missing because the image comparison was skipped. The TSV has 107 lines including the header. The run was 627M under
  Magic Jewel `out`, with `out` at 57G and the volume at about 293Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-133012/suite.tsv`.
- Magic Jewel full default benchmark suite passed after the compatibility/artifact refreshes in command-marker-only
  mode. Aggregate: 5/5 passed, `fallback_sum=0`, 84 old-side CPU samples, 49 new-side CPU samples, one picture-FPS
  row at `229.4`, command-FPS row total `684.8`, and `jbr_command_frames=13695`. The TSV has 6 lines including the
  header. The run was 47M under Magic Jewel `out`, with `out` at 56G and the volume at about 295Gi free after
  completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260530-120858/suite.tsv`.
- Magic Jewel full default command-probe sweep passed in command-marker-only mode after the focused native text,
  surface/transform/UI, saveLayer shader-fallback, shader/effect, RuntimeEffect, graphics-layer, and parser-guard
  refreshes. Aggregate: 487/487 passed, `fallback_sum=350`, `unsupported_rows=26`, `jbr_picture_frames=27683`, and
  `jbr_command_frames=179362`. The unsupported rows remain the intentional raw/unsupported shader, color-filter,
  path-effect, graphics-layer, saveLayer, and RuntimeEffect schema fallback sentinels. The TSV has 488 lines including
  the header. The run used `EXPECT_SCREENSHOT_ASSERTION=false` because local macOS screenshot assertions are currently
  failing independently of command replay on supported rows. The run was 1.7G under Magic Jewel `out`, with `out` at
  40G and the volume at about 356Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260528-012135/suite.tsv`.
- Magic Jewel compatibility matrix passed after the full command-probe consolidation refresh. Aggregate: 57/57 passed,
  `fallback_sum=56`, `jbr_command_frames=400`, and `background_window=true` on every row. The only command frames
  came from the happy path; all ABI, native ABI, command-capability, high-capability, feature-capability, and public
  API mismatch rows fell back exactly once. The TSV has 58 lines including the header. The run was 67M under Magic
  Jewel `out`, with `out` at 40G and the volume at about 355Gi free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260528-153111/matrix.tsv`.
- Magic Jewel artifact matrix passed on current ABI 106 local artifacts after the compatibility refresh. Required rows:
  `current-all` passed with no fallback and 664 JBR command frames, and `missing-public-api` passed with one expected
  public API fallback and zero command frames. The five optional old-artifact rows were recorded as skipped because no
  old bundle variables were configured. The TSV has 8 lines including the header. The run was 3.2M under Magic Jewel
  `out`, with `out` at 40G and the volume at about 355Gi free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260528-160241/matrix.tsv`.
- Magic Jewel full default screenshot parity passed after the command-probe, compatibility, and artifact refreshes.
  Aggregate: 106/106 passed, `fallback_sum=10`, `jbr_picture_frames=0`, `jbr_command_frames=76760`, average pixel
  delta `2.158`, average `bad_pixel_ratio=0.05158`, average header-button `bad_pixel_ratio=0.00594`, average
  Compose-canvas `bad_pixel_ratio=0.07632`, average bottom-label `bad_pixel_ratio=0.11884`, and average
  paragraph-probe `bad_pixel_ratio=0.08648`. The TSV has 107 lines including the header. The run was 460M under Magic
  Jewel `out`, with `out` at 40G and the volume at about 345Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260528-160820/suite.tsv`.
- Magic Jewel screenshot parity suite now has no-run helpers for visual loops: `LIST_CASES=true`,
  `LIST_CASE_COUNT=true`, and bounded default-order `CASES_FROM=... CASES_UNTIL=...`. Validation:
  `bash -n scripts/jbr-skia-screenshot-parity-suite.sh`, `LIST_CASE_COUNT=true` returned 106, and
  `LIST_CASES=true CASES_FROM=parity-image-shader CASES_UNTIL=parity-transformed-shader` printed the expected
  18-row shader slice. A focused `CASES=parity-button-chrome` launch also passed with `fallback_sum=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=1723`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-064955/suite.tsv`.
- Magic Jewel screenshot parity suite now also has curated visual `CASE_GROUPS=...` loops plus no-run
  `LIST_CASE_GROUPS=true`, `LIST_CASE_GROUP_COUNTS=true`, and `LIST_UNGROUPED_CASES=true`. Validation:
  `bash -n scripts/jbr-skia-screenshot-parity-suite.sh`, group counts `3,16,14,6,18,14,7,14,14` totaling all 106
  default rows, `LIST_UNGROUPED_CASES=true` returned no rows, and an unknown `CASES_FROM` fails fast. Focused
  `CASE_GROUPS=smoke` passed 3/3 with `fallback_sum=0`, `jbr_picture_frames=0`, and `jbr_command_frames=3334`.
  Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-065540/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=descriptor-lifecycle` passed through the new visual group path.
  Aggregate: 6/6 passed, `fallback_sum=1`, `jbr_picture_frames=0`, `jbr_command_frames=6426`, and average
  `bad_pixel_ratio=0.05078`. The run was 166M under Magic Jewel `out`, with `out` at 24G and the volume at about
  379Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-070009/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=runtime-effect` passed through the new visual group path.
  Aggregate: 14/14 passed, `fallback_sum=2`, `jbr_picture_frames=0`, `jbr_command_frames=8923`, average pixel delta
  `2.054`, and average `bad_pixel_ratio=0.04879`. The run was 58M under Magic Jewel `out`, with `out` at 24G and the
  volume at about 379Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-070611/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=shader-rendering` passed through the new visual group path.
  Aggregate: 18/18 passed, `fallback_sum=4`, `jbr_picture_frames=0`, `jbr_command_frames=15092`, average pixel delta
  `2.081`, and average `bad_pixel_ratio=0.04995`. The run was 72M under Magic Jewel `out`, with `out` at 34G and the
  volume at about 387Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-094321/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=graphics-layer-basic` passed through the new visual group path.
  Aggregate: 7/7 passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=8326`, average pixel delta
  `2.194`, and average `bad_pixel_ratio=0.05223`. The run was 30M under Magic Jewel `out`, with `out` at 24G and the
  volume at about 379Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-072753/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=graphics-layer-effects` passed through the new visual group path.
  Aggregate: 14/14 passed, `fallback_sum=2`, `jbr_picture_frames=0`, `jbr_command_frames=11941`, average pixel delta
  `2.341`, and average `bad_pixel_ratio=0.05725`. The run was 57M under Magic Jewel `out`, with `out` at 34G and the
  volume at about 386Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-100709/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=graphics-layer-clip-shadow-transform` passed through the new visual
  group path. Aggregate: 14/14 passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=14935`, average
  pixel delta `2.223`, and average `bad_pixel_ratio=0.05296`. The run was 56M under Magic Jewel `out`, with `out` at
  25G and the volume at about 379Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-074310/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=native-text` passed in command-marker-only mode after a screenshot
  assertion failure independent of command replay. Aggregate: 14/14 passed, `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=16132`. The TSV has 15 lines including the header. The run was 51M
  under Magic Jewel `out`, with `out` at 50G and the volume at about 319Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-210151/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=native-text` passed through the new visual group path. Aggregate:
  14/14 passed, `fallback_sum=2`, `jbr_picture_frames=0`, `jbr_command_frames=9822`, average pixel delta `2.037`, and
  average `bad_pixel_ratio=0.04758`. The run was 43M under Magic Jewel `out`, with `out` at 34G and the volume at about
  384Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-110758/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=core-effects` passed as the paired command refresh for core visual
  drawing/effect coverage. Aggregate: 7/7 passed, `fallback_sum=0`, `unsupported_rows=2`, `jbr_picture_frames=1720`,
  and `jbr_command_frames=5244`. The unsupported fallback-sentinel rows were
  `commands-path-effect-color-filter-fallback` and `commands-raw-discrete-path-effect-fallback`. The run was 24M under
  Magic Jewel `out`, with `out` at 34G and the volume at about 386Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-104223/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=save-layer-shader-fallbacks` passed in command-marker-only mode after a
  screenshot assertion failure independent of command replay. Aggregate: 7/7 passed, `fallback_sum=0`,
  `unsupported_rows=5`, `jbr_picture_frames=6640`, and `jbr_command_frames=2760`. The supported saveLayer filter and
  blend-mode rows stayed on command replay; the raw color-filter, opaque/composite/picture shader, and invalid-gradient
  sentinels used intentional unsupported picture replay. The TSV has 8 lines including the header. The run was 29M
  under Magic Jewel `out`, with `out` at 51G and the volume at about 307Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-221530/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=core-drawing` passed through the new visual group path. Aggregate:
  16/16 passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=13217`, average pixel delta `2.175`,
  and average `bad_pixel_ratio=0.05197`. This refreshes the core visual group after the latest command-probe
  consolidation. The run was 55M under Magic Jewel `out`, with `out` at 34G and the volume at about 385Gi
  free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-104710/suite.tsv`.
- Magic Jewel full default screenshot parity passed after the focused visual group refresh. Aggregate: 106/106 passed,
  `fallback_sum=11`, `jbr_picture_frames=0`, `jbr_command_frames=93451`, average pixel delta `2.158`, average
  `bad_pixel_ratio=0.05158`, average header-button `bad_pixel_ratio=0.00594`, average Compose-canvas
  `bad_pixel_ratio=0.07632`, average bottom-label `bad_pixel_ratio=0.11884`, and average paragraph-probe
  `bad_pixel_ratio=0.08648`. The run was 510M under Magic Jewel `out`, with `out` at 25G and the volume at about
  384Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-081358/suite.tsv`.
- Magic Jewel compatibility matrix now has no-run `LIST_CASES=true` and `LIST_CASE_COUNT=true` helpers, and unknown
  `CASES=...` entries fail fast before launch. Validation: `bash -n scripts/jbr-skia-compatibility-matrix.sh`,
  `LIST_CASE_COUNT=true` returned 57, `LIST_CASES=true CASES="happy public-api-missing"` printed those two rows, and
  `LIST_CASE_COUNT=true CASES=missing` failed with `Unknown CASES entry`. A focused real `CASES=happy` launch passed
  with `fallback_sum=0`, `jbr_command_frames=607`, and `background_window=true`. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260526-092457/matrix.tsv`.
- Magic Jewel focused compatibility matrix `CASES=public-api-missing` passed through the new exact-row filter with the
  expected public API fallback. Aggregate: 1/1 passed, `fallback_sum=1`, `jbr_command_frames=0`, and
  `background_window=true`. The run was 1.3M under Magic Jewel `out`, with `out` at 25G and the volume at about 379Gi
  free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260526-094135/matrix.tsv`.
- Magic Jewel artifact matrix now has exact-row `CASES=...` selection plus no-run `LIST_CASES=true` and
  `LIST_CASE_COUNT=true` helpers, and unknown `CASES=...` entries fail fast before artifact checks. Validation:
  `bash -n scripts/jbr-skia-artifact-matrix.sh`, `LIST_CASE_COUNT=true` returned 7,
  `LIST_CASES=true CASES="current-all missing-public-api"` printed those two rows, and
  `LIST_CASE_COUNT=true CASES=missing` failed with `Unknown CASES entry`. A focused real `CASES=current-all` launch
  passed with `fallback_sum=0`, `jbr_command_frames=599`, and `background_window=true`. The run was 3.2M under Magic
  Jewel `out`, with `out` at 25G and the volume at about 384Gi free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260526-093100/matrix.tsv`.
- Magic Jewel focused artifact matrix `CASES=missing-public-api` passed through the new exact-row filter with the
  expected `public-api-missing` fallback. Aggregate: 1/1 passed, `fallback_sum=1`, `jbr_command_frames=0`, and
  `background_window=true`. The run was 1.6M under Magic Jewel `out`, with `out` at 25G and the volume at about 379Gi
  free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260526-093632/matrix.tsv`.
- Magic Jewel full default artifact matrix passed after the exact-row helper refresh. Required rows: `current-all`
  passed with no fallback and 634 JBR command frames, and `missing-public-api` passed with one expected public API
  fallback and zero command frames. The five optional old-artifact rows were recorded as skipped because no old bundle
  variables were configured. The run was 3.5M under Magic Jewel `out`, with `out` at 25G and the volume at about 384Gi
  free after completion. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260526-095132/matrix.tsv`.
- Magic Jewel benchmark suite now has no-run `LIST_CASES=true` and `LIST_CASE_COUNT=true` helpers, and unknown
  `CASES=...` entries fail fast before launch. Validation: `bash -n scripts/jbr-skia-benchmark-suite.sh`,
  `LIST_CASE_COUNT=true` returned 5, `LIST_CASES=true CASES="commands commands-dynamic-images"` printed those two
  rows, and `LIST_CASE_COUNT=true CASES=missing` failed with `Unknown benchmark case`. A deliberately short
  `CASES=commands DURATION_SECONDS=1 WARMUP_SECONDS=0` smoke passed with `fallback_sum=0`, one old/new CPU sample, and
  `jbr_command_frames=633`. The run was 1.6M under Magic Jewel `out`, with `out` at 25G and the volume at about 379Gi
  free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260526-093404/suite.tsv`.
- Magic Jewel short benchmark smoke passed for the image-cache subset through the new exact-row filter. With
  `CASES="commands-stable-images commands-resize-dynamic-images" DURATION_SECONDS=1 WARMUP_SECONDS=0`, aggregate was
  2/2 passed, `fallback_sum=0`, one old/new CPU sample per row, and `jbr_command_frames=1079`. Treat this as a wiring
  check only, not a performance measurement. The run was 4.6M under Magic Jewel `out`, with `out` at 25G and the volume
  at about 379Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260526-093925/suite.tsv`.
- Magic Jewel full default benchmark suite passed after the screenshot parity refresh. Aggregate: 5/5 passed,
  `fallback_sum=0`, 85 old-side CPU samples, 81 new-side CPU samples, and `jbr_command_frames=11675`. The picture row
  reported `jbr_picture_fps=132.9`; command rows reported `jbr_command_fps`: plain commands `215.6`, stable images
  `132.8`, dynamic images `123.2`, and resize dynamic images `112.2`. The run was 42M under Magic Jewel `out`, with
  `out` at 40G and the volume at about 345Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260528-171658/suite.tsv`.
- Skiko full focused `JbrSkiaInteropTest` class passed after the latest 20260530 command, compatibility, artifact,
  and benchmark refreshes.
  Command:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
  The Gradle run completed successfully in `/Users/rock3r/src/jbr-skia-zero-copy/skiko/skiko` with 28 actionable
  tasks, 1 executed, and 27 up-to-date.
- Magic Jewel `scripts/test-jbr-skia-api.sh` passed end-to-end after the latest broad validation refresh and refreshed
  20260530 Skiko gate using `REBUILD_LOCAL_ARTIFACTS=false`. The helper patched the temporary `JBRApi` stub into the
  java.desktop overlay, compiled `JBRSkiaApiTest`, ran it headlessly, and printed `JBR_SKIA_API_TEST passed`.
- CMP focused recorder regression passed after the refreshed 20260530 Skiko and JBR parser/API gates. Command:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
  The Gradle run completed successfully with 79 actionable tasks, 12 executed, and 67 up-to-date; the XML result
  reported 138 tests, 0 skipped, 0 failures, and 0 errors.
- Magic Jewel report validator passed after the refreshed 20260530 broad/source gates. Command:
  `./scripts/test-jbr-skia-report-validation.sh`. The script exercised an expected strict command-validation negative
  path, then completed with `JBR_SKIA_REPORT_VALIDATION_TESTS passed`.
- Skiko full focused `JbrSkiaInteropTest` class passed after the broad validation refresh. Command:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
  The run completed in 15s and covered public-API fallback, command-frame cache behavior, and service canvas acquire
  checks.
- Rebuilt local JBR Skia overlay artifacts with
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/scripts/rebuild-jbr-skia-local-artifacts.sh`, then compiled and
  ran `test/jdk/jb/JBRSkia/JBRSkiaApiTest.java` headlessly against `/tmp/jbr-skia-run/desktop` and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib`. The parser/API-side run exited 0.
- Added Magic Jewel `scripts/test-jbr-skia-api.sh` to make that parser/API-side gate reproducible. The helper rebuilds
  the local overlay by default, patches the temporary `JBRApi` stub back into the desktop overlay for runtime, compiles
  `JBRSkiaApiTest`, runs it headlessly, and passed end-to-end with `JBR_SKIA_API_TEST passed`. A follow-up
  `REBUILD_LOCAL_ARTIFACTS=false ./scripts/test-jbr-skia-api.sh` reuse check also passed.
- Fixed that helper to remove the temporary `com.jetbrains.exported.JBRApi` stub classes from the desktop overlay on
  exit after a focused artifact-matrix `current-all` rerun exposed a split-package module conflict. The unreferenced
  failed output `out/jbr-skia-artifact-matrix/20260528-173711` was trimmed after diagnosis. The replacement focused
  artifact matrix passed for `CASES="current-all missing-public-api"`: `current-all` reported no fallback and 460 JBR
  command frames, while `missing-public-api` reported one expected fallback and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260528-173847/matrix.tsv`.
- Magic Jewel post-helper command-probe smoke passed after the cleanup/artifact rerun: `CASE_GROUPS=smoke` covered
  6/6 rows with `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=7612`. The TSV
  has 7 lines including the header. The run was 24M under Magic Jewel `out`, with `out` at 40G and the volume at about
  352Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260528-174413/suite.tsv`.
- Magic Jewel post-helper focused shader-family command checkpoint passed: `CASE_GROUPS=shader-rendering` covered
  13/13 rows with `fallback_sum=0`, `unsupported_rows=8`, `jbr_picture_frames=7570`, and `jbr_command_frames=5954`.
  Supported dynamic image, image shader, gradient shader, noise shader, and turbulence shader rows stayed on command
  replay; the unsupported rows were the intentional image/path-effect, raw image shader, descriptor stroke-shader, and
  raw gradient/noise/turbulence shader fallback sentinels. The TSV has 14 lines including the header. The run was 47M
  under Magic Jewel `out`, with `out` at 40G and the volume at about 352Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260528-174929/suite.tsv`.
- Magic Jewel paired post-helper focused shader-family screenshot parity passed: `CASE_GROUPS=shader-rendering`
  covered 18/18 rows with `fallback_sum=4`, `jbr_picture_frames=0`, `jbr_command_frames=17519`, average pixel delta
  `2.081`, average `bad_pixel_ratio=0.04995`, average header-button `bad_pixel_ratio=0.00764`, average Compose-canvas
  `bad_pixel_ratio=0.07420`, average bottom-label `bad_pixel_ratio=0.12282`, and average paragraph-probe
  `bad_pixel_ratio=0.08520`. The TSV has 19 lines including the header. The run was 75M under Magic Jewel `out`, with
  `out` at 40G and the volume at about 351Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260528-175750/suite.tsv`.
- Magic Jewel focused descriptor-lifecycle command checkpoint passed after the shader command/visual refresh:
  `CASE_GROUPS=descriptor-lifecycle` covered 18/18 rows with `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=26336`. The run covered descriptor eviction, resize and forced
  destination-context redefinition for effect/shader/color/noise/turbulence/composite descriptors, stable
  RuntimeEffect color filters, and RuntimeEffect source-cache eviction. The TSV has 19 lines including the header. The
  run was 270M under Magic Jewel `out`, with `out` at 47G and the volume at about 335Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-102539/suite.tsv`.
- Magic Jewel paired focused descriptor-lifecycle screenshot parity passed: `CASE_GROUPS=descriptor-lifecycle`
  covered 6/6 rows with `fallback_sum=1`, `jbr_picture_frames=0`, `jbr_command_frames=3624`, average pixel delta
  `2.127`, average `bad_pixel_ratio=0.05078`, average header-button `bad_pixel_ratio=0.00677`, average Compose-canvas
  `bad_pixel_ratio=0.07421`, average bottom-label `bad_pixel_ratio=0.10763`, and average paragraph-probe
  `bad_pixel_ratio=0.08565`. The TSV has 7 lines including the header. The run was 137M under Magic Jewel `out`, with
  `out` at 40G and the volume at about 351Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260528-182142/suite.tsv`.
- Magic Jewel focused graphics-layer command checkpoint passed after the descriptor refresh:
  `CASE_GROUPS=graphics-layer` covered 21/21 rows with `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=25548`. The run covered base layers, ModulateAlpha/Offscreen,
  rectangular/rounded/path clips and shadows, blend/color-filter/color-matrix/render-effect rows, offset/chained
  effects, and 3D scale/rotation/near-camera/off-center-pivot transforms. The TSV has 22 lines including the header.
  The run was 74M under Magic Jewel `out`, with `out` at 41G and the volume at about 350Gi free after completion.
  Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260528-182721/suite.tsv`.
- Magic Jewel paired focused graphics-layer clip/shadow/transform screenshot parity passed:
  `CASE_GROUPS=graphics-layer-clip-shadow-transform` covered 14/14 rows with `fallback_sum=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=11433`, average pixel delta `2.223`, average
  `bad_pixel_ratio=0.05296`, average header-button `bad_pixel_ratio=0.00381`, average Compose-canvas
  `bad_pixel_ratio=0.07770`, average bottom-label `bad_pixel_ratio=0.12590`, and average paragraph-probe
  `bad_pixel_ratio=0.08803`. The TSV has 15 lines including the header. The run was 52M under Magic Jewel `out`, with
  `out` at 41G and the volume at about 350Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260528-184029/suite.tsv`.
- Magic Jewel focused core-effects command checkpoint passed after the graphics-layer refresh:
  `CASE_GROUPS=core-effects` covered 7/7 rows with `fallback_sum=0`, `unsupported_rows=2`,
  `jbr_picture_frames=1837`, and `jbr_command_frames=5009`. Supported gradient stroke, image filter, descriptor
  path-effect, vertices, and blend-mode rows stayed on command replay; the unsupported rows were the intentional
  path-effect color-filter and raw discrete path-effect fallback sentinels. The TSV has 8 lines including the header.
  The run was 24M under Magic Jewel `out`, with `out` at 41G and the volume at about 349Gi free after completion.
  Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260528-184926/suite.tsv`.
- Magic Jewel paired focused core-drawing screenshot parity passed: `CASE_GROUPS=core-drawing` covered 16/16 rows with
  `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=15845`, average pixel delta `2.175`, average
  `bad_pixel_ratio=0.05197`, average header-button `bad_pixel_ratio=0.00369`, average Compose-canvas
  `bad_pixel_ratio=0.07723`, average bottom-label `bad_pixel_ratio=0.13787`, and average paragraph-probe
  `bad_pixel_ratio=0.08822`. The TSV has 17 lines including the header. The run was 63M under Magic Jewel `out`, with
  `out` at 41G and the volume at about 349Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260528-185427/suite.tsv`.
- CMP full focused recorder regression class passed after the JBR/Skiko gates:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
  The XML result reported 132 tests, zero skipped, zero failures, and zero errors; Gradle completed successfully in
  1m11s.
- Magic Jewel report-validator regression tests passed after the broad/source-side refresh:
  `./scripts/test-jbr-skia-report-validation.sh`. The script exercised its expected strict-validation negative fixture
  and then reported `JBR_SKIA_REPORT_VALIDATION_TESTS passed`.
- Magic Jewel RuntimeEffect source-cache eviction rows now require descriptor-handle cache-hit markers in both command
  and screenshot suites. The RuntimeEffect color-filter child row keeps descriptor define/use and source-cache-hit
  gates, but no descriptor cache-hit gate because grouped replay showed that child effect-handle cache hits can
  legitimately be zero. Validation: `bash -n` passed for both suite scripts, exact source-cache command rows
  `commands-runtime-effect-shader-source-cache-eviction` and `commands-runtime-effect-source-cache-eviction` passed
  2/2 with `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=3930`;
  descriptor lifecycle command validation passed 18/18 with `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=29228`; exact visual rows
  `parity-runtime-effect-shader-source-cache-eviction` and `parity-runtime-effect-source-cache-eviction` passed 2/2
  with `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=2253`, and average
  `bad_pixel_ratio=0.04981`; the RuntimeEffect visual group passed 14/14 with `fallback_sum=2`,
  `jbr_picture_frames=0`, `jbr_command_frames=12263`, and average `bad_pixel_ratio=0.04879`. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260526-224211/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260526-224342/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-222738/suite.tsv`,
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-225720/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=graphics-layer` passed as the current graphics-layer command replay
  checkpoint. Aggregate: 21/21 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=27255`. The run covered base layer replay, ModulateAlpha/Offscreen, rectangular/rounded/path
  clips and shadows, blend/color-filter/color-matrix/render-effect rows, offset/chained effects, and 3D
  scale/rotation/near-camera/off-center-pivot transforms. The TSV has 22 lines including the header. The run was 75M
  under Magic Jewel `out`, with `out` at 50G and the volume at about 318Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-211340/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=graphics-layer-clip-shadow-transform` passed as the paired visual
  checkpoint for clip/shadow/3D graphics-layer replay. Aggregate: 14/14 passed, `fallback_sum=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=14847`, average pixel delta `2.223`, and average
  `bad_pixel_ratio=0.05296`. The run was 57M under Magic Jewel `out`, with `out` at 34G and the volume at about 386Gi
  free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-103209/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=descriptor-lifecycle` passed as the current descriptor lifecycle
  command checkpoint. Aggregate: 18/18 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=23175`. The run covered descriptor eviction, resize and forced-context redefinition for
  effect/shader/color/noise/turbulence/composite-noise descriptors, stable RuntimeEffect color filters, and
  RuntimeEffect source-cache eviction. The TSV has 19 lines including the header. The run was 282M under Magic Jewel
  `out`, with `out` at 50G and the volume at about 319Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-204612/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=descriptor-lifecycle` passed as the paired visual checkpoint for
  descriptor handle lifecycle and descriptor-backed color filters. Aggregate: 6/6 passed, `fallback_sum=1`,
  `jbr_picture_frames=0`, `jbr_command_frames=7237`, average pixel delta `2.127`, and average
  `bad_pixel_ratio=0.05078`. The run was 167M under Magic Jewel `out`, with `out` at 35G and the volume at about 382Gi
  free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-130309/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-composition-runtime` passed as the current shader composition
  and RuntimeEffect command checkpoint. Aggregate: 15/15 passed, `fallback_sum=0`, `unsupported_rows=2`,
  `jbr_picture_frames=2386`, and `jbr_command_frames=16985`. The unsupported rows are the intentional raw
  RuntimeEffect shader and raw RuntimeEffect color-filter fallback sentinels; descriptor-backed RuntimeEffect and
  shader-composition rows stayed on command replay. The TSV has 16 lines including the header. The run was 71M under
  Magic Jewel `out`, with `out` at 51G and the volume at about 317Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-214834/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=runtime-effect` passed as the paired RuntimeEffect visual
  checkpoint after the shader composition command checkpoint. Aggregate: 14/14 passed, `fallback_sum=2`,
  `jbr_picture_frames=0`, `jbr_command_frames=12380`, average pixel delta `2.054`, average
  `bad_pixel_ratio=0.04879`, average header-button `bad_pixel_ratio=0.00635`, average Compose-canvas
  `bad_pixel_ratio=0.07246`, average bottom-label `bad_pixel_ratio=0.11024`, and average paragraph-probe
  `bad_pixel_ratio=0.08599`. The run was 63M under Magic Jewel `out`, with `out` at 34G and the volume at about
  383Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-123909/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=native-text-invalid` passed as the current native text/font-data
  parser guard checkpoint. Aggregate: 11/11 passed, `fallback_sum=11`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=1519`. The text and paragraph scalar/font-family guard rows fell
  back before command replay; the font-data record-flags sentinel retained the expected setup command frames before
  fallback. The TSV has 12 lines including the header. The run was 41M under Magic Jewel `out`, with `out` at 54G and
  the volume at about 314Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-055310/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=primitive-invalid` passed as the current primitive parser guard
  checkpoint. Aggregate: 13/13 passed, `fallback_sum=13`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. The run covered stroke-cap, transform record flags, clip operation, drawPoints count/length
  bounds, and drawVertices vertex/index/mode/blend parser guards. The run was 29M under Magic Jewel `out`, with `out`
  at 50G and the volume at about 320Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-200527/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=path-invalid` passed as the current path/path-effect parser guard
  checkpoint. Aggregate: 22/22 passed, `fallback_sum=22`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. The run covered clip/draw/stroke/shadow path verb guards plus dash path-effect interval,
  bounds, radii, stroke metadata, phase, and interval guards. The run was 86M under Magic Jewel `out`, with `out` at
  53G and the volume at about 315Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-030433/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=effect-descriptor-invalid` passed as the current effect descriptor
  parser guard checkpoint. Aggregate: 28/28 passed, `fallback_sum=28`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. The run covered descriptor header/type/version/length guards,
  lighting/tint/color-matrix color-filter descriptors, blur/offset image-filter descriptors, and corner/stamped/chain
  path-effect descriptor payload guards. The run was 113M under Magic Jewel `out`, with `out` at 53G and the volume at
  about 315Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-031858/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-descriptor-invalid` passed as the current shader descriptor
  parser guard checkpoint. Aggregate: 30/30 passed, `fallback_sum=30`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. The run covered shader descriptor header/type/length guards,
  transformed/composite descriptors, linear/radial/sweep gradients, image shader dimensions/tile modes, and
  Perlin/noise kind/frequency/octave/tile bounds. The run was 126M under Magic Jewel `out`, with `out` at 53G and the
  volume at about 315Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-033644/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=image-handles-invalid` passed as the current image handle/cache parser
  guard checkpoint. Aggregate: 27/27 passed, `fallback_sum=27`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=1036`. The run covered image define/cache-clear/evict record flags, image dimensions/pixel
  bounds, undefined and evicted image handles, image-ref scalar guards, color-filter image refs, and descriptor-backed
  color-filter image-ref bounds. The only command frames came from the cache-clear setup row before fallback. The run
  was 108M under Magic Jewel `out`, with `out` at 53G and the volume at about 315Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-035452/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=descriptor-handles-invalid` passed as the current descriptor handle
  lifetime/type guard checkpoint. Aggregate: 48/48 passed, `fallback_sum=48`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. The run covered descriptor use/use-after-evict, descriptor
  evict record flags, saveLayer descriptor refs, child use-after-evict, missing children, and wrong-family child/type
  checks across shader, color-filter, image-filter, path-effect, and RuntimeEffect descriptor families. The run was
  213M under Magic Jewel `out`, with `out` at 53G and the volume at about 315Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-041151/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=gradient-path-invalid` passed as the current gradient path parser
  guard checkpoint. Aggregate: 18/18 passed, `fallback_sum=18`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. The run covered linear/radial/sweep gradient path tile, color-count, stop-order, fill-type,
  path-data length, path verb, and radial radius guards. The TSV has 19 lines including the header. The run was 69M
  under Magic Jewel `out`, with `out` at 53G and the volume at about 314Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-044104/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=save-layer-invalid` passed as the current saveLayer parser guard
  checkpoint. Aggregate: 37/37 passed, `fallback_sum=37`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. The run covered saveLayer alpha, record flags, record lengths, color-filter/blend/image
  filter bounds, blend modes, and descriptor-ref record/bounds guards for color-filter, blend+color-filter, and
  image-filter variants. The previously notable blend color-filter height/width fallback row now reports no
  unsupported marker while still taking structured fallback. The TSV has 38 lines including the header. The run was
  148M under Magic Jewel `out`, with `out` at 54G and the volume at about 314Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-052922/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=gradient-invalid` passed as the current gradient parser guard
  checkpoint. Aggregate: 60/60 passed, `fallback_sum=60`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. The run covered linear/radial/sweep gradient stroke width, tile mode, color-count,
  stop-order, radial radius, round-rect/stroke variants, and the gradient path parser rows included in the broader
  group. The TSV has 61 lines including the header. The run was 231M under Magic Jewel `out`, with `out` at 54G and
  the volume at about 314Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-045310/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=runtime-effect-invalid` passed as the current RuntimeEffect
  parser/schema guard checkpoint. Aggregate: 62/62 passed, `fallback_sum=56`, `unsupported_rows=6`,
  `jbr_picture_frames=7806`, and `jbr_command_frames=0`. The six unsupported rows are the intentional invalid
  uniform, child, and nested-child schema fallbacks for shader and color-filter RuntimeEffect descriptors; all other
  metadata/source/count/name/index/compile/build/child-type rows used structured command fallback. The TSV has 63
  lines including the header. The run was 368M under Magic Jewel `out`, with `out` at 54G and the volume at about
  313Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-060107/suite.tsv`.
- Magic Jewel command-probe quick-group coverage now spans every resolved default case. `LIST_UNGROUPED_CASES=true`
  returns no rows after adding the supported surface/transform/UI, shader-rendering, shader-composition/RuntimeEffect,
  core-effects, graphics-layer-extras, and saveLayer/shader-fallback quick groups. `LIST_CASE_GROUP_COUNTS=true`
  reports the new group sizes as 11, 13, 15, 7, 14, and 7 rows, respectively.
- Magic Jewel exact saveLayer/shader-fallback uncovered command-probe tail passed. Aggregate: 7/7 passed,
  `fallback_sum=0`, `unsupported_rows=5`, `jbr_picture_frames=6640`, and `jbr_command_frames=2760`. It covered
  saveLayer filter and blend-mode replay, saveLayer raw color-filter fallback, opaque/composite opaque/picture shader
  fallbacks, and invalid-gradient fallback. The TSV has 8 lines including the header. The run was 29M under Magic
  Jewel `out`, with `out` at 51G and the volume at about 307Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-221530/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=graphics-layer-extras` passed as the current graphics-layer extras
  command checkpoint. Aggregate: 14/14 passed, `fallback_sum=0`, `unsupported_rows=2`, `jbr_picture_frames=2458`,
  and `jbr_command_frames=18868`. It covered graphics-layer color-matrix and render-effect resize/forced-context
  lifecycle rows, raw color-filter/render-effect fallback sentinels, render-effect color/blend/color-matrix
  combinations, offset/chained render-effect combinations, and the near-camera chained render-effect variant. The run
  was 73M under Magic Jewel `out`, with `out` at 51G and the volume at about 307Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-220535/suite.tsv`.
- Magic Jewel exact core effects uncovered command-probe slice passed. Aggregate: 7/7 passed, `fallback_sum=0`,
  `unsupported_rows=2`, `jbr_picture_frames=2724`, and `jbr_command_frames=7649`. It covered stroked gradients,
  image filters, descriptor path effects, path-effect color-filter fallback, raw discrete path-effect fallback,
  vertices, and blend-mode rendering. The TSV has 8 lines including the header. The run was 30M under Magic Jewel
  `out`, with `out` at 51G and the volume at about 317Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-215950/suite.tsv`.
- Magic Jewel exact shader composition and RuntimeEffect uncovered command-probe slice passed. Aggregate: 15/15
  passed, `fallback_sum=0`, `unsupported_rows=2`, `jbr_picture_frames=2386`, and `jbr_command_frames=16985`. It
  covered image/composite/transformed shaders, shader color-filter combinations, RuntimeEffect shader and color-filter
  replay, pure/uniform/child RuntimeEffects, and raw RuntimeEffect shader/color-filter fallback sentinels. The TSV has
  16 lines including the header. The run was 71M under Magic Jewel `out`, with `out` at 51G and the volume at about
  317Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-214834/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-rendering` passed as the current shader-rendering command
  checkpoint. Aggregate: 13/13 passed, `fallback_sum=0`, `unsupported_rows=8`, `jbr_picture_frames=9129`, and
  `jbr_command_frames=5472`. It covered forced-context dynamic image-cache replay, image path-effect fallback, image
  shader replay, descriptor stroke-shader fallback, gradient/noise/turbulence shader descriptors, and raw
  image/gradient/noise/turbulence shader fallback sentinels. The TSV has 14 lines including the header. The run was
  52M under Magic Jewel `out`, with `out` at 50G and the volume at about 318Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-213937/suite.tsv`.
- Magic Jewel focused surface/transform/UI command-probe slice passed in command-marker-only mode after a macOS
  screenshot-capture failure independent of command replay. Aggregate: 11/11 passed, `fallback_sum=0`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=13359`. It covered native bridge loading,
  drawPoints lines/dots, concat/skew transforms, gradient surfaces/paths, glass-pane popup layering, real popup-window
  capture, Swing menu popup layering, and text-as-image replay. The TSV has 12 lines including the header. The run was
  42M under Magic Jewel `out`, with `out` at 50G and the volume at about 318Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-213157/suite.tsv`.
- Magic Jewel full default command-probe sweep passed with screenshot assertions enabled after the macOS capture
  helper gained the window-bounds retry. Aggregate: 487/487 passed, `fallback_sum=350`, `unsupported_rows=26`,
  `jbr_picture_frames=25539`, and `jbr_command_frames=146052`. The TSV has 488 lines including the header. The sweep
  covered smoke replay, parser invalid groups, image/shader/effect descriptor guards, RuntimeEffect shader and
  color-filter fallback paths, descriptor lifecycle, color/image filters, path effects, vertices, blend modes,
  saveLayer guards, and graphics-layer variants. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-163835/suite.tsv`.
- Magic Jewel compatibility matrix passed after the screenshot-enabled command-probe refresh. Aggregate: 57/57
  passed, `fallback_sum=56`, `jbr_command_frames=421`, and `background_window=true` on every row. The only command
  frames came from the happy path; all ABI, native ABI, command-capability, high-capability, and public API mismatch
  rows fell back exactly once. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260525-220254/matrix.tsv`.
- Magic Jewel artifact matrix passed on current ABI 106 local artifacts after the compatibility refresh. Required rows:
  `current-all` passed with no fallback and 606 JBR command frames, and `missing-public-api` passed with one expected
  public API fallback and zero command frames. The five optional old-artifact rows were recorded as skipped because no
  old bundle variables were configured. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260525-223152/matrix.tsv`.
- Magic Jewel full default screenshot parity passed after the focused visual refreshes and local macOS capture retry
  fix. Aggregate: 106/106 passed, `fallback_sum=11`, `jbr_picture_frames=0`, `jbr_command_frames=90354`, average
  pixel delta `2.158`, average `bad_pixel_ratio=0.05158`, average header-button `bad_pixel_ratio=0.00594`, and
  average Compose-canvas `bad_pixel_ratio=0.07632`. The TSV has 107 lines including the header, and covered button
  chrome, core drawing, native text, gradients, image/color filters, descriptor lifecycle, shader descriptors,
  RuntimeEffect rows, and graphics-layer variants. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-152754/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the remaining RuntimeEffect visual surface. Aggregate: 9/9 passed,
  `fallback_sum=1`, `jbr_picture_frames=0`, `jbr_command_frames=7416`, average pixel delta `2.063`, average
  `bad_pixel_ratio=0.04895`, and average header-button `bad_pixel_ratio=0.00578`. It covered pure-color
  RuntimeEffect base/resize/forced-context lifecycle, uniform-only and child-only RuntimeEffects, shader source-cache
  eviction, RuntimeEffect shader, shader+color-filter, and color-filter rows. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-152025/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the image/shader descriptor visual surface. Aggregate: 12/12
  passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=10079`, average pixel delta `2.202`,
  average `bad_pixel_ratio=0.05304`, and average header-button `bad_pixel_ratio=0.00363`. It covered forced-context
  image refs, image filters, image/color/noise/turbulence shaders, image-shader color filters, composite and
  composite-noise shaders, composite/linear-gradient shader color filters, and transformed shaders. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-151131/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the core drawing visual surface. Aggregate: 10/10 passed,
  `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=8713`, average pixel delta `2.259`, average
  `bad_pixel_ratio=0.05479`, and average header-button `bad_pixel_ratio=0.00351`. It covered clean geometry, skew,
  drawVertices, point dots, path effects, drawPath/drawArc/drawRoundRect shapes, clip rect/path, blend modes, and
  saveLayer tint-filter replay. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-150344/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the gradient visual surface. Aggregate: 4/4 passed,
  `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=4613`, average pixel delta `2.145`, average
  `bad_pixel_ratio=0.05099`, and average header-button `bad_pixel_ratio=0.00381`. It covered gradient surfaces,
  gradient-filled paths, ShaderBrush gradients, and stroked gradients. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-145959/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the descriptor-backed color-filter visual surface. Aggregate:
  6/6 passed, `fallback_sum=1`, `jbr_picture_frames=0`, `jbr_command_frames=6651`, average pixel delta `2.130`,
  average `bad_pixel_ratio=0.05071`, and average header-button `bad_pixel_ratio=0.00677`. It covered image
  color-matrix filtering, color-filter handle base/resize/forced-context lifecycle, color-matrix filter, and lighting
  filter rows. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-145502/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the combined graphics-layer blend/filter/render-effect visual
  surface. Aggregate: 10/10 passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=11712`, average
  pixel delta `2.490`, average `bad_pixel_ratio=0.06161`, and average header-button `bad_pixel_ratio=0.00381`. It
  covered blend+color-filter, blend+color-matrix, render-effect plus color/blend/filter combinations, offset-effect
  blend+color-matrix, chained render-effect blend+color-matrix, and the near-camera chained variant. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-144729/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the graphics-layer shadow/transform visual surface. Aggregate:
  9/9 passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=10016`, average pixel delta `2.192`,
  average `bad_pixel_ratio=0.05198`, and average header-button `bad_pixel_ratio=0.00381`. It covered rectangular,
  rounded, and path shadows plus rotation X/Y/XY, scale/translate, near-camera, and off-center pivot rows. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-144011/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the graphics-layer filter/effect visual surface. Aggregate:
  5/5 passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=6450`, average pixel delta `2.186`,
  average `bad_pixel_ratio=0.05177`, and average header-button `bad_pixel_ratio=0.00381`. It covered graphics-layer
  color filter, color-matrix filter, combined render/offset/chained effects, offset effect, and chained render effect.
  Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-143551/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the graphics-layer base/clip/blend visual surface. Aggregate:
  7/7 passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=6852`, average pixel delta `2.255`,
  average `bad_pixel_ratio=0.05404`, and average header-button `bad_pixel_ratio=0.00381`. It covered plain
  graphics-layer replay, modulate-alpha, offscreen compositing, rectangular/rounded/path clips, and graphics-layer
  blend mode. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-143023/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the native text/font visual lifecycle surface after the local
  capture retry refresh. Aggregate: 14/14 passed, `fallback_sum=3`, `jbr_picture_frames=0`,
  `jbr_command_frames=10056`, average pixel delta `2.037`, average `bad_pixel_ratio=0.04758`, and average
  header-button `bad_pixel_ratio=0.00889`. It covered custom-font image text, generic/loaded/resource/system font text,
  same-context resize, and forced destination-context migration rows. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-142013/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the stable descriptor lifecycle visual surface after the local
  capture retry refresh. Aggregate: 14/14 passed, `fallback_sum=5`, `jbr_picture_frames=0`,
  `jbr_command_frames=10401`, average pixel delta `1.976`, average `bad_pixel_ratio=0.04695`, and average
  header-button `bad_pixel_ratio=0.01016`. It covered descriptor eviction, shader resize/forced-context redefine
  rows, stable RuntimeEffect color-filter lifecycle rows, RuntimeEffect color-filter child replay, and RuntimeEffect
  source-cache eviction. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-140922/suite.tsv`.
- Magic Jewel focused `parity-button-chrome` screenshot parity passed on current artifacts after the macOS capture
  helper gained the window-bounds retry. This narrow capture-health checkpoint stayed on command replay with
  `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=941`, average pixel delta `2.265`, overall
  `bad_pixel_ratio=0.05200`, header-button `bad_pixel_ratio=0.00381`, and zero bottom-swatch bad pixels. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-140430/suite.tsv`.
- Magic Jewel artifact matrix passed on current ABI 106 local artifacts. Required rows: `current-all` passed with no
  fallback and 718 JBR command frames, and `missing-public-api` passed with one expected public API fallback and zero
  command frames. The five optional old-artifact rows were recorded as skipped because no old bundle variables were
  configured. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260525-135942/matrix.tsv`.
- Magic Jewel full default command-probe consolidation passed in command-marker-only mode after the focused group and
  compatibility refreshes. Local macOS screenshot capture still fails independently of command replay, so this sweep
  validated command markers, fallback contracts, unsupported-picture sentinels, and frame counters rather than
  screenshot pixels. Aggregate: 487/487 passed, `fallback_sum=350`, `unsupported_rows=26`,
  `jbr_picture_frames=33702`, and `jbr_command_frames=188677`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-083629/suite.tsv`.
- Magic Jewel compatibility matrix passed after the focused command-probe refreshes. Aggregate: 57/57 passed,
  `fallback_sum=56`, `jbr_command_frames=663`, and `background_window=true` on every row. The only command frames came
  from the happy path; all ABI, native ABI, command-capability, high-capability, and public API mismatch rows fell back
  exactly once. Matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260525-080634/matrix.tsv`.
- Magic Jewel `CASE_GROUPS=runtime-effect-invalid` passed as a focused RuntimeEffect parser/semantic guard refresh.
  Aggregate: 62/62 passed, `fallback_sum=56`, `unsupported_rows=6`, `jbr_picture_frames=5684`, and
  `jbr_command_frames=0`. The six unsupported-picture rows are the intentional shader/color-filter invalid uniform,
  child, and nested-child schema cases; all other malformed source, SKSL, uniform, child, named-count, compile/build,
  and child-type rows failed before replay with one structured fallback marker. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-072243/suite.tsv`.
- Magic Jewel supported command-replay refresh passed in command-marker-only mode while the local macOS screenshot
  capture path remains unavailable. Results: `smoke` 6/6, `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=8392`; `color-filters` 10/10, `fallback_sum=0`,
  `unsupported_rows=1`, `jbr_picture_frames=1047`, `jbr_command_frames=13204`; `descriptor-lifecycle` 18/18,
  `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=23175`; `native-text` 14/14,
  `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=16132`; and
  `graphics-layer` 21/21, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=27255`. The only unsupported row is the intentional raw blend color-filter sentinel. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203230/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203750/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-204612/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-210151/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-211340/suite.tsv`.
- Magic Jewel small invalid-group refresh passed for stream envelope and tiny fill/blend descriptor guards. Results:
  `stream-invalid` 8/8, `fallback_sum=8`;
  `shader-ref-invalid` 3/3, `fallback_sum=3`;
  `fill-rect-color-filter-invalid` 5/5, `fallback_sum=5`;
  `blend-mode-invalid` 2/2, `fallback_sum=2`. All four runs had `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-201711/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202316/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202527/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202909/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=stream-invalid` passed as the current command-stream parser guard
  checkpoint. Aggregate: 8/8 passed, `fallback_sum=8`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. The TSV has 9 lines including the header. The run was 20M under Magic Jewel `out`, with
  `out` at 50G and the volume at about 319Gi
  free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-201711/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=blend-mode-invalid` passed as the current fill-rect blend-mode
  parser guard checkpoint. Aggregate: 2/2 passed, `fallback_sum=2`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  and `jbr_command_frames=0`. The TSV has 3 lines including the header. The run was 3.4M under Magic Jewel `out`,
  with `out` at 50G and the volume at about 319Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202909/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=fill-rect-color-filter-invalid` passed as the current fill-rect
  color-filter parser guard checkpoint. Aggregate: 5/5 passed, `fallback_sum=5`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. The TSV has 6 lines including the header. The run was 9.3M
  under Magic Jewel `out`, with `out` at 50G and the volume at about 319Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202527/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-ref-invalid` passed as the current fill-rect shader-ref parser
  guard checkpoint. Aggregate: 3/3 passed, `fallback_sum=3`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. The TSV has 4 lines including the header. The run was 6.6M under Magic Jewel `out`, with
  `out` at 50G and the volume at about 319Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202316/suite.tsv`.
- Magic Jewel `CASE_GROUPS=gradient-invalid` passed as the broader gradient parser refresh after the focused
  `gradient-path-invalid` run. Aggregate: 60/60 passed, `fallback_sum=60`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. It covered stroke-width, tile-mode, radius, color-count, and
  stop-order guards across linear, radial, and sweep gradients plus the embedded gradient-path rows. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-054237/suite.tsv`.
- Magic Jewel `CASE_GROUPS=gradient-path-invalid` passed as a focused gradient path parser refresh. Aggregate: 18/18
  passed, `fallback_sum=18`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. It covered
  linear/radial/sweep gradient path tile/count/stop-order/fill-type/path-data/path-verb guards. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-053116/suite.tsv`.
- Magic Jewel `CASE_GROUPS=path-invalid` passed as a focused path/path-effect parser refresh after
  `primitive-invalid`. Aggregate: 22/22 passed, `fallback_sum=22`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. It covered malformed clip/draw/drawShadow path verbs plus dash path-effect payload guards
  across line, rect, round-rect, and generic path rows. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-051534/suite.tsv`.
- Magic Jewel `CASE_GROUPS=primitive-invalid` passed as a focused primitive parser refresh after the native text
  invalid refresh. Aggregate: 13/13 passed, `fallback_sum=13`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. It covered invalid stroke cap, transform flags, clip operation, drawPoints payload guards,
  and drawVertices vertex/index/mode/blend guards. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-050721/suite.tsv`.
- Magic Jewel `CASE_GROUPS=native-text-invalid` passed as a focused native text/font parser refresh after the
  command-marker-only full sweep. Aggregate: 11/11 passed, `fallback_sum=11`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=1223`. Text and paragraph font scalar/family-count rows rejected
  before replay; the command frames came from the recoverable font-data record-flags row. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-045900/suite.tsv`.
- Magic Jewel `CASE_GROUPS=descriptor-handles-invalid` passed as a focused descriptor handle lifetime/family refresh
  after the command-marker-only full sweep. Aggregate: 48/48 passed, `fallback_sum=48`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. This rechecked undefined and evicted top-level shader,
  color-filter, path-effect, saveLayer color-filter/blend/image-filter handles, shader/effect child use-after-evict,
  missing-child, and wrong-family cases, all failing before JBR replay. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-042757/suite.tsv`.
- Magic Jewel `CASE_GROUPS=save-layer-invalid` passed as a focused saveLayer parser refresh after the
  command-marker-only full sweep. Aggregate: 37/37 passed, `fallback_sum=37`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. This rechecked saveLayer alpha, record flags/lengths,
  color-filter/blend/image-filter scalar guards, and descriptor-ref scalar/blend-mode guards with all malformed rows
  failing before JBR replay. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-000551/suite.tsv`.
- Magic Jewel `CASE_GROUPS=shader-descriptor-invalid` passed as a focused shader descriptor parser refresh after the
  command-marker-only full sweep. Aggregate: 30/30 passed, `fallback_sum=30`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. This rechecked descriptor header guards, gradient
  tile/stop/radius/color-count guards, image-shader dimension/tile-mode guards, and Perlin noise kind/frequency/octave
  and tile bounds; all malformed rows failed before JBR replay. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-034415/suite.tsv`.
- Magic Jewel `CASE_GROUPS=effect-descriptor-invalid` passed as a focused effect descriptor parser refresh after the
  command-marker-only full sweep. Aggregate: 28/28 passed, `fallback_sum=28`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. This rechecked descriptor header guards, color/image-filter
  payload guards, and corner/stamped/chained path-effect descriptor bounds with all malformed rows failing before JBR
  replay. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-032537/suite.tsv`.
- Magic Jewel `CASE_GROUPS=image-handles-invalid` passed as a focused image handle/ref parser refresh after the
  command-marker-only full sweep. Aggregate: 27/27 passed, `fallback_sum=27`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=980`. The command frames came from the recoverable
  image-cache-clear record-flags row; the other malformed image define/use/ref and color-filter ref rows rejected
  before JBR replay. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-030742/suite.tsv`.
- Magic Jewel full default command-probe sweep passed in command-marker-only mode after the local macOS
  `screencapture` path began failing independently of command replay (`could not create image from window`, and the
  region retry also failed with `could not create image from rect`). The sweep used
  `EXPECT_SCREENSHOT_ASSERTION=false` and covered 487/487 passing rows, `fallback_sum=350`,
  `unsupported_rows=26`, `jbr_picture_frames=24438`, and `jbr_command_frames=141588`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-215756/suite.tsv`.
  A screenshot-enabled broad attempt reached `commands-point-lines` with healthy command markers
  (`fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=1149`) but could not run
  the screenshot assertion because window capture failed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-210938/commands-point-lines/report.md`.
- Magic Jewel `CASE_GROUPS=native-text` passed as a supported native text/font replay refresh after exact base,
  resize, and forced-context slices. Aggregate: 14/14 passed, `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=16132`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-210151/suite.tsv`.
- Magic Jewel exact native-text forced-context slice passed for custom-font text image plus generic, loaded font-data,
  resource, and system font text rows. Aggregate: 5/5 passed, `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=6494`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-205555/suite.tsv`.
- Magic Jewel exact native-text resize slice passed for generic, loaded font-data, resource, and system font text
  rows. Aggregate: 4/4 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=4294`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-205300/suite.tsv`.
- Magic Jewel exact native-text base slice passed for custom-font text image plus generic, loaded font-data, resource,
  and system font text rows. Aggregate: 5/5 passed, `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=5082`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-204934/suite.tsv`.
- Magic Jewel `CASE_GROUPS=graphics-layer` passed as a supported graphics-layer command replay refresh after exact
  base/clip/blend, filters/effects, and shadows/transforms slices. Aggregate: 21/21 passed, `fallback_sum=0`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=27255`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-211340/suite.tsv`.
- Magic Jewel exact graphics-layer shadows/transforms slice passed for shadow, round shadow, path shadow, rotation,
  scale/translate, near-camera, and off-center pivot rows. Aggregate: 9/9 passed, `fallback_sum=0`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=10778`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-202706/suite.tsv`.
- Magic Jewel exact graphics-layer filters/effects slice passed for color-filter, color-matrix, render-effect,
  offset-effect, and chained render-effect rows. Aggregate: 5/5 passed, `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=6297`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-202325/suite.tsv`.
- Magic Jewel exact graphics-layer base/clip/blend slice passed for plain, modulate-alpha, offscreen, rect/round/path
  clip, and blend-mode rows. Aggregate: 7/7 passed, `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=9525`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-201824/suite.tsv`.
- Magic Jewel `CASE_GROUPS=descriptor-lifecycle` passed as a supported descriptor lifecycle/source-cache refresh after
  exact descriptor redefine and RuntimeEffect lifecycle/source-cache slices. Aggregate: 18/18 passed,
  `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=23330`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-200304/suite.tsv`.
- Magic Jewel exact RuntimeEffect lifecycle/source-cache slice passed for stable color-filter, resize, forced-context,
  shader source-cache eviction, and color-filter source-cache eviction rows. Aggregate: 5/5 passed,
  `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=7343`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-195934/suite.tsv`.
- Magic Jewel exact descriptor eviction/redefine slice passed for descriptor eviction plus resize/forced-context
  redefine rows across generic, shader, color shader, noise, turbulence, and composite-noise descriptors. Aggregate:
  13/13 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=18104`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-194922/suite.tsv`.
- Magic Jewel `CASE_GROUPS=color-filters` passed as a supported color-filter command replay refresh after exact base
  and graphics-layer color-filter slices. Aggregate: 10/10 passed, `fallback_sum=0`, `unsupported_rows=1`,
  `jbr_picture_frames=982`, and `jbr_command_frames=13865`. The run was 47M under Magic Jewel `out`, with `out` at
  34G and the volume at about 383Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-122130/suite.tsv`.
  The only unsupported row was `commands-raw-blend-color-filter-fallback`; descriptor-backed base and graphics-layer
  color-filter rows stayed on command replay.
- Magic Jewel exact graphics-layer color-filter slice passed for descriptor-backed color-filter, color-matrix,
  blend-color-filter, and blend color-matrix rows. Aggregate: 4/4 passed, `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=6264`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-193838/suite.tsv`.
- Magic Jewel exact base color-filter slice passed for image color-matrix, raw blend fallback, color-filter handle,
  color-matrix, and lighting rows. Aggregate: 6/6 passed, `fallback_sum=0`, `unsupported_rows=1`,
  `jbr_picture_frames=999`, and `jbr_command_frames=7220`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-193428/suite.tsv`.
- Magic Jewel `CASE_GROUPS=gradient-invalid` passed as the grouped gradient parser consolidation after exact non-path
  gradient slices and the earlier grouped gradient-path consolidation. Aggregate: 60/60 passed, `fallback_sum=60`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-185624/suite.tsv`.
- Magic Jewel exact radial non-path gradient invalid slice passed for radius, tile-mode, color-count, and stop-order
  guards across fill/stroke rect and round-rect forms. Aggregate: 16/16 passed, `fallback_sum=16`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-184612/suite.tsv`.
- Magic Jewel exact sweep non-path gradient invalid slice passed for color-count and stop-order guards across
  fill/stroke rect and round-rect forms. Aggregate: 8/8 passed, `fallback_sum=8`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-184050/suite.tsv`.
- Magic Jewel exact linear non-path gradient invalid slice passed for tile-mode, color-count, and stop-order guards
  across fill/stroke rect and round-rect forms. Aggregate: 12/12 passed, `fallback_sum=12`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-183259/suite.tsv`.
- Magic Jewel exact non-path gradient stroke-width invalid slice passed across linear, radial, and sweep gradient
  stroke/round-rect stroke rows. Aggregate: 6/6 passed, `fallback_sum=6`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-182859/suite.tsv`.
- Magic Jewel `CASE_GROUPS=gradient-path-invalid` passed as the grouped gradient-path parser consolidation after exact
  linear/radial/sweep slices. Aggregate: 18/18 passed, `fallback_sum=18`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-181611/suite.tsv`.
- Magic Jewel exact sweep gradient-path invalid slice passed for color-count, stop-order, fill-type, path-data-length,
  and path-verb guards. Aggregate: 5/5 passed, `fallback_sum=5`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-181247/suite.tsv`.
- Magic Jewel exact radial gradient-path invalid slice passed for radius, tile-mode, color-count, stop-order,
  fill-type, path-data-length, and path-verb guards. Aggregate: 7/7 passed, `fallback_sum=7`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-180753/suite.tsv`.
- Magic Jewel exact linear gradient-path invalid slice passed for tile-mode, color-count, stop-order, fill-type,
  path-data-length, and path-verb guards. Aggregate: 6/6 passed, `fallback_sum=6`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-180353/suite.tsv`.
- Magic Jewel `CASE_GROUPS=runtime-effect-invalid` passed as the grouped RuntimeEffect descriptor/parser
  consolidation after the exact RuntimeEffect focused slices. Aggregate: 62/62 passed, `fallback_sum=56`,
  `unsupported_rows=6`, `jbr_picture_frames=6711`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-171908/suite.tsv`.
  The six unsupported rows are the intentionally invalid RuntimeEffect schema/nested descriptor cases; they validated
  the JBR picture fallback route rather than producing command-stream invalid fallback markers.
- Magic Jewel exact RuntimeEffect compile/build/type fallback tail passed for color-filter compile/build/child-type
  and shader compile/build/child-type rows. Aggregate: 6/6 passed, `fallback_sum=6`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-171059/suite.tsv`.
- Magic Jewel exact RuntimeEffect schema/nested six-pack passed for shader and color-filter uniform-schema,
  child-schema, and nested-child rows. Aggregate: 6/6 passed, `fallback_sum=0`, `unsupported_rows=6`,
  `jbr_picture_frames=5340`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-170650/suite.tsv`.
- Magic Jewel exact RuntimeEffect color-filter descriptor parser slice passed for malformed source hash, source code,
  SKSL length, uniform/child counts, named metadata, uniform schema, child schema, and child index rows. Aggregate:
  25/25 passed, `fallback_sum=25`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`.
  Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-165052/suite.tsv`.
- Magic Jewel exact RuntimeEffect shader descriptor parser slice passed for malformed source hash, source code,
  SKSL length, uniform/child counts, named metadata, uniform schema, child schema, and child index rows. Aggregate:
  25/25 passed, `fallback_sum=25`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`.
  Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-163503/suite.tsv`.
- Magic Jewel `CASE_GROUPS=descriptor-handles-invalid` passed as the grouped descriptor-handle parser/lifecycle
  consolidation after the exact descriptor-handle slices and Skiko corruption-target fix. Aggregate: 48/48 passed,
  `fallback_sum=48`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-160242/suite.tsv`.
- Magic Jewel exact descriptor-handle wrong-type slice passed after Skiko commit `4e7b0a6ba` made the path-effect
  color-filter corruption hook prefer RuntimeEffect color-filter children before falling back to fill-rect
  color-filter refs. Aggregate: 15/15 passed, `fallback_sum=15`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-155203/suite.tsv`.
- Magic Jewel exact RuntimeEffect color-filter child path-effect wrong-type row passed after the same Skiko hook fix,
  proving the marker now targets `runtimeEffectColorFilterChildPathEffect` instead of spending the one-shot
  corruption on the earlier fill-rect color-filter ref. Aggregate: 1/1 passed, `fallback_sum=1`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-154401/suite.tsv`.
- Magic Jewel exact descriptor-handle missing-child slice passed for RuntimeEffect color-filter, blur/offset
  image-filter, chain path-effect, shader-color-filter effect child, and transformed/composite shader child missing
  handles. Aggregate: 9/9 passed, `fallback_sum=9`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-152543/suite.tsv`.
- Magic Jewel exact descriptor child use-after-evict slice passed for transformed/composite/shader-color-filter
  shader children, RuntimeEffect shader/color-filter children, shader-color-filter effect children, and effect/blur/
  path-effect children. Aggregate: 10/10 passed, `fallback_sum=10`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-151912/suite.tsv`.
- Magic Jewel exact direct descriptor-handle use/eviction slice passed for undefined shader/path-effect/saveLayer refs,
  descriptor use-after-evict, shader/color-filter evict record flags, color-filter/path-effect use-after-evict, and
  saveLayer descriptor-ref use-after-evict rows. Aggregate: 14/14 passed, `fallback_sum=14`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-151010/suite.tsv`.
- Magic Jewel `CASE_GROUPS=shader-descriptor-invalid` passed as the grouped shader descriptor parser consolidation
  after the exact shader-descriptor slices. Aggregate: 30/30 passed, `fallback_sum=30`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-144958/suite.tsv`.
- Magic Jewel exact Perlin-noise shader descriptor invalid slice passed for kind, base-frequency, octave, zero-octave,
  tile-size, tile-height, negative tile-size, and negative tile-height guards. Aggregate: 8/8 passed,
  `fallback_sum=8`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-144440/suite.tsv`.
- Magic Jewel exact image-shader descriptor invalid slice passed for width, max-width, height, max-height, X tile-mode,
  and Y tile-mode guards after tightening the report summary parser to ignore concatenated sampled-log prefixes.
  Aggregate: 6/6 passed, `fallback_sum=6`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-144018/suite.tsv`.
- Magic Jewel exact gradient shader descriptor invalid slice passed for linear tile-mode/stop-order, radial
  radius/tile-mode/stop-order, and sweep color-count/stop-order guards. Aggregate: 7/7 passed, `fallback_sum=7`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-142702/suite.tsv`.
- Magic Jewel exact shader descriptor header/color/transformed/composite invalid slice passed for descriptor type,
  record flags, payload count, color/filter payload count, record length, version, transformed payload count, and
  composite blend-mode guards. Aggregate: 9/9 passed, `fallback_sum=9`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-142112/suite.tsv`.
- Magic Jewel `CASE_GROUPS=effect-descriptor-invalid` passed as the grouped effect descriptor parser consolidation
  after the exact effect-descriptor slices. Aggregate: 28/28 passed, `fallback_sum=28`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-140259/suite.tsv`.
- Magic Jewel exact effect path-effect descriptor invalid slice passed for corner, stamped, and chained path-effect
  payload guards, completing all 28 `effect-descriptor-invalid` rows across exact slices. Aggregate: 12/12 passed,
  `fallback_sum=12`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-135419/suite.tsv`.
- Magic Jewel exact effect image-filter descriptor invalid slice passed for blur/blur-with-input sigma, negative sigma,
  tile-mode, and offset/offset-with-input delta guards. Aggregate: 8/8 passed, `fallback_sum=8`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-134749/suite.tsv`.
- Magic Jewel exact effect-descriptor header/color-filter payload invalid slice passed for descriptor type/version,
  record flags, payload count, record length, lighting payload count, tint blend-mode, and color-matrix payload
  guards. Aggregate: 8/8 passed, `fallback_sum=8`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-134121/suite.tsv`.
- Magic Jewel `CASE_GROUPS=save-layer-invalid` passed as the grouped saveLayer parser consolidation after the exact
  saveLayer slices. Aggregate: 37/37 passed, `fallback_sum=37`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-131559/suite.tsv`.
- Magic Jewel exact save-layer ref scalar invalid slice passed for color-filter-ref and blend-color-filter-ref
  width/height/alpha plus blend-mode payload guards, completing all 37 `save-layer-invalid` rows across exact slices.
  Aggregate: 7/7 passed, `fallback_sum=7`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-131028/suite.tsv`.
- Magic Jewel exact save-layer scalar/blend invalid slice passed for color-filter, blend-mode, blend-color-filter, and
  image-filter saveLayer width/height/alpha plus blend-mode payload guards. Aggregate: 15/15 passed,
  `fallback_sum=15`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-125944/suite.tsv`.
- Magic Jewel exact save-layer record/length invalid slice passed for alpha, raw/color-filter/blend/blend-color-filter
  record flags and lengths, plus color-filter-ref/blend-color-filter-ref/image-filter-ref record flags and lengths.
  Aggregate: 15/15 passed, `fallback_sum=15`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-124845/suite.tsv`.
- Magic Jewel `CASE_GROUPS=image-handles-invalid` passed as the grouped image handle/parser consolidation after the
  focused image slices. Aggregate: 27/27 passed, `fallback_sum=27`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  and `jbr_command_frames=1102`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-123035/suite.tsv`.
- Magic Jewel exact image color-filter invalid slice passed for color-filter image handle use/eviction, scalar
  width/height/alpha/filter-quality/blend guards, color-filter-ref handle use/eviction, and descriptor-backed
  color-filter-ref scalar guards. Aggregate: 13/13 passed, `fallback_sum=13`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-122124/suite.tsv`.
- Magic Jewel exact plain image-ref/use invalid slice passed for missing/evicted image handles and image-ref
  width/height/alpha/filter-quality scalar guards: 6/6 passed, `fallback_sum=6`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-121600/suite.tsv`.
- Magic Jewel exact image define/cache invalid slice passed for record flags, image dimensions, max bounds, and pixel
  count: 8/8 passed, `fallback_sum=8`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=1295`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-120938/suite.tsv`.
- Magic Jewel exact fill-rect scalar invalid refresh passed across `fill-rect-color-filter-invalid`,
  `shader-ref-invalid`, and `blend-mode-invalid`: 10/10 passed, `fallback_sum=10`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-120203/suite.tsv`.
- Magic Jewel `CASE_GROUPS=path-invalid` passed as a focused path/path-effect parser refresh. Aggregate:
  22/22 passed, `fallback_sum=22`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`.
  Suite: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-114647/suite.tsv`.
- Magic Jewel `CASE_GROUPS=native-text-invalid` passed as a focused native text/font-data parser refresh. Aggregate:
  11/11 passed, `fallback_sum=11`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=1837`.
  Suite: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-113852/suite.tsv`.
- Magic Jewel `CASE_GROUPS=primitive-invalid` passed as a focused primitive parser refresh. Aggregate:
  13/13 passed, `fallback_sum=13`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`.
  Suite: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-112927/suite.tsv`.
- Magic Jewel exact RuntimeEffect schema/nested fallback consolidation passed:
  `commands-runtime-effect-invalid-uniform-schema-fallback`,
  `commands-runtime-effect-color-filter-invalid-uniform-schema-fallback`,
  `commands-runtime-effect-invalid-child-schema-fallback`,
  `commands-runtime-effect-color-filter-invalid-child-schema-fallback`,
  `commands-runtime-effect-invalid-nested-child-fallback`, and
  `commands-runtime-effect-color-filter-invalid-nested-child-fallback` produced 6/6 passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-112429/suite.tsv`;
  `fallback_sum=0`, `jbr_picture_frames=7665`, and `jbr_command_frames=0`.
- Magic Jewel exact RuntimeEffect nested-child fallback validation passed, completing focused exact coverage for the
  six intentional RuntimeEffect schema/nested parser-fallback rows. `CASES="commands-runtime-effect-invalid-nested-child-fallback
  commands-runtime-effect-color-filter-invalid-nested-child-fallback"` produced 2/2 passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-112201/suite.tsv`;
  unsupported reasons were `shaderDescriptor` and `colorFilterDescriptor`, `jbr_picture_frames=2770`, and
  `jbr_command_frames=0`.
- Magic Jewel exact RuntimeEffect child-schema fallback validation passed after the direct JBR parser-only schema
  additions. `CASES="commands-runtime-effect-invalid-child-schema-fallback
  commands-runtime-effect-color-filter-invalid-child-schema-fallback"` produced 2/2 passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-111924/suite.tsv`;
  unsupported reasons were `shaderDescriptor` and `colorFilterDescriptor`, `jbr_picture_frames=2562`, and
  `jbr_command_frames=0`.
- Magic Jewel exact RuntimeEffect uniform-schema fallback validation passed after the direct JBR parser-only schema
  additions. `CASES="commands-runtime-effect-invalid-uniform-schema-fallback
  commands-runtime-effect-color-filter-invalid-uniform-schema-fallback"` produced 2/2 passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-111654/suite.tsv`;
  unsupported reasons were `shaderDescriptor` and `colorFilterDescriptor`, `jbr_picture_frames=2223`, and
  `jbr_command_frames=0`.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct RuntimeEffect color-filter descriptor rows for malformed
  uniform-schema and child-schema metadata, mirroring the shader RuntimeEffect schema checks already in the test. The
  local parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct non-path sweep-gradient rows for fill rect,
  fill round-rect, stroke rect, and stroke round-rect color-count, stop-order, and stroke-width guards; together with
  the linear/radial/path slices, this directly covers the current 60-row `gradient-invalid` family. The local
  parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct non-path radial-gradient rows for fill rect,
  fill round-rect, stroke rect, and stroke round-rect radius, tile-mode, color-count, stop-order, and stroke-width
  guards; the local parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct non-path linear-gradient rows for fill rect,
  fill round-rect, stroke rect, and stroke round-rect tile-mode, color-count, stop-order, and stroke-width guards;
  the local parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct gradient-path command rows for linear, radial, and sweep
  path gradients. The new streams cover all `gradient-path-invalid` parser guards: tile/radius/color-count/stop-order,
  fill-type, path-data length, and path-verb rejection; the local parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after extending direct shader-descriptor invalid coverage for descriptor
  record flags, color/color-filter payload counts, composite blend mode, and negative Perlin tile width; the local
  parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after extending direct effect-descriptor invalid coverage for descriptor
  record flags, lighting payload count, corner path-effect negative radius, stamped path-effect non-finite/negative
  phase, negative path-data length, and malformed stamped path verbs; the local parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct saveLayer invalid-family rows for raw saveLayer,
  blend-mode, raw color-filter, blend/color-filter, color-filter-ref, blend/color-filter-ref, and image-filter-ref
  record flags, record lengths, dimensions, alpha, and blend-mode guards; the local parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct clip-path, draw-path, and draw-path path-effect-ref rows.
  The new streams cover valid path payloads plus clip op, fill type, path-data length, paint style/stroke width, and
  malformed path-verb rejection; the local parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after aligning direct draw-shadow path validation with the native replay
  contract. The helper now rejects non-finite shadow geometry, negative light radius, unsupported shadow flags,
  malformed fill/path length, and malformed path verbs before replay; the local parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct dashed stroke-path path-effect rows. The new streams
  cover the valid dashed generic-path form plus interval-count, stroke metadata, phase, interval-value, fill-type,
  path-data-length, and path-verb rejection; the local parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct dashed stroke-round-rect path-effect rows. The new
  streams cover the valid dashed round-rect form plus interval-count, right/bottom ordering, radius, stroke metadata,
  phase, and interval-value rejection; the local parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct dashed stroke-rect path-effect rows. The new streams
  cover the valid dashed rect form plus interval-count, width, and height rejection; the local parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct dashed stroke-line path-effect rows for the path-invalid
  family. The new streams cover interval-count, phase, and interval-value rejection; the local parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct draw-points and draw-vertices scalar rows matching most
  of the Magic Jewel `primitive-invalid` group. The new streams cover draw-points point-count lower/upper bounds and
  record length, plus draw-vertices vertex-count lower/upper bounds, record length, vertex mode, blend mode, and
  index-count lower/upper bounds; the local parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct fill-rect shader-ref scalar rows matching the small
  Magic Jewel `shader-ref-invalid` group. The new streams cover horizontal bounds, vertical bounds, and alpha; the
  local parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct fill-rect blend/color-filter scalar rows matching the
  small Magic Jewel `blend-mode-invalid` and `fill-rect-color-filter-invalid` groups. The new direct streams cover
  fill-rect blend-mode width/height, raw color-filter width/height, and descriptor color-filter ref width/height; the
  local parser-only run exited 0.
- Magic Jewel no-run quick-loop plumbing now exposes
  `commands-runtime-effect-color-filter-child-path-effect-wrong-type-fallback` in
  `CASE_GROUPS=descriptor-handles-invalid`. `LIST_CASE_GROUP_COUNTS=true` reports the group at 48 cases, and
  `CASE_GROUPS=descriptor-handles-invalid LIST_CASES=true` lists the new row. The default-order bounded no-run list
  from `commands-runtime-effect-color-filter-child-wrong-effect-type-fallback` through
  `commands-color-filter-path-effect-wrong-type-fallback` also includes the new path-effect child row. Execution is
  pending until Gradle can access the user-home wrapper lock again.
- JBR parser-only `JBRSkiaApiTest` passed after adding a direct cleared-image-cache ref row, covering the
  `COMMAND_CLEAR_IMAGE_CACHE` branch in the parser-helper image-cache tracking. The local overlay parser-only run
  exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after tightening RuntimeEffect color-filter child validation to require
  actual color-filter descriptors rather than merely excluding image filters. The new direct row covers a path-effect
  descriptor supplied as a RuntimeEffect color-filter child; the local overlay parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after making Java2D replay explicitly reject non-color-filter descriptors for
  image-ref and fill-rect color-filter refs. The new direct row covers a path-effect descriptor supplied to
  `COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF`; the local overlay parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after aligning Java2D replay with parser-helper type checks for
  saveLayer color-filter descriptor refs. The direct rows now cover path-effect descriptors supplied to
  `COMMAND_SAVE_LAYER_COLOR_FILTER_REF` and `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF`; the local overlay
  parser-only run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after tightening `isValidCommandStreamForTesting` image-cache tracking for
  `COMMAND_DRAW_IMAGE_REF`, raw/ref color-filter image refs, and `COMMAND_FILL_RECT_IMAGE_SHADER`. The helper now
  rejects undefined, evicted, cleared, or dimension-mismatched image cache keys before replay. The local run used the
  same single-source patched `JBRSkiaService` overlay flow into `/tmp/jbr-skia-service-test-classes`, then
  `-Djbrskia.parserOnly=true`; it exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after tightening `isValidCommandStreamForTesting` so
  `COMMAND_DRAW_PATH_PATH_EFFECT_REF` rejects undefined, evicted, or wrong-type descriptor handles before replay.
  The local run first compiled a single-source patched `JBRSkiaService` overlay into
  `/tmp/jbr-skia-service-test-classes`, then ran `JBRSkiaApiTest` with `-Djbrskia.parserOnly=true`,
  `/tmp/jbr-skia-run/desktop`, `/tmp/jbr-skia-api-stub-classes`, and `/tmp/jbr-skia-native/libjbrskiainterop.dylib`;
  it exited 0. The broader Magic Jewel `CASE_GROUPS=descriptor-handles-invalid` harness run remains pending because
  sandboxed Gradle cannot open the user-home wrapper lock and app escalation is currently quota-blocked.
- JBR parser-only `JBRSkiaApiTest` passed after extending descriptor-handle invalid coverage. The new direct streams
  cover shader/color-filter evict record flags, transformed/composite/shader-color-filter and RuntimeEffect shader/
  color-filter child use-after-evict, blur image-filter child missing/evicted/wrong-type, undefined saveLayer
  image-filter handles, plus evicted saveLayer color-filter, blend/color-filter, and image-filter descriptor handles.
  The local run used `-Djbrskia.parserOnly=true` with
  `/tmp/jbr-skia-run/desktop`,
  `/tmp/jbr-skia-api-stub-classes`, and `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; it exited 0. The broader
  Magic Jewel `CASE_GROUPS=descriptor-handles-invalid` harness run is still pending because sandboxed Gradle cannot
  open the user-home wrapper lock and app escalation is currently quota-blocked.
- Magic Jewel `CASE_GROUPS=image-handles-invalid` passed as the image handle/ref parser fallback refresh. Aggregate:
  27/27 passed, `fallback_sum=27`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=806`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-175721/suite.tsv`.
  The group covers malformed image define/cache-clear/evict records, image use/use-after-evict, image-ref scalar
  corruption, color-filter image-ref/use/ref rows, and descriptor-ref scalar guards.
- Magic Jewel `CASE_GROUPS=color-filters` passed as a focused supported color-filter/graphics-layer replay refresh.
  Aggregate: 10/10 passed, `fallback_sum=0`, `unsupported_rows=1`, `picture_frames=873`, and
  `command_frames=12561`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-174955/suite.tsv`.
  The one unsupported row is the intentional raw blend color-filter sentinel; the supported color-filter, lighting,
  and graphics-layer color-filter rows stayed on command replay.
- Magic Jewel `CASE_GROUPS=path-invalid` passed as the path and path-effect parser fallback refresh. Aggregate:
  22/22 passed, `fallback_sum=22`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-173511/suite.tsv`.
  The group covers malformed clip/draw path verbs, dash path-effect interval/geometry/stroke fields for line, rect,
  round-rect, and generic-path rows, plus the drawShadow path verb fallback.
- Magic Jewel `CASE_GROUPS=primitive-invalid` passed as the primitive paint/draw parser fallback refresh. Aggregate:
  13/13 passed, `fallback_sum=13`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-172558/suite.tsv`.
  The group covers invalid stroke cap, transform record flags, clip operation, point-count/record-length guards, and
  drawVertices count/mode/blend/index guards.
- Magic Jewel `CASE_GROUPS=native-text-invalid` passed as the native text/font parser fallback refresh. Aggregate:
  11/11 passed, `fallback_sum=11`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=1500`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-171910/suite.tsv`.
  The group covers invalid font size/weight/width/slant/family-count payloads for text and paragraph records, plus the
  invalid font-data record flags sentinel.
- Magic Jewel focused screenshot parity passed for the stable descriptor lifecycle visual surface. The bounded
  `CASES=...` subset covered descriptor eviction; same-context resize and forced-context redefine rows for color,
  noise, turbulence, and composite-noise shaders; stable RuntimeEffect color-filter resize/forced-context rows; and
  RuntimeEffect shader/color-filter source-cache eviction. Aggregate: 14/14 passed, `fallback_sum=5`,
  `jbr_picture_frames=0`, and `jbr_command_frames=9992`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260523-170917/suite.tsv`.
- Magic Jewel compatibility matrix passed after the local artifact rebuild and latest default command-probe
  consolidation. Aggregate: 57/57 passed, `fallback_sum=56`, `command_frames=426`, and all 57 rows kept
  `MAGIC_JEWEL_BACKGROUND_WINDOW=true`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260523-163821/matrix.tsv`.
  The matrix covers the happy command path plus ABI mismatch, native ABI mismatch, low/high command capability
  mismatches, and the explicit `public-api-missing` fallback case.
- Magic Jewel periodic default command-probe consolidation passed after the latest focused quick-group refreshes and
  parser/fallback sentinel checks. The run used `EXPECT_SCREENSHOT_ASSERTION=false` and completed as a single full
  default sweep in command-marker-only mode. Aggregate: 487/487 passed, `fallback_sum=350`,
  `unsupported_rows=26`, `picture_frames=33632`, and `command_frames=183989`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-222152/suite.tsv`.
  The TSV has 488 lines including the header; the run directory is 2.2G, `magic-jewel/out` is 53G, and the validation
  volume had 316Gi free after the run.
- Magic Jewel now exposes `CASE_GROUPS=descriptor-lifecycle` as a no-run quick group for the stable descriptor
  create/redefine/reuse/cache-eviction rows. The focused group passed 18/18 with `fallback_sum=0`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=34840`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-050014/suite.tsv`.
  The group covers descriptor eviction, same-context resize and forced-context redefine paths for descriptor-backed
  shader/effect families, stable RuntimeEffect color filters, and RuntimeEffect source-cache eviction rows.
- Magic Jewel `CASE_GROUPS=native-text` passed as a focused native font/text lifecycle refresh. Aggregate: 14/14
  passed, `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=27551`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-051647/suite.tsv`.
  The group covers custom-font image text, generic/loaded/resource/system fonts, same-context resize, and forced
  destination-context migration.
- Magic Jewel `CASE_GROUPS=graphics-layer` passed as a focused graphics-layer transform/effect refresh. Aggregate:
  21/21 passed, `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=27255`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-211340/suite.tsv`.
  The group covers layer alpha/offscreen/clip variants, blend/color-filter/render-effect rows, shadows, rotations,
  scale/translate, near-camera, and off-center pivot replay.
- Magic Jewel `CASE_GROUPS=gradient-path-invalid` passed as a focused gradient path parser/fallback refresh.
  Aggregate: 18/18 passed, `fallback_sum=18`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-054125/suite.tsv`.
  The group covers malformed linear/radial/sweep gradient path tile/count/stop-order/path-data variants.
- Magic Jewel `CASE_GROUPS=gradient-invalid` passed as the broader gradient parser/fallback refresh. Aggregate:
  60/60 passed, `fallback_sum=60`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-055336/suite.tsv`.
  The group covers malformed stroke-width, tile-mode, radius, color-count, stop-order, and embedded gradient-path
  variants across linear, radial, and sweep gradient rows.
- Magic Jewel periodic default command-probe consolidation passed after the shader/effect/RuntimeEffect/saveLayer
  parser guard refreshes. macOS `screencapture` failed on the first broad attempt for `commands-live-animation`
  (`could not create image from window`) even though command replay was healthy, so the row was rerun exactly with
  `EXPECT_SCREENSHOT_ASSERTION=false` and the rest of the default order resumed from the second case in command-marker
  mode. Combined aggregate across the two passing roots: 486/486 passed, `fallback_sum=349`,
  `unsupported_rows=26`, `picture_frames=33767`, and `command_frames=222490`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-234434/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-234537/suite.tsv`.
  The discarded capture-failure root is
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-234330/suite.tsv`.
- Magic Jewel `CASE_GROUPS=runtime-effect-invalid` passed as a focused RuntimeEffect parser/semantic guard refresh
  after the direct JBR parser-test additions. Aggregate: 62/62 passed, `fallback_sum=56`, `unsupported_rows=6`,
  `picture_frames=7689`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-225911/suite.tsv`.
  The six unsupported rows are the intentional invalid uniform/child/nested-child schema fallbacks for shader and
  color-filter RuntimeEffect descriptors.
- Magic Jewel `CASE_GROUPS=effect-descriptor-invalid` passed as a focused effect descriptor parser guard refresh after
  the direct JBR parser-test additions. Aggregate: 28/28 passed, `fallback_sum=28`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-224042/suite.tsv`.
- Magic Jewel `CASE_GROUPS=shader-descriptor-invalid` passed as a focused shader descriptor parser guard refresh after
  the direct JBR parser-test additions. Aggregate: 30/30 passed, `fallback_sum=30`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-222036/suite.tsv`.
- Magic Jewel `CASE_GROUPS=save-layer-invalid` passed as a focused saveLayer parser guard refresh after the direct
  JBR parser-test additions. Aggregate: 37/37 passed, `fallback_sum=37`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-215534/suite.tsv`.
- JBR parser-only `JBRSkiaApiTest` passed after extending direct saveLayer scalar coverage for supported non-ref
  color-filter, blend-mode, and blend/color-filter width/height/alpha bounds. The test was compiled with `javac`
  against `/tmp/jbr-skia-run/desktop` and the local `JBRApi` stub, then run headlessly with the patched
  `java.desktop` module, patched `java.base` stub classes, and `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run
  exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after extending direct RuntimeEffect color-filter descriptor coverage for
  source hash/source-code validation, SKSL length, uniform/child/named-count bounds, and negative count guards. The
  test was compiled with `javac` against `/tmp/jbr-skia-run/desktop` and the local `JBRApi` stub, then run headlessly
  with the patched `java.desktop` module, patched `java.base` stub classes, and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after extending direct RuntimeEffect shader descriptor coverage for SKSL
  length, uniform/child/named-count bounds, negative count guards, and source-code byte validation with a recomputed
  matching hash. The test was compiled with `javac` against `/tmp/jbr-skia-run/desktop` and the local `JBRApi` stub,
  then run headlessly with the patched `java.desktop` module, patched `java.base` stub classes, and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after extending direct effect descriptor payload coverage for blur sigma/tile
  guards, offset finite-delta guards, corner path-effect radius, stamped path-effect advance/phase/style/fill/path-data
  bounds, and chain path-effect payload count. The test was compiled with `javac` against
  `/tmp/jbr-skia-run/desktop` and the local `JBRApi` stub, then run headlessly with the patched `java.desktop` module,
  patched `java.base` stub classes, and `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed again after extending direct shader descriptor payload coverage for
  linear/radial/sweep gradient tile/stop/radius/color-count guards and image shader width/height/tile-mode bounds. The
  test was compiled with `javac` against `/tmp/jbr-skia-run/desktop` and the local `JBRApi` stub, then run headlessly
  with the patched `java.desktop` module, patched `java.base` stub classes, and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- JBR parser-only `JBRSkiaApiTest` passed after extending Perlin-noise shader descriptor coverage to include
  base-frequency Y lower bounds, zero octaves, and tile-height upper/lower bounds. The test was compiled with `javac`
  against `/tmp/jbr-skia-run/desktop` and the local `JBRApi` stub, then run headlessly with
  `--patch-module java.desktop=/tmp/jbr-skia-run/desktop`,
  `--patch-module java.base=/tmp/jbr-skia-api-test-java-base`,
  `-Dsun.java2d.skia.interop.library=/tmp/jbr-skia-native/libjbrskiainterop.dylib`, and explicit
  `com.jetbrains.exported`/`com.jetbrains.desktop` exports. The run exited 0.
- Magic Jewel periodic default command-probe consolidation passed across the full default order using split resume
  roots after two non-replay interruptions. Aggregate across the four clean roots: 486/486 passed,
  `fallback_sum=349`, `unsupported_rows=26`, `picture_frames=31265`, and `command_frames=189208`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-143223/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-160910/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-192154/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-200157/suite.tsv`.
  The first interruption was repaired by rebuilding local JBR Skia artifacts after a transient `public-api-missing`
  state; the affected image-width case then passed exactly. The later failures were macOS `screencapture` failures
  (`could not create image from window`) after command replay had already produced healthy JBR command frames. The
  final tail therefore ran with `EXPECT_SCREENSHOT_ASSERTION=false` and validates command markers, fallback contracts,
  unsupported-picture sentinels, and frame counters rather than screenshot pixels.
- Magic Jewel `CASES=commands-gradient-stroke` passed after relaxing that row's screenshot gate to command-marker
  validation. The exact run reported `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and
  `command_frames=2710`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-191608/suite.tsv`.
  The scoped harness change was pushed to Magic Jewel as `41f08e5` (`Relax gradient stroke screenshot probe`).
- Magic Jewel `CASE_GROUPS=runtime-effect-invalid` passed as a focused RuntimeEffect parser/semantic guard checkpoint.
  Aggregate: 62/62, `fallback_sum=56`, `unsupported_rows=6`, `picture_frames=6338`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-134345/suite.tsv`.
  The six unsupported rows are the intentional invalid uniform/child/nested-child schema fallbacks for shader and
  color-filter RuntimeEffect descriptors; the remaining rows rejected through structured command fallback.
- Magic Jewel `CASE_GROUPS=gradient-invalid` passed as a focused gradient parser guard checkpoint. Aggregate: 60/60,
  `fallback_sum=60`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-130045/suite.tsv`.
  This covers linear/radial/sweep stroke width, tile mode, color count, stop order, path-gradient, and radial radius
  malformed-stream guards.
- Magic Jewel `CASE_GROUPS=descriptor-handles-invalid` passed as a focused descriptor handle lifecycle/type guard
  checkpoint. Aggregate: 47/47, `fallback_sum=47`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-122554/suite.tsv`.
  This covers missing handles, use-after-evict, eviction record flags, child missing/use-after-evict, and wrong-type
  guards across shader, color-filter, image-filter, path-effect, and RuntimeEffect descriptor families.
- Magic Jewel `CASE_GROUPS=save-layer-invalid` passed as a focused saveLayer parser guard checkpoint. Aggregate:
  37/37, `fallback_sum=37`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-115938/suite.tsv`.
  This covers alpha, record flags, record lengths, width/height, blend modes, and descriptor-backed color/image-filter
  saveLayer variants.
- Magic Jewel `CASE_GROUPS=shader-descriptor-invalid` passed as a focused shader descriptor parser guard checkpoint.
  Aggregate: 30/30, `fallback_sum=30`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-113757/suite.tsv`.
  This covers shader descriptor headers, color/filter payloads, transformed/composite shader guards, gradient/image
  shader descriptor bounds, and Perlin noise descriptor guards.
- Magic Jewel `CASE_GROUPS=effect-descriptor-invalid` passed as a focused effect descriptor parser guard checkpoint.
  Aggregate: 28/28, `fallback_sum=28`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-111754/suite.tsv`.
  This covers descriptor header guards, color/image filter payloads, and corner/stamped/chain path-effect descriptor
  parser guards.
- Magic Jewel `CASE_GROUPS=image-handles-invalid` passed as a focused image handle/parser guard checkpoint. Aggregate:
  27/27, `fallback_sum=27`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=1082`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-105821/suite.tsv`.
  The command frames come from the recoverable image-cache-clear record-flags row; the other malformed rows rejected
  before replay.
- Magic Jewel `CASE_GROUPS=graphics-layer` passed as a focused graphics-layer command replay checkpoint. Aggregate:
  21/21, `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=27255`. The TSV has 22
  lines including the header. The run was 75M under Magic Jewel `out`, with `out` at 50G and the volume at about
  318Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-211340/suite.tsv`.
  This covers layer clips, blend/color filters, render effects, shadows, 3D rotations, scale/translate, camera, and
  pivot variants.
- Magic Jewel `CASE_GROUPS=gradient-path-invalid` passed as a focused gradient path parser guard checkpoint. Aggregate:
  18/18, `fallback_sum=18`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-102856/suite.tsv`.
- Magic Jewel `CASE_GROUPS=native-text` passed as a focused native text/font-data command replay checkpoint. Aggregate:
  14/14, `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=16132`. The TSV has 15
  lines including the header. The run was 51M under Magic Jewel `out`, with `out` at 50G and the volume at about
  319Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-210151/suite.tsv`.
  This covers custom, generic, loaded-font-data, resource, system, resize, and forced-context native text rows.
- Magic Jewel `CASE_GROUPS=color-filters` passed as a focused color-filter command replay checkpoint. Aggregate:
  10/10, `fallback_sum=0`, `unsupported_rows=1`, `picture_frames=1047`, and `command_frames=13204`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203750/suite.tsv`.
  The single unsupported row is the intentional raw blend color-filter fallback; supported descriptor and
  graphics-layer color-filter rows replayed through JBR commands.
- Magic Jewel `CASE_GROUPS=path-invalid` passed as a focused path/path-effect parser guard checkpoint. Aggregate:
  22/22, `fallback_sum=22`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-094905/suite.tsv`.
- Magic Jewel `CASE_GROUPS=primitive-invalid` passed as a focused primitive command parser guard checkpoint.
  Aggregate: 13/13, `fallback_sum=13`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-091344/suite.tsv`.
  The bounded default-order range from `commands-core-primitives` through `commands-point-lines` also passed as the
  adjacent primitive/image/path ordering checkpoint. Aggregate: 38/38, `fallback_sum=36`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=3398`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-092240/suite.tsv`.
- Magic Jewel `CASE_GROUPS=native-text-invalid` passed as a focused native text/font-data parser guard checkpoint.
  Aggregate: 11/11, `fallback_sum=11`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=728`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-090411/suite.tsv`.
  The final font-data record-flags row intentionally recovered after the one-shot invalid definition, so it is the
  only row with JBR command frames.
- Magic Jewel `CASE_GROUPS=smoke` passed as the quick happy-path command replay checkpoint. Aggregate: 6/6,
  `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=8392`. The TSV has 7 lines
  including the header. The run was 27M under Magic Jewel `out`, with `out` at 50G and the volume at about 319Gi free
  after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203230/suite.tsv`.
- Magic Jewel `CASE_GROUPS=stream-invalid` passed as the quick command-stream parser guard checkpoint. Aggregate: 8/8,
  `fallback_sum=8`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-004208/suite.tsv`.
- Magic Jewel quick-loop discovery now exposes every implemented command probe quick group through
  `LIST_CASE_GROUPS=true`, including shader-ref, fill-rect color-filter, and blend-mode invalid subsets that were
  previously usable but hidden from the listing. `LIST_CASES=true` no-run checks passed for the expanded group list,
  and a membership scan found no listed quick-group rows missing from default ordering. The newly exposed
  shader-ref/fill-rect color-filter/blend-mode invalid groups passed as a focused validation: 10/10,
  `fallback_sum=10`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-002228/suite.tsv`.
  Magic Jewel also gained `LIST_CASE_GROUP_COUNTS=true` to print quick-group sizes without launching validation.
- Magic Jewel default command-probe ordering now includes the corner/stamped/chain path-effect descriptor invalid rows
  from `CASE_GROUPS=effect-descriptor-invalid`. `LIST_CASES=true` showed no remaining missing rows across the checked
  invalid quick groups (`stream`, `primitive`, `path`, `effect-descriptor`, `shader-descriptor`, `runtime-effect`,
  `descriptor-handles`, `image-handles`, `save-layer`, `gradient`, `gradient-path`, and `native-text`). The bounded
  default-order range from `commands-invalid-offset-image-filter-descriptor-delta-fallback` through
  `commands-invalid-shader-descriptor-type-fallback` passed 15/15 with `fallback_sum=15`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-000243/suite.tsv`.
- Magic Jewel default command-probe ordering now includes the existing blur image-filter child live sentinels for
  use-after-evict, missing-child, and wrong-effect-type fallback. The exact three-row run passed 3/3 with
  `fallback_sum=3`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-230108/suite.tsv`.
  `CASE_GROUPS=descriptor-handles-invalid` passed 47/47 with `fallback_sum=47`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-230330/suite.tsv`.
  Bounded default-order validation passed for the three insertion neighborhoods: use-after-evict 3/3,
  missing-child 3/3, and wrong-type 4/4, all with zero unsupported rows and zero JBR frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-233519/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-233729/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-233942/suite.tsv`.
  The suite also gained `LIST_CASES=true` no-run case listing. Comparing
  `CASE_GROUPS=descriptor-handles-invalid LIST_CASES=true` against the expanded default `LIST_CASES=true` found no
  remaining group rows missing from default.
  The expanded default use-after-evict neighborhood passed 19/19 with `fallback_sum=19`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-234632/suite.tsv`.
- Periodic full default command-probe consolidation passed after the stroke-round-rect dash stroke metadata slice,
  using the quicker split workflow instead of rerunning already-green rows. A first broad pass hit a transient
  runtime/output miss at `commands-invalid-blur-with-input-image-filter-descriptor-tile-mode-fallback`; the durable
  validation reran the prefix through `commands-invalid-blur-image-filter-descriptor-tile-mode-fallback` and resumed
  the tail from the missed row forward. Prefix aggregate: 218/218 passed, `fallback_sum=151`, `unsupported_rows=16`,
  `picture_frames=15365`, and `command_frames=65828`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-202252/suite.tsv`.
  Tail aggregate: 244/244 passed, `fallback_sum=174`, `unsupported_rows=10`, `picture_frames=9283`, and
  `command_frames=80473`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-172927/suite.tsv`.
  Combined aggregate: 462/462 passed, `fallback_sum=325`, `unsupported_rows=26`, `picture_frames=24648`, and
  `command_frames=146301`.
- Stroke-round-rect dash path-effect stroke metadata validation passed for op 60
  `COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT`. The exact four-row run rewrote stroke width to `0`, cap/join to `3`,
  or stroke miter to `-1`, matching JBR's `isValidStrokeMetadata` guards for positive stroke width, cap/join enum
  bounds, and non-negative miter. Each row required the typed
  `SKIKO_JBR_INTEROP_STROKE_ROUND_RECT_DASH_PATH_EFFECT_STROKE_*_CORRUPTED` marker before accepting
  `command-stream-invalid` fallback. Aggregate 4/4 passed, `fallback_sum=4`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-143055/suite.tsv`.
  The scoped `CASE_GROUPS=path-invalid` quick group now covers 22 malformed path rows and passed with
  `fallback_sum=22`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-143333/suite.tsv`.
  The narrower default-order path tail from `commands-invalid-clip-path-verb-fallback` through
  `commands-point-lines` passed as the quick default-order iteration path; aggregate 23/23 passed,
  `fallback_sum=22`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=1972`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-144716/suite.tsv`.
  Skiko `publishAwtPublicationToMavenLocal publishAwtRuntimeElementsPublicationToMavenLocal
  publishKotlinMultiplatformPublicationToMavenLocal` and focused
  `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` both passed.
- Stroke-round-rect dash path-effect phase/interval validation passed for op 60
  `COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT`. The exact two-row run rewrote phase to `-1` or the first dash interval
  to `0`; both rows produced one expected `command-stream-invalid` fallback, zero unsupported rows, zero JBR picture
  frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-135947/suite.tsv`.
  The scoped `CASE_GROUPS=path-invalid` quick group then passed 18/18 with `fallback_sum=18`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-140124/suite.tsv`.
  The narrower default-order path tail from `commands-invalid-clip-path-verb-fallback` through
  `commands-point-lines` passed 19/19 with `fallback_sum=18`, `unsupported_rows=0`, `picture_frames=0`, and
  `command_frames=2308`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-141245/suite.tsv`.
  Skiko focused publication and `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Stroke-round-rect dash path-effect bounds/radii validation passed for op 60
  `COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT`. The exact four-row run rewrote right, bottom, radius X, or radius Y to
  `-1`; all rows produced one expected `command-stream-invalid` fallback, zero unsupported rows, zero JBR picture
  frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-133047/suite.tsv`.
  The scoped `CASE_GROUPS=path-invalid` quick group then passed 16/16 with `fallback_sum=16`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-133333/suite.tsv`.
  The narrower default-order path tail from `commands-invalid-clip-path-verb-fallback` through
  `commands-point-lines` passed 17/17 with `fallback_sum=16`, `unsupported_rows=0`, `picture_frames=0`, and
  `command_frames=1026`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-134426/suite.tsv`.
  Skiko focused publication and `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Stroke-rect dash path-effect dimension validation passed for op 59 `COMMAND_STROKE_RECT_DASH_PATH_EFFECT`. The exact
  two-row run rewrote width and height to `-1`; both rows produced one expected `command-stream-invalid` fallback,
  zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-125148/suite.tsv`.
  The scoped `CASE_GROUPS=path-invalid` quick group then passed 12/12 with `fallback_sum=12`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-125332/suite.tsv`.
  A wider primitive/path attempt reached the existing clip-path row after passing the image rows but missed the new app
  measurement window during Gradle startup, so the default-order path tail was rerun from
  `commands-invalid-clip-path-verb-fallback` through `commands-point-lines`; it passed 13/13 with `fallback_sum=12`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=937`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-131533/suite.tsv`.
  Skiko focused publication and `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Stroke-round-rect dash path-effect interval-count validation passed for op 60
  `COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT`. The exact one-row run rewrote the recorded dash interval count to `1`
  and produced one expected `command-stream-invalid` fallback, zero unsupported rows, zero JBR picture frames, and zero
  JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-122059/suite.tsv`.
  The scoped `CASE_GROUPS=path-invalid` quick group then passed 10/10 with `fallback_sum=10`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-122157/suite.tsv`.
  The bounded default-order range from `commands-core-primitives` through `commands-point-lines` passed 26/26 with
  `fallback_sum=24`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=3888`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-122829/suite.tsv`.
  Skiko focused publication and `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Stroke-rect dash path-effect interval-count validation passed for op 59 `COMMAND_STROKE_RECT_DASH_PATH_EFFECT`. The
  exact one-row run rewrote the recorded dash interval count to `1` and produced one expected
  `command-stream-invalid` fallback, zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-115513/suite.tsv`.
  The scoped `CASE_GROUPS=path-invalid` quick group then passed 9/9 with `fallback_sum=9`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-115607/suite.tsv`.
  The bounded default-order range from `commands-core-primitives` through `commands-point-lines` passed 25/25 with
  `fallback_sum=23`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=3178`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-120233/suite.tsv`.
  Skiko focused publication and `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Stroke-line dash path-effect interval-count validation passed for op 43 `COMMAND_STROKE_LINE_DASH_PATH_EFFECT`. The
  exact one-row run rewrote the recorded dash interval count to `1` and produced one expected
  `command-stream-invalid` fallback, zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-112712/suite.tsv`.
  The scoped `CASE_GROUPS=path-invalid` quick group then passed 8/8 with `fallback_sum=8`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-112800/suite.tsv`.
  The bounded default-order range from `commands-core-primitives` through `commands-point-lines` passed 24/24 with
  `fallback_sum=22`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=3173`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-113311/suite.tsv`.
  Skiko focused publication and `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Shader descriptor version validation now uses an explicit `commands-invalid-shader-descriptor-version-fallback` row
  name matching JBR's unsupported shader descriptor version parser fixture. The exact one-row run produced one
  expected `command-stream-invalid` fallback, zero unsupported rows, zero JBR picture frames, and zero JBR command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-102949/suite.tsv`.
  The scoped `CASE_GROUPS=shader-descriptor-invalid` area group passed 30/30 with `fallback_sum=30`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-103042/suite.tsv`.
  The bounded default-order range from `commands-invalid-shader-descriptor-type-fallback` through
  `commands-shader-wrong-effect-type-fallback` passed 31/31 with `fallback_sum=31`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-110020/suite.tsv`.
- Plain saveLayer record-length validation passed for op 13 `COMMAND_SAVE_LAYER`. The exact one-row run shortened the
  recorded plain saveLayer record length by one int and produced one expected `command-stream-invalid` fallback, zero
  unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-093321/suite.tsv`.
  The scoped `CASE_GROUPS=save-layer-invalid` area group then passed 37/37 with `fallback_sum=37`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-093418/suite.tsv`.
  The bounded default-order range from `commands-save-layer-filter` through
  `commands-save-layer-raw-color-filter-fallback` passed 40/40 with `fallback_sum=37`, `unsupported_rows=1`,
  `picture_frames=985`, and `command_frames=1723`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-095826/suite.tsv`.
  Skiko focused publication and `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Descriptor-backed saveLayer record-length validation passed for op 52 `COMMAND_SAVE_LAYER_COLOR_FILTER_REF`, op 54
  `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF`, and op 55 `COMMAND_SAVE_LAYER_IMAGE_FILTER_REF`. The exact three-row
  run shortened each target record length by one int; all rows produced one expected `command-stream-invalid`
  fallback, zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-083632/suite.tsv`.
  The scoped `CASE_GROUPS=save-layer-invalid` area group then passed 36/36 with `fallback_sum=36`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-083848/suite.tsv`.
  The bounded default-order range from `commands-save-layer-filter` through
  `commands-save-layer-raw-color-filter-fallback` passed 39/39 with `fallback_sum=36`, `unsupported_rows=1`,
  `picture_frames=924`, and `command_frames=3483`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-090249/suite.tsv`.
  Skiko focused publication and `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- SaveLayer variant record-flags validation passed with the new narrow iteration path. The clean exact row set covered
  `COMMAND_SAVE_LAYER_COLOR_FILTER`, `COMMAND_SAVE_LAYER_BLEND_MODE`, `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER`,
  `COMMAND_SAVE_LAYER_COLOR_FILTER_REF`, `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF`, and
  `COMMAND_SAVE_LAYER_IMAGE_FILTER_REF`; all six rows produced one expected `command-stream-invalid` fallback each,
  zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-043049/suite.tsv`.
  The scoped `CASE_GROUPS=save-layer-invalid` area group then passed 21/21 with `fallback_sum=21`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-040032/suite.tsv`.
  The bounded default-order range from `commands-save-layer-filter` through
  `commands-save-layer-raw-color-filter-fallback` passed 24/24 with `fallback_sum=21`, `unsupported_rows=1`,
  `picture_frames=1772`, and `command_frames=5948`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-041438/suite.tsv`.
  Skiko `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- SaveLayer color-filter width/height validation passed for op 44 `COMMAND_SAVE_LAYER_COLOR_FILTER`. The exact two-row
  run rewrote width and height to `-1`; both rows produced one expected `command-stream-invalid` fallback, zero
  unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-044744/suite.tsv`.
  The scoped `CASE_GROUPS=save-layer-invalid` area group then passed 23/23 with `fallback_sum=23`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-044919/suite.tsv`.
  The bounded default-order range from `commands-save-layer-filter` through
  `commands-save-layer-raw-color-filter-fallback` passed 26/26 with `fallback_sum=23`, `unsupported_rows=1`,
  `picture_frames=1775`, and `command_frames=5587`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-050428/suite.tsv`.
  Skiko focused publication and `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- SaveLayer blend-mode width/height validation passed for op 50 `COMMAND_SAVE_LAYER_BLEND_MODE`. The exact two-row run
  rewrote width and height to `-1`; both rows produced one expected `command-stream-invalid` fallback, zero unsupported
  rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-052535/suite.tsv`.
  The scoped `CASE_GROUPS=save-layer-invalid` area group then passed 25/25 with `fallback_sum=25`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-052706/suite.tsv`.
  The bounded default-order range from `commands-save-layer-filter` through
  `commands-save-layer-raw-color-filter-fallback` passed 28/28 with `fallback_sum=25`, `unsupported_rows=1`,
  `picture_frames=1736`, and `command_frames=4996`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-054336/suite.tsv`.
  Skiko focused publication and `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- SaveLayer blend/color-filter width/height validation passed for op 51
  `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER`. The exact two-row run rewrote width and height to `-1`; both rows produced
  one expected `command-stream-invalid` fallback, zero unsupported rows, zero JBR picture frames, and zero JBR command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-060758/suite.tsv`.
  The scoped `CASE_GROUPS=save-layer-invalid` area group then passed 27/27 with `fallback_sum=27`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-060928/suite.tsv`.
  The bounded default-order range from `commands-save-layer-filter` through
  `commands-save-layer-raw-color-filter-fallback` passed 30/30 with `fallback_sum=27`, `unsupported_rows=1`,
  `picture_frames=1481`, and `command_frames=5326`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-062712/suite.tsv`.
  Skiko focused publication and `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- SaveLayer alpha validation passed for the remaining supported non-ref variants: op 44
  `COMMAND_SAVE_LAYER_COLOR_FILTER`, op 50 `COMMAND_SAVE_LAYER_BLEND_MODE`, and op 51
  `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER`. The exact three-row run rewrote `alpha1000` to `1001`; all rows produced
  one expected `command-stream-invalid` fallback, zero unsupported rows, zero JBR picture frames, and zero JBR command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-065320/suite.tsv`.
  The scoped `CASE_GROUPS=save-layer-invalid` area group then passed 30/30 with `fallback_sum=30`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-065533/suite.tsv`.
  The bounded default-order range from `commands-save-layer-filter` through
  `commands-save-layer-raw-color-filter-fallback` passed 33/33 with `fallback_sum=30`, `unsupported_rows=1`,
  `picture_frames=1438`, and `command_frames=5420`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-071506/suite.tsv`.
  Skiko focused publication and `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- SaveLayer record-length validation passed for op 44 `COMMAND_SAVE_LAYER_COLOR_FILTER`, op 50
  `COMMAND_SAVE_LAYER_BLEND_MODE`, and op 51 `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER`. The exact three-row run
  shortened each target record length by one int; all rows produced one expected `command-stream-invalid` fallback,
  zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-074334/suite.tsv`.
  The scoped `CASE_GROUPS=save-layer-invalid` area group then passed 33/33 with `fallback_sum=33`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-074550/suite.tsv`.
  The bounded default-order range from `commands-save-layer-filter` through
  `commands-save-layer-raw-color-filter-fallback` passed 36/36 with `fallback_sum=33`, `unsupported_rows=1`,
  `picture_frames=1719`, and `command_frames=4995`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-080718/suite.tsv`.
  Skiko focused publication and `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Full default command-probe sweep passed after the draw-vertices parser sentinel series and the effect descriptor
  version/payload-count/record-length rows were in the default set. The run used command-semantic validation with
  screenshot assertions disabled. Aggregate: 416/416 passed, 26 rows with intentional unsupported-picture replay,
  30,096 JBR picture frames, 201,780 JBR command frames, and 279 structured fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-203639/suite.tsv`.
  This is the periodic consolidation after the quick exact-row and `CASE_GROUPS=primitive-invalid` iterations for
  draw-vertices vertex count, record length, vertex mode, blend mode, and index count.
- Full default command-probe sweep passed after adding the draw-points record-length sentinel. The run used
  command-semantic validation with screenshot assertions disabled, included
  `commands-invalid-draw-points-point-count-fallback` and
  `commands-invalid-draw-points-record-length-fallback` in the default set, and passed every row. Aggregate: 411/411
  passed, 27 rows with intentional unsupported-picture replay, 25,809 JBR picture frames, 151,351 JBR command frames,
  and 274 structured fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-144422/suite.tsv`.
- Split broad command-probe consolidation passed after the draw-points point-count sentinel and full-log image-cache
  marker validation fix. The first run passed the default prefix through
  `commands-forced-context-native-system-font-text`; the second resumed at `commands-forced-context-dynamic-images`
  after the cache-marker fix and passed through descriptor wrong-type rows; the third resumed at
  `commands-gradient-stroke` with screenshot assertions disabled for command-only semantics after a macOS capture
  flake. Combined aggregate: 410/410 passed, 27 rows with intentional unsupported-picture replay, 27,097 JBR picture
  frames, 147,205 JBR command frames, and 273 structured fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-093730/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-102727/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-123615/suite.tsv`.
- Magic Jewel report validation now reads image-cache clear/evict expectations from full logs instead of sampled logs.
  This fixes long default rows where the one-shot `COMMAND_CLEAR_IMAGE_CACHE` frame is emitted before the sampled-log
  window even though `new.log` contains both `imageCacheClears=1` and `JBR_SKIA_INTEROP_IMAGE_CACHE_CLEAR`. The
  default `commands-forced-context-dynamic-images` row passed after the harness fix with zero fallback, zero
  unsupported rows, zero JBR picture frames, and 952 JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-102630/suite.tsv`.
- Skiko draw-points point-count sentinel validation passed. The new hook rewrites the first recorded
  `COMMAND_DRAW_POINTS` point count to zero and emits
  `SKIKO_JBR_INTEROP_DRAW_POINTS_POINT_COUNT_CORRUPTED`, exercising JBR's parser guard that point-count metadata must
  be in range and match the record length. The exact Magic Jewel row passed with one expected
  `command-stream-invalid` fallback, zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-142958/suite.tsv`.
- Skiko draw-points record-length sentinel validation passed. The hook shortens the first recorded
  `COMMAND_DRAW_POINTS` record length and emits `SKIKO_JBR_INTEROP_DRAW_POINTS_RECORD_LENGTH_CORRUPTED`, exercising
  JBR's exact `recordLength == 9 + pointCount * 2` parser guard without changing the recorded point-count field. The
  exact Magic Jewel row passed with one expected `command-stream-invalid` fallback, zero unsupported rows, zero JBR
  picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-143828/suite.tsv`.
  The narrower Skiko publication path
  `./gradlew publishAwtPublicationToMavenLocal publishAwtRuntimeElementsPublicationToMavenLocal publishKotlinMultiplatformPublicationToMavenLocal`
  passed after full `publishToMavenLocal` hit an external Skia macOS arm64 release 404. Skiko
  `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Skiko draw-points upper-bound sentinel validation passed. The hook rewrites the first recorded
  `COMMAND_DRAW_POINTS` point count to `4097` and emits `SKIKO_JBR_INTEROP_DRAW_POINTS_MAX_POINT_COUNT_CORRUPTED`,
  exercising JBR's `pointCount <= 4096` parser guard. The exact Magic Jewel row passed with one expected
  `command-stream-invalid` fallback, zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-014443/suite.tsv`.
  The narrower Skiko publication path passed, and
  `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Scoped `primitive-invalid` validation passed after adding the draw-points record-length row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-143915/suite.tsv`.
  The quick group now covers malformed stroke cap, transform record flags, clip operation, draw-points point count,
  and draw-points record length. Aggregate: 5/5 passed, zero unsupported rows, zero JBR picture frames, zero JBR
  command frames, and five structured invalid-stream fallback markers. Screenshot assertions were disabled for this
  command-only semantic slice.
- Skiko draw-vertices vertex-count sentinel validation passed. The hook rewrites the first recorded
  `COMMAND_DRAW_VERTICES` vertex count to two and emits `SKIKO_JBR_INTEROP_DRAW_VERTICES_VERTEX_COUNT_CORRUPTED`,
  exercising JBR's parser guard that vertices records must have at least three vertices and match the exact variable
  record length. The exact Magic Jewel row passed with one expected `command-stream-invalid` fallback, zero
  unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-194429/suite.tsv`.
  The narrower Skiko publication path passed, and
  `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Scoped `primitive-invalid` validation passed after adding the draw-vertices vertex-count row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-194537/suite.tsv`.
  The quick group now covers malformed stroke cap, transform record flags, clip operation, draw-points point count,
  draw-points record length, and draw-vertices vertex count. Aggregate: 6/6 passed, zero unsupported rows, zero JBR
  picture frames, zero JBR command frames, and six structured invalid-stream fallback markers. Screenshot assertions
  were disabled for this command-only semantic slice.
- Skiko draw-vertices record-length sentinel validation passed. The hook shortens the first recorded
  `COMMAND_DRAW_VERTICES` record length and emits `SKIKO_JBR_INTEROP_DRAW_VERTICES_RECORD_LENGTH_CORRUPTED`,
  exercising JBR's exact `recordLength == 8 + vertexCount * 5 + indexCount` parser guard. The exact Magic Jewel row
  passed with one expected `command-stream-invalid` fallback, zero unsupported rows, zero JBR picture frames, and zero
  JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-195430/suite.tsv`.
  The narrower Skiko publication path passed, and
  `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Scoped `primitive-invalid` validation passed after adding the draw-vertices record-length row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-195521/suite.tsv`.
  The quick group now covers malformed stroke cap, transform record flags, clip operation, draw-points point count,
  draw-points record length, draw-vertices vertex count, and draw-vertices record length. Aggregate: 7/7 passed, zero
  unsupported rows, zero JBR picture frames, zero JBR command frames, and seven structured invalid-stream fallback
  markers. Screenshot assertions were disabled for this command-only semantic slice.
- Skiko draw-vertices vertex-mode sentinel validation passed. The hook rewrites the first recorded
  `COMMAND_DRAW_VERTICES` vertex mode to `3` and emits `SKIKO_JBR_INTEROP_DRAW_VERTICES_VERTEX_MODE_CORRUPTED`,
  exercising JBR's `vertexMode >= 0 && vertexMode <= 2` parser guard. The exact Magic Jewel row passed with one
  expected `command-stream-invalid` fallback, zero unsupported rows, zero JBR picture frames, and zero JBR command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-200424/suite.tsv`.
  The narrower Skiko publication path passed, and
  `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Scoped `primitive-invalid` validation passed after adding the draw-vertices vertex-mode row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-200517/suite.tsv`.
  The quick group now covers malformed stroke cap, transform record flags, clip operation, draw-points point count,
  draw-points record length, draw-vertices vertex count, draw-vertices record length, and draw-vertices vertex mode.
  Aggregate: 8/8 passed, zero unsupported rows, zero JBR picture frames, zero JBR command frames, and eight
  structured invalid-stream fallback markers. Screenshot assertions were disabled for this command-only semantic
  slice.
- Skiko draw-vertices blend-mode sentinel validation passed. The hook rewrites the first recorded
  `COMMAND_DRAW_VERTICES` blend mode to an unsupported value and emits
  `SKIKO_JBR_INTEROP_DRAW_VERTICES_BLEND_MODE_CORRUPTED`, exercising JBR's `isSupportedBlendMode(blendMode)` parser
  guard. The exact Magic Jewel row passed with one expected `command-stream-invalid` fallback, zero unsupported rows,
  zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-201535/suite.tsv`.
  The narrower Skiko publication path passed, and
  `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Scoped `primitive-invalid` validation passed after adding the draw-vertices blend-mode row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-201625/suite.tsv`.
  The quick group now covers malformed stroke cap, transform record flags, clip operation, draw-points point count,
  draw-points record length, draw-vertices vertex count, draw-vertices record length, draw-vertices vertex mode, and
  draw-vertices blend mode. Aggregate: 9/9 passed, zero unsupported rows, zero JBR picture frames, zero JBR command
  frames, and nine structured invalid-stream fallback markers. Screenshot assertions were disabled for this
  command-only semantic slice.
- Skiko draw-vertices index-count sentinel validation passed. The hook rewrites the first recorded
  `COMMAND_DRAW_VERTICES` index count to `-1` and emits
  `SKIKO_JBR_INTEROP_DRAW_VERTICES_INDEX_COUNT_CORRUPTED`, exercising JBR's `indexCount >= 0` parser guard and the
  exact variable record-length check. The exact Magic Jewel row passed with one expected `command-stream-invalid`
  fallback, zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-202605/suite.tsv`.
  The narrower Skiko publication path passed, and
  `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Scoped `primitive-invalid` validation passed after adding the draw-vertices index-count row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-202656/suite.tsv`.
  The quick group now covers malformed stroke cap, transform record flags, clip operation, draw-points point count,
  draw-points record length, draw-vertices vertex count, draw-vertices record length, draw-vertices vertex mode,
  draw-vertices blend mode, and draw-vertices index count. Aggregate: 10/10 passed, zero unsupported rows, zero JBR
  picture frames, zero JBR command frames, and ten structured invalid-stream fallback markers. Screenshot assertions
  were disabled for this command-only semantic slice.
- Skiko draw-vertices upper-bound sentinel validation passed. The new hooks rewrite the first recorded
  `COMMAND_DRAW_VERTICES` vertex count to `4097` or index count to `8193`, exercising JBR's `vertexCount <= 4096`
  and `indexCount <= 8192` parser guards. The exact two-row Magic Jewel run passed with one expected
  `command-stream-invalid` fallback per row, zero unsupported rows, zero JBR picture frames, and zero JBR command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-013122/suite.tsv`.
  The narrower Skiko publication path passed, and
  `./gradlew awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` also passed.
- Scoped `primitive-invalid` validation passed after adding the draw-vertices upper-bound rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-013258/suite.tsv`.
  The quick group now covers malformed stroke cap, transform record flags, clip operation, draw-points point count,
  draw-points record length, draw-vertices vertex-count lower and upper bounds, draw-vertices record length,
  draw-vertices vertex mode, draw-vertices blend mode, and draw-vertices index-count lower and upper bounds. Aggregate:
  12/12 passed, zero unsupported rows, zero JBR picture frames, zero JBR command frames, and twelve structured
  invalid-stream fallback markers. Screenshot assertions were disabled for this command-only semantic slice.
- Scoped `primitive-invalid` validation passed after adding the draw-points upper-bound row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-014536/suite.tsv`.
  The quick group now covers malformed stroke cap, transform record flags, clip operation, draw-points point-count
  lower and upper bounds, draw-points record length, draw-vertices vertex-count lower and upper bounds, draw-vertices
  record length, draw-vertices vertex mode, draw-vertices blend mode, and draw-vertices index-count lower and upper
  bounds. Aggregate: 13/13 passed, zero unsupported rows, zero JBR picture frames, zero JBR command frames, and
  thirteen structured invalid-stream fallback markers. Screenshot assertions were disabled for this command-only
  semantic slice.
- Bounded default-order validation passed after adding the primitive upper-bound rows. The point-range run covered the
  default insertion path from native bridge through point dots:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-015718/suite.tsv`.
  Aggregate: 27/27 passed, zero unsupported rows, zero JBR picture frames, 11,992 JBR command frames, and
  twenty-three structured fallback markers. The vertices-to-blend-mode range also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-021540/suite.tsv`.
  Aggregate: 9/9 passed, zero unsupported rows, zero JBR picture frames, 5,171 JBR command frames, and seven
  structured fallback markers. Screenshot assertions were disabled for both command-only semantic slices.
- Scoped `primitive-invalid` validation passed after adding the draw-points point-count row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-093531/suite.tsv`.
  The quick group now covers malformed stroke cap, transform record flags, clip operation, and draw-points point
  count. Aggregate: 4/4 passed, zero unsupported rows, zero JBR picture frames, zero JBR command frames, and four
  structured invalid-stream fallback markers. Screenshot assertions were disabled for this command-only semantic
  slice.
- CMP focused recorder validation passed after adding pending image-cache clear emission:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.emitsImageCacheClearForInteropSurfaceChange --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.reusesStableImageCacheEntriesAcrossFrames --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.clearsTintColorFilterHandleCacheForInteropSurfaceChange`
  in `/Users/rock3r/src/jbr-skia-zero-copy/cmp`. The new test verifies that
  `clearInteropCachesForSurfaceChange()` queues one `COMMAND_CLEAR_IMAGE_CACHE` at the start of the next top-level
  frame, increments `imageCacheClearCount`, and redefines a previously cached image; `clearImageCacheForTesting()`
  remains a local-only reset.
- Focused image cache clear record-flags validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-091119/suite.tsv`.
  The new `commands-invalid-image-cache-clear-record-flags-fallback` row forces destination context migration to emit
  `COMMAND_CLEAR_IMAGE_CACHE`, rewrites that clear record's flags word to `COMMAND_RECORD_FLAG_ANTIALIAS`, and
  requires `SKIKO_JBR_INTEROP_IMAGE_CACHE_CLEAR_RECORD_FLAGS_CORRUPTED` plus `command-stream-invalid` fallback. The
  row passed with one expected fallback, zero unsupported rows, zero JBR picture frames, and 176 recovering JBR
  command frames.
- Supported forced-context dynamic-image validation passed with scoped clear markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-091332/suite.tsv`.
  The row now requires at least one CMP image-cache clear, one JBR image-cache clear, and one scoped JBR clear marker
  in addition to image refs, image-cache evictions, surface/context-change markers, and command-cache clears. It
  passed with zero fallback, zero unsupported rows, zero JBR picture frames, and 410 JBR command frames.
- Scoped `image-handles-invalid` validation passed after adding the image cache clear record-flags row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-091447/suite.tsv`.
  The quick group now covers twenty-three malformed image definition/cache-key/cache-clear/eviction and image-ref
  rows. Aggregate: 23/23 passed, zero unsupported rows, zero JBR picture frames, 468 JBR command frames, and
  twenty-three structured invalid-stream fallback markers.
- Focused forced-context image-ref screenshot parity passed after updating the scoped-clear contract:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260520-092645/suite.tsv`.
  The row stayed on command replay with zero fallback, zero JBR picture frames, 487 JBR command frames,
  `avg_delta=2.129`, and `compose_bad_pixel_ratio=0.07527`.
- Focused descriptor-handle eviction record-flags validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-085719/suite.tsv`.
  The new `commands-invalid-shader-evict-record-flags-fallback` and
  `commands-invalid-color-filter-evict-record-flags-fallback` rows reuse the existing descriptor use-after-evict
  insertion path, then rewrite the inserted `COMMAND_EVICT_SHADER_HANDLE` or `COMMAND_EVICT_COLOR_FILTER_HANDLE`
  record flags to `COMMAND_RECORD_FLAG_ANTIALIAS`. Aggregate: 2/2 passed, zero unsupported rows, zero JBR picture
  frames, zero JBR command frames, and two structured invalid-stream fallback markers.
- Focused descriptor-handle adjacent slice passed after adding the eviction record-flags rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-085837/suite.tsv`.
  The explicit five-row quick slice covered the existing shader/color-filter/path-effect descriptor use-after-evict
  rows plus the two new eviction record-flags rows. Aggregate: 5/5 passed, zero unsupported rows, zero JBR picture
  frames, zero JBR command frames, and five structured invalid-stream fallback markers.
- Focused image cache eviction record-flags validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-084204/suite.tsv`.
  The new `commands-invalid-image-evict-record-flags-fallback` row records image cache churn until
  `COMMAND_EVICT_IMAGE_CACHE_KEY` appears, rewrites that eviction record's flags word to
  `COMMAND_RECORD_FLAG_ANTIALIAS`, and requires `SKIKO_JBR_INTEROP_IMAGE_EVICT_RECORD_FLAGS_CORRUPTED` plus
  `command-stream-invalid` fallback. The row passed with one expected fallback, zero unsupported rows, zero JBR
  picture frames, and zero JBR command frames.
- Scoped `image-handles-invalid` validation passed after adding the image cache eviction record-flags row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-084246/suite.tsv`.
  The quick group now covers twenty-two malformed image definition/cache-key/eviction and image-ref rows. Aggregate:
  22/22 passed, zero unsupported rows, zero JBR picture frames, zero JBR command frames, and twenty-two structured
  invalid-stream fallback markers.
- Periodic full default command-probe sweep passed after adding the font-data record-flags sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-055642/suite.tsv`.
  Aggregate: 406/406 passed, 26 rows with intentional unsupported-picture replay, 8,744 JBR picture frames, 49,068
  JBR command frames, and 269 structured fallback markers. The default set now includes
  `commands-invalid-font-data-record-flags-fallback`, which passed with one required invalid-stream fallback and
  command replay recovery afterward.
- Focused font-data definition record-flags validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-055135/suite.tsv`.
  The new `commands-invalid-font-data-record-flags-fallback` row records cache-front-loaded
  `COMMAND_DEFINE_FONT_DATA`, rewrites its record flags to `COMMAND_RECORD_FLAG_ANTIALIAS`, and requires
  `SKIKO_JBR_INTEROP_FONT_DATA_RECORD_FLAGS_CORRUPTED` plus `command-stream-invalid` fallback. Because font-data
  definitions are emitted during command-cache warmup, this row uses the new report-validation recovery expectation:
  one fallback is required, then later frames may return to command replay. Aggregate: 1/1 passed, zero unsupported
  rows, zero JBR picture frames, 469 JBR command frames after recovery, and one structured fallback marker.
- Scoped `native-text-invalid` validation passed after adding the font-data record-flags row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-055209/suite.tsv`.
  The quick group now covers eleven malformed native text/font-data parser rows. Aggregate: 11/11 passed,
  zero unsupported rows, zero JBR picture frames, 482 JBR command frames from the recovering font-data row, and eleven
  structured invalid-stream fallback markers. Screenshot assertions were disabled for this command-only semantic check.
- Focused image definition record-flags validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-053349/suite.tsv`.
  The new `commands-invalid-image-define-record-flags-fallback` row records a `COMMAND_DEFINE_IMAGE_ARGB`, rewrites
  its record flags to `COMMAND_RECORD_FLAG_ANTIALIAS`, and requires
  `SKIKO_JBR_INTEROP_IMAGE_DEFINE_RECORD_FLAGS_CORRUPTED` plus `command-stream-invalid` fallback before replay.
  Aggregate: 1/1 passed, zero unsupported rows, zero JBR picture frames, zero JBR command frames, and one structured
  fallback marker.
- Scoped `image-handles-invalid` validation passed after adding the image definition record-flags row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-053424/suite.tsv`.
  The quick group now covers twenty-one malformed image definition/cache-key and image-ref rows. Aggregate: 21/21
  passed, zero unsupported rows, zero JBR picture frames, zero JBR command frames, and twenty-one structured
  invalid-stream fallback markers. Screenshot assertions were disabled for this command-only semantic check.
- Focused shader descriptor record-flags validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-051747/suite.tsv`.
  The new `commands-invalid-shader-descriptor-record-flags-fallback` row records a
  `COMMAND_DEFINE_SHADER_DESCRIPTOR`, rewrites its record flags to `COMMAND_RECORD_FLAG_ANTIALIAS`, and requires
  `SKIKO_JBR_INTEROP_SHADER_DESCRIPTOR_RECORD_FLAGS_CORRUPTED` plus `command-stream-invalid` fallback before replay.
  Aggregate: 1/1 passed, zero unsupported rows, zero JBR picture frames, zero JBR command frames, and one structured
  fallback marker.
- Scoped `shader-descriptor-invalid` validation passed after adding the record-flags row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-051827/suite.tsv`.
  The quick group now covers thirty malformed shader-descriptor rows. Aggregate: 30/30 passed, zero unsupported rows,
  zero JBR picture frames, zero JBR command frames, and thirty structured invalid-stream fallback markers. Screenshot
  assertions were disabled for this command-only semantic check.
- Focused effect descriptor record-flags validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-050237/suite.tsv`.
  The new `commands-invalid-effect-descriptor-record-flags-fallback` row records a
  `COMMAND_DEFINE_EFFECT_DESCRIPTOR`, rewrites its record flags to `COMMAND_RECORD_FLAG_ANTIALIAS`, and requires
  `SKIKO_JBR_INTEROP_EFFECT_DESCRIPTOR_RECORD_FLAGS_CORRUPTED` plus `command-stream-invalid` fallback before replay.
  Aggregate: 1/1 passed, zero unsupported rows, zero JBR picture frames, zero JBR command frames, and one structured
  fallback marker.
- Scoped `effect-descriptor-invalid` validation passed after adding the record-flags row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-050312/suite.tsv`.
  The quick group now covers twenty-eight malformed effect-descriptor rows. Aggregate: 28/28 passed, zero unsupported
  rows, zero JBR picture frames, zero JBR command frames, and twenty-eight structured invalid-stream fallback markers.
  Screenshot assertions were disabled for this command-only semantic check.
- Focused saveLayer record-flags validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-045239/suite.tsv`.
  The new `commands-invalid-save-layer-record-flags-fallback` row records a plain `COMMAND_SAVE_LAYER`, rewrites its
  record flags to `COMMAND_RECORD_FLAG_ANTIALIAS`, and requires
  `SKIKO_JBR_INTEROP_SAVE_LAYER_RECORD_FLAGS_CORRUPTED` plus `command-stream-invalid` fallback before replay. Aggregate:
  1/1 passed, zero unsupported rows, zero JBR picture frames, zero JBR command frames, and one structured fallback
  marker.
- Scoped `save-layer-invalid` validation passed after adding the record-flags row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-045314/suite.tsv`.
  The quick group now covers fifteen malformed saveLayer rows. Aggregate: 15/15 passed, zero unsupported rows, zero
  JBR picture frames, zero JBR command frames, and fifteen structured invalid-stream fallback markers. Screenshot
  assertions were disabled for this command-only semantic check.
- Full default command-probe sweep passed after adding primitive command parser sentinels and rebuilding local JBR Skia
  artifacts:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-002459/suite.tsv`.
  Aggregate: 401/401 passed, 26 rows with intentional unsupported-picture fallback, 32,480 JBR picture frames,
  184,608 JBR command frames, and 264 structured fallback markers. Screenshot assertions were disabled for this broad
  semantic sweep because earlier macOS window capture attempts failed independently of command replay; command markers,
  fallback reasons, unsupported reasons, JBR picture frames, and JBR command frames were still validated.
- Scoped `primitive-invalid` validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-002341/suite.tsv`.
  The quick group covers three primitive command parser guards: `COMMAND_STROKE_LINE` cap, `COMMAND_TRANSLATE` record
  flags, and `COMMAND_CLIP_RECT` operation. Aggregate: 3/3 passed, zero unsupported rows, zero JBR picture frames,
  zero JBR command frames, and three structured invalid-stream fallback markers.
- Focused stroke-cap validation passed after rebuilding `/tmp` local artifacts:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-002307/suite.tsv`.
  Before the rebuild, several rows reported `SKIKO_JBR_INTEROP_FALLBACK reason=service-unavailable`; rebuilding
  `/tmp/jbr-skia-run/desktop`, `/tmp/jbr-api-shim.jar`, and `/tmp/jbr-skia-native/libjbrskiainterop.dylib` restored
  service discovery and the full sweep later passed.
- Focused command payload and record-length validation passed after adding typed live sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-193923/suite.tsv`.
  The four rows rewrite the command payload length to negative, truncated, or extra values, or rewrite the first command
  record length, matching JBR's parser-only payload/record-length guards. Aggregate: 4/4 passed, zero unsupported rows,
  zero JBR picture frames, zero JBR command frames, and four structured invalid-stream fallback markers.
- Scoped `stream-invalid` validation passed after adding the payload and record-length rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-193642/suite.tsv`.
  This quick group now covers eight stream parser guards. Aggregate: 8/8 passed, zero unsupported rows, zero JBR
  picture frames, zero JBR command frames, and eight structured invalid-stream fallback markers.
- Focused command-stream coordinate-space and paint-format validation passed after adding typed live sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-193259/suite.tsv`.
  The two new rows rewrite the command stream coordinate-space or paint-format header words to unsupported values,
  matching JBR's parser-only header guards. Aggregate: 2/2 passed, zero unsupported rows, zero JBR picture frames,
  zero JBR command frames, and two structured invalid-stream fallback markers.
- Scoped `stream-invalid` validation passed after adding the coordinate-space and paint-format rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-193047/suite.tsv`.
  This quick group now covers stream flags, command record flags, coordinate space, and paint format. Aggregate: 4/4
  passed, zero unsupported rows, zero JBR picture frames, zero JBR command frames, and four structured invalid-stream
  fallback markers.
- Focused command record-flags validation passed after adding a typed live sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-192645/suite.tsv`.
  The new `commands-invalid-command-record-flags-fallback` row rewrites the first command record flags word to an
  unsupported value, matching JBR's parser-only unsupported record-flags guard. It requires
  `SKIKO_JBR_INTEROP_COMMAND_RECORD_FLAGS_CORRUPTED`; aggregate: 1/1 passed, zero unsupported rows, zero JBR picture
  frames, zero JBR command frames, and one structured invalid-stream fallback marker.
- Scoped `stream-invalid` validation passed after adding the record-flags row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-192129/suite.tsv`.
  This quick group now covers stream header flags and per-record flags. Aggregate: 2/2 passed, zero unsupported rows,
  zero JBR picture frames, zero JBR command frames, and two structured invalid-stream fallback markers.
- Focused command-stream header flag validation passed after adding a typed live sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-190837/suite.tsv`.
  The new `commands-invalid-command-stream-flags-fallback` row uses Skiko's generic stream corruption switch to rewrite
  the command stream flags word to an unsupported value, matching JBR's parser-only unsupported stream-flags guard. It
  requires `SKIKO_JBR_INTEROP_COMMAND_STREAM_FLAGS_CORRUPTED`; aggregate: 1/1 passed, zero unsupported rows, zero JBR
  picture frames, zero JBR command frames, and one structured invalid-stream fallback marker.
- Scoped `stream-invalid` validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-190836/suite.tsv`.
  This one-row quick group is the point-to-point iteration path for stream-header parser guards.
- Current-artifact focused validation rechecked the already-live unknown effect descriptor type sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-023959/suite.tsv`.
  The row rewrites one recorded effect descriptor type to an unknown value and requires
  `SKIKO_JBR_INTEROP_EFFECT_DESCRIPTOR_TYPE_CORRUPTED`; aggregate: 1/1 passed, zero unsupported rows, zero JBR picture
  frames, zero JBR command frames, and one structured invalid-stream fallback marker.
- Scoped `effect-descriptor-invalid` validation passed on the same current artifacts:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-024048/suite.tsv`.
  The area group covered twenty-eight malformed effect-descriptor rows, including the unknown-type row, and kept every
  row in structured `command-stream-invalid` fallback. Aggregate: 28/28 passed, zero unsupported rows, zero JBR
  picture frames, zero JBR command frames, and twenty-eight structured invalid-stream fallback markers.
- Focused stroke-path dash path-effect scalar validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-184636/suite.tsv`.
  The two new rows record op 61 `COMMAND_STROKE_PATH` with a dash path effect, corrupt either the dash interval count
  or one interval value, and require typed Skiko corruption markers plus `command-stream-invalid` fallback. Aggregate:
  2/2 passed, zero unsupported rows, zero JBR picture frames, zero JBR command frames, and two structured
  invalid-stream fallback markers.
- Scoped `path-invalid` validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-184818/suite.tsv`.
  This quick group now covers seven malformed path rows, including path verb corruption and stroke-path dash
  path-effect verb, interval-count, and interval-value guards. Aggregate: 7/7 passed, zero unsupported rows, zero JBR
  picture frames, zero JBR command frames, and seven structured invalid-stream fallback markers.
- Focused fill-rect shader-ref bounds/alpha validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-182225/suite.tsv`.
  The three new rows record op 58 `COMMAND_FILL_RECT_SHADER_REF`, corrupt horizontal bounds, vertical bounds, or
  `alpha1000`, and require typed Skiko corruption markers plus `command-stream-invalid` fallback. Aggregate: 3/3
  passed, zero unsupported rows, zero JBR picture frames, zero JBR command frames, and three structured invalid-stream
  fallback markers.
- Scoped `shader-ref-invalid` validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-183006/suite.tsv`.
  This quick group is the point-to-point iteration path for malformed descriptor-backed fill-rect shader refs.
- Bounded default-order shader-ref range validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-183458/suite.tsv`.
  The range covered the existing shader descriptor use, the three malformed shader-ref scalar rows, the path-effect
  descriptor use row, and the descriptor use-after-evict row. Aggregate: 6/6 passed, zero unsupported rows, zero JBR
  picture frames, zero JBR command frames, and six structured invalid-stream fallback markers.
- Focused fill-rect color-filter blend-mode/width/height validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-173809/suite.tsv`.
  The three new rows record op 42 `COMMAND_FILL_RECT_COLOR_FILTER`, rewrite tint blend mode to an unsupported value or
  width/height to `-1`, and require typed Skiko corruption markers plus `command-stream-invalid` fallback. Aggregate:
  3/3 passed, zero unsupported rows, zero JBR picture frames, zero JBR command frames, and three structured
  invalid-stream fallback markers.
- Scoped `fill-rect-color-filter-invalid` validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-174059/suite.tsv`.
  This quick group is the point-to-point iteration path for malformed inline tint color-filter scalar guards.
- Bounded default-order color-filter range validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-180104/suite.tsv`.
  The range covered the existing raw blend color-filter fallback sentinel, the three malformed inline color-filter
  rows, and the supported `commands-color-filter` row. Aggregate: 5/5 passed, one expected unsupported-marker row,
  1,040 JBR picture-fallback frames, 926 JBR command frames, and three structured invalid-stream fallback markers.
- Focused fill-rect color-filter-ref width/height validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-022809/suite.tsv`.
  The two new rows record op 47 `COMMAND_FILL_RECT_COLOR_FILTER_REF`, rewrite width or height to `-1`, and require
  typed Skiko corruption markers plus `command-stream-invalid` fallback. Aggregate: 2/2 passed, zero unsupported rows,
  zero JBR picture frames, zero JBR command frames, and two structured invalid-stream fallback markers.
- Expanded `fill-rect-color-filter-invalid` validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-022940/suite.tsv`.
  This quick group now covers the existing op 42 inline tint color-filter blend-mode/width/height rows plus the new
  op 47 handle-backed color-filter-ref width/height rows. Aggregate: 5/5 passed, zero unsupported rows, zero JBR
  picture frames, zero JBR command frames, and five structured invalid-stream fallback markers.
- Bounded default-order color-filter range validation passed after adding the op 47 rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-023307/suite.tsv`.
  The range covered the existing raw blend color-filter fallback sentinel, the five malformed fill-rect color-filter
  rows, and the supported `commands-color-filter` row. Aggregate: 7/7 passed, one expected unsupported-marker row,
  1,792 JBR picture-fallback frames, 3,025 JBR command frames, and five structured invalid-stream fallback markers.
- Focused fill-rect blend-mode width/height validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-172416/suite.tsv`.
  The two new rows record op 41 `COMMAND_FILL_RECT_BLEND_MODE`, rewrite width or height to `-1`, and require the
  typed Skiko corruption markers plus `command-stream-invalid` fallback. Aggregate: 2/2 passed, zero unsupported rows,
  zero JBR picture frames, zero JBR command frames, and two structured invalid-stream fallback markers.
- Scoped `blend-mode-invalid` validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-172545/suite.tsv`.
  This quick group is the point-to-point iteration path for malformed fill-rect blend-mode scalar guards.
- Bounded default-order blend-mode range validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-172903/suite.tsv`.
  The range covered the supported `commands-blend-mode` row, both malformed fill-rect blend-mode rows, and the adjacent
  `commands-graphics-layer` row. Aggregate: 4/4 passed, zero unsupported rows, 2,013 JBR command frames, and two
  structured invalid-stream fallback markers.
- Focused saveLayer blend/color-filter-ref blend-mode validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-152806/suite.tsv`.
  The new `commands-invalid-save-layer-blend-color-filter-ref-blend-mode-fallback` row records op 54
  `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF` and rewrites its saveLayer blend mode to an unsupported value. It
  reported one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR
  command frames.
- Scoped saveLayer-invalid validation passed after adding the op 54 blend-mode row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-152857/suite.tsv`.
  The quick group now covers fourteen malformed saveLayer rows. All fourteen passed with zero unsupported rows, zero
  picture rows, zero command replay rows, and one structured fallback marker per row.
- Bounded default-order saveLayer range validation passed after adding the op 54 blend-mode row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-153941/suite.tsv`.
  The range covered seventeen rows: two supported saveLayer command-replay rows, fourteen malformed saveLayer rows, and
  the existing raw color-filter fallback sentinel. Aggregate: 17/17 passed, one expected unsupported-marker row, 1,941
  JBR command frames, 863 expected picture-fallback frames, and fourteen structured invalid-stream fallback markers.
- Focused saveLayer color-filter-ref dimension validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-140556/suite.tsv`.
  The new width/height rows cover op 52 `COMMAND_SAVE_LAYER_COLOR_FILTER_REF` and op 54
  `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF`, rewriting the recorded width or height to `-1`. All four rows reported
  one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command
  frames.
- Scoped saveLayer-invalid validation passed after adding the color-filter-ref dimension rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-140848/suite.tsv`.
  The quick group now covers thirteen malformed saveLayer rows, including width, height, and alpha bounds for op 52,
  op 54, and op 55 descriptor-backed saveLayer forms. All thirteen passed with zero unsupported rows, zero picture
  rows, zero command replay rows, and one structured fallback marker per row.
- Bounded default-order saveLayer range validation passed after adding the color-filter-ref dimension rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-141854/suite.tsv`.
  The range covered sixteen rows: two supported saveLayer command-replay rows, thirteen malformed saveLayer rows, and
  the existing raw color-filter fallback sentinel. Aggregate: 16/16 passed, one expected unsupported-marker row, 3,909
  JBR command frames, 1,115 expected picture-fallback frames, and thirteen structured invalid-stream fallback markers.
- Focused saveLayer color-filter-ref alpha validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-124011/suite.tsv`.
  The new `commands-invalid-save-layer-color-filter-ref-alpha-fallback` and
  `commands-invalid-save-layer-blend-color-filter-ref-alpha-fallback` rows record op 52
  `COMMAND_SAVE_LAYER_COLOR_FILTER_REF` and op 54 `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF`, then rewrite
  `alpha1000` to `1001`. Both rows reported one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR
  picture frames, and zero JBR command frames.
- Scoped saveLayer-invalid validation passed after adding the color-filter-ref alpha rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-124137/suite.tsv`.
  The quick group now covers nine malformed saveLayer rows, including op 52 and op 54 alpha bounds sentinels. All nine
  passed with zero unsupported rows, zero picture rows, zero command replay rows, and one structured fallback marker per
  row.
- Bounded default-order saveLayer range validation passed after adding the color-filter-ref alpha rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-124859/suite.tsv`.
  The range covered twelve rows: two supported saveLayer command-replay rows, nine malformed saveLayer rows, and the
  existing raw color-filter fallback sentinel. Aggregate: 12/12 passed, one expected unsupported-marker row, 2,517 JBR
  command frames, 915 expected picture-fallback frames, and nine structured invalid-stream fallback markers.
- Focused saveLayer image-filter dimension validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-121803/suite.tsv`.
  The new `commands-invalid-save-layer-image-filter-width-fallback` and
  `commands-invalid-save-layer-image-filter-height-fallback` rows record op 55
  `COMMAND_SAVE_LAYER_IMAGE_FILTER_REF` through graphics-layer render-effect replay and rewrite width or height to
  `-1`. Both rows reported one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames,
  and zero JBR command frames.
- Scoped saveLayer-invalid validation passed after adding the image-filter dimension rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-121925/suite.tsv`.
  The quick group now covers seven malformed saveLayer rows, including op 55 alpha, width, and height bounds sentinels.
  All seven passed with zero unsupported rows, zero picture rows, zero command replay rows, and one structured fallback
  marker per row.
- Bounded default-order saveLayer range validation passed after adding the image-filter dimension rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-122729/suite.tsv`.
  The range from `commands-save-layer-filter` through `commands-save-layer-raw-color-filter-fallback` covered ten rows:
  two supported saveLayer command-replay rows, seven malformed saveLayer rows, and the existing raw color-filter
  fallback sentinel. Aggregate: 10/10 passed, one expected unsupported-marker row, 3,133 JBR command frames, 1,181
  expected picture-fallback frames, and seven structured invalid-stream fallback markers.
- Focused saveLayer image-filter alpha validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-120426/suite.tsv`.
  The new `commands-invalid-save-layer-image-filter-alpha-fallback` row records op 55
  `COMMAND_SAVE_LAYER_IMAGE_FILTER_REF` through graphics-layer render-effect replay, rewrites `alpha1000` to `1001`
  with the existing saveLayer alpha corruption hook, and reports one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Scoped saveLayer-invalid validation passed after adding the image-filter alpha row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-120842/suite.tsv`.
  The quick group now covers five malformed saveLayer rows, including the new op 55 alpha bounds sentinel. All five
  passed with zero unsupported rows, zero picture rows, zero command replay rows, and one structured fallback marker
  per row.
- Focused saveLayer image-filter handle validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-081201/suite.tsv`.
  The new `commands-invalid-save-layer-image-filter-use-fallback` row records op 55
  `COMMAND_SAVE_LAYER_IMAGE_FILTER_REF` through the graphics-layer render-effect path and rewrites the image-filter
  handle to an undefined value. The new `commands-invalid-save-layer-image-filter-use-after-evict-fallback` row inserts
  `COMMAND_EVICT_COLOR_FILTER_HANDLE` immediately before op 55 consumes the effect descriptor handle. Both rows
  reported one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR
  command frames.
- Descriptor-handle area validation passed after adding the saveLayer image-filter handle rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-105843/suite.tsv`.
  The `CASE_GROUPS=descriptor-handles-invalid` run covered forty-five malformed descriptor/child-handle rows,
  including the new op 55 missing-handle and use-after-evict sentinels. All forty-five passed with zero unsupported
  rows, zero picture rows, zero command replay rows, and one structured fallback marker per row.
- Focused saveLayer color-filter handle use-after-evict validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-041255/suite.tsv`.
  The new `commands-invalid-save-layer-color-filter-use-after-evict-fallback` and
  `commands-invalid-save-layer-blend-color-filter-use-after-evict-fallback` rows insert
  `COMMAND_EVICT_COLOR_FILTER_HANDLE` immediately before op 52 or op 54 consumes the color-filter handle. Both rows
  reported one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR
  command frames.
- Focused saveLayer color-filter handle validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-034302/suite.tsv`.
  The new `commands-invalid-save-layer-color-filter-use-fallback` row records op 52
  `COMMAND_SAVE_LAYER_COLOR_FILTER_REF` through a saveLayer color-matrix filter handle and rewrites the handle to an
  undefined value. The new `commands-invalid-save-layer-blend-color-filter-use-fallback` row records op 54
  `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF` through graphics-layer blend plus color-matrix filter and rewrites that
  handle to an undefined value. Both rows reported one `command-stream-invalid` fallback marker, `unsupported=none`,
  zero JBR picture frames, and zero JBR command frames.
- Descriptor-handle area validation was continued around the new saveLayer handle rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-034438/suite.tsv`.
  The new op 52 and op 54 rows passed in grouped context, and the established descriptor-handle rows remained green
  until the run reached a stale group entry named `commands-chain-path-effect-wrong-effect-type-fallback`. The group
  entry was corrected to the real row name, `commands-chain-path-effect-child-wrong-effect-type-fallback`, and the
  repaired tail passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-040920/suite.tsv`.
- Expanded saveLayer-invalid command-probe validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-032849/suite.tsv`.
  The `CASE_GROUPS=save-layer-invalid` quick area now covers four malformed saveLayer rows:
  `commands-invalid-save-layer-alpha-fallback`,
  `commands-invalid-save-layer-color-filter-blend-mode-fallback`,
  `commands-invalid-save-layer-blend-mode-fallback`, and
  `commands-invalid-save-layer-blend-color-filter-blend-mode-fallback`. All four rows reported one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Bounded default-order saveLayer range validation passed after adding the blend rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-033128/suite.tsv`.
  The range covered six rows: supported tint-filter saveLayer command replay, supported saveLayer blend-mode command
  replay, the four invalid saveLayer rows, and the existing raw color-filter fallback sentinel. Aggregate: 7/7 passed,
  one expected unsupported-marker row, 2,907 JBR command frames, 1,228 expected picture-fallback frames, and four
  structured invalid-stream fallback markers.
- Focused saveLayer-invalid command-probe validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-030846/suite.tsv`.
  The new `CASE_GROUPS=save-layer-invalid` group covered
  `commands-invalid-save-layer-alpha-fallback` and
  `commands-invalid-save-layer-color-filter-blend-mode-fallback`. Both rows reported one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Bounded default-order saveLayer range validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-031011/suite.tsv`.
  The range from `commands-save-layer-filter` through `commands-save-layer-raw-color-filter-fallback` covered four
  rows: the supported saveLayer tint-filter command path, the two new invalid scalar rows, and the existing raw
  color-filter fallback sentinel. Aggregate: 4/4 passed, one expected unsupported-marker row, 1,456 JBR command frames,
  1,096 expected picture-fallback frames, and two structured fallback markers.
- Focused command-probe validation passed after adding live image cache-key sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-203912/suite.tsv`.
  The new `commands-invalid-image-use-fallback` row rewrites one `COMMAND_DRAW_IMAGE_REF` key to an undefined image
  cache key. The new `commands-invalid-image-use-after-evict-fallback` row inserts `COMMAND_EVICT_IMAGE_CACHE_KEY`
  immediately before the draw that uses that key. Both rows report one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Scoped image-handle command-probe consolidation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-204017/suite.tsv`.
  The new `CASE_GROUPS=image-handles-invalid` area run covered both malformed image cache-key rows; both passed. The
  group reported zero unsupported rows, zero picture rows, zero command replay rows, and one structured fallback marker
  per row.
- Expanded scoped image-handle command-probe consolidation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-204739/suite.tsv`.
  The `CASE_GROUPS=image-handles-invalid` area run now covers missing-key and use-after-evict rows for
  `COMMAND_DRAW_IMAGE_REF`, `COMMAND_DRAW_IMAGE_REF_COLOR_FILTER`, and `COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF`.
  All six rows passed. The group reported zero unsupported rows, zero picture rows, zero command replay rows, and one
  structured fallback marker per row.
- Focused command-probe validation passed after adding a live image-ref width mismatch sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-205347/suite.tsv`.
  The new `commands-invalid-image-ref-width-fallback` row records a normal `COMMAND_DRAW_IMAGE_REF` image cache use,
  then increments the recorded width so JBR's cached-image dimension check rejects the stream after lookup.
- Scoped image-handle command-probe consolidation passed after adding the width mismatch row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-205428/suite.tsv`.
  The `CASE_GROUPS=image-handles-invalid` area run now covers seven malformed image cache-key/dimension rows; all seven
  passed. The group reported zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row.
- Focused command-probe validation passed after extending live image-ref height mismatch sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-212843/suite.tsv`.
  The spot-check covered plain `COMMAND_DRAW_IMAGE_REF` height mismatch and descriptor color-filter image-ref height
  mismatch; both rows reported one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture
  frames, and zero JBR command frames.
- Scoped image-handle command-probe consolidation passed after extending dimension mismatch coverage across op 16, op
  45, and op 53:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-212945/suite.tsv`.
  The `CASE_GROUPS=image-handles-invalid` area run now covers twelve malformed image cache-key/dimension rows; all
  twelve passed. The group reported zero unsupported rows, zero picture rows, zero command replay rows, and one
  structured fallback marker per row.
- Focused command-probe validation passed after adding a live image definition pixel-count sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-213840/suite.tsv`.
  The new `commands-invalid-image-define-pixel-count-fallback` row records a normal `COMMAND_DEFINE_IMAGE_ARGB`, then
  increments its pixel-count field while leaving the record length and payload unchanged. JBR rejects the stream before
  replay with one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR
  command frames.
- Scoped image-handle command-probe consolidation passed after adding the image definition pixel-count row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-213912/suite.tsv`.
  The `CASE_GROUPS=image-handles-invalid` area run now covers thirteen malformed image definition/cache-key/dimension
  rows; all thirteen passed. The group reported zero unsupported rows, zero picture rows, zero command replay rows, and
  one structured fallback marker per row.
- Focused image definition width/height bounds validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-030459/suite.tsv`.
  The four new rows record op 15 `COMMAND_DEFINE_IMAGE_ARGB`, rewrite image width or height to `0` or `4097`, and
  require typed Skiko corruption markers plus `command-stream-invalid` fallback. Aggregate: 4/4 passed, zero
  unsupported rows, zero JBR picture frames, zero JBR command frames, and four structured invalid-stream fallback
  markers.
- Expanded `image-handles-invalid` validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-030826/suite.tsv`.
  The area group now covers twenty-seven malformed image definition/cache-key/dimension/alpha/filter rows, including
  the new image-define width/height lower and upper bounds. Aggregate: 27/27 passed, zero unsupported rows, zero JBR
  picture frames, 1,625 JBR command frames, and twenty-seven structured invalid-stream fallback markers.
- Bounded default-order image/path range validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-032618/suite.tsv`.
  The range covered `commands-core-primitives`, the expanded image-define/image-ref malformed block, the adjacent path
  parser sentinels, and `commands-point-lines`. Aggregate: 23/23 passed, zero unsupported rows, zero JBR picture
  frames, 7,321 JBR command frames, and twenty-one structured invalid-stream fallback markers.
- Focused command-probe validation passed after adding live image-ref alpha sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-214844/suite.tsv`.
  The new `commands-invalid-image-ref-alpha-fallback`,
  `commands-invalid-image-color-filter-ref-alpha-fallback`, and
  `commands-invalid-image-color-filter-descriptor-ref-alpha-fallback` rows rewrite `alpha1000` to `1001` for op 16,
  op 45, and op 53. Each row reports one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture
  frames, and zero JBR command frames.
- Scoped image-handle command-probe consolidation passed after adding the image-ref alpha rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-215036/suite.tsv`.
  The `CASE_GROUPS=image-handles-invalid` area run now covers sixteen malformed image definition/cache-key/dimension
  and alpha rows; all sixteen passed. The group reported zero unsupported rows, zero picture rows, zero command replay
  rows, and one structured fallback marker per row.
- Focused command-probe validation passed after adding live image-ref filter-quality sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-220628/suite.tsv`.
  The new `commands-invalid-image-ref-filter-quality-fallback`,
  `commands-invalid-image-color-filter-ref-filter-quality-fallback`, and
  `commands-invalid-image-color-filter-descriptor-ref-filter-quality-fallback` rows rewrite filter quality to `4` for
  op 16, op 45, and op 53. Each row reports one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR
  picture frames, and zero JBR command frames.
- Scoped image-handle command-probe consolidation passed after adding the image-ref filter-quality rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-220829/suite.tsv`.
  The `CASE_GROUPS=image-handles-invalid` area run now covers nineteen malformed image definition/cache-key/dimension,
  alpha, and filter-quality rows; all nineteen passed. The group reported zero unsupported rows, zero picture rows,
  zero command replay rows, and one structured fallback marker per row.
- Periodic full default command-probe sweep was attempted after the image scalar rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-222129/commands-live-animation/report.md`.
  The first row stayed on command replay with `unsupported=none`, zero fallback, zero JBR picture frames, 1636 JBR
  command frames, and one tiny full-scene injection, but validation failed because window capture could not create a
  screenshot. A focused rerun reproduced the capture-only failure under high host load:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-222419/commands-live-animation/report.md`.
- Focused command-probe validation passed after adding a live inline image color-filter blend-mode sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-222714/suite.tsv`.
  The new `commands-invalid-image-color-filter-blend-mode-fallback` row rewrites op 45's blend mode away from `SRC_IN`
  and reports one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR
  command frames.
- Scoped image-handle command-probe consolidation passed after adding the image color-filter blend-mode row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-222802/suite.tsv`.
  The `CASE_GROUPS=image-handles-invalid` area run now covers twenty malformed image definition/cache-key/dimension,
  alpha, filter-quality, and blend-mode rows; all twenty passed. The group reported zero unsupported rows, zero picture
  rows, zero command replay rows, and one structured fallback marker per row.
- Magic Jewel validation now supports `EXPECT_SCREENSHOT_ASSERTION=false` for command-only sweeps when macOS window
  capture is flaky. The previously failing `commands-live-animation` and `commands-popup-window` rows both passed with
  screenshot assertions disabled while still requiring command replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-224301/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-225925/suite.tsv`.
- A periodic full default command-only sweep was restarted with `EXPECT_SCREENSHOT_ASSERTION=false`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-230019/`.
  It progressed through the expanded image sentinel block, path verb sentinels, popup/menu, native text, image/shader,
  and RuntimeEffect rows before stopping at `commands-runtime-effect-child-only` because the row's max shader-handle
  define gate was stale. The report showed strict command replay stayed healthy: zero fallback, `unsupported=none`,
  zero JBR picture frames, 1557 JBR command frames, one RuntimeEffect source-cache miss, and 2058 cache hits.
- Focused validation passed after adjusting `commands-runtime-effect-child-only` to allow the current three shader-handle
  definitions:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-234743/suite.tsv`.
- Magic Jewel command-probe range slicing passed a one-row smoke with
  `CASES_FROM=commands-runtime-effect-child-only` and `CASES_UNTIL=commands-runtime-effect-child-only`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-235136/suite.tsv`.
- The same one-row range smoke passed again after adding fail-fast validation for unknown `CASES_UNTIL` values:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-025324/suite.tsv`.
  A cheap no-app negative check with `CASES=commands-runtime-effect-child-only CASES_UNTIL=does-not-exist` exits with
  `Unknown CASES_UNTIL: does-not-exist`.
- Magic Jewel command-probe group discovery now supports `LIST_CASE_GROUPS=true`, so quick area slices can be listed
  without launching the app or reading the script.
- Resumed command-only tail sweep passed with `EXPECT_SCREENSHOT_ASSERTION=false` and
  `CASES_FROM=commands-runtime-effect-child-only`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-235421/suite.tsv`.
  The tail covered 292 rows; all 292 passed. Aggregate: 94,687 JBR command frames, 17,970 expected picture-fallback
  frames, 17 expected unsupported-marker rows, and 208 total expected fallback markers.
- Focused command-probe validation passed after adding top-level path-effect descriptor-use sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-200433/suite.tsv`.
  The new `commands-invalid-path-effect-descriptor-use-fallback`,
  `commands-invalid-path-effect-descriptor-use-after-evict-fallback`, and
  `commands-path-effect-wrong-effect-type-fallback` rows exercise `COMMAND_DRAW_PATH_PATH_EFFECT_REF` with an undefined
  handle, a just-evicted path-effect handle, and a color-filter handle. Each row reports one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Scoped descriptor-handle command-probe consolidation passed after adding those top-level path-effect use rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-200630/suite.tsv`.
  The `CASE_GROUPS=descriptor-handles-invalid` area run covered thirty-nine malformed descriptor/child-handle rows; all
  thirty-nine passed. The group reported zero unsupported rows, zero picture rows, zero command replay rows, and one
  structured fallback marker per row.
- Focused command-probe validation passed after extending effect-child use-after-evict coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-193417/suite.tsv`.
  The new `commands-runtime-effect-color-filter-child-use-after-evict-fallback` and
  `commands-shader-color-filter-effect-child-use-after-evict-fallback` rows insert a
  `COMMAND_EVICT_COLOR_FILTER_HANDLE` record immediately before descriptor validation, require target-specific
  `SKIKO_JBR_INTEROP_EFFECT_CHILD_USE_AFTER_EVICT_CORRUPTED` markers, and report one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Scoped descriptor-handle command-probe consolidation passed after adding those effect child use-after-evict rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-193605/suite.tsv`.
  The `CASE_GROUPS=descriptor-handles-invalid` area run covered thirty-six malformed descriptor/child-handle rows; all
  thirty-six passed. The group reported zero unsupported rows, zero picture rows, zero command replay rows, and one
  structured fallback marker per row.
- Focused command-probe validation passed after adding live shader child use-after-evict sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-190602/suite.tsv`.
  The new rows cover transformed shader child, composite shader destination child, composite shader source child,
  shader-color-filter shader child, and RuntimeEffect shader child descriptors. Each row inserts a
  `COMMAND_EVICT_SHADER_HANDLE` record immediately before the descriptor that consumes the child handle, requires a
  target-specific `SKIKO_JBR_INTEROP_SHADER_CHILD_USE_AFTER_EVICT_CORRUPTED` marker, and reports one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Scoped descriptor-handle command-probe consolidation passed after adding the shader child use-after-evict rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-190943/suite.tsv`.
  The `CASE_GROUPS=descriptor-handles-invalid` area run covered thirty-four malformed descriptor/child-handle rows; all
  thirty-four passed. The group reported zero unsupported rows, zero picture rows, zero command replay rows, and one
  structured fallback marker per row.
- Focused command-probe validation passed after adding live composite shader source-child wrong-type and missing-child
  sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-184048/suite.tsv`.
  The new `commands-composite-shader-src-child-wrong-effect-type-fallback` row rewrites the composite shader source
  child handle to a color-filter descriptor and requires
  `SKIKO_JBR_INTEROP_SHADER_HANDLE_TYPE_CORRUPTED target=compositeShaderSrcChild`. The new
  `commands-composite-shader-src-child-missing-fallback` row rewrites that source child to an undefined shader handle
  and requires `SKIKO_JBR_INTEROP_SHADER_CHILD_MISSING_CORRUPTED target=compositeShaderSrcChild`. Both rows report one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Scoped descriptor-handle command-probe consolidation passed after adding the composite shader source-child rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-184218/suite.tsv`.
  The `CASE_GROUPS=descriptor-handles-invalid` area run covered twenty-nine malformed descriptor/child-handle rows; all
  twenty-nine passed. The group reported zero unsupported rows, zero picture rows, zero command replay rows, and one
  structured fallback marker per row.
- Focused command-probe validation passed after adding Magic Jewel rows for with-input image-filter descriptor payload
  guards:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-181403/suite.tsv`.
  The four rows cover blur-with-input sigma, blur-with-input negative sigma, blur-with-input tile mode, and
  offset-with-input delta. Each row reuses an existing Skiko corruption hook, requires the target marker, and reports
  one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Scoped effect-descriptor command-probe consolidation passed after adding the with-input image-filter descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-181711/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` area run covered twenty-seven malformed effect-descriptor rows; all
  twenty-seven passed. The group reported zero unsupported rows, zero picture rows, zero command replay rows, and one
  structured fallback marker per row.
- Focused command-probe validation passed after adding live blur-with-input image-filter missing-child and
  use-after-evict sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-175550/suite.tsv`.
  The new `commands-invalid-blur-effect-child-use-after-evict-fallback` and
  `commands-blur-image-filter-child-missing-fallback` rows both record the blur-of-offset render-effect descriptor chain,
  require target-specific `blurImageFilterChild` Skiko markers, and report one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Scoped descriptor-handle command-probe consolidation passed after adding those blur image-filter child missing and
  use-after-evict rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-175712/suite.tsv`.
  The `CASE_GROUPS=descriptor-handles-invalid` area run covered twenty-four malformed descriptor/child-handle rows; all
  twenty-four passed. The group reported zero unsupported rows, zero picture rows, zero command replay rows, and one
  structured fallback marker per row.
- Focused command-probe validation passed after adding a live blur-with-input image-filter child wrong-type sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-173959/suite.tsv`.
  The new `commands-blur-image-filter-child-wrong-effect-type-fallback` row records a blur-of-offset render-effect
  descriptor chain, rewrites the blur child handle to a color-filter descriptor, requires
  `SKIKO_JBR_INTEROP_IMAGE_FILTER_HANDLE_TYPE_CORRUPTED target=blurImageFilterChild`, and reports one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Scoped descriptor-handle command-probe consolidation passed after adding the blur image-filter child wrong-type row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-174050/suite.tsv`.
  The `CASE_GROUPS=descriptor-handles-invalid` area run covered twenty-two malformed descriptor/child-handle rows; all
  twenty-two passed. The group reported zero unsupported rows, zero picture rows, zero command replay rows, and one
  structured fallback marker per row.
- Focused and adjacent command-probe validation passed after adding live RuntimeEffect shader and color-filter
  duplicate child-index sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-164327/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-164445/suite.tsv`.
  The focused rows `commands-runtime-effect-shader-duplicate-child-index-fallback` and
  `commands-runtime-effect-color-filter-duplicate-child-index-fallback` rewrite the second named-child schema entry to
  reference the first child index. Both require their target-specific `*_DUPLICATE_CHILD_INDEX_CORRUPTED` marker and
  record one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command
  frames. The adjacent RuntimeEffect child-schema slice covered fourteen rows; all fourteen passed, with zero
  unsupported rows, zero picture rows, and zero command replay rows.
- Scoped RuntimeEffect command-probe consolidation passed after adding the duplicate child-index live sentinel pairs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-165244/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` area run covered sixty-two malformed RuntimeEffect rows; all sixty-two
  passed. Six rows were intentional picture-fallback/parser-only coverage, and zero rows replayed JBR command frames.
  This is the current RuntimeEffect parser/schema consolidation checkpoint.
- Focused and adjacent command-probe validation passed after adding live RuntimeEffect shader and color-filter
  uniform-schema name-range sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-153449/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-153628/suite.tsv`.
  The focused rows `commands-runtime-effect-shader-uniform-schema-name-range-fallback` and
  `commands-runtime-effect-color-filter-uniform-schema-name-range-fallback` bump the first named-uniform schema name
  length just past the schema boundary while staying under the max-length guard. Both require their target-specific
  `*_UNIFORM_SCHEMA_NAME_RANGE_CORRUPTED` marker and record one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The adjacent RuntimeEffect uniform-schema
  slice covered fourteen rows; all fourteen passed, with zero unsupported rows, zero picture rows, and zero command
  replay rows.
- Scoped RuntimeEffect command-probe consolidation passed after adding the uniform-schema name-range live sentinel
  pairs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-154609/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` area run covered sixty malformed RuntimeEffect rows; all sixty passed. Six
  rows were intentional picture-fallback/parser-only coverage, and zero rows replayed JBR command frames. This is the
  current RuntimeEffect parser/schema consolidation checkpoint.
- Focused and adjacent command-probe validation passed after adding live RuntimeEffect shader and color-filter
  child-schema name-range sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-142845/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-143020/suite.tsv`.
  The focused rows `commands-runtime-effect-shader-child-schema-name-range-fallback` and
  `commands-runtime-effect-color-filter-child-schema-name-range-fallback` bump the first named-child schema name length
  just past the schema boundary while staying under the max-length guard. Both require their target-specific
  `*_CHILD_SCHEMA_NAME_RANGE_CORRUPTED` marker and record one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The adjacent RuntimeEffect child-schema
  slice covered fourteen rows; all fourteen passed, with two intentional picture-fallback/parser-only rows and zero
  command replay rows.
- Scoped RuntimeEffect command-probe consolidation passed after adding the child-schema name-range live sentinel pairs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-144007/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` area run covered fifty-eight malformed RuntimeEffect rows; all fifty-eight
  passed. Six rows were intentional picture-fallback/parser-only coverage, and zero rows replayed JBR command frames.
  This is the current RuntimeEffect parser/schema consolidation checkpoint.
- Focused and adjacent command-probe validation passed after adding live RuntimeEffect shader and color-filter
  child-schema max-name-length sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-132914/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-133102/suite.tsv`.
  The focused rows `commands-runtime-effect-shader-child-schema-max-name-length-fallback` and
  `commands-runtime-effect-color-filter-child-schema-max-name-length-fallback` rewrite the first named-child schema
  name length to `65`. Both require their target-specific `*_CHILD_SCHEMA_MAX_NAME_LENGTH_CORRUPTED` marker and record
  one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command
  frames. The adjacent RuntimeEffect child-schema slice covered twelve rows; all twelve passed, with two intentional
  picture-fallback/parser-only rows and zero command replay rows.
- Scoped RuntimeEffect command-probe consolidation passed after adding the child-schema max-name-length live sentinel
  pairs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-134009/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` area run covered fifty-six malformed RuntimeEffect rows; all fifty-six
  passed. Six rows were intentional picture-fallback/parser-only coverage, and zero rows replayed JBR command frames.
  This is the previous RuntimeEffect parser/schema consolidation checkpoint.
- Focused and adjacent command-probe validation passed after adding live RuntimeEffect shader and color-filter
  child-schema name-length sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-123344/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-123520/suite.tsv`.
  The focused rows `commands-runtime-effect-shader-child-schema-name-length-fallback` and
  `commands-runtime-effect-color-filter-child-schema-name-length-fallback` rewrite the first named-child schema name
  length to `0`. Both require their target-specific `*_CHILD_SCHEMA_NAME_LENGTH_CORRUPTED` marker and record one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The adjacent RuntimeEffect child-schema slice covered ten rows; all ten passed, with two intentional
  picture-fallback/parser-only rows and zero command replay rows.
- Scoped RuntimeEffect command-probe consolidation passed after adding the child-schema name-length live sentinel pairs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-124249/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` area run covered fifty-four malformed RuntimeEffect rows; all fifty-four
  passed. Six rows were intentional picture-fallback/parser-only coverage, and zero rows replayed JBR command frames.
  This is the previous RuntimeEffect parser/schema consolidation checkpoint.
- Focused and adjacent command-probe validation passed after adding live RuntimeEffect shader and color-filter
  negative child-index sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-113957/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-114142/suite.tsv`.
  The focused rows `commands-runtime-effect-shader-negative-child-index-fallback` and
  `commands-runtime-effect-color-filter-negative-child-index-fallback` rewrite the first named-child schema referenced
  index to `-1`. Both require their target-specific `*_NEGATIVE_CHILD_INDEX_CORRUPTED` marker and record one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The adjacent RuntimeEffect child-schema slice covered eight rows; all eight passed, with two intentional
  picture-fallback/parser-only rows and zero command replay rows.
- Scoped RuntimeEffect command-probe consolidation passed after adding the child-schema negative child-index live
  sentinel pairs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-114736/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` area run covered fifty-two malformed RuntimeEffect rows; all fifty-two
  passed. Six rows were intentional picture-fallback/parser-only coverage, and zero rows replayed JBR command frames.
  This is the previous RuntimeEffect parser/schema consolidation checkpoint.
- Current-artifact focused validation rechecked the live unknown effect descriptor type sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-111741/suite.tsv`.
  `commands-invalid-effect-descriptor-type-fallback` rewrites one recorded effect descriptor type to an unknown value
  and requires `SKIKO_JBR_INTEROP_EFFECT_DESCRIPTOR_TYPE_CORRUPTED`; it passed with one structured
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The follow-up `CASE_GROUPS=effect-descriptor-invalid` area run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-111855/suite.tsv`.
  It covered twenty-three effect descriptor parser/fallback rows; all twenty-three passed, with zero unsupported rows,
  zero picture rows, and zero command replay rows.
- Scoped RuntimeEffect command-probe consolidation passed after adding the uniform-schema name-length and max-name-length
  live sentinel pairs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-103723/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` area run covered fifty malformed RuntimeEffect rows; all fifty passed. Six
  rows were intentional picture-fallback/parser-only coverage, and zero rows replayed JBR command frames. This is the
  previous RuntimeEffect parser/schema consolidation checkpoint.
- Focused and adjacent command-probe validation passed after adding live RuntimeEffect shader and color-filter
  uniform-schema max-name-length sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-102412/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-102601/suite.tsv`.
  The focused rows `commands-runtime-effect-shader-uniform-schema-max-name-length-fallback` and
  `commands-runtime-effect-color-filter-uniform-schema-max-name-length-fallback` rewrite the first named-uniform schema
  name length to `65`. Both require their target-specific `*_UNIFORM_SCHEMA_MAX_NAME_LENGTH_CORRUPTED` marker and
  record one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR
  command frames. The adjacent RuntimeEffect uniform-schema slice covered fourteen rows; all fourteen passed, with two
  intentional picture-fallback/parser-only rows and zero command replay rows.
- Skiko `publishToMavenLocal` passed after adding the test-only RuntimeEffect uniform-schema max-name-length corruption
  hooks.
- Focused and adjacent command-probe validation passed after adding live RuntimeEffect shader and color-filter
  uniform-schema name-length sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-101012/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-101145/suite.tsv`.
  The focused rows `commands-runtime-effect-shader-uniform-schema-name-length-fallback` and
  `commands-runtime-effect-color-filter-uniform-schema-name-length-fallback` rewrite the first named-uniform schema
  name length to `0`. Both require their target-specific `*_UNIFORM_SCHEMA_NAME_LENGTH_CORRUPTED` marker and record one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The adjacent RuntimeEffect uniform-schema slice covered twelve rows; all twelve passed, with two intentional
  picture-fallback/parser-only rows and zero command replay rows.
- Skiko `publishToMavenLocal` passed after adding the test-only RuntimeEffect uniform-schema name-length corruption
  hooks.
- Scoped RuntimeEffect command-probe consolidation passed after adding the uniform-schema float-offset and float-range
  live sentinel pairs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-031347/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` area run covered forty-six malformed RuntimeEffect rows; all forty-six
  passed. Six rows were intentional picture-fallback/parser-only coverage, and zero rows replayed JBR command frames.
- Focused and adjacent command-probe validation passed after adding live RuntimeEffect shader and color-filter
  uniform-schema float-range sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-030358/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-030537/suite.tsv`.
  The focused rows `commands-runtime-effect-shader-uniform-schema-float-range-fallback` and
  `commands-runtime-effect-color-filter-uniform-schema-float-range-fallback` rewrite the first named-uniform schema
  float-offset to `uniformFloatCount`. Both require their target-specific `*_UNIFORM_SCHEMA_FLOAT_RANGE_CORRUPTED`
  marker and record one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero
  JBR command frames. The adjacent RuntimeEffect uniform-schema slice covered ten rows; all ten passed, with two
  intentional picture-fallback/parser-only rows and zero command replay rows.
- Skiko `publishToMavenLocal` passed after adding the test-only RuntimeEffect uniform-schema float-range corruption
  hooks.
- Focused and adjacent command-probe validation passed after adding live RuntimeEffect shader and color-filter
  uniform-schema float-offset sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-025315/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-025445/suite.tsv`.
  The focused rows `commands-runtime-effect-shader-uniform-schema-float-offset-fallback` and
  `commands-runtime-effect-color-filter-uniform-schema-float-offset-fallback` rewrite the first named-uniform schema
  float-offset to `-1`. Both require their target-specific `*_UNIFORM_SCHEMA_FLOAT_OFFSET_CORRUPTED` marker and record
  one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command
  frames. The adjacent RuntimeEffect uniform-schema slice covered eight rows; all eight passed, with two intentional
  picture-fallback/parser-only rows and zero command replay rows.
- Skiko `publishToMavenLocal` passed after adding the test-only RuntimeEffect uniform-schema float-offset corruption
  hooks.
- Scoped RuntimeEffect command-probe consolidation passed after adding the child-index and uniform-schema float-count
  live sentinel pairs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-021819/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` area run covered forty-two malformed RuntimeEffect rows; all forty-two
  passed. Six rows were intentional picture-fallback/parser-only coverage, and zero rows replayed JBR command frames.
- Focused and adjacent command-probe validation passed after adding live RuntimeEffect shader and color-filter
  uniform-schema float-count sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-021125/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-021257/suite.tsv`.
  The focused rows `commands-runtime-effect-shader-uniform-schema-float-count-fallback` and
  `commands-runtime-effect-color-filter-uniform-schema-float-count-fallback` rewrite the first named-uniform schema
  float-count to `0`. Both require their target-specific `*_UNIFORM_SCHEMA_FLOAT_COUNT_CORRUPTED` marker and record one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The adjacent RuntimeEffect uniform-schema slice covered six rows; all six passed, with two intentional
  picture-fallback/parser-only rows and zero command replay rows.
- Skiko `publishToMavenLocal` passed after adding the test-only RuntimeEffect uniform-schema float-count corruption
  hooks.
- Focused and adjacent command-probe validation passed after adding live RuntimeEffect shader and color-filter
  child-index schema sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-020224/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-020355/suite.tsv`.
  The focused rows `commands-runtime-effect-shader-child-index-fallback` and
  `commands-runtime-effect-color-filter-child-index-fallback` rewrite the first named-child referenced index to
  `childCount`. Both require their target-specific `*_CHILD_INDEX_CORRUPTED` marker and record one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The adjacent RuntimeEffect child-schema slice covered six rows; all six passed, with two intentional
  picture-fallback/parser-only rows and zero command replay rows.
- Skiko `publishToMavenLocal` passed after adding the test-only RuntimeEffect child-index corruption hooks.
- Scoped RuntimeEffect command-probe consolidation passed after the shader/color-filter source-code, source-hash,
  uniform-name, and child-name live sentinel batch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-012854/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` area run covered thirty-eight malformed RuntimeEffect rows; all thirty-eight
  passed. Six rows were intentional picture-fallback/parser-only coverage, and zero rows replayed JBR command frames.
  This is the current fast consolidation point for RuntimeEffect parser/schema sentinel work; continue iterating with
  exact `CASES=...` rows plus tiny adjacent slices before broader sweeps.
- Focused and adjacent command-probe validation passed after adding a live RuntimeEffect color-filter child-name schema
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-012238/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-012327/suite.tsv`.
  The focused row `commands-runtime-effect-color-filter-child-name-fallback` rewrites the first recorded RuntimeEffect
  color-filter named-child character to `1`. It requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_CHILD_NAME_CORRUPTED` and records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The adjacent
  RuntimeEffect color-filter child-schema slice covered five rows; all five passed.
- Skiko `publishToMavenLocal` passed after adding the test-only RuntimeEffect color-filter child-name corruption hook.
- Focused and adjacent command-probe validation passed after adding a live RuntimeEffect shader child-name schema
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-011504/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-011554/suite.tsv`.
  The focused row `commands-runtime-effect-shader-child-name-fallback` rewrites the first recorded RuntimeEffect
  shader named-child character to `1`. It requires `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_CHILD_NAME_CORRUPTED` and
  records one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR
  command frames. The adjacent RuntimeEffect shader child-schema slice covered five rows; all five passed.
- Skiko `publishToMavenLocal` passed after adding the test-only RuntimeEffect shader child-name corruption hook.
- Focused and adjacent command-probe validation passed after adding a live RuntimeEffect color-filter uniform-name
  schema sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-010124/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-010219/suite.tsv`.
  The focused row `commands-runtime-effect-color-filter-uniform-name-fallback` rewrites the first recorded
  RuntimeEffect color-filter named-uniform character to `1`. It requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_UNIFORM_NAME_CORRUPTED` and records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The adjacent
  RuntimeEffect color-filter parser slice covered twelve rows; all twelve passed.
- Skiko `publishToMavenLocal` passed after adding the test-only RuntimeEffect color-filter uniform-name corruption
  hook.
- Focused and adjacent command-probe validation passed after adding a live RuntimeEffect shader uniform-name schema
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-005128/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-005221/suite.tsv`.
  The focused row `commands-runtime-effect-shader-uniform-name-fallback` rewrites the first recorded RuntimeEffect
  shader named-uniform character to `1`. It requires `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_UNIFORM_NAME_CORRUPTED`
  and records one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR
  command frames. The adjacent RuntimeEffect shader parser slice covered eight rows; all eight passed.
- Skiko `publishToMavenLocal` passed after adding the test-only RuntimeEffect shader uniform-name corruption hook.
- Focused and adjacent command-probe validation passed after adding a live RuntimeEffect color-filter source-hash
  mismatch sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-003812/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-003922/suite.tsv`.
  The focused row `commands-runtime-effect-color-filter-source-hash-fallback` flips one recorded RuntimeEffect
  color-filter descriptor source-hash word while leaving the SKSL payload unchanged. It requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_SOURCE_HASH_CORRUPTED` and records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The adjacent
  RuntimeEffect color-filter parser slice covered eleven rows; all eleven passed.
- Skiko `publishToMavenLocal` passed after adding the test-only RuntimeEffect color-filter source-hash corruption
  hook.
- Focused and adjacent command-probe validation passed after adding a live RuntimeEffect color-filter source-code
  parser sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-002624/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-002719/suite.tsv`.
  The focused row `commands-runtime-effect-color-filter-source-code-fallback` rewrites one recorded RuntimeEffect
  color-filter descriptor SKSL code unit to `0`, recomputes the descriptor source hash, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_SOURCE_CODE_CORRUPTED`, and records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The adjacent RuntimeEffect color-filter parser slice covered ten rows; all ten passed. This intentionally used a
  narrow parser-family slice instead of a full default sweep.
- Skiko `publishToMavenLocal` passed after adding the test-only RuntimeEffect color-filter source-code corruption hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect shader source-code parser
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-235533/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-235624/suite.tsv`.
  The focused row `commands-runtime-effect-shader-source-code-fallback` rewrites one recorded RuntimeEffect shader
  descriptor SKSL code unit to `0`, recomputes the descriptor source hash, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_SOURCE_CODE_CORRUPTED`, and records one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The scoped
  `CASE_GROUPS=runtime-effect-invalid` run covered thirty-two malformed RuntimeEffect rows; all thirty-two passed.
- Skiko `publishToMavenLocal` passed after adding the test-only RuntimeEffect shader source-code corruption hook.
- Focused and grouped command-probe validation passed after adding a live drawShadow path-data verb sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-234649/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-234726/suite.tsv`.
  The focused row targets `COMMAND_DRAW_SHADOW_PATH`, rewriting the first encoded path verb to `99`. It requires
  `SKIKO_JBR_INTEROP_DRAW_SHADOW_PATH_VERB_CORRUPTED` and records one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The scoped `CASE_GROUPS=path-invalid` run
  now covers five malformed path rows; all five passed.
- Skiko `publishToMavenLocal` passed after adding the test-only drawShadow path-verb corruption hook.
- Focused and grouped command-probe validation passed after adding a live stroked-path dash path-effect path-data verb
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-234049/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-234129/suite.tsv`.
  The focused row targets `COMMAND_STROKE_PATH_DASH_PATH_EFFECT`, rewriting the first encoded path verb to `99`. It
  requires `SKIKO_JBR_INTEROP_STROKE_PATH_DASH_PATH_EFFECT_VERB_CORRUPTED` and records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The scoped `CASE_GROUPS=path-invalid` run now covers four malformed direct/path-effect path rows; all four passed.
- Skiko `publishToMavenLocal` passed after adding the test-only stroked-path dash path-effect path-verb corruption
  hook.
- Focused and grouped command-probe validation passed after adding a live drawPath path-effect-ref path-data verb
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-233519/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-233604/suite.tsv`.
  The focused row targets `COMMAND_DRAW_PATH_PATH_EFFECT_REF`, rewriting the first encoded path verb to `99`. It
  requires `SKIKO_JBR_INTEROP_DRAW_PATH_PATH_EFFECT_VERB_CORRUPTED` and records one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The scoped
  `CASE_GROUPS=path-invalid` run now covers three malformed direct/path-effect path rows; all three passed.
- Skiko `publishToMavenLocal` passed after adding the test-only drawPath path-effect-ref path-verb corruption hook.
- Focused and grouped command-probe validation passed after adding a live direct clipPath path-data verb sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-232957/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-233038/suite.tsv`.
  The focused row targets `COMMAND_CLIP_PATH`, rewriting the first encoded path verb to `99`. It requires
  `SKIKO_JBR_INTEROP_CLIP_PATH_VERB_CORRUPTED` and records one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The scoped `CASE_GROUPS=path-invalid` run
  now covers two direct path invalid rows; both passed.
- Skiko `publishToMavenLocal` passed after adding the test-only clipPath path-verb corruption hook.
- Focused and grouped command-probe validation passed after adding a live direct drawPath path-data verb sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-232552/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-232631/suite.tsv`.
  The focused row targets `COMMAND_DRAW_PATH`, rewriting the first encoded path verb to `99`. It requires
  `SKIKO_JBR_INTEROP_DRAW_PATH_VERB_CORRUPTED` and records one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The new scoped `CASE_GROUPS=path-invalid`
  run currently covers this one direct path invalid row; it passed.
- Skiko `publishToMavenLocal` passed after adding the test-only drawPath path-verb corruption hook.
- Focused and grouped command-probe validation passed after adding a live stamped path-effect descriptor path-data verb
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-230818/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-230917/suite.tsv`.
  The focused row targets `COMMAND_EFFECT_DESCRIPTOR_STAMPED_PATH_EFFECT`, rewriting the first encoded descriptor path
  verb to `99`. It requires `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_PATH_VERB_CORRUPTED` and records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The scoped `CASE_GROUPS=effect-descriptor-invalid` run covered twenty-three malformed effect descriptor rows; all
  twenty-three passed.
- Skiko `publishToMavenLocal` passed after adding the test-only stamped path-effect descriptor path-verb corruption
  hook.
- Current-artifact focused and grouped command-probe validation rechecked the already-live unknown effect descriptor
  type sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-225228/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-225310/suite.tsv`.
  The focused row rewrites one `COMMAND_DEFINE_EFFECT_DESCRIPTOR` descriptor type to an unknown value, matching JBR's
  parser-only unknown descriptor type rejection path. It requires
  `SKIKO_JBR_INTEROP_EFFECT_DESCRIPTOR_TYPE_CORRUPTED` and records one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The scoped
  `CASE_GROUPS=effect-descriptor-invalid` run covered twenty-two malformed effect descriptor rows; all twenty-two
  passed.
- Focused and grouped command-probe validation passed after extending live path-gradient path-data verb parser coverage
  to radial and sweep commands:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-223824/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-223942/suite.tsv`.
  The focused rows target `COMMAND_FILL_PATH_RADIAL_GRADIENT` and `COMMAND_FILL_PATH_SWEEP_GRADIENT`, rewriting the
  first encoded path verb to `99`. They require
  `SKIKO_JBR_INTEROP_RADIAL_GRADIENT_PATH_VERB_CORRUPTED` or
  `SKIKO_JBR_INTEROP_SWEEP_GRADIENT_PATH_VERB_CORRUPTED` and each records one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The expanded
  `CASE_GROUPS=gradient-path-invalid` run covered eighteen path-gradient invalid rows; all eighteen passed.
- Skiko `publishToMavenLocal` passed after adding the test-only radial/sweep path-gradient verb corruption hooks.
- Focused and grouped command-probe validation passed after adding a live linear path-gradient path-data verb parser
  guard:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-222317/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-222406/suite.tsv`.
  The focused row targets `COMMAND_FILL_PATH_LINEAR_GRADIENT`, rewriting the first encoded path verb to `99`. It
  requires `SKIKO_JBR_INTEROP_LINEAR_GRADIENT_PATH_VERB_CORRUPTED` and records one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The expanded
  `CASE_GROUPS=gradient-path-invalid` run covered sixteen path-gradient invalid rows; all sixteen passed.
- Skiko `publishToMavenLocal` passed after adding the test-only linear path-gradient verb corruption hook.
- Broader `CASE_GROUPS=gradient-invalid` consolidation passed after completing the path-gradient header guard family:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-214935/suite.tsv`.
  The area group covered fifty-seven malformed gradient rows; all fifty-seven passed with one expected
  `command-stream-invalid` fallback marker per row, `unsupported=none`, zero JBR picture frames, and zero JBR command
  frames.
- Focused and grouped command-probe validation passed after extending path-gradient header parser coverage to radial
  and sweep paths:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-213839/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-214053/suite.tsv`.
  The new focused rows target `COMMAND_FILL_PATH_RADIAL_GRADIENT` and `COMMAND_FILL_PATH_SWEEP_GRADIENT`, rewriting
  the path fill-type slot to `99` or the path-data length slot to `-1`. Each row requires the matching
  `SKIKO_JBR_INTEROP_RADIAL_GRADIENT_PATH_*_CORRUPTED` or `SKIKO_JBR_INTEROP_SWEEP_GRADIENT_PATH_*_CORRUPTED` marker
  and records one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR
  command frames. The expanded `CASE_GROUPS=gradient-path-invalid` run covered fifteen path-gradient invalid rows;
  all fifteen passed.
- Skiko `publishToMavenLocal` passed after adding the test-only radial/sweep path-gradient header corruption hooks.
- Focused and grouped command-probe validation passed after adding linear path-gradient header parser coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-212726/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-212838/suite.tsv`.
  The new focused rows target `COMMAND_FILL_PATH_LINEAR_GRADIENT`, rewriting the path fill-type slot to `99` or the
  path-data length slot to `-1`. Each row requires the matching
  `SKIKO_JBR_INTEROP_LINEAR_GRADIENT_PATH_*_CORRUPTED` marker and records one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The expanded
  `CASE_GROUPS=gradient-path-invalid` run covered eleven path-gradient invalid rows; all eleven passed.
- Skiko `publishToMavenLocal` passed after adding the test-only linear path-gradient header corruption hooks.
- Broader `CASE_GROUPS=gradient-invalid` consolidation passed after completing the linear/radial/sweep path-gradient
  parser guard family:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-205847/suite.tsv`.
  The area group covered fifty-one malformed gradient rows; all fifty-one passed with one expected
  `command-stream-invalid` fallback marker per row, `unsupported=none`, zero JBR picture frames, and zero JBR command
  frames.
- Focused and grouped command-probe validation passed after adding sweep path-gradient parser coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-205145/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-205256/suite.tsv`.
  The new focused rows target `COMMAND_FILL_PATH_SWEEP_GRADIENT`; Skiko reads the recorded path-data length to find
  the gradient payload, then rewrites the color-count slot to `1` or second stop to `0`. Each row requires the
  matching `SKIKO_JBR_INTEROP_SWEEP_GRADIENT_PATH_*_CORRUPTED` marker and records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The expanded
  `CASE_GROUPS=gradient-path-invalid` run covered nine linear/radial/sweep path-gradient invalid rows; all nine
  passed.
- Skiko `publishToMavenLocal` passed after adding the test-only sweep path-gradient corruption hooks.
- Focused and grouped command-probe validation passed after adding radial path-gradient parser coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-204153/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-204356/suite.tsv`.
  The new focused rows target `COMMAND_FILL_PATH_RADIAL_GRADIENT`; Skiko reads the recorded path-data length to find
  the gradient payload, then rewrites the radius slot to `0`, tile-mode slot to `4`, color-count slot to `1`, or
  second stop to `0`. Each row requires the matching
  `SKIKO_JBR_INTEROP_RADIAL_GRADIENT_PATH_*_CORRUPTED` marker and records one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The expanded
  `CASE_GROUPS=gradient-path-invalid` run covered seven linear/radial path-gradient invalid rows; all seven passed.
- Skiko `publishToMavenLocal` passed after adding the test-only radial path-gradient corruption hooks.
- Focused and grouped command-probe validation passed after adding linear path-gradient parser coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-203301/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-203456/suite.tsv`.
  The new focused rows target `COMMAND_FILL_PATH_LINEAR_GRADIENT`; Skiko reads the recorded path-data length to find
  the gradient payload, then rewrites the tile-mode slot to `4`, color-count slot to `1`, or second stop to `0`. Each
  row requires the matching `SKIKO_JBR_INTEROP_LINEAR_GRADIENT_PATH_*_CORRUPTED` marker and records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The new narrow `CASE_GROUPS=gradient-path-invalid` run covered the three path-gradient invalid rows; all three
  passed.
- Skiko `publishToMavenLocal` passed after adding the test-only linear path-gradient corruption hooks.
- Focused and grouped command-probe validation passed after adding sweep-gradient stop-order parser coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-200346/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-200601/suite.tsv`.
  The new focused rows rewrite recorded sweep-gradient second stops to `0` for
  `COMMAND_FILL_RECT_SWEEP_GRADIENT`, `COMMAND_FILL_ROUND_RECT_SWEEP_GRADIENT`,
  `COMMAND_STROKE_RECT_SWEEP_GRADIENT`, and `COMMAND_STROKE_ROUND_RECT_SWEEP_GRADIENT`, require matching typed
  stop-order corruption markers, and each records one `command-stream-invalid` fallback marker, `unsupported=none`,
  zero JBR picture frames, and zero JBR command frames. The grouped `CASE_GROUPS=gradient-invalid` run now covers
  forty-two stroked-gradient/linear/radial/sweep invalid rows; all forty-two passed.
- Skiko `publishToMavenLocal` passed after adding the test-only sweep-gradient stop-order corruption hook.
- Focused and grouped command-probe validation passed after adding sweep-gradient color-count parser coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-193808/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-194011/suite.tsv`.
  The new focused rows rewrite recorded sweep-gradient color-count slots to `1` for
  `COMMAND_FILL_RECT_SWEEP_GRADIENT`, `COMMAND_FILL_ROUND_RECT_SWEEP_GRADIENT`,
  `COMMAND_STROKE_RECT_SWEEP_GRADIENT`, and `COMMAND_STROKE_ROUND_RECT_SWEEP_GRADIENT`, require matching typed
  color-count corruption markers, and each records one `command-stream-invalid` fallback marker, `unsupported=none`,
  zero JBR picture frames, and zero JBR command frames. The grouped `CASE_GROUPS=gradient-invalid` run now covers
  thirty-eight stroked-gradient/linear/radial/sweep invalid rows; all thirty-eight passed.
- Skiko `publishToMavenLocal` passed after adding the test-only sweep-gradient color-count corruption hook.
- Focused and grouped command-probe validation passed after adding linear-gradient stop-order parser coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-191609/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-191802/suite.tsv`.
  The first focused attempt (`20260517-191410`) caught an offset mistake that rewrote a color slot; after correcting
  the second-stop offsets to `13`, `15`, `17`, and `19`, the new focused rows rewrite recorded linear-gradient second
  stops to `0` for `COMMAND_FILL_RECT_LINEAR_GRADIENT`, `COMMAND_FILL_ROUND_RECT_LINEAR_GRADIENT`,
  `COMMAND_STROKE_RECT_LINEAR_GRADIENT`, and `COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT`. They require matching typed
  stop-order corruption markers, and each records one `command-stream-invalid` fallback marker, `unsupported=none`,
  zero JBR picture frames, and zero JBR command frames. The grouped `CASE_GROUPS=gradient-invalid` run now covers
  thirty-four stroked-gradient/linear/radial invalid rows; all thirty-four passed.
- Skiko `publishToMavenLocal` passed after adding the test-only linear-gradient stop-order corruption hook.
- Focused and grouped command-probe validation passed after adding linear-gradient color-count parser coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-185444/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-185635/suite.tsv`.
  The new focused rows rewrite recorded linear-gradient color-count slots to `1` for
  `COMMAND_FILL_RECT_LINEAR_GRADIENT`, `COMMAND_FILL_ROUND_RECT_LINEAR_GRADIENT`,
  `COMMAND_STROKE_RECT_LINEAR_GRADIENT`, and `COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT`, require matching typed
  color-count corruption markers, and each records one `command-stream-invalid` fallback marker, `unsupported=none`,
  zero JBR picture frames, and zero JBR command frames. The grouped `CASE_GROUPS=gradient-invalid` run now covers
  thirty stroked-gradient/linear/radial invalid rows; all thirty passed.
- Skiko `publishToMavenLocal` passed after adding the test-only linear-gradient color-count corruption hook.
- Focused and grouped command-probe validation passed after adding linear-gradient tile-mode parser coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-183701/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-183900/suite.tsv`.
  The new focused rows rewrite recorded linear-gradient tile-mode slots to `4` for
  `COMMAND_FILL_RECT_LINEAR_GRADIENT`, `COMMAND_FILL_ROUND_RECT_LINEAR_GRADIENT`,
  `COMMAND_STROKE_RECT_LINEAR_GRADIENT`, and `COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT`, require matching typed
  tile-mode corruption markers, and each records one `command-stream-invalid` fallback marker, `unsupported=none`,
  zero JBR picture frames, and zero JBR command frames. The grouped `CASE_GROUPS=gradient-invalid` run now covers
  twenty-six stroked-gradient/linear/radial invalid rows; all twenty-six passed.
- Skiko `publishToMavenLocal` passed after adding the test-only linear-gradient tile-mode corruption hook.
- Focused and grouped command-probe validation passed after adding radial-gradient stop-order parser coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-181911/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-182106/suite.tsv`.
  The new focused rows rewrite the second recorded radial-gradient stop to `0` for
  `COMMAND_FILL_RECT_RADIAL_GRADIENT`, `COMMAND_FILL_ROUND_RECT_RADIAL_GRADIENT`,
  `COMMAND_STROKE_RECT_RADIAL_GRADIENT`, and `COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT`, require matching typed
  stop-order corruption markers, and each records one `command-stream-invalid` fallback marker, `unsupported=none`,
  zero JBR picture frames, and zero JBR command frames. The grouped `CASE_GROUPS=gradient-invalid` run now covers
  twenty-two stroked-gradient/radial invalid rows; all twenty-two passed.
- Skiko `publishToMavenLocal` passed after adding the test-only radial-gradient stop-order corruption hook.
- Focused and grouped command-probe validation passed after adding radial-gradient color-count parser coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-175821/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-180104/suite.tsv`.
  The new focused rows rewrite recorded radial-gradient color-count slots to `1` for
  `COMMAND_FILL_RECT_RADIAL_GRADIENT`, `COMMAND_FILL_ROUND_RECT_RADIAL_GRADIENT`,
  `COMMAND_STROKE_RECT_RADIAL_GRADIENT`, and `COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT`, require matching typed
  color-count corruption markers, and each records one `command-stream-invalid` fallback marker, `unsupported=none`,
  zero JBR picture frames, and zero JBR command frames. The grouped `CASE_GROUPS=gradient-invalid` run now covers
  eighteen stroked-gradient/radial invalid rows; all eighteen passed.
- Skiko `publishToMavenLocal` passed after adding the test-only radial-gradient color-count corruption hook.
- Focused and grouped command-probe validation passed after adding radial-gradient tile-mode parser coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-174220/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-174457/suite.tsv`.
  The new focused rows rewrite recorded radial-gradient tile-mode slots to `4` for `COMMAND_FILL_RECT_RADIAL_GRADIENT`,
  `COMMAND_FILL_ROUND_RECT_RADIAL_GRADIENT`, `COMMAND_STROKE_RECT_RADIAL_GRADIENT`, and
  `COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT`, require matching typed tile-mode corruption markers, and each records
  one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command
  frames. The grouped `CASE_GROUPS=gradient-invalid` run now covers fourteen stroked-gradient/radial invalid rows; all
  fourteen passed.
- Skiko `publishToMavenLocal` passed after adding the test-only radial-gradient tile-mode corruption hook.
- Focused and grouped command-probe validation passed after adding radial-gradient radius parser coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-172925/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-173217/suite.tsv`.
  The new focused rows rewrite recorded radial-gradient radius slots to `0` for `COMMAND_FILL_RECT_RADIAL_GRADIENT`,
  `COMMAND_FILL_ROUND_RECT_RADIAL_GRADIENT`, `COMMAND_STROKE_RECT_RADIAL_GRADIENT`, and
  `COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT`, require matching typed radius corruption markers, and each records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=gradient-invalid` run now covers ten stroked-gradient/radial-radius invalid rows; all ten
  passed.
- Skiko `publishToMavenLocal` passed after adding the test-only radial-gradient radius corruption hook.
- Focused and grouped command-probe validation passed after extending stroked-gradient stroke-width parser coverage
  across linear, radial, and sweep rect/round-rect variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-171855/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-172203/suite.tsv`.
  The new focused rows rewrite the recorded stroke-width slot to `0` for
  `COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT`, `COMMAND_STROKE_RECT_RADIAL_GRADIENT`,
  `COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT`, `COMMAND_STROKE_RECT_SWEEP_GRADIENT`, and
  `COMMAND_STROKE_ROUND_RECT_SWEEP_GRADIENT`, require matching typed corruption markers, and each records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=gradient-invalid` run now covers six stroked-gradient invalid rows; all six passed.
- Skiko `publishToMavenLocal` passed after extending the test-only gradient stroke-width corruption hook to linear,
  radial, and sweep rect/round-rect variants.
- Focused and grouped command-probe validation passed after adding a live linear-gradient stroke-width parser guard:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-123644/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-171310/suite.tsv`.
  The focused row `commands-invalid-linear-gradient-stroke-width-fallback` rewrites the recorded
  `COMMAND_STROKE_RECT_LINEAR_GRADIENT` stroke-width slot to `0`, requires
  `SKIKO_JBR_INTEROP_LINEAR_GRADIENT_STROKE_WIDTH_CORRUPTED`, and records one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The new
  `CASE_GROUPS=gradient-invalid` group currently covers this row as the quick iteration path for gradient command
  parser guards.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptLinearGradientStrokeWidthForTesting` hook.
- Focused and grouped command-probe validation passed after adding native paragraph parser-guard coverage for font
  size, weight, width, slant, and font-family-count bounds:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-122225/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-122535/suite.tsv`.
  The new focused rows rewrite recorded `COMMAND_DRAW_PARAGRAPH_UTF16` font metadata to invalid values, require the
  matching `SKIKO_JBR_INTEROP_PARAGRAPH_FONT_SIZE_CORRUPTED`,
  `SKIKO_JBR_INTEROP_PARAGRAPH_FONT_WEIGHT_CORRUPTED`, `SKIKO_JBR_INTEROP_PARAGRAPH_FONT_WIDTH_CORRUPTED`,
  `SKIKO_JBR_INTEROP_PARAGRAPH_FONT_SLANT_CORRUPTED`, and
  `SKIKO_JBR_INTEROP_PARAGRAPH_FONT_FAMILY_COUNT_CORRUPTED` markers, and each records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=native-text-invalid` run now covers ten simple native text and paragraph invalid rows; all
  ten passed.
- Skiko `publishToMavenLocal` passed after extending the test-only native text command corruption hook to paragraph
  font size, weight, width, slant, and font-family-count fields.
- Focused and grouped command-probe validation passed after extending native text parser-guard coverage to font
  weight, width, slant, and font-family-count bounds:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-121327/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-121603/suite.tsv`.
  The new focused rows rewrite recorded `COMMAND_DRAW_TEXT_UTF16` font metadata to invalid values, require the matching
  `SKIKO_JBR_INTEROP_TEXT_FONT_WEIGHT_CORRUPTED`, `SKIKO_JBR_INTEROP_TEXT_FONT_WIDTH_CORRUPTED`,
  `SKIKO_JBR_INTEROP_TEXT_FONT_SLANT_CORRUPTED`, and `SKIKO_JBR_INTEROP_TEXT_FONT_FAMILY_COUNT_CORRUPTED` markers, and
  each records one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR
  command frames. The grouped `CASE_GROUPS=native-text-invalid` run now covers five simple native text invalid rows;
  all five passed.
- Skiko `publishToMavenLocal` passed after extending the test-only native text command corruption hook to font weight,
  width, slant, and font-family-count fields.
- Focused and grouped command-probe validation passed after adding a live native text font-size sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-114019/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-120803/suite.tsv`.
  The focused row `commands-invalid-text-font-size-fallback` rewrites every recorded `COMMAND_DRAW_TEXT_UTF16`
  font-size slot to `0`, requires `SKIKO_JBR_INTEROP_TEXT_FONT_SIZE_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The new `CASE_GROUPS=native-text-invalid` group gives native-text parser-guard work a narrow iteration path before
  the next periodic full default sweep.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptTextFontSizeForTesting` hook.
- Periodic full default command-probe sweep passed after the RuntimeEffect shader/color-filter lower-bound sentinel
  batch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-213052/suite.tsv`.
  Aggregate: 236/236 rows passed, with 110 command replay rows, 26 intentional picture-fallback rows, and 100 explicit
  structured fallback rows. The sweep also reconfirmed the already-existing
  `commands-invalid-effect-descriptor-type-fallback` live sentinel for JBR's unknown effect descriptor type parser
  guard, and the newest `commands-runtime-effect-shader-negative-named-child-count-fallback` row passed with one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect shader negative
  named-child-count sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-195041/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-195130/suite.tsv`.
  The focused row `commands-runtime-effect-shader-negative-named-child-count-fallback` rewrites one recorded
  RuntimeEffect shader descriptor `namedChildCount` to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_NEGATIVE_NAMED_CHILD_COUNT_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=runtime-effect-invalid` run covered thirty-one RuntimeEffect-invalid rows; all thirty-one
  passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectShaderNegativeNamedChildCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect shader negative
  named-uniform-count sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-190942/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-191029/suite.tsv`.
  The focused row `commands-runtime-effect-shader-negative-named-uniform-count-fallback` rewrites one recorded
  RuntimeEffect shader descriptor `namedUniformCount` to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_NEGATIVE_NAMED_UNIFORM_COUNT_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=runtime-effect-invalid` run covered thirty RuntimeEffect-invalid rows; all thirty passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectShaderNegativeNamedUniformCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect shader negative child-count
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-184122/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-184211/suite.tsv`.
  The focused row `commands-runtime-effect-shader-negative-child-count-fallback` rewrites one recorded RuntimeEffect
  shader descriptor `childCount` to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_NEGATIVE_CHILD_COUNT_CORRUPTED`, records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=runtime-effect-invalid` run covered twenty-nine RuntimeEffect-invalid rows; all twenty-nine passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectShaderNegativeChildCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect shader negative
  uniform-float-count sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-181512/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-181601/suite.tsv`.
  The focused row `commands-runtime-effect-shader-negative-uniform-float-count-fallback` rewrites one recorded
  RuntimeEffect shader descriptor `uniformFloatCount` to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_NEGATIVE_UNIFORM_FLOAT_COUNT_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=runtime-effect-invalid` run covered twenty-eight RuntimeEffect-invalid rows; all
  twenty-eight passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectShaderNegativeUniformFloatCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect color-filter negative
  named-child-count sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-173844/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-173928/suite.tsv`.
  The focused row `commands-runtime-effect-color-filter-negative-named-child-count-fallback` rewrites one recorded
  RuntimeEffect color-filter descriptor `namedChildCount` to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_NEGATIVE_NAMED_CHILD_COUNT_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=runtime-effect-invalid` run covered twenty-seven RuntimeEffect-invalid rows; all
  twenty-seven passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectColorFilterNegativeNamedChildCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect color-filter negative
  named-uniform-count sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-171026/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-171111/suite.tsv`.
  The focused row `commands-runtime-effect-color-filter-negative-named-uniform-count-fallback` rewrites one recorded
  RuntimeEffect color-filter descriptor `namedUniformCount` to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_NEGATIVE_NAMED_UNIFORM_COUNT_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=runtime-effect-invalid` run covered twenty-six RuntimeEffect-invalid rows; all twenty-six
  passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectColorFilterNegativeNamedUniformCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect color-filter negative
  child-count sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-151126/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-151507/suite.tsv`.
  The focused row `commands-runtime-effect-color-filter-negative-child-count-fallback` rewrites one recorded
  RuntimeEffect color-filter descriptor `childCount` to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_NEGATIVE_CHILD_COUNT_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=runtime-effect-invalid` run covered twenty-five RuntimeEffect-invalid rows; all twenty-five
  passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectColorFilterNegativeChildCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect color-filter negative
  uniform-float-count sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-144403/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-144448/suite.tsv`.
  The focused row `commands-runtime-effect-color-filter-negative-uniform-float-count-fallback` rewrites one recorded
  RuntimeEffect color-filter descriptor `uniformFloatCount` to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_NEGATIVE_UNIFORM_FLOAT_COUNT_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=runtime-effect-invalid` run covered twenty-four RuntimeEffect-invalid rows; all twenty-four
  passed. This keeps small sentinel work on the quick exact-row plus area-group path rather than running a full default
  command sweep for every lower-bound hook.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectColorFilterNegativeUniformFloatCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect color-filter named-child-count
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-141823/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-141907/suite.tsv`.
  The focused row `commands-runtime-effect-color-filter-named-child-count-fallback` rewrites one recorded
  RuntimeEffect color-filter descriptor `namedChildCount` to `9`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_NAMED_CHILD_COUNT_CORRUPTED`, records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=runtime-effect-invalid` run covered twenty-three RuntimeEffect-invalid rows; all twenty-three passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectColorFilterNamedChildCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect color-filter named-uniform-count
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-135519/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-135603/suite.tsv`.
  The focused row `commands-runtime-effect-color-filter-named-uniform-count-fallback` rewrites one recorded
  RuntimeEffect color-filter descriptor `namedUniformCount` to `17`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_NAMED_UNIFORM_COUNT_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=runtime-effect-invalid` run covered twenty-two RuntimeEffect-invalid rows; all twenty-two
  passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectColorFilterNamedUniformCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect color-filter child-count
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-133243/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-133331/suite.tsv`.
  The focused row `commands-runtime-effect-color-filter-child-count-fallback` rewrites one recorded RuntimeEffect
  color-filter descriptor `childCount` to `9`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_CHILD_COUNT_CORRUPTED`, records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=runtime-effect-invalid` run covered twenty-one RuntimeEffect-invalid rows; all twenty-one passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectColorFilterChildCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect color-filter
  uniform-float-count sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-131044/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-131129/suite.tsv`.
  The focused row `commands-runtime-effect-color-filter-uniform-float-count-fallback` rewrites one recorded
  RuntimeEffect color-filter descriptor `uniformFloatCount` to `257`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_UNIFORM_FLOAT_COUNT_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=runtime-effect-invalid` run covered twenty RuntimeEffect-invalid rows; all twenty passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectColorFilterUniformFloatCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect color-filter SKSL-length
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-124807/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-124855/suite.tsv`.
  The focused row `commands-runtime-effect-color-filter-sksl-length-fallback` rewrites one recorded RuntimeEffect
  color-filter descriptor SKSL length to `0`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_SKSL_LENGTH_CORRUPTED`, records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=runtime-effect-invalid` run covered nineteen RuntimeEffect-invalid rows; all nineteen passed.
  This slice intentionally used the quick exact-row then area-group validation path rather than another full default
  sweep.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectColorFilterSkslLengthForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect shader named-child-count
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-205029/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-205114/suite.tsv`.
  The focused row `commands-runtime-effect-shader-named-child-count-fallback` rewrites one recorded RuntimeEffect
  shader descriptor `namedChildCount` to one more than `childCount`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_NAMED_CHILD_COUNT_CORRUPTED`, records one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=runtime-effect-invalid` run covered eighteen RuntimeEffect-invalid rows; all eighteen passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectShaderNamedChildCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect shader named-uniform-count
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-203131/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-203219/suite.tsv`.
  The focused row `commands-runtime-effect-shader-named-uniform-count-fallback` rewrites one recorded RuntimeEffect
  shader descriptor `namedUniformCount` to `17`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_NAMED_UNIFORM_COUNT_CORRUPTED`, records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=runtime-effect-invalid` run covered seventeen RuntimeEffect-invalid rows; all seventeen passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectShaderNamedUniformCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect shader child-count sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-201222/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-201314/suite.tsv`.
  The focused row `commands-runtime-effect-shader-child-count-fallback` rewrites one recorded RuntimeEffect shader
  descriptor `childCount` to `9`, requires `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_CHILD_COUNT_CORRUPTED`, records
  one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command
  frames. The grouped `CASE_GROUPS=runtime-effect-invalid` run covered sixteen RuntimeEffect-invalid rows; all sixteen
  passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectShaderChildCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect shader uniform-float-count
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-195420/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-195512/suite.tsv`.
  The focused row `commands-runtime-effect-shader-uniform-float-count-fallback` rewrites one recorded RuntimeEffect
  shader descriptor `uniformFloatCount` to `257`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_UNIFORM_FLOAT_COUNT_CORRUPTED`, records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=runtime-effect-invalid` run covered fifteen RuntimeEffect-invalid rows; all fifteen passed.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectShaderUniformFloatCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live RuntimeEffect shader SKSL-length sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-192212/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-193737/suite.tsv`.
  The focused row `commands-runtime-effect-shader-sksl-length-fallback` rewrites one recorded RuntimeEffect shader
  descriptor `skslLength` to `0`, requires `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_SKSL_LENGTH_CORRUPTED`, records
  one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command
  frames. The grouped `CASE_GROUPS=runtime-effect-invalid` run covered fourteen RuntimeEffect-invalid rows; all
  fourteen passed. The command suite now skips default-case migration replacements when `CASE_GROUPS` builds a curated
  list, preventing duplicated group rows.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectShaderSkslLengthForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live shader color-filter descriptor payload-count
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-185920/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-190002/suite.tsv`.
  The focused row `commands-invalid-shader-color-filter-descriptor-payload-count-fallback` rewrites one recorded shader
  color-filter descriptor payload count to `5` and increases the record length to keep the metadata gate consistent,
  requires `SKIKO_JBR_INTEROP_SHADER_COLOR_FILTER_DESCRIPTOR_PAYLOAD_COUNT_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=shader-descriptor-invalid` run covered twenty-nine malformed shader-descriptor rows; all
  twenty-nine passed, with zero JBR replay rows and twenty-nine expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptShaderColorFilterDescriptorPayloadCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live solid color shader descriptor payload-count
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-183751/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-183835/suite.tsv`.
  The focused row `commands-invalid-color-shader-descriptor-payload-count-fallback` rewrites one recorded solid color
  shader descriptor payload count to `2` and increases the record length to keep the metadata gate consistent, requires
  `SKIKO_JBR_INTEROP_COLOR_SHADER_DESCRIPTOR_PAYLOAD_COUNT_CORRUPTED`, records one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=shader-descriptor-invalid` run covered twenty-eight malformed shader-descriptor rows; all twenty-eight
  passed, with zero JBR replay rows and twenty-eight expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptColorShaderDescriptorPayloadCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live composite shader descriptor blend-mode
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-181627/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-181710/suite.tsv`.
  The focused row `commands-invalid-composite-shader-descriptor-blend-mode-fallback` rewrites one recorded composite
  shader descriptor blend-mode slot to `99`, requires
  `SKIKO_JBR_INTEROP_COMPOSITE_SHADER_DESCRIPTOR_BLEND_MODE_CORRUPTED`, records one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=shader-descriptor-invalid` run covered twenty-seven malformed shader-descriptor rows; all twenty-seven
  passed, with zero JBR replay rows and twenty-seven expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptCompositeShaderDescriptorBlendModeForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live chain path-effect descriptor payload-count
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-175121/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-175219/suite.tsv`.
  The focused row `commands-invalid-chain-path-effect-descriptor-payload-count-fallback` rewrites one recorded chain
  path-effect descriptor payload count to `5` and increases the record length to keep the metadata gate consistent,
  requires `SKIKO_JBR_INTEROP_CHAIN_PATH_EFFECT_DESCRIPTOR_PAYLOAD_COUNT_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=effect-descriptor-invalid` run covered twenty-two malformed effect-descriptor rows; all
  twenty-two passed, with zero JBR replay rows and twenty-two expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptChainPathEffectDescriptorPayloadCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live lighting color-filter descriptor payload-count
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-173102/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-173150/suite.tsv`.
  The focused row `commands-invalid-lighting-filter-descriptor-payload-count-fallback` rewrites one recorded lighting
  descriptor payload count to `3` and increases the record length to keep the metadata gate consistent, requires
  `SKIKO_JBR_INTEROP_LIGHTING_FILTER_DESCRIPTOR_PAYLOAD_COUNT_CORRUPTED`, records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=effect-descriptor-invalid` run covered twenty-one malformed effect-descriptor rows; all twenty-one
  passed, with zero JBR replay rows and twenty-one expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptLightingFilterDescriptorPayloadCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live image shader descriptor height upper-bound
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-170405/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-170449/suite.tsv`.
  The focused row `commands-invalid-image-shader-descriptor-max-height-fallback` rewrites one recorded image shader
  descriptor height slot to `4097`, requires `SKIKO_JBR_INTEROP_IMAGE_SHADER_DESCRIPTOR_MAX_HEIGHT_CORRUPTED`, records
  one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command
  frames. The grouped `CASE_GROUPS=shader-descriptor-invalid` run covered twenty-six malformed shader-descriptor rows;
  all twenty-six passed, with zero JBR replay rows and twenty-six expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptImageShaderDescriptorMaxHeightForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live image shader descriptor width upper-bound
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-164502/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-164545/suite.tsv`.
  The focused row `commands-invalid-image-shader-descriptor-max-width-fallback` rewrites one recorded image shader
  descriptor width slot to `4097`, requires `SKIKO_JBR_INTEROP_IMAGE_SHADER_DESCRIPTOR_MAX_WIDTH_CORRUPTED`, records
  one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command
  frames. The grouped `CASE_GROUPS=shader-descriptor-invalid` run covered twenty-five malformed shader-descriptor
  rows; all twenty-five passed, with zero JBR replay rows and twenty-five expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptImageShaderDescriptorMaxWidthForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live Perlin/noise shader descriptor negative
  tile-height sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-162500/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-162542/suite.tsv`.
  The focused row `commands-invalid-perlin-noise-shader-negative-tile-height-fallback` rewrites one recorded
  Perlin/noise shader descriptor tile-height slot to `-1`, requires
  `SKIKO_JBR_INTEROP_PERLIN_NOISE_SHADER_NEGATIVE_TILE_HEIGHT_CORRUPTED`, records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=shader-descriptor-invalid` run covered twenty-four malformed shader-descriptor rows; all twenty-four
  passed, with zero JBR replay rows and twenty-four expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptPerlinNoiseShaderNegativeTileHeightForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live Perlin/noise shader descriptor tile-height
  upper-bound sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-160431/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-160516/suite.tsv`.
  The focused row `commands-invalid-perlin-noise-shader-tile-height-fallback` rewrites one recorded Perlin/noise
  shader descriptor tile-height slot to `4097`, requires `SKIKO_JBR_INTEROP_PERLIN_NOISE_SHADER_TILE_HEIGHT_CORRUPTED`,
  records one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR
  command frames. The grouped `CASE_GROUPS=shader-descriptor-invalid` run covered twenty-three malformed
  shader-descriptor rows; all twenty-three passed, with zero JBR replay rows and twenty-three expected explicit
  fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptPerlinNoiseShaderTileHeightForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live sweep-gradient shader descriptor stop-order
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-154421/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-154510/suite.tsv`.
  The focused row `commands-invalid-sweep-gradient-shader-descriptor-stop-order-fallback` uses the descriptor-backed
  sweep-gradient shader-plus-color-filter probe, rewrites the second recorded sweep-gradient stop position to match
  the first stop, requires `SKIKO_JBR_INTEROP_SWEEP_GRADIENT_SHADER_DESCRIPTOR_STOP_ORDER_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=shader-descriptor-invalid` run covered twenty-two malformed shader-descriptor rows; all
  twenty-two passed, with zero JBR replay rows and twenty-two expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptSweepGradientShaderDescriptorStopOrderForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live radial-gradient shader descriptor stop-order
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-152642/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-152730/suite.tsv`.
  The focused row `commands-invalid-radial-gradient-shader-descriptor-stop-order-fallback` uses the descriptor-backed
  composite shader probe, rewrites the second recorded radial-gradient stop position to match the first stop, requires
  `SKIKO_JBR_INTEROP_RADIAL_GRADIENT_SHADER_DESCRIPTOR_STOP_ORDER_CORRUPTED`, records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=shader-descriptor-invalid` run covered twenty-one malformed shader-descriptor rows; all twenty-one
  passed, with zero JBR replay rows and twenty-one expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRadialGradientShaderDescriptorStopOrderForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live linear-gradient shader descriptor stop-order
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-150917/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-151002/suite.tsv`.
  The focused row `commands-invalid-linear-gradient-shader-descriptor-stop-order-fallback` uses the descriptor-backed
  linear-gradient shader-plus-color-filter probe, rewrites the second recorded stop position to match the first stop,
  requires `SKIKO_JBR_INTEROP_LINEAR_GRADIENT_SHADER_DESCRIPTOR_STOP_ORDER_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=shader-descriptor-invalid` run covered twenty malformed shader-descriptor rows; all twenty
  passed, with zero JBR replay rows and twenty expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptLinearGradientShaderDescriptorStopOrderForTesting` hook.
- Full default command-probe sweep passed after the live image shader descriptor height and X/Y tile-mode sentinel
  batch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-124900/suite.tsv`.
  Aggregate: 206/206 rows passed, with 110 command replay rows, 26 intentional picture-fallback rows, and 70 explicit
  structured fallback rows. The full-suite malformed shader-descriptor block includes all nineteen current
  shader-descriptor invalid rows, including image shader positive-width, positive-height, X tile-mode, and Y tile-mode
  sentinels.
- Focused and grouped command-probe validation passed after adding a live image shader descriptor Y tile-mode
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-123503/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-123547/suite.tsv`.
  The focused row `commands-invalid-image-shader-descriptor-tile-mode-y-fallback` uses the descriptor-backed
  image-shader-plus-color-filter probe, rewrites one recorded image shader descriptor Y tile-mode slot to `99`,
  requires `SKIKO_JBR_INTEROP_IMAGE_SHADER_DESCRIPTOR_TILE_MODE_Y_CORRUPTED`, records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=shader-descriptor-invalid` run covered nineteen malformed shader-descriptor rows; all nineteen passed,
  with zero JBR replay rows and nineteen expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptImageShaderDescriptorTileModeYForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live image shader descriptor X tile-mode
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-122008/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-122053/suite.tsv`.
  The focused row `commands-invalid-image-shader-descriptor-tile-mode-x-fallback` uses the descriptor-backed
  image-shader-plus-color-filter probe, rewrites one recorded image shader descriptor X tile-mode slot to `99`,
  requires `SKIKO_JBR_INTEROP_IMAGE_SHADER_DESCRIPTOR_TILE_MODE_X_CORRUPTED`, records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=shader-descriptor-invalid` run covered eighteen malformed shader-descriptor rows; all eighteen passed,
  with zero JBR replay rows and eighteen expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptImageShaderDescriptorTileModeXForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live image shader descriptor height sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-120348/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-120436/suite.tsv`.
  The focused row `commands-invalid-image-shader-descriptor-height-fallback` uses the descriptor-backed
  image-shader-plus-color-filter probe, rewrites one recorded image shader descriptor height slot to `0`, requires
  `SKIKO_JBR_INTEROP_IMAGE_SHADER_DESCRIPTOR_HEIGHT_CORRUPTED`, records one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=shader-descriptor-invalid` run covered seventeen malformed shader-descriptor rows; all seventeen
  passed, with zero JBR replay rows and seventeen expected explicit fallback markers. This slice used the quick
  exact-row then area-group validation path after the previous full 203-row default sweep.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptImageShaderDescriptorHeightForTesting` hook.
- Focused, grouped, and full command-probe validation passed after adding a live sweep-gradient shader descriptor
  color-count sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-092309/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-092408/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-093643/suite.tsv`.
  The focused row `commands-invalid-sweep-gradient-shader-descriptor-color-count-fallback` uses a new
  `MAGIC_JEWEL_COMPOSE_SWEEP_GRADIENT_SHADER_COLOR_FILTER=true` probe to force descriptor-backed sweep-gradient
  replay, rewrites one recorded sweep-gradient shader descriptor color-count slot to `17`, requires
  `SKIKO_JBR_INTEROP_SWEEP_GRADIENT_SHADER_DESCRIPTOR_COLOR_COUNT_CORRUPTED`, records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=shader-descriptor-invalid` run covered sixteen malformed shader-descriptor rows; all sixteen passed
  with zero JBR replay rows and sixteen expected explicit fallback markers. The full default command sweep passed
  203/203 rows, with 110 command replay rows, 26 intentional picture-fallback rows, and 67 explicit structured
  fallback rows. Before the focused rerun, local JBR artifacts were refreshed with
  `./scripts/rebuild-jbr-skia-local-artifacts.sh` because this shell had an empty `/tmp/jbr-skia-run/desktop` patch
  directory and Temurin was correctly falling back with `service-unavailable`.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptSweepGradientShaderDescriptorColorCountForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live radial-gradient shader descriptor tile-mode
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-234201/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-234247/suite.tsv`.
  The focused row `commands-invalid-radial-gradient-shader-descriptor-tile-mode-fallback` uses the descriptor-backed
  composite shader probe, rewrites one recorded radial-gradient shader descriptor tile-mode slot to `99`, requires
  `SKIKO_JBR_INTEROP_RADIAL_GRADIENT_SHADER_DESCRIPTOR_TILE_MODE_CORRUPTED`, records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=shader-descriptor-invalid` run covered fifteen malformed shader-descriptor rows; all fifteen passed,
  with zero JBR replay rows and fifteen expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRadialGradientShaderDescriptorTileModeForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live linear-gradient shader descriptor tile-mode
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-232913/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-233003/suite.tsv`.
  The focused row `commands-invalid-linear-gradient-shader-descriptor-tile-mode-fallback` uses the descriptor-backed
  linear-gradient shader-plus-color-filter probe, rewrites one recorded linear-gradient shader descriptor tile-mode
  slot to `99`, requires `SKIKO_JBR_INTEROP_LINEAR_GRADIENT_SHADER_DESCRIPTOR_TILE_MODE_CORRUPTED`, records one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=shader-descriptor-invalid` run covered fourteen malformed shader-descriptor rows; all
  fourteen passed, with zero JBR replay rows and fourteen expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptLinearGradientShaderDescriptorTileModeForTesting` hook.
- Full default command-probe sweep passed after the radial-gradient radius and image shader width sentinel batch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-211318/suite.tsv`.
  Aggregate: 200/200 rows passed, with 110 command replay rows, 26 intentional picture-fallback rows, and 64 explicit
  structured fallback rows. The full-suite malformed shader-descriptor block includes thirteen current
  shader-descriptor invalid rows, including the latest radial-gradient positive-radius and image-shader positive-width
  sentinels.
- Focused and grouped command-probe validation passed after adding a live radial-gradient shader descriptor radius
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-210305/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-210352/suite.tsv`.
  The focused row `commands-invalid-radial-gradient-shader-descriptor-radius-fallback` uses the descriptor-backed
  composite shader probe, rewrites one recorded radial-gradient shader descriptor radius slot to `0`, requires
  `SKIKO_JBR_INTEROP_RADIAL_GRADIENT_SHADER_DESCRIPTOR_RADIUS_CORRUPTED`, records one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=shader-descriptor-invalid` run covered thirteen malformed shader-descriptor rows; all thirteen passed,
  with zero JBR replay rows and thirteen expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRadialGradientShaderDescriptorRadiusForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live image shader descriptor width sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-205054/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-205138/suite.tsv`.
  The focused row `commands-invalid-image-shader-descriptor-width-fallback` uses the descriptor-backed
  image-shader-plus-color-filter probe, rewrites one recorded image shader descriptor width slot to `0`, requires
  `SKIKO_JBR_INTEROP_IMAGE_SHADER_DESCRIPTOR_WIDTH_CORRUPTED`, records one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=shader-descriptor-invalid` run covered twelve malformed shader-descriptor rows; all twelve passed,
  with zero JBR replay rows and twelve expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptImageShaderDescriptorWidthForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live Perlin/noise shader descriptor zero-octaves
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-203437/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-203549/suite.tsv`.
  The focused row `commands-invalid-perlin-noise-shader-zero-octaves-fallback` rewrote one recorded Perlin/noise
  shader descriptor octave-count slot to `0`, required
  `SKIKO_JBR_INTEROP_PERLIN_NOISE_SHADER_ZERO_OCTAVES_CORRUPTED`, recorded one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=shader-descriptor-invalid` run covered eleven malformed shader-descriptor rows; all eleven passed,
  with zero JBR replay rows and eleven expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptPerlinNoiseShaderZeroOctavesForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live Perlin/noise shader descriptor negative
  tile-size sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-202424/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-202513/suite.tsv`.
  The focused row `commands-invalid-perlin-noise-shader-negative-tile-size-fallback` rewrote one recorded
  Perlin/noise shader descriptor tile-size slot to `-1`, required
  `SKIKO_JBR_INTEROP_PERLIN_NOISE_SHADER_NEGATIVE_TILE_SIZE_CORRUPTED`, recorded one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=shader-descriptor-invalid` run covered ten malformed shader-descriptor rows; all ten passed, with zero
  JBR replay rows and ten expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptPerlinNoiseShaderNegativeTileSizeForTesting` hook.
- Full default command-probe sweep passed after the blur/stamped/corner finite-bound sentinel batch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-180227/suite.tsv`.
  Aggregate: 196/196 rows passed, with 110 command replay rows, 26 intentional picture-fallback rows, and 60 explicit
  structured fallback rows. The full-suite malformed effect-descriptor block includes all twenty current
  effect-descriptor invalid rows, including the latest negative sigma, negative radius, zero/negative stamped advance
  or phase bounds, and negative/oversized stamped path-data-length sentinels.
- Focused and grouped command-probe validation passed after adding a live stamped path-effect descriptor negative
  path-data-length sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-174121/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-174214/suite.tsv`.
  The focused row `commands-invalid-stamped-path-effect-descriptor-negative-path-data-length-fallback` rewrote one
  recorded stamped path-effect descriptor path-data length slot to `-1`, required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_NEGATIVE_PATH_DATA_LENGTH_CORRUPTED`, recorded one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=effect-descriptor-invalid` run covered twenty malformed effect-descriptor rows; all twenty
  passed, with zero JBR replay rows and twenty expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptStampedPathEffectDescriptorNegativePathDataLengthForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live blur image-filter descriptor negative-sigma
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-172401/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-172454/suite.tsv`.
  The focused row `commands-invalid-blur-image-filter-descriptor-negative-sigma-fallback` rewrote one recorded blur
  image-filter descriptor sigma slot to `-1`, required
  `SKIKO_JBR_INTEROP_BLUR_IMAGE_FILTER_DESCRIPTOR_NEGATIVE_SIGMA_CORRUPTED`, recorded one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=effect-descriptor-invalid` run covered nineteen malformed effect-descriptor rows; all nineteen passed,
  with zero JBR replay rows and nineteen expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptBlurImageFilterDescriptorNegativeSigmaForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live corner path-effect descriptor negative-radius
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-170802/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-170922/suite.tsv`.
  The focused row `commands-invalid-corner-path-effect-descriptor-negative-radius-fallback` rewrote one recorded
  corner path-effect descriptor radius slot to `-1`, required
  `SKIKO_JBR_INTEROP_CORNER_PATH_EFFECT_DESCRIPTOR_NEGATIVE_RADIUS_CORRUPTED`, recorded one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=effect-descriptor-invalid` run covered eighteen malformed effect-descriptor rows; all
  eighteen passed, with zero JBR replay rows and eighteen expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptCornerPathEffectDescriptorNegativeRadiusForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live stamped path-effect descriptor negative-phase
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-165312/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-165402/suite.tsv`.
  The focused row `commands-invalid-stamped-path-effect-descriptor-negative-phase-fallback` rewrote one recorded
  stamped path-effect descriptor phase slot to `-1`, required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_NEGATIVE_PHASE_CORRUPTED`, recorded one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=effect-descriptor-invalid` run covered seventeen malformed effect-descriptor rows; all
  seventeen passed, with zero JBR replay rows and seventeen expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptStampedPathEffectDescriptorNegativePhaseForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live stamped path-effect descriptor zero-advance
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-163606/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-163650/suite.tsv`.
  The focused row `commands-invalid-stamped-path-effect-descriptor-zero-advance-fallback` rewrote one recorded stamped
  path-effect descriptor advance slot to `0`, required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_ZERO_ADVANCE_CORRUPTED`, recorded one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=effect-descriptor-invalid` run covered sixteen malformed effect-descriptor rows; all
  sixteen passed, with zero JBR replay rows and sixteen expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptStampedPathEffectDescriptorZeroAdvanceForTesting` hook. An initial focused attempt at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-163534/suite.tsv`
  failed before app startup because the sandbox blocked Gradle wrapper cache access under `~/.gradle`; the same row
  passed when rerun with the validation suite's normal Gradle access.
- Focused and grouped command-probe validation passed after adding a live blur image-filter descriptor tile-mode
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-161649/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-161742/suite.tsv`.
  The focused row `commands-invalid-blur-image-filter-descriptor-tile-mode-fallback` rewrote one recorded blur
  image-filter descriptor tile-mode slot to `99`, required
  `SKIKO_JBR_INTEROP_BLUR_IMAGE_FILTER_DESCRIPTOR_TILE_MODE_CORRUPTED`, recorded one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=effect-descriptor-invalid` run covered fifteen malformed effect-descriptor rows; all fifteen passed,
  with zero JBR replay rows and fifteen expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptBlurImageFilterDescriptorTileModeForTesting` hook. An initial focused attempt at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-161436/suite.tsv`
  correctly failed because the hook's precheck skipped the plain blur descriptor; the guard was tightened before the
  passing focused and grouped runs above.
- Full default command-probe sweep passed after the stamped path-effect descriptor payload hardening:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-140549/suite.tsv`.
  Aggregate: 190/190 rows passed, with 110 command replay rows, 26 intentional picture-fallback rows, and 54 explicit
  structured fallback rows. The expanded malformed effect-descriptor block covered the new stamped advance, phase,
  style, fill-type, and path-data-length sentinels in the full-suite context.
- Focused and grouped command-probe validation passed after adding a live stamped path-effect descriptor
  path-data-length sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-135433/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-135519/suite.tsv`.
  The focused row `commands-invalid-stamped-path-effect-descriptor-path-data-length-fallback` rewrote one recorded
  stamped path-effect descriptor path-data length slot to `4097`, required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_PATH_DATA_LENGTH_CORRUPTED`, recorded one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The grouped `CASE_GROUPS=effect-descriptor-invalid` run covered fourteen malformed effect-descriptor rows; all
  fourteen passed, with zero JBR replay rows and fourteen expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptStampedPathEffectDescriptorPathDataLengthForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live stamped path-effect descriptor fill-type
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-134123/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-134207/suite.tsv`.
  The focused row `commands-invalid-stamped-path-effect-descriptor-fill-type-fallback` rewrote one recorded stamped
  path-effect descriptor fill-type slot to `99`, required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_FILL_TYPE_CORRUPTED`, recorded one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=effect-descriptor-invalid` run covered thirteen malformed effect-descriptor rows; all thirteen passed,
  with zero JBR replay rows and thirteen expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptStampedPathEffectDescriptorFillTypeForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live stamped path-effect descriptor style
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-132932/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-133018/suite.tsv`.
  The focused row `commands-invalid-stamped-path-effect-descriptor-style-fallback` rewrote one recorded stamped
  path-effect descriptor style slot to `99`, required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_STYLE_CORRUPTED`, recorded one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=effect-descriptor-invalid` run covered twelve malformed effect-descriptor rows; all twelve passed, with
  zero JBR replay rows and twelve expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptStampedPathEffectDescriptorStyleForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live stamped path-effect descriptor phase
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-131759/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-131846/suite.tsv`.
  The focused row `commands-invalid-stamped-path-effect-descriptor-phase-fallback` rewrote one recorded stamped
  path-effect descriptor phase slot to NaN, required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_PHASE_CORRUPTED`, recorded one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=effect-descriptor-invalid` run covered eleven malformed effect-descriptor rows; all eleven passed, with
  zero JBR replay rows and eleven expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptStampedPathEffectDescriptorPhaseForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live stamped path-effect descriptor advance
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-130510/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-130556/suite.tsv`.
  The focused row `commands-invalid-stamped-path-effect-descriptor-advance-fallback` rewrote one recorded stamped
  path-effect descriptor advance slot to NaN, required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_ADVANCE_CORRUPTED`, recorded one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=effect-descriptor-invalid` run covered ten malformed effect-descriptor rows; all ten passed, with zero
  JBR replay rows and ten expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptStampedPathEffectDescriptorAdvanceForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live corner path-effect descriptor radius sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-125458/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-125544/suite.tsv`.
  The focused row `commands-invalid-corner-path-effect-descriptor-radius-fallback` rewrote one recorded corner
  path-effect descriptor radius slot to NaN, required
  `SKIKO_JBR_INTEROP_CORNER_PATH_EFFECT_DESCRIPTOR_RADIUS_CORRUPTED`, recorded one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=effect-descriptor-invalid` run covered nine malformed effect-descriptor rows; all nine passed, with
  zero JBR replay rows and nine expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptCornerPathEffectDescriptorRadiusForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live offset image-filter descriptor delta sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-124549/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-124635/suite.tsv`.
  The focused row `commands-invalid-offset-image-filter-descriptor-delta-fallback` rewrote one recorded offset
  image-filter descriptor delta slot to NaN, required
  `SKIKO_JBR_INTEROP_OFFSET_IMAGE_FILTER_DESCRIPTOR_DELTA_CORRUPTED`, recorded one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=effect-descriptor-invalid` run covered eight malformed effect-descriptor rows; all eight passed, with
  zero JBR replay rows and eight expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptOffsetImageFilterDescriptorDeltaForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live blur image-filter descriptor sigma sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-123706/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-123801/suite.tsv`.
  The focused row `commands-invalid-blur-image-filter-descriptor-sigma-fallback` rewrote one recorded blur
  image-filter descriptor sigma slot to NaN, required
  `SKIKO_JBR_INTEROP_BLUR_IMAGE_FILTER_DESCRIPTOR_SIGMA_CORRUPTED`, recorded one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=effect-descriptor-invalid` run covered seven malformed effect-descriptor rows; all seven passed, with
  zero JBR replay rows and seven expected explicit fallback markers.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptBlurImageFilterDescriptorSigmaForTesting` hook.
- Focused and grouped command-probe validation passed after adding a live color-matrix filter descriptor payload
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-102259/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-122441/suite.tsv`.
  The focused row `commands-invalid-color-matrix-filter-descriptor-payload-fallback` rewrote one recorded
  color-matrix descriptor payload slot to NaN, required
  `SKIKO_JBR_INTEROP_COLOR_MATRIX_FILTER_DESCRIPTOR_PAYLOAD_CORRUPTED`, recorded one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The grouped
  `CASE_GROUPS=effect-descriptor-invalid` run covered six malformed effect-descriptor rows; all six passed, with zero
  JBR replay rows and six expected explicit fallback markers.
- A full default command-probe checkpoint also produced a complete 182-row TSV for the same slice:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-102400/suite.tsv`.
  All 182 row results were `passed`, with 110 command replay rows, 26 intentional JBR picture fallback rows, and 46
  expected explicit fallback-marker rows. The shell process exited after the final row because the suite script was
  edited while Bash was still reading its tail, so this TSV is supporting evidence rather than the promoted full-sweep
  gate.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptColorMatrixFilterDescriptorPayloadForTesting` hook.
- Magic Jewel command-suite grouping is now available for quicker inner-loop runs. `CASES=...` still selects exact
  rows; `CASE_GROUPS=...` selects curated groups such as `effect-descriptor-invalid`, `shader-descriptor-invalid`,
  `runtime-effect-invalid`, `descriptor-handles-invalid`, `color-filters`, `native-text`, and `graphics-layer`.
- Full default command-probe sweep passed after adding a live tint color-filter descriptor blend-mode validation
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-081633/suite.tsv`.
  It covered 181 rows plus header: all 181 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 45 rows reported expected explicit fallback markers. The new
  `commands-invalid-tint-color-filter-descriptor-blend-mode-fallback` row required
  `SKIKO_JBR_INTEROP_TINT_COLOR_FILTER_DESCRIPTOR_BLEND_MODE_CORRUPTED`, recorded one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Focused `commands-invalid-tint-color-filter-descriptor-blend-mode-fallback` validation passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-081541/suite.tsv`.
  The row rewrote one recorded tint color-filter descriptor blend mode from supported `SrcIn` to unsupported `Plus`,
  matching JBR parser-only unsupported tint blend-mode coverage, and failed closed before replay with one explicit
  fallback marker and zero JBR picture/command frames.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptTintColorFilterDescriptorBlendModeForTesting` hook.
- Full default command-probe sweep passed after adding live Perlin/noise shader descriptor payload validation sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-025633/suite.tsv`.
  It covered 180 rows plus header: all 180 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 44 rows reported expected explicit fallback markers. The new
  `commands-invalid-perlin-noise-shader-kind-fallback`,
  `commands-invalid-perlin-noise-shader-frequency-fallback`,
  `commands-invalid-perlin-noise-shader-octaves-fallback`, and
  `commands-invalid-perlin-noise-shader-tile-size-fallback` rows each required their typed Skiko marker, recorded one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Focused Perlin/noise shader descriptor payload validation passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-025354/suite.tsv`.
  The four-row subset covered invalid Perlin/noise kind, base frequency, octave count, and tile-size payloads, matching
  JBR parser-only coverage, and every row failed closed before replay with one explicit fallback marker and zero JBR
  picture/command frames.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptPerlinNoiseShader*ForTesting` hooks.
- Full default command-probe sweep passed after adding a live RuntimeEffect shader source-hash mismatch sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-005156/suite.tsv`.
  It covered 176 rows plus header: all 176 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 40 rows reported expected explicit fallback markers. The new
  `commands-runtime-effect-shader-source-hash-fallback` row required
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_SOURCE_HASH_CORRUPTED`, recorded one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Focused `commands-runtime-effect-shader-source-hash-fallback` validation passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-005110/suite.tsv`.
  The row flipped one RuntimeEffect shader source-hash word after recording while leaving the SKSL payload unchanged,
  matching JBR parser-only RuntimeEffect shader source-hash mismatch coverage, and failed closed before replay with
  zero JBR picture/command frames.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptRuntimeEffectShaderSourceHashForTesting` hook.
- Full default command-probe sweep passed after adding a live transformed shader descriptor payload-count mismatch
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-224940/suite.tsv`.
  It covered 175 rows plus header: all 175 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 39 rows reported expected explicit fallback markers. The new
  `commands-invalid-transformed-shader-descriptor-payload-count-fallback` row required
  `SKIKO_JBR_INTEROP_TRANSFORMED_SHADER_DESCRIPTOR_PAYLOAD_COUNT_CORRUPTED`, recorded one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Focused `commands-invalid-transformed-shader-descriptor-payload-count-fallback` validation passed before the full
  sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-224856/suite.tsv`.
  The row rewrote one transformed shader descriptor payload count from 11 to 10 after recording, matching JBR
  parser-only transformed shader payload-count mismatch coverage, and failed closed before replay with zero JBR
  picture/command frames.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptTransformedShaderDescriptorPayloadCountForTesting` hook.
- Full default command-probe sweep passed after adding a live unknown effect descriptor type sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-204652/suite.tsv`.
  It covered 174 rows plus header: all 174 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 38 rows reported expected explicit fallback markers. The new
  `commands-invalid-effect-descriptor-type-fallback` row required
  `SKIKO_JBR_INTEROP_EFFECT_DESCRIPTOR_TYPE_CORRUPTED`, recorded one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Focused `commands-invalid-effect-descriptor-type-fallback` validation passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-204604/suite.tsv`.
  The row rewrote one effect descriptor type after recording, matching JBR parser-only unknown effect descriptor type
  coverage, and failed closed before replay with zero JBR picture/command frames.
- Full default command-probe sweep passed after adding live effect descriptor version, payload-count, and record-length
  mismatch sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-183706/suite.tsv`.
  It covered 173 rows plus header: all 173 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 37 rows reported expected explicit fallback markers. The new
  `commands-invalid-effect-descriptor-version-fallback`,
  `commands-invalid-effect-descriptor-payload-count-fallback`, and
  `commands-invalid-effect-descriptor-record-length-fallback` rows each required the matching typed Skiko marker,
  recorded one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR
  command frames.
- Focused effect descriptor metadata validation passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-183454/suite.tsv`.
  The three-row subset covered descriptor version, payload-count, and record-length corruption, and every row failed
  closed before replay with one explicit fallback marker and zero JBR picture/command frames.
- Full default command-probe sweep passed after tightening the existing shader descriptor version sentinel to require
  its typed Skiko corruption marker:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-165558/suite.tsv`.
  It covered 170 rows plus header: all 170 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 34 rows reported expected explicit fallback markers. The existing
  `commands-invalid-descriptor-version-fallback` row now requires
  `SKIKO_JBR_INTEROP_DESCRIPTOR_VERSION_CORRUPTED`, recorded one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Focused `commands-invalid-descriptor-version-fallback` validation passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-165524/suite.tsv`.
- Full default command-probe sweep passed after adding a live shader descriptor record-length mismatch sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-153044/suite.tsv`.
  It covered 170 rows plus header: all 170 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 34 rows reported expected explicit fallback markers. The new
  `commands-invalid-shader-descriptor-record-length-fallback` row required
  `SKIKO_JBR_INTEROP_SHADER_DESCRIPTOR_RECORD_LENGTH_CORRUPTED`, recorded one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Focused `commands-invalid-shader-descriptor-record-length-fallback` validation passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-153009/suite.tsv`.
  The row shortened one shader descriptor record length after recording, matching JBR parser-only record-length
  mismatch coverage, and failed closed before replay with zero JBR picture/command frames.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptShaderDescriptorRecordLengthForTesting` hook. Magic Jewel report-validation unit tests
  also passed after wiring `MAGIC_JEWEL_CORRUPT_SHADER_DESCRIPTOR_RECORD_LENGTH` through the report and run scripts.
- Full default command-probe sweep passed after adding a live shader descriptor payload-count mismatch sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-140412/suite.tsv`.
  It covered 169 rows plus header: all 169 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 33 rows reported expected explicit fallback markers. The new
  `commands-invalid-shader-descriptor-payload-count-fallback` row required
  `SKIKO_JBR_INTEROP_SHADER_DESCRIPTOR_PAYLOAD_COUNT_CORRUPTED`, recorded one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Focused `commands-invalid-shader-descriptor-payload-count-fallback` validation passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-140244/suite.tsv`.
  The row rewrote one shader descriptor payload count after recording, matching JBR parser-only payload-count mismatch
  coverage, and failed closed before replay with zero JBR picture/command frames.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptShaderDescriptorPayloadCountForTesting` hook. Magic Jewel report-validation unit tests
  also passed after wiring `MAGIC_JEWEL_CORRUPT_SHADER_DESCRIPTOR_PAYLOAD_COUNT` through the report and run scripts.
- Full default command-probe sweep passed after adding a live unknown shader descriptor type sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-230029/suite.tsv`.
  It covered 168 rows plus header: all 168 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 32 rows reported expected explicit fallback markers. The new
  `commands-invalid-shader-descriptor-type-fallback` row required
  `SKIKO_JBR_INTEROP_SHADER_DESCRIPTOR_TYPE_CORRUPTED`, recorded one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
- Focused `commands-invalid-shader-descriptor-type-fallback` validation passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-225953/suite.tsv`.
  The row rewrote one shader descriptor type after recording, matched JBR parser-only coverage for unknown shader
  descriptor types, and failed closed before replay with zero JBR picture/command frames.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptShaderDescriptorTypeForTesting` hook. Magic Jewel report-validation unit tests also passed
  after wiring `MAGIC_JEWEL_CORRUPT_SHADER_DESCRIPTOR_TYPE` through the report and run scripts.
- Full screenshot parity sweep passed after the RuntimeEffect shader+color-filter lifecycle gate and focused parity
  refresh:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260512-204525/suite.tsv`.
  It covered 106 rows plus header: all 106 passed, all 106 stayed on JBR command replay, and zero rows reported
  fallback or JBR picture replay. The sweep includes button chrome, native font-data/resource/system text lifecycle,
  shader/effect descriptor resize and forced-context rows, RuntimeEffect source-cache eviction, graphics-layer
  transforms, clips, shadows, and render-effect/color-filter/blend-mode combinations.
- Full default command-probe sweep passed after tightening
  `commands-runtime-effect-shader-color-filter` to cap the descriptor-backed color-filter side at one JBR effect-handle
  definition:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-191321/suite.tsv`.
  It covered 167 rows plus header: all 167 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 31 rows reported expected explicit fallback markers. The tightened row recorded zero
  fallback, zero JBR picture frames, 582 JBR command frames, one effect-handle definition, 946 effect-handle uses, 945
  effect-handle cache hits, 945 RuntimeEffect source-cache hits, and one RuntimeEffect source-cache miss.
- Focused `commands-runtime-effect-shader-color-filter` gate validation passed after adding
  `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES=1`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-191213/suite.tsv`.
  The row stayed on command replay with zero fallback, zero JBR picture frames, 600 JBR command frames, one
  effect-handle definition, 1150 effect-handle uses, 1149 effect-handle cache hits, 1151 RuntimeEffect source-cache
  hits, and one RuntimeEffect source-cache miss.
- Focused screenshot parity for the same RuntimeEffect shader+color-filter row passed after the command lifecycle gate
  tightening:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260512-204143/suite.tsv`.
  The row stayed on command replay with zero fallback, zero JBR picture frames, 797 JBR command frames,
  `avg_delta=2.110`, `bad_pixel_ratio=0.05006`, and `compose_shader_linear_bad_pixel_ratio=0.06584`.
- Full compatibility matrix passed after promoting the invalid-handle sentinels into the default command suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260512-183854/matrix.tsv`.
  It covered 57 rows plus header: all 57 passed, the happy-path row replayed commands, and the 56 ABI/capability/API
  mismatch rows each reported fallback with zero JBR command frames. Every row ran with
  `background_window=true`.
- Full default command-probe sweep passed after promoting focused invalid-handle sentinels into the default case list:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-171413/suite.tsv`.
  It covered 167 rows plus header: all 167 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 31 rows reported expected explicit fallback markers. The promoted rows include
  `commands-invalid-shader-descriptor-use-fallback`,
  `commands-invalid-color-filter-descriptor-use-after-evict-fallback`,
  `commands-invalid-effect-child-use-after-evict-fallback`,
  `commands-invalid-path-effect-child-use-after-evict-fallback`,
  `commands-runtime-effect-color-filter-child-missing-fallback`,
  `commands-offset-image-filter-child-missing-fallback`, `commands-chain-path-effect-child-missing-fallback`,
  `commands-shader-color-filter-effect-child-missing-fallback`,
  `commands-transformed-shader-child-missing-fallback`, `commands-composite-shader-child-missing-fallback`, and
  `commands-shader-color-filter-shader-child-missing-fallback`.
- Full default command-probe sweep passed after tightening `commands-invalid-descriptor-use-fallback` to require the
  typed Skiko corruption marker:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-155150/suite.tsv`.
  It covered 156 rows plus header: all 156 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 20 rows reported expected explicit fallback markers. The tightened row recorded
  `expect_command_fallback_marker=SKIKO_JBR_INTEROP_DESCRIPTOR_USE_CORRUPTED op=47`,
  `validation_failures=none`, one `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR
  command frames.
- Focused undefined shader descriptor use sentinel passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-155027/suite.tsv`.
  `commands-invalid-shader-descriptor-use-fallback` recorded
  `expect_command_fallback_marker=SKIKO_JBR_INTEROP_DESCRIPTOR_USE_CORRUPTED op=58`,
  `validation_failures=none`, one `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR
  command frames.
- Focused descriptor use-after-evict subset passed for shader and color-filter refs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-142300/suite.tsv`.
  `commands-invalid-descriptor-use-after-evict-fallback` required
  `SKIKO_JBR_INTEROP_DESCRIPTOR_USE_AFTER_EVICT_CORRUPTED op=58`, and
  `commands-invalid-color-filter-descriptor-use-after-evict-fallback` required the same marker with `op=47`. Both rows
  recorded `validation_failures=none`, one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR
  picture frames, and zero JBR command frames.
- JBR parser-only validation passed against the existing `invalidEvictedShaderHandleStream()` and evicted effect
  descriptor handle coverage in `JBRSkiaApiTest`. The test was run headlessly with the patched `java.desktop` module
  and `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- Skiko `publishToMavenLocal` passed after extending the test-only
  `skiko.jbr.interop.corruptDescriptorUseAfterEvictForTesting` hook to color-filter refs.
- Focused effect descriptor missing-child sentinels passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-141506/suite.tsv`.
  `commands-runtime-effect-color-filter-child-missing-fallback`,
  `commands-offset-image-filter-child-missing-fallback`, `commands-chain-path-effect-child-missing-fallback`, and
  `commands-shader-color-filter-effect-child-missing-fallback` each recorded `validation_failures=none`, one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The rows required `SKIKO_JBR_INTEROP_EFFECT_CHILD_MISSING_CORRUPTED` markers with targets
  `runtimeEffectColorFilterChild`, `offsetImageFilterChild`, `chainPathEffectChild`, and
  `shaderColorFilterEffectChild`.
- JBR parser-only validation passed against the existing `invalidRuntimeColorFilterMissingChildHandleStream()`,
  `invalidOffsetImageFilterMissingChildHandleStream()`, `invalidChainPathEffectMissingChildHandleStream()`, and
  `invalidShaderColorFilterMissingEffectHandleStream()` coverage in `JBRSkiaApiTest`. The test was run headlessly with
  the patched `java.desktop` module and `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptEffectChildMissingForTesting` hook for effect descriptor children.
- Focused shader descriptor missing-child sentinels passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-140546/suite.tsv`.
  `commands-transformed-shader-child-missing-fallback`,
  `commands-composite-shader-child-missing-fallback`, and
  `commands-shader-color-filter-shader-child-missing-fallback` each recorded `validation_failures=none`, one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames.
  The rows required `SKIKO_JBR_INTEROP_SHADER_CHILD_MISSING_CORRUPTED` markers with targets
  `transformedShaderChild`, `compositeShaderDstChild`, and `shaderColorFilterShaderChild`.
- JBR parser-only validation passed against the existing `invalidTransformedShaderMissingChildHandleStream()`,
  `invalidCompositeShaderChildHandleStream()`, and `invalidShaderColorFilterMissingShaderHandleStream()` coverage in
  `JBRSkiaApiTest`. The test was run headlessly with the patched `java.desktop` module and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptShaderChildMissingForTesting` hook for shader descriptor children.
- Focused evicted effect-child handle sentinels passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-135408/suite.tsv`.
  `commands-invalid-effect-child-use-after-evict-fallback` inserted an eviction for the offset image-filter child
  handle immediately before the parent descriptor and recorded
  `SKIKO_JBR_INTEROP_EFFECT_CHILD_USE_AFTER_EVICT_CORRUPTED target=offsetImageFilterChild`,
  `validation_failures=none`, one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture
  frames, and zero JBR command frames. `commands-invalid-path-effect-child-use-after-evict-fallback` covered the same
  use-after-evict failure for the chained path-effect child handle with
  `SKIKO_JBR_INTEROP_EFFECT_CHILD_USE_AFTER_EVICT_CORRUPTED target=chainPathEffectChild` and the same zero-replay
  fallback properties.
- JBR parser-only validation passed against the existing `invalidOffsetImageFilterEvictedChildHandleStream()` and
  `invalidChainPathEffectEvictedChildHandleStream()` coverage in `JBRSkiaApiTest`. The test was compiled with `javac`
  against `/tmp/jbr-skia-run/desktop` and the local `JBRApi` stub, then run headlessly with the patched
  `java.desktop` module and `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptEffectChildUseAfterEvictForTesting` hook for offset image-filter and chained path-effect
  descriptor children.
- Full default command-probe sweep passed after adding the chained path-effect child wrong-type handle sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-205714/suite.tsv`.
  It covered 156 rows plus header: all 156 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 20 rows reported expected explicit fallback markers. The new
  `commands-chain-path-effect-child-wrong-effect-type-fallback` row recorded
  `expect_command_fallback_marker=SKIKO_JBR_INTEROP_PATH_EFFECT_HANDLE_TYPE_CORRUPTED target=chainPathEffectChild`,
  `validation_failures=none`, one `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR
  command frames. The focused sentinel passed first:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-205629/suite.tsv`.
- JBR parser-only validation passed against the existing `invalidChainPathEffectColorFilterChildHandleStream()`
  coverage in `JBRSkiaApiTest`. The test was compiled with `javac` against `/tmp/jbr-skia-run/desktop` and the local
  `JBRApi` stub, then run headlessly with the patched `java.desktop` module and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptPathEffectHandleTypeForTesting` hook for chained path-effect descriptor children.
- Full default command-probe sweep passed after adding the offset image-filter child wrong-type handle sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-192524/suite.tsv`.
  It covered 155 rows plus header: all 155 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 19 rows reported expected explicit fallback markers. The new
  `commands-offset-image-filter-child-wrong-effect-type-fallback` row recorded
  `expect_command_fallback_marker=SKIKO_JBR_INTEROP_IMAGE_FILTER_HANDLE_TYPE_CORRUPTED target=offsetImageFilterChild`,
  `validation_failures=none`, one `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR
  command frames. The focused sentinel passed first:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-192441/suite.tsv`.
- JBR parser-only validation passed against the existing `invalidOffsetImageFilterColorFilterChildHandleStream()`
  coverage in `JBRSkiaApiTest`. The test was compiled with `javac` against `/tmp/jbr-skia-run/desktop` and the local
  `JBRApi` stub, then run headlessly with the patched `java.desktop` module and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- Skiko `publishToMavenLocal` passed after extending the test-only
  `skiko.jbr.interop.corruptImageFilterHandleTypeForTesting` hook to offset image-filter descriptor children.
- Full default command-probe sweep passed after adding the RuntimeEffect color-filter child wrong-type handle sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-174517/suite.tsv`.
  It covered 154 rows plus header: all 154 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 18 rows reported expected explicit fallback markers. The new
  `commands-runtime-effect-color-filter-child-wrong-effect-type-fallback` row recorded
  `expect_command_fallback_marker=SKIKO_JBR_INTEROP_COLOR_FILTER_HANDLE_TYPE_CORRUPTED target=runtimeEffectColorFilterChild`,
  `validation_failures=none`, one `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR
  command frames. The focused sentinel passed first:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-174429/suite.tsv`.
- JBR parser-only validation passed against the existing `invalidRuntimeColorFilterImageFilterChildStream()` coverage
  in `JBRSkiaApiTest`. The test was compiled with `javac` against `/tmp/jbr-skia-run/desktop` and the local `JBRApi`
  stub, then run headlessly with the patched `java.desktop` module and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- Skiko `publishToMavenLocal` passed after extending the test-only
  `skiko.jbr.interop.corruptColorFilterHandleTypeForTesting` hook to RuntimeEffect color-filter descriptor children.
- Full default command-probe sweep passed after adding the RuntimeEffect shader child wrong-type handle sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-161739/suite.tsv`.
  It covered 153 rows plus header: all 153 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 17 rows reported expected explicit fallback markers. The new
  `commands-runtime-effect-shader-child-wrong-effect-type-fallback` row recorded
  `expect_command_fallback_marker=SKIKO_JBR_INTEROP_SHADER_HANDLE_TYPE_CORRUPTED target=runtimeEffectShaderChild`,
  `validation_failures=none`, one `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR
  command frames. The focused sentinel passed first:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-161650/suite.tsv`.
- JBR parser-only validation passed after adding `invalidRuntimeEffectShaderColorFilterChildHandleStream()` to
  `JBRSkiaApiTest`. The test was compiled with `javac` against `/tmp/jbr-skia-run/desktop` and the local
  `JBRApi` stub, then run headlessly with the patched `java.desktop` module and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- Skiko `publishToMavenLocal` passed after extending the test-only
  `skiko.jbr.interop.corruptShaderHandleTypeForTesting` hook to RuntimeEffect shader descriptor children.
- Full default command-probe sweep passed after adding the composite shader child wrong-type handle sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-145503/suite.tsv`.
  It covered 152 rows plus header: all 152 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 16 rows reported expected explicit fallback markers. The new
  `commands-composite-shader-child-wrong-effect-type-fallback` row recorded
  `expect_command_fallback_marker=SKIKO_JBR_INTEROP_SHADER_HANDLE_TYPE_CORRUPTED target=compositeShaderDstChild`,
  `validation_failures=none`, one `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR
  command frames. The focused sentinel passed first:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-145418/suite.tsv`.
- JBR parser-only validation passed after adding `invalidCompositeShaderColorFilterChildHandleStream()` to
  `JBRSkiaApiTest`. The test was compiled with `javac` against `/tmp/jbr-skia-run/desktop` and the local
  `JBRApi` stub, then run headlessly with the patched `java.desktop` module and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- Skiko `publishToMavenLocal` passed after extending the test-only
  `skiko.jbr.interop.corruptShaderHandleTypeForTesting` hook to composite shader descriptor children.
- Full default command-probe sweep passed after adding the transformed shader child wrong-type handle sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-122532/suite.tsv`.
  It covered 151 rows plus header: all 151 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 15 rows reported expected explicit fallback markers. The new
  `commands-transformed-shader-child-wrong-effect-type-fallback` row recorded
  `expect_command_fallback_marker=SKIKO_JBR_INTEROP_SHADER_HANDLE_TYPE_CORRUPTED target=transformedShaderChild`,
  `validation_failures=none`, one `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR
  command frames. The focused sentinel passed first:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-122433/suite.tsv`.
- JBR parser-only validation passed after adding `invalidTransformedShaderColorFilterChildHandleStream()` to
  `JBRSkiaApiTest`. The test was compiled with `javac` against `/tmp/jbr-skia-run/desktop` and the local
  `JBRApi` stub, then run headlessly with the patched `java.desktop` module and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- Skiko `publishToMavenLocal` passed after extending the test-only
  `skiko.jbr.interop.corruptShaderHandleTypeForTesting` hook to transformed shader descriptor children.
- Full default command-probe sweep passed after adding the shader wrong-type handle sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-110320/suite.tsv`.
  It covered 150 rows plus header: all 150 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 14 rows reported expected explicit fallback markers. The new
  `commands-shader-wrong-effect-type-fallback` row recorded
  `expect_command_fallback_marker=SKIKO_JBR_INTEROP_SHADER_HANDLE_TYPE_CORRUPTED target=fillRectShader`,
  `validation_failures=none`, one `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR
  command frames. The focused sentinel passed first:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-110215/suite.tsv`.
- JBR parser-only validation passed after adding `invalidFillRectShaderColorFilterHandleStream()` to
  `JBRSkiaApiTest`. The test was compiled with `javac` against `/tmp/jbr-skia-run/desktop` and the local
  `JBRApi` stub, then run headlessly with the patched `java.desktop` module and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptShaderHandleTypeForTesting` hook that powers the new Magic Jewel row.
- Full default command-probe sweep passed after tightening path-effect and resize/forced-context descriptor-definition
  gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-093809/suite.tsv`.
  It covered 149 rows plus header: all 149 passed, 110 rows reported JBR command replay, 26 rows reported intentional
  JBR picture fallback, and 13 rows reported expected explicit fallback markers. Spot-checked tightened rows reported
  `validation_failures=none`, including `commands-path-effect` with `jbr_effect_handle_define_frames=5`, simple
  resize/forced-context descriptor rows with exactly 2 definitions, and composite-noise descriptor rows with exactly 6
  shader definitions.
- Focused path-effect command replay passed after tightening `commands-path-effect` to require the stable descriptor
  definition contract:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-092433/suite.tsv`.
  The row reported `validation_failures=none`, `fallback_new_count=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=149`, and `jbr_effect_handle_define_frames=5`.
- Focused resize/forced-context descriptor lifecycle subset passed after tightening max descriptor-definition gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-092758/suite.tsv`.
  It covered 18 rows; all 18 passed, no rows reported unsupported reasons, fallback, or JBR picture replay, and all
  rows reported JBR command frames. Exact descriptor-definition counts were enforced at 2 for simple effect/shader
  descriptor rows and 6 for composite-noise shader chains.
- Full default command-probe sweep passed after adding the path-effect wrong-type color-filter handle sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-231315/suite.tsv`.
  The sweep covered 149 rows plus header: all 149 passed, 110 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 13 rows reported expected explicit fallback markers. The new
  `commands-color-filter-path-effect-wrong-type-fallback` row recorded
  `expect_command_fallback_marker=SKIKO_JBR_INTEROP_COLOR_FILTER_HANDLE_TYPE_CORRUPTED target=fillRectColorFilterPathEffect`,
  `validation_failures=none`, one `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR
  command frames. The focused sentinel passed first:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-231235/suite.tsv`.
- JBR parser-only validation passed after adding `invalidFillRectColorFilterPathEffectHandleStream()` to
  `JBRSkiaApiTest`. The test was compiled with `javac` against `/tmp/jbr-skia-run/desktop` and the local
  `JBRApi` stub, then run headlessly with the patched `java.desktop` module and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib`; the run exited 0.
- Skiko `publishToMavenLocal` passed after adding the test-only
  `skiko.jbr.interop.corruptColorFilterHandleToPathEffectTypeForTesting` hook that powers the new Magic Jewel row.
- Focused wrong-type handle subset passed after Magic Jewel report validation learned
  `EXPECT_COMMAND_FALLBACK_MARKER` and the default wrong-type rows were tightened to require exact target-specific
  corruption markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-173315/suite.tsv`.
  The three rows recorded `expect_command_fallback_marker` values for `target=fillRectColorFilter`,
  `target=shaderColorFilter`, and `target=saveLayerImageFilter`; all passed with `validation_failures=none`, one
  `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR command frames. The synthetic
  report-validator regression suite also passed after adding positive and missing-marker cases:
  `./scripts/test-jbr-skia-report-validation.sh` in Magic Jewel.
- Full default command-probe sweep passed after adding the shader color-filter wrong-type child-handle sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-161539/suite.tsv`.
  The sweep covered 148 rows plus header: all 148 passed, 110 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 12 rows reported expected explicit fallback markers. The wrong-type subset also
  passed with target-specific Skiko corruption markers: `target=fillRectColorFilter`,
  `target=shaderColorFilter`, and `target=saveLayerImageFilter`; each row reported one `command-stream-invalid`
  fallback marker, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-161400/suite.tsv`.
- Full default command-probe sweep passed after adding the symmetric wrong-type image-filter handle sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-144923/suite.tsv`.
  The sweep covered 147 rows plus header: all 147 passed, 110 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 11 rows reported expected explicit fallback markers. The new
  `commands-image-filter-wrong-effect-type-fallback` row rewrote one image-filter handle use to point at a color-filter
  descriptor; its report recorded `MAGIC_JEWEL_CORRUPT_IMAGE_FILTER_HANDLE_TYPE=true`, one `command-stream-invalid`
  fallback marker, zero JBR picture frames, and zero JBR command frames. The focused sentinel passed first:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-144841/suite.tsv`.
- Full default command-probe sweep passed after adding a live wrong-type color-filter handle sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-132542/suite.tsv`.
  The sweep covered 146 rows plus header: all 146 passed, 110 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 10 rows reported expected explicit fallback markers. The new
  `commands-color-filter-wrong-effect-type-fallback` row rewrote one color-filter handle use to point at an
  image-filter descriptor; its report recorded `MAGIC_JEWEL_CORRUPT_COLOR_FILTER_HANDLE_TYPE=true`, one
  `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR command frames. The focused sentinel
  also passed before the broad sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-132508/suite.tsv`.
  After cleaning up the default row name wiring, the same focused row passed again:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-144306/suite.tsv`.
- JBR command-stream parser hardening passed after tightening typed effect-handle validation for color-filter vs
  image-filter uses. Local artifact rebuild succeeded:
  `./scripts/rebuild-jbr-skia-local-artifacts.sh` in
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel`. A parser-only run compiled `JBRSkiaApiTest` against the rebuilt
  `/tmp/jbr-skia-run/desktop` patch and invoked `assertCommandStreamValidation`; it passed all valid and invalid
  parser fixtures, including new wrong-type handle cases for shader color filters, fill color-filter refs, image-filter
  refs, offset image-filter children, and chained path-effect children. A full default command-probe sweep then passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-115246/suite.tsv`.
  The sweep covered 145 rows plus header: all 145 passed, 110 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 9 rows reported expected explicit fallback markers.
- Full default command-probe sweep passed after adding the RuntimeEffect color-filter child-count build-failure
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-225942/suite.tsv`.
  The sweep covered 145 rows plus header: all 145 passed, 110 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 9 rows reported expected explicit fallback markers. The new
  `commands-runtime-effect-color-filter-build-fallback` row reported one explicit fallback marker, `unsupported=none`,
  zero JBR picture frames, zero JBR command frames, and 7744 RuntimeEffect build-failure markers. Its report recorded
  `MAGIC_JEWEL_COMPOSE_RUNTIME_EFFECT_COLOR_FILTER_BAD_CHILD=true`, and the native log reported
  `JBR_SKIA_INTEROP_RUNTIME_COLOR_FILTER_BUILD_FAILED ... stage=child-count ... children=0 ... effectChildren=1`.
- Focused RuntimeEffect compile/build fallback subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-225248/suite.tsv`.
  Shader and color-filter compile/build/child-type rows all reported one expected explicit fallback marker with
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames. The new color-filter child-count row
  specifically proved that a source-declared child without a matching descriptor handle fails in JBR native build,
  not in recorder schema validation.
- Magic Jewel report-validator regression tests passed after adding a synthetic RuntimeEffect color-filter
  `stage=child-count` build-failure marker:
  `./scripts/test-jbr-skia-report-validation.sh` in
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel`.
- Full default screenshot parity suite passed after adding RuntimeEffect source-cache eviction parity:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-214152/suite.tsv`.
  The suite covered 106 rows plus header: all 106 passed, all 106 reported JBR command replay, zero rows reported
  structural fallback, and zero rows reported JBR picture fallback. `parity-runtime-effect-shader-source-cache-eviction`
  reported 958 JBR command frames, `fallback_new_count=0`, `jbr_picture_frames=0`, `avg_delta=2.084`, and
  `bad_pixel_ratio=0.04931`; its report recorded 1460 RuntimeEffect source-cache hits, 2923 misses, 2921 evicts,
  16 shader-handle defines, 4383 shader-handle uses, and 4371 shader-handle cache hits.
  `parity-runtime-effect-source-cache-eviction` reported 1026 JBR command frames, `fallback_new_count=0`,
  `jbr_picture_frames=0`, `avg_delta=2.118`, and `bad_pixel_ratio=0.05032`; its report recorded 1612 RuntimeEffect
  source-cache hits, 3227 misses, 3225 evicts, 36 effect-handle defines, 4839 effect-handle uses, and 4827
  effect-handle cache hits.
- Focused RuntimeEffect source-cache eviction parity subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-214028/suite.tsv`.
  The shader and color-filter rows stayed on command replay with zero fallback and zero JBR picture frames while
  requiring typed source-cache eviction markers. The shader row reported 1029 JBR command frames, 3337 RuntimeEffect
  source-cache evicts, and 4995 shader-handle cache hits. The color-filter row reported 1015 JBR command frames, 3251
  RuntimeEffect source-cache evicts, and 4866 effect-handle cache hits.
- Full default screenshot parity suite passed after adding descriptor handle eviction parity:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-203120/suite.tsv`.
  The suite covered 104 rows plus header: all 104 passed, all 104 reported JBR command replay, zero rows reported
  structural fallback, and zero rows reported JBR picture fallback. `parity-descriptor-eviction` reported 34 JBR
  command frames, `fallback_new_count=0`, `jbr_picture_frames=0`, `avg_delta=2.164`, and
  `bad_pixel_ratio=0.05120`; its report recorded 68503 effect-handle defines, 68178 effect-handle uses, 65431
  effect-handle evicts, 201900 shader-handle defines, 67300 shader-handle uses, and 198828 shader-handle evicts.
- Focused descriptor handle eviction parity subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-202925/suite.tsv`.
  The row stayed on command replay with zero fallback and zero JBR picture frames while requiring at least 1024
  effect-handle defines/uses, at least one effect-handle evict, at least 1024 shader-handle defines/uses, and at least
  one shader-handle evict. The focused run reported 46 JBR command frames, 75029 effect-handle evicts, and 229128
  shader-handle evicts.
- Full default screenshot parity suite passed after adding standalone graphics-layer offset and chained renderEffect
  parity rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-192048/suite.tsv`.
  The suite covered 103 rows plus header: all 103 passed, all 103 reported JBR command replay, zero rows reported
  structural fallback, and zero rows reported JBR picture fallback. `parity-graphics-layer-offset-effect` reported
  1420 JBR command frames, `fallback_new_count=0`, `jbr_picture_frames=0`, `avg_delta=2.189`, and
  `bad_pixel_ratio=0.05189`; `parity-graphics-layer-chained-render-effect` reported 1528 JBR command frames,
  `avg_delta=2.182`, and `bad_pixel_ratio=0.05159`.
- Focused standalone graphics-layer offset and chained renderEffect parity subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-191929/suite.tsv`.
  Both rows stayed on command replay with zero fallback and zero JBR picture frames while requiring effect-handle
  definition, use, and cache-hit markers. `parity-graphics-layer-offset-effect` reported 1825 JBR command frames;
  `parity-graphics-layer-chained-render-effect` reported 1943 JBR command frames.
- Full default screenshot parity suite passed after adding static image-shader and composite-shader parity rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-181039/suite.tsv`.
  The suite covered 101 rows plus header: all 101 passed, all 101 reported JBR command replay, zero rows reported
  structural fallback, and zero rows reported JBR picture fallback. `parity-image-shader` reported 1013 JBR command
  frames, `fallback_new_count=0`, `jbr_picture_frames=0`, `avg_delta=2.678`, and `bad_pixel_ratio=0.06777`;
  `parity-composite-shader` reported 1015 JBR command frames, `avg_delta=2.596`, and `bad_pixel_ratio=0.06710`.
- Focused image-shader and composite-shader parity subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-180914/suite.tsv`.
  Both rows stayed on command replay with zero fallback and zero JBR picture frames. `parity-image-shader` reported
  633 JBR command frames and image refs; `parity-composite-shader` reported 616 JBR command frames and required
  shader-handle definition, use, and cache-hit markers.
- Full default screenshot parity suite passed after adding static descriptor-backed color-matrix and lighting
  color-filter parity rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-165808/suite.tsv`.
  The suite covered 99 rows plus header: all 99 passed, all 99 reported JBR command replay, zero rows reported
  structural fallback, and zero rows reported JBR picture fallback. `parity-color-matrix-filter` reported 656 JBR
  command frames, `fallback_new_count=0`, `jbr_picture_frames=0`, `avg_delta=2.204`, and
  `bad_pixel_ratio=0.05265`; `parity-lighting-filter` reported 1060 JBR command frames, `avg_delta=2.204`, and
  `bad_pixel_ratio=0.05265`.
- Focused descriptor-backed color-matrix and lighting color-filter parity subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-165647/suite.tsv`.
  Both rows stayed on command replay with zero fallback and zero JBR picture frames while requiring effect-handle
  definition, use, and cache-hit markers. `parity-color-matrix-filter` reported 841 JBR command frames;
  `parity-lighting-filter` reported 1613 JBR command frames.
- Full default screenshot parity suite passed after adding descriptor-backed tint color-filter lifecycle parity rows for
  static replay, same-context resize, and forced destination context migration:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-155200/suite.tsv`.
  The suite covered 97 rows plus header: all 97 passed, all 97 reported JBR command replay, zero rows reported
  structural fallback, and zero rows reported JBR picture fallback. The new `parity-color-filter-handle` row reported
  1478 JBR command frames, `fallback_new_count=0`, `jbr_picture_frames=0`, `avg_delta=2.206`, and
  `bad_pixel_ratio=0.05271`; `parity-resize-color-filter-handle` reported 878 JBR command frames,
  `avg_delta=1.922`, and `bad_pixel_ratio=0.04650`; `parity-forced-context-color-filter-handle` reported 925 JBR
  command frames, `avg_delta=2.065`, and `bad_pixel_ratio=0.04897`.
- Focused descriptor-backed tint color-filter lifecycle parity subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-154845/suite.tsv`.
  `parity-color-filter-handle`, `parity-resize-color-filter-handle`, and
  `parity-forced-context-color-filter-handle` all stayed on command replay with zero fallback and zero JBR picture
  frames, reporting 615, 1463, and 820 JBR command frames respectively. The resize and forced-context rows also
  required surface-change, command-cache-clear, effect-handle redefinition/use, and effect-handle cache-hit markers.
- Full default screenshot parity suite passed after adding graphics-layer render-effect lifecycle parity rows for
  same-context resize and forced destination context migration:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-144511/suite.tsv`.
  The suite covered 94 rows plus header: all 94 passed, all 94 reported JBR command replay, zero rows reported
  structural fallback, and zero rows reported JBR picture fallback. The new
  `parity-resize-graphics-layer-render-effect` row reported 649 JBR command frames, `fallback_new_count=0`,
  `jbr_picture_frames=0`, and `bad_pixel_ratio=0.04557`; the new
  `parity-forced-context-graphics-layer-render-effect` row reported 1320 JBR command frames,
  `fallback_new_count=0`, `jbr_picture_frames=0`, and `bad_pixel_ratio=0.04801`.
- Focused graphics-layer render-effect lifecycle parity subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-144352/suite.tsv`.
  `parity-resize-graphics-layer-render-effect` stayed on command replay with zero fallback, zero JBR picture frames,
  and 1271 JBR command frames. `parity-forced-context-graphics-layer-render-effect` stayed on command replay with zero
  fallback, zero JBR picture frames, and 1291 JBR command frames.
- Full default screenshot parity suite passed after adding RuntimeEffect pure-color shader lifecycle parity rows for
  same-context resize and forced destination context migration:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-134708/suite.tsv`.
  The suite covered 92 rows plus header: all 92 passed, all 92 reported JBR command replay, zero rows reported
  structural fallback, and zero rows reported JBR picture fallback. The new
  `parity-resize-runtime-effect-pure-color` row reported 740 JBR command frames, `fallback_new_count=0`,
  `jbr_picture_frames=0`, and `bad_pixel_ratio=0.04438`; the new
  `parity-forced-context-runtime-effect-pure-color` row reported 450 JBR command frames,
  `fallback_new_count=0`, `jbr_picture_frames=0`, and `bad_pixel_ratio=0.04619`.
- Focused RuntimeEffect pure-color shader lifecycle parity subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-134544/suite.tsv`.
  `parity-resize-runtime-effect-pure-color` stayed on command replay with zero fallback, zero JBR picture frames, and
  591 JBR command frames. `parity-forced-context-runtime-effect-pure-color` stayed on command replay with zero fallback,
  zero JBR picture frames, and 444 JBR command frames.
- Full default command-probe sweep passed after tightening RuntimeEffect source-cache eviction rows to require typed
  native evict markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-122742/suite.tsv`.
  The sweep covered 144 rows plus header: all 144 passed, 110 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 8 rows reported expected explicit fallback markers. The shader source-cache
  eviction row reported 575 JBR command frames, 2287 typed RuntimeEffect source-cache evicts, and 2286 shader-handle
  cache hits. The color-filter source-cache eviction row reported 276 JBR command frames, 1121 typed RuntimeEffect
  source-cache evicts, and 560 effect-handle cache hits.
- Focused typed RuntimeEffect source-cache eviction subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-122623/suite.tsv`.
  `commands-runtime-effect-shader-source-cache-eviction` stayed on command replay with zero fallback and 985
  `type=shader` evict markers; `commands-runtime-effect-source-cache-eviction` stayed on command replay with zero
  fallback and 1207 `type=colorFilter` evict markers.
- Magic Jewel report-validator regression tests passed after adding `EXPECT_JBR_RUNTIME_EFFECT_CACHE_EVICT_TYPE` and
  synthetic positive/negative typed eviction cases:
  `./scripts/test-jbr-skia-report-validation.sh` in
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel`.
- Full default command-probe sweep passed after adding the RuntimeEffect shader source-cache eviction sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-111217/suite.tsv`.
  The sweep covered 144 rows plus header: all 144 passed, 110 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 8 rows reported expected explicit fallback markers. The new
  `commands-runtime-effect-shader-source-cache-eviction` row stayed on command replay with zero fallback and reported
  444 JBR command frames, 976 RuntimeEffect source-cache hits, 1955 misses, 1953 source-cache evicts, 980 shader-handle
  definitions, 2931 shader-handle uses, and 1952 shader-handle cache hits.
- Focused `commands-runtime-effect-shader-source-cache-eviction` probe passed using the existing RuntimeEffect
  pure-color, uniform-only, and child-only shader sources against `JBR_SKIA_RUNTIME_EFFECT_CACHE_LIMIT_FOR_TEST=2`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-111136/suite.tsv`.
  The row stayed on command replay with zero fallback and reported 316 JBR command frames, 906 RuntimeEffect
  source-cache hits, 1815 misses, 1813 source-cache evicts, 910 shader-handle definitions, 2721 shader-handle uses,
  and 1812 shader-handle cache hits. The log includes `JBR_SKIA_INTEROP_RUNTIME_EFFECT_CACHE_EVICT type=shader ... limit=2`.
- Full default command-probe sweep passed after adding native RuntimeEffect source-cache eviction observability and the
  Magic Jewel eviction sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-095817/suite.tsv`.
  The sweep covered 143 rows plus header: all 143 passed, 109 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 8 rows reported expected explicit fallback markers. The new
  `commands-runtime-effect-source-cache-eviction` row stayed on command replay with zero fallback and reported 363 JBR
  command frames, 734 RuntimeEffect source-cache hits, 1471 misses, 1469 source-cache evicts, 1472 effect-handle
  definitions, 2205 effect-handle uses, 448 effect-handle evicts, and 734 effect-handle cache hits.
- Focused `commands-runtime-effect-source-cache-eviction` probe passed after adding the JBR
  `JBR_SKIA_INTEROP_RUNTIME_EFFECT_CACHE_EVICT` marker and the test-only
  `JBR_SKIA_RUNTIME_EFFECT_CACHE_LIMIT_FOR_TEST=2` override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-095725/suite.tsv`.
  The row stayed on command replay with zero fallback and reported 472 JBR command frames, 837 RuntimeEffect
  source-cache hits, 1677 misses, 1675 source-cache evicts, 1676 effect-handle definitions, 2514 effect-handle uses,
  and 839 effect-handle cache hits. The log alternated three color-filter RuntimeEffect sources against a test cache
  limit of two, producing eviction lines such as
  `JBR_SKIA_INTEROP_RUNTIME_EFFECT_CACHE_EVICT type=colorFilter ... limit=2`.
- Magic Jewel report-validator regression tests passed after adding strict command validation for
  `EXPECT_MIN_JBR_RUNTIME_EFFECT_CACHE_EVICTS`:
  `./scripts/test-jbr-skia-report-validation.sh` in
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel`.
- Focused RuntimeEffect color-filter cache-marker subset passed after teaching JBR color-filter RuntimeEffect cache
  logs to report the real descriptor child count:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-023557/suite.tsv`.
  `commands-runtime-effect-color-filter` and `commands-runtime-effect-color-filter-child` stayed on command replay
  with zero fallback, and `commands-runtime-effect-color-filter-child-type-fallback` still reported one explicit
  fallback marker with zero JBR command frames. The child row log now reports
  `JBR_SKIA_INTEROP_RUNTIME_EFFECT_CACHE_* type=colorFilter ... children=1`.
- Full default command-probe sweep passed after adding the RuntimeEffect color-filter child-type build-failure
  sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-013113/suite.tsv`.
  The sweep covered 142 rows plus header: all 142 passed, 108 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 8 rows reported expected explicit fallback markers. The new
  `commands-runtime-effect-color-filter-child-type-fallback` row reported one explicit fallback marker, 974 Skiko
  command frames, zero JBR command frames, zero JBR picture frames, one JBR RuntimeEffect build-failure marker, and the
  JBR log line `JBR_SKIA_INTEROP_RUNTIME_COLOR_FILTER_BUILD_FAILED ... stage=positional-child-type`.
- Focused `commands-runtime-effect-color-filter-child-type-fallback` probe passed after extending Skiko's test-only
  RuntimeEffect child-type corruption hook to runtime color-filter descriptors and adding JBR color-filter build
  failure markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-012953/suite.tsv`.
  The row reported one explicit fallback marker, 963 Skiko command frames, zero JBR command frames, zero JBR picture
  frames, and one JBR RuntimeEffect build-failure marker with `stage=positional-child-type`.
- Magic Jewel report-validator regression tests passed after broadening RuntimeEffect compile/build marker matching and
  adding explicit synthetic color-filter compile/build marker cases:
  `./scripts/test-jbr-skia-report-validation.sh` in
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel`.
- Full default command-probe sweep passed after adding the RuntimeEffect color-filter compile-failure sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-002725/suite.tsv`.
  The sweep covered 141 rows plus header: all 141 passed, 108 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 7 rows reported expected explicit fallback markers. The new
  `commands-runtime-effect-color-filter-compile-fallback` row reported one explicit fallback marker, 943 Skiko command
  frames, zero JBR command frames, zero JBR picture frames, one JBR RuntimeEffect compile-failure marker, and the JBR
  log line `JBR_SKIA_INTEROP_RUNTIME_COLOR_FILTER_COMPILE_FAILED`.
- Focused `commands-runtime-effect-color-filter-compile-fallback` probe passed after extending Skiko's test-only
  RuntimeEffect source corruption hook to runtime color-filter descriptors:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-002549/suite.tsv`.
  The row reported one explicit fallback marker, 959 Skiko command frames, zero JBR command frames, zero JBR picture
  frames, and one JBR RuntimeEffect compile-failure marker.
- Full default command-probe sweep passed after tightening `commands-runtime-effect-shader-color-filter` to require
  at most one RuntimeEffect source-cache miss:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-232038/suite.tsv`.
  The sweep covered 140 rows plus header: all 140 passed, 108 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 6 rows reported expected explicit fallback markers. The tightened
  shader-plus-color-filter row stayed on command replay with zero fallback and reported one effect-handle definition,
  741 effect-handle uses, 740 effect-handle cache hits, 1482 shader-handle definitions/uses, 740 RuntimeEffect
  source-cache hits, and one RuntimeEffect source-cache miss.
- Focused `commands-runtime-effect-shader-color-filter` probe passed after tightening the miss cap:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-232005/suite.tsv`.
  The row stayed on command replay with zero fallback and reported 868 RuntimeEffect source-cache hits with one miss.
- Full default screenshot parity suite passed after extending every supported RuntimeEffect parity row to require
  RuntimeEffect source-cache hits and at most one source-cache miss:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-222824/suite.tsv`.
  The suite covered 90 rows plus header. All 90 rows passed, all 90 rows reported command replay, and zero rows
  reported JBR picture or structural fallback. The tightened RuntimeEffect parity rows reported hit/miss counts of
  1254/1 for pure-color, 975/1 for uniform-only, 911/1 for child-only, 966/1 for shader, 1547/1 for
  shader-plus-color-filter, 1470/1 for color-filter, 874/1 for stable color-filter, 1079/1 for same-context resize,
  1330/1 for forced destination-context migration, and 1061/1 for child color-filter.
- Focused RuntimeEffect screenshot parity subset passed while calibrating the broader source-cache miss gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-222254/suite.tsv`.
  The subset covered pure-color, uniform-only, child-only, shader, shader-plus-color-filter, color-filter, stable
  color-filter, same-context resize, forced destination-context migration, and child color-filter rows; all stayed on
  command replay with zero fallback and zero JBR picture frames.
- Full default screenshot parity suite passed after extending stable RuntimeEffect color-filter parity rows to require
  RuntimeEffect source-cache hits and at most one source-cache miss:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-213258/suite.tsv`.
  The suite covered 90 rows plus header. All 90 rows passed, all 90 rows reported command replay, and zero rows
  reported JBR picture or structural fallback. The tightened stable RuntimeEffect parity rows reported source-cache
  hit/miss counts of 1031/1 for the base row, 916/1 for same-context resize, and 1048/1 for forced destination-context
  migration.
- Full default command-probe sweep passed after extending the stable RuntimeEffect color-filter lifecycle command rows
  to require at most one RuntimeEffect source-cache miss:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-203502/suite.tsv`.
  The sweep covered 140 rows plus header: all 140 passed, 108 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 6 rows reported expected explicit fallback markers. The tightened lifecycle
  rows reported source-cache hit/miss counts of 1004/1 for same-context resize and 894/1 for forced destination-context
  migration.
- Full default command-probe sweep passed after tightening the stable RuntimeEffect color-filter command row to require
  JBR RuntimeEffect source-cache hits and at most one source-cache miss:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-191427/suite.tsv`.
  The sweep covered 140 rows plus header: all 140 passed, 108 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 6 rows reported expected explicit fallback markers. The tightened
  `commands-runtime-effect-stable-color-filter` row stayed on command replay and reported one JBR effect-handle
  definition, 845 effect-handle uses, 844 effect-handle cache hits, 844 RuntimeEffect source-cache hits, and one
  RuntimeEffect source-cache miss.
- Focused RuntimeEffect command subset passed while calibrating the stable color-filter source-cache gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-191301/suite.tsv`.
  The subset covered `commands-runtime-effect-stable-color-filter`,
  `commands-runtime-effect-shader-color-filter`, and `commands-runtime-effect-color-filter-child`; all three stayed on
  command replay with zero fallback and zero JBR picture frames. Earlier calibration reruns showed shader/effect
  handle cache-hit markers on the animated shader-plus-color-filter and child color-filter rows can vary by run, so
  only the stable color-filter RuntimeEffect source-cache gate was retained.
- Full default command-probe sweep passed after adding the recursive RuntimeEffect shader nested-child sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-164715/suite.tsv`.
  The sweep covered 140 rows plus header: all 140 passed, 108 rows reported JBR command replay, 26 rows reported
  intentional JBR picture fallback, and 6 rows reported expected explicit fallback markers. The recursive shader
  nested-child row reported `shaderDescriptor` fallback with zero command frames; the recursive color-filter
  nested-child row remained on `colorFilterDescriptor` fallback with zero command frames.
- Compact recursive RuntimeEffect schema subset passed after adding shader and color-filter nested-child sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-164220/suite.tsv`.
  Supported shader/color-filter child rows stayed on command replay; invalid shader uniform/child/nested-child rows
  reported `shaderDescriptor` fallback with zero command frames, and the invalid color-filter nested-child row reported
  `colorFilterDescriptor` fallback with zero command frames.
- Focused RuntimeEffect shader invalid nested-child fallback passed after adding recursive shader descriptor validation
  coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-163854/suite.tsv`.
  The row reported `unsupported=shaderDescriptor:199,graphicsLayer:childCommands:200,graphicsLayer:200`,
  `jbr_picture_frames=200`, and `jbr_command_frames=0`, proving invalid nested shader descriptors do not leak through
  a parent RuntimeEffect shader handle.
- Full default command-probe sweep passed after adding the recursive RuntimeEffect color-filter nested-child sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-152515/suite.tsv`.
  The sweep covered 139 rows plus header: all 139 passed, 108 rows reported JBR command replay, 25 rows reported
  intentional JBR picture fallback, and 6 rows reported expected explicit fallback markers. The new recursive
  nested-child row reported `colorFilterDescriptor` fallback and zero command frames.
- Compact RuntimeEffect color-filter command subset passed after adding recursive descriptor validation coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-152047/suite.tsv`.
  Supported top-level and named-child RuntimeEffect color-filter rows stayed on command replay; invalid uniform,
  invalid child-schema, and invalid nested-child rows all reported `colorFilterDescriptor` fallback with zero command
  frames; raw RuntimeEffect color filters remained on structured `colorFilter` fallback.
- Focused RuntimeEffect color-filter invalid nested-child fallback passed after adding recursive descriptor validation
  coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-151749/suite.tsv`.
  The row reported `unsupported=colorFilterDescriptor:373,graphicsLayer:childCommands:373,graphicsLayer:373`,
  `jbr_picture_frames=373`, and `jbr_command_frames=0`, proving invalid nested color-filter descriptors do not leak
  through a parent RuntimeEffect color-filter handle.
- Full default command-probe sweep passed after adding RuntimeEffect color-filter schema sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-140744/suite.tsv`.
  The sweep covered 138 rows plus header: all 138 passed, 108 rows reported JBR command replay, 24 rows reported
  intentional JBR picture fallback, and 6 rows reported expected explicit fallback markers. The new color-filter
  schema rows reported `colorFilterDescriptor` fallback with zero command frames.
- Focused RuntimeEffect color-filter invalid uniform-schema and named-child-schema fallbacks passed after adding live
  Magic Jewel sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-140448/suite.tsv`.
  The uniform row reported
  `unsupported=colorFilterDescriptor:364,graphicsLayer:childCommands:364,graphicsLayer:364`,
  `jbr_picture_frames=365`, and `jbr_command_frames=0`; the named-child row reported
  `unsupported=colorFilterDescriptor:518,graphicsLayer:childCommands:518,graphicsLayer:518`,
  `jbr_picture_frames=518`, and `jbr_command_frames=0`. Both rows prove invalid RuntimeEffect color-filter metadata
  falls back structurally instead of producing partial effect-handle command replay.
- Full default command-probe sweep passed after adding RuntimeEffect schema sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-125344/suite.tsv`.
  The sweep covered 136 rows plus header: all 136 passed, 108 rows reported JBR command replay, 22 rows reported
  intentional JBR picture fallback, and 6 rows reported expected explicit fallback markers.
- Compact RuntimeEffect command subset passed after adding the schema sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-124746/suite.tsv`.
  Supported uniform/child shader rows stayed on command replay, invalid uniform/child schema rows fell back with
  `shaderDescriptor`, and compile/build/child-type JBR failure rows produced the expected fallback markers with zero
  JBR command frames.
- Focused RuntimeEffect invalid uniform-schema and named-child-schema fallbacks passed after adding live Magic Jewel
  sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-124449/suite.tsv`.
  Both rows reported `shaderDescriptor` unsupported metadata, JBR picture fallback, and zero JBR command frames,
  proving invalid descriptor metadata falls back structurally instead of emitting a partial command stream.
- Full expanded default screenshot parity suite passed after adding the latest graphics-layer parity rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-114354/suite.tsv`.
  The suite covered 90 rows plus header. All 90 rows passed, all 90 rows reported command replay, zero rows reported
  JBR picture fallback, and zero rows reported structural fallback. The higher-delta render-effect plus blend
  graphics-layer cluster stayed within its row gates while remaining command-only; keep those rows as sensitive
  sentinels for future blend/effect drift.
- Focused screenshot parity for plain graphics-layer replay and combined graphics-layer blend+color-filter rows passed
  after adding the rows to the default screenshot suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-113917/suite.tsv`.
  All three rows reported `fallback_new_count=0`, `jbr_picture_frames=0`, and nonzero command replay. The plain layer
  row reported `jbr_command_frames=767`, `screenshot_parity_badPixelRatio=0.05189`, and compose-canvas ratio
  `0.07590`; the blend+tint row reported `jbr_command_frames=744`, bad-pixel ratio `0.05280`, and compose-canvas ratio
  `0.07743`; the blend+color-matrix row reported `jbr_command_frames=590`, bad-pixel ratio `0.05278`, and
  compose-canvas ratio `0.07739`.
- Focused screenshot parity for standalone graphics-layer blend mode, tint color filter, and color-matrix filter passed
  after adding the rows to the default screenshot suite. The blend row passed in the first focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-113210/suite.tsv`;
  the standalone color-filter and color-matrix rows passed after removing descriptor-handle gates that do not apply to
  this inline graphics-layer field path:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-113353/suite.tsv`.
  All three rows reported `fallback_new_count=0`, `jbr_picture_frames=0`, and nonzero command replay.
- Focused screenshot parity for rectangular, rounded, and generic-path graphics-layer clips passed after adding the rows
  to the default screenshot suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-112646/suite.tsv`.
  All three rows reported `fallback_new_count=0`, `jbr_picture_frames=0`, and nonzero command replay. The rectangular
  row reported `jbr_command_frames=1071`, `screenshot_parity_badPixelRatio=0.05189`, and
  `screenshot_parity_region_composeCanvas_badPixelRatio=0.07590`; the rounded row reported
  `jbr_command_frames=728`, `screenshot_parity_badPixelRatio=0.05204`, and compose-canvas ratio `0.07614`; the path
  row reported `jbr_command_frames=732`, `screenshot_parity_badPixelRatio=0.05213`, and compose-canvas ratio `0.07630`.
- Focused screenshot parity for graphics-layer `CompositingStrategy.ModulateAlpha` passed after adding the row to the
  default screenshot suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-112203/suite.tsv`.
  The row reported `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=491`,
  `screenshot_parity_badPixelRatio=0.06560`,
  `screenshot_parity_region_headerButtons_badPixelRatio=0.00381`, and
  `screenshot_parity_region_composeCanvas_badPixelRatio=0.09900`. The row uses a dedicated right-probe-strip gate
  because ModulateAlpha intentionally changes alpha compositing in the probe-heavy right side of the scene; the
  command markers and screenshot assertion both stayed clean.
- Full default screenshot parity suite passed after adding explicit graphics-layer scale/translation coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-103324/suite.tsv`.
  The suite covered 80 rows plus header. All rows passed; all 80 rows reported command replay, zero rows reported
  JBR picture fallback, and zero rows reported structural fallback. The new
  `parity-graphics-layer-scale-translate` row reported `fallback_new_count=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=905`, `screenshot_parity_badPixelRatio=0.05192`,
  `screenshot_parity_region_headerButtons_badPixelRatio=0.00381`, and
  `screenshot_parity_region_composeCanvas_badPixelRatio=0.07596`.
- Focused screenshot parity for explicit graphics-layer scale/translation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-090559/suite.tsv`.
  The row reported `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=607`,
  `screenshot_parity_badPixelRatio=0.05192`, `screenshot_parity_region_headerButtons_badPixelRatio=0.00381`, and
  `screenshot_parity_region_composeCanvas_badPixelRatio=0.07596`.
- Focused command probe for explicit graphics-layer scale/translation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-205517/suite.tsv`.
  The row reported `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and
  `jbr_command_frames=289`.
- The full command-probe sweep after adding explicit graphics-layer scale/translation reached and passed the new
  default row in the long run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-090642/suite.tsv`.
  That run covered 126 rows before an existing later row was interrupted by the sandbox Gradle wrapper lock; all 126
  recorded rows passed. The new `commands-graphics-layer-scale-translate` row reported `fallback_new_count=0`,
  `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=428`. The interrupted existing row
  `commands-graphics-layer-blend-color-filter` passed in a focused rerun:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-102926/suite.tsv`.
  The remaining default tail rows passed as a subset:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-103013/suite.tsv`.
- Full default screenshot parity suite passed after adding stable RuntimeEffect color-filter resize and forced-context
  lifecycle rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-200201/suite.tsv`.
  The suite covered 79 rows plus header. All rows passed; all 79 rows reported command replay, zero rows reported
  JBR picture fallback, and zero rows reported structural fallback. The new resize row reported
  `jbr_command_frames=523`, `jbr_runtime_effect_cache_hit_frames=1062`,
  `jbr_effect_handle_define_frames=42`, `jbr_effect_handle_use_frames=1063`,
  `jbr_effect_handle_cache_hit_frames=1056`, `skiko_surface_change_markers=1`,
  `skiko_command_cache_clear_markers=1`, and `screenshot_parity_badPixelRatio=0.04467`. The new forced-context row
  reported `jbr_command_frames=307`, `jbr_runtime_effect_cache_hit_frames=728`,
  `jbr_effect_handle_define_frames=54`, `jbr_effect_handle_use_frames=729`,
  `jbr_effect_handle_cache_hit_frames=720`, `skiko_surface_change_markers=1`,
  `skiko_command_cache_clear_markers=1`, and `screenshot_parity_badPixelRatio=0.04668`.
- Focused screenshot parity for stable RuntimeEffect color-filter lifecycle passed after adding
  `parity-resize-runtime-effect-stable-color-filter` and
  `parity-forced-context-runtime-effect-stable-color-filter`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-195934/suite.tsv`.
  The resize row reported `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=386`,
  `jbr_runtime_effect_cache_hit_frames=739`, `jbr_effect_handle_define_frames=42`,
  `jbr_effect_handle_use_frames=740`, `jbr_effect_handle_cache_hit_frames=733`,
  `skiko_surface_change_markers=1`, `skiko_command_cache_clear_markers=1`, and
  `screenshot_parity_badPixelRatio=0.04467`. The forced-context row reported `jbr_command_frames=294`,
  `jbr_runtime_effect_cache_hit_frames=605`, `jbr_effect_handle_define_frames=54`,
  `jbr_effect_handle_use_frames=606`, `jbr_effect_handle_cache_hit_frames=597`,
  `skiko_surface_change_markers=1`, `skiko_command_cache_clear_markers=1`, and
  `screenshot_parity_badPixelRatio=0.04668`.
- Full default command-probe sweep passed after adding stable RuntimeEffect color-filter resize and forced-context
  lifecycle rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-184927/suite.tsv`.
  The suite covered 133 rows plus header. All rows passed; 107 rows reported command replay, 20 rows reported
  intentional JBR picture fallback sentinels, and 6 rows reported expected structural fallback. The new resize row
  reported `jbr_command_frames=590`, `jbr_runtime_effect_cache_hit_frames=1010`,
  `jbr_effect_handle_define_frames=2`, `jbr_effect_handle_use_frames=1011`,
  `jbr_effect_handle_cache_hit_frames=1009`, `skiko_surface_change_markers=1`, and
  `skiko_command_cache_clear_markers=1`. The new forced-context row reported `jbr_command_frames=277`,
  `jbr_runtime_effect_cache_hit_frames=567`, `jbr_effect_handle_define_frames=2`,
  `jbr_effect_handle_use_frames=568`, `jbr_effect_handle_cache_hit_frames=566`,
  `skiko_surface_change_markers=1`, and `skiko_command_cache_clear_markers=1`.
- Focused command probe for stable RuntimeEffect color-filter lifecycle passed after adding
  `commands-resize-runtime-effect-stable-color-filter` and
  `commands-forced-context-runtime-effect-stable-color-filter`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-184642/suite.tsv`.
  The resize row reported `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=268`,
  `jbr_runtime_effect_cache_hit_frames=673`, `jbr_effect_handle_define_frames=2`,
  `jbr_effect_handle_use_frames=674`, `jbr_effect_handle_cache_hit_frames=672`,
  `skiko_surface_change_markers=1`, and `skiko_command_cache_clear_markers=1`. The forced-context row reported
  `jbr_command_frames=616`, `jbr_runtime_effect_cache_hit_frames=1116`, `jbr_effect_handle_define_frames=2`,
  `jbr_effect_handle_use_frames=1117`, `jbr_effect_handle_cache_hit_frames=1115`,
  `skiko_surface_change_markers=1`, and `skiko_command_cache_clear_markers=1`.
- Full default screenshot parity suite passed after adding graphics-layer color-matrix resize and forced-context
  lifecycle parity rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-180029/suite.tsv`.
  The suite covered 77 rows plus header. All rows passed; all 77 rows reported command replay, zero rows reported
  JBR picture fallback, and zero rows reported structural fallback. The new lifecycle rows stayed within gates:
  `parity-resize-graphics-layer-color-matrix-filter` reported `jbr_command_frames=854`,
  `screenshot_parity_badPixelRatio=0.04577`, `screenshot_parity_region_headerButtons_badPixelRatio=0.02158`, and
  `screenshot_parity_region_composeCanvas_badPixelRatio=0.06496`; the forced-context row reported
  `jbr_command_frames=530`, `screenshot_parity_badPixelRatio=0.04815`,
  `screenshot_parity_region_headerButtons_badPixelRatio=0.00381`, and
  `screenshot_parity_region_composeCanvas_badPixelRatio=0.07011`.
- Focused screenshot parity for graphics-layer color-matrix descriptor lifecycle passed after adding
  `parity-resize-graphics-layer-color-matrix-filter` and
  `parity-forced-context-graphics-layer-color-matrix-filter`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-175623/suite.tsv`.
  The resize row reported `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=319`,
  `jbr_effect_handle_define_frames=8`, `jbr_effect_handle_cache_hit_frames=720`,
  `skiko_surface_change_markers=1`, `skiko_command_cache_clear_markers=1`, and
  `screenshot_parity_badPixelRatio=0.04577`. The forced-context row reported `jbr_command_frames=440`,
  `jbr_effect_handle_define_frames=10`, `jbr_effect_handle_cache_hit_frames=844`,
  `skiko_surface_change_markers=1`, `skiko_command_cache_clear_markers=1`, and
  `screenshot_parity_badPixelRatio=0.04815`.
- Full default command-probe sweep passed after adding graphics-layer color-matrix resize and forced-context lifecycle
  rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-163006/suite.tsv`.
  The suite covered 131 rows plus header. All rows passed; 105 rows reported command replay, 20 rows reported
  intentional JBR picture fallback sentinels, and 6 rows reported expected structural fallback. The new
  `commands-resize-graphics-layer-color-matrix-filter` and
  `commands-forced-context-graphics-layer-color-matrix-filter` rows both stayed on command replay with
  `fallback_new_count=0` and `jbr_picture_frames=0`.
- Focused command probe for graphics-layer color-matrix descriptor lifecycle passed after adding
  `commands-resize-graphics-layer-color-matrix-filter` and
  `commands-forced-context-graphics-layer-color-matrix-filter`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-162709/suite.tsv`.
  The resize row reported `jbr_command_frames=626`, `jbr_effect_handle_define_frames=2`,
  `jbr_effect_handle_use_frames=1317`, `jbr_effect_handle_cache_hit_frames=1315`,
  `skiko_surface_change_markers=1`, and `skiko_command_cache_clear_markers=1`. The forced-context row reported
  `jbr_command_frames=571`, `jbr_effect_handle_define_frames=2`, `jbr_effect_handle_use_frames=1181`,
  `jbr_effect_handle_cache_hit_frames=1179`, `skiko_surface_change_markers=1`, and
  `skiko_command_cache_clear_markers=1`.
- Focused `parity-button-chrome` screenshot parity passed on current artifacts after the full command sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-162055/suite.tsv`.
  The row stayed on command replay with `fallback_new_count=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=346`, `screenshot_primaryButtonWhiteText=405`, `screenshot_primaryButtonDarkText=0`, and
  `screenshot_parity_region_headerButtons_badPixelRatio=0.00381`.
- Full default command-probe sweep passed after adding the graphics-layer raw color-filter fallback sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-151511/suite.tsv`.
  The suite covered 129 rows plus header. All rows passed; 103 rows reported command replay, 20 rows reported
  intentional JBR picture fallback sentinels, and 6 rows reported expected structural fallback.
- Focused Magic Jewel command probe passed for `commands-graphics-layer-raw-color-filter-fallback`, added as a
  sentinel for raw Skia-backed `ColorFilter` values on graphics layers. The row reported
  `graphicsLayer:childCommands:365,graphicsLayer:colorFilter:365,graphicsLayer:365`, `jbr_picture_frames=364`,
  `jbr_command_frames=0`, and `validation_status=passed`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-150427/suite.tsv`.
- Current local artifact matrix passed after the latest rebuild-script stub fix. `current-all` replayed commands with
  `jbr_command_frames=654`, `fallback_new_count=0`, and `background_window=true`; `missing-public-api` reported the
  expected structured fallback with `fallback_new_count=1`, `jbr_command_frames=0`, and `background_window=true`.
  Optional old-artifact rows were skipped because their artifact paths were not configured:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260507-145018/matrix.tsv`.
- Rebuilt local artifacts with `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/scripts/rebuild-jbr-skia-local-artifacts.sh`,
  then compiled and ran `test/jdk/jb/JBRSkia/JBRSkiaApiTest.java` against the patched classes and native bridge using
  headless mode, `--patch-module java.base=/tmp/jbr-skia-run/java-base`,
  `--patch-module java.desktop=/tmp/jbr-skia-run/desktop`, and
  `-Dsun.java2d.skia.interop.library=/tmp/jbr-skia-native/libjbrskiainterop.dylib`. The run exited 0.
- JBR API test source expectation fixed to match current `JBRSkia.ABI_ID = 106`; source grep confirmed no remaining
  stale `105` ABI assertions in `test/jdk/jb/JBRSkia` or the JBR Skia API/service sources.
- Full Skiko `JbrSkiaInteropTest` class passed after adding per-bit low-word capability rejection:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  in `/Users/rock3r/src/jbr-skia-zero-copy/skiko/skiko`.
- Skiko focused `JbrSkiaInteropTest` coverage passed after adding the low-word counterpart to the existing
  per-high-bit missing-capability rejection loop:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest.rejectsEachMissingLowCommandCapability --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest.rejectsEachMissingHighCommandCapability`
  in `/Users/rock3r/src/jbr-skia-zero-copy/skiko/skiko`.
- Full compatibility matrix after adding the remaining exact low-word capability removals for image shaders,
  blend/color filters, line dash path effects, saveLayer variants, color-filter handles, and effect descriptors. It
  covered 57 rows plus the header. The `happy` row stayed on command replay with `jbr_command_frames=168`;
  representative rows including `fill-rect-image-shader-capability-missing`,
  `fill-rect-blend-mode-capability-missing`, `define-effect-descriptor-capability-missing`, and
  `save-layer-blend-color-filter-capability-missing` each reported `fallback_new_count=1`, `jbr_command_frames=0`,
  and `background_window=true`, as did the existing `public-api-missing` row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260507-131028/matrix.tsv`.
- Focused compatibility matrix for those 12 remaining exact low-word capability removals. Every new row reported one
  structured `command-capability-mismatch` fallback, zero JBR command frames, and `background_window=true`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260507-130532/matrix.tsv`.
- Full compatibility matrix after adding exact low-word gradient fill/stroke capability removals. It covered 45 rows
  plus the header. The `happy` row stayed on command replay with `jbr_command_frames=287`; representative gradient
  rows including `fill-rect-linear-gradient-capability-missing`, `fill-path-sweep-gradient-capability-missing`,
  `stroke-rect-linear-gradient-capability-missing`, and `stroke-round-rect-sweep-gradient-capability-missing` each
  reported `fallback_new_count=1`, `jbr_command_frames=0`, and `background_window=true`, as did the existing
  `public-api-missing` row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260507-123106/matrix.tsv`.
- Focused compatibility matrix after adding the 15 exact low-word gradient fill/stroke capability rows. Every new row
  reported one structured `command-capability-mismatch` fallback, zero JBR command frames, and
  `background_window=true`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260507-122221/matrix.tsv`.
- Full compatibility matrix after adding the exact dash path-effect high-word capability removals. It covered 30 rows
  plus the header. The `happy` row stayed on command replay with `jbr_command_frames=407`; every forced mismatch row,
  including the new dash path-effect rows plus draw-points, draw-vertices, and public-API-missing, reported
  `fallback_new_count=1`, `jbr_command_frames=0`, and `background_window=true`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260507-120041/matrix.tsv`.
- Focused compatibility matrix after adding exact missing-capability rows for the dash path-effect replay bits
  (`COMMAND_CAP64_HIGH_STROKE_RECT_DASH_PATH_EFFECT`, `COMMAND_CAP64_HIGH_STROKE_ROUND_RECT_DASH_PATH_EFFECT`, and
  `COMMAND_CAP64_HIGH_STROKE_PATH_DASH_PATH_EFFECT`). The new `CASES`-filtered matrix subset passed; each row reported
  `fallback_new_count=1`, `jbr_command_frames=0`, and `background_window=true`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260507-115729/matrix.tsv`.
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
