package org.lasque.tusdk.core.seles.tusdk.filters.base;

import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU.GpuType;

public class TuSDKSurfaceBlurFilter
  extends TuSDKGaussianBlurFiveRadiusFilter
{
  private int a;
  private int b;
  private float c;
  private float d = 3.6F;
  private float e = 0.14F;
  
  private static String a(boolean paramBoolean, String paramString)
  {
    int i = 5;
    if (paramBoolean) {
      i = Math.max(3, Math.min(TuSdkGPU.getGpuType().getPerformance() + 1, 5));
    }
    return String.format(paramString, new Object[] { Integer.valueOf(i) });
  }
  
  public TuSDKSurfaceBlurFilter()
  {
    this(false);
  }
  
  public TuSDKSurfaceBlurFilter(boolean paramBoolean)
  {
    super(a(paramBoolean, "-sgbv%s"), a(paramBoolean, "-ssfbf%s"));
    setBlurSize(this.d);
    setThresholdLevel(this.e);
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.a = this.mFilterProgram.uniformIndex("thresholdLevel");
    this.b = this.mSecondFilterProgram.uniformIndex("thresholdLevel");
    setThresholdLevel(this.c);
  }
  
  public float getThresholdLevel()
  {
    return this.c;
  }
  
  public void setThresholdLevel(float paramFloat)
  {
    this.c = paramFloat;
    setFloat(paramFloat * 2.5F, this.a, this.mFilterProgram);
    setFloat(paramFloat * 2.5F, this.b, this.mSecondFilterProgram);
  }
  
  public float getMaxBlursize()
  {
    return this.d;
  }
  
  public float getMaxThresholdLevel()
  {
    return this.e;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\base\TuSDKSurfaceBlurFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */