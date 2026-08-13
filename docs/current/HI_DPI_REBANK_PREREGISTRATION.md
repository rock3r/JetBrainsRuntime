# HiDPI Re-Bank Pre-Registration

**VOID 2026-07-13.** AppKit and system-profiler both confirmed the active display
is 1x 1920x1080 at 60Hz; the Settings "retina" label was not an active 2x
framebuffer. This retained packet records the retracted hypothesis and must not
be launched.

## Regime Pin

The scored IDE suite now records a toolwindow surface marker from
GraphicsConfiguration.defaultTransform and rejects a row before sampling unless
it matches the pre-registered string:

~~~bash
REGIME_2X='logical=456x579;backing=2.000x2.000;pixels=912x1158;area=1056096'
~~~

This is an explicit testable premise, not a retroactive inference: the historical
logs did not record backing scale, and the runner will fail rather than score if
the live AWT surface differs. It also records CoreGraphics/AppKit mode, refresh,
and display backing scale at the beginning and end of both arms, checks old/new
and every repetition for equality, and records remote-session/foreign-interactive
process observations during sampling.

## Quiet Window Runs

Use one terminal after Screen Sharing has been disconnected, authenticate sudo in
that same terminal, wait 30 seconds, and keep the ticket alive:

~~~bash
cd /Users/seb/src/jbr-skia-zero-copy/magic-jewel
sudo -v
while true; do sudo -n -v; sleep 60; done & SUDO_KEEPALIVE_PID=$!
trap 'kill $SUDO_KEEPALIVE_PID 2>/dev/null || true' EXIT
sleep 30
export POWERMETRICS=/usr/local/sbin/jbr-powermetrics-cpu-gpu
export REQUIRE_PINNED_SURFACE_REGIME=true
export EXPECTED_TOOLWINDOW_SURFACE_REGIME="$REGIME_2X"
~~~

1. Timer baseline: a fresh old-only 50-second chat active-window gate. This
   re-banks the pacing distribution and replaces the old 1x absolute reference
   only after review.

~~~bash
OUT_ROOT=out/jewel-ide-plugin-benchmark-suite/REPLACE-2x-timer-baseline \
CASES=chat VARIANTS=old REPEAT_COUNT=1 SAMPLE_SECONDS=50 \
COLLECT_POWERMETRICS=false REQUIRE_CLEAN_PREFLIGHT=true \
PER_VARIANT_PREFLIGHT=true PACE_OLD_BASELINE=true \
./scripts/jewel-ide-plugin-benchmark-suite.sh
~~~

2. Reference N=5: the fresh paired old/new command-path reference at the same
   pinned regime. This establishes the 2x CPU/GPU closing reference before a
   bridge comparison.

~~~bash
OUT_ROOT=out/jewel-ide-plugin-benchmark-suite/REPLACE-2x-reference-n5 \
CASES=chat VARIANTS='old new' REPEAT_COUNT=5 SAMPLE_SECONDS=60 \
COLLECT_POWERMETRICS=true REQUIRE_CLEAN_PREFLIGHT=true \
PER_VARIANT_PREFLIGHT=true PACE_OLD_BASELINE=true \
./scripts/jewel-ide-plugin-benchmark-suite.sh
~~~

3. High-DPI D uplift: the short, fresh three-variant maximized standalone row
   can share the quiet window after the timer baseline if the machine remains
   clean. It is directional until repeated/power-backed separately.

~~~bash
OUT_ROOT=out/jewel-standalone-software-fallback-suite/REPLACE-2x-d-uplift \
MAXIMIZED=true DISPLAY_TARGET=default RUN_SECONDS=30 \
./scripts/jewel-standalone-software-fallback-suite.sh
~~~

Do not launch the bridge N=5 from this packet. First derive the 2x absolute p95
proposal from the re-banked timer distribution, preserve the relative deep-miss
and correctness criteria, and obtain review approval. The later bridge N=5 uses
the same regime pin plus:

~~~bash
export BENCHMARK_JVM_ARGS='-Dsun.java2d.skia.interop.displayLinkPacing=true -Dcompose.swing.render.pacing.mode=displayLink'
export REQUIRE_DISPLAY_LINK_PACING=true
~~~
