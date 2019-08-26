package org.lasque.tusdk.core.media.codec.sync;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender.TuSdkAudioRenderCallback;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrack;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackImpl;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackWrap;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlicePatch;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.TuSdkSemaphore;
import org.lasque.tusdk.core.utils.hardware.HardwareHelper;

@TargetApi(16)
public class TuSdkMediaFileDirectorPlayerSync
  implements TuSdkMediaDecodecSync
{
  private static final Map<String, String> a = new HashMap();
  private static final Map<String, String> b = new HashMap();
  private TuSdkEffectFrameCalc c;
  private TuSdkDirectorPlayerStateCallback d;
  private final TuSdkMediaFileCuterTimeline e = new TuSdkMediaFileCuterTimeline();
  private Object f = new Object();
  private boolean g = false;
  private _AudioDecodecSync h;
  private _VideoDecodecSync i;
  private boolean j = true;
  private boolean k = false;
  private boolean l;
  private TuSdkAudioRender m;
  private TuSdkAVSynchronizerImpl n = new TuSdkAVSynchronizerImpl();
  private long o = -1L;
  
  public void setEffectFrameCalc(TuSdkEffectFrameCalc paramTuSdkEffectFrameCalc)
  {
    this.c = paramTuSdkEffectFrameCalc;
  }
  
  public void setDirectorPlayerStateCallback(TuSdkDirectorPlayerStateCallback paramTuSdkDirectorPlayerStateCallback)
  {
    this.d = paramTuSdkDirectorPlayerStateCallback;
  }
  
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
    if (this.i == null) {
      this.i = new _VideoDecodecSync(null);
    }
    return this.i;
  }
  
  public TuSdkAudioDecodecSync getAudioDecodecSync()
  {
    if (this.h == null)
    {
      this.h = new _AudioDecodecSync();
      this.h.setPuaseLocker(this.f);
    }
    return this.h;
  }
  
  public void setHaveAudio(boolean paramBoolean)
  {
    this.n.setHaveAudio(paramBoolean);
  }
  
  public void setMixerRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    this.m = paramTuSdkAudioRender;
  }
  
  public void setProgressOutputMode(int paramInt)
  {
    this.e.setProgressOutputMode(paramInt);
  }
  
  public void enableLoadFirstFramePause(boolean paramBoolean)
  {
    this.l = paramBoolean;
  }
  
  public void release()
  {
    if (this.g) {
      return;
    }
    this.g = true;
    if (this.i != null) {
      this.i.release();
    }
    if (this.h != null)
    {
      this.h.release();
      this.h = null;
    }
    if (this.n != null) {
      this.n.reset();
    }
  }
  
  public long outputTimeUs()
  {
    if (this.i == null) {
      return 0L;
    }
    return this.i.outputTimeUs();
  }
  
  public long decodeFrameTimeUs()
  {
    if (this.i == null) {
      return 0L;
    }
    if (this.i.lastVideoFrameTimestampUs() > totalVideInputDurationUs()) {
      return totalVideInputDurationUs();
    }
    return this.i.lastVideoFrameTimestampUs();
  }
  
  public long totalVideoDurationUs()
  {
    if ((this.e == null) || (this.i == null)) {
      return -1L;
    }
    return this.e.getOutputDurationUs() - this.i.frameIntervalUs();
  }
  
  public long totalVideInputDurationUs()
  {
    if ((this.e == null) || (this.i == null)) {
      return -1L;
    }
    long l1 = this.e.getRemoveOverSliceDurationUs();
    if ((l1 > totalVideoDurationUs()) && (totalVideoDurationUs() > 0L)) {
      l1 = totalVideoDurationUs();
    }
    return l1;
  }
  
  public boolean isPause()
  {
    if (this.i != null) {
      return this.i.isPause();
    }
    return false;
  }
  
  public void setPause()
  {
    synchronized (this.f)
    {
      if (this.i != null) {
        this.i.setPause();
      }
      if (this.h != null) {
        this.h.setPause();
      }
    }
  }
  
  public void setPlay()
  {
    synchronized (this.f)
    {
      if (this.i != null) {
        this.i.setPlay();
      }
      if (this.h != null) {
        this.h.setPlay();
      }
      if (this.h != null) {
        this.h.resetIsPauseSave();
      }
    }
  }
  
  public void setReset()
  {
    setTimeline(new TuSdkMediaTimeline(-1.0F, -1.0F));
  }
  
  public void pauseSave()
  {
    synchronized (this.f)
    {
      if (this.i != null) {
        this.i.pauseSave();
      }
      if (this.h != null) {
        this.h.pauseSave();
      }
    }
  }
  
  public void resumeSave()
  {
    synchronized (this.f)
    {
      if (this.i != null) {
        this.i.resumeSave();
      }
      if (this.h != null) {
        this.h.resumeSave();
      }
    }
  }
  
  public int setVolume(float paramFloat)
  {
    if (this.h != null) {
      return this.h.setVolume(paramFloat);
    }
    return -1;
  }
  
  private void a()
  {
    if (this.i != null) {
      this.i.syncRestart();
    }
    if (this.h != null) {
      this.h.syncRestart();
    }
  }
  
  public boolean syncNeedRestart()
  {
    if (b())
    {
      a();
      this.e.reset();
      return true;
    }
    return false;
  }
  
  private boolean b()
  {
    return ((this.i != null) && (this.i.isNeedRestart())) || ((this.h != null) && (this.h.isNeedRestart()));
  }
  
  public boolean isVideoEos()
  {
    if (this.i != null) {
      return this.i.isVideoEos();
    }
    return true;
  }
  
  public void syncVideoDecodeCompleted()
  {
    if (this.i == null) {
      return;
    }
    this.i.syncVideoDecodeCompleted();
    if ((this.h != null) && (_AudioDecodecSync.a(this.h) != null)) {
      _AudioDecodecSync.a(this.h).reset();
    }
  }
  
  public void syncAudioDecodeCompleted()
  {
    if (this.h == null) {
      return;
    }
    this.h.syncAudioDecodeCompleted();
  }
  
  public boolean isAudioDecodeCrashed()
  {
    if (this.h == null) {
      return false;
    }
    return this.h.isAudioDecodeCrashed();
  }
  
  public void setTimeline(TuSdkMediaTimeline paramTuSdkMediaTimeline)
  {
    pauseSave();
    this.e.fresh(paramTuSdkMediaTimeline);
    this.k = true;
    resumeSave();
  }
  
  public long calInputTimeUs(long paramLong)
  {
    if (!this.e.isFixTimeSlices()) {
      return -1L;
    }
    TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = this.e.sliceWithOutputTimeUs(paramLong);
    if (localTuSdkMediaTimeSliceEntity == null) {
      return -1L;
    }
    if ((localTuSdkMediaTimeSliceEntity.previous != null) && (localTuSdkMediaTimeSliceEntity.previous.overlapIndex > -1)) {
      return localTuSdkMediaTimeSliceEntity.calInputTimeUs(paramLong, this.e);
    }
    return localTuSdkMediaTimeSliceEntity.calInputTimeUs(paramLong);
  }
  
  public long calOutputTimeUs(long paramLong)
  {
    if (!this.e.isFixTimeSlices()) {
      return -1L;
    }
    TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = this.e.sliceWithInputTimeUs(paramLong);
    if (localTuSdkMediaTimeSliceEntity == null) {
      return -1L;
    }
    return localTuSdkMediaTimeSliceEntity.calOutputTimeUs(paramLong);
  }
  
  public void syncSeektoTimeUs(long paramLong)
  {
    this.o = paramLong;
    this.j = true;
  }
  
  public long getSeekToTimeUs()
  {
    return this.o;
  }
  
  public void syncFlushAndSeekto(long paramLong)
  {
    a();
    if (this.i != null) {
      this.i.syncFlushAndSeekto(paramLong);
    }
    if (this.h != null) {
      this.h.syncFlushAndSeekto(paramLong);
    }
    this.o = -1L;
    this.e.reset();
  }
  
  public TuSdkMediaFileCuterTimeline getTimeline()
  {
    return this.e;
  }
  
  private void a(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity, long paramLong)
  {
    if ((this.h == null) || (paramTuSdkMediaTimeSliceEntity == null)) {
      return;
    }
    this.h.setTimeSlice(paramTuSdkMediaTimeSliceEntity, paramLong);
    while ((!ThreadHelper.isInterrupted()) && (!this.g) && (this.h.isTimelineFresh()) && (!this.k)) {}
    if (this.k) {
      this.k = false;
    }
  }
  
  private void a(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity)
  {
    if ((this.h == null) || (paramTuSdkMediaTimeSliceEntity == null)) {
      return;
    }
    this.h.waitVideo(paramTuSdkMediaTimeSliceEntity);
  }
  
  private boolean b(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity)
  {
    if ((this.i == null) || (paramTuSdkMediaTimeSliceEntity == null)) {
      return false;
    }
    return this.i.waitAudio(paramTuSdkMediaTimeSliceEntity);
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
    a.put("Le X620", "LeMobile");
    a.put("MX5", "Meizu");
    b.put("OD103", "Smartisan");
    b.put("OS105", "Smartisan");
  }
  
  private class _AudioDecodecSync
    extends TuSdkAudioDecodecSyncBase
    implements TuSdkAudioPitchSync
  {
    private TuSdkAudioTrack b;
    private TuSdkAudioTrackWrap c;
    private boolean d = false;
    private boolean e = false;
    private long f = 0L;
    private TuSdkMediaTimeSliceEntity g;
    private TuSdkMediaTimeSliceEntity h;
    protected TuSdkSemaphore mAudioSemaphore = new TuSdkSemaphore(1);
    private ReentrantLock i = new ReentrantLock();
    private TuSdkAudioInfo j;
    private long k;
    private float l = 1.0F;
    private boolean m = false;
    private TuSdkMediaExtractor n;
    private TuSdkAudioRender.TuSdkAudioRenderCallback o = new TuSdkAudioRender.TuSdkAudioRenderCallback()
    {
      public boolean isEncodec()
      {
        return false;
      }
      
      public TuSdkAudioInfo getAudioInfo()
      {
        return TuSdkMediaFileDirectorPlayerSync._AudioDecodecSync.b(TuSdkMediaFileDirectorPlayerSync._AudioDecodecSync.this);
      }
      
      public void returnRenderBuffer(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
      {
        TuSdkMediaFileDirectorPlayerSync._AudioDecodecSync.a(TuSdkMediaFileDirectorPlayerSync._AudioDecodecSync.this, paramAnonymousByteBuffer, paramAnonymousBufferInfo);
      }
    };
    
    public _AudioDecodecSync() {}
    
    public void setPause()
    {
      super.setPause();
      if (this.b != null)
      {
        this.c.pause();
        this.b.pause();
        this.b.flush();
        this.c.setAudioBufferPts(this.k);
      }
    }
    
    public int setVolume(float paramFloat)
    {
      if (this.b != null)
      {
        this.l = paramFloat;
        return this.b.setVolume(paramFloat);
      }
      return -1;
    }
    
    public void setPlay()
    {
      super.setPlay();
      if (this.b != null)
      {
        this.c.resume();
        this.b.play();
      }
    }
    
    public void release()
    {
      super.release();
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
      if (this.mAudioResample != null)
      {
        this.mAudioResample.release();
        this.mAudioResample = null;
      }
      if (this.mAudioPitch != null)
      {
        this.mAudioPitch.release();
        this.mAudioPitch = null;
      }
    }
    
    public void syncFlushAndSeekto(long paramLong)
    {
      synchronized (this.mLocker)
      {
        this.e = false;
      }
    }
    
    public void setTimeSlice(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity, long paramLong)
    {
      try
      {
        this.mAudioSemaphore.acquire();
        synchronized (this.mLocker)
        {
          this.g = paramTuSdkMediaTimeSliceEntity;
          this.h = null;
          this.mFlushAndSeekto = paramLong;
          this.d = true;
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.printStackTrace();
      }
    }
    
    public boolean isTimelineFresh()
    {
      if (this.mReleased) {
        return false;
      }
      return this.d;
    }
    
    public void syncAudioDecodecInfo(TuSdkAudioInfo paramTuSdkAudioInfo, TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if ((paramTuSdkAudioInfo == null) || (paramTuSdkMediaExtractor == null)) {
        return;
      }
      super.syncAudioDecodecInfo(paramTuSdkAudioInfo, paramTuSdkMediaExtractor);
      this.j = paramTuSdkAudioInfo;
      this.b = new TuSdkAudioTrackImpl(paramTuSdkAudioInfo);
      this.c = new TuSdkAudioTrackWrap();
      this.c.setAudioTrack((TuSdkAudioTrackImpl)this.b, paramTuSdkAudioInfo);
      TuSdkMediaFileDirectorPlayerSync.b(TuSdkMediaFileDirectorPlayerSync.this).setAudioTrackWarp(this.c);
      this.b.play();
      this.mAudioResample = new TuSdkAudioResampleHardImpl(paramTuSdkAudioInfo);
      this.mAudioResample.setMediaSync(this);
      this.mAudioPitch = new TuSdkAudioPitchHardImpl(paramTuSdkAudioInfo);
      this.mAudioPitch.changeFormat(paramTuSdkAudioInfo);
      this.mAudioPitch.setMediaSync(this);
    }
    
    private void a(TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if ((this.g == null) || (paramTuSdkMediaExtractor == null)) {
        return;
      }
      if (this.mAudioSemaphore.availablePermits() != 1)
      {
        synchronized (this.mLocker)
        {
          a(paramTuSdkMediaExtractor, this.g);
        }
        return;
      }
      synchronized (this.mLocker)
      {
        this.g = this.g.next;
        a(paramTuSdkMediaExtractor, this.g);
      }
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
          this.f = (paramTuSdkMediaTimeSliceEntity.audioEndUs = paramTuSdkMediaExtractor.seekTo(paramTuSdkMediaTimeSliceEntity.endUs, !paramTuSdkMediaTimeSliceEntity.isReverse()));
          paramTuSdkMediaTimeSliceEntity.audioStartUs = a(paramTuSdkMediaExtractor, true, paramTuSdkMediaTimeSliceEntity.startUs);
        }
        else
        {
          this.f = (paramTuSdkMediaTimeSliceEntity.audioEndUs = a(paramTuSdkMediaExtractor, true, paramTuSdkMediaTimeSliceEntity.startUs));
          paramTuSdkMediaTimeSliceEntity.audioStartUs = paramTuSdkMediaExtractor.seekTo(paramTuSdkMediaTimeSliceEntity.endUs, paramTuSdkMediaTimeSliceEntity.isReverse());
        }
      }
      else
      {
        long l1 = paramTuSdkMediaTimeSliceEntity.endUs;
        if (l1 > 0L)
        {
          long l2 = a(paramTuSdkMediaExtractor, true, l1);
          this.f = (paramTuSdkMediaTimeSliceEntity.audioEndUs = l2);
        }
        paramTuSdkMediaTimeSliceEntity.audioStartUs = paramTuSdkMediaExtractor.seekTo(paramTuSdkMediaTimeSliceEntity.startUs, paramTuSdkMediaTimeSliceEntity.isReverse());
      }
      if (this.c != null) {
        this.c.reset();
      }
    }
    
    private long a(TuSdkMediaExtractor paramTuSdkMediaExtractor, boolean paramBoolean, long paramLong)
    {
      long l1 = -1L;
      for (int i1 = 0; l1 == -1L; i1++)
      {
        l1 = paramTuSdkMediaExtractor.seekTo(paramLong, paramBoolean);
        paramLong -= 100L;
      }
      return l1;
    }
    
    private void a(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity)
    {
      if ((this.mAudioResample == null) || (paramTuSdkMediaTimeSliceEntity == null)) {
        return;
      }
      synchronized (this.mLocker)
      {
        this.mAudioPitch.changeSpeed(paramTuSdkMediaTimeSliceEntity.speed);
        this.mAudioResample.changeSequence((paramTuSdkMediaTimeSliceEntity.isReverse()) && (paramTuSdkMediaTimeSliceEntity.isAudioReverse()));
        TuSdkMediaFileDirectorPlayerSync.b(TuSdkMediaFileDirectorPlayerSync.this).reset();
      }
    }
    
    public boolean syncAudioDecodecExtractor(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec)
    {
      if ((this.mReleased) || (paramTuSdkMediaExtractor == null) || (paramTuSdkMediaCodec == null)) {
        return true;
      }
      this.n = paramTuSdkMediaExtractor;
      lockAudio();
      if (this.d)
      {
        flush(paramTuSdkMediaCodec);
        if (this.g != null)
        {
          a(paramTuSdkMediaExtractor, this.g);
          a(this.g);
          l1 = this.mFlushAndSeekto;
          if ((this.g.isReverse()) && (!this.g.isAudioReverse())) {
            l1 = this.g.calOutputTimeUs(this.mFlushAndSeekto);
          }
          long l2 = paramTuSdkMediaExtractor.seekTo(l1, this.g.isReverse());
          if (this.g.isAudioReverse())
          {
            this.k = this.g.calOutputTimeUs(this.mFlushAndSeekto);
            this.c.setAudioBufferPts(this.g.calOutputTimeUs(this.mFlushAndSeekto));
          }
          else
          {
            this.k = this.g.calOutputAudioTimeUs(l2);
            this.c.setAudioBufferPts(this.g.calOutputAudioTimeUs(l2));
          }
        }
        this.h = this.g;
        this.mFlushAndSeekto = -1L;
        this.e = true;
        this.d = false;
        this.mAudioSemaphore.release();
      }
      if ((TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs() > -1L) || (!this.e))
      {
        paramTuSdkMediaExtractor.seekTo(TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs());
        TuSdkMediaUtils.putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, false);
        unLockAudio();
        return false;
      }
      long l1 = paramTuSdkMediaExtractor.getSampleTime();
      boolean bool = TuSdkMediaUtils.putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, false);
      this.mFrameIntervalUs = paramTuSdkMediaExtractor.getFrameIntervalUs();
      if (this.g == null)
      {
        paramTuSdkMediaExtractor.seekTo(this.f);
      }
      else if (this.g.overviewAudio(l1) > 0)
      {
        a(paramTuSdkMediaExtractor);
      }
      else if ((this.g.isReverse()) && (this.g.isAudioReverse()))
      {
        if (this.mMinFrameTimeUs == l1)
        {
          a(paramTuSdkMediaExtractor);
          unLockAudio();
          return false;
        }
        paramTuSdkMediaExtractor.seekTo(l1 - 1L, 0);
      }
      else if ((bool) || (paramTuSdkMediaExtractor.getSampleTime() < 0L))
      {
        this.mMaxFrameTimeUs = paramTuSdkMediaExtractor.seekTo(this.mDurationUs);
        a(paramTuSdkMediaExtractor);
      }
      unLockAudio();
      return false;
    }
    
    public void syncAudioDecodecOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkAudioInfo paramTuSdkAudioInfo)
    {
      TuSdkAudioResample localTuSdkAudioResample = this.mAudioResample;
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = this.h;
      if ((paramBufferInfo == null) || (paramBufferInfo.size < 1) || (localTuSdkAudioResample == null) || (localTuSdkMediaTimeSliceEntity == null) || (!this.e))
      {
        unLockAudio();
        return;
      }
      if (TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs() > -1L)
      {
        this.k = this.h.calOutputAudioTimeUs(paramBufferInfo.presentationTimeUs);
        if (this.c != null) {
          this.c.setAudioBufferPts(this.h.calOutputAudioTimeUs(paramBufferInfo.presentationTimeUs));
        }
        unLockAudio();
        return;
      }
      if ((this.n.getSampleTime() < paramBufferInfo.presentationTimeUs) && (this.n.getSampleTime() >= 0L) && (this.g != null) && (!this.g.isReverse()))
      {
        this.h = this.g;
        return;
      }
      long l1 = paramBufferInfo.presentationTimeUs;
      paramBufferInfo.presentationTimeUs = localTuSdkMediaTimeSliceEntity.calOutputAudioTimeUs(l1);
      this.k = paramBufferInfo.presentationTimeUs;
      int i1 = localTuSdkMediaTimeSliceEntity.overviewAudio(l1);
      if (i1 < 0)
      {
        unLockAudio();
        return;
      }
      if (i1 > 0)
      {
        this.h = localTuSdkMediaTimeSliceEntity.next;
        if (this.h == null)
        {
          unLockAudio();
          return;
        }
        if (TuSdkMediaFileDirectorPlayerSync.b(TuSdkMediaFileDirectorPlayerSync.this, this.h))
        {
          unLockAudio();
          return;
        }
        a(this.h);
        if (this.h.overviewAudio(l1) == 0) {
          paramBufferInfo.presentationTimeUs = this.h.calOutputAudioTimeUs(l1);
        }
        unLockAudio();
        return;
      }
      MediaCodec.BufferInfo localBufferInfo = TuSdkMediaUtils.cloneBufferInfo(paramBufferInfo);
      localBufferInfo.presentationTimeUs = localTuSdkMediaTimeSliceEntity.calOutputOrginTimeUs(l1);
      if ((localTuSdkMediaTimeSliceEntity.next == null) && (localTuSdkMediaTimeSliceEntity.audioEndUs == l1)) {
        this.h = null;
      }
      localTuSdkAudioResample.queueInputBuffer(paramByteBuffer, localBufferInfo);
    }
    
    public void waitVideo(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity)
    {
      if (this.h == null) {
        return;
      }
      while ((!isInterrupted()) && (this.e) && (TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs() < 0L)) {
        if ((this.h == null) || (this.h.index == paramTuSdkMediaTimeSliceEntity.index)) {}
      }
    }
    
    public void syncAudioResampleOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      if ((paramBufferInfo == null) || (paramBufferInfo.size < 1)) {
        return;
      }
      this.mAudioPitch.queueInputBuffer(paramByteBuffer, paramBufferInfo);
    }
    
    private void a(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      TuSdkAudioTrack localTuSdkAudioTrack = this.b;
      if ((localTuSdkAudioTrack == null) || (paramBufferInfo == null) || (paramBufferInfo.size < 1))
      {
        unLockAudio();
        return;
      }
      this.mPreviousTimeUs = this.mLastTimeUs;
      this.mLastTimeUs = paramBufferInfo.presentationTimeUs;
      if (!this.m) {
        if (TuSdkMediaFileDirectorPlayerSync.h(TuSdkMediaFileDirectorPlayerSync.this))
        {
          localTuSdkAudioTrack.setVolume(0.0F);
        }
        else
        {
          setVolume(this.l);
          this.m = true;
        }
      }
      paramByteBuffer.position(paramBufferInfo.offset);
      paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
      localTuSdkAudioTrack.write(paramByteBuffer);
      unLockAudio();
    }
    
    public void lockAudio()
    {
      if (this.i.isLocked()) {
        return;
      }
      this.i.lock();
    }
    
    public void unLockAudio()
    {
      if (!this.i.isLocked()) {
        return;
      }
      this.i.unlock();
    }
    
    public void syncAudioPitchOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      TuSdkAudioRender localTuSdkAudioRender = TuSdkMediaFileDirectorPlayerSync.i(TuSdkMediaFileDirectorPlayerSync.this);
      if ((paramBufferInfo == null) || (paramBufferInfo.size < 1)) {
        return;
      }
      if ((TuSdkMediaFileDirectorPlayerSync.i(TuSdkMediaFileDirectorPlayerSync.this) == null) || (!localTuSdkAudioRender.onAudioSliceRender(paramByteBuffer, paramBufferInfo, this.o))) {
        a(paramByteBuffer, paramBufferInfo);
      }
    }
    
    public long getVideoDisplayTimeUs()
    {
      if (this.c == null) {
        return 0L;
      }
      return this.c.getVideoDisplayTimeUs();
    }
  }
  
  private class _VideoDecodecSync
    extends TuSdkVideoDecodecSyncBase
  {
    private boolean b = false;
    private TuSdkMediaTimeSliceEntity c;
    private TuSdkMediaTimeSliceEntity d;
    private long e = 0L;
    private long f = 0L;
    private ReentrantLock g = new ReentrantLock();
    private TuSdkMediaExtractor h;
    private boolean i = false;
    private TuSdkMediaTimeSlicePatch j = new TuSdkMediaTimeSlicePatch();
    private boolean k = false;
    
    private _VideoDecodecSync() {}
    
    public boolean isVideoEos()
    {
      return (this.b) && (this.d == null);
    }
    
    public void syncFlushAndSeekto(long paramLong)
    {
      this.b = false;
      TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this, true);
      super.syncFlushAndSeekto(paramLong);
    }
    
    public void syncVideoDecodecInfo(TuSdkVideoInfo paramTuSdkVideoInfo, TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if ((paramTuSdkVideoInfo == null) || (paramTuSdkMediaExtractor == null)) {
        return;
      }
      long l1 = paramTuSdkVideoInfo.durationUs;
      paramTuSdkMediaExtractor.seekTo(l1, 0);
      for (long l2 = paramTuSdkMediaExtractor.getSampleTime(); paramTuSdkMediaExtractor.advance(); l2 = Math.max(paramTuSdkMediaExtractor.getSampleTime(), l2)) {}
      paramTuSdkVideoInfo.durationUs = (l2 > 0L ? l2 : l1);
      paramTuSdkMediaExtractor.seekTo(0L);
      super.syncVideoDecodecInfo(paramTuSdkVideoInfo, paramTuSdkMediaExtractor);
      TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this).setInputDurationUs(paramTuSdkVideoInfo.durationUs);
      this.h = paramTuSdkMediaExtractor;
    }
    
    private void a(TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if ((this.c == null) || (paramTuSdkMediaExtractor == null)) {
        return;
      }
      this.c = this.c.next;
      if (this.c != null)
      {
        this.e = this.c.endUs;
        long l = paramTuSdkMediaExtractor.seekTo(this.c.startUs);
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
    
    public boolean syncVideoDecodecExtractor(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec)
    {
      if ((this.mReleased) || (paramTuSdkMediaExtractor == null) || (paramTuSdkMediaCodec == null)) {
        return true;
      }
      lockVideo();
      long l1 = -1L;
      if (!TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this).isFixTimeSlices())
      {
        flush(paramTuSdkMediaCodec);
        TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this).fixTimeSlices(paramTuSdkMediaExtractor, isSupportPrecise(), true);
        this.d = (this.c = TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this).sliceWithOutputTimeUs(this.mFlushAndSeekto));
        if (this.c != null)
        {
          this.e = this.c.endUs;
          l1 = this.c.calInputTimeUs(this.mFlushAndSeekto);
          l1 = paramTuSdkMediaExtractor.seekTo(l1 - 1L);
          this.f = l1;
          this.mOutputTimeUs = this.c.calOutputTimeUs(l1);
        }
        TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this, this.c, l1);
        this.mFlushAndSeekto = -1L;
        this.mRelativeStartNs = -1L;
        TuSdkMediaFileDirectorPlayerSync.b(TuSdkMediaFileDirectorPlayerSync.this).setRelativeStartNs(this.mRelativeStartNs);
        TuSdkMediaFileDirectorPlayerSync.b(TuSdkMediaFileDirectorPlayerSync.this).reset();
        this.b = true;
      }
      if (!this.b)
      {
        unLockVideo();
        return false;
      }
      if (!TuSdkMediaFileDirectorPlayerSync.c(TuSdkMediaFileDirectorPlayerSync.this))
      {
        unLockVideo();
        return false;
      }
      if (TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs() > -1L)
      {
        l2 = (this.c != null) && (this.c.isReverse()) ? l1 : TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs();
        if ((l2 == -1L) && (this.c != null)) {
          l2 = this.c.startUs;
        }
        l1 = paramTuSdkMediaExtractor.seekTo(l2);
        TuSdkMediaUtils.putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, false);
        this.mOutputTimeUs = (this.c == null ? l1 : this.c.calOutputTimeUs(l1));
        TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this, false);
        unLockVideo();
        return false;
      }
      long l2 = paramTuSdkMediaExtractor.getSampleTime();
      boolean bool = TuSdkMediaUtils.putBufferToCoderUntilEnd(paramTuSdkMediaExtractor, paramTuSdkMediaCodec, false);
      this.mFrameIntervalUs = paramTuSdkMediaExtractor.getFrameIntervalUs();
      if (this.c == null)
      {
        paramTuSdkMediaExtractor.seekTo(this.e);
      }
      else if (this.c.overview(l2) > 0)
      {
        a(paramTuSdkMediaExtractor);
      }
      else
      {
        long l3;
        if (this.c.isReverse())
        {
          if (this.mMinFrameTimeUs == l2)
          {
            a(paramTuSdkMediaExtractor);
            unLockVideo();
            return false;
          }
          l3 = b(l2);
          if (TuSdkMediaFileDirectorPlayerSync.d(TuSdkMediaFileDirectorPlayerSync.this)) {
            a(paramTuSdkMediaExtractor, l3, l2);
          } else {
            paramTuSdkMediaExtractor.seekTo(l3, 0);
          }
        }
        else if ((bool) || (paramTuSdkMediaExtractor.getSampleTime() < 0L))
        {
          this.mMaxFrameTimeUs = paramTuSdkMediaExtractor.seekTo(this.mDurationUs);
          a(paramTuSdkMediaExtractor);
        }
        else if (this.c.speed > 1.0F)
        {
          l3 = a(l2);
          paramTuSdkMediaExtractor.seekTo(l3, 2);
        }
      }
      unLockVideo();
      return false;
    }
    
    private void a(TuSdkMediaExtractor paramTuSdkMediaExtractor, long paramLong1, long paramLong2)
    {
      if (!TuSdkMediaFileDirectorPlayerSync.d(TuSdkMediaFileDirectorPlayerSync.this)) {
        return;
      }
      long l = paramLong2;
      while (l >= paramLong2)
      {
        l = paramTuSdkMediaExtractor.seekTo(paramLong1, 0);
        paramLong1 -= 200L;
      }
    }
    
    private long a(long paramLong, MediaCodec.BufferInfo paramBufferInfo)
    {
      long l1;
      synchronized (this.mLocker)
      {
        TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = this.d;
        long l2 = paramBufferInfo.presentationTimeUs;
        long l3 = localTuSdkMediaTimeSliceEntity.calOutputTimeUs(l2);
        paramBufferInfo.presentationTimeUs = l3;
        if ((paramLong < l2) && (paramLong >= 0L) && (this.d != null) && (!this.d.isReverse()) && (this.c != null) && (!this.c.isReverse()))
        {
          this.d = this.c;
          return System.nanoTime();
        }
        if (this.j.isReturnFrame(paramLong, l2)) {
          return System.nanoTime();
        }
        if (this.j.overview(localTuSdkMediaTimeSliceEntity, paramLong, l2))
        {
          this.j.switchSliced();
          this.d = localTuSdkMediaTimeSliceEntity.next;
          TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this, this.d);
          this.f = paramBufferInfo.presentationTimeUs;
          save();
          return System.nanoTime();
        }
        if ((localTuSdkMediaTimeSliceEntity.next == null) && (localTuSdkMediaTimeSliceEntity.endUs == l2)) {
          this.d = null;
        }
        if (this.mRelativeStartNs < 0L)
        {
          this.f = localTuSdkMediaTimeSliceEntity.calOutputNoRepetTimeUs(l3, TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this));
          this.mOutputTimeUs = paramBufferInfo.presentationTimeUs;
          this.mRelativeStartNs = System.nanoTime();
          TuSdkMediaFileDirectorPlayerSync.b(TuSdkMediaFileDirectorPlayerSync.this).setRelativeStartNs(this.mRelativeStartNs);
        }
        this.f = localTuSdkMediaTimeSliceEntity.calOutputNoRepetTimeUs(l3, TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this));
        this.mPreviousTimeUs = this.mOutputTimeUs;
        this.mOutputTimeUs = paramBufferInfo.presentationTimeUs;
        l1 = this.mRelativeStartNs += Math.abs(this.mOutputTimeUs - this.mPreviousTimeUs) * 1000L;
      }
      return l1;
    }
    
    public void syncVideoDecodecOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkVideoInfo paramTuSdkVideoInfo)
    {
      TuSdkMediaFileCuterTimeline localTuSdkMediaFileCuterTimeline = TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this);
      if (!this.b)
      {
        unLockVideo();
        return;
      }
      if ((paramBufferInfo == null) || (paramBufferInfo.size < 1) || (this.h == null))
      {
        unLockVideo();
        return;
      }
      if (TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs() > -1L)
      {
        l1 = paramBufferInfo.presentationTimeUs;
        TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = localTuSdkMediaFileCuterTimeline.existenceWithInputTimeUs(l1);
        if (localTuSdkMediaTimeSliceEntity != null)
        {
          this.mOutputTimeUs = localTuSdkMediaTimeSliceEntity.calOutputTimeUs(l1);
          this.f = localTuSdkMediaTimeSliceEntity.calOutputTimeUs(l1);
          paramBufferInfo.presentationTimeUs = this.mOutputTimeUs;
          TuSdkMediaFileDirectorPlayerSync.b(TuSdkMediaFileDirectorPlayerSync.this).reset();
          TuSdkMediaFileDirectorPlayerSync.b(TuSdkMediaFileDirectorPlayerSync.this).setVideoBufferTimeUs(this.f);
        }
        unLockVideo();
        return;
      }
      if (this.d == null)
      {
        unLockVideo();
        return;
      }
      long l1 = this.d.calOutputTimeUs(paramBufferInfo.presentationTimeUs);
      long l2 = a(this.h.getSampleTime(), paramBufferInfo);
      long l3 = System.nanoTime() / 1000L;
      if (TuSdkMediaFileDirectorPlayerSync.b(TuSdkMediaFileDirectorPlayerSync.this) != null)
      {
        TuSdkMediaFileDirectorPlayerSync.b(TuSdkMediaFileDirectorPlayerSync.this).setVideoBufferTimeUs(l1);
        l3 = TuSdkMediaFileDirectorPlayerSync.b(TuSdkMediaFileDirectorPlayerSync.this).getVideoDisplayTimeUs();
      }
      syncPlay(l2);
      unLockVideo();
    }
    
    public boolean waitAudio(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity)
    {
      if (this.d == null) {
        return false;
      }
      while ((!isInterrupted()) && (this.b) && (TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs() < 0L))
      {
        if ((paramTuSdkMediaTimeSliceEntity == null) || (this.d.taskID != paramTuSdkMediaTimeSliceEntity.taskID)) {
          return true;
        }
        if ((this.d == null) || (this.d.index == paramTuSdkMediaTimeSliceEntity.index)) {
          return false;
        }
      }
      return false;
    }
    
    public void resumeSave()
    {
      super.resumeSave();
      TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this, true);
    }
    
    protected void syncPause()
    {
      if ((isPause()) && (TuSdkMediaFileDirectorPlayerSync.e(TuSdkMediaFileDirectorPlayerSync.this) != null)) {
        TuSdkMediaFileDirectorPlayerSync.e(TuSdkMediaFileDirectorPlayerSync.this).onPauseWait();
      }
      while ((!isInterrupted()) && (isPause()) && (!TuSdkMediaFileDirectorPlayerSync.f(TuSdkMediaFileDirectorPlayerSync.this))) {
        if (TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs() > -1L) {
          break;
        }
      }
      if (TuSdkMediaFileDirectorPlayerSync.f(TuSdkMediaFileDirectorPlayerSync.this)) {
        TuSdkMediaFileDirectorPlayerSync.b(TuSdkMediaFileDirectorPlayerSync.this, false);
      }
    }
    
    public void lockVideo()
    {
      if (this.g.isLocked()) {
        return;
      }
      this.g.lock();
    }
    
    public void unLockVideo()
    {
      if (!this.g.isLocked()) {
        return;
      }
      this.g.unlock();
    }
    
    public long lastVideoFrameTimestampUs()
    {
      return this.f;
    }
    
    public long calcInputTimeUs(long paramLong)
    {
      if ((TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this) == null) || (TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this).getCalcMode() == 0) || (!TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this).isFixTimeSlices())) {
        return paramLong;
      }
      TuSdkMediaTimeSliceEntity localTuSdkMediaTimeSliceEntity = TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this).sliceWithOutputTimeUs(paramLong);
      if (localTuSdkMediaTimeSliceEntity == null) {
        return paramLong;
      }
      return localTuSdkMediaTimeSliceEntity.calOutputNoRepetTimeUs(paramLong, TuSdkMediaFileDirectorPlayerSync.this.getTimeline());
    }
    
    public long calcEffectFrameTimeUs(long paramLong)
    {
      if ((TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this) == null) || (TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this).getCalcMode() == 0) || (!TuSdkMediaFileDirectorPlayerSync.a(TuSdkMediaFileDirectorPlayerSync.this).isFixTimeSlices())) {
        return paramLong;
      }
      if (TuSdkMediaFileDirectorPlayerSync.g(TuSdkMediaFileDirectorPlayerSync.this) == null) {
        return paramLong;
      }
      return TuSdkMediaFileDirectorPlayerSync.g(TuSdkMediaFileDirectorPlayerSync.this).calcEffectFrameUs(paramLong, this.d);
    }
  }
  
  public static abstract interface TuSdkDirectorPlayerStateCallback
  {
    public abstract void onPauseWait();
  }
  
  public static abstract interface TuSdkEffectFrameCalc
  {
    public abstract long calcEffectFrameUs(long paramLong, TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkMediaFileDirectorPlayerSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */