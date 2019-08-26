// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.flowabs;

//import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;

public class TuSDKTfmMixFilter extends SelesThreeInputFilter
{
    private float a;
    private int b;
    
    public TuSDKTfmMixFilter() {
        super("-stfm4mix");
        this.a = 0.8f;
        this.disableSecondFrameCheck();
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.b = this.mFilterProgram.uniformIndex("uLightUp");
        this.setLightUp(this.a);
    }
    
    public void setLightUp(final float a) {
        this.setFloat(this.a = a, this.b, this.mFilterProgram);
    }
}
