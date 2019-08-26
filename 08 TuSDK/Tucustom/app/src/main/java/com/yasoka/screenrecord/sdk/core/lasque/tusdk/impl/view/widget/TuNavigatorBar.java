// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget;

import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.TuSdkContext;
import android.widget.TextView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
//import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;

public class TuNavigatorBar extends TuSdkNavigatorBar
{
    private TextView a;
    
    public static int getLayoutId() {
        return TuSdkContext.getLayoutResId("tusdk_view_widget_navigator");
    }
    
    public TuNavigatorBar(final Context context) {
        super(context);
    }
    
    public TuNavigatorBar(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuNavigatorBar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    @Override
    public void loadView() {
        super.loadView();
        this.a = this.getViewById("lsq_titleView");
    }
    
    @Override
    public void setTitle(final String s) {
        this.setTextViewText(this.a, s);
    }
    
    @Override
    public void setTitle(final int text) {
        if (text == 0) {
            return;
        }
        this.a.setText(text);
    }
    
    @Override
    public String getTitle() {
        return this.getTextViewText(this.a);
    }
    
    public enum TuNavButtonStyle implements TuSdkNavButtonStyleInterface
    {
        button("lsq_color_transparent"), 
        highlight("lsq_color_transparent");
        
        private int a;
        
        private TuNavButtonStyle(final String s) {
            this.a = TuSdkContext.getColorResId(s);
        }
        
        @Override
        public int getBackgroundId() {
            return this.a;
        }
    }
}
