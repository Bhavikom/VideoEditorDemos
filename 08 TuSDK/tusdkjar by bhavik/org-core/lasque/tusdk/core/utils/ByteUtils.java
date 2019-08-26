package org.lasque.tusdk.core.utils;

import java.io.PrintStream;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class ByteUtils
{
  public static final boolean IS_BIG_ENDING = ;
  
  public static byte[] getBytes(short paramShort)
  {
    byte[] arrayOfByte = new byte[2];
    arrayOfByte[0] = ((byte)(paramShort & 0xFF));
    arrayOfByte[1] = ((byte)((paramShort & 0xFF00) >> 8));
    return arrayOfByte;
  }
  
  public static byte[] getBytes(char paramChar)
  {
    byte[] arrayOfByte = new byte[2];
    arrayOfByte[0] = ((byte)paramChar);
    arrayOfByte[1] = ((byte)(paramChar >> '\b'));
    return arrayOfByte;
  }
  
  public static byte[] getBytes(int paramInt)
  {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = ((byte)(paramInt & 0xFF));
    arrayOfByte[1] = ((byte)((paramInt & 0xFF00) >> 8));
    arrayOfByte[2] = ((byte)((paramInt & 0xFF0000) >> 16));
    arrayOfByte[3] = ((byte)((paramInt & 0xFF000000) >> 24));
    return arrayOfByte;
  }
  
  public static byte[] getBytes(long paramLong)
  {
    byte[] arrayOfByte = new byte[8];
    arrayOfByte[0] = ((byte)(int)(paramLong & 0xFF));
    arrayOfByte[1] = ((byte)(int)(paramLong >> 8 & 0xFF));
    arrayOfByte[2] = ((byte)(int)(paramLong >> 16 & 0xFF));
    arrayOfByte[3] = ((byte)(int)(paramLong >> 24 & 0xFF));
    arrayOfByte[4] = ((byte)(int)(paramLong >> 32 & 0xFF));
    arrayOfByte[5] = ((byte)(int)(paramLong >> 40 & 0xFF));
    arrayOfByte[6] = ((byte)(int)(paramLong >> 48 & 0xFF));
    arrayOfByte[7] = ((byte)(int)(paramLong >> 56 & 0xFF));
    return arrayOfByte;
  }
  
  public static byte[] getBytes(float paramFloat)
  {
    int i = Float.floatToIntBits(paramFloat);
    return getBytes(i);
  }
  
  public static byte[] getBytes(double paramDouble)
  {
    long l = Double.doubleToLongBits(paramDouble);
    return getBytes(l);
  }
  
  public static byte[] getBytes(String paramString1, String paramString2)
  {
    Charset localCharset = Charset.forName(paramString2);
    return paramString1.getBytes(localCharset);
  }
  
  public static byte[] getBytes(String paramString)
  {
    return getBytes(paramString, "UTF-8");
  }
  
  public static short getShort(byte[] paramArrayOfByte)
  {
    return (short)(0xFF & paramArrayOfByte[0] | 0xFF00 & paramArrayOfByte[1] << 8);
  }
  
  public static char getChar(byte[] paramArrayOfByte)
  {
    return (char)(0xFF & paramArrayOfByte[0] | 0xFF00 & paramArrayOfByte[1] << 8);
  }
  
  public static int getInt(byte[] paramArrayOfByte)
  {
    return 0xFF & paramArrayOfByte[0] | 0xFF00 & paramArrayOfByte[1] << 8 | 0xFF0000 & paramArrayOfByte[2] << 16 | 0xFF000000 & paramArrayOfByte[3] << 24;
  }
  
  public static int getIntFill(byte[] paramArrayOfByte)
  {
    return 0xFF & paramArrayOfByte[3] | 0xFF00 & paramArrayOfByte[2] << 8 | 0xFF0000 & paramArrayOfByte[1] << 16 | 0xFF000000 & paramArrayOfByte[0] << 24;
  }
  
  public static long getLong(byte[] paramArrayOfByte)
  {
    return 0xFF & paramArrayOfByte[0] | 0xFF00 & paramArrayOfByte[1] << 8 | 0xFF0000 & paramArrayOfByte[2] << 16 | 0xFF000000 & paramArrayOfByte[3] << 24 | 0xFF00000000 & paramArrayOfByte[4] << 32 | 0xFF0000000000 & paramArrayOfByte[5] << 40 | 0xFF000000000000 & paramArrayOfByte[6] << 48 | 0xFF00000000000000 & paramArrayOfByte[7] << 56;
  }
  
  public static float getFloat(byte[] paramArrayOfByte)
  {
    return Float.intBitsToFloat(getInt(paramArrayOfByte));
  }
  
  public static double getDouble(byte[] paramArrayOfByte)
  {
    long l = getLong(paramArrayOfByte);
    System.out.println(l);
    return Double.longBitsToDouble(l);
  }
  
  public static String getString(byte[] paramArrayOfByte, String paramString)
  {
    return new String(paramArrayOfByte, Charset.forName(paramString));
  }
  
  public static String getString(byte[] paramArrayOfByte)
  {
    return getString(paramArrayOfByte, "UTF-8");
  }
  
  public static byte[] randomBytes(int paramInt)
  {
    byte[] arrayOfByte = new byte[paramInt];
    for (int i = 0; i < paramInt; i++) {
      arrayOfByte[i] = ((byte)(int)Math.ceil(Math.random() * 256.0D));
    }
    return arrayOfByte;
  }
  
  public static void intToByteArrayFull(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    paramArrayOfByte[paramInt1] = ((byte)(paramInt2 >> 24 & 0xFF));
    paramArrayOfByte[(paramInt1 + 1)] = ((byte)(paramInt2 >> 16 & 0xFF));
    paramArrayOfByte[(paramInt1 + 2)] = ((byte)(paramInt2 >> 8 & 0xFF));
    paramArrayOfByte[(paramInt1 + 3)] = ((byte)(paramInt2 & 0xFF));
  }
  
  public static void intToByteArrayTwoByte(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    paramArrayOfByte[paramInt1] = ((byte)(paramInt2 >> 8 & 0xFF));
    paramArrayOfByte[(paramInt1 + 1)] = ((byte)(paramInt2 & 0xFF));
  }
  
  public static short getShort(byte paramByte1, byte paramByte2, ByteOrder paramByteOrder)
  {
    short s = 0;
    if (paramByteOrder == ByteOrder.BIG_ENDIAN)
    {
      s = (short)(s | paramByte1 & 0xFF);
      s = (short)(s << 8);
      s = (short)(s | paramByte2 & 0xFF);
    }
    else
    {
      s = (short)(s | paramByte2 & 0xFF);
      s = (short)(s << 8);
      s = (short)(s | paramByte1 & 0xFF);
    }
    return s;
  }
  
  public static byte[] getBytes(short paramShort, ByteOrder paramByteOrder)
  {
    byte[] arrayOfByte = new byte[2];
    if (paramByteOrder == ByteOrder.BIG_ENDIAN)
    {
      arrayOfByte[1] = ((byte)(paramShort & 0xFF));
      paramShort = (short)(paramShort >> 8);
      arrayOfByte[0] = ((byte)(paramShort & 0xFF));
    }
    else
    {
      arrayOfByte[0] = ((byte)(paramShort & 0xFF));
      paramShort = (short)(paramShort >> 8);
      arrayOfByte[1] = ((byte)(paramShort & 0xFF));
    }
    return arrayOfByte;
  }
  
  public static short[] getShorts(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    if ((paramArrayOfByte == null) || (paramArrayOfByte.length % 2 != 0)) {
      return null;
    }
    short[] arrayOfShort = new short[paramArrayOfByte.length / 2];
    for (int i = 0; i < arrayOfShort.length; i++) {
      arrayOfShort[i] = getShort(paramArrayOfByte[(i * 2)], paramArrayOfByte[(i * 2 + 1)], paramByteOrder);
    }
    return arrayOfShort;
  }
  
  public static byte[] getBytes(short[] paramArrayOfShort, ByteOrder paramByteOrder)
  {
    if (paramArrayOfShort == null) {
      return null;
    }
    byte[] arrayOfByte1 = new byte[paramArrayOfShort.length * 2];
    for (int i = 0; i < paramArrayOfShort.length; i++)
    {
      byte[] arrayOfByte2 = getBytes(paramArrayOfShort[i], paramByteOrder);
      arrayOfByte1[(i * 2)] = arrayOfByte2[0];
      arrayOfByte1[(i * 2 + 1)] = arrayOfByte2[1];
    }
    return arrayOfByte1;
  }
  
  public static short safeShort(int paramInt)
  {
    return (short)Math.max(Math.min(paramInt, 32767), 32768);
  }
  
  public static short[] safeShorts(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null) {
      return null;
    }
    short[] arrayOfShort = new short[paramArrayOfInt.length];
    for (int i = 0; i < arrayOfShort.length; i++) {
      arrayOfShort[i] = safeShort(paramArrayOfInt[i]);
    }
    return arrayOfShort;
  }
  
  public static boolean isBigendian()
  {
    int i = 1;
    boolean bool = i >> 8 == 1;
    return bool;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\ByteUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */