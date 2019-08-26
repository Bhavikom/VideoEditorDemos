// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
//import org.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;

public abstract class TuSdkProgressHub implements TuSdkProgressHubView.TuSdkProgressHubViewDelegate
{
    private static boolean a;
    private WindowManager b;
    private TuSdkProgressHubView c;
    private Handler d;
    private TuSdkProgressHubView.HubArgCache e;
    private FrameLayout f;
    private Runnable g;
    private Runnable h;
    
    public TuSdkProgressHub() {
        this.d = new Handler(Looper.getMainLooper());
        this.g = new Runnable() {
            @Override
            public void run() {
                TuSdkProgressHub.this.a();
            }
        };
        this.h = new Runnable() {
            @Override
            public void run() {
                TuSdkProgressHub.this.dismissHub(true);
            }
        };
    }
    
    public abstract int getHubLayoutId();
    
    public static void applyToViewWithNavigationBarHidden(final boolean a) {
        TuSdkProgressHub.a = a;
    }
    
    public void showHubView(final Context context, final TuSdkProgressHubView.TuSdkHubViewShowType tuSdkHubViewShowType, final String s, final int n, final int n2, final long n3) {
        if (context == null) {
            return;
        }
        this.d.post((Runnable)new Runnable() {
            final /* synthetic */ TuSdkProgressHubView.HubArgCache a = new TuSdkProgressHubView.HubArgCache(context, tuSdkHubViewShowType, s, n, n2, n3);
            
            @Override
            public void run() {
                TuSdkProgressHub.this.a(this.a);
            }
        });
    }
    
    private void a(final TuSdkProgressHubView.HubArgCache hubArgCache) {
        if (TuSdkProgressHub.a) {
            this.b(hubArgCache);
        }
        else {
            this.c(hubArgCache);
        }
    }
    
    private void b(final TuSdkProgressHubView.HubArgCache e) {
        if (this.c != null && this.c.getParent() != null) {
            this.f.removeView((View)this.c);
        }
        this.f = null;
        this.c = null;
        this.e = e;
        this.d.postDelayed(this.g, 2L);
    }
    
    private void c(final TuSdkProgressHubView.HubArgCache e) {
        final WindowManager windowManager = ContextUtils.getWindowManager(e.context);
        if (this.b == null || !this.b.equals(windowManager)) {
            this.a(false, true);
            this.e = e;
            this.d.postDelayed(this.g, 2L);
        }
        else {
            this.d.removeCallbacks(this.h);
            if (this.b == null) {
                this.b = windowManager;
                this.a(e.context);
            }
            this.d(e);
        }
    }
    
    private void a() {
        if (this.e == null) {
            return;
        }
        this.d.removeCallbacks(this.h);
        this.b = ContextUtils.getWindowManager(this.e.context);
        this.a(this.e.context);
        this.d(this.e);
        this.e = null;
    }
    
    private void a(final boolean b, final boolean b2) {
        if (!b2) {
            this.d.removeCallbacks(this.g);
            this.e = null;
        }
        if ((this.f == null && TuSdkProgressHub.a) || (this.b == null && !TuSdkProgressHub.a) || this.c == null) {
            return;
        }
        if (b) {
            this.c.runViewShowableAnim(false);
        }
        else {
            this.b();
        }
    }
    
    public void dismissHub(final boolean b) {
        this.a(b, false);
    }
    
    private void b() {
        this.d.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                TuSdkProgressHub.this.c();
            }
        }, 1L);
    }
    
    private void c() {
        if (TuSdkProgressHub.a) {
            this.d();
        }
        else {
            this.e();
        }
        if (this.b != null) {
            this.b = null;
        }
    }
    
    private void d() {
        if (this.f == null || this.c == null) {
            return;
        }
        try {
            if (this.c.getParent() != null) {
                this.f.removeView((View)this.c);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        this.f = null;
        this.c.viewWillDestory();
        this.c = null;
    }
    
    private void e() {
        if (this.b == null || this.c == null) {
            return;
        }
        try {
            if (this.c.getParent() != null) {
                this.b.removeView((View)this.c);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        this.b = null;
        this.c.viewWillDestory();
        this.c = null;
    }
    
    public boolean existHubView() {
        return this.c != null;
    }
    
    private void d(final TuSdkProgressHubView.HubArgCache args) {
        if (this.c == null) {
            return;
        }
        this.c.setArgs(args);
        this.a(args.delay);
    }
    
    private void a(final long n) {
        if (n == 0L) {
            return;
        }
        this.d.postDelayed(this.h, n);
    }
    
    private void a(final Context context) {
        if (TuSdkProgressHub.a) {
            this.b(context);
        }
        else {
            this.c(context);
        }
    }
    
    private void b(final Context context) {
        this.c = TuSdkViewHelper.buildView(context, this.getHubLayoutId());
        if (this.c == null) {
            return;
        }
        this.c.setDelegate(this);
        this.c.runViewShowableAnim(true);
        if (!(context instanceof AppCompatActivity)) {
            TLog.e("TuSdkProgressHub: context is not instance of Activity", new Object[0]);
            return;
        }
        (this.f = (FrameLayout)((AppCompatActivity)context).getWindow().getDecorView().findViewById(16908290)).addView((View)this.c);
    }
    
    private void c(final Context context) {
        this.c = TuSdkViewHelper.buildView(context, this.getHubLayoutId());
        if (this.c == null) {
            return;
        }
        this.c.setDelegate(this);
        this.c.runViewShowableAnim(true);
        this.b.addView((View)this.c, (ViewGroup.LayoutParams)TuSdkViewHelper.buildApplicationPanelParams("ProgressHub"));
    }
    
    @Override
    public void onDismissAnimEnded() {
        this.b();
    }
    
    static {
        TuSdkProgressHub.a = false;
    }
}
