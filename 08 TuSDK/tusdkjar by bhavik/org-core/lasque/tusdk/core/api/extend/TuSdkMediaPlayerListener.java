package org.lasque.tusdk.core.api.extend;

import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public abstract interface TuSdkMediaPlayerListener
{
  public abstract void onStateChanged(int paramInt);
  
  public abstract void onFrameAvailable();
  
  public abstract void onProgress(long paramLong1, TuSdkMediaDataSource paramTuSdkMediaDataSource, long paramLong2);
  
  public abstract void onCompleted(Exception paramException, TuSdkMediaDataSource paramTuSdkMediaDataSource);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\extend\TuSdkMediaPlayerListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */