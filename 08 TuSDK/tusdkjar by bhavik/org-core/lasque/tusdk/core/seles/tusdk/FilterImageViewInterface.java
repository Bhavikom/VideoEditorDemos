package org.lasque.tusdk.core.seles.tusdk;

import android.graphics.Bitmap;

public abstract interface FilterImageViewInterface
{
  public abstract void setImage(Bitmap paramBitmap);
  
  public abstract void setImageBackgroundColor(int paramInt);
  
  public abstract void setFilterWrap(FilterWrap paramFilterWrap);
  
  public abstract void enableTouchForOrigin();
  
  public abstract void disableTouchForOrigin();
  
  public abstract void requestRender();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\FilterImageViewInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */