// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class ByteUtils
{
    public static final boolean IS_BIG_ENDING;
    
    public static byte[] getBytes(final short n) {
        return new byte[] { (byte)(n & 0xFF), (byte)((n & 0xFF00) >> 8) };
    }
    
    public static byte[] getBytes(final char c) {
        return new byte[] { (byte)c, (byte)(c >> 8) };
    }
    
    public static byte[] getBytes(final int n) {
        return new byte[] { (byte)(n & 0xFF), (byte)((n & 0xFF00) >> 8), (byte)((n & 0xFF0000) >> 16), (byte)((n & 0xFF000000) >> 24) };
    }
    
    public static byte[] getBytes(final long n) {
        return new byte[] { (byte)(n & 0xFFL), (byte)(n >> 8 & 0xFFL), (byte)(n >> 16 & 0xFFL), (byte)(n >> 24 & 0xFFL), (byte)(n >> 32 & 0xFFL), (byte)(n >> 40 & 0xFFL), (byte)(n >> 48 & 0xFFL), (byte)(n >> 56 & 0xFFL) };
    }
    
    public static byte[] getBytes(final float value) {
        return getBytes(Float.floatToIntBits(value));
    }
    
    public static byte[] getBytes(final double value) {
        return getBytes(Double.doubleToLongBits(value));
    }
    
    public static byte[] getBytes(final String s, final String charsetName) {
        return s.getBytes(Charset.forName(charsetName));
    }
    
    public static byte[] getBytes(final String s) {
        return getBytes(s, "UTF-8");
    }
    
    public static short getShort(final byte[] array) {
        return (short)((0xFF & array[0]) | (0xFF00 & array[1] << 8));
    }
    
    public static char getChar(final byte[] array) {
        return (char)((0xFF & array[0]) | (0xFF00 & array[1] << 8));
    }
    
    public static int getInt(final byte[] array) {
        return (0xFF & array[0]) | (0xFF00 & array[1] << 8) | (0xFF0000 & array[2] << 16) | (0xFF000000 & array[3] << 24);
    }
    
    public static int getIntFill(final byte[] array) {
        return (0xFF & array[3]) | (0xFF00 & array[2] << 8) | (0xFF0000 & array[1] << 16) | (0xFF000000 & array[0] << 24);
    }
    
    public static long getLong(final byte[] array) {
        return (0xFFL & (long)array[0]) | (0xFF00L & (long)array[1] << 8) | (0xFF0000L & (long)array[2] << 16) | (0xFF000000L & (long)array[3] << 24) | (0xFF00000000L & (long)array[4] << 32) | (0xFF0000000000L & (long)array[5] << 40) | (0xFF000000000000L & (long)array[6] << 48) | (0xFF00000000000000L & (long)array[7] << 56);
    }
    
    public static float getFloat(final byte[] array) {
        return Float.intBitsToFloat(getInt(array));
    }
    
    public static double getDouble(final byte[] array) {
        final long long1 = getLong(array);
        System.out.println(long1);
        return Double.longBitsToDouble(long1);
    }
    
    public static String getString(final byte[] bytes, final String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }
    
    public static String getString(final byte[] array) {
        return getString(array, "UTF-8");
    }
    
    public static byte[] randomBytes(final int n) {
        final byte[] array = new byte[n];
        for (int i = 0; i < n; ++i) {
            array[i] = (byte)Math.ceil(Math.random() * 256.0);
        }
        return array;
    }
    
    public static void intToByteArrayFull(final byte[] array, final int n, final int n2) {
        array[n] = (byte)(n2 >> 24 & 0xFF);
        array[n + 1] = (byte)(n2 >> 16 & 0xFF);
        array[n + 2] = (byte)(n2 >> 8 & 0xFF);
        array[n + 3] = (byte)(n2 & 0xFF);
    }
    
    public static void intToByteArrayTwoByte(final byte[] array, final int n, final int n2) {
        array[n] = (byte)(n2 >> 8 & 0xFF);
        array[n + 1] = (byte)(n2 & 0xFF);
    }
    
    public static short getShort(final byte b, final byte b2, final ByteOrder byteOrder) {
        final int n = 0;
        short n2;
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            n2 = (short)((short)((short)(n | (b & 0xFF)) << 8) | (b2 & 0xFF));
        }
        else {
            n2 = (short)((short)((short)(n | (b2 & 0xFF)) << 8) | (b & 0xFF));
        }
        return n2;
    }
    
    public static byte[] getBytes(final short n, final ByteOrder byteOrder) {
        final byte[] array = new byte[2];
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            array[1] = (byte)(n & 0xFF);
            array[0] = (byte)((short)(n >> 8) & 0xFF);
        }
        else {
            array[0] = (byte)(n & 0xFF);
            array[1] = (byte)((short)(n >> 8) & 0xFF);
        }
        return array;
    }
    
    public static short[] getShorts(final byte[] array, final ByteOrder byteOrder) {
        if (array == null || array.length % 2 != 0) {
            return null;
        }
        final short[] array2 = new short[array.length / 2];
        for (int i = 0; i < array2.length; ++i) {
            array2[i] = getShort(array[i * 2], array[i * 2 + 1], byteOrder);
        }
        return array2;
    }
    
    public static byte[] getBytes(final short[] array, final ByteOrder byteOrder) {
        if (array == null) {
            return null;
        }
        final byte[] array2 = new byte[array.length * 2];
        for (int i = 0; i < array.length; ++i) {
            final byte[] bytes = getBytes(array[i], byteOrder);
            array2[i * 2] = bytes[0];
            array2[i * 2 + 1] = bytes[1];
        }
        return array2;
    }
    
    public static short safeShort(final int a) {
        return (short)Math.max(Math.min(a, 32767), -32768);
    }
    
    public static short[] safeShorts(final int[] array) {
        if (array == null) {
            return null;
        }
        final short[] array2 = new short[array.length];
        for (int i = 0; i < array2.length; ++i) {
            array2[i] = safeShort(array[i]);
        }
        return array2;
    }
    
    public static boolean isBigendian() {
        return 1 >> 8 == 1;
    }
    
    static {
        IS_BIG_ENDING = isBigendian();
    }
}
