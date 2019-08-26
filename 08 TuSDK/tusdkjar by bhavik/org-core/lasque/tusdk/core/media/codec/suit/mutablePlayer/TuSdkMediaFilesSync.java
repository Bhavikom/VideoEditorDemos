package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaEncodecSync;
import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;

public abstract interface TuSdkMediaFilesSync
  extends TuSdkMediaEncodecSync
{
  public abstract long benchmarkUs();
  
  public abstract void setBenchmarkEnd();
  
  public abstract float calculateProgress();
  
  public abstract long totalDurationUs();
  
  public abstract boolean isEncodecCompleted();
  
  public abstract void syncVideoEncodecDrawFrame(long paramLong, boolean paramBoolean, TuSdkRecordSurface paramTuSdkRecordSurface, TuSdkEncodeSurface paramTuSdkEncodeSurface);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\TuSdkMediaFilesSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */