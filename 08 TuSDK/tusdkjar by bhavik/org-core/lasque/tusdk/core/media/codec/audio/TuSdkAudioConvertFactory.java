package org.lasque.tusdk.core.media.codec.audio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import org.lasque.tusdk.core.utils.ReflectUtils;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkAudioConvertFactory
{
  public static TuSdkAudioConvert build(TuSdkAudioInfo paramTuSdkAudioInfo1, TuSdkAudioInfo paramTuSdkAudioInfo2)
  {
    TuSdkAudioConvertBase localTuSdkAudioConvertBase1 = a(paramTuSdkAudioInfo1);
    if (localTuSdkAudioConvertBase1 == null)
    {
      TLog.d("%s build unsupport inputInfo: %s", new Object[] { "TuSdkAudioConvertFactory", paramTuSdkAudioInfo1 });
      return null;
    }
    TuSdkAudioConvertBase localTuSdkAudioConvertBase2 = a(paramTuSdkAudioInfo2);
    if (localTuSdkAudioConvertBase2 == null) {
      TLog.d("%s build unsupport outputInfo: %s", new Object[] { "TuSdkAudioConvertFactory", paramTuSdkAudioInfo2 });
    } else {
      TuSdkAudioConvertBase.a(localTuSdkAudioConvertBase2, localTuSdkAudioConvertBase1, true);
    }
    return localTuSdkAudioConvertBase2;
  }
  
  public static TuSdkAudioConvert build(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    TuSdkAudioConvertBase localTuSdkAudioConvertBase = a(paramTuSdkAudioInfo);
    if (localTuSdkAudioConvertBase == null)
    {
      TLog.d("%s build unsupport inputInfo: %s", new Object[] { "TuSdkAudioConvertFactory", paramTuSdkAudioInfo });
      return null;
    }
    return localTuSdkAudioConvertBase;
  }
  
  private static TuSdkAudioConvertBase a(TuSdkAudioInfo paramTuSdkAudioInfo)
  {
    if (paramTuSdkAudioInfo == null) {
      return null;
    }
    Object localObject = null;
    switch (paramTuSdkAudioInfo.bitWidth)
    {
    case 16: 
      switch (paramTuSdkAudioInfo.channelCount)
      {
      case 1: 
        localObject = new TuSdkAudioConvertPCM16Mono();
        break;
      case 2: 
        localObject = new TuSdkAudioConvertPCM16Stereo();
      }
      break;
    case 8: 
      switch (paramTuSdkAudioInfo.channelCount)
      {
      case 1: 
        localObject = new TuSdkAudioConvertPCM8Mono();
        break;
      case 2: 
        localObject = new TuSdkAudioConvertPCM8Stereo();
      }
      break;
    }
    return (TuSdkAudioConvertBase)localObject;
  }
  
  public static abstract class TuSdkAudioConvertBase
    implements TuSdkAudioConvert
  {
    protected TuSdkAudioConvert mInputConvert;
    protected boolean mNeedRestore = false;
    
    private void a(TuSdkAudioConvert paramTuSdkAudioConvert)
    {
      a(paramTuSdkAudioConvert, false);
    }
    
    private void a(TuSdkAudioConvert paramTuSdkAudioConvert, boolean paramBoolean)
    {
      this.mInputConvert = paramTuSdkAudioConvert;
      this.mNeedRestore = paramBoolean;
      if ((paramBoolean) && ((paramTuSdkAudioConvert instanceof TuSdkAudioConvertBase)))
      {
        TuSdkAudioConvert localTuSdkAudioConvert = (TuSdkAudioConvert)ReflectUtils.classInstance(getClass());
        ((TuSdkAudioConvertBase)paramTuSdkAudioConvert).a(localTuSdkAudioConvert);
      }
    }
    
    public byte[] outputBytes(byte[] paramArrayOfByte, ByteOrder paramByteOrder, int paramInt1, int paramInt2)
    {
      byte[] arrayOfByte = new byte[paramInt2];
      System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
      return outputBytes(arrayOfByte, paramByteOrder);
    }
    
    public void inputReverse(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2)
    {
      this.mInputConvert.reverse(paramByteBuffer1, paramByteBuffer2);
    }
    
    public void restoreBytes(ShortBuffer paramShortBuffer, ByteBuffer paramByteBuffer, ByteOrder paramByteOrder)
    {
      this.mInputConvert.outputBytes(paramShortBuffer, paramByteBuffer, paramByteOrder);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\audio\TuSdkAudioConvertFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */