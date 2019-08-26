// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioResampleSync;

public interface TuSdkAudioResample
{
    void setMediaSync(final TuSdkAudioResampleSync p0);
    
    void changeFormat(final TuSdkAudioInfo p0);
    
    void changeSpeed(final float p0);
    
    void changeSequence(final boolean p0);
    
    void setStartPrefixTimeUs(final long p0);
    
    boolean needResample();
    
    void reset();
    
    void flush();
    
    long getLastInputTimeUs();
    
    long getPrefixTimeUs();
    
    boolean queueInputBuffer(final ByteBuffer p0, final MediaCodec.BufferInfo p1);
    
    void release();
}
