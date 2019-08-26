package org.lasque.tusdk.core.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import org.lasque.tusdk.core.activity.ActivityObserver;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.anim.AnimHelper;
import org.lasque.tusdk.core.view.TuSdkViewInterface;

public class TuSdkEditText
  extends EditText
  implements View.OnFocusChangeListener, View.OnKeyListener, TextView.OnEditorActionListener, TuSdkViewInterface
{
  private TuSdkEditTextListener a;
  private View.OnFocusChangeListener b;
  
  public TuSdkEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  public TuSdkEditText(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }
  
  public TuSdkEditText(Context paramContext)
  {
    super(paramContext);
    initView();
  }
  
  protected void initView()
  {
    super.setOnFocusChangeListener(this);
    setOnEditorActionListener(this);
    setOnKeyListener(this);
  }
  
  public void setOnFocusChangeListener(View.OnFocusChangeListener paramOnFocusChangeListener)
  {
    this.b = paramOnFocusChangeListener;
  }
  
  public void loadView() {}
  
  public void viewDidLoad() {}
  
  public void viewWillDestory() {}
  
  public void viewNeedRest() {}
  
  public String getInputText()
  {
    if (getText() == null) {
      return null;
    }
    String str = StringHelper.trimToNull(getText().toString());
    return str;
  }
  
  public String getTextOrEmpty()
  {
    if (getText() == null) {
      return "";
    }
    String str = StringHelper.trimToEmpty(getText().toString());
    return str;
  }
  
  public void onFocusChange(View paramView, boolean paramBoolean)
  {
    if (getContext() == null) {
      return;
    }
    if (paramBoolean) {
      ActivityObserver.ins.editTextFocus(this);
    } else {
      ActivityObserver.ins.editTextFocusLost(this);
    }
    if (this.b != null) {
      this.b.onFocusChange(paramView, paramBoolean);
    }
  }
  
  public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramKeyEvent != null) && (paramKeyEvent.getAction() == 0)) {
      return false;
    }
    switch (paramInt)
    {
    case 2: 
    case 3: 
    case 4: 
    case 6: 
      return a();
    }
    return false;
  }
  
  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 66) && (paramKeyEvent.getAction() == 0)) {}
    return false;
  }
  
  private boolean a()
  {
    TuSdkEditTextListener localTuSdkEditTextListener = getSubmitListener();
    if (localTuSdkEditTextListener != null) {
      return localTuSdkEditTextListener.onTuSdkEditTextSubmit(this);
    }
    return false;
  }
  
  public TuSdkEditTextListener getSubmitListener()
  {
    return this.a;
  }
  
  public void setSubmitListener(TuSdkEditTextListener paramTuSdkEditTextListener)
  {
    this.a = paramTuSdkEditTextListener;
  }
  
  public void setShakeAnimation()
  {
    setAnimation(AnimHelper.shakeAnimation(3));
  }
  
  public static abstract interface TuSdkEditTextListener
  {
    public abstract boolean onTuSdkEditTextSubmit(TuSdkEditText paramTuSdkEditText);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\TuSdkEditText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */