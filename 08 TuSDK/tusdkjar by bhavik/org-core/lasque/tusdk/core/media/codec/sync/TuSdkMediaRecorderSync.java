package org.lasque.tusdk.core.media.codec.sync;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

@TargetApi(16)
public class TuSdkMediaRecorderSync
  implements TuSdkMediaEncodecSync
{
  private boolean a = false;
  private boolean b = false;
  private final TuSdkMediaTimeline c = new TuSdkMediaTimeline(0L);
  private float d = 1.0F;
  private float e = 1.0F;
  private TuSdkAudioEncodecOperation f;
  private _AudioEncodecSync g;
  private _VideoEncodecSync h;
  
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
    this.a = true;
    if (this.g != null) {
      this.g.release();
    }
    if (this.h != null) {
      this.h.release();
    }
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public boolean isEncodecCompleted()
  {
    return (isVideoEncodeCompleted()) && (isAudioEncodeCompleted());
  }
  
  public long getAudioEncodecTimeUs()
  {
    if (this.g == null) {
      return -1L;
    }
    return this.g.lastStandardPtsUs();
  }
  
  public long getVideoEncodecTimeUs()
  {
    if (this.h == null) {
      return -1L;
    }
    return this.h.lastStandardPtsUs();
  }
  
  public void setAudioOperation(TuSdkAudioEncodecOperation paramTuSdkAudioEncodecOperation)
  {
    this.f = paramTuSdkAudioEncodecOperation;
  }
  
  public void changeSpeed(float paramFloat)
  {
    if ((paramFloat <= 0.0F) || (this.d == paramFloat)) {
      return;
    }
    this.e = this.d;
    this.d = paramFloat;
  }
  
  public void pauseRecord()
  {
    if (this.b) {
      return;
    }
    this.b = true;
  }
  
  public void resumeRecord()
  {
    if (!this.b) {
      return;
    }
    this.b = false;
    long l1 = getVideoEncodecTimeUs();
    long l2 = getAudioEncodecTimeUs();
    if (this.h != null)
    {
      this.c.append(this.h.getPreviousStartTimeUs());
      this.h.reset(l2);
    }
    if (this.g != null) {
      this.g.reset(l1);
    }
  }
  
  public TuSdkMediaTimeline endOfTimeline()
  {
    if (this.h == null) {
      return null;
    }
    this.c.setInputDurationUs(getVideoEncodecTimeUs());
    return this.c;
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
  
  public void syncVideoEncodecDrawFrame(long paramLong, boolean paramBoolean, TuSdkEncodeSurface paramTuSdkEncodeSurface)
  {
    if (this.h == null) {
      return;
    }
    this.h.syncVideoEncodecDrawFrame(paramLong, paramBoolean, paramTuSdkEncodeSurface);
  }
  
  public void syncAudioEncodecFrame(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (this.g == null) {
      return;
    }
    if ((this.h != null) && (!this.h.isEncodecStarted())) {
      return;
    }
    this.g.syncAudioEncodecFrame(paramByteBuffer, paramBufferInfo);
  }
  
  private class _VideoEncodecSync
    implements TuSdkVideoEncodecSync
  {
    private boolean b = false;
    private int c = 0;
    private long d = 0L;
    private long e = 0L;
    private long f = 0L;
    private boolean g = false;
    private long h = 0L;
    private long i = -1L;
    private long j = -1L;
    private long k = 0L;
    private boolean l = false;
    
    private _VideoEncodecSync() {}
    
    public long getPreviousStartTimeUs()
    {
      return this.k;
    }
    
    public void reset(long paramLong)
    {
      this.i = -1L;
      this.j = paramLong;
    }
    
    public boolean isEncodecStarted()
    {
      return this.i > 0L;
    }
    
    public boolean isInterrupted()
    {
      return (ThreadHelper.isInterrupted()) || (this.b);
    }
    
    public boolean isVideoEncodeCompleted()
    {
      return this.g;
    }
    
    public void release()
    {
      this.b = true;
      TuSdkMediaRecorderSync.a(TuSdkMediaRecorderSync.this, 1.0F);
    }
    
    private long a(long paramLong)
    {
      if (this.c < 1) {
        return 0L;
      }
      long l1 = paramLong * 1000000L / this.c;
      return l1;
    }
    
    public long lastStandardPtsUs()
    {
      return this.h;
    }
    
    public long nextStandardPtsUs()
    {
      return a(this.d + 1L);
    }
    
    public void syncEncodecVideoInfo(TuSdkVideoInfo paramTuSdkVideoInfo)
    {
      if (paramTuSdkVideoInfo != null) {
        this.c = paramTuSdkVideoInfo.frameRates;
      }
      this.l = true;
    }
    
    public void syncVideoEncodecOutputBuffer(TuSdkMediaMuxer paramTuSdkMediaMuxer, int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      if (paramBufferInfo.presentationTimeUs == 1L) {
        return;
      }
      TuSdkMediaUtils.processOutputBuffer(paramTuSdkMediaMuxer, paramInt, paramByteBuffer, paramBufferInfo);
    }
    
    public void syncVideoEncodecUpdated(MediaCodec.BufferInfo paramBufferInfo)
    {
      if (paramBufferInfo == null) {
        return;
      }
      if (TLog.LOG_VIDEO_ENCODEC_INFO) {
        TuSdkCodecCapabilities.logBufferInfo(String.format("%s syncVideoEncodecUpdated", new Object[] { "TuSdkMediaRecorderSync" }), paramBufferInfo);
      }
    }
    
    public void syncVideoEncodecCompleted()
    {
      if (TLog.LOG_VIDEO_ENCODEC_INFO) {
        TLog.d("%s syncVideoEncodecCompleted", new Object[] { "TuSdkMediaRecorderSync" });
      }
      this.g = true;
    }
    
    private boolean a()
    {
      return TuSdkMediaRecorderSync.b(TuSdkMediaRecorderSync.this);
    }
    
    private void a(TuSdkEncodeSurface paramTuSdkEncodeSurface)
    {
      if (this.j < 0L) {
        return;
      }
      if (nextStandardPtsUs() < this.j) {
        return;
      }
      for (long l1 = a(this.d); nextStandardPtsUs() < this.j; l1 = a(this.d))
      {
        paramTuSdkEncodeSurface.duplicateFrameReadyInGLThread(l1 * 1000L);
        paramTuSdkEncodeSurface.swapBuffers(l1 * 1000L);
        this.d += 1L;
      }
    }
    
    public void syncVideoEncodecDrawFrame(long paramLong, boolean paramBoolean, TuSdkEncodeSurface paramTuSdkEncodeSurface)
    {
      if ((paramTuSdkEncodeSurface == null) || (this.b)) {
        return;
      }
      if (paramBoolean) {
        return;
      }
      if (!this.l)
      {
        paramTuSdkEncodeSurface.newFrameReadyInGLThread(1000L);
        paramTuSdkEncodeSurface.swapBuffers(1000L);
        return;
      }
      long l1 = paramLong / 1000L;
      if (this.i < 0L)
      {
        a(paramTuSdkEncodeSurface);
        paramTuSdkEncodeSurface.requestKeyFrame();
        this.k = this.h;
        this.i = l1;
      }
      l1 = Math.floor((float)(l1 - this.i) / TuSdkMediaRecorderSync.c(TuSdkMediaRecorderSync.this)) + this.k;
      this.e = l1;
      if (TuSdkMediaRecorderSync.d(TuSdkMediaRecorderSync.this) != TuSdkMediaRecorderSync.c(TuSdkMediaRecorderSync.this))
      {
        paramTuSdkEncodeSurface.flush();
        paramTuSdkEncodeSurface.requestKeyFrame();
        TuSdkMediaRecorderSync.a(TuSdkMediaRecorderSync.this, TuSdkMediaRecorderSync.c(TuSdkMediaRecorderSync.this));
      }
      long l2 = l1;
      if (l2 < 1L)
      {
        paramTuSdkEncodeSurface.requestKeyFrame();
        paramTuSdkEncodeSurface.newFrameReadyInGLThread(0L);
        this.d += 1L;
        paramTuSdkEncodeSurface.swapBuffers(0L);
        return;
      }
      paramLong = l2 * 1000L;
      GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
      GLES20.glClear(16384);
      paramTuSdkEncodeSurface.newFrameReadyInGLThread(paramLong);
      if (a())
      {
        paramTuSdkEncodeSurface.requestKeyFrame();
        this.d += 1L;
        paramTuSdkEncodeSurface.swapBuffers(paramLong);
        this.h = l2;
        return;
      }
      this.d += 1L;
      this.h = l2;
      paramTuSdkEncodeSurface.swapBuffers(paramLong);
    }
  }
  
  private class _AudioEncodecSync
    extends TuSdkAudioEncodecSyncBase
  {
    private long b = -1L;
    private long c = -1L;
    
    private _AudioEncodecSync() {}
    
    public void reset(long paramLong)
    {
      this.b = -1L;
      this.c = paramLong;
    }
    
    public void syncAudioEncodecFrame(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      TuSdkAudioEncodecOperation localTuSdkAudioEncodecOperation = TuSdkMediaRecorderSync.a(TuSdkMediaRecorderSync.this);
      if ((paramByteBuffer == null) || (paramBufferInfo.size < 1) || (localTuSdkAudioEncodecOperation == null) || (this.mReleased)) {
        return;
      }
      if (this.b < 0L)
      {
        this.b = paramBufferInfo.presentationTimeUs;
        a(localTuSdkAudioEncodecOperation);
      }
      paramBufferInfo.presentationTimeUs = getAndAddCountPtsUs();
      TuSdkCodecCapabilities.logBufferInfo("[debug] in audio sync", paramBufferInfo);
      while ((!isInterrupted()) && (localTuSdkAudioEncodecOperation.writeBuffer(paramByteBuffer, paramBufferInfo) == 0)) {}
    }
    
    private void a(TuSdkAudioEncodecOperation paramTuSdkAudioEncodecOperation)
    {
      if (this.c < 0L) {
        return;
      }
      if (nextStandardPtsUs() > this.c) {
        return;
      }
      long l1 = lastStandardPtsUs();
      long l2 = getAndAddCountPtsUs(this.c);
      paramTuSdkAudioEncodecOperation.autoFillMuteData(l1, l2, false);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkMediaRecorderSync.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */