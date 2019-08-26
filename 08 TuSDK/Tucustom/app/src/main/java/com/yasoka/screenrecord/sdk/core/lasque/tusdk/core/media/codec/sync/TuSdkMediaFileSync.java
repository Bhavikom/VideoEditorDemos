// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

//import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
//import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioEncodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record.TuSdkRecordSurface;

public interface TuSdkMediaFileSync extends TuSdkMediaDecodecSync, TuSdkMediaEncodecSync
{
    long benchmarkUs();
    
    void setBenchmarkEnd();
    
    float calculateProgress();
    
    void setTimeline(final TuSdkMediaTimeline p0);
    
    void addAudioEncodecOperation(final TuSdkAudioEncodecOperation p0);
    
    long totalDurationUs();
    
    long processedUs();
    
    long lastVideoDecodecTimestampNs();
    
    boolean isEncodecCompleted();
    
    void syncVideoDecodeCompleted();
    
    boolean isVideoDecodeCompleted();
    
    void syncAudioDecodeCompleted();
    
    boolean isAudioDecodeCompleted();
    
    boolean isAudioDecodeCrashed();
    
    void syncVideoEncodecDrawFrame(final long p0, final boolean p1, final TuSdkRecordSurface p2, final TuSdkEncodeSurface p3);
}
