package org.lasque.tusdk.impl.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.ViewSize;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public class TuSeekBar
  extends TuSdkRelativeLayout
{
  private boolean a = true;
  private View b;
  private View c;
  private View d;
  private float e;
  private TuSeekBarDelegate f;
  private int g;
  private int h;
  private float i = 1.2F;
  private int j;
  
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_view_widget_seekbar");
  }
  
  public TuSeekBar(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuSeekBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSeekBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public View getBottomView()
  {
    if (this.b == null) {
      this.b = getViewById("lsq_seekBottomView");
    }
    return this.b;
  }
  
  public void setBottomViewBackgroundResourceId(int paramInt)
  {
    if (getBottomView() == null) {
      return;
    }
    getBottomView().setBackgroundResource(paramInt);
  }
  
  public View getTopView()
  {
    if (this.c == null) {
      this.c = getViewById("lsq_seekTopView");
    }
    return this.c;
  }
  
  public View getDragView()
  {
    if (this.d == null) {
      this.d = getViewById("lsq_seekDrag");
    }
    return this.d;
  }
  
  public void setDragViewBackgroundResourceId(int paramInt)
  {
    if (getDragView() == null) {
      return;
    }
    getDragView().setBackgroundResource(paramInt);
  }
  
  public TuSeekBarDelegate getDelegate()
  {
    return this.f;
  }
  
  public void setDelegate(TuSeekBarDelegate paramTuSeekBarDelegate)
  {
    this.f = paramTuSeekBarDelegate;
  }
  
  public float getProgress()
  {
    return this.e;
  }
  
  public void setProgress(float paramFloat)
  {
    if (paramFloat < 0.0F) {
      paramFloat = 0.0F;
    } else if (paramFloat > 1.0F) {
      paramFloat = 1.0F;
    }
    this.e = paramFloat;
    int k = (int)Math.floor(this.j * this.e);
    setMarginLeft(getDragView(), k - this.g / 2 + this.h);
    setWidth(getTopView(), k);
  }
  
  protected void onLayouted()
  {
    super.onLayouted();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    a();
  }
  
  private void a()
  {
    this.g = ViewSize.create(getDragView()).width;
    if (this.g == 0) {
      return;
    }
    this.h = ((int)(this.g * this.i / 2.0F));
    this.j = (getMeasuredWidth() - this.h * 2);
    setMargin(getBottomView(), this.h, 0, this.h, 0);
    setMarginLeft(getTopView(), this.h);
    setProgress(this.e);
  }
  
  private void a(float paramFloat)
  {
    float f1 = (paramFloat - this.h) / this.j;
    if (this.e == f1) {
      return;
    }
    setProgress(f1);
    if (this.f != null) {
      this.f.onTuSeekBarChanged(this, getProgress());
    }
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!this.a) {
      return false;
    }
    if (paramMotionEvent.getPointerCount() > 1) {
      return super.onTouchEvent(paramMotionEvent);
    }
    switch (paramMotionEvent.getAction())
    {
    case 2: 
      a(paramMotionEvent.getX());
      break;
    case 0: 
      a(paramMotionEvent.getX());
      ViewCompat.setScaleX(getDragView(), this.i);
      ViewCompat.setScaleY(getDragView(), this.i);
      break;
    case 1: 
    default: 
      ViewCompat.setScaleX(getDragView(), 1.0F);
      ViewCompat.setScaleY(getDragView(), 1.0F);
    }
    return true;
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
  
  public boolean getEnabled()
  {
    return this.a;
  }
  
  public static abstract interface TuSeekBarDelegate
  {
    public abstract void onTuSeekBarChanged(TuSeekBar paramTuSeekBar, float paramFloat);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\TuSeekBar.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */