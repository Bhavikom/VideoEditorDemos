package org.lasque.tusdk.core.seles.tusdk.filters.base;

import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.filters.SelesTwoPassTextureSamplingFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU.GpuType;

public class TuSDKGaussianBlurFiveRadiusFilter
  extends SelesTwoPassTextureSamplingFilter
{
  private float a = 1.0F;
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    checkGLError(getClass().getSimpleName());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
  
  private static int a()
  {
    return Math.max(2, Math.min(TuSdkGPU.getGpuType().getPerformance(), 4));
  }
  
  private TuSDKGaussianBlurFiveRadiusFilter(int paramInt)
  {
    this(String.format("-sgv%s", new Object[] { Integer.valueOf(paramInt) }), String.format("-sgf%s", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public TuSDKGaussianBlurFiveRadiusFilter()
  {
    this(false);
  }
  
  public TuSDKGaussianBlurFiveRadiusFilter(boolean paramBoolean)
  {
    this(paramBoolean ? a() : 4);
  }
  
  public TuSDKGaussianBlurFiveRadiusFilter(String paramString1, String paramString2)
  {
    super(paramString1, paramString2);
    setBlurSize(1.0F);
  }
  
  public float getBlurSize()
  {
    return this.a;
  }
  
  public void setBlurSize(float paramFloat)
  {
    this.a = paramFloat;
    this.mVerticalTexelSpacing = this.a;
    this.mHorizontalTexelSpacing = this.a;
    setupFilterForSize(sizeOfFBO());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\base\TuSDKGaussianBlurFiveRadiusFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */