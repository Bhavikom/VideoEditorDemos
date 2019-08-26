package org.lasque.tusdk.core.media.codec.sync;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFrameInfo;
import org.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

public class TuSdkVideoDecodecSyncBase
  implements TuSdkVideoDecodecSync
{
  protected boolean mReleased = false;
  private TuSdkVideoInfo a;
  protected final Object mLocker = new Object();
  private boolean b = false;
  private boolean c = false;
  private boolean d = false;
  private TuSdkMediaFrameInfo e;
  protected long mDurationUs = 0L;
  protected long mOutputTimeUs = 0L;
  protected long mPreviousTimeUs = 0L;
  protected long mFrameIntervalUs = 0L;
  private boolean f = false;
  private boolean g;
  protected long mMaxFrameTimeUs = -1L;
  protected long mMinFrameTimeUs = -1L;
  protected long mFlushAndSeekto = -1L;
  protected long mRelativeStartNs = -1L;
  
  public void release()
  {
    this.mReleased = true;
  }
  
  public long outputTimeUs()
  {
    return this.mOutputTimeUs;
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
  
  public void syncVideoDecodeCompleted()
  {
    this.b = true;
  }
  
  public boolean isVideoDecodeCompleted()
  {
    return this.b;
  }
  
  public boolean isVideoDecodeCrashed()
  {
    return this.c;
  }
  
  public boolean hasVideoDecodeTrack()
  {
    return this.d;
  }
  
  public void syncVideoDecodeCrashed(Exception paramException)
  {
    if (paramException == null) {
      return;
    }
    this.c = true;
    if ((paramException instanceof TuSdkNoMediaTrackException)) {
      this.d = false;
    }
  }
  
  public TuSdkMediaFrameInfo getFrameInfo()
  {
    return this.e;
  }
  
  public boolean isSupportPrecise()
  {
    if (this.e != null) {
      return this.e.supportAllKeys();
    }
    return false;
  }
  
  public boolean isPause()
  {
    return this.f;
  }
  
  public void setPause()
  {
    this.f = true;
  }
  
  public void setPlay()
  {
    this.mRelativeStartNs = -1L;
    this.f = false;
  }
  
  public void syncRestart()
  {
    this.b = false;
  }
  
  public void pauseSave()
  {
    this.g = this.f;
    setPause();
  }
  
  public void resumeSave()
  {
    resume(this.g);
  }
  
  public void resume(boolean paramBoolean)
  {
    setPlay();
    if (paramBoolean) {
      setPause();
    }
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
    return (this.b) || ((this.d) && (this.c));
  }
  
  public boolean isInterrupted()
  {
    return (ThreadHelper.isInterrupted()) || (this.mReleased);
  }
  
  protected void syncPause()
  {
    while ((!isInterrupted()) && (this.f)) {}
  }
  
  protected boolean syncPlay(long paramLong)
  {
    while ((!isInterrupted()) && (paramLong > System.nanoTime())) {
      if ((this.mRelativeStartNs < 0L) || (this.f)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean syncWithVideo()
  {
    while ((!isInterrupted()) && (this.mRelativeStartNs < 0L)) {
      if (this.f) {
        return false;
      }
    }
    return true;
  }
  
  public void syncVideoDecodecInfo(TuSdkVideoInfo paramTuSdkVideoInfo, TuSdkMediaExtractor paramTuSdkMediaExtractor)
  {
    if ((paramTuSdkVideoInfo == null) || (paramTuSdkMediaExtractor == null)) {
      return;
    }
    this.a = paramTuSdkVideoInfo;
    this.mDurationUs = paramTuSdkVideoInfo.durationUs;
    this.d = true;
    this.mMinFrameTimeUs = paramTuSdkMediaExtractor.getSampleTime();
    this.e = paramTuSdkMediaExtractor.getFrameInfo();
  }
  
  public boolean syncVideoDecodecExtractor(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec)
  {
    if ((this.mReleased) || (paramTuSdkMediaExtractor == null) || (paramTuSdkMediaCodec == null)) {
      return true;
    }
    boolean bool = TuSdkMediaUtils.putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec);
    this.mFrameIntervalUs = paramTuSdkMediaExtractor.getFrameIntervalUs();
    return bool;
  }
  
  public void syncVideoDecodecOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkVideoInfo paramTuSdkVideoInfo)
  {
    if ((paramBufferInfo == null) || (paramBufferInfo.size < 1)) {
      return;
    }
    this.mPreviousTimeUs = this.mOutputTimeUs;
    this.mOutputTimeUs = paramBufferInfo.presentationTimeUs;
    paramByteBuffer.position(paramBufferInfo.offset);
    paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
  }
  
  public void syncVideoDecodecUpdated(MediaCodec.BufferInfo paramBufferInfo)
  {
    if (TLog.LOG_VIDEO_DECODEC_INFO) {
      TuSdkCodecCapabilities.logBufferInfo(String.format("%s syncVideoDecodecUpdated", new Object[] { "TuSdkVideoDecodecSyncBase" }), paramBufferInfo);
    }
    syncPause();
  }
  
  public long calcInputTimeUs(long paramLong)
  {
    return paramLong;
  }
  
  public long calcEffectFrameTimeUs(long paramLong)
  {
    return paramLong;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkVideoDecodecSyncBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */