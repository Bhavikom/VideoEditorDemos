// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge;

import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ImageView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;
//import org.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;
//import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;

public abstract class BrushBarItemCellBase extends TuSdkCellRelativeLayout<BrushData> implements TuSdkListSelectableCellViewInterface
{
    private BrushTableItemCellDelegate a;
    
    public abstract ImageView getImageView();
    
    public BrushBarItemCellBase(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public BrushBarItemCellBase(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public BrushBarItemCellBase(final Context context) {
        super(context);
    }
    
    public BrushTableItemCellDelegate getDelegate() {
        return this.a;
    }
    
    @Override
    protected void bindModel() {
        final BrushData brushData = this.getModel();
        if (brushData == null) {
            return;
        }
        if (brushData.code.equals("Eraser")) {
            this.handleTypeEraser(brushData);
        }
        else if (brushData.getType() == BrushData.BrushType.TypeOnline) {
            this.handleTypeOnline(brushData);
        }
        else {
            this.handleTypeBrush(brushData);
        }
    }
    
    public boolean canHiddenRemoveFlag() {
        return true;
    }
    
    protected void handleTypeEraser(final BrushData brushData) {
    }
    
    protected void handleTypeOnline(final BrushData brushData) {
    }
    
    protected void handleTypeBrush(final BrushData brushData) {
        if (brushData == null || this.getImageView() == null) {
            return;
        }
        this.getImageView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        BrushLocalPackage.shared().loadThumbWithBrush(this.getImageView(), brushData);
    }
    
    protected void handleBlockView(final int n, final int imageResource) {
        if (this.getImageView() != null && imageResource != 0) {
            this.getImageView().setImageResource(imageResource);
            this.getImageView().setScaleType(ImageView.ScaleType.CENTER);
        }
    }
    
    @Override
    public void viewNeedRest() {
        super.viewNeedRest();
        this.setSelected(false);
        if (this.getImageView() != null) {
            this.getImageView().setImageBitmap((Bitmap)null);
            BrushLocalPackage.shared().cancelLoadImage(this.getImageView());
        }
    }
    
    @Override
    public void onCellSelected(final int n) {
        this.setSelected(true);
    }
    
    @Override
    public void onCellDeselected() {
        this.setSelected(false);
    }
    
    public interface BrushTableItemCellDelegate
    {
        void onBrushCellRemove(final BrushBarItemCellBase p0);
    }
}
