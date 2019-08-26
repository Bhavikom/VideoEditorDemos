package org.lasque.tusdk.core.media.codec.sync;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.TuSdkMediaSync;

public abstract interface TuSdkAudioPitchSync
  extends TuSdkMediaSync
{
  public abstract void syncAudioPitchOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkAudioPitchSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */