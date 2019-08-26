package org.lasque.tusdk.video.editor;

import android.graphics.Bitmap;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.TuSDK2DImageFilterWrap;
import org.lasque.tusdk.core.seles.tusdk.textSticker.Image2DStickerData;
import org.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

public abstract class TuSdkMediaTileEffectDataBase
  extends TuSdkMediaEffectData
{
  private TuSdkImage2DSticker a;
  
  public TuSdkMediaTileEffectDataBase(TuSdkImage2DSticker paramTuSdkImage2DSticker)
  {
    if (paramTuSdkImage2DSticker == null) {
      TLog.e("%s,Invalid sticker data", new Object[] { this });
    }
    this.a = paramTuSdkImage2DSticker;
    setVaild(true);
  }
  
  public TuSdkMediaTileEffectDataBase(Bitmap paramBitmap, float paramFloat1, float paramFloat2, float paramFloat3, TuSdkSize paramTuSdkSize1, TuSdkSize paramTuSdkSize2)
  {
    if ((paramBitmap == null) || (paramBitmap.isRecycled()))
    {
      TLog.e("%s bitmap is null or recycled", new Object[] { this });
      return;
    }
    this.a = initStickerImageEffectData(paramBitmap, paramFloat1, paramFloat2, paramFloat3, paramTuSdkSize1, paramTuSdkSize2);
    setVaild(true);
  }
  
  public TuSdkImage2DSticker getStickerData()
  {
    return this.a;
  }
  
  protected TuSdkImage2DSticker initStickerImageEffectData(Bitmap paramBitmap, float paramFloat1, float paramFloat2, float paramFloat3, TuSdkSize paramTuSdkSize1, TuSdkSize paramTuSdkSize2)
  {
    TuSdkImage2DSticker localTuSdkImage2DSticker = new TuSdkImage2DSticker();
    Image2DStickerData localImage2DStickerData = new Image2DStickerData(paramBitmap, paramTuSdkSize1.width, paramTuSdkSize1.height, 0.0F, paramFloat1, paramFloat2, paramFloat3);
    localTuSdkImage2DSticker.setCurrentSticker(localImage2DStickerData);
    localTuSdkImage2DSticker.setDesignScreenSize(paramTuSdkSize2);
    return localTuSdkImage2DSticker;
  }
  
  public TuSdkMediaEffectData clone()
  {
    TuSdkMediaTextEffectData localTuSdkMediaTextEffectData = new TuSdkMediaTextEffectData(this.a);
    localTuSdkMediaTextEffectData.setAtTimeRange(getAtTimeRange());
    localTuSdkMediaTextEffectData.setVaild(true);
    localTuSdkMediaTextEffectData.setMediaEffectType(getMediaEffectType());
    localTuSdkMediaTextEffectData.setIsApplied(false);
    return localTuSdkMediaTextEffectData;
  }
  
  public synchronized TuSDK2DImageFilterWrap getFilterWrap()
  {
    if (this.mFilterWrap == null)
    {
      this.mFilterWrap = TuSDK2DImageFilterWrap.creat();
      this.mFilterWrap.processImage();
    }
    return (TuSDK2DImageFilterWrap)this.mFilterWrap;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaTileEffectDataBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */