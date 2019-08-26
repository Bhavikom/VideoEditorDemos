package org.lasque.tusdk.core.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.OverScroller;
import android.widget.Scroller;

public class TouchImageView
  extends ImageView
{
  private float a;
  private Matrix b;
  private Matrix c;
  private State d;
  private float e;
  private float f;
  private float g;
  private float h;
  private float[] i;
  private Context j;
  private Fling k;
  private ImageView.ScaleType l;
  private boolean m;
  private boolean n;
  private ZoomVariables o;
  private int p;
  private int q;
  private int r;
  private int s;
  private float t;
  private float u;
  private float v;
  private float w;
  private ScaleGestureDetector x;
  private GestureDetector y;
  private GestureDetector.OnDoubleTapListener z = null;
  private View.OnTouchListener A = null;
  private OnTouchImageViewListener B = null;
  
  public TouchImageView(Context paramContext)
  {
    super(paramContext);
    a(paramContext);
  }
  
  public TouchImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    a(paramContext);
  }
  
  public TouchImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    a(paramContext);
  }
  
  private void a(Context paramContext)
  {
    super.setClickable(true);
    this.j = paramContext;
    this.x = new ScaleGestureDetector(paramContext, new ScaleListener(null));
    this.y = new GestureDetector(paramContext, new GestureListener(null));
    this.b = new Matrix();
    this.c = new Matrix();
    this.i = new float[9];
    this.a = 1.0F;
    if (this.l == null) {
      this.l = ImageView.ScaleType.FIT_CENTER;
    }
    this.e = 1.0F;
    this.f = 3.0F;
    this.g = (0.75F * this.e);
    this.h = (1.25F * this.f);
    setImageMatrix(this.b);
    setScaleType(ImageView.ScaleType.MATRIX);
    a(State.NONE);
    this.n = false;
    super.setOnTouchListener(new PrivateOnTouchListener(null));
  }
  
  public void setOnTouchListener(View.OnTouchListener paramOnTouchListener)
  {
    this.A = paramOnTouchListener;
  }
  
  public void setOnTouchImageViewListener(OnTouchImageViewListener paramOnTouchImageViewListener)
  {
    this.B = paramOnTouchImageViewListener;
  }
  
  public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener)
  {
    this.z = paramOnDoubleTapListener;
  }
  
  public void setImageResource(int paramInt)
  {
    super.setImageResource(paramInt);
    a();
    f();
  }
  
  public void setImageBitmap(Bitmap paramBitmap)
  {
    super.setImageBitmap(paramBitmap);
    a();
    f();
  }
  
  public void setImageDrawable(Drawable paramDrawable)
  {
    super.setImageDrawable(paramDrawable);
    a();
    f();
  }
  
  public void setImageURI(Uri paramUri)
  {
    super.setImageURI(paramUri);
    a();
    f();
  }
  
  public void setScaleType(ImageView.ScaleType paramScaleType)
  {
    if ((paramScaleType == ImageView.ScaleType.FIT_START) || (paramScaleType == ImageView.ScaleType.FIT_END)) {
      throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");
    }
    if (paramScaleType == ImageView.ScaleType.MATRIX)
    {
      super.setScaleType(ImageView.ScaleType.MATRIX);
    }
    else
    {
      this.l = paramScaleType;
      if (this.n) {
        setZoom(this);
      }
    }
  }
  
  public ImageView.ScaleType getScaleType()
  {
    return this.l;
  }
  
  public boolean isZoomed()
  {
    return this.a != 1.0F;
  }
  
  public RectF getZoomedRect()
  {
    if (this.l == ImageView.ScaleType.FIT_XY) {
      throw new UnsupportedOperationException("getZoomedRect() not supported with FIT_XY");
    }
    if (getDrawable() == null) {
      return new RectF(0.0F, 0.0F, 1.0F, 1.0F);
    }
    PointF localPointF1 = a(0.0F, 0.0F, true);
    PointF localPointF2 = a(this.p, this.q, true);
    float f1 = getDrawable().getIntrinsicWidth();
    float f2 = getDrawable().getIntrinsicHeight();
    return new RectF(localPointF1.x / f1, localPointF1.y / f2, localPointF2.x / f1, localPointF2.y / f2);
  }
  
  private void a()
  {
    if ((this.b != null) && (this.q != 0) && (this.p != 0))
    {
      this.b.getValues(this.i);
      this.c.setValues(this.i);
      this.w = this.u;
      this.v = this.t;
      this.s = this.q;
      this.r = this.p;
    }
  }
  
  public Parcelable onSaveInstanceState()
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("instanceState", super.onSaveInstanceState());
    localBundle.putFloat("saveScale", this.a);
    localBundle.putFloat("matchViewHeight", this.u);
    localBundle.putFloat("matchViewWidth", this.t);
    localBundle.putInt("viewWidth", this.p);
    localBundle.putInt("viewHeight", this.q);
    this.b.getValues(this.i);
    localBundle.putFloatArray("matrix", this.i);
    localBundle.putBoolean("imageRendered", this.m);
    return localBundle;
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable instanceof Bundle))
    {
      Bundle localBundle = (Bundle)paramParcelable;
      this.a = localBundle.getFloat("saveScale");
      this.i = localBundle.getFloatArray("matrix");
      this.c.setValues(this.i);
      this.w = localBundle.getFloat("matchViewHeight");
      this.v = localBundle.getFloat("matchViewWidth");
      this.s = localBundle.getInt("viewHeight");
      this.r = localBundle.getInt("viewWidth");
      this.m = localBundle.getBoolean("imageRendered");
      super.onRestoreInstanceState(localBundle.getParcelable("instanceState"));
      return;
    }
    super.onRestoreInstanceState(paramParcelable);
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    this.n = true;
    this.m = true;
    if (this.o != null)
    {
      setZoom(this.o.scale, this.o.focusX, this.o.focusY, this.o.scaleType);
      this.o = null;
    }
    super.onDraw(paramCanvas);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    a();
  }
  
  public float getMaxZoom()
  {
    return this.f;
  }
  
  public void setMaxZoom(float paramFloat)
  {
    this.f = paramFloat;
    this.h = (1.25F * this.f);
  }
  
  public float getMinZoom()
  {
    return this.e;
  }
  
  public float getCurrentZoom()
  {
    return this.a;
  }
  
  public void setMinZoom(float paramFloat)
  {
    this.e = paramFloat;
    this.g = (0.75F * this.e);
  }
  
  public void resetZoom()
  {
    this.a = 1.0F;
    f();
  }
  
  public void setZoom(float paramFloat)
  {
    setZoom(paramFloat, 0.5F, 0.5F);
  }
  
  public void setZoom(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    setZoom(paramFloat1, paramFloat2, paramFloat3, this.l);
  }
  
  public void setZoom(float paramFloat1, float paramFloat2, float paramFloat3, ImageView.ScaleType paramScaleType)
  {
    if (!this.n)
    {
      this.o = new ZoomVariables(paramFloat1, paramFloat2, paramFloat3, paramScaleType);
      return;
    }
    if (paramScaleType != this.l) {
      setScaleType(paramScaleType);
    }
    resetZoom();
    a(paramFloat1, this.p / 2, this.q / 2, true);
    this.b.getValues(this.i);
    this.i[2] = (-(paramFloat2 * d() - this.p * 0.5F));
    this.i[5] = (-(paramFloat3 * e() - this.q * 0.5F));
    this.b.setValues(this.i);
    b();
    setImageMatrix(this.b);
  }
  
  public void setZoom(TouchImageView paramTouchImageView)
  {
    PointF localPointF = paramTouchImageView.getScrollPosition();
    setZoom(paramTouchImageView.getCurrentZoom(), localPointF.x, localPointF.y, paramTouchImageView.getScaleType());
  }
  
  public PointF getScrollPosition()
  {
    Drawable localDrawable = getDrawable();
    if (localDrawable == null) {
      return null;
    }
    int i1 = localDrawable.getIntrinsicWidth();
    int i2 = localDrawable.getIntrinsicHeight();
    PointF localPointF = a(this.p / 2, this.q / 2, true);
    localPointF.x /= i1;
    localPointF.y /= i2;
    return localPointF;
  }
  
  public void setScrollPosition(float paramFloat1, float paramFloat2)
  {
    setZoom(this.a, paramFloat1, paramFloat2);
  }
  
  public void resetZoomToCenter()
  {
    this.b.getValues(this.i);
    float f1 = this.i[2];
    float f2 = this.i[5];
    float f3 = d() * 0.5F + f1;
    float f4 = e() * 0.5F + f2;
    DoubleTapZoom localDoubleTapZoom = new DoubleTapZoom(this.e, f3, f4, false);
    compatPostOnAnimation(localDoubleTapZoom);
  }
  
  public void setImageBitmapWithAnim(Bitmap paramBitmap)
  {
    super.setImageBitmap(paramBitmap);
    a();
    g();
    resetZoomToCenter();
  }
  
  private void b()
  {
    this.b.getValues(this.i);
    float f1 = this.i[2];
    float f2 = this.i[5];
    float f3 = a(f1, this.p, d());
    float f4 = a(f2, this.q, e());
    if ((f3 != 0.0F) || (f4 != 0.0F)) {
      this.b.postTranslate(f3, f4);
    }
  }
  
  private void c()
  {
    b();
    this.b.getValues(this.i);
    if (d() < this.p) {
      this.i[2] = ((this.p - d()) / 2.0F);
    }
    if (e() < this.q) {
      this.i[5] = ((this.q - e()) / 2.0F);
    }
    this.b.setValues(this.i);
  }
  
  private float a(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    float f1;
    float f2;
    if (paramFloat3 <= paramFloat2)
    {
      f1 = 0.0F;
      f2 = paramFloat2 - paramFloat3;
    }
    else
    {
      f1 = paramFloat2 - paramFloat3;
      f2 = 0.0F;
    }
    if (paramFloat1 < f1) {
      return -paramFloat1 + f1;
    }
    if (paramFloat1 > f2) {
      return -paramFloat1 + f2;
    }
    return 0.0F;
  }
  
  private float b(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat3 <= paramFloat2) {
      return 0.0F;
    }
    return paramFloat1;
  }
  
  private float d()
  {
    return this.t * this.a;
  }
  
  private float e()
  {
    return this.u * this.a;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    Drawable localDrawable = getDrawable();
    if ((localDrawable == null) || (localDrawable.getIntrinsicWidth() == 0) || (localDrawable.getIntrinsicHeight() == 0))
    {
      setMeasuredDimension(0, 0);
      return;
    }
    int i1 = localDrawable.getIntrinsicWidth();
    int i2 = localDrawable.getIntrinsicHeight();
    int i3 = View.MeasureSpec.getSize(paramInt1);
    int i4 = View.MeasureSpec.getMode(paramInt1);
    int i5 = View.MeasureSpec.getSize(paramInt2);
    int i6 = View.MeasureSpec.getMode(paramInt2);
    this.p = a(i4, i3, i1);
    this.q = a(i6, i5, i2);
    setMeasuredDimension(this.p, this.q);
    f();
  }
  
  private void f()
  {
    g();
    setImageMatrix(this.b);
  }
  
  private void g()
  {
    Drawable localDrawable = getDrawable();
    if ((localDrawable == null) || (localDrawable.getIntrinsicWidth() == 0) || (localDrawable.getIntrinsicHeight() == 0)) {
      return;
    }
    if ((this.b == null) || (this.c == null)) {
      return;
    }
    int i1 = localDrawable.getIntrinsicWidth();
    int i2 = localDrawable.getIntrinsicHeight();
    float f1 = this.p / i1;
    float f2 = this.q / i2;
    switch (1.a[this.l.ordinal()])
    {
    case 1: 
      f1 = f2 = 1.0F;
      break;
    case 2: 
      f1 = f2 = Math.max(f1, f2);
      break;
    case 3: 
      f1 = f2 = Math.min(1.0F, Math.min(f1, f2));
    case 4: 
      f1 = f2 = Math.min(f1, f2);
      break;
    case 5: 
      break;
    default: 
      throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");
    }
    float f3 = this.p - f1 * i1;
    float f4 = this.q - f2 * i2;
    this.t = (this.p - f3);
    this.u = (this.q - f4);
    if ((!isZoomed()) && (!this.m))
    {
      this.b.setScale(f1, f2);
      this.b.postTranslate(f3 / 2.0F, f4 / 2.0F);
      this.a = 1.0F;
    }
    else
    {
      if ((this.v == 0.0F) || (this.w == 0.0F)) {
        a();
      }
      this.c.getValues(this.i);
      this.i[0] = (this.t / i1 * this.a);
      this.i[4] = (this.u / i2 * this.a);
      float f5 = this.i[2];
      float f6 = this.i[5];
      float f7 = this.v * this.a;
      float f8 = d();
      a(2, f5, f7, f8, this.r, this.p, i1);
      float f9 = this.w * this.a;
      float f10 = e();
      a(5, f6, f9, f10, this.s, this.q, i2);
      this.b.setValues(this.i);
    }
    b();
  }
  
  private int a(int paramInt1, int paramInt2, int paramInt3)
  {
    int i1;
    switch (paramInt1)
    {
    case 1073741824: 
      i1 = paramInt2;
      break;
    case -2147483648: 
      i1 = Math.min(paramInt3, paramInt2);
      break;
    case 0: 
      i1 = paramInt3;
      break;
    default: 
      i1 = paramInt2;
    }
    return i1;
  }
  
  private void a(int paramInt1, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramFloat3 < paramInt3)
    {
      this.i[paramInt1] = ((paramInt3 - paramInt4 * this.i[0]) * 0.5F);
    }
    else if (paramFloat1 > 0.0F)
    {
      this.i[paramInt1] = (-((paramFloat3 - paramInt3) * 0.5F));
    }
    else
    {
      float f1 = (Math.abs(paramFloat1) + 0.5F * paramInt2) / paramFloat2;
      this.i[paramInt1] = (-(f1 * paramFloat3 - paramInt3 * 0.5F));
    }
  }
  
  private void a(State paramState)
  {
    this.d = paramState;
  }
  
  public boolean canScrollHorizontallyFroyo(int paramInt)
  {
    return canScrollHorizontally(paramInt);
  }
  
  public boolean canScrollHorizontally(int paramInt)
  {
    this.b.getValues(this.i);
    float f1 = this.i[2];
    if (d() < this.p) {
      return false;
    }
    if ((f1 >= -1.0F) && (paramInt < 0)) {
      return false;
    }
    return (Math.abs(f1) + this.p + 1.0F < d()) || (paramInt <= 0);
  }
  
  private void a(double paramDouble, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    float f1;
    float f2;
    if (paramBoolean)
    {
      f1 = this.g;
      f2 = this.h;
    }
    else
    {
      f1 = this.e;
      f2 = this.f;
    }
    float f3 = this.a;
    this.a = ((float)(this.a * paramDouble));
    if (this.a > f2)
    {
      this.a = f2;
      paramDouble = f2 / f3;
    }
    else if (this.a < f1)
    {
      this.a = f1;
      paramDouble = f1 / f3;
    }
    this.b.postScale((float)paramDouble, (float)paramDouble, paramFloat1, paramFloat2);
    c();
  }
  
  private PointF a(float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    this.b.getValues(this.i);
    float f1 = getDrawable().getIntrinsicWidth();
    float f2 = getDrawable().getIntrinsicHeight();
    float f3 = this.i[2];
    float f4 = this.i[5];
    float f5 = (paramFloat1 - f3) * f1 / d();
    float f6 = (paramFloat2 - f4) * f2 / e();
    if (paramBoolean)
    {
      f5 = Math.min(Math.max(f5, 0.0F), f1);
      f6 = Math.min(Math.max(f6, 0.0F), f2);
    }
    return new PointF(f5, f6);
  }
  
  private PointF a(float paramFloat1, float paramFloat2)
  {
    this.b.getValues(this.i);
    float f1 = getDrawable().getIntrinsicWidth();
    float f2 = getDrawable().getIntrinsicHeight();
    float f3 = paramFloat1 / f1;
    float f4 = paramFloat2 / f2;
    float f5 = this.i[2] + d() * f3;
    float f6 = this.i[5] + e() * f4;
    return new PointF(f5, f6);
  }
  
  @TargetApi(16)
  protected void compatPostOnAnimation(Runnable paramRunnable)
  {
    if (Build.VERSION.SDK_INT >= 16) {
      postOnAnimation(paramRunnable);
    } else {
      postDelayed(paramRunnable, 16L);
    }
  }
  
  public void printMatrixInfo()
  {
    float[] arrayOfFloat = new float[9];
    this.b.getValues(arrayOfFloat);
    Log.d("DEBUG", "Scale: " + arrayOfFloat[0] + " TransX: " + arrayOfFloat[2] + " TransY: " + arrayOfFloat[5]);
  }
  
  private class ZoomVariables
  {
    public float scale;
    public float focusX;
    public float focusY;
    public ImageView.ScaleType scaleType;
    
    public ZoomVariables(float paramFloat1, float paramFloat2, float paramFloat3, ImageView.ScaleType paramScaleType)
    {
      this.scale = paramFloat1;
      this.focusX = paramFloat2;
      this.focusY = paramFloat3;
      this.scaleType = paramScaleType;
    }
  }
  
  @TargetApi(9)
  private class CompatScroller
  {
    Scroller a;
    OverScroller b;
    boolean c;
    
    public CompatScroller(Context paramContext)
    {
      if (Build.VERSION.SDK_INT < 9)
      {
        this.c = true;
        this.a = new Scroller(paramContext);
      }
      else
      {
        this.c = false;
        this.b = new OverScroller(paramContext);
      }
    }
    
    public void fling(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
    {
      if (this.c) {
        this.a.fling(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
      } else {
        this.b.fling(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
      }
    }
    
    public void forceFinished(boolean paramBoolean)
    {
      if (this.c) {
        this.a.forceFinished(paramBoolean);
      } else {
        this.b.forceFinished(paramBoolean);
      }
    }
    
    public boolean isFinished()
    {
      if (this.c) {
        return this.a.isFinished();
      }
      return this.b.isFinished();
    }
    
    public boolean computeScrollOffset()
    {
      if (this.c) {
        return this.a.computeScrollOffset();
      }
      this.b.computeScrollOffset();
      return this.b.computeScrollOffset();
    }
    
    public int getCurrX()
    {
      if (this.c) {
        return this.a.getCurrX();
      }
      return this.b.getCurrX();
    }
    
    public int getCurrY()
    {
      if (this.c) {
        return this.a.getCurrY();
      }
      return this.b.getCurrY();
    }
  }
  
  private class Fling
    implements Runnable
  {
    TouchImageView.CompatScroller a;
    int b;
    int c;
    
    Fling(int paramInt1, int paramInt2)
    {
      TouchImageView.a(TouchImageView.this, TouchImageView.State.FLING);
      this.a = new TouchImageView.CompatScroller(TouchImageView.this, TouchImageView.r(TouchImageView.this));
      TouchImageView.m(TouchImageView.this).getValues(TouchImageView.s(TouchImageView.this));
      int i = (int)TouchImageView.s(TouchImageView.this)[2];
      int j = (int)TouchImageView.s(TouchImageView.this)[5];
      int k;
      int m;
      if (TouchImageView.j(TouchImageView.this) > TouchImageView.i(TouchImageView.this))
      {
        k = TouchImageView.i(TouchImageView.this) - (int)TouchImageView.j(TouchImageView.this);
        m = 0;
      }
      else
      {
        k = m = i;
      }
      int n;
      int i1;
      if (TouchImageView.l(TouchImageView.this) > TouchImageView.k(TouchImageView.this))
      {
        n = TouchImageView.k(TouchImageView.this) - (int)TouchImageView.l(TouchImageView.this);
        i1 = 0;
      }
      else
      {
        n = i1 = j;
      }
      this.a.fling(i, j, paramInt1, paramInt2, k, m, n, i1);
      this.b = i;
      this.c = j;
    }
    
    public void cancelFling()
    {
      if (this.a != null)
      {
        TouchImageView.a(TouchImageView.this, TouchImageView.State.NONE);
        this.a.forceFinished(true);
      }
    }
    
    public void run()
    {
      if (TouchImageView.p(TouchImageView.this) != null) {
        TouchImageView.p(TouchImageView.this).onMove();
      }
      if (this.a.isFinished())
      {
        this.a = null;
        return;
      }
      if (this.a.computeScrollOffset())
      {
        int i = this.a.getCurrX();
        int j = this.a.getCurrY();
        int k = i - this.b;
        int m = j - this.c;
        this.b = i;
        this.c = j;
        TouchImageView.m(TouchImageView.this).postTranslate(k, m);
        TouchImageView.n(TouchImageView.this);
        TouchImageView.this.setImageMatrix(TouchImageView.m(TouchImageView.this));
        TouchImageView.this.compatPostOnAnimation(this);
      }
    }
  }
  
  private class DoubleTapZoom
    implements Runnable
  {
    private long b;
    private float c;
    private float d;
    private float e;
    private float f;
    private boolean g;
    private AccelerateDecelerateInterpolator h = new AccelerateDecelerateInterpolator();
    private PointF i;
    private PointF j;
    
    DoubleTapZoom(float paramFloat1, float paramFloat2, float paramFloat3, boolean paramBoolean)
    {
      TouchImageView.a(TouchImageView.this, TouchImageView.State.ANIMATE_ZOOM);
      this.b = System.currentTimeMillis();
      this.c = TouchImageView.d(TouchImageView.this);
      this.d = paramFloat1;
      this.g = paramBoolean;
      PointF localPointF = TouchImageView.a(TouchImageView.this, paramFloat2, paramFloat3, false);
      this.e = localPointF.x;
      this.f = localPointF.y;
      this.i = TouchImageView.a(TouchImageView.this, this.e, this.f);
      this.j = new PointF(TouchImageView.i(TouchImageView.this) / 2, TouchImageView.k(TouchImageView.this) / 2);
    }
    
    public void run()
    {
      float f1 = a();
      double d1 = b(f1);
      TouchImageView.a(TouchImageView.this, d1, this.e, this.f, this.g);
      a(f1);
      TouchImageView.q(TouchImageView.this);
      TouchImageView.this.setImageMatrix(TouchImageView.m(TouchImageView.this));
      if (TouchImageView.p(TouchImageView.this) != null) {
        TouchImageView.p(TouchImageView.this).onMove();
      }
      if (f1 < 1.0F) {
        TouchImageView.this.compatPostOnAnimation(this);
      } else {
        TouchImageView.a(TouchImageView.this, TouchImageView.State.NONE);
      }
    }
    
    private void a(float paramFloat)
    {
      float f1 = this.i.x + paramFloat * (this.j.x - this.i.x);
      float f2 = this.i.y + paramFloat * (this.j.y - this.i.y);
      PointF localPointF = TouchImageView.a(TouchImageView.this, this.e, this.f);
      TouchImageView.m(TouchImageView.this).postTranslate(f1 - localPointF.x, f2 - localPointF.y);
    }
    
    private float a()
    {
      long l = System.currentTimeMillis();
      float f1 = (float)(l - this.b) / 500.0F;
      f1 = Math.min(1.0F, f1);
      return this.h.getInterpolation(f1);
    }
    
    private double b(float paramFloat)
    {
      double d1 = this.c + paramFloat * (this.d - this.c);
      return d1 / TouchImageView.d(TouchImageView.this);
    }
  }
  
  private class ScaleListener
    extends ScaleGestureDetector.SimpleOnScaleGestureListener
  {
    private ScaleListener() {}
    
    public boolean onScaleBegin(ScaleGestureDetector paramScaleGestureDetector)
    {
      TouchImageView.a(TouchImageView.this, TouchImageView.State.ZOOM);
      return true;
    }
    
    public boolean onScale(ScaleGestureDetector paramScaleGestureDetector)
    {
      TouchImageView.a(TouchImageView.this, paramScaleGestureDetector.getScaleFactor(), paramScaleGestureDetector.getFocusX(), paramScaleGestureDetector.getFocusY(), true);
      if (TouchImageView.p(TouchImageView.this) != null) {
        TouchImageView.p(TouchImageView.this).onMove();
      }
      return true;
    }
    
    public void onScaleEnd(ScaleGestureDetector paramScaleGestureDetector)
    {
      super.onScaleEnd(paramScaleGestureDetector);
      TouchImageView.a(TouchImageView.this, TouchImageView.State.NONE);
      int i = 0;
      float f = TouchImageView.d(TouchImageView.this);
      if (TouchImageView.d(TouchImageView.this) > TouchImageView.f(TouchImageView.this))
      {
        f = TouchImageView.f(TouchImageView.this);
        i = 1;
      }
      else if (TouchImageView.d(TouchImageView.this) < TouchImageView.e(TouchImageView.this))
      {
        f = TouchImageView.e(TouchImageView.this);
        i = 1;
      }
      if (i != 0)
      {
        TouchImageView.DoubleTapZoom localDoubleTapZoom = new TouchImageView.DoubleTapZoom(TouchImageView.this, f, TouchImageView.i(TouchImageView.this) / 2, TouchImageView.k(TouchImageView.this) / 2, true);
        TouchImageView.this.compatPostOnAnimation(localDoubleTapZoom);
      }
    }
  }
  
  private class PrivateOnTouchListener
    implements View.OnTouchListener
  {
    private PointF b = new PointF();
    
    private PrivateOnTouchListener() {}
    
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
    {
      TouchImageView.g(TouchImageView.this).onTouchEvent(paramMotionEvent);
      TouchImageView.h(TouchImageView.this).onTouchEvent(paramMotionEvent);
      PointF localPointF = new PointF(paramMotionEvent.getX(), paramMotionEvent.getY());
      if ((TouchImageView.c(TouchImageView.this) == TouchImageView.State.NONE) || (TouchImageView.c(TouchImageView.this) == TouchImageView.State.DRAG) || (TouchImageView.c(TouchImageView.this) == TouchImageView.State.FLING)) {
        switch (paramMotionEvent.getAction())
        {
        case 0: 
          this.b.set(localPointF);
          if (TouchImageView.b(TouchImageView.this) != null) {
            TouchImageView.b(TouchImageView.this).cancelFling();
          }
          TouchImageView.a(TouchImageView.this, TouchImageView.State.DRAG);
          break;
        case 2: 
          if (TouchImageView.c(TouchImageView.this) == TouchImageView.State.DRAG)
          {
            float f1 = localPointF.x - this.b.x;
            float f2 = localPointF.y - this.b.y;
            float f3 = TouchImageView.a(TouchImageView.this, f1, TouchImageView.i(TouchImageView.this), TouchImageView.j(TouchImageView.this));
            float f4 = TouchImageView.a(TouchImageView.this, f2, TouchImageView.k(TouchImageView.this), TouchImageView.l(TouchImageView.this));
            TouchImageView.m(TouchImageView.this).postTranslate(f3, f4);
            TouchImageView.n(TouchImageView.this);
            this.b.set(localPointF.x, localPointF.y);
          }
          break;
        case 1: 
        case 6: 
          TouchImageView.a(TouchImageView.this, TouchImageView.State.NONE);
        }
      }
      TouchImageView.this.setImageMatrix(TouchImageView.m(TouchImageView.this));
      if (TouchImageView.o(TouchImageView.this) != null) {
        TouchImageView.o(TouchImageView.this).onTouch(paramView, paramMotionEvent);
      }
      if (TouchImageView.p(TouchImageView.this) != null) {
        TouchImageView.p(TouchImageView.this).onMove();
      }
      return true;
    }
  }
  
  public static abstract interface OnTouchImageViewListener
  {
    public abstract void onMove();
  }
  
  private class GestureListener
    extends GestureDetector.SimpleOnGestureListener
  {
    private GestureListener() {}
    
    public boolean onSingleTapConfirmed(MotionEvent paramMotionEvent)
    {
      if (TouchImageView.a(TouchImageView.this) != null) {
        return TouchImageView.a(TouchImageView.this).onSingleTapConfirmed(paramMotionEvent);
      }
      return TouchImageView.this.performClick();
    }
    
    public void onLongPress(MotionEvent paramMotionEvent)
    {
      TouchImageView.this.performLongClick();
    }
    
    public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
    {
      if (TouchImageView.b(TouchImageView.this) != null) {
        TouchImageView.b(TouchImageView.this).cancelFling();
      }
      TouchImageView.a(TouchImageView.this, new TouchImageView.Fling(TouchImageView.this, (int)paramFloat1, (int)paramFloat2));
      TouchImageView.this.compatPostOnAnimation(TouchImageView.b(TouchImageView.this));
      return super.onFling(paramMotionEvent1, paramMotionEvent2, paramFloat1, paramFloat2);
    }
    
    public boolean onDoubleTap(MotionEvent paramMotionEvent)
    {
      boolean bool = false;
      if (TouchImageView.a(TouchImageView.this) != null) {
        bool = TouchImageView.a(TouchImageView.this).onDoubleTap(paramMotionEvent);
      }
      if (TouchImageView.c(TouchImageView.this) == TouchImageView.State.NONE)
      {
        float f = TouchImageView.d(TouchImageView.this) == TouchImageView.e(TouchImageView.this) ? TouchImageView.f(TouchImageView.this) : TouchImageView.e(TouchImageView.this);
        TouchImageView.DoubleTapZoom localDoubleTapZoom = new TouchImageView.DoubleTapZoom(TouchImageView.this, f, paramMotionEvent.getX(), paramMotionEvent.getY(), false);
        TouchImageView.this.compatPostOnAnimation(localDoubleTapZoom);
        bool = true;
      }
      return bool;
    }
    
    public boolean onDoubleTapEvent(MotionEvent paramMotionEvent)
    {
      if (TouchImageView.a(TouchImageView.this) != null) {
        return TouchImageView.a(TouchImageView.this).onDoubleTapEvent(paramMotionEvent);
      }
      return false;
    }
  }
  
  private static enum State
  {
    private State() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\TouchImageView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */