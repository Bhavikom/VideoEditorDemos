package org.lasque.tusdk.core.secret;

import android.graphics.Bitmap;

public class SdkAOFile
{
  private static final boolean a = SdkValid.isInit;
  private String b;
  private long c;
  
  public SdkAOFile(String paramString, boolean paramBoolean)
  {
    this.b = paramString;
    this.c = jniLoadFile(this.b, paramBoolean);
  }
  
  public Bitmap loadImage(String paramString)
  {
    return jniLoadImage(this.c, paramString);
  }
  
  public String loadText(String paramString)
  {
    return jniLoadText(this.c, paramString);
  }
  
  public byte[] loadBinary(String paramString)
  {
    return jniLoadBinary(this.c, paramString);
  }
  
  public void release()
  {
    jniRelease(this.c);
  }
  
  private static native long jniLoadFile(String paramString, boolean paramBoolean);
  
  private static native Bitmap jniLoadImage(long paramLong, String paramString);
  
  private static native String jniLoadText(long paramLong, String paramString);
  
  private static native byte[] jniLoadBinary(long paramLong, String paramString);
  
  private static native void jniRelease(long paramLong);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\secret\SdkAOFile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */