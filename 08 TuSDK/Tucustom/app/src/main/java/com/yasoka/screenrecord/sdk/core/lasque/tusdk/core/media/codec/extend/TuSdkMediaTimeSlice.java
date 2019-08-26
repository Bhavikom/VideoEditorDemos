// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend;

import java.io.Serializable;

public class TuSdkMediaTimeSlice implements Serializable
{
    public long startUs;
    public long endUs;
    public float speed;
    public int overlapIndex;
    
    public TuSdkMediaTimeSlice() {
        this.startUs = -1L;
        this.endUs = -1L;
        this.speed = 1.0f;
        this.overlapIndex = -1;
    }
    
    public TuSdkMediaTimeSlice(final long n, final long n2) {
        this(n, n2, 1.0f);
    }
    
    public TuSdkMediaTimeSlice(final long startUs, final long endUs, final float speed) {
        this.startUs = -1L;
        this.endUs = -1L;
        this.speed = 1.0f;
        this.overlapIndex = -1;
        this.startUs = startUs;
        this.endUs = endUs;
        this.speed = speed;
    }
    
    public TuSdkMediaTimeSlice(final long startUs, final long endUs, final float speed, final int overlapIndex) {
        this.startUs = -1L;
        this.endUs = -1L;
        this.speed = 1.0f;
        this.overlapIndex = -1;
        this.startUs = startUs;
        this.endUs = endUs;
        this.speed = speed;
        this.overlapIndex = overlapIndex;
    }
    
    public TuSdkMediaTimeSlice clone() {
        return new TuSdkMediaTimeSlice(this.startUs, this.endUs, this.speed, this.overlapIndex);
    }
    
    public long reduce() {
        return this.endUs - this.startUs;
    }
    
    public boolean isReverse() {
        return this.startUs > this.endUs;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof TuSdkMediaTimeSlice)) {
            return false;
        }
        final TuSdkMediaTimeSlice tuSdkMediaTimeSlice = (TuSdkMediaTimeSlice)o;
        return tuSdkMediaTimeSlice.startUs == this.startUs && tuSdkMediaTimeSlice.endUs == this.endUs && tuSdkMediaTimeSlice.speed == this.speed;
    }
    
    public int getOverlapIndex() {
        return this.overlapIndex;
    }
    
    @Override
    public String toString() {
        final StringBuffer append = new StringBuffer("TuSdkMediaTimeSlice").append("{ \n");
        append.append("startUs: ").append(this.startUs).append(", \n");
        append.append("endUs: ").append(this.endUs).append(", \n");
        append.append("speed: ").append(this.speed).append(", \n");
        append.append("}");
        return append.toString();
    }
}
