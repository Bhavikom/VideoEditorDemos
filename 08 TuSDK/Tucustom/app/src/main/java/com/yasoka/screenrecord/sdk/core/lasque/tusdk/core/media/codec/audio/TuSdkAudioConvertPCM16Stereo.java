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

public class TuSdkAudioConvertPCM16Stereo extends TuSdkAudioConvertFactory.TuSdkAudioConvertBase
{
    @Override
    public byte[] toPCM8Mono(final byte[] array, final ByteOrder byteOrder) {
        final byte[] array2 = new byte[array.length / 4];
        for (int i = 0; i < array2.length; ++i) {
            array2[i] = (byte)((ByteUtils.getShort(array[i * 4], array[i * 4 + 1], byteOrder) / 2 + ByteUtils.getShort(array[i * 4 + 2], array[i * 4 + 3], byteOrder) / 2) / 256);
        }
        return array2;
    }
    
    @Override
    public byte[] toPCM8Stereo(final byte[] array, final ByteOrder byteOrder) {
        final byte[] array2 = new byte[array.length / 2];
        for (int i = 0; i < array2.length; ++i) {
            array2[i] = (byte)(ByteUtils.getShort(array[i * 2], array[i * 2 + 1], byteOrder) / 256);
        }
        return array2;
    }
    
    @Override
    public byte[] toPCM16Mono(final byte[] array, final ByteOrder byteOrder) {
        final byte[] array2 = new byte[array.length / 2];
        for (int i = 0; i < array2.length; i += 2) {
            final byte[] bytes = ByteUtils.getBytes((short)(ByteUtils.getShort(array[i * 2], array[i * 2 + 1], byteOrder) / 2 + ByteUtils.getShort(array[i * 2 + 2], array[i * 2 + 3], byteOrder) / 2), byteOrder);
            array2[i] = bytes[0];
            array2[i + 1] = bytes[1];
        }
        return array2;
    }
    
    @Override
    public byte[] toPCM16Stereo(final byte[] array, final ByteOrder byteOrder) {
        return array;
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
        for (int min = Math.min(byteBuffer.remaining() / 4, shortBuffer.remaining()), i = 0; i < min; ++i) {
            shortBuffer.put((short)(ByteUtils.getShort(byteBuffer.get(), byteBuffer.get(), byteOrder) / 2 + ByteUtils.getShort(byteBuffer.get(), byteBuffer.get(), byteOrder) / 2));
        }
    }
    
    @Override
    public void toPCM16Stereo(final ByteBuffer byteBuffer, final ShortBuffer shortBuffer, final ByteOrder byteOrder) {
        for (int min = Math.min(byteBuffer.remaining() / 2, shortBuffer.remaining()), i = 0; i < min; ++i) {
            shortBuffer.put(ByteUtils.getShort(byteBuffer.get(), byteBuffer.get(), byteOrder));
        }
    }
    
    @Override
    public void toPCM8Mono(final ShortBuffer shortBuffer, final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
        for (int min = Math.min(shortBuffer.remaining() / 2, byteBuffer.remaining()), i = 0; i < min; ++i) {
            byteBuffer.put((byte)((short)(shortBuffer.get() / 2 + shortBuffer.get() / 2) / 256));
        }
    }
    
    @Override
    public void toPCM8Stereo(final ShortBuffer shortBuffer, final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
        for (int min = Math.min(shortBuffer.remaining(), byteBuffer.remaining()), i = 0; i < min; ++i) {
            byteBuffer.put((byte)(shortBuffer.get() / 256));
        }
    }
    
    @Override
    public void toPCM16Mono(final ShortBuffer shortBuffer, final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
        for (int n = Math.min(shortBuffer.remaining(), byteBuffer.remaining()) / 2, i = 0; i < n; ++i) {
            byteBuffer.put(ByteUtils.getBytes((short)(shortBuffer.get() / 2 + shortBuffer.get() / 2), byteOrder));
        }
    }
    
    @Override
    public void toPCM16Stereo(final ShortBuffer shortBuffer, final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
        for (int min = Math.min(shortBuffer.remaining(), byteBuffer.remaining() / 2), i = 0; i < min; ++i) {
            byteBuffer.put(ByteUtils.getBytes(shortBuffer.get(), byteOrder));
        }
    }
    
    @Override
    public TuSdkAudioData toData(final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
        final int n = byteBuffer.remaining() / 4;
        final TuSdkAudioData tuSdkAudioData = new TuSdkAudioData(2, n);
        for (int i = 0; i < n; ++i) {
            tuSdkAudioData.left[i] = new Complex(ByteUtils.getShort(byteBuffer.get(), byteBuffer.get(), byteOrder) / 32768.0, 0.0);
            tuSdkAudioData.right[i] = new Complex(ByteUtils.getShort(byteBuffer.get(), byteBuffer.get(), byteOrder) / 32768.0, 0.0);
        }
        return tuSdkAudioData;
    }
    
    @Override
    public void toBuffer(final TuSdkAudioData tuSdkAudioData, final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
        for (int i = 0; i < tuSdkAudioData.inputLength; ++i) {
            byteBuffer.put(ByteUtils.getBytes((short)(tuSdkAudioData.left[i].safeRe() * 32767.0), byteOrder));
            byteBuffer.put(ByteUtils.getBytes((short)(tuSdkAudioData.right[i].safeRe() * 32767.0), byteOrder));
        }
    }
    
    @Override
    public void reverse(final ByteBuffer byteBuffer, final ByteBuffer byteBuffer2) {
        if (byteBuffer == null || byteBuffer2 == null || byteBuffer2.capacity() < byteBuffer.remaining()) {
            return;
        }
        byteBuffer2.clear();
        final byte[] array = new byte[4];
        for (int i = byteBuffer.remaining() - 4; i > -1; i -= 4) {
            byteBuffer.position(i);
            byteBuffer.get(array);
            byteBuffer2.put(array);
        }
    }
    
    @Override
    public byte[] resample(final byte[] array, final float n, final ByteOrder byteOrder) {
        final short[] shorts = ByteUtils.getShorts(array, byteOrder);
        return ByteUtils.getBytes(ByteUtils.safeShorts(new int[] { (int)(shorts[0] + (shorts[2] - shorts[0]) * n), (int)(shorts[1] + (shorts[3] - shorts[1]) * n) }), byteOrder);
    }
    
    @Override
    public byte[] outputResamle(final byte[] array, final float n, final ByteOrder byteOrder) {
        return this.outputBytes(this.mInputConvert.resample(array, n, byteOrder), byteOrder);
    }
    
    @Override
    public byte[] outputBytes(final byte[] array, final ByteOrder byteOrder) {
        return this.mInputConvert.toPCM16Stereo(array, byteOrder);
    }
    
    @Override
    public void outputShorts(final ByteBuffer byteBuffer, final ShortBuffer shortBuffer, final ByteOrder byteOrder) {
        this.mInputConvert.toPCM16Stereo(byteBuffer, shortBuffer, byteOrder);
    }
    
    @Override
    public void outputBytes(final ShortBuffer shortBuffer, final ByteBuffer byteBuffer, final ByteOrder byteOrder) {
        this.mInputConvert.toPCM16Stereo(shortBuffer, byteBuffer, byteOrder);
    }
}
