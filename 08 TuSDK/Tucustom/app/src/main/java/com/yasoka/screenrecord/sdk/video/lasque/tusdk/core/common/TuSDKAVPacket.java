// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common;

import java.nio.ByteBuffer;

public class TuSDKAVPacket
{
    public static final int AV_VIDEO_TYPE = 1;
    public static final int AV_AUDIO_TYPE = 2;
    private ByteBuffer a;
    private long b;
    private int c;
    private int d;
    private int e;
    
    public TuSDKAVPacket(final ByteBuffer byteBuffer, final long sampleTimeUs, final int packetType) {
        this.setByteBuffer(byteBuffer);
        this.setSampleTimeUs(sampleTimeUs);
        this.setPacketType(packetType);
    }
    
    public TuSDKAVPacket(final int capacity) {
        this.a = ByteBuffer.allocate(capacity);
    }
    
    public void setByteBuffer(final ByteBuffer a) {
        this.a = a;
    }
    
    public ByteBuffer getByteBuffer() {
        return this.a;
    }
    
    public void setSampleTimeUs(final long b) {
        this.b = b;
    }
    
    public long getSampleTimeUs() {
        return this.b;
    }
    
    public void setChunkSize(final int c) {
        this.c = c;
    }
    
    public int getChunkSize() {
        return this.c;
    }
    
    public void setPacketType(final int d) {
        this.d = d;
    }
    
    public int getPacketType() {
        return this.d;
    }
    
    public int getFlags() {
        return this.e;
    }
    
    public void setFlags(final int e) {
        this.e = e;
    }
}
