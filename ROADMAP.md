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
- Validation cadence: run very narrow validation for each focused change. Broad command sweeps, screenshot sweeps,
  benchmark suites, and full matrices are capped at one broad validation slot per local calendar day unless the user
  explicitly asks for another one or an ABI/capability break needs an emergency gate. Magic Jewel's broad runners
  enforce this with `scripts/jbr-skia-daily-validation-guard.sh`; use exact small `CASES`/`CASE_GROUPS` for normal
  iteration, or `JBR_SKIA_ALLOW_EXTRA_BROAD_VALIDATION=true` only for an explicit override. Default launches are
  guarded before broad runners do slow setup, while default-list `CASES_FROM`/`CASES_UNTIL` range launches and
  resolved selections above
  `JBR_SKIA_BROAD_VALIDATION_CASE_LIMIT` rows count as broad validation; the default limit is 2 rows. List-only range
  helpers remain allowed. This daily cap supersedes the earlier "run a broad sweep every ten focused changes"
  checkpoint rhythm.
- Keep branches committed and pushed to the user's GitHub forks at each major step.
- Keep the top-level plan/roadmap compact. Move verbose historical narrative into `docs/history/` or focused
  `docs/current/` ledgers when these files start to crowd agent context.

## Latest Validations

- Magic Jewel tightened the exact graphics-layer round-clip lifecycle screenshot-parity pair with explicit zero
  ceilings for JBR effect-handle and shader-handle defines, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-round-clip parity-forced-context-graphics-layer-round-clip" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames, zero effect-handle markers, zero shader-handle markers, and
  screenshot status passed. The resize row recorded the known single early `command-stream-invalid` resize parity
  artifact, then passed with 793 JBR command frames, `avg_delta=1.912`, `bad_pixel_ratio=0.04594`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, and nine
  image refs. The forced-context row passed fallback-free with 848 JBR command frames, `avg_delta=2.053`,
  `bad_pixel_ratio=0.04829`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, and nine image refs. This was an exact two-row screenshot-parity refresh, not
  the full suite; output directory was 7.6M and disk free was about 170Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-103200/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer clip lifecycle screenshot-parity pair with explicit zero ceilings for
  JBR effect-handle and shader-handle defines, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-clip parity-forced-context-graphics-layer-clip" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames, zero effect-handle markers, zero shader-handle markers, and
  screenshot status passed. The resize row recorded the known single early `command-stream-invalid` resize parity
  artifact, then passed with 694 JBR command frames, `avg_delta=1.908`, `bad_pixel_ratio=0.04580`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, and nine
  image refs. The forced-context row passed fallback-free with 1,029 JBR command frames, `avg_delta=2.049`,
  `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, and nine image refs. This was an exact two-row screenshot-parity refresh, not
  the full suite; output directory was 7.7M and disk free was about 170Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-102827/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer offscreen lifecycle screenshot-parity pair with explicit zero ceilings
  for JBR effect-handle and shader-handle defines, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offscreen parity-forced-context-graphics-layer-offscreen" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames, zero effect-handle markers, zero shader-handle markers, and
  screenshot status passed. The resize row recorded the known single early `command-stream-invalid` resize parity
  artifact, then passed with 775 JBR command frames, `avg_delta=1.909`, `bad_pixel_ratio=0.04583`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, and nine
  image refs. The forced-context row passed fallback-free with 1,480 JBR command frames, `avg_delta=2.050`,
  `bad_pixel_ratio=0.04821`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, and nine image refs. This was an exact two-row screenshot-parity refresh, not
  the full suite; output directory was 8.0M and disk free was about 170Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-102440/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer modulate-alpha lifecycle screenshot-parity pair with explicit zero
  ceilings for JBR effect-handle and shader-handle defines, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-modulate-alpha parity-forced-context-graphics-layer-modulate-alpha" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames, zero effect-handle markers, zero shader-handle markers, and
  screenshot status passed. The resize row recorded the known single early `command-stream-invalid` resize parity
  artifact, then passed with 865 JBR command frames, `avg_delta=2.265`, `bad_pixel_ratio=0.05718`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, and one scoped image-cache clear. The
  forced-context row passed fallback-free with 884 JBR command frames, `avg_delta=2.479`,
  `bad_pixel_ratio=0.06186`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, and one scoped image-cache clear. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 7.7M and disk free was about 170Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-101842/suite.tsv`.
- Magic Jewel tightened the exact base graphics-layer lifecycle screenshot-parity pair with explicit zero ceilings for
  JBR effect-handle and shader-handle defines, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer parity-forced-context-graphics-layer" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames, zero effect-handle markers, zero shader-handle markers, and
  screenshot status passed. The resize row recorded the known single early `command-stream-invalid` resize parity
  artifact, then passed with 866 JBR command frames, `avg_delta=1.906`, `bad_pixel_ratio=0.04576`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, and one scoped image-cache clear. The
  forced-context row passed fallback-free with 1,162 JBR command frames, `avg_delta=2.048`,
  `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, and one scoped image-cache clear. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 7.9M and disk free was about 170Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-101354/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer blend-mode lifecycle screenshot-parity pair with explicit zero
  ceilings for JBR effect-handle and shader-handle defines, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-blend-mode parity-forced-context-graphics-layer-blend-mode" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames, zero effect-handle markers, zero shader-handle markers, and
  screenshot status passed. The resize row recorded the known single early `command-stream-invalid` resize parity
  artifact, then passed with 1,226 JBR command frames, `avg_delta=1.922`, `bad_pixel_ratio=0.04658`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, and one scoped image-cache clear. The
  forced-context row passed fallback-free with 1,411 JBR command frames, `avg_delta=2.065`,
  `bad_pixel_ratio=0.04904`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, and one scoped image-cache clear. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 8.3M and disk free was about 170Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-100858/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer color-filter lifecycle screenshot-parity pair with explicit zero
  ceilings for JBR effect-handle and shader-handle defines, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-color-filter parity-forced-context-graphics-layer-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames, zero effect-handle markers, zero shader-handle markers, and
  screenshot status passed. The resize row recorded the known single early `command-stream-invalid` resize parity
  artifact, then passed with 631 JBR command frames, `avg_delta=1.906`, `bad_pixel_ratio=0.04576`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, and one scoped image-cache clear. The
  forced-context row passed fallback-free with 1,052 JBR command frames, `avg_delta=2.047`,
  `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, and one scoped image-cache clear. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 7.5M and disk free was about 170Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-100422/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer blend+color-filter lifecycle screenshot-parity pair with explicit
  zero ceilings for JBR effect-handle and shader-handle defines, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-blend-color-filter parity-forced-context-graphics-layer-blend-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames, zero effect-handle markers, zero shader-handle markers, and
  screenshot status passed. The resize row recorded the known single early `command-stream-invalid` resize parity
  artifact, then passed with 679 JBR command frames, `avg_delta=1.915`, `bad_pixel_ratio=0.04659`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, and one scoped image-cache clear. The
  forced-context row passed fallback-free with 789 JBR command frames, `avg_delta=2.056`,
  `bad_pixel_ratio=0.04906`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, and one scoped image-cache clear. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 7.4M and disk free was about 170Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-100013/suite.tsv`.
- Magic Jewel rechecked the exact graphics-layer chained-render-effect lifecycle screenshot-parity caps after stale
  compact notes suggested tighter `7/7` ceilings, confirmed the live resize ceiling remains 16 and the forced-context
  ceiling remains 20, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-chained-render-effect parity-forced-context-graphics-layer-chained-render-effect" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 529 JBR command frames,
  `avg_delta=1.902`, `bad_pixel_ratio=0.04557`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 16 effect-handle define frames, 1,067 effect-handle use frames,
  and 1,059 effect-handle cache-hit frames. The forced-context row passed fallback-free with 816 JBR command frames,
  `avg_delta=2.041`, `bad_pixel_ratio=0.04784`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 20 effect-handle define frames, 1,513 effect-handle use frames,
  and 1,503 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 7.4M and disk free was about 170Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-095456/suite.tsv`.
- Magic Jewel restored the exact graphics-layer offset-effect lifecycle screenshot-parity caps to the documented
  tightened ceilings, reducing the live resize ceiling to 9 and the forced-context ceiling to 11, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offset-effect parity-forced-context-graphics-layer-offset-effect" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 766 JBR command frames,
  `avg_delta=1.908`, `bad_pixel_ratio=0.04583`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 8 effect-handle define frames, 1,523 effect-handle use frames,
  and 1,514 effect-handle cache-hit frames. The forced-context row passed fallback-free with 964 JBR command frames,
  `avg_delta=2.048`, `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 10 effect-handle define frames, 1,718 effect-handle use frames,
  and 1,708 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 7.9M and disk free was about 170Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-094649/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer render-effect lifecycle screenshot-parity pair to match current
  report evidence more closely, reducing the resize ceiling to 8 and the forced-context ceiling to 11, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect parity-forced-context-graphics-layer-render-effect" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,013 JBR command frames,
  `avg_delta=1.902`, `bad_pixel_ratio=0.04557`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 8 effect-handle define frames, 1,876 effect-handle use frames,
  and 1,868 effect-handle cache-hit frames. The forced-context row passed fallback-free with 820 JBR command frames,
  `avg_delta=2.045`, `bad_pixel_ratio=0.04801`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 9 effect-handle define frames, 1,444 effect-handle use frames,
  and 1,435 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 8.0M and disk free was about 197Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-094220/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer color-matrix-filter lifecycle screenshot-parity pair to match current
  report evidence more closely, reducing the resize ceiling to 8 and the forced-context ceiling to 11, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-color-matrix-filter parity-forced-context-graphics-layer-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,291 JBR command frames,
  `avg_delta=1.907`, `bad_pixel_ratio=0.04577`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 8 effect-handle define frames, 2,257 effect-handle use frames,
  and 2,249 effect-handle cache-hit frames. The forced-context row passed fallback-free with 1,227 JBR command frames,
  `avg_delta=2.048`, `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 11 effect-handle define frames, 2,218 effect-handle use frames,
  and 2,207 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 11M and disk free was about 197Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-093812/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer render-effect+color-matrix-filter lifecycle screenshot-parity pair to
  match current report evidence more closely, reducing the resize ceiling to 16 and the forced-context ceiling to 20,
  then reran `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-color-matrix-filter parity-forced-context-graphics-layer-render-effect-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 553 JBR command frames,
  `avg_delta=1.903`, `bad_pixel_ratio=0.04557`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 14 effect-handle define frames, 2,484 effect-handle use frames,
  and 2,470 effect-handle cache-hit frames. The forced-context row passed fallback-free with 1,098 JBR command frames,
  `avg_delta=2.045`, `bad_pixel_ratio=0.04801`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 20 effect-handle define frames, 3,620 effect-handle use frames,
  and 3,600 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 9.3M and disk free was about 197Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-093417/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer render-effect+blend-mode lifecycle screenshot-parity pair to match
  current report evidence more closely, reducing the resize ceiling to 8 and the forced-context ceiling to 10, then
  reran `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-blend-mode parity-forced-context-graphics-layer-render-effect-blend-mode" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 862 JBR command frames,
  `avg_delta=2.376`, `bad_pixel_ratio=0.06017`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 8 effect-handle define frames, 1,555 effect-handle use frames,
  and 1,547 effect-handle cache-hit frames. The forced-context row passed fallback-free with 616 JBR command frames,
  `avg_delta=2.608`, `bad_pixel_ratio=0.06548`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 9 effect-handle define frames, 1,384 effect-handle use frames,
  and 1,375 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 7.7M and disk free was about 197Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260618-092921/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer render-effect+color-filter lifecycle screenshot-parity pair to match
  current report evidence more closely, reducing the resize ceiling to 7 and the forced-context ceiling to 11, then
  reran `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-color-filter parity-forced-context-graphics-layer-render-effect-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 613 JBR command frames,
  `avg_delta=1.901`, `bad_pixel_ratio=0.04557`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 7 effect-handle define frames, 1,149 effect-handle use frames,
  and 1,142 effect-handle cache-hit frames. The forced-context row passed fallback-free with 620 JBR command frames,
  `avg_delta=2.044`, `bad_pixel_ratio=0.04801`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 10 effect-handle define frames, 1,234 effect-handle use
  frames, and 1,224 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the full
  suite; output directory was 7.3M, disk free was about 192Gi, and the 2026-06-16 daily broad-validation stamp
  remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260617-150823/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer chained-render-effect+blend-color-matrix-filter parity pair to match
  current report evidence more closely, reducing the resize ceiling to 24 and the forced-context ceiling to 33, then
  reran `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-chained-render-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-chained-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 417 JBR command frames,
  `avg_delta=2.348`, `bad_pixel_ratio=0.06017`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 24 effect-handle define frames, 3,100 effect-handle use frames,
  and 3,084 effect-handle cache-hit frames. The forced-context row passed fallback-free with 689 JBR command frames,
  `avg_delta=2.575`, `bad_pixel_ratio=0.06531`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 33 effect-handle define frames, 2,948 effect-handle use
  frames, and 2,926 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the full
  suite; output directory was 11M, disk free was about 202Gi, and the 2026-06-16 daily broad-validation stamp remained
  the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260617-150125/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer offset-effect lifecycle screenshot-parity pair to match report
  evidence more closely, reducing the resize ceiling to 9 and the forced-context ceiling to 11, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offset-effect parity-forced-context-graphics-layer-offset-effect" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,200 JBR command frames,
  `avg_delta=1.908`, `bad_pixel_ratio=0.04583`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 9 effect-handle define frames, 2,379 effect-handle use
  frames, and 2,370 effect-handle cache-hit frames. The forced-context row passed fallback-free with 1,298 JBR
  command frames, `avg_delta=2.048`, `bad_pixel_ratio=0.04815`, one destination context-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 11 effect-handle define frames,
  2,310 effect-handle use frames, and 2,299 effect-handle cache-hit frames. This was an exact two-row screenshot-parity
  refresh, not the full suite; output directory was 10M, disk free was about 201Gi, and the 2026-06-16 daily
  broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260617-145616/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer offset-effect+blend-color-matrix-filter parity pair to match report
  evidence more closely, reducing the resize ceiling to 16 and the forced-context ceiling to 22, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offset-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-offset-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 681 JBR command frames,
  `avg_delta=2.257`, `bad_pixel_ratio=0.05727`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 16 effect-handle define frames, 4,080 effect-handle use
  frames, and 4,064 effect-handle cache-hit frames. The forced-context row passed fallback-free with 840 JBR command
  frames, `avg_delta=2.465`, `bad_pixel_ratio=0.06185`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, 22 effect-handle define frames, 3,262 effect-handle
  use frames, and 3,240 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the
  full suite; output directory was 11M, disk free was about 202Gi, and the 2026-06-16 daily broad-validation stamp
  remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260617-145111/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer render-effect+blend-color-matrix-filter parity pair to match report
  evidence more closely, reducing the resize ceiling to 14 and the forced-context ceiling to 22, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 667 JBR command frames,
  `avg_delta=2.348`, `bad_pixel_ratio=0.06017`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 14 effect-handle define frames, 2,276 effect-handle use
  frames, and 2,268 effect-handle cache-hit frames. The forced-context row passed fallback-free with 831 JBR command
  frames, `avg_delta=2.574`, `bad_pixel_ratio=0.06548`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, 22 effect-handle define frames, 2,012 effect-handle
  use frames, and 2,001 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the
  full suite; output directory was 11M, disk free was about 202Gi, and the 2026-06-16 daily broad-validation stamp
  remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260617-144438/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer render-effect+blend-color-filter parity pair to match report
  evidence more closely, reducing the resize ceiling to 8 and the forced-context ceiling to 11, then reran
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-blend-color-filter parity-forced-context-graphics-layer-render-effect-blend-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 889 JBR command frames,
  `avg_delta=2.373`, `bad_pixel_ratio=0.06020`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 8 effect-handle define frames, 2,276 effect-handle use
  frames, and 2,268 effect-handle cache-hit frames. The forced-context row passed fallback-free with 819 JBR command
  frames, `avg_delta=2.606`, `bad_pixel_ratio=0.06552`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, 11 effect-handle define frames, 2,012 effect-handle
  use frames, and 2,001 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the
  full suite; output directory was 11M, disk free was about 202Gi, and the 2026-06-16 daily broad-validation stamp
  remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260617-104234/suite.tsv`.
- Magic Jewel tightened the exact graphics-layer near-camera chained-render-effect+blend-color-matrix-filter parity
  pair to match report evidence more closely, reducing the resize ceiling to 24 and the forced-context ceiling to 30,
  then reran `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`.
  Both exact rows passed with zero picture frames and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 528 JBR command frames,
  `avg_delta=2.248`, `bad_pixel_ratio=0.05651`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 24 effect-handle define frames, 2,914 effect-handle use
  frames, and 2,902 effect-handle cache-hit frames. The forced-context row passed fallback-free with 381 JBR command
  frames, `avg_delta=2.455`, `bad_pixel_ratio=0.06094`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, 30 effect-handle define frames, 3,173 effect-handle
  use frames, and 3,154 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the
  full suite; output directory was 5.0M, disk free was about 174Gi, and the 2026-06-16 daily broad-validation stamp
  remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260617-103703/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer chained-render-effect lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-chained-render-effect parity-forced-context-graphics-layer-chained-render-effect" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 936 JBR command frames,
  `avg_delta=1.902`, `bad_pixel_ratio=0.04557`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 16 effect-handle define frames, 1,926 effect-handle use frames,
  and 1,918 effect-handle cache-hit frames. The forced-context row was fallback-free with 1,288 JBR command frames,
  `avg_delta=2.041`, `bad_pixel_ratio=0.04784`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 22 effect-handle define frames, 2,324 effect-handle use frames,
  and 2,313 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 9.5M, disk free was about 201Gi, and the 2026-06-16 daily broad-validation stamp remained the
  compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260617-083726/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer offset-effect lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offset-effect parity-forced-context-graphics-layer-offset-effect" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,293 JBR command frames,
  `avg_delta=1.908`, `bad_pixel_ratio=0.04583`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine effect-handle define frames, 2,379 effect-handle use
  frames, and 2,370 effect-handle cache-hit frames. The forced-context row was fallback-free with 1,326 JBR command
  frames, `avg_delta=2.048`, `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, 11 effect-handle define frames, 2,310 effect-handle
  use frames, and 2,299 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the
  full suite; output directory was 10M, disk free was about 201Gi, and the 2026-06-16 daily broad-validation stamp
  remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-235754/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer near-camera chained-render-effect+blend-color-matrix-filter
  lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 785 JBR command frames,
  `avg_delta=2.248`, `bad_pixel_ratio=0.05651`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 18 effect-handle define frames, 2,914 effect-handle use frames,
  and 2,902 effect-handle cache-hit frames. The forced-context row was fallback-free with 882 JBR command frames,
  `avg_delta=2.455`, `bad_pixel_ratio=0.06094`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 30 effect-handle define frames, 3,173 effect-handle use frames,
  and 3,154 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 8.3M, disk free was about 174Gi, and the 2026-06-16 daily broad-validation stamp remained the
  compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-235047/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer chained-render-effect+blend-color-matrix-filter lifecycle
  screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-chained-render-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-chained-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,236 JBR command frames,
  `avg_delta=2.348`, `bad_pixel_ratio=0.06017`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 21 effect-handle define frames, 3,846 effect-handle use frames,
  and 3,832 effect-handle cache-hit frames. The forced-context row was fallback-free with 722 JBR command frames,
  `avg_delta=2.575`, `bad_pixel_ratio=0.06531`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 33 effect-handle define frames, 2,948 effect-handle use frames,
  and 2,926 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 9.7M, disk free was about 201Gi, and the 2026-06-16 daily broad-validation stamp remained the
  compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-234420/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer offset-effect+blend-color-matrix-filter lifecycle
  screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offset-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-offset-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed after rerunning the exact pair
  outside the sandbox for Gradle wrapper lock access. The resize row recorded the known single early
  `command-stream-invalid` resize parity artifact, then passed with 1,195 JBR command frames, `avg_delta=2.257`,
  `bad_pixel_ratio=0.05727`, one same-context surface-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, 16 effect-handle define frames, 4,080 effect-handle use frames, and 4,064
  effect-handle cache-hit frames. The forced-context row was fallback-free with 700 JBR command frames,
  `avg_delta=2.465`, `bad_pixel_ratio=0.06185`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, 22 effect-handle define frames, 3,262 effect-handle use frames,
  and 3,240 effect-handle cache-hit frames. This was an exact two-row screenshot-parity refresh, not the full suite;
  output directory was 11M, disk free was about 202Gi, and the 2026-06-16 daily broad-validation stamp remained the
  compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-233834/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer render-effect+blend-color-matrix-filter lifecycle
  screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-blend-color-matrix-filter parity-forced-context-graphics-layer-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 894 JBR command frames,
  `avg_delta=2.348`, `bad_pixel_ratio=0.06017`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, 14 effect-handle define frames, 3,028
  effect-handle use frames, and 3,014 effect-handle cache-hit frames. The forced-context row was fallback-free with
  1,130 JBR command frames, `avg_delta=2.574`, `bad_pixel_ratio=0.06548`, one destination context-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, 22 effect-handle
  define frames, 3,838 effect-handle use frames, and 3,816 effect-handle cache-hit frames. This was an exact two-row
  screenshot-parity refresh, not the full suite; output directory was 9.7M, disk free was about 202Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-233106/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer render-effect+blend-color-filter lifecycle screenshot-parity
  pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-blend-color-filter parity-forced-context-graphics-layer-render-effect-blend-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,300 JBR command frames,
  `avg_delta=2.373`, `bad_pixel_ratio=0.06020`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, 8 effect-handle define frames, 2,276
  effect-handle use frames, and 2,268 effect-handle cache-hit frames. The forced-context row was fallback-free with
  1,016 JBR command frames, `avg_delta=2.606`, `bad_pixel_ratio=0.06552`, one destination context-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, 11 effect-handle
  define frames, 2,012 effect-handle use frames, and 2,001 effect-handle cache-hit frames. This was an exact two-row
  screenshot-parity refresh, not the full suite; output directory was 11M, disk free was about 202Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-232522/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer render-effect+color-matrix-filter lifecycle screenshot-parity
  pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-color-matrix-filter parity-forced-context-graphics-layer-render-effect-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 617 JBR command frames,
  `avg_delta=1.903`, `bad_pixel_ratio=0.04557`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, 16 effect-handle define frames, 2,128
  effect-handle use frames, and 2,112 effect-handle cache-hit frames. The forced-context row was fallback-free with
  870 JBR command frames, `avg_delta=2.045`, `bad_pixel_ratio=0.04801`, one destination context-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, 20 effect-handle
  define frames, 3,114 effect-handle use frames, and 3,094 effect-handle cache-hit frames. This was an exact two-row
  screenshot-parity refresh, not the full suite; output directory was 7.8M, disk free was about 202Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-231819/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer render-effect+blend-mode lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-blend-mode parity-forced-context-graphics-layer-render-effect-blend-mode" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 641 JBR command frames,
  `avg_delta=2.376`, `bad_pixel_ratio=0.06017`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, 7 effect-handle define frames, 1,292
  effect-handle use frames, and 1,285 effect-handle cache-hit frames. The forced-context row was fallback-free with
  477 JBR command frames, `avg_delta=2.608`, `bad_pixel_ratio=0.06548`, one destination context-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, 10 effect-handle
  define frames, 1,132 effect-handle use frames, and 1,122 effect-handle cache-hit frames. This was an exact two-row
  screenshot-parity refresh, not the full suite; output directory was 7.2M, disk free was about 205Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-085329/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer render-effect+color-filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-render-effect-color-filter parity-forced-context-graphics-layer-render-effect-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,100 JBR command frames,
  `avg_delta=1.901`, `bad_pixel_ratio=0.04557`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, 7 effect-handle define frames, 1,853
  effect-handle use frames, and 1,846 effect-handle cache-hit frames. The forced-context row was fallback-free with
  1,138 JBR command frames, `avg_delta=2.044`, `bad_pixel_ratio=0.04801`, one destination context-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, 10 effect-handle
  define frames, 1,960 effect-handle use frames, and 1,950 effect-handle cache-hit frames. This was an exact two-row
  screenshot-parity refresh, not the full suite; output directory was 9.5M, disk free was about 205Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-084801/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer blend+color-matrix-filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-blend-color-matrix-filter parity-forced-context-graphics-layer-blend-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,301 JBR command frames,
  `avg_delta=1.922`, `bad_pixel_ratio=0.04657`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, 8 effect-handle define frames, 2,225
  effect-handle use frames, and 2,217 effect-handle cache-hit frames. The forced-context row was fallback-free with
  1,351 JBR command frames, `avg_delta=2.065`, `bad_pixel_ratio=0.04904`, one destination context-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, 11 effect-handle
  define frames, 2,382 effect-handle use frames, and 2,371 effect-handle cache-hit frames. This was an exact two-row
  screenshot-parity refresh, not the full suite; output directory was 11M, disk free was about 205Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-084054/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer blend+color-filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-blend-color-filter parity-forced-context-graphics-layer-blend-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 868 JBR command frames,
  `avg_delta=1.915`, `bad_pixel_ratio=0.04659`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 785 JBR command frames, `avg_delta=2.056`,
  `bad_pixel_ratio=0.04906`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 7.5M, disk free was about 178Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-083504/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer color-filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-color-filter parity-forced-context-graphics-layer-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 773 JBR command frames,
  `avg_delta=1.906`, `bad_pixel_ratio=0.04576`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 1,343 JBR command frames, `avg_delta=2.047`,
  `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 7.9M, disk free was about 178Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-082945/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer blend-mode lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-blend-mode parity-forced-context-graphics-layer-blend-mode" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,237 JBR command frames,
  `avg_delta=1.922`, `bad_pixel_ratio=0.04658`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 882 JBR command frames, `avg_delta=2.065`,
  `bad_pixel_ratio=0.04904`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 8.1M, disk free was about 178Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-082359/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer off-center pivot lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offcenter-pivot parity-forced-context-graphics-layer-offcenter-pivot" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 828 JBR command frames,
  `avg_delta=1.910`, `bad_pixel_ratio=0.04592`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 871 JBR command frames, `avg_delta=2.050`,
  `bad_pixel_ratio=0.04826`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 7.6M, disk free was about 178Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-081753/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer near-camera lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-near-camera parity-forced-context-graphics-layer-near-camera" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 847 JBR command frames,
  `avg_delta=1.913`, `bad_pixel_ratio=0.04603`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 843 JBR command frames, `avg_delta=2.050`,
  `bad_pixel_ratio=0.04826`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 7.6M, disk free was about 175Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-081103/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer scale-translate lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-scale-translate parity-forced-context-graphics-layer-scale-translate" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 780 JBR command frames,
  `avg_delta=1.906`, `bad_pixel_ratio=0.04576`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 909 JBR command frames, `avg_delta=2.049`,
  `bad_pixel_ratio=0.04818`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 7.6M, disk free was about 175Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-080534/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer rotation-xy lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-rotationxy parity-forced-context-graphics-layer-rotationxy" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,195 JBR command frames,
  `avg_delta=1.911`, `bad_pixel_ratio=0.04596`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 830 JBR command frames, `avg_delta=2.050`,
  `bad_pixel_ratio=0.04826`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 7.9M, disk free was about 175Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-075954/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer rotation-y lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-rotationy parity-forced-context-graphics-layer-rotationy" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,164 JBR command frames,
  `avg_delta=1.908`, `bad_pixel_ratio=0.04582`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 1,367 JBR command frames, `avg_delta=2.048`,
  `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 8.3M, disk free was about 176Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-075424/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer rotation-x lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-rotationx parity-forced-context-graphics-layer-rotationx" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,247 JBR command frames,
  `avg_delta=1.909`, `bad_pixel_ratio=0.04586`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 1,409 JBR command frames, `avg_delta=2.050`,
  `bad_pixel_ratio=0.04825`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 10M, `out` remained under watch after
  previous growth, disk free was about 176Gi, and the 2026-06-16 daily broad-validation stamp remained the
  compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-074859/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer path-shadow lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-path-shadow parity-forced-context-graphics-layer-path-shadow" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,362 JBR command frames,
  `avg_delta=1.912`, `bad_pixel_ratio=0.04596`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, one shadow command per frame, and zero
  shader/effect handle markers. The forced-context row was fallback-free with 1,278 JBR command frames,
  `avg_delta=2.053`, `bad_pixel_ratio=0.04839`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, one shadow command per frame, and zero
  shader/effect handle markers. This was an exact two-row screenshot-parity refresh, not the full suite; output
  directory was 8.5M, `out` remained 122G, disk free was about 202Gi, and the 2026-06-16 daily broad-validation stamp
  remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-074226/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer round-shadow lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-round-shadow parity-forced-context-graphics-layer-round-shadow" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 801 JBR command frames,
  `avg_delta=1.912`, `bad_pixel_ratio=0.04592`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, one shadow command per frame, and zero
  shader/effect handle markers. The forced-context row was fallback-free with 1,134 JBR command frames,
  `avg_delta=2.064`, `bad_pixel_ratio=0.04829`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, one shadow command per frame, and zero
  shader/effect handle markers. This was an exact two-row screenshot-parity refresh, not the full suite; output
  directory was 7.9M, `out` remained 122G, disk free was about 202Gi, and the 2026-06-16 daily broad-validation stamp
  remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-073605/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer shadow lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-shadow parity-forced-context-graphics-layer-shadow" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,203 JBR command frames,
  `avg_delta=1.905`, `bad_pixel_ratio=0.04575`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, one shadow command per frame, and zero
  shader/effect handle markers. The forced-context row was fallback-free with 1,211 JBR command frames,
  `avg_delta=2.046`, `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, one shadow command per frame, and zero
  shader/effect handle markers. This was an exact two-row screenshot-parity refresh, not the full suite; output
  directory was 8.3M, `out` remained 122G, disk free was about 202Gi, and the 2026-06-16 daily broad-validation stamp
  remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-073059/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer path-clip lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-path-clip parity-forced-context-graphics-layer-path-clip" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 874 JBR command frames,
  `avg_delta=1.912`, `bad_pixel_ratio=0.04597`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 1,313 JBR command frames, `avg_delta=2.053`,
  `bad_pixel_ratio=0.04839`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 8.0M, `out` remained 122G, disk free was
  about 202Gi, and the 2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-072558/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer round-clip lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-round-clip parity-forced-context-graphics-layer-round-clip" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,191 JBR command frames,
  `avg_delta=1.912`, `bad_pixel_ratio=0.04594`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 812 JBR command frames, `avg_delta=2.053`,
  `bad_pixel_ratio=0.04829`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 7.8M, `out` remained 122G, disk free was
  about 202Gi, and the 2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-072116/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer clip lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-clip parity-forced-context-graphics-layer-clip" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,042 JBR command frames,
  `avg_delta=1.908`, `bad_pixel_ratio=0.04580`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 1,205 JBR command frames, `avg_delta=2.049`,
  `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 8.1M, `out` remained 122G, disk free was
  about 202Gi, and the 2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-071720/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer offscreen lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-offscreen parity-forced-context-graphics-layer-offscreen" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,024 JBR command frames,
  `avg_delta=1.909`, `bad_pixel_ratio=0.04583`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 1,241 JBR command frames, `avg_delta=2.050`,
  `bad_pixel_ratio=0.04821`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 8.1M, `out` was 122G, disk free was
  about 202Gi, and the 2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-071209/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer modulate-alpha lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer-modulate-alpha parity-forced-context-graphics-layer-modulate-alpha" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,244 JBR command frames,
  `avg_delta=2.265`, `bad_pixel_ratio=0.05718`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 909 JBR command frames, `avg_delta=2.479`,
  `bad_pixel_ratio=0.06186`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 8.0M, `out` was 122G, disk free was
  about 202Gi, and the 2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-070543/suite.tsv`.
- Magic Jewel added and refreshed the exact graphics-layer lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-graphics-layer parity-forced-context-graphics-layer" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,074 JBR command frames,
  `avg_delta=1.906`, `bad_pixel_ratio=0.04576`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 890 JBR command frames, `avg_delta=2.048`,
  `bad_pixel_ratio=0.04815`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 7.8M, `out` was 122G, disk free was
  about 202Gi, and the 2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-070019/suite.tsv`.
- Magic Jewel added and refreshed the exact lighting filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-lighting-filter parity-forced-context-lighting-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,213 JBR command frames,
  `avg_delta=1.919`, `bad_pixel_ratio=0.04643`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, eight effect-handle define frames, 2,103
  effect-handle use frames, and 2,095 effect-handle cache-hit frames. The forced-context row was fallback-free with
  1,312 JBR command frames, `avg_delta=2.063`, `bad_pixel_ratio=0.04891`, one destination context-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, 11 effect-handle
  define frames, 2,305 effect-handle use frames, and 2,294 effect-handle cache-hit frames. This was an exact two-row
  screenshot-parity refresh, not the full suite; output directory was 11M, `out` remained 121G, disk free was about
  202Gi, and the 2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-065454/suite.tsv`.
- Magic Jewel added and refreshed the exact color-matrix filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-color-matrix-filter parity-forced-context-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,058 JBR command frames,
  `avg_delta=1.919`, `bad_pixel_ratio=0.04643`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, eight effect-handle define frames, 1,983
  effect-handle use frames, and 1,975 effect-handle cache-hit frames. The forced-context row was fallback-free with
  836 JBR command frames, `avg_delta=2.063`, `bad_pixel_ratio=0.04891`, one destination context-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, nine image refs, 11 effect-handle
  define frames, 1,797 effect-handle use frames, and 1,786 effect-handle cache-hit frames. This was an exact two-row
  screenshot-parity refresh, not the full suite; output directory was 9.2M, `out` remained 121G, disk free was about
  203Gi, and the 2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-065010/suite.tsv`.
- Magic Jewel added and refreshed the exact skew-transform lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-skew-transform parity-forced-context-skew-transform" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 873 JBR command frames,
  `avg_delta=2.386`, `bad_pixel_ratio=0.06235`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, two image refs, and 40 effect-handle define frames. The
  forced-context row was fallback-free with 825 JBR command frames, `avg_delta=2.606`,
  `bad_pixel_ratio=0.06742`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, two image refs, and 60 effect-handle define frames. This was an exact two-row
  screenshot-parity refresh, not the full suite; output directory was 7.5M, `out` remained 121G, disk free was about
  203Gi, and the 2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-064326/suite.tsv`.
- Magic Jewel added and refreshed the exact point-dot lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-point-dots parity-forced-context-point-dots" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 790 JBR command frames,
  `avg_delta=1.854`, `bad_pixel_ratio=0.04465`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and 40 effect-handle define frames. The
  forced-context row was fallback-free with 570 JBR command frames, `avg_delta=1.981`,
  `bad_pixel_ratio=0.04668`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and 50 effect-handle define frames. This was an exact two-row
  screenshot-parity refresh, not the full suite; `out` remained 121G, disk free was about 203Gi, and the 2026-06-16
  daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-063729/suite.tsv`.
- Magic Jewel added and refreshed the exact blend-mode lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-blend-modes parity-forced-context-blend-modes" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 837 JBR command frames,
  `avg_delta=1.891`, `bad_pixel_ratio=0.04573`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 852 JBR command frames, `avg_delta=2.026`,
  `bad_pixel_ratio=0.04795`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; `out` remained 121G, disk free was about 176Gi, and the
  2026-06-16 daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-063229/suite.tsv`.
- Magic Jewel added and refreshed the exact vertices lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-vertices parity-forced-context-vertices" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 810 JBR command frames,
  `avg_delta=1.853`, `bad_pixel_ratio=0.04464`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and 35 effect-handle define frames. The
  forced-context row was fallback-free with 540 JBR command frames, `avg_delta=1.981`,
  `bad_pixel_ratio=0.04667`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and 50 effect-handle define frames. This was an exact two-row
  screenshot-parity refresh, not the full suite; `out` remained 121G, disk free was about 176Gi, and the 2026-06-16
  daily broad-validation stamp remained the compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-062716/suite.tsv`.
- Magic Jewel added and refreshed the exact path-effect lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-path-effect parity-forced-context-path-effect" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,234 JBR command frames,
  `avg_delta=1.924`, `bad_pixel_ratio=0.04656`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, nine image refs, and 40 effect-handle define frames. The
  forced-context row was fallback-free with 840 JBR command frames, `avg_delta=2.067`,
  `bad_pixel_ratio=0.04901`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, nine image refs, and 55 effect-handle define frames. This was an exact two-row
  screenshot-parity refresh, not the full suite; output directory was 7.9M, `out` remained 121G, and disk free was
  about 176Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-061714/suite.tsv`.
- Magic Jewel added and refreshed the exact image color-matrix filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-image-color-matrix-filter parity-forced-context-image-color-matrix-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 550 JBR command frames,
  `avg_delta=1.903`, `bad_pixel_ratio=0.04495`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, ten image refs, 42 effect-handle define frames, 1,237
  effect-handle use frames, and 1,230 effect-handle cache-hit frames. The forced-context row was fallback-free with
  842 JBR command frames, `avg_delta=2.039`, `bad_pixel_ratio=0.04703`, one destination context-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, ten image refs, 60 effect-handle
  define frames, 1,405 effect-handle use frames, and 1,395 effect-handle cache-hit frames. This was an exact two-row
  screenshot-parity refresh, not the full suite; output directory was 7.7M, `out` remained 121G, and disk free was
  about 176Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-061112/suite.tsv`.
- Magic Jewel added and refreshed the exact image-filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-image-filter parity-forced-context-image-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 665 JBR command frames,
  `avg_delta=1.853`, `bad_pixel_ratio=0.04463`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, ten image refs, and 35 effect-handle define frames. The
  forced-context row was fallback-free with 770 JBR command frames, `avg_delta=1.981`,
  `bad_pixel_ratio=0.04667`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, ten image refs, and 50 effect-handle define frames. This was an exact two-row
  screenshot-parity refresh, not the full suite; output directory was 6.5M, `out` remained 121G, and disk free was
  about 176Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-060436/suite.tsv`.
- Magic Jewel added and refreshed the exact gradient-stroke lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-gradient-stroke parity-forced-context-gradient-stroke" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 890 JBR command frames,
  `avg_delta=2.455`, `bad_pixel_ratio=0.06423`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, two image refs, and zero shader/effect handle markers. The
  forced-context row was fallback-free with 1,113 JBR command frames, `avg_delta=2.691`,
  `bad_pixel_ratio=0.06971`, one destination context-change marker, one command-cache clear, one JBR image-cache
  clear, one scoped image-cache clear, two image refs, and zero shader/effect handle markers. This was an exact
  two-row screenshot-parity refresh, not the full suite; output directory was 7.7M, `out` remained 121G, and disk free
  was about 176Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-055502/suite.tsv`.
- Magic Jewel added and refreshed the exact gradient-paths lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-gradient-paths parity-forced-context-gradient-paths" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 822 JBR command frames,
  `avg_delta=2.442`, `bad_pixel_ratio=0.06387`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, and two image refs. The forced-context row was fallback-free
  with 1,185 JBR command frames, `avg_delta=2.676`, `bad_pixel_ratio=0.06930`, one destination context-change marker,
  one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, and two image refs. This was an
  exact two-row screenshot-parity refresh, not the full suite; output directory was 7.8M, `out` remained 121G, and
  disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-054146/suite.tsv`.
- Magic Jewel added and refreshed the exact gradient-surfaces lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-gradient-surfaces parity-forced-context-gradient-surfaces" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,251 JBR command frames,
  `avg_delta=2.408`, `bad_pixel_ratio=0.06285`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, and two image refs. The forced-context row was fallback-free
  with 876 JBR command frames, `avg_delta=2.631`, `bad_pixel_ratio=0.06798`, one destination context-change marker,
  one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, and two image refs. This was an
  exact two-row screenshot-parity refresh, not the full suite; output directory was 8.1M, `out` remained 121G, and
  disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-053348/suite.tsv`.
- Magic Jewel added and refreshed the exact gradient-shaders lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-gradient-shaders parity-forced-context-gradient-shaders" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 1,063 JBR command frames,
  `avg_delta=2.433`, `bad_pixel_ratio=0.06358`, one same-context surface-change marker, one command-cache clear, one
  JBR image-cache clear, one scoped image-cache clear, and two image refs. The forced-context row was fallback-free
  with 1,099 JBR command frames, `avg_delta=2.655`, `bad_pixel_ratio=0.06872`, one destination context-change marker,
  one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, and two image refs. This was an
  exact two-row screenshot-parity refresh, not the full suite; output directory was 8.0M, `out` remained 121G, and
  disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-052738/suite.tsv`.
- Magic Jewel added and refreshed the exact image-shader lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-image-shader parity-forced-context-image-shader" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 844 JBR command frames,
  `avg_delta=2.447`, `bad_pixel_ratio=0.06268`, `compose_shader_image_bad_pixel_ratio=0.06647`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, three
  image refs, and 35 effect-handle define frames. The forced-context row was fallback-free with 835 JBR command
  frames, `avg_delta=2.678`, `bad_pixel_ratio=0.06777`, `compose_shader_image_bad_pixel_ratio=0.06209`, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  three image refs, and 60 effect-handle define frames. This was an exact two-row screenshot-parity refresh, not the
  full suite; output directory was 7.4M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-052105/suite.tsv`.
- Magic Jewel added and refreshed the exact plain composite-shader lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-composite-shader parity-forced-context-composite-shader" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 841 JBR command frames,
  `avg_delta=2.377`, `bad_pixel_ratio=0.06208`, `compose_shader_composite_bad_pixel_ratio=0.08637`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 27
  shader-handle define frames, 1,499 shader-handle use frames, 1,490 shader-handle cache-hit frames, and 35
  effect-handle define frames. The forced-context row was fallback-free with 842 JBR command frames,
  `avg_delta=2.596`, `bad_pixel_ratio=0.06710`, `compose_shader_composite_bad_pixel_ratio=0.08478`, one destination
  context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 33
  shader-handle define frames, 1,331 shader-handle use frames, 1,320 shader-handle cache-hit frames, and 55
  effect-handle define frames. This was an exact two-row screenshot-parity refresh, not the full suite; output
  directory was 7.9M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-051301/suite.tsv`.
- Magic Jewel added and refreshed the exact image-shader color-filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-image-shader-color-filter parity-forced-context-image-shader-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 788 JBR command frames,
  `avg_delta=1.846`, `bad_pixel_ratio=0.04439`, `compose_shader_image_bad_pixel_ratio=0.05606`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 14
  shader-handle define frames, 1,429 shader-handle use frames, 1,415 shader-handle cache-hit frames, 42 effect-handle
  define frames, and 7 effect-handle use frames. The forced-context row was fallback-free with 544 JBR command frames,
  `avg_delta=1.969`, `bad_pixel_ratio=0.04628`, `compose_shader_image_bad_pixel_ratio=0.03800`, one destination
  context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 18
  shader-handle define frames, 1,243 shader-handle use frames, 1,225 shader-handle cache-hit frames, 54 effect-handle
  define frames, and 9 effect-handle use frames. This was an exact two-row screenshot-parity refresh, not the full
  suite; output directory was 7.7M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-050348/suite.tsv`.
- Magic Jewel added and refreshed the exact composite-shader color-filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-composite-shader-color-filter parity-forced-context-composite-shader-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 792 JBR command frames,
  `avg_delta=1.841`, `bad_pixel_ratio=0.04428`, `compose_shader_composite_bad_pixel_ratio=0.07857`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 36
  shader-handle define frames, 1,487 shader-handle use frames, 1,469 shader-handle cache-hit frames, 42 effect-handle
  define frames, and 9 effect-handle use frames. The forced-context row was fallback-free with 452 JBR command frames,
  `avg_delta=1.962`, `bad_pixel_ratio=0.04616`, `compose_shader_composite_bad_pixel_ratio=0.05382`, one destination
  context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 36
  shader-handle define frames, 1,066 shader-handle use frames, 1,048 shader-handle cache-hit frames, 54 effect-handle
  define frames, and 9 effect-handle use frames. This was an exact two-row screenshot-parity refresh, not the full
  suite; output directory was 7.5M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-045823/suite.tsv`.
- Magic Jewel verified the daily broad-validation cap before continuing: default command-probe, screenshot-parity,
  compatibility-matrix, benchmark-suite, and artifact-matrix launches all exited 3 immediately against the existing
  2026-06-16 broad stamp consumed by the compatibility matrix, while an exact two-row `CASES=... LIST_CASE_COUNT=true`
  selection remained allowed. Magic Jewel then added and refreshed the exact transformed-shader lifecycle
  screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-transformed-shader parity-forced-context-transformed-shader" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 565 JBR command frames,
  `avg_delta=1.844`, `bad_pixel_ratio=0.04437`, `compose_bad_pixel_ratio=0.06442`, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 18 shader-handle define
  frames, 1,191 shader-handle use frames, 1,182 shader-handle cache-hit frames, and 35 effect-handle define frames.
  The forced-context row was fallback-free with 764 JBR command frames, `avg_delta=1.969`,
  `bad_pixel_ratio=0.04633`, `compose_bad_pixel_ratio=0.06885`, one destination context-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 18 shader-handle define frames, 1,327
  shader-handle use frames, 1,318 shader-handle cache-hit frames, and 45 effect-handle define frames. This was an
  exact two-row screenshot-parity refresh, not the full suite; output directory was 7.7M, `out` remained 121G, and
  disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-044958/suite.tsv`.
- Magic Jewel added and refreshed the exact sweep-gradient shader color-filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-sweep-gradient-shader-color-filter parity-forced-context-sweep-gradient-shader-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 717 JBR command frames,
  `avg_delta=1.841`, `bad_pixel_ratio=0.04428`, `compose_shader_linear_bad_pixel_ratio=0.06647`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 18
  shader-handle define frames, 1,414 shader-handle use frames, 1,396 shader-handle cache-hit frames, 42 effect-handle
  define frames, and 9 effect-handle use frames. The forced-context row was fallback-free with 654 JBR command frames,
  `avg_delta=1.970`, `bad_pixel_ratio=0.04634`, `compose_shader_linear_bad_pixel_ratio=0.06844`, one destination
  context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 18
  shader-handle define frames, 1,177 shader-handle use frames, 1,159 shader-handle cache-hit frames, 54 effect-handle
  define frames, and 9 effect-handle use frames. This was an exact two-row screenshot-parity refresh, not the full
  suite; output directory was 7.7M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-044036/suite.tsv`.
- Magic Jewel added and refreshed the exact radial-gradient shader color-filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-radial-gradient-shader-color-filter parity-forced-context-radial-gradient-shader-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 737 JBR command frames,
  `avg_delta=1.842`, `bad_pixel_ratio=0.04431`, `compose_shader_linear_bad_pixel_ratio=0.06647`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 18
  shader-handle define frames, 1,353 shader-handle use frames, 1,335 shader-handle cache-hit frames, 42 effect-handle
  define frames, and 9 effect-handle use frames. The forced-context row was fallback-free with 555 JBR command frames,
  `avg_delta=1.968`, `bad_pixel_ratio=0.04630`, `compose_shader_linear_bad_pixel_ratio=0.06844`, one destination
  context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 18
  shader-handle define frames, 1,158 shader-handle use frames, 1,140 shader-handle cache-hit frames, 54 effect-handle
  define frames, and 9 effect-handle use frames. This was an exact two-row screenshot-parity refresh, not the full
  suite; output directory was 7.6M, `out` remained 121G, and disk free was about 176Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-043542/suite.tsv`.
- Magic Jewel added and refreshed the exact linear-gradient shader color-filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-linear-gradient-shader-color-filter parity-forced-context-linear-gradient-shader-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 553 JBR command frames,
  `avg_delta=1.841`, `bad_pixel_ratio=0.04428`, `compose_shader_linear_bad_pixel_ratio=0.05656`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 20
  shader-handle define frames, 1,224 shader-handle use frames, 1,204 shader-handle cache-hit frames, 48 effect-handle
  define frames, and 10 effect-handle use frames. The forced-context row was fallback-free with 796 JBR command frames,
  `avg_delta=1.966`, `bad_pixel_ratio=0.04623`, `compose_shader_linear_bad_pixel_ratio=0.03938`, one destination
  context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 18
  shader-handle define frames, 1,500 shader-handle use frames, 1,482 shader-handle cache-hit frames, 54 effect-handle
  define frames, and 9 effect-handle use frames. This was an exact two-row screenshot-parity refresh, not the full
  suite; output directory was 7.6M, `out` remained 121G, and disk free was about 176Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-043012/suite.tsv`.
- Magic Jewel added and refreshed the exact RuntimeEffect color-filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-runtime-effect-color-filter parity-forced-context-runtime-effect-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 545 JBR command frames,
  `avg_delta=1.853`, `bad_pixel_ratio=0.04463`, `compose_bad_pixel_ratio=0.06491`, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,198 RuntimeEffect
  source-cache hit frames, one miss, 42 effect-handle define frames, 1,200 effect-handle use frames, and 1,193
  effect-handle cache-hit frames. The forced-context row was fallback-free with 810 JBR command frames,
  `avg_delta=1.979`, `bad_pixel_ratio=0.04663`, `compose_bad_pixel_ratio=0.06934`, one destination context-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,385 source-cache hit
  frames, one miss, 54 effect-handle define frames, 1,386 effect-handle use frames, and 1,377 effect-handle cache-hit
  frames. This was an exact two-row screenshot-parity refresh, not the full suite; output directory was 7.8M, `out`
  remained 121G, and disk free was about 176Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-041714/suite.tsv`.
- Magic Jewel added and refreshed the exact RuntimeEffect shader-color-filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-runtime-effect-shader-color-filter parity-forced-context-runtime-effect-shader-color-filter" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 812 JBR command frames,
  `avg_delta=1.841`, `bad_pixel_ratio=0.04428`, `compose_bad_pixel_ratio=0.06426`, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,412 RuntimeEffect
  source-cache hit frames, one miss, 14 shader-handle define frames, 1,421 shader-handle use frames, 1,407
  shader-handle cache-hit frames, 42 effect-handle define frames, and 7 effect-handle use frames. The forced-context
  row was fallback-free with 806 JBR command frames, `avg_delta=1.969`, `bad_pixel_ratio=0.04632`,
  `compose_bad_pixel_ratio=0.06882`, one destination context-change marker, one command-cache clear, one JBR image
  cache clear, one scoped image-cache clear, 1,444 source-cache hit frames, one miss, 18 shader-handle define frames,
  1,454 shader-handle use frames, 1,436 shader-handle cache-hit frames, 54 effect-handle define frames, and 9
  effect-handle use frames. This was an exact two-row screenshot-parity refresh, not the full suite; output directory
  was 8.1M, `out` remained 121G, and disk free was about 176Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-041113/suite.tsv`.
- Magic Jewel added and refreshed the exact plain RuntimeEffect shader lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-runtime-effect-shader parity-forced-context-runtime-effect-shader" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 536 JBR command frames,
  `avg_delta=1.841`, `bad_pixel_ratio=0.04428`, `compose_bad_pixel_ratio=0.06426`, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,158 RuntimeEffect
  source-cache hit frames, one miss, 30 shader-handle define frames, 1,160 shader-handle use frames, 1,150
  shader-handle cache-hit frames, and 40 effect-handle define frames. The forced-context row was fallback-free with
  550 JBR command frames, `avg_delta=1.970`, `bad_pixel_ratio=0.04634`, `compose_bad_pixel_ratio=0.06886`, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  1,108 source-cache hit frames, one miss, 27 shader-handle define frames, 1,109 shader-handle use frames, 1,100
  shader-handle cache-hit frames, and 45 effect-handle define frames. This was an exact two-row screenshot-parity
  refresh, not the full suite; output directory was 7.6M, `out` remained 121G, and disk free was about 176Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-040432/suite.tsv`.
- Magic Jewel added and refreshed the exact RuntimeEffect child-only lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-runtime-effect-child-only parity-forced-context-runtime-effect-child-only" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 815 JBR command frames,
  `avg_delta=1.847`, `bad_pixel_ratio=0.04446`, `compose_bad_pixel_ratio=0.06456`, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,423 RuntimeEffect
  source-cache hit frames, one miss, 27 shader-handle define frames, 1,425 shader-handle use frames, 1,416
  shader-handle cache-hit frames, and 35 effect-handle define frames. The forced-context row was fallback-free with
  835 JBR command frames, `avg_delta=1.977`, `bad_pixel_ratio=0.04655`, `compose_bad_pixel_ratio=0.06921`, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  1,459 source-cache hit frames, one miss, 27 shader-handle define frames, 1,460 shader-handle use frames, 1,451
  shader-handle cache-hit frames, and 45 effect-handle define frames. This was an exact two-row screenshot-parity
  refresh, not the full suite; output directory was 8.2M, `out` remained 121G, and disk free was about 176Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-035554/suite.tsv`.
- Magic Jewel added and refreshed the exact RuntimeEffect uniform-only lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-runtime-effect-uniform-only parity-forced-context-runtime-effect-uniform-only" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the known
  single early `command-stream-invalid` resize parity artifact, then passed with 787 JBR command frames,
  `avg_delta=1.844`, `bad_pixel_ratio=0.04435`, `compose_bad_pixel_ratio=0.06437`, one same-context surface-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,420 RuntimeEffect
  source-cache hit frames, one miss, 8 shader-handle define frames, 1,422 shader-handle use frames, and 1,414
  shader-handle cache-hit frames. The forced-context row was fallback-free with 791 JBR command frames,
  `avg_delta=1.966`, `bad_pixel_ratio=0.04623`, `compose_bad_pixel_ratio=0.06867`, one destination context-change
  marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,483 source-cache hit
  frames, one miss, 10 shader-handle define frames, 1,484 shader-handle use frames, and 1,474 shader-handle cache-hit
  frames. This was an exact two-row screenshot-parity refresh, not the full suite; output directory was 8.1M, `out`
  remained 121G, and disk free was about 177Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-034556/suite.tsv`.
- Magic Jewel added and refreshed the exact RuntimeEffect child color-filter lifecycle screenshot-parity pair:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="parity-resize-runtime-effect-color-filter-child parity-forced-context-runtime-effect-color-filter-child" ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 2/2 with zero picture frames, command replay, and screenshot status passed. The resize row recorded the same
  single early `command-stream-invalid` fallback artifact seen on the other RuntimeEffect resize parity rows, then
  passed with 827 JBR command frames, `avg_delta=1.853`, `bad_pixel_ratio=0.04463`,
  `compose_bad_pixel_ratio=0.06492`, one same-context surface-change marker, one command-cache clear, one JBR image
  cache clear, one scoped image-cache clear, 1,489 RuntimeEffect source-cache hit frames, one miss, 64 effect-handle
  define frames, 1,491 effect-handle use frames, and 1,483 effect-handle cache-hit frames. The forced-context row was
  fallback-free with 794 JBR command frames, `avg_delta=1.981`, `bad_pixel_ratio=0.04667`,
  `compose_bad_pixel_ratio=0.06944`, one destination context-change marker, one command-cache clear, one JBR image
  cache clear, one scoped image-cache clear, 1,485 source-cache hit frames, one miss, 80 effect-handle define frames,
  1,486 effect-handle use frames, and 1,476 cache-hit frames. This was an exact two-row screenshot-parity refresh, not
  the full suite; output directory was 8.1M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-034035/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect source-cache eviction command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-source-cache-eviction commands-runtime-effect-source-cache-eviction" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 on command replay with `fallback_new_count=0`, `unsupported=none`, zero picture frames, and screenshot
  status passed. The shader source-cache eviction row recorded 772 CMP/Skiko/JBR command frames, 1,358 RuntimeEffect
  source-cache hits, 2,719 misses, 2,717 evicts, 1,363 shader-handle definitions, 4,077 shader-handle uses, 339
  shader-handle evicts, and 2,716 shader-handle cache-hit frames. The color-filter source-cache eviction row recorded
  784 CMP recorder frames, 785 Skiko/JBR command frames, 1,336 RuntimeEffect source-cache hits, 2,675 misses, 2,673
  evicts, 2,677 effect-handle definitions, 4,011 effect-handle uses, 1,653 effect-handle evicts, and 1,336
  effect-handle cache-hit frames. This was an exact two-row command-probe refresh, not the full suite; output directory
  was 11M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-033121/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect invalid nested-child descriptor fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-invalid-nested-child-fallback commands-runtime-effect-color-filter-invalid-nested-child-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 as descriptor-unsupported picture fallback rows, not command-stream rows. The shader row recorded
  `unsupported=shaderDescriptor:538,graphicsLayer:childCommands:538,graphicsLayer:538`, 538 CMP recorder frames, 537
  Skiko/JBR picture frames, zero command frames, zero RuntimeEffect compile/build failures, and screenshot status
  passed. The color-filter row recorded
  `unsupported=colorFilterDescriptor:504,graphicsLayer:childCommands:504,graphicsLayer:504`, 504 CMP recorder frames,
  504 Skiko/JBR picture frames, zero command frames, zero compile/build failures, and screenshot status passed. This
  was an exact two-row command-probe refresh, not the full suite; output directory was 3.9M, `out` remained 121G, and
  disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-032745/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect invalid child-schema descriptor fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-invalid-child-schema-fallback commands-runtime-effect-color-filter-invalid-child-schema-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 as descriptor-unsupported picture fallback rows, not command-stream rows. The shader row recorded
  `unsupported=shaderDescriptor:526,graphicsLayer:childCommands:526,graphicsLayer:526`, 526 CMP recorder frames, 526
  Skiko/JBR picture frames, zero command frames, zero RuntimeEffect compile/build failures, and screenshot status
  passed. The color-filter row recorded
  `unsupported=colorFilterDescriptor:568,graphicsLayer:childCommands:568,graphicsLayer:568`, 568 CMP recorder frames,
  567 Skiko/JBR picture frames, zero command frames, zero compile/build failures, and screenshot status passed. This
  was an exact two-row command-probe refresh, not the full suite; output directory was 3.9M, `out` remained 121G, and
  disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-032451/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect invalid uniform-schema descriptor fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-invalid-uniform-schema-fallback commands-runtime-effect-color-filter-invalid-uniform-schema-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 as descriptor-unsupported picture fallback rows, not command-stream rows. The shader row recorded
  `unsupported=shaderDescriptor:487,graphicsLayer:childCommands:487,graphicsLayer:487`, 487 CMP recorder frames, 488
  Skiko/JBR picture frames, zero command frames, zero RuntimeEffect compile/build failures, and screenshot status
  passed. The color-filter row recorded
  `unsupported=colorFilterDescriptor:521,graphicsLayer:childCommands:521,graphicsLayer:521`, 521 CMP recorder frames,
  521 Skiko/JBR picture frames, zero command frames, zero compile/build failures, and screenshot status passed. This
  was an exact two-row command-probe refresh, not the full suite; output directory was 3.9M, `out` remained 121G, and
  disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-032209/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect duplicate-child-index schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-duplicate-child-index-fallback commands-runtime-effect-color-filter-duplicate-child-index-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader duplicate-child-index row recorded 734 CMP/Skiko command frames,
  zero RuntimeEffect compile/build failures, and zero RuntimeEffect cache hit/miss frames before structural rejection.
  The color-filter row recorded 641 CMP frames, 640 Skiko command frames, zero compile/build failures, 1,070
  RuntimeEffect cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe
  refresh, not the full suite; output directory was 5.5M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-031854/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect negative-child-index schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-negative-child-index-fallback commands-runtime-effect-color-filter-negative-child-index-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader negative-child-index row recorded 680 CMP/Skiko command frames,
  zero RuntimeEffect compile/build failures, and zero RuntimeEffect cache hit/miss frames before structural rejection.
  The color-filter row recorded 425 CMP frames, 424 Skiko command frames, zero compile/build failures, 841
  RuntimeEffect cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe
  refresh, not the full suite; output directory was 4.1M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-031617/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect child-index schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-child-index-fallback commands-runtime-effect-color-filter-child-index-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader child-index row recorded 620 CMP/Skiko command frames, zero
  RuntimeEffect compile/build failures, and zero RuntimeEffect cache hit/miss frames before structural rejection. The
  color-filter row recorded 567 CMP frames, 566 Skiko command frames, zero compile/build failures, 928 RuntimeEffect
  cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe refresh, not the
  full suite; output directory was 4.1M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-031349/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect child-schema-name-range schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-child-schema-name-range-fallback commands-runtime-effect-color-filter-child-schema-name-range-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader child-schema-name-range row recorded 749 CMP/Skiko command frames,
  zero RuntimeEffect compile/build failures, and zero RuntimeEffect cache hit/miss frames before structural rejection.
  The color-filter row recorded 650 CMP/Skiko command frames, zero compile/build failures, 1,066 RuntimeEffect
  cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe refresh, not the
  full suite; output directory was 5.5M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-031116/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect child-schema-max-name-length schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-child-schema-max-name-length-fallback commands-runtime-effect-color-filter-child-schema-max-name-length-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader child-schema-max-name-length row recorded 777 CMP/Skiko command
  frames, zero RuntimeEffect compile/build failures, and zero RuntimeEffect cache hit/miss frames before structural
  rejection. The color-filter row recorded 659 CMP/Skiko command frames, zero compile/build failures, 1,081
  RuntimeEffect cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe
  refresh, not the full suite; output directory was 6.6M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-030834/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect child-schema-name-length schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-child-schema-name-length-fallback commands-runtime-effect-color-filter-child-schema-name-length-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader child-schema-name-length row recorded 835 CMP frames, 834 Skiko
  command frames, zero RuntimeEffect compile/build failures, and zero RuntimeEffect cache hit/miss frames before
  structural rejection. The color-filter row recorded 576 CMP/Skiko command frames, zero compile/build failures, 957
  RuntimeEffect cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe
  refresh, not the full suite; output directory was 5.5M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-030533/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect child-name schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-child-name-fallback commands-runtime-effect-color-filter-child-name-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader child-name row recorded 725 CMP/Skiko command frames, zero
  RuntimeEffect compile/build failures, and zero RuntimeEffect cache hit/miss frames before structural rejection. The
  color-filter row recorded 621 CMP/Skiko command frames, zero compile/build failures, 1,005 RuntimeEffect cache-hit
  frames, and one miss before structural rejection. This was an exact two-row command-probe refresh, not the full
  suite; output directory was 4.3M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-030136/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect uniform-schema-name-range schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-schema-name-range-fallback commands-runtime-effect-color-filter-uniform-schema-name-range-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader uniform-schema-name-range row recorded 753 CMP/Skiko command
  frames, zero RuntimeEffect compile/build failures, 1,166 RuntimeEffect cache-hit frames, and one miss before
  structural rejection. The color-filter row recorded 705 CMP/Skiko command frames, zero compile/build failures, 1,138
  cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe refresh, not the
  full suite; output directory was 6.6M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-025851/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect uniform-schema-max-name-length schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-schema-max-name-length-fallback commands-runtime-effect-color-filter-uniform-schema-max-name-length-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader uniform-schema-max-name-length row recorded 509 CMP/Skiko command
  frames, zero RuntimeEffect compile/build failures, 943 RuntimeEffect cache-hit frames, and one miss before structural
  rejection. The color-filter row recorded 673 CMP/Skiko command frames, zero compile/build failures, 1,170 cache-hit
  frames, and one miss before structural rejection. This was an exact two-row command-probe refresh, not the full
  suite; output directory was 5.3M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-025541/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect uniform-schema-name-length schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-schema-name-length-fallback commands-runtime-effect-color-filter-uniform-schema-name-length-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader uniform-schema-name-length row recorded 752 CMP/Skiko command
  frames, zero RuntimeEffect compile/build failures, 1,238 RuntimeEffect cache-hit frames, and one miss before
  structural rejection. The color-filter row recorded 653 CMP/Skiko command frames, zero compile/build failures, 1,103
  cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe refresh, not the
  full suite; output directory was 6.6M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-025256/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect uniform-schema-float-range schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-schema-float-range-fallback commands-runtime-effect-color-filter-uniform-schema-float-range-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader uniform-schema-float-range row recorded 611 CMP/Skiko command
  frames, zero RuntimeEffect compile/build failures, 1,097 RuntimeEffect cache-hit frames, and one miss before
  structural rejection. The color-filter row recorded 710 CMP/Skiko command frames, zero compile/build failures, 1,156
  cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe refresh, not the
  full suite; output directory was 6.6M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-024949/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect uniform-schema-float-offset schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-schema-float-offset-fallback commands-runtime-effect-color-filter-uniform-schema-float-offset-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader uniform-schema-float-offset row recorded 656 CMP/Skiko command
  frames, zero RuntimeEffect compile/build failures, 1,043 RuntimeEffect cache-hit frames, and one miss before
  structural rejection. The color-filter row recorded 766 CMP/Skiko command frames, zero compile/build failures, 1,209
  cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe refresh, not the
  full suite; output directory was 5.6M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-024712/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect uniform-schema-float-count schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-schema-float-count-fallback commands-runtime-effect-color-filter-uniform-schema-float-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader uniform-schema-float-count row recorded 709 CMP/Skiko command
  frames, zero RuntimeEffect compile/build failures, 1,199 RuntimeEffect cache-hit frames, and one miss before
  structural rejection. The color-filter row recorded 715 CMP/Skiko command frames, zero compile/build failures, 1,206
  cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe refresh, not the
  full suite; output directory was 6.7M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-024354/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect uniform-name schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-name-fallback commands-runtime-effect-color-filter-uniform-name-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader uniform-name row recorded 680 CMP/Skiko command frames, zero
  RuntimeEffect compile/build failures, 1,127 RuntimeEffect cache-hit frames, and one miss before structural rejection.
  The color-filter row recorded 608 CMP frames, 609 Skiko command frames, zero compile/build failures, 1,020 cache-hit
  frames, and one miss before structural rejection. This was an exact two-row command-probe refresh, not the full
  suite; output directory was 5.5M, `out` remained 121G, and disk free was about 203Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-024119/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect negative named-child-count schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-negative-named-child-count-fallback commands-runtime-effect-color-filter-negative-named-child-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader negative named-child-count row recorded 714 CMP/Skiko command
  frames, zero RuntimeEffect compile/build failures, and zero RuntimeEffect cache hit/miss frames before structural
  rejection. The color-filter row recorded 495 CMP frames, 496 Skiko command frames, zero compile/build failures, 949
  RuntimeEffect cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe
  refresh, not the full suite; output directory was 4.1M, `out` remained 121G, and disk free was about 177Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-023821/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect named-child-count schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-named-child-count-fallback commands-runtime-effect-color-filter-named-child-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader named-child-count row recorded 543 CMP/Skiko command frames, zero
  RuntimeEffect compile/build failures, and zero RuntimeEffect cache hit/miss frames before structural rejection. The
  color-filter row recorded 698 CMP/Skiko command frames, zero compile/build failures, 1,114 RuntimeEffect cache-hit
  frames, and one miss before structural rejection. This was an exact two-row command-probe refresh, not the full
  suite; output directory was 5.1M, `out` remained 121G, and disk free was about 177Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-023316/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect negative named-uniform-count schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-negative-named-uniform-count-fallback commands-runtime-effect-color-filter-negative-named-uniform-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader negative named-uniform-count row recorded 685 CMP/Skiko command
  frames, zero RuntimeEffect compile/build failures, 1,163 RuntimeEffect cache-hit frames, and one miss before
  structural rejection. The color-filter row recorded 853 CMP frames, 852 Skiko command frames, zero compile/build
  failures, 1,372 cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe
  refresh, not the full suite; output directory was 6.9M, `out` remained 121G, and disk free was about 177Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-022951/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect named-uniform-count schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-named-uniform-count-fallback commands-runtime-effect-color-filter-named-uniform-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader named-uniform-count row recorded 664 CMP frames, 663 Skiko command
  frames, zero RuntimeEffect compile/build failures, 1,073 RuntimeEffect cache-hit frames, and one miss before
  structural rejection. The color-filter row recorded 749 CMP frames, 750 Skiko command frames, zero compile/build
  failures, 1,164 cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe
  refresh, not the full suite; output directory was 6.6M, `out` remained 121G, and disk free was about 173Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-022648/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect negative child-count schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-negative-child-count-fallback commands-runtime-effect-color-filter-negative-child-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader negative child-count row recorded 759 CMP/Skiko command frames,
  zero RuntimeEffect compile/build failures, and zero RuntimeEffect cache hit/miss frames before structural rejection.
  The color-filter row recorded 548 CMP/Skiko command frames, zero compile/build failures, 948 RuntimeEffect cache-hit
  frames, and one miss before structural rejection. This was an exact two-row command-probe refresh, not the full
  suite; output directory was 4.4M, `out` remained 121G, and disk free was about 173Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-022401/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect child-count schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-child-count-fallback commands-runtime-effect-color-filter-child-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader child-count row recorded 650 CMP/Skiko command frames, zero
  RuntimeEffect compile/build failures, and zero RuntimeEffect cache hit/miss frames before structural rejection. The
  color-filter row recorded 671 CMP/Skiko command frames, zero compile/build failures, 1,096 RuntimeEffect cache-hit
  frames, and one miss before structural rejection. This was an exact two-row command-probe refresh, not the full
  suite; output directory was 5.2M, `out` remained 121G, and disk free was about 173Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-022043/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect negative uniform-float-count schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-negative-uniform-float-count-fallback commands-runtime-effect-color-filter-negative-uniform-float-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader negative uniform-count row recorded 730 CMP/Skiko command frames,
  zero RuntimeEffect compile/build failures, 1,285 RuntimeEffect cache-hit frames, and one miss before structural
  rejection. The color-filter row recorded 704 CMP/Skiko command frames, zero compile/build failures, 1,057 cache-hit
  frames, and one miss before structural rejection. This was an exact two-row command-probe refresh, not the full
  suite; output directory was 6.6M, `out` remained 121G, and disk free was about 173Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-021808/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect uniform-float-count schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-uniform-float-count-fallback commands-runtime-effect-color-filter-uniform-float-count-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader uniform-float-count row recorded 625 CMP frames, 626 Skiko command
  frames, zero RuntimeEffect compile/build failures, 1,041 RuntimeEffect cache-hit frames, and one miss before
  structural rejection. The color-filter row recorded 732 CMP frames, 731 Skiko command frames, zero compile/build
  failures, 1,150 cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe
  refresh, not the full suite; output directory was 5.5M, `out` remained 121G, and disk free was about 173Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-021442/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect SKSL-length schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-sksl-length-fallback commands-runtime-effect-color-filter-sksl-length-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader SKSL-length row recorded 651 CMP frames, 652 Skiko command frames,
  zero RuntimeEffect compile/build failures, and zero RuntimeEffect cache hit/miss frames before structural rejection.
  The color-filter SKSL-length row recorded 542 CMP frames, 541 Skiko command frames, zero compile/build failures, 919
  RuntimeEffect cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe
  refresh, not the full suite; output directory was 4.1M, `out` remained 121G, and disk free was about 173Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-021155/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect source-code schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-source-code-fallback commands-runtime-effect-color-filter-source-code-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader source-code row recorded 707 CMP frames, 708 Skiko command frames,
  zero RuntimeEffect compile/build failures, and zero RuntimeEffect cache hit/miss frames before structural rejection.
  The color-filter source-code row recorded 683 CMP/Skiko command frames, zero compile/build failures, 1,138
  RuntimeEffect cache-hit frames, and one miss before structural rejection. This was an exact two-row command-probe
  refresh, not the full suite; output directory was 5.4M, `out` remained 121G, and disk free was about 173Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-020843/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect source-hash schema fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-shader-source-hash-fallback commands-runtime-effect-color-filter-source-hash-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `command-stream-invalid` fallback per row, `unsupported=none`, zero JBR command/picture
  frames, and screenshot status passed. The shader source-hash row recorded 505 CMP/Skiko command frames, zero
  RuntimeEffect compile/build failures, and zero RuntimeEffect cache hit/miss frames because replay was structurally
  rejected before JBR RuntimeEffect build. The color-filter source-hash row recorded 659 CMP frames, 660 Skiko command
  frames, zero compile/build failures, 1,101 RuntimeEffect cache-hit frames, and one miss before the expected structural
  fallback. This was an exact two-row command-probe refresh, not the full suite; output directory was 5.1M, `out`
  remained 121G, and disk free was about 173Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-020618/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect child-type fallback command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-child-type-fallback commands-runtime-effect-color-filter-child-type-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with one expected `runtime-effect-build-failed` fallback per row, `unsupported=none`, zero JBR
  command/picture frames, and screenshot capture not run. The shader child-type row recorded 694 CMP/Skiko command
  frames, one RuntimeEffect build failure, zero compile failures, 7,489 RuntimeEffect cache-hit frames, two misses, and
  `stage=child-type`. The color-filter child-type row recorded 1,195 CMP/Skiko command frames, one RuntimeEffect build
  failure, zero compile failures, 7,696 cache-hit frames, two misses, and `stage=positional-child-type`. This was an
  exact two-row command-probe refresh, not the full suite; output directory was 20M, `out` remained 121G, and disk free
  was about 173Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-020138/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect compile/build fallback command-probe subset:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-runtime-effect-compile-fallback commands-runtime-effect-build-fallback commands-runtime-effect-color-filter-compile-fallback commands-runtime-effect-color-filter-build-fallback" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 4/4 with one expected fallback per row, `unsupported=none`, zero JBR command/picture frames, and screenshot
  capture not run. The shader compile row recorded 1,216 CMP/Skiko frames, one compile failure, zero build failures,
  and 7,693 RuntimeEffect cache-hit frames with one miss. The shader build row recorded 810 CMP frames, 809 Skiko
  frames, zero compile failures, 7,164 build failures, 7,163 cache-hit frames with one miss, and
  `stage=missing-child`. The color-filter compile row recorded 767 CMP frames, 766 Skiko frames, one compile failure,
  zero build failures, and 8,197 cache-hit frames with one miss. The color-filter build row recorded 1,214 CMP/Skiko
  frames, zero compile failures, 8,072 build failures, 8,071 cache-hit frames with one miss, and `stage=child-count`.
  This was an exact four-row command-probe refresh, not the full suite; output directory was 43M, `out` remained 121G,
  and disk free was about 173Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-015112/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect child-only resize/forced-context command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-resize-runtime-effect-child-only commands-forced-context-runtime-effect-child-only" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0` and `unsupported=none` for both rows. The resize row recorded 819 CMP
  recorder frames, 820 Skiko/JBR command frames, `jbr_command_fps=164.0`, one same-context surface-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,478 RuntimeEffect source-cache hit
  frames, one miss, 9 shader-handle define frames, and 1,479 shader-handle use frames. The forced-context row recorded
  536 CMP recorder frames, 535 Skiko command frames, 536 JBR command frames, `jbr_command_fps=107.2`, one destination
  context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,148
  RuntimeEffect source-cache hit frames, one miss, 6 shader-handle define frames, and 1,149 shader-handle use frames.
  This was an exact two-row command-probe refresh, not the full suite; output directory was 6.8M, `out` remained 121G,
  and disk free was about 174Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-014718/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect uniform-only resize/forced-context command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-resize-runtime-effect-uniform-only commands-forced-context-runtime-effect-uniform-only" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0` and `unsupported=none` for both rows. The resize row recorded 826 CMP
  recorder frames, 825 Skiko/JBR command frames, `jbr_command_fps=165.0`, one same-context surface-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,408 RuntimeEffect source-cache hit
  frames, one miss, and 1,409 shader-handle define/use frames. The forced-context row recorded 817 CMP recorder
  frames, 818 Skiko/JBR command frames, `jbr_command_fps=163.6`, one destination context-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,415 RuntimeEffect source-cache hit
  frames, one miss, 1,415 shader-handle define frames, and 1,416 shader-handle use frames. This was an exact two-row
  command-probe refresh, not the full suite; output directory was 7.2M and `out` remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-014346/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect shader resize/forced-context command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-resize-runtime-effect-shader commands-forced-context-runtime-effect-shader" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0` and `unsupported=none` for both rows. The resize row recorded 610 CMP
  recorder frames, 611 Skiko/JBR command frames, `jbr_command_fps=122.2`, one same-context surface-change marker, one
  command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,146 RuntimeEffect source-cache hit
  frames, one miss, 1,152 shader-handle define frames, and 1,147 shader-handle use frames. The forced-context row
  recorded 794 CMP recorder frames, 793 Skiko/JBR command frames, `jbr_command_fps=158.6`, one destination
  context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,350
  RuntimeEffect source-cache hit frames, one miss, 1,355 shader-handle define frames, and 1,351 shader-handle use
  frames. This was an exact two-row command-probe refresh, not the full suite; output directory was 6.9M and `out`
  remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-014003/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect shader-plus-color-filter resize/forced-context command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-resize-runtime-effect-shader-color-filter commands-forced-context-runtime-effect-shader-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0` and `unsupported=none` for both rows. The resize row recorded 822 CMP
  recorder frames, 821 Skiko command frames, 822 JBR command frames, `jbr_command_fps=164.4`, one same-context
  surface-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear, 1,464
  RuntimeEffect source-cache hit frames, one miss, 2 effect-handle define frames, 1,464 effect-handle use frames, 1,462
  effect-handle cache-hit frames, 2,928 shader-handle define frames, and 2,929 shader-handle use frames. The
  forced-context row recorded 818 CMP recorder frames, 819 Skiko/JBR command frames, `jbr_command_fps=163.8`, one
  destination context-change marker, one command-cache clear, one JBR image-cache clear, one scoped image-cache clear,
  1,502 RuntimeEffect source-cache hit frames, one miss, 2 effect-handle define frames, 1,503 effect-handle use frames,
  1,501 effect-handle cache-hit frames, and 3,006 shader-handle define/use frames. This was an exact two-row
  command-probe refresh, not the full suite; output directory was 10M and `out` remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-013644/suite.tsv`.
- Magic Jewel refreshed the exact RuntimeEffect color-filter resize/forced-context command-probe pair:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES="commands-resize-runtime-effect-color-filter commands-forced-context-runtime-effect-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with `fallback_new_count=0` and `unsupported=none` for both rows. The resize row recorded 810
  CMP/Skiko/JBR command frames, `jbr_command_fps=162.0`, one same-context surface-change marker, one command-cache
  clear, one JBR image-cache clear, one scoped image-cache clear, 1,493 RuntimeEffect source-cache hit frames, one
  miss, and 1,494 effect-handle define/use frames. The forced-context row recorded 826 CMP/Skiko/JBR command frames,
  `jbr_command_fps=165.2`, one destination context-change marker, one command-cache clear, one JBR image-cache clear,
  one scoped image-cache clear, 1,421 RuntimeEffect source-cache hit frames, one miss, and 1,423 effect-handle
  define/use frames. This was an exact two-row command-probe refresh, not the full suite; output directory was 7.3M and
  `out` remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-012445/suite.tsv`.
- Magic Jewel ran an exact RuntimeEffect color-filter child forced-context command-probe smoke:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=commands-forced-context-runtime-effect-color-filter-child ./scripts/jbr-skia-command-probe-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 666 CMP/Skiko/JBR command frames, zero picture frames,
  `jbr_command_fps=133.2`, one destination context-change marker, one command-cache clear, one JBR image-cache clear,
  one scoped image-cache clear, 1,244 RuntimeEffect source-cache hit frames, one miss, 1,247 effect-handle define
  frames, and 1,245 effect-handle use frames. This pairs with the exact resize child command-probe row while
  screenshot-parity child resize/forced-context rows remain unimplemented. Output directory was 3.4M and `out`
  remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-012135/suite.tsv`.
- Magic Jewel ran an exact RuntimeEffect color-filter child resize command-probe smoke:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=commands-resize-runtime-effect-color-filter-child ./scripts/jbr-skia-command-probe-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 779 CMP/Skiko/JBR command frames, zero picture frames,
  `jbr_command_fps=155.8`, one same-context surface-change marker, one command-cache clear, one JBR image-cache clear,
  one scoped image-cache clear, 1,429 RuntimeEffect source-cache hit frames, one miss, 1,434 effect-handle define
  frames, and 1,430 effect-handle use frames. The screenshot-parity suite currently implements the base
  `parity-runtime-effect-color-filter-child` row but not resize/forced-context child parity rows, so this exact
  command-probe row is the current focused lifecycle gate for the child color-filter resize path. Output directory was
  3.5M and `out` remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-011921/suite.tsv`.
- Magic Jewel ran an exact RuntimeEffect stable color-filter forced-context screenshot-parity smoke:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-forced-context-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 801 CMP recorder frames, 801 Skiko/JBR command frames,
  zero picture frames, screenshot status passed, `avg_delta=1.981`, `bad_pixel_ratio=0.04668`,
  `compose_bad_pixel_ratio=0.06943`, one destination context-change marker, one command-cache clear, one JBR
  image-cache clear, one scoped image-cache clear, 1,469 RuntimeEffect source-cache hit frames, one miss, 54
  effect-handle define frames, and 1,470 effect-handle use frames. This was an exact one-row parity smoke, not the
  full screenshot-parity suite; the output directory was 4.0M and `out` remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-011609/suite.tsv`.
- Magic Jewel ran a paired exact RuntimeEffect stable color-filter resize validation under the daily cap. The
  screenshot-parity row
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-resize-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with screenshot status passed, 835 CMP recorder frames, 836 Skiko/JBR command frames, zero picture
  frames, `avg_delta=1.855`, `bad_pixel_ratio=0.04467`, `compose_bad_pixel_ratio=0.06492`, 1,536 RuntimeEffect
  source-cache hit frames, one miss, 42 effect-handle define frames, and 1,538 effect-handle use frames, but surfaced
  one early `command-stream-invalid` fallback during resize capture. The exact command-probe companion
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=commands-resize-runtime-effect-stable-color-filter ./scripts/jbr-skia-command-probe-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 574 CMP recorder frames, 573 Skiko/JBR command frames,
  `jbr_command_fps=114.6`, one same-context surface-change marker, one command-cache clear, one JBR image-cache clear,
  one scoped image-cache clear, 1,201 RuntimeEffect source-cache hit frames, one miss, 2 effect-handle define frames,
  and 1,202 effect-handle use frames. Outputs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-011233/suite.tsv`
  and `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-011348/suite.tsv`.
- Magic Jewel ran an exact RuntimeEffect forced-context pure-color screenshot-parity smoke:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-forced-context-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 781 CMP recorder frames, 780 Skiko/JBR command frames,
  zero picture frames, screenshot status passed, `avg_delta=1.964`, `bad_pixel_ratio=0.04619`,
  `compose_bad_pixel_ratio=0.06861`, one destination context-change marker, one command-cache clear, one JBR
  image-cache clear, one scoped image-cache clear, 1,411 RuntimeEffect source-cache hit frames, one miss, 9
  shader-handle define frames, and 1,412 shader-handle use frames. This was an exact one-row parity smoke, not the
  full screenshot-parity suite; the output directory was 4.1M and `out` remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-010938/suite.tsv`.
- Magic Jewel ran a paired exact RuntimeEffect resize pure-color validation under the daily cap. The screenshot-parity
  row
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-resize-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with screenshot status passed, 826 CMP recorder frames, 827 Skiko/JBR command frames, zero picture
  frames, `avg_delta=1.845`, `bad_pixel_ratio=0.04438`, `compose_bad_pixel_ratio=0.06443`, 1,459 RuntimeEffect
  source-cache hit frames, one miss, 7 shader-handle define frames, and 1,461 shader-handle use frames. It also
  surfaced one early `command-stream-invalid` fallback during resize capture, so Magic Jewel ran the exact command-probe
  companion
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=commands-resize-runtime-effect-pure-color ./scripts/jbr-skia-command-probe-suite.sh`,
  which passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 842 CMP/Skiko/JBR command frames,
  `jbr_command_fps=168.4`, one same-context surface-change marker, one command-cache clear, one JBR image-cache clear,
  one scoped image-cache clear, 1,497 RuntimeEffect source-cache hit frames, one miss, 2 shader-handle define frames,
  and 1,498 shader-handle use frames. Outputs:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-010544/suite.tsv`
  and `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260616-010721/suite.tsv`.
- Magic Jewel ran an exact RuntimeEffect color-filter child screenshot-parity smoke:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-color-filter-child ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 786 CMP recorder frames, 785 Skiko/JBR command frames,
  zero picture frames, screenshot status passed, `avg_delta=2.122`, `bad_pixel_ratio=0.05041`,
  `compose_bad_pixel_ratio=0.07523`, `jbr_runtime_effect_cache_hit_frames=1366`,
  `jbr_runtime_effect_cache_miss_frames=1`, 32 effect-handle define frames, and 1,367 effect-handle use frames. This
  was an exact one-row parity smoke, not the full screenshot-parity suite; the output directory was 4.2M and `out`
  remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-010333/suite.tsv`.
- Magic Jewel ran an exact RuntimeEffect color-filter screenshot-parity smoke:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 849 CMP recorder frames, 848 Skiko/JBR command frames,
  zero picture frames, screenshot status passed, `avg_delta=2.120`, `bad_pixel_ratio=0.05037`,
  `compose_bad_pixel_ratio=0.07513`, `jbr_runtime_effect_cache_hit_frames=1471`,
  `jbr_runtime_effect_cache_miss_frames=1`, 24 effect-handle define frames, and 1,472 effect-handle use frames. This
  was an exact one-row parity smoke, not the full screenshot-parity suite; the output directory was 4.2M and `out`
  remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-005827/suite.tsv`.
- Magic Jewel ran an exact RuntimeEffect shader color-filter screenshot-parity smoke:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-shader-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 738 CMP/Skiko/JBR command frames, zero picture frames,
  screenshot status passed, `avg_delta=2.110`, `bad_pixel_ratio=0.05006`, `compose_bad_pixel_ratio=0.07461`,
  `jbr_runtime_effect_cache_hit_frames=1293`, `jbr_runtime_effect_cache_miss_frames=1`, 8 shader-handle define
  frames, 1,298 shader-handle use frames, 24 effect-handle define frames, and 4 effect-handle use frames. This was an
  exact one-row parity smoke, not the full screenshot-parity suite; the output directory was 4.1M and `out` remained
  121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-005545/suite.tsv`.
- Magic Jewel ran an exact RuntimeEffect shader screenshot-parity smoke:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-shader ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 806 CMP/Skiko/JBR command frames, zero picture frames,
  screenshot status passed, `avg_delta=2.111`, `bad_pixel_ratio=0.05008`, `compose_bad_pixel_ratio=0.07465`,
  `jbr_runtime_effect_cache_hit_frames=1446`, `jbr_runtime_effect_cache_miss_frames=1`, 12 shader-handle define
  frames, 1,447 shader-handle use frames, and 20 effect-handle define frames. This was an exact one-row parity smoke,
  not the full screenshot-parity suite; the output directory was 4.2M and `out` remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-005306/suite.tsv`.
- Magic Jewel ran an exact RuntimeEffect child-only screenshot-parity smoke:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-child-only ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 822 CMP recorder frames, 823 Skiko/JBR command frames,
  zero picture frames, screenshot status passed, `avg_delta=2.118`, `bad_pixel_ratio=0.05029`,
  `compose_bad_pixel_ratio=0.07500`, `jbr_runtime_effect_cache_hit_frames=1477`,
  `jbr_runtime_effect_cache_miss_frames=1`, 9 shader-handle define frames, 1,478 shader-handle use frames, and 15
  effect-handle define frames. This was an exact one-row parity smoke, not the full screenshot-parity suite; the output
  directory was 4.2M and `out` remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-005030/suite.tsv`.
- Magic Jewel ran an exact RuntimeEffect uniform-only screenshot-parity smoke:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-uniform-only ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 521 CMP/Skiko/JBR command frames, zero picture frames,
  screenshot status passed, `avg_delta=2.107`, `bad_pixel_ratio=0.04997`, `compose_bad_pixel_ratio=0.07446`,
  `jbr_runtime_effect_cache_hit_frames=1138`, `jbr_runtime_effect_cache_miss_frames=1`, 4 shader-handle define
  frames, 1,139 shader-handle use frames, and 20 effect-handle define frames. This was an exact one-row parity smoke,
  not the full screenshot-parity suite; the output directory was 3.8M and `out` remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-004752/suite.tsv`.
- Magic Jewel ran an exact RuntimeEffect pure-color screenshot-parity smoke:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 783 CMP recorder frames, 782 Skiko/JBR command frames,
  zero picture frames, screenshot status passed, `avg_delta=2.105`, `bad_pixel_ratio=0.04993`,
  `compose_bad_pixel_ratio=0.07439`, `jbr_runtime_effect_cache_hit_frames=1421`,
  `jbr_runtime_effect_cache_miss_frames=1`, 4 shader-handle define frames, 1,422 shader-handle use frames, and 20
  effect-handle define frames. This was an exact one-row parity smoke, not the full screenshot-parity suite; the output
  directory was 4.1M and `out` remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-004434/suite.tsv`.
- Magic Jewel ran an exact RuntimeEffect shader source-cache eviction screenshot-parity smoke:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-shader-source-cache-eviction ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 725 CMP/Skiko/JBR command frames, zero picture frames,
  screenshot status passed, `avg_delta=2.084`, `bad_pixel_ratio=0.04931`, `compose_bad_pixel_ratio=0.07335`,
  `jbr_runtime_effect_cache_hit_frames=1173`, `jbr_runtime_effect_cache_miss_frames=2349`, 20 shader-handle define
  frames, 3,522 shader-handle use frames, and 20 effect-handle define frames. This was an exact one-row parity smoke,
  not the full screenshot-parity suite; the output directory was 5.7M and `out` remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-003955/suite.tsv`.
- Magic Jewel ran an exact RuntimeEffect source-cache eviction screenshot-parity smoke:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-source-cache-eviction ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 788 CMP recorder frames, 789 Skiko/JBR command frames,
  zero picture frames, screenshot status passed, `avg_delta=2.118`, `bad_pixel_ratio=0.05032`,
  `compose_bad_pixel_ratio=0.07507`, `jbr_runtime_effect_cache_hit_frames=1356`,
  `jbr_runtime_effect_cache_miss_frames=2715`, 40 effect-handle define frames, and 4,071 effect-handle use frames.
  This was an exact one-row parity smoke, not the full screenshot-parity suite; the output directory was 5.8M and `out`
  remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-003700/suite.tsv`.
- Magic Jewel ran a shortened exact benchmark smoke for the SKP picture baseline:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=picture ./scripts/jbr-skia-benchmark-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, screenshot status passed, 769 Skiko/JBR picture frames,
  zero command frames, `app_old_fps=336.6`, `app_new_fps=153.8`, `jbr_picture_fps=153.8`, `old_avg_cpu=107.24`, and
  `new_avg_cpu=116.27`. This was a shortened one-row benchmark smoke, not the full benchmark suite; the output
  directory was 1.7M and `out` remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260616-003352/suite.tsv`.
- Magic Jewel ran an exact RuntimeEffect stable color-filter screenshot-parity smoke under the consumed 2026-06-16
  broad slot:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 546 CMP/Skiko/JBR command frames, zero picture frames,
  screenshot status passed, `avg_delta=2.122`, `bad_pixel_ratio=0.05042`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.07522`, `jbr_runtime_effect_cache_hit_frames=1126`,
  `jbr_runtime_effect_cache_miss_frames=1`, 18 effect-handle define frames, and 1,127 effect-handle use frames. This
  was an exact one-row parity smoke, not the full screenshot-parity suite; the output directory was 3.9M and `out`
  remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260616-003110/suite.tsv`.
- Magic Jewel used the 2026-06-16 daily broad-validation slot for the full compatibility matrix:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with
  `fallback_sum=56`, `unsupported=none`, `validation_failures=none`, background windows shown for all rows, 45,021 CMP
  recorder frames, 909 Skiko/JBR command frames from the happy row, zero picture frames, screenshot status passed for
  56 rows and not-run for the native-ABI row. The guard stamp now records `validation=compatibility matrix` for
  2026-06-16, so further broad suites remain deferred to the next local-day slot or an explicit override. The output
  directory was 79M and `out` remained 121G with about 175Gi free:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260616-000119/matrix.tsv`.
- Magic Jewel ran a narrow compatibility fallback smoke for the RuntimeEffect color-filter capability gate:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=runtime-color-filter-capability-missing ./scripts/jbr-skia-compatibility-matrix.sh`
  passed 1/1 with expected `command-capability-mismatch` fallback, `fallback_new_count=1`, `unsupported=none`,
  background window shown, 560 CMP recorder frames, zero Skiko/JBR command frames, zero picture frames, screenshot
  status passed, and `[SKIKO] warn: SKIKO_JBR_INTEROP_FALLBACK reason=command-capability-mismatch` in `new.log`. This
  was an exact one-row compatibility smoke, not the full matrix; the output directory was 1.1M and `out` remained 121G:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-235857/matrix.tsv`.
- Magic Jewel reaffirmed the once-per-local-day broad-validation cap without launching a broad run. No-launch helpers
  reported 696 command-probe cases and the benchmark groups `baseline=2` / `image-cache=3`; a bare
  `./scripts/jbr-skia-command-probe-suite.sh` exited 3 before launching because the existing 2026-06-15 stamp was
  already consumed by `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-144955`.
  The live docs now explicitly say the daily cap supersedes the older "every ten focused changes" broad-sweep rhythm.
- Magic Jewel ran a narrow compatibility fallback smoke for the shader descriptor capability gate:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=shader-descriptor-capability-missing ./scripts/jbr-skia-compatibility-matrix.sh`
  passed 1/1 with expected `command-capability-mismatch` fallback, `fallback_new_count=1`, `unsupported=none`,
  background window shown, 879 CMP recorder frames, zero Skiko/JBR command frames, zero picture frames, screenshot
  status passed, and `[SKIKO] warn: SKIKO_JBR_INTEROP_FALLBACK reason=command-capability-mismatch` in `new.log`. This
  was an exact one-row compatibility smoke, not the full matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-235048/matrix.tsv`.
- Magic Jewel ran a real screenshot-parity smoke for clean geometry:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-geometry-clean ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with pixel diff enabled, `fallback_new_count=0`, `unsupported=none`, 885 CMP/Skiko/JBR command frames,
  zero picture frames, screenshot status passed, `avg_delta=2.805`, `bad_pixel_ratio=0.07203`,
  `header_buttons_bad_pixel_ratio=0.00272`, `compose_bad_pixel_ratio=0.10955`,
  `compose_bottom_labels_bad_pixel_ratio=0.31738`, `compose_paragraph_probes_bad_pixel_ratio=0.09105`, and
  `compose_bottom_swatches_bad_pixel_ratio=0.00000`. This was an exact one-row parity smoke, not the full
  screenshot-parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260615-234756/suite.tsv`.
- Magic Jewel ran a real screenshot-parity smoke for point dots:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-point-dots ./scripts/jbr-skia-screenshot-parity-suite.sh` passed
  1/1 with pixel diff enabled, `fallback_new_count=0`, `unsupported=none`, 798 CMP/Skiko/JBR command frames, zero
  picture frames, screenshot status passed, `avg_delta=2.122`, `bad_pixel_ratio=0.05042`,
  `header_buttons_bad_pixel_ratio=0.00381`, `compose_bad_pixel_ratio=0.07521`,
  `compose_bottom_labels_bad_pixel_ratio=0.12590`, `compose_paragraph_probes_bad_pixel_ratio=0.08803`, and
  `compose_bottom_swatches_bad_pixel_ratio=0.00000`. This was an exact one-row parity smoke, not the full
  screenshot-parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260615-234511/suite.tsv`.
- Magic Jewel ran a short exact benchmark smoke for image-cache behavior:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASE_GROUPS=image-cache ./scripts/jbr-skia-benchmark-suite.sh`
  passed 3/3 with `fallback_new_count=0`, `unsupported=none`, zero picture frames, screenshot status passed, and
  command replay only. Stable images reported 521 JBR command frames, `app_new_fps=104.0`, `jbr_command_fps=104.2`,
  and zero JBR image-cache evicts/clears. Dynamic images reported 356 JBR command frames, `app_new_fps=71.0`,
  `jbr_command_fps=71.2`, and 4,726 JBR image-cache evict frames. Resize dynamic images reported 552 JBR command
  frames, `app_new_fps=110.4`, `jbr_command_fps=110.4`, 5,276 JBR image-cache evict frames, one JBR image-cache clear,
  and one scoped image-cache clear. This was a shortened 3-row smoke, not the full benchmark suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260615-234133/suite.tsv`.
- Magic Jewel recorded optional old-artifact availability without launching the app:
  `CASE_GROUPS=optional-old ./scripts/jbr-skia-artifact-matrix.sh` produced a skipped-row matrix for all five optional
  rows because no old artifact inputs were configured: `OLD_JBR_API_SHIM`, `OLD_JBR_SKIA_LIB`, `OLD_DESKTOP_PATCH`,
  `OLD_SKIKO_VERSION`, and `OLD_CMP_OUT` are still needed to run those rows. No broad validation or app launch was
  performed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260615-233944/matrix.tsv`.
- Magic Jewel ran a narrow artifact-matrix fallback smoke for the missing public API shim artifact:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=missing-public-api ./scripts/jbr-skia-artifact-matrix.sh` passed 1/1 with
  expected `public-api-missing` fallback, `fallback_new_count=1`, `unsupported=none`, background window shown, 767 CMP
  recorder frames, zero Skiko/JBR command frames, zero picture frames, screenshot status passed, and
  `[SKIKO] warn: SKIKO_JBR_INTEROP_FALLBACK reason=public-api-missing` in `new.log`. This was an exact one-row
  artifact smoke, not the full artifact matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260615-233657/matrix.tsv`.
- Magic Jewel expanded the `shader-composition-runtime` command-probe group to include existing resize and
  forced-context lifecycle companions for composite shader color-filter, transformed shader, RuntimeEffect shader,
  RuntimeEffect shader color-filter, linear/radial shader color-filter, RuntimeEffect pure/uniform/child rows,
  RuntimeEffect shader source-cache eviction, RuntimeEffect color-filter, RuntimeEffect stable color-filter,
  RuntimeEffect color-filter child rows, and RuntimeEffect source-cache eviction. Cheap helper validation only:
  `bash -n scripts/jbr-skia-command-probe-suite.sh` passed,
  `CASE_GROUPS=shader-composition-runtime LIST_CASES=true ./scripts/jbr-skia-command-probe-suite.sh` listed 54 rows,
  `LIST_CASE_GROUP_COUNTS=true ./scripts/jbr-skia-command-probe-suite.sh` reported
  `shader-composition-runtime	54`, and `LIST_UNGROUPED_CASES=true ./scripts/jbr-skia-command-probe-suite.sh`
  returned no rows. No broad validation was launched.
- Magic Jewel ran a narrow compatibility fallback smoke for the remaining core handshake gates:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="abi-mismatch command-capability-mismatch command-capability-high-mismatch" ./scripts/jbr-skia-compatibility-matrix.sh`
  passed 3/3 after an initial sandbox-only Gradle wrapper lock failure. The exact rows reported one expected fallback
  each, `unsupported=none`, background window shown, zero Skiko/JBR command frames, zero picture frames, screenshot
  status passed, and fallback markers for `abi-mismatch` plus low/high `command-capability-mismatch`. CMP recorder
  frames were 869, 464, and 838. This was a three-row smoke, not the full compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-232825/matrix.tsv`.
- Magic Jewel ran a narrow compatibility fallback smoke for the native ABI gate:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=native-abi-mismatch ./scripts/jbr-skia-compatibility-matrix.sh` passed 1/1
  with `expect_command_fallback_reason=native-abi-mismatch`, `fallback_new_count=1`, `unsupported=none`, background
  window shown, 1,047 CMP recorder frames, zero Skiko/JBR command frames, zero picture frames, screenshot status
  not-run, and `[SKIKO] warn: SKIKO_JBR_INTEROP_FALLBACK reason=native-abi-mismatch` in `new.log`. This was an exact
  one-row fallback smoke, not the full compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-232258/matrix.tsv`.
- Magic Jewel ran a narrow compatibility fallback smoke for the public API gate:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=public-api-missing ./scripts/jbr-skia-compatibility-matrix.sh` passed 1/1
  with `expect_command_fallback_reason=public-api-missing`, `fallback_new_count=1`, `unsupported=none`, background
  window shown, 729 CMP recorder frames, zero Skiko/JBR command frames, zero picture frames, screenshot status passed,
  and `[SKIKO] warn: SKIKO_JBR_INTEROP_FALLBACK reason=public-api-missing` in `new.log`. This was an exact one-row
  fallback smoke, not the full compatibility matrix:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-232037/matrix.tsv`.
- Magic Jewel ran a real screenshot-parity smoke for button chrome:
  `DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=parity-button-chrome ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, 1,501 CMP/Skiko/JBR command frames, zero picture
  frames, pixel diff enabled, `avg_delta=2.265`, `bad_pixel_ratio=0.05200`, `header_buttons_bad_pixel_ratio=0.00381`,
  `compose_bad_pixel_ratio=0.07633`, and `compose_bottom_swatches_bad_pixel_ratio=0.00000`. This was an exact
  one-row parity smoke, not the full screenshot-parity suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260615-231755/suite.tsv`.
- Magic Jewel ran a short exact benchmark smoke for the command path:
  `EXPECT_SCREENSHOT_ASSERTION=false DURATION_SECONDS=5 WARMUP_SECONDS=1 CASES=commands ./scripts/jbr-skia-benchmark-suite.sh`
  passed 1/1 with `fallback_new_count=0`, `unsupported=none`, background window shown, 843 CMP recorder frames, 842
  Skiko/JBR command frames, zero picture frames, `app_new_fps=168.6`, `jbr_command_fps=168.4`, and screenshot status
  passed. This was a shortened one-row smoke, not the full benchmark suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260615-231519/suite.tsv`.
- Magic Jewel ran a narrow artifact-matrix smoke for the current artifact set:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=current-all ./scripts/jbr-skia-artifact-matrix.sh` passed 1/1 with
  `fallback_new_count=0`, `unsupported=none`, background window shown, 465 CMP recorder frames, 465 Skiko/JBR command
  frames, zero picture frames, and screenshot status passed. The current artifact inputs were present at
  `/tmp/jbr-skia-run/desktop`, `/tmp/jbr-api-shim.jar`, `/tmp/jbr-skia-native/libjbrskiainterop.dylib`, and
  `../cmp/out/compose-multiplatform-core`. This was an exact one-row smoke, not the full artifact matrix; optional
  old-artifact rows remain pending until old artifacts are provided or the next broad slot is used:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260615-231244/matrix.tsv`.
- Magic Jewel ran the first narrow compatibility-matrix smoke under the daily broad-validation cap:
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=happy ./scripts/jbr-skia-compatibility-matrix.sh` passed 1/1 with
  `fallback_new_count=0`, `unsupported=none`, background window shown, 1,245 CMP recorder frames, 1,244 Skiko/JBR
  command frames, zero picture frames, and screenshot status reported passed. This was an exact one-row compatibility
  smoke, not the full matrix; the earlier sandboxed attempt failed before app startup on the Gradle wrapper lock and
  was rerun outside the sandbox. Full compatibility matrix remains deferred to the next local-day broad slot or an
  explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-225927/matrix.tsv`.
- Magic Jewel tightened no-launch helper handling and command-probe group coverage: `LIST_CASE_GROUP_COUNTS=true` is
  now exempt from daily broad-validation blocking across command-probe, screenshot-parity, compatibility, artifact, and
  benchmark runners; the shader-composition-runtime command group now includes the sweep-gradient shader color-filter
  resize/forced-context rows; and the default list now selects the implemented
  `commands-color-filter-path-effect-wrong-type-fallback` sentinel. Cheap helper validation only: all group-count
  helpers returned counts, command-probe ungrouped output is empty, the shader-composition-runtime group lists the
  sweep-gradient shader color-filter trio, bare broad command/screenshot/compatibility runs still exit 3 for the
  already-consumed 2026-06-15 slot, and no broad validation was launched.
- Magic Jewel closed the daily broad-validation guard range loophole: default-list command-probe and screenshot-parity
  runs selected with `CASES_FROM`/`CASES_UNTIL` now consume or fail against the single daily broad slot instead of
  bypassing the guard. Cheap guard validation only: range launches for command-probe and screenshot-parity both exited
  3 against the already-consumed 2026-06-15 slot, while list-only range helpers still returned selected rows. No broad
  validation was launched.
- Magic Jewel tightened `CASE_GROUPS` helper startup for command-probe and screenshot-parity suites so grouped
  `LIST_CASES`/selection checks skip materializing the large default case lists before the existing group expansion.
  Cheap guard validation only: `CASE_GROUPS=core-effects LIST_CASES=true ./scripts/jbr-skia-command-probe-suite.sh`
  returned the focused core-effects rows immediately,
  `CASE_GROUPS=smoke LIST_CASES=true ./scripts/jbr-skia-screenshot-parity-suite.sh` returned the three smoke parity
  rows immediately, exact-case list probes still returned their selected rows, and bare default command-probe and
  screenshot-parity runs still exited 3 against the already-consumed 2026-06-15 broad slot. No broad validation was
  launched.
- Magic Jewel added resize and forced-context command-probe coverage for gradient-stroke migration:
  `commands-resize-gradient-stroke` and `commands-forced-context-gradient-stroke` now assert destination migration,
  command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while the base gradient-stroke row
  remains on command replay with screenshot assertions disabled for local capture limits. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-gradient-stroke commands-forced-context-gradient-stroke" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,512/1,275 JBR command frames. This
  is focused command-probe lifecycle change 52 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-224020/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for image blend-mode migration:
  `commands-resize-image-blend-mode` and `commands-forced-context-image-blend-mode` now assert image refs,
  destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while the
  base image blend-mode row remains on command replay. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-image-blend-mode commands-forced-context-image-blend-mode" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,287/1,608 JBR command frames. This
  is focused command-probe lifecycle change 51 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-223411/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for linear-gradient path blend-mode migration:
  `commands-resize-linear-gradient-path-blend-mode` and
  `commands-forced-context-linear-gradient-path-blend-mode` now assert destination migration, command-cache clear, JBR
  image-cache clear, and scoped image-cache clear markers while the base linear-gradient path blend-mode row remains
  on command replay. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-linear-gradient-path-blend-mode commands-forced-context-linear-gradient-path-blend-mode" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,642/1,161 JBR command frames. This
  is focused command-probe lifecycle change 50 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-222943/suite.tsv`.
- Magic Jewel tightened the daily broad-validation guard so bare command-probe and screenshot-parity default runs check
  the once-per-local-day stamp before expanding their large default case lists. Cheap guard validation only:
  `./scripts/jbr-skia-command-probe-suite.sh` and `./scripts/jbr-skia-screenshot-parity-suite.sh` now exit 3
  immediately against the already-consumed 2026-06-15 broad slot, while exact-case list probes
  `CASES=commands-core-primitives LIST_CASES=true ./scripts/jbr-skia-command-probe-suite.sh` and
  `CASES=parity-rich LIST_CASES=true ./scripts/jbr-skia-screenshot-parity-suite.sh` still return the selected row.
  No broad validation was launched.
- Magic Jewel added resize and forced-context command-probe coverage for surface gradient blend-mode migration:
  `commands-resize-linear-gradient-blend-mode`, `commands-forced-context-linear-gradient-blend-mode`,
  `commands-resize-radial-gradient-stroke-blend-mode`, `commands-forced-context-radial-gradient-stroke-blend-mode`,
  `commands-resize-sweep-gradient-round-rect-blend-mode`, and
  `commands-forced-context-sweep-gradient-round-rect-blend-mode` now assert destination migration, command-cache clear,
  JBR image-cache clear, and scoped image-cache clear markers while the base gradient blend-mode rows remain on command
  replay. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-linear-gradient-blend-mode commands-forced-context-linear-gradient-blend-mode commands-resize-radial-gradient-stroke-blend-mode commands-forced-context-radial-gradient-stroke-blend-mode commands-resize-sweep-gradient-round-rect-blend-mode commands-forced-context-sweep-gradient-round-rect-blend-mode" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 6/6 with no fallback, no unsupported reasons, zero picture frames, and 1,329/1,284/1,503/932/1,629/1,525
  JBR command frames. This is focused command-probe lifecycle change 49 after the 2026-06-15 daily broad validation
  slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-221607/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for aggregate gradient surface/path migration:
  `commands-resize-gradient-surfaces`, `commands-forced-context-gradient-surfaces`,
  `commands-resize-gradient-paths`, and `commands-forced-context-gradient-paths` now assert destination migration,
  command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while the base aggregate gradient
  rows remain on command replay. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-gradient-surfaces commands-forced-context-gradient-surfaces commands-resize-gradient-paths commands-forced-context-gradient-paths" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 4/4 with no fallback, no unsupported reasons, zero picture frames, and 1,472/1,434/1,021/1,191 JBR command
  frames. This is focused command-probe lifecycle change 48 after the 2026-06-15 daily broad validation slot; broad
  validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-220943/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for point primitive migration:
  `commands-resize-point-lines`, `commands-forced-context-point-lines`, `commands-resize-point-dots`, and
  `commands-forced-context-point-dots` now assert destination migration, command-cache clear, JBR image-cache clear,
  and scoped image-cache clear markers while the base point command rows remain on command replay. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-point-lines commands-forced-context-point-lines commands-resize-point-dots commands-forced-context-point-dots" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 4/4 with no fallback, no unsupported reasons, zero picture frames, and 1,545/1,134/1,526/1,622 JBR command
  frames. This is focused command-probe lifecycle change 47 after the 2026-06-15 daily broad validation slot; broad
  validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-220408/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for concat/skew transform migration:
  `commands-resize-concat-transform`, `commands-forced-context-concat-transform`, `commands-resize-skew-transform`,
  and `commands-forced-context-skew-transform` now assert destination migration, command-cache clear, JBR image-cache
  clear, and scoped image-cache clear markers while the base transform rows remain on command replay. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-concat-transform commands-forced-context-concat-transform commands-resize-skew-transform commands-forced-context-skew-transform" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 4/4 with no fallback, no unsupported reasons, zero picture frames, and 1,456/1,519/1,515/1,000 JBR command
  frames. This is focused command-probe lifecycle change 46 after the 2026-06-15 daily broad validation slot; broad
  validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-215905/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for vertices and fill-rect blend-mode migration:
  `commands-resize-vertices`, `commands-forced-context-vertices`, `commands-resize-blend-mode`, and
  `commands-forced-context-blend-mode` now assert destination migration, command-cache clear, JBR image-cache clear,
  and scoped image-cache clear markers while the base non-descriptor command rows remain on command replay. Exact
  validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-vertices commands-forced-context-vertices commands-resize-blend-mode commands-forced-context-blend-mode" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 4/4 with no fallback, no unsupported reasons, zero picture frames, and 1,582/1,687/898/1,125 JBR command
  frames. This is focused command-probe lifecycle change 45 after the 2026-06-15 daily broad validation slot; broad
  validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-215342/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for path-effect migration:
  `commands-resize-path-effect` and `commands-forced-context-path-effect` now assert destination migration,
  command-cache clear, JBR image-cache clear, scoped image-cache clear, and path-effect handle redefinition markers
  while the base dashed/corner/stamped/chained path-effect row remains on command replay. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-path-effect commands-forced-context-path-effect" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, 10 effect-handle defines per row, and
  1,078/1,383 JBR command frames. This is focused command-probe lifecycle change 44 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-214919/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for image-filter migration:
  `commands-resize-image-filter` and `commands-forced-context-image-filter` now assert image refs, destination
  migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while the base image
  filter path remains on command replay. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-image-filter commands-forced-context-image-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, zero effect-handle markers, and
  1,234/1,561 JBR command frames. This is focused command-probe lifecycle change 43 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-214119/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for plain fill-rect color-filter migration:
  `commands-resize-color-filter` and `commands-forced-context-color-filter` now assert destination migration,
  command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while the non-handle color-filter
  path remains on command replay. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-color-filter commands-forced-context-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, zero effect-handle markers, and
  1,611/1,692 JBR command frames. This is focused command-probe lifecycle change 42 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-213550/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for fill-rect color-filter blend-mode migration:
  `commands-resize-color-filter-blend-mode` and `commands-forced-context-color-filter-blend-mode` now assert
  destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while the
  non-handle blend color-filter path remains on command replay. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-color-filter-blend-mode commands-forced-context-color-filter-blend-mode" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, zero effect-handle markers, and
  1,192/1,209 JBR command frames. This is focused command-probe lifecycle change 41 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-213054/suite.tsv`.
- Magic Jewel extended the daily broad-validation guard to the default benchmark suite so accidental full benchmark
  runs share the same once-per-local-day cap as command sweeps, screenshot parity suites, compatibility matrices, and
  artifact matrices. Cheap guard validation only: `LIST_CASE_COUNT=true ./scripts/jbr-skia-benchmark-suite.sh`
  reported 5 selected benchmark cases, `./scripts/jbr-skia-benchmark-suite.sh` exited 3 against the already-consumed
  2026-06-15 broad slot before launching cases, and
  `CASES=commands LIST_CASES=true ./scripts/jbr-skia-benchmark-suite.sh` reported only `commands`. No broad validation
  was run.
- Magic Jewel added resize and forced-context command-probe coverage for color-filter handle migration:
  `commands-resize-color-filter-handle` and `commands-forced-context-color-filter-handle` now assert destination
  migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, and color-filter effect-handle
  redefine/cache-hit markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-color-filter-handle commands-forced-context-color-filter-handle" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, two effect-handle defines per row, and
  1,494/1,455 JBR command frames. This is focused command-probe lifecycle change 40 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-212130/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for image-shader blend-mode migration:
  `commands-resize-image-shader-blend-mode` and `commands-forced-context-image-shader-blend-mode` now assert image
  refs, destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while
  the image-shader paint blend-mode path remains on command replay. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-image-shader-blend-mode commands-forced-context-image-shader-blend-mode" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, zero shader-handle markers, and
  1,444/1,543 JBR command frames. This is focused command-probe lifecycle change 39 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-211648/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for gradient-shader migration:
  `commands-resize-gradient-shaders` and `commands-forced-context-gradient-shaders` now assert destination migration,
  command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while explicit ShaderBrush
  linear/radial/sweep gradients lower through dedicated gradient commands. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-gradient-shaders commands-forced-context-gradient-shaders" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, zero shader-handle markers, and
  1,195/1,464 JBR command frames. This is focused command-probe lifecycle change 38 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-211223/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for turbulence-shader migration:
  `commands-resize-turbulence-shader` and `commands-forced-context-turbulence-shader` now assert destination
  migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, and turbulence shader handle
  redefine/cache-hit markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-turbulence-shader commands-forced-context-turbulence-shader" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, two shader-handle defines per row, and
  1,491/1,508 JBR command frames. This is focused command-probe lifecycle change 37 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-210800/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for noise-shader migration:
  `commands-resize-noise-shader` and `commands-forced-context-noise-shader` now assert destination migration,
  command-cache clear, JBR image-cache clear, scoped image-cache clear, and fractal-noise shader handle
  redefine/cache-hit markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-noise-shader commands-forced-context-noise-shader" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, two shader-handle defines per row, and
  1,146/1,053 JBR command frames. This is focused command-probe lifecycle change 36 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-210138/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for composite-noise-shader migration:
  `commands-resize-composite-noise-shader` and `commands-forced-context-composite-noise-shader` now assert
  destination migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, and composite-noise
  shader handle redefine/cache-hit markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-composite-noise-shader commands-forced-context-composite-noise-shader" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, six shader-handle defines per row, and
  1,480/1,571 JBR command frames. This is focused command-probe lifecycle change 35 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-205546/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for composite-shader migration:
  `commands-resize-composite-shader` and `commands-forced-context-composite-shader` now assert destination migration,
  command-cache clear, JBR image-cache clear, scoped image-cache clear, and composite-shader handle redefine/cache-hit
  markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-composite-shader commands-forced-context-composite-shader" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, shader-handle cache hits, and
  1,383/1,342 JBR command frames. This is focused command-probe lifecycle change 34 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-205029/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for color-shader blend-mode migration:
  `commands-resize-color-shader-blend-mode` and `commands-forced-context-color-shader-blend-mode` now assert
  destination migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, and color-shader
  blend-mode handle redefine/cache-hit markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-color-shader-blend-mode commands-forced-context-color-shader-blend-mode" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, two shader-handle defines per row, and
  1,545/1,601 JBR command frames. This is focused command-probe lifecycle change 33 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-204516/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for color-shader migration:
  `commands-resize-color-shader` and `commands-forced-context-color-shader` now assert destination migration,
  command-cache clear, JBR image-cache clear, scoped image-cache clear, and color-shader handle redefine/cache-hit
  markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-color-shader commands-forced-context-color-shader" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, two shader-handle defines per row, and
  1,522/1,576 JBR command frames. This is focused command-probe lifecycle change 32 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-203927/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for base graphics-layer migration:
  `commands-resize-graphics-layer` and `commands-forced-context-graphics-layer` now assert destination migration,
  command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while the base graphics-layer path
  is enabled. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer commands-forced-context-graphics-layer" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,479/1,293 JBR command frames. This
  is focused command-probe lifecycle change 31 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-203324/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer rotation-Y migration:
  `commands-resize-graphics-layer-rotationy` and `commands-forced-context-graphics-layer-rotationy` now assert
  destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while the
  graphics-layer rotation-Y path is enabled. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-rotationy commands-forced-context-graphics-layer-rotationy" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 977/1,080 JBR command frames. This
  is focused command-probe lifecycle change 30 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-202801/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer rotation-X migration:
  `commands-resize-graphics-layer-rotationx` and `commands-forced-context-graphics-layer-rotationx` now assert
  destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while the
  graphics-layer rotation-X path is enabled. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-rotationx commands-forced-context-graphics-layer-rotationx" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,504/1,220 JBR command frames. This
  is focused command-probe lifecycle change 29 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-202141/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer path-shadow migration:
  `commands-resize-graphics-layer-path-shadow` and `commands-forced-context-graphics-layer-path-shadow` now assert
  path clipping plus shadow command emission, destination migration, command-cache clear, JBR image-cache clear, and
  scoped image-cache clear markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-path-shadow commands-forced-context-graphics-layer-path-shadow" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, one shadow command per row, and
  1,268/1,376 JBR command frames. This is focused command-probe lifecycle change 28 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-201401/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer round-shadow migration:
  `commands-resize-graphics-layer-round-shadow` and `commands-forced-context-graphics-layer-round-shadow` now assert
  rounded clipping plus shadow command emission, destination migration, command-cache clear, JBR image-cache clear, and
  scoped image-cache clear markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-round-shadow commands-forced-context-graphics-layer-round-shadow" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, one shadow command per row, and 929/1,036
  JBR command frames. This is focused command-probe lifecycle change 27 after the 2026-06-15 daily broad validation
  slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-200706/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer shadow migration:
  `commands-resize-graphics-layer-shadow` and `commands-forced-context-graphics-layer-shadow` now assert shadow
  command emission, destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear
  markers while graphics-layer shadow rendering is enabled. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-shadow commands-forced-context-graphics-layer-shadow" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, one shadow command per row, and
  1,308/1,263 JBR command frames. This is focused command-probe lifecycle change 26 after the 2026-06-15 daily broad
  validation slot; broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-200056/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer offscreen migration:
  `commands-resize-graphics-layer-offscreen` and `commands-forced-context-graphics-layer-offscreen` now assert
  destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while
  graphics-layer offscreen compositing is enabled. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-offscreen commands-forced-context-graphics-layer-offscreen" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,266/1,522 JBR command frames. This
  is focused command-probe lifecycle change 25 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-195406/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer modulate-alpha migration:
  `commands-resize-graphics-layer-modulate-alpha` and `commands-forced-context-graphics-layer-modulate-alpha` now
  assert destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while
  graphics-layer alpha modulation is enabled. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-modulate-alpha commands-forced-context-graphics-layer-modulate-alpha" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,243/1,618 JBR command frames. This
  is focused command-probe lifecycle change 24 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-194821/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer path-clip migration:
  `commands-resize-graphics-layer-path-clip` and `commands-forced-context-graphics-layer-path-clip` now assert
  destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while
  path-based graphics-layer clipping is enabled. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-path-clip commands-forced-context-graphics-layer-path-clip" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 441/1,551 JBR command frames. This is
  focused command-probe lifecycle change 23 after the 2026-06-15 daily broad validation slot; broad validation remains
  deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-194020/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer round-clip migration:
  `commands-resize-graphics-layer-round-clip` and `commands-forced-context-graphics-layer-round-clip` now assert
  destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while
  rounded graphics-layer clipping is enabled. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-round-clip commands-forced-context-graphics-layer-round-clip" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,387/1,474 JBR command frames. This
  is focused command-probe lifecycle change 22 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-193510/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer clip migration:
  `commands-resize-graphics-layer-clip` and `commands-forced-context-graphics-layer-clip` now assert destination
  migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while graphics-layer
  clipping is enabled. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-clip commands-forced-context-graphics-layer-clip" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,340/1,437 JBR command frames. This
  is focused command-probe lifecycle change 21 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-192951/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer off-center pivot migration:
  `commands-resize-graphics-layer-offcenter-pivot` and
  `commands-forced-context-graphics-layer-offcenter-pivot` now assert destination migration, command-cache clear, JBR
  image-cache clear, and scoped image-cache clear markers while rotation X/Y, near-camera depth, and off-center pivot
  are enabled. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-offcenter-pivot commands-forced-context-graphics-layer-offcenter-pivot" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,214/1,429 JBR command frames. This
  is focused command-probe lifecycle change 20 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-192229/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer near-camera migration:
  `commands-resize-graphics-layer-near-camera` and `commands-forced-context-graphics-layer-near-camera` now assert
  destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while
  rotation X/Y and near-camera depth are enabled. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-near-camera commands-forced-context-graphics-layer-near-camera" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,529/1,596 JBR command frames. This
  is focused command-probe lifecycle change 19 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-191720/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer scale/translate migration:
  `commands-resize-graphics-layer-scale-translate` and
  `commands-forced-context-graphics-layer-scale-translate` now assert destination migration, command-cache clear, JBR
  image-cache clear, and scoped image-cache clear markers while graphics-layer scale and translation are enabled.
  Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-scale-translate commands-forced-context-graphics-layer-scale-translate" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,384/1,305 JBR command frames. This
  is focused command-probe lifecycle change 18 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-191035/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer combined rotation migration:
  `commands-resize-graphics-layer-rotationxy` and `commands-forced-context-graphics-layer-rotationxy` now assert
  destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers while both
  rotation X and rotation Y are enabled. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-rotationxy commands-forced-context-graphics-layer-rotationxy" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,936/976 JBR command frames. This is
  focused command-probe lifecycle change 17 after the 2026-06-15 daily broad validation slot; broad validation remains
  deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-190433/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer blend-mode migration:
  `commands-resize-graphics-layer-blend-mode` and `commands-forced-context-graphics-layer-blend-mode` now assert
  destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers for the
  blend-mode-only graphics-layer path. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-blend-mode commands-forced-context-graphics-layer-blend-mode" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,371/1,316 JBR command frames. This
  is focused command-probe lifecycle change 16 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-185806/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for the plain composite shader descriptor tree:
  `commands-resize-composite-shader-descriptor-redefine` and
  `commands-forced-context-composite-shader-descriptor-redefine` now assert destination migration, command-cache clear,
  JBR image-cache clear, scoped image-cache clear, and stable shader-handle redefine/reuse/cache-hit markers for the
  linear/radial composite shader path. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-composite-shader-descriptor-redefine commands-forced-context-composite-shader-descriptor-redefine" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 386/1,228 JBR command frames. This is
  focused command-probe lifecycle change 15 after the 2026-06-15 daily broad validation slot; broad validation remains
  deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-185045/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for image draw plus color-matrix filter migration:
  `commands-resize-image-color-matrix-filter` and `commands-forced-context-image-color-matrix-filter` now assert image
  refs, destination migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, and stable
  effect-handle redefine/reuse/cache-hit markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-image-color-matrix-filter commands-forced-context-image-color-matrix-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,276/1,468 JBR command frames. This
  is focused command-probe lifecycle change 14 after the 2026-06-15 daily broad validation slot; broad validation
  remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-184358/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for plain lighting filter migration:
  `commands-resize-lighting-filter` and `commands-forced-context-lighting-filter` now assert destination migration,
  command-cache clear, JBR image-cache clear, scoped image-cache clear, and stable effect-handle
  redefine/reuse/cache-hit markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-lighting-filter commands-forced-context-lighting-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 994/1,361 JBR command frames. This is
  focused command-probe lifecycle change 13 after the 2026-06-15 daily broad validation slot; broad validation remains
  deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-183843/suite.tsv`.
- Magic Jewel installed a shared daily broad-validation guard across the default command-probe sweep, screenshot parity
  suite, compatibility matrix, and artifact matrix. The guard writes a global local-date stamp under
  `out/.jbr-skia-daily-validation/` before a broad run starts and blocks additional broad runners that day with exit
  code 3; exact `CASES`, focused `CASE_GROUPS`, listing modes, and artifact-matrix dry runs remain unblocked. Today's
  real slot was seeded from the existing 2026-06-15 `20260615-144955` broad command sweep, so broad validation remains
  unavailable for June 15 unless explicitly overridden with `JBR_SKIA_ALLOW_EXTRA_BROAD_VALIDATION=true`. Validation of
  the guard was intentionally narrow: `bash -n` passed for the helper and all four guarded scripts; a temp-stamp default
  command-probe invocation exited 3 before launching cases; `CASES=commands-live-animation LIST_CASE_COUNT=true`
  returned `1` under the same temp stamp.
- Magic Jewel added resize and forced-context command-probe coverage for plain color-matrix filter migration:
  `commands-resize-color-matrix-filter` and `commands-forced-context-color-matrix-filter` now assert destination
  migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, and stable effect-handle
  redefine/reuse/cache-hit markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-color-matrix-filter commands-forced-context-color-matrix-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 932/1,220 JBR command frames. This is
  focused command-probe lifecycle change 12 after the 2026-06-15 daily broad validation slot; broad validation remains
  deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-182817/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for saveLayer blend plus tint color-filter
  migration: `commands-resize-save-layer-blend-color-filter` and
  `commands-forced-context-save-layer-blend-color-filter` now assert destination migration, command-cache clear,
  JBR image-cache clear, and scoped image-cache clear markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-save-layer-blend-color-filter commands-forced-context-save-layer-blend-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 844/852 JBR command frames. This
  saveLayer blend+tint path reports zero effect-handle markers, so these rows intentionally assert the cache-migration
  contract only. This is focused command-probe lifecycle change 11 after the 2026-06-15 daily broad validation slot;
  broad validation remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-182254/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for saveLayer blend-mode migration:
  `commands-resize-save-layer-blend-mode` and `commands-forced-context-save-layer-blend-mode` now assert destination
  migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-save-layer-blend-mode commands-forced-context-save-layer-blend-mode" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 478/1,187 JBR command frames. This
  saveLayer blend-mode path reports zero effect-handle markers, so these rows intentionally assert the cache-migration
  contract only. This is focused command-probe lifecycle change 10 after the 2026-06-15 daily broad validation slot;
  broad validation is due by count but remains deferred until the next local-day broad slot or an explicit override:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-181731/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for saveLayer tint color-filter migration:
  `commands-resize-save-layer-filter` and `commands-forced-context-save-layer-filter` now assert destination migration,
  command-cache clear, JBR image-cache clear, and scoped image-cache clear markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-save-layer-filter commands-forced-context-save-layer-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,098/963 JBR command frames. This
  saveLayer tint path reports zero effect-handle markers, so these rows intentionally assert the cache-migration
  contract only. This is focused command-probe lifecycle change 9 after the 2026-06-15 daily broad validation slot:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-181209/suite.tsv`.
- Magic Jewel added positive command-probe coverage for saveLayer color-matrix filtering and destination migration:
  `commands-save-layer-color-matrix-filter`, `commands-resize-save-layer-color-matrix-filter`, and
  `commands-forced-context-save-layer-color-matrix-filter` now assert color-filter effect-handle replay plus resize and
  forced-context cache migration markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-save-layer-color-matrix-filter commands-resize-save-layer-color-matrix-filter commands-forced-context-save-layer-color-matrix-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 3/3 with no fallback, no unsupported reasons, zero picture frames, and 762/1,092/1,195 JBR command frames.
  This is focused command-probe lifecycle change 8 after the 2026-06-15 daily broad validation slot:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-180618/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer blend plus color-matrix filter
  migration: `commands-resize-graphics-layer-blend-color-matrix-filter` and
  `commands-forced-context-graphics-layer-blend-color-matrix-filter` now assert destination migration,
  command-cache clear, JBR image-cache clear, scoped image-cache clear, and stable effect-handle redefine/reuse/cache-hit
  markers while blend mode and color-matrix filtering are both enabled. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-blend-color-matrix-filter commands-forced-context-graphics-layer-blend-color-matrix-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 943/877 JBR command frames. This is
  focused command-probe lifecycle change 7 after the 2026-06-15 daily broad validation slot:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-180123/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer blend plus tint color-filter
  migration: `commands-resize-graphics-layer-blend-color-filter` and
  `commands-forced-context-graphics-layer-blend-color-filter` now assert destination migration, command-cache clear,
  JBR image-cache clear, and scoped image-cache clear markers while blend mode and tint color filter are both enabled.
  Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-blend-color-filter commands-forced-context-graphics-layer-blend-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,673/1,059 JBR command frames. This
  graphics-layer tint path also reports zero effect-handle markers, so these rows intentionally assert the
  cache-migration contract only. This is focused command-probe lifecycle change 6 after the 2026-06-15 daily broad
  validation slot:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-175354/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer tint color-filter migration:
  `commands-resize-graphics-layer-color-filter` and `commands-forced-context-graphics-layer-color-filter` now assert
  destination migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers. Exact
  validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-color-filter commands-forced-context-graphics-layer-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,408/1,211 JBR command frames. The
  tint graphics-layer path does not emit effect-handle markers, so these rows intentionally assert the cache-migration
  contract only. This is focused command-probe lifecycle change 5 after the 2026-06-15 daily broad validation slot:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-174844/suite.tsv`.
- Magic Jewel added command-probe coverage for radial-gradient shader plus color-filter replay and destination
  migration: `commands-radial-gradient-shader-color-filter`,
  `commands-resize-radial-gradient-shader-color-filter`, and
  `commands-forced-context-radial-gradient-shader-color-filter` now assert shader/effect-handle replay plus resize and
  forced-context cache migration markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-radial-gradient-shader-color-filter commands-resize-radial-gradient-shader-color-filter commands-forced-context-radial-gradient-shader-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 3/3 with no fallback, no unsupported reasons, zero picture frames, and 953/451/1,046 JBR command frames. This
  is focused command-probe lifecycle change 4 after the 2026-06-15 daily broad validation slot, so keep subsequent
  validation exact-row/tiny-group only:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-173931/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for plain image-shader migration:
  `commands-resize-image-shader` and `commands-forced-context-image-shader` now assert image refs, destination
  migration, command-cache clear, JBR image-cache clear, and scoped image-cache clear markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-image-shader commands-forced-context-image-shader" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,193/1,050 JBR command frames. This
  is focused command-probe lifecycle change 3 after the 2026-06-15 daily broad validation slot, so keep subsequent
  validation exact-row/tiny-group only:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-172949/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for image shader plus color-filter migration:
  `commands-resize-image-shader-color-filter` and `commands-forced-context-image-shader-color-filter` now assert image
  refs, destination migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, stable
  shader/effect-handle redefinition, and shader-handle reuse markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-image-shader-color-filter commands-forced-context-image-shader-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,896/1,645 JBR command frames. This
  is focused command-probe lifecycle change 2 after the 2026-06-15 daily broad validation slot, so keep subsequent
  validation exact-row/tiny-group only:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-172520/suite.tsv`.
- Magic Jewel added command-probe coverage for sweep-gradient shader plus color-filter replay and destination
  migration: `commands-sweep-gradient-shader-color-filter`,
  `commands-resize-sweep-gradient-shader-color-filter`, and
  `commands-forced-context-sweep-gradient-shader-color-filter` now assert shader/effect-handle replay plus resize and
  forced-context cache migration markers. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-sweep-gradient-shader-color-filter commands-resize-sweep-gradient-shader-color-filter commands-forced-context-sweep-gradient-shader-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 3/3 with no fallback, no unsupported reasons, zero picture frames, and 656/848/622 JBR command frames. This
  is focused command-probe lifecycle change 1 after the 2026-06-15 daily broad validation slot, so keep subsequent
  validation exact-row/tiny-group only:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-171845/suite.tsv`.
- Magic Jewel refreshed the daily-capped broad command-probe validation after ten focused shader/RuntimeEffect lifecycle
  changes. `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` passed 589/589 with
  `fallback_sum=350`, 79 unsupported rows, 80,698 JBR picture frames, and 202,105 JBR command frames. This is the
  2026-06-15 broad validation slot and resets the focused command-probe counter to zero; continue with exact-row/tiny
  focused validation only until the next local-day slot or an explicit ABI/capability gate. Disk free was about 209Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-103914/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for transformed shader migration:
  `commands-resize-transformed-shader` and `commands-forced-context-transformed-shader` now assert destination
  migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, stable shader-handle redefinition,
  and shader-handle reuse markers. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-transformed-shader commands-forced-context-transformed-shader" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 921/1,011 JBR command frames. This is
  focused command-probe lifecycle change 10 after the 2026-06-15 post-sweep smoke refresh, so the batched broad
  command-probe validation is due next:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-103556/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for composite shader plus color-filter migration:
  `commands-resize-composite-shader-color-filter` and `commands-forced-context-composite-shader-color-filter` now
  assert destination migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, stable
  shader/effect-handle redefinition, and shader-handle reuse markers. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-composite-shader-color-filter commands-forced-context-composite-shader-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,119/1,070 JBR command frames. This
  is focused command-probe lifecycle change 9 after the 2026-06-15 post-sweep smoke refresh, so broad command
  validation remains deferred until roughly one more focused change or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-102824/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for linear-gradient shader plus color-filter
  migration: `commands-resize-linear-gradient-shader-color-filter` and
  `commands-forced-context-linear-gradient-shader-color-filter` now assert destination migration, command-cache clear,
  JBR image-cache clear, scoped image-cache clear, stable shader/effect-handle redefinition, and shader-handle reuse
  markers. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-linear-gradient-shader-color-filter commands-forced-context-linear-gradient-shader-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,037/1,262 JBR command frames. This
  is focused command-probe lifecycle change 8 after the 2026-06-15 post-sweep smoke refresh, so broad command
  validation remains deferred until roughly two more focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-102103/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for RuntimeEffect shader plus color-filter
  migration: `commands-resize-runtime-effect-shader-color-filter` and
  `commands-forced-context-runtime-effect-shader-color-filter` now assert destination migration, command-cache clear,
  JBR image-cache clear, scoped image-cache clear, RuntimeEffect source-cache reuse, shader-handle redefinition/use
  markers, and stable effect-handle redefinition/reuse/cache-hit markers. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-runtime-effect-shader-color-filter commands-forced-context-runtime-effect-shader-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 816/1,219 JBR command frames. This is
  focused command-probe lifecycle change 7 after the 2026-06-15 post-sweep smoke refresh, so broad command validation
  remains deferred until roughly three more focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-101428/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for direct RuntimeEffect shader migration:
  `commands-resize-runtime-effect-shader` and `commands-forced-context-runtime-effect-shader` now assert destination
  migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, RuntimeEffect source-cache reuse, and
  shader-handle redefinition/use markers without capping shader definitions because the dynamic shader intentionally
  creates many handles. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-runtime-effect-shader commands-forced-context-runtime-effect-shader" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 833/999 JBR command frames. This is
  focused command-probe lifecycle change 6 after the 2026-06-15 post-sweep smoke refresh, so broad command validation
  remains deferred until roughly four more focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-100848/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for RuntimeEffect color-filter child migration:
  `commands-resize-runtime-effect-color-filter-child` and `commands-forced-context-runtime-effect-color-filter-child`
  now assert destination migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, RuntimeEffect
  source-cache reuse, and child effect-handle redefinition/use markers. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-runtime-effect-color-filter-child commands-forced-context-runtime-effect-color-filter-child" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 946/1,118 JBR command frames. This is
  focused command-probe lifecycle change 5 after the 2026-06-15 post-sweep smoke refresh, so broad command validation
  remains deferred until roughly five more focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-095954/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for dynamic RuntimeEffect color-filter migration:
  `commands-resize-runtime-effect-color-filter` and `commands-forced-context-runtime-effect-color-filter` now assert
  destination migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, RuntimeEffect source-cache
  reuse, and effect-handle redefinition/use markers without capping effect definitions because the dynamic color filter
  intentionally creates many handles. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-runtime-effect-color-filter commands-forced-context-runtime-effect-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,151/1,160 JBR command frames. This
  is focused command-probe lifecycle change 4 after the 2026-06-15 post-sweep smoke refresh, so broad command
  validation remains deferred until roughly six more focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-095358/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for RuntimeEffect child-only migration:
  `commands-resize-runtime-effect-child-only` and `commands-forced-context-runtime-effect-child-only` now assert
  destination migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, RuntimeEffect source-cache
  reuse, and child shader-handle redefinition/reuse/cache-hit markers. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-runtime-effect-child-only commands-forced-context-runtime-effect-child-only" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,054/1,202 JBR command frames. This
  is focused command-probe lifecycle change 3 after the 2026-06-15 post-sweep smoke refresh, so broad command
  validation remains deferred until roughly seven more focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-094747/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for RuntimeEffect uniform-only migration:
  `commands-resize-runtime-effect-uniform-only` and `commands-forced-context-runtime-effect-uniform-only` now assert
  destination migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, RuntimeEffect source-cache
  reuse, and shader-handle redefinition/use markers without capping shader definitions because animated uniforms
  intentionally produce many handles. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-runtime-effect-uniform-only commands-forced-context-runtime-effect-uniform-only" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,297/1,178 JBR command frames. This
  is focused command-probe lifecycle change 2 after the 2026-06-15 post-sweep smoke refresh, so broad command
  validation remains deferred until roughly eight more focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-094037/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for RuntimeEffect pure-color migration:
  `commands-resize-runtime-effect-pure-color` and `commands-forced-context-runtime-effect-pure-color` now assert
  destination migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, RuntimeEffect source-cache
  reuse, and shader-handle redefinition/reuse/cache-hit markers. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-runtime-effect-pure-color commands-forced-context-runtime-effect-pure-color" ./scripts/jbr-skia-command-probe-suite.sh`
  passed 2/2 with no fallback, no unsupported reasons, zero picture frames, and 1,328/1,221 JBR command frames. This
  is focused command-probe lifecycle change 1 after the 2026-06-15 post-sweep smoke refresh, so broad command
  validation remains deferred until roughly nine more focused changes or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-093344/suite.tsv`.
- Magic Jewel refreshed post-command-sweep compatibility and narrow smoke validation after the 569-row command-probe
  coverage. Compatibility matrix
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with `fallback_sum=56`,
  650 JBR command frames from `happy`, and `background_window=true` on all rows. Required artifact matrix
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2:
  `current-all` replayed 419 command frames with no fallback, and `missing-public-api` took the expected single
  public-API fallback. Command benchmark smoke `CASES=commands ./scripts/jbr-skia-benchmark-suite.sh` passed with no
  fallback, zero picture frames, 3,661 JBR command frames, and `jbr_command_fps=183.1`. Disk free was about 174Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-085511/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260615-092542/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260615-092659/suite.tsv`.
- Magic Jewel completed the cadence-triggered command-probe coverage after ten focused graphics-layer render-effect
  lifecycle hardenings. The original full sweep was interrupted after 564/569 rows had passed; the missing five-row
  tail was rerun after rebuilding the missing `/tmp/jbr-api-shim.jar` and native bridge with
  `./scripts/rebuild-jbr-skia-local-artifacts.sh`. Combined coverage from
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` plus
  `EXPECT_SCREENSHOT_ASSERTION=false CASES_FROM=commands-save-layer-raw-table-color-filter-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed 569/569 with `fallback_sum=350`, 79 unsupported rows, 52,125 JBR picture frames, and 113,849 JBR command
  frames. This resets the focused command-probe counter to zero:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-021440/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-085024/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer near-camera chained
  render-effect plus blend and color-matrix-filter composition:
  `commands-resize-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter` and
  `commands-forced-context-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter` now assert
  destination migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, and chained
  effect-handle redefinition/reuse/cache-hit markers while rotation X/Y and near-camera are enabled. Exact
  command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter commands-forced-context-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 712 and 613 JBR command frames, respectively.
  Both rows reported one surface change, one command-cache clear, one JBR image-cache clear, one scoped image-cache
  clear, six effect-handle definitions, and effect-handle cache hits. This is focused command-probe lifecycle change 10
  after the 2026-06-14 18:10 full command-probe sweep, so the next validation step is the cadence-triggered full
  command-probe sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-021141/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer chained render-effect plus blend
  and color-matrix-filter composition:
  `commands-resize-graphics-layer-chained-render-effect-blend-color-matrix-filter` and
  `commands-forced-context-graphics-layer-chained-render-effect-blend-color-matrix-filter` now assert destination
  migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, and chained effect-handle
  redefinition/reuse/cache-hit markers. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-chained-render-effect-blend-color-matrix-filter commands-forced-context-graphics-layer-chained-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 929 and 922 JBR command frames, respectively.
  Both rows reported one surface change, one command-cache clear, one JBR image-cache clear, one scoped image-cache
  clear, six effect-handle definitions, and effect-handle cache hits. This is focused command-probe lifecycle change 9
  after the 2026-06-14 18:10 full command-probe sweep, so broad command validation remains deferred until one more
  focused command-probe change or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-020421/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer offset-effect plus blend and
  color-matrix-filter composition:
  `commands-resize-graphics-layer-offset-effect-blend-color-matrix-filter` and
  `commands-forced-context-graphics-layer-offset-effect-blend-color-matrix-filter` now assert destination migration,
  command-cache clear, JBR image-cache clear, scoped image-cache clear, and effect-handle redefinition/reuse/cache-hit
  markers. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-offset-effect-blend-color-matrix-filter commands-forced-context-graphics-layer-offset-effect-blend-color-matrix-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 980 and 973 JBR command frames, respectively.
  Both rows reported one surface change, one command-cache clear, one JBR image-cache clear, one scoped image-cache
  clear, four effect-handle definitions, and effect-handle cache hits. This is focused command-probe lifecycle change 8
  after the 2026-06-14 18:10 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-015844/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer render-effect plus blend and
  color-matrix-filter composition:
  `commands-resize-graphics-layer-render-effect-blend-color-matrix-filter` and
  `commands-forced-context-graphics-layer-render-effect-blend-color-matrix-filter` now assert destination migration,
  command-cache clear, JBR image-cache clear, scoped image-cache clear, and effect-handle redefinition/reuse/cache-hit
  markers. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-render-effect-blend-color-matrix-filter commands-forced-context-graphics-layer-render-effect-blend-color-matrix-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 892 and 750 JBR command frames, respectively.
  Both rows reported one surface change, one command-cache clear, one JBR image-cache clear, one scoped image-cache
  clear, four effect-handle definitions, and effect-handle cache hits. This is focused command-probe lifecycle change 7
  after the 2026-06-14 18:10 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-015317/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer render-effect plus blend and
  color-filter composition:
  `commands-resize-graphics-layer-render-effect-blend-color-filter` and
  `commands-forced-context-graphics-layer-render-effect-blend-color-filter` now assert destination migration,
  command-cache clear, JBR image-cache clear, scoped image-cache clear, and effect-handle redefinition/reuse/cache-hit
  markers. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-render-effect-blend-color-filter commands-forced-context-graphics-layer-render-effect-blend-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 852 and 849 JBR command frames, respectively.
  Both rows reported one surface change, one command-cache clear, one JBR image-cache clear, one scoped image-cache
  clear, two effect-handle definitions, and effect-handle cache hits. This is focused command-probe lifecycle change 6
  after the 2026-06-14 18:10 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-014743/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer render-effect plus
  color-matrix-filter composition:
  `commands-resize-graphics-layer-render-effect-color-matrix-filter` and
  `commands-forced-context-graphics-layer-render-effect-color-matrix-filter` now assert destination migration,
  command-cache clear, JBR image-cache clear, scoped image-cache clear, and color-matrix/effect-handle
  redefinition/reuse/cache-hit markers. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-render-effect-color-matrix-filter commands-forced-context-graphics-layer-render-effect-color-matrix-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 710 and 906 JBR command frames, respectively.
  Both rows reported one surface change, one command-cache clear, one JBR image-cache clear, one scoped image-cache
  clear, four effect-handle definitions, and effect-handle cache hits. This is focused command-probe lifecycle change 5
  after the 2026-06-14 18:10 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-014123/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer render-effect plus blend-mode
  composition:
  `commands-resize-graphics-layer-render-effect-blend-mode` and
  `commands-forced-context-graphics-layer-render-effect-blend-mode` now assert destination migration, command-cache
  clear, JBR image-cache clear, scoped image-cache clear, and effect-handle redefinition/reuse/cache-hit markers. Exact
  command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-render-effect-blend-mode commands-forced-context-graphics-layer-render-effect-blend-mode" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 774 and 961 JBR command frames, respectively.
  Both rows reported one surface change, one command-cache clear, one JBR image-cache clear, one scoped image-cache
  clear, two effect-handle definitions, and effect-handle cache hits. This is focused command-probe lifecycle change 4
  after the 2026-06-14 18:10 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-013449/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer render-effect plus color-filter
  composition:
  `commands-resize-graphics-layer-render-effect-color-filter` and
  `commands-forced-context-graphics-layer-render-effect-color-filter` now assert destination migration, command-cache
  clear, JBR image-cache clear, scoped image-cache clear, and effect-handle redefinition/reuse/cache-hit markers. Exact
  command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-render-effect-color-filter commands-forced-context-graphics-layer-render-effect-color-filter" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 871 and 945 JBR command frames, respectively.
  Both rows reported one surface change, one command-cache clear, one JBR image-cache clear, one scoped image-cache
  clear, two effect-handle definitions, and effect-handle cache hits. This is focused command-probe lifecycle change 3
  after the 2026-06-14 18:10 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-012942/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer chained render effects:
  `commands-resize-graphics-layer-chained-render-effect` and
  `commands-forced-context-graphics-layer-chained-render-effect` now assert destination migration, command-cache clear,
  JBR image-cache clear, scoped image-cache clear, and chained effect-handle redefinition/reuse/cache-hit markers.
  Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-chained-render-effect commands-forced-context-graphics-layer-chained-render-effect" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 385 and 939 JBR command frames, respectively.
  Both rows reported one surface change, one command-cache clear, one JBR image-cache clear, one scoped image-cache
  clear, four effect-handle definitions, and effect-handle cache hits. This is focused command-probe lifecycle change 2
  after the 2026-06-14 18:10 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-012432/suite.tsv`.
- Magic Jewel added resize and forced-context command-probe coverage for graphics-layer offset render effects:
  `commands-resize-graphics-layer-offset-effect` and `commands-forced-context-graphics-layer-offset-effect` now assert
  destination migration, command-cache clear, JBR image-cache clear, scoped image-cache clear, and effect-handle
  redefinition/reuse/cache-hit markers. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-graphics-layer-offset-effect commands-forced-context-graphics-layer-offset-effect" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 2,302 and 1,077 JBR command frames,
  respectively. Both rows reported one surface change, one command-cache clear, one JBR image-cache clear, one scoped
  image-cache clear, two effect-handle definitions, and effect-handle cache hits. This is focused command-probe
  lifecycle change 1 after the 2026-06-14 18:10 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260615-011920/suite.tsv`.
- Magic Jewel refreshed the narrow post-matrix artifact/benchmark checks. Required artifact matrix
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2:
  `current-all` replayed 316 command frames with no fallback, and `missing-public-api` took the expected single
  public-API fallback with zero command frames; both rows kept `background_window=true`. Command benchmark smoke
  `CASES=commands ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, zero picture frames, 3,129 command
  frames, `app_new_fps=156.4`, and `jbr_command_fps=156.4`. These were narrow post-compatibility checks only; broad
  command/parity sweeps remain deferred until the usual focused-change cadence or an ABI/capability gate. Disk free
  was about 115Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260615-011346/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260615-011452/suite.tsv`.
- Magic Jewel refreshed the post-command-sweep compatibility matrix after the latest full command-probe sweep.
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with
  `fallback_sum=56`, 229 JBR command frames from `happy`, and `background_window=true` on all 57 rows. The matrix
  rechecked ABI/native ABI mismatch handling, command capability low/high mismatches, public API absence, and current
  gradient, text/font, shader/filter/effect/path/transform/image/vertex capability fallback gates after the
  2026-06-14 18:10 full command-probe sweep. A first sandboxed attempt failed before producing frames because Gradle
  could not create its wrapper lock under `~/.gradle`; the escalated serial rerun passed. Disk free was about 115Gi
  after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260615-004232/matrix.tsv`.
- Magic Jewel completed the cadence-triggered full command-probe sweep after ten focused command-probe hardenings for
  shader descriptor migration and native-font migration rows.
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` passed 549/549 with
  `fallback_sum=350`, 79 unsupported rows, 65,039 JBR picture frames, and 109,771 JBR command frames. The sweep covered
  the newly tightened shader, color-shader, noise-shader, turbulence-shader, composite-noise shader, generic-font,
  loaded-font, resource-font, system-font, and custom-font migration sentinels alongside the existing command/fallback
  coverage. This resets the focused command-probe counter to zero; keep subsequent per-change command validation
  exact-row/tiny-group only until roughly ten more meaningful changes or an ABI/capability gate. Disk free was about
  118Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-181002/suite.tsv`.
- Magic Jewel tightened `commands-forced-context-native-custom-font-text-image` with JBR image-cache clear sentinels.
  Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-forced-context-native-custom-font-text-image" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,313 JBR command frames, one JBR image-cache
  clear, and one scoped image-cache clear. This is focused command-probe lifecycle change 10 after the 2026-06-14
  11:32 full command-probe sweep, so the next validation step is the batched full command-probe sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-180730/suite.tsv`.
- Magic Jewel tightened `commands-resize-native-system-font-text` and
  `commands-forced-context-native-system-font-text` with JBR image-cache clear sentinels. Exact two-row command-probe
  validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-native-system-font-text commands-forced-context-native-system-font-text" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,466 and 1,710 JBR command frames,
  respectively, and one JBR image-cache clear plus one scoped image-cache clear per row. This is focused command-probe
  lifecycle change 9 after the 2026-06-14 11:32 full command-probe sweep, so broad command validation remains deferred
  until one more focused command-probe change or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-180320/suite.tsv`.
- Magic Jewel tightened `commands-resize-native-resource-font-text` and
  `commands-forced-context-native-resource-font-text` with JBR image-cache clear sentinels. Exact two-row command-probe
  validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-native-resource-font-text commands-forced-context-native-resource-font-text" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,220 and 1,318 JBR command frames,
  respectively, and one JBR image-cache clear plus one scoped image-cache clear per row. This is focused command-probe
  lifecycle change 8 after the 2026-06-14 11:32 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-175937/suite.tsv`.
- Magic Jewel tightened `commands-resize-native-loaded-font-data-text` and
  `commands-forced-context-native-loaded-font-data-text` with JBR image-cache clear sentinels. Exact two-row
  command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-native-loaded-font-data-text commands-forced-context-native-loaded-font-data-text" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,386 and 1,465 JBR command frames,
  respectively, and one JBR image-cache clear plus one scoped image-cache clear per row. This is focused command-probe
  lifecycle change 7 after the 2026-06-14 11:32 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-175615/suite.tsv`.
- Magic Jewel tightened `commands-resize-native-generic-font-text` and
  `commands-forced-context-native-generic-font-text` with JBR image-cache clear sentinels. Exact two-row command-probe
  validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-native-generic-font-text commands-forced-context-native-generic-font-text" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,247 and 1,504 JBR command frames,
  respectively, and one JBR image-cache clear plus one scoped image-cache clear per row. This is focused command-probe
  lifecycle change 6 after the 2026-06-14 11:32 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-175231/suite.tsv`.
- Magic Jewel tightened `commands-resize-composite-noise-shader-descriptor-redefine` and
  `commands-forced-context-composite-noise-shader-descriptor-redefine` with JBR image-cache clear sentinels. Exact
  two-row command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-composite-noise-shader-descriptor-redefine commands-forced-context-composite-noise-shader-descriptor-redefine" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,275 and 1,650 JBR command frames,
  respectively, and one JBR image-cache clear plus one scoped image-cache clear per row. This is focused command-probe
  lifecycle change 5 after the 2026-06-14 11:32 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-174852/suite.tsv`.
- Magic Jewel tightened `commands-resize-turbulence-shader-descriptor-redefine` and
  `commands-forced-context-turbulence-shader-descriptor-redefine` with JBR image-cache clear sentinels. Exact two-row
  command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-turbulence-shader-descriptor-redefine commands-forced-context-turbulence-shader-descriptor-redefine" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,494 and 1,359 JBR command frames,
  respectively, and one JBR image-cache clear plus one scoped image-cache clear per row. This is focused command-probe
  lifecycle change 4 after the 2026-06-14 11:32 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-174439/suite.tsv`.
- Magic Jewel tightened `commands-resize-noise-shader-descriptor-redefine` and
  `commands-forced-context-noise-shader-descriptor-redefine` with JBR image-cache clear sentinels. Exact two-row
  command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-noise-shader-descriptor-redefine commands-forced-context-noise-shader-descriptor-redefine" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,635 and 1,658 JBR command frames,
  respectively, and one JBR image-cache clear plus one scoped image-cache clear per row. This is focused command-probe
  lifecycle change 3 after the 2026-06-14 11:32 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-174127/suite.tsv`.
- Magic Jewel tightened `commands-resize-color-shader-descriptor-redefine` and
  `commands-forced-context-color-shader-descriptor-redefine` with JBR image-cache clear sentinels. Exact two-row
  command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-color-shader-descriptor-redefine commands-forced-context-color-shader-descriptor-redefine" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,731 and 1,303 JBR command frames,
  respectively, and one JBR image-cache clear plus one scoped image-cache clear per row. This is focused command-probe
  lifecycle change 2 after the 2026-06-14 11:32 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-173740/suite.tsv`.
- Magic Jewel tightened `commands-resize-shader-descriptor-redefine` and
  `commands-forced-context-shader-descriptor-redefine` with JBR image-cache clear sentinels after the latest broad
  command sweep showed stable destination migration markers. Exact two-row command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES="commands-resize-shader-descriptor-redefine commands-forced-context-shader-descriptor-redefine" ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,538 and 1,630 JBR command frames,
  respectively, and one JBR image-cache clear plus one scoped image-cache clear per row. This is focused command-probe
  lifecycle change 1 after the 2026-06-14 11:32 full command-probe sweep, so broad command validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-173417/suite.tsv`.
- Magic Jewel refreshed the narrow post-matrix artifact/benchmark checks. Required artifact matrix
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2:
  `current-all` replayed 869 command frames with no fallback, and `missing-public-api` took the expected single
  public-API fallback with zero command frames; both rows kept `background_window=true`. Command benchmark smoke
  `CASES=commands ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, zero picture frames, 3,848 command
  frames, `app_new_fps=192.4`, and `jbr_command_fps=192.4`. These are narrow post-compatibility checks only; broad
  command/parity sweeps remain deferred until the usual focused-change cadence. Disk free remained about 161Gi:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260614-172951/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260614-173051/suite.tsv`.
- Magic Jewel refreshed the post-command-sweep compatibility matrix:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with `fallback_sum=56`,
  467 JBR command frames from `happy`, and `background_window=true` on all 57 rows. The matrix rechecked ABI/native ABI
  mismatch handling, command capability low/high mismatches, public API absence, and the current gradient, text/font,
  shader/filter/effect/path/transform/image/vertex capability fallback gates after the 2026-06-14 11:32 full
  command-probe sweep. Disk free was about 161Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260614-170152/matrix.tsv`.
- Magic Jewel completed the cadence-triggered full command-probe sweep after ten focused command-probe lifecycle
  hardenings. `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` passed 549/549 with
  `fallback_sum=350`, 80 unsupported rows, 77,679 JBR picture frames, and 152,941 JBR command frames. The sweep covered
  the newly tightened RuntimeEffect source-cache rows, graphics-layer render-effect/color-matrix resize and
  forced-context rows, RuntimeEffect stable color-filter resize/forced-context rows, and descriptor redefine
  resize/forced-context rows, alongside the existing invalid stream, text/font, shader/filter descriptor,
  graphics-layer, save-layer, transform, and fallback coverage. This resets the focused command-probe counter to zero;
  keep subsequent per-change command validation exact-row/tiny-group only until roughly ten more meaningful changes or
  an ABI/capability gate. Disk free was about 162Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-113210/suite.tsv`.
- Magic Jewel tightened `commands-resize-descriptor-redefine` with resize-path JBR image-cache clear sentinels: at
  least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required alongside the existing
  resize surface-change, command-cache clear, and effect-handle definition/use/cache-hit guards. Exact command-probe
  validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-resize-descriptor-redefine ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,286 JBR command frames, one JBR image-cache
  clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, two
  effect-handle definitions, 2,267 effect-handle uses, and 2,265 effect-handle cache hits. This is focused
  command-probe lifecycle change 10 after the 2026-06-14 00:03 full command-probe sweep, so the next validation step is
  the batched full command-probe sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-113014/suite.tsv`.
- Magic Jewel tightened `commands-forced-context-descriptor-redefine` with forced-context JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required
  alongside the existing forced-context surface-change, command-cache clear, and effect-handle
  definition/use/cache-hit guards. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-forced-context-descriptor-redefine ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,353 JBR command frames, one JBR image-cache
  clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, two
  effect-handle definitions, 2,148 effect-handle uses, and 2,146 effect-handle cache hits. This is focused
  command-probe lifecycle change 9 after the 2026-06-14 00:03 full command-probe sweep, so broad command-probe
  validation remains deferred until the next focused command-probe change:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-112804/suite.tsv`.
- Magic Jewel tightened `commands-resize-runtime-effect-stable-color-filter` with resize-path JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required
  alongside the existing resize surface-change, command-cache clear, effect-handle definition/use/cache-hit, and
  RuntimeEffect source-cache guards. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-resize-runtime-effect-stable-color-filter ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 918 JBR command frames, one JBR image-cache
  clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, two
  effect-handle definitions, 1,596 effect-handle uses, 1,594 effect-handle cache hits, 1,595 RuntimeEffect
  source-cache hits, one RuntimeEffect source-cache miss, and zero RuntimeEffect failures. This is focused
  command-probe lifecycle change 8 after the 2026-06-14 00:03 full command-probe sweep, so broad command-probe
  validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-111943/suite.tsv`.
- Magic Jewel tightened `commands-forced-context-runtime-effect-stable-color-filter` with forced-context JBR
  image-cache clear sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now
  required alongside the existing forced-context surface-change, command-cache clear, effect-handle
  definition/use/cache-hit, and RuntimeEffect source-cache guards. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-forced-context-runtime-effect-stable-color-filter ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,305 JBR command frames, one JBR image-cache
  clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, two
  effect-handle definitions, 2,224 effect-handle uses, 2,222 effect-handle cache hits, 2,223 RuntimeEffect
  source-cache hits, one RuntimeEffect source-cache miss, and zero RuntimeEffect failures. This is focused
  command-probe lifecycle change 7 after the 2026-06-14 00:03 full command-probe sweep, so broad command-probe
  validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-111732/suite.tsv`.
- Magic Jewel tightened `commands-forced-context-graphics-layer-color-matrix-filter` with forced-context JBR
  image-cache clear sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now
  required alongside the existing forced-context surface-change, command-cache clear, and effect-handle
  definition/use/cache-hit guards. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-forced-context-graphics-layer-color-matrix-filter ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 969 JBR command frames, one JBR image-cache
  clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, two
  effect-handle definitions, 1,701 effect-handle uses, and 1,699 effect-handle cache hits. This is focused
  command-probe lifecycle change 6 after the 2026-06-14 00:03 full command-probe sweep, so broad command-probe
  validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-111522/suite.tsv`.
- Magic Jewel tightened `commands-resize-graphics-layer-color-matrix-filter` with resize-path JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required
  alongside the existing resize surface-change, command-cache clear, and effect-handle definition/use/cache-hit guards.
  Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-resize-graphics-layer-color-matrix-filter ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,711 JBR command frames, one JBR image-cache
  clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, two
  effect-handle definitions, 2,282 effect-handle uses, and 2,280 effect-handle cache hits. This is focused
  command-probe lifecycle change 5 after the 2026-06-14 00:03 full command-probe sweep, so broad command-probe
  validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-110941/suite.tsv`.
- Magic Jewel tightened `commands-forced-context-graphics-layer-render-effect` with forced-context JBR image-cache
  clear sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required
  alongside the existing forced-context surface-change, command-cache clear, and effect-handle
  definition/use/cache-hit guards. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-forced-context-graphics-layer-render-effect ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,129 JBR command frames, one JBR image-cache
  clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, two
  effect-handle definitions, 1,789 effect-handle uses, and 1,787 effect-handle cache hits. This is focused
  command-probe lifecycle change 4 after the 2026-06-14 00:03 full command-probe sweep, so broad command-probe
  validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-105847/suite.tsv`.
- Magic Jewel tightened `commands-resize-graphics-layer-render-effect` with resize-path JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required
  alongside the existing resize surface-change, command-cache clear, and effect-handle definition/use/cache-hit guards.
  Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-resize-graphics-layer-render-effect ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,083 JBR command frames, one JBR image-cache
  clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, two
  effect-handle definitions, 1,610 effect-handle uses, and 1,608 effect-handle cache hits. This is focused
  command-probe lifecycle change 3 after the 2026-06-14 00:03 full command-probe sweep, so broad command-probe
  validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-105617/suite.tsv`.
- Magic Jewel tightened `commands-runtime-effect-source-cache-eviction` with a RuntimeEffect source-cache hit sentinel:
  at least one JBR RuntimeEffect source-cache hit is now required alongside the existing color-filter source-cache
  eviction and effect-handle definition/use/cache-hit guards. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-runtime-effect-source-cache-eviction ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,044 JBR command frames, 1,441 RuntimeEffect
  source-cache hits, 2,885 source-cache misses, 2,883 source-cache evicts, 2,887 effect-handle definitions, 4,326
  effect-handle uses, 1,441 effect-handle cache hits, 1,863 effect-handle evicts, and zero RuntimeEffect compile/build
  failures. This is focused command-probe lifecycle change 2 after the 2026-06-14 00:03 full command-probe sweep, so
  broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-104811/suite.tsv`.
- Magic Jewel tightened `commands-runtime-effect-shader-source-cache-eviction` with a RuntimeEffect source-cache hit
  sentinel: at least one JBR RuntimeEffect source-cache hit is now required alongside the existing shader source-cache
  eviction and shader-handle definition/use/cache-hit guards. Exact command-probe validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-runtime-effect-shader-source-cache-eviction ./scripts/jbr-skia-command-probe-suite.sh`
  passed with no fallback, no unsupported reasons, zero picture frames, 1,413 JBR command frames, 1,942 RuntimeEffect
  source-cache hits, 3,887 source-cache misses, 3,885 source-cache evicts, 1,946 shader-handle definitions, 5,829
  shader-handle uses, 3,885 shader-handle cache hits, 922 shader-handle evicts, and zero RuntimeEffect compile/build
  failures. This is focused command-probe lifecycle change 1 after the 2026-06-14 00:03 full command-probe sweep, so
  broad command-probe validation remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-104548/suite.tsv`.
- Magic Jewel tightened `parity-runtime-effect-shader-source-cache-eviction` with a RuntimeEffect source-cache hit
  sentinel: at least one JBR RuntimeEffect source-cache hit is now required alongside the existing shader source-cache
  eviction and shader-handle definition/use/cache-hit guards. Exact validation
  `CASES=parity-runtime-effect-shader-source-cache-eviction ./scripts/jbr-skia-screenshot-parity-suite.sh` passed
  with no fallback, zero picture frames, 938 JBR command frames, 1,449 RuntimeEffect source-cache hits, 2,901
  source-cache misses, 2,899 source-cache evicts, 20 shader-handle definitions, 4,350 shader-handle uses, 4,338
  shader-handle cache hits, zero RuntimeEffect compile/build failures, `avg_delta=2.084`, and
  `bad_pixel_ratio=0.04931`. This is focused descriptor/lifecycle change 7 after the 2026-06-14 09:22 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-104239/suite.tsv`.
- Magic Jewel tightened `parity-runtime-effect-source-cache-eviction` with a RuntimeEffect source-cache hit sentinel:
  at least one JBR RuntimeEffect source-cache hit is now required alongside the existing color-filter source-cache
  eviction and effect-handle definition/use/cache-hit guards. Exact validation
  `CASES=parity-runtime-effect-source-cache-eviction ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 1,004 JBR command frames, 1,427 RuntimeEffect source-cache hits, 2,857 source-cache
  misses, 2,855 source-cache evicts, 40 effect-handle definitions, 4,284 effect-handle uses, 4,272 effect-handle
  cache hits, zero RuntimeEffect compile/build failures, `avg_delta=2.118`, and `bad_pixel_ratio=0.05032`. This is
  focused descriptor/lifecycle change 6 after the 2026-06-14 09:22 full parity sweep, so broad parity remains
  deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-104020/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-graphics-layer-render-effect` with forced-context JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in
  addition to the existing forced-context surface-change, command-cache clear, and effect-handle
  definition/use/cache-hit guards. Exact validation
  `CASES=parity-forced-context-graphics-layer-render-effect ./scripts/jbr-skia-screenshot-parity-suite.sh` passed
  with no fallback, zero picture frames, 1,427 JBR command frames, one JBR image-cache clear, one scoped image-cache
  clear, one Skiko surface-change marker, one command-cache clear marker, 11 effect-handle definitions, 2,195
  effect-handle uses, 2,184 effect-handle cache hits, zero RuntimeEffect markers, `avg_delta=2.045`, and
  `bad_pixel_ratio=0.04801`. This is focused descriptor/lifecycle change 5 after the 2026-06-14 09:22 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-103622/suite.tsv`.
- Magic Jewel tightened `parity-resize-graphics-layer-render-effect` with resize-path JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in
  addition to the existing resize surface-change, command-cache clear, and effect-handle definition/use/cache-hit
  guards. Exact validation `CASES=parity-resize-graphics-layer-render-effect ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with the row's expected single resize fallback, zero picture frames, 1,272 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker,
  eight effect-handle definitions, 1,811 effect-handle uses, 1,803 effect-handle cache hits, zero RuntimeEffect
  markers, `avg_delta=1.902`, and `bad_pixel_ratio=0.04557`. This is focused descriptor/lifecycle change 4 after the
  2026-06-14 09:22 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-103149/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-runtime-effect-stable-color-filter` with forced-context JBR image-cache
  clear sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in
  addition to the existing forced-context surface-change, command-cache clear, effect-handle definition/use/cache-hit,
  and RuntimeEffect source-cache guards. Exact validation
  `CASES=parity-forced-context-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with no fallback, zero picture frames, 697 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 54 effect-handle definitions,
  1,080 effect-handle uses, 1,071 effect-handle cache hits, 1,079 RuntimeEffect source-cache hits, one RuntimeEffect
  source-cache miss, zero RuntimeEffect failures, `avg_delta=1.981`, and `bad_pixel_ratio=0.04668`. This is focused
  descriptor/lifecycle change 1 after the 2026-06-14 09:22 full parity sweep, so broad parity remains deferred. Disk
  free was about 148Gi after the exact run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-102147/suite.tsv`.
- Magic Jewel tightened `parity-resize-graphics-layer-color-matrix-filter` with resize-path JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in
  addition to the existing resize surface-change, command-cache clear, and effect-handle definition/use/cache-hit
  guards. Exact validation `CASES=parity-resize-graphics-layer-color-matrix-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with the row's expected single resize fallback, zero picture frames, 883 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker,
  eight effect-handle definitions, 1,508 effect-handle uses, 1,500 effect-handle cache hits, zero RuntimeEffect
  markers, `avg_delta=1.907`, and `bad_pixel_ratio=0.04577`. This is focused descriptor/lifecycle change 2 after the
  2026-06-14 09:22 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-102542/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-graphics-layer-color-matrix-filter` with forced-context JBR image-cache
  clear sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in
  addition to the existing forced-context surface-change, command-cache clear, and effect-handle
  definition/use/cache-hit guards. Exact validation
  `CASES=parity-forced-context-graphics-layer-color-matrix-filter ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with no fallback, zero picture frames, 696 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 11 effect-handle definitions,
  1,266 effect-handle uses, 1,255 effect-handle cache hits, zero RuntimeEffect markers, `avg_delta=2.048`, and
  `bad_pixel_ratio=0.04815`. This is focused descriptor/lifecycle change 3 after the 2026-06-14 09:22 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-102844/suite.tsv`.
- Magic Jewel completed the cadence-triggered full screenshot parity sweep after ten focused parity lifecycle/descriptor
  hardenings. `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture
  frames, 93,828 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The sweep covered the
  newly tightened forced-context turbulence, resize/forced-context composite-noise, resize/forced-context
  RuntimeEffect pure-color, and resize RuntimeEffect stable color-filter lifecycle rows plus the existing button chrome,
  geometry, text/font data, image/filter/shader descriptors, RuntimeEffect, graphics-layer, transform, resize, and
  forced-context rows. This resets the focused parity change counter to zero; keep subsequent per-change parity
  validation exact-row/tiny-group only until roughly ten more meaningful parity changes or an ABI/capability gate. Disk
  free was about 173Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-092237/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-turbulence-shader` with forced-context JBR image-cache clear sentinels:
  at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in addition to the
  existing forced-context surface-change, command-cache clear, shader-handle definition/use/cache-hit, and
  effect-definition guards. Exact validation
  `CASES=parity-forced-context-turbulence-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 790 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one
  Skiko surface-change marker, one command-cache clear marker, 50 effect-handle definitions, ten shader-handle
  definitions, 1,268 shader-handle uses, 1,258 shader-handle cache hits, zero RuntimeEffect markers,
  `avg_delta=1.978`, and `bad_pixel_ratio=0.04661`. This is focused descriptor/lifecycle change 5 after the
  2026-06-14 07:55 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-090701/suite.tsv`.
- Magic Jewel tightened `parity-resize-composite-noise-shader` with resize-path JBR image-cache clear sentinels: at
  least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in addition to the
  existing resize surface-change, command-cache clear, shader-handle definition/use/cache-hit, and effect-definition
  guards. Exact validation `CASES=parity-resize-composite-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with the row's expected single resize fallback, zero picture frames, 790 JBR command frames, one JBR
  image-cache clear, one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 35
  effect-handle definitions, 21 shader-handle definitions, 1,340 shader-handle uses, 1,333 shader-handle cache hits,
  zero RuntimeEffect markers, `avg_delta=1.846`, and `bad_pixel_ratio=0.04430`. This is focused
  descriptor/lifecycle change 6 after the 2026-06-14 07:55 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-090959/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-composite-noise-shader` with forced-context JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in
  addition to the existing forced-context surface-change, command-cache clear, shader-handle definition/use/cache-hit,
  and effect-definition guards. Exact validation
  `CASES=parity-forced-context-composite-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 800 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one
  Skiko surface-change marker, one command-cache clear marker, 45 effect-handle definitions, 27 shader-handle
  definitions, 1,324 shader-handle uses, 1,315 shader-handle cache hits, zero RuntimeEffect markers,
  `avg_delta=1.973`, and `bad_pixel_ratio=0.04630`. This is focused descriptor/lifecycle change 7 after the
  2026-06-14 07:55 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-091228/suite.tsv`.
- Magic Jewel tightened `parity-resize-runtime-effect-pure-color` with resize-path JBR image-cache clear sentinels: at
  least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in addition to the
  existing resize surface-change, command-cache clear, shader-handle definition/use/cache-hit, effect-definition, and
  RuntimeEffect source-cache guards. Exact validation
  `CASES=parity-resize-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's
  expected single resize fallback, zero picture frames, 771 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 40 effect-handle definitions,
  eight shader-handle definitions, 1,213 shader-handle uses, 1,205 shader-handle cache hits, 1,211 RuntimeEffect
  source-cache hits, one RuntimeEffect source-cache miss, zero RuntimeEffect failures, `avg_delta=1.845`, and
  `bad_pixel_ratio=0.04438`. This is focused descriptor/lifecycle change 8 after the 2026-06-14 07:55 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-091510/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-runtime-effect-pure-color` with forced-context JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in
  addition to the existing forced-context surface-change, command-cache clear, shader-handle definition/use/cache-hit,
  effect-definition, and RuntimeEffect source-cache guards. Exact validation
  `CASES=parity-forced-context-runtime-effect-pure-color ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 711 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one
  Skiko surface-change marker, one command-cache clear marker, 40 effect-handle definitions, eight shader-handle
  definitions, 1,257 shader-handle uses, 1,249 shader-handle cache hits, 1,256 RuntimeEffect source-cache hits, one
  RuntimeEffect source-cache miss, zero RuntimeEffect failures, `avg_delta=1.964`, and `bad_pixel_ratio=0.04619`.
  This is focused descriptor/lifecycle change 9 after the 2026-06-14 07:55 full parity sweep, so broad parity remains
  deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-091740/suite.tsv`.
- Magic Jewel tightened `parity-resize-runtime-effect-stable-color-filter` with resize-path JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in
  addition to the existing resize surface-change, command-cache clear, effect-handle definition/use/cache-hit, and
  RuntimeEffect source-cache guards. Exact validation
  `CASES=parity-resize-runtime-effect-stable-color-filter ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  the row's expected single resize fallback, zero picture frames, 805 JBR command frames, one JBR image-cache clear,
  one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 48 effect-handle
  definitions, 1,330 effect-handle uses, 1,322 effect-handle cache hits, 1,328 RuntimeEffect source-cache hits, one
  RuntimeEffect source-cache miss, zero RuntimeEffect failures, `avg_delta=1.855`, and `bad_pixel_ratio=0.04467`.
  This is focused descriptor/lifecycle change 10 after the 2026-06-14 07:55 full parity sweep, so the next validation
  step is the batched broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-092014/suite.tsv`.
- Magic Jewel tightened `parity-resize-turbulence-shader` with resize-path JBR image-cache clear sentinels: at least
  one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in addition to the existing
  resize surface-change, command-cache clear, shader-handle definition/use/cache-hit, and effect-definition guards.
  Exact validation `CASES=parity-resize-turbulence-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with
  the row's expected single resize fallback, zero picture frames, 1,065 JBR command frames, one JBR image-cache clear,
  one scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 35 effect-handle
  definitions, seven shader-handle definitions, 1,613 shader-handle uses, 1,606 shader-handle cache hits, zero
  RuntimeEffect markers, `avg_delta=1.850`, and `bad_pixel_ratio=0.04455`. This is focused descriptor/lifecycle
  change 4 after the 2026-06-14 07:55 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-090236/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-noise-shader` with forced-context JBR image-cache clear sentinels: at
  least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in addition to the
  existing forced-context surface-change, command-cache clear, shader-handle definition/use/cache-hit, and
  effect-definition guards. Exact validation
  `CASES=parity-forced-context-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no fallback,
  zero picture frames, 1,036 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one Skiko
  surface-change marker, one command-cache clear marker, 45 effect-handle definitions, nine shader-handle definitions,
  1,440 shader-handle uses, 1,431 shader-handle cache hits, zero RuntimeEffect markers, `avg_delta=1.975`, and
  `bad_pixel_ratio=0.04639`. This is focused descriptor/lifecycle change 3 after the 2026-06-14 07:55 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-085944/suite.tsv`.
- Magic Jewel tightened `parity-resize-noise-shader` with resize-path JBR image-cache clear sentinels: at least one
  JBR image-cache clear and at least one scoped JBR image-cache clear are now required in addition to the existing
  resize surface-change, command-cache clear, shader-handle definition/use/cache-hit, and effect-definition guards.
  Exact validation `CASES=parity-resize-noise-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the
  row's expected single resize fallback, zero picture frames, 925 JBR command frames, one JBR image-cache clear, one
  scoped image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 30 effect-handle
  definitions, six shader-handle definitions, 1,347 shader-handle uses, 1,341 shader-handle cache hits, zero
  RuntimeEffect markers, `avg_delta=1.849`, and `bad_pixel_ratio=0.04439`. This is focused descriptor/lifecycle
  change 2 after the 2026-06-14 07:55 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-085700/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-color-shader` with forced-context JBR image-cache clear sentinels: at
  least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in addition to the
  existing forced-context surface-change, command-cache clear, shader-handle definition/use/cache-hit, and
  effect-definition guards. Exact validation
  `CASES=parity-forced-context-color-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no fallback,
  zero picture frames, 1,025 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one Skiko
  surface-change marker, one command-cache clear marker, 45 effect-handle definitions, nine shader-handle definitions,
  1,547 shader-handle uses, 1,538 shader-handle cache hits, zero RuntimeEffect markers, `avg_delta=1.974`, and
  `bad_pixel_ratio=0.04646`. This is focused descriptor/lifecycle change 1 after the 2026-06-14 07:55 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-085352/suite.tsv`.
- Magic Jewel completed the cadence-triggered full screenshot parity sweep after ten focused parity lifecycle/descriptor
  hardenings. `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture
  frames, 97,534 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The sweep covered
  the newly tightened resize native text, color-filter handle, forced-context color-filter handle, and resize
  color-shader lifecycle rows plus the existing button chrome, geometry, text/font data, image/filter/shader
  descriptors, RuntimeEffect, graphics-layer, transform, resize, and forced-context rows. The focused parity change
  counter resets to zero; keep subsequent per-change parity validation exact-row/tiny-group only until roughly ten more
  meaningful parity changes or an ABI/capability gate. Disk free was about 174Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-075504/suite.tsv`.
- Magic Jewel tightened `parity-resize-color-shader` with resize-path JBR image-cache clear sentinels: at least one JBR
  image-cache clear and at least one scoped JBR image-cache clear are now required in addition to the existing resize
  surface-change, command-cache clear, shader-handle definition/use/cache-hit, and effect-definition guards. Exact
  validation `CASES=parity-resize-color-shader ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's
  expected single resize fallback, zero picture frames, 925 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 40 effect-handle definitions,
  eight shader-handle definitions, 1,461 shader-handle uses, 1,453 shader-handle cache hits, zero RuntimeEffect
  markers, `avg_delta=1.850`, and `bad_pixel_ratio=0.04454`. This is focused descriptor/lifecycle change 10 after the
  2026-06-14 06:24 full parity sweep, so the next validation step is a batched broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-075306/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-color-filter-handle` with forced-context JBR image-cache clear sentinels:
  at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in addition to the
  existing forced-context surface-change, command-cache clear, and effect-handle definition/use/cache-hit guards. Exact
  validation `CASES=parity-forced-context-color-filter-handle ./scripts/jbr-skia-screenshot-parity-suite.sh` passed
  with no fallback, zero picture frames, 1,110 JBR command frames, one JBR image-cache clear, one scoped image-cache
  clear, one Skiko surface-change marker, one command-cache clear marker, 11 effect-handle definitions, 1,853
  effect-handle uses, 1,842 effect-handle cache hits, zero shader/RuntimeEffect markers, `avg_delta=2.065`, and
  `bad_pixel_ratio=0.04897`. This is focused descriptor/lifecycle change 9 after the 2026-06-14 06:24 full parity
  sweep, so broad parity remains deferred until roughly one more focused parity change or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-075047/suite.tsv`.
- Magic Jewel tightened `parity-resize-color-filter-handle` with resize-path JBR image-cache clear sentinels: at least
  one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in addition to the existing
  resize surface-change, command-cache clear, and effect-handle definition/use/cache-hit guards. Exact validation
  `CASES=parity-resize-color-filter-handle ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's
  expected single resize fallback, zero picture frames, 1,251 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, nine effect-handle definitions,
  2,013 effect-handle uses, 2,004 effect-handle cache hits, zero shader/RuntimeEffect markers, `avg_delta=1.922`, and
  `bad_pixel_ratio=0.04650`. This is focused descriptor/lifecycle change 8 after the 2026-06-14 06:24 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-074820/suite.tsv`.
- Magic Jewel tightened `parity-resize-native-system-font-text` with resize-path JBR image-cache clear sentinels: at
  least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in addition to the
  existing resize surface-change, command-cache clear, and effect-definition guards. Exact validation
  `CASES=parity-resize-native-system-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's
  expected single resize fallback, zero picture frames, 492 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 35 effect-handle definitions,
  zero effect/shader handle uses, zero RuntimeEffect markers, `avg_delta=1.868`, and `bad_pixel_ratio=0.04499`. This
  is focused descriptor/lifecycle change 7 after the 2026-06-14 06:24 full parity sweep, so broad parity remains
  deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-074522/suite.tsv`.
- Magic Jewel tightened `parity-resize-native-resource-font-text` with resize-path JBR image-cache clear sentinels: at
  least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in addition to the
  existing font-data, resize surface-change, command-cache clear, and effect-definition guards. Exact validation
  `CASES=parity-resize-native-resource-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 1,048 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one
  Skiko surface-change marker, one command-cache clear marker, 25 effect-handle definitions, zero effect/shader handle
  uses, zero RuntimeEffect markers, `avg_delta=1.946`, and `bad_pixel_ratio=0.04588`. This is focused
  descriptor/lifecycle change 6 after the 2026-06-14 06:24 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-074259/suite.tsv`.
- Magic Jewel tightened `parity-resize-native-loaded-font-data-text` with resize-path JBR image-cache clear sentinels:
  at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in addition to the
  existing font-data, resize surface-change, command-cache clear, and effect-definition guards. Exact validation
  `CASES=parity-resize-native-loaded-font-data-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 673 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one
  Skiko surface-change marker, one command-cache clear marker, 25 effect-handle definitions, zero effect/shader handle
  uses, zero RuntimeEffect markers, `avg_delta=1.923`, and `bad_pixel_ratio=0.04559`. This is focused
  descriptor/lifecycle change 5 after the 2026-06-14 06:24 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-074046/suite.tsv`.
- Magic Jewel tightened `parity-resize-native-generic-font-text` with resize-path JBR image-cache clear sentinels: at
  least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in addition to the
  existing resize surface-change, command-cache clear, and effect-definition guards. Exact validation
  `CASES=parity-resize-native-generic-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with the row's
  expected single resize fallback, zero picture frames, 1,063 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 35 effect-handle definitions,
  zero effect/shader handle uses, zero RuntimeEffect markers, `avg_delta=1.982`, and `bad_pixel_ratio=0.04633`. This
  is focused descriptor/lifecycle change 4 after the 2026-06-14 06:24 full parity sweep, so broad parity remains
  deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-073613/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-native-resource-font-text` with forced-context JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in
  addition to the existing font-data, surface-change, command-cache clear, and effect-definition guards. Exact
  validation `CASES=parity-forced-context-native-resource-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with no fallback, zero picture frames, 1,023 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 30 effect-handle definitions,
  zero effect/shader handle uses, zero RuntimeEffect markers, `avg_delta=2.089`, and `bad_pixel_ratio=0.04805`. This
  is focused descriptor/lifecycle change 3 after the 2026-06-14 06:24 full parity sweep, so broad parity remains
  deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-073218/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-native-loaded-font-data-text` with forced-context JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in
  addition to the existing font-data, surface-change, command-cache clear, and effect-definition guards. Exact
  validation `CASES=parity-forced-context-native-loaded-font-data-text ./scripts/jbr-skia-screenshot-parity-suite.sh`
  passed with no fallback, zero picture frames, 890 JBR command frames, one JBR image-cache clear, one scoped
  image-cache clear, one Skiko surface-change marker, one command-cache clear marker, 30 effect-handle definitions,
  zero effect/shader handle uses, zero RuntimeEffect markers, `avg_delta=2.062`, and `bad_pixel_ratio=0.04772`. This
  is focused descriptor/lifecycle change 2 after the 2026-06-14 06:24 full parity sweep, so broad parity remains
  deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-072816/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-native-generic-font-text` with forced-context JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in
  addition to the existing surface-change, command-cache clear, and effect-definition guards. Exact validation
  `CASES=parity-forced-context-native-generic-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 1,032 JBR command frames, one JBR image-cache clear, one scoped image-cache clear,
  one Skiko surface-change marker, one command-cache clear marker, 45 effect-handle definitions, zero effect/shader
  handle uses, zero RuntimeEffect markers, `avg_delta=2.129`, and `bad_pixel_ratio=0.04848`. This is focused
  descriptor/lifecycle change 1 after the 2026-06-14 06:24 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-072430/suite.tsv`.
- Magic Jewel completed the cadence-triggered full screenshot parity sweep after ten focused parity lifecycle/descriptor
  hardenings. `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture
  frames, 99,527 JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. The focused parity
  change counter resets to zero; keep subsequent per-change parity validation exact-row/tiny-group only until roughly
  ten more meaningful parity changes or an ABI/capability gate. Disk free was about 175Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-062458/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-native-system-font-text` with forced-context JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in
  addition to the existing surface-change, command-cache clear, and effect-definition guards. Exact validation
  `CASES=parity-forced-context-native-system-font-text ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no
  fallback, zero picture frames, 956 JBR command frames, one JBR image-cache clear, one scoped image-cache clear, one
  Skiko surface-change marker, one command-cache clear marker, 45 effect-handle definitions, zero effect/shader handle
  uses, zero RuntimeEffect markers, `avg_delta=1.995`, and `bad_pixel_ratio=0.04699`. This was focused
  descriptor/lifecycle change 10 after the 2026-06-13 21:15 full parity sweep and triggered the batched broad parity
  sweep above:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-062342/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-native-custom-font-text-image` with forced-context JBR image-cache clear
  sentinels: at least one JBR image-cache clear and at least one scoped JBR image-cache clear are now required in
  addition to the existing surface-change, command-cache clear, and effect-definition guards. Exact validation
  `CASES=parity-forced-context-native-custom-font-text-image ./scripts/jbr-skia-screenshot-parity-suite.sh` passed
  with no fallback, zero picture frames, 1,041 JBR command frames, one JBR image-cache clear, one scoped image-cache
  clear, one Skiko surface-change marker, one command-cache clear marker, 40 effect-handle definitions, zero
  effect/shader handle uses, zero RuntimeEffect markers, `avg_delta=2.123`, and `bad_pixel_ratio=0.05044`. This is
  focused descriptor/lifecycle change 9 after the 2026-06-13 21:15 full parity sweep, so broad parity remains
  deferred until roughly one more focused change or an ABI/capability gate:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260614-061859/suite.tsv`.
- Magic Jewel refreshed the narrow artifact/benchmark checkpoint after the post-sweep compatibility matrix. Required
  artifact matrix `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh`
  passed 2/2: `current-all` replayed 514 JBR command frames with no fallback, `missing-public-api` produced the
  expected `public-api-missing` fallback with zero command frames, and both rows kept `background_window=true`.
  Single-row benchmark smoke `CASES=commands ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, zero
  picture frames, 4,076 JBR command frames, 17 old-side CPU samples, 17 new-side CPU samples,
  `old_avg_cpu=66.27`, `new_avg_cpu=71.56`, `app_old_fps=255.6`, `app_new_fps=203.8`, and
  `jbr_command_fps=203.8`. Disk free remained about 176Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260614-060948/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260614-061056/suite.tsv`.
- Magic Jewel refreshed the post-sweep compatibility matrix after the cadence-triggered full command-probe sweep:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with
  `fallback_sum=56`, 520 JBR command frames from the `happy` row, and `background_window=true` on all rows. The
  matrix covered ABI/native-ABI/public-API mismatch fallbacks plus low/high command-capability bit fallbacks for
  gradients, text/font data, image/shader/color-filter/effect descriptors, path effects, transforms, shadows, points,
  vertices, and shader Perlin noise. Disk free remained about 176Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260614-054414/matrix.tsv`.
- Magic Jewel completed the cadence-triggered full command-probe sweep after ten focused command-probe hardenings.
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-command-probe-suite.sh` passed 549/549 with
  `fallback_sum=350`, 80 unsupported rows, 70,982 JBR picture frames, and 130,383 JBR command frames. This resets the
  focused command-probe change counter to zero; continue using exact-row command validation per change and defer the
  next full command sweep until roughly ten more meaningful command-probe changes or an ABI/capability gate. Disk free
  was about 176Gi after the run and stale Magic Jewel validation process cleanup:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-000344/suite.tsv`.
- Magic Jewel tightened the focused command-probe row
  `commands-invalid-radial-gradient-shader-descriptor-radius-fallback` with shader-handle reuse guards on top of its
  existing definition guards: exactly three JBR shader-handle definitions, at least one shader-handle use, and at
  least one shader-handle cache-hit frame before the intentional radial-gradient corrupt radius fallback. Exact
  validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-radial-gradient-shader-descriptor-radius-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with one fallback, no unsupported marker, zero picture frames, zero command frames after fallback, three
  shader-handle definitions, 1,663 shader-handle uses, 1,662 shader-handle cache hits, zero effect-handle markers, and
  zero RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260614-000122/suite.tsv`.
- Magic Jewel tightened the focused command-probe row
  `commands-invalid-linear-gradient-shader-descriptor-stop-order-fallback` with shader/effect handle-use guards on top
  of its existing definition guards: exactly two JBR shader-handle definitions, at least one shader-handle use/cache
  hit, exactly one JBR effect-handle definition, and at least one effect-handle use before the intentional linear
  gradient corrupt stop-order fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-linear-gradient-shader-descriptor-stop-order-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with one fallback, no unsupported marker, zero picture frames, zero command frames after fallback, one
  effect-handle definition/use, two shader-handle definitions, 1,463 shader-handle uses, 1,461 shader-handle cache
  hits, zero effect-handle cache hits, and zero RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-235818/suite.tsv`.
- Magic Jewel tightened the focused command-probe row
  `commands-invalid-linear-gradient-shader-descriptor-tile-mode-fallback` with shader/effect handle-use guards on top
  of its existing definition guards: exactly two JBR shader-handle definitions, at least one shader-handle use/cache
  hit, exactly one JBR effect-handle definition, and at least one effect-handle use before the intentional linear
  gradient corrupt tile-mode fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-linear-gradient-shader-descriptor-tile-mode-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with one fallback, no unsupported marker, zero picture frames, zero command frames after fallback, one
  effect-handle definition/use, two shader-handle definitions, 1,651 shader-handle uses, 1,649 shader-handle cache
  hits, zero effect-handle cache hits, and zero RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-235247/suite.tsv`.
- Magic Jewel tightened the focused command-probe row
  `commands-invalid-composite-shader-descriptor-blend-mode-fallback` with shader-handle definition/reuse guards:
  exactly three JBR shader-handle definitions, at least one shader-handle use, and at least one shader-handle cache-hit
  frame before the intentional composite-shader corrupt blend-mode fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-composite-shader-descriptor-blend-mode-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with one fallback, no unsupported marker, zero picture frames, zero command frames after fallback, three
  shader-handle definitions, 1,901 shader-handle uses, 1,900 shader-handle cache hits, zero effect-handle markers, and
  zero RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-235013/suite.tsv`.
- Magic Jewel tightened the focused command-probe row
  `commands-invalid-transformed-shader-descriptor-payload-count-fallback` with shader-handle definition/reuse guards:
  exactly two JBR shader-handle definitions, at least one shader-handle use, and at least one shader-handle cache-hit
  frame before the intentional transformed-shader corrupt payload-count fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-transformed-shader-descriptor-payload-count-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with one fallback, no unsupported marker, zero picture frames, zero command frames after fallback, two
  shader-handle definitions, 1,883 shader-handle uses, 1,882 shader-handle cache hits, zero effect-handle markers, and
  zero RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-234735/suite.tsv`.
- Magic Jewel tightened the focused command-probe row `commands-invalid-shader-descriptor-version-fallback` with
  shader-handle definition/reuse guards on the shared descriptor-version case block: exactly one JBR shader-handle
  definition, at least one shader-handle use, and at least one shader-handle cache-hit frame before the intentional
  corrupt descriptor-version fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-shader-descriptor-version-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with one fallback, no unsupported marker, zero picture frames, zero command frames after fallback, one
  shader-handle definition, 1,544 shader-handle uses, 1,543 shader-handle cache hits, zero effect-handle markers, and
  zero RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-234432/suite.tsv`.
- Magic Jewel tightened the focused command-probe row `commands-invalid-shader-descriptor-record-length-fallback` with
  shader-handle definition/reuse guards: exactly one JBR shader-handle definition, at least one shader-handle use, and
  at least one shader-handle cache-hit frame before the intentional corrupt record-length fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-shader-descriptor-record-length-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with one fallback, no unsupported marker, zero picture frames, zero command frames after fallback, one
  shader-handle definition, 1,665 shader-handle uses/cache hits, zero effect-handle markers, and zero RuntimeEffect
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-234224/suite.tsv`.
- Magic Jewel tightened the focused command-probe row
  `commands-invalid-shader-color-filter-descriptor-payload-count-fallback` with shader/effect handle-definition guards
  plus shader reuse guards: exactly two JBR shader-handle definitions, at least one shader-handle use, at least one
  shader-handle cache-hit frame, and exactly one JBR effect-handle definition before the intentional corrupt
  payload-count fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-shader-color-filter-descriptor-payload-count-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with one fallback, no unsupported marker, zero picture frames, zero command frames after fallback, one
  effect-handle definition, two shader-handle definitions, 1,851 shader-handle uses/cache hits, zero effect-handle
  uses/cache hits, and zero RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-234001/suite.tsv`.
- Magic Jewel tightened the focused command-probe row `commands-invalid-shader-descriptor-payload-count-fallback` with
  shader-handle definition/reuse guards: exactly one JBR shader-handle definition, at least one shader-handle use, and
  at least one shader-handle cache-hit frame before the intentional corrupt payload-count fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-shader-descriptor-payload-count-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with one fallback, no unsupported marker, zero picture frames, zero command frames after fallback, one
  shader-handle definition, 1,391 shader-handle uses, 1,390 shader-handle cache hits, zero effect-handle markers, and
  zero RuntimeEffect markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-233518/suite.tsv`.
- Magic Jewel tightened the focused command-probe row
  `commands-invalid-color-shader-descriptor-payload-count-fallback` with shader-handle definition/reuse guards:
  exactly one JBR shader-handle definition, at least one shader-handle use, and at least one shader-handle cache-hit
  frame before the intentional corrupt payload-count fallback. Exact validation
  `EXPECT_SCREENSHOT_ASSERTION=false CASES=commands-invalid-color-shader-descriptor-payload-count-fallback ./scripts/jbr-skia-command-probe-suite.sh`
  passed with one fallback, no unsupported marker, zero picture frames, zero command frames after fallback, one
  shader-handle definition, 1,532 shader-handle uses/cache hits, zero effect-handle markers, and zero RuntimeEffect
  markers:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-233307/suite.tsv`.
- Magic Jewel refreshed the narrow artifact/benchmark checkpoint after the compatibility matrix and eight focused
  descriptor-cap tightenings. Required artifact matrix
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2:
  `current-all` replayed 492 JBR command frames with no fallback, `missing-public-api` fell back once with zero command
  frames, and both rows kept `background_window=true`. Single-row benchmark smoke
  `CASES=commands ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, zero picture frames, 3,898 JBR
  command frames, `app_new_fps=194.8`, and `jbr_command_fps=194.9`. The focused descriptor-cap counter remains 8, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260613-232728/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260613-232907/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-native-custom-font-text-image`: at least one JBR
  effect-handle definition and a max of 32, after the latest broad sweep showed 20 definitions and no effect-handle
  uses. Exact validation passed with no fallback, zero picture frames, 882 JBR command frames, 20 effect-handle
  definitions, zero effect/shader handle uses, zero RuntimeEffect markers, zero surface-change/cache-clear markers,
  and `bad_pixel_ratio=0.05044`. This is focused descriptor-cap change 8 after the 2026-06-13 21:15 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-232306/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-geometry-clean`: at least one JBR effect-handle definition
  and a max of 40, after the latest broad sweep showed 30 definitions and exact validation observed 20. Exact
  validation passed with no fallback, zero picture frames, 617 JBR command frames, 20 effect-handle definitions, zero
  effect/shader handle uses, zero RuntimeEffect markers, zero surface-change/cache-clear markers, and
  `bad_pixel_ratio=0.07203`. This is focused descriptor-cap change 7 after the 2026-06-13 21:15 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-231721/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-runtime-effect-pure-color`: at least one JBR effect-handle
  definition and a max of 32, after the latest broad sweep showed 15 definitions and exact validation observed 20.
  Exact validation passed with no fallback, zero picture frames, 980 JBR command frames, 20 effect-handle definitions,
  four shader-handle definitions, 1,568 shader-handle uses, 1,564 shader-handle cache-hit frames, 1,567
  RuntimeEffect source-cache hit frames, one source-cache miss, zero effect-handle uses, zero surface-change/cache-clear
  markers, and `bad_pixel_ratio=0.04993`. This is focused descriptor-cap change 6 after the 2026-06-13 21:15 full
  parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-231339/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-forced-context-turbulence-shader`: at least one JBR
  effect-handle definition and a max of 56, after the latest broad sweep showed 30 definitions and exact validation
  observed 45. Exact validation passed with no fallback, zero picture frames, 454 JBR command frames, 45
  effect-handle definitions, nine shader-handle definitions, 928 shader-handle uses, 919 shader-handle cache-hit
  frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one command-cache clear
  marker, zero effect-handle uses, and `bad_pixel_ratio=0.04661`. This is focused descriptor-cap change 5 after the
  2026-06-13 21:15 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-231024/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-resize-turbulence-shader`: at least one JBR effect-handle
  definition and a max of 48, after the latest broad sweep showed 20 definitions and exact validation observed 35.
  Exact validation passed with the row's single resize fallback, zero picture frames, 799 JBR command frames, 35
  effect-handle definitions, seven shader-handle definitions, 1,312 shader-handle uses, 1,305 shader-handle cache-hit
  frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one command-cache clear
  marker, zero effect-handle uses, and `bad_pixel_ratio=0.04455`. This is focused descriptor-cap change 4 after the
  2026-06-13 21:15 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-230715/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-turbulence-shader`: at least one JBR effect-handle
  definition and a max of 32, after the latest broad sweep showed 15 definitions. Exact validation passed with no
  fallback, zero picture frames, 960 JBR command frames, 15 effect-handle definitions, three shader-handle
  definitions, 1,608 shader-handle uses, 1,605 shader-handle cache-hit frames, zero effect-handle uses, zero
  RuntimeEffect markers, zero surface-change/cache-clear markers, and `bad_pixel_ratio=0.05035`. This is focused
  descriptor-cap change 3 after the 2026-06-13 21:15 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-230432/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-forced-context-noise-shader`: at least one JBR
  effect-handle definition and a max of 56, after the latest broad sweep showed 30 definitions and exact validation
  observed 35. Exact validation passed with no fallback, zero picture frames, 564 JBR command frames, 35
  effect-handle definitions, seven shader-handle definitions, 1,031 shader-handle uses, 1,024 shader-handle cache-hit
  frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one command-cache clear
  marker, zero effect-handle uses, and `bad_pixel_ratio=0.04639`. This is focused descriptor-cap change 2 after the
  2026-06-13 21:15 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-230138/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-resize-noise-shader`: at least one JBR effect-handle
  definition and a max of 48, after the latest broad sweep showed 30 definitions and exact validation observed 35.
  Exact validation passed with the row's single resize fallback, zero picture frames, 1,100 JBR command frames, 35
  effect-handle definitions, seven shader-handle definitions, 1,656 shader-handle uses, 1,649 shader-handle cache-hit
  frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one command-cache clear
  marker, zero effect-handle uses, and `bad_pixel_ratio=0.04439`. This is focused descriptor-cap change 1 after the
  2026-06-13 21:15 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-225853/suite.tsv`.
- Magic Jewel refreshed the compatibility matrix after the full command-probe sweep and latest batched parity sweep:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with
  `fallback_sum=56`, 182 JBR command frames from the `happy` row, and `background_window=true` on all 57 rows. The
  matrix covered ABI/native-ABI/public-API mismatch fallbacks plus low/high command-capability bit fallbacks for
  gradients, text/font data, image/shader/color-filter/effect descriptors, path effects, transforms, shadows, points,
  vertices, and shader Perlin noise. Disk free was about 192Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260613-222843/matrix.tsv`.
- Magic Jewel completed the batched full default screenshot parity sweep after ten focused descriptor-cap tightenings.
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=8`, zero picture frames, 63,790
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the focused
  descriptor-cap change counter to zero. Disk free was about 169Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-211500/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-noise-shader`: at least one JBR effect-handle definition and
  a max of 32, after the latest broad sweep showed 20 definitions. Exact validation passed with no fallback, zero
  picture frames, 783 JBR command frames, 20 effect-handle definitions, four shader-handle definitions, 1,278
  shader-handle uses, 1,274 shader-handle cache-hit frames, zero effect-handle uses, zero RuntimeEffect markers, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.05013`. This is focused descriptor-cap change 10 after
  the 2026-06-13 19:34 full parity sweep, so the next step is the batched broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-211224/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-forced-context-color-shader`: at least one JBR
  effect-handle definition and a max of 56, after the latest broad sweep showed 45 definitions and exact validation
  observed 50. Exact validation passed with no fallback, zero picture frames, 1,051 JBR command frames, 50
  effect-handle definitions, 10 shader-handle definitions, 1,536 shader-handle uses, 1,526 shader-handle cache-hit
  frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one context-change
  marker, one command-cache clear marker, zero effect-handle uses, and `bad_pixel_ratio=0.04646`. This is focused
  descriptor-cap change 9 after the 2026-06-13 19:34 full parity sweep; after one more focused change, run the next
  batched broad parity sweep unless an ABI/capability gate supersedes it:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-210911/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-resize-color-shader`: at least one JBR effect-handle
  definition and a max of 48, after the latest broad sweep showed 35 definitions and exact validation observed 30.
  Exact validation passed with the row's single resize fallback, zero picture frames, 627 JBR command frames, 30
  effect-handle definitions, six shader-handle definitions, 1,232 shader-handle uses, 1,226 shader-handle cache-hit
  frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one command-cache clear
  marker, zero effect-handle uses, and `bad_pixel_ratio=0.04454`. This is focused descriptor-cap change 8 after the
  2026-06-13 19:34 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-210633/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-color-shader`: at least one JBR effect-handle definition
  and a max of 32, after the latest broad sweep showed 20 definitions. Exact validation passed with no fallback, zero
  picture frames, 660 JBR command frames, 20 effect-handle definitions, four shader-handle definitions, 1,084
  shader-handle uses, 1,080 shader-handle cache-hit frames, zero effect-handle uses, zero RuntimeEffect markers, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.05020`. This is focused descriptor-cap change 7 after
  the 2026-06-13 19:34 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-210338/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-forced-context-composite-noise-shader`: at least one JBR
  effect-handle definition and a max of 56, after the latest broad sweep showed 45 definitions. Exact validation
  passed with no fallback, zero picture frames, 564 JBR command frames, 45 effect-handle definitions, 27 shader-handle
  definitions, 1,138 shader-handle uses, 1,129 shader-handle cache-hit frames, one JBR image-cache clear, one scoped
  image-cache clear, one surface-change marker, one context-change marker, one command-cache clear marker, zero
  effect-handle uses, and `bad_pixel_ratio=0.04630`. This is focused descriptor-cap change 6 after the 2026-06-13
  19:34 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-205644/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-resize-composite-noise-shader`: at least one JBR
  effect-handle definition and a max of 48, after the latest broad sweep showed 40 definitions and exact validation
  observed 35. Exact validation passed with the row's single resize fallback, zero picture frames, 588 JBR command
  frames, 35 effect-handle definitions, 21 shader-handle definitions, 1,114 shader-handle uses, 1,107 shader-handle
  cache-hit frames, one JBR image-cache clear, one scoped image-cache clear, one surface-change marker, one
  command-cache clear marker, zero effect-handle uses, and `bad_pixel_ratio=0.04430`. This is focused descriptor-cap
  change 5 after the 2026-06-13 19:34 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-205349/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-composite-noise-shader`: at least one JBR effect-handle
  definition and a max of 32, after the latest broad sweep showed 20 definitions and exact validation observed 15.
  Exact validation passed with no fallback, zero picture frames, 402 JBR command frames, 15 effect-handle definitions,
  nine shader-handle definitions, 755 shader-handle uses, 752 shader-handle cache-hit frames, zero effect-handle uses,
  zero RuntimeEffect markers, zero surface-change/cache-clear markers, and `bad_pixel_ratio=0.05004`. This is focused
  descriptor-cap change 4 after the 2026-06-13 19:34 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-205047/suite.tsv`.
- Magic Jewel added the missing minimum descriptor-definition gate to
  `parity-runtime-effect-shader-source-cache-eviction`, preserving its existing max of 32, after the latest broad
  sweep showed 20 JBR effect-handle definitions. Exact validation passed with no fallback, zero picture frames, 730
  JBR command frames, 20 effect-handle definitions, 20 shader-handle definitions, 3,507 shader-handle uses, 3,495
  shader-handle cache-hit frames, 1,168 RuntimeEffect source-cache hits, 2,339 source-cache misses, 2,337
  source-cache evictions, zero compile/build failures, zero surface-change/cache-clear markers, and
  `bad_pixel_ratio=0.04931`. This is focused descriptor-cap change 3 after the 2026-06-13 19:34 full parity sweep,
  so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-204648/suite.tsv`.
- Magic Jewel added the missing minimum descriptor-definition gate to `parity-transformed-shader`, preserving its
  existing max of 32, after the latest broad sweep showed 20 JBR effect-handle definitions. Exact validation passed
  with no fallback, zero picture frames, 539 JBR command frames, 20 effect-handle definitions, eight shader-handle
  definitions, 1,018 shader-handle uses, 1,014 shader-handle cache-hit frames, zero effect-handle uses, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.05007`. This is focused descriptor-cap change 2 after
  the 2026-06-13 19:34 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-204032/suite.tsv`.
- Magic Jewel added the missing minimum descriptor-definition gate to `parity-composite-shader`, preserving its
  existing max of 40, after the latest broad sweep showed 30 JBR effect-handle definitions. Exact validation passed
  with no fallback, zero picture frames, 736 JBR command frames, 25 effect-handle definitions, 15 shader-handle
  definitions, 1,336 shader-handle uses, 1,331 shader-handle cache-hit frames, zero effect-handle uses, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.06710`. This is focused descriptor-cap change 1 after
  the 2026-06-13 19:34 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-203517/suite.tsv`.
- Magic Jewel completed the batched full default screenshot parity sweep after ten focused descriptor-cap tightenings.
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture frames, 90,449
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the focused
  descriptor-cap change counter to zero. Disk free was about 196Gi after the run:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-193432/suite.tsv`.
- Magic Jewel added the missing minimum descriptor-definition gate to `parity-runtime-effect-shader`, preserving its
  existing max of 24, after the latest broad sweep showed 20 JBR effect-handle definitions. Exact validation passed
  with no fallback, zero picture frames, 571 JBR command frames, 20 effect-handle definitions, 12 shader-handle
  definitions, 1,033 shader-handle uses, 1,029 shader-handle cache-hit frames, 1,032 RuntimeEffect source-cache hits,
  one source-cache miss, zero surface-change/cache-clear markers, and `bad_pixel_ratio=0.05008`. This is focused
  descriptor-cap change 10 after the 2026-06-13 18:07 full parity sweep, so the next step is the batched broad
  screenshot parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-193209/suite.tsv`.
- Magic Jewel added the missing minimum descriptor-definition gate to `parity-runtime-effect-child-only`, preserving its
  existing max of 32, after the latest broad sweep showed 20 JBR effect-handle definitions. Exact validation passed
  with no fallback, zero picture frames, 572 JBR command frames, 20 effect-handle definitions, 12 shader-handle
  definitions, 1,060 shader-handle uses, 1,056 shader-handle cache-hit frames, 1,059 RuntimeEffect source-cache hits,
  one source-cache miss, zero surface-change/cache-clear markers, and `bad_pixel_ratio=0.05029`. This is focused
  descriptor-cap change 9 after the 2026-06-13 18:07 full parity sweep; after one more focused change, run the next
  batched broad parity sweep unless an ABI/capability gate supersedes it:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-192932/suite.tsv`.
- Magic Jewel added the missing minimum descriptor-definition gate to `parity-runtime-effect-uniform-only`, preserving
  its existing max of 24, after the latest broad sweep showed 20 JBR effect-handle definitions. Exact validation passed
  with no fallback, zero picture frames, 695 JBR command frames, 20 effect-handle definitions, four shader-handle
  definitions, 1,185 shader-handle uses, 1,181 shader-handle cache-hit frames, 1,184 RuntimeEffect source-cache hits,
  one source-cache miss, zero surface-change/cache-clear markers, and `bad_pixel_ratio=0.04997`. This is focused
  descriptor-cap change 8 after the 2026-06-13 18:07 full parity sweep, so broad parity remains deferred until after
  roughly two more focused changes:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-192644/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-forced-context-runtime-effect-pure-color`: at least one JBR
  effect-handle definition and a max of 56, based on the latest broad sweep showing 45 definitions. Exact validation
  passed with no fallback, zero picture frames, 909 JBR command frames, 45 effect-handle definitions, nine
  shader-handle definitions, 1,389 shader-handle uses, 1,380 shader-handle cache-hit frames, 1,388 RuntimeEffect
  source-cache hits, one source-cache miss, one JBR image-cache clear, one scoped image-cache clear, one surface-change
  marker, one command-cache clear marker, and `bad_pixel_ratio=0.04619`. This is focused descriptor-cap change 7 after
  the 2026-06-13 18:07 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-192406/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-resize-runtime-effect-pure-color`: at least one JBR
  effect-handle definition and a max of 48, based on the latest broad sweep showing 35 definitions. Exact validation
  passed with the row's single resize fallback, zero picture frames, 676 JBR command frames, 35 effect-handle
  definitions, seven shader-handle definitions, 1,129 shader-handle uses, 1,122 shader-handle cache-hit frames, 1,127
  RuntimeEffect source-cache hits, one source-cache miss, one JBR image-cache clear, one scoped image-cache clear, one
  surface-change marker, one command-cache clear marker, and `bad_pixel_ratio=0.04438`. This is focused descriptor-cap
  change 6 after the 2026-06-13 18:07 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-192102/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-forced-context-image-refs`: at least one JBR effect-handle
  definition and a max of 48, based on recent broad sweeps showing exactly 40 definitions. Exact validation passed
  with no fallback, zero picture frames, 638 JBR command frames, one JBR image-cache clear, one scoped image-cache
  clear, 40 effect-handle definitions, one surface-change marker, one command-cache clear marker, zero effect/shader
  handle uses, and `bad_pixel_ratio=0.05050`. This is focused descriptor-cap change 5 after the 2026-06-13 18:07
  full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-191651/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-forced-context-native-custom-font-text-image`: at least one
  JBR effect-handle definition and a max of 48, based on recent broad sweeps showing exactly 40 definitions. Exact
  validation passed with no fallback, zero picture frames, 670 JBR command frames, 40 effect-handle definitions, one
  surface-change marker, one command-cache clear marker, zero effect/shader handle uses, and
  `bad_pixel_ratio=0.05044`. This is focused descriptor-cap change 4 after the 2026-06-13 18:07 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-191420/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-forced-context-native-system-font-text`: at least one JBR
  effect-handle definition and a max of 56, based on recent broad sweeps showing 45 definitions with exact validation
  observing 50. Exact validation passed with no fallback, zero picture frames, 917 JBR command frames, 50
  effect-handle definitions, one surface-change marker, one command-cache clear marker, zero effect/shader handle
  uses, and `bad_pixel_ratio=0.04699`. This is focused descriptor-cap change 3 after the 2026-06-13 18:07 full
  parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-191045/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-forced-context-native-generic-font-text`: at least one JBR
  effect-handle definition and a max of 56, based on recent broad sweeps showing exactly 45 definitions. Exact
  validation passed with no fallback, zero picture frames, 838 JBR command frames, 45 effect-handle definitions, one
  surface-change marker, one command-cache clear marker, zero effect/shader handle uses, and
  `bad_pixel_ratio=0.04848`. This is focused descriptor-cap change 2 after the 2026-06-13 18:07 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-190813/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-forced-context-native-resource-font-text`: at least one JBR
  effect-handle definition and a max of 40, based on recent broad sweeps showing exactly 30 definitions. Exact
  validation passed with no fallback, zero picture frames, 625 JBR command frames, 30 effect-handle definitions, one
  surface-change marker, one command-cache clear marker, zero effect/shader handle uses, and
  `bad_pixel_ratio=0.04805`. This is focused descriptor-cap change 1 after the 2026-06-13 18:07 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-190601/suite.tsv`.
- Magic Jewel completed the batched full default screenshot parity sweep after ten focused descriptor-cap tightenings.
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=12`, zero picture frames, 100,079
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the focused
  descriptor-cap change counter to zero:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-180718/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-forced-context-native-loaded-font-data-text`: at least one
  JBR effect-handle definition and a max of 40, based on recent broad sweeps showing exactly 30 definitions. Exact
  validation passed with no fallback, zero picture frames, 881 JBR command frames, 30 effect-handle definitions, one
  surface-change marker, one command-cache clear marker, zero effect/shader handle uses, and
  `bad_pixel_ratio=0.04772`. This was focused descriptor-cap change 10 after the 2026-06-13 16:43 full parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-180614/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-resize-native-system-font-text`: at least one JBR
  effect-handle definition and a max of 48, based on recent broad sweeps showing 35-40 definitions. Exact validation
  passed with the row's single fallback, zero picture frames, 659 JBR command frames, 35 effect-handle definitions,
  one surface-change marker, one command-cache clear marker, zero effect/shader handle uses, and
  `bad_pixel_ratio=0.04499`. This is focused descriptor-cap change 9 after the 2026-06-13 16:43 full parity sweep;
  after one more focused change, run the next batched broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-180424/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-resize-native-generic-font-text`: at least one JBR
  effect-handle definition and a max of 48, based on recent broad sweeps showing 35-40 definitions. Exact validation
  passed with the row's single fallback, zero picture frames, 687 JBR command frames, 35 effect-handle definitions,
  one surface-change marker, one command-cache clear marker, zero effect/shader handle uses, and
  `bad_pixel_ratio=0.04633`. This is focused descriptor-cap change 8 after the 2026-06-13 16:43 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-180106/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-resize-native-resource-font-text`: at least one JBR
  effect-handle definition and a max of 32, based on recent broad sweeps showing exactly 25 definitions. Exact
  validation passed with the row's single fallback, zero picture frames, 656 JBR command frames, 25 effect-handle
  definitions, one surface-change marker, one command-cache clear marker, zero effect/shader handle uses, and
  `bad_pixel_ratio=0.04588`. This is focused descriptor-cap change 7 after the 2026-06-13 16:43 full parity sweep, so
  broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-175843/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-resize-native-loaded-font-data-text`: at least one JBR
  effect-handle definition and a max of 32, based on recent broad sweeps showing exactly 25 definitions. Exact
  validation passed with no fallback, zero picture frames, 630 JBR command frames, 25 effect-handle definitions, one
  surface-change marker, one command-cache clear marker, zero effect/shader handle uses, and `bad_pixel_ratio=0.04559`.
  This is focused descriptor-cap change 6 after the 2026-06-13 16:43 full parity sweep, so broad parity remains
  deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-175633/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-native-system-font-text`: at least one JBR effect-handle
  definition and a max of 32, based on recent broad sweeps showing 20-25 definitions. Exact validation passed with no
  fallback, zero picture frames, 680 JBR command frames, 20 effect-handle definitions, zero effect/shader handle uses,
  zero surface-change/cache-clear markers, and `bad_pixel_ratio=0.04699`. This is focused descriptor-cap change 5
  after the 2026-06-13 16:43 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-175343/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-native-generic-font-text`: at least one JBR effect-handle
  definition and a max of 32, based on recent broad sweeps showing exactly 25 definitions. Exact validation passed
  with no fallback, zero picture frames, 894 JBR command frames, 20 effect-handle definitions, zero effect/shader
  handle uses, zero surface-change/cache-clear markers, and `bad_pixel_ratio=0.04848`. This is focused descriptor-cap
  change 4 after the 2026-06-13 16:43 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-175119/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-native-resource-font-text`: at least one JBR effect-handle
  definition and a max of 24, based on recent broad sweeps showing exactly 15 definitions. Exact validation passed
  with no fallback, zero picture frames, 892 JBR command frames, 15 effect-handle definitions, zero effect/shader
  handle uses, zero surface-change/cache-clear markers, and `bad_pixel_ratio=0.04805`. This is focused descriptor-cap
  change 3 after the 2026-06-13 16:43 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-174907/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-native-loaded-font-data-text`: at least one JBR
  effect-handle definition and a max of 24, based on recent broad sweeps showing exactly 15 definitions. Exact
  validation passed with no fallback, zero picture frames, 663 JBR command frames, 15 effect-handle definitions, zero
  effect/shader handle uses, zero surface-change/cache-clear markers, and `bad_pixel_ratio=0.04772`. This is focused
  descriptor-cap change 2 after the 2026-06-13 16:43 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-174643/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-rich`: at least one JBR effect-handle definition and a max
  of 32, based on recent broad sweeps showing exactly 20 definitions. Exact validation passed with no fallback, zero
  picture frames, 631 JBR command frames, 20 effect-handle definitions, zero effect/shader handle uses, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.05044`. This is focused descriptor-cap change 1 after
  the 2026-06-13 16:43 full parity sweep, so broad parity is deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-174324/suite.tsv`.
- Magic Jewel completed the batched full default screenshot parity sweep after ten focused descriptor-cap tightenings.
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture frames, 98,167
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the focused
  descriptor-cap change counter to zero:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-164327/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-skew-transform`: at least one JBR effect-handle definition
  and a max of 32, based on recent rows showing 15-30 definitions. Exact validation passed with no fallback, zero
  picture frames, 1,029 JBR command frames, 25 effect-handle definitions, zero effect/shader handle uses, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.06742`. This was focused descriptor-cap change 10 after
  the 2026-06-13 15:16 full parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-164129/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-clip-rects`: at least one JBR effect-handle definition and a
  max of 24, based on recent rows showing 20 definitions. Exact validation passed with no fallback, zero picture
  frames, 696 JBR command frames, 20 effect-handle definitions, zero effect/shader handle uses, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.05000`. This is focused descriptor-cap change 9 after
  the 2026-06-13 15:16 full parity sweep; after one more focused change, run the next batched broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-163916/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-vertices`: at least one JBR effect-handle definition and a
  max of 24, based on recent rows showing 20 definitions. Exact validation passed with no fallback, zero picture
  frames, 700 JBR command frames, 20 effect-handle definitions, zero effect/shader handle uses, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.05041`. This is focused descriptor-cap change 8 after
  the 2026-06-13 15:16 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-163708/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-point-dots`: at least one JBR effect-handle definition and a
  max of 24, based on recent rows showing 20 definitions. Exact validation passed with no fallback, zero picture
  frames, 601 JBR command frames, 20 effect-handle definitions, zero effect/shader handle uses, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.05042`. This is focused descriptor-cap change 7 after
  the 2026-06-13 15:16 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-163501/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-clip-path`: at least one JBR effect-handle definition and a
  max of 24, based on historical rows showing 15-20 definitions. Exact validation passed with no fallback, zero
  picture frames, 667 JBR command frames, 20 effect-handle definitions, zero effect/shader handle uses, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.05039`. This is focused descriptor-cap change 6 after
  the 2026-06-13 15:16 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-163100/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-gradient-stroke`: at least one JBR effect-handle definition
  and a max of 24, based on historical rows showing 15-20 definitions. Exact validation passed with no fallback, zero
  picture frames, 855 JBR command frames, 20 effect-handle definitions, zero effect/shader handle uses, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.05030`. This is focused descriptor-cap change 5 after
  the 2026-06-13 15:16 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-162831/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-image-shader`: at least one JBR effect-handle definition and
  a max of 32, based on historical rows showing 20-30 definitions. Exact validation passed with no fallback, zero
  picture frames, 705 JBR command frames, 25 effect-handle definitions, zero effect/shader handle uses, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.06777`. This is focused descriptor-cap change 4 after
  the 2026-06-13 15:16 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-162546/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-path-effect`: at least one JBR effect-handle definition and
  a max of 32, based on historical rows showing 20-25 definitions. Exact validation passed with no fallback, zero
  picture frames, 1,024 JBR command frames, 20 effect-handle definitions, zero effect/shader handle uses, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.05275`. This is focused descriptor-cap change 3 after
  the 2026-06-13 15:16 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-162333/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-save-layer-filter`: at least one JBR effect-handle definition
  and a max of 32, based on historical rows showing 15-25 definitions. Exact validation passed with no fallback, zero
  picture frames, 799 JBR command frames, 20 effect-handle definitions, zero effect/shader handle uses, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.05037`. This is focused descriptor-cap change 2 after
  the 2026-06-13 15:16 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-162051/suite.tsv`.
- Magic Jewel added descriptor-definition gates to `parity-image-filter`: at least one JBR effect-handle definition and
  a max of 32, based on historical rows showing 15-25 definitions. Exact validation passed with no fallback, zero
  picture frames, 750 JBR command frames, 20 effect-handle definitions, zero effect/shader handle uses, zero
  surface-change/cache-clear markers, and `bad_pixel_ratio=0.05041`. This is focused descriptor-cap change 1 after
  the 2026-06-13 15:16 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-161740/suite.tsv`.
- Magic Jewel completed the batched full default screenshot parity sweep after ten focused descriptor-cap tightenings.
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=10`, zero picture frames, 92,868
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the focused
  descriptor-cap change counter to zero:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-151647/suite.tsv`.
- Magic Jewel tightened `parity-image-color-matrix-filter` from a 64-definition effect-handle cap to 32 after
  historical rows showed 18-30 effect definitions. Exact validation passed with no fallback, 668 JBR command frames,
  zero shader handles, 18 effect-handle definitions, 1,077 effect uses, 1,074 effect cache-hit frames, and
  `bad_pixel_ratio=0.05077`. This is focused descriptor-cap change 10 after the 2026-06-13 13:41 full parity sweep;
  the next step is the batched broad screenshot parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-151442/suite.tsv`.
- Magic Jewel added 32-definition shader/effect-handle caps to `parity-runtime-effect-shader-source-cache-eviction`
  after historical rows showed 15-25 shader and effect definitions. Exact validation passed with no fallback, 626 JBR
  command frames, 20 shader-handle definitions, 3,006 shader uses, 2,994 shader cache-hit frames, 20 effect-handle
  definitions, zero effect-handle uses, 2,003 RuntimeEffect source-cache evicts, zero RuntimeEffect compile/build
  failures, and `bad_pixel_ratio=0.04931`. This is focused descriptor-cap change 9 after the 2026-06-13 13:41 full
  parity sweep; after one more focused change, run the next batched broad parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-151153/suite.tsv`.
- Magic Jewel added a 40-definition effect-handle cap to `parity-runtime-effect-source-cache-eviction` after
  historical rows showed 30-40 effect definitions. Exact validation passed with no fallback, 401 JBR command frames,
  zero shader handles, 30 effect-handle definitions, 2,337 effect uses, 2,328 effect cache-hit frames, 1,557
  RuntimeEffect source-cache evicts, zero RuntimeEffect compile/build failures, and `bad_pixel_ratio=0.05032`. This
  is focused descriptor-cap change 8 after the 2026-06-13 13:41 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-150902/suite.tsv`.
- Magic Jewel added a 32-definition effect-handle cap to `parity-runtime-effect-color-filter-child` after historical
  rows showed 24-32 effect definitions. Exact validation passed with no fallback, 630 JBR command frames, zero shader
  handles, 32 effect-handle definitions, 1,051 effect uses, 1,047 effect cache-hit frames, 1,050 RuntimeEffect
  source-cache hits, one source-cache miss, and `bad_pixel_ratio=0.05041`. This is focused descriptor-cap change 7
  after the 2026-06-13 13:41 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-150518/suite.tsv`.
- Magic Jewel added a 32-definition effect-handle cap to `parity-runtime-effect-child-only` after historical rows
  showed 15-25 effect definitions, keeping headroom for the known 25-definition outlier. Exact validation passed with
  no fallback, 617 JBR command frames, 12 shader-handle definitions, 981 shader uses, 977 shader cache-hit frames, 20
  effect-handle definitions, zero effect-handle uses, 980 RuntimeEffect source-cache hits, one source-cache miss, and
  `bad_pixel_ratio=0.05029`. This is focused descriptor-cap change 6 after the 2026-06-13 13:41 full parity sweep,
  so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-150241/suite.tsv`.
- Magic Jewel added a 24-definition effect-handle cap to `parity-runtime-effect-uniform-only` after historical rows
  showed 15-20 effect definitions. Exact validation passed with no fallback, 686 JBR command frames, four
  shader-handle definitions, 1,132 shader uses, 1,128 shader cache-hit frames, 20 effect-handle definitions, zero
  effect-handle uses, 1,131 RuntimeEffect source-cache hits, one source-cache miss, and `bad_pixel_ratio=0.04997`.
  This is focused descriptor-cap change 5 after the 2026-06-13 13:41 full parity sweep, so broad parity remains
  deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-150021/suite.tsv`.
- Magic Jewel added a 24-definition effect-handle cap to `parity-runtime-effect-shader` after historical rows showed
  15-20 effect definitions, correcting the guard placement so `parity-runtime-effect-child-only` remains uncapped.
  Exact validation passed with no fallback, 759 JBR command frames, 12 shader-handle definitions, 1,132 shader uses,
  1,128 shader cache-hit frames, 20 effect-handle definitions, zero effect-handle uses, 1,131 RuntimeEffect
  source-cache hits, one source-cache miss, and `bad_pixel_ratio=0.05008`. This is focused descriptor-cap change 4
  after the 2026-06-13 13:41 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-145634/suite.tsv`.
- Magic Jewel added a 32-definition effect-handle cap to `parity-runtime-effect-shader-color-filter` after historical
  rows showed 18-24 effect definitions. Exact validation passed with no fallback, 819 JBR command frames, eight
  shader-handle definitions, 1,325 shader uses, 1,317 shader cache-hit frames, 24 effect-handle definitions, four
  effect-handle uses, 1,320 RuntimeEffect source-cache hits, one source-cache miss, and `bad_pixel_ratio=0.05006`.
  This is focused descriptor-cap change 3 after the 2026-06-13 13:41 full parity sweep, so broad parity remains
  deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-144931/suite.tsv`.
- Magic Jewel added a 32-definition effect-handle cap to `parity-transformed-shader` after historical rows showed
  15-25 effect definitions. Exact validation passed with no fallback, 875 JBR command frames, eight shader-handle
  definitions, 1,312 shader uses, 1,308 shader cache-hit frames, 20 effect-handle definitions, zero effect-handle
  uses, zero surface-change/cache-clear markers, and `bad_pixel_ratio=0.05007`. This is focused descriptor-cap
  change 2 after the 2026-06-13 13:41 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-144641/suite.tsv`.
- Magic Jewel tightened `parity-image-shader-color-filter` from a 64-definition effect-handle cap to 32 after
  historical rows showed 18-30 effect definitions. Exact validation passed with no fallback, 672 JBR command frames,
  eight shader-handle definitions, 1,179 shader uses, 1,171 shader cache-hit frames, 24 effect-handle definitions,
  four effect-handle uses, and `bad_pixel_ratio=0.05002`. This is focused descriptor-cap change 1 after the
  2026-06-13 13:41 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-144339/suite.tsv`.
- Magic Jewel completed the batched full default screenshot parity sweep after ten focused descriptor-cap tightenings.
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture frames, 98,884
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`. This resets the focused
  descriptor-cap change counter to zero:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-134112/suite.tsv`.
- Magic Jewel tightened `parity-linear-gradient-shader-color-filter` from a 64-definition effect-handle cap to 32
  after historical rows showed 18-30 effect definitions. Exact validation passed with no fallback, 621 JBR command
  frames, eight shader-handle definitions, 1,098 shader uses, 1,090 shader cache-hit frames, 24 effect-handle
  definitions, four effect-handle uses, and `bad_pixel_ratio=0.04997`. This is focused descriptor-cap change 10 after
  the 2026-06-13 11:44 full parity sweep, so the next step is the batched broad screenshot parity sweep:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-133844/suite.tsv`.
- Magic Jewel tightened `parity-composite-shader-color-filter` from a 64-definition effect-handle cap to 32 after
  historical rows showed exactly 24 effect definitions. Exact validation passed with no fallback, 649 JBR command
  frames, 16 shader-handle definitions, 1,068 shader uses, 1,060 shader cache-hit frames, 24 effect-handle
  definitions, four effect-handle uses, and `bad_pixel_ratio=0.04991`. This is focused descriptor-cap change 9 after
  the 2026-06-13 11:44 full parity sweep; after one more focused change, run the next broad parity batch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-133624/suite.tsv`.
- Magic Jewel added a 40-definition effect-handle cap to `parity-composite-shader` after historical rows showed 20-35
  effect definitions and the row already had a stable 24-definition shader cap. Exact validation passed with no
  fallback, 684 JBR command frames, 15 shader-handle definitions, 1,103 shader uses, 1,098 shader cache-hit frames, 25
  effect-handle definitions, zero surface-change/cache-clear markers, and `bad_pixel_ratio=0.06710`. This is focused
  descriptor-cap change 8 after the 2026-06-13 11:44 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-133324/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-composite-noise-shader` from a 72-definition shader-handle cap to 40
  after historical rows showed 24-33 shader definitions. Exact validation passed with no fallback, 917 JBR command
  frames, 27 shader-handle definitions, 1,397 uses, 1,388 cache-hit frames, 45 effect-handle definitions, one
  surface-change marker, one command-cache clear marker, and `bad_pixel_ratio=0.04630`. This is focused
  descriptor-cap change 7 after the 2026-06-13 11:44 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-133044/suite.tsv`.
- Magic Jewel tightened `parity-resize-composite-noise-shader` from a 48-definition shader-handle cap to 32 after
  historical rows showed 18-24 shader definitions. Exact validation passed with the row's expected single fallback,
  667 JBR command frames, 21 shader-handle definitions, 1,229 uses, 1,222 cache-hit frames, 35 effect-handle
  definitions, one surface-change marker, one command-cache clear marker, and `bad_pixel_ratio=0.04430`. This is
  focused descriptor-cap change 6 after the 2026-06-13 11:44 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-132818/suite.tsv`.
- Magic Jewel tightened `parity-composite-noise-shader` from a 24-definition shader-handle cap to 16 after historical
  rows showed 9-12 shader definitions. Exact validation passed with no fallback, 716 JBR command frames, 12
  shader-handle definitions, 1,108 uses, 1,104 cache-hit frames, 20 effect-handle definitions, zero effect-handle
  uses, and `bad_pixel_ratio=0.05004`. This is focused descriptor-cap change 5 after the 2026-06-13 11:44 full parity
  sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-132536/suite.tsv`.
- Magic Jewel tightened `parity-composite-shader-color-filter` from a 32-definition shader-handle cap to 24 after
  historical rows showed exactly 16 shader definitions. Exact validation passed with no fallback, 848 JBR command
  frames, 16 shader-handle definitions, 1,249 uses, 1,241 cache-hit frames, 24 effect-handle definitions, four
  effect-handle uses, and `bad_pixel_ratio=0.04991`. This is focused descriptor-cap change 4 after the 2026-06-13
  11:44 full parity sweep, so broad parity remains deferred:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-132328/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-runtime-effect-stable-color-filter` from a 144-definition effect-handle
  cap to 72 after historical rows showed 54-60 definitions. Exact validation passed with no fallback, 890 JBR command
  frames, 54 effect-handle definitions, 1,395 uses, 1,386 cache-hit frames, 1,394 RuntimeEffect source-cache hits, one
  surface-change marker, and `bad_pixel_ratio=0.04668`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-131956/suite.tsv`.
- Magic Jewel tightened `parity-resize-runtime-effect-stable-color-filter` from a 96-definition effect-handle cap to
  48 after historical rows showed 36-42 definitions. Exact validation passed with the row's expected single fallback,
  841 JBR command frames, 42 effect-handle definitions, 1,338 uses, 1,331 cache-hit frames, 1,336 RuntimeEffect
  source-cache hits, one surface-change marker, and `bad_pixel_ratio=0.04467`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-131720/suite.tsv`.
- Magic Jewel tightened `parity-runtime-effect-stable-color-filter` from a 64-definition effect-handle cap to 24 after
  historical rows showed exactly 24 definitions. Exact validation passed with no fallback, 948 JBR command frames, 24
  effect-handle definitions, 1,391 uses, 1,387 cache-hit frames, 1,390 RuntimeEffect source-cache hits, and
  `bad_pixel_ratio=0.05042`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-131424/suite.tsv`.
- Magic Jewel refreshed the exact `parity-rich` screenshot row after the benchmark smoke:
  `CASES=parity-rich ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no fallback, zero picture frames, 645
  JBR command frames, `avg_delta=2.123`, and `bad_pixel_ratio=0.05044`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-131153/suite.tsv`.
- Magic Jewel refreshed the focused command benchmark smoke after the artifact slice:
  `CASES=commands DURATION_SECONDS=5 WARMUP_SECONDS=1 EXPECT_SCREENSHOT_ASSERTION=false
  ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, 474 JBR command frames, two new-side CPU samples,
  and 94.8 command FPS:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260613-131013/suite.tsv`.
- Magic Jewel refreshed the required artifact matrix slice after the full compatibility matrix:
  `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2.
  `current-all` replayed 711 JBR command frames with no fallback, and `missing-public-api` produced one structured
  `public-api-missing` fallback with zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260613-130810/matrix.tsv`.
- Magic Jewel completed the full compatibility matrix after the batched screenshot parity reset:
  `EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-compatibility-matrix.sh` passed 57/57 with
  `fallback_sum=56`. The `happy` row replayed 475 JBR command frames with no fallback, every forced mismatch/API row
  produced exactly one structured fallback with zero command frames, and all rows stayed in background-window mode:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260613-124241/matrix.tsv`.
- Magic Jewel completed the batched full default screenshot parity sweep after ten focused descriptor-cap tightenings.
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture frames, 103,309
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-114403/suite.tsv`.
- Magic Jewel added a 24-definition effect-handle cap to `parity-runtime-effect-color-filter` after historical rows
  showed 18-24 definitions. Exact validation passed with no fallback, 850 JBR command frames, 24 effect-handle
  definitions, 1,290 uses, 1,286 cache-hit frames, 1,289 RuntimeEffect source-cache hits, and
  `bad_pixel_ratio=0.05037`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-114224/suite.tsv`.
- Magic Jewel added a 12-definition shader-handle cap to `parity-runtime-effect-shader` after historical rows showed
  9-12 definitions. Exact validation passed with no fallback, 913 JBR command frames, 12 shader-handle definitions,
  1,369 uses, 1,365 cache-hit frames, 1,368 RuntimeEffect source-cache hits, and `bad_pixel_ratio=0.05008`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-114010/suite.tsv`.
- Magic Jewel added a 12-definition shader-handle cap to `parity-runtime-effect-child-only` after historical rows
  showed 9-12 definitions. Exact validation passed with no fallback, 966 JBR command frames, 12 shader-handle
  definitions, 1,479 uses, 1,475 cache-hit frames, 1,478 RuntimeEffect source-cache hits, and
  `bad_pixel_ratio=0.05029`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-113823/suite.tsv`.
- Magic Jewel added a 4-definition shader-handle cap to `parity-runtime-effect-uniform-only` after historical rows
  showed 3-4 definitions. Exact validation passed with no fallback, 558 JBR command frames, three shader-handle
  definitions, 927 uses, 924 cache-hit frames, 926 RuntimeEffect source-cache hits, and
  `bad_pixel_ratio=0.04997`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-113426/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-runtime-effect-pure-color` from a 24-definition shader-handle cap to 12
  after historical rows showed 7-12 definitions. Exact validation passed with no fallback, 554 JBR command frames,
  eight shader-handle definitions, 935 uses, 927 cache-hit frames, 934 RuntimeEffect source-cache hits, one
  surface-change marker, and `bad_pixel_ratio=0.04619`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-113128/suite.tsv`.
- Magic Jewel tightened `parity-resize-runtime-effect-pure-color` from a 16-definition shader-handle cap to 8 after
  historical rows showed 6-8 definitions. Exact validation passed with the row's expected single fallback, 694 JBR
  command frames, seven shader-handle definitions, 1,108 uses, 1,101 cache-hit frames, 1,106 RuntimeEffect
  source-cache hits, and `bad_pixel_ratio=0.04438`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-112823/suite.tsv`.
- Magic Jewel tightened `parity-runtime-effect-pure-color` from an 8-definition shader-handle cap to 4 after
  historical rows showed 3-4 definitions. Corrected exact validation passed with no fallback, 526 JBR command frames,
  four shader-handle definitions, 891 uses, 887 cache-hit frames, 890 RuntimeEffect source-cache hits, and
  `bad_pixel_ratio=0.04993`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-112515/suite.tsv`.
- Magic Jewel added a 12-definition shader-handle cap to `parity-runtime-effect-shader-color-filter` after historical
  rows showed 6-8 definitions. Exact validation passed with no fallback, 673 JBR command frames, eight shader-handle
  definitions, 1,044 uses, 1,036 cache-hit frames, 1,039 RuntimeEffect source-cache hits, and
  `bad_pixel_ratio=0.05006`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-112055/suite.tsv`.
- Magic Jewel tightened `parity-transformed-shader` from a 16-definition shader-handle cap to 12 after historical
  rows showed 6-10 definitions. Exact validation passed with no fallback, 675 JBR command frames, six shader-handle
  definitions, 1,024 uses, 1,021 cache-hit frames, `avg_delta=2.110`, and `bad_pixel_ratio=0.05007`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-111754/suite.tsv`.
- Magic Jewel tightened `parity-linear-gradient-shader-color-filter` from a 24-definition shader-handle cap to 12
  after historical rows showed 6-10 definitions. Exact validation passed with no fallback, 515 JBR command frames,
  eight shader-handle definitions, 955 uses, 947 cache-hit frames, `avg_delta=2.107`, and
  `bad_pixel_ratio=0.04997`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-111254/suite.tsv`.
- Magic Jewel completed the batched full default screenshot parity sweep after ten focused descriptor-cap tightenings.
  `./scripts/jbr-skia-screenshot-parity-suite.sh` passed 106/106 with `fallback_sum=11`, zero picture frames, 85,489
  JBR command frames, mean `avg_delta=2.158`, and mean `bad_pixel_ratio=0.05158`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-100819/suite.tsv`.
- Magic Jewel tightened `parity-image-shader-color-filter` from a 24-definition shader-handle cap to 12 after
  historical rows showed 6-10 definitions. Exact validation passed with no fallback, 695 JBR command frames, eight
  shader-handle definitions, 1,178 uses, 1,170 cache-hit frames, `avg_delta=2.110`, and `bad_pixel_ratio=0.05002`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-100541/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-turbulence-shader` from a 24-definition shader-handle cap to 12 after
  historical rows showed 7-12 definitions. Exact validation passed with no fallback, 708 JBR command frames, nine
  shader-handle definitions, 1,081 uses, 1,072 cache-hit frames, `avg_delta=1.978`, and `bad_pixel_ratio=0.04661`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-100137/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-noise-shader` from a 24-definition shader-handle cap to 12 after
  historical rows showed 7-11 definitions. Exact validation passed with no fallback, 814 JBR command frames, nine
  shader-handle definitions, 1,250 uses, 1,241 cache-hit frames, `avg_delta=1.975`, and `bad_pixel_ratio=0.04639`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-095920/suite.tsv`.
- Magic Jewel tightened `parity-forced-context-color-shader` from a 24-definition shader-handle cap to 12 after
  historical rows showed 7-11 definitions. Exact validation passed with no fallback, 804 JBR command frames, nine
  shader-handle definitions, 1,389 uses, 1,380 cache-hit frames, `avg_delta=1.974`, and `bad_pixel_ratio=0.04646`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-095658/suite.tsv`.
- Magic Jewel tightened `parity-resize-turbulence-shader` from a 16-definition shader-handle cap to 8 after historical
  rows showed 6-8 definitions. Exact validation passed with the row's expected single fallback, 654 JBR command
  frames, seven shader-handle definitions, 1,069 uses, 1,062 cache-hit frames, `avg_delta=1.850`, and
  `bad_pixel_ratio=0.04455`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-095431/suite.tsv`.
- Magic Jewel tightened `parity-resize-noise-shader` from a 16-definition shader-handle cap to 8 after historical
  rows showed 6-8 definitions. Exact validation passed with the row's expected single fallback, 592 JBR command
  frames, seven shader-handle definitions, 1,067 uses, 1,060 cache-hit frames, `avg_delta=1.849`, and
  `bad_pixel_ratio=0.04439`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-095222/suite.tsv`.
- Magic Jewel tightened `parity-resize-color-shader` from a 16-definition shader-handle cap to 8 after historical
  rows showed 6-8 definitions. Exact validation passed with the row's expected single fallback, 550 JBR command
  frames, seven shader-handle definitions, 940 uses, 933 cache-hit frames, `avg_delta=1.850`, and
  `bad_pixel_ratio=0.04454`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-094953/suite.tsv`.
- Magic Jewel tightened `parity-turbulence-shader` from an 8-definition shader-handle cap to 4 after prior rows showed
  exactly four definitions. Exact validation passed with no fallback, 784 JBR command frames, four shader-handle
  definitions, 1,323 uses, 1,319 cache-hit frames, `avg_delta=2.119`, and `bad_pixel_ratio=0.05035`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-094416/suite.tsv`.
- Magic Jewel tightened `parity-noise-shader` from an 8-definition shader-handle cap to 4 after prior rows showed
  3-4 definitions. Exact validation passed with no fallback, 658 JBR command frames, four shader-handle definitions,
  1,204 uses, 1,200 cache-hit frames, `avg_delta=2.116`, and `bad_pixel_ratio=0.05013`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-094115/suite.tsv`.
- Magic Jewel tightened `parity-color-shader` from a loose 8-definition shader-handle cap to 4 after historical rows
  showed 3-4 definitions. Exact validation passed with no fallback, 586 JBR command frames, three shader-handle
  definitions, 995 uses, 992 cache-hit frames, `avg_delta=2.115`, and `bad_pixel_ratio=0.05020`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-093718/suite.tsv`.
- Magic Jewel refreshed an exact screenshot parity row after the benchmark smoke:
  `CASES=parity-rich ./scripts/jbr-skia-screenshot-parity-suite.sh` passed with no fallback, 642 JBR command frames,
  `avg_delta=2.123`, and `bad_pixel_ratio=0.05044`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260613-093333/suite.tsv`.
- Magic Jewel refreshed a single-case benchmark smoke check after the artifact matrix slice:
  `CASES=commands DURATION_SECONDS=5 WARMUP_SECONDS=1 EXPECT_SCREENSHOT_ASSERTION=false
  ./scripts/jbr-skia-benchmark-suite.sh` passed with no fallback, 404 JBR command frames, two new-side CPU samples,
  and 80.8 command FPS:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260613-093152/suite.tsv`.
- Magic Jewel refreshed the required artifact matrix slice after the local artifact rebuild and focused compatibility
  checks. `CASE_GROUPS=required EXPECT_SCREENSHOT_ASSERTION=false ./scripts/jbr-skia-artifact-matrix.sh` passed 2/2:
  `current-all` replayed 418 JBR command frames with no fallback, and `missing-public-api` produced one structured
  `public-api-missing` fallback with zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260613-092936/matrix.tsv`.
- Local JBR API/desktop/native artifacts were refreshed after focused compatibility `happy` and
  `commands-native-bridge-load-library` checks initially fell back with `service-unavailable`. After
  `./scripts/rebuild-jbr-skia-local-artifacts.sh`, exact bridge-load command validation passed with 895 JBR command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260613-092558/suite.tsv`.
- Focused compatibility checks were kept narrow per the batching policy. Exact `happy` passed with no fallback, 184
  JBR command frames, and background-window mode; exact `public-api-missing` passed with one structured fallback, zero
  command frames, and background-window mode:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260613-092753/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260613-092719/matrix.tsv`.
- Magic Jewel completed a full default command-probe sweep after the focused quick-loop refresh batch, using
  structured markers while local screenshot capture remains unavailable. It passed 549/549 with `fallback_sum=350`, 79
  unsupported-picture rows, 78,827 picture frames, and 156,233 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-144955/suite.tsv`.
- Magic Jewel refreshed the tiny focused command-probe quick-loop batch after `smoke`, covering gradient path/stops,
  gradient geometry/color-count/stroke/radius, image shader invalid, shader-ref invalid, fill-rect color-filter
  invalid, and blend-mode invalid groups. It passed 38/38 with `fallback_sum=10`, 28 unsupported-picture rows, 27,825
  picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-134712/suite.tsv`.
- Magic Jewel refreshed the focused `smoke` command-probe group after `stream-invalid`, using structured markers while
  local screenshot capture remains unavailable. It passed 6/6 with no fallback, no unsupported-picture rows, no
  picture frames, and 6,694 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-133407/suite.tsv`.
- Magic Jewel refreshed the focused `stream-invalid` command-probe group after `save-layer-shader-fallbacks`, using
  structured markers while local screenshot capture remains unavailable. It passed 8/8 with `fallback_sum=8`, no
  unsupported-picture rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-132344/suite.tsv`.
- Magic Jewel refreshed the focused `save-layer-shader-fallbacks` command-probe group after `graphics-layer-extras`,
  using structured markers while local screenshot capture remains unavailable. It passed 9/9 with no fallback, six
  unsupported-picture rows, 6,049 picture frames, and 3,221 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-131608/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer-extras` command-probe group after `graphics-layer`, using
  structured markers while local screenshot capture remains unavailable. It passed 16/16 with no fallback, four
  unsupported-picture rows, 4,104 picture frames, and 12,671 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-125529/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer` command-probe group after `surface-transform-ui`, using
  structured markers while local screenshot capture remains unavailable. It passed 22/22 with no fallback, one
  unsupported-picture row, 1,038 picture frames, and 26,873 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-122521/suite.tsv`.
- Magic Jewel refreshed the focused `surface-transform-ui` command-probe group after `runtime-effect-invalid`, using
  structured markers while local screenshot capture remains unavailable. It passed 15/15 with no fallback, no
  unsupported-picture rows, no picture frames, and 23,174 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-121443/suite.tsv`.
- Magic Jewel refreshed the focused `runtime-effect-invalid` command-probe group after `gradient-invalid`, using
  structured markers while local screenshot capture remains unavailable. It passed 62/62 with `fallback_sum=56`, seven
  unsupported-picture rows, 7,153 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-112117/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-invalid` command-probe group after `gradient-path-invalid`, using
  structured markers while local screenshot capture remains unavailable. It passed 81/81 with `fallback_sum=60`, 21
  unsupported-picture rows, 17,899 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-102137/suite.tsv`.
- Subagent validation was attempted for `gradient-invalid` and `runtime-effect-invalid`, but the delegated Magic Jewel
  GUI runs produced no app/CMP/JBR frames on their first rows. Serial exact-row rerun passed the same first rows 2/2,
  so the subagent failures are treated as validation-environment failures rather than command regressions.
- Magic Jewel refreshed the focused `gradient-path-invalid` command-probe group after
  `shader-composition-runtime`, using structured markers while local screenshot capture remains unavailable. It
  passed 21/21 with `fallback_sum=18`, three unsupported-picture rows, 5,088 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-042752/suite.tsv`.
- Magic Jewel refreshed the focused `shader-composition-runtime` command-probe group after `shader-rendering`, using
  structured markers while local screenshot capture remains unavailable. It passed 15/15 with `fallback_sum=0`, two
  unsupported-picture rows, 3,599 picture frames, and 34,803 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-041606/suite.tsv`.
- Magic Jewel refreshed the focused `shader-rendering` command-probe group after `native-text`, using structured
  markers while local screenshot capture remains unavailable. It passed 18/18 with `fallback_sum=0`, ten
  unsupported-picture rows, 17,170 picture frames, and 21,380 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-040250/suite.tsv`.
- Magic Jewel refreshed the focused `native-text` command-probe group after `core-effects`, using structured markers
  while local screenshot capture remains unavailable. It passed 14/14 with `fallback_sum=0`, no unsupported-picture
  rows, no picture frames, and 33,822 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-035202/suite.tsv`.
- Magic Jewel refreshed the focused `core-effects` command-probe group after `descriptor-lifecycle`, using structured
  markers while local screenshot capture remains unavailable. It passed 8/8 with `fallback_sum=0`, three
  unsupported-picture rows, 5,099 picture frames, and 13,321 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-034519/suite.tsv`.
- Magic Jewel refreshed the focused `descriptor-lifecycle` command-probe group after `color-filters`, using structured
  markers while local screenshot capture remains unavailable. It passed 18/18 with `fallback_sum=0`, no
  unsupported-picture rows, no picture frames, and 49,005 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-032814/suite.tsv`.
- Magic Jewel refreshed the focused `color-filters` command-probe group after `graphics-layer-invalid`, using
  structured markers while local screenshot capture remains unavailable. It passed 13/13 with `fallback_sum=0`, three
  unsupported-picture rows, 5,056 picture frames, and 28,945 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-031803/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer-invalid` command-probe group after `save-layer-invalid`, using
  structured markers while local screenshot capture remains unavailable. It passed 15/15 with `fallback_sum=0`, 15
  unsupported-picture rows, 26,029 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-030703/suite.tsv`.
- Magic Jewel refreshed the focused `save-layer-invalid` command-probe group after `descriptor-handles-invalid`,
  using structured markers while local screenshot capture remains unavailable. It passed 37/37 with `fallback_sum=37`,
  no unsupported-picture rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-024126/suite.tsv`.
- Magic Jewel refreshed the focused `descriptor-handles-invalid` command-probe group after `image-handles-invalid`,
  using structured markers while local screenshot capture remains unavailable. It passed 48/48 with `fallback_sum=48`,
  no unsupported-picture rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-020826/suite.tsv`.
- Magic Jewel refreshed the focused `image-handles-invalid` command-probe group after `shader-descriptor-invalid`,
  using structured markers while local screenshot capture remains unavailable. It passed 27/27 with `fallback_sum=27`,
  no unsupported-picture rows, no picture frames, and 1,523 command frames from setup before fallback:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-014928/suite.tsv`.
- Magic Jewel refreshed the focused `shader-descriptor-invalid` command-probe group after `effect-descriptor-invalid`,
  using structured markers while local screenshot capture remains unavailable. It passed 30/30 with `fallback_sum=30`,
  no unsupported-picture rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-012756/suite.tsv`.
- Magic Jewel refreshed the focused `effect-descriptor-invalid` command-probe group after `path-invalid`, using
  structured markers while local screenshot capture remains unavailable. It passed 28/28 with `fallback_sum=28`,
  no unsupported-picture rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-010824/suite.tsv`.
- Magic Jewel refreshed the focused `path-invalid` command-probe group after `primitive-invalid`, using structured
  markers while local screenshot capture remains unavailable. It passed 24/24 with `fallback_sum=22`, two
  unsupported-picture rows, 3,181 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-005139/suite.tsv`.
- Magic Jewel refreshed the focused `primitive-invalid` command-probe group after `native-text-invalid`, using
  structured markers while local screenshot capture remains unavailable. It passed 16/16 with `fallback_sum=13`,
  three unsupported-picture rows, 5,096 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-003919/suite.tsv`.
- Magic Jewel refreshed the focused `native-text-invalid` command-probe group after the full structured-marker sweep.
  It passed 11/11 with `fallback_sum=11`, no unsupported-picture rows, no picture frames, and 1,996 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-003034/suite.tsv`.
- Magic Jewel completed a full default command-probe sweep after the saveLayer/shader fallback tail repair, using
  `EXPECT_SCREENSHOT_ASSERTION=false` because local macOS screenshot capture could not create a window/region image.
  Structured command/fallback validation passed 549/549 with `fallback_sum=350`, 80 unsupported-picture rows, 127,181
  picture frames, and 306,312 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-181446/suite.tsv`.
- Magic Jewel repaired the command-probe saveLayer/shader fallback tail after a broad default sweep reached 543 green
  rows and exposed a `not-run` generic screenshot assertion on `commands-save-layer-raw-color-filter-fallback` despite
  correct structured fallback markers. Exact saveLayer command/fallback rows passed 4/4, then
  `CASE_GROUPS=save-layer-shader-fallbacks` passed 9/9 with `fallback_sum=0`, six unsupported-picture rows, 9,440
  picture frames, and 7,901 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-180417/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer-extras` command-probe group after `graphics-layer`. It passed
  16/16 with `fallback_sum=0`, four unsupported-picture rows, 5,374 picture frames, and 22,521 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-114204/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer` command-probe group after the shadow guard audit. It passed
  22/22 with `fallback_sum=0`, one expected unsupported-picture row, 1,401 picture frames, and 32,675 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-112529/suite.tsv`.
- Magic Jewel refreshed the focused graphics-layer shadow command probes while auditing the internal
  `graphicsLayer:shadowFilter` guard. Exact `CASES` validation passed 5/5: supported rectangular/round/path shadow
  rows produced 5,274 command frames with no unsupported reasons, and invalid elevation/path rows produced expected
  picture fallback summaries:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-112032/suite.tsv`.
- CMP added defensive unit coverage for the recorder-only `roundRectStyle` fallback branch. A direct
  `rejectsUnknownRoundRectPaintStyleInStrictMode` run passed, then the full focused `JbrSkiaCommandRecorderTest`
  class passed with `BUILD SUCCESSFUL` in `/Users/rock3r/src/jbr-skia-zero-copy/cmp`.
- CMP now reports gradient path stroke paint fallbacks with path-specific unsupported reasons before attempting to
  materialize generic gradient payloads. Focused `JbrSkiaCommandRecorderTest` passed, exact Magic Jewel `CASES`
  validation passed 3/3 with `linearGradientPathPaint`, `radialGradientPathPaint`, and `sweepGradientPathPaint`
  summaries, and the named `gradient-path-stroke-fallbacks` group refreshed 3/3 with 3,603 picture frames and no
  command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-110959/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-111207/suite.tsv`.
- Magic Jewel refreshed the combined `shader-rendering color-filters` command-probe groups after image fallback marker
  hardening. It passed 31/31 with `fallback_sum=0`, 15,814 picture frames from expected fallback sentinels, and 30,519
  command frames from supported rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-104452/suite.tsv`.
- Magic Jewel hardened image-paint fallback command probes to require the live recorder `image=1` marker in addition
  to the paint-specific unsupported reason. Exact `CASES` validation passed 2/2 with 2,431 picture frames and no
  command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-104135/suite.tsv`.
- Magic Jewel hardened the graphics-layer color-matrix screenshot parity rows with strict effect-handle define/use and
  cache-hit gates. Exact `CASES` validation passed 2/2 with no fallback, no picture frames, and 2,870 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-103423/suite.tsv`.
- Magic Jewel completed a full default benchmark suite after the parser/API and report-validation refreshes. It passed
  5/5 with no fallback, 82 old-side CPU samples, 80 new-side CPU samples, and 13,724 command frames across the command
  cases:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260611-102359/suite.tsv`.
- JBR parser/API helper refreshed against the current local overlay artifacts:
  `REBUILD_LOCAL_ARTIFACTS=false ./scripts/test-jbr-skia-api.sh` patched the temporary JBRApi stub into the
  java.desktop overlay, compiled `JBRSkiaApiTest`, ran it headlessly, and printed `JBR_SKIA_API_TEST passed`.
- Magic Jewel report-validation unit script refreshed after the CMP recorder validation:
  `./scripts/test-jbr-skia-report-validation.sh` passed with `JBR_SKIA_REPORT_VALIDATION_TESTS passed`; its expected
  negative strict-validation case emitted the temporary failure report line.
- CMP refreshed the focused recorder validation after the source-level unsupported-reason scan:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest` passed in
  `/Users/rock3r/src/jbr-skia-zero-copy/cmp` with `BUILD SUCCESSFUL`.
- Cross-repo ABI/capability drift audit refreshed cleanly after the matrix and Skiko checkpoints. JBR private API,
  JBR API mirror, Skiko discovery/stream writer, and CMP recorder are still aligned on command stream ABI 106 and
  native ABI 3; low-word tail `COMMAND_CAP64_SAVE_LAYER_BLEND_COLOR_FILTER_REF` and high-word tail bits through
  Perlin-noise shaders and drawVertices match the current 262143 high-capability mask.
- Skiko refreshed the focused `JbrSkiaInteropTest` class after the compatibility and artifact matrix refreshes:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  passed in `/Users/rock3r/src/jbr-skia-zero-copy/skiko/skiko` with `BUILD SUCCESSFUL`.
- Magic Jewel refreshed the required artifact matrix on the current ABI 106 local artifacts after the compatibility
  refresh. Required rows passed 2/2: `current-all` replayed 654 command frames with no fallback, `missing-public-api`
  fell back once as expected, and the five optional old-artifact rows were skipped because no old bundle variables
  were set:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260611-101223/matrix.tsv`.
- Magic Jewel refreshed the full compatibility matrix after the full command and screenshot parity sweeps. No-run
  discovery still resolved 57 rows. The matrix passed 57/57 with `fallback_sum=56`; the happy path produced 474 JBR
  command frames, all mismatch rows produced one structured fallback with no command frames, and all rows stayed in
  background-window mode:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260611-094313/matrix.tsv`.
- Magic Jewel completed a full 106-row default screenshot parity sweep after the focused visual parity refresh batch.
  It passed 106/106 with `fallback_sum=12`, no picture frames, 102,236 command frames, mean `avg_delta=2.158`, and
  mean `bad_pixel_ratio=0.05158`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-083428/suite.tsv`.
- Magic Jewel refreshed the focused `smoke` screenshot parity group after `graphics-layer-effects`. It passed 3/3
  with no fallback, no picture frames, 3,176 command frames, mean `avg_delta=2.398`, and mean
  `bad_pixel_ratio=0.05816`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-082953/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer-effects` screenshot parity group after `descriptor-lifecycle`. It
  passed 14/14 with `fallback_sum=2`, no picture frames, 17,833 command frames, mean `avg_delta=2.341`, and mean
  `bad_pixel_ratio=0.05725`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-081932/suite.tsv`.
- Magic Jewel refreshed the focused `descriptor-lifecycle` screenshot parity group after `core-drawing`. It passed
  6/6 with `fallback_sum=1`, no picture frames, 6,461 command frames, mean `avg_delta=2.127`, and mean
  `bad_pixel_ratio=0.05078`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-081244/suite.tsv`.
- Magic Jewel refreshed the focused `core-drawing` screenshot parity group after `native-text`. It passed 16/16 with
  no fallback, no picture frames, 15,732 command frames, mean `avg_delta=2.175`, and mean
  `bad_pixel_ratio=0.05197`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-080125/suite.tsv`.
- Magic Jewel refreshed the focused `native-text` screenshot parity group after `runtime-effect`. It passed 14/14
  with `fallback_sum=3`, no picture frames, 11,672 command frames, mean `avg_delta=2.037`, and mean
  `bad_pixel_ratio=0.04758`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-075039/suite.tsv`.
- Magic Jewel refreshed the focused `runtime-effect` screenshot parity group after `shader-rendering`. It passed
  14/14 with `fallback_sum=2`, no picture frames, 11,139 command frames, mean `avg_delta=2.054`, and mean
  `bad_pixel_ratio=0.04879`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-074046/suite.tsv`.
- Magic Jewel refreshed the focused `shader-rendering` screenshot parity group after the graphics-layer visual groups.
  It passed 18/18 with `fallback_sum=4`, no picture frames, 13,787 command frames, mean `avg_delta=2.081`, and mean
  `bad_pixel_ratio=0.04995`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-072815/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer-clip-shadow-transform` screenshot parity group after
  `graphics-layer-basic`. It passed 14/14 with no fallback, no picture frames, 19,849 command frames, mean
  `avg_delta=2.223`, and mean `bad_pixel_ratio=0.05296`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-071806/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer-basic` screenshot parity group after the full command sweep. It
  passed 7/7 with no fallback, no picture frames, 8,142 command frames, mean `avg_delta=2.194`, and mean
  `bad_pixel_ratio=0.05223`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260611-071236/suite.tsv`.
- Magic Jewel completed a full 549-row default command-probe sweep after the focused group refresh batch. It passed
  549/549 with `fallback_sum=350`, 79 unsupported-picture rows, 87,188 picture frames, and 188,270 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-012205/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-invalid` command-probe group after `runtime-effect-invalid`. It passed
  81/81 with `fallback_sum=60`, 21 unsupported-picture rows, 23,049 picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260611-003036/suite.tsv`.
- Magic Jewel refreshed the focused `runtime-effect-invalid` command-probe group after
  `descriptor-handles-invalid`. It passed 62/62 with `fallback_sum=56`, six unsupported-picture rows, 6,399 picture
  frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-234517/suite.tsv`.
- Magic Jewel refreshed the focused `descriptor-handles-invalid` command-probe group after `save-layer-invalid`. It
  passed 48/48 with `fallback_sum=48`, no unsupported rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-231048/suite.tsv`.
- Magic Jewel refreshed the focused `save-layer-invalid` command-probe group after `gradient-path-invalid`. It passed
  37/37 with `fallback_sum=37`, no unsupported rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-224435/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-path-invalid` command-probe group after `graphics-layer-extras`. It
  passed 21/21 with `fallback_sum=18`, three unsupported-picture rows, 3,542 picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-222857/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer-extras` command-probe group after `graphics-layer`. It passed 16/16
  with no fallback, four unsupported-picture rows, 4,221 picture frames, and 15,698 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-221556/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer` command-probe group after `surface-transform-ui`. It passed 22/22
  with no fallback, one unsupported-picture row, 1,071 picture frames, and 31,583 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-215954/suite.tsv`.
- Magic Jewel refreshed the focused `surface-transform-ui` command-probe group after `graphics-layer-invalid`. It
  passed 15/15 with no fallback, no unsupported rows, no picture frames, and 18,966 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-214838/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer-invalid` command-probe group after `native-text`. It passed 15/15
  with no fallback, 15 unsupported-picture rows, 15,986 picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-213721/suite.tsv`.
- Magic Jewel refreshed the focused `native-text` command-probe group after `image-handles-invalid`. It passed 14/14
  with no fallback, no unsupported rows, no picture frames, and 17,781 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-212619/suite.tsv`.
- Magic Jewel refreshed the focused `image-handles-invalid` command-probe group after `shader-descriptor-invalid`. It
  passed 27/27 with `fallback_sum=27`, no unsupported rows, no picture frames, and 892 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-210711/suite.tsv`.
- Magic Jewel refreshed the focused `shader-descriptor-invalid` command-probe group after `effect-descriptor-invalid`.
  It passed 30/30 with `fallback_sum=30`, no unsupported rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-204523/suite.tsv`.
- Magic Jewel refreshed the focused `effect-descriptor-invalid` command-probe group after `descriptor-lifecycle`. It
  passed 28/28 with `fallback_sum=28`, no unsupported rows, no picture frames, and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-202519/suite.tsv`.
- Magic Jewel refreshed the focused `descriptor-lifecycle` command-probe group after `color-filters`. It passed 18/18
  with no unsupported rows, no picture frames, and 22,552 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-200920/suite.tsv`.
- Magic Jewel refreshed the focused `color-filters` command-probe group after shader composition/runtime validation.
  It passed 13/13 with three unsupported-picture rows, 2,738 picture frames, and 11,697 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-195849/suite.tsv`.
- Magic Jewel refreshed the focused `shader-composition-runtime` command-probe group after `shader-rendering`. It
  passed 15/15 with two unsupported-picture rows, 1,690 picture frames, and 17,687 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-194645/suite.tsv`.
- Magic Jewel refreshed the focused `shader-rendering` command-probe group after `core-effects`. It passed 18/18 with
  ten unsupported-picture rows, 9,154 picture frames, and 10,048 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-193254/suite.tsv`.
- Magic Jewel refreshed the focused `core-effects` command-probe group after `save-layer-shader-fallbacks`. It passed
  8/8 with three unsupported-picture rows, 3,051 picture frames, and 6,875 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-192454/suite.tsv`.
- Magic Jewel refreshed the focused `save-layer-shader-fallbacks` command-probe group after the gradient path groups.
  It passed 9/9 with six unsupported-picture rows, 6,984 picture frames, and 4,511 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-191700/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-path-stroke-fallbacks` command-probe group after path-structure
  validation. It passed 3/3 with three unsupported-picture rows, 2,923 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-191342/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-path-structure-invalid` command-probe group after the public gradient
  shape trio. It passed 3/3 with three unsupported-picture rows, 3,236 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-191022/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-stroke-round-rect-radius-invalid` command-probe group after round-rect
  radius validation. It passed 3/3 with three unsupported-picture rows, 3,360 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-190701/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-round-rect-radius-invalid` command-probe group after stroke-width
  validation. It passed 3/3 with three unsupported-picture rows, 3,126 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-190334/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-stroke-width-invalid` command-probe group after color-count validation.
  It passed 3/3 with three unsupported-picture rows, 3,610 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-185958/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-color-count-invalid` command-probe group after
  `gradient-geometry-invalid`. It passed 3/3 with three unsupported-picture rows, 3,035 picture frames, and zero
  command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-185616/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-geometry-invalid` command-probe group after `gradient-stop-invalid`.
  It passed 3/3 with three unsupported-picture rows, 3,107 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-185303/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-stop-invalid` command-probe group after `image-shader-invalid`. It
  passed 4/4 with four unsupported-picture rows, 4,226 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-184901/suite.tsv`.
- Magic Jewel refreshed the one-row `image-shader-invalid` command-probe group after the color-filter invalid refresh.
  It passed 1/1 with `imageShaderImage`, one unsupported-picture row, 1,034 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-184709/suite.tsv`.
- Magic Jewel refreshed the focused `fill-rect-color-filter-invalid` command-probe group after `shader-ref-invalid`.
  It passed 6/6 with `fallback_sum=5`, one unsupported-picture row, 1,061 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-184147/suite.tsv`.
- Magic Jewel refreshed the focused `shader-ref-invalid` command-probe group after `blend-mode-invalid`. It passed
  3/3 with `fallback_sum=3`, zero unsupported rows, and zero replay frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-183824/suite.tsv`.
- Magic Jewel refreshed the focused `blend-mode-invalid` command-probe group after `path-invalid`. It passed 3/3 with
  `fallback_sum=2`, one unsupported-picture row carrying `blendMode_Clear`, 957 picture frames, and zero command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-183512/suite.tsv`.
- Magic Jewel refreshed the focused `path-invalid` command-probe group after `primitive-invalid`. It passed 24/24
  with `fallback_sum=22`, two unsupported-picture rows, 2,125 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-181836/suite.tsv`.
- Magic Jewel refreshed the focused `primitive-invalid` command-probe group after `native-text-invalid`. It passed
  16/16 with `fallback_sum=13`, three unsupported-picture rows, 3,196 picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-180642/suite.tsv`.
- Magic Jewel refreshed the focused `native-text-invalid` command-probe group after the ABI drift audit. It passed
  11/11 with `fallback_sum=11`, no unsupported rows, no picture frames, and 1,369 command frames from the
  font-data record-flags row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-175826/suite.tsv`.
- Cross-repo ABI/capability drift audit refreshed cleanly after the matrix checkpoints. Shared ABI 106 constants match
  across the JBR private API, JBR API mirror, JBR native parser subset, Skiko discovery/layer subsets, and the CMP
  recorder subset. The public mirror exposes 65 low-word capability bits with required mask `-1`
  (`0xffffffffffffffff`) and 18 high-word bits with required mask `262143` (`0x000000000003ffff`); no shared constant
  mismatches were found.
- Magic Jewel refreshed the required artifact matrix on the current ABI 106 local artifacts after the compatibility
  refresh. Required rows passed 2/2: `current-all` replayed commands with 334 JBR command frames and no fallback, while
  `missing-public-api` produced the expected `public-api-missing` fallback with zero command frames; both rows used
  background-window mode:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260610-175047/matrix.tsv`.
- Magic Jewel refreshed the full compatibility matrix after the 549-row command-probe consolidation. No-run discovery
  still resolves 57 rows across handshake, low/high capability, and public-API fallback groups with no ungrouped
  cases. The matrix passed 57/57 with `fallback_sum=56`, the happy path produced 573 JBR command frames, all mismatch
  rows fell back without command replay, and every row stayed in background-window mode:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260610-171921/matrix.tsv`.
- Magic Jewel completed a 549-row default command-probe consolidation after the public gradient shape expansion. The
  first broad attempts exposed launch-only SIGTERM/no-sample interruptions on the first JBR-side process of a suite
  invocation, so the passing evidence is split across exact `commands-live-animation`, focused `stream-invalid`, and a
  suffix from `commands-native-bridge-load-library` through the final fallback row. Combined result: 549/549 passed,
  `fallback_sum=350`, 79 unsupported-picture rows, 69,200 picture frames, and 111,092 command frames across:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-103258/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-104522/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-105340/suite.tsv`.
- CMP focused recorder validation passed on the current unsupported-reason audit state:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`. This re-anchors the remaining mismatch classifications
  against current code, including nested graphics-layer child/header invariants, shadow replay, gradient path paint
  guards, and the latest public gradient shape rows.
- Magic Jewel refreshed the expanded `gradient-invalid` command-probe group as a split run after an environment refresh
  interrupted the first attempt after four passing rows. Combined result: 81/81 passed, `fallback_sum=60`, 21
  unsupported-picture rows, 22,222 picture frames, and zero command frames across:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-091843/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260610-092520/suite.tsv`.
- Magic Jewel added and validated public low-level canvas sentinels for the three gradient stroke-round-rect radius
  guards. Exact linear validation passed first, then `gradient-stroke-round-rect-radius-invalid` passed 3/3 with
  `linearGradientStrokeRoundRectRadius`, `radialGradientStrokeRoundRectRadius`, and
  `sweepGradientStrokeRoundRectRadius`, all on unsupported-picture fallback with zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-231026/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-231339/suite.tsv`.
  The remaining unsupported-reason mismatch list is now down to dynamic/internal/defensive candidates:
  `blendMode_${blendMode.toReasonToken()}`, graphics-layer child/header/shadow internals, gradient path-paint shadows,
  `roundRectStyle`, and `unsupportedScope`.
- Magic Jewel now has public app-level low-level canvas sentinels for gradient stroke-width and round-rect-radius
  fallback guards. Exact linear-gradient radius and stroke-width rows passed, then the compact
  `gradient-round-rect-radius-invalid` and `gradient-stroke-width-invalid` groups passed 3/3 each with family-specific
  unsupported reasons and zero command frames. Discovery now reports 546 default command-probe rows, with no ungrouped
  or duplicate cases:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-225707/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-225806/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-225855/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-230157/suite.tsv`.
- CMP now hardens public non-finite `ColorMatrixColorFilter` construction by using a benign Skia fallback filter while
  preserving the original matrix metadata for strict recording. The full `JbrSkiaCommandRecorderTest` class passed.
  Magic Jewel added and validated `commands-color-matrix-filter-nonfinite-fallback`; exact validation passed 1/1 with
  `colorMatrixNonfinite`, and the adjacent `fill-rect-color-filter-invalid` group now passes 6/6:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-205901/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-222615/suite.tsv`.
- Magic Jewel added and validated `commands-graphics-layer-unsupported-child-fallback`, a public app-level sentinel that
  draws raw-shader content inside an otherwise valid graphics layer. Strict recording falls back structurally with the
  public parent reason `graphicsLayer:childCommands`; the synthetic `graphicsLayer:childUnsupported` guard remains
  covered by CMP unit tests. The exact row passed 1/1, and the adjacent `graphics-layer-extras` group passed 16/16:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-165600/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-180353/suite.tsv`.
- Magic Jewel completed a full default command-probe consolidation after the focused invalid-slice refreshes. The sweep
  passed 538/538 with `fallback_sum=350`, 68 unsupported-picture rows, 82,301 JBR picture frames, and 200,046 JBR
  command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-094809/suite.tsv`.
- Magic Jewel refreshed the focused `surface-transform-ui` command-probe group. The group passed 15/15 with no
  fallback/unsupported rows, zero picture frames, and 21,502 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-093056/suite.tsv`.
- Magic Jewel refreshed the focused `smoke` command-probe group after the invalid-slice batch. The group passed 6/6
  with no fallback/unsupported rows, zero picture frames, and 8,625 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-092508/suite.tsv`.
- Magic Jewel refreshed the focused `blend-mode-invalid` command-probe group. The group passed 3/3 with
  `fallback_sum=2`, one unsupported-picture row from the live vertices `BlendMode.Clear` sentinel, and zero command
  frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-092118/suite.tsv`.
- Magic Jewel refreshed the focused `fill-rect-color-filter-invalid` command-probe group. The group passed 5/5 with
  `fallback_sum=5`, zero unsupported rows, and zero picture/command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-091041/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-stop-invalid` command-probe group. The group passed 4/4 with live
  linear/radial/sweep gradient stop/points unsupported-picture fallbacks and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-090605/suite.tsv`.
- Magic Jewel refreshed the focused `gradient-color-count-invalid` command-probe group. The group passed 3/3 with live
  linear/radial/sweep gradient color-count unsupported-picture fallbacks and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-090205/suite.tsv`.
- Magic Jewel refreshed the focused `graphics-layer-invalid` command-probe group. The group passed 15/15 with zero
  command frames across invalid size, transform scalar, camera, shadow, blend, and unrecorded-layer fallbacks:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260609-082414/suite.tsv`.
- Magic Jewel refreshed the focused `primitive-invalid` command-probe group. The group passed 16/16 with
  `fallback_sum=13`, three unsupported-picture rows from live `blendLayerBounds`/`transform`/`points` sentinels, 3,189
  picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-222055/suite.tsv`.
- Magic Jewel no-run discovery still reports 538 default command-probe rows with no ungrouped or duplicate cases. The
  one-row `image-shader-invalid` quick group also passed with `imageShaderImage`, one unsupported-picture row, 1,109
  picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-221233/suite.tsv`.
- Magic Jewel refreshed the focused `path-invalid` command-probe group after the native-text invalid slice. The group
  passed 24/24 with `fallback_sum=22`, two unsupported-picture rows from live `clipPath`/`path` sentinels, 1,805
  picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-214338/suite.tsv`.
- Magic Jewel refreshed the focused `native-text-invalid` command-probe group after the graphics-layer child stream
  guard audit. The group passed 11/11 with `fallback_sum=11`, zero unsupported rows, zero picture frames, and 1,165
  command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-194120/suite.tsv`.
- CMP now has focused strict-mode unit coverage for the nested graphics-layer child stream integrity guards:
  `graphicsLayer:childCommands`, `graphicsLayer:childUnsupported`, `graphicsLayer:childHeaderSize`, and
  `graphicsLayer:childHeader`. These are internal nested-recording corruption/unsupported-child guards, not separate
  public app command shapes. The focused `JbrSkiaCommandRecorderTest` class passed after adding the coverage.
- Magic Jewel added the radial/sweep public companions for the invalid-gradient-geometry fallback checkpoint:
  `commands-radial-gradient-invalid-geometry-fallback`, `commands-sweep-gradient-invalid-geometry-fallback`, and a
  compact `gradient-geometry-invalid` quick group. Discovery now resolves 538 default command-probe rows and
  `gradient-invalid` 72 rows. The exact radial/sweep slice passed 2/2 with zero command frames, and the compact
  linear/radial/sweep geometry group passed 3/3 with zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-173150/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-173634/suite.tsv`.
- CMP and Magic Jewel now cover public invalid linear-gradient geometry without an EDT crash. CMP's Skiko gradient
  factories keep JBR metadata while substituting a harmless solid Skia shader for invalid linear/radial/sweep geometry,
  letting strict recording report `linearGradientPoints`/geometry guards and fall back structurally. The focused
  `JbrSkiaCommandRecorderTest` class passed, Magic Jewel discovery now resolves 536 default command-probe rows, and
  the new exact `commands-linear-gradient-invalid-points-fallback` plus adjacent `gradient-stop-invalid` quick group
  passed with zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-152702/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-162415/suite.tsv`.
- Magic Jewel completed the 535-row default command-probe consolidation as a split run after stale `/tmp` local JBR
  artifacts caused command-canvas `service-unavailable` in the first broad sweep. Rebuilding the local API shim,
  desktop patch, and native bridge restored command replay; Magic Jewel also hardened the report parser so interleaved
  log tokens such as `unsupported=6ecc00` are not treated as numeric unsupported counts. Combined split result:
  535/535 passed, `fallback_sum=350`, 65 unsupported-picture rows, 74,635 JBR picture frames, and 193,776 JBR command
  frames across:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-194343/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-091146/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-092405/suite.tsv`,
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260608-092509/suite.tsv`.
- Magic Jewel added `commands-vertices-invalid-blend-mode-fallback` after the recorder unsupported-reason audit found
  public `Canvas.drawVertices(..., BlendMode.Clear, ...)` can reach CMP's dynamic unsupported blend-mode guard. The
  row proves CMP reports `blendMode_Clear` and the public `vertices` fallback before command replay. No-run discovery
  now resolves 535 default rows, `CASE_GROUPS=blend-mode-invalid` resolves 3 rows, and ungrouped plus duplicate-case
  checks printed no rows. The exact row passed with one unsupported-picture row, 972 JBR picture frames, and zero
  command frames; the adjacent blend-mode-invalid group passed 3/3 with `fallback_sum=2`, one unsupported-picture row,
  1,683 JBR picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-193518/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-193626/suite.tsv`.
- Magic Jewel added `commands-invalid-concat-transform-fallback` after the recorder unsupported-reason audit found
  public `Canvas.concat(Matrix)` can reach CMP's live non-finite transform guard. The row feeds a `Float.NaN`
  translation through the public concat path and proves CMP reports `transform` before command replay. No-run discovery
  now resolves 534 default rows, `CASE_GROUPS=primitive-invalid` resolves 16 rows, and ungrouped plus duplicate-case
  checks printed no rows. The exact row passed with one unsupported-picture row, 963 JBR picture frames, and zero
  command frames; the adjacent primitive-invalid group passed 16/16 with `fallback_sum=13`, three unsupported-picture
  rows, 3,043 JBR picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-160032/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-160142/suite.tsv`.
- Magic Jewel added `commands-invalid-point-dots-fallback` after the recorder unsupported-reason audit found public
  `Canvas.drawPoints(PointMode.Points, ...)` can reach CMP's live non-finite point-coordinate guard. The row feeds one
  `Float.NaN` point through the public point-dots path and proves CMP reports `points` before command replay. No-run
  discovery now resolves 533 default rows, `CASE_GROUPS=primitive-invalid` resolves 15 rows, and ungrouped plus
  duplicate-case checks printed no rows. The exact row passed with one unsupported-picture row, 852 JBR picture frames,
  and zero command frames; the adjacent primitive-invalid group passed 15/15 with `fallback_sum=13`, two
  unsupported-picture rows, 2,115 JBR picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-154421/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-154533/suite.tsv`.
- Magic Jewel added `commands-invalid-blend-layer-bounds-fallback` after the recorder unsupported-reason audit found
  public non-finite primitive coordinates can reach CMP's ABI-neutral blend-layer wrapper. The row draws a
  `BlendMode.Plus` line with a non-finite start coordinate and proves CMP reports `blendLayerBounds` before emitting an
  incomplete command stream. No-run discovery now resolves 532 default rows, `CASE_GROUPS=primitive-invalid` resolves
  14 rows, and ungrouped plus duplicate-case checks printed no rows. The exact row passed with one unsupported-picture
  row, 766 JBR picture frames, and zero command frames; the adjacent primitive-invalid group passed 14/14 with
  `fallback_sum=13`, one unsupported-picture row, 946 JBR picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-122501/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-122615/suite.tsv`.
- Magic Jewel added `commands-save-layer-blend-color-filter` to prove the public Compose plain `Canvas.saveLayer`
  blend+color-filter path now records the existing combined saveLayer opcode at app level. No-run discovery now
  resolves 531 default command-probe rows, `CASE_GROUPS=save-layer-shader-fallbacks` resolves 9 rows, and ungrouped
  plus duplicate-case checks printed no rows. The exact row passed with zero fallback, zero unsupported rows, zero
  picture frames, and 2,062 command frames; the adjacent group passed 9/9 with `fallback_sum=0`, six intentional
  unsupported-picture rows, 6,195 JBR picture frames, and 4,079 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-084917/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260607-111237/suite.tsv`.
- CMP commit `f7bb15f788b` keeps plain saveLayer blend+color-filter combinations on command replay: direct tint
  filters plus blend use `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER`, descriptor-backed color filters plus blend use
  `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF`, and supported descriptor filters without blend route through the
  handle-backed saveLayer path. The focused two-test slice and full `JbrSkiaCommandRecorderTest` class both passed.
- Magic Jewel completed a periodic full default command-probe consolidation after the blend sentinel batch. Discovery
  resolved 530 default rows with no ungrouped or duplicate cases, and the sweep passed 530/530 with
  `fallback_sum=350`, 62 intentional unsupported-picture rows, 67,344 JBR picture frames, and 171,452 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-174336/suite.tsv`.
- Magic Jewel added `commands-color-shader-blend-mode` to prove the JBR-owned shader descriptor paint blend-mode replay
  path at app level. The exact row passed with zero fallback, zero unsupported rows, zero picture frames, and 1,225
  command frames while enforcing shader-handle lifecycle gates; the refreshed `CASE_GROUPS=shader-rendering`
  discovery now resolves 18 rows and passed 18/18 with `fallback_sum=0`, ten intentional unsupported-picture rows from
  raw/invalid shader fallback sentinels, 10,135 JBR picture frames, and 10,015 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-172738/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-172840/suite.tsv`.
- Magic Jewel added `commands-image-shader-blend-mode` to prove the image-shader rect paint blend-mode replay path at
  app level. The exact row passed with zero fallback, zero unsupported rows, zero picture frames, and 1,281 command
  frames; the refreshed `CASE_GROUPS=shader-rendering` discovery now resolves 17 rows and passed 17/17 with
  `fallback_sum=0`, ten intentional unsupported-picture rows from raw/invalid shader fallback sentinels, 9,922 JBR
  picture frames, and 8,322 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-171148/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-171254/suite.tsv`.
- Magic Jewel added `commands-image-blend-mode` to prove the image-ref paint blend-mode replay path at app level. The
  exact row passed with zero fallback, zero unsupported rows, zero picture frames, and 1,257 command frames; the
  refreshed `CASE_GROUPS=shader-rendering` discovery now resolves 16 rows and passed 16/16 with `fallback_sum=0`, ten
  intentional unsupported-picture rows from raw/invalid shader fallback sentinels, 12,644 JBR picture frames, and
  9,740 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-165652/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-165819/suite.tsv`.
- Magic Jewel added `commands-radial-gradient-stroke-blend-mode` and
  `commands-sweep-gradient-round-rect-blend-mode` to prove the remaining unit-covered gradient blend-layer shapes at
  app level. The exact two-row slice passed with zero fallback, zero unsupported rows, zero picture frames, and 2,374
  command frames; the refreshed `CASE_GROUPS=surface-transform-ui` discovery now resolves 15 rows and passed 15/15
  with `fallback_sum=0`, zero unsupported-picture rows, zero JBR picture frames, and 21,548 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-164027/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-164210/suite.tsv`.
- Magic Jewel added `commands-linear-gradient-path-blend-mode` to prove the gradient filled-path paint blend-mode
  replay path at app level. The exact row passed with zero fallback, zero unsupported rows, zero picture frames, and
  1,379 command frames; the refreshed `CASE_GROUPS=surface-transform-ui` discovery now resolves 13 rows and passed
  13/13 with `fallback_sum=0`, zero unsupported-picture rows, zero JBR picture frames, and 18,553 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-162640/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-162742/suite.tsv`.
- Magic Jewel added `commands-linear-gradient-blend-mode` to prove the new linear-gradient paint blend-mode replay path
  at app level. The exact row passed with zero fallback, zero unsupported rows, zero picture frames, and 1,418 command
  frames; the refreshed `CASE_GROUPS=surface-transform-ui` discovery now resolves 12 rows and passed 12/12 with
  `fallback_sum=0`, zero unsupported-picture rows, zero JBR picture frames, and 17,354 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-161407/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-161521/suite.tsv`.
- Magic Jewel added `commands-color-filter-blend-mode` to prove the new fill-rect color-filter paint blend-mode
  closure at app level. The exact row passed with zero fallback, zero unsupported rows, zero picture frames, and 1,223
  command frames; the refreshed `CASE_GROUPS=color-filters` discovery now resolves 13 rows and passed 13/13 with
  `fallback_sum=0`, three intentional unsupported-picture rows, 3,253 JBR picture frames, and 15,199 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-155652/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260606-155803/suite.tsv`.
- CMP now records supported paint-level non-`SrcOver` blend modes for fill-rect color-filter commands, including direct
  tint filters and handle-backed tint/color-matrix/lighting/descriptor filters, by wrapping the existing native
  color-filter rect command records in tight `COMMAND_SAVE_LAYER_BLEND_MODE` layers. Focused
  `JbrSkiaCommandRecorderTest` desktop validation passed.
- CMP now records supported paint-level non-`SrcOver` blend modes for linear, radial, and sweep gradient rects,
  round-rects, and filled paths by wrapping the existing gradient command records in tight
  `COMMAND_SAVE_LAYER_BLEND_MODE` layers over fill/stroke/path bounds. Focused `JbrSkiaCommandRecorderTest` desktop
  validation passed.
- CMP now records supported paint-level non-`SrcOver` blend modes for image refs, image-shader rects, and shader
  descriptor rects by wrapping the existing image/shader command records in tight `COMMAND_SAVE_LAYER_BLEND_MODE`
  layers over destination/fill bounds. Focused `JbrSkiaCommandRecorderTest` desktop validation passed.
- CMP now records supported paint-level non-`SrcOver` blend modes for `drawVertices` by wrapping the existing
  `COMMAND_DRAW_VERTICES` record in a tight `COMMAND_SAVE_LAYER_BLEND_MODE` layer over vertex bounds. Focused
  `JbrSkiaCommandRecorderTest` desktop validation passed.
- CMP also records supported non-`SrcOver` blend modes for dashed solid primitives and path-effect descriptor paths by
  wrapping the existing dashed/path-effect command records in tight `COMMAND_SAVE_LAYER_BLEND_MODE` layers with
  stroke-padded bounds. Focused `JbrSkiaCommandRecorderTest` desktop validation passed after this follow-up.
- CMP now records supported non-`SrcOver` solid-color primitive blend modes for lines, stroked/fill rect fallbacks,
  round-rects, ovals, arcs, paths, points, and raw points by wrapping the existing primitive command in a tight
  `COMMAND_SAVE_LAYER_BLEND_MODE`/restore pair when no direct primitive blend opcode exists. This is ABI-neutral and
  preserves the existing direct `COMMAND_FILL_RECT_BLEND_MODE` path. Focused recorder validation passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests
  androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- Skiko now defaults `JbrSkiaSwingLayer` to command replay when CMP enables the JBR interop layer and no explicit
  Skiko diagnostic/picture/texture render mode is requested. Magic Jewel added an `auto` render mode that leaves the
  Skiko render-mode properties unset, then validated a short app smoke in that mode. The run reported
  `SKIKO_JBR_INTEROP_RENDER_MODE commands=true picture=false diagnostic=false texture=false delegateCommands=true`,
  zero fallback markers, zero picture frames, 847 Skiko/JBR command frames, and 848 CMP command-recorder frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-interop-report/20260606-145913/report.md`.
- Cross-repo ABI/capability audit found no constant drift across the JBR private API, JBR API mirror, JBR native
  replay, Skiko interop gate, and CMP recorder after normalizing Java/Kotlin/C++ numeric literal syntax. Shared
  constants reported zero value mismatches. JBR private/API mirrors both expose 212 parsed constants. Native replay
  defines 67 command opcodes and has a switch case for all 67; CMP's 65 emitted opcode constants are all present in
  native replay. CMP's extra `COMMAND_STREAM_ABI_ID` is a local stream-header alias set to 106.
- Magic Jewel refreshed the full default screenshot parity suite after the command, compatibility, artifact, and Skiko
  interop gates. The run was split into a 3-row `CASE_GROUPS=smoke` prefix and a resumed
  `CASES_FROM=parity-skew-transform` tail so already-green smoke rows were not repeated. Combined result: 106/106
  passed with `fallback_sum=11`, zero JBR picture frames, and 105,068 JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260606-133617/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260606-133836/suite.tsv`.
- Magic Jewel refreshed the full compatibility matrix after the 522-row command-probe consolidation. No-run discovery
  still resolved 57 rows. The matrix passed 57/57 with `fallback_sum=56`, the happy path produced 824 JBR command
  frames, every forced ABI/capability/public-API mismatch produced exactly one structured fallback with no command
  frames, and all rows used background-window mode:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260606-031518/matrix.tsv`.
- Magic Jewel refreshed the required artifact matrix on the current ABI 106 local artifacts. Required rows passed 2/2:
  `current-all` replayed 754 JBR command frames with no fallback, while `missing-public-api` produced the expected
  single structured fallback and no command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260606-133156/matrix.tsv`.
- Skiko refreshed the focused `JbrSkiaInteropTest` class against the current branch/artifacts:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Magic Jewel completed a periodic full default command-probe consolidation after the focused refresh batch. Discovery
  still resolved 522 default rows with no ungrouped or duplicate cases. The full sweep passed 522/522 with
  `fallback_sum=350`, 61 intentional unsupported-picture rows, 81,988 JBR picture frames, and 171,971 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-212906/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=gradient-path-stroke-fallbacks` as the stroked gradient-path fallback checkpoint.
  The run passed 3/3 with `fallback_sum=0`, three intentional unsupported-picture rows, 3,864 JBR picture frames, and
  zero command frames across linear/radial/sweep gradient-paint fallback ordering:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-212527/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=surface-transform-ui` as a compact UI/surface replay checkpoint. The run passed
  11/11 with `fallback_sum=0`, zero unsupported-picture rows, zero JBR picture frames, and 16,494 command frames across
  native bridge load-library, points, transforms, gradients, popup/menu layering, and text-image rows:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-211704/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=graphics-layer` as the supported replay pair for the recent layer invalid/extras
  refreshes. The run passed 22/22 with `fallback_sum=0`, one intentional unsupported-picture row from invalid shadow
  elevation, 1,152 JBR picture frames, and 31,905 command frames across base layers, clips, blend/filter/effect,
  shadows, and 2D/3D transforms:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-205726/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=native-text` as the supported native text replay pair for the recently refreshed
  `native-text-invalid` parser guards. The run passed 14/14 with `fallback_sum=0`, zero unsupported-picture rows, zero
  JBR picture frames, and 19,683 command frames across custom/generic/loaded/resource/system fonts plus resize and
  forced-context variants:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-204659/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=shader-composition-runtime` as a supported shader composition and RuntimeEffect
  descriptor checkpoint. The run passed 15/15 with `fallback_sum=0`, two intentional unsupported-picture rows from raw
  RuntimeEffect shader/color-filter fallbacks, 2,043 JBR picture frames, and 16,907 command frames; supported rows
  stayed on command replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-203324/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=graphics-layer-extras` after disabling the generic green-pixel screenshot
  assertion for the raw table graphics-layer color-filter fallback row while preserving the required
  `graphicsLayer:colorFilter` fallback assertion. Exact
  `commands-graphics-layer-raw-table-color-filter-fallback` passed 1/1, and the group rerun passed 15/15 with
  `fallback_sum=0`, three intentional unsupported-picture rows, 3,097 JBR picture frames, and 13,989 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-202046/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-202136/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=core-effects` after the sidecar unsupported-reason audit. No-run discovery
  resolved 8 rows covering gradient stroke, image filter, path effect, path-effect/color-filter fallback, raw discrete
  path-effect fallback, vertices, vertices raw color-filter fallback, and blend mode. The run passed 8/8 with
  `fallback_sum=0`, three intentional unsupported-picture rows, 2,815 JBR picture frames, and 5,031 command frames;
  supported rows stayed on command replay:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-195810/suite.tsv`.
- Unsupported-reason sidecar audit found no remaining app-reachable undocumented recorder/layer guard. Existing live
  rows already cover `graphicsLayer:shadowPath`, `linearGradientPath`, `radialGradientPath`, `sweepGradientPath`, and
  `imageShaderImage`; the remaining reviewed layer/paint reasons stay defensive or shadowed by earlier generic guards.
- Magic Jewel refreshed `CASE_GROUPS=blend-mode-invalid` as a focused fill-rect blend-mode parser checkpoint. The run
  passed 2/2 with `fallback_sum=2`, zero unsupported-picture rows, zero JBR picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-195246/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=save-layer-shader-fallbacks` after disabling the generic green-pixel screenshot
  assertion for the raw table saveLayer fallback row while preserving the required `saveLayer` fallback assertion.
  Exact `commands-save-layer-raw-table-color-filter-fallback` passed 1/1, and the group rerun passed 8/8 with
  `fallback_sum=0`, six intentional unsupported-picture rows, 6,120 JBR picture frames, and 2,254 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-194218/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-194310/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=descriptor-lifecycle` as a focused descriptor lifecycle checkpoint. No-run
  discovery resolved 18 rows covering eviction, resize/forced-context redefinition, stable RuntimeEffect color-filter
  reuse, and RuntimeEffect source-cache eviction. The run passed 18/18 with `fallback_sum=0`, zero
  unsupported-picture rows, zero JBR picture frames, and 22,342 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-192007/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=color-filters` as a focused color-filter replay/fallback checkpoint. No-run
  discovery resolved 12 rows covering image color-matrix, raw image/table/blend color-filter fallback,
  tint/color-matrix/lighting descriptors, descriptor handle reuse, and graphics-layer color-filter/blend combinations.
  The run passed 12/12 with `fallback_sum=0`, three intentional unsupported-picture rows, 4,499 JBR picture frames,
  and 15,627 command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-185044/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=shader-descriptor-invalid` as a focused shader descriptor parser checkpoint.
  No-run discovery resolved 30 rows covering shader descriptor headers plus color, transformed, composite, gradient,
  image shader, and Perlin/noise descriptor payload guards. The run passed 30/30 with `fallback_sum=30`, zero
  unsupported-picture rows, zero JBR picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-182940/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=effect-descriptor-invalid` as a focused descriptor parser checkpoint. No-run
  discovery resolved 28 rows covering effect descriptor headers plus lighting, tint, color-matrix, blur/offset
  image-filter, and corner/stamped/chain path-effect descriptor payload guards. The run passed 28/28 with
  `fallback_sum=28`, zero unsupported-picture rows, zero JBR picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-180727/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=path-invalid` as a focused path parser/live-structure checkpoint. No-run
  discovery resolved 24 rows covering live invalid clip/draw paths plus clip/draw/stroke/shadow path verb and dash
  path-effect parser guards. The run passed 24/24 with `fallback_sum=22`, two intentional unsupported-picture rows,
  2,075 JBR picture frames, and zero command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-174939/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=native-text-invalid` as a focused native-text parser checkpoint. No-run discovery
  resolved 11 rows covering text and paragraph font size/weight/width/slant/family-count guards plus font-data
  record-flags. The run passed 11/11 with `fallback_sum=11`, zero unsupported-picture rows, zero JBR picture frames,
  and 1,041 command frames from the font-data record-flags row:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-174043/suite.tsv`.
- Magic Jewel refreshed `CASE_GROUPS=primitive-invalid` as a focused parser/primitive checkpoint. No-run discovery
  resolved 13 rows covering stroke-cap, transform flags, clip operation, draw-points count/length, and draw-vertices
  count/mode/blend/index corruptions. The run passed 13/13 with `fallback_sum=13`, zero unsupported-picture rows,
  zero JBR picture frames, and zero JBR command frames:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-172804/suite.tsv`.
- Magic Jewel added a live invalid image-shader image fallback sentinel so CMP's app-level `imageShaderImage` guard
  now has command-probe coverage through a public `ImageShader` backed by an oversized `ImageBitmap(2049, 1)`. No-run
  discovery reports 522 default command-probe rows, a new `image-shader-invalid` quick group with 1 row,
  `shader-rendering` 15, and no ungrouped or duplicate default rows. Focused
  `CASES=commands-image-shader-invalid-image-fallback` passed 1/1 with `fallback_sum=0`, one intentional
  unsupported-picture row, 1,467 JBR picture frames, and zero command frames. Adjacent
  `CASE_GROUPS=shader-rendering` passed 15/15 with `fallback_sum=0`, ten intentional unsupported-picture rows, 10,204
  JBR picture frames, and 6,076 command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-161033/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-171211/suite.tsv`.
- Magic Jewel added live invalid gradient-path structure sentinels so CMP's app-level `linearGradientPath`,
  `radialGradientPath`, and `sweepGradientPath` guards now have command-probe coverage from public gradient-filled
  paths with non-finite path data. No-run discovery reports 521 default command-probe rows, a new
  `gradient-path-structure-invalid` quick group with 3 rows, `gradient-path-invalid` 21, `gradient-invalid` 69, and no
  ungrouped or duplicate default rows. Focused `CASE_GROUPS=gradient-path-structure-invalid` passed 3/3 with
  `fallback_sum=0`, three intentional unsupported-picture rows, 3,003 JBR picture frames, and zero command frames.
  Adjacent `CASE_GROUPS=gradient-path-invalid` passed 21/21 with `fallback_sum=18`, three intentional
  unsupported-picture rows, 2,946 JBR picture frames, and zero command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-154145/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-154451/suite.tsv`.
- Magic Jewel added a live invalid graphics-layer shadow-path fallback sentinel so CMP's `graphicsLayer:shadowPath`
  guard now has app-level command-probe coverage through a public `GenericShape` with non-finite path data and positive
  shadow elevation. No-run discovery reports 518 default command-probe rows, `graphics-layer-invalid` 15, and no
  ungrouped or duplicate default rows. Focused `CASES=commands-graphics-layer-invalid-shadow-path-fallback` passed
  1/1 with `fallback_sum=0`, one intentional unsupported-picture row, 961 JBR picture frames, and zero command frames.
  Adjacent `CASE_GROUPS=graphics-layer-invalid` passed 15/15 with `fallback_sum=0`, fifteen intentional
  unsupported-picture rows, 14,768 JBR picture frames, and zero command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-151839/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-151944/suite.tsv`.
- Magic Jewel added live invalid gradient color-count sentinels so CMP's app-level `linearGradientColorCount`,
  `radialGradientColorCount`, and `sweepGradientColorCount` guards now have command-probe coverage from public Brush
  calls with 17 colors. No-run discovery reports 517 default command-probe rows, a new
  `gradient-color-count-invalid` quick group with 3 rows, `gradient-invalid` 66, and no ungrouped rows. Focused
  `CASE_GROUPS=gradient-color-count-invalid` passed 3/3 with `fallback_sum=0`, three intentional
  unsupported-picture rows, 2,989 JBR picture frames, and zero command frames. Adjacent
  `CASE_GROUPS=gradient-invalid` passed 66/66 with `fallback_sum=60`, six intentional unsupported-picture rows, 5,587
  JBR picture frames, and zero command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-141516/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-141803/suite.tsv`.
- Superseded recorder audit note: the earlier public `colorMatrixNonfinite` attempt failed at Skia color-filter
  construction with `Can't wrap nullptr`, so no sentinel was landed at that time. CMP now preserves non-finite matrix
  metadata with a benign native fallback filter, and Magic Jewel covers the live `colorMatrixNonfinite` fallback.
- Recorder audit note: CMP still contains gradient geometry unsupported reasons (`linearGradientPoints`,
  `radialGradientGeometry`, and `sweepGradientGeometry`), but public non-finite Brush geometry is rejected while
  constructing the underlying Skia shader before CMP can record a live unsupported frame. Parser-corruption coverage
  still owns malformed command payload geometry; keep these live recorder guards classified as defensive around
  malformed JBR gradient metadata unless a lower-level public construction path appears.
- Recorder audit note: `roundRectStyle` is defensive behind public Skia `PaintingStyle` enum values that are already
  Fill/Stroke in Compose drawing, and `linearGradientPathPaint`/`radialGradientPathPaint`/`sweepGradientPathPaint` are
  shadowed by the generic gradient-paint helpers, which reject stroked path gradients first as
  `linearGradientPaint`/`radialGradientPaint`/`sweepGradientPaint`.
- Recorder audit note: strict nested graphics-layer recording nulls child command streams when a child records any
  unsupported reason, so live public probes surface through `graphicsLayer:childCommands` plus the child reason. The
  later `graphicsLayer:childUnsupported`, `graphicsLayer:childHeaderSize`, and `graphicsLayer:childHeader` checks are
  defensive invariants for non-strict or internally corrupted nested recordings.
- Recorder audit note: with `graphicsLayer:shadowPath` now covered by a live invalid `Outline.Generic` probe, the
  remaining `graphicsLayer:shadow` and `graphicsLayer:shadowFilter` reasons are defensive. Layer validation rejects
  invalid size/elevation before shadow replay, and the fallback shadow blur descriptor is synthesized with finite
  positive sigma plus an accepted tile mode.
- Magic Jewel added live invalid gradient-stop sentinels so CMP's app-level `linearGradientStops` and
  `radialGradientStops` guards now have command-probe coverage alongside the existing live `sweepGradientStops` row.
  No-run discovery reports 514 default command-probe rows, a new `gradient-stop-invalid` quick group with 3 rows,
  `gradient-invalid` 63, and no ungrouped rows. Focused `CASE_GROUPS=gradient-stop-invalid` passed 3/3 with
  `fallback_sum=0`, three intentional unsupported-picture rows, 2,947 JBR picture frames, and zero command frames.
  Adjacent `CASE_GROUPS=gradient-invalid` passed 63/63 with `fallback_sum=60`, three intentional unsupported-picture
  rows, 3,215 JBR picture frames, and zero command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-091009/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260605-091243/suite.tsv`.
- Magic Jewel added live invalid path-structure fallback sentinels so CMP's app-level `clipPath` and `path` guards now
  have command-probe coverage instead of only parser-corruption coverage. No-run discovery reports 512 default
  command-probe rows, `path-invalid` 24, and no ungrouped rows. Focused exact validation for
  `commands-clip-path-invalid-fallback` and `commands-draw-path-invalid-fallback` passed 2/2 with `fallback_sum=0`,
  two intentional unsupported-picture rows, 1,967 JBR picture frames, and zero command frames. Adjacent
  `CASE_GROUPS=path-invalid` passed 24/24 with `fallback_sum=22`, two intentional unsupported-picture rows, 1,789 JBR
  picture frames, and zero command frames. The invalid clip path also reports `unsupportedScope`, covering that parent
  unsupported reason from live recorder output. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-233653/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-233834/suite.tsv`.
- Magic Jewel added graphics-layer invalid size fallback sentinels so the layer validation guards now have app-level
  coverage for `graphicsLayer:sizeWidth` and `graphicsLayer:sizeHeight`. No-run discovery reports 510 default
  command-probe rows, `graphics-layer-invalid` 14, and no ungrouped rows. Focused exact validation for the two new
  rows passed 2/2 with 1,953 JBR picture frames and zero command frames; adjacent `CASE_GROUPS=graphics-layer-invalid`
  passed 14/14 with `fallback_sum=0`, fourteen intentional unsupported-picture rows, 14,685 picture frames, and zero
  command frames. The remaining shadow/clip outline checks are defensive because `Outline` is sealed to the accepted
  `Rectangle`, `Rounded`, and `Generic` variants. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-204923/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-205101/suite.tsv`.
- Magic Jewel added an unrecorded graphics-layer fallback sentinel so the layer lifecycle guard now has app-level
  coverage for `graphicsLayer:recording`. No-run discovery reports 508 default command-probe rows,
  `graphics-layer-invalid` 12, and no ungrouped rows. Focused `CASES=commands-graphics-layer-unrecorded-fallback`
  passed 1/1 with the `graphicsLayer:recording` unsupported reason present, 891 JBR picture frames, and zero command
  frames; adjacent `CASE_GROUPS=graphics-layer-invalid` passed 12/12 with `fallback_sum=0`, twelve intentional
  unsupported-picture rows, 11,611 picture frames, and zero command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-203529/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-203629/suite.tsv`.
- Magic Jewel added a graphics-layer invalid blend-mode fallback sentinel so the layer validation guard now has
  app-level coverage for unsupported `blendMode`. No-run discovery reports 507 default command-probe rows,
  `graphics-layer-invalid` 11, and no ungrouped rows. Focused
  `CASES=commands-graphics-layer-invalid-blend-mode-fallback` passed 1/1 with the `graphicsLayer:blendMode`
  unsupported reason present, 1,168 JBR picture frames, and zero command frames; adjacent
  `CASE_GROUPS=graphics-layer-invalid` passed 11/11 with `fallback_sum=0`, eleven intentional unsupported-picture
  rows, 11,972 picture frames, and zero command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-202228/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-202314/suite.tsv`.
- Magic Jewel added a graphics-layer invalid rotationY fallback sentinel so the layer validation guard now has
  app-level coverage for non-finite `rotationY`. No-run discovery reports 506 default command-probe rows,
  `graphics-layer-invalid` 10, and no ungrouped rows. Focused
  `CASES=commands-graphics-layer-invalid-rotation-y-fallback` passed 1/1 with the `graphicsLayer:rotationY`
  unsupported reason present, 1,031 JBR picture frames, and zero command frames; adjacent
  `CASE_GROUPS=graphics-layer-invalid` passed 10/10 with `fallback_sum=0`, ten intentional unsupported-picture rows,
  10,196 picture frames, and zero command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-200608/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-200710/suite.tsv`.
- Magic Jewel added a graphics-layer invalid rotationX fallback sentinel so the layer validation guard now has
  app-level coverage for non-finite `rotationX`. No-run discovery reports 505 default command-probe rows,
  `graphics-layer-invalid` 9, and no ungrouped rows. Focused
  `CASES=commands-graphics-layer-invalid-rotation-x-fallback` passed 1/1 with the `graphicsLayer:rotationX`
  unsupported reason present, 1,054 JBR picture frames, and zero command frames; adjacent
  `CASE_GROUPS=graphics-layer-invalid` passed 9/9 with `fallback_sum=0`, nine intentional unsupported-picture rows,
  9,404 picture frames, and zero command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-195334/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-195437/suite.tsv`.
- Magic Jewel added a graphics-layer invalid translationY fallback sentinel so the layer validation guard now has
  app-level coverage for non-finite `translationY`. No-run discovery reports 504 default command-probe rows,
  `graphics-layer-invalid` 8, and no ungrouped rows. Focused
  `CASES=commands-graphics-layer-invalid-translation-y-fallback` passed 1/1 with the `graphicsLayer:translationY`
  unsupported reason present, 1,034 JBR picture frames, and zero command frames; adjacent
  `CASE_GROUPS=graphics-layer-invalid` passed 8/8 with `fallback_sum=0`, eight intentional unsupported-picture rows,
  7,906 picture frames, and zero command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-193442/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-193540/suite.tsv`.
- Magic Jewel added a graphics-layer invalid translationX fallback sentinel so the layer validation guard now has
  app-level coverage for non-finite `translationX`. No-run discovery reports 503 default command-probe rows,
  `graphics-layer-invalid` 7, and no ungrouped rows. Focused
  `CASES=commands-graphics-layer-invalid-translation-x-fallback` passed 1/1 with the `graphicsLayer:translationX`
  unsupported reason present, 1,062 JBR picture frames, and zero command frames; adjacent
  `CASE_GROUPS=graphics-layer-invalid` passed 7/7 with `fallback_sum=0`, seven intentional unsupported-picture rows,
  7,355 picture frames, and zero command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-192056/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-192154/suite.tsv`.
- Magic Jewel added a graphics-layer invalid rotationZ fallback sentinel so the layer validation guard now has
  app-level coverage for non-finite `rotationZ`. No-run discovery reports 502 default command-probe rows,
  `graphics-layer-invalid` 6, and no ungrouped rows. Focused
  `CASES=commands-graphics-layer-invalid-rotation-z-fallback` passed 1/1 with the `graphicsLayer:rotationZ`
  unsupported reason present, 1,055 JBR picture frames, and zero command frames; adjacent
  `CASE_GROUPS=graphics-layer-invalid` passed 6/6 with `fallback_sum=0`, six intentional unsupported-picture rows,
  6,172 picture frames, and zero command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-190925/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-191023/suite.tsv`.
- Magic Jewel added a graphics-layer invalid scaleY fallback sentinel so the layer validation guard now has app-level
  coverage for non-finite `scaleY`. No-run discovery reports 501 default command-probe rows,
  `graphics-layer-invalid` 5, and no ungrouped rows. Focused
  `CASES=commands-graphics-layer-invalid-scale-y-fallback` passed 1/1 with the `graphicsLayer:scaleY` unsupported
  reason present, 937 JBR picture frames, and zero command frames; adjacent `CASE_GROUPS=graphics-layer-invalid`
  passed 5/5 with `fallback_sum=0`, five intentional unsupported-picture rows, 4,669 picture frames, and zero command
  frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-185751/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-185856/suite.tsv`.
- Magic Jewel added a graphics-layer invalid scaleX fallback sentinel so the layer validation guard now has app-level
  coverage for non-finite `scaleX`. No-run discovery reports 500 default command-probe rows,
  `graphics-layer-invalid` 4, and no ungrouped rows. Focused
  `CASES=commands-graphics-layer-invalid-scale-x-fallback` passed 1/1 with the `graphicsLayer:scaleX` unsupported
  reason present, 953 JBR picture frames, and zero command frames; adjacent `CASE_GROUPS=graphics-layer-invalid`
  passed 4/4 with `fallback_sum=0`, four intentional unsupported-picture rows, 3,783 picture frames, and zero command
  frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-184926/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-185029/suite.tsv`.
- Magic Jewel added a graphics-layer invalid camera-distance fallback sentinel so the layer validation guard now has
  app-level coverage for non-positive `cameraDistance`. No-run discovery reports 499 default command-probe rows,
  `graphics-layer-invalid` 3, and no ungrouped rows. Focused
  `CASES=commands-graphics-layer-invalid-camera-distance-fallback` passed 1/1 with the
  `graphicsLayer:cameraDistance` unsupported reason present, 956 JBR picture frames, and zero command frames;
  adjacent `CASE_GROUPS=graphics-layer-invalid` passed 3/3 with `fallback_sum=0`, three intentional
  unsupported-picture rows, 2,841 picture frames, and zero command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-184223/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-184326/suite.tsv`.
- Magic Jewel added a graphics-layer invalid alpha fallback sentinel so the layer validation guard now has app-level
  coverage for out-of-range `alpha`. No-run discovery reports 498 default command-probe rows, a new
  `graphics-layer-invalid` quick group with 2 rows, and no ungrouped rows. Focused
  `CASES=commands-graphics-layer-invalid-alpha-fallback` passed 1/1 with the `graphicsLayer:alpha` unsupported reason
  present, 964 JBR picture frames, and zero command frames; adjacent `CASE_GROUPS=graphics-layer-invalid` passed 2/2
  with `fallback_sum=0`, two intentional unsupported-picture rows, 1,983 picture frames, and zero command frames.
  Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-183358/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-183507/suite.tsv`.
- Magic Jewel added a graphics-layer invalid shadow-elevation fallback sentinel so the layer validation guard now has
  app-level coverage for negative `shadowElevation`. No-run discovery reports 497 default command-probe rows and
  `graphics-layer` 22, with no ungrouped rows. Focused
  `CASES=commands-graphics-layer-invalid-shadow-elevation-fallback` passed 1/1 with the
  `graphicsLayer:shadowElevation` unsupported reason present, 969 JBR picture frames, and zero command frames;
  adjacent `CASE_GROUPS=graphics-layer` passed 22/22 with `fallback_sum=0`, one intentional unsupported-picture row,
  879 picture frames, and 24,305 command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-175414/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-180307/suite.tsv`.
- Magic Jewel added gradient path stroke fallback sentinels for linear, radial, and sweep path gradients. The recorder
  rejects these real Compose stroked-path probes through the gradient-paint guards (`linearGradientPaint`,
  `radialGradientPaint`, and `sweepGradientPaint`) before the path-specific paint-style checks are reached. No-run
  discovery reports 496 default command-probe rows and a new `gradient-path-stroke-fallbacks` group with 3 rows, with
  no ungrouped rows. Focused `CASE_GROUPS=gradient-path-stroke-fallbacks` passed 3/3 with `fallback_sum=0`, three
  intentional unsupported-picture rows, 3,245 picture frames, and zero command frames. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-173734/suite.tsv`.
- Magic Jewel added an image raw Skia table color-filter fallback sentinel so `drawImageRect` now has explicit
  app-level coverage for unsupported raw image color filters, while descriptor-backed image tint/color-matrix rows stay
  on the command path. No-run discovery reports 493 default command-probe rows and `color-filters` 12, with no
  ungrouped rows. Focused `CASES=commands-image-raw-table-color-filter-fallback` passed 1/1 with the `colorFilter`
  unsupported reason present alongside the generic `image` and graphics-layer parent reasons, 931 JBR picture frames,
  and zero command frames; adjacent `CASE_GROUPS=color-filters` passed 12/12 with `fallback_sum=0`, three intentional
  unsupported-picture rows, 3,516 picture frames, and 12,920 command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-171252/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-171707/suite.tsv`.
- Magic Jewel added a `Canvas.drawVertices` raw Skia-backed color-filter fallback sentinel so the recorder's
  solid-color-only vertices path now has explicit structural fallback coverage. No-run discovery reports 492 default
  command-probe rows and `core-effects` 8, with no ungrouped rows. Focused
  `CASES=commands-vertices-raw-color-filter-fallback` passed 1/1 with the `vertices` unsupported reason present,
  927 JBR picture frames, and zero command frames; adjacent `CASE_GROUPS=core-effects` passed 8/8 with
  `fallback_sum=0`, three intentional unsupported-picture rows, 3,195 picture frames, and 6,876 command frames.
  Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-155224/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260603-160716/suite.tsv`.
- Magic Jewel refreshed the post-command-sweep compatibility/artifact/parity gates after restoring the local `/tmp`
  JBR Skia artifacts. Missing `/tmp/jbr-api-shim.jar` and `/tmp/jbr-skia-native/libjbrskiainterop.dylib` had caused
  focused `happy` compatibility launches to fall back with `public-api-missing`; rerunning
  `./scripts/rebuild-jbr-skia-local-artifacts.sh` restored the shim, java.desktop patch, and native dylib. The
  authoritative compatibility matrix then passed 57/57 with `fallback_sum=56`, 770 command frames, and all rows in
  background-window mode:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260603-140432/matrix.tsv`.
- Magic Jewel artifact and screenshot parity refreshes passed after the compatibility matrix: artifact required rows
  passed 2/2 with `fallback_sum=1` and 438 command frames, optional-old rows recorded five expected skips, and full
  screenshot parity marker-only passed 106/106 with `fallback_sum=11`, zero picture frames, and 93,672 command frames.
  Matrices/suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260603-143254/matrix.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260603-143404/matrix.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260603-143459/suite.tsv`.
- Magic Jewel full default command-probe consolidation passed after the focused invalid/supported refreshes, resumed
  from the interrupted prefix instead of replaying already-green rows. Combined suites
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-155529/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-165056/suite.tsv`
  cover 491/491 default rows with `fallback_sum=350`, 30 intentional unsupported-picture rows,
  31,269 picture frames, and 158,073 command frames. The resumed tail ended at
  `commands-invalid-gradient-fallback`, and no non-passed rows were present.
- Magic Jewel focused command-probe graphics-layer refreshes passed after the shader rendering/composition refresh:
  `graphics-layer` 21/21 with `fallback_sum=0`, no unsupported rows, zero picture frames, and 29,909 command frames;
  `graphics-layer-extras` 15/15 with `fallback_sum=0`, three intentional unsupported-picture rows, 3,322 picture
  frames, and 18,044 command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-153002/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-154344/suite.tsv`.
- Magic Jewel focused command-probe shader rendering/composition refreshes passed after the descriptor lifecycle
  refresh: `shader-rendering` 14/14 with `fallback_sum=0`, nine intentional unsupported-picture rows, 10,692 picture
  frames, and 6,032 command frames; `shader-composition-runtime` 15/15 with `fallback_sum=0`, two intentional
  unsupported-picture rows, 2,188 picture frames, and 16,730 command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-150751/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-151724/suite.tsv`.
- Magic Jewel focused command-probe descriptor lifecycle refresh passed after the color/saveLayer fallback refresh:
  `descriptor-lifecycle` 18/18 with `fallback_sum=0`, no unsupported rows, zero picture frames, and 25,506 command
  frames, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-145219/suite.tsv`.
- Magic Jewel focused command-probe color/saveLayer fallback refreshes passed after the transform/text refresh:
  `color-filters` 11/11 with `fallback_sum=0`, two intentional unsupported-picture rows, 2,098 picture frames, and
  11,700 command frames; `save-layer-shader-fallbacks` 8/8 with `fallback_sum=0`, six intentional
  unsupported-picture rows, 7,270 picture frames, and 2,039 command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-143804/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-144528/suite.tsv`.
- Magic Jewel focused command-probe transform/text supported refreshes passed after the supported sanity pair:
  `surface-transform-ui` 11/11 with `fallback_sum=0`, no unsupported rows, and 16,553 command frames; `native-text`
  14/14 with `fallback_sum=0`, no unsupported rows, and 20,315 command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-142012/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-142743/suite.tsv`.
- Magic Jewel focused command-probe supported-command sanity refreshes passed after the compact invalid batch:
  `core-effects` 7/7 with `fallback_sum=0`, two intentional unsupported-picture rows, 2,049 picture frames, and
  7,785 command frames; `smoke` 6/6 with `fallback_sum=0`, no unsupported rows, and 7,435 command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-140936/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-141436/suite.tsv`.
- Magic Jewel compact focused command-probe invalid refresh passed after the gradient invalid batch:
  `shader-ref-invalid`, `fill-rect-color-filter-invalid`, `blend-mode-invalid`, and `stream-invalid` together passed
  18/18 with `fallback_sum=18`, `unsupported_rows=0`, and zero replay frames, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-135655/suite.tsv`.
- Magic Jewel focused command-probe gradient guard refresh passed after the RuntimeEffect invalid batch:
  `gradient-invalid` 60/60 with `fallback_sum=60`, `unsupported_rows=0`, and zero replay frames, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-131836/suite.tsv`.
- Magic Jewel focused command-probe RuntimeEffect guard refresh passed after the shader descriptor invalid batch:
  `runtime-effect-invalid` 62/62 with `fallback_sum=56`, six intentional unsupported-picture rows, 7,356 picture
  frames, and zero command frames, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-123402/suite.tsv`.
- Magic Jewel focused command-probe shader descriptor guard refresh passed after the saveLayer invalid batch:
  `shader-descriptor-invalid` 30/30 with `fallback_sum=30`, `unsupported_rows=0`, and zero replay frames, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-121314/suite.tsv`.
- Magic Jewel focused command-probe saveLayer guard refresh passed after the descriptor-handle invalid batch:
  `save-layer-invalid` 37/37 with `fallback_sum=37`, `unsupported_rows=0`, and zero replay frames, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-114844/suite.tsv`.
- Magic Jewel focused command-probe descriptor handle guard refresh passed after the image-handle invalid batch:
  `descriptor-handles-invalid` 48/48 with `fallback_sum=48`, `unsupported_rows=0`, and zero replay frames, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-111739/suite.tsv`.
- Magic Jewel focused command-probe image handle guard refresh passed after the descriptor/path invalid batch:
  `image-handles-invalid` 27/27 with `fallback_sum=27`, `unsupported_rows=0`, zero picture frames, and 1,102 command
  frames, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-105942/suite.tsv`.
- Magic Jewel focused command-probe descriptor/path guard refreshes passed after the primitive/native/path invalid
  batch: `gradient-path-invalid` 18/18 with `fallback_sum=18` and `effect-descriptor-invalid` 28/28 with
  `fallback_sum=28`; both had `unsupported_rows=0` and zero replay frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-102903/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-104049/suite.tsv`.
- Magic Jewel focused command-probe invalid guard refreshes passed after the raw table full-sweep consolidation:
  `primitive-invalid` 13/13 with `fallback_sum=13`, `native-text-invalid` 11/11 with `fallback_sum=11` and 976
  command frames, and `path-invalid` 22/22 with `fallback_sum=22`; all three had `unsupported_rows=0` and zero
  picture frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-095812/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-100643/suite.tsv`,
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-101406/suite.tsv`.
- Magic Jewel full default command-probe consolidation passed in command-marker-only mode after the raw table
  color-filter sentinel batch: 491/491 passed, `fallback_sum=350`, `unsupported_rows=30`,
  `jbr_picture_frames=38973`, and `jbr_command_frames=166320`. The suite includes 492 TSV lines including the header,
  the run directory was 2.3G, Magic Jewel `out` was 72G, and the volume had about 247Gi free after completion. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-044455/suite.tsv`.
- Magic Jewel raw Skia table color-filter graphicsLayer fallback sentinel landed in pushed commit `7c1ffcc`. No-run
  discovery now reports 491 default command-probe rows and 15 `graphics-layer-extras` rows, with no ungrouped rows.
  Focused `CASES=commands-graphics-layer-raw-table-color-filter-fallback` passed 1/1 with one intentional
  unsupported-picture row, 1,334 JBR picture frames, and zero command frames; the adjacent
  `CASE_GROUPS=graphics-layer-extras` refresh passed 15/15 with three intentional unsupported-picture rows, 3,960 JBR
  picture frames, and 17,534 command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-043200/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-043305/suite.tsv`.
- Magic Jewel raw Skia table color-filter saveLayer fallback sentinel landed in pushed commit `02a250f`. No-run
  discovery now reports 490 default command-probe rows and 8 `save-layer-shader-fallbacks` rows, with no ungrouped
  rows. Focused `CASES=commands-save-layer-raw-table-color-filter-fallback` passed 1/1 with one intentional
  unsupported-picture row, 1,317 JBR picture frames, and zero command frames; the adjacent
  `CASE_GROUPS=save-layer-shader-fallbacks` refresh passed 8/8 with six intentional unsupported-picture rows, 8,377
  JBR picture frames, and 4,799 command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-042132/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-042236/suite.tsv`.
- Magic Jewel raw Skia table color-filter fallback sentinel landed in pushed commit `55df401`. No-run discovery now
  reports 489 default command-probe rows and 11 `color-filters` rows, with no ungrouped rows. Focused
  `CASES=commands-raw-table-color-filter-fallback` passed 1/1 with one intentional unsupported-picture row, 826 JBR
  picture frames, and zero command frames; the adjacent `CASE_GROUPS=color-filters` refresh passed 11/11 with two
  intentional unsupported-picture rows, 2,518 JBR picture frames, and 14,110 command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-040651/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260602-040750/suite.tsv`.
- Magic Jewel full default command-probe consolidation passed in command-marker-only mode after the raw conical
  gradient sentinel: 488/488 passed, `fallback_sum=350`, `unsupported_rows=27`, `jbr_picture_frames=34246`, and
  `jbr_command_frames=188703`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-224702/suite.tsv`.
- Magic Jewel raw Skia two-point conical gradient shader fallback sentinel landed in pushed commit `020dcb3`.
  No-run discovery now reports 488 default command-probe rows and 14 `shader-rendering` rows, with no ungrouped rows.
  Focused `CASES=commands-raw-conical-gradient-shader-fallback` passed 1/1 with one intentional
  unsupported-picture row, 1,496 JBR picture frames, and zero command frames; the adjacent
  `CASE_GROUPS=shader-rendering` refresh passed 14/14 with nine intentional unsupported-picture rows, 11,653 JBR
  picture frames, and 7,884 command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-223319/suite.tsv`
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-223423/suite.tsv`.
- Magic Jewel full default screenshot parity suite passed in marker-only mode after the 20260601 command,
  compatibility, artifact, and Skiko refreshes: 106/106 passed, `fallback_sum=11`, `jbr_picture_frames=0`, and
  `jbr_command_frames=120808`; all screenshot pixel metrics were intentionally `missing` because
  `EXPECT_SCREENSHOT_ASSERTION=false` was set. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-202121/suite.tsv`.
- Skiko focused publication and full focused `JbrSkiaInteropTest` class passed after the 20260601 command,
  compatibility, and artifact matrix refreshes:
  `./gradlew publishAwtPublicationToMavenLocal publishAwtRuntimeElementsPublicationToMavenLocal publishKotlinMultiplatformPublicationToMavenLocal`
  and `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Magic Jewel artifact matrix passed on current ABI 106 local artifacts after the 20260601 compatibility refresh:
  required rows 2/2 passed, optional old-artifact rows skipped because no old bundle variables were set,
  `fallback_sum=1`, `jbr_command_frames=1300`, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260601-201802/matrix.tsv`.
- Magic Jewel compatibility matrix passed in command-marker-only mode after the 20260601 full command-probe
  consolidation: 57/57 passed, `fallback_sum=56`, `jbr_command_frames=285`, background-window mode true for all rows,
  matrix `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260601-194910/matrix.tsv`.
- Magic Jewel full default command-probe consolidation passed in command-marker-only mode after the focused
  invalid/parser, shader, graphics-layer, and smoke refreshes: 487/487 passed, `fallback_sum=350`,
  `unsupported_rows=26`, `jbr_picture_frames=25751`, and `jbr_command_frames=132652`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-140440/suite.tsv`.
- Magic Jewel command-probe shader invalid descriptor gates were tightened with exact max descriptor-setup counts:
  exact touched rows passed 13/13 with `fallback_sum=13`, then `CASE_GROUPS=shader-descriptor-invalid` passed 30/30
  with `fallback_sum=30`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-015339/suite.tsv` and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260601-020219/suite.tsv`.
- Magic Jewel full default screenshot parity suite passed in marker-only mode after the descriptor-guard harness
  change and focused group refreshes: 106/106 passed, `fallback_sum=11`, `jbr_picture_frames=0`, and
  `jbr_command_frames=69504`; all screenshot pixel metrics were intentionally `missing` because
  `EXPECT_SCREENSHOT_ASSERTION=false` was set. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-005125/suite.tsv`.
- Magic Jewel focused screenshot parity group refreshes passed after the descriptor-guard harness change:
  `descriptor-lifecycle` 6/6 with `fallback_sum=1`, `runtime-effect` 14/14 with `fallback_sum=2`, and
  `graphics-layer-effects` 14/14 with `fallback_sum=2`; the adjacent `shader-rendering` group passed 18/18 with
  `fallback_sum=4`; all had `jbr_picture_frames=0`. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-001912/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-002358/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-003141/suite.tsv`,
  and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260601-004029/suite.tsv`.
- Magic Jewel focused screenshot parity descriptor-guard refresh passed after adding bounded JBR shader/effect handle
  definition ceilings to descriptor-backed parity rows: 27/27 passed, `fallback_sum=9`, `jbr_picture_frames=0`, and
  `jbr_command_frames=17040`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260531-235827/suite.tsv`.
- Magic Jewel full default screenshot parity suite passed in marker-only mode after the command, compatibility,
  artifact, and Skiko refreshes: 106/106 passed, `fallback_sum=11`, `jbr_picture_frames=0`, and
  `jbr_command_frames=79348`; all screenshot pixel metrics were intentionally `missing` because
  `EXPECT_SCREENSHOT_ASSERTION=false` was set. Suite:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260531-221616/suite.tsv`.
- Magic Jewel full default command-probe consolidation passed in command-marker-only mode after the latest focused
  invalid/parser guard refreshes: 487/487 passed, `fallback_sum=350`, `unsupported_rows=26`,
  `jbr_picture_frames=25686`, and `jbr_command_frames=122355`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-162857/suite.tsv`.
- Magic Jewel compatibility matrix passed in command-marker-only mode after the full command-probe consolidation:
  57/57 passed, `fallback_sum=56`, `jbr_command_frames=454`, background-window mode true for all rows, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260531-214046/matrix.tsv`.
- Magic Jewel artifact matrix passed on current ABI 106 local artifacts after the compatibility refresh: required rows
  2/2 passed, optional old-artifact rows skipped because no old bundle variables were set, `fallback_sum=1`,
  `jbr_command_frames=445`, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260531-221009/matrix.tsv`.
- Skiko focused publication and full focused `JbrSkiaInteropTest` class passed after the 20260531 command,
  compatibility, and artifact matrix refreshes:
  `./gradlew publishAwtPublicationToMavenLocal publishAwtRuntimeElementsPublicationToMavenLocal publishKotlinMultiplatformPublicationToMavenLocal`
  and `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Magic Jewel focused command-probe `CASE_GROUPS=save-layer-shader-fallbacks` passed after the marker-only parity
  consolidation: 7/7 passed, `fallback_sum=0`, five intentional unsupported-picture rows,
  `jbr_picture_frames=6491`, and `jbr_command_frames=4020`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-143004/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=primitive-invalid` refreshed after the marker-only parity
  consolidation: 13/13 passed, `fallback_sum=13`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-143801/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=native-text-invalid` refreshed after the marker-only parity
  consolidation: 11/11 passed, `fallback_sum=11`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=1857`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-144639/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=path-invalid` refreshed after the marker-only parity consolidation:
  22/22 passed, `fallback_sum=22`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-145405/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=color-filters` refreshed after the marker-only parity consolidation:
  10/10 passed, `fallback_sum=0`, one intentional unsupported-picture row, `jbr_picture_frames=1451`, and
  `jbr_command_frames=19801`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-150741/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-rendering` refreshed after the marker-only parity
  consolidation: 13/13 passed, `fallback_sum=0`, eight intentional unsupported-picture rows,
  `jbr_picture_frames=11161`, and `jbr_command_frames=9643`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-151449/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-composition-runtime` refreshed after the marker-only parity
  consolidation: 15/15 passed, `fallback_sum=0`, two intentional unsupported-picture rows,
  `jbr_picture_frames=3016`, and `jbr_command_frames=28748`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-152335/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=core-effects` refreshed after the marker-only parity consolidation:
  7/7 passed, `fallback_sum=0`, two intentional unsupported-picture rows, `jbr_picture_frames=3122`, and
  `jbr_command_frames=10716`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-153337/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=surface-transform-ui` refreshed after the marker-only parity
  consolidation: 11/11 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=23868`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-153856/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=graphics-layer-extras` refreshed after the marker-only parity
  consolidation: 14/14 passed, `fallback_sum=0`, two intentional unsupported-picture rows,
  `jbr_picture_frames=2700`, and `jbr_command_frames=24591`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-154633/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=graphics-layer` refreshed after the marker-only parity consolidation:
  21/21 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=45890`,
  suite `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-155613/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=descriptor-lifecycle` refreshed after the marker-only parity
  consolidation: 18/18 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=38694`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-160948/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=native-text` refreshed after the marker-only parity consolidation:
  14/14 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=27143`,
  suite `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-162416/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=image-handles-invalid` refreshed after the marker-only parity
  consolidation: 27/27 passed, `fallback_sum=27`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=1153`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-163344/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=save-layer-invalid` refreshed after the marker-only parity
  consolidation: 37/37 passed, `fallback_sum=37`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-165035/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=effect-descriptor-invalid` refreshed after the marker-only parity
  consolidation: 28/28 passed, `fallback_sum=28`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-171350/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-descriptor-invalid` refreshed after the marker-only parity
  consolidation: 30/30 passed, `fallback_sum=30`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-173119/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=gradient-path-invalid` refreshed after the marker-only parity
  consolidation: 18/18 passed, `fallback_sum=18`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-175010/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=descriptor-handles-invalid` refreshed after the marker-only parity
  consolidation: 48/48 passed, `fallback_sum=48`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-180200/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=stream-invalid` refreshed after the marker-only parity consolidation:
  8/8 passed, `fallback_sum=8`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-183130/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=smoke` refreshed after the marker-only parity consolidation:
  6/6 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and `jbr_command_frames=12275`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-183718/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=gradient-invalid` refreshed after the marker-only parity
  consolidation: 60/60 passed, `fallback_sum=60`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-145017/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=runtime-effect-invalid` refreshed after the marker-only parity
  consolidation: 62/62 passed, `fallback_sum=56`, six intentional unsupported-picture rows,
  `jbr_picture_frames=5898`, and `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-152938/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=fill-rect-color-filter-invalid` refreshed after the marker-only parity
  consolidation: 5/5 passed, `fallback_sum=5`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-161535/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-ref-invalid` refreshed after the marker-only parity
  consolidation: 3/3 passed, `fallback_sum=3`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-162224/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=blend-mode-invalid` refreshed after the marker-only parity
  consolidation: 2/2 passed, `fallback_sum=2`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260531-162604/suite.tsv`.
- Magic Jewel exact command-probe `CASES=commands-descriptor-eviction` passed after the CMP recorder eviction guard:
  1/1 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=59`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-013713/suite.tsv`.
- Magic Jewel focused color-shader descriptor command rows passed after the CMP shader descriptor cache gate: 3/3
  passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=5423`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-014358/suite.tsv`.
- Magic Jewel focused graphics-layer render-effect descriptor rows passed after the CMP image-filter descriptor cache
  gate: 3/3 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=4465`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-015048/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=primitive-invalid` passed: 13/13 passed, `fallback_sum=13`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-200527/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=path-invalid` passed: 22/22 passed, `fallback_sum=22`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-030433/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=effect-descriptor-invalid` passed: 28/28 passed,
  `fallback_sum=28`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-031858/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-descriptor-invalid` passed: 30/30 passed,
  `fallback_sum=30`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-033644/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=image-handles-invalid` passed: 27/27 passed, `fallback_sum=27`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=1036`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-035452/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=color-filters` passed: 10/10 passed, `fallback_sum=0`,
  `unsupported_rows=1`, `jbr_picture_frames=1047`, `jbr_command_frames=13204`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203750/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=save-layer-invalid` passed: 37/37 passed, `fallback_sum=37`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-052922/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=descriptor-handles-invalid` passed: 48/48 passed,
  `fallback_sum=48`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-041151/suite.tsv`.
- Magic Jewel compatibility matrix passed in command-marker-only mode after the resumed full command-probe
  consolidation: 57/57 passed, `fallback_sum=56`, `jbr_command_frames=1050`, background-window mode true for all
  rows, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260530-113437/matrix.tsv`.
- Magic Jewel artifact matrix passed on current ABI 106 local artifacts after the compatibility refresh: required rows
  2/2 passed, optional old-artifact rows skipped because no old bundle variables were set, `fallback_sum=1`,
  `jbr_command_frames=1081`, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260530-120135/matrix.tsv`.
- The focused artifact rows also passed after fixing Magic Jewel's JBR API helper to remove its temporary
  `com.jetbrains.exported.JBRApi` desktop-overlay stub on exit: `current-all` replayed commands and
  `missing-public-api` fell back once as expected, matrix
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260528-173847/matrix.tsv`.
- Magic Jewel full default screenshot parity passed after the command-probe, compatibility, and artifact refreshes:
  106/106 passed, `fallback_sum=11`, `jbr_picture_frames=0`, `jbr_command_frames=111257`, average
  `bad_pixel_ratio=0.05158`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260529-091416/suite.tsv`.
- Magic Jewel screenshot parity now honors `EXPECT_SCREENSHOT_ASSERTION=false` by skipping the image diff wrapper after
  the interop report succeeds. A focused marker-only `CASE_GROUPS=smoke` refresh passed 3/3 with `fallback_sum=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=5603`, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-122221/suite.tsv`.
- The next focused marker-only screenshot parity group, `CASE_GROUPS=descriptor-lifecycle`, passed 6/6 with
  `fallback_sum=1`, `jbr_picture_frames=0`, `jbr_command_frames=10185`, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-122542/suite.tsv`.
- Focused marker-only screenshot parity for `CASE_GROUPS=graphics-layer-basic` passed 7/7 with `fallback_sum=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=16730`, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-123134/suite.tsv`.
- Focused marker-only screenshot parity for `CASE_GROUPS=core-drawing` passed 16/16 with `fallback_sum=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=25745`, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-123636/suite.tsv`.
- Focused marker-only screenshot parity for `CASE_GROUPS=native-text` passed 14/14 with `fallback_sum=4`,
  `jbr_picture_frames=0`, `jbr_command_frames=18663`, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-124546/suite.tsv`.
- Focused marker-only screenshot parity for `CASE_GROUPS=runtime-effect` passed 14/14 with `fallback_sum=2`,
  `jbr_picture_frames=0`, `jbr_command_frames=17990`, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-125424/suite.tsv`.
- Focused marker-only screenshot parity for `CASE_GROUPS=shader-rendering` passed 18/18 with `fallback_sum=4`,
  `jbr_picture_frames=0`, `jbr_command_frames=25540`, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-130246/suite.tsv`.
- Focused marker-only screenshot parity for `CASE_GROUPS=graphics-layer-effects` passed 14/14 with `fallback_sum=2`,
  `jbr_picture_frames=0`, `jbr_command_frames=29594`, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-131305/suite.tsv`.
- Focused marker-only screenshot parity for `CASE_GROUPS=graphics-layer-clip-shadow-transform` passed 14/14 with
  `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=34196`, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-132134/suite.tsv`.
- Combined across the nine focused marker-only screenshot parity groups after the wrapper fix, all 106 parity rows
  passed with `fallback_sum=13`, `jbr_picture_frames=0`, `jbr_command_frames=184246`, and intentionally missing pixel
  metrics because local screenshot comparison was skipped.
- A periodic full default marker-only screenshot parity consolidation then passed as a single TSV: 106/106 passed,
  `fallback_sum=12`, `jbr_picture_frames=0`, `jbr_command_frames=186106`, and intentionally missing pixel metrics:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260530-133012/suite.tsv`.
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
- Magic Jewel focused command-probe `CASE_GROUPS=native-text` passed in command-marker-only mode after a screenshot
  assertion failure independent of command replay: 14/14 passed, `fallback_sum=0`, `unsupported_rows=0`,
  `jbr_picture_frames=0`, `jbr_command_frames=16132`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-210151/suite.tsv`.
- Magic Jewel focused visual `CASE_GROUPS=native-text` passed through the new screenshot group path: 14/14 passed,
  `fallback_sum=2`, `jbr_picture_frames=0`, `jbr_command_frames=9822`, average `bad_pixel_ratio=0.04758`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-110758/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=core-effects` passed as the paired command refresh for core visual
  drawing/effect coverage: 7/7 passed, `fallback_sum=0`, `unsupported_rows=2`, `jbr_picture_frames=2724`,
  `jbr_command_frames=7649`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-215950/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=save-layer-shader-fallbacks` passed in command-marker-only mode after a
  screenshot assertion failure independent of command replay: 7/7 passed, `fallback_sum=0`, `unsupported_rows=5`,
  `jbr_picture_frames=6640`, `jbr_command_frames=2760`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-221530/suite.tsv`.
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
- Magic Jewel full default benchmark suite passed after the compatibility/artifact refreshes in command-marker-only
  mode: 5/5 passed, `fallback_sum=0`, 84 old-side CPU samples, 49 new-side CPU samples,
  `jbr_command_frames=13695`, picture FPS `229.4`, and command FPS row total `684.8`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-benchmark-suite/20260530-120858/suite.tsv`.
- Skiko full focused `JbrSkiaInteropTest` class passed after the refreshed 20260530 command, compatibility,
  artifact, and benchmark validations:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- JBR parser-only `JBRSkiaApiTest` passed via Magic Jewel's helper against the current local overlay artifacts and
  `/tmp/jbr-skia-native/libjbrskiainterop.dylib` after the refreshed 20260530 Skiko gate, using
  `REBUILD_LOCAL_ARTIFACTS=false`.
- Magic Jewel now has `scripts/test-jbr-skia-api.sh` to rebuild the local overlay, patch the temporary JBR API stub,
  compile `JBRSkiaApiTest`, run it headlessly, and remove the temporary desktop-overlay stub on exit; the helper
  passed end-to-end.
- CMP full focused `JbrSkiaCommandRecorderTest` class passed after the refreshed 20260530 source gates: 138/138
  desktop tests, zero skipped/failures/errors.
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
  `jbr_command_frames=27255`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-211340/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=graphics-layer-clip-shadow-transform` passed as the paired visual
  checkpoint: 14/14 passed, `fallback_sum=0`, `jbr_picture_frames=0`, `jbr_command_frames=14847`, average
  `bad_pixel_ratio=0.05296`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-103209/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=descriptor-lifecycle` passed as the current descriptor lifecycle
  command checkpoint: 18/18 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=23175`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-204612/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=descriptor-lifecycle` passed as the paired descriptor lifecycle
  visual checkpoint: 6/6 passed, `fallback_sum=1`, `jbr_picture_frames=0`, `jbr_command_frames=7237`, average
  `bad_pixel_ratio=0.05078`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-130309/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-composition-runtime` passed as the current shader composition
  and RuntimeEffect command checkpoint: 15/15 passed, `fallback_sum=0`, `unsupported_rows=2`,
  `jbr_picture_frames=2386`, `jbr_command_frames=16985`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-214834/suite.tsv`.
- Magic Jewel focused screenshot parity `CASE_GROUPS=runtime-effect` passed as the paired RuntimeEffect visual
  checkpoint: 14/14 passed, `fallback_sum=2`, `jbr_picture_frames=0`, `jbr_command_frames=12380`, average
  `bad_pixel_ratio=0.04879`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260527-123909/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=native-text-invalid` passed as the current native text/font-data
  parser guard checkpoint: 11/11 passed, `fallback_sum=11`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=1519`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-055310/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=primitive-invalid` passed as the current primitive parser guard
  checkpoint: 13/13 passed, `fallback_sum=13`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-200527/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=path-invalid` passed as the current path/path-effect parser guard
  checkpoint: 22/22 passed, `fallback_sum=22`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-030433/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=effect-descriptor-invalid` passed as the current effect descriptor
  parser guard checkpoint: 28/28 passed, `fallback_sum=28`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-031858/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-descriptor-invalid` passed as the current shader descriptor
  parser guard checkpoint: 30/30 passed, `fallback_sum=30`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-033644/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=image-handles-invalid` passed as the current image handle/cache parser
  guard checkpoint: 27/27 passed, `fallback_sum=27`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=1036`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-035452/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=descriptor-handles-invalid` passed as the current descriptor handle
  lifetime/type guard checkpoint: 48/48 passed, `fallback_sum=48`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-041151/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=gradient-path-invalid` passed as the current gradient path parser
  guard checkpoint: 18/18 passed, `fallback_sum=18`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-044104/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=save-layer-invalid` passed as the current saveLayer parser guard
  checkpoint: 37/37 passed, `fallback_sum=37`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-052922/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=gradient-invalid` passed as the current gradient parser guard
  checkpoint: 60/60 passed, `fallback_sum=60`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-045310/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=runtime-effect-invalid` passed as the current RuntimeEffect
  parser/schema guard checkpoint: 62/62 passed, `fallback_sum=56`, `unsupported_rows=6`,
  `jbr_picture_frames=7806`, `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260530-060107/suite.tsv`.
- Magic Jewel command-probe quick-group coverage now spans every resolved default case: `LIST_UNGROUPED_CASES=true`
  returns no rows after adding supported surface/transform/UI, shader-rendering, shader-composition/RuntimeEffect,
  core-effects, graphics-layer-extras, and saveLayer/shader-fallback groups.
- Magic Jewel exact saveLayer/shader-fallback uncovered command-probe tail passed: 7/7 passed, `fallback_sum=0`,
  `unsupported_rows=5`, `jbr_picture_frames=4757`, `jbr_command_frames=1765`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-114732/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=graphics-layer-extras` passed: 14/14 passed, `fallback_sum=0`,
  `unsupported_rows=2`, `jbr_picture_frames=2458`, `jbr_command_frames=18868`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-220535/suite.tsv`.
- Magic Jewel exact core effects uncovered command-probe slice passed: 7/7 passed, `fallback_sum=0`,
  `unsupported_rows=2`, `jbr_picture_frames=2724`, `jbr_command_frames=7649`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-215950/suite.tsv`.
- Magic Jewel exact shader composition and RuntimeEffect uncovered command-probe slice passed: 15/15 passed,
  `fallback_sum=0`, `unsupported_rows=2`, `jbr_picture_frames=2386`, `jbr_command_frames=16985`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-214834/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-rendering` passed: 13/13 passed, `fallback_sum=0`,
  `unsupported_rows=8`, `jbr_picture_frames=9129`, `jbr_command_frames=5472`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-213937/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=surface-transform-ui` passed in command-marker-only mode after a
  macOS screenshot-capture failure independent of command replay: 11/11 passed, `fallback_sum=0`,
  `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=13359`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-213157/suite.tsv`.
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
  remains broken independently of replay: `smoke` 6/6 with `jbr_command_frames=8392`, `color-filters` 10/10 with
  `unsupported_rows=1`, `jbr_picture_frames=1047`, and `jbr_command_frames=13204`, `descriptor-lifecycle` 18/18 with
  `jbr_command_frames=23175`, `native-text` 14/14 with `jbr_command_frames=16132`, and `graphics-layer` 21/21 with
  `jbr_command_frames=27255`. All supported rows in these runs had `fallback_sum=0`; the single unsupported row is the
  intentional raw blend color-filter sentinel. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203230/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203750/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-204612/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-210151/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-211340/suite.tsv`.
- Magic Jewel small invalid-group refresh passed: `stream-invalid` 8/8, `shader-ref-invalid` 3/3,
  `fill-rect-color-filter-invalid` 5/5, and `blend-mode-invalid` 2/2. All four runs had expected fallback sums, zero
  unsupported rows, zero JBR picture frames, and zero JBR command frames. Suites:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-201711/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202316/suite.tsv`,
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202527/suite.tsv`, and
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202909/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=stream-invalid` passed as the current command-stream parser guard
  checkpoint: 8/8 passed, `fallback_sum=8`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=0`,
  suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-201711/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=blend-mode-invalid` passed as the current fill-rect blend-mode
  parser guard checkpoint: 2/2 passed, `fallback_sum=2`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202909/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=fill-rect-color-filter-invalid` passed as the current fill-rect
  color-filter parser guard checkpoint: 5/5 passed, `fallback_sum=5`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202527/suite.tsv`.
- Magic Jewel focused command-probe `CASE_GROUPS=shader-ref-invalid` passed as the current fill-rect shader-ref parser
  guard checkpoint: 3/3 passed, `fallback_sum=3`, `unsupported_rows=0`, `jbr_picture_frames=0`,
  `jbr_command_frames=0`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-202316/suite.tsv`.
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
  `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=16132`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-210151/suite.tsv`.
- Magic Jewel exact native-text slices passed before consolidation: base text 5/5 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-204934/suite.tsv`
  with `jbr_command_frames=5082`, resize 4/4 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-205300/suite.tsv`
  with `jbr_command_frames=4294`, and forced-context 5/5 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-205555/suite.tsv`
  with `jbr_command_frames=6494`; all had `fallback_sum=0`, `unsupported_rows=0`, and
  `jbr_picture_frames=0`.
- Magic Jewel `CASE_GROUPS=graphics-layer` passed as a supported graphics-layer command replay refresh: 21/21
  passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=27255`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-211340/suite.tsv`.
- Magic Jewel exact graphics-layer slices passed before consolidation: base/clip/blend 7/7 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-201824/suite.tsv`
  with `jbr_command_frames=9525`, filters/effects 5/5 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-202325/suite.tsv`
  with `jbr_command_frames=6297`, and shadows/transforms 9/9 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-202706/suite.tsv`
  with `jbr_command_frames=10778`; all had `fallback_sum=0`, `unsupported_rows=0`, and
  `jbr_picture_frames=0`.
- Magic Jewel `CASE_GROUPS=descriptor-lifecycle` passed as a supported descriptor lifecycle/source-cache refresh:
  18/18 passed, `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, `jbr_command_frames=23175`,
  suite `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-204612/suite.tsv`.
- Magic Jewel exact descriptor lifecycle slices passed before consolidation: descriptor eviction/redefine 13/13 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-194922/suite.tsv`
  with `jbr_command_frames=18104`, and RuntimeEffect lifecycle/source-cache 5/5 at
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260524-195934/suite.tsv`
  with `jbr_command_frames=7343`; both had `fallback_sum=0`, `unsupported_rows=0`, and
  `jbr_picture_frames=0`.
- Magic Jewel `CASE_GROUPS=color-filters` passed as a supported color-filter command replay refresh: 10/10 passed,
  `fallback_sum=0`, `unsupported_rows=1`, `jbr_picture_frames=1047`, `jbr_command_frames=13204`, suite
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203750/suite.tsv`.
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
  `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=27255`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-211340/suite.tsv`.
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
  `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=27255`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-211340/suite.tsv`.
  This covers layer clips, blend/color filters, render effects, shadows, 3D rotations, scale/translate, camera, and
  pivot variants.
- Magic Jewel `CASE_GROUPS=gradient-path-invalid` passed as the focused gradient path parser guard checkpoint: 18/18,
  `fallback_sum=18`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=0`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260522-102856/suite.tsv`.
- Magic Jewel `CASE_GROUPS=native-text` passed as the focused native text/font-data command replay checkpoint: 14/14,
  `fallback_sum=0`, `unsupported_rows=0`, `picture_frames=0`, and `command_frames=16132`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-210151/suite.tsv`.
  This covers custom, generic, loaded-font-data, resource, system, resize, and forced-context native text rows.
- Magic Jewel `CASE_GROUPS=color-filters` passed as the focused color-filter command replay checkpoint: 10/10,
  `fallback_sum=0`, `unsupported_rows=1`, `picture_frames=1047`, and `command_frames=13204`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203750/suite.tsv`.
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
  `unsupported_rows=0`, `picture_frames=0`, and `command_frames=8392`:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260529-203230/suite.tsv`.
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
