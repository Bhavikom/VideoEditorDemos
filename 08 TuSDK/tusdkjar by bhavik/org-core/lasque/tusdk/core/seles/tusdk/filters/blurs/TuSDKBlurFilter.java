package org.lasque.tusdk.core.seles.tusdk.filters.blurs;

import android.graphics.PointF;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurSevenRadiusFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSelectiveFilter;

public class TuSDKBlurFilter
  extends SelesFilterGroup
  implements SelesParameters.FilterParameterInterface
{
  private final TuSDKGaussianBlurFiveRadiusFilter a = TuSDKGaussianBlurSevenRadiusFilter.hardware(true);
  private final TuSDKSelectiveFilter b;
  private PointF c = new PointF(0.5F, 0.5F);
  private float d = 0.2F;
  private float e;
  private float f = 1.0F;
  
  public TuSDKBlurFilter()
  {
    addFilter(this.a);
    this.b = new TuSDKSelectiveFilter();
    addFilter(this.b);
    this.a.addTarget(this.b, 1);
    setInitialFilters(new SelesOutInput[] { this.a, this.b });
    setTerminalFilter(this.b);
    this.b.setCenter(this.c);
    this.b.setExcessive(this.d);
    this.b.setDegree(this.e);
    this.b.setMaskAlpha(0.0F);
    this.b.setRadius(0.0F);
    this.b.setSelective(0.1F);
    if (this.a.getClass().equals(TuSDKGaussianBlurSevenRadiusFilter.class)) {
      this.f = 0.38F;
    } else {
      this.f = 40.0F;
    }
    a(this.f);
  }
  
  public void setCenter(PointF paramPointF)
  {
    this.c = paramPointF;
    this.b.setCenter(paramPointF);
  }
  
  private float a()
  {
    return this.f;
  }
  
  private void a(float paramFloat)
  {
    this.f = paramFloat;
    this.a.setBlurSize(paramFloat);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    if (this.a.getClass().equals(TuSDKGaussianBlurSevenRadiusFilter.class)) {
      paramSelesParameters.appendFloatArg("blurSize", a(), 0.0F, 2.0F);
    } else {
      paramSelesParameters.appendFloatArg("blurSize", a(), 0.0F, 6.0F);
    }
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("blurSize")) {
      a(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\blurs\TuSDKBlurFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */