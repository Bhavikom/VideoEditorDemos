package org.lasque.tusdk.video.editor;

import android.graphics.RectF;
import java.io.File;
import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class TuSDKMovieEditorOptions
{
  @Deprecated
  public String moviePath;
  public TuSDKMediaDataSource videoDataSource;
  public File movieOutputFilePath = null;
  public TuSdkTimeRange cutTimeRange;
  public boolean includeAudioInVideo = true;
  public boolean clearAudioDecodeCacheInfoOnDestory = false;
  public boolean autoPlay = false;
  public boolean loopingPlay = false;
  public int minCutDuration = -1;
  public int maxCutDuration = -1;
  public Boolean saveToAlbum = Boolean.valueOf(true);
  public String saveToAlbumName;
  public RectF outputRegion = null;
  public TuSdkSize outputSize = null;
  
  public static TuSDKMovieEditorOptions defaultOptions()
  {
    return new TuSDKMovieEditorOptions();
  }
  
  public TuSDKMovieEditorOptions setMoviePath(String paramString)
  {
    this.moviePath = paramString;
    return this;
  }
  
  public TuSDKMovieEditorOptions setVideoDataSource(TuSDKMediaDataSource paramTuSDKMediaDataSource)
  {
    this.videoDataSource = paramTuSDKMediaDataSource;
    return this;
  }
  
  public TuSDKMovieEditorOptions setMovieOutputFilePath(File paramFile)
  {
    this.movieOutputFilePath = paramFile;
    return this;
  }
  
  public TuSDKMovieEditorOptions setCutTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
  {
    this.cutTimeRange = paramTuSdkTimeRange;
    return this;
  }
  
  public TuSDKMovieEditorOptions setIncludeAudioInVideo(boolean paramBoolean)
  {
    this.includeAudioInVideo = paramBoolean;
    return this;
  }
  
  public TuSDKMovieEditorOptions setAutoPlay(boolean paramBoolean)
  {
    this.autoPlay = paramBoolean;
    return this;
  }
  
  public TuSDKMovieEditorOptions setLoopingPlay(boolean paramBoolean)
  {
    this.loopingPlay = paramBoolean;
    return this;
  }
  
  public TuSDKMovieEditorOptions setMaxCutDuration(int paramInt)
  {
    this.maxCutDuration = paramInt;
    return this;
  }
  
  public TuSDKMovieEditorOptions setMinCutDuration(int paramInt)
  {
    this.minCutDuration = paramInt;
    return this;
  }
  
  public TuSDKMovieEditorOptions setOutputRegion(RectF paramRectF)
  {
    this.outputRegion = paramRectF;
    return this;
  }
  
  public TuSDKMovieEditorOptions setOutputSize(TuSdkSize paramTuSdkSize)
  {
    this.outputSize = paramTuSdkSize;
    return this;
  }
  
  public TuSDKMovieEditorOptions setSaveToAlbum(Boolean paramBoolean)
  {
    this.saveToAlbum = paramBoolean;
    return this;
  }
  
  public TuSDKMovieEditorOptions setSaveToAlbumName(String paramString)
  {
    this.saveToAlbumName = paramString;
    return this;
  }
  
  public TuSDKMovieEditorOptions setClearAudioDecodeCacheInfoOnDestory(boolean paramBoolean)
  {
    this.clearAudioDecodeCacheInfoOnDestory = paramBoolean;
    return this;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSDKMovieEditorOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */