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

import sun.java2d.SurfaceData;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Surface data wrapping an externally provided shared Direct3D 11 texture,
 * opened on the Java2D Direct3D 9Ex device.
 * Windows analog of {@code sun.java2d.metal.MTLTextureWrapperSurfaceData}.
 */
public final class D3DTextureWrapperSurfaceData extends D3DSurfaceData {
    private final Image myImage;

    public D3DTextureWrapperSurfaceData(D3DGraphicsConfig gc, Image image, long pD3D11Texture)
            throws IllegalArgumentException {
        super(gc, gc.getColorModel(Transparency.TRANSLUCENT), RT_TEXTURE);
        myImage = image;

        D3DRenderQueue rq = D3DRenderQueue.getInstance();
        AtomicBoolean success = new AtomicBoolean(false);

        rq.lock();
        try {
            rq.flushAndInvokeNow(() ->
                success.set(D3DSurfaceDataExt.initWithSharedTexture(this, pD3D11Texture)));
        } finally {
            rq.unlock();
        }

        if (!success.get()) {
            throw new IllegalArgumentException("Failed to init the surface data");
        }
    }

    @Override
    public SurfaceData getReplacement() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Object getDestination() {
        return myImage;
    }

    @Override
    public Rectangle getBounds() {
        return getNativeBounds();
    }
}
