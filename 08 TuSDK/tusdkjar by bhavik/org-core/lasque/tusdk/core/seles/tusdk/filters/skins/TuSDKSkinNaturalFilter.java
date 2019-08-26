package org.lasque.tusdk.core.seles.tusdk.filters.skins;

import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSurfaceBlurFilter;

public class TuSDKSkinNaturalFilter
  extends SelesFilterGroup
  implements SelesParameters.FilterParameterInterface
{
  private float a;
  private float b;
  private float c;
  private float d;
  private float e;
  private float f;
  private float g;
  private TuSDKSurfaceBlurFilter h = new TuSDKSurfaceBlurFilter();
  private SelesFilter i;
  private _TuSDKSkinNaturalMixFilter j;
  
  public TuSDKSkinNaturalFilter()
  {
    this.h.setScale(0.8F);
    this.i = new SelesFilter();
    this.i.setScale(0.5F);
    this.j = new _TuSDKSkinNaturalMixFilter();
    addFilter(this.j);
    this.h.addTarget(this.j, 1);
    this.i.addTarget(this.j, 2);
    setInitialFilters(new SelesOutInput[] { this.h, this.i, this.j });
    setTerminalFilter(this.j);
    setSmoothing(0.8F);
    b(this.h.getMaxBlursize());
    c(this.h.getMaxThresholdLevel());
    setFair(0.0F);
    a(0.0F);
    d(0.4F);
    e(0.2F);
  }
  
  private float a()
  {
    return this.a;
  }
  
  public void setSmoothing(float paramFloat)
  {
    this.a = paramFloat;
    this.j.setIntensity(paramFloat);
  }
  
  private float b()
  {
    return this.b;
  }
  
  public void setFair(float paramFloat)
  {
    this.b = paramFloat;
    this.j.setFair(paramFloat);
  }
  
  private float c()
  {
    return this.c;
  }
  
  private void a(float paramFloat)
  {
    this.c = paramFloat;
    this.j.setRuddy(paramFloat);
  }
  
  private float d()
  {
    return this.d;
  }
  
  private void b(float paramFloat)
  {
    this.d = paramFloat;
    this.h.setBlurSize(paramFloat);
  }
  
  private float e()
  {
    return this.e;
  }
  
  private void c(float paramFloat)
  {
    this.e = paramFloat;
    this.h.setThresholdLevel(paramFloat);
  }
  
  private float f()
  {
    return this.f;
  }
  
  private void d(float paramFloat)
  {
    this.f = paramFloat;
    this.j.setLight(paramFloat);
  }
  
  private float g()
  {
    return this.g;
  }
  
  private void e(float paramFloat)
  {
    this.g = paramFloat;
    this.j.setDetail(paramFloat);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("smoothing", a());
    paramSelesParameters.appendFloatArg("whitening", b());
    paramSelesParameters.appendFloatArg("ruddy", c());
    float f1 = paramSelesParameters.getDefaultArg("debug");
    if (f1 > 0.0F)
    {
      paramSelesParameters.appendFloatArg("blurSize", d(), 0.0F, this.h.getMaxBlursize());
      paramSelesParameters.appendFloatArg("thresholdLevel", e(), 0.0F, this.h.getMaxThresholdLevel());
      paramSelesParameters.appendFloatArg("lightLevel", f());
      paramSelesParameters.appendFloatArg("detailLevel", g());
    }
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("smoothing")) {
      setSmoothing(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("whitening")) {
      setFair(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("ruddy")) {
      a(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("blurSize")) {
      b(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("thresholdLevel")) {
      c(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("lightLevel")) {
      d(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("detailLevel")) {
      e(paramFilterArg.getValue());
    }
  }
  
  private class _TuSDKSkinNaturalMixFilter
    extends SelesThreeInputFilter
  {
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private float g = 1.0F;
    private float h = 0.0F;
    private float i = 0.0F;
    private float j = 0.4F;
    private float k = 0.2F;
    
    public _TuSDKSkinNaturalMixFilter()
    {
      super();
    }
    
    protected void onInitOnGLThread()
    {
      super.onInitOnGLThread();
      this.b = this.mFilterProgram.uniformIndex("uIntensity");
      this.c = this.mFilterProgram.uniformIndex("uFair");
      this.d = this.mFilterProgram.uniformIndex("uRuddy");
      this.e = this.mFilterProgram.uniformIndex("uLight");
      this.f = this.mFilterProgram.uniformIndex("uDetail");
      setIntensity(this.g);
      setFair(this.h);
      setRuddy(this.i);
      setLight(this.j);
      setDetail(this.k);
    }
    
    public void setIntensity(float paramFloat)
    {
      this.g = paramFloat;
      setFloat(paramFloat, this.b, this.mFilterProgram);
    }
    
    public void setFair(float paramFloat)
    {
      this.h = paramFloat;
      setFloat(paramFloat, this.c, this.mFilterProgram);
    }
    
    public void setRuddy(float paramFloat)
    {
      this.i = paramFloat;
      setFloat(paramFloat, this.d, this.mFilterProgram);
    }
    
    public void setLight(float paramFloat)
    {
      this.j = paramFloat;
      setFloat(paramFloat, this.e, this.mFilterProgram);
    }
    
    public void setDetail(float paramFloat)
    {
      this.k = paramFloat;
      setFloat(paramFloat, this.f, this.mFilterProgram);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\skins\TuSDKSkinNaturalFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */