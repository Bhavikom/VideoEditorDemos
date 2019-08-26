package org.lasque.tusdk.video.editor;

import java.util.List;

public abstract interface TuSDKVideoProcessInterface
{
  public abstract boolean addMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData);
  
  public abstract boolean removeMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData);
  
  public abstract void removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType);
  
  public abstract <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType);
  
  public abstract void removeAllMediaEffects();
  
  public abstract void removeAllLiveSticker();
  
  public abstract void updateEffectTimeLine(long paramLong, TuSDKMediaEffectsManager.OnFilterChangeListener paramOnFilterChangeListener);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSDKVideoProcessInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */