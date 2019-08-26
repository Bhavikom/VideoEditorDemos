package org.lasque.tusdk.core.media.codec.encoder;

import android.graphics.RectF;
import android.media.MediaFormat;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import org.lasque.tusdk.core.seles.sources.SelesWatermark;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract class TuSdkMediaFileSuitEncoderBase
{
  protected static final int TRANS_STATE_UNINITIALIZED = -1;
  protected static final int TRANS_STATE_STARTED = 0;
  protected static final int TRANS_STATE_STOPPED = 1;
  protected final TuSdkMediaFileEncoder mEncoder = new TuSdkMediaFileEncoder();
  protected TuSdkMediaProgress mProgress;
  protected int mState = -1;
  protected TuSdkSurfaceRender mSurfaceRender;
  protected TuSdkAudioRender mAudioRender;
  
  public void setOutputFilePath(String paramString)
  {
    this.mEncoder.setOutputFilePath(paramString);
  }
  
  public int setOutputVideoFormat(MediaFormat paramMediaFormat)
  {
    return this.mEncoder.setOutputVideoFormat(paramMediaFormat);
  }
  
  public int setOutputAudioFormat(MediaFormat paramMediaFormat)
  {
    return this.mEncoder.setOutputAudioFormat(paramMediaFormat);
  }
  
  public TuSdkAudioInfo getOutputAudioInfo()
  {
    return this.mEncoder.getOutputAudioInfo();
  }
  
  public void setSurfaceRender(TuSdkSurfaceRender paramTuSdkSurfaceRender)
  {
    this.mSurfaceRender = paramTuSdkSurfaceRender;
    this.mEncoder.setSurfaceRender(paramTuSdkSurfaceRender);
  }
  
  public void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender)
  {
    this.mAudioRender = paramTuSdkAudioRender;
    this.mEncoder.setAudioRender(paramTuSdkAudioRender);
  }
  
  public void setWatermark(SelesWatermark paramSelesWatermark)
  {
    this.mEncoder.setWatermark(paramSelesWatermark);
  }
  
  public void setOutputOrientation(ImageOrientation paramImageOrientation)
  {
    this.mEncoder.setOutputOrientation(paramImageOrientation);
  }
  
  public void setCanvasRect(RectF paramRectF)
  {
    this.mEncoder.setCanvasRect(paramRectF);
  }
  
  public TuSdkFilterBridge getFilterBridge()
  {
    return this.mEncoder.getFilterBridge();
  }
  
  public void setFilterBridge(TuSdkFilterBridge paramTuSdkFilterBridge)
  {
    this.mEncoder.setFilterBridge(paramTuSdkFilterBridge);
  }
  
  public void disconnect()
  {
    this.mEncoder.disconnect();
  }
  
  public boolean run(TuSdkMediaProgress paramTuSdkMediaProgress)
  {
    if (this.mState != -1)
    {
      TLog.w("%s run can not after initialized.", new Object[] { "TuSdkMediaFileSuitEncoderBase" });
      return false;
    }
    if (this.mEncoder.getOutputDataSource() == null)
    {
      TLog.w("%s run need a output file path.", new Object[] { "TuSdkMediaFileSuitEncoderBase" });
      return false;
    }
    if (!this.mEncoder.hasVideoEncoder())
    {
      TLog.w("%s run need set Output Video Format.", new Object[] { "TuSdkMediaFileSuitEncoderBase" });
      return false;
    }
    if (!this.mEncoder.hasAudioEncoder()) {
      TLog.w("%s run can not find Output Audio Format, then ignore audio.", new Object[] { "TuSdkMediaFileSuitEncoderBase" });
    }
    this.mProgress = paramTuSdkMediaProgress;
    this.mState = 0;
    boolean bool = _init();
    return bool;
  }
  
  public void stop()
  {
    if (this.mState == 1)
    {
      TLog.w("%s already stoped.", new Object[] { "TuSdkMediaFileSuitEncoderBase" });
      return;
    }
    this.mState = 1;
    this.mEncoder.release();
  }
  
  protected void finalize()
  {
    stop();
    super.finalize();
  }
  
  protected abstract boolean _init();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\encoder\TuSdkMediaFileSuitEncoderBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */