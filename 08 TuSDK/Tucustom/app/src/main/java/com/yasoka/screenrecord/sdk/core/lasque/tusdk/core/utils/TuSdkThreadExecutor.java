// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class TuSdkThreadExecutor
{
    private ExecutorService a;
    
    public TuSdkThreadExecutor() {
        this(Runtime.getRuntime().availableProcessors());
    }
    
    public TuSdkThreadExecutor(final int nThreads) {
        this.a = Executors.newFixedThreadPool(nThreads);
    }
    
    public void exec(final Runnable runnable) {
        if (runnable == null) {
            return;
        }
        this.a.execute(runnable);
    }
    
    public void release() {
        if (this.a != null) {
            this.a.shutdownNow();
        }
        this.a = null;
    }
}
