// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder;

import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.media.MediaFormat;

public interface TuSDKVideoDataEncoderDelegate
{
    void onVideoEncoderStarted(final MediaFormat p0);
    
    void onVideoEncoderFrameDataAvailable(final long p0, final ByteBuffer p1, final MediaCodec.BufferInfo p2);
    
    void onVideoEncoderCodecConfig(final long p0, final ByteBuffer p1, final MediaCodec.BufferInfo p2);
}
