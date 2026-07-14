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

#include "D3DPipeline.h"
#include <jlong.h>
#include "D3DSurfaceData.h"
#include "D3DPipelineManager.h"
#include "Trace.h"

#include <d3d11.h>
#include <dxgi.h>

// From D3DSurfaceData.cpp
extern void D3DSD_SetNativeDimensions(JNIEnv *env, D3DSDOps *d3dsdo);

/**
 * Wraps a shared Direct3D 11 texture (created with D3D11_RESOURCE_MISC_SHARED,
 * i.e. a legacy shared handle) as the resource of an existing D3DSDOps by
 * opening the handle on the Java2D Direct3D 9Ex device.
 *
 * Requirements enforced here:
 *  - the pipeline runs in 9Ex mode (sun.java2d.d3d9ex);
 *  - the producer texture is DXGI_FORMAT_B8G8R8A8_UNORM, single-sampled;
 *  - producer and consumer live on the same adapter (LUID equality).
 *
 * Must run on the render queue flusher thread.
 */
static jboolean D3DSD_InitWithSharedD3D11Texture(JNIEnv *env,
                                                 D3DSDOps *d3dsdo,
                                                 void *pTexture11)
{
    HRESULT res;
    D3DPipelineManager *pMgr;
    D3DContext *pCtx;

    J2dRlsTraceLn(J2D_TRACE_INFO, "D3DSD_InitWithSharedD3D11Texture");

    if (d3dsdo == NULL || pTexture11 == NULL) {
        J2dRlsTraceLn(J2D_TRACE_ERROR,
            "D3DSD_InitWithSharedD3D11Texture: null ops/texture");
        return JNI_FALSE;
    }
    if ((pMgr = D3DPipelineManager::GetInstance()) == NULL) {
        return JNI_FALSE;
    }
    if (!pMgr->IsD3D9Ex()) {
        J2dRlsTraceLn(J2D_TRACE_ERROR,
            "D3DSD_InitWithSharedD3D11Texture: pipeline is not in 9Ex mode");
        return JNI_FALSE;
    }
    if (FAILED(res = pMgr->GetD3DContext(d3dsdo->adapter, &pCtx))) {
        return JNI_FALSE;
    }
    if (pCtx->GetResourceManager() == NULL ||
        pCtx->Get3DDevice() == NULL)
    {
        return JNI_FALSE;
    }

    ID3D11Texture2D *tex11 = (ID3D11Texture2D*)pTexture11;
    D3D11_TEXTURE2D_DESC desc;
    tex11->GetDesc(&desc);

    if (desc.Format != DXGI_FORMAT_B8G8R8A8_UNORM ||
        desc.SampleDesc.Count != 1 ||
        desc.Width == 0 || desc.Height == 0)
    {
        J2dRlsTraceLn(J2D_TRACE_ERROR,
            "D3DSD_InitWithSharedD3D11Texture: unsupported producer "\
            "texture (format=%d samples=%d %dx%d)",
            desc.Format, desc.SampleDesc.Count, desc.Width, desc.Height);
        return JNI_FALSE;
    }

    // LUID equality between the producer device's adapter and ours
    {
        LUID luid9 = {};
        if (FAILED(pMgr->GetD3DExObject()->
                GetAdapterLUID(d3dsdo->adapter, &luid9)))
        {
            return JNI_FALSE;
        }
        ID3D11Device *dev11 = NULL;
        tex11->GetDevice(&dev11);
        if (dev11 == NULL) {
            return JNI_FALSE;
        }
        IDXGIDevice *dxgiDev = NULL;
        IDXGIAdapter *dxgiAdapter = NULL;
        DXGI_ADAPTER_DESC adapterDesc = {};
        HRESULT luidRes = dev11->QueryInterface(__uuidof(IDXGIDevice),
                                                (void**)&dxgiDev);
        if (SUCCEEDED(luidRes)) {
            luidRes = dxgiDev->GetAdapter(&dxgiAdapter);
        }
        if (SUCCEEDED(luidRes)) {
            luidRes = dxgiAdapter->GetDesc(&adapterDesc);
        }
        if (dxgiAdapter != NULL) dxgiAdapter->Release();
        if (dxgiDev != NULL) dxgiDev->Release();
        dev11->Release();
        if (FAILED(luidRes)) {
            return JNI_FALSE;
        }
        if (adapterDesc.AdapterLuid.LowPart != luid9.LowPart ||
            adapterDesc.AdapterLuid.HighPart != luid9.HighPart)
        {
            J2dRlsTraceLn(J2D_TRACE_ERROR,
                "D3DSD_InitWithSharedD3D11Texture: adapter LUID mismatch "\
                "(producer %08x-%08x, java2d %08x-%08x)",
                adapterDesc.AdapterLuid.HighPart,
                adapterDesc.AdapterLuid.LowPart,
                luid9.HighPart, luid9.LowPart);
            return JNI_FALSE;
        }
    }

    // The legacy shared handle of the producer texture
    HANDLE hShared = NULL;
    {
        IDXGIResource *dxgiRes = NULL;
        if (FAILED(tex11->QueryInterface(__uuidof(IDXGIResource),
                                         (void**)&dxgiRes)))
        {
            return JNI_FALSE;
        }
        res = dxgiRes->GetSharedHandle(&hShared);
        dxgiRes->Release();
        if (FAILED(res) || hShared == NULL) {
            J2dRlsTraceLn(J2D_TRACE_ERROR,
                "D3DSD_InitWithSharedD3D11Texture: GetSharedHandle "\
                "failed (texture not MISC_SHARED?)");
            return JNI_FALSE;
        }
    }

    // Release any previous resource of this surface data
    pCtx->GetResourceManager()->ReleaseResource(d3dsdo->pResource);
    d3dsdo->pResource = NULL;

    // Open the shared handle on the Java2D 9Ex device
    IDirect3DTexture9 *pTexture9 = NULL;
    res = pCtx->Get3DDevice()->CreateTexture(desc.Width, desc.Height, 1,
                                             D3DUSAGE_RENDERTARGET,
                                             D3DFMT_A8R8G8B8,
                                             D3DPOOL_DEFAULT,
                                             &pTexture9, &hShared);
    if (FAILED(res)) {
        DebugPrintD3DError(res,
            "D3DSD_InitWithSharedD3D11Texture: open shared handle failed");
        return JNI_FALSE;
    }

    D3DResource *pResource = new D3DResource((IDirect3DResource9*)pTexture9);
    res = pCtx->GetResourceManager()->AddResource(pResource);
    if (FAILED(res)) {
        // AddResource deletes the resource object on failure
        return JNI_FALSE;
    }
    d3dsdo->pResource = pResource;
    d3dsdo->pResource->SetSDOps(d3dsdo);

    d3dsdo->width = desc.Width;
    d3dsdo->height = desc.Height;
    d3dsdo->xoff = 0;
    d3dsdo->yoff = 0;

    J2dRlsTraceLn(J2D_TRACE_INFO,
        "D3DSD_InitWithSharedD3D11Texture: wrapped %dx%d shared texture "\
        "(9Ex, handle=0x%p)", desc.Width, desc.Height, hShared);
    return JNI_TRUE;
}

extern "C" {

/*
 * Class:     sun_java2d_d3d_D3DSurfaceDataExt
 * Method:    initWithSharedTexture
 * Signature: (JJ)Z
 */
JNIEXPORT jboolean JNICALL
Java_sun_java2d_d3d_D3DSurfaceDataExt_initWithSharedTexture
  (JNIEnv *env, jclass cls, jlong pData, jlong pTexture11)
{
    D3DSDOps *d3dsdo = (D3DSDOps *)jlong_to_ptr(pData);
    if (!D3DSD_InitWithSharedD3D11Texture(env, d3dsdo,
                                          jlong_to_ptr(pTexture11))) {
        return JNI_FALSE;
    }
    D3DSD_SetNativeDimensions(env, d3dsdo);
    return JNI_TRUE;
}

/*
 * Class:     sun_java2d_d3d_D3DSurfaceDataExt
 * Method:    isD3D9ExDevice
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_sun_java2d_d3d_D3DSurfaceDataExt_isD3D9ExDevice
  (JNIEnv *env, jclass cls)
{
    D3DPipelineManager *pMgr = D3DPipelineManager::GetInstance();
    return (pMgr != NULL && pMgr->IsD3D9Ex()) ? JNI_TRUE : JNI_FALSE;
}

} // extern "C"
