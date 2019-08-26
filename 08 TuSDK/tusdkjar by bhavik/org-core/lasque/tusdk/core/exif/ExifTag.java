package org.lasque.tusdk.core.exif;

import android.annotation.SuppressLint;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class ExifTag
{
  public static final short TYPE_UNSIGNED_BYTE = 1;
  public static final short TYPE_ASCII = 2;
  public static final short TYPE_UNSIGNED_SHORT = 3;
  public static final short TYPE_UNSIGNED_LONG = 4;
  public static final short TYPE_UNSIGNED_RATIONAL = 5;
  public static final short TYPE_UNDEFINED = 7;
  public static final short TYPE_LONG = 9;
  public static final short TYPE_RATIONAL = 10;
  private static final int[] a = new int[11];
  @SuppressLint({"SimpleDateFormat"})
  private static final SimpleDateFormat b = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss");
  private static Charset c = Charset.forName("US-ASCII");
  private final short d;
  private final short e;
  private boolean f;
  private int g;
  private int h;
  private Object i;
  private int j;
  
  ExifTag(short paramShort1, short paramShort2, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this.d = paramShort1;
    this.e = paramShort2;
    this.g = paramInt1;
    this.f = paramBoolean;
    this.h = paramInt2;
    this.i = null;
  }
  
  public static boolean isValidIfd(int paramInt)
  {
    return (paramInt == 0) || (paramInt == 1) || (paramInt == 2) || (paramInt == 3) || (paramInt == 4);
  }
  
  public static boolean isValidType(short paramShort)
  {
    return (paramShort == 1) || (paramShort == 2) || (paramShort == 3) || (paramShort == 4) || (paramShort == 5) || (paramShort == 7) || (paramShort == 9) || (paramShort == 10);
  }
  
  public int getIfd()
  {
    return this.h;
  }
  
  protected void setIfd(int paramInt)
  {
    this.h = paramInt;
  }
  
  public short getTagId()
  {
    return this.d;
  }
  
  public int getDataSize()
  {
    return getComponentCount() * getElementSize(getDataType());
  }
  
  public int getComponentCount()
  {
    return this.g;
  }
  
  public static int getElementSize(short paramShort)
  {
    return a[paramShort];
  }
  
  public short getDataType()
  {
    return this.e;
  }
  
  protected void forceSetComponentCount(int paramInt)
  {
    this.g = paramInt;
  }
  
  public boolean hasValue()
  {
    return this.i != null;
  }
  
  public boolean setValue(int[] paramArrayOfInt)
  {
    if (a(paramArrayOfInt.length)) {
      return false;
    }
    if ((this.e != 3) && (this.e != 9) && (this.e != 4)) {
      return false;
    }
    if ((this.e == 3) && (a(paramArrayOfInt))) {
      return false;
    }
    if ((this.e == 4) && (b(paramArrayOfInt))) {
      return false;
    }
    long[] arrayOfLong = new long[paramArrayOfInt.length];
    for (int k = 0; k < paramArrayOfInt.length; k++) {
      arrayOfLong[k] = paramArrayOfInt[k];
    }
    this.i = arrayOfLong;
    this.g = paramArrayOfInt.length;
    return true;
  }
  
  public boolean setValue(int paramInt)
  {
    return setValue(new int[] { paramInt });
  }
  
  public boolean setValue(long[] paramArrayOfLong)
  {
    if ((a(paramArrayOfLong.length)) || (this.e != 4)) {
      return false;
    }
    if (a(paramArrayOfLong)) {
      return false;
    }
    this.i = paramArrayOfLong;
    this.g = paramArrayOfLong.length;
    return true;
  }
  
  public boolean setValue(long paramLong)
  {
    return setValue(new long[] { paramLong });
  }
  
  public boolean setValue(Rational[] paramArrayOfRational)
  {
    if (a(paramArrayOfRational.length)) {
      return false;
    }
    if ((this.e != 5) && (this.e != 10)) {
      return false;
    }
    if ((this.e == 5) && (a(paramArrayOfRational))) {
      return false;
    }
    if ((this.e == 10) && (b(paramArrayOfRational))) {
      return false;
    }
    this.i = paramArrayOfRational;
    this.g = paramArrayOfRational.length;
    return true;
  }
  
  public boolean setValue(Rational paramRational)
  {
    return setValue(new Rational[] { paramRational });
  }
  
  public boolean setValue(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (a(paramInt2)) {
      return false;
    }
    if ((this.e != 1) && (this.e != 7)) {
      return false;
    }
    this.i = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, this.i, 0, paramInt2);
    this.g = paramInt2;
    return true;
  }
  
  public boolean setValue(byte[] paramArrayOfByte)
  {
    return setValue(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public boolean setValue(byte paramByte)
  {
    return setValue(new byte[] { paramByte });
  }
  
  public boolean setValue(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if ((paramObject instanceof Short)) {
      return setValue(((Short)paramObject).shortValue() & 0xFFFF);
    }
    if ((paramObject instanceof String)) {
      return setValue((String)paramObject);
    }
    if ((paramObject instanceof int[])) {
      return setValue((int[])paramObject);
    }
    if ((paramObject instanceof long[])) {
      return setValue((long[])paramObject);
    }
    if ((paramObject instanceof Rational)) {
      return setValue((Rational)paramObject);
    }
    if ((paramObject instanceof Rational[])) {
      return setValue((Rational[])paramObject);
    }
    if ((paramObject instanceof byte[])) {
      return setValue((byte[])paramObject);
    }
    if ((paramObject instanceof Integer)) {
      return setValue(((Integer)paramObject).intValue());
    }
    if ((paramObject instanceof Long)) {
      return setValue(((Long)paramObject).longValue());
    }
    if ((paramObject instanceof Byte)) {
      return setValue(((Byte)paramObject).byteValue());
    }
    Object localObject1;
    Object localObject2;
    int k;
    if ((paramObject instanceof Short[]))
    {
      localObject1 = (Short[])paramObject;
      localObject2 = new int[localObject1.length];
      for (k = 0; k < localObject1.length; k++) {
        localObject2[k] = (localObject1[k] == null ? 0 : localObject1[k].shortValue() & 0xFFFF);
      }
      return setValue((int[])localObject2);
    }
    if ((paramObject instanceof Integer[]))
    {
      localObject1 = (Integer[])paramObject;
      localObject2 = new int[localObject1.length];
      for (k = 0; k < localObject1.length; k++) {
        localObject2[k] = (localObject1[k] == null ? 0 : localObject1[k].intValue());
      }
      return setValue((int[])localObject2);
    }
    if ((paramObject instanceof Long[]))
    {
      localObject1 = (Long[])paramObject;
      localObject2 = new long[localObject1.length];
      for (k = 0; k < localObject1.length; k++) {
        localObject2[k] = (localObject1[k] == null ? 0L : localObject1[k].longValue());
      }
      return setValue((long[])localObject2);
    }
    if ((paramObject instanceof Byte[]))
    {
      localObject1 = (Byte[])paramObject;
      localObject2 = new byte[localObject1.length];
      for (k = 0; k < localObject1.length; k++) {
        localObject2[k] = (localObject1[k] == null ? 0 : localObject1[k].byteValue());
      }
      return setValue((byte[])localObject2);
    }
    return false;
  }
  
  /* Error */
  public boolean setTimeValue(long paramLong)
  {
    // Byte code:
    //   0: getstatic 63	org/lasque/tusdk/core/exif/ExifTag:b	Ljava/text/SimpleDateFormat;
    //   3: dup
    //   4: astore_3
    //   5: monitorenter
    //   6: aload_0
    //   7: getstatic 63	org/lasque/tusdk/core/exif/ExifTag:b	Ljava/text/SimpleDateFormat;
    //   10: new 53	java/util/Date
    //   13: dup
    //   14: lload_1
    //   15: invokespecial 100	java/util/Date:<init>	(J)V
    //   18: invokevirtual 92	java/text/SimpleDateFormat:format	(Ljava/util/Date;)Ljava/lang/String;
    //   21: invokevirtual 122	org/lasque/tusdk/core/exif/ExifTag:setValue	(Ljava/lang/String;)Z
    //   24: aload_3
    //   25: monitorexit
    //   26: ireturn
    //   27: astore 4
    //   29: aload_3
    //   30: monitorexit
    //   31: aload 4
    //   33: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	34	0	this	ExifTag
    //   0	34	1	paramLong	long
    //   4	26	3	Ljava/lang/Object;	Object
    //   27	5	4	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   6	26	27	finally
    //   27	31	27	finally
  }
  
  public boolean setValue(String paramString)
  {
    if ((this.e != 2) && (this.e != 7)) {
      return false;
    }
    byte[] arrayOfByte1 = paramString.getBytes(c);
    byte[] arrayOfByte2 = arrayOfByte1;
    if (arrayOfByte1.length > 0) {
      arrayOfByte2 = (arrayOfByte1[(arrayOfByte1.length - 1)] == 0) || (this.e == 7) ? arrayOfByte1 : Arrays.copyOf(arrayOfByte1, arrayOfByte1.length + 1);
    } else if ((this.e == 2) && (this.g == 1)) {
      arrayOfByte2 = new byte[] { 0 };
    }
    int k = arrayOfByte2.length;
    if (a(k)) {
      return false;
    }
    this.g = k;
    this.i = arrayOfByte2;
    return true;
  }
  
  private boolean a(int paramInt)
  {
    return (this.f) && (this.g != paramInt);
  }
  
  public String getValueAsString(String paramString)
  {
    String str = getValueAsString();
    if (str == null) {
      return paramString;
    }
    return str;
  }
  
  public String getValueAsString()
  {
    if (this.i == null) {
      return null;
    }
    if ((this.i instanceof String)) {
      return (String)this.i;
    }
    if ((this.i instanceof byte[])) {
      return new String((byte[])this.i, c);
    }
    return null;
  }
  
  public byte getValueAsByte(byte paramByte)
  {
    byte[] arrayOfByte = getValueAsBytes();
    if ((arrayOfByte == null) || (arrayOfByte.length < 1)) {
      return paramByte;
    }
    return arrayOfByte[0];
  }
  
  public byte[] getValueAsBytes()
  {
    if ((this.i instanceof byte[])) {
      return (byte[])this.i;
    }
    return null;
  }
  
  public Rational getValueAsRational(long paramLong)
  {
    Rational localRational = new Rational(paramLong, 1L);
    return getValueAsRational(localRational);
  }
  
  public Rational getValueAsRational(Rational paramRational)
  {
    Rational[] arrayOfRational = getValueAsRationals();
    if ((arrayOfRational == null) || (arrayOfRational.length < 1)) {
      return paramRational;
    }
    return arrayOfRational[0];
  }
  
  public Rational[] getValueAsRationals()
  {
    if ((this.i instanceof Rational[])) {
      return (Rational[])this.i;
    }
    return null;
  }
  
  public int getValueAsInt(int paramInt)
  {
    int[] arrayOfInt = getValueAsInts();
    if ((arrayOfInt == null) || (arrayOfInt.length < 1)) {
      return paramInt;
    }
    return arrayOfInt[0];
  }
  
  public int[] getValueAsInts()
  {
    if (this.i == null) {
      return null;
    }
    if ((this.i instanceof long[]))
    {
      long[] arrayOfLong = (long[])this.i;
      int[] arrayOfInt = new int[arrayOfLong.length];
      for (int k = 0; k < arrayOfLong.length; k++) {
        arrayOfInt[k] = ((int)arrayOfLong[k]);
      }
      return arrayOfInt;
    }
    return null;
  }
  
  public long getValueAsLong(long paramLong)
  {
    long[] arrayOfLong = getValueAsLongs();
    if ((arrayOfLong == null) || (arrayOfLong.length < 1)) {
      return paramLong;
    }
    return arrayOfLong[0];
  }
  
  public long[] getValueAsLongs()
  {
    if ((this.i instanceof long[])) {
      return (long[])this.i;
    }
    return null;
  }
  
  public Object getValue()
  {
    return this.i;
  }
  
  public long forceGetValueAsLong(long paramLong)
  {
    long[] arrayOfLong = getValueAsLongs();
    if ((arrayOfLong != null) && (arrayOfLong.length >= 1)) {
      return arrayOfLong[0];
    }
    byte[] arrayOfByte = getValueAsBytes();
    if ((arrayOfByte != null) && (arrayOfByte.length >= 1)) {
      return arrayOfByte[0];
    }
    Rational[] arrayOfRational = getValueAsRationals();
    if ((arrayOfRational != null) && (arrayOfRational.length >= 1) && (arrayOfRational[0].getDenominator() != 0L)) {
      return arrayOfRational[0].toDouble();
    }
    return paramLong;
  }
  
  protected long getValueAt(int paramInt)
  {
    if ((this.i instanceof long[])) {
      return ((long[])(long[])this.i)[paramInt];
    }
    if ((this.i instanceof byte[])) {
      return ((byte[])(byte[])this.i)[paramInt];
    }
    throw new IllegalArgumentException("Cannot get integer value from " + a(this.e));
  }
  
  private static String a(short paramShort)
  {
    switch (paramShort)
    {
    case 1: 
      return "UNSIGNED_BYTE";
    case 2: 
      return "ASCII";
    case 3: 
      return "UNSIGNED_SHORT";
    case 4: 
      return "UNSIGNED_LONG";
    case 5: 
      return "UNSIGNED_RATIONAL";
    case 7: 
      return "UNDEFINED";
    case 9: 
      return "LONG";
    case 10: 
      return "RATIONAL";
    }
    return "";
  }
  
  protected String getString()
  {
    if (this.e != 2) {
      throw new IllegalArgumentException("Cannot get ASCII value from " + a(this.e));
    }
    return new String((byte[])this.i, c);
  }
  
  protected byte[] getStringByte()
  {
    return (byte[])this.i;
  }
  
  protected Rational getRational(int paramInt)
  {
    if ((this.e != 10) && (this.e != 5)) {
      throw new IllegalArgumentException("Cannot get RATIONAL value from " + a(this.e));
    }
    return ((Rational[])(Rational[])this.i)[paramInt];
  }
  
  protected void getBytes(byte[] paramArrayOfByte)
  {
    getBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  protected void getBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if ((this.e != 7) && (this.e != 1)) {
      throw new IllegalArgumentException("Cannot get BYTE value from " + a(this.e));
    }
    System.arraycopy(this.i, 0, paramArrayOfByte, paramInt1, paramInt2 > this.g ? this.g : paramInt2);
  }
  
  protected int getOffset()
  {
    return this.j;
  }
  
  protected void setOffset(int paramInt)
  {
    this.j = paramInt;
  }
  
  protected void setHasDefinedCount(boolean paramBoolean)
  {
    this.f = paramBoolean;
  }
  
  protected boolean hasDefinedCount()
  {
    return this.f;
  }
  
  private boolean a(int[] paramArrayOfInt)
  {
    for (int n : paramArrayOfInt) {
      if ((n > 65535) || (n < 0)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean a(long[] paramArrayOfLong)
  {
    for (long l : paramArrayOfLong) {
      if ((l < 0L) || (l > 4294967295L)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean b(int[] paramArrayOfInt)
  {
    for (int n : paramArrayOfInt) {
      if (n < 0) {
        return true;
      }
    }
    return false;
  }
  
  private boolean a(Rational[] paramArrayOfRational)
  {
    for (Rational localRational : paramArrayOfRational) {
      if ((localRational.getNumerator() < 0L) || (localRational.getDenominator() < 0L) || (localRational.getNumerator() > 4294967295L) || (localRational.getDenominator() > 4294967295L)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean b(Rational[] paramArrayOfRational)
  {
    for (Rational localRational : paramArrayOfRational) {
      if ((localRational.getNumerator() < -2147483648L) || (localRational.getDenominator() < -2147483648L) || (localRational.getNumerator() > 2147483647L) || (localRational.getDenominator() > 2147483647L)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if ((paramObject instanceof ExifTag))
    {
      ExifTag localExifTag = (ExifTag)paramObject;
      if ((localExifTag.d != this.d) || (localExifTag.g != this.g) || (localExifTag.e != this.e)) {
        return false;
      }
      if (this.i != null)
      {
        if (localExifTag.i == null) {
          return false;
        }
        if ((this.i instanceof long[]))
        {
          if (!(localExifTag.i instanceof long[])) {
            return false;
          }
          return Arrays.equals((long[])this.i, (long[])localExifTag.i);
        }
        if ((this.i instanceof Rational[]))
        {
          if (!(localExifTag.i instanceof Rational[])) {
            return false;
          }
          return Arrays.equals((Rational[])this.i, (Rational[])localExifTag.i);
        }
        if ((this.i instanceof byte[]))
        {
          if (!(localExifTag.i instanceof byte[])) {
            return false;
          }
          return Arrays.equals((byte[])this.i, (byte[])localExifTag.i);
        }
        return this.i.equals(localExifTag.i);
      }
      return localExifTag.i == null;
    }
    return false;
  }
  
  public String toString()
  {
    return String.format("tag id: %04X\n", new Object[] { Short.valueOf(this.d) }) + "ifd id: " + this.h + "\ntype: " + a(this.e) + "\ncount: " + this.g + "\noffset: " + this.j + "\nvalue: " + forceGetValueAsString() + "\n";
  }
  
  public String forceGetValueAsString()
  {
    if (this.i == null) {
      return "";
    }
    if ((this.i instanceof byte[]))
    {
      if (this.e == 2) {
        return new String((byte[])this.i, c);
      }
      return Arrays.toString((byte[])this.i);
    }
    if ((this.i instanceof long[]))
    {
      if (((long[])this.i).length == 1) {
        return String.valueOf(((long[])(long[])this.i)[0]);
      }
      return Arrays.toString((long[])this.i);
    }
    if ((this.i instanceof Object[]))
    {
      if (((Object[])this.i).length == 1)
      {
        Object localObject = ((Object[])(Object[])this.i)[0];
        if (localObject == null) {
          return "";
        }
        return localObject.toString();
      }
      return Arrays.toString((Object[])this.i);
    }
    return this.i.toString();
  }
  
  static
  {
    a[1] = 1;
    a[2] = 1;
    a[3] = 2;
    a[4] = 4;
    a[5] = 8;
    a[7] = 1;
    a[9] = 4;
    a[10] = 8;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\exif\ExifTag.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */