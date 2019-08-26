package org.lasque.tusdk.core.seles.tusdk.filters.base;

import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;

public class TuSDKGaussianBlurSevenRadiusFilter
  extends TuSDKGaussianBlurFiveRadiusFilter
{
  public TuSDKGaussianBlurSevenRadiusFilter()
  {
    super("-sgv7", "-sgf7");
  }
  
  public static TuSDKGaussianBlurFiveRadiusFilter hardware(boolean paramBoolean)
  {
    if ((paramBoolean) && (!TuSdkGPU.lowPerformance())) {
      return new TuSDKGaussianBlurSevenRadiusFilter();
    }
    return new TuSDKGaussianBlurFiveRadiusFilter(true);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\base\TuSDKGaussianBlurSevenRadiusFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */