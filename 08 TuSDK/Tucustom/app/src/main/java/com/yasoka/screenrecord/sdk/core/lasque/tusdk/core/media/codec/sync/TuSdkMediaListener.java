// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.annotation.TargetApi;

@TargetApi(16)
public abstract class TuSdkMediaListener
{
    public abstract void onMediaOutputBuffer(final ByteBuffer p0, final MediaCodec.BufferInfo p1);
    
    public void onMediaOutputBuffer(final ByteBuffer byteBuffer, final int n, final int n2, final int n3, final long n4) {
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        bufferInfo.set(n, n2, n4, n3);
        byteBuffer.clear();
        byteBuffer.position(n);
        byteBuffer.limit(n + n2);
        this.onMediaOutputBuffer(byteBuffer, bufferInfo);
    }
}
