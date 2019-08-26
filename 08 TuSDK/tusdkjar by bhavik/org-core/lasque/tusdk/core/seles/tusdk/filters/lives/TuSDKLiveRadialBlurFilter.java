package org.lasque.tusdk.core.seles.tusdk.filters.lives;

import java.nio.FloatBuffer;
import java.util.HashMap;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKLiveRadialBlurFilter
  extends SelesFilter
{
  private int a;
  private int b;
  private float c = 0.0F;
  private float d = 0.0F;
  private float e = 1.0F;
  private int f;
  
  public TuSDKLiveRadialBlurFilter()
  {
    super("-slive15f");
  }
  
  public TuSDKLiveRadialBlurFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null) && (paramFilterOption.args.containsKey("animation")))
    {
      float f1 = Float.parseFloat((String)paramFilterOption.args.get("animation"));
      if (f1 > 0.0F) {
        setAnimation(f1);
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.a = this.mFilterProgram.uniformIndex("radialBlur");
    this.b = this.mFilterProgram.uniformIndex("scale");
    setRadialBlur(this.c);
    setSunkenScale(this.d);
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  public float getRadialBlur()
  {
    return this.c;
  }
  
  public void setRadialBlur(float paramFloat)
  {
    this.c = paramFloat;
    setFloat(this.c, this.a, this.mFilterProgram);
  }
  
  public float getSunkenScale()
  {
    return this.d;
  }
  
  public void setSunkenScale(float paramFloat)
  {
    this.d = paramFloat;
    setFloat(this.d, this.b, this.mFilterProgram);
  }
  
  public float getAnimation()
  {
    return this.e;
  }
  
  public void setAnimation(float paramFloat)
  {
    this.e = paramFloat;
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    a(paramLong);
    super.informTargetsAboutNewFrame(paramLong);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    checkGLError(getClass().getSimpleName());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("radialBlur", getRadialBlur(), 0.0F, 60.0F);
    paramSelesParameters.appendFloatArg("scale", getSunkenScale(), 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("animation", getAnimation(), 0.0F, 1.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("radialBlur")) {
      setRadialBlur(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("scale")) {
      setSunkenScale(paramFilterArg.getValue());
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
    long[] arrayOfLong = { 0L, 66666666L, 133333333L, 200000000L, 266666666L, 333333333L, 399999999L };
    float[] arrayOfFloat1 = { 0.0F, 0.3F, 0.26F, 0.22F, 0.17F, 0.1F, 0.05F };
    float[] arrayOfFloat2 = { 0.0F, 0.35F, 0.28F, 0.21F, 0.14F, 0.07F, 0.03F };
    if (l > arrayOfLong[(arrayOfLong.length - 1)])
    {
      getParameter().setFilterArg("radialBlur", 0.0F);
      getParameter().setFilterArg("scale", 0.0F);
    }
    for (int i = 1; i < arrayOfLong.length; i++) {
      if ((l > arrayOfLong[(i - 1)]) && (l <= arrayOfLong[i]))
      {
        getParameter().setFilterArg("radialBlur", arrayOfFloat1[i] > 0.05D ? arrayOfFloat1[i] - (this.f == i ? 0.01F : 0.0F) : arrayOfFloat1[i]);
        getParameter().setFilterArg("scale", arrayOfFloat2[i] - (this.f == i ? 0.02F : 0.0F));
        this.f = i;
        break;
      }
    }
    submitParameter();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lives\TuSDKLiveRadialBlurFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */