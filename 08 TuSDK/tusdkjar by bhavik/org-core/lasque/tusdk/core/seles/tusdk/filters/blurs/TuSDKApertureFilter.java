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

public class TuSDKApertureFilter
  extends SelesFilterGroup
  implements SelesParameters.FilterParameterInterface
{
  private final TuSDKGaussianBlurFiveRadiusFilter a = TuSDKGaussianBlurSevenRadiusFilter.hardware(true);
  private final TuSDKSelectiveFilter b;
  private PointF c = new PointF(0.5F, 0.5F);
  private float d = 0.4F;
  private float e = 0.2F;
  private float f;
  private int g = -1;
  private float h;
  private float i;
  private float j = 1.0F;
  
  public TuSDKApertureFilter()
  {
    addFilter(this.a);
    this.b = new TuSDKSelectiveFilter();
    addFilter(this.b);
    this.a.addTarget(this.b, 1);
    setInitialFilters(new SelesOutInput[] { this.a, this.b });
    setTerminalFilter(this.b);
    a(this.d);
    setCenter(this.c);
    b(this.e);
    a(this.g);
    d(this.f);
    e(this.h);
    f(this.i);
    c(this.j);
  }
  
  private PointF a()
  {
    return this.c;
  }
  
  public void setCenter(PointF paramPointF)
  {
    this.c = paramPointF;
    this.b.setCenter(paramPointF);
  }
  
  private float b()
  {
    return this.d;
  }
  
  private void a(float paramFloat)
  {
    this.d = paramFloat;
    this.b.setRadius(paramFloat);
    b(paramFloat * 0.75F);
  }
  
  private float c()
  {
    return this.e;
  }
  
  private void b(float paramFloat)
  {
    this.e = paramFloat;
    this.b.setExcessive(paramFloat);
  }
  
  private void a(int paramInt)
  {
    this.g = paramInt;
    this.b.setMaskColor(paramInt);
  }
  
  private float d()
  {
    return this.j;
  }
  
  private void c(float paramFloat)
  {
    this.j = paramFloat;
    this.a.setBlurSize(paramFloat);
  }
  
  private float e()
  {
    return this.f;
  }
  
  private void d(float paramFloat)
  {
    this.f = paramFloat;
    this.b.setMaskAlpha(paramFloat);
  }
  
  private float f()
  {
    return this.h;
  }
  
  private void e(float paramFloat)
  {
    this.h = paramFloat;
    this.b.setDegree(paramFloat);
  }
  
  private float g()
  {
    return this.i;
  }
  
  private void f(float paramFloat)
  {
    this.i = paramFloat;
    this.b.setSelective(paramFloat);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("aperture", d(), 0.0F, 2.0F);
    paramSelesParameters.appendFloatArg("centerX", a().x);
    paramSelesParameters.appendFloatArg("centerY", a().y);
    paramSelesParameters.appendFloatArg("radius", b());
    paramSelesParameters.appendFloatArg("excessive", c());
    paramSelesParameters.appendFloatArg("maskAlpha", e(), 0.0F, 0.7F);
    paramSelesParameters.appendFloatArg("degree", f(), 0.0F, 360.0F);
    paramSelesParameters.appendFloatArg("selective", g());
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("aperture"))
    {
      c(paramFilterArg.getValue());
    }
    else if (paramFilterArg.equalsKey("radius"))
    {
      a(paramFilterArg.getValue());
    }
    else if (paramFilterArg.equalsKey("excessive"))
    {
      b(paramFilterArg.getValue());
    }
    else if (paramFilterArg.equalsKey("maskAlpha"))
    {
      d(paramFilterArg.getValue());
    }
    else if (paramFilterArg.equalsKey("degree"))
    {
      e(paramFilterArg.getValue());
    }
    else if (paramFilterArg.equalsKey("selective"))
    {
      f(paramFilterArg.getValue());
    }
    else if (paramFilterArg.equalsKey("centerX"))
    {
      a().x = paramFilterArg.getValue();
      setCenter(a());
    }
    else if (paramFilterArg.equalsKey("centerY"))
    {
      a().y = paramFilterArg.getValue();
      setCenter(a());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\blurs\TuSDKApertureFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */