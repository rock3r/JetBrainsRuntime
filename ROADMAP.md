# JBR Skia Compose Zero-Copy Roadmap

This is the quick-open checklist for the local PoC. The detailed design and checkpoint log lives in
`SKIA_COMPOSE_ZERO_COPY_POC_PLAN.md`.

## Current Status

- [x] Worktrees created for JBR, Runtime API, Skiko, CMP, and Magic Jewel.
- [x] Magic Jewel standalone validation app created.
- [x] JBR private `com.jetbrains.desktop.JBRSkia` service and public Runtime API mirror added.
- [x] Skiko discovery/fallback path added with structured `SKIKO_JBR_INTEROP_FALLBACK reason=...` markers.
- [x] CMP routes ComposePanel to the JBR Skia command path when the strict compatibility gate passes.
- [x] Magic Jewel report compares old/new modes and records command/picture/fallback counters.
- [x] Magic Jewel summary exposes machine-readable FPS fields for app, Swing, popup, picture, and command markers.
- [x] Command mode validated with zero picture replay and zero fallback markers.

## Command ABI Coverage

- [x] ABI 1-13: basic command framing, solid rect/oval/line, transforms, clip, saveLayer.
- [x] ABI 14: inline image drawing.
- [x] ABI 15: image cache refs.
- [x] ABI 16: image cache clearing.
- [x] ABI 17: text-command rendering semantics.
- [x] ABI 18-25: paragraph text and paragraph style/layout metadata.
- [x] ABI 26: clip path.
- [x] ABI 27: draw path.
- [x] ABI 28: draw arc.
- [x] ABI 29: solid round-rect drawing.
- [x] ABI 30: linear-gradient rectangle fill.
- [x] ABI 31: 64-bit command capability gate.
- [x] ABI 32: linear-gradient rounded-rectangle fill.
- [x] ABI 33: radial-gradient rectangle fill.
- [x] ABI 34: radial-gradient rounded-rectangle fill.
- [x] ABI 35: linear-gradient path fill.
- [x] ABI 36: radial-gradient path fill.
- [x] ABI 37: sweep-gradient rectangle fill.
- [x] ABI 38: sweep-gradient rounded-rectangle fill.
- [x] ABI 39: sweep-gradient path fill.
- [x] ABI 40/native 2: scoped destination surface identity.
- [x] ABI 41/native 3: scoped destination context identity.
- [x] ABI 42: single-key image cache eviction.
- [x] ABI 43: native text commands carry JBR-resolved font-family names.
- [x] ABI 44: image-shader rectangle fills use serialized image refs and JBR-owned shader reconstruction.
- [x] ABI 45: linear-gradient stroked rectangles use serialized gradient and stroke metadata.
- [x] ABI 46: linear-gradient stroked rounded rectangles use serialized gradient, radii, and stroke metadata.

## Near-Term Rendering Work

- [x] Complete serialized sweep-gradient payloads for rectangles, rounded rectangles, and paths.
- [ ] Generic shader strategy:
  - [x] Short term: keep rejecting opaque/unknown shader pointers and add serialized command payloads for known shader families.
  - [x] Medium term design sketch: document a JBR-owned shader factory ABI so Skiko can request shader construction inside JBR's Skia runtime. See `doc/skia-shader-factory.md`.
  - [ ] Long term: revisit true generic shader support only after Skiko's fast path no longer creates Skia C++ objects in a separate bundled runtime.
- [x] Strict recorder fallback for invalid gradient stops and excessive gradient color counts.
- [x] Strict recorder fallback for invalid rounded-rectangle radii with gradient paints.
- [x] Strict recorder fallback for transformed gradient shaders.
- [x] Strict recorder fallback for composite/opaque shader wrappers.
- [x] Linear-gradient stroke paint graduated from strict fallback to serialized command replay.
- [x] More strict shader fallback tests for nonfinite gradient metadata that survives shader construction.
- [x] Live Magic Jewel recorder-level fallback probes for color filters and path effects.
- [x] Live Magic Jewel recorder-level fallback probe for unsupported saveLayer layer paint.
- [x] Live Magic Jewel recorder-level fallback probe for unsupported image paint.
- [x] Live Magic Jewel rendering probe for linear-gradient stroked rectangles.
- [x] Remaining near-term generic shader fallback probes beyond transformed/composite/opaque shader wrappers.
- [x] Expand screenshot assertions to check core primitive and gradient probe colors/regions explicitly.
- [x] Improve Magic Jewel visual fidelity so labels use Jewel `Text` and `JewelTheme.defaultTextStyle`.
- [x] Prefer fidelity-first text image replay by default so command mode preserves resolved Jewel font/size/alignment until JBR-owned typefaces exist.
- [x] Add font-family metadata to native text commands without sharing Skia `SkTypeface*` pointers.
- [x] Rebuild ABI 43 local artifacts and pass a native-text Magic Jewel command smoke with paragraph commands and screenshot assertion.
- [x] Add narrow image-shader rectangle support without sharing raw `SkShader*` pointers.
- [x] Add narrow linear-gradient stroked-rectangle support without sharing raw `SkShader*` pointers.
- [x] Add narrow linear-gradient stroked rounded-rectangle support without sharing raw `SkShader*` pointers.
- [x] Add screenshot-level text-presence assertions using stable pixel regions for top/bottom Magic Jewel labels.
- [x] Add screenshot-level text placement assertions using stable dark-pixel bounding boxes.
- [x] Add paragraph-row screenshot assertions for native-text layout probes.
- [x] Investigate observed text alignment drift in Magic Jewel/Jewel labels on the command path.

## Validation Harness

- [x] Focused CMP recorder tests for command encodings.
- [x] Focused CMP recorder tests for unsupported saveLayer layer-paint fallback.
- [x] Focused CMP recorder tests for unsupported image paint fallback.
- [x] Focused Skiko compatibility/fallback tests.
- [x] JBR Java service compile smoke.
- [x] JBR native dylib compile smoke.
- [x] Runtime API shim rebuild.
- [x] Magic Jewel report smoke for each ABI checkpoint.
- [x] Magic Jewel command-mode smoke for ABI 40 surface identity.
- [x] Quiet-machine benchmark pass using existing SKP/report paths.
- [x] Magic Jewel benchmark-suite wrapper for SKP picture, command, stable-image, dynamic-image, and resize workloads.
- [x] Magic Jewel benchmark suite supports selectable `CASES` for targeted reruns.
- [x] Benchmark suite writes a machine-readable `suite.tsv` with sample counts, CPU, FPS, and report paths.
- [x] Magic Jewel reports include host CPU count and load-average metadata for noisy-machine benchmark context.
- [x] ABI 43 quiet-machine benchmark refresh: `/tmp/magic-jewel-quiet-benchmark-abi43-20260430-110714/suite.tsv`.
- [x] ABI 44 quiet-machine benchmark refresh: `/tmp/magic-jewel-quiet-benchmark-abi44-20260430-122457/suite.tsv`.
- [x] ABI 45 quiet-machine benchmark refresh: `/tmp/magic-jewel-quiet-benchmark-abi45-20260430-131201/suite.tsv`.
- [x] Async-profiler integration in Magic Jewel report.
- [x] CI-friendly parser for Magic Jewel report summary.
- [x] Machine-readable Skiko surface-change marker count in Magic Jewel reports.
- [x] Live resize/surface-change Magic Jewel smoke requiring at least one Skiko surface-change marker.
- [x] Popup/layered Swing-over-Compose Magic Jewel smoke with screenshot pixels and popup paint markers.
- [x] Parser-level coverage for popup paint marker thresholds and machine-summary fields.
- [x] Parser-level screenshot count fields in `summary.properties`.
- [x] Selectable Magic Jewel command-probe suite for core primitives, gradients, text, popup, and fallback cases.
- [x] Command-probe suite writes a machine-readable `suite.tsv`.
- [x] ABI 43 command-probe suite refresh: `/tmp/magic-jewel-command-probe-abi43-20260430-111549/suite.tsv`.
- [x] ABI 44 image-shader command smoke: `/tmp/magic-jewel-abi44-image-shader-smoke-3/report.md`.
- [x] ABI 44 command-probe suite refresh: `/tmp/magic-jewel-command-probe-abi44-full-20260430-115943/suite.tsv`.
- [x] ABI 45 gradient-stroke command smoke: `/tmp/magic-jewel-abi45-gradient-stroke-smoke/report.md`.
- [x] ABI 45 gradient-stroke command-probe row: `/tmp/magic-jewel-command-probe-abi45-gradient-stroke-20260430-125550/suite.tsv`.
- [x] ABI 45 command-probe suite refresh: `/tmp/magic-jewel-command-probe-abi45-full-20260430-130148/suite.tsv`.
- [x] ABI 46 gradient stroke rounded-rectangle smoke: `/tmp/magic-jewel-abi46-gradient-stroke-round-rect-smoke-2/report.md`.
- [x] ABI 46 gradient-surfaces command-probe row: `/tmp/magic-jewel-command-probe-abi46-gradient-surfaces/suite.tsv`.
- [x] Real undecorated Swing popup-window smoke captured separately by window id.

## Compatibility And ABI Hardening

- [x] Exact ABI/build compatibility gate.
- [x] Reflective ABI/BUILD reads to avoid compile-time constant inlining.
- [x] 64-bit command capability accessor and Skiko gate.
- [x] Structured native C ABI version block beyond Java-level command constants.
- [x] BUILD_ID includes pinned Skia revision plus compile-flags hash.
- [x] New Skiko / pre-native-metadata JBR fallback unit coverage.
- [x] Native ABI mismatch Magic Jewel launch/report validation.
- [x] Explicit Magic Jewel shader-boundary fallback validation.
- [x] CI-style parser coverage for ABI/capability/native/public-API handshake fallbacks.
- [x] Skiko reads JBR scoped paint ids and includes them in acquisition diagnostics.
- [x] Parser-level tests for old Skiko/new JBR and new Skiko/old JBR fallback combinations.
- [x] Launch-level forced compatibility matrix runner for happy path plus ABI/native-ABI/capability/public-API fallbacks.
- [x] Launch-level artifact matrix scaffold for named current/old JBR API, JBR native, desktop patch, Skiko, and CMP artifacts.
- [x] Artifact matrix can require old-artifact rows in CI and fail if optional bundles are missing.
- [x] Skiko invalidates temporary cached surface state when scoped destination identity changes.
- [x] Magic Jewel parser coverage for `SKIKO_JBR_INTEROP_SURFACE_CHANGED`.
- [x] Magic Jewel launch-level resize validation for surface identity changes.
- [x] Unit-tested Skiko surface identity tracker.
- [ ] Full launch-level old/new packaged artifact matrix with real old bundles supplied for every optional row.
- [x] Stronger JBR context identity separate from per-surface identity for production cache keys.
- [x] Skiko distinguishes same-context surface replacement from context migration.
- [x] Magic Jewel strict resize validation for `contextChanged=false surfaceChanged=true`.
- [x] JBR image cache entries are scoped by destination context id.
- [x] Magic Jewel report counts scoped JBR image-cache-clear markers.
- [x] Combined resize + image-cache churn smoke proving stable context-scoped cache ownership across same-context surface replacement.
- [x] Strict report assertion for scoped JBR image-cache-clear markers.
- [x] Reduce redundant image-cache clear/define churn for stable fallback images.
- [x] Replace whole-cache churn with oldest-entry eviction in CMP recorder.
- [x] Add long-running quiet-machine benchmark pass for stable/dynamic image-cache workloads.

## Productionization Later

- [ ] Replace local patched-class/dylib launch wiring with real JBR build integration.
- [ ] Decide final Skiko artifact shape for Skia-less JBR interop.
- [ ] Font/typeface ownership through the JBR Skia runtime.
- [ ] JBR-owned generic shader factory and handles for non-serialized shader families.
- [ ] Screen migration/context invalidation hardening.
- [x] Real menu stress tests beyond popup-window coverage.
- [ ] JCEF/shared-texture exploration after Compose is solid.
- [ ] Windows/Linux backend adapter investigation after macOS MVP.
