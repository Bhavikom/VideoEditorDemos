package org.lasque.tusdk.core.exif;

import java.io.InputStream;
import java.nio.ByteBuffer;

class ByteBufferInputStream
  extends InputStream
{
  private ByteBuffer a;
  
  public ByteBufferInputStream(ByteBuffer paramByteBuffer)
  {
    this.a = paramByteBuffer;
  }
  
  public int read()
  {
    if (!this.a.hasRemaining()) {
      return -1;
    }
    return this.a.get() & 0xFF;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (!this.a.hasRemaining()) {
      return -1;
    }
    paramInt2 = Math.min(paramInt2, this.a.remaining());
    this.a.get(paramArrayOfByte, paramInt1, paramInt2);
    return paramInt2;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\exif\ByteBufferInputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */