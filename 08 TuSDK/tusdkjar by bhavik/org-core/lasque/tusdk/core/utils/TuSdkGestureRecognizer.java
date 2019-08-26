package org.lasque.tusdk.core.utils;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class TuSdkGestureRecognizer
  implements View.OnTouchListener
{
  public static long MultipleStablizationDistance = 50L;
  private int a = -1;
  private int b = -1;
  private PointF c;
  private PointF d;
  private float e;
  private float f;
  private float g;
  private float h;
  private boolean i;
  private long j;
  private final StepData k = new StepData(null);
  private final Runnable l = new Runnable()
  {
    public void run()
    {
      TuSdkGestureRecognizer.a(TuSdkGestureRecognizer.this);
      ThreadHelper.postDelayed(TuSdkGestureRecognizer.b(TuSdkGestureRecognizer.this), TuSdkGestureRecognizer.this.getMultipleStablizationDistance());
    }
  };
  
  public StepData getStepData()
  {
    StepData localStepData = new StepData(null);
    localStepData.stepPoint = this.d;
    localStepData.stepSpace = this.f;
    localStepData.stepDegree = this.h;
    return localStepData;
  }
  
  public PointF getLastPoint()
  {
    return this.c;
  }
  
  public PointF getStepPoint()
  {
    return this.d;
  }
  
  public float getSpace()
  {
    return this.e;
  }
  
  public float getStepSpace()
  {
    return this.f;
  }
  
  public float getDegree()
  {
    return this.g;
  }
  
  public float getStepDegree()
  {
    return this.h;
  }
  
  public boolean isMultipleStablization()
  {
    return this.i;
  }
  
  public void setMultipleStablization(boolean paramBoolean)
  {
    this.i = paramBoolean;
  }
  
  public long getMultipleStablizationDistance()
  {
    if (this.j < 50L) {
      this.j = MultipleStablizationDistance;
    }
    return this.j;
  }
  
  public void setMultipleStablizationDistance(long paramLong)
  {
    this.j = paramLong;
  }
  
  private void a()
  {
    this.a = -1;
    this.b = -1;
    this.c = new PointF();
    this.d = new PointF();
    this.e = 0.0F;
    this.f = 0.0F;
    this.g = 0.0F;
    this.h = 0.0F;
    c();
    StepData.a(this.k);
  }
  
  private float a(MotionEvent paramMotionEvent)
  {
    if ((this.a == -1) || (this.b == -1)) {
      return 0.0F;
    }
    PointF localPointF1 = a(paramMotionEvent, this.a);
    PointF localPointF2 = a(paramMotionEvent, this.b);
    float f1 = localPointF1.x - localPointF2.x;
    float f2 = localPointF1.y - localPointF2.y;
    return (float)Math.sqrt(f1 * f1 + f2 * f2);
  }
  
  private float b(MotionEvent paramMotionEvent)
  {
    if ((this.a == -1) || (this.b == -1)) {
      return 0.0F;
    }
    PointF localPointF1 = a(paramMotionEvent, this.a);
    PointF localPointF2 = a(paramMotionEvent, this.b);
    float f1 = localPointF1.x - localPointF2.x;
    float f2 = localPointF1.y - localPointF2.y;
    double d1 = Math.atan2(f2, f1);
    return (float)Math.toDegrees(d1);
  }
  
  private int c(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent == null) {
      return -1;
    }
    return paramMotionEvent.getPointerId(paramMotionEvent.getActionIndex());
  }
  
  private void d(MotionEvent paramMotionEvent)
  {
    if (this.c == null) {
      return;
    }
    this.c.set(paramMotionEvent.getRawX(), paramMotionEvent.getRawY());
  }
  
  private PointF a(MotionEvent paramMotionEvent, int paramInt)
  {
    if ((paramMotionEvent == null) || (paramInt < 0)) {
      return null;
    }
    int m = paramMotionEvent.findPointerIndex(paramInt);
    if (m == -1) {
      return null;
    }
    PointF localPointF = new PointF(paramMotionEvent.getX(m), paramMotionEvent.getY(m));
    return localPointF;
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction() & 0xFF)
    {
    case 0: 
      a(paramView, paramMotionEvent);
      break;
    case 2: 
      return e(paramView, paramMotionEvent);
    case 5: 
      d(paramView, paramMotionEvent);
      break;
    case 1: 
    case 6: 
      b(paramView, paramMotionEvent);
      break;
    }
    return true;
  }
  
  private void a(View paramView, MotionEvent paramMotionEvent)
  {
    a();
    this.a = c(paramMotionEvent);
    c(paramView, paramMotionEvent);
    onTouchBegin(this, paramView, paramMotionEvent);
  }
  
  private void b(View paramView, MotionEvent paramMotionEvent)
  {
    int m = c(paramMotionEvent);
    if ((this.a != m) && (this.b != m)) {
      return;
    }
    if (m == this.b)
    {
      this.b = -1;
      c(paramView, paramMotionEvent);
    }
    else
    {
      onTouchEnd(this, paramView, paramMotionEvent, getStepData());
      a();
    }
  }
  
  private void c(View paramView, MotionEvent paramMotionEvent)
  {
    d(paramMotionEvent);
    this.d = new PointF();
  }
  
  private void d(View paramView, MotionEvent paramMotionEvent)
  {
    if (this.b != -1) {
      return;
    }
    this.b = paramMotionEvent.getPointerId(paramMotionEvent.getActionIndex());
    this.e = a(paramMotionEvent);
    this.g = b(paramMotionEvent);
    this.f = 0.0F;
    this.h = 0.0F;
    b();
    onTouchMultipleBegin(this, paramView, paramMotionEvent);
  }
  
  private boolean e(View paramView, MotionEvent paramMotionEvent)
  {
    if (this.a == -1) {
      return false;
    }
    if (this.b != -1) {
      return g(paramView, paramMotionEvent);
    }
    return f(paramView, paramMotionEvent);
  }
  
  private boolean f(View paramView, MotionEvent paramMotionEvent)
  {
    int m = c(paramMotionEvent);
    if (m != this.a) {
      return false;
    }
    this.d.set(paramMotionEvent.getRawX() - this.c.x, paramMotionEvent.getRawY() - this.c.y);
    d(paramMotionEvent);
    onTouchSingleMove(this, paramView, paramMotionEvent, getStepData());
    return true;
  }
  
  private boolean g(View paramView, MotionEvent paramMotionEvent)
  {
    int m = c(paramMotionEvent);
    if ((m != this.a) && (m != this.b)) {
      return false;
    }
    float f1 = a(paramMotionEvent);
    float f2 = b(paramMotionEvent);
    this.f = (f1 - this.e);
    this.h = (f2 - this.g);
    this.e = f1;
    this.g = f2;
    a(this, paramView, paramMotionEvent);
    return true;
  }
  
  private void b()
  {
    if (!this.i) {
      return;
    }
    ThreadHelper.postDelayed(this.l, getMultipleStablizationDistance());
  }
  
  private void c()
  {
    ThreadHelper.cancel(this.l);
  }
  
  private void a(TuSdkGestureRecognizer paramTuSdkGestureRecognizer, View paramView, MotionEvent paramMotionEvent)
  {
    StepData localStepData = getStepData();
    if (!this.i)
    {
      onTouchMultipleMove(paramTuSdkGestureRecognizer, paramView, paramMotionEvent, localStepData);
      return;
    }
    StepData.a(this.k, localStepData);
  }
  
  private void d()
  {
    onTouchMultipleMoveForStablization(this, StepData.b(this.k));
    StepData.a(this.k);
  }
  
  public abstract void onTouchBegin(TuSdkGestureRecognizer paramTuSdkGestureRecognizer, View paramView, MotionEvent paramMotionEvent);
  
  public void onTouchEnd(TuSdkGestureRecognizer paramTuSdkGestureRecognizer, View paramView, MotionEvent paramMotionEvent, StepData paramStepData) {}
  
  public abstract void onTouchSingleMove(TuSdkGestureRecognizer paramTuSdkGestureRecognizer, View paramView, MotionEvent paramMotionEvent, StepData paramStepData);
  
  public void onTouchMultipleMove(TuSdkGestureRecognizer paramTuSdkGestureRecognizer, View paramView, MotionEvent paramMotionEvent, StepData paramStepData) {}
  
  public void onTouchMultipleMoveForStablization(TuSdkGestureRecognizer paramTuSdkGestureRecognizer, StepData paramStepData) {}
  
  public void onTouchMultipleBegin(TuSdkGestureRecognizer paramTuSdkGestureRecognizer, View paramView, MotionEvent paramMotionEvent) {}
  
  public static class StepData
  {
    public PointF stepPoint = new PointF();
    public float stepSpace;
    public float stepDegree;
    private int a;
    
    private void a()
    {
      this.stepPoint = new PointF();
      this.stepSpace = 0.0F;
      this.stepDegree = 0.0F;
      this.a = 0;
    }
    
    private void c(StepData paramStepData)
    {
      if (paramStepData == null) {
        return;
      }
      if ((paramStepData.stepPoint != null) || (this.stepPoint != null))
      {
        this.stepPoint.x = ((this.stepPoint.x + paramStepData.stepPoint.x) * 0.5F);
        this.stepPoint.y = ((this.stepPoint.y + paramStepData.stepPoint.y) * 0.5F);
      }
      this.stepSpace = ((this.stepSpace + paramStepData.stepSpace) * 0.5F);
      this.stepDegree = ((this.stepDegree + paramStepData.stepDegree) * 0.5F);
      this.a += 1;
    }
    
    private StepData b()
    {
      if (this.a < 1) {
        return this;
      }
      if (this.stepPoint != null)
      {
        this.stepPoint.x *= this.a;
        this.stepPoint.y *= this.a;
      }
      this.stepSpace *= this.a;
      this.stepDegree *= this.a;
      return this;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\TuSdkGestureRecognizer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */