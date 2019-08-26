package org.lasque.tusdk.api.audio.preproc.processor;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;

public abstract interface TuSdkAudioEngine
{
  public abstract void processInputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void changeAudioInfo(TuSdkAudioInfo paramTuSdkAudioInfo);
  
  public abstract void reset();
  
  public abstract void release();
  
  public static abstract interface TuSdKAudioEngineOutputBufferDelegate
  {
    public abstract void onProcess(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\audio\preproc\processor\TuSdkAudioEngine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */