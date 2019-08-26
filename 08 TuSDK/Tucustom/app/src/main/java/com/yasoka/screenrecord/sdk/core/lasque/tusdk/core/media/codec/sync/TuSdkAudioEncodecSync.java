// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaMuxer;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaSync;

public interface TuSdkAudioEncodecSync extends TuSdkMediaSync
{
    boolean isAudioEncodeCompleted();
    
    void syncAudioEncodecInfo(final TuSdkAudioInfo p0);
    
    void syncAudioEncodecOutputBuffer(final TuSdkMediaMuxer p0, final int p1, final ByteBuffer p2, final MediaCodec.BufferInfo p3);
    
    void syncAudioEncodecUpdated(final MediaCodec.BufferInfo p0);
    
    void syncAudioEncodecCompleted();
}
