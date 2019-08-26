// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

import android.media.MediaFormat;
import android.media.MediaCodec;

public interface TuSDKMediaDecoderInterface<T extends TuSDKMovieReader>
{
    T getMediaReader();
    
    MediaCodec getVideoDecoder();
    
    MediaCodec getAudioDecoder();
    
    void start();
    
    void stop();
    
    long getCurrentSampleTimeUs();
    
    int findVideoTrack();
    
    int selectVideoTrack();
    
    void unselectVideoTrack();
    
    MediaFormat getVideoTrackFormat();
    
    int findAudioTrack();
    
    int selectAudioTrack();
    
    void unselectAudioTrack();
    
    MediaFormat getAudioTrackFormat();
    
    void destroy();
}
