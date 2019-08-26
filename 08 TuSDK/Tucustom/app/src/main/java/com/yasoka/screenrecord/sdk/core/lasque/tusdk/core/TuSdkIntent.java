// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core;

//import org.lasque.tusdk.core.type.ActivityAnimType;
import android.content.Context;
import android.net.Uri;
import android.content.Intent;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ActivityAnimType;

public class TuSdkIntent extends Intent
{
    public static final String WANT_FULL_SCREEN_KEY = "wantFullScreen";
    public static final String FRAGMENT_CLASS = "fragmentClass";
    public static final String FRAGMENT_TRANSMITS = "fragmentTransmit";
    public static final String ACTIVITY_PRESENT_ANIMTYPE = "activityPresentAnimType";
    public static final String ACTIVITY_DISMISS_ANIMTYPE = "activityDismissAnimType";
    public static final String ACTIVITY_FILP_ACTION = "activityFilpAction";
    
    public TuSdkIntent() {
    }
    
    public TuSdkIntent(final Intent intent) {
        super(intent);
    }
    
    public TuSdkIntent(final String s) {
        super(s);
    }
    
    public TuSdkIntent(final String s, final Uri uri) {
        super(s, uri);
    }
    
    public TuSdkIntent(final Context context, final Class<?> clazz) {
        super(context, (Class)clazz);
    }
    
    public TuSdkIntent(final String s, final Uri uri, final Context context, final Class<?> clazz) {
        super(s, uri, context, (Class)clazz);
    }
    
    public void setWantFullScreen(final boolean b) {
        this.putExtra("wantFullScreen", b);
    }
    
    public boolean getWantFullScreen() {
        return this.getBooleanExtra("wantFullScreen", false);
    }
    
    public void setFragmentClass(final Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        this.putExtra("fragmentClass", clazz.getName());
    }
    
    public void needTransmitFragment() {
        this.putExtra("fragmentTransmit", true);
    }
    
    public void setActivityPresentAnimType(final ActivityAnimType activityAnimType) {
        if (activityAnimType == null) {
            return;
        }
        this.putExtra("activityPresentAnimType", activityAnimType.name());
    }
    
    public void setActivityDismissAnimType(final ActivityAnimType activityAnimType) {
        if (activityAnimType == null) {
            return;
        }
        this.putExtra("activityDismissAnimType", activityAnimType.name());
    }
    
    public void setActivityFilp(final boolean b) {
        this.putExtra("activityFilpAction", b);
    }
}
