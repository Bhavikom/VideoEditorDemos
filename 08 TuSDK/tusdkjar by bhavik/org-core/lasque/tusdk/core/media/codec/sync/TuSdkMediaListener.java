package org.lasque.tusdk.core.media.codec.sync;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;

@TargetApi(16)
public abstract class TuSdkMediaListener
{
  public abstract void onMediaOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  public void onMediaOutputBuffer(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, long paramLong)
  {
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    localBufferInfo.set(paramInt1, paramInt2, paramLong, paramInt3);
    paramByteBuffer.clear();
    paramByteBuffer.position(paramInt1);
    paramByteBuffer.limit(paramInt1 + paramInt2);
    onMediaOutputBuffer(paramByteBuffer, localBufferInfo);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkMediaListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */