package org.lasque.tusdk.core.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkCellRelativeLayout<T>
  extends TuSdkRelativeLayout
  implements TuSdkCellViewInterface<T>
{
  private T a;
  
  public TuSdkCellRelativeLayout(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuSdkCellRelativeLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkCellRelativeLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void setModel(T paramT)
  {
    this.a = paramT;
    if (isLayouted()) {
      willBindModel();
    }
  }
  
  public T getModel()
  {
    return (T)this.a;
  }
  
  protected void onLayouted()
  {
    super.onLayouted();
    willBindModel();
  }
  
  protected void willBindModel()
  {
    if (this.a == null) {
      return;
    }
    bindModel();
  }
  
  protected abstract void bindModel();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\listview\TuSdkCellRelativeLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */