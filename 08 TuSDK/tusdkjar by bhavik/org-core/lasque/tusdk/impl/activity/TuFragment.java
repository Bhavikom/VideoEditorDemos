package org.lasque.tusdk.impl.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkIntent;
import org.lasque.tusdk.core.activity.ActivityObserver;
import org.lasque.tusdk.core.activity.TuSdkFragment;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.type.ActivityAnimType;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper.AlertDelegate;
import org.lasque.tusdk.core.view.widget.button.TuSdkNavigatorButton;
import org.lasque.tusdk.impl.view.widget.TuMessageHubInterface;
import org.lasque.tusdk.impl.view.widget.TuNavigatorBar.TuNavButtonStyle;
import org.lasque.tusdk.impl.view.widget.button.TuNavigatorButton;

public abstract class TuFragment
  extends TuSdkFragment
{
  protected void initCreateView()
  {
    setNavigatorBarId(TuSdkContext.getIDResId("lsq_navigatorBar"), TuSdkContext.getIDResId("lsq_backButton"), TuNavigatorButton.getLayoutId());
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    if (!SdkValid.shared.sdkValid()) {
      return null;
    }
    return super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
  }
  
  public void onDetach()
  {
    super.onDetach();
  }
  
  public TuSdkNavigatorButton setNavLeftButton(String paramString)
  {
    return setNavLeftButton(paramString, TuNavigatorBar.TuNavButtonStyle.button);
  }
  
  public TuSdkNavigatorButton setNavLeftButton(String paramString, int paramInt)
  {
    TuSdkNavigatorButton localTuSdkNavigatorButton = setNavLeftButton(paramString, TuNavigatorBar.TuNavButtonStyle.button);
    localTuSdkNavigatorButton.setTextColor(paramInt);
    return localTuSdkNavigatorButton;
  }
  
  public TuSdkNavigatorButton setNavLeftButton(int paramInt)
  {
    return setNavLeftButton(getResString(paramInt));
  }
  
  public TuSdkNavigatorButton setNavLeftHighLightButton(String paramString)
  {
    return setNavLeftButton(paramString, TuNavigatorBar.TuNavButtonStyle.highlight);
  }
  
  public TuSdkNavigatorButton setNavLeftHighLightButton(int paramInt)
  {
    return setNavLeftHighLightButton(getResString(paramInt));
  }
  
  public TuSdkNavigatorButton setNavRightButton(String paramString)
  {
    return setNavRightButton(paramString, TuNavigatorBar.TuNavButtonStyle.button);
  }
  
  public TuSdkNavigatorButton setNavRightButton(String paramString, int paramInt)
  {
    TuSdkNavigatorButton localTuSdkNavigatorButton = setNavRightButton(paramString, TuNavigatorBar.TuNavButtonStyle.button);
    localTuSdkNavigatorButton.setTextColor(paramInt);
    return localTuSdkNavigatorButton;
  }
  
  public TuSdkNavigatorButton setNavRightButton(int paramInt)
  {
    return setNavRightButton(getResString(paramInt));
  }
  
  public TuSdkNavigatorButton setNavRightHighLightButton(String paramString)
  {
    return setNavRightButton(paramString, TuNavigatorBar.TuNavButtonStyle.highlight);
  }
  
  public TuSdkNavigatorButton setNavRightHighLightButton(int paramInt)
  {
    return setNavRightHighLightButton(getResString(paramInt));
  }
  
  public void presentActivity(TuSdkIntent paramTuSdkIntent, boolean paramBoolean)
  {
    presentActivity(paramTuSdkIntent, ActivityObserver.ins.getAnimPresent(), paramBoolean);
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
    presentModalNavigationActivity(paramFragment, ActivityObserver.ins.getAnimPresent(), ActivityObserver.ins.getAnimDismiss(), paramBoolean);
  }
  
  public void presentModalNavigationActivity(Fragment paramFragment, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean)
  {
    presentModalNavigationActivity(ActivityObserver.ins.getMainActivityClazz(), paramFragment, paramActivityAnimType1, paramActivityAnimType2, paramBoolean);
  }
  
  public void filpModalNavigationActivity(Fragment paramFragment, boolean paramBoolean1, boolean paramBoolean2)
  {
    filpModalNavigationActivity(ActivityObserver.ins.getMainActivityClazz(), paramFragment, paramBoolean1, paramBoolean2);
  }
  
  public void filpModalNavigationActivity(Fragment paramFragment, boolean paramBoolean)
  {
    filpModalNavigationActivity(paramFragment, false, paramBoolean);
  }
  
  public void alert(TuSdkViewHelper.AlertDelegate paramAlertDelegate, int paramInt1, int paramInt2)
  {
    alert(paramAlertDelegate, getResString(paramInt1), getResString(paramInt2));
  }
  
  public void alert(TuSdkViewHelper.AlertDelegate paramAlertDelegate, String paramString1, String paramString2)
  {
    TuSdkViewHelper.alert(paramAlertDelegate, getActivity(), paramString1, paramString2, TuSdkContext.getString("lsq_nav_cancel"), TuSdkContext.getString("lsq_button_done"));
  }
  
  public void hubStatus(String paramString)
  {
    TuSdk.messageHub().setStatus(getActivity(), paramString);
  }
  
  public void hubStatus(int paramInt)
  {
    TuSdk.messageHub().setStatus(getActivity(), paramInt);
  }
  
  public void hubSuccess(String paramString)
  {
    TuSdk.messageHub().showSuccess(getActivity(), paramString);
  }
  
  public void hubSuccess(int paramInt)
  {
    TuSdk.messageHub().showSuccess(getActivity(), paramInt);
  }
  
  public void hubError(String paramString)
  {
    TuSdk.messageHub().showError(getActivity(), paramString);
  }
  
  public void hubError(int paramInt)
  {
    TuSdk.messageHub().showError(getActivity(), paramInt);
  }
  
  public void hubDismiss()
  {
    TuSdk.messageHub().dismiss();
  }
  
  public void hubDismissRightNow()
  {
    TuSdk.messageHub().dismissRightNow();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\activity\TuFragment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */