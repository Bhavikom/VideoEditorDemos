// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.AttributeSet;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkRefreshListFooterView extends TuSdkRelativeLayout
{
    public TuSdkRefreshListFooterView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuSdkRefreshListFooterView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuSdkRefreshListFooterView(final Context context) {
        super(context);
    }
    
    public abstract TextView getTitleLabel();
    
    public abstract RelativeLayout getFootWrap();
    
    @Override
    public void loadView() {
        super.loadView();
        this.setViewShowed(false);
    }
    
    public void setTitle(final String text) {
        this.getTitleLabel().setText((CharSequence)text);
    }
    
    public void setViewShowed(final boolean b) {
        this.showView((View)this.getFootWrap(), b);
    }
}
