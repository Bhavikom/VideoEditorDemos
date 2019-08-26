// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio;

import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.media.MediaFormat;

public interface TuSDKAudioDataEncoderInterface
{
    boolean initEncoder(final TuSDKAudioEncoderSetting p0);
    
    void start();
    
    void stop();
    
    void queueAudio(final byte[] p0);
    
    void onAudioEncoderStarted(final MediaFormat p0);
    
    void onAudioEncoderFrameDataAvailable(final long p0, final ByteBuffer p1, final MediaCodec.BufferInfo p2);
    
    void setDelegate(final TuSDKAudioDataEncoderDelegate p0);
    
    public interface TuSDKAudioDataEncoderDelegate
    {
        void onAudioEncoderStarted(final MediaFormat p0);
        
        void onAudioEncoderStoped();
        
        void onAudioEncoderFrameDataAvailable(final long p0, final ByteBuffer p1, final MediaCodec.BufferInfo p2);
        
        void onAudioEncoderCodecConfig(final long p0, final ByteBuffer p1, final MediaCodec.BufferInfo p2);
    }
}
