package org.lasque.tusdk.impl.view.widget;

import android.content.Context;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.widget.TuSdkActionSheet;

public class TuActionSheet
  extends TuSdkActionSheet
{
  public TuActionSheet(Context paramContext)
  {
    super(paramContext);
  }
  
  protected int getActionSheetLayoutId()
  {
    return TuActionSheetView.getLayoutId();
  }
  
  protected int getActionsheetButtonStyleResId()
  {
    return TuSdkContext.getStyleResId("lsq_actionsheetButton");
  }
  
  protected int getButtonBackgroundResId(int paramInt1, int paramInt2)
  {
    if ((paramInt2 == 1) && (getTitle() == null)) {
      return TuSdkContext.getDrawableResId("tusdk_view_widget_actionsheet_radius");
    }
    if ((paramInt1 == 0) && (getTitle() == null)) {
      return TuSdkContext.getDrawableResId("tusdk_view_widget_actionsheet_top_radius");
    }
    if (paramInt1 == paramInt2 - 1) {
      return TuSdkContext.getDrawableResId("tusdk_view_widget_actionsheet_bottom_radius");
    }
    return TuSdkContext.getDrawableResId("tusdk_view_widget_actionsheet_normal");
  }
  
  protected int getButtonColor(int paramInt)
  {
    String str = "lsq_actionsheet_text_color";
    if (getDestructiveIndex() == paramInt) {
      str = "lsq_actionsheet_text_stress";
    }
    return TuSdkContext.getColor(str);
  }
  
  protected int getActionsheetBottomSpace(boolean paramBoolean)
  {
    String str = "lsq_actionsheet_space_button";
    if (paramBoolean) {
      str = "lsq_actionsheet_space_bottom";
    }
    return TuSdkContext.getDimenOffset(str);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\TuActionSheet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */