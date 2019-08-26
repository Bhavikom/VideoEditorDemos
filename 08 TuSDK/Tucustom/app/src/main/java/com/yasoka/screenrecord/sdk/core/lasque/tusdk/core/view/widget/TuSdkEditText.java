// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget;

//import org.lasque.tusdk.core.utils.anim.AnimHelper;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.activity.ActivityObserver;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AnimHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewInterface;

//import org.lasque.tusdk.core.activity.ActivityObserver;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.view.TuSdkViewInterface;

public class TuSdkEditText extends AppCompatEditText implements View.OnFocusChangeListener, View.OnKeyListener, TextView.OnEditorActionListener, TuSdkViewInterface
{
    private TuSdkEditTextListener a;
    private OnFocusChangeListener b;
    
    public TuSdkEditText(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.initView();
    }
    
    public TuSdkEditText(final Context context, final AttributeSet set) {
        super(context, set);
        this.initView();
    }
    
    public TuSdkEditText(final Context context) {
        super(context);
        this.initView();
    }
    
    protected void initView() {
        super.setOnFocusChangeListener((OnFocusChangeListener)this);
        this.setOnEditorActionListener((OnEditorActionListener)this);
        this.setOnKeyListener((OnKeyListener)this);
    }
    
    public void setOnFocusChangeListener(final OnFocusChangeListener b) {
        this.b = b;
    }
    
    public void loadView() {
    }
    
    public void viewDidLoad() {
    }
    
    public void viewWillDestory() {
    }
    
    public void viewNeedRest() {
    }
    
    public String getInputText() {
        if (this.getText() == null) {
            return null;
        }
        return StringHelper.trimToNull(this.getText().toString());
    }
    
    public String getTextOrEmpty() {
        if (this.getText() == null) {
            return "";
        }
        return StringHelper.trimToEmpty(this.getText().toString());
    }
    
    public void onFocusChange(final View view, final boolean b) {
        if (this.getContext() == null) {
            return;
        }
        if (b) {
            ActivityObserver.ins.editTextFocus(this);
        }
        else {
            ActivityObserver.ins.editTextFocusLost(this);
        }
        if (this.b != null) {
            this.b.onFocusChange(view, b);
        }
    }
    
    public boolean onEditorAction(final TextView textView, final int n, final KeyEvent keyEvent) {
        if (keyEvent != null && keyEvent.getAction() == 0) {
            return false;
        }
        switch (n) {
            case 2:
            case 3:
            case 4:
            case 6: {
                return this.a();
            }
            default: {
                return false;
            }
        }
    }
    
    public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
        if (n != 66 || keyEvent.getAction() == 0) {}
        return false;
    }
    
    private boolean a() {
        final TuSdkEditTextListener submitListener = this.getSubmitListener();
        return submitListener != null && submitListener.onTuSdkEditTextSubmit(this);
    }
    
    public TuSdkEditTextListener getSubmitListener() {
        return this.a;
    }
    
    public void setSubmitListener(final TuSdkEditTextListener a) {
        this.a = a;
    }
    
    public void setShakeAnimation() {
        this.setAnimation(AnimHelper.shakeAnimation(3));
    }
    
    public interface TuSdkEditTextListener
    {
        boolean onTuSdkEditTextSubmit(final TuSdkEditText p0);
    }
}
