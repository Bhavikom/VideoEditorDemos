package org.lasque.tusdk.core.view.listview;

import org.lasque.tusdk.core.view.TuSdkViewInterface;

public abstract interface TuSdkCellViewInterface<T>
  extends TuSdkViewInterface
{
  public abstract void setModel(T paramT);
  
  public abstract T getModel();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\listview\TuSdkCellViewInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */