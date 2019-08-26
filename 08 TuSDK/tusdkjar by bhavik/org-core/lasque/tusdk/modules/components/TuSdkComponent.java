package org.lasque.tusdk.modules.components;

import android.app.Activity;
import android.support.v4.app.Fragment;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkIntent;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.activity.ActivityHelper;
import org.lasque.tusdk.core.activity.ActivityObserver;
import org.lasque.tusdk.core.type.ActivityAnimType;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper.AlertDelegate;
import org.lasque.tusdk.impl.activity.TuFragment;

public abstract class TuSdkComponent
  implements TuSdkComponentErrorListener
{
  private Activity a;
  private TuSdkComponentDelegate b;
  private boolean c;
  
  public Activity activity()
  {
    return this.a;
  }
  
  public TuSdkComponentDelegate getDelegate()
  {
    return this.b;
  }
  
  public void setDelegate(TuSdkComponentDelegate paramTuSdkComponentDelegate)
  {
    this.b = paramTuSdkComponentDelegate;
  }
  
  public boolean isAutoDismissWhenCompleted()
  {
    return this.c;
  }
  
  public TuSdkComponent setAutoDismissWhenCompleted(boolean paramBoolean)
  {
    this.c = paramBoolean;
    return this;
  }
  
  public TuSdkComponent(Activity paramActivity)
  {
    this.a = paramActivity;
    initComponent();
  }
  
  protected abstract void initComponent();
  
  public abstract TuSdkComponent showComponent();
  
  protected void notifyResult(TuSdkResult paramTuSdkResult, Error paramError, TuFragment paramTuFragment)
  {
    if ((isAutoDismissWhenCompleted()) && (paramTuFragment != null)) {
      paramTuFragment.dismissActivityWithAnim();
    }
    if (getDelegate() == null) {
      return;
    }
    getDelegate().onComponentFinished(paramTuSdkResult, paramError, paramTuFragment);
  }
  
  public void onComponentError(TuFragment paramTuFragment, TuSdkResult paramTuSdkResult, Error paramError)
  {
    notifyResult(paramTuSdkResult, paramError, paramTuFragment);
  }
  
  public boolean showAlertIfCannotSaveFile()
  {
    if (this.a == null) {
      return true;
    }
    String str = null;
    ComponentErrorType localComponentErrorType = null;
    if (!FileHelper.mountedExternalStorage())
    {
      str = getResString("lsq_save_not_found_sdcard");
      localComponentErrorType = ComponentErrorType.TypeNotFoundSDCard;
    }
    else if (!FileHelper.hasAvailableExternal(this.a))
    {
      str = getResString("lsq_save_insufficient_storage_space");
      localComponentErrorType = ComponentErrorType.TypeStorageSpace;
    }
    if (localComponentErrorType == null) {
      return false;
    }
    TuSdkViewHelper.alert(this.a, getResString("lsq_save_unsupport_storage_title"), str, getResString("lsq_button_done"));
    onComponentError(null, null, localComponentErrorType.getError(this));
    return true;
  }
  
  public void presentActivity(TuSdkIntent paramTuSdkIntent, boolean paramBoolean)
  {
    ActivityHelper.presentActivity(this.a, paramTuSdkIntent, ActivityObserver.ins.getAnimPresent(), paramBoolean);
  }
  
  public void presentModalNavigationActivity(Fragment paramFragment)
  {
    presentModalNavigationActivity(paramFragment, false);
  }
  
  public void presentModalNavigationActivity(Fragment paramFragment, boolean paramBoolean)
  {
    presentModalNavigationActivity(paramFragment, ActivityObserver.ins.getAnimPresent(), ActivityObserver.ins.getAnimDismiss(), paramBoolean);
  }
  
  public void pushModalNavigationActivity(Fragment paramFragment)
  {
    pushModalNavigationActivity(paramFragment, false);
  }
  
  public void pushModalNavigationActivity(Fragment paramFragment, boolean paramBoolean)
  {
    presentModalNavigationActivity(paramFragment, ActivityObserver.ins.getAnimPush(), ActivityObserver.ins.getAnimPop(), paramBoolean);
  }
  
  public void presentModalNavigationActivity(Fragment paramFragment, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean)
  {
    ActivityHelper.presentActivity(this.a, ActivityObserver.ins.getMainActivityClazz(), paramFragment, paramActivityAnimType1, paramActivityAnimType2, paramBoolean, false, false);
  }
  
  public void alert(TuSdkViewHelper.AlertDelegate paramAlertDelegate, int paramInt1, int paramInt2)
  {
    alert(paramAlertDelegate, getResString(paramInt1), getResString(paramInt2));
  }
  
  public void alert(TuSdkViewHelper.AlertDelegate paramAlertDelegate, String paramString1, String paramString2)
  {
    TuSdkViewHelper.alert(paramAlertDelegate, this.a, paramString1, paramString2, getResString("lsq_nav_cancel"), getResString("lsq_button_done"));
  }
  
  public String getResString(int paramInt)
  {
    return ContextUtils.getResString(this.a, paramInt);
  }
  
  public String getResString(String paramString)
  {
    return TuSdkContext.getString(paramString);
  }
  
  public static abstract interface TuSdkComponentDelegate
  {
    public abstract void onComponentFinished(TuSdkResult paramTuSdkResult, Error paramError, TuFragment paramTuFragment);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\TuSdkComponent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */