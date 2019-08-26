package org.lasque.tusdk.impl.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.TuSdkNavButtonStyleInterface;

public class TuNavigatorBar
  extends TuSdkNavigatorBar
{
  private TextView a;
  
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_view_widget_navigator");
  }
  
  public TuNavigatorBar(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuNavigatorBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuNavigatorBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void loadView()
  {
    super.loadView();
    this.a = ((TextView)getViewById("lsq_titleView"));
  }
  
  public void setTitle(String paramString)
  {
    setTextViewText(this.a, paramString);
  }
  
  public void setTitle(int paramInt)
  {
    if (paramInt == 0) {
      return;
    }
    this.a.setText(paramInt);
  }
  
  public String getTitle()
  {
    return getTextViewText(this.a);
  }
  
  public static enum TuNavButtonStyle
    implements TuSdkNavigatorBar.TuSdkNavButtonStyleInterface
  {
    private int a;
    
    private TuNavButtonStyle(String paramString)
    {
      this.a = TuSdkContext.getColorResId(paramString);
    }
    
    public int getBackgroundId()
    {
      return this.a;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\TuNavigatorBar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */