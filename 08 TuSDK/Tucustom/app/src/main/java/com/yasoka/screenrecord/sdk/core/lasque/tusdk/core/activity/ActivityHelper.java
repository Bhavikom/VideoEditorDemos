// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.activity;

//import org.lasque.tusdk.core.utils.ReflectUtils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkIntent;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ActivityAnimType;

//import org.lasque.tusdk.core.TuSdkIntent;
//import org.lasque.tusdk.core.type.ActivityAnimType;

public class ActivityHelper
{
    public static void dismissActivity(final AppCompatActivity activity) {
        if (activity != null) {
            activity.finish();
        }
    }
    
    public static void dismissActivityWithAnim(final AppCompatActivity activity, final ActivityAnimType activityAnimType) {
        if (activity == null) {
            return;
        }
        dismissActivity(activity);
        if (activityAnimType != null) {
            activity.overridePendingTransition(activityAnimType.getEnterAnim(), activityAnimType.getExitAnim());
        }
        else {
            activity.overridePendingTransition(0, 0);
        }
    }
    
    public static TuSdkIntent buildModalActivityIntent(final AppCompatActivity activity, final Class<?> clazz, final Fragment transmit, final ActivityAnimType activityPresentAnimType, final ActivityAnimType activityDismissAnimType, final boolean wantFullScreen, final boolean activityFilp) {
        if (clazz == null || activity == null) {
            return null;
        }
        final TuSdkIntent tuSdkIntent = new TuSdkIntent((Context)activity, clazz);
        tuSdkIntent.setWantFullScreen(wantFullScreen);
        tuSdkIntent.setActivityFilp(activityFilp);
        if (transmit != null) {
            ActivityObserver.ins.setTransmit(transmit);
            tuSdkIntent.needTransmitFragment();
        }
        tuSdkIntent.setActivityPresentAnimType(activityPresentAnimType);
        tuSdkIntent.setActivityDismissAnimType(activityDismissAnimType);
        return tuSdkIntent;
    }
    
    public static void presentActivity(final AppCompatActivity activity, final TuSdkIntent tuSdkIntent, final ActivityAnimType activityAnimType, final boolean b) {
        if (tuSdkIntent == null || activity == null) {
            return;
        }
        activity.startActivity((Intent)tuSdkIntent);
        if (b) {
            dismissActivityWithAnim(activity, activityAnimType);
        }
        else if (activityAnimType != null) {
            activity.overridePendingTransition(activityAnimType.getEnterAnim(), activityAnimType.getExitAnim());
        }
    }
    
    public static void presentActivity(final AppCompatActivity activity, final Class<?> clazz, final ActivityAnimType activityAnimType, final boolean b) {
        presentActivity(activity, clazz, activityAnimType, false, false, b);
    }
    
    public static void presentActivity(final AppCompatActivity activity, final Class<?> clazz, final ActivityAnimType activityAnimType, final boolean b, final boolean b2, final boolean b3) {
        presentActivity(activity, buildModalActivityIntent(activity, clazz, null, activityAnimType, null, b, b2), activityAnimType, b3);
    }
    
    public static void presentActivity(final AppCompatActivity activity, final Class<?> clazz, final Fragment fragment, final ActivityAnimType activityAnimType, final ActivityAnimType activityAnimType2, final boolean b, final boolean b2, final boolean b3) {
        presentActivity(activity, buildModalActivityIntent(activity, clazz, fragment, activityAnimType, activityAnimType2, b, b2), activityAnimType, b3);
    }
    
    public static void presentModalNavigationActivity(final AppCompatActivity activity, final Class<?> clazz, final Fragment fragment, final ActivityAnimType activityAnimType, final ActivityAnimType activityAnimType2, final boolean b, final boolean b2) {
        presentActivity(activity, clazz, fragment, activityAnimType, activityAnimType2, b, false, b2);
    }
    
    public static void presentModalNavigationActivity(final AppCompatActivity activity, final Class<?> clazz, final Fragment fragment, final ActivityAnimType activityAnimType, final ActivityAnimType activityAnimType2, final boolean b) {
        presentActivity(activity, clazz, fragment, activityAnimType, activityAnimType2, b, false, false);
    }
    
   /* public static void presentModalNavigationActivity(final AppCompatActivity activity, final Class<?> clazz, final Class<?> clazz2, final ActivityAnimType activityAnimType, final ActivityAnimType activityAnimType2, final boolean b) {
        presentModalNavigationActivity(activity, clazz, ReflectUtils.classInstance(clazz2), activityAnimType, activityAnimType2, b);
    }*/
}
