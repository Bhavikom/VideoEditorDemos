// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base;

//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesTwoPassTextureSamplingFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.filters.SelesTwoPassTextureSamplingFilter;

public class TuSDKGaussianBlurFiveRadiusFilter extends SelesTwoPassTextureSamplingFilter
{
    private float a;
    
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
    
    private static int a() {
        return Math.max(2, Math.min(TuSdkGPU.getGpuType().getPerformance(), 4));
    }
    
    private TuSDKGaussianBlurFiveRadiusFilter(final int n) {
        this(String.format("-sgv%s", n), String.format("-sgf%s", n));
    }
    
    public TuSDKGaussianBlurFiveRadiusFilter() {
        this(false);
    }
    
    public TuSDKGaussianBlurFiveRadiusFilter(final boolean b) {
        this(b ? a() : 4);
    }
    
    public TuSDKGaussianBlurFiveRadiusFilter(final String s, final String s2) {
        super(s, s2);
        this.setBlurSize(this.a = 1.0f);
    }
    
    public float getBlurSize() {
        return this.a;
    }
    
    public void setBlurSize(final float a) {
        this.a = a;
        this.mVerticalTexelSpacing = this.a;
        this.mHorizontalTexelSpacing = this.a;
        this.setupFilterForSize(this.sizeOfFBO());
    }
}
