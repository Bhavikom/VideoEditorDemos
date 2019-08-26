package org.lasque.tusdk.modules.view.widget.sticker;

import android.graphics.Rect;
import org.lasque.tusdk.impl.components.widget.sticker.StickerView.StickerType;

public abstract interface StickerItemViewInterface
{
  public abstract void setStickerViewType(StickerView.StickerType paramStickerType);
  
  public abstract void setStickerType(StickerView.StickerType paramStickerType);
  
  public abstract StickerView.StickerType getStickerType();
  
  public abstract void setSelected(boolean paramBoolean);
  
  public abstract void setSticker(StickerData paramStickerData);
  
  public abstract StickerData getStickerData();
  
  public abstract void setStroke(int paramInt1, int paramInt2);
  
  public abstract void setParentFrame(Rect paramRect);
  
  public abstract void setDelegate(StickerItemViewDelegate paramStickerItemViewDelegate);
  
  public abstract StickerResult getResult(Rect paramRect);
  
  public static abstract interface StickerItemViewDelegate
  {
    public abstract void onStickerItemViewClose(StickerItemViewInterface paramStickerItemViewInterface);
    
    public abstract void onStickerItemViewSelected(StickerItemViewInterface paramStickerItemViewInterface);
    
    public abstract void onStickerItemViewReleased(StickerItemViewInterface paramStickerItemViewInterface);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\sticker\StickerItemViewInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */