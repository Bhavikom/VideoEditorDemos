// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.flowabs;

//import org.lasque.tusdk.core.struct.TuSdkSizeF;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSizeF;

public class TuSDKTfmEdgeFilter extends SelesFilter
{
    private float a;
    private int b;
    private int c;
    
    public TuSDKTfmEdgeFilter() {
        super("-stfm1edgev", "-stfm1edgef");
        this.a = 1.0f;
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.b = this.mFilterProgram.uniformIndex("stepOffset");
        this.c = this.mFilterProgram.uniformIndex("edgeStrength");
        this.setEdgeStrength(this.a);
        this.setupFilterForSize(this.sizeOfFBO());
    }
    
    @Override
    public void setupFilterForSize(final TuSdkSize tuSdkSize) {
        super.setupFilterForSize(tuSdkSize);
        if (tuSdkSize == null || !tuSdkSize.isSize()) {
            return;
        }
        this.setSize(TuSdkSizeF.create(1.0f / tuSdkSize.width, 1.0f / tuSdkSize.height), this.b, this.mFilterProgram);
    }
    
    public void setEdgeStrength(final float a) {
        this.setFloat(this.a = a, this.c, this.mFilterProgram);
    }
}
