package org.lasque.tusdk.core.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkIntent;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.type.ActivityAnimType;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.ReflectUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.anim.AnimHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.impl.TuSpecialScreenHelper;

public abstract class TuSdkFragmentActivity
  extends FragmentActivity
  implements FragmentManager.OnBackStackChangedListener
{
  private int a;
  private int b;
  private ActivityAnimType c;
  private ActivityAnimType d;
  private ActivityAnimType e;
  private ActivityAnimType f;
  private Fragment g;
  private int h;
  private boolean i;
  private VelocityTracker j;
  private PointF k;
  public static final int MAX_SLIDE_SPEED = 1000;
  public static final float MAX_SLIDE_DISTANCE = 0.3F;
  private float l = 1.0F;
  private boolean m;
  private TuSdkFragmentActivityEventListener n;
  private boolean o;
  private Handler p;
  
  public void setAppExitInfoId(int paramInt)
  {
    this.h = paramInt;
  }
  
  public void setRootView(int paramInt1, int paramInt2)
  {
    this.a = paramInt1;
    this.b = paramInt2;
  }
  
  public void setfragmentChangeAnim(ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2)
  {
    setfragmentChangeAnim(paramActivityAnimType1, paramActivityAnimType2, null);
  }
  
  public void setfragmentChangeAnim(ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, ActivityAnimType paramActivityAnimType3)
  {
    this.d = paramActivityAnimType1;
    this.e = paramActivityAnimType2;
    this.f = paramActivityAnimType3;
  }
  
  public TuSdkFragmentActivity()
  {
    initActivity();
  }
  
  protected void initActivity()
  {
    FragmentManager localFragmentManager = getSupportFragmentManager();
    localFragmentManager.addOnBackStackChangedListener(this);
    setRootView(0, 0);
  }
  
  public void onCreate(Bundle paramBundle)
  {
    if (TuSpecialScreenHelper.isNotchScreen()) {
      setTheme(16973830);
    } else {
      c();
    }
    super.onCreate(paramBundle);
    ActivityObserver.ins.register(this);
    if (this.a == 0) {
      return;
    }
    setContentView(this.a);
    initView();
    b();
    if ((this.b == 0) || (findViewById(this.b) == null)) {
      return;
    }
    a();
    d();
  }
  
  protected void onDestroy()
  {
    ActivityObserver.ins.remove(this);
    super.onDestroy();
  }
  
  protected void initView() {}
  
  private void a()
  {
    Intent localIntent = getIntent();
    boolean bool = localIntent.getBooleanExtra("fragmentTransmit", false);
    Fragment localFragment = null;
    if (bool)
    {
      localFragment = ActivityObserver.ins.getTransmit();
    }
    else
    {
      String str = localIntent.getStringExtra("fragmentClass");
      localFragment = (Fragment)ReflectUtils.classInstance(str);
    }
    if (localFragment == null) {
      return;
    }
    pushFragment(localFragment, true);
  }
  
  private void b()
  {
    Intent localIntent = getIntent();
    String str = localIntent.getStringExtra("activityPresentAnimType");
    if (str != null)
    {
      localObject = getAnimType(str);
      if (localObject != null) {
        overridePendingTransition(((ActivityAnimType)localObject).getEnterAnim(), ((ActivityAnimType)localObject).getExitAnim());
      }
    }
    Object localObject = localIntent.getStringExtra("activityDismissAnimType");
    if (localObject != null) {
      this.c = getAnimType((String)localObject);
    }
  }
  
  protected abstract ActivityAnimType getAnimType(String paramString);
  
  public <T extends View> T getViewById(int paramInt)
  {
    View localView = findViewById(paramInt);
    return TuSdkViewHelper.loadView(localView);
  }
  
  public <T extends View> T getViewById(String paramString)
  {
    int i1 = TuSdkContext.getIDResId(paramString);
    if (i1 == 0) {
      return null;
    }
    return getViewById(i1);
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
  
  public String getResString(int paramInt)
  {
    return ContextUtils.getResString(this, paramInt);
  }
  
  public String getResString(int paramInt, Object... paramVarArgs)
  {
    return ContextUtils.getResString(this, paramInt, paramVarArgs);
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
    return ContextUtils.getResColor(this, paramInt);
  }
  
  private void c()
  {
    Intent localIntent = getIntent();
    this.i = false;
    if (!localIntent.getBooleanExtra("wantFullScreen", false)) {
      return;
    }
    this.i = true;
    requestWindowFeature(1);
    getWindow().setFlags(1024, 1024);
  }
  
  public void wantFullScreen(boolean paramBoolean)
  {
    if (TuSpecialScreenHelper.isNotchScreen())
    {
      setTheme(16973830);
      return;
    }
    this.i = paramBoolean;
    Window localWindow = getWindow();
    WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
    int i1 = 1024;
    if (paramBoolean) {
      localLayoutParams.flags |= 0x400;
    } else {
      localLayoutParams.flags &= 0xFBFF;
    }
    localWindow.setAttributes(localLayoutParams);
  }
  
  public boolean isFullScreen()
  {
    return this.i;
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  public void bindSoftInputEvent()
  {
    getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        if (0 == paramAnonymousMotionEvent.getAction()) {
          return TuSdkFragmentActivity.this.cancelEditTextFocus();
        }
        return false;
      }
    });
  }
  
  public boolean cancelEditTextFocus()
  {
    return ActivityObserver.ins.cancelEditTextFocus();
  }
  
  public ViewGroup getRootView()
  {
    ViewGroup localViewGroup = (ViewGroup)getViewById(16908290);
    if (localViewGroup.getChildCount() < 1) {
      return null;
    }
    return (ViewGroup)localViewGroup.getChildAt(0);
  }
  
  public boolean checkIntent(Intent paramIntent)
  {
    if (paramIntent == null) {
      return false;
    }
    PackageManager localPackageManager = getPackageManager();
    List localList = localPackageManager.queryIntentActivities(paramIntent, 0);
    TLog.w("checkIntent: %s", new Object[] { localList });
    boolean bool = localList.size() > 0;
    return bool;
  }
  
  public <T extends Fragment> void pushFragment(T paramT)
  {
    pushFragment(paramT, false);
  }
  
  public <T extends Fragment> void pushFragment(T paramT, boolean paramBoolean)
  {
    a(this.b, paramT, paramBoolean, false, FragmentChangeType.push);
  }
  
  public <T extends Fragment> void joinFragment(T paramT)
  {
    a(this.b, paramT, false, true, FragmentChangeType.join);
  }
  
  public <T extends Fragment> void replaceFragment(T paramT)
  {
    a(this.b, paramT, false, true, FragmentChangeType.replace);
  }
  
  public <T extends Fragment> void replaceFragment(T paramT, ActivityAnimType paramActivityAnimType)
  {
    this.f = paramActivityAnimType;
    a(this.b, paramT, false, true, FragmentChangeType.replace);
  }
  
  public <T extends Fragment> void tansformFragment(T paramT)
  {
    a(this.b, paramT, false, true, FragmentChangeType.tansform);
  }
  
  private <T extends Fragment> void a(int paramInt, T paramT, boolean paramBoolean1, boolean paramBoolean2, FragmentChangeType paramFragmentChangeType)
  {
    FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
    a(localFragmentTransaction, paramBoolean1, paramFragmentChangeType);
    if (paramFragmentChangeType != FragmentChangeType.replace) {
      hiddenLastFragment(localFragmentTransaction);
    }
    if ((paramBoolean1) && (paramFragmentChangeType == FragmentChangeType.push)) {
      this.g = paramT;
    } else if ((!paramBoolean2) && (!paramBoolean1)) {
      localFragmentTransaction.addToBackStack(paramT.getClass().getName());
    }
    bindFragmentChangeType(localFragmentTransaction, paramInt, paramT, paramFragmentChangeType);
    localFragmentTransaction.commit();
  }
  
  protected <T extends Fragment> void bindFragmentChangeType(FragmentTransaction paramFragmentTransaction, int paramInt, T paramT, FragmentChangeType paramFragmentChangeType)
  {
    switch (4.a[paramFragmentChangeType.ordinal()])
    {
    case 1: 
    case 2: 
      paramFragmentTransaction.add(paramInt, paramT, paramT.getClass().getName());
      break;
    case 3: 
      paramFragmentTransaction.show(paramT);
      break;
    case 4: 
      paramFragmentTransaction.remove(getLastFragment());
      paramFragmentTransaction.add(paramInt, paramT, paramT.getClass().getName());
      break;
    }
  }
  
  private void a(FragmentTransaction paramFragmentTransaction, boolean paramBoolean, FragmentChangeType paramFragmentChangeType)
  {
    if ((paramFragmentChangeType == FragmentChangeType.replace) && (this.f != null))
    {
      paramFragmentTransaction.setCustomAnimations(this.f.getEnterAnim(), this.f.getExitAnim());
      return;
    }
    if ((this.e == null) && (this.d == null)) {
      return;
    }
    if (this.e == null) {
      paramFragmentTransaction.setCustomAnimations(this.d.getEnterAnim(), this.d.getExitAnim());
    } else if (this.d != null) {
      if (paramBoolean) {
        paramFragmentTransaction.setCustomAnimations(0, 0, this.e.getEnterAnim(), this.e.getExitAnim());
      } else {
        paramFragmentTransaction.setCustomAnimations(this.d.getEnterAnim(), this.d.getExitAnim(), this.e.getEnterAnim(), this.e.getExitAnim());
      }
    }
  }
  
  protected void hiddenLastFragment(FragmentTransaction paramFragmentTransaction)
  {
    Fragment localFragment = getLastFragment();
    if (localFragment == null) {
      return;
    }
    if ((localFragment instanceof TuSdkFragment)) {
      ((TuSdkFragment)localFragment).onPauseFragment();
    }
    paramFragmentTransaction.hide(localFragment);
  }
  
  protected <T extends Fragment> T getLastFragment()
  {
    FragmentManager localFragmentManager = getSupportFragmentManager();
    int i1 = localFragmentManager.getBackStackEntryCount();
    if (i1 == 0) {
      return this.g;
    }
    FragmentManager.BackStackEntry localBackStackEntry = localFragmentManager.getBackStackEntryAt(i1 - 1);
    if (localBackStackEntry.getName() == null) {
      return null;
    }
    Fragment localFragment = localFragmentManager.findFragmentByTag(localBackStackEntry.getName());
    return localFragment;
  }
  
  public void popFragment()
  {
    FragmentManager localFragmentManager = getSupportFragmentManager();
    int i1 = localFragmentManager.getBackStackEntryCount();
    if (i1 == 0) {
      return;
    }
    this.m = true;
    localFragmentManager.popBackStack();
  }
  
  public void popFragment(String paramString)
  {
    if (paramString == null) {
      return;
    }
    FragmentManager localFragmentManager = getSupportFragmentManager();
    if (localFragmentManager.findFragmentByTag(paramString) == null) {
      return;
    }
    this.m = true;
    localFragmentManager.popBackStackImmediate(paramString, 0);
  }
  
  public void popFragmentRoot()
  {
    FragmentManager localFragmentManager = getSupportFragmentManager();
    int i1 = localFragmentManager.getBackStackEntryCount();
    if ((i1 == 0) && (this.g == null)) {
      return;
    }
    FragmentManager.BackStackEntry localBackStackEntry = localFragmentManager.getBackStackEntryAt(0);
    this.m = true;
    localFragmentManager.popBackStackImmediate(localBackStackEntry.getName(), 1);
  }
  
  public int backStackEntryCount()
  {
    FragmentManager localFragmentManager = getSupportFragmentManager();
    return localFragmentManager.getBackStackEntryCount();
  }
  
  public ActivityAnimType getDismissAnimType()
  {
    return this.c;
  }
  
  public void dismissActivity()
  {
    finish();
  }
  
  public void dismissActivityWithAnim()
  {
    dismissActivityWithAnim(this.c);
  }
  
  public void dismissActivityWithAnim(ActivityAnimType paramActivityAnimType)
  {
    ActivityHelper.dismissActivityWithAnim(this, paramActivityAnimType);
  }
  
  public void presentActivity(TuSdkIntent paramTuSdkIntent, ActivityAnimType paramActivityAnimType, boolean paramBoolean)
  {
    ActivityHelper.presentActivity(this, paramTuSdkIntent, paramActivityAnimType, paramBoolean);
  }
  
  public void presentActivity(Class<?> paramClass, ActivityAnimType paramActivityAnimType, boolean paramBoolean)
  {
    presentActivity(paramClass, paramActivityAnimType, false, false, paramBoolean);
  }
  
  public void presentActivity(Class<?> paramClass, ActivityAnimType paramActivityAnimType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    ActivityHelper.presentActivity(this, paramClass, paramActivityAnimType, paramBoolean1, paramBoolean2, paramBoolean3);
  }
  
  public void presentActivity(Class<?> paramClass, Fragment paramFragment, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    ActivityHelper.presentActivity(this, paramClass, paramFragment, paramActivityAnimType1, paramActivityAnimType2, paramBoolean1, paramBoolean2, paramBoolean3);
  }
  
  public void presentModalNavigationActivity(Class<?> paramClass, Fragment paramFragment, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean1, boolean paramBoolean2)
  {
    presentActivity(paramClass, paramFragment, paramActivityAnimType1, paramActivityAnimType2, paramBoolean1, false, paramBoolean2);
  }
  
  public void presentModalNavigationActivity(Class<?> paramClass, Fragment paramFragment, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean)
  {
    presentActivity(paramClass, paramFragment, paramActivityAnimType1, paramActivityAnimType2, paramBoolean, false, false);
  }
  
  public void presentModalNavigationActivity(Class<?> paramClass1, Class<?> paramClass2, ActivityAnimType paramActivityAnimType1, ActivityAnimType paramActivityAnimType2, boolean paramBoolean)
  {
    Fragment localFragment = (Fragment)ReflectUtils.classInstance(paramClass2);
    presentModalNavigationActivity(paramClass1, localFragment, paramActivityAnimType1, paramActivityAnimType2, paramBoolean);
  }
  
  public void filpModalNavigationActivity(Class<?> paramClass, Fragment paramFragment, boolean paramBoolean1, final boolean paramBoolean2)
  {
    final TuSdkIntent localTuSdkIntent = ActivityHelper.buildModalActivityIntent(this, paramClass, paramFragment, null, null, paramBoolean1, true);
    AnimHelper.rotate3dAnimtor(getRootView(), 300, 0.0F, 90.0F, 1.0F, 0.8F, new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        paramAnonymousAnimator.removeAllListeners();
        TuSdkFragmentActivity.this.presentActivity(localTuSdkIntent, null, paramBoolean2);
      }
    });
  }
  
  private void d()
  {
    Intent localIntent = getIntent();
    boolean bool = localIntent.getBooleanExtra("activityFilpAction", false);
    if (!bool) {
      return;
    }
    AnimHelper.rotate3dAnimtor(getRootView(), 300, -90.0F, 0.0F, 0.8F, 1.0F, null);
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    if (computerSildeBack(paramMotionEvent)) {
      return true;
    }
    return super.dispatchTouchEvent(paramMotionEvent);
  }
  
  protected boolean computerSildeBack(MotionEvent paramMotionEvent)
  {
    if ((this.j == null) && (paramMotionEvent.getAction() != 0)) {
      return false;
    }
    switch (paramMotionEvent.getAction())
    {
    case 0: 
      hanlderSlideBackDown(paramMotionEvent);
      break;
    case 2: 
      this.j.addMovement(paramMotionEvent);
      break;
    default: 
      return a(paramMotionEvent);
    }
    return false;
  }
  
  @SuppressLint({"Recycle"})
  protected void hanlderSlideBackDown(MotionEvent paramMotionEvent)
  {
    Fragment localFragment = getLastFragment();
    if ((localFragment == null) || (!(localFragment instanceof TuSdkFragment)) || (!((TuSdkFragment)localFragment).isSupportSlideBack())) {
      return;
    }
    if (paramMotionEvent.getX() > ContextUtils.getScreenSize(this).width * 0.2D) {
      return;
    }
    this.k = new PointF(paramMotionEvent.getX(), paramMotionEvent.getY());
    this.j = VelocityTracker.obtain();
    this.j.addMovement(paramMotionEvent);
  }
  
  private boolean a(MotionEvent paramMotionEvent)
  {
    this.j.addMovement(paramMotionEvent);
    this.j.computeCurrentVelocity(1000);
    float f1 = this.j.getXVelocity();
    float f2 = this.j.getYVelocity();
    this.j.recycle();
    this.j = null;
    if ((Math.abs(f1) < Math.abs(f2)) || (f1 < 1000.0F)) {
      return false;
    }
    float f3 = paramMotionEvent.getX() - this.k.x;
    float f4 = paramMotionEvent.getY() - this.k.y;
    if (f3 < f4) {
      return false;
    }
    if (f3 < ContextUtils.getScreenSize(this).width * 0.3F) {
      return false;
    }
    Fragment localFragment = getLastFragment();
    if ((localFragment != null) && ((localFragment instanceof TuSdkFragment))) {
      return ((TuSdkFragment)localFragment).onBackForSlide();
    }
    return false;
  }
  
  private static float b(MotionEvent paramMotionEvent)
  {
    float f1 = paramMotionEvent.getX(0) - paramMotionEvent.getX(1);
    float f2 = paramMotionEvent.getY(0) - paramMotionEvent.getY(1);
    return (float)Math.sqrt(f1 * f1 + f2 * f2);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getPointerCount() == 1) {
      return super.onTouchEvent(paramMotionEvent);
    }
    Fragment localFragment = getLastFragment();
    if ((localFragment == null) || (!(localFragment instanceof TuSdkFragmentActivityEventListener))) {
      return super.onTouchEvent(paramMotionEvent);
    }
    switch (paramMotionEvent.getAction() & 0xFF)
    {
    case 5: 
      this.l = b(paramMotionEvent);
      break;
    case 2: 
      float f1 = b(paramMotionEvent);
      if (f1 > this.l) {
        ((TuSdkFragmentActivityEventListener)localFragment).onActivityTouchMotionDispatcher(this, true);
      } else {
        ((TuSdkFragmentActivityEventListener)localFragment).onActivityTouchMotionDispatcher(this, false);
      }
      this.l = f1;
      break;
    }
    return true;
  }
  
  public void onBackStackChanged()
  {
    Fragment localFragment = getLastFragment();
    if ((this.m) && (localFragment != null) && ((localFragment instanceof TuSdkFragment))) {
      ((TuSdkFragment)localFragment).onResumeFragment();
    }
    this.m = false;
  }
  
  public void onBackPressed()
  {
    if (isDispatchkeyListener(4)) {
      return;
    }
    if (f()) {
      return;
    }
    this.m = true;
    Fragment localFragment = getLastFragment();
    if ((localFragment != null) && ((localFragment instanceof TuSdkFragment)) && (!((TuSdkFragment)localFragment).onBackPressed())) {
      return;
    }
    if (backStackEntryCount() == 0)
    {
      dismissActivityWithAnim();
      return;
    }
    super.onBackPressed();
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    Fragment localFragment = getLastFragment();
    if ((localFragment == null) || (!(localFragment instanceof TuSdkFragmentActivityEventListener)) || (paramKeyEvent.getKeyCode() == 4)) {
      return super.dispatchKeyEvent(paramKeyEvent);
    }
    return ((TuSdkFragmentActivityEventListener)localFragment).onActivityKeyDispatcher(this, paramKeyEvent.getKeyCode());
  }
  
  public void setActivityKeyListener(TuSdkFragmentActivityEventListener paramTuSdkFragmentActivityEventListener)
  {
    this.n = paramTuSdkFragmentActivityEventListener;
  }
  
  public boolean isDispatchkeyListener(int paramInt)
  {
    if (this.n == null) {
      return false;
    }
    return this.n.onActivityKeyDispatcher(this, paramInt);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 82) && (isDispatchkeyListener(paramInt))) {
      return true;
    }
    Fragment localFragment = getLastFragment();
    if ((localFragment != null) && ((localFragment instanceof TuSdkFragment)) && (((TuSdkFragment)localFragment).onKeyUp(paramInt, paramKeyEvent))) {
      return true;
    }
    return super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  private void e()
  {
    if (this.p != null) {
      return;
    }
    this.p = new Handler(new Handler.Callback()
    {
      public boolean handleMessage(Message paramAnonymousMessage)
      {
        TuSdkFragmentActivity.a(TuSdkFragmentActivity.this, false);
        return false;
      }
    });
  }
  
  private boolean f()
  {
    if (this.h == 0) {
      return false;
    }
    e();
    if (!this.o)
    {
      this.o = true;
      TuSdkViewHelper.toast(this, this.h);
      this.p.sendEmptyMessageDelayed(0, 2000L);
      return true;
    }
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.addCategory("android.intent.category.HOME");
    startActivity(localIntent);
    onApplicationWillExit();
    ActivityObserver.ins.exitApp();
    return true;
  }
  
  protected void onApplicationWillExit() {}
  
  public static abstract interface TuSdkFragmentActivityEventListener
  {
    public abstract boolean onActivityKeyDispatcher(TuSdkFragmentActivity paramTuSdkFragmentActivity, int paramInt);
    
    public abstract boolean onActivityTouchMotionDispatcher(TuSdkFragmentActivity paramTuSdkFragmentActivity, boolean paramBoolean);
  }
  
  public static enum FragmentChangeType
  {
    private FragmentChangeType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\activity\TuSdkFragmentActivity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */