package org.lasque.tusdk.core.delegate;

import org.lasque.tusdk.core.video.TuSDKVideoResult;

public abstract interface TuSDKVideoSaveDelegate
{
  public abstract void onProgressChaned(float paramFloat);
  
  public abstract void onSaveResult(TuSDKVideoResult paramTuSDKVideoResult);
  
  public abstract void onResultFail(Exception paramException);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\delegate\TuSDKVideoSaveDelegate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */