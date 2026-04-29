# Skia / Compose Zero-Copy PoC Plan

## Summary

Build a macOS-first PoC across JBR, Skiko, and Compose Multiplatform Core that lets `ComposePanel(RenderSettings.SwingGraphics)` render directly into a JBR-owned Metal-backed Skia surface during Swing painting, avoiding the current Skia GPU -> CPU bitmap -> Swing upload path.

## Checkpoint Status

### Checkpoint 1: ABI, Discovery, And Runnable Fallback MVP

Status: completed.

- JBR worktree created at `/Users/rock3r/src/jbr-skia-compose-poc`.
- Skiko worktree created at `/Users/rock3r/src/skiko-jbr-skia-poc`.
- CMP worktree created at `/Users/rock3r/src/cmp-jbr-skia-poc`.
- JBR now has the initial `com.jetbrains.desktop.JBRSkia` API shape, macOS `JBRSkiaService` provider stub, synthetic JBRApi test, and `--with-skia-interop` configure option.
- Skiko now has reflective JBR discovery, ABI/BUILD gate behavior, scoped-canvas acquisition skeleton, and structured fallback markers.
- CMP now has `useJbrSkiaInteropInComposePanel`, `compose.swing.render.on.jbr.skia`, SwingGraphics paint-time interop probing, and a runnable sample task.
- Runnable MVP smoke command:
  - `cd /Users/rock3r/src/cmp-jbr-skia-poc`
  - `./gradlew :compose:desktop:desktop:desktop-samples:runSwingJbrSkiaInterop`
- Verified smoke behavior: when the Skiko JBR interop runtime is absent or incompatible, the app stays on the old SwingGraphics path and emits `SKIKO_JBR_INTEROP_FALLBACK reason=skiko-jbr-runtime-missing`.
- Verification completed:
  - Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - CMP `./gradlew :compose:ui:ui:desktopTest --tests androidx.compose.ui.JbrSkiaInteropFeatureFlagTest`
  - CMP `./gradlew :compose:desktop:desktop:desktop-samples:jvmJar`
  - JBR `bash configure --help=short`
  - `git diff --check` in all three worktrees.

Known limitation: this checkpoint is a runnable fallback MVP, not zero-copy rendering. It proves feature gating, discovery, warning/fallback, and sample launch plumbing. Direct rendering still requires the native JBR Skia/Metal scope and Skiko Skia-less interop artifact.

### Checkpoint 2: Version-Aligned Sample And Report Harness

Status: completed for the version-aligned fallback MVP harness.

- Skiko and CMP worktrees are now rebased on current fetched remote heads:
  - Skiko branch `skiko-jbr-skia-poc` is `0 behind / 1 ahead` of `origin/master`.
  - CMP branch `cmp-jbr-skia-poc` is `0 behind / 2 ahead` of `origin/jb-main`.
- The local Skiko/CMP version mismatch was caused by CMP resolving a remote timestamped `0.0.0-SNAPSHOT` before `mavenLocal()`, not by missing compatibility shims in the rebased Skiko source.
- CMP now prioritizes `mavenLocal()` for `org.jetbrains.skiko` artifacts when `SKIKO_VERSION` is set, so `SKIKO_VERSION=0.0.0-SNAPSHOT` resolves the locally published Skiko worktree artifact deterministically.
- Added parseable old/new sample launch and report harness in CMP:
  - `compose/desktop/desktop/samples/scripts/jbr-skia-interop-report.sh`
  - old mode: `:compose:desktop:desktop:desktop-samples:runSwing`
  - new mode: `:compose:desktop:desktop:desktop-samples:runSwingJbrSkiaInterop`
- The report records fallback markers, process CPU samples, RSS samples, elapsed run metadata, and log/csv file locations.
- Smoke report generated at `/tmp/jbr-skia-report-smoke2/report.md`.
  - old marker count: `0`
  - new marker count: `1`
  - observed marker: `SKIKO_JBR_INTEROP_FALLBACK reason=skiko-jbr-runtime-missing`
- Zero-copy fast-path frame counts remain reported as unavailable until the native JBR scope exists.
- Additional verification after rebasing:
  - Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - Skiko `./gradlew :skiko:publishToMavenLocal`
  - CMP `./gradlew :compose:ui:ui:desktopTest --tests androidx.compose.ui.JbrSkiaInteropFeatureFlagTest`
  - CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:ui:ui-graphics:dependencyInsight --configuration desktopCompileClasspath --dependency org.jetbrains.skiko --refresh-dependencies`
  - CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:desktop:desktop:desktop-samples:jvmJar --refresh-dependencies`
  - CMP smoke run `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:desktop:desktop:desktop-samples:runSwingJbrSkiaInterop`
- Verified smoke behavior with local Skiko present: the app reaches the runnable sample and emits Skiko's structured fallback marker `SKIKO_JBR_INTEROP_FALLBACK reason=public-api-missing`, which is expected until the runtime JBR/public API surface exposes `JBRSkia`.

### Checkpoint 3: JBR Scope Acquisition

Status: completed for Java/Kotlin scope acquisition; native Metal/Skia pointers still pending.

- JBR now exposes an enabled-mode `JBRSkiaService` when `-Dsun.java2d.skia.interop=true`.
- `JBRSkiaService.acquireCanvas(Graphics2D)` returns a scoped object with:
  - monotonic scope id
  - backend `METAL`
  - user-space clip snapshot
  - sample count metadata
  - idempotent `close()` and guarded `flush()` lifecycle behavior.
- The current scope deliberately reports `canvasPtr=0` and `directContextPtr=0`. This is a placeholder scope for acquisition/lifecycle validation only; it is not yet a Skia/Metal draw target.
- Skiko closes scopes through the public `AutoCloseable` interface so private JBR scope implementations do not trip module-boundary reflection access checks.
- Skiko emits `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED abi=1 build=skia-interop-poc:1` when the scope path is reached.
- Local runnable verification used:
  - a patched `java.desktop` module containing the JBR worktree `JBRSkia`/`JBRSkiaService` classes
  - a temporary public `com.jetbrains.JBR` accessor shim because the real external public JBR API jar is not present in these checkouts
  - CMP sample task `runSwingJbrSkiaInterop`
- Verified sample behavior: the runnable CMP sample reaches `CMP -> Skiko JbrSkiaSwingLayer -> JBRSkiaService.acquireCanvas(...)` and emits `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED abi=1 build=skia-interop-poc:1`.
- Verification completed:
  - JBR patched-module compile/run of `JBRSkiaApiTest`
  - Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaDebugOverlayTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - Skiko `./gradlew :skiko:publishToMavenLocal`
  - CMP patched-service smoke run with `SKIKO_VERSION=0.0.0-SNAPSHOT`.

Next native checkpoint:

- Build/run the CMP sample on a local JBR image rather than patching classes into the current JVM.
- Replace the placeholder scope with the first real macOS Metal paint-scope acquisition:
  - destination `MTLContext`/device identity
  - destination texture identity
  - load-not-clear behavior
  - command ordering before later Java2D commands in the same paint pass
  - non-zero Skia canvas/direct-context pointers only after ABI/build compatibility is proven.

### Checkpoint 4: Public JBR API Mirror And Metal Texture Diagnostic

Status: completed for source/API wiring; full runtime validation requires a JBR image built from this branch.

- Added a fourth worktree, `/Users/rock3r/src/jbr-api-skia-poc`, for `JetBrainsRuntimeApi`.
- Added experimental public `com.jetbrains.JBRSkia` to the public JBR API source, including:
  - non-constant `ABI_ID`
  - non-constant `BUILD_ID`
  - `JBR.getJBRSkia()` / `JBR.isJBRSkiaSupported()` generated accessors
  - `ScopedSkiaCanvas` public mirror with `AutoCloseable`.
- Updated Skiko discovery to prefer the real public API shape:
  - read `ABI_ID` / `BUILD_ID` reflectively from `com.jetbrains.JBRSkia`
  - acquire the service reflectively from `com.jetbrains.JBR.getJBRSkia()`
  - keep a fallback to the earlier `com.jetbrains.desktop.JBRSkia` mirror only for local patched-runtime smoke tests.
- JBR scope metadata now includes `getMetalTexturePtr()` so the next runnable checkpoint can prove that Swing's current destination texture is visible at the Java scope boundary.
- `JBRApiSupport` has a gated registry-file override, `-Djetbrains.runtime.api.registry=<file>`, intended only with the existing test gate `-Djetbrains.runtime.api.extendRegistry=true`. This lets local PoC runs exercise a newly generated public API against a patched runtime before a full JBR image is available.
- Local generated-public-API verification completed in `JetBrainsRuntimeApi`:
  - `tools/build.sh process`
  - `tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev`
  - `javap` confirmed `JBR.getJBRSkia()` and `JBR.isJBRSkiaSupported()`.
- Local runtime validation found an expected limitation: the installed JBR 21 runtime does not contain this source tree's newer `com.jetbrains.internal.jbrapi` backend, and this JBR source requires a JDK 26/27 boot JDK for a full image build. Until a matching local JBR image is built, the generated public accessor cannot replace the temporary shim in the sample run.

Next native checkpoint:

- Build or obtain a matching JBR image from `/Users/rock3r/src/jbr-skia-compose-poc` so the generated public JBR API registry and `JBRSkiaService` are loaded together.
- Run the CMP sample with the real public `com.jetbrains.JBR.getJBRSkia()` path and prove:
  - `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED`
  - non-zero `metalTexture`
  - fallback still works when the ABI/build gate is deliberately mismatched.
- Use the exposed destination texture to prototype an actual Skiko render-to-JBR-texture path, while keeping the planned JBR-owned Skia C ABI as the correctness target.

### Checkpoint 5: Destination Texture Probe

Status: completed as a probe; direct Skiko-owned rendering into the JBR texture is intentionally disabled by default.

- JBR scope metadata now returns a non-zero destination Metal texture pointer during the CMP Swing paint smoke run.
- Skiko can read scope methods through the public scope superclass instead of reflectively invoking private implementation-class methods.
- CMP sample task `runSwingJbrSkiaInterop` accepts extra PoC JVM arguments through `-PjbrSkiaInteropJvmArgs=...`, so patched-module and temporary API-shim runs do not require editing the task each time.
- Smoke command with patched `java.desktop` and temporary public API shim reached:
  - `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED abi=1 build=skia-interop-poc:1 metalTexture=0xb9343c280`
- A deliberately unsafe Skiko-owned render-to-destination-texture experiment was added behind `-Dskiko.jbr.interop.renderToTexture=true`.
- That experiment crashed with SIGTRAP when wrapping the JBR texture with a Skiko-created Metal `DirectContext`, which confirms the plan's core ownership/ordering risk: this cannot be made correct by casually mixing Skiko's Metal queue/context with JBR's destination texture.

Next native checkpoint:

- Stop trying to submit with Skiko's own Metal context for the fast path.
- Move to the planned JBR-owned Skia/Metal C ABI:
  - JBR creates/owns the Skia `GrDirectContext` on the Java2D Metal queue.
  - JBR wraps the destination texture into a Skia surface/canvas.
  - Skiko calls only JBR ABI functions for drawing on that scope.
  - Skiko's bundled Skia path remains fallback-only.

### Checkpoint 6: JBR-Owned Paint Invocation

Status: completed as a diagnostic bridge; native Skia drawing is still the next implementation step.

- Added a PoC-only scoped `renderDiagnosticFrame(width, height, frameTimeNanos)` API to the private JBR API and the public `JetBrainsRuntimeApi` mirror.
- Skiko now invokes this method through the acquired JBR scope when `-Dskiko.jbr.interop.renderDiagnostic=true` is set.
- The diagnostic method proves the desired call direction:
  - CMP routes `SwingGraphics` painting into `JbrSkiaSwingLayer`.
  - Skiko acquires the JBR scope through the version-gated public API path.
  - JBR owns the actual paint into the current Java2D destination.
  - Skiko does not wrap the JBR texture with its own Metal `DirectContext`.
- Smoke command with patched `java.desktop`, temporary public API shim, and local Skiko reached:
  - `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED abi=1 build=skia-interop-poc:1 metalTexture=0xbe9274780`
- Screenshot captured at `/tmp/jbr-skia-diagnostic-render.png`.
- This checkpoint intentionally paints a JBR diagnostic pattern, not Compose UI and not native Skia yet. It closes the previous ownership gap by proving the correct direction for the next bridge: Skiko calls a JBR-owned render entry point inside the paint scope.

Next native checkpoint:

- Replace the Java diagnostic body with the first native JBR-owned Skia/Metal implementation:
  - bootstrap or link the pinned Skia runtime into the JBR branch
  - create a JBR-owned Skia direct context on the Java2D Metal queue
  - wrap the current destination texture with load-not-clear semantics
  - expose a narrow native C ABI entry point that can render a minimal Skia test pattern
  - only after that succeeds, map the Skiko/CMP renderer command surface onto the JBR-owned ABI.

### Checkpoint 7: Native JBR Skia Diagnostic Render

Status: completed as a local native probe; production build wiring and queue ownership cleanup are still pending.

- Added `JBRSkiaInterop.mm`, a JBR-side Objective-C++ JNI bridge that:
  - receives the current destination `MTLTexture*` from the Java paint scope
  - creates a Skia Metal `GrDirectContext`
  - wraps the destination texture with `GrBackendRenderTargets::MakeMtl`
  - draws a minimal Skia pattern into the wrapped surface
  - flushes/submits before returning to the Java paint scope.
- `JBRSkiaService.renderDiagnosticFrame(...)` now optionally tries the native Skia renderer first when both properties are set:
  - `-Dsun.java2d.skia.interop.nativeDiagnostic=true`
  - `-Dsun.java2d.skia.interop.library=<absolute path to dylib>`
- If the native bridge is absent or returns false, the existing Java2D diagnostic remains the fallback.
- Local dylib build used Skiko's pinned m147 arm64 Skia archive set as a stand-in for the future JBR-vendored Skia build:
  - include/archive root: `/Users/rock3r/src/skiko-jbr-skia-poc/skiko/dependencies/skia/m147-64a2414108/Skia-m147-64a2414108-macos-Release-arm64`
  - output: `/tmp/jbr-skia-native/libjbrskiainterop.dylib`
- Smoke command with patched `java.desktop`, temporary public API shim, local Skiko, and native JBR Skia dylib reached:
  - `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED abi=1 build=skia-interop-poc:1 metalTexture=0xc289d7200`
- Screenshot captured at `/tmp/jbr-skia-native-diagnostic-render.png`.
- Visual validation: the component body changed from the Java diagnostic pattern to the native Skia pattern: dark purple background, teal diagonal strokes, and magenta rounded rect.

Important caveats:

- This is still a diagnostic Skia pattern, not Compose UI rendering.
- The local dylib is not yet wired into JBR `make/`; it is a manual build artifact for proving the native path.
- The probe currently derives the Metal device from `texture.device` and creates a fresh command queue. This proves Skia can draw into the destination texture from a JBR-owned native bridge, but the production path still needs to use JBR's existing `MTLContext.commandQueue` for correct Java2D ordering.
- The local dylib links Skia m147 from the Skiko worktree. The production path must vendor/pin the same Skia revision in JBR and expose its ABI/BUILD identity through the documented JBR/Skiko gate.

Next native checkpoint:

- Move from local probe to integrated JBR bridge:
  - expose the destination `MTLContext`/command queue to `JBRSkiaInterop.mm`
  - wire the native file into the JBR build behind `--with-skia-interop`
  - cache/reuse the JBR-owned `GrDirectContext` per destination `MTLContext`
  - preserve load-not-clear semantics and submit before subsequent Java2D commands
  - then replace the hard-coded diagnostic pattern with the first Skiko/CMP command bridge.

### Checkpoint 8: Native Skia On JBR's Metal Queue

Status: completed as a local native probe; build integration and context caching are still pending.

- `JBRSkiaService` now captures both pieces of render-thread Metal metadata during scope acquisition:
  - `AccelSurface.getNativeOps()`, giving the native `BMTLSDOps*`
  - `AccelSurface.getNativeResource(AccelSurface.TEXTURE)`, giving the destination `MTLTexture*`
- `JBRSkiaInterop.mm` now derives the destination `MTLContext` from the native surface ops and uses:
  - `MTLContext.device`
  - `MTLContext.commandQueue`
  - `MTLContext.encoderManager.endEncoder()` before Skia encodes its work.
- The native bridge rejects mismatched devices instead of silently drawing through an unrelated Metal context.
- The local out-of-build dylib still links Skia m147 from the Skiko worktree and uses small temporary generated-header stand-ins under `/tmp/jbr-skia-native/generated` because a full JBR generated include tree is not available in this patched-module probe.
- Smoke command with patched `java.desktop`, temporary public API shim, local Skiko, and rebuilt native bridge reached:
  - `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED abi=1 build=skia-interop-poc:1 metalTexture=0xc9a1b0280`
- Screenshot captured at `/tmp/jbr-skia-jbr-queue-diagnostic-render.png`.
- Visual validation: the component still shows the native Skia pattern, confirming that Skia drawing survived the move from a fresh command queue to JBR's destination Metal command queue.

Important caveats:

- This is still a diagnostic Skia pattern, not Compose UI rendering.
- The bridge creates a fresh `GrDirectContext` per diagnostic call. Production needs a cache keyed by destination `MTLContext`/BUILD_ID and invalidated on screen migration, display changes, resize, or surface loss.
- The Objective-C++ file currently forward-declares the small `MTLContext`/`EncoderManager` selector surface it needs and includes the real `MTLSurfaceDataBase.h` for `BMTLSDOps`. When wired into the JBR build, replace the temporary generated-header workaround with the normal generated include directory.
- The bridge still does not expose a Skiko/CMP drawing command ABI; it only proves that JBR-owned native Skia can paint into Swing's current destination texture using Java2D's queue.

Next native checkpoint:

- Wire `JBRSkiaInterop.mm` into the JBR build behind `--with-skia-interop`.
- Cache/reuse `GrDirectContext` per `MTLContext` and document/test invalidation.
- Add a tiny command-list ABI from Skiko to JBR, starting with simple rect/line/fill commands, so CMP can paint recognizable Compose-owned content through the JBR-owned Skia surface.

### Checkpoint 9: First Skiko-Generated Command Frame

Status: completed as a PoC command-list bridge; full Compose display-list replay is still pending.

- Added a temporary flat integer command ABI to the JBR private API and public Runtime API mirror:
  - `COMMAND_CLEAR`: `[op, argb]`
  - `COMMAND_FILL_RECT`: `[op, argb, x, y, width, height, radius]`
  - `COMMAND_STROKE_LINE`: `[op, argb, x1, y1, x2, y2, strokeWidth]`
- Added `ScopedSkiaCanvas.renderCommandFrame(width, height, frameTimeNanos, commands)` across:
  - JBR private API
  - JBR macOS service implementation
  - `JetBrainsRuntimeApi`
  - Skiko reflective interop wrapper.
- `JBRSkiaInterop.mm` now parses the command list and executes it on a Skia surface wrapping the current Java2D destination texture, using JBR's `MTLContext.commandQueue`.
- Skiko has a new gated command-frame mode:
  - `-Dskiko.jbr.interop.renderCommands=true`
  - this mode builds a small animated command list in `JbrSkiaSwingLayer` and sends it to JBR instead of drawing the hard-coded JBR diagnostic.
- Verification completed:
  - JBR patched-module compile for `JBRSkia` / `JBRSkiaService`
  - local native dylib rebuild at `/tmp/jbr-skia-native/libjbrskiainterop.dylib`
  - Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - Skiko `./gradlew :skiko:publishToMavenLocal`
  - CMP sample smoke with patched `java.desktop`, temporary public API shim, local Skiko, and `skiko.jbr.interop.renderCommands=true`
  - window-id capture through `compose/desktop/desktop/samples/scripts/capture-macos-window.sh`
- Smoke reached:
  - `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED abi=1 build=skia-interop-poc:1 metalTexture=0x7910b8500`
- Window-only screenshot captured at `/tmp/jbr-skia-command-frame-window-script.png`.
- Visual validation: the sample component shows the command-frame colors and geometry generated by Skiko, with the existing Java2D/Swing buttons around it still composited normally.

Important caveats:

- This is still not arbitrary Compose UI rendering. It is the first proof that Skiko can send drawing operations to JBR-owned native Skia without owning the Skia/Metal context itself.
- The command ABI is intentionally throwaway. The production bridge should move to a tightly versioned native ABI with explicit struct layout, validation, and no Java int-array parsing on the hot path.
- The command frame still uses user-space dimensions; HiDPI/device-pixel scaling needs to be pinned before real Compose content is replayed.

Next native checkpoint:

- Replace the toy command generator with a minimal Compose draw-operation recorder/replayer slice, starting with solid fills and simple rectangles from real Compose painting.
- Cache/reuse the JBR-owned `GrDirectContext` per `MTLContext`.
- Add automated screenshot/pixel assertions for the command-frame sample so future queue/context changes cannot silently fall back to Java2D.

### Checkpoint 10: Serialized SkPicture Replay Probe

Status: completed as a runnable probe; correctness, clipping, HiDPI, and font behavior need focused follow-up.

- Added `ScopedSkiaCanvas.renderPictureFrame(width, height, frameTimeNanos, pictureData)` across:
  - JBR private API
  - JBR macOS service implementation
  - `JetBrainsRuntimeApi`
  - Skiko reflective interop wrapper.
- Skiko now has a gated picture replay mode:
  - `-Dskiko.jbr.interop.renderPicture=true`
  - `JbrSkiaSwingLayer` records the real `SkikoRenderDelegate.onRender(...)` call into a Skiko `PictureRecorder`
  - the resulting `Picture` is serialized to bytes and sent to JBR.
- `JBRSkiaInterop.mm` now deserializes the byte payload with JBR-owned Skia (`SkPicture::MakeFromData`) and replays it into the Java2D destination texture using JBR's `MTLContext.commandQueue`.
- This avoids sharing Skia object pointers across the Skiko/JBR boundary for this path. The payload is serialized data, not a raw `SkPicture*`, `SkCanvas*`, or `GrDirectContext*`.
- Verification completed:
  - JBR patched-module compile for `JBRSkia` / `JBRSkiaService`
  - local native dylib rebuild at `/tmp/jbr-skia-native/libjbrskiainterop.dylib`
  - Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - Skiko `./gradlew :skiko:publishToMavenLocal`
  - Runtime API `bash tools/build.sh process`
  - CMP sample smoke with patched `java.desktop`, temporary public API shim, local Skiko, and `skiko.jbr.interop.renderPicture=true`
- Smoke reached:
  - `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED abi=1 build=skia-interop-poc:1 metalTexture=0x7cdaac280`
- Window-id screenshot captured at `/tmp/jbr-skia-picture-frame-window.png`.
- Visual validation: the Swing frame and surrounding Java2D controls remain visible, while the Compose/Skiko render delegate content is replayed by JBR-owned native Skia from serialized picture bytes.

Important caveats:

- The replayed content is visibly rough: clipping, HiDPI scaling, and text positioning are not yet correct.
- SKP serialization is not the final ABI. It is useful as a bridge probe because it avoids pointer identity problems, but it must be evaluated carefully for fonts/typefaces, image payloads, version stability, and security/validation of serialized data.
- The current code records and serializes every frame, which is expected to be expensive. The next slices need caching/invalidation and CPU cost measurements.

Next native checkpoint:

- Fix the picture replay coordinate contract:
  - determine whether the recorded picture is in Swing user space or device pixels
  - apply the same clip/transform on JBR replay that the old Skiko Swing path applies
  - add screenshot/pixel assertions for a small no-text Compose scene.
- Add logging/report markers for picture serialization size and replay success/failure.
- Start measuring CPU overhead versus old SwingGraphics readback, since per-frame SKP serialization may trade GPU copies for CPU work.

### Checkpoint 11: Device-Pixel Picture Replay And Destination Placement

Status: completed as a macOS runnable correctness slice; interop child ordering and automated image assertions remain pending.

- Skiko picture mode now records the serialized `SkPicture` at device-pixel size, matching the existing `SwingRedrawerBase` contract of `componentSize * graphicsConfiguration.defaultTransform.scale`.
- Added focused Skiko tests for the JBR picture-frame size helper, including fractional scale and invalid-scale fallback.
- JBR scope acquisition now derives a device-space paint clip from the scoped `Graphics2D` transform/clip at acquire time.
- `JBRSkiaInterop.mm` now wraps the full destination `MTLTexture`, clips to the device-space destination rect, translates to that rect, and then replays the serialized picture. This fixes the previous behavior where every Compose panel replayed at texture origin and could stomp the whole Swing destination.
- CMP's sample window capture helper now uses CoreGraphics `.optionAll` and filters captureable layer-0 windows, so it can capture the sample even when it lives on another macOS Space.
- Verification completed:
  - JBR patched-module compile for `JBRSkia` / `JBRSkiaService`
  - local native dylib rebuild at `/tmp/jbr-skia-native/libjbrskiainterop.dylib`
  - Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - Skiko `./gradlew :skiko:publishToMavenLocal`
  - CMP sample smoke with patched `java.desktop`, temporary public API shim, local Skiko, and `skiko.jbr.interop.renderPicture=true`
  - window-id capture via `compose/desktop/desktop/samples/scripts/capture-macos-window.sh`
- Smoke reached:
  - `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED abi=1 build=skia-interop-poc:1 metalTexture=0x7f9adc280`
- Window-only screenshot captured at `/tmp/jbr-skia-picture-frame-positioned-window-script.png`.
- Visual validation: both green and blue Compose panels are now visible, scaled correctly on Retina, and placed in their Swing paint regions while surrounding Swing controls remain visible.

Important caveats:

- The current bridge still serializes an SKP every frame; this is a correctness bridge, not the final performance answer.
- Swing interop children inside Compose panels are visible and placed, but ordering/clipping still needs specific tests. The yellow debug overlay can occlude sample content and should stay diagnostic-only.
- JBR still creates a fresh `GrDirectContext` for each replay. The next implementation slice should cache per destination `MTLContext` and invalidate on screen migration/surface loss.

Next native checkpoint:

- Add parseable picture-frame markers for serialized byte size, replay success/failure, destination rect, and frame count.
- Add automated screenshot/pixel assertions for a small no-text Compose scene and one Swing-interop child scene.
- Cache/reuse the JBR-owned `GrDirectContext` per `MTLContext`.
- Start the before/after CPU report against the old SwingGraphics path to determine whether SKP serialization is acceptable as an interim bridge.

### Checkpoint 12: Picture Markers And First Visual Assertion

Status: completed for marker plumbing and a coarse screenshot assertion; report integration is next.

- Skiko now emits a parseable marker for every JBR picture replay attempt:
  - `SKIKO_JBR_INTEROP_PICTURE_FRAME width=<px> height=<px> bytes=<n> rendered=<true|false>`
- JBR native replay now emits a parseable destination marker after successful replay:
  - `JBR_SKIA_INTEROP_PICTURE_FRAME destinationX=<px> destinationY=<px> destinationWidth=<px> destinationHeight=<px> width=<px> height=<px> bytes=<n> rendered=true`
- Added a focused Skiko test that locks the `SKIKO_JBR_INTEROP_PICTURE_FRAME` marker format.
- Added CMP sample helper:
  - `compose/desktop/desktop/samples/scripts/assert-jbr-skia-window-screenshot.sh`
  - It samples the captured PNG and emits `JBR_SKIA_SCREENSHOT_COUNTS green=<n> blue=<n> purple=<n> yellow=<n>`, then fails if expected color regions are missing.
- Verification completed:
  - Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - Skiko `./gradlew :skiko:publishToMavenLocal`
  - JBR patched-module compile for `JBRSkia` / `JBRSkiaService`
  - local native dylib rebuild at `/tmp/jbr-skia-native/libjbrskiainterop.dylib`
  - CMP sample smoke with patched `java.desktop`, temporary public API shim, local Skiko, and `skiko.jbr.interop.renderPicture=true`
  - window capture plus screenshot assertion:
    - `/tmp/jbr-skia-picture-frame-markers-window.png`
    - `JBR_SKIA_SCREENSHOT_COUNTS green=428783 blue=511362 purple=83318 yellow=26824`
- Smoke markers included:
  - `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED abi=1 build=skia-interop-poc:1 metalTexture=0x85b550280`
  - `SKIKO_JBR_INTEROP_PICTURE_FRAME width=1460 height=492 bytes=415432066 rendered=true`
  - `JBR_SKIA_INTEROP_PICTURE_FRAME destinationX=140 destinationY=572 destinationWidth=1460 destinationHeight=492 width=1460 height=492 bytes=415432066 rendered=true`

Important caveats:

- The SKP payload can be extremely large: this run observed a 415 MB serialized picture for a `1460x492` top-level panel. Treat SKP replay as a correctness bridge, not the expected performance architecture.
- The screenshot assertion is deliberately coarse. It catches blank/fallback/misplaced rendering, but it does not yet compare exact layout, text fidelity, or Swing interop child ordering.

Next native checkpoint:

- Wire these markers into `jbr-skia-interop-report.sh` so the report records frame count, bytes per frame, replay success, and screenshot assertion status.
- Add a smaller deterministic no-text visual sample to reduce noise in screenshot assertions.
- Start CPU comparison runs using the old SwingGraphics path versus the JBR picture path, with the SKP byte count called out prominently.

### Checkpoint 13: Report Harness Parses Picture Replay And Screenshot Assertions

Status: completed for a short local report smoke; longer runs and a deterministic Jewel scene remain pending.

- CMP's `jbr-skia-interop-report.sh` now supports:
  - `NEW_JVM_ARGS`, passed through to `runSwingJbrSkiaInterop` as `-PjbrSkiaInteropJvmArgs=...`
  - `CAPTURE_WINDOW_QUERY`, used to capture and assert the new-mode window
  - Skiko picture marker summaries from `SKIKO_JBR_INTEROP_PICTURE_FRAME`
  - JBR native replay marker summaries from `JBR_SKIA_INTEROP_PICTURE_FRAME`
  - screenshot assertion counts from `JBR_SKIA_SCREENSHOT_COUNTS`.
- Screenshot capture now waits until the new-mode log contains at least one Skiko picture-frame marker, then retries until the color assertion passes. This avoids capturing the empty launch window before Compose has painted.
- Short report smoke generated at `/tmp/jbr-skia-report-picture-smoke3/report.md`.
- Report highlights:
  - old process samples: `samples=54 avg_cpu=89.98 max_cpu=445.30 avg_rss_kb=373639 max_rss_kb=1650880`
  - new process samples: `samples=61 avg_cpu=35.66 max_cpu=340.40 avg_rss_kb=805385 max_rss_kb=5528480`
  - fallback markers: old `0`, new `0`
  - Skiko picture frames: `frames=28 avg_bytes=215626133 max_bytes=415432066`
  - JBR picture replays: `frames=28 avg_bytes=215626133 max_bytes=415432066`
  - screenshot counts: `green=428351 blue=511362 purple=83318 yellow=26392`

Important caveats:

- The CPU/RSS numbers are not yet a fair before/after result. They include Gradle/sample startup and are from a short smoke run. Use them only to prove the report pipeline works.
- The memory signal is already concerning: new-mode max RSS reached roughly 5.5 GB in this smoke, consistent with the very large per-frame SKP payloads.
- The next performance checkpoint should run a smaller deterministic scene and separate startup/build time from steady-state repaint time.

Next native checkpoint:

- Create or wire a deterministic Jewel standalone sample scene for stable report runs.
- Add a no-text/no-Swing-child visual mode so screenshot assertions can be precise and SKP payload size can be measured without text/font noise.
- Decide whether the next macOS implementation step is:
  - cache/reuse JBR `GrDirectContext` to reduce native setup overhead, or
  - move away from full SKP serialization toward a narrower command/display-list bridge because payload size is already too high.

### Checkpoint 14: JBR DirectContext Cache Smoke

Status: completed as a PoC cache; invalidation and lifecycle cleanup still need production design.

- `JBRSkiaInterop.mm` now caches a JBR-owned Skia `GrDirectContext` per Java2D `MTLContext` pointer instead of recreating the context on every diagnostic/command/picture replay call.
- The cache is deliberately native-side and keyed by the destination Java2D Metal context. This keeps Skiko out of Metal queue/context ownership while reducing repeated Skia context setup work.
- Current limitations:
  - no explicit invalidation on display migration, surface loss, or JBR Metal context disposal
  - no cache size/resource pressure policy
  - no BUILD_ID-aware teardown because the local dylib is still a manual PoC artifact.
- CMP report harness screenshot capture now waits until at least one `SKIKO_JBR_INTEROP_PICTURE_FRAME` marker appears before capturing, and only treats screenshot capture as complete after the color assertion passes.
- Cache smoke report generated at `/tmp/jbr-skia-report-picture-cache-smoke/report.md`.
- Report highlights:
  - fallback markers: old `0`, new `0`
  - Skiko picture frames: `frames=32 avg_bytes=215626133 max_bytes=415432066`
  - JBR picture replays: `frames=32 avg_bytes=215626133 max_bytes=415432066`
  - screenshot counts: `green=428783 blue=511362 purple=83318 yellow=26824`
  - new max RSS remained high at roughly 5.7 GB, so the dominant performance/memory issue is still serialized picture payload size rather than direct-context setup.

Next native checkpoint:

- Stop investing heavily in full-SKP replay as the likely final path; use it only as the correctness oracle while prototyping a narrower command/display-list ABI.
- Keep the context cache, but add production invalidation only when the JBR build integration and lifecycle hooks are clearer.
- Build the deterministic Jewel sample/report target so the old/new CPU report is meaningful and less dominated by startup and huge demo content.

### Checkpoint 15: Deterministic No-Text Smoke Sample

Status: completed inside CMP samples; standalone Jewel sample is covered by checkpoint 16.

- Added a deterministic Compose/Swing smoke sample:
  - `androidx.compose.desktop.examples.jbrskiainterop.SimpleSmoke_jvmKt`
  - window title: `JbrSkiaSmokeWindow`
  - no text or Swing child components inside the Compose content
  - fixed green/blue/purple geometry for screenshot assertions.
- Added CMP sample tasks:
  - `:compose:desktop:desktop:desktop-samples:runSwingJbrSkiaSmoke`
  - `:compose:desktop:desktop:desktop-samples:runSwingJbrSkiaSmokeInterop`
- Updated the report harness so `OLD_TASK` and `NEW_TASK` can point at the deterministic sample instead of the large Swing example.
- Verification completed:
  - CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:desktop:desktop:desktop-samples:jvmJar`
  - deterministic report smoke at `/tmp/jbr-skia-report-simple-smoke3/report.md`
- Report highlights:
  - old process samples: `samples=69 avg_cpu=26.29 max_cpu=468.90 avg_rss_kb=257419 max_rss_kb=764752`
  - new process samples: `samples=67 avg_cpu=27.91 max_cpu=487.60 avg_rss_kb=257760 max_rss_kb=720560`
  - fallback markers: old `0`, new `0`
  - Skiko picture frames: `frames=1 avg_bytes=386 max_bytes=386`
  - JBR picture replays: `frames=1 avg_bytes=386 max_bytes=386`
  - screenshot counts: `green=525067 blue=532498 purple=92160 yellow=7000`
- Visual screenshot captured at `/tmp/jbr-skia-report-simple-smoke3/new-window.png`.

Key observation:

- The previous 415 MB SKP payload was content/sample dependent. A simple no-text vector scene serializes to 386 bytes and keeps old/new RSS in the same broad range for this short smoke. This makes the SKP bridge more useful as a correctness oracle, but text/images/interop-heavy scenes still need explicit payload and CPU tracking.

Next native checkpoint:

- Port the deterministic scene into the requested standalone Jewel sample once the Jewel project exists.
- Add a steady animation to produce enough frames for a real CPU comparison.
- Keep using the large Swing sample as a stress case for SKP payload growth.

### Checkpoint 16: Standalone Magic Jewel Swing Sample

Status: completed as a standalone validation harness; report automation is covered by checkpoint 17.

- Created a new standalone local project at `/Users/rock3r/src/magic-jewel` and initialized it as its own Git repository.
- Bootstrapped the Gradle/Jewel setup from `compose-pi` conventions while keeping the app intentionally small:
  - Kotlin `2.3.20`
  - Compose plugin/dependencies `1.10.3` for compilation and transitive baseline dependencies
  - Jewel `0.35.0-261.23567.138`
  - local Skiko override through `SKIKO_VERSION=0.0.0-SNAPSHOT` and `mavenLocal()`
- The app is hosted in a Swing `JFrame` with an `androidx.compose.ui.awt.ComposePanel`, not a Compose `Window`, so it exercises the SwingGraphics path.
- Added `runJbrSkiaInterop`, which:
  - enables `compose.swing.render.on.graphics=true` and `compose.swing.render.on.jbr.skia=true`
  - enables `skiko.jbr.interop.debugOverlay=true` and `skiko.jbr.interop.renderPicture=true`
  - accepts the same patched `java.desktop` / public API shim / native dylib JVM arguments used by the CMP smoke sample
  - prepends patched CMP output jars from `/Users/rock3r/src/cmp-jbr-skia-poc/out/compose-multiplatform-core` so the standalone app uses the local interop-enabled CMP classes.
- Added `scripts/run-jbr-skia.sh` as the runnable entry point for the patched local JBR/Skiko/CMP setup.
- Because CMP desktop runtime publication is currently a redirection/stub setup, the sample forces `androidx.compose.runtime:*` to `1.11.0-beta02`, matching CMP's redirection version and avoiding a runtime mismatch with patched UI classes such as `HostDefaultProvider`.
- Verification completed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin` in `/Users/rock3r/src/magic-jewel`
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/run-jbr-skia.sh` in `/Users/rock3r/src/magic-jewel`
- Runtime markers confirmed the standalone Jewel app is using the JBR picture replay path:
  - `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED abi=1 build=skia-interop-poc:1 ...`
  - paired `SKIKO_JBR_INTEROP_PICTURE_FRAME ... rendered=true` and `JBR_SKIA_INTEROP_PICTURE_FRAME ... rendered=true` markers
- Window-only screenshot captured at `/tmp/magic-jewel-jbr-skia-window.png`.

### Checkpoint 17: Magic Jewel Old/New Report Harness

Status: completed as an automated smoke/report harness.

- Added `/Users/rock3r/src/magic-jewel/scripts/jbr-skia-interop-report.sh`.
- The harness runs two modes:
  - old: `./gradlew --no-daemon run`
  - new: `./scripts/run-jbr-skia.sh`
- The report captures:
  - coarse `ps` CPU/RSS samples for each process tree
  - `SKIKO_JBR_INTEROP_FALLBACK` marker counts
  - `SKIKO_JBR_INTEROP_PICTURE_FRAME` marker counts and SKP payload sizes
  - `JBR_SKIA_INTEROP_PICTURE_FRAME` marker counts and replay payload sizes
  - window-only screenshot and deterministic color assertion status
- The harness reuses the CMP window-capture helper instead of whole-screen capture:
  - `/Users/rock3r/src/cmp-jbr-skia-poc/compose/desktop/desktop/samples/scripts/capture-macos-window.sh`
  - `/Users/rock3r/src/cmp-jbr-skia-poc/compose/desktop/desktop/samples/scripts/assert-jbr-skia-window-screenshot.sh`
- Short smoke verification completed with:
  - `OUT_DIR=/tmp/magic-jewel-jbr-skia-report-smoke DURATION_SECONDS=12 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-interop-report.sh`
- Smoke report highlights from `/tmp/magic-jewel-jbr-skia-report-smoke/report.md`:
  - old process samples: `samples=34 avg_cpu=32.58 max_cpu=201.30 avg_rss_kb=160221 max_rss_kb=495328`
  - new process samples: `samples=18 avg_cpu=1.50 max_cpu=21.10 avg_rss_kb=38720 max_rss_kb=123296`
  - fallback markers: old `0`, new `0`
  - Skiko picture frames: `frames=28 avg_bytes=823345 max_bytes=823350`
  - JBR picture replays: `frames=28 avg_bytes=823345 max_bytes=823350`
  - screenshot assertion: `passed`
  - screenshot counts: `green=716429 blue=1098423 purple=26400 yellow=12488`
  - screenshot: `/tmp/magic-jewel-jbr-skia-report-smoke/new-window.png`

Next checkpoint:

- Run a longer Magic Jewel report once the current patch stack is rebuilt cleanly, then treat that report as the first shareable before/after artifact.
- Keep the standalone classpath override documented until the local CMP artifacts are published/consumed through a cleaner Maven-local or composite-build path.
- Start narrowing the bridge from SKP replay toward a lower-overhead command/display-list path while preserving the same fallback markers and report schema.

### Checkpoint 18: Destination-Scoped Command Replay Smoke

Status: completed as a lower-overhead command-list smoke path.

- JBR command replay now mirrors the picture replay destination semantics:
  - wraps the full Java2D destination `MTLTexture`
  - clips to the current Compose/Swing paint destination rect
  - translates command replay into that destination rect
  - flushes through the JBR-owned Skia `GrDirectContext` on the Java2D Metal queue
- JBR now emits a structured command marker:
  - `JBR_SKIA_INTEROP_COMMAND_FRAME destinationX=<px> destinationY=<px> destinationWidth=<px> destinationHeight=<px> width=<px> height=<px> commands=<n> rendered=true`
- Skiko now builds command frames in device pixels, routes them through `ScopedSkiaCanvas.renderCommandFrame(...)`, and emits:
  - `SKIKO_JBR_INTEROP_COMMAND_FRAME width=<px> height=<px> commands=<n> rendered=<true|false>`
- Magic Jewel can switch renderers with:
  - `JBR_SKIA_RENDER_MODE=picture` for the default SKP replay correctness path
  - `JBR_SKIA_RENDER_MODE=commands` for the lower-overhead command-list smoke path
  - `JBR_SKIA_RENDER_MODE=diagnostic` for the JBR-owned diagnostic renderer
- Magic Jewel's report harness now records both picture and command marker summaries.
- Verification completed:
  - JBR patched-module compile for `JBRSkia` / `JBRSkiaService`
  - native dylib rebuild at `/tmp/jbr-skia-native/libjbrskiainterop.dylib`
  - `./gradlew :skiko:compileKotlinAwt :skiko:compileTestKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - `./gradlew :skiko:publishToMavenLocal`
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin` in `/Users/rock3r/src/magic-jewel`
  - `OUT_DIR=/tmp/magic-jewel-jbr-skia-command-smoke DURATION_SECONDS=10 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands ./scripts/jbr-skia-interop-report.sh`
- Command-mode smoke report highlights:
  - old process samples: `samples=29 avg_cpu=46.82 max_cpu=308.10 avg_rss_kb=178099 max_rss_kb=530912`
  - new process samples: `samples=24 avg_cpu=0.82 max_cpu=11.30 avg_rss_kb=40307 max_rss_kb=126064`
  - fallback markers: old `0`, new `0`
  - picture frames: `0`
  - Skiko command frames: `frames=318 avg_commands=549 max_commands=555`
  - JBR command frames: `frames=318 avg_commands=549 max_commands=555`
  - screenshot assertion: `passed`
  - screenshot counts: `dark=3222263 cyan=121037 yellow=25752 pink=18260`
  - screenshot: `/tmp/magic-jewel-jbr-skia-command-window.png`

### Checkpoint 19: Command-Mode Screenshot Oracle

Status: completed for the synthetic command renderer.

- Added `/Users/rock3r/src/magic-jewel/scripts/assert-jbr-skia-command-window-screenshot.sh`.
- The command oracle checks for the synthetic renderer's dark/cyan/yellow/pink palette and emits:
  - `JBR_SKIA_COMMAND_SCREENSHOT_COUNTS dark=<n> cyan=<n> yellow=<n> pink=<n>`
- Magic Jewel's report harness now selects the picture or command screenshot assertion based on `JBR_SKIA_RENDER_MODE`.
- Verification completed:
  - `scripts/assert-jbr-skia-command-window-screenshot.sh /tmp/magic-jewel-jbr-skia-command-window.png`
  - `OUT_DIR=/tmp/magic-jewel-jbr-skia-command-smoke DURATION_SECONDS=10 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands ./scripts/jbr-skia-interop-report.sh`

### Checkpoint 20: Mixed Swing/Compose Layering and FPS Markers

Status: completed as a Magic Jewel validation slice.

- Magic Jewel now contains:
  - always-on Compose animation driven by `rememberInfiniteTransition`
  - an animated Compose progress strip and moving vector stress lines
  - an embedded Swing island through `SwingPanel`
  - a Swing-side indeterminate `JProgressBar` and Swing `Timer`
  - a Compose overlay crossing the Swing island area for layering validation
- Magic Jewel emits app-level draw markers from the Compose canvas:
  - `MAGIC_JEWEL_COMPOSE_FRAME frame=<n>`
- The report harness now:
  - waits for the actual `com.magicjewel.MainKt` process before starting the measurement window
  - samples summed CPU/RSS per timestamp, rather than averaging per-process rows
  - reports old/new app draw FPS from `MAGIC_JEWEL_COMPOSE_FRAME`
  - reports Skiko/JBR replay FPS from structured interop markers
  - records that CPU/RSS numbers are noisy on a busy development machine
  - records that app draw FPS and interop marker FPS are draw/replay-call rates, not display-presented FPS, and may exceed monitor refresh when rendering is not vsync-throttled
- Added Magic Jewel's mixed screenshot oracle:
  - `/Users/rock3r/src/magic-jewel/scripts/assert-jbr-skia-mixed-window-screenshot.sh`
  - marker: `JBR_SKIA_MIXED_SCREENSHOT_COUNTS green=<n> blue=<n> purple=<n> yellow=<n> swingPanel=<n> overlayPurple=<n> orangeProgress=<n>`
- Verification completed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
  - `scripts/assert-jbr-skia-mixed-window-screenshot.sh /tmp/magic-jewel-mixed-picture-smoke/new-window.png`
  - `OUT_DIR=/tmp/magic-jewel-mixed-picture-smoke DURATION_SECONDS=12 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=picture ./scripts/jbr-skia-interop-report.sh`
- Mixed picture-mode smoke highlights:
  - old process samples: `samples=10 avg_cpu=99.06 max_cpu=140.60 avg_rss_kb=1047909 max_rss_kb=1129760`
  - new process samples: `samples=8 avg_cpu=109.49 max_cpu=119.10 avg_rss_kb=1219014 max_rss_kb=1554640`
  - app draw markers: old `frames=2168 fps=180.7`, new `frames=1878 fps=156.5`
  - Skiko picture frames: `frames=1877 fps=156.4 avg_bytes=827776 max_bytes=827778`
  - JBR picture replays: `frames=1877 fps=156.4 avg_bytes=827776 max_bytes=827778`
  - screenshot assertion: `passed`
  - screenshot counts: `green=525345 blue=1013838 purple=30057 yellow=37520 swingPanel=966940 overlayPurple=30269 orangeProgress=21406`

### Checkpoint 21: Visible Swing Animation Signal

Status: completed in Magic Jewel.

- Replaced the stock Swing `JProgressBar` with a custom Swing component because the LAF indeterminate progress animation was too subtle/static inside the validation scene.
- The custom Swing component paints a clearly moving orange block and repaints from a Swing `Timer`.
- Magic Jewel now emits Swing-side repaint markers:
  - `MAGIC_JEWEL_SWING_FRAME frame=<n>`
- The report harness now records `Swing Repaint Markers` for old and new modes.
- Verification completed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
  - `OUT_DIR=/tmp/magic-jewel-swing-progress-smoke DURATION_SECONDS=8 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=picture ./scripts/jbr-skia-interop-report.sh`
- Swing progress smoke highlights:
  - old Swing repaint markers: `frames=144 fps=18.0`
  - new Swing repaint markers: `frames=187 fps=23.4`
  - screenshot assertion: `passed`
  - screenshot: `/tmp/magic-jewel-swing-progress-smoke/new-window.png`

### Checkpoint 22: Magic-Like Command Vector Scene

Status: completed as a wider command-list stress slice; SKP picture replay remains the correctness/reference path for later benchmarks.

- Preserved the existing SKP replay path:
  - `JBR_SKIA_RENDER_MODE=picture` still selects serialized picture replay.
  - `JBR_SKIA_RENDER_MODE=commands` remains an additive command-list experiment.
- Expanded the temporary command ABI in JBR and the public Runtime API mirror with oval primitives:
  - `COMMAND_FILL_OVAL = 4`
  - `COMMAND_STROKE_OVAL = 5`
  - Java2D fallback rendering and native JBR-owned Skia rendering both support the new operations.
- Skiko command mode now paints a Magic-Jewel-like animated vector scene rather than the earlier toy dark/cyan card:
  - green and blue fields
  - purple block
  - yellow filled circle
  - orange moving progress strip
  - moving translucent line field
  - rotating spoke wheel with white dots and a stroked oval.
- Skiko command mode explicitly schedules repaint after successful JBR command rendering. This is required for the command experiment because it bypasses the real Compose `renderDelegate` and therefore does not receive Compose animation invalidation.
- Magic Jewel's command screenshot oracle now validates the Magic-like command palette and emits:
  - `JBR_SKIA_COMMAND_SCREENSHOT_COUNTS green=<n> blue=<n> purple=<n> yellow=<n> orange=<n> white=<n>`
- Verification completed:
  - JBR patched `java.desktop` compile of `JBRSkia.java` and `JBRSkiaService.java`
  - native `libjbrskiainterop.dylib` rebuild against the local Skiko Skia checkout
  - Skiko `./gradlew :skiko:compileKotlinAwt :skiko:compileTestKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - Skiko `./gradlew :skiko:publishToMavenLocal`
  - Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
  - Magic Jewel `bash -n scripts/assert-jbr-skia-command-window-screenshot.sh scripts/jbr-skia-interop-report.sh`
  - `OUT_DIR=/tmp/magic-jewel-command-vector-smoke DURATION_SECONDS=8 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands ./scripts/jbr-skia-interop-report.sh`
- Command vector smoke highlights, recorded on a noisy development machine:
  - report: `/tmp/magic-jewel-command-vector-smoke/report.md`
  - screenshot: `/tmp/magic-jewel-command-vector-smoke/new-window.png`
  - old process samples: `samples=7 avg_cpu=29.29 max_cpu=47.30 avg_rss_kb=927522 max_rss_kb=985184`
  - new process samples: `samples=5 avg_cpu=76.68 max_cpu=136.70 avg_rss_kb=387894 max_rss_kb=497344`
  - old Swing repaint markers: `frames=1 fps=0.1`
  - new Swing repaint markers: `frames=124 fps=15.5`
  - Skiko command frames: `frames=1741 fps=217.6 avg_commands=453 max_commands=464`
  - JBR command frames: `frames=1741 fps=217.6 avg_commands=453 max_commands=464`
  - screenshot assertion: `passed`
  - screenshot counts: `green=847887 blue=1397277 purple=98490 yellow=96680 orange=71506 white=126156`

### Checkpoint 23: CMP Sidecar Command Recorder

Status: completed as the first real Compose-to-JBR command replay slice; SKP picture replay remains available for correctness and benchmark runs.

- Preserved the serialized picture path for later quiet-machine benchmarks:
  - `JBR_SKIA_RENDER_MODE=picture` still selects SKP replay.
  - `JBR_SKIA_RENDER_MODE=commands` selects the lower-overhead command-list experiment.
- Added `JbrSkiaCommandRenderDelegate` in Skiko so a real renderer can provide command frames; if no delegate is present, Skiko still falls back to its synthetic command scene.
- CMP now records the normal `scene.render(...)` call into a real Skia `PictureRecorder` canvas for compatibility, while a sidecar `JbrSkiaCommandRecorder` mirrors the supported vector operations emitted through `SkiaBackedCanvas`.
- The current sidecar command subset covers:
  - solid-color `SrcOver` fill rects
  - solid-color round-rect fills approximated through the current command ABI
  - solid-color lines
  - solid-color ovals and circles
  - simple translate/scale state.
- Unsupported operations intentionally stay out of the command list for this slice:
  - text
  - images
  - shaders
  - paths
  - layer effects
  - rotate/skew/concat transforms.
- `SwingSkiaLayerComponent` forwards command-frame requests from Skiko into `ComposeSceneMediator`, so command mode now records real CMP scene drawing instead of the previous hand-authored Skiko vector scene.
- Magic Jewel's launch script now passes `skiko.jbr.interop.renderCommands`, `skiko.jbr.interop.renderPicture`, or `skiko.jbr.interop.renderDiagnostic` as JVM arguments, not only as Gradle properties, so render mode reliably reaches the app process.
- Magic Jewel's report harness now does a final window-only screenshot capture attempt before teardown when the new-mode replay marker exists, avoiding timing-dependent missed captures.
- Verification completed:
  - Skiko `./gradlew :skiko:compileKotlinAwt :skiko:publishToMavenLocal`
  - CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:ui:ui-graphics:compileKotlinDesktop :compose:ui:ui:compileKotlinDesktop`
  - CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:ui:ui:desktopJar`
  - Magic Jewel `OUT_DIR=/tmp/magic-jewel-compose-command-sidecar-final DURATION_SECONDS=6 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands ./scripts/jbr-skia-interop-report.sh`
- Command-sidecar smoke highlights, recorded on a noisy development machine:
  - report: `/tmp/magic-jewel-compose-command-sidecar-final/report.md`
  - screenshot: `/tmp/magic-jewel-compose-command-sidecar-final/new-window.png`
  - fallback markers: old `0`, new `0`
  - picture frames: `0`
  - Skiko command frames: `frames=724 fps=120.7 avg_commands=702 max_commands=702`
  - JBR command frames: `frames=724 fps=120.7 avg_commands=702 max_commands=702`
  - screenshot assertion: `passed`
  - screenshot counts: `green=565353 blue=1061166 purple=31358 yellow=38327 orange=22136 white=975211`

Important caveats:

- The command recorder is a deliberately tiny proof slice. It proves real CMP scene painting can produce JBR-owned Skia command replays, not that arbitrary Compose content is covered.
- Text still needs a dedicated strategy because font/typeface ownership remains one of the core ABI risks.
- The recorder currently mirrors draw operations while the normal Skia render still happens into an offscreen `PictureRecorder` canvas. The next slices should either widen the command coverage enough to skip the compatibility recording for supported scenes, or make the fallback boundary explicit per frame.
- The temporary integer command ABI is still a PoC transport. Production should move to a tightly versioned native ABI or a validated display-list payload.

### Checkpoint 24: Strict Command Coverage Telemetry

Status: completed for the Magic Jewel no-text/vector scene.

- Added per-frame CMP recorder telemetry:
  - `CMP_JBR_COMMAND_RECORDER_FRAME commands=<n> unsupported=<n> [reason=<count>...]`
  - The marker records unsupported draw-operation reasons while keeping command replay active.
- Magic Jewel's report now summarizes recorder coverage:
  - recorder frame count/FPS
  - average/max command count
  - unsupported frame count
  - average/max unsupported operation count
  - aggregated unsupported reasons.
- Magic Jewel's command-mode report now fails validation when strict command mode is expected and:
  - the CMP recorder emits no frames
  - unsupported recorder operations are present
  - Skiko or JBR command replay emits no frames
  - Skiko/JBR command frame counts diverge
  - SKP picture frames appear unexpectedly
  - the screenshot assertion does not pass.
- The first telemetry run identified only two blockers in the Magic Jewel command scene:
  - stroked round-rects, approximated through the current line-based command ABI
  - one `BlendMode.Clear` operation per frame.
- Added `COMMAND_CLEAR_RECT = 6` across the temporary command ABI:
  - JBR private API
  - public Runtime API mirror
  - Java2D fallback renderer
  - native JBR-owned Skia renderer
  - CMP sidecar command recorder.
- Command mode now enables `-Dcompose.jbr.skia.command.strict=true` in Magic Jewel. Under strict mode, CMP returns no command frame if unsupported operations remain, and Skiko falls back to SKP picture replay for that frame.
- Verification completed:
  - JBR patched `java.desktop` class rebuild into `/tmp/jbr-skia-run/desktop`
  - native `libjbrskiainterop.dylib` rebuild at `/tmp/jbr-skia-native/libjbrskiainterop.dylib`
  - Runtime API `bash tools/build.sh process`
  - Skiko `./gradlew :skiko:compileKotlinAwt :skiko:publishToMavenLocal`
  - CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:ui:ui-graphics:compileKotlinDesktop :compose:ui:ui:compileKotlinDesktop :compose:ui:ui:desktopJar`
  - Magic Jewel `OUT_DIR=/tmp/magic-jewel-command-strict-gate DURATION_SECONDS=6 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands ./scripts/jbr-skia-interop-report.sh`
- Strict command-mode report highlights, recorded on a noisy development machine:
  - report: `/tmp/magic-jewel-command-strict-gate/report.md`
  - screenshot: `/tmp/magic-jewel-command-strict-gate/new-window.png`
  - fallback markers: old `0`, new `0`
  - picture frames: `0`
  - CMP command recorder: `frames=1003 fps=167.2 avg_commands=791 max_commands=791 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
  - Skiko command frames: `frames=1003 fps=167.2 avg_commands=791 max_commands=791`
  - JBR command frames: `frames=1003 fps=167.2 avg_commands=791 max_commands=791`
  - screenshot assertion: `passed`
  - screenshot counts: `green=570602 blue=1062179 purple=31321 yellow=35877 orange=18899 white=969726`
- Follow-up harness validation run:
  - report: `/tmp/magic-jewel-command-strict-validated/report.md`
  - validation status: `passed`
  - CMP command recorder: `frames=1297 fps=216.2 avg_commands=791 max_commands=791 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
  - Skiko/JBR command frames: `1297` / `1297`
  - picture frames: `0`
  - screenshot assertion: `passed`
- The recorder now treats previously invisible clip/path/image/arc/points/vertices calls as unsupported for strict-mode accounting. A clip-aware validation run still passed for the current Magic Jewel vector scene:
  - report: `/tmp/magic-jewel-command-strict-clip-aware/report.md`
  - validation status: `passed`
  - CMP command recorder: `frames=1423 fps=237.2 avg_commands=763 max_commands=791 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
  - Skiko/JBR command frames: `1423` / `1423`
  - picture frames: `0`
  - screenshot assertion: `passed`

Next checkpoint:

- Widen the sidecar recorder beyond the Magic Jewel no-text/vector scene and keep the strict per-frame fallback decision: command replay when every operation is supported, SKP replay when unsupported operations appear.
- Keep text/images on the SKP path until the JBR-owned font/typeface story is implemented.
- Preserve `JBR_SKIA_RENDER_MODE=picture` and SKP byte/frame markers for meaningful benchmark runs when the machine is quieter.

### Checkpoint 25: Text-Aware Strict Command Fallback

Status: completed as a strict compatibility validation slice.

- CMP now marks Skiko paragraph painting as an unsupported command-recorder operation:
  - `SkiaParagraph.paint(...)` calls `JbrSkiaCommandRecorder.markUnsupportedDraw("text")` before invoking `paragraph.paint(canvas.skiaCanvas, ...)`.
  - This closes the previous blind spot where desktop text bypassed `SkiaBackedCanvas` and therefore could silently pass strict command validation.
- Magic Jewel now supports a command-safe visual mode:
  - `MAGIC_JEWEL_COMPOSE_TEXT=false` replaces Compose text labels with simple supported color bars.
  - The report harness defaults to that mode for strict command validation so the command subset can still be regression-tested independently.
- Magic Jewel also supports an expected text-fallback validation mode:
  - `MAGIC_JEWEL_COMPOSE_TEXT=true EXPECT_COMMAND_FALLBACK=true JBR_SKIA_RENDER_MODE=commands`
  - The report requires CMP recorder frames with `text=<n>`, zero Skiko/JBR command frames, positive SKP picture frames, and a passing mixed-content screenshot assertion.
- Verification completed:
  - CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:ui:ui-graphics:compileKotlinDesktop :compose:ui:ui-text:compileKotlinDesktop`
  - CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`
  - Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
  - Magic Jewel `./gradlew assemble`
- Strict command-safe report:
  - command: `OUT_DIR=/tmp/magic-jewel-command-strict-text-aware DURATION_SECONDS=5 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands ./scripts/jbr-skia-interop-report.sh`
  - report: `/tmp/magic-jewel-command-strict-text-aware/report.md`
  - validation status: `passed`
  - `MAGIC_JEWEL_COMPOSE_TEXT: false`
  - CMP command recorder: `frames=1208 fps=241.6 avg_commands=847 max_commands=847 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
  - Skiko/JBR command frames: `1208` / `1208`
  - picture frames: `0`
  - screenshot assertion: `passed`
- Text fallback report:
  - command: `OUT_DIR=/tmp/magic-jewel-command-text-fallback DURATION_SECONDS=5 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_TEXT=true EXPECT_COMMAND_FALLBACK=true ./scripts/jbr-skia-interop-report.sh`
  - report: `/tmp/magic-jewel-command-text-fallback/report.md`
  - validation status: `passed`
  - CMP command recorder: `frames=709 fps=141.8 avg_commands=791 max_commands=791 unsupported_frames=709 avg_unsupported=8.0 max_unsupported=8 reasons=text:5672`
  - Skiko/JBR command frames: `0` / `0`
  - SKP picture frames: `709` / `709`
  - screenshot assertion: `passed`

Next checkpoint:

- Add the first text-capable command/display-list slice only after the font/typeface ownership story is routed through JBR-owned Skia. Until then, strict command mode must fallback on text.
- Add image unsupported validation using the same expected-fallback pattern.
- Keep the SKP picture path and report schema intact for quiet-machine benchmark runs.

### Checkpoint 26: Image-Aware Strict Command Fallback

Status: completed in Magic Jewel; no CMP recorder code change was required.

- Magic Jewel now has an opt-in Compose image probe:
  - `MAGIC_JEWEL_COMPOSE_IMAGE=true`
  - The probe creates a tiny generated `ImageBitmap` and draws it from the Compose canvas.
  - It is disabled by default so the strict command-safe report remains focused on the currently supported vector subset.
- Magic Jewel's expected-fallback report mode is now parameterized by unsupported reason:
  - `EXPECT_COMMAND_FALLBACK_REASON=text` for text fallback validation.
  - `EXPECT_COMMAND_FALLBACK_REASON=image` for image fallback validation.
- The existing CMP recorder already marks image draws as unsupported through the `SkiaBackedCanvas.drawImageRect` path, so the new sample/report mode validates that coverage rather than adding a new recorder hook.
- Verification completed:
  - Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
  - Magic Jewel `bash -n scripts/jbr-skia-interop-report.sh`
  - Magic Jewel `git diff --check`
  - Magic Jewel `OUT_DIR=/tmp/magic-jewel-command-image-fallback DURATION_SECONDS=5 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_TEXT=false MAGIC_JEWEL_COMPOSE_IMAGE=true EXPECT_COMMAND_FALLBACK=true EXPECT_COMMAND_FALLBACK_REASON=image ./scripts/jbr-skia-interop-report.sh`
- Image fallback report:
  - report: `/tmp/magic-jewel-command-image-fallback/report.md`
  - validation status: `passed`
  - `MAGIC_JEWEL_COMPOSE_TEXT: false`
  - `MAGIC_JEWEL_COMPOSE_IMAGE: true`
  - CMP command recorder: `frames=1296 fps=259.2 avg_commands=847 max_commands=847 unsupported_frames=1296 avg_unsupported=1.0 max_unsupported=1 reasons=image:1296`
  - Skiko/JBR command frames: `0` / `0`
  - SKP picture frames: `1296` / `1296`
  - screenshot assertion: `passed`

Next checkpoint:

- Decide whether to add a minimal native command for image blits or keep image content on SKP until the final ABI is less toy-like.
- Start the same strict-fallback validation for transform/saveLayer-heavy content.
- Keep working toward JBR-owned text/font/typeface creation before enabling command/direct text.

### Checkpoint 27: Transform And SaveLayer Strict Fallback

Status: completed in Magic Jewel; existing CMP recorder hooks covered both probes.

- Magic Jewel now has two more opt-in unsupported-operation probes:
  - `MAGIC_JEWEL_COMPOSE_TRANSFORM=true` draws a rotated rect through Compose.
  - `MAGIC_JEWEL_COMPOSE_SAVELAYER=true` draws a small `Canvas.saveLayer(...)` probe.
  - Both are disabled by default so the command-safe strict report remains stable.
- Magic Jewel's report captures the probe toggles in `report.md`, making fallback validation runs self-describing.
- Verification completed:
  - Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
  - Magic Jewel `bash -n scripts/jbr-skia-interop-report.sh`
  - Magic Jewel `git diff --check`
  - Transform fallback command: `OUT_DIR=/tmp/magic-jewel-command-transform-fallback DURATION_SECONDS=5 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_TEXT=false MAGIC_JEWEL_COMPOSE_TRANSFORM=true EXPECT_COMMAND_FALLBACK=true EXPECT_COMMAND_FALLBACK_REASON=transform ./scripts/jbr-skia-interop-report.sh`
  - SaveLayer fallback command: `OUT_DIR=/tmp/magic-jewel-command-savelayer-fallback DURATION_SECONDS=5 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_TEXT=false MAGIC_JEWEL_COMPOSE_SAVELAYER=true EXPECT_COMMAND_FALLBACK=true EXPECT_COMMAND_FALLBACK_REASON=saveLayer ./scripts/jbr-skia-interop-report.sh`
  - Command-safe default command: `OUT_DIR=/tmp/magic-jewel-command-strict-after-probes DURATION_SECONDS=5 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands ./scripts/jbr-skia-interop-report.sh`
- Transform fallback report:
  - report: `/tmp/magic-jewel-command-transform-fallback/report.md`
  - validation status: `passed`
  - CMP command recorder: `frames=1005 fps=201.0 avg_commands=847 max_commands=847 unsupported_frames=1005 avg_unsupported=2.0 max_unsupported=2 reasons=unsupportedScope:1005,transform:1005`
  - Skiko/JBR command frames: `0` / `0`
  - SKP picture frames: `1005` / `1005`
  - screenshot assertion: `passed`
- SaveLayer fallback report:
  - report: `/tmp/magic-jewel-command-savelayer-fallback/report.md`
  - validation status: `passed`
  - CMP command recorder: `frames=887 fps=177.4 avg_commands=847 max_commands=847 unsupported_frames=887 avg_unsupported=2.0 max_unsupported=2 reasons=unsupportedScope:887,saveLayer:887`
  - Skiko/JBR command frames: `0` / `0`
  - SKP picture frames: Skiko `886`, JBR `886`
  - screenshot assertion: `passed`
- Command-safe default report after adding probes:
  - report: `/tmp/magic-jewel-command-strict-after-probes/report.md`
  - validation status: `passed`
  - all probe toggles: `false`
  - CMP command recorder: `frames=1098 fps=219.6 avg_commands=847 max_commands=847 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
  - Skiko/JBR command frames: `1098` / `1098`
  - picture frames: `0`
  - screenshot assertion: `passed`

Next checkpoint:

- Add one or two focused unit tests around Magic Jewel report validation parsing so expected-fallback failures are caught without launching the app.
- Then choose the next positive command-coverage expansion: either native image blit, clip rect support, or a small save/restore/transform stack in the command ABI.

### Checkpoint 28: Report Validation Unit Harness

Status: completed in Magic Jewel.

- Magic Jewel's report script now supports:
  - `scripts/jbr-skia-interop-report.sh --validate-only`
  - This mode skips app launch/report generation and runs the existing `validate_report` logic against files already present in `OUT_DIR`.
- Added `scripts/test-jbr-skia-report-validation.sh` to exercise validation without launching the app:
  - strict command success with CMP/Skiko/JBR command markers
  - expected image fallback success with CMP `image=<n>` plus Skiko/JBR picture markers
  - expected fallback failure when the requested reason is missing.
- Verification completed:
  - Magic Jewel `bash -n scripts/jbr-skia-interop-report.sh scripts/test-jbr-skia-report-validation.sh`
  - Magic Jewel `scripts/test-jbr-skia-report-validation.sh`
  - output: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`

Next checkpoint:

- Choose the next positive command-coverage expansion: either native image blit, clip rect support, or a small save/restore/transform stack in the command ABI.
- Keep expected-fallback tests for text/image/transform/saveLayer as guards while expanding the command subset.

### Checkpoint 29: ClipRect Command Support And ABI 2

Status: completed as the first stateful command-list expansion.

- Bumped the PoC command ABI to `ABI_ID = 2` because the command layout changed.
- Added three state commands to the temporary integer command ABI:
  - `COMMAND_SAVE = 7`
  - `COMMAND_RESTORE = 8`
  - `COMMAND_CLIP_RECT = 9`
- JBR now replays save/restore/clip in both command backends:
  - Java2D fallback replay uses cloned `Graphics2D` state for save/restore and `clipRect(...)` for intersect clips.
  - Native JBR-owned Skia replay calls `SkCanvas::save`, `SkCanvas::restore`, and `SkCanvas::clipRect(..., kIntersect, true)`.
- CMP now emits save/restore commands and encodes intersecting clip rects instead of treating every `clipRect` as unsupported.
- Skiko now expects ABI 2 and has a PoC-only internal provider fallback:
  - It still reads `ABI_ID` / `BUILD_ID` reflectively before acquiring anything.
  - If `com.jetbrains.JBR.getJBRSkia()` returns null under the patch-module setup, it tries `com.jetbrains.desktop.JBRSkiaService` directly.
  - This bridges the local shim/provider binding gap and should be removed once the public JBR API jar and JBR provider are integrated normally.
- Magic Jewel now has a positive clip probe:
  - `MAGIC_JEWEL_COMPOSE_CLIP=true`
  - The probe is disabled by default, and when enabled it must still pass strict command validation without SKP fallback.
- Verification completed:
  - JBR patched class rebuild into `/tmp/jbr-skia-run/desktop`
  - JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`
  - Runtime API `bash tools/build.sh process`
  - Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh
  - Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest :skiko:publishToMavenLocal`
  - CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`
  - Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
  - Magic Jewel `OUT_DIR=/tmp/magic-jewel-command-clip-supported3 DURATION_SECONDS=5 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_TEXT=false MAGIC_JEWEL_COMPOSE_CLIP=true ./scripts/jbr-skia-interop-report.sh`
- Clip-positive strict report:
  - report: `/tmp/magic-jewel-command-clip-supported3/report.md`
  - validation status: `passed`
  - CMP command recorder: `frames=644 fps=128.8 avg_commands=907 max_commands=907 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
  - Skiko/JBR command frames: `643` / `643`
  - picture frames: `0`
  - screenshot assertion: `passed`

Next checkpoint:

- Remove the PoC internal-provider fallback once the public API shim binds the patched service correctly, or document it as local-only launch plumbing.
- Add focused validation for clip-out / non-intersect clips to ensure they still fallback instead of replaying incorrectly.
- Decide whether to add native image blit support or keep image content on SKP until the final ABI shape is clearer.

### Checkpoint 30: Clip-Out Strict Fallback

Status: completed in Magic Jewel.

- Added a Magic Jewel non-intersect clip probe:
  - `MAGIC_JEWEL_COMPOSE_CLIP_OUT=true`
  - The probe uses `ClipOp.Difference`, which the command ABI must not replay as an intersect clip.
- The CMP recorder already rejects non-intersect clip rects as:
  - `clipRect_Difference`
  - It also marks the scope unsupported, so subsequent draws in that scope record `unsupportedScope`.
- Magic Jewel report validation now records `MAGIC_JEWEL_COMPOSE_CLIP_OUT` in `report.md` and can require `EXPECT_COMMAND_FALLBACK_REASON=clipRect_Difference`.
- Verification completed:
  - Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
  - Magic Jewel `bash -n scripts/jbr-skia-interop-report.sh`
  - Magic Jewel `git diff --check`
  - Magic Jewel `OUT_DIR=/tmp/magic-jewel-command-clip-out-fallback DURATION_SECONDS=5 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_TEXT=false MAGIC_JEWEL_COMPOSE_CLIP_OUT=true EXPECT_COMMAND_FALLBACK=true EXPECT_COMMAND_FALLBACK_REASON=clipRect_Difference ./scripts/jbr-skia-interop-report.sh`
- Clip-out fallback report:
  - report: `/tmp/magic-jewel-command-clip-out-fallback/report.md`
  - validation status: `passed`
  - CMP command recorder: `frames=1436 fps=287.2 avg_commands=895 max_commands=895 unsupported_frames=1436 avg_unsupported=2.0 max_unsupported=2 reasons=unsupportedScope:1436,clipRect_Difference:1436`
  - Skiko/JBR command frames: `0` / `0`
  - SKP picture frames: `1436` / `1436`
  - screenshot assertion: `passed`

Next checkpoint:

- Decide image strategy:
  - either add a minimal `COMMAND_DRAW_IMAGE_RECT` with tightly bounded payload semantics,
  - or explicitly keep image content on SKP until the native ABI replaces the toy int-array transport.
- Keep the clip-out fallback report as a guard when expanding clipping support.

### Checkpoint 31: Framed Command Stream And ABI 3

Status: completed as the first structured framing step for the temporary int-array ABI.

- Start replacing the raw temporary integer command list with a small framed command stream.
- Bump the PoC command ABI to `ABI_ID = 3` because command payloads now require a header.
- The command stream header is:
  - `COMMAND_STREAM_MAGIC = 1246972723` (`JSK3`)
  - `ABI_ID`
  - `COMMAND_STREAM_FLAGS_NONE = 0`
  - `payloadLength`, the number of integers after the header.
- JBR validates the header before replaying either backend:
  - Java2D fallback replay rejects missing headers, wrong magic, wrong ABI, unsupported flags, negative payload length, or mismatched payload length.
  - Native JBR-owned Skia replay applies the same validation before touching the destination `SkCanvas`.
- CMP emits the header around every strict command payload and logs the full stream length in `CMP_JBR_COMMAND_RECORDER_FRAME commands=...`.
- Skiko synthetic command mode emits the same header, and Skiko expects ABI 3 during discovery.
- This is still a transitional int-array ABI, not the final native struct ABI. The value is fail-closed framing and a concrete place for flags/versioning while command coverage continues to grow.

Verification completed:

- JBR patched class rebuild into `/tmp/jbr-skia-run/desktop`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.
- Magic Jewel `OUT_DIR=/tmp/magic-jewel-command-header-smoke DURATION_SECONDS=5 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_TEXT=false MAGIC_JEWEL_COMPOSE_CLIP=true ./scripts/jbr-skia-interop-report.sh`.

Framed-stream smoke report:

- report: `/tmp/magic-jewel-command-header-smoke/report.md`
- validation status: `passed`
- fallback markers: `0`
- picture frames: `0`
- CMP command recorder: `frames=1160 fps=232.0 avg_commands=881 max_commands=881 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `1160` / `1160`
- screenshot assertion: `passed`

Next checkpoint:

- Decide whether to add a focused header-negative test fixture around JBR command replay, or move straight to the next ABI structuring step:
  - named command payload metadata,
  - backend capability flags,
  - explicit coordinate-space units,
  - or a native memory block layout that mirrors the eventual C ABI.

### Checkpoint 32: Command Capabilities And Invalid-Stream Fallback

Status: completed as the first capability-negotiated ABI slice.

- Bumped the PoC command ABI to `ABI_ID = 4` because Skiko now requires command capability discovery after the ABI/build gate.
- Added service-level command capability bits to JBR and the public Runtime API mirror:
  - clear
  - fill rect
  - stroke line
  - fill oval
  - stroke oval
  - clear rect
  - save/restore
  - clip rect
  - Swing user-space coordinates.
- `JBRSkiaService.getCommandCapabilities()` returns the supported bitset.
- Skiko now rejects otherwise-compatible services that do not expose all currently required command capabilities and emits:
  - `SKIKO_JBR_INTEROP_FALLBACK reason=command-capability-mismatch`
- JBR now has a focused command-stream validation test hook exercised from `JBRSkiaApiTest`:
  - missing header
  - wrong magic
  - wrong ABI
  - unsupported flags
  - negative payload length
  - truncated payload
  - extra payload.
- Skiko/Magic Jewel now have a deliberate corrupt-stream probe:
  - `MAGIC_JEWEL_CORRUPT_COMMAND_STREAM=true`
  - Skiko flips command-stream flags to an unsupported value.
  - JBR rejects the frame before native replay.
  - Skiko emits `SKIKO_JBR_INTEROP_FALLBACK reason=command-stream-invalid` and falls back to the old Swing renderer.

Verification completed:

- JBR patched class rebuild into `/tmp/jbr-skia-run/desktop`.
- JBR `JBRSkiaApiTest` compiled and passed against the patched module.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`.
- Magic Jewel `bash -n scripts/run-jbr-skia.sh scripts/jbr-skia-interop-report.sh scripts/test-jbr-skia-report-validation.sh`.
- Magic Jewel `scripts/test-jbr-skia-report-validation.sh`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.

Capability-positive smoke report:

- report: `/tmp/magic-jewel-command-caps-smoke/report.md`
- validation status: `passed`
- fallback markers: `0`
- picture frames: `0`
- CMP command recorder: `frames=860 fps=172.0 avg_commands=911 max_commands=911 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `859` / `859`
- screenshot assertion: `passed`

Invalid-stream fallback report:

- report: `/tmp/magic-jewel-command-invalid-stream/report.md`
- validation status: `passed`
- fallback markers: `1`
- expected fallback reason: `command-stream-invalid`
- CMP command recorder: `frames=744 fps=148.8 avg_commands=897 max_commands=897 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `743` / `0`
- screenshot assertion: `passed` on the old Swing fallback renderer.

Next checkpoint:

- Push capability awareness down into the CMP command recorder so it can omit optional commands when a future JBR advertises a smaller command set.
- Start replacing the int-array payload with a native memory block layout:
  - fixed stream header struct
  - per-command opcode + byte length
  - explicit coordinate-space enum
  - reserved extension fields for images/text.

### Checkpoint 33: Command Record Lengths And ABI 5

Status: completed as the next ABI-structure slice for command replay.

- Bumped the PoC command ABI to `ABI_ID = 5`.
- Kept the outer command stream header from ABI 3/4:
  - `COMMAND_STREAM_MAGIC`
  - `ABI_ID`
  - flags
  - payload length.
- Changed each command payload record from raw `[op, args...]` to:
  - `[op, recordLength, args...]`
  - `recordLength` is counted in integers and includes the opcode and length fields.
- JBR validates command record boundaries before replay:
  - record length must be at least `2`
  - record end must stay inside the declared payload
  - every supported opcode must consume exactly its record
  - malformed records are rejected before native replay.
- JBR native Skia replay now applies the same exact record-boundary checks as the Java fallback replay.
- JBR `JBRSkiaApiTest` now includes a negative case for a wrong per-command record length.
- Skiko and CMP command emitters now use helper functions that prepend record lengths, so future optional payload extensions can be skipped or rejected predictably.
- The ABI remains a temporary int-array bridge. This slice makes the payload self-describing enough to map cleanly to the eventual native memory-block layout.

Verification completed:

- JBR patched class rebuild into `/tmp/jbr-skia-run/desktop`.
- JBR `JBRSkiaApiTest` compiled and passed against the patched module.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.

Record-length positive smoke report:

- report: `/tmp/magic-jewel-command-record-length-smoke/report.md`
- validation status: `passed`
- fallback markers: `0`
- picture frames: `0`
- CMP command recorder: `frames=1392 fps=278.4 avg_commands=1085 max_commands=1085 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `1391` / `1391`
- screenshot assertion: `passed`

Record-length invalid-stream fallback report:

- report: `/tmp/magic-jewel-command-record-length-invalid/report.md`
- validation status: `passed`
- fallback markers: `1`
- expected fallback reason: `command-stream-invalid`
- CMP command recorder: `frames=612 fps=122.4 avg_commands=1067 max_commands=1067 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `612` / `0`
- screenshot assertion: `passed` on the old Swing fallback renderer.

Next checkpoint:

- Move the command payload one step closer to the native ABI:
  - byte-sized or fixed-width opcodes
  - byte lengths instead of integer counts
  - explicit record alignment
  - reserved per-record extension flags.

### Checkpoint 34: Byte-Length Command Records And ABI 6

Status: completed as the first native-block-shaped command record slice.

- Bumped the PoC command ABI to `ABI_ID = 6`.
- Kept the temporary `int[]` transport, but changed every command record header to:
  - `[op, recordByteLength, recordFlags, args...]`
  - `recordByteLength` is the byte length of the whole aligned record, including the three-field header.
  - `recordFlags` must be `COMMAND_RECORD_FLAGS_NONE`.
- Added public/private constants for the record-level contract:
  - `COMMAND_RECORD_HEADER_SIZE_BYTES = 12`
  - `COMMAND_RECORD_FLAGS_NONE = 0`
- JBR Java fallback replay and native Skia replay now both reject:
  - record byte lengths smaller than the record header
  - byte lengths that are not aligned to `sizeof(jint)` / `Integer.BYTES`
  - unsupported record flags
  - records whose byte length does not match the opcode-specific payload size.
- Runtime API docs now describe byte lengths and record flags instead of integer record lengths.
- CMP and Skiko command emitters now compute byte lengths with `(args.size + 3) * Int.SIZE_BYTES`.
- JBR `JBRSkiaApiTest` now validates a good byte-length record and negative cases for wrong byte length and unsupported record flags.

Verification completed:

- JBR patched class rebuild into `/tmp/jbr-skia-run/desktop`.
- JBR `JBRSkiaApiTest` compiled and passed against the patched module.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.

Byte-length positive smoke report:

- report: `/tmp/magic-jewel-command-record-byte-length-smoke/report.md`
- validation status: `passed`
- fallback markers: `0`
- picture frames: `0`
- CMP command recorder: `frames=1146 fps=229.2 avg_commands=1259 max_commands=1259 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `1146` / `1146`
- screenshot assertion: `passed`

Byte-length invalid-stream fallback report:

- report: `/tmp/magic-jewel-command-record-byte-length-invalid/report.md`
- validation status: `passed`
- fallback markers: `1`
- expected fallback reason: `command-stream-invalid`
- CMP command recorder: `frames=769 fps=153.8 avg_commands=1237 max_commands=1237 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `769` / `0`
- screenshot assertion: `passed` on the old Swing fallback renderer.

Next checkpoint:

- Start extracting the temporary `int[]` layout behind explicit encode/decode helpers so the next change can swap the carrier for a native memory block with less churn.
- Add record-level coordinate-space and paint-format metadata before expanding beyond the current solid-color vector subset.

### Checkpoint 35: Command Header Metadata And ABI 7

Status: completed as the stream-level metadata slice for the command ABI.

- Bumped the PoC command ABI to `ABI_ID = 7`.
- Extended the temporary stream header from four integers to six:
  - `COMMAND_STREAM_MAGIC`
  - `ABI_ID`
  - stream flags
  - payload length
  - coordinate-space id
  - paint-format id.
- Added explicit public/private header metadata constants:
  - `COMMAND_COORDINATE_SPACE_SWING_USER = 1`
  - `COMMAND_PAINT_FORMAT_SOLID_ARGB = 1`
- JBR Java fallback replay and native Skia replay now reject streams whose coordinate-space or paint-format metadata does not match the currently supported subset.
- Runtime API docs now describe the six-field header, so consumers do not have to infer coordinate or paint semantics from capability bits alone.
- CMP and Skiko emitters now populate the expanded header for both real and synthetic command streams.
- JBR `JBRSkiaApiTest` now includes negative cases for unsupported coordinate space and unsupported paint format.

Verification completed:

- JBR patched class rebuild into `/tmp/jbr-skia-run/desktop`.
- JBR `JBRSkiaApiTest` compiled and passed against the patched module.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.

Header-metadata positive smoke report:

- report: `/tmp/magic-jewel-command-header-metadata-smoke/report.md`
- validation status: `passed`
- fallback markers: `0`
- picture frames: `0`
- CMP command recorder: `frames=834 fps=166.8 avg_commands=1261 max_commands=1261 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `834` / `834`
- screenshot assertion: `passed`

Header-metadata invalid-stream fallback report:

- report: `/tmp/magic-jewel-command-header-metadata-invalid/report.md`
- validation status: `passed`
- fallback markers: `1`
- expected fallback reason: `command-stream-invalid`
- CMP command recorder: `frames=662 fps=132.4 avg_commands=1239 max_commands=1239 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `662` / `0`
- screenshot assertion: `passed` on the old Swing fallback renderer.

Next checkpoint:

- Extract shared stream writer/reader helpers in CMP/Skiko/JBR so the temporary `int[]` representation is isolated behind one encoder/decoder surface per repo.
- Then replace the `int[]` carrier with a direct native byte buffer or memory segment without changing command-call sites again.

### Checkpoint 36: Command Stream Writer/Reader Helpers

Status: completed as a behavior-preserving refactor on top of ABI 7.

- Kept the command ABI at `ABI_ID = 7`; no wire-format changes.
- CMP now routes command emission through a `CommandStreamWriter` helper:
  - owns payload accumulation
  - writes the six-field stream header
  - writes byte-length command records with record flags
  - reports total stream size for telemetry.
- Skiko synthetic command mode now uses its own `CommandStreamWriter` helper instead of building the header and record fields inline.
- JBR Java command validation/replay now uses a shared `readCommandRecord(...)` helper and `CommandRecord` record so the Java fallback path and validation hook agree on record parsing.
- This deliberately leaves the native parser unchanged because it already has a narrow byte-length helper and will be replaced by the native memory-block reader in the next ABI-carrier slice.

Verification completed:

- JBR patched class rebuild into `/tmp/jbr-skia-run/desktop`.
- JBR `JBRSkiaApiTest` compiled and passed against the patched module.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.

Writer-helper smoke report:

- report: `/tmp/magic-jewel-command-writer-helper-smoke/report.md`
- validation status: `passed`
- fallback markers: `0`
- picture frames: `0`
- CMP command recorder: `frames=913 fps=182.6 avg_commands=1261 max_commands=1261 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `912` / `912`
- screenshot assertion: `passed`

Next checkpoint:

- Introduce an explicit command-stream byte-buffer carrier beside the existing `int[]` method, initially backed by equivalent data and the same parser semantics.
- Use the helper boundary added here so call sites continue to emit commands without knowing the carrier shape.

### Checkpoint 37: Command Byte-Buffer Carrier

Status: completed as the first carrier-boundary slice beside the ABI 7 `int[]` stream.

- Kept the command ABI at `ABI_ID = 7`; the stream layout and command records are unchanged.
- Added `ScopedSkiaCanvas.renderCommandBufferFrame(width, height, frameTimeNanos, byte[] commands)` to the JBR private API and public Runtime API mirror.
- The byte-buffer carrier is little-endian 32-bit words containing the same ABI 7 stream used by the existing `int[]` method.
- JBR currently decodes the byte carrier back into the shared command parser/replayer. This is intentional for the slice:
  - validates API shape and fallback behavior first
  - keeps native Metal/Skia replay unchanged
  - creates the call boundary needed to replace the decoder with a direct native memory-block reader later.
- Skiko command mode now calls `renderCommandBufferFrame(...)` and encodes the command stream to little-endian bytes after the existing corruption-test hook.
- Skiko tests exercise both the legacy `int[]` method and the new byte-buffer method on the fake scoped canvas.
- JBR tests exercise a valid byte-buffer command frame and reject an unaligned byte buffer.

Verification completed:

- JBR patched class rebuild into `/tmp/jbr-skia-run/desktop`.
- JBR `JBRSkiaApiTest` compiled and passed against the patched module.
- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.

Byte-carrier positive smoke report:

- report: `/tmp/magic-jewel-command-byte-carrier-smoke-2/report.md`
- validation status: `passed`
- fallback markers: `0`
- picture frames: `0`
- CMP command recorder: `frames=1215 fps=243.0 avg_commands=1261 max_commands=1261 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `1214` / `1214`
- screenshot assertion: `passed`

Byte-carrier invalid-stream fallback report:

- report: `/tmp/magic-jewel-command-byte-carrier-invalid/report.md`
- validation status: `passed`
- fallback markers: `1`
- expected fallback reason: `command-stream-invalid`
- CMP command recorder: `frames=748 fps=149.6 avg_commands=1239 max_commands=1239 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `748` / `0`
- screenshot assertion: `passed` on the old Swing fallback renderer.

Next checkpoint:

- Move byte-buffer decoding down to the JBR native bridge so the Java service no longer reconstructs an `int[]` before native replay.
- Once native byte parsing is in place, replace the `byte[]` with a direct buffer or memory segment and keep the Java byte-array method as a test-only adapter.

### Checkpoint 38: Native Command Byte Parsing

Status: completed as the native-side byte-carrier parser slice.

- Kept the command ABI at `ABI_ID = 7`; no stream-layout changes.
- JBR Java now attempts `nativeRenderCommandBufferFrame(...)` before using the Java fallback renderer.
- The Java fallback path still decodes the byte carrier for software/non-native surfaces, preserving testability.
- JBR native bridge now accepts the byte-carrier method directly:
  - validates the byte length is aligned to 32-bit words
  - decodes little-endian words in native code
  - reuses the existing command-list parser/replayer
  - logs the same `JBR_SKIA_INTEROP_COMMAND_FRAME` marker as the int-array native path.
- This removes Java `int[]` reconstruction from the native Metal/Skia hot path while still leaving one final copy into a native `std::vector<jint>`.

Verification completed:

- JBR patched class rebuild into `/tmp/jbr-skia-run/desktop`.
- JBR `JBRSkiaApiTest` compiled and passed against the patched module.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.

Native-byte positive smoke report:

- report: `/tmp/magic-jewel-command-native-byte-smoke/report.md`
- validation status: `passed`
- fallback markers: `0`
- picture frames: `0`
- CMP command recorder: `frames=1187 fps=237.4 avg_commands=1261 max_commands=1261 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `1187` / `1187`
- screenshot assertion: `passed`

Native-byte invalid-stream fallback report:

- report: `/tmp/magic-jewel-command-native-byte-invalid/report.md`
- validation status: `passed`
- fallback markers: `1`
- expected fallback reason: `command-stream-invalid`
- CMP command recorder: `frames=999 fps=199.8 avg_commands=1239 max_commands=1239 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `999` / `0`
- screenshot assertion: `passed` on the old Swing fallback renderer.

Next checkpoint:

- Replace the JNI `byte[]` handoff with a direct buffer or memory segment so native replay can parse without an additional Java-array pin/copy step.
- After that, add real per-record paint payload expansion rather than continuing to encode all paint as solid ARGB integers.

### Checkpoint 39: Direct Command Buffer Handoff

Status: completed as the direct-memory carrier slice on top of ABI 7.

- Kept the command ABI at `ABI_ID = 7`; no stream-layout changes.
- Added `ScopedSkiaCanvas.renderCommandDirectFrame(width, height, frameTimeNanos, ByteBuffer commands)` to the JBR private API and public Runtime API mirror.
- Skiko command mode now encodes the ABI 7 stream into a direct little-endian `ByteBuffer` and calls `renderCommandDirectFrame(...)`.
- Skiko reflection now maps concrete direct-buffer implementations back to the public `ByteBuffer` parameter type.
- JBR Java validates direct-buffer size/alignment, tries native direct replay first, and keeps a Java fallback decoder for software/non-native surfaces.
- JBR native bridge now accepts a direct buffer with `GetDirectBufferAddress`, decodes little-endian words in native code, and reuses the existing command parser/replayer.
- JBR tests cover valid direct-buffer replay and unaligned direct-buffer rejection. Skiko tests cover the reflective direct-buffer method on the fake scoped canvas.

Verification completed:

- JBR patched class rebuild into `/tmp/jbr-skia-run/desktop`.
- JBR `JBRSkiaApiTest` compiled and passed against the patched module.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.

Direct-buffer positive smoke report:

- report: `/tmp/magic-jewel-command-direct-buffer-smoke/report.md`
- validation status: `passed`
- fallback markers: `0`
- picture frames: `0`
- CMP command recorder: `frames=994 fps=198.8 avg_commands=1261 max_commands=1261 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `994` / `994`
- screenshot assertion: `passed`

Direct-buffer invalid-stream fallback report:

- report: `/tmp/magic-jewel-command-direct-buffer-invalid/report.md`
- validation status: `passed`
- fallback markers: `1`
- expected fallback reason: `command-stream-invalid`
- CMP command recorder: `frames=1083 fps=216.6 avg_commands=1239 max_commands=1239 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `1083` / `0`
- screenshot assertion: `passed` on the old Swing fallback renderer.

Next checkpoint:

- Remove the remaining native copy from direct-buffer input into `std::vector<jint>` by teaching the parser to read little-endian words lazily from the direct byte span.
- Then start expanding paint payloads beyond solid ARGB.

### Checkpoint 40: Lazy Native Direct-Buffer Parsing

Status: completed as a native hot-path cleanup on top of ABI 7.

- Kept the command ABI at `ABI_ID = 7`; no stream-layout or carrier changes.
- Replaced the native `std::vector<jint>` materialization step with small command-word readers:
  - `IntCommandWords` for the legacy `int[]` path.
  - `LittleEndianByteCommandWords` for both `byte[]` and direct `ByteBuffer` paths.
- Templated the native command replayer so all three carriers use the same validated replay logic without requiring a copied `jint` buffer.
- The direct-buffer path now parses little-endian command words directly from `GetDirectBufferAddress(...)`.
- The `byte[]` path still has JNI array pin/copy semantics, but no longer allocates an additional native vector before replay.
- The legacy `int[]` path remains intact as a compatibility and Java fallback path.

Verification completed:

- JBR patched class rebuild into `/tmp/jbr-skia-run/desktop`.
- JBR `JBRSkiaApiTest` compiled and passed against the patched module.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Magic Jewel direct-buffer lazy positive smoke report:
  - report: `/tmp/magic-jewel-command-direct-lazy-smoke/report.md`
  - validation status: `passed`
  - fallback markers: `0`
  - CMP command recorder: `frames=1118 fps=223.6 avg_commands=1261 max_commands=1261 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
  - Skiko/JBR command frames: `1117` / `1117`
  - screenshot assertion: `passed`
- Magic Jewel direct-buffer lazy invalid-stream fallback report:
  - report: `/tmp/magic-jewel-command-direct-lazy-invalid/report.md`
  - validation status: `passed`
  - fallback markers: `1`
  - expected fallback reason: `command-stream-invalid`
  - CMP command recorder: `frames=881 fps=176.2 avg_commands=1239 max_commands=1239 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
  - Skiko/JBR command frames: `881` / `0`
  - screenshot assertion: `passed` on the old Swing fallback renderer.

Next checkpoint:

- Start expanding command paint payloads beyond solid ARGB, with an ABI bump, so the command stream can carry per-record paint state instead of relying on implicit defaults.
- Preserve the SKP path as the visual/correctness oracle for benchmark runs on a quieter machine.

### Checkpoint 41: Per-Record Antialias Paint Flag And ABI 8

Status: completed as the first paint-payload expansion for the command stream.

- Bumped the PoC command ABI to `ABI_ID = 8`.
- Added `COMMAND_RECORD_FLAG_ANTIALIAS = 1` and `COMMAND_CAP_RECORD_ANTIALIAS = 512` to the JBR private API, public Runtime API mirror, Skiko discovery requirements, and CMP command recorder.
- Kept the stream header, byte-length record framing, and direct-buffer carrier unchanged.
- JBR now validates command record flags with a strict mask instead of requiring every record to use `COMMAND_RECORD_FLAGS_NONE`.
- JBR Java2D fallback replay applies each record's antialiasing flag before drawing paint-bearing records.
- JBR native Skia replay applies each record's antialiasing flag to paints and rect clips.
- CMP records `Paint.isAntiAlias` into each paint-bearing command record; clip-rect commands carry antialiasing because the Skiko canvas clip call is antialiased on this path.
- Skiko's synthetic command scene emits ABI 8 streams and uses antialias flags on the rounded/oval/line primitives.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh from `out/classes/8`.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`.
- JBR isolated patched-class compile and `JBRSkiaApiTest` run against `/tmp/jbr-skia-abi8-compile`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.

ABI 8 positive command smoke report:

- report: `/tmp/magic-jewel-command-aa-abi8-smoke-2/report.md`
- validation status: `passed`
- fallback markers: `0`
- CMP command recorder: `frames=1010 fps=202.0 avg_commands=1239 max_commands=1239 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `1010` / `1010`
- screenshot assertion: `passed`

ABI 8 invalid-stream fallback report:

- report: `/tmp/magic-jewel-command-aa-abi8-invalid-2/report.md`
- validation status: `passed`
- fallback markers: `1`
- expected fallback reason: `command-stream-invalid`
- CMP command recorder: `frames=709 fps=141.8 avg_commands=1197 max_commands=1197 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `709` / `0`
- screenshot assertion: `passed` on the old Swing fallback renderer.

Next checkpoint:

- Add a second paint payload slice for stroke metadata, starting with cap/join/miter in record payload or flags.
- Keep SKP replay available as the correctness oracle for benchmark and rendering-difference runs.

### Checkpoint 42: Stroke Metadata Paint Payload And ABI 9

Status: completed as the second paint-payload expansion for the command stream.

- Bumped the PoC command ABI to `ABI_ID = 9`.
- Added `COMMAND_CAP_STROKE_METADATA = 1024` to the JBR private API, public Runtime API mirror, Skiko compatibility gate, and command capability requirements.
- Expanded stroke command records from implicit stroke defaults to explicit stroke metadata:
  - `COMMAND_STROKE_LINE`: `[op, 48, flags, argb, x1, y1, x2, y2, strokeWidth, strokeCap, strokeJoin, strokeMiter1000]`
  - `COMMAND_STROKE_OVAL`: `[op, 48, flags, argb, x, y, width, height, strokeWidth, strokeCap, strokeJoin, strokeMiter1000]`
- The metadata encoding is shared across JBR, Skiko, and CMP:
  - cap: `0=butt`, `1=round`, `2=square`
  - join: `0=miter`, `1=round`, `2=bevel`
  - miter: `strokeMiterLimit * 1000`, clamped to non-negative integer.
- JBR validates stroke metadata before replaying command streams and rejects invalid cap/join/miter records as command-stream invalid.
- JBR Java2D fallback replay maps stroke metadata into `BasicStroke(width, cap, join, miter)` so invalid-command fallback remains visually meaningful.
- JBR native Skia replay maps the same payload into `SkPaint::setStrokeCap`, `setStrokeJoin`, and `setStrokeMiter`.
- CMP records Compose `StrokeCap`, `StrokeJoin`, and `Paint.strokeMiterLimit` for line, rect-outline, and oval stroke records.
- Skiko's synthetic command scene emits ABI 9 streams with explicit stroke metadata.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh from `out/classes/8`.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`.
- JBR isolated patched-class compile and `JBRSkiaApiTest` run against `/tmp/jbr-skia-abi9-compile`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.

ABI 9 positive command smoke report:

- report: `/tmp/magic-jewel-command-stroke-abi9-smoke-2/report.md`
- validation status: `passed`
- fallback markers: `0`
- CMP command recorder: `frames=721 fps=90.1 avg_commands=1458 max_commands=1488 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `720` / `720`
- screenshot assertion: `passed`

ABI 9 invalid-stream fallback report:

- report: `/tmp/magic-jewel-command-stroke-abi9-invalid/report.md`
- validation status: `passed`
- fallback markers: `1`
- expected fallback reason: `command-stream-invalid`
- CMP command recorder: `frames=936 fps=156.0 avg_commands=1488 max_commands=1488 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `936` / `0`
- screenshot assertion: `passed` on the old Swing fallback renderer.

Harness note:

- A first 5-second Magic smoke run at `/tmp/magic-jewel-command-stroke-abi9-smoke/report.md` failed before any draw markers appeared, while a manual run painted immediately. Re-running with an 8-second duration and 60-second startup timeout produced the passing report above, so command-path validation should keep a startup/duration margin when Gradle is launching the app under load.

Next checkpoint:

- Continue broadening the command record surface toward common Compose vector output, likely with path/image or transform metadata depending on what the next unsupported SKP comparison shows.
- Keep the SKP replay path around as the correctness oracle and benchmark reference for runs on a quieter machine.

### Checkpoint 43: Basic Transform Commands And ABI 10

Status: completed as the first canvas-state command expansion.

- Bumped the PoC command ABI to `ABI_ID = 10`.
- Added `COMMAND_CAP_BASIC_TRANSFORMS = 2048` to the JBR private API, public Runtime API mirror, and Skiko compatibility gate.
- Added explicit transform records to the command stream:
  - `COMMAND_TRANSLATE`: `[op, 20, 0, dx1000, dy1000]`
  - `COMMAND_SCALE`: `[op, 20, 0, sx1000, sy1000]`
  - `COMMAND_ROTATE`: `[op, 16, 0, degrees1000]`
- CMP now records translate, scale, and rotate as canvas-state commands instead of pre-baking translate/scale into primitive coordinates.
- JBR Java2D fallback replay maps these records to `Graphics2D.translate`, `Graphics2D.scale`, and `Graphics2D.rotate`.
- JBR native Skia replay maps them to `SkCanvas::translate`, `SkCanvas::scale`, and `SkCanvas::rotate`.
- Transform records reject non-zero record flags; paint-bearing records still carry antialias and stroke metadata as before.
- Magic Jewel report harness now waits for draw/replay markers before starting the measurement window, avoiding false failures where short runs killed Gradle before first paint under load.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh from `out/classes/8`.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`.
- JBR isolated patched-class compile and `JBRSkiaApiTest` run against `/tmp/jbr-skia-abi10-compile`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.
- Magic Jewel report harness validation `./scripts/test-jbr-skia-report-validation.sh`.

ABI 10 transform command smoke report:

- report: `/tmp/magic-jewel-command-transform-abi10-smoke/report.md`
- validation status: `passed`
- fallback markers: `0`
- CMP command recorder: `frames=551 fps=91.8 avg_commands=2157 max_commands=2157 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `550` / `550`
- screenshot assertion: `passed`

ABI 10 all-probe fallback report:

- report: `/tmp/magic-jewel-command-probe-abi10-all/report.md`
- validation status: `passed`
- unsupported reasons: `unsupportedScope`, `saveLayer`, `clipRect_Difference`, `image`
- confirmed `transform` no longer appears in the unsupported-reason set.
- Skiko/JBR picture replay frames: `1697` / `1697`
- screenshot assertion: `passed`

ABI 10 invalid-stream fallback report:

- report: `/tmp/magic-jewel-command-transform-abi10-invalid/report.md`
- validation status: `passed`
- fallback markers: `1`
- expected fallback reason: `command-stream-invalid`
- CMP command recorder: `frames=1139 fps=189.8 avg_commands=2157 max_commands=2157 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `1139` / `0`
- screenshot assertion: `passed` on the old Swing fallback renderer.

Next checkpoint:

- Reduce fallback frequency from remaining common probes: `image`, `clipRect_Difference`, and `saveLayer` are the next obvious unsupported surfaces.
- Path/image work should be weighed against keeping the ABI compact; the SKP replay path remains the correctness oracle while command coverage grows.

### Checkpoint 44: Clip Operation Payload And ABI 11

Status: completed as the second canvas-state command expansion.

- Bumped the PoC command ABI to `ABI_ID = 11`.
- Added `COMMAND_CAP_CLIP_RECT_OP = 4096` to the JBR private API, public Runtime API mirror, and Skiko compatibility gate.
- Expanded `COMMAND_CLIP_RECT` from an implicit intersect record to an explicit clip-op record:
  - `COMMAND_CLIP_RECT`: `[op, 32, flags, x, y, width, height, clipOp]`
  - `clipOp=0`: intersect
  - `clipOp=1`: difference
- CMP now records both `ClipOp.Intersect` and `ClipOp.Difference` instead of marking clip-out scopes unsupported.
- JBR native Skia replay maps the op to `SkClipOp::kIntersect` or `SkClipOp::kDifference`.
- JBR Java2D fallback replay maps intersect to `Graphics2D.clipRect` and difference to `Area.subtract(...)` when a current clip exists.
- JBR validates clip op payloads and rejects unknown clip operation values as invalid command streams.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh from `out/classes/8`.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`.
- JBR isolated patched-class compile and `JBRSkiaApiTest` run against `/tmp/jbr-skia-abi11-compile`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.

ABI 11 clip command smoke report:

- report: `/tmp/magic-jewel-command-clip-abi11-smoke/report.md`
- validation status: `passed`
- fallback markers: `0`
- CMP command recorder: `frames=456 fps=76.0 avg_commands=2174 max_commands=2174 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `455` / `455`
- screenshot assertion: `passed`

ABI 11 all-probe fallback report:

- report: `/tmp/magic-jewel-command-probe-abi11-all/report.md`
- validation status: `passed`
- unsupported reasons: `unsupportedScope`, `saveLayer`, `image`
- confirmed `clipRect_Difference` no longer appears in the unsupported-reason set.
- Skiko/JBR picture replay frames: `406` / `406`
- screenshot assertion: `passed`

Next checkpoint:

- Decide between image support and saveLayer support. Image is a single draw operation but needs pixel/texture payload design; saveLayer removes `unsupportedScope` for the current probe but needs compositing semantics.
- Keep SKP replay as fallback/correctness oracle while command coverage remains incomplete.

### Checkpoint 45: SaveLayer Command Records And ABI 12

Status: completed as the third canvas-state command expansion.

- Bumped the PoC command ABI to `ABI_ID = 12`.
- Added `COMMAND_CAP_SAVE_LAYER = 8192` to the JBR private API, public Runtime API mirror, and Skiko compatibility gate.
- Added a bounded save-layer command:
  - `COMMAND_SAVE_LAYER`: `[op, 32, 0, x, y, width, height, alpha1000]`
  - `alpha1000` is a fixed-point opacity value in the inclusive range `0..1000`.
- CMP records `Canvas.saveLayer(bounds, paint)` when the layer paint is simple enough for the current command ABI: `BlendMode.SrcOver`, no shader, no color filter, no path effect, and alpha expressible as `alpha1000`.
- CMP still falls back to the SKP replay path for unsupported layer paint shapes rather than silently dropping effects.
- JBR native Skia replay maps the command to `SkCanvas::saveLayerAlphaf`.
- JBR Java2D fallback replay creates a child `Graphics2D` and clips it to the layer bounds. Alpha is intentionally not modeled in the Java2D fallback because this path is only a validation/fallback oracle for the command parser.
- JBR validates save-layer flags, payload length, and alpha range and rejects invalid streams.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh from `out/classes/8`.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`.
- JBR isolated patched-class compile and `JBRSkiaApiTest` run against `/tmp/jbr-skia-abi12-compile`, using a temporary `JBRApi` compile stub for the isolated test harness only.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.

ABI 12 saveLayer command smoke report:

- report: `/tmp/magic-jewel-command-savelayer-abi12-smoke/report.md`
- validation status: `passed`
- fallback markers: `0`
- CMP command recorder: `frames=1827 fps=304.5 avg_commands=2094 max_commands=2148 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `1827` / `1827`
- screenshot assertion: `passed`

ABI 12 all-probe fallback report:

- report: `/tmp/magic-jewel-command-probe-abi12-all/report.md`
- validation status: `passed`
- unsupported reasons: `image`
- confirmed `unsupportedScope` and `saveLayer` no longer appear in the unsupported-reason set.
- Skiko/JBR picture replay frames: `761` / `761`
- screenshot assertion: `passed`

ABI 12 invalid-stream fallback report:

- report: `/tmp/magic-jewel-command-savelayer-abi12-invalid/report.md`
- validation status: `passed`
- fallback markers: `1`
- CMP/Skiko command frames: `1441` / `1441`
- JBR command frames: `0`
- screenshot assertion: `passed`

Next checkpoint:

- Image content is now the only expected fallback reason in the all-probe command run.
- Decide whether to add a minimal image payload command for small raster content or keep image content on SKP until the final native ABI replaces the toy int-array command transport.

### Checkpoint 46: Inline ARGB Image Command And ABI 13

Status: completed as the first raster-image command expansion.

- Bumped the PoC command ABI to `ABI_ID = 13`.
- Added `COMMAND_CAP_DRAW_IMAGE_ARGB = 16384` to the JBR private API, public Runtime API mirror, and Skiko compatibility gate.
- Added an inline raster image command:
  - `COMMAND_DRAW_IMAGE_ARGB`: `[op, 64 + pixelCount * 4, flags, srcLeft1000, srcTop1000, srcRight1000, srcBottom1000, dstLeft1000, dstTop1000, dstRight1000, dstBottom1000, imageWidth, imageHeight, alpha1000, filterQuality, pixelCount, argb0, ...]`
  - pixels are copied from Compose `ImageBitmap.readPixels(...)` as ARGB ints.
  - image dimensions are capped in CMP at `512x512` for this int-array PoC transport, and JBR rejects dimensions larger than `4096x4096`.
- CMP now records simple image draws into the command stream when the paint is `BlendMode.SrcOver` with no shader, color filter, or path effect.
- JBR native Skia replay copies the inline ARGB payload into a temporary raster `SkImage` via `SkImages::RasterFromPixmapCopy(...)` and draws it with the recorded source/destination rect, alpha, and sampling hint.
- JBR Java2D fallback replay builds a temporary `BufferedImage(TYPE_INT_ARGB)` from the payload and draws it with source/destination rects and an alpha composite.
- This is intentionally not the final image ABI. It is a correctness and coverage step for small raster content; a production ABI should avoid per-frame inline pixel payloads for stable images/textures.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh from `out/classes/8`.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`.
- JBR isolated patched-class compile and `JBRSkiaApiTest` run against `/tmp/jbr-skia-abi13-compile`, using a temporary `JBRApi` compile stub for the isolated test harness only.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.

ABI 13 image command smoke report:

- report: `/tmp/magic-jewel-command-image-abi13-smoke/report.md`
- validation status: `passed`
- fallback markers: `0`
- CMP command recorder: `frames=1653 fps=275.5 avg_commands=7328 max_commands=7328 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `1652` / `1652`
- picture replay frames: `0`
- screenshot assertion: `passed`

ABI 13 all-probe command report:

- report: `/tmp/magic-jewel-command-probe-abi13-all/report.md`
- validation status: `passed`
- fallback markers: `0`
- unsupported reasons: `none`
- CMP command recorder: `frames=982 fps=163.7 avg_commands=7423 max_commands=7423 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `981` / `981`
- picture replay frames: `0`
- screenshot assertion: `passed`

ABI 13 invalid-stream fallback report:

- report: `/tmp/magic-jewel-command-image-abi13-invalid/report.md`
- validation status: `passed`
- fallback markers: `1`
- CMP/Skiko command frames: `1362` / `1362`
- JBR command frames: `0`
- screenshot assertion: `passed`

Next checkpoint:

- Text remains intentionally outside the command subset until the JBR-owned font/typeface story is implemented.
- The command transport now covers the current non-text Magic Jewel probe without SKP fallback; the next valuable slice is either a stricter image cache/handle strategy or beginning the text/font bridge design.

### Checkpoint 47: Temporary Text-As-Image Command Bridge

Status: completed as a Magic Jewel validation bridge; not the final font/typeface architecture.

- Added a CMP-side temporary text bridge that rasterizes Skia Paragraph output into an `ImageBitmap` and records it through the existing ABI 13 `COMMAND_DRAW_IMAGE_ARGB` command.
- Raised CMP's inline image recorder limit from `512x512` to `2048x2048` so typical Jewel paragraph bounds can remain on command replay.
- Kept the true production rule unchanged: final fast-path text must be produced by JBR-owned Skia text/font/typeface objects. This checkpoint avoids crossing Skiko/JBR `SkTypeface*` boundaries by crossing only ARGB pixels, so it is useful for mixed-content validation but still has CPU raster/pixel payload cost for text.
- Updated Magic Jewel command-report defaults so Compose text is enabled by default in strict command mode.
- Updated Magic Jewel README examples to treat text/image/transform/saveLayer/clip/clip-out as supported command-mode probes and keep corrupt command streams as the deliberate fallback case.

Verification completed:

- Baseline expected-fallback report before the bridge:
  - report: `/tmp/magic-jewel-command-text-abi13-fallback/report.md`
  - validation status: `passed`
  - CMP command recorder: `frames=701 fps=116.8 avg_commands=2144 max_commands=2144 unsupported_frames=701 avg_unsupported=8.0 max_unsupported=8 reasons=text:5608`
  - Skiko/JBR command frames: `0` / `0`
  - Skiko/JBR picture replay frames: `700` / `700`
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-text:desktopJar`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`.
- Magic Jewel text command report:
  - report: `/tmp/magic-jewel-command-text-image-bridge-abi13-smoke/report.md`
  - validation status: `passed`
  - fallback markers: `0`
  - CMP command recorder: `frames=894 fps=149.0 avg_commands=80603 max_commands=80888 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
  - Skiko/JBR command frames: `893` / `893`
  - picture replay frames: `0`
  - screenshot assertion: `passed`
- Magic Jewel full mixed-content command report:
  - report: `/tmp/magic-jewel-command-full-text-image-bridge-abi13-smoke/report.md`
  - validation status: `passed`
  - fallback markers: `0`
  - CMP command recorder: `frames=784 fps=130.7 avg_commands=85880 max_commands=86183 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
  - Skiko/JBR command frames: `784` / `784`
  - picture replay frames: `0`
  - screenshot assertion: `passed`

Next checkpoint:

- Begin replacing text-as-image with a real text ABI design:
  - define the minimal records JBR needs to create/draw text with JBR-owned fonts/typefaces, or
  - define a higher-level paragraph/text-layout service owned by JBR Skia.
- In parallel, investigate image payload caching so stable raster/text payloads stop resending full ARGB pixels every frame.

### Checkpoint 48: Cached ARGB Image Handles And ABI 14

Status: completed as a PoC command-stream cache for stable raster payloads.

- Bumped the PoC command ABI to `ABI_ID = 14`.
- Added `COMMAND_CAP_IMAGE_CACHE = 32768` to the JBR private API, public Runtime API mirror, and Skiko compatibility gate.
- Added two cached-image records:
  - `COMMAND_DEFINE_IMAGE_ARGB`: `[op, 32 + pixelCount * 4, 0, cacheKeyHigh, cacheKeyLow, imageWidth, imageHeight, pixelCount, argb0, ...]`
  - `COMMAND_DRAW_IMAGE_REF`: `[op, 68, flags, srcLeft1000, srcTop1000, srcRight1000, srcBottom1000, dstLeft1000, dstTop1000, dstRight1000, dstBottom1000, cacheKeyHigh, cacheKeyLow, imageWidth, imageHeight, alpha1000, filterQuality]`
- CMP now hashes image dimensions plus ARGB pixels, emits `DEFINE_IMAGE_ARGB` only once per process for each key, and emits `DRAW_IMAGE_REF` for every draw.
- JBR Java2D fallback keeps a bounded LRU cache of `BufferedImage` instances.
- JBR native Skia replay keeps a PoC process-local `SkImage` cache keyed by the 64-bit image key.
- `COMMAND_DRAW_IMAGE_ARGB` remains available for compatibility within ABI 14, but CMP's recorder now prefers cached-image records.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh from `out/classes/8`.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`.
- JBR isolated patched-class compile and `JBRSkiaApiTest` run against `/tmp/jbr-skia-abi14-compile`, using a temporary `JBRApi` compile stub for the isolated test harness only.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.

ABI 14 cached-image Magic Jewel report:

- report: `/tmp/magic-jewel-command-image-cache-abi14-smoke/report.md`
- validation status: `passed`
- fallback markers: `0`
- CMP command recorder: `frames=927 fps=154.5 avg_commands=2593 max_commands=85768 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `927` / `927`
- picture replay frames: `0`
- screenshot assertion: `passed`
- comparison note: the previous text-as-image bridge report averaged about `85880` command words per frame for the same mixed-content probe; ABI 14 cache refs reduce steady-state payload size while retaining the high first-frame max for image definitions.

ABI 14 invalid-stream fallback report:

- report: `/tmp/magic-jewel-command-image-cache-abi14-invalid/report.md`
- validation status: `passed`
- fallback markers: `1`
- CMP/Skiko command frames: `466` / `466`
- JBR command frames: `0`
- screenshot assertion: `passed`

Next checkpoint:

- Replace the temporary text-as-image bridge with a real text/font ABI design, or add cache invalidation/eviction handshakes if this image cache becomes more than a PoC transport optimization.

### Checkpoint 49: Simple JBR-Owned UTF-16 Text Command And ABI 15

Status: completed as a narrow PoC text command for simple single-line ASCII runs.

- Bumped the PoC command ABI to `ABI_ID = 15`.
- Added `COMMAND_CAP_DRAW_TEXT_UTF16 = 65536` to the JBR private API, public Runtime API mirror, and Skiko compatibility gate.
- Added `COMMAND_DRAW_TEXT_UTF16`:
  - layout: `[op, 32 + charCount * 4, flags, x1000, baseline1000, fontSize1000, argb, charCount, codeUnit0, ...]`
  - supported flags: none and antialias.
  - validation rejects non-positive font sizes, malformed lengths, and more than 4096 UTF-16 code units.
- JBR Java2D fallback replays the record with derived `Graphics2D` font size and antialiasing.
- JBR native replay creates the `SkFont` inside JBR's Skia runtime and uses `drawSimpleText`; no Skiko `SkTypeface*`, `SkFont*`, or paragraph object crosses the ABI boundary.
- CMP records simple text only when it is single-line ASCII, uses a solid color, has no shadow/decoration/blend-mode override, uses normal fill drawing, and has a finite positive font size.
- CMP still falls back to the cached ARGB image bridge for richer text, non-ASCII text, multiline paragraphs, brushes that are not `SolidColor`, and styled text effects.
- First Magic Jewel strict run failed because Jewel `Text` was using the brush paint overload; the final CMP patch records `SolidColor` brush text through the same simple text command.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev` and `/tmp/jbr-api-shim.jar` refresh from `out/classes/8`.
- Skiko `./gradlew :skiko:compileKotlinAwt :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`.
- CMP follow-up `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar` after adding the `SolidColor` brush text path.
- JBR isolated patched-class compile and `JBRSkiaApiTest` run against `/tmp/jbr-skia-abi15-compile`, using a temporary `JBRApi` compile stub for the isolated test harness only.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.

ABI 15 command-mode Magic Jewel report before per-op metrics:

- report: `/tmp/magic-jewel-command-simple-text-abi15-smoke-2/report.md`
- screenshot: `/tmp/magic-jewel-command-simple-text-abi15-smoke-2/new-window.png`
- validation status: `passed`
- fallback markers: `0`
- CMP command recorder: `frames=913 fps=152.2 avg_commands=2577 max_commands=85768 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 reasons=none`
- Skiko/JBR command frames: `913` / `913`
- picture replay frames: `0`
- screenshot assertion: `passed`
- note: this report proved ABI 15 compatibility and strict command-mode rendering, but did not expose per-operation record counts. Checkpoint 50 adds those counters and the explicit text-command probe.

ABI 15 invalid-stream fallback report:

- report: `/tmp/magic-jewel-command-simple-text-abi15-invalid-2/report.md`
- validation status: `passed`
- fallback markers: `1`
- CMP/Skiko command frames before rejection: `664` / `664`
- JBR command frames: `0`
- screenshot assertion: `passed`

Next checkpoint:

- Add command-stream diagnostics for per-op counts, especially text-vs-image records, so reports can prove which ABI records are carrying each visual feature without requiring ad hoc log inspection.
- Continue the real text/font plan beyond this simple smoke command: JBR-owned font selection, shaping, paragraph layout, non-ASCII text, decoration, and richer style coverage.

### Checkpoint 50: Per-Operation Command Metrics And Text Probe

Status: completed as report/harness instrumentation.

- CMP command-recorder frame markers now include parseable per-frame operation counters:
  - `textCommands=<n>`
  - `imageDefines=<n>`
  - `imageRefs=<n>`
- Magic Jewel report parsing now treats those fields as metrics rather than unsupported-operation reasons and reports:
  - average/max text command count
  - average/max image definition count
  - average/max image reference count.
- Magic Jewel now includes an explicit `JbrSkiaCommandRecorder.drawTextUtf16(...)` probe inside the Compose canvas so ABI 15 text commands are visible in the end-to-end report.
- Magic Jewel keeps normal UI labels visible via `BasicText`; those labels still use the richer cached-image text bridge in this checkpoint.
- The explicit text probe is intentionally a PoC harness probe, not the final Compose text integration. The remaining production text work is still JBR-owned font selection/shaping/paragraph layout for real Compose text.

Verification completed:

- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`.
- Magic Jewel `./scripts/test-jbr-skia-report-validation.sh`.
- Magic Jewel command report:
  - report: `/tmp/magic-jewel-command-op-metrics-abi15-direct-text-smoke-2/report.md`
  - screenshot: `/tmp/magic-jewel-command-op-metrics-abi15-direct-text-smoke-2/new-window.png`
  - validation status: `passed`
  - fallback markers: `0`
  - CMP command recorder: `frames=1248 fps=208.0 avg_commands=2669 max_commands=125796 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=1.0 max_text_commands=1 avg_image_defines=0.0 max_image_defines=9 avg_image_refs=9.0 max_image_refs=9 reasons=none`
  - Skiko/JBR command frames: `1247` / `1247`
  - picture replay frames: `0`
  - screenshot assertion: `passed`

Next checkpoint:

- Move the text command from the explicit Magic probe into real Compose text rendering by teaching the Skia paragraph path how to derive a solid text color and JBR-owned font choice safely.
- Keep cached-image text as the fallback for styled/rich text until the JBR-owned paragraph/text shaping ABI exists.

### Checkpoint 51: Real BasicText Uses The JBR Text Command

Status: completed for simple filled Compose text.

- CMP now treats the normalized `Fill` draw style from `TextPainter` as eligible for the simple JBR text command. This fixes the gap where ordinary `BasicText` labels were rejected because `TextPainter` passes `drawStyle = Fill` rather than `null`.
- The simple text command eligibility remains conservative:
  - single-line ASCII only
  - solid color only
  - no shadow or decoration
  - `BlendMode.SrcOver`
  - normal fill draw style only
  - finite positive font size.
- Cached-image text remains the fallback for richer text until the JBR-owned shaping/paragraph ABI exists.
- Magic Jewel's normal `BasicText` labels now use `COMMAND_DRAW_TEXT_UTF16`; the explicit canvas text probe remains in place as a stable ABI probe.
- Magic Jewel strict command validation now allows a one-frame Skiko/JBR marker count delta at teardown. The strict invariants remain: command frames must exist, picture fallback must be zero, unsupported command frames must be zero, and the screenshot assertion must pass.

Verification completed:

- CMP focused test:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-text:desktopTest --tests androidx.compose.ui.text.DesktopParagraphTest.paint_withFillDrawStyle_recordsJbrSkiaSimpleText`
- CMP patched jars:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`
- Magic Jewel report-validation tests:
  - `./scripts/test-jbr-skia-report-validation.sh`
- Magic Jewel text-only report:
  - report: `/tmp/magic-jewel-basictext-fill-textcommands/report.md`
  - validation status: `passed`
  - fallback markers: `0`
  - CMP command recorder: `frames=959 fps=239.8 avg_commands=2389 max_commands=2389 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 reasons=none`
  - Skiko/JBR command frames: `958` / `958`
  - picture replay frames: `0`
  - screenshot assertion: `passed`
- Magic Jewel full mixed-content command report:
  - report: `/tmp/magic-jewel-basictext-fill-full-smoke-2/report.md`
  - screenshot: `/tmp/magic-jewel-basictext-fill-full-smoke-2/new-window.png`
  - validation status: `passed`
  - fallback markers: `0`
  - CMP command recorder: `frames=1011 fps=252.8 avg_commands=2560 max_commands=7746 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_image_defines=0.0 max_image_defines=1 avg_image_refs=1.0 max_image_refs=1 reasons=none`
  - Skiko/JBR command frames: `1010` / `1010`
  - picture replay frames: `0`
  - screenshot assertion: `passed`

Next checkpoint:

- Remove or separately gate the explicit Magic Jewel text probe once ordinary Compose labels are enough for the text-command signal.
- Start the next text slice: either wider Unicode coverage with safe JBR-owned font selection, or a shaped-glyph/paragraph command that keeps all font/typeface objects inside JBR Skia.
- Preserve the cached-image text fallback and the SKP replay path as correctness oracles for styled text and future benchmark runs.

### Checkpoint 52: Magic Jewel Text Signal Comes From Real UI Labels

Status: completed as a sample cleanup after Checkpoint 51.

- Removed the explicit `JbrSkiaCommandRecorder.drawTextUtf16(...)` probe from Magic Jewel.
- Removed Magic Jewel's direct compile-only dependency on the patched `ui-graphics` jar; the app no longer needs to call the recorder API directly.
- The report text-command signal now comes from normal `BasicText` labels rendered through CMP's paragraph path.

Verification completed:

- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.
- Magic Jewel no-probe text-only report:
  - report: `/tmp/magic-jewel-basictext-no-probe-text-smoke/report.md`
  - screenshot: `/tmp/magic-jewel-basictext-no-probe-text-smoke/new-window.png`
  - validation status: `passed`
  - fallback markers: `0`
  - CMP command recorder: `frames=869 fps=217.2 avg_commands=2419 max_commands=2419 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=8.0 max_text_commands=8 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 reasons=none`
  - Skiko/JBR command frames: `869` / `869`
  - picture replay frames: `0`
  - screenshot assertion: `passed`

Next checkpoint:

- Continue text coverage beyond the single-line ASCII fast path while preserving JBR-owned font/typeface ownership.
- Add report assertions that can require a minimum text-command count for text-enabled strict command runs, so future regressions fail without manual report inspection.
- Preserve SKP and cached-image fallback for styled/rich text until the JBR-owned paragraph/text shaping ABI exists.

### Checkpoint 53: Report Guard For Real Text Commands

Status: completed in Magic Jewel report validation.

- Added `EXPECT_MIN_TEXT_COMMANDS` to the Magic Jewel report harness.
- In strict command mode, the report can now fail if the maximum per-frame CMP `textCommands` count is below the requested threshold.
- The guard is opt-in and defaults to `0`, so non-text and fallback tests keep their existing behavior.
- Added report-validation unit cases for:
  - passing when the threshold is met
  - failing when the threshold is not met.

Verification completed:

- Magic Jewel `./scripts/test-jbr-skia-report-validation.sh`.
- Magic Jewel strict text-command report with `EXPECT_MIN_TEXT_COMMANDS=8`:
  - report: `/tmp/magic-jewel-basictext-min-text-guard-smoke/report.md`
  - screenshot: `/tmp/magic-jewel-basictext-min-text-guard-smoke/new-window.png`
  - validation status: `passed`
  - fallback markers: `0`
  - CMP command recorder: `frames=975 fps=243.8 avg_commands=2365 max_commands=2418 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=8.0 max_text_commands=8 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 reasons=none`
  - Skiko/JBR command frames: `975` / `975`
  - picture replay frames: `0`
  - screenshot assertion: `passed`

Next checkpoint:

- Continue text coverage beyond the single-line ASCII fast path while preserving JBR-owned font/typeface ownership.
- Add a negative end-to-end report mode for text disabled or intentionally unsupported text if the harness needs stronger CI-style examples.
- Preserve SKP and cached-image fallback for styled/rich text until the JBR-owned paragraph/text shaping ABI exists.

### Checkpoint 54: Latin-1 Simple Text Coverage

Status: completed as a conservative UTF-16 widening slice.

- CMP now allows simple text commands for Latin-1 BMP code units (`<= 0xff`) while still rejecting surrogates and higher code points.
- This intentionally does not claim full Unicode shaping, font fallback, emoji, CJK, bidi, or complex-script support. Those remain future JBR-owned paragraph/text-shaping work.
- Magic Jewel now includes a normal `BasicText` label with `Caf\u00e9`, so the end-to-end report exercises a non-ASCII UTF-16 code unit through the real Compose text path.

Verification completed:

- CMP focused tests and patched jars:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-text:desktopTest --tests androidx.compose.ui.text.DesktopParagraphTest.paint_withFillDrawStyle_recordsJbrSkiaSimpleText --tests androidx.compose.ui.text.DesktopParagraphTest.paint_withLatin1Text_recordsJbrSkiaSimpleText :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.
- Magic Jewel Latin-1 strict text-command report:
  - report: `/tmp/magic-jewel-latin1-text-command-smoke/report.md`
  - screenshot: `/tmp/magic-jewel-latin1-text-command-smoke/new-window.png`
  - validation status: `passed`
  - fallback markers: `0`
  - `EXPECT_MIN_TEXT_COMMANDS`: `9`
  - CMP command recorder: `frames=679 fps=169.8 avg_commands=2426 max_commands=2427 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 reasons=none`
  - Skiko/JBR command frames: `679` / `679`
  - picture replay frames: `0`
  - screenshot assertion: `passed`

Next checkpoint:

- Add an intentional unsupported text case and validate that it uses the cached-image/SKP fallback path explicitly, or begin a more structured JBR-owned shaped-text command.
- Preserve the current Latin-1 gate until the JBR-owned font fallback/shaping story is explicit.

### Checkpoint 55: Unsupported Text Uses Cached-Image Command Fallback

Status: completed in Magic Jewel report validation.

- Added a Magic Jewel `MAGIC_JEWEL_UNSUPPORTED_TEXT` toggle.
- When enabled, Magic Jewel renders a normal `BasicText` label containing a surrogate-pair character (`\uD83D\uDE80`).
- CMP's simple text command rejects this label because surrogate pairs remain outside the conservative Latin-1 gate.
- The unsupported text still renders through the cached ARGB image command path, not through SKP picture replay and not through an unsupported-command fallback.
- Added `EXPECT_MIN_IMAGE_REFS` to the Magic Jewel report harness so strict command runs can assert the cached-image fallback is present.

Verification completed:

- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.
- Magic Jewel `./scripts/test-jbr-skia-report-validation.sh`.
- Magic Jewel unsupported-text strict command report:
  - report: `/tmp/magic-jewel-unsupported-text-image-ref-smoke/report.md`
  - screenshot: `/tmp/magic-jewel-unsupported-text-image-ref-smoke/new-window.png`
  - validation status: `passed`
  - fallback markers: `0`
  - `EXPECT_MIN_TEXT_COMMANDS`: `9`
  - `EXPECT_MIN_IMAGE_REFS`: `1`
  - CMP command recorder: `frames=800 fps=200.0 avg_commands=2552 max_commands=17661 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_image_defines=0.0 max_image_defines=1 avg_image_refs=1.0 max_image_refs=1 reasons=none`
  - Skiko/JBR command frames: `799` / `799`
  - picture replay frames: `0`
  - screenshot assertion: `passed`

Next checkpoint:

- Begin replacing text-as-image fallback with a real JBR-owned shaped text/paragraph command, or add a cache eviction/invalidation contract for image fallback if it remains in the PoC longer.
- Preserve the SKP path as the correctness oracle for styled/rich text and future benchmark runs.

### Checkpoint 56: Cached Image Reset Handshake

Status: completed in source, unit validation, and Magic Jewel strict command validation.

- Bump the PoC command ABI to `ABI_ID = 16`.
- Add `COMMAND_CAP_CLEAR_IMAGE_CACHE` and `COMMAND_CLEAR_IMAGE_CACHE`.
- CMP now bounds its process-local command image-key set and emits `COMMAND_CLEAR_IMAGE_CACHE` before redefining images after the local key threshold is reached.
- JBR Java validation and native replay both accept the new clear-cache record.
- JBR Java2D fallback replay clears its image cache when it sees the clear-cache record.
- Native JBR command replay clears the JBR-owned image cache when it sees the clear-cache record.
- Skiko compatibility now requires the clear-image-cache capability before selecting command mode.
- This keeps the current cached-image fallback honest while unsupported/rich text still depends on image commands, and avoids an unbounded key/cache lifetime in long-running animated sessions.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev`.
- `/tmp/jbr-api-shim.jar` refreshed from `/tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar`.
- Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`.
- JBR isolated patched-class compile and `JBRSkiaApiTest` run against `/tmp/jbr-skia-abi16-compile`.
- JBR patched module refreshed at `/tmp/jbr-skia-run/desktop`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.

ABI 16 strict command Magic Jewel report:

- report: `/tmp/magic-jewel-clear-cache-abi16-smoke-2/report.md`
- screenshot: `/tmp/magic-jewel-clear-cache-abi16-smoke-2/new-window.png`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_TEXT_COMMANDS`: `9`
- `EXPECT_MIN_IMAGE_REFS`: `1`
- CMP command recorder: `frames=866 fps=216.5 avg_commands=2551 max_commands=17661 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_image_defines=0.0 max_image_defines=1 avg_image_refs=1.0 max_image_refs=1 reasons=none`
- Skiko/JBR command frames: `865` / `865`
- screenshot assertion: `passed`

Next checkpoint:

- Add a deliberate sample/test path that creates enough distinct fallback images to exercise `COMMAND_CLEAR_IMAGE_CACHE` through the full Magic Jewel report, or move directly into a JBR-owned shaped-text/paragraph command to reduce reliance on cached-image text fallback.

### Checkpoint 57: End-To-End Image Cache Reset Churn

Status: completed in CMP marker instrumentation and Magic Jewel report validation.

- CMP's command recorder marker now includes `imageCacheClears=<count>`.
- Magic Jewel has an opt-in `MAGIC_JEWEL_IMAGE_CACHE_CHURN` mode.
- The churn mode draws 260 distinct tiny images per frame, forcing CMP to emit `COMMAND_CLEAR_IMAGE_CACHE` once the local image-key threshold is crossed.
- The Magic Jewel report harness now supports `EXPECT_MIN_IMAGE_CACHE_CLEARS`.
- Report validation tests cover both passing and failing minimum image-cache-clear assertions.

Verification completed:

- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.
- Magic Jewel `./scripts/test-jbr-skia-report-validation.sh`.

Image-cache churn strict command report:

- report: `/tmp/magic-jewel-image-cache-clear-churn-smoke/report.md`
- screenshot: `/tmp/magic-jewel-image-cache-clear-churn-smoke/new-window.png`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_TEXT_COMMANDS`: `8`
- `EXPECT_MIN_IMAGE_CACHE_CLEARS`: `1`
- CMP command recorder: `frames=461 fps=153.7 avg_commands=11584 max_commands=11587 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_image_defines=260.0 max_image_defines=260 avg_image_refs=260.0 max_image_refs=260 avg_image_cache_clears=1.0 max_image_cache_clears=2 reasons=none`
- Skiko/JBR command frames: `460` / `460`
- screenshot assertion: `passed`

Next checkpoint:

- Start reducing reliance on image fallback by adding a JBR-owned shaped-text/paragraph command, or add narrower cache-reset tests around long-running rich-text/image workloads if shaped text is still too large for the next slice.

### Checkpoint 58: JBR-Side Image Cache Reset Marker

Status: completed in JBR replay logging and Magic Jewel report validation.

- JBR Java2D fallback replay now emits `JBR_SKIA_INTEROP_IMAGE_CACHE_CLEAR backend=java2d` when it consumes `COMMAND_CLEAR_IMAGE_CACHE`.
- JBR native Metal/Skia replay now emits `JBR_SKIA_INTEROP_IMAGE_CACHE_CLEAR backend=native` when it consumes `COMMAND_CLEAR_IMAGE_CACHE`.
- Magic Jewel report parsing now includes JBR image-cache-clear marker counts.
- The report harness now supports `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS`.
- Report validation tests cover both passing and failing JBR-side cache-clear assertions.

Verification completed:

- JBR isolated patched-class compile and `JBRSkiaApiTest` run against `/tmp/jbr-skia-abi16-compile`.
- JBR patched module refreshed at `/tmp/jbr-skia-run/desktop`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Magic Jewel `./scripts/test-jbr-skia-report-validation.sh`.

JBR cache-clear marker strict command report:

- report: `/tmp/magic-jewel-jbr-cache-clear-marker-smoke/report.md`
- screenshot: `/tmp/magic-jewel-jbr-cache-clear-marker-smoke/new-window.png`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_IMAGE_CACHE_CLEARS`: `1`
- `EXPECT_MIN_JBR_IMAGE_CACHE_CLEARS`: `1`
- CMP command recorder: `frames=492 fps=164.0 avg_commands=11584 max_commands=11587 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_image_defines=260.0 max_image_defines=260 avg_image_refs=260.0 max_image_refs=260 avg_image_cache_clears=1.0 max_image_cache_clears=2 reasons=none`
- Skiko/JBR command frames: `492` / `492`
- JBR image cache clear markers: `499`
- screenshot assertion: `passed`

Next checkpoint:

- Begin the shaped-text/paragraph command slice with a deliberately small contract: JBR-owned font/typeface/paragraph state only, no Skiko text-object pointers crossing the ABI, and SKP/image fallback preserved for unsupported styling.

### Checkpoint 59: UTF-16 Text Command Encodes Non-ASCII Correctly

Status: completed for the existing simple text command.

- Fixed the JBR native `COMMAND_DRAW_TEXT_UTF16` replay path so UTF-16 command code units are encoded to UTF-8 instead of replacing every non-ASCII code unit with `?`.
- The native decoder now rejects malformed surrogate pairs and supports valid supplementary code points at the command decoding layer.
- Bumped the PoC command ABI to `ABI_ID = 17` because the text-command rendering semantics changed.
- Runtime API, Skiko, CMP, JBR Java, and JBR native replay were all moved to ABI 17 together.
- Added JBR validation coverage for a Latin-1 text stream.
- Added CMP recorder coverage for a Latin-1 text record.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev`.
- `/tmp/jbr-api-shim.jar` refreshed from `/tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar`.
- Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`.
- JBR isolated patched-class compile and `JBRSkiaApiTest` run against `/tmp/jbr-skia-abi17-compile`.
- JBR patched module refreshed at `/tmp/jbr-skia-run/desktop`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.

ABI 17 Latin-1 strict command Magic Jewel report:

- report: `/tmp/magic-jewel-latin1-utf8-abi17-smoke/report.md`
- screenshot: `/tmp/magic-jewel-latin1-utf8-abi17-smoke/new-window.png`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_TEXT_COMMANDS`: `9`
- CMP command recorder: `frames=1080 fps=360.0 avg_commands=2480 max_commands=2481 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `1079` / `1079`
- screenshot assertion: `passed`

Next checkpoint:

- Start the shaped-text/paragraph command design in earnest. The first implementation target should be a JBR-owned paragraph/text-run command that can render unsupported text without raster image fallback, while preserving the strict ABI/build gate and the SKP/image paths as correctness fallbacks.

### Checkpoint 60: JBR-Owned Paragraph Text Command And ABI 18

Status: completed for a first single-style paragraph command that keeps text objects JBR-owned.

- Added `COMMAND_CAP_DRAW_PARAGRAPH_UTF16` and `COMMAND_DRAW_PARAGRAPH_UTF16` to the JBR private API and public Runtime API mirror.
- Bumped the command ABI to `ABI_ID = 18` across Runtime API, Skiko, CMP, JBR Java replay, and JBR native replay.
- Added Java validation and Java2D fallback replay for the paragraph record shape: `x1000`, `y1000`, `width1000`, `fontSize1000`, `argb`, `charCount`, and UTF-16 code units.
- Added native JBR replay for the paragraph command using JBR-owned Skia Paragraph, CoreText font manager, and ICU Unicode support. No Skiko `SkTypeface`, `SkFont`, or paragraph pointer crosses the ABI.
- Added CMP recorder support and a distinct `paragraphTextCommands` marker so the report can prove paragraph-text usage separately from simple text and image refs.
- Changed `SkiaParagraph` command recording to try simple text first, then the JBR-owned paragraph command, then the existing text-as-image fallback.
- Added Magic Jewel strict report support for `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS`.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev`.
- `/tmp/jbr-api-shim.jar` refreshed from `/tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar`.
- Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP desktop jars rebuilt with `SKIKO_VERSION=0.0.0-SNAPSHOT`.
- Magic Jewel `./scripts/test-jbr-skia-report-validation.sh`.
- JBR isolated patched-class compile into `/tmp/jbr-skia-abi18-compile`.
- JBR patched module refreshed at `/tmp/jbr-skia-run/desktop`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.

ABI 18 paragraph-text strict command Magic Jewel report:

- report: `/tmp/magic-jewel-paragraph-text-abi18-smoke/report.md`
- screenshot: `/tmp/magic-jewel-paragraph-text-abi18-smoke/new-window.png`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_TEXT_COMMANDS`: `8`
- `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS`: `1`
- CMP command recorder: `frames=110 fps=36.7 avg_commands=2545 max_commands=2546 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=1.0 max_paragraph_text_commands=1 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `109` / `109`
- screenshot assertion: `passed`

Next checkpoint:

- Tighten paragraph fidelity: pass font family/style/weight and paragraph alignment/line metrics through the command ABI, then add a screenshot/report probe that compares the paragraph-command path against the existing Skiko paragraph output for representative Jewel labels.

### Checkpoint 61: Paragraph Font-Style Metadata And ABI 19

Status: completed for scalar font-style metadata.

- Bumped the command ABI to `ABI_ID = 19` across Runtime API, Skiko, CMP, JBR Java replay, and JBR native replay.
- Added `COMMAND_CAP_PARAGRAPH_FONT_STYLE` so Skiko can require runtimes where paragraph commands carry font metadata.
- Extended `COMMAND_DRAW_PARAGRAPH_UTF16` with `fontWeight`, `fontWidth`, and `fontSlant` scalar fields before `charCount`.
- CMP now derives those scalars from the resolved paragraph default font and passes only integers across the ABI. No Skiko `SkTypeface`/`SkFont` pointer crosses into JBR.
- JBR native replay reconstructs a JBR-owned `SkFontStyle` and applies it to the JBR-owned Skia Paragraph `TextStyle`.
- JBR Java validation and Java2D fallback replay validate the new font-style fields.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev`.
- `/tmp/jbr-api-shim.jar` refreshed from `/tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar`.
- Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP desktop jars rebuilt with `SKIKO_VERSION=0.0.0-SNAPSHOT`.
- JBR isolated patched-class compile into `/tmp/jbr-skia-abi19-compile`.
- JBR patched module refreshed at `/tmp/jbr-skia-run/desktop`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.

ABI 19 paragraph font-style strict command Magic Jewel report:

- report: `/tmp/magic-jewel-paragraph-fontstyle-abi19-smoke/report.md`
- screenshot: `/tmp/magic-jewel-paragraph-fontstyle-abi19-smoke/new-window.png`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_TEXT_COMMANDS`: `8`
- `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS`: `1`
- CMP command recorder: `frames=101 fps=33.7 avg_commands=2548 max_commands=2549 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=1.0 max_paragraph_text_commands=1 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `100` / `100`
- screenshot assertion: `passed`

Next checkpoint:

- Add paragraph alignment/direction/line-height metadata, then add a Magic Jewel probe that exercises at least one bold/italic or aligned label and keeps the strict no-picture/no-image-ref assertion.

### Checkpoint 62: Paragraph Layout Metadata And ABI 20

Status: completed for scalar paragraph alignment and direction metadata.

- Bumped the command ABI to `ABI_ID = 20` across Runtime API, Skiko, CMP, JBR Java replay, and JBR native replay.
- Added `COMMAND_CAP_PARAGRAPH_LAYOUT` so Skiko can require runtimes where paragraph commands carry layout metadata.
- Extended `COMMAND_DRAW_PARAGRAPH_UTF16` with `textAlign` and `textDirection` scalar fields before `charCount`.
- The ABI 20 paragraph payload is `[op, 56 + charCount * 4, flags, x1000, y1000, width1000, fontSize1000, argb, fontWeight, fontWidth, fontSlant, textAlign, textDirection, charCount, codeUnit0, ...]`.
- `textAlign` follows Skia paragraph ordinals: `Left=0`, `Right=1`, `Center=2`, `Justify=3`, `Start=4`, `End=5`.
- `textDirection` follows Skia paragraph ordinals: `Rtl=0`, `Ltr=1`.
- CMP maps Compose paragraph alignment and resolved text direction to these scalars and still sends no Skiko-owned text, font, or paragraph pointer across the ABI.
- JBR native replay applies the metadata to JBR-owned Skia Paragraph `ParagraphStyle`; Java validation and Java2D fallback replay validate the fields.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev`.
- `/tmp/jbr-api-shim.jar` refreshed from `/tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar`.
- Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP desktop jars rebuilt with `SKIKO_VERSION=0.0.0-SNAPSHOT`.
- JBR isolated patched-class compile into `/tmp/jbr-skia-abi20-compile`.
- JBR patched module refreshed at `/tmp/jbr-skia-run/desktop`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.

ABI 20 paragraph layout strict command Magic Jewel report:

- report: `/tmp/magic-jewel-paragraph-layout-abi20-smoke/report.md`
- screenshot: `/tmp/magic-jewel-paragraph-layout-abi20-smoke/new-window.png`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_TEXT_COMMANDS`: `8`
- `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS`: `1`
- CMP command recorder: `frames=103 fps=34.3 avg_commands=2550 max_commands=2551 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=1.0 max_paragraph_text_commands=1 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `102` / `102`
- screenshot assertion: `passed`

Next checkpoint:

- Add a Magic Jewel visual probe with centered/bold/italic and RTL text so paragraph layout metadata is validated by visible content, not only by command-stream acceptance.

### Checkpoint 63: Magic Jewel Paragraph Layout Visual Probe

Status: completed for an opt-in Magic Jewel visual probe that exercises paragraph layout metadata.

- Added `MAGIC_JEWEL_PARAGRAPH_LAYOUT_TEXT` / `magic.jewel.paragraphLayoutText` to Magic Jewel.
- The probe renders centered bold text, right-aligned italic text, and explicit RTL text through Compose `BasicText`.
- The centered and right-aligned labels intentionally include surrogate-pair text so they take the JBR-owned paragraph command path instead of the simple text command.
- The report harness records the new flag in `report.md`, exports it to old/new runs, and documents it in `--help`.
- README now includes a strict command-mode invocation that requires at least four paragraph text commands.

Verification completed:

- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.
- Magic Jewel strict command report with `MAGIC_JEWEL_UNSUPPORTED_TEXT=true`, `MAGIC_JEWEL_PARAGRAPH_LAYOUT_TEXT=true`, and `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS=4`.

Magic Jewel paragraph layout visual report:

- report: `/tmp/magic-jewel-paragraph-layout-visual-smoke-2/report.md`
- screenshot: `/tmp/magic-jewel-paragraph-layout-visual-smoke-2/new-window.png`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_TEXT_COMMANDS`: `8`
- `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS`: `4`
- CMP command recorder: `frames=20 fps=6.7 avg_commands=2767 max_commands=2767 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=4.0 max_paragraph_text_commands=4 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `19` / `19`
- screenshot assertion: `passed`

Next checkpoint:

- Start tightening paragraph fidelity beyond scalar layout: line-height/max-lines/ellipsis/decorations, with each addition guarded by a command-stream capability bit and an opt-in Magic Jewel probe.

### Checkpoint 64: Cached JBR Paragraph Dependencies

Status: completed for the first paragraph-command performance cleanup.

- JBR native replay now reuses a JBR-owned Skia Paragraph `FontCollection`, CoreText font manager, and ICU Unicode object instead of rebuilding them for every `COMMAND_DRAW_PARAGRAPH_UTF16`.
- This does not change `ABI_ID`, `BUILD_ID`, command payload shape, or any cross-repo compatibility contract.
- The cache still preserves the core ownership rule: all paragraph/font/unicode objects used by the fast path are created inside JBR's Skia runtime.

Verification completed:

- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Magic Jewel strict command report with `MAGIC_JEWEL_UNSUPPORTED_TEXT=true`, `MAGIC_JEWEL_PARAGRAPH_LAYOUT_TEXT=true`, and `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS=4`.

Magic Jewel cached paragraph dependency report:

- report: `/tmp/magic-jewel-paragraph-deps-cache-smoke/report.md`
- screenshot: `/tmp/magic-jewel-paragraph-deps-cache-smoke/new-window.png`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_TEXT_COMMANDS`: `8`
- `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS`: `4`
- CMP command recorder: `frames=766 fps=255.3 avg_commands=2713 max_commands=2714 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=4.0 max_paragraph_text_commands=4 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `765` / `765`
- screenshot assertion: `passed`
- note: the prior visual-probe smoke on the same machine reported `frames=20 fps=6.7`; this large jump is a useful smoke signal, but benchmark-grade numbers still require a quiet-machine run with longer duration and stable profiling.

Next checkpoint:

- Add per-stage timing markers around command replay and paragraph replay so future slowdowns can be attributed to CMP recording, Skiko handoff, JBR command parsing, paragraph build/layout, or Metal submission.

### Checkpoint 65: JBR Command Replay Timing Markers

Status: completed for native command replay timing telemetry.

- Added `JBR_SKIA_INTEROP_COMMAND_TIMING` stderr markers from JBR native command replay.
- The marker reports `totalNanos`, `drawNanos`, `flushNanos`, `paragraphCommands`, and `paragraphNanos`.
- The marker is emitted for int-array, byte-array, and direct-byte-buffer command replay entry points.
- Magic Jewel report parsing now summarizes the timing marker as average/max total, draw, flush, paragraph time, and paragraph command count.
- This is diagnostic-only and does not change `ABI_ID`, `BUILD_ID`, or the command-stream shape.

Verification completed:

- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Magic Jewel strict command report with `MAGIC_JEWEL_UNSUPPORTED_TEXT=true`, `MAGIC_JEWEL_PARAGRAPH_LAYOUT_TEXT=true`, and `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS=4`.

Magic Jewel command timing report:

- report: `/tmp/magic-jewel-command-timing-smoke/report.md`
- screenshot: `/tmp/magic-jewel-command-timing-smoke/new-window.png`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- CMP command recorder: `frames=435 fps=145.0 avg_commands=2767 max_commands=2768 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=4.0 max_paragraph_text_commands=4 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `434` / `434`
- JBR command timing: `frames=434 avg_total_ms=1.938 max_total_ms=82.658 avg_draw_ms=0.353 max_draw_ms=73.038 avg_flush_ms=1.557 max_flush_ms=8.792 avg_paragraph_ms=0.229 max_paragraph_ms=72.360 avg_paragraph_commands=4.0 max_paragraph_commands=4`
- screenshot assertion: `passed`
- note: the timing marker exposes the cold first-frame paragraph outlier directly; steady-state paragraph cost after dependency caching is much lower and Metal flush currently dominates average native replay time in this smoke.

Next checkpoint:

- Add warmup-aware benchmark/report mode so smoke reports can keep catching regressions while benchmark runs can discard cold frames and collect longer, quieter timing windows.

### Checkpoint 66: Warmup-Aware Magic Jewel Report Mode

Status: completed for sampled-window reporting after warmup.

- Added `WARMUP_SECONDS` to the Magic Jewel report harness.
- The harness now waits for the normal startup marker, optionally waits the warmup period, then records the measured window.
- The harness writes `old-sampled.log` and `new-sampled.log`; marker summaries and strict validation use those sampled logs when present.
- Reports include `Warmup per mode` and list both raw logs and sampled logs.
- This keeps correctness smokes at `WARMUP_SECONDS=0` while allowing benchmark-style runs to drop cold startup, JIT, and first-use font/paragraph costs.

Verification completed:

- Magic Jewel `bash -n scripts/jbr-skia-interop-report.sh`.
- Magic Jewel strict command report with `WARMUP_SECONDS=2`, `MAGIC_JEWEL_UNSUPPORTED_TEXT=true`, `MAGIC_JEWEL_PARAGRAPH_LAYOUT_TEXT=true`, and `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS=4`.

Magic Jewel warmup command timing report:

- report: `/tmp/magic-jewel-warmup-command-timing-smoke-2/report.md`
- screenshot: `/tmp/magic-jewel-warmup-command-timing-smoke-2/new-window.png`
- sampled log: `/tmp/magic-jewel-warmup-command-timing-smoke-2/new-sampled.log`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- CMP command recorder: `frames=1117 fps=372.3 avg_commands=2768 max_commands=2768 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=4.0 max_paragraph_text_commands=4 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `1117` / `1117`
- JBR command timing: `frames=1117 avg_total_ms=0.891 max_total_ms=2.593 avg_draw_ms=0.136 max_draw_ms=0.273 avg_flush_ms=0.739 max_flush_ms=2.477 avg_paragraph_ms=0.042 max_paragraph_ms=0.107 avg_paragraph_commands=4.0 max_paragraph_commands=4`
- screenshot assertion: `passed`

Next checkpoint:

- Use the warmup mode for a longer quiet-machine comparison of old path vs command path, keeping the SKP/picture path available as a baseline and recording CPU/FPS/timing in the report.

### Checkpoint 67: Paragraph Line-Height Metadata And ABI 21

Status: completed for scalar paragraph line-height metadata.

- Bumped the command ABI to `ABI_ID = 21` across Runtime API, Skiko, CMP, JBR Java replay, and JBR native replay.
- Added `COMMAND_CAP_PARAGRAPH_LINE_HEIGHT` so Skiko can require runtimes where paragraph commands carry line-height metadata.
- Extended `COMMAND_DRAW_PARAGRAPH_UTF16` with `lineHeightMultiplier1000` before `charCount`.
- The ABI 21 paragraph payload is `[op, 60 + charCount * 4, flags, x1000, y1000, width1000, fontSize1000, argb, fontWeight, fontWidth, fontSlant, textAlign, textDirection, lineHeightMultiplier1000, charCount, codeUnit0, ...]`.
- `lineHeightMultiplier1000 = 0` means the JBR Skia paragraph text style keeps Skia's default height behavior; positive values encode `lineHeightPx / fontSizePx * 1000`.
- CMP derives the multiplier from Compose `TextStyle.lineHeight` for `sp` and `em` units and sends only the scalar across the ABI.
- JBR native replay applies the multiplier with JBR-owned Skia `TextStyle.setHeight()` / `setHeightOverride(true)`.
- Magic Jewel's paragraph layout probe now includes an explicit line-height label so the end-to-end smoke exercises the new field.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev`.
- `/tmp/jbr-api-shim.jar` refreshed from `/tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar`.
- Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP desktop jars rebuilt with `SKIKO_VERSION=0.0.0-SNAPSHOT`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.
- JBR isolated patched-class compile into `/tmp/jbr-skia-abi21-compile`.
- JBR patched module refreshed at `/tmp/jbr-skia-run/desktop`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.

ABI 21 paragraph line-height strict command Magic Jewel report:

- report: `/tmp/magic-jewel-paragraph-lineheight-abi21-smoke/report.md`
- screenshot: `/tmp/magic-jewel-paragraph-lineheight-abi21-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-paragraph-lineheight-abi21-smoke/new-sampled.log`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_TEXT_COMMANDS`: `8`
- `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS`: `4`
- CMP command recorder: `frames=469 fps=156.3 avg_commands=2772 max_commands=2772 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=4.0 max_paragraph_text_commands=4 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `469` / `469`
- JBR command timing: `frames=469 avg_total_ms=1.237 max_total_ms=3.142 avg_draw_ms=0.149 max_draw_ms=0.354 avg_flush_ms=1.069 max_flush_ms=2.815 avg_paragraph_ms=0.048 max_paragraph_ms=0.243 avg_paragraph_commands=4.0 max_paragraph_commands=4`
- screenshot assertion: `passed`

Next checkpoint:

- Add paragraph overflow metadata (`maxLines` and end ellipsis) as the next scalar text-fidelity capability, then gate it with a Magic Jewel probe that deliberately clips a one-line label.

### Checkpoint 68: Paragraph Overflow Metadata And ABI 22

Status: completed for scalar paragraph overflow metadata.

- Bumped the command ABI to `ABI_ID = 22` across Runtime API, Skiko, CMP, JBR Java replay, and JBR native replay.
- Added `COMMAND_CAP_PARAGRAPH_OVERFLOW` so Skiko can require runtimes where paragraph commands carry max-lines and ellipsis metadata.
- Extended `COMMAND_DRAW_PARAGRAPH_UTF16` with `maxLines` and `ellipsisMode` before `charCount`.
- The ABI 22 paragraph payload is `[op, 68 + charCount * 4, flags, x1000, y1000, width1000, fontSize1000, argb, fontWeight, fontWidth, fontSlant, textAlign, textDirection, lineHeightMultiplier1000, maxLines, ellipsisMode, charCount, codeUnit0, ...]`.
- `maxLines = 0` means default/unlimited. Positive values are bounded to `1..4096`.
- `ellipsisMode = 0` means none; `ellipsisMode = 1` means end ellipsis. The ABI intentionally sends a scalar mode rather than any Skiko-owned string or pointer. The current native PoC maps mode `1` to `SkString("...")` inside the JBR-owned Skia runtime.
- CMP derives the fields from Compose paragraph state and clamps `Int.MAX_VALUE` to `0` for the default/unlimited case.
- Magic Jewel's paragraph layout probe now includes a constrained one-line ellipsis label so the strict command smoke exercises the new fields.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev`.
- `/tmp/jbr-api-shim.jar` refreshed from `/tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar`.
- Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP desktop jars rebuilt with `SKIKO_VERSION=0.0.0-SNAPSHOT`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.
- JBR isolated patched-class compile into `/tmp/jbr-skia-abi22-compile`.
- JBR patched module refreshed at `/tmp/jbr-skia-run/desktop`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.

ABI 22 paragraph overflow strict command Magic Jewel report:

- report: `/tmp/magic-jewel-paragraph-overflow-abi22-smoke/report.md`
- screenshot: `/tmp/magic-jewel-paragraph-overflow-abi22-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-paragraph-overflow-abi22-smoke/new-sampled.log`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_TEXT_COMMANDS`: `8`
- `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS`: `5`
- CMP command recorder: `frames=473 fps=157.7 avg_commands=2891 max_commands=2891 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=5.0 max_paragraph_text_commands=5 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `473` / `473`
- JBR command timing: `frames=473 avg_total_ms=1.161 max_total_ms=2.892 avg_draw_ms=0.183 max_draw_ms=0.342 avg_flush_ms=0.959 max_flush_ms=2.750 avg_paragraph_ms=0.080 max_paragraph_ms=0.205 avg_paragraph_commands=5.0 max_paragraph_commands=5`
- screenshot assertion: `passed`

Next checkpoint:

- Add another text-fidelity scalar field, likely paragraph decoration metadata (`underline` / `lineThrough`) if confirmed by CMP/Skia code ground truth, and keep the strict Magic Jewel probe path as the acceptance gate.

### Checkpoint 69: Paragraph Decoration Metadata And ABI 23

Status: completed for scalar paragraph underline / line-through metadata.

- Bumped the command ABI to `ABI_ID = 23` across Runtime API, Skiko, CMP, JBR Java replay, and JBR native replay.
- Added `COMMAND_CAP_PARAGRAPH_DECORATION` so Skiko can require runtimes where paragraph commands carry text-decoration metadata.
- Extended `COMMAND_DRAW_PARAGRAPH_UTF16` with `decorationMask` before `charCount`.
- The ABI 23 paragraph payload is `[op, 72 + charCount * 4, flags, x1000, y1000, width1000, fontSize1000, argb, fontWeight, fontWidth, fontSlant, textAlign, textDirection, lineHeightMultiplier1000, maxLines, ellipsisMode, decorationMask, charCount, codeUnit0, ...]`.
- `decorationMask` uses Compose's stable mask values at the ABI boundary: `0 = none`, `1 = underline`, `2 = lineThrough`, `3 = underline | lineThrough`.
- JBR native replay maps Compose mask bit `1` to Skia `TextDecoration::kUnderline` and Compose mask bit `2` to Skia `TextDecoration::kLineThrough`. No Skiko decoration objects or pointers cross the ABI.
- CMP keeps the simple text command on the no-decoration path and records decorated text through the paragraph command path when there is no unsupported shadow/draw-style/blend-mode effect.
- Magic Jewel's paragraph layout probe now includes an underline + line-through label so the strict command smoke exercises the new field.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev`.
- `/tmp/jbr-api-shim.jar` refreshed from `/tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar`.
- Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP desktop jars rebuilt with `SKIKO_VERSION=0.0.0-SNAPSHOT`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.
- JBR isolated patched-class compile into `/tmp/jbr-skia-abi23-compile`.
- JBR patched module refreshed at `/tmp/jbr-skia-run/desktop`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.

ABI 23 paragraph decoration strict command Magic Jewel report:

- report: `/tmp/magic-jewel-paragraph-decoration-abi23-smoke/report.md`
- screenshot: `/tmp/magic-jewel-paragraph-decoration-abi23-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-paragraph-decoration-abi23-smoke/new-sampled.log`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_TEXT_COMMANDS`: `8`
- `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS`: `6`
- CMP command recorder: `frames=187 fps=62.3 avg_commands=2923 max_commands=2923 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=6.0 max_paragraph_text_commands=6 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `186` / `186`
- JBR command timing: `frames=186 avg_total_ms=1.472 max_total_ms=3.568 avg_draw_ms=0.225 max_draw_ms=0.344 avg_flush_ms=1.223 max_flush_ms=3.340 avg_paragraph_ms=0.113 max_paragraph_ms=0.208 avg_paragraph_commands=6.0 max_paragraph_commands=6`
- screenshot assertion: `passed`

Next checkpoint:

- Continue text-fidelity expansion from code ground truth. Likely candidates are paragraph letter-spacing or foreground alpha/style, but the next slice should first inspect Compose's Skia paragraph builder and only add scalar metadata that Skia paragraph can consume without crossing object-pointer ownership boundaries.

### Checkpoint 70: Paragraph Letter-Spacing Metadata And ABI 24

Status: completed for scalar paragraph letter-spacing metadata.

- Bumped the command ABI to `ABI_ID = 24` across Runtime API, Skiko, CMP, JBR Java replay, and JBR native replay.
- Added `COMMAND_CAP_PARAGRAPH_LETTER_SPACING` so Skiko can require runtimes where paragraph commands carry letter-spacing metadata.
- Extended `COMMAND_DRAW_PARAGRAPH_UTF16` with `letterSpacing1000` before `charCount`.
- The ABI 24 paragraph payload is `[op, 76 + charCount * 4, flags, x1000, y1000, width1000, fontSize1000, argb, fontWeight, fontWidth, fontSlant, textAlign, textDirection, lineHeightMultiplier1000, maxLines, ellipsisMode, decorationMask, letterSpacing1000, charCount, codeUnit0, ...]`.
- `letterSpacing1000` is letter spacing in user-space pixels multiplied by `1000`; the accepted range is `-100000..100000`, allowing negative tracking while bounding malformed streams.
- CMP resolves Compose `sp` and `em` letter spacing to pixels before writing the command stream.
- JBR native replay applies the value through JBR-owned Skia `TextStyle.setLetterSpacing()`.
- Magic Jewel's decorated paragraph probe now includes non-default letter spacing so the strict command smoke exercises the new field.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev`.
- `/tmp/jbr-api-shim.jar` refreshed from `/tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar`.
- Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP desktop jars rebuilt with `SKIKO_VERSION=0.0.0-SNAPSHOT`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.
- JBR isolated patched-class compile into `/tmp/jbr-skia-abi24-compile`.
- JBR patched module refreshed at `/tmp/jbr-skia-run/desktop`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.

ABI 24 paragraph letter-spacing strict command Magic Jewel report:

- report: `/tmp/magic-jewel-paragraph-letterspacing-abi24-smoke/report.md`
- screenshot: `/tmp/magic-jewel-paragraph-letterspacing-abi24-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-paragraph-letterspacing-abi24-smoke/new-sampled.log`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_TEXT_COMMANDS`: `8`
- `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS`: `6`
- CMP command recorder: `frames=579 fps=193.0 avg_commands=2983 max_commands=2983 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=6.0 max_paragraph_text_commands=6 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `578` / `578`
- JBR command timing: `frames=578 avg_total_ms=1.513 max_total_ms=7.306 avg_draw_ms=0.247 max_draw_ms=4.000 avg_flush_ms=1.241 max_flush_ms=7.050 avg_paragraph_ms=0.126 max_paragraph_ms=3.762 avg_paragraph_commands=6.0 max_paragraph_commands=6`
- screenshot assertion: `passed`

Next checkpoint:

- Inspect remaining text effects against CMP/Skia code ground truth. Foreground alpha/color is already carried for solid-color paths; font feature settings, locale, baseline shift, and geometric transform need stricter review before becoming ABI fields because they may require strings, locale objects, or transform semantics rather than simple scalars.

### Checkpoint 71: Paragraph Background Metadata And ABI 25

Status: completed for scalar paragraph background-paint metadata.

- Bumped the command ABI to `ABI_ID = 25` across Runtime API, Skiko, CMP, JBR Java replay, and JBR native replay.
- Added `COMMAND_CAP_PARAGRAPH_BACKGROUND` so Skiko can require runtimes where paragraph commands carry background paint metadata.
- Extended `COMMAND_DRAW_PARAGRAPH_UTF16` with `backgroundSpecified` and `backgroundArgb` before `charCount`.
- The ABI 25 paragraph payload is `[op, 84 + charCount * 4, flags, x1000, y1000, width1000, fontSize1000, argb, fontWeight, fontWidth, fontSlant, textAlign, textDirection, lineHeightMultiplier1000, maxLines, ellipsisMode, decorationMask, letterSpacing1000, backgroundSpecified, backgroundArgb, charCount, codeUnit0, ...]`.
- `backgroundSpecified` is `0` or `1`, so a specified transparent ARGB value remains representable without using a sentinel color.
- CMP derives the fields from Compose `TextStyle.background`.
- JBR native replay applies the value through JBR-owned Skia `TextStyle.setBackgroundPaint()`.
- Magic Jewel's decorated paragraph probe now includes a translucent background so the strict command smoke exercises the new fields.

Verification completed:

- Runtime API `bash tools/build.sh process`.
- Runtime API `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev`.
- `/tmp/jbr-api-shim.jar` refreshed from `/tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar`.
- Skiko `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- Skiko `./gradlew :skiko:publishToMavenLocal`.
- CMP `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`.
- CMP desktop jars rebuilt with `SKIKO_VERSION=0.0.0-SNAPSHOT`.
- Magic Jewel `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`.
- JBR isolated patched-class compile into `/tmp/jbr-skia-abi25-compile`.
- JBR patched module refreshed at `/tmp/jbr-skia-run/desktop`.
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.

ABI 25 paragraph background strict command Magic Jewel report:

- report: `/tmp/magic-jewel-paragraph-background-abi25-smoke/report.md`
- screenshot: `/tmp/magic-jewel-paragraph-background-abi25-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-paragraph-background-abi25-smoke/new-sampled.log`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_TEXT_COMMANDS`: `8`
- `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS`: `6`
- CMP command recorder: `frames=362 fps=120.7 avg_commands=2995 max_commands=2995 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=6.0 max_paragraph_text_commands=6 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `362` / `362`
- JBR command timing: `frames=362 avg_total_ms=1.467 max_total_ms=3.708 avg_draw_ms=0.244 max_draw_ms=1.160 avg_flush_ms=1.198 max_flush_ms=3.412 avg_paragraph_ms=0.122 max_paragraph_ms=1.004 avg_paragraph_commands=6.0 max_paragraph_commands=6`
- screenshot assertion: `passed`

Next checkpoint:

- Pause scalar text expansion and review the remaining unsupported text causes in sampled logs/source before choosing the next ABI field. The likely next step is not another blind scalar bump, but a small compatibility report that lists why text still falls back when strict command mode is enabled on broader Magic Jewel content.

### Checkpoint 72: Broad Strict Command Coverage Smoke

Status: completed as a broader no-fallback validation run.

- Ran Magic Jewel strict command mode with the richer scene enabled:
  - Compose text, including paragraph layout / decoration / letter-spacing / background probes.
  - Compose image probe.
  - Transform probe.
  - SaveLayer probe.
  - Clip intersect and clip difference probes.
- The existing per-frame recorder telemetry was sufficient for this checkpoint; no new telemetry code was required.
- Result: all enabled probes stayed on the strict command path with `unsupported_frames=0`, `reasons=none`, no picture replay, no fallback marker, and a passing screenshot oracle.

Broad strict command Magic Jewel report:

- report: `/tmp/magic-jewel-broad-strict-abi25-smoke/report.md`
- screenshot: `/tmp/magic-jewel-broad-strict-abi25-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-broad-strict-abi25-smoke/new-sampled.log`
- validation status: `passed`
- fallback markers: `0`
- picture replay frames: `0`
- `EXPECT_MIN_TEXT_COMMANDS`: `8`
- `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS`: `6`
- `EXPECT_MIN_IMAGE_REFS`: `1`
- CMP command recorder: `frames=156 fps=52.0 avg_commands=3107 max_commands=3107 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=6.0 max_paragraph_text_commands=6 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=1.0 max_image_refs=1 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `156` / `156`
- JBR command timing: `frames=156 avg_total_ms=2.107 max_total_ms=3.401 avg_draw_ms=0.255 max_draw_ms=0.349 avg_flush_ms=1.828 max_flush_ms=3.154 avg_paragraph_ms=0.115 max_paragraph_ms=0.193 avg_paragraph_commands=6.0 max_paragraph_commands=6`
- screenshot assertion: `passed`

Next checkpoint:

- Run an intentionally wider incompatibility/fallback matrix with strict command mode disabled/enabled to confirm the fallback story is still disciplined after the ABI 25 text expansion. Include at least ABI mismatch, command-stream corruption, and unsupported text effect cases, with parseable markers for each.

### Checkpoint 73: Command-Corruption Fallback Validation

Status: completed for deterministic invalid-command fallback validation.

- Re-ran the existing Magic Jewel command-stream corruption path after ABI 25.
- The observed runtime behavior is:
  - CMP records a valid command stream before Skiko corruption mutates it.
  - Skiko emits command-frame markers with `rendered=false`.
  - JBR emits no command replay markers.
  - The mixed/picture fallback screenshot oracle passes.
- Updated the Magic Jewel report validator so `EXPECT_COMMAND_FALLBACK_REASON=command-stream-invalid` accepts the current parseable signal: `SKIKO_JBR_INTEROP_COMMAND_FRAME ... rendered=false` with zero JBR command frames. The older explicit fallback marker is still accepted if a future implementation emits it.

Command-corruption fallback Magic Jewel report:

- report: `/tmp/magic-jewel-command-corruption-fallback-smoke-2/report.md`
- screenshot: `/tmp/magic-jewel-command-corruption-fallback-smoke-2/new-window.png`
- sampled log: `/tmp/magic-jewel-command-corruption-fallback-smoke-2/new-sampled.log`
- validation status: `passed`
- fallback markers: `0`
- Skiko command frames: `553`, all invalid/corrupted frames reported as not rendered.
- JBR command frames: `0`
- screenshot assertion: `passed`

Next checkpoint:

- Add an ABI mismatch smoke that deliberately runs Skiko/CMP against an incompatible JBRSkia `ABI_ID` and validates a structured compatibility fallback before service/native work is attempted.

### Checkpoint 74: ABI-Mismatch Fallback Validation

Status: completed for deterministic compatibility-gate fallback validation.

- Added a Skiko test-only expected-ABI override (`skiko.jbr.interop.expectedAbiIdForTest`) so the runtime can exercise the real reflective discovery path against an intentionally incompatible expected ABI without rebuilding JBR.
- Verified the Skiko unit test path rejects a compatible ABI 25 JBR when the test override expects ABI 999 and emits `SKIKO_JBR_INTEROP_FALLBACK reason=abi-mismatch`.
- Wired the override through Magic Jewel's `run-jbr-skia.sh` and report harness.
- Updated the Magic Jewel validator so `EXPECT_COMMAND_FALLBACK_REASON=abi-mismatch` requires:
  - a structured `abi-mismatch` fallback marker in the full new-mode log,
  - zero Skiko command frames,
  - zero JBR command frames,
  - a passing mixed Swing/Compose screenshot assertion.
- Kept fallback marker counts based on the full log rather than only the sampled log so one-shot compatibility warnings emitted before the measurement window remain visible in the report.

ABI-mismatch fallback Magic Jewel report:

- report: `/tmp/magic-jewel-abi-mismatch-fallback-smoke-2/report.md`
- screenshot: `/tmp/magic-jewel-abi-mismatch-fallback-smoke-2/new-window.png`
- sampled log: `/tmp/magic-jewel-abi-mismatch-fallback-smoke-2/new-sampled.log`
- validation status: `passed`
- fallback markers: `1`
- CMP command recorder: `frames=689 fps=229.7 avg_commands=2480 max_commands=2481 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko command frames: `0`
- JBR command frames: `0`
- screenshot assertion: `passed`

Next checkpoint:

- Add the remaining fallback-matrix smoke for command capability mismatch or public API absence, then revisit the visually observed low-FPS case with a focused report that separates app draw markers, Swing repaint markers, and JBR command timing.

### Checkpoint 75: Command-Capability Fallback Validation

Status: completed for deterministic command-capability compatibility validation.

- Added a Skiko test-only required-command-capability override (`skiko.jbr.interop.requiredCommandCapabilitiesForTest`) so the runtime can exercise the command-capability gate against an intentionally impossible required mask without rebuilding JBR.
- Verified the Skiko unit test path rejects a compatible ABI 25 service when the test override requires capability mask `-1` and emits `SKIKO_JBR_INTEROP_FALLBACK reason=command-capability-mismatch`.
- Wired the override through Magic Jewel's `run-jbr-skia.sh` and report harness.
- Updated the Magic Jewel validator so `EXPECT_COMMAND_FALLBACK_REASON=command-capability-mismatch` requires:
  - a structured `command-capability-mismatch` fallback marker in the full new-mode log,
  - zero Skiko command frames,
  - zero JBR command frames,
  - a passing mixed Swing/Compose screenshot assertion.

Command-capability fallback Magic Jewel report:

- report: `/tmp/magic-jewel-command-capability-fallback-smoke/report.md`
- screenshot: `/tmp/magic-jewel-command-capability-fallback-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-command-capability-fallback-smoke/new-sampled.log`
- validation status: `passed`
- fallback markers: `1`
- CMP command recorder: `frames=453 fps=151.0 avg_commands=2480 max_commands=2481 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko command frames: `0`
- JBR command frames: `0`
- screenshot assertion: `passed`

Next checkpoint:

- Revisit the visually observed low-FPS case with a focused report that separates app draw markers, Swing repaint markers, JBR command timing, and any app-side throttling/focus behavior.

### Checkpoint 76: FPS Sanity Report

Status: completed for a focused strict command-mode timing sanity check.

- Re-ran Magic Jewel in strict command mode with the broad mixed-content probes enabled:
  - Compose text,
  - paragraph layout text,
  - image ref drawing,
  - transforms,
  - saveLayer,
  - intersect and difference clips.
- The run did not reproduce a 5 fps Skia replay path.
- Command replay markers were ~258 fps in the sampled window and JBR replay timing was ~1.49 ms total per replay, with ~0.26 ms in draw work and ~1.21 ms in flush.
- Swing repaint markers were ~27 fps, which means visually slow motion should be investigated as Swing/app scheduling, window focus/desktop behavior, or smoke-harness measurement cadence before treating JBR Skia replay as the bottleneck.
- Process CPU is still noisy and should remain a smoke-only signal because other agents/builds may be active on the machine.

FPS sanity Magic Jewel report:

- report: `/tmp/magic-jewel-fps-sanity-command-smoke/report.md`
- screenshot: `/tmp/magic-jewel-fps-sanity-command-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-fps-sanity-command-smoke/new-sampled.log`
- validation status: `passed`
- app draw markers: `old frames=1355 fps=271.0`, `new frames=1291 fps=258.2`
- Swing repaint markers: `old frames=139 fps=27.8`, `new frames=135 fps=27.0`
- CMP command recorder: `frames=1291 fps=258.2 avg_commands=3107 max_commands=3107 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=6.0 max_paragraph_text_commands=6 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=1.0 max_image_refs=1 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `1290` / `1291`
- JBR command timing: `frames=1290 avg_total_ms=1.488 max_total_ms=3.876 avg_draw_ms=0.255 max_draw_ms=1.320 avg_flush_ms=1.209 max_flush_ms=3.565 avg_paragraph_ms=0.116 max_paragraph_ms=1.049 avg_paragraph_commands=6.0 max_paragraph_commands=6`
- screenshot assertion: `passed`

Next checkpoint:

- Improve the app-side animation cadence story so the visible Jewel progress probes are paced by a stable animation clock instead of relying on accidental over-invalidation; then rerun the strict command smoke and compare visual cadence to the marker counts.

### Checkpoint 77: Magic Jewel Progress Cadence

Status: completed for app-side progress cadence cleanup.

- Moved the Magic Jewel `MAGIC_JEWEL_SWING_FRAME` marker from the Swing timer callback into `MovingSwingProgressBar.paintComponent`, so the report measures actual Swing progress-bar paints rather than timer firings.
- Changed the Swing progress position to derive from `System.nanoTime()` instead of an incremented timer counter. If timer callbacks are delayed or coalesced, the progress bar now advances according to wall-clock time on the next paint instead of appearing stuck at a stale phase.
- The Swing timer now repaints both the progress component and its parent panel, making the Swing island more robust inside the Compose-hosted `SwingPanel`.
- Renamed the report section to "Swing Progress Paint Markers" so future readers do not confuse these counts with generic Swing repaint scheduling.

Progress-cadence Magic Jewel report:

- report: `/tmp/magic-jewel-progress-cadence-command-smoke/report.md`
- screenshot: `/tmp/magic-jewel-progress-cadence-command-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-progress-cadence-command-smoke/new-sampled.log`
- validation status: `passed`
- app draw markers: `old frames=1236 fps=247.2`, `new frames=1132 fps=226.4`
- Swing progress paint markers: `old frames=1361 fps=272.2`, `new frames=1249 fps=249.8`
- CMP command recorder: `frames=1132 fps=226.4 avg_commands=3107 max_commands=3107 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=6.0 max_paragraph_text_commands=6 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=1.0 max_image_refs=1 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `1133` / `1132`
- JBR command timing: `frames=1132 avg_total_ms=1.478 max_total_ms=11.722 avg_draw_ms=0.298 max_draw_ms=4.039 avg_flush_ms=1.153 max_flush_ms=10.969 avg_paragraph_ms=0.136 max_paragraph_ms=1.993 avg_paragraph_commands=6.0 max_paragraph_commands=6`
- screenshot assertion: `passed`

Next checkpoint:

- Continue expanding strict command support or fallback coverage based on the next missing operation observed in CMP.

### Checkpoint 78: Public-API-Missing Fallback Validation

Status: completed for deterministic missing-public-accessor fallback validation.

- Added a Magic Jewel smoke path that points `JBR_API_SHIM` at a missing jar, leaving the patched `java.desktop` mirror visible while removing the public `com.jetbrains.JBR` accessor from Skiko's discovery path.
- Updated the Magic Jewel validator so `EXPECT_COMMAND_FALLBACK_REASON=public-api-missing` requires:
  - a structured `public-api-missing` fallback marker in the full new-mode log,
  - zero Skiko command frames,
  - zero JBR command frames,
  - a passing mixed Swing/Compose screenshot assertion.
- This complements the ABI mismatch and command-capability mismatch smokes: all three compatibility gates now have deterministic Magic Jewel coverage with parseable markers.

Public-API-missing fallback Magic Jewel report:

- report: `/tmp/magic-jewel-public-api-missing-fallback-smoke/report.md`
- screenshot: `/tmp/magic-jewel-public-api-missing-fallback-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-public-api-missing-fallback-smoke/new-sampled.log`
- validation status: `passed`
- fallback markers: `1`
- CMP command recorder: `frames=938 fps=312.7 avg_commands=2481 max_commands=2481 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko command frames: `0`
- JBR command frames: `0`
- screenshot assertion: `passed`

Next checkpoint:

- Add an explicit unsupported-operation fallback probe for `clipPath` or shader-backed paint, so the matrix includes both compatibility fallback and recorder-level operation fallback.

### Checkpoint 79: `clipPath` Unsupported-Operation Fallback

Status: completed for recorder-level unsupported-operation fallback validation.

- Added a Magic Jewel `MAGIC_JEWEL_COMPOSE_CLIP_PATH=true` probe that draws through Compose `clipPath`.
- CMP's existing command recorder marks `clipPath` unsupported and marks subsequent drawing in that scope as `unsupportedScope`.
- In strict command mode, Skiko receives no command stream for that frame and falls back to the existing picture replay path.
- The Magic Jewel validator already covers this operation-level fallback shape via `EXPECT_COMMAND_FALLBACK_REASON=clipPath`: zero Skiko/JBR command frames, positive Skiko/JBR picture replay frames, the expected CMP unsupported reason, and a passing mixed screenshot assertion.

`clipPath` fallback Magic Jewel report:

- report: `/tmp/magic-jewel-clippath-fallback-smoke/report.md`
- screenshot: `/tmp/magic-jewel-clippath-fallback-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-clippath-fallback-smoke/new-sampled.log`
- validation status: `passed`
- CMP command recorder: `frames=164 fps=54.7 avg_commands=2486 max_commands=2487 unsupported_frames=164 avg_unsupported=2.0 max_unsupported=2 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=unsupportedScope:164,clipPath:164`
- Skiko/JBR command frames: `0` / `0`
- Skiko/JBR picture replay frames: `164` / `164`
- screenshot assertion: `passed`

Next checkpoint:

- Add command support for `clipPath` so the operation-level fallback probe becomes a strict command replay probe, and keep the fallback report as historical coverage for pre-ABI-26 behavior.

### Checkpoint 80: `clipPath` Command Support And ABI 26

Status: completed for Compose `clipPath` command recording and JBR/Skia replay.

- Bumped the command ABI to `26` across JBR, public JBR API, Skiko, and CMP.
- Added command capability `COMMAND_CAP_CLIP_PATH` and command opcode `COMMAND_CLIP_PATH`.
- CMP serializes Compose paths into fixed-point path verb payloads using move/line/quad/cubic/close verbs and NonZero/EvenOdd fill metadata.
- Skiko requires the new clip-path capability for strict command mode compatibility, so older JBR builds fall back instead of accepting a partial renderer.
- JBR validates the variable-length path payload before replay, supports Java2D fallback clipping, and replays native Skia `clipPath` with intersect/difference clip operations.
- The previous Magic Jewel `MAGIC_JEWEL_COMPOSE_CLIP_PATH=true` unsupported-operation probe is now a strict command replay probe.

`clipPath` command Magic Jewel report:

- report: `/tmp/magic-jewel-clippath-command-abi26-smoke/report.md`
- screenshot: `/tmp/magic-jewel-clippath-command-abi26-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-clippath-command-abi26-smoke/new-sampled.log`
- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=425 fps=141.7 avg_commands=2518 max_commands=2518 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `426` / `426`
- JBR command timing: `frames=426 avg_total_ms=1.087 max_total_ms=3.252 avg_draw_ms=0.103 max_draw_ms=0.340 avg_flush_ms=0.964 max_flush_ms=3.136 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

### Checkpoint 81: Path Drawing Command Support And ABI 27

Status: completed for solid-color Compose path fill/stroke command recording and JBR/Skia replay.

- Bumped the command ABI to `27` across JBR, public JBR API, Skiko, and CMP.
- Added command capability `COMMAND_CAP_DRAW_PATH` and command opcode `COMMAND_DRAW_PATH`.
- Reused the ABI 26 serialized path verb payload for draw-path records.
- CMP records solid-color path fills and strokes with style, ARGB, stroke width/cap/join/miter, fill type, and path verbs; unsupported paint features still fall back through the existing strict-command guard.
- JBR validates the variable-length path payload before replay, supports Java2D fallback drawing, and replays native Skia `drawPath`.
- Magic Jewel now has `MAGIC_JEWEL_COMPOSE_DRAW_PATH=true`, drawing an orange filled path with a white stroke as an explicit strict-command probe.

Path drawing command Magic Jewel report:

- report: `/tmp/magic-jewel-drawpath-command-abi27-smoke/report.md`
- screenshot: `/tmp/magic-jewel-drawpath-command-abi27-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-drawpath-command-abi27-smoke/new-sampled.log`
- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=591 fps=197.0 avg_commands=2535 max_commands=2535 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `592` / `592`
- JBR command timing: `frames=592 avg_total_ms=1.232 max_total_ms=4.279 avg_draw_ms=0.136 max_draw_ms=0.655 avg_flush_ms=1.066 max_flush_ms=4.132 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

### Checkpoint 82: Arc Drawing Command Support And ABI 28

Status: completed for solid-color Compose arc fill/stroke command recording and JBR/Skia replay.

- Bumped the command ABI to `28` across JBR, public JBR API, Skiko, and CMP.
- Added command capability `COMMAND_CAP_DRAW_ARC` and command opcode `COMMAND_DRAW_ARC`.
- CMP records Compose `drawArc` calls with paint style, ARGB, bounds, start/sweep angles, `useCenter`, and stroke metadata.
- JBR validates the fixed-length arc payload, supports Java2D fallback drawing with `Arc2D`, and replays native Skia `drawArc`.
- Magic Jewel now has `MAGIC_JEWEL_COMPOSE_DRAW_ARC=true`, drawing a cyan pie arc with a white stroke as an explicit strict-command probe.

Arc drawing command Magic Jewel report:

- report: `/tmp/magic-jewel-drawarc-command-abi28-smoke/report.md`
- screenshot: `/tmp/magic-jewel-drawarc-command-abi28-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-drawarc-command-abi28-smoke/new-sampled.log`
- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=778 fps=259.3 avg_commands=2513 max_commands=2513 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `778` / `778`
- JBR command timing: `frames=778 avg_total_ms=1.207 max_total_ms=2.891 avg_draw_ms=0.119 max_draw_ms=0.245 avg_flush_ms=1.067 max_flush_ms=2.747 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

### Checkpoint 83: Round-Rect Drawing Command Support And ABI 29

Status: completed for solid-color Compose round-rect fill/stroke command recording and JBR/Skia replay.

- Bumped the command ABI to `29` across JBR, public JBR API, Skiko, and CMP.
- Added command capability `COMMAND_CAP_DRAW_ROUND_RECT` and command opcode `COMMAND_DRAW_ROUND_RECT`.
- CMP now records Compose `drawRoundRect` calls with paint style, ARGB, bounds, independent X/Y radii, and stroke metadata instead of approximating stroked round-rects as four straight lines.
- JBR validates the fixed-length round-rect payload, supports Java2D fallback drawing with `RoundRectangle2D`, and replays native Skia `drawRRect`.
- Magic Jewel now has `MAGIC_JEWEL_COMPOSE_DRAW_ROUND_RECT=true`, drawing a purple rounded rectangle with a white stroke as an explicit strict-command probe.

Round-rect drawing command Magic Jewel report:

- report: `/tmp/magic-jewel-roundrect-command-abi29-smoke/report.md`
- screenshot: `/tmp/magic-jewel-roundrect-command-abi29-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-roundrect-command-abi29-smoke/new-sampled.log`
- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=229 fps=76.3 avg_commands=2424 max_commands=2424 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `229` / `229`
- JBR command timing: `frames=229 avg_total_ms=1.225 max_total_ms=7.800 avg_draw_ms=0.112 max_draw_ms=0.787 avg_flush_ms=1.087 max_flush_ms=7.527 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

Next checkpoint:

- Validate the higher-risk shader-backed paint ABI with a narrow linear-gradient fill first.

### Checkpoint 84: Linear-Gradient Rect Command Support And ABI 30

Status: completed for serialized Compose linear-gradient rectangle fills.

- Bumped the command ABI to `30` across JBR, public JBR API, Skiko, and CMP.
- Added command capability `COMMAND_CAP_FILL_RECT_LINEAR_GRADIENT` and command opcode `COMMAND_FILL_RECT_LINEAR_GRADIENT`.
- CMP now preserves serializable metadata on Skiko-backed `LinearGradientShader` instances and records `drawRect(brush = Brush.linearGradient(...))` as command data instead of passing a Skiko `SkShader` pointer across runtimes.
- The serialized paint payload contains rect bounds, gradient start/end points, tile mode, 2..16 ARGB colors, and monotonic stops in fixed-point units scaled by 1000.
- JBR validates the variable-length payload, replays native Skia gradients through the m147 `SkGradient` / `SkShaders::LinearGradient` API, and keeps a Java2D fallback via `LinearGradientPaint`.
- Skiko requires ABI `30` plus the new command capability, preserving the strict mismatch fallback behavior before command replay starts.
- Magic Jewel now has `MAGIC_JEWEL_COMPOSE_LINEAR_GRADIENT=true`, drawing a green/blue/purple Compose gradient rectangle as the explicit shader-backed paint probe.

Validation completed so far:

- CMP focused recorder test: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Skiko focused interop test: `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- JBR Java service compile against the patched API stubs.
- JBR native bridge compile against pinned Skia `m147-64a2414108`.
- Magic Jewel strict command report: `OUT_DIR=/tmp/magic-jewel-linear-gradient-command-abi30-smoke JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_LINEAR_GRADIENT=true SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-interop-report.sh`

Linear-gradient command Magic Jewel report:

- report: `/tmp/magic-jewel-linear-gradient-command-abi30-smoke/report.md`
- screenshot: `/tmp/magic-jewel-linear-gradient-command-abi30-smoke/new-window.png`
- sampled log: `/tmp/magic-jewel-linear-gradient-command-abi30-smoke/new-sampled.log`
- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=8367 fps=418.4 avg_commands=2461 max_commands=2461 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `8368` / `8368`
- JBR command timing: `frames=8368 avg_total_ms=0.860 max_total_ms=2.685 avg_draw_ms=0.095 max_draw_ms=0.303 avg_flush_ms=0.749 max_flush_ms=2.566 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

Next checkpoint:

- Add strict command-stream validation for gradient fallbacks/edge cases (unsupported shader transforms, non-linear shaders, too many stops) and start the next shader-backed paint shape once the fallback surface is pinned.

### Checkpoint 85: 64-bit Command Capability Gate And ABI 31

Status: completed for widening command capability compatibility checks before adding more drawing operations.

- Bumped the command ABI to `31` across JBR, public JBR API, Skiko, and CMP.
- Added `JBRSkia.getCommandCapabilities64()` while keeping the existing `getCommandCapabilities()` low-bit accessor for compatibility/documentation.
- Skiko now performs the required command-capability gate against the 64-bit reflective accessor, avoiding the signed-`int` ceiling after ABI 30 consumed bit `1 << 30`.
- CMP command streams now carry ABI `31`; existing strict recorder coverage still passes, including the linear-gradient rect command and the opaque-shader strict fallback test.
- Magic Jewel linear-gradient command smoke still runs through strict command replay only, proving the widened gate did not change the runnable paint path.

Validation:

- Skiko focused interop test: `./gradlew :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- CMP focused recorder test: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- JBR Java service compile against patched stubs.
- JBR native bridge compile against pinned Skia `m147-64a2414108`.
- Public JBR API shim rebuild: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev && cp /tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar /tmp/jbr-api-shim.jar`
- Magic Jewel report: `/tmp/magic-jewel-linear-gradient-command-abi31-smoke/report.md`

ABI 31 Magic Jewel report:

- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=8155 fps=407.8 avg_commands=2461 max_commands=2461 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `8155` / `8155`
- JBR command timing: `frames=8155 avg_total_ms=0.996 max_total_ms=4.273 avg_draw_ms=0.099 max_draw_ms=1.302 avg_flush_ms=0.880 max_flush_ms=4.172 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

Next checkpoint:

- Use the 64-bit capability gate for the next shader-backed paint shape, preferably a gradient rounded rectangle or path, without consuming the sign bit of the legacy `int` mask.

### Checkpoint 86: Linear-Gradient Round-Rect Command Support And ABI 32

Status: completed for serialized Compose linear-gradient rounded-rectangle fills.

- Bumped the command ABI to `32` across JBR, public JBR API, Skiko, and CMP.
- Added 64-bit-only command capability `COMMAND_CAP64_FILL_ROUND_RECT_LINEAR_GRADIENT = 2147483648L`, proving the widened capability mask is now required for new commands past the signed `int` ceiling.
- Added command opcode `COMMAND_FILL_ROUND_RECT_LINEAR_GRADIENT = 25`.
- CMP now records `drawRoundRect(brush = Brush.linearGradient(...))` as command data when the paint is a supported fill-style linear gradient.
- The serialized payload contains rounded-rect bounds, independent X/Y radii, gradient start/end points, tile mode, 2..16 ARGB colors, and monotonic stops in fixed-point units scaled by 1000.
- JBR validates the variable-length payload, replays native Skia rounded gradients through `SkShaders::LinearGradient` plus `drawRRect`, and keeps a Java2D fallback via `LinearGradientPaint` plus `RoundRectangle2D`.
- Skiko requires ABI `32` and the new 64-bit capability bit before command replay starts, preserving strict fallback behavior on older JBR builds.
- Magic Jewel now has `MAGIC_JEWEL_COMPOSE_LINEAR_GRADIENT_ROUND_RECT=true`, drawing an orange/pink/cyan rounded gradient as the explicit post-`int`-capability shader-backed paint probe.

Validation:

- CMP focused recorder test: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Skiko focused interop test: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Public JBR API process/dev rebuild: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev && cp /tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar /tmp/jbr-api-shim.jar`
- JBR Java service compile against patched stubs and refresh of `/tmp/jbr-skia-run/desktop`.
- JBR native bridge compile against pinned Skia `m147-64a2414108`.
- Skiko snapshot publish: `./gradlew --no-configuration-cache :skiko:publishToMavenLocal`
- CMP patched UI desktop jars: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`
- Magic Jewel compile: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
- Magic Jewel report: `/tmp/magic-jewel-linear-gradient-roundrect-command-abi32-smoke/report.md`

ABI 32 Magic Jewel report:

- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=7002 fps=350.1 avg_commands=2430 max_commands=2430 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `7002` / `7002`
- JBR command timing: `frames=7002 avg_total_ms=1.082 max_total_ms=19.656 avg_draw_ms=0.114 max_draw_ms=1.751 avg_flush_ms=0.945 max_flush_ms=19.512 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

Next checkpoint:

- Continue expanding shader-backed paint coverage now that the 64-bit gate is exercised; likely next candidates are gradient path fill or radial/sweep gradient payloads.

### Checkpoint 87: Radial-Gradient Rect Command Support And ABI 33

Status: completed for serialized Compose radial-gradient rectangle fills.

- Bumped the command ABI to `33` across JBR, public JBR API, Skiko, and CMP.
- Added 64-bit-only command capability `COMMAND_CAP64_FILL_RECT_RADIAL_GRADIENT = 4294967296L`.
- Added command opcode `COMMAND_FILL_RECT_RADIAL_GRADIENT = 26`.
- CMP now preserves serializable metadata on Skiko-backed `RadialGradientShader` instances and records `drawRect(brush = Brush.radialGradient(...))` as command data.
- The serialized payload contains rect bounds, radial center, radius, tile mode, 2..16 ARGB colors, and monotonic stops in fixed-point units scaled by 1000.
- JBR validates the variable-length payload, replays native Skia radial gradients through `SkShaders::RadialGradient`, and keeps a Java2D fallback via `RadialGradientPaint`.
- Skiko requires ABI `33` and the new 64-bit capability bit before command replay starts.
- Magic Jewel now has `MAGIC_JEWEL_COMPOSE_RADIAL_GRADIENT=true`, drawing a cream/orange/purple radial-gradient rectangle as the explicit radial paint probe.
- Added `ROADMAP.md` as a quick-open checklist and documented why generic/opaque shader pointers remain out of scope until shader construction happens inside the JBR-owned Skia runtime.

Validation:

- CMP focused recorder test: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Skiko focused interop test: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Public JBR API process/dev rebuild: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev && cp /tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar /tmp/jbr-api-shim.jar`
- JBR Java service compile against patched stubs and refresh of `/tmp/jbr-skia-run/desktop`.
- JBR native bridge compile against pinned Skia `m147-64a2414108`.
- Skiko snapshot publish: `./gradlew --no-configuration-cache :skiko:publishToMavenLocal`
- CMP patched UI desktop jars: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`
- Magic Jewel compile: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
- Magic Jewel report: `/tmp/magic-jewel-radial-gradient-command-abi33-smoke/report.md`

ABI 33 Magic Jewel report:

- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=8383 fps=419.1 avg_commands=2460 max_commands=2460 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `8382` / `8382`
- JBR command timing: `frames=8382 avg_total_ms=0.869 max_total_ms=47.415 avg_draw_ms=0.099 max_draw_ms=8.547 avg_flush_ms=0.753 max_flush_ms=38.842 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

Next checkpoint:

- Add radial-gradient rounded-rectangle fills, then gradient path fills once the second radial shape proves the payload is reusable.

### Checkpoint 88: Radial-Gradient Round-Rect Command Support And ABI 34

Status: completed for serialized Compose radial-gradient rounded-rectangle fills.

- Bumped the command ABI to `34` across JBR, public JBR API, Skiko, and CMP.
- Added 64-bit-only command capability `COMMAND_CAP64_FILL_ROUND_RECT_RADIAL_GRADIENT = 8589934592L`.
- Added command opcode `COMMAND_FILL_ROUND_RECT_RADIAL_GRADIENT = 27`.
- CMP now records `drawRoundRect(brush = Brush.radialGradient(...))` as command data when the paint is a supported fill-style radial gradient.
- The serialized payload contains rounded-rect bounds, independent X/Y radii, radial center, radius, tile mode, 2..16 ARGB colors, and monotonic stops in fixed-point units scaled by 1000.
- JBR validates the variable-length payload, replays native Skia radial rounded gradients through `SkShaders::RadialGradient` plus `drawRRect`, and keeps a Java2D fallback via `RadialGradientPaint` plus `RoundRectangle2D`.
- Skiko requires ABI `34` and the new 64-bit capability bit before command replay starts.
- Magic Jewel now has `MAGIC_JEWEL_COMPOSE_RADIAL_GRADIENT_ROUND_RECT=true`, drawing a cyan/blue radial rounded-rectangle probe.

Validation:

- CMP focused recorder test: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Skiko focused interop test: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Public JBR API process/dev rebuild: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev && cp /tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar /tmp/jbr-api-shim.jar`
- JBR Java service compile against patched stubs and refresh of `/tmp/jbr-skia-run/desktop`.
- JBR native bridge compile against pinned Skia `m147-64a2414108`.
- Skiko snapshot publish: `./gradlew --no-configuration-cache :skiko:publishToMavenLocal`
- CMP patched UI desktop jars: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`
- Magic Jewel compile: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
- Magic Jewel report: `/tmp/magic-jewel-radial-gradient-roundrect-command-abi34-smoke/report.md`

ABI 34 Magic Jewel report:

- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=5769 fps=288.4 avg_commands=2429 max_commands=2429 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `5769` / `5769`
- JBR command timing: `frames=5769 avg_total_ms=0.885 max_total_ms=8.672 avg_draw_ms=0.099 max_draw_ms=0.796 avg_flush_ms=0.768 max_flush_ms=8.520 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

Next checkpoint:

- Move from rect-family shader fills to gradient path fills, starting with linear-gradient path fill because the path payload and linear shader payload are already independently proven.

### Checkpoint 89: Linear-Gradient Path Command Support And ABI 35

Status: completed for serialized Compose linear-gradient path fills.

- Bumped the command ABI to `35` across JBR, public JBR API, Skiko, and CMP.
- Added 64-bit-only command capability `COMMAND_CAP64_FILL_PATH_LINEAR_GRADIENT = 17179869184L`.
- Added command opcode `COMMAND_FILL_PATH_LINEAR_GRADIENT = 28`.
- CMP now records fill-style `drawPath(path, brush = Brush.linearGradient(...))` when the path and linear-gradient payloads are serializable under the strict command ABI.
- The serialized payload combines path fill type, path verb data, linear-gradient endpoints, tile mode, 2..16 ARGB colors, and monotonic stops in fixed-point units scaled by 1000.
- JBR validates the variable-length path-plus-gradient payload, replays native Skia linear-gradient paths through `SkShaders::LinearGradient` plus `drawPath`, and keeps a Java2D fallback via `LinearGradientPaint` plus `Path2D`.
- Skiko requires ABI `35` and the new 64-bit capability bit before command replay starts.
- Magic Jewel now has `MAGIC_JEWEL_COMPOSE_LINEAR_GRADIENT_PATH=true`, drawing a teal/yellow/pink Compose gradient path probe.
- `ROADMAP.md` now records ABI 35 and keeps generic shaders as a later JBR-owned shader-factory strategy rather than raw `SkShader*` pointer sharing.

Validation:

- CMP focused recorder test: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Skiko focused interop test: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Public JBR API process/dev rebuild: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev && cp /tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar /tmp/jbr-api-shim.jar`
- JBR Java service compile against patched stubs and refresh of `/tmp/jbr-skia-run/desktop`.
- JBR native bridge compile against pinned Skia `m147-64a2414108`.
- Skiko snapshot publish: `./gradlew --no-configuration-cache :skiko:publishToMavenLocal`
- CMP patched UI desktop jars: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`
- Magic Jewel compile: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
- Magic Jewel report: `/tmp/magic-jewel-linear-gradient-path-command-abi35-smoke/report.md`

ABI 35 Magic Jewel report:

- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=2642 fps=132.1 avg_commands=2441 max_commands=2441 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `2643` / `2643`
- JBR command timing: `frames=2643 avg_total_ms=1.524 max_total_ms=12.069 avg_draw_ms=0.188 max_draw_ms=3.604 avg_flush_ms=1.298 max_flush_ms=11.888 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

Next checkpoint:

- Continue shader-backed path coverage with radial-gradient path fill, then sweep-gradient payloads and stricter shader fallback tests.

### Checkpoint 90: Radial-Gradient Path Command Support And ABI 36

Status: completed for serialized Compose radial-gradient path fills.

- Bumped the command ABI to `36` across JBR, public JBR API, Skiko, and CMP.
- Added 64-bit-only command capability `COMMAND_CAP64_FILL_PATH_RADIAL_GRADIENT = 34359738368L`.
- Added command opcode `COMMAND_FILL_PATH_RADIAL_GRADIENT = 29`.
- CMP now records fill-style `drawPath(path, brush = Brush.radialGradient(...))` when the path and radial-gradient payloads are serializable under the strict command ABI.
- The serialized payload combines path fill type, path verb data, radial-gradient center, radius, tile mode, 2..16 ARGB colors, and monotonic stops in fixed-point units scaled by 1000.
- JBR validates the variable-length path-plus-radial-gradient payload, replays native Skia radial-gradient paths through `SkShaders::RadialGradient` plus `drawPath`, and keeps a Java2D fallback via `RadialGradientPaint` plus `Path2D`.
- Skiko requires ABI `36` and the new 64-bit capability bit before command replay starts.
- Magic Jewel now has `MAGIC_JEWEL_COMPOSE_RADIAL_GRADIENT_PATH=true`, drawing a cream/orange/purple Compose radial-gradient path probe.

Validation:

- CMP focused recorder test: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Skiko focused interop test: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Public JBR API process/dev rebuild: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev && cp /tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar /tmp/jbr-api-shim.jar`
- JBR Java service compile against patched stubs and refresh of `/tmp/jbr-skia-run/desktop`.
- JBR native bridge compile against pinned Skia `m147-64a2414108`.
- Skiko snapshot publish: `./gradlew --no-configuration-cache :skiko:publishToMavenLocal`
- CMP patched UI desktop jars: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`
- Magic Jewel compile: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
- Magic Jewel report: `/tmp/magic-jewel-radial-gradient-path-command-abi36-smoke/report.md`

ABI 36 Magic Jewel report:

- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=3217 fps=160.8 avg_commands=2461 max_commands=2461 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `3218` / `3218`
- JBR command timing: `frames=3218 avg_total_ms=1.320 max_total_ms=17.341 avg_draw_ms=0.131 max_draw_ms=2.283 avg_flush_ms=1.162 max_flush_ms=17.184 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

Next checkpoint:

- Add sweep-gradient payloads or, if we want to harden first, add shader edge-case fallback tests for transformed gradients, invalid stops, invalid radii, and excessive color counts.

### Checkpoint 91: Sweep-Gradient Rect Command Support And ABI 37

Status: completed for serialized Compose sweep-gradient rectangle fills.

- Bumped the command ABI to `37` across JBR, public JBR API, Skiko, and CMP.
- Added 64-bit-only command capability `COMMAND_CAP64_FILL_RECT_SWEEP_GRADIENT = 68719476736L`.
- Added command opcode `COMMAND_FILL_RECT_SWEEP_GRADIENT = 30`.
- CMP now preserves sweep-gradient shader metadata on desktop and records fill-style `drawRect(brush = Brush.sweepGradient(...))` when the payload is serializable under the strict command ABI.
- The serialized payload contains rectangle bounds, sweep center, 2..16 ARGB colors, and monotonic stops in fixed-point units scaled by 1000.
- JBR validates the variable-length sweep-gradient payload, replays native Skia sweep rectangles through `SkShaders::SweepGradient` plus `drawRect`, and keeps a Java2D fallback through a small JBR-owned `Paint`/`PaintContext` implementation.
- Skiko requires ABI `37` and the new 64-bit capability bit before command replay starts.
- Magic Jewel now has `MAGIC_JEWEL_COMPOSE_SWEEP_GRADIENT=true`, drawing a red/yellow/green/blue Compose sweep-gradient rectangle probe.

Validation:

- CMP focused recorder test: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Skiko focused interop test: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Public JBR API process/dev rebuild: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev && cp /tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar /tmp/jbr-api-shim.jar`
- JBR Java service compile against patched stubs and refresh of `/tmp/jbr-skia-run/desktop`.
- JBR native bridge compile against pinned Skia `m147-64a2414108`.
- Skiko snapshot publish: `./gradlew --no-configuration-cache :skiko:publishToMavenLocal`
- CMP patched UI desktop jars: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`
- Magic Jewel compile: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
- Magic Jewel report: `/tmp/magic-jewel-sweep-gradient-command-abi37-smoke/report.md`

ABI 37 Magic Jewel report:

- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=4238 fps=211.9 avg_commands=2460 max_commands=2460 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `4238` / `4238`
- JBR command timing: `frames=4238 avg_total_ms=1.382 max_total_ms=12.526 avg_draw_ms=0.162 max_draw_ms=2.798 avg_flush_ms=1.180 max_flush_ms=12.250 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

Next checkpoint:

- Continue sweep-gradient coverage with rounded rectangles or paths, then add stricter shader fallback tests for transformed gradients, invalid stops, invalid radii, and excessive color counts.

### Checkpoint 92: Sweep-Gradient Round-Rect Command Support And ABI 38

Status: completed for serialized Compose sweep-gradient rounded-rectangle fills.

- Bumped the command ABI to `38` across JBR, public JBR API, Skiko, and CMP.
- Added 64-bit-only command capability `COMMAND_CAP64_FILL_ROUND_RECT_SWEEP_GRADIENT = 137438953472L`.
- Added command opcode `COMMAND_FILL_ROUND_RECT_SWEEP_GRADIENT = 31`.
- CMP records fill-style `drawRoundRect(brush = Brush.sweepGradient(...))` when the sweep payload is serializable under the strict command ABI.
- The serialized payload contains rounded-rectangle bounds, corner radii, sweep center, 2..16 ARGB colors, and monotonic stops in fixed-point units scaled by 1000.
- JBR validates bounds, nonnegative radii, color-count limits, monotonic stops, and exact variable-length record sizing before replay.
- JBR native replay draws through `SkShaders::SweepGradient` plus `drawRRect`; the Java service fallback uses the JBR-owned `SweepGradientPaint` with `RoundRectangle2D`.
- Skiko requires ABI `38` and the new 64-bit capability bit before command replay starts.
- Magic Jewel now has `MAGIC_JEWEL_COMPOSE_SWEEP_GRADIENT_ROUND_RECT=true`, drawing a rounded sweep-gradient Compose probe.
- Follow-up visual-fidelity note: Magic Jewel's current text is still probe/default typography, not a full Jewel theme fidelity sample. Track a separate pass to make the validation app exercise real Jewel typography and components.

Validation:

- CMP focused recorder test: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Skiko focused interop test: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Public JBR API process/dev rebuild: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev && cp /tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar /tmp/jbr-api-shim.jar`
- JBR Java service compile against patched stubs and refresh of `/tmp/jbr-skia-run/desktop`.
- JBR native bridge compile against pinned Skia `m147-64a2414108`.
- Skiko snapshot publish: `./gradlew --no-configuration-cache :skiko:publishToMavenLocal`
- CMP patched UI desktop jars: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`
- Magic Jewel compile: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
- Magic Jewel report: `/tmp/magic-jewel-sweep-gradient-roundrect-command-abi38-smoke/report.md`

ABI 38 Magic Jewel report:

- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=6121 fps=306.1 avg_commands=2429 max_commands=2429 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `6121` / `6121`
- JBR command timing: `frames=6121 avg_total_ms=1.151 max_total_ms=24.048 avg_draw_ms=0.111 max_draw_ms=0.442 avg_flush_ms=1.017 max_flush_ms=23.900 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

Next checkpoint:

- Continue sweep-gradient coverage with path fills, then add stricter shader fallback tests for transformed gradients, invalid stops, invalid radii, excessive color counts, and nonserializable/generic shaders.

### Checkpoint 93: Sweep-Gradient Path Command Support And ABI 39

Status: completed for serialized Compose sweep-gradient path fills.

- Bumped the command ABI to `39` across JBR, public JBR API, Skiko, and CMP.
- Added 64-bit-only command capability `COMMAND_CAP64_FILL_PATH_SWEEP_GRADIENT = 274877906944L`.
- Added command opcode `COMMAND_FILL_PATH_SWEEP_GRADIENT = 32`.
- CMP records fill-style `drawPath(brush = Brush.sweepGradient(...))` when the path data and sweep payload are serializable under the strict command ABI.
- The serialized payload contains path fill type, path verb data, sweep center, 2..16 ARGB colors, and monotonic stops in fixed-point units scaled by 1000.
- JBR validates path payload length, path verb data, color-count limits, monotonic stops, and exact variable-length record sizing before replay.
- JBR native replay draws through `SkShaders::SweepGradient` plus `drawPath`; the Java service fallback uses the JBR-owned `SweepGradientPaint` with `Path2D`.
- Skiko requires ABI `39` and the new 64-bit capability bit before command replay starts.
- Magic Jewel now has `MAGIC_JEWEL_COMPOSE_SWEEP_GRADIENT_PATH=true`, drawing a sweep-gradient Compose path probe.

Validation:

- CMP focused recorder test: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Skiko focused interop test: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Public JBR API process/dev rebuild: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-dev && cp /tmp/jbr-api-skia-dev/jbr-api-SNAPSHOT.jar /tmp/jbr-api-shim.jar`
- JBR Java service compile against patched stubs and refresh of `/tmp/jbr-skia-run/desktop`.
- JBR native bridge compile against pinned Skia `m147-64a2414108`.
- Skiko snapshot publish: `./gradlew --no-configuration-cache :skiko:publishToMavenLocal`
- CMP patched UI desktop jars: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`
- Magic Jewel compile: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
- Magic Jewel report: `/tmp/magic-jewel-sweep-gradient-path-command-abi39-smoke-2/report.md`

ABI 39 Magic Jewel report:

- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=5291 fps=264.6 avg_commands=2461 max_commands=2461 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `5291` / `5291`
- JBR command timing: `frames=5291 avg_total_ms=1.110 max_total_ms=7.668 avg_draw_ms=0.123 max_draw_ms=1.787 avg_flush_ms=0.966 max_flush_ms=7.530 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

Next checkpoint:

- Add stricter shader fallback tests for transformed gradients, invalid stops, invalid radii, excessive color counts, and nonserializable/generic shaders before expanding into more complex shader constructs.

### Checkpoint 94: Strict Invalid-Gradient Fallback Tests

Status: completed for recorder-side gradient stop and color-count guardrails.

- CMP now rejects gradient stops unless they are finite, in `[0, 1]`, match the color count, and are strictly increasing. This aligns recorder behavior with JBR's stricter command validator and avoids producing command frames JBR would reject.
- CMP focused tests now cover:
  - non-monotonic linear-gradient stops,
  - excessive radial-gradient color counts,
  - duplicate sweep-gradient stops on path drawing.
- Magic Jewel now has `MAGIC_JEWEL_INVALID_SWEEP_GRADIENT=true`, a deliberate duplicate-stop sweep-gradient probe.
- Magic Jewel README/report plumbing documents the fallback smoke command:
  `JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_INVALID_SWEEP_GRADIENT=true EXPECT_COMMAND_FALLBACK=true EXPECT_COMMAND_FALLBACK_REASON=sweepGradientStops SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-interop-report.sh`

Validation:

- CMP focused recorder test: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- CMP patched UI desktop jars: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`
- Magic Jewel compile: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
- Magic Jewel report: `/tmp/magic-jewel-invalid-sweep-gradient-fallback-smoke/report.md`

Invalid sweep-gradient fallback report:

- validation status: `passed`
- expected fallback reason: `sweepGradientStops`
- Skiko/JBR picture replay frames: `894` / `894`
- CMP command recorder: `frames=894 fps=44.7 avg_commands=2373 max_commands=2373 unsupported_frames=894 avg_unsupported=1.0 max_unsupported=1 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=sweepGradientStops:894`
- Skiko/JBR command frames: `0` / `0`
- screenshot assertion: `passed`

Next checkpoint:

- Continue strict fallback coverage for invalid radii/nonfinite gradient geometry and transformed/nonserializable shader cases, then start the higher-level shader factory design if those guardrails stay stable.

### Checkpoint 95: Strict Invalid Gradient Geometry Tests

Status: completed for recorder-owned gradient geometry guardrails.

- CMP focused tests now cover invalid rounded-rectangle radii when the paint uses linear, radial, and sweep gradients.
- This pins the existing recorder behavior that rejects those commands before they reach JBR replay.
- Attempted nonfinite shader-coordinate coverage showed Skia can fail during shader construction (`Can't wrap nullptr`) before CMP has a `Shader` object to record. That case is below the recorder boundary and should be covered separately if we add a wrapper/factory around shader construction.

Validation:

- CMP focused recorder test: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`

Next checkpoint:

- Keep hardening fallback around shader cases that do produce Compose `Shader` objects, then move toward a documented JBR-owned shader factory design for anything beyond serialized known shader families.

### Checkpoint 96: Magic Jewel Uses Jewel Text Styling

Status: completed for the validation app typography correction.

- Magic Jewel labels now use Jewel's `Text` component instead of raw `BasicText`.
- Label styles now merge into `JewelTheme.defaultTextStyle`, so default font family and size come from the active Jewel theme while custom probe styles still override only the fields they need.
- This addresses the visual-fidelity issue where the sample looked like oversized default Compose/Swing typography instead of a Jewel-themed standalone app.

Validation:

- Magic Jewel compile: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
- Magic Jewel command-mode report: `/tmp/magic-jewel-jewel-text-command-smoke/report.md`

Jewel text command-mode report:

- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=7264 fps=363.2 avg_commands=2465 max_commands=2465 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `7264` / `7264`
- JBR command timing: `frames=7264 avg_total_ms=0.852 max_total_ms=2.787 avg_draw_ms=0.100 max_draw_ms=0.581 avg_flush_ms=0.736 max_flush_ms=2.679 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

Next checkpoint:

- Continue from visual smoke toward stable screenshot/text fidelity assertions, or return to rendering coverage with transformed/nonserializable shader fallback cases.

### Checkpoint 97: Native ABI Metadata Gate

Status: completed as a compatibility-hardening slice after ABI 39.

- Runtime API and JBR now expose `NATIVE_ABI_VERSION = 1` alongside the command-stream `ABI_ID` / `BUILD_ID`.
- The JBR service now reports runtime native metadata through `getNativeAbiVersion()`, `getNativeCommandStreamAbiId()`, and `getNativeBuildId()`.
- Skiko discovery now treats the public static command ABI and the service-reported native metadata as separate compatibility gates. A mismatch emits `SKIKO_JBR_INTEROP_FALLBACK reason=native-abi-mismatch` and falls back to the old Swing path before command acquisition.
- Skiko tests cover native ABI version mismatch, native command-stream ABI mismatch, native build id mismatch, and the test-only expected-native-ABI override.
- `ROADMAP.md` now marks the structured native C ABI version block complete.

Validation:

- Runtime API processor: `bash tools/build.sh process`
- Runtime API dev jar: `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-native-abi-dev`
- JBR service compile check: `javac --add-exports ... -cp /tmp/jbr-api-shim.jar ... JBRSkia.java JBRSkiaService.java`
- Skiko focused tests: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`

Next checkpoint:

- Add old/new compatibility matrix probes around the new native ABI gate, then continue into the remaining shader/fallback coverage and quiet-machine benchmark pass.

### Checkpoint 98: Pre-Native-Metadata Service Fallback

Status: completed as a Skiko compatibility-hardening test slice.

- Skiko now treats a service that passes the public static `ABI_ID` / `BUILD_ID` check but does not expose native metadata getters as `native-abi-mismatch`.
- This gives new Skiko an explicit marker when it runs against an older patched JBR/service shape instead of reporting a generic public API failure.
- The focused Skiko test suite now includes a fake legacy service with command capabilities but no native metadata getters.
- `ROADMAP.md` now records the new-Skiko/pre-native-metadata-JBR unit coverage separately from the remaining full launch matrix.

Validation:

- Skiko focused tests: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`

Next checkpoint:

- Build the full old/new launch matrix when we have packaged artifacts for both sides, and continue coverage on shader/fallback cases that are still outside the command subset.

### Checkpoint 99: Build Identity Carries Skia Revision And Flags

Status: completed as a strict-versioning cleanup.

- Runtime API and JBR now expose `SKIA_REVISION = m147-64a2414108` and `SKIA_FLAGS_HASH = macos-release-metal-poc:1`.
- `BUILD_ID` is now structured as `skia=<revision>;flags=<fingerprint>;abi=<commandAbi>;native=<nativeAbi>`.
- The initializer remains non-constant, so Skiko must still read it reflectively and compile-only consumers cannot safely inline stale compatibility values.
- The roadmap now marks the pinned Skia revision plus compile-flags hash item complete for the PoC configuration.

Validation:

- Runtime API processor: `bash tools/build.sh process`
- Runtime API dev jar: `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-build-id-dev`
- JBR service compile check: `javac --add-exports ... -cp /tmp/jbr-api-shim.jar ... JBRSkia.java JBRSkiaService.java`
- Magic Jewel structured-build-id report: `/tmp/magic-jewel-structured-build-id-smoke/report.md`

Structured-build-id command-mode report:

- validation status: `passed`
- fallback markers: `0`
- Skiko/JBR picture replay frames: `0` / `0`
- CMP command recorder: `frames=1821 fps=227.6 avg_commands=2465 max_commands=2465 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=0.0 max_image_refs=0 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=none`
- Skiko/JBR command frames: `1821` / `1821`
- JBR command timing: `frames=1821 avg_total_ms=1.111 max_total_ms=7.382 avg_draw_ms=0.114 max_draw_ms=0.802 avg_flush_ms=0.972 max_flush_ms=7.116 avg_paragraph_ms=0.000 max_paragraph_ms=0.000 avg_paragraph_commands=0.0 max_paragraph_commands=0`
- screenshot assertion: `passed`

Next checkpoint:

- Re-run the Magic Jewel command-mode smoke with the structured build id in the launch patch set, then continue shader/fallback coverage.

### Checkpoint 100: Native ABI Mismatch Launch Validation

Status: completed in Magic Jewel after publishing the refreshed Skiko snapshot to Maven local.

- Magic Jewel's launcher now forwards `SKIKO_EXPECTED_NATIVE_ABI_VERSION_FOR_TEST` to `-Dskiko.jbr.interop.expectedNativeAbiVersionForTest`.
- The report harness records the native-ABI override in generated reports.
- Strict fallback validation now knows `native-abi-mismatch` as a handshake fallback: it requires zero Skiko/JBR command frames and a structured fallback marker, but does not require a screenshot oracle.
- Report parser tests cover the native-ABI mismatch branch.
- The README now includes a native-ABI mismatch smoke command next to the command ABI/capability/public-API fallback probes.

Validation:

- Magic Jewel report parser tests: `bash scripts/test-jbr-skia-report-validation.sh`
- Skiko local publish refresh: `./gradlew :skiko:publishAwtPublicationToMavenLocal :skiko:publishAwtRuntimeElementsPublicationToMavenLocal :skiko:publishSkikoJvmRuntimeMacosArm64PublicationToMavenLocal`
- Magic Jewel native-ABI mismatch report: `/tmp/magic-jewel-native-abi-mismatch-smoke-2/report.md`

Native-ABI mismatch report:

- validation status: `passed`
- fallback markers: `1`
- Skiko/JBR command frames: `0` / `0`
- Skiko/JBR picture replay frames: `0` / `0`
- screenshot assertion: `not run`

Next checkpoint:

- Continue shader/fallback coverage, especially explicit non-gradient shader probes and eventual JBR-owned shader factory design notes.

### Checkpoint 101: Shader Boundary Fallback Probe

Status: completed in Magic Jewel as an explicit command-subset boundary validation.

- Magic Jewel now has `MAGIC_JEWEL_COMPOSE_IMAGE_SHADER=true` / `magic.jewel.compose.imageShader`.
- The probe draws a visible image placeholder and marks `shader` unsupported through the active command recorder. This is intentionally synthetic: a direct Compose `ImageShader` call hit mixed-runtime ABI drift in the current local jar set before the recorder could observe it.
- Strict command mode falls back to picture replay when the shader boundary marker is present.
- The README includes the shader fallback smoke command.

Validation:

- Magic Jewel compile: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
- Magic Jewel shader fallback report: `/tmp/magic-jewel-image-shader-fallback-smoke-3/report.md`

Shader fallback report:

- validation status: `passed`
- CMP command recorder: `frames=437 fps=87.4 avg_commands=2482 max_commands=2482 unsupported_frames=437 avg_unsupported=1.0 max_unsupported=1 avg_text_commands=9.0 max_text_commands=9 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=0 avg_image_refs=1.0 max_image_refs=1 avg_image_cache_clears=0.0 max_image_cache_clears=0 reasons=shader:437`
- Skiko/JBR command frames: `0` / `0`
- Skiko/JBR picture replay frames: `437` / `437`
- screenshot assertion: `passed`

Next checkpoint:

- Keep generic shader support as a later JBR-owned shader factory design task; continue macOS MVP hardening with full compatibility matrix packaging and quieter benchmark/report runs.

### Checkpoint 102: Handshake Fallback Parser Matrix

Status: completed in Magic Jewel report validation.

- `scripts/test-jbr-skia-report-validation.sh` now covers every handshake-style fallback branch that the report harness validates specially:
  - `abi-mismatch`
  - `native-abi-mismatch`
  - `command-capability-mismatch`
  - `public-api-missing`
  - `command-stream-invalid`
- The parser tests also reject a bogus handshake fallback report if Skiko/JBR command replay frames are present during an expected ABI mismatch.
- This keeps launch-report validation honest for old/new local artifact mixes where the correct result is "clear marker, no replay".

Validation:

- Magic Jewel report parser tests: `bash scripts/test-jbr-skia-report-validation.sh`

Next checkpoint:

- Convert the synthetic/parser compatibility coverage into a full packaged artifact matrix once we have named local old/new Skiko and JBR API/JBR patch bundles.

### Checkpoint 103: Machine-Readable Report Summary

Status: completed in Magic Jewel report validation.

- `scripts/jbr-skia-interop-report.sh` now writes `summary.properties` beside `report.md`.
- The summary uses stable `key=value` entries for automation:
  - `validation_status`
  - `validation_failures`
  - `fallback_new_count`
  - `cmp_unsupported_reasons`
  - `skiko_picture_frames` / `jbr_picture_frames`
  - `skiko_command_frames` / `jbr_command_frames`
  - `screenshot_status`
- Parser tests now assert the machine-readable summary for passing strict command reports, failed validation reports, picture fallback reports, and handshake fallback reports.
- Magic Jewel README documents the summary artifact.
- `ROADMAP.md` marks the CI-friendly report summary complete.

Validation:

- Magic Jewel report parser tests: `bash scripts/test-jbr-skia-report-validation.sh`

Next checkpoint:

- Add async-profiler hooks to the report harness, then defer quiet-machine benchmark collection until the host load is predictable.

### Checkpoint 104: Optional Async-Profiler Report Hooks

Status: completed in Magic Jewel report harness.

- `scripts/jbr-skia-interop-report.sh` now accepts:
  - `ENABLE_ASPROF=true`
  - `ASPROF=/path/to/asprof`
  - `ASPROF_EVENT=cpu` or another async-profiler event
- Profiling starts after warmup for each mode and stops before process teardown.
- If profiling is disabled or `asprof` is unavailable, the report records that status and continues.
- `report.md` now has an `Async Profiler` section.
- `summary.properties` now includes `asprof_old_status` and `asprof_new_status`.
- Magic Jewel README documents the optional profiler mode.
- `ROADMAP.md` marks async-profiler integration complete; quiet-machine benchmark collection remains open.

Validation:

- Magic Jewel report parser tests: `bash scripts/test-jbr-skia-report-validation.sh`
- Dry-run report generation with profiler env enabled but unavailable: `DRY_RUN=true OUT_DIR=/tmp/magic-jewel-asprof-dry-run ENABLE_ASPROF=true ASPROF=/tmp/not-asprof SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-interop-report.sh`

Next checkpoint:

- Keep quiet-machine benchmark collection open, and move to screen/context invalidation or packaged old/new artifact matrix work while the host remains noisy.

### Checkpoint 105: Scope Identity Read By Skiko

Status: completed in Skiko interop unit coverage.

- JBR already exposes a monotonic `getScopeId()` on each scoped paint canvas.
- Skiko now reads that scope id through the reflective `ScopedCanvas` wrapper.
- The scope-acquired diagnostic marker now includes both `scopeId=...` and `metalTexture=...`, giving the report/log stream a stable place to hang future context/surface invalidation telemetry.
- This checkpoint does not claim full screen-migration invalidation yet. It only wires the current scope identity across the JBR/Skiko Java boundary and keeps the production cache-invalidation work visible.

Validation:

- Skiko JBR interop unit test: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`

Next checkpoint:

- Add explicit old/new packaged-artifact fallback matrix coverage, then return to real context/surface invalidation once the JBR scope exposes a stable destination-context identity rather than only per-paint scope ids and Metal texture pointers.

### Checkpoint 106: Compatibility Matrix Report Fixtures

Status: completed in Magic Jewel report validation.

- `scripts/test-jbr-skia-report-validation.sh` now has explicit fixtures for the two high-risk version skew stories:
  - new Skiko against old/pre-native-metadata JBR accepts only a structured `SKIKO_JBR_INTEROP_FALLBACK reason=native-abi-mismatch` marker and zero command frames.
  - old or otherwise uninstrumented Skiko-style logs, with neither command frames nor a structured fallback marker, fail validation instead of being treated as a clean fallback.
- Magic Jewel README documents the matrix behavior beside the report/summary schema.
- This is parser-level coverage. Full launch-level artifact matrix coverage still needs real packaged old/new JBR and Skiko artifacts.

Validation:

- Magic Jewel report parser tests: `bash scripts/test-jbr-skia-report-validation.sh`

Next checkpoint:

- Add the stable destination-context identity needed for true cache invalidation on resize/surface migration, then wire Skiko cache invalidation to that identity rather than per-paint scope ids.

### Checkpoint 107: Stable Surface Identity Hook

Status: completed across JBR, Runtime API, Skiko, and CMP command-stream constants.

- ABI moved from `39/native=1` to `40/native=2` because the scoped canvas contract now exposes a new destination identity method.
- Runtime API and JBR `ScopedSkiaCanvas` now expose `getSurfaceId()`.
- JBR's macOS service currently returns the Java2D accelerated surface native-ops pointer as the surface id, or `0` for software/non-Metal test surfaces.
- Skiko reads `surfaceId` reflectively, includes it in `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED`, and compares it at `acquireCanvas` time.
- When Skiko sees the destination identity change, it logs `SKIKO_JBR_INTEROP_SURFACE_CHANGED ...` and drops the temporary diagnostic `DirectContext` cache.
- CMP and Skiko command-stream constants/tests were bumped to ABI `40` so recorder output remains accepted by JBR validation.

Validation:

- Runtime API processing: `bash tools/build.sh process`
- Skiko JBR interop unit test: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- CMP recorder compile check: `./gradlew --no-configuration-cache :compose:ui:ui-graphics:compileKotlinDesktop`
- Attempted CMP recorder desktop test: `./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`; this reached `ui-graphics` compilation but failed later in downstream `compose:ui:ui:compileKotlinDesktop` on existing unresolved Skiko JBR delegate imports.
- Attempted standalone JBR `javac` patch-module compile of `JBRSkiaApiTest`; this is not a valid lightweight check for this tree because patching `java.desktop` alone pulls broader JBR sources requiring `java.base/com.jetbrains.exported`, preview APIs, and additional platform modules.

Next checkpoint:

- Re-publish the ABI 40 Runtime API/Skiko/CMP artifacts locally, refresh the Magic Jewel launch wiring, and run the command-mode smoke so the screenshot/report path proves the new surface identity in a real paint.

### Checkpoint 108: ABI 40 Magic Jewel Smoke

Status: completed in Magic Jewel command-mode report.

- Runtime API dev jar was rebuilt and `/tmp/jbr-api-shim.jar` refreshed.
- JBR patched classes were refreshed under `/tmp/jbr-skia-run/desktop` using the temp-only `JBRApi` compile stub.
- JBR native `libjbrskiainterop.dylib` was rebuilt with native command ABI `40`; the native bridge had its hardcoded command ABI updated from `39` to `40`.
- Skiko ABI 40 artifacts were published to Maven local.
- CMP `ui-graphics` desktop jar was refreshed with command-stream ABI `40`.
- Magic Jewel command-mode smoke passed at `/tmp/magic-jewel-abi40-surface-identity-smoke/report.md`.
- The smoke recorded:
  - `validation_status=passed`
  - `fallback_new_count=0`
  - `cmp_recorder_frames=806`
  - `skiko_command_frames=805`
  - `jbr_command_frames=805`
  - `screenshot_status=passed`
- The first live acquisition marker included the new stable destination identity:
  - `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED abi=40 build=skia=m147-64a2414108;flags=macos-release-metal-poc:1;abi=40;native=2 scopeId=1 surfaceId=0xa35a48000 metalTexture=0xa35484500`

Validation:

- Runtime API dev jar: `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-abi40-dev`
- JBR patched class refresh into `/tmp/jbr-skia-run/desktop`
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`
- Skiko Maven-local publish: `./gradlew --no-configuration-cache :skiko:publishAwtPublicationToMavenLocal :skiko:publishAwtRuntimeElementsPublicationToMavenLocal :skiko:publishSkikoJvmRuntimeMacosArm64PublicationToMavenLocal`
- CMP jar refresh: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar`
- Magic Jewel compile: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
- Magic Jewel smoke: `OUT_DIR=/tmp/magic-jewel-abi40-surface-identity-smoke DURATION_SECONDS=5 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands ./scripts/jbr-skia-interop-report.sh`

Next checkpoint:

- Make the surface-change marker parseable in Magic Jewel reports, then add a resize/surface-change smoke that proves Skiko notices identity changes and invalidates cached surface state.

Use separate worktrees for every existing repo touched:

- `JetBrainsRuntime` worktree: `jbr-skia-compose-poc`
- `JetBrainsRuntimeApi` worktree: `jbr-api-skia-poc`
- `skiko` worktree: `skiko-jbr-skia-poc`
- `compose-multiplatform-core` worktree: `cmp-jbr-skia-poc`
- standalone validation project: `/Users/rock3r/src/magic-jewel`

Create a new local demo project at `~/src/magic-jewel` for the standalone Jewel app. This is the validation harness, not one of the three modified upstream repos.

Primary target: **macOS + Metal + direct canvas path**. JCEF, video/external surfaces, and shared texture interop are v2 nice-to-haves and must not block Compose.

## Key Decisions

- The PoC does **not** attempt a full Skia Java2D renderer. JBR keeps using the existing macOS Java2D/Metal rendering path and creates a scoped Skia canvas over the destination Metal render target during Swing paint.
- The fast path uses **one Skia runtime for all Skia C++ object pointers**. Any `SkCanvas*`, `GrDirectContext*`, `SkTypeface*`, `SkFont*`, `SkImage*`, or related pointer used during the fast path must be created by the same JBR-provided Skia runtime.
- Skiko's current JVM runtime statically links Skia archives. The PoC therefore requires a new Skiko native artifact/build mode: a Skia-less JBR interop shim that resolves required Skia entry points through JBR, instead of loading or calling Skiko's bundled Skia for the fast path.
- Text is in scope for the fast path. Typeface/font creation must route through the same JBR-provided Skia runtime; mixing Skiko-bundled `SkTypeface*` with a JBR-owned canvas is unsupported and must be rejected.
- The canvas coordinate system for interop is **Swing user space**. JBR applies the current `Graphics2D` transform, HiDPI scale, and clip before handing the scoped canvas to Skiko. Skiko's fast-path redrawer must not re-apply device scale to the acquired canvas.
- ABI compatibility is exact-match only for the PoC. A mismatch produces one structured warning and falls back to the old behavior.
- JBR's existing `SharedTextures` API is acknowledged but not used for v1. Direct canvas is preferred because it preserves Swing paint order, lightweight component layering, clipping, and `RenderSettings.SwingGraphics` semantics. Shared texture interop remains the v2 path for cached layers, JCEF, video, or external surfaces.

## Implementation Changes

### JBR

- Bootstrap Skia into the JDK build as new native third-party/runtime code. This includes vendoring/pinning Skia source or a reproducible prebuilt, wiring `make/`, exposing legal metadata, producing native symbols usable by the bridge, and handling Skia lifecycle.
- Gate the native Skia interop build behind `bash configure --with-skia-interop=<path-or-bundled>`. Builds without this flag must not compile or package the PoC Skia interop runtime.
- Add opt-in interop behind `-Dsun.java2d.skia.interop=true`. Do not use `-Dsun.java2d.skia=true` for the PoC, to avoid implying a full Java2D pipeline replacement.
- Add a private JBR API under `com.jetbrains.desktop`, registered through the existing `@JBRApi.Service` + `@JBRApi.Provides` + `JBRApi.internalService()` mechanism. Use existing naming conventions: `JBRSkia` / `JBRSkiaService`, not a flat static `JbrSkiaBridge`.
- `JBRSkia` exposes:
  - static final non-constant `ABI_ID`
  - static final non-constant `BUILD_ID`
  - `acquireCanvas(Graphics2D): ScopedSkiaCanvas?`
  - `ScopedSkiaCanvas` with opaque scope id, backend `METAL`, canvas pointer, direct context pointer, pixel format, color space id, sample count, user-space clip, flush, and close.
- Availability follows existing JBR service conventions:
  - `JBRSkiaService` constructor performs platform/backend validation.
  - unavailable cases throw `JBRApi.ServiceNotAvailableException`.
  - `JBRApi.internalService(JBRSkia.class)` returns null to clients when unavailable.
  - `acquireCanvas` returns null only when the service exists but the specific paint destination/scope is not compatible.
  - static `ABI_ID` and `BUILD_ID` must be mirrored into the public JBR API jar so Skiko can read them before acquiring the service.
  - `ABI_ID` and `BUILD_ID` must use non-constant initializers on both JBR-side and public-jar mirror classes, for example `Integer.parseInt("1")` and a static helper/string builder, so `javac` cannot inline stale values into Skiko or other `compileOnly` clients.
- For local PoC and tests only, `JBRApiSupport` may load a replacement public registry from `-Djetbrains.runtime.api.registry=<file>` when `-Djetbrains.runtime.api.extendRegistry=true` is also enabled. Product/runtime validation should use the generated `META-INF/jbrapi.public` from a real JBR image.
- Publish a native C ABI for Skiko to validate and call the runtime without sharing C++ ABI assumptions:
  - ABI id
  - Skia revision
  - Skia compile flags hash
  - JBR build id
  - backend id
  - Metal device, command queue, pixel format, color space, sample count
  - required canvas/font/typeface/image operations for the PoC.
  - Metal device, command queue, and pixel format are exposed on the C ABI for handshake/identity verification only; Skiko must not submit work to the queue directly.
  - color space and sample count are informational; Skiko may use them to align Compose render settings but must not construct independent Skia objects from them.
- Pin the Skia revision/config for the PoC by using the exact Skia revision selected for the Skiko worktree. Record it in `BUILD_ID` as `skiaRevision + skiaFlagsHash + ABI_ID`.
- Scope rules:
  - `acquireCanvas` returns non-null only during Swing painting on EDT against a compatible Metal destination.
  - the scope is tied to the destination's current `MTLGraphicsConfig` / per-screen `MTLContext`; screen migration, display changes, resize, or surface loss invalidate Skiko cached state bound to the previous context/surface.
  - Skiko detects context/surface changes at `acquireCanvas` time by comparing the returned scope's `MTLContext` identifier or direct-context pointer against cached state, then discards caches keyed to the previous context.
  - nested acquisition is supported only as stack-disciplined scopes on the same EDT for the same destination; reentrant acquisition for a different destination returns null and logs a diagnostic.
  - JBR performs `save()` before returning and `restoreToCount()` on close.
  - JBR asserts save-count balance on close; in release builds it restores and logs a structured error, in debug builds it may fail fast.
  - Skiko must not retain canvas/context/font/typeface pointers or submit work after close.
- Flush/ordering:
  - Skiko may issue Skia draw calls only within the scope.
  - Skiko may request a flush through the scope, but JBR owns final flush/ordering before AWT frame submission.
  - the Skia render target must preserve existing destination contents at scope acquisition; it must load existing pixels, never clear the target.
  - closing the scope flushes/commits Skia work on JBR's owned Metal/Skia context so it is ordered before any subsequent Java2D Metal commands targeting that surface in the same paint pass.
  - closing the scope marks the Java2D surface dirty and preserves Swing paint order for siblings and parent post-paint hooks.
- Pipeline flag behavior:
  - on non-macOS, `sun.java2d.skia.interop=true` is ignored with a one-time diagnostic and old behavior remains.
  - if Metal is disabled by existing macOS Java2D flags, interop is unavailable.
  - OpenGL/XRender/Vulkan Java2D flags do not participate in the PoC fast path.
- Note third-party duplication risk: JBR already carries HarfBuzz for font code, while Skia may bring its own HarfBuzz. The PoC must document symbol/linkage isolation for these native dependencies.

### Skiko

- Treat Skiko as the load-bearing repo for the PoC. The current macOS runtimes link `libskiko` dylibs for arm64 and x64 with many static Skia/native archives; add a new macOS JBR interop native artifact that does **not** link bundled Skia.
- Add conditional native build branches in Skiko's native/JVM task configuration:
  - normal runtime remains unchanged
  - JBR interop runtime builds a small JNI/C shim
  - the shim resolves JBR's C ABI at runtime and calls only JBR-provided Skia operations for fast-path objects.
- Discover JBR from JVM code through the public JBR API surface, not `JBRApi.internalService()`.
  - Preferred PoC path: reflection on the public `com.jetbrains.JBR` accessor from the public JBR API jar, e.g. discover `getJBRSkia` once that public accessor is added.
  - Acceptable alternative: `compileOnly` dependency on the public JBR API jar, with runtime absence handled gracefully.
  - Do not call `JBRApi.internalService()` from Skiko; it is caller-sensitive and only works from the JBR-side `@JBRApi.Provided` interface.
  - The normal Skiko runtime must remain loadable on non-JBR and older-JBR runtimes.
- Discovery order:
  - `Class.forName("com.jetbrains.JBRSkia")` from the public JBR API jar and read static `ABI_ID` / `BUILD_ID`; the current local Skiko PoC also accepts `com.jetbrains.desktop.JBRSkia` as a patched-runtime fallback until the public API jar is wired normally.
  - read those static fields reflectively, for example `clazz.getDeclaredField("ABI_ID").get(null)`, never through direct compiled field references.
  - perform compatibility checks before acquiring the service.
  - only after compatibility passes, reflect on `com.jetbrains.JBR.getJBRSkia()` or the final public accessor name.
  - only after a non-null service is returned, attempt `acquireCanvas(Graphics2D)`.
  - incompatible runtimes must fail before loading service-side native code.
  - reflective discovery failures are normal "interop unavailable" signals: `ClassNotFoundException`, `NoSuchMethodException`, `NoSuchFieldException`, `IllegalAccessException`, and `InvocationTargetException`; emit the fallback marker and use the old `SwingGraphics` path.
  - if Skiko uses the acceptable `compileOnly` public JBR API mode, link-time failures such as `NoSuchMethodError`, `NoSuchFieldError`, and `LinkageError` are also normal "interop unavailable" signals.
  - even in `compileOnly` mode, Skiko must read `ABI_ID` and `BUILD_ID` reflectively for the compatibility gate and must not compile direct references to those fields.
- Add exact compatibility checks before enabling the fast path:
  - ABI id equals `1`
  - backend is `METAL`
  - Skia revision matches the local Skiko PoC revision
  - Skia flags hash matches
  - Metal device and command queue are present.
- If any check fails:
  - emit structured marker `SKIKO_JBR_INTEROP_FALLBACK reason=<reason>`
  - increment fallback counter
  - use the old `SwingGraphics` behavior.
- Add a new Swing component/redrawer path parallel to existing `SkiaSwingLayer`:
  - receives the `Graphics2D` from Swing paint
  - acquires `JBRSkia.ScopedSkiaCanvas`
  - renders Compose into the externally-owned canvas
  - closes the scope before returning from paint.
- Route all fast-path text/font/typeface creation through JBR-provided Skia functions. Do not pass `SkTypeface*` or related objects from the bundled Skiko runtime into the JBR canvas.
- Existing Skiko external-pointer APIs for Metal (`DirectContext.makeMetal`, `BackendRenderTarget.makeMetal`, `Surface.makeFromBackendRenderTarget`) are useful references, but the PoC must avoid crossing raw Skia C++ objects between two runtimes.
- `skiko.jbr.interop.renderToTexture=true` is a diagnostic-only crash-prone probe that attempts to wrap the JBR destination texture with Skiko's bundled Metal context. It must remain disabled by default and must not be treated as the target architecture.
- Add diagnostics counters:
  - fast-path frames
  - fallback frames
  - ABI mismatch fallback count
  - CPU bitmap upload/readback count where observable.

### Compose Multiplatform Core

- Keep CMP changes intentionally small. The current expensive path is in Skiko's Swing layer; CMP should mainly choose the Skiko component/redrawer.
- Update `RenderSettings.SwingGraphics` / `SwingSkiaLayerComponent` routing:
  - if JBR Skia interop is enabled and compatible, use the new Skiko JBR interop Swing component/redrawer
  - otherwise use the existing `SkiaSwingLayer` path unchanged.
- Preserve existing Swing behavior:
  - paint order stays Swing-driven
  - repaint scheduling still comes from `SwingComposeScene` calling Swing repaint
  - no heavyweight native layer
  - popups/tooltips remain current CMP behavior for the PoC.
- Add `useJbrSkiaInteropInComposePanel` in `ComposeFeatureFlags.desktop.kt` alongside the existing Swing graphics feature flag.
  - intended meaning: enable JBR Skia direct-canvas fast path for `ComposePanel(RenderSettings.SwingGraphics)`
  - backing system property: `compose.swing.render.on.jbr.skia`
  - default false for PoC unless explicitly enabled by Magic Jewel scripts.

### Magic Jewel Demo

- Create `~/src/magic-jewel` as a standalone Jewel app using local-built CMP and Skiko artifacts plus the local JBR.
- Include launch modes:
  - `old-path`: current `RenderSettings.SwingGraphics` fallback behavior
  - `new-path`: JBR Skia direct-canvas path
  - optional side-by-side mode if practical without distorting measurements.
- Scripts must set explicit `JAVA_HOME` to the local JBR build and pass required JVM/system properties:
  - `run-old`
  - `run-new`
  - `profile-old`
  - `profile-new`
  - `report`
- App content must stress previously expensive cases:
  - animated gradients
  - scrolling text/editor-like content
  - animated icons/shapes
  - translucent overlays
  - mixed Swing controls around/over ComposePanel
  - Jewel menus/popups
  - FPS counter
  - CPU and memory usage meters in-app.
- Report emits before/after:
  - async-profiler wall-clock and CPU profile links
  - average/max process CPU from `ps`, labeled as noisy sanity data
  - RSS/memory summary
  - in-app FPS/CPU/memory summary
  - Skiko fast-path/fallback frame counts
  - parsed fallback markers
  - short text conclusion.

## Test Plan

- JBR:
  - build macOS JBR with `bash configure --with-skia-interop=<path-or-bundled>` and runtime `-Dsun.java2d.skia.interop=true`
  - smoke test Swing window paint with Metal enabled
  - verify `JBRSkiaService` is registered with `@JBRApi.Service` and `@JBRApi.Provides`
  - verify a synthetic test-only `@JBRApi.Provided("JBRSkia")` interface can resolve the provider through `JBRApi.internalService()`
  - verify an external-client test reads static `ABI_ID` / `BUILD_ID` reflectively via `Class.forName("com.jetbrains.desktop.JBRSkia")`
  - verify an external-client test acquires `JBRSkia` via reflection on `com.jetbrains.JBR.getJBRSkia()` or the final public accessor name and receives a non-null service when JBR is compatible
  - verify unavailable provider construction throws `JBRApi.ServiceNotAvailableException` and clients observe a null service
  - verify `acquireCanvas` returns non-null only in valid paint scope
  - verify non-macOS/Metal-disabled cases warn once and return null
  - verify transform, clip, and save count are restored after scope close
  - verify resize/surface loss/screen migration invalidates prior scoped objects.
  - verify Skia draw preserves existing Java2D destination contents and is ordered before later Java2D commands in the same Swing paint pass.
- Skiko:
  - unit test exact ABI match succeeds
  - unit test missing JBR falls back
  - unit test ABI mismatch emits `SKIKO_JBR_INTEROP_FALLBACK reason=abi-mismatch`
  - native smoke test wraps external canvas without owning/destroying it
  - test that bundled Skia is not used for JBR-created pointers
  - text smoke test proves typeface/font creation uses the JBR Skia runtime on the fast path.
- CMP:
  - ComposePanel test confirms fast path selects the new Skiko component with compatible JBR/Skiko
  - ComposePanel test confirms old path remains used without compatible JBR
  - animation test runs without user input for 60 seconds and asserts at least 80% of `displayRefreshRate * duration`, with refresh rate captured from `java.awt.DisplayMode` at test start; if the refresh rate is `DisplayMode.REFRESH_RATE_UNKNOWN`, use 60 Hz for the threshold
  - visual smoke test for clipping, resizing, repaint, HiDPI, and popup opening.
- Magic Jewel:
  - run old and new modes for the same scripted 60-second scenario
  - collect async-profiler and `ps` samples
  - fail report if new mode records zero fast-path frames
  - fail report if an ABI mismatch does not produce a structured fallback marker
  - compare screenshots for obvious rendering breakage.

## Risks And Defaults

- Highest risk: Skiko build surgery for a Skia-less JBR interop native artifact. There is no existing external-Skia build mode, so this is the first PoC milestone.
- Second highest risk: font/typeface ownership. Fast-path text is only valid if all Skia text objects come from the same JBR Skia runtime.
- JBR Skia integration is greenfield: vendoring/building Skia inside the JDK build is real infrastructure work, not a small hook.
- Metal handoff is tractable because both sides already have relevant Metal concepts, but it must be bound to the destination's current per-screen `MTLContext`.
- macOS/Metal is the only required PoC platform.
- Direct canvas is v1 and productionization candidate.
- Shared texture, JCEF, and video/external surfaces are v2 nice-to-haves.
- `ABI_ID` covers pointer types exposed, native C ABI layout, scope layout, lifecycle rules, threading rules, and Metal object contract; Skia revision and Skia compile flags hash are tracked separately in `BUILD_ID`.
- On any incompatibility, the user gets one clear warning and Compose falls back to old behavior.
- Magic Jewel is a new local project at `~/src/magic-jewel`, not a fourth upstream repo.

## Documentation Deliverables

- JBR:
  - add `doc/skia-interop.md`.
  - document the `JBRSkia` public/private API split, service registration, availability semantics, and why external clients must not call `JBRApi.internalService()`.
  - document `ABI_ID` and `BUILD_ID`: `ABI_ID` is bumped on any change to pointer types, C ABI layout, scope layout, lifecycle/threading rules, or Metal-object contract; `BUILD_ID` is derived from Skia revision, Skia compile flags hash, and `ABI_ID`.
  - document the Metal contract: destination `MTLContext`, load-not-clear behavior, command ordering, scope lifetime, and screen-migration invalidation.
  - document HarfBuzz coexistence between JBR's existing `src/java.desktop/share/native/libharfbuzz/` and any Skia-bundled HarfBuzz, with the chosen symbol/linkage isolation strategy.
- Skiko:
  - add `JBR-INTEROP.md` or a dedicated section in top-level `DEVELOPMENT.md`.
  - document the new Skia-less JBR interop artifact, how it differs from normal `libskiko` runtimes, and why no Skia C++ objects may cross between bundled Skiko and JBR Skia.
  - document JVM discovery order, fallback markers, and diagnostics counters.
  - document fast-path text/font/typeface ownership through JBR Skia.
- Compose Multiplatform Core:
  - document in `compose/desktop/desktop/README.md` or an adjacent desktop doc.
  - document `useJbrSkiaInteropInComposePanel` and system property `compose.swing.render.on.jbr.skia`.
  - document that CMP only routes to the new Skiko component/redrawer and preserves existing `RenderSettings.SwingGraphics` fallback behavior.
- Magic Jewel:
  - add top-level `README.md` plus `docs/`.
  - document setup using local JBR, Skiko, and CMP builds.
  - document old/new launch modes, profiler scripts, report interpretation, known noise in `ps` CPU data, and expected fallback warnings.
  - document the report file structure and `SKIKO_JBR_INTEROP_FALLBACK reason=...` marker format so CI/jobs can parse it stably.
