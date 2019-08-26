package org.lasque.tusdk.core.seles.tusdk.filters.flowabs;

import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;

public class TuSDKTfmInkFilter
  extends SelesFilterGroup
  implements SelesParameters.FilterParameterInterface
{
  private float g = 2.0F;
  private float h = 1.0F;
  private float i = 160.0F;
  private float j = 0.25F;
  private float k = 1.5F;
  SelesFilter a = new SelesFilter();
  SelesFilter b;
  TuSDKGaussianBlurFiveRadiusFilter c;
  TuSDKTfmEdgeFilter d;
  TuSDKTfmDogFilter e;
  TuSDKTfmLicFilter f;
  
  public TuSDKTfmInkFilter()
  {
    this.a.setScale(0.5F);
    this.c = new TuSDKGaussianBlurFiveRadiusFilter();
    this.c.setScale(0.5F);
    this.e = new TuSDKTfmDogFilter();
    this.d = new TuSDKTfmEdgeFilter();
    this.d.setScale(0.5F);
    this.f = new TuSDKTfmLicFilter();
    this.b = new SelesFilter();
    this.b.setScale(2.0F);
    addFilter(this.b);
    this.a.addTarget(this.d, 0);
    this.a.addTarget(this.c, 0);
    this.a.addTarget(this.e, 0);
    this.c.addTarget(this.e, 1);
    this.d.addTarget(this.e, 2);
    this.e.addTarget(this.f, 0);
    this.f.addTarget(this.b, 0);
    setInitialFilters(new SelesOutInput[] { this.a });
    setTerminalFilter(this.b);
    setSst(this.g);
    setTau(this.h);
    setPhi(this.i);
    setDogBlur(this.j);
    setTfmEdge(this.k);
  }
  
  public void setSst(float paramFloat)
  {
    this.g = paramFloat;
    this.e.setStepLength(paramFloat);
  }
  
  public void setTau(float paramFloat)
  {
    this.h = paramFloat;
    this.e.setTau(paramFloat);
  }
  
  public void setPhi(float paramFloat)
  {
    this.i = paramFloat;
    this.e.setPhi(paramFloat);
  }
  
  public void setDogBlur(float paramFloat)
  {
    this.j = paramFloat;
    this.c.setBlurSize(paramFloat);
  }
  
  public void setTfmEdge(float paramFloat)
  {
    this.k = paramFloat;
    this.d.setEdgeStrength(paramFloat);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("tau")) {
      setTau(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("phi")) {
      setPhi(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("sst")) {
      setSst(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("dogBlur")) {
      setDogBlur(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("tfmEdge")) {
      setTfmEdge(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\flowabs\TuSDKTfmInkFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */