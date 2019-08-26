package org.lasque.tusdk.impl.view.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;
import org.lasque.tusdk.core.view.listview.TuSdkListTotalFootView;

public class TuListTotalFootView
  extends TuSdkListTotalFootView
{
  private TuSdkRelativeLayout a;
  private TextView b;
  
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_view_widget_list_view_total_footer_view");
  }
  
  public TuListTotalFootView(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuListTotalFootView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuListTotalFootView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkRelativeLayout getWrapView()
  {
    if (this.a == null) {
      this.a = ((TuSdkRelativeLayout)getViewById("lsq_wrapView"));
    }
    return this.a;
  }
  
  public void setmWrapView(TuSdkRelativeLayout paramTuSdkRelativeLayout)
  {
    this.a = paramTuSdkRelativeLayout;
  }
  
  public TextView getTitleView()
  {
    if (this.b == null) {
      this.b = ((TextView)getViewById("lsq_titleView"));
    }
    return this.b;
  }
  
  public void setTitleView(TextView paramTextView)
  {
    this.b = paramTextView;
  }
  
  public void setTotal(int paramInt)
  {
    super.setTotal(paramInt);
    setTitle(paramInt);
  }
  
  public void setTitle(int paramInt)
  {
    if ((getTitleFormater() == null) || (getTitleView() == null)) {
      return;
    }
    String str = String.format(getTitleFormater(), new Object[] { Integer.valueOf(paramInt) });
    getTitleView().setText(str);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\listview\TuListTotalFootView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */