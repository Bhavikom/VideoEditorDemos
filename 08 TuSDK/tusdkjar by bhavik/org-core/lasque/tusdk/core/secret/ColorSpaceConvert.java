package org.lasque.tusdk.core.secret;

public class ColorSpaceConvert
{
  private static final boolean a = SdkValid.isInit;
  
  public static void nv21ToRgba(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    yuvNv21ToRgbaJNI(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfInt);
  }
  
  public static void rgbaToNv21(int[] paramArrayOfInt, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    yuvRgbaToNv21JNI(paramArrayOfInt, paramInt1, paramInt2, paramArrayOfByte);
  }
  
  public static void rgbaToYv12(int[] paramArrayOfInt, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    yuvRgbaToYv12JNI(paramArrayOfInt, paramInt1, paramInt2, paramArrayOfByte);
  }
  
  public static void rgbaToI420(int[] paramArrayOfInt, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    yuvRgbaToI420JNI(paramArrayOfInt, paramInt1, paramInt2, paramArrayOfByte);
  }
  
  public static void nv21ToYuv420sp(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
  {
    yuvNv21ToNv12JNI(paramArrayOfByte1, paramArrayOfByte2, paramInt);
  }
  
  public static void nv21TOYuv420p(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
  {
    yuvNv21ToI420JNI(paramArrayOfByte1, paramArrayOfByte2, paramInt);
  }
  
  public static void nv21Transform(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, int paramInt3)
  {
    yuvNV21TransformJNI(paramArrayOfByte1, paramArrayOfByte2, paramInt1, paramInt2, paramInt3);
  }
  
  public static void copyAndFlipGrayData(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, int paramInt3)
  {
    yuvSinglePlaneCopyFlipJNI(paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramInt3);
  }
  
  private static native void yuvNv21ToRgbaJNI(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt);
  
  private static native void yuvRgbaToNv21JNI(int[] paramArrayOfInt, int paramInt1, int paramInt2, byte[] paramArrayOfByte);
  
  private static native void yuvRgbaToYv12JNI(int[] paramArrayOfInt, int paramInt1, int paramInt2, byte[] paramArrayOfByte);
  
  private static native void yuvRgbaToI420JNI(int[] paramArrayOfInt, int paramInt1, int paramInt2, byte[] paramArrayOfByte);
  
  private static native void yuvNv21ToNv12JNI(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt);
  
  private static native void yuvNv21ToI420JNI(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt);
  
  private static native void yuvNV21TransformJNI(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, int paramInt3);
  
  private static native void yuvSinglePlaneCopyFlipJNI(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\secret\ColorSpaceConvert.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */