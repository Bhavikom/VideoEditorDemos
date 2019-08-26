package org.lasque.tusdk.core.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;

public class NetworkHelper
{
  public static InetAddress getLocalHost()
  {
    InetAddress localInetAddress = null;
    try
    {
      localInetAddress = InetAddress.getLocalHost();
    }
    catch (UnknownHostException localUnknownHostException)
    {
      TLog.e(localUnknownHostException, "getLocalHost", new Object[0]);
    }
    return localInetAddress;
  }
  
  public static InetAddress getInetAddress(String paramString)
  {
    if (StringHelper.isEmpty(paramString)) {
      return null;
    }
    InetAddress localInetAddress = null;
    try
    {
      localInetAddress = InetAddress.getByName(paramString);
    }
    catch (UnknownHostException localUnknownHostException)
    {
      TLog.e(localUnknownHostException, "getInetAddress: %s", new Object[] { paramString });
    }
    return localInetAddress;
  }
  
  public static Socket buildSocket(InetAddress paramInetAddress, int paramInt)
  {
    if ((paramInetAddress == null) || (paramInt < 1) || (paramInt > 65535)) {
      return null;
    }
    Socket localSocket = null;
    try
    {
      localSocket = new Socket(paramInetAddress, paramInt);
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "buildSocket: %s | %s", new Object[] { paramInetAddress, Integer.valueOf(paramInt) });
    }
    return localSocket;
  }
  
  public static long ipToLong(String paramString)
  {
    long[] arrayOfLong = new long[4];
    int i = paramString.indexOf(".");
    int j = paramString.indexOf(".", i + 1);
    int k = paramString.indexOf(".", j + 1);
    arrayOfLong[0] = Long.parseLong(paramString.substring(0, i));
    arrayOfLong[1] = Long.parseLong(paramString.substring(i + 1, j));
    arrayOfLong[2] = Long.parseLong(paramString.substring(j + 1, k));
    arrayOfLong[3] = Long.parseLong(paramString.substring(k + 1));
    return (arrayOfLong[0] << 24) + (arrayOfLong[1] << 16) + (arrayOfLong[2] << 8) + arrayOfLong[3];
  }
  
  public static String longToIP(long paramLong)
  {
    String str = String.format("%d.%d.%d.%d", new Object[] { Long.valueOf(paramLong & 0xFF), Long.valueOf(paramLong >> 8 & 0xFF), Long.valueOf(paramLong >> 16 & 0xFF), Long.valueOf(paramLong >> 24 & 0xFF) });
    return str;
  }
  
  public static boolean isPhoneNetwork()
  {
    ConnectivityManager localConnectivityManager = (ConnectivityManager)TuSdk.appContext().getContext().getSystemService("connectivity");
    NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
    return (localNetworkInfo != null) && (localNetworkInfo.getType() == 0);
  }
  
  public static boolean isWifiNetwork()
  {
    ConnectivityManager localConnectivityManager = (ConnectivityManager)TuSdk.appContext().getContext().getSystemService("connectivity");
    NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
    return (localNetworkInfo != null) && (localNetworkInfo.getType() == 1);
  }
  
  public static String getNetworkState()
  {
    if (isPhoneNetwork()) {
      return "Cellular";
    }
    if (isWifiNetwork()) {
      return "wifi";
    }
    return "No Connection";
  }
  
  public static WIFIInfoBean getLocalMacAddress(Context paramContext)
  {
    WIFIInfoBean localWIFIInfoBean = new WIFIInfoBean();
    if ((!a()) || (isPhoneNetwork())) {
      return localWIFIInfoBean;
    }
    Object localObject1;
    Object localObject2;
    try
    {
      WifiManager localWifiManager = (WifiManager)paramContext.getSystemService("wifi");
      localObject1 = localWifiManager.getConnectionInfo();
      localWIFIInfoBean.setMacAddress(((WifiInfo)localObject1).getMacAddress());
      localWIFIInfoBean.setSSID(((WifiInfo)localObject1).getSSID());
      localWIFIInfoBean.setBSSID(((WifiInfo)localObject1).getBSSID());
      localObject2 = localWifiManager.getDhcpInfo();
      localWIFIInfoBean.setGateway(longToIP(((DhcpInfo)localObject2).gateway));
      localWIFIInfoBean.setIp(longToIP(((DhcpInfo)localObject2).ipAddress));
      localWIFIInfoBean.setMacAddress(TuSdkDeviceInfo.getMac());
    }
    catch (Throwable localThrowable1)
    {
      localThrowable1.printStackTrace();
    }
    try
    {
      if (Build.VERSION.SDK_INT >= 23)
      {
        Enumeration localEnumeration = NetworkInterface.getNetworkInterfaces();
        while (localEnumeration.hasMoreElements())
        {
          localObject1 = (NetworkInterface)localEnumeration.nextElement();
          if ("wlan0".equalsIgnoreCase(((NetworkInterface)localObject1).getName()))
          {
            localObject2 = new byte[0];
            localObject2 = ((NetworkInterface)localObject1).getHardwareAddress();
            if ((localObject2 != null) && (localObject2.length != 0))
            {
              StringBuilder localStringBuilder = new StringBuilder();
              for (byte b : localObject2) {
                localStringBuilder.append(String.format("%02X:", new Object[] { Byte.valueOf(b) }));
              }
              if (localStringBuilder.length() > 0) {
                localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
              }
              ??? = localStringBuilder.toString();
              localWIFIInfoBean.setMacAddress((String)???);
            }
          }
        }
      }
    }
    catch (Throwable localThrowable2)
    {
      localThrowable2.printStackTrace();
    }
    return localWIFIInfoBean;
  }
  
  private static boolean a()
  {
    if (Build.VERSION.SDK_INT < 23)
    {
      PackageManager localPackageManager = TuSdkContext.context().getPackageManager();
      boolean bool = 0 == localPackageManager.checkPermission("android.permission.ACCESS_WIFI_STATE", TuSdkContext.getPackageName());
      return bool;
    }
    return TuSdkContext.context().checkSelfPermission("android.permission.ACCESS_WIFI_STATE") != -1;
  }
  
  public static List<ScanResultBean> getScanResultList()
  {
    ArrayList localArrayList = new ArrayList();
    if (!a()) {
      return localArrayList;
    }
    List localList = getWifiScanResult(TuSdk.appContext().getContext());
    if ((localList == null) || (localList.size() == 0)) {
      return localArrayList;
    }
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      ScanResult localScanResult = (ScanResult)localIterator.next();
      ScanResultBean localScanResultBean = new ScanResultBean();
      ScanResultBean.a(localScanResultBean, localScanResult.BSSID);
      ScanResultBean.b(localScanResultBean, localScanResult.level + "");
      ScanResultBean.c(localScanResultBean, localScanResult.SSID);
      ScanResultBean.d(localScanResultBean, localScanResult.capabilities);
      localArrayList.add(localScanResultBean);
    }
    return localArrayList;
  }
  
  public static List<ScanResult> getWifiScanResult(Context paramContext)
  {
    int i = paramContext == null ? 1 : 0;
    return ((WifiManager)paramContext.getSystemService("wifi")).getScanResults();
  }
  
  public static class ScanResultBean
  {
    private String a;
    private String b;
    private String c;
    private String d;
    
    public String getSSID()
    {
      return this.a;
    }
    
    public void setSSID(String paramString)
    {
      this.a = paramString;
    }
    
    public String getBSSID()
    {
      return this.b;
    }
    
    public void setBSSID(String paramString)
    {
      this.b = paramString;
    }
    
    public String getLevel()
    {
      return this.c;
    }
    
    public void setLevel(String paramString)
    {
      this.c = paramString;
    }
    
    public String getCapabilities()
    {
      return this.d;
    }
    
    public void setCapabilities(String paramString)
    {
      this.d = paramString;
    }
    
    public String toString()
    {
      JSONObject localJSONObject = new JSONObject();
      try
      {
        localJSONObject.put("0x2001", StringHelper.Base64Encode(getSSID()));
        localJSONObject.put("0x2002", StringHelper.Base64Encode(getBSSID()));
        localJSONObject.put("0x2003", StringHelper.Base64Encode(getLevel()));
        localJSONObject.put("0x2004", StringHelper.Base64Encode(getCapabilities()));
      }
      catch (JSONException localJSONException) {}
      return localJSONObject.toString();
    }
  }
  
  public static class WIFIInfoBean
  {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    
    public String getMacAddress()
    {
      return this.a;
    }
    
    public void setMacAddress(String paramString)
    {
      this.a = paramString;
    }
    
    public String getSSID()
    {
      return this.b;
    }
    
    public void setSSID(String paramString)
    {
      this.b = paramString;
    }
    
    public String getBSSID()
    {
      return this.c;
    }
    
    public void setBSSID(String paramString)
    {
      this.c = paramString;
    }
    
    public String getGateway()
    {
      return this.d;
    }
    
    public void setGateway(String paramString)
    {
      this.d = paramString;
    }
    
    public String getIp()
    {
      return this.e;
    }
    
    public void setIp(String paramString)
    {
      this.e = paramString;
    }
    
    public String toString()
    {
      JSONObject localJSONObject = new JSONObject();
      try
      {
        localJSONObject.put("0x5001", StringHelper.Base64Encode(this.a));
        localJSONObject.put("0x5002", StringHelper.Base64Encode(this.b));
        localJSONObject.put("0x5003", StringHelper.Base64Encode(this.c));
        localJSONObject.put("0x5004", StringHelper.Base64Encode(this.d));
        localJSONObject.put("0x5005", StringHelper.Base64Encode(this.e));
      }
      catch (JSONException localJSONException)
      {
        localJSONException.printStackTrace();
      }
      return localJSONObject.toString();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\NetworkHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */