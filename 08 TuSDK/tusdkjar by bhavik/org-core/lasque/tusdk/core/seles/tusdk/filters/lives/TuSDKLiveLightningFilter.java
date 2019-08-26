package org.lasque.tusdk.core.seles.tusdk.filters.lives;

import java.nio.FloatBuffer;
import java.util.HashMap;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKLiveLightningFilter
  extends SelesFilter
{
  private int c;
  private float d = -0.5F;
  int a;
  int b;
  
  public TuSDKLiveLightningFilter()
  {
    super("-slive07f");
  }
  
  public TuSDKLiveLightningFilter(FilterOption paramFilterOption)
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
    this.c = this.mFilterProgram.uniformIndex("strength");
    setStrength(this.d);
    checkGLError("TuSDKLiveLightningFilter onInitOnGLThread");
  }
  
  protected void informTargetsAboutNewFrame(long paramLong)
  {
    a();
    super.informTargetsAboutNewFrame(paramLong);
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    checkGLError("TuSDKLiveLightningFilter");
    captureFilterImage("TuSDKLiveLightningFilter", this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
  
  public float getStrength()
  {
    return this.d;
  }
  
  public void setStrength(float paramFloat)
  {
    this.d = paramFloat;
    setFloat(this.d, this.c, this.mFilterProgram);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("strength", getStrength(), -0.5F, 0.7F);
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
    int[] arrayOfInt = { 14, 15, 16, 18, 19, 21, 22, 23, 27, 30, 33, 34, 35, 37 };
    float[] arrayOfFloat = { -0.5F, 0.6F, 0.7F, -0.5F, 0.7F, -0.5F, 0.6F, 0.7F, -0.5F, 0.7F, 0.6F, 0.7F, 0.6F, 0.7F };
    if (this.a == 0) {
      this.b = 0;
    }
    this.a += 1;
    int i = arrayOfInt[this.b];
    if (this.a > i) {
      this.b += 1;
    }
    getParameter().setFilterArg("strength", arrayOfFloat[this.b]);
    if (this.a >= 37) {
      this.a = 0;
    }
    submitParameter();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lives\TuSDKLiveLightningFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */