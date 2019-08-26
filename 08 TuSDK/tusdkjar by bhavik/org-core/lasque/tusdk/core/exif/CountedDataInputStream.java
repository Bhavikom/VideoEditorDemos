package org.lasque.tusdk.core.exif;

import android.annotation.SuppressLint;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

class CountedDataInputStream
  extends FilterInputStream
{
  private final byte[] b = new byte[8];
  private final ByteBuffer c = ByteBuffer.wrap(this.b);
  private int d = 0;
  private int e = 0;
  
  protected CountedDataInputStream(InputStream paramInputStream)
  {
    super(paramInputStream);
  }
  
  public void setEnd(int paramInt)
  {
    this.e = paramInt;
  }
  
  public int getEnd()
  {
    return this.e;
  }
  
  public int getReadByteCount()
  {
    return this.d;
  }
  
  public int read(byte[] paramArrayOfByte)
  {
    int i = this.in.read(paramArrayOfByte);
    this.d += (i >= 0 ? i : 0);
    return i;
  }
  
  public int read()
  {
    int i = this.in.read();
    this.d += (i >= 0 ? 1 : 0);
    return i;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
    this.d += (i >= 0 ? i : 0);
    return i;
  }
  
  public long skip(long paramLong)
  {
    long l = this.in.skip(paramLong);
    this.d = ((int)(this.d + l));
    return l;
  }
  
  @SuppressLint({"Assert"})
  public void skipTo(long paramLong)
  {
    long l1 = this.d;
    long l2 = paramLong - l1;
    if ((!a) && (l2 < 0L)) {
      throw new AssertionError();
    }
    skipOrThrow(l2);
  }
  
  public void skipOrThrow(long paramLong)
  {
    if (skip(paramLong) != paramLong) {
      throw new EOFException();
    }
  }
  
  public ByteOrder getByteOrder()
  {
    return this.c.order();
  }
  
  public void setByteOrder(ByteOrder paramByteOrder)
  {
    this.c.order(paramByteOrder);
  }
  
  public int readUnsignedShort()
  {
    return readShort() & 0xFFFF;
  }
  
  public short readShort()
  {
    readOrThrow(this.b, 0, 2);
    this.c.rewind();
    return this.c.getShort();
  }
  
  public byte readByte()
  {
    readOrThrow(this.b, 0, 1);
    this.c.rewind();
    return this.c.get();
  }
  
  public int readUnsignedByte()
  {
    readOrThrow(this.b, 0, 1);
    this.c.rewind();
    return this.c.get() & 0xFF;
  }
  
  public void readOrThrow(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = read(paramArrayOfByte, paramInt1, paramInt2);
    if (i != paramInt2) {
      throw new EOFException();
    }
  }
  
  public long readUnsignedInt()
  {
    return readInt() & 0xFFFFFFFF;
  }
  
  public int readInt()
  {
    readOrThrow(this.b, 0, 4);
    this.c.rewind();
    return this.c.getInt();
  }
  
  public long readLong()
  {
    readOrThrow(this.b, 0, 8);
    this.c.rewind();
    return this.c.getLong();
  }
  
  public String readString(int paramInt)
  {
    byte[] arrayOfByte = new byte[paramInt];
    readOrThrow(arrayOfByte);
    return new String(arrayOfByte, "UTF8");
  }
  
  public void readOrThrow(byte[] paramArrayOfByte)
  {
    readOrThrow(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public String readString(int paramInt, Charset paramCharset)
  {
    byte[] arrayOfByte = new byte[paramInt];
    readOrThrow(arrayOfByte);
    return new String(arrayOfByte, paramCharset);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\exif\CountedDataInputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */