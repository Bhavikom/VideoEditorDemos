package org.lasque.tusdk.core.delegate;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;

public abstract interface TuSDKAudioPacketDelegate
{
  public abstract void onAudioInfoReady(MediaFormat paramMediaFormat);
  
  public abstract void onAudioPacketAvailable(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\delegate\TuSDKAudioPacketDelegate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */