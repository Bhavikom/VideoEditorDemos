package org.lasque.tusdk.core.media.record;

import android.annotation.TargetApi;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import java.io.File;
import java.nio.ByteBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioRecord;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioRecord.TuSdkAudioRecordListener;
import org.lasque.tusdk.core.media.codec.audio.TuSdkMicRecord;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileRecorder;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileRecorder.TuSdkMediaFileRecorderProgress;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileRecorderImpl;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.sources.SelesWatermark;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.utils.JVMUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

@TargetApi(18)
public class TuSdkCameraRecorder
  implements TuSdkMediaRecordHub
{
  private TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus a = TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.UNINITIALIZED;
  private TuSdkMediaRecordHub.TuSdkMediaRecordHubListener b;
  private TuSdkAudioRecord c;
  private TuSdkAudioPitch d;
  private TuSdkAudioPitch e;
  private TuSdkMediaFileRecorder f;
  private TuSdkRecordSurface g;
  private SelesWatermark h;
  private MediaFormat i;
  private MediaFormat j;
  private TuSdkSurfaceRender k;
  private TuSdkAudioRender l;
  private final TuSdkFilterBridge m = new TuSdkFilterBridge();
  private boolean n = false;
  private float o = 1.0F;
  private float p = 1.0F;
  private boolean q;
  private TuSdkAudioRecord.TuSdkAudioRecordListener r = new TuSdkAudioRecord.TuSdkAudioRecordListener()
  {
    public void onAudioRecordOutputBuffer(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkCameraRecorder.o(TuSdkCameraRecorder.this) != null) {
        TuSdkCameraRecorder.o(TuSdkCameraRecorder.this).queueInputBuffer(paramAnonymousByteBuffer, paramAnonymousBufferInfo);
      }
    }
    
    public void onAudioRecordError(int paramAnonymousInt)
    {
      TLog.e("%s AudioRecordError  code  is :%s", new Object[] { "TuSdkCameraRecorder", Integer.valueOf(paramAnonymousInt) });
    }
  };
  private TuSdkAudioPitchSync s = new TuSdkAudioPitchSync()
  {
    public void syncAudioPitchOutputBuffer(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkMediaFileRecorder localTuSdkMediaFileRecorder = TuSdkCameraRecorder.b(TuSdkCameraRecorder.this);
      if (localTuSdkMediaFileRecorder == null) {
        return;
      }
      localTuSdkMediaFileRecorder.newFrameReadyWithAudio(paramAnonymousByteBuffer, paramAnonymousBufferInfo);
    }
    
    public void release() {}
  };
  private TuSdkAudioPitchSync t = new TuSdkAudioPitchSync()
  {
    public void syncAudioPitchOutputBuffer(ByteBuffer paramAnonymousByteBuffer, MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkCameraRecorder.m(TuSdkCameraRecorder.this) != null) {
        TuSdkCameraRecorder.m(TuSdkCameraRecorder.this).queueInputBuffer(paramAnonymousByteBuffer, paramAnonymousBufferInfo);
      }
    }
    
    public void release() {}
  };
  private TuSdkMediaFileRecorder.TuSdkMediaFileRecorderProgress u = new TuSdkMediaFileRecorder.TuSdkMediaFileRecorderProgress()
  {
    public void onProgress(long paramAnonymousLong, TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource)
    {
      if (TuSdkCameraRecorder.r(TuSdkCameraRecorder.this) != null) {
        TuSdkCameraRecorder.r(TuSdkCameraRecorder.this).onProgress(paramAnonymousLong, paramAnonymousTuSdkMediaDataSource);
      }
    }
    
    public void onCompleted(Exception paramAnonymousException, TuSdkMediaDataSource paramAnonymousTuSdkMediaDataSource, TuSdkMediaTimeline paramAnonymousTuSdkMediaTimeline)
    {
      if (TuSdkCameraRecorder.b(TuSdkCameraRecorder.this) != null)
      {
        TuSdkCameraRecorder.b(TuSdkCameraRecorder.this).disconnect();
        TuSdkCameraRecorder.a(TuSdkCameraRecorder.this, null);
      }
      TuSdkCameraRecorder.a(TuSdkCameraRecorder.this, TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.STOP);
      if (TuSdkCameraRecorder.r(TuSdkCameraRecorder.this) != null) {
        TuSdkCameraRecorder.r(TuSdkCameraRecorder.this).onCompleted(paramAnonymousException, paramAnonymousTuSdkMediaDataSource, paramAnonymousTuSdkMediaTimeline);
      }
    }
  };
  private GLSurfaceView.Renderer v = new GLSurfaceView.Renderer()
  {
    public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig)
    {
      GLES20.glDisable(2929);
      TuSdkCameraRecorder.this.initInGLThread();
    }
    
    public void onSurfaceChanged(GL10 paramAnonymousGL10, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      GLES20.glViewport(0, 0, paramAnonymousInt1, paramAnonymousInt2);
    }
    
    public void onDrawFrame(GL10 paramAnonymousGL10)
    {
      GLES20.glClear(16640);
      TuSdkCameraRecorder.this.newFrameReadyInGLThread();
    }
  };
  
  public void appendRecordSurface(TuSdkRecordSurface paramTuSdkRecordSurface)
  {
    if (this.a != TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.UNINITIALIZED)
    {
      TLog.w("%s appendRecordSurface need before start.", new Object[] { "TuSdkCameraRecorder" });
      return;
    }
    this.g = paramTuSdkRecordSurface;
    if (this.g != null) {
      this.g.addTarget(this.m, 0);
    }
  }
  
  public void setOutputVideoFormat(MediaFormat paramMediaFormat)
  {
    if (this.a != TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.UNINITIALIZED)
    {
      TLog.w("%s setOutputVideoFormat need before start.", new Object[] { "TuSdkCameraRecorder" });
      return;
    }
    this.i = paramMediaFormat;
  }
  
  public void setOutputAudioFormat(MediaFormat paramMediaFormat)
  {
    if (this.a != TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.UNINITIALIZED)
    {
      TLog.w("%s setOutputAudioFormat need before start.", new Object[] { "TuSdkCameraRecorder" });
      return;
    }
    this.j = paramMediaFormat;
  }
  
  public void setSurfaceRender(TuSdkSurfaceRender paramTuSdkSurfaceRender)
  {
    this.k = paramTuSdkSurfaceRender;
    this.m.setSurfaceDraw(this.k);
  }
  
  public void addTarget(SelesContext.SelesInput paramSelesInput, int paramInt)
  {
    this.m.addTarget(paramSelesInput, paramInt);
  }
  
  public void removeTarget(SelesContext.SelesInput paramSelesInput)
  {
    this.m.removeTarget(paramSelesInput);
  }
  
  public void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    this.l = paramTuSdkAudioRender;
  }
  
  public void setRecordListener(TuSdkMediaRecordHub.TuSdkMediaRecordHubListener paramTuSdkMediaRecordHubListener)
  {
    this.b = paramTuSdkMediaRecordHubListener;
  }
  
  public void setWatermark(SelesWatermark paramSelesWatermark)
  {
    this.h = paramSelesWatermark;
  }
  
  private void a(TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus paramTuSdkMediaRecordHubStatus)
  {
    this.a = paramTuSdkMediaRecordHubStatus;
    if (this.b != null) {
      this.b.onStatusChanged(paramTuSdkMediaRecordHubStatus, this);
    }
  }
  
  public TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus getState()
  {
    return this.a;
  }
  
  private boolean a()
  {
    switch (8.a[this.a.ordinal()])
    {
    case 1: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
    case 6: 
      return false;
    }
    return true;
  }
  
  private boolean b()
  {
    switch (8.a[this.a.ordinal()])
    {
    case 1: 
    case 2: 
    case 4: 
    case 5: 
    case 6: 
      return true;
    }
    return false;
  }
  
  public void release()
  {
    if (this.a == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.RELEASED) {
      return;
    }
    a(TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.RELEASED);
    if (this.f != null)
    {
      this.f.release();
      this.f = null;
    }
    c();
    TLog.e("[debug] %s ============ release record ", new Object[] { "TuSdkCameraRecorder" });
    if (this.m != null) {
      this.m.destroy();
    }
    JVMUtils.runGC();
  }
  
  private void c()
  {
    if (this.c != null)
    {
      this.c.stop();
      this.c.release();
      this.c = null;
    }
    if (this.d != null)
    {
      this.d.reset();
      this.d.release();
      this.d = null;
    }
    if (this.e != null)
    {
      this.e.reset();
      this.e.release();
      this.e = null;
    }
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public boolean start(final File paramFile)
  {
    switch (8.a[this.a.ordinal()])
    {
    case 1: 
    case 2: 
      break;
    default: 
      TLog.w("%s start had incorrect status: %s", new Object[] { "TuSdkCameraRecorder", this.a });
      return false;
    }
    if (this.f != null)
    {
      TLog.w("%s start need wait stop compeleted.", new Object[] { "TuSdkCameraRecorder" });
      return false;
    }
    if (paramFile == null)
    {
      TLog.w("%s start need put outputFile.", new Object[] { "TuSdkCameraRecorder" });
      return false;
    }
    if (paramFile.exists())
    {
      TLog.w("%s start with outputFile exists.", new Object[] { "TuSdkCameraRecorder" });
      return false;
    }
    if (this.g == null)
    {
      TLog.w("%s start need appendRecordSurface first.", new Object[] { "TuSdkCameraRecorder" });
      return false;
    }
    if (this.i == null)
    {
      TLog.w("%s start need setOutputVideoFormat first.", new Object[] { "TuSdkCameraRecorder" });
      return false;
    }
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        TuSdkCameraRecorder.a(TuSdkCameraRecorder.this, new TuSdkMediaFileRecorderImpl());
        TuSdkCameraRecorder.b(TuSdkCameraRecorder.this).setOutputVideoFormat(TuSdkCameraRecorder.a(TuSdkCameraRecorder.this));
        TuSdkCameraRecorder.b(TuSdkCameraRecorder.this).setOutputAudioFormat(TuSdkCameraRecorder.c(TuSdkCameraRecorder.this));
        TuSdkCameraRecorder.b(TuSdkCameraRecorder.this).setSurfaceRender(TuSdkCameraRecorder.d(TuSdkCameraRecorder.this));
        TuSdkCameraRecorder.b(TuSdkCameraRecorder.this).setAudioRender(TuSdkCameraRecorder.e(TuSdkCameraRecorder.this));
        TuSdkCameraRecorder.b(TuSdkCameraRecorder.this).changeSpeed(TuSdkCameraRecorder.f(TuSdkCameraRecorder.this));
        TuSdkCameraRecorder.b(TuSdkCameraRecorder.this).setOutputFilePath(paramFile.getAbsolutePath());
        TuSdkCameraRecorder.b(TuSdkCameraRecorder.this).setRecordProgress(TuSdkCameraRecorder.g(TuSdkCameraRecorder.this));
        TuSdkCameraRecorder.b(TuSdkCameraRecorder.this).setFilterBridge(TuSdkCameraRecorder.h(TuSdkCameraRecorder.this));
        if (TuSdkCameraRecorder.i(TuSdkCameraRecorder.this) != null) {
          TuSdkCameraRecorder.b(TuSdkCameraRecorder.this).setWatermark(TuSdkCameraRecorder.i(TuSdkCameraRecorder.this));
        }
        if (TuSdkCameraRecorder.b(TuSdkCameraRecorder.this).getOutputAudioInfo() != null)
        {
          TuSdkCameraRecorder.a(TuSdkCameraRecorder.this, new TuSdkMicRecord());
          TuSdkCameraRecorder.j(TuSdkCameraRecorder.this).setAudioInfo(TuSdkCameraRecorder.b(TuSdkCameraRecorder.this).getOutputAudioInfo());
          TuSdkCameraRecorder.j(TuSdkCameraRecorder.this).setListener(TuSdkCameraRecorder.k(TuSdkCameraRecorder.this));
          TuSdkCameraRecorder.j(TuSdkCameraRecorder.this).startRecording();
          TuSdkCameraRecorder.a(TuSdkCameraRecorder.this, new TuSdkAudioPitchHardImpl(TuSdkCameraRecorder.b(TuSdkCameraRecorder.this).getOutputAudioInfo()));
          TuSdkCameraRecorder.m(TuSdkCameraRecorder.this).changePitch(TuSdkCameraRecorder.l(TuSdkCameraRecorder.this));
          TuSdkCameraRecorder.m(TuSdkCameraRecorder.this).setMediaSync(TuSdkCameraRecorder.n(TuSdkCameraRecorder.this));
          TuSdkCameraRecorder.b(TuSdkCameraRecorder.this, new TuSdkAudioPitchHardImpl(TuSdkCameraRecorder.b(TuSdkCameraRecorder.this).getOutputAudioInfo()));
          TuSdkCameraRecorder.o(TuSdkCameraRecorder.this).changeSpeed(TuSdkCameraRecorder.f(TuSdkCameraRecorder.this));
          TuSdkCameraRecorder.o(TuSdkCameraRecorder.this).setMediaSync(TuSdkCameraRecorder.p(TuSdkCameraRecorder.this));
        }
        TuSdkCameraRecorder.a(TuSdkCameraRecorder.this, TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START);
        if (TuSdkCameraRecorder.q(TuSdkCameraRecorder.this)) {
          TuSdkCameraRecorder.this.pause();
        }
      }
    });
    return true;
  }
  
  public void stop()
  {
    switch (8.a[this.a.ordinal()])
    {
    case 4: 
    case 7: 
      break;
    default: 
      TLog.w("%s stop had incorrect status: %s", new Object[] { "TuSdkCameraRecorder", this.a });
      return;
    }
    TLog.e("[debug] %s ============ stop record ", new Object[] { "TuSdkCameraRecorder" });
    if (this.f == null)
    {
      a(TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.STOP);
    }
    else
    {
      a(TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.PREPARE_STOP);
      this.f.stopRecord();
    }
    c();
  }
  
  public boolean pause()
  {
    this.q = true;
    if (this.a != TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START_RECORD)
    {
      TLog.w("%s pause had incorrect status: %s", new Object[] { "TuSdkCameraRecorder", this.a });
      return false;
    }
    a(TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.PAUSE_RECORD);
    if (this.f != null) {
      this.f.pauseRecord();
    }
    if (this.c != null) {
      this.c.stop();
    }
    this.q = false;
    return true;
  }
  
  public boolean resume()
  {
    if (this.a != TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.PAUSE_RECORD)
    {
      TLog.w("%s resume had incorrect status: %s", new Object[] { "TuSdkCameraRecorder", this.a });
      return false;
    }
    if (this.c != null) {
      this.c.startRecording();
    }
    if (this.f != null) {
      this.f.resumeRecord();
    }
    a(TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START_RECORD);
    return true;
  }
  
  public void reset()
  {
    if (!b())
    {
      TLog.w("%s reset had incorrect status: %s", new Object[] { "TuSdkCameraRecorder", this.a });
      return;
    }
    if (this.d != null) {
      this.d.reset();
    }
    if (this.e != null) {
      this.e.reset();
    }
    if (this.f != null) {
      this.f.changeSpeed(1.0F);
    }
    if (this.f != null) {
      this.f.release();
    }
    if (this.c != null) {
      this.c.release();
    }
    if (this.e != null) {
      this.e.release();
    }
    if (this.d != null) {
      this.d.reset();
    }
    this.f = null;
    this.c = null;
    this.d = null;
    this.e = null;
    a(TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.UNINITIALIZED);
  }
  
  public void changeSpeed(float paramFloat)
  {
    if ((paramFloat <= 0.0F) || (this.o == paramFloat)) {
      return;
    }
    if (!b())
    {
      TLog.w("%s changeSpeed had incorrect status: %s", new Object[] { "TuSdkCameraRecorder", this.a });
      return;
    }
    this.o = paramFloat;
    if (this.e != null) {
      this.e.changeSpeed(this.o);
    }
    if (this.f != null) {
      this.f.changeSpeed(this.o);
    }
  }
  
  public void changePitch(float paramFloat)
  {
    if ((this.p <= 0.0F) || (this.p == paramFloat)) {
      return;
    }
    if (!b())
    {
      TLog.w("%s changePitch had incorrect status: %s", new Object[] { "TuSdkCameraRecorder", this.a });
      return;
    }
    this.p = paramFloat;
    if (this.d != null) {
      this.d.changePitch(this.p);
    }
    if (this.f != null) {
      this.f.changeSpeed(1.0F);
    }
  }
  
  public GLSurfaceView.Renderer getExtenalRenderer()
  {
    return this.v;
  }
  
  public void initInGLThread()
  {
    if (this.g == null) {
      return;
    }
    this.g.initInGLThread();
  }
  
  public void newFrameReadyInGLThread()
  {
    if (this.g == null) {
      return;
    }
    long l1 = System.nanoTime();
    this.g.updateSurfaceTexImage();
    if (this.n) {
      return;
    }
    this.n = true;
    this.g.newFrameReadyInGLThread(l1);
    a(this.f, l1);
    this.n = false;
  }
  
  private void a(final TuSdkMediaFileRecorder paramTuSdkMediaFileRecorder, final long paramLong)
  {
    if ((paramTuSdkMediaFileRecorder == null) || (!a())) {
      return;
    }
    if (this.a == TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START)
    {
      a(TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.PREPARE_RECORD);
      final EGLContext localEGLContext = EGL14.eglGetCurrentContext();
      ThreadHelper.runThread(new Runnable()
      {
        public void run()
        {
          paramTuSdkMediaFileRecorder.startRecord(localEGLContext);
          paramTuSdkMediaFileRecorder.newFrameReadyInGLThread(paramLong);
          TuSdkCameraRecorder.a(TuSdkCameraRecorder.this, TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus.START_RECORD);
          if (TuSdkCameraRecorder.q(TuSdkCameraRecorder.this)) {
            TuSdkCameraRecorder.this.pause();
          }
        }
      });
      return;
    }
    paramTuSdkMediaFileRecorder.newFrameReadyInGLThread(paramLong);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\record\TuSdkCameraRecorder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */