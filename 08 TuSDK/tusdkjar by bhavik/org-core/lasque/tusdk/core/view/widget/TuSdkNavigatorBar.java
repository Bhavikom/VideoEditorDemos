package org.lasque.tusdk.core.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import java.util.HashMap;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.core.view.widget.button.TuSdkNavigatorBackButton;
import org.lasque.tusdk.core.view.widget.button.TuSdkNavigatorButton;

public abstract class TuSdkNavigatorBar
  extends TuSdkRelativeLayout
{
  private int a;
  public TuSdkNavigatorBarDelegate delegate;
  private boolean b;
  private TuSdkSegmented c;
  private TuSdkSearchView d;
  private HashMap<NavigatorBarButtonType, NavigatorBarButtonInterface> e = new HashMap();
  private TuSdkViewHelper.OnSafeClickListener f = new TuSdkViewHelper.OnSafeClickListener()
  {
    public void onSafeClick(View paramAnonymousView)
    {
      TuSdkNavigatorBar.this.onButtonClicked(paramAnonymousView);
    }
  };
  
  public boolean isBackButtonShowed()
  {
    return this.b;
  }
  
  public TuSdkNavigatorBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkNavigatorBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkNavigatorBar(Context paramContext)
  {
    super(paramContext);
  }
  
  public void setButtonLayoutId(int paramInt)
  {
    this.a = paramInt;
  }
  
  public void setBackButtonId(int paramInt)
  {
    a(paramInt);
  }
  
  private void a(int paramInt)
  {
    TuSdkNavigatorBackButton localTuSdkNavigatorBackButton = (TuSdkNavigatorBackButton)getViewById(paramInt);
    if (localTuSdkNavigatorBackButton == null) {
      return;
    }
    localTuSdkNavigatorBackButton.setType(NavigatorBarButtonType.back);
    localTuSdkNavigatorBackButton.setOnClickListener(this.f);
    this.e.put(localTuSdkNavigatorBackButton.getType(), localTuSdkNavigatorBackButton);
  }
  
  public NavigatorBarButtonInterface getButton(NavigatorBarButtonType paramNavigatorBarButtonType)
  {
    if (paramNavigatorBarButtonType == null) {
      return null;
    }
    return (NavigatorBarButtonInterface)this.e.get(paramNavigatorBarButtonType);
  }
  
  private void a(TuSdkNavigatorButton paramTuSdkNavigatorButton)
  {
    if ((paramTuSdkNavigatorButton == null) || (paramTuSdkNavigatorButton.getType() == null) || (paramTuSdkNavigatorButton.getType() == NavigatorBarButtonType.back)) {
      return;
    }
    if (paramTuSdkNavigatorButton.getType() == NavigatorBarButtonType.left) {
      showBackButton(false);
    }
    b(paramTuSdkNavigatorButton.getType());
    this.e.put(paramTuSdkNavigatorButton.getType(), paramTuSdkNavigatorButton);
    addView(paramTuSdkNavigatorButton);
    paramTuSdkNavigatorButton.setOnClickListener(this.f);
  }
  
  public TuSdkNavigatorButton setButton(String paramString, TuSdkNavButtonStyleInterface paramTuSdkNavButtonStyleInterface, NavigatorBarButtonType paramNavigatorBarButtonType)
  {
    TuSdkNavigatorButton localTuSdkNavigatorButton = a(paramNavigatorBarButtonType);
    if (localTuSdkNavigatorButton == null) {
      return null;
    }
    if ((paramTuSdkNavButtonStyleInterface != null) && (paramTuSdkNavButtonStyleInterface.getBackgroundId() != 0)) {
      localTuSdkNavigatorButton.setBackgroundResource(paramTuSdkNavButtonStyleInterface.getBackgroundId());
    }
    localTuSdkNavigatorButton.setTitle(paramString);
    a(localTuSdkNavigatorButton);
    return localTuSdkNavigatorButton;
  }
  
  private TuSdkNavigatorButton a(NavigatorBarButtonType paramNavigatorBarButtonType)
  {
    if ((this.a == 0) || (paramNavigatorBarButtonType == null) || (paramNavigatorBarButtonType == NavigatorBarButtonType.back)) {
      return null;
    }
    TuSdkNavigatorButton localTuSdkNavigatorButton = (TuSdkNavigatorButton)buildView(this.a);
    if (localTuSdkNavigatorButton == null) {
      return null;
    }
    a(paramNavigatorBarButtonType, localTuSdkNavigatorButton);
    localTuSdkNavigatorButton.loadView();
    localTuSdkNavigatorButton.setType(paramNavigatorBarButtonType);
    return localTuSdkNavigatorButton;
  }
  
  private void a(NavigatorBarButtonType paramNavigatorBarButtonType, TuSdkNavigatorButton paramTuSdkNavigatorButton)
  {
    RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)paramTuSdkNavigatorButton.getLayoutParams();
    localLayoutParams.addRule(15);
    switch (2.a[paramNavigatorBarButtonType.ordinal()])
    {
    case 1: 
      localLayoutParams.addRule(9);
      break;
    case 2: 
      localLayoutParams.addRule(11);
      break;
    }
    paramTuSdkNavigatorButton.setLayoutParams(localLayoutParams);
  }
  
  public void showBackButton(boolean paramBoolean)
  {
    NavigatorBarButtonInterface localNavigatorBarButtonInterface = getButton(NavigatorBarButtonType.back);
    if (localNavigatorBarButtonInterface == null) {
      return;
    }
    this.b = paramBoolean;
    localNavigatorBarButtonInterface.setVisibility(paramBoolean ? 0 : 8);
  }
  
  private void b(NavigatorBarButtonType paramNavigatorBarButtonType)
  {
    NavigatorBarButtonInterface localNavigatorBarButtonInterface = getButton(paramNavigatorBarButtonType);
    if (localNavigatorBarButtonInterface == null) {
      return;
    }
    removeView((View)localNavigatorBarButtonInterface);
  }
  
  public abstract void setTitle(String paramString);
  
  public abstract void setTitle(int paramInt);
  
  public abstract String getTitle();
  
  protected void onButtonClicked(View paramView)
  {
    if (this.delegate == null) {
      return;
    }
    this.delegate.onNavigatorBarButtonClicked((NavigatorBarButtonInterface)paramView);
  }
  
  protected TuSdkSegmented buildSegmented(int paramInt)
  {
    if ((this.c != null) || (paramInt == 0)) {
      return this.c;
    }
    this.c = ((TuSdkSegmented)buildView(paramInt));
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
    localLayoutParams.bottomMargin = (localLayoutParams.topMargin = ContextUtils.dip2px(getContext(), 7.0F));
    localLayoutParams.leftMargin = (localLayoutParams.rightMargin = ContextUtils.dip2px(getContext(), 70.0F));
    this.c.setLayoutParams(localLayoutParams);
    return this.c;
  }
  
  public TuSdkSegmented getSegmented()
  {
    return this.c;
  }
  
  public void addSegmentedText(int... paramVarArgs)
  {
    TuSdkSegmented localTuSdkSegmented = getSegmented();
    if (localTuSdkSegmented == null) {
      return;
    }
    localTuSdkSegmented.addSegmentedText(paramVarArgs);
  }
  
  public void addSegmentedText(String... paramVarArgs)
  {
    TuSdkSegmented localTuSdkSegmented = getSegmented();
    if (localTuSdkSegmented == null) {
      return;
    }
    localTuSdkSegmented.addSegmentedText(paramVarArgs);
  }
  
  public void setSegmentedDelegate(TuSdkSegmented.TuSdkSegmentedDelegate paramTuSdkSegmentedDelegate)
  {
    TuSdkSegmented localTuSdkSegmented = getSegmented();
    if (localTuSdkSegmented == null) {
      return;
    }
    localTuSdkSegmented.setSegmentedDelegate(paramTuSdkSegmentedDelegate);
  }
  
  public void setSegmentedSelected(int paramInt)
  {
    TuSdkSegmented localTuSdkSegmented = getSegmented();
    if (localTuSdkSegmented == null) {
      return;
    }
    localTuSdkSegmented.changeSelected(paramInt);
  }
  
  public TuSdkSearchView getSearchView()
  {
    return this.d;
  }
  
  protected TuSdkSearchView buildSearchView(int paramInt)
  {
    if ((this.d != null) || (paramInt == 0)) {
      return this.d;
    }
    this.d = ((TuSdkSearchView)buildView(paramInt));
    RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
    localLayoutParams.bottomMargin = (localLayoutParams.topMargin = ContextUtils.dip2px(getContext(), 5.0F));
    localLayoutParams.leftMargin = ContextUtils.dip2px(getContext(), 70.0F);
    this.d.setLayoutParams(localLayoutParams);
    return this.d;
  }
  
  public void setSearchViewDelegate(TuSdkSearchView.TuSdkSearchViewDelegate paramTuSdkSearchViewDelegate)
  {
    TuSdkSearchView localTuSdkSearchView = getSearchView();
    if (localTuSdkSearchView == null) {
      return;
    }
    localTuSdkSearchView.setDelegate(paramTuSdkSearchViewDelegate);
  }
  
  public void searchKeyword(String paramString)
  {
    TuSdkSearchView localTuSdkSearchView = getSearchView();
    if (localTuSdkSearchView == null) {
      return;
    }
    localTuSdkSearchView.setTextAndSubmit(paramString);
  }
  
  public void viewWillDestory()
  {
    super.viewWillDestory();
    if (this.c != null) {
      this.c.viewWillDestory();
    }
    if (this.d != null) {
      this.d.viewWillDestory();
    }
  }
  
  public static abstract interface TuSdkNavigatorBarDelegate
  {
    public abstract void onNavigatorBarButtonClicked(TuSdkNavigatorBar.NavigatorBarButtonInterface paramNavigatorBarButtonInterface);
  }
  
  public static abstract interface NavigatorBarButtonInterface
  {
    public abstract void setVisibility(int paramInt);
    
    public abstract TuSdkNavigatorBar.NavigatorBarButtonType getType();
    
    public abstract void setType(TuSdkNavigatorBar.NavigatorBarButtonType paramNavigatorBarButtonType);
    
    public abstract String getTitle();
    
    public abstract void setTitle(String paramString);
    
    public abstract void showDot(boolean paramBoolean);
    
    public abstract void setBadge(String paramString);
    
    public abstract void setEnabled(boolean paramBoolean);
    
    public abstract void setTextColor(int paramInt);
  }
  
  public static abstract interface TuSdkNavButtonStyleInterface
  {
    public abstract int getBackgroundId();
  }
  
  public static enum NavigatorBarButtonType
  {
    private NavigatorBarButtonType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\TuSdkNavigatorBar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */