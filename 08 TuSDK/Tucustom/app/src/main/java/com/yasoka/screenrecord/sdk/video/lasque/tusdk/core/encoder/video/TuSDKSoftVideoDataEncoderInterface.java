// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video;

import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.media.MediaFormat;

public interface TuSDKSoftVideoDataEncoderInterface extends TuSDKVideoDataEncoderInterface
{
    boolean initEncoder(final TuSDKVideoEncoderSetting p0);
    
    boolean start();
    
    void stop();
    
    void queueVideo(final byte[] p0);
    
    void onVideoEncoderStarted(final MediaFormat p0);
    
    void onVideoEncoderFrameDataAvailable(final long p0, final ByteBuffer p1, final MediaCodec.BufferInfo p2);
}
