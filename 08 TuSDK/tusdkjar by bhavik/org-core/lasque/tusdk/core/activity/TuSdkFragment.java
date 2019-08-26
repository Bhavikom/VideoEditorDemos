package org.lasque.tusdk.core.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout.LayoutParams;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkIntent;
import org.lasque.tusdk.core.listener.AnimationListenerAdapter;
import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener;
import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener.TuSdkOrientationDelegate;
import org.lasque.tusdk.core.type.ActivityAnimType;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.anim.MarginTopAnimation;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonType;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.TuSdkNavButtonStyleInterface;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.TuSdkNavigatorBarDelegate;
import org.lasque.tusdk.core.view.widget.TuSdkSearchView.TuSdkSearchViewDelegate;
import org.lasque.tusdk.core.view.widget.TuSdkSegmented.TuSdkSegmentedDelegate;
import org.lasque.tusdk.core.view.widget.button.TuSdkNavigatorButton;

public abstract class TuSdkFragment
  extends Fragment
  implements ViewTreeObserver.OnPreDrawListener, TuSdkOrientationEventListener.TuSdkOrientationDelegate, TuSdkNavigatorBar.TuSdkNavigatorBarDelegate
{
  private int a = 0;
  private ViewGroup b;
  private boolean c;
  private int d;
  private int e;
  private int f;
  private Fragment g;
  private TuSdkFragmentActivity h;
  private boolean i;
  private boolean j;
  private TuSdkNavigatorBar k;
  private TuSdkOrientationEventListener l;
  private String m;
  
  public void setRootViewLayoutId(int paramInt)
  {
    if (this.a == 0) {
      this.a = paramInt;
    }
  }
  
  public int getRootViewLayoutId()
  {
    return this.a;
  }
  
  public boolean isSupportSlideBack()
  {
    return this.i;
  }
  
  public void setIsSupportSlideBack(boolean paramBoolean)
  {
    this.i = paramBoolean;
  }
  
  public void setNavigatorBarId(int paramInt1, int paramInt2, int paramInt3)
  {
    this.d = paramInt1;
    this.e = paramInt2;
    this.f = paramInt3;
  }
  
  public Fragment getOriginFragment()
  {
    return this.g;
  }
  
  public void setOriginFragment(Fragment paramFragment)
  {
    this.g = paramFragment;
  }
  
  public TuSdkFragmentActivity getSdkActivity()
  {
    if (this.h != null) {
      return this.h;
    }
    FragmentActivity localFragmentActivity = getActivity();
    if ((localFragmentActivity != null) && ((localFragmentActivity instanceof TuSdkFragmentActivity))) {
      this.h = ((TuSdkFragmentActivity)localFragmentActivity);
    }
    return this.h;
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    initCreateView();
    if (this.a == 0)
    {
      TLog.e("can not defind rootViewId", new Object[0]);
      return this.b;
    }
    this.b = ((ViewGroup)TuSdkViewHelper.buildView(getActivity(), this.a, paramViewGroup));
    this.b.getViewTreeObserver().addOnPreDrawListener(this);
    b(this.b);
    loadView(this.b);
    return this.b;
  }
  
  protected abstract void initCreateView();
  
  public Animation onCreateAnimation(int paramInt1, final boolean paramBoolean, int paramInt2)
  {
    if (paramInt2 == 0) {
      return super.onCreateAnimation(paramInt1, paramBoolean, paramInt2);
    }
    Animation localAnimation = AnimationUtils.loadAnimation(getActivity(), paramInt2);
    if (localAnimation == null) {
      return localAnimation;
    }
    localAnimation.setAnimationListener(new AnimationListenerAdapter()
    {
      public void onAnimationEnd(Animation paramAnonymousAnimation)
      {
        paramAnonymousAnimation.setAnimationListener(null);
        TuSdkFragment.this.onFragmentAnimationEnd(paramBoolean, TuSdkFragment.a(TuSdkFragment.this));
        TuSdkFragment.a(TuSdkFragment.this, true);
      }
    });
    return localAnimation;
  }
  
  protected void onFragmentAnimationEnd(boolean paramBoolean1, boolean paramBoolean2) {}
  
  protected abstract void loadView(ViewGroup paramViewGroup);
  
  public boolean onPreDraw()
  {
    a(this.b);
    this.b.getViewTreeObserver().removeOnPreDrawListener(this);
    return true;
  }
  
  protected void navigatorBarLoaded(TuSdkNavigatorBar paramTuSdkNavigatorBar) {}
  
  protected abstract void viewDidLoad(ViewGroup paramViewGroup);
  
  private void a(ViewGroup paramViewGroup)
  {
    d();
    navigatorBarLoaded(this.k);
    viewDidLoad(this.b);
  }
  
  public void onResume()
  {
    super.onResume();
    if (this.j) {
      return;
    }
    a();
  }
  
  public void onPause()
  {
    b();
    super.onPause();
  }
  
  public void onDestroyView()
  {
    this.g = null;
    if (this.k != null) {
      this.k.viewWillDestory();
    }
    super.onDestroyView();
  }
  
  public void onDetach()
  {
    c();
    TuSdkViewHelper.viewWillDestory(this.b);
    super.onDetach();
  }
  
  public void onDestroy()
  {
    c();
    TuSdkViewHelper.viewWillDestory(getView());
    super.onDestroy();
  }
  
  public void onPauseFragment()
  {
    this.j = true;
    onPause();
  }
  
  public void onResumeFragment()
  {
    this.j = false;
    onResume();
  }
  
  public boolean isFragmentPause()
  {
    return this.j;
  }
  
  public boolean onBackPressed()
  {
    c();
    return true;
  }
  
  public boolean onBackForSlide()
  {
    if (this.i) {
      navigatorBarBackAction(null);
    }
    return this.i;
  }
  
  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    if ((paramActivity instanceof TuSdkFragmentActivity)) {
      this.h = ((TuSdkFragmentActivity)paramActivity);
    }
  }
  
  public void onRefreshData(int paramInt) {}
  
  public void refreshOriginFragment(int paramInt)
  {
    if ((this.g != null) && ((this.g instanceof TuSdkFragment))) {
      ((TuSdkFragment)this.g).onRefreshData(paramInt);
    }
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public <T extends View> T getViewById(int paramInt)
  {
    if (this.b == null) {
      return null;
    }
    View localView = this.b.findViewById(paramInt);
    return TuSdkViewHelper.loadView(localView);
  }
  
  public <T extends View> T getViewById(String paramString)
  {
    int n = TuSdkContext.getIDResId(paramString);
    if (n == 0) {
      return null;
    }
    return getViewById(n);
  }
  
  public int getViewId(View paramView)
  {
    if (paramView == null) {
      return 0;
    }
    return paramView.getId();
  }
  
  public boolean equalViewIds(View paramView1, View paramView2)
  {
    return getViewId(paramView1) == getViewId(paramView2);
  }
  
  public <T extends ViewGroup> T getRootView()
  {
    if (this.b == null) {
      return null;
    }
    return this.b;
  }
  
  public String getResString(int paramInt)
  {
    return ContextUtils.getResString(getActivity(), paramInt);
  }
  
  public String getResString(int paramInt, Object... paramVarArgs)
  {
    return ContextUtils.getResString(getActivity(), paramInt, paramVarArgs);
  }
  
  public String getResString(String paramString)
  {
    return TuSdkContext.getString(paramString);
  }
  
  public String getResString(String paramString, Object... paramVarArgs)
  {
    return TuSdkContext.getString(paramString, paramVarArgs);
  }
  
  public int getResColor(int paramInt)
  {
    return ContextUtils.getResColor(getActivity(), paramInt);
  }
  
  public void wantFullScreen(boolean paramBoolean)
  {
    if (this.h == null) {
      return;
    }
    this.h.wantFullScreen(paramBoolean);
  }
  
  public boolean isFullScreen()
  {
    if (this.h == null) {
      return false;
    }
    return this.h.isFullScreen();
  }
  
  public void bindSoftInputEvent()
  {
    if (this.h == null) {
      return;
    }
    this.h.bindSoftInputEvent();
  }
  
  public void setRequestedOrientation(int paramInt)
  {
    if (this.h == null) {
      return;
    }
    this.h.setRequestedOrientation(paramInt);
  }
  
  public void runOnUiThread(Runnable paramRunnable)
  {
    if (getActivity() == null) {
      return;
    }
    getActivity().runOnUiThread(paramRunnable);
  }
  
  public void showView(View paramView, boolean paramBoolean)
  {
    TuSdkViewHelper.showView(paramView, paramBoolean);
  }
  
  public void showViewIn(View paramView, boolean paramBoolean)
  {
    TuSdkViewHelper.showViewIn(paramView, paramBoolean);
  }
  
  public int backStackEntryCount()
  {
    if (this.h == null) {
      return 0;
    }
    return this.h.backStackEntryCount();
  }
  
  public void pushFragment(Fragment paramFragment)
  {
    if (this.h == null) {
      return;
    }
    this.h.pushFragment(paramFragment);
  }
  
  public <T extends Fragment> void replaceFragment(Fragment paramFragment, ActivityAnimType paramActivityAnimType)
  {
    if (this.h == null) {
      return;
    }
    this.h.replaceFragment(paramFragment, paramActivityAnimType);
  }
  
  public void popFragment()
  {
    if (this.h == null) {
      return;
    }
    this.h.popFragment();
  }
  
  public void popFragment(String paramString)
  {
    if (this.h == null) {
      return;
    }
    this.h.popFragment(paramString);
  }
  
  public void popFragmentRoot()
  {
    if (this.h == null) {
      return;
    }
    this.h.popFragmentRoot();
  }
  
  public void dismissActivity()
  {
    if (this.h == null) {
      return;
    }
    this.h.dismissActivity();
  }
  
  public void dismissActivityWithAnim()
  {
    if (this.h == null) {
      return;
    }
    this.h.dismissActivityWithAnim();
  }
  
  public void dismissActivityWithAnim(ActivityAnimType paramActivityAnimType)
  {
    if (this.h == null) {
      return;
    }
    this.h.dismissActivityWithAnim(paramActivityAnimType);
  }
  
  public ActivityAnimType getDismissAnimType()
  {
    if (this.h == null) {
      return null;
    }
    return this.h.getDismissAnimType();
  }
  
  public void addOrientationListener()
  {
    if (this.l != null) {
      return;
    }
    this.l = new TuSdkOrientationEventListener(getActivity());
    this.l.setDelegate(this, null);
    this.l.enable();
  }
  
  private void a()
  {
    if (this.l == null) {
      return;
    }
    this.l.enable();
  }
  
  private void b()
  {
    if (this.l == null) {
      return;
    }
    this.l.disable();
  }
  
  private void c()
  {
    if (this.l == null) {
      return;
    }
    this.l.setDelegate(null, null);
    this.l.disable();
    this.l = null;
  }
  
  public void onOrientationChanged(InterfaceOrientation paramInterfaceOrientation)
  {
    TLog.w("you need overwrite 'public void onOrientationChanged(LasqueOrientation orien)' in class:" + getClass().getName(), new Object[0]);
  }
  
  public void setTitle(String paramString)
  {
    this.m = paramString;
    if (this.k == null) {
      return;
    }
    this.k.setTitle(paramString);
  }
  
  public void setTitle(int paramInt)
  {
    this.m = getResString(paramInt);
    setTitle(this.m);
  }
  
  public String getTitle()
  {
    if (this.k == null) {
      return this.m;
    }
    return this.k.getTitle();
  }
  
  private void b(ViewGroup paramViewGroup)
  {
    if ((this.d == 0) || (this.f == 0)) {
      return;
    }
    this.k = ((TuSdkNavigatorBar)getViewById(this.d));
    if (this.k == null) {
      return;
    }
    this.k.setButtonLayoutId(this.f);
    this.k.setBackButtonId(this.e);
    this.k.setTitle(this.m);
    this.k.delegate = this;
  }
  
  private void d()
  {
    showBackButton(backStackEntryCount() > 0);
  }
  
  public void setNavigatorBarOnButtom()
  {
    if (this.k == null) {
      return;
    }
    RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)this.k.getLayoutParams();
    if (localLayoutParams == null) {
      return;
    }
    localLayoutParams.addRule(12);
    this.k.setLayoutParams(localLayoutParams);
  }
  
  public int navigatorBarHeight()
  {
    if (this.k == null) {
      return 0;
    }
    return this.k.getHeight();
  }
  
  public void showBackButton(boolean paramBoolean)
  {
    if (this.k == null) {
      return;
    }
    this.i = paramBoolean;
    this.k.showBackButton(paramBoolean);
  }
  
  public boolean isBackButtonShowed()
  {
    if (this.k == null) {
      return false;
    }
    return this.k.isBackButtonShowed();
  }
  
  public TuSdkNavigatorButton setNavLeftButton(TuSdkNavigatorBar.TuSdkNavButtonStyleInterface paramTuSdkNavButtonStyleInterface)
  {
    return setNavLeftButton(null, paramTuSdkNavButtonStyleInterface);
  }
  
  public TuSdkNavigatorButton setNavLeftButton(String paramString, TuSdkNavigatorBar.TuSdkNavButtonStyleInterface paramTuSdkNavButtonStyleInterface)
  {
    if (this.k == null) {
      return null;
    }
    return this.k.setButton(paramString, paramTuSdkNavButtonStyleInterface, TuSdkNavigatorBar.NavigatorBarButtonType.left);
  }
  
  public TuSdkNavigatorButton setNavRightButton(TuSdkNavigatorBar.TuSdkNavButtonStyleInterface paramTuSdkNavButtonStyleInterface)
  {
    return setNavRightButton(null, paramTuSdkNavButtonStyleInterface);
  }
  
  public TuSdkNavigatorButton setNavRightButton(String paramString, TuSdkNavigatorBar.TuSdkNavButtonStyleInterface paramTuSdkNavButtonStyleInterface)
  {
    if (this.k == null) {
      return null;
    }
    return this.k.setButton(paramString, paramTuSdkNavButtonStyleInterface, TuSdkNavigatorBar.NavigatorBarButtonType.right);
  }
  
  public TuSdkNavigatorBar.NavigatorBarButtonInterface getNavButton(TuSdkNavigatorBar.NavigatorBarButtonType paramNavigatorBarButtonType)
  {
    if (this.k == null) {
      return null;
    }
    return this.k.getButton(paramNavigatorBarButtonType);
  }
  
  public void navigatorBarBackAction(TuSdkNavigatorBar.NavigatorBarButtonInterface paramNavigatorBarButtonInterface)
  {
    if (backStackEntryCount() == 0) {
      navigatorBarCancelAction(paramNavigatorBarButtonInterface);
    } else {
      popFragment();
    }
  }
  
  public void navigatorBarCancelAction(TuSdkNavigatorBar.NavigatorBarButtonInterface paramNavigatorBarButtonInterface)
  {
    dismissActivityWithAnim();
  }
  
  public void navigatorBarLeftAction(TuSdkNavigatorBar.NavigatorBarButtonInterface paramNavigatorBarButtonInterface)
  {
    TLog.i("You need overwrite navigatorBarLeftAction in " + getClass(), new Object[0]);
  }
  
  public void navigatorBarRightAction(TuSdkNavigatorBar.NavigatorBarButtonInterface paramNavigatorBarButtonInterface)
  {
    TLog.i("You need overwrite navigatorBarRightAction in " + getClass(), new Object[0]);
  }
  
  public void onNavigatorBarButtonClicked(TuSdkNavigatorBar.NavigatorBarButtonInterface paramNavigatorBarButtonInterface)
  {
    if ((paramNavigatorBarButtonInterface == null) || (paramNavigatorBarButtonInterface.getType() == null)) {
      return;
    }
    switch (2.a[paramNavigatorBarButtonInterface.getType().ordinal()])
    {
    case 1: 
      navigatorBarBackAction(paramNavigatorBarButtonInterface);
      break;
    case 2: 
      navigatorBarLeftAction(paramNavigatorBarButtonInterface);
      break;
    case 3: 
      navigatorBarRightAction(paramNavigatorBarButtonInterface);
      break;
    }
  }
  
  public void showNavigatorBar(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.k == null) {
      return;
    }
    if (!paramBoolean2)
    {
      this.k.showView(paramBoolean1);
      return;
    }
    MarginTopAnimation.showTopView(this.k, 260L, paramBoolean1);
  }
  
  public void navSegmentedAddTexts(String... paramVarArgs)
  {
    if (this.k == null) {
      return;
    }
    this.k.addSegmentedText(paramVarArgs);
  }
  
  public void navSegmentedAddTexts(int... paramVarArgs)
  {
    if (this.k == null) {
      return;
    }
    this.k.addSegmentedText(paramVarArgs);
  }
  
  public void navSegmentedSetDelegate(TuSdkSegmented.TuSdkSegmentedDelegate paramTuSdkSegmentedDelegate)
  {
    if (this.k == null) {
      return;
    }
    this.k.setSegmentedDelegate(paramTuSdkSegmentedDelegate);
  }
  
  public void navSegmentedSetected(int paramInt)
  {
    if (this.k == null) {
      return;
    }
    this.k.setSegmentedSelected(paramInt);
  }
  
  public void navSearchViewSetDelegate(TuSdkSearchView.TuSdkSearchViewDelegate paramTuSdkSearchViewDelegate)
  {
    if (this.k == null) {
      return;
    }
    this.k.setSearchViewDelegate(paramTuSdkSearchViewDelegate);
  }
  
  public void navSearchViewSearch(String paramString)
  {
    if (this.k == null) {
      return;
    }
    this.k.searchKeyword(paramString);
  }
  
  public void presentActivity(TuSdkIntent paramTuSdkIntent, ActivityAnimType paramActivityAnimType, boolean paramBoolean)
  {
    ActivityHelper.presentActivity(getActivity(), paramTuSdkIntent, paramActivityAnimType, paramBoolean);
  }
  
  public void presentActivity(Class<?> paramClass, ActivityAnimType paramActivityAnimType, boolean paramBoolean)
  {
    ActivityHelper.presentActivity(getActivity(), paramClass, paramActivityAnimType, paramBoolean);
  }
  
  public void presentModalNavigationActivity(Class<?> paramClass, Fragment paramFragment, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean1, boolean paramBoolean2)
  {
    ActivityHelper.presentModalNavigationActivity(getActivity(), paramClass, paramFragment, paramActivityAnimType1, paramActivityAnimType2, paramBoolean1, paramBoolean2);
  }
  
  public void presentModalNavigationActivity(Class<?> paramClass, Fragment paramFragment, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean)
  {
    presentModalNavigationActivity(paramClass, paramFragment, paramActivityAnimType1, paramActivityAnimType2, paramBoolean, false);
  }
  
  public void presentModalNavigationActivity(Class<?> paramClass1, Class<?> paramClass2, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean)
  {
    ActivityHelper.presentModalNavigationActivity(getActivity(), paramClass1, paramClass2, paramActivityAnimType1, paramActivityAnimType2, paramBoolean);
  }
  
  public void filpModalNavigationActivity(Class<?> paramClass, Fragment paramFragment, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.h == null) {
      return;
    }
    this.h.filpModalNavigationActivity(paramClass, paramFragment, paramBoolean1, paramBoolean2);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\activity\TuSdkFragment.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */