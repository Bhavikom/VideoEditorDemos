package org.lasque.tusdk.core.media.codec.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public abstract interface TuSdkAudioConvert
{
  public abstract byte[] toPCM8Mono(byte[] paramArrayOfByte, ByteOrder paramByteOrder);
  
  public abstract byte[] toPCM8Stereo(byte[] paramArrayOfByte, ByteOrder paramByteOrder);
  
  public abstract byte[] toPCM16Mono(byte[] paramArrayOfByte, ByteOrder paramByteOrder);
  
  public abstract byte[] toPCM16Stereo(byte[] paramArrayOfByte, ByteOrder paramByteOrder);
  
  public abstract void toPCM8Mono(ByteBuffer paramByteBuffer, ShortBuffer paramShortBuffer, ByteOrder paramByteOrder);
  
  public abstract void toPCM8Stereo(ByteBuffer paramByteBuffer, ShortBuffer paramShortBuffer, ByteOrder paramByteOrder);
  
  public abstract void toPCM16Mono(ByteBuffer paramByteBuffer, ShortBuffer paramShortBuffer, ByteOrder paramByteOrder);
  
  public abstract void toPCM16Stereo(ByteBuffer paramByteBuffer, ShortBuffer paramShortBuffer, ByteOrder paramByteOrder);
  
  public abstract void toPCM8Mono(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder);
  
  public abstract void toPCM8Stereo(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder);
  
  public abstract void toPCM16Mono(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder);
  
  public abstract void toPCM16Stereo(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder);
  
  public abstract TuSdkAudioData toData(ByteBuffer paramByteBuffer, ByteOrder paramByteOrder);
  
  public abstract void toBuffer(TuSdkAudioData paramTuSdkAudioData, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder);
  
  public abstract void reverse(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2);
  
  public abstract byte[] resample(byte[] paramArrayOfByte, float paramFloat, ByteOrder paramByteOrder);
  
  public abstract byte[] outputResamle(byte[] paramArrayOfByte, float paramFloat, ByteOrder paramByteOrder);
  
  public abstract byte[] outputBytes(byte[] paramArrayOfByte, ByteOrder paramByteOrder);
  
  public abstract byte[] outputBytes(byte[] paramArrayOfByte, ByteOrder paramByteOrder, int paramInt1, int paramInt2);
  
  public abstract void inputReverse(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2);
  
  public abstract void outputShorts(ByteBuffer paramByteBuffer, ShortBuffer paramShortBuffer, ByteOrder paramByteOrder);
  
  public abstract void outputBytes(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder);
  
  public abstract void restoreBytes(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioConvert.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */