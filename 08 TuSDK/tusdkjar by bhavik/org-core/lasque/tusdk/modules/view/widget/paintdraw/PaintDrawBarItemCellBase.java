package org.lasque.tusdk.modules.view.widget.paintdraw;

import android.content.Context;
import android.util.AttributeSet;
import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;
import org.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;

public abstract class PaintDrawBarItemCellBase
  extends TuSdkCellRelativeLayout<PaintData>
  implements TuSdkListSelectableCellViewInterface
{
  public PaintDrawBarItemCellBase(Context paramContext)
  {
    super(paramContext);
  }
  
  public PaintDrawBarItemCellBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public PaintDrawBarItemCellBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void onCellSelected(int paramInt)
  {
    setSelected(true);
  }
  
  public void onCellDeselected()
  {
    setSelected(false);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\paintdraw\PaintDrawBarItemCellBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */