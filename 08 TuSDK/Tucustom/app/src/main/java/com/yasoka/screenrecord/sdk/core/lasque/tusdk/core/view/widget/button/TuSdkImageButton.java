// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.button;

import android.annotation.SuppressLint;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkImageView;
//import org.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
//import org.lasque.tusdk.core.view.TuSdkImageView;

public class TuSdkImageButton extends TuSdkImageView
{
    private TuSdkTouchColorChangeListener a;
    public int index;
    
    public TuSdkImageButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuSdkImageButton(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuSdkImageButton(final Context context) {
        super(context);
    }
    
    @Override
    protected void initView() {
    }
    
    protected void bindColorChangeListener() {
        if (this.a != null) {
            return;
        }
        this.a = TuSdkTouchColorChangeListener.bindTouchDark((View)this);
    }
    
    @SuppressLint({ "ClickableViewAccessibility" })
    protected void removeColorChangeListener() {
        this.a = null;
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
        if (this.a != null && this.isEnabled() != enabled) {
            this.a.enabledChanged((View)this, enabled);
        }
        super.setEnabled(enabled);
    }
    
    public void setSelected(final boolean selected) {
        if (this.a != null && this.isSelected() != selected) {
            this.a.selectedChanged((View)this, selected);
        }
        super.setSelected(selected);
    }
}
