// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.listview;

import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.TuSdkContext;
import android.widget.TextView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;
//import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;

public class TuDefaultLineListCellView extends TuSdkCellRelativeLayout<String>
{
    private TextView a;
    
    public static int getLayoutId() {
        return TuSdkContext.getLayoutResId("tusdk_view_widget_list_view_default_line_cell_view");
    }
    
    public TuDefaultLineListCellView(final Context context) {
        super(context);
    }
    
    public TuDefaultLineListCellView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuDefaultLineListCellView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TextView getTitleView() {
        if (this.a == null) {
            this.a = this.getViewById("lsq_titleView");
        }
        return this.a;
    }
    
    public void setTitleView(final TextView a) {
        this.a = a;
    }
    
    @Override
    protected void bindModel() {
        if (this.getTitleView() == null || this.getModel() == null) {
            return;
        }
        this.getTitleView().setText((CharSequence)(getModel()));
    }
}
