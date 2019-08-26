package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.graphics.RectF;
import android.media.MediaFormat;
import java.util.List;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlice;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract interface TuSdkMediaFilesCuter
{
  public abstract void setMediaDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource);
  
  public abstract void setMediaDataSources(List<TuSdkMediaDataSource> paramList);
  
  public abstract void setOutputFilePath(String paramString);
  
  public abstract int setOutputVideoFormat(MediaFormat paramMediaFormat);
  
  public abstract int setOutputAudioFormat(MediaFormat paramMediaFormat);
  
  public abstract void setSurfaceRender(TuSdkSurfaceRender paramTuSdkSurfaceRender);
  
  public abstract void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender);
  
  public abstract boolean run(TuSdkMediaProgress paramTuSdkMediaProgress);
  
  public abstract void stop();
  
  public abstract TuSdkSize preferredOutputSize();
  
  public abstract void setOutputOrientation(ImageOrientation paramImageOrientation);
  
  public abstract void setCanvasRect(RectF paramRectF);
  
  public abstract void setCropRect(RectF paramRectF);
  
  public abstract void setTimeSlice(TuSdkMediaTimeSlice paramTuSdkMediaTimeSlice);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\TuSdkMediaFilesCuter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */