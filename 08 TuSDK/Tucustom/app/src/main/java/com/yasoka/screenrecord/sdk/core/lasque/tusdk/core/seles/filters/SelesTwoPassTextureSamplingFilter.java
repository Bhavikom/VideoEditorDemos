// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters;

//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.opengl.GLES20;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;

public class SelesTwoPassTextureSamplingFilter extends SelesTwoPassFilter
{
    protected int mVerticalPassTexelWidthOffsetUniform;
    protected int mVerticalPassTexelHeightOffsetUniform;
    protected int mHorizontalPassTexelWidthOffsetUniform;
    protected int mHorizontalPassTexelHeightOffsetUniform;
    protected float mVerticalPassTexelWidthOffset;
    protected float mVerticalPassTexelHeightOffset;
    protected float mHorizontalPassTexelWidthOffset;
    protected float mHorizontalPassTexelHeightOffset;
    protected float mVerticalTexelSpacing;
    protected float mHorizontalTexelSpacing;
    
    public SelesTwoPassTextureSamplingFilter(final String s, final String s2) {
        this(s, s2, s, s2);
    }
    
    public SelesTwoPassTextureSamplingFilter(final String s, final String s2, final String s3, final String s4) {
        super(s, s2, s3, s4);
        this.setVerticalTexelSpacing(1.0f);
        this.setHorizontalTexelSpacing(1.0f);
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.mVerticalPassTexelWidthOffsetUniform = this.mFilterProgram.uniformIndex("texelWidthOffset");
        this.mVerticalPassTexelHeightOffsetUniform = this.mFilterProgram.uniformIndex("texelHeightOffset");
        this.mHorizontalPassTexelWidthOffsetUniform = this.mSecondFilterProgram.uniformIndex("texelWidthOffset");
        this.mHorizontalPassTexelHeightOffsetUniform = this.mSecondFilterProgram.uniformIndex("texelHeightOffset");
    }
    
    @Override
    public void setUniformsForProgramAtIndex(final int uniformsForProgramAtIndex) {
        super.setUniformsForProgramAtIndex(uniformsForProgramAtIndex);
        if (uniformsForProgramAtIndex == 0) {
            GLES20.glUniform1f(this.mVerticalPassTexelWidthOffsetUniform, this.mVerticalPassTexelWidthOffset);
            GLES20.glUniform1f(this.mVerticalPassTexelHeightOffsetUniform, this.mVerticalPassTexelHeightOffset);
        }
        else {
            GLES20.glUniform1f(this.mHorizontalPassTexelWidthOffsetUniform, this.mHorizontalPassTexelWidthOffset);
            GLES20.glUniform1f(this.mHorizontalPassTexelHeightOffsetUniform, this.mHorizontalPassTexelHeightOffset);
        }
    }
    
    @Override
    public void setupFilterForSize(final TuSdkSize tuSdkSize) {
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                if (SelesTwoPassTextureSamplingFilter.this.mInputRotation.isTransposed()) {
                    SelesTwoPassTextureSamplingFilter.this.mVerticalPassTexelWidthOffset = SelesTwoPassTextureSamplingFilter.this.mVerticalTexelSpacing / tuSdkSize.height;
                    SelesTwoPassTextureSamplingFilter.this.mVerticalPassTexelHeightOffset = 0.0f;
                }
                else {
                    SelesTwoPassTextureSamplingFilter.this.mVerticalPassTexelWidthOffset = 0.0f;
                    SelesTwoPassTextureSamplingFilter.this.mVerticalPassTexelHeightOffset = SelesTwoPassTextureSamplingFilter.this.mVerticalTexelSpacing / tuSdkSize.height;
                }
                SelesTwoPassTextureSamplingFilter.this.mHorizontalPassTexelWidthOffset = SelesTwoPassTextureSamplingFilter.this.mHorizontalTexelSpacing / tuSdkSize.width;
                SelesTwoPassTextureSamplingFilter.this.mHorizontalPassTexelHeightOffset = 0.0f;
            }
        });
    }
    
    public float getVerticalTexelSpacing() {
        return this.mVerticalTexelSpacing;
    }
    
    public void setVerticalTexelSpacing(final float mVerticalTexelSpacing) {
        this.mVerticalTexelSpacing = mVerticalTexelSpacing;
        this.setupFilterForSize(this.sizeOfFBO());
    }
    
    public float getHorizontalTexelSpacing() {
        return this.mHorizontalTexelSpacing;
    }
    
    public void setHorizontalTexelSpacing(final float mHorizontalTexelSpacing) {
        this.mHorizontalTexelSpacing = mHorizontalTexelSpacing;
        this.setupFilterForSize(this.sizeOfFBO());
    }
}
