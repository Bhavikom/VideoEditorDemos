// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.audio;

//import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;

import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;

public interface TuSDKAudioRecorderInterface
{
    void startRecording();
    
    void stopRecording();
    
    boolean isRecording();
    
    void mute(final boolean p0);
    
    TuSDKAudioDataEncoderInterface getAudioEncoder();
}
