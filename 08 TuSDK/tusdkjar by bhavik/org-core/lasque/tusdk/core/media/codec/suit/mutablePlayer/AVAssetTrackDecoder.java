package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.media.MediaFormat;

public abstract interface AVAssetTrackDecoder
  extends AVAssetTrackSampleBufferOutput<AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput>
{
  public abstract boolean feedInputBuffer();
  
  public abstract boolean drainOutputBuffer();
  
  public abstract boolean renderOutputBuffers();
  
  public abstract boolean renderOutputBuffer();
  
  public abstract void onInputFormatChanged(MediaFormat paramMediaFormat);
  
  public abstract long durationTimeUs();
  
  public abstract long outputTimeUs();
  
  public abstract boolean seekTo(long paramLong, boolean paramBoolean);
  
  public abstract boolean isDecodeCompleted();
  
  public abstract void reset();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVAssetTrackDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */