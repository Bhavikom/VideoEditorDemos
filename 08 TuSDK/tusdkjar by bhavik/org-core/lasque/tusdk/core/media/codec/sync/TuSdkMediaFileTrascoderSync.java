package org.lasque.tusdk.core.media.codec.sync;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;

@TargetApi(18)
public class TuSdkMediaFileTrascoderSync
  implements TuSdkMediaDecodecSync, TuSdkMediaEncodecSync
{
  private long a = System.nanoTime();
  private long b;
  private boolean c = false;
  private int d = -1;
  private int e = 0;
  private boolean f = false;
  private long g = 0L;
  private long h = 0L;
  private long i = 0L;
  private long j = 0L;
  private long k = 0L;
  private long l = 0L;
  private TuSdkAudioEncodecOperation m;
  private TuSdkAudioResample n;
  private _AudioEncodecSync o;
  private _VideoEncodecSync p;
  private _AudioDecodecSync q;
  private _VideoDecodecSync r;
  
  public TuSdkAudioDecodecSync buildAudioDecodecSync()
  {
    if (this.q != null)
    {
      this.q.release();
      this.q = null;
    }
    this.q = new _AudioDecodecSync(null);
    this.q.setAudioResample(this.n);
    return this.q;
  }
  
  public TuSdkVideoDecodecSync buildVideoDecodecSync()
  {
    if (this.r != null)
    {
      this.r.release();
      this.r = null;
    }
    this.r = new _VideoDecodecSync(null);
    return this.r;
  }
  
  public TuSdkVideoDecodecSync getVideoDecodecSync()
  {
    return this.r;
  }
  
  public TuSdkAudioDecodecSync getAudioDecodecSync()
  {
    return this.q;
  }
  
  public TuSdkAudioEncodecSync getAudioEncodecSync()
  {
    if (this.o == null) {
      this.o = new _AudioEncodecSync(null);
    }
    return this.o;
  }
  
  public TuSdkVideoEncodecSync getVideoEncodecSync()
  {
    if (this.p == null) {
      this.p = new _VideoEncodecSync(null);
    }
    return this.p;
  }
  
  public void release()
  {
    this.c = true;
    a();
    if (this.o != null)
    {
      this.o.release();
      this.o = null;
    }
    if (this.p != null)
    {
      this.p.release();
      this.p = null;
    }
  }
  
  private void a()
  {
    if (this.q != null)
    {
      this.q.release();
      this.q = null;
    }
    if (this.r != null)
    {
      this.r.release();
      this.r = null;
    }
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public void addAudioEncodecOperation(TuSdkAudioEncodecOperation paramTuSdkAudioEncodecOperation)
  {
    this.m = paramTuSdkAudioEncodecOperation;
  }
  
  public void setBenchmarkEnd()
  {
    this.b = (System.nanoTime() - this.a);
  }
  
  public long benchmarkUs()
  {
    return this.b / 1000L;
  }
  
  public long totalDurationUs()
  {
    return this.g + Math.max(this.h, this.l);
  }
  
  public long lastVideoEndTimeUs()
  {
    return this.g;
  }
  
  public long lastVideoDecodecTimestampNs()
  {
    return this.k * 1000L;
  }
  
  public void setTotal(int paramInt)
  {
    this.e = paramInt;
  }
  
  public int total()
  {
    return this.e;
  }
  
  public boolean isLast()
  {
    boolean bool = this.d + 1 >= this.e;
    return bool;
  }
  
  public int lastIndex()
  {
    return this.d;
  }
  
  public boolean isEncodecCompleted()
  {
    return (isVideoEncodeCompleted()) && (isAudioEncodeCompleted());
  }
  
  public float calculateProgress()
  {
    float f1 = 0.0F;
    if (this.h > 0L) {
      f1 = (this.d + (float)(this.k - this.g) / (float)this.h) / this.e;
    }
    return Math.min(Math.max(f1, 0.0F), 1.0F);
  }
  
  public boolean syncDecodecNext()
  {
    if ((this.d > -1) && ((!isAudioDecodeCompleted()) || (!isVideoDecodeCompleted()))) {
      return false;
    }
    if (isLast()) {
      return false;
    }
    a();
    this.d += 1;
    this.f = false;
    this.g += Math.max(this.h, this.l);
    this.j = 0L;
    if (this.p != null) {
      this.p.clearLocker();
    }
    return true;
  }
  
  public void syncVideoDecodeCompleted()
  {
    if (this.r == null) {
      return;
    }
    this.r.syncVideoDecodeCompleted();
  }
  
  public boolean isVideoDecodeCompleted()
  {
    if (this.r == null) {
      return true;
    }
    return this.r.isVideoDecodeCompleted();
  }
  
  public void syncAudioDecodeCompleted()
  {
    if (this.q == null) {
      return;
    }
    this.q.syncAudioDecodeCompleted();
  }
  
  public boolean isAudioDecodeCompleted()
  {
    if (this.q == null) {
      return true;
    }
    return this.q.isAudioDecodeCompleted();
  }
  
  public boolean isAudioDecodeCrashed()
  {
    if (this.q == null) {
      return false;
    }
    return this.q.isAudioDecodeCrashed();
  }
  
  public boolean isAudioEncodeCompleted()
  {
    if (this.o == null) {
      return true;
    }
    return this.o.isAudioEncodeCompleted();
  }
  
  public boolean isVideoEncodeCompleted()
  {
    if (this.p == null) {
      return true;
    }
    return this.p.isVideoEncodeCompleted();
  }
  
  public void syncVideoEncodecDrawFrame(long paramLong, boolean paramBoolean, TuSdkRecordSurface paramTuSdkRecordSurface, TuSdkEncodeSurface paramTuSdkEncodeSurface)
  {
    if (this.p == null) {
      return;
    }
    this.p.syncVideoEncodecDrawFrame(paramLong, paramBoolean, paramTuSdkRecordSurface, paramTuSdkEncodeSurface);
  }
  
  private void a(TuSdkAudioResample paramTuSdkAudioResample)
  {
    if (paramTuSdkAudioResample == null) {
      return;
    }
    this.n = paramTuSdkAudioResample;
    if (this.q != null) {
      this.q.setAudioResample(paramTuSdkAudioResample);
    }
  }
  
  private class _VideoEncodecSync
    extends TuSdkVideoEncodecSyncBase
  {
    private _VideoEncodecSync() {}
    
    protected boolean isLastDecodeFrame(long paramLong)
    {
      if ((TuSdkMediaFileTrascoderSync.e(TuSdkMediaFileTrascoderSync.this) < 1L) || (TuSdkMediaFileTrascoderSync.f(TuSdkMediaFileTrascoderSync.this) < 1L)) {
        return false;
      }
      return TuSdkMediaFileTrascoderSync.e(TuSdkMediaFileTrascoderSync.this) - paramLong < TuSdkMediaFileTrascoderSync.f(TuSdkMediaFileTrascoderSync.this);
    }
    
    protected long getInputIntervalUs()
    {
      return TuSdkMediaFileTrascoderSync.f(TuSdkMediaFileTrascoderSync.this);
    }
  }
  
  private class _VideoDecodecSync
    extends TuSdkVideoDecodecSyncBase
  {
    private _VideoDecodecSync() {}
    
    public boolean syncVideoDecodecExtractor(TuSdkMediaExtractor paramTuSdkMediaExtractor, TuSdkMediaCodec paramTuSdkMediaCodec)
    {
      boolean bool = super.syncVideoDecodecExtractor(paramTuSdkMediaExtractor, paramTuSdkMediaCodec);
      TuSdkMediaFileTrascoderSync.b(TuSdkMediaFileTrascoderSync.this, this.mFrameIntervalUs);
      return bool;
    }
    
    public void syncVideoDecodecInfo(TuSdkVideoInfo paramTuSdkVideoInfo, TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if ((paramTuSdkVideoInfo == null) || (paramTuSdkMediaExtractor == null)) {
        return;
      }
      super.syncVideoDecodecInfo(paramTuSdkVideoInfo, paramTuSdkMediaExtractor);
      TuSdkMediaFileTrascoderSync.c(TuSdkMediaFileTrascoderSync.this, paramTuSdkVideoInfo.durationUs);
      TuSdkMediaFileTrascoderSync.d(TuSdkMediaFileTrascoderSync.this, TuSdkMediaFileTrascoderSync.b(TuSdkMediaFileTrascoderSync.this) + paramTuSdkVideoInfo.durationUs);
    }
    
    public void syncVideoDecodecOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkVideoInfo paramTuSdkVideoInfo)
    {
      TuSdkMediaFileTrascoderSync._VideoEncodecSync local_VideoEncodecSync = TuSdkMediaFileTrascoderSync.d(TuSdkMediaFileTrascoderSync.this);
      if ((paramBufferInfo == null) || (paramBufferInfo.size < 1) || (local_VideoEncodecSync == null)) {
        return;
      }
      while ((!isInterrupted()) && (local_VideoEncodecSync.hasLocked())) {}
      this.mPreviousTimeUs = this.mOutputTimeUs;
      paramBufferInfo.presentationTimeUs += TuSdkMediaFileTrascoderSync.b(TuSdkMediaFileTrascoderSync.this);
      TuSdkMediaFileTrascoderSync.e(TuSdkMediaFileTrascoderSync.this, this.mOutputTimeUs = paramBufferInfo.presentationTimeUs);
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
      TuSdkMediaFileTrascoderSync.a(TuSdkMediaFileTrascoderSync.this, getAudioResample());
    }
  }
  
  private class _AudioDecodecSync
    extends TuSdkAudioDecodecSyncBase
  {
    private _AudioDecodecSync() {}
    
    public void syncAudioDecodecInfo(TuSdkAudioInfo paramTuSdkAudioInfo, TuSdkMediaExtractor paramTuSdkMediaExtractor)
    {
      if ((paramTuSdkAudioInfo == null) || (paramTuSdkMediaExtractor == null)) {
        return;
      }
      super.syncAudioDecodecInfo(paramTuSdkAudioInfo, paramTuSdkMediaExtractor);
      TuSdkMediaFileTrascoderSync.a(TuSdkMediaFileTrascoderSync.this, paramTuSdkAudioInfo.durationUs);
      while ((!isInterrupted()) && (this.mAudioResample == null)) {}
      if (this.mAudioResample != null) {
        this.mAudioResample.changeFormat(paramTuSdkAudioInfo);
      }
    }
    
    public void syncAudioDecodecOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo, TuSdkAudioInfo paramTuSdkAudioInfo)
    {
      TuSdkAudioEncodecOperation localTuSdkAudioEncodecOperation = TuSdkMediaFileTrascoderSync.a(TuSdkMediaFileTrascoderSync.this);
      TuSdkAudioResample localTuSdkAudioResample = this.mAudioResample;
      if ((localTuSdkAudioEncodecOperation == null) || (localTuSdkAudioResample == null) || (paramBufferInfo == null) || (paramBufferInfo.size < 1)) {
        return;
      }
      this.mPreviousTimeUs = this.mLastTimeUs;
      paramBufferInfo.presentationTimeUs += TuSdkMediaFileTrascoderSync.b(TuSdkMediaFileTrascoderSync.this);
      this.mLastTimeUs = paramBufferInfo.presentationTimeUs;
      if (!TuSdkMediaFileTrascoderSync.c(TuSdkMediaFileTrascoderSync.this))
      {
        TuSdkMediaFileTrascoderSync.a(TuSdkMediaFileTrascoderSync.this, true);
        if (paramBufferInfo.presentationTimeUs > TuSdkMediaFileTrascoderSync.b(TuSdkMediaFileTrascoderSync.this)) {
          localTuSdkAudioEncodecOperation.autoFillMuteDataWithinEnd(TuSdkMediaFileTrascoderSync.b(TuSdkMediaFileTrascoderSync.this), paramBufferInfo.presentationTimeUs);
        }
      }
      localTuSdkAudioResample.queueInputBuffer(paramByteBuffer, paramBufferInfo);
    }
    
    public void syncAudioDecodeCrashed(Exception paramException)
    {
      super.syncAudioDecodeCrashed(paramException);
      if (paramException == null) {
        return;
      }
      TuSdkMediaFileTrascoderSync.a(TuSdkMediaFileTrascoderSync.this, 0L);
    }
    
    public void syncAudioResampleOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      TuSdkCodecCapabilities.logBufferInfo(String.format("%s resampleOutputBuffer", new Object[] { "TuSdkMediaFileTrascoderSync" }), paramBufferInfo);
      TuSdkAudioEncodecOperation localTuSdkAudioEncodecOperation = TuSdkMediaFileTrascoderSync.a(TuSdkMediaFileTrascoderSync.this);
      if ((localTuSdkAudioEncodecOperation == null) || (paramBufferInfo == null) || (paramBufferInfo.size < 1)) {
        return;
      }
      paramByteBuffer.position(paramBufferInfo.offset);
      paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
      while ((!isInterrupted()) && (localTuSdkAudioEncodecOperation.writeBuffer(paramByteBuffer, paramBufferInfo) == 0)) {}
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkMediaFileTrascoderSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */