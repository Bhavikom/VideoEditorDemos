// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.flowabs;

//import org.lasque.tusdk.core.struct.TuSdkSizeF;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSizeF;

public class TuSDKTfmDogFilter extends SelesThreeInputFilter
{
    private float a;
    private float b;
    private float c;
    private float d;
    private int e;
    private int f;
    private int g;
    private int h;
    private int i;
    
    public TuSDKTfmDogFilter() {
        super("-stfm2dogv", "-stfm2dogf");
        this.a = 2.0f;
        this.b = 1.0f;
        this.c = 1.02f;
        this.d = 160.0f;
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.e = this.mFilterProgram.uniformIndex("stepOffset");
        this.f = this.mFilterProgram.uniformIndex("stepLength");
        this.g = this.mFilterProgram.uniformIndex("uTau");
        this.h = this.mFilterProgram.uniformIndex("uSigma");
        this.i = this.mFilterProgram.uniformIndex("uPhi");
        this.setStepLength(this.a);
        this.setTau(this.b);
        this.setPhi(this.d);
        this.setupFilterForSize(this.sizeOfFBO());
        this.setSigma(this.c);
    }
    
    @Override
    public void setupFilterForSize(final TuSdkSize tuSdkSize) {
        super.setupFilterForSize(tuSdkSize);
        if (tuSdkSize == null || !tuSdkSize.isSize()) {
            return;
        }
        this.setSize(TuSdkSizeF.create(1.0f / tuSdkSize.width, 1.0f / tuSdkSize.height), this.e, this.mFilterProgram);
    }
    
    public void setStepLength(final float a) {
        this.setFloat(this.a = a, this.f, this.mFilterProgram);
    }
    
    public void setTau(final float b) {
        this.setFloat(this.b = b, this.g, this.mFilterProgram);
    }
    
    public void setSigma(final float c) {
        this.setFloat(this.c = c, this.h, this.mFilterProgram);
    }
    
    public void setPhi(final float d) {
        this.setFloat(this.d = d, this.i, this.mFilterProgram);
    }
}
