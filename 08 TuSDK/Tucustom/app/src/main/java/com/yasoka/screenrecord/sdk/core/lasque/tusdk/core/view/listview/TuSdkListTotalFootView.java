// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview;

import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.util.AttributeSet;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkListTotalFootView extends TuSdkRelativeLayout
{
    private int a;
    private String b;
    private int c;
    
    public TuSdkListTotalFootView(final Context context) {
        super(context);
    }
    
    public TuSdkListTotalFootView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuSdkListTotalFootView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public String getTitleFormater() {
        return this.b;
    }
    
    public void setTitleFormater(final String b) {
        this.b = b;
    }
    
    public int getTotal() {
        return this.a;
    }
    
    public void setTotal(final int a) {
        this.a = a;
    }
    
    public abstract RelativeLayout getWrapView();
    
    @Override
    public void loadView() {
        super.loadView();
        this.c = this.getWrapView().getLayoutParams().height;
    }
    
    public void needShowFooter(final boolean b) {
        final ViewGroup.LayoutParams layoutParams = this.getWrapView().getLayoutParams();
        layoutParams.height = (b ? this.c : 0);
        this.getWrapView().setLayoutParams(layoutParams);
    }
}
