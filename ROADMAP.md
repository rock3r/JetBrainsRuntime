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
- Continue shader/effect lifecycle coverage: create, use, context-scoped cache hit, compile/build failure, descriptor
  eviction, resize, and forced destination context migration.
- Tighten stable descriptor definition/reuse gates on supported rows where real report data proves the marker contract.
- Continue closing transform/graphics-layer edge gaps as they appear in real recorder ground truth.
- Keep old/new screenshot parity coverage broad enough to catch text/color/placement regressions, including button
  chrome, embedded resource fonts, system fonts, point dots, shader descriptors, RuntimeEffect rows, and graphics-layer
  transforms.
- Keep compatibility matrix coverage current after each ABI/capability-affecting slice.
- Keep branches committed and pushed to the user's GitHub forks at each major step.
- Keep the top-level plan/roadmap compact. Move verbose historical narrative into `docs/history/` or focused
  `docs/current/` ledgers when these files start to crowd agent context.

## Latest Validations

- Magic Jewel full default command-probe sweep passed after the RuntimeEffect cache-gate calibration and focused
  invalid parser/semantic refreshes: 487/487 passed, `fallback_sum=350`, `unsupported_rows=26`,
  `jbr_picture_frames=32385`, `jbr_command_frames=183405`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-023649/suite.tsv`.
- Magic Jewel compatibility matrix passed after the full command-probe consolidation refresh: 57/57 passed,
  `fallback_sum=56`, `jbr_command_frames=1130`, background-window mode true for all rows, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260527-074723/matrix.tsv`.
- Magic Jewel artifact matrix passed on current ABI 106 local artifacts after the compatibility refresh: required rows
  2/2 passed, optional old-artifact rows skipped because no old bundle variables were set, `fallback_sum=1`,
  `jbr_command_frames=479`, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260527-081549/matrix.tsv`.
- Magic Jewel full default screenshot parity passed after the command-probe, compatibility, and artifact refreshes:
  106/106 passed, `fallback_sum=12`, `jbr_picture_frames=0`, `jbr_command_frames=107121`, average
  `bad_pixel_ratio=0.05158`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-081758/suite.tsv`.
- Magic Jewel screenshot parity suite now has no-run `LIST_CASES=true`, `LIST_CASE_COUNT=true`, and
  `CASES_FROM=... CASES_UNTIL=...` helpers. `parity-button-chrome` passed after the helper change with
  `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=1723`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-064955/suite.tsv`.
- Magic Jewel screenshot parity suite now also has curated visual `CASE_GROUPS=...` loops and matching no-run group
  discovery. `LIST_UNGROUPED_CASES=true` returns no rows, group counts cover all 106 default rows, and
  `CASE_GROUPS=smoke` passed 3/3 with `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=3334`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-065540/suite.tsv`.
- Magic Jewel focused visual `CASE_GROUPS=descriptor-lifecycle` passed through the new screenshot group path: 6/6
  passed, `fallback_sum=1`, `jbr_picture_frames=0`, `jbr_command_frames=6426`, average `bad_pixel_ratio=0.05078`,
  suite `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-070009/suite.tsv`.
- Magic Jewel focused visual `CASE_GROUPS=runtime-effect` passed through the new screenshot group path: 14/14 passed,
  `fallback_sum=2`, `jbr_picture_frames=0`, `jbr_command_frames=8923`, average `bad_pixel_ratio=0.04879`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-070611/suite.tsv`.
- Magic Jewel focused visual `CASE_GROUPS=shader-rendering` passed through the new screenshot group path: 18/18
  passed, `fallback_sum=4`, `jbr_picture_frames=0`, `jbr_command_frames=15092`, average `bad_pixel_ratio=0.04995`,
  suite `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-094321/suite.tsv`.
- Magic Jewel focused visual `CASE_GROUPS=graphics-layer-basic` passed through the new screenshot group path: 7/7
  passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=8326`, average `bad_pixel_ratio=0.05223`,
  suite `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-072753/suite.tsv`.
- Magic Jewel focused visual `CASE_GROUPS=graphics-layer-effects` passed through the new screenshot group path: 14/14
  passed, `fallback_sum=2`, `jbr_picture_frames=0`, `jbr_command_frames=11941`, average `bad_pixel_ratio=0.05725`,
  suite `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-100709/suite.tsv`.
- Magic Jewel focused visual `CASE_GROUPS=graphics-layer-clip-shadow-transform` passed through the new screenshot group
  path: 14/14 passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=14935`, average
  `bad_pixel_ratio=0.05296`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-074310/suite.tsv`.
- Magic Jewel focused visual `CASE_GROUPS=native-text` passed through the new screenshot group path: 14/14 passed,
  `fallback_sum=2`, `jbr_picture_frames=0`, `jbr_command_frames=8531`, average `bad_pixel_ratio=0.04758`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-075241/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=core-effects` passed as the paired command refresh for core visual
  drawing/effect coverage: 7/7 passed, `fallback_sum=0`, `unsupported_rows=2`, `jbr_picture_frames=1720`,
  `jbr_command_frames=5244`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-104223/suite.tsv`.
- Magic Jewel focused visual `CASE_GROUPS=core-drawing` passed through the new screenshot group path: 16/16 passed,
  `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=13217`, average `bad_pixel_ratio=0.05197`, completing
  one focused pass over every curated visual group; suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-104710/suite.tsv`.
- Magic Jewel full default screenshot parity passed after the focused visual group refresh: 106/106 passed,
  `fallback_sum=11`, `jbr_picture_frames=0`, `jbr_command_frames=93451`, average `bad_pixel_ratio=0.05158`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-081358/suite.tsv`.
- Magic Jewel compatibility matrix now has no-run `LIST_CASES=true` and `LIST_CASE_COUNT=true` helpers with unknown
  `CASES=...` validation. A focused `CASES=happy` launch passed with `fallback_sum=0`, `jbr_command_frames=607`, and
  `background_window=true`, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260526-092457/matrix.tsv`.
- Magic Jewel focused compatibility matrix `CASES=public-api-missing` also passed through the exact-row filter with
  `fallback_sum=1`, `jbr_command_frames=0`, and `background_window=true`, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260526-094135/matrix.tsv`.
- Magic Jewel artifact matrix now has exact-row `CASES=...` selection and no-run `LIST_CASES=true` /
  `LIST_CASE_COUNT=true` helpers with unknown `CASES=...` validation. A focused `CASES=current-all` launch passed with
  `fallback_sum=0`, `jbr_command_frames=599`, and `background_window=true`, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260526-093100/matrix.tsv`.
- Magic Jewel focused artifact matrix `CASES=missing-public-api` also passed through the exact-row filter with
  `fallback_sum=1`, `jbr_command_frames=0`, and `background_window=true`, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260526-093632/matrix.tsv`.
- Magic Jewel full default artifact matrix passed after the exact-row helper refresh: required rows 2/2 passed,
  optional old-artifact rows skipped because no old bundle variables were set, `fallback_sum=1`,
  `jbr_command_frames=634`, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260526-095132/matrix.tsv`.
- Magic Jewel benchmark suite now has no-run `LIST_CASES=true` / `LIST_CASE_COUNT=true` helpers with unknown
  `CASES=...` validation. A short `CASES=commands DURATION_SECONDS=1 WARMUP_SECONDS=0` smoke passed with
  `fallback_sum=0`, one old/new CPU sample, and `jbr_command_frames=633`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260526-093404/suite.tsv`.
- Magic Jewel short benchmark image-cache subset passed through the exact-row filter: 2/2 passed,
  `fallback_sum=0`, one old/new CPU sample per row, and `jbr_command_frames=1079`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260526-093925/suite.tsv`.
- Magic Jewel full default benchmark suite passed after the invalid-refresh validation gates: 5/5 passed,
  `fallback_sum=0`, `jbr_command_frames=12205`, picture FPS `196.2`, and command FPS rows `202.1`, `136.8`,
  `141.9`, and `129.4`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260527-092418/suite.tsv`.
- Magic Jewel RuntimeEffect source-cache eviction rows now require descriptor-handle cache-hit markers in command and
  screenshot suites. The RuntimeEffect color-filter child row keeps descriptor define/use and source-cache-hit gates,
  but not a descriptor cache-hit gate because grouped replay can legitimately produce zero child effect-handle cache
  hits. Exact source-cache command rows passed 2/2 with `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, and `jbr_command_frames=3930`; descriptor lifecycle command validation passed 18/18 with
  `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=29228`; exact visual rows
  passed 2/2 with `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=2253`, and average
  `bad_pixel_ratio=0.04981`; the RuntimeEffect visual group passed 14/14 with `fallback_sum=2`,
  `jbr_picture_frames=0`, `jbr_command_frames=12263`, and average `bad_pixel_ratio=0.04879`, suites
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260526-224211/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260526-224342/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-222738/suite.tsv`,
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-225720/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=graphics-layer` passed as the current graphics-layer command replay
  checkpoint: 21/21 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=24769`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-101706/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=graphics-layer-clip-shadow-transform` passed as the paired visual
  checkpoint: 14/14 passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=14847`, average
  `bad_pixel_ratio=0.05296`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-103209/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=descriptor-lifecycle` passed as the current descriptor lifecycle
  command checkpoint: 18/18 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=33447`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260526-102113/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=descriptor-lifecycle` passed as the paired descriptor lifecycle
  visual checkpoint: 6/6 passed, `fallback_sum=1`, `jbr_picture_frames=0`, `jbr_command_frames=6153`, average
  `bad_pixel_ratio=0.05078`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-103610/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-composition-runtime` passed as the current shader composition
  and RuntimeEffect command checkpoint: 15/15 passed, `fallback_sum=0`, `unsupported_rows=2`,
  `jbr_picture_frames=2613`, `jbr_command_frames=23910`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260526-104459/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=runtime-effect` passed as the paired RuntimeEffect visual
  checkpoint: 14/14 passed, `fallback_sum=2`, `jbr_picture_frames=0`, `jbr_command_frames=12948`, average
  `bad_pixel_ratio=0.04879`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260526-105613/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=native-text-invalid` passed as the current native text/font-data
  parser guard checkpoint: 11/11 passed, `fallback_sum=11`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=981`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260526-110644/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=primitive-invalid` passed as the current primitive parser guard
  checkpoint: 13/13 passed, `fallback_sum=13`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260526-111457/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=path-invalid` passed as the current path/path-effect parser guard
  checkpoint: 22/22 passed, `fallback_sum=22`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260526-112440/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=effect-descriptor-invalid` passed as the current effect descriptor
  parser guard checkpoint: 28/28 passed, `fallback_sum=28`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260526-230853/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-descriptor-invalid` passed as the current shader descriptor
  parser guard checkpoint: 30/30 passed, `fallback_sum=30`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260526-232759/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=image-handles-invalid` passed as the current image handle/cache parser
  guard checkpoint: 27/27 passed, `fallback_sum=27`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=1162`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260526-234804/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=descriptor-handles-invalid` passed as the current descriptor handle
  lifetime/type guard checkpoint: 48/48 passed, `fallback_sum=48`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-012055/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=gradient-path-invalid` passed as the current gradient path parser
  guard checkpoint: 18/18 passed, `fallback_sum=18`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-003026/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=save-layer-invalid` passed as the current saveLayer parser guard
  checkpoint: 37/37 passed, `fallback_sum=37`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-000551/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=gradient-invalid` passed as the current gradient parser guard
  checkpoint: 60/60 passed, `fallback_sum=60`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-004244/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=runtime-effect-invalid` passed as the current RuntimeEffect
  parser/schema guard checkpoint: 62/62 passed, `fallback_sum=56`, `unsupported_rows=6`,
  `jbr_picture_frames=6919`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-015158/suite.tsv`.
- Magic Jewel command-probe quick-group coverage now spans every resolved default case: `LIST_UNGROUPED_CASES=true`
  returns no rows after adding supported surface/transform/UI, shader-rendering, shader-composition/RuntimeEffect,
  core-effects, graphics-layer-extras, and saveLayer/shader-fallback groups.
- Magic Jewel exact saveLayer/shader-fallback uncovered command-probe tail passed: 7/7 passed, `fallback_sum=0`,
  `unsupported_rows=5`, `jbr_picture_frames=4435`, `jbr_command_frames=1659`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-233413/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=graphics-layer-extras` passed: 14/14 passed, `fallback_sum=0`,
  `unsupported_rows=2`, `jbr_picture_frames=2032`, `jbr_command_frames=13005`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-095544/suite.tsv`.
- Magic Jewel exact core effects uncovered command-probe slice passed: 7/7 passed, `fallback_sum=0`,
  `unsupported_rows=2`, `jbr_picture_frames=1694`, `jbr_command_frames=5042`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-231448/suite.tsv`.
- Magic Jewel exact shader composition and RuntimeEffect uncovered command-probe slice passed: 15/15 passed,
  `fallback_sum=0`, `unsupported_rows=2`, `jbr_picture_frames=2005`, `jbr_command_frames=17627`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-230231/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-rendering` passed: 13/13 passed, `fallback_sum=0`,
  `unsupported_rows=8`, `jbr_picture_frames=8930`, `jbr_command_frames=7006`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-093249/suite.tsv`.
- Magic Jewel exact uncovered surface/transform/UI command-probe slice passed after adding ungrouped-case discovery:
  11/11 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=15296`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-224136/suite.tsv`.
- Magic Jewel full default command-probe sweep passed with screenshot assertions enabled after the macOS capture retry:
  487/487 passed, `fallback_sum=350`, `unsupported_rows=26`, `jbr_picture_frames=25539`,
  `jbr_command_frames=146052`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-163835/suite.tsv`.
- Magic Jewel compatibility matrix passed after the screenshot-enabled command-probe refresh: 57/57 passed,
  `fallback_sum=56`, `jbr_command_frames=421`, background-window mode true for all rows, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260525-220254/matrix.tsv`.
- Magic Jewel artifact matrix passed on current ABI 106 local artifacts: required rows 2/2 passed, optional
  old-artifact rows skipped because no old bundle variables were set, `fallback_sum=1`, `jbr_command_frames=606`,
  matrix `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260525-223152/matrix.tsv`.
- Magic Jewel full default screenshot parity passed after the focused visual refreshes and local macOS capture retry:
  106/106 passed, `fallback_sum=11`, `jbr_picture_frames=0`, `jbr_command_frames=90354`, average
  `bad_pixel_ratio=0.05158`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-152754/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the remaining RuntimeEffect visual surface: 9/9 passed,
  `fallback_sum=1`, `jbr_picture_frames=0`, `jbr_command_frames=7416`, average `bad_pixel_ratio=0.04895`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-152025/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the image/shader descriptor visual surface: 12/12 passed,
  `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=10079`, average `bad_pixel_ratio=0.05304`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-151131/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the core drawing visual surface: 10/10 passed, `fallback_sum=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=8713`, average `bad_pixel_ratio=0.05479`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-150344/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the gradient visual surface: 4/4 passed, `fallback_sum=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=4613`, average `bad_pixel_ratio=0.05099`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-145959/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the descriptor-backed color-filter visual surface: 6/6 passed,
  `fallback_sum=1`, `jbr_picture_frames=0`, `jbr_command_frames=6651`, average `bad_pixel_ratio=0.05071`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-145502/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the combined graphics-layer blend/filter/render-effect visual
  surface: 10/10 passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=11712`, average
  `bad_pixel_ratio=0.06161`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-144729/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the graphics-layer shadow/transform visual surface: 9/9 passed,
  `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=10016`, average `bad_pixel_ratio=0.05198`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-144011/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the graphics-layer filter/effect visual surface: 5/5 passed,
  `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=6450`, average `bad_pixel_ratio=0.05177`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-143551/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the graphics-layer base/clip/blend visual surface: 7/7 passed,
  `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=6852`, average `bad_pixel_ratio=0.05404`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-143023/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the native text/font visual lifecycle surface after the local
  capture retry refresh: 14/14 passed, `fallback_sum=3`, `jbr_picture_frames=0`, `jbr_command_frames=10056`,
  average `bad_pixel_ratio=0.04758`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-142013/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the stable descriptor lifecycle visual surface after the local
  capture retry refresh: 14/14 passed, `fallback_sum=5`, `jbr_picture_frames=0`, `jbr_command_frames=10401`,
  average `bad_pixel_ratio=0.04695`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-140922/suite.tsv`.
- Magic Jewel focused `parity-button-chrome` screenshot parity passed on current artifacts after the macOS capture
  helper gained the window-bounds retry: 1/1 passed, `fallback_sum=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=941`, `bad_pixel_ratio=0.05200`, header-button `bad_pixel_ratio=0.00381`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260525-140430/suite.tsv`.
- Magic Jewel artifact matrix passed on current ABI 106 local artifacts: required rows 2/2 passed, optional old-artifact
  rows skipped because no old bundle variables were set, `fallback_sum=1`, `jbr_command_frames=718`, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260525-135942/matrix.tsv`.
- Magic Jewel full default command-probe consolidation passed in command-marker-only mode after the focused group and
  compatibility refreshes: 487/487 passed, `fallback_sum=350`, `unsupported_rows=26`,
  `jbr_picture_frames=33702`, `jbr_command_frames=188677`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-083629/suite.tsv`.
- Magic Jewel compatibility matrix passed after the focused command-probe refreshes: 57/57 passed,
  `fallback_sum=56`, `jbr_command_frames=663`, background-window mode true for all rows, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260525-080634/matrix.tsv`.
- Magic Jewel `CASE_GROUPS=runtime-effect-invalid` passed as a focused RuntimeEffect parser/semantic guard refresh:
  62/62 passed, `fallback_sum=56`, `unsupported_rows=6`, `jbr_picture_frames=5684`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-072243/suite.tsv`.
  The six unsupported rows are the intentional shader/color-filter invalid uniform, child, and nested-child schema
  cases; the remaining malformed rows failed before replay with structured fallback markers.
- Magic Jewel supported command-replay refresh passed in command-marker-only mode while local macOS screenshot capture
  remains broken independently of replay: `smoke` 6/6 with `jbr_command_frames=7336`, `color-filters` 10/10 with
  `unsupported_rows=1`, `jbr_picture_frames=979`, and `jbr_command_frames=11118`, `descriptor-lifecycle` 18/18 with
  `jbr_command_frames=20144`, `native-text` 14/14 with `jbr_command_frames=15012`, and `graphics-layer` 21/21 with
  `jbr_command_frames=26150`. All supported rows in these runs had `fallback_sum=0`; the single unsupported row is the
  intentional raw blend color-filter sentinel. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-063438/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-063910/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-064533/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-065848/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-070741/suite.tsv`.
- Magic Jewel small invalid-group refresh passed: `stream-invalid` 8/8, `shader-ref-invalid` 3/3,
  `fill-rect-color-filter-invalid` 5/5, and `blend-mode-invalid` 2/2. All four runs had expected fallback sums, zero
  unsupported rows, zero JBR picture frames, and zero JBR command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-062055/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-062610/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-062808/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-063118/suite.tsv`.
- Magic Jewel `CASE_GROUPS=gradient-invalid` passed as the broader gradient parser refresh: 60/60 passed,
  `fallback_sum=60`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-054237/suite.tsv`.
- Magic Jewel `CASE_GROUPS=gradient-path-invalid` passed as a focused gradient path parser refresh: 18/18 passed,
  `fallback_sum=18`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-053116/suite.tsv`.
- Magic Jewel `CASE_GROUPS=path-invalid` passed as a focused path/path-effect parser refresh: 22/22 passed,
  `fallback_sum=22`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-051534/suite.tsv`.
- Magic Jewel `CASE_GROUPS=primitive-invalid` passed as a focused primitive parser refresh: 13/13 passed,
  `fallback_sum=13`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-050721/suite.tsv`.
- Magic Jewel `CASE_GROUPS=native-text-invalid` passed as a focused native text/font parser refresh: 11/11 passed,
  `fallback_sum=11`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=1223`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-045900/suite.tsv`.
- Magic Jewel `CASE_GROUPS=descriptor-handles-invalid` passed as a focused descriptor handle lifetime/family refresh:
  48/48 passed, `fallback_sum=48`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-042757/suite.tsv`.
- Magic Jewel `CASE_GROUPS=save-layer-invalid` passed as a focused saveLayer parser refresh: 37/37 passed,
  `fallback_sum=37`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260527-000551/suite.tsv`.
- Magic Jewel `CASE_GROUPS=shader-descriptor-invalid` passed as a focused shader descriptor parser refresh: 30/30
  passed, `fallback_sum=30`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-034415/suite.tsv`.
- Magic Jewel `CASE_GROUPS=effect-descriptor-invalid` passed as a focused effect descriptor parser refresh: 28/28
  passed, `fallback_sum=28`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-032537/suite.tsv`.
- Magic Jewel `CASE_GROUPS=image-handles-invalid` passed as a focused image handle/ref parser refresh after the full
  command-marker sweep: 27/27 passed, `fallback_sum=27`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=980`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260525-030742/suite.tsv`.
- Magic Jewel full default command-probe sweep passed in command-marker-only mode after local macOS screenshot capture
  began failing independently of command replay: 487/487 passed, `fallback_sum=350`, `unsupported_rows=26`,
  `jbr_picture_frames=24438`, `jbr_command_frames=141588`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-215756/suite.tsv`.
  The screenshot-enabled broad attempt was blocked at `commands-point-lines` by `screencapture` while command markers
  were healthy.
- Magic Jewel `CASE_GROUPS=native-text` passed as a supported native text/font replay refresh: 14/14 passed,
  `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=16173`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-205921/suite.tsv`.
- Magic Jewel exact native-text slices passed before consolidation: base text 5/5 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-204934/suite.tsv`
  with `jbr_command_frames=5082`, resize 4/4 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-205300/suite.tsv`
  with `jbr_command_frames=4294`, and forced-context 5/5 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-205555/suite.tsv`
  with `jbr_command_frames=6494`; all had `fallback_sum=0`, `unsupported_rows=0`, and
  `jbr_picture_frames=0`.
- Magic Jewel `CASE_GROUPS=graphics-layer` passed as a supported graphics-layer command replay refresh: 21/21
  passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=23622`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-203342/suite.tsv`.
- Magic Jewel exact graphics-layer slices passed before consolidation: base/clip/blend 7/7 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-201824/suite.tsv`
  with `jbr_command_frames=9525`, filters/effects 5/5 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-202325/suite.tsv`
  with `jbr_command_frames=6297`, and shadows/transforms 9/9 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-202706/suite.tsv`
  with `jbr_command_frames=10778`; all had `fallback_sum=0`, `unsupported_rows=0`, and
  `jbr_picture_frames=0`.
- Magic Jewel `CASE_GROUPS=descriptor-lifecycle` passed as a supported descriptor lifecycle/source-cache refresh:
  18/18 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=23330`,
  suite `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-200304/suite.tsv`.
- Magic Jewel exact descriptor lifecycle slices passed before consolidation: descriptor eviction/redefine 13/13 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-194922/suite.tsv`
  with `jbr_command_frames=18104`, and RuntimeEffect lifecycle/source-cache 5/5 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-195934/suite.tsv`
  with `jbr_command_frames=7343`; both had `fallback_sum=0`, `unsupported_rows=0`, and
  `jbr_picture_frames=0`.
- Magic Jewel `CASE_GROUPS=color-filters` passed as a supported color-filter command replay refresh: 10/10 passed,
  `fallback_sum=0`, `unsupported_rows=1`, `jbr_picture_frames=1165`, `jbr_command_frames=13465`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-194122/suite.tsv`.
  The single unsupported row was the intentional raw blend color-filter fallback; descriptor-backed color-filter rows
  stayed on command replay.
- Magic Jewel exact color-filter slices passed before consolidation: base color filters 6/6 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-193428/suite.tsv`
  with `unsupported_rows=1`, `jbr_picture_frames=999`, and `jbr_command_frames=7220`, and graphics-layer
  color filters 4/4 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-193838/suite.tsv`
  with `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=6264`.
- Magic Jewel `CASE_GROUPS=gradient-invalid` passed as the grouped gradient parser consolidation, including the
  previously grouped gradient-path rows: 60/60 passed, `fallback_sum=60`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-185624/suite.tsv`.
- Magic Jewel exact non-path gradient invalid slices passed before consolidation: stroke-width 6/6 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-182859/suite.tsv`,
  linear 12/12 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-183259/suite.tsv`,
  sweep 8/8 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-184050/suite.tsv`,
  and radial 16/16 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-184612/suite.tsv`;
  all had `fallback_sum` equal to row count, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`.
- Magic Jewel `CASE_GROUPS=gradient-path-invalid` passed as the grouped gradient-path parser consolidation: 18/18
  passed, `fallback_sum=18`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-181611/suite.tsv`.
- Magic Jewel exact gradient-path invalid slices passed before consolidation: linear 6/6 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-180353/suite.tsv`,
  radial 7/7 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-180753/suite.tsv`,
  and sweep 5/5 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-181247/suite.tsv`;
  all had `fallback_sum` equal to row count, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`.
- Magic Jewel `CASE_GROUPS=runtime-effect-invalid` passed as the grouped RuntimeEffect descriptor/parser
  consolidation after the exact focused slices: 62/62 passed, `fallback_sum=56`, `unsupported_rows=6`,
  `jbr_picture_frames=6711`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-171908/suite.tsv`.
  The six unsupported rows are the intentional schema/nested descriptor cases, which fall back through JBR picture
  replay rather than command-stream invalid fallback markers.
- Magic Jewel exact RuntimeEffect focused slices passed before consolidation: shader descriptor parser 25/25 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-163503/suite.tsv`,
  color-filter descriptor parser 25/25 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-165052/suite.tsv`,
  schema/nested picture fallback 6/6 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-170650/suite.tsv`,
  and compile/build/type fallback tail 6/6 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-171059/suite.tsv`.
- Magic Jewel `CASE_GROUPS=descriptor-handles-invalid` passed as the grouped descriptor-handle parser/lifecycle
  consolidation after Skiko commit `4e7b0a6ba`: 48/48 passed, `fallback_sum=48`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-160242/suite.tsv`.
- Magic Jewel exact descriptor-handle slices passed before consolidation: direct use/eviction 14/14 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-151010/suite.tsv`,
  child use-after-evict 10/10 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-151912/suite.tsv`,
  missing-child 9/9 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-152543/suite.tsv`,
  the fixed RuntimeEffect color-filter child path-effect wrong-type row 1/1 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-154401/suite.tsv`,
  and wrong-type 15/15 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-155203/suite.tsv`.
- Magic Jewel `CASE_GROUPS=shader-descriptor-invalid` passed as the grouped shader descriptor parser consolidation:
  30/30 passed, `fallback_sum=30`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-144958/suite.tsv`.
- Magic Jewel exact shader descriptor slices passed before consolidation: header/color/transformed/composite 9/9 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-142112/suite.tsv`,
  gradient 7/7 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-142702/suite.tsv`,
  image-shader 6/6 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-144018/suite.tsv`,
  and Perlin-noise 8/8 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-144440/suite.tsv`;
  all had `fallback_sum` equal to row count, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`.
- Magic Jewel `CASE_GROUPS=effect-descriptor-invalid` passed as the grouped effect descriptor parser consolidation:
  28/28 passed, `fallback_sum=28`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-140259/suite.tsv`.
- Magic Jewel exact effect path-effect descriptor invalid slice passed for corner/stamped/chain path-effect payload
  guards, completing all 28 `effect-descriptor-invalid` rows across exact slices: 12/12 passed, `fallback_sum=12`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-135419/suite.tsv`.
- Magic Jewel exact effect image-filter descriptor invalid slice passed for blur/offset payload guards:
  8/8 passed, `fallback_sum=8`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-134749/suite.tsv`.
- Magic Jewel exact effect-descriptor header/color-filter payload invalid slice passed: 8/8 passed,
  `fallback_sum=8`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-134121/suite.tsv`.
- Magic Jewel `CASE_GROUPS=save-layer-invalid` passed as the grouped saveLayer parser consolidation:
  37/37 passed, `fallback_sum=37`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-131559/suite.tsv`.
- Magic Jewel exact save-layer ref scalar invalid slice passed for color-filter-ref and blend-color-filter-ref
  scalar/blend guards, completing all 37 `save-layer-invalid` rows across exact slices: 7/7 passed,
  `fallback_sum=7`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-131028/suite.tsv`.
- Magic Jewel exact save-layer scalar/blend invalid slice passed for color-filter/blend/image-filter saveLayer
  width/height/alpha and blend-mode payload guards: 15/15 passed, `fallback_sum=15`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-125944/suite.tsv`.
- Magic Jewel exact save-layer record/length invalid slice passed for raw/color-filter/blend/blend-color-filter
  variants and descriptor-ref variants: 15/15 passed, `fallback_sum=15`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-124845/suite.tsv`.
- Magic Jewel `CASE_GROUPS=image-handles-invalid` passed as the grouped image handle/parser consolidation:
  27/27 passed, `fallback_sum=27`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=1102`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-123035/suite.tsv`.
- Magic Jewel exact image color-filter invalid slice passed for color-filter image handle/scalar guards and
  descriptor-backed color-filter-ref scalar guards: 13/13 passed, `fallback_sum=13`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-122124/suite.tsv`.
- Magic Jewel exact plain image-ref/use invalid slice passed for missing/evicted image handles and image-ref
  width/height/alpha/filter-quality scalar guards: 6/6 passed, `fallback_sum=6`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-121600/suite.tsv`.
- Magic Jewel exact image define/cache invalid slice passed for record flags, image dimensions, max bounds, and pixel
  count: 8/8 passed, `fallback_sum=8`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=1295`,
  suite `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-120938/suite.tsv`.
- Magic Jewel exact fill-rect scalar invalid refresh passed across `fill-rect-color-filter-invalid`,
  `shader-ref-invalid`, and `blend-mode-invalid`: 10/10 passed, `fallback_sum=10`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-120203/suite.tsv`.
- Magic Jewel `CASE_GROUPS=path-invalid` passed as a focused path/path-effect parser refresh: 22/22 passed,
  `fallback_sum=22`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-114647/suite.tsv`.
- Magic Jewel `CASE_GROUPS=native-text-invalid` passed as a focused native text/font-data parser refresh:
  11/11 passed, `fallback_sum=11`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=1837`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-113852/suite.tsv`.
- Magic Jewel `CASE_GROUPS=primitive-invalid` passed as a focused primitive parser refresh: 13/13 passed,
  `fallback_sum=13`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-112927/suite.tsv`.
- Magic Jewel exact RuntimeEffect schema/nested fallback consolidation passed: 6/6 passed,
  `fallback_sum=0`, `jbr_picture_frames=7665`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-112429/suite.tsv`.
- Magic Jewel exact RuntimeEffect nested-child fallback validation passed for the shader and color-filter rows,
  completing the focused six-row schema/nested parser-fallback cluster: 2/2 passed, `jbr_picture_frames=2770`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-112201/suite.tsv`.
- Magic Jewel exact RuntimeEffect child-schema fallback validation passed for the shader and color-filter rows:
  2/2 passed, `jbr_picture_frames=2562`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-111924/suite.tsv`.
- Magic Jewel exact RuntimeEffect uniform-schema fallback validation passed for the shader and color-filter rows:
  2/2 passed, `jbr_picture_frames=2223`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-111654/suite.tsv`.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct RuntimeEffect color-filter descriptor rows for malformed
  uniform-schema and child-schema metadata, mirroring the existing shader RuntimeEffect schema checks.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct non-path sweep-gradient rows for fill/stroke
  rect/round-rect color-count, stop-order, and stroke-width guards, completing direct coverage for the current
  `gradient-invalid` family with the linear/radial/path slices.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct non-path radial-gradient rows for fill/stroke
  rect/round-rect radius, tile-mode, color-count, stop-order, and stroke-width guards.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct non-path linear-gradient rows for fill/stroke
  rect/round-rect tile-mode, color-count, stop-order, and stroke-width guards.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct linear/radial/sweep gradient-path command rows covering
  the full `gradient-path-invalid` guard set.
- JBR parser-only `JBRSkiaApiTest` passed after extending direct shader-descriptor invalid coverage for record flags,
  color/color-filter payload counts, composite blend mode, and Perlin tile-width parser guards.
- JBR parser-only `JBRSkiaApiTest` passed after extending direct effect-descriptor invalid coverage for descriptor
  flags, lighting payload count, corner/stamped path-effect bounds, and stamped path-verb guards.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct saveLayer invalid-family rows for record flags,
  record lengths, dimensions, alpha, and blend-mode parser guards across raw/ref saveLayer variants.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct clip-path, draw-path, and draw-path path-effect-ref rows
  for clip op, paint/stroke, fill/path length, and path-verb parser guards.
- JBR parser-only `JBRSkiaApiTest` passed after aligning direct draw-shadow path validation with native replay for
  finite geometry, light radius, shadow flags, fill/path length, and path-verb parser guards.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct dashed stroke-path path-effect rows for interval-count,
  stroke metadata, phase, interval-value, fill-type, path-data-length, and path-verb parser guards.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct dashed stroke-round-rect path-effect rows for
  interval-count, bounds/radii, stroke metadata, phase, and interval-value parser guards.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct dashed stroke-rect path-effect rows for interval-count,
  width, and height parser guards.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct dashed stroke-line path-effect rows for interval-count,
  phase, and interval-value parser guards.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct draw-points and draw-vertices scalar rows matching the
  main `primitive-invalid` point/vertices guards.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct fill-rect shader-ref scalar rows matching
  `shader-ref-invalid`.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct fill-rect blend/color-filter scalar rows matching the
  small `blend-mode-invalid` and `fill-rect-color-filter-invalid` Magic Jewel groups.
- Magic Jewel no-run quick-loop plumbing now exposes the RuntimeEffect color-filter child path-effect wrong-type row in
  `descriptor-handles-invalid`; the group count is 48. Execution remains pending on Gradle wrapper-lock access.
- JBR parser-only `JBRSkiaApiTest` passed after adding a cleared-image-cache ref row for the direct image-cache
  tracking helper.
- JBR parser-only `JBRSkiaApiTest` passed after tightening RuntimeEffect color-filter child validation to require
  actual color-filter descriptors. A path-effect child row now pins the wrong-family rejection.
- JBR parser-only `JBRSkiaApiTest` passed after making Java2D replay explicitly reject non-color-filter descriptors for
  image-ref and fill-rect color-filter refs. A path-effect image-ref color-filter row now pins the direct contract.
- JBR parser-only `JBRSkiaApiTest` passed after aligning Java2D replay with parser-helper type checks for saveLayer
  color-filter descriptor refs. Path-effect descriptors are now rejected for both color-filter and blend/color-filter
  saveLayer refs.
- JBR parser-only `JBRSkiaApiTest` passed after tightening direct parser-helper image-cache tracking for image refs and
  image-shader refs. The new direct rows reject undefined, evicted, and dimension-mismatched image cache keys before
  replay.
- JBR parser-only `JBRSkiaApiTest` passed after tightening the direct parser helper for
  `COMMAND_DRAW_PATH_PATH_EFFECT_REF` descriptor handles. The new checks reject undefined, evicted, and wrong-type
  path-effect handles with a single-source patched `JBRSkiaService` overlay; the broader Magic Jewel
  `CASE_GROUPS=descriptor-handles-invalid` harness run remains pending until Gradle can access the user-home wrapper
  lock again.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct descriptor-handle invalid checks for shader/color-filter
  evict record flags, transformed/composite/shader-color-filter and RuntimeEffect shader/color-filter child use-after-evict,
  blur image-filter child missing/evicted/wrong-type, undefined saveLayer image-filter handles, and evicted saveLayer
  color/filter descriptor handles. The Magic Jewel
  `CASE_GROUPS=descriptor-handles-invalid` harness run remains pending until Gradle can access the user-home wrapper
  lock again.
- Magic Jewel `CASE_GROUPS=image-handles-invalid` passed as the image handle/ref parser fallback refresh: 27/27,
  `fallback_sum=27`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=806`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-175721/suite.tsv`.
- Magic Jewel `CASE_GROUPS=color-filters` passed as a focused supported color-filter/graphics-layer replay refresh:
  10/10, `fallback_sum=0`, `unsupported_rows=1`, `picture_frames=873`, and `command_frames=12561`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-174955/suite.tsv`.
- Magic Jewel `CASE_GROUPS=path-invalid` passed as the path and path-effect parser fallback refresh: 22/22,
  `fallback_sum=22`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-173511/suite.tsv`.
- Magic Jewel `CASE_GROUPS=primitive-invalid` passed as the primitive paint/draw parser fallback refresh: 13/13,
  `fallback_sum=13`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-172558/suite.tsv`.
- Magic Jewel `CASE_GROUPS=native-text-invalid` passed as the native text/font parser fallback refresh: 11/11,
  `fallback_sum=11`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=1500`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-171910/suite.tsv`.
- Magic Jewel focused screenshot parity passed for the stable descriptor lifecycle visual surface: 14/14,
  `fallback_sum=5`, `jbr_picture_frames=0`, and `jbr_command_frames=9992`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260523-170917/suite.tsv`.
  The subset covered descriptor eviction, shader resize/forced-context redefine rows, stable RuntimeEffect
  color-filter lifecycle rows, and RuntimeEffect source-cache eviction.
- Magic Jewel compatibility matrix passed after the local artifact rebuild and latest default command-probe
  consolidation: 57/57, `fallback_sum=56`, `command_frames=426`, all rows background-windowed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260523-163821/matrix.tsv`.
- Magic Jewel periodic default command-probe consolidation passed after the latest focused quick refreshes, using split
  resume roots and an exact repaired rerun after a transient local `public-api-missing` artifact state: 486/486,
  `fallback_sum=349`, `unsupported_rows=26`, `picture_frames=34318`, and `command_frames=200336`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-063205/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-153433/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-153519/suite.tsv`.
- Magic Jewel now exposes `CASE_GROUPS=descriptor-lifecycle` for focused stable descriptor lifecycle validation. The
  group passed 18/18 with `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=34840`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-050014/suite.tsv`.
- Magic Jewel `CASE_GROUPS=native-text` passed as a focused native font/text lifecycle refresh: 14/14,
  `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=27551`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-051647/suite.tsv`.
- Magic Jewel `CASE_GROUPS=graphics-layer` passed as a focused graphics-layer transform/effect refresh: 21/21,
  `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=42087`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-052656/suite.tsv`.
- Magic Jewel `CASE_GROUPS=gradient-path-invalid` passed as a focused gradient path parser/fallback refresh: 18/18,
  `fallback_sum=18`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-054125/suite.tsv`.
- Magic Jewel `CASE_GROUPS=gradient-invalid` passed as the broader gradient parser/fallback refresh: 60/60,
  `fallback_sum=60`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260523-055336/suite.tsv`.
- Magic Jewel periodic default command-probe consolidation passed after the focused parser guard refreshes, using an
  exact `commands-live-animation` rerun plus a resumed default tail after macOS window capture failed independently of
  command replay on the first broad attempt: 486/486, `fallback_sum=349`, `unsupported_rows=26`,
  `picture_frames=33767`, and `command_frames=222490`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-234434/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-234537/suite.tsv`.
- Magic Jewel `CASE_GROUPS=runtime-effect-invalid` passed as a focused RuntimeEffect parser/semantic guard refresh:
  62/62, `fallback_sum=56`, `unsupported_rows=6`, `picture_frames=7689`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-225911/suite.tsv`.
  The six unsupported rows are the intentional invalid uniform/child/nested-child schema fallbacks for shader and
  color-filter RuntimeEffect descriptors.
- Magic Jewel `CASE_GROUPS=effect-descriptor-invalid` passed as a focused effect descriptor parser guard refresh:
  28/28, `fallback_sum=28`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-224042/suite.tsv`.
- Magic Jewel `CASE_GROUPS=shader-descriptor-invalid` passed as a focused shader descriptor parser guard refresh:
  30/30, `fallback_sum=30`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-222036/suite.tsv`.
- Magic Jewel `CASE_GROUPS=save-layer-invalid` passed as a focused saveLayer parser guard refresh: 37/37,
  `fallback_sum=37`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-215534/suite.tsv`.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct saveLayer scalar guards for supported non-ref
  color-filter, blend-mode, and blend/color-filter bounds.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct RuntimeEffect color-filter descriptor guards for
  hash/source code and uniform/child/named-count bounds.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct RuntimeEffect shader descriptor guards for SKSL/source
  code and uniform/child/named-count bounds.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct effect descriptor payload guards for blur, offset,
  corner/stamped path-effect, and chain path-effect descriptor bounds.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct shader descriptor payload guards for gradient tile/stop
  ordering, radial radius, sweep color counts, and image shader dimension/tile bounds.
- JBR parser-only `JBRSkiaApiTest` passed after adding direct Perlin-noise shader descriptor guards for base-frequency
  Y, zero octaves, and tile-height bounds. The test compiled against the patched `/tmp/jbr-skia-run/desktop` classes
  and local `JBRApi` stub, then ran headlessly with the rebuilt native bridge.
- Magic Jewel periodic default command-probe consolidation passed across the full default order using split resume
  roots: 486/486, `fallback_sum=349`, `unsupported_rows=26`, `picture_frames=31265`, and
  `command_frames=189208`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-143223/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-160910/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-192154/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-200157/suite.tsv`.
  The run recovered from a transient local `public-api-missing` artifact state by rebuilding JBR Skia artifacts; the
  final tail used command-marker-only validation after macOS `screencapture` began failing to create images from the
  matched Magic Jewel window despite healthy command replay.
- Magic Jewel `CASES=commands-gradient-stroke` passed after the scoped screenshot-gate relaxation for that row:
  `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=2710`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-191608/suite.tsv`.
  Magic Jewel commit `41f08e5` pushed the harness change.
- Magic Jewel `CASE_GROUPS=runtime-effect-invalid` passed as the focused RuntimeEffect parser/semantic guard
  checkpoint: 62/62, `fallback_sum=56`, `unsupported_rows=6`, `picture_frames=6338`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-134345/suite.tsv`.
  The six unsupported rows are the intentional invalid uniform/child/nested-child schema fallbacks for shader and
  color-filter RuntimeEffect descriptors; the remaining rows rejected through structured command fallback.
- Magic Jewel `CASE_GROUPS=gradient-invalid` passed as the focused gradient parser guard checkpoint: 60/60,
  `fallback_sum=60`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-130045/suite.tsv`.
  This covers linear/radial/sweep stroke width, tile mode, color count, stop order, path-gradient, and radial radius
  malformed-stream guards.
- Magic Jewel `CASE_GROUPS=descriptor-handles-invalid` passed as the focused descriptor handle lifecycle/type guard
  checkpoint: 47/47, `fallback_sum=47`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-122554/suite.tsv`.
  This covers missing handles, use-after-evict, eviction record flags, child missing/use-after-evict, and wrong-type
  guards across shader, color-filter, image-filter, path-effect, and RuntimeEffect descriptor families.
- Magic Jewel `CASE_GROUPS=save-layer-invalid` passed as the focused saveLayer parser guard checkpoint: 37/37,
  `fallback_sum=37`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-115938/suite.tsv`.
  This covers alpha, record flags, record lengths, width/height, blend modes, and descriptor-backed color/image-filter
  saveLayer variants.
- Magic Jewel `CASE_GROUPS=shader-descriptor-invalid` passed as the focused shader descriptor parser guard checkpoint:
  30/30, `fallback_sum=30`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-113757/suite.tsv`.
  This covers shader descriptor headers, color/filter payloads, transformed/composite shader guards, gradient/image
  shader descriptor bounds, and Perlin noise descriptor guards.
- Magic Jewel `CASE_GROUPS=effect-descriptor-invalid` passed as the focused effect descriptor parser guard checkpoint:
  28/28, `fallback_sum=28`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-111754/suite.tsv`.
  This covers descriptor header guards, color/image filter payloads, and corner/stamped/chain path-effect descriptor
  parser guards.
- Magic Jewel `CASE_GROUPS=image-handles-invalid` passed as the focused image handle/parser guard checkpoint: 27/27,
  `fallback_sum=27`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=1082`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-105821/suite.tsv`.
  The command frames come from the recoverable image-cache-clear record-flags row; the other malformed rows rejected
  before replay.
- Magic Jewel `CASE_GROUPS=graphics-layer` passed as the focused graphics-layer command replay checkpoint: 21/21,
  `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=30306`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-104254/suite.tsv`.
  This covers layer clips, blend/color filters, render effects, shadows, 3D rotations, scale/translate, camera, and
  pivot variants.
- Magic Jewel `CASE_GROUPS=gradient-path-invalid` passed as the focused gradient path parser guard checkpoint: 18/18,
  `fallback_sum=18`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-102856/suite.tsv`.
- Magic Jewel `CASE_GROUPS=native-text` passed as the focused native text/font-data command replay checkpoint: 14/14,
  `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=15912`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-101728/suite.tsv`.
  This covers custom, generic, loaded-font-data, resource, system, resize, and forced-context native text rows.
- Magic Jewel `CASE_GROUPS=color-filters` passed as the focused color-filter command replay checkpoint: 10/10,
  `fallback_sum=0`, `unsupported_rows=1`, `picture_frames=1197`, and `command_frames=10861`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-100853/suite.tsv`.
  The single unsupported row is the intentional raw blend color-filter fallback; supported descriptor and
  graphics-layer color-filter rows replayed through JBR commands.
- Magic Jewel `CASE_GROUPS=path-invalid` passed as the focused path/path-effect parser guard checkpoint: 22/22,
  `fallback_sum=22`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-094905/suite.tsv`.
- Magic Jewel `CASE_GROUPS=primitive-invalid` passed as the focused primitive command parser guard checkpoint: 13/13,
  `fallback_sum=13`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-091344/suite.tsv`.
  The bounded default-order range from `commands-core-primitives` through `commands-point-lines` also passed as the
  adjacent primitive/image/path ordering checkpoint: 38/38, `fallback_sum=36`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=3398`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-092240/suite.tsv`.
- Magic Jewel `CASE_GROUPS=native-text-invalid` passed as the focused native text/font-data parser guard checkpoint:
  11/11, `fallback_sum=11`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=728`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-090411/suite.tsv`.
  The font-data record-flags row is recoverable after the one-shot invalid definition, accounting for the JBR command
  frames.
- Magic Jewel `CASE_GROUPS=smoke` passed as the quick happy-path command replay check: 6/6, `fallback_sum=0`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=6891`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-003530/suite.tsv`.
- Magic Jewel `CASE_GROUPS=stream-invalid` passed as the quick command-stream parser guard check: 8/8,
  `fallback_sum=8`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-004208/suite.tsv`.
- Magic Jewel quick-loop discovery is now complete for the command probe suite: `LIST_CASE_GROUPS=true` exposes every
  implemented quick group, including shader-ref, fill-rect color-filter, and blend-mode invalid groups that were
  previously usable but hidden from the group listing. A no-run membership scan confirmed every listed quick-group row
  is represented in the default case order. The exposed shader-ref, fill-rect color-filter, and blend-mode invalid
  groups also passed as a focused 10-row validation with `fallback_sum=10`, zero unsupported rows, and zero JBR frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-002228/suite.tsv`.
  `LIST_CASE_GROUP_COUNTS=true` now prints quick-group sizes without launching validation.
- Magic Jewel default command-probe ordering now also includes the corner/stamped/chain path-effect descriptor invalid
  rows from `CASE_GROUPS=effect-descriptor-invalid`. `LIST_CASES=true` showed no remaining missing rows across the
  checked invalid quick groups (`stream`, `primitive`, `path`, `effect-descriptor`, `shader-descriptor`,
  `runtime-effect`, `descriptor-handles`, `image-handles`, `save-layer`, `gradient`, `gradient-path`, and
  `native-text`). The bounded default-order effect-descriptor range passed 15/15 with `fallback_sum=15`, zero
  unsupported rows, and zero JBR frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-000243/suite.tsv`.
- Magic Jewel default command-probe ordering now includes the existing blur image-filter child live sentinels for
  use-after-evict, missing-child, and wrong-effect-type fallback, matching the offset image-filter child coverage that
  was already in the default path. The exact three-row run passed with `fallback_sum=3`, zero unsupported rows, and
  zero JBR frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-230108/suite.tsv`.
  The `CASE_GROUPS=descriptor-handles-invalid` group passed 47/47 with `fallback_sum=47`, zero unsupported rows, and
  zero JBR frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-230330/suite.tsv`.
  Three bounded default-order ranges also passed, proving the new blur rows are inserted in the default use-after-evict,
  missing-child, and wrong-type neighborhoods:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-233519/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-233729/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-233942/suite.tsv`.
  Magic Jewel also gained `LIST_CASES=true` no-run case listing, which confirmed the descriptor-handle invalid group is
  no longer missing rows from the default case list. The expanded default use-after-evict neighborhood passed 19/19
  with `fallback_sum=19`, zero unsupported rows, and zero JBR frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-234632/suite.tsv`.
- Periodic full default command-probe consolidation passed as a split sweep after the stroke-round-rect dash stroke
  metadata slice. The first broad run hit a transient runtime/output miss at
  `commands-invalid-blur-with-input-image-filter-descriptor-tile-mode-fallback`, so the durable validation was split
  into a prefix through `commands-invalid-blur-image-filter-descriptor-tile-mode-fallback` and a resumed tail from the
  missed row forward. Combined aggregate: 462/462 passed, `fallback_sum=325`, `unsupported_rows=26`,
  `picture_frames=24648`, and `command_frames=146301`. Prefix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-202252/suite.tsv`.
  Tail:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-172927/suite.tsv`.
- Stroke-round-rect dash path-effect stroke metadata validation now covers op 60 stroke width/cap/join/miter guards.
  Skiko can rewrite `COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT` stroke width to `0`, cap/join to `3`, or miter to
  `-1`, matching JBR's `isValidStrokeMetadata` checks, and Magic Jewel requires typed stroke metadata corruption
  markers before accepting `command-stream-invalid` fallback. The exact four-row run passed with aggregate 4/4,
  `fallback_sum=4`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-143055/suite.tsv`.
  The scoped `CASE_GROUPS=path-invalid` quick group now covers 22 malformed path rows and passed with
  `fallback_sum=22`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-143333/suite.tsv`.
  The narrower default-order path tail from `commands-invalid-clip-path-verb-fallback` through
  `commands-point-lines` also passed; aggregate 23/23 passed, `fallback_sum=22`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=1972`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-144716/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Stroke-round-rect dash path-effect phase/interval validation now covers op 60 dash scalar guards. Skiko can rewrite
  `COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT` phase to `-1` or the first dash interval to `0`, matching JBR's
  `phase >= 0` and positive interval checks, and Magic Jewel requires typed phase/interval corruption markers before
  accepting `command-stream-invalid` fallback. The exact two-row run passed with aggregate 2/2, `fallback_sum=2`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-135947/suite.tsv`.
  The scoped `CASE_GROUPS=path-invalid` quick group now covers 18 malformed path rows and passed with
  `fallback_sum=18`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-140124/suite.tsv`.
  The narrower default-order path tail from `commands-invalid-clip-path-verb-fallback` through
  `commands-point-lines` also passed; aggregate 19/19 passed, `fallback_sum=18`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=2308`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-141245/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Stroke-round-rect dash path-effect bounds/radii validation now covers op 60 right/bottom ordering and non-negative
  radius guards. Skiko can rewrite `COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT` right or bottom to `-1`, or radius X/Y
  to `-1`, matching JBR's live parser checks, and Magic Jewel requires typed right/bottom/radius corruption markers
  before accepting `command-stream-invalid` fallback. The exact four-row run passed with aggregate 4/4,
  `fallback_sum=4`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-133047/suite.tsv`.
  The scoped `CASE_GROUPS=path-invalid` quick group now covers 16 malformed path rows and passed with
  `fallback_sum=16`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-133333/suite.tsv`.
  The narrower default-order path tail from `commands-invalid-clip-path-verb-fallback` through
  `commands-point-lines` also passed; aggregate 17/17 passed, `fallback_sum=16`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=1026`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-134426/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Stroke-rect dash path-effect dimension validation now covers op 59 width/height lower bounds. Skiko can rewrite the
  recorded `COMMAND_STROKE_RECT_DASH_PATH_EFFECT` width or height to `-1`, matching JBR's live parser guards, and
  Magic Jewel requires typed width/height corruption markers before accepting `command-stream-invalid` fallback. The
  exact two-row run passed with aggregate 2/2, `fallback_sum=2`, `unsupported_rows=0`, `picture_frames=0`, and
  `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-125148/suite.tsv`.
  The scoped `CASE_GROUPS=path-invalid` quick group now covers 12 malformed path rows and passed with
  `fallback_sum=12`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-125332/suite.tsv`.
  After a wider primitive/path attempt hit a Gradle/app startup measurement miss at an existing clip-path row, the
  narrower default-order path tail from `commands-invalid-clip-path-verb-fallback` through `commands-point-lines`
  passed; aggregate 13/13 passed, `fallback_sum=12`, `unsupported_rows=0`, `picture_frames=0`, and
  `command_frames=937`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-131533/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Stroke-round-rect dash path-effect interval-count validation now covers op 60
  `COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT`. Skiko can rewrite the recorded dash interval count to `1`, matching
  JBR's round-rect dash path-effect parser guard at `argsStart + 12`, and Magic Jewel requires the typed
  `SKIKO_JBR_INTEROP_STROKE_ROUND_RECT_DASH_PATH_EFFECT_INTERVAL_COUNT_CORRUPTED` marker before accepting
  `command-stream-invalid` fallback. The exact one-row run passed with aggregate 1/1, `fallback_sum=1`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-122059/suite.tsv`.
  The scoped `CASE_GROUPS=path-invalid` quick group now covers 10 malformed path rows and passed with
  `fallback_sum=10`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-122157/suite.tsv`.
  The bounded default-order primitive/path slice from `commands-core-primitives` through `commands-point-lines` also
  passed; aggregate 26/26 passed, `fallback_sum=24`, `unsupported_rows=0`, `picture_frames=0`, and
  `command_frames=3888`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-122829/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Stroke-rect dash path-effect interval-count validation now extends the live dash parser sentinels to op 59
  `COMMAND_STROKE_RECT_DASH_PATH_EFFECT`. Skiko can rewrite the recorded dash interval count to `1`, matching JBR's
  shared line/rect dash path-effect parser guard, and Magic Jewel requires the typed
  `SKIKO_JBR_INTEROP_STROKE_RECT_DASH_PATH_EFFECT_INTERVAL_COUNT_CORRUPTED` marker before accepting
  `command-stream-invalid` fallback. The exact one-row run passed with aggregate 1/1, `fallback_sum=1`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-115513/suite.tsv`.
  The scoped `CASE_GROUPS=path-invalid` quick group now covers 9 malformed path rows and passed with
  `fallback_sum=9`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-115607/suite.tsv`.
  The bounded default-order primitive/path slice from `commands-core-primitives` through `commands-point-lines` also
  passed; aggregate 25/25 passed, `fallback_sum=23`, `unsupported_rows=0`, `picture_frames=0`, and
  `command_frames=3178`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-120233/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Stroke-line dash path-effect interval-count validation now has a live sentinel for op 43
  `COMMAND_STROKE_LINE_DASH_PATH_EFFECT`. Skiko can rewrite the recorded dash interval count from an even value to
  `1`, matching JBR's line dash path-effect parser guard, and Magic Jewel requires the typed
  `SKIKO_JBR_INTEROP_STROKE_LINE_DASH_PATH_EFFECT_INTERVAL_COUNT_CORRUPTED` marker before accepting
  `command-stream-invalid` fallback. The exact one-row run passed with aggregate 1/1, `fallback_sum=1`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-112712/suite.tsv`.
  The scoped `CASE_GROUPS=path-invalid` quick group now covers 8 malformed path rows and passed with
  `fallback_sum=8`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-112800/suite.tsv`.
  The bounded default-order primitive/path slice from `commands-core-primitives` through `commands-point-lines` also
  passed; aggregate 24/24 passed, `fallback_sum=22`, `unsupported_rows=0`, `picture_frames=0`, and
  `command_frames=3173`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-113311/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- SaveLayer variant record-flags parser validation now has live sentinels for op 44, 50, 51, 52, 54, and 55:
  color-filter, blend-mode, blend/color-filter, color-filter-ref, blend/color-filter-ref, and image-filter-ref
  saveLayer records. Skiko can rewrite each variant's record-flags word to the antialias bit, and JBR rejects each
  malformed stream before replay. The exact six-row run passed with aggregate 6/6, `fallback_sum=6`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-043049/suite.tsv`.
  The scoped `CASE_GROUPS=save-layer-invalid` quick group now covers 21 malformed saveLayer rows and passed with
  `fallback_sum=21`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-040032/suite.tsv`.
  The bounded default-order range from `commands-save-layer-filter` through
  `commands-save-layer-raw-color-filter-fallback` also passed; aggregate 24/24 passed, `fallback_sum=21`,
  `unsupported_rows=1`, `picture_frames=1772`, and `command_frames=5948`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-041438/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- SaveLayer color-filter scalar validation now covers op 44 width/height lower bounds. Skiko can rewrite the
  `COMMAND_SAVE_LAYER_COLOR_FILTER` width or height to `-1`, matching JBR's live parser guards. The exact two-row run
  passed with aggregate 2/2, `fallback_sum=2`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-044744/suite.tsv`.
  The expanded `CASE_GROUPS=save-layer-invalid` quick group now covers 23 malformed saveLayer rows and passed with
  `fallback_sum=23`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-044919/suite.tsv`.
  The bounded default-order saveLayer range also passed; aggregate 26/26 passed, `fallback_sum=23`,
  `unsupported_rows=1`, `picture_frames=1775`, and `command_frames=5587`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-050428/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- SaveLayer blend-mode scalar validation now mirrors that width/height coverage for op 50
  `COMMAND_SAVE_LAYER_BLEND_MODE`. The exact two-row run passed with aggregate 2/2, `fallback_sum=2`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-052535/suite.tsv`.
  The expanded `CASE_GROUPS=save-layer-invalid` quick group now covers 25 malformed saveLayer rows and passed with
  `fallback_sum=25`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-052706/suite.tsv`.
  The bounded default-order saveLayer range also passed; aggregate 28/28 passed, `fallback_sum=25`,
  `unsupported_rows=1`, `picture_frames=1736`, and `command_frames=4996`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-054336/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- SaveLayer blend/color-filter scalar validation now mirrors the op 44 and op 50 width/height coverage for op 51
  `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER`. Skiko can rewrite the recorded width or height to `-1`, and JBR rejects the
  malformed stream before replay. The exact two-row run passed with aggregate 2/2, `fallback_sum=2`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-060758/suite.tsv`.
  The expanded `CASE_GROUPS=save-layer-invalid` quick group now covers 27 malformed saveLayer rows and passed with
  `fallback_sum=27`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-060928/suite.tsv`.
  The bounded default-order saveLayer range also passed; aggregate 30/30 passed, `fallback_sum=27`,
  `unsupported_rows=1`, `picture_frames=1481`, and `command_frames=5326`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-062712/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- SaveLayer alpha scalar validation now covers the remaining supported non-ref variants: op 44 color-filter, op 50
  blend-mode, and op 51 blend/color-filter. Skiko can rewrite each variant's `alpha1000` to `1001`, and JBR rejects
  the malformed stream before replay. The exact three-row run passed with aggregate 3/3, `fallback_sum=3`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-065320/suite.tsv`.
  The expanded `CASE_GROUPS=save-layer-invalid` quick group now covers 30 malformed saveLayer rows and passed with
  `fallback_sum=30`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-065533/suite.tsv`.
  The bounded default-order saveLayer range also passed; aggregate 33/33 passed, `fallback_sum=30`,
  `unsupported_rows=1`, `picture_frames=1438`, and `command_frames=5420`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-071506/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- SaveLayer record-length parser validation now covers op 44 color-filter, op 50 blend-mode, and op 51
  blend/color-filter. Skiko shortens each variant's record length by one int, and JBR rejects the malformed stream
  before replay. The exact three-row run passed with aggregate 3/3, `fallback_sum=3`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-074334/suite.tsv`.
  The expanded `CASE_GROUPS=save-layer-invalid` quick group now covers 33 malformed saveLayer rows and passed with
  `fallback_sum=33`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-074550/suite.tsv`.
  The bounded default-order saveLayer range also passed; aggregate 36/36 passed, `fallback_sum=33`,
  `unsupported_rows=1`, `picture_frames=1719`, and `command_frames=4995`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-080718/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Descriptor-backed saveLayer record-length parser validation now covers op 52 color-filter-ref, op 54
  blend/color-filter-ref, and op 55 image-filter-ref. Skiko shortens each target record by one int after descriptor
  recording, matching JBR's exact `offset == recordEnd` guards for the handle-backed variants. The exact three-row run
  passed with aggregate 3/3, `fallback_sum=3`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-083632/suite.tsv`.
  The expanded `CASE_GROUPS=save-layer-invalid` quick group now covers 36 malformed saveLayer rows and passed with
  `fallback_sum=36`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-083848/suite.tsv`.
  The bounded default-order saveLayer range also passed; aggregate 39/39 passed, `fallback_sum=36`,
  `unsupported_rows=1`, `picture_frames=924`, and `command_frames=3483`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-090249/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Plain saveLayer record-length parser validation now covers op 13 `COMMAND_SAVE_LAYER`. Skiko shortens the recorded
  plain saveLayer record by one int, matching JBR's exact `offset + 5 == recordEnd` guard. The exact one-row run
  passed with aggregate 1/1, `fallback_sum=1`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-093321/suite.tsv`.
  The expanded `CASE_GROUPS=save-layer-invalid` quick group now covers 37 malformed saveLayer rows and passed with
  `fallback_sum=37`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-093418/suite.tsv`.
  The bounded default-order saveLayer range also passed; aggregate 40/40 passed, `fallback_sum=37`,
  `unsupported_rows=1`, `picture_frames=985`, and `command_frames=1723`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-095826/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Shader descriptor version validation now has an explicit live row name matching JBR's parser fixture for unsupported
  `COMMAND_DEFINE_SHADER_DESCRIPTOR` versions. The row uses the existing Skiko shader-version corruption hook and
  replaces the older generic descriptor-version row in the shader descriptor quick/default paths. The exact one-row
  run passed with aggregate 1/1, `fallback_sum=1`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-102949/suite.tsv`.
  The scoped `CASE_GROUPS=shader-descriptor-invalid` quick group passed 30/30 with `fallback_sum=30`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-103042/suite.tsv`.
  The bounded default-order shader descriptor slice also passed; aggregate 31/31 passed, `fallback_sum=31`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-110020/suite.tsv`.
- Image definition parser validation now has live width/height lower- and upper-bound sentinels for
  `COMMAND_DEFINE_IMAGE_ARGB`. Skiko can rewrite the first emitted image define width or height to `0` or `4097`,
  matching JBR's `imageWidth > 0`, `imageHeight > 0`, and `<= 4096` parser guards. The exact four-row run passed with
  aggregate 4/4, `fallback_sum=4`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-030459/suite.tsv`.
  The scoped `CASE_GROUPS=image-handles-invalid` quick group now covers 27 malformed image rows and passed with
  `fallback_sum=27`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=1625`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-030826/suite.tsv`.
  The bounded default-order range from `commands-core-primitives` through `commands-point-lines` also passed; aggregate
  23/23 passed, `fallback_sum=21`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=7321`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-032618/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Current-artifact validation rechecked the already-live unknown effect descriptor type sentinel. The exact
  `commands-invalid-effect-descriptor-type-fallback` row rewrites the first recorded `COMMAND_DEFINE_EFFECT_DESCRIPTOR`
  descriptor type to `Int.MAX_VALUE`, matching JBR's parser-only unknown-type fixture, and passed with one expected
  `command-stream-invalid` fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-023959/suite.tsv`.
  The scoped `CASE_GROUPS=effect-descriptor-invalid` area group passed with 28/28 rows, `fallback_sum=28`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-024048/suite.tsv`.
- Fill-rect color-filter-ref parser validation now has live width/height sentinels for op 47
  `COMMAND_FILL_RECT_COLOR_FILTER_REF`. Skiko can record the handle-backed fill-rect color-filter path, rewrite width
  or height to `-1`, and force JBR's command-stream parser to reject the frame before replay. The exact two-row run
  passed with two expected `command-stream-invalid` fallbacks and no unsupported rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-022809/suite.tsv`.
  The scoped `CASE_GROUPS=fill-rect-color-filter-invalid` quick group now covers five rows; aggregate 5/5 passed,
  `fallback_sum=5`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-022940/suite.tsv`.
  The bounded default-order range from `commands-raw-blend-color-filter-fallback` through `commands-color-filter`
  also passed; aggregate 7/7 passed, `fallback_sum=5`, `unsupported_rows=1`, `picture_frames=1792`, and
  `command_frames=3025`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-023307/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Full default command-probe sweep passed after the draw-vertices parser sentinel series and the effect descriptor
  version/payload-count/record-length rows landed. The run used command-semantic validation with screenshot
  assertions disabled and included the ten-row `primitive-invalid` draw-points/draw-vertices quick group plus the
  current descriptor fallback rows. Aggregate: 416/416 passed, `fallback_sum=279`, `unsupported_rows=26`,
  `picture_frames=30096`, and `command_frames=201780`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-203639/suite.tsv`.
- Full default command-probe sweep passed after adding the draw-points record-length sentinel. The run used
  command-semantic validation with screenshot assertions disabled, included both draw-points invalid rows in the
  default set, and passed all rows. Aggregate: 411/411 passed, `fallback_sum=274`, `unsupported_rows=27`,
  `picture_frames=25809`, and `command_frames=151351`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-144422/suite.tsv`.
- Split broad command-probe consolidation completed after the draw-points point-count and report-validation fixes.
  Instead of re-running already-green prefixes, the default suite was validated in resumed segments:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-093730/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-102727/suite.tsv`,
  and `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-123615/suite.tsv`.
  Combined aggregate: 410/410 passed, `fallback_sum=273`, `unsupported_rows=27`, `picture_frames=27097`, and
  `command_frames=147205`. Screenshot assertions were disabled for the final gradient/saveLayer/graphics tail to keep
  the broad run command-semantic-only after a macOS capture flake.
- Draw-points parser validation now has live point-count and record-length sentinels. Skiko can rewrite
  `COMMAND_DRAW_POINTS` point count to zero or shorten the recorded draw-points record length after CMP records the
  point-dots scene, and Magic Jewel exposes both rows in the `primitive-invalid` quick group. The exact record-length
  row passed with one expected `command-stream-invalid` fallback and no unsupported rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-143828/suite.tsv`.
  The previous point-count row also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-142958/suite.tsv`.
  The scoped `CASE_GROUPS=primitive-invalid` quick group now covers stroke cap, transform record flags, clip
  operation, draw-points point count, and draw-points record length; aggregate 5/5 passed, `fallback_sum=5`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-143915/suite.tsv`.
  The earlier 4-row primitive quick group also passed, `fallback_sum=4`, `unsupported_rows=0`,
  `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-093531/suite.tsv`.
  Skiko `publishAwtPublicationToMavenLocal publishAwtRuntimeElementsPublicationToMavenLocal
  publishKotlinMultiplatformPublicationToMavenLocal` and `JbrSkiaInteropTest` also passed. The full
  `publishToMavenLocal` task hit an external Skia macOS arm64 release 404 before the narrower publication succeeded.
- Draw-points parser validation now also has a live upper-bound sentinel for `pointCount <= 4096`. Skiko can rewrite
  the first recorded `COMMAND_DRAW_POINTS` point count to `4097`. The exact row passed with one expected
  `command-stream-invalid` fallback and no unsupported rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-014443/suite.tsv`.
  The scoped `CASE_GROUPS=primitive-invalid` quick group now covers thirteen rows; aggregate 13/13 passed,
  `fallback_sum=13`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-014536/suite.tsv`.
  Bounded default-order ranges also passed around the point and vertices insertion slots. Point range aggregate:
  27/27 passed, `fallback_sum=23`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=11992`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-015718/suite.tsv`.
  Vertices range aggregate: 9/9 passed, `fallback_sum=7`, `unsupported_rows=0`, `picture_frames=0`, and
  `command_frames=5171`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-021540/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Draw-vertices parser validation now has a live vertex-count sentinel. Skiko can rewrite the first recorded
  `COMMAND_DRAW_VERTICES` vertex count to two, matching JBR's `vertexCount >= 3` and exact variable record-length
  guards. The exact row passed with one expected `command-stream-invalid` fallback and no unsupported rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-194429/suite.tsv`.
  The scoped `CASE_GROUPS=primitive-invalid` quick group now covers six rows including draw-vertices vertex count;
  aggregate 6/6 passed, `fallback_sum=6`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-194537/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Draw-vertices parser validation now also has a live record-length sentinel. Skiko can shorten the first recorded
  `COMMAND_DRAW_VERTICES` record length, matching JBR's exact `recordLength == 8 + vertexCount * 5 + indexCount`
  guard. The exact row passed with one expected `command-stream-invalid` fallback and no unsupported rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-195430/suite.tsv`.
  The scoped `CASE_GROUPS=primitive-invalid` quick group now covers seven rows including both draw-vertices sentinels;
  aggregate 7/7 passed, `fallback_sum=7`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-195521/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Draw-vertices parser validation now also has a live vertex-mode sentinel. Skiko can rewrite the first recorded
  `COMMAND_DRAW_VERTICES` vertex mode to `3`, matching JBR's `vertexMode <= 2` guard. The exact row passed with one
  expected `command-stream-invalid` fallback and no unsupported rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-200424/suite.tsv`.
  The scoped `CASE_GROUPS=primitive-invalid` quick group now covers eight rows including draw-vertices vertex mode;
  aggregate 8/8 passed, `fallback_sum=8`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-200517/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Draw-vertices parser validation now also has a live blend-mode sentinel. Skiko can rewrite the first recorded
  `COMMAND_DRAW_VERTICES` blend mode to an unsupported value, matching JBR's `isSupportedBlendMode(blendMode)` guard.
  The exact row passed with one expected `command-stream-invalid` fallback and no unsupported rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-201535/suite.tsv`.
  The scoped `CASE_GROUPS=primitive-invalid` quick group now covers nine rows including draw-vertices blend mode;
  aggregate 9/9 passed, `fallback_sum=9`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-201625/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Draw-vertices parser validation now also has a live index-count sentinel. Skiko can rewrite the first recorded
  `COMMAND_DRAW_VERTICES` index count to `-1`, matching JBR's `indexCount >= 0` guard and variable record-length
  checks. The exact row passed with one expected `command-stream-invalid` fallback and no unsupported rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-202605/suite.tsv`.
  The scoped `CASE_GROUPS=primitive-invalid` quick group now covers ten rows including draw-vertices index count;
  aggregate 10/10 passed, `fallback_sum=10`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-202656/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- Draw-vertices parser validation now also has live upper-bound sentinels for `vertexCount <= 4096` and
  `indexCount <= 8192`. Skiko can rewrite the first recorded `COMMAND_DRAW_VERTICES` vertex count to `4097` or index
  count to `8193`, and Magic Jewel exposes both rows in the `primitive-invalid` quick group. The exact two-row run
  passed with one expected `command-stream-invalid` fallback per row and no unsupported rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-013122/suite.tsv`.
  The scoped `CASE_GROUPS=primitive-invalid` quick group now covers twelve rows; aggregate 12/12 passed,
  `fallback_sum=12`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-013258/suite.tsv`.
  Skiko focused publication and `JbrSkiaInteropTest` also passed.
- CMP now emits `COMMAND_CLEAR_IMAGE_CACHE` on the next top-level command frame after
  `clearInteropCachesForSurfaceChange()`, so JBR receives an explicit scoped image-cache clear when Skiko observes a
  destination surface/context migration. CMP focused recorder tests passed for the new pending-clear behavior plus the
  adjacent stable-image and color-filter-handle cache cases. Skiko now has a live record-flags corruption hook for op
  18, and Magic Jewel's exact `commands-invalid-image-cache-clear-record-flags-fallback` row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-091119/suite.tsv`.
  The supported `commands-forced-context-dynamic-images` row now requires the CMP/JBR scoped clear markers and passed
  with zero fallback and 410 JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-091332/suite.tsv`.
  The scoped `CASE_GROUPS=image-handles-invalid` quick group now covers twenty-three malformed image rows and passed
  with aggregate 23/23, `fallback_sum=23`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=468`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-091447/suite.tsv`.
  Focused screenshot parity for forced-context image refs also passed with zero fallback and 487 JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260520-092645/suite.tsv`.
- Descriptor-handle eviction parser guards now have live record-flags sentinels for both eviction command families.
  Skiko can rewrite `COMMAND_EVICT_SHADER_HANDLE` or `COMMAND_EVICT_COLOR_FILTER_HANDLE` record flags to the antialias
  bit after the existing use-after-evict hook inserts the eviction record. The exact two-row run passed with aggregate
  2/2, `fallback_sum=2`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-085719/suite.tsv`.
  A focused five-row descriptor-handle slice around the new rows also passed with aggregate 5/5:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-085837/suite.tsv`.
- Image cache eviction parser guards now have a live record-flags sentinel. Skiko can rewrite
  `COMMAND_EVICT_IMAGE_CACHE_KEY` record flags to the antialias bit, matching JBR's parser guard that image cache
  eviction metadata records must have no flags. The exact row passed with one expected `command-stream-invalid`
  fallback and no unsupported rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-084204/suite.tsv`.
  The scoped `CASE_GROUPS=image-handles-invalid` quick group now covers twenty-two malformed image definition,
  cache-key, eviction, and image-ref rows and passed with aggregate 22/22, `fallback_sum=22`,
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-084246/suite.tsv`.
- Periodic full default command-probe sweep passed after the font-data record-flags slice landed. Aggregate: 406/406
  passed, 26 intentional unsupported-picture rows, 8,744 JBR picture frames, 49,068 JBR command frames, and 269
  structured fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-055642/suite.tsv`.
- Font-data definition parser guards now have a live record-flags sentinel. Skiko can rewrite the cache-front-loaded
  `COMMAND_DEFINE_FONT_DATA` record flags to `COMMAND_RECORD_FLAG_ANTIALIAS`, Magic Jewel exposes
  `commands-invalid-font-data-record-flags-fallback`, and report validation now supports this one-shot fallback plus
  recovery shape for records emitted only during command-cache warmup. The exact row passed with one expected fallback,
  no unsupported rows, no JBR picture frames, and command replay recovering afterward:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-055135/suite.tsv`.
  The scoped `CASE_GROUPS=native-text-invalid` quick group now covers eleven native text/font-data parser rows and
  passed with aggregate 11/11, `fallback_sum=11`, `unsupported_rows=0`, `picture_frames=0`, and
  `command_frames=482`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-055209/suite.tsv`.
- Focused image definition parser validation now includes record-flags coverage. Skiko can rewrite
  `COMMAND_DEFINE_IMAGE_ARGB` record flags to the antialias bit, matching JBR's parser guard that image definition
  metadata records must have no flags. The exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-053349/suite.tsv`.
  The scoped `CASE_GROUPS=image-handles-invalid` quick group now covers twenty-one malformed image definition/cache-key
  and image-ref rows and passed with zero unsupported rows, zero picture rows, zero command replay rows, and twenty-one
  structured invalid-stream fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-053424/suite.tsv`.
- Focused shader descriptor parser validation now includes record-flags coverage. Skiko can rewrite
  `COMMAND_DEFINE_SHADER_DESCRIPTOR` record flags to the antialias bit, matching JBR's parser guard that shader
  descriptor definition records must have no flags. The exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-051747/suite.tsv`.
  The scoped `CASE_GROUPS=shader-descriptor-invalid` quick group now covers thirty malformed shader-descriptor rows
  and passed with zero unsupported rows, zero picture rows, zero command replay rows, and thirty structured
  invalid-stream fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-051827/suite.tsv`.
- Focused effect descriptor parser validation now includes record-flags coverage. Skiko can rewrite
  `COMMAND_DEFINE_EFFECT_DESCRIPTOR` record flags to the antialias bit, matching JBR's parser guard that descriptor
  definition records must have no flags. The exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-050237/suite.tsv`.
  The scoped `CASE_GROUPS=effect-descriptor-invalid` quick group now covers twenty-eight malformed effect-descriptor
  rows and passed with zero unsupported rows, zero picture rows, zero command replay rows, and twenty-eight structured
  invalid-stream fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-050312/suite.tsv`.
- Focused saveLayer command parser validation now includes the record-flags guard where `COMMAND_SAVE_LAYER` rejects
  the antialias bit even though the bit is otherwise globally recognized. The exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-045239/suite.tsv`.
  The scoped `CASE_GROUPS=save-layer-invalid` quick group now covers fifteen malformed saveLayer rows and passed with
  zero unsupported rows, zero picture rows, zero command replay rows, and fifteen structured invalid-stream fallback
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-045314/suite.tsv`.
- Full default command-probe sweep passed after rebuilding local JBR Skia artifacts and adding primitive command
  parser sentinels for stroke cap, transform record flags, and clip operation. Screenshot assertions were disabled for
  the broad semantic sweep to avoid known macOS window-capture flakes; command/fallback markers were still validated.
  Aggregate: 401/401 passed, 26 intentional unsupported-picture rows, 32,480 JBR picture frames, 184,608 JBR command
  frames, and 264 structured fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-002459/suite.tsv`.
- Focused primitive command parser validation now has a quick group. The exact row and grouped runs passed after
  rebuilding `/tmp/jbr-skia-run/desktop`, `/tmp/jbr-api-shim.jar`, and `/tmp/jbr-skia-native/libjbrskiainterop.dylib`;
  stale artifacts had presented as `SKIKO_JBR_INTEROP_FALLBACK reason=service-unavailable` before the rebuild. The
  quick group covers `COMMAND_STROKE_LINE` cap, `COMMAND_TRANSLATE` record flags, and `COMMAND_CLIP_RECT` operation:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-002341/suite.tsv`.
- Focused command payload and record-length validation passed after adding typed live sentinels for JBR's parser-only
  payload-length and record-length guards. The focused four-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-193923/suite.tsv`.
  The expanded `CASE_GROUPS=stream-invalid` quick path now covers eight stream parser guards; all eight passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-193642/suite.tsv`.
- Focused command-stream coordinate-space and paint-format validation passed after adding typed live sentinels for
  JBR's parser-only header guards. The focused two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-193259/suite.tsv`.
  The expanded `CASE_GROUPS=stream-invalid` quick path now covers stream flags, command record flags, coordinate
  space, and paint format; all four rows passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-193047/suite.tsv`.
- Focused command record-flags validation passed after adding a typed marker and live suite row for JBR's parser-only
  unsupported command record-flags guard. The new row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-192645/suite.tsv`.
  The expanded `CASE_GROUPS=stream-invalid` quick path now covers both stream header flags and command record flags,
  and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-192129/suite.tsv`.
- Focused command-stream header flag validation passed after adding a typed marker and live suite row for Skiko's
  generic stream corruption switch. The new `commands-invalid-command-stream-flags-fallback` row rewrites the stream
  flags word to an unsupported value, matching JBR's parser-only unsupported stream-flags guard:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-190837/suite.tsv`.
  The one-row `CASE_GROUPS=stream-invalid` quick path also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-190836/suite.tsv`.
- Current-artifact focused validation rechecked the already-live unknown effect descriptor type sentinel:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-190020/suite.tsv`.
  The row still reaches JBR's parser-only unknown-type rejection path with
  `SKIKO_JBR_INTEROP_EFFECT_DESCRIPTOR_TYPE_CORRUPTED` and zero JBR replay frames.
- Focused plus grouped stroke-path dash path-effect scalar validation passed after adding op 61 interval-count and
  interval-value corruption hooks. The focused two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-184636/suite.tsv`.
  The rows record `COMMAND_STROKE_PATH` with a dash path effect, corrupt the interval count or one interval value, and
  require structured `command-stream-invalid` fallback with zero JBR replay frames. The `CASE_GROUPS=path-invalid`
  quick group now covers seven malformed path rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-184818/suite.tsv`.
- Focused plus grouped fill-rect shader-ref scalar validation passed after adding op 58 bounds and alpha corruption
  hooks. The focused three-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-182225/suite.tsv`.
  The rows record `COMMAND_FILL_RECT_SHADER_REF`, corrupt horizontal bounds, vertical bounds, or `alpha1000`, and
  require structured `command-stream-invalid` fallback with zero JBR replay frames. The new
  `CASE_GROUPS=shader-ref-invalid` quick group passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-183006/suite.tsv`.
  The bounded default-order range from `commands-invalid-shader-descriptor-use-fallback` through
  `commands-invalid-descriptor-use-after-evict-fallback` also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-183458/suite.tsv`.
- Focused plus grouped fill-rect color-filter scalar validation passed after adding op 42 blend-mode, width, and height
  corruption hooks plus op 47 color-filter-ref width/height hooks. The original focused three-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-173809/suite.tsv`.
  The newer op 47 exact two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-022809/suite.tsv`.
  The rows record `COMMAND_FILL_RECT_COLOR_FILTER` or `COMMAND_FILL_RECT_COLOR_FILTER_REF`, corrupt the tint blend
  mode or dimensions, and require structured `command-stream-invalid` fallback with zero JBR replay frames. The
  expanded `CASE_GROUPS=fill-rect-color-filter-invalid` quick group passed with five rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-022940/suite.tsv`.
  The bounded default-order range from `commands-raw-blend-color-filter-fallback` through `commands-color-filter` also
  passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260521-023307/suite.tsv`.
- Focused plus grouped fill-rect blend-mode scalar validation passed after adding op 41 width/height corruption hooks.
  The focused two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-172416/suite.tsv`.
  The rows record `COMMAND_FILL_RECT_BLEND_MODE`, rewrite width or height to `-1`, and require structured
  `command-stream-invalid` fallback with zero JBR replay frames. The new `CASE_GROUPS=blend-mode-invalid` quick group
  passed as the targeted iteration slice:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-172545/suite.tsv`.
  The bounded default-order range from `commands-blend-mode` through `commands-graphics-layer` also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-172903/suite.tsv`.
- Focused plus grouped saveLayer blend/color-filter-ref blend-mode validation passed after adding an op 54 blend-mode
  corruption hook. The focused row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-152806/suite.tsv`.
  The row records `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF`, rewrites its saveLayer blend mode to an unsupported
  value, and requires structured `command-stream-invalid` fallback with zero JBR replay frames. The expanded
  `CASE_GROUPS=save-layer-invalid` group now covers fourteen malformed saveLayer rows; all fourteen passed with zero
  unsupported rows, zero picture rows, zero command replay rows, and one structured fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-152857/suite.tsv`.
  The bounded default-order saveLayer range also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-153941/suite.tsv`.
  It covered two supported command-replay rows, all fourteen malformed saveLayer rows, and the existing raw
  color-filter fallback sentinel.
- Focused plus grouped saveLayer color-filter-ref dimension validation passed after adding op 52/op 54 width/height
  corruption hooks. The focused four-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-140556/suite.tsv`.
  The rows record descriptor-backed saveLayer color-filter refs, rewrite width or height to `-1`, and require
  structured `command-stream-invalid` fallback with zero JBR replay frames. The expanded
  `CASE_GROUPS=save-layer-invalid` group now covers thirteen malformed saveLayer rows; all thirteen passed with zero
  unsupported rows, zero picture rows, zero command replay rows, and one structured fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-140848/suite.tsv`.
  The bounded default-order saveLayer range also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-141854/suite.tsv`.
  It covered two supported command-replay rows, all thirteen malformed saveLayer rows, and the existing raw
  color-filter fallback sentinel.
- Focused plus grouped saveLayer color-filter-ref alpha validation passed after adding op 52/op 54 alpha corruption
  hooks. The focused two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-124011/suite.tsv`.
  The rows record descriptor-backed saveLayer color-filter refs, rewrite `alpha1000` to `1001`, and require structured
  `command-stream-invalid` fallback with zero JBR replay frames. The expanded `CASE_GROUPS=save-layer-invalid` group
  now covers nine malformed saveLayer rows; all nine passed with zero unsupported rows, zero picture rows, zero command
  replay rows, and one structured fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-124137/suite.tsv`.
  The bounded default-order saveLayer range also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-124859/suite.tsv`.
  It covered two supported command-replay rows, all nine malformed saveLayer rows, and the existing raw color-filter
  fallback sentinel.
- Focused plus grouped saveLayer image-filter dimension validation passed after adding op 55 width/height corruption
  hooks. The focused two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-121803/suite.tsv`.
  The rows record graphics-layer render-effect replay, rewrite width or height to `-1`, and require structured
  `command-stream-invalid` fallback with zero JBR replay frames. The expanded `CASE_GROUPS=save-layer-invalid` group
  now covers seven malformed saveLayer rows; all seven passed with zero unsupported rows, zero picture rows, zero
  command replay rows, and one structured fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-121925/suite.tsv`.
  The bounded default-order saveLayer range also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-122729/suite.tsv`.
  It covered two supported command-replay rows, all seven malformed saveLayer rows, and the existing raw color-filter
  fallback sentinel.
- Focused plus grouped saveLayer image-filter alpha validation passed after extending the saveLayer-invalid quick group
  to op 55 `COMMAND_SAVE_LAYER_IMAGE_FILTER_REF`. The focused row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-120426/suite.tsv`.
  It records graphics-layer render-effect replay, rewrites `alpha1000` to `1001`, and requires structured
  `command-stream-invalid` fallback with zero JBR replay frames. The expanded `CASE_GROUPS=save-layer-invalid` group
  now covers five malformed saveLayer rows; all five passed with zero unsupported rows, zero picture rows, zero command
  replay rows, and one structured fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-120842/suite.tsv`.
- Focused plus grouped saveLayer image-filter handle validation passed after extending descriptor-use corruption and
  descriptor use-after-evict insertion to `COMMAND_SAVE_LAYER_IMAGE_FILTER_REF`. The exact two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-081201/suite.tsv`.
  The rows corrupt the graphics-layer render-effect image-filter handle to an undefined value or evict it immediately
  before op 55 consumes it, and both require structured `command-stream-invalid` fallback with zero JBR replay frames.
  The follow-up `CASE_GROUPS=descriptor-handles-invalid` run covered forty-five malformed descriptor/child-handle rows;
  all forty-five passed with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-105843/suite.tsv`.
- Focused saveLayer color-filter use-after-evict validation passed after extending descriptor eviction insertion to
  `COMMAND_SAVE_LAYER_COLOR_FILTER_REF` and `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF`. The exact two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-041255/suite.tsv`.
  Both rows insert `COMMAND_EVICT_COLOR_FILTER_HANDLE` immediately before the saveLayer record that consumes the handle,
  then require structured `command-stream-invalid` fallback with zero JBR replay frames.
- Focused saveLayer color-filter handle validation passed after extending live descriptor-use corruption to
  `COMMAND_SAVE_LAYER_COLOR_FILTER_REF` and `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF`. The exact two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-034302/suite.tsv`.
  The rows corrupt the recorded color-filter handle pair to an undefined handle for op 52 and op 54, matching JBR's
  parser-only undefined saveLayer color-filter handle checks. A follow-up `descriptor-handles-invalid` group run proved
  the new rows in area context before surfacing a stale group typo near the tail:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-034438/suite.tsv`.
  After correcting the group entry to `commands-chain-path-effect-child-wrong-effect-type-fallback`, the repaired tail
  passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-040920/suite.tsv`.
- Focused and bounded-range saveLayer validation passed after extending live parser sentinels to the remaining
  saveLayer blend guards: `COMMAND_SAVE_LAYER_BLEND_MODE` rejects unsupported blend-mode ids, and
  `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER` rejects tint color-filter blend modes other than `SRC_IN`. The scoped
  `CASE_GROUPS=save-layer-invalid` run now covers four malformed saveLayer rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-032849/suite.tsv`.
  The bounded default-order range from `commands-save-layer-filter` through
  `commands-save-layer-raw-color-filter-fallback` also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-033128/suite.tsv`.
  The op 51 row uses the existing graphics-layer blend+tint path because CMP emits
  `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER` from nested layer replay rather than plain `Canvas.saveLayer`.
- Focused and bounded-range saveLayer validation passed after adding live parser sentinels for `COMMAND_SAVE_LAYER`
  alpha bounds and `COMMAND_SAVE_LAYER_COLOR_FILTER` blend-mode constraints. The scoped quick group covers the two
  malformed saveLayer rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-030846/suite.tsv`.
  The adjacent default-order range from `commands-save-layer-filter` through
  `commands-save-layer-raw-color-filter-fallback` also passed, covering the supported tint-filter row, both new invalid
  rows, and the existing raw color-filter fallback row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-031011/suite.tsv`.
  This keeps saveLayer scalar hardening on the quick exact/group/range path instead of requiring a full default sweep
  for every small guard.
- Focused plus grouped image-handle validation passed after adding live `COMMAND_DRAW_IMAGE_REF` cache-key sentinels
  for missing image handles and use-after-evict. The exact two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-203912/suite.tsv`.
  New scoped `CASE_GROUPS=image-handles-invalid` covers the same two image cache-key rows for quick point-to-point
  iteration; both passed with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-204017/suite.tsv`.
- Expanded scoped image-handle validation passed after extending the cache-key hooks to target
  `COMMAND_DRAW_IMAGE_REF_COLOR_FILTER` and `COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF` without tripping earlier plain
  image refs in the same scene. `CASE_GROUPS=image-handles-invalid` now covers six malformed image cache-key rows; all
  six passed with zero unsupported rows, zero picture rows, zero command replay rows, and one structured fallback marker
  per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-204739/suite.tsv`.
- Focused plus grouped image-handle validation passed after adding an image-ref width mismatch sentinel for
  `COMMAND_DRAW_IMAGE_REF`. The focused row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-205347/suite.tsv`.
  `CASE_GROUPS=image-handles-invalid` now covers seven malformed image cache-key/dimension rows; all seven passed with
  zero unsupported rows, zero picture rows, zero command replay rows, and one structured fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-205428/suite.tsv`.
- Focused plus grouped image-handle validation passed after extending image-ref dimension mismatch sentinels across
  width and height for `COMMAND_DRAW_IMAGE_REF`, `COMMAND_DRAW_IMAGE_REF_COLOR_FILTER`, and
  `COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF`. The focused height spot-check passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-212843/suite.tsv`.
  `CASE_GROUPS=image-handles-invalid` now covers twelve malformed image cache-key/dimension rows; all twelve passed
  with zero unsupported rows, zero picture rows, zero command replay rows, and one structured fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-212945/suite.tsv`.
- Focused plus grouped image-definition validation passed after adding a live `COMMAND_DEFINE_IMAGE_ARGB` pixel-count
  mismatch sentinel. The focused row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-213840/suite.tsv`.
  `CASE_GROUPS=image-handles-invalid` now covers thirteen malformed image definition/cache-key/dimension rows; all
  thirteen passed with zero unsupported rows, zero picture rows, zero command replay rows, and one structured fallback
  marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-213912/suite.tsv`.
- Focused plus grouped image-ref validation passed after adding live alpha bounds sentinels for
  `COMMAND_DRAW_IMAGE_REF`, `COMMAND_DRAW_IMAGE_REF_COLOR_FILTER`, and
  `COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF`. The focused three-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-214844/suite.tsv`.
  `CASE_GROUPS=image-handles-invalid` now covers sixteen malformed image definition/cache-key/dimension/alpha rows; all
  sixteen passed with zero unsupported rows, zero picture rows, zero command replay rows, and one structured fallback
  marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-215036/suite.tsv`.
- Focused plus grouped image-ref validation passed after adding live filter-quality bounds sentinels for the same op 16,
  op 45, and op 53 image-ref forms. The focused three-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-220628/suite.tsv`.
  `CASE_GROUPS=image-handles-invalid` now covers nineteen malformed image definition/cache-key/dimension/alpha/filter
  rows; all nineteen passed with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-220829/suite.tsv`.
- Focused plus grouped image-ref validation passed after adding a live inline color-filter blend-mode sentinel for
  `COMMAND_DRAW_IMAGE_REF_COLOR_FILTER`. The focused row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-222714/suite.tsv`.
  `CASE_GROUPS=image-handles-invalid` now covers twenty malformed image definition/cache-key/dimension/alpha/filter
  rows; all twenty passed with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-222802/suite.tsv`.
- Command-only full-sweep iteration now has an explicit screenshot-assertion skip for macOS capture flakiness, and the
  `commands-runtime-effect-child-only` gate was refreshed to the current three shader-handle definitions:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-234743/suite.tsv`.
- Magic Jewel command-probe iteration now supports `CASES_FROM`/`CASES_UNTIL` range slicing for faster resumed or
  point-to-point batches. One-row range smokes passed, including the post-guard check:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-235136/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-025324/suite.tsv`.
  A resumed command-only tail sweep from `commands-runtime-effect-child-only` then passed 292/292 rows with 94,687 JBR
  command frames, 17,970 expected picture-fallback frames, 17 expected unsupported-marker rows, and 208 total expected
  fallback markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-235421/suite.tsv`.
- Focused plus grouped descriptor-handle validation passed after adding top-level path-effect descriptor-use sentinels
  for missing handles, use-after-evict, and wrong-family handles on `COMMAND_DRAW_PATH_PATH_EFFECT_REF`. The focused
  three-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-200433/suite.tsv`.
  Scoped `CASE_GROUPS=descriptor-handles-invalid` now covers thirty-nine malformed descriptor/child-handle rows; all
  thirty-nine passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-200630/suite.tsv`.
- Focused plus grouped descriptor child-handle validation passed after extending effect-child use-after-evict coverage to
  RuntimeEffect color-filter children and shader-color-filter effect children. The focused two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-193417/suite.tsv`.
  Scoped `CASE_GROUPS=descriptor-handles-invalid` now covers thirty-six malformed descriptor/child-handle rows; all
  thirty-six passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-193605/suite.tsv`.
- Focused plus grouped descriptor child-handle validation passed after adding live shader child use-after-evict
  sentinels. Skiko can now evict a shader child handle immediately before descriptor validation for transformed shader,
  composite shader destination and source children, shader-color-filter shader child, and RuntimeEffect shader child
  descriptors. The focused five-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-190602/suite.tsv`.
  Scoped `CASE_GROUPS=descriptor-handles-invalid` now covers thirty-four malformed descriptor/child-handle rows; all
  thirty-four passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-190943/suite.tsv`.
- Focused plus grouped descriptor child-handle validation passed after adding live composite shader source-child
  sentinels. Skiko can now rewrite the source child of a `COMMAND_SHADER_DESCRIPTOR_COMPOSITE` descriptor to a
  color-filter handle or to an undefined shader handle, matching JBR's existing `srcHandle` parser checks. The focused
  two-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-184048/suite.tsv`.
  Scoped `CASE_GROUPS=descriptor-handles-invalid` now covers twenty-nine malformed descriptor/child-handle rows; all
  twenty-nine passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-184218/suite.tsv`.
- Focused plus grouped effect-descriptor validation passed after adding Magic Jewel rows for with-input image-filter
  descriptor payload guards: blur-with-input sigma, negative sigma, tile mode, and offset-with-input delta. The focused
  four-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-181403/suite.tsv`.
  Scoped `CASE_GROUPS=effect-descriptor-invalid` now covers twenty-seven malformed effect-descriptor rows; all
  twenty-seven passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-181711/suite.tsv`.
- Focused plus grouped descriptor child-handle validation passed after adding live blur-with-input image-filter
  missing-child and use-after-evict sentinels. The focused two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-175550/suite.tsv`.
  Scoped `CASE_GROUPS=descriptor-handles-invalid` now covers twenty-four malformed descriptor/child-handle rows; all
  twenty-four passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-175712/suite.tsv`.
- Focused plus grouped descriptor-handle validation passed after adding a live blur-with-input image-filter child
  wrong-type sentinel. The focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-173959/suite.tsv`.
  Scoped `CASE_GROUPS=descriptor-handles-invalid` now covers twenty-two malformed descriptor/child-handle rows; all
  twenty-two passed, with zero unsupported rows, zero picture rows, zero command replay rows, and one structured
  fallback marker per row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-174050/suite.tsv`.
- Focused plus adjacent child-schema validation passed after adding live RuntimeEffect shader and color-filter duplicate
  child-index sentinels. The focused two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-164327/suite.tsv`.
  The compact fourteen-row RuntimeEffect child-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-164445/suite.tsv`.
  Scoped `CASE_GROUPS=runtime-effect-invalid` consolidation now covers sixty-two malformed RuntimeEffect rows; all
  sixty-two passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-165244/suite.tsv`.
- Focused plus adjacent uniform-schema validation passed after adding live RuntimeEffect shader and color-filter
  uniform-schema name-range sentinels. The focused two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-153449/suite.tsv`.
  The compact fourteen-row RuntimeEffect uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-153628/suite.tsv`.
  Scoped `CASE_GROUPS=runtime-effect-invalid` consolidation now covers sixty malformed RuntimeEffect rows; all sixty
  passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-154609/suite.tsv`.
- Focused plus adjacent child-schema validation passed after adding live RuntimeEffect shader and color-filter
  child-schema name-range sentinels. The focused two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-142845/suite.tsv`.
  The compact fourteen-row RuntimeEffect child-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-143020/suite.tsv`.
  Scoped `CASE_GROUPS=runtime-effect-invalid` consolidation now covers fifty-eight malformed RuntimeEffect rows; all
  fifty-eight passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-144007/suite.tsv`.
- Focused plus adjacent child-schema validation passed after adding live RuntimeEffect shader and color-filter
  child-schema max-name-length sentinels. The focused two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-132914/suite.tsv`.
  The compact twelve-row RuntimeEffect child-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-133102/suite.tsv`.
  Scoped `CASE_GROUPS=runtime-effect-invalid` consolidation now covers fifty-six malformed RuntimeEffect rows; all
  fifty-six passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-134009/suite.tsv`.
- Focused plus adjacent child-schema validation passed after adding live RuntimeEffect shader and color-filter
  child-schema name-length sentinels. The focused two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-123344/suite.tsv`.
  The compact ten-row RuntimeEffect child-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-123520/suite.tsv`.
  Scoped `CASE_GROUPS=runtime-effect-invalid` consolidation now covers fifty-four malformed RuntimeEffect rows; all
  fifty-four passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-124249/suite.tsv`.
- Focused plus adjacent child-schema validation passed after adding live RuntimeEffect shader and color-filter negative
  child-index sentinels. The focused two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-113957/suite.tsv`.
  The compact eight-row RuntimeEffect child-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-114142/suite.tsv`.
  Scoped `CASE_GROUPS=runtime-effect-invalid` consolidation now covers fifty-two malformed RuntimeEffect rows; all
  fifty-two passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-114736/suite.tsv`.
- Current-artifact focused plus grouped validation rechecked the live unknown effect descriptor type sentinel. The exact
  row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-111741/suite.tsv`.
  The scoped `CASE_GROUPS=effect-descriptor-invalid` area group also passed, covering twenty-three effect descriptor
  parser/fallback rows with zero unsupported rows, zero picture rows, and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-111855/suite.tsv`.
- Scoped `CASE_GROUPS=runtime-effect-invalid` consolidation passed after adding the RuntimeEffect uniform-schema
  name-length and max-name-length live sentinels. The area sweep now covers fifty malformed RuntimeEffect rows; all
  fifty passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-103723/suite.tsv`.
- Focused plus adjacent uniform-schema validation passed after adding live RuntimeEffect shader and color-filter
  uniform-schema max-name-length sentinels. The focused two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-102412/suite.tsv`.
  The compact fourteen-row RuntimeEffect uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-102601/suite.tsv`.
  The new rows rewrite the first recorded named-uniform schema name length to `65`, reaching JBR's `nameLength > 64`
  guard before native compile/build or replay.
- Focused plus adjacent uniform-schema validation passed after adding live RuntimeEffect shader and color-filter
  uniform-schema name-length sentinels. The focused two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-101012/suite.tsv`.
  The compact twelve-row RuntimeEffect uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-101145/suite.tsv`.
  The new rows rewrite the first recorded named-uniform schema name length to `0`, reaching JBR's
  `nameLength <= 0` guard before native compile/build or replay.
- Scoped `CASE_GROUPS=runtime-effect-invalid` consolidation passed after adding the RuntimeEffect uniform-schema
  float-offset and float-range live sentinels. The area sweep now covers forty-six malformed RuntimeEffect rows; all
  forty-six passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-031347/suite.tsv`.
- Focused plus adjacent uniform-schema validation passed after adding live RuntimeEffect shader and color-filter
  uniform-schema float-range sentinels. The focused two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-030358/suite.tsv`.
  The compact ten-row RuntimeEffect uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-030537/suite.tsv`.
  The new rows rewrite the first recorded named-uniform schema float-offset to `uniformFloatCount`, reaching JBR's
  `floatOffset > uniformFloatCount - floatCount` guard before native compile/build or replay.
- Focused plus adjacent uniform-schema validation passed after adding live RuntimeEffect shader and color-filter
  uniform-schema float-offset sentinels. The focused two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-025315/suite.tsv`.
  The compact eight-row RuntimeEffect uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-025445/suite.tsv`.
  The new rows rewrite the first recorded named-uniform schema float-offset to `-1`, reaching JBR's
  `floatOffset < 0` guard before native compile/build or replay.
- Scoped `CASE_GROUPS=runtime-effect-invalid` consolidation passed after adding the RuntimeEffect child-index and
  uniform-schema float-count live sentinels. The area sweep now covers forty-two malformed RuntimeEffect rows; all
  forty-two passed, with six intentional picture-fallback/parser-only rows and zero command replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-021819/suite.tsv`.
- Focused plus adjacent uniform-schema validation passed after adding live RuntimeEffect shader and color-filter
  uniform-schema float-count sentinels. The focused two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-021125/suite.tsv`.
  The compact six-row RuntimeEffect uniform-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-021257/suite.tsv`.
  The new rows rewrite the first recorded named-uniform schema float-count to `0`, reaching JBR's `floatCount <= 0`
  guard before native compile/build or replay.
- Focused plus adjacent child-schema validation passed after adding live RuntimeEffect shader and color-filter
  child-index schema sentinels. The focused two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-020224/suite.tsv`.
  The compact six-row RuntimeEffect child-schema slice passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-020355/suite.tsv`.
  The new rows rewrite the first recorded named-child referenced index to `childCount`, reaching JBR's
  `referencedChildIndex >= childCount` guard before native build or replay.
- Scoped `CASE_GROUPS=runtime-effect-invalid` consolidation passed after the RuntimeEffect shader/color-filter
  source-code, source-hash, uniform-name, and child-name live sentinels. The area sweep covered thirty-eight malformed
  RuntimeEffect rows; all thirty-eight passed, with six intentional picture-fallback/parser-only rows and zero command
  replay rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-012854/suite.tsv`.
  Continue using exact `CASES=...` rows plus tiny adjacent slices for point-to-point iteration, and reserve full default
  sweeps for periodic consolidation.
- Focused plus adjacent child-schema validation passed after adding a live RuntimeEffect color-filter child-name schema
  sentinel. The focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-012238/suite.tsv`.
  The narrower five-row RuntimeEffect color-filter child-schema subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-012327/suite.tsv`.
  The new row rewrites the first recorded RuntimeEffect color-filter named-child character to `1`, reaching JBR's
  `isValidRuntimeEffectUniformName` first-character guard while validating child schema metadata.
- Focused plus adjacent child-schema validation passed after adding a live RuntimeEffect shader child-name schema
  sentinel. The focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-011504/suite.tsv`.
  The narrower five-row RuntimeEffect shader child-schema subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-011554/suite.tsv`.
  The new row rewrites the first recorded RuntimeEffect shader named-child character to `1`, reaching JBR's
  `isValidRuntimeEffectUniformName` first-character guard while validating child schema metadata.
- Focused plus adjacent color-filter parser validation passed after adding a live RuntimeEffect color-filter
  uniform-name schema sentinel. The focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-010124/suite.tsv`.
  The narrower twelve-row RuntimeEffect color-filter parser subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-010219/suite.tsv`.
  The new row rewrites the first recorded RuntimeEffect color-filter named-uniform character to `1`, reaching JBR's
  `isValidRuntimeEffectUniformName` first-character guard during descriptor validation.
- Focused plus adjacent shader parser validation passed after adding a live RuntimeEffect shader uniform-name schema
  sentinel. The focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-005128/suite.tsv`.
  The narrower eight-row RuntimeEffect shader parser subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-005221/suite.tsv`.
  The new row rewrites the first recorded RuntimeEffect shader named-uniform character to `1`, reaching JBR's
  `isValidRuntimeEffectUniformName` first-character guard during descriptor validation.
- Focused plus adjacent color-filter parser validation passed after adding a live RuntimeEffect color-filter
  source-hash mismatch sentinel. The focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-003812/suite.tsv`.
  The narrower eleven-row RuntimeEffect color-filter parser subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-003922/suite.tsv`.
  The new row flips one recorded RuntimeEffect color-filter descriptor source-hash word while leaving the SKSL payload
  unchanged, reaching JBR's source-hash mismatch guard before native compile.
- Focused plus adjacent color-filter parser validation passed after adding a live RuntimeEffect color-filter
  source-code parser sentinel. The focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-002624/suite.tsv`.
  The narrower ten-row RuntimeEffect color-filter parser subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260518-002719/suite.tsv`.
  The new row rewrites one RuntimeEffect color-filter descriptor SKSL code unit to `0`, recomputes the descriptor
  source hash, and reaches JBR's source-code range guard before native compile. This used the quick exact-row plus
  adjacent-family slice rather than a full default sweep.
- Focused plus grouped validation passed after adding a live RuntimeEffect shader source-code parser sentinel. The
  focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-235533/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers thirty-two malformed RuntimeEffect rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-235624/suite.tsv`.
  The new row rewrites one RuntimeEffect shader descriptor SKSL code unit to `0`, recomputes the descriptor source
  hash, and reaches JBR's source-code range guard before native compile.
- Focused plus grouped validation passed after adding a live drawShadow path-data verb sentinel. The focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-234649/suite.tsv`.
  The `CASE_GROUPS=path-invalid` subset now covers five malformed path rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-234726/suite.tsv`.
  The new row corrupts the first `COMMAND_DRAW_SHADOW_PATH` path verb to `99`, reaching JBR's `validatePathData`
  unknown-verb branch before replay.
- Focused plus grouped validation passed after adding a live stroked-path dash path-effect path-data verb sentinel. The
  focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-234049/suite.tsv`.
  The `CASE_GROUPS=path-invalid` subset now covers four malformed direct/path-effect path rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-234129/suite.tsv`.
  The new row corrupts the first `COMMAND_STROKE_PATH_DASH_PATH_EFFECT` path verb to `99`, reaching JBR's
  `validatePathData` unknown-verb branch before replay.
- Focused plus grouped validation passed after adding a live drawPath path-effect-ref path-data verb sentinel. The
  focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-233519/suite.tsv`.
  The `CASE_GROUPS=path-invalid` subset now covers three malformed direct/path-effect path rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-233604/suite.tsv`.
  The new row corrupts the first `COMMAND_DRAW_PATH_PATH_EFFECT_REF` path verb to `99`, reaching JBR's
  `validatePathData` unknown-verb branch before replay.
- Focused plus grouped validation passed after adding a live direct clipPath path-data verb sentinel. The focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-232957/suite.tsv`.
  The `CASE_GROUPS=path-invalid` subset now covers two direct malformed-path rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-233038/suite.tsv`.
  The new row corrupts the first `COMMAND_CLIP_PATH` path verb to `99`, reaching JBR's `validatePathData`
  unknown-verb branch before replay.
- Focused plus grouped validation passed after adding a live direct drawPath path-data verb sentinel. The focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-232552/suite.tsv`.
  The new `CASE_GROUPS=path-invalid` subset currently covers the same direct drawPath malformed-path row, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-232631/suite.tsv`.
  The row corrupts the first `COMMAND_DRAW_PATH` path verb to `99`, reaching JBR's `validatePathData` unknown-verb
  branch before replay.
- Focused plus grouped validation passed after adding a live stamped path-effect descriptor path-data verb sentinel.
  The focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-230818/suite.tsv`.
  The scoped `CASE_GROUPS=effect-descriptor-invalid` subset now covers twenty-three malformed effect descriptor rows,
  all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-230917/suite.tsv`.
  The new row corrupts the first stamped path-effect descriptor path verb to `99`, reaching JBR's `validatePathData`
  unknown-verb branch before replay.
- Current-artifact focused plus grouped validation rechecked the already-live unknown effect descriptor type sentinel.
  The focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-225228/suite.tsv`.
  The scoped `CASE_GROUPS=effect-descriptor-invalid` subset covered twenty-two malformed effect descriptor rows, all
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-225310/suite.tsv`.
  The row rewrites one recorded effect descriptor type to an unknown value and reaches JBR's parser-only unknown-type
  rejection before replay.
- Focused plus grouped validation passed after extending the live path-gradient path-data verb parser guard to radial
  and sweep path commands. The focused rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-223824/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-path-invalid` subset now covers eighteen path-gradient invalid rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-223942/suite.tsv`.
  The new rows corrupt the first recorded radial/sweep path-gradient path verb to `99`, reaching JBR's
  `validatePathData` unknown-verb branch before replay.
- Focused plus grouped validation passed after adding a live linear path-gradient path-data verb parser guard. The
  focused row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-222317/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-path-invalid` subset now covers sixteen path-gradient invalid rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-222406/suite.tsv`.
  The new row corrupts the first recorded linear path-gradient path verb to `99`, reaching JBR's `validatePathData`
  unknown-verb branch before replay.
- Broader `CASE_GROUPS=gradient-invalid` consolidation passed after completing the path-gradient header guards. The
  area sweep now covers fifty-seven invalid gradient rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-214935/suite.tsv`.
- Focused plus grouped validation passed after extending path-gradient header parser guards to radial and sweep paths.
  The focused four-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-213839/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-path-invalid` subset now covers fifteen path-gradient invalid rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-214053/suite.tsv`.
  The new rows corrupt radial/sweep path-gradient fill-type to `99` and path-data length to `-1`, reaching JBR's path
  header guard before replay.
- Focused plus grouped validation passed after adding live linear path-gradient header parser guards. The focused
  two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-212726/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-path-invalid` subset now covers eleven path-gradient invalid rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-212838/suite.tsv`.
  The new rows corrupt the linear path-gradient fill-type to `99` and path-data length to `-1`, reaching JBR's path
  header guard before replay.
- Broader `CASE_GROUPS=gradient-invalid` consolidation passed after completing the path-gradient guard family. The
  area sweep now covers fifty-one stroked-gradient/linear/radial/sweep/path-gradient invalid rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-205847/suite.tsv`.
- Focused plus grouped validation passed after adding live sweep path-gradient parser guards. The focused two-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-205145/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-path-invalid` subset now covers nine linear/radial/sweep path-gradient invalid
  rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-205256/suite.tsv`.
  The new rows corrupt sweep path-gradient color-count to `1` and the second stop to `0`, each with a typed
  `SKIKO_JBR_INTEROP_SWEEP_GRADIENT_PATH_*_CORRUPTED` marker before JBR replay.
- Focused plus grouped validation passed after adding live radial path-gradient parser guards. The focused four-row
  run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-204153/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-path-invalid` subset now covers seven linear/radial path-gradient invalid rows,
  all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-204356/suite.tsv`.
  The new rows corrupt radial path-gradient radius to `0`, tile-mode to `4`, color-count to `1`, and the second stop
  to `0`, each with a typed `SKIKO_JBR_INTEROP_RADIAL_GRADIENT_PATH_*_CORRUPTED` marker before JBR replay.
- Focused plus grouped validation passed after adding live linear path-gradient parser guards. The focused three-row
  run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-203301/suite.tsv`.
  The new narrow `CASE_GROUPS=gradient-path-invalid` subset passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-203456/suite.tsv`.
  The new rows compute the variable path payload length, then corrupt the linear-gradient path tile-mode slot to `4`,
  color-count slot to `1`, and second stop to `0`; each requires a typed
  `SKIKO_JBR_INTEROP_LINEAR_GRADIENT_PATH_*_CORRUPTED` marker and reaches JBR's parser guard before replay.
- Focused plus grouped validation passed after adding live sweep-gradient stop-order parser guards. The focused
  four-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-200346/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-invalid` subset now covers forty-two stroked-gradient/linear/radial/sweep invalid
  rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-200601/suite.tsv`.
  The new rows corrupt the second sweep-gradient stop to `0`, require typed
  `SKIKO_JBR_INTEROP_SWEEP_GRADIENT*_STOP_ORDER_CORRUPTED` markers, and reach JBR's strictly-increasing stop-order
  validator before replay.
- Focused plus grouped validation passed after adding live sweep-gradient color-count parser guards. The focused
  four-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-193808/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-invalid` subset now covers thirty-eight stroked-gradient/linear/radial/sweep
  invalid rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-194011/suite.tsv`.
  The new rows corrupt sweep-gradient color-count slots to `1`, require typed
  `SKIKO_JBR_INTEROP_SWEEP_GRADIENT*_COLOR_COUNT_CORRUPTED` markers, and reach JBR's color-count lower bound before
  replay.
- Focused plus grouped validation passed after adding live linear-gradient stop-order parser guards. The first focused
  attempt exposed an offset mistake that rewrote a color slot and kept replay enabled; after correcting the second-stop
  offsets to `13`, `15`, `17`, and `19`, the focused four-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-191609/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-invalid` subset now covers thirty-four stroked-gradient/linear/radial invalid
  rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-191802/suite.tsv`.
  The new rows corrupt the second linear-gradient stop to `0`, require typed
  `SKIKO_JBR_INTEROP_LINEAR_GRADIENT*_STOP_ORDER_CORRUPTED` markers, and reach JBR's strictly-increasing stop-order
  validator before replay.
- Focused plus grouped validation passed after adding live linear-gradient color-count parser guards. The focused
  four-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-185444/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-invalid` subset now covers thirty stroked-gradient/linear/radial invalid rows,
  all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-185635/suite.tsv`.
  The new rows corrupt linear-gradient color-count slots to `1`, require typed
  `SKIKO_JBR_INTEROP_LINEAR_GRADIENT*_COLOR_COUNT_CORRUPTED` markers, and reach JBR's color-count lower bound before
  replay.
- Focused plus grouped validation passed after adding live linear-gradient tile-mode parser guards. The focused four-row
  run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-183701/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-invalid` subset now covers twenty-six stroked-gradient/linear/radial invalid rows,
  all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-183900/suite.tsv`.
  The new rows corrupt linear-gradient tile-mode slots to `4`, require typed
  `SKIKO_JBR_INTEROP_LINEAR_GRADIENT*_TILE_MODE_CORRUPTED` markers, and reach JBR's tile-mode bounds before replay.
- Focused plus grouped validation passed after adding live radial-gradient stop-order parser guards. The focused
  four-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-181911/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-invalid` subset now covers twenty-two stroked-gradient/radial invalid rows, all
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-182106/suite.tsv`.
  The new rows corrupt the second radial-gradient stop to `0`, require typed
  `SKIKO_JBR_INTEROP_RADIAL_GRADIENT*_STOP_ORDER_CORRUPTED` markers, and reach JBR's strictly-increasing stop-order
  validator before replay.
- Focused plus grouped validation passed after adding live radial-gradient color-count parser guards. The focused
  four-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-175821/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-invalid` subset now covers eighteen stroked-gradient/radial invalid rows, all
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-180104/suite.tsv`.
  The new rows corrupt radial-gradient color-count slots to `1`, require typed
  `SKIKO_JBR_INTEROP_RADIAL_GRADIENT*_COLOR_COUNT_CORRUPTED` markers, and reach JBR's color-count lower bound before
  replay.
- Focused plus grouped validation passed after adding live radial-gradient tile-mode parser guards. The focused four-row
  run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-174220/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-invalid` subset now covers fourteen stroked-gradient/radial invalid rows, all
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-174457/suite.tsv`.
  The new rows corrupt radial-gradient tile-mode slots to `4`, require typed
  `SKIKO_JBR_INTEROP_RADIAL_GRADIENT*_TILE_MODE_CORRUPTED` markers, and reach JBR's tile-mode bounds before replay.
- Focused plus grouped validation passed after adding live radial-gradient radius parser guards. The focused four-row
  run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-172925/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-invalid` subset now covers ten stroked-gradient/radial-radius invalid rows, all
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-173217/suite.tsv`.
  The new rows corrupt radial-gradient radius slots to `0`, require typed `SKIKO_JBR_INTEROP_RADIAL_GRADIENT*_RADIUS`
  corruption markers, and reach JBR's `radius1000 > 0` validator before replay.
- Focused plus grouped validation passed after extending live stroked-gradient stroke-width parser guards across linear,
  radial, and sweep rect/round-rect variants. The focused five-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-171855/suite.tsv`.
  The expanded `CASE_GROUPS=gradient-invalid` subset now covers six stroked-gradient invalid rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-172203/suite.tsv`.
  Each row corrupts the recorded stroke-width slot to `0`, requires a typed `SKIKO_JBR_INTEROP_*_STROKE_WIDTH_CORRUPTED`
  marker, and reaches JBR's `strokeWidth1000 > 0` validator before replay.
- Focused plus grouped validation passed after adding a live linear-gradient stroke-width parser guard. The focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-123644/suite.tsv`.
  The new `CASE_GROUPS=gradient-invalid` subset currently covers the matching row and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-171310/suite.tsv`.
  The `commands-invalid-linear-gradient-stroke-width-fallback` row corrupts the recorded
  `COMMAND_STROKE_RECT_LINEAR_GRADIENT` stroke-width slot to `0`, requires
  `SKIKO_JBR_INTEROP_LINEAR_GRADIENT_STROKE_WIDTH_CORRUPTED`, and reaches JBR's `strokeWidth1000 > 0` validator before
  replay.
- Focused plus grouped validation passed after adding live native paragraph parser-guard coverage for font size, weight,
  width, slant, and font-family count. The focused five-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-122225/suite.tsv`.
  The expanded `CASE_GROUPS=native-text-invalid` subset now covers ten simple native text and paragraph invalid rows,
  all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-122535/suite.tsv`.
  The paragraph rows corrupt recorded `COMMAND_DRAW_PARAGRAPH_UTF16` font metadata to out-of-range values and require
  matching typed `SKIKO_JBR_INTEROP_PARAGRAPH_FONT_*_CORRUPTED` markers, keeping this parser-guard slice on the quick
  exact-row plus area-group path rather than another full default sweep.
- Focused plus grouped validation passed after extending live native text parser-guard coverage to font weight, width,
  slant, and font-family count. The focused four-row run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-121327/suite.tsv`.
  The expanded `CASE_GROUPS=native-text-invalid` subset now covers five simple native text invalid rows, all passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-121603/suite.tsv`.
  The new rows corrupt recorded `COMMAND_DRAW_TEXT_UTF16` font metadata to out-of-range values and require matching
  typed `SKIKO_JBR_INTEROP_TEXT_FONT_*_CORRUPTED` markers, closing the simple text parser-only guard cluster without a
  full default sweep.
- Focused plus grouped validation passed after adding a live native text font-size sentinel. The single-row focused
  run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-114019/suite.tsv`.
  The new `CASE_GROUPS=native-text-invalid` subset currently covers the matching row and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260517-120803/suite.tsv`.
  The new `commands-invalid-text-font-size-fallback` row corrupts a recorded `COMMAND_DRAW_TEXT_UTF16` font-size slot
  to `0`, requires `SKIKO_JBR_INTEROP_TEXT_FONT_SIZE_CORRUPTED`, and reaches JBR's `fontSize1000 > 0` parser guard
  before replay, with zero picture frames and zero command frames.
- Periodic full default command-probe sweep passed after the RuntimeEffect shader/color-filter lower-bound sentinel
  batch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-213052/suite.tsv`.
  The sweep covered 236 command-probe rows, all passing, with 110 command replay rows, 26 intentional picture-fallback
  rows, and 100 explicit structured fallback rows. The already-existing unknown effect descriptor type row and the
  newest `commands-runtime-effect-shader-negative-named-child-count-fallback` row both passed in the full sweep.
- Focused plus grouped validation passed after adding a live RuntimeEffect shader negative named-child-count sentinel.
  The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-195041/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers thirty-one RuntimeEffect-invalid rows with all rows
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-195130/suite.tsv`.
  The new `commands-runtime-effect-shader-negative-named-child-count-fallback` row corrupts a RuntimeEffect shader
  descriptor `namedChildCount` slot to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_NEGATIVE_NAMED_CHILD_COUNT_CORRUPTED`, and reaches JBR's
  named-child-count lower-bound parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect shader negative named-uniform-count
  sentinel. The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-190942/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers thirty RuntimeEffect-invalid rows with all rows passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-191029/suite.tsv`.
  The new `commands-runtime-effect-shader-negative-named-uniform-count-fallback` row corrupts a RuntimeEffect shader
  descriptor `namedUniformCount` slot to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_NEGATIVE_NAMED_UNIFORM_COUNT_CORRUPTED`, and reaches JBR's
  named-uniform-count lower-bound parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect shader negative child-count sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-184122/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers twenty-nine RuntimeEffect-invalid rows with all rows
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-184211/suite.tsv`.
  The new `commands-runtime-effect-shader-negative-child-count-fallback` row corrupts a RuntimeEffect shader descriptor
  `childCount` slot to `-1`, requires `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_NEGATIVE_CHILD_COUNT_CORRUPTED`, and
  reaches JBR's child-count lower-bound parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect shader negative uniform-float-count sentinel.
  The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-181512/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers twenty-eight RuntimeEffect-invalid rows with all rows
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-181601/suite.tsv`.
  The new `commands-runtime-effect-shader-negative-uniform-float-count-fallback` row corrupts a RuntimeEffect shader
  descriptor `uniformFloatCount` slot to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_NEGATIVE_UNIFORM_FLOAT_COUNT_CORRUPTED`, and reaches JBR's uniform-count
  lower-bound parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect color-filter negative named-child-count
  sentinel. The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-173844/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers twenty-seven RuntimeEffect-invalid rows with all rows
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-173928/suite.tsv`.
  The new `commands-runtime-effect-color-filter-negative-named-child-count-fallback` row corrupts a RuntimeEffect
  color-filter descriptor `namedChildCount` slot to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_NEGATIVE_NAMED_CHILD_COUNT_CORRUPTED`, and reaches JBR's
  named-child-count lower-bound parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect color-filter negative named-uniform-count
  sentinel. The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-171026/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers twenty-six RuntimeEffect-invalid rows with all rows
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-171111/suite.tsv`.
  The new `commands-runtime-effect-color-filter-negative-named-uniform-count-fallback` row corrupts a RuntimeEffect
  color-filter descriptor `namedUniformCount` slot to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_NEGATIVE_NAMED_UNIFORM_COUNT_CORRUPTED`, and reaches JBR's
  named-uniform-count lower-bound parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect color-filter negative child-count sentinel.
  The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-151126/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers twenty-five RuntimeEffect-invalid rows with all rows
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-151507/suite.tsv`.
  The new `commands-runtime-effect-color-filter-negative-child-count-fallback` row corrupts a RuntimeEffect
  color-filter descriptor `childCount` slot to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_NEGATIVE_CHILD_COUNT_CORRUPTED`, and reaches JBR's child-count
  lower-bound parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect color-filter negative uniform-float-count
  sentinel. The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-144403/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers twenty-four RuntimeEffect-invalid rows with all rows
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-144448/suite.tsv`.
  The new `commands-runtime-effect-color-filter-negative-uniform-float-count-fallback` row corrupts a RuntimeEffect
  color-filter descriptor `uniformFloatCount` slot to `-1`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_NEGATIVE_UNIFORM_FLOAT_COUNT_CORRUPTED`, and reaches JBR's
  uniform-count lower-bound parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect color-filter named-child-count sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-141823/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers twenty-three RuntimeEffect-invalid rows with all rows
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-141907/suite.tsv`.
  The new `commands-runtime-effect-color-filter-named-child-count-fallback` row corrupts a RuntimeEffect color-filter
  descriptor `namedChildCount` slot to `9`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_NAMED_CHILD_COUNT_CORRUPTED`, and reaches JBR's named-child-count
  upper-bound parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect color-filter named-uniform-count sentinel.
  The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-135519/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers twenty-two RuntimeEffect-invalid rows with all rows
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-135603/suite.tsv`.
  The new `commands-runtime-effect-color-filter-named-uniform-count-fallback` row corrupts a RuntimeEffect
  color-filter descriptor `namedUniformCount` slot to `17`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_NAMED_UNIFORM_COUNT_CORRUPTED`, and reaches JBR's named-uniform-count
  upper-bound parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect color-filter child-count sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-133243/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers twenty-one RuntimeEffect-invalid rows with all rows
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-133331/suite.tsv`.
  The new `commands-runtime-effect-color-filter-child-count-fallback` row corrupts a RuntimeEffect color-filter
  descriptor `childCount` slot to `9`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_CHILD_COUNT_CORRUPTED`, and reaches JBR's child-count upper-bound
  parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect color-filter uniform-float-count sentinel.
  The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-131044/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers twenty RuntimeEffect-invalid rows with all rows passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-131129/suite.tsv`.
  The new `commands-runtime-effect-color-filter-uniform-float-count-fallback` row corrupts a RuntimeEffect
  color-filter descriptor `uniformFloatCount` slot to `257`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_UNIFORM_FLOAT_COUNT_CORRUPTED`, and reaches JBR's uniform-count
  upper-bound parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect color-filter SKSL-length sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-124807/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers nineteen RuntimeEffect-invalid rows with all rows passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260516-124855/suite.tsv`.
  The new `commands-runtime-effect-color-filter-sksl-length-fallback` row corrupts a RuntimeEffect color-filter
  descriptor SKSL-length slot to `0`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_COLOR_FILTER_SKSL_LENGTH_CORRUPTED`, and reaches JBR's positive SKSL-length parser
  guard before schema validation or native compile. This followed the faster exact-row plus area-group iteration path;
  no full default command sweep was run for this micro-sentinel.
- Focused plus grouped validation passed after adding a live RuntimeEffect shader named-child-count sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-205029/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers eighteen RuntimeEffect-invalid rows with all rows passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-205114/suite.tsv`.
  The new `commands-runtime-effect-shader-named-child-count-fallback` row corrupts a RuntimeEffect shader descriptor
  `namedChildCount` slot to one more than `childCount`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_NAMED_CHILD_COUNT_CORRUPTED`, and reaches JBR's named-child-count bound
  parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect shader named-uniform-count sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-203131/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers seventeen RuntimeEffect-invalid rows with all rows
  passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-203219/suite.tsv`.
  The new `commands-runtime-effect-shader-named-uniform-count-fallback` row corrupts a RuntimeEffect shader descriptor
  `namedUniformCount` slot to `17`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_NAMED_UNIFORM_COUNT_CORRUPTED`, and reaches JBR's named-uniform-count
  upper-bound parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect shader child-count sentinel. The single-row
  focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-201222/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers sixteen RuntimeEffect-invalid rows with all rows passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-201314/suite.tsv`.
  The new `commands-runtime-effect-shader-child-count-fallback` row corrupts a RuntimeEffect shader descriptor
  `childCount` slot to `9`, requires `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_CHILD_COUNT_CORRUPTED`, and reaches
  JBR's child-count upper-bound parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect shader uniform-float-count sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-195420/suite.tsv`.
  The `CASE_GROUPS=runtime-effect-invalid` subset now covers fifteen RuntimeEffect-invalid rows with all rows passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-195512/suite.tsv`.
  The new `commands-runtime-effect-shader-uniform-float-count-fallback` row corrupts a RuntimeEffect shader descriptor
  `uniformFloatCount` slot to `257`, requires
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_UNIFORM_FLOAT_COUNT_CORRUPTED`, and reaches JBR's uniform-count upper-bound
  parser guard before schema validation or native compile.
- Focused plus grouped validation passed after adding a live RuntimeEffect shader SKSL-length sentinel. The single-row
  focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-192212/suite.tsv`.
  After tightening `CASE_GROUPS` expansion to avoid duplicating migrated default rows, the
  `CASE_GROUPS=runtime-effect-invalid` subset covered fourteen RuntimeEffect-invalid rows with all rows passing:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-193737/suite.tsv`.
  The new `commands-runtime-effect-shader-sksl-length-fallback` row corrupts a RuntimeEffect shader descriptor
  `skslLength` slot to `0`, requires `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_SKSL_LENGTH_CORRUPTED`, and reaches
  JBR's positive SKSL-length parser guard before source hashing or native compile.
- Focused plus grouped validation passed after adding a live shader color-filter descriptor payload-count sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-185920/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers twenty-nine malformed shader-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and twenty-nine expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-190002/suite.tsv`.
  The new `commands-invalid-shader-color-filter-descriptor-payload-count-fallback` row corrupts a shader color-filter
  descriptor payload count to `5` while keeping record length consistent, requires
  `SKIKO_JBR_INTEROP_SHADER_COLOR_FILTER_DESCRIPTOR_PAYLOAD_COUNT_CORRUPTED`, and reaches JBR's
  shader-color-filter-specific `payloadIntCount == 4` rejection.
- Focused plus grouped validation passed after adding a live solid color shader descriptor payload-count sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-183751/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers twenty-eight malformed shader-descriptor live
  sentinels with all rows passing, zero JBR picture/command frames, and twenty-eight expected explicit fallback-marker
  rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-183835/suite.tsv`.
  The new `commands-invalid-color-shader-descriptor-payload-count-fallback` row corrupts a solid color shader
  descriptor payload count to `2` while keeping record length consistent, requires
  `SKIKO_JBR_INTEROP_COLOR_SHADER_DESCRIPTOR_PAYLOAD_COUNT_CORRUPTED`, and reaches JBR's color-specific
  `payloadIntCount == 1` rejection.
- Focused plus grouped validation passed after adding a live composite shader descriptor blend-mode sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-181627/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers twenty-seven malformed shader-descriptor live
  sentinels with all rows passing, zero JBR picture/command frames, and twenty-seven expected explicit fallback-marker
  rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-181710/suite.tsv`.
  The new `commands-invalid-composite-shader-descriptor-blend-mode-fallback` row corrupts a composite shader descriptor
  blend-mode slot to `99`, requires `SKIKO_JBR_INTEROP_COMPOSITE_SHADER_DESCRIPTOR_BLEND_MODE_CORRUPTED`, and reaches
  JBR's composite-specific `isSupportedBlendMode(blendMode)` rejection while keeping descriptor payload count intact.
- Focused plus grouped validation passed after adding a live chain path-effect descriptor payload-count sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-175121/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset now covers twenty-two malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and twenty-two expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-175219/suite.tsv`.
  The new `commands-invalid-chain-path-effect-descriptor-payload-count-fallback` row corrupts a chain path-effect
  descriptor payload count to `5` while keeping record length consistent, requires
  `SKIKO_JBR_INTEROP_CHAIN_PATH_EFFECT_DESCRIPTOR_PAYLOAD_COUNT_CORRUPTED`, and reaches JBR's chain-specific
  `payloadIntCount == 4` rejection.
- Focused plus grouped validation passed after adding a live lighting color-filter descriptor payload-count sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-173102/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset now covers twenty-one malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and twenty-one expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-173150/suite.tsv`.
  The new `commands-invalid-lighting-filter-descriptor-payload-count-fallback` row corrupts a lighting color-filter
  descriptor payload count to `3` while keeping record length consistent, requires
  `SKIKO_JBR_INTEROP_LIGHTING_FILTER_DESCRIPTOR_PAYLOAD_COUNT_CORRUPTED`, and reaches JBR's lighting-specific
  payload-count rejection rather than the generic descriptor metadata gate.
- Focused plus grouped validation passed after adding a live image shader descriptor height upper-bound sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-170405/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers twenty-six malformed shader-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and twenty-six expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-170449/suite.tsv`.
  The new `commands-invalid-image-shader-descriptor-max-height-fallback` row corrupts the image shader height slot to
  `4097`, requires `SKIKO_JBR_INTEROP_IMAGE_SHADER_DESCRIPTOR_MAX_HEIGHT_CORRUPTED`, and completes live image-dimension
  upper-bound coverage for width and height.
- Focused plus grouped validation passed after adding a live image shader descriptor width upper-bound sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-164502/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers twenty-five malformed shader-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and twenty-five expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-164545/suite.tsv`.
  The new `commands-invalid-image-shader-descriptor-max-width-fallback` row corrupts the image shader width slot to
  `4097`, requires `SKIKO_JBR_INTEROP_IMAGE_SHADER_DESCRIPTOR_MAX_WIDTH_CORRUPTED`, and matches JBR's image-width
  upper-bound validation.
- Focused plus grouped validation passed after adding a live Perlin/noise shader descriptor negative tile-height
  sentinel. The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-162500/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers twenty-four malformed shader-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and twenty-four expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-162542/suite.tsv`.
  The new `commands-invalid-perlin-noise-shader-negative-tile-height-fallback` row corrupts the Perlin/noise
  tile-height slot to `-1`, requires `SKIKO_JBR_INTEROP_PERLIN_NOISE_SHADER_NEGATIVE_TILE_HEIGHT_CORRUPTED`, and
  matches JBR's non-negative tile-height validation.
- Focused plus grouped validation passed after adding a live Perlin/noise shader descriptor tile-height upper-bound
  sentinel. The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-160431/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers twenty-three malformed shader-descriptor live
  sentinels with all rows passing, zero JBR picture/command frames, and twenty-three expected explicit fallback-marker
  rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-160516/suite.tsv`.
  The new `commands-invalid-perlin-noise-shader-tile-height-fallback` row corrupts the Perlin/noise tile-height slot
  to `4097`, requires `SKIKO_JBR_INTEROP_PERLIN_NOISE_SHADER_TILE_HEIGHT_CORRUPTED`, and matches JBR's tile-height
  upper-bound validation.
- Focused plus grouped validation passed after adding a live sweep-gradient shader descriptor stop-order sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-154421/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers twenty-two malformed shader-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and twenty-two expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-154510/suite.tsv`.
  The new `commands-invalid-sweep-gradient-shader-descriptor-stop-order-fallback` row uses the descriptor-backed
  sweep-gradient shader-plus-color-filter probe, requires
  `SKIKO_JBR_INTEROP_SWEEP_GRADIENT_SHADER_DESCRIPTOR_STOP_ORDER_CORRUPTED`, and completes live stop-order coverage
  for linear/radial/sweep gradient shader descriptors.
- Focused plus grouped validation passed after adding a live radial-gradient shader descriptor stop-order sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-152642/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers twenty-one malformed shader-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and twenty-one expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-152730/suite.tsv`.
  The new `commands-invalid-radial-gradient-shader-descriptor-stop-order-fallback` row uses the descriptor-backed
  composite shader probe, requires `SKIKO_JBR_INTEROP_RADIAL_GRADIENT_SHADER_DESCRIPTOR_STOP_ORDER_CORRUPTED`, and
  matches JBR's strictly-increasing gradient-stop validation.
- Focused plus grouped validation passed after adding a live linear-gradient shader descriptor stop-order sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-150917/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers twenty malformed shader-descriptor live sentinels with
  all rows passing, zero JBR picture/command frames, and twenty expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-151002/suite.tsv`.
  The new `commands-invalid-linear-gradient-shader-descriptor-stop-order-fallback` row uses the descriptor-backed
  linear-gradient shader-plus-color-filter probe, requires
  `SKIKO_JBR_INTEROP_LINEAR_GRADIENT_SHADER_DESCRIPTOR_STOP_ORDER_CORRUPTED`, and matches JBR's strictly-increasing
  gradient-stop validation.
- Full default command-probe sweep passed after adding the image shader descriptor height and X/Y tile-mode sentinels.
  The sweep covered 206/206 passing rows: 110 command replay rows, 26 intentional picture-fallback rows, and 70
  explicit structured fallback rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-124900/suite.tsv`.
- Focused plus grouped validation passed after adding a live image shader descriptor Y tile-mode sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-123503/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers nineteen malformed shader-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and nineteen expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-123547/suite.tsv`.
  The new `commands-invalid-image-shader-descriptor-tile-mode-y-fallback` row uses the descriptor-backed
  image-shader-plus-color-filter probe, requires
  `SKIKO_JBR_INTEROP_IMAGE_SHADER_DESCRIPTOR_TILE_MODE_Y_CORRUPTED`, and completes live X/Y image shader tile-mode
  range sentinel coverage.
- Focused plus grouped validation passed after adding a live image shader descriptor X tile-mode sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-122008/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers eighteen malformed shader-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and eighteen expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-122053/suite.tsv`.
  The new `commands-invalid-image-shader-descriptor-tile-mode-x-fallback` row uses the descriptor-backed
  image-shader-plus-color-filter probe, requires
  `SKIKO_JBR_INTEROP_IMAGE_SHADER_DESCRIPTOR_TILE_MODE_X_CORRUPTED`, and matches JBR's image shader tile-mode range
  rejection.
- Focused plus grouped validation passed after adding a live image shader descriptor height sentinel. The single-row
  focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-120348/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers seventeen malformed shader-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and seventeen expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-120436/suite.tsv`.
  The new `commands-invalid-image-shader-descriptor-height-fallback` row uses the descriptor-backed
  image-shader-plus-color-filter probe, requires `SKIKO_JBR_INTEROP_IMAGE_SHADER_DESCRIPTOR_HEIGHT_CORRUPTED`, and
  matches JBR's image shader positive-height rejection. This run used the quick exact-row then area-group iteration
  path; the previous full default command-probe checkpoint remains the 203/203 sweep below.
- Focused, grouped, and full default command-probe validation passed after adding a live sweep-gradient shader
  descriptor color-count sentinel. The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-092309/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers sixteen malformed shader-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and sixteen expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-092408/suite.tsv`.
  The full default sweep passed 203/203 rows: 110 command replay rows, 26 intentional picture-fallback rows, and
  67 explicit structured fallback rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260515-093643/suite.tsv`.
  The new `commands-invalid-sweep-gradient-shader-descriptor-color-count-fallback` row uses a descriptor-backed
  sweep-gradient shader-plus-color-filter probe, requires
  `SKIKO_JBR_INTEROP_SWEEP_GRADIENT_SHADER_DESCRIPTOR_COLOR_COUNT_CORRUPTED`, and matches JBR's gradient stop
  color-count bounds/record-length rejection.
- Focused plus grouped validation passed after adding a live radial-gradient shader descriptor tile-mode sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-234201/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers fifteen malformed shader-descriptor live sentinels with
  all rows passing, zero JBR picture/command frames, and fifteen expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-234247/suite.tsv`.
  The new `commands-invalid-radial-gradient-shader-descriptor-tile-mode-fallback` row uses the descriptor-backed
  composite shader probe, requires `SKIKO_JBR_INTEROP_RADIAL_GRADIENT_SHADER_DESCRIPTOR_TILE_MODE_CORRUPTED`, and
  matches JBR's radial tile-mode range rejection.
- Focused plus grouped validation passed after adding a live linear-gradient shader descriptor tile-mode sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-232913/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers fourteen malformed shader-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and fourteen expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-233003/suite.tsv`.
  The new `commands-invalid-linear-gradient-shader-descriptor-tile-mode-fallback` row uses the descriptor-backed
  linear-gradient shader-plus-color-filter probe, requires
  `SKIKO_JBR_INTEROP_LINEAR_GRADIENT_SHADER_DESCRIPTOR_TILE_MODE_CORRUPTED`, and matches JBR's tile-mode range
  rejection.
- Full default command-probe sweep passed after the radial-gradient radius and image shader width sentinel batch. The
  sweep covered 200/200 passing rows: 110 command replay rows, 26 intentional picture-fallback rows, and 64 explicit
  structured fallback rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-211318/suite.tsv`.
- Focused plus grouped validation passed after adding a live radial-gradient shader descriptor radius sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-210305/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers thirteen malformed shader-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and thirteen expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-210352/suite.tsv`.
  The new `commands-invalid-radial-gradient-shader-descriptor-radius-fallback` row uses the descriptor-backed
  composite shader probe, requires `SKIKO_JBR_INTEROP_RADIAL_GRADIENT_SHADER_DESCRIPTOR_RADIUS_CORRUPTED`, and matches
  JBR's radial-gradient positive-radius rejection.
