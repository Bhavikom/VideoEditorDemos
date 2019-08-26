package org.lasque.tusdk.core.utils.hardware;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Vibrator;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.utils.AssetsHelper;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.NetworkHelper;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;

public class HardwareHelper
{
  public static long appMemoryBit()
  {
    return Runtime.getRuntime().maxMemory();
  }
  
  public static AudioManager getAudioManager(Context paramContext)
  {
    return (AudioManager)ContextUtils.getSystemService(paramContext, "audio");
  }
  
  public static Vibrator getVibrator(Context paramContext)
  {
    return (Vibrator)ContextUtils.getSystemService(paramContext, "vibrator");
  }
  
  public static int getAlertVolume(Context paramContext, int paramInt)
  {
    AudioManager localAudioManager = getAudioManager(paramContext);
    if (localAudioManager == null) {
      return 100;
    }
    float f1 = localAudioManager.getStreamMaxVolume(paramInt);
    float f2 = localAudioManager.getStreamVolume(paramInt);
    return (int)(f2 * 100.0F / f1);
  }
  
  public static MediaPlayer loadMediaAsset(Context paramContext, String paramString)
  {
    AssetFileDescriptor localAssetFileDescriptor = AssetsHelper.getAssetFileDescriptor(paramContext, paramString);
    if (localAssetFileDescriptor == null) {
      return null;
    }
    MediaPlayer localMediaPlayer = new MediaPlayer();
    try
    {
      localMediaPlayer.setDataSource(localAssetFileDescriptor.getFileDescriptor(), localAssetFileDescriptor.getStartOffset(), localAssetFileDescriptor.getLength());
      localAssetFileDescriptor.close();
      localMediaPlayer.prepare();
      return localMediaPlayer;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      TLog.e(localIllegalArgumentException, "loadMediaAsset: %s", new Object[] { paramString });
    }
    catch (IllegalStateException localIllegalStateException)
    {
      TLog.e(localIllegalStateException, "loadMediaAsset: %s", new Object[] { paramString });
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "loadMediaAsset: %s", new Object[] { paramString });
    }
    return null;
  }
  
  public static void playSound(Context paramContext, final int paramInt)
  {
    if (paramContext == null) {
      return;
    }
    new Thread(new Runnable()
    {
      public void run()
      {
        MediaPlayer localMediaPlayer = MediaPlayer.create(this.a, paramInt);
        localMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
          public void onCompletion(MediaPlayer paramAnonymous2MediaPlayer)
          {
            paramAnonymous2MediaPlayer.release();
          }
        });
        localMediaPlayer.start();
      }
    }).start();
  }
  
  public static Intent getLaunchIntent(Context paramContext)
  {
    if (paramContext == null) {
      return null;
    }
    return paramContext.getPackageManager().getLaunchIntentForPackage(paramContext.getPackageName());
  }
  
  public static boolean hasShortcut(Context paramContext, String paramString)
  {
    String str = getAuthorityFromPermission(paramContext, "com.android.launcher.permission.READ_SETTINGS");
    if (str == null) {
      return true;
    }
    Uri localUri = Uri.parse("content://" + str + "/favorites?notify=true");
    boolean bool = false;
    Cursor localCursor = paramContext.getContentResolver().query(localUri, new String[] { "title" }, "title=?", new String[] { paramString }, null);
    if (localCursor == null) {
      return bool;
    }
    if (localCursor.moveToNext()) {
      bool = true;
    }
    localCursor.close();
    return bool;
  }
  
  public static String getAuthorityFromPermission(Context paramContext, String paramString)
  {
    if (StringHelper.isEmpty(paramString)) {
      return null;
    }
    List localList = paramContext.getPackageManager().getInstalledPackages(8);
    if ((localList == null) || (localList.isEmpty())) {
      return null;
    }
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      PackageInfo localPackageInfo = (PackageInfo)localIterator.next();
      ProviderInfo[] arrayOfProviderInfo1 = localPackageInfo.providers;
      if (arrayOfProviderInfo1 != null) {
        for (ProviderInfo localProviderInfo : arrayOfProviderInfo1) {
          if ((paramString.equals(localProviderInfo.readPermission)) || (paramString.equals(localProviderInfo.writePermission))) {
            return localProviderInfo.authority;
          }
        }
      }
    }
    return null;
  }
  
  public static boolean exsitAppInstall(Context paramContext, String paramString)
  {
    if ((paramContext == null) || (StringHelper.isEmpty(paramString))) {
      return false;
    }
    List localList = paramContext.getPackageManager().getInstalledPackages(0);
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      PackageInfo localPackageInfo = (PackageInfo)localIterator.next();
      if (localPackageInfo.packageName.equalsIgnoreCase(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean isNetworkAvailable(Context paramContext)
  {
    if (paramContext == null) {
      return false;
    }
    ConnectivityManager localConnectivityManager = (ConnectivityManager)ContextUtils.getSystemService(paramContext, "connectivity");
    if (localConnectivityManager == null) {
      return false;
    }
    NetworkInfo[] arrayOfNetworkInfo1 = localConnectivityManager.getAllNetworkInfo();
    if (arrayOfNetworkInfo1 == null) {
      return false;
    }
    for (NetworkInfo localNetworkInfo : arrayOfNetworkInfo1) {
      if (localNetworkInfo.isConnected()) {
        return true;
      }
    }
    return false;
  }
  
  public static boolean isNetworkWifi(Context paramContext)
  {
    if (paramContext == null) {
      return false;
    }
    ConnectivityManager localConnectivityManager = (ConnectivityManager)ContextUtils.getSystemService(paramContext, "connectivity");
    NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
    if ((localNetworkInfo != null) && (localNetworkInfo.getType() == 1)) {
      return localNetworkInfo.isConnected();
    }
    return false;
  }
  
  public static String getWifiIp(Context paramContext)
  {
    if (!isNetworkWifi(paramContext)) {
      return null;
    }
    WifiManager localWifiManager = (WifiManager)ContextUtils.getSystemService(paramContext, "wifi");
    if ((localWifiManager == null) || (!localWifiManager.isWifiEnabled())) {
      return null;
    }
    int i = localWifiManager.getConnectionInfo().getIpAddress();
    if (i == 0) {
      return null;
    }
    return NetworkHelper.longToIP(i);
  }
  
  public static boolean isMatchDeviceModel(String paramString)
  {
    if ((paramString == null) || (Build.MODEL == null)) {
      return false;
    }
    return Build.MODEL.equalsIgnoreCase(paramString);
  }
  
  public static boolean isMatchDeviceManuFacturer(String paramString)
  {
    if ((paramString == null) || (Build.MANUFACTURER == null)) {
      return false;
    }
    return Build.MANUFACTURER.equalsIgnoreCase(paramString);
  }
  
  public static boolean isMatchDeviceModelAndManuFacturer(String paramString1, String paramString2)
  {
    return (isMatchDeviceModel(paramString1)) && (isMatchDeviceManuFacturer(paramString2));
  }
  
  public static boolean isSupportAbi(String... paramVarArgs)
  {
    if ((paramVarArgs == null) || (paramVarArgs.length < 1)) {
      return false;
    }
    String[] arrayOfString1 = null;
    if (Build.VERSION.SDK_INT < 21) {
      arrayOfString1 = a();
    } else {
      arrayOfString1 = b();
    }
    if (arrayOfString1 == null) {
      return false;
    }
    for (String str1 : paramVarArgs) {
      for (String str2 : arrayOfString1) {
        if (str1.equalsIgnoreCase(str2)) {
          return true;
        }
      }
    }
    return false;
  }
  
  private static String[] a()
  {
    String str = Build.CPU_ABI;
    if (str == null) {
      return null;
    }
    return new String[] { str };
  }
  
  @TargetApi(21)
  private static String[] b()
  {
    return Build.SUPPORTED_ABIS;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\hardware\HardwareHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */