package org.lasque.tusdk.core.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import org.lasque.tusdk.core.listener.TuSdkTouchColorChangeListener;

public class TuSdkTextField
  extends TuSdkEditText
  implements TextWatcher
{
  private Drawable a;
  private TuSdkTextFieldListener b;
  private boolean c;
  
  public TuSdkTextField(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkTextField(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkTextField(Context paramContext)
  {
    super(paramContext);
  }
  
  public void setClearListener(TuSdkTextFieldListener paramTuSdkTextFieldListener)
  {
    this.b = paramTuSdkTextFieldListener;
  }
  
  protected void initView()
  {
    super.initView();
    a();
  }
  
  private void a()
  {
    this.a = getCompoundDrawables()[2];
    if (this.a == null) {
      return;
    }
    setClearIconVisible(getText().length() > 0);
    addTextChangedListener(this);
  }
  
  protected void setClearIconVisible(boolean paramBoolean)
  {
    if (this.a == null) {
      return;
    }
    Drawable localDrawable = paramBoolean ? this.a : null;
    Drawable[] arrayOfDrawable = getCompoundDrawables();
    setCompoundDrawables(arrayOfDrawable[0], arrayOfDrawable[1], localDrawable, arrayOfDrawable[3]);
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    a(paramMotionEvent);
    return super.onTouchEvent(paramMotionEvent);
  }
  
  private void a(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction())
    {
    case 0: 
      b(paramMotionEvent);
      break;
    case 1: 
    case 3: 
    case 4: 
      c(paramMotionEvent);
      break;
    }
  }
  
  private void b(MotionEvent paramMotionEvent)
  {
    if (!d(paramMotionEvent)) {
      return;
    }
    TuSdkTouchColorChangeListener.setDark(this.a);
    this.c = true;
  }
  
  private void c(MotionEvent paramMotionEvent)
  {
    if (!this.c) {
      return;
    }
    this.c = false;
    TuSdkTouchColorChangeListener.clearColorType(this.a);
    if (!d(paramMotionEvent)) {
      return;
    }
    setText("");
    if (this.b != null) {
      this.b.onTextFieldClickClear(this);
    }
  }
  
  private boolean d(MotionEvent paramMotionEvent)
  {
    if ((this.a == null) || (getCompoundDrawables()[2] == null)) {
      return false;
    }
    float f = paramMotionEvent.getX();
    int i = getWidth() - getTotalPaddingRight();
    int j = getWidth() - getPaddingRight();
    return (f >= i) && (f <= j);
  }
  
  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  
  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    setClearIconVisible(paramCharSequence.length() > 0);
  }
  
  public void afterTextChanged(Editable paramEditable) {}
  
  public static abstract interface TuSdkTextFieldListener
  {
    public abstract void onTextFieldClickClear(TuSdkTextField paramTuSdkTextField);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\TuSdkTextField.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */