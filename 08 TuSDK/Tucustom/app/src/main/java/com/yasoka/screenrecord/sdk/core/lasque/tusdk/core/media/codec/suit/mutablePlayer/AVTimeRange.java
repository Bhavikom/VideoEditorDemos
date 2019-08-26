// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

public class AVTimeRange
{
    private long a;
    private long b;
    
    public long durationUs() {
        return this.a;
    }
    
    public long startUs() {
        return this.b;
    }
    
    public long endUs() {
        return this.b + this.a;
    }
    
    @Override
    public String toString() {
        return "[ startUs : " + this.startUs() + "  endUs : " + this.endUs() + " ]";
    }
    
    public boolean containsTimeUs(final long n) {
        return n >= this.startUs() && n <= this.endUs();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof AVTimeRange)) {
            return false;
        }
        final AVTimeRange avTimeRange = (AVTimeRange)o;
        return avTimeRange.b == this.b && avTimeRange.a == this.a;
    }
    
    public static AVTimeRange AVTimeRangeMake(final long b, final long a) {
        final AVTimeRange avTimeRange = new AVTimeRange();
        avTimeRange.b = b;
        avTimeRange.a = a;
        return avTimeRange;
    }
    
    public static boolean AVTimeRangeEqual(final AVTimeRange avTimeRange, final AVTimeRange avTimeRange2) {
        if (avTimeRange == null && avTimeRange2 == null) {
            return true;
        }
        if (avTimeRange != null) {
            return avTimeRange.equals(avTimeRange2);
        }
        return avTimeRange2.equals(avTimeRange);
    }
}
