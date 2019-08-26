package org.lasque.tusdk.core.seles.tusdk.filters.colors;

import java.nio.FloatBuffer;
import java.util.HashMap;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKColorSelectiveFilter
  extends SelesFilter
  implements SelesParameters.FilterParameterInterface
{
  private float a = 0.0F;
  private float b = 0.2F;
  private int c;
  private int d;
  
  public TuSDKColorSelectiveFilter()
  {
    super("-sc4");
  }
  
  public TuSDKColorSelectiveFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null))
    {
      float f;
      if (paramFilterOption.args.containsKey("hue"))
      {
        f = Float.parseFloat((String)paramFilterOption.args.get("hue"));
        if (f > 0.0F) {
          a(f);
        }
      }
      if (paramFilterOption.args.containsKey("hueSpace"))
      {
        f = Float.parseFloat((String)paramFilterOption.args.get("hueSpace"));
        if (f > 0.0F) {
          b(f);
        }
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.c = this.mFilterProgram.uniformIndex("hue");
    this.d = this.mFilterProgram.uniformIndex("hueSpace");
    a(this.a);
    b(this.b);
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
    setFloat(this.a, this.c, this.mFilterProgram);
  }
  
  private float b()
  {
    return this.b;
  }
  
  private void b(float paramFloat)
  {
    this.b = paramFloat;
    setFloat(this.b, this.d, this.mFilterProgram);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("hue", a());
    paramSelesParameters.appendFloatArg("hueSpace", b());
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("hue")) {
      a(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("hueSpace")) {
      b(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\colors\TuSDKColorSelectiveFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */