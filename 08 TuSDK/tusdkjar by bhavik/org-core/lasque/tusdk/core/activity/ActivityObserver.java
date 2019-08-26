package org.lasque.tusdk.core.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import org.lasque.tusdk.core.type.ActivityAnimType;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.impl.TuAnimType;
import org.lasque.tusdk.impl.activity.TuFragmentActivity;

public class ActivityObserver
{
  public static final ActivityObserver ins = new ActivityObserver();
  private ArrayList<Activity> a = new ArrayList();
  private Fragment b;
  private EditText c;
  private Class<?> d;
  @SuppressLint({"ClickableViewAccessibility"})
  private View.OnTouchListener e = new View.OnTouchListener()
  {
    public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
    {
      if ((paramAnonymousMotionEvent != null) && (paramAnonymousMotionEvent.getAction() == 0)) {
        ActivityObserver.this.cancelEditTextFocus(paramAnonymousView);
      }
      return false;
    }
  };
  private Hashtable<String, ActivityAnimType> f = new Hashtable();
  private ActivityAnimType g;
  private ActivityAnimType h;
  private ActivityAnimType i;
  private ActivityAnimType j;
  
  public Class<?> getMainActivityClazz()
  {
    if (this.d == null) {
      this.d = TuFragmentActivity.class;
    }
    return this.d;
  }
  
  public void setMainActivityClazz(Class<?> paramClass)
  {
    this.d = paramClass;
  }
  
  private ActivityObserver()
  {
    for (TuAnimType localTuAnimType : TuAnimType.values()) {
      this.f.put(localTuAnimType.name(), localTuAnimType);
    }
  }
  
  public void register(Activity paramActivity)
  {
    if ((paramActivity == null) || (this.a.contains(paramActivity))) {
      return;
    }
    this.a.add(paramActivity);
  }
  
  public void remove(Activity paramActivity)
  {
    if (paramActivity == null) {
      return;
    }
    this.a.remove(paramActivity);
  }
  
  public Activity getTopActivity()
  {
    if ((this.a == null) || (this.a.isEmpty())) {
      return null;
    }
    return (Activity)this.a.get(this.a.size() - 1);
  }
  
  public void exitApp()
  {
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      Activity localActivity = (Activity)localIterator.next();
      localActivity.finish();
    }
    this.a.clear();
    Process.killProcess(Process.myPid());
    System.exit(0);
  }
  
  public void setTransmit(Fragment paramFragment)
  {
    this.b = paramFragment;
  }
  
  public Fragment getTransmit()
  {
    Fragment localFragment = this.b;
    this.b = null;
    return localFragment;
  }
  
  public View.OnTouchListener getHiddenKeyboardListener()
  {
    return this.e;
  }
  
  public void bindAutoHiddenKeyboard(View paramView)
  {
    if (paramView == null) {
      return;
    }
    paramView.setOnTouchListener(this.e);
  }
  
  public void editTextFocus(EditText paramEditText)
  {
    showSoftInput(paramEditText.getContext(), paramEditText);
    this.c = paramEditText;
  }
  
  public void editTextFocusLost(EditText paramEditText)
  {
    if ((this.c != null) && (paramEditText != null) && (this.c.equals(paramEditText))) {
      this.c = null;
    }
  }
  
  public boolean cancelEditTextFocus(View paramView)
  {
    if (paramView != null) {
      cancelEditTextFocusBinder(paramView.getContext(), paramView.getWindowToken());
    }
    if (this.c == null) {
      return false;
    }
    cancelEditTextFocusBinder(this.c.getContext(), this.c.getWindowToken());
    this.c.clearFocus();
    this.c = null;
    return true;
  }
  
  public boolean cancelEditTextFocus()
  {
    return cancelEditTextFocus(null);
  }
  
  public boolean cancelEditTextFocusBinder(Context paramContext, IBinder paramIBinder)
  {
    if (paramIBinder == null) {
      return false;
    }
    InputMethodManager localInputMethodManager = (InputMethodManager)ContextUtils.getSystemService(paramContext, "input_method");
    if (localInputMethodManager == null) {
      return false;
    }
    localInputMethodManager.hideSoftInputFromWindow(paramIBinder, 2);
    return true;
  }
  
  public void showSoftInput(Context paramContext, EditText paramEditText)
  {
    if (paramEditText == null) {
      return;
    }
    InputMethodManager localInputMethodManager = (InputMethodManager)ContextUtils.getSystemService(paramContext, "input_method");
    if (localInputMethodManager == null) {
      return;
    }
    localInputMethodManager.showSoftInput(paramEditText, 0);
  }
  
  public Hashtable<String, ActivityAnimType> getActivityAnims()
  {
    return this.f;
  }
  
  public ActivityAnimType getAnimPresent()
  {
    if (this.g == null) {
      this.g = TuAnimType.up;
    }
    return this.g;
  }
  
  public void setAnimPresent(ActivityAnimType paramActivityAnimType)
  {
    this.g = paramActivityAnimType;
  }
  
  public ActivityAnimType getAnimDismiss()
  {
    if (this.h == null) {
      this.h = TuAnimType.down;
    }
    return this.h;
  }
  
  public void setAnimDismiss(ActivityAnimType paramActivityAnimType)
  {
    this.h = paramActivityAnimType;
  }
  
  public ActivityAnimType getAnimPush()
  {
    if (this.i == null) {
      this.i = TuAnimType.push;
    }
    return this.i;
  }
  
  public void setAnimPush(ActivityAnimType paramActivityAnimType)
  {
    this.i = paramActivityAnimType;
  }
  
  public ActivityAnimType getAnimPop()
  {
    if (this.j == null) {
      this.j = TuAnimType.pop;
    }
    return this.j;
  }
  
  public void setAnimPop(ActivityAnimType paramActivityAnimType)
  {
    this.j = paramActivityAnimType;
  }
  
  protected ActivityAnimType getAnimType(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return (ActivityAnimType)this.f.get(paramString);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\activity\ActivityObserver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */