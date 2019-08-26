// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec;

public interface TuSdkEncodecOperation
{
    boolean isEncodecStarted();
    
    boolean encodecInit(final TuSdkMediaMuxer p0);
    
    boolean encodecProcessUntilEnd(final TuSdkMediaMuxer p0);
    
    void encodecRelease();
    
    void encodecException(final Exception p0);
    
    void flush();
}
