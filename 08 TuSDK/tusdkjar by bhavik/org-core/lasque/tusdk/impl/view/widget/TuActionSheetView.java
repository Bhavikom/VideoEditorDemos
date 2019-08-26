package org.lasque.tusdk.impl.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.type.ActivityAnimType;
import org.lasque.tusdk.core.view.widget.TuSdkActionSheetView;
import org.lasque.tusdk.impl.TuAnimType;

public class TuActionSheetView
  extends TuSdkActionSheetView
{
  private View a;
  private LinearLayout b;
  private TextView c;
  private Button d;
  
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_view_widget_actionsheet");
  }
  
  public TuActionSheetView(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuActionSheetView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuActionSheetView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public View getMaskBg()
  {
    if (this.a == null) {
      this.a = getViewById("lsq_maskBg");
    }
    return this.a;
  }
  
  public LinearLayout getSheetTable()
  {
    if (this.b == null) {
      this.b = ((LinearLayout)getViewById("lsq_sheetTable"));
    }
    return this.b;
  }
  
  public TextView getTitleView()
  {
    if (this.c == null) {
      this.c = ((TextView)getViewById("lsq_titleView"));
    }
    return this.c;
  }
  
  public Button getCancelButton()
  {
    if (this.d == null) {
      this.d = ((Button)getViewById("lsq_cancelButton"));
    }
    return this.d;
  }
  
  public ActivityAnimType getAlphaAnimType()
  {
    return TuAnimType.fade;
  }
  
  public ActivityAnimType getTransAnimType()
  {
    return TuAnimType.upDownSub;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\TuActionSheetView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */