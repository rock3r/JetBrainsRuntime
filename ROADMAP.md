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

## Near-Term Rendering Work

- [x] Complete serialized sweep-gradient payloads for rectangles, rounded rectangles, and paths.
- [ ] Generic shader strategy:
  - [ ] Short term: keep rejecting opaque/unknown shader pointers and add serialized command payloads for known shader families.
  - [ ] Medium term: design a JBR-owned shader factory ABI so Skiko can request shader construction inside JBR's Skia runtime.
  - [ ] Long term: revisit true generic shader support only after Skiko's fast path no longer creates Skia C++ objects in a separate bundled runtime.
- [x] Strict recorder fallback for invalid gradient stops and excessive gradient color counts.
- [x] Strict recorder fallback for invalid rounded-rectangle radii with gradient paints.
- [ ] More strict shader fallback tests: transformed shaders, nonfinite shader geometry that survives shader construction, and broader generic shader cases.
- [ ] Expand screenshot assertions to check newly added probe colors/regions explicitly.
- [x] Improve Magic Jewel visual fidelity so labels use Jewel `Text` and `JewelTheme.defaultTextStyle`.
- [ ] Add screenshot-level text/typography assertions once the harness can make stable OCR or pixel-region claims.

## Validation Harness

- [x] Focused CMP recorder tests for command encodings.
- [x] Focused Skiko compatibility/fallback tests.
- [x] JBR Java service compile smoke.
- [x] JBR native dylib compile smoke.
- [x] Runtime API shim rebuild.
- [x] Magic Jewel report smoke for each ABI checkpoint.
- [ ] Quiet-machine benchmark pass using existing SKP/report paths.
- [x] Async-profiler integration in Magic Jewel report.
- [x] CI-friendly parser for Magic Jewel report summary.

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
- [x] Skiko invalidates temporary cached surface state when scoped destination identity changes.
- [ ] Full launch-level old/new packaged artifact matrix.

## Productionization Later

- [ ] Replace local patched-class/dylib launch wiring with real JBR build integration.
- [ ] Decide final Skiko artifact shape for Skia-less JBR interop.
- [ ] Font/typeface ownership through the JBR Skia runtime.
- [ ] JBR-owned generic shader factory and handles for non-serialized shader families.
- [ ] Screen migration/context invalidation hardening.
- [ ] Popup/menu and layered Swing/Compose stress tests.
- [ ] JCEF/shared-texture exploration after Compose is solid.
- [ ] Windows/Linux backend adapter investigation after macOS MVP.
