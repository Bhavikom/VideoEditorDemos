package org.lasque.tusdk.core.seles.tusdk.filters.lives;

import java.nio.FloatBuffer;
import java.util.HashMap;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKLiveSloshFilter
  extends SelesFilter
{
  private int a;
  private float b = 0.0F;
  private float c = 1.0F;
  
  public TuSDKLiveSloshFilter()
  {
    super("-slive10f");
  }
  
  public TuSDKLiveSloshFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null))
    {
      float f;
      if (paramFilterOption.args.containsKey("strength"))
      {
        f = Float.parseFloat((String)paramFilterOption.args.get("strength"));
        if (f > 0.0F) {
          setStrength(f);
        }
      }
      if (paramFilterOption.args.containsKey("animation"))
      {
        f = Float.parseFloat((String)paramFilterOption.args.get("animation"));
        if (f > 0.0F) {
          setAnimation(f);
        }
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.a = this.mFilterProgram.uniformIndex("strength");
    setStrength(this.b);
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
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
  
  public float getStrength()
  {
    return this.b;
  }
  
  public void setStrength(float paramFloat)
  {
    this.b = paramFloat;
    setFloat(this.b, this.a, this.mFilterProgram);
  }
  
  public float getAnimation()
  {
    return this.c;
  }
  
  public void setAnimation(float paramFloat)
  {
    this.c = paramFloat;
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("strength", getStrength(), 0.0F, 1.0F);
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
    } else if (paramFilterArg.equalsKey("animation")) {
      setAnimation(paramFilterArg.getValue());
    }
  }
  
  private void a(long paramLong)
  {
    int i = 500;
    long l = System.currentTimeMillis();
    float f1 = (float)(l % i);
    float f2 = (float)Math.cos(f1 / 360.0D);
    getParameter().setFilterArg("strength", f2);
    submitParameter();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lives\TuSDKLiveSloshFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */