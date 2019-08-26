package org.lasque.tusdk.core.seles.sources;

import android.graphics.RectF;
import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public abstract interface TuSdkEditorTranscoder
{
  public abstract void setVideoDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource);
  
  public abstract TuSdkMediaDataSource getVideoDataSource();
  
  public abstract void setTimeRange(TuSdkTimeRange paramTuSdkTimeRange);
  
  public abstract void setCanvasRect(RectF paramRectF);
  
  public abstract void setCropRect(RectF paramRectF);
  
  public abstract float getVideoOutputDuration();
  
  public abstract long getVideoOutputDurationTimeUs();
  
  public abstract float getVideoInputDuration();
  
  public abstract long getVideoInputDurationTimeUS();
  
  public abstract TuSDKVideoInfo getInputVideoInfo();
  
  public abstract TuSDKVideoInfo getOutputVideoInfo();
  
  public abstract void addTransCoderProgressListener(TuSdkTranscoderProgressListener paramTuSdkTranscoderProgressListener);
  
  public abstract void removeTransCoderProgressListener(TuSdkTranscoderProgressListener paramTuSdkTranscoderProgressListener);
  
  public abstract void removeAllTransCoderProgressListener();
  
  public abstract void startTransCoder();
  
  public abstract int getStatus();
  
  public abstract void stopTransCoder();
  
  public abstract void destroy();
  
  public static abstract interface TuSdkTranscoderProgressListener
  {
    public abstract void onProgressChanged(float paramFloat);
    
    public abstract void onLoadComplete(TuSDKVideoInfo paramTuSDKVideoInfo, TuSdkMediaDataSource paramTuSdkMediaDataSource);
    
    public abstract void onError(Exception paramException);
  }
  
  public static class TuSdkTranscoderStatus
  {
    public static final int Init = 0;
    public static final int Loading = 1;
    public static final int Loaded = 2;
    public static final int Error = 3;
    public static final int Stop = 4;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\sources\TuSdkEditorTranscoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */