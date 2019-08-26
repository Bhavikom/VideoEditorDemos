package org.lasque.tusdk.core.media.codec.audio;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;

public abstract interface TuSdkAudioRecord
{
  public abstract void setAudioInfo(TuSdkAudioInfo paramTuSdkAudioInfo);
  
  public abstract void setListener(TuSdkAudioRecordListener paramTuSdkAudioRecordListener);
  
  public abstract void startRecording();
  
  public abstract void stop();
  
  public abstract void release();
  
  public static abstract interface TuSdkAudioRecordListener
  {
    public static final int PARAMETRTS_ERROR = 2001;
    public static final int PERMISSION_ERROR = 2002;
    
    public abstract void onAudioRecordOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo);
    
    public abstract void onAudioRecordError(int paramInt);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioRecord.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */