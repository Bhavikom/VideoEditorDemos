package org.lasque.tusdk.core.media.codec.sync;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.TuSdkMediaSync;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;

public abstract interface TuSdkAudioDecodecSync
  extends TuSdkMediaSync
{
  public abstract void syncAudioDecodeCompleted();
  
  public abstract boolean isAudioDecodeCompleted();
  
  public abstract boolean isAudioDecodeCrashed();
  
  public abstract boolean hasAudioDecodeTrack();
  
  public abstract void syncAudioDecodeCrashed(Exception paramException);
  
  public abstract void syncAudioDecodecInfo(TuSdkAudioInfo paramTuSdkAudioInfo, TuSdkMediaExtractor paramTuSdkMediaExtractor);
  
  public abstract boolean syncAudioDecodecExtractor(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec);
  
  public abstract void syncAudioDecodecOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkAudioInfo paramTuSdkAudioInfo);
  
  public abstract void syncAudioDecodecUpdated(MediaCodec.BufferInfo paramBufferInfo);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkAudioDecodecSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */