// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core;

import android.content.Context;
import android.app.Application;

public class TuSdkApplication extends Application
{
    private boolean a;
    
    public boolean isEnableLog() {
        return this.a;
    }
    
    public void setEnableLog(final boolean a) {
        TuSdk.enableDebugLog(this.a = a);
    }
    
    public void onCreate() {
        TuSdkCrashException.bindExceptionHandler(this.getApplicationContext());
        super.onCreate();
    }
    
    protected void initPreLoader(final Context context, final String s) {
        this.initPreLoader(context, s, null);
    }
    
    protected void initPreLoader(final Context context, final String s, final String s2) {
        TuSdk.init(context, s, s2);
    }
}
