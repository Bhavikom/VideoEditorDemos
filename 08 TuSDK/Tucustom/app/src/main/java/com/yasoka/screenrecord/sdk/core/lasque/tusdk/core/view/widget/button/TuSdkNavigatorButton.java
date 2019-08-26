// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.button;

import android.util.AttributeSet;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
//import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;

public abstract class TuSdkNavigatorButton extends TuSdkRelativeButton implements TuSdkNavigatorBar.NavigatorBarButtonInterface
{
    private TuSdkNavigatorBar.NavigatorBarButtonType a;
    
    public TuSdkNavigatorButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuSdkNavigatorButton(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuSdkNavigatorButton(final Context context) {
        super(context);
    }
    
    @Override
    public void loadView() {
        super.loadView();
    }
    
    @Override
    public TuSdkNavigatorBar.NavigatorBarButtonType getType() {
        return this.a;
    }
    
    @Override
    public void setType(final TuSdkNavigatorBar.NavigatorBarButtonType a) {
        this.a = a;
    }
}
