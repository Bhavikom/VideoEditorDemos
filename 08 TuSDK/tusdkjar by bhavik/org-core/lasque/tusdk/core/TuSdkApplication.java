package org.lasque.tusdk.core;

import android.app.Application;
import android.content.Context;

public class TuSdkApplication
  extends Application
{
  private boolean a;
  
  public boolean isEnableLog()
  {
    return this.a;
  }
  
  public void setEnableLog(boolean paramBoolean)
  {
    this.a = paramBoolean;
    TuSdk.enableDebugLog(this.a);
  }
  
  public void onCreate()
  {
    TuSdkCrashException.bindExceptionHandler(getApplicationContext());
    super.onCreate();
  }
  
  protected void initPreLoader(Context paramContext, String paramString)
  {
    initPreLoader(paramContext, paramString, null);
  }
  
  protected void initPreLoader(Context paramContext, String paramString1, String paramString2)
  {
    TuSdk.init(paramContext, paramString1, paramString2);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\TuSdkApplication.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */