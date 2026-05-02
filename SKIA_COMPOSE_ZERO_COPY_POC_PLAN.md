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

### Checkpoint 109: Surface-Change Report Marker Parsing

Status: completed in Magic Jewel report validation.

- Magic Jewel report parsing now recognizes Skiko's surface identity invalidation marker:
  - `SKIKO_JBR_INTEROP_SURFACE_CHANGED oldSurfaceId=... newSurfaceId=... oldMetalTexture=... newMetalTexture=...`
- `summary.properties` now includes:
  - `skiko_surface_change_markers=<count>`
- `report.md` now has a dedicated "Surface Identity Markers" section and explains that the marker means Skiko observed a different JBR destination surface and discarded cached surface-bound state.
- Magic Jewel README documents the marker and the new machine-readable summary key.

Validation:

- Magic Jewel report parser tests: `bash scripts/test-jbr-skia-report-validation.sh`

Next checkpoint:

- Add a live resize/surface-change smoke that deliberately changes the JFrame size during the new-mode measurement window and requires `skiko_surface_change_markers > 0`.

### Checkpoint 110: Live Resize Surface-Change Smoke

Status: completed in Magic Jewel command-mode report.

- Magic Jewel now has an opt-in resize probe:
  - `MAGIC_JEWEL_AUTO_RESIZE=true`
  - app property: `magic.jewel.autoResize=true`
  - marker: `MAGIC_JEWEL_WINDOW_RESIZE width=... height=...`
- The report harness now supports:
  - `EXPECT_MIN_SURFACE_CHANGES=<n>`
  - full-log counting for `SKIKO_JBR_INTEROP_SURFACE_CHANGED`, because resize/surface lifecycle events can happen before the sampled performance window starts.
- The live resize smoke passed with one Skiko surface-change marker:
  - `validation_status=passed`
  - `fallback_new_count=0`
  - `skiko_picture_frames=0`
  - `jbr_picture_frames=0`
  - `skiko_command_frames=2180`
  - `jbr_command_frames=2180`
  - `skiko_surface_change_markers=1`
  - `screenshot_status=passed`
- The live marker showed the surface and destination texture changing after the app resize.

Validation:

- Magic Jewel report parser tests: `bash scripts/test-jbr-skia-report-validation.sh`
- Magic Jewel compile: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`
- Magic Jewel resize smoke: `OUT_DIR=/tmp/magic-jewel-surface-change-resize-smoke-2 DURATION_SECONDS=7 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_AUTO_RESIZE=true EXPECT_MIN_SURFACE_CHANGES=1 ./scripts/jbr-skia-interop-report.sh`

Next checkpoint:

- Turn the temporary Skiko cached-surface invalidation diagnostic into the next production-shaped cache boundary: cache only state that is safe across paints, key it by the exposed surface/context identity, and keep full-log lifecycle markers parseable.

### Checkpoint 111: Unit-Tested Skiko Surface Identity Tracker

Status: completed in Skiko.

- Skiko's `JbrSkiaSwingLayer` no longer keeps surface identity comparison as paint-method-only local state.
- Added a small internal `SurfaceIdentityTracker` that:
  - ignores unknown `0/0/0` identities
  - treats repeated identities as stable
  - reports a structured change when either `surfaceId` or `metalTexturePtr` changes
  - resets on `removeNotify()`
- The existing layer still closes its temporary diagnostic `DirectContext` on identity change and emits the same parseable marker.
- Added focused Skiko tests for unknown, stable, changed, and cleared identity tracking.

Validation:

- Skiko focused test: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`

Next checkpoint:

- Decide whether the next production-shaped cache should live in Skiko only as command/replay-side bookkeeping or whether JBR should expose a stronger context id separate from `surfaceId` so Skiko can distinguish context migration from same-context texture replacement.

### Checkpoint 112: Scoped Destination Context Identity

Status: completed across Runtime API, JBR, Skiko, CMP, and Magic Jewel.

- Runtime API and JBR now expose `ScopedSkiaCanvas.getContextId()`.
- JBR's macOS service derives `contextId` from the destination `MTLContext*` via a native `nativeGetContextId(nativeOpsPtr)` helper.
- ABI bumped to `41`; native metadata bumped to `3`; `BUILD_ID` is now `skia=m147-64a2414108;flags=macos-release-metal-poc:1;abi=41;native=3`.
- Skiko now requires ABI `41` / native metadata `3`, reads `contextId` reflectively, includes it in `SKIKO_JBR_INTEROP_SCOPE_ACQUIRED`, and tracks `contextId + surfaceId + metalTexturePtr`.
- Surface-change markers now include context ids:
  - `SKIKO_JBR_INTEROP_SURFACE_CHANGED oldContextId=... newContextId=... oldSurfaceId=... newSurfaceId=... oldMetalTexture=... newMetalTexture=...`
- CMP's command recorder emits command-stream ABI `41`.
- Magic Jewel README/test fixtures were updated for the context-aware surface-change marker.
- ABI 41 resize smoke showed the key distinction we wanted: same context id, changed surface id, changed texture id after window resize.

Validation:

- Runtime API process/dev build: `bash tools/build.sh process`; `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-abi41-dev`
- Public shim refreshed: `/tmp/jbr-api-shim.jar`
- JBR patched Java compile into `/tmp/jbr-skia-run/desktop`
- JBR native dylib rebuild into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`
- Skiko focused test: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Skiko Maven-local publish: `./gradlew --no-configuration-cache :skiko:publishAwtPublicationToMavenLocal :skiko:publishAwtRuntimeElementsPublicationToMavenLocal :skiko:publishSkikoJvmRuntimeMacosArm64PublicationToMavenLocal`
- CMP compile/jar: `./gradlew --no-configuration-cache :compose:ui:ui-graphics:compileKotlinDesktop`; `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar`
- Magic Jewel compile and parser tests: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew compileKotlin`; `bash scripts/test-jbr-skia-report-validation.sh`
- Magic Jewel ABI 41 resize smoke: `/tmp/magic-jewel-abi41-context-id-resize-smoke/report.md`

Next checkpoint:

- Use `contextId` to split Skiko/JBR cache policy: same-context surface replacement should invalidate surface-bound wrappers only; changed-context migration should invalidate context-bound caches too.

### Checkpoint 113: Same-Context Surface Replacement Policy

Status: completed in Skiko and Magic Jewel validation.

- Skiko's `SurfaceIdentityChange` now exposes:
  - `contextChanged`
  - `surfaceChanged`
- `JbrSkiaSwingLayer` keeps context-bound diagnostic state on same-context surface replacement, and only closes the cached diagnostic `DirectContext` when `contextChanged=true`.
- The surface-change marker now includes both booleans:
  - `SKIKO_JBR_INTEROP_SURFACE_CHANGED oldContextId=... newContextId=... contextChanged=... surfaceChanged=... oldSurfaceId=... newSurfaceId=... oldMetalTexture=... newMetalTexture=...`
- Added a focused Skiko test proving resize-shaped changes are `contextChanged=false surfaceChanged=true`.
- Magic Jewel docs and parser fixtures now use the context-aware marker shape.
- Live Magic Jewel resize smoke passed and showed:
  - `contextChanged=false`
  - `surfaceChanged=true`
  - `skiko_surface_change_markers=1`
  - `fallback_new_count=0`
  - `skiko_picture_frames=0`
  - `jbr_picture_frames=0`
  - `screenshot_status=passed`

Validation:

- Skiko focused test: `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Skiko Maven-local publish after marker update.
- Magic Jewel report parser tests: `bash scripts/test-jbr-skia-report-validation.sh`
- Magic Jewel context/surface policy smoke: `/tmp/magic-jewel-context-surface-policy-smoke/report.md`

Next checkpoint:

- Add a report parser assertion for `contextChanged=false surfaceChanged=true` in resize smoke mode, then decide the next cacheable object to move from diagnostic bookkeeping toward production JBR-owned state.

### Checkpoint 114: Strict Resize Surface-Policy Validation

Status: completed in Magic Jewel report validation.

- Magic Jewel report validation now supports:
  - `EXPECT_SURFACE_CONTEXT_CHANGED=true|false`
  - `EXPECT_SURFACE_CHANGED=true|false`
- `summary.properties` now includes:
  - `skiko_context_change_markers`
  - `skiko_same_context_surface_change_markers`
- Added parser fixtures proving the report passes for resize-shaped markers and fails for context-migration-shaped markers when resize semantics are expected.
- The README resize smoke now requires:
  - `EXPECT_MIN_SURFACE_CHANGES=1`
  - `EXPECT_SURFACE_CONTEXT_CHANGED=false`
  - `EXPECT_SURFACE_CHANGED=true`
- Live strict resize smoke passed with:
  - `skiko_surface_change_markers=1`
  - `skiko_context_change_markers=0`
  - `skiko_same_context_surface_change_markers=1`
  - `fallback_new_count=0`
  - `skiko_picture_frames=0`
  - `jbr_picture_frames=0`
  - `screenshot_status=passed`

Validation:

- Magic Jewel parser tests: `bash scripts/test-jbr-skia-report-validation.sh`
- Magic Jewel strict resize smoke: `/tmp/magic-jewel-context-surface-policy-strict-smoke/report.md`

### Checkpoint: Context-Scoped JBR Image Cache

Status: completed for the PoC command-stream image cache.

- JBR native command replay no longer stores `SkImage` entries in one process-global namespace keyed only by image id.
- Native image entries are keyed by `(MTLContext*, imageKey)`, so a future destination context migration cannot reuse images created for a different Java2D/Metal context.
- Java2D fallback replay mirrors the same ownership rule with `(contextId, imageKey)` `BufferedImage` entries.
- `COMMAND_CLEAR_IMAGE_CACHE` now clears only the current context namespace and emits parseable markers:
  - native: `JBR_SKIA_INTEROP_IMAGE_CACHE_CLEAR backend=native contextId=0x... cleared=N`
  - Java2D fallback: `JBR_SKIA_INTEROP_IMAGE_CACHE_CLEAR backend=java2d contextId=0x... cleared=N`
- Magic Jewel reports now count scoped JBR image-cache-clear markers separately from generic clear markers.
- Same-context surface replacement remains compatible with cached image reuse; context changes get isolated cache namespaces.

Validation:

- JBR patched Java service compile: `javac ... JBRSkia.java JBRSkiaService.java`
- JBR native dylib compile: `clang++ ... JBRSkiaInterop.mm ...`
- Magic Jewel parser tests: `bash scripts/test-jbr-skia-report-validation.sh`
- Magic Jewel scoped image-cache churn smoke: `/tmp/magic-jewel-context-image-cache-smoke/report.md`
  - `validation_status=passed`
  - `fallback_new_count=0`
  - `skiko_picture_frames=0`
  - `jbr_picture_frames=0`
  - `cmp_recorder_frames=646`
  - `jbr_command_frames=646`
  - `jbr_image_cache_clear_frames=656`
  - `jbr_scoped_image_cache_clear_frames=656`
  - `screenshot_status=passed`

### Checkpoint: Combined Resize + Image-Cache Ownership Smoke

Status: completed.

- Ran the image-cache churn sample with automatic live resize enabled.
- The run validates that same-context surface replacement is observed as `contextChanged=false surfaceChanged=true`.
- The same run validates that JBR continues to emit context-scoped image-cache clear markers after resize.
- This ties together the two production-shaped ownership rules:
  - surface-bound wrappers are invalidated on texture/surface replacement
  - context-bound cache namespaces survive same-context replacement and are isolated by context id

Validation:

- Magic Jewel combined resize/image-cache smoke: `/tmp/magic-jewel-resize-image-cache-policy-smoke/report.md`
  - `validation_status=passed`
  - `fallback_new_count=0`
  - `skiko_picture_frames=0`
  - `jbr_picture_frames=0`
  - `cmp_recorder_frames=535`
  - `jbr_command_frames=534`
  - `jbr_image_cache_clear_frames=542`
  - `jbr_scoped_image_cache_clear_frames=542`
  - `skiko_surface_change_markers=1`
  - `skiko_context_change_markers=0`
  - `skiko_same_context_surface_change_markers=1`
  - `screenshot_status=passed`
- Observed marker:
  - `SKIKO_JBR_INTEROP_SURFACE_CHANGED oldContextId=0x8c7749140 newContextId=0x8c7749140 contextChanged=false surfaceChanged=true ...`

### Checkpoint: Strict Scoped Image-Cache Marker Gate

Status: completed.

- Magic Jewel gained `EXPECT_MIN_JBR_SCOPED_IMAGE_CACHE_CLEARS`.
- The report harness now fails strict command runs when JBR emits only the old unscoped `JBR_SKIA_INTEROP_IMAGE_CACHE_CLEAR` marker shape.
- Parser tests cover both passing scoped markers and failing unscoped-only markers.
- The combined resize + image-cache churn smoke now requires:
  - command recorder image-cache clears
  - generic JBR image-cache clear markers
  - scoped JBR image-cache clear markers with `contextId=0x`
  - at least one same-context surface replacement marker

Validation:

- Magic Jewel parser tests: `bash scripts/test-jbr-skia-report-validation.sh`
- Magic Jewel strict combined resize/image-cache smoke: `/tmp/magic-jewel-resize-image-cache-policy-strict-scoped-smoke/report.md`
  - `validation_status=passed`
  - `fallback_new_count=0`
  - `skiko_picture_frames=0`
  - `jbr_picture_frames=0`
  - `cmp_recorder_frames=791`
  - `jbr_command_frames=792`
  - `jbr_image_cache_clear_frames=803`
  - `jbr_scoped_image_cache_clear_frames=803`
  - `skiko_surface_change_markers=1`
  - `skiko_context_change_markers=0`
  - `skiko_same_context_surface_change_markers=1`
  - `screenshot_status=passed`

### Checkpoint: Stable Image-Cache Reuse

Status: completed for the PoC recorder policy.

- CMP's command recorder image-key budget increased from 256 to 1024 entries.
- The previous 256-entry budget caused the 260-image Magic Jewel cache stress case to clear/redefine the JBR image cache every frame, even when image content was stable.
- Added a focused CMP test proving that 260 stable images are defined on the first frame and then reused as `COMMAND_DRAW_IMAGE_REF` records with no new `COMMAND_DEFINE_IMAGE_ARGB` and no `COMMAND_CLEAR_IMAGE_CACHE`.
- Kept the overflow path covered by moving the threshold test from 257 images to 1025 images.
- Magic Jewel gained `MAGIC_JEWEL_STABLE_IMAGE_CACHE_CHURN=true` so launch-level reports can use stable image content instead of per-frame-changing image content.
- Magic Jewel report validation gained `EXPECT_MAX_IMAGE_DEFINES` and `EXPECT_MAX_IMAGE_CACHE_CLEARS`.

Validation:

- CMP pre-fix TDD check failed as expected:
  - `JbrSkiaCommandRecorderTest.reusesStableImageCacheEntriesAcrossFrames` saw one cache clear before the budget change.
- CMP focused recorder tests:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.reusesStableImageCacheEntriesAcrossFrames --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.clearsImageCacheBeforeRedefiningAfterThreshold`
- CMP jar refresh:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`
- Magic Jewel parser tests:
  - `bash scripts/test-jbr-skia-report-validation.sh`
- Magic Jewel stable image-cache reuse smoke: `/tmp/magic-jewel-stable-image-cache-reuse-smoke/report.md`
  - `validation_status=passed`
  - `fallback_new_count=0`
  - `skiko_picture_frames=0`
  - `jbr_picture_frames=0`
  - `cmp_recorder_frames=1156`
  - `jbr_command_frames=1155`
  - `jbr_image_cache_clear_frames=0`
  - `jbr_scoped_image_cache_clear_frames=0`
  - `screenshot_status=passed`
  - recorder summary: `avg_image_defines=0.0 max_image_defines=0 avg_image_refs=260.0 max_image_refs=260 avg_image_cache_clears=0.0 max_image_cache_clears=0`

### Checkpoint: ABI 42 Single-Key Image Cache Eviction

Status: completed.

- Added ABI 42 / command capability `COMMAND_CAP64_EVICT_IMAGE_CACHE_KEY`.
- Added command `COMMAND_EVICT_IMAGE_CACHE_KEY = 33` with payload `[op, 20, 0, cacheKeyHigh, cacheKeyLow]`.
- JBR native replay evicts only the keyed `SkImage` from the current destination context namespace and logs:
  - `JBR_SKIA_INTEROP_IMAGE_CACHE_EVICT backend=native contextId=... key=... removed=...`
- JBR Java2D fallback replay mirrors the same keyed eviction against `(contextId, imageKey)` `BufferedImage` entries.
- CMP recorder now keeps an access-ordered image-key map. When the budget is full it emits one eviction for the eldest key, then defines the new image, instead of emitting `COMMAND_CLEAR_IMAGE_CACHE`.
- Skiko requires the ABI 42 eviction capability before selecting command mode, so old JBR builds fall back instead of accepting streams that may contain command 33.
- Magic Jewel report parsing now treats `imageCacheEvicts` as a first-class recorder metric and counts JBR eviction markers.

Validation:

- Runtime API:
  - `bash tools/build.sh process`
  - `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-abi42-dev`
  - refreshed `/tmp/jbr-api-shim.jar`
- JBR:
  - patched Java service compile
  - native `libjbrskiainterop.dylib` compile
- Skiko:
  - `./gradlew --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - published ABI 42 Skiko artifacts to Maven local
- CMP:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar`
- Magic Jewel:
  - parser tests: `bash scripts/test-jbr-skia-report-validation.sh`
  - eviction smoke: `/tmp/magic-jewel-image-cache-evict-abi42-smoke/report.md`
    - `validation_status=passed`
    - `fallback_new_count=0`
    - `skiko_picture_frames=0`
    - `jbr_picture_frames=0`
    - `cmp_recorder_frames=799`
    - `jbr_command_frames=799`
    - `jbr_image_cache_clear_frames=0`
    - `jbr_image_cache_evict_frames=5980`
    - `screenshot_status=passed`
    - recorder summary: `avg_image_cache_clears=0.0 max_image_cache_clears=0 avg_image_cache_evicts=7.5 max_image_cache_evicts=260`
  - combined resize + eviction smoke: `/tmp/magic-jewel-resize-image-cache-evict-abi42-smoke/report.md`
    - `validation_status=passed`
    - `fallback_new_count=0`
    - `skiko_picture_frames=0`
    - `jbr_picture_frames=0`
    - `cmp_recorder_frames=454`
    - `jbr_command_frames=454`
    - `jbr_image_cache_clear_frames=0`
    - `jbr_image_cache_evict_frames=4940`
    - `skiko_surface_change_markers=1`
    - `skiko_context_change_markers=0`
    - `skiko_same_context_surface_change_markers=1`
    - `screenshot_status=passed`

Next checkpoint:

- Add a long-running quiet-machine benchmark pass for stable and dynamic image-cache workloads, then decide whether to continue reducing image fallback or move another remaining text/style case into native JBR-owned commands.

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
  - `Class.forName("com.jetbrains.desktop.JBRSkia")` from the public JBR API jar and read static `ABI_ID` / `BUILD_ID`.
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

## Checkpoint: Text Fidelity Default

Date: 2026-04-29

Status: in progress as a Magic Jewel visual-fidelity cleanup after the screenshot showed command-path text using the wrong Jewel font/size/alignment.

Changes:
- CMP now records desktop text through the fidelity-first text image bridge by default in command mode.
  - The bridge rasterizes the already-laid-out Skia Paragraph into an ARGB `ImageBitmap` and sends it through the existing image-ref/image-cache command path.
  - This keeps Magic Jewel on JBR command replay while preserving Compose/Jewel's resolved typeface, font size, alignment, decorations, fallback glyphs, and paragraph layout.
- The experimental native text commands remain available behind `-Dcompose.jbr.skia.command.nativeText=true`.
  - This keeps ABI 17/18-25 tests and future JBR-owned typeface experiments alive.
  - It is not the default because those commands currently cannot carry the resolved Jewel/Compose typeface and therefore can visibly drift from real Compose text.
- Magic Jewel scripts now expose `JBR_SKIA_NATIVE_TEXT=true` as the opt-in switch for that native text command path.
- Magic Jewel README examples now validate default text through `EXPECT_MIN_IMAGE_REFS` and reserve `EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS` for explicit native-text probes.

Validation so far:
- Red/green CMP focused tests:
  - `paint_withFillDrawStyle_recordsJbrSkiaTextImageByDefault`
  - `paint_withFillDrawStyle_recordsJbrSkiaSimpleTextWhenNativeTextIsEnabled`
  - `paint_withLatin1Text_recordsJbrSkiaSimpleText`
- Command:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-text:desktopTest --tests androidx.compose.ui.text.DesktopParagraphTest.paint_withFillDrawStyle_recordsJbrSkiaTextImageByDefault --tests androidx.compose.ui.text.DesktopParagraphTest.paint_withFillDrawStyle_recordsJbrSkiaSimpleTextWhenNativeTextIsEnabled --tests androidx.compose.ui.text.DesktopParagraphTest.paint_withLatin1Text_recordsJbrSkiaSimpleText`
- Magic Jewel report parser:
  - `bash scripts/test-jbr-skia-report-validation.sh`
- Patched CMP jars:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`
- Magic Jewel command-mode smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-text-image-fidelity-smoke DURATION_SECONDS=6 WARMUP_SECONDS=2 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands EXPECT_MIN_IMAGE_REFS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-interop-report.sh`
  - report: `/tmp/magic-jewel-text-image-fidelity-smoke/report.md`
  - validation: passed
  - fallback markers: `0`
  - picture frames: `0`
  - CMP command recorder: `frames=1049 fps=174.8 avg_commands=2413 max_commands=8049 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=0.0 max_text_commands=0 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=1 avg_image_refs=9.0 max_image_refs=9 avg_image_cache_clears=0.0 max_image_cache_clears=0 avg_image_cache_evicts=0.0 max_image_cache_evicts=0 reasons=none`
  - Skiko/JBR command frames: `1048` / `1048`
  - screenshot: `/tmp/magic-jewel-text-image-fidelity-smoke/new-window.png`

Next:
- Add screenshot-level text/typography assertions if we can make a stable enough pixel oracle, then continue macOS MVP hardening with compatibility matrix packaging and quiet-machine benchmark runs.

Follow-up:
- Magic Jewel command screenshot assertion now counts dark text pixels in two known light-background regions:
  - top text band for header/buttons/counters
  - bottom text band for the canvas labels.
- This is intentionally a text-presence/placement guardrail rather than OCR. It catches disappeared or wildly shifted command-path text without pretending to verify exact glyph metrics.
- Validation:
  - direct assertion on `/tmp/magic-jewel-text-image-fidelity-smoke/new-window.png`: `topText=5335 bottomText=7341`
  - full report command: `OUT_DIR=/tmp/magic-jewel-text-image-assertion-smoke DURATION_SECONDS=5 WARMUP_SECONDS=2 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands EXPECT_MIN_IMAGE_REFS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-interop-report.sh`
  - report: `/tmp/magic-jewel-text-image-assertion-smoke/report.md`
  - validation: passed
  - CMP command recorder: `frames=461 fps=92.2 avg_commands=2498 max_commands=8049 unsupported_frames=0 avg_unsupported=0.0 max_unsupported=0 avg_text_commands=0.0 max_text_commands=0 avg_paragraph_text_commands=0.0 max_paragraph_text_commands=0 avg_image_defines=0.0 max_image_defines=1 avg_image_refs=9.0 max_image_refs=9 avg_image_cache_clears=0.0 max_image_cache_clears=0 avg_image_cache_evicts=0.0 max_image_cache_evicts=0 reasons=none`
  - Skiko/JBR command frames: `461` / `461`
  - screenshot assertion: `JBR_SKIA_COMMAND_SCREENSHOT_COUNTS green=570210 blue=1053509 purple=31352 yellow=35545 orange=18828 white=963664 topText=5334 bottomText=7341`

## Checkpoint: Launch-Level Forced Compatibility Matrix

Date: 2026-04-29

Status: completed as a repeatable launch-level smoke for the current forced compatibility scenarios. This is not the final packaged old/new artifact matrix; it uses the current local artifacts plus explicit test properties/missing API shim to force each fallback reason.

Changes:
- Magic Jewel now has `scripts/jbr-skia-compatibility-matrix.sh`.
- The matrix runs:
  - happy command-mode path with text image refs required
  - forced `abi-mismatch`
  - forced `native-abi-mismatch`
  - forced `command-capability-mismatch`
  - forced `public-api-missing`.
- For the happy path it requires a passing report with JBR command frames.
- For forced mismatch paths it requires a passing report with one structured fallback marker and zero JBR command frames.

Validation:
- Command: `OUT_ROOT=/tmp/magic-jewel-compatibility-matrix-smoke DURATION_SECONDS=3 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-compatibility-matrix.sh`
- Result: `JBR_SKIA_COMPATIBILITY_MATRIX passed out_root=/tmp/magic-jewel-compatibility-matrix-smoke`
- Cases:
  - `happy`: `status=passed fallback_new_count=0 jbr_command_frames=343`
  - `abi-mismatch`: `status=passed fallback_new_count=1 jbr_command_frames=0`
  - `native-abi-mismatch`: `status=passed fallback_new_count=1 jbr_command_frames=0`
  - `command-capability-mismatch`: `status=passed fallback_new_count=1 jbr_command_frames=0`
  - `public-api-missing`: `status=passed fallback_new_count=1 jbr_command_frames=0`

Next:
- Keep the full packaged old/new artifact matrix open until we have named old/new bundles for JBR, Runtime API, Skiko, and CMP.
- Continue macOS MVP hardening with quieter benchmark runs and remaining rendering edge cases.

## Checkpoint: Transformed Shader Strict Fallback

Date: 2026-04-29

Status: completed as a focused CMP recorder guardrail.

Why:
- Serialized gradient commands are safe only while CMP still has the original known shader-family metadata.
- `TransformShader` wraps the Skia shader with a local matrix and returns an opaque Compose `Shader` without the original serialized gradient metadata.
- Strict command mode must therefore reject transformed gradients and fall back instead of sending a partial or raw-pointer shader representation across the Skiko/JBR Skia boundary.

Change:
- Added `JbrSkiaCommandRecorderTest.rejectsTransformedGradientShaderInStrictMode`.
- The test builds a linear gradient, wraps it in `TransformShader` with a local translation matrix, draws it in strict mode, and asserts the command array is `null`.

Validation:
- Command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.rejectsTransformedGradientShaderInStrictMode`
- Result: passed.

Next:
- Keep broader generic/nonserializable shader support behind the future JBR-owned shader factory design.
- Continue with remaining macOS MVP hardening and benchmark/report work.

Follow-up:
- Added `JbrSkiaCommandRecorderTest.rejectsCompositeShaderInStrictMode`.
- The test builds a `CompositeShader` from linear/radial gradients and confirms strict command recording returns `null`.
- This protects the same boundary as transformed shaders: once Compose has an opaque shader wrapper rather than the original serialized family metadata, command replay must fall back instead of sharing raw Skia shader objects across runtimes.
- Validation:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.rejectsCompositeShaderInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.rejectsTransformedGradientShaderInStrictMode`
  - result: passed.

## Checkpoint: Benchmark Suite Wrapper

Date: 2026-04-29

Status: completed as a repeatable collection wrapper for later quiet-machine runs.

Changes:
- Magic Jewel now has `scripts/jbr-skia-benchmark-suite.sh`.
- The suite runs consistent old/new reports for:
  - SKP picture replay
  - command replay
  - command replay with stable image-cache workload
  - command replay with dynamic image-cache workload
  - command replay with resize plus dynamic image-cache workload.
- `ENABLE_ASPROF=true` is forwarded so a quiet-machine run can collect async-profiler outputs for every scenario.
- The stable-image benchmark records cache behavior but does not enforce zero `imageDefines` during short warmups. The stricter zero-define invariant remains covered by the dedicated stable-image-cache smoke.

Validation:
- First short run exposed that `EXPECT_MAX_IMAGE_DEFINES=0` was too strict for very short warmups, because image definitions can still occur inside the sampled window.
- After relaxing that benchmark-suite-only assertion:
  - command: `OUT_ROOT=/tmp/magic-jewel-benchmark-suite-smoke-2 DURATION_SECONDS=2 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-benchmark-suite.sh`
  - result: `JBR_SKIA_BENCHMARK_SUITE passed out_root=/tmp/magic-jewel-benchmark-suite-smoke-2`
  - `picture`: `status=passed fallback_new_count=0 jbr_picture_frames=508 jbr_command_frames=0`
  - `commands`: `status=passed fallback_new_count=0 jbr_picture_frames=0 jbr_command_frames=322`
  - `commands-stable-images`: `status=passed fallback_new_count=0 jbr_picture_frames=0 jbr_command_frames=484`
  - `commands-dynamic-images`: `status=passed fallback_new_count=0 jbr_picture_frames=0 jbr_command_frames=472`
  - `commands-resize-dynamic-images`: `status=passed fallback_new_count=0 jbr_picture_frames=0 jbr_command_frames=478`

Next:
- Use this suite with longer durations and optionally `ENABLE_ASPROF=true` when the host is quiet enough for meaningful numbers.

## Checkpoint: Layered Swing Popup Stress

Date: 2026-04-29

Status: completed as a Magic Jewel glass-pane overlay stress.

Why:
- IDE UIs commonly combine Compose content, embedded Swing islands, overlays, and popup-like surfaces.
- A strict command-mode smoke should prove that an animated Swing overlay can repaint over the ComposePanel without making CMP/Skiko fall back to picture replay.
- The existing title-based window capture intentionally captures the app window only, not separate OS popup windows. For this harness slice the popup-like surface is therefore hosted in the JFrame glass pane so screenshot assertions can validate the layering in the same captured window.

Changes:
- Magic Jewel added `MAGIC_JEWEL_POPUP_STRESS=true`.
- The flag installs an animated Swing glass-pane popup card over the ComposePanel and emits:
  - `MAGIC_JEWEL_POPUP_SHOWN`
  - `MAGIC_JEWEL_POPUP_FRAME`.
- The report script records `popup_old_frames`, `popup_new_frames`, and `popup_new_shown` in `summary.properties`.
- `EXPECT_MIN_POPUP_FRAMES` makes strict command validation require popup repaint activity.
- Command and mixed screenshot assertion scripts count `popupPink` and `popupCyan` pixels when `MAGIC_JEWEL_POPUP_STRESS=true`.
- The screenshot capture waits for `MAGIC_JEWEL_POPUP_SHOWN` before capturing when popup stress is enabled.

Validation:
- Command: `./gradlew --no-daemon compileKotlin`
- Result: passed.
- Command: `OUT_DIR=/tmp/magic-jewel-popup-stress-smoke-4 DURATION_SECONDS=4 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 MAGIC_JEWEL_POPUP_STRESS=true EXPECT_MIN_POPUP_FRAMES=5 JBR_SKIA_RENDER_MODE=commands SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
- Result: passed.
- Summary:
  - `validation_status=passed`
  - `fallback_new_count=0`
  - `skiko_picture_frames=0`
  - `jbr_picture_frames=0`
  - `skiko_command_frames=622`
  - `jbr_command_frames=622`
  - `popup_new_frames=692`
  - `popup_new_shown=1`
  - `screenshot_status=passed`
  - screenshot assertion: `popupPink=8460 popupCyan=2368`.

Next:
- Keep real OS popup/menu windows as a later productionization test, since they are not captured by the current window-only screenshot helper.

## Checkpoint: Non-Finite Gradient Metadata Fallback

Date: 2026-04-29

Status: completed as a focused CMP recorder test.

Why:
- The command ABI serializes gradient coordinates and radii as fixed-point integers.
- NaN or infinity must never cross the CMP/Skiko to JBR boundary as encoded command payload.
- Local code-ground-truth check showed that constructing a real Skia gradient shader with NaN fails before the recorder can see it (`Can't wrap nullptr`). The meaningful guardrail is therefore a valid native Skia shader backing with corrupted/non-finite JBR gradient metadata, which is the metadata the command recorder serializes.

Change:
- Added `JbrSkiaCommandRecorderTest.rejectsNonFiniteGradientGeometryInStrictMode`.
- The test creates valid native shader backing and intentionally non-finite JBR metadata for:
  - linear gradient endpoint metadata
  - radial gradient radius metadata
  - sweep gradient center metadata.
- Strict command recording must return `null` for all three cases.

Validation:
- Initial direct-NaN shader-construction test failed before recorder entry with `Can't wrap nullptr`, confirming that it did not exercise the intended boundary.
- Adjusted the test to inject non-finite command metadata over valid shader backing.
- Command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.rejectsNonFiniteGradientGeometryInStrictMode`
- Result: passed.

Next:
- Keep remaining generic shader families behind explicit fallback tests or future JBR-owned shader factory design.

## Checkpoint: Popup Marker Parser Coverage

Date: 2026-04-29

Status: completed as a Magic Jewel report-parser fixture.

Change:
- Added parser-level tests for the popup stress markers in `scripts/test-jbr-skia-report-validation.sh`.
- The passing fixture writes `MAGIC_JEWEL_POPUP_SHOWN` plus two `MAGIC_JEWEL_POPUP_FRAME` markers, runs with `MAGIC_JEWEL_POPUP_STRESS=true EXPECT_MIN_POPUP_FRAMES=2`, and verifies:
  - `popup_new_shown=1`
  - `popup_new_frames=2`.
- The failing fixture verifies that `EXPECT_MIN_POPUP_FRAMES=2` rejects a log with only one popup paint marker.

Validation:
- Command: `bash scripts/test-jbr-skia-report-validation.sh`
- Result: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`.

Next:
- Keep using live Magic Jewel smoke for screenshot/layering confidence and parser fixtures for cheap CI-style marker regression coverage.

## Checkpoint: Command Probe Suite Wrapper

Date: 2026-04-29

Status: completed as a selectable Magic Jewel command-mode suite.

Changes:
- Added `scripts/jbr-skia-command-probe-suite.sh`.
- The suite wraps existing report cases for:
  - `commands-core-primitives`
  - `commands-gradient-surfaces`
  - `commands-gradient-paths`
  - `commands-popup`
  - `commands-text-image`
  - `commands-native-text`
  - `commands-shader-fallback`
  - `commands-invalid-gradient-fallback`.
- `CASES="..."` can select a subset, which keeps local smoke runs small while still making the full matrix easy to launch.

Validation:
- Syntax check: `bash -n scripts/jbr-skia-command-probe-suite.sh`
- Result: passed.
- Smoke command: `OUT_ROOT=/tmp/magic-jewel-command-probe-suite-smoke CASES=commands-core-primitives DURATION_SECONDS=2 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
- Result: `JBR_SKIA_COMMAND_PROBE_SUITE passed out_root=/tmp/magic-jewel-command-probe-suite-smoke`
- Case summary:
  - `commands-core-primitives`: `status=passed fallback_new_count=0 unsupported=none jbr_picture_frames=0 jbr_command_frames=967`.

Next:
- Run the full suite on a quieter machine or before a larger handoff; use targeted `CASES=...` locally while the host is busy.

## Checkpoint: Real Popup Window Capture

Date: 2026-04-29

Status: completed for an undecorated Swing popup-window smoke.

Why:
- The glass-pane popup validates layered Swing-over-Compose inside the main window, but real popups/menus often exist as separate native windows on macOS.
- The screenshot harness must preserve the window-capture constraint: capture the main app window by id and capture the popup window separately by id, without screen capture plus crop.

Changes:
- Magic Jewel added `MAGIC_JEWEL_POPUP_WINDOW_STRESS=true`.
- The sample opens an undecorated `JDialog` titled `MagicJewelPopupWindow` over the ComposePanel.
- The report script now supports:
  - `CAPTURE_POPUP_WINDOW_QUERY`
  - `POPUP_WINDOW_ASSERT_SCRIPT`
  - `popup_window_new_shown`
  - `popup_window_screenshot_status`.
- Added `scripts/assert-jbr-skia-popup-window-screenshot.sh`, which checks popup-window white/pink/cyan/text pixels.
- The command-probe suite gained a `commands-popup-window` case.
- Parser fixtures cover popup-window marker/screenshot success and missing-screenshot failure.

Validation:
- Command: `./gradlew --no-daemon compileKotlin`
- Result: passed.
- Command: `OUT_DIR=/tmp/magic-jewel-popup-window-smoke-2 DURATION_SECONDS=4 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 MAGIC_JEWEL_POPUP_WINDOW_STRESS=true EXPECT_MIN_POPUP_FRAMES=5 JBR_SKIA_RENDER_MODE=commands SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
- Result: passed.
- Summary:
  - `validation_status=passed`
  - `fallback_new_count=0`
  - `skiko_picture_frames=0`
  - `jbr_picture_frames=0`
  - `skiko_command_frames=319`
  - `jbr_command_frames=319`
  - `popup_window_new_shown=1`
  - `popup_window_screenshot_status=passed`
  - popup screenshot assertion: `white=55373 popupPink=6748 popupCyan=2368 darkText=73611 width=612 height=256`.
- Parser validation:
  - command: `bash scripts/test-jbr-skia-report-validation.sh`
  - result: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`.

Next:
- Menu-specific stress remains open; this checkpoint covers real popup-window capture and repaint behavior.

## Checkpoint: Launch Artifact Matrix Scaffold

Date: 2026-04-29

Status: completed as a launch-level artifact-matrix scaffold. The full old/new matrix remains open until named old bundles are available.

Why:
- The forced compatibility matrix proves the fallback machinery, but it does so with test-only mismatch properties.
- We also need a launcher that names the actual artifact surfaces: patched `java.desktop`, public JBR API shim, native JBR Skia dylib, Skiko Maven version, and patched CMP output root.
- Old/new combinations should be explicit and parseable even when a local machine only has the current artifact set.

Changes:
- Magic Jewel added `scripts/jbr-skia-artifact-matrix.sh`.
- The required rows are:
  - `current-all`: current desktop patch + public API shim + native dylib + Skiko version + CMP output root; expects no fallback and JBR command frames.
  - `missing-public-api`: current runtime artifacts with a missing public API shim; expects `SKIKO_JBR_INTEROP_FALLBACK reason=public-api-missing` and zero JBR command frames.
- Optional rows are recorded as skipped unless their corresponding artifact variable is supplied:
  - `OLD_JBR_API_SHIM`
  - `OLD_JBR_SKIA_LIB`
  - `OLD_DESKTOP_PATCH`
  - `OLD_SKIKO_VERSION`
  - `OLD_CMP_OUT`.
- The script writes `matrix.tsv` with stable columns: case, status, expected fallback, actual fallback count, command frames, report path, and note.
- Magic Jewel's Gradle/run wrapper now accepts `LOCAL_CMP_OUT`, so artifact rows can swap CMP output roots without editing `build.gradle.kts`.
- Magic Jewel README documents the artifact-matrix runner and the `LOCAL_CMP_OUT` override.

Validation:
- Syntax checks:
  - `bash -n scripts/jbr-skia-artifact-matrix.sh`
  - `bash -n scripts/run-jbr-skia.sh`
  - result: passed.
- Dry run:
  - command: `DRY_RUN=true OUT_ROOT=/tmp/magic-jewel-artifact-matrix-dry-run bash scripts/jbr-skia-artifact-matrix.sh`
  - result: passed and recorded current rows plus skipped optional rows.
- Live smoke:
  - command: `OUT_ROOT=/tmp/magic-jewel-artifact-matrix-smoke DURATION_SECONDS=2 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-artifact-matrix.sh`
  - result: `JBR_SKIA_ARTIFACT_MATRIX passed out_root=/tmp/magic-jewel-artifact-matrix-smoke`
  - `current-all`: `status=passed`, `fallback_new_count=0`, `jbr_command_frames=918`.
  - `missing-public-api`: `status=passed`, `fallback_new_count=1`, `jbr_command_frames=0`.
  - old artifact rows skipped because no old bundles were provided.

Next:
- Feed this scaffold with real old JBR API, JBR native, Skiko, desktop-patch, and CMP bundles when they are available, then close the full packaged old/new matrix item.

## Checkpoint: Swing Menu Popup Stress

Date: 2026-04-29

Status: completed as a Magic Jewel menu-layering stress slice.

Why:
- The glass-pane popup and undecorated popup-window checks cover two important layering shapes, but real Swing/Jewel apps lean heavily on menu popups.
- A menu check should prove the command-mode Compose surface can coexist with an animated Swing `JPopupMenu` without falling back to picture replay.

Changes:
- Magic Jewel added `MAGIC_JEWEL_MENU_STRESS=true`.
- The sample opens a `JPopupMenu` over the `ComposePanel`, containing the same animated Swing popup panel used by the glass-pane smoke.
- The app emits `MAGIC_JEWEL_MENU_SHOWN` plus the existing `MAGIC_JEWEL_POPUP_FRAME` paint markers.
- The report parser records `menu_new_shown` and fails strict validation if menu stress is enabled but the menu-shown marker is missing.
- The main-window screenshot assertions now have menu-specific pixel checks (`menuWhite`, `menuYellow`) instead of reusing the glass-pane popup's pink/cyan signature.
- `scripts/jbr-skia-command-probe-suite.sh` now includes a `commands-menu` case.
- Magic Jewel README documents the new menu stress command.

Validation:
- Syntax checks:
  - `bash -n scripts/jbr-skia-interop-report.sh`
  - `bash -n scripts/jbr-skia-command-probe-suite.sh`
  - `bash -n scripts/test-jbr-skia-report-validation.sh`
  - result: passed.
- Parser fixtures:
  - command: `bash scripts/test-jbr-skia-report-validation.sh`
  - result: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`.
- Compile:
  - command: `./gradlew --no-daemon compileKotlin`
  - result: passed.
- Live menu smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-menu-stress-smoke-2 DURATION_SECONDS=4 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 MAGIC_JEWEL_MENU_STRESS=true EXPECT_MIN_POPUP_FRAMES=5 JBR_SKIA_RENDER_MODE=commands SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
  - result: passed.
  - summary: `fallback_new_count=0`, `skiko_picture_frames=0`, `jbr_picture_frames=0`, `skiko_command_frames=601`, `jbr_command_frames=601`, `menu_new_shown=1`, `popup_new_frames=69`, `screenshot_status=passed`.
  - screenshot assertion: `menuWhite=150652`, `menuYellow=16182`.
- Command-probe suite case:
  - command: `OUT_ROOT=/tmp/magic-jewel-command-menu-suite-smoke CASES=commands-menu DURATION_SECONDS=4 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: `JBR_SKIA_COMMAND_PROBE_SUITE passed out_root=/tmp/magic-jewel-command-menu-suite-smoke`
  - case summary: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=599`.

Next:
- Continue macOS MVP hardening with either old-artifact bundles for the artifact matrix or remaining rendering edge cases; quiet-machine benchmark collection remains deferred until host load is stable.

## Checkpoint: Machine-Readable FPS Summary Fields

Date: 2026-04-29

Status: completed as report-schema hardening.

Why:
- `report.md` already prints FPS-style marker summaries, but automation had to parse prose to compare frame rates.
- The noisy-host caveat still applies, but the summary file should expose the figures directly so quiet-machine benchmark runs can be consumed by scripts.

Changes:
- Magic Jewel `summary.properties` now records FPS fields for:
  - app frame markers: `app_old_fps`, `app_new_fps`
  - Swing frame markers: `swing_old_fps`, `swing_new_fps`
  - popup frame markers: `popup_old_fps`, `popup_new_fps`
  - Skiko/JBR picture markers: `skiko_picture_fps`, `jbr_picture_fps`
  - Skiko/JBR command markers: `skiko_command_fps`, `jbr_command_fps`.
- README documents the new machine-readable FPS keys.
- Parser fixtures assert the command FPS keys are emitted.

Validation:
- Syntax check:
  - command: `bash -n scripts/jbr-skia-interop-report.sh scripts/test-jbr-skia-report-validation.sh`
  - result: passed.
- Parser fixtures:
  - command: `bash scripts/test-jbr-skia-report-validation.sh`
  - result: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`.
- Existing menu-smoke summary regenerated with `--validate-only`:
  - command: `OUT_DIR=/tmp/magic-jewel-menu-stress-smoke-2 JBR_SKIA_RENDER_MODE=commands EXPECT_STRICT_COMMANDS=true MAGIC_JEWEL_MENU_STRESS=true EXPECT_MIN_POPUP_FRAMES=5 bash scripts/jbr-skia-interop-report.sh --validate-only`
  - result: passed.
  - FPS keys: `app_new_fps=30.1`, `swing_new_fps=33.2`, `popup_new_fps=3.5`, `skiko_picture_fps=0.0`, `jbr_picture_fps=0.0`, `skiko_command_fps=30.1`, `jbr_command_fps=30.1`.

Next:
- Use these summary keys in quiet-machine benchmark runs instead of scraping human Markdown.

## Checkpoint: Benchmark Suite TSV Summary

Date: 2026-04-29

Status: completed as benchmark harness hardening.

Why:
- The benchmark suite already runs the right scenarios, but comparing results required opening each case's `summary.properties`.
- Short smoke runs also revealed that `ps` sampling can legitimately miss one side of a run; a suite-level table should expose sample counts and avoid treating missing samples as zero CPU.

Changes:
- Magic Jewel `summary.properties` now includes coarse `ps` sample fields:
  - `old_samples`, `old_avg_cpu`, `old_max_cpu`, `old_avg_rss_kb`, `old_max_rss_kb`
  - `new_samples`, `new_avg_cpu`, `new_max_cpu`, `new_avg_rss_kb`, `new_max_rss_kb`.
- `scripts/jbr-skia-benchmark-suite.sh` now writes `suite.tsv` with:
  - case name
  - validation status
  - fallback count
  - old/new sample counts
  - old/new average CPU (`na` when sample count is zero)
  - app FPS
  - JBR picture/command FPS
  - JBR command frame count
  - report path.
- Magic Jewel README documents the `suite.tsv` structure and the `na` CPU value for missing samples.

Validation:
- Syntax check:
  - command: `bash -n scripts/jbr-skia-benchmark-suite.sh scripts/jbr-skia-interop-report.sh scripts/test-jbr-skia-report-validation.sh`
  - result: passed.
- Parser fixtures:
  - command: `bash scripts/test-jbr-skia-report-validation.sh`
  - result: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`.
- Short suite smoke:
  - command: `OUT_ROOT=/tmp/magic-jewel-benchmark-suite-tsv-smoke-2 DURATION_SECONDS=2 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-benchmark-suite.sh`
  - result: `JBR_SKIA_BENCHMARK_SUITE passed out_root=/tmp/magic-jewel-benchmark-suite-tsv-smoke-2`
  - `suite.tsv` rows all passed.
  - sample handling examples:
    - `picture`: `old_samples=2`, `new_samples=0`, `new_avg_cpu=na`
    - `commands-dynamic-images`: `old_samples=2`, `new_samples=1`, `new_avg_cpu=120.90`

Next:
- Investigate why short new-mode runs sometimes produce zero `ps` samples before relying on CPU columns for benchmark conclusions; async-profiler and FPS/command timing remain the stronger signals.

## Checkpoint: Color Filter And Path Effect Fallback Probes

Date: 2026-04-29

Status: completed as live recorder-level fallback coverage.

Why:
- Color filters and path effects are common `Paint` features that currently require Skia-owned objects outside the command subset.
- Strict command mode should reject them cleanly and fall back to picture replay rather than attempting a partial JBR command render.

Changes:
- Magic Jewel added two opt-in probes:
  - `MAGIC_JEWEL_COMPOSE_COLOR_FILTER=true`
  - `MAGIC_JEWEL_COMPOSE_PATH_EFFECT=true`.
- The command-probe suite gained:
  - `commands-color-filter-fallback`
  - `commands-path-effect-fallback`.
- README documents the new manual report commands and suite coverage.

Validation:
- Syntax check:
  - command: `bash -n scripts/jbr-skia-interop-report.sh scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
- Compile:
  - command: `./gradlew --no-daemon compileKotlin`
  - result: passed.
- Focused command-probe suite:
  - command: `OUT_ROOT=/tmp/magic-jewel-paint-effect-fallback-suite-smoke CASES="commands-color-filter-fallback commands-path-effect-fallback" DURATION_SECONDS=3 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: `JBR_SKIA_COMMAND_PROBE_SUITE passed out_root=/tmp/magic-jewel-paint-effect-fallback-suite-smoke`
  - `commands-color-filter-fallback`: `status=passed`, `fallback_new_count=0`, `unsupported=colorFilter:145`, `jbr_picture_frames=145`, `jbr_command_frames=0`.
  - `commands-path-effect-fallback`: `status=passed`, `fallback_new_count=0`, `unsupported=pathEffect:227`, `jbr_picture_frames=227`, `jbr_command_frames=0`.

Note:
- These are recorder-level operation fallbacks, not compatibility-gate fallbacks. The expected machine-readable signal is `cmp_unsupported_reasons` plus picture replay and zero command frames, not `SKIKO_JBR_INTEROP_FALLBACK`.

Next:
- Keep generic shader factory work deferred; continue adding explicit fallback probes for unsupported paint/object families as they show up in real Jewel content.

## Checkpoint: Command Probe Suite TSV Summary

Date: 2026-04-29

Status: completed as command-probe harness hardening.

Change:
- Magic Jewel `scripts/jbr-skia-command-probe-suite.sh` now writes `suite.tsv`.
- Each row records:
  - case name
  - validation status
  - compatibility fallback count
  - recorder unsupported reasons
  - JBR picture frame count
  - JBR command frame count
  - JBR command FPS
  - report path.
- README documents the suite summary file.

Validation:
- Syntax check:
  - command: `bash -n scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
- Focused suite smoke:
  - command: `OUT_ROOT=/tmp/magic-jewel-command-suite-tsv-smoke CASES=commands-color-filter-fallback DURATION_SECONDS=2 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: `JBR_SKIA_COMMAND_PROBE_SUITE passed out_root=/tmp/magic-jewel-command-suite-tsv-smoke`
  - `suite.tsv`: `commands-color-filter-fallback passed 0 colorFilter:396 396 0 0.0 .../report.md`.

Next:
- Use the command-probe `suite.tsv` alongside the benchmark and artifact matrix summaries for handoff/CI jobs.

## Checkpoint: Core Primitive Screenshot Region Assertions

Date: 2026-04-30

Status: completed as screenshot-oracle hardening for the command core-primitives probe.

Why:
- The command screenshot assertion already checked broad scene colors, text presence, popup/menu pixels, and zero-picture command-mode report counters.
- The core-primitives probe enables several extra draw operations, but a missing optional probe could still pass if the base scene had enough similar colors elsewhere.

Changes:
- `scripts/assert-jbr-skia-command-window-screenshot.sh` now records env-gated region counts for:
  - top-left cyan probe area: clip/clip-out
  - bottom-left cyan probe area: transform
  - right-side purple probe area: rounded rectangle
  - right-side orange probe area: draw path
  - right-side cyan probe area: clip path / draw arc.
- The checks only run when the corresponding `MAGIC_JEWEL_COMPOSE_*` probe environment variable is enabled, so normal command-mode screenshots remain broad-scene checks.

Validation:
- Calibrated against an existing core-primitives screenshot:
  - command: `MAGIC_JEWEL_COMPOSE_IMAGE=true MAGIC_JEWEL_COMPOSE_TRANSFORM=true MAGIC_JEWEL_COMPOSE_SAVELAYER=true MAGIC_JEWEL_COMPOSE_CLIP=true MAGIC_JEWEL_COMPOSE_CLIP_OUT=true MAGIC_JEWEL_COMPOSE_CLIP_PATH=true MAGIC_JEWEL_COMPOSE_DRAW_PATH=true MAGIC_JEWEL_COMPOSE_DRAW_ARC=true MAGIC_JEWEL_COMPOSE_DRAW_ROUND_RECT=true scripts/assert-jbr-skia-command-window-screenshot.sh /tmp/magic-jewel-core-primitives-probe-region-smoke/commands-core-primitives/new-window.png`
  - result: passed.
  - counts: `probeTopLeftCyan=7564`, `probeBottomLeftCyan=4417`, `probeRightPurple=5428`, `probeRightOrange=2592`, `probeRightCyan=12606`.
- Live command-probe suite:
  - command: `OUT_ROOT=/tmp/magic-jewel-core-primitives-region-suite-smoke CASES=commands-core-primitives DURATION_SECONDS=3 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: `JBR_SKIA_COMMAND_PROBE_SUITE passed out_root=/tmp/magic-jewel-core-primitives-region-suite-smoke`
  - case summary: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=289`.

Next:
- Add similarly targeted region checks for gradient probe cases if their colors prove stable enough across captures.

## Checkpoint: Gradient Screenshot Region Assertions

Date: 2026-04-30

Status: completed as screenshot-oracle hardening for gradient command probes.

Change:
- `scripts/assert-jbr-skia-command-window-screenshot.sh` now adds env-gated right-side region checks for gradient probes:
  - linear-gradient surfaces require purple gradient pixels.
  - radial-gradient surfaces require cyan gradient pixels.
  - sweep-gradient surfaces require orange gradient pixels.
  - gradient path probes use lower right-region thresholds because their shapes are smaller.

Validation:
- Calibrated against existing gradient captures:
  - surfaces counts: `probeRightPurple=8830`, `probeRightOrange=3532`, `probeRightCyan=2510`.
  - path counts: `probeRightPurple=1103`, `probeRightOrange=2169`, `probeRightCyan=868`.
- Live command-probe suite:
  - command: `OUT_ROOT=/tmp/magic-jewel-gradient-region-suite-smoke CASES="commands-gradient-surfaces commands-gradient-paths" DURATION_SECONDS=3 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: `JBR_SKIA_COMMAND_PROBE_SUITE passed out_root=/tmp/magic-jewel-gradient-region-suite-smoke`
  - `commands-gradient-surfaces`: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=355`.
  - `commands-gradient-paths`: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=175`.

Next:
- Keep screenshot assertions focused on stable deterministic probes; avoid adding fragile pixel checks for typography until there is a stronger OCR or glyph-region oracle.

## Checkpoint: Blend Mode Fallback Probe

Date: 2026-04-30

Status: completed as live recorder-level fallback coverage.

Why:
- Command mode supports `SrcOver` and the special clear-rect case, but arbitrary blend modes need Skia paint semantics that are outside the current command subset.
- Like color filters and path effects, unsupported blend modes should fall back to picture replay cleanly.

Changes:
- Magic Jewel added `MAGIC_JEWEL_COMPOSE_BLEND_MODE=true`.
- The probe draws a rectangle with `BlendMode.Plus`.
- The command-probe suite gained `commands-blend-mode-fallback`.
- README documents the manual command and suite coverage.

Validation:
- Syntax check:
  - command: `bash -n scripts/jbr-skia-interop-report.sh scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
- Compile:
  - command: `./gradlew --no-daemon compileKotlin`
  - result: passed.
- Focused command-probe suite:
  - command: `OUT_ROOT=/tmp/magic-jewel-blend-mode-fallback-suite-smoke CASES=commands-blend-mode-fallback DURATION_SECONDS=3 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: `JBR_SKIA_COMMAND_PROBE_SUITE passed out_root=/tmp/magic-jewel-blend-mode-fallback-suite-smoke`
  - case summary: `status=passed`, `fallback_new_count=0`, `unsupported=blendMode_Plus:230`, `jbr_picture_frames=230`, `jbr_command_frames=0`.

Note:
- This is another recorder-level fallback, so the expected signal is `cmp_unsupported_reasons=blendMode_Plus:...`, picture replay, and zero JBR command frames.

Next:
- Remaining generic shader work is now mostly design/production work rather than missing fallback coverage for the obvious paint-object boundaries.

## Checkpoint: saveLayer Filter Fallback Probe

Date: 2026-04-30

Status: completed as live recorder-level fallback coverage.

Why:
- Simple `saveLayer` is now part of the command subset, so the old saveLayer fallback probe no longer exercises the unsupported path.
- Layer paints with color filters still require Skia paint objects outside the current command ABI and must fall back to picture replay cleanly.

Changes:
- Magic Jewel added `MAGIC_JEWEL_COMPOSE_SAVELAYER_FILTER=true`.
- The probe calls `Canvas.saveLayer(...)` with a tinted layer paint, then draws content inside the layer.
- The command-probe suite gained `commands-save-layer-filter-fallback`.
- README documents the manual command and suite coverage.
- `ROADMAP.md` records this as completed recorder-level fallback coverage for unsupported layer paints.

Validation:
- Syntax check:
  - command: `bash -n scripts/jbr-skia-interop-report.sh scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
- Report parser tests:
  - command: `bash scripts/test-jbr-skia-report-validation.sh`
  - result: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`.
- Compile:
  - command: `./gradlew --no-daemon compileKotlin`
  - result: passed.
- Focused command-probe suite:
  - command: `OUT_ROOT=/tmp/magic-jewel-save-layer-filter-suite CASES=commands-save-layer-filter-fallback DURATION_SECONDS=3 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: `JBR_SKIA_COMMAND_PROBE_SUITE passed out_root=/tmp/magic-jewel-save-layer-filter-suite`
  - case summary: `status=passed`, `fallback_new_count=0`, `unsupported=unsupportedScope:235,saveLayer:235`, `jbr_picture_frames=236`, `jbr_command_frames=0`.
- CMP recorder unit test:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.rejectsSaveLayerWithUnsupportedBlendMode`
  - result: passed.

Note:
- This is a recorder-level operation fallback, not a compatibility-gate fallback, so the expected signal is `cmp_unsupported_reasons=saveLayer:...`, picture replay, and zero JBR command frames.
- The focused CMP unit test uses `BlendMode.Plus` as the unsupported layer-paint input because that boundary is deterministic in the recorder-only test harness; the Magic Jewel live probe keeps exercising the filtered layer-paint path.

## Checkpoint: Image Paint Fallback Probe

Date: 2026-04-30

Status: completed as live recorder-level fallback coverage plus CMP unit coverage.

Why:
- Simple image replay is supported by the command ABI, but image draws with paint features like color filters still need Skia paint objects outside the current serialized subset.
- The fallback harness should prove these image paint cases go to picture replay and do not emit partial JBR command frames.

Changes:
- Magic Jewel added `MAGIC_JEWEL_COMPOSE_IMAGE_FILTER=true`.
- The probe draws the deterministic image with a Compose `ColorFilter.tint(...)`.
- The command-probe suite gained `commands-image-filter-fallback`.
- CMP added `JbrSkiaCommandRecorderTest.rejectsImageWithUnsupportedPaint`.
- README documents the manual command and suite coverage.
- `ROADMAP.md` records both the live Magic Jewel image-paint fallback probe and CMP recorder unit coverage.

Validation:
- Syntax check:
  - command: `bash -n scripts/jbr-skia-interop-report.sh scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
- Magic Jewel compile:
  - command: `./gradlew --no-daemon compileKotlin`
  - result: passed.
- CMP recorder unit test:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.rejectsImageWithUnsupportedPaint`
  - result: passed.
- Report parser tests:
  - command: `bash scripts/test-jbr-skia-report-validation.sh`
  - result: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`.
- Focused command-probe suite:
  - command: `OUT_ROOT=/tmp/magic-jewel-image-filter-suite CASES=commands-image-filter-fallback DURATION_SECONDS=3 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: `JBR_SKIA_COMMAND_PROBE_SUITE passed out_root=/tmp/magic-jewel-image-filter-suite`
  - case summary: `status=passed`, `fallback_new_count=0`, `unsupported=image:238`, `jbr_picture_frames=238`, `jbr_command_frames=0`.

Note:
- This is another recorder-level operation fallback. The expected signal is `cmp_unsupported_reasons=image:...`, picture replay, and zero JBR command frames.

## Checkpoint: Gradient Stroke Paint Fallback Probe

Date: 2026-04-30

Status: completed as live recorder-level fallback coverage plus CMP unit coverage.

Why:
- The command ABI supports serialized gradient fills for rectangles, rounded rectangles, and paths, but not gradient stroke paint yet.
- Stroke styles change Skia paint semantics enough that they must not be encoded as gradient fill commands.

Changes:
- Magic Jewel added `MAGIC_JEWEL_COMPOSE_LINEAR_GRADIENT_STROKE=true`.
- The probe draws a stroked rectangle with `Brush.linearGradient(...)`.
- The command-probe suite gained `commands-gradient-stroke-fallback`.
- CMP added `JbrSkiaCommandRecorderTest.rejectsGradientStrokePaintInStrictMode`.
- README documents the manual command and suite coverage.
- `ROADMAP.md` records strict/live fallback coverage for unsupported gradient stroke paint.

Validation:
- Syntax check:
  - command: `bash -n scripts/jbr-skia-interop-report.sh scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
- Magic Jewel compile:
  - command: `./gradlew --no-daemon compileKotlin`
  - result: passed.
- CMP recorder unit test:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.rejectsGradientStrokePaintInStrictMode`
  - result: passed.
- Report parser tests:
  - command: `bash scripts/test-jbr-skia-report-validation.sh`
  - result: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`.
- Focused command-probe suite:
  - command: `OUT_ROOT=/tmp/magic-jewel-gradient-stroke-suite CASES=commands-gradient-stroke-fallback DURATION_SECONDS=3 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: `JBR_SKIA_COMMAND_PROBE_SUITE passed out_root=/tmp/magic-jewel-gradient-stroke-suite`
  - case summary: `status=passed`, `fallback_new_count=0`, `unsupported=linearGradientPaint:233`, `jbr_picture_frames=233`, `jbr_command_frames=0`.

Note:
- This closes one of the remaining practical generic-shader edge probes by proving gradient shader metadata with unsupported paint style falls back at the recorder boundary.

## Checkpoint: Full Command Probe Suite Sweep

Date: 2026-04-30

Status: completed as a short-duration integration sweep after adding the expanded fallback cases.

Command:
- `OUT_ROOT=/tmp/magic-jewel-full-command-probe-short DURATION_SECONDS=2 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`

Result:
- `JBR_SKIA_COMMAND_PROBE_SUITE passed out_root=/tmp/magic-jewel-full-command-probe-short`
- suite TSV: `/tmp/magic-jewel-full-command-probe-short/suite.tsv`

Case summaries:
- `commands-core-primitives`: `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=769`.
- `commands-gradient-surfaces`: `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=762`.
- `commands-gradient-paths`: `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=459`.
- `commands-popup`: `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=418`.
- `commands-popup-window`: `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=781`.
- `commands-menu`: `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=788`.
- `commands-text-image`: `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=515`.
- `commands-native-text`: `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=888`.
- `commands-shader-fallback`: `unsupported=shader:482`, `jbr_picture_frames=481`, `jbr_command_frames=0`.
- `commands-image-filter-fallback`: `unsupported=image:488`, `jbr_picture_frames=488`, `jbr_command_frames=0`.
- `commands-gradient-stroke-fallback`: `unsupported=linearGradientPaint:167`, `jbr_picture_frames=168`, `jbr_command_frames=0`.
- `commands-color-filter-fallback`: `unsupported=colorFilter:174`, `jbr_picture_frames=174`, `jbr_command_frames=0`.
- `commands-path-effect-fallback`: `unsupported=pathEffect:225`, `jbr_picture_frames=226`, `jbr_command_frames=0`.
- `commands-blend-mode-fallback`: `unsupported=blendMode_Plus:421`, `jbr_picture_frames=420`, `jbr_command_frames=0`.
- `commands-save-layer-filter-fallback`: `unsupported=unsupportedScope:487,saveLayer:487`, `jbr_picture_frames=487`, `jbr_command_frames=0`.
- `commands-invalid-gradient-fallback`: `unsupported=sweepGradientStops:471`, `jbr_picture_frames=470`, `jbr_command_frames=0`.

Note:
- Short timings are for harness confidence, not final performance numbers. The user already noted the host is noisy, so benchmark numbers remain deferred to the quiet-machine pass.

Roadmap update:
- Marked the remaining near-term generic shader fallback probes complete after covering opaque image shaders, transformed gradients, composite shaders, invalid gradient metadata, image-filter paint, gradient stroke paint, color filters, path effects, blend modes, and unsupported saveLayer paints.
- Kept the broader generic shader strategy open because true generic shader support still needs JBR-owned shader construction rather than raw `SkShader*` sharing across Skia runtimes.

## Checkpoint: Shader Factory Strategy Doc

Date: 2026-04-30

Status: completed as design documentation, without changing the command ABI.

Why:
- The remaining generic shader work is production design, not missing fallback coverage.
- Adding ABI constants before the ownership model is agreed would create compatibility churn without improving the current macOS MVP.

Change:
- Added `doc/skia-shader-factory.md`.
- The doc commits to a JBR-owned shader factory/handle table instead of raw `SkShader*` sharing.
- It scopes handles to the JBR destination context id, requires explicit capability gates, and lists candidate factory requests in priority order.
- `ROADMAP.md` now marks the short-term shader fallback work and the medium-term design sketch complete, while leaving true generic shader support as a later production item.

## Checkpoint: Text Placement Screenshot Assertions

Date: 2026-04-30

Status: completed as a stronger screenshot oracle without OCR.

Why:
- The previous command screenshot assertion counted dark pixels in broad top/bottom text regions.
- That caught disappeared text, but not text that was badly shifted or squeezed inside the region.

Change:
- `scripts/assert-jbr-skia-command-window-screenshot.sh` now reports `topTextBox` and `bottomTextBox` bounding boxes for dark text pixels.
- The assertion checks loose width, height, and vertical-anchor constraints for the top and bottom text regions.
- `ROADMAP.md` now records screenshot-level text placement assertions as complete.

Validation:
- Existing capture calibration:
  - command: `MAGIC_JEWEL_COMPOSE_IMAGE=true MAGIC_JEWEL_COMPOSE_TRANSFORM=true MAGIC_JEWEL_COMPOSE_SAVELAYER=true MAGIC_JEWEL_COMPOSE_CLIP=true MAGIC_JEWEL_COMPOSE_CLIP_OUT=true MAGIC_JEWEL_COMPOSE_CLIP_PATH=true MAGIC_JEWEL_COMPOSE_DRAW_PATH=true MAGIC_JEWEL_COMPOSE_DRAW_ARC=true MAGIC_JEWEL_COMPOSE_DRAW_ROUND_RECT=true scripts/assert-jbr-skia-command-window-screenshot.sh /tmp/magic-jewel-full-command-probe-short/commands-core-primitives/new-window.png`
  - result: passed.
  - measured boxes: `topTextBox=139,159,954,284`, `bottomTextBox=209,1176,1002,1289`.
- Live focused command-probe suite:
  - command: `OUT_ROOT=/tmp/magic-jewel-text-placement-suite CASES=commands-core-primitives DURATION_SECONDS=2 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: `JBR_SKIA_COMMAND_PROBE_SUITE passed out_root=/tmp/magic-jewel-text-placement-suite`
  - case summary: `status=passed`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=390`.

Note:
- This remains a placement guardrail, not a glyph-metric or OCR assertion.

## Checkpoint: Artifact Matrix Required-Old Guard

Date: 2026-04-30

Status: completed as artifact-matrix CI ergonomics.

Why:
- The artifact matrix can only run full old/new rows when old bundles are supplied locally.
- For local development, skipped optional rows are useful; for CI jobs that promise old bundles, skipped rows should fail loudly.

Change:
- `scripts/jbr-skia-artifact-matrix.sh` added `REQUIRE_OLD_ARTIFACT_ROWS=true`.
- When enabled, the script fails if any optional old-artifact row is skipped.
- `README.md` documents the guard.
- `ROADMAP.md` records this as complete while keeping the real old-bundle matrix pass open until artifacts are supplied.

Validation:
- Default dry-run:
  - command: `DRY_RUN=true OUT_ROOT=/tmp/magic-jewel-artifact-matrix-dry bash scripts/jbr-skia-artifact-matrix.sh --dry-run`
  - result: passed with five optional old rows skipped.
- Required-old dry-run:
  - command: `DRY_RUN=true REQUIRE_OLD_ARTIFACT_ROWS=true OUT_ROOT=/tmp/magic-jewel-artifact-matrix-required-dry bash scripts/jbr-skia-artifact-matrix.sh --dry-run`
  - result: failed as expected with `JBR_SKIA_ARTIFACT_MATRIX failed: 5 optional old-artifact rows were skipped`.

## Checkpoint: Host Load Metadata In Reports

Date: 2026-04-30

Status: completed as benchmark/report context metadata.

Why:
- CPU and FPS numbers are only useful when paired with host-load context, especially while other agents/builds are running on the same machine.

Change:
- `scripts/jbr-skia-interop-report.sh` now writes `host_cpu_count`, `host_load_1m`, `host_load_5m`, and `host_load_15m` to `summary.properties`.
- `report.md` includes the same host CPU/load metadata near the top.
- README lists the new machine-readable summary keys.
- Report validation tests assert the keys are present.
- `ROADMAP.md` records host-load report context as complete.

Validation:
- Syntax check:
  - command: `bash -n scripts/jbr-skia-interop-report.sh scripts/test-jbr-skia-report-validation.sh`
  - result: passed.
- Report parser tests:
  - command: `bash scripts/test-jbr-skia-report-validation.sh`
  - result: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`.
- Real report smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-host-load-summary-smoke DURATION_SECONDS=2 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
  - result: passed with report `/tmp/magic-jewel-host-load-summary-smoke/report.md`.
  - summary keys observed: `host_cpu_count=10`, `host_load_1m=3.90`, `host_load_5m=4.38`, `host_load_15m=5.20`.

## Checkpoint: Longer Quiet-Machine Benchmark Collection

Date: 2026-04-30

Status: completed as a 30-second-per-mode benchmark-suite pass with host-load metadata attached.

Command:
- `OUT_ROOT=/tmp/magic-jewel-quiet-benchmark-20260430-102300 DURATION_SECONDS=30 WARMUP_SECONDS=5 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ENABLE_ASPROF=false bash scripts/jbr-skia-benchmark-suite.sh`

Result:
- `JBR_SKIA_BENCHMARK_SUITE passed out_root=/tmp/magic-jewel-quiet-benchmark-20260430-102300`
- suite TSV: `/tmp/magic-jewel-quiet-benchmark-20260430-102300/suite.tsv`

Case summaries:
- `picture`: `status=passed`, `fallbacks=0`, `old_avg_cpu=71.44`, `new_avg_cpu=102.05`, `app_old_fps=307.5`, `app_new_fps=231.5`, `jbr_picture_fps=231.4`, `jbr_command_fps=0.0`, `jbr_command_frames=0`.
- `commands`: `status=passed`, `fallbacks=0`, `old_avg_cpu=72.92`, `new_avg_cpu=81.12`, `app_old_fps=314.1`, `app_new_fps=249.2`, `jbr_picture_fps=0.0`, `jbr_command_fps=249.2`, `jbr_command_frames=7475`.
- `commands-stable-images`: `status=passed`, `fallbacks=0`, `old_avg_cpu=133.51`, `new_avg_cpu=126.23`, `app_old_fps=177.7`, `app_new_fps=172.3`, `jbr_picture_fps=0.0`, `jbr_command_fps=172.3`, `jbr_command_frames=5170`.
- `commands-dynamic-images`: `status=passed`, `fallbacks=0`, `old_avg_cpu=122.81`, `new_avg_cpu=129.23`, `app_old_fps=181.5`, `app_new_fps=170.0`, `jbr_picture_fps=0.0`, `jbr_command_fps=170.0`, `jbr_command_frames=5099`.
- `commands-resize-dynamic-images`: `status=passed`, `fallbacks=0`, `old_avg_cpu=133.50`, `new_avg_cpu=120.49`, `app_old_fps=166.6`, `app_new_fps=163.4`, `jbr_picture_fps=0.0`, `jbr_command_fps=163.4`, `jbr_command_frames=4903`.

Notable report details:
- Host load was lower than earlier noisy runs at start, but still not laboratory quiet during the suite:
  - `picture`: host load `5.43 3.97 3.45`
  - `commands`: host load `5.70 4.46 3.69`
  - `commands-dynamic-images`: host load `10.01 6.48 4.64`
  - `commands-resize-dynamic-images`: host load `7.46 6.40 4.78`
- The functional signal is strong across all command cases:
  - zero fallback markers;
  - zero JBR picture replay frames in command mode;
  - JBR command replay frames equal the app draw frames;
  - dynamic-image cases use per-key eviction with zero whole-cache clears;
  - resize + dynamic images records one Skiko surface-change marker while preserving `contextChanged=false surfaceChanged=true`.
- The `picture` row remains the SKP correctness/reference path and intentionally records picture replay rather than command replay.

Interpretation:
- The CPU columns are still coarse `ps` samples and should not be treated as final perf proof.
- The most meaningful result for this checkpoint is that the command path stayed strict and fallback-free under longer warmup-aware workloads, including stable images, dynamic image churn, and same-context resize.

Roadmap update:
- Marked the quiet-machine benchmark pass and long-running stable/dynamic image-cache benchmark pass complete.

Next:
- Continue macOS MVP hardening with either real old-artifact bundles for the artifact matrix or the next rendering/ownership slice, with text/font ownership and JBR-owned shader factories still deliberately deferred production items.

## Checkpoint: ABI 43 Text Font-Family Metadata

Date: 2026-04-30

Status: completed as a source-level ABI slice across JBR, public Runtime API, Skiko, CMP, and Magic Jewel docs, with a live native-text Magic Jewel smoke.

Why:
- Native JBR text commands previously carried font size/style/alignment, but not the resolved font family.
- That left JBR's SkParagraph/CoreText path free to choose a different default family than the Skiko paragraph path, which matched the observed wrong-looking Jewel text when `JBR_SKIA_NATIVE_TEXT=true`.
- Passing a font-family name keeps ownership on the correct side: CMP/Skiko may read a family name from Skiko's typeface, but the JBR runtime resolves the actual `SkTypeface` through its own CoreText-backed Skia runtime. No raw `SkTypeface*` crosses the ABI.

Changes:
- JBR command ABI bumped to `ABI_ID=43`; `BUILD_ID` now includes `abi=43`.
- Public Runtime API `JBRSkia` mirrors ABI 43 and adds `COMMAND_CAP64_TEXT_FONT_FAMILY`.
- JBR service advertises the new 64-bit capability.
- `COMMAND_DRAW_TEXT_UTF16` payload now includes:
  - `fontFamilyCharCount`, UTF-16 family code units, `charCount`, UTF-16 text code units.
- `COMMAND_DRAW_PARAGRAPH_UTF16` payload now includes:
  - font style metadata, then `fontFamilyCharCount`, UTF-16 family code units, then paragraph layout metadata and text.
- Native JBR replay resolves simple text typefaces via JBR's CoreText `SkFontMgr`.
- Native JBR paragraph replay calls `TextStyle::setFontFamilies(...)` when a family was provided.
- Java fallback replay reads the same family payload and uses it when deriving an AWT font for software/test surfaces.
- CMP recorder API now accepts nullable `fontFamily` for simple and paragraph text commands.
- CMP Skia paragraph recording passes `defaultFont.typeface?.familyName`.
- Skiko compatibility gate expects ABI 43 and the new text-family capability.
- Magic Jewel README now refers to ABI 43 for the current image-cache/text-family ABI generation.
- `ROADMAP.md` records ABI 43 text-family metadata as complete at the checklist level.

Validation:
- CMP recorder tests:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
  - result: passed.
- Skiko compatibility tests:
  - command: `./gradlew --no-daemon :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - result: passed.
- Public Runtime API compile smoke:
  - command: `javac -d /tmp/jbr-api-skia-compile src/com/jetbrains/Provided.java src/com/jetbrains/Service.java src/com/jetbrains/JBRSkia.java`
  - result: passed.
- JBR API class compile smoke:
  - command: `javac -d /tmp/jbr-runtime-skia-api-compile src/java.desktop/share/classes/com/jetbrains/desktop/JBRSkia.java`
  - result: passed.
- JBR service standalone `javac` smoke was attempted, but this is not a meaningful standalone compile target because it pulls broad JDK sources that require the configured JDK build toolchain and preview settings. It failed before isolating this file with unrelated source-tree preview/internal dependency errors.
- Diff hygiene:
  - command: `git diff --check`
  - result: passed in JBR, Runtime API, Skiko, CMP, and Magic Jewel worktrees.
- Local artifact refresh:
  - Public Runtime API snapshot rebuilt with `bash tools/build.sh process` and `bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-abi43-dev`; `/tmp/jbr-api-shim.jar` refreshed from the result.
  - Skiko `0.0.0-SNAPSHOT` republished to Maven local with ABI 43.
  - JBR patched `java.desktop` classes refreshed into `/tmp/jbr-skia-run/desktop`.
  - JBR native `libjbrskiainterop.dylib` rebuilt into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Live native-text Magic Jewel command smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-abi43-native-text-smoke-3 DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands JBR_SKIA_NATIVE_TEXT=true MAGIC_JEWEL_UNSUPPORTED_TEXT=true MAGIC_JEWEL_PARAGRAPH_LAYOUT_TEXT=true EXPECT_MIN_PARAGRAPH_TEXT_COMMANDS=4 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
  - result: passed.
  - report: `/tmp/magic-jewel-abi43-native-text-smoke-3/report.md`.
  - counters: 960 CMP recorder frames, 960 Skiko command frames, 960 JBR command frames, 0 picture replay frames, 0 fallback markers, 6 paragraph text commands per frame.
  - screenshot: `/tmp/magic-jewel-abi43-native-text-smoke-3/new-window.png`; command screenshot assertion passed.
  - timing: JBR command replay averaged 1.684 ms/frame total, including 0.494 ms draw, 1.166 ms flush, and 0.108 ms paragraph replay.

Remaining caveat:
- The native text path is still opt-in with `JBR_SKIA_NATIVE_TEXT=true`; default command mode remains fidelity-first text-as-image replay until more typography metrics, baseline, and style parity are proven across a wider Jewel surface.

Next:
- Continue hardening native text toward becoming the default by adding baseline/metrics parity checks or by documenting the remaining typography differences if the next slice proves they are JBR/Skia font-manager policy rather than ABI gaps.

## Checkpoint: ABI 43 Quiet Benchmark Refresh

Date: 2026-04-30

Status: completed after refreshing the local ABI 43 JBR, Runtime API, Skiko, and CMP artifacts.

Why:
- The earlier quiet-machine benchmark was collected before the live ABI 43 native-text artifact refresh.
- The user explicitly noted the machine was quieter, so this pass is a better smoke datapoint for the current runnable MVP.
- The SKP picture row remains useful as the reference artifact path for later quieter or profiler-backed benchmarking.

Command:
- `OUT_ROOT=/tmp/magic-jewel-quiet-benchmark-abi43-20260430-110714 DURATION_SECONDS=30 WARMUP_SECONDS=5 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ENABLE_ASPROF=false bash scripts/jbr-skia-benchmark-suite.sh`

Results:
- suite: `/tmp/magic-jewel-quiet-benchmark-abi43-20260430-110714/suite.tsv`.
- picture: passed, 0 fallbacks, old avg CPU 85.02, new avg CPU 94.89, old app FPS 404.7, new app/JBR picture FPS 233.4.
- commands: passed, 0 fallbacks, old avg CPU 85.74, new avg CPU 88.67, old app FPS 402.2, new app/JBR command FPS 314.0, 9419 JBR command frames.
- commands-stable-images: passed, 0 fallbacks, old avg CPU 127.60, new avg CPU 119.63, old app FPS 173.1, new app/JBR command FPS 174.3, 5228 JBR command frames.
- commands-dynamic-images: passed, 0 fallbacks, old avg CPU 125.06, new avg CPU 110.68, old app FPS 173.6, new app/JBR command FPS 169.7, 5091 JBR command frames.
- commands-resize-dynamic-images: passed, 0 fallbacks, old avg CPU 114.93, new avg CPU 112.69, old app FPS 164.8, new app/JBR command FPS 167.4, 5023 JBR command frames, 1 Skiko surface-change marker.

Interpretation:
- All current benchmark rows remain strict and fallback-free with zero picture replay in command rows.
- CPU still comes from coarse `ps` sampling and the host load was not zero (`host_load_1m` ranged roughly 4.87-6.18), but this pass is a better checkpoint than the previous noisier run.
- The image-cache workloads now show the new path slightly lower or roughly tied on CPU while preserving the same-context resize/surface-change signal.

Roadmap update:
- Marked the ABI 43 quiet benchmark refresh complete and kept the exact suite/SKP report root in this plan.

Next:
- Move from measurement refresh back to rendering parity: either native text baseline/style parity or the next unsupported rendering family that still forces text/image fallback.

## Checkpoint: ABI 43 Command Probe Suite Refresh

Date: 2026-04-30

Status: completed against the refreshed ABI 43 artifacts.

Why:
- After rebuilding local JBR classes/native dylib, public Runtime API shim, Skiko snapshot, and CMP jars, the broad command probe suite needed to prove that the text-family ABI change did not regress primitive, gradient, popup/layering, native-text, or expected-fallback behavior.

Command:
- `OUT_ROOT=/tmp/magic-jewel-command-probe-abi43-20260430-111549 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`

Results:
- suite: `/tmp/magic-jewel-command-probe-abi43-20260430-111549/suite.tsv`.
- strict command rows passed with zero fallbacks and zero picture replay:
  - core primitives: 1787 JBR command frames.
  - gradient surfaces: 1880 JBR command frames.
  - gradient paths: 1900 JBR command frames.
  - glass-pane popup: 1869 JBR command frames.
  - popup window: 1663 JBR command frames.
  - Swing menu popup: 1527 JBR command frames.
  - text-as-image default: 1689 JBR command frames.
  - native text opt-in: 900 JBR command frames.
- deliberate fallback rows passed by producing picture replay and the expected unsupported reasons:
  - shader, image filter, gradient stroke paint, color filter, path effect, plus blend mode, saveLayer filter, and invalid sweep-gradient stops.

Interpretation:
- ABI 43 native text is no longer only a focused smoke: it is covered by the broad command probe suite and remains strict/fallback-free.
- The existing expected-fallback safety rails still work after the ABI refresh.

Roadmap update:
- Recorded the ABI 43 command-probe suite refresh and its report root.

Next:
- Start the next rendering parity slice. The most useful candidates are native text baseline/style parity, or a JBR-owned shader factory for unsupported shader families.

## Checkpoint: ABI 44 Image-Shader Rect Fill

Date: 2026-04-30

Status: completed as a narrow serialized shader-family slice across JBR, public Runtime API, Skiko, CMP, and Magic Jewel.

Why:
- Generic shader support is still deliberately deferred because raw `SkShader*` pointers cannot cross the Skiko/JBR Skia runtime boundary safely.
- Compose `ImageShader(image, tileModeX, tileModeY)` is a tractable known shader family: CMP already owns the `ImageBitmap`, the command stream already has image cache definitions, and JBR can reconstruct the actual Skia image shader inside the JBR-owned Skia runtime.
- This gives us real shader-backed paint coverage without weakening the strict fallback rules for opaque/nonserializable shader wrappers.

Changes:
- JBR command ABI bumped to `ABI_ID=44`; `BUILD_ID` now includes `abi=44`.
- Public Runtime API `JBRSkia` mirrors ABI 44 and adds `COMMAND_CAP64_FILL_RECT_IMAGE_SHADER` plus `COMMAND_FILL_RECT_IMAGE_SHADER`.
- `COMMAND_FILL_RECT_IMAGE_SHADER` payload records a destination rect, cached image key, image dimensions, horizontal/vertical tile modes, and `alpha1000`.
- JBR Java validation rejects malformed image-shader records and advertises the new 64-bit capability.
- Native JBR replay looks up the context-scoped `SkImage`, reconstructs an `SkShader` in JBR's Skia runtime with the recorded tile modes, and fills the destination rect.
- CMP desktop shader metadata now preserves the source `ImageBitmap` and tile modes for plain `ImageShader` construction.
- CMP command recording emits image-shader rect fills only for simple fill-style `SrcOver` paints with no color filter/path effect and a cacheable bitmap.
- Skiko compatibility now requires ABI 44 and the image-shader capability bit before enabling command mode.
- Magic Jewel's image-shader probe now draws a real tiled image shader instead of a synthetic unsupported-shader marker.
- Magic Jewel compiles against the same patched CMP jars that it prepends at runtime, preventing compile/runtime Compose API signature drift in local PoC runs.
- Screenshot assertions now check the visible tiled image-shader probe with right-region yellow/dark pixel counts.

Validation:
- CMP focused recorder test:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
  - result: passed.
- JBR API class compile smoke:
  - command: `javac -d /tmp/jbr-runtime-skia-api-compile src/java.desktop/share/classes/com/jetbrains/desktop/JBRSkia.java`
  - result: passed.
- JBR service restricted compile smoke:
  - result: passed with patched output in `/tmp/jbr-skia-run/desktop`; the temporary `com/jetbrains/exported` stub was removed from the patch tree afterward.
- JBR native dylib compile smoke:
  - result: passed; `/tmp/jbr-skia-native/libjbrskiainterop.dylib` rebuilt with ABI 44.
- Public Runtime API compile/build:
  - command: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-abi44-dev`
  - result: passed; `/tmp/jbr-api-shim.jar` refreshed.
- Skiko compatibility tests:
  - command: `./gradlew --no-daemon :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - result: passed.
- Skiko local artifact:
  - command: `./gradlew --no-daemon :skiko:publishToMavenLocal`
  - result: passed.
- Magic Jewel compile against patched CMP:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon compileKotlin --rerun-tasks`
  - result: passed.
- Focused Magic Jewel image-shader smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-abi44-image-shader-smoke-3 DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_IMAGE_SHADER=true EXPECT_MIN_IMAGE_REFS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
  - result: passed.
  - report: `/tmp/magic-jewel-abi44-image-shader-smoke-3/report.md`.
  - counters: 429 CMP recorder frames, 430 Skiko command frames, 430 JBR command frames, 0 picture replay frames, 0 fallback markers, 10 image refs per frame, max 1 image define.
  - screenshot: `/tmp/magic-jewel-abi44-image-shader-smoke-3/new-window.png`; command screenshot assertion passed with `probeRightYellow=3346` and `probeRightDark=21514`.
- Focused command-probe row:
  - command: `OUT_ROOT=/tmp/magic-jewel-command-probe-abi44-image-shader-20260430-115855 CASES=commands-image-shader SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: passed; 1,918 JBR command frames, 0 fallbacks, 0 picture replay frames.
- Full command-probe suite:
  - command: `OUT_ROOT=/tmp/magic-jewel-command-probe-abi44-full-20260430-115943 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
  - suite: `/tmp/magic-jewel-command-probe-abi44-full-20260430-115943/suite.tsv`.
  - strict rows passed with zero fallback and zero picture replay, including `commands-image-shader` with 1,968 JBR command frames.
  - deliberate fallback rows still passed with expected unsupported reasons for image filter, gradient stroke paint, color filter, path effect, blend mode, saveLayer filter, and invalid sweep-gradient stops.

Remaining caveat:
- This is not generic shader support. It is a serialized known-family shader command where JBR reconstructs the shader from image cache refs and tile-mode metadata. Opaque/transformed/composite/nonserializable shader objects still fall back by design.

Next:
- Continue macOS MVP hardening with either native text baseline/style parity, productionizing JBR-owned shader factory design for broader shader families, or packaging a full old/new artifact matrix with real old bundles.

## Checkpoint: Paragraph Text Screenshot Guardrails

Date: 2026-04-30

Status: completed as validation hardening for the native-text parity slice.

Why:
- Native text remains opt-in and visually close, but the existing screenshot assertion only checked broad top/bottom text presence.
- The paragraph layout probe intentionally exercises centered bold text, right-aligned italic text, RTL text, ellipsis/overflow, and decorated text. Those rows should have their own stable pixel tripwires before native text becomes a serious default-candidate.

Changes:
- Magic Jewel command screenshot assertions now emit and validate paragraph-row dark-pixel counters when `MAGIC_JEWEL_PARAGRAPH_LAYOUT_TEXT=true`:
  - `paragraphCentered`
  - `paragraphItalicRight`
  - `paragraphRtl`
  - `paragraphOverflow`
  - `paragraphDecorated`
- `ROADMAP.md` now records paragraph-row screenshot assertions as complete.

Validation:
- Text parity probe run:
  - command: `OUT_ROOT=/tmp/magic-jewel-text-parity-20260430-121318 CASES="commands-text-image commands-native-text" SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
  - suite: `/tmp/magic-jewel-text-parity-20260430-121318/suite.tsv`.
  - text-as-image row: 738 JBR command frames, 0 fallback markers, 0 picture replay frames.
  - native-text row: 998 JBR command frames, 0 fallback markers, 0 picture replay frames, 9 simple text commands and 6 paragraph text commands per frame.
- New assertion replay against captured reference screenshots:
  - text-as-image screenshot passed with paragraph row counts `paragraphCentered=3601`, `paragraphItalicRight=2339`, `paragraphRtl=2410`, `paragraphOverflow=3538`, `paragraphDecorated=1745`.
  - native-text screenshot passed with paragraph row counts `paragraphCentered=6693`, `paragraphItalicRight=1974`, `paragraphRtl=2573`, `paragraphOverflow=3781`, `paragraphDecorated=954`.
- Magic Jewel report validation tests:
  - command: `bash scripts/test-jbr-skia-report-validation.sh`
  - result: passed.

Next:
- Use these stricter paragraph guardrails while investigating native text baseline/style parity or while deciding whether native text can replace text-as-image for a narrower subset of Jewel labels.

## Checkpoint: Machine-Readable Screenshot Counters

Date: 2026-04-30

Status: completed as parser-level validation plumbing.

Why:
- Screenshot assertions already print useful pixel counters, including the new paragraph-row counters and image-shader probe counters.
- CI and benchmark jobs should not have to scrape Markdown to compare those fields across runs.

Changes:
- Magic Jewel `summary.properties` now includes scalar screenshot count fields parsed from assertion logs:
  - main-window fields use the `screenshot_` prefix, for example `screenshot_paragraphCentered` and `screenshot_probeRightDark`.
  - popup-window fields use the `popup_window_screenshot_` prefix.
- Bounding-box tuple fields such as `topTextBox=...` remain in the human report only because they are not scalar `key=value` counters.
- Magic Jewel README documents the screenshot counter keys.
- `ROADMAP.md` records parser-level screenshot count fields as complete.

Validation:
- Magic Jewel report validation tests:
  - command: `bash scripts/test-jbr-skia-report-validation.sh`
  - result: passed.
- A real native-text report was revalidated with `--validate-only` to confirm the summary writer still accepts existing report directories.

Next:
- Use the new summary keys in later quiet benchmark comparisons and native-text parity tracking.

## Checkpoint: ABI 44 Quiet Benchmark Refresh

Date: 2026-04-30

Status: completed against the committed ABI 44 artifacts.

Why:
- ABI 44 changed the command capability gate and added image-shader command replay, so the earlier ABI 43 benchmark was no longer the best reference for the current local state.
- The machine was quiet enough to collect a more useful coarse CPU/FPS checkpoint, while still treating `ps` CPU as noisy smoke data.

Command:
- `OUT_ROOT=/tmp/magic-jewel-quiet-benchmark-abi44-20260430-122457 DURATION_SECONDS=30 WARMUP_SECONDS=5 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ENABLE_ASPROF=false bash scripts/jbr-skia-benchmark-suite.sh`

Results:
- suite: `/tmp/magic-jewel-quiet-benchmark-abi44-20260430-122457/suite.tsv`.
- picture: passed, 0 fallbacks, old avg CPU 72.27, new avg CPU 95.53, old app FPS 322.3, new app/JBR picture FPS 228.1.
- commands: passed, 0 fallbacks, old avg CPU 86.92, new avg CPU 88.08, old app FPS 393.4, new app/JBR command FPS 311.3, 9,340 JBR command frames.
- commands-stable-images: passed, 0 fallbacks, old avg CPU 132.82, new avg CPU 110.89, old app FPS 172.9, new app/JBR command FPS 171.3, 5,138 JBR command frames.
- commands-dynamic-images: passed, 0 fallbacks, old avg CPU 130.23, new avg CPU 110.76, old app FPS 172.2, new app/JBR command FPS 166.4, 4,993 JBR command frames.
- commands-resize-dynamic-images: passed, 0 fallbacks, old avg CPU 127.00, new avg CPU 116.88, old app FPS 172.6, new app/JBR command FPS 167.2, 5,016 JBR command frames.

Interpretation:
- All command rows stayed strict and fallback-free with zero picture replay.
- The plain command row is roughly tied on coarse CPU in this pass.
- Stable/dynamic image-cache rows show the command path lower on coarse CPU, while keeping FPS in the same range.
- The SKP/picture row remains the useful retained picture artifact path for later profiler-backed comparisons.

Roadmap update:
- Recorded the ABI 44 quiet benchmark suite path.

Next:
- Continue native text parity work or move to a broader shader-factory/unsupported-family production slice, using the ABI 44 benchmark as the current local reference.

## Checkpoint: ABI 45 Linear-Gradient Stroke Rect

Date: 2026-04-30

Status: completed as another narrow serialized shader-family slice across JBR, public Runtime API, Skiko, CMP, and Magic Jewel.

Why:
- Gradient stroke paint had deliberately fallen back because the earlier command ABI only serialized gradient fills.
- Compose/Jewel can produce simple stroked rectangles with `Brush.linearGradient(...)`; this is still a known shader family with scalar stroke metadata, so it can be replayed safely by reconstructing the shader inside JBR's Skia runtime.
- This keeps generic/opaque shader pointers out of the ABI while shrinking the practical fallback surface.

Changes:
- JBR command ABI bumped to `ABI_ID=45`; `BUILD_ID` now includes `abi=45`.
- Public Runtime API and private JBR `JBRSkia` expose `COMMAND_CAP64_STROKE_RECT_LINEAR_GRADIENT` plus `COMMAND_STROKE_RECT_LINEAR_GRADIENT`.
- CMP records fill-style linear gradients as before and now records stroke-style linear-gradient rectangles when the paint is otherwise simple `SrcOver`.
- The new command payload carries:
  - rect bounds in fixed-point user coordinates,
  - stroke width/cap/join/miter metadata,
  - linear-gradient endpoints, tile mode, ARGB colors, and strictly increasing stops.
- JBR Java validation rejects malformed gradient-stroke records and Java2D fallback replay maps them to `LinearGradientPaint` plus `BasicStroke`.
- JBR native replay reconstructs `SkShaders::LinearGradient`, applies stroke style metadata, and draws the rect without sharing any `SkShader*` or `SkPaint*` from Skiko.
- Skiko compatibility now requires ABI 45 and the gradient-stroke capability bit before enabling command mode.
- Magic Jewel's `MAGIC_JEWEL_COMPOSE_LINEAR_GRADIENT_STROKE=true` probe is now a strict rendering probe instead of an expected fallback row.
- Command screenshot assertions require both cyan and orange pixels in the right-side probe region for the gradient-stroke case.

Validation:
- CMP focused recorder tests:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesImageShaderRectRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesLinearGradientStrokeRectRecord`
  - result: passed.
- JBR private API/service compile smoke:
  - result: passed for `JBRSkia.java`, `JBRSkiaService.java`, and the updated `JBRSkiaApiTest.java` compile.
- JBR native dylib compile smoke:
  - result: passed; `/tmp/jbr-skia-native/libjbrskiainterop.dylib` rebuilt with ABI 45.
- Runtime API shim rebuild:
  - command: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-abi45-dev`
  - result: passed; copied to `/tmp/jbr-api-shim.jar`.
- Skiko compatibility tests:
  - command: `./gradlew --no-daemon :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - result: passed.
- Magic Jewel compile:
  - command: `./gradlew --no-daemon compileKotlin`
  - result: passed.
- Focused Magic Jewel gradient-stroke smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-abi45-gradient-stroke-smoke DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_LINEAR_GRADIENT_STROKE=true SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
  - result: passed.
  - report: `/tmp/magic-jewel-abi45-gradient-stroke-smoke/report.md`.
  - counters: `jbr_command_frames=676`, `jbr_command_fps=112.7`, `fallback_new_count=0`, `cmp_unsupported_reasons=none`, `screenshot_probeRightCyan=1394`, `screenshot_probeRightOrange=1466`.
- Focused Magic Jewel command-probe row:
  - command: `OUT_ROOT=/tmp/magic-jewel-command-probe-abi45-gradient-stroke-20260430-125550 CASES=commands-gradient-stroke SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
  - suite: `/tmp/magic-jewel-command-probe-abi45-gradient-stroke-20260430-125550/suite.tsv`.
  - row: `commands-gradient-stroke`, `fallbacks=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=1858`.
- Full Magic Jewel command-probe suite refresh:
  - command: `OUT_ROOT=/tmp/magic-jewel-command-probe-abi45-full-20260430-130148 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
  - suite: `/tmp/magic-jewel-command-probe-abi45-full-20260430-130148/suite.tsv`.
  - strict command rows passed with zero fallback and zero picture replay, including `commands-gradient-stroke` with 1,994 JBR command frames.
  - deliberate fallback rows still passed with zero JBR command frames and expected unsupported reasons for image filter, color filter, path effect, blend mode, saveLayer filter, and invalid sweep-gradient stops.

Roadmap update:
- Recorded ABI 45 command coverage.
- Reclassified gradient-stroke paint from deliberate fallback to supported serialized command replay.
- Recorded the focused smoke, focused command-probe row, and full command-probe suite paths.

Next:
- Continue macOS MVP hardening with either native text parity, packaged old/new artifact matrix bundles, or the next narrow known-family rendering slice.

## Checkpoint: ABI 45 Quiet Benchmark Refresh

Date: 2026-04-30

Status: completed against the committed ABI 45 artifacts.

Why:
- ABI 45 changed the command capability gate and added gradient-stroke command replay, so the ABI 44 quiet benchmark is no longer the freshest local baseline.
- The machine was reported quiet, making it a good moment to keep the SKP/picture path and command/image-cache rows around for later profiler-backed comparisons.

Command:
- `OUT_ROOT=/tmp/magic-jewel-quiet-benchmark-abi45-20260430-131201 DURATION_SECONDS=30 WARMUP_SECONDS=5 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ENABLE_ASPROF=false bash scripts/jbr-skia-benchmark-suite.sh`

Results:
- suite: `/tmp/magic-jewel-quiet-benchmark-abi45-20260430-131201/suite.tsv`.
- picture: passed, 0 fallbacks, old avg CPU 67.80, new avg CPU 95.50, old app FPS 273.7, new app/JBR picture FPS 228.2.
- commands: passed, 0 fallbacks, old avg CPU 86.54, new avg CPU 87.54, old app FPS 392.9, new app/JBR command FPS 311.2, 9,337 JBR command frames.
- commands-stable-images: passed, 0 fallbacks, old avg CPU 112.50, new avg CPU 126.88, old app FPS 175.3, new app/JBR command FPS 172.7, 5,182 JBR command frames.
- commands-dynamic-images: passed, 0 fallbacks, old avg CPU 126.88, new avg CPU 119.95, old app FPS 173.1, new app/JBR command FPS 168.1, 5,043 JBR command frames.
- commands-resize-dynamic-images: passed, 0 fallbacks, old avg CPU 106.98, new avg CPU 120.02, old app FPS 159.2, new app/JBR command FPS 163.3, 4,898 JBR command frames.

Interpretation:
- All rows stayed strict and fallback-free.
- The plain command row remains roughly tied on coarse CPU while avoiding picture replay.
- Dynamic image-cache churn is lower on coarse CPU in the command path in this pass.
- Stable-image and resize-dynamic rows are higher on coarse CPU in this pass, so they remain candidates for profiler-backed investigation instead of being treated as wins.
- The SKP/picture row remains available as the retained picture baseline for later benchmark/profiler comparisons.

Roadmap update:
- Recorded the ABI 45 quiet benchmark suite path.

Next:
- Continue macOS MVP hardening. Good candidates are native text baseline/style parity, real old-artifact bundles for the artifact matrix, or a small profiler-backed look at the image-cache benchmark rows where coarse CPU regressed.

## Checkpoint: Targeted Benchmark Case Selection

Date: 2026-04-30

Status: completed as harness ergonomics for profiler-backed follow-up.

Why:
- The ABI 45 quiet benchmark kept the full SKP/command/image-cache suite, but later investigation should be able to rerun only suspicious rows with async-profiler or longer durations.
- `asprof` was not on PATH in the current shell, so the immediate improvement is making targeted reruns cheap once a profiler path is supplied.

Changes:
- Magic Jewel `scripts/jbr-skia-benchmark-suite.sh` now accepts `CASES`, matching the command-probe suite pattern.
- Supported benchmark case names:
  - `picture`
  - `commands`
  - `commands-stable-images`
  - `commands-dynamic-images`
  - `commands-resize-dynamic-images`
- Unknown benchmark case names fail fast.
- Magic Jewel README documents subset usage, for example `CASES="commands-stable-images commands-resize-dynamic-images"`.
- `ROADMAP.md` records benchmark case selection as complete.

Validation:
- Shell syntax check:
  - command: `bash -n scripts/jbr-skia-benchmark-suite.sh`
  - result: passed.
- Focused one-row benchmark smoke:
  - command: `OUT_ROOT=/tmp/magic-jewel-benchmark-cases-smoke-20260430-132120 CASES=commands DURATION_SECONDS=4 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ENABLE_ASPROF=false bash scripts/jbr-skia-benchmark-suite.sh`
  - result: passed.
  - suite: `/tmp/magic-jewel-benchmark-cases-smoke-20260430-132120/suite.tsv`.
  - row: `commands`, `fallbacks=0`, `jbr_picture_frames=0`, `jbr_command_frames=543`.

Next:
- Use `CASES` for targeted profiler-backed reruns once `ASPROF=/path/to/asprof` is available, or continue with the next macOS MVP hardening slice.

## Checkpoint: ABI 46 Linear-Gradient Stroke Round Rect

Date: 2026-04-30

Status: completed for Compose linear-gradient stroked rounded-rectangle command recording and JBR/Skia replay.

Why:
- ABI 45 covered linear-gradient stroked rectangles, but rounded rectangles still depended on either fill-only gradient replay or fallback/approximation for stroked outlines.
- Magic Jewel already had a gradient rounded-rectangle probe; adding a gradient stroke on top makes it validate both fill and stroke rounded-rect replay in the same visual region.

Changes:
- JBR command ABI bumped to `ABI_ID=46`; `BUILD_ID` now includes `abi=46`.
- Added 64-bit command capability `COMMAND_CAP64_STROKE_ROUND_RECT_LINEAR_GRADIENT = 8796093022208L`.
- Added command opcode `COMMAND_STROKE_ROUND_RECT_LINEAR_GRADIENT = 36`.
- The new payload carries rect bounds, independent X/Y radii, stroke width/cap/join/miter metadata, and linear-gradient endpoints/tile/colors/stops.
- CMP records `drawRoundRect(brush = Brush.linearGradient(...), style = Stroke(...))` when the paint is otherwise strict-command-compatible.
- JBR Java validation rejects malformed radii, stroke metadata, gradient metadata, and stop/color counts; Java2D fallback maps the record to `RoundRectangle2D` with `LinearGradientPaint` and `BasicStroke`.
- JBR native replay reconstructs `SkShaders::LinearGradient`, applies stroke style metadata, builds an `SkRRect`, and draws it through the JBR-owned Skia runtime.
- Skiko compatibility now requires ABI 46 and the rounded-rectangle gradient-stroke capability bit before enabling command mode.
- Magic Jewel's `MAGIC_JEWEL_COMPOSE_LINEAR_GRADIENT_ROUND_RECT=true` probe now includes a cyan/orange linear-gradient stroke over the existing gradient rounded-rectangle fill.
- Screenshot assertions now distinguish plain linear-gradient rectangle coverage from rounded-rectangle coverage and require purple fill plus cyan/orange stroke pixels for the rounded probe.

Validation:
- CMP TDD pass:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesLinearGradientStrokeRoundRectRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesLinearGradientRoundRectRecord`
  - result: passed after the new recorder implementation.
- JBR private API/service/test compile smoke:
  - result: passed for `JBRSkia.java`, `JBRSkiaService.java`, and the updated `JBRSkiaApiTest.java` compile.
- JBR native dylib compile smoke:
  - result: passed; `/tmp/jbr-skia-native/libjbrskiainterop.dylib` rebuilt with ABI 46 using the scoped Skiko Skia m147 arm64 artifacts.
- Runtime API shim rebuild:
  - command: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-abi46-dev`
  - result: passed; copied to `/tmp/jbr-api-shim.jar`.
- Skiko compatibility tests:
  - command: `./gradlew --no-daemon :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - result: passed.
- Skiko local publish:
  - command: `./gradlew --no-daemon :skiko:publishToMavenLocal`
  - result: passed.
- CMP desktop jars refresh:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`
  - result: passed.
- Magic Jewel compile:
  - command: `./gradlew --no-daemon compileKotlin`
  - result: passed.
- Focused Magic Jewel gradient stroke rounded-rectangle smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-abi46-gradient-stroke-round-rect-smoke-2 DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_LINEAR_GRADIENT_ROUND_RECT=true SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
  - result: passed.
  - report: `/tmp/magic-jewel-abi46-gradient-stroke-round-rect-smoke-2/report.md`.
  - counters: `jbr_command_frames=1224`, `jbr_command_fps=204.0`, `fallback_new_count=0`, `cmp_unsupported_reasons=none`, `screenshot_probeRightPurple=3148`, `screenshot_probeRightOrange=561`, `screenshot_probeRightCyan=546`.
- Focused Magic Jewel command-probe row:
  - command: `CASES=commands-gradient-surfaces OUT_ROOT=/tmp/magic-jewel-command-probe-abi46-gradient-surfaces DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
  - suite: `/tmp/magic-jewel-command-probe-abi46-gradient-surfaces/suite.tsv`.
  - row: `commands-gradient-surfaces`, `fallbacks=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=933`.

Roadmap update:
- Recorded ABI 46 command coverage.
- Recorded the focused smoke and focused command-probe row paths.

Next:
- Continue macOS MVP hardening with the next known-family rendering slice, or run a profiler-backed targeted benchmark row now that the machine is quieter and `CASES` is available.

## Checkpoint: ABI 47 Radial-Gradient Stroke Rect

Date: 2026-04-30

Status: completed for Compose radial-gradient stroked rectangle command recording and JBR/Skia replay.

Why:
- Linear-gradient stroke support covered rectangles and rounded rectangles, but radial-gradient strokes still fell outside the strict command surface.
- This keeps the known shader-family strategy moving without exposing raw `SkShader*` pointers across the Skiko/JBR boundary.

Changes:
- JBR command ABI bumped to `ABI_ID=47`; `BUILD_ID` now includes `abi=47`.
- Added 64-bit command capability `COMMAND_CAP64_STROKE_RECT_RADIAL_GRADIENT = 17592186044416L`.
- Added command opcode `COMMAND_STROKE_RECT_RADIAL_GRADIENT = 37`.
- The new payload carries rect bounds, stroke width/cap/join/miter metadata, radial-gradient center/radius/tile mode, and ARGB colors/stops.
- CMP records `drawRect(brush = Brush.radialGradient(...), style = Stroke(...))` when the paint is otherwise strict-command-compatible.
- JBR Java validation rejects malformed stroke metadata, radial geometry, tile mode, color count, and stops; Java2D fallback maps the record to `RadialGradientPaint` plus `BasicStroke`.
- JBR native replay reconstructs `SkShaders::RadialGradient`, applies stroke style metadata, and draws the stroked `SkRect` through the JBR-owned Skia runtime.
- Skiko compatibility now requires ABI 47 and the radial-gradient stroke capability bit before enabling command mode.
- Magic Jewel's `MAGIC_JEWEL_COMPOSE_RADIAL_GRADIENT=true` probe now includes a cyan/orange radial-gradient stroke over the existing radial-gradient rectangle fill.
- Screenshot assertions now distinguish radial rectangle and radial rounded-rectangle probes, requiring orange and cyan pixels for the rectangle stroke case.

Validation:
- CMP TDD/focused recorder tests:
  - first run failed before implementation with `expected:<47> but was:<46>` and only the command header emitted.
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRadialGradientStrokeRectRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRadialGradientRectRecord`
  - result: passed after the recorder implementation.
- Runtime API compile:
  - command: `javac -d /tmp/jbr-api-skia-abi47-compile src/com/jetbrains/Provided.java src/com/jetbrains/Service.java src/com/jetbrains/JBRSkia.java`
  - result: passed.
- JBR private API/service/test compile smoke:
  - result: passed for `JBRSkia.java`, `JBRSkiaService.java`, and the updated `JBRSkiaApiTest.java` compile.
- JBR native dylib compile smoke:
  - result: passed; `/tmp/jbr-skia-native/libjbrskiainterop.dylib` rebuilt with ABI 47.
- Default desktop patch refresh:
  - result: passed; `/tmp/jbr-skia-run/desktop` refreshed with ABI 47 service classes.
- Runtime API shim rebuild:
  - command: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-abi47-dev`
  - result: passed; copied to `/tmp/jbr-api-shim.jar`.
- Skiko compatibility tests:
  - command: `./gradlew --no-daemon :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - result: passed.
- Skiko local publish:
  - command: `./gradlew --no-daemon :skiko:publishToMavenLocal`
  - result: passed.
- CMP desktop jars refresh:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`
  - result: passed.
- Magic Jewel compile:
  - command: `./gradlew --no-daemon compileKotlin`
  - result: passed.
- Focused Magic Jewel radial-gradient stroke smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-abi47-radial-gradient-stroke-smoke-3 DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_RADIAL_GRADIENT=true SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
  - result: passed.
  - report: `/tmp/magic-jewel-abi47-radial-gradient-stroke-smoke-3/report.md`.
  - counters: `jbr_command_frames=917`, `jbr_command_fps=152.8`, `fallback_new_count=0`, `cmp_unsupported_reasons=none`, `screenshot_probeRightOrange=3663`, `screenshot_probeRightCyan=748`.
- Focused Magic Jewel command-probe row:
  - command: `CASES=commands-gradient-surfaces OUT_ROOT=/tmp/magic-jewel-command-probe-abi47-gradient-surfaces DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
  - suite: `/tmp/magic-jewel-command-probe-abi47-gradient-surfaces/suite.tsv`.
  - row: `commands-gradient-surfaces`, `fallbacks=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=440`.

Roadmap update:
- Recorded ABI 47 command coverage.
- Recorded the focused smoke and focused command-probe row paths.

Next:
- Continue the same known-family stroke expansion with radial-gradient stroked rounded rectangles, or move laterally into sweep-gradient stroke support.

## Checkpoint: ABI 48 Radial-Gradient Stroke Round Rect

Date: 2026-04-30

Status: completed for Compose radial-gradient stroked rounded-rectangle command recording and JBR/Skia replay.

Why:
- ABI 47 covered radial-gradient stroked rectangles; rounded rectangles needed the same radial stroke payload plus independent X/Y radii.
- This completes radial-gradient stroke support for the same rect/round-rect shape pair already covered by linear gradients.

Changes:
- JBR command ABI bumped to `ABI_ID=48`; `BUILD_ID` now includes `abi=48`.
- Added 64-bit command capability `COMMAND_CAP64_STROKE_ROUND_RECT_RADIAL_GRADIENT = 35184372088832L`.
- Added command opcode `COMMAND_STROKE_ROUND_RECT_RADIAL_GRADIENT = 38`.
- The new payload carries rect bounds, independent X/Y radii, stroke width/cap/join/miter metadata, radial-gradient center/radius/tile mode, and ARGB colors/stops.
- CMP records `drawRoundRect(brush = Brush.radialGradient(...), style = Stroke(...))` when the paint is otherwise strict-command-compatible.
- JBR Java validation/replay and native Skia replay mirror the ABI 47 radial stroke path with rounded-rectangle geometry.
- Skiko compatibility now requires ABI 48 and the radial-gradient stroked rounded-rectangle capability bit before enabling command mode.
- Magic Jewel's `MAGIC_JEWEL_COMPOSE_RADIAL_GRADIENT_ROUND_RECT=true` probe now includes a cyan/orange radial-gradient stroke over the existing radial-gradient rounded-rectangle fill.

Validation:
- CMP TDD/focused recorder tests:
  - first run failed before implementation with `expected:<48> but was:<47>` and only the command header emitted.
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRadialGradientStrokeRoundRectRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRadialGradientRoundRectRecord`
  - result: passed after the recorder implementation.
- Runtime API compile:
  - command: `javac -d /tmp/jbr-api-skia-abi48-compile src/com/jetbrains/Provided.java src/com/jetbrains/Service.java src/com/jetbrains/JBRSkia.java`
  - result: passed.
- JBR private API/service/test compile smoke:
  - result: passed for `JBRSkia.java`, `JBRSkiaService.java`, and the updated `JBRSkiaApiTest.java`; `/tmp/jbr-skia-run/desktop` refreshed with ABI 48 service classes.
- JBR native dylib compile smoke:
  - result: passed; `/tmp/jbr-skia-native/libjbrskiainterop.dylib` rebuilt with ABI 48.
- Runtime API shim rebuild:
  - command: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-abi48-dev`
  - result: passed; copied to `/tmp/jbr-api-shim.jar`.
- Skiko compatibility tests:
  - command: `./gradlew --no-daemon :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - result: passed.
- Skiko local publish:
  - command: `./gradlew --no-daemon :skiko:publishToMavenLocal`
  - result: passed.
- CMP desktop jars refresh:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`
  - result: passed.
- Magic Jewel compile:
  - command: `./gradlew --no-daemon compileKotlin`
  - result: passed.
- Focused Magic Jewel radial-gradient stroke rounded-rectangle smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-abi48-radial-gradient-stroke-round-rect-smoke-2 DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_RADIAL_GRADIENT_ROUND_RECT=true SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
  - result: passed.
  - report: `/tmp/magic-jewel-abi48-radial-gradient-stroke-round-rect-smoke-2/report.md`.
- Focused Magic Jewel command-probe row:
  - command: `CASES=commands-gradient-surfaces OUT_ROOT=/tmp/magic-jewel-command-probe-abi48-gradient-surfaces DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
  - suite: `/tmp/magic-jewel-command-probe-abi48-gradient-surfaces/suite.tsv`.
  - row: `commands-gradient-surfaces`, `fallbacks=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=559`.

Roadmap update:
- Recorded ABI 48 command coverage.
- Recorded the focused smoke and focused command-probe row paths.

Next:
- Continue the known-family stroke expansion with sweep-gradient stroked rectangles, then sweep-gradient stroked rounded rectangles.

## Checkpoint: ABI 49 Sweep-Gradient Stroke Rect

Date: 2026-04-30

Status: completed for Compose sweep-gradient stroked-rectangle command recording and JBR/Skia replay.

Why:
- ABI 37 covered sweep-gradient rectangle fills; ABI 49 adds the matching stroked-rectangle payload without sharing raw `SkShader*` pointers between Skiko and JBR.
- This keeps the strict command path moving through known shader families before the later generic JBR-owned shader-factory work.

Changes:
- JBR command ABI bumped to `ABI_ID=49`; `BUILD_ID` now includes `abi=49`.
- Added 64-bit command capability `COMMAND_CAP64_STROKE_RECT_SWEEP_GRADIENT = 70368744177664L`.
- Added command opcode `COMMAND_STROKE_RECT_SWEEP_GRADIENT = 39`.
- The new payload carries rect bounds, stroke width/cap/join/miter metadata, sweep-gradient center, and ARGB colors/stops.
- CMP records `drawRect(brush = Brush.sweepGradient(...), style = Stroke(...))` when the paint is otherwise strict-command-compatible.
- JBR Java validation/replay and native Skia replay reconstruct the sweep shader inside the JBR-owned runtime and draw a stroked rectangle.
- Skiko compatibility now requires ABI 49 and the sweep-gradient stroked-rectangle capability bit before enabling command mode.
- Magic Jewel's `MAGIC_JEWEL_COMPOSE_SWEEP_GRADIENT=true` probe now draws a visible sweep-gradient stroke over the existing sweep-gradient rectangle fill; the screenshot assertion checks the cyan/purple stroked marker.

Validation:
- CMP TDD/focused recorder tests:
  - first run failed before implementation with the stream still at ABI 48 and only the command header emitted.
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesSweepGradientStrokeRectRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesSweepGradientRectRecord`
  - result: passed after the recorder implementation.
- Runtime API compile:
  - command: `javac -d /tmp/jbr-api-skia-abi49-compile src/com/jetbrains/Provided.java src/com/jetbrains/Service.java src/com/jetbrains/JBRSkia.java`
  - result: passed.
- JBR private API/service/test compile and runtime smoke:
  - result: passed for `JBRSkia.java`, `JBRSkiaService.java`, and `JBRSkiaApiTest`; `/tmp/jbr-skia-run/desktop` refreshed with ABI 49 service classes.
- JBR native dylib compile smoke:
  - result: passed; `/tmp/jbr-skia-native/libjbrskiainterop.dylib` rebuilt with ABI 49.
- Runtime API shim rebuild:
  - command: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-abi49-dev`
  - result: passed; copied to `/tmp/jbr-api-shim.jar`.
- Skiko compatibility tests:
  - command: `./gradlew --no-daemon :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - result: passed.
- Skiko local publish:
  - command: `./gradlew --no-daemon :skiko:publishToMavenLocal`
  - result: passed.
- CMP desktop jars refresh:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`
  - result: passed.
- Magic Jewel compile:
  - command: `./gradlew --no-daemon compileKotlin`
  - result: passed.
- Focused Magic Jewel sweep-gradient stroke smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-abi49-sweep-gradient-stroke-smoke-2 DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_SWEEP_GRADIENT=true SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
  - result: passed.
  - report: `/tmp/magic-jewel-abi49-sweep-gradient-stroke-smoke-2/report.md`.
- Focused Magic Jewel command-probe row:
  - command: `CASES=commands-gradient-surfaces OUT_ROOT=/tmp/magic-jewel-command-probe-abi49-gradient-surfaces DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
  - suite: `/tmp/magic-jewel-command-probe-abi49-gradient-surfaces/suite.tsv`.
  - row: `commands-gradient-surfaces`, `fallbacks=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=1240`.

Roadmap update:
- Recorded ABI 49 command coverage.
- Recorded the focused smoke and focused command-probe row paths.

Next:
- Continue the known-family stroke expansion with sweep-gradient stroked rounded rectangles.

## Checkpoint: ABI 50 Sweep-Gradient Stroke Round Rect

Date: 2026-04-30

Status: completed for Compose sweep-gradient stroked rounded-rectangle command recording and JBR/Skia replay.

Why:
- ABI 49 covered sweep-gradient stroked rectangles; rounded rectangles needed the same sweep stroke payload plus independent X/Y radii.
- This completes serialized sweep-gradient stroke support for the same rect/round-rect shape pair already covered by linear and radial gradients.

Changes:
- JBR command ABI bumped to `ABI_ID=50`; `BUILD_ID` now includes `abi=50`.
- Added 64-bit command capability `COMMAND_CAP64_STROKE_ROUND_RECT_SWEEP_GRADIENT = 140737488355328L`.
- Added command opcode `COMMAND_STROKE_ROUND_RECT_SWEEP_GRADIENT = 40`.
- The new payload carries rect bounds, independent X/Y radii, stroke width/cap/join/miter metadata, sweep-gradient center, and ARGB colors/stops.
- CMP records `drawRoundRect(brush = Brush.sweepGradient(...), style = Stroke(...))` when the paint is otherwise strict-command-compatible.
- JBR Java validation/replay and native Skia replay mirror the ABI 49 sweep stroke path with rounded-rectangle geometry.
- Skiko compatibility now requires ABI 50 and the sweep-gradient stroked rounded-rectangle capability bit before enabling command mode.
- Magic Jewel's `MAGIC_JEWEL_COMPOSE_SWEEP_GRADIENT_ROUND_RECT=true` probe now includes a cyan/purple sweep-gradient stroke over the existing sweep-gradient rounded-rectangle fill.

Validation:
- CMP TDD/focused recorder tests:
  - first run failed before implementation with `expected:<50> but was:<49>` and only the command header emitted.
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesSweepGradientStrokeRoundRectRecord`
  - result: failed as expected before implementation.
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesSweepGradientStrokeRoundRectRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesSweepGradientRoundRectRecord`
  - result: passed after the recorder implementation.
- Runtime API compile:
  - command: `javac -d /tmp/jbr-api-skia-abi50-compile src/com/jetbrains/Provided.java src/com/jetbrains/Service.java src/com/jetbrains/JBRSkia.java`
  - result: passed.
- JBR private API/service/test compile and runtime smoke:
  - result: passed for `JBRSkia.java`, `JBRSkiaService.java`, and `JBRSkiaApiTest`; `/tmp/jbr-skia-run/desktop` refreshed with ABI 50 service classes.
- JBR native dylib compile smoke:
  - result: passed; `/tmp/jbr-skia-native/libjbrskiainterop.dylib` rebuilt with ABI 50.
- Runtime API shim rebuild:
  - command: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-abi50-dev`
  - result: passed; copied to `/tmp/jbr-api-shim.jar`.
- Skiko compatibility tests:
  - command: `./gradlew --no-daemon :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - result: passed.
- Skiko local publish:
  - command: `./gradlew --no-daemon :skiko:publishToMavenLocal`
  - result: passed.
- CMP desktop jars refresh:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`
  - result: passed.
- Magic Jewel compile:
  - command: `./gradlew --no-daemon compileKotlin`
  - result: passed.
- Focused Magic Jewel sweep-gradient stroke rounded-rectangle smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-abi50-sweep-gradient-stroke-round-rect-smoke-2 DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_SWEEP_GRADIENT_ROUND_RECT=true SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
  - result: passed.
  - report: `/tmp/magic-jewel-abi50-sweep-gradient-stroke-round-rect-smoke-2/report.md`.
- Focused Magic Jewel command-probe row:
  - command: `CASES=commands-gradient-surfaces OUT_ROOT=/tmp/magic-jewel-command-probe-abi50-gradient-surfaces DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
  - suite: `/tmp/magic-jewel-command-probe-abi50-gradient-surfaces/suite.tsv`.
  - row: `commands-gradient-surfaces`, `fallbacks=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=431`.

Roadmap update:
- Recorded ABI 50 command coverage.
- Recorded the focused smoke and focused command-probe row paths.

Next:
- Move into the next known shader/paint gap from the Magic Jewel fallback matrix, with generic JBR-owned shader handles still deferred to productionization.

## Checkpoint: ABI 51 Plus Blend-Mode Fill Rect

Date: 2026-04-30

Status: completed for the narrow `BlendMode.Plus` solid fill-rectangle command path.

Why:
- The previous blend-mode probe intentionally proved that unsupported blend modes fell back cleanly to picture replay.
- `BlendMode.Plus` on a solid fill rectangle is a small, deterministic paint-semantics slice that can move from fallback to JBR-owned Skia replay without solving every blend mode or every primitive at once.
- This is not generic blend-mode support yet; it is a tightly versioned command for one common Compose paint case.

Changes:
- JBR command ABI bumped to `ABI_ID=51`; `BUILD_ID` now includes `abi=51`.
- Added 64-bit command capability `COMMAND_CAP64_FILL_RECT_BLEND_MODE = 281474976710656L`.
- Added command opcode `COMMAND_FILL_RECT_BLEND_MODE = 41`.
- Added blend payload value `COMMAND_BLEND_MODE_PLUS = 1`.
- CMP records `drawRect` with `BlendMode.Plus`, solid color, fill style, and no shader/color-filter/path-effect as a command payload instead of marking the frame unsupported.
- Skiko compatibility now requires ABI 51 and the Plus blend fill-rect capability bit before enabling command mode.
- JBR validates the command payload and native replay maps it to `SkBlendMode::kPlus` before drawing the rect.
- Magic Jewel's blend-mode probe now expects command replay instead of picture fallback; the historical case name `commands-blend-mode-fallback` currently remains as the row identifier while its expected result has changed.

Validation:
- CMP TDD/focused recorder tests:
  - first run failed before implementation with ABI 50/header-only output for `writesFillRectPlusBlendModeRecord`.
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesFillRectPlusBlendModeRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesAntialiasRecordFlag`
  - result: passed after the recorder implementation.
- Runtime API compile:
  - command: `javac -d /tmp/jbr-api-skia-abi51-compile src/com/jetbrains/Provided.java src/com/jetbrains/Service.java src/com/jetbrains/JBRSkia.java`
  - result: passed.
- JBR private API/service/test compile and runtime smoke:
  - result: passed for `JBRSkia.java`, `JBRSkiaService.java`, and `JBRSkiaApiTest`; `/tmp/jbr-skia-run/desktop` refreshed with ABI 51 service classes.
- JBR native dylib compile smoke:
  - result: passed; `/tmp/jbr-skia-native/libjbrskiainterop.dylib` rebuilt with ABI 51.
- Runtime API shim rebuild:
  - command: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-abi51-dev`
  - result: passed; copied to `/tmp/jbr-api-shim.jar`.
- Skiko compatibility tests:
  - command: `./gradlew --no-daemon :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - result: passed.
- Skiko local publish:
  - command: `./gradlew --no-daemon :skiko:publishToMavenLocal`
  - result: passed.
- CMP desktop jars refresh:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`
  - result: passed.
- Magic Jewel compile:
  - command: `./gradlew --no-daemon compileKotlin`
  - result: passed.
- Focused Magic Jewel Plus blend-mode smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-abi51-fill-rect-plus-blend-smoke DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_BLEND_MODE=true SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
  - result: passed.
  - report: `/tmp/magic-jewel-abi51-fill-rect-plus-blend-smoke/report.md`.
  - command replay: `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=315` in the report summary.
- Focused Magic Jewel command-probe row:
  - command: `CASES=commands-blend-mode-fallback OUT_ROOT=/tmp/magic-jewel-command-probe-abi51-blend-mode DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
  - suite: `/tmp/magic-jewel-command-probe-abi51-blend-mode/suite.tsv`.
  - row: `commands-blend-mode-fallback`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=882`.

Roadmap update:
- Recorded ABI 51 command coverage.
- Recorded the focused Plus blend-mode smoke and command-probe row paths.
- Added concrete roadmap action items for old/new golden screenshot diffing and the eventual JBR-owned shader/effect handle ABI.

Next:
- Continue filling the paint-semantics gaps with small command slices while keeping broader generic shader/effect work behind a structured JBR-owned handle ABI.

## Checkpoint: ABI 52 Tint Color-Filter Fill Rect

Date: 2026-04-30

Status: completed for the narrow `ColorFilter.tint(..., BlendMode.SrcIn)` solid fill-rectangle command path.

Why:
- The previous color-filter probe intentionally proved that unsupported color filters fell back cleanly to picture replay.
- A tint color filter with `BlendMode.SrcIn` over a solid fill rectangle is serializable as data: source ARGB, tint ARGB, tint blend mode, and rectangle geometry.
- This is not generic color-filter support yet; arbitrary color matrices, lighting filters, image filters, path effects, and effect graphs still need descriptor/handle work owned by JBR's Skia runtime.

Changes:
- JBR command ABI bumped to `ABI_ID=52`; `BUILD_ID` now includes `abi=52`.
- Added 64-bit command capability `COMMAND_CAP64_FILL_RECT_COLOR_FILTER = 562949953421312L`.
- Added command opcode `COMMAND_FILL_RECT_COLOR_FILTER = 42`.
- Added blend payload value `COMMAND_BLEND_MODE_SRC_IN = 2`.
- CMP records `drawRect` with a `BlendModeColorFilter` whose blend mode is `BlendMode.SrcIn`, solid color, fill style, and otherwise supported paint as a command payload instead of marking the frame unsupported.
- Skiko compatibility now requires ABI 52 and the tint color-filter fill-rect capability bit before enabling command mode.
- JBR validates the command payload; native replay reconstructs the color filter inside JBR's Skia runtime through `SkColorFilters::Blend(filterColor, SkBlendMode::kSrcIn)`.
- The Java2D fallback replay approximates the same solid-fill case by applying the source/tint alpha and filling with the tint RGB.
- Magic Jewel's color-filter probe now expects command replay instead of picture fallback; the historical case name `commands-color-filter-fallback` currently remains as the row identifier while its expected result has changed.

Validation:
- CMP TDD/focused recorder tests:
  - first run failed before implementation with ABI 51/header-only output for `writesFillRectTintColorFilterRecord`.
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesFillRectTintColorFilterRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesFillRectPlusBlendModeRecord`
  - result: passed after the recorder implementation.
- Runtime API compile:
  - command: `javac -d /tmp/jbr-api-skia-abi52-compile src/com/jetbrains/Provided.java src/com/jetbrains/Service.java src/com/jetbrains/JBRSkia.java`
  - result: passed.
- JBR private API/service/test compile and runtime smoke:
  - result: passed for `JBRSkia.java`, `JBRSkiaService.java`, and `JBRSkiaApiTest`; `/tmp/jbr-skia-run/desktop` refreshed with ABI 52 service classes.
- JBR native dylib compile smoke:
  - result: passed; `/tmp/jbr-skia-native/libjbrskiainterop.dylib` rebuilt with ABI 52.
- Runtime API shim rebuild:
  - command: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-abi52-dev`
  - result: passed; copied to `/tmp/jbr-api-shim.jar`.
- Skiko compatibility tests:
  - first ABI52 run failed because the fake native metadata fixture still advertised command-stream ABI 51, causing `native-abi-mismatch` before capability validation.
  - command: `./gradlew --no-daemon :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - result: passed after updating the fixture to ABI 52.
- Skiko local publish:
  - command: `./gradlew --no-daemon :skiko:publishToMavenLocal`
  - result: passed.
- CMP desktop jars refresh:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`
  - result: passed.
- Magic Jewel compile:
  - command: `./gradlew --no-daemon compileKotlin && bash -n scripts/assert-jbr-skia-command-window-screenshot.sh && bash -n scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
- Focused Magic Jewel tint color-filter smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-abi52-tint-color-filter-smoke DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_COLOR_FILTER=true SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
  - result: passed.
  - report: `/tmp/magic-jewel-abi52-tint-color-filter-smoke/report.md`.
  - command replay: `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=1398` in the report summary.
- Focused Magic Jewel command-probe row:
  - command: `CASES=commands-color-filter-fallback OUT_ROOT=/tmp/magic-jewel-command-probe-abi52-color-filter DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
  - suite: `/tmp/magic-jewel-command-probe-abi52-color-filter/suite.tsv`.
  - row: `commands-color-filter-fallback`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=641`.

Roadmap update:
- Recorded ABI 52 command coverage.
- Recorded the focused tint color-filter smoke and command-probe row paths.
- Kept broader color-filter/effect support tied to the JBR-owned descriptor/handle ABI rather than raw Skiko runtime pointers.

Next:
- Continue shrinking the fallback matrix; path effects and image filters remain intentionally harder because they require effect descriptors or handles, not just paint scalar metadata.

## Checkpoint: ABI 53 Dashed Stroke Line

Date: 2026-04-30

Status: completed for the narrow dash path-effect stroked-line command path.

Why:
- The previous path-effect probe intentionally proved that unsupported path effects fell back cleanly to picture replay.
- The Magic Jewel path-effect probe uses `PathEffect.dashPathEffect(floatArrayOf(16f, 10f), 0f)` on a straight stroked line. That can be serialized as dash intervals plus phase without sharing a Skiko `SkPathEffect*`.
- This is not generic path-effect support yet; corner, chained, stamped, and arbitrary effect graphs still need serialized descriptors or JBR-owned effect handles.

Changes:
- JBR command ABI bumped to `ABI_ID=53`; `BUILD_ID` now includes `abi=53`.
- Added 64-bit command capability `COMMAND_CAP64_STROKE_LINE_DASH_PATH_EFFECT = 1125899906842624L`.
- Added command opcode `COMMAND_STROKE_LINE_DASH_PATH_EFFECT = 43`.
- CMP now preserves dash metadata on Skiko-backed dash path effects and records dashed `drawLine` calls when the paint is otherwise a supported solid-color stroke.
- Skiko compatibility now requires ABI 53 and the dashed-line path-effect capability bit before enabling command mode.
- JBR validates dash interval count/ranges and reconstructs `SkDashPathEffect` inside JBR's Skia runtime for native replay.
- The Java2D fallback replay maps the same command to `BasicStroke(..., dash, dashPhase)`.
- Magic Jewel's path-effect probe now expects command replay instead of picture fallback; the historical case name `commands-path-effect-fallback` currently remains as the row identifier while its expected result has changed.

Validation:
- CMP TDD/focused recorder tests:
  - first run failed before implementation with ABI 52/header-only output for `writesDashedStrokeLineRecord`.
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesDashedStrokeLineRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesFillRectTintColorFilterRecord`
  - result: passed after metadata retention and command recording implementation.
- Runtime API compile:
  - command: `javac -d /tmp/jbr-api-skia-abi53-compile src/com/jetbrains/Provided.java src/com/jetbrains/Service.java src/com/jetbrains/JBRSkia.java`
  - result: passed.
- JBR private API/service/test compile and runtime smoke:
  - result: passed for `JBRSkia.java`, `JBRSkiaService.java`, and `JBRSkiaApiTest`; `/tmp/jbr-skia-run/desktop` refreshed with ABI 53 service classes.
- JBR native dylib compile smoke:
  - first run failed because Skia m147's `SkDashPathEffect::Make` takes `SkSpan<const SkScalar>` plus phase, not pointer/count/phase.
  - result: passed after switching the call to `SkDashPathEffect::Make(SkSpan<const SkScalar>(...), phase)`.
- Runtime API shim rebuild:
  - command: `bash tools/build.sh process && bash tools/build.sh dev $(/usr/libexec/java_home -v 21) /tmp/jbr-api-skia-abi53-dev`
  - result: passed; copied to `/tmp/jbr-api-shim.jar`.
- Skiko compatibility tests:
  - command: `./gradlew --no-daemon :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - result: passed.
- Skiko local publish:
  - command: `./gradlew --no-daemon :skiko:publishToMavenLocal`
  - result: passed.
- CMP desktop jars refresh:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopJar :compose:ui:ui-text:desktopJar :compose:ui:ui:desktopJar`
  - result: passed.
- Magic Jewel compile:
  - command: `./gradlew --no-daemon compileKotlin && bash -n scripts/assert-jbr-skia-command-window-screenshot.sh && bash -n scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
- Focused Magic Jewel dashed path-effect smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-abi53-dashed-path-effect-smoke DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_PATH_EFFECT=true SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
  - result: passed.
  - report: `/tmp/magic-jewel-abi53-dashed-path-effect-smoke/report.md`.
  - command replay: `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=522` in the report summary.
- Focused Magic Jewel command-probe row:
  - command: `CASES=commands-path-effect-fallback OUT_ROOT=/tmp/magic-jewel-command-probe-abi53-path-effect DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: passed.
  - suite: `/tmp/magic-jewel-command-probe-abi53-path-effect/suite.tsv`.
  - row: `commands-path-effect-fallback`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=893`.

Roadmap update:
- Recorded ABI 53 command coverage.
- Recorded the focused dashed path-effect smoke and command-probe row paths.
- Kept broader path-effect/effect support tied to serialized descriptors or JBR-owned effect handles.

Next:
- Continue shrinking the fallback matrix. Image filters and saveLayer filters are the remaining paint/effect rows that likely need descriptor or handle work rather than scalar command payloads.

## Checkpoint: ABI 54 saveLayer Tint Color-Filter

Status: completed for the narrow `Canvas.saveLayer` layer-paint path using `ColorFilter.tint(..., BlendMode.SrcIn)`.

What changed:
- CMP records a new `COMMAND_SAVE_LAYER_COLOR_FILTER` command instead of marking the saveLayer scope unsupported when the layer paint is otherwise simple and has a SrcIn tint color filter.
- Runtime API and JBR private API now expose ABI 54, capability `COMMAND_CAP64_SAVE_LAYER_COLOR_FILTER = 2251799813685248L`, and opcode `COMMAND_SAVE_LAYER_COLOR_FILTER = 44`.
- Skiko compatibility now requires ABI 54 and the saveLayer color-filter capability before enabling command mode.
- JBR validates the new payload and native replay reconstructs the layer paint inside JBR's Skia runtime with `SkColorFilters::Blend(filterColor, SkBlendMode::kSrcIn)`.
- JBR Java2D command fallback now restores a missing image interpolation hint by removing the hint instead of setting it to `null`; this avoided masking native replay diagnostics when native was intentionally disabled.
- Skiko now unwraps `InvocationTargetException` from reflective scope calls so command-frame failures log the real underlying exception.
- Magic Jewel renamed the probe row from `commands-save-layer-filter-fallback` to `commands-save-layer-filter` and no longer expects a recorder fallback for this case.
- `ROADMAP.md` now marks ABI 54 complete and records concrete old/new screenshot parity and generic shader/effect implementation action points.

Validation:
- CMP focused recorder test class:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
  - result: passed after updating all expected command-stream headers to ABI 54.
- Runtime API compile:
  - command: `javac -d /tmp/jbr-api-skia-abi54-compile src/com/jetbrains/Provided.java src/com/jetbrains/Service.java src/com/jetbrains/JBRSkia.java`
  - result: passed.
- Skiko compatibility tests:
  - command: `./gradlew --no-daemon :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
  - result: passed with ABI 54 discovery.
- JBR Java validator/API smoke:
  - command: patched `JBRSkia.java` + `JBRSkiaService.java` compile into `/tmp/jbr-skia-run/desktop`, then `JBRSkiaApiTest`.
  - result: passed for ABI 54, capability mask, valid saveLayer color-filter stream, and invalid blend-mode rejection.
- JBR native standalone build:
  - command: standalone `clang++` build of `JBRSkiaInterop.mm` against Skia m147 arm64 into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
  - result: passed; first native smoke failed until the native ABI constant was bumped from 53 to 54, proving the ABI gate caught the mismatch.
- Magic Jewel ABI54 smoke:
  - command: `OUT_DIR=/tmp/magic-jewel-abi54-save-layer-color-filter-smoke-3 DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 JBR_SKIA_RENDER_MODE=commands MAGIC_JEWEL_COMPOSE_SAVELAYER_FILTER=true SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-interop-report.sh`
  - result: passed.
  - report: `/tmp/magic-jewel-abi54-save-layer-color-filter-smoke-3/report.md`.
- Magic Jewel command-probe row:
  - command: `CASES=commands-save-layer-filter OUT_ROOT=/tmp/magic-jewel-command-probe-abi54-save-layer-filter DURATION_SECONDS=6 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT bash scripts/jbr-skia-command-probe-suite.sh`
  - result: passed with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=409`.
  - suite: `/tmp/magic-jewel-command-probe-abi54-save-layer-filter/suite.tsv`.

Next:
- Continue shrinking effect fallbacks with image-paint color filters or move into the first descriptor/handle ABI for generic image filters/runtime effects, while keeping the screenshot parity harness as the validation track for visual drift.

## Checkpoint: ABI 55 Image Tint Color-Filter

Status: completed for cached image refs drawn with `ColorFilter.tint(..., BlendMode.SrcIn)`.

What changed:
- CMP records `drawImageRect` with a SrcIn tint color filter as `COMMAND_DRAW_IMAGE_REF_COLOR_FILTER` instead of marking the image draw unsupported.
- Runtime API and JBR private API now expose ABI 55, capability `COMMAND_CAP64_DRAW_IMAGE_REF_COLOR_FILTER = 4503599627370496L`, and opcode `COMMAND_DRAW_IMAGE_REF_COLOR_FILTER = 45`.
- Skiko compatibility now requires ABI 55 and the image-ref color-filter capability before enabling command mode.
- JBR validates the new image-ref payload; native replay applies `SkColorFilters::Blend(filterColor, SkBlendMode::kSrcIn)` to the image paint inside JBR's Skia runtime.
- JBR Java2D command fallback approximates SrcIn image tinting by creating a tinted ARGB image before drawing.
- Magic Jewel renamed the probe row from `commands-image-filter-fallback` to `commands-image-filter` and no longer expects a recorder fallback for this case.

Validation:
- CMP focused test: `writesImageTintColorFilterRecord` passed.
- Runtime API compile passed for ABI 55.
- Skiko `JbrSkiaInteropTest` passed with ABI 55 discovery.
- JBR API/validator smoke passed with a valid image-ref tint color-filter stream.
- Native `JBRSkiaInterop.mm` standalone build passed with command-stream ABI 55.
- Magic Jewel smoke passed: `/tmp/magic-jewel-abi55-image-color-filter-smoke/report.md`.
- Magic Jewel command-probe row passed: `/tmp/magic-jewel-command-probe-abi55-image-color-filter/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=1707`.

Next:
- The remaining obvious effect family is no longer these narrow SrcIn tint cases; move toward the descriptor/handle ABI for real image filters/runtime effects or pick the next small explicit command shape only if it corresponds to a common Jewel/CMP paint pattern.

## Checkpoint: ABI 56 Tint Color-Filter Handles

Status: completed for the first descriptor-shaped effect handle slice.

What changed:
- CMP can opt into `ColorFilter.tint(..., BlendMode.SrcIn)` handle emission with `-Dcompose.jbr.skia.command.colorFilterHandles=true`.
- The recorder emits an in-frame `COMMAND_DEFINE_COLOR_FILTER_TINT` descriptor followed by `COMMAND_FILL_RECT_COLOR_FILTER_REF`, instead of carrying the tint inline on the draw command.
- Runtime API and JBR private API now expose ABI 56, capabilities `COMMAND_CAP64_DEFINE_COLOR_FILTER_TINT = 9007199254740992L` and `COMMAND_CAP64_FILL_RECT_COLOR_FILTER_REF = 18014398509481984L`, and opcodes 46/47.
- Skiko compatibility now requires ABI 56 and both color-filter handle capabilities before enabling command mode.
- JBR validates that color-filter handles are defined before use, rejects undefined handles, and reconstructs the tint filter inside JBR's Skia runtime for native replay.
- JBR Java2D command fallback keeps a per-frame descriptor map and approximates the same SrcIn tint behavior.
- Magic Jewel has a `MAGIC_JEWEL_COMPOSE_COLOR_FILTER_HANDLE=true` probe switch and a `commands-color-filter-handle` command-probe row.

Validation:
- CMP focused test `writesFillRectTintColorFilterHandleRecord` passed with configuration cache disabled after Gradle hit an unrelated cache serialization `ConcurrentModificationException`.
- Runtime API compile passed for ABI 56.
- Skiko `JbrSkiaInteropTest` passed with ABI 56 discovery and capability mask.
- JBR API/validator smoke passed with a valid color-filter handle stream and invalid undefined-handle rejection.
- Native `JBRSkiaInterop.mm` standalone build passed with command-stream ABI 56.
- Magic Jewel command-probe row passed: `/tmp/magic-jewel-command-probe-abi56-color-filter-handle/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=618`.

Next:
- Generalize this descriptor pattern beyond tint handles: add stable descriptor type ids, explicit create/reuse/evict semantics, and then use the same mechanism for image filters/runtime effects and eventually SKSL runtime shaders.
- Build the old/new rich-content screenshot parity harness so descriptor/effect work is judged by visual equivalence for Compose/Jewel content, with separate tolerances for Swing text that may intentionally change once Swing itself is Skia-backed.

## Checkpoint: ABI 57 Persistent Color-Filter Handles

Status: completed for destination-context-scoped descriptor reuse and explicit eviction.

What changed:
- CMP now caches tint color-filter handles when `compose.jbr.skia.command.colorFilterHandles=true`, emitting `COMMAND_DEFINE_COLOR_FILTER_TINT` only once for stable descriptors and reusing `COMMAND_FILL_RECT_COLOR_FILTER_REF` across later frames.
- Runtime API and JBR private API now expose ABI 57, capability `COMMAND_CAP64_EVICT_COLOR_FILTER_HANDLE = 36028797018963968L`, and opcode `COMMAND_EVICT_COLOR_FILTER_HANDLE = 48`.
- Skiko compatibility now requires ABI 57 and the eviction capability before enabling command mode.
- JBR Java and native replay keep color-filter descriptor caches scoped by destination context id, so handles never cross Java2D destination contexts.
- JBR validates in-frame define/use/evict order for static API tests and rejects undefined handles; runtime replay can also resolve handles cached by previous frames in the same destination context.

Validation:
- CMP focused tests `writesFillRectTintColorFilterHandleRecord` and `reusesTintColorFilterHandleAcrossFrames` passed.
- Runtime API compile passed for ABI 57.
- Skiko `JbrSkiaInteropTest` passed with ABI 57 discovery and capability mask.
- JBR API/validator smoke passed with valid color-filter define/use/evict streams and undefined-handle rejection.
- Native `JBRSkiaInterop.mm` standalone build passed with command-stream ABI 57.
- Magic Jewel command-probe row passed: `/tmp/magic-jewel-command-probe-abi57-color-filter-handle/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=366`.

Next:
- Replace the tint-specific handle payload with a generalized descriptor envelope: descriptor kind, version, payload length, stable hash, create/use/evict commands, and parseable fallback reasons.
- Use that envelope for the first non-tint effect descriptor, likely blur/image-filter or a small runtime-effect/SKSL probe depending on what Compose exposes cleanly without raw Skia pointer transfer.

## Checkpoint: ABI 58 Generic Effect Descriptor Envelope

Status: completed for a generic typed/versioned descriptor envelope, with tint/SrcIn color filters as descriptor type 1 version 1.

What changed:
- CMP now emits `COMMAND_DEFINE_EFFECT_DESCRIPTOR` for reusable tint color-filter handles instead of the tint-specific `COMMAND_DEFINE_COLOR_FILTER_TINT` record.
- Runtime API and JBR private API expose ABI 58, capability `COMMAND_CAP64_DEFINE_EFFECT_DESCRIPTOR = 72057594037927936L`, opcode `COMMAND_DEFINE_EFFECT_DESCRIPTOR = 49`, descriptor type `COMMAND_EFFECT_DESCRIPTOR_TINT_COLOR_FILTER = 1`, and descriptor version `COMMAND_EFFECT_DESCRIPTOR_VERSION_1 = 1`.
- Skiko compatibility now requires ABI 58 and the generic descriptor capability before enabling command mode.
- JBR Java validation and native replay parse the descriptor envelope as `[handleHigh, handleLow, descriptorType, descriptorVersion, payloadIntCount, payload...]`, then construct/cache the tint filter inside the JBR-owned destination context.
- The old tint-specific define command remains accepted for compatibility, but the forward path is now the generic effect descriptor envelope. This is the concrete first step toward runtime effects/SKSL and other JBR-owned effect handles without raw Skia pointer sharing.
- `ROADMAP.md` now marks ABI 58 complete and keeps screenshot parity plus generic shader/effect support as concrete action tracks, not just documentation.

Validation:
- CMP focused tests `writesFillRectTintColorFilterHandleRecord` and `reusesTintColorFilterHandleAcrossFrames` passed with ABI 58 expectations.
- Runtime API compile passed for ABI 58.
- Skiko `JbrSkiaInteropTest` passed with ABI 58 discovery and capability mask.
- JBR API/validator smoke passed with valid generic effect descriptor define/use/evict streams.
- Native `JBRSkiaInterop.mm` standalone build passed with command-stream ABI 58.
- Magic Jewel command-probe row passed: `/tmp/magic-jewel-command-probe-abi58-effect-descriptor/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=735`.

Next:
- Add negative validation tests for malformed effect descriptors: unknown descriptor type/version, mismatched payload length, unsupported blend mode, undefined handle, stale/evicted handle, and context migration.
- Start the first non-tint descriptor: either image-filter/blur or runtime-effect/SKSL, depending on which Compose paint path can be serialized cleanly without transferring Skiko-owned Skia C++ objects.
- Build the old/new rich-content screenshot parity harness as the visual regression gate for descriptor/effect work, with tight Compose/Jewel tolerances and separate Swing-text tolerance notes.

## Checkpoint: Effect Descriptor Negative Validation

Status: completed for JBR static command-stream validation.

What changed:
- `JBRSkiaApiTest` now rejects malformed `COMMAND_DEFINE_EFFECT_DESCRIPTOR` records for unknown descriptor type, unsupported descriptor version, mismatched payload count, mismatched record length, unsupported tint blend mode, and use after explicit handle eviction.
- No ABI or runtime implementation changes were needed; this slice pins the stricter compatibility contract around ABI 58 so future descriptor kinds do not loosen validation accidentally.
- `ROADMAP.md` records the strict descriptor validation coverage as complete while keeping runtime-effect/SKSL descriptor support open.

Validation:
- JBR API/validator smoke passed after adding the malformed descriptor cases.

Next:
- Add live/report-level fallback markers for descriptor parse failures once Skiko/CMP can intentionally emit an incompatible descriptor under a test switch.
- Start the first non-tint descriptor implementation, with blur/image-filter and runtime-effect/SKSL still the two candidate paths.

## Checkpoint: ABI 59 Multiply Blend-Mode Fill Rectangles

Status: completed for `BlendMode.Multiply` on solid fill rectangles.

What changed:
- CMP now records solid fill rectangles using `BlendMode.Multiply` as `COMMAND_FILL_RECT_BLEND_MODE` with payload value `COMMAND_BLEND_MODE_MULTIPLY = 3`.
- Runtime API and JBR private API expose ABI 59 and the new multiply blend-mode payload constant.
- Skiko compatibility now requires ABI 59 before enabling command mode.
- JBR validation accepts multiply for blend-mode fill rectangles; native replay maps it to `SkBlendMode::kMultiply` inside the JBR-owned Skia runtime.
- Magic Jewel now draws an overlapping Plus/Multiply blend-mode probe and the command-probe suite exposes the clearer `commands-blend-mode` case name while keeping `commands-blend-mode-fallback` as an alias.
- `ROADMAP.md` records ABI 59 and keeps future blend-mode expansion as a mode-by-mode compatibility track.

Validation:
- CMP focused tests `writesFillRectPlusBlendModeRecord` and `writesFillRectMultiplyBlendModeRecord` passed.
- Runtime API compile passed for ABI 59.
- Skiko `JbrSkiaInteropTest` passed with ABI 59 discovery and capability mask.
- JBR API/validator smoke passed with valid Plus and Multiply blend-mode streams.
- Native `JBRSkiaInterop.mm` standalone build passed with command-stream ABI 59.
- Magic Jewel command-probe row passed: `/tmp/magic-jewel-command-probe-abi59-multiply-blend-renamed/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=894`.

Next:
- Add screenshot-region assertions for the Plus/Multiply blend probe so the live harness proves color semantics, not only no-fallback replay.
- Continue blend-mode expansion one value at a time, likely `Screen` or `Overlay`, only after pinning exact Skia mapping and fallback behavior.

## Checkpoint: Blend-Mode Screenshot Assertions

Status: completed for Magic Jewel window-only screenshot validation of the Plus/Multiply blend-mode probe.

What changed:
- Magic Jewel screenshot assertions now count `blendModeYellow` pixels for the base rectangle and `blendModeMultiply` pixels for the multiplied overlap when `MAGIC_JEWEL_COMPOSE_BLEND_MODE=true`.
- The assertion remains window-only and records the new counters in the existing `JBR_SKIA_COMMAND_SCREENSHOT_COUNTS` marker for report parsing.
- `ROADMAP.md` records screenshot-region assertions for the Plus/Multiply blend-mode probe as complete.

Validation:
- Magic Jewel command-probe row passed: `/tmp/magic-jewel-command-probe-abi59-blend-screenshot/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=941`.

Next:
- Feed the blend counters into `summary.properties` if we want CI dashboards to compare them without opening the report log.
- Continue mode-by-mode blend expansion or start the first non-tint descriptor implementation.

## Checkpoint: ABI 60 Screen Blend-Mode Fill Rectangles

Status: completed for `BlendMode.Screen` on solid fill rectangles.

What changed:
- CMP now records solid fill rectangles using `BlendMode.Screen` as `COMMAND_FILL_RECT_BLEND_MODE` with payload value `COMMAND_BLEND_MODE_SCREEN = 4`.
- Runtime API and JBR private API expose ABI 60 and the new screen blend-mode payload constant.
- Skiko compatibility now requires ABI 60 before enabling command mode.
- JBR validation accepts screen for blend-mode fill rectangles; native replay maps it to `SkBlendMode::kScreen` inside the JBR-owned Skia runtime.
- Magic Jewel now draws Plus, Multiply, and Screen blend-mode probes, and screenshot assertions count the Screen region through `blendModeScreen`.
- `ROADMAP.md` records ABI 60 and keeps remaining blend modes as a mode-by-mode compatibility track.

Validation:
- CMP focused tests `writesFillRectPlusBlendModeRecord`, `writesFillRectMultiplyBlendModeRecord`, and `writesFillRectScreenBlendModeRecord` passed.
- Runtime API compile passed for ABI 60.
- Skiko `JbrSkiaInteropTest` passed with ABI 60 discovery and capability mask.
- JBR API/validator smoke passed with valid Plus, Multiply, and Screen blend-mode streams.
- Native `JBRSkiaInterop.mm` standalone build passed with command-stream ABI 60.

## Checkpoint: ABI 61 Overlay Blend-Mode Fill Rectangles

Status: completed for `BlendMode.Overlay` on solid fill rectangles.

Changes:
- CMP records solid fill rectangles using `BlendMode.Overlay` as `COMMAND_FILL_RECT_BLEND_MODE` with payload value `COMMAND_BLEND_MODE_OVERLAY = 5`.
- Runtime API and JBR private API expose ABI 61 and the overlay blend-mode payload constant.
- JBR Java validation accepts Overlay wherever the fill-rect blend-mode command is valid.
- Native `JBRSkiaInterop.mm` maps the payload to `SkBlendMode::kOverlay`, keeping the replay implementation inside JBR's Skia runtime.
- Native `JBRSkiaInterop.mm` gates command streams on ABI 61. The first live run caught a stale native ABI 60 gate because Skiko command frames succeeded while JBR native replay markers stayed at zero and Java fallback drew the frame instead.
- Skiko compatibility requires ABI 61 before enabling command mode.
- Magic Jewel extends the blend-mode visual probe and screenshot assertion with an Overlay region.
- CMP's graphics-layer command fallback was narrowed so ordinary benign layers created by z-ordering/internal composition no longer force picture fallback; only unsupported layer semantics such as alpha, transform, clipping, shadow, or explicit layer creation mark the command stream unsupported.

Validation:
- CMP focused recorder tests passed for Screen and Overlay blend-mode records.
- Runtime API compile passed for ABI 61.
- Magic Jewel Kotlin compile passed after adding the Overlay probe.
- Native `JBRSkiaInterop.mm` standalone build passed with the Overlay mapping.
- JBR API validator passed with ABI 61 and the Overlay payload.
- Skiko interop tests passed with ABI 61 fixture expectations.
- Skiko `publishToMavenLocal` and CMP desktop jar rebuild passed for the local Magic Jewel run.
- Magic Jewel ABI 61 Overlay live command probe passed: `/tmp/magic-jewel-command-probe-abi61-overlay-blend-4/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=908`.
- Magic Jewel explicit graphics-layer fallback probe passed after narrowing benign layer fallback: `/tmp/magic-jewel-command-probe-graphics-layer-fallback-2/suite.tsv`, with `unsupported=graphicsLayer:780`, `jbr_picture_frames=781`, and `jbr_command_frames=0`.

Next:
- Continue blend-mode expansion one value at a time or pivot to the first non-tint effect descriptor; render-effect/blur support needs graphics-layer plumbing rather than the simple paint recorder.
- Keep the roadmap's screenshot-parity and generic-shader implementation tracks active while doing the next rendering slice:
  - screenshot parity must compare rich old/new Swing/CMP/Jewel content with tight Compose/Jewel thresholds and Swing-text-aware regions.
  - generic shaders must become a real JBR-owned shader/effect handle implementation, not just documentation.

## Checkpoint: ABI 62 Darken Blend-Mode Fill Rectangles

Status: completed for `BlendMode.Darken` on solid fill rectangles.

Changes:
- CMP records solid fill rectangles using `BlendMode.Darken` as `COMMAND_FILL_RECT_BLEND_MODE` with payload value `COMMAND_BLEND_MODE_DARKEN = 6`.
- Runtime API and JBR private API expose ABI 62 and the Darken blend-mode payload constant.
- JBR Java validation accepts Darken wherever the fill-rect blend-mode command is valid.
- Native `JBRSkiaInterop.mm` gates command streams on ABI 62 and maps the payload to `SkBlendMode::kDarken` inside JBR's Skia runtime.
- Skiko compatibility requires ABI 62 before enabling command mode.
- Magic Jewel extends the blend-mode visual probe and screenshot assertion with a Darken region.

Validation:
- Runtime API compile passed for ABI 62.
- JBR API validator passed with ABI 62 and the Darken payload.
- Native `JBRSkiaInterop.mm` standalone build passed with the Darken mapping.
- CMP focused recorder tests passed for Darken and Overlay blend-mode records.
- Skiko interop tests passed with ABI 62 fixture expectations.
- Magic Jewel Kotlin compile passed after adding the Darken probe.
- Runtime API shim, Skiko Maven-local artifact, and CMP desktop jars were rebuilt for the live run.
- Magic Jewel ABI 62 Darken live command probe passed: `/tmp/magic-jewel-command-probe-abi62-darken-blend/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=583`.
- Screenshot assertion passed with `blendModeDarken=884`.

Next:
- Continue blend-mode expansion one value at a time only for modes with direct Skia mappings, or pivot to screenshot parity/generic shader handles if rich-content drift becomes the larger risk.

## Checkpoint: ABI 63 Lighten Blend-Mode Fill Rectangles

Status: completed for `BlendMode.Lighten` on solid fill rectangles.

Changes:
- CMP records solid fill rectangles using `BlendMode.Lighten` as `COMMAND_FILL_RECT_BLEND_MODE` with payload value `COMMAND_BLEND_MODE_LIGHTEN = 7`.
- Runtime API and JBR private API expose ABI 63 and the Lighten blend-mode payload constant.
- JBR Java validation accepts Lighten wherever the fill-rect blend-mode command is valid.
- Native `JBRSkiaInterop.mm` gates command streams on ABI 63 and maps the payload to `SkBlendMode::kLighten` inside JBR's Skia runtime.
- Skiko compatibility requires ABI 63 before enabling command mode.
- Magic Jewel extends the blend-mode visual probe and screenshot assertion with a Lighten region.

Validation:
- Runtime API compile passed for ABI 63.
- JBR API validator passed with ABI 63 and the Lighten payload.
- Native `JBRSkiaInterop.mm` standalone build passed with the Lighten mapping.
- CMP focused recorder tests passed for Lighten and Darken blend-mode records.
- Skiko interop tests passed with ABI 63 fixture expectations.
- Magic Jewel Kotlin compile passed after adding the Lighten probe.
- Runtime API shim, Skiko Maven-local artifact, and CMP desktop jars were rebuilt for the live run.
- Magic Jewel ABI 63 Lighten live command probe passed: `/tmp/magic-jewel-command-probe-abi63-lighten-blend/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=448`.
- Screenshot assertion passed with `blendModeLighten=86070`.

Next:
- Continue exact blend-mode slices for direct Skia mappings, then pivot to the screenshot parity harness or JBR-owned shader/effect handles when the simple fill-rect blend family stops being the largest coverage gap.

## Checkpoint: ABI 64 Difference Blend-Mode Fill Rectangles

Status: completed as another exact direct-mapping blend-mode slice across JBR, public Runtime API, Skiko, CMP, and Magic Jewel.

What changed:

- Runtime API and JBR private API expose ABI 64 and the Difference blend-mode payload constant.
- JBR Java validation accepts Difference wherever the fill-rect blend-mode command is valid.
- Native `JBRSkiaInterop.mm` gates command streams on ABI 64 and maps the payload to `SkBlendMode::kDifference` inside JBR's Skia runtime.
- Skiko compatibility requires ABI 64 before enabling command mode.
- CMP records `BlendMode.Difference` solid fill rectangles through the structured blend-mode command when the paint has no shader, color filter, or path effect.
- Magic Jewel extends the blend-mode visual probe and screenshot assertion with a visible Difference region.
- `ROADMAP.md` now marks ABI 64 complete and keeps the two review-driven action tracks concrete:
  - window-only old/new screenshot parity with deterministic rich Swing/CMP/Jewel content and region-specific tolerances.
  - real generic shader/effect implementation through JBR-owned handles/descriptors, including RuntimeEffect/SKSL payloads, not raw Skiko Skia pointers.

Verification:

- Runtime API compile passed for ABI 64.
- JBR API validator passed with ABI 64 and the Difference payload.
- JBR native dylib compile passed at `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- CMP focused recorder tests passed for Difference and Lighten blend-mode records.
- Skiko interop tests passed with ABI 64 fixture expectations.
- Magic Jewel compile passed.
- Magic Jewel ABI 64 Difference live command probe passed: `/tmp/magic-jewel-command-probe-abi64-difference-blend-2/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=833`.
- The window screenshot assertion passed with `blendModeDifference=924`; the first run correctly caught that the new visual probe was hidden behind the Swing island, so the probe was moved into a visible top-row strip before accepting the slice.

Next:

- Continue exact blend-mode slices for remaining direct Skia mappings, or pause the blend series to implement the window-only old/new screenshot parity harness now that the command path has enough rich content to compare.

## Checkpoint: ABI 65 Exclusion Blend-Mode Fill Rectangles

Status: completed as another exact direct-mapping blend-mode slice across JBR, public Runtime API, Skiko, CMP, and Magic Jewel.

What changed:

- Runtime API and JBR private API expose ABI 65 and the Exclusion blend-mode payload constant.
- JBR Java validation accepts Exclusion wherever the fill-rect blend-mode command is valid.
- Native `JBRSkiaInterop.mm` gates command streams on ABI 65 and maps the payload to `SkBlendMode::kExclusion` inside JBR's Skia runtime.
- Skiko compatibility requires ABI 65 before enabling command mode.
- CMP records `BlendMode.Exclusion` solid fill rectangles through the structured blend-mode command when the paint has no shader, color filter, or path effect.
- Magic Jewel extends the blend-mode visual probe and screenshot assertion with a visible Exclusion region.
- `ROADMAP.md` marks ABI 65 complete and keeps the screenshot-parity and generic-shader implementation tracks as active follow-up work.

Verification:

- Runtime API compile passed for ABI 65.
- JBR API validator passed with ABI 65 and the Exclusion payload.
- Native `JBRSkiaInterop.mm` standalone build passed at `/tmp/jbr-skia-native/libjbrskiainterop.dylib` with the Exclusion mapping.
- CMP focused recorder tests passed for Exclusion and Difference blend-mode records.
- Skiko interop tests passed with ABI 65 fixture expectations.
- Magic Jewel compile passed.
- Magic Jewel ABI 65 Exclusion live command probe passed: `/tmp/magic-jewel-command-probe-abi65-exclusion-blend/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=570`.
- The window screenshot assertion passed with `blendModeExclusion=3202`.

Next:

- Continue exact direct-mapping blend-mode slices if we want more paint coverage quickly; otherwise the richer next validation slice is the window-only old/new screenshot parity harness.

## Checkpoint: ABI 66 ColorDodge Blend-Mode Fill Rectangles

Status: completed as another exact direct-mapping blend-mode slice across JBR, public Runtime API, Skiko, CMP, and Magic Jewel.

What changed:

- Runtime API and JBR private API expose ABI 66 and the ColorDodge blend-mode payload constant.
- JBR Java validation accepts ColorDodge wherever the fill-rect blend-mode command is valid.
- Native `JBRSkiaInterop.mm` gates command streams on ABI 66 and maps the payload to `SkBlendMode::kColorDodge` inside JBR's Skia runtime.
- Skiko compatibility requires ABI 66 before enabling command mode.
- CMP records `BlendMode.ColorDodge` solid fill rectangles through the structured blend-mode command when the paint has no shader, color filter, or path effect.
- Magic Jewel extends the blend-mode visual probe and screenshot assertion with a visible far-right ColorDodge region.
- `ROADMAP.md` marks ABI 66 complete and records the live probe path.

Verification:

- Runtime API compile passed for ABI 66.
- JBR API validator passed with ABI 66 and the ColorDodge payload.
- Native `JBRSkiaInterop.mm` standalone build passed at `/tmp/jbr-skia-native/libjbrskiainterop.dylib` with the ColorDodge mapping.
- CMP focused recorder tests passed for ColorDodge and Exclusion blend-mode records.
- Skiko interop tests passed with ABI 66 fixture expectations.
- Magic Jewel compile passed.
- Magic Jewel ABI 66 ColorDodge live command probe passed: `/tmp/magic-jewel-command-probe-abi66-color-dodge-blend/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=530`.
- The window screenshot assertion passed with `blendModeColorDodge=3200`.

Next:

- Continue exact direct-mapping blend-mode slices, likely `ColorBurn`, or pivot to the window-only old/new screenshot parity harness now that the blend probe has enough varied color math to catch more regressions.

## Checkpoint: ABI 67 ColorBurn Blend-Mode Fill Rectangles

Status: completed as another exact direct-mapping blend-mode slice across JBR, public Runtime API, Skiko, CMP, and Magic Jewel.

What changed:

- Runtime API and JBR private API expose ABI 67 and the ColorBurn blend-mode payload constant.
- JBR Java validation accepts ColorBurn wherever the fill-rect blend-mode command is valid.
- Native `JBRSkiaInterop.mm` gates command streams on ABI 67 and maps the payload to `SkBlendMode::kColorBurn` inside JBR's Skia runtime.
- Skiko compatibility requires ABI 67 before enabling command mode.
- CMP records `BlendMode.ColorBurn` solid fill rectangles through the structured blend-mode command when the paint has no shader, color filter, or path effect.
- Magic Jewel extends the blend-mode visual probe and screenshot assertion with a visible far-right lower ColorBurn region.
- `ROADMAP.md` marks ABI 67 complete and records the live probe path.

Verification:

- Runtime API compile passed for ABI 67.
- JBR API validator passed with ABI 67 and the ColorBurn payload.
- Native `JBRSkiaInterop.mm` standalone build passed at `/tmp/jbr-skia-native/libjbrskiainterop.dylib` with the ColorBurn mapping.
- CMP focused recorder tests passed for ColorBurn and ColorDodge blend-mode records.
- Skiko interop tests passed with ABI 67 fixture expectations.
- Magic Jewel compile passed.
- Magic Jewel ABI 67 ColorBurn live command probe passed: `/tmp/magic-jewel-command-probe-abi67-color-burn-blend/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=846`.
- The window screenshot assertion passed with `blendModeColorBurn=1500`.

Next:

- Either continue with `Softlight` direct mapping, or start the window-only old/new screenshot parity harness. The latter is increasingly valuable because the current Magic Jewel scene now has enough blend/color/animation/layer content for meaningful visual-diff regions.

## Checkpoint: ABI 68 Hardlight Blend-Mode Fill Rectangles

Status: completed as another exact direct-mapping blend-mode slice across JBR, public Runtime API, Skiko, CMP, and Magic Jewel.

What changed:

- Runtime API and JBR private API expose ABI 68 and the Hardlight blend-mode payload constant.
- JBR Java validation accepts Hardlight wherever the fill-rect blend-mode command is valid.
- Native `JBRSkiaInterop.mm` gates command streams on ABI 68 and maps the payload to `SkBlendMode::kHardLight` inside JBR's Skia runtime.
- Skiko compatibility requires ABI 68 before enabling command mode.
- CMP records `BlendMode.Hardlight` solid fill rectangles through the structured blend-mode command when the paint has no shader, color filter, or path effect.
- Magic Jewel extends the blend-mode visual probe and screenshot assertion with a visible far-right lower Hardlight region.
- `ROADMAP.md` marks ABI 68 complete and records the live probe path.

Verification:

- Runtime API compile passed for ABI 68.
- JBR API validator passed with ABI 68 and the Hardlight payload.
- Native `JBRSkiaInterop.mm` standalone build passed at `/tmp/jbr-skia-native/libjbrskiainterop.dylib` with the Hardlight mapping.
- CMP focused recorder tests passed for Hardlight and ColorBurn blend-mode records.
- Skiko interop tests passed with ABI 68 fixture expectations.
- Magic Jewel compile passed.
- Magic Jewel ABI 68 Hardlight live command probe passed: `/tmp/magic-jewel-command-probe-abi68-hardlight-blend/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=820`.
- The window screenshot assertion passed with `blendModeHardlight=2250`.

Next:

- Continue exact direct-mapping blend-mode coverage, then decide whether to switch to the window-only old/new screenshot parity harness before less common blend/effect modes.

## Checkpoint: ABI 69 Softlight Blend-Mode Fill Rectangles

Status: completed as another exact direct-mapping blend-mode slice across JBR, public Runtime API, Skiko, CMP, and Magic Jewel.

What changed:

- Runtime API and JBR private API expose ABI 69 and the Softlight blend-mode payload constant.
- JBR Java validation accepts Softlight wherever the fill-rect blend-mode command is valid.
- Native `JBRSkiaInterop.mm` gates command streams on ABI 69 and maps the payload to `SkBlendMode::kSoftLight` inside JBR's Skia runtime.
- Skiko compatibility requires ABI 69 before enabling command mode.
- CMP records `BlendMode.Softlight` solid fill rectangles through the structured blend-mode command when the paint has no shader, color filter, or path effect.
- Magic Jewel extends the blend-mode visual probe and screenshot assertion with a visible far-right lower Softlight region.
- `ROADMAP.md` marks ABI 69 complete and records the live probe path.

Verification:

- Runtime API compile passed for ABI 69.
- JBR API validator passed with ABI 69 and the Softlight payload.
- Native `JBRSkiaInterop.mm` standalone build passed at `/tmp/jbr-skia-native/libjbrskiainterop.dylib` with the Softlight mapping.
- CMP focused recorder tests passed for Softlight and Hardlight blend-mode records.
- Skiko interop tests passed with ABI 69 fixture expectations.
- Magic Jewel compile passed.
- Magic Jewel ABI 69 Softlight live command probe passed: `/tmp/magic-jewel-command-probe-abi69-softlight-blend/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=824`.
- The window screenshot assertion passed with `blendModeSoftlight=4360`.

Next:

- Continue exact direct-mapping blend modes where Skia has a one-to-one `SkBlendMode`, or switch to the window-only old/new screenshot parity harness now that the blend probe has broad visible coverage.

## Checkpoint: ABI 70 Hue Blend-Mode Fill Rectangles

Status: completed as another exact direct-mapping blend-mode slice across JBR, public Runtime API, Skiko, CMP, and Magic Jewel.

What changed:

- Runtime API and JBR private API expose ABI 70 and the Hue blend-mode payload constant.
- JBR Java validation accepts Hue wherever the fill-rect blend-mode command is valid.
- Native `JBRSkiaInterop.mm` gates command streams on ABI 70 and maps the payload to `SkBlendMode::kHue` inside JBR's Skia runtime.
- Skiko compatibility requires ABI 70 before enabling command mode.
- CMP records `BlendMode.Hue` solid fill rectangles through the structured blend-mode command when the paint has no shader, color filter, or path effect.
- Magic Jewel extends the blend-mode visual probe and screenshot assertion with a visible far-right lower Hue region.
- `ROADMAP.md` marks ABI 70 complete and records the live probe path.

Verification:

- Runtime API compile passed for ABI 70.
- JBR API validator passed with ABI 70 and the Hue payload.
- Native `JBRSkiaInterop.mm` standalone build passed at `/tmp/jbr-skia-native/libjbrskiainterop.dylib` with the Hue mapping.
- CMP focused recorder tests passed for Hue and Softlight blend-mode records.
- Skiko interop tests passed with ABI 70 fixture expectations.
- Magic Jewel compile passed.
- Magic Jewel ABI 70 Hue live command probe passed: `/tmp/magic-jewel-command-probe-abi70-hue-blend/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=739`.
- The window screenshot assertion passed with `blendModeHue=1508`.

Next:

- Continue the component blend family with `Color` and `Luminosity`, or spend a slice on the old/new screenshot parity harness before adding more modes.

## Checkpoint: ABI 71 Saturation Blend-Mode Fill Rectangles

Status: completed as another exact direct-mapping blend-mode slice across JBR, public Runtime API, Skiko, CMP, and Magic Jewel.

What changed:

- Runtime API and JBR private API expose ABI 71 and the Saturation blend-mode payload constant.
- JBR Java validation accepts Saturation wherever the fill-rect blend-mode command is valid.
- Native `JBRSkiaInterop.mm` gates command streams on ABI 71 and maps the payload to `SkBlendMode::kSaturation` inside JBR's Skia runtime.
- Skiko compatibility requires ABI 71 before enabling command mode.
- CMP records `BlendMode.Saturation` solid fill rectangles through the structured blend-mode command when the paint has no shader, color filter, or path effect.
- Magic Jewel extends the blend-mode visual probe and screenshot assertion with a visible far-right lower Saturation region.
- `ROADMAP.md` marks ABI 71 complete and records the live probe path.

Verification:

- Runtime API compile passed for ABI 71.
- JBR API validator passed with ABI 71 and the Saturation payload.
- Native `JBRSkiaInterop.mm` standalone build passed at `/tmp/jbr-skia-native/libjbrskiainterop.dylib` with the Saturation mapping.
- CMP focused recorder tests passed for Saturation and Hue blend-mode records.
- Skiko interop tests passed with ABI 71 fixture expectations.
- Magic Jewel compile passed.
- Magic Jewel ABI 71 Saturation live command probe passed: `/tmp/magic-jewel-command-probe-abi71-saturation-blend/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=485`.
- The window screenshot assertion passed with `blendModeSaturation=6518`.

Next:

- Continue the component blend family with `Color` and `Luminosity`, then reassess whether the old/new screenshot parity harness should become the next priority checkpoint.

## Checkpoint: ABI 72 Color Blend-Mode Fill Rectangles

Status: completed as another exact direct-mapping blend-mode slice across JBR, public Runtime API, Skiko, CMP, and Magic Jewel.

What changed:

- Runtime API and JBR private API expose ABI 72 and the Color blend-mode payload constant.
- JBR Java validation accepts Color wherever the fill-rect blend-mode command is valid.
- Native `JBRSkiaInterop.mm` gates command streams on ABI 72 and maps the payload to `SkBlendMode::kColor` inside JBR's Skia runtime.
- Skiko compatibility requires ABI 72 before enabling command mode.
- CMP records `BlendMode.Color` solid fill rectangles through the structured blend-mode command when the paint has no shader, color filter, or path effect.
- Magic Jewel extends the blend-mode visual probe and screenshot assertion with a visible lower Color region.
- `ROADMAP.md` marks ABI 72 complete and records the live probe path.

Verification:

- Runtime API compile passed for ABI 72.
- JBR API validator passed with ABI 72 and the Color payload.
- Native `JBRSkiaInterop.mm` standalone build passed at `/tmp/jbr-skia-native/libjbrskiainterop.dylib` with the Color mapping.
- CMP focused recorder tests passed for Color and Saturation blend-mode records.
- Skiko interop tests passed with ABI 72 fixture expectations.
- Magic Jewel compile passed.
- Magic Jewel ABI 72 Color live command probe passed: `/tmp/magic-jewel-command-probe-abi72-color-blend/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=585`.
- The window screenshot assertion passed with `blendModeColor=531786`.

Next:

- Finish the component blend family with `Luminosity`, then reassess whether the old/new screenshot parity harness should become the next priority checkpoint.

## Checkpoint: ABI 73 Luminosity Blend-Mode Fill Rectangles

Status: completed as the final component blend-mode slice across JBR, public Runtime API, Skiko, CMP, and Magic Jewel.

What changed:

- Runtime API and JBR private API expose ABI 73 and the Luminosity blend-mode payload constant.
- JBR Java validation accepts Luminosity wherever the fill-rect blend-mode command is valid.
- Native `JBRSkiaInterop.mm` gates command streams on ABI 73 and maps the payload to `SkBlendMode::kLuminosity` inside JBR's Skia runtime.
- Skiko compatibility requires ABI 73 before enabling command mode.
- CMP records `BlendMode.Luminosity` solid fill rectangles through the structured blend-mode command when the paint has no shader, color filter, or path effect.
- Magic Jewel extends the blend-mode visual probe and screenshot assertion with a visible lower Luminosity region.
- `ROADMAP.md` marks ABI 73 complete and records the live probe path.

Verification:

- Runtime API compile passed for ABI 73.
- Native `JBRSkiaInterop.mm` standalone build passed at `/tmp/jbr-skia-native/libjbrskiainterop.dylib` with the Luminosity mapping.
- Refreshed `/tmp/jbr-skia-run/desktop` patched classes so the Java service ABI matched the public API and native dylib. The first live run correctly fell back as `native-abi-mismatch` while those classes were stale.
- CMP focused recorder tests passed for Luminosity and Color blend-mode records.
- Skiko interop tests passed with ABI 73 fixture expectations.
- Magic Jewel compile passed.
- Magic Jewel ABI 73 Luminosity live command probe passed: `/tmp/magic-jewel-command-probe-abi73-luminosity-blend-2/suite.tsv`, with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=350`.
- The window screenshot assertion passed with `blendModeLuminosity=3200`.
- An attempted source-tree `javac --patch-module` run was discarded as an invalid local harness because it asked stock javac to compile unrelated JBR source/generated-code surfaces. The patched-class refresh plus native compile are the useful local checks for this slice until a full configured JBR build is wired.

Next:

- Start the window-only old/new screenshot parity harness for rich Swing/CMP/Jewel content, with deterministic animation phases so animated blend probes, gradients, progress bars, and Swing islands can be compared repeatably.

## Checkpoint: Window-Only Screenshot Parity And Swing Repaint Preservation

Status: completed as the first rich old/new screenshot parity harness and a Skiko-side preservation guard for Swing-driven interop-only repaint passes.

What changed:

- Magic Jewel gained deterministic screenshot controls:
  - `magic.jewel.fixedAnimationPhase`
  - `magic.jewel.fixedFrameTicks`
  - `magic.jewel.pauseSwingAnimation`
- The report harness can now capture old and new renderer windows by window id/title, without full-screen screenshot-and-crop fallback.
- Added `scripts/jbr-skia-screenshot-parity.sh`, which runs old SwingGraphics and new JBR Skia command mode with the same deterministic Magic Jewel scene, captures both windows, runs strict command screenshot assertions, and then compares the two PNGs with a Swift image-diff helper.
- Added `scripts/compare-jbr-skia-window-screenshots.sh`, which emits a stable `JBR_SKIA_SCREENSHOT_PARITY ...` marker with size, average delta, max delta, bad pixel count, and bad pixel ratio.
- Skiko command mode no longer self-schedules a repaint after every successful command replay. That loop created extra tiny command streams after a useful full-frame replay.
- Skiko now caches the last meaningful command stream and replays it for tiny interop-only command streams. This prevents Swing child repaint/layout passes from replacing the full Compose scene with a mostly blank destination while still allowing real Compose command frames to refresh the cache.
- The preservation cache clears on Skiko removal and JBR destination surface replacement, so it does not carry stale commands across surface migration/resize.
- Magic Jewel screenshot thresholds were tuned for the richer all-probes parity scene, where Skia text/raster differences can shift paragraph and arc-probe pixel counts without indicating a blank or misplaced scene.

Verification:

- Skiko focused interop tests passed, including new command-frame cache tests:
  - cache returns the current meaningful stream
  - cache replays the last meaningful stream for a minimal interop-only stream
  - cache returns a minimal stream if no meaningful stream exists yet
  - cache clears after surface replacement/removal
- Skiko snapshot was republished locally for Magic Jewel.
- Magic Jewel compile passed with the deterministic parity properties.
- Magic Jewel frozen old/new screenshot parity passed:
  - `/tmp/magic-jewel-screenshot-parity-cache-guard-3/summary.tsv`
  - report: `/tmp/magic-jewel-screenshot-parity-cache-guard-3/report/report.md`
- The parity run emitted `SKIKO_JBR_INTEROP_COMMAND_REPLAY_CACHED` markers for tiny 26-word interop-only streams and rendered the cached full scene instead of blanking the window.
- Magic Jewel non-frozen command-mode smoke passed:
  - report: `/tmp/magic-jewel-command-animation-smoke/report.md`
  - `new` app frames: `980`, `app_new_fps=163.3`
  - `jbr_command_frames=980`, `jbr_command_fps=163.3`
  - zero picture replay and zero unsupported command frames
- The non-frozen smoke screenshot shows advancing Magic Jewel frame/tick counters, confirming the deterministic parity freeze did not disable normal animation.

Open risks:

- The Skiko preservation guard currently uses command-stream size as the signal for interop-only repaint frames. That is acceptable for this PoC checkpoint, but the next cleanup should replace it with an explicit recorder/frame-kind marker from CMP/Skiko so a legitimately tiny full-scene frame is not misclassified.
- The parity script persists old/new screenshots and pass/fail markers, but does not yet persist a diff image or per-region ownership metrics into `summary.properties`.
- Old/new parity is intentionally tolerant of text/raster differences. Compose/Jewel-owned regions still need a more explicit ownership map so Swing text changes do not hide real Compose geometry drift.

Next:

- Replace the cache-size heuristic with an explicit frame-kind signal, then add diff-image/ownership-region output to the parity report. After that, the next feature slice should be graphics-layer nested command recording or the first real JBR-owned generic shader/effect handle.

## Checkpoint: Explicit Command Frame Kinds For Interop Repaints

Status: completed as a CMP/Skiko cleanup over the screenshot-preservation checkpoint.

What changed:

- Skiko `JbrSkiaCommandRenderDelegate` now supports `renderJbrSkiaCommandFrameInfo(...)`, returning a `JbrSkiaCommandFrame` with a `JbrSkiaCommandFrameKind` of `FullScene`, `InteropOnly`, or `Unknown`.
- Skiko command replay preserves the previous meaningful command stream only when CMP explicitly reports an `InteropOnly` frame, with the old command-size heuristic retained only for legacy `Unknown` delegates.
- CMP `JbrSkiaCommandRecorder.recordFrame(...)` now returns structured per-frame metadata: command word count, unsupported count, image define/ref/cache counters, and text/paragraph counters.
- CMP `ComposeSceneMediator` classifies command recordings before handing them to Skiko and emits `CMP_JBR_COMMAND_FRAME_KIND kind=... commands=...` markers for report/debug tooling.
- Magic Jewel reports now summarize frame-kind markers in `report.md` and `summary.properties` using stable keys including `cmp_frame_kind_full_scene`, `cmp_frame_kind_interop_only`, and `cmp_frame_kind_unknown`.
- `ROADMAP.md` marks explicit frame-kind repaint preservation complete and keeps diff-image/per-region parity metrics as the next validation-harness gap.

Verification:

- Skiko focused interop tests passed and the updated Skiko snapshot was published locally:
  - `./gradlew --no-daemon :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest :skiko:publishToMavenLocal`
- CMP recorder metadata test passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.recordsFrameMetadata`
- CMP desktop jars rebuilt successfully for Magic Jewel:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar :compose:foundation:foundation:desktopJar :compose:material3:material3:desktopJar`
- Magic Jewel non-frozen command-mode animation smoke passed:
  - report: `/tmp/magic-jewel-command-animation-frame-kind/report.md`
  - `app_new_fps=65.3`, `swing_new_fps=75.8`, `jbr_command_fps=65.5`
  - screenshot assertion passed with zero picture replay and zero fallback markers.
- Magic Jewel frozen old/new screenshot parity passed with explicit interop-only replay markers:
  - `/tmp/magic-jewel-screenshot-parity-frame-kind-report/summary.tsv`
  - report: `/tmp/magic-jewel-screenshot-parity-frame-kind-report/report/report.md`
  - frame-kind summary: `FullScene=53`, `InteropOnly=478`, `Unknown=0` during the sampled window.
  - logs show `CMP_JBR_COMMAND_FRAME_KIND kind=InteropOnly commands=26` followed by `SKIKO_JBR_INTEROP_COMMAND_REPLAY_CACHED kind=InteropOnly ...`.

Open risks:

- CMP's first classification rule is intentionally conservative: tiny command frames with no unsupported/text/image activity are treated as interop-only. This is now isolated to CMP and visible in report markers, but a production version should carry an even stronger paint-origin signal from the Swing/interop invalidation path if possible.
- Magic Jewel still needs persisted diff images and per-region ownership metrics so old/new screenshot comparisons can distinguish Compose/Jewel drift from expected Swing/text-rendering differences.

Next:

- Add diff-image and ownership-region output to the parity report, or start the graphics-layer nested command recording slice. Generic shader/effect handles remain the larger non-negotiable end-state track after the current macOS MVP hardening.

## Checkpoint: Screenshot Parity Diff Artifacts

Status: completed as a Magic Jewel validation-harness slice.

What changed:

- `scripts/compare-jbr-skia-window-screenshots.sh` now optionally writes a heatmap-style PNG diff image.
- The comparator emits whole-window metrics plus coarse ownership-region metrics for `headerControls`, `composeCanvas`, `swingIsland`, and `rightProbeStrip`.
- The comparator records original old/new capture sizes and a `dimensionsMatch` field. If macOS/JBR window chrome produces mismatched captures, the tool compares the common captured area and reports the mismatch explicitly instead of failing before diagnostics are emitted.
- `scripts/jbr-skia-screenshot-parity.sh` now appends the parity log to `report.md`, writes `parity-diff.png`, and mirrors scalar parity metrics into `summary.properties` as `screenshot_parity_*` keys.
- Magic Jewel README now documents the diff artifact and stable parity metric keys.
- `ROADMAP.md` marks persisted diff images and per-region parity metrics complete.

Verification:

- Shell syntax checks passed for the updated scripts.
- Comparator smoke against a dimension-mismatched old/new pair emitted `dimensionsMatch=false` and wrote a diff image instead of crashing.
- Full window-only old/new parity passed after stopping the live sample app:
  - `/tmp/magic-jewel-screenshot-parity-diff-2/summary.tsv`
  - report: `/tmp/magic-jewel-screenshot-parity-diff-2/report/report.md`
  - diff image: `/tmp/magic-jewel-screenshot-parity-diff-2/report/parity-diff.png`
  - summary metrics include `screenshot_parity_avgDelta=1.646`, `screenshot_parity_badPixelRatio=0.03910`, and region-specific `screenshot_parity_region_*` keys.

Next:

- Continue macOS MVP hardening. Good next slices are graphics-layer nested command recording or the first real JBR-owned shader/effect handle implementation, with the parity diff artifact now available as a regression gate.

## Checkpoint: Nested Graphics-Layer Command Replay

Status: completed as a narrow CMP/Magic Jewel rendering slice.

What changed:

- CMP `JbrSkiaCommandRecorder` can now create a nested child command recording while preserving the parent recorder, then splice that child stream back at the eventual layer draw site.
- CMP Skiko `GraphicsLayer` records layer-local drawing into that child stream when the root command recorder is active.
- Supported command-layer replay is intentionally narrow: non-released 2D layers with finite alpha, translation, scale, rotationZ, no clip/shadow/color-filter/render-effect, and `BlendMode.SrcOver`.
- Replay brackets the child commands with save, layer transform, saveLayer alpha, child payload, restore, restore. Unsupported layer semantics still report `graphicsLayer` and fall back to the old behavior.
- Magic Jewel's graphics-layer probe graduated from expected fallback to command replay while retaining the old `commands-graphics-layer-fallback` case alias for compatibility.
- `ROADMAP.md` records the completed narrow graphics-layer subset and splits the remaining layer semantics into explicit follow-up work.

Verification:

- Focused CMP nested recorder test passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysAtLayerDrawSite`
- CMP desktop jars rebuilt successfully for Magic Jewel:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopJar :compose:ui:ui:desktopJar :compose:foundation:foundation:desktopJar :compose:material3:material3:desktopJar`
- Magic Jewel graphics-layer report passed with zero fallbacks and zero unsupported command frames:
  - report: `/tmp/magic-jewel-graphics-layer-command/report.md`
  - `CMP command recorder: frames=300 fps=75.0 ... unsupported_frames=0`
  - `JBR command frames: frames=300 fps=75.0`
  - screenshot assertion passed with `probeRightPurple=35580`.
- Magic Jewel command probe suite case passed:
  - `/tmp/magic-jewel-command-suite-graphics-layer/suite.tsv`
  - `status=passed fallback_new_count=0 unsupported=none jbr_picture_frames=0 jbr_command_frames=317`.

Known test note:

- The full `JbrSkiaCommandRecorderTest` class still has unrelated exact-array expectation failures in three older gradient/image-shader tests (`writesSweepGradientStrokeRectRecord`, `writesRadialGradientRoundRectRecord`, `writesImageShaderRectRecord`). The new nested-layer test and app-level graphics-layer probe pass; the older failures reproduce when run individually and should be cleaned up in a separate test-maintenance slice.

Next:

- Continue layer coverage only where semantics are explicit: first add clip/outline or non-SrcOver/color-filter layer paint support, or pivot to the JBR-owned generic shader/effect handle implementation needed for `RenderEffect`.

## Checkpoint: Rectangular Graphics-Layer Clip Replay

Status: completed as a follow-up graphics-layer semantics slice.

What changed:

- CMP nested graphics-layer replay now accepts an optional rectangular clip and emits `COMMAND_CLIP_RECT` inside the saved layer before splicing the child command stream.
- CMP `GraphicsLayer` treats `clip=true` with a rectangular outline as command-replayable. Non-rectangular outlines still remain outside this slice.
- Magic Jewel gained `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_CLIP`, which adds overflowing cyan content inside the rotated alpha layer and turns on rectangular layer clipping.
- Magic Jewel report/help output and screenshot assertion now surface and validate the clipped graphics-layer probe.
- The command probe suite includes `commands-graphics-layer-clip`.

Verification:

- Focused CMP nested recorder test passed after adding clip plumbing:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysAtLayerDrawSite`
- CMP desktop jars rebuilt successfully for Magic Jewel.
- Magic Jewel clipped graphics-layer suite case passed:
  - `/tmp/magic-jewel-command-suite-graphics-layer-clip/suite.tsv`
  - `status=passed fallback_new_count=0 unsupported=none jbr_picture_frames=0 jbr_command_frames=143`
  - report: `/tmp/magic-jewel-command-suite-graphics-layer-clip/commands-graphics-layer-clip/report.md`
  - screenshot assertion passed with `probeRightCyan=20561` and `probeRightPurple=10819`.

Next:

- The remaining layer semantics need their own explicit contracts: rounded outline clips can reuse path commands, generic path outlines need their own probe, while color filters, render effects, and non-SrcOver layer paints should wait for the JBR-owned effect/shader handle path.

## Checkpoint: Rounded Graphics-Layer Clip Replay

Status: completed as a follow-up graphics-layer semantics slice.

What changed:

- CMP nested graphics-layer replay now accepts an optional path clip and emits `COMMAND_CLIP_PATH` inside the saved layer before splicing the child command stream.
- CMP `GraphicsLayer` treats `clip=true` with a rounded outline as command-replayable.
- CMP command-only layer recording now records layer-local content into a plain Skia `PictureRecorder` canvas instead of the clipped `RenderNode` recording canvas, so the layer clip is represented exactly once by the parent replay command.
- CMP path command serialization now lowers Skiko conic path segments to quadratic path verbs for this command ABI. This makes rounded outlines serializable without adding a new conic verb yet.
- Magic Jewel gained `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_ROUND_CLIP`, which turns on a rounded clipped graphics layer with overflowing cyan content.
- Magic Jewel report/help output, command probe suite, and screenshot assertion now surface and validate the rounded clipped graphics-layer probe.

Verification:

- Focused CMP tests passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRoundedClipPathRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerClipPath :compose:ui:ui-graphics:desktopJar`
- Magic Jewel rounded clipped graphics-layer suite case passed:
  - `/tmp/magic-jewel-command-suite-graphics-layer-round-clip/suite.tsv`
  - `status=passed fallback_new_count=0 unsupported=none jbr_picture_frames=0 jbr_command_frames=337`
  - report: `/tmp/magic-jewel-command-suite-graphics-layer-round-clip/commands-graphics-layer-round-clip/report.md`

Known notes:

- Conic lowering is a PoC-compatible approximation through existing quadratic path verbs. A production ABI can either keep this lowering with explicit tolerance documentation or add a versioned conic path verb if exact rational quadratics become necessary.
- The full `JbrSkiaCommandRecorderTest` class still has unrelated exact-array expectation failures in three older gradient/image-shader tests (`writesSweepGradientStrokeRectRecord`, `writesRadialGradientRoundRectRecord`, `writesImageShaderRectRecord`). Focused graphics-layer/path tests pass.

Next:

- Continue layer coverage only where semantics are explicit: non-SrcOver layer paints, color filters, and render effects should wait for the JBR-owned effect/shader handle path.

## Checkpoint: Generic Path Graphics-Layer Clip Replay

Status: completed as a follow-up graphics-layer semantics slice.

What changed:

- CMP `GraphicsLayer` now treats `clip=true` with `Outline.Generic` as command-replayable when the path can be serialized by the existing path command ABI.
- Generic path layer outlines are replayed through the same parent-scope `COMMAND_CLIP_PATH` used by rounded outlines, before the nested child command stream is spliced into the frame.
- Magic Jewel gained `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_PATH_CLIP`, which uses a non-rectangular `GenericShape` with overflowing cyan content to validate path-outline clipping.
- Magic Jewel report/help output, command probe suite, README, and screenshot assertion now surface and validate the generic-path clipped graphics-layer probe.

Verification:

- Focused CMP tests passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerClipPath --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesClipPathRecord`
- Magic Jewel compiled after the new probe wiring:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
- Magic Jewel generic-path clipped graphics-layer suite case passed:
  - `/tmp/magic-jewel-command-suite-graphics-layer-path-clip/suite.tsv`
  - `status=passed fallback_new_count=0 unsupported=none jbr_picture_frames=0 jbr_command_frames=365`
  - report: `/tmp/magic-jewel-command-suite-graphics-layer-path-clip/commands-graphics-layer-path-clip/report.md`

Known notes:

- This does not broaden arbitrary graphics-layer effects. It only allows path outlines that lower to the already-supported move/line/quad/cubic/close path command stream.
- The full `JbrSkiaCommandRecorderTest` class still has unrelated exact-array expectation failures in three older gradient/image-shader tests (`writesSweepGradientStrokeRectRecord`, `writesRadialGradientRoundRectRecord`, `writesImageShaderRectRecord`). Focused graphics-layer/path tests pass.

Next:

- Continue layer coverage only where semantics are explicit: non-SrcOver layer paints, color filters, and render effects should wait for the JBR-owned effect/shader handle path. The nearest small rendering slice is likely polishing animation/screenshot stability or choosing one directly mappable layer blend mode with a strict old/new screenshot gate.

## Checkpoint: ABI 74 Save-Layer Blend Mode

Status: in progress; source, focused JVM-side validation, and the full CMP command-recorder regression gate are complete. Native JBR/live Magic Jewel validation is blocked on local Xcode license acceptance.

What changed:

- JBR private API, public Runtime API, Skiko's compatibility gate, and CMP command recorder now use command ABI 74.
- Added capability `COMMAND_CAP64_SAVE_LAYER_BLEND_MODE` and opcode `COMMAND_SAVE_LAYER_BLEND_MODE = 50`.
- The new command shape is `[op, 36, 0, x, y, width, height, alpha1000, blendMode]`, using the same direct Skia blend-mode payload values already accepted by fill-rect blend commands.
- JBR Java validation and native Metal replay accept the new command and map its blend payload to `SkPaint::setBlendMode(...)` on the saved layer paint.
- CMP now records `Canvas.saveLayer(...)` and command-recorded `GraphicsLayer` replay with directly mapped non-SrcOver blend modes instead of marking those layer paints unsupported.
- Magic Jewel gained `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_BLEND_MODE`, and the command probe suite gained `commands-graphics-layer-blend-mode`.

Verification:

- Focused CMP tests passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesSaveLayerBlendModeRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysAtLayerDrawSite`
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerBlendMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesSaveLayerBlendModeRecord`
- The full CMP command-recorder desktop test class passed after refreshing stale exact-array expectations for the ABI 74 stream header/lengths:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Skiko AWT sources compiled after the ABI/capability gate bump:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:compileKotlinAwt`
- Skiko interop tests passed after adding explicit coverage that a JBR missing only `COMMAND_CAP64_SAVE_LAYER_BLEND_MODE` falls back with `command-capability-mismatch`:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Magic Jewel compiled after the graphics-layer blend probe wiring:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
- JBR `make test TEST='test/jdk/jb/JBRSkia/JBRSkiaApiTest.java'` could not run yet:
  - an existing config was unavailable in this worktree;
  - `bash configure --with-skia-interop=bundled --disable-warnings-as-errors` failed because the active developer directory is Command Line Tools;
  - `bash configure --with-xcode-path=/Applications/Xcode.app --with-skia-interop=bundled --disable-warnings-as-errors` found Xcode but failed because the Xcode license has not been accepted on this machine.

Known notes:

- This is structured save-layer blend support, not arbitrary layer effects. Layer color filters and render effects still need the JBR-owned descriptor/handle path.
- Live Magic Jewel command-mode validation should run after JBR can be configured/built with Xcode available and the ABI 74 Skiko/JBR artifacts are refreshed together.

Next:

- Unblock JBR validation by accepting the local Xcode license or using a preconfigured JBR build environment, then run the JBR API test and `commands-graphics-layer-blend-mode` Magic Jewel probe with refreshed ABI 74 artifacts.

## Checkpoint: Graphics-Layer Tint Color Filter

Status: source and JVM-side validation are complete; this reuses ABI 74 and does not require a new JBR/native command. Live Magic Jewel validation remains blocked until ABI 74 JBR/Skiko artifacts can be refreshed together.

What changed:

- CMP graphics-layer command replay now accepts `ColorFilter.tint(..., BlendMode.SrcIn)` on a supported 2D graphics layer.
- The layer replay emits the existing `COMMAND_SAVE_LAYER_COLOR_FILTER` opcode around the nested child command stream, so no raw Skiko `SkColorFilter*` crosses the ABI.
- Graphics layers with a non-SrcOver blend mode and a color filter still fall back explicitly as `graphicsLayer:colorFilterBlendMode`.
- Non-tint/non-SrcIn graphics-layer color filters still fall back explicitly as `graphicsLayer:colorFilter`.
- Magic Jewel gained `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_COLOR_FILTER`, a command-probe suite row, report/help wiring, and a screenshot-region assertion for the tinted layer.

Verification:

- Focused CMP tests passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerTintColorFilter --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesSaveLayerTintColorFilterRecord`
- Full CMP command-recorder desktop test class passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Magic Jewel compiled with the new probe wiring:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`

Known notes:

- This is narrow tint/SrcIn layer color-filter support only. General color matrices, lighting filters, image filters, and layer render effects still require the JBR-owned effect/shader handle path.
- Live `commands-graphics-layer-color-filter` validation should run after the local JBR native build is unblocked.

## Checkpoint: ABI 75 Combined Save-Layer Blend And Tint

Status: source and JVM-side validation are complete; native JBR/live Magic Jewel validation is blocked on local Xcode license acceptance.

What changed:

- JBR private API, public Runtime API, Skiko's compatibility gate, and CMP command recorder now use command ABI 75.
- Added capability `COMMAND_CAP64_SAVE_LAYER_BLEND_COLOR_FILTER` and opcode `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER = 51`.
- The new command shape is `[op, 44, 0, x, y, width, height, alpha1000, blendMode, colorFilterArgb, colorFilterBlendMode]`.
- `blendMode` uses the direct Skia blend-mode values already accepted by fill-rect and save-layer blend commands.
- `colorFilterBlendMode` is still intentionally narrow and must be `COMMAND_BLEND_MODE_SRC_IN`.
- JBR Java validation, Java2D fallback replay, and native Metal replay accept the new command. Native replay sets both `SkPaint::setBlendMode(...)` and `SkPaint::setColorFilter(SkColorFilters::Blend(..., kSrcIn))` on the saved layer paint.
- CMP records command-mode `GraphicsLayer` replay with both a directly mapped non-SrcOver blend mode and a tint/SrcIn color filter instead of falling back.
- Skiko's strict ABI/capability gate now requires ABI 75 and the new combined save-layer paint capability.
- Magic Jewel gained a `commands-graphics-layer-blend-color-filter` command-probe row by enabling both existing graphics-layer blend and color-filter flags.

Verification:

- Focused CMP tests passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerBlendModeAndTintColorFilter --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerTintColorFilter --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerBlendMode`
- Full CMP command-recorder desktop test class passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Skiko interop tests passed:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Magic Jewel compiled:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
- Magic Jewel script syntax checks passed:
  - `bash -n scripts/jbr-skia-command-probe-suite.sh`

Known notes:

- This does not broaden color-filter support beyond tint/SrcIn. It only removes the artificial fallback when a supported graphics-layer blend mode and supported graphics-layer tint filter are used together.
- JBR native test and live `commands-graphics-layer-blend-color-filter` validation should run after the local JBR native build is unblocked.

## Checkpoint: ABI 76 Color-Matrix Effect Descriptor

Status: source and JVM-side validation are complete; native JBR/live Magic Jewel validation remains blocked on local Xcode license acceptance.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gate, and CMP command recorder now use command ABI 76.
- Added capability `COMMAND_CAP64_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER` and descriptor type `COMMAND_EFFECT_DESCRIPTOR_COLOR_MATRIX_FILTER = 2`.
- The generic `COMMAND_DEFINE_EFFECT_DESCRIPTOR` envelope now accepts version-1 color-matrix descriptors with a 20-int payload containing `Float.floatToRawIntBits(...)` for the row-major 4x5 Skia color matrix.
- CMP remaps Compose color-matrix translation entries from Compose 0..255 units to Skia normalized units before serializing the descriptor, matching the existing Skiko `ColorFilter.makeMatrix` path.
- JBR Java validation rejects malformed/nonfinite color-matrix descriptor payloads. Java2D fallback replay applies the matrix approximately for solid fill colors, and native replay reconstructs `SkColorFilters::Matrix(...)` inside JBR-owned Skia.
- Skiko now requires the ABI 76 capability so older JBR builds fall back before command replay.
- Magic Jewel gained `MAGIC_JEWEL_COMPOSE_COLOR_MATRIX_FILTER` and a `commands-color-matrix-filter` command-probe row.

Verification so far:

- Skiko interop tests passed:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- CMP `ui-graphics` desktop sources compile:
  - `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:compileKotlinDesktop`
- Focused CMP desktop test execution is currently blocked by a broader `compose:ui` desktop compile issue resolving the Skiko JBR command-frame bridge package, before the new recorder test runs.

Known notes:

- This slice covers solid fill rectangles with `ColorFilter.colorMatrix(...)`; it does not yet cover color matrices on images, saveLayer paints, graphics layers, or arbitrary shaders/runtime effects.
- Native JBR test and live `commands-color-matrix-filter` validation should run after the local JBR native build is unblocked.

## Checkpoint: ABI 77 Lighting Effect Descriptor

Status: source and JVM-side validation are complete; native JBR/live Magic Jewel validation remains blocked on local Xcode license acceptance.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gate, and CMP command recorder now use command ABI 77.
- Added capability `COMMAND_CAP64_EFFECT_DESCRIPTOR_LIGHTING_FILTER` and descriptor type `COMMAND_EFFECT_DESCRIPTOR_LIGHTING_FILTER = 3`.
- The generic `COMMAND_DEFINE_EFFECT_DESCRIPTOR` envelope now accepts version-1 lighting descriptors with payload `[multiplyArgb, addArgb]`.
- JBR Java validation, Java2D fallback replay, and native Metal replay accept the lighting descriptor. Native replay reconstructs `SkColorFilters::Lighting(...)` inside JBR-owned Skia.
- CMP records solid fill rectangles using `ColorFilter.lighting(...)` through the existing descriptor define/use path instead of falling back.
- Skiko's strict ABI/capability gate now requires ABI 77 and the lighting descriptor capability.
- Magic Jewel gained `MAGIC_JEWEL_COMPOSE_LIGHTING_FILTER` and a `commands-lighting-filter` command-probe row.

Verification:

- Skiko interop tests passed:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- CMP `ui-graphics` desktop sources compile:
  - `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:compileKotlinDesktop`
- Magic Jewel compiled:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
- Magic Jewel script syntax checks passed:
  - `bash -n scripts/jbr-skia-interop-report.sh && bash -n scripts/jbr-skia-command-probe-suite.sh`

Known notes:

- This slice covers solid fill rectangles with `ColorFilter.lighting(...)`; it does not yet cover lighting filters on images, saveLayer paints, graphics layers, or arbitrary shaders/runtime effects.
- Native JBR test and live `commands-lighting-filter` validation should run after the local JBR native build is unblocked.

## Checkpoint: ABI 78 SaveLayer Descriptor-Handle Filters

Status: source and JVM-side validation are complete; native JBR/live Magic Jewel validation remains blocked on local Xcode license acceptance.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gate, and CMP command recorder now use command ABI 78.
- Added capability `COMMAND_CAP64_SAVE_LAYER_COLOR_FILTER_REF` and opcode `COMMAND_SAVE_LAYER_COLOR_FILTER_REF = 52`.
- The new saveLayer command carries `[x, y, width, height, alpha1000, handleHigh, handleLow]` and references a previously defined typed effect descriptor instead of embedding raw Skia pointers.
- JBR Java validation rejects undefined descriptor handles, malformed dimensions, bad alpha, and bad record shape. Java2D fallback replay validates the descriptor handle and preserves layer clipping; native Metal replay reconstructs the descriptor-backed `SkColorFilter` inside JBR-owned Skia and attaches it to the saveLayer paint.
- CMP records `saveLayer` and command-recorded graphics layers with `ColorFilter.colorMatrix(...)` or `ColorFilter.lighting(...)` by defining/reusing descriptor handles, then emitting the new saveLayer reference command.
- CMP intentionally still rejects descriptor color filters combined with non-SrcOver graphics-layer blend modes until a combined blend+descriptor ABI is introduced.
- Skiko's strict ABI/capability gate now requires ABI 78 and the saveLayer descriptor-reference capability.
- Magic Jewel gained `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_COLOR_MATRIX_FILTER` and a `commands-graphics-layer-color-matrix-filter` command-probe row.

Verification:

- Skiko interop tests passed:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- CMP `ui-graphics` desktop sources compile:
  - `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:compileKotlinDesktop`
- Magic Jewel compiled:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
- Magic Jewel script syntax checks passed:
  - `bash -n scripts/jbr-skia-interop-report.sh && bash -n scripts/jbr-skia-command-probe-suite.sh && bash -n scripts/assert-jbr-skia-command-window-screenshot.sh`

Known notes:

- Focused CMP recorder test execution still trips the broader `compose:ui` desktop compile issue resolving the Skiko JBR command-frame bridge package before the recorder test class runs.
- Native JBR test and live `commands-graphics-layer-color-matrix-filter` validation should run after the local JBR native build is unblocked.

## Checkpoint: ABI 79 Cached Image Descriptor-Handle Filters

Status: source and JVM-side validation passed; native JBR/live Magic Jewel validation remains blocked on local Xcode license acceptance.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gate, and CMP command recorder now use command ABI 79.
- Added capability `COMMAND_CAP64_DRAW_IMAGE_REF_COLOR_FILTER_REF` and opcode `COMMAND_DRAW_IMAGE_REF_COLOR_FILTER_REF = 53`.
- The new cached-image command carries `[srcLeft1000, srcTop1000, srcRight1000, srcBottom1000, dstLeft1000, dstTop1000, dstRight1000, dstBottom1000, cacheKeyHigh, cacheKeyLow, imageWidth, imageHeight, alpha1000, filterQuality, handleHigh, handleLow]`, so images can reference JBR-owned typed color-filter descriptors without sharing raw Skia pointers.
- JBR Java validation rejects undefined descriptor handles, malformed image dimensions, alpha/filter-quality errors, and bad record shape. Java2D fallback replay applies descriptor-backed tint, color-matrix, and lighting filters to cached ARGB images. Native Metal replay reconstructs the descriptor-backed `SkColorFilter` inside JBR-owned Skia and attaches it to the image paint.
- CMP records `drawImageRect` calls with `ColorFilter.colorMatrix(...)` or `ColorFilter.lighting(...)` by defining/reusing descriptor handles, then emitting the new cached-image reference command. Existing tint/SrcIn image filters keep using the narrower ABI 55 command for compatibility.
- Skiko's strict ABI/capability gate now requires ABI 79 and the image descriptor-reference capability.
- Magic Jewel gained `MAGIC_JEWEL_COMPOSE_IMAGE_COLOR_MATRIX_FILTER` and a `commands-image-color-matrix-filter` command-probe row.

Verification:

- Skiko interop tests passed:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- CMP `ui-graphics` desktop sources compile:
  - `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:compileKotlinDesktop`
- Magic Jewel compiled:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
- Magic Jewel script syntax checks passed:
  - `bash -n scripts/jbr-skia-interop-report.sh && bash -n scripts/jbr-skia-command-probe-suite.sh && bash -n scripts/assert-jbr-skia-command-window-screenshot.sh`

Known notes:

- Focused CMP recorder test execution still trips the broader `compose:ui` desktop compile issue resolving the Skiko JBR command-frame bridge package before the recorder test class runs.
- Native JBR test and live `commands-image-color-matrix-filter` validation should run after the local JBR native build is unblocked.

## Checkpoint: ABI 80 SaveLayer Blend + Descriptor-Handle Filters

Status: source and JVM-side validation passed; native JBR/live Magic Jewel validation remains blocked on local Xcode license acceptance.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gate, and CMP command recorder now use command ABI 80.
- Added capability `COMMAND_CAP64_SAVE_LAYER_BLEND_COLOR_FILTER_REF = Long.MIN_VALUE` and opcode `COMMAND_SAVE_LAYER_BLEND_COLOR_FILTER_REF = 54`.
- This consumes the sign bit of the 64-bit command capability mask; Skiko now requires the full `-1L` mask for ABI 80, and test coverage includes the old `Long.MAX_VALUE` mask as a missing-capability case.
- The new saveLayer command carries `[x, y, width, height, alpha1000, blendMode, handleHigh, handleLow]`, allowing graphics-layer/saveLayer paints to combine a direct Skia blend mode with a JBR-owned typed color-filter descriptor.
- JBR Java validation rejects undefined descriptor handles, invalid dimensions/alpha/blend modes, and bad record shape. Java2D fallback replay validates and clips the layer scope. Native Metal replay reconstructs the descriptor-backed `SkColorFilter` inside JBR-owned Skia, sets the layer blend mode, and attaches both to the saveLayer paint.
- CMP records command-replayed graphics layers with both non-SrcOver blend modes and `ColorFilter.colorMatrix(...)`/`ColorFilter.lighting(...)`, removing the previous `graphicsLayer:colorFilterBlendMode` fallback for descriptor filters.
- Magic Jewel gained a `commands-graphics-layer-blend-color-matrix-filter` command-probe row.

Verification:

- Skiko focused interop tests passed:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- CMP `ui-graphics` desktop sources compile:
  - `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:compileKotlinDesktop`
- Magic Jewel compiled:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
- Magic Jewel script syntax checks passed:
  - `bash -n scripts/jbr-skia-interop-report.sh && bash -n scripts/jbr-skia-command-probe-suite.sh && bash -n scripts/assert-jbr-skia-command-window-screenshot.sh`

Known notes:

- Focused CMP recorder test execution still trips the broader `compose:ui` desktop compile issue resolving the Skiko JBR command-frame bridge package before the recorder test class runs.
- Native JBR test and live `commands-graphics-layer-blend-color-matrix-filter` validation should run after the local JBR native build is unblocked.

## Checkpoint: ABI 81 Expandable Command Capability Negotiation

Status: source and JVM-side validation passed; native JBR/live validation remains blocked on local Xcode license acceptance.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gate, and CMP command recorder now use command ABI 81.
- Added `getCommandCapabilities64High()` as a second 64-bit command capability word. It currently returns `0L`, but gives future render-effect/generic-shader commands a strict negotiated space now that ABI 80 consumed every bit of the low word.
- Skiko reads both low and high words reflectively and treats missing required bits in either word as `SKIKO_JBR_INTEROP_FALLBACK reason=command-capability-mismatch`.
- Skiko tests include a high-word override property, `skiko.jbr.interop.requiredCommandCapabilitiesHighForTest`, so future high-word capabilities can be tested before a concrete command lands.

Verification:

- Skiko focused interop tests passed:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- CMP `ui-graphics` desktop sources compile:
  - `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:compileKotlinDesktop`
- Magic Jewel compiled:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`

Known notes:

- This is a structural ABI checkpoint only; it intentionally adds no drawing command.
- Native JBR/live validation remains blocked on local Xcode license acceptance.

## Checkpoint: ABI 82 Blur Image-Filter Descriptor

Status: source and JVM-side validation passed for Skiko/CMP; native JBR/live validation remains blocked on local Xcode license acceptance.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gate, and CMP command recorder now use command ABI 82.
- Added high-word capability `COMMAND_CAP64_HIGH_SAVE_LAYER_IMAGE_FILTER_REF = 1`, descriptor type `COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER = 4`, and opcode `COMMAND_SAVE_LAYER_IMAGE_FILTER_REF = 55`.
- CMP serializes graphics-layer `BlurEffect` with no child effect into the generic effect-descriptor envelope as `[sigmaXBits, sigmaYBits, tileMode]`, then applies it through a saveLayer image-filter reference command.
- CMP still rejects nested/opaque render effects, or render effects combined with non-SrcOver blend/color-filter layer paints, instead of passing raw Skiko `SkImageFilter*` pointers across the ABI.
- JBR Java validation rejects malformed blur descriptors, non-finite/negative sigmas, invalid tile modes, undefined handles, bad dimensions, and bad alpha. Java2D fallback replay validates the scope; native Metal replay reconstructs `SkImageFilters::Blur(...)` inside JBR-owned Skia.
- Magic Jewel gained `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_RENDER_EFFECT` and a `commands-graphics-layer-render-effect` command-probe suite row for live validation once ABI 82 artifacts are refreshed.

Verification:

- Skiko focused interop tests passed:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- CMP `ui-graphics` desktop sources compile:
  - `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:compileKotlinDesktop`
- Magic Jewel compiled:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
- Magic Jewel script syntax checks passed:
  - `bash -n scripts/jbr-skia-interop-report.sh && bash -n scripts/jbr-skia-command-probe-suite.sh && bash -n scripts/assert-jbr-skia-command-window-screenshot.sh`

Known notes:

- This slice covers only simple graphics-layer blur effects. Offset effects, nested render-effect chains, runtime effects/SKSL, and combinations with layer blend/color-filter paints remain future descriptor/handle work.
- Native JBR test and live Magic Jewel blur-render-effect validation should run after the local JBR native build is unblocked.

## Checkpoint: ABI 83 Offset Image-Filter Descriptor

Status: source and JVM-side validation passed for Skiko/CMP; native JBR/live validation remains blocked on local Xcode license acceptance.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gate, and CMP command recorder now use command ABI 83.
- Added high-word capability `COMMAND_CAP64_HIGH_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER = 2` and descriptor type `COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER = 5`.
- CMP serializes graphics-layer `OffsetEffect` with no child effect into the generic effect-descriptor envelope as `[dxBits, dyBits]`, then reuses `COMMAND_SAVE_LAYER_IMAGE_FILTER_REF`.
- JBR Java validation rejects malformed offset descriptors and non-finite offsets. Java2D fallback replay validates the descriptor scope; native Metal replay reconstructs `SkImageFilters::Offset(...)` inside JBR-owned Skia.
- Magic Jewel now exposes `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_OFFSET_EFFECT` / `magic.jewel.compose.graphicsLayerOffsetEffect` and a `commands-graphics-layer-offset-effect` command-probe suite row for live validation once refreshed ABI 83 artifacts are available.

Verification:

- Skiko focused interop tests passed:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- CMP `ui-graphics` desktop sources compile:
  - `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:compileKotlinDesktop`
- Magic Jewel sample sources and scripts validate:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
  - `bash -n scripts/jbr-skia-interop-report.sh && bash -n scripts/jbr-skia-command-probe-suite.sh && bash -n scripts/assert-jbr-skia-command-window-screenshot.sh`

Known notes:

- This slice still rejects nested render-effect chains. The next render-effect step is a chain descriptor contract if we want `OffsetEffect(BlurEffect(...), ...)` and similar composed image filters on the fast path.

## Checkpoint: ABI 84 Chained Image-Filter Descriptors

Status: source and JVM-side validation passed for Skiko/CMP/Magic Jewel; native JBR/live validation remains blocked on local Xcode license acceptance.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gate, and CMP command recorder now use command ABI 84.
- Added high-word capability `COMMAND_CAP64_HIGH_EFFECT_DESCRIPTOR_CHAIN_IMAGE_FILTER = 4`.
- Added descriptor types `COMMAND_EFFECT_DESCRIPTOR_BLUR_IMAGE_FILTER_WITH_INPUT = 6` and `COMMAND_EFFECT_DESCRIPTOR_OFFSET_IMAGE_FILTER_WITH_INPUT = 7`.
- CMP now serializes simple render-effect chains by defining the child image-filter descriptor first, then defining the parent descriptor with the child handle in its payload.
- JBR Java/native replay snapshots the child descriptor at parent definition time, so later handle eviction cannot silently break a cached parent descriptor.
- Magic Jewel now exposes `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_CHAINED_RENDER_EFFECT` / `magic.jewel.compose.graphicsLayerChainedRenderEffect` and a `commands-graphics-layer-chained-render-effect` command-probe suite row.

Verification:

- Skiko focused interop tests passed:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- CMP `ui-graphics` desktop sources compile:
  - `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:compileKotlinDesktop`
- CMP command-recorder class tests pass when CMP resolves the locally published ABI 84 Skiko snapshot:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:publishToMavenLocal`
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Magic Jewel sample sources and scripts validate:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
  - `bash -n scripts/jbr-skia-interop-report.sh && bash -n scripts/jbr-skia-command-probe-suite.sh && bash -n scripts/assert-jbr-skia-command-window-screenshot.sh`

Known notes:

- This supports chains composed from the currently structured blur/offset descriptors. Arbitrary Skia image filters and runtime shader filters still need their own descriptor contract instead of raw Skia pointer sharing.
- Native JBR test and live validation should run after the local JBR native build is unblocked.

## Checkpoint: Composite Shader Metadata Prerequisite

Status: CMP source and focused recorder validation passed; this is a metadata prerequisite, not yet a JBR shader-descriptor ABI.

What changed:

- CMP now preserves JBR-compatible metadata for `CompositeShader(dst, src, blendMode)` when both child shaders already have structured JBR metadata.
- The metadata stores the child `Shader` wrappers and blend mode so the next ABI slice can serialize a shader descriptor tree without passing raw Skiko `SkShader*` pointers to JBR.
- Opaque shaders created through `SkShader.asComposeShader()` remain unsupported on the fast path and must continue to fall back until a real descriptor source exists.

Verification:

- CMP focused metadata test passed against the locally published Skiko snapshot:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.compositeShaderKeepsJbrSkiaMetadataForStructuredChildren`

Next:

- Add a shader descriptor-handle ABI for known shader families, then serialize composite shader trees by defining child shader descriptors before the parent blend descriptor.

## Checkpoint: ABI 85 Shader Descriptor Handles

Status: source implementation and focused JVM validation passed; live/native JBR validation still waits for the local Xcode license/build unblock.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gates, and CMP command recorder now use command ABI 85.
- The ABI adds a high-word shader-descriptor capability, `COMMAND_DEFINE_SHADER_DESCRIPTOR`, `COMMAND_EVICT_SHADER_HANDLE`, and `COMMAND_FILL_RECT_SHADER_REF`.
- JBR can reconstruct linear, radial, sweep, cached-image, and composite shader descriptors inside JBR-owned Skia, with `SrcOver` composition for structured `CompositeShader` trees.
- CMP serializes known shader metadata into scoped descriptor handles and emits shader-handle rectangle draws for structured composite shaders without passing raw Skiko `SkShader*` pointers.
- Magic Jewel has a `MAGIC_JEWEL_COMPOSE_COMPOSITE_SHADER=true` probe and command-suite case so the shader-descriptor path is visible in the sample and report automation.

Verification:

- CMP focused shader-descriptor test passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesCompositeShaderDescriptorRectInStrictMode`
- CMP command-recorder class tests passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Skiko interop tests passed with `abi=85`:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Skiko was republished locally for Magic Jewel/CMP smoke use:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:publishToMavenLocal`
- JBR public API compile smoke passed:
  - `javac -d /tmp/jbr-skia-api-compile src/java.desktop/share/classes/com/jetbrains/desktop/JBRSkia.java`
- Runtime API focused compile smoke passed:
  - `javac -d /tmp/jbr-runtime-api-skia-compile src/com/jetbrains/Service.java src/com/jetbrains/Provided.java src/com/jetbrains/JBRSkia.java`

Known notes:

- This is still a known-shader-family descriptor ABI. RuntimeEffect/SKSL and truly arbitrary shader sources remain open and non-negotiable for the end state.
- Full native JBR build/live validation is still blocked locally by the unaccepted Xcode license. The JBR service smoke compile also still depends on module-private JDK internals and is not a substitute for the real JBR build.

Next:

- Run Magic Jewel compile/script smoke for the new composite-shader probe, then collect live command-mode screenshots once a refreshed native JBR build is available.
- Start the RuntimeEffect/SKSL descriptor design/implementation branch after the known-shader descriptor path is stable.

## Checkpoint: ABI 86 RuntimeEffect Shader Descriptor MVP

Status: source implementation and focused JVM validation passed; native JBR build/live validation still waits for the local Xcode license/build unblock.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gates, and CMP command recorder now use command ABI 86.
- The shader descriptor schema adds `COMMAND_SHADER_DESCRIPTOR_RUNTIME_EFFECT` for a first generic shader MVP.
- CMP adds a desktop/skiko `RuntimeEffectShader(sksl, uniforms)` factory that keeps metadata alongside the normal Skiko shader so old rendering remains visually meaningful while command replay can serialize the descriptor.
- The descriptor payload carries ASCII SKSL source and raw float uniform bits. JBR validates payload shape, compiles the SKSL inside JBR-owned Skia with `SkRuntimeEffect::MakeForShader`, builds uniform `SkData`, and draws through the existing shader-handle rectangle command.
- Magic Jewel now has `MAGIC_JEWEL_COMPOSE_RUNTIME_EFFECT_SHADER=true` and a `commands-runtime-effect-shader` suite case for the animated SKSL probe.

Verification:

- CMP focused RuntimeEffect descriptor tests passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.runtimeEffectShaderKeepsJbrSkiaMetadata --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRuntimeEffectShaderDescriptorRectInStrictMode`
- Skiko interop tests passed with `abi=86`:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Skiko was republished locally:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:publishToMavenLocal`
- Magic Jewel compile and script syntax checks passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
  - `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-interop-report.sh`
- JBR public API and Runtime API compile smokes passed:
  - `javac -d /tmp/jbr-skia-api-compile src/java.desktop/share/classes/com/jetbrains/desktop/JBRSkia.java`
  - `javac -d /tmp/jbr-runtime-api-skia-compile src/com/jetbrains/Service.java src/com/jetbrains/Provided.java src/com/jetbrains/JBRSkia.java`

Known notes:

- ABI 86 intentionally supports only ASCII SKSL and positional raw float uniforms. Named uniforms, source hashing, child shader/color-filter handles, compile diagnostics, and stable compile-failure fallback markers remain open.
- This still needs a real native JBR build before claiming live rendering parity. The current machine remains blocked on Xcode license acceptance.

Next:

- Run the full CMP command-recorder suite and Magic Jewel command probe once refreshed JBR native artifacts exist.
- Extend RuntimeEffect descriptors with named uniforms and child shader handles so generic shaders can compose with the existing descriptor tree.

## Checkpoint: ABI 87 RuntimeEffect Child Shader Handles

Status: source implementation and focused JVM validation passed; native JBR build/live validation still waits for the local Xcode license/build unblock.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gates, and CMP command recorder now use command ABI 87.
- RuntimeEffect shader descriptor payloads now include a child shader count and child shader handle pairs before the SKSL source and uniform payload.
- CMP's `RuntimeEffectShader` factory accepts child Compose `Shader`s, keeps them in JBR metadata, defines child shader descriptors first, and then defines the RuntimeEffect descriptor by handle.
- JBR validation rejects missing child handles, excessive child counts, malformed payload lengths, and non-ASCII SKSL. Native replay reconstructs child shaders recursively and passes them to `SkRuntimeEffect::makeShader`.
- Magic Jewel's `MAGIC_JEWEL_COMPOSE_RUNTIME_EFFECT_SHADER=true` probe now uses a child linear-gradient shader so it exercises the child-handle descriptor graph.

Verification:

- CMP focused child RuntimeEffect tests passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRuntimeEffectShaderDescriptorWithChildShaderInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRuntimeEffectShaderDescriptorRectInStrictMode`
- CMP full command-recorder suite passed with ABI 87:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Skiko interop tests passed with `abi=87`:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- JBR public API and Runtime API compile smokes passed:
  - `javac -d /tmp/jbr-skia-api-compile src/java.desktop/share/classes/com/jetbrains/desktop/JBRSkia.java`
  - `javac -d /tmp/jbr-runtime-api-skia-compile src/com/jetbrains/Service.java src/com/jetbrains/Provided.java src/com/jetbrains/JBRSkia.java`

Known notes:

- Child shader handles are positional, matching `RuntimeEffect.makeShader(uniforms, children, matrix)`. Named builder-style child bindings remain future work.
- RuntimeEffect descriptors still need source hashing, named uniform schema validation, compile diagnostics, and stable compile-failure fallback markers.

## Checkpoint: ABI 88 RuntimeEffect Source Hash Validation

Status: source implementation and JVM validation passed; native JBR build/live validation still waits for the local Xcode license/build unblock.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gates, and CMP command recorder now use command ABI 88.
- RuntimeEffect shader descriptor payloads now include a 64-bit FNV-1a source hash after `skslLength`, `uniformFloatCount`, and `childCount`, before child shader handles, SKSL bytes, and uniform payload.
- CMP computes the hash from the ASCII SKSL source and writes it into every RuntimeEffect descriptor.
- JBR Java validation and native replay recompute the source hash from the descriptor SKSL bytes and reject mismatches before a RuntimeEffect is cached or compiled.
- JBR API coverage now includes a corrupted RuntimeEffect source-hash stream that must fail validation.

Verification:

- JBR public API and Runtime API compile smokes passed:
  - `javac -d /tmp/jbr-skia-api-compile src/java.desktop/share/classes/com/jetbrains/desktop/JBRSkia.java`
  - `javac -d /tmp/jbr-runtime-api-skia-compile src/com/jetbrains/Service.java src/com/jetbrains/Provided.java src/com/jetbrains/JBRSkia.java`
- Skiko interop tests passed with `abi=88`:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Skiko ABI 88 snapshot published locally:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:publishToMavenLocal`
- CMP focused RuntimeEffect descriptor tests passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRuntimeEffectShaderDescriptorRectInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRuntimeEffectShaderDescriptorWithChildShaderInStrictMode`
- CMP full command-recorder suite passed with ABI 88:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Magic Jewel compiles against the refreshed local ABI 88 Skiko/CMP stack:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`

Known notes:

- Source hashing is a descriptor integrity/diagnostic primitive; it is not a security boundary.
- RuntimeEffect descriptors still need named uniform schema validation, compile diagnostics, stable compile-failure fallback markers, and child color-filter handles.

## Checkpoint: RuntimeEffect Compile-Failure Marker

Status: marker plumbing and report parser validation passed; native live validation still waits for the local Xcode license/build unblock.

What changed:

- JBR native RuntimeEffect replay now emits a parseable marker when `SkRuntimeEffect::MakeForShader(...)` fails:
  - `JBR_SKIA_INTEROP_RUNTIME_EFFECT_COMPILE_FAILED hash=0x... skslLength=... uniforms=... children=... errorLength=...`
- The marker intentionally does not print raw Skia error text yet, so logs stay single-line and stable for report parsing. The source hash ties the failure back to the descriptor without dumping the full SKSL body.
- Magic Jewel reports now count the marker in `summary.properties` as `jbr_runtime_effect_compile_failures`.
- Magic Jewel validation recognizes `EXPECT_COMMAND_FALLBACK_REASON=runtime-effect-compile-failed` and requires both the JBR compile-failure marker and a rendered=false Skiko command frame.

Verification:

- JBR public API compile smoke passed:
  - `javac -d /tmp/jbr-skia-api-compile src/java.desktop/share/classes/com/jetbrains/desktop/JBRSkia.java`
- Magic Jewel report scripts passed syntax validation:
  - `bash -n scripts/jbr-skia-interop-report.sh scripts/test-jbr-skia-report-validation.sh`
- Magic Jewel report parser regression suite passed:
  - `bash scripts/test-jbr-skia-report-validation.sh`

Known notes:

- This is the first stable compile-failure marker. A future slice should add a controlled live invalid-RuntimeEffect probe and decide whether sanitized error codes/snippets are useful enough to include without making the marker brittle.

## Checkpoint: ABI 89 RuntimeEffect Named Uniform Schema

Status: schema implementation and JVM validation passed; native JBR build/live validation still waits for the local Xcode license/build unblock.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gates, and CMP command recorder now use command ABI 89.
- RuntimeEffect shader descriptor payloads now include a named-uniform schema count after `childCount`, followed by the source hash, child shader handles, named uniform entries, SKSL source bytes, and raw uniform float bits.
- CMP adds experimental `RuntimeEffectUniform(name, floatOffset, floatCount)` metadata to `RuntimeEffectShader(...)` and serializes validated ASCII identifier names plus float ranges into the descriptor.
- JBR Java validation and native replay validate schema count, identifier names, and overflow-safe uniform float ranges before accepting or compiling a RuntimeEffect descriptor.
- Magic Jewel's RuntimeEffect probe now declares the animated `phase` uniform schema, so the sample exercises the new descriptor metadata while still rendering through the old path when interop is unavailable.

Verification:

- JBR public API and Runtime API compile smokes passed:
  - `javac -d /tmp/jbr-skia-api-compile src/java.desktop/share/classes/com/jetbrains/desktop/JBRSkia.java`
  - `javac -d /tmp/jbr-runtime-api-skia-compile src/com/jetbrains/Service.java src/com/jetbrains/Provided.java src/com/jetbrains/JBRSkia.java`
- JBR API source coverage now includes a malformed RuntimeEffect uniform-schema stream that must fail validation. A standalone `javac` of the full test class is blocked outside the JBR module graph by existing internal JBR/Java2D package dependencies; the native/JTReg path still waits for the local Xcode license/build unblock.
- Skiko interop tests passed with `abi=89`:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Skiko ABI 89 snapshot published locally:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:publishToMavenLocal`
- CMP focused RuntimeEffect descriptor tests passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRuntimeEffectNamedUniformSchemaInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.rejectsInvalidRuntimeEffectUniformSchemaInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRuntimeEffectShaderDescriptorRectInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRuntimeEffectShaderDescriptorWithChildShaderInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.runtimeEffectShaderKeepsJbrSkiaMetadata`
- CMP full command-recorder suite passed with ABI 89:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Magic Jewel compiles against the refreshed local ABI 89 Skiko/CMP stack:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`

Known notes:

- ABI 89 validates named uniform metadata but still passes raw uniform bytes positionally to `SkRuntimeEffect::makeShader`. Builder-backed named uniform assignment remains the next correctness slice if we need Skia-side layout validation instead of schema diagnostics.
- Uniform names are intentionally restricted to simple ASCII SkSL identifiers for this first schema. Array/dotted names can be added later with explicit tests and an ABI bump.

## Checkpoint: ABI 90 RuntimeEffect Named Child Schema

Status: source implementation and JVM validation passed; native JBR build/live validation still waits for the local Xcode license/build unblock.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gates, and CMP command recorder now use command ABI 90.
- RuntimeEffect shader descriptor payloads now include a named-child schema count after the named-uniform count, followed by the source hash, child shader handles, named uniform entries, named child entries, SKSL source bytes, and raw uniform float bits.
- CMP adds experimental `RuntimeEffectChild(name, shader)` metadata to `RuntimeEffectShader(...)`, serializes named child entries as child-index/name pairs, and keeps the old positional `children` list for compatibility.
- JBR Java validation and native replay validate named child count, duplicate child indices, child index range, and child names before accepting a RuntimeEffect descriptor.
- JBR API source coverage now includes malformed RuntimeEffect uniform-schema and child-schema streams that must fail validation.
- JBR native replay can use `SkRuntimeEffectBuilder` when all child shaders are named and the named uniform schema covers the full uniform payload. This covers Magic Jewel's `phase` plus `content` RuntimeEffect probe without passing Skiko runtime objects across the ABI.
- Magic Jewel's RuntimeEffect probe now declares both `RuntimeEffectUniform("phase", 0, 1)` and `RuntimeEffectChild("content", child)`.

Verification:

- JBR public API and Runtime API compile smokes passed:
  - `javac -d /tmp/jbr-skia-api-compile src/java.desktop/share/classes/com/jetbrains/desktop/JBRSkia.java`
  - `javac -d /tmp/jbr-runtime-api-skia-compile src/com/jetbrains/Service.java src/com/jetbrains/Provided.java src/com/jetbrains/JBRSkia.java`
- Skiko interop tests passed with `abi=90`:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Skiko ABI 90 snapshot published locally:
  - `./gradlew --no-daemon --no-configuration-cache :skiko:publishToMavenLocal`
- CMP focused RuntimeEffect descriptor tests passed:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRuntimeEffectNamedChildSchemaInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.rejectsInvalidRuntimeEffectNamedChildSchemaInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRuntimeEffectNamedUniformSchemaInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRuntimeEffectShaderDescriptorWithChildShaderInStrictMode`
- CMP full command-recorder suite passed with ABI 90:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Magic Jewel compiles against the refreshed local ABI 90 Skiko/CMP stack:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`

Known notes:

- The builder path is deliberately conservative: it is used only when every child is named and named uniform coverage equals the full uniform payload. Partial schemas continue through the positional `makeShader` path after validation.
- Child color-filter handles and richer compile diagnostics remain open RuntimeEffect work.

## Checkpoint: RuntimeEffect Builder-Failure Marker

Status: marker plumbing and Magic Jewel report parser validation passed; native live validation still waits for the local Xcode license/build unblock.

What changed:

- JBR native RuntimeEffect replay now emits a parseable marker when the named-schema `SkRuntimeEffectBuilder` path cannot produce a shader:
  - `JBR_SKIA_INTEROP_RUNTIME_EFFECT_BUILD_FAILED hash=0x... skslLength=... uniforms=... children=... namedUniforms=... namedChildren=...`
- Magic Jewel adds `MAGIC_JEWEL_COMPOSE_RUNTIME_EFFECT_BAD_CHILD=true`, which keeps the Skiko fallback shader valid but intentionally sends a wrong JBR child name. That gives us a controlled builder-failure fallback probe without requiring invalid SKSL that would fail before JBR receives the descriptor.
- Magic Jewel reports now count the marker in `summary.properties` as `jbr_runtime_effect_build_failures`.
- Magic Jewel validation recognizes `EXPECT_COMMAND_FALLBACK_REASON=runtime-effect-build-failed` and requires both the JBR build-failure marker and a rendered=false Skiko command frame.
- The command probe suite includes `commands-runtime-effect-build-fallback`.

Verification:

- JBR public API compile smoke passed:
  - `javac -d /tmp/jbr-skia-api-compile src/java.desktop/share/classes/com/jetbrains/desktop/JBRSkia.java`
- Magic Jewel compiles:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
- Magic Jewel report scripts passed syntax validation and parser fixtures:
  - `bash -n scripts/jbr-skia-interop-report.sh scripts/jbr-skia-command-probe-suite.sh scripts/test-jbr-skia-report-validation.sh`
  - `bash scripts/test-jbr-skia-report-validation.sh`

Known notes:

- This is a JBR-builder-path failure probe, not an invalid-SKSL compile probe. Invalid SKSL still fails too early in the current CMP factory because old-path rendering constructs a normal Skiko RuntimeEffect first.

## Checkpoint: Magic Jewel RuntimeEffect Conformance Probes

Status: live command-mode validation passed for the refreshed ABI 90 artifact set.

What changed:

- Magic Jewel adds separate RuntimeEffect probe flags:
  - `MAGIC_JEWEL_COMPOSE_RUNTIME_EFFECT_PURE_COLOR=true`
  - `MAGIC_JEWEL_COMPOSE_RUNTIME_EFFECT_UNIFORM_ONLY=true`
  - `MAGIC_JEWEL_COMPOSE_RUNTIME_EFFECT_CHILD_ONLY=true`
  - existing combined `MAGIC_JEWEL_COMPOSE_RUNTIME_EFFECT_SHADER=true`
  - existing builder-fallback `MAGIC_JEWEL_COMPOSE_RUNTIME_EFFECT_BAD_CHILD=true`
- The command probe suite exposes matching cases: `commands-runtime-effect-pure-color`, `commands-runtime-effect-uniform-only`, `commands-runtime-effect-child-only`, `commands-runtime-effect-shader`, and `commands-runtime-effect-build-fallback`.
- This splits generic shader validation into smaller failure domains: source-only, uniform layout, named child composition, combined builder path, and builder fallback.

Verification:

- Magic Jewel compiles:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
- Magic Jewel report scripts passed syntax validation and parser fixtures:
  - `bash -n scripts/jbr-skia-interop-report.sh scripts/jbr-skia-command-probe-suite.sh scripts/test-jbr-skia-report-validation.sh`
  - `bash scripts/test-jbr-skia-report-validation.sh`
- JBR native replay now checks `SkRuntimeEffect::findChild(name)` before assigning through `SkRuntimeEffectBuilder::child(name)`, so malformed child schemas emit `JBR_SKIA_INTEROP_RUNTIME_EFFECT_BUILD_FAILED` instead of tripping Skia's debug assertion.
- JBR command rendering now propagates a native replay failure as `false` when the native bridge and destination texture are present, instead of hiding native failures behind Java2D command replay. Java2D command replay remains the fallback when the native bridge/surface is unavailable.
- Magic Jewel RuntimeEffect conformance subset passed:
  - command: `CASES="commands-runtime-effect-pure-color commands-runtime-effect-uniform-only commands-runtime-effect-child-only commands-runtime-effect-shader commands-runtime-effect-build-fallback" DURATION_SECONDS=6 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-050347/suite.tsv`.
  - zero-fallback native command rows passed for pure-color (`jbr_command_frames=970`), uniform-only (`405`), child-only (`432`), and combined child+uniform (`390`).
  - the intentional builder-fallback row passed with `fallback_new_count=1`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=0`, and the JBR build-failure marker present.

Known notes:

- These are launch/report probes, not yet screenshot parity assertions per individual RuntimeEffect variant. They provide stable switches for the next screenshot-parity expansion.
- The first live builder-fallback attempt exposed two correctness gaps: assigning an unknown named child into Skia's builder is fatal in debug builds, and Java-side command replay was masking native `false` results as successful frames.

## Checkpoint: Defensive Command-Frame Animation Preservation

Status: focused Skiko regression tests passed.

What changed:

- Skiko's command-frame cache now treats a `FullScene` command frame as cacheable only when it reaches the same minimum meaningful command-word threshold used for unknown frames.
- If a tiny `FullScene` frame arrives after a meaningful animated frame, Skiko replays the cached frame instead of replacing it. This avoids a transient blank or tiny command stream from freezing/flashing the JBR command renderer.
- The no-cache case still returns the tiny `FullScene` frame, so a genuinely empty first scene is not hidden forever.
- Focused Skiko tests cover both behaviors:
  - `commandFrameCacheDoesNotReplaceMeaningfulFrameWithMinimalFullSceneFrame`
  - `commandFrameCacheReturnsMinimalFullSceneFrameWhenNoMeaningfulFrameWasSeen`

Verification:

- `git diff --check` passed in the Skiko worktree.
- Focused Skiko tests passed:
  - `./gradlew --no-daemon --no-configuration-cache awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest.commandFrameCacheDoesNotReplaceMeaningfulFrameWithMinimalFullSceneFrame --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest.commandFrameCacheReturnsMinimalFullSceneFrameWhenNoMeaningfulFrameWasSeen --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest.commandFrameCacheReplaysLastMeaningfulFrameForMinimalInteropOnlyFrame`

Known notes:

- This is a defensive guard for transient repaint ordering. Magic Jewel now has a follow-up animation-preservation report case that asserts non-frozen command-mode frame markers advance and that the tiny-frame path is exercised.

## Checkpoint: Magic Jewel Live Animation Marker Assertion

Status: live command-mode validation passed after refreshing the ABI 90 public API shim, patched `java.desktop` classes, and native bridge dylib.

What changed:

- Magic Jewel reports accept `EXPECT_MIN_APP_NEW_FRAMES=N` in strict command mode.
- The validator counts `MAGIC_JEWEL_COMPOSE_FRAME` markers in the new-renderer sample window and fails if the count is below `N`.
- Skiko has a test-only `skiko.jbr.interop.forceTinyFullSceneOnceForTesting=true` switch that replaces the second meaningful `FullScene` command stream with a tiny header-only full-scene frame and logs `SKIKO_JBR_INTEROP_TINY_FULL_SCENE_INJECTED`.
- Magic Jewel forwards this via `SKIKO_FORCE_TINY_FULL_SCENE_ONCE_FOR_TEST=true`; reports expose `skiko_tiny_full_scene_injections` and can assert it with `EXPECT_MIN_TINY_FULL_SCENE_INJECTIONS=N`.
- The command-probe suite now includes `commands-live-animation`, which runs the normal non-frozen command renderer, requires at least five Compose frame markers, forces one tiny `FullScene` frame, and asserts the injection marker.
- The README documents the new assertion and keeps the distinction clear: screenshot parity intentionally freezes animation; ordinary report/probe runs validate liveness.

Verification:

- Magic Jewel report scripts passed syntax validation and parser fixtures:
  - `bash -n scripts/jbr-skia-interop-report.sh scripts/jbr-skia-command-probe-suite.sh scripts/test-jbr-skia-report-validation.sh`
  - `bash scripts/test-jbr-skia-report-validation.sh`
- Magic Jewel compiles with the new JVM property forwarding:
  - `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
- Skiko source whitespace validation passed:
  - `git diff --check`
- Focused Skiko command-frame cache tests passed:
  - `./gradlew --no-daemon --no-configuration-cache awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest.commandFrameCacheDoesNotReplaceMeaningfulFrameWithMinimalFullSceneFrame --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest.commandFrameCacheReturnsMinimalFullSceneFrameWhenNoMeaningfulFrameWasSeen --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest.commandFrameCacheReplaysLastMeaningfulFrameForMinimalInteropOnlyFrame`
- Refreshed local runtime artifacts for ABI 90:
  - Runtime API public shim rebuilt with `bash tools/build.sh dev "" out` and copied to `/tmp/jbr-api-shim.jar`.
  - JBR patched `java.desktop` classes rebuilt into `/tmp/jbr-skia-run/desktop`; the temporary `com.jetbrains.exported` compile stub was removed from the patch output to avoid a boot-layer split package.
  - JBR native bridge rebuilt into `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Magic Jewel live command probe passed:
  - command: `CASES=commands-live-animation DURATION_SECONDS=6 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - result: passed with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `skiko_command_frames=432`, `jbr_command_frames=432`, `app_new_frames=432`, `app_new_fps=72.0`, and `skiko_tiny_full_scene_injections=1`.
  - report: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-045238/commands-live-animation/report.md`.

Known notes:

- This assertion proves frame production and exercises the tiny-frame preservation branch. It does not yet compare visual pixel movement across two live phases.
- Two stale-artifact failures were useful:
  - stale public API shim produced `SKIKO_JBR_INTEROP_FALLBACK reason=abi-mismatch`;
  - refreshed public API with stale native/classes produced `reason=native-abi-mismatch`;
  - patched-class refresh must not leave the temporary `com.jetbrains.exported` stub under the `java.desktop` patch directory.

## Checkpoint: Full Command Probe Sweep With Nested Fallback Reasons

Status: full Magic Jewel command-probe sweep passed with refreshed ABI 90 artifacts.

What changed:

- Magic Jewel's screenshot classifier now distinguishes the cyan graphics-layer color-filter probe from the purple
  graphics-layer color-matrix probe.
- The purple classifier accepts the paler blended color-matrix output used by the graphics-layer blend/color-matrix row.
- Magic Jewel reports now parse `CMP_JBR_COMMAND_RECORDER_NESTED_UNSUPPORTED` alongside the top-level
  `CMP_JBR_COMMAND_RECORDER_FRAME` marker when summarizing unsupported command reasons.
- Strict fallback validation can match an expected unsupported reason from either top-level or nested recorder markers.
  This keeps intentionally unsupported nested command families, such as invalid sweep-gradient child commands, visible
  in machine-readable reports.
- Parser fixtures cover nested unsupported reasons so CI can assert the precise fallback reason instead of accepting a
  generic graphics-layer fallback.

Verification:

- Magic Jewel report scripts passed syntax validation and parser fixtures:
  - `bash -n scripts/jbr-skia-interop-report.sh scripts/jbr-skia-command-probe-suite.sh scripts/test-jbr-skia-report-validation.sh`
  - `bash scripts/test-jbr-skia-report-validation.sh`
- Targeted invalid-gradient fallback passed:
  - command: `CASES=commands-invalid-gradient-fallback DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - report: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-053330/commands-invalid-gradient-fallback/report.md`
  - result: `unsupported=sweepGradientStops:247,graphicsLayer:childCommands:247,graphicsLayer:247`, `jbr_picture_frames=246`, `jbr_command_frames=0`.
- Full Magic Jewel command-probe sweep passed:
  - command: `DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-053403/suite.tsv`
  - result: 39/39 rows passed.
  - zero-fallback command rows include live animation, mixed Swing popups/menus, text/images, image/composite shaders,
    RuntimeEffects, color filters, blend modes, graphics layers, render effects, and saveLayer filters.
  - the only intentional picture-path row is `commands-invalid-gradient-fallback`, which reports nested
    `sweepGradientStops` reasons and no JBR command frames.

Known notes:

- This is still a short per-row validation run. It proves command-path coverage and fallback labeling, not stable
  benchmark numbers.
- Some frame-rate values exceed display refresh because marker counts track renderer command submissions over the sample
  interval, not presented vsync frames.

## Checkpoint: Named Screenshot Parity Suite

Status: focused old/new screenshot parity rows passed for the rich baseline, RuntimeEffect descriptors, and graphics-layer effects.

What changed:

- Magic Jewel now has `scripts/jbr-skia-screenshot-parity-suite.sh`, a named-case wrapper around the window-only old/new
  parity script.
- Magic Jewel now has `MAGIC_JEWEL_SWING_ISLAND=false`, which hides the embedded SwingPanel island for focused
  geometry-only parity scenes.
- The suite writes `suite.tsv` with case status, whole-window average delta, whole-window bad-pixel ratio,
  Compose-canvas bad-pixel ratio, report path, and diff-image path.
- Initial named cases cover:
  - `parity-rich`
  - `parity-geometry-clean`
  - `parity-native-text`
  - `parity-runtime-effect-pure-color`
  - `parity-runtime-effect-uniform-only`
  - `parity-runtime-effect-child-only`
  - `parity-runtime-effect-shader`
  - `parity-graphics-layer-effects`
- The graphics-layer effects row disables unrelated baseline shape/gradient/blend probes so its strict screenshot oracle
  does not fail on a cyan counter partially occluded by effect-specific content. The rich baseline row still covers those
  baseline probes.
- Magic Jewel README documents the suite and `CASES=...` subset usage.

Verification:

- Script syntax passed:
  - `bash -n scripts/jbr-skia-screenshot-parity-suite.sh scripts/jbr-skia-screenshot-parity.sh`
- Rich baseline parity-suite row passed:
  - command: `CASES=parity-rich DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-055348/suite.tsv`
  - metrics: `avg_delta=1.646`, `bad_pixel_ratio=0.03910`, `compose_bad_pixel_ratio=0.06235`.
- Clean-geometry parity row passed:
  - command: `CASES=parity-geometry-clean DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-061323/suite.tsv`
  - metrics: `avg_delta=2.197`, `bad_pixel_ratio=0.05687`, `compose_bad_pixel_ratio=0.09083`.
  - report signals: no Swing progress frames, no image refs, `jbr_command_frames=298`.
- Native-text parity row passed with text-aware thresholds:
  - command: `CASES=parity-native-text DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-062651/suite.tsv`
  - metrics: `avg_delta=2.503`, `bad_pixel_ratio=0.04689`, `compose_bad_pixel_ratio=0.07407`.
  - report signals: `JBR_SKIA_NATIVE_TEXT=true`, no image refs, paragraph text commands present, `jbr_command_frames=454`.
- RuntimeEffect parity rows passed:
  - command: `CASES="parity-runtime-effect-pure-color parity-runtime-effect-uniform-only parity-runtime-effect-child-only" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-055435/suite.tsv`
  - metrics:
    - pure color: `avg_delta=1.632`, `bad_pixel_ratio=0.03871`, `compose_bad_pixel_ratio=0.06168`
    - uniform only: `avg_delta=1.633`, `bad_pixel_ratio=0.03874`, `compose_bad_pixel_ratio=0.06173`
    - child only: `avg_delta=1.642`, `bad_pixel_ratio=0.03899`, `compose_bad_pixel_ratio=0.06215`
- Combined RuntimeEffect parity row passed:
  - command: `CASES=parity-runtime-effect-shader DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-060341/suite.tsv`
  - metrics: `avg_delta=1.636`, `bad_pixel_ratio=0.03883`, `compose_bad_pixel_ratio=0.06188`.
- Graphics-layer effects parity row passed:
  - command: `CASES=parity-graphics-layer-effects DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-055842/suite.tsv`
  - metrics: `avg_delta=1.690`, `bad_pixel_ratio=0.03997`, `compose_bad_pixel_ratio=0.06380`.
- Default named screenshot parity suite passed end to end:
  - command: `DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-061514/suite.tsv`
  - rows: rich baseline, clean geometry, RuntimeEffect pure-color, RuntimeEffect uniform-only, RuntimeEffect child-only,
    and graphics-layer effects.
- Default named screenshot parity suite passed again with native-text included:
  - command: `DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-062821/suite.tsv`
  - rows: rich baseline, clean geometry, native text, RuntimeEffect pure-color, RuntimeEffect uniform-only,
    RuntimeEffect child-only, and graphics-layer effects.

Known notes:

- These parity rows use the same broad thresholds as the rich baseline. The next validation-hardening step is to split
  tighter region-specific thresholds for Compose/Jewel-owned regions from known Swing/text raster drift.
- Native text remains opt-in. Its parity row proves the command path renders and roughly matches layout, while preserving
  the known raster/font drift as a separate text-aware threshold rather than silently treating it as identical to
  text-as-image.
- The command-probe suite still remains the primary validation for the intentional RuntimeEffect build-fallback row.

## Checkpoint: Configurable Screenshot Region Gates

Status: initial per-region screenshot parity gates are wired and passing on the rich baseline parity case.

What changed:

- `scripts/compare-jbr-skia-window-screenshots.sh` now enforces separate bad-pixel-ratio limits for coarse ownership
  regions in addition to the full-window threshold.
- The comparator also emits metric-only Compose subregions for left backdrop, center animation, and bottom labels. These
  are not hard-gated yet because the current scene still mixes animated geometry and text/raster differences in those
  areas.
- Region thresholds are configurable with:
  - `MAX_HEADER_CONTROLS_BAD_PIXEL_RATIO`
  - `MAX_COMPOSE_CANVAS_BAD_PIXEL_RATIO`
  - `MAX_SWING_ISLAND_BAD_PIXEL_RATIO`
  - `MAX_RIGHT_PROBE_STRIP_BAD_PIXEL_RATIO`
- Default thresholds are intentionally conservative for the current mixed scene:
  - header controls: `0.04`
  - Compose canvas: `0.08`
  - Swing island: `0.03`
  - right probe strip: `0.05`
- The README documents these knobs so CI or local slices can tighten regions independently.

Verification:

- Script syntax passed:
  - `bash -n scripts/compare-jbr-skia-window-screenshots.sh scripts/jbr-skia-screenshot-parity.sh scripts/jbr-skia-screenshot-parity-suite.sh`
- Rich baseline parity-suite row passed with the new gates:
  - command: `CASES=parity-rich DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-060146/suite.tsv`
  - metrics: `avg_delta=1.646`, `bad_pixel_ratio=0.03910`, `compose_bad_pixel_ratio=0.06235`.
- Rich baseline parity-suite row also passed after adding metric-only subregions:
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-060531/suite.tsv`
  - subregion metrics: left backdrop `0.08741`, center animation `0.09997`, bottom labels `0.08375`, right probe strip `0.02867`.

Known notes:

- This is the threshold plumbing, not the final threshold policy. The current Compose-canvas region still includes text,
  Swing overlap, and broad animated content, so its default threshold remains looser than the desired final Compose/Jewel
  geometry gate.
- Next validation work should split smaller Compose-owned regions or mask text-heavy areas so the thresholds can become
  meaningfully tight without hiding known raster/font differences.

## Checkpoint: Local Artifact Rebuild Helper

Status: Magic Jewel can refresh the local patched JBR artifacts with one script, and the regenerated artifacts passed a
live command-mode smoke.

What changed:

- Added `scripts/rebuild-jbr-skia-local-artifacts.sh` in Magic Jewel.
- The helper:
  - rebuilds the public JBR API shim from `/Users/rock3r/src/jbr-api-skia-poc`;
  - copies it to `/tmp/jbr-api-shim.jar`;
  - compiles patched JBR `java.desktop` classes into `/tmp/jbr-skia-run/desktop`;
  - generates JNI headers under `/tmp/jbr-skia-native/generated`;
  - removes the temporary `com.jetbrains.exported` compile stub from the patch-module output to avoid boot-layer split
    packages;
  - links `/tmp/jbr-skia-native/libjbrskiainterop.dylib` against the Skia archive from the Skiko worktree.
- The script keeps path overrides for moved worktrees and non-default artifact locations.
- Magic Jewel README documents the helper and its override variables.

Verification:

- Script syntax passed:
  - `bash -n scripts/rebuild-jbr-skia-local-artifacts.sh`
- Artifact rebuild helper completed successfully:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
  - output artifacts: `/tmp/jbr-api-shim.jar`, `/tmp/jbr-skia-run/desktop`, `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
  - verified no `com.jetbrains.exported` class files remain under `/tmp/jbr-skia-run/desktop`.
- Post-rebuild command smoke passed:
  - command: `CASES=commands-live-animation DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-062207/suite.tsv`
  - result: `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=269`.

Known notes:

- This is still local patched-artifact wiring, not true JBR build-system integration.
- The helper is intentionally macOS arm64 only for the current PoC artifact path. x64/universal packaging belongs with the
  later production build integration.

## Checkpoint: Artifact Bundle Packaging

Status: Magic Jewel can package a local artifact set and feed it back into the launch-level artifact matrix as a required
old-artifact bundle.

What changed:

- Added `scripts/package-jbr-skia-artifact-bundle.sh` in Magic Jewel.
- The helper captures:
  - patched `java.desktop` output;
  - public JBR API shim jar;
  - native JBR Skia bridge dylib;
  - Skiko version;
  - CMP output root.
- The bundle writes a stable `manifest.properties` plus `use-as-old.env`.
- `scripts/jbr-skia-artifact-matrix.sh` now accepts `OLD_ARTIFACT_BUNDLE`, reads the manifest without sourcing arbitrary
  shell, and uses explicit `OLD_*` variables as overrides when both are present.
- Magic Jewel README documents the bundle helper, the replay command, and the same-version self-check mode.
- `ROADMAP.md` marks the bundle packaging helper and bundle-backed artifact matrix self-check complete.

Verification:

- Script syntax passed:
  - `bash -n scripts/package-jbr-skia-artifact-bundle.sh`
  - `bash -n scripts/jbr-skia-artifact-matrix.sh`
- Packaged the current local artifacts:
  - command: `./scripts/package-jbr-skia-artifact-bundle.sh`
  - bundle: `/Users/rock3r/src/magic-jewel/out/jbr-skia-artifact-bundles/20260501-063751`
  - manifest: `/Users/rock3r/src/magic-jewel/out/jbr-skia-artifact-bundles/20260501-063751/manifest.properties`
- Dry-run matrix with all optional rows required passed:
  - command: `OLD_ARTIFACT_BUNDLE=/Users/rock3r/src/magic-jewel/out/jbr-skia-artifact-bundles/20260501-063751 REQUIRE_OLD_ARTIFACT_ROWS=true ./scripts/jbr-skia-artifact-matrix.sh --dry-run`
  - matrix: `/Users/rock3r/src/magic-jewel/out/jbr-skia-artifact-matrix/20260501-063801/matrix.tsv`
- Real bundle-backed self-check passed, with the same-version bundle expected to stay on the fast path:
  - command: `OLD_ARTIFACT_BUNDLE=/Users/rock3r/src/magic-jewel/out/jbr-skia-artifact-bundles/20260501-063751 REQUIRE_OLD_ARTIFACT_ROWS=true OLD_JBR_EXPECTED_REASON=none OLD_API_EXPECTED_REASON=none OLD_SKIKO_EXPECTED_REASON=none OLD_CMP_EXPECTED_REASON=none DURATION_SECONDS=4 WARMUP_SECONDS=1 SAMPLE_INTERVAL_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-artifact-matrix.sh`
  - matrix: `/Users/rock3r/src/magic-jewel/out/jbr-skia-artifact-matrix/20260501-063815/matrix.tsv`
  - `current-all`: passed, `fallback_new_count=0`, `jbr_command_frames=598`
  - `missing-public-api`: passed, `fallback_new_count=1`, `jbr_command_frames=0`
  - all bundle-backed optional rows: passed, `fallback_new_count=0`

Known notes:

- This validates the packaging/replay mechanism using a same-version bundle. The final old/new compatibility matrix still
  needs real older incompatible JBR API/JBR native/Skiko/CMP bundles to prove the expected fallback reasons with historical
  artifacts.
- The bundle currently stores the CMP output as a manifest pointer by default; set `COPY_CMP_OUT=true` when a fully
  self-contained archive is needed.

## Checkpoint: Finer Geometry Screenshot Gates

Status: screenshot parity now exposes and gates smaller Compose-owned geometry/color regions so broad text/font and
anti-aliasing drift no longer has to carry the whole validation burden.

What changed:

- `scripts/compare-jbr-skia-window-screenshots.sh` now emits additional Compose subregions:
  - `composePurpleRect`
  - `composeTopProgress`
  - `composeBottomSwatches`
- These regions have opt-in bad-pixel gates:
  - `MAX_COMPOSE_PURPLE_RECT_BAD_PIXEL_RATIO`
  - `MAX_COMPOSE_TOP_PROGRESS_BAD_PIXEL_RATIO`
  - `MAX_COMPOSE_BOTTOM_SWATCHES_BAD_PIXEL_RATIO`
- `parity-geometry-clean` now enables those gates while continuing to keep broader canvas/Swing regions looser.
- `scripts/jbr-skia-screenshot-parity-suite.sh` writes the new subregion ratios into `suite.tsv` columns for CI/report
  consumers.
- Magic Jewel README documents the new region names and knobs.
- `ROADMAP.md` marks the text/font-vs-geometry parity split complete.

Verification:

- Script syntax passed:
  - `bash -n scripts/compare-jbr-skia-window-screenshots.sh`
  - `bash -n scripts/jbr-skia-screenshot-parity-suite.sh`
- Focused clean-geometry parity row passed:
  - command: `CASES=parity-geometry-clean DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-064534/suite.tsv`
  - metrics: `avg_delta=2.197`, `bad_pixel_ratio=0.05687`, `compose_bad_pixel_ratio=0.09083`,
    `compose_purple_rect_bad_pixel_ratio=0.07653`, `compose_top_progress_bad_pixel_ratio=0.09475`,
    `compose_bottom_swatches_bad_pixel_ratio=0.00000`.
- Rich parity row still passed with the additional metric output:
  - command: `CASES=parity-rich DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-064621/suite.tsv`
  - metrics: `avg_delta=1.646`, `bad_pixel_ratio=0.03910`, `compose_bad_pixel_ratio=0.06235`,
    `compose_bottom_swatches_bad_pixel_ratio=0.00000`.

Known notes:

- The purple rectangle and top progress regions still include anti-aliased diagonal stripe crossings, so their thresholds
  remain intentionally higher than the bottom swatch region.
- The bottom swatch region is the first near-exact color-ownership gate in the mixed screenshot suite. Future rows should
  add more such isolated regions instead of tightening the full Compose canvas too aggressively.

## Checkpoint: Descriptor Handle Lifecycle Markers

Status: JBR now emits structured effect/shader handle lifecycle markers from command streams before native submission, and
Magic Jewel can assert those markers from the full process log.

What changed:

- `JBRSkiaService` now scans command streams submitted through:
  - `renderCommandFrame(int[])`
  - `renderCommandBufferFrame(byte[])`
  - `renderCommandDirectFrame(ByteBuffer)`
- When it sees descriptor lifecycle records, it emits:
  - `JBR_SKIA_INTEROP_EFFECT_HANDLE_DEFINE backend=... contextId=0x... handle=0x... type=... version=... payloadInts=... legacy=...`
  - `JBR_SKIA_INTEROP_EFFECT_HANDLE_EVICT backend=... contextId=0x... handle=0x... removed=...`
  - `JBR_SKIA_INTEROP_SHADER_HANDLE_DEFINE backend=... contextId=0x... handle=0x... type=... version=... payloadInts=...`
  - `JBR_SKIA_INTEROP_SHADER_HANDLE_EVICT backend=... contextId=0x... handle=0x... removed=...`
- Marker emission happens before native submission, so the currently active Metal/native command path is observable. The
  Java2D fallback path also emits the same marker family while interpreting commands.
- Magic Jewel report summaries now include:
  - `jbr_effect_handle_define_frames`
  - `jbr_effect_handle_evict_frames`
  - `jbr_shader_handle_define_frames`
  - `jbr_shader_handle_evict_frames`
- Descriptor marker counts use the full new-renderer log rather than only the sampled measurement window, because handle
  defines commonly happen during warmup/first frame.
- The command-probe suite now asserts JBR-side descriptor define markers for the focused effect/shader descriptor cases.
- `ROADMAP.md` records this as descriptor-handle observability work; reuse, eviction, context migration, and old/new
  fallback probes remain open as separate lifecycle semantics.

Verification:

- Magic Jewel script syntax passed:
  - `bash -n scripts/jbr-skia-interop-report.sh`
  - `bash -n scripts/test-jbr-skia-report-validation.sh`
  - `bash -n scripts/jbr-skia-command-probe-suite.sh`
- Report parser tests passed:
  - command: `./scripts/test-jbr-skia-report-validation.sh`
  - result: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`
- Rebuilt local patched artifacts after the JBR change:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
  - output artifacts: `/tmp/jbr-api-shim.jar`, `/tmp/jbr-skia-run/desktop`, `/tmp/jbr-skia-native/libjbrskiainterop.dylib`.
- Focused descriptor marker smoke passed:
  - command: `CASES="commands-color-filter-handle commands-runtime-effect-pure-color" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-070251/suite.tsv`
  - `commands-color-filter-handle`: `jbr_effect_handle_define_frames=1`
  - `commands-runtime-effect-pure-color`: `jbr_shader_handle_define_frames=1`
- Broader descriptor subset passed:
  - command: `CASES="commands-image-shader commands-composite-shader commands-runtime-effect-shader commands-runtime-effect-uniform-only commands-runtime-effect-child-only commands-image-color-matrix-filter commands-color-matrix-filter commands-lighting-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-070506/suite.tsv`

Known notes:

- `commands-image-shader` still validates cached image refs, not shader descriptor handles, so it does not require a
  shader-handle marker.
- This slice improves lifecycle observability. It does not yet add explicit descriptor cache-hit, eviction, or
  context-migration invalidation probes.

## Checkpoint: Descriptor Eviction Probe

Status: Magic Jewel now has a command-mode stress row that forces both effect-handle and shader-handle eviction, and the
report validator asserts the JBR-side lifecycle markers.

What changed:

- Added `MAGIC_JEWEL_COMPOSE_DESCRIPTOR_EVICTION=true` / `magic.jewel.compose.descriptorEviction`.
- The probe draws `1032` unique tint color filters while `MAGIC_JEWEL_COMPOSE_COLOR_FILTER_HANDLE=true` is active, which
  overfills CMP's `MAX_DEFINED_COLOR_FILTER_HANDLES = 1024` LRU cache.
- The same probe draws `1032` unique `CompositeShader` trees. This deliberately uses composite shader descriptors rather
  than plain linear gradients because the recorder has dedicated gradient commands; composite shader trees route through
  the generic shader descriptor-handle path.
- Added `commands-descriptor-eviction` to the command probe suite with strict expectations for:
  - at least `1024` JBR effect-handle define markers
  - at least one JBR effect-handle evict marker
  - at least `1024` JBR shader-handle define markers
  - at least one JBR shader-handle evict marker
- `README.md` documents the new probe and its reason for using composite shader churn.
- `ROADMAP.md` marks descriptor eviction marker coverage complete.

Verification:

- Magic Jewel script syntax passed:
  - `bash -n scripts/jbr-skia-interop-report.sh scripts/jbr-skia-command-probe-suite.sh`
- Magic Jewel Kotlin compile passed:
  - command: `./gradlew compileKotlin`
- Focused descriptor eviction row passed:
  - command: `CASES="commands-descriptor-eviction" DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-072013/suite.tsv`
  - `jbr_effect_handle_define_frames=56760`
  - `jbr_effect_handle_evict_frames=55736`
  - `jbr_shader_handle_define_frames=170280`
  - `jbr_shader_handle_evict_frames=169256`

Known notes:

- This row is intentionally heavy and produced `14` command frames in a three-second run. It is a lifecycle stress probe,
  not a representative FPS benchmark.
- Earlier plain-linear-gradient churn produced no shader descriptor markers because those draws can use dedicated gradient
  commands; this is now captured in the probe design.

## Checkpoint: Stable Descriptor Reuse Gates

Status: Magic Jewel can now fail command-mode probes when stable descriptors are redefined too often, not only when
descriptor defines are missing.

What changed:

- Added strict report validator knobs:
  - `EXPECT_MAX_JBR_EFFECT_HANDLE_DEFINES`
  - `EXPECT_MAX_JBR_SHADER_HANDLE_DEFINES`
- Added parser/validator fixtures proving those max gates pass at the limit and fail above it.
- Applied max-count reuse gates to stable descriptor cases:
  - `commands-color-filter-handle`: exactly one effect define
  - `commands-color-matrix-filter`: exactly one effect define
  - `commands-lighting-filter`: exactly one effect define
  - `commands-image-color-matrix-filter`: exactly one effect define
  - `commands-runtime-effect-pure-color`: exactly one shader define
  - `commands-composite-shader`: exactly three shader defines for dst, src, and composite handles
- Left animated/runtime-uniform rows without max gates because their descriptor payload can intentionally change over time.
- `README.md` documents the stable descriptor reuse gates.
- `ROADMAP.md` marks stable descriptor reuse validation complete.

Verification:

- Magic Jewel script syntax passed:
  - `bash -n scripts/jbr-skia-interop-report.sh scripts/test-jbr-skia-report-validation.sh scripts/jbr-skia-command-probe-suite.sh`
- Report parser tests passed:
  - command: `./scripts/test-jbr-skia-report-validation.sh`
  - result: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`
- Focused stable descriptor suite passed:
  - command: `CASES="commands-color-filter-handle commands-color-matrix-filter commands-lighting-filter commands-image-color-matrix-filter commands-runtime-effect-pure-color commands-composite-shader" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-072500/suite.tsv`
  - observed define counts:
    - `commands-color-filter-handle`: `jbr_effect_handle_define_frames=1`
    - `commands-color-matrix-filter`: `jbr_effect_handle_define_frames=1`
    - `commands-lighting-filter`: `jbr_effect_handle_define_frames=1`
    - `commands-image-color-matrix-filter`: `jbr_effect_handle_define_frames=1`
    - `commands-runtime-effect-pure-color`: `jbr_shader_handle_define_frames=1`
    - `commands-composite-shader`: `jbr_shader_handle_define_frames=3`

Known notes:

- These gates validate recorder/JBR lifecycle stability through emitted markers. They do not yet expose explicit cache-hit
  markers; cache hits are inferred from the absence of additional define markers across many command frames.

## Checkpoint: Surface-Change Command Cache Invalidation

Status: Skiko now clears CMP-owned JBR command caches when the JBR destination surface changes, and Magic Jewel has a
resize probe that proves descriptors are redefined for the replacement surface.

What changed:

- CMP `JbrSkiaCommandRecorder` now exposes `clearInteropCachesForSurfaceChange()` as a JVM-callable hook.
- The hook clears the recorder's image-key, color-filter-handle, and shader-handle caches.
- Existing test-only cache clearing now delegates to the same implementation.
- Added a CMP unit test proving a stable tint color-filter handle is reused across frames until
  `clearInteropCachesForSurfaceChange()` is called, after which the descriptor define record is emitted again.
- Skiko `JbrSkiaSwingLayer` now calls that CMP hook reflectively when `SurfaceIdentityChange.surfaceChanged` is true.
  The reflection avoids a hard dependency from Skiko back to Compose UI.
- Skiko emits:
  - `SKIKO_JBR_INTEROP_COMMAND_CACHES_CLEARED reason=surfaceChanged`
  - or `SKIKO_JBR_INTEROP_COMMAND_CACHES_CLEARED reason=contextChanged`
- If the CMP hook is absent, Skiko emits `SKIKO_JBR_INTEROP_COMMAND_CACHES_CLEAR_UNAVAILABLE reason=... error=...`.
- Magic Jewel reports `skiko_command_cache_clear_markers` and can require them with
  `EXPECT_MIN_COMMAND_CACHE_CLEARS`.
- Added `commands-resize-descriptor-redefine`, which enables a stable descriptor-handle color-filter probe and auto
  resize. It requires:
  - one same-context surface-change marker
  - one command-cache clear marker
  - at least two JBR effect-handle define markers, proving the stable descriptor was redefined after resize
- `README.md` documents the cache-clear marker and resize descriptor probe.
- `ROADMAP.md` marks surface-change command cache invalidation complete.

Verification:

- Skiko AWT compile passed:
  - command: `./gradlew compileKotlinAwt`
- Skiko patched AWT artifact was republished to Maven Local for the consumed `0.0.0-SNAPSHOT` coordinate:
  - command: `./gradlew publishAwtPublicationToMavenLocal`
- CMP changed source set compiled:
  - command: `./gradlew :compose:ui:ui-graphics:compileKotlinDesktop`
- CMP targeted desktop test attempt was blocked by existing wider `compose:ui:ui` unresolved `org.jetbrains.skiko.jbr`
  symbols in this worktree, after `ui-graphics` itself compiled. The new unit test remains in tree for the next full CMP
  test pass once that wiring is restored.
- Magic Jewel report parser tests passed:
  - command: `./scripts/test-jbr-skia-report-validation.sh`
  - result: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`
- Focused live resize descriptor row passed:
  - command: `CASES="commands-resize-descriptor-redefine" DURATION_SECONDS=7 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-073716/suite.tsv`
  - `jbr_effect_handle_define_frames=2`
  - `skiko_surface_change_markers=1`
  - `skiko_command_cache_clear_markers=1`

Known notes:

- This hook is intentionally reflective. The following checkpoint tightens the missing-hook path into a structured
  compatibility fallback.

## Checkpoint: Command Cache Clear Compatibility Fallback

Status: a missing CMP command-cache clear hook is now a structured compatibility fallback instead of a silent
stale-descriptor risk.

What changed:

- Added Skiko fallback reason:
  - `command-cache-clear-unavailable`
- `JbrSkiaSwingLayer` now treats `clearInteropCachesForSurfaceChange()` failure as a command-mode fallback for the current
  paint after a JBR surface change.
- The successful path still emits:
  - `SKIKO_JBR_INTEROP_COMMAND_CACHES_CLEARED reason=...`
- The unavailable path emits:
  - `SKIKO_JBR_INTEROP_COMMAND_CACHES_CLEAR_UNAVAILABLE reason=... error=...`
  - `SKIKO_JBR_INTEROP_FALLBACK reason=command-cache-clear-unavailable`
- Magic Jewel's report validator accepts `command-cache-clear-unavailable` as a structured command fallback reason.
- Added a report parser fixture for the new fallback reason.
- Skiko `JBR-INTEROP.md` and Magic Jewel `README.md` document the behavior.
- `ROADMAP.md` marks this compatibility fallback complete.

Verification:

- Skiko AWT compile passed:
  - command: `./gradlew compileKotlinAwt`
- Magic Jewel script syntax and report parser tests passed:
  - command: `bash -n scripts/jbr-skia-interop-report.sh scripts/test-jbr-skia-report-validation.sh && ./scripts/test-jbr-skia-report-validation.sh`
  - result: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`

## Checkpoint: Combined Descriptor Lifecycle Smoke

Status: the core descriptor lifecycle rows pass together against the current local JBR/CMP/Skiko/Magic Jewel worktrees.

Verification:

- Republished the patched Skiko AWT artifact to the local `0.0.0-SNAPSHOT` coordinate:
  - command: `./gradlew publishAwtPublicationToMavenLocal`
- Combined Magic Jewel lifecycle suite passed:
  - command: `CASES="commands-color-filter-handle commands-runtime-effect-pure-color commands-composite-shader commands-descriptor-eviction commands-resize-descriptor-redefine" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-074549/suite.tsv`
  - rows:
    - `commands-color-filter-handle`: passed, `fallback_new_count=0`, `unsupported=none`, `jbr_command_frames=165`
    - `commands-runtime-effect-pure-color`: passed, `fallback_new_count=0`, `unsupported=none`, `jbr_command_frames=282`
    - `commands-composite-shader`: passed, `fallback_new_count=0`, `unsupported=none`, `jbr_command_frames=377`
    - `commands-descriptor-eviction`: passed, `fallback_new_count=0`, `unsupported=none`, `jbr_command_frames=26`
    - `commands-resize-descriptor-redefine`: passed, `fallback_new_count=0`, `unsupported=none`, `jbr_command_frames=568`

Known notes:

- The eviction row is intentionally heavy; the low frame count is expected and should not be interpreted as a normal
  rendering performance benchmark.

## Checkpoint: RuntimeEffect Build-Failure Diagnostics

Status: JBR RuntimeEffect builder fallback markers now identify the failing builder stage without dumping raw SKSL or Skia
error text into logs.

What changed:

- JBR native RuntimeEffect replay now hashes diagnostic details for stable one-line markers:
  - compile failures include `errorHash=0x...`
  - builder failures include `stage=missing-child|uniform-set|make-shader`
  - child/uniform assignment failures include `nameHash=0x...`
- The bad-child Magic Jewel probe now requires `EXPECT_RUNTIME_EFFECT_BUILD_FAILURE_STAGE=missing-child`, so the report
  proves that the intended JBR builder path failed gracefully instead of falling through some unrelated fallback.
- Magic Jewel parser fixtures and README marker documentation were updated for the richer RuntimeEffect diagnostics.
- `ROADMAP.md` records the diagnostic slice and keeps remaining RuntimeEffect work focused on child color-filter handles
  and other shader-family fallback markers.

Verification:

- Magic Jewel script syntax passed:
  - command: `bash -n scripts/jbr-skia-interop-report.sh scripts/test-jbr-skia-report-validation.sh scripts/jbr-skia-command-probe-suite.sh`
- Magic Jewel report parser tests passed:
  - command: `./scripts/test-jbr-skia-report-validation.sh`
  - result: `JBR_SKIA_REPORT_VALIDATION_TESTS passed`
- Focused RuntimeEffect build-fallback diagnostic row passed:
  - command: `CASES="commands-runtime-effect-build-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-075650/suite.tsv`
  - marker sample: `JBR_SKIA_INTEROP_RUNTIME_EFFECT_BUILD_FAILED hash=0x6836c43e20a229b7 stage=missing-child nameHash=0xd7c56a11bd76ad10 skslLength=304 uniforms=1 children=1 namedUniforms=1 namedChildren=1`

Next checkpoint:

- Decide whether the next RuntimeEffect slice is child color-filter handles or broader screenshot parity for the
  RuntimeEffect conformance rows.

## Checkpoint: RuntimeEffect Parity Default Coverage

Status: the Magic Jewel screenshot parity suite now includes the combined child+uniform RuntimeEffect row in its default
RuntimeEffect coverage, and the full focused RuntimeEffect parity subset passed.

What changed:

- `jbr-skia-screenshot-parity-suite.sh` default cases now include `parity-runtime-effect-shader` alongside:
  - `parity-runtime-effect-pure-color`
  - `parity-runtime-effect-uniform-only`
  - `parity-runtime-effect-child-only`
- Magic Jewel `README.md` documents that RuntimeEffect screenshot parity covers pure-color, uniform-only, child-only, and
  combined child+uniform rows.
- `ROADMAP.md` records the new default parity coverage.

Verification:

- Magic Jewel screenshot parity script syntax passed:
  - command: `bash -n scripts/jbr-skia-screenshot-parity-suite.sh scripts/jbr-skia-screenshot-parity.sh`
- Focused RuntimeEffect screenshot parity subset passed:
  - command: `CASES="parity-runtime-effect-pure-color parity-runtime-effect-uniform-only parity-runtime-effect-child-only parity-runtime-effect-shader" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-080056/suite.tsv`
  - observed Compose-canvas bad-pixel ratios:
    - `parity-runtime-effect-pure-color`: `0.06168`
    - `parity-runtime-effect-uniform-only`: `0.06173`
    - `parity-runtime-effect-child-only`: `0.06215`
    - `parity-runtime-effect-shader`: `0.06188`

Next checkpoint:

- Continue RuntimeEffect functionality with child color-filter handles, or run the full default screenshot parity suite
  after the next artifact refresh.

## Checkpoint: Skiko RuntimeEffect ColorFilter Prerequisite

Status: Skiko now exposes the missing wrapper needed for RuntimeEffect-backed color filters, which is a prerequisite for
serializing RuntimeEffect color-filter descriptors through CMP and JBR.

What changed:

- Added `RuntimeEffect.makeColorFilter(uniforms: Data?): ColorFilter` to Skiko common API.
- Added JVM and native/JS C++ bindings to call `SkRuntimeEffect::makeColorFilter(...)`.
- Extended the existing Skiko `RuntimeEffectTest` color-filter smoke to instantiate a real `ColorFilter`, not just compile
  the RuntimeEffect.
- Republished the patched Skiko AWT artifact to Maven Local for downstream local experiments.
- `ROADMAP.md` records this as the Skiko prerequisite before CMP/JBR RuntimeEffect color-filter descriptor work.

Verification:

- Skiko JVM/AWT Kotlin compilation passed:
  - command: `./gradlew compileKotlinJvm compileKotlinAwt`
- Skiko focused JVM test attempt:
  - command: `./gradlew jvmTest --tests org.jetbrains.skia.RuntimeEffectTest`
  - result: Gradle completed successfully, but only the `import-generator` JVM test task was selected by this build layout.
- Skiko root `test` task is not present in this checkout:
  - command: `./gradlew test --tests org.jetbrains.skia.RuntimeEffectTest`
  - result: failed with `Task 'test' not found in root project 'skiko' and its subprojects.`
- Skiko AWT publication passed:
  - command: `./gradlew publishAwtPublicationToMavenLocal`

Next checkpoint:

- Add CMP metadata/factory support for RuntimeEffect color filters, then decide the JBR descriptor schema for replaying
  those filters through the existing typed effect-handle path.

## Checkpoint: CMP RuntimeEffect ColorFilter Metadata

Status: CMP can now create RuntimeEffect-backed color filters on the Skiko desktop path while retaining the SKSL/uniform
metadata needed for a future JBR-owned effect descriptor.

What changed:

- Added `RuntimeEffectColorFilter(...)` to CMP desktop/skiko graphics.
- The factory compiles the normal Skiko `RuntimeEffect.makeForColorFilter(...)` path and calls the newly-added
  Skiko `RuntimeEffect.makeColorFilter(...)`, so old rendering remains meaningful.
- CMP stores JBR metadata beside the color filter:
  - ASCII SKSL source
  - copied float uniform payload
  - named uniform schema using the same `RuntimeEffectUniform` model as RuntimeEffect shaders
- Shared the existing float-uniform `SkData` packing helper between RuntimeEffect shader and color-filter factories.
- Added a focused CMP desktop test proving the factory preserves JBR metadata.

Verification:

- CMP ui-graphics desktop compile passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:compileKotlinDesktop`
- Initial focused CMP test caught a native artifact mismatch:
  - failure: `UnsatisfiedLinkError: 'long org.jetbrains.skia.RuntimeEffectKt._nMakeColorFilter(long, long)'`
- Rebuilt and published Skiko macOS arm64 runtime artifact with the new JNI symbol:
  - command: `./gradlew linkJvmBindingsMacosArm64 skikoJvmRuntimeJarMacosArm64 publishSkikoJvmRuntimeMacosArm64PublicationToMavenLocal publishAwtPublicationToMavenLocal`
- Focused CMP metadata test passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.runtimeEffectColorFilterKeepsJbrSkiaMetadata`

Next checkpoint:

- Add a typed JBR effect descriptor for RuntimeEffect color filters and wire CMP command recording to define/use that
  handle in saveLayer/image/solid-paint paths.

## Checkpoint: ABI 91 RuntimeEffect ColorFilter Descriptor

Status: RuntimeEffect-backed Compose color filters can now render through the typed JBR effect-descriptor path without
passing Skiko-owned `SkColorFilter*` or `SkRuntimeEffect*` objects across the ABI.

What changed:

- JBR private API, public Runtime API, Skiko compatibility gates, and CMP command recording now use command ABI 91.
- The public Runtime API shim exposes `COMMAND_CAP64_HIGH_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER` and
  `COMMAND_EFFECT_DESCRIPTOR_RUNTIME_COLOR_FILTER`, so Skiko can require the feature explicitly before using the fast
  path.
- CMP serializes RuntimeEffect color-filter metadata into a typed effect descriptor:
  - ASCII SKSL source
  - 64-bit source hash
  - raw float uniform bits
  - named uniform schema entries
- JBR Java validation and native replay validate the descriptor payload shape, source hash, ASCII source, and named
  uniform schema before accepting or compiling it.
- JBR native replay compiles the color filter with `SkRuntimeEffect::MakeForColorFilter(...)` and
  `SkRuntimeEffect::makeColorFilter(...)` inside JBR-owned Skia, then applies the resulting filter through the existing
  descriptor-handle fill path.
- Magic Jewel adds `MAGIC_JEWEL_COMPOSE_RUNTIME_EFFECT_COLOR_FILTER=true` plus a
  `commands-runtime-effect-color-filter` command-probe row.
- `ROADMAP.md` records ABI 91 and keeps child color-filter handles plus remaining shader-family fallback markers as
  follow-up RuntimeEffect work.

Verification:

- Skiko AWT publication after ABI 91 compatibility update passed:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt publishAwtPublicationToMavenLocal`
- JBR local artifact refresh passed after public Runtime API/JBR/native changes:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
- Rebuilt public API shim reports ABI 91 and the new descriptor constants:
  - command: `javap -classpath /tmp/jbr-api-shim.jar com.jetbrains.JBRSkia | rg "RUNTIME_COLOR|ABI_ID|HIGH_EFFECT"`
- CMP focused recorder/metadata tests passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRuntimeEffectColorFilterDescriptorRectInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.runtimeEffectColorFilterKeepsJbrSkiaMetadata`
- Focused Magic Jewel RuntimeEffect color-filter row passed with no fallback:
  - command: `CASES="commands-runtime-effect-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-083405/suite.tsv`
  - result: `status=passed fallback_new_count=0 unsupported=none jbr_command_frames=168`
- Broader descriptor regression subset passed with no fallback across RuntimeEffect shader/color-filter rows, image
  color-matrix, typed color filters, and graphics-layer color-filter rows:
  - command: `CASES="commands-runtime-effect-shader commands-runtime-effect-pure-color commands-runtime-effect-uniform-only commands-runtime-effect-child-only commands-runtime-effect-color-filter commands-image-color-matrix-filter commands-color-filter-handle commands-color-matrix-filter commands-lighting-filter commands-graphics-layer-color-filter commands-graphics-layer-color-matrix-filter commands-graphics-layer-blend-color-filter commands-graphics-layer-blend-color-matrix-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-083644/suite.tsv`

Next checkpoint:

- Commit the five-repo ABI 91 slice, then continue with child color-filter handles or screenshot parity for the new
  RuntimeEffect color-filter probe.

## Checkpoint: RuntimeEffect ColorFilter Screenshot Parity

Status: the Magic Jewel screenshot parity suite now has a dedicated RuntimeEffect color-filter row, and that focused row
passed against the ABI 91 artifact set.

What changed:

- Added `parity-runtime-effect-color-filter` to the screenshot parity suite.
- The default parity suite now includes the RuntimeEffect color-filter row alongside pure-color, uniform-only,
  child-only, and combined child+uniform RuntimeEffect rows.
- Magic Jewel `README.md` documents the expanded RuntimeEffect parity coverage.
- `ROADMAP.md` records the focused parity run and report path.

Verification:

- Magic Jewel screenshot parity script syntax passed:
  - command: `bash -n scripts/jbr-skia-screenshot-parity-suite.sh scripts/jbr-skia-screenshot-parity.sh`
- Focused RuntimeEffect color-filter screenshot parity row passed:
  - command: `CASES="parity-runtime-effect-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-084511/suite.tsv`
  - observed ratios: `bad_pixel_ratio=0.03905`, `compose_bad_pixel_ratio=0.06225`

Next checkpoint:

- Continue RuntimeEffect completeness with child color-filter handle design/probing, or run the full default screenshot
  parity suite with the ABI 91 artifact set if visual confidence becomes the priority.

## Checkpoint: Skiko RuntimeEffect ColorFilter Children Prerequisite

Status: Skiko can now create RuntimeEffect-backed color filters with child color filters through the direct
`RuntimeEffect.makeColorFilter(...)` API. This unblocks the ABI 92 descriptor work for child color-filter handles without
requiring CMP to use Skiko-owned `SkColorFilter*` pointers on the JBR fast path.

What changed:

- Extended `RuntimeEffect.makeColorFilter` to accept optional `Array<ColorFilter?>` children.
- Added JVM and native/JS C++ bindings for `SkRuntimeEffect::makeColorFilter(uniforms, children, childCount)`.
- Added a Skiko `RuntimeEffectTest` smoke that compiles a `uniform colorFilter child` effect and creates the child-backed
  color filter.
- Republished the patched Skiko AWT and macOS arm64 runtime artifacts to Maven Local.
- `ROADMAP.md` records this as the Skiko prerequisite for the next command-stream ABI slice.

Verification:

- Skiko JVM/AWT Kotlin compilation passed:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinJvm compileKotlinAwt`
- Skiko native/JVM macOS arm64 runtime and AWT publication passed:
  - command: `./gradlew --no-daemon --no-configuration-cache linkJvmBindingsMacosArm64 skikoJvmRuntimeJarMacosArm64 publishSkikoJvmRuntimeMacosArm64PublicationToMavenLocal publishAwtPublicationToMavenLocal`

Next checkpoint:

- Add ABI 92 child color-filter descriptor handles across Runtime API, JBR, CMP, Skiko compatibility gates, and Magic
  Jewel probes.

## Checkpoint: ABI 92 RuntimeEffect ColorFilter Child Handles

Status: RuntimeEffect-backed Compose color filters can now carry child color-filter handles through the typed descriptor
path, letting JBR reconstruct a child-backed `SkRuntimeEffect` color filter inside the destination Skia runtime without
sharing Skiko-owned `SkColorFilter*` pointers.

What changed:

- Bumped the JBR private API, public Runtime API mirror, Skiko compatibility gate, and CMP command stream to ABI 92.
- CMP's desktop/skiko `RuntimeEffectColorFilter(...)` factory now accepts positional child color filters and named child
  metadata via `RuntimeEffectColorFilterChild(name, colorFilter)`, keeps those children alive for the old Skiko path, and
  serializes child descriptor handles for the JBR command path.
- The RuntimeEffect color-filter descriptor payload now includes child-handle pairs and named child schema entries before
  the SKSL/uniform payload, matching the shader RuntimeEffect descriptor shape while staying type-safe for color filters.
- JBR Java validation rejects malformed child counts, missing child descriptors, image-filter descriptors used as color
  filter children, and malformed named child schema metadata.
- JBR native replay resolves child color-filter descriptors recursively, compiles the color-filter RuntimeEffect inside
  JBR-owned Skia, and calls `SkRuntimeEffect::makeColorFilter(uniformData, children, childCount)`.
- Magic Jewel adds `MAGIC_JEWEL_COMPOSE_RUNTIME_EFFECT_COLOR_FILTER_CHILD` and a
  `commands-runtime-effect-color-filter-child` command-suite row that asserts descriptor-handle creation without
  picture fallback.
- `ROADMAP.md` records ABI 92 and the focused live command-probe report path.

Verification:

- CMP focused recorder tests passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRuntimeEffectColorFilterDescriptorRectInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRuntimeEffectColorFilterDescriptorWithNamedChildInStrictMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.runtimeEffectColorFilterKeepsJbrSkiaMetadata`
- JBR local API/native artifact rebuild passed:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
- Skiko AWT publication after ABI 92 compatibility update passed:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt publishAwtPublicationToMavenLocal`
- Magic Jewel Kotlin compile passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache compileKotlin`
- Focused Magic Jewel RuntimeEffect color-filter child row passed with no fallback:
  - command: `CASES="commands-runtime-effect-color-filter-child" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-090456/suite.tsv`
  - result: `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=186`
- Broader ABI 92 descriptor regression subset passed with no fallback:
  - command: `CASES="commands-runtime-effect-shader commands-runtime-effect-color-filter commands-runtime-effect-color-filter-child commands-image-color-matrix-filter commands-color-filter-handle commands-color-matrix-filter commands-lighting-filter commands-graphics-layer-color-filter commands-graphics-layer-color-matrix-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-090711/suite.tsv`
  - result: `9/9` rows passed with `fallback_new_count=0`, `unsupported=none`, and `jbr_picture_frames=0`

Next checkpoint:

- Add screenshot parity coverage for the child color-filter RuntimeEffect row, then run a broader ABI 92 descriptor
  regression subset before moving on to the remaining shader/effect diagnostics and lifecycle-marker gaps.

## Checkpoint: RuntimeEffect ColorFilter Child Screenshot Parity

Status: the Magic Jewel screenshot parity suite now has a dedicated RuntimeEffect child color-filter row, and the focused
old/new renderer capture passed against the ABI 92 artifact set.

What changed:

- Added `parity-runtime-effect-color-filter-child` to Magic Jewel's screenshot parity suite and default RuntimeEffect
  parity coverage.
- The row enables `MAGIC_JEWEL_COMPOSE_RUNTIME_EFFECT_COLOR_FILTER_CHILD`, capturing the old Skiko renderer and the JBR
  command renderer with deterministic timing and window geometry.
- Magic Jewel `README.md` documents child color-filter RuntimeEffect parity alongside the existing pure-color,
  uniform-only, child-shader, combined child+uniform, and color-filter rows.
- `ROADMAP.md` records the focused parity run and report path.

Verification:

- Focused RuntimeEffect child color-filter screenshot parity row passed:
  - command: `CASES="parity-runtime-effect-color-filter-child" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-091255/suite.tsv`
  - result: `avg_delta=1.645`, `bad_pixel_ratio=0.03908`, `compose_bad_pixel_ratio=0.06230`

Next checkpoint:

- Run the expanded RuntimeEffect parity subset including the new child color-filter row, then continue with the remaining
  shader/effect diagnostic and lifecycle-marker gaps.

## Checkpoint: RuntimeEffect Parity Coverage With Child ColorFilters

Status: the focused RuntimeEffect screenshot parity subset passed with the new ABI 92 child color-filter row included.

Verification:

- Expanded RuntimeEffect screenshot parity subset passed:
  - command: `CASES="parity-runtime-effect-pure-color parity-runtime-effect-uniform-only parity-runtime-effect-child-only parity-runtime-effect-shader parity-runtime-effect-color-filter parity-runtime-effect-color-filter-child" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-091420/suite.tsv`
  - result: all six rows passed; child color-filter row reported `avg_delta=1.645`, `bad_pixel_ratio=0.03908`, and
    `compose_bad_pixel_ratio=0.06230`

Next checkpoint:

- Continue shader/effect hardening with the remaining diagnostic and lifecycle-marker gaps: explicit create/use/cache-hit
  markers for RuntimeEffect descriptor handles, plus fallback marker coverage for intentionally invalid descriptor use.

## Checkpoint: Descriptor Handle Use Markers

Status: JBR and Magic Jewel now distinguish descriptor handles that were defined from descriptor handles that were
actually consumed by command replay.

What changed:

- JBR emits `JBR_SKIA_INTEROP_EFFECT_HANDLE_USE backend=... contextId=... handle=... op=...` for typed effect handle
  reference commands.
- JBR emits `JBR_SKIA_INTEROP_SHADER_HANDLE_USE backend=... contextId=... handle=... op=...` for typed shader handle
  reference commands.
- The marker scanner covers the native command path, and Java2D fallback replay emits the same markers for the descriptor
  rect-use commands it can replay.
- Magic Jewel's report parser now exports `jbr_effect_handle_use_frames` and `jbr_shader_handle_use_frames`, prints those
  summaries in reports, and supports strict `EXPECT_MIN_JBR_EFFECT_HANDLE_USES` /
  `EXPECT_MIN_JBR_SHADER_HANDLE_USES` validation gates.
- RuntimeEffect shader/color-filter/child-color-filter command-suite rows now assert handle-use markers, not just handle
  definitions.

Verification:

- Magic Jewel report parser tests passed:
  - command: `./scripts/test-jbr-skia-report-validation.sh`
- Local JBR Skia artifact rebuild passed:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
- RuntimeEffect command rows passed with handle-use assertions:
  - command: `CASES="commands-runtime-effect-shader commands-runtime-effect-color-filter commands-runtime-effect-color-filter-child" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-092522/suite.tsv`
  - observed counts:
    - shader row: `jbr_shader_handle_define_frames=795`, `jbr_shader_handle_use_frames=794`
    - color-filter row: `jbr_effect_handle_define_frames=947`, `jbr_effect_handle_use_frames=947`
    - child color-filter row: `jbr_effect_handle_define_frames=1347`, `jbr_effect_handle_use_frames=1346`

Next checkpoint:

- Add a controlled invalid descriptor-use probe that references a missing effect/shader handle and proves the command path
  fails with a structured fallback marker instead of silently rendering stale or partial output.

## Checkpoint: Invalid Descriptor-Use Fallback Probe

Status: Magic Jewel can now exercise a controlled bad-handle use after CMP has produced a valid command stream, proving
that descriptor-reference corruption falls back with a structured marker instead of silently rendering stale or partial
content.

What changed:

- Skiko gained a test-only `skiko.jbr.interop.corruptDescriptorUseForTesting` switch. In command mode it corrupts one
  descriptor-reference command by replacing the referenced shader/color-filter handle with an impossible handle value
  after the frame is recorded but before JBR replay sees it.
- Skiko emits one `SKIKO_JBR_INTEROP_DESCRIPTOR_USE_CORRUPTED op=...` marker when the test hook mutates a stream.
- Magic Jewel gained `MAGIC_JEWEL_CORRUPT_DESCRIPTOR_USE` plumbing in the launcher and report scripts.
- The command-probe suite now includes `commands-invalid-descriptor-use-fallback`, which enables the child color-filter
  RuntimeEffect row, corrupts one descriptor use, and requires `SKIKO_JBR_INTEROP_FALLBACK reason=command-stream-invalid`.
- The README documents the focused invalid descriptor-use report invocation for future compatibility and regression runs.

Verification:

- Rebuilt Skiko AWT artifacts with the test hook:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt publishAwtPublicationToMavenLocal`
- Focused invalid descriptor-use command probe passed:
  - command: `CASES="commands-invalid-descriptor-use-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-093549/suite.tsv`
  - result: `status=passed`, `fallback_new_count=1`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=0`
  - markers: `SKIKO_JBR_INTEROP_DESCRIPTOR_USE_CORRUPTED op=47`,
    `SKIKO_JBR_INTEROP_COMMAND_FRAME ... rendered=false`,
    `SKIKO_JBR_INTEROP_FALLBACK reason=command-stream-invalid`

Next checkpoint:

- Continue descriptor lifecycle hardening with cache-hit/reuse observability for RuntimeEffect shader and effect handles,
  then add context-migration invalidation probes so steady-state reuse and cache clearing are both visible in reports.

## Checkpoint: Descriptor Cache-Hit Markers

Status: JBR and Magic Jewel now expose explicit descriptor cache-hit markers for steady-state shader/effect handle reuse.
This turns reuse from an inferred condition ("define count stopped growing") into a directly asserted lifecycle signal.

What changed:

- JBR tracks descriptor handles seen by its lifecycle marker scanner per backend, destination context, and handle id.
- JBR emits `JBR_SKIA_INTEROP_SHADER_HANDLE_CACHE_HIT backend=... contextId=... handle=... op=...` when a shader-handle
  use references a handle known from an earlier command frame, not one defined in the same frame.
- JBR emits `JBR_SKIA_INTEROP_EFFECT_HANDLE_CACHE_HIT backend=... contextId=... handle=... op=...` with the same semantics
  for effect/color-filter/image-filter descriptor handles.
- Evict commands remove the handle from the marker-side cache so later uses do not report stale cache hits.
- Magic Jewel report parsing now records `jbr_shader_handle_cache_hit_frames` and
  `jbr_effect_handle_cache_hit_frames`, prints both summaries, and supports
  `EXPECT_MIN_JBR_SHADER_HANDLE_CACHE_HITS` / `EXPECT_MIN_JBR_EFFECT_HANDLE_CACHE_HITS` gates.
- The command-probe suite asserts cache-hit markers on stable descriptor rows:
  `commands-runtime-effect-pure-color`, `commands-color-filter-handle`, `commands-color-matrix-filter`, and
  `commands-lighting-filter`. Animated RuntimeEffect rows remain define/use probes because their uniform payloads
  intentionally produce fresh descriptor handles each frame.

Verification:

- Magic Jewel report parser tests passed:
  - command: `./scripts/test-jbr-skia-report-validation.sh`
- Local JBR Skia artifact rebuild passed:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
  - output artifacts: `/tmp/jbr-api-shim.jar`, `/tmp/jbr-skia-run/desktop`,
    `/tmp/jbr-skia-native/libjbrskiainterop.dylib`
- Stable descriptor cache-hit command probes passed:
  - command: `CASES="commands-runtime-effect-pure-color commands-color-filter-handle commands-color-matrix-filter commands-lighting-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-094603/suite.tsv`
  - result: all four rows passed with `fallback_new_count=0`, `unsupported=none`, and `jbr_picture_frames=0`
  - observed cache hits:
    - RuntimeEffect pure-color shader: `jbr_shader_handle_define_frames=1`,
      `jbr_shader_handle_use_frames=1288`, `jbr_shader_handle_cache_hit_frames=1287`
    - tint color-filter handle: `jbr_effect_handle_define_frames=1`, `jbr_effect_handle_use_frames=889`,
      `jbr_effect_handle_cache_hit_frames=888`
    - color-matrix filter: `jbr_effect_handle_define_frames=1`, `jbr_effect_handle_use_frames=835`,
      `jbr_effect_handle_cache_hit_frames=834`
    - lighting filter: `jbr_effect_handle_define_frames=1`, `jbr_effect_handle_use_frames=1093`,
      `jbr_effect_handle_cache_hit_frames=1092`
- Animated RuntimeEffect define/use rows still passed after moving cache-hit assertions to stable rows:
  - command: `CASES="commands-runtime-effect-shader commands-runtime-effect-color-filter commands-runtime-effect-color-filter-child" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-094911/suite.tsv`
  - result: all three rows passed with `fallback_new_count=0`, `unsupported=none`, and `jbr_picture_frames=0`

Next checkpoint:

- Add an explicit context-migration/cache-clear validation pass that proves Skiko clears CMP-owned descriptor/image caches
  on destination context changes and that JBR sees fresh defines rather than stale handle reuse after migration.

## Checkpoint: Resize Descriptor Cache Recovery

Status: the existing resize/surface-replacement descriptor probe now also proves steady-state cache reuse resumes after
Skiko clears CMP-owned command caches and CMP emits a fresh descriptor define for the new destination surface.

What changed:

- Tightened Magic Jewel's `commands-resize-descriptor-redefine` row to require
  `EXPECT_MIN_JBR_EFFECT_HANDLE_CACHE_HITS=1` in addition to the existing surface-change, command-cache-clear, and
  second-define gates.
- The row still requires `contextChanged=false` and `surfaceChanged=true`, so this checkpoint covers same-context
  destination surface replacement such as resize. True multi-monitor context migration remains a separate follow-up.

Verification:

- Focused resize descriptor redefine row passed:
  - command: `CASES="commands-resize-descriptor-redefine" DURATION_SECONDS=6 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-095217/suite.tsv`
  - result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=981`
  - observed counts: `skiko_surface_change_markers=1`, `skiko_command_cache_clear_markers=1`,
    `jbr_effect_handle_define_frames=2`, `jbr_effect_handle_use_frames=1797`,
    `jbr_effect_handle_cache_hit_frames=1795`

Next checkpoint:

- Add a true context-migration probe when we can reliably move the Magic Jewel window between displays/graphics configs,
  or add a JBR/Skiko test hook that forces a context identity change without depending on physical monitor topology.

## Checkpoint: Forced Context-Change Descriptor Recovery

Status: Skiko and Magic Jewel now have a deterministic context-change validation path that does not depend on the machine
having multiple active displays or on moving the test window between macOS Spaces.

What changed:

- Skiko gained a test-only `skiko.jbr.interop.forceContextChangeOnceForTesting` switch. After the first real JBR surface
  identity has been observed, Skiko applies a persistent synthetic context-id offset to subsequent surface identities.
- The forced identity change emits `SKIKO_JBR_INTEROP_FORCED_CONTEXT_CHANGE oldContextId=...`.
- `noteSurfaceIdentity(...)` now clears command caches when either `contextChanged` or `surfaceChanged` is true. Before
  this checkpoint, a synthetic context-only change would close the Skiko direct context but would not exercise the CMP
  command-cache clear hook.
- Magic Jewel gained `MAGIC_JEWEL_FORCE_CONTEXT_CHANGE` launcher/report plumbing and a
  `commands-forced-context-descriptor-redefine` command-suite row.
- The forced-context row requires `contextChanged=true`, `surfaceChanged=false`, at least one
  `SKIKO_JBR_INTEROP_COMMAND_CACHES_CLEARED reason=contextChanged`, a second JBR effect-handle define, and resumed
  effect-handle cache-hit markers.

Verification:

- Rebuilt Skiko AWT artifacts:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt publishAwtPublicationToMavenLocal`
- Focused forced context-change descriptor row passed:
  - command: `CASES="commands-forced-context-descriptor-redefine" DURATION_SECONDS=6 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-095701/suite.tsv`
  - result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=491`
  - observed markers: `SKIKO_JBR_INTEROP_FORCED_CONTEXT_CHANGE`, `SKIKO_JBR_INTEROP_COMMAND_CACHES_CLEARED reason=contextChanged`,
    `SKIKO_JBR_INTEROP_SURFACE_CHANGED ... contextChanged=true surfaceChanged=false`
  - observed counts: `skiko_surface_change_markers=1`, `skiko_command_cache_clear_markers=1`,
    `jbr_effect_handle_define_frames=2`, `jbr_effect_handle_use_frames=1192`,
    `jbr_effect_handle_cache_hit_frames=1190`

Next checkpoint:

- Run a broader lifecycle/default-suite subset with the new context and cache-hit rows included, then decide whether the
  next functionality slice should target remaining graphics-layer effects or typed path/image-filter descriptor expansion.

## Checkpoint: Descriptor Lifecycle Subset

Status: the descriptor lifecycle probes pass together after adding cache-hit, invalid-use, resize-recovery, and
forced-context-recovery gates.

Verification:

- Broader descriptor lifecycle command subset passed:
  - command: `CASES="commands-runtime-effect-pure-color commands-color-filter-handle commands-color-matrix-filter commands-lighting-filter commands-invalid-descriptor-use-fallback commands-resize-descriptor-redefine commands-forced-context-descriptor-redefine" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-095930/suite.tsv`
  - result: all seven rows passed; cache-hit rows reported zero fallback/picture replay, invalid-use reported one
    structured `command-stream-invalid` fallback, and resize/forced-context rows reported zero fallback/picture replay.

Next checkpoint:

- Move back to functionality coverage. The next likely target is the remaining graphics-layer/render-effect gap: using
  the existing image-filter descriptor machinery from ABI 82-84 in more layer/image-filter drawing surfaces, while keeping
  raw Skiko `SkImageFilter*` pointers out of the ABI.

## Checkpoint: Graphics-Layer RenderEffect Descriptor Assertions

Status: the graphics-layer blur, offset, and chained render-effect rows now prove that JBR consumes and reuses
image-filter descriptor handles instead of merely avoiding fallback.

What changed:

- Tightened Magic Jewel's graphics-layer render-effect command-suite rows:
  - `commands-graphics-layer-render-effect` requires JBR effect-handle define, use, and cache-hit markers.
  - `commands-graphics-layer-offset-effect` requires JBR effect-handle define, use, and cache-hit markers.
  - `commands-graphics-layer-chained-render-effect` requires at least two effect-handle defines, plus use and cache-hit
    markers, so the chained `OffsetEffect(BlurEffect(...))` path proves both child descriptors are materialized.
- `ROADMAP.md` now marks graphics-layer `RenderEffect` descriptors complete and narrows the remaining graphics-layer
  gap to shadows, 3D/camera transforms, offscreen semantics, and broader image-filter surfaces.

Verification:

- Focused graphics-layer effect descriptor row set passed:
  - command: `CASES="commands-graphics-layer-render-effect commands-graphics-layer-offset-effect commands-graphics-layer-chained-render-effect" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-100545/suite.tsv`
  - result: all three rows passed with `fallback_new_count=0`, `unsupported=none`, and `jbr_picture_frames=0`
  - observed command frames: render-effect `257`, offset-effect `605`, chained-render-effect `586`

Next checkpoint:

- Expand the next functionality slice around the remaining graphics-layer gaps or typed image-filter/path-effect
  descriptor coverage, while preserving the strict lifecycle gates added for descriptor define/use/cache-hit behavior.

## Checkpoint: ABI 93 Dashed Rectangle PathEffect Replay

Status: dash path-effect command replay now covers stroked rectangles as well as lines.

What changed:

- Bumped the tightly versioned interop ABI to `93` across JBR, the public Runtime API mirror, Skiko's compatibility gate,
  and CMP's command stream header.
- Added `COMMAND_STROKE_RECT_DASH_PATH_EFFECT = 59` and high capability bit
  `COMMAND_CAP64_HIGH_STROKE_RECT_DASH_PATH_EFFECT = 32`.
- CMP records stroked rectangles with `PathEffect.dashPathEffect(...)` as one structured command carrying solid color,
  rectangle bounds, stroke metadata, dash phase, and dash intervals.
- JBR Java validation rejects malformed dashed-rectangle records, including negative dimensions, bad stroke metadata,
  bad dash phase, and invalid interval counts/values.
- JBR Java2D fallback replay and native Skia replay both consume the new command; native replay rebuilds a
  `SkDashPathEffect` and draws the destination rectangle with a stroke paint.
- Magic Jewel's path-effect probe now draws both a dashed line and a dashed rectangle.

Verification:

- Rebuilt Skiko AWT and published the local snapshot:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt publishAwtPublicationToMavenLocal`
- Focused CMP recorder test passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesDashedStrokeRectRecord`
- Rebuilt local JBR API/desktop/native artifacts:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
- Focused Magic Jewel path-effect command probe passed:
  - command: `CASES="commands-path-effect-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-102456/suite.tsv`
  - result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=155`

Known verification gap:

- Skiko's broader `awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` did not reach the interop tests because
  `compileTestKotlinAwt` is currently blocked by an unrelated `RuntimeEffectTest.kt` unresolved `makeMode` reference.
  Main AWT compile/publish succeeded.

Next checkpoint:

- Continue path-effect coverage toward a typed descriptor shape for non-dash effects such as corner/stamped/chain, or
  add a narrower dashed round-rect/path command if we want one more low-risk step before descriptorizing path effects.

## Checkpoint: ABI 94 Dashed Rounded-Rectangle PathEffect Replay

Status: dash path-effect command replay now covers stroked rounded rectangles in addition to lines and rectangles.

What changed:

- Bumped the tightly versioned interop ABI to `94` across JBR, the public Runtime API mirror, Skiko's compatibility gate,
  and CMP's command stream header.
- Added `COMMAND_STROKE_ROUND_RECT_DASH_PATH_EFFECT = 60` and high capability bit
  `COMMAND_CAP64_HIGH_STROKE_ROUND_RECT_DASH_PATH_EFFECT = 64`.
- CMP records stroked rounded rectangles with `PathEffect.dashPathEffect(...)` as one structured command carrying solid
  color, fixed-point round-rect bounds/radii, stroke metadata, dash phase, and dash intervals.
- JBR Java validation rejects malformed dashed rounded-rectangle records, including inverted bounds, negative radii, bad
  stroke metadata, bad dash phase, and invalid interval counts/values.
- JBR Java2D fallback replay and native Skia replay both consume the new command. Native replay rebuilds a
  `SkDashPathEffect` and draws a stroked `SkRRect`.
- Magic Jewel's path-effect probe now draws a dashed line, dashed rectangle, and dashed rounded rectangle.

Verification:

- Rebuilt Skiko AWT and published the local snapshot:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt publishAwtPublicationToMavenLocal`
- Focused CMP recorder tests passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesDashedStrokeRectRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesDashedStrokeRoundRectRecord`
- Rebuilt local JBR API/desktop/native artifacts:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
- Focused Magic Jewel path-effect command probe passed:
  - command: `CASES="commands-path-effect-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-103512/suite.tsv`
  - result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=152`

Next checkpoint:

- Decide whether the next path-effect increment should be dashed arbitrary paths, which requires a variable path payload
  plus dash metadata, or the broader typed descriptor route for corner/stamped/chain effects.

## Checkpoint: ABI 95 Dashed Arbitrary-Path PathEffect Replay

Status: dash path-effect command replay now covers arbitrary stroked paths in addition to lines, rectangles, and rounded
rectangles.

What changed:

- Bumped the tightly versioned interop ABI to `95` across JBR, the public Runtime API mirror, Skiko's compatibility gate,
  and CMP's command stream header.
- Added `COMMAND_STROKE_PATH_DASH_PATH_EFFECT = 61` and high capability bit
  `COMMAND_CAP64_HIGH_STROKE_PATH_DASH_PATH_EFFECT = 128`.
- CMP records stroked arbitrary paths with `PathEffect.dashPathEffect(...)` as one structured command carrying solid
  color, stroke metadata, dash phase/intervals, fill type, and the existing path verb payload.
- JBR Java validation rejects malformed dashed-path records, including bad stroke metadata, bad dash phase, invalid
  interval counts/values, invalid fill types, mismatched path payload lengths, and malformed path verbs.
- JBR Java2D fallback replay and native Skia replay both consume the new command. Native replay rebuilds the existing
  path payload into `SkPath`, attaches `SkDashPathEffect`, and strokes the path with Skia.
- Magic Jewel's path-effect probe now draws a dashed line, rectangle, rounded rectangle, and cubic arbitrary path.

Verification:

- Rebuilt Skiko AWT and published the local snapshot:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt publishAwtPublicationToMavenLocal`
- Focused CMP recorder test passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesDashedStrokePathRecord`
- Rebuilt local JBR API/desktop/native artifacts:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
- Focused Magic Jewel path-effect command probe passed:
  - command: `CASES="commands-path-effect-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-104709/suite.tsv`
  - result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=260`

Next checkpoint:

- Continue path-effect coverage by designing typed path-effect descriptors for non-dash effects such as corner,
  stamped, discrete, and chained path effects, unless a higher-value command replay gap appears first.

## Checkpoint: ABI 96 Corner PathEffect Descriptor Replay

Status: typed path-effect descriptors now cover `PathEffect.cornerPathEffect(...)` for arbitrary `drawPath` records.

What changed:

- Bumped the tightly versioned interop ABI to `96` across JBR, the public Runtime API mirror, Skiko's compatibility gate,
  and CMP's command stream header.
- Added `COMMAND_DRAW_PATH_PATH_EFFECT_REF = 62`, high capability bit
  `COMMAND_CAP64_HIGH_PATH_EFFECT_DESCRIPTOR_REF = 256`, and descriptor type
  `COMMAND_EFFECT_DESCRIPTOR_CORNER_PATH_EFFECT = 9`.
- CMP now preserves structured corner path-effect metadata in `SkiaBackedPathEffect`, defines a reusable descriptor
  handle, and records descriptor-backed `drawPath` commands for solid-color fill/stroke paths.
- JBR validates corner path-effect descriptors as a one-float payload, rejects non-finite/negative radii, and validates
  descriptor-ref path records against the existing path payload parser.
- Native replay resolves the descriptor from the scoped handle cache, rebuilds `SkCornerPathEffect`, attaches it to the
  `SkPaint`, and draws the path through Skia. Java2D fallback accepts the command and draws the original path if the
  native bridge is unavailable.
- Magic Jewel's path-effect probe now includes a corner path-effect shape in addition to dashed line/rect/round-rect/path
  coverage.

Verification:

- Rebuilt Skiko AWT and published the local snapshot:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt publishAwtPublicationToMavenLocal`
- Focused CMP recorder test passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesCornerPathEffectDescriptorPathRecord`
- Rebuilt local JBR API/desktop/native artifacts:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
- Focused Magic Jewel path-effect command probe passed:
  - command: `CASES="commands-path-effect-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-105953/suite.tsv`
  - result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=145`

Next checkpoint:

- Extend the typed path-effect descriptor family to stamped path effects, then chain descriptors once both dash/corner and
  stamped leaves can be represented.

## Checkpoint: ABI 97 Stamped PathEffect Descriptor Replay

Status: typed path-effect descriptors now cover `PathEffect.stampedPathEffect(...)` for arbitrary `drawPath` records.

What changed:

- Bumped the tightly versioned interop ABI to `97` across JBR, the public Runtime API mirror, Skiko's compatibility gate,
  and CMP's command stream header.
- Added descriptor type `COMMAND_EFFECT_DESCRIPTOR_STAMPED_PATH_EFFECT = 10`.
- CMP now preserves stamped path-effect metadata in `SkiaBackedPathEffect`, including advance, phase, style, fill type,
  and the nested stamp-shape path payload.
- JBR validates stamped descriptors for finite positive advance, finite non-negative phase, valid style/fill type,
  bounded path payload length, and valid path verbs.
- Native replay rebuilds the nested stamp path, creates `SkPath1DPathEffect`, attaches it to the paint, and reuses the
  existing descriptor-ref `drawPath` command.
- Magic Jewel's path-effect probe now includes a stamped rotating marker path in addition to dash and corner coverage.

Verification:

- Rebuilt Skiko AWT and published the local snapshot:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt publishAwtPublicationToMavenLocal`
- Focused CMP recorder test passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesStampedPathEffectDescriptorPathRecord`
- Rebuilt local JBR API/desktop/native artifacts:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
- Focused Magic Jewel path-effect command probe passed:
  - command: `CASES="commands-path-effect-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-110918/suite.tsv`
  - result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=330`

Next checkpoint:

- Add chained path-effect descriptors so Compose can combine descriptor-backed leaves without falling back to picture
  replay.

## Checkpoint: ABI 98 Chained PathEffect Descriptor Replay

Status: typed path-effect descriptors now cover `PathEffect.chainPathEffect(...)` when both child effects are already
descriptor-backed.

What changed:

- Bumped the tightly versioned interop ABI to `98` across JBR, the public Runtime API mirror, Skiko's compatibility gate,
  and CMP's command stream header.
- Added descriptor type `COMMAND_EFFECT_DESCRIPTOR_CHAIN_PATH_EFFECT = 11`.
- CMP now preserves structured chain metadata when both outer and inner path effects have descriptors, recursively
  defines the child descriptors first, and emits a chain descriptor containing child handles.
- JBR validates chain descriptors as two path-effect descriptor handle pairs and rejects missing or non-path-effect
  children.
- Native replay resolves both children, rebuilds their Skia path effects recursively, composes them with
  `SkPathEffect::MakeCompose`, and reuses the descriptor-ref `drawPath` command.
- Magic Jewel's path-effect probe now includes a chained corner+stamped path-effect shape.

Verification:

- Rebuilt Skiko AWT and published the local snapshot:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt publishAwtPublicationToMavenLocal`
- Focused CMP recorder test passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesChainedPathEffectDescriptorPathRecord`
- Rebuilt local JBR API/desktop/native artifacts:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
- Focused Magic Jewel path-effect command probe passed:
  - command: `CASES="commands-path-effect-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-111807/suite.tsv`
  - result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=657`

Next checkpoint:

- Move to the next unsupported rendering family surfaced by the full ABI 98 sweep.

## Checkpoint: ABI 98 Full Command-Path Sweep

Status: the full Magic Jewel command-probe suite passes after the path-effect descriptor work. All non-negative rows stay
on JBR command replay with no new fallback, while the intentional fallback rows still report structured fallback markers.

Verification:

- Full Magic Jewel command-probe sweep passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-112035/suite.tsv`
  - result: `45/45` rows passed.
- The sweep covers live animation, mixed Swing popups/menus, text/images, image/composite shaders, RuntimeEffects,
  runtime-effect color filters, image filters, color filters, blend modes, graphics layers, render effects, saveLayer
  filters, descriptor lifecycle probes, path-effect descriptors, and explicit fallback rows.
- The broad rows reported `fallback_new_count=0`, `unsupported=none`, and `jbr_picture_frames=0`; the expected fallback
  rows stayed observable through `SKIKO_JBR_INTEROP_FALLBACK`/unsupported markers instead of silently using the fast path.

Next checkpoint:

- Use the now-green broad sweep to target the next remaining visual/compatibility gap, with preference for screenshot
  parity and any rendering family that still requires intentional fallback rather than command replay.

## Checkpoint: RuntimeEffect Child-Type Crash Hardening

Status: JBR native RuntimeEffect shader replay now treats child type mismatches as structured build fallback instead of
allowing Skia's builder assignment to abort the JVM.

What changed:

- JBR checks `SkRuntimeEffect::Child::type` before assigning a child shader through `SkRuntimeEffectBuilder`.
- A non-shader child slot now logs
  `JBR_SKIA_INTEROP_RUNTIME_EFFECT_BUILD_FAILED ... stage=child-type ...` and returns `false` from command replay, which
  Skiko reports as command-stream fallback.
- Skiko has a test-only post-recording corruption hook that mutates one valid RuntimeEffect shader descriptor so JBR sees
  a `colorFilter` child declaration while the descriptor still supplies a shader handle. This avoids relying on Skiko's
  bundled runtime to construct an invalid object up front.
- Magic Jewel documents and runs `commands-runtime-effect-child-type-fallback` as the crash-regression row.

Verification:

- Rebuilt local JBR API/desktop/native artifacts:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
- Rebuilt Skiko AWT and published the local snapshot:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt publishAwtPublicationToMavenLocal`
- Focused Magic Jewel crash-regression probe passed:
  - command: `CASES="commands-runtime-effect-child-type-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-120647/suite.tsv`
  - result: `status=passed`, `fallback_new_count=1`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=0`
- Compact RuntimeEffect regression subset passed:
  - command: `CASES="commands-runtime-effect-shader commands-runtime-effect-child-only commands-runtime-effect-build-fallback commands-runtime-effect-child-type-fallback commands-runtime-effect-color-filter commands-runtime-effect-color-filter-child" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-120945/suite.tsv`
- Magic Jewel report parser validation passed:
  - command: `bash -n scripts/jbr-skia-interop-report.sh scripts/test-jbr-skia-report-validation.sh scripts/jbr-skia-command-probe-suite.sh scripts/run-jbr-skia.sh && ./scripts/test-jbr-skia-report-validation.sh`

Next checkpoint:

- Fold the new child-type fallback row into the full command-probe sweep, then resume visual parity/remaining fallback
  work.

## Checkpoint: RuntimeEffect Child-Type Full Sweep

Status: the full Magic Jewel command-probe suite passes with the new child-type crash-regression row included.

Verification:

- Full Magic Jewel command-probe sweep passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-121521/suite.tsv`
  - result: `46/46` rows passed.
- The new `commands-runtime-effect-child-type-fallback` row reported `fallback_new_count=1`, `unsupported=none`,
  `jbr_picture_frames=0`, and `jbr_command_frames=0`, proving the malformed RuntimeEffect child type exits through
  structured fallback rather than native abort.

Next checkpoint:

- Resume remaining visual parity and intentional fallback work. The only full-suite unsupported markers are still from
  the deliberate invalid-gradient row (`sweepGradientStops` nested under graphics-layer fallback).

## Checkpoint: Shadowless Screenshot Parity Harness

Status: Magic Jewel screenshot parity now captures the actual macOS window contents without variable window-shadow extents,
and failed comparisons preserve their metrics in the report instead of dropping diagnostics.

What changed:

- CMP's shared macOS window capture helper now logs the selected window id/bounds and calls `screencapture -o -l...`, so
  old/new parity images compare the same window content rectangle instead of shadow-included PNGs whose dimensions can
  drift by focus/window-server state.
- Magic Jewel's parity script now appends `parity.log` output into `report.md` and mirrors `screenshot_parity_*` metrics
  into `summary.properties` even when the comparator exits non-zero.
- Magic Jewel's parity suite records failed rows into `suite.tsv` before returning failure, with `missing` placeholders
  for fields that cannot be computed.
- Shadowless capture required retuning screenshot assertions and broad full-window tolerances while preserving tighter
  region gates for stable geometry/color probes.

Verification:

- Focused RuntimeEffect uniform-only parity passed after the capture fix:
  - command: `CASES="parity-runtime-effect-uniform-only" SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-130130/suite.tsv`
  - result: `avg_delta=2.764`, `bad_pixel_ratio=0.05250`, `compose_bad_pixel_ratio=0.07446`,
    `compose_bottom_swatches_bad_pixel_ratio=0.00000`
- Rich baseline parity passed with shadowless capture:
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-130313/suite.tsv`
- Remaining shadowless parity matrix passed for clean geometry, native text, RuntimeEffect pure/uniform/child/shader
  variants, RuntimeEffect color-filter variants, and graphics-layer effects:
  - command: `CASES="parity-native-text parity-runtime-effect-pure-color parity-runtime-effect-uniform-only parity-runtime-effect-child-only parity-runtime-effect-shader parity-runtime-effect-color-filter parity-runtime-effect-color-filter-child parity-graphics-layer-effects" SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-130835/suite.tsv`
- Full default shadowless screenshot parity suite passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-131506/suite.tsv`

Next checkpoint:

- Continue toward the next remaining intentional fallback or visual parity gap, using the shadowless parity suite as the
  visual regression gate.

## Checkpoint: Rectangular Graphics-Layer Shadow Command Replay

Status: completed as a first narrow shadow slice for command-recorded graphics layers.

What changed:

- CMP nested graphics-layer replay now accepts finite positive rectangular `shadowElevation` and emits a shadow before
  the layer content using only existing JBR-owned command primitives:
  - define/reuse a blur image-filter descriptor
  - `COMMAND_SAVE_LAYER_IMAGE_FILTER_REF` around the shadow bounds
  - a layer-local shadow fill rectangle encoded in raw pixel coordinates
  - restore, then replay the normal layer saveLayer/content sequence.
- The slice intentionally supports only rectangular layer shadows. Non-rectangular shadow outlines remain explicit
  `graphicsLayer:shadowOutline` fallback work rather than approximating path/round-rect elevation semantics silently.
- Magic Jewel gained `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_SHADOW` /
  `magic.jewel.compose.graphicsLayerShadow`, a `commands-graphics-layer-shadow` command-probe row, and a screenshot
  assertion that detects the shadow-darkened right probe region.
- The first Magic Jewel run exposed a real encoder bug: shadow fill coordinates were accidentally written in fixed-1000
  scalar form even though JBR's `COMMAND_FILL_RECT` expects raw pixel-space ints. The focused CMP recorder test now
  asserts those raw coordinates so this does not regress.
- Broader suite validation exposed two unrelated brittle screenshot thresholds while preserving clean command replay:
  image-shader strict-dark pixels and gradient-stroke orange pixels. Magic Jewel now keeps the old `probeRightDark`
  meaning for image-shader checks, adds a separate `probeRightShadow` counter for layer shadows, and slightly lowers the
  gradient-stroke orange threshold to match the captured probe size.

Verification:

- Focused CMP recorder test passed against the patched local Skiko snapshot:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysRectangularLayerShadow`
- Focused Magic Jewel shadow row passed:
  - command: `CASES="commands-graphics-layer-shadow" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-134341/suite.tsv`
  - result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=157`
- Graphics-layer render-effect plus shadow regression subset passed:
  - command: `CASES="commands-graphics-layer-render-effect commands-graphics-layer-offset-effect commands-graphics-layer-chained-render-effect commands-graphics-layer-shadow" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-134631/suite.tsv`
- Magic Jewel report/script validation passed after adding `probeRightShadow`:
  - command: `bash -n scripts/jbr-skia-interop-report.sh scripts/jbr-skia-command-probe-suite.sh scripts/assert-jbr-skia-command-window-screenshot.sh scripts/test-jbr-skia-report-validation.sh && ./scripts/test-jbr-skia-report-validation.sh`
- Previously brittle image-shader and gradient-stroke rows passed in isolation after screenshot-oracle retuning:
  - image-shader suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-140254/suite.tsv`
  - gradient-stroke suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-141834/suite.tsv`
- Remaining command-probe tail from image filters through graphics-layer shadow and invalid-gradient fallback passed:
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-141933/suite.tsv`

Roadmap update:

- `ROADMAP.md` now marks the first rectangular graphics-layer shadow slice complete and narrows the remaining graphics-layer
  gap to non-rectangular/elevation-accurate shadows, 3D/camera transforms, offscreen strategy semantics, and broader
  image-filter surfaces.

Next checkpoint:

- Commit the CMP, Magic Jewel, and roadmap/plan updates, then continue with the next remaining graphics-layer gap or
  parity row depending on the next validation target.

## Checkpoint: Rectangular Graphics-Layer Shadow Screenshot Parity

Status: completed for the focused old/new parity row.

What changed:

- Magic Jewel's screenshot parity suite now includes `parity-graphics-layer-shadow` in the default row set.
- The row disables unrelated optional probes, enables the rectangular graphics-layer shadow probe, and keeps the
  comparison scoped to the same shadowless window-capture pipeline used by the other parity rows.
- This gives the rectangular shadow slice both command-level validation and old/new visual parity coverage.

Verification:

- Focused graphics-layer shadow parity row passed:
  - command: `CASES="parity-graphics-layer-shadow" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-143422/suite.tsv`
  - result: `avg_delta=2.360`, `bad_pixel_ratio=0.05277`, `compose_bad_pixel_ratio=0.07589`,
    `compose_bottom_swatches_bad_pixel_ratio=0.00000`

Next checkpoint:

- Commit the Magic Jewel parity row and docs, then continue with the next remaining graphics-layer semantic gap.

## Checkpoint: Rounded Graphics-Layer Shadow Command Replay

Status: completed as the next narrow shadow slice for command-recorded graphics layers.

What changed:

- CMP graphics-layer command replay now allows `Outline.Rounded` shadows in addition to rectangular shadows. The replay
  still uses the existing ABI: a JBR-owned blur image-filter descriptor, a saveLayer-image-filter command, transform and
  clip-path commands, and a layer-local shadow fill before the actual layer content.
- Rounded shadows are generated by translating the rounded outline path by the shadow offset, clipping the shadow source
  fill to that translated path, then blurring the saveLayer result. This preserves the command ordering and avoids sharing
  any Skiko-owned `SkImageFilter*`, `SkPath*`, or shader pointers with JBR.
- The slice deliberately remains approximate Compose shadow replay. It covers rounded-outline visibility and ordering, not
  full platform-elevation/spot/ambient shadow parity. `Outline.Generic` shadows remain an explicit
  `graphicsLayer:shadowOutline` fallback until we add a separately validated path/elevation strategy.
- Magic Jewel now has a `commands-graphics-layer-round-shadow` command-probe row that combines the existing rounded-clip
  layer probe with the graphics-layer shadow flag. The screenshot oracle keeps the strict rounded-clip thresholds for the
  non-shadow row and uses a looser cyan threshold only for the combined shadow case, where shadow-darkened pixels are part
  of the expected output.

Verification:

- Focused CMP recorder tests passed against the patched local Skiko snapshot:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysRectangularLayerShadow --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerShadowPathBeforeShadowFill`
- Focused Magic Jewel rounded-shadow row passed:
  - command: `CASES="commands-graphics-layer-round-shadow" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-144839/suite.tsv`
  - result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=214`
- Graphics-layer shadow/rounded-clip regression subset passed:
  - command: `CASES="commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-round-clip" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-144925/suite.tsv`

Roadmap update:

- `ROADMAP.md` now marks rounded-outline graphics-layer shadow replay complete and narrows the remaining graphics-layer
  shadow gap to generic-path/elevation-accurate semantics.

Next checkpoint:

- Commit the CMP, Magic Jewel, and roadmap/plan updates, then continue with the next graphics-layer gap: either generic
  path shadows or the next offscreen/3D semantics fallback probe.

## Checkpoint: Generic-Path Graphics-Layer Shadow Command Replay

Status: completed by extending the rounded-shadow machinery to `Outline.Generic`.

What changed:

- CMP now accepts generic-path outlines for command-recorded graphics-layer shadows and passes the existing
  `Outline.Generic.path` into the same shadow replay helper used by rounded outlines.
- No new ABI was required. JBR still receives ordinary command records: saveLayer-image-filter by descriptor handle,
  translate, clipPath, fillRect, restore, then the actual layer content replay.
- This closes the previous `graphicsLayer:shadowOutline` fallback for path-backed 2D layer shadows. The remaining shadow
  work is accuracy rather than coverage: Compose's full elevation model, spot/ambient split, and platform-specific shadow
  semantics are still outside this approximation.
- Magic Jewel now includes `commands-graphics-layer-path-shadow`, combining the existing generic-path clip probe with the
  graphics-layer shadow flag and the same shadow-region screenshot oracle.

Verification:

- Focused CMP recorder test passed after rebuilding the local CMP UI graphics jar:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerShadowPathBeforeShadowFill`
- Focused Magic Jewel path-shadow row passed:
  - command: `CASES="commands-graphics-layer-path-shadow" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-145744/suite.tsv`
  - result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=150`
- Graphics-layer shadow/path regression subset passed:
  - command: `CASES="commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow commands-graphics-layer-path-clip" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-145831/suite.tsv`

Roadmap update:

- `ROADMAP.md` now marks generic-path graphics-layer shadow replay complete and narrows remaining shadow work to
  elevation-accurate semantics.

Next checkpoint:

- Commit this follow-up slice, then move to the next graphics-layer semantic boundary: explicit validation/fallback for
  3D rotation and offscreen compositing modes before attempting broader support.

## Checkpoint: Graphics-Layer 3D/Offscreen Strict Fallbacks

Status: completed as explicit safety coverage for semantics the command bridge does not replay yet.

What changed:

- CMP already rejected nonzero `rotationX` and `rotationY` for command-recorded graphics layers. This checkpoint adds the
  missing explicit rejection for `CompositingStrategy.Offscreen`, because that strategy requires intermediate-buffer
  semantics that the current nested command replay should not approximate silently.
- Magic Jewel gained probe flags for:
  - `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_ROTATION_X`
  - `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_OFFSCREEN`
- The command-probe suite now has two intentional fallback rows:
  - `commands-graphics-layer-rotationx-fallback`
  - `commands-graphics-layer-offscreen-fallback`
- These rows assert the exact recorder reasons, prove picture fallback remains available, and guard against future
  accidental partial command replay of 3D/camera or offscreen-buffer semantics.

Verification:

- CMP focused recorder test still passed after adding the Offscreen guard:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerShadowPathBeforeShadowFill`
- Magic Jewel compiled after adding the new probe flags:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon compileKotlin`
- Focused fallback rows passed:
  - command: `CASES="commands-graphics-layer-rotationx-fallback commands-graphics-layer-offscreen-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-150748/suite.tsv`
  - rotationX result: `status=passed`, `unsupported=graphicsLayer:rotationX:172,...`, `jbr_picture_frames=171`,
    `jbr_command_frames=0`
  - Offscreen result: `status=passed`, `unsupported=graphicsLayer:compositingStrategy:164,...`,
    `jbr_picture_frames=165`, `jbr_command_frames=0`

Roadmap update:

- `ROADMAP.md` now records 3D rotation/camera and Offscreen compositing as named strict fallback surfaces rather than
  ambiguous gaps.

Next checkpoint:

- Continue with the next fidelity gap that can move from fallback to command replay safely. The leading candidates are
  elevation-accurate shadow semantics or a first offscreen-buffer command model, but both need a small design pass before
  implementation.

## Checkpoint: Rounded/Path Graphics-Layer Shadow Screenshot Parity

Status: focused old/new parity rows passed for the rounded and generic-path shadow slices.

What changed:

- Magic Jewel's screenshot parity suite now includes these rows in the default set:
  - `parity-graphics-layer-round-shadow`
  - `parity-graphics-layer-path-shadow`
- Both rows disable unrelated optional probes, enable the corresponding rounded/path graphics-layer outline plus shadow,
  and keep the comparison in the existing shadowless, window-only capture pipeline.
- The rows use slightly broader right-probe/Compose-canvas thresholds than the rectangular shadow row because the clipped
  shadow region intentionally changes how many cyan/purple/shadow pixels land in the focused probe area.

Verification:

- Focused rounded/path shadow parity rows passed:
  - command: `CASES="parity-graphics-layer-round-shadow parity-graphics-layer-path-shadow" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-151108/suite.tsv`
  - rounded result: `avg_delta=2.276`, `bad_pixel_ratio=0.05203`, `compose_bad_pixel_ratio=0.07613`,
    `compose_bottom_swatches_bad_pixel_ratio=0.00000`
  - path result: `avg_delta=2.265`, `bad_pixel_ratio=0.05213`, `compose_bad_pixel_ratio=0.07630`,
    `compose_bottom_swatches_bad_pixel_ratio=0.00000`

Roadmap update:

- `ROADMAP.md` now records screenshot parity coverage for rectangular, rounded, and generic-path graphics-layer shadows.

Next checkpoint:

- Commit the parity suite updates, then continue toward the next command coverage or fidelity gap.

## Checkpoint: Graphics-Layer ModulateAlpha Command Probe

Status: completed as positive coverage for the supported non-Offscreen compositing strategy.

What changed:

- Magic Jewel gained `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_MODULATE_ALPHA` /
  `magic.jewel.compose.graphicsLayerModulateAlpha`.
- The graphics-layer probe sets `CompositingStrategy.ModulateAlpha` when requested, while leaving Offscreen as the higher
  precedence unsupported toggle for fallback validation.
- The command-probe suite now includes `commands-graphics-layer-modulate-alpha` in the default graphics-layer matrix.
- This sits next to the Offscreen fallback row: ModulateAlpha is expected to remain command-renderable because CMP records
  the layer contents with an alpha multiplier and replays the parent layer without an additional offscreen-alpha saveLayer.

Verification:

- Magic Jewel compiled:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon compileKotlin`
- Focused ModulateAlpha row passed:
  - command: `CASES="commands-graphics-layer-modulate-alpha" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-151636/suite.tsv`
  - result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=236`

Roadmap update:

- `ROADMAP.md` now records ModulateAlpha as validated command-path graphics-layer coverage, while Offscreen remains a
  named strict fallback.

Next checkpoint:

- Continue with a broader graphics-layer matrix sweep or start the design/implementation slice for an actual offscreen
  buffer command model.

## Checkpoint: Compact Graphics-Layer Command Matrix

Status: completed as a focused regression sweep over the current graphics-layer command surface.

Verification:

- The compact graphics-layer matrix passed:
  - command: `CASES="commands-graphics-layer commands-graphics-layer-modulate-alpha commands-graphics-layer-clip commands-graphics-layer-round-clip commands-graphics-layer-path-clip commands-graphics-layer-blend-mode commands-graphics-layer-color-filter commands-graphics-layer-color-matrix-filter commands-graphics-layer-render-effect commands-graphics-layer-offset-effect commands-graphics-layer-chained-render-effect commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow commands-graphics-layer-rotationx-fallback commands-graphics-layer-offscreen-fallback commands-graphics-layer-blend-color-filter commands-graphics-layer-blend-color-matrix-filter" DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-151820/suite.tsv`
- Supported rows stayed in command mode with `fallback_new_count=0`, `unsupported=none`, and `jbr_picture_frames=0`.
- Intentional fallback rows stayed on picture replay with exact unsupported reasons:
  - `graphicsLayer:rotationX`
  - `graphicsLayer:compositingStrategy`

Roadmap update:

- `ROADMAP.md` now records the compact graphics-layer matrix as the current regression checkpoint for this subsystem.

Next checkpoint:

- Move from coverage polish to the next implementation surface. The highest-value remaining graphics-layer work is an
  explicit offscreen-buffer command model, but it should start with a small ABI/design checkpoint rather than a blind
  implementation.

## Checkpoint: Short Full Command-Probe Sweep

Status: completed after the graphics-layer shadow/fallback/ModulateAlpha additions.

Verification:

- Full default Magic Jewel command-probe suite passed at short duration:
  - command: `DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-152730/suite.tsv`
- Supported rows stayed on the JBR command path with no picture replay.
- Intentional fallback rows, including RuntimeEffect builder/child-type failures, invalid descriptor use, graphics-layer
  rotationX, graphics-layer Offscreen, and invalid sweep-gradient metadata, passed by taking the expected fallback path
  with parseable reasons.

Roadmap update:

- `ROADMAP.md` now records the short full-suite pass as the latest broad command-regression checkpoint.

Next checkpoint:

- Start the Offscreen command-model design/implementation slice, or postpone it and keep shrinking smaller fallback
  surfaces first.

## Checkpoint: Simple Offscreen Graphics-Layer Replay

Status: completed for the first bounded Offscreen command model.

What changed:

- CMP command-recorded graphics layers now accept `CompositingStrategy.Offscreen` for the current 2D layer subset.
- The replay still uses existing ABI commands. It emits the normal layer saveLayer, then adds an explicit
  `COMMAND_CLIP_RECT` to the layer bounds before appending child commands. This models the important Offscreen behavior
  we can safely claim in the current command stream: layer contents are bounded before being composited back into the
  parent.
- `CompositingStrategy.ModulateAlpha` remains the alpha-multiplied child-recording path. `CompositingStrategy.Offscreen`
  remains an actual saveLayer path with bounds clipping.
- 3D rotation/camera stays on strict fallback; this checkpoint does not try to approximate perspective transforms.
- Magic Jewel's Offscreen row changed from an intentional fallback row to a supported command row:
  `commands-graphics-layer-offscreen`.

Verification:

- Focused CMP recorder test passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysOffscreenLayerBoundsClipBeforeContent`
- Magic Jewel compiled:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon compileKotlin`
- Focused Magic Jewel Offscreen/rotationX rows passed:
  - command: `CASES="commands-graphics-layer-offscreen commands-graphics-layer-rotationx-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-155623/suite.tsv`
  - Offscreen result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=304`
  - rotationX result: `status=passed`, `unsupported=graphicsLayer:rotationX:161,...`,
    `jbr_picture_frames=161`, `jbr_command_frames=0`
- Compact graphics-layer matrix with Offscreen in the supported set passed:
  - command: `CASES="commands-graphics-layer commands-graphics-layer-modulate-alpha commands-graphics-layer-offscreen commands-graphics-layer-clip commands-graphics-layer-round-clip commands-graphics-layer-path-clip commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow commands-graphics-layer-rotationx-fallback" DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-155924/suite.tsv`

Roadmap update:

- `ROADMAP.md` now records simple Offscreen replay as supported and narrows the remaining graphics-layer semantic gap to
  elevation-accurate shadows, 3D/camera transforms, and broader image-filter surfaces.

Next checkpoint:

- Commit this slice, then add screenshot parity for the Offscreen row or move to the next graphics-layer fidelity gap.

## Checkpoint: Simple Offscreen Graphics-Layer Screenshot Parity

Status: completed as the visual parity gate for the first bounded Offscreen command model.

What changed:

- Magic Jewel's named screenshot parity suite now includes `parity-graphics-layer-offscreen` in the default row set.
- The row enables the graphics-layer probe with `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_OFFSCREEN=true` while disabling
  unrelated blend, color-filter, path-effect, path, arc, round-rect, and gradient probes so the diff focuses on the
  Offscreen layer semantics.
- The README now documents graphics-layer Offscreen as part of the deterministic old/new window-capture parity suite.

Verification:

- Focused Magic Jewel Offscreen screenshot parity row passed:
  - command: `CASES="parity-graphics-layer-offscreen" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-160638/suite.tsv`
  - result: `status=passed`, `avg_delta=2.191`, `bad_pixel_ratio=0.05195`,
    `compose_bad_pixel_ratio=0.07600`, `compose_bottom_swatches_bad_pixel_ratio=0.00000`

Roadmap update:

- `ROADMAP.md` now records the Offscreen screenshot parity row alongside the rectangular, rounded, and generic-path
  graphics-layer shadow parity rows.

Next checkpoint:

- Continue from graphics-layer Offscreen into the next remaining fidelity gap. The highest-risk item is 3D/camera
  transform support because it needs a perspective-transform command model; the smaller alternative is another bounded
  image-filter/effect surface with screenshot parity.

## Checkpoint: ABI 99 Canvas Concat Matrix

Status: completed as the first general matrix-transform command slice.

What changed:

- JBR, the public JBR API mirror, Skiko, and CMP now agree on command-stream ABI 99.
- JBR exposes and gates `COMMAND_CAP64_HIGH_CONCAT_MATRIX33`.
- CMP records `Canvas.concat(Matrix)` as `COMMAND_CONCAT_MATRIX33`, carrying nine raw float bits in SkMatrix order:
  scaleX, skewX, translateX, skewY, scaleY, translateY, perspective0, perspective1, perspective2.
- JBR replays the record by reconstructing an `SkMatrix` and calling `SkCanvas::concat`.
- Magic Jewel gained a dedicated `commands-concat-transform` row that draws a skewed cyan rectangle through
  `Canvas.concat(...)`, so this path is validated independently from the older translate/scale/rotate commands.

Verification:

- CMP focused recorder test passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesConcatMatrix33Record`
- JBR local artifacts rebuilt:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
  - outputs: `/tmp/jbr-api-shim.jar`, `/tmp/jbr-skia-run/desktop`, `/tmp/jbr-skia-native/libjbrskiainterop.dylib`
- Skiko ABI 99 AWT artifact published:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt publishAwtPublicationToMavenLocal`
- Magic Jewel compiled:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon compileKotlin`
- Focused Magic Jewel concat-transform row passed:
  - command: `CASES="commands-concat-transform" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-162333/suite.tsv`
  - result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=301`

Known validation note:

- A targeted Skiko `awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` run did not reach the interop tests
  because `src/commonTest/kotlin/org/jetbrains/skia/RuntimeEffectTest.kt` currently fails test compilation with an
  unrelated unresolved `makeMode` reference. The production `compileKotlinAwt` and Maven-local publish path passed.

Roadmap update:

- `ROADMAP.md` now records ABI 99 and the focused Magic Jewel concat-transform command-probe pass.

Next checkpoint:

- Use the new matrix primitive as the basis for a 3D/camera graphics-layer design slice. The next safe step is to add a
  strict positive/negative recorder test for matrix-composed layer transforms before enabling any live rotationX/Y row.

## Checkpoint: Graphics-Layer RotationX Matrix Replay

Status: completed as the first live graphics-layer 3D transform slice on top of ABI 99.

What changed:

- CMP graphics-layer command replay now accepts finite `rotationX`/`rotationY` and positive finite `cameraDistance`.
- When a graphics layer has 3D rotation, CMP computes Compose's layer transform with `prepareTransformationMatrix(...)`,
  translates to the layer's destination, emits `COMMAND_CONCAT_MATRIX33`, and then replays the existing saveLayer/content
  commands inside that transformed scope.
- The older translate/pivot/rotateZ/scale command sequence remains in place for non-3D layers, so the existing 2D layer
  command streams stay compact and stable.
- Magic Jewel's graphics-layer rotationX probe is now a supported command row, `commands-graphics-layer-rotationx`,
  instead of an expected fallback row.

Verification:

- CMP focused recorder test passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerMatrixTransformBeforeSaveLayer`
- Magic Jewel compiled:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon compileKotlin`
- Focused Magic Jewel rotationX graphics-layer row passed:
  - command: `CASES="commands-graphics-layer-rotationx" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-163107/suite.tsv`
  - result: `status=passed`, `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=161`

Roadmap update:

- `ROADMAP.md` now marks first 3D/camera graphics-layer replay as supported and narrows remaining graphics-layer work
  to elevation-accurate shadows, broader image-filter surfaces, and screenshot parity for 3D/camera transforms.

Next checkpoint:

- Add old/new screenshot parity for the rotationX graphics-layer row, then run a compact graphics-layer matrix that
  includes Offscreen, ModulateAlpha, shadows, and rotationX together.

## Checkpoint: Graphics-Layer RotationX Screenshot Parity

Status: completed as the old/new visual gate for the first 3D/camera graphics-layer command replay path.

What changed:

- Magic Jewel's named screenshot parity suite now includes `parity-graphics-layer-rotationx` in the default row set.
- The row isolates the rotationX graphics-layer probe by disabling unrelated primitive/gradient/effect probes, so the
  old/new diff is focused on layer transform, clip, and paint-order fidelity.
- Magic Jewel `README.md` documents graphics-layer rotationX as part of the deterministic old/new window-capture parity
  suite.
- `ROADMAP.md` records the focused parity run and narrows remaining graphics-layer 3D work to additional transform
  coverage rather than the first rotationX gate.

Verification:

- Magic Jewel screenshot parity script syntax passed:
  - command: `bash -n scripts/jbr-skia-screenshot-parity-suite.sh scripts/jbr-skia-screenshot-parity.sh`
- Focused Magic Jewel rotationX screenshot parity row passed:
  - command: `CASES="parity-graphics-layer-rotationx" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-163641/suite.tsv`
  - result: `status=passed`, `avg_delta=2.191`, `bad_pixel_ratio=0.05199`,
    `compose_bad_pixel_ratio=0.07606`, `compose_bottom_swatches_bad_pixel_ratio=0.00000`

Next checkpoint:

- Run a compact graphics-layer matrix that includes Offscreen, ModulateAlpha, shadows, and rotationX together. Then add
  rotationY as the next bounded 3D/camera row if the compact matrix stays green.

## Checkpoint: Compact Graphics-Layer Matrix With RotationX

Status: completed as the command-family regression sweep after enabling 3D matrix replay for rotationX.

What changed:

- No code changed in this checkpoint; this is the broader validation pass for the ABI 99 graphics-layer matrix path.
- The compact Magic Jewel graphics-layer matrix now includes `commands-graphics-layer-rotationx` in the supported command
  set alongside the base layer, ModulateAlpha, Offscreen, clips, and shadow rows.
- `ROADMAP.md` records the new matrix pass so the rotationX slice has both focused and grouped validation evidence.

Verification:

- Compact Magic Jewel graphics-layer command matrix passed:
  - command: `CASES="commands-graphics-layer commands-graphics-layer-modulate-alpha commands-graphics-layer-offscreen commands-graphics-layer-rotationx commands-graphics-layer-clip commands-graphics-layer-round-clip commands-graphics-layer-path-clip commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow" DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-163854/suite.tsv`
  - result: all ten rows passed with `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`.

Next checkpoint:

- Add a Magic Jewel rotationY row that exercises the same CMP matrix path from the other 3D axis, then run command and
  screenshot parity validation for it.

## Checkpoint: Graphics-Layer RotationY Probe And Parity

Status: completed as the second bounded 3D/camera graphics-layer validation row.

What changed:

- Magic Jewel now exposes `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_ROTATION_Y` /
  `magic.jewel.compose.graphicsLayerRotationY`.
- The sample applies `rotationY = -24f` to the existing graphics-layer probe when the flag is enabled, exercising the
  same CMP `prepareTransformationMatrix(...)` + `COMMAND_CONCAT_MATRIX33` replay path from the other 3D axis.
- The command-probe suite now includes `commands-graphics-layer-rotationy`.
- The screenshot parity suite now includes `parity-graphics-layer-rotationy` in the default row set.
- Magic Jewel `README.md` and report-script help text document the rotationY probe.
- `ROADMAP.md` records both the command and old/new screenshot parity evidence.

Verification:

- Magic Jewel scripts passed syntax checks:
  - command: `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-screenshot-parity-suite.sh scripts/jbr-skia-interop-report.sh`
- Magic Jewel compiled:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon compileKotlin`
- Focused Magic Jewel rotationY command row passed:
  - command: `CASES="commands-graphics-layer-rotationy" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-164836/suite.tsv`
  - result: `status=passed`, `fallbacks=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=678`
- Focused Magic Jewel rotationY screenshot parity row passed:
  - command: `CASES="parity-graphics-layer-rotationy" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-164917/suite.tsv`
  - result: `status=passed`, `avg_delta=2.189`, `bad_pixel_ratio=0.05189`,
    `compose_bad_pixel_ratio=0.07589`, `compose_bottom_swatches_bad_pixel_ratio=0.00000`

Next checkpoint:

- Run the compact graphics-layer matrix with both rotationX and rotationY included. If it stays green, the remaining
  graphics-layer work should move away from single-axis 3D coverage and toward combined transforms, edge-case camera
  distances, and higher-fidelity shadows/image-filter surfaces.

## Checkpoint: Compact Graphics-Layer Matrix With Both 3D Axes

Status: completed as the grouped command regression sweep after adding the rotationY probe.

What changed:

- No code changed in this checkpoint; this is the grouped validation pass for the supported graphics-layer command rows.
- The compact Magic Jewel graphics-layer matrix now includes both `commands-graphics-layer-rotationx` and
  `commands-graphics-layer-rotationy`.
- `ROADMAP.md` records the both-axes matrix pass so single-axis 3D graphics-layer support has focused command,
  screenshot parity, and grouped regression evidence.

Verification:

- Compact Magic Jewel graphics-layer command matrix passed:
  - command: `CASES="commands-graphics-layer commands-graphics-layer-modulate-alpha commands-graphics-layer-offscreen commands-graphics-layer-rotationx commands-graphics-layer-rotationy commands-graphics-layer-clip commands-graphics-layer-round-clip commands-graphics-layer-path-clip commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow" DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-165236/suite.tsv`
  - result: all eleven rows passed with `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`.

Next checkpoint:

- Add a combined rotationX + rotationY Magic Jewel row to exercise matrix composition beyond single-axis transforms,
  then validate it through command probes and screenshot parity.

## Checkpoint: Combined Graphics-Layer 3D Rotation Probe

Status: completed as the first combined-axis graphics-layer 3D validation row.

What changed:

- Magic Jewel's command-probe suite now includes `commands-graphics-layer-rotationxy`, which enables both existing
  rotation flags on the same graphics-layer probe.
- Magic Jewel's screenshot parity suite now includes `parity-graphics-layer-rotationxy` in the default row set.
- The combined row exercises Compose's composed 3D layer matrix through the same ABI 99 `COMMAND_CONCAT_MATRIX33`
  replay path, covering transform composition beyond the single-axis rows.
- Magic Jewel `README.md` documents the combined-rotation parity row.
- `ROADMAP.md` records both command and old/new screenshot parity evidence.

Verification:

- Magic Jewel suite scripts passed syntax checks:
  - command: `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-screenshot-parity-suite.sh`
- Focused Magic Jewel combined rotation command row passed:
  - command: `CASES="commands-graphics-layer-rotationxy" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-165908/suite.tsv`
  - result: `status=passed`, `fallbacks=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=295`
- Focused Magic Jewel combined rotation screenshot parity row passed:
  - command: `CASES="parity-graphics-layer-rotationxy" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-165948/suite.tsv`
  - result: `status=passed`, `avg_delta=2.192`, `bad_pixel_ratio=0.05200`,
    `compose_bad_pixel_ratio=0.07609`, `compose_bottom_swatches_bad_pixel_ratio=0.00000`

Next checkpoint:

- Run the compact graphics-layer matrix with the combined rotation row included. After that, move from transform coverage
  to the next higher-risk graphics-layer fidelity gap: edge camera-distance coverage, higher-fidelity shadows, or broader
  image-filter/effect surfaces.

## Checkpoint: Compact Graphics-Layer Matrix With Combined Rotation

Status: completed as the grouped command regression sweep after adding the combined 3D rotation row.

What changed:

- No code changed in this checkpoint; this is the grouped validation pass for the expanded graphics-layer command set.
- The compact Magic Jewel graphics-layer matrix now includes `commands-graphics-layer-rotationxy` alongside the
  single-axis 3D rows, Offscreen, ModulateAlpha, clips, and shadows.
- `ROADMAP.md` records the expanded matrix pass.

Verification:

- Compact Magic Jewel graphics-layer command matrix passed:
  - command: `CASES="commands-graphics-layer commands-graphics-layer-modulate-alpha commands-graphics-layer-offscreen commands-graphics-layer-rotationx commands-graphics-layer-rotationy commands-graphics-layer-rotationxy commands-graphics-layer-clip commands-graphics-layer-round-clip commands-graphics-layer-path-clip commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow" DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-170137/suite.tsv`
  - result: all twelve rows passed with `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`.

Next checkpoint:

- Move from transform-coverage rows to the next graphics-layer fidelity gap. The current priority order is:
  edge camera-distance coverage, higher-fidelity shadows, then broader image-filter/effect surfaces.

## Checkpoint: Near-Camera Graphics-Layer 3D Probe

Status: completed as a bounded camera-distance validation row for the 3D graphics-layer matrix path.

What changed:

- Magic Jewel now exposes `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_NEAR_CAMERA` /
  `magic.jewel.compose.graphicsLayerNearCamera`.
- The sample lowers `cameraDistance` to `180f` when the flag is enabled, and the new command/parity rows combine that
  stronger perspective with rotationX + rotationY.
- The command-probe suite now includes `commands-graphics-layer-near-camera`.
- The screenshot parity suite now includes `parity-graphics-layer-near-camera` in the default row set.
- Magic Jewel `README.md` and report-script help text document the near-camera probe.
- `ROADMAP.md` records both command and old/new screenshot parity evidence.

Verification:

- Magic Jewel scripts passed syntax checks:
  - command: `bash -n scripts/jbr-skia-command-probe-suite.sh scripts/jbr-skia-screenshot-parity-suite.sh scripts/jbr-skia-interop-report.sh`
- Magic Jewel compiled:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon compileKotlin`
- Focused Magic Jewel near-camera command row passed:
  - command: `CASES="commands-graphics-layer-near-camera" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-171044/suite.tsv`
  - result: `status=passed`, `fallbacks=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=141`
- Focused Magic Jewel near-camera screenshot parity row passed:
  - command: `CASES="parity-graphics-layer-near-camera" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-171119/suite.tsv`
  - result: `status=passed`, `avg_delta=2.191`, `bad_pixel_ratio=0.05200`,
    `compose_bad_pixel_ratio=0.07609`, `compose_bottom_swatches_bad_pixel_ratio=0.00000`

Next checkpoint:

- Run the compact graphics-layer matrix with the near-camera row included. If it stays green, move to the next
  non-transform graphics-layer fidelity gap: higher-fidelity shadows or broader image-filter/effect surfaces.

## Checkpoint: Compact Graphics-Layer Matrix With Near Camera

Status: completed as the grouped command regression sweep after adding the near-camera perspective row.

What changed:

- No code changed in this checkpoint; this is the grouped validation pass for the expanded graphics-layer command set.
- The compact Magic Jewel graphics-layer matrix now includes `commands-graphics-layer-near-camera` alongside the
  single-axis and combined-axis 3D rows, Offscreen, ModulateAlpha, clips, and shadows.
- `ROADMAP.md` records the expanded matrix pass.

Verification:

- Compact Magic Jewel graphics-layer command matrix passed:
  - command: `CASES="commands-graphics-layer commands-graphics-layer-modulate-alpha commands-graphics-layer-offscreen commands-graphics-layer-rotationx commands-graphics-layer-rotationy commands-graphics-layer-rotationxy commands-graphics-layer-near-camera commands-graphics-layer-clip commands-graphics-layer-round-clip commands-graphics-layer-path-clip commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow" DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-171313/suite.tsv`
  - result: all thirteen rows passed with `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`.

Next checkpoint:

- Inspect CMP's current graphics-layer shadow command replay and either improve shadow fidelity or add a precise
  fallback/validation row for a shadow case that the current approximation should not claim yet.

## Checkpoint: Two-Pass Graphics-Layer Shadow Replay

Status: completed as a graphics-layer shadow fidelity improvement that stays within the existing command ABI.

What changed:

- CMP no longer collapses graphics-layer shadows to a single spot-colored blur pass.
- `SkiaGraphicsLayer` now passes both Compose shadow colors into the JBR command recorder:
  `ambientShadowColor` and `spotShadowColor`.
- The recorder emits two shadow passes before layer content when `shadowElevation > 0f`:
  an ambient centered blur pass, then a spot blur pass with the existing vertical offset.
- Rectangular, rounded, and generic-path shadow replay continue to use existing JBR-owned image-filter descriptors and
  saveLayer-image-filter commands, so this change does not require an ABI bump.
- Magic Jewel's screenshot oracle now allows lower cyan content pixels for shadowed clipped layer rows while still
  requiring a dedicated shadow-pixel signal.
- `ROADMAP.md` records the two-pass shadow capability and the refreshed command/parity evidence.

Verification:

- CMP focused recorder tests passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysRectangularLayerShadow --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerShadowPathBeforeShadowFill`
- Magic Jewel screenshot assertion syntax passed:
  - command: `bash -n scripts/assert-jbr-skia-command-window-screenshot.sh`
- Focused Magic Jewel shadow command subset passed:
  - command: `CASES="commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-172736/suite.tsv`
  - result: all three rows passed with `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`.
- Focused Magic Jewel shadow screenshot parity subset passed:
  - command: `CASES="parity-graphics-layer-shadow parity-graphics-layer-round-shadow parity-graphics-layer-path-shadow" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-172910/suite.tsv`
  - result:
    - `parity-graphics-layer-shadow`: `avg_delta=2.331`, `bad_pixel_ratio=0.05189`,
      `compose_bad_pixel_ratio=0.07589`
    - `parity-graphics-layer-round-shadow`: `avg_delta=2.333`, `bad_pixel_ratio=0.05203`,
      `compose_bad_pixel_ratio=0.07613`
    - `parity-graphics-layer-path-shadow`: `avg_delta=2.313`, `bad_pixel_ratio=0.05214`,
      `compose_bad_pixel_ratio=0.07631`

Next checkpoint:

- Run a compact graphics-layer matrix after the two-pass shadow change, then move to broader image-filter/effect surfaces
  if the matrix stays green.

## Checkpoint: Compact Graphics-Layer Matrix After Two-Pass Shadows

Status: completed as the grouped regression sweep for the two-pass shadow replay change.

What changed:

- No code changed in this checkpoint; this validates the complete current graphics-layer command subset after the
  ambient/spot shadow replay change.
- The compact matrix includes transforms, near-camera perspective, clips, Offscreen/ModulateAlpha, and rectangular,
  rounded, and generic-path shadow rows.
- `ROADMAP.md` records the post-shadow matrix pass.

Verification:

- Compact Magic Jewel graphics-layer command matrix passed:
  - command: `CASES="commands-graphics-layer commands-graphics-layer-modulate-alpha commands-graphics-layer-offscreen commands-graphics-layer-rotationx commands-graphics-layer-rotationy commands-graphics-layer-rotationxy commands-graphics-layer-near-camera commands-graphics-layer-clip commands-graphics-layer-round-clip commands-graphics-layer-path-clip commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow" DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-173302/suite.tsv`
  - result: all thirteen rows passed with `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`.

Next checkpoint:

- Broaden graphics-layer image-filter/effect combinations. The current CMP guard still rejects renderEffect combined with
  blend/color-filter paint metadata (`graphicsLayer:renderEffectPaint`), so the next tractable slice is to add a focused
  fallback row for that combination, then decide whether nested saveLayers can support it without an ABI bump.

## Checkpoint: Graphics-Layer RenderEffect/Paint Fallback Probe

Status: completed as a precise fallback baseline before attempting nested saveLayer support.

What changed:

- Magic Jewel's command-probe suite now includes `commands-graphics-layer-render-effect-color-filter-fallback`.
- The row enables a graphics-layer blur renderEffect plus tint/SrcIn color filter and expects the current CMP strict
  recorder to reject the combination with `graphicsLayer:renderEffectPaint`.
- Magic Jewel `README.md` documents this row as a graphics-layer render-effect/color-filter fallback probe.
- `ROADMAP.md` records the fallback baseline and adds the follow-up implementation item.

Verification:

- Magic Jewel command-suite syntax passed:
  - command: `bash -n scripts/jbr-skia-command-probe-suite.sh`
- Focused Magic Jewel expected-fallback row passed:
  - command: `CASES="commands-graphics-layer-render-effect-color-filter-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-174104/suite.tsv`
  - result: `status=passed`, `unsupported=graphicsLayer:childCommands:312,graphicsLayer:renderEffectPaint:312,graphicsLayer:624`,
    `jbr_picture_frames=311`, `jbr_command_frames=0`

Next checkpoint:

- Attempt command replay for this combination by nesting the existing image-filter saveLayer with the existing
  color-filter/blend saveLayer records, then validate command mode and screenshot parity.

## Checkpoint: Graphics-Layer RenderEffect + ColorFilter Replay

Status: completed as the first nested graphics-layer renderEffect/paint command replay slice.

What changed:

- CMP now supports graphics-layer renderEffect combined with tint/SrcIn color-filter metadata by nesting existing command
  records instead of introducing a new ABI command.
- The recorder emits the image-filter saveLayer first, then an inner color-filter saveLayer, then the layer content.
- Layer alpha is applied on the outer image-filter saveLayer for this nested case, while the inner paint saveLayer uses
  full alpha to avoid double application.
- The previous `graphicsLayer:renderEffectPaint` guard is removed for this tint color-filter case.
- Magic Jewel's command-probe row is now `commands-graphics-layer-render-effect-color-filter` and expects command replay
  plus JBR image-filter handle use instead of picture fallback.
- Magic Jewel's screenshot parity suite now includes `parity-graphics-layer-render-effect-color-filter` in the default
  row set.
- `ROADMAP.md` records the supported tint color-filter case and leaves blend/descriptor color-filter combinations as the
  next nested renderEffect work.

Verification:

- CMP focused recorder tests passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerImageFilterThenColorFilter --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerNestedImageFilterHandle --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysRectangularLayerShadow --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerShadowPathBeforeShadowFill`
- Magic Jewel command and screenshot parity suite syntax passed:
  - command: `bash -n scripts/jbr-skia-command-probe-suite.sh`
  - command: `bash -n scripts/jbr-skia-screenshot-parity-suite.sh`
- Focused Magic Jewel renderEffect + colorFilter command row passed:
  - command: `CASES="commands-graphics-layer-render-effect-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-174650/suite.tsv`
  - result: `status=passed`, `fallbacks=0`, `unsupported=none`, `jbr_picture_frames=0`,
    `jbr_command_frames=715`
- Focused Magic Jewel renderEffect + colorFilter screenshot parity row passed:
  - command: `CASES="parity-graphics-layer-render-effect-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-174829/suite.tsv`
  - result: `status=passed`, `avg_delta=2.185`, `bad_pixel_ratio=0.05176`,
    `compose_bad_pixel_ratio=0.07567`, `compose_bottom_swatches_bad_pixel_ratio=0.00000`

Next checkpoint:

- Run the compact graphics-layer command matrix with the new renderEffect/colorFilter row included, then extend nested
  renderEffect replay to blend-mode or descriptor color-filter combinations.

## Checkpoint: Graphics-Layer RenderEffect + ColorFilter Matrix

Status: completed as the compact graphics-layer regression check after nested renderEffect/tint color-filter replay.

Verification:

- Compact Magic Jewel graphics-layer command matrix passed with the renderEffect/colorFilter row included:
  - command: `CASES="commands-graphics-layer commands-graphics-layer-modulate-alpha commands-graphics-layer-offscreen commands-graphics-layer-render-effect commands-graphics-layer-render-effect-color-filter commands-graphics-layer-rotationx commands-graphics-layer-rotationy commands-graphics-layer-rotationxy commands-graphics-layer-near-camera commands-graphics-layer-clip commands-graphics-layer-round-clip commands-graphics-layer-path-clip commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow" DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-175049/suite.tsv`
  - result: all fifteen rows passed with `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`.

Next checkpoint:

- Extend nested renderEffect replay to blend-mode and descriptor color-filter combinations, with focused command rows and
  screenshot parity rows for each supported combination.

## Checkpoint: Graphics-Layer RenderEffect + Blend/Descriptor Replay

Status: completed for the current blend-mode and descriptor color-filter combinations.

What changed:

- CMP recorder unit coverage now asserts nested replay order for graphics-layer imageFilter followed by:
  - blend-mode saveLayer,
  - descriptor color-filter saveLayer,
  - blend-mode plus descriptor color-filter saveLayer.
- Magic Jewel's command suite now has focused rows for:
  - `commands-graphics-layer-render-effect-blend-mode`,
  - `commands-graphics-layer-render-effect-color-matrix-filter`,
  - `commands-graphics-layer-render-effect-blend-color-filter`,
  - `commands-graphics-layer-render-effect-blend-color-matrix-filter`.
- Magic Jewel's screenshot parity suite has matching rows for the same four combinations. Blend-heavy rows use a
  per-case `rightProbeStrip` threshold of `0.16` because the broad right-side probe is expected to carry AA/color drift
  from BlendMode.Plus under the blur renderEffect, while the command screenshot oracle and geometry/color swatches remain
  strict.

Verification:

- CMP focused recorder tests passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerImageFilterThenBlendMode --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerImageFilterThenColorMatrixFilter --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerImageFilterThenBlendModeAndColorMatrixFilter`
- Magic Jewel command-suite syntax passed:
  - command: `bash -n scripts/jbr-skia-command-probe-suite.sh`
- Magic Jewel screenshot-suite syntax passed:
  - command: `bash -n scripts/jbr-skia-screenshot-parity-suite.sh`
- Focused Magic Jewel command rows passed:
  - command: `CASES="commands-graphics-layer-render-effect-blend-mode commands-graphics-layer-render-effect-color-matrix-filter commands-graphics-layer-render-effect-blend-color-filter commands-graphics-layer-render-effect-blend-color-matrix-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-180307/suite.tsv`
  - result: all four rows passed with `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`.
- Focused Magic Jewel screenshot parity rows passed:
  - command: `CASES="parity-graphics-layer-render-effect-blend-mode parity-graphics-layer-render-effect-color-matrix-filter parity-graphics-layer-render-effect-blend-color-filter parity-graphics-layer-render-effect-blend-color-matrix-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-180743/suite.tsv`
  - result: all four rows passed; bottom swatches stayed exact (`compose_bottom_swatches_bad_pixel_ratio=0.00000`) for
    every row.

Next checkpoint:

- Run an expanded compact graphics-layer matrix including all renderEffect/blend/filter combinations, then either fold
  the rows into the broader command sweep or move to the next unsupported graphics-layer edge.

## Checkpoint: Expanded Graphics-Layer Command Matrix

Status: completed after adding nested renderEffect/blend/filter combinations.

Verification:

- Expanded Magic Jewel graphics-layer command matrix passed:
  - command: `CASES="commands-graphics-layer commands-graphics-layer-modulate-alpha commands-graphics-layer-offscreen commands-graphics-layer-render-effect commands-graphics-layer-offset-effect commands-graphics-layer-chained-render-effect commands-graphics-layer-render-effect-color-filter commands-graphics-layer-render-effect-blend-mode commands-graphics-layer-render-effect-color-matrix-filter commands-graphics-layer-render-effect-blend-color-filter commands-graphics-layer-render-effect-blend-color-matrix-filter commands-graphics-layer-rotationx commands-graphics-layer-rotationy commands-graphics-layer-rotationxy commands-graphics-layer-near-camera commands-graphics-layer-clip commands-graphics-layer-round-clip commands-graphics-layer-path-clip commands-graphics-layer-blend-mode commands-graphics-layer-color-filter commands-graphics-layer-color-matrix-filter commands-graphics-layer-blend-color-filter commands-graphics-layer-blend-color-matrix-filter commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow" DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-181133/suite.tsv`
  - result: all twenty-six rows passed with `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`.

Next checkpoint:

- Fold the expanded renderEffect/blend/filter rows into the broad command sweep result, then inspect the remaining
  unsupported graphics-path surface for the next implementation slice.

## Checkpoint: Broad Command Sweep After RenderEffect Blend Rows

Status: completed with the expanded default command-probe case list.

Verification:

- Broad Magic Jewel command-probe sweep passed:
  - command: `DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-182335/suite.tsv`
  - result: 61/61 rows passed. The new renderEffect/blend/filter rows stayed on JBR command replay with no fallback or
    picture replay. The only `jbr_picture_frames` in the suite were from the explicit `commands-invalid-gradient-fallback`
    row, which still reports the expected `sweepGradientStops` unsupported marker.

Next checkpoint:

- Inspect unsupported/fallback inventory after this sweep and pick the next implementation slice, likely a remaining
  shader/filter/path edge that currently requires picture replay or an explicit fallback row.

## Checkpoint: Broad Screenshot Parity Sweep After RenderEffect Blend Rows

Status: completed with the expanded screenshot-parity default case list.

Verification:

- Broad Magic Jewel screenshot parity suite passed:
  - command: `DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-185303/suite.tsv`
  - result: 23/23 rows passed. Coverage includes the rich mixed Swing/Compose baseline, geometry-clean baseline,
    native text, RuntimeEffect shader/color-filter rows, graphics-layer renderEffect/blend/filter rows, shadows,
    Offscreen, and 3D rotation/camera rows. Every row kept `compose_bottom_swatches_bad_pixel_ratio=0.00000`.

Next checkpoint:

- Choose the next functionality slice from the remaining non-intentional gaps. Current automated evidence says the broad
  command and screenshot suites are green; remaining picture replay is limited to explicit fallback probes.

## Checkpoint: Offset/Chained RenderEffect + Blend/Descriptor Replay

Status: completed as broader image-filter tree coverage for graphics-layer paint metadata.

What changed:

- CMP recorder unit coverage now asserts a nested image-filter descriptor tree (blur input plus offset wrapper) can be
  replayed as an outer image-filter saveLayer followed by an inner blend+descriptor-color-filter saveLayer.
- Magic Jewel command and screenshot parity suites now include:
  - `commands-graphics-layer-offset-effect-blend-color-matrix-filter`,
  - `commands-graphics-layer-chained-render-effect-blend-color-matrix-filter`,
  - `parity-graphics-layer-offset-effect-blend-color-matrix-filter`,
  - `parity-graphics-layer-chained-render-effect-blend-color-matrix-filter`.

Verification:

- CMP focused recorder test passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerNestedImageFilterThenBlendModeAndColorMatrixFilter`
- Magic Jewel command-suite syntax passed:
  - command: `bash -n scripts/jbr-skia-command-probe-suite.sh`
- Magic Jewel screenshot-suite syntax passed:
  - command: `bash -n scripts/jbr-skia-screenshot-parity-suite.sh`
- Focused Magic Jewel command rows passed:
  - command: `CASES="commands-graphics-layer-offset-effect-blend-color-matrix-filter commands-graphics-layer-chained-render-effect-blend-color-matrix-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-190948/suite.tsv`
  - result: both rows passed with `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`.
- Focused Magic Jewel screenshot parity rows passed:
  - command: `CASES="parity-graphics-layer-offset-effect-blend-color-matrix-filter parity-graphics-layer-chained-render-effect-blend-color-matrix-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-191109/suite.tsv`
  - result: both rows passed and kept exact bottom-swatches parity.

Next checkpoint:

- Fold the offset/chained renderEffect paint rows into the expanded graphics-layer command matrix and broad screenshot
  parity suite.

## Checkpoint: Expanded Graphics-Layer Matrix With Offset/Chained RenderEffect Paint

Status: completed after adding offset/chained renderEffect + blend/color-matrix rows.

Verification:

- Expanded Magic Jewel graphics-layer command matrix passed:
  - command: `CASES="commands-graphics-layer commands-graphics-layer-modulate-alpha commands-graphics-layer-offscreen commands-graphics-layer-render-effect commands-graphics-layer-offset-effect commands-graphics-layer-chained-render-effect commands-graphics-layer-render-effect-color-filter commands-graphics-layer-render-effect-blend-mode commands-graphics-layer-render-effect-color-matrix-filter commands-graphics-layer-render-effect-blend-color-filter commands-graphics-layer-render-effect-blend-color-matrix-filter commands-graphics-layer-offset-effect-blend-color-matrix-filter commands-graphics-layer-chained-render-effect-blend-color-matrix-filter commands-graphics-layer-rotationx commands-graphics-layer-rotationy commands-graphics-layer-rotationxy commands-graphics-layer-near-camera commands-graphics-layer-clip commands-graphics-layer-round-clip commands-graphics-layer-path-clip commands-graphics-layer-blend-mode commands-graphics-layer-color-filter commands-graphics-layer-color-matrix-filter commands-graphics-layer-blend-color-filter commands-graphics-layer-blend-color-matrix-filter commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow" DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-191346/suite.tsv`
  - result: all twenty-eight rows passed with `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`.

Next checkpoint:

- Run the broad screenshot parity suite with the offset/chained renderEffect paint rows included, then run the default
  broad command sweep again if visual parity remains green.

## Checkpoint: Broad Screenshot Parity With Offset/Chained RenderEffect Paint

Status: completed with the updated screenshot-parity default case list.

Verification:

- Broad Magic Jewel screenshot parity suite passed:
  - command: `DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-192628/suite.tsv`
  - result: 25/25 rows passed. The two new offset/chained renderEffect + blend/color-matrix rows passed, and every row
    kept exact bottom-swatches parity.

Next checkpoint:

- Run the default broad command sweep with the offset/chained renderEffect paint rows included.

## Checkpoint: Broad Command Sweep With Offset/Chained RenderEffect Paint

Status: completed with the updated command-probe default case list.

Verification:

- Broad Magic Jewel command-probe sweep passed:
  - command: `DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-194132/suite.tsv`
  - result: 63/63 rows passed. The offset/chained renderEffect + blend/color-matrix rows stayed on JBR command replay
    with no fallback or picture replay. The only `jbr_picture_frames` in the suite were from the explicit
    `commands-invalid-gradient-fallback` row with the expected `sweepGradientStops` unsupported marker.

Next checkpoint:

- Reassess remaining roadmap items now that broad command and visual suites are green with the expanded renderEffect
  paint coverage.

## Checkpoint: Near-Camera 3D + Chained RenderEffect Paint Probe

Status: completed as a combined graphics-layer stress row.

What changed:

- Magic Jewel now includes a command and screenshot parity row that combines:
  - graphics-layer rotationX + rotationY,
  - near-camera perspective,
  - chained offset-of-blur renderEffect descriptor tree,
  - BlendMode.Plus saveLayer metadata,
  - color-matrix descriptor color filter.

Verification:

- Magic Jewel command-suite syntax passed:
  - command: `bash -n scripts/jbr-skia-command-probe-suite.sh`
- Magic Jewel screenshot-suite syntax passed:
  - command: `bash -n scripts/jbr-skia-screenshot-parity-suite.sh`
- Focused Magic Jewel command row passed:
  - command: `CASES="commands-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-201050/suite.tsv`
  - result: `status=passed`, `fallbacks=0`, `unsupported=none`, `jbr_picture_frames=0`.
- Focused Magic Jewel screenshot parity row passed:
  - command: `CASES="parity-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-201126/suite.tsv`
  - result: `status=passed`, `avg_delta=2.596`, `bad_pixel_ratio=0.06468`,
    `compose_bad_pixel_ratio=0.09744`, `compose_bottom_swatches_bad_pixel_ratio=0.00000`.

Next checkpoint:

- Fold this combined stress row into the expanded graphics-layer matrix and broad screenshot/command default sweeps.

## Checkpoint: Expanded Graphics-Layer Matrix With Near-Camera Stress Row

Status: completed after adding the combined near-camera/chained-renderEffect paint row.

Verification:

- Expanded Magic Jewel graphics-layer command matrix passed:
  - command: `CASES="commands-graphics-layer commands-graphics-layer-modulate-alpha commands-graphics-layer-offscreen commands-graphics-layer-render-effect commands-graphics-layer-offset-effect commands-graphics-layer-chained-render-effect commands-graphics-layer-render-effect-color-filter commands-graphics-layer-render-effect-blend-mode commands-graphics-layer-render-effect-color-matrix-filter commands-graphics-layer-render-effect-blend-color-filter commands-graphics-layer-render-effect-blend-color-matrix-filter commands-graphics-layer-offset-effect-blend-color-matrix-filter commands-graphics-layer-chained-render-effect-blend-color-matrix-filter commands-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter commands-graphics-layer-rotationx commands-graphics-layer-rotationy commands-graphics-layer-rotationxy commands-graphics-layer-near-camera commands-graphics-layer-clip commands-graphics-layer-round-clip commands-graphics-layer-path-clip commands-graphics-layer-blend-mode commands-graphics-layer-color-filter commands-graphics-layer-color-matrix-filter commands-graphics-layer-blend-color-filter commands-graphics-layer-blend-color-matrix-filter commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow" DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-201311/suite.tsv`
  - result: all twenty-nine rows passed with `fallbacks=0`, `unsupported=none`, and `jbr_picture_frames=0`.

Next checkpoint:

- Run broad screenshot parity and command default sweeps with the combined near-camera stress row included.

## Checkpoint: Broad Screenshot Parity With Near-Camera Stress Row

Status: completed after folding the combined near-camera/chained-renderEffect stress row into the default screenshot suite.

What changed:

- Magic Jewel's strict command-mode validator now tolerates small Skiko/JBR command-frame marker boundary skew at high
  frame counts: tolerance is `max(3, ceil(max(skikoCommandFrames, jbrCommandFrames) / 100))`.
- The production invariants remain strict: fallback must stay at zero for command-mode rows, `unsupported=none` is still
  required, and both Skiko/JBR picture-frame counts must stay at zero unless the row explicitly expects picture replay.

Verification:

- Magic Jewel report-script syntax passed:
  - command: `bash -n scripts/jbr-skia-interop-report.sh`
- Broad Magic Jewel screenshot parity sweep passed:
  - command: `DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-203740/suite.tsv`
  - result: all twenty-six rows passed, including the combined near-camera/chained-renderEffect stress row, with
    `compose_bottom_swatches_bad_pixel_ratio=0.00000` in every row.

Next checkpoint:

- Run the broad command-probe default sweep with the combined near-camera stress row included, then reassess remaining
  graphics-layer gaps against the roadmap.

## Checkpoint: Broad Command Sweep With Near-Camera Stress Row

Status: completed after folding the combined near-camera/chained-renderEffect stress row into the default command suite.

Verification:

- Broad Magic Jewel command-probe sweep passed:
  - command: `DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-205417/suite.tsv`
  - result: all sixty-four rows passed.
- Strict command rows stayed on JBR command replay with `fallback_new_count=0`, `unsupported=none`, and
  `jbr_picture_frames=0`.
- The only picture replay was the explicit `commands-invalid-gradient-fallback` row, with the expected unsupported
  markers `sweepGradientStops`, `graphicsLayer:childCommands`, and `graphicsLayer`.

Next checkpoint:

- Reassess the remaining graphics-layer gaps. The practical next candidates are elevation-accurate shadow semantics,
  screenshot parity coverage for any still-command-only rows, and a quieter benchmark pass once functionality changes pause.

## Checkpoint: Focused PathEffect Screenshot Parity Row

Status: completed as a focused visual regression row for path-effect command replay.

What changed:

- Magic Jewel's screenshot parity suite now includes `parity-path-effect`.
- The row isolates dash, corner, stamped, and chained path-effect drawing by disabling unrelated blend/filter/path/arc/
  round-rect/gradient probes while keeping the old/new window comparison.
- Magic Jewel `README.md` documents the focused path-effect parity row.

Verification:

- Magic Jewel suite syntax passed:
  - command: `bash -n scripts/jbr-skia-screenshot-parity-suite.sh`
- Magic Jewel report syntax passed:
  - command: `bash -n scripts/jbr-skia-interop-report.sh`
- Focused Magic Jewel path-effect screenshot parity row passed:
  - command: `CASES="parity-path-effect" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-213130/suite.tsv`
  - result: `status=passed`, `avg_delta=2.208`, `bad_pixel_ratio=0.05275`,
    `compose_bad_pixel_ratio=0.07736`, `compose_bottom_swatches_bad_pixel_ratio=0.00000`.

Next checkpoint:

- Run the broad default screenshot parity suite with `parity-path-effect` included, then continue with the next
  remaining functionality gap.

## Checkpoint: Broad Screenshot Parity With PathEffect Row

Status: completed after adding `parity-path-effect` to the default screenshot suite.

Verification:

- Broad Magic Jewel screenshot parity sweep passed:
  - command: `DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-213341/suite.tsv`
  - result: all twenty-seven rows passed with `compose_bottom_swatches_bad_pixel_ratio=0.00000` in every row.

Next checkpoint:

- Continue with the next remaining functionality gap. Current candidates are exact shadow/elevation semantics or
  additional descriptor lifecycle/old-runtime compatibility tests.

## Checkpoint: High-Word Capability Compatibility Matrix

Status: completed as a stricter launch-level compatibility guard for newer descriptor capabilities.

What changed:

- Magic Jewel now propagates `SKIKO_REQUIRED_COMMAND_CAPABILITIES_HIGH_FOR_TEST` through `run-jbr-skia.sh` into
  `-Dskiko.jbr.interop.requiredCommandCapabilitiesHighForTest=...`.
- `jbr-skia-compatibility-matrix.sh` now validates high-word `command-capability-mismatch` instead of the old low-word
  mismatch probe.
- The low 64-bit capability word is saturated in this PoC, so requiring `-1` on the low word is no longer a valid
  negative test. The high word still has spare bits and is the useful forward-compatibility guard for new descriptor
  families.
- Magic Jewel `README.md` and report-script help document the high-word test property.

Verification:

- Magic Jewel launch/report/matrix syntax passed:
  - command: `bash -n scripts/run-jbr-skia.sh`
  - command: `bash -n scripts/jbr-skia-interop-report.sh`
  - command: `bash -n scripts/jbr-skia-compatibility-matrix.sh`
- Magic Jewel compatibility matrix passed:
  - command: `DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-compatibility-matrix.sh`
  - output root: `/Users/rock3r/src/magic-jewel/out/jbr-skia-compatibility-matrix/20260501-215807`
  - result: happy path produced JBR command frames; `abi-mismatch`, `native-abi-mismatch`,
    `command-capability-high-mismatch`, and `public-api-missing` each emitted one structured fallback marker and zero
    JBR command frames.

Next checkpoint:

- Continue with renderer functionality. Exact shadow/elevation semantics remains the largest visible fidelity gap.

## Checkpoint: Direct Skia Graphics-Layer Shadows and RuntimeEffect Crash Hardening

Status: completed as a focused renderer-fidelity and native-stability slice.

What changed:

- CMP records graphics-layer elevation shadows as `COMMAND_DRAW_SHADOW_PATH` when the JBR/Skiko high-word capability is
  available, carrying ambient/spot colors, z-plane, light position/radius, alpha flags, fill type, and serialized path
  commands. Older runtimes still fall back to the previous blur-pass approximation or old Swing rendering path.
- JBR API and Skiko capability negotiation now include `COMMAND_CAP64_HIGH_DRAW_SHADOW_PATH`, keeping the new command
  behind strict compatibility gating instead of silently sending it to old runtimes.
- JBR native command replay reconstructs the path inside JBR-owned Skia and calls `SkShadowUtils::DrawShadow`; the Java2D
  fallback decoder validates and consumes the same payload so malformed command streams fail predictably.
- Magic Jewel command and screenshot suites now validate rectangular, rounded, and generic-path graphics-layer shadows
  with zero picture replay and zero fallback markers.
- JBR native RuntimeEffect shader replay now validates positional child counts and child types before assigning builder
  children, converting the previously observed `SkRuntimeEffectBuilder::BuilderChild` abort into a structured
  `JBR_SKIA_INTEROP_RUNTIME_EFFECT_BUILD_FAILED` fallback.

Verification:

- JBR local Skia interop artifacts rebuilt successfully:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
- Skiko patched artifact published locally:
  - command: `./gradlew --no-daemon --no-configuration-cache publishToMavenLocal`
- CMP command-recorder suite passed for ABI 99:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
  - result: 104 tests passed.
- Focused Magic Jewel shadow command suite passed:
  - command: `CASES="commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-223410/suite.tsv`
  - result: each row stayed on JBR command replay with `fallback_new_count=0`, `unsupported=none`, and `jbr_picture_frames=0`.
- Focused Magic Jewel shadow screenshot parity suite passed:
  - command: `CASES="parity-graphics-layer-shadow parity-graphics-layer-round-shadow parity-graphics-layer-path-shadow" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-223626/suite.tsv`
  - result: all three rows passed with exact bottom swatches and acceptable Compose-region deltas.
- RuntimeEffect child-type crash regression passed without producing a new crash report:
  - command: `CASES="commands-runtime-effect-child-type-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-223409/suite.tsv`
  - result: structured fallback marker observed; no newer `java-2026-05-01` diagnostic report appeared.

Known test gap:

- Skiko `awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` still reaches an unrelated existing common-test
  compile issue in `RuntimeEffectTest.kt` (`makeMode` unresolved). `compileKotlinAwt` and `publishToMavenLocal` are green
  for this slice.

Next checkpoint:

- Commit this direct-shadow/crash-hardening slice across JBR, Runtime API, Skiko, CMP, and Magic Jewel, then run a broad
  command/screenshot sweep with the direct shadow command included before choosing the next remaining graphics-layer gap.

## Checkpoint: Dynamic Root Lighting for Direct Shadow Commands

Status: completed as a CMP-only fidelity refinement on top of `COMMAND_DRAW_SHADOW_PATH`.

What changed:

- CMP command recording now carries an explicit `JbrSkiaCommandShadowContext` for graphics-layer shadow replay.
- Nested graphics-layer command recordings inherit the active shadow context, so layer-local recording still emits shadow
  payloads using the root scene's lighting configuration.
- `ComposeSceneMediator.renderJbrSkiaCommandFrameInfo(...)` computes the same style of root-light geometry used by
  Skiko render-node replay: window-relative light X/Y, density-scaled light radius, dynamic light Z adjustment based on
  the smallest container dimension, and the ambient/spot shadow alpha factors.
- The JBR and Skiko ABI did not need to change; the existing direct-shadow payload already had fields for light position,
  radius, and alpha-scaled colors.

Verification:

- Focused CMP shadow recorder tests passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysRectangularLayerShadow --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.nestedRecordingReplaysLayerShadowPathBeforeShadowFill`
- Full CMP command-recorder suite passed:
  - command: `SKIKO_VERSION=0.0.0-SNAPSHOT ./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest`
- Broad Magic Jewel command-probe suite passed with direct-shadow rows included:
  - command: `DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-223956/suite.tsv`
- Focused Magic Jewel shadow screenshot parity suite passed with dynamic lighting:
  - command: `CASES="parity-graphics-layer-shadow parity-graphics-layer-round-shadow parity-graphics-layer-path-shadow" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260501-230926/suite.tsv`
  - result: all three rows passed; exact bottom swatches remained at `0.00000` bad-pixel ratio.

Next checkpoint:

- Commit the CMP dynamic-lighting slice, then continue with the remaining graphics-layer/image-filter or compatibility
  gaps from `ROADMAP.md`.

## Checkpoint: Direct Shadow Replay Metrics

Status: completed as a harness-hardening slice for the direct Skia shadow command.

What changed:

- JBR native command replay counts successful `COMMAND_DRAW_SHADOW_PATH` executions in the existing
  `JBR_SKIA_INTEROP_COMMAND_TIMING` marker as `shadowCommands=<n>`.
- Magic Jewel reports mirror the maximum observed value as `jbr_shadow_commands_max` in `summary.properties`.
- The rectangular, rounded, and generic-path graphics-layer shadow command rows now require
  `EXPECT_MIN_JBR_SHADOW_COMMANDS=1`, proving that they reached JBR's direct `SkShadowUtils::DrawShadow` replay path
  instead of merely avoiding picture fallback.
- The report validator has pass/fail unit coverage for the new metric, and the old Bash 3.2 regex fallback gate was
  replaced with a `case` statement after the screenshot sweep exposed the parser issue.

Verification:

- JBR local Skia interop artifacts rebuilt successfully:
  - command: `./scripts/rebuild-jbr-skia-local-artifacts.sh`
- Magic Jewel report-validation tests passed:
  - command: `scripts/test-jbr-skia-report-validation.sh`
- Focused Magic Jewel direct-shadow metric suite passed:
  - command: `CASES="commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-232255/suite.tsv`
  - result: all three rows passed with `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and
    `jbr_shadow_commands_max=1`.
- Crash-report check remained clean after the earlier RuntimeEffect hardening:
  - newest matching report is still `/Users/rock3r/Library/Logs/DiagnosticReports/java-2026-05-01-045659.ips`.

Next checkpoint:

- Commit the metric slice, then continue renderer functionality work from `ROADMAP.md`, with the strongest candidates being
  the remaining generic shader/color-filter coverage and broader graphics-layer image-filter surfaces.

## Checkpoint: Descriptor Version Fallback Probe

Status: completed as a strict compatibility regression slice.

What changed:

- Skiko has a test-only `skiko.jbr.interop.corruptDescriptorVersionForTesting` hook that mutates one shader descriptor
  version after CMP recording but before JBR command submission.
- Magic Jewel exposes the hook as `MAGIC_JEWEL_CORRUPT_DESCRIPTOR_VERSION=true` and adds
  `commands-invalid-descriptor-version-fallback` to the command probe suite.
- The new row uses a RuntimeEffect shader descriptor, corrupts the descriptor version, and requires the structured
  `command-stream-invalid` fallback with zero JBR command frames and zero picture frames.

Verification:

- Magic Jewel script syntax checks passed:
  - command: `bash -n scripts/run-jbr-skia.sh`
  - command: `bash -n scripts/jbr-skia-interop-report.sh`
  - command: `bash -n scripts/jbr-skia-command-probe-suite.sh`
- Skiko AWT compile and local publish passed:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt`
  - command: `./gradlew --no-daemon --no-configuration-cache publishToMavenLocal`
- Focused descriptor-version fallback row passed:
  - command: `CASES="commands-invalid-descriptor-version-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-233237/suite.tsv`
  - result: `SKIKO_JBR_INTEROP_DESCRIPTOR_VERSION_CORRUPTED` and `SKIKO_JBR_INTEROP_FALLBACK reason=command-stream-invalid` observed; `jbr_command_frames=0`.
- Neighboring invalid-stream fallback rows passed together:
  - command: `CASES="commands-invalid-descriptor-use-fallback commands-invalid-descriptor-version-fallback commands-runtime-effect-child-type-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-233318/suite.tsv`

Next checkpoint:

- Commit the descriptor-version fallback slice in Skiko, Magic Jewel, and JBR docs, then continue with remaining
  descriptor lifecycle/version tests or the next renderer coverage gap.

## Checkpoint: Descriptor Use-After-Evict Fallback Probe

Status: completed as a stale-handle compatibility regression slice.

What changed:

- Skiko has a test-only `skiko.jbr.interop.corruptDescriptorUseAfterEvictForTesting` hook that inserts a
  `COMMAND_EVICT_SHADER_HANDLE` for the same handle immediately before a `COMMAND_FILL_RECT_SHADER_REF` use.
- Magic Jewel exposes the hook as `MAGIC_JEWEL_CORRUPT_DESCRIPTOR_USE_AFTER_EVICT=true` and adds
  `commands-invalid-descriptor-use-after-evict-fallback` to the command probe suite.
- The new row proves JBR rejects stale/use-after-free shader handles with structured `command-stream-invalid` fallback
  instead of replaying with an invalid descriptor.

Verification:

- Magic Jewel script syntax checks passed:
  - command: `bash -n scripts/run-jbr-skia.sh`
  - command: `bash -n scripts/jbr-skia-interop-report.sh`
  - command: `bash -n scripts/jbr-skia-command-probe-suite.sh`
- Skiko AWT compile and local publish passed:
  - command: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt publishToMavenLocal`
- Focused stale-handle fallback row passed:
  - command: `CASES="commands-invalid-descriptor-use-after-evict-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-233924/suite.tsv`
  - result: `SKIKO_JBR_INTEROP_DESCRIPTOR_USE_AFTER_EVICT_CORRUPTED` and
    `SKIKO_JBR_INTEROP_FALLBACK reason=command-stream-invalid` observed; `jbr_command_frames=0`.
- Descriptor invalid-stream group passed:
  - command: `CASES="commands-invalid-descriptor-use-fallback commands-invalid-descriptor-use-after-evict-fallback commands-invalid-descriptor-version-fallback commands-runtime-effect-child-type-fallback" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-234018/suite.tsv`

Broad validation after commit:

- Broad Magic Jewel command-probe suite passed with descriptor-version and stale-handle fallback rows included in the
  default case list:
  - command: `DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
  - suite: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260501-234428/suite.tsv`
  - result: all rows passed. The only picture replay remains the intentional `commands-invalid-gradient-fallback` row;
    descriptor invalid-stream rows each produced structured fallback with zero JBR command frames.

Next checkpoint:

- Continue with remaining descriptor lifecycle/version tests or renderer coverage.

## Checkpoint: Shader Descriptor Color-Filter Wrappers

Status: completed for the first RuntimeEffect shader + typed color-filter command path.

What changed:
- JBR/JBR API define `COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_COLOR_FILTER` and `COMMAND_SHADER_DESCRIPTOR_COLOR_FILTER`.
- CMP records a wrapped shader descriptor when a supported shader paint also has a supported color filter, defining the child shader handle and color-filter handle before the wrapper.
- JBR Java and native validation reject missing child shader/color-filter handles; native replay rebuilds the child shader and applies the color filter via `SkShader::makeWithColorFilter`.
- Skiko now requires the new high capability bit, so old JBRs fall back through `command-capability-mismatch` instead of silently dropping the filter.
- Magic Jewel has `commands-runtime-effect-shader-color-filter` and `MAGIC_JEWEL_COMPOSE_RUNTIME_EFFECT_SHADER_COLOR_FILTER` for live validation.

Validation:
- JBR local artifacts rebuilt with `./scripts/rebuild-jbr-skia-local-artifacts.sh` from Magic Jewel.
- Skiko `compileKotlinAwt publishToMavenLocal` passed.
- Skiko focused JBR interop tests passed: `./gradlew --no-daemon --no-configuration-cache compileKotlinAwt awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- CMP `:compose:ui:ui-graphics:compileKotlinDesktop` passed. The broader focused `desktopTest` run remains blocked by the existing local `ui` module command-delegate wiring, after `ui-graphics` itself compiles.
- Magic Jewel focused command probe passed: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-003307/suite.tsv`.

## Checkpoint: RuntimeEffect Shader + Color-Filter Screenshot Parity

Status: completed as a focused window-parity smoke row.

What changed:
- Magic Jewel's named screenshot parity suite now includes `parity-runtime-effect-shader-color-filter`.
- The row enables `MAGIC_JEWEL_COMPOSE_RUNTIME_EFFECT_SHADER_COLOR_FILTER=true`, exercising the same wrapped shader descriptor path as the command probe while comparing frozen old/new window captures.
- The README documents the row with the rest of the RuntimeEffect parity coverage.
- `ROADMAP.md` marks the shader + color-filter parity item complete and records the report location.

Validation:
- Focused parity command passed:
  `CASES="parity-runtime-effect-shader-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
- Suite result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-003744/suite.tsv`.
- Report result: validation passed, zero fallback markers, zero unsupported commands, `jbr_command_frames=485`, and `screenshot_parity_badPixelRatio=0.05006` under the row's `0.06` full-window threshold.
- Caveat: this row is a smoke/parity guard, not a pixel-perfect oracle. The visible diff is dominated by animation phase, AA, and text-sensitive regions; broader geometry/color subregion gates remain the better signal for missing command replay.

## Checkpoint: Shader Color-Filter Capability Mismatch Guard

Status: completed for Skiko's strict compatibility gate.

What changed:
- Skiko now has a focused unit fixture for the exact old-runtime shape that matters for the new wrapped-shader feature: the fake JBR service advertises all previous high-word command capabilities but omits `COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_COLOR_FILTER`.
- Discovery rejects that runtime with `FallbackReason.COMMAND_CAPABILITY_MISMATCH` and the structured `SKIKO_JBR_INTEROP_FALLBACK reason=command-capability-mismatch` marker.

Validation:
- `./gradlew --no-daemon --no-configuration-cache awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest` passed in `/Users/rock3r/src/skiko-jbr-skia-poc/skiko`.

## Checkpoint: Linear-Gradient Shader + Color-Filter Wrapper Probe

Status: completed for the first non-RuntimeEffect wrapped-shader family.

What changed:
- CMP has a recorder regression for a normal `LinearGradientShader` with `ColorFilter.tint(..., BlendMode.SrcIn)`, proving the generic `ShaderDescriptor.ColorFiltered` path is not RuntimeEffect-specific.
- Magic Jewel adds `MAGIC_JEWEL_COMPOSE_LINEAR_GRADIENT_SHADER_COLOR_FILTER` / `magic.jewel.compose.linearGradientShaderColorFilter` and a named command-probe row, `commands-linear-gradient-shader-color-filter`.
- The Magic Jewel row asserts at least two shader handle definitions, at least one shader handle use, and at least one effect handle definition, so it proves the child shader, wrapper shader, and tint descriptor all reach JBR-owned replay.

Validation:
- Magic Jewel compile passed: `./gradlew --no-daemon --no-configuration-cache :compileKotlin`.
- Magic Jewel focused command probe passed:
  `CASES="commands-linear-gradient-shader-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`.
- Suite result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-004855/suite.tsv`.
- CMP `:compose:ui:ui-graphics:compileKotlinDesktop` passed. `:compose:ui:ui-graphics:compileTestKotlinDesktop` is still blocked by the existing local `compose.ui` command-delegate wiring failure before the new recorder test can execute.

## Checkpoint: Composite Shader + Color-Filter Wrapper Probe

Status: completed for nested shader descriptor trees under the color-filter wrapper.

What changed:
- CMP has a recorder regression for `CompositeShader(linearGradient, radialGradient, SrcOver)` plus `ColorFilter.tint(..., BlendMode.SrcIn)`, proving the wrapper can sit above a descriptor tree with multiple child shader handles.
- Magic Jewel adds `MAGIC_JEWEL_COMPOSE_COMPOSITE_SHADER_COLOR_FILTER` / `magic.jewel.compose.compositeShaderColorFilter` and a named command-probe row, `commands-composite-shader-color-filter`.
- The Magic Jewel row asserts at least four shader handle definitions, at least one shader handle use, and at least one effect handle definition.

Validation:
- Magic Jewel compile passed: `./gradlew --no-daemon --no-configuration-cache :compileKotlin`.
- CMP `:compose:ui:ui-graphics:compileKotlinDesktop` passed.
- Magic Jewel focused command probe passed:
  `CASES="commands-composite-shader-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`.
- Suite result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-005357/suite.tsv`.

## Checkpoint: Image Shader + Color-Filter Wrapper Probe

Status: completed for image-backed shader descriptor trees under the color-filter wrapper.

What changed:
- CMP has a recorder regression for `ImageShader(...)` plus `ColorFilter.tint(..., BlendMode.SrcIn)`, proving image shader descriptors can be wrapped without bypassing the descriptor/effect handle path.
- Magic Jewel adds `MAGIC_JEWEL_COMPOSE_IMAGE_SHADER_COLOR_FILTER` / `magic.jewel.compose.imageShaderColorFilter` and a named command-probe row, `commands-image-shader-color-filter`.
- The Magic Jewel row asserts image refs plus shader/effect handle markers, so it catches both image-cache and wrapper-descriptor regressions.
- Initial validation exposed that the new flag was missing from `imageProbe` creation; after adding it to the `remember(...)` keys and creation predicate, the row produced the expected handle markers.

Validation:
- Magic Jewel compile passed: `./gradlew --no-daemon --no-configuration-cache :compileKotlin`.
- CMP `:compose:ui:ui-graphics:compileKotlinDesktop` and `:compose:ui:ui-graphics:desktopJar` passed.
- Magic Jewel focused command probe passed:
  `CASES="commands-image-shader-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`.
- Suite result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-010440/suite.tsv`.

## Checkpoint: Wrapped Shader + Color-Filter Screenshot Parity Rows

Status: completed as focused window-parity smoke rows.

What changed:
- Magic Jewel's named screenshot parity suite now includes:
  - `parity-image-shader-color-filter`
  - `parity-composite-shader-color-filter`
  - `parity-linear-gradient-shader-color-filter`
- These rows complement the existing `parity-runtime-effect-shader-color-filter` row, so each stable wrapped-shader family now has both command-probe and old/new screenshot coverage.
- The README documents shader-plus-color-filter parity coverage alongside the RuntimeEffect and graphics-layer parity rows.

Validation:
- Focused parity command passed:
  `CASES="parity-image-shader-color-filter parity-composite-shader-color-filter parity-linear-gradient-shader-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
- Suite result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-010715/suite.tsv`.
- Results:
  - image shader + color filter: `bad_pixel_ratio=0.05002`, `compose_bad_pixel_ratio=0.07454`
  - composite shader + color filter: `bad_pixel_ratio=0.04991`, `compose_bad_pixel_ratio=0.07435`
  - linear gradient shader + color filter: `bad_pixel_ratio=0.04997`, `compose_bad_pixel_ratio=0.07446`
- Caveat: these rows remain smoke parity guards. They share the current shader parity noise floor dominated by animation phase, AA, and text-sensitive regions; they are useful for detecting missing replay paths but not yet for pixel-perfect evaluation.

## Checkpoint: Wrapped Shader Local Screenshot Gates

Status: completed for tighter local parity coverage.

What changed:
- Magic Jewel's screenshot comparator now emits three additional local regions:
  - `composeShaderImage`
  - `composeShaderComposite`
  - `composeShaderLinear`
- The matching parity rows opt into local thresholds:
  - image shader + color filter: `MAX_COMPOSE_SHADER_IMAGE_BAD_PIXEL_RATIO=0.05`
  - composite shader + color filter: `MAX_COMPOSE_SHADER_COMPOSITE_BAD_PIXEL_RATIO=0.07`
  - linear-gradient shader + color filter: `MAX_COMPOSE_SHADER_LINEAR_BAD_PIXEL_RATIO=0.05`
- The README documents the local gate environment variables for the shader-plus-color-filter rows.

Validation:
- Focused parity command passed:
  `CASES="parity-image-shader-color-filter parity-composite-shader-color-filter parity-linear-gradient-shader-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
- Suite result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-011323/suite.tsv`.

Next:
- Continue moving high-value unsupported surfaces from picture fallback to command replay, prioritizing paths that affect Jewel/CMP real apps.
- Add old-artifact matrix coverage for the new high-word shader color-filter capability when an actual pre-wrapper JBR artifact bundle is available.

## Checkpoint: Wrapped Shader Local Parity Reporting

Status: completed for automation visibility.

What changed:
- Magic Jewel's screenshot parity suite `suite.tsv` now includes the local shader probe ratios:
  - `compose_shader_image_bad_pixel_ratio`
  - `compose_shader_composite_bad_pixel_ratio`
  - `compose_shader_linear_bad_pixel_ratio`
- The README documents that the parity suite exposes core Compose geometry and shader-probe ratios.
- `ROADMAP.md` now marks broader wrapped-shader screenshot parity rows complete, matching the already-passing image/composite/linear shader-family rows.

Validation:
- Focused parity command passed:
  `CASES="parity-image-shader-color-filter parity-composite-shader-color-filter parity-linear-gradient-shader-color-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
- Suite result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-011838/suite.tsv`.
- The TSV contains populated local shader-ratio columns for all three rows.

Next:
- Continue the next functional command-replay gap after wrapped shader/color-filter coverage, likely either remaining descriptor lifecycle/version matrix coverage or a high-value graphics/image-filter surface still falling back to picture replay.

## Checkpoint: Image Draw Color-Filter Screenshot Parity

Status: completed for tint and color-matrix image draws.

What changed:
- Magic Jewel's command suite now asserts `commands-image-filter` produces image references, so the tint image draw row cannot pass without exercising the image command path.
- Magic Jewel's screenshot parity suite now includes:
  - `parity-image-filter`
  - `parity-image-color-matrix-filter`
- The README documents image draw color-filter parity coverage separately from shader-plus-color-filter coverage.
- `ROADMAP.md` records that image draw tint/color-matrix color-filter rows replay through command mode and have old/new screenshot parity coverage.

Validation:
- Focused command probe passed:
  `CASES="commands-image-filter commands-image-color-matrix-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
- Command suite result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-012429/suite.tsv`.
- Focused screenshot parity passed:
  `CASES="parity-image-filter parity-image-color-matrix-filter" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
- Screenshot suite result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-012646/suite.tsv`.

Next:
- Reconcile the remaining color-filter roadmap umbrella with the already-passing RuntimeEffect/generic-shader/image-draw rows, then keep closing descriptor lifecycle and compatibility-matrix gaps.

## Checkpoint: Shader Descriptor Resize/Context Redefine Probes

Status: completed for RuntimeEffect shader handles.

What changed:
- Magic Jewel's command suite now includes:
  - `commands-resize-shader-descriptor-redefine`
  - `commands-forced-context-shader-descriptor-redefine`
- The rows mirror the existing effect-handle lifecycle rows, but use a stable RuntimeEffect shader descriptor.
- Each row requires a surface-change marker, a command-cache-clear marker, at least two JBR shader-handle define markers, and shader-handle cache hits after the fresh define.
- The README documents shader descriptor redefinition alongside the existing effect descriptor resize/context-change rows.
- `ROADMAP.md` records shader descriptor redefine coverage for both same-context resize and forced context migration.

Validation:
- Focused command probe passed:
  `CASES="commands-resize-shader-descriptor-redefine commands-forced-context-shader-descriptor-redefine" DURATION_SECONDS=5 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
- Suite result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-013134/suite.tsv`.
- Marker checks:
  - resize row: `jbr_shader_handle_define_frames=2`, `jbr_shader_handle_cache_hit_frames=828`, `skiko_surface_change_markers=1`, `skiko_command_cache_clear_markers=1`
  - forced-context row: `jbr_shader_handle_define_frames=2`, `jbr_shader_handle_cache_hit_frames=561`, `skiko_surface_change_markers=1`, `skiko_command_cache_clear_markers=1`

Next:
- Keep tightening compatibility/version coverage, especially old/new fallback rows for newer high-word descriptor capabilities when reusable old artifact bundles are available.

## Checkpoint: Short Broad Command Sweep After Descriptor Rows

Status: completed.

What changed:
- No product code changes in this checkpoint; this was a broad validation pass after adding image draw color-filter parity rows and shader descriptor resize/context rows to the default Magic Jewel command suite.

Validation:
- Broad command probe passed:
  `DURATION_SECONDS=2 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
- Suite result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-013411/suite.tsv`.
- The newly added default rows passed inside the broad sweep:
  - `commands-image-filter`
  - `commands-image-color-matrix-filter`
  - `commands-resize-shader-descriptor-redefine`
  - `commands-forced-context-shader-descriptor-redefine`
- The only picture replay in the sweep remained the intentional `commands-invalid-gradient-fallback` row.

Next:
- Continue with compatibility/version hardening where it can be validated with current local artifacts; defer old-artifact matrix rows that require an actual pre-wrapper artifact bundle.

## Checkpoint: Exact Shader Color-Filter Capability Fallback Row

Status: completed.

What changed:
- JBR has a test-only `sun.java2d.skia.interop.commandCapabilitiesHighMaskForTest` property that masks advertised high-word command capabilities before Skiko performs compatibility checks.
- Magic Jewel propagates this as `JBR_SKIA_COMMAND_CAPABILITIES_HIGH_MASK_FOR_TEST`.
- The launch-level compatibility matrix now includes `shader-color-filter-capability-missing`, which masks high-word capabilities to `2047` so only `COMMAND_CAP64_HIGH_SHADER_DESCRIPTOR_COLOR_FILTER` is missing from the current `4095` high-word set.

Validation:
- Local JBR artifacts rebuilt successfully:
  `./scripts/rebuild-jbr-skia-local-artifacts.sh`
- Compatibility matrix passed:
  `DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-compatibility-matrix.sh`
- Matrix result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-compatibility-matrix/20260502-020745`.
- Exact row result: `shader-color-filter-capability-missing` passed with `JBR_SKIA_COMMAND_CAPABILITIES_HIGH_MASK_FOR_TEST=2047`, `fallback_new_count=1`, and `jbr_command_frames=0`.

Next:
- Commit the JBR/Magic Jewel compatibility hook and continue with the remaining version/fallback hardening that does not need old artifact bundles.

## Checkpoint: Compatibility Matrix TSV Reporting

Status: completed.

What changed:
- Magic Jewel's launch-level compatibility matrix now writes `matrix.tsv` with stable columns:
  `case`, `status`, `fallbacks`, `command_frames`, and `report`.
- The README documents the parseable matrix output.

Validation:
- Compatibility matrix passed:
  `DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-compatibility-matrix.sh`
- Matrix TSV: `/Users/rock3r/src/magic-jewel/out/jbr-skia-compatibility-matrix/20260502-021312/matrix.tsv`.
- The TSV contains all six rows: happy path, ABI mismatch, native ABI mismatch, broad high-word capability mismatch, exact shader/color-filter capability missing, and public API missing.

Next:
- Continue reducing stale naming/checklist ambiguity around rows that now replay in command mode, then return to remaining rendering-surface gaps.

## Checkpoint: RuntimeEffect Child Color-Filter Parity Recheck

Status: completed.

What changed:
- No product code changes; this reconciles roadmap wording with existing Magic Jewel coverage.
- `ROADMAP.md` now separates the still-open RuntimeEffect shader-family fallback-marker work from the completed child color-filter screenshot parity coverage.

Validation:
- Focused screenshot parity passed:
  `CASES="parity-runtime-effect-color-filter-child" DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`
- Suite result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-021903/suite.tsv`.

Next:
- Continue with remaining fallback-marker hardening and broader rendering-surface gaps.

## Checkpoint: CMP Runtime-Reflective JBR Command Delegate

Status: completed.

What changed:
- CMP no longer imports or implements patched Skiko's `org.jetbrains.skiko.jbr.JbrSkiaCommandRenderDelegate` at compile time.
- `ComposeSceneMediator` now exposes a local `JbrSkiaCommandFrameData` model with `FullScene` / `InteropOnly` frame-kind metadata.
- `SwingSkiaLayerComponent` builds a runtime `Proxy` that implements both `SkikoRenderDelegate` and Skiko's optional JBR command delegate only when the patched Skiko JBR classes are present.
- The CMP version catalog is back on the normal Skiko coordinate (`0.146.2`), avoiding the broad API mismatch from trying to compile all CMP modules against the local older `0.0.0-SNAPSHOT` Skiko artifact.

Validation:
- CMP desktop compile gate passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui:compileKotlinDesktop :compose:ui:ui-graphics:compileTestKotlinDesktop`
- Magic Jewel live command replay passed after the reflective bridge change:
  `CASES=commands-live-animation DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
- Suite result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-022941/suite.tsv`.
- Runtime markers stayed on the command path: `fallback_new_count=0`, `jbr_picture_frames=0`, `jbr_command_frames=903`.

Next:
- Commit the CMP and roadmap/plan updates, then continue with the next renderer-coverage gap from `ROADMAP.md`.

## Checkpoint: Expanded Exact High-Capability Matrix Rows

Status: completed.

What changed:
- Magic Jewel's launch-level compatibility matrix now exercises single-missing high-word capability fallbacks in addition to the broad high-word mismatch row.
- New exact rows cover missing image-filter refs, offset image-filter descriptors, chained image-filter descriptors, shader descriptor refs, RuntimeEffect color-filter descriptors, path-effect descriptors, concat matrices, direct shadows, and shader+color-filter wrappers.
- The README now documents that the matrix is a forward-compatibility guard for each high-word descriptor/capability family, not only a generic capability mismatch smoke test.

Validation:
- Expanded compatibility matrix passed:
  `DURATION_SECONDS=2 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-compatibility-matrix.sh`
- Matrix TSV: `/Users/rock3r/src/magic-jewel/out/jbr-skia-compatibility-matrix/20260502-023224/matrix.tsv`.
- Happy path stayed on command replay with `command_frames=643`.
- Every forced mismatch row emitted one structured fallback and zero command frames.

Next:
- Commit the Magic Jewel matrix and roadmap/plan updates, then continue with renderer functionality gaps rather than benchmark polish.

## Checkpoint: Low-Word Exact Capability Matrix Rows

Status: completed.

What changed:
- JBR now has a test-only `sun.java2d.skia.interop.commandCapabilitiesMaskForTest` property that masks advertised low-word command capabilities before Skiko performs compatibility checks.
- Magic Jewel propagates this as `JBR_SKIA_COMMAND_CAPABILITIES_MASK_FOR_TEST` through the report and launch scripts.
- The compatibility matrix now has exact low-word rows for missing color-matrix descriptors, lighting descriptors, saveLayer color-filter refs, image color-filter refs, and saveLayer blend+color-filter refs.
- The matrix still includes the high-word exact rows from the previous checkpoint, so low and high capability families are both guarded at launch level.

Validation:
- Local patched JBR/API/native artifacts rebuilt successfully:
  `./scripts/rebuild-jbr-skia-local-artifacts.sh`
- Expanded compatibility matrix passed:
  `DURATION_SECONDS=2 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-compatibility-matrix.sh`
- Matrix TSV: `/Users/rock3r/src/magic-jewel/out/jbr-skia-compatibility-matrix/20260502-024402/matrix.tsv`.
- Happy path stayed on command replay with `command_frames=720`.
- Every forced mismatch row emitted one structured fallback and zero command frames.

Next:
- Commit the JBR, Magic Jewel, and roadmap/plan updates, then continue with the remaining renderer-surface gaps.

## Checkpoint: drawPoints Line/Polygon Replay

Status: completed for `PointMode.Lines` and `PointMode.Polygon`; `PointMode.Points` remains an explicit fallback.

What changed:
- CMP no longer marks every `Canvas.drawPoints(...)` call unsupported.
- `PointMode.Lines` and `PointMode.Polygon` now record through the existing `COMMAND_STROKE_LINE` path, preserving stroke width/cap/join/miter metadata and avoiding a command ABI bump.
- `PointMode.Points` and raw point-dot drawing still emit explicit `points` / `rawPoints` unsupported markers until we add a command that can preserve Skia point-cap semantics directly.
- Magic Jewel has a new `commands-point-lines` row that draws both line-pair and polygon point modes through the live Compose canvas.

Validation:
- Focused CMP recorder tests passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesPointLineRecords --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRawPointPolygonRecords`
- Magic Jewel live probe passed:
  `CASES=commands-point-lines DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
- Suite result: `/Users/rock3r/src/magic-jewel/out/jbr-skia-command-probe-suite/20260502-030049/suite.tsv`.
- Runtime markers: `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=292`.

Next:
- Commit the CMP, Magic Jewel, and roadmap/plan updates, then continue with either point-dot semantics or another small renderer fallback surface.

## Checkpoint: drawPoints Point-Dot Replay

Status: completed.

What changed:
- ABI 100 adds `COMMAND_DRAW_POINTS` for `Canvas.drawPoints(PointMode.Points, ...)` and raw point-dot drawing.
- JBR private API and public Runtime API mirror expose `COMMAND_CAP64_HIGH_DRAW_POINTS` and `COMMAND_DRAW_POINTS`.
- JBR validates point-count, record length, antialias flags, and stroke metadata before replay.
- Native JBR replay uses JBR-owned Skia `drawPoints(kPoints_PointMode, ...)` on the current paint-scope canvas.
- CMP records `PointMode.Points` / raw points through the new command instead of emitting `points` / `rawPoints` unsupported markers.
- Skiko requires the new high-word capability, with a focused missing-capability fallback test.
- Magic Jewel has a `commands-point-dots` row that draws round-capped point dots through Compose.

Validation:
- Skiko ABI/fallback tests passed:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Focused CMP recorder tests passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesPointRecords --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesPointLineRecords --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesRawPointPolygonRecords`
- Local artifact refresh passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/scripts/rebuild-jbr-skia-local-artifacts.sh`
- Magic Jewel live probe passed:
  `CASES=commands-point-dots DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
- Suite result: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-130938/suite.tsv`.
- Runtime markers: `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=232`, `screenshot_status=passed`.

Next:
- Commit the JBR, JBR API, Skiko, CMP, and Magic Jewel updates, then continue with the next roadmap item.

## Checkpoint: ABI 100 Capability Matrix Refresh

Status: completed.

What changed:
- Magic Jewel's launch-level compatibility matrix now computes exact high-word missing-capability masks from the ABI 100
  high-word capability set (`8191`) instead of the previous ABI 99 mask (`4095`).
- Added an exact `draw-points-capability-missing` row that masks only `COMMAND_CAP64_HIGH_DRAW_POINTS` and verifies
  Skiko takes the structured `command-capability-mismatch` fallback before any command replay.
- Magic Jewel docs now list draw-points capability fallback coverage in the compatibility matrix description.

Validation:
- Expanded compatibility matrix passed:
  `SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=3 WARMUP_SECONDS=1 ./scripts/jbr-skia-compatibility-matrix.sh`
- Matrix TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260502-132955/matrix.tsv`.
- Happy path stayed on command replay with `command_frames=801`.
- `draw-points-capability-missing` emitted one structured fallback and zero command frames.
- Every forced mismatch row emitted one structured fallback and zero command frames.

Next:
- Commit and push the Magic Jewel matrix update plus this roadmap/plan checkpoint, then continue with the next renderer
  surface gap.

## Checkpoint: drawPoints Point-Dot Screenshot Parity

Status: completed.

What changed:
- Magic Jewel's old/new screenshot parity suite has a focused `parity-point-dots` row.
- The row enables `MAGIC_JEWEL_COMPOSE_POINT_DOTS=true` so the deterministic old SwingGraphics capture and the JBR-owned
  command replay capture both exercise `drawPoints(PointMode.Points)` dot/cap rendering.
- Magic Jewel docs list point-dot parity coverage alongside the existing native-text, path-effect, shader/effect, and
  graphics-layer parity rows.

Validation:
- Focused screenshot parity row passed:
  `CASES=parity-point-dots SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=4 WARMUP_SECONDS=1 ./scripts/jbr-skia-screenshot-parity-suite.sh`
- Suite result: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-134126/suite.tsv`.
- Parity metrics: `avg_delta=2.122`, `bad_pixel_ratio=0.05042`, `compose_bad_pixel_ratio=0.07521`.
- Runtime markers in the row stayed on command replay with zero fallback, zero unsupported commands, and zero picture
  frames.

Next:
- Commit and push the Magic Jewel parity update plus this roadmap/plan checkpoint, then continue with the next rendering
  gap.

## Checkpoint: Graphics-Layer Off-Center Pivot Coverage

Status: completed as an edge-case 3D graphics-layer harness slice.

What changed:
- Magic Jewel has a `MAGIC_JEWEL_COMPOSE_GRAPHICS_LAYER_OFFCENTER_PIVOT` probe flag that moves the graphics-layer
  `transformOrigin` away from the center while keeping the existing rotationX, rotationY, and near-camera perspective
  stress path.
- The command-probe suite includes `commands-graphics-layer-offcenter-pivot`.
- The screenshot parity suite includes `parity-graphics-layer-offcenter-pivot`.
- Magic Jewel report docs and README describe the new off-center-pivot 3D graphics-layer coverage.

Validation:
- Focused command row passed:
  `CASES=commands-graphics-layer-offcenter-pivot DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
- Command suite result: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-134726/suite.tsv`.
- Runtime markers: `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=299`.
- Focused screenshot parity row passed:
  `CASES=parity-graphics-layer-offcenter-pivot SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=4 WARMUP_SECONDS=1 ./scripts/jbr-skia-screenshot-parity-suite.sh`
- Parity suite result: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-134810/suite.tsv`.
- Parity metrics: `avg_delta=2.191`, `bad_pixel_ratio=0.05201`, `compose_bad_pixel_ratio=0.07609`,
  `compose_bottom_swatches_bad_pixel_ratio=0.00000`.

Next:
- Commit and push the Magic Jewel harness update plus this roadmap/plan checkpoint, then continue with the remaining
  graphics-layer/image-filter or generic shader/effect gaps.

## Checkpoint: Expanded Graphics-Layer Matrix With Off-Center Pivot Row

Status: completed as a follow-on regression gate after adding the off-center-pivot 3D row.

Validation:
- Expanded graphics-layer command matrix passed with the new off-center-pivot row included:
  `CASES="commands-graphics-layer commands-graphics-layer-modulate-alpha commands-graphics-layer-offscreen commands-graphics-layer-render-effect commands-graphics-layer-offset-effect commands-graphics-layer-chained-render-effect commands-graphics-layer-render-effect-color-filter commands-graphics-layer-render-effect-blend-mode commands-graphics-layer-render-effect-color-matrix-filter commands-graphics-layer-render-effect-blend-color-filter commands-graphics-layer-render-effect-blend-color-matrix-filter commands-graphics-layer-offset-effect-blend-color-matrix-filter commands-graphics-layer-chained-render-effect-blend-color-matrix-filter commands-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter commands-graphics-layer-rotationx commands-graphics-layer-rotationy commands-graphics-layer-rotationxy commands-graphics-layer-near-camera commands-graphics-layer-offcenter-pivot commands-graphics-layer-clip commands-graphics-layer-round-clip commands-graphics-layer-path-clip commands-graphics-layer-blend-mode commands-graphics-layer-color-filter commands-graphics-layer-color-matrix-filter commands-graphics-layer-blend-color-filter commands-graphics-layer-blend-color-matrix-filter commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow" DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
- Suite result: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-135031/suite.tsv`.
- Result summary: 30/30 rows passed; every row had `fallback_new_count=0` and `jbr_picture_frames=0`.
- The off-center-pivot row in the matrix reported `jbr_command_frames=521`.

Next:
- Continue with the remaining graphics-layer/image-filter or generic shader/effect gaps.

## Checkpoint: Shader Descriptor Validator Fixtures

Status: completed as JBR-side ABI contract-test hardening.

What changed:
- Fixed the stale `validImageRefColorMatrixFilterHandleStream()` command-count fixture so the stream's header matches
  the actual effect-descriptor, image-define, and draw-image-ref records.
- `JBRSkiaApiTest` now has focused invalid-stream fixtures for shader descriptor validation:
  - unknown shader descriptor type,
  - unsupported shader descriptor version,
  - descriptor payload/record length mismatch,
  - fill using an undefined shader handle,
  - composite shader descriptor referencing undefined child shader handles,
  - valid shader+color-filter wrapper descriptor construction,
  - shader+color-filter wrapper descriptors referencing undefined shader or effect handles,
  - fill using a shader handle after `COMMAND_EVICT_SHADER_HANDLE`.
- These tests cover the descriptor lifecycle/version paths that the live Magic Jewel fallback rows exercise at launch
  time, but at the lower JBR validator contract boundary.

Validation:
- `git diff --check` passed in the JBR worktree.
- Refreshed local JBR artifacts with `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/scripts/rebuild-jbr-skia-local-artifacts.sh`.
- Compiled `JBRSkiaApiTest.java` against `/tmp/jbr-skia-run/desktop` plus a tiny local `JBRApi` test stub.
- Ran a reflection smoke over `assertCommandStreamValidation()` against `JBRSkiaService.isValidCommandStreamForTesting(...)`;
  all command-stream validator fixtures, including the new shader descriptor cases, passed.
- Ran an isolated reflection smoke over the new shader descriptor fixtures; all produced the expected valid/invalid result.
- Caveat: this was a local patched-class validator smoke, not a canonical jtreg run. The full `JBRSkiaApiTest.main`
  path enters the scope/native smoke after command validation and still needs a configured JBR build image for the
  standard in-tree test workflow.

Next:
- Commit and push the JBR test/docs update, then continue with the remaining descriptor lifecycle/version or renderer
  surface gaps.

## Checkpoint: Effect Descriptor Child-Reference Validator Hardening

Status: completed as JBR-side command-stream validator hardening.

What changed:
- `JBRSkiaService.isValidCommandStreamForTesting(...)` now tracks effect descriptor handle types in addition to handle
  existence.
- The test validator now rejects:
  - blur/offset image-filter descriptors with missing input image-filter handles,
  - RuntimeEffect color-filter descriptors with missing child color-filter handles,
  - RuntimeEffect color-filter descriptors that reference image-filter handles as children,
  - chained path-effect descriptors with missing path-effect child handles.
- `JBRSkiaApiTest` has valid RuntimeEffect color-filter child coverage plus invalid fixtures for those child-reference
  cases.
- Follow-up validator fixtures also cover valid offset-with-input image-filter descriptors and valid chained path-effect
  descriptors, so the test suite proves supported child-reference shapes still pass.
- Additional invalid fixtures cover child references after `COMMAND_EVICT_COLOR_FILTER_HANDLE`, proving evicted image-filter
  and path-effect handles cannot be reused as descriptor children later in the same stream.

Validation:
- `git diff --check` passed in the JBR worktree.
- Refreshed local JBR artifacts with `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/scripts/rebuild-jbr-skia-local-artifacts.sh`.
- Compiled `JBRSkiaApiTest.java` against `/tmp/jbr-skia-run/desktop` plus the local `JBRApi` test stub.
- Reflected `assertCommandStreamValidation()` against `JBRSkiaService.isValidCommandStreamForTesting(...)`; all command
  validator fixtures passed.
- Focused Magic Jewel descriptor/effect live rows passed after the stricter validator rebuild:
  `CASES="commands-runtime-effect-color-filter-child commands-graphics-layer-offset-effect commands-graphics-layer-chained-render-effect commands-path-effect-fallback commands-invalid-descriptor-use-after-evict-fallback" DURATION_SECONDS=3 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
- Suite result: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-162858/suite.tsv`.
- Supported rows stayed on command replay with `fallback_new_count=0`, `unsupported=none`, and `jbr_picture_frames=0`;
  the intentional stale-handle row emitted one structured fallback and zero command frames.

Next:
- Commit and push this JBR validator hardening, then continue with the remaining descriptor lifecycle/version or renderer
  surface gaps.

## Checkpoint: Compatibility Matrix After Descriptor Validator Hardening

Status: completed as a regression gate after tightening JBR-side effect descriptor child-reference validation.

Validation:
- Full Magic Jewel launch-level compatibility matrix passed:
  `SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=3 WARMUP_SECONDS=1 ./scripts/jbr-skia-compatibility-matrix.sh`
- Matrix TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-compatibility-matrix/20260502-163211/matrix.tsv`.
- Happy path stayed on command replay with `fallback_new_count=0` and `jbr_command_frames=327`.
- Every forced ABI/native/capability/public-API mismatch row emitted one structured fallback and zero command frames.

Next:
- Continue with the remaining descriptor lifecycle/version or renderer surface gaps.

## Checkpoint: Current Artifact Matrix After Validator Hardening

Status: completed for the required current-artifact rows.

Validation:
- Magic Jewel artifact matrix passed with the umbrella CMP output root:
  `CURRENT_CMP_OUT=/Users/rock3r/src/jbr-skia-zero-copy/cmp/out/compose-multiplatform-core SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=3 WARMUP_SECONDS=1 ./scripts/jbr-skia-artifact-matrix.sh`
- Matrix TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-164541/matrix.tsv`.
- `current-all` passed with `fallback_new_count=0` and `jbr_command_frames=284`.
- `missing-public-api` passed with expected `public-api-missing`, one structured fallback, and zero command frames.
- Optional old-artifact rows were skipped because no `OLD_*` artifact paths or bundle were supplied.

Next:
- Keep the current-artifact row green as the default smoke. Full old/new packaged artifact coverage still needs real old
  bundles supplied for every optional row.

## Checkpoint: Artifact Bundle Optional-Row Self-Check

Status: completed for a same-version artifact bundle self-check.

Validation:
- Packaged the current ABI 100 local artifacts:
  `CMP_OUT=/Users/rock3r/src/jbr-skia-zero-copy/cmp/out/compose-multiplatform-core SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/package-jbr-skia-artifact-bundle.sh`
- Bundle: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-bundles/20260502-164733`.
- Ran the artifact matrix with that bundle supplied as `OLD_ARTIFACT_BUNDLE` and all optional expected reasons set to
  `none`, because the bundle is the current artifact set:
  `CURRENT_CMP_OUT=/Users/rock3r/src/jbr-skia-zero-copy/cmp/out/compose-multiplatform-core OLD_ARTIFACT_BUNDLE=/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-bundles/20260502-164733 OLD_JBR_EXPECTED_REASON=none OLD_API_EXPECTED_REASON=none OLD_SKIKO_EXPECTED_REASON=none OLD_CMP_EXPECTED_REASON=none SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=3 WARMUP_SECONDS=1 ./scripts/jbr-skia-artifact-matrix.sh`
- Matrix TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-164746/matrix.tsv`.
- All optional rows passed with `fallback_new_count=0` and nonzero command frames; the deliberate
  `missing-public-api` row still produced the expected structured fallback.

Next:
- Keep this bundle as a known-good current ABI 100 replay baseline. Full old/new packaged artifact coverage still needs
  a genuinely older incompatible bundle for negative rows.

## Checkpoint: Artifact Script Workspace Defaults

Status: completed as Magic Jewel harness cleanup.

What changed:
- Magic Jewel's artifact matrix and artifact bundle scripts now default the CMP output root to the sibling umbrella
  worktree path, `../cmp/out/compose-multiplatform-core`, instead of the older absolute checkout path.
- Magic Jewel README documents that sibling CMP output default in the artifact matrix section.

Validation:
- Magic Jewel `git diff --check` passed.
- Shell syntax checks passed for `scripts/jbr-skia-artifact-matrix.sh` and
  `scripts/package-jbr-skia-artifact-bundle.sh`.
- `./scripts/jbr-skia-artifact-matrix.sh --dry-run` found the current artifact roots with no `CURRENT_CMP_OUT`
  override and wrote `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-165202/matrix.tsv`.
- `./scripts/package-jbr-skia-artifact-bundle.sh` found the sibling CMP output with no `CMP_OUT` override and created
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-bundles/20260502-165209`.

Next:
- Commit and push the Magic Jewel harness cleanup plus this checkpoint, then continue with the remaining compatibility
  and rendering-surface gaps.

## Checkpoint: Report Validation Harness Recheck

Status: completed after the artifact harness cleanup.

Validation:
- Magic Jewel report-validation unit harness passed:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/scripts/test-jbr-skia-report-validation.sh`

Next:
- Continue with the remaining compatibility and rendering-surface gaps.

## Checkpoint: Broad Command Sweep After Descriptor Validator Hardening

Status: completed as the broad live command-mode regression gate after descriptor validator and artifact harness
cleanup.

Validation:
- Broad Magic Jewel command-probe suite passed:
  `DURATION_SECONDS=2 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
- Suite TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-165516/suite.tsv`.
- Summary: `total=75`, `failed=0`, `fallback_rows=5`, `picture_rows=1`.
- Supported command rows stayed on JBR command replay with zero new fallback frames and zero JBR picture frames.
- The expected structured-fallback rows remained bounded; only `commands-invalid-gradient-fallback` used picture replay,
  with the expected `sweepGradientStops`, `graphicsLayer:childCommands`, and `graphicsLayer` unsupported markers.

Next:
- Continue with the remaining compatibility and rendering-surface gaps, keeping the 75-row command sweep as the broad
  live regression baseline.

## Checkpoint: Broad Screenshot Parity After Descriptor Validator Hardening

Status: completed as the broad visual regression gate after descriptor validator and artifact harness cleanup.

Validation:
- Broad Magic Jewel screenshot parity suite passed:
  `SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=2 WARMUP_SECONDS=1 ./scripts/jbr-skia-screenshot-parity-suite.sh`
- Suite TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-172551/suite.tsv`.
- Summary: `total=35`, `failed=0`, `bottom_swatches_nonzero=0`, `max_avg_delta=3.229`,
  `max_compose_bad_pixel_ratio=0.10955`.
- The sweep covered text, point dots, path effects, image/color-filter refs, shader/color-filter wrappers,
  RuntimeEffect shader and color-filter descriptors, renderEffect graphics layers, direct shadows, 3D transforms,
  near-camera stress, and off-center pivot rows.

Next:
- Continue with the remaining compatibility and rendering-surface gaps. The command and screenshot sweeps now both cover
  the descriptor-validator hardening baseline.

## Checkpoint: Skiko Exact High-Capability Unit Gate

Status: completed as Skiko-side unit coverage for exact high-word command capability negotiation.

What changed:
- `JbrSkiaInteropTest` now rejects each missing high-word command capability independently instead of relying only on
  launch-level compatibility rows for the full high-word matrix.
- The loop covers image-filter refs/descriptors, shader descriptor refs, RuntimeEffect color-filter descriptors,
  path-effect stroke/descriptor support, concat matrices, direct shadows, shader color-filter descriptors, and
  `drawPoints`.

Validation:
- Skiko focused interop tests passed:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`

Next:
- Continue with remaining descriptor lifecycle/version checks and the larger renderer-surface gaps.

## Checkpoint: Native Metadata From Loaded Bridge

Status: completed after real ABI 99 native artifact testing exposed a render-false mismatch path.

What changed:
- `JBRSkiaService` now asks the loaded native bridge for native ABI version, command-stream ABI id, and build id when a
  native bridge library is present.
- The native bridge exports metadata JNI entry points for those values.
- If an older dylib loads but does not provide the metadata entry points, Java reports unavailable metadata, causing
  Skiko discovery to use the structured `native-abi-mismatch` fallback instead of emitting command frames that always
  return `rendered=false`.

Validation:
- Rebuilt current local artifacts with `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/scripts/rebuild-jbr-skia-local-artifacts.sh`.
- Built real ABI 99 JBR-side artifacts from detached worktrees:
  - JBR: `2d17097ad51`
  - JBR API: `7218190`
  - desktop patch: `/tmp/jbr-skia-run/abi99/desktop`
  - API shim: `/tmp/jbr-api-shim-abi99.jar`
  - native dylib: `/tmp/jbr-skia-native/abi99/libjbrskiainterop.dylib`
- Magic Jewel artifact matrix passed for current artifacts plus real ABI 99 JBR-side old rows:
  `OLD_JBR_API_SHIM=/tmp/jbr-api-shim-abi99.jar OLD_DESKTOP_PATCH=/tmp/jbr-skia-run/abi99/desktop OLD_JBR_SKIA_LIB=/tmp/jbr-skia-native/abi99/libjbrskiainterop.dylib OLD_API_EXPECTED_REASON=abi-mismatch OLD_JBR_EXPECTED_REASON=native-abi-mismatch SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=2 WARMUP_SECONDS=1 ./scripts/jbr-skia-artifact-matrix.sh`
- Matrix TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-175232/matrix.tsv`.
- `current-all` stayed on command replay with `fallback_new_count=0` and `jbr_command_frames=743`.
- `old-api-current-runtime` produced the expected `abi-mismatch` fallback.
- `old-native-current-api` and `old-desktop-current-runtime` produced the expected `native-abi-mismatch` fallback with
  zero command frames.

Remaining:
- The full old/new packaged artifact matrix item remains open until separately versioned old Skiko and old CMP artifact
  roots are available for the optional `old-skiko-current-jbr` and `old-cmp-current-jbr` rows.

Post-change broad validation:
- Broad Magic Jewel command-probe suite passed after rebuilding current artifacts with the native metadata JNI entry
  points:
  `DURATION_SECONDS=2 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`
- Suite TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-175743/suite.tsv`.
- Summary: `total=75`, `failed=0`, `fallback_rows=5`, `picture_rows=1`.
- Supported rows stayed on command replay; only the expected invalid-gradient row used picture fallback.

## Checkpoint: Skiko Command-Stream ABI Preflight

Status: completed after a real ABI 99 CMP artifact row exposed another render-false mismatch path.

What changed:
- Skiko now checks the command stream header before handing a command frame to JBR.
- A current ABI stream proceeds normally.
- A stream with the wrong command-stream ABI logs the structured `abi-mismatch` fallback and returns to picture replay
  before JBR native replay is called.
- A malformed/short stream logs the existing `command-stream-invalid` fallback before replay.
- Skiko's synthetic fallback command stream header was updated to ABI 100.

Validation:
- Skiko focused interop tests passed:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`
- Republished Skiko locally:
  `./gradlew --no-daemon --no-configuration-cache :publishToMavenLocal`
- Built real ABI 99 CMP UI desktop jars from detached worktree `c3821214032d`.
- Magic Jewel artifact matrix passed for the real old-CMP row:
  `OLD_CMP_OUT=/Users/rock3r/src/jbr-skia-zero-copy/old-artifacts/cmp-abi99/out/compose-multiplatform-core OLD_CMP_EXPECTED_REASON=abi-mismatch SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=2 WARMUP_SECONDS=1 ./scripts/jbr-skia-artifact-matrix.sh`
- Matrix TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-183016/matrix.tsv`.
- `old-cmp-current-jbr` produced the expected `abi-mismatch` fallback with `fallback_new_count=1` and
  `jbr_command_frames=0`.

Follow-up:
- The separately versioned old Skiko artifact was supplied in the next checkpoint, closing the full old/new packaged
  artifact matrix item.

## Checkpoint: Full Old/New Packaged Artifact Matrix

Status: completed for the launch-level packaged-artifact compatibility guard.

What changed:
- Published the ABI 99 Skiko worktree at commit `6fd533f90` under the non-colliding Maven version
  `0.0.0-abi99-SNAPSHOT`.
- Rebuilt the ABI 99 CMP UI desktop jars from detached worktree `c3821214032d` into
  `/Users/rock3r/src/jbr-skia-zero-copy/old-artifacts/cmp-abi99/out/compose-multiplatform-core`.
- Reused the real ABI 99 JBR-side artifacts:
  - desktop patch: `/tmp/jbr-skia-run/abi99/desktop`
  - API shim: `/tmp/jbr-api-shim-abi99.jar`
  - native dylib: `/tmp/jbr-skia-native/abi99/libjbrskiainterop.dylib`

Validation:
- Magic Jewel artifact matrix passed with all optional old-artifact rows required:
  `OLD_JBR_API_SHIM=/tmp/jbr-api-shim-abi99.jar OLD_DESKTOP_PATCH=/tmp/jbr-skia-run/abi99/desktop OLD_JBR_SKIA_LIB=/tmp/jbr-skia-native/abi99/libjbrskiainterop.dylib OLD_SKIKO_VERSION=0.0.0-abi99-SNAPSHOT OLD_CMP_OUT=/Users/rock3r/src/jbr-skia-zero-copy/old-artifacts/cmp-abi99/out/compose-multiplatform-core OLD_API_EXPECTED_REASON=abi-mismatch OLD_JBR_EXPECTED_REASON=native-abi-mismatch OLD_SKIKO_EXPECTED_REASON=abi-mismatch OLD_CMP_EXPECTED_REASON=abi-mismatch REQUIRE_OLD_ARTIFACT_ROWS=true SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=2 WARMUP_SECONDS=1 ./scripts/jbr-skia-artifact-matrix.sh`
- Matrix TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-183840/matrix.tsv`.
- `current-all` stayed on command replay with `fallback_new_count=0` and `jbr_command_frames=515`.
- `missing-public-api` produced `public-api-missing`.
- `old-api-current-runtime`, `old-skiko-current-jbr`, and `old-cmp-current-jbr` produced `abi-mismatch`.
- `old-native-current-api` and `old-desktop-current-runtime` produced `native-abi-mismatch`.
- Every negative row reported `fallback_new_count=1` and `jbr_command_frames=0`.

Next:
- Remove the temporary old-artifact worktrees after committing this checkpoint, then continue with the remaining
  descriptor lifecycle/version and renderer-surface gaps.

Roadmap closure:
- The narrower ABI/version testing row for shader/effect handle creation, use-after-free rejection, context migration
  invalidation, and old/new fallback markers is now complete. The command-probe and validator coverage already covered
  descriptor version corruption, use-after-evict rejection, child-handle validation, resize/context migration
  redefinition, and handle lifecycle marker assertions; the full artifact matrix adds the real old/new fallback marker
  coverage. The broader lifecycle-command umbrella remains open for future explicit create/use/cache/evict command
  lifecycle APIs.

## Checkpoint: Color-Filter Coverage Closure

Status: completed for the roadmap row covering image filters, RuntimeEffect filters, and generic shader wrappers.

Validation:
- Focused Magic Jewel command-probe suite passed:
  `CASES="commands-image-filter commands-image-color-matrix-filter commands-runtime-effect-color-filter commands-runtime-effect-color-filter-child commands-image-shader-color-filter commands-composite-shader-color-filter commands-linear-gradient-shader-color-filter commands-runtime-effect-shader-color-filter commands-graphics-layer-render-effect-color-filter commands-graphics-layer-render-effect-color-matrix-filter commands-graphics-layer-render-effect-blend-color-filter commands-graphics-layer-render-effect-blend-color-matrix-filter" SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=2 WARMUP_SECONDS=1 ./scripts/jbr-skia-command-probe-suite.sh`
- Suite TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-184719/suite.tsv`.
- All 12 rows reported `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and positive
  `jbr_command_frames`.

Notes:
- Image-filter coverage here is through supported RenderEffect/saveLayer and graphics-layer descriptor paths, which are
  the image-filter surfaces currently exposed by the CMP desktop pipeline.
- RuntimeEffect color filters cover both direct color-filter descriptors and child color-filter descriptors.
- Generic shader coverage covers image, composite, linear-gradient, and RuntimeEffect shader descriptors wrapped with
  typed color-filter handles.

## Checkpoint: Blend-Mode Coverage Closure

Status: completed for the remaining blend-mode command coverage row.

Ground truth:
- JBR validates every supported fill blend-mode token individually in `JBRSkiaApiTest`, including Plus, Multiply,
  Screen, Overlay, Darken, Lighten, Difference, Exclusion, ColorDodge, ColorBurn, Hardlight, Softlight, Hue,
  Saturation, Color, and Luminosity.
- JBR native replay maps those same command tokens to Skia `SkBlendMode` values, and Java2D fallback gates the same
  supported set.
- CMP command recording maps the same Compose `BlendMode` set for primitive fill rectangles and graphics-layer
  saveLayer paint paths; unsupported blend modes still take structured fallback rather than semantic guessing.

Validation:
- Focused Magic Jewel command-probe suite passed:
  `CASES="commands-blend-mode commands-graphics-layer-blend-mode commands-graphics-layer-render-effect-blend-mode commands-graphics-layer-render-effect-blend-color-filter commands-graphics-layer-render-effect-blend-color-matrix-filter commands-graphics-layer-blend-color-filter commands-graphics-layer-blend-color-matrix-filter" SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=2 WARMUP_SECONDS=1 ./scripts/jbr-skia-command-probe-suite.sh`
- Suite TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-185229/suite.tsv`.
- All 7 rows reported `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and positive
  `jbr_command_frames`.

## Checkpoint: Graphics-Layer Coverage Closure

Status: completed for the current graphics-layer command replay roadmap bucket.

Ground truth:
- CMP's real desktop render path builds `JbrSkiaCommandShadowContext` from the root container size, density, and content
  offset before recording, so shadow replay receives dynamic root-lighting metadata rather than a fixed test-only light.
- Graphics-layer replay covers nested layer-local command recording, alpha, ModulateAlpha, Offscreen, rectangular/round/path
  clips, blend modes, tint/color-matrix filters, blur/offset/chained image-filter descriptors, direct shadows, and
  rotationX/rotationY/near-camera/off-center-pivot transforms.

Validation:
- Focused Magic Jewel graphics-layer command-probe suite passed:
  `CASES="commands-graphics-layer commands-graphics-layer-modulate-alpha commands-graphics-layer-offscreen commands-graphics-layer-clip commands-graphics-layer-round-clip commands-graphics-layer-path-clip commands-graphics-layer-blend-mode commands-graphics-layer-color-filter commands-graphics-layer-color-matrix-filter commands-graphics-layer-render-effect commands-graphics-layer-offset-effect commands-graphics-layer-chained-render-effect commands-graphics-layer-render-effect-color-filter commands-graphics-layer-render-effect-blend-mode commands-graphics-layer-render-effect-color-matrix-filter commands-graphics-layer-render-effect-blend-color-filter commands-graphics-layer-render-effect-blend-color-matrix-filter commands-graphics-layer-offset-effect-blend-color-matrix-filter commands-graphics-layer-chained-render-effect-blend-color-matrix-filter commands-graphics-layer-near-camera-chained-render-effect-blend-color-matrix-filter commands-graphics-layer-shadow commands-graphics-layer-round-shadow commands-graphics-layer-path-shadow commands-graphics-layer-rotationx commands-graphics-layer-rotationy commands-graphics-layer-rotationxy commands-graphics-layer-near-camera commands-graphics-layer-offcenter-pivot commands-graphics-layer-blend-color-filter commands-graphics-layer-blend-color-matrix-filter" SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=2 WARMUP_SECONDS=1 ./scripts/jbr-skia-command-probe-suite.sh`
- Suite TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-185632/suite.tsv`.
- All 30 rows reported `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and positive
  `jbr_command_frames`.

## Checkpoint: Bundled Native Bridge Load Path

Status: completed as the runtime half of production image integration; JBR build wiring remains open.

Changes:
- `JBRSkiaService` now calls `System.loadLibrary("jbrskiainterop")` when
  `sun.java2d.skia.interop.library` is absent or blank. The explicit absolute-path property remains supported for the
  local patched-module harness.
- Magic Jewel's `run-jbr-skia.sh` still defaults to `/tmp/jbr-skia-native/libjbrskiainterop.dylib`, but an explicitly
  empty `JBR_SKIA_LIB=` now omits the property so a real JBR image can exercise its bundled native bridge.
- Magic Jewel README documents that `JBR_SKIA_LIB=` is for image-bundled validation. The current patched-class harness
  has no bundled bridge in the runtime library path, so the no-explicit-property run is expected to produce no JBR native
  command frames until JBR image build integration lands.

Validation:
- Local artifact rebuild passed via Magic Jewel's `./scripts/rebuild-jbr-skia-local-artifacts.sh`.
- Explicit local dylib command replay passed:
  `CASES=commands-core-primitives SKIKO_VERSION=0.0.0-SNAPSHOT DURATION_SECONDS=2 WARMUP_SECONDS=1 ./scripts/jbr-skia-command-probe-suite.sh`
- Suite TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-191032/suite.tsv`.
- The row reported `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=628`.
- A no-explicit-property local patched run with `JBR_SKIA_LIB=` reached Skiko command recording but logged
  `no jbrskiainterop in system library path` and produced `jbr_command_frames=0`, confirming the remaining work is
  packaging the native bridge into a real JBR image rather than another command-stream ABI change.

Next:
- Wire `libjbrskiainterop` into the JBR build/image behind the existing `--with-skia-interop` configure surface, then
  rerun the same `JBR_SKIA_LIB=` probe and require native command frames without an explicit library property.

## Checkpoint: JBR Native Bridge Make Target

Status: completed for first source-level make wiring; full image validation still needs a local JDK 26/27 boot JDK.

Changes:
- `make/modules/java.desktop/lib/AwtLibraries.gmk` now adds a macOS-only `BUILD_LIBJBRSKIAINTEROP` target when
  `SKIA_INTEROP_ENABLED=true`.
- The target builds `libjbrskiainterop` from the existing `JBRSkiaInterop.mm` source, links against `libawt_lwawt` and
  the same pinned m147 Skia static archives used by the Magic Jewel local rebuild helper, and includes the Skia core,
  Ganesh/Metal, paragraph, unicode, generated Skia, Java2D Metal, and generated Java header surfaces needed by the bridge.
- `--with-skia-interop=<Skia release root>` is the validated shape for this checkpoint. `--with-skia-interop=bundled`
  is still a placeholder for a future vendored Skia location under the JBR tree.

Validation:
- `git diff --check` passed.
- `bash configure --help=short` still lists `--with-skia-interop`.
- A full `bash configure --with-conf-name=skia-interop-poc --with-skia-interop=<m147 root>` regenerated configure
  support but stopped at the known machine prerequisite: this JBR source requires a boot JDK 26 or 27, while the local
  `/usr/libexec/java_home` candidates are JDK 21.
- Standalone build-style Objective-C++ compilation of `JBRSkiaInterop.mm` passed against the m147 Skia root when supplied
  with the generated Java headers from the local patched harness, matching the header surface the real JBR build should
  provide under `$(SUPPORT_OUTPUTDIR)/headers/java.desktop`.

Next:
- Obtain or point configure at a JDK 26/27 boot JDK and build the `java.desktop` native target to prove the new make
  target links inside the JBR image.
- After the image contains `libjbrskiainterop`, rerun Magic Jewel with `JBR_SKIA_LIB=` and require positive
  `jbr_command_frames` without an explicit native-library property.

## Checkpoint: Skia Interop Configure Shape Guard

Status: completed for external Skia release roots.

Changes:
- `--with-skia-interop=<path>` now validates the headers and macOS arm64 static archives consumed by the
  `libjbrskiainterop` make target, including core Skia, Ganesh surface headers, paragraph/unicode headers, and the key
  `out/Release-macos-arm64` archives.
- This keeps malformed or wrong-platform Skia roots from reaching a much later native link failure.
- `--with-skia-interop=bundled` remains a future vendored-source placeholder and is not declared complete by this guard.

Validation:
- `git diff --check` passed.
- `bash configure --help=short` regenerated configure support and still lists `--with-skia-interop`.

Next:
- Re-run full configure with a JDK 26/27 boot JDK so the new shape guard executes before the Java.desktop native build.

## Checkpoint: Skiko Artifact Shape Decision

Status: completed as a packaging decision; implementation of a genuinely Skia-less JBR-only runtime remains gated on
removing the remaining Skiko JNI dependencies from command recording.

Decision:
- Keep `org.jetbrains.skiko:skiko-awt` as the JVM/Kotlin API artifact used by Compose and Skiko callers.
- Keep the normal `skiko-awt-runtime-*` native artifacts for apps that need the old SwingGraphics fallback, direct Skiko
  surfaces, or any recording helper still backed by Skiko JNI.
- Do not publish an empty or marker-only `skiko-awt-runtime-jbr-*` artifact yet. That would make dependency graphs look
  Skia-less while the current recorder can still need Skiko native code for fallback and some helper surfaces.
- Package the native command replay bridge in JBR as `libjbrskiainterop`; Skiko remains the reflective ABI/metadata gate
  and command-buffer sender.

Ground truth:
- Skiko's publication model already separates `skiko-awt` from platform runtime artifacts with constraints rather than a
  hard runtime dependency, so a future JBR-only distribution can omit `skiko-awt-runtime-*` once the remaining JNI-backed
  recording surfaces are gone.
- Skiko `JBR-INTEROP.md` now records the ABI 101 gate and this artifact-shape decision.

Next:
- Track and remove the specific Skiko JNI calls still needed during Compose Swing command recording before claiming a
  runnable Skia-less Skiko runtime.

## Checkpoint: ABI 101 Simple Native Text Font Style Metadata

Status: completed.

Changes:
- `COMMAND_DRAW_TEXT_UTF16` now carries `fontWeight`, `fontWidth`, and `fontSlant` alongside the existing font-family
  and size metadata.
- JBR validates the new style fields before replay and resolves family-backed styled typefaces through its own Skia
  font manager, keeping `SkTypeface*` ownership on the JBR side.
- CMP records the default font style metadata for the simple native-text path; paragraph text continues to use its
  existing styled paragraph payload.
- Skiko and the public JBR API mirror now gate on ABI 101, and Skiko's command-stream preflight uses the shared stream
  ABI constant.

Validation:
- Skiko focused ABI/gating tests passed:
  `./gradlew --no-daemon --no-configuration-cache :awtTest --tests org.jetbrains.skiko.jbr.JbrSkiaInteropTest`.
- CMP focused recorder tests passed:
  `./gradlew --no-daemon --no-configuration-cache :compose:ui:ui-graphics:desktopTest --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesSimpleTextRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesLatin1TextRecord --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.recordsFrameMetadata --tests androidx.compose.ui.graphics.JbrSkiaCommandRecorderTest.writesParagraphTextRecord`.
- Local artifact rebuild passed via Magic Jewel's `./scripts/rebuild-jbr-skia-local-artifacts.sh`.
- After publishing patched Skiko `0.0.0-SNAPSHOT` to Maven local, the focused Magic Jewel native-text command probe
  passed:
  `CASES=commands-native-text DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`.
- Suite TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-193545/suite.tsv`.
- The row reported `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=673`.

Next:
- Continue removing remaining command-recording dependencies on Skiko-owned native objects, with typeface/font fallback
  semantics still on the watch list for no-family text and screenshot-level font parity.

## Checkpoint: Default-Family Simple Native Text Style Resolution

Status: completed as an ABI-neutral native replay polish.

Changes:
- JBR native replay now resolves empty-family `COMMAND_DRAW_TEXT_UTF16` records through the CoreText-backed Skia font
  manager with the requested `fontWeight`, `fontWidth`, and `fontSlant`.
- This replaces the previous `SkFont(nullptr, size)` path for no-family simple text, so style metadata is still applied
  by a JBR-owned `SkTypeface` without crossing any Skiko font/typeface pointer.

Validation:
- Local artifact rebuild passed via Magic Jewel's `./scripts/rebuild-jbr-skia-local-artifacts.sh`.
- Focused Magic Jewel native-text command probe passed:
  `CASES=commands-native-text DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`.
- Suite TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-193931/suite.tsv`.
- The row reported `fallback_new_count=0`, `unsupported=none`, `jbr_picture_frames=0`, and `jbr_command_frames=375`.
- Focused native-text screenshot parity also passed:
  `CASES=parity-native-text DURATION_SECONDS=4 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-screenshot-parity-suite.sh`.
- Parity suite TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-screenshot-parity-suite/20260502-194040/suite.tsv`.
- The row reported `status=passed`, `avg_delta=3.229`, `bad_pixel_ratio=0.06048`, and
  `compose_bottom_swatches_bad_pixel_ratio=0.00000`.

Next:
- Continue native text parity work with screenshot-level font/style/baseline checks before making native text a default
  replacement for text-as-image replay.

## Checkpoint: Short Broad Command Sweep After ABI 101 Text Metadata

Status: completed as a regression sweep.

Validation:
- Ran Magic Jewel's default command-probe suite with short row duration after ABI 101 simple text style metadata and the
  default-family native replay polish:
  `DURATION_SECONDS=2 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-command-probe-suite.sh`.
- Suite TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260502-194216/suite.tsv`.
- The suite passed. Supported rows stayed on command replay with `fallback_new_count=0`, `unsupported=none`,
  `jbr_picture_frames=0`, and positive `jbr_command_frames`.
- Intentional fallback rows remained structured. The invalid-gradient row used picture replay with expected unsupported
  reasons (`sweepGradientStops`, `graphicsLayer:childCommands`, `graphicsLayer`).

Next:
- Continue with the remaining roadmap buckets: production JBR image packaging, deeper font/typeface parity, remaining
  generic shader/effect lifecycle semantics, and screen/context migration hardening.

## Checkpoint: ABI 101 Artifact Bundle Self-Check

Status: completed for the current local artifact baseline.

Validation:
- Packaged the current ABI 101 local artifacts:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-bundles/20260502-200947`.
- Ran the Magic Jewel artifact matrix with that bundle wired into the optional artifact rows and expected as compatible:
  `OLD_ARTIFACT_BUNDLE=/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-bundles/20260502-200947 OLD_JBR_EXPECTED_REASON=none OLD_API_EXPECTED_REASON=none OLD_SKIKO_EXPECTED_REASON=none OLD_CMP_EXPECTED_REASON=none DURATION_SECONDS=2 WARMUP_SECONDS=1 SKIKO_VERSION=0.0.0-SNAPSHOT ./scripts/jbr-skia-artifact-matrix.sh`.
- Matrix TSV: `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-artifact-matrix/20260502-201003/matrix.tsv`.
- All required and optional rows passed; compatible bundle rows reported `fallback_new_count=0` and positive
  `jbr_command_frames`, while the deliberate missing-public-API row still produced the expected structured fallback.

Next:
- Keep this bundle as the known-good current ABI 101 baseline. Real old/new packaged artifact coverage still needs
  separately versioned old Skiko/CMP/JBR artifacts rather than reusing the current bundle as both sides.
