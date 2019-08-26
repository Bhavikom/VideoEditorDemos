package org.lasque.tusdk.core.media.codec.encoder;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import java.io.File;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;

public abstract class TuSdkMediaEncoderBase
{
  public static final int TRANS_STATE_UNINITIALIZED = -1;
  public static final int TRANS_STATE_STARTED = 0;
  public static final int TRANS_STATE_STOPPED = 1;
  protected int mState = -1;
  protected String mOutputFilePath;
  protected TuSdkMediaProgress mProgress;
  protected File mTempFile;
  protected TuSdkSurfaceRender mSurfaceRender;
  protected TuSdkAudioRender mAudioRender;
  protected TuSdkSize mOutputSize;
  protected TuSdkVideoSurfaceEncoder mVideoEncoder;
  protected TuSdkAudioEncoder mAudioEncoder;
  protected TuSdkMediaFileMuxer mMediaMuxer;
  protected final TuSdkVideoSurfaceEncoderListener mVideoEncoderListener = new TuSdkVideoSurfaceEncoderListenerImpl()
  {
    public void onSurfaceCreated(GL10 paramAnonymousGL10, EGLConfig paramAnonymousEGLConfig)
    {
      TuSdkMediaEncoderBase.this._onSurfaceCreated(paramAnonymousGL10, paramAnonymousEGLConfig);
    }
    
    public void onEncoderDrawFrame(long paramAnonymousLong, boolean paramAnonymousBoolean)
    {
      TuSdkMediaEncoderBase.this._onEncoderDrawFrame(paramAnonymousLong, paramAnonymousBoolean);
    }
    
    public void onEncoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkMediaEncoderBase.this._notifyProgress(false);
    }
    
    public void onEncoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException == null)
      {
        TLog.d("%s encodec Video updatedToEOS", new Object[] { "TuSdkMediaEncoderBase" });
        TuSdkMediaEncoderBase.this._notifyProgress(true);
      }
      else
      {
        TLog.e(paramAnonymousException, "%s VideoEncoderListener thread catch exception, The thread will exit.", new Object[] { "TuSdkMediaEncoderBase" });
      }
      TuSdkMediaEncoderBase.this._notifyCompleted(paramAnonymousException);
    }
  };
  protected final TuSdkEncoderListener mAudioEncoderListener = new TuSdkEncoderListener()
  {
    public void onEncoderUpdated(MediaCodec.BufferInfo paramAnonymousBufferInfo)
    {
      TuSdkCodecCapabilities.logBufferInfo("AudioEncoderListener updated", paramAnonymousBufferInfo);
    }
    
    public void onEncoderCompleted(Exception paramAnonymousException)
    {
      if (paramAnonymousException == null)
      {
        TLog.d("%s encodec Audio updatedToEOS", new Object[] { "TuSdkMediaEncoderBase" });
        TuSdkMediaEncoderBase.this._notifyProgress(true);
      }
      else
      {
        TLog.e(paramAnonymousException, "%s AudioEncoderListener thread catch exception, The thread will exit.", new Object[] { "TuSdkMediaEncoderBase" });
      }
      TuSdkMediaEncoderBase.this._notifyCompleted(paramAnonymousException);
    }
  };
  
  public void setOutputFilePath(String paramString)
  {
    if (this.mState != -1)
    {
      TLog.w("%s setOutputFilePath need before run.", new Object[] { "TuSdkMediaEncoderBase" });
      return;
    }
    if (StringHelper.isEmpty(paramString))
    {
      TLog.w("%s setOutputFilePath need a valid file path: %s", new Object[] { "TuSdkMediaEncoderBase", paramString });
      return;
    }
    this.mOutputFilePath = paramString;
  }
  
  public int setOutputVideoFormat(MediaFormat paramMediaFormat)
  {
    if (this.mState != -1)
    {
      TLog.w("%s setOutputVideoFormat need before run.", new Object[] { "TuSdkMediaEncoderBase" });
      return -1;
    }
    this.mVideoEncoder = new TuSdkVideoSurfaceEncoder();
    int i = this.mVideoEncoder.setOutputFormat(paramMediaFormat);
    if (i != 0)
    {
      this.mVideoEncoder = null;
      TLog.w("%s setOutputVideoFormat has a error code: %d, %s", new Object[] { "TuSdkMediaEncoderBase", Integer.valueOf(i), paramMediaFormat });
      return i;
    }
    this.mVideoEncoder.setSurfaceRender(this.mSurfaceRender);
    this.mVideoEncoder.setListener(this.mVideoEncoderListener);
    return 0;
  }
  
  public int setOutputAudioFormat(MediaFormat paramMediaFormat)
  {
    if (this.mState != -1)
    {
      TLog.w("%s setOutputAudioFormat need before run.", new Object[] { "TuSdkMediaEncoderBase" });
      return -1;
    }
    this.mAudioEncoder = new TuSdkAudioEncoder();
    int i = this.mAudioEncoder.setOutputFormat(paramMediaFormat);
    if (i != 0)
    {
      this.mAudioEncoder = null;
      TLog.w("%s setOutputAudioFormat has a error code: %d, %s", new Object[] { "TuSdkMediaEncoderBase", Integer.valueOf(i), paramMediaFormat });
      return i;
    }
    this.mAudioEncoder.setAudioRender(this.mAudioRender);
    this.mAudioEncoder.setListener(this.mAudioEncoderListener);
    return 0;
  }
  
  public void setSurfaceRender(TuSdkSurfaceRender paramTuSdkSurfaceRender)
  {
    this.mSurfaceRender = paramTuSdkSurfaceRender;
    if (this.mVideoEncoder != null) {
      this.mVideoEncoder.setSurfaceRender(paramTuSdkSurfaceRender);
    }
  }
  
  public void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    this.mAudioRender = paramTuSdkAudioRender;
    if (this.mAudioEncoder != null) {
      this.mAudioEncoder.setAudioRender(paramTuSdkAudioRender);
    }
  }
  
  protected abstract boolean _init();
  
  protected abstract void _notifyProgress(boolean paramBoolean);
  
  protected abstract void _notifyCompleted(Exception paramException);
  
  protected abstract void _onSurfaceCreated(GL10 paramGL10, EGLConfig paramEGLConfig);
  
  protected abstract void _onEncoderDrawFrame(long paramLong, boolean paramBoolean);
  
  public boolean run(TuSdkMediaProgress paramTuSdkMediaProgress)
  {
    if (this.mState != -1)
    {
      TLog.w("%s run can not after initialized.", new Object[] { "TuSdkMediaEncoderBase" });
      return false;
    }
    if (this.mOutputFilePath == null)
    {
      TLog.w("%s run need a output file path.", new Object[] { "TuSdkMediaEncoderBase" });
      return false;
    }
    if (this.mVideoEncoder == null)
    {
      TLog.w("%s run need set Output Video Format.", new Object[] { "TuSdkMediaEncoderBase" });
      return false;
    }
    if (this.mAudioEncoder == null) {
      TLog.w("%s run can not find Output Audio Format, then ignore audio.", new Object[] { "TuSdkMediaEncoderBase" });
    }
    this.mTempFile = new File(TuSdk.getAppTempPath(), String.format("lsq_media_tmp_%d.tmp", new Object[] { Long.valueOf(System.currentTimeMillis()) }));
    this.mProgress = paramTuSdkMediaProgress;
    this.mState = 0;
    boolean bool = _init();
    return bool;
  }
  
  public void stop()
  {
    if (this.mState == 1)
    {
      TLog.w("%s already stoped.", new Object[] { "TuSdkMediaEncoderBase" });
      return;
    }
    this.mState = 1;
    if (this.mVideoEncoder != null)
    {
      this.mVideoEncoder.release();
      this.mVideoEncoder = null;
    }
    if (this.mAudioEncoder != null)
    {
      this.mAudioEncoder.release();
      this.mAudioEncoder = null;
    }
    if (this.mMediaMuxer != null)
    {
      this.mMediaMuxer.release();
      this.mMediaMuxer = null;
    }
    FileHelper.delete(this.mTempFile);
  }
  
  protected void finalize()
  {
    stop();
    super.finalize();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\encoder\TuSdkMediaEncoderBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */