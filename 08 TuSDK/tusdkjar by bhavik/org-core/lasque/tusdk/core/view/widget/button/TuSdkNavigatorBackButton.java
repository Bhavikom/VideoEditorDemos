package org.lasque.tusdk.core.view.widget.button;

import android.content.Context;
import android.util.AttributeSet;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonType;

public class TuSdkNavigatorBackButton
  extends TuSdkButton
  implements TuSdkNavigatorBar.NavigatorBarButtonInterface
{
  private TuSdkNavigatorBar.NavigatorBarButtonType a;
  
  public TuSdkNavigatorBackButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkNavigatorBackButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkNavigatorBackButton(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuSdkNavigatorBar.NavigatorBarButtonType getType()
  {
    return this.a;
  }
  
  public void setType(TuSdkNavigatorBar.NavigatorBarButtonType paramNavigatorBarButtonType)
  {
    this.a = paramNavigatorBarButtonType;
  }
  
  public String getTitle()
  {
    if (getText() == null) {
      return null;
    }
    return getText().toString();
  }
  
  public void setTitle(String paramString)
  {
    if (paramString == null) {
      paramString = "";
    }
    setText(paramString);
  }
  
  public void showDot(boolean paramBoolean) {}
  
  public void setBadge(String paramString) {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\button\TuSdkNavigatorBackButton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */