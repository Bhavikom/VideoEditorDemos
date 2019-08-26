package org.lasque.tusdk.core.media.codec.suit;

import android.media.MediaCodec.BufferInfo;
import android.opengl.EGLContext;
import java.nio.ByteBuffer;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncoderListener;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileEncoder;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileSuitEncoderBase;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkVideoSurfaceEncoder;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkVideoSurfaceEncoderListener;
import org.lasque.tusdk.core.media.codec.encoder.TuSdkVideoSurfaceEncoderListenerImpl;
import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaRecorderSync;
import org.lasque.tusdk.core.seles.egl.SelesVirtualDisplay;
import org.lasque.tusdk.core.seles.egl.SelesVirtualDisplayPatch;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;

public class TuSdkMediaFileRecorderImpl
  extends TuSdkMediaFileSuitEncoderBase
  implements TuSdkMediaFileRecorder
{
  private final TuSdkMediaRecorderSync a = new TuSdkMediaRecorderSync();
  private EGLContext b;
  private TuSdkMediaFileRecorder.TuSdkMediaFileRecorderProgress c;
  private TuSdkVideoSurfaceEncoderListener d = new TuSdkVideoSurfaceEncoderListenerImpl()
  {
    public void onEncoderDrawFrame(long paramAnonymousLong, boolean paramAnonymousBoolean)
    {
      TuSdkMediaFileRecorderImpl.b(TuSdkMediaFileRecorderImpl.this).syncVideoEncodecDrawFrame(paramAnonymousLong, paramAnonymousBoolean, TuSdkMediaFileRecorderImpl.g(TuSdkMediaFileRecorderImpl.this).getVideoEncoder());
    }
    
    public void onEncoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkMediaFileRecorderImpl.h(TuSdkMediaFileRecorderImpl.this);
    }
    
    public void onEncoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException == null)
      {
        TLog.d("%s encodec Video updatedToEOS", new Object[] { "TuSdkMediaFileRecorderImpl" });
        TuSdkMediaFileRecorderImpl.h(TuSdkMediaFileRecorderImpl.this);
      }
      else
      {
        TLog.e(paramAnonymousException, "%s VideoEncoderListener thread catch exception, The thread will exit.", new Object[] { "TuSdkMediaFileRecorderImpl" });
      }
      TuSdkMediaFileRecorderImpl.a(TuSdkMediaFileRecorderImpl.this, paramAnonymousException);
    }
  };
  private TuSdkEncoderListener e = new TuSdkEncoderListener()
  {
    public void onEncoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      if (TLog.LOG_VIDEO_ENCODEC_INFO) {
        TuSdkCodecCapabilities.logBufferInfo("AudioEncoderListener updated", paramAnonymousBufferInfo);
      }
    }
    
    public void onEncoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException == null) {
        TLog.d("%s encodec Audio updatedToEOS", new Object[] { "TuSdkMediaFileRecorderImpl" });
      } else {
        TLog.e(paramAnonymousException, "%s AudioEncoderListener thread catch exception, The thread will exit.", new Object[] { "TuSdkMediaFileRecorderImpl" });
      }
      TuSdkMediaFileRecorderImpl.a(TuSdkMediaFileRecorderImpl.this, paramAnonymousException);
    }
  };
  
  public void release()
  {
    stop();
    this.a.release();
  }
  
  public void setRecordProgress(TuSdkMediaFileRecorder.TuSdkMediaFileRecorderProgress paramTuSdkMediaFileRecorderProgress)
  {
    this.c = paramTuSdkMediaFileRecorderProgress;
  }
  
  private void a()
  {
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        if (TuSdkMediaFileRecorderImpl.a(TuSdkMediaFileRecorderImpl.this) == null) {
          return;
        }
        TuSdkMediaFileRecorderImpl.a(TuSdkMediaFileRecorderImpl.this).onProgress(TuSdkMediaFileRecorderImpl.b(TuSdkMediaFileRecorderImpl.this).getVideoEncodecTimeUs(), TuSdkMediaFileRecorderImpl.c(TuSdkMediaFileRecorderImpl.this).getOutputDataSource());
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
    TLog.d("%s runCompleted: %f", new Object[] { "TuSdkMediaFileRecorderImpl", Float.valueOf((float)this.a.getVideoEncodecTimeUs() / 1000000.0F) });
    stop();
    ThreadHelper.post(new Runnable()
    {
      public void run()
      {
        if (TuSdkMediaFileRecorderImpl.a(TuSdkMediaFileRecorderImpl.this) == null) {
          return;
        }
        TuSdkMediaFileRecorderImpl.a(TuSdkMediaFileRecorderImpl.this).onCompleted(paramException, TuSdkMediaFileRecorderImpl.d(TuSdkMediaFileRecorderImpl.this).getOutputDataSource(), TuSdkMediaFileRecorderImpl.b(TuSdkMediaFileRecorderImpl.this).endOfTimeline());
      }
    });
  }
  
  public void changeSpeed(float paramFloat)
  {
    this.a.changeSpeed(paramFloat);
  }
  
  public boolean startRecord(EGLContext paramEGLContext)
  {
    this.b = paramEGLContext;
    return run(this.mProgress);
  }
  
  protected boolean _init()
  {
    if (this.mState == 1)
    {
      TLog.w("%s init Encodec Environment has released.", new Object[] { "TuSdkMediaFileRecorderImpl" });
      return false;
    }
    if (!b())
    {
      TLog.w("%s init Encodec Environment failed.", new Object[] { "TuSdkMediaFileRecorderImpl" });
      return false;
    }
    this.mEncoder.getVideoEncoder().getVirtualDisplay().setSyncRender(SelesVirtualDisplayPatch.isNeedVirtualDisplayPatch());
    return true;
  }
  
  private boolean b()
  {
    this.mEncoder.setSurfaceRender(this.mSurfaceRender);
    this.mEncoder.setAudioRender(this.mAudioRender);
    this.mEncoder.setMediaSync(this.a);
    this.mEncoder.setListener(this.d, this.e);
    boolean bool = this.mEncoder.prepare(this.b, true);
    if (bool) {
      this.a.setAudioOperation(this.mEncoder.getAudioOperation());
    }
    return bool;
  }
  
  public void stopRecord()
  {
    if (this.mState == 1) {
      return;
    }
    pauseRecord();
    ThreadHelper.runThread(new Runnable()
    {
      public void run()
      {
        TuSdkMediaFileRecorderImpl.e(TuSdkMediaFileRecorderImpl.this).signalVideoEndOfInputStream();
        TuSdkMediaFileRecorderImpl.f(TuSdkMediaFileRecorderImpl.this).autoFillAudioMuteData(TuSdkMediaFileRecorderImpl.b(TuSdkMediaFileRecorderImpl.this).getAudioEncodecTimeUs(), Math.max(TuSdkMediaFileRecorderImpl.b(TuSdkMediaFileRecorderImpl.this).getVideoEncodecTimeUs(), TuSdkMediaFileRecorderImpl.b(TuSdkMediaFileRecorderImpl.this).getAudioEncodecTimeUs()), true);
      }
    });
  }
  
  public void pauseRecord()
  {
    if (this.mState == 1) {
      return;
    }
    this.mEncoder.setSurfacePause(true);
    this.a.pauseRecord();
  }
  
  public void resumeRecord()
  {
    if (this.mState == 1) {
      return;
    }
    this.a.resumeRecord();
    this.mEncoder.setSurfacePause(false);
  }
  
  public void newFrameReadyInGLThread(long paramLong)
  {
    if (this.mState == 1) {
      return;
    }
    this.mEncoder.requestVideoRender(paramLong);
  }
  
  public void newFrameReadyWithAudio(ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if (this.mState == 1) {
      return;
    }
    this.a.syncAudioEncodecFrame(paramByteBuffer, paramBufferInfo);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\TuSdkMediaFileRecorderImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */