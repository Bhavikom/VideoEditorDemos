// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.audio.preproc.processor;

//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;

import java.nio.ByteBuffer;

public interface TuSdkAudioEngine
{
    void processInputBuffer(final ByteBuffer p0, final MediaCodec.BufferInfo p1);
    
    void changeAudioInfo(final TuSdkAudioInfo p0);
    
    void reset();
    
    void release();
    
    public interface TuSdKAudioEngineOutputBufferDelegate
    {
        void onProcess(final ByteBuffer p0, final MediaCodec.BufferInfo p1);
    }
}
