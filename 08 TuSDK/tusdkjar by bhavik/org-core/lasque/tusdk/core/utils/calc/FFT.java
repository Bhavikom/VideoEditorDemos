package org.lasque.tusdk.core.utils.calc;

import org.lasque.tusdk.core.utils.Complex;

public class FFT
{
  public static Complex[] fft(Complex[] paramArrayOfComplex)
  {
    int i = paramArrayOfComplex.length;
    if (i == 1) {
      return new Complex[] { paramArrayOfComplex[0] };
    }
    if (i % 2 != 0) {
      throw new IllegalArgumentException("n is not a power of 2");
    }
    Complex[] arrayOfComplex1 = new Complex[i / 2];
    Complex[] arrayOfComplex2 = new Complex[i / 2];
    for (int j = 0; j < i / 2; j++)
    {
      arrayOfComplex1[j] = paramArrayOfComplex[(2 * j)];
      arrayOfComplex2[j] = paramArrayOfComplex[(2 * j + 1)];
    }
    Complex[] arrayOfComplex3 = fft(arrayOfComplex1);
    Complex[] arrayOfComplex4 = fft(arrayOfComplex2);
    Complex[] arrayOfComplex5 = new Complex[i];
    for (int k = 0; k < i / 2; k++)
    {
      double d = -2 * k * 3.141592653589793D / i;
      Complex localComplex = new Complex(Math.cos(d), Math.sin(d));
      arrayOfComplex5[k] = arrayOfComplex3[k].plus(localComplex.times(arrayOfComplex4[k]));
      arrayOfComplex5[(k + i / 2)] = arrayOfComplex3[k].minus(localComplex.times(arrayOfComplex4[k]));
    }
    return arrayOfComplex5;
  }
  
  public static Complex[] ifft(Complex[] paramArrayOfComplex)
  {
    int i = paramArrayOfComplex.length;
    Complex[] arrayOfComplex = new Complex[i];
    for (int j = 0; j < i; j++) {
      arrayOfComplex[j] = paramArrayOfComplex[j].conjugate();
    }
    arrayOfComplex = fft(arrayOfComplex);
    for (j = 0; j < i; j++) {
      arrayOfComplex[j] = arrayOfComplex[j].conjugateScale(1.0D / i);
    }
    return arrayOfComplex;
  }
  
  public static Complex[] cconvolve(Complex[] paramArrayOfComplex1, Complex[] paramArrayOfComplex2)
  {
    if (paramArrayOfComplex1.length != paramArrayOfComplex2.length) {
      throw new IllegalArgumentException("Dimensions don't agree");
    }
    int i = paramArrayOfComplex1.length;
    Complex[] arrayOfComplex1 = fft(paramArrayOfComplex1);
    Complex[] arrayOfComplex2 = fft(paramArrayOfComplex2);
    Complex[] arrayOfComplex3 = new Complex[i];
    for (int j = 0; j < i; j++) {
      arrayOfComplex3[j] = arrayOfComplex1[j].times(arrayOfComplex2[j]);
    }
    return ifft(arrayOfComplex3);
  }
  
  public static Complex[] convolve(Complex[] paramArrayOfComplex1, Complex[] paramArrayOfComplex2)
  {
    Complex localComplex = new Complex(0.0D, 0.0D);
    Complex[] arrayOfComplex1 = new Complex[2 * paramArrayOfComplex1.length];
    for (int i = 0; i < paramArrayOfComplex1.length; i++) {
      arrayOfComplex1[i] = paramArrayOfComplex1[i];
    }
    for (i = paramArrayOfComplex1.length; i < 2 * paramArrayOfComplex1.length; i++) {
      arrayOfComplex1[i] = localComplex;
    }
    Complex[] arrayOfComplex2 = new Complex[2 * paramArrayOfComplex2.length];
    for (int j = 0; j < paramArrayOfComplex2.length; j++) {
      arrayOfComplex2[j] = paramArrayOfComplex2[j];
    }
    for (j = paramArrayOfComplex2.length; j < 2 * paramArrayOfComplex2.length; j++) {
      arrayOfComplex2[j] = localComplex;
    }
    return cconvolve(arrayOfComplex1, arrayOfComplex2);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\calc\FFT.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */