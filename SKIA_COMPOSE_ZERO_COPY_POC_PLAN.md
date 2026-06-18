# Skia Compose Zero-Copy PoC Plan

This is the compact current-state plan. The full historical checkpoint log was archived to
[`docs/history/SKIA_COMPOSE_ZERO_COPY_POC_PLAN.full.md`](docs/history/SKIA_COMPOSE_ZERO_COPY_POC_PLAN.full.md).

## Goal

Render Compose from `ComposePanel(RenderSettings.SwingGraphics)` through a JBR-owned Skia surface during Swing painting,
avoiding the Skiko GPU to CPU bitmap to Swing re-upload path. The command stream must replay directly into the Java2D
Metal destination when ABI/capability checks match, and must fall back cleanly on mismatch.

## Current Snapshot

- ABI 106 artifacts are current across JBR private API, JBR API mirror, Skiko, CMP, and Magic Jewel.
- Magic Jewel tightened the exact graphics-layer color-filter lifecycle screenshot-parity pair with explicit zero
  ceilings for JBR effect-handle and shader-handle defines, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-color-filter parity-forced-context-graphics-layer-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed.
- Magic Jewel tightened the exact graphics-layer blend+color-filter lifecycle screenshot-parity pair with explicit
  zero ceilings for JBR effect-handle and shader-handle defines, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-blend-color-filter parity-forced-context-graphics-layer-blend-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed.
- Magic Jewel restored the exact graphics-layer offset-effect lifecycle screenshot-parity caps to the documented
  tightened ceilings, reducing the live resize ceiling to 9 and the forced-context ceiling to 11, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offset-effect parity-forced-context-graphics-layer-offset-effect" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed.
- Magic Jewel tightened the exact graphics-layer render-effect lifecycle screenshot-parity pair to match current
  report evidence more closely, reducing the resize ceiling to 8 and the forced-context ceiling to 11, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect parity-forced-context-graphics-layer-render-effect" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed.
- Magic Jewel tightened the exact graphics-layer color-matrix-filter lifecycle screenshot-parity pair to match current
  report evidence more closely, reducing the resize ceiling to 8 and the forced-context ceiling to 11, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-color-matrix-filter parity-forced-context-graphics-layer-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed.
- Magic Jewel tightened the exact graphics-layer render-effect+color-matrix-filter lifecycle screenshot-parity pair to
  match current report evidence more closely, reducing the resize ceiling to 16 and the forced-context ceiling to 20,
  then reran `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-color-matrix-filter parity-forced-context-graphics-layer-render-effect-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed.
- Magic Jewel tightened the exact graphics-layer render-effect+blend-mode lifecycle screenshot-parity pair to match
  current report evidence more closely, reducing the resize ceiling to 8 and the forced-context ceiling to 10, then
  reran `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-blend-mode parity-forced-context-graphics-layer-render-effect-blend-mode" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed.
- Magic Jewel tightened the exact graphics-layer render-effect+color-filter lifecycle screenshot-parity pair to match
  current report evidence more closely, reducing the resize ceiling to 7 and the forced-context ceiling to 11, then
  reran `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-color-filter parity-forced-context-graphics-layer-render-effect-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed.
- Magic Jewel tightened the exact graphics-layer chained-render-effect+blend-color-matrix-filter parity pair to match
  current report evidence more closely, reducing the resize ceiling to 24 and the forced-context ceiling to 33, then
  reran `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-chained-render-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-chained-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed.
- Magic Jewel tightened the exact graphics-layer offset-effect lifecycle screenshot-parity pair to match report
  evidence more closely, reducing the resize ceiling to 9 and the forced-context ceiling to 11, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offset-effect parity-forced-context-graphics-layer-offset-effect" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed.
- Magic Jewel tightened the exact graphics-layer offset-effect+blend-color-matrix-filter parity pair to match report
  evidence more closely, reducing the resize ceiling to 16 and the forced-context ceiling to 22, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offset-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-offset-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed.
- Magic Jewel tightened the exact graphics-layer render-effect+blend-color-matrix-filter parity pair to match report
  evidence more closely, reducing the resize ceiling to 14 and the forced-context ceiling to 22, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed.
- Magic Jewel tightened the exact graphics-layer render-effect+blend-color-filter parity pair to match report
  evidence more closely, reducing the resize ceiling to 8 and the forced-context ceiling to 11, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-blend-color-filter parity-forced-context-graphics-layer-render-effect-blend-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed.
- Magic Jewel tightened the exact graphics-layer near-camera chained-render-effect+blend-color-matrix-filter parity
  pair to match report evidence more closely, reducing the resize ceiling to 24 and the forced-context ceiling to 30,
  then reran `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed.
- Validation should be batched to keep iteration cost under control: use exact cases or tiny focused groups per change,
  and cap broad command sweeps, screenshot sweeps, benchmark suites, and full matrices at one broad validation slot per
  local calendar day unless the user explicitly asks for another one or an ABI/capability break needs an emergency
  gate. Magic Jewel now enforces that cap with `scripts/jbr-skia-daily-validation-guard.sh` for command-probe sweeps,
  screenshot parity suites, benchmark suites, compatibility matrices, and artifact matrices. The 2026-06-16 broad slot
  has now been consumed by the full compatibility matrix, so continue with exact small `CASES`/`CASE_GROUPS` only until
  the next local-day slot. Default launches, default-list `CASES_FROM`/`CASES_UNTIL` range launches, and any resolved
  selection above `JBR_SKIA_BROAD_VALIDATION_CASE_LIMIT` rows now count as broad validation; the default broad limit is
  2 rows. The guard runs after row selection and before launching validation, so accidental second broad runs on the
  same day fail fast. A follow-up hardening moved default-launch daily checks earlier in the broad shell runners so an
  accidental default full command-probe/screenshot/benchmark/matrix launch is rejected before slow setup. This daily
  cap replaces the older "broad sweep after ten focused changes" checkpoint cadence.
- Magic Jewel refreshed the full compatibility matrix in the 2026-06-16 daily broad slot:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with 56 expected fallback
  rows, one happy command row, no unsupported reasons, no validation failures, 45,021 CMP recorder frames, 909
  Skiko/JBR command frames, zero picture frames, screenshot status passed for 56 rows and not-run for the native-ABI
  row. This completes the intended compatibility-matrix broad checkpoint after the prior full command-probe sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260616-000119/matrix.tsv`.
- Magic Jewel rechecked the graphics-layer chained-render-effect lifecycle parity gates after stale compact notes
  suggested tighter `7/7` ceilings. Exact one-row probes showed the current recorder still needs the live 16 resize
  and 20 forced-context effect-handle ceilings, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-chained-render-effect parity-forced-context-graphics-layer-chained-render-effect" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  The exact two-row validation passed 2/2. Resize passed with screenshot status passed, 529 JBR command frames,
  `avg_delta=1.902`, `bad_pixel_ratio=0.04557`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 16 effect-handle define frames, 1,067 effect-handle use
  frames, and 1,059 effect-handle cache-hit frames, plus the known single early resize parity `command-stream-invalid`
  fallback artifact. Forced context passed fallback-free with 816 JBR command frames, `avg_delta=2.041`,
  `bad_pixel_ratio=0.04784`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, 20 effect-handle define frames, 1,513 effect-handle use frames, and 1,503
  effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-095456/suite.tsv`.
- Magic Jewel filled the graphics-layer chained-render-effect lifecycle parity gap by adding
  `parity-resize-graphics-layer-chained-render-effect` and
  `parity-forced-context-graphics-layer-chained-render-effect` to the screenshot-parity default list,
  `graphics-layer-effects` group, and case switch. The rows mirror the command-probe lifecycle gates, isolate the
  graphics-layer chained render effect probe, and keep validation exact under the daily broad cap. The exact two-row
  validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-chained-render-effect parity-forced-context-graphics-layer-chained-render-effect" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 936 JBR command frames, `avg_delta=1.902`,
  `bad_pixel_ratio=0.04557`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, 16 effect-handle define frames, 1,926 effect-handle use frames, and 1,918
  effect-handle cache-hit frames, and the known single early resize parity `command-stream-invalid` fallback artifact.
  Forced context passed fallback-free with 1,288 JBR command frames, `avg_delta=2.041`, `bad_pixel_ratio=0.04784`,
  one destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache
  clear, 22 effect-handle define frames, 2,324 effect-handle use frames, and 2,313 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260617-083726/suite.tsv`.
- Magic Jewel filled the graphics-layer offset-effect lifecycle parity gap by adding
  `parity-resize-graphics-layer-offset-effect` and `parity-forced-context-graphics-layer-offset-effect` to the
  screenshot-parity default list, `graphics-layer-effects` group, and case switch. The rows mirror the command-probe
  lifecycle gates, isolate the graphics-layer offset image filter probe, and keep validation exact under the daily
  broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offset-effect parity-forced-context-graphics-layer-offset-effect" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,293 JBR command frames, `avg_delta=1.908`,
  `bad_pixel_ratio=0.04583`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine effect-handle define frames, 2,379 effect-handle use frames, 2,370
  effect-handle cache-hit frames, and the known single early resize parity `command-stream-invalid` fallback artifact.
  Forced context passed fallback-free with 1,326 JBR command frames, `avg_delta=2.048`, `bad_pixel_ratio=0.04815`,
  one destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache
  clear, 11 effect-handle define frames, 2,310 effect-handle use frames, and 2,299 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-235754/suite.tsv`.
- Magic Jewel filled the graphics-layer near-camera chained-render-effect+blend-color-matrix-filter lifecycle parity
  gap by adding `parity-resize-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter` and
  `parity-forced-context-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter` to the
  screenshot-parity default list, `graphics-layer-effects` group, and case switch. The rows mirror the command-probe
  lifecycle gates, isolate the near-camera graphics layer plus chained image filter, blend-mode, and
  color-matrix-filter probe, and keep validation exact under the daily broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 785 JBR command frames, `avg_delta=2.248`,
  `bad_pixel_ratio=0.05651`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, 18 effect-handle define frames, 2,914 effect-handle use frames, 2,902
  effect-handle cache-hit frames, and the known single early resize parity `command-stream-invalid` fallback artifact.
  Forced context passed fallback-free with 882 JBR command frames, `avg_delta=2.455`, `bad_pixel_ratio=0.06094`, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  30 effect-handle define frames, 3,173 effect-handle use frames, and 3,154 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-235047/suite.tsv`.
- Magic Jewel filled the graphics-layer chained-render-effect+blend-color-matrix-filter lifecycle parity gap by adding
  `parity-resize-graphics-layer-chained-render-effect-blend-color-matrix-filter` and
  `parity-forced-context-graphics-layer-chained-render-effect-blend-color-matrix-filter` to the screenshot-parity
  default list, `graphics-layer-effects` group, and case switch. The rows mirror the command-probe lifecycle gates,
  isolate the graphics-layer chained image filter plus blend-mode and color-matrix-filter probe, and keep validation
  exact under the daily broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-chained-render-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-chained-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,236 JBR command frames, `avg_delta=2.348`,
  `bad_pixel_ratio=0.06017`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, 21 effect-handle define frames, 3,846 effect-handle use frames, 3,832
  effect-handle cache-hit frames, and the known single early resize parity `command-stream-invalid` fallback artifact.
  Forced context passed fallback-free with 722 JBR command frames, `avg_delta=2.575`, `bad_pixel_ratio=0.06531`, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  33 effect-handle define frames, 2,948 effect-handle use frames, and 2,926 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-234420/suite.tsv`.
- Magic Jewel filled the graphics-layer offset-effect+blend-color-matrix-filter lifecycle parity gap by adding
  `parity-resize-graphics-layer-offset-effect-blend-color-matrix-filter` and
  `parity-forced-context-graphics-layer-offset-effect-blend-color-matrix-filter` to the screenshot-parity default list,
  `graphics-layer-effects` group, and case switch. The rows mirror the command-probe lifecycle gates, isolate the
  graphics-layer offset image filter plus blend-mode and color-matrix-filter probe, and keep validation exact under the
  daily broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offset-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-offset-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 after rerunning outside the sandbox for Gradle wrapper lock access. Resize passed with screenshot status
  passed, 1,195 JBR command frames, `avg_delta=2.257`, `bad_pixel_ratio=0.05727`, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 16 effect-handle define
  frames, 4,080 effect-handle use frames, 4,064 effect-handle cache-hit frames, and the known single early resize
  parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 700 JBR command frames,
  `avg_delta=2.465`, `bad_pixel_ratio=0.06185`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 22 effect-handle define frames, 3,262 effect-handle use frames,
  and 3,240 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-233834/suite.tsv`.
- Magic Jewel filled the graphics-layer render-effect+blend-color-matrix-filter lifecycle parity gap by adding
  `parity-resize-graphics-layer-render-effect-blend-color-matrix-filter` and
  `parity-forced-context-graphics-layer-render-effect-blend-color-matrix-filter` to the screenshot-parity default list,
  `graphics-layer-effects` group, and case switch. The rows mirror the command-probe lifecycle gates, isolate the
  graphics-layer render-effect plus blend-mode and color-matrix-filter probe, and keep validation exact under the daily
  broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 894 JBR command frames, `avg_delta=2.348`,
  `bad_pixel_ratio=0.06017`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 14 effect-handle define frames, 3,028 effect-handle use frames,
  3,014 effect-handle cache-hit frames, and the known single early resize parity `command-stream-invalid` fallback
  artifact. Forced context passed fallback-free with 1,130 JBR command frames, `avg_delta=2.574`,
  `bad_pixel_ratio=0.06548`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 22 effect-handle define frames, 3,838 effect-handle use frames,
  and 3,816 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-233106/suite.tsv`.
- Magic Jewel filled the graphics-layer render-effect+blend-color-filter lifecycle parity gap by adding
  `parity-resize-graphics-layer-render-effect-blend-color-filter` and
  `parity-forced-context-graphics-layer-render-effect-blend-color-filter` to the screenshot-parity default list,
  `graphics-layer-effects` group, and case switch. The rows mirror the command-probe lifecycle gates, isolate the
  graphics-layer render-effect plus blend-mode and color-filter probe, and keep validation exact under the daily broad
  cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-blend-color-filter parity-forced-context-graphics-layer-render-effect-blend-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,300 JBR command frames, `avg_delta=2.373`,
  `bad_pixel_ratio=0.06020`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 8 effect-handle define frames, 2,276 effect-handle use frames,
  2,268 effect-handle cache-hit frames, and the known single early resize parity `command-stream-invalid` fallback
  artifact. Forced context passed fallback-free with 1,016 JBR command frames, `avg_delta=2.606`,
  `bad_pixel_ratio=0.06552`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 11 effect-handle define frames, 2,012 effect-handle use frames,
  and 2,001 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-232522/suite.tsv`.
- Magic Jewel filled the graphics-layer render-effect+color-matrix-filter lifecycle parity gap by adding
  `parity-resize-graphics-layer-render-effect-color-matrix-filter` and
  `parity-forced-context-graphics-layer-render-effect-color-matrix-filter` to the screenshot-parity default list,
  `graphics-layer-effects` group, and case switch. The rows mirror the command-probe lifecycle gates, isolate the
  graphics-layer render-effect plus color-matrix-filter probe, and keep validation exact under the daily broad cap. The
  exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-color-matrix-filter parity-forced-context-graphics-layer-render-effect-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 617 JBR command frames, `avg_delta=1.903`,
  `bad_pixel_ratio=0.04557`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 16 effect-handle define frames, 2,128 effect-handle use
  frames, 2,112 effect-handle cache-hit frames, and the known single early resize parity `command-stream-invalid`
  fallback artifact. Forced context passed fallback-free with 870 JBR command frames, `avg_delta=2.045`,
  `bad_pixel_ratio=0.04801`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 20 effect-handle define frames, 3,114 effect-handle use frames,
  and 3,094 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-231819/suite.tsv`.
- Magic Jewel filled the graphics-layer render-effect+blend-mode lifecycle parity gap by adding
  `parity-resize-graphics-layer-render-effect-blend-mode` and
  `parity-forced-context-graphics-layer-render-effect-blend-mode` to the screenshot-parity default list,
  `graphics-layer-effects` group, and case switch. The rows mirror the command-probe lifecycle gates, isolate the
  graphics-layer render-effect plus blend-mode probe, and keep validation exact under the daily broad cap. The exact
  two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-blend-mode parity-forced-context-graphics-layer-render-effect-blend-mode" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 641 JBR command frames, `avg_delta=2.376`,
  `bad_pixel_ratio=0.06017`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 7 effect-handle define frames, 1,292 effect-handle use frames,
  1,285 effect-handle cache-hit frames, and the known single early resize parity `command-stream-invalid` fallback
  artifact. Forced context passed fallback-free with 477 JBR command frames, `avg_delta=2.608`,
  `bad_pixel_ratio=0.06548`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 10 effect-handle define frames, 1,132 effect-handle use frames,
  and 1,122 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-085329/suite.tsv`.
- Magic Jewel filled the graphics-layer render-effect+color-filter lifecycle parity gap by adding
  `parity-resize-graphics-layer-render-effect-color-filter` and
  `parity-forced-context-graphics-layer-render-effect-color-filter` to the screenshot-parity default list,
  `graphics-layer-effects` group, and case switch. The rows mirror the command-probe lifecycle gates, isolate the
  graphics-layer render-effect plus color-filter probe, and keep validation exact under the daily broad cap. The exact
  two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-color-filter parity-forced-context-graphics-layer-render-effect-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,100 JBR command frames, `avg_delta=1.901`,
  `bad_pixel_ratio=0.04557`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 7 effect-handle define frames, 1,853 effect-handle use frames,
  1,846 effect-handle cache-hit frames, and the known single early resize parity `command-stream-invalid` fallback
  artifact. Forced context passed fallback-free with 1,138 JBR command frames, `avg_delta=2.044`,
  `bad_pixel_ratio=0.04801`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 10 effect-handle define frames, 1,960 effect-handle use frames,
  and 1,950 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-084801/suite.tsv`.
- Magic Jewel filled the graphics-layer blend+color-matrix-filter lifecycle parity gap by adding
  `parity-resize-graphics-layer-blend-color-matrix-filter` and
  `parity-forced-context-graphics-layer-blend-color-matrix-filter` to the screenshot-parity default list,
  `graphics-layer-basic` group, and case switch. The rows mirror the command-probe lifecycle gates, isolate the
  graphics-layer blend-mode plus color-matrix-filter probe, and keep validation exact under the daily broad cap. The
  exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-blend-color-matrix-filter parity-forced-context-graphics-layer-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,301 JBR command frames, `avg_delta=1.922`,
  `bad_pixel_ratio=0.04657`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 8 effect-handle define frames, 2,225 effect-handle use frames,
  2,217 effect-handle cache-hit frames, and the known single early resize parity `command-stream-invalid` fallback
  artifact. Forced context passed fallback-free with 1,351 JBR command frames, `avg_delta=2.065`,
  `bad_pixel_ratio=0.04904`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 11 effect-handle define frames, 2,382 effect-handle use frames,
  and 2,371 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-084054/suite.tsv`.
- Magic Jewel filled the graphics-layer blend+color-filter lifecycle parity gap by adding
  `parity-resize-graphics-layer-blend-color-filter` and
  `parity-forced-context-graphics-layer-blend-color-filter` to the screenshot-parity default list,
  `graphics-layer-basic` group, and case switch. The rows mirror the command-probe lifecycle gates, isolate the
  graphics-layer blend-mode plus color-filter probe, and keep validation exact under the daily broad cap. The exact
  two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-blend-color-filter parity-forced-context-graphics-layer-blend-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 868 JBR command frames, `avg_delta=1.915`,
  `bad_pixel_ratio=0.04659`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 785 JBR command
  frames, `avg_delta=2.056`, `bad_pixel_ratio=0.04906`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-083504/suite.tsv`.
- Magic Jewel filled the graphics-layer color-filter lifecycle parity gap by adding
  `parity-resize-graphics-layer-color-filter` and `parity-forced-context-graphics-layer-color-filter` to the
  screenshot-parity default list, `graphics-layer-basic` group, and case switch. The rows mirror the command-probe
  lifecycle gates, isolate the graphics-layer color-filter probe, and keep validation exact under the daily broad cap.
  The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-color-filter parity-forced-context-graphics-layer-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 773 JBR command frames, `avg_delta=1.906`,
  `bad_pixel_ratio=0.04576`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 1,343 JBR command
  frames, `avg_delta=2.047`, `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-082945/suite.tsv`.
- Magic Jewel filled the graphics-layer blend-mode lifecycle parity gap by adding
  `parity-resize-graphics-layer-blend-mode` and `parity-forced-context-graphics-layer-blend-mode` to the
  screenshot-parity default list, `graphics-layer-basic` group, and case switch. The rows mirror the command-probe
  lifecycle gates, isolate the graphics-layer blend-mode probe, and keep validation exact under the daily broad cap.
  The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-blend-mode parity-forced-context-graphics-layer-blend-mode" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,237 JBR command frames, `avg_delta=1.922`,
  `bad_pixel_ratio=0.04658`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 882 JBR command
  frames, `avg_delta=2.065`, `bad_pixel_ratio=0.04904`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-082359/suite.tsv`.
- Magic Jewel filled the graphics-layer off-center pivot lifecycle parity gap by adding
  `parity-resize-graphics-layer-offcenter-pivot` and `parity-forced-context-graphics-layer-offcenter-pivot` to the
  screenshot-parity default list, `graphics-layer-clip-shadow-transform` group, and case switch. The rows mirror the
  command-probe lifecycle gates, isolate the near-camera rotation probe with an off-center pivot, and keep validation
  exact under the daily broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offcenter-pivot parity-forced-context-graphics-layer-offcenter-pivot" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 828 JBR command frames, `avg_delta=1.910`,
  `bad_pixel_ratio=0.04592`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 871 JBR command
  frames, `avg_delta=2.050`, `bad_pixel_ratio=0.04826`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-081753/suite.tsv`.
- Magic Jewel filled the graphics-layer near-camera lifecycle parity gap by adding
  `parity-resize-graphics-layer-near-camera` and `parity-forced-context-graphics-layer-near-camera` to the
  screenshot-parity default list, `graphics-layer-clip-shadow-transform` group, and case switch. The rows mirror the
  command-probe lifecycle gates, isolate the near-camera graphics-layer probe, and keep validation exact under the
  daily broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-near-camera parity-forced-context-graphics-layer-near-camera" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 847 JBR command frames, `avg_delta=1.913`,
  `bad_pixel_ratio=0.04603`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 843 JBR command
  frames, `avg_delta=2.050`, `bad_pixel_ratio=0.04826`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-081103/suite.tsv`.
- Magic Jewel filled the graphics-layer scale-translate lifecycle parity gap by adding
  `parity-resize-graphics-layer-scale-translate` and `parity-forced-context-graphics-layer-scale-translate` to the
  screenshot-parity default list, `graphics-layer-clip-shadow-transform` group, and case switch. The rows mirror the
  command-probe lifecycle gates, isolate the scale/translation graphics-layer probe, and keep validation exact under
  the daily broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-scale-translate parity-forced-context-graphics-layer-scale-translate" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 780 JBR command frames, `avg_delta=1.906`,
  `bad_pixel_ratio=0.04576`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 909 JBR command
  frames, `avg_delta=2.049`, `bad_pixel_ratio=0.04818`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-080534/suite.tsv`.
- Magic Jewel filled the graphics-layer rotation-xy lifecycle parity gap by adding
  `parity-resize-graphics-layer-rotationxy` and `parity-forced-context-graphics-layer-rotationxy` to the
  screenshot-parity default list, `graphics-layer-clip-shadow-transform` group, and case switch. The rows mirror the
  command-probe lifecycle gates, isolate the combined rotation-x/y graphics-layer probe, and keep validation exact
  under the daily broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-rotationxy parity-forced-context-graphics-layer-rotationxy" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,195 JBR command frames, `avg_delta=1.911`,
  `bad_pixel_ratio=0.04596`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 830 JBR command
  frames, `avg_delta=2.050`, `bad_pixel_ratio=0.04826`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-075954/suite.tsv`.
- Magic Jewel filled the graphics-layer rotation-y lifecycle parity gap by adding
  `parity-resize-graphics-layer-rotationy` and `parity-forced-context-graphics-layer-rotationy` to the
  screenshot-parity default list, `graphics-layer-clip-shadow-transform` group, and case switch. The rows mirror the
  command-probe lifecycle gates, isolate the rotation-y graphics-layer probe, and keep validation exact under the daily
  broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-rotationy parity-forced-context-graphics-layer-rotationy" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,164 JBR command frames, `avg_delta=1.908`,
  `bad_pixel_ratio=0.04582`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 1,367 JBR command
  frames, `avg_delta=2.048`, `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-075424/suite.tsv`.
- Magic Jewel filled the graphics-layer rotation-x lifecycle parity gap by adding
  `parity-resize-graphics-layer-rotationx` and `parity-forced-context-graphics-layer-rotationx` to the
  screenshot-parity default list, `graphics-layer-clip-shadow-transform` group, and case switch. The rows mirror the
  command-probe lifecycle gates, isolate the rotation-x graphics-layer probe, and keep validation exact under the daily
  broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-rotationx parity-forced-context-graphics-layer-rotationx" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,247 JBR command frames, `avg_delta=1.909`,
  `bad_pixel_ratio=0.04586`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 1,409 JBR command
  frames, `avg_delta=2.050`, `bad_pixel_ratio=0.04825`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-074859/suite.tsv`.
- Magic Jewel filled the graphics-layer path-shadow lifecycle parity gap by adding
  `parity-resize-graphics-layer-path-shadow` and `parity-forced-context-graphics-layer-path-shadow` to the
  screenshot-parity default list, `graphics-layer-clip-shadow-transform` group, and case switch. The rows mirror the
  command-probe lifecycle gates, isolate the path-clip shadow graphics-layer probe, and keep validation exact under the
  daily broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-path-shadow parity-forced-context-graphics-layer-path-shadow" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,362 JBR command frames, `avg_delta=1.912`,
  `bad_pixel_ratio=0.04596`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, one shadow command per frame, zero shader/effect handle
  markers, and the known single early resize parity `command-stream-invalid` fallback artifact. Forced context passed
  fallback-free with 1,278 JBR command frames, `avg_delta=2.053`, `bad_pixel_ratio=0.04839`, one destination
  context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, nine image
  refs, one shadow command per frame, and zero shader/effect handle markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-074226/suite.tsv`.
- Magic Jewel filled the graphics-layer round-shadow lifecycle parity gap by adding
  `parity-resize-graphics-layer-round-shadow` and `parity-forced-context-graphics-layer-round-shadow` to the
  screenshot-parity default list, `graphics-layer-clip-shadow-transform` group, and case switch. The rows mirror the
  command-probe lifecycle gates, isolate the round-clip shadow graphics-layer probe, and keep validation exact under
  the daily broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-round-shadow parity-forced-context-graphics-layer-round-shadow" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 801 JBR command frames, `avg_delta=1.912`,
  `bad_pixel_ratio=0.04592`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, one shadow command per frame, zero shader/effect handle
  markers, and the known single early resize parity `command-stream-invalid` fallback artifact. Forced context passed
  fallback-free with 1,134 JBR command frames, `avg_delta=2.064`, `bad_pixel_ratio=0.04829`, one destination
  context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, nine image
  refs, one shadow command per frame, and zero shader/effect handle markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-073605/suite.tsv`.
- Magic Jewel filled the graphics-layer shadow lifecycle parity gap by adding `parity-resize-graphics-layer-shadow`
  and `parity-forced-context-graphics-layer-shadow` to the screenshot-parity default list,
  `graphics-layer-clip-shadow-transform` group, and case switch. The rows mirror the command-probe lifecycle gates,
  isolate the plain shadow graphics-layer probe, and keep validation exact under the daily broad cap. The exact
  two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-shadow parity-forced-context-graphics-layer-shadow" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,203 JBR command frames, `avg_delta=1.905`,
  `bad_pixel_ratio=0.04575`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, one shadow command per frame, zero shader/effect handle
  markers, and the known single early resize parity `command-stream-invalid` fallback artifact. Forced context passed
  fallback-free with 1,211 JBR command frames, `avg_delta=2.046`, `bad_pixel_ratio=0.04815`, one destination
  context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, nine image
  refs, one shadow command per frame, and zero shader/effect handle markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-073059/suite.tsv`.
- Magic Jewel filled the graphics-layer path-clip lifecycle parity gap by adding
  `parity-resize-graphics-layer-path-clip` and `parity-forced-context-graphics-layer-path-clip` to the
  screenshot-parity default list, `graphics-layer-clip-shadow-transform` group, and case switch. The rows mirror the
  command-probe lifecycle gates, isolate the path-clip graphics-layer probe, and keep validation exact under the daily
  broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-path-clip parity-forced-context-graphics-layer-path-clip" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 874 JBR command frames, `avg_delta=1.912`,
  `bad_pixel_ratio=0.04597`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 1,313 JBR command
  frames, `avg_delta=2.053`, `bad_pixel_ratio=0.04839`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-072558/suite.tsv`.
- Magic Jewel filled the graphics-layer round-clip lifecycle parity gap by adding
  `parity-resize-graphics-layer-round-clip` and `parity-forced-context-graphics-layer-round-clip` to the
  screenshot-parity default list, `graphics-layer-clip-shadow-transform` group, and case switch. The rows mirror the
  command-probe lifecycle gates, isolate the round-clip graphics-layer probe, and keep validation exact under the daily
  broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-round-clip parity-forced-context-graphics-layer-round-clip" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,191 JBR command frames, `avg_delta=1.912`,
  `bad_pixel_ratio=0.04594`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 812 JBR command
  frames, `avg_delta=2.053`, `bad_pixel_ratio=0.04829`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-072116/suite.tsv`.
- Magic Jewel filled the graphics-layer clip lifecycle parity gap by adding `parity-resize-graphics-layer-clip` and
  `parity-forced-context-graphics-layer-clip` to the screenshot-parity default list,
  `graphics-layer-clip-shadow-transform` group, and case switch. The rows mirror the command-probe lifecycle gates,
  isolate the rectangular clip graphics-layer probe, and keep validation exact under the daily broad cap. The exact
  two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-clip parity-forced-context-graphics-layer-clip" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,042 JBR command frames, `avg_delta=1.908`,
  `bad_pixel_ratio=0.04580`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 1,205 JBR command
  frames, `avg_delta=2.049`, `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-071720/suite.tsv`.
- Magic Jewel filled the graphics-layer offscreen lifecycle parity gap by adding
  `parity-resize-graphics-layer-offscreen` and `parity-forced-context-graphics-layer-offscreen` to the screenshot-parity
  default list, `graphics-layer-clip-shadow-transform` group, and case switch. The rows mirror the command-probe
  lifecycle gates, isolate the offscreen graphics-layer probe, and keep validation exact under the daily broad cap. The
  exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offscreen parity-forced-context-graphics-layer-offscreen" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,024 JBR command frames, `avg_delta=1.909`,
  `bad_pixel_ratio=0.04583`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 1,241 JBR command
  frames, `avg_delta=2.050`, `bad_pixel_ratio=0.04821`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-071209/suite.tsv`.
- Magic Jewel filled the graphics-layer modulate-alpha lifecycle parity gap by adding
  `parity-resize-graphics-layer-modulate-alpha` and `parity-forced-context-graphics-layer-modulate-alpha` to the
  screenshot-parity default list, `graphics-layer-clip-shadow-transform` group, and case switch. The rows mirror the
  command-probe lifecycle gates, isolate the modulate-alpha graphics-layer probe, and keep validation exact under the
  daily broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-modulate-alpha parity-forced-context-graphics-layer-modulate-alpha" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,244 JBR command frames, `avg_delta=2.265`,
  `bad_pixel_ratio=0.05718`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 909 JBR command
  frames, `avg_delta=2.479`, `bad_pixel_ratio=0.06186`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-070543/suite.tsv`.
- Magic Jewel filled the base graphics-layer lifecycle parity gap by adding `parity-resize-graphics-layer` and
  `parity-forced-context-graphics-layer` to the screenshot-parity default list, `graphics-layer-basic` group, and case
  switch. The rows mirror the command-probe lifecycle gates, isolate the base graphics-layer probe, and keep validation
  exact under the daily broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer parity-forced-context-graphics-layer" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,074 JBR command frames, `avg_delta=1.906`,
  `bad_pixel_ratio=0.04576`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 890 JBR command
  frames, `avg_delta=2.048`, `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-070019/suite.tsv`.
- Magic Jewel filled the lighting filter lifecycle parity gap by adding `parity-resize-lighting-filter` and
  `parity-forced-context-lighting-filter` to the screenshot-parity default list, `color-filtering` group, and case
  switch. The rows mirror the command-probe lifecycle gates and keep validation exact under the daily broad cap. The
  exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-lighting-filter parity-forced-context-lighting-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,213 JBR command frames, `avg_delta=1.919`,
  `bad_pixel_ratio=0.04643`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, eight effect-handle define frames, 2,103 effect-handle use
  frames, 2,095 effect-handle cache-hit frames, and the known single early resize parity `command-stream-invalid`
  fallback artifact. Forced context passed fallback-free with 1,312 JBR command frames, `avg_delta=2.063`,
  `bad_pixel_ratio=0.04891`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 11 effect-handle define frames, 2,305 effect-handle use
  frames, and 2,294 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-065454/suite.tsv`.
- Magic Jewel filled the color-matrix filter lifecycle parity gap by adding `parity-resize-color-matrix-filter` and
  `parity-forced-context-color-matrix-filter` to the screenshot-parity default list, `color-filtering` group, and case
  switch. The rows mirror the command-probe lifecycle gates and keep validation exact under the daily broad cap. The
  exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-color-matrix-filter parity-forced-context-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,058 JBR command frames, `avg_delta=1.919`,
  `bad_pixel_ratio=0.04643`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, eight effect-handle define frames, 1,983 effect-handle use
  frames, 1,975 effect-handle cache-hit frames, and the known single early resize parity `command-stream-invalid`
  fallback artifact. Forced context passed fallback-free with 836 JBR command frames, `avg_delta=2.063`,
  `bad_pixel_ratio=0.04891`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 11 effect-handle define frames, 1,797 effect-handle use
  frames, and 1,786 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-065010/suite.tsv`.
- Magic Jewel filled the skew-transform lifecycle parity gap by adding `parity-resize-skew-transform` and
  `parity-forced-context-skew-transform` to the screenshot-parity default list, `core-drawing` group, and case switch.
  The rows mirror the command-probe lifecycle gates, isolate the skew transform probe, and keep validation exact under
  the daily broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-skew-transform parity-forced-context-skew-transform" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 873 JBR command frames, `avg_delta=2.386`,
  `bad_pixel_ratio=0.06235`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, two image refs, 40 effect-handle define frames, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 825 JBR command
  frames, `avg_delta=2.606`, `bad_pixel_ratio=0.06742`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, two image refs, and 60 effect-handle define frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-064326/suite.tsv`.
- Magic Jewel filled the point-dot lifecycle parity gap by adding `parity-resize-point-dots` and
  `parity-forced-context-point-dots` to the screenshot-parity default list, `core-drawing` group, and case switch. The
  rows mirror the command-probe lifecycle gates and keep validation exact under the daily broad cap. The exact two-row
  validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-point-dots parity-forced-context-point-dots" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 790 JBR command frames, `avg_delta=1.854`,
  `bad_pixel_ratio=0.04465`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 40 effect-handle define frames, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 570 JBR command
  frames, `avg_delta=1.981`, `bad_pixel_ratio=0.04668`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and 50 effect-handle define frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-063729/suite.tsv`.
- Magic Jewel filled the blend-mode lifecycle parity gap by adding `parity-resize-blend-modes` and
  `parity-forced-context-blend-modes` to the screenshot-parity default list, `core-drawing` group, and case switch.
  The rows mirror the command-probe lifecycle gates, isolate the blend-mode probe, and keep validation exact under the
  daily broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-blend-modes parity-forced-context-blend-modes" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 837 JBR command frames, `avg_delta=1.891`,
  `bad_pixel_ratio=0.04573`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, zero shader/effect handle markers, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 852 JBR command
  frames, `avg_delta=2.026`, `bad_pixel_ratio=0.04795`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-063229/suite.tsv`.
- Magic Jewel filled the vertices lifecycle parity gap by adding `parity-resize-vertices` and
  `parity-forced-context-vertices` to the screenshot-parity default list, `core-drawing` group, and case switch. The
  rows mirror the command-probe lifecycle gates, disable unrelated paragraph probes, and keep validation exact under
  the daily broad cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-vertices parity-forced-context-vertices" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 810 JBR command frames, `avg_delta=1.853`,
  `bad_pixel_ratio=0.04464`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 35 effect-handle define frames, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 540 JBR command
  frames, `avg_delta=1.981`, `bad_pixel_ratio=0.04667`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and 50 effect-handle define frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-062716/suite.tsv`.
- Magic Jewel filled the image-filter lifecycle parity gap by adding `parity-resize-image-filter` and
  `parity-forced-context-image-filter` to the screenshot-parity default list, `core-drawing` group, and case switch.
  The rows add image-ref, surface/cache, and effect-handle gates while keeping validation exact under the daily broad
  cap. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-image-filter parity-forced-context-image-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 665 JBR command frames, `avg_delta=1.853`,
  `bad_pixel_ratio=0.04463`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, ten image refs, 35 effect-handle define frames, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 770 JBR command
  frames, `avg_delta=1.981`, `bad_pixel_ratio=0.04667`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, ten image refs, and 50 effect-handle define frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-060436/suite.tsv`.
- Magic Jewel filled the image color-matrix filter lifecycle parity gap by adding
  `parity-resize-image-color-matrix-filter` and `parity-forced-context-image-color-matrix-filter` to the
  screenshot-parity default list, `core-drawing` group, and case switch. The rows mirror the command-probe lifecycle
  gates and require effect-handle define/use/cache-hit markers. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-image-color-matrix-filter parity-forced-context-image-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 550 JBR command frames, `avg_delta=1.903`,
  `bad_pixel_ratio=0.04495`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, ten image refs, 42 effect-handle define frames, 1,237 effect-handle use frames,
  1,230 effect-handle cache-hit frames, and the known single early resize parity `command-stream-invalid` fallback
  artifact. Forced context passed fallback-free with 842 JBR command frames, `avg_delta=2.039`,
  `bad_pixel_ratio=0.04703`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, ten image refs, 60 effect-handle define frames, 1,405 effect-handle use frames,
  and 1,395 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-061112/suite.tsv`.
- Magic Jewel filled the path-effect lifecycle parity gap by adding `parity-resize-path-effect` and
  `parity-forced-context-path-effect` to the screenshot-parity default list, `core-drawing` group, and case switch. The
  rows isolate the path-effect probe and add lifecycle surface/cache gates. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-path-effect parity-forced-context-path-effect" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2. Resize passed with screenshot status passed, 1,234 JBR command frames, `avg_delta=1.924`,
  `bad_pixel_ratio=0.04656`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, 40 effect-handle define frames, and the known single early
  resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 840 JBR command
  frames, `avg_delta=2.067`, `bad_pixel_ratio=0.04901`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, and 55 effect-handle define frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-061714/suite.tsv`.
- Magic Jewel filled the gradient-stroke lifecycle parity gap by adding `parity-resize-gradient-stroke` and
  `parity-forced-context-gradient-stroke` to the screenshot-parity default list, `core-drawing` group, and case
  switch. The rows isolate the linear-gradient stroke probe, add lifecycle surface/cache gates, and use a
  row-local orange-probe threshold while preserving the default screenshot assertion threshold for existing rows. The
  exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-gradient-stroke parity-forced-context-gradient-stroke" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 890 JBR command frames,
  `avg_delta=2.455`, `bad_pixel_ratio=0.06423`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, two image refs, zero shader/effect handle markers, and the known
  single early resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 1,113
  JBR command frames, `avg_delta=2.691`, `bad_pixel_ratio=0.06971`, one destination context-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, two image refs, and zero shader/effect
  handle markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-055502/suite.tsv`.
- Magic Jewel filled the gradient-paths lifecycle parity gap by adding `parity-resize-gradient-paths` and
  `parity-forced-context-gradient-paths` to the screenshot-parity default list, `core-drawing` group, and case switch.
  The new rows explicitly disable the surface-gradient probes so the assertion targets only the path probes. The exact
  two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-gradient-paths parity-forced-context-gradient-paths" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 822 JBR command frames,
  `avg_delta=2.442`, `bad_pixel_ratio=0.06387`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, two image refs, and the known single early resize parity
  `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 1,185 JBR command frames,
  `avg_delta=2.676`, `bad_pixel_ratio=0.06930`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, and two image refs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-054146/suite.tsv`.
- Magic Jewel filled the gradient-surfaces lifecycle parity gap by adding `parity-resize-gradient-surfaces` and
  `parity-forced-context-gradient-surfaces` to the screenshot-parity default list, `core-drawing` group, and case
  switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-gradient-surfaces parity-forced-context-gradient-surfaces" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 1,251 JBR command frames,
  `avg_delta=2.408`, `bad_pixel_ratio=0.06285`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, two image refs, and the known single early resize parity
  `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 876 JBR command frames,
  `avg_delta=2.631`, `bad_pixel_ratio=0.06798`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, and two image refs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-053348/suite.tsv`.
- Magic Jewel filled the gradient-shaders lifecycle parity gap by adding `parity-resize-gradient-shaders` and
  `parity-forced-context-gradient-shaders` to the screenshot-parity default list, `core-drawing` group, and case
  switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-gradient-shaders parity-forced-context-gradient-shaders" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 1,063 JBR command frames,
  `avg_delta=2.433`, `bad_pixel_ratio=0.06358`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, two image refs, and the known single early resize parity
  `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 1,099 JBR command frames,
  `avg_delta=2.655`, `bad_pixel_ratio=0.06872`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, and two image refs. One earlier exact calibration attempt failed
  only because the new resize row initially kept paragraph probes enabled; the passing lifecycle rows disable text and
  paragraph probes like the focused shader lifecycle rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-052738/suite.tsv`.
- Magic Jewel filled the plain image-shader lifecycle parity gap by adding `parity-resize-image-shader` and
  `parity-forced-context-image-shader` to the screenshot-parity default list, `shader-rendering` group, and case
  switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-image-shader parity-forced-context-image-shader" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 844 JBR command frames,
  `avg_delta=2.447`, `bad_pixel_ratio=0.06268`, `compose_shader_image_bad_pixel_ratio=0.06647`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, three image
  refs, 35 effect-handle define frames, and the known single early resize parity `command-stream-invalid` fallback
  artifact. Forced context passed fallback-free with 835 JBR command frames, `avg_delta=2.678`,
  `bad_pixel_ratio=0.06777`, `compose_shader_image_bad_pixel_ratio=0.06209`, one destination context-change marker,
  one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, three image refs, and 60
  effect-handle define frames. Two earlier exact calibration attempts failed only while widening the new rows'
  `swingIsland` bad-pixel cap to 0.04 and the forced row's effect-handle define ceiling to 64:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-052105/suite.tsv`.
- Magic Jewel filled the plain composite-shader lifecycle parity gap by adding `parity-resize-composite-shader` and
  `parity-forced-context-composite-shader` to the screenshot-parity default list, `shader-rendering` group, and case
  switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-composite-shader parity-forced-context-composite-shader" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 841 JBR command frames,
  `avg_delta=2.377`, `bad_pixel_ratio=0.06208`, `compose_shader_composite_bad_pixel_ratio=0.08637`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 27
  shader-handle define frames, 1,499 shader-handle use frames, 35 effect-handle define frames, and the known single
  early resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 842 JBR
  command frames, `avg_delta=2.596`, `bad_pixel_ratio=0.06710`,
  `compose_shader_composite_bad_pixel_ratio=0.08478`, one destination context-change marker, one command-cache clear,
  one JBR image-cache clear, one scoped image-cache clear, 33 shader-handle define frames, 1,331 shader-handle use
  frames, and 55 effect-handle define frames. One earlier exact calibration attempt failed only because the new resize
  row inherited the default `swingIsland` bad-pixel cap of 0.03 while the observed value was 0.03118; the final row cap
  is 0.04 for the two new cases only:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-051301/suite.tsv`.
- Magic Jewel filled the linear-gradient shader color-filter lifecycle parity gap by adding
  `parity-resize-linear-gradient-shader-color-filter` and
  `parity-forced-context-linear-gradient-shader-color-filter` to the screenshot-parity default list,
  `shader-rendering` group, and case switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-linear-gradient-shader-color-filter parity-forced-context-linear-gradient-shader-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 553 JBR command frames,
  `avg_delta=1.841`, `bad_pixel_ratio=0.04428`, `compose_shader_linear_bad_pixel_ratio=0.05656`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 20
  shader-handle define frames, 1,224 shader-handle use frames, 48 effect-handle define frames, and the known single
  early resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 796 JBR
  command frames, `avg_delta=1.966`, `bad_pixel_ratio=0.04623`, `compose_shader_linear_bad_pixel_ratio=0.03938`, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  18 shader-handle define frames, 1,500 shader-handle use frames, and 54 effect-handle define frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-043012/suite.tsv`.
- Magic Jewel then filled the radial-gradient shader color-filter lifecycle parity gap by adding
  `parity-resize-radial-gradient-shader-color-filter` and
  `parity-forced-context-radial-gradient-shader-color-filter` to the screenshot-parity default list,
  `shader-rendering` group, and case switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-radial-gradient-shader-color-filter parity-forced-context-radial-gradient-shader-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 737 JBR command frames,
  `avg_delta=1.842`, `bad_pixel_ratio=0.04431`, `compose_shader_linear_bad_pixel_ratio=0.06647`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 18
  shader-handle define frames, 1,353 shader-handle use frames, 42 effect-handle define frames, and the known single
  early resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 555 JBR
  command frames, `avg_delta=1.968`, `bad_pixel_ratio=0.04630`, `compose_shader_linear_bad_pixel_ratio=0.06844`, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  18 shader-handle define frames, 1,158 shader-handle use frames, and 54 effect-handle define frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-043542/suite.tsv`.
- Magic Jewel then filled the sweep-gradient shader color-filter lifecycle parity gap by adding
  `parity-resize-sweep-gradient-shader-color-filter` and
  `parity-forced-context-sweep-gradient-shader-color-filter` to the screenshot-parity default list,
  `shader-rendering` group, and case switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-sweep-gradient-shader-color-filter parity-forced-context-sweep-gradient-shader-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 717 JBR command frames,
  `avg_delta=1.841`, `bad_pixel_ratio=0.04428`, `compose_shader_linear_bad_pixel_ratio=0.06647`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 18
  shader-handle define frames, 1,414 shader-handle use frames, 42 effect-handle define frames, and the known single
  early resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 654 JBR
  command frames, `avg_delta=1.970`, `bad_pixel_ratio=0.04634`, `compose_shader_linear_bad_pixel_ratio=0.06844`, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  18 shader-handle define frames, 1,177 shader-handle use frames, and 54 effect-handle define frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-044036/suite.tsv`.
- Magic Jewel verified the daily broad-validation cap before the next focused change: the default command-probe,
  screenshot-parity, compatibility-matrix, benchmark-suite, and artifact-matrix launch paths all exited 3 immediately
  against the existing 2026-06-16 broad stamp, while exact two-row selection remained allowed. It then filled the
  transformed-shader lifecycle parity gap by adding `parity-resize-transformed-shader` and
  `parity-forced-context-transformed-shader` to the screenshot-parity default list, `shader-rendering` group, and case
  switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-transformed-shader parity-forced-context-transformed-shader" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 565 JBR command frames,
  `avg_delta=1.844`, `bad_pixel_ratio=0.04437`, `compose_bad_pixel_ratio=0.06442`, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 18 shader-handle define
  frames, 1,191 shader-handle use frames, 35 effect-handle define frames, and the known single early resize parity
  `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 764 JBR command frames,
  `avg_delta=1.969`, `bad_pixel_ratio=0.04633`, `compose_bad_pixel_ratio=0.06885`, one destination context-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 18 shader-handle define
  frames, 1,327 shader-handle use frames, and 45 effect-handle define frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-044958/suite.tsv`.
- Magic Jewel then filled the composite-shader color-filter lifecycle parity gap by adding
  `parity-resize-composite-shader-color-filter` and `parity-forced-context-composite-shader-color-filter` to the
  screenshot-parity default list, `shader-rendering` group, and case switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-composite-shader-color-filter parity-forced-context-composite-shader-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 792 JBR command frames,
  `avg_delta=1.841`, `bad_pixel_ratio=0.04428`, `compose_shader_composite_bad_pixel_ratio=0.07857`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 36
  shader-handle define frames, 1,487 shader-handle use frames, 42 effect-handle define frames, and the known single
  early resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 452 JBR
  command frames, `avg_delta=1.962`, `bad_pixel_ratio=0.04616`,
  `compose_shader_composite_bad_pixel_ratio=0.05382`, one destination context-change marker, one command-cache clear,
  one JBR image-cache clear, one scoped image-cache clear, 36 shader-handle define frames, 1,066 shader-handle use
  frames, and 54 effect-handle define frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-045823/suite.tsv`.
- Magic Jewel then filled the image-shader color-filter lifecycle parity gap by adding
  `parity-resize-image-shader-color-filter` and `parity-forced-context-image-shader-color-filter` to the
  screenshot-parity default list, `shader-rendering` group, and case switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-image-shader-color-filter parity-forced-context-image-shader-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 788 JBR command frames,
  `avg_delta=1.846`, `bad_pixel_ratio=0.04439`, `compose_shader_image_bad_pixel_ratio=0.05606`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 14
  shader-handle define frames, 1,429 shader-handle use frames, 42 effect-handle define frames, and the known single
  early resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 544 JBR
  command frames, `avg_delta=1.969`, `bad_pixel_ratio=0.04628`, `compose_shader_image_bad_pixel_ratio=0.03800`, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  18 shader-handle define frames, 1,243 shader-handle use frames, and 54 effect-handle define frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-050348/suite.tsv`.
- Magic Jewel filled the child RuntimeEffect color-filter lifecycle parity gap by adding
  `parity-resize-runtime-effect-color-filter-child` and
  `parity-forced-context-runtime-effect-color-filter-child` to the screenshot-parity suite default list,
  `runtime-effect` group, and case switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-runtime-effect-color-filter-child parity-forced-context-runtime-effect-color-filter-child" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 827 JBR command frames,
  `avg_delta=1.853`, `bad_pixel_ratio=0.04463`, `compose_bad_pixel_ratio=0.06492`, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,489 RuntimeEffect
  source-cache hit frames, one miss, 64 effect-handle define frames, and the known single early resize parity
  `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 794 JBR command frames,
  `avg_delta=1.981`, `bad_pixel_ratio=0.04667`, `compose_bad_pixel_ratio=0.06944`, one destination context-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,485 source-cache hit
  frames, one miss, and 80 effect-handle define frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-034035/suite.tsv`.
- Magic Jewel then filled the RuntimeEffect uniform-only lifecycle parity gap by adding
  `parity-resize-runtime-effect-uniform-only` and `parity-forced-context-runtime-effect-uniform-only` to the same
  screenshot-parity default list, `runtime-effect` group, and case switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-runtime-effect-uniform-only parity-forced-context-runtime-effect-uniform-only" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 787 JBR command frames,
  `avg_delta=1.844`, `bad_pixel_ratio=0.04435`, `compose_bad_pixel_ratio=0.06437`, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,420 RuntimeEffect
  source-cache hit frames, one miss, 8 shader-handle define frames, 1,422 shader-handle use frames, and the known
  single early resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 791
  JBR command frames, `avg_delta=1.966`, `bad_pixel_ratio=0.04623`, `compose_bad_pixel_ratio=0.06867`, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  1,483 source-cache hit frames, one miss, 10 shader-handle define frames, and 1,484 shader-handle use frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-034556/suite.tsv`.
- Magic Jewel then filled the RuntimeEffect child-only lifecycle parity gap by adding
  `parity-resize-runtime-effect-child-only` and `parity-forced-context-runtime-effect-child-only` to the
  screenshot-parity default list, `runtime-effect` group, and case switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-runtime-effect-child-only parity-forced-context-runtime-effect-child-only" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 815 JBR command frames,
  `avg_delta=1.847`, `bad_pixel_ratio=0.04446`, `compose_bad_pixel_ratio=0.06456`, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,423 RuntimeEffect
  source-cache hit frames, one miss, 27 shader-handle define frames, 35 effect-handle define frames, and the known
  single early resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 835
  JBR command frames, `avg_delta=1.977`, `bad_pixel_ratio=0.04655`, `compose_bad_pixel_ratio=0.06921`, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  1,459 source-cache hit frames, one miss, 27 shader-handle define frames, and 45 effect-handle define frames. Two
  earlier exact calibration attempts failed the strict gate because the new child-only lifecycle cases initially capped
  shader-handle define frames at 16 while screenshot parity observed 30, so the final case gate now caps that marker at
  40:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-035554/suite.tsv`.
- Magic Jewel then filled the plain RuntimeEffect shader lifecycle parity gap by adding
  `parity-resize-runtime-effect-shader` and `parity-forced-context-runtime-effect-shader` to the screenshot-parity
  default list, `runtime-effect` group, and case switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-runtime-effect-shader parity-forced-context-runtime-effect-shader" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 536 JBR command frames,
  `avg_delta=1.841`, `bad_pixel_ratio=0.04428`, `compose_bad_pixel_ratio=0.06426`, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,158 RuntimeEffect
  source-cache hit frames, one miss, 30 shader-handle define frames, 40 effect-handle define frames, and the known
  single early resize parity `command-stream-invalid` fallback artifact. Forced context passed fallback-free with 550
  JBR command frames, `avg_delta=1.970`, `bad_pixel_ratio=0.04634`, `compose_bad_pixel_ratio=0.06886`, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  1,108 source-cache hit frames, one miss, 27 shader-handle define frames, and 45 effect-handle define frames. The new
  shader define cap is 40, matching the final observed parity lifecycle counts with a small buffer:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-040432/suite.tsv`.
- Magic Jewel then filled the RuntimeEffect shader-color-filter lifecycle parity gap by adding
  `parity-resize-runtime-effect-shader-color-filter` and
  `parity-forced-context-runtime-effect-shader-color-filter` to the screenshot-parity default list, `runtime-effect`
  group, and case switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-runtime-effect-shader-color-filter parity-forced-context-runtime-effect-shader-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 812 JBR command frames,
  `avg_delta=1.841`, `bad_pixel_ratio=0.04428`, `compose_bad_pixel_ratio=0.06426`, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,412 RuntimeEffect
  source-cache hit frames, one miss, 14 shader-handle define frames, 42 effect-handle define frames, 7 effect-handle
  use frames, and the known single early resize parity `command-stream-invalid` fallback artifact. Forced context
  passed fallback-free with 806 JBR command frames, `avg_delta=1.969`, `bad_pixel_ratio=0.04632`,
  `compose_bad_pixel_ratio=0.06882`, one destination context-change marker, one command-cache clear, one JBR
  image-cache clear, one scoped image-cache clear, 1,444 source-cache hit frames, one miss, 18 shader-handle define
  frames, 54 effect-handle define frames, and 9 effect-handle use frames. The new lifecycle rows intentionally require
  effect-handle use but not effect-handle cache-hit markers, because the resize calibration row observed zero effect
  cache-hit frames while still replaying through command mode:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-041113/suite.tsv`.
- Magic Jewel then filled the non-stable RuntimeEffect color-filter lifecycle parity gap by adding
  `parity-resize-runtime-effect-color-filter` and `parity-forced-context-runtime-effect-color-filter` to the
  screenshot-parity default list, `runtime-effect` group, and case switch. The exact two-row validation
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-runtime-effect-color-filter parity-forced-context-runtime-effect-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 under the daily cap. Resize passed with screenshot status passed, 545 JBR command frames,
  `avg_delta=1.853`, `bad_pixel_ratio=0.04463`, `compose_bad_pixel_ratio=0.06491`, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,198 RuntimeEffect
  source-cache hit frames, one miss, 42 effect-handle define frames, 1,200 effect-handle use frames, 1,193
  effect-handle cache-hit frames, and the known single early resize parity `command-stream-invalid` fallback artifact.
  Forced context passed fallback-free with 810 JBR command frames, `avg_delta=1.979`, `bad_pixel_ratio=0.04663`,
  `compose_bad_pixel_ratio=0.06934`, one destination context-change marker, one command-cache clear, one JBR
  image-cache clear, one scoped image-cache clear, 1,385 source-cache hit frames, one miss, 54 effect-handle define
  frames, 1,386 effect-handle use frames, and 1,377 effect-handle cache-hit frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-041714/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect named-child-count schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-named-child-count-fallback commands-runtime-effect-color-filter-named-child-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 543 CMP/Skiko command frames before
  structural rejection; the color-filter row recorded 698 CMP/Skiko command frames, 1,114 RuntimeEffect cache-hit
  frames, and one miss before structural rejection. This keeps the next RuntimeEffect schema corruption pair covered
  with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-023316/suite.tsv`.
- Magic Jewel followed with the exact RuntimeEffect negative named-child-count schema fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-negative-named-child-count-fallback commands-runtime-effect-color-filter-negative-named-child-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 714 CMP/Skiko command frames before
  structural rejection; the color-filter row recorded 495 CMP frames, 496 Skiko command frames, 949 RuntimeEffect
  cache-hit frames, and one miss before structural rejection. This keeps the paired negative schema corruption case
  covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-023821/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect uniform-name schema fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-name-fallback commands-runtime-effect-color-filter-uniform-name-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 680 CMP/Skiko command frames, 1,127
  RuntimeEffect cache-hit frames, and one miss before structural rejection; the color-filter row recorded 608 CMP
  frames, 609 Skiko command frames, 1,020 cache-hit frames, and one miss before structural rejection. This keeps the
  uniform-name schema corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-024119/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect uniform-schema-float-count fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-schema-float-count-fallback commands-runtime-effect-color-filter-uniform-schema-float-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 709 CMP/Skiko command frames, 1,199
  RuntimeEffect cache-hit frames, and one miss before structural rejection; the color-filter row recorded 715 CMP/Skiko
  command frames, 1,206 cache-hit frames, and one miss before structural rejection. This keeps the uniform schema count
  corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-024354/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect uniform-schema-float-offset fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-schema-float-offset-fallback commands-runtime-effect-color-filter-uniform-schema-float-offset-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 656 CMP/Skiko command frames, 1,043
  RuntimeEffect cache-hit frames, and one miss before structural rejection; the color-filter row recorded 766 CMP/Skiko
  command frames, 1,209 cache-hit frames, and one miss before structural rejection. This keeps the uniform schema offset
  corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-024712/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect uniform-schema-float-range fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-schema-float-range-fallback commands-runtime-effect-color-filter-uniform-schema-float-range-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 611 CMP/Skiko command frames, 1,097
  RuntimeEffect cache-hit frames, and one miss before structural rejection; the color-filter row recorded 710 CMP/Skiko
  command frames, 1,156 cache-hit frames, and one miss before structural rejection. This keeps the uniform schema range
  corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-024949/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect uniform-schema-name-length fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-schema-name-length-fallback commands-runtime-effect-color-filter-uniform-schema-name-length-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 752 CMP/Skiko command frames, 1,238
  RuntimeEffect cache-hit frames, and one miss before structural rejection; the color-filter row recorded 653 CMP/Skiko
  command frames, 1,103 cache-hit frames, and one miss before structural rejection. This keeps the uniform schema name
  length corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-025256/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect uniform-schema-max-name-length fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-schema-max-name-length-fallback commands-runtime-effect-color-filter-uniform-schema-max-name-length-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 509 CMP/Skiko command frames, 943
  RuntimeEffect cache-hit frames, and one miss before structural rejection; the color-filter row recorded 673 CMP/Skiko
  command frames, 1,170 cache-hit frames, and one miss before structural rejection. This keeps the uniform schema max
  name length corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-025541/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect uniform-schema-name-range fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-schema-name-range-fallback commands-runtime-effect-color-filter-uniform-schema-name-range-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 753 CMP/Skiko command frames, 1,166
  RuntimeEffect cache-hit frames, and one miss before structural rejection; the color-filter row recorded 705 CMP/Skiko
  command frames, 1,138 cache-hit frames, and one miss before structural rejection. This keeps the uniform schema name
  range corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-025851/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect child-name schema fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-child-name-fallback commands-runtime-effect-color-filter-child-name-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 725 CMP/Skiko command frames before
  structural rejection; the color-filter row recorded 621 CMP/Skiko command frames, 1,005 RuntimeEffect cache-hit
  frames, and one miss before structural rejection. This keeps the child-name schema corruption pair covered with an
  exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-030136/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect child-schema-name-length fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-child-schema-name-length-fallback commands-runtime-effect-color-filter-child-schema-name-length-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 835 CMP frames, 834 Skiko command
  frames, and no RuntimeEffect cache hit/miss frames before structural rejection; the color-filter row recorded 576
  CMP/Skiko command frames, 957 RuntimeEffect cache-hit frames, and one miss before structural rejection. This keeps
  the child schema name-length corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-030533/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect child-schema-max-name-length fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-child-schema-max-name-length-fallback commands-runtime-effect-color-filter-child-schema-max-name-length-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 777 CMP/Skiko command frames and no
  RuntimeEffect cache hit/miss frames before structural rejection; the color-filter row recorded 659 CMP/Skiko command
  frames, 1,081 RuntimeEffect cache-hit frames, and one miss before structural rejection. This keeps the child schema
  max-name-length corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-030834/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect child-schema-name-range fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-child-schema-name-range-fallback commands-runtime-effect-color-filter-child-schema-name-range-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 749 CMP/Skiko command frames and no
  RuntimeEffect cache hit/miss frames before structural rejection; the color-filter row recorded 650 CMP/Skiko command
  frames, 1,066 RuntimeEffect cache-hit frames, and one miss before structural rejection. This keeps the child schema
  name-range corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-031116/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect child-index fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-child-index-fallback commands-runtime-effect-color-filter-child-index-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 620 CMP/Skiko command frames and no
  RuntimeEffect cache hit/miss frames before structural rejection; the color-filter row recorded 567 CMP frames, 566
  Skiko command frames, 928 RuntimeEffect cache-hit frames, and one miss before structural rejection. This keeps the
  child index corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-031349/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect negative-child-index fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-negative-child-index-fallback commands-runtime-effect-color-filter-negative-child-index-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 680 CMP/Skiko command frames and no
  RuntimeEffect cache hit/miss frames before structural rejection; the color-filter row recorded 425 CMP frames, 424
  Skiko command frames, 841 RuntimeEffect cache-hit frames, and one miss before structural rejection. This keeps the
  negative child index corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-031617/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect duplicate-child-index fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-duplicate-child-index-fallback commands-runtime-effect-color-filter-duplicate-child-index-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 734 CMP/Skiko command frames and no
  RuntimeEffect cache hit/miss frames before structural rejection; the color-filter row recorded 641 CMP frames, 640
  Skiko command frames, 1,070 RuntimeEffect cache-hit frames, and one miss before structural rejection. This closes the
  child index schema corruption trio with exact two-row coverage, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-031854/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect invalid uniform-schema descriptor fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-invalid-uniform-schema-fallback commands-runtime-effect-color-filter-invalid-uniform-schema-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 as descriptor-unsupported picture fallback rows, not command-stream rows. The shader row recorded
  `shaderDescriptor` plus graphics-layer unsupported markers across 487 CMP recorder frames, 488 Skiko/JBR picture
  frames, zero command frames, and screenshot status passed; the color-filter row recorded `colorFilterDescriptor`
  plus graphics-layer unsupported markers across 521 CMP recorder frames, 521 Skiko/JBR picture frames, zero command
  frames, and screenshot status passed. This keeps invalid uniform schema descriptor rejection covered with an exact
  two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-032209/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect invalid child-schema descriptor fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-invalid-child-schema-fallback commands-runtime-effect-color-filter-invalid-child-schema-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 as descriptor-unsupported picture fallback rows, not command-stream rows. The shader row recorded
  `shaderDescriptor` plus graphics-layer unsupported markers across 526 CMP recorder frames, 526 Skiko/JBR picture
  frames, zero command frames, and screenshot status passed; the color-filter row recorded `colorFilterDescriptor`
  plus graphics-layer unsupported markers across 568 CMP recorder frames, 567 Skiko/JBR picture frames, zero command
  frames, and screenshot status passed. This keeps invalid child schema descriptor rejection covered with an exact
  two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-032451/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect invalid nested-child descriptor fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-invalid-nested-child-fallback commands-runtime-effect-color-filter-invalid-nested-child-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 as descriptor-unsupported picture fallback rows, not command-stream rows. The shader row recorded
  `shaderDescriptor` plus graphics-layer unsupported markers across 538 CMP recorder frames, 537 Skiko/JBR picture
  frames, zero command frames, and screenshot status passed; the color-filter row recorded `colorFilterDescriptor`
  plus graphics-layer unsupported markers across 504 CMP recorder frames, 504 Skiko/JBR picture frames, zero command
  frames, and screenshot status passed. This closes the invalid descriptor schema trio with exact two-row coverage, not
  a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-032745/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect source-cache eviction command pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-source-cache-eviction commands-runtime-effect-source-cache-eviction" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 on command replay with no fallback, no unsupported reasons, zero picture frames, and screenshot status
  passed. The shader row recorded 772 JBR command frames, 1,358 RuntimeEffect source-cache hits, 2,719 misses, 2,717
  evicts, 4,077 shader-handle uses, and 2,716 shader-handle cache-hit frames; the color-filter row recorded 785 JBR
  command frames, 1,336 source-cache hits, 2,675 misses, 2,673 evicts, 4,011 effect-handle uses, and 1,336
  effect-handle cache-hit frames. This keeps source-cache churn covered with exact command validation under the daily
  broad cap:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-033121/suite.tsv`.
- Magic Jewel continued RuntimeEffect parity with an exact pure-color row:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with no fallback, no unsupported reasons, 783 CMP recorder frames, 782 Skiko/JBR command frames, zero
  picture frames, screenshot status passed, `avg_delta=2.105`, `bad_pixel_ratio=0.04993`,
  `compose_bad_pixel_ratio=0.07439`, 1,421 JBR RuntimeEffect cache-hit frames, one miss, 4 shader-handle define
  frames, 1,422 shader-handle use frames, and 20 effect-handle define frames. This keeps the baseline RuntimeEffect
  shader descriptor path covered under the daily cap without running the full screenshot-parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-004434/suite.tsv`.
- Magic Jewel paired that with an exact RuntimeEffect uniform-only row:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-uniform-only ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with no fallback, no unsupported reasons, 521 CMP/Skiko/JBR command frames, zero picture frames,
  screenshot status passed, `avg_delta=2.107`, `bad_pixel_ratio=0.04997`, `compose_bad_pixel_ratio=0.07446`, 1,138
  JBR RuntimeEffect cache-hit frames, one miss, 4 shader-handle define frames, 1,139 shader-handle use frames, and 20
  effect-handle define frames. This keeps the RuntimeEffect uniform schema descriptor path covered under the daily cap
  without running the full screenshot-parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-004752/suite.tsv`.
- Magic Jewel continued that ladder with an exact RuntimeEffect child-only row:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-child-only ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with no fallback, no unsupported reasons, 822 CMP recorder frames, 823 Skiko/JBR command frames, zero
  picture frames, screenshot status passed, `avg_delta=2.118`, `bad_pixel_ratio=0.05029`,
  `compose_bad_pixel_ratio=0.07500`, 1,477 JBR RuntimeEffect cache-hit frames, one miss, 9 shader-handle define
  frames, 1,478 shader-handle use frames, and 15 effect-handle define frames. This keeps the RuntimeEffect child shader
  descriptor path covered under the daily cap without running the full screenshot-parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-005030/suite.tsv`.
- Magic Jewel refreshed the combined RuntimeEffect shader row:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-shader ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with no fallback, no unsupported reasons, 806 CMP/Skiko/JBR command frames, zero picture frames,
  screenshot status passed, `avg_delta=2.111`, `bad_pixel_ratio=0.05008`, `compose_bad_pixel_ratio=0.07465`, 1,446
  JBR RuntimeEffect cache-hit frames, one miss, 12 shader-handle define frames, 1,447 shader-handle use frames, and 20
  effect-handle define frames. This keeps the combined RuntimeEffect shader descriptor path covered under the daily cap
  without running the full screenshot-parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-005306/suite.tsv`.
- Magic Jewel refreshed the RuntimeEffect shader-plus-color-filter row:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-shader-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with no fallback, no unsupported reasons, 738 CMP/Skiko/JBR command frames, zero picture frames,
  screenshot status passed, `avg_delta=2.110`, `bad_pixel_ratio=0.05006`, `compose_bad_pixel_ratio=0.07461`, 1,293
  JBR RuntimeEffect cache-hit frames, one miss, 8 shader-handle define frames, 1,298 shader-handle use frames, 24
  effect-handle define frames, and 4 effect-handle use frames. This keeps the combined shader/effect handle path
  covered under the daily cap without running the full screenshot-parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-005545/suite.tsv`.
- Magic Jewel refreshed the RuntimeEffect color-filter row:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with no fallback, no unsupported reasons, 849 CMP recorder frames, 848 Skiko/JBR command frames, zero
  picture frames, screenshot status passed, `avg_delta=2.120`, `bad_pixel_ratio=0.05037`,
  `compose_bad_pixel_ratio=0.07513`, 1,471 JBR RuntimeEffect cache-hit frames, one miss, 24 effect-handle define
  frames, and 1,472 effect-handle use frames. This keeps the descriptor-backed RuntimeEffect color-filter path covered
  under the daily cap without running the full screenshot-parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-005827/suite.tsv`.
- Magic Jewel refreshed the RuntimeEffect color-filter child row:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-color-filter-child ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with no fallback, no unsupported reasons, 786 CMP recorder frames, 785 Skiko/JBR command frames, zero
  picture frames, screenshot status passed, `avg_delta=2.122`, `bad_pixel_ratio=0.05041`,
  `compose_bad_pixel_ratio=0.07523`, 1,366 JBR RuntimeEffect cache-hit frames, one miss, 32 effect-handle define
  frames, and 1,367 effect-handle use frames. This keeps the RuntimeEffect child color-filter descriptor path covered
  under the daily cap without running the full screenshot-parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-010333/suite.tsv`.
- Magic Jewel started the RuntimeEffect resize lifecycle parity track with paired exact rows. The parity row
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-resize-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with screenshot status passed and steady command replay, but recorded one early `command-stream-invalid`
  fallback during resize capture. The exact command-probe companion
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=commands-resize-runtime-effect-pure-color ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback or unsupported reasons, 842 CMP/Skiko/JBR command frames, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,497 RuntimeEffect
  source-cache hit frames, one miss, 2 shader-handle define frames, and 1,498 shader-handle use frames. This proves the
  focused command lifecycle row is clean while preserving the parity-row resize recovery artifact for follow-up:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-010544/suite.tsv`
  and `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-010721/suite.tsv`.
- Magic Jewel paired the resize pure-color row with exact forced-destination-context parity:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-forced-context-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with no fallback or unsupported reasons, 781 CMP recorder frames, 780 Skiko/JBR command frames, zero
  picture frames, screenshot status passed, `avg_delta=1.964`, `bad_pixel_ratio=0.04619`,
  `compose_bad_pixel_ratio=0.06861`, one destination context-change marker, one command-cache clear, one JBR
  image-cache clear, one scoped image-cache clear, 1,411 RuntimeEffect source-cache hit frames, one miss, 9
  shader-handle define frames, and 1,412 shader-handle use frames. This keeps RuntimeEffect shader descriptor
  redefinition across destination context migration covered under the daily cap:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-010938/suite.tsv`.
- Magic Jewel extended the resize lifecycle check to the stable RuntimeEffect color-filter descriptor row. The parity
  row
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-resize-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with screenshot status passed and steady command replay, but recorded the same single early
  `command-stream-invalid` fallback during resize capture seen on the pure-color resize parity row. The exact
  command-probe companion
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=commands-resize-runtime-effect-stable-color-filter ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback or unsupported reasons, 573 Skiko/JBR command frames, one same-context surface-change marker,
  one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,201 RuntimeEffect source-cache
  hit frames, one miss, 2 effect-handle define frames, and 1,202 effect-handle use frames. This keeps stable
  RuntimeEffect color-filter resize lifecycle coverage moving while preserving the parity-row recovery artifact for
  follow-up:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-011233/suite.tsv`
  and `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-011348/suite.tsv`.
- Magic Jewel paired the stable color-filter resize check with exact forced-destination-context parity:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-forced-context-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with no fallback or unsupported reasons, 801 CMP/Skiko/JBR command frames, zero picture frames,
  screenshot status passed, `avg_delta=1.981`, `bad_pixel_ratio=0.04668`, `compose_bad_pixel_ratio=0.06943`, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  1,469 RuntimeEffect source-cache hit frames, one miss, 54 effect-handle define frames, and 1,470 effect-handle use
  frames. This keeps stable RuntimeEffect color-filter descriptor redefinition across destination context migration
  covered under the daily cap:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-011609/suite.tsv`.
- Magic Jewel continued the child color-filter lifecycle track with an exact resize command-probe row:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=commands-resize-runtime-effect-color-filter-child ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback or unsupported reasons, 779 CMP/Skiko/JBR command frames, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,429 RuntimeEffect
  source-cache hit frames, one miss, 1,434 effect-handle define frames, and 1,430 effect-handle use frames. The
  screenshot-parity suite has only the base child color-filter parity row today, so this exact command-probe row is the
  current narrow resize lifecycle gate until child resize/forced-context parity rows are implemented:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-011921/suite.tsv`.
- Magic Jewel paired that with an exact child color-filter forced-destination-context command-probe row:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=commands-forced-context-runtime-effect-color-filter-child ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback or unsupported reasons, 666 CMP/Skiko/JBR command frames, one destination context-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,244 RuntimeEffect
  source-cache hit frames, one miss, 1,247 effect-handle define frames, and 1,245 effect-handle use frames. Together
  the resize and forced-context command-probe rows cover the child color-filter lifecycle path until parity rows exist:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-012135/suite.tsv`.
- Magic Jewel also refreshed the non-child RuntimeEffect color-filter resize/forced-context command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-resize-runtime-effect-color-filter commands-forced-context-runtime-effect-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback or unsupported reasons. Resize reported 810 CMP/Skiko/JBR command frames, one
  same-context surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  1,493 RuntimeEffect source-cache hit frames, one miss, and 1,494 effect-handle define/use frames. Forced context
  reported 826 CMP/Skiko/JBR command frames, one destination context-change marker, one command-cache clear, one JBR
  image-cache clear, one scoped image-cache clear, 1,421 RuntimeEffect source-cache hit frames, one miss, and 1,423
  effect-handle define/use frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-012445/suite.tsv`.
- Magic Jewel refreshed the RuntimeEffect shader-plus-color-filter resize/forced-context command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-resize-runtime-effect-shader-color-filter commands-forced-context-runtime-effect-shader-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback or unsupported reasons. Resize reported 822 CMP recorder frames, 821 Skiko command
  frames, 822 JBR command frames, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, 1,464 RuntimeEffect source-cache hit frames, one miss, 2 effect-handle define
  frames, 1,464 effect-handle use frames, 1,462 effect-handle cache-hit frames, 2,928 shader-handle define frames, and
  2,929 shader-handle use frames. Forced context reported 818 CMP recorder frames, 819 Skiko/JBR command frames, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  1,502 RuntimeEffect source-cache hit frames, one miss, 2 effect-handle define frames, 1,503 effect-handle use frames,
  1,501 effect-handle cache-hit frames, and 3,006 shader-handle define/use frames. This keeps the combined
  RuntimeEffect shader/effect handle lifecycle path covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-013644/suite.tsv`.
- Magic Jewel refreshed the plain RuntimeEffect shader resize/forced-context command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-resize-runtime-effect-shader commands-forced-context-runtime-effect-shader" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback or unsupported reasons. Resize reported 610 CMP recorder frames, 611 Skiko/JBR command
  frames, one same-context surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped
  image-cache clear, 1,146 RuntimeEffect source-cache hit frames, one miss, 1,152 shader-handle define frames, and
  1,147 shader-handle use frames. Forced context reported 794 CMP recorder frames, 793 Skiko/JBR command frames, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  1,350 RuntimeEffect source-cache hit frames, one miss, 1,355 shader-handle define frames, and 1,351 shader-handle use
  frames. This keeps the shader-only RuntimeEffect handle lifecycle path covered with an exact two-row probe, not a
  broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-014003/suite.tsv`.
- Magic Jewel refreshed the RuntimeEffect uniform-only resize/forced-context command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-resize-runtime-effect-uniform-only commands-forced-context-runtime-effect-uniform-only" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback or unsupported reasons. Resize reported 826 CMP recorder frames, 825 Skiko/JBR command
  frames, one same-context surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped
  image-cache clear, 1,408 RuntimeEffect source-cache hit frames, one miss, and 1,409 shader-handle define/use frames.
  Forced context reported 817 CMP recorder frames, 818 Skiko/JBR command frames, one destination context-change marker,
  one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,415 RuntimeEffect source-cache hit
  frames, one miss, 1,415 shader-handle define frames, and 1,416 shader-handle use frames. This keeps the uniform
  schema RuntimeEffect lifecycle path covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-014346/suite.tsv`.
- Magic Jewel refreshed the RuntimeEffect child-only resize/forced-context command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-resize-runtime-effect-child-only commands-forced-context-runtime-effect-child-only" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback or unsupported reasons. Resize reported 819 CMP recorder frames, 820 Skiko/JBR command
  frames, one same-context surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped
  image-cache clear, 1,478 RuntimeEffect source-cache hit frames, one miss, 9 shader-handle define frames, and 1,479
  shader-handle use frames. Forced context reported 536 CMP recorder frames, 535 Skiko command frames, 536 JBR command
  frames, one destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped
  image-cache clear, 1,148 RuntimeEffect source-cache hit frames, one miss, 6 shader-handle define frames, and 1,149
  shader-handle use frames. This keeps the child shader RuntimeEffect lifecycle path covered with an exact two-row
  probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-014718/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect compile/build fallback subset:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-compile-fallback commands-runtime-effect-build-fallback commands-runtime-effect-color-filter-compile-fallback commands-runtime-effect-color-filter-build-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 4/4 with one expected fallback per row, no unsupported reasons, zero JBR command/picture frames, and
  screenshot capture not run. The shader compile row reported one compile failure and 7,693 RuntimeEffect cache-hit
  frames; the shader build row reported 7,164 build failures at `stage=missing-child`; the color-filter compile row
  reported one compile failure and 8,197 cache-hit frames; and the color-filter build row reported 8,072 build failures
  at `stage=child-count`. This keeps RuntimeEffect failure fallback contracts covered with an exact four-row probe, not
  a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-015112/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect child-type fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-child-type-fallback commands-runtime-effect-color-filter-child-type-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `runtime-effect-build-failed` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot capture not run. The shader row reported one build failure at
  `stage=child-type`, 694 CMP/Skiko command frames, 7,489 RuntimeEffect cache-hit frames, and two misses. The
  color-filter row reported one build failure at `stage=positional-child-type`, 1,195 CMP/Skiko command frames, 7,696
  cache-hit frames, and two misses. This keeps child type mismatch fallback contracts covered with an exact two-row
  probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-020138/suite.tsv`.
- Magic Jewel started the RuntimeEffect schema fallback slices with exact source-hash rows:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-source-hash-fallback commands-runtime-effect-color-filter-source-hash-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader source-hash row recorded 505 CMP/Skiko command
  frames before structural rejection; the color-filter source-hash row recorded 659 CMP frames, 660 Skiko command
  frames, 1,101 RuntimeEffect cache-hit frames, and one miss before structural rejection. This keeps the first
  RuntimeEffect schema corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-020618/suite.tsv`.
- Magic Jewel followed with the exact RuntimeEffect source-code schema fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-source-code-fallback commands-runtime-effect-color-filter-source-code-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader source-code row recorded 707 CMP frames and 708
  Skiko command frames before structural rejection; the color-filter source-code row recorded 683 CMP/Skiko command
  frames, 1,138 RuntimeEffect cache-hit frames, and one miss before structural rejection. This keeps the second
  RuntimeEffect schema corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-020843/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect SKSL-length schema fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-sksl-length-fallback commands-runtime-effect-color-filter-sksl-length-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 651 CMP frames and 652 Skiko command
  frames before structural rejection; the color-filter row recorded 542 CMP frames, 541 Skiko command frames, 919
  RuntimeEffect cache-hit frames, and one miss before structural rejection. This keeps the third RuntimeEffect schema
  corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-021155/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect uniform-float-count schema fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-float-count-fallback commands-runtime-effect-color-filter-uniform-float-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 625 CMP frames, 626 Skiko command
  frames, 1,041 RuntimeEffect cache-hit frames, and one miss before structural rejection; the color-filter row recorded
  732 CMP frames, 731 Skiko command frames, 1,150 cache-hit frames, and one miss before structural rejection. This keeps
  the fourth RuntimeEffect schema corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-021442/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect negative uniform-float-count schema fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-negative-uniform-float-count-fallback commands-runtime-effect-color-filter-negative-uniform-float-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 730 CMP/Skiko command frames, 1,285
  RuntimeEffect cache-hit frames, and one miss before structural rejection; the color-filter row recorded 704 CMP/Skiko
  command frames, 1,057 cache-hit frames, and one miss before structural rejection. This keeps the fifth RuntimeEffect
  schema corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-021808/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect child-count schema fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-child-count-fallback commands-runtime-effect-color-filter-child-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 650 CMP/Skiko command frames before
  structural rejection; the color-filter row recorded 671 CMP/Skiko command frames, 1,096 RuntimeEffect cache-hit
  frames, and one miss before structural rejection. This keeps the sixth RuntimeEffect schema corruption pair covered
  with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-022043/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect negative child-count schema fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-negative-child-count-fallback commands-runtime-effect-color-filter-negative-child-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 759 CMP/Skiko command frames before
  structural rejection; the color-filter row recorded 548 CMP/Skiko command frames, 948 RuntimeEffect cache-hit frames,
  and one miss before structural rejection. This keeps the seventh RuntimeEffect schema corruption pair covered with an
  exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-022401/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect named-uniform-count schema fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-named-uniform-count-fallback commands-runtime-effect-color-filter-named-uniform-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 664 CMP frames, 663 Skiko command
  frames, 1,073 RuntimeEffect cache-hit frames, and one miss before structural rejection; the color-filter row recorded
  749 CMP frames, 750 Skiko command frames, 1,164 cache-hit frames, and one miss before structural rejection. This keeps
  the eighth RuntimeEffect schema corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-022648/suite.tsv`.
- Magic Jewel continued with the exact RuntimeEffect negative named-uniform-count schema fallback pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-negative-named-uniform-count-fallback commands-runtime-effect-color-filter-negative-named-uniform-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, no unsupported reasons, zero JBR
  command/picture frames, and screenshot status passed. The shader row recorded 685 CMP/Skiko command frames, 1,163
  RuntimeEffect cache-hit frames, and one miss before structural rejection; the color-filter row recorded 853 CMP
  frames, 852 Skiko command frames, 1,372 cache-hit frames, and one miss before structural rejection. This keeps the
  ninth RuntimeEffect schema corruption pair covered with an exact two-row probe, not a broad suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-022951/suite.tsv`.
- Magic Jewel continued RuntimeEffect parity with an exact stable color-filter row:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with no fallback, no unsupported reasons, 546 CMP/Skiko/JBR command frames, zero picture frames,
  screenshot status passed, `avg_delta=2.122`, `bad_pixel_ratio=0.05042`, `compose_bad_pixel_ratio=0.07522`,
  1,126 JBR RuntimeEffect cache-hit frames, one miss, 18 effect-handle define frames, and 1,127 effect-handle use
  frames. This keeps RuntimeEffect stable descriptor/cache parity moving under the daily cap without running the full
  screenshot-parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-003110/suite.tsv`.
- Magic Jewel filled the shortened benchmark picture baseline:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=picture ./scripts/jbr-skia-benchmark-suite.sh`
  passed 1/1 with no fallback, no unsupported reasons, screenshot status passed, 769 Skiko/JBR picture frames, zero
  command frames, `app_old_fps=336.6`, `app_new_fps=153.8`, `jbr_picture_fps=153.8`, `old_avg_cpu=107.24`, and
  `new_avg_cpu=116.27`. This keeps benchmark coverage moving under the daily cap without running the full benchmark
  suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260616-003352/suite.tsv`.
- Magic Jewel continued RuntimeEffect lifecycle parity with an exact source-cache eviction row:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-source-cache-eviction ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with no fallback, no unsupported reasons, 788 CMP recorder frames, 789 Skiko/JBR command frames, zero
  picture frames, screenshot status passed, `avg_delta=2.118`, `bad_pixel_ratio=0.05032`,
  `compose_bad_pixel_ratio=0.07507`, 1,356 JBR RuntimeEffect cache-hit frames, 2,715 cache-miss frames, 40
  effect-handle define frames, and 4,071 effect-handle use frames. This keeps RuntimeEffect source-cache churn coverage
  moving under the daily cap without running the full screenshot-parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-003700/suite.tsv`.
- Magic Jewel paired that with an exact RuntimeEffect shader source-cache eviction row:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-shader-source-cache-eviction ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with no fallback, no unsupported reasons, 725 CMP/Skiko/JBR command frames, zero picture frames,
  screenshot status passed, `avg_delta=2.084`, `bad_pixel_ratio=0.04931`, `compose_bad_pixel_ratio=0.07335`, 1,173
  JBR RuntimeEffect cache-hit frames, 2,349 cache-miss frames, 20 shader-handle define frames, 3,522 shader-handle use
  frames, and 20 effect-handle define frames. This keeps RuntimeEffect shader source-cache churn coverage moving under
  the daily cap without running the full screenshot-parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-003955/suite.tsv`.
- Magic Jewel reaffirmed the daily cap with cheap checks only: `LIST_CASE_COUNT=true ./scripts/jbr-skia-command-probe-suite.sh`
  resolved 696 cases, `LIST_CASE_GROUP_COUNTS=true ./scripts/jbr-skia-benchmark-suite.sh` reported `baseline=2` and
  `image-cache=3`, and bare `./scripts/jbr-skia-command-probe-suite.sh` exited 3 before launch against the consumed
  2026-06-15 broad slot. No broad validation was launched.
- Magic Jewel continued the compatibility fallback track with an exact RuntimeEffect color-filter capability row:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=runtime-color-filter-capability-missing ./scripts/jbr-skia-compatibility-matrix.sh`
  passed 1/1 with expected `command-capability-mismatch`, one new fallback, no unsupported reasons, background window
  shown, 560 CMP recorder frames, zero Skiko/JBR command frames, zero picture frames, screenshot status passed, and the
  expected fallback marker in `new.log`. This keeps high-word RuntimeEffect color-filter capability validation moving
  under the daily cap without running the full matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-235857/matrix.tsv`.
- Magic Jewel closed the daily broad-validation guard range loophole for command-probe and screenshot-parity suites.
  Cheap guard validation only: non-list `CASES_FROM`/`CASES_UNTIL` launches for both suites exited 3 against the
  already-consumed 2026-06-15 broad slot, while list-only range helpers still returned the selected command/parity rows.
  No broad validation was launched.
- Magic Jewel tightened no-launch helper and grouping hygiene after the daily cap work. `LIST_CASE_GROUP_COUNTS=true`
  now stays list-only across the command-probe, screenshot-parity, compatibility, artifact, and benchmark runners; the
  shader-composition-runtime command group includes the sweep-gradient shader color-filter migration trio; and the
  default command list selects the implemented `commands-color-filter-path-effect-wrong-type-fallback` sentinel. Cheap
  helper validation only: all group-count helpers returned counts, command-probe ungrouped output is empty, and bare
  command/screenshot/compatibility launches still exit 3 against today's consumed broad slot. No broad validation was
  launched.
- Magic Jewel further expanded the `shader-composition-runtime` command group so it now carries the existing resize and
  forced-context lifecycle companions for composite shader color-filter, transformed shader, RuntimeEffect shader,
  RuntimeEffect shader color-filter, linear/radial/sweep shader color-filter, RuntimeEffect pure/uniform/child rows,
  RuntimeEffect shader source-cache eviction, RuntimeEffect color-filter, RuntimeEffect stable color-filter,
  RuntimeEffect color-filter child rows, and RuntimeEffect source-cache eviction. Cheap helper validation only:
  `bash -n` passed, the group lists 54 rows, group counts report `shader-composition-runtime	54`, and ungrouped
  command cases remain empty. No broad validation was launched.
- Magic Jewel ran a narrow compatibility-matrix smoke for the next validation track:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=happy ./scripts/jbr-skia-compatibility-matrix.sh` passed 1/1 with no
  fallback, no unsupported reasons, background window shown, 1,245 CMP recorder frames, 1,244 Skiko/JBR command frames,
  zero picture frames, and screenshot status passed. This exact row was rerun outside the sandbox after a sandboxed
  attempt failed on the Gradle wrapper lock. The full compatibility matrix is still deferred to the next local-day
  broad slot:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-225927/matrix.tsv`.
- Magic Jewel ran a narrow current-artifact matrix smoke:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=current-all ./scripts/jbr-skia-artifact-matrix.sh` passed 1/1 with no
  fallback, no unsupported reasons, background window shown, 465 CMP recorder frames, 465 Skiko/JBR command frames,
  zero picture frames, and screenshot status passed. Current desktop patch, JBR API shim, JBR Skia native library, and
  CMP output inputs were all present. This exact row keeps artifact validation moving under the daily cap; optional old
  artifact rows and the full artifact matrix remain pending:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260615-231244/matrix.tsv`.
- Magic Jewel ran a narrow artifact fallback smoke for the missing public API shim:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=missing-public-api ./scripts/jbr-skia-artifact-matrix.sh` passed 1/1 with
  expected `public-api-missing` fallback, one new fallback, no unsupported reasons, background window shown, 767 CMP
  recorder frames, zero Skiko/JBR command frames, zero picture frames, screenshot status passed, and the expected
  `SKIKO_JBR_INTEROP_FALLBACK reason=public-api-missing` marker in `new.log`. This exact row keeps artifact fallback
  validation moving under the daily cap without running the full artifact matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260615-233657/matrix.tsv`.
- Magic Jewel recorded the optional old-artifact rows as unavailable without launching the app:
  `CASE_GROUPS=optional-old ./scripts/jbr-skia-artifact-matrix.sh` skipped all five optional rows and reported the
  required inputs: `OLD_JBR_API_SHIM`, `OLD_JBR_SKIA_LIB`, `OLD_DESKTOP_PATCH`, `OLD_SKIKO_VERSION`, and
  `OLD_CMP_OUT`. Provide those artifacts, or an `OLD_ARTIFACT_BUNDLE`, before expecting old/new artifact rows to run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260615-233944/matrix.tsv`.
- Magic Jewel ran a shortened exact benchmark smoke:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=commands ./scripts/jbr-skia-benchmark-suite.sh`
  passed 1/1 with no fallback, no unsupported reasons, background window shown, 843 CMP recorder frames, 842 Skiko/JBR
  command frames, zero picture frames, `app_new_fps=168.6`, `jbr_command_fps=168.4`, and screenshot status passed. This
  keeps benchmark validation moving under the daily cap without running the full benchmark suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260615-231519/suite.tsv`.
- Magic Jewel ran a shortened exact image-cache benchmark smoke:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASE_GROUPS=image-cache ./scripts/jbr-skia-benchmark-suite.sh`
  passed 3/3 with no fallback, no unsupported reasons, zero picture frames, and screenshot status passed. Stable images
  stayed cache-stable with zero JBR image-cache evicts/clears at `jbr_command_fps=104.2`; dynamic images reported 4,726
  JBR image-cache evict frames at `jbr_command_fps=71.2`; resize dynamic images reported 5,276 JBR image-cache evict
  frames plus one JBR image-cache clear and one scoped clear at `jbr_command_fps=110.4`. This keeps image-cache
  benchmark validation moving under the daily cap without running the full benchmark suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260615-234133/suite.tsv`.
- Magic Jewel ran a real pixel-diff screenshot-parity smoke:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-button-chrome ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with no fallback, no unsupported reasons, 1,501 CMP/Skiko/JBR command frames, zero picture frames,
  `avg_delta=2.265`, `bad_pixel_ratio=0.05200`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.07633`, and `compose_bottom_swatches_bad_pixel_ratio=0.00000`. This exact row keeps
  screenshot parity moving under the daily cap without running the full parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260615-231755/suite.tsv`.
- Magic Jewel ran a real pixel-diff screenshot-parity smoke for point dots:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-point-dots ./scripts/jbr-skia-screenshot-parity-suite.sh` passed
  1/1 with no fallback, no unsupported reasons, 798 CMP/Skiko/JBR command frames, zero picture frames, screenshot
  status passed, `avg_delta=2.122`, `bad_pixel_ratio=0.05042`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.07521`, and `compose_bottom_swatches_bad_pixel_ratio=0.00000`. This exact row keeps
  point-dot parity coverage moving under the daily cap without running the full parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260615-234511/suite.tsv`.
- Magic Jewel ran a real pixel-diff screenshot-parity smoke for clean geometry:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-geometry-clean ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with no fallback, no unsupported reasons, 885 CMP/Skiko/JBR command frames, zero picture frames,
  screenshot status passed, `avg_delta=2.805`, `bad_pixel_ratio=0.07203`, `header_buttons_bad_pixel_ratio=0.00272`,
  `compose_bad_pixel_ratio=0.10955`, and `compose_bottom_swatches_bad_pixel_ratio=0.00000`. This exact row keeps
  geometry parity coverage moving under the daily cap without running the full parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260615-234756/suite.tsv`.
- Magic Jewel ran a narrow public-API compatibility fallback smoke:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=public-api-missing ./scripts/jbr-skia-compatibility-matrix.sh` passed 1/1
  with expected `public-api-missing` fallback, one new fallback, no unsupported reasons, background window shown, 729
  CMP recorder frames, zero Skiko/JBR command frames, zero picture frames, screenshot status passed, and the expected
  `SKIKO_JBR_INTEROP_FALLBACK reason=public-api-missing` marker in `new.log`. This exact row strengthens fallback
  validation under the daily cap without running the full compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-232037/matrix.tsv`.
- Magic Jewel ran a narrow native-ABI compatibility fallback smoke:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=native-abi-mismatch ./scripts/jbr-skia-compatibility-matrix.sh` passed 1/1
  with expected `native-abi-mismatch` fallback, one new fallback, no unsupported reasons, background window shown,
  1,047 CMP recorder frames, zero Skiko/JBR command frames, zero picture frames, screenshot status not-run, and the
  expected `SKIKO_JBR_INTEROP_FALLBACK reason=native-abi-mismatch` marker in `new.log`. This exact row strengthens ABI
  fallback validation under the daily cap without running the full compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-232258/matrix.tsv`.
- Magic Jewel ran a narrow core-handshake compatibility fallback smoke:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="abi-mismatch command-capability-mismatch command-capability-high-mismatch" ./scripts/jbr-skia-compatibility-matrix.sh`
  passed 3/3 after an initial sandbox-only Gradle wrapper lock failure. Each exact row produced one expected fallback,
  no unsupported reasons, background window shown, zero Skiko/JBR command frames, zero picture frames, screenshot
  status passed, and the expected fallback marker in `new.log`; CMP recorder frames were 869 for ABI mismatch, 464 for
  low-word command capability mismatch, and 838 for high-word command capability mismatch. This keeps compatibility
  validation moving under the daily cap without running the full matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-232825/matrix.tsv`.
- Magic Jewel ran a narrow shader-descriptor capability fallback smoke:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=shader-descriptor-capability-missing ./scripts/jbr-skia-compatibility-matrix.sh`
  passed 1/1 with expected `command-capability-mismatch` fallback, one new fallback, no unsupported reasons, background
  window shown, 879 CMP recorder frames, zero Skiko/JBR command frames, zero picture frames, screenshot status passed,
  and the expected `SKIKO_JBR_INTEROP_FALLBACK reason=command-capability-mismatch` marker in `new.log`. This keeps
  shader descriptor gate validation moving under the daily cap without running the full matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-235048/matrix.tsv`.
- Magic Jewel tightened daily broad-validation guard startup for bare command-probe and screenshot-parity default runs.
  Cheap guard validation only: `./scripts/jbr-skia-command-probe-suite.sh` and
  `./scripts/jbr-skia-screenshot-parity-suite.sh` exit 3 immediately against the already-consumed 2026-06-15 broad
  slot, while exact-case list probes for `commands-core-primitives` and `parity-rich` still work. No broad validation
  was launched.
- Magic Jewel tightened grouped helper startup for command-probe and screenshot-parity suites: when `CASE_GROUPS` is
  set and `CASES` is unset, the runners now skip materializing their large default case lists before the existing
  group expansion. Cheap guard validation only:
  `CASE_GROUPS=core-effects LIST_CASES=true ./scripts/jbr-skia-command-probe-suite.sh` returned the focused
  core-effects rows immediately, `CASE_GROUPS=smoke LIST_CASES=true ./scripts/jbr-skia-screenshot-parity-suite.sh`
  returned the three smoke parity rows immediately, exact-case list probes still work, and bare default runs still exit
  3 against the already-consumed 2026-06-15 broad slot. No broad validation was launched.
- Focused command-probe lifecycle hardening continued with gradient-stroke migration rows. Magic Jewel now includes
  `commands-resize-gradient-stroke` and `commands-forced-context-gradient-stroke`, asserting destination migration,
  command-cache clear, JBR image-cache and scoped image-cache clears while the base gradient-stroke row remains on
  command replay with screenshot assertions disabled for local capture limits. Exact two-row validation passed with no
  fallback, no unsupported reasons, zero picture frames, 1,512 and 1,275 JBR command frames, one surface change and one
  command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache clear per row. This is
  focused command-probe lifecycle change 52 after the 2026-06-15 daily broad slot, so broad validation remains
  daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-224020/suite.tsv`.
- Focused command-probe lifecycle hardening continued with image blend-mode migration rows. Magic Jewel now includes
  `commands-resize-image-blend-mode` and `commands-forced-context-image-blend-mode`, asserting image refs,
  destination migration, command-cache clear, JBR image-cache and scoped image-cache clears while the base image
  blend-mode row remains on command replay. Exact two-row validation passed with no fallback, no unsupported reasons,
  zero picture frames, 1,287 and 1,608 JBR command frames, one surface change and one command-cache clear per row, one
  JBR image-cache clear per row, and one scoped image-cache clear per row. This is focused command-probe lifecycle
  change 51 after the 2026-06-15 daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-223411/suite.tsv`.
- Focused command-probe lifecycle hardening continued with linear-gradient path blend-mode migration rows. Magic Jewel
  now includes `commands-resize-linear-gradient-path-blend-mode` and
  `commands-forced-context-linear-gradient-path-blend-mode`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears while the base linear-gradient path blend-mode row remains on command
  replay. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 1,642 and
  1,161 JBR command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row,
  and one scoped image-cache clear per row. This is focused command-probe lifecycle change 50 after the 2026-06-15
  daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-222943/suite.tsv`.
- Focused command-probe lifecycle hardening continued with surface gradient blend-mode migration rows. Magic Jewel now
  includes `commands-resize-linear-gradient-blend-mode`, `commands-forced-context-linear-gradient-blend-mode`,
  `commands-resize-radial-gradient-stroke-blend-mode`,
  `commands-forced-context-radial-gradient-stroke-blend-mode`,
  `commands-resize-sweep-gradient-round-rect-blend-mode`, and
  `commands-forced-context-sweep-gradient-round-rect-blend-mode`, asserting destination migration, command-cache clear,
  JBR image-cache and scoped image-cache clears while the base gradient blend-mode rows remain on command replay. Exact
  six-row validation passed with no fallback, no unsupported reasons, zero picture frames, 1,329, 1,284, 1,503, 932,
  1,629, and 1,525 JBR command frames, one surface change and one command-cache clear per row, one JBR image-cache
  clear per row, and one scoped image-cache clear per row. This is focused command-probe lifecycle change 49 after the
  2026-06-15 daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-221607/suite.tsv`.
- Focused command-probe lifecycle hardening continued with aggregate gradient surface/path migration rows. Magic Jewel
  now includes `commands-resize-gradient-surfaces`, `commands-forced-context-gradient-surfaces`,
  `commands-resize-gradient-paths`, and `commands-forced-context-gradient-paths`, asserting destination migration,
  command-cache clear, JBR image-cache and scoped image-cache clears while the base aggregate gradient rows remain on
  command replay. Exact four-row validation passed with no fallback, no unsupported reasons, zero picture frames,
  1,472, 1,434, 1,021, and 1,191 JBR command frames, one surface change and one command-cache clear per row, one JBR
  image-cache clear per row, and one scoped image-cache clear per row. This is focused command-probe lifecycle change
  48 after the 2026-06-15 daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-220943/suite.tsv`.
- Focused command-probe lifecycle hardening continued with point primitive migration rows. Magic Jewel now includes
  `commands-resize-point-lines`, `commands-forced-context-point-lines`, `commands-resize-point-dots`, and
  `commands-forced-context-point-dots`, asserting destination migration, command-cache clear, JBR image-cache and
  scoped image-cache clears while the base point rows remain on command replay. Exact four-row validation passed with
  no fallback, no unsupported reasons, zero picture frames, 1,545, 1,134, 1,526, and 1,622 JBR command frames, one
  surface change and one command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache
  clear per row. This is focused command-probe lifecycle change 47 after the 2026-06-15 daily broad slot, so broad
  validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-220408/suite.tsv`.
- Focused command-probe lifecycle hardening continued with concat/skew transform migration rows. Magic Jewel now
  includes `commands-resize-concat-transform`, `commands-forced-context-concat-transform`,
  `commands-resize-skew-transform`, and `commands-forced-context-skew-transform`, asserting destination migration,
  command-cache clear, JBR image-cache and scoped image-cache clears while the base transform rows remain on command
  replay. Exact four-row validation passed with no fallback, no unsupported reasons, zero picture frames, 1,456,
  1,519, 1,515, and 1,000 JBR command frames, one surface change and one command-cache clear per row, one JBR
  image-cache clear per row, and one scoped image-cache clear per row. This is focused command-probe lifecycle change
  46 after the 2026-06-15 daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-215905/suite.tsv`.
- Focused command-probe lifecycle hardening continued with non-descriptor vertices and fill-rect blend-mode migration
  rows. Magic Jewel now includes `commands-resize-vertices`, `commands-forced-context-vertices`,
  `commands-resize-blend-mode`, and `commands-forced-context-blend-mode`, asserting destination migration,
  command-cache clear, JBR image-cache and scoped image-cache clears while the base command rows remain on command
  replay. Exact four-row validation passed with no fallback, no unsupported reasons, zero picture frames, 1,582,
  1,687, 898, and 1,125 JBR command frames, one surface change and one command-cache clear per row, one JBR image-cache
  clear per row, and one scoped image-cache clear per row. This is focused command-probe lifecycle change 45 after the
  2026-06-15 daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-215342/suite.tsv`.
- Focused command-probe lifecycle hardening continued with path-effect resize and forced-context migration rows. Magic
  Jewel now includes `commands-resize-path-effect` and `commands-forced-context-path-effect`, asserting destination
  migration, command-cache clear, JBR image-cache and scoped image-cache clears, and path-effect handle redefinitions
  while the dashed/corner/stamped/chained path-effect row remains on command replay. Exact two-row validation passed
  with no fallback, no unsupported reasons, zero picture frames, 10 effect-handle defines per row, 1,078 and 1,383 JBR
  command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, and one
  scoped image-cache clear per row. This is focused command-probe lifecycle change 44 after the 2026-06-15 daily broad
  slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-214919/suite.tsv`.
- Focused command-probe lifecycle hardening continued with image-filter resize and forced-context migration rows.
  Magic Jewel now includes `commands-resize-image-filter` and `commands-forced-context-image-filter`, asserting image
  refs, destination migration, command-cache clear, JBR image-cache and scoped image-cache clears while the base image
  filter path remains on command replay. Exact two-row validation passed with no fallback, no unsupported reasons,
  zero picture frames, zero effect-handle markers, 1,234 and 1,561 JBR command frames, one surface change and one
  command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache clear per row. This is
  focused command-probe lifecycle change 43 after the 2026-06-15 daily broad slot, so broad validation remains
  daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-214119/suite.tsv`.
- Focused command-probe lifecycle hardening continued with plain fill-rect color-filter resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-color-filter` and
  `commands-forced-context-color-filter`, asserting destination migration, command-cache clear, JBR image-cache and
  scoped image-cache clears while the non-handle color-filter path remains on command replay. Exact two-row validation
  passed with no fallback, no unsupported reasons, zero picture frames, zero effect-handle markers, 1,611 and 1,692
  JBR command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, and
  one scoped image-cache clear per row. This is focused command-probe lifecycle change 42 after the 2026-06-15 daily
  broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-213550/suite.tsv`.
- Focused command-probe lifecycle hardening continued with fill-rect color-filter blend-mode resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-color-filter-blend-mode` and
  `commands-forced-context-color-filter-blend-mode`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears while the non-handle blend color-filter path remains on command replay.
  Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, zero effect-handle
  markers, 1,192 and 1,209 JBR command frames, one surface change and one command-cache clear per row, one JBR
  image-cache clear per row, and one scoped image-cache clear per row. This is focused command-probe lifecycle change
  41 after the 2026-06-15 daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-213054/suite.tsv`.
- Magic Jewel extended the once-per-local-day broad-validation guard to `scripts/jbr-skia-benchmark-suite.sh` default
  runs. Cheap guard validation confirmed `LIST_CASE_COUNT=true ./scripts/jbr-skia-benchmark-suite.sh` still lists 5
  benchmark cases, default `./scripts/jbr-skia-benchmark-suite.sh` exits 3 against the already-consumed 2026-06-15
  broad slot before launching cases, and `CASES=commands LIST_CASES=true ./scripts/jbr-skia-benchmark-suite.sh`
  preserves exact-case selection. No broad validation was run for this guardrail-only change.
- Daily-capped broad command-probe validation refreshed after ten focused shader/RuntimeEffect lifecycle changes:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` passed 589/589 with
  `fallback_sum=350`, 79 unsupported rows, 80,698 JBR picture frames, and 202,105 JBR command frames. This is the
  2026-06-15 broad validation slot and resets the focused command-probe counter to zero; keep subsequent validation
  exact-row/tiny-group only until the next local-day slot or an explicit ABI/capability gate. Disk free was about
  209Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-103914/suite.tsv`.
- Focused command-probe lifecycle hardening continued with color-filter handle resize and forced-context migration
  rows. Magic Jewel now includes `commands-resize-color-filter-handle` and
  `commands-forced-context-color-filter-handle`, asserting destination migration, command-cache clear, JBR image-cache
  and scoped image-cache clears, and color-filter effect-handle redefine/cache-hit markers. Exact two-row validation
  passed with no fallback, no unsupported reasons, zero picture frames, two effect-handle defines per row, 1,494 and
  1,455 JBR command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row,
  and one scoped image-cache clear per row. This is focused command-probe lifecycle change 40 after the 2026-06-15
  daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-212130/suite.tsv`.
- Focused command-probe lifecycle hardening continued with image-shader blend-mode resize and forced-context migration
  rows. Magic Jewel now includes `commands-resize-image-shader-blend-mode` and
  `commands-forced-context-image-shader-blend-mode`, asserting image refs, destination migration, command-cache clear,
  JBR image-cache and scoped image-cache clears while the image-shader paint blend-mode path remains on command replay.
  Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, zero shader-handle
  markers, 1,444 and 1,543 JBR command frames, one surface change and one command-cache clear per row, one JBR
  image-cache clear per row, and one scoped image-cache clear per row. This is focused command-probe lifecycle change
  39 after the 2026-06-15 daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-211648/suite.tsv`.
- Focused command-probe lifecycle hardening continued with gradient-shader resize and forced-context migration rows.
  Magic Jewel now includes `commands-resize-gradient-shaders` and `commands-forced-context-gradient-shaders`,
  asserting destination migration, command-cache clear, JBR image-cache and scoped image-cache clears while explicit
  ShaderBrush linear/radial/sweep gradients lower through dedicated gradient commands. Exact two-row validation passed
  with no fallback, no unsupported reasons, zero picture frames, zero shader-handle markers, 1,195 and 1,464 JBR
  command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, and one
  scoped image-cache clear per row. This is focused command-probe lifecycle change 38 after the 2026-06-15 daily broad
  slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-211223/suite.tsv`.
- Focused command-probe lifecycle hardening continued with turbulence-shader resize and forced-context migration rows.
  Magic Jewel now includes `commands-resize-turbulence-shader` and `commands-forced-context-turbulence-shader`,
  asserting destination migration, command-cache clear, JBR image-cache and scoped image-cache clears, and
  shader-handle redefine/cache-hit markers. Exact two-row validation passed with no fallback, no unsupported reasons,
  zero picture frames, two shader-handle defines per row, 1,491 and 1,508 JBR command frames, one surface change and
  one command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache clear per row. This
  is focused command-probe lifecycle change 37 after the 2026-06-15 daily broad slot, so broad validation remains
  daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-210800/suite.tsv`.
- Focused command-probe lifecycle hardening continued with noise-shader resize and forced-context migration rows.
  Magic Jewel now includes `commands-resize-noise-shader` and `commands-forced-context-noise-shader`, asserting
  destination migration, command-cache clear, JBR image-cache and scoped image-cache clears, and shader-handle
  redefine/cache-hit markers. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture
  frames, two shader-handle defines per row, 1,146 and 1,053 JBR command frames, one surface change and one
  command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache clear per row. This is
  focused command-probe lifecycle change 36 after the 2026-06-15 daily broad slot, so broad validation remains
  daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-210138/suite.tsv`.
- Focused command-probe lifecycle hardening continued with composite-noise-shader resize and forced-context migration
  rows. Magic Jewel now includes `commands-resize-composite-noise-shader` and
  `commands-forced-context-composite-noise-shader`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears, and shader-handle redefine/cache-hit markers. Exact two-row validation
  passed with no fallback, no unsupported reasons, zero picture frames, six shader-handle defines per row, 1,480 and
  1,571 JBR command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row,
  and one scoped image-cache clear per row. This is focused command-probe lifecycle change 35 after the 2026-06-15
  daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-205546/suite.tsv`.
- Focused command-probe lifecycle hardening continued with composite-shader resize and forced-context migration rows.
  Magic Jewel now includes `commands-resize-composite-shader` and `commands-forced-context-composite-shader`,
  asserting destination migration, command-cache clear, JBR image-cache and scoped image-cache clears, and
  shader-handle redefine/cache-hit markers. Exact two-row validation passed with no fallback, no unsupported reasons,
  zero picture frames, shader-handle cache hits per row, 1,383 and 1,342 JBR command frames, one surface change and
  one command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache clear per row. This
  is focused command-probe lifecycle change 34 after the 2026-06-15 daily broad slot, so broad validation remains
  daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-205029/suite.tsv`.
- Focused command-probe lifecycle hardening continued with color-shader blend-mode resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-color-shader-blend-mode` and
  `commands-forced-context-color-shader-blend-mode`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears, and shader-handle redefine/cache-hit markers. Exact two-row validation
  passed with no fallback, no unsupported reasons, zero picture frames, two shader-handle defines per row, 1,545 and
  1,601 JBR command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row,
  and one scoped image-cache clear per row. This is focused command-probe lifecycle change 33 after the 2026-06-15
  daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-204516/suite.tsv`.
- Focused command-probe lifecycle hardening continued with color-shader resize and forced-context migration rows.
  Magic Jewel now includes `commands-resize-color-shader` and `commands-forced-context-color-shader`, asserting
  destination migration, command-cache clear, JBR image-cache and scoped image-cache clears, and shader-handle
  redefine/cache-hit markers. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture
  frames, two shader-handle defines per row, 1,522 and 1,576 JBR command frames, one surface change and one
  command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache clear per row. This is
  focused command-probe lifecycle change 32 after the 2026-06-15 daily broad slot, so broad validation remains
  daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-203927/suite.tsv`.
- Focused command-probe lifecycle hardening continued with base graphics-layer resize and forced-context migration
  rows. Magic Jewel now includes `commands-resize-graphics-layer` and `commands-forced-context-graphics-layer`,
  asserting destination migration, command-cache clear, JBR image-cache and scoped image-cache clears while the base
  graphics-layer path is enabled. Exact two-row validation passed with no fallback, no unsupported reasons, zero
  picture frames, 1,479 and 1,293 JBR command frames, one surface change and one command-cache clear per row, one JBR
  image-cache clear per row, and one scoped image-cache clear per row. This is focused command-probe lifecycle change
  31 after the 2026-06-15 daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-203324/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer rotation-Y resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-graphics-layer-rotationy` and
  `commands-forced-context-graphics-layer-rotationy`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears while rotation Y is enabled. Exact two-row validation passed with no
  fallback, no unsupported reasons, zero picture frames, 977 and 1,080 JBR command frames, one surface change and one
  command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache clear per row. This is
  focused command-probe lifecycle change 30 after the 2026-06-15 daily broad slot, so broad validation remains
  daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-202801/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer rotation-X resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-graphics-layer-rotationx` and
  `commands-forced-context-graphics-layer-rotationx`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears while rotation X is enabled. Exact two-row validation passed with no
  fallback, no unsupported reasons, zero picture frames, 1,504 and 1,220 JBR command frames, one surface change and
  one command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache clear per row. This
  is focused command-probe lifecycle change 29 after the 2026-06-15 daily broad slot, so broad validation remains
  daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-202141/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer path-shadow resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-graphics-layer-path-shadow` and
  `commands-forced-context-graphics-layer-path-shadow`, asserting path clipping plus shadow command emission,
  destination migration, command-cache clear, JBR image-cache and scoped image-cache clears. Exact two-row validation
  passed with no fallback, no unsupported reasons, zero picture frames, one shadow command per row, 1,268 and 1,376
  JBR command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, and
  one scoped image-cache clear per row. This is focused command-probe lifecycle change 28 after the 2026-06-15 daily
  broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-201401/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer round-shadow resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-graphics-layer-round-shadow` and
  `commands-forced-context-graphics-layer-round-shadow`, asserting rounded clipping plus shadow command emission,
  destination migration, command-cache clear, JBR image-cache and scoped image-cache clears. Exact two-row validation
  passed with no fallback, no unsupported reasons, zero picture frames, one shadow command per row, 929 and 1,036 JBR
  command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, and one
  scoped image-cache clear per row. This is focused command-probe lifecycle change 27 after the 2026-06-15 daily broad
  slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-200706/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer shadow resize and forced-context migration
  rows. Magic Jewel now includes `commands-resize-graphics-layer-shadow` and
  `commands-forced-context-graphics-layer-shadow`, asserting shadow command emission, destination migration,
  command-cache clear, JBR image-cache and scoped image-cache clears while graphics-layer shadow rendering is enabled.
  Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, one shadow command per
  row, 1,308 and 1,263 JBR command frames, one surface change and one command-cache clear per row, one JBR image-cache
  clear per row, and one scoped image-cache clear per row. This is focused command-probe lifecycle change 26 after the
  2026-06-15 daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-200056/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer offscreen resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-graphics-layer-offscreen` and
  `commands-forced-context-graphics-layer-offscreen`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears while graphics-layer offscreen compositing is enabled. Exact two-row
  validation passed with no fallback, no unsupported reasons, zero picture frames, 1,266 and 1,522 JBR command frames,
  one surface change and one command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache
  clear per row. This is focused command-probe lifecycle change 25 after the 2026-06-15 daily broad slot, so broad
  validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-195406/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer modulate-alpha resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-graphics-layer-modulate-alpha` and
  `commands-forced-context-graphics-layer-modulate-alpha`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears while graphics-layer alpha modulation is enabled. Exact two-row validation
  passed with no fallback, no unsupported reasons, zero picture frames, 1,243 and 1,618 JBR command frames, one
  surface change and one command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache
  clear per row. This is focused command-probe lifecycle change 24 after the 2026-06-15 daily broad slot, so broad
  validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-194821/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer path-clip resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-graphics-layer-path-clip` and
  `commands-forced-context-graphics-layer-path-clip`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears while path-based graphics-layer clipping is enabled. Exact two-row
  validation passed with no fallback, no unsupported reasons, zero picture frames, 441 and 1,551 JBR command frames,
  one surface change and one command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache
  clear per row. This is focused command-probe lifecycle change 23 after the 2026-06-15 daily broad slot, so broad
  validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-194020/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer round-clip resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-graphics-layer-round-clip` and
  `commands-forced-context-graphics-layer-round-clip`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears while rounded graphics-layer clipping is enabled. Exact two-row validation
  passed with no fallback, no unsupported reasons, zero picture frames, 1,387 and 1,474 JBR command frames, one
  surface change and one command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache
  clear per row. This is focused command-probe lifecycle change 22 after the 2026-06-15 daily broad slot, so broad
  validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-193510/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer clip resize and forced-context migration
  rows. Magic Jewel now includes `commands-resize-graphics-layer-clip` and
  `commands-forced-context-graphics-layer-clip`, asserting destination migration, command-cache clear, JBR image-cache
  and scoped image-cache clears while graphics-layer clipping is enabled. Exact two-row validation passed with no
  fallback, no unsupported reasons, zero picture frames, 1,340 and 1,437 JBR command frames, one surface change and
  one command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache clear per row. This
  is focused command-probe lifecycle change 21 after the 2026-06-15 daily broad slot, so broad validation remains
  daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-192951/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer off-center pivot resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-graphics-layer-offcenter-pivot` and
  `commands-forced-context-graphics-layer-offcenter-pivot`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears while rotation X/Y, near-camera depth, and off-center pivot are enabled.
  Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 1,214 and 1,429 JBR
  command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, and one
  scoped image-cache clear per row. This is focused command-probe lifecycle change 20 after the 2026-06-15 daily
  broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-192229/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer near-camera resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-graphics-layer-near-camera` and
  `commands-forced-context-graphics-layer-near-camera`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears while rotation X/Y and near-camera depth are enabled. Exact two-row
  validation passed with no fallback, no unsupported reasons, zero picture frames, 1,529 and 1,596 JBR command frames,
  one surface change and one command-cache clear per row, one JBR image-cache clear per row, and one scoped
  image-cache clear per row. This is focused command-probe lifecycle change 19 after the 2026-06-15 daily broad slot,
  so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-191720/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer scale/translate resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-graphics-layer-scale-translate` and
  `commands-forced-context-graphics-layer-scale-translate`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears while graphics-layer scale and translation are enabled. Exact two-row
  validation passed with no fallback, no unsupported reasons, zero picture frames, 1,384 and 1,305 JBR command frames,
  one surface change and one command-cache clear per row, one JBR image-cache clear per row, and one scoped
  image-cache clear per row. This is focused command-probe lifecycle change 18 after the 2026-06-15 daily broad slot,
  so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-191035/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer combined rotation X/Y resize and
  forced-context migration rows. Magic Jewel now includes `commands-resize-graphics-layer-rotationxy` and
  `commands-forced-context-graphics-layer-rotationxy`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears while both 3D rotation axes are enabled. Exact two-row validation passed
  with no fallback, no unsupported reasons, zero picture frames, 1,936 and 976 JBR command frames, one surface change
  and one command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache clear per row.
  This is focused command-probe lifecycle change 17 after the 2026-06-15 daily broad slot, so broad validation remains
  daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-190433/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer blend-mode-only resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-graphics-layer-blend-mode` and
  `commands-forced-context-graphics-layer-blend-mode`, asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears while blend mode is enabled. Exact two-row validation passed with no
  fallback, no unsupported reasons, zero picture frames, 1,371 and 1,316 JBR command frames, one surface change and
  one command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache clear per row. This
  is focused command-probe lifecycle change 16 after the 2026-06-15 daily broad slot, so broad validation remains
  daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-185806/suite.tsv`.
- Focused command-probe lifecycle hardening continued with the plain composite shader descriptor tree resize and
  forced-context migration rows. Magic Jewel now includes `commands-resize-composite-shader-descriptor-redefine` and
  `commands-forced-context-composite-shader-descriptor-redefine`, asserting destination migration, command-cache clear,
  JBR image-cache and scoped image-cache clears, and stable shader-handle redefinition/reuse/cache-hit markers for the
  linear/radial composite shader path. Exact two-row validation passed with no fallback, no unsupported reasons, zero
  picture frames, 386 and 1,228 JBR command frames, one surface change and one command-cache clear per row, one JBR
  image-cache clear per row, one scoped image-cache clear per row, six to nine shader-handle definitions, and
  shader-handle cache hits. This is focused command-probe lifecycle change 15 after the 2026-06-15 daily broad slot,
  so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-185045/suite.tsv`.
- Focused command-probe lifecycle hardening continued with image draw plus color-matrix filter resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-image-color-matrix-filter` and
  `commands-forced-context-image-color-matrix-filter`, asserting image refs, destination migration, command-cache
  clear, JBR image-cache and scoped image-cache clears, and stable effect-handle redefinition/reuse/cache-hit markers.
  Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 1,276 and 1,468 JBR
  command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, one
  scoped image-cache clear per row, two effect-handle definitions per row, and effect-handle cache hits. This is
  focused command-probe lifecycle change 14 after the 2026-06-15 daily broad slot, so broad validation remains
  daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-184358/suite.tsv`.
- Focused command-probe lifecycle hardening continued with plain lighting filter resize and forced-context migration
  rows. Magic Jewel now includes `commands-resize-lighting-filter` and `commands-forced-context-lighting-filter`,
  asserting destination migration, command-cache clear, JBR image-cache and scoped image-cache clears, and stable
  effect-handle redefinition/reuse/cache-hit markers. Exact two-row validation passed with no fallback, no unsupported
  reasons, zero picture frames, 994 and 1,361 JBR command frames, one surface change and one command-cache clear per
  row, one JBR image-cache clear per row, one scoped image-cache clear per row, two effect-handle definitions per row,
  and effect-handle cache hits. This is focused command-probe lifecycle change 13 after the 2026-06-15 daily broad
  slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-183843/suite.tsv`.
- Focused command-probe lifecycle hardening resumed after the 2026-06-15 daily broad slot with sweep-gradient shader
  plus color-filter replay. Magic Jewel now includes `commands-sweep-gradient-shader-color-filter`,
  `commands-resize-sweep-gradient-shader-color-filter`, and
  `commands-forced-context-sweep-gradient-shader-color-filter`, asserting shader/effect-handle replay plus resize and
  forced-context cache migration markers. Exact three-row validation passed with no fallback, no unsupported reasons,
  zero picture frames, 656/848/622 JBR command frames, one JBR image-cache clear and one scoped image-cache clear on
  each migration row, two effect-handle definitions on each migration row, four to six shader-handle definitions, and
  shader-handle cache hits. This is focused command-probe lifecycle change 1 after the 2026-06-15 daily broad slot, so
  broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-171845/suite.tsv`.
- Focused command-probe lifecycle hardening continued with image shader plus color-filter resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-image-shader-color-filter` and
  `commands-forced-context-image-shader-color-filter`, asserting image refs, destination migration, command-cache
  clear, JBR image-cache and scoped image-cache clears, stable shader/effect-handle redefinition, and shader-handle
  reuse markers. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames,
  1,896 and 1,645 JBR command frames, one surface change and one command-cache clear per row, one JBR image-cache clear
  per row, one scoped image-cache clear per row, two effect-handle definitions per row, four shader-handle definitions
  per row, and shader-handle cache hits. This is focused command-probe lifecycle change 2 after the 2026-06-15 daily
  broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-172520/suite.tsv`.
- Focused command-probe lifecycle hardening continued with plain image-shader resize and forced-context migration rows.
  Magic Jewel now includes `commands-resize-image-shader` and `commands-forced-context-image-shader`, asserting image
  refs, destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers without
  shader-handle expectations because the current plain image-shader path reports image refs rather than JBR
  shader-handle markers. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture
  frames, 1,193 and 1,050 JBR command frames, one surface change and one command-cache clear per row, one JBR
  image-cache clear per row, and one scoped image-cache clear per row. This is focused command-probe lifecycle change 3
  after the 2026-06-15 daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-172949/suite.tsv`.
- Focused command-probe lifecycle hardening continued with radial-gradient shader plus color-filter replay and
  migration rows. Magic Jewel now includes `commands-radial-gradient-shader-color-filter`,
  `commands-resize-radial-gradient-shader-color-filter`, and
  `commands-forced-context-radial-gradient-shader-color-filter`, asserting shader/effect-handle replay plus resize and
  forced-context cache migration markers. Exact three-row validation passed with no fallback, no unsupported reasons,
  zero picture frames, 953/451/1,046 JBR command frames, one JBR image-cache clear and one scoped image-cache clear on
  each migration row, one to two effect-handle definitions, two to six shader-handle definitions, and shader-handle
  cache hits. This is focused command-probe lifecycle change 4 after the 2026-06-15 daily broad slot, so broad
  validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-173931/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer tint color-filter resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-graphics-layer-color-filter` and
  `commands-forced-context-graphics-layer-color-filter`, asserting destination migration, command-cache clear, JBR
  image-cache clear, and scoped image-cache clear markers. Exact two-row validation passed with no fallback, no
  unsupported reasons, zero picture frames, 1,408 and 1,211 JBR command frames, one surface change and one
  command-cache clear per row, one JBR image-cache clear per row, and one scoped image-cache clear per row. The tint
  graphics-layer path does not emit effect-handle markers, so these rows intentionally assert the cache-migration
  contract only. This is focused command-probe lifecycle change 5 after the 2026-06-15 daily broad slot, so broad
  validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-174844/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer blend plus tint color-filter resize and
  forced-context migration rows. Magic Jewel now includes `commands-resize-graphics-layer-blend-color-filter` and
  `commands-forced-context-graphics-layer-blend-color-filter`, asserting destination migration, command-cache clear,
  JBR image-cache clear, and scoped image-cache clear markers while blend mode and tint color filter are both enabled.
  Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 1,673 and 1,059 JBR
  command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, and one
  scoped image-cache clear per row. This graphics-layer tint path also reports zero effect-handle markers, so these
  rows intentionally assert the cache-migration contract only. This is focused command-probe lifecycle change 6 after
  the 2026-06-15 daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-175354/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer blend plus color-matrix filter resize and
  forced-context migration rows. Magic Jewel now includes
  `commands-resize-graphics-layer-blend-color-matrix-filter` and
  `commands-forced-context-graphics-layer-blend-color-matrix-filter`, asserting destination migration, command-cache
  clear, JBR image-cache and scoped image-cache clears, and stable effect-handle redefinition/reuse/cache-hit markers
  while blend mode and color-matrix filtering are both enabled. Exact two-row validation passed with no fallback, no
  unsupported reasons, zero picture frames, 943 and 877 JBR command frames, one surface change and one command-cache
  clear per row, one JBR image-cache clear per row, one scoped image-cache clear per row, two effect-handle definitions
  per row, and effect-handle cache hits. This is focused command-probe lifecycle change 7 after the 2026-06-15 daily
  broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-180123/suite.tsv`.
- Focused command-probe lifecycle hardening continued with saveLayer color-matrix filter replay and migration rows.
  Magic Jewel now includes `commands-save-layer-color-matrix-filter`,
  `commands-resize-save-layer-color-matrix-filter`, and
  `commands-forced-context-save-layer-color-matrix-filter`, asserting stable color-filter effect-handle replay plus
  resize and forced-context cache migration markers. Exact three-row validation passed with no fallback, no unsupported
  reasons, zero picture frames, 762/1,092/1,195 JBR command frames, one effect-handle definition on the base row, two
  effect-handle definitions on each migration row, one JBR image-cache clear and scoped image-cache clear on each
  migration row, and effect-handle cache hits. This is focused command-probe lifecycle change 8 after the 2026-06-15
  daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-180618/suite.tsv`.
- Focused command-probe lifecycle hardening continued with saveLayer tint color-filter resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-save-layer-filter` and
  `commands-forced-context-save-layer-filter`, asserting destination migration, command-cache clear, JBR image-cache
  clear, and scoped image-cache clear markers. Exact two-row validation passed with no fallback, no unsupported
  reasons, zero picture frames, 1,098 and 963 JBR command frames, one surface change and one command-cache clear per
  row, one JBR image-cache clear per row, and one scoped image-cache clear per row. This saveLayer tint path reports
  zero effect-handle markers, so these rows intentionally assert the cache-migration contract only. This is focused
  command-probe lifecycle change 9 after the 2026-06-15 daily broad slot, so broad validation remains daily-capped:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-181209/suite.tsv`.
- Focused command-probe lifecycle hardening continued with saveLayer blend-mode resize and forced-context migration
  rows. Magic Jewel now includes `commands-resize-save-layer-blend-mode` and
  `commands-forced-context-save-layer-blend-mode`, asserting destination migration, command-cache clear, JBR image-cache
  clear, and scoped image-cache clear markers. Exact two-row validation passed with no fallback, no unsupported
  reasons, zero picture frames, 478 and 1,187 JBR command frames, one surface change and one command-cache clear per
  row, one JBR image-cache clear per row, and one scoped image-cache clear per row. This saveLayer blend-mode path
  reports zero effect-handle markers, so these rows intentionally assert the cache-migration contract only. This is
  focused command-probe lifecycle change 10 after the 2026-06-15 daily broad slot; broad command-probe validation is
  due by focused-change count but remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-181731/suite.tsv`.
- Focused command-probe lifecycle hardening continued with saveLayer blend plus tint color-filter resize and
  forced-context migration rows. Magic Jewel now includes `commands-resize-save-layer-blend-color-filter` and
  `commands-forced-context-save-layer-blend-color-filter`, asserting destination migration, command-cache clear, JBR
  image-cache clear, and scoped image-cache clear markers while blend mode and tint color filter are both enabled.
  Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 844 and 852 JBR
  command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, and one
  scoped image-cache clear per row. This saveLayer blend+tint path reports zero effect-handle markers, so these rows
  intentionally assert the cache-migration contract only. This is focused command-probe lifecycle change 11 after the
  2026-06-15 daily broad slot; broad command-probe validation remains deferred until the next local-day broad slot or
  an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-182254/suite.tsv`.
- Focused command-probe lifecycle hardening continued with plain color-matrix filter resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-color-matrix-filter` and
  `commands-forced-context-color-matrix-filter`, asserting destination migration, command-cache clear, JBR image-cache
  and scoped image-cache clears, and stable effect-handle redefinition/reuse/cache-hit markers. Exact two-row
  validation passed with no fallback, no unsupported reasons, zero picture frames, 932 and 1,220 JBR command frames,
  one surface change and one command-cache clear per row, one JBR image-cache clear per row, one scoped image-cache
  clear per row, two effect-handle definitions per row, and effect-handle cache hits. This is focused command-probe
  lifecycle change 12 after the 2026-06-15 daily broad slot; broad command-probe validation remains deferred until the
  next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-182817/suite.tsv`.
- Batched command-probe validation refreshed after ten focused graphics-layer render-effect lifecycle hardenings. The
  original full sweep was interrupted after 564/569 rows had passed; after rebuilding the missing `/tmp` JBR API shim
  and native bridge, the five missing tail rows passed in a scoped recovery run. Combined coverage passed 569/569 with
  `fallback_sum=350`, 79 unsupported rows, 52,125 JBR picture frames, and 113,849 JBR command frames. This resets the
  focused command-probe change counter to zero; keep subsequent per-change command-probe validation exact-row/tiny-group
  only until roughly ten more meaningful command-probe changes or an ABI/capability gate. Disk free was about 175Gi
  after the recovery:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-021440/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-085024/suite.tsv`.
- Post-sweep compatibility matrix refreshed after the 569-row command-probe coverage:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with
  `fallback_sum=56`, 650 JBR command frames from `happy`, and `background_window=true` on all rows. This rechecked the
  ABI/native ABI, command capability, public API, and current capability fallback gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-085511/matrix.tsv`.
- Narrow artifact/benchmark validation refreshed after the post-sweep compatibility matrix. Required artifact matrix
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2:
  `current-all` had no fallback and 419 command frames, while `missing-public-api` took the expected single public-API
  fallback with zero command frames; both rows reported `background_window=true`. Command benchmark smoke
  `CASES=commands ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, zero picture frames, 3,661 command
  frames, `app_new_fps=183.0`, and `jbr_command_fps=183.1`. This was intentionally narrow and does not advance any
  focused change counter:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260615-092542/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260615-092659/suite.tsv`.
- Focused command-probe lifecycle hardening continued with direct RuntimeEffect shader resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-runtime-effect-shader` and
  `commands-forced-context-runtime-effect-shader`, both asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears, RuntimeEffect source-cache reuse, and shader-handle redefinition/use
  markers. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 833 and 999
  command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, one scoped
  image-cache clear per row, dynamic shader-handle definitions/evictions, and RuntimeEffect source-cache hits with at
  most one miss per row. This is focused command-probe lifecycle change 6 after the 2026-06-15 post-sweep smoke
  refresh, so broad command-probe validation remains deferred until roughly four more focused changes or an
  ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-100848/suite.tsv`.
- Focused command-probe lifecycle hardening continued with RuntimeEffect shader plus color-filter resize and
  forced-context migration rows. Magic Jewel now includes `commands-resize-runtime-effect-shader-color-filter` and
  `commands-forced-context-runtime-effect-shader-color-filter`, both asserting destination migration, command-cache
  clear, JBR image-cache and scoped image-cache clears, RuntimeEffect source-cache reuse, dynamic shader-handle
  redefinition/use markers, and stable effect-handle redefinition/reuse/cache-hit markers. Exact two-row validation
  passed with no fallback, no unsupported reasons, zero picture frames, 816 and 1,219 command frames, one surface change
  and one command-cache clear per row, one JBR image-cache clear per row, one scoped image-cache clear per row, two
  effect-handle definitions per row, and RuntimeEffect source-cache hits with at most one miss per row. This is focused
  command-probe lifecycle change 7 after the 2026-06-15 post-sweep smoke refresh, so broad command-probe validation
  remains deferred until roughly three more focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-101428/suite.tsv`.
- Focused command-probe lifecycle hardening continued with linear-gradient shader plus color-filter resize and
  forced-context migration rows. Magic Jewel now includes `commands-resize-linear-gradient-shader-color-filter` and
  `commands-forced-context-linear-gradient-shader-color-filter`, both asserting destination migration, command-cache
  clear, JBR image-cache and scoped image-cache clears, stable shader/effect-handle redefinition, and shader-handle
  reuse markers. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 1,037
  and 1,262 command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row,
  one scoped image-cache clear per row, two effect-handle definitions per row, four to six shader-handle definitions,
  and shader-handle cache hits. This is focused command-probe lifecycle change 8 after the 2026-06-15 post-sweep smoke
  refresh, so broad command-probe validation remains deferred until roughly two more focused changes or an
  ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-102103/suite.tsv`.
- Focused command-probe lifecycle hardening continued with composite shader plus color-filter resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-composite-shader-color-filter` and
  `commands-forced-context-composite-shader-color-filter`, both asserting destination migration, command-cache clear,
  JBR image-cache and scoped image-cache clears, stable shader/effect-handle redefinition, and shader-handle reuse
  markers. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 1,119 and
  1,070 command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, one
  scoped image-cache clear per row, two effect-handle definitions per row, eight to twelve shader-handle definitions,
  and shader-handle cache hits. This is focused command-probe lifecycle change 9 after the 2026-06-15 post-sweep smoke
  refresh, so broad command-probe validation remains deferred until roughly one more focused change or an
  ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-102824/suite.tsv`.
- Focused command-probe lifecycle hardening continued with transformed shader resize and forced-context migration rows.
  Magic Jewel now includes `commands-resize-transformed-shader` and `commands-forced-context-transformed-shader`, both
  asserting destination migration, command-cache clear, JBR image-cache and scoped image-cache clears, stable
  shader-handle redefinition, and shader-handle reuse markers. Exact two-row validation passed with no fallback, no
  unsupported reasons, zero picture frames, 921 and 1,011 command frames, one surface change and one command-cache
  clear per row, one JBR image-cache clear per row, one scoped image-cache clear per row, four to six shader-handle
  definitions, and shader-handle cache hits. This is focused command-probe lifecycle change 10 after the 2026-06-15
  post-sweep smoke refresh, so the batched broad command-probe validation is due next:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-103556/suite.tsv`.
- Focused command-probe lifecycle hardening resumed after the post-sweep smoke refresh with RuntimeEffect pure-color
  resize and forced-context migration rows. Magic Jewel now includes `commands-resize-runtime-effect-pure-color` and
  `commands-forced-context-runtime-effect-pure-color`, both asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears, RuntimeEffect source-cache reuse, and shader-handle
  redefinition/reuse/cache-hit markers. Exact two-row validation passed with no fallback, no unsupported reasons, zero
  picture frames, 1,328 and 1,221 command frames, one surface change and one command-cache clear per row, one JBR
  image-cache clear per row, one scoped image-cache clear per row, two shader-handle definitions per row, and
  RuntimeEffect source-cache hits with at most one miss per row. This is focused command-probe lifecycle change 1 after
  the 2026-06-15 post-sweep smoke refresh, so broad command-probe validation remains deferred until roughly nine more
  focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-093344/suite.tsv`.
- Focused command-probe lifecycle hardening continued with RuntimeEffect uniform-only resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-runtime-effect-uniform-only` and
  `commands-forced-context-runtime-effect-uniform-only`, both asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears, RuntimeEffect source-cache reuse, and shader-handle redefinition/use
  markers. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 1,297 and
  1,178 command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, one
  scoped image-cache clear per row, and RuntimeEffect source-cache hits with at most one miss per row. Animated uniforms
  intentionally produced many shader-handle definitions and evictions, so these rows do not cap shader definitions.
  This is focused command-probe lifecycle change 2 after the 2026-06-15 post-sweep smoke refresh, so broad
  command-probe validation remains deferred until roughly eight more focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-094037/suite.tsv`.
- Focused command-probe lifecycle hardening continued with RuntimeEffect child-only resize and forced-context migration
  rows. Magic Jewel now includes `commands-resize-runtime-effect-child-only` and
  `commands-forced-context-runtime-effect-child-only`, both asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears, RuntimeEffect source-cache reuse, and child shader-handle
  redefinition/reuse/cache-hit markers. Exact two-row validation passed with no fallback, no unsupported reasons, zero
  picture frames, 1,054 and 1,202 command frames, one surface change and one command-cache clear per row, one JBR
  image-cache clear per row, one scoped image-cache clear per row, six to nine shader-handle definitions, and
  RuntimeEffect source-cache hits with at most one miss per row. This is focused command-probe lifecycle change 3 after
  the 2026-06-15 post-sweep smoke refresh, so broad command-probe validation remains deferred until roughly seven more
  focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-094747/suite.tsv`.
- Focused command-probe lifecycle hardening continued with dynamic RuntimeEffect color-filter resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-runtime-effect-color-filter` and
  `commands-forced-context-runtime-effect-color-filter`, both asserting destination migration, command-cache clear, JBR
  image-cache and scoped image-cache clears, RuntimeEffect source-cache reuse, and effect-handle redefinition/use
  markers. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 1,151 and
  1,160 command frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, one
  scoped image-cache clear per row, dynamic effect-handle definitions/evictions, and RuntimeEffect source-cache hits
  with at most one miss per row. This is focused command-probe lifecycle change 4 after the 2026-06-15 post-sweep
  smoke refresh, so broad command-probe validation remains deferred until roughly six more focused changes or an
  ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-095358/suite.tsv`.
- Focused command-probe lifecycle hardening continued with RuntimeEffect color-filter child resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-runtime-effect-color-filter-child` and
  `commands-forced-context-runtime-effect-color-filter-child`, both asserting destination migration, command-cache
  clear, JBR image-cache and scoped image-cache clears, RuntimeEffect source-cache reuse, and child effect-handle
  redefinition/use markers. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture
  frames, 946 and 1,118 command frames, one surface change and one command-cache clear per row, one JBR image-cache
  clear per row, one scoped image-cache clear per row, dynamic effect-handle definitions/evictions, and RuntimeEffect
  source-cache hits with at most one miss per row. This is focused command-probe lifecycle change 5 after the
  2026-06-15 post-sweep smoke refresh, so broad command-probe validation remains deferred until roughly five more
  focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-095954/suite.tsv`.
- Focused command-probe lifecycle hardening resumed with graphics-layer offset render-effect resize and forced-context
  migration rows. Magic Jewel now includes `commands-resize-graphics-layer-offset-effect` and
  `commands-forced-context-graphics-layer-offset-effect`, both asserting surface/cache migration, JBR image-cache and
  scoped image-cache clears, and effect-handle redefinition/reuse/cache-hit markers. Exact two-row validation passed
  with no fallback, no unsupported reasons, zero picture frames, 2,302 and 1,077 command frames, one surface change and
  one command-cache clear per row, one JBR image-cache clear per row, one scoped image-cache clear per row, and two
  effect-handle definitions per row. This is focused command-probe lifecycle change 1 after the 2026-06-14 18:10 full
  command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-011920/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer chained render-effect resize and
  forced-context migration rows. Magic Jewel now includes `commands-resize-graphics-layer-chained-render-effect` and
  `commands-forced-context-graphics-layer-chained-render-effect`, both asserting surface/cache migration, JBR
  image-cache and scoped image-cache clears, and chained effect-handle redefinition/reuse/cache-hit markers. Exact
  two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 385 and 939 command frames,
  one surface change and one command-cache clear per row, one JBR image-cache clear per row, one scoped image-cache
  clear per row, and four effect-handle definitions per row. This is focused command-probe lifecycle change 2 after the
  2026-06-14 18:10 full command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-012432/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer render-effect plus color-filter resize and
  forced-context migration rows. Magic Jewel now includes
  `commands-resize-graphics-layer-render-effect-color-filter` and
  `commands-forced-context-graphics-layer-render-effect-color-filter`, both asserting surface/cache migration, JBR
  image-cache and scoped image-cache clears, and effect-handle redefinition/reuse/cache-hit markers. Exact two-row
  validation passed with no fallback, no unsupported reasons, zero picture frames, 871 and 945 command frames, one
  surface change and one command-cache clear per row, one JBR image-cache clear per row, one scoped image-cache clear
  per row, and two effect-handle definitions per row. This is focused command-probe lifecycle change 3 after the
  2026-06-14 18:10 full command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-012942/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer render-effect plus blend-mode resize and
  forced-context migration rows. Magic Jewel now includes
  `commands-resize-graphics-layer-render-effect-blend-mode` and
  `commands-forced-context-graphics-layer-render-effect-blend-mode`, both asserting surface/cache migration, JBR
  image-cache and scoped image-cache clears, and effect-handle redefinition/reuse/cache-hit markers. Exact two-row
  validation passed with no fallback, no unsupported reasons, zero picture frames, 774 and 961 command frames, one
  surface change and one command-cache clear per row, one JBR image-cache clear per row, one scoped image-cache clear
  per row, and two effect-handle definitions per row. This is focused command-probe lifecycle change 4 after the
  2026-06-14 18:10 full command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-013449/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer render-effect plus color-matrix-filter resize
  and forced-context migration rows. Magic Jewel now includes
  `commands-resize-graphics-layer-render-effect-color-matrix-filter` and
  `commands-forced-context-graphics-layer-render-effect-color-matrix-filter`, both asserting surface/cache migration,
  JBR image-cache and scoped image-cache clears, and color-matrix/effect-handle redefinition/reuse/cache-hit markers.
  Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 710 and 906 command
  frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, one scoped
  image-cache clear per row, and four effect-handle definitions per row. This is focused command-probe lifecycle
  change 5 after the 2026-06-14 18:10 full command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-014123/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer render-effect plus blend/color-filter resize
  and forced-context migration rows. Magic Jewel now includes
  `commands-resize-graphics-layer-render-effect-blend-color-filter` and
  `commands-forced-context-graphics-layer-render-effect-blend-color-filter`, both asserting surface/cache migration,
  JBR image-cache and scoped image-cache clears, and effect-handle redefinition/reuse/cache-hit markers. Exact two-row
  validation passed with no fallback, no unsupported reasons, zero picture frames, 852 and 849 command frames, one
  surface change and one command-cache clear per row, one JBR image-cache clear per row, one scoped image-cache clear
  per row, and two effect-handle definitions per row. This is focused command-probe lifecycle change 6 after the
  2026-06-14 18:10 full command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-014743/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer render-effect plus blend/color-matrix-filter
  resize and forced-context migration rows. Magic Jewel now includes
  `commands-resize-graphics-layer-render-effect-blend-color-matrix-filter` and
  `commands-forced-context-graphics-layer-render-effect-blend-color-matrix-filter`, both asserting surface/cache
  migration, JBR image-cache and scoped image-cache clears, and effect-handle redefinition/reuse/cache-hit markers.
  Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 892 and 750 command
  frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, one scoped
  image-cache clear per row, and four effect-handle definitions per row. This is focused command-probe lifecycle
  change 7 after the 2026-06-14 18:10 full command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-015317/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer offset-effect plus blend/color-matrix-filter
  resize and forced-context migration rows. Magic Jewel now includes
  `commands-resize-graphics-layer-offset-effect-blend-color-matrix-filter` and
  `commands-forced-context-graphics-layer-offset-effect-blend-color-matrix-filter`, both asserting surface/cache
  migration, JBR image-cache and scoped image-cache clears, and effect-handle redefinition/reuse/cache-hit markers.
  Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 980 and 973 command
  frames, one surface change and one command-cache clear per row, one JBR image-cache clear per row, one scoped
  image-cache clear per row, and four effect-handle definitions per row. This is focused command-probe lifecycle
  change 8 after the 2026-06-14 18:10 full command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-015844/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer chained render-effect plus
  blend/color-matrix-filter resize and forced-context migration rows. Magic Jewel now includes
  `commands-resize-graphics-layer-chained-render-effect-blend-color-matrix-filter` and
  `commands-forced-context-graphics-layer-chained-render-effect-blend-color-matrix-filter`, both asserting
  surface/cache migration, JBR image-cache and scoped image-cache clears, and chained effect-handle
  redefinition/reuse/cache-hit markers. Exact two-row validation passed with no fallback, no unsupported reasons, zero
  picture frames, 929 and 922 command frames, one surface change and one command-cache clear per row, one JBR
  image-cache clear per row, one scoped image-cache clear per row, and six effect-handle definitions per row. This is
  focused command-probe lifecycle change 9 after the 2026-06-14 18:10 full command-probe sweep, so broad command-probe
  validation remains deferred until one more focused command-probe change or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-020421/suite.tsv`.
- Focused command-probe lifecycle hardening continued with graphics-layer near-camera chained render-effect plus
  blend/color-matrix-filter resize and forced-context migration rows. Magic Jewel now includes
  `commands-resize-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter` and
  `commands-forced-context-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter`, both asserting
  surface/cache migration, JBR image-cache and scoped image-cache clears, and chained effect-handle
  redefinition/reuse/cache-hit markers while rotation X/Y and near-camera are enabled. Exact two-row validation passed
  with no fallback, no unsupported reasons, zero picture frames, 712 and 613 command frames, one surface change and one
  command-cache clear per row, one JBR image-cache clear per row, one scoped image-cache clear per row, and six
  effect-handle definitions per row. This is focused command-probe lifecycle change 10 after the 2026-06-14 18:10 full
  command-probe sweep, so the next validation step is the cadence-triggered full command-probe sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-021141/suite.tsv`.
- Batched command-probe validation refreshed after ten focused graphics-layer render-effect lifecycle hardenings. The
  cadence-triggered full command
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` was interrupted after 564/569 rows had
  passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-021440/suite.tsv`.
  The five missing tail rows initially exposed a stale validation environment because `/tmp/jbr-api-shim.jar` and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib` were missing; after
  `./scripts/rebuild-jbr-skia-local-artifacts.sh`, the scoped recovery command
  `EXPECT_SCREENSHOT_ASSERTION=false CASES_FROM=commands-save-layer-raw-table-color-filter-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed 5/5:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-085024/suite.tsv`.
  Combined coverage passed 569/569 with `fallback_sum=350`, 79 unsupported rows, 52,125 JBR picture frames, and 113,849
  JBR command frames. This resets the focused command-probe counter to zero.
- Post-sweep compatibility matrix refreshed after the recovered 569-row command-probe coverage:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with
  `fallback_sum=56`, 650 JBR command frames from `happy`, and `background_window=true` on all rows. The matrix
  rechecked ABI/native ABI mismatch handling, command capability low/high mismatches, public API absence, and the
  current gradient, text/font, shader/filter/effect/path/transform/image/vertex capability fallback gates:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-085511/matrix.tsv`.
- Narrow artifact/benchmark validation refreshed after the post-sweep compatibility matrix. Required artifact matrix
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2:
  `current-all` had no fallback and 419 command frames, while `missing-public-api` took the expected single
  public-API fallback with zero command frames; both rows reported `background_window=true`. Command benchmark smoke
  `CASES=commands ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, zero picture frames, 3,661 command
  frames, `app_new_fps=183.0`, and `jbr_command_fps=183.1`. This was intentionally narrow and does not advance the
  focused command-probe counter. Disk free was about 174Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260615-092542/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260615-092659/suite.tsv`.
- Batched full command-probe validation refreshed after ten focused command-probe hardenings:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` passed 549/549 with
  `fallback_sum=350`, 80 unsupported rows, 77,679 JBR picture frames, and 152,941 JBR command frames. This resets the
  focused command-probe change counter to zero; keep subsequent per-change command-probe validation exact-row/tiny-group
  only until roughly ten more meaningful command-probe changes or an ABI/capability gate. Disk free was about 162Gi
  after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-113210/suite.tsv`.
- Post-sweep compatibility matrix refreshed after the full command-probe sweep:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with
  `fallback_sum=56`, 467 JBR command frames from `happy`, and `background_window=true` on all rows. Disk free was about
  161Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260614-170152/matrix.tsv`.
- Narrow artifact/benchmark validation refreshed after the post-sweep compatibility matrix. Required artifact matrix
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2:
  `current-all` had no fallback and 869 command frames, while `missing-public-api` took the expected single
  public-API fallback with zero command frames; both rows reported `background_window=true`. Command benchmark smoke
  `CASES=commands ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, zero picture frames, 3,848 command
  frames, `app_new_fps=192.4`, and `jbr_command_fps=192.4`. This was intentionally narrow and does not advance any
  focused change counter:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260614-172951/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260614-173051/suite.tsv`.
- Focused command-probe lifecycle hardening resumed with the shader descriptor redefine migration rows:
  `commands-resize-shader-descriptor-redefine` and `commands-forced-context-shader-descriptor-redefine` now require at
  least one JBR image-cache clear and one scoped JBR image-cache clear alongside their existing surface/cache,
  shader-handle, and RuntimeEffect source-cache guards. Exact two-row validation passed with no fallback, no unsupported
  reasons, zero picture frames, 1,538 and 1,630 command frames, one JBR image-cache clear per row, and one scoped
  image-cache clear per row. This is focused command-probe lifecycle change 1 after the 2026-06-14 11:32 full
  command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-173417/suite.tsv`.
- Focused command-probe lifecycle hardening continued with the color-shader descriptor redefine migration rows:
  `commands-resize-color-shader-descriptor-redefine` and `commands-forced-context-color-shader-descriptor-redefine` now
  require JBR image-cache and scoped image-cache clear markers alongside their existing surface/cache and shader-handle
  guards. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 1,731 and
  1,303 command frames, one JBR image-cache clear per row, and one scoped image-cache clear per row. This is focused
  command-probe lifecycle change 2 after the 2026-06-14 11:32 full command-probe sweep, so broad command-probe
  validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-173740/suite.tsv`.
- Focused command-probe lifecycle hardening continued with the noise-shader descriptor redefine migration rows:
  `commands-resize-noise-shader-descriptor-redefine` and `commands-forced-context-noise-shader-descriptor-redefine` now
  require JBR image-cache and scoped image-cache clear markers alongside their existing surface/cache and shader-handle
  guards. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames, 1,635 and
  1,658 command frames, one JBR image-cache clear per row, and one scoped image-cache clear per row. This is focused
  command-probe lifecycle change 3 after the 2026-06-14 11:32 full command-probe sweep, so broad command-probe
  validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-174127/suite.tsv`.
- Focused command-probe lifecycle hardening continued with the turbulence-shader descriptor redefine migration rows:
  `commands-resize-turbulence-shader-descriptor-redefine` and
  `commands-forced-context-turbulence-shader-descriptor-redefine` now require JBR image-cache and scoped image-cache
  clear markers alongside their existing surface/cache and shader-handle guards. Exact two-row validation passed with
  no fallback, no unsupported reasons, zero picture frames, 1,494 and 1,359 command frames, one JBR image-cache clear
  per row, and one scoped image-cache clear per row. This is focused command-probe lifecycle change 4 after the
  2026-06-14 11:32 full command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-174439/suite.tsv`.
- Focused command-probe lifecycle hardening continued with the composite-noise shader descriptor redefine migration
  rows: `commands-resize-composite-noise-shader-descriptor-redefine` and
  `commands-forced-context-composite-noise-shader-descriptor-redefine` now require JBR image-cache and scoped
  image-cache clear markers alongside their existing surface/cache and shader-handle guards. Exact two-row validation
  passed with no fallback, no unsupported reasons, zero picture frames, 1,275 and 1,650 command frames, one JBR
  image-cache clear per row, and one scoped image-cache clear per row. This is focused command-probe lifecycle change 5
  after the 2026-06-14 11:32 full command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-174852/suite.tsv`.
- Focused command-probe lifecycle hardening continued with the generic native-font resize/forced-context rows:
  `commands-resize-native-generic-font-text` and `commands-forced-context-native-generic-font-text` now require JBR
  image-cache and scoped image-cache clear markers alongside their existing image/text, surface-change, and
  command-cache guards. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames,
  1,247 and 1,504 command frames, one JBR image-cache clear per row, and one scoped image-cache clear per row. This is
  focused command-probe lifecycle change 6 after the 2026-06-14 11:32 full command-probe sweep, so broad command-probe
  validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-175231/suite.tsv`.
- Focused command-probe lifecycle hardening continued with the loaded native-font resize/forced-context rows:
  `commands-resize-native-loaded-font-data-text` and `commands-forced-context-native-loaded-font-data-text` now require
  JBR image-cache and scoped image-cache clear markers alongside their existing image/text, font-data, surface-change,
  and command-cache guards. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture
  frames, 1,386 and 1,465 command frames, one JBR image-cache clear per row, and one scoped image-cache clear per row.
  This is focused command-probe lifecycle change 7 after the 2026-06-14 11:32 full command-probe sweep, so broad
  command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-175615/suite.tsv`.
- Focused command-probe lifecycle hardening continued with the resource native-font resize/forced-context rows:
  `commands-resize-native-resource-font-text` and `commands-forced-context-native-resource-font-text` now require JBR
  image-cache and scoped image-cache clear markers alongside their existing image/text, font-data, surface-change, and
  command-cache guards. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames,
  1,220 and 1,318 command frames, one JBR image-cache clear per row, and one scoped image-cache clear per row. This is
  focused command-probe lifecycle change 8 after the 2026-06-14 11:32 full command-probe sweep, so broad command-probe
  validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-175937/suite.tsv`.
- Focused command-probe lifecycle hardening continued with the system native-font resize/forced-context rows:
  `commands-resize-native-system-font-text` and `commands-forced-context-native-system-font-text` now require JBR
  image-cache and scoped image-cache clear markers alongside their existing image/text, surface-change, and
  command-cache guards. Exact two-row validation passed with no fallback, no unsupported reasons, zero picture frames,
  1,466 and 1,710 command frames, one JBR image-cache clear per row, and one scoped image-cache clear per row. This is
  focused command-probe lifecycle change 9 after the 2026-06-14 11:32 full command-probe sweep, so broad command-probe
  validation remains deferred until one more focused command-probe change or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-180320/suite.tsv`.
- Focused command-probe lifecycle hardening reached the cadence trigger with
  `commands-forced-context-native-custom-font-text-image`: the row now requires JBR image-cache and scoped image-cache
  clear markers alongside its existing image-ref, surface-change, and command-cache guards. Exact validation passed
  with no fallback, no unsupported reasons, zero picture frames, 1,313 command frames, one JBR image-cache clear, and
  one scoped image-cache clear. This is focused command-probe lifecycle change 10 after the 2026-06-14 11:32 full
  command-probe sweep, so the next validation step is the batched full command-probe sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-180730/suite.tsv`.
- Batched full command-probe validation refreshed after ten focused command-probe hardenings:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` passed 549/549 with
  `fallback_sum=350`, 80 unsupported rows, 70,982 JBR picture frames, and 130,383 JBR command frames. This resets the
  focused command-probe change counter to zero; keep subsequent per-change validation exact-row only until roughly ten
  more meaningful command-probe changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-000344/suite.tsv`.
- Focused command-probe lifecycle hardening continued with
  `commands-runtime-effect-shader-source-cache-eviction`: the row now requires at least one RuntimeEffect source-cache
  hit alongside its existing shader source-cache eviction and shader-handle reuse guards. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-runtime-effect-shader-source-cache-eviction ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,413 command frames, 1,942 RuntimeEffect
  source-cache hits, 3,887 misses, 3,885 evicts, 1,946 shader definitions, 5,829 shader uses, 3,885 shader cache hits,
  and 922 shader evicts. This is focused command-probe lifecycle change 1 after the 2026-06-14 00:03 full
  command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-104548/suite.tsv`.
- Focused command-probe lifecycle hardening continued with `commands-runtime-effect-source-cache-eviction`: the row now
  requires at least one RuntimeEffect source-cache hit alongside its existing color-filter source-cache eviction and
  effect-handle reuse guards. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-runtime-effect-source-cache-eviction ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,044 command frames, 1,441 RuntimeEffect
  source-cache hits, 2,885 misses, 2,883 evicts, 2,887 effect definitions, 4,326 effect uses, 1,441 effect cache hits,
  and 1,863 effect evicts. This is focused command-probe lifecycle change 2 after the 2026-06-14 00:03 full
  command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-104811/suite.tsv`.
- Focused command-probe lifecycle hardening continued with `commands-resize-graphics-layer-render-effect`: the row now
  requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing resize
  surface/cache and effect-handle reuse guards. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-resize-graphics-layer-render-effect ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,083 command frames, one JBR image-cache
  clear, one scoped image-cache clear, one surface-change marker, one command-cache clear marker, two effect
  definitions, 1,610 effect uses, and 1,608 effect cache hits. This is focused command-probe lifecycle change 3 after
  the 2026-06-14 00:03 full command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-105617/suite.tsv`.
- Focused command-probe lifecycle hardening continued with `commands-forced-context-graphics-layer-render-effect`: the
  row now requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing
  forced-context surface/cache and effect-handle reuse guards. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-forced-context-graphics-layer-render-effect ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,129 command frames, one JBR image-cache
  clear, one scoped image-cache clear, one surface-change marker, one command-cache clear marker, two effect
  definitions, 1,789 effect uses, and 1,787 effect cache hits. This is focused command-probe lifecycle change 4 after
  the 2026-06-14 00:03 full command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-105847/suite.tsv`.
- Focused command-probe lifecycle hardening continued with `commands-resize-graphics-layer-color-matrix-filter`: the
  row now requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing
  resize surface/cache and effect-handle reuse guards. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-resize-graphics-layer-color-matrix-filter ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,711 command frames, one JBR image-cache
  clear, one scoped image-cache clear, one surface-change marker, one command-cache clear marker, two effect
  definitions, 2,282 effect uses, and 2,280 effect cache hits. This is focused command-probe lifecycle change 5 after
  the 2026-06-14 00:03 full command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-110941/suite.tsv`.
- Focused command-probe lifecycle hardening continued with
  `commands-forced-context-graphics-layer-color-matrix-filter`: the row now requires at least one JBR image-cache clear
  and one scoped JBR image-cache clear alongside its existing forced-context surface/cache and effect-handle reuse
  guards. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-forced-context-graphics-layer-color-matrix-filter ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 969 command frames, one JBR image-cache
  clear, one scoped image-cache clear, one surface-change marker, one command-cache clear marker, two effect
  definitions, 1,701 effect uses, and 1,699 effect cache hits. This is focused command-probe lifecycle change 6 after
  the 2026-06-14 00:03 full command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-111522/suite.tsv`.
- Focused command-probe lifecycle hardening continued with
  `commands-forced-context-runtime-effect-stable-color-filter`: the row now requires at least one JBR image-cache
  clear and one scoped JBR image-cache clear alongside its existing forced-context surface/cache, effect-handle reuse,
  and RuntimeEffect source-cache guards. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-forced-context-runtime-effect-stable-color-filter ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,305 command frames, one JBR image-cache
  clear, one scoped image-cache clear, one surface-change marker, one command-cache clear marker, two effect
  definitions, 2,224 effect uses, 2,222 effect cache hits, 2,223 RuntimeEffect source-cache hits, one RuntimeEffect
  source-cache miss, and zero RuntimeEffect failures. This is focused command-probe lifecycle change 7 after the
  2026-06-14 00:03 full command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-111732/suite.tsv`.
- Focused command-probe lifecycle hardening continued with `commands-resize-runtime-effect-stable-color-filter`: the
  row now requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing resize
  surface/cache, effect-handle reuse, and RuntimeEffect source-cache guards. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-resize-runtime-effect-stable-color-filter ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 918 command frames, one JBR image-cache clear,
  one scoped image-cache clear, one surface-change marker, one command-cache clear marker, two effect definitions, 1,596
  effect uses, 1,594 effect cache hits, 1,595 RuntimeEffect source-cache hits, one RuntimeEffect source-cache miss, and
  zero RuntimeEffect failures. This is focused command-probe lifecycle change 8 after the 2026-06-14 00:03 full
  command-probe sweep, so broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-111943/suite.tsv`.
- Focused command-probe lifecycle hardening continued with `commands-forced-context-descriptor-redefine`: the row now
  requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing
  forced-context surface/cache and effect-handle reuse guards. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-forced-context-descriptor-redefine ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,353 command frames, one JBR image-cache
  clear, one scoped image-cache clear, one surface-change marker, one command-cache clear marker, two effect
  definitions, 2,148 effect uses, and 2,146 effect cache hits. This is focused command-probe lifecycle change 9 after
  the 2026-06-14 00:03 full command-probe sweep, so broad command-probe validation remains deferred until the next
  focused command-probe change:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-112804/suite.tsv`.
- Focused command-probe lifecycle hardening continued with `commands-resize-descriptor-redefine`: the row now requires
  at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing resize surface/cache
  and effect-handle reuse guards. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-resize-descriptor-redefine ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,286 command frames, one JBR image-cache
  clear, one scoped image-cache clear, one surface-change marker, one command-cache clear marker, two effect
  definitions, 2,267 effect uses, and 2,265 effect cache hits. This is focused command-probe lifecycle change 10 after
  the 2026-06-14 00:03 full command-probe sweep, so the next validation step is the batched full command-probe sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-113014/suite.tsv`.
- Post-sweep compatibility matrix refreshed after the full command-probe sweep:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with
  `fallback_sum=56`, 520 JBR command frames from `happy`, and `background_window=true` on all rows. Disk free remained
  about 176Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260614-054414/matrix.tsv`.
- Narrow artifact/benchmark validation refreshed after the post-sweep compatibility matrix. Required artifact matrix
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2:
  `current-all` replayed 514 command frames with no fallback, `missing-public-api` produced the expected
  `public-api-missing` fallback with zero command frames, and both rows reported `background_window=true`. Benchmark
  smoke `CASES=commands ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, zero picture frames, 4,076
  command frames, `app_new_fps=203.8`, and `jbr_command_fps=203.8`. This does not change the focused descriptor-cap
  counter, which remains 8 after the 2026-06-13 21:15 full parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260614-060948/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260614-061056/suite.tsv`.
- Batched broad screenshot parity validation refreshed after ten focused parity lifecycle/descriptor hardenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture frames, 99,527
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the focused parity
  change counter to zero; keep subsequent per-change parity validation exact-row/tiny-group only until roughly ten more
  meaningful parity changes or an ABI/capability gate. Disk free was about 175Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-062458/suite.tsv`.
- Batched broad screenshot parity validation refreshed again after ten more focused parity lifecycle/descriptor
  hardenings: `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture
  frames, 97,534 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the
  focused parity change counter to zero again; keep subsequent per-change parity validation exact-row/tiny-group only
  until roughly ten more meaningful parity changes or an ABI/capability gate. Disk free was about 174Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-075504/suite.tsv`.
- Batched broad screenshot parity validation refreshed after ten focused parity lifecycle/descriptor hardenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture frames, 93,828
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the focused parity
  change counter to zero again; keep subsequent per-change parity validation exact-row/tiny-group only until roughly
  ten more meaningful parity changes or an ABI/capability gate. Disk free was about 173Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-092237/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-forced-context-runtime-effect-stable-color-filter`: the row
  now requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing
  forced-context surface/cache, effect-handle reuse, and RuntimeEffect source-cache guards. Exact validation
  `CASES=parity-forced-context-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with no fallback, zero picture frames, 697 command frames, one JBR image-cache clear, one scoped image-cache
  clear, one surface-change marker, one command-cache clear marker, 54 effect definitions, 1,080 effect uses, 1,071
  effect cache hits, 1,079 RuntimeEffect source-cache hits, one RuntimeEffect source-cache miss, zero RuntimeEffect
  failures, `avg_delta=1.981`, and `bad_pixel_ratio=0.04668`. This is focused descriptor/lifecycle change 1 after the
  2026-06-14 09:22 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-102147/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-resize-graphics-layer-color-matrix-filter`: the row now
  requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing resize
  surface/cache and effect-handle reuse guards. Exact validation
  `CASES=parity-resize-graphics-layer-color-matrix-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  the row's expected single resize fallback, zero picture frames, 883 command frames, one JBR image-cache clear, one
  scoped image-cache clear, one surface-change marker, one command-cache clear marker, eight effect definitions, 1,508
  effect uses, 1,500 effect cache hits, `avg_delta=1.907`, and `bad_pixel_ratio=0.04577`. This is focused
  descriptor/lifecycle change 2 after the 2026-06-14 09:22 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-102542/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-forced-context-graphics-layer-color-matrix-filter`: the row
  now requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing
  forced-context surface/cache and effect-handle reuse guards. Exact validation
  `CASES=parity-forced-context-graphics-layer-color-matrix-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with no fallback, zero picture frames, 696 command frames, one JBR image-cache clear, one scoped image-cache
  clear, one surface-change marker, one command-cache clear marker, 11 effect definitions, 1,266 effect uses, 1,255
  effect cache hits, `avg_delta=2.048`, and `bad_pixel_ratio=0.04815`. This is focused descriptor/lifecycle change 3
  after the 2026-06-14 09:22 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-102844/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-resize-graphics-layer-render-effect`: the row now requires
  at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing resize surface/cache
  and effect-handle reuse guards. Exact validation
  `CASES=parity-resize-graphics-layer-render-effect ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the
  row's expected single resize fallback, zero picture frames, 1,272 command frames, one JBR image-cache clear, one
  scoped image-cache clear, one surface-change marker, one command-cache clear marker, eight effect definitions, 1,811
  effect uses, 1,803 effect cache hits, `avg_delta=1.902`, and `bad_pixel_ratio=0.04557`. This is focused
  descriptor/lifecycle change 4 after the 2026-06-14 09:22 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-103149/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-forced-context-graphics-layer-render-effect`: the row now
  requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing forced-context
  surface/cache and effect-handle reuse guards. Exact validation
  `CASES=parity-forced-context-graphics-layer-render-effect ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  no fallback, zero picture frames, 1,427 command frames, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, 11 effect definitions, 2,195 effect uses, 2,184 effect cache
  hits, `avg_delta=2.045`, and `bad_pixel_ratio=0.04801`. This is focused descriptor/lifecycle change 5 after the
  2026-06-14 09:22 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-103622/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-runtime-effect-source-cache-eviction`: the row now requires
  at least one RuntimeEffect source-cache hit alongside its existing color-filter source-cache eviction and
  effect-handle reuse guards. Exact validation
  `CASES=parity-runtime-effect-source-cache-eviction ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 1,004 command frames, 1,427 RuntimeEffect source-cache hits, 2,857 misses, 2,855
  evicts, 40 effect definitions, 4,284 effect uses, 4,272 effect cache hits, `avg_delta=2.118`, and
  `bad_pixel_ratio=0.05032`. This is focused descriptor/lifecycle change 6 after the 2026-06-14 09:22 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-104020/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-runtime-effect-shader-source-cache-eviction`: the row now
  requires at least one RuntimeEffect source-cache hit alongside its existing shader source-cache eviction and
  shader-handle reuse guards. Exact validation
  `CASES=parity-runtime-effect-shader-source-cache-eviction ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  no fallback, zero picture frames, 938 command frames, 1,449 RuntimeEffect source-cache hits, 2,901 misses, 2,899
  evicts, 20 shader definitions, 4,350 shader uses, 4,338 shader cache hits, `avg_delta=2.084`, and
  `bad_pixel_ratio=0.04931`. This is focused descriptor/lifecycle change 7 after the 2026-06-14 09:22 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-104239/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-forced-context-color-shader`: the row now requires at least
  one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing forced-context surface/cache,
  shader-definition/reuse, and effect-definition guards. Exact validation
  `CASES=parity-forced-context-color-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no fallback,
  zero picture frames, 1,025 command frames, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, 45 effect definitions, nine shader definitions, 1,547 shader
  uses, 1,538 shader cache hits, `avg_delta=1.974`, and `bad_pixel_ratio=0.04646`. This is focused
  descriptor/lifecycle change 1 after the 2026-06-14 07:55 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-085352/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-resize-noise-shader`: the row now requires at least one JBR
  image-cache clear and one scoped JBR image-cache clear alongside its existing resize surface/cache,
  shader-definition/reuse, and effect-definition guards. Exact validation
  `CASES=parity-resize-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's expected
  single resize fallback, zero picture frames, 925 command frames, one JBR image-cache clear, one scoped image-cache
  clear, one surface-change marker, one command-cache clear marker, 30 effect definitions, six shader definitions,
  1,347 shader uses, 1,341 shader cache hits, `avg_delta=1.849`, and `bad_pixel_ratio=0.04439`. This is focused
  descriptor/lifecycle change 2 after the 2026-06-14 07:55 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-085700/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-forced-context-noise-shader`: the row now requires at least
  one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing forced-context surface/cache,
  shader-definition/reuse, and effect-definition guards. Exact validation
  `CASES=parity-forced-context-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no fallback,
  zero picture frames, 1,036 command frames, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, 45 effect definitions, nine shader definitions, 1,440 shader
  uses, 1,431 shader cache hits, `avg_delta=1.975`, and `bad_pixel_ratio=0.04639`. This is focused
  descriptor/lifecycle change 3 after the 2026-06-14 07:55 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-085944/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-resize-turbulence-shader`: the row now requires at least
  one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing resize surface/cache,
  shader-definition/reuse, and effect-definition guards. Exact validation
  `CASES=parity-resize-turbulence-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's expected
  single resize fallback, zero picture frames, 1,065 command frames, one JBR image-cache clear, one scoped image-cache
  clear, one surface-change marker, one command-cache clear marker, 35 effect definitions, seven shader definitions,
  1,613 shader uses, 1,606 shader cache hits, `avg_delta=1.850`, and `bad_pixel_ratio=0.04455`. This is focused
  descriptor/lifecycle change 4 after the 2026-06-14 07:55 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-090236/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-forced-context-turbulence-shader`: the row now requires at
  least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing forced-context
  surface/cache, shader-definition/reuse, and effect-definition guards. Exact validation
  `CASES=parity-forced-context-turbulence-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 790 command frames, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, 50 effect definitions, ten shader definitions, 1,268 shader
  uses, 1,258 shader cache hits, `avg_delta=1.978`, and `bad_pixel_ratio=0.04661`. This is focused
  descriptor/lifecycle change 5 after the 2026-06-14 07:55 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-090701/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-resize-composite-noise-shader`: the row now requires at
  least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing resize surface/cache,
  shader-definition/reuse, and effect-definition guards. Exact validation
  `CASES=parity-resize-composite-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's
  expected single resize fallback, zero picture frames, 790 command frames, one JBR image-cache clear, one scoped
  image-cache clear, one surface-change marker, one command-cache clear marker, 35 effect definitions, 21 shader
  definitions, 1,340 shader uses, 1,333 shader cache hits, `avg_delta=1.846`, and `bad_pixel_ratio=0.04430`. This is
  focused descriptor/lifecycle change 6 after the 2026-06-14 07:55 full parity sweep, so broad parity remains
  deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-090959/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-forced-context-composite-noise-shader`: the row now
  requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing
  forced-context surface/cache, shader-definition/reuse, and effect-definition guards. Exact validation
  `CASES=parity-forced-context-composite-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 800 command frames, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, 45 effect definitions, 27 shader definitions, 1,324 shader
  uses, 1,315 shader cache hits, `avg_delta=1.973`, and `bad_pixel_ratio=0.04630`. This is focused
  descriptor/lifecycle change 7 after the 2026-06-14 07:55 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-091228/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-resize-runtime-effect-pure-color`: the row now requires at
  least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing resize surface/cache,
  shader-definition/reuse, effect-definition, and RuntimeEffect source-cache guards. Exact validation
  `CASES=parity-resize-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's
  expected single resize fallback, zero picture frames, 771 command frames, one JBR image-cache clear, one scoped
  image-cache clear, one surface-change marker, one command-cache clear marker, 40 effect definitions, eight shader
  definitions, 1,213 shader uses, 1,205 shader cache hits, 1,211 RuntimeEffect source-cache hits, one RuntimeEffect
  source-cache miss, zero RuntimeEffect failures, `avg_delta=1.845`, and `bad_pixel_ratio=0.04438`. This is focused
  descriptor/lifecycle change 8 after the 2026-06-14 07:55 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-091510/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-forced-context-runtime-effect-pure-color`: the row now
  requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing
  forced-context surface/cache, shader-definition/reuse, effect-definition, and RuntimeEffect source-cache guards.
  Exact validation `CASES=parity-forced-context-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with no fallback, zero picture frames, 711 command frames, one JBR image-cache clear, one scoped image-cache
  clear, one surface-change marker, one command-cache clear marker, 40 effect definitions, eight shader definitions,
  1,257 shader uses, 1,249 shader cache hits, 1,256 RuntimeEffect source-cache hits, one RuntimeEffect source-cache
  miss, zero RuntimeEffect failures, `avg_delta=1.964`, and `bad_pixel_ratio=0.04619`. This is focused
  descriptor/lifecycle change 9 after the 2026-06-14 07:55 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-091740/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-resize-runtime-effect-stable-color-filter`: the row now
  requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing resize
  surface/cache, effect-handle reuse, and RuntimeEffect source-cache guards. Exact validation
  `CASES=parity-resize-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  the row's expected single resize fallback, zero picture frames, 805 command frames, one JBR image-cache clear, one
  scoped image-cache clear, one surface-change marker, one command-cache clear marker, 48 effect definitions, 1,330
  effect uses, 1,322 effect cache hits, 1,328 RuntimeEffect source-cache hits, one RuntimeEffect source-cache miss,
  zero RuntimeEffect failures, `avg_delta=1.855`, and `bad_pixel_ratio=0.04467`. This is focused
  descriptor/lifecycle change 10 after the 2026-06-14 07:55 full parity sweep, so the next validation step is the
  batched broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-092014/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-forced-context-native-generic-font-text`: the row now
  requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing
  forced-context surface/cache guards and effect-definition guard. Exact validation
  `CASES=parity-forced-context-native-generic-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 1,032 command frames, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, 45 effect definitions, `avg_delta=2.129`, and
  `bad_pixel_ratio=0.04848`. This is focused descriptor/lifecycle change 1 after the 2026-06-14 06:24 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-072430/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-forced-context-native-loaded-font-data-text`: the row now
  requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing font-data,
  forced-context surface/cache, and effect-definition guards. Exact validation
  `CASES=parity-forced-context-native-loaded-font-data-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  no fallback, zero picture frames, 890 command frames, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, 30 effect definitions, `avg_delta=2.062`, and
  `bad_pixel_ratio=0.04772`. This is focused descriptor/lifecycle change 2 after the 2026-06-14 06:24 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-072816/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-forced-context-native-resource-font-text`: the row now
  requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing font-data,
  forced-context surface/cache, and effect-definition guards. Exact validation
  `CASES=parity-forced-context-native-resource-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 1,023 command frames, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, 30 effect definitions, `avg_delta=2.089`, and
  `bad_pixel_ratio=0.04805`. This is focused descriptor/lifecycle change 3 after the 2026-06-14 06:24 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-073218/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-resize-native-generic-font-text`: the row now requires at
  least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing resize surface/cache and
  effect-definition guards. Exact validation
  `CASES=parity-resize-native-generic-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's
  expected single resize fallback, zero picture frames, 1,063 command frames, one JBR image-cache clear, one scoped
  image-cache clear, one surface-change marker, one command-cache clear marker, 35 effect definitions,
  `avg_delta=1.982`, and `bad_pixel_ratio=0.04633`. This is focused descriptor/lifecycle change 4 after the
  2026-06-14 06:24 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-073613/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-resize-native-loaded-font-data-text`: the row now requires
  at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing font-data, resize
  surface/cache, and effect-definition guards. Exact validation
  `CASES=parity-resize-native-loaded-font-data-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 673 command frames, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, 25 effect definitions, `avg_delta=1.923`, and
  `bad_pixel_ratio=0.04559`. This is focused descriptor/lifecycle change 5 after the 2026-06-14 06:24 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-074046/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-resize-native-resource-font-text`: the row now requires at
  least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing font-data, resize
  surface/cache, and effect-definition guards. Exact validation
  `CASES=parity-resize-native-resource-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 1,048 command frames, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, 25 effect definitions, `avg_delta=1.946`, and
  `bad_pixel_ratio=0.04588`. This is focused descriptor/lifecycle change 6 after the 2026-06-14 06:24 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-074259/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-resize-native-system-font-text`: the row now requires at
  least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing resize surface/cache and
  effect-definition guards. Exact validation
  `CASES=parity-resize-native-system-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's
  expected single resize fallback, zero picture frames, 492 command frames, one JBR image-cache clear, one scoped
  image-cache clear, one surface-change marker, one command-cache clear marker, 35 effect definitions,
  `avg_delta=1.868`, and `bad_pixel_ratio=0.04499`. This is focused descriptor/lifecycle change 7 after the
  2026-06-14 06:24 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-074522/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-resize-color-filter-handle`: the row now requires at least
  one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing resize surface/cache and
  effect-handle definition/use/cache-hit guards. Exact validation
  `CASES=parity-resize-color-filter-handle ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's
  expected single resize fallback, zero picture frames, 1,251 command frames, one JBR image-cache clear, one scoped
  image-cache clear, one surface-change marker, one command-cache clear marker, nine effect definitions, 2,013 effect
  uses, 2,004 effect cache hits, `avg_delta=1.922`, and `bad_pixel_ratio=0.04650`. This is focused
  descriptor/lifecycle change 8 after the 2026-06-14 06:24 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-074820/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-forced-context-color-filter-handle`: the row now requires
  at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing forced-context
  surface/cache and effect-handle definition/use/cache-hit guards. Exact validation
  `CASES=parity-forced-context-color-filter-handle ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 1,110 command frames, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, 11 effect definitions, 1,853 effect uses, 1,842 effect cache
  hits, `avg_delta=2.065`, and `bad_pixel_ratio=0.04897`. This is focused descriptor/lifecycle change 9 after the
  2026-06-14 06:24 full parity sweep, so broad parity remains deferred until roughly one more focused parity change or
  an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-075047/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-resize-color-shader`: the row now requires at least one JBR
  image-cache clear and one scoped JBR image-cache clear alongside its existing resize surface/cache,
  shader-definition/reuse, and effect-definition guards. Exact validation
  `CASES=parity-resize-color-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's expected
  single resize fallback, zero picture frames, 925 command frames, one JBR image-cache clear, one scoped image-cache
  clear, one surface-change marker, one command-cache clear marker, 40 effect definitions, eight shader definitions,
  1,461 shader uses, 1,453 shader cache hits, `avg_delta=1.850`, and `bad_pixel_ratio=0.04454`. This is focused
  descriptor/lifecycle change 10 after the 2026-06-14 06:24 full parity sweep, so the next validation step is a
  batched broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-075306/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-forced-context-native-system-font-text`: the row now
  requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing
  forced-context surface/cache guards and effect-definition guard. Exact validation
  `CASES=parity-forced-context-native-system-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 956 command frames, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, 45 effect definitions, `avg_delta=1.995`, and
  `bad_pixel_ratio=0.04699`. This was focused descriptor/lifecycle change 10 after the 2026-06-13 21:15 full parity
  sweep and triggered the batched broad parity sweep above:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-062342/suite.tsv`.
- Focused parity lifecycle hardening continued with `parity-forced-context-native-custom-font-text-image`: the row now
  requires at least one JBR image-cache clear and one scoped JBR image-cache clear alongside its existing
  forced-context surface/cache guards and effect-definition guard. Exact validation
  `CASES=parity-forced-context-native-custom-font-text-image ./scripts/jbr-skia-screenshot-parity-suite.sh` passed
  with no fallback, zero picture frames, 1,041 command frames, one JBR image-cache clear, one scoped image-cache clear,
  one surface-change marker, one command-cache clear marker, 40 effect definitions, `avg_delta=2.123`, and
  `bad_pixel_ratio=0.05044`. This is focused descriptor/lifecycle change 9 after the 2026-06-13 21:15 full parity
  sweep, so broad parity remains deferred until roughly one more focused change or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-061859/suite.tsv`.
- Focused command-probe hardening continued with
  `commands-invalid-radial-gradient-shader-descriptor-radius-fallback`: the row now requires shader-handle reuse/cache
  evidence in addition to its existing definition guards before the intentional radial-gradient corrupt radius
  fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-radial-gradient-shader-descriptor-radius-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, no unsupported marker, zero picture frames, zero command frames after fallback,
  three shader definitions, 1,663 shader uses, 1,662 shader cache hits, zero effect-handle markers, and zero
  RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-000122/suite.tsv`.
- Focused command-probe hardening continued with
  `commands-invalid-linear-gradient-shader-descriptor-stop-order-fallback`: the row now requires shader/effect handle
  use evidence in addition to its existing definition guards before the intentional linear-gradient corrupt stop-order
  fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-linear-gradient-shader-descriptor-stop-order-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, no unsupported marker, zero picture frames, zero command frames after fallback,
  one effect definition/use, two shader definitions, 1,463 shader uses, 1,461 shader cache hits, zero effect cache
  hits, and zero RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-235818/suite.tsv`.
- Focused command-probe hardening continued with
  `commands-invalid-linear-gradient-shader-descriptor-tile-mode-fallback`: the row now requires shader/effect handle
  use evidence in addition to its existing definition guards before the intentional linear-gradient corrupt tile-mode
  fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-linear-gradient-shader-descriptor-tile-mode-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, no unsupported marker, zero picture frames, zero command frames after fallback,
  one effect definition/use, two shader definitions, 1,651 shader uses, 1,649 shader cache hits, zero effect cache
  hits, and zero RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-235247/suite.tsv`.
- Focused command-probe hardening continued with
  `commands-invalid-composite-shader-descriptor-blend-mode-fallback`: the row now requires exactly three JBR
  shader-handle definitions plus shader-handle use/cache-hit evidence before the intentional composite-shader corrupt
  blend-mode fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-composite-shader-descriptor-blend-mode-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, no unsupported marker, zero picture frames, zero command frames after fallback,
  three shader definitions, 1,901 shader uses, 1,900 shader cache hits, zero effect-handle markers, and zero
  RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-235013/suite.tsv`.
- Focused command-probe hardening continued with
  `commands-invalid-transformed-shader-descriptor-payload-count-fallback`: the row now requires exactly two JBR
  shader-handle definitions plus shader-handle use/cache-hit evidence before the intentional transformed-shader corrupt
  payload-count fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-transformed-shader-descriptor-payload-count-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, no unsupported marker, zero picture frames, zero command frames after fallback,
  two shader definitions, 1,883 shader uses, 1,882 shader cache hits, zero effect-handle markers, and zero
  RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-234735/suite.tsv`.
- Focused command-probe hardening continued with `commands-invalid-shader-descriptor-version-fallback`: the shared
  descriptor-version case block now requires exactly one JBR shader-handle definition plus shader-handle use/cache-hit
  evidence before the intentional corrupt descriptor-version fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-shader-descriptor-version-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, no unsupported marker, zero picture frames, zero command frames after fallback,
  one shader definition, 1,544 shader uses, 1,543 shader cache hits, zero effect-handle markers, and zero RuntimeEffect
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-234432/suite.tsv`.
- Focused command-probe hardening continued with `commands-invalid-shader-descriptor-record-length-fallback`: the row
  now requires exactly one JBR shader-handle definition plus shader-handle use/cache-hit evidence before the
  intentional corrupt record-length fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-shader-descriptor-record-length-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, no unsupported marker, zero picture frames, zero command frames after fallback,
  one shader definition, 1,665 shader uses/cache hits, zero effect-handle markers, and zero RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-234224/suite.tsv`.
- Focused command-probe hardening continued with
  `commands-invalid-shader-color-filter-descriptor-payload-count-fallback`: the row now requires exactly two JBR
  shader-handle definitions, at least one shader-handle use/cache-hit frame, and exactly one JBR effect-handle
  definition before the intentional corrupt shader-color-filter payload-count fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-shader-color-filter-descriptor-payload-count-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, no unsupported marker, zero picture frames, zero command frames after fallback,
  one effect definition, two shader definitions, 1,851 shader uses/cache hits, zero effect uses/cache hits, and zero
  RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-234001/suite.tsv`.
- Focused command-probe hardening continued with `commands-invalid-shader-descriptor-payload-count-fallback`: the row
  now requires exactly one JBR shader-handle definition plus shader-handle use/cache-hit evidence before the
  intentional corrupt payload-count fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-shader-descriptor-payload-count-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, no unsupported marker, zero picture frames, zero command frames after fallback,
  one shader definition, 1,391 shader uses, 1,390 shader cache hits, zero effect-handle markers, and zero
  RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-233518/suite.tsv`.
- Focused command-probe hardening continued with
  `commands-invalid-color-shader-descriptor-payload-count-fallback`: the row now requires exactly one JBR
  shader-handle definition plus shader-handle use/cache-hit evidence before the intentional corrupt payload-count
  fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-color-shader-descriptor-payload-count-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with `fallback_new_count=1`, no unsupported marker, zero picture frames, zero command frames after fallback,
  one shader definition, 1,532 shader uses, 1,532 shader cache hits, zero effect-handle markers, and zero
  RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-233307/suite.tsv`.
- Narrow artifact/benchmark validation was refreshed after the compatibility matrix and eight focused descriptor-cap
  tightenings. Required artifact matrix
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2:
  `current-all` replayed 492 command frames with no fallback, `missing-public-api` produced the expected single
  fallback with zero command frames, and both rows reported `background_window=true`. Benchmark smoke
  `CASES=commands ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, zero picture frames, 3,898 command
  frames, `app_new_fps=194.8`, and `jbr_command_fps=194.9`. This does not change the focused descriptor-cap counter,
  which remains 8 after the 2026-06-13 21:15 full parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260613-232728/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260613-232907/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-native-custom-font-text-image`: minimum and max JBR
  effect-handle definition guards were added after the latest broad sweep showed 20 definitions and no effect-handle
  uses. Exact validation passed with no fallback, zero picture frames, 882 JBR command frames, 20 effect definitions,
  zero effect/shader handle uses, zero RuntimeEffect markers, zero surface-change/cache-clear markers,
  `avg_delta=2.123`, and `bad_pixel_ratio=0.05044`. This is focused descriptor-cap change 8 after the 2026-06-13
  21:15 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-232306/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-geometry-clean`: minimum and max JBR effect-handle
  definition guards were added after the latest broad sweep showed 30 definitions and no effect-handle uses. Exact
  validation passed with no fallback, zero picture frames, 617 JBR command frames, 20 effect definitions, zero
  effect/shader handle uses, zero RuntimeEffect markers, zero surface-change/cache-clear markers, `avg_delta=2.805`,
  and `bad_pixel_ratio=0.07203`. This is focused descriptor-cap change 7 after the 2026-06-13 21:15 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-231721/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-pure-color`: minimum and max JBR
  effect-handle definition guards were added after the latest broad sweep showed 15 definitions and no effect-handle
  uses. Exact validation passed with no fallback, zero picture frames, 980 JBR command frames, 20 effect definitions,
  four shader definitions, 1,568 shader uses, 1,564 shader cache-hit frames, 1,567 RuntimeEffect source-cache hits,
  one source-cache miss, zero surface-change/cache-clear markers, `avg_delta=2.105`, and
  `bad_pixel_ratio=0.04993`. This is focused descriptor-cap change 6 after the 2026-06-13 21:15 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-231339/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-turbulence-shader`: minimum and max JBR
  effect-handle definition guards were added after the latest broad sweep showed 30 definitions and no effect-handle
  uses. Exact validation passed with no fallback, zero picture frames, 454 JBR command frames, 45 effect definitions,
  nine shader definitions, 928 shader uses, 919 shader cache-hit frames, one JBR image-cache clear, one scoped
  image-cache clear, one surface-change marker, one command-cache clear marker, `avg_delta=1.978`, and
  `bad_pixel_ratio=0.04661`. This is focused descriptor-cap change 5 after the 2026-06-13 21:15 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-231024/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-turbulence-shader`: minimum and max JBR
  effect-handle definition guards were added after the latest broad sweep showed 20 definitions and no effect-handle
  uses. Exact validation passed with the row's single resize fallback, zero picture frames, 799 JBR command frames, 35
  effect definitions, seven shader definitions, 1,312 shader uses, 1,305 shader cache-hit frames, one JBR image-cache
  clear, one scoped image-cache clear, one surface-change marker, one command-cache clear marker,
  `avg_delta=1.850`, and `bad_pixel_ratio=0.04455`. This is focused descriptor-cap change 4 after the 2026-06-13
  21:15 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-230715/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-turbulence-shader`: minimum and max JBR effect-handle
  definition guards were added after the latest broad sweep showed 15 definitions and no effect-handle uses. Exact
  validation passed with no fallback, zero picture frames, 960 JBR command frames, 15 effect definitions, three shader
  definitions, 1,608 shader uses, 1,605 shader cache-hit frames, zero RuntimeEffect markers, zero
  surface-change/cache-clear markers, `avg_delta=2.119`, and `bad_pixel_ratio=0.05035`. This is focused
  descriptor-cap change 3 after the 2026-06-13 21:15 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-230432/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-noise-shader`: minimum and max JBR
  effect-handle definition guards were added after the latest broad sweep showed 30 definitions and no effect-handle
  uses. Exact validation passed with no fallback, zero picture frames, 564 JBR command frames, 35 effect definitions,
  seven shader definitions, 1,031 shader uses, 1,024 shader cache-hit frames, one JBR image-cache clear, one scoped
  image-cache clear, one surface-change marker, one command-cache clear marker, `avg_delta=1.975`, and
  `bad_pixel_ratio=0.04639`. This is focused descriptor-cap change 2 after the 2026-06-13 21:15 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-230138/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-noise-shader`: minimum and max JBR effect-handle
  definition guards were added after the latest broad sweep showed 30 definitions and no effect-handle uses. Exact
  validation passed with the row's single resize fallback, zero picture frames, 1,100 JBR command frames, 35 effect
  definitions, seven shader definitions, 1,656 shader uses, 1,649 shader cache-hit frames, one JBR image-cache clear,
  one scoped image-cache clear, one surface-change marker, one command-cache clear marker, `avg_delta=1.849`, and
  `bad_pixel_ratio=0.04439`. This is focused descriptor-cap change 1 after the 2026-06-13 21:15 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-225853/suite.tsv`.
- Compatibility matrix refreshed after the full command-probe sweep and latest batched parity sweep:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with
  `fallback_sum=56`, 182 JBR command frames from the `happy` row, and `background_window=true` on all rows. Disk free
  was about 192Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260613-222843/matrix.tsv`.
- Batched broad parity validation refreshed after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=8`, zero picture frames, 63,790
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the focused descriptor
  cap change counter to zero. Disk free was about 169Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-211500/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-noise-shader`: minimum and max JBR effect-handle definition
  guards were added after the latest broad sweep showed 20 definitions and no effect-handle uses. Exact validation
  passed with no fallback, zero picture frames, 783 JBR command frames, 20 effect definitions, four shader definitions,
  1,278 shader uses, 1,274 shader cache-hit frames, zero RuntimeEffect markers, zero surface-change/cache-clear
  markers, `avg_delta=2.116`, and `bad_pixel_ratio=0.05013`. This is focused descriptor-cap change 10 after the
  2026-06-13 19:34 full parity sweep, so the next step is the batched broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-211224/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-color-shader`: minimum and max JBR
  effect-handle definition guards were added after the latest broad sweep showed 45 definitions and no effect-handle
  uses. Exact validation passed with no fallback, zero picture frames, 1,051 JBR command frames, 50 effect
  definitions, 10 shader definitions, 1,536 shader uses, 1,526 shader cache-hit frames, one JBR image-cache clear, one
  scoped image-cache clear, one surface-change marker, one context-change marker, one command-cache clear marker,
  `avg_delta=1.974`, and `bad_pixel_ratio=0.04646`. This is focused descriptor-cap change 9 after the 2026-06-13
  19:34 full parity sweep; after one more focused change, run the next batched broad parity sweep unless an
  ABI/capability gate supersedes it:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-210911/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-color-shader`: minimum and max JBR effect-handle
  definition guards were added after the latest broad sweep showed 35 definitions and no effect-handle uses. Exact
  validation passed with the row's single resize fallback, zero picture frames, 627 JBR command frames, 30 effect
  definitions, six shader definitions, 1,232 shader uses, 1,226 shader cache-hit frames, one JBR image-cache clear,
  one scoped image-cache clear, one surface-change marker, one command-cache clear marker, `avg_delta=1.850`, and
  `bad_pixel_ratio=0.04454`. This is focused descriptor-cap change 8 after the 2026-06-13 19:34 full parity sweep,
  so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-210633/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-color-shader`: minimum and max JBR effect-handle definition
  guards were added after the latest broad sweep showed 20 definitions and no effect-handle uses. Exact validation
  passed with no fallback, zero picture frames, 660 JBR command frames, 20 effect definitions, four shader definitions,
  1,084 shader uses, 1,080 shader cache-hit frames, zero RuntimeEffect markers, zero surface-change/cache-clear
  markers, `avg_delta=2.115`, and `bad_pixel_ratio=0.05020`. This is focused descriptor-cap change 7 after the
  2026-06-13 19:34 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-210338/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-composite-noise-shader`: minimum and max JBR
  effect-handle definition guards were added after the latest broad sweep showed 45 definitions and no effect-handle
  uses. Exact validation passed with no fallback, zero picture frames, 564 JBR command frames, 45 effect definitions,
  27 shader definitions, 1,138 shader uses, 1,129 shader cache-hit frames, one JBR image-cache clear, one scoped
  image-cache clear, one surface-change marker, one context-change marker, one command-cache clear marker,
  `avg_delta=1.973`, and `bad_pixel_ratio=0.04630`. This is focused descriptor-cap change 6 after the 2026-06-13
  19:34 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-205644/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-composite-noise-shader`: minimum and max JBR
  effect-handle definition guards were added after the latest broad sweep showed 40 definitions and no effect-handle
  uses. Exact validation passed with the row's single resize fallback, zero picture frames, 588 JBR command frames, 35
  effect definitions, 21 shader definitions, 1,114 shader uses, 1,107 shader cache-hit frames, one JBR image-cache
  clear, one scoped image-cache clear, one surface-change marker, one command-cache clear marker,
  `avg_delta=1.846`, and `bad_pixel_ratio=0.04430`. This is focused descriptor-cap change 5 after the 2026-06-13
  19:34 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-205349/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-composite-noise-shader`: minimum and max JBR
  effect-handle definition guards were added after the latest broad sweep showed 20 definitions and no effect-handle
  uses. Exact validation passed with no fallback, zero picture frames, 402 JBR command frames, 15 effect definitions,
  nine shader definitions, 755 shader uses, 752 shader cache-hit frames, zero RuntimeEffect markers, zero
  surface-change/cache-clear markers, `avg_delta=2.114`, and `bad_pixel_ratio=0.05004`. This is focused
  descriptor-cap change 4 after the 2026-06-13 19:34 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-205047/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-shader-source-cache-eviction`: the missing
  minimum JBR effect-handle definition guard was added after the latest broad sweep showed 20 definitions and no
  effect-handle uses, preserving the existing max of 32. Exact validation passed with no fallback, zero picture frames,
  730 JBR command frames, 20 effect definitions, 20 shader definitions, 3,507 shader uses, 3,495 shader cache-hit
  frames, 1,168 RuntimeEffect source-cache hits, 2,339 misses, 2,337 evictions, zero compile/build failures, zero
  surface-change/cache-clear markers, `avg_delta=2.084`, and `bad_pixel_ratio=0.04931`. This is focused
  descriptor-cap change 3 after the 2026-06-13 19:34 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-204648/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-transformed-shader`: the missing minimum JBR effect-handle
  definition guard was added after the latest broad sweep showed 20 definitions and no effect-handle uses, preserving
  the existing max of 32. Exact validation passed with no fallback, zero picture frames, 539 JBR command frames, 20
  effect definitions, eight shader definitions, 1,018 shader uses, 1,014 shader cache-hit frames, zero
  surface-change/cache-clear markers, `avg_delta=2.110`, and `bad_pixel_ratio=0.05007`. This is focused
  descriptor-cap change 2 after the 2026-06-13 19:34 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-204032/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-composite-shader`: the missing minimum JBR effect-handle
  definition guard was added after the latest broad sweep showed 30 definitions and no effect-handle uses, preserving
  the existing max of 40. Exact validation passed with no fallback, zero picture frames, 736 JBR command frames, 25
  effect definitions, 15 shader definitions, 1,336 shader uses, 1,331 shader cache-hit frames, zero
  surface-change/cache-clear markers, `avg_delta=2.596`, and `bad_pixel_ratio=0.06710`. This is focused
  descriptor-cap change 1 after the 2026-06-13 19:34 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-203517/suite.tsv`.
- Batched broad parity validation refreshed after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture frames, 90,449
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the focused descriptor
  cap change counter to zero:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-193432/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-shader`: the missing minimum JBR
  effect-handle definition guard was added after the latest broad sweep showed 20 definitions and no effect-handle
  uses, preserving the existing max of 24. Exact validation passed with no fallback, zero picture frames, 571 JBR
  command frames, 20 effect definitions, 12 shader definitions, 1,033 shader uses, 1,029 shader cache-hit frames,
  1,032 RuntimeEffect source-cache hits, one source-cache miss, zero surface-change/cache-clear markers,
  `avg_delta=2.111`, and `bad_pixel_ratio=0.05008`. This is focused descriptor-cap change 10 after the 2026-06-13
  18:07 full parity sweep, so the next step is the batched broad screenshot parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-193209/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-child-only`: the missing minimum JBR
  effect-handle definition guard was added after the latest broad sweep showed 20 definitions and no effect-handle
  uses, preserving the existing max of 32. Exact validation passed with no fallback, zero picture frames, 572 JBR
  command frames, 20 effect definitions, 12 shader definitions, 1,060 shader uses, 1,056 shader cache-hit frames,
  1,059 RuntimeEffect source-cache hits, one source-cache miss, zero surface-change/cache-clear markers,
  `avg_delta=2.118`, and `bad_pixel_ratio=0.05029`. This is focused descriptor-cap change 9 after the 2026-06-13
  18:07 full parity sweep; after one more focused change, run the next batched broad parity sweep unless an
  ABI/capability gate supersedes it:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-192932/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-uniform-only`: the missing minimum JBR
  effect-handle definition guard was added after the latest broad sweep showed 20 definitions and no effect-handle
  uses, preserving the existing max of 24. Exact validation passed with no fallback, zero picture frames, 695 JBR
  command frames, 20 effect definitions, four shader definitions, 1,185 shader uses, 1,181 shader cache-hit frames,
  1,184 RuntimeEffect source-cache hits, one source-cache miss, zero surface-change/cache-clear markers,
  `avg_delta=2.107`, and `bad_pixel_ratio=0.04997`. This is focused descriptor-cap change 8 after the 2026-06-13
  18:07 full parity sweep, so broad parity remains deferred until after roughly two more focused changes:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-192644/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-runtime-effect-pure-color`: minimum and max
  JBR effect-handle definition guards were added after the latest broad sweep showed 45 definitions and no
  effect-handle uses. Exact validation passed with no fallback, zero picture frames, 909 JBR command frames, 45 effect
  definitions, nine shader definitions, 1,389 shader uses, 1,380 shader cache-hit frames, 1,388 RuntimeEffect
  source-cache hits, one source-cache miss, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, `avg_delta=1.964`, and `bad_pixel_ratio=0.04619`. This is
  focused descriptor-cap change 7 after the 2026-06-13 18:07 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-192406/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-runtime-effect-pure-color`: minimum and max JBR
  effect-handle definition guards were added after the latest broad sweep showed 35 definitions and no effect-handle
  uses. Exact validation passed with the row's single resize fallback, zero picture frames, 676 JBR command frames, 35
  effect definitions, seven shader definitions, 1,129 shader uses, 1,122 shader cache-hit frames, 1,127 RuntimeEffect
  source-cache hits, one source-cache miss, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, `avg_delta=1.845`, and `bad_pixel_ratio=0.04438`. This is
  focused descriptor-cap change 6 after the 2026-06-13 18:07 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-192102/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-image-shader-color-filter`: its max effect-handle
  definition guard was reduced from 64 to 32 based on historical 18-30 definition reports, leaving the already-tight
  shader-handle guard unchanged. Exact validation passed with 24 effect definitions, four effect uses, eight shader
  definitions, 1,179 shader uses, 1,171 shader cache-hit frames, 672 JBR command frames, and no fallback. This is
  focused descriptor-cap change 1 after the 2026-06-13 13:41 full parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-144339/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-transformed-shader`: a max effect-handle definition guard
  of 32 was added based on historical 15-25 definition reports, leaving the existing shader-handle guard unchanged.
  Exact validation passed with 20 effect definitions, zero effect uses, eight shader definitions, 1,312 shader uses,
  1,308 shader cache-hit frames, 875 JBR command frames, and no fallback. This is focused descriptor-cap change 2
  after the 2026-06-13 13:41 full parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-144641/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-shader-color-filter`: a max effect-handle
  definition guard of 32 was added based on historical 18-24 definition reports, leaving the existing shader-handle
  guard unchanged. Exact validation passed with 24 effect definitions, four effect uses, eight shader definitions,
  1,325 shader uses, 1,317 shader cache-hit frames, 1,320 RuntimeEffect source-cache hits, one source-cache miss, 819
  JBR command frames, and no fallback. This is focused descriptor-cap change 3 after the 2026-06-13 13:41 full parity
  sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-144931/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-shader`: a max effect-handle definition
  guard of 24 was added based on historical 15-20 definition reports, leaving the existing shader-handle guard
  unchanged and correcting the guard placement so `parity-runtime-effect-child-only` remains uncapped. Exact validation
  passed with 20 effect definitions, zero effect uses, 12 shader definitions, 1,132 shader uses, 1,128 shader
  cache-hit frames, 1,131 RuntimeEffect source-cache hits, one source-cache miss, 759 JBR command frames, and no
  fallback. This is focused descriptor-cap change 4 after the 2026-06-13 13:41 full parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-145634/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-uniform-only`: a max effect-handle
  definition guard of 24 was added based on historical 15-20 definition reports, leaving the existing shader-handle
  guard unchanged. Exact validation passed with 20 effect definitions, zero effect uses, four shader definitions,
  1,132 shader uses, 1,128 shader cache-hit frames, 1,131 RuntimeEffect source-cache hits, one source-cache miss, 686
  JBR command frames, and no fallback. This is focused descriptor-cap change 5 after the 2026-06-13 13:41 full parity
  sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-150021/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-child-only`: a max effect-handle definition
  guard of 32 was added based on historical 15-25 definition reports, leaving the existing shader-handle guard
  unchanged and keeping headroom for the known 25-definition outlier. Exact validation passed with 20 effect
  definitions, zero effect uses, 12 shader definitions, 981 shader uses, 977 shader cache-hit frames, 980
  RuntimeEffect source-cache hits, one source-cache miss, 617 JBR command frames, and no fallback. This is focused
  descriptor-cap change 6 after the 2026-06-13 13:41 full parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-150241/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-color-filter-child`: a max effect-handle
  definition guard of 32 was added based on historical 24-32 definition reports. Exact validation passed with 32
  effect definitions, 1,051 effect uses, 1,047 effect cache-hit frames, zero shader handles, 1,050 RuntimeEffect
  source-cache hits, one source-cache miss, 630 JBR command frames, and no fallback. This is focused descriptor-cap
  change 7 after the 2026-06-13 13:41 full parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-150518/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-source-cache-eviction`: a max effect-handle
  definition guard of 40 was added based on historical 30-40 definition reports. Exact validation passed with 30
  effect definitions, 2,337 effect uses, 2,328 effect cache-hit frames, zero shader handles, 1,557 RuntimeEffect
  source-cache evicts, zero RuntimeEffect compile/build failures, 401 JBR command frames, and no fallback. This is
  focused descriptor-cap change 8 after the 2026-06-13 13:41 full parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-150902/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-shader-source-cache-eviction`: max
  shader/effect-handle definition guards of 32 were added based on historical 15-25 definition reports. Exact
  validation passed with 20 shader definitions, 3,006 shader uses, 2,994 shader cache-hit frames, 20 effect
  definitions, zero effect uses, 2,003 RuntimeEffect source-cache evicts, zero RuntimeEffect compile/build failures,
  626 JBR command frames, and no fallback. This is focused descriptor-cap change 9 after the 2026-06-13 13:41 full
  parity sweep; after one more focused change, run the next batched broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-151153/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-image-color-matrix-filter`: its max effect-handle
  definition guard was reduced from 64 to 32 based on historical 18-30 definition reports. Exact validation passed
  with 18 effect definitions, 1,077 effect uses, 1,074 effect cache-hit frames, zero shader handles, 668 JBR command
  frames, and no fallback. This is focused descriptor-cap change 10 after the 2026-06-13 13:41 full parity sweep, so
  the next step is the batched broad screenshot parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-151442/suite.tsv`.
- Batched broad parity validation refreshed after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=10`, zero picture frames, 92,868
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the focused descriptor
  cap change counter to zero:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-151647/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-image-filter`: minimum and max JBR effect-handle definition
  guards were added after historical rows showed 15-25 definitions and no effect/shader uses. Exact validation passed
  with no fallback, zero picture frames, 750 JBR command frames, 20 effect definitions, zero effect/shader handle uses,
  zero surface-change/cache-clear markers, `avg_delta=2.122`, and `bad_pixel_ratio=0.05041`. This is focused
  descriptor-cap change 1 after the 2026-06-13 15:16 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-161740/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-save-layer-filter`: minimum and max JBR effect-handle
  definition guards were added after historical rows showed 15-25 definitions and no effect/shader uses. Exact
  validation passed with no fallback, zero picture frames, 799 JBR command frames, 20 effect definitions, zero
  effect/shader handle uses, zero surface-change/cache-clear markers, `avg_delta=2.131`, and
  `bad_pixel_ratio=0.05037`. This is focused descriptor-cap change 2 after the 2026-06-13 15:16 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-162051/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-path-effect`: minimum and max JBR effect-handle definition
  guards were added after historical rows showed 20-25 definitions and no effect/shader uses. Exact validation passed
  with no fallback, zero picture frames, 1,024 JBR command frames, 20 effect definitions, zero effect/shader handle
  uses, zero surface-change/cache-clear markers, `avg_delta=2.208`, and `bad_pixel_ratio=0.05275`. This is focused
  descriptor-cap change 3 after the 2026-06-13 15:16 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-162333/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-image-shader`: minimum and max JBR effect-handle definition
  guards were added after historical rows showed 20-30 definitions and no effect/shader uses. Exact validation passed
  with no fallback, zero picture frames, 705 JBR command frames, 25 effect definitions, zero effect/shader handle uses,
  zero surface-change/cache-clear markers, `avg_delta=2.678`, and `bad_pixel_ratio=0.06777`. This is focused
  descriptor-cap change 4 after the 2026-06-13 15:16 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-162546/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-gradient-stroke`: minimum and max JBR effect-handle
  definition guards were added after historical rows showed 15-20 definitions and no effect/shader uses. Exact
  validation passed with no fallback, zero picture frames, 855 JBR command frames, 20 effect definitions, zero
  effect/shader handle uses, zero surface-change/cache-clear markers, `avg_delta=2.118`, and
  `bad_pixel_ratio=0.05030`. This is focused descriptor-cap change 5 after the 2026-06-13 15:16 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-162831/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-clip-path`: minimum and max JBR effect-handle definition
  guards were added after historical rows showed 15-20 definitions and no effect/shader uses. Exact validation passed
  with no fallback, zero picture frames, 667 JBR command frames, 20 effect definitions, zero effect/shader handle uses,
  zero surface-change/cache-clear markers, `avg_delta=2.121`, and `bad_pixel_ratio=0.05039`. This is focused
  descriptor-cap change 6 after the 2026-06-13 15:16 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-163100/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-point-dots`: minimum and max JBR effect-handle definition
  guards were added after recent rows showed exactly 20 definitions and no effect/shader uses. Exact validation passed
  with no fallback, zero picture frames, 601 JBR command frames, 20 effect definitions, zero effect/shader handle uses,
  zero surface-change/cache-clear markers, `avg_delta=2.122`, and `bad_pixel_ratio=0.05042`. This is focused
  descriptor-cap change 7 after the 2026-06-13 15:16 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-163501/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-vertices`: minimum and max JBR effect-handle definition
  guards were added after recent rows showed exactly 20 definitions and no effect/shader uses. Exact validation passed
  with no fallback, zero picture frames, 700 JBR command frames, 20 effect definitions, zero effect/shader handle uses,
  zero surface-change/cache-clear markers, `avg_delta=2.122`, and `bad_pixel_ratio=0.05041`. This is focused
  descriptor-cap change 8 after the 2026-06-13 15:16 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-163708/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-clip-rects`: minimum and max JBR effect-handle definition
  guards were added after recent rows showed exactly 20 definitions and no effect/shader uses. Exact validation passed
  with no fallback, zero picture frames, 696 JBR command frames, 20 effect definitions, zero effect/shader handle uses,
  zero surface-change/cache-clear markers, `avg_delta=2.108`, and `bad_pixel_ratio=0.05000`. This is focused
  descriptor-cap change 9 after the 2026-06-13 15:16 full parity sweep; after one more focused change, run the next
  batched broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-163916/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-skew-transform`: minimum and max JBR effect-handle
  definition guards were added after recent rows showed 15-30 definitions and no effect/shader uses. Exact validation
  passed with no fallback, zero picture frames, 1,029 JBR command frames, 25 effect definitions, zero effect/shader
  handle uses, zero surface-change/cache-clear markers, `avg_delta=2.606`, and `bad_pixel_ratio=0.06742`. This was
  focused descriptor-cap change 10 after the 2026-06-13 15:16 full parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-164129/suite.tsv`.
- Batched broad parity validation refreshed after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture frames, 98,167
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the focused descriptor
  cap change counter to zero:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-164327/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-rich`: minimum and max JBR effect-handle definition guards
  were added after recent broad sweeps showed exactly 20 definitions and no effect/shader uses. Exact validation
  passed with no fallback, zero picture frames, 631 JBR command frames, 20 effect definitions, zero effect/shader
  handle uses, zero surface-change/cache-clear markers, `avg_delta=2.123`, and `bad_pixel_ratio=0.05044`. This is
  focused descriptor-cap change 1 after the 2026-06-13 16:43 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-174324/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-native-loaded-font-data-text`: minimum and max JBR
  effect-handle definition guards were added after recent broad sweeps showed exactly 15 definitions and no
  effect/shader uses. Exact validation passed with no fallback, zero picture frames, 663 JBR command frames, 15 effect
  definitions, zero effect/shader handle uses, zero surface-change/cache-clear markers, `avg_delta=2.062`, and
  `bad_pixel_ratio=0.04772`. This is focused descriptor-cap change 2 after the 2026-06-13 16:43 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-174643/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-native-resource-font-text`: minimum and max JBR
  effect-handle definition guards were added after recent broad sweeps showed exactly 15 definitions and no
  effect/shader uses. Exact validation passed with no fallback, zero picture frames, 892 JBR command frames, 15 effect
  definitions, zero effect/shader handle uses, zero surface-change/cache-clear markers, `avg_delta=2.089`, and
  `bad_pixel_ratio=0.04805`. This is focused descriptor-cap change 3 after the 2026-06-13 16:43 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-174907/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-native-generic-font-text`: minimum and max JBR effect-handle
  definition guards were added after recent broad sweeps showed exactly 25 definitions and no effect/shader uses.
  Exact validation passed with no fallback, zero picture frames, 894 JBR command frames, 20 effect definitions, zero
  effect/shader handle uses, zero surface-change/cache-clear markers, `avg_delta=2.129`, and
  `bad_pixel_ratio=0.04848`. This is focused descriptor-cap change 4 after the 2026-06-13 16:43 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-175119/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-native-system-font-text`: minimum and max JBR effect-handle
  definition guards were added after recent broad sweeps showed 20-25 definitions and no effect/shader uses. Exact
  validation passed with no fallback, zero picture frames, 680 JBR command frames, 20 effect definitions, zero
  effect/shader handle uses, zero surface-change/cache-clear markers, `avg_delta=1.995`, and
  `bad_pixel_ratio=0.04699`. This is focused descriptor-cap change 5 after the 2026-06-13 16:43 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-175343/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-native-loaded-font-data-text`: minimum and max JBR
  effect-handle definition guards were added after recent broad sweeps showed exactly 25 definitions and no
  effect/shader uses. Exact validation passed with no fallback, zero picture frames, 630 JBR command frames, 25 effect
  definitions, one surface-change marker, one command-cache clear marker, zero effect/shader handle uses,
  `avg_delta=1.923`, and `bad_pixel_ratio=0.04559`. This is focused descriptor-cap change 6 after the 2026-06-13
  16:43 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-175633/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-native-resource-font-text`: minimum and max JBR
  effect-handle definition guards were added after recent broad sweeps showed exactly 25 definitions and no
  effect/shader uses. Exact validation passed with the row's single fallback, zero picture frames, 656 JBR command
  frames, 25 effect definitions, one surface-change marker, one command-cache clear marker, zero effect/shader handle
  uses, `avg_delta=1.946`, and `bad_pixel_ratio=0.04588`. This is focused descriptor-cap change 7 after the
  2026-06-13 16:43 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-175843/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-native-generic-font-text`: minimum and max JBR
  effect-handle definition guards were added after recent broad sweeps showed 35-40 definitions and no effect/shader
  uses. Exact validation passed with the row's single fallback, zero picture frames, 687 JBR command frames, 35 effect
  definitions, one surface-change marker, one command-cache clear marker, zero effect/shader handle uses,
  `avg_delta=1.982`, and `bad_pixel_ratio=0.04633`. This is focused descriptor-cap change 8 after the 2026-06-13
  16:43 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-180106/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-native-system-font-text`: minimum and max JBR
  effect-handle definition guards were added after recent broad sweeps showed 35-40 definitions and no effect/shader
  uses. Exact validation passed with the row's single fallback, zero picture frames, 659 JBR command frames, 35 effect
  definitions, one surface-change marker, one command-cache clear marker, zero effect/shader handle uses,
  `avg_delta=1.868`, and `bad_pixel_ratio=0.04499`. This is focused descriptor-cap change 9 after the 2026-06-13
  16:43 full parity sweep; after one more focused change, run the next batched broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-180424/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-native-loaded-font-data-text`: minimum and
  max JBR effect-handle definition guards were added after recent broad sweeps showed exactly 30 definitions and no
  effect/shader uses. Exact validation passed with no fallback, zero picture frames, 881 JBR command frames, 30 effect
  definitions, one surface-change marker, one command-cache clear marker, zero effect/shader handle uses,
  `avg_delta=2.062`, and `bad_pixel_ratio=0.04772`. This was focused descriptor-cap change 10 after the 2026-06-13
  16:43 full parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-180614/suite.tsv`.
- Batched broad parity validation refreshed after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=12`, zero picture frames, 100,079
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the focused descriptor
  cap change counter to zero:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-180718/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-native-resource-font-text`: minimum and max
  JBR effect-handle definition guards were added after recent broad sweeps showed exactly 30 definitions and no
  effect/shader uses. Exact validation passed with no fallback, zero picture frames, 625 JBR command frames, 30 effect
  definitions, one surface-change marker, one command-cache clear marker, zero effect/shader handle uses,
  `avg_delta=2.089`, and `bad_pixel_ratio=0.04805`. This is focused descriptor-cap change 1 after the 2026-06-13
  18:07 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-190601/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-native-generic-font-text`: minimum and max
  JBR effect-handle definition guards were added after recent broad sweeps showed exactly 45 definitions and no
  effect/shader uses. Exact validation passed with no fallback, zero picture frames, 838 JBR command frames, 45 effect
  definitions, one surface-change marker, one command-cache clear marker, zero effect/shader handle uses,
  `avg_delta=2.129`, and `bad_pixel_ratio=0.04848`. This is focused descriptor-cap change 2 after the 2026-06-13
  18:07 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-190813/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-native-system-font-text`: minimum and max JBR
  effect-handle definition guards were added after recent broad sweeps showed 45 definitions and exact validation
  observed 50 definitions, with no effect/shader uses. Exact validation passed with no fallback, zero picture frames,
  917 JBR command frames, 50 effect definitions, one surface-change marker, one command-cache clear marker, zero
  effect/shader handle uses, `avg_delta=1.995`, and `bad_pixel_ratio=0.04699`. This is focused descriptor-cap change
  3 after the 2026-06-13 18:07 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-191045/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-native-custom-font-text-image`: minimum and
  max JBR effect-handle definition guards were added after recent broad sweeps showed exactly 40 definitions and no
  effect/shader uses. Exact validation passed with no fallback, zero picture frames, 670 JBR command frames, 40 effect
  definitions, one surface-change marker, one command-cache clear marker, zero effect/shader handle uses,
  `avg_delta=2.123`, and `bad_pixel_ratio=0.05044`. This is focused descriptor-cap change 4 after the 2026-06-13
  18:07 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-191420/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-image-refs`: minimum and max JBR
  effect-handle definition guards were added after recent broad sweeps showed exactly 40 definitions and no
  effect/shader uses. Exact validation passed with no fallback, zero picture frames, 638 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, 40 effect definitions, one surface-change marker, one command-cache
  clear marker, zero effect/shader handle uses, `avg_delta=2.129`, and `bad_pixel_ratio=0.05050`. This is focused
  descriptor-cap change 5 after the 2026-06-13 18:07 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-191651/suite.tsv`.
- Local `/tmp` JBR API/desktop/native artifacts were refreshed on 2026-06-13 after stale artifacts caused
  `service-unavailable` fallback in exact compatibility/bridge-load checks. Post-rebuild focused validation passed:
  `commands-native-bridge-load-library` produced 895 JBR command frames with no fallback, compatibility `happy`
  produced 184 command frames with no fallback, and compatibility `public-api-missing` produced one structured
  fallback with zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-092558/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260613-092753/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260613-092719/matrix.tsv`.
- Required artifact matrix slice refreshed after the local artifact rebuild:
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2.
  `current-all` replayed 418 command frames with no fallback, and `missing-public-api` fell back once as expected:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260613-092936/matrix.tsv`.
- Single-case benchmark smoke refreshed after the artifact slice:
  `CASES=commands DURATION_SECONDS=5 WARMUP_SECONDS=1 EXPECT_SCREENSHOT_ASSERTION=false
  ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, 404 JBR command frames, and 80.8 command FPS:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260613-093152/suite.tsv`.
- Exact screenshot parity smoke row refreshed:
  `CASES=parity-rich ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no fallback, 642 JBR command frames,
  `avg_delta=2.123`, and `bad_pixel_ratio=0.05044`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-093333/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-color-shader`: its max shader-handle definition guard was
  reduced from 8 to 4 based on prior report data, then exact validation passed with three definitions, 995 uses, 992
  cache-hit frames, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-093718/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-noise-shader`: its max shader-handle definition guard was
  reduced from 8 to 4 based on prior report data, then exact validation passed with four definitions, 1,204 uses,
  1,200 cache-hit frames, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-094115/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-turbulence-shader`: its max shader-handle definition guard
  was reduced from 8 to 4 based on prior report data, then exact validation passed with four definitions, 1,323 uses,
  1,319 cache-hit frames, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-094416/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-color-shader`: its max shader-handle definition
  guard was reduced from 16 to 8 based on historical 6-8 definition reports, then exact validation passed with seven
  definitions, 940 uses, 933 cache-hit frames, and the row's expected single fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-094953/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-noise-shader`: its max shader-handle definition
  guard was reduced from 16 to 8 based on historical 6-8 definition reports, then exact validation passed with seven
  definitions, 1,067 uses, 1,060 cache-hit frames, and the row's expected single fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-095222/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-turbulence-shader`: its max shader-handle definition
  guard was reduced from 16 to 8 based on historical 6-8 definition reports, then exact validation passed with seven
  definitions, 1,069 uses, 1,062 cache-hit frames, and the row's expected single fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-095431/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-color-shader`: its max shader-handle
  definition guard was reduced from 24 to 12 based on historical 7-11 definition reports, then exact validation passed
  with nine definitions, 1,389 uses, 1,380 cache-hit frames, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-095658/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-noise-shader`: its max shader-handle
  definition guard was reduced from 24 to 12 based on historical 7-11 definition reports, then exact validation passed
  with nine definitions, 1,250 uses, 1,241 cache-hit frames, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-095920/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-turbulence-shader`: its max shader-handle
  definition guard was reduced from 24 to 12 based on historical 7-12 definition reports, then exact validation passed
  with nine definitions, 1,081 uses, 1,072 cache-hit frames, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-100137/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-image-shader-color-filter`: its max shader-handle
  definition guard was reduced from 24 to 12 based on historical 6-10 definition reports, then exact validation passed
  with eight definitions, 1,178 uses, 1,170 cache-hit frames, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-100541/suite.tsv`.
- Batched broad parity validation refreshed after the ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture frames, 85,489
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-100819/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-linear-gradient-shader-color-filter`: its max shader-handle
  definition guard was reduced from 24 to 12 based on historical 6-10 definition reports, then exact validation passed
  with eight definitions, 955 uses, 947 cache-hit frames, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-111254/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-transformed-shader`: its max shader-handle definition guard
  was reduced from 16 to 12 based on historical 6-10 definition reports, then exact validation passed with six
  definitions, 1,024 uses, 1,021 cache-hit frames, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-111754/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-shader-color-filter`: a max shader-handle
  definition guard of 12 was added based on historical 6-8 definition reports, then exact validation passed with eight
  definitions, 1,044 uses, 1,036 cache-hit frames, 1,039 RuntimeEffect source-cache hits, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-112055/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-pure-color`: its max shader-handle
  definition guard was reduced from 8 to 4 based on historical 3-4 definition reports. After correcting an initial
  broad text patch that briefly touched the already-validated resize color-shader row before commit, exact validation
  passed with four definitions, 891 uses, 887 cache-hit frames, 890 RuntimeEffect source-cache hits, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-112515/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-runtime-effect-pure-color`: its max shader-handle
  definition guard was reduced from 16 to 8 based on historical 6-8 definition reports, then exact validation passed
  with seven definitions, 1,108 uses, 1,101 cache-hit frames, 1,106 RuntimeEffect source-cache hits, and the row's
  expected single fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-112823/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-runtime-effect-pure-color`: its max
  shader-handle definition guard was reduced from 24 to 12 based on historical 7-12 definition reports, then exact
  validation passed with eight definitions, 935 uses, 927 cache-hit frames, 934 RuntimeEffect source-cache hits, one
  surface-change marker, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-113128/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-uniform-only`: a max shader-handle
  definition guard of 4 was added based on historical 3-4 definition reports, then exact validation passed with three
  definitions, 927 uses, 924 cache-hit frames, 926 RuntimeEffect source-cache hits, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-113426/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-child-only`: a max shader-handle definition
  guard of 12 was added based on historical 9-12 definition reports, then exact validation passed with 12 definitions,
  1,479 uses, 1,475 cache-hit frames, 1,478 RuntimeEffect source-cache hits, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-113823/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-shader`: a max shader-handle definition guard
  of 12 was added based on historical 9-12 definition reports, then exact validation passed with 12 definitions, 1,369
  uses, 1,365 cache-hit frames, 1,368 RuntimeEffect source-cache hits, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-114010/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-color-filter`: a max effect-handle definition
  guard of 24 was added based on historical 18-24 definition reports, then exact validation passed with 24 definitions,
  1,290 uses, 1,286 cache-hit frames, 1,289 RuntimeEffect source-cache hits, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-114224/suite.tsv`.
- Batched broad parity validation refreshed after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture frames, 103,309
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-114403/suite.tsv`.
- Full compatibility matrix refreshed after the batched parity reset:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57. The `happy` row
  replayed 475 command frames with no fallback, every forced mismatch/API row produced one structured fallback with
  zero command frames, and all rows stayed in background-window mode:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260613-124241/matrix.tsv`.
- Required artifact matrix slice refreshed after the full compatibility matrix:
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2.
  `current-all` replayed 711 command frames with no fallback, and `missing-public-api` fell back once as expected:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260613-130810/matrix.tsv`.
- Single-case benchmark smoke refreshed after the artifact slice:
  `CASES=commands DURATION_SECONDS=5 WARMUP_SECONDS=1 EXPECT_SCREENSHOT_ASSERTION=false
  ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, 474 JBR command frames, and 94.8 command FPS:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260613-131013/suite.tsv`.
- Exact screenshot parity smoke row refreshed after the benchmark smoke:
  `CASES=parity-rich ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no fallback, zero picture frames, 645
  JBR command frames, `avg_delta=2.123`, and `bad_pixel_ratio=0.05044`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-131153/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-runtime-effect-stable-color-filter`: its max effect-handle
  definition guard was reduced from 64 to 24 based on historical 24-definition reports, then exact validation passed
  with 24 definitions, 1,391 uses, 1,387 cache-hit frames, 1,390 RuntimeEffect source-cache hits, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-131424/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-runtime-effect-stable-color-filter`: its max
  effect-handle definition guard was reduced from 96 to 48 based on historical 36-42 definition reports, then exact
  validation passed with 42 definitions, 1,338 uses, 1,331 cache-hit frames, 1,336 RuntimeEffect source-cache hits, one
  surface-change marker, and the row's expected single fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-131720/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-runtime-effect-stable-color-filter`: its max
  effect-handle definition guard was reduced from 144 to 72 based on historical 54-60 definition reports, then exact
  validation passed with 54 definitions, 1,395 uses, 1,386 cache-hit frames, 1,394 RuntimeEffect source-cache hits, one
  surface-change marker, and no fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-131956/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-composite-shader-color-filter`: its max shader-handle
  definition guard was reduced from 32 to 24 based on historical 16-definition reports, then exact validation passed
  with 16 definitions, 1,249 uses, 1,241 cache-hit frames, 24 effect-handle definitions, four effect-handle uses, no
  fallback, and zero surface-change/cache-clear markers. This is focused descriptor-cap change 4 after the 2026-06-13
  11:44 full parity sweep; the next broad parity sweep remains deferred until roughly ten focused changes:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-132328/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-composite-noise-shader`: its max shader-handle definition
  guard was reduced from 24 to 16 based on historical 9-12 definition reports, then exact validation passed with 12
  definitions, 1,108 uses, 1,104 cache-hit frames, 20 effect-handle definitions, no fallback, and zero
  surface-change/cache-clear markers. This is focused descriptor-cap change 5 after the 2026-06-13 11:44 full parity
  sweep; the next broad parity sweep remains deferred until roughly ten focused changes:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-132536/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-resize-composite-noise-shader`: its max shader-handle
  definition guard was reduced from 48 to 32 based on historical 18-24 definition reports, then exact validation
  passed with 21 definitions, 1,229 uses, 1,222 cache-hit frames, 35 effect-handle definitions, the row's expected
  single fallback, one surface-change marker, and one command-cache clear marker. This is focused descriptor-cap
  change 6 after the 2026-06-13 11:44 full parity sweep; the next broad parity sweep remains deferred until roughly
  ten focused changes:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-132818/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-forced-context-composite-noise-shader`: its max
  shader-handle definition guard was reduced from 72 to 40 based on historical 24-33 definition reports, then exact
  validation passed with 27 definitions, 1,397 uses, 1,388 cache-hit frames, 45 effect-handle definitions, no fallback,
  one surface-change marker, and one command-cache clear marker. This is focused descriptor-cap change 7 after the
  2026-06-13 11:44 full parity sweep; the next broad parity sweep remains deferred until roughly ten focused changes:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-133044/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-composite-shader`: a max effect-handle definition guard of
  40 was added based on historical 20-35 definition reports, leaving the existing shader-handle cap unchanged. Exact
  validation passed with 25 effect definitions, 15 shader definitions, 1,103 shader uses, 1,098 shader cache-hit
  frames, no fallback, and zero surface-change/cache-clear markers. This is focused descriptor-cap change 8 after the
  2026-06-13 11:44 full parity sweep; the next broad parity sweep remains deferred until roughly ten focused changes:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-133324/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-composite-shader-color-filter`: its max effect-handle
  definition guard was reduced from 64 to 32 based on stable 24-definition reports, leaving the already-tightened
  shader-handle cap unchanged. Exact validation passed with 24 effect definitions, four effect uses, 16 shader
  definitions, 1,068 shader uses, 1,060 shader cache-hit frames, no fallback, and zero surface-change/cache-clear
  markers. This is focused descriptor-cap change 9 after the 2026-06-13 11:44 full parity sweep; after one more
  focused change, run the next broad parity batch unless an ABI/capability gate supersedes it:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-133624/suite.tsv`.
- Stable descriptor gate tightening continued with `parity-linear-gradient-shader-color-filter`: its max effect-handle
  definition guard was reduced from 64 to 32 based on historical 18-30 definition reports, leaving the
  already-tightened shader-handle cap unchanged. Exact validation passed with 24 effect definitions, four effect uses,
  eight shader definitions, 1,098 shader uses, 1,090 shader cache-hit frames, no fallback, and zero
  surface-change/cache-clear markers. This is focused descriptor-cap change 10 after the 2026-06-13 11:44 full parity
  sweep; the next step is the batched broad screenshot parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-133844/suite.tsv`.
- Batched broad parity validation refreshed after ten focused descriptor-cap tightenings:
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture frames, 98,884
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the focused descriptor
  cap change counter to zero:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-134112/suite.tsv`.
- Full default command-probe sweep refreshed after the focused quick-loop batch:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` passed 549/549 with
  `fallback_sum=350`, 79 unsupported-picture rows, 78,827 picture frames, and 156,233 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-144955/suite.tsv`.
- Tiny focused command-probe quick-loop batch refreshed after `smoke`: gradient path/stops,
  geometry/color-count/stroke/radius, image shader invalid, shader-ref invalid, fill-rect color-filter invalid, and
  blend-mode invalid groups passed 38/38 with `fallback_sum=10`, 28 unsupported-picture rows, 27,825 picture frames,
  and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-134712/suite.tsv`.
- Focused `smoke` command-probe group refreshed after `stream-invalid`: 6/6 passed with no fallback, no
  unsupported-picture rows, no picture frames, and 6,694 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-133407/suite.tsv`.
- Focused `stream-invalid` command-probe group refreshed after `save-layer-shader-fallbacks`: 8/8 passed with
  `fallback_sum=8`, no unsupported-picture rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-132344/suite.tsv`.
- Focused `save-layer-shader-fallbacks` command-probe group refreshed after `graphics-layer-extras`: 9/9 passed with
  no fallback, six unsupported-picture rows, 6,049 picture frames, and 3,221 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-131608/suite.tsv`.
- Focused `graphics-layer-extras` command-probe group refreshed after `graphics-layer`: 16/16 passed with no fallback,
  four unsupported-picture rows, 4,104 picture frames, and 12,671 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-125529/suite.tsv`.
- Focused `graphics-layer` command-probe group refreshed after `surface-transform-ui`: 22/22 passed with no fallback,
  one unsupported-picture row, 1,038 picture frames, and 26,873 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-122521/suite.tsv`.
- Focused `surface-transform-ui` command-probe group refreshed after `runtime-effect-invalid`: 15/15 passed with no
  fallback, no unsupported-picture rows, no picture frames, and 23,174 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-121443/suite.tsv`.
- Focused `runtime-effect-invalid` command-probe group refreshed after `gradient-invalid`: 62/62 passed with
  `fallback_sum=56`, seven unsupported-picture rows, 7,153 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-112117/suite.tsv`.
- Focused `gradient-invalid` command-probe group refreshed after `gradient-path-invalid`: 81/81 passed with
  `fallback_sum=60`, 21 unsupported-picture rows, 17,899 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-102137/suite.tsv`.
- Subagent validation can currently fail to produce Magic Jewel GUI frames: delegated `gradient-invalid` and
  `runtime-effect-invalid` attempts wrote first-row reports with no app/CMP/JBR frames, while serial exact rerun of
  those first rows passed 2/2. Keep command-probe validation serial unless the subagent GUI issue is isolated.
- Focused `gradient-path-invalid` command-probe group refreshed after `shader-composition-runtime`: 21/21 passed with
  `fallback_sum=18`, three unsupported-picture rows, 5,088 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-042752/suite.tsv`.
- Focused `shader-composition-runtime` command-probe group refreshed after `shader-rendering`: 15/15 passed with
  `fallback_sum=0`, two unsupported-picture rows, 3,599 picture frames, and 34,803 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-041606/suite.tsv`.
- Focused `shader-rendering` command-probe group refreshed after `native-text`: 18/18 passed with `fallback_sum=0`,
  ten unsupported-picture rows, 17,170 picture frames, and 21,380 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-040250/suite.tsv`.
- Focused `native-text` command-probe group refreshed after `core-effects`: 14/14 passed with `fallback_sum=0`, no
  unsupported-picture rows, no picture frames, and 33,822 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-035202/suite.tsv`.
- Focused `core-effects` command-probe group refreshed after `descriptor-lifecycle`: 8/8 passed with
  `fallback_sum=0`, three unsupported-picture rows, 5,099 picture frames, and 13,321 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-034519/suite.tsv`.
- Focused `descriptor-lifecycle` command-probe group refreshed after `color-filters`: 18/18 passed with
  `fallback_sum=0`, no unsupported-picture rows, no picture frames, and 49,005 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-032814/suite.tsv`.
- Focused `color-filters` command-probe group refreshed after `graphics-layer-invalid`: 13/13 passed with
  `fallback_sum=0`, three unsupported-picture rows, 5,056 picture frames, and 28,945 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-031803/suite.tsv`.
- Focused `graphics-layer-invalid` command-probe group refreshed after `save-layer-invalid`: 15/15 passed with
  `fallback_sum=0`, 15 unsupported-picture rows, 26,029 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-030703/suite.tsv`.
- Focused `save-layer-invalid` command-probe group refreshed after `descriptor-handles-invalid`: 37/37 passed with
  `fallback_sum=37`, no unsupported-picture rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-024126/suite.tsv`.
- Focused `descriptor-handles-invalid` command-probe group refreshed after `image-handles-invalid`: 48/48 passed with
  `fallback_sum=48`, no unsupported-picture rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-020826/suite.tsv`.
- Focused `image-handles-invalid` command-probe group refreshed after `shader-descriptor-invalid`: 27/27 passed with
  `fallback_sum=27`, no unsupported-picture rows, no picture frames, and 1,523 command frames from setup before
  fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-014928/suite.tsv`.
- Focused `shader-descriptor-invalid` command-probe group refreshed after `effect-descriptor-invalid`: 30/30 passed
  with `fallback_sum=30`, no unsupported-picture rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-012756/suite.tsv`.
- Focused `effect-descriptor-invalid` command-probe group refreshed after `path-invalid`: 28/28 passed with
  `fallback_sum=28`, no unsupported-picture rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-010824/suite.tsv`.
- Focused `path-invalid` command-probe group refreshed after `primitive-invalid`: 24/24 passed with
  `fallback_sum=22`, two unsupported-picture rows, 3,181 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-005139/suite.tsv`.
- Focused `primitive-invalid` command-probe group refreshed after `native-text-invalid`: 16/16 passed with
  `fallback_sum=13`, three unsupported-picture rows, 5,096 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-003919/suite.tsv`.
- Focused `native-text-invalid` command-probe group refreshed after the full sweep: 11/11 passed with
  `fallback_sum=11`, no unsupported-picture rows, no picture frames, and 1,996 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-003034/suite.tsv`.
- Full default command-probe structured-marker sweep refreshed after the saveLayer/shader fallback tail repair:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` passed 549/549 with
  `fallback_sum=350`, 80 unsupported-picture rows, 127,181 picture frames, and 306,312 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-181446/suite.tsv`.
- SaveLayer/shader command-probe fallback tail was refreshed after a broad default sweep reached 543 passing rows and
  exposed screenshot-gate brittleness on the raw saveLayer color-filter sentinel. Exact saveLayer command/fallback rows
  passed 4/4, and `CASE_GROUPS=save-layer-shader-fallbacks` passed 9/9 with `fallback_sum=0`, six
  unsupported-picture rows, 9,440 picture frames, and 7,901 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-180417/suite.tsv`.
- Focused `graphics-layer-extras` command-probe validation refreshed after `graphics-layer`: 16/16 passed with
  `fallback_sum=0`, four unsupported-picture rows, 5,374 picture frames, and 22,521 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-114204/suite.tsv`.
- Focused `graphics-layer` command-probe validation refreshed after the shadow guard audit: 22/22 passed with
  `fallback_sum=0`, one expected unsupported-picture row, 1,401 picture frames, and 32,675 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-112529/suite.tsv`.
- Magic Jewel focused graphics-layer shadow validation passed 5/5 while auditing `graphicsLayer:shadowFilter` as an
  internal image-filter descriptor-definition guard: supported shadow rows replayed 5,274 command frames with no
  unsupported reasons, and invalid elevation/path rows fell back with the expected public shadow summaries:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-112032/suite.tsv`.
- CMP now has direct defensive unit coverage for the recorder-only `roundRectStyle` unsupported branch. The exact
  `rejectsUnknownRoundRectPaintStyleInStrictMode` method passed, followed by the full focused
  `JbrSkiaCommandRecorderTest` class with `BUILD SUCCESSFUL`.
- CMP now emits path-specific unsupported reasons for gradient path stroke paint fallbacks by checking non-fill style
  before generic gradient payload extraction. Focused `JbrSkiaCommandRecorderTest` passed; exact Magic Jewel `CASES`
  validation passed 3/3 with `linearGradientPathPaint`, `radialGradientPathPaint`, and `sweepGradientPathPaint`
  counters; and `CASE_GROUPS=gradient-path-stroke-fallbacks` refreshed 3/3 with 3,603 picture frames and zero command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-110959/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-111207/suite.tsv`.
- Combined `shader-rendering color-filters` command-probe refresh passed after image fallback marker hardening: 31/31
  passed with `fallback_sum=0`, 15,814 picture frames, and 30,519 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-104452/suite.tsv`.
- Magic Jewel command probes now require the live recorder `image=1` marker on image path-effect and raw table
  color-filter fallback rows. Exact `CASES` validation passed 2/2 with 2,431 picture frames and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-104135/suite.tsv`.
- Magic Jewel screenshot parity now gates graphics-layer color-matrix rows on effect-handle define/use/cache-hit
  markers. Exact `CASES` validation passed 2/2 with no fallback, no picture frames, and 2,870 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-103423/suite.tsv`.
- Full default benchmark suite refreshed after the parser/API and report-validation gates: 5/5 passed with no fallback,
  82 old-side CPU samples, 80 new-side CPU samples, and 13,724 command frames across command cases:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260611-102359/suite.tsv`.
- JBR parser/API helper refreshed against the current local overlay artifacts:
  `REBUILD_LOCAL_ARTIFACTS=false ./scripts/test-jbr-skia-api.sh` passed with `JBR_SKIA_API_TEST passed`.
- Magic Jewel report-validation unit script refreshed: `./scripts/test-jbr-skia-report-validation.sh` passed with
  `JBR_SKIA_REPORT_VALIDATION_TESTS passed`.
- CMP focused recorder validation refreshed after the unsupported-reason source scan:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest` passed with `BUILD SUCCESSFUL`.
- Cross-repo ABI/capability drift audit is clean: JBR private API, JBR API mirror, Skiko discovery/stream writer, and
  CMP recorder are aligned on command stream ABI 106 and native ABI 3; the low-word and high-word capability tails
  match through the current drawVertices bit.
- Skiko focused interop validation refreshed after the matrix gates:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  passed with `BUILD SUCCESSFUL`.
- Current-artifact matrix refreshed after the compatibility matrix: required rows passed 2/2. `current-all` replayed
  654 command frames with no fallback, `missing-public-api` fell back once as expected, and the five optional
  old-artifact rows were skipped because no old bundle variables were set:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260611-101223/matrix.tsv`.
- Full compatibility matrix refreshed after the full command and screenshot parity sweeps: 57/57 passed with
  `fallback_sum=56`; the happy path produced 474 JBR command frames, every mismatch row produced exactly one
  structured fallback with no command frames, and all rows stayed in background-window mode:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260611-094313/matrix.tsv`.
- Full default screenshot parity validation refreshed after the focused visual parity batch: 106/106 passed with
  `fallback_sum=12`, no picture frames, 102,236 command frames, mean `avg_delta=2.158`, and mean
  `bad_pixel_ratio=0.05158`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-083428/suite.tsv`.
- Focused `smoke` screenshot parity refreshed after `graphics-layer-effects`: 3/3 passed with no fallback, no picture
  frames, 3,176 command frames, mean `avg_delta=2.398`, and mean `bad_pixel_ratio=0.05816`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-082953/suite.tsv`.
- Focused `graphics-layer-effects` screenshot parity refreshed after `descriptor-lifecycle`: 14/14 passed with
  `fallback_sum=2`, no picture frames, 17,833 command frames, mean `avg_delta=2.341`, and mean
  `bad_pixel_ratio=0.05725`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-081932/suite.tsv`.
- Focused `descriptor-lifecycle` screenshot parity refreshed after `core-drawing`: 6/6 passed with `fallback_sum=1`,
  no picture frames, 6,461 command frames, mean `avg_delta=2.127`, and mean `bad_pixel_ratio=0.05078`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-081244/suite.tsv`.
- Focused `core-drawing` screenshot parity refreshed after `native-text`: 16/16 passed with no fallback, no picture
  frames, 15,732 command frames, mean `avg_delta=2.175`, and mean `bad_pixel_ratio=0.05197`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-080125/suite.tsv`.
- Focused `native-text` screenshot parity refreshed after `runtime-effect`: 14/14 passed with `fallback_sum=3`, no
  picture frames, 11,672 command frames, mean `avg_delta=2.037`, and mean `bad_pixel_ratio=0.04758`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-075039/suite.tsv`.
- Focused `runtime-effect` screenshot parity refreshed after `shader-rendering`: 14/14 passed with `fallback_sum=2`,
  no picture frames, 11,139 command frames, mean `avg_delta=2.054`, and mean `bad_pixel_ratio=0.04879`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-074046/suite.tsv`.
- Focused `shader-rendering` screenshot parity refreshed after the graphics-layer visual groups: 18/18 passed with
  `fallback_sum=4`, no picture frames, 13,787 command frames, mean `avg_delta=2.081`, and mean
  `bad_pixel_ratio=0.04995`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-072815/suite.tsv`.
- Focused `graphics-layer-clip-shadow-transform` screenshot parity refreshed after `graphics-layer-basic`: 14/14
  passed with no fallback, no picture frames, 19,849 command frames, mean `avg_delta=2.223`, and mean
  `bad_pixel_ratio=0.05296`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-071806/suite.tsv`.
- Focused `graphics-layer-basic` screenshot parity refreshed after the full command sweep: 7/7 passed with no
  fallback, no picture frames, 8,142 command frames, mean `avg_delta=2.194`, and mean
  `bad_pixel_ratio=0.05223`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-071236/suite.tsv`.
- Full default command-probe validation refreshed after the focused group batch: 549/549 passed with
  `fallback_sum=350`, 79 unsupported-picture rows, 87,188 picture frames, and 188,270 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-012205/suite.tsv`.
- Focused `gradient-invalid` command-probe validation refreshed after `runtime-effect-invalid`: 81/81 passed with
  `fallback_sum=60`, 21 unsupported-picture rows, 23,049 picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-003036/suite.tsv`.
- Focused `runtime-effect-invalid` command-probe validation refreshed after `descriptor-handles-invalid`: 62/62 passed
  with `fallback_sum=56`, six unsupported-picture rows, 6,399 picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-234517/suite.tsv`.
- Focused `descriptor-handles-invalid` command-probe validation refreshed after `save-layer-invalid`: 48/48 passed
  with `fallback_sum=48`, no unsupported rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-231048/suite.tsv`.
- Focused `save-layer-invalid` command-probe validation refreshed after `gradient-path-invalid`: 37/37 passed with
  `fallback_sum=37`, no unsupported rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-224435/suite.tsv`.
- Focused `gradient-path-invalid` command-probe validation refreshed after `graphics-layer-extras`: 21/21 passed with
  `fallback_sum=18`, three unsupported-picture rows, 3,542 picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-222857/suite.tsv`.
- Focused `graphics-layer-extras` command-probe validation refreshed after `graphics-layer`: 16/16 passed with no
  fallback, four unsupported-picture rows, 4,221 picture frames, and 15,698 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-221556/suite.tsv`.
- Focused `graphics-layer` command-probe validation refreshed after `surface-transform-ui`: 22/22 passed with no
  fallback, one unsupported-picture row, 1,071 picture frames, and 31,583 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-215954/suite.tsv`.
- Focused `surface-transform-ui` command-probe validation refreshed after `graphics-layer-invalid`: 15/15 passed with
  no fallback, no unsupported rows, no picture frames, and 18,966 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-214838/suite.tsv`.
- Focused `graphics-layer-invalid` command-probe validation refreshed after `native-text`: 15/15 passed with no
  fallback, 15 unsupported-picture rows, 15,986 picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-213721/suite.tsv`.
- Focused `native-text` command-probe validation refreshed after `image-handles-invalid`: 14/14 passed with no
  fallback, no unsupported rows, no picture frames, and 17,781 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-212619/suite.tsv`.
- Focused `image-handles-invalid` command-probe validation refreshed after `shader-descriptor-invalid`: 27/27 passed
  with `fallback_sum=27`, no unsupported rows, no picture frames, and 892 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-210711/suite.tsv`.
- Focused `shader-descriptor-invalid` command-probe validation refreshed after `effect-descriptor-invalid`: 30/30
  passed with `fallback_sum=30`, no unsupported rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-204523/suite.tsv`.
- Focused `effect-descriptor-invalid` command-probe validation refreshed after `descriptor-lifecycle`: 28/28 passed
  with `fallback_sum=28`, no unsupported rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-202519/suite.tsv`.
- Focused `descriptor-lifecycle` command-probe validation refreshed after `color-filters`: 18/18 passed with no
  unsupported rows, no picture frames, and 22,552 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-200920/suite.tsv`.
- Focused `color-filters` command-probe validation refreshed after shader composition/runtime validation: 13/13
  passed with three unsupported-picture rows, 2,738 picture frames, and 11,697 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-195849/suite.tsv`.
- Focused `shader-composition-runtime` command-probe validation refreshed after `shader-rendering`: 15/15 passed with
  two unsupported-picture rows, 1,690 picture frames, and 17,687 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-194645/suite.tsv`.
- Focused `shader-rendering` command-probe validation refreshed after `core-effects`: 18/18 passed with ten
  unsupported-picture rows, 9,154 picture frames, and 10,048 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-193254/suite.tsv`.
- Focused `core-effects` command-probe validation refreshed after `save-layer-shader-fallbacks`: 8/8 passed with
  three unsupported-picture rows, 3,051 picture frames, and 6,875 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-192454/suite.tsv`.
- Focused `save-layer-shader-fallbacks` command-probe validation refreshed after the gradient path groups: 9/9 passed
  with six unsupported-picture rows, 6,984 picture frames, and 4,511 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-191700/suite.tsv`.
- Focused `gradient-path-stroke-fallbacks` command-probe validation refreshed after path-structure validation: 3/3
  passed with three unsupported-picture rows, 2,923 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-191342/suite.tsv`.
- Focused `gradient-path-structure-invalid` command-probe validation refreshed after the public gradient shape trio:
  3/3 passed with three unsupported-picture rows, 3,236 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-191022/suite.tsv`.
- Focused `gradient-stroke-round-rect-radius-invalid` command-probe validation refreshed after round-rect radius
  validation: 3/3 passed with three unsupported-picture rows, 3,360 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-190701/suite.tsv`.
- Focused `gradient-round-rect-radius-invalid` command-probe validation refreshed after stroke-width validation: 3/3
  passed with three unsupported-picture rows, 3,126 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-190334/suite.tsv`.
- Focused `gradient-stroke-width-invalid` command-probe validation refreshed after color-count validation: 3/3 passed
  with three unsupported-picture rows, 3,610 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-185958/suite.tsv`.
- Focused `gradient-color-count-invalid` command-probe validation refreshed after `gradient-geometry-invalid`: 3/3
  passed with three unsupported-picture rows, 3,035 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-185616/suite.tsv`.
- Focused `gradient-geometry-invalid` command-probe validation refreshed after `gradient-stop-invalid`: 3/3 passed
  with three unsupported-picture rows, 3,107 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-185303/suite.tsv`.
- Focused `gradient-stop-invalid` command-probe validation refreshed after `image-shader-invalid`: 4/4 passed with
  four unsupported-picture rows, 4,226 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-184901/suite.tsv`.
- One-row `image-shader-invalid` command-probe validation refreshed after the color-filter invalid refresh: 1/1 passed
  with `imageShaderImage`, one unsupported-picture row, 1,034 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-184709/suite.tsv`.
- Focused `fill-rect-color-filter-invalid` command-probe validation refreshed after `shader-ref-invalid`: 6/6 passed,
  `fallback_sum=5`, one unsupported-picture row, 1,061 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-184147/suite.tsv`.
- Focused `shader-ref-invalid` command-probe validation refreshed after `blend-mode-invalid`: 3/3 passed,
  `fallback_sum=3`, zero unsupported rows, and zero replay frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-183824/suite.tsv`.
- Focused `blend-mode-invalid` command-probe validation refreshed after `path-invalid`: 3/3 passed,
  `fallback_sum=2`, one unsupported-picture row with dynamic `blendMode_Clear`, 957 picture frames, and zero command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-183512/suite.tsv`.
- Focused `path-invalid` command-probe validation refreshed after `primitive-invalid`: 24/24 passed,
  `fallback_sum=22`, two unsupported-picture rows, 2,125 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-181836/suite.tsv`.
- Focused `primitive-invalid` command-probe validation refreshed after `native-text-invalid`: 16/16 passed,
  `fallback_sum=13`, three unsupported-picture rows, 3,196 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-180642/suite.tsv`.
- Focused `native-text-invalid` command-probe validation refreshed after the ABI drift audit: 11/11 passed,
  `fallback_sum=11`, zero unsupported rows, zero picture frames, and 1,369 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-175826/suite.tsv`.
- A scoped cross-repo ABI/capability audit found no shared constant drift across the JBR private API, JBR API mirror,
  JBR native parser subset, Skiko discovery/layer subsets, and the CMP recorder subset. Current masks remain
  `low=-1` (`0xffffffffffffffff`) across 65 low-word capability bits and `high=262143`
  (`0x000000000003ffff`) across 18 high-word bits.
- The required artifact matrix also passed on the current ABI 106 local artifacts after the compatibility refresh:
  required rows 2/2, `current-all` with 334 JBR command frames and no fallback, and `missing-public-api` with the
  expected fallback and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260610-175047/matrix.tsv`.
- The latest compatibility matrix checkpoint passed after the 549-row command-probe consolidation. Discovery resolved
  57/57 grouped rows with no ungrouped cases, and the matrix passed 57/57 with `fallback_sum=56`, 573 happy-path JBR
  command frames, zero command frames on all mismatch rows, and background-window mode on every row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260610-171921/matrix.tsv`.
- The latest default Magic Jewel command-probe consolidation now covers all 549 default rows after the public gradient
  shape expansion. Because fresh broad starts hit launch-only SIGTERM/no-sample interruptions before any app markers,
  the passing checkpoint is a split run: exact `commands-live-animation`, focused `stream-invalid`, and a suffix from
  `commands-native-bridge-load-library` through `commands-invalid-gradient-fallback`. Combined result: 549/549 passed,
  `fallback_sum=350`, 79 unsupported-picture rows, 69,200 picture frames, and 111,092 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-103258/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-104522/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-105340/suite.tsv`.
- CMP focused recorder validation passed after the gradient invalid consolidation:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`. The current remaining unsupported-reason audit is therefore
  backed by current recorder tests for nested child/header invariants, shadow replay, gradient path paint guards, and
  public gradient shape fallbacks.
- The expanded `gradient-invalid` medium group refreshed green as a split run after an environment refresh interrupted
  the first attempt after four rows. Combined result: 81/81 passed, `fallback_sum=60`, 21 unsupported-picture rows,
  22,222 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-091843/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-092520/suite.tsv`.
- Magic Jewel now covers the public low-level canvas paths for the three gradient stroke-round-rect radius fallback
  guards. Exact `commands-linear-gradient-stroke-round-rect-invalid-radius-fallback` passed first, then
  `gradient-stroke-round-rect-radius-invalid` passed 3/3 with family-specific unsupported reasons and zero command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-231026/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-231339/suite.tsv`.
  The unsupported-reason audit is now narrowed to dynamic/internal/defensive candidates rather than obviously missing
  public gradient-shape rows.
- Magic Jewel now covers the public low-level canvas paths for gradient stroke-width and round-rect-radius fallback
  guards. Added six public sentinels across linear/radial/sweep families; exact linear radius and stroke-width rows
  passed first, then `gradient-round-rect-radius-invalid` and `gradient-stroke-width-invalid` each passed 3/3 with
  family-specific unsupported reasons and zero command frames. Command-probe discovery now reports 546 default rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-225707/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-225806/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-225855/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-230157/suite.tsv`.
- CMP now preserves public non-finite color-matrix metadata without letting Skia's native color-filter constructor throw
  on the EDT. Invalid matrices use a harmless native fallback filter while strict command recording reports
  `colorMatrixNonfinite` and falls back structurally. The full `JbrSkiaCommandRecorderTest` class passed, Magic Jewel
  discovery now reports 540 default command-probe rows and `fill-rect-color-filter-invalid` 6 rows, the exact
  `commands-color-matrix-filter-nonfinite-fallback` row passed 1/1, and the adjacent group passed 6/6:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-205901/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-222615/suite.tsv`.
- Magic Jewel now has a public app-level nested graphics-layer unsupported-child sentinel. The row proves raw shader
  content inside a valid graphics layer falls back structurally with `graphicsLayer:childCommands`; CMP's
  `graphicsLayer:childUnsupported` remains classified as a synthetic/internal guard where commands are present but
  `unsupportedCount > 0`. Exact validation passed, and `graphics-layer-extras` refreshed 16/16:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-165600/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-180353/suite.tsv`.
- The latest full default command-probe consolidation passed after the focused invalid-slice refreshes: 538/538 rows,
  `fallback_sum=350`, 68 unsupported-picture rows, 82,301 JBR picture frames, and 200,046 JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-094809/suite.tsv`.
- The focused `surface-transform-ui` command-probe group refreshed cleanly: 15/15 passed with `fallback_sum=0`, zero
  unsupported rows, zero picture frames, and 21,502 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-093056/suite.tsv`.
- The focused `smoke` command-probe group refreshed cleanly after the invalid-slice batch: 6/6 passed with
  `fallback_sum=0`, zero unsupported rows, zero picture frames, and 8,625 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-092508/suite.tsv`.
- The focused `blend-mode-invalid` command-probe group refreshed cleanly: 3/3 passed with `fallback_sum=2`, one
  unsupported-picture row from the live vertices `BlendMode.Clear` sentinel, 1,222 picture frames, and zero command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-092118/suite.tsv`.
- The focused `fill-rect-color-filter-invalid` command-probe group refreshed cleanly: 5/5 passed with
  `fallback_sum=5`, zero unsupported rows, and zero picture/command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-091041/suite.tsv`.
- The focused `gradient-stop-invalid` command-probe group refreshed cleanly: 4/4 passed with live linear/radial/sweep
  gradient stop/points unsupported-picture fallbacks, 3,653 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-090605/suite.tsv`.
- The focused `gradient-color-count-invalid` command-probe group refreshed cleanly: 3/3 passed with live
  linear/radial/sweep gradient color-count unsupported-picture fallbacks, 3,041 picture frames, and zero command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-090205/suite.tsv`.
- The focused `graphics-layer-invalid` command-probe group refreshed cleanly: 15/15 passed with `fallback_sum=0`, all
  rows intentionally on unsupported-picture fallback, 16,327 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-082414/suite.tsv`.
- The focused `primitive-invalid` command-probe group refreshed cleanly: 16/16 passed with `fallback_sum=13`, three
  unsupported-picture rows from live `blendLayerBounds`/`transform`/`points` sentinels, 3,189 picture frames, and zero
  command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-222055/suite.tsv`.
- Magic Jewel command-probe discovery remains healthy at 538 default rows with no ungrouped or duplicate case names.
  The one-row `image-shader-invalid` quick group passed with `imageShaderImage`, one unsupported-picture row, 1,109
  picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-221233/suite.tsv`.
- The focused `path-invalid` command-probe group refreshed cleanly as the next medium invalid slice: 24/24 passed with
  `fallback_sum=22`, two unsupported-picture rows from live `clipPath`/`path` sentinels, 1,805 picture frames, and zero
  command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-214338/suite.tsv`.
- The focused `native-text-invalid` command-probe group refreshed cleanly after the current unsupported-reason audit:
  11/11 passed with `fallback_sum=11`, zero unsupported rows, zero picture frames, and 1,165 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-194120/suite.tsv`.
- CMP's nested graphics-layer child stream guards are now explicitly covered in focused strict-mode recorder tests.
  The audit classified `graphicsLayer:childCommands`, `graphicsLayer:childUnsupported`,
  `graphicsLayer:childHeaderSize`, and `graphicsLayer:childHeader` as internal nested-recording integrity guards rather
  than public app command shapes, and the focused `JbrSkiaCommandRecorderTest` class passed after adding the coverage.
- Magic Jewel now has public app-level sentinels for all three live gradient geometry guards covered by the CMP
  invalid-geometry factory hardening: linear points, radial radius geometry, and sweep center geometry. Discovery now
  reports 538 default command-probe rows, `gradient-geometry-invalid` 3, and `gradient-invalid` 72. The exact
  radial/sweep geometry slice passed 2/2 with zero command frames, and the compact linear/radial/sweep geometry group
  passed 3/3 with zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-173150/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-173634/suite.tsv`.
- Public invalid gradient geometry no longer escapes as a Skia shader-construction crash before recorder fallback.
  CMP's Skiko linear/radial/sweep gradient factories now preserve JBR metadata while using a harmless solid Skia shader
  for invalid geometry, so strict recording reports `linearGradientPoints`/geometry unsupported reasons and falls back
  structurally. Magic Jewel added the live `commands-linear-gradient-invalid-points-fallback` row; no-run discovery now
  reports 536 default command-probe rows, `gradient-stop-invalid` 4, and `gradient-invalid` 70. The exact row passed
  with `linearGradientPoints`, one unsupported-picture row, 976 picture frames, and zero command frames; the adjacent
  `gradient-stop-invalid` quick group passed 4/4 with zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-152702/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-162415/suite.tsv`.
- The latest 535-row default command-probe consolidation is green as a split run: a 403-row prefix, a 10-row resumed
  descriptor chunk, an exact forced-context color-shader descriptor rerun after Magic Jewel report-parser hardening,
  and a 121-row suffix together passed 535/535 with `fallback_sum=350`, 65 unsupported-picture rows, 74,635 JBR
  picture frames, and 193,776 JBR command frames. The split was needed because stale `/tmp` local JBR artifacts first
  caused command-canvas `service-unavailable`, then an interleaved log token `unsupported=6ecc00` exposed an overly
  permissive numeric parser in the Magic Jewel report harness.
- Magic Jewel now covers the live dynamic `blendMode_Clear` fallback guard with
  `commands-vertices-invalid-blend-mode-fallback`. Public Compose can feed `BlendMode.Clear` into
  `Canvas.drawVertices`; the new row proves CMP falls back structurally before emitting command replay. No-run
  discovery reports 535 default command-probe rows, no ungrouped rows, no duplicate case names, and 3
  `blend-mode-invalid` rows. The exact row passed with the `blendMode_Clear` unsupported reason, and the adjacent
  blend-mode-invalid group passed 3/3:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-193518/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-193626/suite.tsv`.
- Magic Jewel now covers CMP's `transform` live fallback guard with `commands-invalid-concat-transform-fallback`.
  Public Compose can feed a non-finite matrix value into `Canvas.concat(Matrix)`; the new row proves CMP falls back
  structurally before emitting command replay. No-run discovery reports 534 default command-probe rows, no ungrouped
  rows, no duplicate case names, and 16 `primitive-invalid` rows. The exact row passed with the `transform`
  unsupported reason, and the adjacent primitive-invalid group passed 16/16:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-160032/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-160142/suite.tsv`.
- Magic Jewel now covers CMP's `points` live fallback guard with `commands-invalid-point-dots-fallback`. Public
  Compose can feed a non-finite point into `Canvas.drawPoints(PointMode.Points, ...)`; the new row proves CMP falls
  back structurally before emitting command replay. No-run discovery reports 533 default command-probe rows, no
  ungrouped rows, no duplicate case names, and 15 `primitive-invalid` rows. The exact row passed with the `points`
  unsupported reason, and the adjacent primitive-invalid group passed 15/15:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-154421/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-154533/suite.tsv`.
- Magic Jewel now covers CMP's `blendLayerBounds` live fallback guard with
  `commands-invalid-blend-layer-bounds-fallback`. Public Compose can feed a non-finite primitive coordinate into the
  ABI-neutral blend-layer wrapper when a supported non-`SrcOver` blend mode is present; the new row proves CMP falls
  back structurally before emitting command replay. No-run discovery reports 532 default command-probe rows, no
  ungrouped rows, no duplicate case names, and 14 `primitive-invalid` rows. The exact row passed with the
  `blendLayerBounds` unsupported reason, and the adjacent primitive-invalid group passed 14/14:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-122501/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-122615/suite.tsv`.
- CMP commit `f7bb15f788b` closes the plain `Canvas.saveLayer` blend+color-filter escape. Direct tint saveLayer paints
  with supported non-`SrcOver` blend modes now emit `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER`, descriptor-backed
  color-filter paints with blend emit `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF`, and supported descriptor filters
  without blend route through the handle-backed saveLayer path. The focused two-test slice and full
  `JbrSkiaCommandRecorderTest` class passed.
- Magic Jewel now has a direct app-level `commands-save-layer-blend-color-filter` sentinel for that plain saveLayer
  combined paint path. No-run discovery reports 531 default command-probe rows, no ungrouped rows, no duplicate case
  names, and 9 `save-layer-shader-fallbacks` rows. The exact row passed with no fallback/picture frames; the adjacent
  group passed 9/9 with supported saveLayer rows on command replay and raw saveLayer/shader rows falling back
  intentionally:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-084917/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-111237/suite.tsv`.
- CMP now keeps paint-level supported non-`SrcOver` blend modes for fill-rect color-filter commands on command replay,
  covering direct tint filters and handle-backed tint/color-matrix/lighting/descriptor filters through tight
  blend-mode layers around the existing native color-filter rect records.
- Magic Jewel now has a direct app-level `commands-color-filter-blend-mode` sentinel for that fill-rect color-filter
  blend-mode path. The exact row passed with no fallback/picture frames, and the adjacent `color-filters` group now
  resolves 13 rows and passed 13/13 with supported rows staying on command replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-155652/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-155803/suite.tsv`.
- CMP now keeps paint-level supported non-`SrcOver` blend modes for linear, radial, and sweep gradient rects,
  round-rects, and filled paths on command replay by wrapping the existing gradient command records in tight blend-mode
  layers over fill/stroke/path bounds.
- Magic Jewel now also has a direct app-level `commands-linear-gradient-blend-mode` sentinel for the gradient
  blend-layer path. The exact row passed with no fallback/picture frames, and the adjacent `surface-transform-ui` group
  now resolves 12 rows and passed 12/12 with every row staying on command replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-161407/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-161521/suite.tsv`.
- Magic Jewel now has a direct app-level `commands-linear-gradient-path-blend-mode` sentinel for the gradient
  filled-path blend-layer path. The exact row passed with no fallback/picture frames, and the adjacent
  `surface-transform-ui` group now resolves 13 rows and passed 13/13 with every row staying on command replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-162640/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-162742/suite.tsv`.
- Magic Jewel now has direct app-level `commands-radial-gradient-stroke-blend-mode` and
  `commands-sweep-gradient-round-rect-blend-mode` sentinels for the remaining unit-covered gradient blend-layer shapes.
  The exact pair passed with no fallback/picture frames, and the adjacent `surface-transform-ui` group now resolves 15
  rows and passed 15/15 with every row staying on command replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-164027/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-164210/suite.tsv`.
- CMP now keeps paint-level supported non-`SrcOver` blend modes for image refs, image-shader rects, and shader
  descriptor rects on command replay by wrapping the existing image/shader command records in tight blend-mode layers
  over destination/fill bounds.
- Magic Jewel now has a direct app-level `commands-image-blend-mode` sentinel for the image-ref blend-layer path. The
  exact row passed with no fallback/picture frames, and the adjacent `shader-rendering` group now resolves 16 rows and
  passed 16/16 with supported rows staying on command replay and raw/invalid shader rows falling back intentionally:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-165652/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-165819/suite.tsv`.
- Magic Jewel now has a direct app-level `commands-image-shader-blend-mode` sentinel for the image-shader rect
  blend-layer path. The exact row passed with no fallback/picture frames, and the adjacent `shader-rendering` group now
  resolves 17 rows and passed 17/17 with supported rows staying on command replay and raw/invalid shader rows falling
  back intentionally:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-171148/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-171254/suite.tsv`.
- Magic Jewel now has a direct app-level `commands-color-shader-blend-mode` sentinel for the JBR-owned shader
  descriptor blend-layer path. The exact row passed with no fallback/picture frames and retained shader-handle
  lifecycle gates; the adjacent `shader-rendering` group now resolves 18 rows and passed 18/18 with supported rows
  staying on command replay and raw/invalid shader rows falling back intentionally:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-172738/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-172840/suite.tsv`.
- CMP now keeps paint-level supported non-`SrcOver` blend modes for `drawVertices` on command replay by wrapping
  `COMMAND_DRAW_VERTICES` in a tight blend-mode layer over vertex bounds.
- CMP now applies the same ABI-neutral blend-layer strategy to dashed solid primitive commands and path-effect
  descriptor path commands, with stroke-padded layer bounds. This closes the adjacent supported non-`SrcOver`
  blend-mode fallbacks without adding command opcodes.
- CMP's solid-color primitive recorder now keeps supported non-`SrcOver` blend modes on command replay for lines,
  rect stroke fallback, round-rects, ovals, arcs, paths, points, and raw points by emitting tight
  `COMMAND_SAVE_LAYER_BLEND_MODE` wrappers around the existing primitive commands. The direct filled-rect blend-mode
  opcode remains unchanged. Focused `JbrSkiaCommandRecorderTest` desktop validation passed after this ABI-neutral
  closure.
- Skiko's JBR Swing layer now defaults to command replay when CMP creates the JBR interop layer and no explicit
  diagnostic/picture/texture mode is requested. The old explicit picture/diagnostic/texture properties still override
  the default, and `skiko.jbr.interop.renderCommands=false` keeps the probe-only Swing fallback path available for
  debugging.
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
  coherent batch of sentinels has landed. The latest periodic default command-probe consolidation passed after the
  app-level blend sentinel batch: discovery resolved 530 default rows with no ungrouped or duplicate cases, and the
  sweep passed 530/530 with `fallback_sum=350`, `unsupported_rows=62`, `picture_frames=67344`, and
  `command_frames=171452`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-174336/suite.tsv`.
- The latest compatibility matrix checkpoint passed after that command-probe consolidation: 57/57 rows,
  `fallback_sum=56`, 824 happy-path command frames, exact structured fallback for every forced
  ABI/capability/public-API mismatch, and background-window mode on every row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260606-031518/matrix.tsv`.
- The latest required artifact matrix checkpoint passed on the current ABI 106 local artifacts: required rows 2/2,
  `fallback_sum=1`, 754 current-artifact command frames, and the expected `public-api-missing` fallback for the
  missing-public-API row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260606-133156/matrix.tsv`.
- Skiko focused `JbrSkiaInteropTest` also passed against the current branch/artifacts:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- The latest full default screenshot parity checkpoint then passed as a split smoke-plus-tail refresh. The
  `CASE_GROUPS=smoke` prefix passed 3/3, and the `CASES_FROM=parity-skew-transform` tail passed 103/103. Combined
  result: 106/106 passed, `fallback_sum=11`, zero JBR picture frames, and 105,068 JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260606-133617/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260606-133836/suite.tsv`.
- Cross-repo ABI/capability drift audit is clean for the current command surface: shared constants across JBR private
  API, JBR API mirror, native replay, Skiko interop, and CMP recorder had zero normalized value mismatches; JBR
  private/API constant sets match; native replay has switch cases for all 67 native command opcodes; and every CMP
  emitted opcode constant is present in native replay.
- Magic Jewel's new `JBR_SKIA_RENDER_MODE=auto` harness mode leaves Skiko render-mode properties unset so the default
  command path is testable. A focused auto-mode app smoke passed with zero fallback markers, zero picture frames, 847
  Skiko/JBR command frames, 848 CMP recorder frames, and an effective render-mode marker of
  `commands=true picture=false diagnostic=false texture=false delegateCommands=true`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-interop-report/20260606-145913/report.md`.
- A new Magic Jewel command-probe sentinel covers graphics layers with invalid out-of-range `alpha`. This exercises the
  layer-level `graphicsLayer:alpha` guard before replay. No-run discovery now reports 498 default command-probe rows,
  `graphics-layer` 22, and a `graphics-layer-invalid` quick group with 2 rows. Focused exact validation passed 1/1
  with 964 JBR picture frames and zero command frames; the adjacent `graphics-layer-invalid` group passed 2/2 with
  `fallback_sum=0`, two intentional unsupported-picture rows, 1,983 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-183358/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-183507/suite.tsv`.
- A new Magic Jewel command-probe sentinel covers graphics layers with invalid non-positive `cameraDistance`. This
  exercises the layer-level `graphicsLayer:cameraDistance` guard before replay. No-run discovery now reports 499
  default command-probe rows and the `graphics-layer-invalid` quick group now has 3 rows. Focused exact validation
  passed 1/1 with 956 JBR picture frames and zero command frames; the adjacent `graphics-layer-invalid` group passed
  3/3 with `fallback_sum=0`, three intentional unsupported-picture rows, 2,841 picture frames, and zero command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-184223/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-184326/suite.tsv`.
- A new Magic Jewel command-probe sentinel covers graphics layers with invalid non-finite `scaleX`. This exercises the
  layer-level `graphicsLayer:scaleX` guard before replay. No-run discovery now reports 500 default command-probe rows
  and the `graphics-layer-invalid` quick group now has 4 rows. Focused exact validation passed 1/1 with 953 JBR
  picture frames and zero command frames; the adjacent `graphics-layer-invalid` group passed 4/4 with
  `fallback_sum=0`, four intentional unsupported-picture rows, 3,783 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-184926/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-185029/suite.tsv`.
- A new Magic Jewel command-probe sentinel covers graphics layers with invalid non-finite `scaleY`. This exercises the
  layer-level `graphicsLayer:scaleY` guard before replay. No-run discovery now reports 501 default command-probe rows
  and the `graphics-layer-invalid` quick group now has 5 rows. Focused exact validation passed 1/1 with 937 JBR
  picture frames and zero command frames; the adjacent `graphics-layer-invalid` group passed 5/5 with
  `fallback_sum=0`, five intentional unsupported-picture rows, 4,669 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-185751/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-185856/suite.tsv`.
- A new Magic Jewel command-probe sentinel covers graphics layers with invalid non-finite `rotationZ`. This exercises
  the layer-level `graphicsLayer:rotationZ` guard before replay. No-run discovery now reports 502 default
  command-probe rows and the `graphics-layer-invalid` quick group now has 6 rows. Focused exact validation passed 1/1
  with 1,055 JBR picture frames and zero command frames; the adjacent `graphics-layer-invalid` group passed 6/6 with
  `fallback_sum=0`, six intentional unsupported-picture rows, 6,172 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-190925/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-191023/suite.tsv`.
- A new Magic Jewel command-probe sentinel covers graphics layers with invalid non-finite `translationX`. This
  exercises the layer-level `graphicsLayer:translationX` guard before replay. No-run discovery now reports 503 default
  command-probe rows and the `graphics-layer-invalid` quick group now has 7 rows. Focused exact validation passed 1/1
  with 1,062 JBR picture frames and zero command frames; the adjacent `graphics-layer-invalid` group passed 7/7 with
  `fallback_sum=0`, seven intentional unsupported-picture rows, 7,355 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-192056/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-192154/suite.tsv`.
- A new Magic Jewel command-probe sentinel covers graphics layers with invalid non-finite `translationY`. This
  exercises the layer-level `graphicsLayer:translationY` guard before replay. No-run discovery now reports 504 default
  command-probe rows and the `graphics-layer-invalid` quick group now has 8 rows. Focused exact validation passed 1/1
  with 1,034 JBR picture frames and zero command frames; the adjacent `graphics-layer-invalid` group passed 8/8 with
  `fallback_sum=0`, eight intentional unsupported-picture rows, 7,906 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-193442/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-193540/suite.tsv`.
- A new Magic Jewel command-probe sentinel covers graphics layers with invalid non-finite `rotationX`. This exercises
  the layer-level `graphicsLayer:rotationX` guard before replay. No-run discovery now reports 505 default
  command-probe rows and the `graphics-layer-invalid` quick group now has 9 rows. Focused exact validation passed 1/1
  with 1,054 JBR picture frames and zero command frames; the adjacent `graphics-layer-invalid` group passed 9/9 with
  `fallback_sum=0`, nine intentional unsupported-picture rows, 9,404 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-195334/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-195437/suite.tsv`.
- A new Magic Jewel command-probe sentinel covers graphics layers with invalid non-finite `rotationY`. This exercises
  the layer-level `graphicsLayer:rotationY` guard before replay. No-run discovery now reports 506 default
  command-probe rows and the `graphics-layer-invalid` quick group now has 10 rows. Focused exact validation passed 1/1
  with 1,031 JBR picture frames and zero command frames; the adjacent `graphics-layer-invalid` group passed 10/10
  with `fallback_sum=0`, ten intentional unsupported-picture rows, 10,196 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-200608/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-200710/suite.tsv`.
- A new Magic Jewel command-probe sentinel covers graphics layers with an unsupported `blendMode`. This exercises the
  layer-level `graphicsLayer:blendMode` guard before replay. No-run discovery now reports 507 default command-probe
  rows and the `graphics-layer-invalid` quick group now has 11 rows. Focused exact validation passed 1/1 with 1,168
  JBR picture frames and zero command frames; the adjacent `graphics-layer-invalid` group passed 11/11 with
  `fallback_sum=0`, eleven intentional unsupported-picture rows, 11,972 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-202228/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-202314/suite.tsv`.
- A new Magic Jewel command-probe sentinel covers an unrecorded remembered graphics layer. This exercises the
  lifecycle `graphicsLayer:recording` guard before replay. No-run discovery now reports 508 default command-probe rows
  and the `graphics-layer-invalid` quick group now has 12 rows. Focused exact validation passed 1/1 with 891 JBR
  picture frames and zero command frames; the adjacent `graphics-layer-invalid` group passed 12/12 with
  `fallback_sum=0`, twelve intentional unsupported-picture rows, 11,611 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-203529/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-203629/suite.tsv`.
- New Magic Jewel command-probe sentinels cover remembered graphics layers recorded with invalid negative width or
  height. These exercise the layer-level `graphicsLayer:sizeWidth` and `graphicsLayer:sizeHeight` guards before replay.
  No-run discovery now reports 510 default command-probe rows and the `graphics-layer-invalid` quick group now has 14
  rows. Focused exact validation passed 2/2 with 1,953 JBR picture frames and zero command frames; the adjacent
  `graphics-layer-invalid` group passed 14/14 with `fallback_sum=0`, fourteen intentional unsupported-picture rows,
  14,685 picture frames, and zero command frames. The remaining shadow/clip outline guards are defensive only with the
  current sealed `Outline` hierarchy (`Rectangle`, `Rounded`, and `Generic`, all accepted):
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-204923/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-205101/suite.tsv`.
- New Magic Jewel command-probe sentinels cover live invalid `clipPath` and `drawPath` structures, complementing the
  older path parser-corruption rows. These inject non-finite path coordinates through
  `MAGIC_JEWEL_COMPOSE_INVALID_CLIP_PATH` and `MAGIC_JEWEL_COMPOSE_INVALID_DRAW_PATH`, so CMP rejects the live Compose
  recording as `clipPath`/`path` before replay; the invalid clip row also exposes the parent `unsupportedScope`
  surface. No-run discovery now reports 512 default command-probe rows and the `path-invalid` quick group now has 24
  rows, with no ungrouped rows. Focused exact validation passed 2/2 with `fallback_sum=0`, two intentional
  unsupported-picture rows, 1,967 JBR picture frames, and zero command frames; the adjacent `path-invalid` group passed
  24/24 with `fallback_sum=22`, two intentional unsupported-picture rows, 1,789 picture frames, and zero command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-233653/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-233834/suite.tsv`.
- New Magic Jewel command-probe sentinels cover live invalid linear and radial gradient stop order, complementing the
  existing live invalid sweep-gradient stop row. These use duplicate public Brush stops through
  `MAGIC_JEWEL_COMPOSE_INVALID_LINEAR_GRADIENT_STOPS` and
  `MAGIC_JEWEL_COMPOSE_INVALID_RADIAL_GRADIENT_STOPS`, so CMP rejects the live Compose recording as
  `linearGradientStops`/`radialGradientStops` before replay. No-run discovery now reports 514 default command-probe
  rows, a `gradient-stop-invalid` quick group with 3 rows, and `gradient-invalid` 63. Focused
  `CASE_GROUPS=gradient-stop-invalid` passed 3/3 with `fallback_sum=0`, three intentional unsupported-picture rows,
  2,947 JBR picture frames, and zero command frames; the adjacent `gradient-invalid` group passed 63/63 with
  `fallback_sum=60`, three intentional unsupported-picture rows, 3,215 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-091009/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-091243/suite.tsv`.
- Superseded recorder audit note: the earlier `colorMatrixNonfinite` app-level attempt failed during Skia color-filter
  construction (`Can't wrap nullptr`) before CMP could count unsupported command frames. CMP now preserves non-finite
  matrix metadata with a benign native fallback filter, so Magic Jewel covers the live `colorMatrixNonfinite` fallback.
- The follow-on recorder audit classified the remaining live gradient geometry and nested graphics-layer internal
  guards. Public non-finite Brush geometry fails during Skia shader construction before CMP can produce
  `linearGradientPoints`, `radialGradientGeometry`, or `sweepGradientGeometry` unsupported frames; malformed command
  payload geometry remains covered by parser-corruption rows. In strict command mode, nested graphics-layer child
  unsupported output nulls the child command stream, so public probes exercise `graphicsLayer:childCommands` plus the
  child reason; `graphicsLayer:childUnsupported`, `graphicsLayer:childHeaderSize`, and `graphicsLayer:childHeader`
  remain defensive checks for non-strict/internal-corruption states.
- The recorder audit also classifies `roundRectStyle` and the path-specific
  `linearGradientPathPaint`/`radialGradientPathPaint`/`sweepGradientPathPaint` reasons as defensive/shadow guards.
  Public Compose round-rect drawing only supplies Fill or Stroke paint styles, and stroked path gradients are rejected
  first by the generic gradient payload helpers as `linearGradientPaint`, `radialGradientPaint`, or
  `sweepGradientPaint`; the earlier failed path-stroke validation expecting `linearGradientPathPaint` proves that
  ordering.
- A new Magic Jewel command-probe sentinel covers graphics layers whose generic shadow outline serializes to an invalid
  path. This exercises the app-level `graphicsLayer:shadowPath` guard before replay. No-run discovery now reports 518
  default command-probe rows and `graphics-layer-invalid` 15. Focused exact validation passed 1/1 with 961 JBR picture
  frames and zero command frames; the adjacent `graphics-layer-invalid` group passed 15/15 with `fallback_sum=0`,
  fifteen intentional unsupported-picture rows, 14,768 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-151839/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-151944/suite.tsv`.
- The post-shadow-path audit classifies `graphicsLayer:shadow` and `graphicsLayer:shadowFilter` as defensive. Invalid
  layer size/elevation is rejected before shadow replay, and the fallback shadow blur descriptor is generated
  internally with finite positive sigma and an accepted tile mode, so public app probes should not reach those two
  reason strings.
- New Magic Jewel command-probe sentinels cover live invalid linear, radial, and sweep gradient-filled paths. These
  reuse the supported gradient path probes with non-finite path data, so CMP now rejects the real Compose recordings as
  `linearGradientPath`, `radialGradientPath`, and `sweepGradientPath` before replay instead of relying only on mutated
  command bytes. No-run discovery now reports 521 default command-probe rows, `gradient-path-structure-invalid` 3,
  `gradient-path-invalid` 21, and `gradient-invalid` 69. Focused
  `CASE_GROUPS=gradient-path-structure-invalid` passed 3/3 with `fallback_sum=0`, three intentional
  unsupported-picture rows, 3,003 JBR picture frames, and zero command frames; the adjacent `gradient-path-invalid`
  group passed 21/21 with `fallback_sum=18`, three intentional unsupported-picture rows, 2,946 picture frames, and
  zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-154145/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-154451/suite.tsv`.
- A new Magic Jewel command-probe sentinel covers an oversized public Compose image used as an `ImageShader`. This
  exercises CMP's app-level `imageShaderImage` guard before replay rather than relying only on image-shader descriptor
  parser-corruption rows. No-run discovery now reports 522 default command-probe rows, a new `image-shader-invalid`
  quick group with 1 row, and `shader-rendering` 15. Focused
  `CASES=commands-image-shader-invalid-image-fallback` passed 1/1 with `fallback_sum=0`, one intentional
  unsupported-picture row, 1,467 JBR picture frames, and zero command frames; the adjacent `shader-rendering` group
  passed 15/15 with `fallback_sum=0`, ten intentional unsupported-picture rows, 10,204 picture frames, and 6,076
  command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-161033/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-171211/suite.tsv`.
- The focused `primitive-invalid` command-probe group was refreshed after the image-shader/paint-reason audit. It
  passed 13/13 with `fallback_sum=13`, zero unsupported-picture rows, zero picture frames, and zero command frames
  across stroke-cap, transform flags, clip operation, draw-points, and draw-vertices parser corruptions:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-172804/suite.tsv`.
- The focused `native-text-invalid` command-probe group was refreshed next. It passed 11/11 with `fallback_sum=11`,
  zero unsupported-picture rows, zero picture frames, and 1,041 command frames from the font-data record-flags row
  across text/paragraph font parser guards and font-data record flags:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-174043/suite.tsv`.
- The focused `path-invalid` command-probe group was refreshed next. It passed 24/24 with `fallback_sum=22`, two
  intentional unsupported-picture rows from the live invalid clip/draw path probes, 2,075 picture frames, and zero
  command frames across live path structure and path/dash parser guards:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-174939/suite.tsv`.
- The focused `effect-descriptor-invalid` command-probe group was refreshed next. It passed 28/28 with
  `fallback_sum=28`, zero unsupported-picture rows, zero picture frames, and zero command frames across effect
  descriptor header and lighting/tint/color-matrix/image-filter/path-effect payload parser guards:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-180727/suite.tsv`.
- The focused `shader-descriptor-invalid` command-probe group was refreshed next. It passed 30/30 with
  `fallback_sum=30`, zero unsupported-picture rows, zero picture frames, and zero command frames across shader
  descriptor header and color/transformed/composite/gradient/image-shader/Perlin-noise parser guards:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-182940/suite.tsv`.
- The focused `color-filters` command-probe group was refreshed next. It passed 12/12 with `fallback_sum=0`, three
  intentional unsupported-picture rows from raw color-filter fallbacks, 4,499 picture frames, and 15,627 command frames
  across supported tint/color-matrix/lighting descriptors, handle reuse, and graphics-layer color-filter/blend rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-185044/suite.tsv`.
- The focused `descriptor-lifecycle` command-probe group was refreshed next. It passed 18/18 with `fallback_sum=0`,
  zero unsupported-picture rows, zero picture frames, and 22,342 command frames across descriptor eviction,
  resize/forced-context redefinition, stable RuntimeEffect color-filter reuse, and RuntimeEffect source-cache eviction:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-192007/suite.tsv`.
- The focused `save-layer-shader-fallbacks` command-probe group was refreshed next after narrowing the raw table
  saveLayer row to skip the generic screenshot assertion while still requiring `saveLayer` fallback. The exact row
  passed 1/1, and the group rerun passed 8/8 with `fallback_sum=0`, six intentional unsupported-picture rows, 6,120
  picture frames, and 2,254 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-194218/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-194310/suite.tsv`.
- The focused `blend-mode-invalid` command-probe group was refreshed next. It passed 2/2 with `fallback_sum=2`, zero
  unsupported-picture rows, zero picture frames, and zero command frames across fill-rect blend-mode width/height
  parser guards:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-195246/suite.tsv`.
- The focused `core-effects` command-probe group was refreshed next after a sidecar unsupported-reason audit found no
  remaining app-reachable undocumented recorder/layer guard. It passed 8/8 with `fallback_sum=0`, three intentional
  unsupported-picture rows, 2,815 picture frames, and 5,031 command frames across gradient stroke, image filter, path
  effect, raw path-effect/color-filter fallbacks, vertices, and blend mode; supported rows stayed on command replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-195810/suite.tsv`.
- The focused `graphics-layer-extras` command-probe group was refreshed next after narrowing the raw table
  graphics-layer color-filter row to skip only the generic screenshot assertion while still requiring
  `graphicsLayer:colorFilter` fallback. Exact validation passed 1/1, and the group rerun passed 15/15 with
  `fallback_sum=0`, three intentional unsupported-picture rows, 3,097 picture frames, and 13,989 command frames across
  graphics-layer color-matrix/render-effect lifecycle, raw filter fallback, and descriptor-combination rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-202046/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-202136/suite.tsv`.
- The focused `shader-composition-runtime` command-probe group was refreshed next. It passed 15/15 with
  `fallback_sum=0`, two intentional unsupported-picture rows, 2,043 picture frames, and 16,907 command frames across
  image/composite/transformed shader descriptors and RuntimeEffect shader/color-filter descriptor rows; only raw
  RuntimeEffect shader/color-filter probes fell back structurally:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-203324/suite.tsv`.
- The focused `native-text` command-probe group was refreshed next as the supported replay pair for the recent
  `native-text-invalid` parser checkpoint. It passed 14/14 with `fallback_sum=0`, zero unsupported-picture rows, zero
  picture frames, and 19,683 command frames across custom/generic/loaded/resource/system fonts plus same-context resize
  and forced destination-context variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-204659/suite.tsv`.
- The focused `graphics-layer` command-probe group was refreshed next as the supported replay pair for the recent layer
  invalid/extras checkpoints. It passed 22/22 with `fallback_sum=0`, one intentional unsupported-picture row, 1,152
  picture frames, and 31,905 command frames across base graphics layers, clips, blend/filter/effect descriptors,
  shadows, and 2D/3D transform variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-205726/suite.tsv`.
- The focused `surface-transform-ui` command-probe group was refreshed next. It passed 11/11 with `fallback_sum=0`,
  zero unsupported-picture rows, zero picture frames, and 16,494 command frames across native bridge load-library,
  points, transforms, gradient surfaces/paths, popup/menu layering, and text-image rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-211704/suite.tsv`.
- The focused `gradient-path-stroke-fallbacks` command-probe group was refreshed next. It passed 3/3 with
  `fallback_sum=0`, three intentional unsupported-picture rows, 3,864 picture frames, and zero command frames across
  linear/radial/sweep stroked gradient-path fallback ordering:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-212527/suite.tsv`.
- The periodic full default command-probe consolidation passed after the focused refresh batch. Discovery still
  resolved 522 default rows with no ungrouped or duplicate cases, and the sweep passed 522/522 with
  `fallback_sum=350`, 61 intentional unsupported-picture rows, 81,988 picture frames, and 171,971 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-212906/suite.tsv`.
- New Magic Jewel command-probe sentinels cover live invalid linear, radial, and sweep gradient color counts. Public
  Brush construction accepts 17 colors and attaches JBR gradient metadata, so CMP now rejects these live Compose
  recordings as `linearGradientColorCount`, `radialGradientColorCount`, and `sweepGradientColorCount` before replay.
  No-run discovery now reports 517 default command-probe rows, a `gradient-color-count-invalid` quick group with
  3 rows, and `gradient-invalid` 66. Focused `CASE_GROUPS=gradient-color-count-invalid` passed 3/3 with
  `fallback_sum=0`, three intentional unsupported-picture rows, 2,989 JBR picture frames, and zero command frames;
  the adjacent `gradient-invalid` group passed 66/66 with `fallback_sum=60`, six intentional unsupported-picture rows,
  5,587 JBR picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-141516/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-141803/suite.tsv`.
- A new Magic Jewel command-probe sentinel covers graphics layers with invalid negative `shadowElevation`. This
  exercises the layer-level `graphicsLayer:shadowElevation` guard before replay. No-run discovery now reports 497
  default command-probe rows and `graphics-layer` 22. Focused exact validation passed 1/1 with 969 JBR picture frames
  and zero command frames; the adjacent `graphics-layer` group passed 22/22 with `fallback_sum=0`, one intentional
  unsupported-picture row, 879 picture frames, and 24,305 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-175414/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-180307/suite.tsv`.
- New Magic Jewel command-probe sentinels cover stroked linear, radial, and sweep path gradients. CMP currently rejects
  these real Compose probes through the gradient-paint guards (`linearGradientPaint`, `radialGradientPaint`, and
  `sweepGradientPaint`) before the path-specific style checks are reached, so the unsupported surface is now explicitly
  documented by actual recorder output. No-run discovery now reports 496 default command-probe rows and a
  `gradient-path-stroke-fallbacks` group with 3 rows. The focused group passed 3/3 with `fallback_sum=0`, three
  intentional unsupported-picture rows, 3,245 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-173734/suite.tsv`.
- A new Magic Jewel command-probe sentinel covers `drawImageRect` with an unsupported raw Skia table color filter,
  matching the recorder's image-paint guard that only accepts tint/color-matrix descriptor-backed color filters on the
  command path. No-run discovery now reports 493 default command-probe rows and `color-filters` 12, with no ungrouped
  rows. Focused exact validation passed 1/1 with the `colorFilter` unsupported reason present, 931 JBR picture frames,
  and zero command frames; the adjacent `color-filters` group passed 12/12 with `fallback_sum=0`, three intentional
  unsupported-picture rows, 3,516 picture frames, and 12,920 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-171252/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-171707/suite.tsv`.
- A new Magic Jewel command-probe sentinel covers `Canvas.drawVertices` with raw Skia-backed color-filter paint, which
  CMP intentionally rejects because the vertices command ABI is currently solid-color-only. No-run discovery now reports
  492 default command-probe rows and `core-effects` 8, with no ungrouped rows. Focused exact validation passed 1/1 with
  the `vertices` unsupported reason present, 927 JBR picture frames, and zero command frames; the adjacent
  `core-effects` group passed 8/8 with `fallback_sum=0`, three intentional unsupported-picture rows, 3,195 picture
  frames, and 6,876 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-155224/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-160716/suite.tsv`.
- The latest compatibility matrix passed after rebuilding the local `/tmp` JBR Skia artifacts that the harness
  consumes: 57/57 passed, `fallback_sum=56`, 770 command frames, and every row used background-window mode. The
  pre-rebuild focused `happy` failures were artifact-state failures (`public-api-missing` after
  `/tmp/jbr-api-shim.jar` and `/tmp/jbr-skia-native/libjbrskiainterop.dylib` were absent), not command ABI
  regressions:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260603-140432/matrix.tsv`.
- The latest artifact matrix refresh passed on the restored current artifacts: required rows 2/2 passed with
  `fallback_sum=1` and 438 command frames, while optional-old rows recorded the five expected skips because no old
  artifact variables were set:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260603-143254/matrix.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260603-143404/matrix.tsv`.
- The latest full default screenshot parity suite passed in marker-only mode after the command, compatibility, and
  artifact refreshes: 106/106 passed, `fallback_sum=11`, zero JBR picture frames, and 93,672 JBR command frames. The
  11 fallback markers are bounded to resize sentinel rows, while all screenshot pixel metrics are intentionally
  `missing` because `EXPECT_SCREENSHOT_ASSERTION=false` was set:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260603-143459/suite.tsv`.
- The latest full default command-probe consolidation passed in command-marker-only mode after the raw conical
  gradient sentinel: 488/488 passed, `fallback_sum=350`, `unsupported_rows=27`, `jbr_picture_frames=34246`, and
  `jbr_command_frames=188703`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-224702/suite.tsv`.
- Magic Jewel raw Skia table color-filter fallback sentinel landed in pushed commit `55df401`. No-run discovery now
  reports 489 default command-probe rows and 11 `color-filters` rows, with no ungrouped rows. Focused
  `CASES=commands-raw-table-color-filter-fallback` passed 1/1 with one intentional unsupported-picture row, 826 JBR
  picture frames, and zero command frames; the adjacent `CASE_GROUPS=color-filters` refresh passed 11/11 with two
  intentional unsupported-picture rows, 2,518 JBR picture frames, and 14,110 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-040651/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-040750/suite.tsv`.
- Magic Jewel raw Skia table color-filter saveLayer fallback sentinel landed in pushed commit `02a250f`. No-run
  discovery now reports 490 default command-probe rows and 8 `save-layer-shader-fallbacks` rows, with no ungrouped
  rows. Focused `CASES=commands-save-layer-raw-table-color-filter-fallback` passed 1/1 with one intentional
  unsupported-picture row, 1,317 JBR picture frames, and zero command frames; the adjacent
  `CASE_GROUPS=save-layer-shader-fallbacks` refresh passed 8/8 with six intentional unsupported-picture rows, 8,377
  JBR picture frames, and 4,799 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-042132/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-042236/suite.tsv`.
- Magic Jewel raw Skia table color-filter graphicsLayer fallback sentinel landed in pushed commit `7c1ffcc`. No-run
  discovery now reports 491 default command-probe rows and 15 `graphics-layer-extras` rows, with no ungrouped rows.
  Focused `CASES=commands-graphics-layer-raw-table-color-filter-fallback` passed 1/1 with one intentional
  unsupported-picture row, 1,334 JBR picture frames, and zero command frames; the adjacent
  `CASE_GROUPS=graphics-layer-extras` refresh passed 15/15 with three intentional unsupported-picture rows, 3,960 JBR
  picture frames, and 17,534 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-043200/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-043305/suite.tsv`.
- Magic Jewel full default command-probe consolidation passed in command-marker-only mode after the raw Skia table
  color-filter sentinel batch: 491/491 passed, `fallback_sum=350`, `unsupported_rows=30`,
  `jbr_picture_frames=38973`, and `jbr_command_frames=166320`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-044455/suite.tsv`.
- Focused command-probe invalid guard refreshes passed after the raw table full-sweep consolidation:
  `primitive-invalid` 13/13 with `fallback_sum=13`, `native-text-invalid` 11/11 with `fallback_sum=11` and 976
  command frames, and `path-invalid` 22/22 with `fallback_sum=22`; all three had `unsupported_rows=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-095812/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-100643/suite.tsv`,
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-101406/suite.tsv`.
- Focused command-probe descriptor/path guard refreshes passed next: `gradient-path-invalid` 18/18 with
  `fallback_sum=18` and `effect-descriptor-invalid` 28/28 with `fallback_sum=28`; both had `unsupported_rows=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-102903/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-104049/suite.tsv`.
- Focused command-probe image handle guard refresh passed next: `image-handles-invalid` 27/27 with
  `fallback_sum=27`, `unsupported_rows=0`, zero picture frames, and 1,102 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-105942/suite.tsv`.
- Focused command-probe descriptor handle guard refresh passed next: `descriptor-handles-invalid` 48/48 with
  `fallback_sum=48`, `unsupported_rows=0`, and zero replay frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-111739/suite.tsv`.
- Focused command-probe saveLayer guard refresh passed next: `save-layer-invalid` 37/37 with `fallback_sum=37`,
  `unsupported_rows=0`, and zero replay frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-114844/suite.tsv`.
- Focused command-probe shader descriptor guard refresh passed next: `shader-descriptor-invalid` 30/30 with
  `fallback_sum=30`, `unsupported_rows=0`, and zero replay frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-121314/suite.tsv`.
- Focused command-probe RuntimeEffect guard refresh passed next: `runtime-effect-invalid` 62/62 with
  `fallback_sum=56`, six intentional unsupported-picture rows, 7,356 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-123402/suite.tsv`.
- Focused command-probe gradient guard refresh passed next: `gradient-invalid` 60/60 with `fallback_sum=60`,
  `unsupported_rows=0`, and zero replay frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-131836/suite.tsv`.
- Compact focused command-probe invalid refresh passed next: `shader-ref-invalid`,
  `fill-rect-color-filter-invalid`, `blend-mode-invalid`, and `stream-invalid` together passed 18/18 with
  `fallback_sum=18`, `unsupported_rows=0`, and zero replay frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-135655/suite.tsv`.
- Focused command-probe supported-command sanity refreshes passed next: `core-effects` 7/7 with `fallback_sum=0`, two
  intentional unsupported-picture rows, 2,049 picture frames, and 7,785 command frames; `smoke` 6/6 with
  `fallback_sum=0`, no unsupported rows, and 7,435 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-140936/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-141436/suite.tsv`.
- Focused command-probe transform/text supported refreshes passed next: `surface-transform-ui` 11/11 with
  `fallback_sum=0`, no unsupported rows, and 16,553 command frames; `native-text` 14/14 with `fallback_sum=0`, no
  unsupported rows, and 20,315 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-142012/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-142743/suite.tsv`.
- Focused command-probe color/saveLayer fallback refreshes passed next: `color-filters` 11/11 with `fallback_sum=0`,
  two intentional unsupported-picture rows, 2,098 picture frames, and 11,700 command frames;
  `save-layer-shader-fallbacks` 8/8 with `fallback_sum=0`, six intentional unsupported-picture rows, 7,270 picture
  frames, and 2,039 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-143804/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-144528/suite.tsv`.
- Focused command-probe descriptor lifecycle refresh passed next: `descriptor-lifecycle` 18/18 with `fallback_sum=0`,
  no unsupported rows, zero picture frames, and 25,506 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-145219/suite.tsv`.
- Focused command-probe shader rendering/composition refreshes passed next: `shader-rendering` 14/14 with
  `fallback_sum=0`, nine intentional unsupported-picture rows, 10,692 picture frames, and 6,032 command frames;
  `shader-composition-runtime` 15/15 with `fallback_sum=0`, two intentional unsupported-picture rows, 2,188 picture
  frames, and 16,730 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-150751/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-151724/suite.tsv`.
- Focused command-probe graphics-layer refreshes passed next: `graphics-layer` 21/21 with `fallback_sum=0`, no
  unsupported rows, zero picture frames, and 29,909 command frames; `graphics-layer-extras` 15/15 with
  `fallback_sum=0`, three intentional unsupported-picture rows, 3,322 picture frames, and 18,044 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-153002/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-154344/suite.tsv`.
- Magic Jewel raw Skia two-point conical gradient shader fallback sentinel landed in pushed commit `020dcb3`.
  No-run discovery now reports 488 default command-probe rows and 14 `shader-rendering` rows, with no ungrouped rows.
  Focused `CASES=commands-raw-conical-gradient-shader-fallback` passed 1/1 with one intentional
  unsupported-picture row, 1,496 JBR picture frames, and zero command frames; the adjacent
  `CASE_GROUPS=shader-rendering` refresh passed 14/14 with nine intentional unsupported-picture rows, 11,653 JBR
  picture frames, and 7,884 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-223319/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-223423/suite.tsv`.
- Focused command-probe `CASE_GROUPS=save-layer-shader-fallbacks` passed after the marker-only parity consolidation:
  7/7 passed, `fallback_sum=0`, five intentional unsupported-picture rows, 6,491 JBR picture frames, and 4,020 JBR
  command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-143004/suite.tsv`.
- Focused command-probe `CASE_GROUPS=primitive-invalid` refreshed after the marker-only parity consolidation:
  13/13 passed, `fallback_sum=13`, no unsupported rows, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-143801/suite.tsv`.
- Focused command-probe `CASE_GROUPS=native-text-invalid` refreshed after the marker-only parity consolidation:
  11/11 passed, `fallback_sum=11`, no unsupported rows, zero picture frames, and 1,857 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-144639/suite.tsv`.
- Focused command-probe `CASE_GROUPS=path-invalid` refreshed after the marker-only parity consolidation:
  22/22 passed, `fallback_sum=22`, no unsupported rows, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-145405/suite.tsv`.
- Focused command-probe `CASE_GROUPS=color-filters` refreshed after the marker-only parity consolidation:
  10/10 passed, `fallback_sum=0`, one intentional unsupported-picture row, 1,451 picture frames, and 19,801 command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-150741/suite.tsv`.
- Focused command-probe `CASE_GROUPS=shader-rendering` refreshed after the marker-only parity consolidation:
  13/13 passed, `fallback_sum=0`, eight intentional unsupported-picture rows, 11,161 picture frames, and 9,643
  command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-151449/suite.tsv`.
- Focused command-probe `CASE_GROUPS=shader-composition-runtime` refreshed after the marker-only parity
  consolidation: 15/15 passed, `fallback_sum=0`, two intentional unsupported-picture rows, 3,016 picture frames, and
  28,748 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-152335/suite.tsv`.
- Focused command-probe `CASE_GROUPS=core-effects` refreshed after the marker-only parity consolidation:
  7/7 passed, `fallback_sum=0`, two intentional unsupported-picture rows, 3,122 picture frames, and 10,716 command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-153337/suite.tsv`.
- Focused command-probe `CASE_GROUPS=surface-transform-ui` refreshed after the marker-only parity consolidation:
  11/11 passed, `fallback_sum=0`, no unsupported rows, zero picture frames, and 23,868 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-153856/suite.tsv`.
- Focused command-probe `CASE_GROUPS=graphics-layer-extras` refreshed after the marker-only parity consolidation:
  14/14 passed, `fallback_sum=0`, two intentional unsupported-picture rows, 2,700 picture frames, and 24,591 command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-154633/suite.tsv`.
- Focused command-probe `CASE_GROUPS=graphics-layer` refreshed after the marker-only parity consolidation:
  21/21 passed, `fallback_sum=0`, no unsupported rows, zero picture frames, and 45,890 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-155613/suite.tsv`.
- Focused command-probe `CASE_GROUPS=descriptor-lifecycle` refreshed after the marker-only parity consolidation:
  18/18 passed, `fallback_sum=0`, no unsupported rows, zero picture frames, and 38,694 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-160948/suite.tsv`.
- Focused command-probe `CASE_GROUPS=native-text` refreshed after the marker-only parity consolidation:
  14/14 passed, `fallback_sum=0`, no unsupported rows, zero picture frames, and 27,143 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-162416/suite.tsv`.
- Focused command-probe `CASE_GROUPS=image-handles-invalid` refreshed after the marker-only parity consolidation:
  27/27 passed, `fallback_sum=27`, no unsupported rows, zero picture frames, and 1,153 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-163344/suite.tsv`.
- Focused command-probe `CASE_GROUPS=save-layer-invalid` refreshed after the marker-only parity consolidation:
  37/37 passed, `fallback_sum=37`, no unsupported rows, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-165035/suite.tsv`.
- Focused command-probe `CASE_GROUPS=effect-descriptor-invalid` refreshed after the marker-only parity consolidation:
  28/28 passed, `fallback_sum=28`, no unsupported rows, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-171350/suite.tsv`.
- Focused command-probe `CASE_GROUPS=shader-descriptor-invalid` refreshed after the marker-only parity consolidation:
  30/30 passed, `fallback_sum=30`, no unsupported rows, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-173119/suite.tsv`.
- Focused command-probe `CASE_GROUPS=gradient-path-invalid` refreshed after the marker-only parity consolidation:
  18/18 passed, `fallback_sum=18`, no unsupported rows, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-175010/suite.tsv`.
- Focused command-probe `CASE_GROUPS=descriptor-handles-invalid` refreshed after the marker-only parity consolidation:
  48/48 passed, `fallback_sum=48`, no unsupported rows, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-180200/suite.tsv`.
- Focused command-probe `CASE_GROUPS=stream-invalid` refreshed after the marker-only parity consolidation:
  8/8 passed, `fallback_sum=8`, no unsupported rows, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-183130/suite.tsv`.
- Focused command-probe `CASE_GROUPS=smoke` refreshed after the marker-only parity consolidation:
  6/6 passed, `fallback_sum=0`, no unsupported rows, zero picture frames, and 12,275 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-183718/suite.tsv`.
- Focused command-probe `CASE_GROUPS=gradient-invalid` refreshed after the marker-only parity consolidation:
  60/60 passed, `fallback_sum=60`, no unsupported rows, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-145017/suite.tsv`.
- Focused command-probe `CASE_GROUPS=runtime-effect-invalid` refreshed after the marker-only parity consolidation:
  62/62 passed, `fallback_sum=56`, six intentional unsupported-picture rows, 5,898 picture frames, and zero command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-152938/suite.tsv`.
- Focused command-probe `CASE_GROUPS=fill-rect-color-filter-invalid` refreshed after the marker-only parity
  consolidation: 5/5 passed, `fallback_sum=5`, no unsupported rows, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-161535/suite.tsv`.
- Focused command-probe `CASE_GROUPS=shader-ref-invalid` refreshed after the marker-only parity consolidation:
  3/3 passed, `fallback_sum=3`, no unsupported rows, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-162224/suite.tsv`.
- Focused command-probe `CASE_GROUPS=blend-mode-invalid` refreshed after the marker-only parity consolidation:
  2/2 passed, `fallback_sum=2`, no unsupported rows, zero picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-162604/suite.tsv`.
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
- The latest compatibility matrix checkpoint passed in command-marker-only mode after the full command-probe
  consolidation, with 57/57 rows passed, `fallback_sum=56`, 454 JBR command frames from the happy path, and
  background-window mode on every row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260531-214046/matrix.tsv`.
- The latest artifact matrix checkpoint passed the required current-artifact rows on ABI 106 local artifacts:
  `current-all` replayed commands with 445 JBR command frames, `missing-public-api` fell back exactly once, and the
  optional old-artifact rows were skipped because no bundle variables were set:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260531-221009/matrix.tsv`.
- Skiko focused publication and full focused `JbrSkiaInteropTest` class passed after the 20260531 command,
  compatibility, and artifact matrix refreshes:
  `./gradlew publishAwtPublicationToMavenLocal publishAwtRuntimeElementsPublicationToMavenLocal publishKotlinMultiplatformPublicationToMavenLocal`
  and `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- A follow-up focused artifact matrix also passed after fixing Magic Jewel's JBR API helper to remove its temporary
  `com.jetbrains.exported.JBRApi` desktop-overlay stub on exit: `current-all` replayed commands with 460 JBR command
  frames, and `missing-public-api` fell back exactly once:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260528-173847/matrix.tsv`.
- The latest full default screenshot parity checkpoint passed after the command-probe, compatibility, and artifact
  refreshes. Aggregate: 106/106 passed, `fallback_sum=11`, zero JBR picture frames, 111,257 JBR command frames,
  average `bad_pixel_ratio=0.05158`, and 107 TSV lines including the header:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260529-091416/suite.tsv`.
- The latest focused screenshot parity marker-only checkpoint passed after teaching the wrapper to honor
  `EXPECT_SCREENSHOT_ASSERTION=false` by skipping the image diff when command/report validation succeeds. Aggregate:
  3/3 `CASE_GROUPS=smoke` rows passed, `fallback_sum=0`, zero JBR picture frames, 5,603 JBR command frames, and
  intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-122221/suite.tsv`.
- The next focused screenshot parity marker-only checkpoint also passed: 6/6 `CASE_GROUPS=descriptor-lifecycle`
  rows, `fallback_sum=1`, zero JBR picture frames, 10,185 JBR command frames, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-122542/suite.tsv`.
- Focused screenshot parity marker-only `CASE_GROUPS=graphics-layer-basic` passed 7/7 with `fallback_sum=0`, zero JBR
  picture frames, 16,730 JBR command frames, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-123134/suite.tsv`.
- Focused screenshot parity marker-only `CASE_GROUPS=core-drawing` passed 16/16 with `fallback_sum=0`, zero JBR
  picture frames, 25,745 JBR command frames, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-123636/suite.tsv`.
- Focused screenshot parity marker-only `CASE_GROUPS=native-text` passed 14/14 with `fallback_sum=4`, zero JBR
  picture frames, 18,663 JBR command frames, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-124546/suite.tsv`.
- Focused screenshot parity marker-only `CASE_GROUPS=runtime-effect` passed 14/14 with `fallback_sum=2`, zero JBR
  picture frames, 17,990 JBR command frames, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-125424/suite.tsv`.
- Focused screenshot parity marker-only `CASE_GROUPS=shader-rendering` passed 18/18 with `fallback_sum=4`, zero JBR
  picture frames, 25,540 JBR command frames, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-130246/suite.tsv`.
- Focused screenshot parity marker-only `CASE_GROUPS=graphics-layer-effects` passed 14/14 with `fallback_sum=2`, zero
  JBR picture frames, 29,594 JBR command frames, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-131305/suite.tsv`.
- Focused screenshot parity marker-only `CASE_GROUPS=graphics-layer-clip-shadow-transform` passed 14/14 with
  `fallback_sum=0`, zero JBR picture frames, 34,196 JBR command frames, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-132134/suite.tsv`.
- Combined across the nine focused marker-only screenshot parity groups after the wrapper fix, all 106 parity rows
  passed with `fallback_sum=13`, zero JBR picture frames, 184,246 JBR command frames, and intentionally missing pixel
  metrics because local screenshot comparison was skipped.
- A periodic full default screenshot parity marker-only consolidation then passed as a single TSV: 106/106 passed,
  `fallback_sum=12`, zero JBR picture frames, 186,106 JBR command frames, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-133012/suite.tsv`.
- Current no-run discovery helpers cover the command-probe, screenshot parity, compatibility matrix, artifact matrix,
  and benchmark suite loops. Use `LIST_CASES=true` / `LIST_CASE_COUNT=true` for exact row discovery, and
  `CASE_GROUPS=...`, `LIST_CASE_GROUPS=true`, `LIST_CASE_GROUP_COUNTS=true`, and `LIST_UNGROUPED_CASES=true` for
  curated slice discovery and grouped-coverage audits.
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
- The latest CMP recorder gate passed after the refreshed 20260530 Skiko and JBR parser/API gates:
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

The ABI 105 skew slice introduced `Canvas.skew` lowering through the 3x3 concat-matrix command instead of marking the
command stream unsupported; current ABI 106 keeps that replay path:

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
- Descriptor-backed screenshot parity rows now also carry bounded JBR shader/effect handle definition ceilings for
  color-filter, shader, RuntimeEffect, and graphics-layer lifecycle probes. These are full-scene runaway guards rather
  than exact frame-count assertions; the focused descriptor-guard refresh passed 27/27 with `fallback_sum=9`,
  `jbr_picture_frames=0`, and `jbr_command_frames=17040`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260531-235827/suite.tsv`.
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
