// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker;

import android.graphics.Rect;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.sticker.StickerView;
//import org.lasque.tusdk.impl.components.widget.sticker.StickerView;

public interface
StickerItemViewInterface
{
    void setStickerViewType(final StickerView.StickerType p0);
    
    void setStickerType(final StickerView.StickerType p0);
    
    StickerView.StickerType getStickerType();
    
    void setSelected(final boolean p0);
    
    void setSticker(final StickerData p0);
    
    StickerData getStickerData();
    
    void setStroke(final int p0, final int p1);
    
    void setParentFrame(final Rect p0);
    
    void setDelegate(final StickerItemViewDelegate p0);
    
    StickerResult getResult(final Rect p0);
    
    public interface StickerItemViewDelegate
    {
        void onStickerItemViewClose(final StickerItemViewInterface p0);
        
        void onStickerItemViewSelected(final StickerItemViewInterface p0);
        
        void onStickerItemViewReleased(final StickerItemViewInterface p0);
    }
}
