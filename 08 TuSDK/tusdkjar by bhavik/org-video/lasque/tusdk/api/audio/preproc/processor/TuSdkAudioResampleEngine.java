package org.lasque.tusdk.api.audio.preproc.processor;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;

public class TuSdkAudioResampleEngine
  implements TuSdkAudioEngine
{
  private TuSdkAudioResample a;
  private TuSdkAudioResampleSync b = new TuSdkAudioResampleSync()
  {
    public void syncAudioResampleOutputBuffer(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkAudioResampleEngine.this.processOutputBuffer(paramAnonymousByteBuffer, paramAnonymousBufferInfo);
    }
    
    public void release() {}
  };
  private TuSdkAudioEngine.TuSdKAudioEngineOutputBufferDelegate c;
  
  public TuSdkAudioResampleEngine(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (!a(paramTuSdkAudioInfo)) {
      throw new IllegalArgumentException(String.format("%s outputAudioInfo is error . audioInfo : %s", new Object[] { "TuSdkAudioResampleEngine", paramTuSdkAudioInfo }));
    }
    this.a = new TuSdkAudioResampleHardImpl(paramTuSdkAudioInfo);
    this.a.setMediaSync(this.b);
  }
  
  public void processInputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    this.a.queueInputBuffer(paramByteBuffer, paramBufferInfo);
  }
  
  public void processOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (this.c == null) {
      return;
    }
    this.c.onProcess(paramByteBuffer, paramBufferInfo);
  }
  
  public void changeAudioInfo(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (!a(paramTuSdkAudioInfo)) {
      throw new IllegalArgumentException(String.format("%s inputAudioInfo is error . audioInfo : %s", new Object[] { "TuSdkAudioResampleEngine", paramTuSdkAudioInfo }));
    }
    this.a.changeFormat(paramTuSdkAudioInfo);
  }
  
  public void setOutputBufferDelegate(TuSdkAudioEngine.TuSdKAudioEngineOutputBufferDelegate paramTuSdKAudioEngineOutputBufferDelegate)
  {
    this.c = paramTuSdKAudioEngineOutputBufferDelegate;
  }
  
  public void reset()
  {
    this.a.reset();
  }
  
  public void release()
  {
    this.a.release();
  }
  
  private boolean a(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null) {
      return false;
    }
    return (paramTuSdkAudioInfo.bitWidth != 0) && (paramTuSdkAudioInfo.channelCount != 0) && (paramTuSdkAudioInfo.sampleRate != 0);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\audio\preproc\processor\TuSdkAudioResampleEngine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */