package org.lasque.tusdk.core.media.codec.suit;

import android.media.MediaFormat;
import java.util.List;
import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public abstract interface TuSdkMediaFileTranscoder
{
  public abstract void addInputDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource);
  
  public abstract void addInputDataSouces(List<TuSdkMediaDataSource> paramList);
  
  public abstract void setOutputFilePath(String paramString);
  
  public abstract int setOutputVideoFormat(MediaFormat paramMediaFormat);
  
  public abstract int setOutputAudioFormat(MediaFormat paramMediaFormat);
  
  public abstract void setSurfaceRender(TuSdkSurfaceRender paramTuSdkSurfaceRender);
  
  public abstract void setAudioRender(TuSdkAudioRender paramTuSdkAudioRender);
  
  public abstract boolean run(TuSdkMediaProgress paramTuSdkMediaProgress);
  
  public abstract void stop();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\TuSdkMediaFileTranscoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */