package org.lasque.tusdk.core.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.view.View;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.ContextUtils;

public class TuSdkViewDrawer
{
  private View a;
  private int b;
  private boolean c;
  private int d;
  private int e;
  private boolean f;
  private RectF g = new RectF();
  private Path h = new Path();
  private Paint i = new Paint(1);
  
  public int getCornerRadius()
  {
    return this.b;
  }
  
  public void setCornerRadius(int paramInt)
  {
    this.c = (this.b != paramInt);
    this.b = paramInt;
  }
  
  public void setCornerRadiusDP(int paramInt)
  {
    int j = ContextUtils.dip2px(this.a.getContext(), paramInt);
    setCornerRadius(j);
  }
  
  public int getStrokeWidth()
  {
    return this.d;
  }
  
  public void setStrokeWidth(int paramInt)
  {
    this.f = (this.d != paramInt);
    this.d = paramInt;
  }
  
  public void setStrokeWidthDP(int paramInt)
  {
    int j = ContextUtils.dip2px(this.a.getContext(), paramInt);
    setStrokeWidth(j);
  }
  
  public int getStrokeColor()
  {
    return this.e;
  }
  
  public void setStrokeColor(int paramInt)
  {
    this.f = (this.e != paramInt);
    this.e = paramInt;
  }
  
  public void setStrokeColorRes(int paramInt)
  {
    int j = ContextUtils.getResColor(this.a.getContext(), paramInt);
    setStrokeColor(j);
  }
  
  public void setStrokeColorRes(String paramString)
  {
    int j = TuSdkContext.getColor(paramString);
    setStrokeColor(j);
  }
  
  public void setStroke(int paramInt1, int paramInt2)
  {
    setStrokeWidth(paramInt1);
    setStrokeColor(paramInt2);
  }
  
  public void setStrokeDP(int paramInt1, int paramInt2)
  {
    setStrokeWidthDP(paramInt1);
    setStrokeColor(paramInt2);
  }
  
  public TuSdkViewDrawer(View paramView)
  {
    this.i.setAntiAlias(true);
    this.a = paramView;
    if (this.a != null) {
      this.a.setLayerType(1, null);
    }
  }
  
  public void invalidate()
  {
    this.a.invalidate();
  }
  
  public void postInvalidate()
  {
    this.a.postInvalidate();
  }
  
  public void dispatchDrawBefore(Canvas paramCanvas)
  {
    a(paramCanvas);
  }
  
  public void dispatchDrawAfter(Canvas paramCanvas)
  {
    b(paramCanvas);
  }
  
  private void a(Canvas paramCanvas)
  {
    if (!this.c) {
      return;
    }
    this.g.set(0.0F, 0.0F, this.a.getMeasuredWidth(), this.a.getMeasuredHeight());
    this.i.setColor(0);
    paramCanvas.drawRect(this.g, this.i);
    this.h.reset();
    this.h.moveTo(0.0F, 0.0F);
    this.h.addRoundRect(this.g, this.b, this.b, Path.Direction.CW);
    this.h.close();
    paramCanvas.clipPath(this.h);
    this.i.setColor(0);
    paramCanvas.drawRect(this.g, this.i);
  }
  
  private void b(Canvas paramCanvas)
  {
    if (!this.f) {
      return;
    }
    this.g.set(0.0F, 0.0F, this.a.getMeasuredWidth(), this.a.getMeasuredHeight());
    this.i.setColor(this.e);
    this.i.setStrokeWidth(this.d * 2);
    this.i.setStyle(Paint.Style.STROKE);
    paramCanvas.drawRoundRect(this.g, this.b, this.b, this.i);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\TuSdkViewDrawer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */