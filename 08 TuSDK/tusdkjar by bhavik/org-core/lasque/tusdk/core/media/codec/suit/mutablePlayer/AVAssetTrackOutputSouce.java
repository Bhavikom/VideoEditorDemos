package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

public abstract interface AVAssetTrackOutputSouce
{
  public abstract boolean seekTo(long paramLong, int paramInt);
  
  public abstract AVSampleBuffer readNextSampleBuffer(int paramInt);
  
  public abstract AVSampleBuffer readSampleBuffer(int paramInt);
  
  public abstract void setTimeRange(AVTimeRange paramAVTimeRange);
  
  public abstract boolean advance();
  
  public abstract boolean isDecodeOnly(long paramLong);
  
  public abstract boolean isOutputDone();
  
  public abstract void reset();
  
  public abstract void setAlwaysCopiesSampleData(boolean paramBoolean);
  
  public abstract AVAssetTrack inputTrack();
  
  public abstract long durationTimeUs();
  
  public abstract long outputTimeUs();
  
  public abstract long calOutputTimeUs(long paramLong);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVAssetTrackOutputSouce.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */