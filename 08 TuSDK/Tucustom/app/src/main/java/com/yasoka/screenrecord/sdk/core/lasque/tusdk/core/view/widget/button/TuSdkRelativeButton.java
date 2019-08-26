// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.button;

import android.annotation.SuppressLint;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
//import org.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public class TuSdkRelativeButton extends TuSdkRelativeLayout
{
    protected TuSdkTouchColorChangeListener colorChangeListener;
    public int index;
    public int typeTag;
    public long idTag;
    
    public TuSdkRelativeButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuSdkRelativeButton(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuSdkRelativeButton(final Context context) {
        super(context);
    }
    
    protected void bindColorChangeListener() {
        if (this.colorChangeListener != null) {
            return;
        }
        this.colorChangeListener = TuSdkTouchColorChangeListener.bindTouchDark((View)this);
    }
    
    @SuppressLint({ "ClickableViewAccessibility" })
    protected void removeColorChangeListener() {
        this.colorChangeListener = null;
        this.setOnTouchListener((View.OnTouchListener)null);
    }
    
    public void setOnClickListener(final View.OnClickListener onClickListener) {
        if (onClickListener != null) {
            this.bindColorChangeListener();
        }
        else {
            this.removeColorChangeListener();
        }
        super.setOnClickListener(onClickListener);
    }
    
    public void setEnabled(final boolean enabled) {
        if (this.colorChangeListener != null && this.isEnabled() != enabled) {
            this.colorChangeListener.enabledChanged((View)this, enabled);
        }
        super.setEnabled(enabled);
    }
    
    public void setSelected(final boolean selected) {
        if (this.colorChangeListener != null && this.isSelected() != selected) {
            this.colorChangeListener.selectedChanged((View)this, selected);
        }
        super.setSelected(selected);
    }
}
