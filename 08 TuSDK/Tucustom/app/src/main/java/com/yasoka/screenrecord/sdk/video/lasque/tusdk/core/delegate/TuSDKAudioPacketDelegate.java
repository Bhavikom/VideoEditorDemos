// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.delegate;

import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.media.MediaFormat;

public interface TuSDKAudioPacketDelegate
{
    void onAudioInfoReady(final MediaFormat p0);
    
    void onAudioPacketAvailable(final long p0, final ByteBuffer p1, final MediaCodec.BufferInfo p2);
}
