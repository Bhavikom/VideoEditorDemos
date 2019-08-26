package org.lasque.tusdk.impl.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper.AlertDelegate;
import org.lasque.tusdk.modules.components.ComponentErrorType;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;

public abstract class TuComponentFragment
  extends TuFragment
{
  private TuSdkComponentErrorListener a;
  protected TuSdkViewHelper.AlertDelegate permissionAlertDelegate = new TuSdkViewHelper.AlertDelegate()
  {
    public void onAlertConfirm(AlertDialog paramAnonymousAlertDialog)
    {
      Intent localIntent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", TuComponentFragment.this.getContext().getPackageName(), null));
      localIntent.addFlags(268435456);
      TuComponentFragment.this.startActivity(localIntent);
    }
    
    public void onAlertCancel(AlertDialog paramAnonymousAlertDialog)
    {
      TuComponentFragment.this.dismissActivityWithAnim();
    }
  };
  
  public TuSdkComponentErrorListener getErrorListener()
  {
    return this.a;
  }
  
  public void setErrorListener(TuSdkComponentErrorListener paramTuSdkComponentErrorListener)
  {
    this.a = paramTuSdkComponentErrorListener;
  }
  
  protected void notifyError(final TuSdkResult paramTuSdkResult, final ComponentErrorType paramComponentErrorType)
  {
    if ((paramComponentErrorType == null) || (getErrorListener() == null)) {
      return;
    }
    if (!ThreadHelper.isMainThread())
    {
      new Handler(Looper.getMainLooper()).post(new Runnable()
      {
        public void run()
        {
          TuComponentFragment.this.notifyError(paramTuSdkResult, paramComponentErrorType);
        }
      });
      return;
    }
    getErrorListener().onComponentError(this, paramTuSdkResult, paramComponentErrorType.getError(this));
  }
  
  @TargetApi(23)
  protected String[] getRequiredPermissions()
  {
    String[] arrayOfString = { "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.ACCESS_FINE_LOCATION" };
    return arrayOfString;
  }
  
  public int getRequestPermissionCode()
  {
    return 1;
  }
  
  @TargetApi(23)
  public boolean hasRequiredPermission()
  {
    if (Build.VERSION.SDK_INT < 23) {
      return true;
    }
    String[] arrayOfString1 = getRequiredPermissions();
    if ((arrayOfString1 != null) && (arrayOfString1.length > 0)) {
      for (String str : arrayOfString1) {
        if (getActivity().checkSelfPermission(str) != 0) {
          if (str == "android.permission.ACCESS_FINE_LOCATION") {
            TLog.d("Access to fine location has been denied", new Object[0]);
          } else {
            return false;
          }
        }
      }
    }
    return true;
  }
  
  public void requestRequiredPermissions()
  {
    String[] arrayOfString = getRequiredPermissions();
    if ((arrayOfString != null) && (arrayOfString.length > 0)) {
      requestPermissions(arrayOfString, getRequestPermissionCode());
    }
  }
  
  public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt)
  {
    super.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfInt);
    if (paramInt == getRequestPermissionCode())
    {
      boolean bool = true;
      for (int k : paramArrayOfInt) {
        if (k != 0)
        {
          bool = false;
          break;
        }
      }
      bool = hasRequiredPermission();
      onPermissionGrantedResult(bool);
    }
  }
  
  protected void onPermissionGrantedResult(boolean paramBoolean) {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\activity\TuComponentFragment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */