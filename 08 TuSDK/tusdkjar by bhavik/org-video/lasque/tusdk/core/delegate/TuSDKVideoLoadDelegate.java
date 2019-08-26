package org.lasque.tusdk.core.delegate;

import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;

public abstract interface TuSDKVideoLoadDelegate
{
  public abstract void onProgressChaned(float paramFloat);
  
  public abstract void onLoadComplete(TuSDKVideoInfo paramTuSDKVideoInfo);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\delegate\TuSDKVideoLoadDelegate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */