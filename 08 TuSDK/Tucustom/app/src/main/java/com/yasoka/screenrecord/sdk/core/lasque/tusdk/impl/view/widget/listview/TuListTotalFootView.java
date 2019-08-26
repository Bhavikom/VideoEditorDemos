// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.listview;

import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.TuSdkContext;
import android.widget.TextView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkListTotalFootView;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;
//import org.lasque.tusdk.core.view.listview.TuSdkListTotalFootView;

public class TuListTotalFootView extends TuSdkListTotalFootView
{
    private TuSdkRelativeLayout a;
    private TextView b;
    
    public static int getLayoutId() {
        return TuSdkContext.getLayoutResId("tusdk_view_widget_list_view_total_footer_view");
    }
    
    public TuListTotalFootView(final Context context) {
        super(context);
    }
    
    public TuListTotalFootView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuListTotalFootView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    @Override
    public TuSdkRelativeLayout getWrapView() {
        if (this.a == null) {
            this.a = this.getViewById("lsq_wrapView");
        }
        return this.a;
    }
    
    public void setmWrapView(final TuSdkRelativeLayout a) {
        this.a = a;
    }
    
    public TextView getTitleView() {
        if (this.b == null) {
            this.b = this.getViewById("lsq_titleView");
        }
        return this.b;
    }
    
    public void setTitleView(final TextView b) {
        this.b = b;
    }
    
    @Override
    public void setTotal(final int n) {
        super.setTotal(n);
        this.setTitle(n);
    }
    
    public void setTitle(final int i) {
        if (this.getTitleFormater() == null || this.getTitleView() == null) {
            return;
        }
        this.getTitleView().setText((CharSequence)String.format(this.getTitleFormater(), i));
    }
}