- Focused plus grouped validation passed after adding a live image shader descriptor width sentinel. The single-row
  focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-205054/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset now covers twelve malformed shader-descriptor live sentinels with
  all rows passing, zero JBR picture/command frames, and twelve expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-205138/suite.tsv`.
  The new `commands-invalid-image-shader-descriptor-width-fallback` row uses the descriptor-backed
  image-shader-plus-color-filter probe, requires `SKIKO_JBR_INTEROP_IMAGE_SHADER_DESCRIPTOR_WIDTH_CORRUPTED`, and
  matches JBR's image shader positive-width rejection.
- Malformed descriptor iteration now prefers exact `CASES=...` rows followed by curated `CASE_GROUPS=...` area sweeps
  before periodic full default command-probe checkpoints.
- Focused plus grouped validation passed after adding a live Perlin/noise shader descriptor zero-octaves sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-203437/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset then covered eleven malformed shader-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and eleven expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-203549/suite.tsv`.
  The new `commands-invalid-perlin-noise-shader-zero-octaves-fallback` row required
  `SKIKO_JBR_INTEROP_PERLIN_NOISE_SHADER_ZERO_OCTAVES_CORRUPTED` and matched JBR's Perlin octave-count lower-bound
  rejection.
- Focused plus grouped validation passed after adding a live Perlin/noise shader descriptor negative tile-size
  sentinel. The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-202424/suite.tsv`.
  The `CASE_GROUPS=shader-descriptor-invalid` subset then covered ten malformed shader-descriptor live sentinels with
  all rows passing, zero JBR picture/command frames, and ten expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-202513/suite.tsv`.
  The new `commands-invalid-perlin-noise-shader-negative-tile-size-fallback` row required
  `SKIKO_JBR_INTEROP_PERLIN_NOISE_SHADER_NEGATIVE_TILE_SIZE_CORRUPTED` and matched JBR's Perlin tile-size lower-bound
  rejection.
- Full default command-probe sweep passed after the blur/stamped/corner finite-bound sentinel batch. The sweep covered
  196/196 passing rows: 110 command replay rows, 26 intentional picture-fallback rows, and 60 explicit structured
  fallback rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-180227/suite.tsv`.
- Focused plus grouped validation passed after adding a live stamped path-effect descriptor negative path-data-length
  sentinel. The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-174121/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset then covered twenty malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and twenty expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-174214/suite.tsv`.
  The new `commands-invalid-stamped-path-effect-descriptor-negative-path-data-length-fallback` row required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_NEGATIVE_PATH_DATA_LENGTH_CORRUPTED` and matched JBR's stamped
  path-effect non-negative path-data-length rejection.
- Focused plus grouped validation passed after adding a live blur image-filter descriptor negative-sigma sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-172401/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset then covered nineteen malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and nineteen expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-172454/suite.tsv`.
  The new `commands-invalid-blur-image-filter-descriptor-negative-sigma-fallback` row required
  `SKIKO_JBR_INTEROP_BLUR_IMAGE_FILTER_DESCRIPTOR_NEGATIVE_SIGMA_CORRUPTED` and matched JBR's blur non-negative sigma
  rejection.
- Focused plus grouped validation passed after adding a live corner path-effect descriptor negative-radius sentinel.
  The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-170802/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset then covered eighteen malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and eighteen expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-170922/suite.tsv`.
  The new `commands-invalid-corner-path-effect-descriptor-negative-radius-fallback` row required
  `SKIKO_JBR_INTEROP_CORNER_PATH_EFFECT_DESCRIPTOR_NEGATIVE_RADIUS_CORRUPTED` and matched JBR's corner path-effect
  non-negative radius rejection.
- Focused plus grouped validation passed after adding a live stamped path-effect descriptor negative-phase sentinel.
  The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-165312/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset then covered seventeen malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and seventeen expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-165402/suite.tsv`.
  The new `commands-invalid-stamped-path-effect-descriptor-negative-phase-fallback` row required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_NEGATIVE_PHASE_CORRUPTED` and matched JBR's stamped path-effect
  non-negative phase rejection.
- Focused plus grouped validation passed after adding a live stamped path-effect descriptor zero-advance sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-163606/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset then covered sixteen malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and sixteen expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-163650/suite.tsv`.
  The new `commands-invalid-stamped-path-effect-descriptor-zero-advance-fallback` row required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_ZERO_ADVANCE_CORRUPTED` and matched JBR's stamped path-effect
  positive-advance rejection.
- Focused plus grouped validation passed after adding a live blur image-filter descriptor tile-mode sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-161649/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset then covered fifteen malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and fifteen expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-161742/suite.tsv`.
  The new `commands-invalid-blur-image-filter-descriptor-tile-mode-fallback` row required
  `SKIKO_JBR_INTEROP_BLUR_IMAGE_FILTER_DESCRIPTOR_TILE_MODE_CORRUPTED` and matched JBR's blur tile-mode bounds
  rejection.
- Full default command-probe sweep passed after the stamped path-effect descriptor payload hardening. The sweep covered
  190/190 passing rows: 110 command replay rows, 26 intentional picture-fallback rows, and 54 explicit structured
  fallback rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-140549/suite.tsv`.
- Focused plus grouped validation passed after adding a live stamped path-effect descriptor path-data-length sentinel.
  The single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-135433/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset then covered fourteen malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and fourteen expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-135519/suite.tsv`.
  The new `commands-invalid-stamped-path-effect-descriptor-path-data-length-fallback` row required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_PATH_DATA_LENGTH_CORRUPTED` and matched JBR's stamped path-effect
  path-data-length rejection.
- Focused plus grouped validation passed after adding a live stamped path-effect descriptor fill-type sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-134123/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset then covered thirteen malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and thirteen expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-134207/suite.tsv`.
  The new `commands-invalid-stamped-path-effect-descriptor-fill-type-fallback` row required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_FILL_TYPE_CORRUPTED` and matched JBR's stamped path-effect
  fill-type rejection.
- Focused plus grouped validation passed after adding a live stamped path-effect descriptor style sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-132932/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset then covered twelve malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and twelve expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-133018/suite.tsv`.
  The new `commands-invalid-stamped-path-effect-descriptor-style-fallback` row required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_STYLE_CORRUPTED` and matched JBR's stamped path-effect style
  bounds rejection.
- Focused plus grouped validation passed after adding a live stamped path-effect descriptor phase sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-131759/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset then covered eleven malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and eleven expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-131846/suite.tsv`.
  The new `commands-invalid-stamped-path-effect-descriptor-phase-fallback` row required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_PHASE_CORRUPTED` and matched JBR's finite/non-negative stamped
  path-effect phase payload rejection.
- Focused plus grouped validation passed after adding a live stamped path-effect descriptor advance sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-130510/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset then covered ten malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and ten expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-130556/suite.tsv`.
  The new `commands-invalid-stamped-path-effect-descriptor-advance-fallback` row required
  `SKIKO_JBR_INTEROP_STAMPED_PATH_EFFECT_DESCRIPTOR_ADVANCE_CORRUPTED` and matched JBR's finite/positive stamped
  path-effect advance payload rejection.
- Focused plus grouped validation passed after adding a live corner path-effect descriptor radius sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-125458/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset then covered nine malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and nine expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-125544/suite.tsv`.
  The new `commands-invalid-corner-path-effect-descriptor-radius-fallback` row required
  `SKIKO_JBR_INTEROP_CORNER_PATH_EFFECT_DESCRIPTOR_RADIUS_CORRUPTED` and matched JBR's non-finite/non-negative corner
  radius payload rejection.
- Focused plus grouped validation passed after adding a live offset image-filter descriptor delta sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-124549/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset then covered eight malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and eight expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-124635/suite.tsv`.
  The new `commands-invalid-offset-image-filter-descriptor-delta-fallback` row required
  `SKIKO_JBR_INTEROP_OFFSET_IMAGE_FILTER_DESCRIPTOR_DELTA_CORRUPTED` and matched JBR's non-finite offset delta payload
  rejection.
- Focused plus grouped validation passed after adding a live blur image-filter descriptor sigma sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-123706/suite.tsv`.
  The `CASE_GROUPS=effect-descriptor-invalid` subset then covered seven malformed effect-descriptor live sentinels
  with all rows passing, zero JBR picture/command frames, and seven expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-123801/suite.tsv`.
  The new `commands-invalid-blur-image-filter-descriptor-sigma-fallback` row required
  `SKIKO_JBR_INTEROP_BLUR_IMAGE_FILTER_DESCRIPTOR_SIGMA_CORRUPTED` and matched JBR's non-finite blur sigma payload
  rejection.
- Focused plus grouped validation passed after adding a live color-matrix filter descriptor payload sentinel. The
  single-row focused run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-102259/suite.tsv`.
  The new `CASE_GROUPS=effect-descriptor-invalid` subset then covered all six malformed effect-descriptor live
  sentinels with all rows passing, zero JBR picture/command frames, and six expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-122441/suite.tsv`.
  The new `commands-invalid-color-matrix-filter-descriptor-payload-fallback` row required
  `SKIKO_JBR_INTEROP_COLOR_MATRIX_FILTER_DESCRIPTOR_PAYLOAD_CORRUPTED` and matched JBR parser-only non-finite
  color-matrix payload rejection.
- Full default command-probe sweep passed after adding a live tint color-filter descriptor blend-mode validation
  sentinel. The sweep covered 181 rows plus header with all rows passing, 110 command replay rows, 26 intentional JBR
  picture fallback rows, and 45 expected explicit fallback-marker rows. The new
  `commands-invalid-tint-color-filter-descriptor-blend-mode-fallback` row required
  `SKIKO_JBR_INTEROP_TINT_COLOR_FILTER_DESCRIPTOR_BLEND_MODE_CORRUPTED`, recorded one `command-stream-invalid`
  fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-081633/suite.tsv`.
- Focused validation for the same tint color-filter descriptor blend-mode sentinel passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-081541/suite.tsv`.
- Full default command-probe sweep passed after adding live Perlin/noise shader descriptor payload validation
  sentinels. The sweep covered 180 rows plus header with all rows passing, 110 command replay rows, 26 intentional JBR
  picture fallback rows, and 44 expected explicit fallback-marker rows. The new
  `commands-invalid-perlin-noise-shader-kind-fallback`,
  `commands-invalid-perlin-noise-shader-frequency-fallback`,
  `commands-invalid-perlin-noise-shader-octaves-fallback`, and
  `commands-invalid-perlin-noise-shader-tile-size-fallback` rows each required their typed Skiko corruption marker,
  recorded one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR
  command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-025633/suite.tsv`.
- Focused validation for the same four Perlin/noise shader descriptor payload sentinels passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-025354/suite.tsv`.
- Full default command-probe sweep passed after adding a live RuntimeEffect shader source-hash mismatch sentinel. The
  sweep covered 176 rows plus header with all rows passing, 110 command replay rows, 26 intentional JBR picture
  fallback rows, and 40 expected explicit fallback-marker rows. The new
  `commands-runtime-effect-shader-source-hash-fallback` row required
  `SKIKO_JBR_INTEROP_RUNTIME_EFFECT_SHADER_SOURCE_HASH_CORRUPTED`, recorded one `command-stream-invalid` fallback
  marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-005156/suite.tsv`.
- Focused validation for the same RuntimeEffect shader source-hash sentinel passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260514-005110/suite.tsv`.
- Full default command-probe sweep passed after adding a live transformed shader descriptor payload-count mismatch
  sentinel. The sweep covered 175 rows plus header with all rows passing, 110 command replay rows, 26 intentional JBR
  picture fallback rows, and 39 expected explicit fallback-marker rows. The new
  `commands-invalid-transformed-shader-descriptor-payload-count-fallback` row required
  `SKIKO_JBR_INTEROP_TRANSFORMED_SHADER_DESCRIPTOR_PAYLOAD_COUNT_CORRUPTED`, recorded one
  `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-224940/suite.tsv`.
- Focused validation for the same transformed shader descriptor payload-count sentinel passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-224856/suite.tsv`.
- Full default command-probe sweep passed after adding a live unknown effect descriptor type sentinel. The sweep covered
  174 rows plus header with all rows passing, 110 command replay rows, 26 intentional JBR picture fallback rows, and
  38 expected explicit fallback-marker rows. The new `commands-invalid-effect-descriptor-type-fallback` row required
  `SKIKO_JBR_INTEROP_EFFECT_DESCRIPTOR_TYPE_CORRUPTED`, recorded one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-204652/suite.tsv`.
- Focused validation for the same unknown effect descriptor type sentinel passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-204604/suite.tsv`.
- Full default command-probe sweep passed after adding live effect descriptor version, payload-count, and
  record-length corruption sentinels. The sweep covered 173 rows plus header with all rows passing, 110 command replay
  rows, 26 intentional JBR picture fallback rows, and 37 expected explicit fallback-marker rows. The new
  `commands-invalid-effect-descriptor-version-fallback`,
  `commands-invalid-effect-descriptor-payload-count-fallback`, and
  `commands-invalid-effect-descriptor-record-length-fallback` rows each required their typed Skiko corruption marker,
  recorded one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and zero JBR
  command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-183706/suite.tsv`.
- Focused validation for the same three effect descriptor metadata sentinels passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-183454/suite.tsv`.
- Full default command-probe sweep passed after tightening the existing shader descriptor version sentinel to require
  its typed Skiko corruption marker. The sweep covered 170 rows plus header with all rows passing, 110 command replay
  rows, 26 intentional JBR picture fallback rows, and 34 expected explicit fallback-marker rows. The
  `commands-invalid-descriptor-version-fallback` row now requires
  `SKIKO_JBR_INTEROP_DESCRIPTOR_VERSION_CORRUPTED`, recorded one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-165558/suite.tsv`.
- Focused validation for the same descriptor-version marker gate passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-165524/suite.tsv`.
- Full default command-probe sweep passed after adding a live shader descriptor record-length mismatch sentinel. The
  sweep covered 170 rows plus header with all rows passing, 110 command replay rows, 26 intentional JBR picture
  fallback rows, and 34 expected explicit fallback-marker rows. The new
  `commands-invalid-shader-descriptor-record-length-fallback` row required
  `SKIKO_JBR_INTEROP_SHADER_DESCRIPTOR_RECORD_LENGTH_CORRUPTED`, recorded one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-153044/suite.tsv`.
- Focused validation for the same record-length sentinel passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-153009/suite.tsv`.
- Full default command-probe sweep passed after adding a live shader descriptor payload-count mismatch sentinel. The
  sweep covered 169 rows plus header with all rows passing, 110 command replay rows, 26 intentional JBR picture
  fallback rows, and 33 expected explicit fallback-marker rows. The new
  `commands-invalid-shader-descriptor-payload-count-fallback` row required
  `SKIKO_JBR_INTEROP_SHADER_DESCRIPTOR_PAYLOAD_COUNT_CORRUPTED`, recorded one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-140412/suite.tsv`.
- Focused validation for the same payload-count sentinel passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260513-140244/suite.tsv`.
- Full default command-probe sweep passed after adding a live unknown shader descriptor type sentinel. The sweep
  covered 168 rows plus header with all rows passing, 110 command replay rows, 26 intentional JBR picture fallback
  rows, and 32 expected explicit fallback-marker rows. The new
  `commands-invalid-shader-descriptor-type-fallback` row required
  `SKIKO_JBR_INTEROP_SHADER_DESCRIPTOR_TYPE_CORRUPTED`, recorded one `command-stream-invalid` fallback marker,
  `unsupported=none`, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-230029/suite.tsv`.
- Focused validation for the same unknown shader descriptor type sentinel passed before the full sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-225953/suite.tsv`.
- Full screenshot parity sweep passed after the RuntimeEffect shader+color-filter lifecycle gate and focused parity
  refresh. The sweep covered 106 rows plus header with all rows passing, all 106 rows staying on JBR command replay,
  and zero rows reporting fallback or JBR picture replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260512-204525/suite.tsv`.
- Full default command-probe sweep passed after tightening the `commands-runtime-effect-shader-color-filter` lifecycle
  gate to require at most one JBR effect-handle definition for the descriptor-backed color-filter side. The sweep
  covered 167 rows plus header with all rows passing, 110 command replay rows, 26 intentional JBR picture fallback
  rows, and 31 expected explicit fallback-marker rows. The tightened row recorded one effect-handle definition, 946
  effect-handle uses, 945 effect-handle cache hits, 945 RuntimeEffect source-cache hits, one RuntimeEffect source-cache
  miss, zero JBR picture frames, and 582 JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-191321/suite.tsv`.
- Focused screenshot parity for the same RuntimeEffect shader+color-filter row passed with zero fallback, zero JBR
  picture frames, 797 JBR command frames, `avg_delta=2.110`, `bad_pixel_ratio=0.05006`, and
  `compose_shader_linear_bad_pixel_ratio=0.06584`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260512-204143/suite.tsv`.
- Full compatibility matrix passed after promoting the invalid-handle sentinels into the default command suite. The
  matrix covered 57 rows plus header: all rows passed, the happy-path row replayed commands, the 56 ABI/capability/API
  mismatch rows reported fallback with zero JBR command frames, and every row used a background probe window:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260512-183854/matrix.tsv`.
- Full default command-probe sweep passed after promoting focused invalid-handle sentinels into the default case list.
  The sweep covered 167 rows plus header with all rows passing, 110 command replay rows, 26 intentional JBR picture
  fallback rows, and 31 expected explicit fallback-marker rows. The promoted rows cover undefined shader descriptor
  use, color-filter descriptor use-after-evict, effect-child use-after-evict, effect-child missing handles, and shader
  child missing handles:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-171413/suite.tsv`.
- Full default command-probe sweep passed after tightening the descriptor undefined-use default row to require the typed
  Skiko corruption marker. The sweep covered 156 rows plus header with all rows passing, 110 command replay rows, 26
  intentional JBR picture fallback rows, and 20 expected explicit fallback-marker rows. The tightened
  `commands-invalid-descriptor-use-fallback` row required
  `SKIKO_JBR_INTEROP_DESCRIPTOR_USE_CORRUPTED op=47`, recorded one `command-stream-invalid` fallback marker, and
  produced zero JBR picture/command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-155150/suite.tsv`.
- Focused undefined shader descriptor use sentinel passed. The
  `commands-invalid-shader-descriptor-use-fallback` row rewrites a shader ref to an undefined handle, requires
  `SKIKO_JBR_INTEROP_DESCRIPTOR_USE_CORRUPTED op=58`, and falls back with `command-stream-invalid` before JBR replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-155027/suite.tsv`.
- Focused descriptor use-after-evict subset passed for both shader and color-filter refs. The existing
  `commands-invalid-descriptor-use-after-evict-fallback` row now requires
  `SKIKO_JBR_INTEROP_DESCRIPTOR_USE_AFTER_EVICT_CORRUPTED op=58`, and the new
  `commands-invalid-color-filter-descriptor-use-after-evict-fallback` row requires the same marker with `op=47`.
  Both rows recorded one `command-stream-invalid` fallback marker, `unsupported=none`, zero JBR picture frames, and
  zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-142300/suite.tsv`.
- Direct parser validation passed against the existing `JBRSkiaApiTest` coverage for undefined shader/effect descriptor
  uses and evicted shader/effect descriptor handles.
- Focused effect descriptor missing-child sentinels passed. The
  `commands-runtime-effect-color-filter-child-missing-fallback`,
  `commands-offset-image-filter-child-missing-fallback`, `commands-chain-path-effect-child-missing-fallback`, and
  `commands-shader-color-filter-effect-child-missing-fallback` rows rewrite an effect-child slot to an undefined
  effect handle, require `SKIKO_JBR_INTEROP_EFFECT_CHILD_MISSING_CORRUPTED target=...`, and fall back with
  `command-stream-invalid` before JBR replay. All four rows recorded one fallback marker, `unsupported=none`, zero JBR
  picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-141506/suite.tsv`.
- Direct parser validation passed against the existing `JBRSkiaApiTest` coverage for missing RuntimeEffect
  color-filter child handles, missing offset image-filter child handles, missing chained path-effect child handles, and
  missing shader-color-filter effect handles.
- Focused shader descriptor missing-child sentinels passed. The
  `commands-transformed-shader-child-missing-fallback`,
  `commands-composite-shader-child-missing-fallback`, and
  `commands-shader-color-filter-shader-child-missing-fallback` rows rewrite a descriptor child slot to an undefined
  shader handle, require `SKIKO_JBR_INTEROP_SHADER_CHILD_MISSING_CORRUPTED target=...`, and fall back with
  `command-stream-invalid` before JBR replay. All three rows recorded one fallback marker, `unsupported=none`, zero
  JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-140546/suite.tsv`.
- Direct parser validation passed against the existing `JBRSkiaApiTest` coverage for missing transformed-shader child
  handles, missing composite-shader child handles, and missing shader-color-filter shader handles. The test was run
  headlessly with the patched desktop module and native bridge.
- Focused evicted effect-child handle sentinels passed. The
  `commands-invalid-effect-child-use-after-evict-fallback` and
  `commands-invalid-path-effect-child-use-after-evict-fallback` rows insert a child-handle eviction immediately before
  the parent offset image-filter or chained path-effect descriptor, require
  `SKIKO_JBR_INTEROP_EFFECT_CHILD_USE_AFTER_EVICT_CORRUPTED target=...`, and fall back with
  `command-stream-invalid` before JBR replay. Both rows recorded one fallback marker, `unsupported=none`, zero JBR
  picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260512-135408/suite.tsv`.
- Direct parser validation passed against the existing `JBRSkiaApiTest` coverage for evicted offset image-filter child
  handles and evicted chained path-effect child handles. The test was compiled against `/tmp/jbr-skia-run/desktop` and
  run headlessly with the patched desktop module and native bridge.
- Full default command-probe sweep passed after adding the chained path-effect child wrong-type handle sentinel. The new
  `commands-chain-path-effect-child-wrong-effect-type-fallback` row rewrites the chained path-effect child handle to a
  color-filter descriptor, requires
  `SKIKO_JBR_INTEROP_PATH_EFFECT_HANDLE_TYPE_CORRUPTED target=chainPathEffectChild`, and falls back with
  `command-stream-invalid` before JBR replay. The sweep covered 156 rows plus header with all rows passing, 110 command
  replay rows, 26 intentional JBR picture fallback rows, and 20 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-205714/suite.tsv`.
- Direct parser validation passed against the existing `JBRSkiaApiTest` coverage for a chained path-effect descriptor
  whose child path-effect handle points at a color-filter descriptor. The test was compiled against
  `/tmp/jbr-skia-run/desktop` and run headlessly with the patched desktop module and native bridge.
- Focused chained path-effect child wrong-type sentinel passed before the full sweep, recording one
  `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-205629/suite.tsv`.
- Full default command-probe sweep passed after adding the offset image-filter child wrong-type handle sentinel. The new
  `commands-offset-image-filter-child-wrong-effect-type-fallback` row rewrites the chained render-effect offset
  image-filter child handle to a color-filter descriptor, requires
  `SKIKO_JBR_INTEROP_IMAGE_FILTER_HANDLE_TYPE_CORRUPTED target=offsetImageFilterChild`, and falls back with
  `command-stream-invalid` before JBR replay. The sweep covered 155 rows plus header with all rows passing, 110 command
  replay rows, 26 intentional JBR picture fallback rows, and 19 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-192524/suite.tsv`.
- Direct parser validation passed against the existing `JBRSkiaApiTest` coverage for an offset image-filter descriptor
  whose child image-filter handle points at a color-filter descriptor. The test was compiled against
  `/tmp/jbr-skia-run/desktop` and run headlessly with the patched desktop module and native bridge.
