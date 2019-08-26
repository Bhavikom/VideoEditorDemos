package org.lasque.tusdk.core.media.codec.audio;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;

public abstract interface TuSdkAudioPitch
{
  public abstract void setMediaSync(TuSdkAudioPitchSync paramTuSdkAudioPitchSync);
  
  public abstract void changeFormat(TuSdkAudioInfo paramTuSdkAudioInfo);
  
  public abstract void changePitch(float paramFloat);
  
  public abstract void changeSpeed(float paramFloat);
  
  public abstract boolean needPitch();
  
  public abstract void reset();
  
  public abstract void flush();
  
  public abstract boolean queueInputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void release();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioPitch.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */