// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.media.codec.TuSdkEncodecOperation;

public interface TuSdkAudioEncodecOperation extends TuSdkEncodecOperation
{
    void setAudioRender(final TuSdkAudioRender p0);
    
    TuSdkAudioInfo getAudioInfo();
    
    int writeBuffer(final ByteBuffer p0, final MediaCodec.BufferInfo p1);
    
    void autoFillMuteData(final long p0, final long p1, final boolean p2);
    
    void autoFillMuteDataWithinEnd(final long p0, final long p1);
    
    void signalEndOfInputStream(final long p0);
}
