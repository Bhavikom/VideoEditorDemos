package org.lasque.tusdk.impl.view.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;

public class TuDefaultLineListCellView
  extends TuSdkCellRelativeLayout<String>
{
  private TextView a;
  
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_view_widget_list_view_default_line_cell_view");
  }
  
  public TuDefaultLineListCellView(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuDefaultLineListCellView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuDefaultLineListCellView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TextView getTitleView()
  {
    if (this.a == null) {
      this.a = ((TextView)getViewById("lsq_titleView"));
    }
    return this.a;
  }
  
  public void setTitleView(TextView paramTextView)
  {
    this.a = paramTextView;
  }
  
  protected void bindModel()
  {
    if ((getTitleView() == null) || (getModel() == null)) {
      return;
    }
    getTitleView().setText((CharSequence)getModel());
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\listview\TuDefaultLineListCellView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */