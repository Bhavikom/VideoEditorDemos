package org.lasque.tusdk.core.seles.tusdk.filters.skins;

import java.util.HashMap;
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
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSurfaceBlurFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorMixedFilter;

public class TuSDKSkinColorFilter
  extends SelesFilterGroup
  implements SelesParameters.FilterParameterInterface, SelesParameters.FilterTexturesInterface
{
  private float a;
  private float b;
  private float c;
  private float d;
  private float e;
  private float f;
  private TuSDKColorMixedFilter g = new TuSDKColorMixedFilter();
  private TuSDKSurfaceBlurFilter h;
  private SelesFilter i;
  private TuSDKSkinColorMixedFilter j;
  
  public TuSDKSkinColorFilter()
  {
    addFilter(this.g);
    this.h = new TuSDKSurfaceBlurFilter();
    this.h.setScale(0.5F);
    this.i = new SelesFilter();
    this.i.setScale(0.5F);
    this.j = new TuSDKSkinColorMixedFilter();
    addFilter(this.j);
    this.j.addTarget(this.g, 0);
    this.h.addTarget(this.j, 1);
    this.i.addTarget(this.j, 2);
    setInitialFilters(new SelesOutInput[] { this.h, this.i, this.j });
    setTerminalFilter(this.g);
    setSmoothing(0.8F);
    setMixed(0.6F);
    setBlurSize(this.h.getMaxBlursize());
    setThresholdLevel(this.h.getMaxThresholdLevel());
    setLightLevel(0.4F);
    setDetailLevel(0.2F);
  }
  
  public TuSDKSkinColorFilter(FilterOption paramFilterOption)
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
    this.j.setIntensity(1.0F - paramFloat);
  }
  
  public float getMixed()
  {
    return this.b;
  }
  
  public void setMixed(float paramFloat)
  {
    this.b = paramFloat;
    this.g.setMixed(this.b);
  }
  
  public float getBlurSize()
  {
    return this.c;
  }
  
  public void setBlurSize(float paramFloat)
  {
    this.c = paramFloat;
    this.h.setBlurSize(this.c);
  }
  
  public float getThresholdLevel()
  {
    return this.d;
  }
  
  public void setThresholdLevel(float paramFloat)
  {
    this.d = paramFloat;
    this.h.setThresholdLevel(this.d);
  }
  
  public void setLightLevel(float paramFloat)
  {
    this.e = paramFloat;
    this.j.setLightLevel(this.e);
  }
  
  public float getLightLevel()
  {
    return this.e;
  }
  
  public void setDetailLevel(float paramFloat)
  {
    this.f = paramFloat;
    this.j.setDetailLevel(this.f);
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
    int k = 1;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      SelesPicture localSelesPicture = (SelesPicture)localIterator.next();
      localSelesPicture.processImage();
      localSelesPicture.addTarget(this.g, k);
      k++;
    }
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    float f1 = paramSelesParameters.getDefaultArg("blurSize");
    if (f1 > 0.0F) {
      setBlurSize(f1);
    }
    float f2 = paramSelesParameters.getDefaultArg("thresholdLevel");
    if (f2 > 0.0F) {
      setThresholdLevel(f2);
    }
    paramSelesParameters.appendFloatArg("smoothing", this.a, 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("mixied", getMixed());
    if (paramSelesParameters.getDefaultArg("skinColor") > 0.0F) {
      this.j.setEnableSkinColorDetection(paramSelesParameters.getDefaultArg("skinColor"));
    }
    float f3 = paramSelesParameters.getDefaultArg("debug");
    if (f3 > 0.0F)
    {
      paramSelesParameters.appendFloatArg("blurSize", getBlurSize(), 0.0F, this.h.getMaxBlursize());
      paramSelesParameters.appendFloatArg("thresholdLevel", getThresholdLevel(), 0.0F, this.h.getMaxThresholdLevel());
      paramSelesParameters.appendFloatArg("lightLevel", getLightLevel(), 0.0F, 1.0F);
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
    } else if (paramFilterArg.equalsKey("lightLevel")) {
      setLightLevel(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("detailLevel")) {
      setDetailLevel(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\skins\TuSDKSkinColorFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */