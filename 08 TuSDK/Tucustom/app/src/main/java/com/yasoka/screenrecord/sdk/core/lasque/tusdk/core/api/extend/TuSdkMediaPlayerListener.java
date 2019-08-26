// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend;

//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;

public interface TuSdkMediaPlayerListener
{
    void onStateChanged(final int p0);
    
    void onFrameAvailable();
    
    void onProgress(final long p0, final TuSdkMediaDataSource p1, final long p2);
    
    void onCompleted(final Exception p0, final TuSdkMediaDataSource p1);
}
