// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor;

public class TuSdkTimeRange
{
    public static final long BASE_TIME_US = 1000000L;
    private long a;
    private long b;
    
    public TuSdkTimeRange() {
        this.a = 0L;
        this.b = 0L;
    }
    
    public static TuSdkTimeRange makeRange(final float n, final float n2) {
        final TuSdkTimeRange tuSdkTimeRange = new TuSdkTimeRange();
        tuSdkTimeRange.setStartTimeUs((long)(n * 1000000.0f));
        tuSdkTimeRange.setEndTimeUs((long)(n2 * 1000000.0f));
        return tuSdkTimeRange;
    }
    
    public static TuSdkTimeRange makeTimeUsRange(final long startTimeUs, final long endTimeUs) {
        final TuSdkTimeRange tuSdkTimeRange = new TuSdkTimeRange();
        tuSdkTimeRange.setStartTimeUs(startTimeUs);
        tuSdkTimeRange.setEndTimeUs(endTimeUs);
        return tuSdkTimeRange;
    }
    
    public boolean isValid() {
        return this.a >= 0L && this.b > this.a;
    }
    
    public float duration() {
        if (!this.isValid()) {
            return 0.0f;
        }
        return (this.b - this.a) / 1000000.0f;
    }
    
    public long durationTimeUS() {
        if (!this.isValid()) {
            return 0L;
        }
        return this.b - this.a;
    }
    
    public long getStartTimeUS() {
        return this.a;
    }
    
    public void setStartTimeUs(final long a) {
        this.a = a;
    }
    
    public long getEndTimeUS() {
        return this.b;
    }
    
    public void setEndTimeUs(final long b) {
        this.b = b;
    }
    
    public float getStartTime() {
        return this.getStartTimeUS() / 1000000.0f;
    }
    
    public void setStartTime(final float n) {
        this.setStartTimeUs((long)(n * 1000000.0f));
    }
    
    public float getEndTime() {
        return this.getEndTimeUS() / 1000000.0f;
    }
    
    public void setEndTime(final float n) {
        this.setEndTimeUs((long)(n * 1000000.0f));
    }
    
    public boolean contains(final TuSdkTimeRange tuSdkTimeRange) {
        return tuSdkTimeRange != null && tuSdkTimeRange.isValid() && this.isValid() && tuSdkTimeRange.a >= this.a && tuSdkTimeRange.a < this.b && tuSdkTimeRange.b <= this.b;
    }
    
    public boolean contains(final long n) {
        return this.isValid() && this.a <= n && this.b >= n;
    }
    
    public TuSdkTimeRange convertTo(final TuSdkTimeRange tuSdkTimeRange) {
        if (tuSdkTimeRange == null || !tuSdkTimeRange.isValid() || !this.isValid()) {
            return this;
        }
        return makeTimeUsRange(tuSdkTimeRange.a + this.a, tuSdkTimeRange.b + this.b);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof TuSdkTimeRange)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        final TuSdkTimeRange tuSdkTimeRange = (TuSdkTimeRange)o;
        return tuSdkTimeRange.a == this.a && tuSdkTimeRange.b == this.b;
    }
    
    @Override
    public String toString() {
        return "Range startTimeUs = " + this.a + " endTimeUs = " + this.b + "  durationTimeUS = " + this.durationTimeUS();
    }
}
