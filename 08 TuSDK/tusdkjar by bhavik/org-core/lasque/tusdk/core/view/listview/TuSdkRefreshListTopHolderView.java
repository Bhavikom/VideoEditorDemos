package org.lasque.tusdk.core.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout.LayoutParams;
import org.lasque.tusdk.core.view.TuSdkLinearLayout;

public class TuSdkRefreshListTopHolderView
  extends TuSdkLinearLayout
{
  private View a;
  
  public TuSdkRefreshListTopHolderView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkRefreshListTopHolderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkRefreshListTopHolderView(Context paramContext)
  {
    super(paramContext);
  }
  
  public void loadView()
  {
    super.loadView();
    this.a = new View(getContext());
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 0);
    addView(this.a, localLayoutParams);
  }
  
  public void setVisiableHeight(int paramInt)
  {
    ViewGroup.LayoutParams localLayoutParams = this.a.getLayoutParams();
    localLayoutParams.height = paramInt;
    this.a.setLayoutParams(localLayoutParams);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\listview\TuSdkRefreshListTopHolderView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */