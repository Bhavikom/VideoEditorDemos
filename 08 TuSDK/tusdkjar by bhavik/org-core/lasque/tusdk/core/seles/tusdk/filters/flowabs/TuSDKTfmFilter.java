package org.lasque.tusdk.core.seles.tusdk.filters.flowabs;

import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.SelesParameters.FilterTexturesInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBilateralFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;

public class TuSDKTfmFilter
  extends SelesFilterGroup
  implements SelesParameters.FilterParameterInterface, SelesParameters.FilterTexturesInterface
{
  private float h = 2.0F;
  private float i = 0.96F;
  private float j = 200.0F;
  private float k = 0.24F;
  private float l = 0.5F;
  private float m = 1.6F;
  private float n = 0.3F;
  private float o = 1.2F;
  private float p = 0.85F;
  SelesFilter a = new SelesFilter();
  TuSDKGaussianBilateralFilter b;
  TuSDKGaussianBlurFiveRadiusFilter c;
  TuSDKTfmEdgeFilter d;
  TuSDKTfmDogFilter e;
  TuSDKTfmLicFilter f;
  TuSDKTfmMixFilter g;
  
  public TuSDKTfmFilter()
  {
    this.a.setScale(0.4F);
    this.c = new TuSDKGaussianBlurFiveRadiusFilter();
    this.c.setScale(0.6F);
    this.e = new TuSDKTfmDogFilter();
    this.d = new TuSDKTfmEdgeFilter();
    this.d.setScale(0.6F);
    this.f = new TuSDKTfmLicFilter();
    this.b = new TuSDKGaussianBilateralFilter();
    this.g = new TuSDKTfmMixFilter();
    this.g.setScale(2.5F);
    addFilter(this.g);
    this.a.addTarget(this.d, 0);
    this.a.addTarget(this.c, 0);
    this.a.addTarget(this.e, 0);
    this.a.addTarget(this.b, 0);
    this.c.addTarget(this.e, 1);
    this.d.addTarget(this.e, 2);
    this.e.addTarget(this.f, 0);
    this.b.addTarget(this.g, 0);
    this.f.addTarget(this.g, 2);
    setInitialFilters(new SelesOutInput[] { this.a });
    setTerminalFilter(this.g);
    setSst(this.h);
    setTau(this.i);
    setPhi(this.j);
    setDogBlur(this.k);
    setTfmEdge(this.l);
    setVectorSize(this.m);
    setVectorDist(this.n);
    setStepLength(this.o);
    setLightUp(this.p);
  }
  
  public void setSst(float paramFloat)
  {
    this.h = paramFloat;
    this.e.setStepLength(paramFloat);
  }
  
  public void setTau(float paramFloat)
  {
    this.i = paramFloat;
    this.e.setTau(paramFloat);
  }
  
  public void setPhi(float paramFloat)
  {
    this.j = paramFloat;
    this.e.setPhi(paramFloat);
  }
  
  public void setDogBlur(float paramFloat)
  {
    this.k = paramFloat;
    this.c.setBlurSize(paramFloat);
  }
  
  public void setTfmEdge(float paramFloat)
  {
    this.l = paramFloat;
    this.d.setEdgeStrength(paramFloat);
  }
  
  public void setVectorSize(float paramFloat)
  {
    this.m = paramFloat;
    this.b.setBlurSize(paramFloat);
  }
  
  public void setVectorDist(float paramFloat)
  {
    this.n = paramFloat;
    this.b.setDistanceNormalizationFactor(paramFloat);
  }
  
  public void setStepLength(float paramFloat)
  {
    this.o = paramFloat;
    this.f.setStepLength(paramFloat);
  }
  
  public void setLightUp(float paramFloat)
  {
    this.p = paramFloat;
    this.g.setLightUp(paramFloat);
  }
  
  public void appendTextures(List<SelesPicture> paramList)
  {
    if (paramList == null) {
      return;
    }
    int i1 = 1;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      SelesPicture localSelesPicture = (SelesPicture)localIterator.next();
      localSelesPicture.processImage();
      localSelesPicture.addTarget(this.g, i1);
      i1++;
    }
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
    } else if (paramFilterArg.equalsKey("vectorSize")) {
      setVectorSize(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("vectorDist")) {
      setVectorDist(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("stepLength")) {
      setStepLength(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("lightUp")) {
      setLightUp(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\flowabs\TuSDKTfmFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */