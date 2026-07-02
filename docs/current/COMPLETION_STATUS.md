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
/Users/seb/src/jbr-skia-zero-copy/magic-jewel/out/jewel-ide-plugin-benchmark-suite/20260702-131610/suite.tsv
```

Strict analysis passed:

- `rows=3`
- `command-clean rows=3/3`
- `visual-proof rows=3/3`
- `powermetrics rows=3/3`

New renderer summary:

| Case | Command Frames | Avg Total ms | Old CPU | New CPU | Old GPU mW | New GPU mW | Old GPU Active | New GPU Active |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| `redraw` | 705 | 1.139 | 84.97 | 88.35 | 50 | 58 | 15.13 | 17.70 |
| `hypnotoad` | 18068 | 1.238 | 190.20 | 200.72 | 676 | 537 | 87.70 | 93.87 |
| `chat` | 3445 | 1.337 | 107.97 | 106.31 | 120 | 123 | 27.90 | 31.73 |

The default command path stayed off picture replay and reported no fallbacks.

## Known Non-Blockers

- The standalone powermetrics audit is informational. Standalone validation is the broad coverage surface; IDE validation
  carries the sampled CPU/GPU powermetrics evidence.
- The current readiness probe can fail with `powermetrics-sudo-missing` after the evidence has already been collected.
  That only means another clean powermetrics run cannot start without refreshing sudo credentials.
- A fully remote-free SSH/no-GUI run failed before rendering with `Unable to detect graphics environment` and one
  `HeadlessException`. This is an environment limitation of the headless Mac Studio without a live GUI graphics
  context, not a renderer regression.

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
