// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.activity;

//import org.lasque.tusdk.core.utils.ContextUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.IBinder;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ActivityAnimType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.TuAnimType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuFragmentActivity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

//import org.lasque.tusdk.impl.TuAnimType;
//import org.lasque.tusdk.impl.activity.TuFragmentActivity;
//import org.lasque.tusdk.core.type.ActivityAnimType;

public class ActivityObserver
{
    public static final ActivityObserver ins;
    private ArrayList<AppCompatActivity> a;
    private Fragment b;
    private EditText c;
    private Class<?> d;
    @SuppressLint({ "ClickableViewAccessibility" })
    private View.OnTouchListener e;
    private Hashtable<String, ActivityAnimType> f;
    private ActivityAnimType g;
    private ActivityAnimType h;
    private ActivityAnimType i;
    private ActivityAnimType j;
    
    public Class<?> getMainActivityClazz() {
        if (this.d == null) {
            this.d = TuFragmentActivity.class;
        }
        return this.d;
    }
    
    public void setMainActivityClazz(final Class<?> d) {
        this.d = d;
    }
    
    private ActivityObserver() {
        this.e = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent != null && motionEvent.getAction() == 0) {
                    ActivityObserver.this.cancelEditTextFocus(view);
                }
                return false;
            }
        };
        this.f = new Hashtable<String, ActivityAnimType>();
        this.a = new ArrayList<AppCompatActivity>();
        for (final TuAnimType value : TuAnimType.values()) {
            this.f.put(value.name(), value);
        }
    }
    
    public void register(final AppCompatActivity activity) {
        if (activity == null || this.a.contains(activity)) {
            return;
        }
        this.a.add(activity);
    }
    
    public void remove(final AppCompatActivity o) {
        if (o == null) {
            return;
        }
        this.a.remove(o);
    }
    
    public AppCompatActivity getTopActivity() {
        if (this.a == null || this.a.isEmpty()) {
            return null;
        }
        return this.a.get(this.a.size() - 1);
    }
    
    public void exitApp() {
        final Iterator<AppCompatActivity> iterator = this.a.iterator();
        while (iterator.hasNext()) {
            iterator.next().finish();
        }
        this.a.clear();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
    
    public void setTransmit(final Fragment b) {
        this.b = b;
    }
    
    public Fragment getTransmit() {
        final Fragment b = this.b;
        this.b = null;
        return b;
    }
    
    public View.OnTouchListener getHiddenKeyboardListener() {
        return this.e;
    }
    
    public void bindAutoHiddenKeyboard(final View view) {
        if (view == null) {
            return;
        }
        view.setOnTouchListener(this.e);
    }
    
    public void editTextFocus(final EditText c) {
        this.showSoftInput(c.getContext(), c);
        this.c = c;
    }
    
    public void editTextFocusLost(final EditText obj) {
        if (this.c != null && obj != null && this.c.equals(obj)) {
            this.c = null;
        }
    }
    
    public boolean cancelEditTextFocus(final View view) {
        if (view != null) {
            this.cancelEditTextFocusBinder(view.getContext(), view.getWindowToken());
        }
        if (this.c == null) {
            return false;
        }
        this.cancelEditTextFocusBinder(this.c.getContext(), this.c.getWindowToken());
        this.c.clearFocus();
        this.c = null;
        return true;
    }
    
    public boolean cancelEditTextFocus() {
        return this.cancelEditTextFocus(null);
    }
    
    public boolean cancelEditTextFocusBinder(final Context context, final IBinder binder) {
        if (binder == null) {
            return false;
        }
        final InputMethodManager inputMethodManager = ContextUtils.getSystemService(context, "input_method");
        if (inputMethodManager == null) {
            return false;
        }
        inputMethodManager.hideSoftInputFromWindow(binder, 2);
        return true;
    }
    
    public void showSoftInput(final Context context, final EditText editText) {
        if (editText == null) {
            return;
        }
        final InputMethodManager inputMethodManager = ContextUtils.getSystemService(context, "input_method");
        if (inputMethodManager == null) {
            return;
        }
        inputMethodManager.showSoftInput((View)editText, 0);
    }
    
    public Hashtable<String, ActivityAnimType> getActivityAnims() {
        return this.f;
    }
    
    public ActivityAnimType getAnimPresent() {
        if (this.g == null) {
            this.g = TuAnimType.up;
        }
        return this.g;
    }
    
    public void setAnimPresent(final ActivityAnimType g) {
        this.g = g;
    }
    
    public ActivityAnimType getAnimDismiss() {
        if (this.h == null) {
            this.h = TuAnimType.down;
        }
        return this.h;
    }
    
    public void setAnimDismiss(final ActivityAnimType h) {
        this.h = h;
    }
    
    public ActivityAnimType getAnimPush() {
        if (this.i == null) {
            this.i = TuAnimType.push;
        }
        return this.i;
    }
    
    public void setAnimPush(final ActivityAnimType i) {
        this.i = i;
    }
    
    public ActivityAnimType getAnimPop() {
        if (this.j == null) {
            this.j = TuAnimType.pop;
        }
        return this.j;
    }
    
    public void setAnimPop(final ActivityAnimType j) {
        this.j = j;
    }
    
    protected ActivityAnimType getAnimType(final String key) {
        if (key == null) {
            return null;
        }
        return this.f.get(key);
    }
    
    static {
        ins = new ActivityObserver();
    }
}
