package org.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaListener;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class TuSdkAudioPitchHardImpl
  implements TuSdkAudioPitch
{
  private static final boolean a = SdkValid.isInit;
  private TuSdkAudioInfo b;
  private long c;
  private boolean d = false;
  private TuSdkAudioPitchSync e;
  private float f = 1.0F;
  private float g = 1.0F;
  private TuSdkMediaListener h = new TuSdkMediaListener()
  {
    public void onMediaOutputBuffer(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkAudioPitchHardImpl.a(TuSdkAudioPitchHardImpl.this) == null) {
        return;
      }
      TuSdkAudioPitchHardImpl.a(TuSdkAudioPitchHardImpl.this).syncAudioPitchOutputBuffer(paramAnonymousByteBuffer, paramAnonymousBufferInfo);
    }
  };
  
  public TuSdkAudioPitchHardImpl(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null) {
      throw new NullPointerException(String.format("%s outputInfo is empty.", new Object[] { "TuSdkAudioResampleHardImpl" }));
    }
    this.c = jniInit(paramTuSdkAudioInfo.channelCount, paramTuSdkAudioInfo.bitWidth, paramTuSdkAudioInfo.sampleRate, this.h);
    if (this.c == 0L) {
      throw new NullPointerException(String.format("%s Create hard failed.", new Object[] { "TuSdkAudioResampleHardImpl" }));
    }
    this.b = paramTuSdkAudioInfo;
  }
  
  public void release()
  {
    if (this.d) {
      return;
    }
    this.d = true;
    jniPitchCommand(this.c, 0, 0L);
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public void setMediaSync(TuSdkAudioPitchSync paramTuSdkAudioPitchSync)
  {
    this.e = paramTuSdkAudioPitchSync;
  }
  
  public void changeFormat(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null)
    {
      TLog.w("%s changeFormat need inputInfo.", new Object[] { "TuSdkAudioResampleHardImpl" });
      return;
    }
    this.b = paramTuSdkAudioInfo;
    jniChangeFormat(this.c, paramTuSdkAudioInfo.channelCount, paramTuSdkAudioInfo.bitWidth, paramTuSdkAudioInfo.sampleRate);
  }
  
  public void changePitch(float paramFloat)
  {
    if (!SdkValid.shared.audioPitchEffectsSupport())
    {
      TLog.e("You are not allowed to use audio pitch effect , please see https://tutucloud.com", new Object[0]);
      return;
    }
    if ((paramFloat <= 0.0F) || (this.g == paramFloat)) {
      return;
    }
    this.g = paramFloat;
    this.f = 1.0F;
    jniChangePitch(this.c, paramFloat);
  }
  
  public void changeSpeed(float paramFloat)
  {
    if ((paramFloat <= 0.0F) || (this.f == paramFloat)) {
      return;
    }
    this.f = paramFloat;
    this.g = 1.0F;
    jniChangeSpeed(this.c, paramFloat);
  }
  
  public boolean needPitch()
  {
    return jniPitchCommand(this.c, 64, 0L) > 0L;
  }
  
  public void reset()
  {
    this.g = 1.0F;
    this.f = 1.0F;
    jniPitchCommand(this.c, 16, 0L);
  }
  
  public void flush()
  {
    jniPitchCommand(this.c, 32, 0L);
  }
  
  public boolean queueInputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    return jniQueueInputBuffer(this.c, paramByteBuffer, paramBufferInfo.offset, paramBufferInfo.size, paramBufferInfo.flags, paramBufferInfo.presentationTimeUs);
  }
  
  private static native long jniInit(int paramInt1, int paramInt2, int paramInt3, Object paramObject);
  
  private static native void jniChangeFormat(long paramLong, int paramInt1, int paramInt2, int paramInt3);
  
  private static native void jniChangeSpeed(long paramLong, float paramFloat);
  
  private static native void jniChangePitch(long paramLong, float paramFloat);
  
  private static native long jniPitchCommand(long paramLong1, int paramInt, long paramLong2);
  
  private static native boolean jniQueueInputBuffer(long paramLong1, ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, long paramLong2);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioPitchHardImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */