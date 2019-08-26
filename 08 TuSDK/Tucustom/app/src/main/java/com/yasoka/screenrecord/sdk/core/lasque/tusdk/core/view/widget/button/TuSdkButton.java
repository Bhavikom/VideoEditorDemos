// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewInterface;

//import org.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
//import org.lasque.tusdk.core.view.TuSdkViewInterface;

public class TuSdkButton extends AppCompatButton implements TuSdkViewInterface
{
    private TuSdkTouchColorChangeListener a;
    public int index;
    
    public TuSdkButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.initView();
    }
    
    public TuSdkButton(final Context context, final AttributeSet set) {
        super(context, set);
        this.initView();
    }
    
    public TuSdkButton(final Context context) {
        super(context);
        this.initView();
    }
    
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
        this.setOnTouchListener((OnTouchListener)null);
    }
    
    public void setOnClickListener(final OnClickListener onClickListener) {
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
    
    public void loadView() {
    }
    
    public void viewDidLoad() {
    }
    
    public void viewWillDestory() {
    }
    
    public void viewNeedRest() {
    }
}
