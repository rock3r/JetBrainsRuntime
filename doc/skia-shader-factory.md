# JBR Skia Shader Factory Strategy

This note records the production direction for shader support beyond the serialized command subset used by the current Compose zero-copy PoC.

## Current PoC Boundary

The command stream intentionally does not pass raw `SkShader*`, `SkColorFilter*`, `SkPathEffect*`, `SkImageFilter*`, `SkRuntimeEffect*`, or `SkTypeface*` pointers from Skiko/CMP into JBR.

That boundary is required because Skiko and JBR may load different Skia runtimes. Even if the Skia revisions match, C++ type identity, global registries, ref-counting ownership, font/typeface ownership, and GPU context ownership are not safe across the process-local library boundary.

The current safe subset is:

- serialized solid drawing commands,
- serialized image payloads/cache references,
- serialized text image replay by default,
- experimental native text commands,
- serialized path/clip/path-fill commands,
- serialized known gradient families for fills,
- strict fallback to picture replay for unsupported shader/paint families.

## Required Production Shape

Generic shader support should be implemented as a JBR-owned factory and handle table, not as pointer sharing.

Skiko/CMP should send declarative shader construction requests to JBR. JBR should construct the actual Skia objects inside its own Skia runtime and return opaque integer handles scoped to the current JBR Skia interop context.

Handles must be:

- scoped to the JBR destination context id, not globally reusable;
- invalidated on JBR context migration;
- released explicitly or by scoped cache eviction;
- never interpreted as raw native pointers by Skiko/CMP;
- versioned by `ABI_ID`, `BUILD_ID`, native ABI version, and command capability bits.

## Candidate Factory Requests

Start with shader families that Compose commonly produces and that can be represented without arbitrary native pointers:

- linear/radial/sweep gradients with local matrices;
- image shaders backed by the existing image command/cache ids;
- composed/blended shaders where both children are JBR-owned handles;
- shader local-matrix wrappers over JBR-owned handles;
- shader color-filter wrappers after color-filter factories exist.

Defer these until the simpler factories are stable:

- runtime effects and SKSL;
- picture shaders;
- Perlin/noise shaders;
- external texture/video shaders;
- arbitrary Skiko-created Skia shader pointers.

## Command Stream Model

The command stream should gain capability-gated operations in phases:

1. `CREATE_SHADER_*` commands define or refresh a JBR-owned shader handle.
2. Paint-bearing draw commands reference shader handles by id.
3. `RELEASE_SHADER` or scoped cache eviction clears handles no longer used.

Each command must include enough serialized data for JBR to construct the Skia object deterministically. If any requested feature is unsupported by the active JBR capability set, Skiko/CMP must emit the existing recorder-level fallback signal and replay the frame through picture mode.

## Validation

Before enabling any factory request by default:

- add CMP recorder tests for supported encoding and strict unsupported fallback;
- add Skiko compatibility tests for missing capability bits;
- add JBR parser/replay tests for malformed handles and lifecycle violations;
- add Magic Jewel live command probes for happy-path rendering and forced fallback;
- update the command-probe suite TSV schema only if new stable counters are needed.

The current Magic Jewel suite already covers the safe fallback boundary for opaque image shaders, transformed/composite shader wrappers, invalid gradient metadata, image filters, gradient stroke paint, color filters, path effects, blend modes, and unsupported layer paints.

