# Zero-Copy Rendering PRD — Current Snapshot

Updated: 2026-08-13

## Executive summary

The zero-copy program lets Compose render into the on-screen Java2D destination without the normal GPU-to-CPU readback and Swing re-upload loop. Its priority is platform-dependent:

- **macOS:** shared Metal textures already remove the expensive pixel round-trip. Command replay is functionally valuable and validated, but its incremental throughput benefit is marginal; measured performance must remain gated rather than assumed.
- **Windows:** the current path is readback-bound. A shared Direct3D texture hand-off is the first high-value deliverable; command replay follows only after that hand-off is correct and measured.
- **Linux:** no corresponding rendering backend exists yet. It is a planned backend, not a supported zero-copy path.

Frame pacing is a related but separate follow-up. It addresses avoidable repaint work and tail latency rather than pixel transfer. The macOS CVDisplayLink experiment achieved its primary deep-stall target in a clean N=1 run, but remains experimental and default-off.

## Product intent

For supported desktop configurations, render a Compose frame on the GPU and present it without transferring the finished pixels through CPU memory. Preserve a safe, visibly correct fallback whenever the accelerated path cannot prove ABI compatibility, capabilities, or rendering correctness.

## Current state

The macOS command-replay path is working and guarded by ABI/capability checks, with strict fallback on a mismatch. Layer hoisting is default-on: unchanged `graphicsLayer` content is referenced rather than rebuilt, reducing a many-layer frame-build probe from 2,850 to 640 microseconds (98 recordings to 1). The remaining macOS evidence does not justify describing command replay as a broad performance win: shared textures already avoid the primary transfer cost, and some benchmark rows still exceed the unchanged CPU regression threshold.

Windows baseline evidence establishes the opportunity: the active path reads GPU pixels into a `BufferedImage` and uploads them again through Java2D; throughput falls with pixel area while one core remains near saturation. A Windows shared-texture backend is therefore the prerequisite for meaningful zero-copy performance work. Command replay and pacing are subsequent, separately gated stages.

Linux has neither a shared-texture hand-off nor a command-replay backend. Its discovery/design work must establish the active Java2D and Skia graphics APIs, safe sharing/synchronization, compatibility, and fallback behavior before implementation begins.

## Public spec

The complete PRD, architecture, current evidence, decision record, non-goals, and platform roadmap are published at the canonical project spec URL after deployment.

## Code copies

The public, zero-copy-specific code copies are the source links for this document. They are intentionally separate
from the similarly named upstream forks:

- [JBR zero-copy PoC](https://github.com/rock3r/jbr-zero-copy-poc/tree/jbr-skia-compose-poc)
- [JBR API zero-copy PoC](https://github.com/rock3r/jbr-api-zero-copy-poc/tree/jbr-api-skia-poc)
- [Magic Jewel zero-copy harness](https://github.com/rock3r/magic-jewel/tree/magic-jewel-skia-poc)

## Follow-up: frame pacing

Zero-copy and frame pacing share the same user-facing goal—smoother desktop UI—but solve different bottlenecks. Frame pacing should remain an independently deployable follow-up: it caps/coalesces repaint work to display cadence, retains timer fallback, and must not be used to make a rendering-path comparison look faster than it is.
