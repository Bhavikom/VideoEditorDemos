package org.lasque.tusdk.core.utils;

import android.annotation.SuppressLint;
import java.io.UnsupportedEncodingException;

@SuppressLint({"Assert"})
public class Base64Coder
{
  public static String encodeToString(byte[] paramArrayOfByte, int paramInt)
  {
    try
    {
      return new String(encode(paramArrayOfByte, paramInt), "US-ASCII");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new AssertionError(localUnsupportedEncodingException);
    }
  }
  
  public static String encodeToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      return new String(encode(paramArrayOfByte, paramInt1, paramInt2, paramInt3), "US-ASCII");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new AssertionError(localUnsupportedEncodingException);
    }
  }
  
  public static byte[] encode(byte[] paramArrayOfByte, int paramInt)
  {
    return encode(paramArrayOfByte, 0, paramArrayOfByte.length, paramInt);
  }
  
  public static byte[] encode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    Encoder localEncoder = new Encoder(paramInt3, null);
    int i = paramInt2 / 3 * 4;
    if (localEncoder.do_padding)
    {
      if (paramInt2 % 3 > 0) {
        i += 4;
      }
    }
    else {
      switch (paramInt2 % 3)
      {
      case 0: 
        break;
      case 1: 
        i += 2;
        break;
      case 2: 
        i += 3;
      }
    }
    if ((localEncoder.do_newline) && (paramInt2 > 0)) {
      i += ((paramInt2 - 1) / 57 + 1) * (localEncoder.do_cr ? 2 : 1);
    }
    localEncoder.output = new byte[i];
    localEncoder.process(paramArrayOfByte, paramInt1, paramInt2, true);
    if ((!a) && (localEncoder.op != i)) {
      throw new AssertionError();
    }
    return localEncoder.output;
  }
  
  public static class Encoder
    extends Base64Coder.Coder
  {
    public static final int LINE_GROUPS = 19;
    private static final byte[] c = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
    private static final byte[] d = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
    private final byte[] e;
    int a;
    private int f;
    public final boolean do_padding;
    public final boolean do_newline;
    public final boolean do_cr;
    private final byte[] g;
    
    public Encoder(int paramInt, byte[] paramArrayOfByte)
    {
      this.output = paramArrayOfByte;
      this.do_padding = ((paramInt & 0x1) == 0);
      this.do_newline = ((paramInt & 0x2) == 0);
      this.do_cr = ((paramInt & 0x4) != 0);
      this.g = ((paramInt & 0x8) == 0 ? c : d);
      this.e = new byte[2];
      this.a = 0;
      this.f = (this.do_newline ? 19 : -1);
    }
    
    public int maxOutputSize(int paramInt)
    {
      return paramInt * 8 / 5 + 10;
    }
    
    public boolean process(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
    {
      byte[] arrayOfByte1 = this.g;
      byte[] arrayOfByte2 = this.output;
      int i = 0;
      int j = this.f;
      int k = paramInt1;
      paramInt2 += paramInt1;
      int m = -1;
      switch (this.a)
      {
      case 0: 
        break;
      case 1: 
        if (k + 2 <= paramInt2)
        {
          m = (this.e[0] & 0xFF) << 16 | (paramArrayOfByte[(k++)] & 0xFF) << 8 | paramArrayOfByte[(k++)] & 0xFF;
          this.a = 0;
        }
        break;
      case 2: 
        if (k + 1 <= paramInt2)
        {
          m = (this.e[0] & 0xFF) << 16 | (this.e[1] & 0xFF) << 8 | paramArrayOfByte[(k++)] & 0xFF;
          this.a = 0;
        }
        break;
      }
      if (m != -1)
      {
        arrayOfByte2[(i++)] = arrayOfByte1[(m >> 18 & 0x3F)];
        arrayOfByte2[(i++)] = arrayOfByte1[(m >> 12 & 0x3F)];
        arrayOfByte2[(i++)] = arrayOfByte1[(m >> 6 & 0x3F)];
        arrayOfByte2[(i++)] = arrayOfByte1[(m & 0x3F)];
        j--;
        if (j == 0)
        {
          if (this.do_cr) {
            arrayOfByte2[(i++)] = 13;
          }
          arrayOfByte2[(i++)] = 10;
          j = 19;
        }
      }
      while (k + 3 <= paramInt2)
      {
        m = (paramArrayOfByte[k] & 0xFF) << 16 | (paramArrayOfByte[(k + 1)] & 0xFF) << 8 | paramArrayOfByte[(k + 2)] & 0xFF;
        arrayOfByte2[i] = arrayOfByte1[(m >> 18 & 0x3F)];
        arrayOfByte2[(i + 1)] = arrayOfByte1[(m >> 12 & 0x3F)];
        arrayOfByte2[(i + 2)] = arrayOfByte1[(m >> 6 & 0x3F)];
        arrayOfByte2[(i + 3)] = arrayOfByte1[(m & 0x3F)];
        k += 3;
        i += 4;
        j--;
        if (j == 0)
        {
          if (this.do_cr) {
            arrayOfByte2[(i++)] = 13;
          }
          arrayOfByte2[(i++)] = 10;
          j = 19;
        }
      }
      if (paramBoolean)
      {
        int n;
        if (k - this.a == paramInt2 - 1)
        {
          n = 0;
          m = ((this.a > 0 ? this.e[(n++)] : paramArrayOfByte[(k++)]) & 0xFF) << 4;
          this.a -= n;
          arrayOfByte2[(i++)] = arrayOfByte1[(m >> 6 & 0x3F)];
          arrayOfByte2[(i++)] = arrayOfByte1[(m & 0x3F)];
          if (this.do_padding)
          {
            arrayOfByte2[(i++)] = 61;
            arrayOfByte2[(i++)] = 61;
          }
          if (this.do_newline)
          {
            if (this.do_cr) {
              arrayOfByte2[(i++)] = 13;
            }
            arrayOfByte2[(i++)] = 10;
          }
        }
        else if (k - this.a == paramInt2 - 2)
        {
          n = 0;
          m = ((this.a > 1 ? this.e[(n++)] : paramArrayOfByte[(k++)]) & 0xFF) << 10 | ((this.a > 0 ? this.e[(n++)] : paramArrayOfByte[(k++)]) & 0xFF) << 2;
          this.a -= n;
          arrayOfByte2[(i++)] = arrayOfByte1[(m >> 12 & 0x3F)];
          arrayOfByte2[(i++)] = arrayOfByte1[(m >> 6 & 0x3F)];
          arrayOfByte2[(i++)] = arrayOfByte1[(m & 0x3F)];
          if (this.do_padding) {
            arrayOfByte2[(i++)] = 61;
          }
          if (this.do_newline)
          {
            if (this.do_cr) {
              arrayOfByte2[(i++)] = 13;
            }
            arrayOfByte2[(i++)] = 10;
          }
        }
        else if ((this.do_newline) && (i > 0) && (j != 19))
        {
          if (this.do_cr) {
            arrayOfByte2[(i++)] = 13;
          }
          arrayOfByte2[(i++)] = 10;
        }
        if ((!b) && (this.a != 0)) {
          throw new AssertionError();
        }
        if ((!b) && (k != paramInt2)) {
          throw new AssertionError();
        }
      }
      else if (k == paramInt2 - 1)
      {
        this.e[(this.a++)] = paramArrayOfByte[k];
      }
      else if (k == paramInt2 - 2)
      {
        this.e[(this.a++)] = paramArrayOfByte[k];
        this.e[(this.a++)] = paramArrayOfByte[(k + 1)];
      }
      this.op = i;
      this.f = j;
      return true;
    }
  }
  
  public static class Decoder
    extends Base64Coder.Coder
  {
    private static final int[] a = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    private static final int[] b = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
    private int c;
    private int d;
    private final int[] e;
    
    public Decoder(int paramInt, byte[] paramArrayOfByte)
    {
      this.output = paramArrayOfByte;
      this.e = ((paramInt & 0x8) == 0 ? a : b);
      this.c = 0;
      this.d = 0;
    }
    
    public int maxOutputSize(int paramInt)
    {
      return paramInt * 3 / 4 + 10;
    }
    
    public boolean process(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
    {
      if (this.c == 6) {
        return false;
      }
      int i = paramInt1;
      paramInt2 += paramInt1;
      int j = this.c;
      int k = this.d;
      int m = 0;
      byte[] arrayOfByte = this.output;
      int[] arrayOfInt = this.e;
      while (i < paramInt2)
      {
        if (j == 0)
        {
          while ((i + 4 <= paramInt2) && ((k = arrayOfInt[(paramArrayOfByte[i] & 0xFF)] << 18 | arrayOfInt[(paramArrayOfByte[(i + 1)] & 0xFF)] << 12 | arrayOfInt[(paramArrayOfByte[(i + 2)] & 0xFF)] << 6 | arrayOfInt[(paramArrayOfByte[(i + 3)] & 0xFF)]) >= 0))
          {
            arrayOfByte[(m + 2)] = ((byte)k);
            arrayOfByte[(m + 1)] = ((byte)(k >> 8));
            arrayOfByte[m] = ((byte)(k >> 16));
            m += 3;
            i += 4;
          }
          if (i >= paramInt2) {
            break;
          }
        }
        int n = arrayOfInt[(paramArrayOfByte[(i++)] & 0xFF)];
        switch (j)
        {
        case 0: 
          if (n >= 0)
          {
            k = n;
            j++;
          }
          else if (n != -1)
          {
            this.c = 6;
            return false;
          }
          break;
        case 1: 
          if (n >= 0)
          {
            k = k << 6 | n;
            j++;
          }
          else if (n != -1)
          {
            this.c = 6;
            return false;
          }
          break;
        case 2: 
          if (n >= 0)
          {
            k = k << 6 | n;
            j++;
          }
          else if (n == -2)
          {
            arrayOfByte[(m++)] = ((byte)(k >> 4));
            j = 4;
          }
          else if (n != -1)
          {
            this.c = 6;
            return false;
          }
          break;
        case 3: 
          if (n >= 0)
          {
            k = k << 6 | n;
            arrayOfByte[(m + 2)] = ((byte)k);
            arrayOfByte[(m + 1)] = ((byte)(k >> 8));
            arrayOfByte[m] = ((byte)(k >> 16));
            m += 3;
            j = 0;
          }
          else if (n == -2)
          {
            arrayOfByte[(m + 1)] = ((byte)(k >> 2));
            arrayOfByte[m] = ((byte)(k >> 10));
            m += 2;
            j = 5;
          }
          else if (n != -1)
          {
            this.c = 6;
            return false;
          }
          break;
        case 4: 
          if (n == -2)
          {
            j++;
          }
          else if (n != -1)
          {
            this.c = 6;
            return false;
          }
          break;
        case 5: 
          if (n != -1)
          {
            this.c = 6;
            return false;
          }
          break;
        }
      }
      if (!paramBoolean)
      {
        this.c = j;
        this.d = k;
        this.op = m;
        return true;
      }
      switch (j)
      {
      case 0: 
        break;
      case 1: 
        this.c = 6;
        return false;
      case 2: 
        arrayOfByte[(m++)] = ((byte)(k >> 4));
        break;
      case 3: 
        arrayOfByte[(m++)] = ((byte)(k >> 10));
        arrayOfByte[(m++)] = ((byte)(k >> 2));
        break;
      case 4: 
        this.c = 6;
        return false;
      }
      this.c = j;
      this.op = m;
      return true;
    }
  }
  
  public static abstract class Coder
  {
    public byte[] output;
    public int op;
    
    public abstract boolean process(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean);
    
    public abstract int maxOutputSize(int paramInt);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\Base64Coder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */