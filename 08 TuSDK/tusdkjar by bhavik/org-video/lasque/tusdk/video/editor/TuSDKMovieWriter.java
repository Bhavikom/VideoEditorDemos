package org.lasque.tusdk.video.editor;

import android.annotation.SuppressLint;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.utils.TLog;

@SuppressLint({"InlinedApi"})
public class TuSDKMovieWriter
  implements TuSDKMovieWriterInterface
{
  public static final int INVALID_TRACK_INDEX = -1;
  private volatile State a = State.UnKnow;
  private MediaMuxer b;
  private int c = -1;
  private int d = -1;
  private String e;
  private TuSDKMovieWriterInterface.MovieWriterOutputFormat f;
  private boolean g = false;
  private TuSDKMovieWriterDelegate h;
  protected boolean mIsFirstAudioWrite = true;
  private boolean i = false;
  private long j = 0L;
  private long k = 0L;
  private long l = 0L;
  private long m = 0L;
  public static final byte[] AAC_MUTE_BYTES = { 33, 33, 69, 0, 20, 80, 1, 70, -1, -31, 10, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 90, 94 };
  
  public TuSDKMovieWriter(String paramString, TuSDKMovieWriterInterface.MovieWriterOutputFormat paramMovieWriterOutputFormat)
  {
    try
    {
      this.b = new MediaMuxer(paramString, paramMovieWriterOutputFormat.getOutputFormat());
      this.e = paramString;
      this.f = paramMovieWriterOutputFormat;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }
  
  public static TuSDKMovieWriter create(String paramString, TuSDKMovieWriterInterface.MovieWriterOutputFormat paramMovieWriterOutputFormat)
  {
    return new TuSDKMovieWriter(paramString, paramMovieWriterOutputFormat);
  }
  
  public void setDelegate(TuSDKMovieWriterDelegate paramTuSDKMovieWriterDelegate)
  {
    this.h = paramTuSDKMovieWriterDelegate;
  }
  
  public void setOrientationHint(int paramInt)
  {
    if ((this.b == null) || (this.a == State.Started)) {
      return;
    }
    if ((paramInt != 0) && (paramInt != 90) && (paramInt != 180) && (paramInt != 270))
    {
      TLog.e("Unsupported angle: " + paramInt, new Object[0]);
      return;
    }
    this.b.setOrientationHint(paramInt);
  }
  
  public boolean start()
  {
    if ((this.b == null) || (isStarted())) {
      return false;
    }
    this.b.start();
    this.a = State.Started;
    this.i = false;
    return true;
  }
  
  public boolean stop()
  {
    if ((this.b == null) || (!isStarted()) || (!this.i)) {
      return false;
    }
    this.a = State.Stopped;
    this.b.stop();
    this.b.release();
    this.b = null;
    this.c = -1;
    this.d = -1;
    this.i = false;
    return true;
  }
  
  public boolean isStarted()
  {
    return this.a == State.Started;
  }
  
  public boolean isStoped()
  {
    return this.a == State.Stopped;
  }
  
  public TuSDKMovieWriterInterface.MovieWriterOutputFormat getOutputFormat()
  {
    return this.f;
  }
  
  public String getOutputFilePath()
  {
    return this.e;
  }
  
  public long getDurationTime()
  {
    return Math.max(this.m - this.l / 1000L / 1000L, 0L);
  }
  
  public void writeSampleData(int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if ((this.b == null) || (this.a != State.Started)) {
      return;
    }
    TuSDKMovieWriterInterface.ByteBufferData localByteBufferData = new TuSDKMovieWriterInterface.ByteBufferData();
    localByteBufferData.trackIndex = paramInt;
    localByteBufferData.buffer = paramByteBuffer;
    localByteBufferData.bufferInfo = paramBufferInfo;
    writeSampleData(localByteBufferData);
  }
  
  public void setWriteMuteAudioPlaceholderData(boolean paramBoolean)
  {
    this.g = paramBoolean;
  }
  
  public void writeSampleData(TuSDKMovieWriterInterface.ByteBufferData paramByteBufferData)
  {
    if ((this.b == null) || (this.a != State.Started)) {
      return;
    }
    if ((a()) && (paramByteBufferData.trackIndex == this.d)) {
      a(paramByteBufferData);
    } else if (paramByteBufferData.trackIndex == this.c) {
      b(paramByteBufferData);
    }
  }
  
  public void writeAudioSampleData(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (hasAudioTrack()) {
      writeSampleData(this.d, paramByteBuffer, paramBufferInfo);
    }
  }
  
  public void writeVideoSampleData(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (hasVideoTrack()) {
      writeSampleData(this.c, paramByteBuffer, paramBufferInfo);
    }
  }
  
  public boolean hasVideoTrack()
  {
    return this.c != -1;
  }
  
  public boolean hasAudioTrack()
  {
    return this.d != -1;
  }
  
  private int a(MediaFormat paramMediaFormat)
  {
    if (this.b == null) {
      return -1;
    }
    return this.b.addTrack(paramMediaFormat);
  }
  
  public boolean canAddVideoTrack()
  {
    return (!hasVideoTrack()) && (!isStarted()) && (this.b != null);
  }
  
  public int addVideoTrack(MediaFormat paramMediaFormat)
  {
    if (this.b == null) {
      return -1;
    }
    if (this.c != -1) {
      throw new IllegalArgumentException("The video track already exists");
    }
    int n = a(paramMediaFormat);
    this.c = n;
    return n;
  }
  
  public boolean canAddAudioTrack()
  {
    return (!hasAudioTrack()) && (!isStarted()) && (this.b != null);
  }
  
  public int addAudioTrack(MediaFormat paramMediaFormat)
  {
    if (this.b == null) {
      return -1;
    }
    if (this.d != -1) {
      throw new IllegalArgumentException("The audio track already exists");
    }
    int n = a(paramMediaFormat);
    this.d = n;
    return n;
  }
  
  public long getLastAudioPresentationTimeUs()
  {
    return this.j > 0L ? this.j : this.m;
  }
  
  public long getBeginVideoPresentationTimeUs()
  {
    return this.l;
  }
  
  public long getLastVideoPresentationTimeUs()
  {
    return this.m;
  }
  
  public void setAudioStartTimeUs(long paramLong)
  {
    this.mIsFirstAudioWrite = false;
    this.j = paramLong;
    this.k = 0L;
  }
  
  private void a(long paramLong)
  {
    if (this.d == -1) {
      return;
    }
    ByteBuffer localByteBuffer = ByteBuffer.wrap(AAC_MUTE_BYTES);
    TuSDKMovieWriterInterface.ByteBufferData localByteBufferData1 = new TuSDKMovieWriterInterface.ByteBufferData();
    localByteBufferData1.trackIndex = this.d;
    localByteBufferData1.buffer = localByteBuffer;
    localByteBufferData1.bufferInfo = new MediaCodec.BufferInfo();
    localByteBufferData1.bufferInfo.presentationTimeUs = paramLong;
    localByteBufferData1.bufferInfo.size = AAC_MUTE_BYTES.length;
    localByteBufferData1.bufferInfo.offset = 0;
    a(localByteBufferData1);
    TuSDKMovieWriterInterface.ByteBufferData localByteBufferData2 = new TuSDKMovieWriterInterface.ByteBufferData();
    localByteBufferData2.trackIndex = this.d;
    localByteBufferData2.buffer = localByteBuffer;
    localByteBufferData2.bufferInfo = new MediaCodec.BufferInfo();
    localByteBufferData2.bufferInfo.presentationTimeUs = (getLastAudioPresentationTimeUs() + 100L);
    localByteBufferData2.bufferInfo.size = AAC_MUTE_BYTES.length;
    localByteBufferData2.bufferInfo.offset = 0;
    a(localByteBufferData2);
  }
  
  private void a(TuSDKMovieWriterInterface.ByteBufferData paramByteBufferData)
  {
    if (this.mIsFirstAudioWrite)
    {
      this.mIsFirstAudioWrite = false;
      this.j = this.l;
      this.k = (this.l - paramByteBufferData.bufferInfo.presentationTimeUs);
    }
    paramByteBufferData.bufferInfo.presentationTimeUs += this.k;
    if (paramByteBufferData.bufferInfo.presentationTimeUs < this.j) {
      paramByteBufferData.bufferInfo.presentationTimeUs = TuSDKMediaUtils.getSafePts(this.j, paramByteBufferData.bufferInfo.presentationTimeUs);
    }
    this.j = paramByteBufferData.bufferInfo.presentationTimeUs;
    this.b.writeSampleData(paramByteBufferData.trackIndex, paramByteBufferData.buffer, paramByteBufferData.bufferInfo);
    this.i = true;
  }
  
  private void b(TuSDKMovieWriterInterface.ByteBufferData paramByteBufferData)
  {
    paramByteBufferData.bufferInfo.presentationTimeUs = TuSDKMediaUtils.getSafePts(this.m, paramByteBufferData.bufferInfo.presentationTimeUs);
    this.m = paramByteBufferData.bufferInfo.presentationTimeUs;
    if (this.l <= 0L) {
      this.l = this.m;
    }
    if (this.g) {
      a(this.m);
    }
    if (this.h != null)
    {
      float f1 = (float)this.m;
      float f2 = (float)this.l;
      if (this.l == this.m) {
        this.h.onFirstVideoSampleDataWrited(this.l);
      }
      this.h.onProgressChanged((f1 - f2) / 1000.0F / 1000.0F, this.m);
    }
    this.b.writeSampleData(paramByteBufferData.trackIndex, paramByteBufferData.buffer, paramByteBufferData.bufferInfo);
    this.i = true;
  }
  
  private boolean a()
  {
    return (!hasVideoTrack()) || ((hasAudioTrack()) && (this.l > 0L));
  }
  
  public static enum State
  {
    private State() {}
  }
  
  public static abstract interface TuSDKMovieWriterDelegate
  {
    public abstract void onFirstVideoSampleDataWrited(long paramLong);
    
    public abstract void onProgressChanged(float paramFloat, long paramLong);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSDKMovieWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */