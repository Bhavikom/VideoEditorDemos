package org.lasque.tusdk.core.decoder;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.view.Surface;
import java.io.File;
import java.nio.ByteBuffer;
import org.lasque.tusdk.api.transcoder.TuSDKMovieTranscoder;
import org.lasque.tusdk.core.common.TuSDKAVPacket;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.common.TuSDKMediaUtils;
import org.lasque.tusdk.core.delegate.TuSDKVideoLoadDelegate;
import org.lasque.tusdk.core.delegate.TuSDKVideoSaveDelegate;
import org.lasque.tusdk.core.delegate.TuSDKVideoSurfaceDecodeDelegate;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.video.TuSDKVideoResult;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

@TargetApi(16)
public class TuSDKMoviePacketDecoder
  extends TuSDKMediaDecoder<TuSDKMoviePacketReader>
{
  public static final int TIMEOUT_USEC = 5000;
  public static final int INVALID_SEEKTIME_FLAG = -1;
  private Surface a;
  private TuSdkTimeRange b;
  private long c;
  private TuSDKVideoSpeedControl d;
  private boolean e = false;
  private TuSDKVideoInfo f;
  private TuSDKVideoInfo g;
  private volatile State h = State.Idle;
  private TuSDKVideoSurfaceDecodeDelegate i;
  private TuSDKVideoLoadDelegate j;
  private long k = -1L;
  private TuSDKMoviePacketReader.ReadMode l = TuSDKMoviePacketReader.ReadMode.SequenceMode;
  private TuSDKVideoTimeEffectController m = TuSDKVideoTimeEffectController.create(TuSDKVideoTimeEffectController.TimeEffectMode.NoMode);
  private boolean n = true;
  private String o;
  private boolean p = true;
  private TuSdkSize q;
  private boolean r = false;
  private TuSDKVideoSaveDelegate s = new TuSDKVideoSaveDelegate()
  {
    public void onProgressChaned(final float paramAnonymousFloat)
    {
      if (TuSDKMoviePacketDecoder.a(TuSDKMoviePacketDecoder.this) == null) {
        return;
      }
      ThreadHelper.post(new Runnable()
      {
        public void run()
        {
          TuSDKMoviePacketDecoder.a(TuSDKMoviePacketDecoder.this).onProgressChaned(paramAnonymousFloat);
        }
      });
    }
    
    public void onSaveResult(TuSDKVideoResult paramAnonymousTuSDKVideoResult)
    {
      if (paramAnonymousTuSDKVideoResult != null)
      {
        if (TuSDKMoviePacketDecoder.b(TuSDKMoviePacketDecoder.this) == TuSDKMoviePacketDecoder.State.Terminated)
        {
          new File(paramAnonymousTuSDKVideoResult.videoPath.getAbsolutePath()).delete();
          return;
        }
        TuSDKMoviePacketDecoder.a(TuSDKMoviePacketDecoder.this, paramAnonymousTuSDKVideoResult.videoPath.getAbsolutePath());
        TuSDKMoviePacketDecoder.this.start();
        if (TuSDKMoviePacketDecoder.this.getVideoInfo() == null)
        {
          TuSDKMoviePacketDecoder.this.onDecoderError(TuSDKMediaDecoder.TuSDKMediaDecoderError.UnsupportedVideoFormat);
          return;
        }
        if (TuSDKMoviePacketDecoder.a(TuSDKMoviePacketDecoder.this) == null) {
          return;
        }
        ThreadHelper.post(new Runnable()
        {
          public void run()
          {
            TuSDKMoviePacketDecoder.a(TuSDKMoviePacketDecoder.this).onLoadComplete(TuSDKMoviePacketDecoder.this.getVideoInfo());
          }
        });
      }
      else
      {
        TuSDKMoviePacketDecoder.a(TuSDKMoviePacketDecoder.this, TuSDKMoviePacketDecoder.State.Terminated);
        TuSDKMoviePacketDecoder.this.onDecoderError(TuSDKMediaDecoder.TuSDKMediaDecoderError.UnsupportedVideoFormat);
      }
    }
    
    public void onResultFail(Exception paramAnonymousException) {}
  };
  private TuSDKMoviePacketReader.TuSDKMovieReaderPacketDelegate t = new TuSDKMoviePacketReader.TuSDKMovieReaderPacketDelegate()
  {
    public void onAVPacketAvailable(TuSDKAVPacket paramAnonymousTuSDKAVPacket)
    {
      if ((paramAnonymousTuSDKAVPacket == null) || (!TuSDKMoviePacketDecoder.this.isDecoding())) {
        return;
      }
      TuSDKMoviePacketDecoder.a(TuSDKMoviePacketDecoder.this, -1L);
      if (paramAnonymousTuSDKAVPacket.getPacketType() == 1)
      {
        TuSDKMoviePacketDecoder.a(TuSDKMoviePacketDecoder.this, paramAnonymousTuSDKAVPacket);
        if (TuSDKMoviePacketDecoder.c(TuSDKMoviePacketDecoder.this)) {
          TuSDKMoviePacketDecoder.d(TuSDKMoviePacketDecoder.this);
        } else {
          while (!TuSDKMoviePacketDecoder.d(TuSDKMoviePacketDecoder.this)) {}
        }
      }
    }
    
    public void onReadComplete()
    {
      if (TuSDKMoviePacketDecoder.e(TuSDKMoviePacketDecoder.this) != null) {
        TuSDKMoviePacketDecoder.e(TuSDKMoviePacketDecoder.this).reset();
      }
      if (TuSDKMoviePacketDecoder.this.mVideoDecoder != null) {
        TuSDKMoviePacketDecoder.this.mVideoDecoder.flush();
      }
      TuSDKMoviePacketDecoder.this.onDecoderComplete();
      if ((TuSDKMoviePacketDecoder.f(TuSDKMoviePacketDecoder.this)) && (TuSDKMoviePacketDecoder.this.mMovieReader != null)) {
        ((TuSDKMoviePacketReader)TuSDKMoviePacketDecoder.this.mMovieReader).start();
      }
    }
  };
  
  public TuSDKMoviePacketDecoder(String paramString)
  {
    this(TuSDKMediaDataSource.create(paramString));
  }
  
  public TuSDKMoviePacketDecoder(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    super(paramTuSDKMediaDataSource);
  }
  
  public TuSDKVideoInfo getVideoInfo()
  {
    return this.f;
  }
  
  public TuSDKVideoInfo getOriginalVideoInfo()
  {
    if (this.g == null) {
      this.g = TuSDKMediaUtils.getVideoInfo(this.mDataSource);
    }
    return this.g;
  }
  
  public MediaCodec getVideoDecoder()
  {
    return this.mVideoDecoder;
  }
  
  public MediaCodec getAudioDecoder()
  {
    return null;
  }
  
  public void setEnableTranscoding(boolean paramBoolean)
  {
    this.n = paramBoolean;
  }
  
  public void setTranscodingOutputSize(TuSdkSize paramTuSdkSize)
  {
    this.q = paramTuSdkSize;
  }
  
  public String getProcessedFilePath()
  {
    return this.o;
  }
  
  public TuSDKVideoSpeedControl getVideoSpeedControl()
  {
    if (this.d == null) {
      this.d = new TuSDKVideoSpeedControl();
    }
    return this.d;
  }
  
  public TuSDKVideoSurfaceDecodeDelegate getVideoDelegate()
  {
    return this.i;
  }
  
  public void setVideoDelegate(TuSDKVideoSurfaceDecodeDelegate paramTuSDKVideoSurfaceDecodeDelegate)
  {
    this.i = paramTuSDKVideoSurfaceDecodeDelegate;
  }
  
  public void setLoadDelegate(TuSDKVideoLoadDelegate paramTuSDKVideoLoadDelegate)
  {
    this.j = paramTuSDKVideoLoadDelegate;
  }
  
  public void setReadMode(TuSDKMoviePacketReader.ReadMode paramReadMode)
  {
    this.l = paramReadMode;
    if (this.mMovieReader != null) {
      ((TuSDKMoviePacketReader)this.mMovieReader).setReadMode(paramReadMode);
    }
  }
  
  public TuSDKMoviePacketReader.ReadMode getReadMode()
  {
    return this.l;
  }
  
  public void setTimeEffectController(TuSDKVideoTimeEffectController paramTuSDKVideoTimeEffectController)
  {
    this.m = paramTuSDKVideoTimeEffectController;
    if (this.mMovieReader != null) {
      ((TuSDKMoviePacketReader)this.mMovieReader).setTimeEffectController(this.m);
    }
  }
  
  public long getVideoDurationTimeUS()
  {
    if (getVideoInfo() == null) {
      return 0L;
    }
    return getVideoInfo().durationTimeUs;
  }
  
  public TuSdkTimeRange getTimeRange()
  {
    return this.b;
  }
  
  public long getCurrentSampleTimeUs()
  {
    if (this.k != -1L) {
      return this.k;
    }
    return this.c;
  }
  
  public TuSdkSize getVideoSize()
  {
    if (getVideoInfo() != null) {
      return TuSdkSize.create(getVideoInfo().width, getVideoInfo().height);
    }
    return TuSdkSize.create(0);
  }
  
  public void setLooping(boolean paramBoolean)
  {
    this.e = paramBoolean;
  }
  
  public void setDoRender(boolean paramBoolean)
  {
    this.p = paramBoolean;
  }
  
  protected void setState(State paramState)
  {
    this.h = paramState;
  }
  
  public State getState()
  {
    return this.h;
  }
  
  protected boolean isDecoding()
  {
    return this.h == State.Decoding;
  }
  
  public void prepare(Surface paramSurface, TuSdkTimeRange paramTuSdkTimeRange)
  {
    this.a = paramSurface;
    this.b = paramTuSdkTimeRange;
  }
  
  protected void onDecoderError(TuSDKMediaDecoder.TuSDKMediaDecoderError paramTuSDKMediaDecoderError)
  {
    if (this.i != null) {
      this.i.onDecoderError(paramTuSDKMediaDecoderError);
    }
  }
  
  protected void onDecoderComplete()
  {
    this.k = -1L;
    this.c = 0L;
    if (!this.e) {
      setState(State.Terminated);
    }
    if (this.i != null) {
      this.i.onDecoderComplete();
    }
  }
  
  public void loadVideoCover()
  {
    this.r = true;
    start();
  }
  
  public void start()
  {
    this.c = 0L;
    if ((this.n) && (this.o == null))
    {
      b();
      return;
    }
    if (isDecoding()) {
      return;
    }
    this.mMovieReader = createMovieReader();
    if (this.mMovieReader == null) {
      return;
    }
    this.mVideoDecoder = createVideoDecoder(this.a);
    if (this.mVideoDecoder == null) {
      return;
    }
    if (this.f == null)
    {
      this.f = ((TuSDKMoviePacketReader)this.mMovieReader).getVideoInfo();
      if (this.n)
      {
        TuSDKVideoInfo localTuSDKVideoInfo = getOriginalVideoInfo();
        this.f.bitrate = localTuSDKVideoInfo.bitrate;
        this.f.existAudioTrack = localTuSDKVideoInfo.existAudioTrack;
      }
      if (this.i != null) {
        this.i.onVideoInfoReady(this.f);
      }
    }
    if (findVideoTrack() == -1)
    {
      TLog.e("No video track found", new Object[0]);
      destroyMediaReader();
      return;
    }
    if ((this.f == null) || (this.f.width <= 0))
    {
      TLog.e("Invalid video size", new Object[0]);
      return;
    }
    if (getVideoSpeedControl() != null) {
      getVideoSpeedControl().reset();
    }
    setState(State.Decoding);
    this.mVideoDecoder.start();
    ((TuSDKMoviePacketReader)this.mMovieReader).setReadAudioPacketEnable(false);
    ((TuSDKMoviePacketReader)this.mMovieReader).setTimeEffectController(this.m);
    ((TuSDKMoviePacketReader)this.mMovieReader).setReadMode(this.l);
    ((TuSDKMoviePacketReader)this.mMovieReader).setDelegate(this.t);
    if (this.k != -1L) {
      ((TuSDKMoviePacketReader)this.mMovieReader).seekTo(this.k);
    }
    ((TuSDKMoviePacketReader)this.mMovieReader).start();
    this.k = -1L;
  }
  
  public void pause()
  {
    if (!isDecoding()) {
      return;
    }
    this.k = this.c;
    setState(State.Idle);
    ((TuSDKMoviePacketReader)this.mMovieReader).stop();
    super.stop();
  }
  
  public void stop()
  {
    if (this.h == State.Idle) {
      return;
    }
    if ((this.h != State.Terminated) || (this.e)) {
      this.c = 0L;
    }
    setState(State.Idle);
    this.r = false;
    if (this.mMovieReader != null) {
      ((TuSDKMoviePacketReader)this.mMovieReader).stop();
    }
    super.stop();
  }
  
  public void seekTo(long paramLong, int paramInt)
  {
    this.k = paramLong;
    super.seekTo(paramLong, paramInt);
  }
  
  public TuSDKMoviePacketReader createMovieReader()
  {
    if (this.n)
    {
      if ((this.o == null) || (!new File(this.o).exists()))
      {
        TLog.e("Please set a valid data source.", new Object[0]);
        onDecoderError(TuSDKMediaDecoder.TuSDKMediaDecoderError.InvalidDataSource);
        return null;
      }
      return new TuSDKMoviePacketReader(TuSDKMediaDataSource.create(this.o));
    }
    if ((this.mDataSource == null) || (!this.mDataSource.isValid()))
    {
      TLog.e("Please set a valid data source", new Object[0]);
      onDecoderError(TuSDKMediaDecoder.TuSDKMediaDecoderError.InvalidDataSource);
      return null;
    }
    return new TuSDKMoviePacketReader(this.mDataSource);
  }
  
  public void destroy()
  {
    stop();
    destroyMediaReader();
    setState(State.Terminated);
    if (this.o != null) {
      new File(this.o).delete();
    }
    this.i = null;
  }
  
  private void a()
  {
    if ((this.i == null) || (this.r) || (!isDecoding())) {
      return;
    }
    float f1 = getProgress();
    this.i.onProgressChanged(getCurrentSampleTimeUs(), f1);
  }
  
  public float getProgress()
  {
    float f1 = (float)getCurrentSampleTimeUs() / (float)getVideoDurationTimeUS();
    return f1;
  }
  
  private void b()
  {
    if (this.h == State.Processing) {
      return;
    }
    setState(State.Processing);
    TuSDKMovieTranscoder localTuSDKMovieTranscoder = new TuSDKMovieTranscoder(this.mDataSource);
    TuSDKVideoEncoderSetting localTuSDKVideoEncoderSetting = TuSDKVideoEncoderSetting.getDefaultRecordSetting();
    localTuSDKVideoEncoderSetting.videoSize = this.q;
    localTuSDKVideoEncoderSetting.videoQuality = null;
    localTuSDKVideoEncoderSetting.enableAllKeyFrame = true;
    localTuSDKMovieTranscoder.setTimeRange(getTimeRange());
    localTuSDKMovieTranscoder.setVideoEncoderSetting(localTuSDKVideoEncoderSetting);
    localTuSDKMovieTranscoder.setSaveDelegate(this.s);
    localTuSDKMovieTranscoder.startRecording();
  }
  
  protected void onVideoDecoderNewFrameAvailable(int paramInt, MediaCodec.BufferInfo paramBufferInfo)
  {
    if ((paramBufferInfo.size > 0) && (this.i != null))
    {
      if (this.r)
      {
        setState(State.Processing);
        stop();
      }
      this.i.onVideoDecoderNewFrameAvailable(paramInt, paramBufferInfo);
    }
  }
  
  private boolean a(TuSDKAVPacket paramTuSDKAVPacket)
  {
    ByteBuffer[] arrayOfByteBuffer = this.mVideoDecoder.getInputBuffers();
    int i1 = this.mVideoDecoder.dequeueInputBuffer(5000L);
    if (i1 >= 0)
    {
      ByteBuffer localByteBuffer = arrayOfByteBuffer[i1];
      if (paramTuSDKAVPacket == null)
      {
        this.mVideoDecoder.queueInputBuffer(i1, 0, 0, 0L, 4);
        return true;
      }
      localByteBuffer.put(paramTuSDKAVPacket.getByteBuffer());
      long l1 = paramTuSDKAVPacket.getSampleTimeUs();
      this.c = l1;
      a();
      this.mVideoDecoder.queueInputBuffer(i1, 0, paramTuSDKAVPacket.getChunkSize(), l1, 0);
    }
    return false;
  }
  
  @TargetApi(21)
  private boolean c()
  {
    MediaCodec.BufferInfo localBufferInfo = new MediaCodec.BufferInfo();
    int i1 = this.mVideoDecoder.dequeueOutputBuffer(localBufferInfo, 5000L);
    switch (i1)
    {
    case -2: 
      this.f.syncCodecCrop(this.mVideoDecoder.getOutputFormat());
      if (this.i != null) {
        this.i.onVideoInfoReady(this.f);
      }
      break;
    }
    if ((localBufferInfo.flags & 0x4) != 0)
    {
      onDecoderComplete();
      return true;
    }
    boolean bool = localBufferInfo.size > 0;
    if (i1 >= 0)
    {
      this.mVideoDecoder.releaseOutputBuffer(i1, bool);
      onVideoDecoderNewFrameAvailable(i1, localBufferInfo);
      if ((bool) && (getVideoSpeedControl() != null)) {
        getVideoSpeedControl().preRender(localBufferInfo.presentationTimeUs);
      }
    }
    return !bool;
  }
  
  public static abstract interface VideoSpeedControlInterface
  {
    public abstract void setEnable(boolean paramBoolean);
    
    public abstract void setFrameRate(int paramInt);
    
    public abstract void preRender(long paramLong);
    
    public abstract void reset();
  }
  
  public static enum State
  {
    private State() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\decoder\TuSDKMoviePacketDecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */