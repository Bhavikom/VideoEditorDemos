// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import android.os.Looper;
import android.os.Handler;

public class ThreadHelper
{
    public static final Handler handler;
    
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
    
    public static Thread runThread(final Runnable target) {
        final Thread thread = new Thread(target);
        thread.start();
        return thread;
    }
    
    public static void post(final Runnable runnable) {
        if (runnable == null) {
            return;
        }
        ThreadHelper.handler.post(runnable);
    }
    
    public static void postDelayed(final Runnable runnable, final long n) {
        if (runnable == null) {
            return;
        }
        ThreadHelper.handler.postDelayed(runnable, n);
    }
    
    public static void cancel(final Runnable runnable) {
        if (runnable == null) {
            return;
        }
        ThreadHelper.handler.removeCallbacks(runnable);
    }
    
    public static void sleep(final long n) {
        try {
            Thread.sleep(n);
        }
        catch (InterruptedException ex) {}
    }
    
    public static boolean interrupted() {
        return Thread.interrupted();
    }
    
    public static boolean isInterrupted() {
        return Thread.currentThread().isInterrupted();
    }
    
    static {
        handler = new Handler(Looper.getMainLooper());
    }
}
