package org.lasque.tusdk.core.secret;

import android.content.pm.PackageInfo;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.NetworkHelper;
import org.lasque.tusdk.core.utils.NetworkHelper.ScanResultBean;
import org.lasque.tusdk.core.utils.NetworkHelper.WIFIInfoBean;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.TuSdkDeviceInfo;

public class LogStashManager
{
  private static String a = "logstash.statistics";
  private static LogStashManager b;
  private final File c;
  private Object d = new Object();
  
  public static void init(File paramFile)
  {
    if (b == null) {
      b = new LogStashManager(paramFile);
    }
  }
  
  public static LogStashManager getInstance()
  {
    if (b == null) {
      TLog.w("LogStashManager is not Initialization !!!", new Object[0]);
    }
    return b;
  }
  
  public LogStashManager(File paramFile)
  {
    this.c = paramFile;
    stashLog();
  }
  
  public void stashLog()
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          Thread.sleep(200L);
          LogStashManager.a(LogStashManager.this);
        }
        catch (InterruptedException localInterruptedException)
        {
          localInterruptedException.printStackTrace();
        }
      }
    }).start();
  }
  
  private void a()
  {
    try
    {
      String str = "";
      try
      {
        str = TuSdkDeviceInfo.getAdvertisingIdInfo(TuSdk.appContext().getContext());
      }
      catch (Exception localException2)
      {
        TLog.w("Google id has exception!!!", new Object[0]);
      }
      List localList1 = NetworkHelper.getScanResultList();
      List localList2 = TuSdkDeviceInfo.getInstallAppInfoList();
      ArrayList localArrayList = new ArrayList();
      if ((localList2 != null) && (localList2.size() > 0))
      {
        localObject = localList2.iterator();
        while (((Iterator)localObject).hasNext())
        {
          PackageInfo localPackageInfo = (PackageInfo)((Iterator)localObject).next();
          PacketInfoBean localPacketInfoBean = new PacketInfoBean(null);
          PacketInfoBean.a(localPacketInfoBean, localPackageInfo.versionName);
          PacketInfoBean.b(localPacketInfoBean, localPackageInfo.versionCode + "");
          PacketInfoBean.c(localPacketInfoBean, localPackageInfo.firstInstallTime + "");
          PacketInfoBean.d(localPacketInfoBean, localPackageInfo.packageName);
          localArrayList.add(localPacketInfoBean);
        }
      }
      Object localObject = a(str, localList1, localArrayList);
      a(((JSONObject)localObject).toString());
    }
    catch (Exception localException1)
    {
      localException1.printStackTrace();
    }
  }
  
  private JSONObject a(String paramString, List<NetworkHelper.ScanResultBean> paramList, List<PacketInfoBean> paramList1)
  {
    JSONObject localJSONObject1 = new JSONObject();
    localJSONObject1.put("ttp", String.valueOf(System.currentTimeMillis()));
    localJSONObject1.put("0x1000", StringHelper.Base64Encode(paramString));
    JSONArray localJSONArray = new JSONArray();
    Object localObject3;
    if ((paramList != null) && (paramList.size() > 0))
    {
      localObject1 = paramList.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (NetworkHelper.ScanResultBean)((Iterator)localObject1).next();
        localObject3 = new JSONObject(((NetworkHelper.ScanResultBean)localObject2).toString());
        localJSONArray.put(localObject3);
      }
    }
    localJSONObject1.put("0x2000", localJSONArray);
    Object localObject1 = new JSONArray();
    if ((paramList1 != null) && (paramList1.size() > 0))
    {
      localObject2 = paramList1.iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (PacketInfoBean)((Iterator)localObject2).next();
        JSONObject localJSONObject2 = new JSONObject(((PacketInfoBean)localObject3).toString());
        ((JSONArray)localObject1).put(localJSONObject2);
      }
    }
    localJSONObject1.put("0x3000", localObject1);
    localJSONObject1.put("0x4000", StringHelper.Base64Encode(NetworkHelper.getNetworkState()));
    Object localObject2 = NetworkHelper.getLocalMacAddress(TuSdk.appContext().getContext());
    localJSONObject1.put("0x5000", new JSONObject(((NetworkHelper.WIFIInfoBean)localObject2).toString()));
    return localJSONObject1;
  }
  
  private void a(String paramString)
  {
    String str = paramString;
    synchronized (this.d)
    {
      SdkValid.shared.saveLogStash(str, this.c.getPath() + "/" + a);
    }
  }
  
  public LogBean getUpLoadData()
  {
    if (!new File(this.c + "/" + a).exists()) {
      return null;
    }
    LogBean localLogBean = new LogBean();
    synchronized (this.d)
    {
      byte[] arrayOfByte = FileHelper.getBytesFromFile(new File(this.c + "/" + a));
      if (arrayOfByte == null) {
        return localLogBean;
      }
      String str1 = new String(arrayOfByte);
      if (str1.isEmpty()) {
        return localLogBean;
      }
      String str2 = str1.substring(0, str1.length() - 2);
      String str3 = str1.substring(str1.length() - 2, str1.length());
      LogBean.a(localLogBean, str3);
      LogBean.b(localLogBean, str2);
    }
    return localLogBean;
  }
  
  public void deleteTempFile()
  {
    try
    {
      if (!new File(this.c + "/" + a).exists()) {
        return;
      }
      FileHelper.delete(new File(this.c + "/" + a));
      if (getInstance() != null) {
        getInstance().stashLog();
      }
    }
    catch (Exception localException)
    {
      TLog.w("delete log temp file error", new Object[0]);
    }
  }
  
  private class PacketInfoBean
  {
    private String b;
    private String c;
    private String d;
    private String e;
    
    private PacketInfoBean() {}
    
    public String getVersion()
    {
      return this.b;
    }
    
    public void setVersion(String paramString)
    {
      this.b = paramString;
    }
    
    public String getCode()
    {
      return this.c;
    }
    
    public void setCode(String paramString)
    {
      this.c = paramString;
    }
    
    public String getAppName()
    {
      return this.d;
    }
    
    public void setAppName(String paramString)
    {
      this.d = paramString;
    }
    
    public String getInstallTime()
    {
      return this.e;
    }
    
    public void setInstallTime(String paramString)
    {
      this.e = paramString;
    }
    
    public String toString()
    {
      JSONObject localJSONObject = new JSONObject();
      try
      {
        localJSONObject.put("0x3001", StringHelper.Base64Encode(getVersion()));
        localJSONObject.put("0x3002", StringHelper.Base64Encode(getCode()));
        localJSONObject.put("0x3003", StringHelper.Base64Encode(getAppName()));
        localJSONObject.put("0x3004", StringHelper.Base64Encode(getInstallTime()));
      }
      catch (JSONException localJSONException) {}
      return localJSONObject.toString();
    }
  }
  
  public class LogBean
  {
    private String b;
    private String c;
    
    public LogBean() {}
    
    public String getIndex()
    {
      return this.b;
    }
    
    public void setIndex(String paramString)
    {
      this.b = paramString;
    }
    
    public String getData()
    {
      return this.c;
    }
    
    public void setData(String paramString)
    {
      this.c = paramString;
    }
    
    public ByteArrayInputStream getByteArrayInputStream()
    {
      byte[] arrayOfByte = Base64.decode(getData(), 0);
      return new ByteArrayInputStream(arrayOfByte);
    }
    
    public boolean isValid()
    {
      try
      {
        return (!StringHelper.isEmpty(this.b)) && (!StringHelper.isEmpty(this.c));
      }
      catch (Exception localException) {}
      return false;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\secret\LogStashManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */