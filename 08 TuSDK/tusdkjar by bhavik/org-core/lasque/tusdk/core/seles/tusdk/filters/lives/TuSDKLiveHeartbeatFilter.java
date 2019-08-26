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

public class TuSDKLiveHeartbeatFilter
  extends SelesFilter
{
  private int c;
  private int d;
  private float e = 0.0F;
  private PointF f = new PointF(0.2F, 0.2F);
  private float g = 0.0F;
  int a;
  int b;
  
  public TuSDKLiveHeartbeatFilter()
  {
    super("-slive09f");
  }
  
  public TuSDKLiveHeartbeatFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null))
    {
      float f1;
      if (paramFilterOption.args.containsKey("strength"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("strength"));
        if (f1 > 0.0F) {
          setStrength(f1);
        }
      }
      if (paramFilterOption.args.containsKey("animation"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("animation"));
        if (f1 > 0.0F) {
          setAnimation(f1);
        }
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.c = this.mFilterProgram.uniformIndex("strength");
    this.d = this.mFilterProgram.uniformIndex("offset");
    setStrength(this.e);
    setOffset(this.f);
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    a();
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
    return this.e;
  }
  
  public void setStrength(float paramFloat)
  {
    this.e = paramFloat;
    setFloat(this.e, this.c, this.mFilterProgram);
  }
  
  public PointF getOffset()
  {
    return this.f;
  }
  
  public void setOffset(PointF paramPointF)
  {
    this.f = paramPointF;
    setPoint(this.f, this.d, this.mFilterProgram);
  }
  
  public void setOffsetX(float paramFloat)
  {
    setOffset(new PointF(paramFloat, this.f.y));
  }
  
  public void setOffsetY(float paramFloat)
  {
    setOffset(new PointF(this.f.x, paramFloat));
  }
  
  public float getAnimation()
  {
    return this.g;
  }
  
  public void setAnimation(float paramFloat)
  {
    this.g = paramFloat;
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
  
  private void a()
  {
    float[] arrayOfFloat = { 0.15F, 0.08F, 0.02F };
    if (this.b == 0)
    {
      this.a = 0;
      this.b = 0;
    }
    if (this.b % 3 == 0) {
      this.a = 0;
    }
    this.b += 1;
    if (this.b <= 9)
    {
      getParameter().setFilterArg("strength", arrayOfFloat[this.a]);
      this.a += 1;
    }
    else
    {
      getParameter().setFilterArg("strength", 0.0F);
    }
    if (this.b >= 19) {
      this.b = 0;
    }
    submitParameter();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lives\TuSDKLiveHeartbeatFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */