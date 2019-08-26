package org.lasque.tusdk.core.api.extend;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;

public abstract interface TuSdkAudioRender
{
  public abstract boolean onAudioSliceRender(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkAudioRenderCallback paramTuSdkAudioRenderCallback);
  
  public static abstract interface TuSdkAudioRenderCallback
  {
    public abstract boolean isEncodec();
    
    public abstract TuSdkAudioInfo getAudioInfo();
    
    public abstract void returnRenderBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\extend\TuSdkAudioRender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */