// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaSync;

public interface TuSdkVideoDecodecSync extends TuSdkMediaSync
{
    void syncVideoDecodeCompleted();
    
    boolean isVideoDecodeCompleted();
    
    boolean isVideoDecodeCrashed();
    
    boolean hasVideoDecodeTrack();
    
    void syncVideoDecodeCrashed(final Exception p0);
    
    void syncVideoDecodecInfo(final TuSdkVideoInfo p0, final TuSdkMediaExtractor p1);
    
    boolean syncVideoDecodecExtractor(final TuSdkMediaExtractor p0, final TuSdkMediaCodec p1);
    
    void syncVideoDecodecOutputBuffer(final ByteBuffer p0, final MediaCodec.BufferInfo p1, final TuSdkVideoInfo p2);
    
    void syncVideoDecodecUpdated(final MediaCodec.BufferInfo p0);
    
    long calcInputTimeUs(final long p0);
    
    long calcEffectFrameTimeUs(final long p0);
}