- Focused offset image-filter child wrong-type sentinel passed before the full sweep, recording one
  `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-192441/suite.tsv`.
- Full default command-probe sweep passed after adding the RuntimeEffect color-filter child wrong-type handle sentinel.
  The new `commands-runtime-effect-color-filter-child-wrong-effect-type-fallback` row rewrites the first
  RuntimeEffect color-filter child handle to an image-filter descriptor supplied by the graphics-layer render-effect
  scene, requires `SKIKO_JBR_INTEROP_COLOR_FILTER_HANDLE_TYPE_CORRUPTED target=runtimeEffectColorFilterChild`, and
  falls back with `command-stream-invalid` before JBR replay. The sweep covered 154 rows plus header with all rows
  passing, 110 command replay rows, 26 intentional JBR picture fallback rows, and 18 expected explicit fallback-marker
  rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-174517/suite.tsv`.
- Direct parser validation passed against the existing `JBRSkiaApiTest` coverage for a RuntimeEffect color-filter
  descriptor whose color-filter child handle points at an image-filter descriptor. The test was compiled against
  `/tmp/jbr-skia-run/desktop` and run headlessly with the patched desktop module and native bridge.
- Focused RuntimeEffect color-filter child wrong-type sentinel passed before the full sweep, recording one
  `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-174429/suite.tsv`.
- Full default command-probe sweep passed after adding the RuntimeEffect shader child wrong-type handle sentinel. The
  new `commands-runtime-effect-shader-child-wrong-effect-type-fallback` row rewrites the first RuntimeEffect shader
  child handle to a color-filter descriptor, requires
  `SKIKO_JBR_INTEROP_SHADER_HANDLE_TYPE_CORRUPTED target=runtimeEffectShaderChild`, and falls back with
  `command-stream-invalid` before JBR replay. The sweep covered 153 rows plus header with all rows passing, 110 command
  replay rows, 26 intentional JBR picture fallback rows, and 17 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-161739/suite.tsv`.
- Direct parser validation passed after adding `JBRSkiaApiTest` coverage for a RuntimeEffect shader descriptor whose
  child shader handle points at a color-filter descriptor. The test was compiled against `/tmp/jbr-skia-run/desktop`
  and run headlessly with the patched desktop module and native bridge.
- Focused RuntimeEffect shader child wrong-type sentinel passed before the full sweep, recording one
  `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-161650/suite.tsv`.
- Full default command-probe sweep passed after adding the composite shader child wrong-type handle sentinel. The new
  `commands-composite-shader-child-wrong-effect-type-fallback` row rewrites the composite shader descriptor dst-child
  handle to a color-filter descriptor, requires
  `SKIKO_JBR_INTEROP_SHADER_HANDLE_TYPE_CORRUPTED target=compositeShaderDstChild`, and falls back with
  `command-stream-invalid` before JBR replay. The sweep covered 152 rows plus header with all rows passing, 110 command
  replay rows, 26 intentional JBR picture fallback rows, and 16 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-145503/suite.tsv`.
- Direct parser validation passed after adding `JBRSkiaApiTest` coverage for a composite shader descriptor whose
  dst-child shader handle points at a color-filter descriptor. The test was compiled against
  `/tmp/jbr-skia-run/desktop` and run headlessly with the patched desktop module and native bridge.
- Focused composite shader child wrong-type sentinel passed before the full sweep, recording one
  `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-145418/suite.tsv`.
- Full default command-probe sweep passed after adding the transformed shader child wrong-type handle sentinel. The new
  `commands-transformed-shader-child-wrong-effect-type-fallback` row rewrites the transformed shader descriptor child
  handle to a color-filter descriptor, requires
  `SKIKO_JBR_INTEROP_SHADER_HANDLE_TYPE_CORRUPTED target=transformedShaderChild`, and falls back with
  `command-stream-invalid` before JBR replay. The sweep covered 151 rows plus header with all rows passing, 110 command
  replay rows, 26 intentional JBR picture fallback rows, and 15 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-122532/suite.tsv`.
- Direct parser validation passed after adding `JBRSkiaApiTest` coverage for a transformed shader descriptor whose child
  shader handle points at a color-filter descriptor. The test was compiled against `/tmp/jbr-skia-run/desktop` and run
  headlessly with the patched desktop module and native bridge.
- Focused transformed shader child wrong-type sentinel passed before the full sweep, recording one
  `command-stream-invalid` fallback marker, zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-122433/suite.tsv`.
- Full default command-probe sweep passed after adding the shader wrong-type handle sentinel. The new
  `commands-shader-wrong-effect-type-fallback` row rewrites a fill shader handle use to a color-filter descriptor,
  requires `SKIKO_JBR_INTEROP_SHADER_HANDLE_TYPE_CORRUPTED target=fillRectShader`, and falls back with
  `command-stream-invalid` before JBR replay. The sweep covered 150 rows plus header with all rows passing, 110 command
  replay rows, 26 intentional JBR picture fallback rows, and 14 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-110320/suite.tsv`.
- Direct parser validation passed after adding `JBRSkiaApiTest` coverage for a fill shader ref that points at a
  color-filter descriptor handle. The test was compiled against `/tmp/jbr-skia-run/desktop` and run headlessly with the
  patched desktop module and native bridge.
- Full default command-probe sweep passed after tightening path-effect and resize/forced-context descriptor-definition
  gates. The sweep covered 149 rows plus header with all rows passing, 110 command replay rows, 26 intentional JBR
  picture fallback rows, and 13 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-093809/suite.tsv`.
- Focused path-effect command replay passed after tightening the default `commands-path-effect` row to require exactly
  five JBR effect-handle descriptor definitions for the dash, corner, stamped, and chained path-effect scene. The row
  stayed on command replay with no fallback, zero JBR picture frames, and `jbr_effect_handle_define_frames=5`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-092433/suite.tsv`.
- Focused resize/forced-context descriptor lifecycle subset passed after tightening max descriptor-definition gates on
  effect, shader, RuntimeEffect stable color-filter, composite-noise shader, and graphics-layer descriptor rows. The
  subset covered 18 rows; all 18 passed, all stayed on command replay, and the exact define counts were 2 for simple
  descriptors and 6 for composite-noise shader chains:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260511-092758/suite.tsv`.
- Full default command-probe sweep passed after adding a path-effect wrong-type color-filter handle sentinel. The new
  `commands-color-filter-path-effect-wrong-type-fallback` row rewrites a fill color-filter handle to a path-effect
  descriptor, requires the target marker `target=fillRectColorFilterPathEffect`, and falls back with
  `command-stream-invalid` before JBR replay. The sweep covered 149 rows plus header with all rows passing, 110 command
  replay rows, 26 intentional JBR picture fallback rows, and 13 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-231315/suite.tsv`.
- Direct parser validation passed after adding `JBRSkiaApiTest` coverage for a fill color-filter ref that points at a
  path-effect descriptor handle. The test was compiled against `/tmp/jbr-skia-run/desktop` and run headlessly with the
  patched desktop module and native bridge.
- Focused wrong-type handle subset passed after tightening Magic Jewel report validation to require exact
  target-specific corruption markers for each `command-stream-invalid` sentinel. The three rows now assert
  `target=fillRectColorFilter`, `target=shaderColorFilter`, and `target=saveLayerImageFilter` respectively, each with
  zero JBR command frames and zero JBR picture frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-173315/suite.tsv`.
- Full default command-probe sweep passed after adding the shader color-filter wrong-type child-handle sentinel. The
  wrong-type subset now covers `target=fillRectColorFilter`, `target=shaderColorFilter`, and
  `target=saveLayerImageFilter`; each row asserts structured `command-stream-invalid` fallback before replay. The sweep
  covered 148 rows plus header with all rows passing, 110 command replay rows, 26 intentional JBR picture fallback rows,
  and 12 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-161539/suite.tsv`.
- Full default command-probe sweep passed after adding the symmetric wrong-type image-filter handle sentinel. The new
  `commands-image-filter-wrong-effect-type-fallback` row corrupts an image-filter handle use so it references a
  color-filter descriptor and asserts structured `command-stream-invalid` fallback before replay. The sweep covered
  147 rows plus header with all rows passing, 110 command replay rows, 26 intentional JBR picture fallback rows, and
  11 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-144923/suite.tsv`.
- Full default command-probe sweep passed after adding a live wrong-type color-filter handle sentinel. The new
  `commands-color-filter-wrong-effect-type-fallback` row corrupts a color-filter handle use so it references an
  image-filter descriptor and asserts structured `command-stream-invalid` fallback before replay. The sweep covered
  146 rows plus header with all rows passing, 110 command replay rows, 26 intentional JBR picture fallback rows, and
  10 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-132542/suite.tsv`.
- JBR command-stream parser hardening passed after tightening typed effect-handle validation so color-filter refs cannot
  use image/path-effect descriptors and image-filter refs cannot use color-filter descriptors. Local artifact rebuild
  succeeded, the parser-only `JBRSkiaApiTest` validation passed against the rebuilt `/tmp/jbr-skia-run/desktop` patch,
  and the full default command-probe sweep covered 145 rows plus header with all rows passing, 110 command replay rows,
  26 intentional JBR picture fallback rows, and 9 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260510-115246/suite.tsv`.
- Full default command-probe sweep passed after adding the RuntimeEffect color-filter child-count build-failure
  sentinel. The sweep covered 145 rows plus header; all rows passed, with 110 command replay rows, 26 intentional JBR
  picture fallback rows, and 9 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-225942/suite.tsv`.
- Focused RuntimeEffect compile/build fallback subset passed across shader and color-filter families. The new
  `commands-runtime-effect-color-filter-build-fallback` row reported one explicit `runtime-effect-build-failed`
  marker, `stage=child-count`, zero JBR command frames, and zero JBR picture frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-225248/suite.tsv`.
- Full default screenshot parity suite passed after adding RuntimeEffect source-cache eviction parity rows. The suite
  covered 106 rows plus header; all rows passed, all rows stayed on command replay, and zero rows reported structural
  or JBR picture fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-214152/suite.tsv`.
- Focused RuntimeEffect shader and color-filter source-cache eviction parity passed. The shader row reported 1029 JBR
  command frames, 3337 RuntimeEffect source-cache evicts, and 4995 shader-handle cache hits; the color-filter row
  reported 1015 JBR command frames, 3251 RuntimeEffect source-cache evicts, and 4866 effect-handle cache hits:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-214028/suite.tsv`.
- Full default screenshot parity suite passed after adding descriptor handle eviction parity. The suite covered 104 rows
  plus header; all rows passed, all rows stayed on command replay, and zero rows reported structural or JBR picture
  fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-203120/suite.tsv`.
- Focused descriptor handle eviction parity passed. The row reported 46 JBR command frames, zero fallback, zero JBR
  picture frames, and strict effect/shader handle define/use/evict gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-202925/suite.tsv`.
- Full default screenshot parity suite passed after adding standalone graphics-layer offset and chained renderEffect
  parity rows. The suite covered 103 rows plus header; all rows passed, all rows stayed on command replay, and zero
  rows reported structural or JBR picture fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-192048/suite.tsv`.
- Focused standalone graphics-layer offset and chained renderEffect parity passed. The rows reported 1825 and 1943
  JBR command frames respectively, both with zero fallback and zero JBR picture frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-191929/suite.tsv`.
- Full default screenshot parity suite passed after adding static image-shader and composite-shader parity rows. The
  suite covered 101 rows plus header; all rows passed, all rows stayed on command replay, and zero rows reported
  structural or JBR picture fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-181039/suite.tsv`.
- Focused image-shader and composite-shader parity passed. The rows reported 633 and 616 JBR command frames
  respectively, both with zero fallback and zero JBR picture frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-180914/suite.tsv`.
- Full default screenshot parity suite passed after adding static descriptor-backed color-matrix and lighting
  color-filter parity rows. The suite covered 99 rows plus header; all rows passed, all rows stayed on command replay,
  and zero rows reported structural or JBR picture fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-165808/suite.tsv`.
- Focused descriptor-backed color-matrix and lighting color-filter parity passed. The rows reported 841 and 1613 JBR
  command frames respectively, both with zero fallback and zero JBR picture frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-165647/suite.tsv`.
- Full default screenshot parity suite passed after adding descriptor-backed tint color-filter lifecycle parity rows for
  static replay, same-context resize, and forced destination context migration. The suite covered 97 rows plus header;
  all rows passed, all rows stayed on command replay, and zero rows reported structural or JBR picture fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-155200/suite.tsv`.
- Focused descriptor-backed tint color-filter lifecycle parity passed for the new static, resize, and forced-context
  rows. The rows reported 615, 1463, and 820 JBR command frames respectively, all with zero fallback and zero JBR
  picture frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-154845/suite.tsv`.
- Full default screenshot parity suite passed after adding graphics-layer render-effect lifecycle parity rows for
  same-context resize and forced destination context migration. The suite covered 94 rows plus header; all rows passed,
  all rows stayed on command replay, and zero rows reported structural or JBR picture fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-144511/suite.tsv`.
- Focused graphics-layer render-effect lifecycle parity passed for the new resize and forced-context rows. The resize
  row reported 1271 JBR command frames and the forced-context row reported 1291 JBR command frames, both with zero
  fallback and zero JBR picture frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-144352/suite.tsv`.
- Full default screenshot parity suite passed after adding RuntimeEffect pure-color shader lifecycle parity rows for
  same-context resize and forced destination context migration. The suite covered 92 rows plus header; all rows passed,
  all rows stayed on command replay, and zero rows reported structural or JBR picture fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-134708/suite.tsv`.
- Focused RuntimeEffect pure-color shader lifecycle parity passed for the new resize and forced-context rows. The
  resize row reported 591 JBR command frames and the forced-context row reported 444 JBR command frames, both with zero
  fallback and zero JBR picture frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260509-134544/suite.tsv`.
- Full default command-probe sweep passed after tightening RuntimeEffect source-cache eviction rows to require typed
  native evict markers. The sweep covered 144 rows plus header; all rows passed, with 110 command replay rows, 26
  intentional JBR picture fallback rows, and 8 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-122742/suite.tsv`.
- Focused typed RuntimeEffect source-cache eviction subset passed. The shader row reported 985 `type=shader` evict
  markers, and the color-filter row reported 1207 `type=colorFilter` evict markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-122623/suite.tsv`.
- Full default command-probe sweep passed after adding the RuntimeEffect shader source-cache eviction sentinel. The
  sweep covered 144 rows plus header; all rows passed, with 110 command replay rows, 26 intentional JBR picture
  fallback rows, and 8 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-111217/suite.tsv`.
- Focused `commands-runtime-effect-shader-source-cache-eviction` passed with
  `JBR_SKIA_RUNTIME_EFFECT_CACHE_LIMIT_FOR_TEST=2`, zero fallback, 316 JBR command frames, and 1813 shader
  `JBR_SKIA_INTEROP_RUNTIME_EFFECT_CACHE_EVICT` markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-111136/suite.tsv`.
- Full default command-probe sweep passed after adding RuntimeEffect source-cache eviction observability. The sweep
  covered 143 rows plus header; all rows passed, with 109 command replay rows, 26 intentional JBR picture fallback
  rows, and 8 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-095817/suite.tsv`.
- Focused `commands-runtime-effect-source-cache-eviction` passed with
  `JBR_SKIA_RUNTIME_EFFECT_CACHE_LIMIT_FOR_TEST=2`, zero fallback, 472 JBR command frames, and 1675
  `JBR_SKIA_INTEROP_RUNTIME_EFFECT_CACHE_EVICT` markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-095725/suite.tsv`.
- Magic Jewel report-validator regression tests passed after adding the strict
  `EXPECT_MIN_JBR_RUNTIME_EFFECT_CACHE_EVICTS` gate:
  `./scripts/test-jbr-skia-report-validation.sh` in
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel`.
- Full default command-probe sweep passed after adding the RuntimeEffect color-filter child-type build-failure
  sentinel. The sweep covered 142 rows plus header; all rows passed, with 108 command replay rows, 26 intentional JBR
  picture fallback rows, and 8 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-013113/suite.tsv`.
- Full default command-probe sweep passed after adding the RuntimeEffect color-filter compile-failure sentinel. The
  sweep covered 141 rows plus header; all rows passed, with 108 command replay rows, 26 intentional JBR picture
  fallback rows, and 7 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260509-002725/suite.tsv`.
- Full default command-probe sweep passed after tightening the RuntimeEffect shader-plus-color-filter command row to
  require source-cache reuse with at most one miss. The sweep covered 140 rows plus header; all rows passed, with 108
  command replay rows, 26 intentional JBR picture fallback rows, and 6 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-232038/suite.tsv`.
- Full default screenshot parity suite passed after extending every supported RuntimeEffect parity row to require
  RuntimeEffect source-cache reuse with at most one miss. The suite covered 90 rows plus header; all rows passed, all
  rows stayed on command replay, and zero rows reported structural or JBR picture fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-222824/suite.tsv`.
- Full default screenshot parity suite passed after extending stable RuntimeEffect color-filter parity rows to require
  RuntimeEffect source-cache reuse with at most one miss. The suite covered 90 rows plus header; all rows passed, all
  rows stayed on command replay, and zero rows reported structural or JBR picture fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-213258/suite.tsv`.
- Full default command-probe sweep passed after extending stable RuntimeEffect color-filter lifecycle command rows to
  require at most one RuntimeEffect source-cache miss. The sweep covered 140 rows plus header; all rows passed, with 108
  command replay rows, 26 intentional JBR picture fallback rows, and 6 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-203502/suite.tsv`.
- Full default command-probe sweep passed after tightening the stable RuntimeEffect color-filter command row to require
  JBR RuntimeEffect source-cache reuse. The sweep covered 140 rows plus header; all rows passed, with 108 command replay
  rows, 26 intentional JBR picture fallback rows, and 6 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-191427/suite.tsv`.
- Focused RuntimeEffect command subset passed while calibrating the source-cache gate. The supported stable
  color-filter, shader-plus-color-filter, and child color-filter rows all stayed on command replay with zero fallback;
  only the stable color-filter source-cache gate was retained because animated shader/effect handle cache-hit markers
  vary across short runs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-191301/suite.tsv`.
- Full default command-probe sweep passed after adding the recursive RuntimeEffect shader nested-child sentinel. The
  sweep covered 140 rows plus header; all rows passed, with 108 command replay rows, 26 intentional JBR picture
  fallback rows, and 6 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-164715/suite.tsv`.
- Compact recursive RuntimeEffect schema subset passed after adding shader and color-filter nested-child sentinels:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-164220/suite.tsv`.
- Focused RuntimeEffect shader invalid nested-child fallback passed after adding recursive shader descriptor validation
  coverage. The row reported structured `shaderDescriptor` unsupported metadata and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-163854/suite.tsv`.
- Full default command-probe sweep passed after adding the recursive RuntimeEffect color-filter nested-child sentinel.
  The sweep covered 139 rows plus header; all rows passed, with 108 command replay rows, 25 intentional JBR picture
  fallback rows, and 6 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-152515/suite.tsv`.
- Compact RuntimeEffect color-filter command subset passed after adding recursive descriptor validation coverage:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-152047/suite.tsv`.
- Focused RuntimeEffect color-filter invalid nested-child fallback passed after adding recursive descriptor validation
  coverage. The row reported structured `colorFilterDescriptor` unsupported metadata and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-151749/suite.tsv`.
- Full default command-probe sweep passed after adding RuntimeEffect color-filter schema sentinels. The sweep covered
  138 rows plus header; all rows passed, with 108 command replay rows, 24 intentional JBR picture fallback rows, and
  6 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-140744/suite.tsv`.
- Focused RuntimeEffect color-filter invalid uniform-schema and named-child-schema fallbacks passed after adding live
  Magic Jewel sentinels. Both rows stayed off command replay, reported structured `colorFilterDescriptor`
  unsupported metadata, and fell back through JBR picture replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-140448/suite.tsv`.
- Focused RuntimeEffect invalid uniform-schema and named-child-schema fallbacks passed after adding live Magic Jewel
  sentinels. Both rows stayed off command replay, reported structured `shaderDescriptor` unsupported metadata, and
  fell back through JBR picture replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-124449/suite.tsv`.
- Compact RuntimeEffect command subset passed after adding the schema sentinels, covering supported uniform/child
  shader replay and invalid-schema, compile, build, and child-type fallback rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-124746/suite.tsv`.
- Full default command-probe sweep passed with the RuntimeEffect schema sentinels in the default set. The sweep covered
  136 rows plus header; all rows passed, with 108 command replay rows, 22 intentional JBR picture fallback rows, and
  6 expected explicit fallback-marker rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-125344/suite.tsv`.
- Full expanded default screenshot parity suite passed after adding the latest graphics-layer parity rows. The sweep
  covered 90 rows plus header; every row passed, every row stayed on command replay, and no rows reported JBR picture
  fallback or structural fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-114354/suite.tsv`.
- Focused screenshot parity for plain graphics-layer replay plus combined graphics-layer blend+tint and
  blend+color-matrix rows passed after adding the rows to the default screenshot suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-113917/suite.tsv`.
- Focused screenshot parity for standalone graphics-layer blend mode, tint color filter, and color-matrix filter passed
  after adding the rows to the default screenshot suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-113210/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-113353/suite.tsv`.
- Focused screenshot parity for rectangular, rounded, and generic-path graphics-layer clips passed after adding the rows
  to the default screenshot suite. All three rows stayed on command replay with no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-112646/suite.tsv`.
- Focused screenshot parity for graphics-layer `ModulateAlpha` passed after adding the row to the default screenshot
  suite. It stayed on command replay and matched old SwingGraphics within its graphics-layer probe gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-112203/suite.tsv`.
- Full default screenshot parity suite passed after adding explicit graphics-layer scale/translation coverage. The
  sweep covered 80 rows plus header; every row passed, every row stayed on command replay, and no rows reported JBR
  picture fallback or structural fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-103324/suite.tsv`.
- Focused command and screenshot probes for explicit graphics-layer scale/translation passed. The command row stayed on
  JBR command replay with no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-205517/suite.tsv`;
  the parity row matched old SwingGraphics within gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260508-090559/suite.tsv`.
- The full command-probe sweep reached and passed the new `commands-graphics-layer-scale-translate` default row before
  an existing later row hit the sandbox Gradle-wrapper lock. The interrupted row and remaining default tail passed in
  focused reruns:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-090642/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-102926/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260508-103013/suite.tsv`.
- Full default screenshot parity suite passed after adding stable RuntimeEffect color-filter resize and forced-context
  lifecycle rows. The sweep covered 79 rows plus header; every row passed, every row stayed on command replay, and no
  rows reported JBR picture fallback or structural fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-200201/suite.tsv`.
- Focused screenshot parity for stable RuntimeEffect color-filter lifecycle passed across same-context resize and forced
  destination-context rows. Both rows stayed on command replay, required surface/cache/effect-handle markers, and
  matched old SwingGraphics within parity gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260507-195934/suite.tsv`.
- Full default command-probe sweep passed after adding stable RuntimeEffect color-filter resize and forced-context
  lifecycle rows. The sweep covered 133 rows plus header; 107 rows reported command replay, 20 rows reported
  intentional JBR picture fallback sentinels, and 6 rows reported expected structural fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-184927/suite.tsv`.
- Focused command probe for stable RuntimeEffect color-filter lifecycle passed after adding same-context resize and
  forced destination-context rows. Both rows stayed on command replay and required surface-change, command-cache-clear,
  effect-handle redefinition/use/cache-hit, and RuntimeEffect source-cache-hit markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260507-184642/suite.tsv`.
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
