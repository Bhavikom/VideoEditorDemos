package org.lasque.tusdk.core.seles.tusdk.filters.skins;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.SelesParameters.FilterTexturesInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSurfaceBlurFilter;

public class TuSDKSkinColor2Filter
  extends SelesFilterGroup
  implements SelesParameters.FilterParameterInterface, SelesParameters.FilterTexturesInterface
{
  private float a;
  private float b;
  private float c;
  private float d;
  private float e;
  private float f;
  private TuSDKSurfaceBlurFilter g = new TuSDKSurfaceBlurFilter();
  private TuSDKSkinColor2MixedFilter h;
  
  public TuSDKSkinColor2Filter()
  {
    this.g.setScale(0.5F);
    this.h = new TuSDKSkinColor2MixedFilter();
    addFilter(this.h);
    this.g.addTarget(this.h, 1);
    setInitialFilters(new SelesOutInput[] { this.g, this.h });
    setTerminalFilter(this.h);
    setSmoothing(1.0F);
    setMixed(0.5F);
    setBlurSize(this.g.getMaxBlursize());
    setThresholdLevel(this.g.getMaxThresholdLevel());
    setLightLevel(1.0F);
    setDetailLevel(0.18F);
  }
  
  public TuSDKSkinColor2Filter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null))
    {
      float f1;
      if (paramFilterOption.args.containsKey("smoothing"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("smoothing"));
        if (f1 > 0.0F) {
          setSmoothing(f1);
        }
      }
      if (paramFilterOption.args.containsKey("mixied"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("mixied"));
        if (f1 > 0.0F) {
          setMixed(f1);
        }
      }
      if (paramFilterOption.args.containsKey("lightLevel"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("lightLevel"));
        if (f1 > 0.0F) {
          setLightLevel(f1);
        }
      }
      if (paramFilterOption.args.containsKey("detailLevel"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("detailLevel"));
        if (f1 > 0.0F) {
          setDetailLevel(f1);
        }
      }
    }
  }
  
  public float getSmoothing()
  {
    return this.a;
  }
  
  public void setSmoothing(float paramFloat)
  {
    this.a = paramFloat;
    this.h.setIntensity(this.a);
  }
  
  public float getMixed()
  {
    return this.b;
  }
  
  public void setMixed(float paramFloat)
  {
    this.b = paramFloat;
    this.h.setMixed(this.b);
  }
  
  public float getBlurSize()
  {
    return this.c;
  }
  
  public void setBlurSize(float paramFloat)
  {
    this.c = paramFloat;
    this.g.setBlurSize(this.c);
  }
  
  public float getThresholdLevel()
  {
    return this.d;
  }
  
  public void setThresholdLevel(float paramFloat)
  {
    this.d = paramFloat;
    this.g.setThresholdLevel(this.d);
  }
  
  public void setLightLevel(float paramFloat)
  {
    this.e = paramFloat;
    this.h.setLightLevel(this.e);
  }
  
  public float getLightLevel()
  {
    return this.e;
  }
  
  public void setDetailLevel(float paramFloat)
  {
    this.f = paramFloat;
    this.h.setDetailLevel(this.f);
  }
  
  public float getDetailLevel()
  {
    return this.f;
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
      localSelesPicture.addTarget(this.h, i);
      i++;
    }
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("smoothing", this.a, 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("mixied", getMixed());
    paramSelesParameters.appendFloatArg("whitening", getLightLevel(), 1.0F, 0.5F);
    float f1 = paramSelesParameters.getDefaultArg("debug");
    if (f1 > 0.0F)
    {
      paramSelesParameters.appendFloatArg("blurSize", getBlurSize(), 0.0F, this.g.getMaxBlursize());
      paramSelesParameters.appendFloatArg("thresholdLevel", getThresholdLevel(), 0.0F, this.g.getMaxThresholdLevel());
      paramSelesParameters.appendFloatArg("detailLevel", getDetailLevel(), 0.0F, 1.0F);
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
    } else if (paramFilterArg.equalsKey("mixied")) {
      setMixed(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("blurSize")) {
      setBlurSize(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("thresholdLevel")) {
      setThresholdLevel(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("whitening")) {
      setLightLevel(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("detailLevel")) {
      setDetailLevel(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\skins\TuSDKSkinColor2Filter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */