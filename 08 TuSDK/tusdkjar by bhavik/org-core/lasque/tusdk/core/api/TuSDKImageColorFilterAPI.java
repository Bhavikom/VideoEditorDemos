package org.lasque.tusdk.core.api;

import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorAdjustmentFilter;

public final class TuSDKImageColorFilterAPI
  extends TuSDKImageFilterAPI
{
  public static final String KEY_BRIGHTNESS = "brightness";
  public static final String KEY_CONTRAST = "contrast";
  public static final String KEY_SATURATION = "saturation";
  public static final String KEY_EXPOSURE = "exposure";
  public static final String KEY_SHADOWS = "shadows";
  public static final String KEY_HIGHLIGHTS = "highlights";
  public static final String KEY_TEMPERATURE = "temperature";
  private FilterWrap a;
  
  protected FilterWrap getFilterWrap()
  {
    if (this.a == null)
    {
      FilterOption local1 = new FilterOption()
      {
        public SelesOutInput getFilter()
        {
          return new TuSDKColorAdjustmentFilter();
        }
      };
      local1.id = Long.MAX_VALUE;
      local1.canDefinition = true;
      local1.isInternal = true;
      this.a = FilterWrap.creat(local1);
    }
    return this.a;
  }
  
  public void setBrightnessPrecentValue(float paramFloat)
  {
    setFilterArgPrecentValue("brightness", paramFloat);
  }
  
  public void setContrastPrecentValue(float paramFloat)
  {
    setFilterArgPrecentValue("contrast", paramFloat);
  }
  
  public void setSaturationPrecentValue(float paramFloat)
  {
    setFilterArgPrecentValue("saturation", paramFloat);
  }
  
  public void setExposurePrecentValue(float paramFloat)
  {
    setFilterArgPrecentValue("exposure", paramFloat);
  }
  
  public void setShadowsPrecentValue(float paramFloat)
  {
    setFilterArgPrecentValue("shadows", paramFloat);
  }
  
  public void setHighlightsPrecentValue(float paramFloat)
  {
    setFilterArgPrecentValue("highlights", paramFloat);
  }
  
  public void setTemperaturePrecentValue(float paramFloat)
  {
    setFilterArgPrecentValue("temperature", paramFloat);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\TuSDKImageColorFilterAPI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */