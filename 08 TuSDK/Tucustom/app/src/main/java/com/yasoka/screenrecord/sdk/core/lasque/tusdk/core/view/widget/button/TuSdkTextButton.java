// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.button;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

//import org.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
//import org.lasque.tusdk.core.view.TuSdkViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewInterface;


public class TuSdkTextButton extends AppCompatTextView implements TuSdkViewInterface
{
    private TuSdkTouchColorChangeListener a;
    public int index;
    private int b;
    private int c;
    
    public TuSdkTextButton(final Context context) {
        super(context);
        this.b = Integer.MAX_VALUE;
        this.c = Integer.MAX_VALUE;
        this.initView();
    }
    
    public TuSdkTextButton(final Context context, final AttributeSet set) {
        super(context, set);
        this.b = Integer.MAX_VALUE;
        this.c = Integer.MAX_VALUE;
        this.initView();
    }
    
    public TuSdkTextButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.b = Integer.MAX_VALUE;
        this.c = Integer.MAX_VALUE;
        this.initView();
    }
    
    protected void initView() {
        this.a = TuSdkTouchColorChangeListener.bindTouchDark((View)this);
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
        this.changeColor(selected ? this.b : this.c);
    }
    
    public void changeColor(final int textColor) {
        if (this.b == Integer.MAX_VALUE) {
            return;
        }
        this.setTextColor(textColor);
        if (textColor == this.c) {
            this.a(this.getCompoundDrawables());
        }
        else {
            this.a(this.getCompoundDrawables(), textColor);
        }
    }
    
    private void a(final Drawable[] array) {
        for (final Drawable drawable : array) {
            if (drawable != null) {
                drawable.clearColorFilter();
            }
        }
    }
    
    private void a(final Drawable[] array, final int n) {
        for (final Drawable drawable : array) {
            if (drawable != null) {
                drawable.clearColorFilter();
                drawable.setColorFilter(n, PorterDuff.Mode.SRC_IN);
            }
        }
    }
    
    public void setDefaultColor(final int c) {
        this.c = c;
    }
    
    public void setSelectedColor(final int b) {
        this.b = b;
    }
    
    public void loadView() {
        this.c = this.getTextColors().getDefaultColor();
    }
    
    public void viewDidLoad() {
    }
    
    public void viewWillDestory() {
    }
    
    public void viewNeedRest() {
    }
}
