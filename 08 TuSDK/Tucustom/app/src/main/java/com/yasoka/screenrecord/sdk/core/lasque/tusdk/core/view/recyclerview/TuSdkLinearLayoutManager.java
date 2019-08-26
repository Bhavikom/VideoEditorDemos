// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.recyclerview;

import android.view.View;
//import org.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;

public class TuSdkLinearLayoutManager extends LinearLayoutManager
{
    public TuSdkLinearLayoutManager(final Context context) {
        super(context);
    }
    
    public TuSdkLinearLayoutManager(final Context context, final int n, final boolean b) {
        super(context, n, b);
        this.initManager();
    }
    
    protected void initManager() {
    }
    
    public void selectedPosition(final int n, final boolean b) {
        final View viewByPosition = this.findViewByPosition(n);
        if (viewByPosition == null || !(viewByPosition instanceof TuSdkListSelectableCellViewInterface)) {
            return;
        }
        if (b) {
            ((TuSdkListSelectableCellViewInterface)viewByPosition).onCellSelected(n);
        }
        else {
            ((TuSdkListSelectableCellViewInterface)viewByPosition).onCellDeselected();
        }
    }
}
