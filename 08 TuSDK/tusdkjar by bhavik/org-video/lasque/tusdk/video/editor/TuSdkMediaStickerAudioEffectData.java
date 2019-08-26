package org.lasque.tusdk.video.editor;

import android.net.Uri;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.combo.Face2DComboFilterWrap;
import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;

public class TuSdkMediaStickerAudioEffectData
  extends TuSdkMediaEffectData
{
  private TuSdkMediaAudioEffectData a;
  private TuSdkMediaStickerEffectData b;
  
  public TuSdkMediaStickerAudioEffectData(TuSdkMediaAudioEffectData paramTuSdkMediaAudioEffectData, TuSdkMediaStickerEffectData paramTuSdkMediaStickerEffectData)
  {
    if ((paramTuSdkMediaAudioEffectData == null) || (paramTuSdkMediaStickerEffectData == null))
    {
      TLog.e("%s : Invalid MV data", new Object[] { this });
      return;
    }
    if ((!paramTuSdkMediaAudioEffectData.isVaild()) || (!paramTuSdkMediaStickerEffectData.isVaild()))
    {
      TLog.e("%s : Invalid MV data", new Object[] { this });
      return;
    }
    this.a = paramTuSdkMediaAudioEffectData;
    this.b = paramTuSdkMediaStickerEffectData;
    setVaild(true);
    setMediaEffectType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio);
  }
  
  public TuSdkMediaStickerAudioEffectData(String paramString, StickerGroup paramStickerGroup)
  {
    this(new TuSdkMediaDataSource(paramString), paramStickerGroup);
  }
  
  public TuSdkMediaStickerAudioEffectData(Uri paramUri, StickerGroup paramStickerGroup)
  {
    this(new TuSdkMediaDataSource(TuSdk.appContext().getContext(), paramUri), paramStickerGroup);
  }
  
  public TuSdkMediaStickerAudioEffectData(TuSdkMediaDataSource paramTuSdkMediaDataSource, StickerGroup paramStickerGroup)
  {
    if ((paramTuSdkMediaDataSource == null) || (!paramTuSdkMediaDataSource.isValid()) || (paramStickerGroup == null))
    {
      setVaild(false);
      TLog.w("%s : Invalid MV data", new Object[] { this });
      return;
    }
    this.a = new TuSdkMediaAudioEffectData(paramTuSdkMediaDataSource);
    this.b = new TuSdkMediaStickerEffectData(paramStickerGroup);
    setVaild(true);
    setMediaEffectType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeStickerAudio);
  }
  
  public void setAtTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
  {
    if (!isVaild()) {
      return;
    }
    super.setAtTimeRange(paramTuSdkTimeRange);
    this.a.setAtTimeRange(paramTuSdkTimeRange);
    this.b.setAtTimeRange(paramTuSdkTimeRange);
  }
  
  public TuSdkMediaAudioEffectData getMediaAudioEffectData()
  {
    return this.a;
  }
  
  public TuSdkMediaStickerEffectData getMediaStickerEffectData()
  {
    return this.b;
  }
  
  public final void setVolume(float paramFloat)
  {
    if (!isVaild()) {
      return;
    }
    this.a.setVolume(paramFloat);
  }
  
  public TuSdkMediaEffectData clone()
  {
    TuSdkMediaStickerAudioEffectData localTuSdkMediaStickerAudioEffectData = new TuSdkMediaStickerAudioEffectData(this.a, this.b);
    localTuSdkMediaStickerAudioEffectData.setAtTimeRange(getAtTimeRange());
    localTuSdkMediaStickerAudioEffectData.setVaild(true);
    localTuSdkMediaStickerAudioEffectData.setMediaEffectType(getMediaEffectType());
    localTuSdkMediaStickerAudioEffectData.setIsApplied(false);
    return localTuSdkMediaStickerAudioEffectData;
  }
  
  public synchronized FilterWrap getFilterWrap()
  {
    if (this.mFilterWrap == null)
    {
      FilterOption localFilterOption = FilterLocalPackage.shared().option("Normal");
      this.mFilterWrap = Face2DComboFilterWrap.creat(localFilterOption);
      this.mFilterWrap.processImage();
    }
    return this.mFilterWrap;
  }
  
  public void setIsApplied(boolean paramBoolean)
  {
    super.setIsApplied(paramBoolean);
    if (this.a != null) {
      this.a.setIsApplied(paramBoolean);
    }
    if (this.b != null) {
      this.b.setIsApplied(paramBoolean);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaStickerAudioEffectData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */