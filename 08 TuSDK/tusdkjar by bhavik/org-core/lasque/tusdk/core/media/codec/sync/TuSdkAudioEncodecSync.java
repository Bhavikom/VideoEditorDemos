package org.lasque.tusdk.core.media.codec.sync;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import org.lasque.tusdk.core.media.codec.TuSdkMediaSync;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;

public abstract interface TuSdkAudioEncodecSync
  extends TuSdkMediaSync
{
  public abstract boolean isAudioEncodeCompleted();
  
  public abstract void syncAudioEncodecInfo(TuSdkAudioInfo paramTuSdkAudioInfo);
  
  public abstract void syncAudioEncodecOutputBuffer(TuSdkMediaMuxer paramTuSdkMediaMuxer, int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void syncAudioEncodecUpdated(MediaCodec.BufferInfo paramBufferInfo);
  
  public abstract void syncAudioEncodecCompleted();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkAudioEncodecSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */