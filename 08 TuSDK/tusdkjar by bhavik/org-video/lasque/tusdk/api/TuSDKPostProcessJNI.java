package org.lasque.tusdk.api;

import org.lasque.tusdk.core.decoder.TuSDKVideoInfo;

public class TuSDKPostProcessJNI
{
  public static boolean runVideoCommands(String[] paramArrayOfString)
  {
    return runVideoCommandsJNI(paramArrayOfString) == 0;
  }
  
  private static native int runVideoCommandsJNI(String[] paramArrayOfString);
  
  public static native void readVideoInfo(String paramString, TuSDKVideoInfo paramTuSDKVideoInfo);
  
  public static native int fastStart(String paramString1, String paramString2);
  
  static
  {
    System.loadLibrary("tusdk-video");
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\api\TuSDKPostProcessJNI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */