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

### Checkpoint 3: Native Scope Or Version-Aligned Skiko Runtime

Status: next.

- The Java/Kotlin handoff is now version-aligned and runnable for the fallback path.
- Next native checkpoint:
  - add the public JBR API jar mirror/accessor for `JBRSkia`
  - make the runnable sample move from `reason=public-api-missing` to service discovery on a local JBR build
  - implement the first valid paint-scope acquisition path in JBR, initially returning a scoped object with metadata and no direct draw
  - only then begin attaching Skia/Metal native canvas pointers.

Use separate worktrees for every existing repo touched:

- `JetBrainsRuntime` worktree: `jbr-skia-compose-poc`
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
