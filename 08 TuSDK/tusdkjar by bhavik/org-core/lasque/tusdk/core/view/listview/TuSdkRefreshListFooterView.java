package org.lasque.tusdk.core.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkRefreshListFooterView
  extends TuSdkRelativeLayout
{
  public TuSdkRefreshListFooterView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkRefreshListFooterView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkRefreshListFooterView(Context paramContext)
  {
    super(paramContext);
  }
  
  public abstract TextView getTitleLabel();
  
  public abstract RelativeLayout getFootWrap();
  
  public void loadView()
  {
    super.loadView();
    setViewShowed(false);
  }
  
  public void setTitle(String paramString)
  {
    getTitleLabel().setText(paramString);
  }
  
  public void setViewShowed(boolean paramBoolean)
  {
    showView(getFootWrap(), paramBoolean);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\listview\TuSdkRefreshListFooterView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */