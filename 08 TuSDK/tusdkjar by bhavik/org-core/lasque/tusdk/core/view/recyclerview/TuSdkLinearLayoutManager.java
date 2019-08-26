package org.lasque.tusdk.core.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import org.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;

public class TuSdkLinearLayoutManager
  extends LinearLayoutManager
{
  public TuSdkLinearLayoutManager(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuSdkLinearLayoutManager(Context paramContext, int paramInt, boolean paramBoolean)
  {
    super(paramContext, paramInt, paramBoolean);
    initManager();
  }
  
  protected void initManager() {}
  
  public void selectedPosition(int paramInt, boolean paramBoolean)
  {
    View localView = findViewByPosition(paramInt);
    if ((localView == null) || (!(localView instanceof TuSdkListSelectableCellViewInterface))) {
      return;
    }
    if (paramBoolean) {
      ((TuSdkListSelectableCellViewInterface)localView).onCellSelected(paramInt);
    } else {
      ((TuSdkListSelectableCellViewInterface)localView).onCellDeselected();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\recyclerview\TuSdkLinearLayoutManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */