# JBR Skia Compose Zero-Copy Completion Status

Status date: 2026-07-02

## Verdict

The current JBR Skia / Compose zero-copy validation goal is complete for the Mac Studio validation environment.

The latest status helper reports:

- `coverage_ready=true`
- `perf_evidence_ready=true`
- `completion_ready=true`

The helper output is produced from:

```bash
cd /Users/seb/src/jbr-skia-zero-copy/magic-jewel
./scripts/jbr-skia-current-validation-status.sh
```

## Validated Checkpoints

- JBR: `b4f2daf5942 Default Skia command rendering to AppKit thread`
- Magic Jewel: `bc048a3 Refresh JBR Skia validation status helper`

Both validated commits were pushed to the user's fork branches:

- JBR branch: `fork/jbr-skia-compose-poc`
- Magic Jewel branch: `fork/magic-jewel-skia-poc`

## Runtime Policy

The macOS direct command renderer now runs on the AppKit main thread by default.

The opt-out property is:

```text
-Dsun.java2d.skia.interop.appkitRender=false
```

The default-on path was validated without passing an explicit AppKit JVM property.

## Confirmed Evidence

Standalone full showcase coverage:

```text
/Users/seb/src/jbr-skia-zero-copy/magic-jewel/out/jewel-standalone-focused-benchmark-suite/20260701-161913/suite.tsv
```

This passed all retained command/tour/component coverage gates:

- `rows=4`
- `command-clean rows=4/4`
- `Spectre-tour-clean rows=4/4`
- all required showcase cases observed
- all required Jewel components observed

Default-on AppKit IDE powermetrics evidence:

```text
/Users/seb/src/jbr-skia-zero-copy/magic-jewel/out/jewel-ide-plugin-benchmark-suite/20260702-181115/suite.tsv
```

Baseline-definition caveat: this retained suite predates the 2026-07-05 paced-baseline cutover. Its `old` variant is
the stock unpaced IDE baseline. New IDE perf suites now default to `PACE_OLD_BASELINE=true`, where `old` means patched
IDE product with patched/paced CMP jars and JBR interop disabled. Do not compare old-column values across that
boundary; the first paced-vs-paced run should show old `hypnotoad` GPU power dropping from the old churn signature to
the low hundreds because the baseline gained pacing.

Strict analysis passed:

- `rows=3`
- `command-clean rows=3/3`
- `visual-proof rows=3/3`
- `powermetrics rows=3/3`

New renderer summary:

| Case | Command Frames | Avg Total ms | Old CPU | New CPU | Old GPU mW | New GPU mW | Old GPU Active | New GPU Active |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| `redraw` | 691 | 1.095 | 84.77 | 99.12 | 46 | 50 | 14.57 | 15.38 |
| `hypnotoad` | 18323 | 1.226 | 190.61 | 223.92 | 682 | 536 | 88.16 | 93.61 |
| `chat` | 3501 | 1.331 | 131.30 | 109.47 | 113 | 122 | 26.51 | 31.30 |

The default command path stayed off picture replay and reported no fallbacks.

Opt-out diagnostic smoke:

```text
/tmp/jbr-skia-optout-redraw-smoke-old-new/suite.tsv
```

This short non-powermetrics run used `-Dsun.java2d.skia.interop.appkitRender=false` and passed the intended diagnostic
scope:

- `rows=1`
- `command-clean rows=1/1`
- `visual-proof rows=1/1`
- `powermetrics rows=0/1`

It is not GPU/Metal perf evidence.

Popup/menu compositing smoke:

```text
/tmp/jbr-skia-popup-menu-appkit-default-smoke-final/suite.tsv
```

The existing command-probe popup/menu rows passed after narrowing their screenshot gates to the targeted surface:

| Case | Fallbacks | Unsupported | JBR Picture Frames | JBR Command Frames |
| --- | ---: | --- | ---: | ---: |
| `commands-popup` | 0 | none | 0 | 1162 |
| `commands-popup-window` | 0 | none | 0 | 975 |
| `commands-menu` | 0 | none | 0 | 474 |

Live-animation command-probe smoke:

```text
/tmp/jbr-skia-live-animation-physical-threshold-smoke-2/suite.tsv
```

This rerun passed after making the screenshot text oracle scale with the captured image size and current font
anti-aliasing:

- `commands-live-animation`: `fallbacks=0`, `unsupported=none`, `jbr_picture_frames=0`, `jbr_command_frames=550`

## Environment Matrix

| Environment | Suite | Result | Note |
| --- | --- | --- | --- |
| Screen Sharing connected, AppKit opt-in | `20260701-203151` | strict IDE powermetrics pass | Valid for the current remote workstation, but Screen Sharing was active. |
| Screen Sharing closed after launch, AppKit opt-in | `20260701-230506` | strict IDE powermetrics pass | Completed unattended after GUI context was established. |
| Physical display attached, AppKit opt-in | `20260702-122703` | strict IDE powermetrics pass | No Screen Sharing process in preflight. |
| Physical display attached, AppKit default-on | `20260702-181115` | strict IDE powermetrics pass | Current best evidence; no explicit AppKit property. |
| SSH only, no GUI context | `20260702-111126` | launch failure before rendering | Environment limitation: IDE could not detect a graphics environment. |

## Known Non-Blockers

- The standalone powermetrics audit is informational. Standalone validation is the broad coverage surface; IDE validation
  carries the sampled CPU/GPU powermetrics evidence.
- The current readiness probe can fail with `powermetrics-sudo-missing` after the evidence has already been collected.
  That only means another clean powermetrics run cannot start without refreshing sudo credentials.
- A fully remote-free SSH/no-GUI run failed before rendering with `Unable to detect graphics environment` and one
  `HeadlessException`. This is an environment limitation of the headless Mac Studio without a live GUI graphics
  context, not a renderer regression.

## Post-Completion Hardening Experiments

These are useful follow-ups, but they do not block the completed status above.

1. Broader opt-out sanity:

   ```bash
   cd /Users/seb/src/jbr-skia-zero-copy/magic-jewel
   JBR_SKIA_INTEROP_EXTRA_JVM_ARGS='-Dcompose.jbr.skia.command.logOpCounts=true -Dcompose.jbr.skia.command.strict=true -Dsun.java2d.skia.interop.appkitRender=false' \
     CASES=redraw SAMPLE_SECONDS=20 COLLECT_POWERMETRICS=false \
     ./scripts/jewel-ide-plugin-benchmark-suite.sh
   ```

   A narrow redraw smoke already passed. Use this pattern for broader targeted cases only; do not treat non-powermetrics
   runs as GPU/Metal evidence.

2. Popup/menu command-probe refresh:

   ```bash
   cd /Users/seb/src/jbr-skia-zero-copy/magic-jewel
   CASES='commands-popup commands-popup-window commands-menu' \
     ./scripts/jbr-skia-command-probe-suite.sh
   ```

   These rows already passed once after the screenshot-oracle cleanup above. They cover glass-pane popup layering, real
   popup-window capture, and Swing menu popup layering. Run them again after AppKit-thread changes if popup/compositing
   behavior is suspect.

3. Broad command-probe refresh:

   ```bash
   cd /Users/seb/src/jbr-skia-zero-copy/magic-jewel
   JBR_SKIA_ALLOW_EXTRA_BROAD_VALIDATION=true ./scripts/jbr-skia-command-probe-suite.sh
   ```

   This is expensive and should be treated as a deliberate broad-validation slot, not routine iteration.

4. Physical-console IDE soak:

   Start the IDE or benchmark from a local Terminal while a physical display/keyboard session is active, then exercise
   window moves/resizes, dialogs, popups, menus, tooltips, and shutdown. This is the highest-value manual check because
   SSH-only sessions cannot provide the GUI context needed for the IDE benchmark.

## Next Session

Start in Magic Jewel when checking the validated state:

```bash
cd /Users/seb/src/jbr-skia-zero-copy/magic-jewel
./scripts/jbr-skia-current-validation-status.sh
```

Start in JBR when changing the runtime implementation or validation ledger:

```bash
cd /Users/seb/src/jbr-skia-zero-copy/jbr
```

If `/tmp` artifacts were lost after reboot, rebuild them before running local command/IDE validation:

```bash
cd /Users/seb/src/jbr-skia-zero-copy/magic-jewel
SKIA_ROOT=/Users/seb/src/jbr-skia-zero-copy/skiko/skiko/skia \
JAVA_HOME=/Users/seb/Library/Java/JavaVirtualMachines/sdkman-21.0.11-tem.jdk/Contents/Home \
SKIKO_VERSION=0.0.0-SNAPSHOT \
./scripts/rebuild-jbr-skia-local-artifacts.sh
```
