// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaSync;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaSync;

public interface TuSdkAudioPitchSync extends TuSdkMediaSync
{
    void syncAudioPitchOutputBuffer(final ByteBuffer p0, final MediaCodec.BufferInfo p1);
}
