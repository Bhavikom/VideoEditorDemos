// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget;

//import org.lasque.tusdk.core.utils.StringHelper;
import android.text.Editable;
//import org.lasque.tusdk.core.activity.ActivityObserver;
import android.util.AttributeSet;
import android.content.Context;
import android.text.TextWatcher;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.activity.ActivityObserver;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkSearchView extends TuSdkRelativeLayout
{
    protected boolean isFocused;
    protected boolean isClearClicked;
    protected TuSdkTextField searchFiled;
    protected View searchButton;
    protected TuSdkSearchViewDelegate delegate;
    private TuSdkViewHelper.OnSafeClickListener a;
    private TuSdkEditText.TuSdkEditTextListener b;
    private View.OnFocusChangeListener c;
    private TuSdkTextField.TuSdkTextFieldListener d;
    private TextWatcher e;
    
    public TuSdkSearchView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.a = new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                ActivityObserver.ins.cancelEditTextFocus();
                TuSdkSearchView.this.onSubmitSearch();
            }
        };
        this.b = new TuSdkEditText.TuSdkEditTextListener() {
            @Override
            public boolean onTuSdkEditTextSubmit(final TuSdkEditText tuSdkEditText) {
                return TuSdkSearchView.this.onSubmitSearch();
            }
        };
        this.c = (View.OnFocusChangeListener)new View.OnFocusChangeListener() {
            public void onFocusChange(final View view, final boolean isFocused) {
                if (TuSdkSearchView.this.isClearClicked) {
                    TuSdkSearchView.this.isClearClicked = false;
                    ActivityObserver.ins.cancelEditTextFocus();
                }
                else {
                    TuSdkSearchView.this.isFocused = isFocused;
                    TuSdkSearchView.this.a();
                }
            }
        };
        this.d = new TuSdkTextField.TuSdkTextFieldListener() {
            @Override
            public void onTextFieldClickClear(final TuSdkTextField tuSdkTextField) {
                TuSdkSearchView.this.isClearClicked = true;
                TuSdkSearchView.this.closeSearchModel();
            }
        };
        this.e = (TextWatcher)new TextWatcher() {
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                TuSdkSearchView.this.onFiledTextChanged(charSequence, n, n2, n3);
            }
            
            public void afterTextChanged(final Editable editable) {
            }
        };
    }
    
    public TuSdkSearchView(final Context context, final AttributeSet set) {
        super(context, set);
        this.a = new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                ActivityObserver.ins.cancelEditTextFocus();
                TuSdkSearchView.this.onSubmitSearch();
            }
        };
        this.b = new TuSdkEditText.TuSdkEditTextListener() {
            @Override
            public boolean onTuSdkEditTextSubmit(final TuSdkEditText tuSdkEditText) {
                return TuSdkSearchView.this.onSubmitSearch();
            }
        };
        this.c = (View.OnFocusChangeListener)new View.OnFocusChangeListener() {
            public void onFocusChange(final View view, final boolean isFocused) {
                if (TuSdkSearchView.this.isClearClicked) {
                    TuSdkSearchView.this.isClearClicked = false;
                    ActivityObserver.ins.cancelEditTextFocus();
                }
                else {
                    TuSdkSearchView.this.isFocused = isFocused;
                    TuSdkSearchView.this.a();
                }
            }
        };
        this.d = new TuSdkTextField.TuSdkTextFieldListener() {
            @Override
            public void onTextFieldClickClear(final TuSdkTextField tuSdkTextField) {
                TuSdkSearchView.this.isClearClicked = true;
                TuSdkSearchView.this.closeSearchModel();
            }
        };
        this.e = (TextWatcher)new TextWatcher() {
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                TuSdkSearchView.this.onFiledTextChanged(charSequence, n, n2, n3);
            }
            
            public void afterTextChanged(final Editable editable) {
            }
        };
    }
    
    public TuSdkSearchView(final Context context) {
        super(context);
        this.a = new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                ActivityObserver.ins.cancelEditTextFocus();
                TuSdkSearchView.this.onSubmitSearch();
            }
        };
        this.b = new TuSdkEditText.TuSdkEditTextListener() {
            @Override
            public boolean onTuSdkEditTextSubmit(final TuSdkEditText tuSdkEditText) {
                return TuSdkSearchView.this.onSubmitSearch();
            }
        };
        this.c = (View.OnFocusChangeListener)new View.OnFocusChangeListener() {
            public void onFocusChange(final View view, final boolean isFocused) {
                if (TuSdkSearchView.this.isClearClicked) {
                    TuSdkSearchView.this.isClearClicked = false;
                    ActivityObserver.ins.cancelEditTextFocus();
                }
                else {
                    TuSdkSearchView.this.isFocused = isFocused;
                    TuSdkSearchView.this.a();
                }
            }
        };
        this.d = new TuSdkTextField.TuSdkTextFieldListener() {
            @Override
            public void onTextFieldClickClear(final TuSdkTextField tuSdkTextField) {
                TuSdkSearchView.this.isClearClicked = true;
                TuSdkSearchView.this.closeSearchModel();
            }
        };
        this.e = (TextWatcher)new TextWatcher() {
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                TuSdkSearchView.this.onFiledTextChanged(charSequence, n, n2, n3);
            }
            
            public void afterTextChanged(final Editable editable) {
            }
        };
    }
    
    public boolean isFocusModel() {
        return this.isFocused;
    }
    
    public void setDelegate(final TuSdkSearchViewDelegate delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public void loadView() {
        super.loadView();
        (this.searchFiled = this.findSearchFiled()).setOnFocusChangeListener(this.c);
        this.searchFiled.setClearListener(this.d);
        this.searchFiled.addTextChangedListener(this.e);
        this.searchFiled.setSubmitListener(this.b);
        (this.searchButton = this.findSearchButton()).setOnClickListener((View.OnClickListener)this.a);
    }
    
    public void setText(final String text) {
        this.searchFiled.setText((CharSequence)text);
    }
    
    public void setTextAndSubmit(final String text) {
        this.setText(text);
        this.onSubmitSearch();
    }
    
    protected abstract TuSdkTextField findSearchFiled();
    
    protected abstract View findSearchButton();
    
    protected boolean onSubmitSearch() {
        if (StringHelper.isEmpty(this.searchFiled.getInputText())) {
            return true;
        }
        if (this.delegate != null) {
            this.delegate.onSearchViewSubmited(this, this.searchFiled.getInputText());
        }
        return false;
    }
    
    protected void onFiledTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        if (this.isFocused && this.delegate != null) {
            this.delegate.onSearchViewTextChanged(this, StringHelper.trimToNull(charSequence.toString()));
        }
    }
    
    public void closeSearchModel() {
        ActivityObserver.ins.cancelEditTextFocus();
        this.isFocused = false;
        this.searchFiled.setText((CharSequence)null);
        this.a();
    }
    
    private void a() {
        if (this.delegate == null) {
            return;
        }
        this.delegate.onSearchViewFocusChange(this);
    }
    
    @Override
    public void viewWillDestory() {
        super.viewWillDestory();
        this.searchFiled.removeTextChangedListener(this.e);
        this.searchFiled.setOnFocusChangeListener(null);
        this.searchFiled.setClearListener(null);
        this.searchFiled.setSubmitListener(null);
        this.searchButton.setOnClickListener((View.OnClickListener)null);
        this.delegate = null;
    }
    
    public interface TuSdkSearchViewDelegate
    {
        void onSearchViewFocusChange(final TuSdkSearchView p0);
        
        void onSearchViewTextChanged(final TuSdkSearchView p0, final String p1);
        
        void onSearchViewSubmited(final TuSdkSearchView p0, final String p1);
    }
}
