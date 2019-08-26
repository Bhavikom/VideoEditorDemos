package org.lasque.tusdk.core.seles.tusdk.filters.lives;

import java.nio.FloatBuffer;
import java.util.HashMap;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKLiveXRayFilter
  extends SelesFilter
{
  private int b;
  private float c = 0.0F;
  int a;
  
  public TuSDKLiveXRayFilter()
  {
    super("-slive08f");
  }
  
  public TuSDKLiveXRayFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null) && (paramFilterOption.args.containsKey("strength")))
    {
      float f = Float.parseFloat((String)paramFilterOption.args.get("strength"));
      if (f > 0.0F) {
        setStrength(f);
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.b = this.mFilterProgram.uniformIndex("strength");
    setStrength(this.c);
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
    return this.c;
  }
  
  public void setStrength(float paramFloat)
  {
    this.c = paramFloat;
    setFloat(this.c, this.b, this.mFilterProgram);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("strength", getStrength(), 0.0F, 1.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("strength")) {
      setStrength(paramFilterArg.getValue());
    }
  }
  
  private void a()
  {
    if (this.a < 3) {
      getParameter().setFilterArg("strength", 0.0F);
    } else {
      getParameter().setFilterArg("strength", 1.0F);
    }
    this.a += 1;
    if (this.a >= 6) {
      this.a = 0;
    }
    submitParameter();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lives\TuSDKLiveXRayFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */