package org.lasque.tusdk.core.exif;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class OrderedDataOutputStream
  extends FilterOutputStream
{
  private final ByteBuffer a = ByteBuffer.allocate(4);
  
  public OrderedDataOutputStream(OutputStream paramOutputStream)
  {
    super(paramOutputStream);
  }
  
  public OrderedDataOutputStream setByteOrder(ByteOrder paramByteOrder)
  {
    this.a.order(paramByteOrder);
    return this;
  }
  
  public OrderedDataOutputStream writeShort(short paramShort)
  {
    this.a.rewind();
    this.a.putShort(paramShort);
    this.out.write(this.a.array(), 0, 2);
    return this;
  }
  
  public OrderedDataOutputStream writeRational(Rational paramRational)
  {
    writeInt((int)paramRational.getNumerator());
    writeInt((int)paramRational.getDenominator());
    return this;
  }
  
  public OrderedDataOutputStream writeInt(int paramInt)
  {
    this.a.rewind();
    this.a.putInt(paramInt);
    this.out.write(this.a.array());
    return this;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\exif\OrderedDataOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */