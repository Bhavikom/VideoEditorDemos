package org.lasque.tusdk.api.audio.preproc.mixer;

import org.lasque.tusdk.core.utils.TLog;

public class TuSDKAverageAudioMixer
  extends TuSDKAudioMixer
{
  public byte[] mixRawAudioBytes(byte[][] paramArrayOfByte)
  {
    if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
      return null;
    }
    byte[] arrayOfByte = paramArrayOfByte[0];
    if (paramArrayOfByte.length == 1) {
      return arrayOfByte;
    }
    for (int i = 0; i < paramArrayOfByte.length; i++) {
      if (paramArrayOfByte[i].length != arrayOfByte.length)
      {
        TLog.e("column of the road of audio + " + i + " is diffrent.", new Object[0]);
        return null;
      }
    }
    i = paramArrayOfByte.length;
    int j = arrayOfByte.length / 2;
    short[][] arrayOfShort = new short[i][j];
    int m;
    for (int k = 0; k < i; k++) {
      for (m = 0; m < j; m++) {
        arrayOfShort[k][m] = ((short)(paramArrayOfByte[k][(m * 2)] & 0xFF | (paramArrayOfByte[k][(m * 2 + 1)] & 0xFF) << 8));
      }
    }
    short[] arrayOfShort1 = new short[j];
    int n = 0;
    for (int i1 = 0; i1 < j; i1++)
    {
      m = 0;
      for (n = 0; n < i; n++) {
        m += arrayOfShort[n][i1];
      }
      arrayOfShort1[i1] = ((short)(m / i));
    }
    for (n = 0; n < j; n++)
    {
      arrayOfByte[(n * 2)] = ((byte)(arrayOfShort1[n] & 0xFF));
      arrayOfByte[(n * 2 + 1)] = ((byte)((arrayOfShort1[n] & 0xFF00) >> 8));
    }
    return arrayOfByte;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\audio\preproc\mixer\TuSDKAverageAudioMixer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */