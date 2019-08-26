package org.lasque.tusdk.core.seles.tusdk.filters.colors;

import java.util.HashMap;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;

public class TuSDKColorMixedFilter
  extends SelesTwoInputFilter
  implements SelesParameters.FilterParameterInterface
{
  private float a = 1.0F;
  private int b;
  
  public TuSDKColorMixedFilter()
  {
    super("-sc1");
    disableSecondFrameCheck();
  }
  
  public TuSDKColorMixedFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null) && (paramFilterOption.args.containsKey("mixied")))
    {
      float f = Float.parseFloat((String)paramFilterOption.args.get("mixied"));
      if (f > 0.0F) {
        setMixed(f);
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.b = this.mFilterProgram.uniformIndex("mixturePercent");
    setMixed(this.a);
  }
  
  public float getMixed()
  {
    return this.a;
  }
  
  public void setMixed(float paramFloat)
  {
    this.a = paramFloat;
    setFloat(this.a, this.b, this.mFilterProgram);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("mixied", getMixed());
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("mixied")) {
      setMixed(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\colors\TuSDKColorMixedFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */