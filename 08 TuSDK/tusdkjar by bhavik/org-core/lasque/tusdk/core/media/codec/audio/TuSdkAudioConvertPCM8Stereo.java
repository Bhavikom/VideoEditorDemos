package org.lasque.tusdk.core.media.codec.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import org.lasque.tusdk.core.utils.ByteUtils;
import org.lasque.tusdk.core.utils.Complex;

public class TuSdkAudioConvertPCM8Stereo
  extends TuSdkAudioConvertFactory.TuSdkAudioConvertBase
{
  public byte[] toPCM8Mono(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length / 2];
    for (int i = 0; i < arrayOfByte.length; i++)
    {
      int j = (short)((short)paramArrayOfByte[(2 * i)] + (short)paramArrayOfByte[(2 * i + 1)]);
      arrayOfByte[i] = ((byte)(j >> 1));
    }
    return arrayOfByte;
  }
  
  public byte[] toPCM8Stereo(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    return paramArrayOfByte;
  }
  
  public byte[] toPCM16Mono(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    byte[] arrayOfByte1 = new byte[paramArrayOfByte.length];
    for (int i = 0; i < paramArrayOfByte.length; i += 2)
    {
      int j = (short)(paramArrayOfByte[i] * 256);
      int k = (short)(paramArrayOfByte[(i + 1)] * 256);
      byte[] arrayOfByte2 = ByteUtils.getBytes((short)(j / 2 + k / 2), paramByteOrder);
      arrayOfByte1[i] = arrayOfByte2[0];
      arrayOfByte1[(i + 1)] = arrayOfByte2[1];
    }
    return arrayOfByte1;
  }
  
  public byte[] toPCM16Stereo(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    byte[] arrayOfByte1 = new byte[paramArrayOfByte.length * 2];
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      byte[] arrayOfByte2 = ByteUtils.getBytes((short)(paramArrayOfByte[i] * 256), paramByteOrder);
      arrayOfByte1[(i * 2)] = arrayOfByte2[0];
      arrayOfByte1[(i * 2 + 1)] = arrayOfByte2[1];
    }
    return arrayOfByte1;
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
      int k = (short)(paramByteBuffer.get() * 256);
      int m = (short)(paramByteBuffer.get() * 256);
      paramShortBuffer.put((short)(k / 2 + m / 2));
    }
  }
  
  public void toPCM16Stereo(ByteBuffer paramByteBuffer, ShortBuffer paramShortBuffer, ByteOrder paramByteOrder)
  {
    int i = Math.min(paramByteBuffer.remaining(), paramShortBuffer.remaining());
    for (int j = 0; j < i; j++)
    {
      short s = (short)(paramByteBuffer.get() * 256);
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
      short s = paramShortBuffer.get();
      byte[] arrayOfByte = ByteUtils.getBytes(s, paramByteOrder);
      paramByteBuffer.put(arrayOfByte);
    }
  }
  
  public TuSdkAudioData toData(ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    int i = paramByteBuffer.remaining() / 2;
    TuSdkAudioData localTuSdkAudioData = new TuSdkAudioData(2, i);
    for (int j = 0; j < i; j++)
    {
      localTuSdkAudioData.left[j] = new Complex(paramByteBuffer.get() / 128.0D, 0.0D);
      localTuSdkAudioData.right[j] = new Complex(paramByteBuffer.get() / 128.0D, 0.0D);
    }
    return localTuSdkAudioData;
  }
  
  public void toBuffer(TuSdkAudioData paramTuSdkAudioData, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    for (int i = 0; i < paramTuSdkAudioData.inputLength; i++)
    {
      paramByteBuffer.put((byte)(int)(paramTuSdkAudioData.left[i].safeRe() * 127.0D));
      paramByteBuffer.put((byte)(int)(paramTuSdkAudioData.right[i].safeRe() * 127.0D));
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
    float f1 = paramArrayOfByte[0] + (paramArrayOfByte[2] - paramArrayOfByte[0]) * paramFloat;
    float f2 = paramArrayOfByte[1] + (paramArrayOfByte[3] - paramArrayOfByte[1]) * paramFloat;
    byte[] arrayOfByte = { (byte)(int)f1, (byte)(int)f2 };
    return arrayOfByte;
  }
  
  public byte[] outputResamle(byte[] paramArrayOfByte, float paramFloat, ByteOrder paramByteOrder)
  {
    byte[] arrayOfByte = this.mInputConvert.resample(paramArrayOfByte, paramFloat, paramByteOrder);
    return outputBytes(arrayOfByte, paramByteOrder);
  }
  
  public byte[] outputBytes(byte[] paramArrayOfByte, ByteOrder paramByteOrder)
  {
    return this.mInputConvert.toPCM8Stereo(paramArrayOfByte, paramByteOrder);
  }
  
  public void outputShorts(ByteBuffer paramByteBuffer, ShortBuffer paramShortBuffer, ByteOrder paramByteOrder)
  {
    this.mInputConvert.toPCM8Stereo(paramByteBuffer, paramShortBuffer, paramByteOrder);
  }
  
  public void outputBytes(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
  {
    this.mInputConvert.toPCM8Stereo(paramShortBuffer, paramByteBuffer, paramByteOrder);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioConvertPCM8Stereo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */