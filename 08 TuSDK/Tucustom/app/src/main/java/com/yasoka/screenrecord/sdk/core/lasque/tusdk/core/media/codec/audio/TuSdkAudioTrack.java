// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

import android.annotation.TargetApi;
import android.media.AudioTimestamp;
import java.nio.ByteBuffer;

public interface TuSdkAudioTrack
{
    int write(final ByteBuffer p0);
    
    int getPlaybackHeadPosition();
    
    @TargetApi(19)
    boolean getTimestamp(final AudioTimestamp p0);
    
    void play();
    
    void pause();
    
    int setVolume(final float p0);
    
    void flush();
    
    void release();
}
