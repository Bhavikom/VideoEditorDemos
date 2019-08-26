package org.lasque.tusdk.core.seles.tusdk.filters.lights;

import android.graphics.Color;
import android.graphics.PointF;
import java.nio.FloatBuffer;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKLightVignetteFilter
  extends SelesFilter
  implements SelesParameters.FilterParameterInterface
{
  private int a;
  private PointF b = new PointF(0.5F, 0.5F);
  private int c;
  private float[] d = { 0.0F, 0.0F, 0.0F };
  private int e;
  private float f = 0.0F;
  private int g;
  private float h = 1.0F;
  
  public TuSDKLightVignetteFilter()
  {
    super("-ss2");
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.a = this.mFilterProgram.uniformIndex("vignetteCenter");
    this.c = this.mFilterProgram.uniformIndex("vignetteColor");
    this.e = this.mFilterProgram.uniformIndex("vignetteStart");
    this.g = this.mFilterProgram.uniformIndex("vignetteEnd");
    a(this.b);
    a(this.d);
    a(this.f);
    b(this.h);
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  private void a(PointF paramPointF)
  {
    this.b = paramPointF;
    setPoint(this.b, this.a, this.mFilterProgram);
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
    this.d = paramArrayOfFloat;
    setVec3(this.d, this.c, this.mFilterProgram);
  }
  
  private void a(float paramFloat)
  {
    this.f = paramFloat;
    setFloat(this.f, this.e, this.mFilterProgram);
  }
  
  private void b(float paramFloat)
  {
    this.h = paramFloat;
    setFloat(this.h, this.g, this.mFilterProgram);
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
    paramSelesParameters.appendFloatArg("vignette", this.f, 1.0F, 0.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("vignette")) {
      a(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lights\TuSDKLightVignetteFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */