package org.lasque.tusdk.core.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;

public class TuSdkDeviceInfo
{
  public static final String VENDER_HUAWEI = "HUAWEI";
  public static final String MODEL_HUAWEI_NXTAL10 = "HUAWEI NXT-AL10";
  public static final String VENDER_OPPO = "OPPO";
  public static final String MODEL_OPPO_A3 = "PADM00";
  public static final String VENDER_MEITU = "Meitu";
  public static final String VENDER_XIAOMI = "XiaoMi";
  public static final String MODEL_XIAOMI_MI_NOTE_LTE = "MI NOTE LTE";
  
  @TargetApi(23)
  public static boolean hasRequiredPermissions(Context paramContext, String[] paramArrayOfString)
  {
    if (Build.VERSION.SDK_INT < 23) {
      return true;
    }
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0)) {
      for (String str : paramArrayOfString) {
        if (paramContext.checkSelfPermission(str) != 0) {
          return false;
        }
      }
    }
    return true;
  }
  
  public static String getIMEI()
  {
    String str = "";
    Context localContext = TuSdkContext.context();
    if (localContext == null) {
      return str;
    }
    boolean bool = hasRequiredPermissions(localContext, getRequiredPermissions());
    if (bool)
    {
      str = readPhoneInfo(localContext, 10);
      return str;
    }
    return str;
  }
  
  protected static String readPhoneInfo(Context paramContext, int paramInt)
  {
    String str = "";
    try
    {
      TelephonyManager localTelephonyManager = (TelephonyManager)paramContext.getSystemService("phone");
      if (localTelephonyManager == null) {
        return str;
      }
      switch (paramInt)
      {
      case 10: 
        str = localTelephonyManager.getDeviceId();
        break;
      case 20: 
        str = localTelephonyManager.getSubscriberId();
      }
    }
    catch (Exception localException) {}
    return str;
  }
  
  public static String getIMSI()
  {
    String str = "";
    Context localContext = TuSdkContext.context();
    if (localContext == null) {
      return str;
    }
    boolean bool = hasRequiredPermissions(localContext, getRequiredPermissions());
    if (bool)
    {
      str = readPhoneInfo(localContext, 20);
      return str;
    }
    return str;
  }
  
  @TargetApi(23)
  protected static String[] getRequiredPermissions()
  {
    String[] arrayOfString = { "android.permission.READ_PHONE_STATE" };
    return arrayOfString;
  }
  
  public static String getMac()
  {
    String str = "";
    Context localContext = TuSdkContext.context();
    if (localContext == null) {
      return str;
    }
    try
    {
      NetworkInterface localNetworkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName(getIP()));
      byte[] arrayOfByte = localNetworkInterface.getHardwareAddress();
      str = byte2hex(arrayOfByte);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return str;
  }
  
  public static String getMac2()
  {
    Enumeration localEnumeration = null;
    try
    {
      localEnumeration = NetworkInterface.getNetworkInterfaces();
    }
    catch (SocketException localSocketException) {}
    String str1 = null;
    NetworkInterface localNetworkInterface = null;
    if (localEnumeration == null) {
      return null;
    }
    String str2 = getIP();
    while (localEnumeration.hasMoreElements())
    {
      localNetworkInterface = (NetworkInterface)localEnumeration.nextElement();
      try
      {
        if (!TextUtils.isEmpty(str2))
        {
          Iterator localIterator = localNetworkInterface.getInterfaceAddresses().iterator();
          while (localIterator.hasNext())
          {
            InterfaceAddress localInterfaceAddress = (InterfaceAddress)localIterator.next();
            if ((localInterfaceAddress.getAddress().getHostAddress().equals(str2)) && (localNetworkInterface.getHardwareAddress() != null))
            {
              str1 = byte2hex(localNetworkInterface.getHardwareAddress());
              break;
            }
          }
          if (str1 != null) {
            break;
          }
        }
      }
      catch (Exception localException) {}
    }
    return str1 == null ? "" : str1;
  }
  
  public static String byte2hex(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer(paramArrayOfByte.length);
    String str = "";
    int i = paramArrayOfByte.length;
    for (int j = 0; j < i; j++)
    {
      str = Integer.toHexString(paramArrayOfByte[j] & 0xFF);
      if (str.length() == 1) {
        localStringBuffer = localStringBuffer.append("0").append(str);
      } else {
        localStringBuffer = localStringBuffer.append(str);
      }
    }
    return String.valueOf(localStringBuffer);
  }
  
  public static String getIP()
  {
    String str = "";
    try
    {
      Enumeration localEnumeration1 = NetworkInterface.getNetworkInterfaces();
      while (localEnumeration1.hasMoreElements())
      {
        NetworkInterface localNetworkInterface = (NetworkInterface)localEnumeration1.nextElement();
        Enumeration localEnumeration2 = localNetworkInterface.getInetAddresses();
        while (localEnumeration2.hasMoreElements())
        {
          InetAddress localInetAddress = (InetAddress)localEnumeration2.nextElement();
          if (!localInetAddress.isLoopbackAddress()) {
            str = localInetAddress.getHostAddress().toString();
          }
        }
      }
    }
    catch (Exception localException) {}
    return str;
  }
  
  public static String getOSVersion()
  {
    String str = Build.VERSION.RELEASE;
    return str;
  }
  
  public static String getModel()
  {
    String str = Build.MODEL;
    return str;
  }
  
  public static String getVender()
  {
    String str = Build.MANUFACTURER;
    return str;
  }
  
  public static String getAndroidID()
  {
    String str = "";
    Context localContext = TuSdkContext.context();
    if (localContext == null) {
      return str;
    }
    str = Settings.Secure.getString(localContext.getContentResolver(), "android_id");
    return str;
  }
  
  public static String getLocation()
  {
    Location localLocation = TuSdkLocation.getLastLocation();
    if (localLocation == null) {
      return "";
    }
    return localLocation.getLongitude() + "," + localLocation.getLatitude();
  }
  
  public static boolean isSupportPbo()
  {
    Context localContext = TuSdkContext.context();
    ActivityManager localActivityManager = (ActivityManager)localContext.getSystemService("activity");
    ConfigurationInfo localConfigurationInfo = localActivityManager.getDeviceConfigurationInfo();
    int i = (localConfigurationInfo.reqGlEsVersion & 0xFFFF0000) >> 16;
    return (i >= 3) && (Build.VERSION.SDK_INT >= 18);
  }
  
  public static List<PackageInfo> getInstallAppInfoList()
  {
    List localList = TuSdk.appContext().getContext().getPackageManager().getInstalledPackages(0);
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      PackageInfo localPackageInfo = (PackageInfo)localIterator.next();
      if ((localPackageInfo.applicationInfo.flags & 0x1) == 0) {
        localArrayList.add(localPackageInfo);
      }
    }
    return localArrayList;
  }
  
  public static String getAdvertisingIdInfo(Context paramContext)
  {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      throw new IllegalStateException("Cannot be called from the main thread");
    }
    try
    {
      PackageManager localPackageManager = paramContext.getPackageManager();
      localPackageManager.getPackageInfo("com.android.vending", 0);
    }
    catch (Exception localException1)
    {
      throw localException1;
    }
    Intent localIntent = new Intent("com.google.android.gms.ads.identifier.service.START");
    localIntent.setPackage("com.google.android.gms");
    ServiceConnection local1AdvertisingConnection = new ServiceConnection()
    {
      private boolean a = false;
      private final LinkedBlockingQueue<IBinder> b = new LinkedBlockingQueue(1);
      
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        try
        {
          this.b.put(paramAnonymousIBinder);
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
        }
      }
      
      public void onServiceDisconnected(ComponentName paramAnonymousComponentName) {}
      
      public IBinder getBinder()
      {
        if (this.a) {
          throw new IllegalStateException();
        }
        this.a = true;
        return (IBinder)this.b.take();
      }
    };
    if (paramContext.bindService(localIntent, local1AdvertisingConnection, 1)) {
      try
      {
        IInterface local1AdvertisingInterface = new IInterface()
        {
          public IBinder asBinder()
          {
            return TuSdkDeviceInfo.this;
          }
          
          public String getId()
          {
            Parcel localParcel1 = Parcel.obtain();
            Parcel localParcel2 = Parcel.obtain();
            String str = null;
            try
            {
              localParcel1.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
              TuSdkDeviceInfo.this.transact(1, localParcel1, localParcel2, 0);
              localParcel2.readException();
              str = localParcel2.readString();
            }
            catch (Exception localException)
            {
              localException.printStackTrace();
            }
            finally
            {
              localParcel2.recycle();
              localParcel1.recycle();
            }
            return str;
          }
        };
        String str = local1AdvertisingInterface.getId();
        return str;
      }
      catch (Exception localException2)
      {
        throw localException2;
      }
      finally
      {
        paramContext.unbindService(local1AdvertisingConnection);
      }
    }
    throw new IOException("Google Play connection failed");
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\TuSdkDeviceInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */