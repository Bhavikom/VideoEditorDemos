package org.lasque.tusdk.impl.view.widget;

import android.graphics.RectF;
import org.lasque.tusdk.core.struct.TuSdkSize;

public abstract interface RegionHandler
{
  public abstract void setRatio(float paramFloat);
  
  public abstract float getRatio();
  
  public abstract void setWrapSize(TuSdkSize paramTuSdkSize);
  
  public abstract TuSdkSize getWrapSize();
  
  public abstract RectF getRectPercent();
  
  public abstract RectF getCenterRectPercent();
  
  public abstract void setOffsetTopPercent(float paramFloat);
  
  public abstract float getOffsetTopPercent();
  
  public abstract RectF changeWithRatio(float paramFloat, RegionChangerListener paramRegionChangerListener);
  
  public static abstract interface RegionChangerListener
  {
    public abstract void onRegionChanged(RectF paramRectF);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\RegionHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */