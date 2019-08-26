// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

import java.nio.ShortBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public interface TuSdkAudioConvert
{
    byte[] toPCM8Mono(final byte[] p0, final ByteOrder p1);
    
    byte[] toPCM8Stereo(final byte[] p0, final ByteOrder p1);
    
    byte[] toPCM16Mono(final byte[] p0, final ByteOrder p1);
    
    byte[] toPCM16Stereo(final byte[] p0, final ByteOrder p1);
    
    void toPCM8Mono(final ByteBuffer p0, final ShortBuffer p1, final ByteOrder p2);
    
    void toPCM8Stereo(final ByteBuffer p0, final ShortBuffer p1, final ByteOrder p2);
    
    void toPCM16Mono(final ByteBuffer p0, final ShortBuffer p1, final ByteOrder p2);
    
    void toPCM16Stereo(final ByteBuffer p0, final ShortBuffer p1, final ByteOrder p2);
    
    void toPCM8Mono(final ShortBuffer p0, final ByteBuffer p1, final ByteOrder p2);
    
    void toPCM8Stereo(final ShortBuffer p0, final ByteBuffer p1, final ByteOrder p2);
    
    void toPCM16Mono(final ShortBuffer p0, final ByteBuffer p1, final ByteOrder p2);
    
    void toPCM16Stereo(final ShortBuffer p0, final ByteBuffer p1, final ByteOrder p2);
    
    TuSdkAudioData toData(final ByteBuffer p0, final ByteOrder p1);
    
    void toBuffer(final TuSdkAudioData p0, final ByteBuffer p1, final ByteOrder p2);
    
    void reverse(final ByteBuffer p0, final ByteBuffer p1);
    
    byte[] resample(final byte[] p0, final float p1, final ByteOrder p2);
    
    byte[] outputResamle(final byte[] p0, final float p1, final ByteOrder p2);
    
    byte[] outputBytes(final byte[] p0, final ByteOrder p1);
    
    byte[] outputBytes(final byte[] p0, final ByteOrder p1, final int p2, final int p3);
    
    void inputReverse(final ByteBuffer p0, final ByteBuffer p1);
    
    void outputShorts(final ByteBuffer p0, final ShortBuffer p1, final ByteOrder p2);
    
    void outputBytes(final ShortBuffer p0, final ByteBuffer p1, final ByteOrder p2);
    
    void restoreBytes(final ShortBuffer p0, final ByteBuffer p1, final ByteOrder p2);
}
