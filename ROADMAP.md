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
