package org.lasque.tusdk.impl.view.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.listview.TuSdkRefreshListFooterView;

public class TuRefreshListFooterView
  extends TuSdkRefreshListFooterView
{
  private TextView a;
  private RelativeLayout b;
  
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_view_widget_list_view_refresh_footer_view");
  }
  
  public TuRefreshListFooterView(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuRefreshListFooterView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuRefreshListFooterView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TextView getTitleLabel()
  {
    if (this.a == null) {
      this.a = ((TextView)getViewById("lsq_titleLabel"));
    }
    return this.a;
  }
  
  public void setTitleLabel(TextView paramTextView)
  {
    this.a = paramTextView;
  }
  
  public RelativeLayout getFootWrap()
  {
    if (this.b == null) {
      this.b = ((RelativeLayout)getViewById("lsq_footWrap"));
    }
    return this.b;
  }
  
  public void setFootWrap(RelativeLayout paramRelativeLayout)
  {
    this.b = paramRelativeLayout;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\listview\TuRefreshListFooterView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */