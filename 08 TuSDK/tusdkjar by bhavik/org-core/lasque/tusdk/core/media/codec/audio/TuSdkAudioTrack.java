package org.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
import android.media.AudioTimestamp;
import java.nio.ByteBuffer;

public abstract interface TuSdkAudioTrack
{
  public abstract int write(ByteBuffer paramByteBuffer);
  
  public abstract int getPlaybackHeadPosition();
  
  @TargetApi(19)
  public abstract boolean getTimestamp(AudioTimestamp paramAudioTimestamp);
  
  public abstract void play();
  
  public abstract void pause();
  
  public abstract int setVolume(float paramFloat);
  
  public abstract void flush();
  
  public abstract void release();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioTrack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */