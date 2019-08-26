package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import junit.framework.Assert;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import org.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitch;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrack;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackImpl;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;
import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;
import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

public class TuSdkMediaMutableFilePlayerImpl
  implements TuSdkMediaMutableFilePlayer
{
  private List<AVAsset> a = new ArrayList(2);
  private TuSdkMediaPlayerStatus b = TuSdkMediaPlayerStatus.Unknown;
  private TuSdkSurfaceDraw c;
  private AudioRender d = new AudioRender(null);
  private VideoRender e = new VideoRender(null);
  private long f = -1L;
  private SelesSurfaceReceiver g;
  private TuSdkFilterBridge h = new TuSdkFilterBridge();
  private TuSdkMediaPlayerListener i;
  private boolean j;
  private boolean k;
  private AVAssetTrackOutputSouce l;
  private AVAssetTrackCodecDecoder m;
  private AVAssetTrackOutputSouce n;
  private AVAssetTrackCodecDecoder o;
  private TuSdkVideoInfo p;
  private AVMediaSyncClock q = new AVMediaSyncClock();
  private TuSdkAudioTrack r;
  private TuSdkAudioPitch s;
  private TuSdkAudioResample t;
  private float u = 0.0F;
  private GLSurfaceView.Renderer v = new GLSurfaceView.Renderer()
  {
    public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig)
    {
      GLES20.glDisable(2929);
      TuSdkMediaMutableFilePlayerImpl.this.initInGLThread();
    }
    
    public void onSurfaceChanged(GL10 paramAnonymousGL10, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      GLES20.glViewport(0, 0, paramAnonymousInt1, paramAnonymousInt2);
    }
    
    public void onDrawFrame(GL10 paramAnonymousGL10)
    {
      TuSdkMediaMutableFilePlayerImpl.this.newFrameReadyInGLThread();
    }
  };
  
  public final int maxInputSize()
  {
    return 9;
  }
  
  public void setOutputRatio(float paramFloat)
  {
    this.u = paramFloat;
    if (this.g != null) {
      this.g.setOutputSize(preferredOutputSize());
    }
  }
  
  public final void setMediaDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    setMediaDataSources(Arrays.asList(new TuSdkMediaDataSource[] { paramTuSdkMediaDataSource }));
  }
  
  public final void setMediaDataSources(List<TuSdkMediaDataSource> paramList)
  {
    if (this.b != TuSdkMediaPlayerStatus.Unknown)
    {
      TLog.e("%s : The data source is not allowed to be set again after load", new Object[] { this });
      return;
    }
    if ((paramList == null) || (paramList.size() == 0))
    {
      TLog.w("%s setMediaDataSource not exists: %s", new Object[] { "TuSdkMediaMutableFilePlayerImpl", paramList });
      return;
    }
    if (paramList.size() > maxInputSize()) {
      TLog.w("The maximum number of video supported is %d", new Object[] { Integer.valueOf(maxInputSize()) });
    }
    List<TuSdkMediaDataSource> localList = paramList.size() > maxInputSize() ? paramList.subList(0, maxInputSize() - 1) : paramList;
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaDataSource localTuSdkMediaDataSource = (TuSdkMediaDataSource)localIterator.next();
      if (!localTuSdkMediaDataSource.isValid())
      {
        TLog.e("%s :This data source is invalid", new Object[] { localTuSdkMediaDataSource });
      }
      else
      {
        AVAssetDataSource localAVAssetDataSource = new AVAssetDataSource(localTuSdkMediaDataSource);
        this.a.add(localAVAssetDataSource);
      }
    }
  }
  
  private List<AVAssetTrack> a(AVMediaType paramAVMediaType)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      AVAsset localAVAsset = (AVAsset)localIterator.next();
      List localList = localAVAsset.tracksWithMediaType(paramAVMediaType);
      localArrayList.addAll(localList);
    }
    return localArrayList;
  }
  
  public TuSdkSize preferredOutputSize()
  {
    List localList = a(AVMediaType.AVMediaTypeVideo);
    if (localList.size() == 0) {
      return null;
    }
    TuSdkSize localTuSdkSize = ((AVAssetTrack)localList.get(0)).presentSize();
    if (this.u > 0.0F)
    {
      int i1 = Math.min(localTuSdkSize.width, localTuSdkSize.height);
      return TuSdkSize.create(i1, (int)(i1 * this.u));
    }
    return localTuSdkSize;
  }
  
  public void setEnableClip(boolean paramBoolean)
  {
    if (this.g != null) {
      this.g.setEnableClip(paramBoolean);
    }
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize)
  {
    if (this.g != null) {
      this.g.setOutputSize(paramTuSdkSize);
    }
  }
  
  public void setSurfaceDraw(TuSdkSurfaceDraw paramTuSdkSurfaceDraw)
  {
    this.c = paramTuSdkSurfaceDraw;
    if (this.h != null) {
      this.h.setSurfaceDraw(paramTuSdkSurfaceDraw);
    }
  }
  
  public void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender) {}
  
  public void setListener(TuSdkMediaPlayerListener paramTuSdkMediaPlayerListener)
  {
    this.i = paramTuSdkMediaPlayerListener;
  }
  
  public TuSdkFilterBridge getFilterBridge()
  {
    return this.h;
  }
  
  public GLSurfaceView.Renderer getExtenalRenderer()
  {
    return this.v;
  }
  
  public boolean load(boolean paramBoolean)
  {
    if (this.b != TuSdkMediaPlayerStatus.Unknown)
    {
      TLog.w("%s repeated loading is not allowed.", new Object[] { "TuSdkMediaMutableFilePlayerImpl" });
      return false;
    }
    this.j = paramBoolean;
    return g();
  }
  
  private boolean a()
  {
    while (!this.g.isInited()) {}
    SurfaceTexture localSurfaceTexture = this.g.requestSurfaceTexture();
    Surface localSurface = new Surface(localSurfaceTexture);
    localSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener()
    {
      public void onFrameAvailable(SurfaceTexture paramAnonymousSurfaceTexture)
      {
        TuSdkMediaMutableFilePlayerImpl.a(TuSdkMediaMutableFilePlayerImpl.this).onFrameAvailable();
      }
    });
    a(localSurface);
    if (this.l == null) {
      return false;
    }
    this.g.setInputRotation(this.l.inputTrack().orientation());
    this.g.setInputSize(this.l.inputTrack().naturalSize());
    this.g.setOutputSize(preferredOutputSize());
    b();
    return true;
  }
  
  private void a(Surface paramSurface)
  {
    ArrayList localArrayList1 = new ArrayList(1);
    ArrayList localArrayList2 = new ArrayList(1);
    Object localObject = this.a.iterator();
    while (((Iterator)localObject).hasNext())
    {
      AVAsset localAVAsset = (AVAsset)((Iterator)localObject).next();
      List localList1 = localAVAsset.tracksWithMediaType(AVMediaType.AVMediaTypeVideo);
      List localList2 = localAVAsset.tracksWithMediaType(AVMediaType.AVMediaTypeAudio);
      if ((localList1.size() > 0) && (localList2.size() > 0))
      {
        localArrayList1.addAll(localList1);
        localArrayList2.addAll(localList2);
      }
    }
    if ((localArrayList1.size() == 0) || (localArrayList2.size() == 0))
    {
      TLog.e("%s No tracks are available in the data source.", new Object[] { this });
      return;
    }
    this.l = new AVAssetTrackPipeMediaExtractor(localArrayList1);
    this.m = new AVAssetTrackCodecDecoder(this.l);
    this.m.setOutputSurface(paramSurface);
    this.m.addTarget(this.e);
    this.n = new AVAssetTrackPipeMediaExtractor(localArrayList2);
    this.o = new AVAssetTrackCodecDecoder(this.n);
    this.o.addTarget(this.d);
    localObject = new TuSdkAudioInfo(this.n.inputTrack().mediaFormat());
    this.r = new TuSdkAudioTrackImpl((TuSdkAudioInfo)localObject);
    this.r.play();
    this.t = new TuSdkAudioResampleHardImpl((TuSdkAudioInfo)localObject);
    this.t.setMediaSync(this.d);
    this.s = new TuSdkAudioPitchHardImpl((TuSdkAudioInfo)localObject);
    this.s.changeSpeed(this.q.getSpeed());
    this.s.setMediaSync(this.d);
  }
  
  private boolean b()
  {
    if (this.k) {
      return false;
    }
    this.e.a(new Runnable()
    {
      public void run()
      {
        TuSdkMediaMutableFilePlayerImpl.VideoRender.a(TuSdkMediaMutableFilePlayerImpl.b(TuSdkMediaMutableFilePlayerImpl.this));
      }
    });
    return true;
  }
  
  @TargetApi(14)
  public void initInGLThread()
  {
    if (this.g == null)
    {
      TLog.w("%s initInGLThread need after load, before release.", new Object[] { "TuSdkMediaMutableFilePlayerImpl" });
      return;
    }
    this.g.initInGLThread();
    this.e.a(new Runnable()
    {
      public void run()
      {
        TuSdkMediaMutableFilePlayerImpl.c(TuSdkMediaMutableFilePlayerImpl.this);
      }
    });
  }
  
  public void newFrameReadyInGLThread()
  {
    if (this.g == null)
    {
      TLog.w("%s newFrameReadyInGLThread need after load, before release." + this.g, new Object[] { "TuSdkMediaMutableFilePlayerImpl" });
      return;
    }
    long l1 = this.g.getSurfaceTexTimestampNs();
    this.g.updateSurfaceTexImage(l1);
  }
  
  public void release()
  {
    if (this.b == TuSdkMediaPlayerStatus.Unknown)
    {
      TLog.w("%s already released.", new Object[] { "TuSdkMediaMutableFilePlayerImpl" });
      return;
    }
    this.e.a(new Runnable()
    {
      public void run()
      {
        if (TuSdkMediaMutableFilePlayerImpl.d(TuSdkMediaMutableFilePlayerImpl.this) != null)
        {
          TuSdkMediaMutableFilePlayerImpl.d(TuSdkMediaMutableFilePlayerImpl.this).destroy();
          TuSdkMediaMutableFilePlayerImpl.a(TuSdkMediaMutableFilePlayerImpl.this, null);
        }
        if (TuSdkMediaMutableFilePlayerImpl.e(TuSdkMediaMutableFilePlayerImpl.this) != null)
        {
          TuSdkMediaMutableFilePlayerImpl.e(TuSdkMediaMutableFilePlayerImpl.this).destroy();
          TuSdkMediaMutableFilePlayerImpl.a(TuSdkMediaMutableFilePlayerImpl.this, null);
        }
        TuSdkMediaMutableFilePlayerImpl.b(TuSdkMediaMutableFilePlayerImpl.this).release();
      }
    });
    this.d.b(new Runnable()
    {
      public void run()
      {
        TuSdkMediaMutableFilePlayerImpl.f(TuSdkMediaMutableFilePlayerImpl.this).release();
      }
    });
    this.b = TuSdkMediaPlayerStatus.Unknown;
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  private void a(final long paramLong)
  {
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        if ((TuSdkMediaMutableFilePlayerImpl.g(TuSdkMediaMutableFilePlayerImpl.this) != -1L) && (Math.abs(paramLong - TuSdkMediaMutableFilePlayerImpl.g(TuSdkMediaMutableFilePlayerImpl.this)) > 500000L)) {
          return;
        }
        TuSdkMediaMutableFilePlayerImpl.a(TuSdkMediaMutableFilePlayerImpl.this, -1L);
        TuSdkMediaMutableFilePlayerImpl.a(TuSdkMediaMutableFilePlayerImpl.this).onProgress(TuSdkMediaMutableFilePlayerImpl.h(TuSdkMediaMutableFilePlayerImpl.this).outputTimeUs(), null, TuSdkMediaMutableFilePlayerImpl.h(TuSdkMediaMutableFilePlayerImpl.this).durationTimeUs());
      }
    });
  }
  
  private void c()
  {
    d();
    f();
  }
  
  private void a(final TuSdkMediaPlayerStatus paramTuSdkMediaPlayerStatus)
  {
    if (this.b == paramTuSdkMediaPlayerStatus) {
      return;
    }
    this.b = paramTuSdkMediaPlayerStatus;
    if (this.i == null) {
      return;
    }
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuSdkMediaMutableFilePlayerImpl.a(TuSdkMediaMutableFilePlayerImpl.this).onStateChanged(paramTuSdkMediaPlayerStatus.ordinal());
      }
    });
  }
  
  public long durationUs()
  {
    if (this.m == null) {
      return 0L;
    }
    return this.m.durationTimeUs();
  }
  
  public long elapsedUs()
  {
    return this.m.outputTimeUs();
  }
  
  public boolean isSupportPrecise()
  {
    return false;
  }
  
  public boolean isPause()
  {
    return this.b != TuSdkMediaPlayerStatus.Playing;
  }
  
  public void pause()
  {
    this.e.a(new Runnable()
    {
      public void run()
      {
        TuSdkMediaMutableFilePlayerImpl.i(TuSdkMediaMutableFilePlayerImpl.this);
      }
    });
  }
  
  private void d()
  {
    if (this.b != TuSdkMediaPlayerStatus.Playing) {
      return;
    }
    a(TuSdkMediaPlayerStatus.ReadyToPlay);
    this.q.stop();
    AudioRender.a(this.d);
  }
  
  public void resume()
  {
    e();
  }
  
  private void e()
  {
    if (((this.b == TuSdkMediaPlayerStatus.Playing) || (this.f > 0L)) && (Math.abs(this.f - durationUs()) > 50000L)) {
      return;
    }
    a(TuSdkMediaPlayerStatus.Playing);
    this.e.a(new Runnable()
    {
      public void run()
      {
        TuSdkMediaMutableFilePlayerImpl.j(TuSdkMediaMutableFilePlayerImpl.this).start();
        if (TuSdkMediaMutableFilePlayerImpl.h(TuSdkMediaMutableFilePlayerImpl.this).renderOutputBuffer())
        {
          TuSdkMediaMutableFilePlayerImpl.f(TuSdkMediaMutableFilePlayerImpl.this).a(new Runnable()
          {
            public void run()
            {
              TuSdkMediaMutableFilePlayerImpl.f(TuSdkMediaMutableFilePlayerImpl.this).render();
            }
          });
          TuSdkMediaMutableFilePlayerImpl.b(TuSdkMediaMutableFilePlayerImpl.this).render();
        }
      }
    });
  }
  
  public void reset()
  {
    f();
  }
  
  private void f()
  {
    if ((this.m == null) || (this.b == TuSdkMediaPlayerStatus.Unknown)) {
      return;
    }
    a(TuSdkMediaPlayerStatus.ReadyToPlay);
    this.f = -1L;
    this.d.a(new Runnable()
    {
      public void run()
      {
        TuSdkMediaMutableFilePlayerImpl.f(TuSdkMediaMutableFilePlayerImpl.this).reset();
      }
    });
    this.e.reset();
  }
  
  public void setSpeed(final float paramFloat)
  {
    Assert.assertEquals(String.format("Unsupported playback speed : %f", new Object[] { Float.valueOf(paramFloat) }), true, (paramFloat > 0.0F) && (paramFloat <= 4.0F));
    this.e.b(new Runnable()
    {
      public void run()
      {
        if (TuSdkMediaMutableFilePlayerImpl.this.speed() == paramFloat) {
          return;
        }
        TuSdkMediaMutableFilePlayerImpl.j(TuSdkMediaMutableFilePlayerImpl.this).setSpeed(paramFloat);
        if (TuSdkMediaMutableFilePlayerImpl.f(TuSdkMediaMutableFilePlayerImpl.this) != null) {
          TuSdkMediaMutableFilePlayerImpl.AudioRender.a(TuSdkMediaMutableFilePlayerImpl.f(TuSdkMediaMutableFilePlayerImpl.this), paramFloat);
        }
      }
    });
  }
  
  public float speed()
  {
    return this.q.getSpeed();
  }
  
  public void setReverse(boolean paramBoolean)
  {
    TLog.e("%s ： Sorry, reverse mode is not supported", new Object[] { this });
  }
  
  public boolean isReverse()
  {
    return false;
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
  
  public void seekTo(final long paramLong)
  {
    if (this.b == TuSdkMediaPlayerStatus.Unknown) {
      return;
    }
    this.f = paramLong;
    this.e.c(new Runnable()
    {
      public void run()
      {
        TuSdkMediaMutableFilePlayerImpl.i(TuSdkMediaMutableFilePlayerImpl.this);
        TuSdkMediaMutableFilePlayerImpl.h(TuSdkMediaMutableFilePlayerImpl.this).seekTo(paramLong, true);
        TuSdkMediaMutableFilePlayerImpl.f(TuSdkMediaMutableFilePlayerImpl.this).b(new Runnable()
        {
          public void run()
          {
            TuSdkMediaMutableFilePlayerImpl.AudioRender.b(TuSdkMediaMutableFilePlayerImpl.f(TuSdkMediaMutableFilePlayerImpl.this));
            TuSdkMediaMutableFilePlayerImpl.k(TuSdkMediaMutableFilePlayerImpl.this).seekTo(TuSdkMediaMutableFilePlayerImpl.12.this.a, true);
            TuSdkMediaMutableFilePlayerImpl.a(TuSdkMediaMutableFilePlayerImpl.this, -1L);
          }
        });
      }
    });
  }
  
  @TargetApi(14)
  private boolean g()
  {
    SelesVerticeCoordinateCropBuilderImpl localSelesVerticeCoordinateCropBuilderImpl = new SelesVerticeCoordinateCropBuilderImpl(false);
    this.g = new SelesSurfaceReceiver();
    this.g.setTextureCoordinateBuilder(localSelesVerticeCoordinateCropBuilderImpl);
    this.g.addTarget(this.h, 0);
    return true;
  }
  
  private class AudioRender
    extends TuSdkMediaMutableFilePlayerImpl.InternalRender
    implements AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput, TuSdkAudioPitchSync, TuSdkAudioResampleSync
  {
    private AudioRender()
    {
      super(null);
    }
    
    private void a()
    {
      if (TuSdkMediaMutableFilePlayerImpl.r(TuSdkMediaMutableFilePlayerImpl.this) == null) {
        return;
      }
      TuSdkMediaMutableFilePlayerImpl.r(TuSdkMediaMutableFilePlayerImpl.this).pause();
    }
    
    private void b()
    {
      if (TuSdkMediaMutableFilePlayerImpl.r(TuSdkMediaMutableFilePlayerImpl.this) != null) {
        TuSdkMediaMutableFilePlayerImpl.r(TuSdkMediaMutableFilePlayerImpl.this).flush();
      }
      if (TuSdkMediaMutableFilePlayerImpl.s(TuSdkMediaMutableFilePlayerImpl.this) != null) {
        TuSdkMediaMutableFilePlayerImpl.s(TuSdkMediaMutableFilePlayerImpl.this).reset();
      }
    }
    
    public void render()
    {
      if ((TuSdkMediaMutableFilePlayerImpl.k(TuSdkMediaMutableFilePlayerImpl.this) == null) || (TuSdkMediaMutableFilePlayerImpl.m(TuSdkMediaMutableFilePlayerImpl.this) != TuSdkMediaMutableFilePlayerImpl.TuSdkMediaPlayerStatus.Playing))
      {
        TLog.i(" audio play paused Status ： %s", new Object[] { TuSdkMediaMutableFilePlayerImpl.m(TuSdkMediaMutableFilePlayerImpl.this) });
        return;
      }
      if (TuSdkMediaMutableFilePlayerImpl.r(TuSdkMediaMutableFilePlayerImpl.this) != null) {
        TuSdkMediaMutableFilePlayerImpl.r(TuSdkMediaMutableFilePlayerImpl.this).play();
      }
      if (TuSdkMediaMutableFilePlayerImpl.k(TuSdkMediaMutableFilePlayerImpl.this).renderOutputBuffers()) {
        a(new Runnable()
        {
          public void run()
          {
            TuSdkMediaMutableFilePlayerImpl.AudioRender.this.render();
          }
        });
      }
    }
    
    public void newFrameReady(AVSampleBuffer paramAVSampleBuffer)
    {
      if (paramAVSampleBuffer.isDecodeOnly()) {
        return;
      }
      if (TuSdkMediaMutableFilePlayerImpl.m(TuSdkMediaMutableFilePlayerImpl.this) != TuSdkMediaMutableFilePlayerImpl.TuSdkMediaPlayerStatus.Playing) {
        return;
      }
      TuSdkMediaMutableFilePlayerImpl.j(TuSdkMediaMutableFilePlayerImpl.this).lock(paramAVSampleBuffer.renderTimeUs(), 0L);
      paramAVSampleBuffer.info().presentationTimeUs = paramAVSampleBuffer.renderTimeUs();
      TuSdkMediaMutableFilePlayerImpl.s(TuSdkMediaMutableFilePlayerImpl.this).queueInputBuffer(paramAVSampleBuffer.buffer(), paramAVSampleBuffer.info());
    }
    
    public void outputFormatChaned(MediaFormat paramMediaFormat, AVAssetTrack paramAVAssetTrack)
    {
      TuSdkAudioInfo localTuSdkAudioInfo = new TuSdkAudioInfo(paramMediaFormat);
      TuSdkMediaMutableFilePlayerImpl.s(TuSdkMediaMutableFilePlayerImpl.this).reset();
      TuSdkMediaMutableFilePlayerImpl.s(TuSdkMediaMutableFilePlayerImpl.this).changeFormat(localTuSdkAudioInfo);
    }
    
    private void a(final float paramFloat)
    {
      b(new Runnable()
      {
        public void run()
        {
          if (TuSdkMediaMutableFilePlayerImpl.r(TuSdkMediaMutableFilePlayerImpl.this) != null) {
            TuSdkMediaMutableFilePlayerImpl.r(TuSdkMediaMutableFilePlayerImpl.this).flush();
          }
          if (TuSdkMediaMutableFilePlayerImpl.t(TuSdkMediaMutableFilePlayerImpl.this) != null)
          {
            TuSdkMediaMutableFilePlayerImpl.t(TuSdkMediaMutableFilePlayerImpl.this).reset();
            TuSdkMediaMutableFilePlayerImpl.t(TuSdkMediaMutableFilePlayerImpl.this).changeSpeed(paramFloat);
          }
        }
      });
    }
    
    public void syncAudioResampleOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      TuSdkMediaMutableFilePlayerImpl.t(TuSdkMediaMutableFilePlayerImpl.this).queueInputBuffer(paramByteBuffer, paramBufferInfo);
    }
    
    public void syncAudioPitchOutputBuffer(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
    {
      paramByteBuffer.position(paramBufferInfo.offset);
      paramByteBuffer.limit(paramBufferInfo.offset + paramBufferInfo.size);
      TuSdkMediaMutableFilePlayerImpl.r(TuSdkMediaMutableFilePlayerImpl.this).write(paramByteBuffer);
    }
    
    public void reset()
    {
      if ((TuSdkMediaMutableFilePlayerImpl.k(TuSdkMediaMutableFilePlayerImpl.this) == null) || (TuSdkMediaMutableFilePlayerImpl.m(TuSdkMediaMutableFilePlayerImpl.this) == TuSdkMediaMutableFilePlayerImpl.TuSdkMediaPlayerStatus.Unknown)) {
        return;
      }
      TuSdkMediaMutableFilePlayerImpl.k(TuSdkMediaMutableFilePlayerImpl.this).reset();
    }
  }
  
  private class VideoRender
    extends TuSdkMediaMutableFilePlayerImpl.InternalRender
    implements AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput
  {
    private VideoRender()
    {
      super(null);
    }
    
    public void newFrameReady(AVSampleBuffer paramAVSampleBuffer)
    {
      if (paramAVSampleBuffer.isDecodeOnly()) {
        return;
      }
      TuSdkMediaMutableFilePlayerImpl.a(TuSdkMediaMutableFilePlayerImpl.this, true);
      TuSdkMediaMutableFilePlayerImpl.j(TuSdkMediaMutableFilePlayerImpl.this).lock(paramAVSampleBuffer.renderTimeUs(), 0L);
      TuSdkMediaMutableFilePlayerImpl.b(TuSdkMediaMutableFilePlayerImpl.this, paramAVSampleBuffer.renderTimeUs());
    }
    
    public void outputFormatChaned(MediaFormat paramMediaFormat, AVAssetTrack paramAVAssetTrack)
    {
      TuSdkMediaMutableFilePlayerImpl.a(TuSdkMediaMutableFilePlayerImpl.this, new TuSdkVideoInfo(paramMediaFormat));
      TuSdkMediaMutableFilePlayerImpl.l(TuSdkMediaMutableFilePlayerImpl.this).setCorp(paramMediaFormat);
      TuSdkMediaMutableFilePlayerImpl.d(TuSdkMediaMutableFilePlayerImpl.this).setInputSize(TuSdkMediaMutableFilePlayerImpl.l(TuSdkMediaMutableFilePlayerImpl.this).codecSize);
      TuSdkMediaMutableFilePlayerImpl.d(TuSdkMediaMutableFilePlayerImpl.this).setPreCropRect(TuSdkMediaMutableFilePlayerImpl.l(TuSdkMediaMutableFilePlayerImpl.this).codecCrop);
      TuSdkMediaMutableFilePlayerImpl.d(TuSdkMediaMutableFilePlayerImpl.this).setInputRotation(paramAVAssetTrack.orientation());
    }
    
    public void render()
    {
      if ((TuSdkMediaMutableFilePlayerImpl.h(TuSdkMediaMutableFilePlayerImpl.this) == null) || (TuSdkMediaMutableFilePlayerImpl.m(TuSdkMediaMutableFilePlayerImpl.this) != TuSdkMediaMutableFilePlayerImpl.TuSdkMediaPlayerStatus.Playing))
      {
        TLog.i("%s : play paused", new Object[] { this });
        return;
      }
      if (TuSdkMediaMutableFilePlayerImpl.h(TuSdkMediaMutableFilePlayerImpl.this).renderOutputBuffers())
      {
        a(new Runnable()
        {
          public void run()
          {
            TuSdkMediaMutableFilePlayerImpl.VideoRender.this.render();
          }
        });
      }
      else
      {
        TLog.i("%s : play done", new Object[] { this });
        TuSdkMediaMutableFilePlayerImpl.n(TuSdkMediaMutableFilePlayerImpl.this);
      }
    }
    
    private void a()
    {
      if ((TuSdkMediaMutableFilePlayerImpl.h(TuSdkMediaMutableFilePlayerImpl.this) == null) || (TuSdkMediaMutableFilePlayerImpl.m(TuSdkMediaMutableFilePlayerImpl.this) == TuSdkMediaMutableFilePlayerImpl.TuSdkMediaPlayerStatus.Playing)) {
        return;
      }
      if (TuSdkMediaMutableFilePlayerImpl.o(TuSdkMediaMutableFilePlayerImpl.this))
      {
        TuSdkMediaMutableFilePlayerImpl.a(TuSdkMediaMutableFilePlayerImpl.this, TuSdkMediaMutableFilePlayerImpl.TuSdkMediaPlayerStatus.ReadyToPlay);
        if (!TuSdkMediaMutableFilePlayerImpl.p(TuSdkMediaMutableFilePlayerImpl.this)) {
          TuSdkMediaMutableFilePlayerImpl.this.resume();
        }
        return;
      }
      if (TuSdkMediaMutableFilePlayerImpl.h(TuSdkMediaMutableFilePlayerImpl.this).renderOutputBuffer()) {
        a(new Runnable()
        {
          public void run()
          {
            TuSdkMediaMutableFilePlayerImpl.VideoRender.a(TuSdkMediaMutableFilePlayerImpl.VideoRender.this);
          }
        });
      } else {
        TuSdkMediaMutableFilePlayerImpl.q(TuSdkMediaMutableFilePlayerImpl.this);
      }
    }
    
    public void reset()
    {
      TuSdkMediaMutableFilePlayerImpl.j(TuSdkMediaMutableFilePlayerImpl.this).stop();
      TuSdkMediaMutableFilePlayerImpl.h(TuSdkMediaMutableFilePlayerImpl.this).reset();
    }
  }
  
  private abstract class InternalRender
  {
    AVMediaProcessQueue b = new AVMediaProcessQueue();
    
    private InternalRender() {}
    
    void a(Runnable paramRunnable)
    {
      this.b.runAsynchronouslyOnProcessingQueue(paramRunnable);
    }
    
    void b(Runnable paramRunnable)
    {
      this.b.runSynchronouslyOnProcessingQueue(paramRunnable);
    }
    
    void c(Runnable paramRunnable)
    {
      this.b.clearAll();
      this.b.runAsynchronouslyOnProcessingQueue(paramRunnable);
    }
    
    public abstract void render();
    
    public abstract void reset();
    
    public void release()
    {
      this.b.quit();
      reset();
    }
  }
  
  public static enum TuSdkMediaPlayerStatus
  {
    private TuSdkMediaPlayerStatus() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\TuSdkMediaMutableFilePlayerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */