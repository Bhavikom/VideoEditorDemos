package org.lasque.tusdk.video.editor;

import java.util.List;
import org.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate;
import org.lasque.tusdk.core.mergefilter.TuSDKComboFilterWrapChain;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.sticker.LiveStickerPlayController;

public abstract interface TuSDKMediaEffectsManager
{
  public abstract void setMediaEffectDelegate(TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate paramTuSDKVideoProcessorMediaEffectDelegate);
  
  public abstract TuSDKComboFilterWrapChain getFilterWrapChain();
  
  public abstract boolean addMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData);
  
  public abstract void addTerminalNode(SelesContext.SelesInput paramSelesInput);
  
  public abstract <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType);
  
  public abstract List<TuSdkMediaEffectData> getAllMediaEffectData();
  
  public abstract boolean removeMediaEffectData(TuSdkMediaEffectData paramTuSdkMediaEffectData);
  
  public abstract void removeMediaEffectsWithType(TuSdkMediaEffectData.TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType);
  
  public abstract void removeAllMediaEffects();
  
  public abstract void updateEffectTimeLine(long paramLong, OnFilterChangeListener paramOnFilterChangeListener);
  
  public abstract void showGroupSticker(TuSdkMediaStickerEffectData paramTuSdkMediaStickerEffectData);
  
  public abstract LiveStickerPlayController getLiveStickerPlayController();
  
  public abstract void removeAllLiveSticker();
  
  public abstract void release();
  
  public static abstract interface OnFilterChangeListener
  {
    public abstract void onFilterChanged(FilterWrap paramFilterWrap);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSDKMediaEffectsManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */