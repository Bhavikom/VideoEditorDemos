// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview;

import android.util.AttributeSet;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkCellRelativeLayout<T> extends TuSdkRelativeLayout implements TuSdkCellViewInterface<T>
{
    private T a;
    
    public TuSdkCellRelativeLayout(final Context context) {
        super(context);
    }
    
    public TuSdkCellRelativeLayout(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuSdkCellRelativeLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    @Override
    public void setModel(final T a) {
        this.a = a;
        if (this.isLayouted()) {
            this.willBindModel();
        }
    }
    
    @Override
    public T getModel() {
        return this.a;
    }
    
    @Override
    protected void onLayouted() {
        super.onLayouted();
        this.willBindModel();
    }
    
    protected void willBindModel() {
        if (this.a == null) {
            return;
        }
        this.bindModel();
    }
    
    protected abstract void bindModel();
}
