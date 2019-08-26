// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend;

import android.media.MediaCodec;
import java.nio.ByteBuffer;

public class TuSdkBufferCache
{
    public ByteBuffer buffer;
    public MediaCodec.BufferInfo info;
    
    public TuSdkBufferCache() {
    }
    
    public TuSdkBufferCache(final ByteBuffer buffer, final MediaCodec.BufferInfo info) {
        this.buffer = buffer;
        this.info = info;
    }
    
    public void clear() {
        this.buffer.clear();
        this.info.flags = 0;
        this.info.size = 0;
        this.info.offset = 0;
        this.info.presentationTimeUs = -1L;
    }
}
