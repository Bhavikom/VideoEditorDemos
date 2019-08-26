package org.lasque.tusdk.core.media.codec.sync;

import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;

public abstract interface TuSdkMediaFileSync
  extends TuSdkMediaDecodecSync, TuSdkMediaEncodecSync
{
  public abstract long benchmarkUs();
  
  public abstract void setBenchmarkEnd();
  
  public abstract float calculateProgress();
  
  public abstract void setTimeline(TuSdkMediaTimeline paramTuSdkMediaTimeline);
  
  public abstract void addAudioEncodecOperation(TuSdkAudioEncodecOperation paramTuSdkAudioEncodecOperation);
  
  public abstract long totalDurationUs();
  
  public abstract long processedUs();
  
  public abstract long lastVideoDecodecTimestampNs();
  
  public abstract boolean isEncodecCompleted();
  
  public abstract void syncVideoDecodeCompleted();
  
  public abstract boolean isVideoDecodeCompleted();
  
  public abstract void syncAudioDecodeCompleted();
  
  public abstract boolean isAudioDecodeCompleted();
  
  public abstract boolean isAudioDecodeCrashed();
  
  public abstract void syncVideoEncodecDrawFrame(long paramLong, boolean paramBoolean, TuSdkRecordSurface paramTuSdkRecordSurface, TuSdkEncodeSurface paramTuSdkEncodeSurface);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkMediaFileSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */