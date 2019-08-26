package org.lasque.tusdk.impl.activity;

import android.support.v4.app.Fragment;
import java.util.Hashtable;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkIntent;
import org.lasque.tusdk.core.activity.ActivityObserver;
import org.lasque.tusdk.core.activity.TuSdkFragmentActivity;
import org.lasque.tusdk.core.type.ActivityAnimType;

public class TuFragmentActivity
  extends TuSdkFragmentActivity
{
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_activity_fragment_context_layout");
  }
  
  protected void initActivity()
  {
    super.initActivity();
    setRootView(getLayoutId(), TuSdkContext.getIDResId("lsq_fragment_container"));
    setfragmentChangeAnim(ActivityObserver.ins.getAnimPush(), ActivityObserver.ins.getAnimPop());
  }
  
  protected ActivityAnimType getAnimType(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    ActivityAnimType localActivityAnimType = (ActivityAnimType)ActivityObserver.ins.getActivityAnims().get(paramString);
    return localActivityAnimType;
  }
  
  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
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
    presentModalNavigationActivity(paramFragment, ActivityObserver.ins.getAnimPush(), ActivityObserver.ins.getAnimPop(), paramBoolean);
  }
  
  public void presentModalNavigationActivity(Fragment paramFragment, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean)
  {
    presentModalNavigationActivity(ActivityObserver.ins.getMainActivityClazz(), paramFragment, paramActivityAnimType1, paramActivityAnimType2, paramBoolean);
  }
  
  public void filpModalNavigationActivity(Fragment paramFragment, boolean paramBoolean1, boolean paramBoolean2)
  {
    filpModalNavigationActivity(ActivityObserver.ins.getMainActivityClazz(), paramFragment, paramBoolean1, paramBoolean2);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\activity\TuFragmentActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */