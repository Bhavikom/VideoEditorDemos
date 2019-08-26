package org.lasque.tusdk.core.media.codec.audio;

import java.nio.ShortBuffer;
import java.util.Arrays;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkAudioStretch
{
  private static final TuSdkAudioStretch a = new TuSdkAudioStretch();
  private int b;
  private float c;
  private int d;
  private int e;
  private short[] f;
  private short[] g;
  private int h;
  private int i;
  private float[] j;
  private int k = 1;
  
  public static boolean process(ShortBuffer paramShortBuffer1, ShortBuffer paramShortBuffer2, int paramInt, float paramFloat)
  {
    return a.a(paramShortBuffer1, paramShortBuffer2, paramInt, paramFloat);
  }
  
  private boolean a(ShortBuffer paramShortBuffer1, ShortBuffer paramShortBuffer2, int paramInt, float paramFloat)
  {
    if ((paramShortBuffer1 == null) || (paramShortBuffer1.limit() < 1) || (paramShortBuffer2 == null) || (paramShortBuffer2.capacity() < 1) || (paramInt < 1) || (paramFloat <= 0.0F))
    {
      TLog.e("%s process invalid params: input[%s], output[%s], sampleRate[%d], speedRatio[%f]", new Object[] { "TuSdkAudioStretch", paramShortBuffer1, paramShortBuffer2, Integer.valueOf(paramInt), Float.valueOf(paramFloat) });
      return false;
    }
    paramShortBuffer2.clear();
    if (paramFloat == 1.0F)
    {
      if (paramShortBuffer1.limit() < paramShortBuffer2.limit()) {
        paramShortBuffer2.limit(paramShortBuffer1.limit());
      }
      paramShortBuffer1.put(paramShortBuffer2);
      return true;
    }
    if (!b(paramShortBuffer1, paramShortBuffer2, paramInt, paramFloat))
    {
      b();
      TLog.w("%s process prepare failed", new Object[] { "TuSdkAudioStretch" });
      return false;
    }
    int m = this.i / this.e;
    a(0, 0, 3);
    int i2;
    int i1;
    int n = i1 = i2 = 0;
    while ((n < m) && (i1 < this.i - this.d))
    {
      int i3 = (int)(i1 * this.c);
      i5 = a(i2, i3);
      int i4 = a(i5, i1, 1);
      if (i4 < this.d) {
        break;
      }
      i2 = i5 + this.e;
      n++;
      i1 += this.e;
    }
    int i5 = this.i - i1;
    a(this.h - i5, i1, 2);
    c(this.h - (i5 - this.e), i1 + this.e);
    paramShortBuffer2.put(this.g, 0, this.i);
    paramShortBuffer2.flip();
    return true;
  }
  
  private void a()
  {
    this.b = 0;
    this.c = -1.0F;
    this.d = (this.e = 0);
    this.f = (this.g = null);
    this.j = null;
    this.k = 5;
  }
  
  private void b()
  {
    if (this.j != null) {
      this.j = null;
    }
    a();
  }
  
  private void c()
  {
    if ((this.f == null) || (this.f.length < this.h)) {
      this.f = new short[this.h];
    }
    if ((this.g == null) || (this.g.length < this.i)) {
      this.g = new short[this.i];
    } else {
      Arrays.fill(this.g, (short)0);
    }
  }
  
  private float[] a(int paramInt)
  {
    float[] arrayOfFloat = new float[paramInt];
    float f1 = (float)(6.283185307179586D / paramInt);
    for (int m = 0; m < paramInt; m++) {
      arrayOfFloat[m] = (0.5F * (1.0F - (float)Math.cos(m * f1)));
    }
    return arrayOfFloat;
  }
  
  private boolean b(ShortBuffer paramShortBuffer1, ShortBuffer paramShortBuffer2, int paramInt, float paramFloat)
  {
    this.i = ((int)Math.ceil(paramShortBuffer1.limit() / paramFloat));
    if (this.i % 2 != 0) {
      this.i += 1;
    }
    if (paramShortBuffer2.capacity() < this.i)
    {
      TLog.e("%s process output buffer length too small: need[%d], out: %s", new Object[] { "TuSdkAudioStretch", Integer.valueOf(this.i), paramShortBuffer2 });
      return false;
    }
    if (paramInt != this.b)
    {
      this.d = (20 * paramInt / 1000);
      this.e = (this.d / 2);
      this.d = (this.e * 2);
      this.j = a(this.d);
      this.b = paramInt;
    }
    this.c = paramFloat;
    this.k = 1;
    this.h = paramShortBuffer1.limit();
    c();
    paramShortBuffer1.get(this.f, 0, this.h);
    return true;
  }
  
  private int a(int paramInt1, int paramInt2, int paramInt3)
  {
    int m = paramInt3 == 1 ? this.d : this.e;
    int n = this.h - paramInt1;
    int i1 = this.i - paramInt2;
    if (m > n) {
      m = n;
    }
    if (m > i1) {
      m = i1;
    }
    if (m == 0) {
      return 0;
    }
    float[] arrayOfFloat = paramInt3 == 3 ? Arrays.copyOfRange(this.j, this.e, this.j.length) : this.j;
    for (int i2 = 0; i2 < m; i2++)
    {
      float f1 = arrayOfFloat[i2];
      int tmp118_117 = (paramInt2 + i2);
      short[] tmp118_111 = this.g;
      tmp118_111[tmp118_117] = ((short)(tmp118_111[tmp118_117] + (short)(int)(this.f[(paramInt1 + i2)] * f1)));
    }
    return m;
  }
  
  private int a(int paramInt1, int paramInt2)
  {
    if (paramInt1 > this.h - this.d) {
      return paramInt2;
    }
    int m = paramInt2 - this.e;
    if (m < 0) {
      m = 0;
    }
    int n = paramInt2 + this.e;
    if (n > this.h - this.d) {
      n = this.h - this.d;
    }
    if (m >= n) {
      return paramInt2;
    }
    int i2 = m;
    int i1 = paramInt2;
    float f2 = -1.0F;
    while (i2 < n)
    {
      float f1 = b(paramInt1, i2);
      if (f1 > f2)
      {
        f2 = f1;
        i1 = i2;
      }
      i2++;
    }
    return i1;
  }
  
  private float b(int paramInt1, int paramInt2)
  {
    int n = paramInt1;
    int i1 = paramInt2;
    float f1 = 0.0F;
    int m = 0;
    while (m < this.d)
    {
      f1 += this.f[n] * this.f[i1];
      m += this.k;
      n += this.k;
      i1 += this.k;
    }
    return f1;
  }
  
  private int c(int paramInt1, int paramInt2)
  {
    int m = this.h - paramInt1;
    int n = this.i - paramInt2;
    int i1 = m;
    if (i1 > n) {
      i1 = n;
    }
    if (i1 == 0) {
      return 0;
    }
    for (int i2 = 0; i2 < i1; i2++) {
      this.g[(paramInt2 + i2)] = this.f[(paramInt1 + i2)];
    }
    return i1;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioStretch.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */