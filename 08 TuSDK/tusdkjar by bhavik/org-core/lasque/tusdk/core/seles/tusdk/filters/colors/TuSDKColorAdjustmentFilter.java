package org.lasque.tusdk.core.seles.tusdk.filters.colors;

import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKColorAdjustmentFilter
  extends SelesFilter
  implements SelesParameters.FilterParameterInterface
{
  private float a = 0.0F;
  private float b = 1.0F;
  private float c = 1.0F;
  private float d = 0.0F;
  private float e = 0.0F;
  private float f = 1.0F;
  private float g = 5000.0F;
  private int h;
  private int i;
  private int j;
  private int k;
  private int l;
  private int m;
  private int n;
  
  public TuSDKColorAdjustmentFilter()
  {
    super("-sc5");
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.h = this.mFilterProgram.uniformIndex("brightness");
    this.i = this.mFilterProgram.uniformIndex("contrast");
    this.j = this.mFilterProgram.uniformIndex("saturation");
    this.k = this.mFilterProgram.uniformIndex("exposure");
    this.l = this.mFilterProgram.uniformIndex("shadows");
    this.m = this.mFilterProgram.uniformIndex("highlights");
    this.n = this.mFilterProgram.uniformIndex("temperature");
    a(this.a);
    b(this.b);
    c(this.c);
    d(this.d);
    e(this.e);
    f(this.f);
    g(this.g);
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    checkGLError(getClass().getSimpleName());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
  
  private float a()
  {
    return this.a;
  }
  
  private void a(float paramFloat)
  {
    this.a = paramFloat;
    setFloat(this.a, this.h, this.mFilterProgram);
  }
  
  private float b()
  {
    return this.b;
  }
  
  private void b(float paramFloat)
  {
    this.b = paramFloat;
    setFloat(this.b, this.i, this.mFilterProgram);
  }
  
  private float c()
  {
    return this.c;
  }
  
  private void c(float paramFloat)
  {
    this.c = paramFloat;
    setFloat(this.c, this.j, this.mFilterProgram);
  }
  
  private float d()
  {
    return this.d;
  }
  
  private void d(float paramFloat)
  {
    this.d = paramFloat;
    setFloat(this.d, this.k, this.mFilterProgram);
  }
  
  private float e()
  {
    return this.e;
  }
  
  private void e(float paramFloat)
  {
    this.e = paramFloat;
    setFloat(this.e, this.l, this.mFilterProgram);
  }
  
  private float f()
  {
    return this.f;
  }
  
  private void f(float paramFloat)
  {
    this.f = paramFloat;
    setFloat(this.f, this.m, this.mFilterProgram);
  }
  
  private float g()
  {
    return this.g;
  }
  
  private void g(float paramFloat)
  {
    this.g = paramFloat;
    setFloat(paramFloat < 5000.0F ? (float)(4.0E-4D * (paramFloat - 5000.0D)) : (float)(6.0E-5D * (paramFloat - 5000.0D)), this.n, this.mFilterProgram);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("brightness", a(), -0.4F, 0.5F);
    paramSelesParameters.appendFloatArg("contrast", b(), 0.6F, 1.8F);
    paramSelesParameters.appendFloatArg("saturation", c(), 0.0F, 2.0F);
    paramSelesParameters.appendFloatArg("exposure", d(), -2.5F, 2.0F);
    paramSelesParameters.appendFloatArg("shadows", e());
    paramSelesParameters.appendFloatArg("highlights", f());
    paramSelesParameters.appendFloatArg("temperature", g(), 3500.0F, 7500.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("brightness")) {
      a(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("contrast")) {
      b(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("saturation")) {
      c(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("exposure")) {
      d(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("shadows")) {
      e(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("highlights")) {
      f(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("temperature")) {
      g(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\colors\TuSDKColorAdjustmentFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */