// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkIntent;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ActivityAnimType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ReflectUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AnimHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.TuSpecialScreenHelper;

import java.util.List;

//import org.lasque.tusdk.core.utils.anim.AnimHelper;
//import org.lasque.tusdk.core.TuSdkIntent;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
//import org.lasque.tusdk.core.utils.ReflectUtils;
//import org.lasque.tusdk.impl.TuSpecialScreenHelper;
//import org.lasque.tusdk.core.type.ActivityAnimType;

public abstract class TuSdkFragmentActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener
{
    private int layout;
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
    public static final float MAX_SLIDE_DISTANCE = 0.3f;
    private float l;
    private boolean m;
    private TuSdkFragmentActivityEventListener n;
    private boolean o;
    private Handler p;

    public void setAppExitInfoId(final int h) {
        this.h = h;
    }

    public void setRootView(final int a, final int b) {
        this.layout = a;
        this.b = b;
    }

    public void setfragmentChangeAnim(final ActivityAnimType activityAnimType, final ActivityAnimType activityAnimType2) {
        this.setfragmentChangeAnim(activityAnimType, activityAnimType2, null);
    }

    public void setfragmentChangeAnim(final ActivityAnimType d, final ActivityAnimType e, final ActivityAnimType f) {
        this.d = d;
        this.e = e;
        this.f = f;
    }

    public TuSdkFragmentActivity() {
        this.l = 1.0f;
        this.initActivity();
    }

    protected void initActivity() {
        this.getSupportFragmentManager().addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener)this);
        this.setRootView(0, 0);
    }

    @SuppressLint("ResourceType")
    public void onCreate(final Bundle bundle) {
        if (TuSpecialScreenHelper.isNotchScreen()) {
            this.setTheme(16973830);
        }
        else {
            this.c();
        }
        super.onCreate(bundle);
        ActivityObserver.ins.register((AppCompatActivity) this);
        if (this.layout == 0) {
            return;
        }
        this.setContentView(this.layout);
        this.initView();
        this.b();
        if (this.b == 0 || this.findViewById(this.b) == null) {
            return;
        }
        this.a();
        this.d();
    }

    protected void onDestroy() {
        ActivityObserver.ins.remove((AppCompatActivity) this);
        super.onDestroy();
    }

    protected void initView() {
    }

    private void a() {
        final Intent intent = this.getIntent();
        Fragment transmit;
        if (intent.getBooleanExtra("fragmentTransmit", false)) {
            transmit = ActivityObserver.ins.getTransmit();
        }
        else {
            transmit = ReflectUtils.classInstance(intent.getStringExtra("fragmentClass"));
        }
        if (transmit == null) {
            return;
        }
        this.pushFragment(transmit, true);
    }

    private void b() {
        final Intent intent = this.getIntent();
        final String stringExtra = intent.getStringExtra("activityPresentAnimType");
        if (stringExtra != null) {
            final ActivityAnimType animType = this.getAnimType(stringExtra);
            if (animType != null) {
                this.overridePendingTransition(animType.getEnterAnim(), animType.getExitAnim());
            }
        }
        final String stringExtra2 = intent.getStringExtra("activityDismissAnimType");
        if (stringExtra2 != null) {
            this.c = this.getAnimType(stringExtra2);
        }
    }

    protected abstract ActivityAnimType getAnimType(final String p0);

    public <T extends View> T getViewById(final int n) {
        return TuSdkViewHelper.loadView(this.findViewById(n));
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

    public String getResString(final int n) {
        return ContextUtils.getResString((Context)this, n);
    }

    public String getResString(final int n, final Object... array) {
        return ContextUtils.getResString((Context)this, n, array);
    }

    public String getResString(final String s) {
        return TuSdkContext.getString(s);
    }

    public String getResString(final String s, final Object... array) {
        return TuSdkContext.getString(s, array);
    }

    public int getResColor(final int n) {
        return ContextUtils.getResColor((Context)this, n);
    }

    private void c() {
        final Intent intent = this.getIntent();
        this.i = false;
        if (!intent.getBooleanExtra("wantFullScreen", false)) {
            return;
        }
        this.i = true;
        this.requestWindowFeature(1);
        this.getWindow().setFlags(1024, 1024);
    }

    @SuppressLint("ResourceType")
    public void wantFullScreen(final boolean i) {
        if (TuSpecialScreenHelper.isNotchScreen()) {
            this.setTheme(16973830);
            return;
        }
        this.i = i;
        final Window window = this.getWindow();
        final WindowManager.LayoutParams attributes = window.getAttributes();
        if (i) {
            final WindowManager.LayoutParams layoutParams = attributes;
            layoutParams.flags |= 0x400;
        }
        else {
            final WindowManager.LayoutParams layoutParams2 = attributes;
            layoutParams2.flags &= 0xFFFFFBFF;
        }
        window.setAttributes(attributes);
    }

    public boolean isFullScreen() {
        return this.i;
    }

    @SuppressLint({ "ClickableViewAccessibility" })
    public void bindSoftInputEvent() {
        this.getWindow().getDecorView().setOnTouchListener((View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return 0 == motionEvent.getAction() && TuSdkFragmentActivity.this.cancelEditTextFocus();
            }
        });
    }

    public boolean cancelEditTextFocus() {
        return ActivityObserver.ins.cancelEditTextFocus();
    }

    public ViewGroup getRootView() {
        final ViewGroup viewGroup = this.getViewById(16908290);
        if (viewGroup.getChildCount() < 1) {
            return null;
        }
        return (ViewGroup)viewGroup.getChildAt(0);
    }

    public boolean checkIntent(final Intent intent) {
        if (intent == null) {
            return false;
        }
        final List queryIntentActivities = this.getPackageManager().queryIntentActivities(intent, 0);
        TLog.w("checkIntent: %s", queryIntentActivities);
        return queryIntentActivities.size() > 0;
    }

    public <T extends Fragment> void pushFragment(final T t) {
        this.pushFragment(t, false);
    }

    public <T extends Fragment> void pushFragment(final T t, final boolean b) {
        this.a(this.b, t, b, false, FragmentChangeType.push);
    }

    public <T extends Fragment> void joinFragment(final T t) {
        this.a(this.b, t, false, true, FragmentChangeType.join);
    }

    public <T extends Fragment> void replaceFragment(final T t) {
        this.a(this.b, t, false, true, FragmentChangeType.replace);
    }

    public <T extends Fragment> void replaceFragment(final T t, final ActivityAnimType f) {
        this.f = f;
        this.a(this.b, t, false, true, FragmentChangeType.replace);
    }

    public <T extends Fragment> void tansformFragment(final T t) {
        this.a(this.b, t, false, true, FragmentChangeType.tansform);
    }

    private <T extends Fragment> void a(final int n, final T g, final boolean b, final boolean b2, final FragmentChangeType fragmentChangeType) {
        final FragmentTransaction beginTransaction = this.getSupportFragmentManager().beginTransaction();
        this.a(beginTransaction, b, fragmentChangeType);
        if (fragmentChangeType != FragmentChangeType.replace) {
            this.hiddenLastFragment(beginTransaction);
        }
        if (b && fragmentChangeType == FragmentChangeType.push) {
            this.g = g;
        }
        else if (!b2 && !b) {
            beginTransaction.addToBackStack(g.getClass().getName());
        }
        this.bindFragmentChangeType(beginTransaction, n, g, fragmentChangeType);
        beginTransaction.commit();
    }

    protected <T extends Fragment> void bindFragmentChangeType(final FragmentTransaction fragmentTransaction, final int n, final T t, final FragmentChangeType fragmentChangeType) {
        switch (fragmentChangeType.ordinal()) {
            case 1:
            case 2: {
                fragmentTransaction.add(n, (Fragment)t, t.getClass().getName());
                break;
            }
            case 3: {
                fragmentTransaction.show((Fragment)t);
                break;
            }
            case 4: {
                fragmentTransaction.remove(this.getLastFragment());
                fragmentTransaction.add(n, (Fragment)t, t.getClass().getName());
                break;
            }
        }
    }

    private void a(final FragmentTransaction fragmentTransaction, final boolean b, final FragmentChangeType fragmentChangeType) {
        if (fragmentChangeType == FragmentChangeType.replace && this.f != null) {
            fragmentTransaction.setCustomAnimations(this.f.getEnterAnim(), this.f.getExitAnim());
            return;
        }
        if (this.e == null && this.d == null) {
            return;
        }
        if (this.e == null) {
            fragmentTransaction.setCustomAnimations(this.d.getEnterAnim(), this.d.getExitAnim());
        }
        else if (this.d != null) {
            if (b) {
                fragmentTransaction.setCustomAnimations(0, 0, this.e.getEnterAnim(), this.e.getExitAnim());
            }
            else {
                fragmentTransaction.setCustomAnimations(this.d.getEnterAnim(), this.d.getExitAnim(), this.e.getEnterAnim(), this.e.getExitAnim());
            }
        }
    }

    protected void hiddenLastFragment(final FragmentTransaction fragmentTransaction) {
        final TuSdkFragment lastFragment = this.getLastFragment();
        if (lastFragment == null) {
            return;
        }
        if (lastFragment instanceof TuSdkFragment) {
            lastFragment.onPauseFragment();
        }
        fragmentTransaction.hide((Fragment)lastFragment);
    }

    protected <T extends Fragment> T getLastFragment() {
        final FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        final int backStackEntryCount = supportFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            return (T)this.g;
        }
        final FragmentManager.BackStackEntry backStackEntry = supportFragmentManager.getBackStackEntryAt(backStackEntryCount - 1);
        if (backStackEntry.getName() == null) {
            return null;
        }
        return (T)supportFragmentManager.findFragmentByTag(backStackEntry.getName());
    }

    public void popFragment() {
        final FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        if (supportFragmentManager.getBackStackEntryCount() == 0) {
            return;
        }
        this.m = true;
        supportFragmentManager.popBackStack();
    }

    public void popFragment(final String s) {
        if (s == null) {
            return;
        }
        final FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        if (supportFragmentManager.findFragmentByTag(s) == null) {
            return;
        }
        this.m = true;
        supportFragmentManager.popBackStackImmediate(s, 0);
    }

    public void popFragmentRoot() {
        final FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        if (supportFragmentManager.getBackStackEntryCount() == 0 && this.g == null) {
            return;
        }
        final FragmentManager.BackStackEntry backStackEntry = supportFragmentManager.getBackStackEntryAt(0);
        this.m = true;
        supportFragmentManager.popBackStackImmediate(backStackEntry.getName(), 1);
    }

    public int backStackEntryCount() {
        return this.getSupportFragmentManager().getBackStackEntryCount();
    }

    public ActivityAnimType getDismissAnimType() {
        return this.c;
    }

    public void dismissActivity() {
        this.finish();
    }

    public void dismissActivityWithAnim() {
        this.dismissActivityWithAnim(this.c);
    }

    public void dismissActivityWithAnim(final ActivityAnimType activityAnimType) {
        ActivityHelper.dismissActivityWithAnim((AppCompatActivity)this, activityAnimType);
    }

    public void presentActivity(final TuSdkIntent tuSdkIntent, final ActivityAnimType activityAnimType, final boolean b) {
        ActivityHelper.presentActivity((AppCompatActivity)this, tuSdkIntent, activityAnimType, b);
    }

    public void presentActivity(final Class<?> clazz, final ActivityAnimType activityAnimType, final boolean b) {
        this.presentActivity(clazz, activityAnimType, false, false, b);
    }

    public void presentActivity(final Class<?> clazz, final ActivityAnimType activityAnimType, final boolean b, final boolean b2, final boolean b3) {
        ActivityHelper.presentActivity((AppCompatActivity)this, clazz, activityAnimType, b, b2, b3);
    }

    public void presentActivity(final Class<?> clazz, final Fragment fragment, final ActivityAnimType activityAnimType, final ActivityAnimType activityAnimType2, final boolean b, final boolean b2, final boolean b3) {
        ActivityHelper.presentActivity((AppCompatActivity)this, clazz, fragment, activityAnimType, activityAnimType2, b, b2, b3);
    }

    public void presentModalNavigationActivity(final Class<?> clazz, final Fragment fragment, final ActivityAnimType activityAnimType, final ActivityAnimType activityAnimType2, final boolean b, final boolean b2) {
        this.presentActivity(clazz, fragment, activityAnimType, activityAnimType2, b, false, b2);
    }

    public void presentModalNavigationActivity(final Class<?> clazz, final Fragment fragment, final ActivityAnimType activityAnimType, final ActivityAnimType activityAnimType2, final boolean b) {
        this.presentActivity(clazz, fragment, activityAnimType, activityAnimType2, b, false, false);
    }

  /*  public void presentModalNavigationActivity(final Class<?> clazz, final Class<?> clazz2, final ActivityAnimType activityAnimType, final ActivityAnimType activityAnimType2, final boolean b) {
        this.presentModalNavigationActivity(clazz, ReflectUtils.classInstance(clazz2), activityAnimType, activityAnimType2, b);
    }*/

    public void filpModalNavigationActivity(final Class<?> clazz, final Fragment fragment, final boolean b, final boolean b2) {
        AnimHelper.rotate3dAnimtor((View)this.getRootView(), 300, 0.0f, 90.0f, 1.0f, 0.8f, (Animator.AnimatorListener)new AnimatorListenerAdapter() {
            final /* synthetic */ TuSdkIntent a = ActivityHelper.buildModalActivityIntent(TuSdkFragmentActivity.this, clazz, fragment, null, null, b, true);

            public void onAnimationEnd(final Animator animator) {
                animator.removeAllListeners();
                TuSdkFragmentActivity.this.presentActivity(this.a, null, b2);
            }
        });
    }

    private void d() {
        if (!this.getIntent().getBooleanExtra("activityFilpAction", false)) {
            return;
        }
        AnimHelper.rotate3dAnimtor((View)this.getRootView(), 300, -90.0f, 0.0f, 0.8f, 1.0f, null);
    }

    public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
        return this.computerSildeBack(motionEvent) || super.dispatchTouchEvent(motionEvent);
    }

    protected boolean computerSildeBack(final MotionEvent motionEvent) {
        if (this.j == null && motionEvent.getAction() != 0) {
            return false;
        }
        switch (motionEvent.getAction()) {
            case 0: {
                this.hanlderSlideBackDown(motionEvent);
                break;
            }
            case 2: {
                this.j.addMovement(motionEvent);
                break;
            }
            default: {
                return this.a(motionEvent);
            }
        }
        return false;
    }

    @SuppressLint({ "Recycle" })
    protected void hanlderSlideBackDown(final MotionEvent motionEvent) {
        final TuSdkFragment lastFragment = this.getLastFragment();
        if (lastFragment == null || !(lastFragment instanceof TuSdkFragment) || !lastFragment.isSupportSlideBack()) {
            return;
        }
        if (motionEvent.getX() > ContextUtils.getScreenSize((Context)this).width * 0.2) {
            return;
        }
        this.k = new PointF(motionEvent.getX(), motionEvent.getY());
        (this.j = VelocityTracker.obtain()).addMovement(motionEvent);
    }

    private boolean a(final MotionEvent motionEvent) {
        this.j.addMovement(motionEvent);
        this.j.computeCurrentVelocity(1000);
        final float xVelocity = this.j.getXVelocity();
        final float yVelocity = this.j.getYVelocity();
        this.j.recycle();
        this.j = null;
        if (Math.abs(xVelocity) < Math.abs(yVelocity) || xVelocity < 1000.0f) {
            return false;
        }
        final float n = motionEvent.getX() - this.k.x;
        if (n < motionEvent.getY() - this.k.y) {
            return false;
        }
        if (n < ContextUtils.getScreenSize((Context)this).width * 0.3f) {
            return false;
        }
        final TuSdkFragment lastFragment = this.getLastFragment();
        return lastFragment != null && lastFragment instanceof TuSdkFragment && lastFragment.onBackForSlide();
    }

    private static float b(final MotionEvent motionEvent) {
        final float n = motionEvent.getX(0) - motionEvent.getX(1);
        final float n2 = motionEvent.getY(0) - motionEvent.getY(1);
        return (float)Math.sqrt(n * n + n2 * n2);
    }

    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() == 1) {
            return super.onTouchEvent(motionEvent);
        }
        final TuSdkFragmentActivityEventListener lastFragment = this.getLastFragment();
        if (lastFragment == null || !(lastFragment instanceof TuSdkFragmentActivityEventListener)) {
            return super.onTouchEvent(motionEvent);
        }
        switch (motionEvent.getAction() & 0xFF) {
            case 5: {
                this.l = b(motionEvent);
                break;
            }
            case 2: {
                final float b = b(motionEvent);
                if (b > this.l) {
                    lastFragment.onActivityTouchMotionDispatcher(this, true);
                }
                else {
                    lastFragment.onActivityTouchMotionDispatcher(this, false);
                }
                this.l = b;
                break;
            }
        }
        return true;
    }

    public void onBackStackChanged() {
        final TuSdkFragment lastFragment = this.getLastFragment();
        if (this.m && lastFragment != null && lastFragment instanceof TuSdkFragment) {
            lastFragment.onResumeFragment();
        }
        this.m = false;
    }

    public void onBackPressed() {
        if (this.isDispatchkeyListener(4)) {
            return;
        }
        if (this.f()) {
            return;
        }
        this.m = true;
        final TuSdkFragment lastFragment = this.getLastFragment();
        if (lastFragment != null && lastFragment instanceof TuSdkFragment && !lastFragment.onBackPressed()) {
            return;
        }
        if (this.backStackEntryCount() == 0) {
            this.dismissActivityWithAnim();
            return;
        }
        super.onBackPressed();
    }

    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        final TuSdkFragmentActivityEventListener lastFragment = this.getLastFragment();
        if (lastFragment == null || !(lastFragment instanceof TuSdkFragmentActivityEventListener) || keyEvent.getKeyCode() == 4) {
            return super.dispatchKeyEvent(keyEvent);
        }
        return lastFragment.onActivityKeyDispatcher(this, keyEvent.getKeyCode());
    }

    public void setActivityKeyListener(final TuSdkFragmentActivityEventListener n) {
        this.n = n;
    }

    public boolean isDispatchkeyListener(final int n) {
        return this.n != null && this.n.onActivityKeyDispatcher(this, n);
    }

    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        if (n == 82 && this.isDispatchkeyListener(n)) {
            return true;
        }
        final TuSdkFragment lastFragment = this.getLastFragment();
        return (lastFragment != null && lastFragment instanceof TuSdkFragment && lastFragment.onKeyUp(n, keyEvent)) || super.onKeyUp(n, keyEvent);
    }

    private void e() {
        if (this.p != null) {
            return;
        }
        this.p = new Handler((Handler.Callback)new Handler.Callback() {
            public boolean handleMessage(final Message message) {
                TuSdkFragmentActivity.this.o = false;
                return false;
            }
        });
    }

    private boolean f() {
        if (this.h == 0) {
            return false;
        }
        this.e();
        if (!this.o) {
            this.o = true;
            TuSdkViewHelper.toast((Context)this, this.h);
            this.p.sendEmptyMessageDelayed(0, 2000L);
            return true;
        }
        final Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        this.startActivity(intent);
        this.onApplicationWillExit();
        ActivityObserver.ins.exitApp();
        return true;
    }

    protected void onApplicationWillExit() {
    }

    public interface TuSdkFragmentActivityEventListener
    {
        boolean onActivityKeyDispatcher(final TuSdkFragmentActivity p0, final int p1);

        boolean onActivityTouchMotionDispatcher(final TuSdkFragmentActivity p0, final boolean p1);
    }

    public enum FragmentChangeType
    {
        join,
        push,
        tansform,
        replace;
    }
}
