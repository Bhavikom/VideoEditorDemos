package org.lasque.tusdk.core.media.codec.sync;

import org.lasque.tusdk.core.media.codec.TuSdkMediaSync;

public abstract interface TuSdkMediaEncodecSync
  extends TuSdkMediaSync
{
  public abstract TuSdkAudioEncodecSync getAudioEncodecSync();
  
  public abstract TuSdkVideoEncodecSync getVideoEncodecSync();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkMediaEncodecSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */