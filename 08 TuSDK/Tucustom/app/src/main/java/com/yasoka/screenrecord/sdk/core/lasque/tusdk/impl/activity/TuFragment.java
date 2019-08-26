// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity;

//import org.lasque.tusdk.core.TuSdk;
import android.content.Context;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
//import org.lasque.tusdk.core.type.ActivityAnimType;
import android.support.v4.app.Fragment;
//import org.lasque.tusdk.core.activity.ActivityObserver;
//import org.lasque.tusdk.core.TuSdkIntent;
//import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
//import org.lasque.tusdk.impl.view.widget.TuNavigatorBar;
//import org.lasque.tusdk.core.view.widget.button.TuSdkNavigatorButton;
//import org.lasque.tusdk.core.secret.SdkValid;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkIntent;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.activity.ActivityObserver;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.activity.TuSdkFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ActivityAnimType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.button.TuSdkNavigatorButton;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.TuNavigatorBar;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.button.TuNavigatorButton;
//import org.lasque.tusdk.impl.view.widget.button.TuNavigatorButton;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.activity.TuSdkFragment;

public abstract class TuFragment extends TuSdkFragment
{
    @Override
    protected void initCreateView() {
        this.setNavigatorBarId(TuSdkContext.getIDResId("lsq_navigatorBar"), TuSdkContext.getIDResId("lsq_backButton"), TuNavigatorButton.getLayoutId());
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        if (!SdkValid.shared.sdkValid()) {
            return null;
        }
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
    }
    
    public TuSdkNavigatorButton setNavLeftButton(final String s) {
        return this.setNavLeftButton(s, TuNavigatorBar.TuNavButtonStyle.button);
    }
    
    public TuSdkNavigatorButton setNavLeftButton(final String s, final int textColor) {
        final TuSdkNavigatorButton setNavLeftButton = this.setNavLeftButton(s, TuNavigatorBar.TuNavButtonStyle.button);
        setNavLeftButton.setTextColor(textColor);
        return setNavLeftButton;
    }
    
    public TuSdkNavigatorButton setNavLeftButton(final int n) {
        return this.setNavLeftButton(this.getResString(n));
    }
    
    public TuSdkNavigatorButton setNavLeftHighLightButton(final String s) {
        return this.setNavLeftButton(s, TuNavigatorBar.TuNavButtonStyle.highlight);
    }
    
    public TuSdkNavigatorButton setNavLeftHighLightButton(final int n) {
        return this.setNavLeftHighLightButton(this.getResString(n));
    }
    
    public TuSdkNavigatorButton setNavRightButton(final String s) {
        return this.setNavRightButton(s, TuNavigatorBar.TuNavButtonStyle.button);
    }
    
    public TuSdkNavigatorButton setNavRightButton(final String s, final int textColor) {
        final TuSdkNavigatorButton setNavRightButton = this.setNavRightButton(s, TuNavigatorBar.TuNavButtonStyle.button);
        setNavRightButton.setTextColor(textColor);
        return setNavRightButton;
    }
    
    public TuSdkNavigatorButton setNavRightButton(final int n) {
        return this.setNavRightButton(this.getResString(n));
    }
    
    public TuSdkNavigatorButton setNavRightHighLightButton(final String s) {
        return this.setNavRightButton(s, TuNavigatorBar.TuNavButtonStyle.highlight);
    }
    
    public TuSdkNavigatorButton setNavRightHighLightButton(final int n) {
        return this.setNavRightHighLightButton(this.getResString(n));
    }
    
    public void presentActivity(final TuSdkIntent tuSdkIntent, final boolean b) {
        this.presentActivity(tuSdkIntent, ActivityObserver.ins.getAnimPresent(), b);
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
        this.presentModalNavigationActivity(fragment, ActivityObserver.ins.getAnimPresent(), ActivityObserver.ins.getAnimDismiss(), b);
    }
    
    public void presentModalNavigationActivity(final Fragment fragment, final ActivityAnimType activityAnimType, final ActivityAnimType activityAnimType2, final boolean b) {
        this.presentModalNavigationActivity(ActivityObserver.ins.getMainActivityClazz(), fragment, activityAnimType, activityAnimType2, b);
    }
    
    public void filpModalNavigationActivity(final Fragment fragment, final boolean b, final boolean b2) {
        this.filpModalNavigationActivity(ActivityObserver.ins.getMainActivityClazz(), fragment, b, b2);
    }
    
    public void filpModalNavigationActivity(final Fragment fragment, final boolean b) {
        this.filpModalNavigationActivity(fragment, false, b);
    }
    
    public void alert(final TuSdkViewHelper.AlertDelegate alertDelegate, final int n, final int n2) {
        this.alert(alertDelegate, this.getResString(n), this.getResString(n2));
    }
    
    public void alert(final TuSdkViewHelper.AlertDelegate alertDelegate, final String s, final String s2) {
        TuSdkViewHelper.alert(alertDelegate, (Context)this.getActivity(), s, s2, TuSdkContext.getString("lsq_nav_cancel"), TuSdkContext.getString("lsq_button_done"));
    }
    
    public void hubStatus(final String s) {
        TuSdk.messageHub().setStatus((Context)this.getActivity(), s);
    }
    
    public void hubStatus(final int n) {
        TuSdk.messageHub().setStatus((Context)this.getActivity(), n);
    }
    
    public void hubSuccess(final String s) {
        TuSdk.messageHub().showSuccess((Context)this.getActivity(), s);
    }
    
    public void hubSuccess(final int n) {
        TuSdk.messageHub().showSuccess((Context)this.getActivity(), n);
    }
    
    public void hubError(final String s) {
        TuSdk.messageHub().showError((Context)this.getActivity(), s);
    }
    
    public void hubError(final int n) {
        TuSdk.messageHub().showError((Context)this.getActivity(), n);
    }
    
    public void hubDismiss() {
        TuSdk.messageHub().dismiss();
    }
    
    public void hubDismissRightNow() {
        TuSdk.messageHub().dismissRightNow();
    }
}
