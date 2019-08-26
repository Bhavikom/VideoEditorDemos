// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base;

//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;

public class TuSDKGaussianBilateralFilter extends TuSDKGaussianBlurFiveRadiusFilter
{
    private int a;
    private int b;
    private float c;
    
    private static String a(final boolean b, final String s) {
        final int max = Math.max(3, Math.min(TuSdkGPU.getGpuType().getPerformance() + 1, 5));
        if (max > 4) {
            return String.format(s, "");
        }
        return String.format(s, max);
    }
    
    public TuSDKGaussianBilateralFilter() {
        this(false);
    }
    
    public TuSDKGaussianBilateralFilter(final boolean b) {
        super(a(b, "-sgbv%s"), a(b, "-sgbf%s"));
        this.setBlurSize(4.0f);
        this.setDistanceNormalizationFactor(8.0f);
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.a = this.mFilterProgram.uniformIndex("distanceNormalizationFactor");
        this.b = this.mSecondFilterProgram.uniformIndex("distanceNormalizationFactor");
        this.setDistanceNormalizationFactor(this.c);
    }
    
    public float getDistanceNormalizationFactor() {
        return this.c;
    }
    
    public void setDistanceNormalizationFactor(final float c) {
        this.setFloat(this.c = c, this.a, this.mFilterProgram);
        this.setFloat(c, this.b, this.mSecondFilterProgram);
    }
}
