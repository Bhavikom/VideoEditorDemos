package org.lasque.tusdk.core.seles.sources;

import android.view.ViewGroup;
import java.util.ArrayList;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.video.editor.TuSdkMediaTimeEffect;

public abstract interface TuSdkEditorPlayer
{
  public abstract void setPreviewContainer(ViewGroup paramViewGroup);
  
  public abstract void setDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource);
  
  public abstract void setEditTimeSlice(ArrayList<TuSdkMediaTimeSlice> paramArrayList);
  
  public abstract void enableFirstFramePause(boolean paramBoolean);
  
  public abstract void setCanvasColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  public abstract void setCanvasColor(int paramInt);
  
  public abstract void setBackGround(int paramInt);
  
  public abstract void startPreview();
  
  public abstract void pausePreview();
  
  public abstract boolean isPause();
  
  public abstract void seekTimeUs(long paramLong);
  
  public abstract void seekOutputTimeUs(long paramLong);
  
  public abstract void seekInputTimeUs(long paramLong);
  
  public abstract long getCurrentTimeUs();
  
  public abstract long getTotalTimeUs();
  
  public abstract long getCurrentInputTimeUs();
  
  public abstract long getCurrentOutputTimeUs();
  
  public abstract long getOutputTotalTimeUS();
  
  public abstract long getInputTotalTimeUs();
  
  public abstract void setOutputRatio(float paramFloat, boolean paramBoolean);
  
  public abstract void setOutputSize(TuSdkSize paramTuSdkSize, boolean paramBoolean);
  
  public abstract void setVideoSoundVolume(float paramFloat);
  
  public abstract void addProgressListener(TuSdkProgressListener paramTuSdkProgressListener);
  
  public abstract void removeProgressListener(TuSdkProgressListener paramTuSdkProgressListener);
  
  public abstract void removeAllProgressListener();
  
  public abstract void addPreviewSizeChangeListener(TuSdkPreviewSizeChangeListener paramTuSdkPreviewSizeChangeListener);
  
  public abstract void removePreviewSizeChangeListener(TuSdkPreviewSizeChangeListener paramTuSdkPreviewSizeChangeListener);
  
  public abstract void removeAllPreviewSizeChangeListener();
  
  public abstract void setTimeEffect(TuSdkMediaTimeEffect paramTuSdkMediaTimeEffect);
  
  public abstract void clearTimeEffect();
  
  public abstract boolean isReversing();
  
  public abstract void destroy();
  
  public static abstract interface TuSdkPreviewSizeChangeListener
  {
    public abstract void onPreviewSizeChanged(TuSdkSize paramTuSdkSize);
  }
  
  public static abstract interface TuSdkProgressListener
  {
    public abstract void onStateChanged(int paramInt);
    
    public abstract void onProgress(long paramLong1, long paramLong2, float paramFloat);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\sources\TuSdkEditorPlayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */