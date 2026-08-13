# CVDisplayLink Next-Frame-OK Design

## Scope and baseline

This document specifies the experimental replacement for the fixed-cadence Swing
timer pacer. It does not change command recording, command replay, or the
current Metal-sentinel lifetime protocol.

The timer-pacer baseline is the old-path chat proof at
`magic-jewel/out/jewel-ide-plugin-benchmark-suite/20260711-timer-pacing-chat-proof-v3`:

- Paint intervals: 2,643; p50 14.566 ms, p95 68.339 ms, p99 72.720 ms;
  jitter standard deviation 24.146 ms; 474 intervals above 1.5 refresh periods.
- The workload was active for the entire sampled marker span: 2,644 markers
  over 50.092 s, 187 chat ticks, and 589 benchmark frames. There were only two
  gaps above 100 ms, so the p95/p99 tail is not an intentional idle-window
  artifact.
- This is symmetric old/new paint instrumentation. Old `flushAndSubmit(syncCpu
  = true)` makes old paint a completion-cadence upper-bound proxy. New command
  replay uses `JBR_SKIA_INTEROP_COMMAND_COMPLETION` sentinel callbacks as the
  measured completion-cadence proxy. Neither is a CAMetalDrawable present
  timestamp. A locally built JBR runtime for actual presented timestamps is
  optional hardening, not a prerequisite for this phase.

The bridge only ships behind an experimental flag. Timer pacing remains the
default and remains available as the fallback for every bridge failure mode.

## Existing ownership and ordering

- `MTLLayer` owns display-synchronized redraw registration. `startRedraw` and
  `stopRedraw` transfer work to AppKit asynchronously; validation moves a layer
  between `MTLContext` and display IDs by unregistering the old pair first.
  See `jbr/src/java.desktop/macosx/native/libawt_lwawt/java2d/metal/MTLLayer.m`.
- The current command path creates a sentinel after Skia submits. Its completion
  retains/releases the texture and layer, then calls `[layer startRedraw]`.
  `JBRSkiaInterop.mm` owns this `CommandFramePresentationContext` lifetime.
- CMP's `SwingRepaintPacer` is an EDT `Timer` using a fixed refresh-rate anchor.
  The command delegate reaches it through the Swing layer registration bridge in
  `SwingSkiaLayerComponent.desktop.kt`.
- The native `MTLContext` display machinery is therefore the only source of
  display ticks. CMP must request permission to schedule, not own a second
  display-link or predict presentation itself.

## Delivery mechanism (amendment 5)

The display-link callback runs on the native display-link thread, not the EDT
and not necessarily the AppKit main thread. Command replay surfaces do not carry
an `MTLLayer`: the confirmed registration and frame-flush pointers were identical
while `MTLSDOps.layer` was null. Native therefore keeps one JNI global reference
to a display-scoped listener, never resolves CMP classes by name, and emits the
display ID plus callback timestamp once per MTLContext display tick.

Java multiplexes those ticks to active Swing windows by each window's current
`GraphicsConfiguration` display ID. A window owns its dirty bit, permit,
coalescing, watchdog, and generation locally; display migration remaps that Java
association and late callbacks cannot consume a replacement window's permit.
The listener proxy uses the existing classloader-safe delegate pattern and
dispatches to the EDT through `SwingUtilities.invokeLater`.

## Proposed protocol

1. When display sync is active, `MTLContext`'s display-link callback emits a
   nonblocking display-ID/timestamp notification through one native listener.
2. Native does not wait for the EDT and does not acquire the Java `RenderQueue`.
   Java maps the display ID to windows and coalesces one outstanding permit per
   window.
3. The notification is dispatched asynchronously to the EDT. CMP's experimental
   pacer consumes one permit and requests one repaint only when the hierarchy is
   displayable and dirty/animating. Multiple ticks before consumption collapse
   into one permit; no repaint backlog is accumulated.
4. Command recording/replay proceeds as it does today. The existing sentinel
   completion remains responsible for texture lifetime. The replay destination
   has no `MTLLayer`, so its `startRedraw` branch is a no-op; ordinary Java2D
   window presentation owns display-link arming. It must not synchronously invoke
   EDT work.
5. A stalled, occluded, sleeping, detached, or display-migrating layer clears
   pending permits and reverts to the timer. A later valid display-link tick may
   re-arm experimental pacing. Each transition is logged once with a reason.
6. Entering display-link mode, or waking from idle, first schedules one normal
   EDT repaint and asynchronously calls `startRedraw` once. It never waits for
   a previous sentinel completion. Subsequent frames use permits plus the
   existing sentinel re-arm path.

## Stall detector

The EDT owns a lightweight watchdog while a displayable layer is dirty or has
an active animation. It records the last generation-matching next-frame-OK
receipt and, after four expected refresh periods without one, clears the permit,
switches to timer pacing, and logs `displaylink-stall`. It runs only on the EDT;
the display-link callback does not poll or block. A valid later tick for the
same generation may re-arm display-link pacing. Occlusion, zero size, sleep,
and display migration select the same fallback immediately rather than waiting
for the watchdog.

## Threading and safety invariants

- Never perform a blocking main-thread hop while holding `RenderQueue`, a Metal
  command-queue critical section, or a layer lock. Native-to-EDT delivery is
  asynchronous only.
- Java owns the permit atomically; EDT only consumes it. Destroying or migrating
  a window invalidates its generation before remapping its display ID, so a late
  tick cannot repaint a replacement window.
- At most one permit and one queued EDT repaint exist per layer. A skipped tick
  is intentional backpressure, not a missed ownership release.
- Resize, zero size, occlusion, display-ID change, sleep/wake, and native
  callback failure clear the permit and select timer fallback. The fallback
  reason is observable in logs and Perfetto counters.
- Multi-window behavior is per Java window/display ID. A window move remaps its
  `GraphicsConfiguration` display ID; windows never share permits.

## API and flags

The first implementation uses an opt-in JVM property such as
`compose.swing.render.pacing.mode=displaylink`; absent or invalid values select
`timer`. A native availability handshake is required before CMP enables the
experimental mode. If registration or identity validation fails, it logs the
reason and continues with timer pacing.

The public API remains unchanged. The single registration bridge is internal;
the Java dispatcher unregisters closed windows so the native listener does not
retain their layers or plugin classloaders.

## Validation plan

1. Unit/integration coverage: one permit coalesces many ticks; no repaint after
   close; display migration invalidates old permits; timer fallback occurs for
   occlusion/stall; a callback never blocks the AppKit or render thread.
2. Existing correctness gates: command presentation, idle frames, resize storm,
   visual probe, zero fallback, and no picture fallback regressions.
3. Pacing evidence: short chat and commands-live-animation old/new comparison.
   Report paint p50/p95/p99, jitter, and missed intervals for both variants;
   also report old sync-CPU paint completion proxy versus new sentinel-completion
   intervals. Keep unavailable true-present timing explicit. Do not treat a
   zero `CAMetalDrawable.presentedTime` handler callback as a presentation event:
   it is the runtime's dropped branch and may arrive shortly after the present
   call. Such callbacks are diagnostic-only and cannot satisfy present pacing.
4. Performance evidence: only after short correctness/pacing success, run the
   paced N=5. The bridge targets smoother cadence, not a throughput gain: CPU
   and GPU must remain within the current closing-reference noise envelope.

## Acceptance criteria

- Pre-registered active-window targets: paint p95 at or below 33.333 ms (two
  60 Hz refresh periods), p99 at or below 50 ms, and missed intervals at or
  below 10% of intervals. The timer baseline is 68.339 ms p95, 72.720 ms p99,
  and 474/2,643 missed intervals (17.9%).
- The targets hold without increasing fallback, picture frames, or visual
  mismatches.
- Per-layer coalescing prevents an EDT repaint queue from growing under stalls.
- Timer remains the default and every native or lifecycle failure falls back
  cleanly with a logged reason.
- No blocking main-thread hop occurs while `RenderQueue` is held.
