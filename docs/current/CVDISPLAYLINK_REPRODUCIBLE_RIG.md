# CVDisplayLink Reproducible Pacing Rig

Status: executed and closed on 2026-07-13. This document records the final N=1 rig; it does not authorize another
run.

## Representative Workload

Use the live quiet-environment chat toolwindow signature:

```
logical=456x909;backing=1.000x1.000;pixels=456x909;area=414504
```

The initial `456x579` proposal was reasonable from the then-visible runs, but the automated agent-blackout launch
resolved its provenance. Its archived first attempt requested `456x579` and was correctly rejected before scoring:
the live old surface was `456x909` / `414504` pixels at 1x and 60 Hz. The corrected quiet rerun then accepted the
same `456x909` signature in both arms. Thus `456x909` is the observed representative surface for the quiesced
benchmark environment; `456x579` is an agent-app-open diagnostic state. The mechanism that consumes the vertical
space remains unidentified, but the workload association is bounded by the two guarded runs.

The suite does not resize the IDE to force this shape. `REQUIRE_PINNED_SURFACE_REGIME=true` instead rejects a row
unless the live toolwindow marker exactly matches the predeclared signature. The display guard also rejects any
start/end or old/new display-regime mismatch.

## Quiet-Environment Protocol

1. The operator launches the self-contained runner from Terminal, then leaves it running.
2. After the IDE is ready, the suite waits up to 120 seconds before the active window for watched interactive
   processes to fall below 1.0% CPU. Seb closes ChatGPT and Codex for that wait and keeps them closed through both
   60-second active windows. Screen Sharing stays disconnected.
3. The suite records the census every two seconds and rejects the row if any watched interactive process exceeds
   1.0% CPU during sampling. It also rejects any remote-session process.
4. Reopen the agent applications only after the detached command exits. A rejected row is no-data, not a retryable
   performance result.

The CPU threshold is an explicit quiescence tripwire, not a claim that CPU samples measure GPU power. This short
preflight deliberately disables powermetrics, thread-CPU, and machine-CPU collection because those collectors
perturbed timer-old pacing in the prior N=5.

## N=1 Preflight

Run one fresh old/new pair only. This is not an N=5 and must not be used for CPU/GPU scoring.

```bash
OUT_ROOT=/Users/seb/src/jbr-skia-zero-copy/magic-jewel/out/jewel-ide-plugin-benchmark-suite/20260713-rig-579-light-n1 \
CASES=chat VARIANTS='old new' REPEAT_COUNT=1 SAMPLE_SECONDS=60 \
COLLECT_POWERMETRICS=false COLLECT_THREAD_CPU=false COLLECT_MACHINE_CPU=false \
REQUIRE_CLEAN_PREFLIGHT=true PER_VARIANT_PREFLIGHT=true \
PACE_OLD_BASELINE=true REQUIRE_DISPLAY_LINK_PACING=true \
REQUIRE_DISPLAY_REGIME_MATCH=true REQUIRE_PINNED_SURFACE_REGIME=true \
EXPECTED_TOOLWINDOW_SURFACE_REGIME='logical=456x909;backing=1.000x1.000;pixels=456x909;area=414504' \
REQUIRE_REMOTE_SESSION_CLEAN=true REQUIRE_FOREIGN_GPU_WATCH_CLEAN=true \
MAX_FOREIGN_GPU_WATCH_CPU=1.0 FOREIGN_GPU_WATCH_QUIET_WAIT_SECONDS=120 \
FOREIGN_GPU_WATCH_QUIET_POLL_SECONDS=2 \
LOCAL_JBR_RUNTIME_HOME=/Users/seb/src/jbr-skia-zero-copy/jbr/build/macosx-aarch64-server-release/images/jdk \
JBR_SKIA_LIB=/Users/seb/src/jbr-skia-zero-copy/jbr/build/macosx-aarch64-server-release/images/jdk/lib/libjbrskiainterop.dylib \
BENCHMARK_JVM_ARGS='-Dsun.java2d.skia.interop.displayLinkPacing=true -Dcompose.swing.render.pacing.mode=displayLink' \
./scripts/jewel-ide-plugin-benchmark-suite.sh
```

## Stop Rule

Inspect the completed, accepted row only. If the new arm remains near 40 paint/s or p95 remains above 45 ms, bank
the bridge as flag-off, implemented, correct, and performance-unverified; local-JBR phase timing is a documented
resume option, not work authorized by this run. If it passes the primary predeclared pacing criterion with zero
correctness, display, remote-session, and foreign-workload failures, register the N=1 result as a candidate for
one confirming N=5 only if the campaign is explicitly reopened. Neither outcome launches an N=5 automatically;
both close pacing work for now.

## Final Result

The corrected quiet rerun completed with all guards green. Fixed-window `>50` ms paint intervals fell from `690`
to `154`, a `77.68%` reduction that exceeds the primary `>=60%` criterion. New paint p95 was `53.317` ms, above
the provisional `<=45` ms target. The bridge remains default-off and is banked as a clean N=1 primary-depth pass,
with a provisional p95 miss and a candidate confirming N=5 only if the campaign is explicitly reopened.
