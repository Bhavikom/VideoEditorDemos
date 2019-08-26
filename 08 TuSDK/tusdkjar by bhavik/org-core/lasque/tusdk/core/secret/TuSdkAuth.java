package org.lasque.tusdk.core.secret;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.lasque.tusdk.core.TuSdkConfigs;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.http.ResponseHandlerInterface;
import org.lasque.tusdk.core.network.TuSdkAuthInfo;
import org.lasque.tusdk.core.network.TuSdkHttpEngine;
import org.lasque.tusdk.core.network.TuSdkHttpHandler;
import org.lasque.tusdk.core.network.TuSdkHttpParams;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.TuSdkSharedPreferences;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;
import org.lasque.tusdk.core.utils.json.JsonWrapper;
import org.lasque.tusdk.modules.components.ComponentActType;

public class TuSdkAuth
{
  private static TuSdkAuth a;
  
  public static TuSdkAuth shared()
  {
    if (a == null) {
      a = new TuSdkAuth();
    }
    return a;
  }
  
  public LocalAuthInfo localAuthInfo()
  {
    LocalAuthInfo localLocalAuthInfo = (LocalAuthInfo)TuSdkContext.sharedPreferences().loadSharedCacheObject("tusdk_local_auth_info");
    if (localLocalAuthInfo == null) {
      return new LocalAuthInfo();
    }
    return localLocalAuthInfo;
  }
  
  public void requestRemoteAuthInfo(final AuthInfoCallback paramAuthInfoCallback)
  {
    if (StringHelper.isEmpty(SdkValid.shared.a()))
    {
      TLog.e("app key not found! Please see: http://tusdk.com/docs/android/get-started", new Object[0]);
      if (paramAuthInfoCallback != null) {
        paramAuthInfoCallback.onAuthInfo(null);
      }
      return;
    }
    final LocalAuthInfo localLocalAuthInfo = localAuthInfo();
    if ((localLocalAuthInfo != null) && (!localLocalAuthInfo.canRequestAuthUpdate()))
    {
      if (LocalAuthInfo.a(localLocalAuthInfo) != null)
      {
        localObject1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ((SimpleDateFormat)localObject1).setCalendar(LocalAuthInfo.a(localLocalAuthInfo));
        localObject2 = ((SimpleDateFormat)localObject1).format(LocalAuthInfo.a(localLocalAuthInfo).getTime());
        TLog.i("No auth permissions need to be updated for the time being. The next update permission time is %s ", new Object[] { localObject2 });
      }
      if ((paramAuthInfoCallback != null) && (localLocalAuthInfo != null)) {
        paramAuthInfoCallback.onAuthInfo(localLocalAuthInfo.remoteAuthInfo);
      }
      return;
    }
    Object localObject1 = new TuSdkHttpHandler()
    {
      protected void onRequestedSucceed(TuSdkHttpHandler paramAnonymousTuSdkHttpHandler)
      {
        TuSdkAuthInfo localTuSdkAuthInfo = (TuSdkAuthInfo)paramAnonymousTuSdkHttpHandler.getJson().getJsonWithType(TuSdkAuthInfo.class);
        if ((localTuSdkAuthInfo == null) || (!localTuSdkAuthInfo.isValid()))
        {
          TuSdkAuth.LocalAuthInfo localLocalAuthInfo = new TuSdkAuth.LocalAuthInfo();
          localLocalAuthInfo.b();
          localLocalAuthInfo.persistence();
          if (paramAuthInfoCallback != null) {
            paramAuthInfoCallback.onAuthInfo(localTuSdkAuthInfo);
          }
          return;
        }
        StatisticsManger.appendComponent(ComponentActType.getAppAuthActionSuccess);
        localLocalAuthInfo.remoteAuthInfo = localTuSdkAuthInfo;
        localLocalAuthInfo.a();
        localLocalAuthInfo.persistence();
        if (paramAuthInfoCallback != null) {
          paramAuthInfoCallback.onAuthInfo(localTuSdkAuthInfo);
        }
      }
      
      protected void onRequestedFailed(TuSdkHttpHandler paramAnonymousTuSdkHttpHandler)
      {
        StatisticsManger.appendComponent(ComponentActType.getAppAuthActionFail);
        localLocalAuthInfo.b();
        localLocalAuthInfo.persistence();
        if (paramAuthInfoCallback != null) {
          paramAuthInfoCallback.onAuthInfo(localLocalAuthInfo.remoteAuthInfo);
        }
      }
    };
    Object localObject2 = new TuSdkHttpParams();
    ((TuSdkHttpParams)localObject2).put("app_key", SdkValid.shared.a());
    TuSdkHttpEngine.auth().post("/app/authorize", (TuSdkHttpParams)localObject2, true, (ResponseHandlerInterface)localObject1);
    StatisticsManger.appendComponent(ComponentActType.getUpdatedAppAuthAction);
  }
  
  public static class LocalAuthInfo
    extends JsonBaseBean
    implements Serializable
  {
    public TuSdkAuthInfo remoteAuthInfo;
    public String bundleMasterMD5;
    private GregorianCalendar a;
    
    public boolean canRequestAuthUpdate()
    {
      if (d() <= 0L) {
        return false;
      }
      if (this.a == null) {
        return true;
      }
      String str1 = SdkValid.shared.geTuSdkConfigs().master;
      if ((str1 != null) && (this.bundleMasterMD5 != null))
      {
        String str2 = StringHelper.md5(str1);
        String str3 = this.bundleMasterMD5;
        if (!str3.equalsIgnoreCase(str2))
        {
          TLog.i("An update to the bundle resource has been detected and permission information needs to be requested.", new Object[0]);
          return true;
        }
      }
      return Calendar.getInstance().compareTo(this.a) >= 0;
    }
    
    public void persistence()
    {
      String str = SdkValid.shared.geTuSdkConfigs().master;
      if (str != null) {
        this.bundleMasterMD5 = StringHelper.md5(str);
      }
      TuSdkContext.sharedPreferences().saveSharedCacheObject("tusdk_local_auth_info", this);
    }
    
    void a()
    {
      double d = c();
      GregorianCalendar localGregorianCalendar = new GregorianCalendar();
      if (d > 30.0D)
      {
        localGregorianCalendar.add(2, 1);
        localGregorianCalendar.set(5, 1);
        localGregorianCalendar.set(11, 0);
        localGregorianCalendar.set(12, 0);
        localGregorianCalendar.set(13, 0);
        localGregorianCalendar.set(14, 0);
      }
      else if ((d >= 0.0D) && (d <= 30.0D))
      {
        localGregorianCalendar.add(5, 1);
        localGregorianCalendar.set(11, 0);
        localGregorianCalendar.set(12, 0);
        localGregorianCalendar.set(13, 0);
        localGregorianCalendar.set(14, 0);
      }
      else if (d < 0.0D)
      {
        localGregorianCalendar.add(1, 10);
      }
      this.a = localGregorianCalendar;
    }
    
    void b()
    {
      double d = c();
      GregorianCalendar localGregorianCalendar = new GregorianCalendar();
      if (d >= 0.0D)
      {
        localGregorianCalendar.add(11, 6);
        localGregorianCalendar.set(12, 0);
        localGregorianCalendar.set(13, 0);
        localGregorianCalendar.set(14, 0);
      }
      else
      {
        localGregorianCalendar = new GregorianCalendar();
      }
      this.a = localGregorianCalendar;
    }
    
    private long d()
    {
      if ((SdkValid.shared.isExpired()) && (this.remoteAuthInfo != null) && (this.remoteAuthInfo.service_expire != null)) {
        return this.remoteAuthInfo.service_expire.getTimeInMillis() / 1000L;
      }
      return SdkValid.shared.serviceExpireSeconds();
    }
    
    double c()
    {
      long l1 = d();
      long l2 = new GregorianCalendar().getTimeInMillis() / 1000L;
      long l3 = l1 - l2;
      if (l3 < 0L) {
        return -1.0D;
      }
      double d = l3 / 60L / 60L / 24L;
      return d;
    }
  }
  
  public static abstract interface AuthInfoCallback
  {
    public abstract void onAuthInfo(TuSdkAuthInfo paramTuSdkAuthInfo);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\secret\TuSdkAuth.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */