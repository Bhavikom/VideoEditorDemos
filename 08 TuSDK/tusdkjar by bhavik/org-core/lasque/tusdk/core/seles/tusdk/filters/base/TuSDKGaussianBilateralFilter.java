package org.lasque.tusdk.core.seles.tusdk.filters.base;

import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU.GpuType;

public class TuSDKGaussianBilateralFilter
  extends TuSDKGaussianBlurFiveRadiusFilter
{
  private int a;
  private int b;
  private float c;
  
  private static String a(boolean paramBoolean, String paramString)
  {
    int i = Math.max(3, Math.min(TuSdkGPU.getGpuType().getPerformance() + 1, 5));
    if (i > 4) {
      return String.format(paramString, new Object[] { "" });
    }
    return String.format(paramString, new Object[] { Integer.valueOf(i) });
  }
  
  public TuSDKGaussianBilateralFilter()
  {
    this(false);
  }
  
  public TuSDKGaussianBilateralFilter(boolean paramBoolean)
  {
    super(a(paramBoolean, "-sgbv%s"), a(paramBoolean, "-sgbf%s"));
    setBlurSize(4.0F);
    setDistanceNormalizationFactor(8.0F);
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.a = this.mFilterProgram.uniformIndex("distanceNormalizationFactor");
    this.b = this.mSecondFilterProgram.uniformIndex("distanceNormalizationFactor");
    setDistanceNormalizationFactor(this.c);
  }
  
  public float getDistanceNormalizationFactor()
  {
    return this.c;
  }
  
  public void setDistanceNormalizationFactor(float paramFloat)
  {
    this.c = paramFloat;
    setFloat(paramFloat, this.a, this.mFilterProgram);
    setFloat(paramFloat, this.b, this.mSecondFilterProgram);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\base\TuSDKGaussianBilateralFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */