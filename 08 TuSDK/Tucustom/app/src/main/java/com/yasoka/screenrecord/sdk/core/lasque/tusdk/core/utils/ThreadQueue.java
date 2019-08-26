// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;

public class ThreadQueue
{
    private HandlerThread a;
    private Handler b;
    
    public ThreadQueue(final String s) {
        (this.a = new HandlerThread((s == null) ? "ThreadQueue" : s)).start();
        this.b = new Handler(this.a.getLooper());
    }
    
    public void release() {
        if (this.a == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 18) {
            this.a.quit();
        }
        else {
            this.a.quitSafely();
        }
        this.b = null;
        this.a = null;
    }
    
    @Override
    protected void finalize() {
        this.release();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    public void post(final Runnable runnable) {
        if (this.b == null || runnable == null) {
            return;
        }
        this.b.post(runnable);
    }
    
    public void postAtFrontOfQueue(final Runnable runnable) {
        if (this.b == null || runnable == null) {
            return;
        }
        this.b.postAtFrontOfQueue(runnable);
    }
    
    public void postDelayed(final Runnable runnable, final long n) {
        if (this.b == null || runnable == null) {
            return;
        }
        this.b.postDelayed(runnable, n);
    }
}
