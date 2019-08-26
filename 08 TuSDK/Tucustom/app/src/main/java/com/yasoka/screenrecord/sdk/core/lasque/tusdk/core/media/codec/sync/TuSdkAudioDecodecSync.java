// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaSync;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaSync;

public interface TuSdkAudioDecodecSync extends TuSdkMediaSync
{
    void syncAudioDecodeCompleted();
    
    boolean isAudioDecodeCompleted();
    
    boolean isAudioDecodeCrashed();
    
    boolean hasAudioDecodeTrack();
    
    void syncAudioDecodeCrashed(final Exception p0);
    
    void syncAudioDecodecInfo(final TuSdkAudioInfo p0, final TuSdkMediaExtractor p1);
    
    boolean syncAudioDecodecExtractor(final TuSdkMediaExtractor p0, final TuSdkMediaCodec p1);
    
    void syncAudioDecodecOutputBuffer(final ByteBuffer p0, final MediaCodec.BufferInfo p1, final TuSdkAudioInfo p2);
    
    void syncAudioDecodecUpdated(final MediaCodec.BufferInfo p0);
}
