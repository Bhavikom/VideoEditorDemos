package org.lasque.tusdk.core.decoder;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.delegate.TuSDKAudioPacketDelegate;
import org.lasque.tusdk.core.delegate.TuSDKVideoSurfaceDecodeDelegate;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

@TargetApi(21)
public class TuSDKMovieSurfaceDecoder
  extends TuSDKMediaDecoder
{
  public static final int TIMEOUT_USEC = 500;
  public static final int INVALID_SEEKTIME_FLAG = -1;
  private TuSDKVideoInfo c;
  private TuSdkTimeRange d;
  private long e;
  private boolean f = false;
  private volatile boolean g;
  private boolean h;
  private EventHandler i = new EventHandler();
  private MovieDecoderThread j;
  private TuSDKVideoSurfaceDecodeDelegate k;
  private TuSDKAudioPacketDelegate l;
  private long m = -1L;
  private Surface n;
  int a = 0;
  long b = 0L;
  
  public TuSDKMovieSurfaceDecoder(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    super(paramTuSDKMediaDataSource);
  }
  
  public TuSDKVideoInfo getVideoInfo()
  {
    return this.c;
  }
  
  public MediaCodec getVideoDecoder()
  {
    return this.mVideoDecoder;
  }
  
  public MediaCodec getAudioDecoder()
  {
    return null;
  }
  
  public TuSDKVideoSurfaceDecodeDelegate getVideoDelegte()
  {
    return this.k;
  }
  
  public void setVideoDelegate(TuSDKVideoSurfaceDecodeDelegate paramTuSDKVideoSurfaceDecodeDelegate)
  {
    this.k = paramTuSDKVideoSurfaceDecodeDelegate;
  }
  
  public void setAudioPacketDelegate(TuSDKAudioPacketDelegate paramTuSDKAudioPacketDelegate)
  {
    this.l = paramTuSDKAudioPacketDelegate;
  }
  
  public long getVideoDurationTimeUS()
  {
    if (this.c == null) {
      return 0L;
    }
    if ((getTimeRange() != null) && (getTimeRange().isValid())) {
      return Math.min(this.c.durationTimeUs, getTimeRange().durationTimeUS());
    }
    return this.c.durationTimeUs;
  }
  
  public long getVideoDuration()
  {
    return getVideoDurationTimeUS() / 1000000L;
  }
  
  public TuSdkTimeRange getTimeRange()
  {
    return this.d;
  }
  
  public void seekTimeUs(long paramLong)
  {
    this.m = paramLong;
    this.e = 0L;
    this.i.removeMessages(6);
    this.i.sendEmptyMessage(6);
  }
  
  private void a()
  {
    if ((this.g) && (this.m != -1L)) {
      i();
    }
  }
  
  public long getCurrentSampleTimeUs()
  {
    if (this.m != -1L) {
      return this.m;
    }
    if (b()) {
      return Math.max(getTimeRange().getStartTimeUS(), this.e);
    }
    return this.e;
  }
  
  private long a(int paramInt)
  {
    long l1 = 1000000L;
    return paramInt * 1000000L / this.c.fps;
  }
  
  public long getComputePresentationTimeUs()
  {
    return this.b;
  }
  
  private boolean b()
  {
    return (getTimeRange() != null) && (getTimeRange().isValid());
  }
  
  public TuSdkSize getVideoSize()
  {
    if (this.c != null) {
      return TuSdkSize.create(this.c.width, this.c.height);
    }
    return TuSdkSize.create(0);
  }
  
  public void setLooping(boolean paramBoolean)
  {
    this.f = paramBoolean;
  }
  
  public void prepare(Surface paramSurface, TuSdkTimeRange paramTuSdkTimeRange, boolean paramBoolean)
  {
    this.d = paramTuSdkTimeRange;
    this.h = paramBoolean;
    this.n = paramSurface;
  }
  
  public TuSDKMovieReader createMovieReader()
  {
    if (this.mDataSource == null)
    {
      TLog.e("Please set the data source", new Object[0]);
      onDecoderError(TuSDKMediaDecoder.TuSDKMediaDecoderError.InvalidDataSource);
      return null;
    }
    if (!this.mDataSource.isValid())
    {
      TLog.e("Unable to read media file: %s", new Object[] { this.mDataSource.getFilePath() });
      onDecoderError(TuSDKMediaDecoder.TuSDKMediaDecoderError.InvalidDataSource);
      return null;
    }
    return new TuSDKMovieReader(this.mDataSource);
  }
  
  protected void onDecoderError(TuSDKMediaDecoder.TuSDKMediaDecoderError paramTuSDKMediaDecoderError)
  {
    super.onDecoderError(paramTuSDKMediaDecoderError);
    this.g = false;
    if (this.k != null) {
      this.k.onDecoderError(paramTuSDKMediaDecoderError);
    }
  }
  
  protected void onDecoderComplete()
  {
    this.m = -1L;
    this.e = 0L;
    this.a = 0;
    if (this.k != null) {
      this.k.onDecoderComplete();
    }
  }
  
  public void start()
  {
    this.i.removeMessages(2);
    this.i.sendEmptyMessage(2);
  }
  
  private void c()
  {
    if (this.g) {
      return;
    }
    this.mMovieReader = createMovieReader();
    if (this.mMovieReader == null) {
      return;
    }
    this.mMovieReader.setTimeRange(getTimeRange());
    this.c = this.mMovieReader.getVideoInfo();
    if ((this.c != null) && (TuSdkSize.create(this.c.width, this.c.height).maxSide() >= 3500))
    {
      onDecoderError(TuSDKMediaDecoder.TuSDKMediaDecoderError.UnsupportedVideoFormat);
      return;
    }
    if (getVideoDelegte() != null) {
      getVideoDelegte().onVideoInfoReady(this.c);
    }
    if ((this.h) && (findAudioTrack() != -1) && (this.l != null))
    {
      MediaFormat localMediaFormat = getAudioTrackFormat();
      this.l.onAudioInfoReady(localMediaFormat);
    }
    this.mVideoDecoder = createVideoDecoder(this.n);
    if (this.mVideoDecoder == null) {
      return;
    }
    this.g = true;
    this.a = 0;
    this.mVideoDecoder.start();
    this.j = new MovieDecoderThread(null);
    this.j.start();
  }
  
  public void pause()
  {
    this.i.removeMessages(2);
    this.i.sendEmptyMessage(3);
  }
  
  private void d()
  {
    this.m = this.e;
    e();
  }
  
  public void stop()
  {
    this.i.removeMessages(2);
    this.i.sendEmptyMessage(4);
  }
  
  private void e()
  {
    if (!this.g) {
      return;
    }
    this.g = false;
    this.e = 0L;
    this.a = 0;
    if (this.j != null)
    {
      this.j.interrupt();
      try
      {
        this.j.join();
        this.j = null;
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.printStackTrace();
      }
    }
    super.stop();
  }
  
  public void destroy()
  {
    this.i.removeCallbacksAndMessages(null);
    this.i.sendEmptyMessage(5);
  }
  
  private void f()
  {
    e();
    destroyMediaReader();
    this.k = null;
    this.c = null;
  }
  
  private void g()
  {
    if (getVideoDelegte() == null) {
      return;
    }
    float f1 = getProgress();
    if (b()) {
      getVideoDelegte().onProgressChanged(getCurrentSampleTimeUs(), f1);
    } else {
      getVideoDelegte().onProgressChanged(getCurrentSampleTimeUs(), f1);
    }
  }
  
  public float getProgress()
  {
    float f1 = (float)getCurrentSampleTimeUs() / (float)getVideoDurationTimeUS();
    if (b()) {
      f1 = (float)(getCurrentSampleTimeUs() - getTimeRange().getStartTimeUS()) / (float)getVideoDurationTimeUS();
    }
    return f1;
  }
  
  private boolean a(long paramLong)
  {
    ByteBuffer[] arrayOfByteBuffer = this.mVideoDecoder.getInputBuffers();
    int i1 = this.mVideoDecoder.dequeueInputBuffer(500L);
    long l1 = 0L;
    if (i1 >= 0) {
      do
      {
        this.b = a(this.a++);
        ByteBuffer localByteBuffer = arrayOfByteBuffer[i1];
        int i2 = this.mMovieReader.readSampleData(localByteBuffer, 0);
        if (i2 < 0)
        {
          this.mVideoDecoder.queueInputBuffer(i1, 0, 0, this.mMovieReader.getSampleTime(), 4);
          return true;
        }
        l1 = this.mMovieReader.getSampleTime();
        if (l1 > this.e)
        {
          this.e = l1;
          g();
        }
        if (!this.mMovieReader.isVideoSampleTrackIndex()) {
          TLog.w("WEIRD: got sample from track " + this.mMovieReader.getSampleTrackIndex() + ", expected " + this.mMovieReader.findVideoTrack(), new Object[0]);
        }
        if (l1 >= paramLong) {
          this.mVideoDecoder.queueInputBuffer(i1, 0, i2, l1, 0);
        }
      } while (l1 < paramLong);
    }
    return false;
  }
  
  @TargetApi(21)
  private boolean h()
  {
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    int i1 = this.mVideoDecoder.dequeueOutputBuffer(localBufferInfo, 500L);
    switch (i1)
    {
    case -2: 
      this.c.syncCodecCrop(this.mVideoDecoder.getOutputFormat());
      if (this.k != null) {
        this.k.onVideoInfoReady(this.c);
      }
      break;
    }
    if (i1 >= 0)
    {
      if ((localBufferInfo.flags & 0x4) != 0)
      {
        if ((!this.h) && (getVideoDelegte() != null)) {
          onDecoderComplete();
        }
        return true;
      }
      boolean bool = localBufferInfo.size != 0;
      this.mVideoDecoder.releaseOutputBuffer(i1, bool);
      this.k.onVideoDecoderNewFrameAvailable(i1, localBufferInfo);
      if ((b()) && (getCurrentSampleTimeUs() >= getTimeRange().getEndTimeUS()))
      {
        unselectVideoTrack();
        if ((!this.h) && (getVideoDelegte() != null)) {
          onDecoderComplete();
        }
        return true;
      }
    }
    return false;
  }
  
  private long i()
  {
    if (this.m != -1L)
    {
      long l1 = this.m;
      seekTo(l1 - 5000000L, 0);
      return l1;
    }
    if ((getTimeRange() != null) && (getTimeRange().isValid()))
    {
      seekTo(getTimeRange().getStartTimeUS() - 5000000L, 0);
      return getTimeRange().getStartTimeUS();
    }
    return 0L;
  }
  
  private void j()
  {
    if (!this.g) {
      return;
    }
    selectVideoTrack();
    long l1 = Math.max(0L, i());
    boolean bool1 = false;
    boolean bool2 = false;
    while (!bool1)
    {
      if (!this.g) {
        return;
      }
      if (!bool2) {
        bool2 = a(l1);
      }
      if (!bool1) {
        bool1 = h();
      }
    }
  }
  
  public void seekTo(long paramLong, int paramInt)
  {
    super.seekTo(paramLong, paramInt);
    this.m = -1L;
  }
  
  private void k()
  {
    if (!this.g) {
      return;
    }
    selectAudioTrack();
    int i1 = 262144;
    i();
    for (;;)
    {
      if (!this.g) {
        return;
      }
      ByteBuffer localByteBuffer = ByteBuffer.allocate(262144);
      int i2 = this.mMovieReader.readSampleData(localByteBuffer, 0);
      if (i2 <= 0)
      {
        unselectAudioTrack();
        if (getVideoDelegte() == null) {
          break;
        }
        getVideoDelegte().onDecoderComplete();
        break;
      }
      long l1 = this.mMovieReader.getSampleTime();
      if ((b()) && (l1 >= getTimeRange().getEndTimeUS()))
      {
        unselectAudioTrack();
        if (getVideoDelegte() == null) {
          break;
        }
        getVideoDelegte().onDecoderComplete();
        break;
      }
      MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
      localBufferInfo.offset = 0;
      localBufferInfo.size = i2;
      localBufferInfo.presentationTimeUs = l1;
      if (this.l != null) {
        this.l.onAudioPacketAvailable(l1, localByteBuffer, localBufferInfo);
      }
    }
  }
  
  private class MovieDecoderThread
    extends Thread
  {
    private MovieDecoderThread() {}
    
    public void run()
    {
      try
      {
        while (TuSDKMovieSurfaceDecoder.f(TuSDKMovieSurfaceDecoder.this))
        {
          TuSDKMovieSurfaceDecoder.g(TuSDKMovieSurfaceDecoder.this);
          TuSDKMovieSurfaceDecoder.this.mVideoDecoder.flush();
          if (!TuSDKMovieSurfaceDecoder.h(TuSDKMovieSurfaceDecoder.this)) {
            break;
          }
        }
        if ((TuSDKMovieSurfaceDecoder.i(TuSDKMovieSurfaceDecoder.this)) && (TuSDKMovieSurfaceDecoder.f(TuSDKMovieSurfaceDecoder.this))) {
          TuSDKMovieSurfaceDecoder.j(TuSDKMovieSurfaceDecoder.this);
        }
      }
      catch (Throwable localThrowable)
      {
        TLog.e("The video cannot be decoded", new Object[0]);
        TuSDKMovieSurfaceDecoder.this.onDecoderError(TuSDKMediaDecoder.TuSDKMediaDecoderError.UnsupportedVideoFormat);
      }
    }
  }
  
  private class EventHandler
    extends Handler
  {
    EventHandler()
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      case 2: 
        TuSDKMovieSurfaceDecoder.a(TuSDKMovieSurfaceDecoder.this);
        break;
      case 3: 
        TuSDKMovieSurfaceDecoder.b(TuSDKMovieSurfaceDecoder.this);
        break;
      case 4: 
        TuSDKMovieSurfaceDecoder.c(TuSDKMovieSurfaceDecoder.this);
        break;
      case 5: 
        TuSDKMovieSurfaceDecoder.d(TuSDKMovieSurfaceDecoder.this);
        break;
      case 6: 
        TuSDKMovieSurfaceDecoder.e(TuSDKMovieSurfaceDecoder.this);
        break;
      }
    }
  }
  
  public static enum TuSDKMovieDecoderError
  {
    private TuSDKMovieDecoderError() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKMovieSurfaceDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */