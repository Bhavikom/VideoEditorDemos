package org.lasque.tusdk.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import org.lasque.tusdk.core.type.ActivityAnimType;

public class TuSdkIntent
  extends Intent
{
  public static final String WANT_FULL_SCREEN_KEY = "wantFullScreen";
  public static final String FRAGMENT_CLASS = "fragmentClass";
  public static final String FRAGMENT_TRANSMITS = "fragmentTransmit";
  public static final String ACTIVITY_PRESENT_ANIMTYPE = "activityPresentAnimType";
  public static final String ACTIVITY_DISMISS_ANIMTYPE = "activityDismissAnimType";
  public static final String ACTIVITY_FILP_ACTION = "activityFilpAction";
  
  public TuSdkIntent() {}
  
  public TuSdkIntent(Intent paramIntent)
  {
    super(paramIntent);
  }
  
  public TuSdkIntent(String paramString)
  {
    super(paramString);
  }
  
  public TuSdkIntent(String paramString, Uri paramUri)
  {
    super(paramString, paramUri);
  }
  
  public TuSdkIntent(Context paramContext, Class<?> paramClass)
  {
    super(paramContext, paramClass);
  }
  
  public TuSdkIntent(String paramString, Uri paramUri, Context paramContext, Class<?> paramClass)
  {
    super(paramString, paramUri, paramContext, paramClass);
  }
  
  public void setWantFullScreen(boolean paramBoolean)
  {
    putExtra("wantFullScreen", paramBoolean);
  }
  
  public boolean getWantFullScreen()
  {
    return getBooleanExtra("wantFullScreen", false);
  }
  
  public void setFragmentClass(Class<?> paramClass)
  {
    if (paramClass == null) {
      return;
    }
    putExtra("fragmentClass", paramClass.getName());
  }
  
  public void needTransmitFragment()
  {
    putExtra("fragmentTransmit", true);
  }
  
  public void setActivityPresentAnimType(ActivityAnimType paramActivityAnimType)
  {
    if (paramActivityAnimType == null) {
      return;
    }
    putExtra("activityPresentAnimType", paramActivityAnimType.name());
  }
  
  public void setActivityDismissAnimType(ActivityAnimType paramActivityAnimType)
  {
    if (paramActivityAnimType == null) {
      return;
    }
    putExtra("activityDismissAnimType", paramActivityAnimType.name());
  }
  
  public void setActivityFilp(boolean paramBoolean)
  {
    putExtra("activityFilpAction", paramBoolean);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\TuSdkIntent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */