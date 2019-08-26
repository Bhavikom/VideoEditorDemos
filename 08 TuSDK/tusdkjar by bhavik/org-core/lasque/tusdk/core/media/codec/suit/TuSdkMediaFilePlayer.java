package org.lasque.tusdk.core.media.codec.suit;

import android.opengl.GLSurfaceView.Renderer;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import org.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public abstract interface TuSdkMediaFilePlayer
{
  public abstract void setMediaDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource);
  
  public abstract void setSurfaceDraw(TuSdkSurfaceDraw paramTuSdkSurfaceDraw);
  
  public abstract void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender);
  
  public abstract void setListener(TuSdkMediaPlayerListener paramTuSdkMediaPlayerListener);
  
  public abstract TuSdkFilterBridge getFilterBridge();
  
  public abstract GLSurfaceView.Renderer getExtenalRenderer();
  
  public abstract boolean load(boolean paramBoolean);
  
  public abstract void initInGLThread();
  
  public abstract void newFrameReadyInGLThread();
  
  public abstract void release();
  
  public abstract boolean isPause();
  
  public abstract void pause();
  
  public abstract void resume();
  
  public abstract void reset();
  
  public abstract boolean isSupportPrecise();
  
  public abstract void setSpeed(float paramFloat);
  
  public abstract float speed();
  
  public abstract void setReverse(boolean paramBoolean);
  
  public abstract boolean isReverse();
  
  public abstract long seekToPercentage(float paramFloat);
  
  public abstract long durationUs();
  
  public abstract long elapsedUs();
  
  public abstract void seekTo(long paramLong);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\TuSdkMediaFilePlayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */