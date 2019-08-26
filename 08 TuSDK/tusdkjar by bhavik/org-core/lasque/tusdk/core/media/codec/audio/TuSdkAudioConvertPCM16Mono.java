package org.lasque.tusdk.core.media.codec.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import org.lasque.tusdk.core.utils.ByteUtils;
import org.lasque.tusdk.core.utils.Complex;

public class TuSdkAudioConvertPCM16Mono
  extends TuSdkAudioConvertFactory.TuSdkAudioConvertBase
{
  public byte[] toPCM8Mono(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length / 2];
    for (int i = 0; i < arrayOfByte.length; i++) {
      arrayOfByte[i] = ((byte)(ByteUtils.getShort(paramArrayOfByte[(2 * i)], paramArrayOfByte[(2 * i + 1)], paramByteOrder) / 256));
    }
    return arrayOfByte;
  }
  
  public byte[] toPCM8Stereo(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    for (int i = 0; i < paramArrayOfByte.length; i += 2) {
      arrayOfByte[(i + 1)] = (arrayOfByte[i] = (byte)(ByteUtils.getShort(paramArrayOfByte[i], paramArrayOfByte[(i + 1)], paramByteOrder) / 256));
    }
    return arrayOfByte;
  }
  
  public byte[] toPCM16Mono(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    return paramArrayOfByte;
  }
  
  public byte[] toPCM16Stereo(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length * 2];
    for (int i = 0; i < paramArrayOfByte.length; i += 2)
    {
      arrayOfByte[(i * 2)] = paramArrayOfByte[i];
      arrayOfByte[(i * 2 + 1)] = paramArrayOfByte[(i + 1)];
      arrayOfByte[(i * 2 + 2)] = paramArrayOfByte[i];
      arrayOfByte[(i * 2 + 3)] = paramArrayOfByte[(i + 1)];
    }
    return arrayOfByte;
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
    int i = Math.min(paramByteBuffer.remaining() / 2, paramShortBuffer.remaining());
    for (int j = 0; j < i; j++)
    {
      short s = ByteUtils.getShort(paramByteBuffer.get(), paramByteBuffer.get(), paramByteOrder);
      paramShortBuffer.put(s);
    }
  }
  
  public void toPCM16Stereo(ByteBuffer paramByteBuffer, ShortBuffer paramShortBuffer, ByteOrder paramByteOrder)
  {
    int i = Math.min(paramByteBuffer.remaining(), paramShortBuffer.remaining()) / 2;
    for (int j = 0; j < i; j++)
    {
      short s = ByteUtils.getShort(paramByteBuffer.get(), paramByteBuffer.get(), paramByteOrder);
      paramShortBuffer.put(s);
      paramShortBuffer.put(s);
    }
  }
  
  public void toPCM8Mono(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    int i = Math.min(paramShortBuffer.remaining(), paramByteBuffer.remaining());
    for (int j = 0; j < i; j++)
    {
      int k = paramShortBuffer.get();
      paramByteBuffer.put((byte)(k / 256));
    }
  }
  
  public void toPCM8Stereo(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    int i = Math.min(paramShortBuffer.remaining(), paramByteBuffer.remaining() / 2);
    for (int j = 0; j < i; j++)
    {
      byte b = (byte)(paramShortBuffer.get() / 256);
      paramByteBuffer.put(b);
      paramByteBuffer.put(b);
    }
  }
  
  public void toPCM16Mono(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    int i = Math.min(paramShortBuffer.remaining(), paramByteBuffer.remaining() / 2);
    for (int j = 0; j < i; j++)
    {
      short s = paramShortBuffer.get();
      byte[] arrayOfByte = ByteUtils.getBytes(s, paramByteOrder);
      paramByteBuffer.put(arrayOfByte);
    }
  }
  
  public void toPCM16Stereo(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    int i = Math.min(paramShortBuffer.remaining(), paramByteBuffer.remaining() / 4);
    for (int j = 0; j < i; j++)
    {
      short s = paramShortBuffer.get();
      byte[] arrayOfByte = ByteUtils.getBytes(s, paramByteOrder);
      paramByteBuffer.put(arrayOfByte);
      paramByteBuffer.put(arrayOfByte);
    }
  }
  
  public TuSdkAudioData toData(ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    int i = paramByteBuffer.remaining() / 2;
    TuSdkAudioData localTuSdkAudioData = new TuSdkAudioData(1, i);
    for (int j = 0; j < i; j++) {
      localTuSdkAudioData.left[j] = new Complex(ByteUtils.getShort(paramByteBuffer.get(), paramByteBuffer.get(), paramByteOrder) / 32768.0D, 0.0D);
    }
    return localTuSdkAudioData;
  }
  
  public void toBuffer(TuSdkAudioData paramTuSdkAudioData, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    for (int i = 0; i < paramTuSdkAudioData.inputLength; i++) {
      paramByteBuffer.put(ByteUtils.getBytes((short)(int)(paramTuSdkAudioData.left[i].safeRe() * 32767.0D), paramByteOrder));
    }
  }
  
  public void reverse(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2)
  {
    if ((paramByteBuffer1 == null) || (paramByteBuffer2 == null) || (paramByteBuffer2.capacity() < paramByteBuffer1.remaining())) {
      return;
    }
    paramByteBuffer2.clear();
    byte[] arrayOfByte = new byte[2];
    for (int i = paramByteBuffer1.remaining() - 2; i > -1; i -= 2)
    {
      paramByteBuffer1.position(i);
      paramByteBuffer1.get(arrayOfByte);
      paramByteBuffer2.put(arrayOfByte);
    }
  }
  
  public byte[] resample(byte[] paramArrayOfByte, float paramFloat, ByteOrder paramByteOrder)
  {
    short[] arrayOfShort = ByteUtils.getShorts(paramArrayOfByte, paramByteOrder);
    float f = arrayOfShort[0] + (arrayOfShort[1] - arrayOfShort[0]) * paramFloat;
    byte[] arrayOfByte = ByteUtils.getBytes(ByteUtils.safeShort((int)f), paramByteOrder);
    return arrayOfByte;
  }
  
  public byte[] outputResamle(byte[] paramArrayOfByte, float paramFloat, ByteOrder paramByteOrder)
  {
    byte[] arrayOfByte = this.mInputConvert.resample(paramArrayOfByte, paramFloat, paramByteOrder);
    return outputBytes(arrayOfByte, paramByteOrder);
  }
  
  public byte[] outputBytes(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    return this.mInputConvert.toPCM16Mono(paramArrayOfByte, paramByteOrder);
  }
  
  public void outputShorts(ByteBuffer paramByteBuffer, ShortBuffer paramShortBuffer, ByteOrder paramByteOrder)
  {
    this.mInputConvert.toPCM16Mono(paramByteBuffer, paramShortBuffer, paramByteOrder);
  }
  
  public void outputBytes(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    this.mInputConvert.toPCM16Mono(paramShortBuffer, paramByteBuffer, paramByteOrder);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioConvertPCM16Mono.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */