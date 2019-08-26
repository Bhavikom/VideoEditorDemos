package org.lasque.tusdk.core.media.codec.suit;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.MediaCodec.BufferInfo;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkDecoderListener;
import org.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFileDecoder;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncoderListener;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileEncoder;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileSuitEncoderBase;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkVideoSurfaceEncoderListener;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkVideoSurfaceEncoderListenerImpl;
import org.lasque.tusdk.core.media.codec.exception.TuSdkTaskExitException;
import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileCuterSync;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileDirectorSync;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileSync;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

@TargetApi(18)
public class TuSdkMediaFileCuterImpl
  extends TuSdkMediaFileSuitEncoderBase
  implements TuSdkMediaFileCuter
{
  private final TuSdkMediaFileSync a;
  private final SelesVerticeCoordinateCorpBuilder b = new SelesVerticeCoordinateCropBuilderImpl(false);
  private TuSdkMediaDataSource c;
  private float d = 0.0F;
  private float e = 0.0F;
  private float f = 0.0F;
  private float g = 1.0F;
  private SelesSurfaceReceiver h;
  private TuSdkMediaFileDecoder i;
  private TuSdkVideoSurfaceEncoderListener j = new TuSdkVideoSurfaceEncoderListenerImpl()
  {
    public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig)
    {
      if (TuSdkMediaFileCuterImpl.i(TuSdkMediaFileCuterImpl.this) == null) {
        return;
      }
      TuSdkMediaFileCuterImpl.i(TuSdkMediaFileCuterImpl.this).initInGLThread();
    }
    
    public void onEncoderDrawFrame(long paramAnonymousLong, boolean paramAnonymousBoolean)
    {
      TuSdkMediaFileCuterImpl.g(TuSdkMediaFileCuterImpl.this).syncVideoEncodecDrawFrame(paramAnonymousLong, paramAnonymousBoolean, TuSdkMediaFileCuterImpl.i(TuSdkMediaFileCuterImpl.this), TuSdkMediaFileCuterImpl.j(TuSdkMediaFileCuterImpl.this).getVideoEncoder());
    }
    
    public void onEncoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkMediaFileCuterImpl.a(TuSdkMediaFileCuterImpl.this, false);
    }
    
    public void onEncoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException == null)
      {
        TLog.d("%s encodec Video updatedToEOS", new Object[] { "TuSdkMediaFileCuterImpl" });
        TuSdkMediaFileCuterImpl.a(TuSdkMediaFileCuterImpl.this, true);
      }
      else
      {
        TLog.e(paramAnonymousException, "%s VideoEncoderListener thread catch exception, The thread will exit.", new Object[] { "TuSdkMediaFileCuterImpl" });
      }
      TuSdkMediaFileCuterImpl.a(TuSdkMediaFileCuterImpl.this, paramAnonymousException);
    }
  };
  private TuSdkDecoderListener k = new TuSdkDecoderListener()
  {
    public void onDecoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo) {}
    
    public void onDecoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException != null)
      {
        TuSdkMediaFileCuterImpl.a(TuSdkMediaFileCuterImpl.this, paramAnonymousException);
        return;
      }
      TLog.d("%s VideoDecoderListenerprocess buffer stream end", new Object[] { "TuSdkMediaFileCuterImpl" });
      TuSdkMediaFileCuterImpl.k(TuSdkMediaFileCuterImpl.this);
      TuSdkMediaFileCuterImpl.l(TuSdkMediaFileCuterImpl.this);
    }
  };
  private TuSdkDecoderListener l = new TuSdkDecoderListener()
  {
    public void onDecoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo) {}
    
    public void onDecoderCompleted(Exception paramAnonymousException)
    {
      if ((paramAnonymousException != null) && ((paramAnonymousException instanceof TuSdkTaskExitException)))
      {
        TuSdkMediaFileCuterImpl.a(TuSdkMediaFileCuterImpl.this, paramAnonymousException);
        return;
      }
      if (paramAnonymousException != null) {
        TLog.e(paramAnonymousException, "%s AudioDecoderListener catch a exception, skip audio and ignore.", new Object[] { "TuSdkMediaFileCuterImpl" });
      }
      if (!TuSdkMediaFileCuterImpl.g(TuSdkMediaFileCuterImpl.this).isAudioDecodeCrashed()) {
        TLog.d("%s AudioDecoderListener process buffer stream end", new Object[] { "TuSdkMediaFileCuterImpl" });
      }
      TuSdkMediaFileCuterImpl.m(TuSdkMediaFileCuterImpl.this);
    }
  };
  private TuSdkEncoderListener m = new TuSdkEncoderListener()
  {
    public void onEncoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TLog.LOG_AUDIO_ENCODEC_INFO) {
        TuSdkCodecCapabilities.logBufferInfo("AudioEncoderListener updated", paramAnonymousBufferInfo);
      }
    }
    
    public void onEncoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException == null) {
        TLog.d("%s encodec Audio updatedToEOS", new Object[] { "TuSdkMediaFileCuterImpl" });
      } else {
        TLog.e(paramAnonymousException, "%s AudioEncoderListener thread catch exception, The thread will exit.", new Object[] { "TuSdkMediaFileCuterImpl" });
      }
      TuSdkMediaFileCuterImpl.a(TuSdkMediaFileCuterImpl.this, paramAnonymousException);
    }
  };
  
  public TuSdkMediaFileCuterImpl()
  {
    this(new TuSdkMediaFileCuterSync());
  }
  
  public TuSdkMediaFileCuterImpl(TuSdkMediaFileSync paramTuSdkMediaFileSync)
  {
    this.a = paramTuSdkMediaFileSync;
  }
  
  public void setMediaDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    this.c = paramTuSdkMediaDataSource;
  }
  
  public void setCropRect(RectF paramRectF)
  {
    if (paramRectF == null) {
      return;
    }
    this.b.setCropRect(paramRectF);
  }
  
  public void setEnableClip(boolean paramBoolean)
  {
    this.b.setEnableClip(paramBoolean);
  }
  
  public void setOutputRatio(float paramFloat)
  {
    this.b.setOutputRatio(paramFloat);
  }
  
  public void setOutputSize(TuSdkSize paramTuSdkSize)
  {
    if (!paramTuSdkSize.isSize()) {
      return;
    }
    this.b.setOutputSize(paramTuSdkSize);
  }
  
  public void setCanvasColor(int paramInt)
  {
    setCanvasColor(Color.red(paramInt) / 255.0F, Color.green(paramInt) / 255.0F, Color.blue(paramInt) / 255.0F, Color.alpha(paramInt) / 255.0F);
  }
  
  public void setCanvasColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.d = paramFloat1;
    this.e = paramFloat2;
    this.f = paramFloat3;
    this.g = paramFloat4;
  }
  
  public void setTimeline(TuSdkMediaTimeline paramTuSdkMediaTimeline)
  {
    if (this.mState != -1)
    {
      TLog.w("%s setTimeline need before run.", new Object[] { "TuSdkMediaFileCuterImpl" });
      return;
    }
    this.a.setTimeline(paramTuSdkMediaTimeline);
  }
  
  public void setTimeSlices(List<TuSdkMediaTimeSlice> paramList)
  {
    if (this.mState != -1)
    {
      TLog.w("%s setTimeSlices need before run.", new Object[] { "TuSdkMediaFileCuterImpl" });
      return;
    }
    this.a.setTimeline(new TuSdkMediaTimeline(paramList));
  }
  
  public void setTimeSlice(TuSdkMediaTimeSlice paramTuSdkMediaTimeSlice)
  {
    if (this.mState != -1)
    {
      TLog.w("%s setTimeSlice need before run.", new Object[] { "TuSdkMediaFileCuterImpl" });
      return;
    }
    this.a.setTimeline(new TuSdkMediaTimeline(paramTuSdkMediaTimeSlice));
  }
  
  public void setTimeSlice(long paramLong1, long paramLong2)
  {
    setTimeSlice(new TuSdkMediaTimeSlice(paramLong1, paramLong2));
  }
  
  public void setTimeSliceDuration(long paramLong1, long paramLong2)
  {
    setTimeSlice(paramLong1, paramLong1 + paramLong2);
  }
  
  public void setTimeSliceScaling(float paramFloat1, float paramFloat2)
  {
    if (this.mState != -1)
    {
      TLog.w("%s setTimeSlices need before run.", new Object[] { "TuSdkMediaFileCuterImpl" });
      return;
    }
    this.a.setTimeline(new TuSdkMediaTimeline(paramFloat1, paramFloat2));
  }
  
  public void setTimeSliceDurationScaling(float paramFloat1, float paramFloat2)
  {
    setTimeSliceScaling(paramFloat1, paramFloat1 + paramFloat2);
  }
  
  public void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    super.setAudioRender(paramTuSdkAudioRender);
    if (this.i != null) {
      this.i.setAudioRender(paramTuSdkAudioRender);
    }
  }
  
  public void setAudioMixerRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    if (this.a == null) {
      return;
    }
    if ((this.a instanceof TuSdkMediaFileDirectorSync)) {
      ((TuSdkMediaFileDirectorSync)this.a).setAudioMixerRender(paramTuSdkAudioRender);
    } else if ((this.a instanceof TuSdkMediaFileCuterSync)) {
      ((TuSdkMediaFileCuterSync)this.a).setAudioMixerRender(paramTuSdkAudioRender);
    }
  }
  
  public boolean run(TuSdkMediaProgress paramTuSdkMediaProgress)
  {
    if ((this.c == null) || (!this.c.isValid()))
    {
      TLog.w("%s run need a input file path.", new Object[] { "TuSdkMediaFileCuterImpl" });
      return false;
    }
    return super.run(paramTuSdkMediaProgress);
  }
  
  public void stop()
  {
    if (this.mState == 1)
    {
      TLog.w("%s already stoped.", new Object[] { "TuSdkMediaFileCuterImpl" });
      return;
    }
    this.mState = 1;
    d();
    f();
    if (this.h != null)
    {
      this.h.destroy();
      this.h = null;
    }
    this.mEncoder.release();
    this.a.release();
  }
  
  private void a(boolean paramBoolean)
  {
    final float f1 = paramBoolean ? 1.0F : this.a.calculateProgress();
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        if (TuSdkMediaFileCuterImpl.a(TuSdkMediaFileCuterImpl.this) == null) {
          return;
        }
        TuSdkMediaFileCuterImpl.c(TuSdkMediaFileCuterImpl.this).onProgress(f1, TuSdkMediaFileCuterImpl.b(TuSdkMediaFileCuterImpl.this), 1, 1);
      }
    });
  }
  
  private void a(final Exception paramException)
  {
    if (paramException == null)
    {
      if (!this.a.isEncodecCompleted()) {
        return;
      }
      this.mEncoder.cleanTemp();
    }
    this.a.setBenchmarkEnd();
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuSdkMediaFileCuterImpl.this.stop();
        if (TuSdkMediaFileCuterImpl.d(TuSdkMediaFileCuterImpl.this) == null) {
          return;
        }
        TuSdkMediaFileCuterImpl.f(TuSdkMediaFileCuterImpl.this).onCompleted(paramException, TuSdkMediaFileCuterImpl.e(TuSdkMediaFileCuterImpl.this).getOutputDataSource(), 1);
      }
    });
    TLog.d("%s runCompleted: %f / %f", new Object[] { "TuSdkMediaFileCuterImpl", Float.valueOf((float)this.a.benchmarkUs() / 1000000.0F), Float.valueOf((float)this.a.totalDurationUs() / 1000000.0F) });
  }
  
  protected boolean _init()
  {
    if (!a())
    {
      TLog.w("%s init Encodec Environment failed.", new Object[] { "TuSdkMediaFileCuterImpl" });
      return false;
    }
    b();
    return true;
  }
  
  private boolean a()
  {
    this.b.setOutputSize(this.mEncoder.getOutputSize());
    this.h = new SelesSurfaceReceiver();
    this.h.setTextureCoordinateBuilder(this.b);
    this.h.addTarget(this.mEncoder.getFilterBridge(), 0);
    this.h.setCanvasColor(this.d, this.e, this.f, this.g);
    this.h.setSurfaceTextureListener(new SurfaceTexture.OnFrameAvailableListener()
    {
      public void onFrameAvailable(SurfaceTexture paramAnonymousSurfaceTexture)
      {
        TuSdkMediaFileCuterImpl.h(TuSdkMediaFileCuterImpl.this).requestVideoRender(TuSdkMediaFileCuterImpl.g(TuSdkMediaFileCuterImpl.this).lastVideoDecodecTimestampNs());
      }
    });
    this.a.addAudioEncodecOperation(this.mEncoder.getAudioOperation());
    this.mEncoder.setSurfaceRender(this.mSurfaceRender);
    this.mEncoder.setAudioRender(this.mAudioRender);
    this.mEncoder.setMediaSync(this.a);
    this.mEncoder.setListener(this.j, this.m);
    boolean bool = this.mEncoder.prepare(null);
    return bool;
  }
  
  private void b()
  {
    this.mEncoder.requestVideoKeyFrame();
    this.i = new TuSdkMediaFileDecoder(true, this.mEncoder.hasAudioEncoder());
    this.i.setMediaDataSource(this.c);
    this.i.setMediaSync(this.a);
    this.i.setSurfaceReceiver(this.h);
    this.i.setAudioRender(this.mAudioRender);
    this.i.setListener(this.k, this.l);
    this.i.prepare();
    if (!this.i.isVideoStared())
    {
      c();
      return;
    }
    if ((this.mEncoder.hasAudioEncoder()) && (!this.i.isAudioStared())) {
      e();
    }
  }
  
  private void c()
  {
    if (this.a.isVideoDecodeCompleted()) {
      return;
    }
    this.mEncoder.signalVideoEndOfInputStream();
    d();
    this.a.syncVideoDecodeCompleted();
  }
  
  private void d()
  {
    if (this.i == null) {
      return;
    }
    this.i.releaseVideoDecoder();
  }
  
  private void e()
  {
    if (this.a.isAudioDecodeCompleted()) {
      return;
    }
    if (!this.a.isAudioDecodeCrashed()) {
      g();
    }
    f();
    this.a.syncAudioDecodeCompleted();
  }
  
  private void f()
  {
    if (this.i == null) {
      return;
    }
    this.i.releaseAudioDecoder();
  }
  
  private void g()
  {
    this.mEncoder.signalAudioEndOfInputStream(this.a.totalDurationUs());
  }
  
  private void h()
  {
    if (!this.a.isAudioDecodeCrashed()) {
      return;
    }
    this.mEncoder.autoFillAudioMuteData(0L, this.a.totalDurationUs(), true);
  }
  
  public TuSdkMediaTimeline getTimeLine()
  {
    if ((this.a instanceof TuSdkMediaFileDirectorSync)) {
      return ((TuSdkMediaFileDirectorSync)this.a).getTimeLine();
    }
    return null;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\TuSdkMediaFileCuterImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */