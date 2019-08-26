package org.lasque.tusdk.core.media.codec.audio;

import org.lasque.tusdk.core.utils.Complex;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.calc.FFT;

public class TuSdkAudioGainData
{
  public static final double AUDIO_GAIN_RE_WEIGHT = 0.75D;
  public static final double AUDIO_GAIN_IM_WEIGHT = 0.25D;
  public final Complex[] gain;
  public final int length;
  
  public TuSdkAudioGainData(int paramInt, float paramFloat)
  {
    double d1 = Math.log(paramInt) / Math.log(2.0D);
    this.length = ((int)Math.pow(2.0D, Math.ceil(d1)));
    this.gain = new Complex[paramInt];
    double d2 = paramFloat < 1.0F ? -1.0F / paramFloat * 2.0F : paramFloat * 2.0F;
    double d3 = Math.pow(10.0D, d2 / 20.0D);
    int i = 0;
    int j = this.length / 2;
    int k = this.length - 1;
    while (i < j)
    {
      this.gain[i] = new Complex(d3 * 0.75D, d3 * 0.25D);
      this.gain[(k - i)] = new Complex(this.gain[i].re(), this.gain[i].im());
      i++;
    }
    Complex[] arrayOfComplex1 = FFT.ifft(this.gain);
    for (Complex localComplex : arrayOfComplex1) {
      localComplex.setImZero();
    }
    arrayOfComplex1 = FFT.fft(arrayOfComplex1);
    System.arraycopy(arrayOfComplex1, 0, this.gain, 0, this.length);
  }
  
  public void gainWithData(Complex[] paramArrayOfComplex)
  {
    if (paramArrayOfComplex == null)
    {
      TLog.w("%s gainWithData need %d datas", new Object[] { "TuSdkAudioGainData", Integer.valueOf(this.length) });
      return;
    }
    int i = Math.min(paramArrayOfComplex.length, this.length);
    int j = 0;
    int k = i / 2;
    int m = i - 1;
    while (j < k)
    {
      paramArrayOfComplex[j] = this.gain[j].times(paramArrayOfComplex[j]);
      paramArrayOfComplex[(m - j)] = this.gain[(m - j)].times(paramArrayOfComplex[(m - j)]);
      j++;
    }
  }
  
  public void gainWithData(Complex[] paramArrayOfComplex1, Complex[] paramArrayOfComplex2)
  {
    if ((paramArrayOfComplex1 == null) || (paramArrayOfComplex2 == null))
    {
      TLog.w("%s gainWithData need %d datas", new Object[] { "TuSdkAudioGainData", Integer.valueOf(this.length) });
      return;
    }
    int i = Math.min(Math.min(paramArrayOfComplex1.length, paramArrayOfComplex2.length), this.length);
    int j = 0;
    int k = i / 2;
    int m = i - 1;
    while (j < k)
    {
      paramArrayOfComplex1[j] = this.gain[j].times(paramArrayOfComplex1[j]);
      paramArrayOfComplex1[(m - j)] = this.gain[(m - j)].times(paramArrayOfComplex1[(m - j)]);
      paramArrayOfComplex2[j] = this.gain[j].times(paramArrayOfComplex2[j]);
      paramArrayOfComplex2[(m - j)] = this.gain[(m - j)].times(paramArrayOfComplex2[(m - j)]);
      j++;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioGainData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */