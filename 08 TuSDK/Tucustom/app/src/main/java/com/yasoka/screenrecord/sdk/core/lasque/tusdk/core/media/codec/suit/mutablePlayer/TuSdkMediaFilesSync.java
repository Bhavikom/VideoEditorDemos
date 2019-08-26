// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

//import org.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
//import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkMediaEncodecSync;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkEncodeSurface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkMediaEncodecSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record.TuSdkRecordSurface;

public interface TuSdkMediaFilesSync extends TuSdkMediaEncodecSync
{
    long benchmarkUs();
    
    void setBenchmarkEnd();
    
    float calculateProgress();
    
    long totalDurationUs();
    
    boolean isEncodecCompleted();
    
    void syncVideoEncodecDrawFrame(final long p0, final boolean p1, final TuSdkRecordSurface p2, final TuSdkEncodeSurface p3);
}
