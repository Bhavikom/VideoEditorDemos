package org.lasque.tusdk.core.decoder;

import android.media.MediaCodec;
import android.media.MediaFormat;

public abstract interface TuSDKMediaDecoderInterface<T extends TuSDKMovieReader>
{
  public abstract T getMediaReader();
  
  public abstract MediaCodec getVideoDecoder();
  
  public abstract MediaCodec getAudioDecoder();
  
  public abstract void start();
  
  public abstract void stop();
  
  public abstract long getCurrentSampleTimeUs();
  
  public abstract int findVideoTrack();
  
  public abstract int selectVideoTrack();
  
  public abstract void unselectVideoTrack();
  
  public abstract MediaFormat getVideoTrackFormat();
  
  public abstract int findAudioTrack();
  
  public abstract int selectAudioTrack();
  
  public abstract void unselectAudioTrack();
  
  public abstract MediaFormat getAudioTrackFormat();
  
  public abstract void destroy();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKMediaDecoderInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */