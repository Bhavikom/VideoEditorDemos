package org.lasque.tusdk.impl.view.widget;

import android.content.Context;
import org.lasque.tusdk.core.view.widget.TuSdkProgressHub;
import org.lasque.tusdk.core.view.widget.TuSdkProgressHubView.TuSdkHubViewShowType;

public class TuProgressHub
  extends TuSdkProgressHub
{
  public static final TuProgressHub ins = new TuProgressHub();
  
  public static void setStatus(Context paramContext, String paramString)
  {
    ins.showHubView(paramContext, TuSdkProgressHubView.TuSdkHubViewShowType.TypeDefault, paramString, 0, 0, 0L);
  }
  
  public static void setStatus(Context paramContext, int paramInt)
  {
    ins.showHubView(paramContext, TuSdkProgressHubView.TuSdkHubViewShowType.TypeDefault, null, paramInt, 0, 0L);
  }
  
  public static void showToast(Context paramContext, String paramString)
  {
    ins.showHubView(paramContext, TuSdkProgressHubView.TuSdkHubViewShowType.TypeImage, paramString, 0, 0, 1000L);
  }
  
  public static void showToast(Context paramContext, int paramInt)
  {
    ins.showHubView(paramContext, TuSdkProgressHubView.TuSdkHubViewShowType.TypeImage, null, paramInt, 0, 1000L);
  }
  
  public static void showSuccess(Context paramContext, String paramString)
  {
    ins.showHubView(paramContext, TuSdkProgressHubView.TuSdkHubViewShowType.TypeSucceed, paramString, 0, 0, 1000L);
  }
  
  public static void showSuccess(Context paramContext, int paramInt)
  {
    ins.showHubView(paramContext, TuSdkProgressHubView.TuSdkHubViewShowType.TypeSucceed, null, paramInt, 0, 1000L);
  }
  
  public static void showError(Context paramContext, String paramString)
  {
    ins.showHubView(paramContext, TuSdkProgressHubView.TuSdkHubViewShowType.TypeFailed, paramString, 0, 0, 1000L);
  }
  
  public static void showError(Context paramContext, int paramInt)
  {
    ins.showHubView(paramContext, TuSdkProgressHubView.TuSdkHubViewShowType.TypeFailed, null, paramInt, 0, 1000L);
  }
  
  public static void showImage(Context paramContext, int paramInt, String paramString)
  {
    ins.showHubView(paramContext, TuSdkProgressHubView.TuSdkHubViewShowType.TypeImage, paramString, 0, paramInt, 0L);
  }
  
  public static void showImage(Context paramContext, int paramInt1, int paramInt2)
  {
    ins.showHubView(paramContext, TuSdkProgressHubView.TuSdkHubViewShowType.TypeImage, null, paramInt2, paramInt1, 0L);
  }
  
  public static void dismiss()
  {
    ins.dismissHub(true);
  }
  
  public static void dismissRightNow()
  {
    ins.dismissHub(false);
  }
  
  public static boolean isVisible()
  {
    return ins.existHubView();
  }
  
  public int getHubLayoutId()
  {
    return TuProgressHubView.getLayoutId();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\TuProgressHub.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */