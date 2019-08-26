package org.lasque.tusdk.core.media.codec.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import org.lasque.tusdk.core.utils.ByteUtils;
import org.lasque.tusdk.core.utils.Complex;

public class TuSdkAudioConvertPCM16Stereo
  extends TuSdkAudioConvertFactory.TuSdkAudioConvertBase
{
  public byte[] toPCM8Mono(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length / 4];
    for (int i = 0; i < arrayOfByte.length; i++)
    {
      int j = ByteUtils.getShort(paramArrayOfByte[(i * 4)], paramArrayOfByte[(i * 4 + 1)], paramByteOrder);
      int k = ByteUtils.getShort(paramArrayOfByte[(i * 4 + 2)], paramArrayOfByte[(i * 4 + 3)], paramByteOrder);
      arrayOfByte[i] = ((byte)((j / 2 + k / 2) / 256));
    }
    return arrayOfByte;
  }
  
  public byte[] toPCM8Stereo(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length / 2];
    for (int i = 0; i < arrayOfByte.length; i++) {
      arrayOfByte[i] = ((byte)(ByteUtils.getShort(paramArrayOfByte[(i * 2)], paramArrayOfByte[(i * 2 + 1)], paramByteOrder) / 256));
    }
    return arrayOfByte;
  }
  
  public byte[] toPCM16Mono(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    byte[] arrayOfByte1 = new byte[paramArrayOfByte.length / 2];
    for (int i = 0; i < arrayOfByte1.length; i += 2)
    {
      int j = ByteUtils.getShort(paramArrayOfByte[(i * 2)], paramArrayOfByte[(i * 2 + 1)], paramByteOrder);
      int k = ByteUtils.getShort(paramArrayOfByte[(i * 2 + 2)], paramArrayOfByte[(i * 2 + 3)], paramByteOrder);
      byte[] arrayOfByte2 = ByteUtils.getBytes((short)(j / 2 + k / 2), paramByteOrder);
      arrayOfByte1[i] = arrayOfByte2[0];
      arrayOfByte1[(i + 1)] = arrayOfByte2[1];
    }
    return arrayOfByte1;
  }
  
  public byte[] toPCM16Stereo(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    return paramArrayOfByte;
  }
  
  public void toPCM8Mono(ByteBuffer paramByteBuffer, ShortBuffer paramShortBuffer, ByteOrder paramByteOrder)
  {
    toPCM16Mono(paramByteBuffer, paramShortBuffer, paramByteOrder);
  }
  
  public void toPCM8Stereo(ByteBuffer paramByteBuffer, ShortBuffer paramShortBuffer, ByteOrder paramByteOrder)
  {
    toPCM16Stereo(paramByteBuffer, paramShortBuffer, paramByteOrder);
  }
  
  public void toPCM16Mono(ByteBuffer paramByteBuffer, ShortBuffer paramShortBuffer, ByteOrder paramByteOrder)
  {
    int i = Math.min(paramByteBuffer.remaining() / 4, paramShortBuffer.remaining());
    for (int j = 0; j < i; j++)
    {
      int k = ByteUtils.getShort(paramByteBuffer.get(), paramByteBuffer.get(), paramByteOrder);
      int m = ByteUtils.getShort(paramByteBuffer.get(), paramByteBuffer.get(), paramByteOrder);
      short s = (short)(k / 2 + m / 2);
      paramShortBuffer.put(s);
    }
  }
  
  public void toPCM16Stereo(ByteBuffer paramByteBuffer, ShortBuffer paramShortBuffer, ByteOrder paramByteOrder)
  {
    int i = Math.min(paramByteBuffer.remaining() / 2, paramShortBuffer.remaining());
    for (int j = 0; j < i; j++)
    {
      short s = ByteUtils.getShort(paramByteBuffer.get(), paramByteBuffer.get(), paramByteOrder);
      paramShortBuffer.put(s);
    }
  }
  
  public void toPCM8Mono(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    int i = Math.min(paramShortBuffer.remaining() / 2, paramByteBuffer.remaining());
    for (int j = 0; j < i; j++)
    {
      int k = paramShortBuffer.get();
      int m = paramShortBuffer.get();
      int n = (short)(k / 2 + m / 2);
      paramByteBuffer.put((byte)(n / 256));
    }
  }
  
  public void toPCM8Stereo(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    int i = Math.min(paramShortBuffer.remaining(), paramByteBuffer.remaining());
    for (int j = 0; j < i; j++)
    {
      int k = paramShortBuffer.get();
      paramByteBuffer.put((byte)(k / 256));
    }
  }
  
  public void toPCM16Mono(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    int i = Math.min(paramShortBuffer.remaining(), paramByteBuffer.remaining()) / 2;
    for (int j = 0; j < i; j++)
    {
      int k = paramShortBuffer.get();
      int m = paramShortBuffer.get();
      short s = (short)(k / 2 + m / 2);
      byte[] arrayOfByte = ByteUtils.getBytes(s, paramByteOrder);
      paramByteBuffer.put(arrayOfByte);
    }
  }
  
  public void toPCM16Stereo(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    int i = Math.min(paramShortBuffer.remaining(), paramByteBuffer.remaining() / 2);
    for (int j = 0; j < i; j++)
    {
      byte[] arrayOfByte = ByteUtils.getBytes(paramShortBuffer.get(), paramByteOrder);
      paramByteBuffer.put(arrayOfByte);
    }
  }
  
  public TuSdkAudioData toData(ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    int i = paramByteBuffer.remaining() / 4;
    TuSdkAudioData localTuSdkAudioData = new TuSdkAudioData(2, i);
    for (int j = 0; j < i; j++)
    {
      localTuSdkAudioData.left[j] = new Complex(ByteUtils.getShort(paramByteBuffer.get(), paramByteBuffer.get(), paramByteOrder) / 32768.0D, 0.0D);
      localTuSdkAudioData.right[j] = new Complex(ByteUtils.getShort(paramByteBuffer.get(), paramByteBuffer.get(), paramByteOrder) / 32768.0D, 0.0D);
    }
    return localTuSdkAudioData;
  }
  
  public void toBuffer(TuSdkAudioData paramTuSdkAudioData, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    for (int i = 0; i < paramTuSdkAudioData.inputLength; i++)
    {
      paramByteBuffer.put(ByteUtils.getBytes((short)(int)(paramTuSdkAudioData.left[i].safeRe() * 32767.0D), paramByteOrder));
      paramByteBuffer.put(ByteUtils.getBytes((short)(int)(paramTuSdkAudioData.right[i].safeRe() * 32767.0D), paramByteOrder));
    }
  }
  
  public void reverse(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2)
  {
    if ((paramByteBuffer1 == null) || (paramByteBuffer2 == null) || (paramByteBuffer2.capacity() < paramByteBuffer1.remaining())) {
      return;
    }
    paramByteBuffer2.clear();
    byte[] arrayOfByte = new byte[4];
    for (int i = paramByteBuffer1.remaining() - 4; i > -1; i -= 4)
    {
      paramByteBuffer1.position(i);
      paramByteBuffer1.get(arrayOfByte);
      paramByteBuffer2.put(arrayOfByte);
    }
  }
  
  public byte[] resample(byte[] paramArrayOfByte, float paramFloat, ByteOrder paramByteOrder)
  {
    short[] arrayOfShort = ByteUtils.getShorts(paramArrayOfByte, paramByteOrder);
    float f1 = arrayOfShort[0] + (arrayOfShort[2] - arrayOfShort[0]) * paramFloat;
    float f2 = arrayOfShort[1] + (arrayOfShort[3] - arrayOfShort[1]) * paramFloat;
    byte[] arrayOfByte = ByteUtils.getBytes(ByteUtils.safeShorts(new int[] { (int)f1, (int)f2 }), paramByteOrder);
    return arrayOfByte;
  }
  
  public byte[] outputResamle(byte[] paramArrayOfByte, float paramFloat, ByteOrder paramByteOrder)
  {
    byte[] arrayOfByte = this.mInputConvert.resample(paramArrayOfByte, paramFloat, paramByteOrder);
    return outputBytes(arrayOfByte, paramByteOrder);
  }
  
  public byte[] outputBytes(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    return this.mInputConvert.toPCM16Stereo(paramArrayOfByte, paramByteOrder);
  }
  
  public void outputShorts(ByteBuffer paramByteBuffer, ShortBuffer paramShortBuffer, ByteOrder paramByteOrder)
  {
    this.mInputConvert.toPCM16Stereo(paramByteBuffer, paramShortBuffer, paramByteOrder);
  }
  
  public void outputBytes(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    this.mInputConvert.toPCM16Stereo(paramShortBuffer, paramByteBuffer, paramByteOrder);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioConvertPCM16Stereo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */