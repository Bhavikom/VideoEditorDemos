package org.lasque.tusdk.impl.components.widget.smudge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import org.lasque.tusdk.core.utils.anim.AnimHelper.TuSdkViewAnimatorAdapter;

public class TuBrushSizeAnimView
  extends View
{
  protected boolean isLayouted;
  private boolean a;
  private int b;
  private Paint c = new Paint(1);
  private int d;
  private int e;
  private ViewTreeObserver.OnPreDrawListener f;
  private SizeChangeAnimation g;
  private Runnable h;
  
  public TuBrushSizeAnimView(Context paramContext)
  {
    super(paramContext);
    this.c.setAntiAlias(true);
    this.d = -1;
    this.e = 2;
    this.f = new ViewTreeObserver.OnPreDrawListener()
    {
      public boolean onPreDraw()
      {
        TuBrushSizeAnimView.this.getViewTreeObserver().removeOnPreDrawListener(TuBrushSizeAnimView.a(TuBrushSizeAnimView.this));
        if (!TuBrushSizeAnimView.this.isLayouted)
        {
          TuBrushSizeAnimView.this.isLayouted = true;
          TuBrushSizeAnimView.this.onLayouted();
        }
        return false;
      }
    };
    this.h = new Runnable()
    {
      public void run()
      {
        TuBrushSizeAnimView.d(TuBrushSizeAnimView.this);
      }
    };
    initView();
  }
  
  public TuBrushSizeAnimView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.c.setAntiAlias(true);
    this.d = -1;
    this.e = 2;
    this.f = new ViewTreeObserver.OnPreDrawListener()
    {
      public boolean onPreDraw()
      {
        TuBrushSizeAnimView.this.getViewTreeObserver().removeOnPreDrawListener(TuBrushSizeAnimView.a(TuBrushSizeAnimView.this));
        if (!TuBrushSizeAnimView.this.isLayouted)
        {
          TuBrushSizeAnimView.this.isLayouted = true;
          TuBrushSizeAnimView.this.onLayouted();
        }
        return false;
      }
    };
    this.h = new Runnable()
    {
      public void run()
      {
        TuBrushSizeAnimView.d(TuBrushSizeAnimView.this);
      }
    };
    initView();
  }
  
  public TuBrushSizeAnimView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.c.setAntiAlias(true);
    this.d = -1;
    this.e = 2;
    this.f = new ViewTreeObserver.OnPreDrawListener()
    {
      public boolean onPreDraw()
      {
        TuBrushSizeAnimView.this.getViewTreeObserver().removeOnPreDrawListener(TuBrushSizeAnimView.a(TuBrushSizeAnimView.this));
        if (!TuBrushSizeAnimView.this.isLayouted)
        {
          TuBrushSizeAnimView.this.isLayouted = true;
          TuBrushSizeAnimView.this.onLayouted();
        }
        return false;
      }
    };
    this.h = new Runnable()
    {
      public void run()
      {
        TuBrushSizeAnimView.d(TuBrushSizeAnimView.this);
      }
    };
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
    localViewTreeObserver.addOnPreDrawListener(this.f);
  }
  
  protected void onLayouted()
  {
    if (this.a)
    {
      a(getRadius());
      this.a = false;
    }
  }
  
  public int getBorderColor()
  {
    return this.d;
  }
  
  public void setBorderColor(int paramInt)
  {
    this.d = paramInt;
  }
  
  public int getBorderWidth()
  {
    return this.e;
  }
  
  public void setBorderWidth(int paramInt)
  {
    this.e = paramInt;
  }
  
  private void a(int paramInt)
  {
    this.b = paramInt;
    invalidate();
  }
  
  public int getRadius()
  {
    return this.b;
  }
  
  public void changeRadius(int paramInt1, int paramInt2)
  {
    if (this.b == paramInt2) {
      return;
    }
    this.b = paramInt1;
    ViewCompat.setAlpha(this, 1.0F);
    b().start(this.b, paramInt2);
    startAnimation(b());
    this.b = paramInt2;
  }
  
  private void b(int paramInt)
  {
    a(paramInt);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    a(paramCanvas, getRadius());
    super.onDraw(paramCanvas);
  }
  
  private SizeChangeAnimation b()
  {
    if (this.g == null)
    {
      this.g = new SizeChangeAnimation();
      this.g.setDuration(260L);
      this.g.setInterpolator(new AccelerateDecelerateInterpolator());
    }
    this.g.cancel();
    this.g.reset();
    return this.g;
  }
  
  private void c()
  {
    ViewCompat.animate(this).alpha(0.0F).setDuration(200L).setListener(new AnimHelper.TuSdkViewAnimatorAdapter()
    {
      public void onAnimationEnd(View paramAnonymousView, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean) {
          return;
        }
        paramAnonymousView.setVisibility(4);
      }
    });
  }
  
  private void a(Canvas paramCanvas, int paramInt)
  {
    if ((paramCanvas == null) || (paramInt <= 0)) {
      return;
    }
    int i = getMeasuredWidth();
    int j = getMeasuredHeight();
    this.c.setColor(getBorderColor());
    this.c.setStrokeWidth(getBorderWidth());
    this.c.setStyle(Paint.Style.FILL_AND_STROKE);
    paramCanvas.drawCircle(i / 2, j / 2, paramInt, this.c);
  }
  
  private class SizeChangeAnimation
    extends Animation
  {
    private int b;
    private int c;
    
    public SizeChangeAnimation() {}
    
    public void start(int paramInt1, int paramInt2)
    {
      this.b = paramInt2;
      this.c = (paramInt2 - paramInt1);
    }
    
    protected void applyTransformation(float paramFloat, Transformation paramTransformation)
    {
      paramFloat = 1.0F - paramFloat;
      TuBrushSizeAnimView.a(TuBrushSizeAnimView.this, this.b - (int)(this.c * paramFloat));
      if (paramFloat <= 0.0F)
      {
        TuBrushSizeAnimView.b(TuBrushSizeAnimView.this).cancel();
        ThreadHelper.postDelayed(TuBrushSizeAnimView.c(TuBrushSizeAnimView.this), 500L);
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\smudge\TuBrushSizeAnimView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */