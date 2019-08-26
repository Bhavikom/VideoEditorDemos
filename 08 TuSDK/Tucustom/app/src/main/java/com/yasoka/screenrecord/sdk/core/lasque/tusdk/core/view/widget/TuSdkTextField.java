// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget;

import android.text.Editable;
//import org.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;

public class TuSdkTextField extends TuSdkEditText implements TextWatcher
{
    private Drawable a;
    private TuSdkTextFieldListener b;
    private boolean c;
    
    public TuSdkTextField(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuSdkTextField(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuSdkTextField(final Context context) {
        super(context);
    }
    
    public void setClearListener(final TuSdkTextFieldListener b) {
        this.b = b;
    }
    
    @Override
    protected void initView() {
        super.initView();
        this.a();
    }
    
    private void a() {
        this.a = this.getCompoundDrawables()[2];
        if (this.a == null) {
            return;
        }
        this.setClearIconVisible(this.getText().length() > 0);
        this.addTextChangedListener((TextWatcher)this);
    }
    
    protected void setClearIconVisible(final boolean b) {
        if (this.a == null) {
            return;
        }
        final Drawable drawable = b ? this.a : null;
        final Drawable[] compoundDrawables = this.getCompoundDrawables();
        this.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3]);
    }
    
    @SuppressLint({ "ClickableViewAccessibility" })
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        this.a(motionEvent);
        return super.onTouchEvent(motionEvent);
    }
    
    private void a(final MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0: {
                this.b(motionEvent);
                break;
            }
            case 1:
            case 3:
            case 4: {
                this.c(motionEvent);
                break;
            }
        }
    }
    
    private void b(final MotionEvent motionEvent) {
        if (!this.d(motionEvent)) {
            return;
        }
        TuSdkTouchColorChangeListener.setDark(this.a);
        this.c = true;
    }
    
    private void c(final MotionEvent motionEvent) {
        if (!this.c) {
            return;
        }
        this.c = false;
        TuSdkTouchColorChangeListener.clearColorType(this.a);
        if (!this.d(motionEvent)) {
            return;
        }
        this.setText((CharSequence)"");
        if (this.b != null) {
            this.b.onTextFieldClickClear(this);
        }
    }
    
    private boolean d(final MotionEvent motionEvent) {
        if (this.a == null || this.getCompoundDrawables()[2] == null) {
            return false;
        }
        final float x = motionEvent.getX();
        final int n = this.getWidth() - this.getTotalPaddingRight();
        final int n2 = this.getWidth() - this.getPaddingRight();
        return x >= n && x <= n2;
    }
    
    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
    }
    
    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        this.setClearIconVisible(charSequence.length() > 0);
    }
    
    public void afterTextChanged(final Editable editable) {
    }
    
    public interface TuSdkTextFieldListener
    {
        void onTextFieldClickClear(final TuSdkTextField p0);
    }
}
