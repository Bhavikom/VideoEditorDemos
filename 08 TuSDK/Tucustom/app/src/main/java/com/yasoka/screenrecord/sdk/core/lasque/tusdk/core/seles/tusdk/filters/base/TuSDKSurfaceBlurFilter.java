// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base;

//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;

public class TuSDKSurfaceBlurFilter extends TuSDKGaussianBlurFiveRadiusFilter
{
    private int a;
    private int b;
    private float c;
    private float d;
    private float e;
    
    private static String a(final boolean b, final String format) {
        int max = 5;
        if (b) {
            max = Math.max(3, Math.min(TuSdkGPU.getGpuType().getPerformance() + 1, 5));
        }
        return String.format(format, max);
    }
    
    public TuSDKSurfaceBlurFilter() {
        this(false);
    }
    
    public TuSDKSurfaceBlurFilter(final boolean b) {
        super(a(b, "-sgbv%s"), a(b, "-ssfbf%s"));
        this.d = 3.6f;
        this.e = 0.14f;
        this.setBlurSize(this.d);
        this.setThresholdLevel(this.e);
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.a = this.mFilterProgram.uniformIndex("thresholdLevel");
        this.b = this.mSecondFilterProgram.uniformIndex("thresholdLevel");
        this.setThresholdLevel(this.c);
    }
    
    public float getThresholdLevel() {
        return this.c;
    }
    
    public void setThresholdLevel(final float c) {
        this.c = c;
        this.setFloat(c * 2.5f, this.a, this.mFilterProgram);
        this.setFloat(c * 2.5f, this.b, this.mSecondFilterProgram);
    }
    
    public float getMaxBlursize() {
        return this.d;
    }
    
    public float getMaxThresholdLevel() {
        return this.e;
    }
}
