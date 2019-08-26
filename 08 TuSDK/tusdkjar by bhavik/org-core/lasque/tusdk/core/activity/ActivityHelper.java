package org.lasque.tusdk.core.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import org.lasque.tusdk.core.TuSdkIntent;
import org.lasque.tusdk.core.type.ActivityAnimType;
import org.lasque.tusdk.core.utils.ReflectUtils;

public class ActivityHelper
{
  public static void dismissActivity(Activity paramActivity)
  {
    if (paramActivity != null) {
      paramActivity.finish();
    }
  }
  
  public static void dismissActivityWithAnim(Activity paramActivity, ActivityAnimType paramActivityAnimType)
  {
    if (paramActivity == null) {
      return;
    }
    dismissActivity(paramActivity);
    if (paramActivityAnimType != null) {
      paramActivity.overridePendingTransition(paramActivityAnimType.getEnterAnim(), paramActivityAnimType.getExitAnim());
    } else {
      paramActivity.overridePendingTransition(0, 0);
    }
  }
  
  public static TuSdkIntent buildModalActivityIntent(Activity paramActivity, Class<?> paramClass, Fragment paramFragment, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramClass == null) || (paramActivity == null)) {
      return null;
    }
    TuSdkIntent localTuSdkIntent = new TuSdkIntent(paramActivity, paramClass);
    localTuSdkIntent.setWantFullScreen(paramBoolean1);
    localTuSdkIntent.setActivityFilp(paramBoolean2);
    if (paramFragment != null)
    {
      ActivityObserver.ins.setTransmit(paramFragment);
      localTuSdkIntent.needTransmitFragment();
    }
    localTuSdkIntent.setActivityPresentAnimType(paramActivityAnimType1);
    localTuSdkIntent.setActivityDismissAnimType(paramActivityAnimType2);
    return localTuSdkIntent;
  }
  
  public static void presentActivity(Activity paramActivity, TuSdkIntent paramTuSdkIntent, ActivityAnimType paramActivityAnimType, boolean paramBoolean)
  {
    if ((paramTuSdkIntent == null) || (paramActivity == null)) {
      return;
    }
    paramActivity.startActivity(paramTuSdkIntent);
    if (paramBoolean) {
      dismissActivityWithAnim(paramActivity, paramActivityAnimType);
    } else if (paramActivityAnimType != null) {
      paramActivity.overridePendingTransition(paramActivityAnimType.getEnterAnim(), paramActivityAnimType.getExitAnim());
    }
  }
  
  public static void presentActivity(Activity paramActivity, Class<?> paramClass, ActivityAnimType paramActivityAnimType, boolean paramBoolean)
  {
    presentActivity(paramActivity, paramClass, paramActivityAnimType, false, false, paramBoolean);
  }
  
  public static void presentActivity(Activity paramActivity, Class<?> paramClass, ActivityAnimType paramActivityAnimType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    TuSdkIntent localTuSdkIntent = buildModalActivityIntent(paramActivity, paramClass, null, paramActivityAnimType, null, paramBoolean1, paramBoolean2);
    presentActivity(paramActivity, localTuSdkIntent, paramActivityAnimType, paramBoolean3);
  }
  
  public static void presentActivity(Activity paramActivity, Class<?> paramClass, Fragment paramFragment, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    TuSdkIntent localTuSdkIntent = buildModalActivityIntent(paramActivity, paramClass, paramFragment, paramActivityAnimType1, paramActivityAnimType2, paramBoolean1, paramBoolean2);
    presentActivity(paramActivity, localTuSdkIntent, paramActivityAnimType1, paramBoolean3);
  }
  
  public static void presentModalNavigationActivity(Activity paramActivity, Class<?> paramClass, Fragment paramFragment, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean1, boolean paramBoolean2)
  {
    presentActivity(paramActivity, paramClass, paramFragment, paramActivityAnimType1, paramActivityAnimType2, paramBoolean1, false, paramBoolean2);
  }
  
  public static void presentModalNavigationActivity(Activity paramActivity, Class<?> paramClass, Fragment paramFragment, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean)
  {
    presentActivity(paramActivity, paramClass, paramFragment, paramActivityAnimType1, paramActivityAnimType2, paramBoolean, false, false);
  }
  
  public static void presentModalNavigationActivity(Activity paramActivity, Class<?> paramClass1, Class<?> paramClass2, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean)
  {
    Fragment localFragment = (Fragment)ReflectUtils.classInstance(paramClass2);
    presentModalNavigationActivity(paramActivity, paramClass1, localFragment, paramActivityAnimType1, paramActivityAnimType2, paramBoolean);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\activity\ActivityHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */