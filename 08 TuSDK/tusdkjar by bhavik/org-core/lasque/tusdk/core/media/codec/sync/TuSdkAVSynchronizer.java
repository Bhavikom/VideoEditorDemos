package org.lasque.tusdk.core.media.codec.sync;

public abstract class TuSdkAVSynchronizer
{
  protected long mAudioBufferTimeUs = -1L;
  protected long mVideoBufferTimeUs = -1L;
  
  public long getAudioBufferTimeUs()
  {
    return this.mAudioBufferTimeUs;
  }
  
  public void setAudioBufferTimeUs(long paramLong)
  {
    this.mAudioBufferTimeUs = paramLong;
  }
  
  public long getVideoBufferTimeUs()
  {
    return this.mVideoBufferTimeUs;
  }
  
  public void setVideoBufferTimeUs(long paramLong)
  {
    this.mVideoBufferTimeUs = paramLong;
  }
  
  public abstract long getVideoDisplayTimeUs();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkAVSynchronizer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */