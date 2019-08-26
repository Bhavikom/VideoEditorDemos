package org.lasque.tusdk.core.media.codec.sync;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender.TuSdkAudioRenderCallback;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
import org.lasque.tusdk.core.utils.TLog;

@TargetApi(18)
public class TuSdkMediaFileCuterSync
  implements TuSdkMediaFileSync
{
  private long a = System.nanoTime();
  private long b;
  private boolean c = false;
  private TuSdkAudioEncodecOperation d;
  private final TuSdkMediaFileCuterTimeline e = new TuSdkMediaFileCuterTimeline();
  private TuSdkAudioResample f;
  private _AudioEncodecSync g;
  private _VideoEncodecSync h;
  private _AudioDecodecSync i;
  private _VideoDecodecSync j;
  private TuSdkAudioRender k;
  
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
    if (this.j == null) {
      this.j = new _VideoDecodecSync(null);
    }
    return this.j;
  }
  
  public TuSdkAudioDecodecSync getAudioDecodecSync()
  {
    if (this.i == null) {
      this.i = new _AudioDecodecSync(null);
    }
    return this.i;
  }
  
  public TuSdkAudioEncodecSync getAudioEncodecSync()
  {
    if (this.g == null) {
      this.g = new _AudioEncodecSync(null);
    }
    return this.g;
  }
  
  public TuSdkVideoEncodecSync getVideoEncodecSync()
  {
    if (this.h == null) {
      this.h = new _VideoEncodecSync(null);
    }
    return this.h;
  }
  
  public void release()
  {
    if (this.c) {
      return;
    }
    this.c = true;
    a();
    if (this.g != null)
    {
      this.g.release();
      this.g = null;
    }
    if (this.h != null) {
      this.h.release();
    }
  }
  
  private void a()
  {
    if (this.i != null)
    {
      this.i.release();
      this.i = null;
    }
    if (this.j != null)
    {
      this.j.release();
      this.j = null;
    }
  }
  
  public void setAudioMixerRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    this.k = paramTuSdkAudioRender;
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public long benchmarkUs()
  {
    return this.b / 1000L;
  }
  
  public void setBenchmarkEnd()
  {
    this.b = (System.nanoTime() - this.a);
  }
  
  public void addAudioEncodecOperation(TuSdkAudioEncodecOperation paramTuSdkAudioEncodecOperation)
  {
    this.d = paramTuSdkAudioEncodecOperation;
  }
  
  public void setTimeline(TuSdkMediaTimeline paramTuSdkMediaTimeline)
  {
    this.e.fresh(paramTuSdkMediaTimeline);
  }
  
  public long totalDurationUs()
  {
    return this.e.getOutputDurationUs();
  }
  
  public long processedUs()
  {
    if (this.h == null) {
      return 0L;
    }
    return this.h.getLastTimeUs();
  }
  
  public float calculateProgress()
  {
    float f1 = 0.0F;
    if (totalDurationUs() > 0L) {
      f1 = (float)processedUs() / (float)totalDurationUs();
    }
    return Math.min(Math.max(f1, 0.0F), 1.0F);
  }
  
  public long lastVideoDecodecTimestampNs()
  {
    if (this.j == null) {
      return 0L;
    }
    return this.j.outputTimeUs() * 1000L;
  }
  
  public boolean isEncodecCompleted()
  {
    return (isVideoEncodeCompleted()) && (isAudioEncodeCompleted());
  }
  
  public void syncVideoDecodeCompleted()
  {
    if (this.j == null) {
      return;
    }
    this.j.syncVideoDecodeCompleted();
  }
  
  public boolean isVideoDecodeCompleted()
  {
    if (this.j == null) {
      return true;
    }
    return this.j.isVideoDecodeCompleted();
  }
  
  public void syncAudioDecodeCompleted()
  {
    if (this.i == null) {
      return;
    }
    this.i.syncAudioDecodeCompleted();
  }
  
  public boolean isAudioDecodeCompleted()
  {
    if (this.i == null) {
      return true;
    }
    return this.i.isAudioDecodeCompleted();
  }
  
  public boolean isAudioDecodeCrashed()
  {
    if (this.i == null) {
      return false;
    }
    return this.i.isAudioDecodeCrashed();
  }
  
  public boolean isAudioEncodeCompleted()
  {
    if (this.g == null) {
      return true;
    }
    return this.g.isAudioEncodeCompleted();
  }
  
  public boolean isVideoEncodeCompleted()
  {
    if (this.h == null) {
      return true;
    }
    return this.h.isVideoEncodeCompleted();
  }
  
  public void syncVideoEncodecDrawFrame(long paramLong, boolean paramBoolean, TuSdkRecordSurface paramTuSdkRecordSurface, TuSdkEncodeSurface paramTuSdkEncodeSurface)
  {
    if (this.h == null) {
      return;
    }
    this.h.syncVideoEncodecDrawFrame(paramLong, paramBoolean, paramTuSdkRecordSurface, paramTuSdkEncodeSurface);
  }
  
  private void a(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity)
  {
    if (this.h == null) {
      return;
    }
    this.h.setTimeSlice(paramTuSdkMediaTimeSliceEntity);
  }
  
  private void a(TuSdkAudioResample paramTuSdkAudioResample)
  {
    if (paramTuSdkAudioResample == null) {
      return;
    }
    this.f = paramTuSdkAudioResample;
    if (this.i != null) {
      this.i.setAudioResample(paramTuSdkAudioResample);
    }
  }
  
  private class _VideoEncodecSync
    extends TuSdkVideoEncodecSyncBase
  {
    private TuSdkMediaTimeSliceEntity b;
    
    private _VideoEncodecSync() {}
    
    public void setTimeSlice(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity)
    {
      this.b = paramTuSdkMediaTimeSliceEntity;
    }
    
    protected boolean isLastDecodeFrame(long paramLong)
    {
      if (this.b == null) {
        return true;
      }
      if ((this.b.next != null) || (getInputIntervalUs() < 1L)) {
        return false;
      }
      boolean bool = Math.abs(this.b.outputEndUs - paramLong) < getInputIntervalUs();
      return bool;
    }
    
    protected boolean needSkip(long paramLong)
    {
      if (this.b == null) {
        return true;
      }
      if (!hadLockVideoTimestampUs(paramLong)) {
        return true;
      }
      int i = this.b.overviewOutput(paramLong);
      if (i != 0)
      {
        if (i > 0) {
          this.b = this.b.next;
        }
        return true;
      }
      return false;
    }
  }
  
  private class _VideoDecodecSync
    extends TuSdkVideoDecodecSyncBase
  {
    private TuSdkMediaTimeSliceEntity b;
    private TuSdkMediaTimeSliceEntity c;
    private boolean d = false;
    
    private _VideoDecodecSync() {}
    
    private void a(TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if ((this.b == null) || (paramTuSdkMediaExtractor == null)) {
        return;
      }
      this.d = false;
      this.b = this.b.next;
      if (this.b != null) {
        paramTuSdkMediaExtractor.seekTo(this.b.startUs, 0);
      }
    }
    
    public void syncVideoDecodecInfo(TuSdkVideoInfo paramTuSdkVideoInfo, TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if ((paramTuSdkVideoInfo == null) || (paramTuSdkMediaExtractor == null)) {
        return;
      }
      super.syncVideoDecodecInfo(paramTuSdkVideoInfo, paramTuSdkMediaExtractor);
      TuSdkMediaFileCuterSync.a(TuSdkMediaFileCuterSync.this).setInputDurationUs(paramTuSdkVideoInfo.durationUs);
      TuSdkMediaFileCuterSync.a(TuSdkMediaFileCuterSync.this).fixTimeSlices(paramTuSdkMediaExtractor, false);
      if (TuSdkMediaFileCuterSync.a(TuSdkMediaFileCuterSync.this).getOutputDurationUs() < 1L) {
        TLog.w("%s cuter the timeline is too short.", new Object[] { "TuSdkMediaFileCuterSync" });
      }
      this.c = (this.b = TuSdkMediaFileCuterSync.a(TuSdkMediaFileCuterSync.this).firstSlice());
      TuSdkMediaFileCuterSync.a(TuSdkMediaFileCuterSync.this, this.b);
      if (this.b != null) {
        paramTuSdkMediaExtractor.seekTo(this.b.startUs, 0);
      }
    }
    
    public boolean syncVideoDecodecExtractor(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec)
    {
      if (paramTuSdkMediaExtractor == null) {
        return true;
      }
      if (this.b == null)
      {
        TuSdkMediaUtils.putEosToCoder(paramTuSdkMediaExtractor, paramTuSdkMediaCodec);
        return true;
      }
      long l = paramTuSdkMediaExtractor.getSampleTime();
      boolean bool = TuSdkMediaUtils.putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, false);
      this.mFrameIntervalUs = paramTuSdkMediaExtractor.getFrameIntervalUs();
      int i = this.b.overview(l);
      if ((!this.d) && (i < 0))
      {
        this.d = true;
        paramTuSdkMediaExtractor.seekTo(this.b.startUs, 0);
      }
      if (i > 0)
      {
        a(paramTuSdkMediaExtractor);
      }
      else if ((bool) || (paramTuSdkMediaExtractor.getSampleTime() < 0L))
      {
        this.mMaxFrameTimeUs = paramTuSdkMediaExtractor.seekTo(this.mDurationUs);
        a(paramTuSdkMediaExtractor);
      }
      return false;
    }
    
    public void syncVideoDecodecOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkVideoInfo paramTuSdkVideoInfo)
    {
      TuSdkMediaFileCuterSync._VideoEncodecSync local_VideoEncodecSync = TuSdkMediaFileCuterSync.d(TuSdkMediaFileCuterSync.this);
      if ((paramBufferInfo == null) || (this.c == null) || (paramBufferInfo.size < 1) || (local_VideoEncodecSync == null)) {
        return;
      }
      long l1 = paramBufferInfo.presentationTimeUs;
      paramBufferInfo.presentationTimeUs = this.c.calOutputTimeUs(l1);
      int i = this.c.overview(l1);
      if (i != 0)
      {
        if (i > 0) {
          this.c = this.c.next;
        }
        return;
      }
      long l2 = System.currentTimeMillis();
      while ((!isInterrupted()) && (local_VideoEncodecSync.hasLocked())) {
        if (System.currentTimeMillis() - l2 > 500L) {
          local_VideoEncodecSync.clearLocker();
        }
      }
      this.mPreviousTimeUs = this.mOutputTimeUs;
      this.mOutputTimeUs = paramBufferInfo.presentationTimeUs;
      local_VideoEncodecSync.lockVideoTimestampUs(this.mOutputTimeUs);
    }
  }
  
  private class _AudioEncodecSync
    extends TuSdkAudioEncodecSyncBase
  {
    private _AudioEncodecSync() {}
    
    public void syncAudioEncodecInfo(TuSdkAudioInfo paramTuSdkAudioInfo)
    {
      super.syncAudioEncodecInfo(paramTuSdkAudioInfo);
      TuSdkMediaFileCuterSync.a(TuSdkMediaFileCuterSync.this, getAudioResample());
    }
  }
  
  private class _AudioDecodecSync
    extends TuSdkAudioDecodecSyncBase
  {
    private boolean b = false;
    private long c = -1L;
    private TuSdkMediaTimeSliceEntity d;
    private TuSdkMediaTimeSliceEntity e;
    private TuSdkAudioInfo f;
    private TuSdkAudioRender.TuSdkAudioRenderCallback g = new TuSdkAudioRender.TuSdkAudioRenderCallback()
    {
      public boolean isEncodec()
      {
        return false;
      }
      
      public TuSdkAudioInfo getAudioInfo()
      {
        return TuSdkMediaFileCuterSync._AudioDecodecSync.a(TuSdkMediaFileCuterSync._AudioDecodecSync.this);
      }
      
      public void returnRenderBuffer(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
      {
        TuSdkMediaFileCuterSync._AudioDecodecSync.a(TuSdkMediaFileCuterSync._AudioDecodecSync.this, paramAnonymousByteBuffer, paramAnonymousBufferInfo);
      }
    };
    
    private _AudioDecodecSync() {}
    
    private void a(TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if ((this.d == null) || (paramTuSdkMediaExtractor == null)) {
        return;
      }
      this.d = this.d.next;
      a(paramTuSdkMediaExtractor, this.d);
    }
    
    private void a(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity)
    {
      if ((paramTuSdkMediaTimeSliceEntity == null) || (paramTuSdkMediaExtractor == null)) {
        return;
      }
      paramTuSdkMediaTimeSliceEntity.audioEndUs = paramTuSdkMediaExtractor.seekTo(paramTuSdkMediaTimeSliceEntity.endUs, !paramTuSdkMediaTimeSliceEntity.isReverse());
      paramTuSdkMediaTimeSliceEntity.audioStartUs = paramTuSdkMediaExtractor.seekTo(paramTuSdkMediaTimeSliceEntity.startUs, paramTuSdkMediaTimeSliceEntity.isReverse());
    }
    
    private void a(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity)
    {
      if ((this.mAudioResample == null) || (paramTuSdkMediaTimeSliceEntity == null)) {
        return;
      }
      synchronized (this.mLocker)
      {
        this.mAudioResample.changeSpeed(paramTuSdkMediaTimeSliceEntity.speed);
        this.mAudioResample.changeSequence((paramTuSdkMediaTimeSliceEntity.isReverse()) && (paramTuSdkMediaTimeSliceEntity.isAudioReverse()));
      }
    }
    
    public void syncAudioDecodecInfo(TuSdkAudioInfo paramTuSdkAudioInfo, TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if ((paramTuSdkAudioInfo == null) || (paramTuSdkMediaExtractor == null)) {
        return;
      }
      super.syncAudioDecodecInfo(paramTuSdkAudioInfo, paramTuSdkMediaExtractor);
      this.f = paramTuSdkAudioInfo;
      while ((!isInterrupted()) && ((this.mAudioResample == null) || (!TuSdkMediaFileCuterSync.a(TuSdkMediaFileCuterSync.this).isFixTimeSlices()))) {}
      if (TuSdkMediaFileCuterSync.a(TuSdkMediaFileCuterSync.this).isFixTimeSlices())
      {
        this.e = (this.d = TuSdkMediaFileCuterSync.a(TuSdkMediaFileCuterSync.this).firstSlice());
        if (this.d != null)
        {
          this.c = this.d.startUs;
          a(paramTuSdkMediaExtractor, this.d);
        }
      }
      if (this.mAudioResample != null)
      {
        this.mAudioResample.changeFormat(paramTuSdkAudioInfo);
        a(this.d);
      }
    }
    
    public boolean syncAudioDecodecExtractor(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec)
    {
      if (paramTuSdkMediaExtractor == null) {
        return true;
      }
      if (this.d == null)
      {
        TuSdkMediaUtils.putEosToCoder(paramTuSdkMediaExtractor, paramTuSdkMediaCodec);
        return true;
      }
      long l = paramTuSdkMediaExtractor.getSampleTime();
      boolean bool = TuSdkMediaUtils.putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, false);
      this.mFrameIntervalUs = paramTuSdkMediaExtractor.getFrameIntervalUs();
      if (this.d.overview(l) > 0)
      {
        a(paramTuSdkMediaExtractor);
      }
      else if ((bool) || (paramTuSdkMediaExtractor.getSampleTime() < 0L))
      {
        this.mMaxFrameTimeUs = paramTuSdkMediaExtractor.seekTo(this.mDurationUs);
        a(paramTuSdkMediaExtractor);
      }
      return false;
    }
    
    public void syncAudioDecodecOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkAudioInfo paramTuSdkAudioInfo)
    {
      TuSdkAudioEncodecOperation localTuSdkAudioEncodecOperation = TuSdkMediaFileCuterSync.b(TuSdkMediaFileCuterSync.this);
      TuSdkAudioResample localTuSdkAudioResample = this.mAudioResample;
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = this.e;
      if ((localTuSdkAudioEncodecOperation == null) || (localTuSdkAudioResample == null) || (localTuSdkMediaTimeSliceEntity == null) || (paramBufferInfo == null) || (paramBufferInfo.size < 1)) {
        return;
      }
      long l = paramBufferInfo.presentationTimeUs;
      paramBufferInfo.presentationTimeUs = localTuSdkMediaTimeSliceEntity.calOutputTimeUs(l);
      int i = localTuSdkMediaTimeSliceEntity.overview(l);
      if (i != 0)
      {
        if (i > 0)
        {
          this.e = localTuSdkMediaTimeSliceEntity.next;
          a(this.e);
        }
        return;
      }
      if (!this.b)
      {
        this.b = true;
        if (paramBufferInfo.presentationTimeUs > this.c) {
          localTuSdkAudioEncodecOperation.autoFillMuteDataWithinEnd(this.c, paramBufferInfo.presentationTimeUs);
        }
      }
      MediaCodec.BufferInfo localBufferInfo = TuSdkMediaUtils.cloneBufferInfo(paramBufferInfo);
      localTuSdkAudioResample.queueInputBuffer(paramByteBuffer, localBufferInfo);
    }
    
    public void syncAudioResampleOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      if (TLog.LOG_AUDIO_DECODEC_INFO) {
        TuSdkCodecCapabilities.logBufferInfo(String.format("%s resampleOutputBuffer", new Object[] { "TuSdkMediaFileCuterSync" }), paramBufferInfo);
      }
      TuSdkAudioRender localTuSdkAudioRender = TuSdkMediaFileCuterSync.c(TuSdkMediaFileCuterSync.this);
      if ((localTuSdkAudioRender == null) || (!localTuSdkAudioRender.onAudioSliceRender(paramByteBuffer, paramBufferInfo, this.g))) {
        a(paramByteBuffer, paramBufferInfo);
      }
    }
    
    private void a(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      TuSdkAudioEncodecOperation localTuSdkAudioEncodecOperation = TuSdkMediaFileCuterSync.b(TuSdkMediaFileCuterSync.this);
      if ((localTuSdkAudioEncodecOperation == null) || (paramBufferInfo == null) || (paramBufferInfo.size < 1)) {
        return;
      }
      this.mPreviousTimeUs = this.mLastTimeUs;
      this.mLastTimeUs = paramBufferInfo.presentationTimeUs;
      paramByteBuffer.position(paramBufferInfo.offset);
      paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
      while ((!isInterrupted()) && (localTuSdkAudioEncodecOperation.writeBuffer(paramByteBuffer, paramBufferInfo) == 0)) {}
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkMediaFileCuterSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */