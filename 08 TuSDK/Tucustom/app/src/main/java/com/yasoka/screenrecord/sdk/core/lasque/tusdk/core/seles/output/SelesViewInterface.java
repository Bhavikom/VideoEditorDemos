// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output;

import android.opengl.GLSurfaceView;

public interface SelesViewInterface
{
    boolean isCreatedSurface();
    
    void setRenderer(final GLSurfaceView.Renderer p0);
    
    void setRenderMode(final int p0);
    
    int getRenderMode();
    
    void setRenderModeDirty();
    
    void setRenderModeContinuously();
    
    void requestRender();
    
    void onPause();
    
    void onResume();
    
    int getRendererFPS();
    
    void setRendererFPS(final int p0);
}
