package org.lasque.tusdk.core.view.widget;

import android.content.Context;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import java.util.ArrayList;
import org.lasque.tusdk.core.activity.TuSdkFragmentActivity;
import org.lasque.tusdk.core.activity.TuSdkFragmentActivity.TuSdkFragmentActivityEventListener;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.core.view.widget.button.TuSdkButton;

public abstract class TuSdkActionSheet
{
  private static boolean a;
  private int b = -1;
  private int c = -1;
  private int d = -1;
  private String e;
  private TuSdkActionSheetView f;
  private Context g;
  private String h;
  private int i;
  private ArrayList<String> j = new ArrayList();
  private ActionSheetClickDelegate k;
  private ActionSheetAnimaExitDelegate l;
  private boolean m;
  private View.OnClickListener n = new TuSdkViewHelper.OnSafeClickListener()
  {
    public void onSafeClick(View paramAnonymousView)
    {
      if (TuSdkActionSheet.a(TuSdkActionSheet.this)) {
        return;
      }
      if ((paramAnonymousView instanceof TuSdkButton)) {
        TuSdkActionSheet.a(TuSdkActionSheet.this, (TuSdkButton)paramAnonymousView);
      }
      TuSdkActionSheet.this.dismiss();
    }
  };
  private TuSdkFragmentActivity.TuSdkFragmentActivityEventListener o = new TuSdkFragmentActivity.TuSdkFragmentActivityEventListener()
  {
    public boolean onActivityKeyDispatcher(TuSdkFragmentActivity paramAnonymousTuSdkFragmentActivity, int paramAnonymousInt)
    {
      switch (paramAnonymousInt)
      {
      case 4: 
      case 82: 
        TuSdkActionSheet.this.dismiss();
        return true;
      }
      return false;
    }
    
    public boolean onActivityTouchMotionDispatcher(TuSdkFragmentActivity paramAnonymousTuSdkFragmentActivity, boolean paramAnonymousBoolean)
    {
      return false;
    }
  };
  private boolean p;
  private TuSdkActionSheetView.TuSdkActionSheetViewAnimation q = new TuSdkActionSheetView.TuSdkActionSheetViewAnimation()
  {
    public void onAnimationEnd(boolean paramAnonymousBoolean)
    {
      TuSdkActionSheet.a(TuSdkActionSheet.this, paramAnonymousBoolean);
    }
  };
  
  public static boolean isExsitInWindow()
  {
    return a;
  }
  
  public int getCategory()
  {
    return this.i;
  }
  
  public int getDestructiveIndex()
  {
    return this.b;
  }
  
  public void setDestructiveIndex(int paramInt)
  {
    this.b = paramInt;
  }
  
  public int getCancelIndex()
  {
    return this.c;
  }
  
  public void setCancelIndex(int paramInt)
  {
    this.c = paramInt;
  }
  
  public String getTitle()
  {
    return this.e;
  }
  
  public Context getContext()
  {
    return this.g;
  }
  
  protected abstract int getActionSheetLayoutId();
  
  protected abstract int getActionsheetButtonStyleResId();
  
  protected abstract int getButtonBackgroundResId(int paramInt1, int paramInt2);
  
  protected abstract int getButtonColor(int paramInt);
  
  protected abstract int getActionsheetBottomSpace(boolean paramBoolean);
  
  public TuSdkActionSheet(Context paramContext)
  {
    this.g = paramContext;
    a();
  }
  
  private void a()
  {
    this.f = ((TuSdkActionSheetView)TuSdkViewHelper.buildView(this.g, getActionSheetLayoutId()));
    if (this.f == null) {
      return;
    }
    this.f.setAnimationListener(this.q);
    this.f.setDismissClickListener(this.n);
  }
  
  public TuSdkActionSheet init(String paramString1, String paramString2, String paramString3, String... paramVarArgs)
  {
    this.j = new ArrayList();
    if (this.f == null) {
      return this;
    }
    a(paramString1);
    b(paramString3);
    this.h = paramString2;
    a(paramVarArgs);
    return this;
  }
  
  public TuSdkActionSheet init(int paramInt1, int paramInt2, int paramInt3, int... paramVarArgs)
  {
    this.j = new ArrayList();
    if (this.f == null) {
      return this;
    }
    a(paramInt1);
    b(paramInt3);
    if (paramInt2 != 0) {
      this.h = ContextUtils.getResString(this.g, paramInt2);
    }
    a(paramVarArgs);
    return this;
  }
  
  private void a(String paramString)
  {
    if (paramString == null) {
      return;
    }
    this.e = paramString;
    this.f.setTitle(this.e);
    this.f.showView(this.f.getTitleView(), true);
  }
  
  private void a(int paramInt)
  {
    if (paramInt == 0) {
      return;
    }
    String str = ContextUtils.getResString(this.g, paramInt);
    a(str);
  }
  
  private void b(String paramString)
  {
    if (paramString == null) {
      return;
    }
    this.b = 0;
    this.j.add(paramString);
  }
  
  private void b(int paramInt)
  {
    if (paramInt == 0) {
      return;
    }
    String str = ContextUtils.getResString(this.g, paramInt);
    b(str);
  }
  
  private void a(String[] paramArrayOfString)
  {
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0)) {
      return;
    }
    for (String str : paramArrayOfString) {
      this.j.add(str);
    }
  }
  
  private void a(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null) {
      return;
    }
    for (int i3 : paramArrayOfInt) {
      addButtonTitle(i3);
    }
  }
  
  public void addButtonTitle(String paramString)
  {
    if (paramString == null) {
      return;
    }
    this.j.add(paramString);
  }
  
  public void addButtonTitle(int paramInt)
  {
    if (paramInt == 0) {
      return;
    }
    String str = ContextUtils.getResString(this.g, paramInt);
    addButtonTitle(str);
  }
  
  public void showInView(ActionSheetClickDelegate paramActionSheetClickDelegate)
  {
    showInView(paramActionSheetClickDelegate, 0);
  }
  
  public void showInView(ActionSheetClickDelegate paramActionSheetClickDelegate, int paramInt)
  {
    showInView(paramActionSheetClickDelegate, null, paramInt);
  }
  
  public void showInView(ActionSheetClickDelegate paramActionSheetClickDelegate, ActionSheetAnimaExitDelegate paramActionSheetAnimaExitDelegate, int paramInt)
  {
    this.k = paramActionSheetClickDelegate;
    this.l = paramActionSheetAnimaExitDelegate;
    this.i = paramInt;
    b();
  }
  
  private void b()
  {
    if (this.f == null) {
      return;
    }
    c();
    a(this.f);
  }
  
  private void a(TuSdkButton paramTuSdkButton)
  {
    this.d = paramTuSdkButton.index;
    if (this.k == null) {
      return;
    }
    this.k.onActionSheetClicked(this, this.d);
  }
  
  public String getButtonTitle(int paramInt)
  {
    if (paramInt < this.j.size()) {
      return (String)this.j.get(paramInt);
    }
    return null;
  }
  
  public int buttonsSize()
  {
    if (this.j == null) {
      return 0;
    }
    return this.j.size();
  }
  
  private void c()
  {
    LinearLayout localLinearLayout = this.f.getSheetTable();
    if (localLinearLayout == null) {
      return;
    }
    int i1 = 0;
    int i2 = this.j.size();
    while (i1 < i2)
    {
      TuSdkButton localTuSdkButton = a(i1, i2);
      if (localTuSdkButton != null)
      {
        int i3 = localLinearLayout.getChildCount() - 1;
        localLinearLayout.addView(localTuSdkButton, i3);
        localTuSdkButton.setOnClickListener(this.n);
      }
      i1++;
    }
    d();
  }
  
  private TuSdkButton a(int paramInt1, int paramInt2)
  {
    ContextThemeWrapper localContextThemeWrapper = ContextUtils.getResStyleContext(this.g, getActionsheetButtonStyleResId());
    TuSdkButton localTuSdkButton = new TuSdkButton(localContextThemeWrapper);
    localTuSdkButton.index = paramInt1;
    localTuSdkButton.setText(getButtonTitle(paramInt1));
    localTuSdkButton.setBackgroundResource(getButtonBackgroundResId(paramInt1, paramInt2));
    localTuSdkButton.setTextColor(getButtonColor(paramInt1));
    localTuSdkButton.setLayoutParams(a(paramInt1 == paramInt2 - 1));
    return localTuSdkButton;
  }
  
  public void setButtonColor(int paramInt1, int paramInt2)
  {
    if ((this.f.getSheetTable() != null) && (paramInt1 < this.j.size())) {
      this.f.getSheetTable().getChildAt(paramInt1 + 1).setBackgroundResource(paramInt2);
    }
  }
  
  private LinearLayout.LayoutParams a(boolean paramBoolean)
  {
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -2);
    localLayoutParams.bottomMargin = getActionsheetBottomSpace(paramBoolean);
    return localLayoutParams;
  }
  
  private void d()
  {
    if ((this.h == null) || (this.f.getCancelButton() == null) || (!(this.f.getCancelButton() instanceof TuSdkButton))) {
      return;
    }
    TuSdkButton localTuSdkButton = (TuSdkButton)this.f.getCancelButton();
    localTuSdkButton.index = this.j.size();
    localTuSdkButton.setText(this.h);
    this.j.add(this.h);
    if (this.c == -1) {
      this.c = localTuSdkButton.index;
    }
    this.f.showView(localTuSdkButton, true);
  }
  
  private void a(TuSdkActionSheetView paramTuSdkActionSheetView)
  {
    WindowManager.LayoutParams localLayoutParams = TuSdkViewHelper.buildApplicationPanelParams("ActionSheet");
    localLayoutParams.flags = 424;
    WindowManager localWindowManager = ContextUtils.getWindowManager(this.g);
    if (paramTuSdkActionSheetView.getParent() != null) {
      localWindowManager.removeView(paramTuSdkActionSheetView);
    }
    if ((this.g instanceof TuSdkFragmentActivity)) {
      ((TuSdkFragmentActivity)this.g).setActivityKeyListener(this.o);
    }
    a = true;
    localWindowManager.addView(paramTuSdkActionSheetView, localLayoutParams);
    this.m = true;
    this.f.runViewShowableAnim(false);
  }
  
  public void dismiss()
  {
    if (this.p) {
      return;
    }
    this.m = true;
    this.f.runViewShowableAnim(true);
  }
  
  public void dismissRightNow()
  {
    this.p = true;
    b(true);
  }
  
  private void e()
  {
    if (this.g == null) {
      return;
    }
    WindowManager localWindowManager = ContextUtils.getWindowManager(this.g);
    if (this.f.getParent() != null) {
      localWindowManager.removeView(this.f);
    }
    if ((this.g instanceof TuSdkFragmentActivity)) {
      ((TuSdkFragmentActivity)this.g).setActivityKeyListener(null);
    }
    this.g = null;
    this.f.setAnimationListener(null);
    this.f = null;
    a = false;
  }
  
  private void b(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.f.showView(false);
      f();
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
          TuSdkActionSheet.b(TuSdkActionSheet.this);
        }
      }, 1L);
      return;
    }
    this.m = false;
  }
  
  private void f()
  {
    if (this.l == null) {
      return;
    }
    this.l.onActionSheetAnimaExited(this, this.d);
  }
  
  public static abstract interface ActionSheetAnimaExitDelegate
  {
    public abstract void onActionSheetAnimaExited(TuSdkActionSheet paramTuSdkActionSheet, int paramInt);
  }
  
  public static abstract interface ActionSheetClickDelegate
  {
    public abstract void onActionSheetClicked(TuSdkActionSheet paramTuSdkActionSheet, int paramInt);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\TuSdkActionSheet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */