// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKLiveMirrorImageFilter extends SelesFilter
{
    public TuSDKLiveMirrorImageFilter() {
        super("-slive12f");
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.checkGLError("TuSDKLiveMirrorImageFilter");
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, floatBuffer2);
        this.checkGLError("TuSDKLiveMirrorImageFilter");
        this.captureFilterImage("TuSDKLiveMirrorImageFilter", this.mInputTextureSize.width, this.mInputTextureSize.height);
    }
}
