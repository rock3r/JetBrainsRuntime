# Command Probe Current Default Surface

This ledger tracks the current Magic Jewel default command-probe row surface that the final zero-unsupported proof must
cover. It is separate from `UNSUPPORTED_COMMAND_CENSUS.md`, which tracks rows that have actually reported unsupported
markers.

## 2026-06-19 List-Only Audit

- Current default row count: `696`
  - Command: `LIST_CASE_COUNT=true ./scripts/jbr-skia-command-probe-suite.sh`
- Latest completed full/default unsupported census: `549` rows
  - Evidence: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-144955/suite.tsv`
- Delta from latest completed full/default census:
  - Added to current default surface: `151` rows
  - Removed from current default surface: `4` rows
- Daily broad guard check:
  - A default launch on 2026-06-19 exited before validation with code `3` because
    `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/.jbr-skia-daily-validation/broad.2026-06-19.stamp`
    already consumed the daily broad slot.

Interpretation: focused evidence currently shows zero known active unsupported rows, but final completion requires the
next capped full/default sweep to cover the current `696`-row surface and report `unsupported_rows=0`.

## Focused Current-Surface Addition Validation

- 2026-06-19 forced-context color/shader batch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260619-192030/suite.tsv`
  passed `10/10` with `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=122863`. Rows covered:
  `commands-forced-context-blend-mode`, `commands-forced-context-color-filter`,
  `commands-forced-context-color-filter-blend-mode`, `commands-forced-context-color-filter-handle`,
  `commands-forced-context-color-matrix-filter`, `commands-forced-context-color-shader`,
  `commands-forced-context-color-shader-blend-mode`, `commands-forced-context-composite-noise-shader`,
  `commands-forced-context-composite-shader`, and `commands-forced-context-composite-shader-color-filter`.
- 2026-06-19 forced-context transform/gradient/graphics batch:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260619-193859/suite.tsv`
  passed `10/10` with `fallback_sum=0`, `unsupported_rows=0`, `jbr_picture_frames=0`, and
  `jbr_command_frames=126723`. Rows covered:
  `commands-forced-context-composite-shader-descriptor-redefine`,
  `commands-forced-context-concat-transform`, `commands-forced-context-gradient-shaders`,
  `commands-forced-context-gradient-stroke`, `commands-forced-context-gradient-surfaces`,
  `commands-forced-context-graphics-layer`, `commands-forced-context-graphics-layer-blend-color-filter`,
  `commands-forced-context-graphics-layer-blend-color-matrix-filter`,
  `commands-forced-context-graphics-layer-blend-mode`, and
  `commands-forced-context-graphics-layer-chained-render-effect`.

## Added Rows Since Latest Completed Full Sweep

These rows are part of the current default surface but were not present in the latest completed `549`-row full/default
census:

- `commands-forced-context-blend-mode`
- `commands-forced-context-color-filter`
- `commands-forced-context-color-filter-blend-mode`
- `commands-forced-context-color-filter-handle`
- `commands-forced-context-color-matrix-filter`
- `commands-forced-context-color-shader`
- `commands-forced-context-color-shader-blend-mode`
- `commands-forced-context-composite-noise-shader`
- `commands-forced-context-composite-shader`
- `commands-forced-context-composite-shader-color-filter`
- `commands-forced-context-composite-shader-descriptor-redefine`
- `commands-forced-context-concat-transform`
- `commands-forced-context-gradient-shaders`
- `commands-forced-context-gradient-stroke`
- `commands-forced-context-gradient-surfaces`
- `commands-forced-context-graphics-layer`
- `commands-forced-context-graphics-layer-blend-color-filter`
- `commands-forced-context-graphics-layer-blend-color-matrix-filter`
- `commands-forced-context-graphics-layer-blend-mode`
- `commands-forced-context-graphics-layer-chained-render-effect`
- `commands-forced-context-graphics-layer-chained-render-effect-blend-color-matrix-filter`
- `commands-forced-context-graphics-layer-clip`
- `commands-forced-context-graphics-layer-color-filter`
- `commands-forced-context-graphics-layer-modulate-alpha`
- `commands-forced-context-graphics-layer-near-camera`
- `commands-forced-context-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter`
- `commands-forced-context-graphics-layer-offcenter-pivot`
- `commands-forced-context-graphics-layer-offscreen`
- `commands-forced-context-graphics-layer-offset-effect`
- `commands-forced-context-graphics-layer-offset-effect-blend-color-matrix-filter`
- `commands-forced-context-graphics-layer-path-clip`
- `commands-forced-context-graphics-layer-path-shadow`
- `commands-forced-context-graphics-layer-render-effect-blend-color-filter`
- `commands-forced-context-graphics-layer-render-effect-blend-color-matrix-filter`
- `commands-forced-context-graphics-layer-render-effect-blend-mode`
- `commands-forced-context-graphics-layer-render-effect-color-filter`
- `commands-forced-context-graphics-layer-render-effect-color-matrix-filter`
- `commands-forced-context-graphics-layer-rotationx`
- `commands-forced-context-graphics-layer-rotationxy`
- `commands-forced-context-graphics-layer-rotationy`
- `commands-forced-context-graphics-layer-round-clip`
- `commands-forced-context-graphics-layer-round-shadow`
- `commands-forced-context-graphics-layer-scale-translate`
- `commands-forced-context-graphics-layer-shadow`
- `commands-forced-context-image-blend-mode`
- `commands-forced-context-image-color-matrix-filter`
- `commands-forced-context-image-filter`
- `commands-forced-context-image-shader`
- `commands-forced-context-image-shader-blend-mode`
- `commands-forced-context-image-shader-color-filter`
- `commands-forced-context-lighting-filter`
- `commands-forced-context-linear-gradient-path-blend-mode`
- `commands-forced-context-linear-gradient-shader-color-filter`
- `commands-forced-context-noise-shader`
- `commands-forced-context-path-effect`
- `commands-forced-context-point-dots`
- `commands-forced-context-point-lines`
- `commands-forced-context-radial-gradient-shader-color-filter`
- `commands-forced-context-runtime-effect-child-only`
- `commands-forced-context-runtime-effect-color-filter`
- `commands-forced-context-runtime-effect-color-filter-child`
- `commands-forced-context-runtime-effect-pure-color`
- `commands-forced-context-runtime-effect-shader`
- `commands-forced-context-runtime-effect-shader-color-filter`
- `commands-forced-context-runtime-effect-uniform-only`
- `commands-forced-context-save-layer-blend-color-filter`
- `commands-forced-context-save-layer-blend-mode`
- `commands-forced-context-save-layer-color-matrix-filter`
- `commands-forced-context-save-layer-filter`
- `commands-forced-context-skew-transform`
- `commands-forced-context-sweep-gradient-shader-color-filter`
- `commands-forced-context-transformed-shader`
- `commands-forced-context-turbulence-shader`
- `commands-forced-context-vertices`
- `commands-radial-gradient-shader-color-filter`
- `commands-resize-blend-mode`
- `commands-resize-color-filter`
- `commands-resize-color-filter-blend-mode`
- `commands-resize-color-filter-handle`
- `commands-resize-color-matrix-filter`
- `commands-resize-color-shader`
- `commands-resize-color-shader-blend-mode`
- `commands-resize-composite-noise-shader`
- `commands-resize-composite-shader`
- `commands-resize-composite-shader-color-filter`
- `commands-resize-composite-shader-descriptor-redefine`
- `commands-resize-concat-transform`
- `commands-resize-gradient-shaders`
- `commands-resize-gradient-stroke`
- `commands-resize-gradient-surfaces`
- `commands-resize-graphics-layer`
- `commands-resize-graphics-layer-blend-color-filter`
- `commands-resize-graphics-layer-blend-color-matrix-filter`
- `commands-resize-graphics-layer-blend-mode`
- `commands-resize-graphics-layer-chained-render-effect`
- `commands-resize-graphics-layer-chained-render-effect-blend-color-matrix-filter`
- `commands-resize-graphics-layer-clip`
- `commands-resize-graphics-layer-color-filter`
- `commands-resize-graphics-layer-modulate-alpha`
- `commands-resize-graphics-layer-near-camera`
- `commands-resize-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter`
- `commands-resize-graphics-layer-offcenter-pivot`
- `commands-resize-graphics-layer-offscreen`
- `commands-resize-graphics-layer-offset-effect`
- `commands-resize-graphics-layer-offset-effect-blend-color-matrix-filter`
- `commands-resize-graphics-layer-path-clip`
- `commands-resize-graphics-layer-path-shadow`
- `commands-resize-graphics-layer-render-effect-blend-color-filter`
- `commands-resize-graphics-layer-render-effect-blend-color-matrix-filter`
- `commands-resize-graphics-layer-render-effect-blend-mode`
- `commands-resize-graphics-layer-render-effect-color-filter`
- `commands-resize-graphics-layer-render-effect-color-matrix-filter`
- `commands-resize-graphics-layer-rotationx`
- `commands-resize-graphics-layer-rotationxy`
- `commands-resize-graphics-layer-rotationy`
- `commands-resize-graphics-layer-round-clip`
- `commands-resize-graphics-layer-round-shadow`
- `commands-resize-graphics-layer-scale-translate`
- `commands-resize-graphics-layer-shadow`
- `commands-resize-image-blend-mode`
- `commands-resize-image-color-matrix-filter`
- `commands-resize-image-filter`
- `commands-resize-image-shader`
- `commands-resize-image-shader-blend-mode`
- `commands-resize-image-shader-color-filter`
- `commands-resize-lighting-filter`
- `commands-resize-linear-gradient-path-blend-mode`
- `commands-resize-linear-gradient-shader-color-filter`
- `commands-resize-noise-shader`
- `commands-resize-path-effect`
- `commands-resize-point-dots`
- `commands-resize-point-lines`
- `commands-resize-radial-gradient-shader-color-filter`
- `commands-resize-runtime-effect-child-only`
- `commands-resize-runtime-effect-color-filter`
- `commands-resize-runtime-effect-color-filter-child`
- `commands-resize-runtime-effect-pure-color`
- `commands-resize-runtime-effect-shader`
- `commands-resize-runtime-effect-shader-color-filter`
- `commands-resize-runtime-effect-uniform-only`
- `commands-resize-save-layer-blend-color-filter`
- `commands-resize-save-layer-blend-mode`
- `commands-resize-save-layer-color-matrix-filter`
- `commands-resize-save-layer-filter`
- `commands-resize-skew-transform`
- `commands-resize-sweep-gradient-shader-color-filter`
- `commands-resize-transformed-shader`
- `commands-resize-turbulence-shader`
- `commands-resize-vertices`
- `commands-save-layer-color-matrix-filter`
- `commands-sweep-gradient-shader-color-filter`

## Removed Rows Since Latest Completed Full Sweep

These rows were present in the latest completed `549`-row full/default census but are not part of the current default
surface:

- `commands-graphics-layer-invalid-blend-mode-fallback`
- `commands-graphics-layer-unrecorded-fallback`
- `commands-invalid-fill-rect-blend-mode-height-fallback`
- `commands-invalid-fill-rect-blend-mode-width-fallback`
