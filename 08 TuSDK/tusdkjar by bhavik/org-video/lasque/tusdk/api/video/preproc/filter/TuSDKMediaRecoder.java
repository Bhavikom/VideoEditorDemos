package org.lasque.tusdk.api.video.preproc.filter;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.opengl.EGLContext;
import java.io.File;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.audio.TuSDKAudioCaptureSetting;
import org.lasque.tusdk.core.audio.TuSDKAudioRecorderCore;
import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;
import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate;
import org.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;
import org.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoder;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.video.SelesSurfaceEncoderInterface;
import org.lasque.tusdk.core.seles.video.SelesSurfaceTextureEncoder;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.video.editor.TuSDKMovieWriter;
import org.lasque.tusdk.video.editor.TuSDKMovieWriter.TuSDKMovieWriterDelegate;
import org.lasque.tusdk.video.editor.TuSDKMovieWriterInterface.MovieWriterOutputFormat;

public class TuSDKMediaRecoder
{
  private TuSDKMediaRecoderDelegate a;
  private TuSDKMediaRecoderVideoFirstSampleDelegate b;
  private State c = State.Idle;
  private SelesSurfaceEncoderInterface d;
  private TuSDKMovieWriter e;
  private int f;
  private boolean g = false;
  private TuSDKAudioRecorderCore h;
  private TuSDKVideoEncoderSetting i;
  private File j;
  private boolean k = false;
  private long l;
  private long m = 0L;
  private long n;
  private long o;
  private TuSDKMovieWriter.TuSDKMovieWriterDelegate p = new TuSDKMovieWriter.TuSDKMovieWriterDelegate()
  {
    public void onFirstVideoSampleDataWrited(long paramAnonymousLong)
    {
      TuSDKMediaRecoder.a(TuSDKMediaRecoder.this, paramAnonymousLong);
      if (TuSDKMediaRecoder.a(TuSDKMediaRecoder.this) != null) {
        TuSDKMediaRecoder.a(TuSDKMediaRecoder.this).onFirstVideoSampleDataAlready(paramAnonymousLong);
      }
    }
    
    public void onProgressChanged(float paramAnonymousFloat, long paramAnonymousLong)
    {
      TuSDKMediaRecoder.b(TuSDKMediaRecoder.this, paramAnonymousLong);
      if (TuSDKMediaRecoder.b(TuSDKMediaRecoder.this) != null) {
        TuSDKMediaRecoder.b(TuSDKMediaRecoder.this).onMediaRecoderProgressChanged(paramAnonymousFloat);
      }
    }
  };
  private TuSDKVideoDataEncoderDelegate q = new TuSDKVideoDataEncoderDelegate()
  {
    public void onVideoEncoderStarted(MediaFormat paramAnonymousMediaFormat)
    {
      if (!TuSDKMediaRecoder.c(TuSDKMediaRecoder.this).canAddVideoTrack()) {
        return;
      }
      TuSDKMediaRecoder.c(TuSDKMediaRecoder.this).addVideoTrack(paramAnonymousMediaFormat);
      if (TuSDKMediaRecoder.c(TuSDKMediaRecoder.this).hasAudioTrack()) {
        TuSDKMediaRecoder.c(TuSDKMediaRecoder.this).start();
      }
      if ((!TuSDKMediaRecoder.d(TuSDKMediaRecoder.this)) && ((TuSDKMediaRecoder.e(TuSDKMediaRecoder.this)) || (TuSDKMediaRecoder.f(TuSDKMediaRecoder.this) == null))) {
        TuSDKMediaRecoder.c(TuSDKMediaRecoder.this).start();
      }
    }
    
    public void onVideoEncoderFrameDataAvailable(long paramAnonymousLong, ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if ((TuSDKMediaRecoder.c(TuSDKMediaRecoder.this) == null) || (!TuSDKMediaRecoder.c(TuSDKMediaRecoder.this).isStarted()) || (!TuSDKMediaRecoder.c(TuSDKMediaRecoder.this).hasVideoTrack()) || (!TuSDKMediaRecoder.this.isRecording())) {
        return;
      }
      if (!a(paramAnonymousBufferInfo)) {
        return;
      }
      TuSDKMediaRecoder.c(TuSDKMediaRecoder.this).writeVideoSampleData(paramAnonymousByteBuffer, paramAnonymousBufferInfo);
    }
    
    private boolean a(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if ((TuSDKMediaRecoder.g(TuSDKMediaRecoder.this) > 0L) && (TuSDKMediaRecoder.h(TuSDKMediaRecoder.this) == 0L) && (!TuSDKMediaRecoder.e(TuSDKMediaRecoder.this))) {
        TuSDKMediaRecoder.c(TuSDKMediaRecoder.this, TuSDKMediaRecoder.g(TuSDKMediaRecoder.this) - paramAnonymousBufferInfo.presentationTimeUs);
      }
      paramAnonymousBufferInfo.presentationTimeUs += TuSDKMediaRecoder.i(TuSDKMediaRecoder.this);
      TuSDKMediaRecoder.b(TuSDKMediaRecoder.this, paramAnonymousBufferInfo.presentationTimeUs);
      return true;
    }
    
    public void onVideoEncoderCodecConfig(long paramAnonymousLong, ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo) {}
  };
  private TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate r = new TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate()
  {
    public void onAudioEncoderStarted(MediaFormat paramAnonymousMediaFormat)
    {
      TuSDKMediaRecoder.this.addAudioTrack(paramAnonymousMediaFormat);
      if (TuSDKMediaRecoder.c(TuSDKMediaRecoder.this).hasVideoTrack()) {
        TuSDKMediaRecoder.c(TuSDKMediaRecoder.this).start();
      }
    }
    
    public void onAudioEncoderStoped() {}
    
    public void onAudioEncoderFrameDataAvailable(long paramAnonymousLong, ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSDKMediaRecoder.a(TuSDKMediaRecoder.this, paramAnonymousByteBuffer, paramAnonymousBufferInfo);
    }
    
    public void onAudioEncoderCodecConfig(long paramAnonymousLong, ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo) {}
  };
  
  private final SelesSurfaceEncoderInterface a()
  {
    if (this.d != null) {
      return this.d;
    }
    SelesSurfaceTextureEncoder local1 = new SelesSurfaceTextureEncoder()
    {
      protected void prepareEncoder(TuSDKVideoEncoderSetting paramAnonymousTuSDKVideoEncoderSetting)
      {
        TuSDKHardVideoDataEncoder localTuSDKHardVideoDataEncoder = new TuSDKHardVideoDataEncoder();
        if (localTuSDKHardVideoDataEncoder.initCodec(paramAnonymousTuSDKVideoEncoderSetting)) {
          this.mVideoEncoder = localTuSDKHardVideoDataEncoder;
        }
      }
    };
    local1.setVideoEncoderSetting(getVideoEncoderSetting());
    local1.setDelegate(this.q);
    this.d = local1;
    return this.d;
  }
  
  public void setSelesSurfaceEncoder(SelesSurfaceEncoderInterface paramSelesSurfaceEncoderInterface)
  {
    this.d = paramSelesSurfaceEncoderInterface;
    if (this.d != null) {
      this.d.setDelegate(this.q);
    }
  }
  
  private TuSDKAudioRecorderCore b()
  {
    if (this.h == null)
    {
      this.h = new TuSDKAudioRecorderCore(TuSDKAudioCaptureSetting.defaultCaptureSetting(), TuSDKAudioEncoderSetting.defaultEncoderSetting());
      this.h.getAudioEncoder().setDelegate(this.r);
    }
    return this.h;
  }
  
  public TuSDKMediaRecoder setDelegate(TuSDKMediaRecoderDelegate paramTuSDKMediaRecoderDelegate)
  {
    this.a = paramTuSDKMediaRecoderDelegate;
    return this;
  }
  
  public TuSDKMediaRecoderDelegate getDelegate()
  {
    return this.a;
  }
  
  public TuSDKMediaRecoder setVideoFirstSampleDelegate(TuSDKMediaRecoderVideoFirstSampleDelegate paramTuSDKMediaRecoderVideoFirstSampleDelegate)
  {
    this.b = paramTuSDKMediaRecoderVideoFirstSampleDelegate;
    return this;
  }
  
  public void updateFilter(SelesOutInput paramSelesOutInput)
  {
    if (paramSelesOutInput == null) {
      return;
    }
    paramSelesOutInput.addTarget(a(), 0);
  }
  
  public void updateFilter(FilterWrap paramFilterWrap)
  {
    if (paramFilterWrap == null) {
      return;
    }
    paramFilterWrap.addTarget(a(), 0);
  }
  
  public State getState()
  {
    return this.c;
  }
  
  private void a(State paramState)
  {
    if (this.c == paramState) {
      return;
    }
    this.c = paramState;
    if (this.a != null) {
      this.a.onMediaRecoderStateChanged(paramState);
    }
  }
  
  @TargetApi(17)
  public void startRecording(EGLContext paramEGLContext, SurfaceTexture paramSurfaceTexture)
  {
    if (paramSurfaceTexture == null)
    {
      TLog.e("startRecording : The surfaceTexture parameters cannot be null", new Object[0]);
      return;
    }
    if (isRecording()) {
      return;
    }
    e();
    c();
    startVideoDataEncoder(paramEGLContext, paramSurfaceTexture);
    a(State.Recording);
  }
  
  public void pauseRecording()
  {
    if (!isRecording()) {
      return;
    }
    if (this.d != null) {
      this.d.pausdRecording();
    }
    d();
    this.m = 0L;
    this.o = 0L;
    a(State.Paused);
  }
  
  public void stopRecording()
  {
    if (this.c == State.Idle) {
      return;
    }
    a(State.Saving);
    d();
    stopVideoDataEncoder();
    f();
    this.m = 0L;
    this.o = 0L;
    a(State.RecordCompleted);
    this.c = State.Idle;
  }
  
  public void cancelRecording()
  {
    if (this.c == State.Idle) {
      return;
    }
    d();
    stopVideoDataEncoder();
    f();
    if ((this.j != null) && (this.j.exists())) {
      this.j.delete();
    }
    this.m = 0L;
    this.o = 0L;
    a(State.Canceled);
    this.c = State.Idle;
  }
  
  public boolean isRecording()
  {
    if ((this.d == null) || (this.e == null)) {
      return false;
    }
    if (isPaused()) {
      return false;
    }
    return (this.d.isRecording()) && (this.e.isStarted());
  }
  
  public boolean isPaused()
  {
    if ((this.d == null) || (this.e == null)) {
      return false;
    }
    return this.c == State.Paused;
  }
  
  public void startVideoDataEncoder(EGLContext paramEGLContext, SurfaceTexture paramSurfaceTexture)
  {
    if (paramSurfaceTexture == null)
    {
      TLog.e("startVideoDataEncoder : The surfaceTexture parameters is null", new Object[0]);
      return;
    }
    a().startRecording(paramEGLContext, paramSurfaceTexture);
  }
  
  public void stopVideoDataEncoder()
  {
    if (this.d != null) {
      this.d.stopRecording();
    }
  }
  
  private boolean c()
  {
    if (h()) {
      return true;
    }
    TuSDKAudioRecorderCore localTuSDKAudioRecorderCore = b();
    if ((localTuSDKAudioRecorderCore == null) || (!localTuSDKAudioRecorderCore.isPrepared()))
    {
      TLog.e("Please open the audio permissions", new Object[0]);
      this.h = null;
      return false;
    }
    localTuSDKAudioRecorderCore.startRecording();
    return false;
  }
  
  private void d()
  {
    if (this.h != null) {
      this.h.stopRecording();
    }
  }
  
  private final TuSDKMediaRecoder e()
  {
    if (this.e == null)
    {
      this.e = TuSDKMovieWriter.create(getOutputFilePath().toString(), TuSDKMovieWriterInterface.MovieWriterOutputFormat.MPEG_4);
      this.e.setDelegate(this.p);
      this.e.setOrientationHint(this.f);
    }
    return this;
  }
  
  public final File getOutputFilePath()
  {
    if (this.j == null) {
      this.j = new File(TuSdk.getAppTempPath(), String.format("lsq_%s.mp4", new Object[] { StringHelper.timeStampString() }));
    }
    return this.j;
  }
  
  public final TuSDKMediaRecoder setOutputFilePath(File paramFile)
  {
    if (isRecording())
    {
      TLog.w("Please set the output path before starting the recording", new Object[0]);
      return this;
    }
    this.j = paramFile;
    return this;
  }
  
  private void f()
  {
    if (this.e != null)
    {
      this.e.stop();
      this.e = null;
    }
  }
  
  private TuSDKMovieWriter g()
  {
    if (this.e == null) {
      e();
    }
    return this.e;
  }
  
  private boolean h()
  {
    return this.k;
  }
  
  public TuSDKMediaRecoder setMute(boolean paramBoolean)
  {
    this.k = paramBoolean;
    return this;
  }
  
  public TuSDKMediaRecoder setEnableExternalAudio(boolean paramBoolean)
  {
    this.g = paramBoolean;
    if (this.g) {
      setMute(true);
    }
    return this;
  }
  
  public TuSDKMediaRecoder setOrientationHint(int paramInt)
  {
    this.f = paramInt;
    return this;
  }
  
  public TuSDKVideoEncoderSetting getVideoEncoderSetting()
  {
    if (this.i == null) {
      this.i = TuSDKVideoEncoderSetting.getDefaultRecordSetting();
    }
    return this.i;
  }
  
  public TuSDKMediaRecoder setVideoEncoderSetting(TuSDKVideoEncoderSetting paramTuSDKVideoEncoderSetting)
  {
    this.i = paramTuSDKVideoEncoderSetting;
    if (this.d != null) {
      this.d.setVideoEncoderSetting(paramTuSDKVideoEncoderSetting);
    }
    return this;
  }
  
  public void addAudioTrack(MediaFormat paramMediaFormat)
  {
    if ((h()) && (!this.g)) {
      return;
    }
    if (!g().canAddAudioTrack()) {
      return;
    }
    g().addAudioTrack(paramMediaFormat);
  }
  
  private boolean a(MediaCodec.BufferInfo paramBufferInfo)
  {
    if ((this.n > 0L) && (this.o == 0L)) {
      this.o = (this.n - paramBufferInfo.presentationTimeUs);
    }
    paramBufferInfo.presentationTimeUs += this.o;
    this.n = paramBufferInfo.presentationTimeUs;
    return true;
  }
  
  public void writeExternalAudioSampleData(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if ((!this.g) || (!h()))
    {
      TLog.e("Please set enableExternalAudio for true. ", new Object[0]);
      return;
    }
    a(paramByteBuffer, paramBufferInfo);
  }
  
  private void a(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if ((g() == null) || (!g().isStarted()) || (!g().hasAudioTrack()) || (!isRecording())) {
      return;
    }
    if (!a(paramBufferInfo)) {
      return;
    }
    g().writeAudioSampleData(paramByteBuffer, paramBufferInfo);
  }
  
  public TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate getAudioDataEncoderDelegate()
  {
    return this.r;
  }
  
  public static enum State
  {
    private State() {}
  }
  
  public static abstract interface TuSDKMediaRecoderVideoFirstSampleDelegate
  {
    public abstract void onFirstVideoSampleDataAlready(long paramLong);
  }
  
  public static abstract interface TuSDKMediaRecoderDelegate
  {
    public abstract void onMediaRecoderProgressChanged(float paramFloat);
    
    public abstract void onMediaRecoderStateChanged(TuSDKMediaRecoder.State paramState);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\video\preproc\filter\TuSDKMediaRecoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */