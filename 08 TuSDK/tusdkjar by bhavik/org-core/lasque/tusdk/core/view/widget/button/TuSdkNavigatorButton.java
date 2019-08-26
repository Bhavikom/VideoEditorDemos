package org.lasque.tusdk.core.view.widget.button;

import android.content.Context;
import android.util.AttributeSet;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonType;

public abstract class TuSdkNavigatorButton
  extends TuSdkRelativeButton
  implements TuSdkNavigatorBar.NavigatorBarButtonInterface
{
  private TuSdkNavigatorBar.NavigatorBarButtonType a;
  
  public TuSdkNavigatorButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkNavigatorButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkNavigatorButton(Context paramContext)
  {
    super(paramContext);
  }
  
  public void loadView()
  {
    super.loadView();
  }
  
  public TuSdkNavigatorBar.NavigatorBarButtonType getType()
  {
    return this.a;
  }
  
  public void setType(TuSdkNavigatorBar.NavigatorBarButtonType paramNavigatorBarButtonType)
  {
    this.a = paramNavigatorBarButtonType;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\button\TuSdkNavigatorButton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */