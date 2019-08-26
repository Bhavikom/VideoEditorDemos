package org.lasque.tusdk.core.media.record;

import android.media.MediaFormat;
import android.opengl.GLSurfaceView.Renderer;
import java.io.File;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import org.lasque.tusdk.core.media.codec.suit.TuSdkMediaFileRecorder.TuSdkMediaFileRecorderProgress;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.sources.SelesWatermark;

public abstract interface TuSdkMediaRecordHub
{
  public abstract void setRecordListener(TuSdkMediaRecordHubListener paramTuSdkMediaRecordHubListener);
  
  public abstract void setWatermark(SelesWatermark paramSelesWatermark);
  
  public abstract void setOutputVideoFormat(MediaFormat paramMediaFormat);
  
  public abstract void setOutputAudioFormat(MediaFormat paramMediaFormat);
  
  public abstract void appendRecordSurface(TuSdkRecordSurface paramTuSdkRecordSurface);
  
  public abstract void setSurfaceRender(TuSdkSurfaceRender paramTuSdkSurfaceRender);
  
  public abstract void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender);
  
  public abstract TuSdkMediaRecordHubStatus getState();
  
  public abstract boolean start(File paramFile);
  
  public abstract void stop();
  
  public abstract boolean pause();
  
  public abstract boolean resume();
  
  public abstract void reset();
  
  public abstract void changeSpeed(float paramFloat);
  
  public abstract void changePitch(float paramFloat);
  
  public abstract void release();
  
  public abstract void addTarget(SelesContext.SelesInput paramSelesInput, int paramInt);
  
  public abstract void removeTarget(SelesContext.SelesInput paramSelesInput);
  
  public abstract GLSurfaceView.Renderer getExtenalRenderer();
  
  public abstract void initInGLThread();
  
  public abstract void newFrameReadyInGLThread();
  
  public static abstract interface TuSdkMediaRecordHubListener
    extends TuSdkMediaFileRecorder.TuSdkMediaFileRecorderProgress
  {
    public abstract void onStatusChanged(TuSdkMediaRecordHub.TuSdkMediaRecordHubStatus paramTuSdkMediaRecordHubStatus, TuSdkMediaRecordHub paramTuSdkMediaRecordHub);
  }
  
  public static enum TuSdkMediaRecordHubStatus
  {
    private TuSdkMediaRecordHubStatus() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\record\TuSdkMediaRecordHub.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */