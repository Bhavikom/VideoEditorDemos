package org.lasque.tusdk.core.view.widget.button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import org.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;
import org.lasque.tusdk.core.view.TuSdkImageView;

public class TuSdkImageButton
  extends TuSdkImageView
{
  private TuSdkTouchColorChangeListener a;
  public int index;
  
  public TuSdkImageButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkImageButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkImageButton(Context paramContext)
  {
    super(paramContext);
  }
  
  protected void initView() {}
  
  protected void bindColorChangeListener()
  {
    if (this.a != null) {
      return;
    }
    this.a = TuSdkTouchColorChangeListener.bindTouchDark(this);
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  protected void removeColorChangeListener()
  {
    this.a = null;
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
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\button\TuSdkImageButton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */