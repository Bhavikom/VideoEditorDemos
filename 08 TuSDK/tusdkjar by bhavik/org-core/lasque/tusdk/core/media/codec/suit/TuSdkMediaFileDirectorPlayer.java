package org.lasque.tusdk.core.media.codec.suit;

import android.opengl.GLSurfaceView.Renderer;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkFilterBridge;
import org.lasque.tusdk.core.api.extend.TuSdkMediaPlayerListener;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceDraw;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;

public abstract interface TuSdkMediaFileDirectorPlayer
{
  public abstract void setMediaDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource);
  
  public abstract void setSurfaceDraw(TuSdkSurfaceDraw paramTuSdkSurfaceDraw);
  
  public abstract void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender);
  
  public abstract void setAudioMixerRender(TuSdkAudioRender paramTuSdkAudioRender);
  
  public abstract void setListener(TuSdkMediaPlayerListener paramTuSdkMediaPlayerListener);
  
  public abstract TuSdkFilterBridge getFilterBridge();
  
  public abstract GLSurfaceView.Renderer getExtenalRenderer();
  
  public abstract void setCanvasColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  public abstract void setCanvasColor(int paramInt);
  
  public abstract boolean load(boolean paramBoolean);
  
  public abstract void initInGLThread();
  
  public abstract void newFrameReadyInGLThread();
  
  public abstract void release();
  
  public abstract boolean isPause();
  
  public abstract void pause();
  
  public abstract void resume();
  
  public abstract void reset();
  
  public abstract long seekToPercentage(float paramFloat);
  
  public abstract void seekTo(long paramLong);
  
  public abstract long durationUs();
  
  public abstract long outputTimeUs();
  
  public abstract void setEnableClip(boolean paramBoolean);
  
  public abstract TuSdkSize setOutputRatio(float paramFloat);
  
  public abstract void setOutputSize(TuSdkSize paramTuSdkSize);
  
  public abstract void preview(TuSdkMediaTimeline paramTuSdkMediaTimeline);
  
  public abstract int setVolume(float paramFloat);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\TuSdkMediaFileDirectorPlayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */