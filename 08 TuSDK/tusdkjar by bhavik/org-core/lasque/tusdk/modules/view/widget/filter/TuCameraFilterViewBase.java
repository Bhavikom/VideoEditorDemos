package org.lasque.tusdk.modules.view.widget.filter;

import android.content.Context;
import android.util.AttributeSet;

public abstract class TuCameraFilterViewBase
  extends GroupFilterBaseView
{
  protected abstract long getCaptureActivateWaitMillis();
  
  protected abstract boolean onGroupFilterSelected(GroupFilterItem paramGroupFilterItem, boolean paramBoolean);
  
  public TuCameraFilterViewBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuCameraFilterViewBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuCameraFilterViewBase(Context paramContext)
  {
    super(paramContext);
  }
  
  protected boolean onDispatchGroupFilterSelected(GroupFilterBarInterface paramGroupFilterBarInterface, GroupFilterItemViewInterface paramGroupFilterItemViewInterface, GroupFilterItem paramGroupFilterItem)
  {
    boolean bool1 = true;
    boolean bool2 = false;
    if ((paramGroupFilterItem.type == GroupFilterItem.GroupFilterItemType.TypeFilter) && (!notifyTitle(paramGroupFilterItemViewInterface, paramGroupFilterItem))) {
      if ((!notifyTitle(paramGroupFilterItemViewInterface, paramGroupFilterItem)) && (paramGroupFilterItemViewInterface.isActivating()))
      {
        paramGroupFilterItemViewInterface.stopActivating();
        bool2 = true;
      }
      else
      {
        paramGroupFilterItemViewInterface.waitInActivate(getCaptureActivateWaitMillis());
        return true;
      }
    }
    bool1 = onGroupFilterSelected(paramGroupFilterItem, bool2);
    return bool1;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\filter\TuCameraFilterViewBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */