// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.activity;

//import org.lasque.tusdk.core.TuSdkIntent;
//import org.lasque.tusdk.core.view.widget.TuSdkSearchView;
//import org.lasque.tusdk.core.view.widget.TuSdkSegmented;
//import org.lasque.tusdk.core.utils.anim.MarginTopAnimation;
//import org.lasque.tusdk.core.view.widget.button.TuSdkNavigatorButton;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkIntent;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener.AnimationListenerAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener.TuSdkOrientationEventListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ActivityAnimType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.MarginTopAnimation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.TuSdkSearchView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.TuSdkSegmented;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.button.TuSdkNavigatorButton;

//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
//import org.lasque.tusdk.core.type.ActivityAnimType;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.listener.AnimationListenerAdapter;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
//import org.lasque.tusdk.core.listener.TuSdkOrientationEventListener;

public abstract class TuSdkFragment extends Fragment implements ViewTreeObserver.OnPreDrawListener, TuSdkOrientationEventListener.TuSdkOrientationDelegate, TuSdkNavigatorBar.TuSdkNavigatorBarDelegate
{
    private int a;
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
    
    public TuSdkFragment() {
        this.a = 0;
    }
    
    public void setRootViewLayoutId(final int a) {
        if (this.a == 0) {
            this.a = a;
        }
    }
    
    public int getRootViewLayoutId() {
        return this.a;
    }
    
    public boolean isSupportSlideBack() {
        return this.i;
    }
    
    public void setIsSupportSlideBack(final boolean i) {
        this.i = i;
    }
    
    public void setNavigatorBarId(final int d, final int e, final int f) {
        this.d = d;
        this.e = e;
        this.f = f;
    }
    
    public Fragment getOriginFragment() {
        return this.g;
    }
    
    public void setOriginFragment(final Fragment g) {
        this.g = g;
    }
    
    public TuSdkFragmentActivity getSdkActivity() {
        if (this.h != null) {
            return this.h;
        }
        final FragmentActivity activity = this.getActivity();
        if (activity != null && activity instanceof TuSdkFragmentActivity) {
            this.h = (TuSdkFragmentActivity)activity;
        }
        return this.h;
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.initCreateView();
        if (this.a == 0) {
            TLog.e("can not defind rootViewId", new Object[0]);
            return (View)this.b;
        }
        this.b = TuSdkViewHelper.buildView((Context)this.getActivity(), this.a, viewGroup);
        this.b.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this);
        this.b(this.b);
        this.loadView(this.b);
        return (View)this.b;
    }
    
    protected abstract void initCreateView();
    
    public Animation onCreateAnimation(final int n, final boolean b, final int n2) {
        if (n2 == 0) {
            return super.onCreateAnimation(n, b, n2);
        }
        final Animation loadAnimation = AnimationUtils.loadAnimation((Context)this.getActivity(), n2);
        if (loadAnimation == null) {
            return loadAnimation;
        }
        loadAnimation.setAnimationListener((Animation.AnimationListener)new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animation animation) {
                animation.setAnimationListener((Animation.AnimationListener)null);
                TuSdkFragment.this.onFragmentAnimationEnd(b, TuSdkFragment.this.c);
                TuSdkFragment.this.c = true;
            }
        });
        return loadAnimation;
    }
    
    protected void onFragmentAnimationEnd(final boolean b, final boolean b2) {
    }
    
    protected abstract void loadView(final ViewGroup p0);
    
    public boolean onPreDraw() {
        this.a(this.b);
        this.b.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this);
        return true;
    }
    
    protected void navigatorBarLoaded(final TuSdkNavigatorBar tuSdkNavigatorBar) {
    }
    
    protected abstract void viewDidLoad(final ViewGroup p0);
    
    private void a(final ViewGroup viewGroup) {
        this.d();
        this.navigatorBarLoaded(this.k);
        this.viewDidLoad(this.b);
    }
    
    public void onResume() {
        super.onResume();
        if (this.j) {
            return;
        }
        this.a();
    }
    
    public void onPause() {
        this.b();
        super.onPause();
    }
    
    public void onDestroyView() {
        this.g = null;
        if (this.k != null) {
            this.k.viewWillDestory();
        }
        super.onDestroyView();
    }
    
    public void onDetach() {
        this.c();
        TuSdkViewHelper.viewWillDestory((View)this.b);
        super.onDetach();
    }
    
    public void onDestroy() {
        this.c();
        TuSdkViewHelper.viewWillDestory(this.getView());
        super.onDestroy();
    }
    
    public void onPauseFragment() {
        this.j = true;
        this.onPause();
    }
    
    public void onResumeFragment() {
        this.j = false;
        this.onResume();
    }
    
    public boolean isFragmentPause() {
        return this.j;
    }
    
    public boolean onBackPressed() {
        this.c();
        return true;
    }
    
    public boolean onBackForSlide() {
        if (this.i) {
            this.navigatorBarBackAction(null);
        }
        return this.i;
    }
    
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        if (activity instanceof TuSdkFragmentActivity) {
            this.h = (TuSdkFragmentActivity)activity;
        }
    }
    
    public void onRefreshData(final int n) {
    }
    
    public void refreshOriginFragment(final int n) {
        if (this.g != null && this.g instanceof TuSdkFragment) {
            ((TuSdkFragment)this.g).onRefreshData(n);
        }
    }
    
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        return false;
    }
    
    public <T extends View> T getViewById(final int n) {
        if (this.b == null) {
            return null;
        }
        return TuSdkViewHelper.loadView(this.b.findViewById(n));
    }
    
    public <T extends View> T getViewById(final String s) {
        final int idResId = TuSdkContext.getIDResId(s);
        if (idResId == 0) {
            return null;
        }
        return (T)this.getViewById(idResId);
    }
    
    public int getViewId(final View view) {
        if (view == null) {
            return 0;
        }
        return view.getId();
    }
    
    public boolean equalViewIds(final View view, final View view2) {
        return this.getViewId(view) == this.getViewId(view2);
    }
    
    public <T extends ViewGroup> T getRootView() {
        if (this.b == null) {
            return null;
        }
        return (T)this.b;
    }
    
    public String getResString(final int n) {
        return ContextUtils.getResString((Context)this.getActivity(), n);
    }
    
    public String getResString(final int n, final Object... array) {
        return ContextUtils.getResString((Context)this.getActivity(), n, array);
    }
    
    public String getResString(final String s) {
        return TuSdkContext.getString(s);
    }
    
    public String getResString(final String s, final Object... array) {
        return TuSdkContext.getString(s, array);
    }
    
    public int getResColor(final int n) {
        return ContextUtils.getResColor((Context)this.getActivity(), n);
    }
    
    public void wantFullScreen(final boolean b) {
        if (this.h == null) {
            return;
        }
        this.h.wantFullScreen(b);
    }
    
    public boolean isFullScreen() {
        return this.h != null && this.h.isFullScreen();
    }
    
    public void bindSoftInputEvent() {
        if (this.h == null) {
            return;
        }
        this.h.bindSoftInputEvent();
    }
    
    public void setRequestedOrientation(final int requestedOrientation) {
        if (this.h == null) {
            return;
        }
        this.h.setRequestedOrientation(requestedOrientation);
    }
    
    public void runOnUiThread(final Runnable runnable) {
        if (this.getActivity() == null) {
            return;
        }
        this.getActivity().runOnUiThread(runnable);
    }
    
    public void showView(final View view, final boolean b) {
        TuSdkViewHelper.showView(view, b);
    }
    
    public void showViewIn(final View view, final boolean b) {
        TuSdkViewHelper.showViewIn(view, b);
    }
    
    public int backStackEntryCount() {
        if (this.h == null) {
            return 0;
        }
        return this.h.backStackEntryCount();
    }
    
    public void pushFragment(final Fragment fragment) {
        if (this.h == null) {
            return;
        }
        this.h.pushFragment(fragment);
    }
    
    public <T extends Fragment> void replaceFragment(final Fragment fragment, final ActivityAnimType activityAnimType) {
        if (this.h == null) {
            return;
        }
        this.h.replaceFragment(fragment, activityAnimType);
    }
    
    public void popFragment() {
        if (this.h == null) {
            return;
        }
        this.h.popFragment();
    }
    
    public void popFragment(final String s) {
        if (this.h == null) {
            return;
        }
        this.h.popFragment(s);
    }
    
    public void popFragmentRoot() {
        if (this.h == null) {
            return;
        }
        this.h.popFragmentRoot();
    }
    
    public void dismissActivity() {
        if (this.h == null) {
            return;
        }
        this.h.dismissActivity();
    }
    
    public void dismissActivityWithAnim() {
        if (this.h == null) {
            return;
        }
        this.h.dismissActivityWithAnim();
    }
    
    public void dismissActivityWithAnim(final ActivityAnimType activityAnimType) {
        if (this.h == null) {
            return;
        }
        this.h.dismissActivityWithAnim(activityAnimType);
    }
    
    public ActivityAnimType getDismissAnimType() {
        if (this.h == null) {
            return null;
        }
        return this.h.getDismissAnimType();
    }
    
    public void addOrientationListener() {
        if (this.l != null) {
            return;
        }
        (this.l = new TuSdkOrientationEventListener((Context)this.getActivity())).setDelegate(this, null);
        this.l.enable();
    }
    
    private void a() {
        if (this.l == null) {
            return;
        }
        this.l.enable();
    }
    
    private void b() {
        if (this.l == null) {
            return;
        }
        this.l.disable();
    }
    
    private void c() {
        if (this.l == null) {
            return;
        }
        this.l.setDelegate(null, null);
        this.l.disable();
        this.l = null;
    }
    
    public void onOrientationChanged(final InterfaceOrientation interfaceOrientation) {
        TLog.w("you need overwrite 'public void onOrientationChanged(LasqueOrientation orien)' in class:" + this.getClass().getName(), new Object[0]);
    }
    
    public void setTitle(final String s) {
        this.m = s;
        if (this.k == null) {
            return;
        }
        this.k.setTitle(s);
    }
    
    public void setTitle(final int n) {
        this.setTitle(this.m = this.getResString(n));
    }
    
    public String getTitle() {
        if (this.k == null) {
            return this.m;
        }
        return this.k.getTitle();
    }
    
    private void b(final ViewGroup viewGroup) {
        if (this.d == 0 || this.f == 0) {
            return;
        }
        this.k = this.getViewById(this.d);
        if (this.k == null) {
            return;
        }
        this.k.setButtonLayoutId(this.f);
        this.k.setBackButtonId(this.e);
        this.k.setTitle(this.m);
        this.k.delegate = this;
    }
    
    private void d() {
        this.showBackButton(this.backStackEntryCount() > 0);
    }
    
    public void setNavigatorBarOnButtom() {
        if (this.k == null) {
            return;
        }
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.k.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.addRule(12);
        this.k.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }
    
    public int navigatorBarHeight() {
        if (this.k == null) {
            return 0;
        }
        return this.k.getHeight();
    }
    
    public void showBackButton(final boolean i) {
        if (this.k == null) {
            return;
        }
        this.i = i;
        this.k.showBackButton(i);
    }
    
    public boolean isBackButtonShowed() {
        return this.k != null && this.k.isBackButtonShowed();
    }
    
    public TuSdkNavigatorButton setNavLeftButton(final TuSdkNavigatorBar.TuSdkNavButtonStyleInterface tuSdkNavButtonStyleInterface) {
        return this.setNavLeftButton(null, tuSdkNavButtonStyleInterface);
    }
    
    public TuSdkNavigatorButton setNavLeftButton(final String s, final TuSdkNavigatorBar.TuSdkNavButtonStyleInterface tuSdkNavButtonStyleInterface) {
        if (this.k == null) {
            return null;
        }
        return this.k.setButton(s, tuSdkNavButtonStyleInterface, TuSdkNavigatorBar.NavigatorBarButtonType.left);
    }
    
    public TuSdkNavigatorButton setNavRightButton(final TuSdkNavigatorBar.TuSdkNavButtonStyleInterface tuSdkNavButtonStyleInterface) {
        return this.setNavRightButton(null, tuSdkNavButtonStyleInterface);
    }
    
    public TuSdkNavigatorButton setNavRightButton(final String s, final TuSdkNavigatorBar.TuSdkNavButtonStyleInterface tuSdkNavButtonStyleInterface) {
        if (this.k == null) {
            return null;
        }
        return this.k.setButton(s, tuSdkNavButtonStyleInterface, TuSdkNavigatorBar.NavigatorBarButtonType.right);
    }
    
    public TuSdkNavigatorBar.NavigatorBarButtonInterface getNavButton(final TuSdkNavigatorBar.NavigatorBarButtonType navigatorBarButtonType) {
        if (this.k == null) {
            return null;
        }
        return this.k.getButton(navigatorBarButtonType);
    }
    
    public void navigatorBarBackAction(final TuSdkNavigatorBar.NavigatorBarButtonInterface navigatorBarButtonInterface) {
        if (this.backStackEntryCount() == 0) {
            this.navigatorBarCancelAction(navigatorBarButtonInterface);
        }
        else {
            this.popFragment();
        }
    }
    
    public void navigatorBarCancelAction(final TuSdkNavigatorBar.NavigatorBarButtonInterface navigatorBarButtonInterface) {
        this.dismissActivityWithAnim();
    }
    
    public void navigatorBarLeftAction(final TuSdkNavigatorBar.NavigatorBarButtonInterface navigatorBarButtonInterface) {
        TLog.i("You need overwrite navigatorBarLeftAction in " + this.getClass(), new Object[0]);
    }
    
    public void navigatorBarRightAction(final TuSdkNavigatorBar.NavigatorBarButtonInterface navigatorBarButtonInterface) {
        TLog.i("You need overwrite navigatorBarRightAction in " + this.getClass(), new Object[0]);
    }
    
    public void onNavigatorBarButtonClicked(final TuSdkNavigatorBar.NavigatorBarButtonInterface navigatorBarButtonInterface) {
        if (navigatorBarButtonInterface == null || navigatorBarButtonInterface.getType() == null) {
            return;
        }
        switch (navigatorBarButtonInterface.getType().ordinal()) {
            case 1: {
                this.navigatorBarBackAction(navigatorBarButtonInterface);
                break;
            }
            case 2: {
                this.navigatorBarLeftAction(navigatorBarButtonInterface);
                break;
            }
            case 3: {
                this.navigatorBarRightAction(navigatorBarButtonInterface);
                break;
            }
        }
    }
    
    public void showNavigatorBar(final boolean b, final boolean b2) {
        if (this.k == null) {
            return;
        }
        if (!b2) {
            this.k.showView(b);
            return;
        }
        MarginTopAnimation.showTopView((View)this.k, 260L, b);
    }
    
    public void navSegmentedAddTexts(final String... array) {
        if (this.k == null) {
            return;
        }
        this.k.addSegmentedText(array);
    }
    
    public void navSegmentedAddTexts(final int... array) {
        if (this.k == null) {
            return;
        }
        this.k.addSegmentedText(array);
    }
    
    public void navSegmentedSetDelegate(final TuSdkSegmented.TuSdkSegmentedDelegate segmentedDelegate) {
        if (this.k == null) {
            return;
        }
        this.k.setSegmentedDelegate(segmentedDelegate);
    }
    
    public void navSegmentedSetected(final int segmentedSelected) {
        if (this.k == null) {
            return;
        }
        this.k.setSegmentedSelected(segmentedSelected);
    }
    
    public void navSearchViewSetDelegate(final TuSdkSearchView.TuSdkSearchViewDelegate searchViewDelegate) {
        if (this.k == null) {
            return;
        }
        this.k.setSearchViewDelegate(searchViewDelegate);
    }
    
    public void navSearchViewSearch(final String s) {
        if (this.k == null) {
            return;
        }
        this.k.searchKeyword(s);
    }
    
    public void presentActivity(final TuSdkIntent tuSdkIntent, final ActivityAnimType activityAnimType, final boolean b) {
        ActivityHelper.presentActivity((AppCompatActivity)this.getActivity(), tuSdkIntent, activityAnimType, b);
    }
    
    public void presentActivity(final Class<?> clazz, final ActivityAnimType activityAnimType, final boolean b) {
        ActivityHelper.presentActivity((AppCompatActivity)this.getActivity(), clazz, activityAnimType, b);
    }
    
    public void presentModalNavigationActivity(final Class<?> clazz, final Fragment fragment, final ActivityAnimType activityAnimType, final ActivityAnimType activityAnimType2, final boolean b, final boolean b2) {
        ActivityHelper.presentModalNavigationActivity((AppCompatActivity)this.getActivity(), clazz, fragment, activityAnimType, activityAnimType2, b, b2);
    }
    
    public void presentModalNavigationActivity(final Class<?> clazz, final Fragment fragment, final ActivityAnimType activityAnimType, final ActivityAnimType activityAnimType2, final boolean b) {
        this.presentModalNavigationActivity(clazz, fragment, activityAnimType, activityAnimType2, b, false);
    }
    
    /*public void presentModalNavigationActivity(final Class<?> clazz, final Class<?> clazz2, final ActivityAnimType activityAnimType, final ActivityAnimType activityAnimType2, final boolean b) {
        ActivityHelper.presentModalNavigationActivity((AppCompatActivity)this.getActivity(), clazz, clazz2, activityAnimType, activityAnimType2, b);
    }*/
    
    public void filpModalNavigationActivity(final Class<?> clazz, final Fragment fragment, final boolean b, final boolean b2) {
        if (this.h == null) {
            return;
        }
        this.h.filpModalNavigationActivity(clazz, fragment, b, b2);
    }
}
