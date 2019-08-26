package org.lasque.tusdk.core.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import org.lasque.tusdk.core.TuSdkConfigs;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.http.RequestHandle;
import org.lasque.tusdk.core.http.ResponseHandlerInterface;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.TuSdkDeviceInfo;
import org.lasque.tusdk.core.utils.hardware.HardwareHelper;
import org.lasque.tusdk.core.utils.hardware.TuSdkSharedPreferences;

public class TuSdkHttpEngine
{
  public static final String SDK_TYPE_IMAGE = "1";
  public static final int ENVIRONMENT_LOCAL = 0;
  public static final int ENVIRONMENT_TEST = 1;
  public static final int ENVIRONMENT_PRODUCTION = 2;
  public static final int ENVIRONMENT = 2;
  public static final String NETWORK_PATH = "api";
  public static final String WEB_PATH = "web";
  public static final String SRV_PATH = "srv";
  public static final String NETWORK_DOMAIN;
  public static final String NETWORK_WEB_DOMAIN;
  public static final String SERVICE_DOMAIN;
  public static final String NETWORK_AUTH_DOMAIN;
  public static final int NETWORK_PORT;
  public static final int SERVICE_PORT;
  public static String API_DOMAIN;
  public static String WEB_DOMAIN;
  public static String WEB_API_DOMAIN;
  public static String AUTH_DOMAIN;
  public static String WEB_PIC_DOMAIN;
  public static String SRV_DOMAIN;
  public static final boolean DEBUG;
  private boolean a = false;
  private static TuSdkHttpEngine b;
  private static TuSdkHttpEngine c;
  private static TuSdkHttpEngine d;
  private static TuSdkHttpEngine e;
  public static boolean useSSL = true;
  private Context f;
  private TuSdkHttp g;
  private String h;
  private String i;
  private String j;
  private String k;
  private String l;
  private int m;
  private BroadcastReceiver n;
  
  public static TuSdkHttpEngine shared()
  {
    return b;
  }
  
  public static TuSdkHttpEngine service()
  {
    return c;
  }
  
  public static TuSdkHttpEngine webAPIEngine()
  {
    return d;
  }
  
  public static TuSdkHttpEngine auth()
  {
    return e;
  }
  
  public static TuSdkHttpEngine init(TuSdkConfigs paramTuSdkConfigs, String paramString, Context paramContext)
  {
    if ((b == null) && (c == null) && (d == null) && (e == null) && (paramTuSdkConfigs != null))
    {
      String str = useSSL ? "https://%s/%s" : "http://%s/%s";
      API_DOMAIN = String.format("http://%s/%s", new Object[] { NETWORK_DOMAIN, "api" });
      WEB_DOMAIN = String.format(str, new Object[] { NETWORK_WEB_DOMAIN, "web" });
      SRV_DOMAIN = String.format(str, new Object[] { SERVICE_DOMAIN, "srv" });
      WEB_API_DOMAIN = String.format(str, new Object[] { NETWORK_WEB_DOMAIN, "api" });
      WEB_PIC_DOMAIN = String.format(str, new Object[] { "img.tusdk.com", "api" });
      AUTH_DOMAIN = String.format("https://%s/%s", new Object[] { NETWORK_AUTH_DOMAIN, "api" });
      b = new TuSdkHttpEngine(paramTuSdkConfigs, paramString, paramContext, API_DOMAIN, NETWORK_PORT);
      c = new TuSdkHttpEngine(paramTuSdkConfigs, paramString, paramContext, SRV_DOMAIN, SERVICE_PORT);
      d = new TuSdkHttpEngine(paramTuSdkConfigs, paramString, paramContext, WEB_API_DOMAIN, NETWORK_PORT);
      e = new TuSdkHttpEngine(paramTuSdkConfigs, paramString, paramContext, AUTH_DOMAIN, NETWORK_PORT);
    }
    return b;
  }
  
  private TuSdkHttp a()
  {
    return this.g;
  }
  
  public String userIdentify()
  {
    return this.k;
  }
  
  public void setUserIdentify(Object paramObject)
  {
    if (this.k == null)
    {
      this.k = null;
      this.g.removeHeader("x-client-user");
    }
    else
    {
      this.k = paramObject.toString();
      this.g.addHeader("x-client-user", this.k);
    }
  }
  
  private TuSdkHttpEngine(TuSdkConfigs paramTuSdkConfigs, String paramString1, Context paramContext, String paramString2, int paramInt)
  {
    this.f = paramContext;
    this.j = paramString1;
    this.l = paramString2;
    this.m = paramInt;
    a(paramString1);
  }
  
  private void a(String paramString)
  {
    this.g = new TuSdkHttp(this.m);
    this.g.setEnableRedirct(true);
    this.g.setMaxConnections(2);
    this.g.addHeader("x-client-identifier", "android");
    this.g.addHeader("uuid", uniqueDeviceID());
    if (paramString != null) {
      this.g.addHeader("x-client-dev", paramString);
    }
    b();
  }
  
  private void b()
  {
    StringBuilder localStringBuilder = new StringBuilder(String.format("%s:%s", new Object[] { Integer.valueOf(24), StringHelper.Base64Encode("3.1.1") }));
    localStringBuilder.append(String.format("|%s:%s", new Object[] { Integer.valueOf(40), StringHelper.Base64Encode(TuSdkDeviceInfo.getModel()) }));
    localStringBuilder.append(String.format("|%s:%s", new Object[] { Integer.valueOf(56), StringHelper.Base64Encode(TuSdkDeviceInfo.getOSVersion()) }));
    localStringBuilder.append(String.format("|%s:%s", new Object[] { Integer.valueOf(72), StringHelper.Base64Encode(this.f.getPackageName()) }));
    localStringBuilder.append(String.format("|%s:%s", new Object[] { Integer.valueOf(88), StringHelper.Base64Encode(ContextUtils.getVersionName(this.f)) }));
    localStringBuilder.append(String.format("|%s:%s", new Object[] { Integer.valueOf(120), StringHelper.Base64Encode(c()) }));
    localStringBuilder.append(String.format("|%s:%s", new Object[] { Integer.valueOf(136), StringHelper.Base64Encode(TuSdkDeviceInfo.getIMSI()) }));
    localStringBuilder.append(String.format("|%s:%s", new Object[] { Integer.valueOf(152), StringHelper.Base64Encode(TuSdkDeviceInfo.getIMEI()) }));
    localStringBuilder.append(String.format("|%s:%s", new Object[] { Integer.valueOf(264), StringHelper.Base64Encode(TuSdkDeviceInfo.getMac()) }));
    localStringBuilder.append(String.format("|%s:%s", new Object[] { Integer.valueOf(280), StringHelper.Base64Encode(TuSdkDeviceInfo.getVender()) }));
    localStringBuilder.append(String.format("|%s:%s", new Object[] { Integer.valueOf(296), StringHelper.Base64Encode(TuSdkDeviceInfo.getIP()) }));
    localStringBuilder.append(String.format("|%s:%s", new Object[] { Integer.valueOf(312), StringHelper.Base64Encode(TuSdkDeviceInfo.getAndroidID()) }));
    this.a = ((TuSdkDeviceInfo.getLocation() != null) && (!TuSdkDeviceInfo.getLocation().isEmpty()));
    localStringBuilder.append(String.format("|%s:%s", new Object[] { Integer.valueOf(328), StringHelper.Base64Encode(TuSdkDeviceInfo.getLocation()) }));
    if (SdkValid.shared.appType() > 0)
    {
      str = String.format("|%s:%s", new Object[] { Integer.valueOf(376), StringHelper.Base64Encode(String.format("%s", new Object[] { Integer.valueOf(SdkValid.shared.appType()) })) });
      localStringBuilder.append(str);
    }
    String str = StringHelper.Base64Encode(localStringBuilder.toString());
    this.g.addHeader("x-client-bundle", str);
  }
  
  public RequestHandle get(String paramString, boolean paramBoolean, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    if (!this.a) {
      b();
    }
    return get(paramString, null, paramBoolean, paramResponseHandlerInterface);
  }
  
  public RequestHandle get(String paramString, TuSdkHttpParams paramTuSdkHttpParams, boolean paramBoolean, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    if (!this.a) {
      b();
    }
    paramString = urlBuild(paramString, paramBoolean);
    return a().get(paramString, paramTuSdkHttpParams, paramResponseHandlerInterface);
  }
  
  public RequestHandle post(String paramString, boolean paramBoolean, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    if (!this.a) {
      b();
    }
    return post(paramString, null, paramBoolean, paramResponseHandlerInterface);
  }
  
  public RequestHandle post(String paramString, TuSdkHttpParams paramTuSdkHttpParams, boolean paramBoolean, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    if (!this.a) {
      b();
    }
    paramString = urlBuild(paramString, paramBoolean);
    return a().post(paramString, paramTuSdkHttpParams, paramResponseHandlerInterface);
  }
  
  public RequestHandle postService(String paramString, TuSdkHttpParams paramTuSdkHttpParams, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    if (!this.a) {
      b();
    }
    paramString = serviceUrlBuild(paramString, paramTuSdkHttpParams, true);
    return a().post(paramString, paramTuSdkHttpParams, paramResponseHandlerInterface);
  }
  
  protected String urlBuild(String paramString, boolean paramBoolean)
  {
    if (paramString == null) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder(this.l).append(paramString);
    if (paramBoolean) {
      a(localStringBuilder, paramString);
    }
    b(localStringBuilder, paramString);
    return localStringBuilder.toString();
  }
  
  private StringBuilder a(StringBuilder paramStringBuilder, String paramString)
  {
    if (paramStringBuilder == null) {
      return null;
    }
    long l1 = Calendar.getInstance().getTimeInMillis() / 1000000L * 1000L;
    String str = StringHelper.md5(paramString + "_" + uniqueDeviceID() + "_" + l1);
    if (paramStringBuilder.indexOf("?") == -1) {
      paramStringBuilder.append("?");
    } else {
      paramStringBuilder.append("&");
    }
    paramStringBuilder.append("hash=").append(str);
    paramStringBuilder.append("&t=").append(l1);
    return paramStringBuilder;
  }
  
  protected String serviceUrlBuild(String paramString, TuSdkHttpParams paramTuSdkHttpParams, boolean paramBoolean)
  {
    if (paramString == null) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder(this.l).append(paramString);
    if ((paramBoolean) && (localStringBuilder != null))
    {
      long l1 = Calendar.getInstance().getTimeInMillis() / 1000000L * 1000L;
      String str = StringHelper.md5(this.j);
      paramTuSdkHttpParams.add("pid", str);
      paramTuSdkHttpParams.add("t", String.valueOf(l1));
      paramTuSdkHttpParams.add("sign", StringHelper.md5(paramTuSdkHttpParams.toPairString() + this.j));
    }
    return localStringBuilder.toString();
  }
  
  public RequestHandle postService(String paramString, boolean paramBoolean, TuSdkHttpParams paramTuSdkHttpParams, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    if (paramBoolean) {
      paramString = serviceUrlBuild(paramString, paramTuSdkHttpParams, true);
    }
    return a().post(paramString, paramTuSdkHttpParams, paramResponseHandlerInterface);
  }
  
  private StringBuilder b(StringBuilder paramStringBuilder, String paramString)
  {
    if (paramStringBuilder == null) {
      return null;
    }
    String str = this.f.getPackageName();
    if (paramStringBuilder.indexOf("?") == -1) {
      paramStringBuilder.append("?");
    } else {
      paramStringBuilder.append("&");
    }
    paramStringBuilder.append("app=").append(str);
    return paramStringBuilder;
  }
  
  public String getWebUrl(String paramString, boolean paramBoolean)
  {
    if (paramString == null) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder(WEB_DOMAIN);
    localStringBuilder.append(paramString);
    b(localStringBuilder, paramString);
    if (!paramBoolean) {
      return localStringBuilder.toString();
    }
    if (localStringBuilder.indexOf("?") == -1) {
      localStringBuilder.append("?");
    } else {
      localStringBuilder.append("&");
    }
    localStringBuilder.append("uuid=").append(uniqueDeviceID());
    if (this.j != null) {
      localStringBuilder.append("&devid=").append(this.j);
    }
    if (this.k != null) {
      localStringBuilder.append("&uid=").append(this.k);
    }
    localStringBuilder.append("&v=").append(11);
    a(localStringBuilder, paramString);
    return localStringBuilder.toString();
  }
  
  public String uniqueDeviceID()
  {
    if (this.h != null) {
      return this.h;
    }
    this.h = TuSdkContext.sharedPreferences().loadSharedCache("TUSDK_DeviceUUID");
    if (this.h != null) {
      return this.h;
    }
    long l1 = Calendar.getInstance().getTimeInMillis() / 1000000L * 1000L;
    this.h = StringHelper.md5(StringHelper.uuid() + "_" + l1);
    TuSdkContext.sharedPreferences().saveSharedCache("TUSDK_DeviceUUID", this.h);
    return this.h;
  }
  
  public String getDevId()
  {
    return this.j;
  }
  
  private String c()
  {
    if (this.i != null) {
      return this.i;
    }
    this.i = d();
    if (this.i == null)
    {
      this.i = uniqueDeviceID();
      b(this.i);
    }
    return this.i;
  }
  
  private String d()
  {
    File localFile1 = FileHelper.getExternalStoragePublicDirectory(null);
    if (localFile1 == null) {
      return null;
    }
    File localFile2 = new File(localFile1, "TuSDK/global.keystore");
    byte[] arrayOfByte = FileHelper.readFile(localFile2);
    if (arrayOfByte == null) {
      return null;
    }
    String str = null;
    try
    {
      str = new String(arrayOfByte, "UTF-8");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      return null;
    }
    return str.trim();
  }
  
  private void b(String paramString)
  {
    File localFile1 = FileHelper.getExternalStoragePublicDirectory(null);
    if (localFile1 == null) {
      return;
    }
    File localFile2 = new File(localFile1, "TuSDK");
    localFile2.mkdirs();
    localFile2 = new File(localFile1, "TuSDK/global.keystore");
    try
    {
      FileHelper.saveFile(localFile2, paramString.getBytes("UTF-8"));
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
  }
  
  protected void overseeNetwork()
  {
    if (this.n != null) {
      return;
    }
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
    this.n = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        TuSdkHttpEngine.this.onNetworkStateChanged(HardwareHelper.isNetworkAvailable(paramAnonymousContext));
      }
    };
    this.f.registerReceiver(this.n, localIntentFilter);
  }
  
  protected void cancelOverseeNetwork()
  {
    if (this.n != null) {
      this.f.unregisterReceiver(this.n);
    }
    this.n = null;
  }
  
  protected void onNetworkStateChanged(boolean paramBoolean)
  {
    TLog.d("connected: %s", new Object[] { Boolean.valueOf(paramBoolean) });
  }
  
  static
  {
    switch (2)
    {
    case 2: 
      NETWORK_DOMAIN = "api.tusdk.com";
      NETWORK_WEB_DOMAIN = "m.tusdk.com";
      NETWORK_AUTH_DOMAIN = "auth.tusdk.com";
      NETWORK_PORT = 80;
      SERVICE_DOMAIN = "srv2.tusdk.com";
      SERVICE_PORT = 80;
      DEBUG = false;
      break;
    case 1: 
      NETWORK_DOMAIN = "10.10.10.25";
      NETWORK_WEB_DOMAIN = "m.tusdk.com";
      NETWORK_AUTH_DOMAIN = "auth.tusdk.com";
      NETWORK_PORT = 80;
      SERVICE_DOMAIN = "srv2.tusdk.com";
      SERVICE_PORT = 80;
      DEBUG = false;
      break;
    default: 
      NETWORK_DOMAIN = "192.168.199.152:80";
      NETWORK_WEB_DOMAIN = "192.168.199.152:80";
      NETWORK_AUTH_DOMAIN = "auth.tusdk.com";
      NETWORK_PORT = 80;
      SERVICE_DOMAIN = "192.168.199.152:80";
      SERVICE_PORT = 80;
      DEBUG = true;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\TuSdkHttpEngine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */