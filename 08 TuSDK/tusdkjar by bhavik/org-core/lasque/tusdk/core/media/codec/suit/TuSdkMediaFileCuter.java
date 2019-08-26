package org.lasque.tusdk.core.media.codec.suit;

import android.graphics.RectF;
import android.media.MediaFormat;
import java.util.List;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract interface TuSdkMediaFileCuter
{
  public abstract void setMediaDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource);
  
  public abstract void setOutputFilePath(String paramString);
  
  public abstract int setOutputVideoFormat(MediaFormat paramMediaFormat);
  
  public abstract int setOutputAudioFormat(MediaFormat paramMediaFormat);
  
  public abstract void setSurfaceRender(TuSdkSurfaceRender paramTuSdkSurfaceRender);
  
  public abstract void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender);
  
  public abstract void setAudioMixerRender(TuSdkAudioRender paramTuSdkAudioRender);
  
  public abstract boolean run(TuSdkMediaProgress paramTuSdkMediaProgress);
  
  public abstract void stop();
  
  public abstract void setOutputOrientation(ImageOrientation paramImageOrientation);
  
  public abstract void setCanvasRect(RectF paramRectF);
  
  public abstract void setCropRect(RectF paramRectF);
  
  public abstract void setEnableClip(boolean paramBoolean);
  
  public abstract void setOutputRatio(float paramFloat);
  
  public abstract void setOutputSize(TuSdkSize paramTuSdkSize);
  
  public abstract void setCanvasColor(int paramInt);
  
  public abstract void setCanvasColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  public abstract void setTimeline(TuSdkMediaTimeline paramTuSdkMediaTimeline);
  
  public abstract void setTimeSlices(List<TuSdkMediaTimeSlice> paramList);
  
  public abstract void setTimeSlice(TuSdkMediaTimeSlice paramTuSdkMediaTimeSlice);
  
  public abstract void setTimeSlice(long paramLong1, long paramLong2);
  
  public abstract void setTimeSliceDuration(long paramLong1, long paramLong2);
  
  public abstract void setTimeSliceScaling(float paramFloat1, float paramFloat2);
  
  public abstract void setTimeSliceDurationScaling(float paramFloat1, float paramFloat2);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\TuSdkMediaFileCuter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */