// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components;

//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.type.ActivityAnimType;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkIntent;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.activity.ActivityHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.activity.ActivityObserver;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ActivityAnimType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuFragment;

//import org.lasque.tusdk.core.activity.ActivityHelper;
//import org.lasque.tusdk.core.activity.ActivityObserver;
//import org.lasque.tusdk.core.TuSdkIntent;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
//import org.lasque.tusdk.core.utils.FileHelper;
//import org.lasque.tusdk.impl.activity.TuFragment;
//import org.lasque.tusdk.core.TuSdkResult;

public abstract class TuSdkComponent implements TuSdkComponentErrorListener
{
    private AppCompatActivity a;
    private TuSdkComponentDelegate b;
    private boolean c;
    
    public AppCompatActivity activity() {
        return this.a;
    }
    
    public TuSdkComponentDelegate getDelegate() {
        return this.b;
    }
    
    public void setDelegate(final TuSdkComponentDelegate b) {
        this.b = b;
    }
    
    public boolean isAutoDismissWhenCompleted() {
        return this.c;
    }
    
    public TuSdkComponent setAutoDismissWhenCompleted(final boolean c) {
        this.c = c;
        return this;
    }
    
    public TuSdkComponent(final AppCompatActivity a) {
        this.a = a;
        this.initComponent();
    }
    
    protected abstract void initComponent();
    
    public abstract TuSdkComponent showComponent();
    
    protected void notifyResult(final TuSdkResult tuSdkResult, final Error error, final TuFragment tuFragment) {
        if (this.isAutoDismissWhenCompleted() && tuFragment != null) {
            tuFragment.dismissActivityWithAnim();
        }
        if (this.getDelegate() == null) {
            return;
        }
        this.getDelegate().onComponentFinished(tuSdkResult, error, tuFragment);
    }
    
    @Override
    public void onComponentError(final TuFragment tuFragment, final TuSdkResult tuSdkResult, final Error error) {
        this.notifyResult(tuSdkResult, error, tuFragment);
    }
    
    public boolean showAlertIfCannotSaveFile() {
        if (this.a == null) {
            return true;
        }
        String s = null;
        ComponentErrorType componentErrorType = null;
        if (!FileHelper.mountedExternalStorage()) {
            s = this.getResString("lsq_save_not_found_sdcard");
            componentErrorType = ComponentErrorType.TypeNotFoundSDCard;
        }
        else if (!FileHelper.hasAvailableExternal((Context)this.a)) {
            s = this.getResString("lsq_save_insufficient_storage_space");
            componentErrorType = ComponentErrorType.TypeStorageSpace;
        }
        if (componentErrorType == null) {
            return false;
        }
        TuSdkViewHelper.alert((Context)this.a, this.getResString("lsq_save_unsupport_storage_title"), s, this.getResString("lsq_button_done"));
        this.onComponentError(null, null, componentErrorType.getError(this));
        return true;
    }
    
    public void presentActivity(final TuSdkIntent tuSdkIntent, final boolean b) {
        ActivityHelper.presentActivity(this.a, tuSdkIntent, ActivityObserver.ins.getAnimPresent(), b);
    }
    
    public void presentModalNavigationActivity(final Fragment fragment) {
        this.presentModalNavigationActivity(fragment, false);
    }
    
    public void presentModalNavigationActivity(final Fragment fragment, final boolean b) {
        this.presentModalNavigationActivity(fragment, ActivityObserver.ins.getAnimPresent(), ActivityObserver.ins.getAnimDismiss(), b);
    }
    
    public void pushModalNavigationActivity(final Fragment fragment) {
        this.pushModalNavigationActivity(fragment, false);
    }
    
    public void pushModalNavigationActivity(final Fragment fragment, final boolean b) {
        this.presentModalNavigationActivity(fragment, ActivityObserver.ins.getAnimPush(), ActivityObserver.ins.getAnimPop(), b);
    }
    
    public void presentModalNavigationActivity(final Fragment fragment, final ActivityAnimType activityAnimType, final ActivityAnimType activityAnimType2, final boolean b) {
        ActivityHelper.presentActivity(this.a, ActivityObserver.ins.getMainActivityClazz(), fragment, activityAnimType, activityAnimType2, b, false, false);
    }
    
    public void alert(final TuSdkViewHelper.AlertDelegate alertDelegate, final int n, final int n2) {
        this.alert(alertDelegate, this.getResString(n), this.getResString(n2));
    }
    
    public void alert(final TuSdkViewHelper.AlertDelegate alertDelegate, final String s, final String s2) {
        TuSdkViewHelper.alert(alertDelegate, (Context)this.a, s, s2, this.getResString("lsq_nav_cancel"), this.getResString("lsq_button_done"));
    }
    
    public String getResString(final int n) {
        return ContextUtils.getResString((Context)this.a, n);
    }
    
    public String getResString(final String s) {
        return TuSdkContext.getString(s);
    }
    
    public interface TuSdkComponentDelegate
    {
        void onComponentFinished(final TuSdkResult p0, final Error p1, final TuFragment p2);
    }
}
