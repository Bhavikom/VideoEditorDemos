package org.lasque.tusdk.core.media.codec.suit;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.MediaCodec.BufferInfo;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import org.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkDecoderListener;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFileDecoder;
import org.lasque.tusdk.core.media.codec.exception.TuSdkTaskExitException;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFilePlayerSync;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

@TargetApi(16)
public class TuSdkMediaFilePlayerImpl
  implements TuSdkMediaFilePlayer
{
  private int a = -1;
  private final TuSdkMediaFilePlayerSync b = new TuSdkMediaFilePlayerSync();
  private TuSdkSurfaceDraw c;
  private TuSdkAudioRender d;
  private TuSdkMediaFileDecoder e;
  private SelesSurfaceReceiver f;
  private TuSdkFilterBridge g = new TuSdkFilterBridge();
  private TuSdkMediaPlayerListener h;
  private TuSdkMediaDataSource i;
  private boolean j;
  private long k = -1L;
  private GLSurfaceView.Renderer l = new GLSurfaceView.Renderer()
  {
    public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig)
    {
      GLES20.glDisable(2929);
      TuSdkMediaFilePlayerImpl.this.initInGLThread();
    }
    
    public void onSurfaceChanged(GL10 paramAnonymousGL10, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      GLES20.glViewport(0, 0, paramAnonymousInt1, paramAnonymousInt2);
    }
    
    public void onDrawFrame(GL10 paramAnonymousGL10)
    {
      GLES20.glClear(16640);
      TuSdkMediaFilePlayerImpl.this.newFrameReadyInGLThread();
    }
  };
  private TuSdkDecoderListener m = new TuSdkDecoderListener()
  {
    public void onDecoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkMediaFilePlayerImpl.b(TuSdkMediaFilePlayerImpl.this).isVideoEos(paramAnonymousBufferInfo.presentationTimeUs))
      {
        onDecoderCompleted(null);
        return;
      }
      TuSdkMediaFilePlayerImpl.a(TuSdkMediaFilePlayerImpl.this, false);
    }
    
    public void onDecoderCompleted(Exception paramAnonymousException)
    {
      TuSdkMediaFilePlayerImpl.this.pause();
      TuSdkMediaFilePlayerImpl.b(TuSdkMediaFilePlayerImpl.this).syncVideoDecodeCompleted();
      if (paramAnonymousException != null) {
        TuSdkMediaFilePlayerImpl.a(TuSdkMediaFilePlayerImpl.this, (paramAnonymousException instanceof TuSdkTaskExitException) ? null : paramAnonymousException);
      }
      TuSdkMediaFilePlayerImpl.a(TuSdkMediaFilePlayerImpl.this, true);
      TLog.d("%s VideoDecoderListener process buffer stream end", new Object[] { "TuSdkMediaFilePlayerImpl" });
    }
  };
  private TuSdkDecoderListener n = new TuSdkDecoderListener()
  {
    public void onDecoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TuSdkMediaFilePlayerImpl.b(TuSdkMediaFilePlayerImpl.this).isAudioEos(paramAnonymousBufferInfo.presentationTimeUs))
      {
        onDecoderCompleted(null);
        return;
      }
    }
    
    public void onDecoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException != null)
      {
        if ((paramAnonymousException instanceof TuSdkTaskExitException))
        {
          TuSdkMediaFilePlayerImpl.a(TuSdkMediaFilePlayerImpl.this, null);
          return;
        }
        TLog.e(paramAnonymousException, "%s AudioDecoderListener catch a exception, skip audio and ignore.", new Object[] { "TuSdkMediaFilePlayerImpl" });
      }
      TuSdkMediaFilePlayerImpl.b(TuSdkMediaFilePlayerImpl.this).syncAudioDecodeCompleted();
      if (!TuSdkMediaFilePlayerImpl.b(TuSdkMediaFilePlayerImpl.this).isAudioDecodeCrashed()) {
        TLog.d("%s AudioDecoderListener process buffer stream end", new Object[] { "TuSdkMediaFilePlayerImpl" });
      }
    }
  };
  
  public void setMediaDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    if ((paramTuSdkMediaDataSource == null) || (!paramTuSdkMediaDataSource.isValid()))
    {
      TLog.w("%s setMediaDataSource not exists: %s", new Object[] { "TuSdkMediaFilePlayerImpl", paramTuSdkMediaDataSource });
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
    return this.l;
  }
  
  public boolean load(boolean paramBoolean)
  {
    if (this.a != -1)
    {
      TLog.w("%s repeated loading is not allowed.", new Object[] { "TuSdkMediaFilePlayerImpl" });
      return false;
    }
    this.j = paramBoolean;
    this.a = 0;
    boolean bool = c();
    return bool;
  }
  
  public void initInGLThread()
  {
    if ((this.f == null) || (this.a != 0))
    {
      TLog.w("%s initInGLThread need after load, before release.", new Object[] { "TuSdkMediaFilePlayerImpl" });
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
      TLog.w("%s newFrameReadyInGLThread need after load, before release.", new Object[] { "TuSdkMediaFilePlayerImpl" });
      return;
    }
    long l1 = this.f.getSurfaceTexTimestampNs();
    this.f.updateSurfaceTexImage(l1);
  }
  
  public void release()
  {
    if (this.a == 2)
    {
      TLog.w("%s already released.", new Object[] { "TuSdkMediaFilePlayerImpl" });
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
        if (TuSdkMediaFilePlayerImpl.a(TuSdkMediaFilePlayerImpl.this) != null) {
          TuSdkMediaFilePlayerImpl.a(TuSdkMediaFilePlayerImpl.this).onStateChanged(TuSdkMediaFilePlayerImpl.b(TuSdkMediaFilePlayerImpl.this).isPause() ? 1 : 0);
        }
      }
    });
  }
  
  private void a(boolean paramBoolean)
  {
    final long l1 = elapsedUs();
    long l2 = durationUs();
    if (l2 < 1L) {
      return;
    }
    if (this.j)
    {
      this.j = false;
      pause();
    }
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        if (TuSdkMediaFilePlayerImpl.a(TuSdkMediaFilePlayerImpl.this) == null) {
          return;
        }
        TuSdkMediaFilePlayerImpl.a(TuSdkMediaFilePlayerImpl.this).onProgress(l1, TuSdkMediaFilePlayerImpl.c(TuSdkMediaFilePlayerImpl.this), this.b);
      }
    });
  }
  
  private void a(final Exception paramException)
  {
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuSdkMediaFilePlayerImpl.this.release();
        if (TuSdkMediaFilePlayerImpl.a(TuSdkMediaFilePlayerImpl.this) == null) {
          return;
        }
        TuSdkMediaFilePlayerImpl.a(TuSdkMediaFilePlayerImpl.this).onCompleted(paramException, TuSdkMediaFilePlayerImpl.c(TuSdkMediaFilePlayerImpl.this));
      }
    });
  }
  
  public long durationUs()
  {
    return this.b.totalVideoDurationUs();
  }
  
  public long elapsedUs()
  {
    return this.b.lastVideoTimestampUs();
  }
  
  public boolean isSupportPrecise()
  {
    return this.b.isSupportPrecise();
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
    a();
  }
  
  public void resume()
  {
    if ((this.a != 1) || (!this.b.isPause())) {
      return;
    }
    b();
    if (this.k > -1L)
    {
      this.b.syncFlushAndSeekto(this.k);
      this.k = -1L;
    }
    this.b.setPlay();
    a();
  }
  
  private void b()
  {
    if (!this.b.syncNeedRestart()) {
      return;
    }
    this.k = (this.b.isReverse() ? this.b.totalVideoDurationUs() : 0L);
  }
  
  public void reset()
  {
    this.b.setReset();
  }
  
  public void setSpeed(float paramFloat)
  {
    this.b.setSpeed(paramFloat);
  }
  
  public float speed()
  {
    return this.b.speed();
  }
  
  public void setReverse(boolean paramBoolean)
  {
    this.b.setReverse(paramBoolean);
  }
  
  public boolean isReverse()
  {
    return this.b.isReverse();
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
    if (this.e != null) {
      this.k = this.e.seekTo(paramLong, paramInt);
    }
    this.b.resumeSave();
  }
  
  private boolean c()
  {
    SelesVerticeCoordinateCropBuilderImpl localSelesVerticeCoordinateCropBuilderImpl = new SelesVerticeCoordinateCropBuilderImpl(false);
    this.f = new SelesSurfaceReceiver();
    this.f.setTextureCoordinateBuilder(localSelesVerticeCoordinateCropBuilderImpl);
    this.f.addTarget(this.g, 0);
    this.f.setSurfaceTextureListener(new SurfaceTexture.OnFrameAvailableListener()
    {
      public void onFrameAvailable(SurfaceTexture paramAnonymousSurfaceTexture)
      {
        if (TuSdkMediaFilePlayerImpl.a(TuSdkMediaFilePlayerImpl.this) != null) {
          TuSdkMediaFilePlayerImpl.a(TuSdkMediaFilePlayerImpl.this).onFrameAvailable();
        }
      }
    });
    this.e = new TuSdkMediaFileDecoder(true, true);
    this.e.setMediaDataSource(this.i);
    this.e.setMediaSync(this.b);
    this.e.setSurfaceReceiver(this.f);
    this.e.setAudioRender(this.d);
    this.e.setListener(this.m, this.n);
    this.e.prepare();
    if (!this.e.isVideoStared())
    {
      a(new Exception(String.format("%s VideoFileDecoder start failed", new Object[] { "TuSdkMediaFilePlayerImpl" })));
      return false;
    }
    if (!this.e.isAudioStared()) {
      this.e.releaseAudioDecoder();
    }
    return true;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\TuSdkMediaFilePlayerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */