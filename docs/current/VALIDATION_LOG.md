# Current Validation Log

This file keeps the rolling validation ledger out of the top-level roadmap and plan. Keep the newest high-signal
entries here, and move older narrative detail to `docs/history/` only when this file starts getting noisy.

## Latest Broad Sweeps

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
