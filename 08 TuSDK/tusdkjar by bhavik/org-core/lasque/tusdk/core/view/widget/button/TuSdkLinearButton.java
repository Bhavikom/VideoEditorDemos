package org.lasque.tusdk.core.view.widget.button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import org.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
import org.lasque.tusdk.core.view.TuSdkLinearLayout;

public class TuSdkLinearButton
  extends TuSdkLinearLayout
{
  protected TuSdkTouchColorChangeListener colorChangeListener;
  public int index;
  public int typeTag;
  public long idTag;
  
  public TuSdkLinearButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkLinearButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkLinearButton(Context paramContext)
  {
    super(paramContext);
  }
  
  protected void bindColorChangeListener()
  {
    if (this.colorChangeListener != null) {
      return;
    }
    this.colorChangeListener = TuSdkTouchColorChangeListener.bindTouchDark(this);
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  protected void removeColorChangeListener()
  {
    this.colorChangeListener = null;
    setOnTouchListener(null);
  }
  
  public void setOnClickListener(View.OnClickListener paramOnClickListener)
  {
    if (paramOnClickListener != null) {
      bindColorChangeListener();
    } else {
      removeColorChangeListener();
    }
    super.setOnClickListener(paramOnClickListener);
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    if ((this.colorChangeListener != null) && (isEnabled() != paramBoolean)) {
      this.colorChangeListener.enabledChanged(this, paramBoolean);
    }
    super.setEnabled(paramBoolean);
  }
  
  public void setSelected(boolean paramBoolean)
  {
    if ((this.colorChangeListener != null) && (isSelected() != paramBoolean)) {
      this.colorChangeListener.selectedChanged(this, paramBoolean);
    }
    super.setSelected(paramBoolean);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\button\TuSdkLinearButton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */