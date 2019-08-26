package org.lasque.tusdk.impl.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.widget.TuSdkProgressHubView;

public class TuProgressHubView
  extends TuSdkProgressHubView
{
  private ProgressBar a;
  private TextView b;
  private LinearLayout c;
  private ImageView d;
  
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_view_widget_progress_hud_view");
  }
  
  public TuProgressHubView(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuProgressHubView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuProgressHubView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public ProgressBar getProgressBar()
  {
    if (this.a == null) {
      this.a = ((ProgressBar)getViewById("lsq_progressBar"));
    }
    return this.a;
  }
  
  public ImageView getImageView()
  {
    if (this.d == null) {
      this.d = ((ImageView)getViewById("lsq_hubImageView"));
    }
    return this.d;
  }
  
  public TextView getTitleView()
  {
    if (this.b == null) {
      this.b = ((TextView)getViewById("lsq_hubTitleView"));
    }
    return this.b;
  }
  
  public LinearLayout getHubView()
  {
    if (this.c == null) {
      this.c = ((LinearLayout)getViewById("lsq_hubView"));
    }
    return this.c;
  }
  
  public int getImageSucceedResId()
  {
    return TuSdkContext.getDrawableResId("lsq_style_default_hud_success");
  }
  
  public int getImageFailedResId()
  {
    return TuSdkContext.getDrawableResId("lsq_style_default_hud_error");
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\TuProgressHubView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */