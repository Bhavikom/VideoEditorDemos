package org.lasque.tusdk.core.media.codec.suit;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.MediaCodec.BufferInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import org.lasque.tusdk.core.api.extend.TuSdkMediaDirectorPlayerListener;
import org.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkDecoderListener;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFileDecoder;
import org.lasque.tusdk.core.media.codec.exception.TuSdkTaskExitException;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorPlayerSync;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorPlayerSync.TuSdkDirectorPlayerStateCallback;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorPlayerSync.TuSdkEffectFrameCalc;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

public class TuSdkMediaFileDirectorPlayerImpl
  implements TuSdkMediaFileDirectorPlayer
{
  private int a = -1;
  private final TuSdkMediaFileDirectorPlayerSync b = new TuSdkMediaFileDirectorPlayerSync();
  private TuSdkSurfaceDraw c;
  private TuSdkAudioRender d;
  private TuSdkMediaFileDecoder e;
  private SelesSurfaceReceiver f;
  private TuSdkFilterBridge g = new TuSdkFilterBridge();
  private TuSdkMediaPlayerListener h;
  private TuSdkMediaDataSource i;
  private boolean j;
  private long k = -1L;
  private int l = 0;
  private GLSurfaceView.Renderer m = new GLSurfaceView.Renderer()
  {
    public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig)
    {
      GLES20.glDisable(2929);
      TuSdkMediaFileDirectorPlayerImpl.this.initInGLThread();
    }
    
    public void onSurfaceChanged(GL10 paramAnonymousGL10, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      GLES20.glViewport(0, 0, paramAnonymousInt1, paramAnonymousInt2);
    }
    
    public void onDrawFrame(GL10 paramAnonymousGL10)
    {
      GLES20.glClear(16640);
      TuSdkMediaFileDirectorPlayerImpl.this.newFrameReadyInGLThread();
    }
  };
  private TuSdkDecoderListener n = new TuSdkDecoderListener()
  {
    private long b = -1L;
    
    public void onDecoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if ((TuSdkMediaFileDirectorPlayerImpl.b(TuSdkMediaFileDirectorPlayerImpl.this).getSeekToTimeUs() > -1L) || (this.b == paramAnonymousBufferInfo.presentationTimeUs)) {
        return;
      }
      this.b = paramAnonymousBufferInfo.presentationTimeUs;
      if (TuSdkMediaFileDirectorPlayerImpl.b(TuSdkMediaFileDirectorPlayerImpl.this).isVideoEos())
      {
        onDecoderCompleted(null);
        return;
      }
      TuSdkMediaFileDirectorPlayerImpl.a(TuSdkMediaFileDirectorPlayerImpl.this, false);
    }
    
    public void onDecoderCompleted(Exception paramAnonymousException)
    {
      TuSdkMediaFileDirectorPlayerImpl.this.pause();
      TuSdkMediaFileDirectorPlayerImpl.b(TuSdkMediaFileDirectorPlayerImpl.this).syncVideoDecodeCompleted();
      if (paramAnonymousException != null)
      {
        TuSdkMediaFileDirectorPlayerImpl.a(TuSdkMediaFileDirectorPlayerImpl.this, (paramAnonymousException instanceof TuSdkTaskExitException) ? null : paramAnonymousException);
        return;
      }
      TuSdkMediaFileDirectorPlayerImpl.a(TuSdkMediaFileDirectorPlayerImpl.this, true);
      TLog.d("%s VideoDecoderListener process buffer stream end", new Object[] { "TuSdkMediaFileDirectorPlayerImpl" });
    }
  };
  private TuSdkDecoderListener o = new TuSdkDecoderListener()
  {
    public void onDecoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo) {}
    
    public void onDecoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException != null)
      {
        if ((paramAnonymousException instanceof TuSdkTaskExitException))
        {
          TuSdkMediaFileDirectorPlayerImpl.a(TuSdkMediaFileDirectorPlayerImpl.this, null);
          return;
        }
        TLog.e(paramAnonymousException, "%s AudioDecoderListener catch a exception, skip audio and ignore.", new Object[] { "TuSdkMediaFileDirectorPlayerImpl" });
      }
      TuSdkMediaFileDirectorPlayerImpl.b(TuSdkMediaFileDirectorPlayerImpl.this).syncAudioDecodeCompleted();
      if (!TuSdkMediaFileDirectorPlayerImpl.b(TuSdkMediaFileDirectorPlayerImpl.this).isAudioDecodeCrashed()) {
        TLog.d("%s AudioDecoderListener process buffer stream end", new Object[] { "TuSdkMediaFileDirectorPlayerImpl" });
      }
    }
  };
  
  public void setEffectFrameCalc(TuSdkMediaFileDirectorPlayerSync.TuSdkEffectFrameCalc paramTuSdkEffectFrameCalc)
  {
    this.b.setEffectFrameCalc(paramTuSdkEffectFrameCalc);
  }
  
  public void setMediaDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    if ((paramTuSdkMediaDataSource == null) || (!paramTuSdkMediaDataSource.isValid()))
    {
      TLog.w("%s setMediaDataSource not exists: %s", new Object[] { "TuSdkMediaFileDirectorPlayerImpl", paramTuSdkMediaDataSource });
      return;
    }
    this.i = paramTuSdkMediaDataSource;
  }
  
  public void setSurfaceDraw(TuSdkSurfaceDraw paramTuSdkSurfaceDraw)
  {
    this.c = paramTuSdkSurfaceDraw;
    if (this.g != null) {
      this.g.setSurfaceDraw(paramTuSdkSurfaceDraw);
    }
  }
  
  public void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    this.d = paramTuSdkAudioRender;
    if (this.e != null) {
      this.e.setAudioRender(paramTuSdkAudioRender);
    }
  }
  
  public void setAudioMixerRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    this.b.setMixerRender(paramTuSdkAudioRender);
  }
  
  public void setProgressOutputMode(int paramInt)
  {
    this.l = paramInt;
    this.b.setProgressOutputMode(paramInt);
  }
  
  public TuSdkMediaFileCuterTimeline getTimeLine()
  {
    return this.b.getTimeline();
  }
  
  public long calcInputTimeUs(long paramLong)
  {
    return this.b.calInputTimeUs(paramLong);
  }
  
  public void setListener(TuSdkMediaPlayerListener paramTuSdkMediaPlayerListener)
  {
    this.h = paramTuSdkMediaPlayerListener;
  }
  
  public TuSdkFilterBridge getFilterBridge()
  {
    return this.g;
  }
  
  public GLSurfaceView.Renderer getExtenalRenderer()
  {
    return this.m;
  }
  
  public void setCanvasColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    if (this.f == null) {
      return;
    }
    this.f.setCanvasColor(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public void setCanvasColor(int paramInt)
  {
    if (this.f == null) {
      return;
    }
    this.f.setCanvasColor(paramInt);
  }
  
  public boolean load(boolean paramBoolean)
  {
    if (this.a != -1)
    {
      TLog.w("%s repeated loading is not allowed.", new Object[] { "TuSdkMediaFileDirectorPlayerImpl" });
      return false;
    }
    this.j = paramBoolean;
    this.a = 0;
    boolean bool = b();
    return bool;
  }
  
  public void initInGLThread()
  {
    if ((this.f == null) || (this.a != 0))
    {
      TLog.w("%s initInGLThread need after load, before release.", new Object[] { "TuSdkMediaFileDirectorPlayerImpl" });
      return;
    }
    this.a = 1;
    a();
    this.f.initInGLThread();
  }
  
  public void newFrameReadyInGLThread()
  {
    if ((this.f == null) || (this.a != 1))
    {
      TLog.w("%s newFrameReadyInGLThread need after load, before release.", new Object[] { "TuSdkMediaFileDirectorPlayerImpl" });
      return;
    }
    long l1 = this.f.getSurfaceTexTimestampNs();
    this.f.updateSurfaceTexImage(l1);
  }
  
  public void release()
  {
    if (this.a == 2)
    {
      TLog.w("%s already released.", new Object[] { "TuSdkMediaFileDirectorPlayerImpl" });
      return;
    }
    this.a = 2;
    a();
    this.b.release();
    if (this.f != null)
    {
      this.f.destroy();
      this.f = null;
    }
    if (this.g != null)
    {
      this.g.destroy();
      this.g = null;
    }
    if (this.e != null)
    {
      this.e.release();
      this.e = null;
    }
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  private void a()
  {
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        if (TuSdkMediaFileDirectorPlayerImpl.a(TuSdkMediaFileDirectorPlayerImpl.this) != null) {
          TuSdkMediaFileDirectorPlayerImpl.a(TuSdkMediaFileDirectorPlayerImpl.this).onStateChanged(TuSdkMediaFileDirectorPlayerImpl.b(TuSdkMediaFileDirectorPlayerImpl.this).isPause() ? 1 : 0);
        }
      }
    });
  }
  
  private void a(boolean paramBoolean)
  {
    long l1 = outputTimeUs();
    long l2 = durationUs();
    long l3 = decodeFrameTimeUs();
    long l4 = inputDurationUs();
    if (l2 < 1L) {
      return;
    }
    if (this.j)
    {
      this.j = false;
      pause();
      this.b.enableLoadFirstFramePause(this.j);
    }
    if (paramBoolean)
    {
      l1 = l2;
      l3 = l4;
    }
    final long l5 = l1;
    long l6 = l3;
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        if (TuSdkMediaFileDirectorPlayerImpl.a(TuSdkMediaFileDirectorPlayerImpl.this) == null) {
          return;
        }
        if ((TuSdkMediaFileDirectorPlayerImpl.a(TuSdkMediaFileDirectorPlayerImpl.this) instanceof TuSdkMediaDirectorPlayerListener)) {
          ((TuSdkMediaDirectorPlayerListener)TuSdkMediaFileDirectorPlayerImpl.a(TuSdkMediaFileDirectorPlayerImpl.this)).onProgress(l5, this.b, TuSdkMediaFileDirectorPlayerImpl.c(TuSdkMediaFileDirectorPlayerImpl.this), TuSdkMediaFileDirectorPlayerImpl.b(TuSdkMediaFileDirectorPlayerImpl.this).getTimeline());
        } else {
          TuSdkMediaFileDirectorPlayerImpl.a(TuSdkMediaFileDirectorPlayerImpl.this).onProgress(l5, TuSdkMediaFileDirectorPlayerImpl.c(TuSdkMediaFileDirectorPlayerImpl.this), this.b);
        }
      }
    });
  }
  
  private void a(final Exception paramException)
  {
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuSdkMediaFileDirectorPlayerImpl.this.release();
        if (TuSdkMediaFileDirectorPlayerImpl.a(TuSdkMediaFileDirectorPlayerImpl.this) == null) {
          return;
        }
        TuSdkMediaFileDirectorPlayerImpl.a(TuSdkMediaFileDirectorPlayerImpl.this).onCompleted(paramException, TuSdkMediaFileDirectorPlayerImpl.c(TuSdkMediaFileDirectorPlayerImpl.this));
      }
    });
  }
  
  public long durationUs()
  {
    return this.b.totalVideoDurationUs();
  }
  
  public long inputDurationUs()
  {
    return this.b.totalVideInputDurationUs();
  }
  
  public long outputTimeUs()
  {
    return this.b.outputTimeUs();
  }
  
  public void setEnableClip(boolean paramBoolean)
  {
    if (this.f != null) {
      this.f.setEnableClip(paramBoolean);
    }
  }
  
  public TuSdkSize setOutputRatio(float paramFloat)
  {
    if (this.f != null) {
      return this.f.setOutputRatio(paramFloat);
    }
    return null;
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize)
  {
    if (this.f != null) {
      this.f.setOutputSize(paramTuSdkSize);
    }
  }
  
  public long decodeFrameTimeUs()
  {
    return this.b.decodeFrameTimeUs();
  }
  
  public boolean isPause()
  {
    return (this.a != 1) || (this.b.isPause());
  }
  
  public void pause()
  {
    if (isPause()) {
      return;
    }
    this.b.setPause();
  }
  
  public void resume()
  {
    if ((this.a != 1) || (!this.b.isPause())) {
      return;
    }
    if ((this.k >= this.b.totalVideoDurationUs()) && (this.b.getTimeline().isFixTimeSlices())) {
      this.k = 0L;
    }
    if (this.k > -1L)
    {
      this.b.syncFlushAndSeekto(this.k);
      this.k = -1L;
    }
    else
    {
      this.b.syncNeedRestart();
    }
    a();
    this.b.setPlay();
  }
  
  public void reset()
  {
    this.b.setReset();
  }
  
  public long seekToPercentage(float paramFloat)
  {
    if (paramFloat < 0.0F) {
      paramFloat = 0.0F;
    } else if (paramFloat > 1.0F) {
      paramFloat = 1.0F;
    }
    long l1 = (paramFloat * (float)durationUs());
    seekTo(l1);
    return l1;
  }
  
  public void seekTo(long paramLong)
  {
    a(paramLong, 2);
  }
  
  private void a(long paramLong, int paramInt)
  {
    if (this.a != 1) {
      return;
    }
    this.b.pauseSave();
    long l1 = this.b.calInputTimeUs(paramLong);
    if ((l1 > -1L) && (this.e != null))
    {
      l1 = this.e.seekTo(l1, paramInt);
      this.k = this.b.calOutputTimeUs(l1);
      this.b.syncSeektoTimeUs(l1);
    }
    this.b.resumeSave();
  }
  
  public void preview(TuSdkMediaTimeline paramTuSdkMediaTimeline)
  {
    this.b.setTimeline(paramTuSdkMediaTimeline);
  }
  
  public int setVolume(float paramFloat)
  {
    return this.b.setVolume(paramFloat);
  }
  
  private boolean b()
  {
    SelesVerticeCoordinateCropBuilderImpl localSelesVerticeCoordinateCropBuilderImpl = new SelesVerticeCoordinateCropBuilderImpl(false);
    this.f = new SelesSurfaceReceiver();
    this.f.setTextureCoordinateBuilder(localSelesVerticeCoordinateCropBuilderImpl);
    this.f.addTarget(this.g, 0);
    this.f.setSurfaceTextureListener(new SurfaceTexture.OnFrameAvailableListener()
    {
      public void onFrameAvailable(SurfaceTexture paramAnonymousSurfaceTexture)
      {
        if (TuSdkMediaFileDirectorPlayerImpl.a(TuSdkMediaFileDirectorPlayerImpl.this) != null) {
          TuSdkMediaFileDirectorPlayerImpl.a(TuSdkMediaFileDirectorPlayerImpl.this).onFrameAvailable();
        }
      }
    });
    this.b.setDirectorPlayerStateCallback(new TuSdkMediaFileDirectorPlayerSync.TuSdkDirectorPlayerStateCallback()
    {
      public void onPauseWait()
      {
        TuSdkMediaFileDirectorPlayerImpl.d(TuSdkMediaFileDirectorPlayerImpl.this);
      }
    });
    this.b.enableLoadFirstFramePause(this.j);
    this.e = new TuSdkMediaFileDecoder(true, true);
    this.e.setMediaDataSource(this.i);
    this.e.setMediaSync(this.b);
    this.e.setSurfaceReceiver(this.f);
    this.e.setAudioRender(this.d);
    this.e.setListener(this.n, this.o);
    this.e.prepare();
    if (!this.e.isVideoStared())
    {
      a(new Exception(String.format("%s VideoFileDecoder start failed", new Object[] { "TuSdkMediaFileDirectorPlayerImpl" })));
      return false;
    }
    if (!this.e.isAudioStared())
    {
      this.e.releaseAudioDecoder();
      this.b.setHaveAudio(false);
    }
    return true;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\TuSdkMediaFileDirectorPlayerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */