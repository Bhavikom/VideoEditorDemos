// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaFormat;
import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.nio.ByteBuffer;

public class AVSampleBuffer
{
    public static final int AV_BUFFER_FLAG_NONE = -1;
    public static final int AV_BUFFER_FLAG_DATA = 0;
    public static final int AV_BUFFER_FLAG_FORMAT = 1;
    public static final int AV_BUFFER_FLAG_DECODE_ONLY = 2;
    private ByteBuffer a;
    private MediaCodec.BufferInfo b;
    private MediaFormat c;
    private int d;
    private long e;
    
    AVSampleBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final MediaFormat mediaFormat) {
        this(byteBuffer, bufferInfo, mediaFormat, 0);
    }
    
    AVSampleBuffer(final ByteBuffer a, final MediaCodec.BufferInfo b, final MediaFormat c, final int d) {
        this.d = -1;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
    
    AVSampleBuffer(final MediaFormat c) {
        this.d = -1;
        this.c = c;
        this.d = 1;
    }
    
    public void setRenderTimeUs(final long e) {
        this.e = e;
    }
    
    public long renderTimeUs() {
        return this.e;
    }
    
    public MediaFormat format() {
        return this.c;
    }
    
    public ByteBuffer buffer() {
        return this.a;
    }
    
    public MediaCodec.BufferInfo info() {
        return this.b;
    }
    
    public boolean isKeyFrame() {
        if (this.b == null) {
            TLog.w("%s : isKeyFrame return false. because info is null.", this);
            return false;
        }
        return (this.b.flags & 0x1) != 0x0;
    }
    
    public boolean isDecodeOnly() {
        return this.d == 2;
    }
    
    public boolean isFormat() {
        return this.d == 1;
    }
    
    public boolean isData() {
        return this.d == 0;
    }
}
