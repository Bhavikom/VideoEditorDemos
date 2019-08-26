package org.lasque.tusdk.impl.view.widget;

import android.content.Context;

public class TuMessageHubImpl
  implements TuMessageHubInterface
{
  public void setStatus(Context paramContext, String paramString)
  {
    try
    {
      TuProgressHub.setStatus(paramContext, paramString);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void setStatus(Context paramContext, int paramInt)
  {
    try
    {
      TuProgressHub.setStatus(paramContext, paramInt);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void showToast(Context paramContext, String paramString)
  {
    try
    {
      TuProgressHub.showToast(paramContext, paramString);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void showToast(Context paramContext, int paramInt)
  {
    try
    {
      TuProgressHub.showToast(paramContext, paramInt);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void showSuccess(Context paramContext, String paramString)
  {
    try
    {
      TuProgressHub.showSuccess(paramContext, paramString);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void showSuccess(Context paramContext, int paramInt)
  {
    try
    {
      TuProgressHub.showSuccess(paramContext, paramInt);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void showError(Context paramContext, String paramString)
  {
    try
    {
      TuProgressHub.showError(paramContext, paramString);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void showError(Context paramContext, int paramInt)
  {
    try
    {
      TuProgressHub.showError(paramContext, paramInt);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void dismiss()
  {
    try
    {
      
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void dismissRightNow()
  {
    try
    {
      
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void applyToViewWithNavigationBarHidden(boolean paramBoolean)
  {
    TuProgressHub.applyToViewWithNavigationBarHidden(paramBoolean);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\TuMessageHubImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */