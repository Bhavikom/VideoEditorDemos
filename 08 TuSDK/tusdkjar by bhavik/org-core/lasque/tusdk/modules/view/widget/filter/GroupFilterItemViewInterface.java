package org.lasque.tusdk.modules.view.widget.filter;

import org.lasque.tusdk.core.task.FilterTaskInterface;

public abstract interface GroupFilterItemViewInterface
{
  public abstract void setAction(GroupFilterAction paramGroupFilterAction);
  
  public abstract boolean isSelected();
  
  public abstract boolean isCameraAction();
  
  public abstract boolean isActivating();
  
  public abstract void stopActivating();
  
  public abstract void waitInActivate(long paramLong);
  
  public abstract void setFilterTask(FilterTaskInterface paramFilterTaskInterface);
  
  public static enum GroupFilterAction
  {
    private int a;
    
    private GroupFilterAction(int paramInt)
    {
      this.a = paramInt;
    }
    
    public int getValue()
    {
      return this.a;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\filter\GroupFilterItemViewInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */