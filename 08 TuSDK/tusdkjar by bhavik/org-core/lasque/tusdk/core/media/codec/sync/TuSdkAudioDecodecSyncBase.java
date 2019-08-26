package org.lasque.tusdk.core.media.codec.sync;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.ReentrantLock;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import org.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

public abstract class TuSdkAudioDecodecSyncBase
  implements TuSdkAudioDecodecSync, TuSdkAudioResampleSync
{
  protected boolean mReleased = false;
  private TuSdkAudioInfo a;
  protected final Object mLocker = new Object();
  private ReentrantLock b = new ReentrantLock(true);
  private Object c = new Object();
  protected TuSdkAudioResample mAudioResample;
  protected TuSdkAudioPitch mAudioPitch;
  private boolean d = false;
  private boolean e = false;
  private boolean f = false;
  protected long mDurationUs = 0L;
  protected long mFrameIntervalUs = 0L;
  protected long mLastTimeUs;
  protected long mPreviousTimeUs = 0L;
  protected long mMaxFrameTimeUs = -1L;
  protected long mMinFrameTimeUs = -1L;
  protected long mFlushAndSeekto = -1L;
  private volatile boolean g = false;
  private volatile boolean h = true;
  
  public void release()
  {
    this.mReleased = true;
  }
  
  public long lastTimestampUs()
  {
    return this.mLastTimeUs;
  }
  
  public long durationUs()
  {
    return this.mDurationUs;
  }
  
  public long frameIntervalUs()
  {
    return this.mFrameIntervalUs;
  }
  
  public void syncFlushAndSeekto(long paramLong)
  {
    this.mFlushAndSeekto = paramLong;
  }
  
  public void setAudioResample(TuSdkAudioResample paramTuSdkAudioResample)
  {
    if (paramTuSdkAudioResample == null) {
      return;
    }
    paramTuSdkAudioResample.setMediaSync(this);
    this.mAudioResample = paramTuSdkAudioResample;
  }
  
  public void syncAudioDecodeCompleted()
  {
    this.d = true;
  }
  
  public boolean isAudioDecodeCompleted()
  {
    return this.d;
  }
  
  public boolean isAudioDecodeCrashed()
  {
    return this.e;
  }
  
  public boolean hasAudioDecodeTrack()
  {
    return this.f;
  }
  
  public void syncAudioDecodeCrashed(Exception paramException)
  {
    if (paramException == null) {
      return;
    }
    this.e = true;
    if ((paramException instanceof TuSdkNoMediaTrackException)) {
      this.f = false;
    }
  }
  
  public boolean isPause()
  {
    return this.g;
  }
  
  public boolean isPauseSave()
  {
    return this.h;
  }
  
  public void setPuaseLocker(Object paramObject)
  {
    this.c = paramObject;
  }
  
  public void setPause()
  {
    synchronized (this.c)
    {
      this.g = true;
      if (this.mAudioResample != null) {
        this.mAudioResample.reset();
      }
      if (this.mAudioPitch != null) {
        this.mAudioPitch.reset();
      }
    }
  }
  
  public void setPlay()
  {
    synchronized (this.c)
    {
      this.g = false;
    }
  }
  
  public void syncRestart()
  {
    this.d = false;
  }
  
  public void pauseSave()
  {
    this.b.tryLock();
    this.h = this.g;
    setPause();
    if (this.b.getHoldCount() > 0) {
      this.b.unlock();
    }
  }
  
  public void resumeSave()
  {
    this.b.tryLock();
    resume(this.h);
    if (this.b.getHoldCount() > 0) {
      this.b.unlock();
    }
  }
  
  public void resume(boolean paramBoolean)
  {
    if (paramBoolean) {
      setPause();
    } else {
      setPlay();
    }
  }
  
  public void resetIsPauseSave()
  {
    this.h = false;
  }
  
  protected void flush(TuSdkMediaCodec paramTuSdkMediaCodec)
  {
    if (paramTuSdkMediaCodec == null) {
      return;
    }
    paramTuSdkMediaCodec.flush();
    save();
  }
  
  protected void save()
  {
    pauseSave();
    resumeSave();
  }
  
  public boolean isNeedRestart()
  {
    return (this.d) || ((this.f) && (this.e));
  }
  
  protected boolean isInterrupted()
  {
    return (ThreadHelper.isInterrupted()) || (this.mReleased);
  }
  
  protected void syncPause()
  {
    while ((!isInterrupted()) && (this.g)) {}
  }
  
  public void syncAudioDecodecInfo(TuSdkAudioInfo paramTuSdkAudioInfo, TuSdkMediaExtractor paramTuSdkMediaExtractor)
  {
    if ((paramTuSdkAudioInfo == null) || (paramTuSdkMediaExtractor == null)) {
      return;
    }
    this.f = true;
    this.a = paramTuSdkAudioInfo;
    this.mDurationUs = paramTuSdkAudioInfo.durationUs;
    this.mMinFrameTimeUs = paramTuSdkMediaExtractor.getSampleTime();
  }
  
  public boolean syncAudioDecodecExtractor(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec)
  {
    if ((this.mReleased) || (paramTuSdkMediaExtractor == null) || (paramTuSdkMediaCodec == null)) {
      return true;
    }
    boolean bool = TuSdkMediaUtils.putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec);
    this.mFrameIntervalUs = paramTuSdkMediaExtractor.getFrameIntervalUs();
    return bool;
  }
  
  public void syncAudioDecodecOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if ((paramBufferInfo == null) || (paramBufferInfo.size < 1) || (this.mAudioResample == null)) {
      return;
    }
    this.mAudioResample.queueInputBuffer(paramByteBuffer, paramBufferInfo);
  }
  
  public void syncAudioDecodecUpdated(MediaCodec.BufferInfo paramBufferInfo)
  {
    if (TLog.LOG_AUDIO_DECODEC_INFO) {
      TuSdkCodecCapabilities.logBufferInfo(String.format("%s syncAudioDecodecUpdated", new Object[] { "TuSdkAudioDecodecSyncBase" }), paramBufferInfo);
    }
    syncPause();
  }
  
  public void syncAudioResampleOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (TLog.LOG_AUDIO_DECODEC_INFO) {
      TuSdkCodecCapabilities.logBufferInfo(String.format("%s resampleOutputBuffer", new Object[] { "TuSdkAudioDecodecSyncBase" }), paramBufferInfo);
    }
    if ((paramBufferInfo == null) || (paramBufferInfo.size < 1)) {
      return;
    }
    this.mPreviousTimeUs = this.mLastTimeUs;
    this.mLastTimeUs = paramBufferInfo.presentationTimeUs;
    paramByteBuffer.position(paramBufferInfo.offset);
    paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkAudioDecodecSyncBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */