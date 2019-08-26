package org.lasque.tusdk.video.editor;

import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.combo.Face2DComboFilterWrap;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;

public class TuSdkMediaStickerEffectData
  extends TuSdkMediaEffectData
{
  private StickerGroup a;
  
  public TuSdkMediaStickerEffectData(StickerGroup paramStickerGroup)
  {
    if (paramStickerGroup == null)
    {
      TLog.e("%s : Invalid sticker data", new Object[] { this });
      return;
    }
    this.a = paramStickerGroup;
    setMediaEffectType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeSticker);
    setVaild(true);
  }
  
  public StickerGroup getStickerGroup()
  {
    return this.a;
  }
  
  public TuSdkMediaEffectData clone()
  {
    TuSdkMediaStickerEffectData localTuSdkMediaStickerEffectData = new TuSdkMediaStickerEffectData(this.a);
    localTuSdkMediaStickerEffectData.mFilterWrap = this.mFilterWrap.clone();
    localTuSdkMediaStickerEffectData.setAtTimeRange(getAtTimeRange());
    localTuSdkMediaStickerEffectData.setVaild(true);
    localTuSdkMediaStickerEffectData.setMediaEffectType(getMediaEffectType());
    localTuSdkMediaStickerEffectData.setIsApplied(false);
    return localTuSdkMediaStickerEffectData;
  }
  
  public synchronized Face2DComboFilterWrap getFilterWrap()
  {
    if (this.mFilterWrap == null)
    {
      FilterOption localFilterOption = FilterLocalPackage.shared().option("Normal");
      this.mFilterWrap = Face2DComboFilterWrap.creat(localFilterOption);
      this.mFilterWrap.processImage();
    }
    return (Face2DComboFilterWrap)this.mFilterWrap;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaStickerEffectData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */