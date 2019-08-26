package org.lasque.tusdk.core.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.ViewSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;

public class TuMaskRegionView
  extends View
{
  protected boolean isLayouted;
  private ViewTreeObserver.OnPreDrawListener a = new ViewTreeObserver.OnPreDrawListener()
  {
    public boolean onPreDraw()
    {
      TuMaskRegionView.this.getViewTreeObserver().removeOnPreDrawListener(TuMaskRegionView.a(TuMaskRegionView.this));
      if (!TuMaskRegionView.this.isLayouted)
      {
        TuMaskRegionView.this.isLayouted = true;
        TuMaskRegionView.this.onLayouted();
      }
      return false;
    }
  };
  private TuSdkSize b;
  private float c = 0.0F;
  private Rect d;
  private int e = 0;
  private int f = 0;
  private int g;
  private boolean h;
  private RectF i = new RectF();
  private Path j = new Path();
  private Paint k = new Paint(1);
  private RegionChangeAnimation l;
  
  public TuMaskRegionView(Context paramContext)
  {
    super(paramContext);
    this.k.setAntiAlias(true);
    initView();
  }
  
  public TuMaskRegionView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.k.setAntiAlias(true);
    initView();
  }
  
  public TuMaskRegionView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.k.setAntiAlias(true);
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
  
  protected void onLayouted()
  {
    if (this.h)
    {
      a(a(getRegionRatio()));
      this.h = false;
    }
  }
  
  public TuSdkSize getRegionSize()
  {
    return this.b;
  }
  
  public void setRegionSize(TuSdkSize paramTuSdkSize)
  {
    this.b = paramTuSdkSize;
    if (paramTuSdkSize != null)
    {
      this.c = (paramTuSdkSize.width / paramTuSdkSize.height);
      autoShowForRegionRatio();
      a(a(this.c));
    }
  }
  
  public float getRegionRatio()
  {
    return this.c;
  }
  
  public Rect setRegionRatio(float paramFloat)
  {
    this.c = paramFloat;
    Rect localRect = a(this.c);
    if (this.isLayouted) {
      a(localRect);
    } else {
      this.h = true;
    }
    autoShowForRegionRatio();
    return localRect;
  }
  
  public void autoShowForRegionRatio()
  {
    if (this.c <= 0.0F) {
      ViewCompat.setAlpha(this, 0.0F);
    } else if (ViewCompat.getAlpha(this) == 0.0F) {
      ViewCompat.setAlpha(this, 1.0F);
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramBoolean) {
      setRegionRatio(getRegionRatio());
    }
  }
  
  private void a(Rect paramRect)
  {
    this.d = paramRect;
    invalidate();
  }
  
  private Rect a(float paramFloat)
  {
    return RectHelper.computerCenter(ViewSize.create(this), paramFloat);
  }
  
  public Rect getRegionRect()
  {
    return this.d;
  }
  
  public int getEdgeMaskColor()
  {
    return this.e;
  }
  
  public void setEdgeMaskColor(int paramInt)
  {
    this.e = paramInt;
  }
  
  public int getEdgeSideColor()
  {
    return this.f;
  }
  
  public void setEdgeSideColor(int paramInt)
  {
    this.f = paramInt;
  }
  
  public int getEdgeSideWidth()
  {
    return this.g;
  }
  
  public void setEdgeSideWidth(int paramInt)
  {
    this.g = paramInt;
  }
  
  public void setEdgeSideWidthDP(int paramInt)
  {
    this.g = ContextUtils.dip2px(getContext(), paramInt);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    a(paramCanvas, getRegionRect());
    super.onDraw(paramCanvas);
  }
  
  private void a(Canvas paramCanvas, Rect paramRect)
  {
    if ((paramRect == null) || (paramCanvas == null)) {
      return;
    }
    this.i.set(0.0F, 0.0F, getMeasuredWidth(), getMeasuredHeight());
    float f1 = 0.0F;
    if (getEdgeSideWidth() > 0)
    {
      this.k.setColor(getEdgeSideColor());
      this.k.setStrokeWidth(getEdgeSideWidth());
      this.k.setStyle(Paint.Style.STROKE);
      f1 = getEdgeSideWidth() * 0.5F;
      paramCanvas.drawRect(new RectF(paramRect.left + f1, paramRect.top + f1, paramRect.right - f1, paramRect.bottom - f1), this.k);
    }
    this.j.reset();
    this.j.addRect(paramRect.left, paramRect.top, paramRect.right, paramRect.bottom, Path.Direction.CW);
    this.j.close();
    paramCanvas.clipPath(this.j, Region.Op.DIFFERENCE);
    this.k.setColor(getEdgeMaskColor());
    this.k.setStyle(Paint.Style.FILL);
    paramCanvas.drawRect(this.i, this.k);
  }
  
  public Rect changeRegionRatio(float paramFloat)
  {
    Rect localRect = a(paramFloat);
    if (this.c == paramFloat) {
      return localRect;
    }
    if (paramFloat <= 0.0F) {
      ViewCompat.animate(this).alpha(0.0F).setDuration(260L).setInterpolator(new AccelerateDecelerateInterpolator());
    } else if (ViewCompat.getAlpha(this) == 0.0F) {
      ViewCompat.animate(this).alpha(1.0F).setDuration(260L).setInterpolator(new AccelerateDecelerateInterpolator());
    }
    b().startTo(localRect);
    startAnimation(b());
    this.c = paramFloat;
    return localRect;
  }
  
  private RegionChangeAnimation b()
  {
    if (this.l == null)
    {
      this.l = new RegionChangeAnimation(getRegionRatio());
      this.l.setDuration(260L);
      this.l.setInterpolator(new AccelerateDecelerateInterpolator());
    }
    this.l.cancel();
    this.l.reset();
    return this.l;
  }
  
  private class RegionChangeAnimation
    extends Animation
  {
    private Rect b;
    private Rect c;
    private Rect d;
    
    public RegionChangeAnimation(float paramFloat)
    {
      this.b = TuMaskRegionView.a(TuMaskRegionView.this, paramFloat);
    }
    
    public void startTo(Rect paramRect)
    {
      this.c = paramRect;
      this.d = new Rect(this.c.left - this.b.left, this.c.top - this.b.top, this.c.right - this.b.right, this.c.bottom - this.b.bottom);
    }
    
    protected void applyTransformation(float paramFloat, Transformation paramTransformation)
    {
      paramFloat = 1.0F - paramFloat;
      this.b.left = (this.c.left - (int)(this.d.left * paramFloat));
      this.b.top = (this.c.top - (int)(this.d.top * paramFloat));
      this.b.right = (this.c.right - (int)(this.d.right * paramFloat));
      this.b.bottom = (this.c.bottom - (int)(this.d.bottom * paramFloat));
      TuMaskRegionView.a(TuMaskRegionView.this, this.b);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\TuMaskRegionView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */