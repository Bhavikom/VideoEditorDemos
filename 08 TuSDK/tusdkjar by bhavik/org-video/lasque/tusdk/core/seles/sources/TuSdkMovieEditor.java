package org.lasque.tusdk.core.seles.sources;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import java.io.File;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.WaterMarkPosition;
import org.lasque.tusdk.video.editor.TuSdkTimeRange;

public abstract interface TuSdkMovieEditor
{
  public abstract Context getContext();
  
  public abstract void setVideoPath(String paramString);
  
  public abstract void setDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource);
  
  public abstract void setEnableTranscode(boolean paramBoolean);
  
  public abstract void loadVideo();
  
  public abstract void saveVideo();
  
  public abstract TuSdkEditorTranscoder getEditorTransCoder();
  
  public abstract TuSdkEditorPlayer getEditorPlayer();
  
  public abstract TuSdkEditorEffector getEditorEffector();
  
  public abstract TuSdkEditorAudioMixer getEditorMixer();
  
  public abstract TuSdkEditorSaver getEditorSaver();
  
  public abstract void onDestroy();
  
  public static class TuSdkMovieEditorOptions
  {
    public TuSdkMediaDataSource videoDataSource;
    public File movieOutputFilePath = null;
    public TuSdkTimeRange cutTimeRange;
    public RectF canvasRect;
    public RectF cropRect;
    public boolean includeAudioInVideo = true;
    public boolean enableFirstFramePause = true;
    public TuSdkMediaPictureEffectReferTimelineType timelineType = TuSdkMediaPictureEffectReferTimelineType.TuSdkMediaEffectReferInputTimelineType;
    public boolean clearAudioDecodeCacheInfoOnDestory = false;
    public Boolean saveToAlbum = Boolean.valueOf(true);
    public String saveToAlbumName;
    public TuSdkSize outputSize = null;
    public Bitmap waterImage;
    public float waterImageScale = 0.09F;
    public boolean isRecycleWaterImage;
    public TuSdkWaterMarkOption.WaterMarkPosition watermarkPosition = TuSdkWaterMarkOption.WaterMarkPosition.TopRight;
    
    public static TuSdkMovieEditorOptions defaultOptions()
    {
      return new TuSdkMovieEditorOptions();
    }
    
    public TuSdkMovieEditorOptions setVideoDataSource(TuSdkMediaDataSource paramTuSdkMediaDataSource)
    {
      this.videoDataSource = paramTuSdkMediaDataSource;
      return this;
    }
    
    public TuSdkMovieEditorOptions setMovieOutputFilePath(File paramFile)
    {
      this.movieOutputFilePath = paramFile;
      return this;
    }
    
    public TuSdkMovieEditorOptions setCutTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
    {
      this.cutTimeRange = paramTuSdkTimeRange;
      return this;
    }
    
    public TuSdkMovieEditorOptions setCanvasRectF(RectF paramRectF)
    {
      this.canvasRect = paramRectF;
      return this;
    }
    
    public TuSdkMovieEditorOptions setIncludeAudioInVideo(boolean paramBoolean)
    {
      this.includeAudioInVideo = paramBoolean;
      return this;
    }
    
    public TuSdkMovieEditorOptions setPictureEffectReferTimelineType(TuSdkMediaPictureEffectReferTimelineType paramTuSdkMediaPictureEffectReferTimelineType)
    {
      this.timelineType = paramTuSdkMediaPictureEffectReferTimelineType;
      return this;
    }
    
    public TuSdkMovieEditorOptions setOutputSize(TuSdkSize paramTuSdkSize)
    {
      this.outputSize = paramTuSdkSize;
      return this;
    }
    
    public TuSdkMovieEditorOptions setSaveToAlbum(Boolean paramBoolean)
    {
      this.saveToAlbum = paramBoolean;
      return this;
    }
    
    public TuSdkMovieEditorOptions setSaveToAlbumName(String paramString)
    {
      this.saveToAlbumName = paramString;
      return this;
    }
    
    public TuSdkMovieEditorOptions setClearAudioDecodeCacheInfoOnDestory(boolean paramBoolean)
    {
      this.clearAudioDecodeCacheInfoOnDestory = paramBoolean;
      return this;
    }
    
    public TuSdkMovieEditorOptions setWaterImage(Bitmap paramBitmap, TuSdkWaterMarkOption.WaterMarkPosition paramWaterMarkPosition, boolean paramBoolean)
    {
      this.isRecycleWaterImage = paramBoolean;
      this.watermarkPosition = paramWaterMarkPosition;
      this.waterImage = paramBitmap;
      return this;
    }
    
    public static enum TuSdkMediaPictureEffectReferTimelineType
    {
      private int a;
      
      private TuSdkMediaPictureEffectReferTimelineType(int paramInt)
      {
        this.a = paramInt;
      }
      
      public int getType()
      {
        return this.a;
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\sources\TuSdkMovieEditor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */