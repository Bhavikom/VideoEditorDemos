// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder;

public class TuSdkMediaFrameInfo
{
    public long startTimeUs;
    public long endTimeUs;
    public long intervalUs;
    public long keyFrameIntervalUs;
    public int keyFrameRate;
    public long skipMinUs;
    public long skipPreviousMinUs;
    
    public TuSdkMediaFrameInfo() {
        this.keyFrameIntervalUs = -1L;
        this.keyFrameRate = -1;
        this.skipMinUs = -1L;
        this.skipPreviousMinUs = -1L;
    }
    
    public boolean supportAllKeys() {
        return this.keyFrameRate == 0;
    }
    
    @Override
    public String toString() {
        final StringBuffer append = new StringBuffer("TuSdkVideoFileFrame").append("{ \n");
        append.append("startTimeUs: ").append(this.startTimeUs).append(", \n");
        append.append("endTimeUs: ").append(this.endTimeUs).append(", \n");
        append.append("intervalUs: ").append(this.intervalUs).append(", \n");
        append.append("keyFrameIntervalUs: ").append(this.keyFrameIntervalUs).append(", \n");
        append.append("keyFrameRate: ").append(this.keyFrameRate).append(", \n");
        append.append("skipMinUs: ").append(this.skipMinUs).append(", \n");
        append.append("skipPreviousMinUs: ").append(this.skipPreviousMinUs).append(", \n");
        append.append("}");
        return append.toString();
    }
}
