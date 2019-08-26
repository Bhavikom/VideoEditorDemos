package org.lasque.tusdk.core.media.codec.sync;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.TuSdkMediaSync;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;

public abstract interface TuSdkVideoDecodecSync
  extends TuSdkMediaSync
{
  public abstract void syncVideoDecodeCompleted();
  
  public abstract boolean isVideoDecodeCompleted();
  
  public abstract boolean isVideoDecodeCrashed();
  
  public abstract boolean hasVideoDecodeTrack();
  
  public abstract void syncVideoDecodeCrashed(Exception paramException);
  
  public abstract void syncVideoDecodecInfo(TuSdkVideoInfo paramTuSdkVideoInfo, TuSdkMediaExtractor paramTuSdkMediaExtractor);
  
  public abstract boolean syncVideoDecodecExtractor(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec);
  
  public abstract void syncVideoDecodecOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkVideoInfo paramTuSdkVideoInfo);
  
  public abstract void syncVideoDecodecUpdated(MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract long calcInputTimeUs(long paramLong);
  
  public abstract long calcEffectFrameTimeUs(long paramLong);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkVideoDecodecSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */