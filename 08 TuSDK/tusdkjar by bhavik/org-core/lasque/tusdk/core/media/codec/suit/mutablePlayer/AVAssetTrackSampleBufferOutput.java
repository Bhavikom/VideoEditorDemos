package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.media.MediaFormat;
import java.util.List;

public abstract interface AVAssetTrackSampleBufferOutput<Target extends AVAssetTrackSampleBufferInput>
{
  public abstract List<Target> targets();
  
  public abstract void addTarget(Target paramTarget, int paramInt);
  
  public abstract void addTarget(Target paramTarget);
  
  public abstract void removeTarget(Target paramTarget);
  
  public static abstract interface AVAssetTrackSampleBufferInput
  {
    public abstract void newFrameReady(AVSampleBuffer paramAVSampleBuffer);
    
    public abstract void outputFormatChaned(MediaFormat paramMediaFormat, AVAssetTrack paramAVAssetTrack);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVAssetTrackSampleBufferOutput.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */