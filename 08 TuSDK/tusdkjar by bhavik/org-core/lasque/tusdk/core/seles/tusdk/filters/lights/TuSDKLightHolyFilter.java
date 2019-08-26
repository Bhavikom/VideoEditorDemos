package org.lasque.tusdk.core.seles.tusdk.filters.lights;

import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.SelesParameters.FilterTexturesInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurSevenRadiusFilter;

public class TuSDKLightHolyFilter
  extends SelesFilterGroup
  implements SelesParameters.FilterParameterInterface, SelesParameters.FilterTexturesInterface
{
  private float a;
  private float b;
  private TuSDKGaussianBlurFiveRadiusFilter c = TuSDKGaussianBlurSevenRadiusFilter.hardware(true);
  private _TuSDKLightHolyFilter d;
  
  public TuSDKLightHolyFilter()
  {
    addFilter(this.c);
    this.d = new _TuSDKLightHolyFilter(null);
    addFilter(this.d);
    this.c.addTarget(this.d, 1);
    setInitialFilters(new SelesOutInput[] { this.c, this.d });
    setTerminalFilter(this.d);
    setHolyLight(0.3F);
    setBrightness(0.0F);
  }
  
  public float getHolyLight()
  {
    return this.a;
  }
  
  public void setHolyLight(float paramFloat)
  {
    this.a = paramFloat;
    this.d.setIntensity(1.0F - paramFloat);
    this.d.setContrast(1.0F + paramFloat * 0.52F);
    this.c.setBlurSize(3.0F + paramFloat * 2.0F);
  }
  
  public float getBrightness()
  {
    return this.b;
  }
  
  public void setBrightness(float paramFloat)
  {
    this.b = paramFloat;
    this.d.setMix(paramFloat);
  }
  
  public void appendTextures(List<SelesPicture> paramList)
  {
    if (paramList == null) {
      return;
    }
    int i = 2;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      SelesPicture localSelesPicture = (SelesPicture)localIterator.next();
      localSelesPicture.processImage();
      localSelesPicture.addTarget(this.d, i);
      i++;
    }
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("mixied", getHolyLight(), 0.0F, 0.6F);
    paramSelesParameters.appendFloatArg("brightness", getBrightness());
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("mixied")) {
      setHolyLight(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("brightness")) {
      setBrightness(paramFilterArg.getValue());
    }
  }
  
  private static class _TuSDKLightHolyFilter
    extends SelesThreeInputFilter
  {
    private int a;
    private int b;
    private int c;
    private float d = 1.0F;
    private float e = 1.0F;
    private float f = 0.5F;
    
    private _TuSDKLightHolyFilter()
    {
      super();
      disableThirdFrameCheck();
    }
    
    protected void onInitOnGLThread()
    {
      super.onInitOnGLThread();
      this.a = this.mFilterProgram.uniformIndex("intensity");
      this.b = this.mFilterProgram.uniformIndex("contrast");
      this.c = this.mFilterProgram.uniformIndex("mixturePercent");
      setIntensity(this.d);
      setContrast(this.e);
      setMix(this.f);
    }
    
    public void setIntensity(float paramFloat)
    {
      this.d = paramFloat;
      setFloat(paramFloat, this.a, this.mFilterProgram);
    }
    
    public void setContrast(float paramFloat)
    {
      this.e = paramFloat;
      setFloat(paramFloat, this.b, this.mFilterProgram);
    }
    
    public void setMix(float paramFloat)
    {
      this.f = paramFloat;
      setFloat(paramFloat, this.c, this.mFilterProgram);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lights\TuSDKLightHolyFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */