package org.lasque.tusdk.core.media.codec.audio;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;

public abstract interface TuSdkAudioEncodecOperation
  extends TuSdkEncodecOperation
{
  public abstract void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender);
  
  public abstract TuSdkAudioInfo getAudioInfo();
  
  public abstract int writeBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void autoFillMuteData(long paramLong1, long paramLong2, boolean paramBoolean);
  
  public abstract void autoFillMuteDataWithinEnd(long paramLong1, long paramLong2);
  
  public abstract void signalEndOfInputStream(long paramLong);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioEncodecOperation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */