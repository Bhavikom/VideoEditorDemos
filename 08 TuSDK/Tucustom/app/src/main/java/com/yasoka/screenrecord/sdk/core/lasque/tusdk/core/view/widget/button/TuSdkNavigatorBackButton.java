// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.button;

import android.util.AttributeSet;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
//import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;

public class TuSdkNavigatorBackButton extends TuSdkButton implements TuSdkNavigatorBar.NavigatorBarButtonInterface
{
    private TuSdkNavigatorBar.NavigatorBarButtonType a;
    
    public TuSdkNavigatorBackButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuSdkNavigatorBackButton(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuSdkNavigatorBackButton(final Context context) {
        super(context);
    }
    
    @Override
    public TuSdkNavigatorBar.NavigatorBarButtonType getType() {
        return this.a;
    }
    
    @Override
    public void setType(final TuSdkNavigatorBar.NavigatorBarButtonType a) {
        this.a = a;
    }
    
    @Override
    public String getTitle() {
        if (this.getText() == null) {
            return null;
        }
        return this.getText().toString();
    }
    
    @Override
    public void setTitle(String text) {
        if (text == null) {
            text = "";
        }
        this.setText((CharSequence)text);
    }
    
    @Override
    public void showDot(final boolean b) {
    }
    
    @Override
    public void setBadge(final String s) {
    }
}
