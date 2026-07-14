/*
 * Copyright 2026 JetBrains s.r.o.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package sun.java2d.d3d;

/**
 * Native extensions used by the SharedTextures interop
 * (see {@code com.jetbrains.desktop.SharedTexturesService}).
 * All methods must be invoked on the render queue flusher thread.
 */
public class D3DSurfaceDataExt {
    /**
     * Wraps a shared Direct3D 11 texture (created with
     * {@code D3D11_RESOURCE_MISC_SHARED}) by opening its legacy shared
     * handle on the Java2D Direct3D 9Ex device.
     *
     * @param sd the surface data whose native resource is initialized
     * @param pD3D11Texture pointer to the producer's {@code ID3D11Texture2D}
     */
    public static boolean initWithSharedTexture(D3DSurfaceData sd, long pD3D11Texture) {
        return initWithSharedTexture(sd.getNativeOps(), pD3D11Texture);
    }

    private static native boolean initWithSharedTexture(long pData, long pD3D11Texture);

    /**
     * Whether the Direct3D pipeline runs on a Direct3D 9Ex device
     * (required for opening shared handles).
     */
    public static native boolean isD3D9ExDevice();
}
