package org.lasque.tusdk.core.view.widget.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;
import org.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
import org.lasque.tusdk.core.view.TuSdkViewInterface;

public class TuSdkTextButton
  extends TextView
  implements TuSdkViewInterface
{
  private TuSdkTouchColorChangeListener a;
  public int index;
  private int b = Integer.MAX_VALUE;
  private int c = Integer.MAX_VALUE;
  
  public TuSdkTextButton(Context paramContext)
  {
    super(paramContext);
    initView();
  }
  
  public TuSdkTextButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }
  
  public TuSdkTextButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  protected void initView()
  {
    this.a = TuSdkTouchColorChangeListener.bindTouchDark(this);
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    if ((this.a != null) && (isEnabled() != paramBoolean)) {
      this.a.enabledChanged(this, paramBoolean);
    }
    super.setEnabled(paramBoolean);
  }
  
  public void setSelected(boolean paramBoolean)
  {
    if ((this.a != null) && (isSelected() != paramBoolean)) {
      this.a.selectedChanged(this, paramBoolean);
    }
    super.setSelected(paramBoolean);
    changeColor(paramBoolean ? this.b : this.c);
  }
  
  public void changeColor(int paramInt)
  {
    if (this.b == Integer.MAX_VALUE) {
      return;
    }
    setTextColor(paramInt);
    if (paramInt == this.c) {
      a(getCompoundDrawables());
    } else {
      a(getCompoundDrawables(), paramInt);
    }
  }
  
  private void a(Drawable[] paramArrayOfDrawable)
  {
    for (Drawable localDrawable : paramArrayOfDrawable) {
      if (localDrawable != null) {
        localDrawable.clearColorFilter();
      }
    }
  }
  
  private void a(Drawable[] paramArrayOfDrawable, int paramInt)
  {
    for (Drawable localDrawable : paramArrayOfDrawable) {
      if (localDrawable != null)
      {
        localDrawable.clearColorFilter();
        localDrawable.setColorFilter(paramInt, PorterDuff.Mode.SRC_IN);
      }
    }
  }
  
  public void setDefaultColor(int paramInt)
  {
    this.c = paramInt;
  }
  
  public void setSelectedColor(int paramInt)
  {
    this.b = paramInt;
  }
  
  public void loadView()
  {
    this.c = getTextColors().getDefaultColor();
  }
  
  public void viewDidLoad() {}
  
  public void viewWillDestory() {}
  
  public void viewNeedRest() {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\button\TuSdkTextButton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */