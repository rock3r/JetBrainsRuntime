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

package com.jetbrains.desktop;

import com.jetbrains.desktop.image.TextureWrapperSurfaceManager;
import com.jetbrains.exported.JBRApi;
import sun.awt.image.SurfaceManager;
import sun.java2d.SurfaceData;
import sun.java2d.d3d.D3DGraphicsConfig;
import sun.java2d.d3d.D3DSurfaceDataExt;
import sun.java2d.d3d.D3DTextureWrapperSurfaceData;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;

/**
 * Windows provider of the SharedTextures API. Available only when the
 * Direct3D pipeline is active in 9Ex mode
 * ({@code -Dsun.java2d.d3d=true -Dsun.java2d.d3d9ex=true}).
 * The texture value accepted by {@code wrapTexture} is a pointer to an
 * {@code ID3D11Texture2D} created with {@code D3D11_RESOURCE_MISC_SHARED}
 * on the same adapter; its legacy shared handle is opened on the Java2D
 * Direct3D 9Ex device.
 */
@JBRApi.Service
@JBRApi.Provides("SharedTextures")
public class SharedTexturesService extends SharedTextures {
    private final int textureType;

    public SharedTexturesService() {
        textureType = getTextureTypeImpl();
        if (textureType == UNDEFINED_TEXTURE_TYPE) {
            throw new JBRApi.ServiceNotAvailableException();
        }
    }

    @Override
    public int getTextureType() {
        return textureType;
    }

    private static int getTextureTypeImpl() {
        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        if (gc instanceof D3DGraphicsConfig && D3DSurfaceDataExt.isD3D9ExDevice()) {
            return D3D9EX_SHARED_HANDLE_TEXTURE_TYPE;
        }

        return UNDEFINED_TEXTURE_TYPE;
    }

    @Override
    protected SurfaceManager createSurfaceManager(GraphicsConfiguration gc, Image image, long texture) {
        SurfaceData sd;
        if (gc instanceof D3DGraphicsConfig d3dGraphicsConfig) {
            sd = new D3DTextureWrapperSurfaceData(d3dGraphicsConfig, image, texture);
        } else {
            throw new IllegalArgumentException("Unsupported graphics configuration: " + gc);
        }

        return new TextureWrapperSurfaceManager(sd);
    }
}
