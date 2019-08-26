// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.paintdraw;

import android.util.AttributeSet;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;
//import org.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;
//import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;

public abstract class PaintDrawBarItemCellBase extends TuSdkCellRelativeLayout<PaintData> implements TuSdkListSelectableCellViewInterface
{
    public PaintDrawBarItemCellBase(final Context context) {
        super(context);
    }
    
    public PaintDrawBarItemCellBase(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public PaintDrawBarItemCellBase(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    @Override
    public void onCellSelected(final int n) {
        this.setSelected(true);
    }
    
    @Override
    public void onCellDeselected() {
        this.setSelected(false);
    }
}
