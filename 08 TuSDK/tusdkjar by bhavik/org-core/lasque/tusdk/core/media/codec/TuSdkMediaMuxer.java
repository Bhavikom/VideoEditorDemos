package org.lasque.tusdk.core.media.codec;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.nio.ByteBuffer;

public abstract interface TuSdkMediaMuxer
{
  public abstract int addTrack(MediaFormat paramMediaFormat);
  
  public abstract void writeSampleData(int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\TuSdkMediaMuxer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */