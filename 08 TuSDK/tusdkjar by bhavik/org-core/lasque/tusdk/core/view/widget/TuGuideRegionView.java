package org.lasque.tusdk.core.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import org.lasque.tusdk.core.struct.ViewSize;

public class TuGuideRegionView
  extends View
{
  protected boolean isLayouted;
  private ViewTreeObserver.OnPreDrawListener a = new ViewTreeObserver.OnPreDrawListener()
  {
    public boolean onPreDraw()
    {
      TuGuideRegionView.this.getViewTreeObserver().removeOnPreDrawListener(TuGuideRegionView.a(TuGuideRegionView.this));
      if (!TuGuideRegionView.this.isLayouted) {
        TuGuideRegionView.this.isLayouted = true;
      }
      return false;
    }
  };
  private RectF b;
  private boolean c = false;
  private int d = -1711276033;
  private int e = 2;
  private float f = 8.0F;
  private float g = 4.0F;
  private Path h = new Path();
  private Paint i = new Paint(1);
  
  public TuGuideRegionView(Context paramContext)
  {
    super(paramContext);
    this.i.setAntiAlias(true);
    initView();
  }
  
  public TuGuideRegionView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.i.setAntiAlias(true);
    initView();
  }
  
  public TuGuideRegionView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.i.setAntiAlias(true);
    initView();
  }
  
  protected void initView()
  {
    setLayerType(1, null);
    a();
  }
  
  private void a()
  {
    ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
    localViewTreeObserver.addOnPreDrawListener(this.a);
  }
  
  public void setRegionPercent(RectF paramRectF)
  {
    this.b = paramRectF;
    invalidate();
  }
  
  public RectF getRegionPercent()
  {
    if (this.b == null) {
      this.b = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
    }
    return this.b;
  }
  
  public void setGuideLineViewState(boolean paramBoolean)
  {
    this.c = paramBoolean;
    invalidate();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramBoolean) {
      setRegionPercent(getRegionPercent());
    }
  }
  
  private RectF b()
  {
    ViewSize localViewSize = ViewSize.create(this);
    float f1 = getRegionPercent().left * localViewSize.width;
    float f2 = getRegionPercent().top * localViewSize.height;
    float f3 = getRegionPercent().right * localViewSize.width;
    float f4 = getRegionPercent().bottom * localViewSize.height;
    RectF localRectF = new RectF(f1, f2, f3, f4);
    return localRectF;
  }
  
  public int getGuideLineWidth()
  {
    return this.e;
  }
  
  public void setGuideLineWidth(int paramInt)
  {
    this.e = paramInt;
  }
  
  public float getGuideLineHeight()
  {
    return this.f;
  }
  
  public void setGuideLineHeight(float paramFloat)
  {
    this.f = paramFloat;
  }
  
  public float getGuideLineOffset()
  {
    return this.g;
  }
  
  public void setGuideLineOffset(float paramFloat)
  {
    this.g = paramFloat;
  }
  
  public int getGuideLineColor()
  {
    return this.d;
  }
  
  public void setGuideLineColor(int paramInt)
  {
    this.d = paramInt;
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    a(paramCanvas, b());
    super.onDraw(paramCanvas);
  }
  
  private void a(Canvas paramCanvas, RectF paramRectF)
  {
    if ((paramRectF == null) || (paramCanvas == null) || (!this.c)) {
      return;
    }
    this.h.reset();
    this.h.addRect(paramRectF.left, paramRectF.top, paramRectF.right, paramRectF.bottom, Path.Direction.CW);
    this.h.close();
    this.i.setStyle(Paint.Style.STROKE);
    this.i.setColor(getGuideLineColor());
    this.i.setStrokeWidth(getGuideLineWidth());
    DashPathEffect localDashPathEffect = new DashPathEffect(new float[] { getGuideLineHeight(), getGuideLineOffset() }, 2.0F);
    this.i.setPathEffect(localDashPathEffect);
    float f1 = getGuideLineWidth() * 0.5F;
    paramCanvas.drawLine(paramRectF.left + paramRectF.width() / 3.0F + f1, paramRectF.top, paramRectF.left + paramRectF.width() / 3.0F + f1, paramRectF.bottom, this.i);
    paramCanvas.drawLine(paramRectF.left + paramRectF.width() * 2.0F / 3.0F + f1, paramRectF.top, paramRectF.left + paramRectF.width() * 2.0F / 3.0F + f1, paramRectF.bottom, this.i);
    paramCanvas.drawLine(paramRectF.left, paramRectF.top + paramRectF.height() / 3.0F + f1, paramRectF.right, paramRectF.top + paramRectF.height() / 3.0F + f1, this.i);
    paramCanvas.drawLine(paramRectF.left, paramRectF.top + paramRectF.height() * 2.0F / 3.0F + f1, paramRectF.right, paramRectF.top + paramRectF.height() * 2.0F / 3.0F + f1, this.i);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\TuGuideRegionView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */