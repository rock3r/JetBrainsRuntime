# Current Validation Log

This file keeps the rolling validation ledger out of the top-level roadmap and plan. Keep the newest high-signal
entries here, and move older narrative detail to `docs/history/` only when this file starts getting noisy.

## Latest Broad Sweeps

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
