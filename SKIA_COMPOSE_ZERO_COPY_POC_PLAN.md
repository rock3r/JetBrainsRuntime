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

Use separate worktrees for every existing repo touched:

- `JetBrainsRuntime` worktree: `jbr-skia-compose-poc`
- `JetBrainsRuntimeApi` worktree: `jbr-api-skia-poc`
- `skiko` worktree: `skiko-jbr-skia-poc`
- `compose-multiplatform-core` worktree: `cmp-jbr-skia-poc`

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
  - `Class.forName("com.jetbrains.JBRSkia")` from the public JBR API jar and read static `ABI_ID` / `BUILD_ID`.
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
  - verify an external-client test reads static `ABI_ID` / `BUILD_ID` reflectively via `Class.forName("com.jetbrains.JBRSkia")`
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
- ABI id `1` covers pointer types exposed, native C ABI layout, scope layout, lifecycle rules, threading rules, and Metal object contract; Skia revision and Skia compile flags hash are tracked separately in `BUILD_ID`.
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
