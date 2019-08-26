package org.lasque.tusdk.core.media.codec.audio;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;

public abstract interface TuSdkAudioResample
{
  public abstract void setMediaSync(TuSdkAudioResampleSync paramTuSdkAudioResampleSync);
  
  public abstract void changeFormat(TuSdkAudioInfo paramTuSdkAudioInfo);
  
  public abstract void changeSpeed(float paramFloat);
  
  public abstract void changeSequence(boolean paramBoolean);
  
  public abstract void setStartPrefixTimeUs(long paramLong);
  
  public abstract boolean needResample();
  
  public abstract void reset();
  
  public abstract void flush();
  
  public abstract long getLastInputTimeUs();
  
  public abstract long getPrefixTimeUs();
  
  public abstract boolean queueInputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void release();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioResample.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */