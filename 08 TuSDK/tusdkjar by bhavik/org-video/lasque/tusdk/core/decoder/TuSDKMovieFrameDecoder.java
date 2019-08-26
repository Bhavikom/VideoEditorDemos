package org.lasque.tusdk.core.decoder;

import android.annotation.TargetApi;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.media.Image;
import android.media.Image.Plane;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.Message;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.delegate.TuSDKAudioPacketDelegate;
import org.lasque.tusdk.core.delegate.TuSDKVideoFrameDecodeDelegate;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

@TargetApi(21)
public class TuSDKMovieFrameDecoder
  extends TuSDKMediaDecoder
{
  public static final int COLOR_FormatI420 = 1;
  public static final int COLOR_FormatNV21 = 2;
  public static final int TIMEOUT_USEC = 500;
  public static final int INVALID_SEEKTIME_FLAG = -1;
  private TuSDKVideoInfo a;
  private TuSdkTimeRange b;
  private long c;
  private boolean d = false;
  private volatile boolean e;
  private boolean f;
  private EventHandler g = new EventHandler(null);
  private MovieDecoderThread h;
  private TuSDKVideoFrameDecodeDelegate i;
  private TuSDKAudioPacketDelegate j;
  private long k = -1L;
  
  public TuSDKMovieFrameDecoder(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    super(paramTuSDKMediaDataSource);
  }
  
  public TuSDKVideoInfo getVideoInfo()
  {
    return this.a;
  }
  
  public MediaCodec getVideoDecoder()
  {
    return this.mVideoDecoder;
  }
  
  public MediaCodec getAudioDecoder()
  {
    return null;
  }
  
  public TuSDKVideoFrameDecodeDelegate getVideoDelegate()
  {
    return this.i;
  }
  
  public void setVideoDelegate(TuSDKVideoFrameDecodeDelegate paramTuSDKVideoFrameDecodeDelegate)
  {
    this.i = paramTuSDKVideoFrameDecodeDelegate;
  }
  
  public void setAudioPacketDelegate(TuSDKAudioPacketDelegate paramTuSDKAudioPacketDelegate)
  {
    this.j = paramTuSDKAudioPacketDelegate;
  }
  
  public long getVideoDurationTimeUS()
  {
    if (this.a == null) {
      return 0L;
    }
    return this.a.durationTimeUs;
  }
  
  public long getVideoDuration()
  {
    return getVideoDurationTimeUS() / 1000000L;
  }
  
  public TuSdkTimeRange getTimeRange()
  {
    return this.b;
  }
  
  public void seekTimeUs(long paramLong)
  {
    this.k = paramLong;
    this.c = 0L;
    this.g.removeMessages(6);
    this.g.sendEmptyMessage(6);
  }
  
  private void a()
  {
    if ((this.e) && (this.k != -1L)) {
      i();
    }
  }
  
  public long getCurrentSampleTimeUs()
  {
    if (this.k != -1L) {
      return this.k;
    }
    if (b()) {
      return Math.max(getTimeRange().getStartTimeUS(), this.c);
    }
    return this.c;
  }
  
  private boolean b()
  {
    return (getTimeRange() != null) && (getTimeRange().isValid());
  }
  
  public TuSdkSize getVideoSize()
  {
    if (this.a != null) {
      return TuSdkSize.create(this.a.width, this.a.height);
    }
    return TuSdkSize.create(0);
  }
  
  public void setLooping(boolean paramBoolean)
  {
    this.d = paramBoolean;
  }
  
  public void prepare(TuSdkTimeRange paramTuSdkTimeRange, boolean paramBoolean)
  {
    this.b = paramTuSdkTimeRange;
    this.f = paramBoolean;
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
    if (this.i != null) {
      this.i.onDecoderError(paramTuSDKMediaDecoderError);
    }
  }
  
  protected void onDecoderComplete()
  {
    this.k = -1L;
    this.c = 0L;
    if (this.i != null) {
      this.i.onDecoderComplete();
    }
  }
  
  public void start()
  {
    this.g.removeMessages(2);
    this.g.sendEmptyMessage(2);
  }
  
  private void c()
  {
    if (this.e) {
      return;
    }
    this.mMovieReader = createMovieReader();
    if (this.mMovieReader == null) {
      return;
    }
    this.mMovieReader.setTimeRange(getTimeRange());
    this.a = this.mMovieReader.getVideoInfo();
    if (getVideoDelegate() != null) {
      getVideoDelegate().onVideoInfoReady(this.a);
    }
    if ((this.f) && (findAudioTrack() != -1) && (this.j != null))
    {
      MediaFormat localMediaFormat = getAudioTrackFormat();
      this.j.onAudioInfoReady(localMediaFormat);
    }
    this.mVideoDecoder = createVideoDecoder(null);
    if (this.mVideoDecoder == null) {
      return;
    }
    this.e = true;
    this.mVideoDecoder.start();
    this.h = new MovieDecoderThread(null);
    this.h.start();
  }
  
  public void pause()
  {
    this.g.removeMessages(2);
    this.g.sendEmptyMessage(3);
  }
  
  private void d()
  {
    this.k = this.c;
    e();
  }
  
  public void stop()
  {
    this.g.removeMessages(2);
    this.g.sendEmptyMessage(4);
  }
  
  private void e()
  {
    if (!this.e) {
      return;
    }
    this.e = false;
    this.c = 0L;
    if (this.h != null)
    {
      this.h.interrupt();
      try
      {
        this.h.join();
        this.h = null;
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
    this.g.removeCallbacksAndMessages(null);
    this.g.sendEmptyMessage(5);
  }
  
  private void f()
  {
    e();
    destroyMediaReader();
    this.i = null;
    this.a = null;
  }
  
  private void g()
  {
    if (getVideoDelegate() == null) {
      return;
    }
    float f1 = getProgress();
    if (b()) {
      getVideoDelegate().onProgressChanged(getCurrentSampleTimeUs(), (float)((getCurrentSampleTimeUs() - getTimeRange().getStartTimeUS()) / 1000000L), f1);
    } else {
      getVideoDelegate().onProgressChanged(getCurrentSampleTimeUs(), (float)(getCurrentSampleTimeUs() / 1000000L), f1);
    }
  }
  
  public float getProgress()
  {
    float f1 = (float)getCurrentSampleTimeUs() / (float)getVideoDurationTimeUS();
    if (b()) {
      f1 = (float)((getCurrentSampleTimeUs() - getTimeRange().getStartTimeUS()) / getTimeRange().durationTimeUS());
    }
    return f1;
  }
  
  private boolean a(long paramLong)
  {
    ByteBuffer[] arrayOfByteBuffer = this.mVideoDecoder.getInputBuffers();
    int m = this.mVideoDecoder.dequeueInputBuffer(500L);
    long l = 0L;
    if (m >= 0) {
      do
      {
        ByteBuffer localByteBuffer = arrayOfByteBuffer[m];
        int n = this.mMovieReader.readSampleData(localByteBuffer, 0);
        if (n < 0)
        {
          this.mVideoDecoder.queueInputBuffer(m, 0, 0, this.mMovieReader.getSampleTime(), 4);
          return true;
        }
        l = this.mMovieReader.getSampleTime();
        this.c = l;
        if (!this.mMovieReader.isVideoSampleTrackIndex()) {
          TLog.w("WEIRD: got sample from track " + this.mMovieReader.getSampleTrackIndex() + ", expected " + this.mMovieReader.findVideoTrack(), new Object[0]);
        }
        if (l >= paramLong) {
          this.mVideoDecoder.queueInputBuffer(m, 0, n, l, 0);
        }
      } while (l < paramLong);
    }
    return false;
  }
  
  @TargetApi(21)
  private boolean h()
  {
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    int m = this.mVideoDecoder.dequeueOutputBuffer(localBufferInfo, 500L);
    if (m >= 0)
    {
      if ((localBufferInfo.flags & 0x4) != 0)
      {
        if ((!this.f) && (getVideoDelegate() != null)) {
          onDecoderComplete();
        }
        return true;
      }
      boolean bool = localBufferInfo.size != 0;
      g();
      onVideoDecoderNewFrameAvailable(m, localBufferInfo);
      this.mVideoDecoder.releaseOutputBuffer(m, bool);
      if ((b()) && (getCurrentSampleTimeUs() >= getTimeRange().getEndTimeUS()))
      {
        unselectVideoTrack();
        if ((!this.f) && (getVideoDelegate() != null)) {
          onDecoderComplete();
        }
        return true;
      }
    }
    return false;
  }
  
  private long i()
  {
    if (this.k != -1L)
    {
      long l = this.k;
      seekTo(l - 5000000L, 0);
      return l;
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
    if (!this.e) {
      return;
    }
    selectVideoTrack();
    long l = Math.max(0L, i());
    boolean bool1 = false;
    boolean bool2 = false;
    while (!bool1)
    {
      if (!this.e) {
        return;
      }
      if (!bool2) {
        bool2 = a(l);
      }
      if (!bool1) {
        bool1 = h();
      }
    }
  }
  
  public void seekTo(long paramLong, int paramInt)
  {
    super.seekTo(paramLong, paramInt);
    this.k = -1L;
  }
  
  private void k()
  {
    if (!this.e) {
      return;
    }
    selectAudioTrack();
    int m = 262144;
    i();
    for (;;)
    {
      if (!this.e) {
        return;
      }
      ByteBuffer localByteBuffer = ByteBuffer.allocate(262144);
      int n = this.mMovieReader.readSampleData(localByteBuffer, 0);
      if (n <= 0)
      {
        unselectAudioTrack();
        if (getVideoDelegate() == null) {
          break;
        }
        getVideoDelegate().onDecoderComplete();
        break;
      }
      long l = this.mMovieReader.getSampleTime();
      if ((b()) && (l >= getTimeRange().getEndTimeUS()))
      {
        unselectAudioTrack();
        if (getVideoDelegate() == null) {
          break;
        }
        getVideoDelegate().onDecoderComplete();
        break;
      }
      MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
      localBufferInfo.offset = 0;
      localBufferInfo.size = n;
      localBufferInfo.presentationTimeUs = l;
      if (this.j != null) {
        this.j.onAudioPacketAvailable(l, localByteBuffer, localBufferInfo);
      }
    }
  }
  
  protected void onVideoDecoderNewFrameAvailable(int paramInt, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (this.i == null) {
      return;
    }
    byte[] arrayOfByte = a(this.mVideoDecoder.getOutputImage(paramInt), 2);
    this.i.onVideoDecoderNewFrameAvailable(arrayOfByte, paramBufferInfo);
  }
  
  @TargetApi(19)
  private static boolean a(Image paramImage)
  {
    int m = paramImage.getFormat();
    switch (m)
    {
    case 17: 
    case 35: 
    case 842094169: 
      return true;
    }
    return false;
  }
  
  @TargetApi(21)
  private byte[] a(Image paramImage, int paramInt)
  {
    if ((paramInt != 1) && (paramInt != 2)) {
      throw new IllegalArgumentException("only support COLOR_FormatI420 and COLOR_FormatNV21");
    }
    if (!a(paramImage)) {
      throw new RuntimeException("can't convert Image to byte array, format " + paramImage.getFormat());
    }
    Rect localRect = paramImage.getCropRect();
    int m = paramImage.getFormat();
    int n = localRect.width();
    int i1 = localRect.height();
    Image.Plane[] arrayOfPlane = paramImage.getPlanes();
    byte[] arrayOfByte1 = new byte[n * i1 * ImageFormat.getBitsPerPixel(m) / 8];
    byte[] arrayOfByte2 = new byte[arrayOfPlane[0].getRowStride()];
    int i2 = 0;
    int i3 = 1;
    for (int i4 = 0; i4 < arrayOfPlane.length; i4++)
    {
      switch (i4)
      {
      case 0: 
        i2 = 0;
        i3 = 1;
        break;
      case 1: 
        if (paramInt == 1)
        {
          i2 = n * i1;
          i3 = 1;
        }
        else if (paramInt == 2)
        {
          i2 = n * i1 + 1;
          i3 = 2;
        }
        break;
      case 2: 
        if (paramInt == 1)
        {
          i2 = (int)(n * i1 * 1.25D);
          i3 = 1;
        }
        else if (paramInt == 2)
        {
          i2 = n * i1;
          i3 = 2;
        }
        break;
      }
      ByteBuffer localByteBuffer = arrayOfPlane[i4].getBuffer();
      int i5 = arrayOfPlane[i4].getRowStride();
      int i6 = arrayOfPlane[i4].getPixelStride();
      int i7 = i4 == 0 ? 0 : 1;
      int i8 = n >> i7;
      int i9 = i1 >> i7;
      localByteBuffer.position(i5 * (localRect.top >> i7) + i6 * (localRect.left >> i7));
      for (int i10 = 0; i10 < i9; i10++)
      {
        int i11;
        if ((i6 == 1) && (i3 == 1))
        {
          i11 = i8;
          localByteBuffer.get(arrayOfByte1, i2, i11);
          i2 += i11;
        }
        else
        {
          i11 = (i8 - 1) * i6 + 1;
          localByteBuffer.get(arrayOfByte2, 0, i11);
          for (int i12 = 0; i12 < i8; i12++)
          {
            arrayOfByte1[i2] = arrayOfByte2[(i12 * i6)];
            i2 += i3;
          }
        }
        if (i10 < i9 - 1) {
          localByteBuffer.position(localByteBuffer.position() + i5 - i11);
        }
      }
    }
    return arrayOfByte1;
  }
  
  private class MovieDecoderThread
    extends Thread
  {
    private MovieDecoderThread() {}
    
    public void run()
    {
      while (TuSDKMovieFrameDecoder.f(TuSDKMovieFrameDecoder.this))
      {
        TuSDKMovieFrameDecoder.g(TuSDKMovieFrameDecoder.this);
        TuSDKMovieFrameDecoder.this.mVideoDecoder.flush();
        if (!TuSDKMovieFrameDecoder.h(TuSDKMovieFrameDecoder.this)) {
          break;
        }
      }
      if ((TuSDKMovieFrameDecoder.i(TuSDKMovieFrameDecoder.this)) && (TuSDKMovieFrameDecoder.f(TuSDKMovieFrameDecoder.this))) {
        TuSDKMovieFrameDecoder.j(TuSDKMovieFrameDecoder.this);
      }
    }
  }
  
  private class EventHandler
    extends Handler
  {
    private EventHandler() {}
    
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      case 2: 
        TuSDKMovieFrameDecoder.a(TuSDKMovieFrameDecoder.this);
        break;
      case 3: 
        TuSDKMovieFrameDecoder.b(TuSDKMovieFrameDecoder.this);
        break;
      case 4: 
        TuSDKMovieFrameDecoder.c(TuSDKMovieFrameDecoder.this);
        break;
      case 5: 
        TuSDKMovieFrameDecoder.d(TuSDKMovieFrameDecoder.this);
        break;
      case 6: 
        TuSDKMovieFrameDecoder.e(TuSDKMovieFrameDecoder.this);
        break;
      }
    }
  }
  
  public static enum TuSDKMovieDecoderError
  {
    private TuSDKMovieDecoderError() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKMovieFrameDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */