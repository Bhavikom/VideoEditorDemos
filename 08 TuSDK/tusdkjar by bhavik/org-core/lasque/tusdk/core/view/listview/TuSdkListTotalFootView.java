package org.lasque.tusdk.core.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkListTotalFootView
  extends TuSdkRelativeLayout
{
  private int a;
  private String b;
  private int c;
  
  public TuSdkListTotalFootView(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuSdkListTotalFootView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkListTotalFootView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public String getTitleFormater()
  {
    return this.b;
  }
  
  public void setTitleFormater(String paramString)
  {
    this.b = paramString;
  }
  
  public int getTotal()
  {
    return this.a;
  }
  
  public void setTotal(int paramInt)
  {
    this.a = paramInt;
  }
  
  public abstract RelativeLayout getWrapView();
  
  public void loadView()
  {
    super.loadView();
    this.c = getWrapView().getLayoutParams().height;
  }
  
  public void needShowFooter(boolean paramBoolean)
  {
    ViewGroup.LayoutParams localLayoutParams = getWrapView().getLayoutParams();
    localLayoutParams.height = (paramBoolean ? this.c : 0);
    getWrapView().setLayoutParams(localLayoutParams);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\listview\TuSdkListTotalFootView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */