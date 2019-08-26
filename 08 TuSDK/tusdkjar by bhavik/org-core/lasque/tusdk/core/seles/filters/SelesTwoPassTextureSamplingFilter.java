package org.lasque.tusdk.core.seles.filters;

import android.opengl.GLES20;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesTwoPassTextureSamplingFilter
  extends SelesTwoPassFilter
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
  
  public SelesTwoPassTextureSamplingFilter(String paramString1, String paramString2)
  {
    this(paramString1, paramString2, paramString1, paramString2);
  }
  
  public SelesTwoPassTextureSamplingFilter(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    super(paramString1, paramString2, paramString3, paramString4);
    setVerticalTexelSpacing(1.0F);
    setHorizontalTexelSpacing(1.0F);
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.mVerticalPassTexelWidthOffsetUniform = this.mFilterProgram.uniformIndex("texelWidthOffset");
    this.mVerticalPassTexelHeightOffsetUniform = this.mFilterProgram.uniformIndex("texelHeightOffset");
    this.mHorizontalPassTexelWidthOffsetUniform = this.mSecondFilterProgram.uniformIndex("texelWidthOffset");
    this.mHorizontalPassTexelHeightOffsetUniform = this.mSecondFilterProgram.uniformIndex("texelHeightOffset");
  }
  
  public void setUniformsForProgramAtIndex(int paramInt)
  {
    super.setUniformsForProgramAtIndex(paramInt);
    if (paramInt == 0)
    {
      GLES20.glUniform1f(this.mVerticalPassTexelWidthOffsetUniform, this.mVerticalPassTexelWidthOffset);
      GLES20.glUniform1f(this.mVerticalPassTexelHeightOffsetUniform, this.mVerticalPassTexelHeightOffset);
    }
    else
    {
      GLES20.glUniform1f(this.mHorizontalPassTexelWidthOffsetUniform, this.mHorizontalPassTexelWidthOffset);
      GLES20.glUniform1f(this.mHorizontalPassTexelHeightOffsetUniform, this.mHorizontalPassTexelHeightOffset);
    }
  }
  
  public void setupFilterForSize(final TuSdkSize paramTuSdkSize)
  {
    runOnDraw(new Runnable()
    {
      public void run()
      {
        if (SelesTwoPassTextureSamplingFilter.this.mInputRotation.isTransposed())
        {
          SelesTwoPassTextureSamplingFilter.this.mVerticalPassTexelWidthOffset = (SelesTwoPassTextureSamplingFilter.this.mVerticalTexelSpacing / paramTuSdkSize.height);
          SelesTwoPassTextureSamplingFilter.this.mVerticalPassTexelHeightOffset = 0.0F;
        }
        else
        {
          SelesTwoPassTextureSamplingFilter.this.mVerticalPassTexelWidthOffset = 0.0F;
          SelesTwoPassTextureSamplingFilter.this.mVerticalPassTexelHeightOffset = (SelesTwoPassTextureSamplingFilter.this.mVerticalTexelSpacing / paramTuSdkSize.height);
        }
        SelesTwoPassTextureSamplingFilter.this.mHorizontalPassTexelWidthOffset = (SelesTwoPassTextureSamplingFilter.this.mHorizontalTexelSpacing / paramTuSdkSize.width);
        SelesTwoPassTextureSamplingFilter.this.mHorizontalPassTexelHeightOffset = 0.0F;
      }
    });
  }
  
  public float getVerticalTexelSpacing()
  {
    return this.mVerticalTexelSpacing;
  }
  
  public void setVerticalTexelSpacing(float paramFloat)
  {
    this.mVerticalTexelSpacing = paramFloat;
    setupFilterForSize(sizeOfFBO());
  }
  
  public float getHorizontalTexelSpacing()
  {
    return this.mHorizontalTexelSpacing;
  }
  
  public void setHorizontalTexelSpacing(float paramFloat)
  {
    this.mHorizontalTexelSpacing = paramFloat;
    setupFilterForSize(sizeOfFBO());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\filters\SelesTwoPassTextureSamplingFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */