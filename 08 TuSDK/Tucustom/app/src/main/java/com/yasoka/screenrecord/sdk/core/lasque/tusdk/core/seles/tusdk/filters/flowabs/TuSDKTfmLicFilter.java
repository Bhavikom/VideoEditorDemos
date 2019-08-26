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

public class TuSDKTfmLicFilter extends SelesFilter
{
    private float a;
    private int b;
    private int c;
    
    public TuSDKTfmLicFilter() {
        super("-stfm3lic");
        this.a = 1.5f;
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.b = this.mFilterProgram.uniformIndex("stepOffset");
        this.c = this.mFilterProgram.uniformIndex("stepLength");
        this.setupFilterForSize(this.sizeOfFBO());
        this.setStepLength(this.a);
    }
    
    @Override
    public void setupFilterForSize(final TuSdkSize tuSdkSize) {
        super.setupFilterForSize(tuSdkSize);
        if (tuSdkSize == null || !tuSdkSize.isSize()) {
            return;
        }
        this.setSize(TuSdkSizeF.create(1.0f / tuSdkSize.width, 1.0f / tuSdkSize.height), this.b, this.mFilterProgram);
    }
    
    public void setStepLength(final float a) {
        this.setFloat(this.a = a, this.c, this.mFilterProgram);
    }
}
