package org.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaListener;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class TuSdkAudioResampleHardImpl
  implements TuSdkAudioResample
{
  private static final boolean a = SdkValid.isInit;
  private TuSdkAudioInfo b;
  private TuSdkAudioInfo c;
  private long d;
  private boolean e = false;
  private TuSdkAudioResampleSync f;
  private float g = 1.0F;
  private boolean h = false;
  private TuSdkMediaListener i = new TuSdkMediaListener()
  {
    public void onMediaOutputBuffer(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkAudioResampleHardImpl.a(TuSdkAudioResampleHardImpl.this) == null) {
        return;
      }
      TuSdkAudioResampleHardImpl.a(TuSdkAudioResampleHardImpl.this).syncAudioResampleOutputBuffer(paramAnonymousByteBuffer, paramAnonymousBufferInfo);
    }
  };
  
  public TuSdkAudioResampleHardImpl(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null) {
      throw new NullPointerException(String.format("%s outputInfo is empty.", new Object[] { "TuSdkAudioResampleHardImpl" }));
    }
    this.d = jniInit(paramTuSdkAudioInfo.channelCount, paramTuSdkAudioInfo.bitWidth, paramTuSdkAudioInfo.sampleRate, this.i);
    if (this.d == 0L) {
      throw new NullPointerException(String.format("%s Create hard failed.", new Object[] { "TuSdkAudioResampleHardImpl" }));
    }
    this.c = paramTuSdkAudioInfo;
  }
  
  public void release()
  {
    if (this.e) {
      return;
    }
    this.e = true;
    jniResampleCommand(this.d, 0, 0L);
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public void setMediaSync(TuSdkAudioResampleSync paramTuSdkAudioResampleSync)
  {
    this.f = paramTuSdkAudioResampleSync;
  }
  
  public void changeFormat(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null)
    {
      TLog.w("%s changeFormat need inputInfo.", new Object[] { "TuSdkAudioResampleHardImpl" });
      return;
    }
    this.b = paramTuSdkAudioInfo;
    jniChangeFormat(this.d, paramTuSdkAudioInfo.channelCount, paramTuSdkAudioInfo.bitWidth, paramTuSdkAudioInfo.sampleRate);
  }
  
  public void changeSpeed(float paramFloat)
  {
    if ((paramFloat <= 0.0F) || (this.g == paramFloat)) {
      return;
    }
    this.g = paramFloat;
    jniChangeSpeed(this.d, paramFloat);
  }
  
  public void changeSequence(boolean paramBoolean)
  {
    if (this.h == paramBoolean) {
      return;
    }
    this.h = paramBoolean;
    jniChangeSequence(this.d, paramBoolean);
  }
  
  public boolean needResample()
  {
    return jniResampleCommand(this.d, 64, 0L) > 0L;
  }
  
  public void reset()
  {
    this.g = 1.0F;
    this.h = false;
    jniResampleCommand(this.d, 16, 0L);
  }
  
  public void flush()
  {
    jniResampleCommand(this.d, 32, 0L);
  }
  
  public void setStartPrefixTimeUs(long paramLong)
  {
    jniResampleCommand(this.d, 80, paramLong);
  }
  
  public long getLastInputTimeUs()
  {
    return jniResampleCommand(this.d, 96, 0L);
  }
  
  public long getPrefixTimeUs()
  {
    return jniResampleCommand(this.d, 112, 0L);
  }
  
  public boolean queueInputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (!SdkValid.shared.audioResampleEffectsSupport())
    {
      TLog.e("You are not allowed to use resample effect , please see https://tutucloud.com", new Object[0]);
      return false;
    }
    return jniQueueInputBuffer(this.d, paramByteBuffer, paramBufferInfo.offset, paramBufferInfo.size, paramBufferInfo.flags, paramBufferInfo.presentationTimeUs);
  }
  
  private static native long jniInit(int paramInt1, int paramInt2, int paramInt3, TuSdkMediaListener paramTuSdkMediaListener);
  
  private static native void jniChangeFormat(long paramLong, int paramInt1, int paramInt2, int paramInt3);
  
  private static native void jniChangeSpeed(long paramLong, float paramFloat);
  
  private static native void jniChangeSequence(long paramLong, boolean paramBoolean);
  
  private static native long jniResampleCommand(long paramLong1, int paramInt, long paramLong2);
  
  private static native boolean jniQueueInputBuffer(long paramLong1, ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, long paramLong2);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioResampleHardImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */