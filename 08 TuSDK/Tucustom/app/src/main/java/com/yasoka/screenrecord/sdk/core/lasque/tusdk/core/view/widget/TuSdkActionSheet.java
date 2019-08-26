// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget;

import android.os.Handler;
import android.view.WindowManager;
import android.view.ViewGroup;
import android.widget.LinearLayout;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.view.widget.button.TuSdkButton;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
//import org.lasque.tusdk.core.activity.TuSdkFragmentActivity;
import android.view.View;
import java.util.ArrayList;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.activity.TuSdkFragmentActivity;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.button.TuSdkButton;

public abstract class TuSdkActionSheet
{
    private static boolean a;
    private int b;
    private int c;
    private int d;
    private String e;
    private TuSdkActionSheetView f;
    private Context g;
    private String h;
    private int i;
    private ArrayList<String> j;
    private ActionSheetClickDelegate k;
    private ActionSheetAnimaExitDelegate l;
    private boolean m;
    private View.OnClickListener n;
    private TuSdkFragmentActivity.TuSdkFragmentActivityEventListener o;
    private boolean p;
    private TuSdkActionSheetView.TuSdkActionSheetViewAnimation q;
    
    public static boolean isExsitInWindow() {
        return TuSdkActionSheet.a;
    }
    
    public int getCategory() {
        return this.i;
    }
    
    public int getDestructiveIndex() {
        return this.b;
    }
    
    public void setDestructiveIndex(final int b) {
        this.b = b;
    }
    
    public int getCancelIndex() {
        return this.c;
    }
    
    public void setCancelIndex(final int c) {
        this.c = c;
    }
    
    public String getTitle() {
        return this.e;
    }
    
    public Context getContext() {
        return this.g;
    }
    
    protected abstract int getActionSheetLayoutId();
    
    protected abstract int getActionsheetButtonStyleResId();
    
    protected abstract int getButtonBackgroundResId(final int p0, final int p1);
    
    protected abstract int getButtonColor(final int p0);
    
    protected abstract int getActionsheetBottomSpace(final boolean p0);
    
    public TuSdkActionSheet(final Context g) {
        this.b = -1;
        this.c = -1;
        this.d = -1;
        this.j = new ArrayList<String>();
        this.n = (View.OnClickListener)new TuSdkViewHelper.OnSafeClickListener() {
            @Override
            public void onSafeClick(final View view) {
                if (TuSdkActionSheet.this.m) {
                    return;
                }
                if (view instanceof TuSdkButton) {
                    TuSdkActionSheet.this.a((TuSdkButton)view);
                }
                TuSdkActionSheet.this.dismiss();
            }
        };
        this.o = new TuSdkFragmentActivity.TuSdkFragmentActivityEventListener() {
            @Override
            public boolean onActivityKeyDispatcher(final TuSdkFragmentActivity tuSdkFragmentActivity, final int n) {
                switch (n) {
                    case 4:
                    case 82: {
                        TuSdkActionSheet.this.dismiss();
                        return true;
                    }
                    default: {
                        return false;
                    }
                }
            }
            
            @Override
            public boolean onActivityTouchMotionDispatcher(final TuSdkFragmentActivity tuSdkFragmentActivity, final boolean b) {
                return false;
            }
        };
        this.q = new TuSdkActionSheetView.TuSdkActionSheetViewAnimation() {
            @Override
            public void onAnimationEnd(final boolean b) {
                TuSdkActionSheet.this.b(b);
            }
        };
        this.g = g;
        this.a();
    }
    
    private void a() {
        this.f = TuSdkViewHelper.buildView(this.g, this.getActionSheetLayoutId());
        if (this.f == null) {
            return;
        }
        this.f.setAnimationListener(this.q);
        this.f.setDismissClickListener(this.n);
    }
    
    public TuSdkActionSheet init(final String s, final String h, final String s2, final String... array) {
        this.j = new ArrayList<String>();
        if (this.f == null) {
            return this;
        }
        this.a(s);
        this.b(s2);
        this.h = h;
        this.a(array);
        return this;
    }
    
    public TuSdkActionSheet init(final int n, final int n2, final int n3, final int... array) {
        this.j = new ArrayList<String>();
        if (this.f == null) {
            return this;
        }
        this.a(n);
        this.b(n3);
        if (n2 != 0) {
            this.h = ContextUtils.getResString(this.g, n2);
        }
        this.a(array);
        return this;
    }
    
    private void a(final String e) {
        if (e == null) {
            return;
        }
        this.e = e;
        this.f.setTitle(this.e);
        this.f.showView((View)this.f.getTitleView(), true);
    }
    
    private void a(final int n) {
        if (n == 0) {
            return;
        }
        this.a(ContextUtils.getResString(this.g, n));
    }
    
    private void b(final String e) {
        if (e == null) {
            return;
        }
        this.b = 0;
        this.j.add(e);
    }
    
    private void b(final int n) {
        if (n == 0) {
            return;
        }
        this.b(ContextUtils.getResString(this.g, n));
    }
    
    private void a(final String[] array) {
        if (array == null || array.length == 0) {
            return;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            this.j.add(array[i]);
        }
    }
    
    private void a(final int[] array) {
        if (array == null) {
            return;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            this.addButtonTitle(array[i]);
        }
    }
    
    public void addButtonTitle(final String e) {
        if (e == null) {
            return;
        }
        this.j.add(e);
    }
    
    public void addButtonTitle(final int n) {
        if (n == 0) {
            return;
        }
        this.addButtonTitle(ContextUtils.getResString(this.g, n));
    }
    
    public void showInView(final ActionSheetClickDelegate actionSheetClickDelegate) {
        this.showInView(actionSheetClickDelegate, 0);
    }
    
    public void showInView(final ActionSheetClickDelegate actionSheetClickDelegate, final int n) {
        this.showInView(actionSheetClickDelegate, null, n);
    }
    
    public void showInView(final ActionSheetClickDelegate k, final ActionSheetAnimaExitDelegate l, final int i) {
        this.k = k;
        this.l = l;
        this.i = i;
        this.b();
    }
    
    private void b() {
        if (this.f == null) {
            return;
        }
        this.c();
        this.a(this.f);
    }
    
    private void a(final TuSdkButton tuSdkButton) {
        this.d = tuSdkButton.index;
        if (this.k == null) {
            return;
        }
        this.k.onActionSheetClicked(this, this.d);
    }
    
    public String getButtonTitle(final int index) {
        if (index < this.j.size()) {
            return this.j.get(index);
        }
        return null;
    }
    
    public int buttonsSize() {
        if (this.j == null) {
            return 0;
        }
        return this.j.size();
    }
    
    private void c() {
        final LinearLayout sheetTable = this.f.getSheetTable();
        if (sheetTable == null) {
            return;
        }
        for (int i = 0, size = this.j.size(); i < size; ++i) {
            final TuSdkButton a = this.a(i, size);
            if (a != null) {
                sheetTable.addView((View)a, sheetTable.getChildCount() - 1);
                a.setOnClickListener(this.n);
            }
        }
        this.d();
    }
    
    private TuSdkButton a(final int index, final int n) {
        final TuSdkButton tuSdkButton = new TuSdkButton((Context)ContextUtils.getResStyleContext(this.g, this.getActionsheetButtonStyleResId()));
        tuSdkButton.index = index;
        tuSdkButton.setText((CharSequence)this.getButtonTitle(index));
        tuSdkButton.setBackgroundResource(this.getButtonBackgroundResId(index, n));
        tuSdkButton.setTextColor(this.getButtonColor(index));
        tuSdkButton.setLayoutParams((ViewGroup.LayoutParams)this.a(index == n - 1));
        return tuSdkButton;
    }
    
    public void setButtonColor(final int n, final int backgroundResource) {
        if (this.f.getSheetTable() != null && n < this.j.size()) {
            this.f.getSheetTable().getChildAt(n + 1).setBackgroundResource(backgroundResource);
        }
    }
    
    private LinearLayout.LayoutParams a(final boolean b) {
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.bottomMargin = this.getActionsheetBottomSpace(b);
        return layoutParams;
    }
    
    private void d() {
        if (this.h == null || this.f.getCancelButton() == null || !(this.f.getCancelButton() instanceof TuSdkButton)) {
            return;
        }
        final TuSdkButton tuSdkButton = (TuSdkButton)this.f.getCancelButton();
        tuSdkButton.index = this.j.size();
        tuSdkButton.setText((CharSequence)this.h);
        this.j.add(this.h);
        if (this.c == -1) {
            this.c = tuSdkButton.index;
        }
        this.f.showView((View)tuSdkButton, true);
    }
    
    private void a(final TuSdkActionSheetView tuSdkActionSheetView) {
        final WindowManager.LayoutParams buildApplicationPanelParams = TuSdkViewHelper.buildApplicationPanelParams("ActionSheet");
        buildApplicationPanelParams.flags = 424;
        final WindowManager windowManager = ContextUtils.getWindowManager(this.g);
        if (tuSdkActionSheetView.getParent() != null) {
            windowManager.removeView((View)tuSdkActionSheetView);
        }
        if (this.g instanceof TuSdkFragmentActivity) {
            ((TuSdkFragmentActivity)this.g).setActivityKeyListener(this.o);
        }
        TuSdkActionSheet.a = true;
        windowManager.addView((View)tuSdkActionSheetView, (ViewGroup.LayoutParams)buildApplicationPanelParams);
        this.m = true;
        this.f.runViewShowableAnim(false);
    }
    
    public void dismiss() {
        if (this.p) {
            return;
        }
        this.m = true;
        this.f.runViewShowableAnim(true);
    }
    
    public void dismissRightNow() {
        this.b(this.p = true);
    }
    
    private void e() {
        if (this.g == null) {
            return;
        }
        final WindowManager windowManager = ContextUtils.getWindowManager(this.g);
        if (this.f.getParent() != null) {
            windowManager.removeView((View)this.f);
        }
        if (this.g instanceof TuSdkFragmentActivity) {
            ((TuSdkFragmentActivity)this.g).setActivityKeyListener(null);
        }
        this.g = null;
        this.f.setAnimationListener(null);
        this.f = null;
        TuSdkActionSheet.a = false;
    }
    
    private void b(final boolean b) {
        if (b) {
            this.f.showView(false);
            this.f();
            new Handler().postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    TuSdkActionSheet.this.e();
                }
            }, 1L);
            return;
        }
        this.m = false;
    }
    
    private void f() {
        if (this.l == null) {
            return;
        }
        this.l.onActionSheetAnimaExited(this, this.d);
    }
    
    public interface ActionSheetAnimaExitDelegate
    {
        void onActionSheetAnimaExited(final TuSdkActionSheet p0, final int p1);
    }
    
    public interface ActionSheetClickDelegate
    {
        void onActionSheetClicked(final TuSdkActionSheet p0, final int p1);
    }
}
