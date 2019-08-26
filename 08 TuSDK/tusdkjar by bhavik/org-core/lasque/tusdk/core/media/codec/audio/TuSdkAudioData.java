package org.lasque.tusdk.core.media.codec.audio;

import org.lasque.tusdk.core.utils.Complex;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.calc.FFT;

public class TuSdkAudioData
{
  public final Complex[] left;
  public final Complex[] right;
  public final int length;
  public final int channels;
  public final int inputLength;
  
  public TuSdkAudioData(int paramInt1, int paramInt2)
  {
    double d = Math.log(paramInt2) / Math.log(2.0D);
    this.length = ((int)Math.pow(2.0D, Math.ceil(d)));
    this.inputLength = paramInt2;
    this.channels = paramInt1;
    this.left = new Complex[paramInt2];
    for (int i = paramInt2; i < this.length; i++) {
      this.left[i] = new Complex(0.0D, 0.0D);
    }
    if (this.channels > 1)
    {
      this.right = new Complex[paramInt2];
      for (i = paramInt2; i < this.length; i++) {
        this.right[i] = new Complex(0.0D, 0.0D);
      }
    }
    else
    {
      this.right = null;
    }
  }
  
  public void gainWithArgs(TuSdkAudioGainData paramTuSdkAudioGainData)
  {
    if (paramTuSdkAudioGainData == null)
    {
      TLog.w("%s gain need gainData", new Object[] { "TuSdkAudioData" });
      return;
    }
    if (this.channels > 1)
    {
      arrayOfComplex1 = FFT.fft(this.left);
      Complex[] arrayOfComplex2 = FFT.fft(this.right);
      paramTuSdkAudioGainData.gainWithData(arrayOfComplex1, arrayOfComplex2);
      arrayOfComplex1 = FFT.ifft(arrayOfComplex1);
      arrayOfComplex2 = FFT.ifft(arrayOfComplex2);
      System.arraycopy(arrayOfComplex1, 0, this.left, 0, this.length);
      System.arraycopy(arrayOfComplex2, 0, this.right, 0, this.length);
      return;
    }
    Complex[] arrayOfComplex1 = FFT.fft(this.left);
    paramTuSdkAudioGainData.gainWithData(arrayOfComplex1);
    arrayOfComplex1 = FFT.ifft(arrayOfComplex1);
    System.arraycopy(arrayOfComplex1, 0, this.left, 0, this.length);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */