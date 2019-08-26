package org.lasque.tusdk.core.seles.sources;

import android.graphics.Bitmap;
import java.nio.IntBuffer;
import java.util.List;
import org.lasque.tusdk.api.engine.TuSdkFilterEngine;
import org.lasque.tusdk.api.engine.TuSdkFilterEngine.TuSdkFilterEngineListener;
import org.lasque.tusdk.api.engine.TuSdkFilterEngineImpl;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.video.editor.TuSdkMediaAudioEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData.TuSdkMediaEffectDataType;
import org.lasque.tusdk.video.editor.TuSdkMediaFilterEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaStickerAudioEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaStickerEffectData;

public class TuSdkEditorEffectorImpl
  implements TuSdkEditorEffector
{
  private TuSdkFilterEngine a = new TuSdkFilterEngineImpl(false, false);
  private TuSdkEditorEffector.TuSdkEffectorFilterChangeListener b;
  private TuSdkEditorAudioMixer c;
  private SelesVerticeCoordinateCorpBuilder d = new SelesVerticeCoordinateCropBuilderImpl(false);
  private TuSdkFilterEngine.TuSdkFilterEngineListener e = new TuSdkFilterEngine.TuSdkFilterEngineListener()
  {
    public void onPictureDataCompleted(IntBuffer paramAnonymousIntBuffer, TuSdkSize paramAnonymousTuSdkSize) {}
    
    public void onPreviewScreenShot(Bitmap paramAnonymousBitmap) {}
    
    public void onFilterChanged(FilterWrap paramAnonymousFilterWrap)
    {
      if (TuSdkEditorEffectorImpl.a(TuSdkEditorEffectorImpl.this) != null) {
        TuSdkEditorEffectorImpl.a(TuSdkEditorEffectorImpl.this).onFilterChanged(paramAnonymousFilterWrap);
      }
    }
  };
  
  public TuSdkEditorEffectorImpl()
  {
    this.a.setCordinateBuilder(this.d);
    this.a.setListener(this.e);
  }
  
  protected void setAudioMixer(TuSdkEditorAudioMixer paramTuSdkEditorAudioMixer)
  {
    this.c = paramTuSdkEditorAudioMixer;
  }
  
  public void setInputImageOrientation(ImageOrientation paramImageOrientation)
  {
    if (this.a == null) {
      return;
    }
    this.a.setInputImageOrientation(paramImageOrientation);
  }
  
  public void setOutputImageOrientation(ImageOrientation paramImageOrientation)
  {
    if (this.a == null) {
      return;
    }
    this.a.setOutputImageOrientation(paramImageOrientation);
  }
  
  public void setFilterChangeListener(TuSdkEditorEffector.TuSdkEffectorFilterChangeListener paramTuSdkEffectorFilterChangeListener)
  {
    this.b = paramTuSdkEffectorFilterChangeListener;
  }
  
  protected void onSurfaceCreated()
  {
    this.a.onSurfaceCreated();
  }
  
  protected void onSurfaceChanged(int paramInt1, int paramInt2)
  {
    this.a.onSurfaceChanged(paramInt1, paramInt2);
  }
  
  protected int processFrame(int paramInt1, int paramInt2, int paramInt3, long paramLong)
  {
    return this.a.processFrame(paramInt1, paramInt2, paramInt3, paramLong);
  }
  
  public boolean addMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    boolean bool = false;
    if ((paramTuSdkMediaEffectData instanceof TuSdkMediaStickerAudioEffectData))
    {
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio);
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker);
      this.c.clearAllAudioData();
      TuSdkMediaAudioEffectData localTuSdkMediaAudioEffectData = ((TuSdkMediaStickerAudioEffectData)paramTuSdkMediaEffectData).getMediaAudioEffectData();
      if ((localTuSdkMediaAudioEffectData != null) && (this.c != null))
      {
        this.c.addAudioRenderEntry(localTuSdkMediaAudioEffectData.getAudioEntry());
        this.c.loadAudio();
      }
      bool = this.a.addMediaEffectData(paramTuSdkMediaEffectData);
    }
    else if ((paramTuSdkMediaEffectData instanceof TuSdkMediaStickerEffectData))
    {
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker);
      bool = this.a.addMediaEffectData(paramTuSdkMediaEffectData);
    }
    else if (((paramTuSdkMediaEffectData instanceof TuSdkMediaAudioEffectData)) && (this.c != null))
    {
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker);
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio);
      this.c.clearAllAudioData();
      this.c.addAudioRenderEntry(((TuSdkMediaAudioEffectData)paramTuSdkMediaEffectData).getAudioEntry());
      this.c.loadAudio();
      bool = this.a.addMediaEffectData(paramTuSdkMediaEffectData);
    }
    else if ((paramTuSdkMediaEffectData instanceof TuSdkMediaFilterEffectData))
    {
      removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeFilter);
      bool = this.a.addMediaEffectData(paramTuSdkMediaEffectData);
    }
    else
    {
      bool = this.a.addMediaEffectData(paramTuSdkMediaEffectData);
    }
    return bool;
  }
  
  public <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    return this.a.mediaEffectsWithType(paramTuSdkMediaEffectDataType);
  }
  
  public List<TuSdkMediaEffectData> getAllMediaEffectData()
  {
    return this.a.getAllMediaEffectData();
  }
  
  public void removeMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData)
  {
    if (((paramTuSdkMediaEffectData instanceof TuSdkMediaStickerAudioEffectData)) || ((paramTuSdkMediaEffectData instanceof TuSdkMediaAudioEffectData)))
    {
      if (this.c == null) {
        return;
      }
      this.c.clearAllAudioData();
    }
    this.a.removeMediaEffectData(paramTuSdkMediaEffectData);
  }
  
  public void removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    if ((paramTuSdkMediaEffectDataType == TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio) || (paramTuSdkMediaEffectDataType == TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeAudio))
    {
      if (this.c == null) {
        return;
      }
      this.c.clearAllAudioData();
    }
    this.a.removeMediaEffectsWithType(paramTuSdkMediaEffectDataType);
  }
  
  public void removeAllMediaEffect()
  {
    this.a.removeAllMediaEffects();
  }
  
  public void destroy()
  {
    this.a.release();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\sources\TuSdkEditorEffectorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */