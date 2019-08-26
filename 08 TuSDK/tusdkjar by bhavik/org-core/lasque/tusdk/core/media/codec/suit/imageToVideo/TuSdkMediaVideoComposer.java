package org.lasque.tusdk.core.media.codec.suit.imageToVideo;

import android.media.MediaCodec.BufferInfo;
import java.util.LinkedList;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncoderListener;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileEncoder;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileSuitEncoderBase;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkVideoSurfaceEncoderListener;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkVideoSurfaceEncoderListenerImpl;
import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public final class TuSdkMediaVideoComposer
  extends TuSdkMediaFileSuitEncoderBase
{
  private final SelesVerticeCoordinateCorpBuilder a = new SelesVerticeCoordinateCropBuilderImpl(false);
  private TuSdkMediaVideoComposeSync b = new TuSdkMediaVideoComposeSync();
  private TuSdkMediaVideoComposProcesser c;
  private LinkedList<TuSdkComposeItem> d;
  private TuSdkMediaVideoComposeConductor e;
  private boolean f;
  private TuSdkVideoSurfaceEncoderListener g = new TuSdkVideoSurfaceEncoderListenerImpl()
  {
    public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig)
    {
      TuSdkMediaVideoComposer.a(TuSdkMediaVideoComposer.this).initInGLThread();
      TuSdkMediaVideoComposer.b(TuSdkMediaVideoComposer.this).run();
      TuSdkMediaVideoComposer.c(TuSdkMediaVideoComposer.this).requestVideoKeyFrame();
    }
    
    public void onEncoderDrawFrame(long paramAnonymousLong, boolean paramAnonymousBoolean)
    {
      TuSdkMediaVideoComposer.e(TuSdkMediaVideoComposer.this).syncVideoEncodecDrawFrame(paramAnonymousLong, paramAnonymousBoolean, TuSdkMediaVideoComposer.a(TuSdkMediaVideoComposer.this), TuSdkMediaVideoComposer.d(TuSdkMediaVideoComposer.this).getVideoEncoder());
    }
    
    public void onEncoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkMediaVideoComposer.a(TuSdkMediaVideoComposer.this, false);
    }
    
    public void onEncoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException == null)
      {
        TLog.d("%s encodec Video updatedToEOS", new Object[] { "TuSdkMediaVideoComposer" });
        TuSdkMediaVideoComposer.a(TuSdkMediaVideoComposer.this, false);
      }
      else
      {
        TLog.e(paramAnonymousException, "%s VideoEncoderListener thread catch exception, The thread will exit.", new Object[] { "TuSdkMediaVideoComposer" });
      }
    }
  };
  private TuSdkEncoderListener h = new TuSdkEncoderListener()
  {
    public void onEncoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TLog.LOG_AUDIO_ENCODEC_INFO) {
        TuSdkCodecCapabilities.logBufferInfo("AudioEncoderListener updated", paramAnonymousBufferInfo);
      }
    }
    
    public void onEncoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException == null)
      {
        TLog.d("%s encodec Audio updatedToEOS", new Object[] { "TuSdkMediaVideoComposer" });
        TuSdkMediaVideoComposer.a(TuSdkMediaVideoComposer.this, false);
      }
      else
      {
        TLog.e(paramAnonymousException, "%s AudioEncoderListener thread catch exception, The thread will exit.", new Object[] { "TuSdkMediaVideoComposer" });
      }
      TuSdkMediaVideoComposer.a(TuSdkMediaVideoComposer.this, paramAnonymousException);
    }
  };
  
  public void setInputComposList(LinkedList<TuSdkComposeItem> paramLinkedList)
  {
    if ((paramLinkedList == null) || (paramLinkedList.size() == 0))
    {
      TLog.w("%s set input compose item list is invalid", new Object[] { "TuSdkMediaVideoComposer" });
      return;
    }
    this.d = paramLinkedList;
  }
  
  public final boolean run(TuSdkMediaProgress paramTuSdkMediaProgress)
  {
    if (!SdkValid.shared.videoImageComposeSupport())
    {
      TLog.e("You are not allowed to use image video compositing , please see https://tutucloud.com", new Object[0]);
      return false;
    }
    if ((this.d == null) || (this.d.size() == 0))
    {
      TLog.w("%s run need a input file path.", new Object[] { "TuSdkMediaVideoComposer" });
      return false;
    }
    return super.run(paramTuSdkMediaProgress);
  }
  
  protected boolean _init()
  {
    if (!a())
    {
      TLog.w("%s init Encodec Environment failed.", new Object[] { "TuSdkMediaVideoComposer" });
      return false;
    }
    return true;
  }
  
  private boolean a()
  {
    this.e = new TuSdkMediaVideoComposeConductor();
    this.e.setItemList(this.d);
    this.e.setIsAllKeyFrame(this.f);
    this.a.setOutputSize(this.mEncoder.getOutputSize());
    this.a.setEnableClip(false);
    this.c = new TuSdkMediaVideoComposProcesser();
    this.c.setTextureCoordinateBuilder(this.a);
    this.c.setCanvasColor(0.0F, 0.0F, 0.0F, 1.0F);
    this.c.addTarget(this.mEncoder.getFilterBridge());
    this.c.setInputRotation(ImageOrientation.Up);
    this.e.setComposProcesser(this.c);
    this.mEncoder.setSurfaceRender(this.mSurfaceRender);
    this.mEncoder.setAudioRender(this.mAudioRender);
    this.mEncoder.setMediaSync(this.b);
    this.mEncoder.setListener(this.g, this.h);
    this.e.setMediaFileEncoder(this.mEncoder);
    boolean bool = this.mEncoder.prepare(null);
    return bool;
  }
  
  private void a(boolean paramBoolean)
  {
    final float f1 = paramBoolean ? 1.0F : this.e.calculateProgress();
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        if (TuSdkMediaVideoComposer.f(TuSdkMediaVideoComposer.this) == null) {
          return;
        }
        TuSdkMediaVideoComposer.g(TuSdkMediaVideoComposer.this).onProgress(f1, null, 1, 1);
      }
    });
  }
  
  private void a(final Exception paramException)
  {
    if (paramException == null) {
      this.mEncoder.cleanTemp();
    }
    a(true);
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        TuSdkMediaVideoComposer.this.stop();
        TuSdkMediaVideoComposer.h(TuSdkMediaVideoComposer.this).cleanTemp();
        if (TuSdkMediaVideoComposer.i(TuSdkMediaVideoComposer.this) == null) {
          return;
        }
        TuSdkMediaVideoComposer.k(TuSdkMediaVideoComposer.this).onCompleted(paramException, TuSdkMediaVideoComposer.j(TuSdkMediaVideoComposer.this).getOutputDataSource(), 1);
      }
    });
  }
  
  public void stop()
  {
    super.stop();
  }
  
  public void setIsAllKeyFrame(boolean paramBoolean)
  {
    this.f = paramBoolean;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\imageToVideo\TuSdkMediaVideoComposer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */