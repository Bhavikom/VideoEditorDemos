package org.lasque.tusdk.api.transcoder;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.media.MediaCodec.BufferInfo;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.view.Surface;
import java.io.File;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.lasque.tusdk.api.video.preproc.filter.TuSDKMediaRecoder;
import org.lasque.tusdk.api.video.preproc.filter.TuSDKMediaRecoder.State;
import org.lasque.tusdk.api.video.preproc.filter.TuSDKMediaRecoder.TuSDKMediaRecoderDelegate;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.decoder.TuSDKMediaDecoder.TuSDKMediaDecoderError;
import org.lasque.tusdk.core.decoder.TuSDKMovieSurfaceDecoder;
import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import org.lasque.tusdk.core.delegate.TuSDKVideoSaveDelegate;
import org.lasque.tusdk.core.delegate.TuSDKVideoSurfaceDecodeDelegate;
import org.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoder;
import org.lasque.tusdk.core.encoder.video.TuSDKHardVideoDataEncoderInterface;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting.VideoQuality;
import org.lasque.tusdk.core.gl.EGLConfigAttrs;
import org.lasque.tusdk.core.gl.EGLContextAttrs;
import org.lasque.tusdk.core.gl.EglCore;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.SelesFramebuffer.SelesFramebufferMode;
import org.lasque.tusdk.core.seles.SelesFramebufferCache;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.sources.SelesOutput;
import org.lasque.tusdk.core.seles.video.SelesSyncSurfaceTextureEncoder;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.video.TuSDKVideoResult;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

@TargetApi(16)
public class TuSDKMovieTranscoder
  extends SelesOutput
{
  private static final float[] a = { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
  private static final float[] b = { 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
  protected ImageOrientation mOutputRotation;
  protected ImageOrientation mInputRotation;
  private boolean c;
  private FloatBuffer d;
  private FloatBuffer e;
  private SelesGLProgram f;
  private int g;
  private int h;
  private int i;
  private EglCore j;
  private SurfaceTexture k;
  private Surface l;
  private int m = -1;
  private final Queue<Runnable> n;
  private boolean o;
  private TuSDKMediaDataSource p;
  private TuSDKMovieSurfaceDecoder q;
  private TuSdkTimeRange r;
  private TuSDKVideoInfo s;
  private SelesSyncSurfaceTextureEncoder t;
  private TuSDKVideoEncoderSetting u;
  private TuSDKMediaRecoder v;
  private File w;
  private TuSDKVideoSaveDelegate x;
  private SelesVerticeCoordinateCorpBuilder y = new SelesVerticeCoordinateCropBuilderImpl(false);
  private TuSDKVideoSurfaceDecodeDelegate z = new TuSDKVideoSurfaceDecodeDelegate()
  {
    public void onDecoderError(TuSDKMediaDecoder.TuSDKMediaDecoderError paramAnonymousTuSDKMediaDecoderError)
    {
      if (TuSDKMovieTranscoder.e(TuSDKMovieTranscoder.this) != null) {
        TuSDKMovieTranscoder.e(TuSDKMovieTranscoder.this).onSaveResult(null);
      }
    }
    
    public void onVideoInfoReady(TuSDKVideoInfo paramAnonymousTuSDKVideoInfo)
    {
      TuSDKMovieTranscoder.a(TuSDKMovieTranscoder.this, paramAnonymousTuSDKVideoInfo);
      TuSDKMovieTranscoder.this.mInputRotation = paramAnonymousTuSDKVideoInfo.videoOrientation;
      TuSDKMovieTranscoder.a(TuSDKMovieTranscoder.this, TuSdkSize.create(paramAnonymousTuSDKVideoInfo.width, paramAnonymousTuSDKVideoInfo.height));
      TuSDKMovieTranscoder.f(TuSDKMovieTranscoder.this).setPreCropRect(paramAnonymousTuSDKVideoInfo.codecCrop);
    }
    
    public void onProgressChanged(final long paramAnonymousLong, float paramAnonymousFloat)
    {
      if (TuSDKMovieTranscoder.e(TuSDKMovieTranscoder.this) == null) {
        return;
      }
      ThreadHelper.post(new Runnable()
      {
        public void run()
        {
          if ((TuSDKMovieTranscoder.this.getTimeRange() != null) && (TuSDKMovieTranscoder.this.getTimeRange().isValid()))
          {
            float f = Math.min(1.0F, (float)paramAnonymousLong / (float)TuSDKMovieTranscoder.this.getTimeRange().getEndTimeUS());
            TuSDKMovieTranscoder.e(TuSDKMovieTranscoder.this).onProgressChaned(f);
          }
          else
          {
            TuSDKMovieTranscoder.e(TuSDKMovieTranscoder.this).onProgressChaned(Math.min(1.0F, this.b));
          }
        }
      });
    }
    
    public void onVideoDecoderNewFrameAvailable(int paramAnonymousInt, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSDKMovieTranscoder.this.runPendingOnDrawTasks();
      if ((TuSDKMovieTranscoder.this.getTimeRange() != null) && (TuSDKMovieTranscoder.this.getTimeRange().isValid()))
      {
        if (paramAnonymousBufferInfo.presentationTimeUs < TuSDKMovieTranscoder.this.getTimeRange().getStartTimeUS()) {
          return;
        }
        if (paramAnonymousBufferInfo.presentationTimeUs >= TuSDKMovieTranscoder.this.getTimeRange().getEndTimeUS())
        {
          TuSDKMovieTranscoder.g(TuSDKMovieTranscoder.this).stop();
          onDecoderComplete();
          return;
        }
      }
      TuSDKMovieTranscoder.h(TuSDKMovieTranscoder.this).updateTexImage();
      TuSDKMovieTranscoder.d(TuSDKMovieTranscoder.this, TuSDKMovieTranscoder.i(TuSDKMovieTranscoder.this));
      long l = TuSDKMovieTranscoder.g(TuSDKMovieTranscoder.this).getComputePresentationTimeUs();
      TuSDKMovieTranscoder.this.updateTargetsForVideoCameraUsingCacheTexture(l);
    }
    
    public void onDecoderComplete()
    {
      TuSDKMovieTranscoder.this.stopRecording();
    }
  };
  private TuSDKMediaRecoder.TuSDKMediaRecoderDelegate A = new TuSDKMediaRecoder.TuSDKMediaRecoderDelegate()
  {
    public void onMediaRecoderProgressChanged(float paramAnonymousFloat) {}
    
    public void onMediaRecoderStateChanged(TuSDKMediaRecoder.State paramAnonymousState)
    {
      if ((TuSDKMovieTranscoder.e(TuSDKMovieTranscoder.this) != null) && (paramAnonymousState == TuSDKMediaRecoder.State.RecordCompleted))
      {
        TuSDKVideoResult localTuSDKVideoResult = new TuSDKVideoResult();
        localTuSDKVideoResult.videoPath = TuSDKMovieTranscoder.j(TuSDKMovieTranscoder.this).getOutputFilePath();
        localTuSDKVideoResult.videoInfo = TuSDKMovieTranscoder.k(TuSDKMovieTranscoder.this);
        TuSDKMovieTranscoder.e(TuSDKMovieTranscoder.this).onSaveResult(localTuSDKVideoResult);
      }
    }
  };
  
  public TuSDKMovieTranscoder(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    setVideoDataSource(paramTuSDKMediaDataSource);
    this.m = d();
    this.k = new SurfaceTexture(this.m);
    this.l = new Surface(this.k);
    this.c = true;
    this.n = new LinkedList();
    this.mOutputRotation = ImageOrientation.Up;
    this.mInputRotation = ImageOrientation.Up;
  }
  
  public TuSDKMovieTranscoder setSaveDelegate(TuSDKVideoSaveDelegate paramTuSDKVideoSaveDelegate)
  {
    this.x = paramTuSDKVideoSaveDelegate;
    return this;
  }
  
  public TuSDKMovieTranscoder setVideoDataSource(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    if (isProcessing())
    {
      TLog.d("Please set 'moviePath' before processing", new Object[0]);
      return this;
    }
    this.p = paramTuSDKMediaDataSource;
    return this;
  }
  
  public TuSDKMovieTranscoder setOutputFile(File paramFile)
  {
    this.w = paramFile;
    return this;
  }
  
  public File getOutputFile()
  {
    if (this.w == null) {
      this.w = new File(TuSdk.getAppTempPath(), String.format("lsq_%s.mp4", new Object[] { StringHelper.timeStampString() }));
    }
    return this.w;
  }
  
  protected final TuSdkSize getOutputSize()
  {
    if (this.u.videoSize != null) {
      return this.u.videoSize;
    }
    if ((this.mInputTextureSize == null) || (getVideoInfo() == null)) {
      return TuSdkSize.create(0);
    }
    TuSdkSize localTuSdkSize = this.mInputTextureSize;
    if (this.mInputTextureSize.minSide() >= 1080) {
      localTuSdkSize = TuSdkSize.create((int)(this.mInputTextureSize.width * 0.5D), (int)(this.mInputTextureSize.height * 0.5D));
    }
    if (this.mInputRotation.isTransposed()) {
      localTuSdkSize = TuSdkSize.create(localTuSdkSize.height, localTuSdkSize.width);
    }
    this.u.videoSize = localTuSdkSize;
    return this.u.videoSize;
  }
  
  protected TuSDKVideoEncoderSetting getVideoEncoderSetting()
  {
    if (this.u == null)
    {
      this.u = TuSDKVideoEncoderSetting.getDefaultRecordSetting();
      this.u.videoSize = getOutputSize();
    }
    return this.u;
  }
  
  public final TuSDKMovieTranscoder setVideoEncoderSetting(TuSDKVideoEncoderSetting paramTuSDKVideoEncoderSetting)
  {
    this.u = paramTuSDKVideoEncoderSetting;
    if ((!SdkValid.shared.videoEditorResolutionEnabled()) && (paramTuSDKVideoEncoderSetting != null) && (!paramTuSDKVideoEncoderSetting.videoSize.equals(TuSDKVideoEncoderSetting.getDefaultRecordSetting().videoSize)))
    {
      TLog.e("You are not allowed to change video editor resolution, please see http://tusdk.com", new Object[0]);
      paramTuSDKVideoEncoderSetting.videoSize = TuSDKVideoEncoderSetting.getDefaultRecordSetting().videoSize;
    }
    if ((!SdkValid.shared.videoEditorBitrateEnabled()) && (paramTuSDKVideoEncoderSetting != null) && (!paramTuSDKVideoEncoderSetting.videoQuality.equals(TuSDKVideoEncoderSetting.getDefaultRecordSetting().videoQuality)))
    {
      TLog.e("You are not allowed to change video editor bitrate, please see http://tusdk.com", new Object[0]);
      paramTuSDKVideoEncoderSetting.videoQuality = TuSDKVideoEncoderSetting.getDefaultRecordSetting().videoQuality;
    }
    return this;
  }
  
  private void a()
  {
    if (this.f != null) {
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        TuSDKMovieTranscoder.a(TuSDKMovieTranscoder.this, new EglCore());
        boolean bool = TuSDKMovieTranscoder.a(TuSDKMovieTranscoder.this).createGLESWithSurface(new EGLConfigAttrs(), new EGLContextAttrs(), new SurfaceTexture(1));
        if (!bool)
        {
          TLog.d("createGLESWithSurface failed", new Object[0]);
          return;
        }
        SelesContext.createEGLContext(SelesContext.currentEGLContext());
        TuSDKMovieTranscoder.a(TuSDKMovieTranscoder.this, SelesContext.program("attribute vec4 position;attribute vec4 inputTextureCoordinate;varying vec2 textureCoordinate;void main(){    gl_Position = position;    textureCoordinate = inputTextureCoordinate.xy;}", "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 textureCoordinate;\nuniform samplerExternalOES inputImageTexture;\nvoid main() {\n    gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}\n"));
        if (!TuSDKMovieTranscoder.b(TuSDKMovieTranscoder.this).isInitialized())
        {
          TuSDKMovieTranscoder.b(TuSDKMovieTranscoder.this).addAttribute("position");
          TuSDKMovieTranscoder.b(TuSDKMovieTranscoder.this).addAttribute("inputTextureCoordinate");
          if (!TuSDKMovieTranscoder.b(TuSDKMovieTranscoder.this).link())
          {
            TLog.i("Program link log: %s", new Object[] { TuSDKMovieTranscoder.b(TuSDKMovieTranscoder.this).getProgramLog() });
            TLog.i("Fragment shader compile log: %s", new Object[] { TuSDKMovieTranscoder.b(TuSDKMovieTranscoder.this).getFragmentShaderLog() });
            TLog.i("Vertex link log: %s", new Object[] { TuSDKMovieTranscoder.b(TuSDKMovieTranscoder.this).getVertexShaderLog() });
            TuSDKMovieTranscoder.a(TuSDKMovieTranscoder.this, null);
            TLog.e("Filter shader link failed: %s", new Object[] { getClass() });
            return;
          }
        }
        TuSDKMovieTranscoder.a(TuSDKMovieTranscoder.this, TuSDKMovieTranscoder.b(TuSDKMovieTranscoder.this).attributeIndex("position"));
        TuSDKMovieTranscoder.b(TuSDKMovieTranscoder.this, TuSDKMovieTranscoder.b(TuSDKMovieTranscoder.this).attributeIndex("inputTextureCoordinate"));
        TuSDKMovieTranscoder.c(TuSDKMovieTranscoder.this, TuSDKMovieTranscoder.b(TuSDKMovieTranscoder.this).uniformIndex("inputImageTexture"));
        SelesContext.setActiveShaderProgram(TuSDKMovieTranscoder.b(TuSDKMovieTranscoder.this));
        GLES20.glEnableVertexAttribArray(TuSDKMovieTranscoder.c(TuSDKMovieTranscoder.this));
        GLES20.glEnableVertexAttribArray(TuSDKMovieTranscoder.d(TuSDKMovieTranscoder.this));
      }
    });
  }
  
  private FloatBuffer b()
  {
    if (this.d == null) {
      this.d = SelesFilter.buildBuffer(a);
    }
    return this.d;
  }
  
  private FloatBuffer c()
  {
    if (this.e == null) {
      this.e = SelesFilter.buildBuffer(b);
    }
    return this.e;
  }
  
  public void addTarget(SelesContext.SelesInput paramSelesInput, int paramInt)
  {
    super.addTarget(paramSelesInput, paramInt);
    if (paramSelesInput != null) {
      paramSelesInput.setInputRotation(this.mOutputRotation, paramInt);
    }
  }
  
  private int d()
  {
    int[] arrayOfInt = new int[1];
    GLES20.glGenTextures(1, arrayOfInt, 0);
    GLES20.glBindTexture(36197, arrayOfInt[0]);
    GLES20.glTexParameteri(36197, 10241, 9729);
    GLES20.glTexParameteri(36197, 10240, 9729);
    GLES20.glTexParameteri(36197, 10242, 33071);
    GLES20.glTexParameteri(36197, 10243, 33071);
    return arrayOfInt[0];
  }
  
  private SelesFramebuffer e()
  {
    SelesFramebuffer localSelesFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE, getOutputSize());
    localSelesFramebuffer.disableReferenceCounting();
    return localSelesFramebuffer;
  }
  
  private void a(int paramInt)
  {
    this.y.calculate(this.mInputTextureSize, this.mInputRotation, b(), c());
    SelesContext.setActiveShaderProgram(this.f);
    f();
    GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
    GLES20.glClear(16384);
    GLES20.glActiveTexture(33986);
    GLES20.glBindTexture(36197, paramInt);
    GLES20.glUniform1i(this.i, 2);
    GLES20.glVertexAttribPointer(this.g, 2, 5126, false, 0, b());
    GLES20.glVertexAttribPointer(this.h, 2, 5126, false, 0, c());
    GLES20.glDrawArrays(5, 0, 4);
    GLES20.glBindTexture(36197, 0);
    GLES20.glBindFramebuffer(36160, 0);
  }
  
  private void f()
  {
    if ((this.c) || (this.mOutputFramebuffer == null))
    {
      this.mOutputFramebuffer = e();
      this.c = false;
    }
    this.mOutputFramebuffer.activateFramebuffer();
  }
  
  protected void updateTargetsForVideoCameraUsingCacheTexture(long paramLong)
  {
    int i1 = 0;
    int i2 = this.mTargets.size();
    SelesContext.SelesInput localSelesInput;
    int i3;
    while (i1 < i2)
    {
      localSelesInput = (SelesContext.SelesInput)this.mTargets.get(i1);
      if (localSelesInput.isEnabled())
      {
        i3 = ((Integer)this.mTargetTextureIndices.get(i1)).intValue();
        localSelesInput.setInputRotation(this.mOutputRotation, i3);
        if (localSelesInput != getTargetToIgnoreForUpdates())
        {
          localSelesInput.setInputSize(getOutputSize(), i3);
          localSelesInput.setCurrentlyReceivingMonochromeInput(localSelesInput.wantsMonochromeInput());
        }
        localSelesInput.setInputFramebuffer(this.mOutputFramebuffer, i3);
      }
      i1++;
    }
    i1 = 0;
    i2 = this.mTargets.size();
    while (i1 < i2)
    {
      localSelesInput = (SelesContext.SelesInput)this.mTargets.get(i1);
      if ((localSelesInput.isEnabled()) && (localSelesInput != getTargetToIgnoreForUpdates()))
      {
        i3 = ((Integer)this.mTargetTextureIndices.get(i1)).intValue();
        localSelesInput.newFrameReady(paramLong, i3);
      }
      i1++;
    }
  }
  
  private void g()
  {
    if (k() == null) {
      return;
    }
    addTarget(this.t);
  }
  
  public TuSdkTimeRange getTimeRange()
  {
    return this.r;
  }
  
  public void setTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
  {
    this.r = paramTuSdkTimeRange;
  }
  
  private TuSDKMovieSurfaceDecoder h()
  {
    if (this.q == null)
    {
      this.q = new TuSDKMovieSurfaceDecoder(this.p);
      this.q.setLooping(false);
      this.q.setVideoDelegate(this.z);
    }
    return this.q;
  }
  
  private void i()
  {
    SelesSyncSurfaceTextureEncoder local2 = new SelesSyncSurfaceTextureEncoder(this.j)
    {
      protected void prepareEncoder(TuSDKVideoEncoderSetting paramAnonymousTuSDKVideoEncoderSetting)
      {
        this.mVideoEncoder = new TuSDKHardVideoDataEncoder();
        this.mVideoEncoder.initCodec(paramAnonymousTuSDKVideoEncoderSetting);
      }
    };
    TuSDKVideoEncoderSetting localTuSDKVideoEncoderSetting = getVideoEncoderSetting();
    if (localTuSDKVideoEncoderSetting.videoSize == null) {
      localTuSDKVideoEncoderSetting.videoSize = getOutputSize();
    }
    if (localTuSDKVideoEncoderSetting.videoQuality == null) {
      localTuSDKVideoEncoderSetting.videoQuality = TuSDKVideoEncoderSetting.VideoQuality.RECORD_MEDIUM2.setBitrate(getVideoInfo().bitrate).setFps(Math.max(20, getVideoInfo().fps));
    }
    localTuSDKVideoEncoderSetting.bitrateMode = getVideoInfo().getBestBitrateMode();
    if (getVideoInfo().profile >= 66) {
      localTuSDKVideoEncoderSetting.videoQuality.setBitrate(localTuSDKVideoEncoderSetting.videoQuality.getBitrate() * 2);
    }
    local2.setVideoEncoderSetting(localTuSDKVideoEncoderSetting);
    this.t = local2;
  }
  
  private void j()
  {
    if (this.q != null)
    {
      this.q.setVideoDelegate(null);
      this.q.destroy();
      this.q = null;
    }
  }
  
  public TuSDKVideoInfo getVideoInfo()
  {
    return this.s;
  }
  
  protected void runPendingOnDrawTasks()
  {
    a(this.n);
  }
  
  protected boolean isOnDrawTasksEmpty()
  {
    boolean bool = false;
    synchronized (this.n)
    {
      bool = this.n.isEmpty();
    }
    return bool;
  }
  
  private void a(Queue<Runnable> paramQueue)
  {
    synchronized (paramQueue)
    {
      while (!paramQueue.isEmpty()) {
        ((Runnable)paramQueue.poll()).run();
      }
    }
  }
  
  protected void runOnDraw(Runnable paramRunnable)
  {
    this.n.add(paramRunnable);
  }
  
  protected void onDestroy()
  {
    j();
    SdkValid.shared.checkAppAuth();
  }
  
  private TuSDKMediaRecoder k()
  {
    if (this.v == null)
    {
      this.v = new TuSDKMediaRecoder();
      this.v.setSelesSurfaceEncoder(this.t);
      this.v.setVideoEncoderSetting(getVideoEncoderSetting()).setOutputFilePath(getOutputFile()).setDelegate(this.A).setMute(true);
    }
    return this.v;
  }
  
  protected boolean isProcessing()
  {
    return this.o;
  }
  
  @TargetApi(17)
  public void startRecording()
  {
    if (this.o) {
      return;
    }
    this.o = true;
    a();
    runOnDraw(new Runnable()
    {
      public void run()
      {
        TuSDKMovieTranscoder.l(TuSDKMovieTranscoder.this);
        TuSDKMovieTranscoder.m(TuSDKMovieTranscoder.this);
        TuSDKMovieTranscoder.n(TuSDKMovieTranscoder.this).startRecording(EGL14.eglGetCurrentContext(), TuSDKMovieTranscoder.h(TuSDKMovieTranscoder.this));
      }
    });
    h().prepare(this.l, null, false);
    h().start();
  }
  
  public void stopRecording()
  {
    this.o = false;
    if (this.q != null) {
      this.q.stop();
    }
    if (this.v != null) {
      this.v.stopRecording();
    }
  }
  
  public void cancelRecording()
  {
    this.o = false;
    if (this.q != null) {
      this.q.stop();
    }
    if (this.v != null) {
      this.v.cancelRecording();
    }
  }
  
  public boolean isRecording()
  {
    return (this.v != null) && (this.v.isRecording());
  }
  
  public boolean isPaused()
  {
    return (this.v != null) && (this.v.isPaused());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\transcoder\TuSDKMovieTranscoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */