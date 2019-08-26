// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker;

import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ImageView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;
//import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;

public abstract class StickerListGridBase extends TuSdkCellRelativeLayout<StickerData>
{
    public abstract ImageView getPosterView();
    
    public StickerListGridBase(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public StickerListGridBase(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public StickerListGridBase(final Context context) {
        super(context);
    }
    
    @Override
    protected void bindModel() {
        StickerLocalPackage.shared().loadThumb(this.getModel(), this.getPosterView());
    }
    
    @Override
    public void viewNeedRest() {
        StickerLocalPackage.shared().cancelLoadImage(this.getPosterView());
        if (this.getPosterView() != null) {
            this.getPosterView().setImageBitmap((Bitmap)null);
        }
        super.viewNeedRest();
    }
    
    @Override
    public void viewWillDestory() {
        this.viewNeedRest();
        super.viewWillDestory();
    }
}
