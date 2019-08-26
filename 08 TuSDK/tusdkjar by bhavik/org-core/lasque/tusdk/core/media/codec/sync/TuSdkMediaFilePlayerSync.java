package org.lasque.tusdk.core.media.codec.sync;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrack;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackImpl;
import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
public class TuSdkMediaFilePlayerSync
  implements TuSdkMediaDecodecSync
{
  private boolean a = false;
  private _AudioPlaySyncKey b;
  private _VideoPlaySyncKey c;
  
  public TuSdkVideoDecodecSync buildVideoDecodecSync()
  {
    return getVideoDecodecSync();
  }
  
  public TuSdkAudioDecodecSync buildAudioDecodecSync()
  {
    return getAudioDecodecSync();
  }
  
  public TuSdkVideoDecodecSync getVideoDecodecSync()
  {
    if (this.c == null) {
      this.c = new _VideoPlaySyncKey(null);
    }
    return this.c;
  }
  
  public TuSdkAudioDecodecSync getAudioDecodecSync()
  {
    if (this.b == null) {
      this.b = new _AudioPlaySyncKey(null);
    }
    return this.b;
  }
  
  public void release()
  {
    this.a = true;
    if (this.c != null)
    {
      this.c.release();
      this.c = null;
    }
    if (this.b != null)
    {
      this.b.release();
      this.b = null;
    }
  }
  
  public long lastVideoTimestampUs()
  {
    if (this.c == null) {
      return 0L;
    }
    return this.c.outputTimeUs();
  }
  
  public long totalVideoDurationUs()
  {
    if (this.c == null) {
      return 0L;
    }
    return this.c.durationUs() - this.c.frameIntervalUs();
  }
  
  public boolean isPause()
  {
    if (this.c != null) {
      return this.c.isPause();
    }
    return false;
  }
  
  public void setPause()
  {
    if (this.c != null) {
      this.c.setPause();
    }
    if (this.b != null) {
      this.b.setPause();
    }
  }
  
  public void setPlay()
  {
    if (this.c != null) {
      this.c.setPlay();
    }
    if (this.b != null) {
      this.b.setPlay();
    }
  }
  
  public void setReset()
  {
    if (this.c != null) {
      this.c.setReset();
    }
    if (this.b != null) {
      this.b.setReset();
    }
  }
  
  public void pauseSave()
  {
    if (this.c != null) {
      this.c.pauseSave();
    }
    if (this.b != null) {
      this.b.pauseSave();
    }
  }
  
  public void resumeSave()
  {
    if (this.c != null) {
      this.c.resumeSave();
    }
    if (this.b != null) {
      this.b.resumeSave();
    }
  }
  
  public boolean isSupportPrecise()
  {
    if (this.c == null) {
      return false;
    }
    return this.c.isSupportPrecise();
  }
  
  public void setSpeed(float paramFloat)
  {
    if (!isSupportPrecise())
    {
      TLog.w("%s setSpeed unsupport this media.", new Object[] { "TuSdkMediaFilePlayerSync" });
      return;
    }
    if (paramFloat <= 0.0F)
    {
      TLog.w("%s setSpeed need greater than 0.", new Object[] { "TuSdkMediaFilePlayerSync" });
      return;
    }
    if (this.c != null) {
      this.c.setSpeed(paramFloat);
    }
    if (this.b != null) {
      this.b.setSpeed(paramFloat);
    }
  }
  
  public float speed()
  {
    if (this.c != null) {
      return this.c.speed();
    }
    return 1.0F;
  }
  
  public void setReverse(boolean paramBoolean)
  {
    if (!isSupportPrecise())
    {
      TLog.w("%s setReverse unsupport this media.", new Object[] { "TuSdkMediaFilePlayerSync" });
      return;
    }
    if (this.c != null) {
      this.c.setReverse(paramBoolean);
    }
    if (this.b != null) {
      this.b.setReverse(paramBoolean);
    }
  }
  
  public boolean isReverse()
  {
    if (this.c != null) {
      return this.c.isReverse();
    }
    return false;
  }
  
  private void a()
  {
    if (this.c != null) {
      this.c.syncRestart();
    }
    if (this.b != null) {
      this.b.syncRestart();
    }
  }
  
  public boolean syncNeedRestart()
  {
    if (b())
    {
      a();
      return true;
    }
    return false;
  }
  
  private boolean b()
  {
    return ((this.c != null) && (this.c.isNeedRestart())) || ((this.b != null) && (this.b.isNeedRestart()));
  }
  
  public boolean isVideoEos(long paramLong)
  {
    if (this.c != null) {
      return this.c.isVideoEos(paramLong);
    }
    return false;
  }
  
  public boolean isAudioEos(long paramLong)
  {
    if (this.b != null) {
      return this.b.isAudioEos(paramLong);
    }
    return false;
  }
  
  public void syncFlushAndSeekto(long paramLong)
  {
    if (this.c != null) {
      this.c.syncFlushAndSeekto(paramLong);
    }
    if (this.b != null) {
      this.b.syncFlushAndSeekto(paramLong);
    }
  }
  
  public void syncVideoDecodeCompleted()
  {
    if (this.c == null) {
      return;
    }
    this.c.syncVideoDecodeCompleted();
  }
  
  public void syncAudioDecodeCompleted()
  {
    if (this.b == null) {
      return;
    }
    this.b.syncAudioDecodeCompleted();
  }
  
  public boolean isAudioDecodeCrashed()
  {
    if (this.b == null) {
      return false;
    }
    return this.b.isAudioDecodeCrashed();
  }
  
  private class _AudioPlaySyncKey
    extends TuSdkAudioDecodecSyncBase
  {
    private TuSdkAudioTrack b;
    private float c = 1.0F;
    private boolean d = false;
    
    private _AudioPlaySyncKey() {}
    
    public void setPause()
    {
      super.setPause();
      if (this.b != null)
      {
        this.b.pause();
        this.b.flush();
      }
    }
    
    public void setPlay()
    {
      super.setPlay();
      if (this.b != null) {
        this.b.play();
      }
    }
    
    public void release()
    {
      super.release();
      if (this.b != null)
      {
        this.b.release();
        this.b = null;
      }
      if (this.mAudioResample != null)
      {
        this.mAudioResample.release();
        this.mAudioResample = null;
      }
    }
    
    public void setReset()
    {
      setSpeed(1.0F);
      setReverse(false);
    }
    
    public void setSpeed(float paramFloat)
    {
      if (paramFloat <= 0.0F)
      {
        TLog.w("%s setSpeed need greater than 0.", new Object[] { "TuSdkMediaFilePlayerSync" });
        return;
      }
      if (this.c == paramFloat) {
        return;
      }
      pauseSave();
      this.c = paramFloat;
      if (this.mAudioResample != null) {
        this.mAudioResample.changeSpeed(paramFloat);
      }
      resumeSave();
    }
    
    public float speed()
    {
      return this.c;
    }
    
    public void setReverse(boolean paramBoolean)
    {
      if (this.d == paramBoolean) {
        return;
      }
      pauseSave();
      this.d = paramBoolean;
      if (this.mAudioResample != null) {
        this.mAudioResample.changeSequence(this.d);
      }
      resumeSave();
    }
    
    public boolean isReverse()
    {
      return this.d;
    }
    
    public boolean isAudioEos(long paramLong)
    {
      if ((this.d) && (this.mMinFrameTimeUs != -1L) && (this.mMinFrameTimeUs == paramLong)) {
        return true;
      }
      return (!this.d) && (this.mMaxFrameTimeUs != -1L) && (this.mMaxFrameTimeUs == paramLong);
    }
    
    public boolean syncAudioDecodecExtractor(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec)
    {
      if ((this.mReleased) || (paramTuSdkMediaExtractor == null) || (paramTuSdkMediaCodec == null)) {
        return true;
      }
      if (this.mFlushAndSeekto > -1L)
      {
        paramTuSdkMediaCodec.flush();
        paramTuSdkMediaExtractor.seekTo(this.mFlushAndSeekto);
        this.mFlushAndSeekto = -1L;
      }
      long l = paramTuSdkMediaExtractor.getSampleTime();
      boolean bool = TuSdkMediaUtils.putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, false);
      this.mFrameIntervalUs = paramTuSdkMediaExtractor.getFrameIntervalUs();
      if (this.d)
      {
        if (this.mMinFrameTimeUs == l)
        {
          paramTuSdkMediaExtractor.seekTo(this.mMinFrameTimeUs);
          return false;
        }
        paramTuSdkMediaExtractor.seekTo(l - 1L, 0);
        return false;
      }
      if ((bool) || (paramTuSdkMediaExtractor.getSampleTime() < 0L))
      {
        this.mMaxFrameTimeUs = paramTuSdkMediaExtractor.seekTo(this.mDurationUs);
        return false;
      }
      return false;
    }
    
    public void syncAudioDecodecInfo(TuSdkAudioInfo paramTuSdkAudioInfo, TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if ((paramTuSdkAudioInfo == null) || (paramTuSdkMediaExtractor == null)) {
        return;
      }
      super.syncAudioDecodecInfo(paramTuSdkAudioInfo, paramTuSdkMediaExtractor);
      this.b = new TuSdkAudioTrackImpl(paramTuSdkAudioInfo);
      this.b.play();
      this.mAudioResample = new TuSdkAudioResampleHardImpl(paramTuSdkAudioInfo);
      this.mAudioResample.setMediaSync(this);
      this.mAudioResample.changeSpeed(speed());
    }
    
    public void syncAudioDecodecOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkAudioInfo paramTuSdkAudioInfo)
    {
      if ((paramBufferInfo == null) || (paramBufferInfo.size < 1) || (this.mAudioResample == null) || (TuSdkMediaFilePlayerSync.a(TuSdkMediaFilePlayerSync.this) == null)) {
        return;
      }
      if (!TuSdkMediaFilePlayerSync.a(TuSdkMediaFilePlayerSync.this).syncWithVideo()) {
        return;
      }
      this.mAudioResample.queueInputBuffer(paramByteBuffer, paramBufferInfo);
    }
    
    public void syncAudioResampleOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      if (TLog.LOG_AUDIO_DECODEC_INFO) {
        TuSdkCodecCapabilities.logBufferInfo(String.format("%s resampleOutputBuffer", new Object[] { "TuSdkMediaFilePlayerSync" }), paramBufferInfo);
      }
      TuSdkAudioTrack localTuSdkAudioTrack = this.b;
      if ((localTuSdkAudioTrack == null) || (paramBufferInfo == null) || (paramBufferInfo.size < 1)) {
        return;
      }
      this.mPreviousTimeUs = this.mLastTimeUs;
      this.mLastTimeUs = paramBufferInfo.presentationTimeUs;
      paramByteBuffer.position(paramBufferInfo.offset);
      paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
      localTuSdkAudioTrack.write(paramByteBuffer);
    }
  }
  
  private class _VideoPlaySyncKey
    extends TuSdkVideoDecodecSyncBase
  {
    private float b = 1.0F;
    private boolean c = false;
    
    private _VideoPlaySyncKey() {}
    
    public void setReset()
    {
      setSpeed(1.0F);
      setReverse(false);
    }
    
    public void setSpeed(float paramFloat)
    {
      if (paramFloat <= 0.0F)
      {
        TLog.w("%s setSpeed need greater than 0.", new Object[] { "TuSdkMediaFilePlayerSync" });
        return;
      }
      if (this.b == paramFloat) {
        return;
      }
      pauseSave();
      this.b = paramFloat;
      resumeSave();
    }
    
    public float speed()
    {
      return this.b;
    }
    
    public void setReverse(boolean paramBoolean)
    {
      if (this.c == paramBoolean) {
        return;
      }
      pauseSave();
      this.c = paramBoolean;
      resumeSave();
    }
    
    public boolean isReverse()
    {
      return this.c;
    }
    
    private long a(long paramLong)
    {
      if (paramLong < 0L) {
        return -1L;
      }
      if ((this.b <= 1.0F) || (this.mFrameIntervalUs == 0L)) {
        return paramLong;
      }
      long l = Math.floor((float)paramLong + (float)this.mFrameIntervalUs * this.b);
      return l;
    }
    
    private long b(long paramLong)
    {
      if (paramLong < 0L) {
        return -1L;
      }
      if ((this.b <= 1.0F) || (this.mFrameIntervalUs == 0L)) {
        return paramLong - this.mFrameIntervalUs;
      }
      long l = Math.ceil((float)paramLong - (float)this.mFrameIntervalUs * this.b);
      return l;
    }
    
    public boolean isVideoEos(long paramLong)
    {
      if ((this.c) && (this.mMinFrameTimeUs != -1L) && (this.mMinFrameTimeUs == paramLong)) {
        return true;
      }
      return (!this.c) && (this.mMaxFrameTimeUs != -1L) && (this.mMaxFrameTimeUs == paramLong);
    }
    
    public boolean syncVideoDecodecExtractor(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec)
    {
      if ((this.mReleased) || (paramTuSdkMediaExtractor == null) || (paramTuSdkMediaCodec == null)) {
        return true;
      }
      if (this.mFlushAndSeekto > -1L)
      {
        paramTuSdkMediaCodec.flush();
        paramTuSdkMediaExtractor.seekTo(this.mFlushAndSeekto);
        this.mFlushAndSeekto = -1L;
      }
      long l1 = paramTuSdkMediaExtractor.getSampleTime();
      boolean bool = TuSdkMediaUtils.putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, false);
      this.mFrameIntervalUs = paramTuSdkMediaExtractor.getFrameIntervalUs();
      long l2;
      if (this.c)
      {
        if (this.mMinFrameTimeUs == l1)
        {
          paramTuSdkMediaExtractor.seekTo(this.mMinFrameTimeUs);
          return false;
        }
        l2 = b(l1);
        paramTuSdkMediaExtractor.seekTo(l2, 0);
        return false;
      }
      if ((bool) || (paramTuSdkMediaExtractor.getSampleTime() < 0L))
      {
        this.mMaxFrameTimeUs = Math.max(paramTuSdkMediaExtractor.seekTo(this.mDurationUs), l1);
        return false;
      }
      if (this.b > 1.0F)
      {
        l2 = a(l1);
        paramTuSdkMediaExtractor.seekTo(l2, 2);
      }
      return false;
    }
    
    private long a(MediaCodec.BufferInfo paramBufferInfo)
    {
      long l;
      synchronized (this.mLocker)
      {
        if (this.mRelativeStartNs < 0L)
        {
          this.mOutputTimeUs = paramBufferInfo.presentationTimeUs;
          this.mRelativeStartNs = System.nanoTime();
        }
        this.mPreviousTimeUs = this.mOutputTimeUs;
        this.mOutputTimeUs = paramBufferInfo.presentationTimeUs;
        l = this.mRelativeStartNs += Math.abs((float)((this.mOutputTimeUs - this.mPreviousTimeUs) * 1000L) / speed());
      }
      return l;
    }
    
    public void syncVideoDecodecOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkVideoInfo paramTuSdkVideoInfo)
    {
      if ((paramBufferInfo == null) || (paramBufferInfo.size < 1)) {
        return;
      }
      long l = a(paramBufferInfo);
      syncPlay(l);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkMediaFilePlayerSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */