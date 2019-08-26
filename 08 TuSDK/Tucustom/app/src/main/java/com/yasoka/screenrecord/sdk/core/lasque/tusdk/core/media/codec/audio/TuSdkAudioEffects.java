// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

public interface TuSdkAudioEffects
{
    void release();
    
    boolean enableAcousticEchoCanceler();
    
    boolean enableAutomaticGainControl();
    
    boolean enableNoiseSuppressor();
}
