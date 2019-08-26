// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.sync.TuSdkAudioPitchSync;

public interface TuSdkAudioPitch
{
    void setMediaSync(final TuSdkAudioPitchSync p0);
    
    void changeFormat(final TuSdkAudioInfo p0);
    
    void changePitch(final float p0);
    
    void changeSpeed(final float p0);
    
    boolean needPitch();
    
    void reset();
    
    void flush();
    
    boolean queueInputBuffer(final ByteBuffer p0, final MediaCodec.BufferInfo p1);
    
    void release();
}
