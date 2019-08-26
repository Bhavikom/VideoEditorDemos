// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

//import org.lasque.tusdk.core.utils.TLog;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

public class TuSDKVideoSpeedControl implements TuSDKMoviePacketDecoder.VideoSpeedControlInterface
{
    private long a;
    private boolean b;
    private long c;
    private long d;
    
    public TuSDKVideoSpeedControl() {
        this.b = true;
    }
    
    @Override
    public void setEnable(final boolean b) {
        this.b = b;
    }
    
    @Override
    public void setFrameRate(final int n) {
        if (n > 0 && n < 50) {
            this.c = 1000000L / n;
        }
        else {
            this.c = 0L;
        }
    }
    
    @Override
    public void reset() {
        this.a = 0L;
        this.c = 0L;
    }
    
    @Override
    public void preRender(final long n) {
        if (!this.b || n <= 0L) {
            return;
        }
        if (this.a <= 0L) {
            this.d = System.nanoTime() / 1000L;
            this.a = n;
        }
        else {
            long n2;
            if (this.c != 0L) {
                n2 = this.c;
            }
            else {
                n2 = Math.abs(n - this.a);
                this.a = n;
            }
            if (n2 <= 0L) {
                this.d = 0L;
                this.a = 0L;
            }
            else if (n2 > 10000000L) {
                TLog.w("Inter-frame pause was " + n2 / 1000000L + "sec, capping at 5 sec", new Object[0]);
                n2 = 5000000L;
            }
            try {
                Thread.sleep(n2 / 1000L);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
