package org.lasque.tusdk.core.struct;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class ViewSize
  extends TuSdkSize
{
  public static ViewSize create(View paramView)
  {
    return new ViewSize(paramView);
  }
  
  public ViewSize() {}
  
  public ViewSize(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
  }
  
  public ViewSize(View paramView)
  {
    if (paramView == null) {
      return;
    }
    this.width = paramView.getWidth();
    this.height = paramView.getHeight();
    ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    if (localLayoutParams == null) {
      return;
    }
    if (this.width < 1) {
      this.width = localLayoutParams.width;
    }
    if (this.height < 1) {
      this.height = localLayoutParams.height;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\struct\ViewSize.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */