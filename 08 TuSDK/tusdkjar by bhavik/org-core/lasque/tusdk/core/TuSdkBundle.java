package org.lasque.tusdk.core;

import java.io.File;

public class TuSdkBundle
{
  public static final String BUNDLE = "TuSDK.bundle";
  public static final String FILTER_TEXTURES = "textures";
  public static final String LOCAL_STICKERS = "stickers";
  public static final String LOCAL_BRUSHES = "brushes";
  public static final String OTHER_RESOURES = "others";
  public static final String MODEL_RESOURES = "model";
  public static final String CAMERA_FOCUS_BEEP_AUDIO_RAW = "camera_focus_beep.mp3";
  public static final String INTERNAL_FILTERS_CONFIG = "lsq_internal_filters.filter";
  
  public static String sdkBundle()
  {
    return "TuSDK.bundle";
  }
  
  public static String sdkBundle(String paramString)
  {
    return String.format("%s%s%s", new Object[] { sdkBundle(), File.separator, paramString });
  }
  
  public static String sdkBundle(String paramString1, String paramString2)
  {
    return sdkBundle(String.format("%s%s%s", new Object[] { paramString1, File.separator, paramString2 }));
  }
  
  public static String sdkBundleTexture(String paramString)
  {
    return sdkBundle("textures", paramString);
  }
  
  public static String sdkBundleOther(String paramString)
  {
    return sdkBundle("others", paramString);
  }
  
  public static String sdkBundleModel(String paramString)
  {
    return sdkBundle("model", paramString);
  }
  
  public static String sdkBundleSticker(String paramString)
  {
    return sdkBundle("stickers", paramString);
  }
  
  public static String sdkBundleBrush(String paramString)
  {
    return sdkBundle("brushes", paramString);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\TuSdkBundle.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */