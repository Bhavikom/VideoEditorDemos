package org.lasque.tusdk.impl.components.widget.sticker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.ViewSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TuSdkGestureRecognizer;
import org.lasque.tusdk.core.utils.TuSdkGestureRecognizer.StepData;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerItemViewInterface;
import org.lasque.tusdk.modules.view.widget.sticker.StickerItemViewInterface.StickerItemViewDelegate;
import org.lasque.tusdk.modules.view.widget.sticker.StickerResult;

public abstract class StickerItemViewBase
  extends TuSdkRelativeLayout
  implements StickerItemViewInterface
{
  protected boolean mEnableExpand = true;
  protected StickerItemViewInterface.StickerItemViewDelegate mDelegate;
  private ViewTreeObserver.OnGlobalLayoutListener a;
  protected StickerData mSticker;
  protected TuSdkSize mCSize;
  protected Point mCMargin = new Point();
  protected Point mCOffset = new Point();
  protected Rect mParentFrame;
  protected float mMinScale = 0.5F;
  protected float mMaxScale;
  protected TuSdkSize mDefaultViewSize;
  protected float mCHypotenuse;
  private boolean b;
  protected StickerView.StickerType mStickerType;
  protected StickerView.StickerType mType;
  protected PointF mLastPoint = new PointF();
  protected PointF mTranslation = new PointF();
  protected float mScale = 1.0F;
  protected float mDegree;
  protected boolean mHasExceededMaxSize;
  private TuSdkGestureRecognizer c = new TuSdkGestureRecognizer()
  {
    public void onTouchBegin(TuSdkGestureRecognizer paramAnonymousTuSdkGestureRecognizer, View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
    {
      StickerItemViewBase.a(StickerItemViewBase.this, paramAnonymousMotionEvent);
    }
    
    public void onTouchSingleMove(TuSdkGestureRecognizer paramAnonymousTuSdkGestureRecognizer, View paramAnonymousView, MotionEvent paramAnonymousMotionEvent, TuSdkGestureRecognizer.StepData paramAnonymousStepData)
    {
      StickerItemViewBase.a(StickerItemViewBase.this, paramAnonymousTuSdkGestureRecognizer, paramAnonymousMotionEvent);
    }
    
    public void onTouchMultipleMoveForStablization(TuSdkGestureRecognizer paramAnonymousTuSdkGestureRecognizer, TuSdkGestureRecognizer.StepData paramAnonymousStepData)
    {
      StickerItemViewBase.a(StickerItemViewBase.this, paramAnonymousTuSdkGestureRecognizer, paramAnonymousStepData);
    }
    
    public void onTouchEnd(TuSdkGestureRecognizer paramAnonymousTuSdkGestureRecognizer, View paramAnonymousView, MotionEvent paramAnonymousMotionEvent, TuSdkGestureRecognizer.StepData paramAnonymousStepData)
    {
      StickerItemViewBase.b(StickerItemViewBase.this, paramAnonymousMotionEvent);
    }
  };
  @SuppressLint({"ClickableViewAccessibility"})
  protected View.OnTouchListener mOnTouchListener = new View.OnTouchListener()
  {
    public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
    {
      if (paramAnonymousMotionEvent.getPointerCount() > 1) {
        return false;
      }
      Rect localRect = TuSdkViewHelper.locationInWindow((View)StickerItemViewBase.this.getParent());
      float f = 0.0F;
      if (StickerItemViewBase.this.mParentFrame.top - localRect.top > 100) {
        f = StickerItemViewBase.this.mParentFrame.top - localRect.top;
      }
      switch (paramAnonymousMotionEvent.getAction())
      {
      case 0: 
        StickerItemViewBase.this.handleTurnAndScaleActionStart(null, paramAnonymousMotionEvent.getRawX(), paramAnonymousMotionEvent.getRawY() + f);
        break;
      case 2: 
        StickerItemViewBase.this.handleTurnAndScaleActionMove(null, paramAnonymousMotionEvent.getRawX(), paramAnonymousMotionEvent.getRawY() + f);
        break;
      }
      return true;
    }
  };
  
  public StickerItemViewBase(Context paramContext)
  {
    super(paramContext);
  }
  
  public StickerItemViewBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public StickerItemViewBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public StickerItemViewInterface.StickerItemViewDelegate getDelegate()
  {
    return this.mDelegate;
  }
  
  public void setDelegate(StickerItemViewInterface.StickerItemViewDelegate paramStickerItemViewDelegate)
  {
    this.mDelegate = paramStickerItemViewDelegate;
  }
  
  protected void initView()
  {
    ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
    this.a = new ViewTreeObserver.OnGlobalLayoutListener()
    {
      public void onGlobalLayout()
      {
        TuSdkViewHelper.removeGlobalLayoutListener(StickerItemViewBase.this.getViewTreeObserver(), StickerItemViewBase.a(StickerItemViewBase.this));
        if (!StickerItemViewBase.b(StickerItemViewBase.this))
        {
          StickerItemViewBase.a(StickerItemViewBase.this, true);
          StickerItemViewBase.c(StickerItemViewBase.this).setMultipleStablization(true);
          StickerItemViewBase.d(StickerItemViewBase.this);
        }
      }
    };
    localViewTreeObserver.addOnGlobalLayoutListener(this.a);
  }
  
  public void setStickerType(StickerView.StickerType paramStickerType)
  {
    this.mType = paramStickerType;
  }
  
  public void setStickerViewType(StickerView.StickerType paramStickerType)
  {
    this.mStickerType = paramStickerType;
  }
  
  public StickerView.StickerType getStickerType()
  {
    return this.mType;
  }
  
  public void setSticker(StickerData paramStickerData) {}
  
  public StickerData getStickerData()
  {
    return this.mSticker;
  }
  
  public float getMinScale()
  {
    if (this.mMinScale < 0.5F) {
      this.mMinScale = 0.5F;
    }
    return this.mMinScale;
  }
  
  public void setMinScale(float paramFloat)
  {
    this.mMinScale = paramFloat;
  }
  
  public void setParentFrame(Rect paramRect)
  {
    this.mParentFrame = paramRect;
  }
  
  protected void initStickerPostion()
  {
    if (this.mCSize == null) {
      return;
    }
    this.mCHypotenuse = RectHelper.getDistanceOfTwoPoints(0.0F, 0.0F, this.mCSize.width, this.mCSize.height);
    this.mDefaultViewSize = TuSdkSize.create(this.mCSize.width + this.mCMargin.x, this.mCSize.height + this.mCMargin.y);
    this.mMaxScale = Math.min((this.mParentFrame.width() - this.mCMargin.x) / this.mCSize.width, (this.mParentFrame.height() - this.mCMargin.y) / this.mCSize.height);
    if (this.mMaxScale < this.mMinScale) {
      this.mMaxScale = this.mMinScale;
    }
    setSize(this, this.mDefaultViewSize);
    if (this.mParentFrame == null) {
      return;
    }
    this.mTranslation.x = ((this.mParentFrame.width() - this.mDefaultViewSize.width) * 0.5F);
    this.mTranslation.y = ((this.mParentFrame.height() - this.mDefaultViewSize.height) * 0.5F);
    ViewCompat.setTranslationX(this, this.mTranslation.x);
    ViewCompat.setTranslationY(this, this.mTranslation.y);
    if (this.mSticker != null) {}
  }
  
  public StickerResult getResult(Rect paramRect)
  {
    StickerResult localStickerResult = new StickerResult();
    localStickerResult.item = this.mSticker.copy();
    localStickerResult.degree = this.mDegree;
    localStickerResult.center = getCenterPercent(paramRect);
    return localStickerResult;
  }
  
  protected RectF getCenterPercent(Rect paramRect)
  {
    PointF localPointF = getCenterOpposite();
    RectF localRectF = new RectF();
    if (paramRect != null)
    {
      localPointF.offset(-paramRect.left, -paramRect.top);
      localRectF.left = (localPointF.x / paramRect.width());
      localRectF.top = (localPointF.y / paramRect.height());
      localRectF.right = (localRectF.left + this.mCSize.width * this.mScale / paramRect.width());
      localRectF.bottom = (localRectF.top + this.mCSize.height * this.mScale / paramRect.height());
    }
    else
    {
      localRectF.left = (localPointF.x / this.mParentFrame.width());
      localRectF.top = (localPointF.y / this.mParentFrame.height());
      localRectF.right = (localRectF.left + this.mCSize.width * this.mScale / this.mParentFrame.width());
      localRectF.bottom = (localRectF.top + this.mCSize.height * this.mScale / this.mParentFrame.height());
    }
    return localRectF;
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    return this.c.onTouch(this, paramMotionEvent);
  }
  
  private void a(MotionEvent paramMotionEvent)
  {
    this.b = false;
    if (this.mDelegate != null) {
      this.mDelegate.onStickerItemViewSelected(this);
    }
  }
  
  private void b(MotionEvent paramMotionEvent)
  {
    if ((this.mDelegate != null) && (!this.b)) {
      this.mDelegate.onStickerItemViewReleased(this);
    }
  }
  
  private void a(TuSdkGestureRecognizer paramTuSdkGestureRecognizer, MotionEvent paramMotionEvent)
  {
    if ((Math.abs(paramTuSdkGestureRecognizer.getStepPoint().x) >= 2.0F) || (Math.abs(paramTuSdkGestureRecognizer.getStepPoint().y) >= 2.0F)) {
      this.b = true;
    }
    this.mTranslation.offset(paramTuSdkGestureRecognizer.getStepPoint().x, paramTuSdkGestureRecognizer.getStepPoint().y);
    PointF localPointF = a(this.mTranslation);
    ViewSize localViewSize = ViewSize.create(this);
    RectF localRectF = RectHelper.minEnclosingRectangle(localPointF, localViewSize, this.mDegree);
    a(this.mTranslation, localRectF);
    ViewCompat.setTranslationX(this, this.mTranslation.x);
    ViewCompat.setTranslationY(this, this.mTranslation.y);
    requestLayout();
  }
  
  private void a(PointF paramPointF, RectF paramRectF)
  {
    if ((this.mParentFrame == null) || (paramPointF == null) || (paramRectF == null)) {
      return;
    }
    RectF localRectF = new RectF(-paramRectF.width() * 0.5F, -paramRectF.height() * 0.5F, this.mParentFrame.width() + paramRectF.width() * 0.5F, this.mParentFrame.height() + paramRectF.height() * 0.5F);
    if (paramRectF.left < localRectF.left) {
      paramPointF.x = (localRectF.left + (paramRectF.width() - getWidth()) * 0.5F);
    }
    if (paramRectF.right > localRectF.right) {
      paramPointF.x = (localRectF.right - (paramRectF.width() + getWidth()) * 0.5F);
    }
    if (paramRectF.top < localRectF.top) {
      paramPointF.y = (localRectF.top + (paramRectF.height() - getHeight()) * 0.5F);
    }
    if (paramRectF.bottom > localRectF.bottom) {
      paramPointF.y = (localRectF.bottom - (paramRectF.height() + getHeight()) * 0.5F);
    }
  }
  
  private void a(TuSdkGestureRecognizer paramTuSdkGestureRecognizer, TuSdkGestureRecognizer.StepData paramStepData)
  {
    this.mDegree = ((360.0F + this.mDegree + paramStepData.stepDegree) % 360.0F);
    ViewCompat.setRotation(this, this.mDegree);
    computerScale(paramStepData.stepSpace, getCenterOpposite());
    requestLayout();
  }
  
  protected void handleTurnAndScaleActionStart(TuSdkImageButton paramTuSdkImageButton, float paramFloat1, float paramFloat2)
  {
    this.mLastPoint.set(paramFloat1, paramFloat2);
    if (this.mDelegate != null) {
      this.mDelegate.onStickerItemViewSelected(this);
    }
  }
  
  protected void handleTurnAndScaleActionMove(TuSdkImageButton paramTuSdkImageButton, float paramFloat1, float paramFloat2)
  {
    PointF localPointF1 = new PointF(paramFloat1, paramFloat2);
    PointF localPointF2 = getCenterOpposite();
    a(localPointF1, localPointF2);
    c(localPointF1, localPointF2);
    requestLayout();
    this.mLastPoint.set(localPointF1.x, localPointF1.y);
  }
  
  private void a(PointF paramPointF1, PointF paramPointF2)
  {
    float f1 = b(this.mLastPoint, paramPointF2);
    float f2 = b(paramPointF1, paramPointF2);
    this.mDegree = ((360.0F + this.mDegree + (f2 - f1)) % 360.0F);
    ViewCompat.setRotation(this, this.mDegree);
  }
  
  public void resetRotation()
  {
    ViewCompat.setRotation(this, 0.0F);
  }
  
  private float b(PointF paramPointF1, PointF paramPointF2)
  {
    PointF localPointF = new PointF(paramPointF1.x - this.mParentFrame.left, paramPointF1.y - this.mParentFrame.top);
    return RectHelper.computeAngle(localPointF, paramPointF2);
  }
  
  protected PointF getCenterOpposite()
  {
    return a(this.mTranslation);
  }
  
  private PointF a(PointF paramPointF)
  {
    PointF localPointF = new PointF();
    paramPointF.x += getWidth() * 0.5F;
    paramPointF.y += getHeight() * 0.5F;
    return localPointF;
  }
  
  private void c(PointF paramPointF1, PointF paramPointF2)
  {
    float f1 = RectHelper.getDistanceOfTwoPoints(paramPointF2, this.mLastPoint);
    float f2 = RectHelper.getDistanceOfTwoPoints(paramPointF2, paramPointF1);
    computerScale(f2 - f1, paramPointF2);
  }
  
  protected void computerScale(float paramFloat, PointF paramPointF)
  {
    if (paramFloat == 0.0F) {
      return;
    }
    float f = paramFloat / this.mCHypotenuse * 2.0F;
    if ((!this.mHasExceededMaxSize) || (f < 0.0F)) {
      this.mScale += f;
    }
    if (this.mScale < this.mMinScale) {
      this.mScale = this.mMinScale;
    } else if (this.mScale > this.mMaxScale) {
      this.mScale = this.mMaxScale;
    }
    TuSdkSize localTuSdkSize = getScaledSize();
    if (!this.mEnableExpand)
    {
      RectF localRectF = RectHelper.minEnclosingRectangle(paramPointF, localTuSdkSize, this.mDegree);
      this.mTranslation.offset((getWidth() - localTuSdkSize.width) * 0.5F, (getHeight() - localTuSdkSize.height) * 0.5F);
      a(this.mTranslation, localRectF);
      ViewCompat.setTranslationX(this, this.mTranslation.x);
      ViewCompat.setTranslationY(this, this.mTranslation.y);
    }
    setViewSize(this, localTuSdkSize.width, localTuSdkSize.height);
  }
  
  public Point getCMargin()
  {
    return this.mCMargin;
  }
  
  public Point getCOffset()
  {
    return this.mCOffset;
  }
  
  public TuSdkSize getScaledSize()
  {
    TuSdkSize localTuSdkSize = TuSdkSize.create((int)Math.ceil(this.mCSize.width * this.mScale + this.mCMargin.x), (int)Math.ceil(this.mCSize.height * this.mScale + this.mCMargin.y));
    return localTuSdkSize;
  }
  
  public void scaleSize(float paramFloat)
  {
    this.mScale = (this.mMaxScale * paramFloat < getMinScale() ? getMinScale() : this.mMaxScale * paramFloat);
    setViewSize(this, getScaledSize().width, getScaledSize().height);
    invalidate();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\sticker\StickerItemViewBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */