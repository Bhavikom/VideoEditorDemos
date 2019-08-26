package org.lasque.tusdk.core.secret;

import android.graphics.Bitmap;
import java.io.OutputStream;
import java.nio.Buffer;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;

public class TuSdkImageNative
{
  private static final boolean a = SdkValid.isInit;
  
  private static int a(int paramInt)
  {
    if (paramInt < 1) {
      paramInt = 100;
    }
    paramInt = Math.max(Math.min(paramInt, 100), 1);
    return paramInt;
  }
  
  public static boolean imageCompress(Bitmap paramBitmap, OutputStream paramOutputStream)
  {
    return imageCompress(paramBitmap, paramOutputStream, 95);
  }
  
  public static boolean imageCompress(Bitmap paramBitmap, OutputStream paramOutputStream, int paramInt)
  {
    return imageCompress(paramBitmap, paramOutputStream, paramInt, true);
  }
  
  public static boolean imageCompress(Bitmap paramBitmap, OutputStream paramOutputStream, int paramInt, boolean paramBoolean)
  {
    if ((paramBitmap == null) || (paramBitmap.isRecycled()) || (paramOutputStream == null) || (!TuSdkGPU.isSupporTurbo())) {
      return false;
    }
    String str = compressBitmap2JNI(paramBitmap, paramOutputStream, a(paramInt), paramBoolean, new byte['က']);
    if ("1".equalsIgnoreCase(str)) {
      return true;
    }
    TLog.e("saveImage: %s | %s", new Object[] { paramBitmap.getConfig(), str });
    return false;
  }
  
  public static int getClipHistList(Bitmap paramBitmap, int paramInt1, int paramInt2, float paramFloat, byte[] paramArrayOfByte)
  {
    return getBitmapClipHistListJNI(paramBitmap, paramInt1, paramInt2, paramFloat, paramArrayOfByte);
  }
  
  public static int getYUVHistgrameRange(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, float[] paramArrayOfFloat)
  {
    return getYUVHistgrameRangeJNI(paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramArrayOfFloat);
  }
  
  public static void glReadPixels(int paramInt1, int paramInt2)
  {
    glReadPixelsJNI(paramInt1, paramInt2);
  }
  
  public static void copyBuffer(Buffer paramBuffer1, int paramInt1, int paramInt2, Buffer paramBuffer2)
  {
    if ((paramBuffer1 == null) || (paramBuffer2 == null) || (paramInt1 < 4) || (paramInt2 < 4)) {
      return;
    }
    copyBufferJNI(paramBuffer1, paramInt1, paramInt2, paramBuffer2);
  }
  
  private static native String compressBitmap2JNI(Bitmap paramBitmap, OutputStream paramOutputStream, int paramInt, boolean paramBoolean, byte[] paramArrayOfByte);
  
  private static native int getBitmapClipHistListJNI(Bitmap paramBitmap, int paramInt1, int paramInt2, float paramFloat, byte[] paramArrayOfByte);
  
  private static native int getYUVHistgrameRangeJNI(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, float[] paramArrayOfFloat);
  
  private static native void glReadPixelsJNI(int paramInt1, int paramInt2);
  
  private static native void copyBufferJNI(Buffer paramBuffer1, int paramInt1, int paramInt2, Buffer paramBuffer2);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\secret\TuSdkImageNative.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */