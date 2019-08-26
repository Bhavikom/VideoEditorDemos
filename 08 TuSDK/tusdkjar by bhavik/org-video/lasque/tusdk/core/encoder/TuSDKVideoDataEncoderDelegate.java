package org.lasque.tusdk.core.encoder;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;

public abstract interface TuSDKVideoDataEncoderDelegate
{
  public abstract void onVideoEncoderStarted(MediaFormat paramMediaFormat);
  
  public abstract void onVideoEncoderFrameDataAvailable(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void onVideoEncoderCodecConfig(long paramLong, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\encoder\TuSDKVideoDataEncoderDelegate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */