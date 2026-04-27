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

import com.jetbrains.exported.JBRApi;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@JBRApi.Service
@JBRApi.Provides("JBRSkia")
public class JBRSkiaService extends JBRSkia {
    private static final String PROPERTY = "sun.java2d.skia.interop";
    private static final AtomicLong NEXT_SCOPE_ID = new AtomicLong(1);

    public JBRSkiaService() {
        if (!Boolean.getBoolean(PROPERTY)) {
            throw new JBRApi.ServiceNotAvailableException("JBR Skia interop is disabled");
        }
    }

    @Override
    public ScopedSkiaCanvas acquireCanvas(Graphics2D graphics) {
        Objects.requireNonNull(graphics, "graphics");
        return new PocScopedSkiaCanvas(NEXT_SCOPE_ID.getAndIncrement(), graphics.getClipBounds());
    }

    private static final class PocScopedSkiaCanvas extends ScopedSkiaCanvas {
        private final long scopeId;
        private final Rectangle userSpaceClip;
        private boolean closed;
        private boolean flushed;

        private PocScopedSkiaCanvas(long scopeId, Rectangle userSpaceClip) {
            this.scopeId = scopeId;
            this.userSpaceClip = userSpaceClip == null ? null : new Rectangle(userSpaceClip);
        }

        @Override
        public long getScopeId() {
            return scopeId;
        }

        @Override
        public int getBackend() {
            return BACKEND_METAL;
        }

        @Override
        public long getCanvasPtr() {
            return 0;
        }

        @Override
        public long getDirectContextPtr() {
            return 0;
        }

        @Override
        public int getPixelFormat() {
            return 0;
        }

        @Override
        public int getColorSpaceId() {
            return 0;
        }

        @Override
        public int getSampleCount() {
            return 1;
        }

        @Override
        public Rectangle getUserSpaceClip() {
            return userSpaceClip == null ? null : new Rectangle(userSpaceClip);
        }

        @Override
        public void flush() {
            ensureOpen();
            flushed = true;
        }

        @Override
        public void close() {
            if (!closed) {
                if (!flushed) {
                    flush();
                }
                closed = true;
            }
        }

        private void ensureOpen() {
            if (closed) {
                throw new IllegalStateException("JBR Skia scope is already closed");
            }
        }
    }
}
