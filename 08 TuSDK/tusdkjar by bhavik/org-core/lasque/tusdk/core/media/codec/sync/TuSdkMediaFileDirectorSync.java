package org.lasque.tusdk.core.media.codec.sync;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender.TuSdkAudioRenderCallback;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
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
import org.lasque.tusdk.core.utils.hardware.HardwareHelper;

@TargetApi(18)
public class TuSdkMediaFileDirectorSync
  implements TuSdkMediaFileSync
{
  private static final Map<String, String> a = new HashMap();
  private static final Map<String, String> b = new HashMap();
  private long c = System.nanoTime();
  private long d;
  private boolean e = false;
  private TuSdkAudioEncodecOperation f;
  private TuSdkMediaFileCuterTimeline g = new TuSdkMediaFileCuterTimeline();
  private TuSdkAudioResample h;
  private _AudioEncodecSync i;
  private _VideoEncodecSync j;
  private _AudioDecodecSync k;
  private _VideoDecodecSync l;
  private TuSdkAudioRender m;
  
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
    if (this.l == null) {
      this.l = new _VideoDecodecSync(null);
    }
    return this.l;
  }
  
  public TuSdkAudioDecodecSync getAudioDecodecSync()
  {
    if (this.k == null) {
      this.k = new _AudioDecodecSync(null);
    }
    return this.k;
  }
  
  public TuSdkAudioEncodecSync getAudioEncodecSync()
  {
    if (this.i == null) {
      this.i = new _AudioEncodecSync(null);
    }
    return this.i;
  }
  
  public TuSdkVideoEncodecSync getVideoEncodecSync()
  {
    if (this.j == null) {
      this.j = new _VideoEncodecSync(null);
    }
    return this.j;
  }
  
  public void release()
  {
    if (this.e) {
      return;
    }
    this.e = true;
    a();
    if (this.i != null)
    {
      this.i.release();
      this.i = null;
    }
    if (this.j != null) {
      this.j.release();
    }
  }
  
  private void a()
  {
    if (this.k != null)
    {
      this.k.release();
      this.k = null;
    }
    if (this.l != null)
    {
      this.l.release();
      this.l = null;
    }
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public long benchmarkUs()
  {
    return this.d / 1000L;
  }
  
  public void setBenchmarkEnd()
  {
    this.d = (System.nanoTime() - this.c);
  }
  
  public void addAudioEncodecOperation(TuSdkAudioEncodecOperation paramTuSdkAudioEncodecOperation)
  {
    this.f = paramTuSdkAudioEncodecOperation;
  }
  
  public void setAudioMixerRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    this.m = paramTuSdkAudioRender;
  }
  
  public void setTimeline(TuSdkMediaTimeline paramTuSdkMediaTimeline)
  {
    this.g = ((TuSdkMediaFileCuterTimeline)paramTuSdkMediaTimeline);
  }
  
  public TuSdkMediaFileCuterTimeline getTimeLine()
  {
    return this.g;
  }
  
  public long totalDurationUs()
  {
    return this.g.getOutputDurationUs();
  }
  
  public long processedUs()
  {
    if (this.j == null) {
      return 0L;
    }
    return this.j.getLastTimeUs();
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
    if (this.l == null) {
      return 0L;
    }
    return this.l.outputTimeUs() * 1000L;
  }
  
  public boolean isEncodecCompleted()
  {
    return (isVideoEncodeCompleted()) && (isAudioEncodeCompleted());
  }
  
  public void syncVideoDecodeCompleted()
  {
    if (this.l == null) {
      return;
    }
    this.l.syncVideoDecodeCompleted();
  }
  
  public boolean isVideoDecodeCompleted()
  {
    if (this.l == null) {
      return true;
    }
    return this.l.isVideoDecodeCompleted();
  }
  
  public void syncAudioDecodeCompleted()
  {
    if (this.k == null) {
      return;
    }
    this.k.syncAudioDecodeCompleted();
  }
  
  public boolean isAudioDecodeCompleted()
  {
    if (this.k == null) {
      return true;
    }
    return this.k.isAudioDecodeCompleted();
  }
  
  public boolean isAudioDecodeCrashed()
  {
    if (this.k == null) {
      return false;
    }
    return this.k.isAudioDecodeCrashed();
  }
  
  public boolean isAudioEncodeCompleted()
  {
    if (this.i == null) {
      return true;
    }
    return this.i.isAudioEncodeCompleted();
  }
  
  public boolean isVideoEncodeCompleted()
  {
    if (this.j == null) {
      return true;
    }
    return this.j.isVideoEncodeCompleted();
  }
  
  public void syncVideoEncodecDrawFrame(long paramLong, boolean paramBoolean, TuSdkRecordSurface paramTuSdkRecordSurface, TuSdkEncodeSurface paramTuSdkEncodeSurface)
  {
    if (this.j == null) {
      return;
    }
    this.j.syncVideoEncodecDrawFrame(paramLong, paramBoolean, paramTuSdkRecordSurface, paramTuSdkEncodeSurface);
  }
  
  private void a(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity)
  {
    if (this.j == null) {
      return;
    }
    this.j.setTimeSlice(paramTuSdkMediaTimeSliceEntity);
  }
  
  private void a(TuSdkAudioResample paramTuSdkAudioResample)
  {
    if (paramTuSdkAudioResample == null) {
      return;
    }
    this.h = paramTuSdkAudioResample;
    if (this.k != null) {
      this.k.setAudioResample(paramTuSdkAudioResample);
    }
  }
  
  private boolean b()
  {
    boolean bool = false;
    Iterator localIterator = a.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      bool = HardwareHelper.isMatchDeviceModelAndManuFacturer((String)localEntry.getKey(), (String)localEntry.getValue());
      if (bool) {
        break;
      }
    }
    return bool;
  }
  
  private boolean c()
  {
    boolean bool = false;
    Iterator localIterator = b.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      bool = HardwareHelper.isMatchDeviceModelAndManuFacturer((String)localEntry.getKey(), (String)localEntry.getValue());
      if (bool) {
        break;
      }
    }
    return bool;
  }
  
  static
  {
    a.put("PADM00", "OPPO");
    a.put("PACT00", "OPPO");
    a.put("MI 6", "Xiaomi");
    b.put("OD103", "Smartisan");
    b.put("OS105", "Smartisan");
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
    private TuSdkMediaTimeSliceEntity c;
    private TuSdkMediaTimeSliceEntity d;
    private boolean e = false;
    private TuSdkMediaExtractor f;
    boolean a = false;
    private long g = 0L;
    
    private _VideoDecodecSync() {}
    
    private void a(TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if ((this.c == null) || (paramTuSdkMediaExtractor == null)) {
        return;
      }
      this.e = false;
      this.c = this.c.next;
      if (this.c != null) {
        paramTuSdkMediaExtractor.seekTo(this.c.startUs, isSupportPrecise() ? 2 : 0);
      }
    }
    
    private long a(long paramLong)
    {
      if ((paramLong < 0L) || (this.c == null)) {
        return -1L;
      }
      if ((this.c.speed <= 1.0F) || (this.mFrameIntervalUs == 0L)) {
        return paramLong;
      }
      long l = Math.floor((float)paramLong + (float)this.mFrameIntervalUs * this.c.speed);
      return l;
    }
    
    private long b(long paramLong)
    {
      if ((paramLong < 0L) || (this.c == null)) {
        return -1L;
      }
      if ((this.c.speed <= 1.0F) || (this.mFrameIntervalUs == 0L)) {
        return paramLong - this.mFrameIntervalUs;
      }
      long l = Math.ceil((float)paramLong - (float)this.mFrameIntervalUs * this.c.speed);
      return l;
    }
    
    public void syncVideoDecodecInfo(TuSdkVideoInfo paramTuSdkVideoInfo, TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if ((paramTuSdkVideoInfo == null) || (paramTuSdkMediaExtractor == null)) {
        return;
      }
      super.syncVideoDecodecInfo(paramTuSdkVideoInfo, paramTuSdkMediaExtractor);
      this.f = paramTuSdkMediaExtractor;
      TuSdkMediaFileDirectorSync.b(TuSdkMediaFileDirectorSync.this).setInputDurationUs(paramTuSdkVideoInfo.durationUs);
      TuSdkMediaFileDirectorSync.b(TuSdkMediaFileDirectorSync.this).fixTimeSlices(paramTuSdkMediaExtractor, isSupportPrecise(), true);
      if (TuSdkMediaFileDirectorSync.b(TuSdkMediaFileDirectorSync.this).getOutputDurationUs() < 1L) {
        TLog.w("%s cuter the timeline is too short.", new Object[] { "TuSdkMediaFileDirectorSync" });
      }
      this.d = (this.c = TuSdkMediaFileDirectorSync.b(TuSdkMediaFileDirectorSync.this).firstSlice());
      TuSdkMediaFileDirectorSync.a(TuSdkMediaFileDirectorSync.this, this.c);
      if (this.c != null) {
        paramTuSdkMediaExtractor.seekTo(this.c.startUs, isSupportPrecise() ? 2 : 0);
      }
    }
    
    public boolean syncVideoDecodecExtractor(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec)
    {
      if (paramTuSdkMediaExtractor == null) {
        return true;
      }
      if (this.c == null)
      {
        TuSdkMediaUtils.putEosToCoder(paramTuSdkMediaExtractor, paramTuSdkMediaCodec);
        return true;
      }
      long l1 = paramTuSdkMediaExtractor.getSampleTime();
      int i = this.c.overview(l1);
      boolean bool = TuSdkMediaUtils.putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, false);
      this.mFrameIntervalUs = paramTuSdkMediaExtractor.getFrameIntervalUs();
      if ((!isSupportPrecise()) && (!this.e) && (i < 0))
      {
        this.e = true;
        paramTuSdkMediaExtractor.seekTo(this.c.startUs, 0);
      }
      else if (i > 0)
      {
        a(paramTuSdkMediaExtractor);
      }
      else
      {
        long l2;
        if (this.c.isReverse())
        {
          if (this.mMinFrameTimeUs == l1)
          {
            a(paramTuSdkMediaExtractor);
            return false;
          }
          l2 = b(l1);
          if (TuSdkMediaFileDirectorSync.e(TuSdkMediaFileDirectorSync.this)) {
            l2 -= 110L;
          }
          paramTuSdkMediaExtractor.seekTo(l2, 0);
        }
        else if ((bool) || (paramTuSdkMediaExtractor.getSampleTime() < 0L))
        {
          this.mMaxFrameTimeUs = paramTuSdkMediaExtractor.seekTo(this.mDurationUs);
          a(paramTuSdkMediaExtractor);
        }
        else if ((isSupportPrecise()) && (this.c.speed > 1.0F))
        {
          l2 = a(l1);
          paramTuSdkMediaExtractor.seekTo(l2, 2);
        }
      }
      return false;
    }
    
    public void syncVideoDecodecOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkVideoInfo paramTuSdkVideoInfo)
    {
      TuSdkMediaFileDirectorSync._VideoEncodecSync local_VideoEncodecSync = TuSdkMediaFileDirectorSync.f(TuSdkMediaFileDirectorSync.this);
      if ((paramBufferInfo == null) || (this.d == null) || (paramBufferInfo.size < 1) || (local_VideoEncodecSync == null) || (this.f == null)) {
        return;
      }
      long l1 = paramBufferInfo.presentationTimeUs;
      int i = this.d.overview(l1);
      if ((this.f.getSampleTime() < l1) && (this.c != null) && (!this.c.isReverse()) && (this.d != null) && (!this.d.isReverse()))
      {
        this.d = this.c;
        return;
      }
      if ((this.a) && (this.f.getSampleTime() > l1)) {
        this.a = false;
      }
      if (((this.f.getSampleTime() <= l1) && (!this.d.isReverse()) && (TuSdkMediaFileDirectorSync.g(TuSdkMediaFileDirectorSync.this)) && (!this.a)) || (i > 0))
      {
        this.d = this.d.next;
        this.a = true;
        return;
      }
      paramBufferInfo.presentationTimeUs = this.d.calOutputTimeUs(l1);
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
      TuSdkMediaFileDirectorSync.a(TuSdkMediaFileDirectorSync.this, getAudioResample());
    }
    
    public TuSdkAudioInfo getEncodeAudioInfo()
    {
      return this.mAudioInfo;
    }
  }
  
  private class _AudioDecodecSync
    extends TuSdkAudioDecodecSyncBase
    implements TuSdkAudioPitchSync
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
        return TuSdkMediaFileDirectorSync._AudioDecodecSync.a(TuSdkMediaFileDirectorSync._AudioDecodecSync.this);
      }
      
      public void returnRenderBuffer(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
      {
        TuSdkMediaFileDirectorSync._AudioDecodecSync.a(TuSdkMediaFileDirectorSync._AudioDecodecSync.this, paramAnonymousByteBuffer, paramAnonymousBufferInfo);
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
      if (paramTuSdkMediaTimeSliceEntity.isReverse())
      {
        if (paramTuSdkMediaTimeSliceEntity.isAudioReverse())
        {
          paramTuSdkMediaTimeSliceEntity.audioEndUs = paramTuSdkMediaExtractor.seekTo(paramTuSdkMediaTimeSliceEntity.endUs, !paramTuSdkMediaTimeSliceEntity.isReverse());
          paramTuSdkMediaTimeSliceEntity.audioStartUs = paramTuSdkMediaExtractor.seekTo(paramTuSdkMediaTimeSliceEntity.startUs, paramTuSdkMediaTimeSliceEntity.isReverse());
        }
        else
        {
          paramTuSdkMediaTimeSliceEntity.audioEndUs = paramTuSdkMediaExtractor.seekTo(paramTuSdkMediaTimeSliceEntity.startUs, paramTuSdkMediaTimeSliceEntity.isReverse());
          paramTuSdkMediaTimeSliceEntity.audioStartUs = paramTuSdkMediaExtractor.seekTo(paramTuSdkMediaTimeSliceEntity.endUs, !paramTuSdkMediaTimeSliceEntity.isReverse());
        }
      }
      else
      {
        paramTuSdkMediaTimeSliceEntity.audioEndUs = paramTuSdkMediaExtractor.seekTo(paramTuSdkMediaTimeSliceEntity.endUs, !paramTuSdkMediaTimeSliceEntity.isReverse());
        if (paramTuSdkMediaTimeSliceEntity.audioEndUs < 0L) {
          paramTuSdkMediaTimeSliceEntity.audioEndUs = paramTuSdkMediaExtractor.seekTo(this.f.durationUs, !paramTuSdkMediaTimeSliceEntity.isReverse());
        }
        paramTuSdkMediaTimeSliceEntity.audioStartUs = paramTuSdkMediaExtractor.seekTo(paramTuSdkMediaTimeSliceEntity.startUs, paramTuSdkMediaTimeSliceEntity.isReverse());
      }
    }
    
    private void a(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity)
    {
      if ((this.mAudioResample == null) || (this.mAudioPitch == null) || (paramTuSdkMediaTimeSliceEntity == null)) {
        return;
      }
      synchronized (this.mLocker)
      {
        this.mAudioPitch.flush();
        this.mAudioPitch.changeSpeed(paramTuSdkMediaTimeSliceEntity.speed);
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
      if (this.mAudioPitch == null)
      {
        this.mAudioPitch = new TuSdkAudioPitchHardImpl(TuSdkMediaFileDirectorSync.a(TuSdkMediaFileDirectorSync.this).getEncodeAudioInfo());
        this.mAudioPitch.changeFormat(TuSdkMediaFileDirectorSync.a(TuSdkMediaFileDirectorSync.this).getEncodeAudioInfo());
        this.mAudioPitch.setMediaSync(this);
      }
      while ((!isInterrupted()) && ((this.mAudioResample == null) || (!TuSdkMediaFileDirectorSync.b(TuSdkMediaFileDirectorSync.this).isFixTimeSlices()))) {}
      if (TuSdkMediaFileDirectorSync.b(TuSdkMediaFileDirectorSync.this).isFixTimeSlices())
      {
        this.e = (this.d = TuSdkMediaFileDirectorSync.b(TuSdkMediaFileDirectorSync.this).firstSlice());
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
      TLog.d("timeSlice = " + this.d, new Object[0]);
      if (this.d == null)
      {
        TuSdkMediaUtils.putEosToCoder(paramTuSdkMediaExtractor, paramTuSdkMediaCodec);
        return true;
      }
      long l = paramTuSdkMediaExtractor.getSampleTime();
      boolean bool = TuSdkMediaUtils.putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, false);
      this.mFrameIntervalUs = paramTuSdkMediaExtractor.getFrameIntervalUs();
      if (this.d.overviewAudio(l) > 0)
      {
        a(paramTuSdkMediaExtractor);
      }
      else if ((this.d.isReverse()) && (this.d.isAudioReverse()))
      {
        if (this.mMinFrameTimeUs == l)
        {
          a(paramTuSdkMediaExtractor);
          return false;
        }
        paramTuSdkMediaExtractor.seekTo(l - 1L, 0);
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
      TuSdkAudioEncodecOperation localTuSdkAudioEncodecOperation = TuSdkMediaFileDirectorSync.c(TuSdkMediaFileDirectorSync.this);
      TuSdkAudioResample localTuSdkAudioResample = this.mAudioResample;
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = this.e;
      if ((localTuSdkAudioEncodecOperation == null) || (localTuSdkAudioResample == null) || (localTuSdkMediaTimeSliceEntity == null) || (paramBufferInfo == null) || (paramBufferInfo.size < 1)) {
        return;
      }
      long l = paramBufferInfo.presentationTimeUs;
      paramBufferInfo.presentationTimeUs = localTuSdkMediaTimeSliceEntity.calOutputAudioTimeUs(l);
      int i = this.e.overviewAudio(l);
      if (i < 0) {
        return;
      }
      if (i > 0)
      {
        this.e = this.e.next;
        if (this.e == null) {
          return;
        }
        a(this.e);
        if (this.e.overviewAudio(l) == 0) {
          paramBufferInfo.presentationTimeUs = this.e.calOutputTimeUs(l);
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
      localBufferInfo.presentationTimeUs = localTuSdkMediaTimeSliceEntity.calOutputOrginTimeUs(l);
      localTuSdkAudioResample.queueInputBuffer(paramByteBuffer, localBufferInfo);
    }
    
    public void syncAudioResampleOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      if (TLog.LOG_AUDIO_DECODEC_INFO) {
        TuSdkCodecCapabilities.logBufferInfo(String.format("%s resampleOutputBuffer", new Object[] { "TuSdkMediaFileDirectorSync" }), paramBufferInfo);
      }
      this.mAudioPitch.queueInputBuffer(paramByteBuffer, paramBufferInfo);
    }
    
    private void a(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      TuSdkAudioEncodecOperation localTuSdkAudioEncodecOperation = TuSdkMediaFileDirectorSync.c(TuSdkMediaFileDirectorSync.this);
      if ((localTuSdkAudioEncodecOperation == null) || (paramBufferInfo == null) || (paramBufferInfo.size < 1)) {
        return;
      }
      this.mPreviousTimeUs = this.mLastTimeUs;
      this.mLastTimeUs = paramBufferInfo.presentationTimeUs;
      paramByteBuffer.position(paramBufferInfo.offset);
      paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
      while ((!isInterrupted()) && (localTuSdkAudioEncodecOperation.writeBuffer(paramByteBuffer, paramBufferInfo) == 0)) {}
    }
    
    public void syncAudioPitchOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      TuSdkAudioRender localTuSdkAudioRender = TuSdkMediaFileDirectorSync.d(TuSdkMediaFileDirectorSync.this);
      if ((localTuSdkAudioRender == null) || (!localTuSdkAudioRender.onAudioSliceRender(paramByteBuffer, paramBufferInfo, this.g))) {
        a(paramByteBuffer, paramBufferInfo);
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkMediaFileDirectorSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */