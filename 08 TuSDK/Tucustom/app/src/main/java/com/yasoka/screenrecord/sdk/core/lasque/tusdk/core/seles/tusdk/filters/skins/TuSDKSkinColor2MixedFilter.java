// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.skins;

//import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;

public class TuSDKSkinColor2MixedFilter extends SelesThreeInputFilter
{
    private int a;
    private int b;
    private int c;
    private int d;
    private float e;
    private float f;
    private float g;
    private float h;
    
    public TuSDKSkinColor2MixedFilter() {
        super("-sscf2");
        this.e = 1.0f;
        this.f = 1.0f;
        this.g = 1.0f;
        this.h = 0.18f;
        this.disableThirdFrameCheck();
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.a = this.mFilterProgram.uniformIndex("mixturePercent");
        this.b = this.mFilterProgram.uniformIndex("intensity");
        this.c = this.mFilterProgram.uniformIndex("lightLevel");
        this.d = this.mFilterProgram.uniformIndex("detailLevel");
        this.setIntensity(this.f);
        this.setMixed(this.e);
        this.setLightLevel(this.g);
        this.setDetailLevel(this.h);
    }
    
    public void setMixed(final float e) {
        this.setFloat(this.e = e, this.a, this.mFilterProgram);
    }
    
    public void setIntensity(final float f) {
        this.setFloat(this.f = f, this.b, this.mFilterProgram);
    }
    
    public void setLightLevel(final float g) {
        this.setFloat(this.g = g, this.c, this.mFilterProgram);
    }
    
    public void setDetailLevel(final float h) {
        this.setFloat(this.h = h, this.d, this.mFilterProgram);
    }
}
