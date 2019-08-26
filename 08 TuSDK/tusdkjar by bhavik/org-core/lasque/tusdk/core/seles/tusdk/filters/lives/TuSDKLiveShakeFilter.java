package org.lasque.tusdk.core.seles.tusdk.filters.lives;

import android.graphics.PointF;
import java.nio.FloatBuffer;
import java.util.HashMap;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKLiveShakeFilter
  extends SelesFilter
{
  private int f;
  private int g;
  private float h = 0.0F;
  private PointF i = new PointF(0.2F, 0.2F);
  private float j = 0.0F;
  int a;
  long b;
  long c;
  int d;
  int e;
  
  public TuSDKLiveShakeFilter()
  {
    super("-slive01f");
  }
  
  public TuSDKLiveShakeFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null))
    {
      if (paramFilterOption.args.containsKey("strength"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("strength"));
        if (f1 > 0.0F) {
          setStrength(f1);
        }
      }
      float f1 = 0.2F;
      float f2 = 0.2F;
      float f3;
      if (paramFilterOption.args.containsKey("offsetX"))
      {
        f3 = Float.parseFloat((String)paramFilterOption.args.get("offsetX"));
        if (f3 > 0.0F) {
          f1 = f3;
        }
      }
      if (paramFilterOption.args.containsKey("offsetY"))
      {
        f3 = Float.parseFloat((String)paramFilterOption.args.get("offsetY"));
        if (f3 > 0.0F) {
          f2 = f3;
        }
      }
      setOffset(new PointF(f1, f2));
      if (paramFilterOption.args.containsKey("animation")) {
        setAnimation(Math.max(Float.parseFloat((String)paramFilterOption.args.get("animation")), 0.0F));
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.f = this.mFilterProgram.uniformIndex("strength");
    this.g = this.mFilterProgram.uniformIndex("offset");
    setStrength(this.h);
    setOffset(this.i);
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    a(System.currentTimeMillis());
    super.informTargetsAboutNewFrame(paramLong);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    checkGLError(getClass().getSimpleName());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
  
  public float getStrength()
  {
    return this.h;
  }
  
  public void setStrength(float paramFloat)
  {
    this.h = paramFloat;
    setFloat(this.h, this.f, this.mFilterProgram);
  }
  
  public PointF getOffset()
  {
    return this.i;
  }
  
  public void setOffset(PointF paramPointF)
  {
    this.i = paramPointF;
    setPoint(this.i, this.g, this.mFilterProgram);
  }
  
  public void setOffsetX(float paramFloat)
  {
    setOffset(new PointF(paramFloat, this.i.y));
  }
  
  public void setOffsetY(float paramFloat)
  {
    setOffset(new PointF(this.i.x, paramFloat));
  }
  
  public float getAnimation()
  {
    return this.j;
  }
  
  public void setAnimation(float paramFloat)
  {
    this.j = paramFloat;
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("strength", getStrength(), 0.0F, 0.5F);
    paramSelesParameters.appendFloatArg("offsetX", getOffset().x, -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("offsetY", getOffset().y, -1.0F, 1.0F);
    paramSelesParameters.appendFloatArg("animation", getAnimation(), 0.0F, 1.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("strength")) {
      setStrength(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("offsetX")) {
      setOffsetX(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("offsetY")) {
      setOffsetY(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("animation")) {
      setAnimation(paramFilterArg.getValue());
    }
  }
  
  private void a(long paramLong)
  {
    long l = paramLong / 50L;
    if (l == this.b) {
      return;
    }
    this.b = l;
    int[] arrayOfInt = { 0, 6, 12 };
    float[] arrayOfFloat1 = { 0.038F, 0.0F };
    float[] arrayOfFloat2 = { 0.0F, 0.0F };
    if ((this.d == 0) || (this.d >= arrayOfInt[(arrayOfInt.length - 1)] - 1))
    {
      this.d = 0;
      this.a = 0;
      this.b = -1L;
      this.e = 0;
      this.c = l;
    }
    this.d += 1;
    int k = arrayOfInt[(this.a + 1)];
    if ((this.d >= k) || (this.d == 0))
    {
      if (this.d != 0) {
        this.a += 1;
      }
      this.e = 0;
      getParameter().setFilterArg("strength", arrayOfFloat2[this.a]);
    }
    else
    {
      this.e += 1;
      getParameter().setFilterArg("strength", arrayOfFloat1[this.a] * this.e + arrayOfFloat2[this.a]);
    }
    submitParameter();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lives\TuSDKLiveShakeFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */