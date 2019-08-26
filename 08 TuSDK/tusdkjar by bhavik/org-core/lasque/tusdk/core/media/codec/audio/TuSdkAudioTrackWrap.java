package org.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
import android.media.AudioTimestamp;
import android.media.AudioTrack;
import java.lang.reflect.Method;
import org.lasque.tusdk.core.utils.ReflectUtils;

public class TuSdkAudioTrackWrap
{
  private TuSdkAudioTrackImpl a;
  private TuSdkAudioInfo b;
  private long c = 0L;
  private Method d;
  private long e;
  private AudioTimestamp f;
  private boolean g = false;
  
  @TargetApi(19)
  public void setAudioTrack(TuSdkAudioTrackImpl paramTuSdkAudioTrackImpl, TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    this.a = paramTuSdkAudioTrackImpl;
    this.b = paramTuSdkAudioInfo;
    this.d = ReflectUtils.getMethod(AudioTrack.class, "getLatency", (Class[])null);
    this.f = new AudioTimestamp();
  }
  
  public long getVideoDisplayTimeUs()
  {
    if (this.g) {
      return System.nanoTime() / 1000L;
    }
    int i = this.a.getPlaybackHeadPosition();
    if (this.d != null) {
      try
      {
        this.e = (((Integer)this.d.invoke(this.a, (Object[])null)).intValue() * 1000L / 2L);
        this.e = Math.max(this.e, 0L);
      }
      catch (Exception localException)
      {
        this.d = null;
      }
    }
    long l = this.c + i * 1000000L / this.b.sampleRate - this.e;
    return l;
  }
  
  public void pause() {}
  
  public void resume() {}
  
  public void reset() {}
  
  public void release()
  {
    this.g = true;
  }
  
  public void setAudioBufferPts(long paramLong)
  {
    this.c = paramLong;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioTrackWrap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */