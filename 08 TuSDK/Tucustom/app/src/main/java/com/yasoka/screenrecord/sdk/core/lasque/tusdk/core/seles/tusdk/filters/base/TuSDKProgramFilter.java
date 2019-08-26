// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKProgramFilter extends SelesFilter
{
    public TuSDKProgramFilter(final String s) {
        super(s);
    }
    
    public TuSDKProgramFilter(final String s, final String s2) {
        super(s, s2);
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, floatBuffer2);
        this.checkGLError(this.getClass().getSimpleName());
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    }
}
