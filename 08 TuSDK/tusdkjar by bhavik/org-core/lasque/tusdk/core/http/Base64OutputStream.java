package org.lasque.tusdk.core.http;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.lasque.tusdk.core.utils.Base64Coder.Coder;
import org.lasque.tusdk.core.utils.Base64Coder.Decoder;
import org.lasque.tusdk.core.utils.Base64Coder.Encoder;

public class Base64OutputStream
  extends FilterOutputStream
{
  private static final byte[] a = new byte[0];
  private final Base64Coder.Coder b;
  private final int c;
  private byte[] d = null;
  private int e = 0;
  
  public Base64OutputStream(OutputStream paramOutputStream, int paramInt)
  {
    this(paramOutputStream, paramInt, true);
  }
  
  public Base64OutputStream(OutputStream paramOutputStream, int paramInt, boolean paramBoolean)
  {
    super(paramOutputStream);
    this.c = paramInt;
    if (paramBoolean) {
      this.b = new Base64Coder.Encoder(paramInt, null);
    } else {
      this.b = new Base64Coder.Decoder(paramInt, null);
    }
  }
  
  public void write(int paramInt)
  {
    if (this.d == null) {
      this.d = new byte['Ѐ'];
    }
    if (this.e >= this.d.length)
    {
      a(this.d, 0, this.e, false);
      this.e = 0;
    }
    this.d[(this.e++)] = ((byte)paramInt);
  }
  
  private void a()
  {
    if (this.e > 0)
    {
      a(this.d, 0, this.e, false);
      this.e = 0;
    }
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt2 <= 0) {
      return;
    }
    a();
    a(paramArrayOfByte, paramInt1, paramInt2, false);
  }
  
  public void close()
  {
    Object localObject = null;
    try
    {
      a();
      a(a, 0, 0, true);
    }
    catch (IOException localIOException1)
    {
      localObject = localIOException1;
    }
    try
    {
      if ((this.c & 0x10) == 0) {
        this.out.close();
      } else {
        this.out.flush();
      }
    }
    catch (IOException localIOException2)
    {
      if (localObject != null) {
        localObject = localIOException2;
      }
    }
    if (localObject != null) {
      throw ((Throwable)localObject);
    }
  }
  
  private void a(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this.b.output = a(this.b.output, this.b.maxOutputSize(paramInt2));
    if (!this.b.process(paramArrayOfByte, paramInt1, paramInt2, paramBoolean)) {
      throw new Base64DataException("bad base-64");
    }
    this.out.write(this.b.output, 0, this.b.op);
  }
  
  private byte[] a(byte[] paramArrayOfByte, int paramInt)
  {
    if ((paramArrayOfByte == null) || (paramArrayOfByte.length < paramInt)) {
      return new byte[paramInt];
    }
    return paramArrayOfByte;
  }
  
  public static class Base64DataException
    extends IOException
  {
    public Base64DataException(String paramString)
    {
      super();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\Base64OutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */