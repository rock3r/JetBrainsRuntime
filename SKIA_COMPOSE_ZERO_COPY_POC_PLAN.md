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
- Stable descriptor rows assert JBR handle definitions, uses, cache hits, and context invalidation behavior.
- The default path-effect command row now also gates the stable descriptor-definition contract: five JBR-owned
  path-effect descriptor definitions and no picture fallback.
- Raw Skiko-owned shader/effect/path-effect families remain explicit fallback sentinels.
- Harness windows are non-focus-stealing by default via `MAGIC_JEWEL_BACKGROUND_WINDOW=true`.
- Validation iteration is now area-scoped by default for small sentinel slices: run the exact `CASES=...` row first,
  then the smallest relevant adjacent `CASES=...` slice or `CASE_GROUPS=...` subset, and reserve full default command
  sweeps for periodic consolidation.
- Primitive command parser guards now have the same point-to-point workflow. Skiko can corrupt a recorded stroke cap,
  transform record flags, or clip operation after recording, Magic Jewel exposes the exact rows and the
  `CASE_GROUPS=primitive-invalid` quick path, and the grouped validation passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-002341/suite.tsv`.
- SaveLayer parser guards continue to use the quick exact/group workflow. Skiko can now rewrite a plain
  `COMMAND_SAVE_LAYER` record-flags word to the antialias bit, which JBR rejects for saveLayer records even though the
  bit is accepted for paint-bearing records. The exact row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-045239/suite.tsv`.
  The expanded `CASE_GROUPS=save-layer-invalid` run now covers fifteen malformed saveLayer rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-045314/suite.tsv`.
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
- Image definition parser guards now cover record flags alongside the existing pixel payload-length guard. Skiko can
  rewrite `COMMAND_DEFINE_IMAGE_ARGB` record flags to the antialias bit, and Magic Jewel exposes the exact
  `commands-invalid-image-define-record-flags-fallback` row in the `image-handles-invalid` quick group. The exact row
  passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-053349/suite.tsv`.
  The expanded group covered twenty-one malformed image definition/cache-key and image-ref rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-053424/suite.tsv`.
- Image cache eviction parser guards now cover record flags too. Skiko can rewrite
  `COMMAND_EVICT_IMAGE_CACHE_KEY` record flags to the antialias bit, and Magic Jewel exposes the exact
  `commands-invalid-image-evict-record-flags-fallback` row in the same `image-handles-invalid` quick group. The exact
  row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-084204/suite.tsv`.
  The expanded group covered twenty-two malformed image definition/cache-key/eviction and image-ref rows and passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260520-084246/suite.tsv`.
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
- The already-live unknown effect descriptor type sentinel was refreshed on current artifacts. The exact
  `commands-invalid-effect-descriptor-type-fallback` row passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-190020/suite.tsv`.
- Fill-rect blend-mode scalar validation now has a quick group. Skiko can corrupt op 41
  `COMMAND_FILL_RECT_BLEND_MODE` width or height to `-1`, matching JBR's parser bounds guards. The exact two-row run
  passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-172416/suite.tsv`.
  The focused `CASE_GROUPS=blend-mode-invalid` run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-172545/suite.tsv`.
  A bounded default-order range from `commands-blend-mode` through `commands-graphics-layer` also passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-172903/suite.tsv`.
- Inline fill-rect color-filter scalar validation now has the same quick path. Skiko can corrupt op 42
  `COMMAND_FILL_RECT_COLOR_FILTER` tint blend mode, width, or height after recording. The exact three-row run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-173809/suite.tsv`.
  The focused `CASE_GROUPS=fill-rect-color-filter-invalid` run passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-174059/suite.tsv`.
  A bounded default-order range from `commands-raw-blend-color-filter-fallback` through `commands-color-filter` also
  passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260519-180104/suite.tsv`.
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
   Magic Jewel `CASE_GROUPS=...` for area slices during inner-loop work. Use `LIST_CASE_GROUPS=true` to print the
   current group names without launching validation. Current command groups include `smoke`, `path-invalid`,
   `effect-descriptor-invalid`, `shader-descriptor-invalid`, `gradient-invalid`, `gradient-path-invalid`,
   `runtime-effect-invalid`, `descriptor-handles-invalid`, `image-handles-invalid`, `color-filters`, `native-text`,
   `native-text-invalid`, and `graphics-layer`.
4. Run full default command/screenshot sweeps as checkpoint or periodic gates instead of every edit iteration.
5. Keep updating this compact plan; move verbose historical details to archive or focused docs, not back into this file.
6. Commit and push each major slice.

## Current Validation Hardening

Current validation gates are intentionally broad but summarized here to keep this file small:

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
  covers twenty-nine malformed shader descriptor rows.
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
