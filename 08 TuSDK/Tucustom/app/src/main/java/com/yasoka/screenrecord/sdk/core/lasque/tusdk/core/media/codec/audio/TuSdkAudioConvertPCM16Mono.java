// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio;

//import org.lasque.tusdk.core.utils.Complex;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ByteUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.Complex;

import java.nio.ShortBuffer;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.utils.ByteUtils;
import java.nio.ByteOrder;

public class TuSdkAudioConvertPCM16Mono extends TuSdkAudioConvertFactory.TuSdkAudioConvertBase
{
    @Override
    public byte[] toPCM8Mono(final byte[] array, final ByteOrder byteOrder) {
        final byte[] array2 = new byte[array.length / 2];
        for (int i = 0; i < array2.length; ++i) {
            array2[i] = (byte)(ByteUtils.getShort(array[2 * i], array[2 * i + 1], byteOrder) / 256);
        }
        return array2;
    }
    
    @Override
    public byte[] toPCM8Stereo(final byte[] array, final ByteOrder byteOrder) {
        final byte[] array2 = new byte[array.length];
        for (int i = 0; i < array.length; i += 2) {
            array2[i + 1] = (array2[i] = (byte)(ByteUtils.getShort(array[i], array[i + 1], byteOrder) / 256));
        }
        return array2;
    }
    
    @Override
    public byte[] toPCM16Mono(final byte[] array, final ByteOrder byteOrder) {
        return array;
    }
    
    @Override
    public byte[] toPCM16Stereo(final byte[] array, final ByteOrder byteOrder) {
        final byte[] array2 = new byte[array.length * 2];
        for (int i = 0; i < array.length; i += 2) {
            array2[i * 2] = array[i];
            array2[i * 2 + 1] = array[i + 1];
            array2[i * 2 + 2] = array[i];
            array2[i * 2 + 3] = array[i + 1];
        }
        return array2;
    }
    
    @Override
    public void toPCM8Mono(final ByteBuffer byteBuffer, final ShortBuffer shortBuffer, final ByteOrder byteOrder) {
        this.toPCM16Mono(byteBuffer, shortBuffer, byteOrder);
    }
    
    @Override
    public void toPCM8Stereo(final ByteBuffer byteBuffer, final ShortBuffer shortBuffer, final ByteOrder byteOrder) {
        this.toPCM16Stereo(byteBuffer, shortBuffer, byteOrder);
    }
    
    @Override
    public void toPCM16Mono(final ByteBuffer byteBuffer, final ShortBuffer shortBuffer, final ByteOrder byteOrder) {
        for (int min = Math.min(byteBuffer.remaining() / 2, shortBuffer.remaining()), i = 0; i < min; ++i) {
            shortBuffer.put(ByteUtils.getShort(byteBuffer.get(), byteBuffer.get(), byteOrder));
        }
    }
    
    @Override
    public void toPCM16Stereo(final ByteBuffer byteBuffer, final ShortBuffer shortBuffer, final ByteOrder byteOrder) {
        for (int n = Math.min(byteBuffer.remaining(), shortBuffer.remaining()) / 2, i = 0; i < n; ++i) {
            final short short1 = ByteUtils.getShort(byteBuffer.get(), byteBuffer.get(), byteOrder);
            shortBuffer.put(short1);
            shortBuffer.put(short1);
        }
    }
    
    @Override
    public void toPCM8Mono(final ShortBuffer shortBuffer, final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
        for (int min = Math.min(shortBuffer.remaining(), byteBuffer.remaining()), i = 0; i < min; ++i) {
            byteBuffer.put((byte)(shortBuffer.get() / 256));
        }
    }
    
    @Override
    public void toPCM8Stereo(final ShortBuffer shortBuffer, final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
        for (int min = Math.min(shortBuffer.remaining(), byteBuffer.remaining() / 2), i = 0; i < min; ++i) {
            final byte b = (byte)(shortBuffer.get() / 256);
            byteBuffer.put(b);
            byteBuffer.put(b);
        }
    }
    
    @Override
    public void toPCM16Mono(final ShortBuffer shortBuffer, final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
        for (int min = Math.min(shortBuffer.remaining(), byteBuffer.remaining() / 2), i = 0; i < min; ++i) {
            byteBuffer.put(ByteUtils.getBytes(shortBuffer.get(), byteOrder));
        }
    }
    
    @Override
    public void toPCM16Stereo(final ShortBuffer shortBuffer, final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
        for (int min = Math.min(shortBuffer.remaining(), byteBuffer.remaining() / 4), i = 0; i < min; ++i) {
            final byte[] bytes = ByteUtils.getBytes(shortBuffer.get(), byteOrder);
            byteBuffer.put(bytes);
            byteBuffer.put(bytes);
        }
    }
    
    @Override
    public TuSdkAudioData toData(final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
        final int n = byteBuffer.remaining() / 2;
        final TuSdkAudioData tuSdkAudioData = new TuSdkAudioData(1, n);
        for (int i = 0; i < n; ++i) {
            tuSdkAudioData.left[i] = new Complex(ByteUtils.getShort(byteBuffer.get(), byteBuffer.get(), byteOrder) / 32768.0, 0.0);
        }
        return tuSdkAudioData;
    }
    
    @Override
    public void toBuffer(final TuSdkAudioData tuSdkAudioData, final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
        for (int i = 0; i < tuSdkAudioData.inputLength; ++i) {
            byteBuffer.put(ByteUtils.getBytes((short)(tuSdkAudioData.left[i].safeRe() * 32767.0), byteOrder));
        }
    }
    
    @Override
    public void reverse(final ByteBuffer byteBuffer, final ByteBuffer byteBuffer2) {
        if (byteBuffer == null || byteBuffer2 == null || byteBuffer2.capacity() < byteBuffer.remaining()) {
            return;
        }
        byteBuffer2.clear();
        final byte[] array = new byte[2];
        for (int i = byteBuffer.remaining() - 2; i > -1; i -= 2) {
            byteBuffer.position(i);
            byteBuffer.get(array);
            byteBuffer2.put(array);
        }
    }
    
    @Override
    public byte[] resample(final byte[] array, final float n, final ByteOrder byteOrder) {
        final short[] shorts = ByteUtils.getShorts(array, byteOrder);
        return ByteUtils.getBytes(ByteUtils.safeShort((int)(shorts[0] + (shorts[1] - shorts[0]) * n)), byteOrder);
    }
    
    @Override
    public byte[] outputResamle(final byte[] array, final float n, final ByteOrder byteOrder) {
        return this.outputBytes(this.mInputConvert.resample(array, n, byteOrder), byteOrder);
    }
    
    @Override
    public byte[] outputBytes(final byte[] array, final ByteOrder byteOrder) {
        return this.mInputConvert.toPCM16Mono(array, byteOrder);
    }
    
    @Override
    public void outputShorts(final ByteBuffer byteBuffer, final ShortBuffer shortBuffer, final ByteOrder byteOrder) {
        this.mInputConvert.toPCM16Mono(byteBuffer, shortBuffer, byteOrder);
    }
    
    @Override
    public void outputBytes(final ShortBuffer shortBuffer, final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
        this.mInputConvert.toPCM16Mono(shortBuffer, byteBuffer, byteOrder);
    }
}
