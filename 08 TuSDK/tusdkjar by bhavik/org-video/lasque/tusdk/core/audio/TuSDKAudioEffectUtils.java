package org.lasque.tusdk.core.audio;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Process;
import java.util.Arrays;
import java.util.List;
import org.lasque.tusdk.core.utils.TLog;

final class TuSDKAudioEffectUtils
{
  private static final String[] a = new String[0];
  private static final String[] b = { "D6503", "ONE A2005", "MotoG3", "Redmi 4A" };
  private static final String[] c = { "Nexus 10", "Nexus 9", "Redmi 4A" };
  private static final String[] d = { "Nexus 10", "Nexus 9", "ONE A2005", "Redmi 4A" };
  private static int e = 16000;
  private static boolean f = false;
  private static boolean g = false;
  private static boolean h = false;
  private static boolean i = false;
  
  public static synchronized void setBasedAcousticEchoCanceler(boolean paramBoolean)
  {
    g = paramBoolean;
  }
  
  public static synchronized void setBasedAutomaticGainControl(boolean paramBoolean)
  {
    h = paramBoolean;
  }
  
  public static synchronized void setBasedNoiseSuppressor(boolean paramBoolean)
  {
    i = paramBoolean;
  }
  
  public static synchronized boolean useBasedAcousticEchoCanceler()
  {
    return g;
  }
  
  public static synchronized boolean useBasedAutomaticGainControl()
  {
    return h;
  }
  
  public static synchronized boolean useBasedNoiseSuppressor()
  {
    return i;
  }
  
  public static boolean isAcousticEchoCancelerSupported()
  {
    return TuSDKAudioEffects.canUseAcousticEchoCanceler();
  }
  
  public static boolean isAutomaticGainControlSupported()
  {
    return TuSDKAudioEffects.canUseAutomaticGainControl();
  }
  
  public static boolean isNoiseSuppressorSupported()
  {
    return TuSDKAudioEffects.canUseNoiseSuppressor();
  }
  
  public static synchronized void setDefaultSampleRateHz(int paramInt)
  {
    f = true;
    e = paramInt;
  }
  
  public static synchronized boolean isDefaultSampleRateOverridden()
  {
    return f;
  }
  
  public static synchronized int getDefaultSampleRateHz()
  {
    return e;
  }
  
  public static List<String> getBlackListedModelsForAecUsage()
  {
    return Arrays.asList(b);
  }
  
  public static List<String> getBlackListedModelsForAgcUsage()
  {
    return Arrays.asList(c);
  }
  
  public static List<String> getBlackListedModelsForNsUsage()
  {
    return Arrays.asList(d);
  }
  
  public static boolean runningOnGingerBreadOrHigher()
  {
    return Build.VERSION.SDK_INT >= 9;
  }
  
  public static boolean runningOnJellyBeanOrHigher()
  {
    return Build.VERSION.SDK_INT >= 16;
  }
  
  public static boolean runningOnJellyBeanMR1OrHigher()
  {
    return Build.VERSION.SDK_INT >= 17;
  }
  
  public static boolean runningOnJellyBeanMR2OrHigher()
  {
    return Build.VERSION.SDK_INT >= 18;
  }
  
  public static boolean runningOnLollipopOrHigher()
  {
    return Build.VERSION.SDK_INT >= 21;
  }
  
  public static boolean runningOnMarshmallowOrHigher()
  {
    return Build.VERSION.SDK_INT >= 23;
  }
  
  public static String getThreadInfo()
  {
    return "@[name=" + Thread.currentThread().getName() + ", id=" + Thread.currentThread().getId() + "]";
  }
  
  public static boolean runningOnEmulator()
  {
    return (Build.HARDWARE.equals("goldfish")) && (Build.BRAND.startsWith("generic_"));
  }
  
  public static boolean deviceIsBlacklistedForOpenSLESUsage()
  {
    List localList = Arrays.asList(a);
    return localList.contains(Build.MODEL);
  }
  
  public static void logDeviceInfo(String paramString)
  {
    TLog.d("Android SDK: " + Build.VERSION.SDK_INT + ", Release: " + Build.VERSION.RELEASE + ", Brand: " + Build.BRAND + ", Device: " + Build.DEVICE + ", Id: " + Build.ID + ", Hardware: " + Build.HARDWARE + ", Manufacturer: " + Build.MANUFACTURER + ", Model: " + Build.MODEL + ", Product: " + Build.PRODUCT, new Object[0]);
  }
  
  public static boolean hasPermission(Context paramContext, String paramString)
  {
    return paramContext.checkPermission(paramString, Process.myPid(), Process.myUid()) == 0;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\audio\TuSDKAudioEffectUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */