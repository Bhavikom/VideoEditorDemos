package org.lasque.tusdk.modules.components.filter;

import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewInterface.GroupFilterAction;

public abstract interface TuFilterOnlineFragmentInterface
{
  public abstract void setDelegate(TuFilterOnlineFragmentDelegate paramTuFilterOnlineFragmentDelegate);
  
  public abstract void setAction(GroupFilterItemViewInterface.GroupFilterAction paramGroupFilterAction);
  
  public abstract void setDetailDataId(long paramLong);
  
  public static abstract interface TuFilterOnlineFragmentDelegate
  {
    public abstract void onTuFilterOnlineFragmentSelected(TuFilterOnlineFragmentInterface paramTuFilterOnlineFragmentInterface, long paramLong);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\filter\TuFilterOnlineFragmentInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */