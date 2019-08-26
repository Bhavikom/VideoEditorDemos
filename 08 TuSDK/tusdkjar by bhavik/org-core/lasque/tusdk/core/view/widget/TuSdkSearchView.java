package org.lasque.tusdk.core.view.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import org.lasque.tusdk.core.activity.ActivityObserver;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;

public abstract class TuSdkSearchView
  extends TuSdkRelativeLayout
{
  protected boolean isFocused;
  protected boolean isClearClicked;
  protected TuSdkTextField searchFiled;
  protected View searchButton;
  protected TuSdkSearchViewDelegate delegate;
  private TuSdkViewHelper.OnSafeClickListener a = new TuSdkViewHelper.OnSafeClickListener()
  {
    public void onSafeClick(View paramAnonymousView)
    {
      ActivityObserver.ins.cancelEditTextFocus();
      TuSdkSearchView.this.onSubmitSearch();
    }
  };
  private TuSdkEditText.TuSdkEditTextListener b = new TuSdkEditText.TuSdkEditTextListener()
  {
    public boolean onTuSdkEditTextSubmit(TuSdkEditText paramAnonymousTuSdkEditText)
    {
      return TuSdkSearchView.this.onSubmitSearch();
    }
  };
  private View.OnFocusChangeListener c = new View.OnFocusChangeListener()
  {
    public void onFocusChange(View paramAnonymousView, boolean paramAnonymousBoolean)
    {
      if (TuSdkSearchView.this.isClearClicked)
      {
        TuSdkSearchView.this.isClearClicked = false;
        ActivityObserver.ins.cancelEditTextFocus();
      }
      else
      {
        TuSdkSearchView.this.isFocused = paramAnonymousBoolean;
        TuSdkSearchView.a(TuSdkSearchView.this);
      }
    }
  };
  private TuSdkTextField.TuSdkTextFieldListener d = new TuSdkTextField.TuSdkTextFieldListener()
  {
    public void onTextFieldClickClear(TuSdkTextField paramAnonymousTuSdkTextField)
    {
      TuSdkSearchView.this.isClearClicked = true;
      TuSdkSearchView.this.closeSearchModel();
    }
  };
  private TextWatcher e = new TextWatcher()
  {
    public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
    
    public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
    {
      TuSdkSearchView.this.onFiledTextChanged(paramAnonymousCharSequence, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
    }
    
    public void afterTextChanged(Editable paramAnonymousEditable) {}
  };
  
  public TuSdkSearchView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkSearchView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkSearchView(Context paramContext)
  {
    super(paramContext);
  }
  
  public boolean isFocusModel()
  {
    return this.isFocused;
  }
  
  public void setDelegate(TuSdkSearchViewDelegate paramTuSdkSearchViewDelegate)
  {
    this.delegate = paramTuSdkSearchViewDelegate;
  }
  
  public void loadView()
  {
    super.loadView();
    this.searchFiled = findSearchFiled();
    this.searchFiled.setOnFocusChangeListener(this.c);
    this.searchFiled.setClearListener(this.d);
    this.searchFiled.addTextChangedListener(this.e);
    this.searchFiled.setSubmitListener(this.b);
    this.searchButton = findSearchButton();
    this.searchButton.setOnClickListener(this.a);
  }
  
  public void setText(String paramString)
  {
    this.searchFiled.setText(paramString);
  }
  
  public void setTextAndSubmit(String paramString)
  {
    setText(paramString);
    onSubmitSearch();
  }
  
  protected abstract TuSdkTextField findSearchFiled();
  
  protected abstract View findSearchButton();
  
  protected boolean onSubmitSearch()
  {
    if (StringHelper.isEmpty(this.searchFiled.getInputText())) {
      return true;
    }
    if (this.delegate != null) {
      this.delegate.onSearchViewSubmited(this, this.searchFiled.getInputText());
    }
    return false;
  }
  
  protected void onFiledTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((this.isFocused) && (this.delegate != null)) {
      this.delegate.onSearchViewTextChanged(this, StringHelper.trimToNull(paramCharSequence.toString()));
    }
  }
  
  public void closeSearchModel()
  {
    ActivityObserver.ins.cancelEditTextFocus();
    this.isFocused = false;
    this.searchFiled.setText(null);
    a();
  }
  
  private void a()
  {
    if (this.delegate == null) {
      return;
    }
    this.delegate.onSearchViewFocusChange(this);
  }
  
  public void viewWillDestory()
  {
    super.viewWillDestory();
    this.searchFiled.removeTextChangedListener(this.e);
    this.searchFiled.setOnFocusChangeListener(null);
    this.searchFiled.setClearListener(null);
    this.searchFiled.setSubmitListener(null);
    this.searchButton.setOnClickListener(null);
    this.delegate = null;
  }
  
  public static abstract interface TuSdkSearchViewDelegate
  {
    public abstract void onSearchViewFocusChange(TuSdkSearchView paramTuSdkSearchView);
    
    public abstract void onSearchViewTextChanged(TuSdkSearchView paramTuSdkSearchView, String paramString);
    
    public abstract void onSearchViewSubmited(TuSdkSearchView paramTuSdkSearchView, String paramString);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\TuSdkSearchView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */