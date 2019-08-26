package org.lasque.tusdk.core.view.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.view.TuSdkViewHelper;

public abstract class TuSdkProgressHub
  implements TuSdkProgressHubView.TuSdkProgressHubViewDelegate
{
  private static boolean a = false;
  private WindowManager b;
  private TuSdkProgressHubView c;
  private Handler d = new Handler(Looper.getMainLooper());
  private TuSdkProgressHubView.HubArgCache e;
  private FrameLayout f;
  private Runnable g = new Runnable()
  {
    public void run()
    {
      TuSdkProgressHub.a(TuSdkProgressHub.this);
    }
  };
  private Runnable h = new Runnable()
  {
    public void run()
    {
      TuSdkProgressHub.this.dismissHub(true);
    }
  };
  
  public abstract int getHubLayoutId();
  
  public static void applyToViewWithNavigationBarHidden(boolean paramBoolean)
  {
    a = paramBoolean;
  }
  
  public void showHubView(Context paramContext, TuSdkProgressHubView.TuSdkHubViewShowType paramTuSdkHubViewShowType, String paramString, int paramInt1, int paramInt2, long paramLong)
  {
    if (paramContext == null) {
      return;
    }
    final TuSdkProgressHubView.HubArgCache localHubArgCache = new TuSdkProgressHubView.HubArgCache(paramContext, paramTuSdkHubViewShowType, paramString, paramInt1, paramInt2, paramLong);
    this.d.post(new Runnable()
    {
      public void run()
      {
        TuSdkProgressHub.a(TuSdkProgressHub.this, localHubArgCache);
      }
    });
  }
  
  private void a(TuSdkProgressHubView.HubArgCache paramHubArgCache)
  {
    if (a) {
      b(paramHubArgCache);
    } else {
      c(paramHubArgCache);
    }
  }
  
  private void b(TuSdkProgressHubView.HubArgCache paramHubArgCache)
  {
    if ((this.c != null) && (this.c.getParent() != null)) {
      this.f.removeView(this.c);
    }
    this.f = null;
    this.c = null;
    this.e = paramHubArgCache;
    this.d.postDelayed(this.g, 2L);
  }
  
  private void c(TuSdkProgressHubView.HubArgCache paramHubArgCache)
  {
    WindowManager localWindowManager = ContextUtils.getWindowManager(paramHubArgCache.context);
    if ((this.b == null) || (!this.b.equals(localWindowManager)))
    {
      a(false, true);
      this.e = paramHubArgCache;
      this.d.postDelayed(this.g, 2L);
    }
    else
    {
      this.d.removeCallbacks(this.h);
      if (this.b == null)
      {
        this.b = localWindowManager;
        a(paramHubArgCache.context);
      }
      d(paramHubArgCache);
    }
  }
  
  private void a()
  {
    if (this.e == null) {
      return;
    }
    this.d.removeCallbacks(this.h);
    this.b = ContextUtils.getWindowManager(this.e.context);
    a(this.e.context);
    d(this.e);
    this.e = null;
  }
  
  private void a(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (!paramBoolean2)
    {
      this.d.removeCallbacks(this.g);
      this.e = null;
    }
    if (((this.f == null) && (a)) || ((this.b == null) && (!a)) || (this.c == null)) {
      return;
    }
    if (paramBoolean1) {
      this.c.runViewShowableAnim(false);
    } else {
      b();
    }
  }
  
  public void dismissHub(boolean paramBoolean)
  {
    a(paramBoolean, false);
  }
  
  private void b()
  {
    this.d.postDelayed(new Runnable()
    {
      public void run()
      {
        TuSdkProgressHub.b(TuSdkProgressHub.this);
      }
    }, 1L);
  }
  
  private void c()
  {
    if (a) {
      d();
    } else {
      e();
    }
    if (this.b != null) {
      this.b = null;
    }
  }
  
  private void d()
  {
    if ((this.f == null) || (this.c == null)) {
      return;
    }
    try
    {
      if (this.c.getParent() != null) {
        this.f.removeView(this.c);
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    this.f = null;
    this.c.viewWillDestory();
    this.c = null;
  }
  
  private void e()
  {
    if ((this.b == null) || (this.c == null)) {
      return;
    }
    try
    {
      if (this.c.getParent() != null) {
        this.b.removeView(this.c);
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    this.b = null;
    this.c.viewWillDestory();
    this.c = null;
  }
  
  public boolean existHubView()
  {
    return this.c != null;
  }
  
  private void d(TuSdkProgressHubView.HubArgCache paramHubArgCache)
  {
    if (this.c == null) {
      return;
    }
    this.c.setArgs(paramHubArgCache);
    a(paramHubArgCache.delay);
  }
  
  private void a(long paramLong)
  {
    if (paramLong == 0L) {
      return;
    }
    this.d.postDelayed(this.h, paramLong);
  }
  
  private void a(Context paramContext)
  {
    if (a) {
      b(paramContext);
    } else {
      c(paramContext);
    }
  }
  
  private void b(Context paramContext)
  {
    this.c = ((TuSdkProgressHubView)TuSdkViewHelper.buildView(paramContext, getHubLayoutId()));
    if (this.c == null) {
      return;
    }
    this.c.setDelegate(this);
    this.c.runViewShowableAnim(true);
    if (!(paramContext instanceof Activity))
    {
      TLog.e("TuSdkProgressHub: context is not instance of Activity", new Object[0]);
      return;
    }
    View localView = ((Activity)paramContext).getWindow().getDecorView();
    this.f = ((FrameLayout)localView.findViewById(16908290));
    this.f.addView(this.c);
  }
  
  private void c(Context paramContext)
  {
    this.c = ((TuSdkProgressHubView)TuSdkViewHelper.buildView(paramContext, getHubLayoutId()));
    if (this.c == null) {
      return;
    }
    this.c.setDelegate(this);
    this.c.runViewShowableAnim(true);
    WindowManager.LayoutParams localLayoutParams = TuSdkViewHelper.buildApplicationPanelParams("ProgressHub");
    this.b.addView(this.c, localLayoutParams);
  }
  
  public void onDismissAnimEnded()
  {
    b();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\TuSdkProgressHub.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */