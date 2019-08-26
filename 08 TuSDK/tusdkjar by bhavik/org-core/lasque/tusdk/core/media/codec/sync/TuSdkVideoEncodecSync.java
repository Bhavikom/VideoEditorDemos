package org.lasque.tusdk.core.media.codec.sync;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import org.lasque.tusdk.core.media.codec.TuSdkMediaSync;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;

public abstract interface TuSdkVideoEncodecSync
  extends TuSdkMediaSync
{
  public abstract boolean isVideoEncodeCompleted();
  
  public abstract void syncEncodecVideoInfo(TuSdkVideoInfo paramTuSdkVideoInfo);
  
  public abstract void syncVideoEncodecOutputBuffer(TuSdkMediaMuxer paramTuSdkMediaMuxer, int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void syncVideoEncodecUpdated(MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void syncVideoEncodecCompleted();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkVideoEncodecSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */