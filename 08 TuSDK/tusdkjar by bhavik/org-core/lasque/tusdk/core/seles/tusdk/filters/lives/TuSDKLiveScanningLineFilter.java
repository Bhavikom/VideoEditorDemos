package org.lasque.tusdk.core.seles.tusdk.filters.lives;

import android.graphics.PointF;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;

public class TuSDKLiveScanningLineFilter
  extends SelesTwoInputFilter
{
  private int a;
  private int b;
  private int c;
  private PointF d = new PointF(0.0F, 0.0F);
  private float e = 0.0F;
  private float f = 0.0F;
  private float g = 1.0F;
  private int h = 0;
  private boolean i = false;
  
  public TuSDKLiveScanningLineFilter()
  {
    super("-slive13f");
    disableSecondFrameCheck();
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.c = this.mFilterProgram.uniformIndex("curve");
    this.a = this.mFilterProgram.uniformIndex("screenPercent");
    this.b = this.mFilterProgram.uniformIndex("line");
    setScreenPercent(this.e);
    setLine(this.f);
    setCurve(this.d);
  }
  
  public float getScreenPercent()
  {
    return this.e;
  }
  
  public void setScreenPercent(float paramFloat)
  {
    this.e = paramFloat;
    setFloat(this.e, this.a, this.mFilterProgram);
  }
  
  public float getLine()
  {
    return this.f;
  }
  
  public void setLine(float paramFloat)
  {
    this.f = paramFloat;
    setFloat(this.f, this.b, this.mFilterProgram);
  }
  
  public PointF getCurve()
  {
    return this.d;
  }
  
  public void setCurve(PointF paramPointF)
  {
    this.d = paramPointF;
    setPoint(this.d, this.c, this.mFilterProgram);
  }
  
  public void setCurveStrength(float paramFloat)
  {
    PointF localPointF = getCurve();
    localPointF.x = paramFloat;
    setCurve(localPointF);
  }
  
  public void setCurveTone(float paramFloat)
  {
    PointF localPointF = getCurve();
    localPointF.y = paramFloat;
    setCurve(localPointF);
  }
  
  public float getAnimation()
  {
    return this.g;
  }
  
  public void setAnimation(float paramFloat)
  {
    this.g = paramFloat;
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    a(paramLong);
    super.informTargetsAboutNewFrame(paramLong);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("screenPercent", getScreenPercent(), -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("line", getLine(), 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("curveStrength", getCurve().x, -2.0F, 2.0F);
    paramSelesParameters.appendFloatArg("curveTone", getCurve().y, 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("animation", getAnimation(), 0.0F, 1.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("screenPercent")) {
      setScreenPercent(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("line")) {
      setLine(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("curveStrength")) {
      setCurveStrength(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("curveTone")) {
      setCurveTone(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("animation")) {
      setAnimation(paramFilterArg.getValue());
    }
  }
  
  private void a(long paramLong)
  {
    if (getAnimation() < 0.5D) {
      return;
    }
    long l = paramLong % 1000000000L;
    if (paramLong / 1000000000L % 2L == 0L) {
      this.i = true;
    } else {
      this.i = false;
    }
    long[] arrayOfLong = { 0L, 50000000L, 100000000L, 150000000L, 200000000L, 250000000L, 300000000L };
    float[] arrayOfFloat1 = { 0.0F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F, 0.1F };
    float[] arrayOfFloat2 = { 0.0F, 0.5F, 0.56F, 0.66F, 0.55F, 0.65F, 0.56F };
    float[] arrayOfFloat3 = { 0.0F, 0.0F, 0.2F, 0.4F, 0.2F, 0.4F, 0.2F };
    if (l > arrayOfLong[(arrayOfLong.length - 1)])
    {
      getParameter().setFilterArg("screenPercent", 0.5F);
      getParameter().setFilterArg("curveStrength", 0.5F);
      getParameter().setFilterArg("curveTone", 0.0F);
      getParameter().setFilterArg("line", 0.0F);
    }
    for (int j = 0; j < arrayOfLong.length - 1; j++) {
      if ((arrayOfLong[j] < l) && (arrayOfLong[(j + 1)] >= l))
      {
        getParameter().setFilterArg("screenPercent", 0.5F + (this.i ? 1.0F : -1.0F) * arrayOfFloat1[(j + 1)]);
        getParameter().setFilterArg("curveStrength", arrayOfFloat2[(j + 1)]);
        getParameter().setFilterArg("curveTone", arrayOfFloat3[(j + 1)]);
        getParameter().setFilterArg("line", 1.0F);
      }
    }
    submitParameter();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lives\TuSDKLiveScanningLineFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */