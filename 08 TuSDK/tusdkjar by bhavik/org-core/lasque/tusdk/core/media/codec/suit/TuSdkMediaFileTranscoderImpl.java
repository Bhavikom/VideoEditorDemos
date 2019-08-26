package org.lasque.tusdk.core.media.codec.suit;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.MediaCodec.BufferInfo;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaFileTrascoderSync;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

@TargetApi(18)
public class TuSdkMediaFileTranscoderImpl
  extends TuSdkMediaFileSuitEncoderBase
  implements TuSdkMediaFileTranscoder
{
  private final TuSdkMediaFileTrascoderSync a = new TuSdkMediaFileTrascoderSync();
  private final List<TuSdkMediaDataSource> b = new ArrayList(5);
  private SelesSurfaceReceiver c;
  private TuSdkMediaDataSource d;
  private TuSdkMediaFileDecoder e;
  private TuSdkVideoSurfaceEncoderListener f = new TuSdkVideoSurfaceEncoderListenerImpl()
  {
    public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig)
    {
      if (TuSdkMediaFileTranscoderImpl.i(TuSdkMediaFileTranscoderImpl.this) == null) {
        return;
      }
      TuSdkMediaFileTranscoderImpl.i(TuSdkMediaFileTranscoderImpl.this).initInGLThread();
    }
    
    public void onEncoderDrawFrame(long paramAnonymousLong, boolean paramAnonymousBoolean)
    {
      TuSdkMediaFileTranscoderImpl.c(TuSdkMediaFileTranscoderImpl.this).syncVideoEncodecDrawFrame(paramAnonymousLong, paramAnonymousBoolean, TuSdkMediaFileTranscoderImpl.i(TuSdkMediaFileTranscoderImpl.this), TuSdkMediaFileTranscoderImpl.j(TuSdkMediaFileTranscoderImpl.this).getVideoEncoder());
    }
    
    public void onEncoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkMediaFileTranscoderImpl.a(TuSdkMediaFileTranscoderImpl.this, false);
    }
    
    public void onEncoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException == null)
      {
        TLog.d("%s encodec Video updatedToEOS", new Object[] { "TuSdkMediaFileTranscoderImpl" });
        TuSdkMediaFileTranscoderImpl.a(TuSdkMediaFileTranscoderImpl.this, true);
      }
      else
      {
        TLog.e(paramAnonymousException, "%s VideoEncoderListener thread catch exception, The thread will exit.", new Object[] { "TuSdkMediaFileTranscoderImpl" });
      }
      TuSdkMediaFileTranscoderImpl.a(TuSdkMediaFileTranscoderImpl.this, paramAnonymousException);
    }
  };
  private TuSdkDecoderListener g = new TuSdkDecoderListener()
  {
    public void onDecoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo) {}
    
    public void onDecoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException != null)
      {
        TuSdkMediaFileTranscoderImpl.a(TuSdkMediaFileTranscoderImpl.this, paramAnonymousException);
        return;
      }
      TLog.d("%s VideoDecoderListenerprocess buffer stream end", new Object[] { "TuSdkMediaFileTranscoderImpl" });
      TuSdkMediaFileTranscoderImpl.k(TuSdkMediaFileTranscoderImpl.this);
      TuSdkMediaFileTranscoderImpl.l(TuSdkMediaFileTranscoderImpl.this);
    }
  };
  private TuSdkDecoderListener h = new TuSdkDecoderListener()
  {
    public void onDecoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo) {}
    
    public void onDecoderCompleted(Exception paramAnonymousException)
    {
      if ((paramAnonymousException != null) && ((paramAnonymousException instanceof TuSdkTaskExitException)))
      {
        TuSdkMediaFileTranscoderImpl.a(TuSdkMediaFileTranscoderImpl.this, paramAnonymousException);
        return;
      }
      if (paramAnonymousException != null) {
        TLog.e(paramAnonymousException, "%s AudioDecoderListener catch a exception, skip audio and ignore.", new Object[] { "TuSdkMediaFileTranscoderImpl" });
      }
      if (!TuSdkMediaFileTranscoderImpl.c(TuSdkMediaFileTranscoderImpl.this).isAudioDecodeCrashed()) {
        TLog.d("%s AudioDecoderListener process buffer stream end", new Object[] { "TuSdkMediaFileTranscoderImpl" });
      }
      TuSdkMediaFileTranscoderImpl.m(TuSdkMediaFileTranscoderImpl.this);
    }
  };
  private TuSdkEncoderListener i = new TuSdkEncoderListener()
  {
    public void onEncoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkCodecCapabilities.logBufferInfo("AudioEncoderListener updated", paramAnonymousBufferInfo);
    }
    
    public void onEncoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException == null)
      {
        TLog.d("%s encodec Audio updatedToEOS", new Object[] { "TuSdkMediaFileTranscoderImpl" });
        TuSdkMediaFileTranscoderImpl.a(TuSdkMediaFileTranscoderImpl.this, true);
      }
      else
      {
        TLog.e(paramAnonymousException, "%s AudioEncoderListener thread catch exception, The thread will exit.", new Object[] { "TuSdkMediaFileTranscoderImpl" });
      }
      TuSdkMediaFileTranscoderImpl.a(TuSdkMediaFileTranscoderImpl.this, paramAnonymousException);
    }
  };
  
  public void addInputDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
  {
    if (this.mState != -1)
    {
      TLog.w("%s addInputDataSource need before run: %s", new Object[] { "TuSdkMediaFileTranscoderImpl", paramTuSdkMediaDataSource });
      return;
    }
    if ((paramTuSdkMediaDataSource == null) || (!paramTuSdkMediaDataSource.isValid()))
    {
      TLog.w("%s addInputDataSource not exists: %s", new Object[] { "TuSdkMediaFileTranscoderImpl", paramTuSdkMediaDataSource });
      return;
    }
    this.b.add(paramTuSdkMediaDataSource);
  }
  
  public void addInputDataSouces(List<TuSdkMediaDataSource> paramList)
  {
    if ((paramList == null) || (paramList.size() < 1))
    {
      TLog.w("%s addInputDataSouces need least 1 item.", new Object[] { "TuSdkMediaFileTranscoderImpl" });
      return;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkMediaDataSource localTuSdkMediaDataSource = (TuSdkMediaDataSource)localIterator.next();
      addInputDataSource(localTuSdkMediaDataSource);
    }
  }
  
  public void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    super.setAudioRender(paramTuSdkAudioRender);
    if (this.e != null) {
      this.e.setAudioRender(paramTuSdkAudioRender);
    }
  }
  
  public boolean run(TuSdkMediaProgress paramTuSdkMediaProgress)
  {
    if (this.b.size() < 1)
    {
      TLog.w("%s run need a input file path.", new Object[] { "TuSdkMediaFileTranscoderImpl" });
      return false;
    }
    return super.run(paramTuSdkMediaProgress);
  }
  
  public void stop()
  {
    if (this.mState == 1)
    {
      TLog.w("%s already stoped.", new Object[] { "TuSdkMediaFileTranscoderImpl" });
      return;
    }
    this.mState = 1;
    d();
    f();
    if (this.c != null)
    {
      this.c.destroy();
      this.c = null;
    }
    this.mEncoder.release();
    this.a.release();
  }
  
  private void a(boolean paramBoolean)
  {
    final float f1 = paramBoolean ? 1.0F : this.a.calculateProgress();
    final int j = this.a.lastIndex();
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        if (TuSdkMediaFileTranscoderImpl.a(TuSdkMediaFileTranscoderImpl.this) == null) {
          return;
        }
        TuSdkMediaFileTranscoderImpl.d(TuSdkMediaFileTranscoderImpl.this).onProgress(f1, TuSdkMediaFileTranscoderImpl.b(TuSdkMediaFileTranscoderImpl.this), j, TuSdkMediaFileTranscoderImpl.c(TuSdkMediaFileTranscoderImpl.this).total());
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
        TuSdkMediaFileTranscoderImpl.this.stop();
        if (TuSdkMediaFileTranscoderImpl.e(TuSdkMediaFileTranscoderImpl.this) == null) {
          return;
        }
        TuSdkMediaFileTranscoderImpl.g(TuSdkMediaFileTranscoderImpl.this).onCompleted(paramException, TuSdkMediaFileTranscoderImpl.f(TuSdkMediaFileTranscoderImpl.this).getOutputDataSource(), TuSdkMediaFileTranscoderImpl.c(TuSdkMediaFileTranscoderImpl.this).total());
      }
    });
    TLog.d("%s runCompleted: %f / %f", new Object[] { "TuSdkMediaFileTranscoderImpl", Float.valueOf((float)this.a.benchmarkUs() / 1000000.0F), Float.valueOf((float)this.a.totalDurationUs() / 1000000.0F) });
  }
  
  protected boolean _init()
  {
    this.a.setTotal(this.b.size());
    if (!a())
    {
      TLog.w("%s init Encodec Environment failed.", new Object[] { "TuSdkMediaFileTranscoderImpl" });
      return false;
    }
    b();
    return true;
  }
  
  private boolean a()
  {
    SelesVerticeCoordinateCropBuilderImpl localSelesVerticeCoordinateCropBuilderImpl = new SelesVerticeCoordinateCropBuilderImpl(false);
    localSelesVerticeCoordinateCropBuilderImpl.setOutputSize(this.mEncoder.getOutputSize());
    this.c = new SelesSurfaceReceiver();
    this.c.setTextureCoordinateBuilder(localSelesVerticeCoordinateCropBuilderImpl);
    this.c.addTarget(this.mEncoder.getFilterBridge(), 0);
    this.c.setSurfaceTextureListener(new SurfaceTexture.OnFrameAvailableListener()
    {
      public void onFrameAvailable(SurfaceTexture paramAnonymousSurfaceTexture)
      {
        TuSdkMediaFileTranscoderImpl.h(TuSdkMediaFileTranscoderImpl.this).requestVideoRender(TuSdkMediaFileTranscoderImpl.c(TuSdkMediaFileTranscoderImpl.this).lastVideoDecodecTimestampNs());
      }
    });
    this.a.addAudioEncodecOperation(this.mEncoder.getAudioOperation());
    this.mEncoder.setSurfaceRender(this.mSurfaceRender);
    this.mEncoder.setAudioRender(this.mAudioRender);
    this.mEncoder.setMediaSync(this.a);
    this.mEncoder.setListener(this.f, this.i);
    boolean bool = this.mEncoder.prepare(null);
    return bool;
  }
  
  private void b()
  {
    if ((this.mState != 0) || (!this.a.syncDecodecNext())) {
      return;
    }
    this.mEncoder.requestVideoKeyFrame();
    this.d = ((TuSdkMediaDataSource)this.b.get(this.a.lastIndex()));
    this.e = new TuSdkMediaFileDecoder(true, this.mEncoder.hasAudioEncoder());
    this.e.setMediaDataSource(this.d);
    this.e.setMediaSync(this.a);
    this.e.setSurfaceReceiver(this.c);
    this.e.setAudioRender(this.mAudioRender);
    this.e.setListener(this.g, this.h);
    this.e.prepare();
    if (!this.e.isVideoStared())
    {
      c();
      return;
    }
    if ((this.mEncoder.hasAudioEncoder()) && (!this.e.isAudioStared())) {
      e();
    }
  }
  
  private void c()
  {
    if (this.a.isVideoDecodeCompleted()) {
      return;
    }
    if (this.a.isLast()) {
      this.mEncoder.signalVideoEndOfInputStream();
    }
    d();
    this.a.syncVideoDecodeCompleted();
    b();
  }
  
  private void d()
  {
    if (this.e == null) {
      return;
    }
    this.e.releaseVideoDecoder();
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
    b();
  }
  
  private void f()
  {
    if (this.e == null) {
      return;
    }
    this.e.releaseAudioDecoder();
  }
  
  private void g()
  {
    if (this.a.isLast()) {
      this.mEncoder.signalAudioEndOfInputStream(this.a.totalDurationUs());
    }
  }
  
  private void h()
  {
    if (!this.a.isAudioDecodeCrashed()) {
      return;
    }
    this.mEncoder.autoFillAudioMuteData(this.a.lastVideoEndTimeUs(), this.a.totalDurationUs(), this.a.isLast());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\TuSdkMediaFileTranscoderImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */