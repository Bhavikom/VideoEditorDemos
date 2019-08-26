// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec;

public interface TuSdkDecodecOperation
{
    void flush();
    
    boolean decodecInit(final TuSdkMediaExtractor p0);
    
    boolean decodecProcessUntilEnd(final TuSdkMediaExtractor p0);
    
    void decodecRelease();
    
    void decodecException(final Exception p0);
}
