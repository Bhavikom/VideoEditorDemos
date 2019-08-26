package org.lasque.tusdk.api.audio.preproc.processor;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchSoftImpl;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
import org.lasque.tusdk.core.utils.TLog;

public final class TuSdkAudioPitchEngine
  implements TuSdkAudioEngine
{
  private TuSdkAudioPitch a;
  private TuSdkSoundPitchType b = TuSdkSoundPitchType.Normal;
  private TuSdkAudioEnginePitchTypeChangeDelegate c;
  private TuSdkAudioEngine.TuSdKAudioEngineOutputBufferDelegate d;
  private TuSdkAudioPitchSync e = new TuSdkAudioPitchSync()
  {
    public void syncAudioPitchOutputBuffer(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkAudioPitchEngine.this.processOutputBuffer(paramAnonymousByteBuffer, paramAnonymousBufferInfo);
    }
    
    public void release() {}
  };
  
  public TuSdkAudioPitchEngine(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    this(paramTuSdkAudioInfo, true);
  }
  
  public TuSdkAudioPitchEngine(TuSdkAudioInfo paramTuSdkAudioInfo, boolean paramBoolean)
  {
    if (paramTuSdkAudioInfo == null) {
      paramTuSdkAudioInfo = new TuSdkAudioInfo();
    }
    this.a = a(paramTuSdkAudioInfo, paramBoolean);
    this.a.setMediaSync(this.e);
  }
  
  private TuSdkAudioPitch a(TuSdkAudioInfo paramTuSdkAudioInfo, boolean paramBoolean)
  {
    if (paramBoolean) {
      return new TuSdkAudioPitchHardImpl(paramTuSdkAudioInfo);
    }
    return new TuSdkAudioPitchSoftImpl(paramTuSdkAudioInfo);
  }
  
  public void changeAudioInfo(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null)
    {
      TLog.e(" %s change the AudioInfo is null !!!", new Object[] { "TuSdkAudioPitchEngine" });
      return;
    }
    this.a.changeFormat(paramTuSdkAudioInfo);
  }
  
  public void setSoundPitchType(TuSdkSoundPitchType paramTuSdkSoundPitchType)
  {
    if (this.b == null) {
      return;
    }
    this.b = paramTuSdkSoundPitchType;
    if (paramTuSdkSoundPitchType.b != 1.0F) {
      this.a.changePitch(paramTuSdkSoundPitchType.b);
    }
    if (paramTuSdkSoundPitchType.a != 1.0F) {
      this.a.changeSpeed(paramTuSdkSoundPitchType.a);
    }
    a(paramTuSdkSoundPitchType);
  }
  
  public TuSdkSoundPitchType getSoundType()
  {
    return this.b;
  }
  
  public void setOutputBufferDelegate(TuSdkAudioEngine.TuSdKAudioEngineOutputBufferDelegate paramTuSdKAudioEngineOutputBufferDelegate)
  {
    this.d = paramTuSdKAudioEngineOutputBufferDelegate;
  }
  
  public void setSoundTypeChangeListener(TuSdkAudioEnginePitchTypeChangeDelegate paramTuSdkAudioEnginePitchTypeChangeDelegate)
  {
    this.c = paramTuSdkAudioEnginePitchTypeChangeDelegate;
  }
  
  public void processInputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    this.a.queueInputBuffer(paramByteBuffer, paramBufferInfo);
  }
  
  protected void processOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (this.d == null) {
      return;
    }
    this.d.onProcess(paramByteBuffer, paramBufferInfo);
  }
  
  public void flush()
  {
    this.a.flush();
    this.a.reset();
  }
  
  public void reset()
  {
    this.a.reset();
    setSoundPitchType(TuSdkSoundPitchType.Normal);
  }
  
  public void release()
  {
    this.a.release();
  }
  
  private void a(TuSdkSoundPitchType paramTuSdkSoundPitchType)
  {
    if (this.c == null) {
      return;
    }
    this.c.onSoundTypeChanged(paramTuSdkSoundPitchType);
  }
  
  public static abstract interface TuSdkAudioEnginePitchTypeChangeDelegate
  {
    public abstract void onSoundTypeChanged(TuSdkAudioPitchEngine.TuSdkSoundPitchType paramTuSdkSoundPitchType);
  }
  
  public static enum TuSdkSoundPitchType
  {
    float a;
    float b;
    
    private TuSdkSoundPitchType(float paramFloat1, float paramFloat2)
    {
      this.a = paramFloat1;
      this.b = paramFloat2;
    }
    
    public float getSpeed()
    {
      return this.a;
    }
    
    public float getPitch()
    {
      return this.b;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\audio\preproc\processor\TuSdkAudioPitchEngine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */