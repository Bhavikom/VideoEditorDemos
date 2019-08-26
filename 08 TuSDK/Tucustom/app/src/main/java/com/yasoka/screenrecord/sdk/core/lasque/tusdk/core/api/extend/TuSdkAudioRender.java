// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend;

//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;

import java.nio.ByteBuffer;

public interface TuSdkAudioRender
{
    boolean onAudioSliceRender(final ByteBuffer p0, final MediaCodec.BufferInfo p1, final TuSdkAudioRenderCallback p2);
    
    public interface TuSdkAudioRenderCallback
    {
        boolean isEncodec();
        
        TuSdkAudioInfo getAudioInfo();
        
        void returnRenderBuffer(final ByteBuffer p0, final MediaCodec.BufferInfo p1);
    }
}
