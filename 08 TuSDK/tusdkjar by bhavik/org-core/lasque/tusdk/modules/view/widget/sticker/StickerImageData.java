package org.lasque.tusdk.modules.view.widget.sticker;

public class StickerImageData
  extends StickerData
{
  public long starTimeUs;
  public long stopTimeUs;
  
  public boolean isContains(float paramFloat)
  {
    return isContains(paramFloat * 100000L);
  }
  
  public boolean isContains(long paramLong)
  {
    return (this.starTimeUs <= paramLong) && (this.stopTimeUs >= paramLong);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\sticker\StickerImageData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */