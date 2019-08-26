package org.lasque.tusdk.core.seles.sources;

import java.util.List;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
import org.lasque.tusdk.video.editor.TuSdkMediaEffectData.TuSdkMediaEffectDataType;

public abstract interface TuSdkEditorEffector
{
  public abstract void setInputImageOrientation(ImageOrientation paramImageOrientation);
  
  public abstract void setOutputImageOrientation(ImageOrientation paramImageOrientation);
  
  public abstract void setFilterChangeListener(TuSdkEffectorFilterChangeListener paramTuSdkEffectorFilterChangeListener);
  
  public abstract boolean addMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData);
  
  public abstract <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType);
  
  public abstract List<TuSdkMediaEffectData> getAllMediaEffectData();
  
  public abstract void removeMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData);
  
  public abstract void removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType);
  
  public abstract void removeAllMediaEffect();
  
  public abstract void destroy();
  
  public static abstract interface TuSdkEffectorFilterChangeListener
  {
    public abstract void onFilterChanged(FilterWrap paramFilterWrap);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\seles\sources\TuSdkEditorEffector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */