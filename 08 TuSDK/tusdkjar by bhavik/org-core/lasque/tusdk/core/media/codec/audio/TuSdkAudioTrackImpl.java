package org.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
import android.media.AudioTimestamp;
import android.media.AudioTrack;
import android.os.Build.VERSION;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkAudioTrackImpl
  implements TuSdkAudioTrack
{
  private TuSdkAudioInfo a;
  private AudioTrack b;
  private int c = 3;
  private int d = 2;
  private int e;
  private int f = 1;
  private int g = 0;
  private byte[] h;
  
  public TuSdkAudioTrackImpl() {}
  
  public TuSdkAudioTrackImpl(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    this();
    setAudioInfo(paramTuSdkAudioInfo);
  }
  
  public void setAudioInfo(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null) {
      return;
    }
    if (this.b != null)
    {
      TLog.w("%s not allowed to repeat setAudioInfo ", new Object[] { "TuSdkAudioTrackImpl" });
      return;
    }
    this.a = paramTuSdkAudioInfo;
    this.d = (this.a.bitWidth == 8 ? 3 : 2);
    this.e = (this.a.channelCount < 2 ? 4 : 12);
    int i = AudioTrack.getMinBufferSize(this.a.sampleRate, this.e, this.d);
    int j = i * 4;
    int k = paramTuSdkAudioInfo.channelCount * 2;
    this.g = (j / k * k);
    if (this.g < 1)
    {
      TLog.w("%s setAudioInfo existence of invalid parameters: %s", new Object[] { "TuSdkAudioTrackImpl", this.a });
      return;
    }
    this.b = new AudioTrack(this.c, this.a.sampleRate, this.e, this.d, this.g, this.f);
  }
  
  protected AudioTrack getRealAudioTrack()
  {
    return this.b;
  }
  
  public int getBufferSize()
  {
    return this.g;
  }
  
  public int write(ByteBuffer paramByteBuffer)
  {
    if ((this.b == null) || (paramByteBuffer == null)) {
      return -1;
    }
    if (Build.VERSION.SDK_INT < 21)
    {
      if ((this.h == null) || (this.h.length < paramByteBuffer.limit())) {
        this.h = new byte[paramByteBuffer.limit()];
      }
      paramByteBuffer.get(this.h, 0, paramByteBuffer.limit());
      return this.b.write(this.h, 0, Math.min(paramByteBuffer.limit(), this.g));
    }
    return this.b.write(paramByteBuffer, Math.min(paramByteBuffer.limit(), this.g), 0);
  }
  
  public int getPlaybackHeadPosition()
  {
    if (this.b == null) {
      return 0;
    }
    return this.b.getPlaybackHeadPosition();
  }
  
  @TargetApi(19)
  public boolean getTimestamp(AudioTimestamp paramAudioTimestamp)
  {
    if ((this.b == null) || (paramAudioTimestamp == null)) {
      return false;
    }
    return this.b.getTimestamp(paramAudioTimestamp);
  }
  
  public void play()
  {
    if ((this.b == null) || (this.b.getState() != 1)) {
      return;
    }
    this.b.play();
  }
  
  public void pause()
  {
    if ((this.b == null) || (this.b.getState() != 1)) {
      return;
    }
    this.b.pause();
  }
  
  public int setVolume(float paramFloat)
  {
    if (this.b == null) {
      return 1;
    }
    paramFloat = Math.max(Math.min(AudioTrack.getMaxVolume(), paramFloat), AudioTrack.getMinVolume());
    if (Build.VERSION.SDK_INT < 21) {
      return this.b.setStereoVolume(paramFloat, paramFloat);
    }
    return this.b.setVolume(paramFloat);
  }
  
  public void flush()
  {
    if ((this.b == null) || (this.b.getState() != 1)) {
      return;
    }
    this.b.flush();
  }
  
  public void release()
  {
    if (this.b == null) {
      return;
    }
    try
    {
      this.b.release();
    }
    catch (Exception localException) {}
    this.b = null;
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioTrackImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */