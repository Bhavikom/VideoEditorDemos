package org.lasque.tusdk.core.media.codec.sync;

import android.media.MediaCodec.BufferInfo;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

public abstract class TuSdkVideoEncodecSyncBase
  implements TuSdkVideoEncodecSync
{
  protected boolean mReleased = false;
  private TuSdkVideoInfo a;
  protected long mLastTimeUs = 0L;
  protected int mFrameRates = 0;
  protected long mFrameCounts = 0L;
  protected long mPreviousTimeUs = -1L;
  protected long mFrameIntervalUs = 0L;
  private boolean b = false;
  private final List<Long> c = new ArrayList();
  protected final Object mSyncLock = new Object();
  
  public boolean hasLocked()
  {
    return this.c.size() > 0;
  }
  
  public void clearLocker()
  {
    synchronized (this.mSyncLock)
    {
      this.c.clear();
    }
  }
  
  public void lockVideoTimestampUs(long paramLong)
  {
    synchronized (this.mSyncLock)
    {
      this.c.add(Long.valueOf(paramLong));
    }
  }
  
  public void unlockVideoTimestampUs(long paramLong)
  {
    synchronized (this.mSyncLock)
    {
      this.c.remove(Long.valueOf(paramLong));
    }
  }
  
  public boolean hadLockVideoTimestampUs(long paramLong)
  {
    boolean bool = false;
    synchronized (this.mSyncLock)
    {
      bool = this.c.contains(Long.valueOf(paramLong));
    }
    return bool;
  }
  
  public long getLastTimeUs()
  {
    return this.mLastTimeUs;
  }
  
  public boolean isInterrupted()
  {
    return (ThreadHelper.isInterrupted()) || (this.mReleased);
  }
  
  public boolean isVideoEncodeCompleted()
  {
    return this.b;
  }
  
  public void release()
  {
    this.mReleased = true;
  }
  
  public void syncEncodecVideoInfo(TuSdkVideoInfo paramTuSdkVideoInfo)
  {
    this.a = paramTuSdkVideoInfo;
    if (paramTuSdkVideoInfo != null) {
      this.mFrameRates = paramTuSdkVideoInfo.frameRates;
    }
  }
  
  public void syncVideoEncodecOutputBuffer(TuSdkMediaMuxer paramTuSdkMediaMuxer, int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    TuSdkMediaUtils.processOutputBuffer(paramTuSdkMediaMuxer, paramInt, paramByteBuffer, paramBufferInfo);
  }
  
  public void syncVideoEncodecUpdated(MediaCodec.BufferInfo paramBufferInfo)
  {
    if (paramBufferInfo == null) {
      return;
    }
    unlockVideoTimestampUs(paramBufferInfo.presentationTimeUs);
    if (TLog.LOG_VIDEO_ENCODEC_INFO) {
      TuSdkCodecCapabilities.logBufferInfo(String.format("%s syncVideoEncodecUpdated", new Object[] { "TuSdkVideoEncodecSyncBase" }), paramBufferInfo);
    }
  }
  
  public void syncVideoEncodecCompleted()
  {
    if (TLog.LOG_VIDEO_ENCODEC_INFO) {
      TLog.d("%s syncVideoEncodecCompleted", new Object[] { "TuSdkVideoEncodecSyncBase" });
    }
    this.b = true;
  }
  
  protected long calculateEncodeTimestampUs(int paramInt, long paramLong)
  {
    if (paramInt < 1) {
      return 0L;
    }
    long l = paramLong * 1000000L / paramInt;
    return l;
  }
  
  protected abstract boolean isLastDecodeFrame(long paramLong);
  
  protected long getInputIntervalUs()
  {
    return this.mFrameIntervalUs;
  }
  
  protected boolean needSkip(long paramLong)
  {
    return false;
  }
  
  public void syncVideoEncodecDrawFrame(long paramLong, boolean paramBoolean, TuSdkRecordSurface paramTuSdkRecordSurface, TuSdkEncodeSurface paramTuSdkEncodeSurface)
  {
    if ((paramTuSdkRecordSurface == null) || (paramTuSdkEncodeSurface == null) || (this.mReleased)) {
      return;
    }
    paramTuSdkRecordSurface.updateSurfaceTexImage();
    if (paramBoolean)
    {
      clearLocker();
      return;
    }
    long l1 = paramLong / 1000L;
    if (needSkip(l1))
    {
      unlockVideoTimestampUs(l1);
      this.mPreviousTimeUs = -1L;
      this.mFrameIntervalUs = 0L;
      return;
    }
    if (this.mPreviousTimeUs < 0L) {
      this.mPreviousTimeUs = l1;
    }
    this.mFrameIntervalUs = (l1 - this.mPreviousTimeUs);
    this.mPreviousTimeUs = l1;
    long l2 = calculateEncodeTimestampUs(this.mFrameRates, this.mFrameCounts);
    if (l2 < 1L)
    {
      renderToEncodec(l2, l1, paramTuSdkRecordSurface, paramTuSdkEncodeSurface);
      return;
    }
    for (long l3 = l2 * 1000L; l2 < l1; l3 = l2 * 1000L)
    {
      lockVideoTimestampUs(l2);
      this.mLastTimeUs = l2;
      this.mFrameCounts += 1L;
      paramTuSdkEncodeSurface.duplicateFrameReadyInGLThread(l3);
      paramTuSdkEncodeSurface.swapBuffers(l3);
      l2 = calculateEncodeTimestampUs(this.mFrameRates, this.mFrameCounts);
    }
    if (isLastDecodeFrame(l1))
    {
      renderToEncodec(l2, l1, paramTuSdkRecordSurface, paramTuSdkEncodeSurface);
      return;
    }
    if ((l2 > l1) && (getInputIntervalUs() > 0L))
    {
      long l4 = l1 + getInputIntervalUs();
      if (l2 > l4)
      {
        unlockVideoTimestampUs(l1);
        return;
      }
    }
    renderToEncodec(l2, l1, paramTuSdkRecordSurface, paramTuSdkEncodeSurface);
  }
  
  protected void renderToEncodec(long paramLong1, long paramLong2, TuSdkRecordSurface paramTuSdkRecordSurface, TuSdkEncodeSurface paramTuSdkEncodeSurface)
  {
    if (paramLong1 != paramLong2)
    {
      lockVideoTimestampUs(paramLong1);
      unlockVideoTimestampUs(paramLong2);
    }
    long l = paramLong1 * 1000L;
    paramTuSdkRecordSurface.newFrameReadyInGLThread(l);
    paramTuSdkEncodeSurface.newFrameReadyInGLThread(l);
    this.mLastTimeUs = paramLong1;
    this.mFrameCounts += 1L;
    paramTuSdkEncodeSurface.swapBuffers(l);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\sync\TuSdkVideoEncodecSyncBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */