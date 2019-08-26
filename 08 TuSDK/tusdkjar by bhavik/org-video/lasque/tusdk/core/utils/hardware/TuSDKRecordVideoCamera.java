package org.lasque.tusdk.core.utils.hardware;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.widget.RelativeLayout;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.api.movie.postproc.muxer.TuSDKMovieClipper;
import org.lasque.tusdk.api.movie.postproc.muxer.TuSDKMovieClipper.TuSDKMovieClipperListener;
import org.lasque.tusdk.api.movie.postproc.muxer.TuSDKMovieClipper.TuSDKMovieClipperOption;
import org.lasque.tusdk.api.movie.postproc.muxer.TuSDKMovieClipper.TuSDKMovieSegment;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting.VideoQuality;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.sources.TuSDKMovieCreatorInterface;
import org.lasque.tusdk.core.seles.sources.TuSDKMovieCreatorInterface.ByteDataFrame;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.video.SelesSurfaceEncoderInterface;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.AlbumHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import org.lasque.tusdk.core.video.TuSDKVideoCaptureSetting;
import org.lasque.tusdk.core.video.TuSDKVideoResult;
import org.lasque.tusdk.impl.components.camera.TuVideoFocusTouchView;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

@SuppressLint({"InlinedApi"})
@TargetApi(18)
public class TuSDKRecordVideoCamera
  extends TuSDKVideoCamera
  implements TuSDKMovieCreatorInterface
{
  private RecordState b;
  private RecordMode c = RecordMode.Normal;
  private List<TuSdkTimeRange> d = new ArrayList(5);
  private List<TuSdkTimeRange> e = new ArrayList(5);
  private File f;
  private boolean g = true;
  private String h;
  private long i = 52428800L;
  protected MediaMuxer mMuxer;
  private boolean j = false;
  private int k = -1;
  private int l = -1;
  private HandlerThread m = new HandlerThread("WriteSampleDataWriter");
  private RecordHandler n;
  private TuSDKMovieClipper o;
  private TuSDKRecordVideoCameraDelegate p;
  private int q = 2;
  private int r = 10;
  private long s;
  private long t;
  private boolean u;
  private boolean v;
  private SpeedMode w = SpeedMode.NORMAL;
  private TuSDKFrameSpeedRateController x = new TuSDKFrameSpeedRateController();
  private TuSDKFrameSpeedRateController y = new TuSDKFrameSpeedRateController();
  TuSDKMovieClipper.TuSDKMovieClipperListener a = new TuSDKMovieClipper.TuSDKMovieClipperListener()
  {
    public void onStart()
    {
      TuSDKRecordVideoCamera.this.notifyRecordingState(TuSDKRecordVideoCamera.RecordState.Saving);
    }
    
    public void onCancel() {}
    
    public void onDone(String paramAnonymousString)
    {
      TuSDKRecordVideoCamera.a(TuSDKRecordVideoCamera.this);
      File localFile = new File(paramAnonymousString);
      TuSDKRecordVideoCamera.this.notifyRecordingResultWithVideoFile(localFile);
    }
    
    public void onError(Exception paramAnonymousException)
    {
      TuSDKRecordVideoCamera.a(TuSDKRecordVideoCamera.this, TuSDKRecordVideoCamera.RecordError.SaveFailed);
    }
  };
  private TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate z = new TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate()
  {
    public void onAudioEncoderStarted(MediaFormat paramAnonymousMediaFormat)
    {
      if ((!TuSDKRecordVideoCamera.b(TuSDKRecordVideoCamera.this)) && (!TuSDKRecordVideoCamera.c(TuSDKRecordVideoCamera.this)))
      {
        MediaMuxer localMediaMuxer = TuSDKRecordVideoCamera.this.getMediaMuxer();
        if (localMediaMuxer == null) {
          return;
        }
        TuSDKRecordVideoCamera.a(TuSDKRecordVideoCamera.this, localMediaMuxer.addTrack(paramAnonymousMediaFormat));
        if (TuSDKRecordVideoCamera.d(TuSDKRecordVideoCamera.this))
        {
          TuSDKRecordVideoCamera.this.getMediaMuxer().start();
          TuSDKRecordVideoCamera.a(TuSDKRecordVideoCamera.this, true);
        }
      }
    }
    
    public void onAudioEncoderStoped() {}
    
    public void onAudioEncoderFrameDataAvailable(long paramAnonymousLong, ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if ((TuSDKRecordVideoCamera.b(TuSDKRecordVideoCamera.this)) && (TuSDKRecordVideoCamera.c(TuSDKRecordVideoCamera.this)) && (TuSDKRecordVideoCamera.this.isRecording()) && (!TuSDKRecordVideoCamera.this.isRecordingPaused()) && (TuSDKRecordVideoCamera.e(TuSDKRecordVideoCamera.this) > 0L))
      {
        TuSDKMovieCreatorInterface.ByteDataFrame localByteDataFrame = new TuSDKMovieCreatorInterface.ByteDataFrame();
        localByteDataFrame.trackIndex = TuSDKRecordVideoCamera.f(TuSDKRecordVideoCamera.this);
        localByteDataFrame.buffer = paramAnonymousByteBuffer;
        localByteDataFrame.bufferInfo = paramAnonymousBufferInfo;
        Message localMessage = TuSDKRecordVideoCamera.g(TuSDKRecordVideoCamera.this).obtainMessage(6, localByteDataFrame);
        TuSDKRecordVideoCamera.g(TuSDKRecordVideoCamera.this).sendMessage(localMessage);
      }
    }
    
    public void onAudioEncoderCodecConfig(long paramAnonymousLong, ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo) {}
  };
  private TuSDKVideoDataEncoderDelegate A = new TuSDKVideoDataEncoderDelegate()
  {
    public void onVideoEncoderStarted(MediaFormat paramAnonymousMediaFormat)
    {
      if ((!TuSDKRecordVideoCamera.b(TuSDKRecordVideoCamera.this)) && (!TuSDKRecordVideoCamera.d(TuSDKRecordVideoCamera.this)))
      {
        MediaMuxer localMediaMuxer = TuSDKRecordVideoCamera.this.getMediaMuxer();
        if (localMediaMuxer == null) {
          return;
        }
        TuSDKRecordVideoCamera.b(TuSDKRecordVideoCamera.this, localMediaMuxer.addTrack(paramAnonymousMediaFormat));
        if (TuSDKRecordVideoCamera.h(TuSDKRecordVideoCamera.this))
        {
          if (TuSDKRecordVideoCamera.c(TuSDKRecordVideoCamera.this))
          {
            TuSDKRecordVideoCamera.this.getMediaMuxer().start();
            TuSDKRecordVideoCamera.a(TuSDKRecordVideoCamera.this, true);
          }
        }
        else
        {
          TuSDKRecordVideoCamera.this.getMediaMuxer().start();
          TuSDKRecordVideoCamera.a(TuSDKRecordVideoCamera.this, true);
        }
      }
    }
    
    public void onVideoEncoderFrameDataAvailable(long paramAnonymousLong, ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if ((TuSDKRecordVideoCamera.b(TuSDKRecordVideoCamera.this)) && (TuSDKRecordVideoCamera.d(TuSDKRecordVideoCamera.this)) && (TuSDKRecordVideoCamera.this.isRecording()) && (!TuSDKRecordVideoCamera.this.isRecordingPaused()))
      {
        TuSDKMovieCreatorInterface.ByteDataFrame localByteDataFrame = new TuSDKMovieCreatorInterface.ByteDataFrame();
        localByteDataFrame.trackIndex = TuSDKRecordVideoCamera.i(TuSDKRecordVideoCamera.this);
        localByteDataFrame.buffer = paramAnonymousByteBuffer;
        localByteDataFrame.bufferInfo = paramAnonymousBufferInfo;
        Message localMessage = TuSDKRecordVideoCamera.g(TuSDKRecordVideoCamera.this).obtainMessage(6, localByteDataFrame);
        TuSDKRecordVideoCamera.g(TuSDKRecordVideoCamera.this).sendMessage(localMessage);
      }
    }
    
    public void onVideoEncoderCodecConfig(long paramAnonymousLong, ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo) {}
  };
  
  public TuSDKRecordVideoCamera(Context paramContext, TuSDKVideoCaptureSetting paramTuSDKVideoCaptureSetting, RelativeLayout paramRelativeLayout)
  {
    super(paramContext, paramTuSDKVideoCaptureSetting, paramRelativeLayout);
    this.m.start();
    this.n = new RecordHandler(this.m.getLooper());
  }
  
  public TuSDKRecordVideoCamera(Context paramContext, RelativeLayout paramRelativeLayout)
  {
    this(paramContext, new TuSDKVideoCaptureSetting(), paramRelativeLayout);
  }
  
  public TuSDKVideoEncoderSetting getVideoEncoderSetting()
  {
    if (this.mVideoEncoderSetting == null) {
      this.mVideoEncoderSetting = TuSDKVideoEncoderSetting.getDefaultRecordSetting();
    }
    return super.getVideoEncoderSetting();
  }
  
  public TuSDKRecordVideoCameraDelegate getVideoDelegate()
  {
    return this.p;
  }
  
  public void setVideoDelegate(TuSDKRecordVideoCameraDelegate paramTuSDKRecordVideoCameraDelegate)
  {
    this.p = paramTuSDKRecordVideoCameraDelegate;
  }
  
  public final void setRecordMode(RecordMode paramRecordMode)
  {
    if (paramRecordMode == null) {
      return;
    }
    if ((paramRecordMode == RecordMode.Keep) && (!SdkValid.shared.videoRecordContinuousEnabled()))
    {
      TLog.e("You are not allowed to use the record continuous mode, please see http://tusdk.com", new Object[0]);
      return;
    }
    this.c = paramRecordMode;
  }
  
  public RecordMode getRecordMode()
  {
    return this.c;
  }
  
  private boolean a()
  {
    if ((getContext() == null) || (!FileHelper.mountedExternalStorage())) {
      return false;
    }
    File localFile = getMovieOutputPath();
    if (localFile == null)
    {
      TLog.e("TuSDKRecordVideoCamera | Create movie output file failed", new Object[0]);
      return true;
    }
    boolean bool = FileHelper.getAvailableStore(localFile.getParent()) > getMinAvailableSpaceBytes();
    return bool;
  }
  
  public void setMinAvailableSpaceBytes(long paramLong)
  {
    this.i = paramLong;
  }
  
  public long getMinAvailableSpaceBytes()
  {
    return this.i;
  }
  
  public boolean isSaveToAlbum()
  {
    return this.g;
  }
  
  public void setSaveToAlbum(boolean paramBoolean)
  {
    if (isRecording())
    {
      TLog.w("Could not set 'saveToAlbum' while recording.", new Object[0]);
      return;
    }
    this.g = paramBoolean;
  }
  
  public String getSaveToAlbumName()
  {
    return this.h;
  }
  
  public void setSaveToAlbumName(String paramString)
  {
    this.h = paramString;
  }
  
  public int getMinRecordingTime()
  {
    return Math.max(0, this.q);
  }
  
  public void setMinRecordingTime(int paramInt)
  {
    this.q = Math.max(0, paramInt);
  }
  
  public int getMaxRecordingTime()
  {
    return Math.max(0, this.r);
  }
  
  public void setMaxRecordingTime(int paramInt)
  {
    this.r = Math.max(0, paramInt);
  }
  
  public void setSpeedMode(SpeedMode paramSpeedMode)
  {
    if ((paramSpeedMode == null) || (this.w == paramSpeedMode)) {
      return;
    }
    if (isRecording())
    {
      TLog.e("The rate of change is not allowed during recording.", new Object[0]);
      return;
    }
    this.w = paramSpeedMode;
    getVideoEncoderSetting().enableAllKeyFrame = (paramSpeedMode != SpeedMode.NORMAL);
    this.y.setSpeedRate(SpeedMode.a(paramSpeedMode));
    this.x.setSpeedRate(SpeedMode.a(paramSpeedMode));
  }
  
  public float getMovieDuration()
  {
    return c() - d() + b();
  }
  
  private float b()
  {
    if (this.s == 0L) {
      return 0.0F;
    }
    float f1 = (float)(this.t - this.s) / 1000000.0F;
    return f1;
  }
  
  private float c()
  {
    float f1 = 0.0F;
    Iterator localIterator = this.d.iterator();
    while (localIterator.hasNext())
    {
      TuSdkTimeRange localTuSdkTimeRange = (TuSdkTimeRange)localIterator.next();
      f1 += localTuSdkTimeRange.duration();
    }
    return f1;
  }
  
  private float d()
  {
    float f1 = 0.0F;
    Iterator localIterator = this.e.iterator();
    while (localIterator.hasNext())
    {
      TuSdkTimeRange localTuSdkTimeRange = (TuSdkTimeRange)localIterator.next();
      f1 += localTuSdkTimeRange.duration();
    }
    return f1;
  }
  
  private float e()
  {
    return c() + b();
  }
  
  protected void initCamera()
  {
    super.initCamera();
    setEnableFaceTrace(true);
  }
  
  public void initOutputSettings()
  {
    super.initOutputSettings();
    TuVideoFocusTouchView localTuVideoFocusTouchView = new TuVideoFocusTouchView(getContext());
    setFocusTouchView(localTuVideoFocusTouchView);
    setEnableAudioCapture(true);
    updateOutputFilterSettings();
    setVideoDataDelegate(this.A);
    setAudioDataDelegate(this.z);
  }
  
  protected void updateOutputFilterSettings()
  {
    boolean bool = (isDisableMirrorFrontFacing()) && (isFrontFacingCameraPresent()) && (isHorizontallyMirrorFrontFacingCamera());
    SelesSurfaceEncoderInterface localSelesSurfaceEncoderInterface = (SelesSurfaceEncoderInterface)getVideoDataEncoder();
    if (localSelesSurfaceEncoderInterface != null) {
      localSelesSurfaceEncoderInterface.setEnableHorizontallyFlip(bool);
    }
  }
  
  protected void setState(TuSdkStillCameraAdapter.CameraState paramCameraState)
  {
    super.setState(paramCameraState);
    if (paramCameraState == TuSdkStillCameraAdapter.CameraState.StateStarted) {
      StatisticsManger.appendComponent(this.c == RecordMode.Keep ? 9441280L : 9437186L);
    } else if (paramCameraState == TuSdkStillCameraAdapter.CameraState.StateUnknow) {
      StatisticsManger.appendComponent(this.c == RecordMode.Keep ? 9441281L : 9437187L);
    }
  }
  
  private Boolean f()
  {
    if (!SdkValid.shared.videoRecordEnabled())
    {
      TLog.e("The video record feature has been expired, please contact us via http://tusdk.com", new Object[0]);
      return Boolean.valueOf(false);
    }
    return Boolean.valueOf(true);
  }
  
  private void g()
  {
    if ((this.mMuxer == null) && (getMovieOutputPath() != null)) {
      try
      {
        this.mMuxer = new MediaMuxer(getMovieOutputPath().toString(), 0);
        this.mMuxer.setOrientationHint(getDeviceOrient().getDegree());
      }
      catch (IOException localIOException)
      {
        TLog.e("Error on get mediaMuxer: %s", new Object[] { localIOException });
      }
    }
  }
  
  protected MediaMuxer getMediaMuxer()
  {
    return this.mMuxer;
  }
  
  private void h()
  {
    if (!validateMovieFile()) {
      this.mMuxer = null;
    }
    if (this.mMuxer == null) {
      return;
    }
    try
    {
      this.j = false;
      this.mMuxer.stop();
      this.mMuxer.release();
      this.l = -1;
      this.k = -1;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    this.mMuxer = null;
  }
  
  private void i()
  {
    if ((getRecordMode() == RecordMode.Keep) && (this.f != null) && (this.s != 0L) && (this.t != 0L))
    {
      TuSdkTimeRange localTuSdkTimeRange1;
      if (this.d.isEmpty())
      {
        localTuSdkTimeRange1 = TuSdkTimeRange.makeRange(0.0F, e());
        this.d.add(localTuSdkTimeRange1);
      }
      else
      {
        localTuSdkTimeRange1 = (TuSdkTimeRange)this.d.get(this.d.size() - 1);
        float f1 = e();
        TuSdkTimeRange localTuSdkTimeRange2 = TuSdkTimeRange.makeRange(localTuSdkTimeRange1.getEndTime(), f1);
        if (localTuSdkTimeRange2.duration() > 0.0F) {
          this.d.add(localTuSdkTimeRange2);
        }
      }
    }
    this.s = 0L;
    this.t = 0L;
  }
  
  private void j()
  {
    if (this.e.isEmpty())
    {
      notifyRecordingResultWithVideoFile(this.f);
      return;
    }
    Uri localUri = Uri.fromFile(this.f);
    getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", localUri));
    File localFile = v();
    TuSDKMovieClipper.TuSDKMovieClipperOption localTuSDKMovieClipperOption = new TuSDKMovieClipper.TuSDKMovieClipperOption();
    localTuSDKMovieClipperOption.savePath = localFile.getPath();
    localTuSDKMovieClipperOption.srcUri = Uri.parse(this.f.getPath());
    localTuSDKMovieClipperOption.listener = this.a;
    localTuSDKMovieClipperOption.fps = getVideoEncoderSetting().videoQuality.getFps();
    this.o = new TuSDKMovieClipper(localTuSDKMovieClipperOption);
    ArrayList localArrayList = new ArrayList(this.e.size());
    Iterator localIterator = this.e.iterator();
    while (localIterator.hasNext())
    {
      TuSdkTimeRange localTuSdkTimeRange = (TuSdkTimeRange)localIterator.next();
      long l1 = localTuSdkTimeRange.getStartTimeUS();
      long l2 = localTuSdkTimeRange.getEndTimeUS();
      TuSDKMovieClipper.TuSDKMovieSegment localTuSDKMovieSegment = this.o.createSegment(l1, l2);
      localArrayList.add(localTuSDKMovieSegment);
    }
    this.o.removeSegments(localArrayList);
  }
  
  private void k()
  {
    i();
    notifyRecordingState(RecordState.Paused);
  }
  
  private void l()
  {
    if (!validateMinRecordingTime())
    {
      TLog.e("Recording time is less than %d seconds", new Object[] { Integer.valueOf(getMinRecordingTime()) });
      a(RecordError.LessMinRecordingTime);
      return;
    }
    if (!validateMovieFile())
    {
      TLog.e("Invalid recording time  : %f seconds", new Object[] { Float.valueOf(getMovieDuration()) });
      a(RecordError.InvalidRecordingTime);
      u();
      return;
    }
    if (getRecordMode() == RecordMode.Keep)
    {
      StatisticsManger.appendComponent(9441282L);
      j();
    }
    else
    {
      notifyRecordingResultWithVideoFile(getMovieOutputPath());
    }
    u();
  }
  
  protected void notifyRecordingState(final RecordState paramRecordState)
  {
    if (getVideoDelegate() == null) {
      return;
    }
    this.b = paramRecordState;
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuSDKRecordVideoCamera.this.getVideoDelegate().onMovieRecordStateChanged(paramRecordState);
      }
    });
  }
  
  private void a(final RecordError paramRecordError)
  {
    if (getVideoDelegate() == null) {
      return;
    }
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuSDKRecordVideoCamera.this.getVideoDelegate().onMovieRecordFailed(paramRecordError);
      }
    });
  }
  
  protected void notifyRecordingResultWithVideoFile(File paramFile)
  {
    TuSDKVideoResult localTuSDKVideoResult = new TuSDKVideoResult();
    localTuSDKVideoResult.videoPath = paramFile;
    if (isSaveToAlbum())
    {
      localTuSDKVideoResult.videoSqlInfo = ImageSqlHelper.saveMp4ToAlbum(getContext(), paramFile);
      ImageSqlHelper.notifyRefreshAblum(getContext(), localTuSDKVideoResult.videoSqlInfo);
    }
    notifyResult(localTuSDKVideoResult);
  }
  
  protected void notifyResult(final TuSDKVideoResult paramTuSDKVideoResult)
  {
    t();
    this.f = null;
    if (getVideoDelegate() == null) {
      return;
    }
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        if (paramTuSDKVideoResult != null) {
          TuSDKRecordVideoCamera.this.getVideoDelegate().onMovieRecordComplete(paramTuSDKVideoResult);
        } else {
          TuSDKRecordVideoCamera.this.getVideoDelegate().onMovieRecordFailed(TuSDKRecordVideoCamera.RecordError.Unknow);
        }
      }
    });
  }
  
  private void m()
  {
    if (this.b == RecordState.RecordCompleted) {
      return;
    }
    float f1 = getMovieDuration();
    if ((getVideoDelegate() == null) || (getMaxRecordingTime() == 0)) {
      return;
    }
    final float f2 = Math.min(f1, getMaxRecordingTime());
    final float f3 = f2 / getMaxRecordingTime();
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuSDKRecordVideoCamera.this.getVideoDelegate().onMovieRecordProgressChanged(f3, f2);
      }
    });
    if (f1 > getMaxRecordingTime())
    {
      f1 = getMaxRecordingTime();
      if (getRecordMode() == RecordMode.Keep)
      {
        o();
        notifyRecordingState(RecordState.RecordCompleted);
      }
      else
      {
        p();
      }
    }
  }
  
  private boolean a(MediaCodec.BufferInfo paramBufferInfo)
  {
    return this.s != 0L;
  }
  
  private boolean b(MediaCodec.BufferInfo paramBufferInfo)
  {
    if (this.t > paramBufferInfo.presentationTimeUs) {
      return false;
    }
    int i1 = (paramBufferInfo.flags & 0x1) != 0 ? 1 : 0;
    if ((i1 != 0) && (!this.u)) {
      this.u = true;
    }
    if (!this.u) {
      return false;
    }
    if (this.s == 0L) {
      this.s = paramBufferInfo.presentationTimeUs;
    }
    this.t = paramBufferInfo.presentationTimeUs;
    TLog.e("mVideoFragmentLastTimeUS : %s", new Object[] { Long.valueOf(paramBufferInfo.presentationTimeUs) });
    return true;
  }
  
  protected boolean validateMovieFile()
  {
    if (isCanCaptureAudio()) {
      return (this.v) && (this.u) && (this.f != null);
    }
    return (this.u) && (this.f != null);
  }
  
  protected boolean validateMinRecordingTime()
  {
    return getMovieDuration() >= getMinRecordingTime();
  }
  
  protected boolean validateMaxRecordingTime()
  {
    if (getMaxRecordingTime() == 0) {
      return false;
    }
    return getMovieDuration() >= getMaxRecordingTime();
  }
  
  public void stopCameraCapture()
  {
    super.stopCameraCapture();
    if (isCameraFacingChangeing()) {
      return;
    }
    h();
    stopAudioRecording();
    stopVideoDataEncoder();
    u();
  }
  
  public void startRecording()
  {
    if (!a())
    {
      TLog.e("TuSDKRecordVideoCamera | There is no space available for your device （Min %dM is required）", new Object[] { Long.valueOf(getMinAvailableSpaceBytes() / 1024L / 1024L) });
      a(RecordError.NotEnoughSpace);
      return;
    }
    this.n.removeCallbacksAndMessages(null);
    this.n.sendEmptyMessage(1);
  }
  
  private void n()
  {
    if (isRecording()) {
      return;
    }
    if (validateMaxRecordingTime())
    {
      a(RecordError.MoreMaxDuration);
      return;
    }
    this.y.prepare();
    this.x.prepare();
    this.s = 0L;
    this.t = 0L;
    g();
    super.startRecording();
    notifyRecordingState(RecordState.Recording);
  }
  
  public void stopRecording()
  {
    this.n.removeCallbacksAndMessages(null);
    this.n.sendEmptyMessage(2);
  }
  
  public void _stopRecording()
  {
    if ((!isRecording()) || (isRecordingPaused())) {
      return;
    }
    p();
  }
  
  public void pauseRecording()
  {
    this.n.removeMessages(3);
    this.n.sendEmptyMessageDelayed(3, 200L);
  }
  
  private void o()
  {
    if ((!isRecording()) || (isRecordingPaused())) {
      return;
    }
    if (getRecordMode() == RecordMode.Normal)
    {
      _stopRecording();
      return;
    }
    if (this.o != null) {
      this.o.cancel();
    }
    pauseEncoder();
    k();
  }
  
  public void finishRecording()
  {
    this.n.removeMessages(5);
    this.n.sendEmptyMessage(5);
  }
  
  private void p()
  {
    if (!validateMinRecordingTime())
    {
      a(RecordError.LessMinRecordingTime);
      return;
    }
    h();
    if ((!isRecording()) && (!isRecordingPaused()))
    {
      l();
      return;
    }
    super.stopRecording();
    h();
    l();
  }
  
  public void pauseCameraCapture()
  {
    super.stopRecording();
    super.pauseCameraCapture();
  }
  
  public void cancelRecording()
  {
    this.n.removeCallbacksAndMessages(null);
    this.n.sendEmptyMessage(4);
  }
  
  private void q()
  {
    boolean bool = isRecording();
    if (!bool) {
      return;
    }
    r();
    notifyRecordingState(RecordState.Canceled);
  }
  
  public int getRecordingFragmentSize()
  {
    if (getRecordMode() == RecordMode.Normal) {
      return 0;
    }
    return this.d.size();
  }
  
  public TuSdkTimeRange popVideoFragment()
  {
    if (getRecordingFragmentSize() == 0) {
      return null;
    }
    TuSdkTimeRange localTuSdkTimeRange = lastVideoFragmentRange();
    if ((localTuSdkTimeRange != null) && (localTuSdkTimeRange.isValid())) {
      this.e.add(localTuSdkTimeRange);
    }
    StatisticsManger.appendComponent(9441296L);
    return localTuSdkTimeRange;
  }
  
  public void clearAllFragments()
  {
    if ((getRecordMode() == RecordMode.Normal) || (getRecordingFragmentSize() == 0)) {
      return;
    }
    this.n.removeCallbacksAndMessages(null);
    this.n.sendEmptyMessage(7);
  }
  
  private void r()
  {
    super.stopRecording();
    h();
    u();
    s();
    t();
  }
  
  public TuSdkTimeRange lastVideoFragmentRange()
  {
    if (getRecordingFragmentSize() == 0) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(this.d);
    localArrayList.removeAll(this.e);
    if (!localArrayList.isEmpty()) {
      return (TuSdkTimeRange)localArrayList.get(localArrayList.size() - 1);
    }
    return null;
  }
  
  private void s()
  {
    if ((getRecordMode() == RecordMode.Keep) && (this.f != null) && (this.f.exists()))
    {
      this.f.delete();
      Uri localUri = Uri.fromFile(this.f);
      getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", localUri));
    }
    this.f = null;
  }
  
  private void t()
  {
    this.d.clear();
    this.e.clear();
    this.s = 0L;
    this.t = 0L;
  }
  
  private void u()
  {
    this.j = false;
    this.l = -1;
    this.k = -1;
    this.u = false;
    this.v = false;
    t();
    this.y.reset();
    this.x.reset();
  }
  
  public File getMovieOutputPath()
  {
    if (this.f == null) {
      this.f = v();
    }
    return this.f;
  }
  
  private File v()
  {
    if (isSaveToAlbum()) {
      return w();
    }
    return new File(TuSdk.getAppTempPath(), String.format("lsq_%s.mp4", new Object[] { StringHelper.timeStampString() }));
  }
  
  private File w()
  {
    if (StringHelper.isNotBlank(getSaveToAlbumName())) {
      return AlbumHelper.getAlbumVideoFile(getSaveToAlbumName());
    }
    return AlbumHelper.getAlbumVideoFile();
  }
  
  public void switchFilter(String paramString)
  {
    if ((paramString == null) || (isFilterChanging()) || (this.mFilterWrap.equalsCode(paramString))) {
      return;
    }
    if ((!FilterManager.shared().isNormalFilter(paramString)) && (!f().booleanValue())) {
      return;
    }
    super.switchFilter(paramString);
  }
  
  private boolean x()
  {
    return (!y()) && (isCanCaptureAudio());
  }
  
  private boolean y()
  {
    return this.l != -1;
  }
  
  private boolean z()
  {
    return this.k != -1;
  }
  
  @SuppressLint({"InlinedApi"})
  private void a(TuSDKMovieCreatorInterface.ByteDataFrame paramByteDataFrame)
  {
    if ((isRecording()) && (!isRecordingPaused()) && (!validateMaxRecordingTime()) && (this.j))
    {
      if (paramByteDataFrame.trackIndex == this.l)
      {
        if (!a(paramByteDataFrame.bufferInfo))
        {
          TLog.e("Audio timeampUs error", new Object[0]);
          return;
        }
        this.v = true;
      }
      if ((paramByteDataFrame.trackIndex == this.k) && (!b(paramByteDataFrame.bufferInfo)))
      {
        TLog.e("Video timeampUs error", new Object[0]);
        return;
      }
      getMediaMuxer().writeSampleData(paramByteDataFrame.trackIndex, paramByteDataFrame.buffer, paramByteDataFrame.bufferInfo);
      m();
    }
  }
  
  private void b(final TuSDKMovieCreatorInterface.ByteDataFrame paramByteDataFrame)
  {
    TuSDKFrameSpeedRateController localTuSDKFrameSpeedRateController = null;
    if (paramByteDataFrame.trackIndex == this.l) {
      localTuSDKFrameSpeedRateController = this.x;
    }
    if (paramByteDataFrame.trackIndex == this.k) {
      localTuSDKFrameSpeedRateController = this.y;
    }
    localTuSDKFrameSpeedRateController.setFrameSpeedRateCallback(new TuSDKFrameSpeedRateController.FrameSpeedRateCallback()
    {
      public void onAvailableFrameTimeUs(long paramAnonymousLong)
      {
        TuSDKRecordVideoCamera.a(TuSDKRecordVideoCamera.this, paramByteDataFrame);
      }
    });
    localTuSDKFrameSpeedRateController.requestAdjustSpeed(paramByteDataFrame.bufferInfo);
  }
  
  private class RecordHandler
    extends Handler
  {
    public RecordHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      case 1: 
        TuSDKRecordVideoCamera.j(TuSDKRecordVideoCamera.this);
        break;
      case 2: 
        TuSDKRecordVideoCamera.this._stopRecording();
        break;
      case 3: 
        TuSDKRecordVideoCamera.k(TuSDKRecordVideoCamera.this);
        break;
      case 4: 
        TuSDKRecordVideoCamera.l(TuSDKRecordVideoCamera.this);
        break;
      case 5: 
        TuSDKRecordVideoCamera.m(TuSDKRecordVideoCamera.this);
        break;
      case 6: 
        TuSDKMovieCreatorInterface.ByteDataFrame localByteDataFrame = (TuSDKMovieCreatorInterface.ByteDataFrame)paramMessage.obj;
        TuSDKRecordVideoCamera.b(TuSDKRecordVideoCamera.this, localByteDataFrame);
        break;
      case 7: 
        TuSDKRecordVideoCamera.n(TuSDKRecordVideoCamera.this);
        break;
      }
    }
  }
  
  public static enum SpeedMode
  {
    private float a;
    
    private SpeedMode(float paramFloat)
    {
      this.a = paramFloat;
    }
    
    public float getSpeedRate()
    {
      return this.a;
    }
  }
  
  public static enum RecordState
  {
    private RecordState() {}
  }
  
  public static enum RecordError
  {
    private RecordError() {}
  }
  
  public static enum RecordMode
  {
    private RecordMode() {}
  }
  
  public static abstract interface TuSDKRecordVideoCameraDelegate
  {
    public abstract void onMovieRecordComplete(TuSDKVideoResult paramTuSDKVideoResult);
    
    public abstract void onMovieRecordProgressChanged(float paramFloat1, float paramFloat2);
    
    public abstract void onMovieRecordStateChanged(TuSDKRecordVideoCamera.RecordState paramRecordState);
    
    public abstract void onMovieRecordFailed(TuSDKRecordVideoCamera.RecordError paramRecordError);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\utils\hardware\TuSDKRecordVideoCamera.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */