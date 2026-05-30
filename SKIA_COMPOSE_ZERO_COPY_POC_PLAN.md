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
- Direct JBR parser coverage now mirrors the live shader descriptor bounds for gradient tile/stop/radius/color-count
  guards, image shader dimensions/tile modes, and Perlin-noise base-frequency/octave/tile limits.
- Direct RuntimeEffect shader/color-filter parser coverage now includes hash, SKSL/source-code validation, and
  uniform/child/named-count bounds alongside the existing schema checks.
- Direct effect descriptor parser coverage now mirrors the live blur, offset, corner/stamped path-effect, and chained
  path-effect descriptor payload bounds.
- Direct descriptor-handle parser coverage now includes top-level draw-path path-effect refs, so the parser helper
  rejects undefined, evicted, and wrong-type path-effect handles before replay.
- Direct image-cache parser coverage now mirrors live replay for image refs and image-shader refs by rejecting
  undefined, evicted, cleared, or dimension-mismatched cache keys before replay.
- Java2D replay and parser-helper descriptor type checks now agree for saveLayer color-filter refs, including
  blend/color-filter refs.
- Java2D replay now explicitly rejects non-color-filter descriptors for image-ref and fill-rect color-filter refs
  instead of relying on downstream filter application to fail.
- RuntimeEffect color-filter child validation now requires color-filter descriptor children explicitly, including a
  direct path-effect wrong-family sentinel.
- Direct saveLayer parser coverage includes supported non-ref color-filter, blend-mode, and blend/color-filter scalar
  bounds.
- Stable descriptor rows assert JBR handle definitions, uses, cache hits, and context invalidation behavior.
- The default path-effect command row now also gates the stable descriptor-definition contract: five JBR-owned
  path-effect descriptor definitions and no picture fallback.
- Raw Skiko-owned shader/effect/path-effect families remain explicit fallback sentinels.
- Harness windows are non-focus-stealing by default via `MAGIC_JEWEL_BACKGROUND_WINDOW=true`.
- Validation iteration is now area-scoped by default for small sentinel slices: run the exact `CASES=...` row first,
  then the smallest relevant adjacent `CASES=...` slice or `CASE_GROUPS=...` subset, and reserve full default command
  sweeps for periodic consolidation. Long broad sweeps can be resumed with `CASES_FROM=...` after a failing/flaky row
  is understood, so already-green prefixes do not need to be repeated. The current fast loop is exact row, quick group
  or bounded adjacent range, Skiko focused publication, `JbrSkiaInteropTest`, and then a periodic full sweep once a
  coherent batch of sentinels has landed. The latest periodic default command-probe consolidation passed in
  command-marker-only mode after the latest focused invalid/parser guard refreshes. The run was split by a Codex
  restart and resumed from `commands-invalid-linear-gradient-stroke-color-count-fallback`. Combined aggregate:
  487/487 passed, `fallback_sum=350`, `unsupported_rows=26`, `picture_frames=34406`, and `command_frames=226952`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-064308/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-094245/suite.tsv`.
- The latest exact descriptor-handle eviction command row passed after adding matching CMP recorder eviction guards.
  Aggregate: 1/1 passed, `fallback_sum=0`, `unsupported_rows=0`, zero picture frames, and 59 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-013713/suite.tsv`.
- The paired focused color-shader descriptor command rows passed after the CMP shader descriptor cache-reuse guard.
  Aggregate: 3/3 passed, `fallback_sum=0`, `unsupported_rows=0`, zero picture frames, and 5,423 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-014358/suite.tsv`.
- The paired focused graphics-layer render-effect descriptor command rows passed after the CMP image-filter descriptor
  cache-reuse guard. Aggregate: 3/3 passed, `fallback_sum=0`, `unsupported_rows=0`, zero picture frames, and 4,465
  command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-015048/suite.tsv`.
- The focused `native-text-invalid` command-probe group passed as the next small guardrail batch. Aggregate: 11/11
  passed, `fallback_sum=11`, `unsupported_rows=0`, zero picture frames, and 1,519 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-055310/suite.tsv`.
- The focused `primitive-invalid` command-probe group passed after `native-text-invalid`. Aggregate: 13/13 passed,
  `fallback_sum=13`, `unsupported_rows=0`, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-200527/suite.tsv`.
- The focused `path-invalid` command-probe group passed after `primitive-invalid`. Aggregate: 22/22 passed,
  `fallback_sum=22`, `unsupported_rows=0`, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-030433/suite.tsv`.
- The focused `effect-descriptor-invalid` command-probe group passed after `path-invalid`. Aggregate: 28/28 passed,
  `fallback_sum=28`, `unsupported_rows=0`, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-031858/suite.tsv`.
- The focused `shader-descriptor-invalid` command-probe group passed after `effect-descriptor-invalid`. Aggregate:
  30/30 passed, `fallback_sum=30`, `unsupported_rows=0`, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-033644/suite.tsv`.
- The focused `image-handles-invalid` command-probe group passed after `shader-descriptor-invalid`. Aggregate: 27/27
  passed, `fallback_sum=27`, `unsupported_rows=0`, zero picture frames, and 1,036 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-035452/suite.tsv`.
- The focused `color-filters` command-probe group passed after the image-handle invalid guardrails. Aggregate: 10/10
  passed, `fallback_sum=0`, `unsupported_rows=1`, 1,047 picture frames, and 13,204 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203750/suite.tsv`.
- The focused `save-layer-invalid` command-probe group passed after `color-filters`. Aggregate: 37/37 passed,
  `fallback_sum=37`, `unsupported_rows=0`, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-052922/suite.tsv`.
- The focused `descriptor-handles-invalid` command-probe group passed after `save-layer-invalid`. Aggregate: 48/48
  passed, `fallback_sum=48`, `unsupported_rows=0`, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-041151/suite.tsv`.
- The previous full default command-probe checkpoint after the focused color-filter, RuntimeEffect,
  descriptor-lifecycle, and smoke refreshes passed 487/487 with `fallback_sum=350`, `unsupported_rows=26`,
  `picture_frames=24888`, and `command_frames=140464`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-131448/suite.tsv`.
- The latest compatibility matrix checkpoint passed in command-marker-only mode after the resumed full command-probe
  consolidation, with 57/57 rows passed, `fallback_sum=56`, 1,050 JBR command frames from the happy path, and
  background-window mode on every row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260530-113437/matrix.tsv`.
- The latest artifact matrix checkpoint passed the required current-artifact rows on ABI 106 local artifacts:
  `current-all` replayed commands with 1,081 JBR command frames, `missing-public-api` fell back exactly once, and the
  optional old-artifact rows were skipped because no bundle variables were set:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260530-120135/matrix.tsv`.
- A follow-up focused artifact matrix also passed after fixing Magic Jewel's JBR API helper to remove its temporary
  `com.jetbrains.exported.JBRApi` desktop-overlay stub on exit: `current-all` replayed commands with 460 JBR command
  frames, and `missing-public-api` fell back exactly once:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260528-173847/matrix.tsv`.
- The latest full default screenshot parity checkpoint passed after the command-probe, compatibility, and artifact
  refreshes. Aggregate: 106/106 passed, `fallback_sum=11`, zero JBR picture frames, 111,257 JBR command frames,
  average `bad_pixel_ratio=0.05158`, and 107 TSV lines including the header:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260529-091416/suite.tsv`.
- Current no-run discovery helpers cover the command-probe, screenshot parity, compatibility matrix, artifact matrix,
  and benchmark suite loops. Use `LIST_CASES=true` / `LIST_CASE_COUNT=true` where available for exact row discovery;
  command and screenshot suites also expose `CASE_GROUPS=...` plus group listing/count helpers.
- The latest full default benchmark checkpoint passed after the compatibility/artifact refreshes in
  command-marker-only mode. Aggregate: 5/5 passed, `fallback_sum=0`, 84 old-side CPU samples,
  49 new-side CPU samples, picture FPS `229.4`, command FPS row total `684.8`, and
  `jbr_command_frames=13695`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260530-120858/suite.tsv`.
- The latest Skiko focused source-side gate passed after the refreshed 20260530 command, compatibility, artifact,
  and benchmark validations:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- The latest JBR parser/API-side gate passed after the refreshed 20260530 Skiko gate with Magic Jewel's helper using
  `REBUILD_LOCAL_ARTIFACTS=false` and running `JBRSkiaApiTest` against `/tmp/jbr-skia-run/desktop` plus
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Magic Jewel now exposes that gate as `scripts/test-jbr-skia-api.sh`; the helper passed end-to-end and prints
  `JBR_SKIA_API_TEST passed`. The helper removes its temporary desktop-overlay `JBRApi` stub on exit so later artifact
  matrix runs do not inherit split-package state.
- The latest CMP recorder gate passed after the refreshed Skiko and JBR parser/API gates:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
  reported 138/138 desktop tests with zero skipped/failures/errors.
- RuntimeEffect source-cache eviction rows now assert descriptor-handle cache reuse in both command and visual suites.
  The RuntimeEffect color-filter child row keeps descriptor define/use and source-cache-hit gates, but omits the
  descriptor cache-hit gate after grouped replay showed that child effect-handle cache hits can legitimately be zero.
  Exact source-cache command rows passed 2/2 with zero fallback, zero unsupported rows, and 3,930 JBR command frames;
  descriptor lifecycle command validation passed 18/18 with zero fallback and 29,228 JBR command frames; exact
  affected visual rows passed 2/2 with zero fallback, zero picture frames, 2,253 JBR command frames, and average
  `bad_pixel_ratio=0.04981`; the RuntimeEffect visual group passed 14/14 with `fallback_sum=2`, zero picture frames,
  12,263 JBR command frames, and average `bad_pixel_ratio=0.04879`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260526-224211/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260526-224342/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-222738/suite.tsv`,
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-225720/suite.tsv`.
- The latest focused graphics-layer command/visual checkpoints passed: command `CASE_GROUPS=graphics-layer` covered 21
  rows with zero fallback, zero picture frames, and 27,255 JBR command frames, while screenshot
  `CASE_GROUPS=graphics-layer-clip-shadow-transform` covered 14 clip/shadow/3D rows with zero fallback, zero picture
  frames, 14,847 JBR command frames, and average `bad_pixel_ratio=0.05296`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-211340/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-103209/suite.tsv`.
- The latest focused core drawing/effect checkpoints passed: command `CASE_GROUPS=core-effects` covered 7 rows with
  `fallback_sum=0`, 2 unsupported fallback-sentinel rows, 2,724 picture frames, and 7,649 JBR command frames; screenshot
  `CASE_GROUPS=core-drawing` covered 16 rows with zero fallback, zero picture frames, 13,217 JBR command frames, and
  average `bad_pixel_ratio=0.05197`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-215950/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-104710/suite.tsv`.
- The latest focused descriptor lifecycle command/visual checkpoints passed: command
  `CASE_GROUPS=descriptor-lifecycle` covered 18 rows with zero fallback, zero picture frames, and 23,175 JBR command
  frames, while screenshot `CASE_GROUPS=descriptor-lifecycle` covered 6 rows with `fallback_sum=1`, zero picture
  frames, 7,237 JBR command frames, and average `bad_pixel_ratio=0.05078`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-204612/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-130309/suite.tsv`.
- The previous full default command-probe checkpoint passed with screenshot assertions enabled after the focused
  screenshot parity refreshes and macOS capture retry fix. Aggregate: 487/487 passed, `fallback_sum=350`,
  `unsupported_rows=26`, `picture_frames=25539`, and `command_frames=146052`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-163835/suite.tsv`.
- The previous full default command-probe checkpoint passed in command-marker-only mode after the focused group and
  compatibility refreshes while local macOS screenshot capture still failed independently of command replay. Aggregate:
  487/487 passed, `fallback_sum=350`, `unsupported_rows=26`, `picture_frames=33702`, and `command_frames=188677`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-083629/suite.tsv`.
- The latest supported command-replay refresh passed in command-marker-only mode while the local macOS screenshot
  capture path remains unavailable: `smoke` 6/6 with 8,392 command frames, `color-filters` 10/10 with one intentional
  unsupported raw blend color-filter row, 1,047 picture frames, and 13,204 command frames, `descriptor-lifecycle` 18/18
  with 23,175 command frames, `native-text` 14/14 with 16,132 command frames, and `graphics-layer` 21/21 with 27,255
  command frames. All supported rows stayed on command replay with `fallback_sum=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203230/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203750/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-204612/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-210151/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-211340/suite.tsv`.
- The current small invalid-group refresh passed for `stream-invalid`, `shader-ref-invalid`,
  `fill-rect-color-filter-invalid`, and `blend-mode-invalid`, covering 18 rows total with expected fallback sums, zero
  unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-201711/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202316/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202527/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202909/suite.tsv`.
- The current focused command-stream parser guard checkpoint is `CASE_GROUPS=stream-invalid`, which passed 8/8 with
  `fallback_sum=8`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-201711/suite.tsv`.
- The current focused fill-rect blend-mode parser guard checkpoint is `CASE_GROUPS=blend-mode-invalid`, which passed
  2/2 with `fallback_sum=2`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202909/suite.tsv`.
- The current focused fill-rect color-filter parser guard checkpoint is `CASE_GROUPS=fill-rect-color-filter-invalid`,
  which passed 5/5 with `fallback_sum=5`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202527/suite.tsv`.
- The current focused fill-rect shader-ref parser guard checkpoint is `CASE_GROUPS=shader-ref-invalid`, which passed
  3/3 with `fallback_sum=3`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202316/suite.tsv`.
- The latest periodic default command-probe consolidation after the shader/effect/RuntimeEffect/saveLayer parser guard
  refreshes used an exact `commands-live-animation` rerun plus a resumed default tail after macOS window capture failed
  independently of command replay on the first broad attempt. Combined aggregate: 486/486 passed,
  `fallback_sum=349`, `unsupported_rows=26`, `picture_frames=33767`, and `command_frames=222490`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-234434/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-234537/suite.tsv`.
- The latest periodic default command-probe consolidation after the descriptor lifecycle, native text, graphics-layer,
  and gradient quick refreshes used split resume roots and an exact repaired rerun after a transient local
  `public-api-missing` artifact state. Combined aggregate: 486/486 passed, `fallback_sum=349`,
  `unsupported_rows=26`, `picture_frames=34318`, and `command_frames=200336`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-063205/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-153433/suite.tsv`,
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-153519/suite.tsv`.
- Magic Jewel command-probe quick-group coverage now spans every resolved default case. `LIST_UNGROUPED_CASES=true`
  returns no rows after adding supported surface/transform/UI, shader-rendering, shader-composition/RuntimeEffect,
  core-effects, graphics-layer-extras, and saveLayer/shader-fallback groups.
- The current exact saveLayer/shader-fallback uncovered command-probe tail passed 7/7, with `fallback_sum=0`, five
  intentional unsupported fallback rows, 4,757 JBR picture frames, and 1,765 JBR command frames across saveLayer filter
  and blend-mode replay, saveLayer raw color-filter fallback, opaque/composite opaque/picture shader fallbacks, and
  invalid-gradient fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-114732/suite.tsv`.
- The current focused graphics-layer extras command-probe checkpoint is `CASE_GROUPS=graphics-layer-extras`, which
  passed 14/14 with `fallback_sum=0`, two intentional unsupported fallback rows, 2,458 JBR picture frames, and 18,868
  JBR command frames across graphics-layer color-matrix/render-effect resize and forced-context lifecycle rows, raw
  color-filter/render-effect fallback sentinels, render-effect color/blend/color-matrix combinations, offset/chained
  render-effect combinations, and the near-camera chained render-effect variant:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-220535/suite.tsv`.
- The current exact core effects uncovered command-probe checkpoint passed 7/7, with `fallback_sum=0`, two intentional
  unsupported fallback rows, 2,724 JBR picture frames, and 7,649 JBR command frames across stroked gradients, image
  filters, descriptor path effects, path-effect color-filter fallback, raw discrete path-effect fallback, vertices,
  and blend-mode rendering:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-215950/suite.tsv`.
- The current exact shader composition and RuntimeEffect uncovered command-probe checkpoint passed 15/15, with
  `fallback_sum=0`, two intentional unsupported fallback rows, 2,386 JBR picture frames, and 16,985 JBR command frames
  across image/composite/transformed shaders, shader color-filter combinations, RuntimeEffect shader and color-filter
  replay, pure/uniform/child RuntimeEffects, and raw RuntimeEffect shader/color-filter fallback sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-214834/suite.tsv`.
- The current focused shader-rendering command-probe checkpoint is `CASE_GROUPS=shader-rendering`, which passed 13/13
  with `fallback_sum=0`, eight intentional unsupported fallback rows, 9,129 JBR picture frames, and 5,472 JBR command
  frames across forced-context dynamic images, image path-effect fallback, image shader replay, descriptor
  stroke-shader fallback, gradient/noise/turbulence shader descriptors, and raw image/gradient/noise/turbulence
  fallback sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-213937/suite.tsv`.
- The current focused surface/transform/UI command-probe checkpoint passed 11/11 in command-marker-only mode after a
  macOS screenshot-capture failure independent of command replay, with `fallback_sum=0`, no unsupported rows, zero JBR
  picture frames, and 13,359 JBR command frames across native bridge loading, drawPoints lines/dots, concat/skew
  transforms, gradient surfaces/paths, popup/menu layering, popup-window capture, and text-as-image replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-213157/suite.tsv`.
- The current compatibility matrix checkpoint passed 57/57 after the screenshot-enabled command-probe refresh, with
  `fallback_sum=56`, 421 JBR command frames from the happy path, and background-window mode on every row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260525-220254/matrix.tsv`.
- The current artifact matrix checkpoint passed the required current-artifact rows on ABI 106 local artifacts:
  `current-all` replayed commands with 606 JBR command frames, `missing-public-api` fell back exactly once, and the
  optional old-artifact rows were skipped because no old bundle variables were set:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260525-223152/matrix.tsv`.
- The current focused button-chrome screenshot parity checkpoint passed after the macOS capture helper gained the
  window-bounds retry. The row stayed on command replay with `fallback_sum=0`, zero JBR picture frames, 941 JBR command
  frames, overall `bad_pixel_ratio=0.05200`, and header-button `bad_pixel_ratio=0.00381`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-140430/suite.tsv`.
- The current focused screenshot parity checkpoint for the stable descriptor lifecycle visual surface passed 14/14
  after the local capture retry refresh, with `fallback_sum=5`, zero JBR picture frames, 10,401 JBR command frames,
  and average `bad_pixel_ratio=0.04695` across descriptor eviction, shader resize/forced-context redefine rows,
  stable RuntimeEffect color-filter lifecycle rows, RuntimeEffect color-filter child replay, and RuntimeEffect
  source-cache eviction:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-140922/suite.tsv`.
- The current focused native text visual checkpoint passed 14/14 after the local capture retry refresh, with
  `fallback_sum=3`, zero JBR picture frames, 10,056 JBR command frames, and average `bad_pixel_ratio=0.04758` across
  custom-font image text, generic/loaded/resource/system font text, same-context resize, and forced destination-context
  migration:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-142013/suite.tsv`.
- The current focused graphics-layer base/clip/blend visual checkpoint passed 7/7, with `fallback_sum=0`, zero JBR
  picture frames, 6,852 JBR command frames, and average `bad_pixel_ratio=0.05404` across plain graphics-layer replay,
  modulate-alpha, offscreen compositing, rectangular/rounded/path clips, and graphics-layer blend mode:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-143023/suite.tsv`.
- The current focused graphics-layer filter/effect visual checkpoint passed 5/5, with `fallback_sum=0`, zero JBR
  picture frames, 6,450 JBR command frames, and average `bad_pixel_ratio=0.05177` across graphics-layer color filter,
  color-matrix filter, combined render/offset/chained effects, offset effect, and chained render effect:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-143551/suite.tsv`.
- The current focused graphics-layer shadow/transform visual checkpoint passed 9/9, with `fallback_sum=0`, zero JBR
  picture frames, 10,016 JBR command frames, and average `bad_pixel_ratio=0.05198` across rectangular/rounded/path
  shadows, rotation X/Y/XY, scale/translate, near-camera, and off-center pivot rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-144011/suite.tsv`.
- The current focused combined graphics-layer blend/filter/render-effect visual checkpoint passed 10/10, with
  `fallback_sum=0`, zero JBR picture frames, 11,712 JBR command frames, and average `bad_pixel_ratio=0.06161` across
  blend+color-filter, blend+color-matrix, render-effect plus color/blend/filter combinations, offset-effect
  blend+color-matrix, chained render-effect blend+color-matrix, and the near-camera chained variant:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-144729/suite.tsv`.
- The current focused descriptor-backed color-filter visual checkpoint passed 6/6, with `fallback_sum=1`, zero JBR
  picture frames, 6,651 JBR command frames, and average `bad_pixel_ratio=0.05071` across image color-matrix filtering,
  color-filter handle base/resize/forced-context lifecycle, color-matrix filter, and lighting filter rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-145502/suite.tsv`.
- The current focused gradient visual checkpoint passed 4/4, with `fallback_sum=0`, zero JBR picture frames, 4,613
  JBR command frames, and average `bad_pixel_ratio=0.05099` across gradient surfaces, gradient-filled paths,
  ShaderBrush gradients, and stroked gradients:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-145959/suite.tsv`.
- The current focused core drawing visual checkpoint passed 10/10, with `fallback_sum=0`, zero JBR picture frames,
  8,713 JBR command frames, and average `bad_pixel_ratio=0.05479` across clean geometry, skew, drawVertices, point
  dots, path effects, drawPath/drawArc/drawRoundRect shapes, clip rect/path, blend modes, and saveLayer tint-filter
  replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-150344/suite.tsv`.
- The current focused image/shader descriptor visual checkpoint passed 12/12, with `fallback_sum=0`, zero JBR picture
  frames, 10,079 JBR command frames, and average `bad_pixel_ratio=0.05304` across forced-context image refs, image
  filters, image/color/noise/turbulence shaders, image-shader color filters, composite and composite-noise shaders,
  composite/linear-gradient shader color filters, and transformed shaders:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-151131/suite.tsv`.
- The current focused remaining RuntimeEffect visual checkpoint passed 9/9, with `fallback_sum=1`, zero JBR picture
  frames, 7,416 JBR command frames, and average `bad_pixel_ratio=0.04895` across pure-color RuntimeEffect
  base/resize/forced-context lifecycle, uniform-only and child-only RuntimeEffects, shader source-cache eviction,
  RuntimeEffect shader, shader+color-filter, and color-filter rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-152025/suite.tsv`.
- The current full default screenshot parity checkpoint passed after the focused visual refreshes and local macOS
  capture retry fix. Aggregate: 106/106 passed, `fallback_sum=11`, zero JBR picture frames, 90,354 JBR command frames,
  average `bad_pixel_ratio=0.05158`, and 107 TSV lines including the header across button chrome, core drawing, native
  text, gradients, image/color filters, descriptor lifecycle, shader descriptors, RuntimeEffect rows, and
  graphics-layer variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-152754/suite.tsv`.
- The current focused stable descriptor lifecycle checkpoint is `CASE_GROUPS=descriptor-lifecycle`, which passed 18/18
  with no fallback, no unsupported rows, zero JBR picture frames, and 23,175 JBR command frames across descriptor
  eviction, same-context resize/forced-context redefine, stable RuntimeEffect color-filter, and RuntimeEffect
  source-cache eviction rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-204612/suite.tsv`.
- The current focused native text lifecycle checkpoint is `CASE_GROUPS=native-text`, which passed 14/14 in
  command-marker-only mode after a screenshot assertion failure independent of command replay, with no fallback, no
  unsupported rows, zero JBR picture frames, and 16,132 JBR command frames across custom-font image text,
  generic/loaded/resource/system fonts, same-context resize, and forced destination-context migration. The paired
  screenshot `CASE_GROUPS=native-text` refresh passed 14/14 with `fallback_sum=2`, zero picture frames, 9,822 command
  frames, and average `bad_pixel_ratio=0.04758`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-210151/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-110758/suite.tsv`.
- The current native text/font parser fallback checkpoint is `CASE_GROUPS=native-text-invalid`, which passed 11/11
  with `fallback_sum=11`, zero unsupported rows, zero JBR picture frames, and 1,519 JBR command frames across invalid
  font size/weight/width/slant/family-count rows and the invalid font-data record flags sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-055310/suite.tsv`.
- The current primitive paint/draw parser fallback checkpoint is `CASE_GROUPS=primitive-invalid`, which passed 13/13
  with `fallback_sum=13`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames across invalid
  stroke cap, transform flags, clip operation, drawPoints, and drawVertices payload guards:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-200527/suite.tsv`.
- The current path and path-effect parser fallback checkpoint is `CASE_GROUPS=path-invalid`, which passed 22/22 with
  `fallback_sum=22`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames across malformed
  clip/draw path verbs, dash path-effect line/rect/round-rect/generic-path payloads, and drawShadow path verbs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-030433/suite.tsv`.
- The current supported color-filter/graphics-layer replay checkpoint is `CASE_GROUPS=color-filters`, which passed
  10/10 with no fallback, one intentional unsupported raw color-filter sentinel, 1,047 JBR picture frames for that row,
  and 13,204 JBR command frames across the supported color-filter, lighting, and graphics-layer color-filter rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203750/suite.tsv`.
- The current save-layer shader fallback checkpoint is `CASE_GROUPS=save-layer-shader-fallbacks`, which passed 7/7 in
  command-marker-only mode after a screenshot assertion failure independent of command replay, with `fallback_sum=0`,
  five intentional unsupported picture-replay rows, 6,640 JBR picture frames, and 2,760 JBR command frames across
  supported saveLayer filter/blend-mode replay and raw color-filter/shader/invalid-gradient fallback sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-221530/suite.tsv`.
- The current image handle/ref parser fallback checkpoint is `CASE_GROUPS=image-handles-invalid`, which passed 27/27
  with `fallback_sum=27`, zero unsupported rows, zero JBR picture frames, and 1,042 JBR command frames across malformed
  image define/cache-clear/evict records, image use/use-after-evict, image-ref scalar corruption, color-filter
  image-ref/use/ref rows, and descriptor-ref scalar guards:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-185310/suite.tsv`.
- The current descriptor handle/family parser checkpoint is `CASE_GROUPS=descriptor-handles-invalid`, which passed
  48/48 with `fallback_sum=48`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames across
  undefined/evicted top-level handles, saveLayer descriptor handles, shader/effect child use-after-evict, missing
  children, and wrong-family descriptor children:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-041151/suite.tsv`.
- The current focused graphics-layer transform/effect checkpoint is `CASE_GROUPS=graphics-layer`, which passed 21/21
  with no fallback, no unsupported rows, zero JBR picture frames, and 27,255 JBR command frames across layer
  alpha/offscreen/clip variants, blend/color-filter/render-effect rows, shadows, rotations, scale/translate,
  near-camera, and off-center pivot replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-211340/suite.tsv`.
- The current focused gradient path parser/fallback checkpoint is `CASE_GROUPS=gradient-path-invalid`, which passed
  18/18 with `fallback_sum=18`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames across
  malformed linear/radial/sweep gradient path tile/count/stop-order/path-data variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-044104/suite.tsv`.
- The current broader gradient parser/fallback checkpoint is `CASE_GROUPS=gradient-invalid`, which passed 60/60 with
  `fallback_sum=60`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames across malformed
  stroke-width, tile-mode, radius, color-count, stop-order, and embedded gradient-path variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-045310/suite.tsv`.
- The current focused saveLayer parser guard refresh is `CASE_GROUPS=save-layer-invalid`, which passed 37/37 with
  `fallback_sum=37`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames across saveLayer
  alpha, record-flag, record-length, bounds, blend-mode, and descriptor-backed color/image-filter variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-052922/suite.tsv`.
- The current focused shader descriptor parser guard refresh is `CASE_GROUPS=shader-descriptor-invalid`, which passed
  30/30 with `fallback_sum=30`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames after the
  direct parser-test additions for gradient/image shader and Perlin-noise descriptor bounds:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-193003/suite.tsv`.
- The current focused effect descriptor parser guard refresh is `CASE_GROUPS=effect-descriptor-invalid`, which passed
  28/28 with `fallback_sum=28`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames after the
  direct parser-test additions for blur, offset, corner/stamped path-effect, and chain path-effect descriptor bounds:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-031858/suite.tsv`.
- The current focused RuntimeEffect parser/semantic guard refresh is `CASE_GROUPS=runtime-effect-invalid`, which passed
  62/62 with `fallback_sum=56`, six intentional unsupported-picture rows, 7,806 JBR picture frames, and zero JBR
  command frames across source, SKSL, uniform, child, named-count, compile/build, and child-type bounds:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-060107/suite.tsv`.
- The current quick happy-path command replay checkpoint is `CASE_GROUPS=smoke`, which passed 6/6 with no fallback,
  no unsupported rows, no picture fallback, and 8,392 JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203230/suite.tsv`.
- The current quick command-stream parser guard checkpoint is `CASE_GROUPS=stream-invalid`, which passed 8/8 with
  `fallback_sum=8`, zero unsupported rows, and zero JBR frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-201711/suite.tsv`.
- The current focused native text/font-data parser guard checkpoint is `CASE_GROUPS=native-text-invalid`, which passed
  11/11 with `fallback_sum=11`, zero unsupported rows, zero JBR picture frames, and 1,519 JBR command frames from the
  recoverable font-data record-flags row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-055310/suite.tsv`.
- The current focused primitive command parser guard checkpoint is `CASE_GROUPS=primitive-invalid`, which passed 13/13
  with `fallback_sum=13`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-200527/suite.tsv`.
  The adjacent default-order primitive/image/path range from `commands-core-primitives` through
  `commands-point-lines` also passed 38/38 with `fallback_sum=36`, zero unsupported rows, zero JBR picture frames, and
  3,398 JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-092240/suite.tsv`.
- The current focused path/path-effect parser guard checkpoint is `CASE_GROUPS=path-invalid`, which passed 22/22 with
  `fallback_sum=22`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-030433/suite.tsv`.
- The current focused color-filter command replay checkpoint is `CASE_GROUPS=color-filters`, which passed 10/10 with
  `fallback_sum=0`, one intentional unsupported raw blend color-filter row, 1,047 JBR picture frames, and 13,204 JBR
  command frames across the supported descriptor and graphics-layer color-filter rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203750/suite.tsv`.
- The current focused native text/font-data command replay checkpoint is `CASE_GROUPS=native-text`, which passed 14/14
  with `fallback_sum=0`, zero unsupported rows, zero JBR picture frames, and 16,132 JBR command frames across custom,
  generic, loaded-font-data, resource, system, resize, and forced-context native text rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-210151/suite.tsv`.
- The current focused gradient path parser guard checkpoint is `CASE_GROUPS=gradient-path-invalid`, which passed 18/18
  with `fallback_sum=18`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-044104/suite.tsv`.
- The current focused graphics-layer command replay checkpoint is `CASE_GROUPS=graphics-layer`, which passed 21/21
  with `fallback_sum=0`, zero unsupported rows, zero JBR picture frames, and 27,255 JBR command frames across layer
  clips, blend/color filters, render effects, shadows, 3D rotations, scale/translate, camera, and pivot variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-211340/suite.tsv`.
- The current focused image handle/parser guard checkpoint is `CASE_GROUPS=image-handles-invalid`, which passed 27/27
  with `fallback_sum=27`, zero unsupported rows, zero JBR picture frames, and 1,109 JBR command frames from the
  recoverable image-cache-clear record-flags row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-192940/suite.tsv`.
- The current focused effect descriptor parser guard checkpoint is `CASE_GROUPS=effect-descriptor-invalid`, which
  passed 28/28 with `fallback_sum=28`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames
  across descriptor header guards, color/image filter payloads, and corner/stamped/chain path-effect descriptor parser
  guards:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-031858/suite.tsv`.
- The current focused shader descriptor parser guard checkpoint is `CASE_GROUPS=shader-descriptor-invalid`, which
  passed 30/30 with `fallback_sum=30`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames
  across shader descriptor headers, color/filter payloads, transformed/composite shader guards, gradient/image shader
  descriptor bounds, and Perlin noise descriptor guards:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-033644/suite.tsv`.
- The current focused saveLayer parser guard checkpoint is `CASE_GROUPS=save-layer-invalid`, which passed 37/37 with
  `fallback_sum=37`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames across alpha, record
  flags, record lengths, width/height, blend modes, and descriptor-backed color/image-filter saveLayer variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-052922/suite.tsv`.
- The current focused descriptor handle lifecycle/type guard checkpoint is `CASE_GROUPS=descriptor-handles-invalid`,
  which passed 48/48 with `fallback_sum=48`, zero unsupported rows, zero JBR picture frames, and zero JBR command
  frames across missing handles, use-after-evict, eviction record flags, child missing/use-after-evict, and wrong-type
  guards for shader, color-filter, image-filter, path-effect, and RuntimeEffect descriptor families:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-041151/suite.tsv`.
- The current focused gradient parser guard checkpoint is `CASE_GROUPS=gradient-invalid`, which passed 60/60 with
  `fallback_sum=60`, zero unsupported rows, zero JBR picture frames, and zero JBR command frames across
  linear/radial/sweep stroke width, tile mode, color count, stop order, path-gradient, and radial radius
  malformed-stream guards:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-045310/suite.tsv`.
- The current focused RuntimeEffect parser/semantic guard checkpoint is `CASE_GROUPS=runtime-effect-invalid`, which
  passed 62/62 with `fallback_sum=56`, six intentional unsupported-picture rows, 7,806 JBR picture frames, and zero
  JBR command frames. The unsupported rows are the invalid uniform/child/nested-child schema fallbacks for shader and
  color-filter RuntimeEffect descriptors; the remaining rows rejected through structured command fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-060107/suite.tsv`.
- Descriptor/effect child handle validation has a default-order quick path for blur image-filter children too. The
  existing blur use-after-evict, missing-child, and wrong-effect-type sentinels are now inserted into the default
  command-probe order beside the offset image-filter child cases. Exact validation passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-230108/suite.tsv`;
  `CASE_GROUPS=descriptor-handles-invalid` passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-230330/suite.tsv`;
  and three bounded default-order ranges passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-233519/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-233729/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-233942/suite.tsv`.
  Magic Jewel also supports `LIST_CASES=true` to print the resolved default/group/range case list without launching
  validation. That no-run check confirmed `CASE_GROUPS=descriptor-handles-invalid` is fully represented in default
  ordering; the expanded default use-after-evict neighborhood passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-234632/suite.tsv`.
  The same no-run scan now shows the checked invalid quick groups have no rows missing from default ordering, after
  fixing the effect-descriptor insertion anchor for corner/stamped/chain path-effect descriptor sentinels. The bounded
  effect-descriptor default-order range passed at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-000243/suite.tsv`.
  Magic Jewel README and `LIST_CASE_GROUPS=true` now expose the full quick-group surface, including shader-ref,
  fill-rect color-filter, and blend-mode invalid subsets that were previously implemented but hidden from the listing.
  A no-run membership scan confirmed every listed group remains represented in default ordering, and the newly exposed
  shader-ref/fill-rect color-filter/blend-mode invalid groups passed as a 10-row focused validation:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-002228/suite.tsv`.
  The suite also supports `LIST_CASE_GROUP_COUNTS=true` so agents can pick smaller area batches by size before
  launching validation.
- Primitive command parser guards now have the same point-to-point workflow. Skiko can corrupt a recorded stroke cap,
  transform record flags, clip operation, draw-points point-count lower/upper bounds, draw-points record length, or
  draw-vertices vertex-count lower/upper bounds, record length, vertex mode, blend mode, and index-count lower/upper bounds after
  recording. Magic Jewel exposes the exact rows and the `CASE_GROUPS=primitive-invalid` quick path. The latest
  draw-points upper-bound exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-014443/suite.tsv`.
  The thirteen-row grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-014536/suite.tsv`.
  Bounded default-order validation passed around the point insertion slot:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-015718/suite.tsv`.
  The bounded vertices-to-blend-mode default-order range also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-021540/suite.tsv`.
  The earlier draw-vertices upper-bound exact run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-013122/suite.tsv`.
  The earlier twelve-row grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-013258/suite.tsv`.
  The earlier draw-vertices index-count row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-202605/suite.tsv`.
  The earlier ten-row grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-202656/suite.tsv`.
  A periodic full default command-probe sweep then passed with all five draw-vertices sentinels in the default set.
  Aggregate: 416/416 passed, 26 intentional unsupported-picture rows, 30,096 JBR picture frames, 201,780 JBR command
  frames, and 279 structured fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-203639/suite.tsv`.
  The draw-vertices blend-mode row also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-201535/suite.tsv`.
  The nine-row grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-201625/suite.tsv`.
  The draw-vertices vertex-mode row also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-200424/suite.tsv`.
  The eight-row grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-200517/suite.tsv`.
  The draw-vertices record-length row also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-195430/suite.tsv`.
  The seven-row grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-195521/suite.tsv`.
  The draw-vertices vertex-count row also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-194429/suite.tsv`.
  The earlier six-row grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-194537/suite.tsv`.
  The draw-points record-length row also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-143828/suite.tsv`.
  The five-row grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-143915/suite.tsv`.
  The earlier point-count-only grouped validation remains a useful baseline:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-093531/suite.tsv`.
- SaveLayer parser guards continue to use the quick exact/group workflow. Skiko can now rewrite record flags to the
  antialias bit for plain `COMMAND_SAVE_LAYER` and every supported saveLayer variant that JBR requires to use
  `COMMAND_RECORD_FLAGS_NONE`: op 44 color-filter, op 50 blend-mode, op 51 blend/color-filter, op 52
  color-filter-ref, op 54 blend/color-filter-ref, and op 55 image-filter-ref. JBR rejects each malformed stream before
  replay. The plain saveLayer exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-045239/suite.tsv`.
  The six-row variant record-flags exact run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-043049/suite.tsv`.
  The expanded `CASE_GROUPS=save-layer-invalid` run now covers twenty-one malformed saveLayer rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-040032/suite.tsv`.
  Bounded default-order validation for the saveLayer insertion range also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-041438/suite.tsv`.
  The same quick group now also covers op 44 `COMMAND_SAVE_LAYER_COLOR_FILTER` width/height lower-bound guards. The
  exact two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-044744/suite.tsv`.
  The expanded group covered twenty-three malformed saveLayer rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-044919/suite.tsv`.
  The bounded default-order saveLayer range passed again with the op 44 dimension rows included:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-050428/suite.tsv`.
  Op 50 `COMMAND_SAVE_LAYER_BLEND_MODE` width/height lower-bound guards now use the same quick path. The exact two-row
  run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-052535/suite.tsv`.
  The expanded group covered twenty-five malformed saveLayer rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-052706/suite.tsv`.
  The bounded default-order saveLayer range passed again with the op 50 dimension rows included:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-054336/suite.tsv`.
  Op 51 `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER` width/height lower-bound guards now use the same quick path. The exact
  two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-060758/suite.tsv`.
  The expanded group covered twenty-seven malformed saveLayer rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-060928/suite.tsv`.
  The bounded default-order saveLayer range passed again with the op 51 dimension rows included:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-062712/suite.tsv`.
  Alpha bounds are now covered for the remaining supported non-ref saveLayer variants: op 44 color-filter, op 50
  blend-mode, and op 51 blend/color-filter. The exact three-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-065320/suite.tsv`.
  The expanded group covered thirty malformed saveLayer rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-065533/suite.tsv`.
  The bounded default-order saveLayer range passed again with the non-ref alpha rows included:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-071506/suite.tsv`.
  Record-length guards are now covered for op 44 color-filter, op 50 blend-mode, and op 51 blend/color-filter. The
  exact three-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-074334/suite.tsv`.
  The expanded group covered thirty-three malformed saveLayer rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-074550/suite.tsv`.
  The bounded default-order saveLayer range passed again with the record-length rows included:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-080718/suite.tsv`.
- Effect descriptor parser guards now also cover record flags. Skiko can rewrite
  `COMMAND_DEFINE_EFFECT_DESCRIPTOR` record flags to the antialias bit, and Magic Jewel exposes the exact
  `commands-invalid-effect-descriptor-record-flags-fallback` row in the `effect-descriptor-invalid` quick group. The
  exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-050237/suite.tsv`.
  The expanded group covered twenty-eight malformed effect-descriptor rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-050312/suite.tsv`.
- Shader descriptor parser guards now mirror that record-flags coverage. Skiko can rewrite
  `COMMAND_DEFINE_SHADER_DESCRIPTOR` record flags to the antialias bit, and Magic Jewel exposes the exact
  `commands-invalid-shader-descriptor-record-flags-fallback` row in the `shader-descriptor-invalid` quick group. The
  exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-051747/suite.tsv`.
  The expanded group covered thirty malformed shader-descriptor rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-051827/suite.tsv`.
- Image definition parser guards now cover record flags, width/height bounds, and the existing pixel payload-length
  guard. Skiko can rewrite `COMMAND_DEFINE_IMAGE_ARGB` record flags to the antialias bit, width/height to `0` or
  `4097`, and pixel count to mismatch the payload. The record-flags exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-053349/suite.tsv`.
  The width/height exact four-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-030459/suite.tsv`.
  The expanded group covered twenty-seven malformed image definition/cache-key and image-ref rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-030826/suite.tsv`.
  The bounded default-order range from `commands-core-primitives` through `commands-point-lines` also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-032618/suite.tsv`.
- Image cache eviction parser guards now cover record flags too. Skiko can rewrite
  `COMMAND_EVICT_IMAGE_CACHE_KEY` record flags to the antialias bit, and Magic Jewel exposes the exact
  `commands-invalid-image-evict-record-flags-fallback` row in the same `image-handles-invalid` quick group. The exact
  row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-084204/suite.tsv`.
  The expanded group covered twenty-two malformed image definition/cache-key/eviction and image-ref rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-084246/suite.tsv`.
- Image cache clear is now a live producer path, not only parser/native support. CMP queues one
  `COMMAND_CLEAR_IMAGE_CACHE` for the next top-level command frame after `clearInteropCachesForSurfaceChange()`, while
  test-only reset stays local-only. The focused CMP recorder tests passed, the supported Magic Jewel
  `commands-forced-context-dynamic-images` row passed with scoped CMP/JBR clear markers, and Skiko/Magic now cover the
  op 18 record-flags parser guard:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-091119/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-091332/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-091447/suite.tsv`.
  Focused forced-context image-ref screenshot parity passed with the same scoped-clear contract:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260520-092645/suite.tsv`.
- Magic Jewel report validation now reads image-cache clear/evict expectations from full logs, because scoped cache
  clears are one-shot markers and can occur before the sampled-log window in long default rows. The default
  `commands-forced-context-dynamic-images` row passed after that harness fix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-102630/suite.tsv`.
- Descriptor-handle eviction parser guards now mirror that record-flags coverage for both
  `COMMAND_EVICT_SHADER_HANDLE` and `COMMAND_EVICT_COLOR_FILTER_HANDLE`. The exact two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-085719/suite.tsv`.
  A focused five-row descriptor-handle slice around the new rows passed as the quick adjacent-area validation:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-085837/suite.tsv`.
- Font-data definition parser guards now cover record flags too. Skiko can rewrite cache-front-loaded
  `COMMAND_DEFINE_FONT_DATA` record flags to the antialias bit, Magic Jewel exposes the exact
  `commands-invalid-font-data-record-flags-fallback` row in the `native-text-invalid` quick group, and report
  validation has an explicit one-shot fallback plus recovery mode for definitions emitted during cache warmup. The
  exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-055135/suite.tsv`.
  The expanded group covered eleven native text/font-data parser rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-055209/suite.tsv`.
- A periodic full default command-probe sweep then passed with this row in the default set. Aggregate: 406/406 passed,
  26 intentional unsupported-picture rows, 8,744 JBR picture frames, 49,068 JBR command frames, and 269 structured
  fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-055642/suite.tsv`.
- A split broad command-probe consolidation passed after the draw-points sentinel and report-validation fix. Combined
  resumed aggregate across the default prefix/tail segments: 410/410 passed, 27 intentional unsupported-picture rows,
  27,097 JBR picture frames, 147,205 JBR command frames, and 273 structured fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-093730/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-102727/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-123615/suite.tsv`.
- A full default command-probe sweep then passed after adding the draw-points record-length sentinel. The run used
  command-semantic validation with screenshot assertions disabled and included both draw-points invalid rows in the
  default set. Aggregate: 411/411 passed, 27 intentional unsupported-picture rows, 25,809 JBR picture frames,
  151,351 JBR command frames, and 274 structured fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-144422/suite.tsv`.
- A full default command-probe sweep then passed after adding the draw-vertices parser sentinel series and validating
  the effect descriptor version/payload-count/record-length rows. The run used command-semantic validation with
  screenshot assertions disabled. Aggregate: 416/416 passed, 26 intentional unsupported-picture rows, 30,096 JBR
  picture frames, 201,780 JBR command frames, and 279 structured fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-203639/suite.tsv`.
- The latest full default command-probe consolidation passed after rebuilding local JBR Skia artifacts. Aggregate:
  401/401 passed, 26 intentional unsupported-picture rows, 32,480 JBR picture frames, 184,608 JBR command frames, and
  264 structured fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-002459/suite.tsv`.
  If command probes suddenly report `SKIKO_JBR_INTEROP_FALLBACK reason=service-unavailable`, rebuild the local
  artifacts before diagnosing recorder/parser changes:
  `./scripts/rebuild-jbr-skia-local-artifacts.sh`.
- Command-stream header flags now have a live quick-path sentinel. Skiko's generic stream corruption switch emits
  `SKIKO_JBR_INTEROP_COMMAND_STREAM_FLAGS_CORRUPTED`, and Magic Jewel exposes both the exact
  `commands-invalid-command-stream-flags-fallback` row and `CASE_GROUPS=stream-invalid`. The exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-190837/suite.tsv`.
  The one-row group passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-190836/suite.tsv`.
- The same quick group now covers per-record command flags. Skiko can rewrite the first command record flags word to
  `2` and emits `SKIKO_JBR_INTEROP_COMMAND_RECORD_FLAGS_CORRUPTED`. The exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-192645/suite.tsv`.
  The expanded `CASE_GROUPS=stream-invalid` run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-192129/suite.tsv`.
- Stream header coordinate-space and paint-format guards now have live quick-path sentinels too. Skiko can rewrite the
  coordinate-space or paint-format header words to unsupported values and emits typed markers for both. The exact
  two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-193259/suite.tsv`.
  The expanded four-row `CASE_GROUPS=stream-invalid` run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-193047/suite.tsv`.
- Stream payload and first-record length guards are now in the same quick group. Skiko can rewrite the payload length
  to negative, truncated, or extra values, or rewrite the first command record length, and emits typed markers for each
  path. The exact four-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-193923/suite.tsv`.
  The expanded eight-row `CASE_GROUPS=stream-invalid` run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-193642/suite.tsv`.
- The already-live unknown effect descriptor type sentinel was refreshed again on current artifacts. The exact
  `commands-invalid-effect-descriptor-type-fallback` row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-023959/suite.tsv`.
  The scoped `CASE_GROUPS=effect-descriptor-invalid` area group then covered twenty-eight malformed
  effect-descriptor rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-024048/suite.tsv`.
- Fill-rect blend-mode scalar validation now has a quick group. Skiko can corrupt op 41
  `COMMAND_FILL_RECT_BLEND_MODE` width or height to `-1`, matching JBR's parser bounds guards. The exact two-row run
  passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-172416/suite.tsv`.
  The focused `CASE_GROUPS=blend-mode-invalid` run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-172545/suite.tsv`.
  A bounded default-order range from `commands-blend-mode` through `commands-graphics-layer` also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-172903/suite.tsv`.
- Fill-rect color-filter scalar validation now has the same quick path. Skiko can corrupt op 42
  `COMMAND_FILL_RECT_COLOR_FILTER` tint blend mode, width, or height after recording, and op 47
  `COMMAND_FILL_RECT_COLOR_FILTER_REF` width or height after recording the descriptor-backed handle path. The original
  exact three-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-173809/suite.tsv`.
  The new op 47 exact two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-022809/suite.tsv`.
  The focused `CASE_GROUPS=fill-rect-color-filter-invalid` run now covers five rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-022940/suite.tsv`.
  A bounded default-order range from `commands-raw-blend-color-filter-fallback` through `commands-color-filter` also
  passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-023307/suite.tsv`.
- Descriptor-backed fill-rect shader refs now have scalar parser coverage. Skiko can corrupt op 58
  `COMMAND_FILL_RECT_SHADER_REF` horizontal bounds, vertical bounds, or `alpha1000` after recording. The exact
  three-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-182225/suite.tsv`.
  The focused `CASE_GROUPS=shader-ref-invalid` run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-183006/suite.tsv`.
  A bounded default-order range from `commands-invalid-shader-descriptor-use-fallback` through
  `commands-invalid-descriptor-use-after-evict-fallback` also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-183458/suite.tsv`.
- Stroke-path dash path-effect parser coverage now includes the scalar guards adjacent to the existing verb guard.
  Skiko can corrupt op 61 `COMMAND_STROKE_PATH` dash path-effect interval count or interval value after recording. The
  exact two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-184636/suite.tsv`.
  The focused `CASE_GROUPS=path-invalid` run now covers seven malformed path rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-184818/suite.tsv`.
- SaveLayer invalid validation now has its own quick group. Skiko can corrupt plain `COMMAND_SAVE_LAYER` alpha to
  `1001` and tint-filter `COMMAND_SAVE_LAYER_COLOR_FILTER` blend mode away from `SRC_IN`, matching JBR's parser
  bounds/type guards. The focused `CASE_GROUPS=save-layer-invalid` run covered both malformed saveLayer rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-030846/suite.tsv`.
  A bounded default-order range through the adjacent saveLayer rows also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-031011/suite.tsv`.
- The same saveLayer quick group now covers the remaining blend parser guards. Skiko can corrupt op 50
  `COMMAND_SAVE_LAYER_BLEND_MODE` to an unsupported blend mode, and op 51 `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER` so
  the tint color-filter blend mode is no longer `SRC_IN`. The focused four-row group passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-032849/suite.tsv`.
  The bounded saveLayer range also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-033128/suite.tsv`.
- Descriptor-handle validation now includes saveLayer color-filter handle uses. Skiko's descriptor-use corruption hook
  can rewrite op 52 and op 54 saveLayer color-filter handle pairs to undefined handles. The exact two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-034302/suite.tsv`.
  The descriptor-handle group also exposed a stale tail case name; after fixing it, the repaired tail passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-040920/suite.tsv`.
- SaveLayer color-filter handles also have targeted use-after-evict coverage. Skiko can insert
  `COMMAND_EVICT_COLOR_FILTER_HANDLE` immediately before op 52 or op 54 consumes the handle; both exact rows passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-041255/suite.tsv`.
- SaveLayer image-filter handle validation now mirrors the color-filter path for op 55
  `COMMAND_SAVE_LAYER_IMAGE_FILTER_REF`. Skiko can rewrite the graphics-layer render-effect handle to an undefined
  descriptor or insert an eviction immediately before use. The exact two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-081201/suite.tsv`.
  The follow-up `CASE_GROUPS=descriptor-handles-invalid` run covered forty-five malformed descriptor/child-handle rows;
  all forty-five passed with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-105843/suite.tsv`.
- SaveLayer image-filter replay also has a scalar alpha bounds sentinel for op 55. The row reuses Skiko's saveLayer
  alpha corruption hook against the graphics-layer render-effect path and rewrites `alpha1000` to `1001`. The focused
  row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-120426/suite.tsv`.
  The expanded `CASE_GROUPS=save-layer-invalid` quick group now covers five malformed saveLayer rows; all five passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-120842/suite.tsv`.
- SaveLayer image-filter replay now also covers the adjacent width/height bounds guards for op 55. Skiko can rewrite
  the graphics-layer render-effect saveLayer image-filter width or height to `-1`. The exact two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-121803/suite.tsv`.
  The expanded `CASE_GROUPS=save-layer-invalid` quick group now covers seven malformed saveLayer rows; all seven
  passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-121925/suite.tsv`.
  The bounded default-order saveLayer range also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-122729/suite.tsv`.
  It covered ten rows: two supported command-replay rows, seven malformed rows, and the existing raw color-filter
  fallback sentinel.
- Descriptor-backed saveLayer color-filter refs now have alpha bounds sentinels for op 52 and op 54. Skiko can rewrite
  `alpha1000` to `1001` after recording, and Magic Jewel covers both the direct saveLayer color-matrix path and the
  graphics-layer blend plus color-matrix path. The exact two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-124011/suite.tsv`.
  The `CASE_GROUPS=save-layer-invalid` quick group now covers nine malformed saveLayer rows; all nine passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-124137/suite.tsv`.
  The bounded default-order saveLayer range also passed with twelve rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-124859/suite.tsv`.
- Descriptor-backed saveLayer color-filter refs now also cover width/height bounds for op 52 and op 54. The focused
  four-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-140556/suite.tsv`.
  The `CASE_GROUPS=save-layer-invalid` quick group now covers thirteen malformed saveLayer rows; all thirteen passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-140848/suite.tsv`.
  The bounded default-order saveLayer range also passed with sixteen rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-141854/suite.tsv`.
- Op 54 `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF` now also has a focused saveLayer blend-mode bounds sentinel. The
  focused row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-152806/suite.tsv`.
  The `CASE_GROUPS=save-layer-invalid` quick group now covers fourteen malformed saveLayer rows; all fourteen passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-152857/suite.tsv`.
  The bounded default-order saveLayer range also passed with seventeen rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-153941/suite.tsv`.
- Descriptor-backed saveLayer refs now also cover record-length mismatch for op 52
  `COMMAND_SAVE_LAYER_COLOR_FILTER_REF`, op 54 `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF`, and op 55
  `COMMAND_SAVE_LAYER_IMAGE_FILTER_REF`. The focused exact rows passed with three expected structured fallbacks:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-083632/suite.tsv`.
  The expanded `CASE_GROUPS=save-layer-invalid` quick group covers 36 malformed saveLayer rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-083848/suite.tsv`.
  The bounded default-order saveLayer range passed 39/39 with the one expected raw-color-filter unsupported row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-090249/suite.tsv`.
- Plain op 13 `COMMAND_SAVE_LAYER` now has matching record-length coverage. The focused exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-093321/suite.tsv`.
  The expanded `CASE_GROUPS=save-layer-invalid` quick group covers 37 malformed saveLayer rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-093418/suite.tsv`.
  The bounded default-order saveLayer range passed 40/40 with the one expected raw-color-filter unsupported row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-095826/suite.tsv`.
- Shader descriptor version fallback now has an explicit `commands-invalid-shader-descriptor-version-fallback` row
  instead of relying on the older generic descriptor-version case name. The exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-102949/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` quick group passed 30/30:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-103042/suite.tsv`.
  The bounded default-order shader descriptor slice passed 31/31:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-110020/suite.tsv`.
- Image-handle validation now has a dedicated quick group. Skiko can rewrite a `COMMAND_DRAW_IMAGE_REF` cache key to an
  undefined key or insert `COMMAND_EVICT_IMAGE_CACHE_KEY` immediately before the draw. The focused two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-203912/suite.tsv`.
  The new `CASE_GROUPS=image-handles-invalid` run covered both image cache-key rows; both passed, with zero unsupported
  rows, zero picture rows, zero command replay rows, and one structured fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-204017/suite.tsv`.
- The same image-handle quick group now also targets inline color-filter and descriptor color-filter image refs. Skiko
  uses target-specific flags for op 16, op 45, and op 53 so mixed image scenes corrupt the intended image-ref command.
  The expanded `CASE_GROUPS=image-handles-invalid` run covered six malformed image cache-key rows; all six passed, with
  zero unsupported rows, zero picture rows, zero command replay rows, and one structured fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-204739/suite.tsv`.
- Image-handle validation also covers JBR's cached-image dimension check for plain `COMMAND_DRAW_IMAGE_REF`. Skiko can
  bump the recorded image width while leaving the cached image payload unchanged. The focused row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-205347/suite.tsv`.
  The grouped `CASE_GROUPS=image-handles-invalid` run now covers seven malformed image cache-key/dimension rows; all
  seven passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured fallback
  marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-205428/suite.tsv`.
- The image-handle quick group now covers width and height mismatches for all current image-ref replay forms: op 16,
  op 45, and op 53. The focused height spot-check passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-212843/suite.tsv`.
  The grouped `CASE_GROUPS=image-handles-invalid` run now covers twelve malformed image cache-key/dimension rows; all
  twelve passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured fallback
  marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-212945/suite.tsv`.
- The image quick group now also covers JBR's `COMMAND_DEFINE_IMAGE_ARGB` pixel payload length guard. Skiko can bump
  the recorded pixel count while leaving the record body unchanged, forcing pre-replay `command-stream-invalid`
  fallback. The focused row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-213840/suite.tsv`.
  The grouped `CASE_GROUPS=image-handles-invalid` run now covers thirteen malformed image definition/cache-key/dimension
  rows; all thirteen passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-213912/suite.tsv`.
- The image quick group now covers the `alpha1000` bounds guard for all current image-ref replay forms. Skiko rewrites
  the recorded alpha to `1001` for op 16, op 45, or op 53, and Magic Jewel requires the target-specific marker. The
  focused three-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-214844/suite.tsv`.
  The grouped `CASE_GROUPS=image-handles-invalid` run now covers sixteen malformed image definition/cache-key/dimension
  and alpha rows; all sixteen passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one
  structured fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-215036/suite.tsv`.
- The image quick group now covers the adjacent filter-quality bounds guard for the same image-ref forms. Skiko rewrites
  the recorded filter quality to `4` for op 16, op 45, or op 53. The focused three-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-220628/suite.tsv`.
  The grouped `CASE_GROUPS=image-handles-invalid` run now covers nineteen malformed image definition/cache-key/dimension,
  alpha, and filter-quality rows; all nineteen passed, with zero unsupported rows, zero picture rows, zero command replay
  rows, and one structured fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-220829/suite.tsv`.
- The image quick group also covers the inline color-filter image-ref blend-mode guard. Skiko rewrites op 45's recorded
  blend mode away from `SRC_IN`, matching JBR's parser/replay check. The focused row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-222714/suite.tsv`.
  The grouped `CASE_GROUPS=image-handles-invalid` run now covers twenty malformed image definition/cache-key/dimension,
  alpha, filter-quality, and blend-mode rows; all twenty passed, with zero unsupported rows, zero picture rows, zero
  command replay rows, and one structured fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-222802/suite.tsv`.
- Magic Jewel report validation now has `EXPECT_SCREENSHOT_ASSERTION=false` for command-only sweeps when window capture
  is unstable. `commands-live-animation` and `commands-popup-window` both passed with the screenshot gate disabled while
  still proving command replay, and `commands-runtime-effect-child-only` now allows the current three shader-handle
  definitions after focused validation:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-234743/suite.tsv`.
- Magic Jewel command-probe iteration now supports `CASES_FROM`/`CASES_UNTIL` range slicing, so local work can run a
  one-row smoke, a bounded area, or a resumed tail before spending time on periodic full default batches. One-row range
  smokes passed, including the post-guard check:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-235136/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-025324/suite.tsv`.
  The resumed command-only tail from `commands-runtime-effect-child-only` passed 292/292 rows with 94,687 JBR command
  frames, 17,970 expected picture-fallback frames, 17 expected unsupported-marker rows, and 208 total expected fallback
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-235421/suite.tsv`.
- Descriptor-handle validation now covers top-level path-effect descriptor uses. Skiko can rewrite or evict the
  `COMMAND_DRAW_PATH_PATH_EFFECT_REF` handle to exercise JBR's missing-handle, use-after-evict, and wrong-family
  checks. The focused three-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-200433/suite.tsv`.
  The follow-up `CASE_GROUPS=descriptor-handles-invalid` run covered thirty-nine malformed descriptor/child-handle rows;
  all thirty-nine passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-200630/suite.tsv`.
- Effect-child use-after-evict validation now also covers RuntimeEffect color-filter children and shader-color-filter
  effect children. The focused two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-193417/suite.tsv`.
  The follow-up `CASE_GROUPS=descriptor-handles-invalid` run covered thirty-six malformed descriptor/child-handle rows;
  all thirty-six passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-193605/suite.tsv`.
- Descriptor child-handle validation now covers shader child use-after-evict paths. Skiko can insert a shader-handle
  eviction immediately before descriptor validation for transformed shader, composite shader destination/source,
  shader-color-filter shader child, and RuntimeEffect shader child descriptors. The focused five-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-190602/suite.tsv`.
  The follow-up `CASE_GROUPS=descriptor-handles-invalid` run covered thirty-four malformed descriptor/child-handle rows;
  all thirty-four passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-190943/suite.tsv`.
- Descriptor child-handle validation now includes both children of composite shader descriptors. Skiko can corrupt the
  composite shader source child to point at a color-filter descriptor or an undefined shader handle, matching JBR's
  existing `srcHandle` validation alongside the previously covered destination child. The focused two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-184048/suite.tsv`.
  The follow-up `CASE_GROUPS=descriptor-handles-invalid` run covered twenty-nine malformed descriptor/child-handle rows;
  all twenty-nine passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-184218/suite.tsv`.
- Effect-descriptor invalid validation now includes with-input image-filter descriptor payload guards. Magic Jewel rows
  exercise blur-with-input sigma, negative sigma, tile mode, and offset-with-input delta using the existing Skiko
  corruption hooks. The focused four-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-181403/suite.tsv`.
  The follow-up `CASE_GROUPS=effect-descriptor-invalid` run covered twenty-seven malformed effect-descriptor rows; all
  twenty-seven passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-181711/suite.tsv`.
- Descriptor child-handle validation also covers blur-with-input image-filter missing-child and use-after-evict paths.
  The focused two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-175550/suite.tsv`.
  The follow-up `CASE_GROUPS=descriptor-handles-invalid` run covered twenty-four malformed descriptor/child-handle rows;
  all twenty-four passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-175712/suite.tsv`.
- Descriptor child-handle validation now includes the blur-with-input image-filter wrong-type path. Magic Jewel can
  record a blur-of-offset render-effect descriptor chain, and Skiko can rewrite the blur child handle to a
  color-filter descriptor. The focused row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-173959/suite.tsv`.
  The follow-up `CASE_GROUPS=descriptor-handles-invalid` run covered twenty-two malformed descriptor/child-handle rows;
  all twenty-two passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-174050/suite.tsv`.
- RuntimeEffect child-schema duplicate-index validation adds live shader and color-filter sentinels for the
  `seen[referencedChildIndex]` duplicate guard. Magic Jewel's RuntimeEffect child probes now record two named children;
  Skiko can rewrite the second named-child schema entry to reference the first child index. The focused two-row run and
  compact fourteen-row child-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-164327/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-164445/suite.tsv`.
  The follow-up `CASE_GROUPS=runtime-effect-invalid` consolidation covered sixty-two malformed RuntimeEffect rows; all
  sixty-two passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-165244/suite.tsv`.
- RuntimeEffect uniform-schema name-range validation adds live shader and color-filter sentinels for
  `offset + nameLength > schemaEnd`. Skiko can bump the first named-uniform schema name length just past the available
  schema payload while staying under the max-length guard; the focused two-row run and compact fourteen-row
  uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-153449/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-153628/suite.tsv`.
  The follow-up `CASE_GROUPS=runtime-effect-invalid` consolidation covered sixty malformed RuntimeEffect rows; all
  sixty passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-154609/suite.tsv`.
- RuntimeEffect child-schema name-range validation adds live shader and color-filter sentinels for
  `offset + nameLength > schemaEnd`. Skiko can bump the first named-child schema name length just past the available
  schema payload while staying under the max-length guard; the focused two-row run and compact fourteen-row
  child-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-142845/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-143020/suite.tsv`.
  The follow-up `CASE_GROUPS=runtime-effect-invalid` consolidation covered fifty-eight malformed RuntimeEffect rows;
  all fifty-eight passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-144007/suite.tsv`.
- RuntimeEffect child-schema max-name-length validation adds live shader and color-filter sentinels for
  `nameLength > 64`. Skiko can corrupt the first named-child schema entry so its name length is `65`; the focused
  two-row run and compact twelve-row child-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-132914/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-133102/suite.tsv`.
  The follow-up `CASE_GROUPS=runtime-effect-invalid` consolidation covered fifty-six malformed RuntimeEffect rows; all
  fifty-six passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-134009/suite.tsv`.
- RuntimeEffect child-schema hardening now also adds live shader and color-filter name-length sentinels for
  `nameLength <= 0`. Skiko can corrupt the first named-child schema entry so its name length is `0`; the focused
  two-row run and compact ten-row child-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-123344/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-123520/suite.tsv`.
  The follow-up `CASE_GROUPS=runtime-effect-invalid` consolidation covered fifty-four malformed RuntimeEffect rows; all
  fifty-four passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-124249/suite.tsv`.
- Latest RuntimeEffect child-schema hardening adds live shader and color-filter negative referenced-child-index
  sentinels. Skiko can corrupt the first named-child schema entry so its referenced index is `-1`, and JBR rejects the
  descriptor before native build. The focused two-row run and compact eight-row child-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-113957/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-114142/suite.tsv`.
  The follow-up `CASE_GROUPS=runtime-effect-invalid` consolidation covered fifty-two malformed RuntimeEffect rows; all
  fifty-two passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-114736/suite.tsv`.
- Current-artifact focused validation rechecked the live unknown effect descriptor type sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-111741/suite.tsv`.
  The `effect-descriptor-invalid` quick area group then covered twenty-three descriptor parser/fallback rows; all
  twenty-three passed with zero unsupported rows, zero picture rows, and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-111855/suite.tsv`.
- Latest scoped RuntimeEffect consolidation passed after the uniform-schema name-length and max-name-length sentinel
  pairs. The `runtime-effect-invalid` group covered fifty malformed RuntimeEffect rows; all fifty passed, with six
  intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-103723/suite.tsv`.
- Latest scoped RuntimeEffect consolidation passed after the shader/color-filter source-code, source-hash,
  uniform-name, and child-name sentinel batch. The `runtime-effect-invalid` group covered thirty-eight malformed
  RuntimeEffect rows; all thirty-eight passed, with six intentional picture-fallback/parser-only rows and zero command
  replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-012854/suite.tsv`.
- Latest RuntimeEffect child-schema hardening adds live shader and color-filter referenced-child-index sentinels.
  Skiko can corrupt the first named-child schema entry so its referenced index equals `childCount`, and JBR rejects the
  descriptor before native build. The focused two-row run and compact six-row child-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-020224/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-020355/suite.tsv`.
- Latest RuntimeEffect uniform-schema hardening adds live shader and color-filter float-count sentinels. Skiko can
  corrupt the first named-uniform schema entry so its `floatCount` is `0`, and JBR rejects the descriptor before native
  compile/build. The focused two-row run and compact six-row uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-021125/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-021257/suite.tsv`.
- RuntimeEffect uniform-schema hardening also adds live shader and color-filter float-offset sentinels. Skiko can
  corrupt the first named-uniform schema entry so its `floatOffset` is `-1`, and JBR rejects the descriptor before
  native compile/build. The focused two-row run and compact eight-row uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-025315/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-025445/suite.tsv`.
- RuntimeEffect uniform-schema range validation adds live shader and color-filter sentinels for
  `floatOffset > uniformFloatCount - floatCount`. Skiko can corrupt the first named-uniform schema entry so its
  `floatOffset` equals `uniformFloatCount`; the focused two-row run and compact ten-row uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-030358/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-030537/suite.tsv`.
- RuntimeEffect uniform-schema name-length validation adds live shader and color-filter sentinels for
  `nameLength <= 0`. Skiko can corrupt the first named-uniform schema entry so its name length is `0`; the focused
  two-row run and compact twelve-row uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-101012/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-101145/suite.tsv`.
- RuntimeEffect uniform-schema max-name-length validation adds live shader and color-filter sentinels for
  `nameLength > 64`. Skiko can corrupt the first named-uniform schema entry so its name length is `65`; the focused
  two-row run and compact fourteen-row uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-102412/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-102601/suite.tsv`.
  The latest follow-up `CASE_GROUPS=runtime-effect-invalid` consolidation covered fifty malformed RuntimeEffect rows
  after adding the name-length and max-name-length live sentinel pairs; all fifty passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-103723/suite.tsv`.
  The follow-up `CASE_GROUPS=runtime-effect-invalid` consolidation covered forty-six malformed RuntimeEffect rows after
  adding the float-offset and float-range live sentinel pairs; all forty-six passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-031347/suite.tsv`.
  The follow-up `CASE_GROUPS=runtime-effect-invalid` consolidation covered forty-two malformed RuntimeEffect rows after
  adding the child-index and uniform-schema float-count live sentinel pairs; all forty-two passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-021819/suite.tsv`.
- Direct path command validation now has a quick `path-invalid` group. It currently covers live `COMMAND_CLIP_PATH`,
  `COMMAND_DRAW_PATH`, `COMMAND_DRAW_PATH_PATH_EFFECT_REF`, `COMMAND_STROKE_PATH_DASH_PATH_EFFECT`, and
  `COMMAND_DRAW_SHADOW_PATH` unknown-verb sentinels that corrupt the first encoded path verb to `99` and require
  structured `command-stream-invalid` fallback before replay.
- Latest periodic full default command-probe consolidation after the RuntimeEffect lower-bound sentinel batch covered
  236 rows, all passed, with 110 command replay rows, 26 intentional picture-fallback rows, and 100 explicit
  structured fallback rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-213052/suite.tsv`.
- Latest native text validation starts live coverage for parser-only text and paragraph guards. Skiko can corrupt the
  recorded `COMMAND_DRAW_TEXT_UTF16` and `COMMAND_DRAW_PARAGRAPH_UTF16` font-size, weight, width, slant, and
  font-family-count slots to out-of-range values. Magic Jewel requires matching typed
  `SKIKO_JBR_INTEROP_TEXT_FONT_*_CORRUPTED` and `SKIKO_JBR_INTEROP_PARAGRAPH_FONT_*_CORRUPTED` markers, and JBR rejects
  each stream before replay with structured `command-stream-invalid` fallback. Focused validation plus the quick
  `native-text-invalid` group passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-114019/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-120803/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-121327/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-121603/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-122225/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-122535/suite.tsv`.
- Latest gradient validation adds live parser-guard rows for stroked gradient command widths. Skiko can corrupt the
  recorded stroke-width slot to `0` across linear, radial, and sweep rect/round-rect stroke variants, and can corrupt
  linear-gradient tile-mode slots to `4`, color-count slots to `1`, and second stop slots to `0`, and radial-gradient
  radius slots to `0`, tile-mode slots to `4`, color-count slots to `1`, and second stop slots to `0`, and
  sweep-gradient color-count slots to `1` and second stop slots to `0` across fill/stroke rect/round-rect variants.
  Path-gradient command guards are now live too: Skiko computes the variable path payload length, then corrupts linear
  path-gradient tile-mode/color-count/stop-order slots or radial path-gradient radius/tile-mode/color-count/stop-order
  slots or sweep path-gradient color-count/stop-order slots to invalid values.
  Path-gradient header guards are also live across linear/radial/sweep for invalid fill-type and negative
  path-data length, and path-gradient path-data validation now has live unknown-verb sentinels for linear, radial, and
  sweep commands.
  Magic Jewel requires typed `SKIKO_JBR_INTEROP_*_STROKE_WIDTH_CORRUPTED`,
  `SKIKO_JBR_INTEROP_RADIAL_GRADIENT*_RADIUS_CORRUPTED`, and
  `SKIKO_JBR_INTEROP_*_GRADIENT*_TILE_MODE_CORRUPTED` / `*_COLOR_COUNT_CORRUPTED` / `*_STOP_ORDER_CORRUPTED` markers,
  including the path-gradient markers, and JBR rejects each stream before replay. Focused validation plus the quick
  `gradient-invalid` and `gradient-path-invalid` groups passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-123644/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-171310/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-171855/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-172203/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-172925/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-173217/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-174220/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-174457/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-175821/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-180104/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-181911/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-182106/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-183701/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-183900/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-185444/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-185635/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-191609/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-191802/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-193808/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-194011/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-200346/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-200601/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-203301/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-203456/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-204153/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-204356/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-205145/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-205256/suite.tsv`.
  The broader post-family `CASE_GROUPS=gradient-invalid` consolidation also passed across fifty-one invalid-gradient
  rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-205847/suite.tsv`.
  The follow-up linear path-header focused and grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-212726/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-212838/suite.tsv`.
  The radial/sweep path-header focused and grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-213839/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-214053/suite.tsv`.
  The broader post-header `CASE_GROUPS=gradient-invalid` consolidation passed across fifty-seven invalid-gradient
  rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-214935/suite.tsv`.
  The linear path-data verb focused and grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-222317/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-222406/suite.tsv`.
  The radial/sweep path-data verb focused and grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-223824/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-223942/suite.tsv`.
- Latest RuntimeEffect source-code hardening adds live parser guards for invalid SKSL code units. Skiko can corrupt
  one RuntimeEffect shader descriptor or RuntimeEffect color-filter descriptor source code unit to `0` and recompute
  the source hash so JBR reaches the source-code range guard before native compile. Shader focused validation plus the
  quick `runtime-effect-invalid` group passed; the group covers thirty-two malformed RuntimeEffect rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-235533/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-235624/suite.tsv`.
  Color-filter source-code focused validation plus a narrower ten-row adjacent parser subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-002624/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-002719/suite.tsv`.
  Color-filter source-hash focused validation plus an eleven-row adjacent parser subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-003812/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-003922/suite.tsv`.
- Latest RuntimeEffect shader schema-name hardening adds a live parser guard for invalid named-uniform identifiers.
  Skiko can corrupt the first recorded RuntimeEffect shader uniform-name character to `1`, and JBR rejects the
  descriptor before native compile. Focused validation plus an eight-row adjacent shader parser subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-005128/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-005221/suite.tsv`.
- Latest RuntimeEffect color-filter schema-name hardening mirrors the same named-uniform identifier guard for
  color-filter descriptors. Skiko can corrupt the first recorded RuntimeEffect color-filter uniform-name character to
  `1`. Focused validation plus a twelve-row adjacent color-filter parser subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-010124/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-010219/suite.tsv`.
  RuntimeEffect uniform-schema float-count validation now has matching live shader and color-filter rows. The focused
  two-row run and compact six-row uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-021125/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-021257/suite.tsv`.
  RuntimeEffect uniform-schema float-offset validation now has matching live shader and color-filter rows. The focused
  two-row run and compact eight-row uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-025315/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-025445/suite.tsv`.
  RuntimeEffect uniform-schema float-range validation now has matching live shader and color-filter rows. The focused
  two-row run and compact ten-row uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-030358/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-030537/suite.tsv`.
  RuntimeEffect uniform-schema name-length validation now has matching live shader and color-filter rows. The focused
  two-row run and compact twelve-row uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-101012/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-101145/suite.tsv`.
  RuntimeEffect uniform-schema max-name-length validation now has matching live shader and color-filter rows. The
  focused two-row run and compact fourteen-row uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-102412/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-102601/suite.tsv`.
  The scoped `runtime-effect-invalid` group was rechecked after the float-offset and float-range pairs and now covers
  forty-six malformed RuntimeEffect rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-031347/suite.tsv`.
  The scoped `runtime-effect-invalid` group was rechecked after the child-index and uniform-schema float-count pairs
  and now covers forty-two malformed RuntimeEffect rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-021819/suite.tsv`.
- Latest RuntimeEffect shader child-schema hardening adds a live parser guard for invalid named-child identifiers.
  Skiko can corrupt the first recorded RuntimeEffect shader child-name character to `1`. Focused validation plus a
  five-row adjacent child-schema subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-011504/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-011554/suite.tsv`.
- Latest RuntimeEffect color-filter child-schema hardening mirrors the named-child identifier guard for color-filter
  descriptors. Skiko can corrupt the first recorded RuntimeEffect color-filter child-name character to `1`. Focused
  validation plus a five-row adjacent child-schema subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-012238/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-012327/suite.tsv`.
  RuntimeEffect child-schema referenced-index validation now has matching live shader and color-filter rows. The
  focused two-row run and compact six-row child-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-020224/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-020355/suite.tsv`.
  The follow-up `CASE_GROUPS=runtime-effect-invalid` consolidation covered thirty-eight malformed RuntimeEffect rows
  after the full source/schema-name batch; all thirty-eight passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-012854/suite.tsv`.
- Latest RuntimeEffect color-filter hardening adds live SKSL-length, uniform-count upper/lower-bound, child-count
  upper/lower-bound, named-uniform-count, and named-child-count sentinels. Skiko can corrupt one recorded
  RuntimeEffect color-filter descriptor length to `0`, `uniformFloatCount` to `257` or `-1`, `childCount` to `9` or
  `-1`, `namedUniformCount` to `17` or `-1`, or `namedChildCount` to `9` or `-1`; Magic Jewel requires target-specific corruption
  markers; focused and
  `runtime-effect-invalid` grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-124807/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-124855/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-131044/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-131129/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-133243/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-133331/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-135519/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-135603/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-141823/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-141907/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-144403/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-144448/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-151126/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-151507/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-171026/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-171111/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-173844/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-173928/suite.tsv`.
- Latest RuntimeEffect shader lower-bound hardening now adds live negative uniform-count, child-count,
  named-uniform-count, and named-child-count sentinels. Skiko can corrupt one recorded RuntimeEffect shader descriptor
  `uniformFloatCount`, `childCount`, `namedUniformCount`, or `namedChildCount` to `-1`, Magic Jewel requires matching
  typed corruption markers, and focused plus
  `runtime-effect-invalid` grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-181512/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-181601/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-184122/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-184211/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-190942/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-191029/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-195041/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-195130/suite.tsv`.
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
- Latest RuntimeEffect shader+color-filter lifecycle tightening caps the descriptor-backed color-filter side at one
  JBR effect-handle definition while leaving the animated shader side uncapped. Focused validation and a full default
  command sweep both passed; the full sweep covered 167 rows plus the header with all rows passing. Focused screenshot
  parity for the same row also passed, followed by a full screenshot parity sweep covering 106 passing command-replay
  rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-191213/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-191321/suite.tsv`, plus
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260512-204143/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260512-204525/suite.tsv`.
- Latest malformed shader descriptor metadata hardening adds a live unknown descriptor-type sentinel. Skiko can now
  corrupt one shader descriptor type after recording, Magic Jewel requires
  `SKIKO_JBR_INTEROP_SHADER_DESCRIPTOR_TYPE_CORRUPTED`, and the row fails closed with structured
  `command-stream-invalid` fallback before JBR replay. Focused validation passed, followed by a full default command
  sweep covering 168 passing rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-225953/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-230029/suite.tsv`.
- Latest malformed shader descriptor metadata hardening also adds a live payload-count mismatch sentinel. Skiko can
  corrupt one shader descriptor payload count after recording, Magic Jewel requires
  `SKIKO_JBR_INTEROP_SHADER_DESCRIPTOR_PAYLOAD_COUNT_CORRUPTED`, and JBR rejects the stream before replay. Focused
  validation passed, followed by a full default command sweep covering 169 passing rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-140244/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-140412/suite.tsv`.
- Latest malformed shader descriptor metadata hardening now also covers descriptor record-length mismatch. Skiko can
  shorten one shader descriptor record length after recording, Magic Jewel requires
  `SKIKO_JBR_INTEROP_SHADER_DESCRIPTOR_RECORD_LENGTH_CORRUPTED`, and JBR rejects the stream before replay. Focused
  validation passed, followed by a full default command sweep covering 170 passing rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-153009/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-153044/suite.tsv`.
- The existing shader descriptor version sentinel is now marker-gated too: Magic Jewel requires
  `SKIKO_JBR_INTEROP_DESCRIPTOR_VERSION_CORRUPTED`, and focused plus full default command validation passed with the
  row failing closed before replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-165524/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-165558/suite.tsv`.
- Latest malformed effect descriptor metadata hardening adds live version, payload-count, and record-length mismatch
  sentinels. Skiko can corrupt one effect descriptor metadata field after recording, Magic Jewel requires the matching
  typed `SKIKO_JBR_INTEROP_EFFECT_DESCRIPTOR_*_CORRUPTED` marker, and JBR rejects each stream before replay. Focused
  validation passed, followed by a full default command sweep covering 173 passing rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-183454/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-183706/suite.tsv`.
- Latest malformed effect descriptor metadata hardening now also covers unknown descriptor types. Skiko can corrupt one
  effect descriptor type after recording, Magic Jewel requires `SKIKO_JBR_INTEROP_EFFECT_DESCRIPTOR_TYPE_CORRUPTED`,
  and JBR rejects the stream before replay. Focused validation passed, followed by a full default command sweep
  covering 174 passing rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-204604/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-204652/suite.tsv`.
  Current-artifact focused validation plus the scoped `effect-descriptor-invalid` area group also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-225228/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-225310/suite.tsv`.
- Latest transformed shader descriptor hardening mirrors parser-only payload-count mismatch coverage in a live command
  row. Skiko can corrupt only transformed shader descriptors from payload count 11 to 10 after recording, Magic Jewel
  requires `SKIKO_JBR_INTEROP_TRANSFORMED_SHADER_DESCRIPTOR_PAYLOAD_COUNT_CORRUPTED`, and JBR rejects the stream before
  replay. Focused validation passed, followed by a full default command sweep covering 175 passing rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-224856/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-224940/suite.tsv`.
- Latest RuntimeEffect shader descriptor hardening mirrors parser-only source-hash mismatch coverage in a live command
  row. Skiko can corrupt the recorded RuntimeEffect shader source hash while leaving the SKSL payload unchanged, Magic
  Jewel requires `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_SOURCE_HASH_CORRUPTED`, and JBR rejects the stream before
  replay. Focused validation passed, followed by a full default command sweep covering 176 passing rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-005110/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-005156/suite.tsv`.
- Latest Perlin/noise shader descriptor hardening mirrors parser-only payload validation for kind, base frequency,
  octave count, and tile size in live command rows. Skiko can corrupt each field after recording, Magic Jewel requires
  the matching `SKIKO_JBR_INTEROP_PERLIN_NOISE_SHADER_*_CORRUPTED` marker, and JBR rejects each stream before replay.
  Focused validation passed, followed by a full default command sweep covering 180 passing rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-025354/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-025633/suite.tsv`.
- Latest tint color-filter descriptor hardening mirrors parser-only unsupported blend-mode coverage in a live command
  row. Skiko can corrupt one recorded tint color-filter descriptor from `SrcIn` to unsupported `Plus`, Magic Jewel
  requires `SKIKO_JBR_INTEROP_TINT_COLOR_FILTER_DESCRIPTOR_BLEND_MODE_CORRUPTED`, and JBR rejects the stream before
  replay. Focused validation passed, followed by a full default command sweep covering 181 passing rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-081541/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-081633/suite.tsv`.
- Latest color-matrix filter descriptor hardening mirrors parser-only non-finite payload validation in a live command
  row. Skiko can corrupt one recorded color-matrix descriptor payload slot to NaN, Magic Jewel requires
  `SKIKO_JBR_INTEROP_COLOR_MATRIX_FILTER_DESCRIPTOR_PAYLOAD_CORRUPTED`, and JBR rejects the stream before replay.
  Focused single-row validation passed, followed by the new grouped `CASE_GROUPS=effect-descriptor-invalid` subset
  covering six passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-102259/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-122441/suite.tsv`.
- Latest blur image-filter descriptor hardening mirrors JBR's non-finite sigma payload validation in a live command
  row. Skiko can corrupt one recorded blur descriptor sigma slot to NaN, Magic Jewel requires
  `SKIKO_JBR_INTEROP_BLUR_IMAGE_FILTER_DESCRIPTOR_SIGMA_CORRUPTED`, and JBR rejects the stream before replay. Focused
  single-row validation passed, followed by the grouped `CASE_GROUPS=effect-descriptor-invalid` subset covering seven
  passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-123706/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-123801/suite.tsv`.
- Latest offset image-filter descriptor hardening mirrors JBR's non-finite delta payload validation in a live command
  row. Skiko can corrupt one recorded offset descriptor delta slot to NaN, Magic Jewel requires
  `SKIKO_JBR_INTEROP_OFFSET_IMAGE_FILTER_DESCRIPTOR_DELTA_CORRUPTED`, and JBR rejects the stream before replay. Focused
  single-row validation passed, followed by the grouped `CASE_GROUPS=effect-descriptor-invalid` subset covering eight
  passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-124549/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-124635/suite.tsv`.
- Latest corner path-effect descriptor hardening mirrors JBR's finite/non-negative radius payload validation in a live
  command row. Skiko can corrupt one recorded corner path-effect radius slot to NaN, Magic Jewel requires
  `SKIKO_JBR_INTEROP_CORNER_PATH_EFFECT_DESCRIPTOR_RADIUS_CORRUPTED`, and JBR rejects the stream before replay.
  Focused single-row validation passed, followed by the grouped `CASE_GROUPS=effect-descriptor-invalid` subset
  covering nine passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-125458/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-125544/suite.tsv`.
- Latest stamped path-effect descriptor hardening mirrors JBR's finite/positive advance payload validation in a live
  command row. Skiko can corrupt one recorded stamped path-effect advance slot to NaN, Magic Jewel requires
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_ADVANCE_CORRUPTED`, and JBR rejects the stream before replay.
  Focused single-row validation passed, followed by the grouped `CASE_GROUPS=effect-descriptor-invalid` subset
  covering ten passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-130510/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-130556/suite.tsv`.
- Latest stamped path-effect descriptor hardening also mirrors JBR's finite/non-negative phase payload validation in a
  live command row. Skiko can corrupt one recorded stamped path-effect phase slot to NaN, Magic Jewel requires
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_PHASE_CORRUPTED`, and JBR rejects the stream before replay.
  Focused single-row validation passed, followed by the grouped `CASE_GROUPS=effect-descriptor-invalid` subset
  covering eleven passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-131759/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-131846/suite.tsv`.
- Latest stamped path-effect descriptor hardening also mirrors JBR's style bounds validation in a live command row.
  Skiko can corrupt one recorded stamped path-effect style slot to `99`, Magic Jewel requires
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_STYLE_CORRUPTED`, and JBR rejects the stream before replay.
  Focused single-row validation passed, followed by the grouped `CASE_GROUPS=effect-descriptor-invalid` subset
  covering twelve passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-132932/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-133018/suite.tsv`.
- Latest stamped path-effect descriptor hardening also mirrors JBR's fill-type validation in a live command row.
  Skiko can corrupt one recorded stamped path-effect fill-type slot to `99`, Magic Jewel requires
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_FILL_TYPE_CORRUPTED`, and JBR rejects the stream before replay.
  Focused single-row validation passed, followed by the grouped `CASE_GROUPS=effect-descriptor-invalid` subset
  covering thirteen passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-134123/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-134207/suite.tsv`.
- Latest stamped path-effect descriptor hardening also mirrors JBR's path-data-length validation in a live command
  row. Skiko can corrupt one recorded stamped path-effect path-data length slot to `4097`, Magic Jewel requires
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_PATH_DATA_LENGTH_CORRUPTED`, and JBR rejects the stream before
  replay. Focused single-row validation passed, followed by the grouped `CASE_GROUPS=effect-descriptor-invalid`
  subset covering fourteen passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-135433/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-135519/suite.tsv`.
- Latest full default command-probe sweep passed after the stamped path-effect descriptor payload hardening. It covered
  190/190 passing rows, including 110 command replay rows, 26 intentional picture-fallback rows, and 54 explicit
  structured fallback rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-140549/suite.tsv`.
- Latest blur image-filter descriptor hardening also mirrors JBR's tile-mode bounds validation in a live command row.
  Skiko can corrupt one recorded blur tile-mode slot to `99`, Magic Jewel requires
  `SKIKO_JBR_INTEROP_BLUR_IMAGE_FILTER_DESCRIPTOR_TILE_MODE_CORRUPTED`, and JBR rejects the stream before replay.
  Focused single-row validation passed after tightening the plain-blur slot guard, followed by the grouped
  `CASE_GROUPS=effect-descriptor-invalid` subset covering fifteen passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-161649/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-161742/suite.tsv`.
- Latest stamped path-effect descriptor hardening now separately exercises JBR's positive-advance bound. Skiko can
  corrupt one recorded stamped path-effect advance slot to `0`, Magic Jewel requires
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_ZERO_ADVANCE_CORRUPTED`, and JBR rejects the stream before replay.
  Focused single-row validation passed, followed by the grouped `CASE_GROUPS=effect-descriptor-invalid` subset
  covering sixteen passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-163606/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-163650/suite.tsv`.
- Latest stamped path-effect descriptor hardening now also separately exercises JBR's non-negative phase bound. Skiko
  can corrupt one recorded stamped path-effect phase slot to `-1`, Magic Jewel requires
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_NEGATIVE_PHASE_CORRUPTED`, and JBR rejects the stream before
  replay. Focused single-row validation passed, followed by the grouped `CASE_GROUPS=effect-descriptor-invalid`
  subset covering seventeen passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-165312/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-165402/suite.tsv`.
- Latest corner path-effect descriptor hardening now separately exercises JBR's non-negative radius bound. Skiko can
  corrupt one recorded corner path-effect radius slot to `-1`, Magic Jewel requires
  `SKIKO_JBR_INTEROP_CORNER_PATH_EFFECT_DESCRIPTOR_NEGATIVE_RADIUS_CORRUPTED`, and JBR rejects the stream before
  replay. Focused single-row validation passed, followed by the grouped `CASE_GROUPS=effect-descriptor-invalid`
  subset covering eighteen passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-170802/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-170922/suite.tsv`.
- Latest blur image-filter descriptor hardening now separately exercises JBR's non-negative sigma bound. Skiko can
  corrupt one recorded blur sigma slot to `-1`, Magic Jewel requires
  `SKIKO_JBR_INTEROP_BLUR_IMAGE_FILTER_DESCRIPTOR_NEGATIVE_SIGMA_CORRUPTED`, and JBR rejects the stream before replay.
  Focused single-row validation passed, followed by the grouped `CASE_GROUPS=effect-descriptor-invalid` subset
  covering nineteen passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-172401/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-172454/suite.tsv`.
- Latest stamped path-effect descriptor hardening now separately exercises JBR's non-negative path-data-length bound.
  Skiko can corrupt one recorded stamped path-effect path-data length slot to `-1`, Magic Jewel requires
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_NEGATIVE_PATH_DATA_LENGTH_CORRUPTED`, and JBR rejects the stream
  before replay. Focused single-row validation passed, followed by the grouped `CASE_GROUPS=effect-descriptor-invalid`
  subset covering twenty passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-174121/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-174214/suite.tsv`.
- Latest stamped path-effect descriptor hardening now also exercises JBR's path-data unknown-verb validation. Skiko can
  corrupt the first recorded stamped path-effect descriptor path verb to `99`, Magic Jewel requires
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_PATH_VERB_CORRUPTED`, and JBR rejects the stream before replay.
  Focused single-row validation passed, followed by the grouped `CASE_GROUPS=effect-descriptor-invalid` subset
  covering twenty-three passing malformed effect-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-230818/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-230917/suite.tsv`.
- Latest full default command-probe sweep passed after the finite-bound sentinel batch. It covered 196/196 passing
  rows, including 110 command replay rows, 26 intentional picture-fallback rows, and 60 explicit structured fallback
  rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-180227/suite.tsv`.
- Latest Perlin/noise shader descriptor hardening now separately exercises JBR's non-negative tile-size bound. Skiko
  can corrupt one recorded Perlin/noise shader tile-size slot to `-1`, Magic Jewel requires
  `SKIKO_JBR_INTEROP_PERLIN_NOISE_SHADER_NEGATIVE_TILE_SIZE_CORRUPTED`, and JBR rejects the stream before replay.
  Focused single-row validation passed, followed by the grouped `CASE_GROUPS=shader-descriptor-invalid` subset
  covering ten passing malformed shader-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-202424/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-202513/suite.tsv`.
- Latest Perlin/noise shader descriptor hardening now separately exercises JBR's positive octave-count lower bound.
  Skiko can corrupt one recorded Perlin/noise shader octave-count slot to `0`, Magic Jewel requires
  `SKIKO_JBR_INTEROP_PERLIN_NOISE_SHADER_ZERO_OCTAVES_CORRUPTED`, and JBR rejects the stream before replay. Focused
  single-row validation passed, followed by the grouped `CASE_GROUPS=shader-descriptor-invalid` subset covering
  eleven passing malformed shader-descriptor rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-203437/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-203549/suite.tsv`.
- Latest compatibility matrix passed after that promotion. It covered 57 rows plus the header: all rows passed, the
  happy-path row replayed commands, the 56 ABI/capability/API mismatch rows fell back with zero JBR command frames, and
  every row used a background probe window:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260512-183854/matrix.tsv`.
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
3. Use `CASES=...` for exact one-off rows, `CASES_FROM`/`CASES_UNTIL` for bounded/resumed default-order ranges, and
   Magic Jewel `CASE_GROUPS=...` for area slices during inner-loop work. Use `LIST_CASES=true`,
   `LIST_CASE_COUNT=true`, `LIST_CASE_GROUPS=true`, and `LIST_CASE_GROUP_COUNTS=true` where supported to plan slices
   without launching validation. Current command groups include `smoke`,
   `surface-transform-ui`, `shader-rendering`, `shader-composition-runtime`, `core-effects`,
   `graphics-layer-extras`, `save-layer-shader-fallbacks`, `stream-invalid`, `primitive-invalid`, `path-invalid`,
   `effect-descriptor-invalid`, `shader-descriptor-invalid`, `gradient-invalid`, `gradient-path-invalid`,
   `runtime-effect-invalid`, `descriptor-handles-invalid`, `shader-ref-invalid`, `image-handles-invalid`,
   `save-layer-invalid`, `color-filters`, `descriptor-lifecycle`, `fill-rect-color-filter-invalid`, `native-text`,
   `native-text-invalid`, `blend-mode-invalid`, and `graphics-layer`.
4. Run full default command/screenshot sweeps as checkpoint or periodic gates instead of every edit iteration.
5. Keep updating this compact plan; move verbose historical details to archive or focused docs, not back into this file.
6. Commit and push each major slice.

## Current Validation Hardening

Current validation gates are intentionally broad but summarized here to keep this file small:

- Shader composition and RuntimeEffect command/visual checkpoints are current. The focused command group
  `CASE_GROUPS=shader-composition-runtime` passed 15/15 with `fallback_sum=0`, `unsupported_rows=3`,
  `jbr_picture_frames=2068`, and `jbr_command_frames=19883`; the unsupported rows are the intentional raw
  RuntimeEffect shader/color-filter fallback sentinels plus the interop-scope marker on the descriptor-backed
  RuntimeEffect shader/color-filter row. The paired visual group `CASE_GROUPS=runtime-effect` passed 14/14 with
  `fallback_sum=2`, `jbr_picture_frames=0`, `jbr_command_frames=12380`, and average
  `bad_pixel_ratio=0.04879`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-122909/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-123909/suite.tsv`.
- Stroke-round-rect dash path-effect stroke metadata validation now covers op 60 stroke width/cap/join/miter guards.
  Skiko can corrupt stroke width to `0`, cap/join to `3`, or miter to `-1`; Magic Jewel requires the matching
  `SKIKO_JBR_INTEROP_STROKE_ROUND_RECT_DASH_PATH_EFFECT_STROKE_{WIDTH,CAP,JOIN,MITER}_CORRUPTED` marker; and the exact
  four-row run, `path-invalid` quick group, and narrower default-order path tail through `commands-point-lines` all
  passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-143055/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-143333/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-144716/suite.tsv`.
- Stroke-round-rect dash path-effect phase/interval validation now covers op 60 dash scalar guards. Skiko can corrupt
  phase to `-1` or the first dash interval to `0`; Magic Jewel requires the matching
  `SKIKO_JBR_INTEROP_STROKE_ROUND_RECT_DASH_PATH_EFFECT_{PHASE,INTERVAL}_CORRUPTED` marker; and the exact two-row run,
  `path-invalid` quick group, and narrower default-order path tail through `commands-point-lines` all passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-135947/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-140124/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-141245/suite.tsv`.
- Stroke-round-rect dash path-effect bounds/radii validation now covers op 60 right/bottom ordering and radius
  non-negativity. Skiko can corrupt right, bottom, radius X, or radius Y to `-1`; Magic Jewel requires the matching
  `SKIKO_JBR_INTEROP_STROKE_ROUND_RECT_DASH_PATH_EFFECT_*_CORRUPTED` marker; and the exact four-row run,
  `path-invalid` quick group, and narrower default-order path tail through `commands-point-lines` all passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-133047/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-133333/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-134426/suite.tsv`.
- Stroke-rect dash path-effect dimension validation now covers op 59 width and height lower bounds. Skiko can corrupt
  either field to `-1`, Magic Jewel requires the matching
  `SKIKO_JBR_INTEROP_STROKE_RECT_DASH_PATH_EFFECT_{WIDTH,HEIGHT}_CORRUPTED` marker, and the exact two-row run,
  `path-invalid` quick group, and narrower default-order path tail through `commands-point-lines` all passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-125148/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-125332/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-131533/suite.tsv`.
- Stroke-round-rect dash path-effect interval-count validation now covers op 60
  `COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT`. Skiko can corrupt the recorded dash interval count to `1`, Magic Jewel
  requires `SKIKO_JBR_INTEROP_STROKE_ROUND_RECT_DASH_PATH_EFFECT_INTERVAL_COUNT_CORRUPTED`, and the exact row,
  `path-invalid` quick group, and bounded `commands-core-primitives` through `commands-point-lines` slice all passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-122059/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-122157/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-122829/suite.tsv`.
- Stroke-rect dash path-effect interval-count validation now mirrors the same live parser guard for op 59
  `COMMAND_STROKE_RECT_DASH_PATH_EFFECT`. Skiko can corrupt the recorded dash interval count to `1`, Magic Jewel
  requires `SKIKO_JBR_INTEROP_STROKE_RECT_DASH_PATH_EFFECT_INTERVAL_COUNT_CORRUPTED`, and the exact row,
  `path-invalid` quick group, and bounded `commands-core-primitives` through `commands-point-lines` slice all passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-115513/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-115607/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-120233/suite.tsv`.
- Stroke-line dash path-effect interval-count validation now mirrors JBR's op 43 live parser guard. Skiko can corrupt
  `COMMAND_STROKE_LINE_DASH_PATH_EFFECT` by changing the dash interval count to `1`, Magic Jewel requires
  `SKIKO_JBR_INTEROP_STROKE_LINE_DASH_PATH_EFFECT_INTERVAL_COUNT_CORRUPTED`, and the exact row, `path-invalid` quick
  group, and bounded `commands-core-primitives` through `commands-point-lines` slice all passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-112712/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-112800/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-113311/suite.tsv`.
  This keeps the quick point-to-point iteration path focused on the affected path-effect family while periodically
  rechecking the surrounding default-order slice.
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
- RuntimeEffect shader descriptors now also have a focused live SKSL-length sentinel. The quick `CASES=...` row and
  the `CASE_GROUPS=runtime-effect-invalid` area sweep passed; the area group now covers 14 malformed RuntimeEffect
  rows and includes the shader descriptor positive `skslLength` parser branch. `CASE_GROUPS` expansion was tightened
  so curated groups are not also mutated by default-suite migration replacements.
- RuntimeEffect shader descriptors now also have a focused live uniform-float-count upper-bound sentinel. The quick
  `CASES=...` row and the `CASE_GROUPS=runtime-effect-invalid` area sweep passed; the area group now covers 15
  malformed RuntimeEffect rows and includes the shader descriptor `uniformFloatCount <= 256` parser branch.
- RuntimeEffect shader descriptors now also have a focused live child-count upper-bound sentinel. The quick `CASES=...`
  row and the `CASE_GROUPS=runtime-effect-invalid` area sweep passed; the area group now covers 16 malformed
  RuntimeEffect rows and includes the shader descriptor `childCount <= 8` parser branch.
- RuntimeEffect shader descriptors now also have a focused live named-uniform-count upper-bound sentinel. The quick
  `CASES=...` row and the `CASE_GROUPS=runtime-effect-invalid` area sweep passed; the area group now covers 17
  malformed RuntimeEffect rows and includes the shader descriptor `namedUniformCount <= 16` parser branch.
- RuntimeEffect shader descriptors now also have a focused live named-child-count bound sentinel. The quick
  `CASES=...` row and the `CASE_GROUPS=runtime-effect-invalid` area sweep passed; the area group now covers 18
  malformed RuntimeEffect rows and includes the shader descriptor `namedChildCount <= childCount` parser branch.
- The latest full default command-probe sweep after the radial-gradient/image shader descriptor sentinel batch covered
  200 rows, all passed, with 110 command replay rows, 26 intentional picture-fallback rows, and 64 explicit structured
  fallback rows.
- Shader color-filter descriptors now also have a focused live payload-count sentinel. The quick `CASES=...` row and
  the `CASE_GROUPS=shader-descriptor-invalid` area sweep passed; the area group now covers 29 malformed shader
  descriptor rows and includes the shader color-filter descriptor `payloadIntCount == 4` parser branch.
- Solid color shader descriptors now also have a focused live payload-count sentinel. The quick `CASES=...` row and
  the `CASE_GROUPS=shader-descriptor-invalid` area sweep passed; the area group now covers 28 malformed shader
  descriptor rows and includes the solid color descriptor `payloadIntCount == 1` parser branch.
- Composite shader descriptors now also have a focused live unsupported blend-mode sentinel. The quick `CASES=...` row
  and the `CASE_GROUPS=shader-descriptor-invalid` area sweep passed; the area group now covers 27 malformed shader
  descriptor rows and includes the composite descriptor `isSupportedBlendMode(blendMode)` parser branch.
- Image shader descriptors now also have a focused live height upper-bound sentinel. The quick `CASES=...` row and the
  `CASE_GROUPS=shader-descriptor-invalid` area sweep passed; the area group now covers 26 malformed shader descriptor
  rows and includes live width/height lower-bound and upper-bound checks plus tile-mode range checks.
- Image shader descriptors now also have a focused live width upper-bound sentinel. The quick `CASES=...` row and the
  `CASE_GROUPS=shader-descriptor-invalid` area sweep passed; the area group now covers 25 malformed shader descriptor
  rows and includes positive width/height, width upper-bound, and tile-mode range checks.
- Perlin/noise shader descriptors now also have a focused live negative tile-height sentinel. The quick
  `CASES=...` row and the `CASE_GROUPS=shader-descriptor-invalid` area sweep passed; the area group now covers 24
  malformed shader descriptor rows and includes both Perlin tile-width/tile-height upper bounds plus lower-bound checks.
- Perlin/noise shader descriptors now also have a focused live tile-height upper-bound sentinel. The quick
  `CASES=...` row and the `CASE_GROUPS=shader-descriptor-invalid` area sweep passed; the area group covers 23
  malformed shader descriptor rows and includes both Perlin tile-width and tile-height upper-bound checks.
- Sweep-gradient shader descriptors now also have a focused live invalid stop-order sentinel using the
  descriptor-backed sweep-gradient shader-plus-color-filter probe. The quick `CASES=...` row and the
  `CASE_GROUPS=shader-descriptor-invalid` area sweep passed; the area group now covers 22 malformed shader descriptor
  rows and live strictly-increasing gradient-stop validation now spans linear, radial, and sweep descriptors.
- Radial-gradient shader descriptors now also have a focused live invalid stop-order sentinel using the
  descriptor-backed composite shader probe. The quick `CASES=...` row and the
  `CASE_GROUPS=shader-descriptor-invalid` area sweep passed; the area group now covers 21 malformed shader descriptor
  rows and includes strictly-increasing gradient-stop validation for linear and radial descriptors.
- Linear-gradient shader descriptors now have a focused live invalid stop-order sentinel using the descriptor-backed
  linear-gradient shader-plus-color-filter probe. The quick `CASES=...` row and the
  `CASE_GROUPS=shader-descriptor-invalid` area sweep passed; the area group now covers 20 malformed shader descriptor
  rows and includes JBR's strictly-increasing gradient-stop validation.
- Image shader descriptors now also have a focused live invalid Y tile-mode sentinel using the descriptor-backed
  image-shader-plus-color-filter probe. The quick `CASES=...` row and the
  `CASE_GROUPS=shader-descriptor-invalid` area sweep passed; the area group now covers 19 malformed shader descriptor
  rows and image shader descriptor width, height, tile-mode-X, and tile-mode-Y bounds are all covered by live rows.
  A periodic full default command-probe sweep after the three image shader descriptor slices covered 206/206 passing
  rows, with 110 command replay rows, 26 intentional picture-fallback rows, and 70 explicit structured fallback rows.
- Image shader descriptors now also have a focused live invalid X tile-mode sentinel using the descriptor-backed
  image-shader-plus-color-filter probe. The quick `CASES=...` row and the
  `CASE_GROUPS=shader-descriptor-invalid` area sweep passed; the area group now covers 18 malformed shader descriptor
  rows.
- Image shader descriptors now also have a focused live invalid-height sentinel using the descriptor-backed
  image-shader-plus-color-filter probe. The quick `CASES=...` row and the
  `CASE_GROUPS=shader-descriptor-invalid` area sweep passed; the area group now covers 17 malformed shader descriptor
  rows while the previous full default command-probe checkpoint remains the 203-row sweep below.
- Sweep-gradient shader descriptors now have a focused live invalid color-count sentinel using a descriptor-backed
  sweep-gradient shader-plus-color-filter probe. The quick `CASES=...` row and the
  `CASE_GROUPS=shader-descriptor-invalid` area sweep passed, and the latest full default command-probe sweep covered
  203 rows, all passed, with 110 command replay rows, 26 intentional picture-fallback rows, and 67 explicit structured
  fallback rows.
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
- Lighting color-filter descriptors now have a focused live payload-count sentinel. The quick `CASES=...` row and the
  `CASE_GROUPS=effect-descriptor-invalid` area sweep passed; the area group now covers 21 malformed effect descriptor
  rows and includes the lighting-specific `payloadIntCount == 2` parser branch.
- Chain path-effect descriptors now have a focused live payload-count sentinel. The quick `CASES=...` row and the
  `CASE_GROUPS=effect-descriptor-invalid` area sweep passed; the area group now covers 22 malformed effect descriptor
  rows and includes the chain-specific `payloadIntCount == 4` parser branch.
- Tint color-filter descriptors now have a focused live unsupported blend-mode sentinel that rewrites recorded
  descriptor payload from `SrcIn` to `Plus`, requires the typed Skiko corruption marker, and fails closed before any JBR
  picture or command replay.
- Color-matrix filter descriptors now have a focused live non-finite payload sentinel and belong to the
  `effect-descriptor-invalid` command group for quicker parser/replay validation iterations.
- Blur image-filter descriptors now have a focused live non-finite sigma sentinel in the same grouped validation path.
- Blur image-filter descriptors now also have a focused live invalid-tile-mode sentinel in the same grouped validation path.
- Offset image-filter descriptors now have a focused live non-finite delta sentinel in the same grouped validation path.
- Corner path-effect descriptors now have a focused live non-finite radius sentinel in the same grouped validation path.
- Stamped path-effect descriptors now have a focused live non-finite advance sentinel in the same grouped validation path.
- Stamped path-effect descriptors now also have a focused live non-finite phase sentinel in the same grouped validation path.
- Stamped path-effect descriptors now also have a focused live invalid-style sentinel in the same grouped validation path.
- Stamped path-effect descriptors now also have a focused live invalid-fill-type sentinel in the same grouped validation path.
- Stamped path-effect descriptors now also have a focused live invalid-path-data-length sentinel in the same grouped validation path.
- Linear-gradient shader descriptors now have a focused live invalid tile-mode sentinel using the descriptor-backed
  linear-gradient shader-plus-color-filter probe. Solid color and shader color-filter descriptors have payload-count
  sentinels, composite shader descriptors have an unsupported blend-mode sentinel, radial-gradient shader descriptors
  have invalid-radius and invalid-tile-mode sentinels using the descriptor-backed composite shader probe,
  sweep-gradient shader descriptors have an invalid color-count sentinel using a descriptor-backed sweep-gradient
  shader-plus-color-filter probe, and image shader descriptors have invalid width, width upper-bound, height,
  height upper-bound, X tile-mode, and Y tile-mode sentinels using the descriptor-backed image-shader-plus-color-filter
  probe. Linear-gradient, radial-gradient, and sweep-gradient shader descriptors also have invalid stop-order sentinels.
  Perlin/noise shader descriptors cover kind, frequency, octave bounds, zero octave count, tile-width upper bound,
  tile-height upper bound, negative tile-size rejection, and negative tile-height rejection.
  `CASE_GROUPS=shader-descriptor-invalid` is the quick parser/replay validation path for this shader family and now
  covers thirty malformed shader descriptor rows.
- Day-to-day malformed-descriptor work now uses exact `CASES=...` rows first, then curated `CASE_GROUPS=...`
  area sweeps before periodic full default command-probe batches, keeping iteration tight while preserving full-suite
  checkpoints.
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

Use focused `CASES=...` subsets and curated `CASE_GROUPS=...` area sweeps before broad default sweeps.
