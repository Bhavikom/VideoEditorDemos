// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif;

import java.nio.ByteBuffer;
import java.io.InputStream;

class ByteBufferInputStream extends InputStream
{
    private ByteBuffer a;
    
    public ByteBufferInputStream(final ByteBuffer a) {
        this.a = a;
    }
    
    @Override
    public int read() {
        if (!this.a.hasRemaining()) {
            return -1;
        }
        return this.a.get() & 0xFF;
    }
    
    @Override
    public int read(final byte[] dst, final int offset, int min) {
        if (!this.a.hasRemaining()) {
            return -1;
        }
        min = Math.min(min, this.a.remaining());
        this.a.get(dst, offset, min);
        return min;
    }
}
