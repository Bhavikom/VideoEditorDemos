package org.lasque.tusdk.core.view.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;

public class TuSdkListViewFlingAction
{
  private int a;
  private int b;
  private int c;
  private long d = 150L;
  private float e;
  private boolean f;
  private VelocityTracker g;
  private LasqueListViewFlingActionInterface h;
  private PointF i;
  private Context j;
  private TuSdkListViewFlingActionDelegate k;
  
  public void setDelegate(TuSdkListViewFlingActionDelegate paramTuSdkListViewFlingActionDelegate)
  {
    this.k = paramTuSdkListViewFlingActionDelegate;
  }
  
  public TuSdkListViewFlingAction(Context paramContext)
  {
    this.j = paramContext;
    a();
  }
  
  private void a()
  {
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(this.j);
    this.a = localViewConfiguration.getScaledTouchSlop();
    this.b = (localViewConfiguration.getScaledMinimumFlingVelocity() * 8);
    this.c = localViewConfiguration.getScaledMaximumFlingVelocity();
  }
  
  public void onDestory()
  {
    b();
    this.h = null;
    this.k = null;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent, View paramView)
  {
    switch (paramMotionEvent.getAction())
    {
    case 0: 
      a(paramMotionEvent, paramView);
      break;
    case 2: 
      return a(paramMotionEvent);
    case 1: 
      b(paramMotionEvent);
      break;
    }
    return false;
  }
  
  @SuppressLint({"Recycle"})
  private void a(MotionEvent paramMotionEvent, View paramView)
  {
    this.e = 0.0F;
    if ((this.h != null) && (!this.h.equals(paramView))) {
      resetDownView();
    }
    if ((paramView == null) || (!(paramView instanceof LasqueListViewFlingActionInterface)) || (!((LasqueListViewFlingActionInterface)paramView).flingCanShow()))
    {
      this.h = null;
      b();
      return;
    }
    this.i = new PointF(paramMotionEvent.getX(), paramMotionEvent.getY());
    this.h = ((LasqueListViewFlingActionInterface)paramView);
    this.g = VelocityTracker.obtain();
    this.g.addMovement(paramMotionEvent);
  }
  
  private boolean a(MotionEvent paramMotionEvent)
  {
    if ((this.g == null) || (this.h == null)) {
      return false;
    }
    PointF localPointF = new PointF(paramMotionEvent.getX() - this.i.x, paramMotionEvent.getY() - this.i.y);
    if ((!this.f) && (Math.abs(localPointF.x) > this.a) && (Math.abs(localPointF.y) < this.a) && (this.k != null))
    {
      this.f = true;
      this.h.flingStateWillChange(true);
      this.e = ViewCompat.getTranslationX(this.h.flingCellWrap());
      this.k.onFlingActionCancelItemClick(paramMotionEvent);
    }
    if (this.f)
    {
      float f1 = localPointF.x + this.e;
      ViewCompat.setTranslationX(this.h.flingCellWrap(), f1 < 0.0F ? f1 : 0.0F);
      return true;
    }
    return false;
  }
  
  private void b(MotionEvent paramMotionEvent)
  {
    if ((this.g == null) || (this.h == null) || (!this.f))
    {
      resetDownView();
      b();
      return;
    }
    float f1 = ViewCompat.getTranslationX(this.h.flingCellWrap());
    this.g.computeCurrentVelocity(1000);
    float f2 = Math.abs(this.g.getXVelocity());
    float f3 = Math.abs(this.g.getYVelocity());
    int m = 0;
    if (Math.abs(f1) > this.h.flingWrapWidth() * 0.75D) {
      m = 1;
    } else if ((this.b <= f2) && (f2 <= this.c) && (f3 < f2)) {
      m = 1;
    }
    if (m != 0)
    {
      this.h.flingStateWillChange(true);
      ViewCompat.animate(this.h.flingCellWrap()).translationX(-this.h.flingWrapWidth()).setDuration(this.d).setInterpolator(new AccelerateDecelerateInterpolator());
    }
    else
    {
      resetDownView();
    }
    b();
    this.f = false;
  }
  
  private void b()
  {
    if (this.g != null)
    {
      this.g.recycle();
      this.g = null;
    }
  }
  
  public void resetDownView()
  {
    if (this.h == null) {
      return;
    }
    this.h.flingStateWillChange(false);
    ViewCompat.animate(this.h.flingCellWrap()).translationX(0.0F).setDuration(this.d).setInterpolator(new AccelerateDecelerateInterpolator());
  }
  
  public static abstract interface TuSdkListViewFlingActionDelegate
  {
    public abstract void onFlingActionCancelItemClick(MotionEvent paramMotionEvent);
  }
  
  public static abstract interface LasqueListViewFlingActionInterface
  {
    public abstract ViewGroup flingCellWrap();
    
    public abstract int flingWrapWidth();
    
    public abstract void flingStateWillChange(boolean paramBoolean);
    
    public abstract boolean flingCanShow();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\listview\TuSdkListViewFlingAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */