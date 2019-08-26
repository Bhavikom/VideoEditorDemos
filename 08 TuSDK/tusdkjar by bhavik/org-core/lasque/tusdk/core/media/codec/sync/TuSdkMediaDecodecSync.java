package org.lasque.tusdk.core.media.codec.sync;

import org.lasque.tusdk.core.media.codec.TuSdkMediaSync;

public abstract interface TuSdkMediaDecodecSync
  extends TuSdkMediaSync
{
  public abstract TuSdkAudioDecodecSync buildAudioDecodecSync();
  
  public abstract TuSdkVideoDecodecSync buildVideoDecodecSync();
  
  public abstract TuSdkVideoDecodecSync getVideoDecodecSync();
  
  public abstract TuSdkAudioDecodecSync getAudioDecodecSync();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkMediaDecodecSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */