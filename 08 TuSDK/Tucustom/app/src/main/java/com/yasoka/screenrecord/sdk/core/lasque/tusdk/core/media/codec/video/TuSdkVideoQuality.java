// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video;

//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
//import org.lasque.tusdk.core.struct.TuSdkSize;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;

public enum TuSdkVideoQuality
{
    LIVE_LOW1(12, 150000, 16), 
    LIVE_LOW2(15, 264000, 12), 
    LIVE_LOW3(15, 350000, 8), 
    LIVE_MEDIUM1(20, 512000, 16), 
    LIVE_MEDIUM2(20, 800000, 12), 
    LIVE_MEDIUM3(24, 1000000, 8), 
    LIVE_HIGH1(30, 1200000, 16), 
    LIVE_HIGH2(30, 1500000, 12), 
    LIVE_HIGH3(30, 2000000, 8), 
    RECORD_LOW1(12, 1200000, 24), 
    RECORD_LOW2(15, 2400000, 16), 
    RECORD_LOW3(24, 3600000, 12), 
    RECORD_MEDIUM1(24, 5120000, 10), 
    RECORD_MEDIUM2(30, 8000000, 8), 
    RECORD_MEDIUM3(30, 10000000, 6), 
    RECORD_HIGH1(30, 12000000, 4), 
    RECORD_HIGH2(30, 15000000, 2), 
    RECORD_HIGH3(30, 18000000, 1);
    
    private int a;
    private int b;
    private int c;
    
    private TuSdkVideoQuality(final int a, final int b, final int a2) {
        this.a = a;
        this.b = b;
        this.c = Math.max(a2, 1);
    }
    
    public int getFrameRates() {
        return this.a;
    }
    
    public int getBitrate() {
        return this.b;
    }
    
    public int getRefer() {
        return this.c;
    }
    
    public int dynamicBitrate(final TuSdkSize tuSdkSize) {
        if (tuSdkSize == null) {
            return this.b;
        }
        return this.dynamicBitrate(tuSdkSize.width, tuSdkSize.height);
    }
    
    public int dynamicBitrate(final int n, final int n2) {
        return dynamicBitrate(n, n2, this.c);
    }
    
    public TuSdkVideoQuality upgrade() {
        final TuSdkVideoQuality[] values = values();
        final int ordinal = this.ordinal();
        if (ordinal + 1 > TuSdkVideoQuality.LIVE_HIGH3.ordinal()) {
            return TuSdkVideoQuality.LIVE_HIGH3;
        }
        if (ordinal + 1 > values.length - 1) {
            return values[values.length - 1];
        }
        return values[ordinal + 1];
    }
    
    public TuSdkVideoQuality degrade() {
        final TuSdkVideoQuality[] values = values();
        final int ordinal = this.ordinal();
        if (ordinal - 1 < 0) {
            return values[0];
        }
        return values[ordinal - 1];
    }
    
    public static TuSdkVideoQuality safeQuality() {
        final int performance = TuSdkGPU.getGpuType().getPerformance();
        if (performance < 3) {
            return TuSdkVideoQuality.RECORD_LOW2;
        }
        if (performance == 3) {
            return TuSdkVideoQuality.RECORD_MEDIUM1;
        }
        if (performance == 4) {
            return TuSdkVideoQuality.RECORD_MEDIUM3;
        }
        return TuSdkVideoQuality.RECORD_HIGH1;
    }
    
    public static int dynamicBitrate(final int n, final int n2, final int n3) {
        if (n < 2 || n2 < 2 || n3 < 1) {
            return 0;
        }
        return n * n2 * 3 * 4 / n3;
    }
}
