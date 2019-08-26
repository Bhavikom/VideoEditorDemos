package org.lasque.tusdk.core.seles.tusdk.filters.base;

import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.image.SelesSharpenFilter;

public class TuSDKSharpenFilter
  extends SelesSharpenFilter
  implements SelesParameters.FilterParameterInterface
{
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("sharpness", getSharpness(), -1.0F, 1.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("sharpness")) {
      setSharpness(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\base\TuSDKSharpenFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */