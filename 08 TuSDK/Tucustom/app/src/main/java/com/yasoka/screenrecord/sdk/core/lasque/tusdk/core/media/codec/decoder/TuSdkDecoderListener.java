// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder;

import android.media.MediaCodec;

public interface TuSdkDecoderListener
{
    void onDecoderUpdated(final MediaCodec.BufferInfo p0);
    
    void onDecoderCompleted(final Exception p0);
}
