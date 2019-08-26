// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Semaphore;

public class TuSdkSemaphore extends Semaphore
{
    private final long a = 0L;
    
    public TuSdkSemaphore(final int permits) {
        super(permits);
    }
    
    public void signal() {
        this.release();
    }
    
    public boolean waitSignal(final long timeout) {
        boolean tryAcquire = false;
        try {
            tryAcquire = this.tryAcquire(timeout, TimeUnit.MILLISECONDS);
        }
        catch (Exception ex) {}
        return tryAcquire;
    }
    
    public boolean waitSignal(final int permits, final long timeout) {
        boolean tryAcquire = false;
        try {
            tryAcquire = this.tryAcquire(permits, timeout, TimeUnit.MILLISECONDS);
        }
        catch (Exception ex) {}
        return tryAcquire;
    }
    
    public void log(final String s) {
        TLog.d("%s %s: available: %d, queueLength: %d", s, "TuSdkSemaphore", this.availablePermits(), this.getQueueLength());
    }
}
