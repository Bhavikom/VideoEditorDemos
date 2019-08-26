// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import java.util.concurrent.TimeUnit;
import android.os.SystemClock;

class AVMediaSyncClock
{
    private long a;
    private float b;
    private long c;
    private boolean d;
    
    AVMediaSyncClock() {
        this.b = 1.0f;
    }
    
    public void start() {
        if (this.d) {
            return;
        }
        this.a();
        this.d = true;
    }
    
    public void stop() {
        this.a = 0L;
        this.d = false;
        this.c = 0L;
    }
    
    private void a() {
        this.a = 0L;
        this.c = SystemClock.elapsedRealtime();
    }
    
    public void lock(final long a, final long n) {
        if (!this.d) {
            return;
        }
        if (this.a == 0L) {
            this.a = a;
        }
        final long timeout = this.c + (usToMs((long)((a - this.a) * (1.0f / this.b))) + n) - SystemClock.elapsedRealtime();
        if (timeout > 0L) {
            try {
                TimeUnit.MILLISECONDS.sleep(timeout);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void setSpeed(final float b) {
        this.a();
        this.b = b;
    }
    
    public float getSpeed() {
        return this.b;
    }
    
    public static long usToMs(final long n) {
        return (n == -9223372036854775807L || n == Long.MIN_VALUE) ? n : (n / 1000L);
    }
    
    public static long msToUs(final long n) {
        return (n == -9223372036854775807L || n == Long.MIN_VALUE) ? n : (n * 1000L);
    }
}
