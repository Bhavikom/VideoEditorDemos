package org.lasque.tusdk.core.seles.tusdk.filters.colors;

import android.graphics.Color;
import android.graphics.PointF;
import java.util.HashMap;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;

public class TuSDKColorLomoFilter
  extends SelesTwoInputFilter
  implements SelesParameters.FilterParameterInterface
{
  private float a = 1.0F;
  private int b;
  private int c;
  private PointF d = new PointF(0.5F, 0.5F);
  private int e;
  private float[] f = { 0.0F, 0.0F, 0.0F };
  private int g;
  private float h = 0.25F;
  private int i;
  private float j = 1.0F;
  
  public TuSDKColorLomoFilter()
  {
    super("-sc2");
    disableSecondFrameCheck();
  }
  
  public TuSDKColorLomoFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null))
    {
      float f1;
      if (paramFilterOption.args.containsKey("mixied"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("mixied"));
        if (f1 > 0.0F) {
          setMixed(f1);
        }
      }
      if (paramFilterOption.args.containsKey("vignette"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("vignette"));
        if (f1 > 0.0F) {
          a(f1);
        }
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.b = this.mFilterProgram.uniformIndex("mixturePercent");
    this.c = this.mFilterProgram.uniformIndex("vignetteCenter");
    this.e = this.mFilterProgram.uniformIndex("vignetteColor");
    this.g = this.mFilterProgram.uniformIndex("vignetteStart");
    this.i = this.mFilterProgram.uniformIndex("vignetteEnd");
    setMixed(this.a);
    a(this.d);
    a(this.f);
    a(this.h);
    b(this.j);
  }
  
  public float getMixed()
  {
    return this.a;
  }
  
  public void setMixed(float paramFloat)
  {
    this.a = paramFloat;
    setFloat(this.a, this.b, this.mFilterProgram);
  }
  
  private void a(PointF paramPointF)
  {
    this.d = paramPointF;
    setPoint(this.d, this.c, this.mFilterProgram);
  }
  
  public void setVignetteColor(int paramInt)
  {
    float[] arrayOfFloat = new float[3];
    arrayOfFloat[0] = (Color.red(paramInt) / 255.0F);
    arrayOfFloat[1] = (Color.green(paramInt) / 255.0F);
    arrayOfFloat[2] = (Color.blue(paramInt) / 255.0F);
    a(arrayOfFloat);
  }
  
  private void a(float[] paramArrayOfFloat)
  {
    this.f = paramArrayOfFloat;
    setVec3(this.f, this.e, this.mFilterProgram);
  }
  
  private void a(float paramFloat)
  {
    this.h = paramFloat;
    setFloat(this.h, this.g, this.mFilterProgram);
  }
  
  private void b(float paramFloat)
  {
    this.j = paramFloat;
    setFloat(this.j, this.i, this.mFilterProgram);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("mixied", getMixed());
    paramSelesParameters.appendFloatArg("vignette", this.h, 1.0F, 0.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("mixied")) {
      setMixed(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("vignette")) {
      a(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\colors\TuSDKColorLomoFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */