package org.lasque.tusdk.impl.view.widget;

import android.content.Context;

public abstract interface TuMessageHubInterface
{
  public abstract void setStatus(Context paramContext, String paramString);
  
  public abstract void setStatus(Context paramContext, int paramInt);
  
  public abstract void showToast(Context paramContext, String paramString);
  
  public abstract void showToast(Context paramContext, int paramInt);
  
  public abstract void showSuccess(Context paramContext, String paramString);
  
  public abstract void showSuccess(Context paramContext, int paramInt);
  
  public abstract void showError(Context paramContext, String paramString);
  
  public abstract void showError(Context paramContext, int paramInt);
  
  public abstract void dismiss();
  
  public abstract void dismissRightNow();
  
  public abstract void applyToViewWithNavigationBarHidden(boolean paramBoolean);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\TuMessageHubInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */