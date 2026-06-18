# Unsupported Command Census

This file is the working ledger for driving Magic Jewel/JBR Skia command-probe coverage to zero unsupported rows on
macOS/Metal. Keep it updated from full command-probe evidence, and move rows out only after focused validation proves
they no longer report unsupported markers.

## Target Goal

- End state: full command-probe pass with `unsupported_rows=0`, no picture fallback rows for supported command coverage,
  and no unexpected fallback.
- Cadence: clear unsupported rows in narrow batches; validate focused batches of up to 10 affected rows; run at most one
  full broad pass per local calendar day; fix regressions immediately as they appear.
- Current evidence source:
  `/Users/rock3r/src/jbr-skia-zero-copy/magic-jewel/out/jbr-skia-command-probe-suite/20260612-144955/suite.tsv`.
- Evidence summary from that full/default sweep: `549/549` rows passed, but `unsupported_rows=79`,
  `picture_frames=78827` across unsupported rows, and `jbr_command_frames=0` across unsupported rows.
- Current interpretation: these rows are green because they fall back structurally as expected today. They are not
  counted as complete for the zero-unsupported goal until their unsupported markers are gone and command replay is
  validated.

## Unsupported Marker Families

| Row count | Marker family |
| ---: | --- |
| 176 | `graphicsLayer` |
| 11 | `shader` |
| 6 | `colorFilter` |
| 4 | `unsupportedScope` |
| 4 | `path` |
| 3 | `shaderDescriptor` |
| 3 | `colorFilterDescriptor` |
| 2 | `vertices` |
| 2 | `saveLayer` |
| 2 | `pathEffect` |
| 2 | `image` |
| 2 | `clipPath` |
| 1 | `transform` |
| 1 | `points` |
| 1 | `paintStyle` |
| 1 | `imageShaderImage` |
| 1 | `colorMatrixNonfinite` |
| 1 | `blendMode_Clear` |
| 1 | `blendLayerBounds` |
| 1 | `linearGradientStops` |
| 1 | `linearGradientPoints` |
| 1 | `linearGradientColorCount` |
| 1 | `linearGradientPath` |
| 1 | `linearGradientPathPaint` |
| 1 | `linearGradientStrokeWidth` |
| 1 | `linearGradientRoundRectRadius` |
| 1 | `linearGradientStrokeRoundRectRadius` |
| 1 | `radialGradientStops` |
| 1 | `radialGradientGeometry` |
| 1 | `radialGradientColorCount` |
| 1 | `radialGradientPath` |
| 1 | `radialGradientPathPaint` |
| 1 | `radialGradientStrokeWidth` |
| 1 | `radialGradientRoundRectRadius` |
| 1 | `radialGradientStrokeRoundRectRadius` |
| 1 | `sweepGradientStops` |
| 1 | `sweepGradientGeometry` |
| 1 | `sweepGradientColorCount` |
| 1 | `sweepGradientPath` |
| 1 | `sweepGradientPathPaint` |
| 1 | `sweepGradientStrokeWidth` |
| 1 | `sweepGradientRoundRectRadius` |
| 1 | `sweepGradientStrokeRoundRectRadius` |

## Unsupported Rows

| Row | Unsupported markers | fallback_new_count | picture frames | command frames |
| --- | --- | ---: | ---: | ---: |
| `commands-invalid-blend-layer-bounds-fallback` | `graphicsLayer:childCommands:912,blendLayerBounds:912,graphicsLayer:912` | 0 | 911 | 0 |
| `commands-invalid-concat-transform-fallback` | `unsupportedScope:997,graphicsLayer:childCommands:997,transform:997,graphicsLayer:997` | 0 | 997 | 0 |
| `commands-clip-path-invalid-fallback` | `unsupportedScope:904,clipPath:904,graphicsLayer:childCommands:904,graphicsLayer:904` | 0 | 905 | 0 |
| `commands-draw-path-invalid-fallback` | `path:1920,graphicsLayer:childCommands:960,graphicsLayer:960` | 0 | 959 | 0 |
| `commands-invalid-point-dots-fallback` | `points:984,graphicsLayer:childCommands:984,graphicsLayer:984` | 0 | 984 | 0 |
| `commands-linear-gradient-path-invalid-fallback` | `path:947,graphicsLayer:childCommands:947,linearGradientPath:947,graphicsLayer:947` | 0 | 948 | 0 |
| `commands-radial-gradient-path-invalid-fallback` | `path:975,graphicsLayer:childCommands:975,radialGradientPath:975,graphicsLayer:975` | 0 | 976 | 0 |
| `commands-sweep-gradient-path-invalid-fallback` | `path:956,sweepGradientPath:956,graphicsLayer:childCommands:956,graphicsLayer:956` | 0 | 956 | 0 |
| `commands-linear-gradient-path-stroke-fallback` | `linearGradientPathPaint:959,graphicsLayer:childCommands:959,graphicsLayer:959` | 0 | 959 | 0 |
| `commands-radial-gradient-path-stroke-fallback` | `graphicsLayer:childCommands:940,radialGradientPathPaint:940,graphicsLayer:940` | 0 | 940 | 0 |
| `commands-sweep-gradient-path-stroke-fallback` | `sweepGradientPathPaint:984,graphicsLayer:childCommands:984,graphicsLayer:984` | 0 | 985 | 0 |
| `commands-image-path-effect-fallback` | `graphicsLayer:childCommands:1002,image:1002,pathEffect:1002,graphicsLayer:1002` | 0 | 1002 | 0 |
| `commands-image-shader-invalid-image-fallback` | `imageShaderImage:1230,graphicsLayer:childCommands:1230,graphicsLayer:1230` | 0 | 1230 | 0 |
| `commands-raw-image-shader-fallback` | `shader:934,graphicsLayer:childCommands:934,graphicsLayer:934` | 0 | 934 | 0 |
| `commands-descriptor-stroke-shader-fallback` | `graphicsLayer:childCommands:984,paintStyle:1968,graphicsLayer:984` | 0 | 984 | 0 |
| `commands-raw-linear-gradient-shader-fallback` | `shader:1026,graphicsLayer:childCommands:1026,graphicsLayer:1026` | 0 | 1025 | 0 |
| `commands-raw-radial-gradient-shader-fallback` | `shader:971,graphicsLayer:childCommands:971,graphicsLayer:971` | 0 | 972 | 0 |
| `commands-raw-sweep-gradient-shader-fallback` | `shader:1068,graphicsLayer:childCommands:1068,graphicsLayer:1068` | 0 | 1067 | 0 |
| `commands-raw-conical-gradient-shader-fallback` | `shader:816,graphicsLayer:childCommands:816,graphicsLayer:816` | 0 | 815 | 0 |
| `commands-raw-noise-shader-fallback` | `shader:1059,graphicsLayer:childCommands:1059,graphicsLayer:1059` | 0 | 1058 | 0 |
| `commands-raw-turbulence-shader-fallback` | `shader:1077,graphicsLayer:childCommands:1077,graphicsLayer:1077` | 0 | 1077 | 0 |
| `commands-raw-runtime-effect-shader-fallback` | `shader:906,graphicsLayer:childCommands:906,graphicsLayer:906` | 0 | 906 | 0 |
| `commands-runtime-effect-invalid-uniform-schema-fallback` | `shaderDescriptor:956,graphicsLayer:childCommands:956,graphicsLayer:956` | 0 | 956 | 0 |
| `commands-runtime-effect-invalid-child-schema-fallback` | `shaderDescriptor:924,graphicsLayer:childCommands:924,graphicsLayer:924` | 0 | 924 | 0 |
| `commands-runtime-effect-invalid-nested-child-fallback` | `shaderDescriptor:1044,graphicsLayer:childCommands:1044,graphicsLayer:1044` | 0 | 1045 | 0 |
| `commands-raw-runtime-effect-color-filter-fallback` | `colorFilter:1005,graphicsLayer:childCommands:1005,graphicsLayer:1005` | 0 | 1005 | 0 |
| `commands-runtime-effect-color-filter-invalid-uniform-schema-fallback` | `colorFilterDescriptor:1003,graphicsLayer:childCommands:1003,graphicsLayer:1003` | 0 | 1003 | 0 |
| `commands-runtime-effect-color-filter-invalid-child-schema-fallback` | `colorFilterDescriptor:903,graphicsLayer:childCommands:903,graphicsLayer:903` | 0 | 903 | 0 |
| `commands-runtime-effect-color-filter-invalid-nested-child-fallback` | `colorFilterDescriptor:990,graphicsLayer:childCommands:990,graphicsLayer:990` | 0 | 991 | 0 |
| `commands-linear-gradient-invalid-stops-fallback` | `linearGradientStops:914,graphicsLayer:childCommands:914,graphicsLayer:914` | 0 | 914 | 0 |
| `commands-linear-gradient-invalid-points-fallback` | `linearGradientPoints:907,graphicsLayer:childCommands:907,graphicsLayer:907` | 0 | 908 | 0 |
| `commands-radial-gradient-invalid-stops-fallback` | `graphicsLayer:childCommands:963,radialGradientStops:963,graphicsLayer:963` | 0 | 963 | 0 |
| `commands-radial-gradient-invalid-geometry-fallback` | `radialGradientGeometry:933,graphicsLayer:childCommands:933,graphicsLayer:933` | 0 | 932 | 0 |
| `commands-sweep-gradient-invalid-geometry-fallback` | `graphicsLayer:childCommands:916,sweepGradientGeometry:916,graphicsLayer:916` | 0 | 916 | 0 |
| `commands-linear-gradient-invalid-color-count-fallback` | `graphicsLayer:childCommands:923,linearGradientColorCount:923,graphicsLayer:923` | 0 | 924 | 0 |
| `commands-radial-gradient-invalid-color-count-fallback` | `radialGradientColorCount:934,graphicsLayer:childCommands:934,graphicsLayer:934` | 0 | 935 | 0 |
| `commands-sweep-gradient-invalid-color-count-fallback` | `sweepGradientColorCount:958,graphicsLayer:childCommands:958,graphicsLayer:958` | 0 | 959 | 0 |
| `commands-linear-gradient-invalid-stroke-width-public-fallback` | `graphicsLayer:childCommands:948,linearGradientStrokeWidth:948,graphicsLayer:948` | 0 | 948 | 0 |
| `commands-radial-gradient-invalid-stroke-width-public-fallback` | `radialGradientStrokeWidth:932,graphicsLayer:childCommands:932,graphicsLayer:932` | 0 | 932 | 0 |
| `commands-sweep-gradient-invalid-stroke-width-public-fallback` | `sweepGradientStrokeWidth:917,graphicsLayer:childCommands:917,graphicsLayer:917` | 0 | 917 | 0 |
| `commands-linear-gradient-round-rect-invalid-radius-fallback` | `linearGradientRoundRectRadius:1046,graphicsLayer:childCommands:1046,graphicsLayer:1046` | 0 | 1046 | 0 |
| `commands-radial-gradient-round-rect-invalid-radius-fallback` | `radialGradientRoundRectRadius:1199,graphicsLayer:childCommands:1199,graphicsLayer:1199` | 0 | 1199 | 0 |
| `commands-sweep-gradient-round-rect-invalid-radius-fallback` | `graphicsLayer:childCommands:1010,sweepGradientRoundRectRadius:1010,graphicsLayer:1010` | 0 | 1010 | 0 |
| `commands-linear-gradient-stroke-round-rect-invalid-radius-fallback` | `linearGradientStrokeRoundRectRadius:1213,graphicsLayer:childCommands:1213,graphicsLayer:1213` | 0 | 1213 | 0 |
| `commands-radial-gradient-stroke-round-rect-invalid-radius-fallback` | `radialGradientStrokeRoundRectRadius:1070,graphicsLayer:childCommands:1070,graphicsLayer:1070` | 0 | 1069 | 0 |
| `commands-sweep-gradient-stroke-round-rect-invalid-radius-fallback` | `graphicsLayer:childCommands:916,sweepGradientStrokeRoundRectRadius:916,graphicsLayer:916` | 0 | 917 | 0 |
| `commands-image-raw-table-color-filter-fallback` | `colorFilter:892,graphicsLayer:childCommands:892,image:892,graphicsLayer:892` | 0 | 892 | 0 |
| `commands-raw-blend-color-filter-fallback` | `colorFilter:1056,graphicsLayer:childCommands:1056,graphicsLayer:1056` | 0 | 1057 | 0 |
| `commands-raw-table-color-filter-fallback` | `colorFilter:833,graphicsLayer:childCommands:833,graphicsLayer:833` | 0 | 832 | 0 |
| `commands-color-matrix-filter-nonfinite-fallback` | `graphicsLayer:childCommands:970,colorMatrixNonfinite:970,graphicsLayer:970` | 0 | 969 | 0 |
| `commands-path-effect-color-filter-fallback` | `colorFilter:910,graphicsLayer:childCommands:910,graphicsLayer:910` | 0 | 910 | 0 |
| `commands-raw-discrete-path-effect-fallback` | `graphicsLayer:childCommands:842,pathEffect:842,graphicsLayer:842` | 0 | 842 | 0 |
| `commands-vertices-raw-color-filter-fallback` | `colorFilter:860,graphicsLayer:childCommands:860,vertices:860,graphicsLayer:860` | 0 | 859 | 0 |
| `commands-vertices-invalid-blend-mode-fallback` | `graphicsLayer:childCommands:1240,vertices:1240,blendMode_Clear:1240,graphicsLayer:1240` | 0 | 1240 | 0 |
| `commands-graphics-layer-invalid-size-width-fallback` | `graphicsLayer:sizeWidth:1097,graphicsLayer:childCommands:1097,graphicsLayer:1097` | 0 | 1097 | 0 |
| `commands-graphics-layer-invalid-size-height-fallback` | `graphicsLayer:childCommands:351,graphicsLayer:sizeHeight:351,graphicsLayer:351` | 0 | 351 | 0 |
| `commands-graphics-layer-invalid-alpha-fallback` | `graphicsLayer:alpha:767,graphicsLayer:childCommands:767,graphicsLayer:767` | 0 | 767 | 0 |
| `commands-graphics-layer-invalid-scale-x-fallback` | `graphicsLayer:scaleX:792,graphicsLayer:childCommands:792,graphicsLayer:792` | 0 | 792 | 0 |
| `commands-graphics-layer-invalid-scale-y-fallback` | `graphicsLayer:scaleY:1059,graphicsLayer:childCommands:1059,graphicsLayer:1059` | 0 | 1060 | 0 |
| `commands-graphics-layer-invalid-rotation-z-fallback` | `graphicsLayer:rotationZ:1283,graphicsLayer:childCommands:1283,graphicsLayer:1283` | 0 | 1283 | 0 |
| `commands-graphics-layer-invalid-translation-x-fallback` | `graphicsLayer:childCommands:999,graphicsLayer:translationX:999,graphicsLayer:999` | 0 | 999 | 0 |
| `commands-graphics-layer-invalid-translation-y-fallback` | `graphicsLayer:childCommands:1032,graphicsLayer:translationY:1032,graphicsLayer:1032` | 0 | 1031 | 0 |
| `commands-graphics-layer-invalid-rotation-x-fallback` | `graphicsLayer:rotationX:1006,graphicsLayer:childCommands:1006,graphicsLayer:1006` | 0 | 1006 | 0 |
| `commands-graphics-layer-invalid-rotation-y-fallback` | `graphicsLayer:rotationY:1079,graphicsLayer:childCommands:1079,graphicsLayer:1079` | 0 | 1079 | 0 |
| `commands-graphics-layer-invalid-blend-mode-fallback` | `graphicsLayer:blendMode:1077,graphicsLayer:childCommands:1077,graphicsLayer:1077` | 0 | 1077 | 0 |
| `commands-graphics-layer-unrecorded-fallback` | `graphicsLayer:childCommands:1049,graphicsLayer:recording:1049,graphicsLayer:2098` | 0 | 1049 | 0 |
| `commands-graphics-layer-raw-color-filter-fallback` | `graphicsLayer:childCommands:1440,graphicsLayer:colorFilter:1440,graphicsLayer:1440` | 0 | 1441 | 0 |
| `commands-graphics-layer-raw-table-color-filter-fallback` | `graphicsLayer:childCommands:1279,graphicsLayer:colorFilter:1279,graphicsLayer:1279` | 0 | 1279 | 0 |
| `commands-graphics-layer-unsupported-child-fallback` | `graphicsLayer:childCommands:2030,graphicsLayer:2030` | 0 | 1015 | 0 |
| `commands-graphics-layer-raw-image-filter-effect-fallback` | `graphicsLayer:childCommands:1043,graphicsLayer:renderEffect:1042,graphicsLayer:1043` | 0 | 1043 | 0 |
| `commands-graphics-layer-invalid-shadow-elevation-fallback` | `graphicsLayer:shadowElevation:1138,graphicsLayer:childCommands:1138,graphicsLayer:1138` | 0 | 1137 | 0 |
| `commands-graphics-layer-invalid-shadow-path-fallback` | `clipPath:2328,graphicsLayer:childCommands:1164,graphicsLayer:shadowPath:1164,graphicsLayer:1164` | 0 | 1163 | 0 |
| `commands-graphics-layer-invalid-camera-distance-fallback` | `graphicsLayer:childCommands:1194,graphicsLayer:cameraDistance:1194,graphicsLayer:1194` | 0 | 1195 | 0 |
| `commands-save-layer-raw-color-filter-fallback` | `unsupportedScope:1059,saveLayer:1059,graphicsLayer:childCommands:1059,graphicsLayer:1059` | 0 | 1058 | 0 |
| `commands-save-layer-raw-table-color-filter-fallback` | `unsupportedScope:1377,saveLayer:1377,graphicsLayer:childCommands:1377,graphicsLayer:1377` | 0 | 1377 | 0 |
| `commands-opaque-shader-fallback` | `shader:1140,graphicsLayer:childCommands:1140,graphicsLayer:1140` | 0 | 1139 | 0 |
| `commands-composite-opaque-shader-fallback` | `shader:972,graphicsLayer:childCommands:972,graphicsLayer:972` | 0 | 973 | 0 |
| `commands-picture-shader-fallback` | `shader:789,graphicsLayer:childCommands:789,graphicsLayer:789` | 0 | 788 | 0 |
| `commands-invalid-gradient-fallback` | `sweepGradientStops:909,graphicsLayer:childCommands:909,graphicsLayer:909` | 0 | 908 | 0 |
