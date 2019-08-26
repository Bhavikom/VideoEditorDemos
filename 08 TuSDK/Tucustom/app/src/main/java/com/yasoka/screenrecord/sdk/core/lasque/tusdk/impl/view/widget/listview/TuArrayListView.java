// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.listview;

import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.view.listview.TuSdkArrayListView;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkArrayListView;

public abstract class TuArrayListView<T, V extends View> extends TuSdkArrayListView<T, V>
{
    public TuArrayListView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuArrayListView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuArrayListView(final Context context) {
        super(context);
    }
    
    @Override
    protected void initView() {
        this.setTotalFooterViewId(TuListTotalFootView.getLayoutId());
        this.setRefreshLayoutResId(TuRefreshListHeaderView.getLayoutId(), TuRefreshListFooterView.getLayoutId());
        super.initView();
    }
}
