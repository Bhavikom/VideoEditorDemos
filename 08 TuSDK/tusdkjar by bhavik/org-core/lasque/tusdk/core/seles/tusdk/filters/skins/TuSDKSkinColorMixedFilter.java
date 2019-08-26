package org.lasque.tusdk.core.seles.tusdk.filters.skins;

import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;

public class TuSDKSkinColorMixedFilter
  extends SelesThreeInputFilter
{
  private int a;
  private int b;
  private int c;
  private int d;
  private float e = 1.0F;
  private float f = 0.0F;
  private float g = 0.4F;
  private float h = 0.2F;
  
  public TuSDKSkinColorMixedFilter()
  {
    super("-sscf6");
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.a = this.mFilterProgram.uniformIndex("intensity");
    this.b = this.mFilterProgram.uniformIndex("enableSkinColorDetection");
    this.c = this.mFilterProgram.uniformIndex("lightLevel");
    this.d = this.mFilterProgram.uniformIndex("detailLevel");
    setIntensity(this.e);
    setEnableSkinColorDetection(this.f);
    setLightLevel(this.g);
    setDetailLevel(this.h);
  }
  
  public void setIntensity(float paramFloat)
  {
    this.e = paramFloat;
    setFloat(paramFloat, this.a, this.mFilterProgram);
  }
  
  public void setEnableSkinColorDetection(float paramFloat)
  {
    this.f = paramFloat;
    setFloat(paramFloat, this.b, this.mFilterProgram);
  }
  
  public void setLightLevel(float paramFloat)
  {
    this.g = paramFloat;
    setFloat(this.g, this.c, this.mFilterProgram);
  }
  
  public void setDetailLevel(float paramFloat)
  {
    this.h = paramFloat;
    setFloat(this.h, this.d, this.mFilterProgram);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\skins\TuSDKSkinColorMixedFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */