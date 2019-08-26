package org.lasque.tusdk.core.media.codec.sync;

import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackWrap;

public class TuSdkAVSynchronizerImpl
  extends TuSdkAVSynchronizer
{
  private boolean a = true;
  private long b = 0L;
  private long c;
  private long d = -1L;
  private TuSdkAudioTrackWrap e;
  
  public void setHaveAudio(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
  
  public void setAudioTrackWarp(TuSdkAudioTrackWrap paramTuSdkAudioTrackWrap)
  {
    this.e = paramTuSdkAudioTrackWrap;
  }
  
  public long getVideoDisplayTimeUs()
  {
    return this.a ? b() : a();
  }
  
  public void setRelativeStartNs(long paramLong)
  {
    this.c = paramLong;
  }
  
  private long a()
  {
    long l1 = this.mVideoBufferTimeUs - this.b;
    if (l1 < 0L) {
      l1 = 0L;
    }
    this.c += l1 * 1000L;
    long l2 = this.c;
    return l2;
  }
  
  private long b()
  {
    if (this.d < 0L) {
      this.d = (getVideoBufferTimeUs() - getAudioBufferTimeUs());
    }
    long l1 = getVideoBufferTimeUs() - this.e.getVideoDisplayTimeUs();
    if (l1 > 66666L) {
      l1 = 66666L;
    }
    long l2 = System.nanoTime() + l1 * 1000L + this.d;
    return l2;
  }
  
  public void setVideoBufferTimeUs(long paramLong)
  {
    if (this.mVideoBufferTimeUs != -1L) {
      this.b = this.mVideoBufferTimeUs;
    }
    super.setVideoBufferTimeUs(paramLong);
  }
  
  public void setAudioBufferTimeUs(long paramLong)
  {
    super.setAudioBufferTimeUs(paramLong);
  }
  
  public void reset()
  {
    if (!this.a) {
      this.b = 0L;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkAVSynchronizerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */