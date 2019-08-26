// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.sticker;

//import org.lasque.tusdk.core.struct.ViewSize;
import android.graphics.RectF;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerResult;
import android.support.v4.view.ViewCompat;
//import org.lasque.tusdk.core.utils.RectHelper;
import android.util.AttributeSet;
//import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.view.MotionEvent;
import android.content.Context;
import android.annotation.SuppressLint;
import android.view.View;
//import org.lasque.tusdk.core.utils.TuSdkGestureRecognizer;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Point;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import android.view.ViewTreeObserver;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.ViewSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkGestureRecognizer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerItemViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerResult;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerItemViewInterface;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class StickerItemViewBase extends TuSdkRelativeLayout implements StickerItemViewInterface
{
    protected boolean mEnableExpand;
    protected StickerItemViewDelegate mDelegate;
    private ViewTreeObserver.OnGlobalLayoutListener a;
    protected StickerData mSticker;
    protected TuSdkSize mCSize;
    protected Point mCMargin;
    protected Point mCOffset;
    protected Rect mParentFrame;
    protected float mMinScale;
    protected float mMaxScale;
    protected TuSdkSize mDefaultViewSize;
    protected float mCHypotenuse;
    private boolean b;
    protected StickerView.StickerType mStickerType;
    protected StickerView.StickerType mType;
    protected PointF mLastPoint;
    protected PointF mTranslation;
    protected float mScale;
    protected float mDegree;
    protected boolean mHasExceededMaxSize;
    private TuSdkGestureRecognizer c;
    @SuppressLint({ "ClickableViewAccessibility" })
    protected View.OnTouchListener mOnTouchListener;
    
    public StickerItemViewBase(final Context context) {
        super(context);
        this.mEnableExpand = true;
        this.mCMargin = new Point();
        this.mCOffset = new Point();
        this.mMinScale = 0.5f;
        this.mLastPoint = new PointF();
        this.mTranslation = new PointF();
        this.mScale = 1.0f;
        this.c = new TuSdkGestureRecognizer() {
            @Override
            public void onTouchBegin(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent) {
                StickerItemViewBase.this.a(motionEvent);
            }
            
            @Override
            public void onTouchSingleMove(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent, final StepData stepData) {
                StickerItemViewBase.this.a(tuSdkGestureRecognizer, motionEvent);
            }
            
            @Override
            public void onTouchMultipleMoveForStablization(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final StepData stepData) {
                StickerItemViewBase.this.a(tuSdkGestureRecognizer, stepData);
            }
            
            @Override
            public void onTouchEnd(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent, final StepData stepData) {
                StickerItemViewBase.this.b(motionEvent);
            }
        };
        this.mOnTouchListener = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getPointerCount() > 1) {
                    return false;
                }
                final Rect locationInWindow = TuSdkViewHelper.locationInWindow((View)StickerItemViewBase.this.getParent());
                float n = 0.0f;
                if (StickerItemViewBase.this.mParentFrame.top - locationInWindow.top > 100) {
                    n = (float)(StickerItemViewBase.this.mParentFrame.top - locationInWindow.top);
                }
                switch (motionEvent.getAction()) {
                    case 0: {
                        StickerItemViewBase.this.handleTurnAndScaleActionStart(null, motionEvent.getRawX(), motionEvent.getRawY() + n);
                        break;
                    }
                    case 2: {
                        StickerItemViewBase.this.handleTurnAndScaleActionMove(null, motionEvent.getRawX(), motionEvent.getRawY() + n);
                        break;
                    }
                }
                return true;
            }
        };
    }
    
    public StickerItemViewBase(final Context context, final AttributeSet set) {
        super(context, set);
        this.mEnableExpand = true;
        this.mCMargin = new Point();
        this.mCOffset = new Point();
        this.mMinScale = 0.5f;
        this.mLastPoint = new PointF();
        this.mTranslation = new PointF();
        this.mScale = 1.0f;
        this.c = new TuSdkGestureRecognizer() {
            @Override
            public void onTouchBegin(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent) {
                StickerItemViewBase.this.a(motionEvent);
            }
            
            @Override
            public void onTouchSingleMove(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent, final StepData stepData) {
                StickerItemViewBase.this.a(tuSdkGestureRecognizer, motionEvent);
            }
            
            @Override
            public void onTouchMultipleMoveForStablization(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final StepData stepData) {
                StickerItemViewBase.this.a(tuSdkGestureRecognizer, stepData);
            }
            
            @Override
            public void onTouchEnd(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent, final StepData stepData) {
                StickerItemViewBase.this.b(motionEvent);
            }
        };
        this.mOnTouchListener = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getPointerCount() > 1) {
                    return false;
                }
                final Rect locationInWindow = TuSdkViewHelper.locationInWindow((View)StickerItemViewBase.this.getParent());
                float n = 0.0f;
                if (StickerItemViewBase.this.mParentFrame.top - locationInWindow.top > 100) {
                    n = (float)(StickerItemViewBase.this.mParentFrame.top - locationInWindow.top);
                }
                switch (motionEvent.getAction()) {
                    case 0: {
                        StickerItemViewBase.this.handleTurnAndScaleActionStart(null, motionEvent.getRawX(), motionEvent.getRawY() + n);
                        break;
                    }
                    case 2: {
                        StickerItemViewBase.this.handleTurnAndScaleActionMove(null, motionEvent.getRawX(), motionEvent.getRawY() + n);
                        break;
                    }
                }
                return true;
            }
        };
    }
    
    public StickerItemViewBase(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mEnableExpand = true;
        this.mCMargin = new Point();
        this.mCOffset = new Point();
        this.mMinScale = 0.5f;
        this.mLastPoint = new PointF();
        this.mTranslation = new PointF();
        this.mScale = 1.0f;
        this.c = new TuSdkGestureRecognizer() {
            @Override
            public void onTouchBegin(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent) {
                StickerItemViewBase.this.a(motionEvent);
            }
            
            @Override
            public void onTouchSingleMove(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent, final StepData stepData) {
                StickerItemViewBase.this.a(tuSdkGestureRecognizer, motionEvent);
            }
            
            @Override
            public void onTouchMultipleMoveForStablization(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final StepData stepData) {
                StickerItemViewBase.this.a(tuSdkGestureRecognizer, stepData);
            }
            
            @Override
            public void onTouchEnd(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent, final StepData stepData) {
                StickerItemViewBase.this.b(motionEvent);
            }
        };
        this.mOnTouchListener = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getPointerCount() > 1) {
                    return false;
                }
                final Rect locationInWindow = TuSdkViewHelper.locationInWindow((View)StickerItemViewBase.this.getParent());
                float n = 0.0f;
                if (StickerItemViewBase.this.mParentFrame.top - locationInWindow.top > 100) {
                    n = (float)(StickerItemViewBase.this.mParentFrame.top - locationInWindow.top);
                }
                switch (motionEvent.getAction()) {
                    case 0: {
                        StickerItemViewBase.this.handleTurnAndScaleActionStart(null, motionEvent.getRawX(), motionEvent.getRawY() + n);
                        break;
                    }
                    case 2: {
                        StickerItemViewBase.this.handleTurnAndScaleActionMove(null, motionEvent.getRawX(), motionEvent.getRawY() + n);
                        break;
                    }
                }
                return true;
            }
        };
    }
    
    public StickerItemViewDelegate getDelegate() {
        return this.mDelegate;
    }
    
    @Override
    public void setDelegate(final StickerItemViewDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }
    
    @Override
    protected void initView() {
        this.getViewTreeObserver().addOnGlobalLayoutListener(this.a = (ViewTreeObserver.OnGlobalLayoutListener)new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                TuSdkViewHelper.removeGlobalLayoutListener(StickerItemViewBase.this.getViewTreeObserver(), StickerItemViewBase.this.a);
                if (!StickerItemViewBase.this.isLayouted) {
                    StickerItemViewBase.this.isLayouted = true;
                    StickerItemViewBase.this.c.setMultipleStablization(true);
                    onLayouted();
                }
            }
        });
    }
    
    @Override
    public void setStickerType(final StickerView.StickerType mType) {
        this.mType = mType;
    }
    
    @Override
    public void setStickerViewType(final StickerView.StickerType mStickerType) {
        this.mStickerType = mStickerType;
    }
    
    @Override
    public StickerView.StickerType getStickerType() {
        return this.mType;
    }
    
    @Override
    public void setSticker(final StickerData stickerData) {
    }
    
    @Override
    public StickerData getStickerData() {
        return this.mSticker;
    }
    
    public float getMinScale() {
        if (this.mMinScale < 0.5f) {
            this.mMinScale = 0.5f;
        }
        return this.mMinScale;
    }
    
    public void setMinScale(final float mMinScale) {
        this.mMinScale = mMinScale;
    }
    
    @Override
    public void setParentFrame(final Rect mParentFrame) {
        this.mParentFrame = mParentFrame;
    }
    
    protected void initStickerPostion() {
        if (this.mCSize == null) {
            return;
        }
        this.mCHypotenuse = RectHelper.getDistanceOfTwoPoints(0.0f, 0.0f, (float)this.mCSize.width, (float)this.mCSize.height);
        this.mDefaultViewSize = TuSdkSize.create(this.mCSize.width + this.mCMargin.x, this.mCSize.height + this.mCMargin.y);
        this.mMaxScale = Math.min((this.mParentFrame.width() - this.mCMargin.x) / (float)this.mCSize.width, (this.mParentFrame.height() - this.mCMargin.y) / (float)this.mCSize.height);
        if (this.mMaxScale < this.mMinScale) {
            this.mMaxScale = this.mMinScale;
        }
        this.setSize((View)this, this.mDefaultViewSize);
        if (this.mParentFrame == null) {
            return;
        }
        this.mTranslation.x = (this.mParentFrame.width() - this.mDefaultViewSize.width) * 0.5f;
        this.mTranslation.y = (this.mParentFrame.height() - this.mDefaultViewSize.height) * 0.5f;
        ViewCompat.setTranslationX((View)this, this.mTranslation.x);
        ViewCompat.setTranslationY((View)this, this.mTranslation.y);
        if (this.mSticker != null) {}
    }
    
    @Override
    public StickerResult getResult(final Rect rect) {
        final StickerResult stickerResult = new StickerResult();
        stickerResult.item = this.mSticker.copy();
        stickerResult.degree = this.mDegree;
        stickerResult.center = this.getCenterPercent(rect);
        return stickerResult;
    }
    
    protected RectF getCenterPercent(final Rect rect) {
        final PointF centerOpposite = this.getCenterOpposite();
        final RectF rectF = new RectF();
        if (rect != null) {
            centerOpposite.offset((float)(-rect.left), (float)(-rect.top));
            rectF.left = centerOpposite.x / rect.width();
            rectF.top = centerOpposite.y / rect.height();
            rectF.right = rectF.left + this.mCSize.width * this.mScale / rect.width();
            rectF.bottom = rectF.top + this.mCSize.height * this.mScale / rect.height();
        }
        else {
            rectF.left = centerOpposite.x / this.mParentFrame.width();
            rectF.top = centerOpposite.y / this.mParentFrame.height();
            rectF.right = rectF.left + this.mCSize.width * this.mScale / this.mParentFrame.width();
            rectF.bottom = rectF.top + this.mCSize.height * this.mScale / this.mParentFrame.height();
        }
        return rectF;
    }
    
    @SuppressLint({ "ClickableViewAccessibility" })
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return this.c.onTouch((View)this, motionEvent);
    }
    
    private void a(final MotionEvent motionEvent) {
        this.b = false;
        if (this.mDelegate != null) {
            this.mDelegate.onStickerItemViewSelected(this);
        }
    }
    
    private void b(final MotionEvent motionEvent) {
        if (this.mDelegate != null && !this.b) {
            this.mDelegate.onStickerItemViewReleased(this);
        }
    }
    
    private void a(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final MotionEvent motionEvent) {
        if (Math.abs(tuSdkGestureRecognizer.getStepPoint().x) >= 2.0f || Math.abs(tuSdkGestureRecognizer.getStepPoint().y) >= 2.0f) {
            this.b = true;
        }
        this.mTranslation.offset(tuSdkGestureRecognizer.getStepPoint().x, tuSdkGestureRecognizer.getStepPoint().y);
        this.a(this.mTranslation, RectHelper.minEnclosingRectangle(this.a(this.mTranslation), ViewSize.create((View)this), this.mDegree));
        ViewCompat.setTranslationX((View)this, this.mTranslation.x);
        ViewCompat.setTranslationY((View)this, this.mTranslation.y);
        this.requestLayout();
    }
    
    private void a(final PointF pointF, final RectF rectF) {
        if (this.mParentFrame == null || pointF == null || rectF == null) {
            return;
        }
        final RectF rectF2 = new RectF(-rectF.width() * 0.5f, -rectF.height() * 0.5f, this.mParentFrame.width() + rectF.width() * 0.5f, this.mParentFrame.height() + rectF.height() * 0.5f);
        if (rectF.left < rectF2.left) {
            pointF.x = rectF2.left + (rectF.width() - this.getWidth()) * 0.5f;
        }
        if (rectF.right > rectF2.right) {
            pointF.x = rectF2.right - (rectF.width() + this.getWidth()) * 0.5f;
        }
        if (rectF.top < rectF2.top) {
            pointF.y = rectF2.top + (rectF.height() - this.getHeight()) * 0.5f;
        }
        if (rectF.bottom > rectF2.bottom) {
            pointF.y = rectF2.bottom - (rectF.height() + this.getHeight()) * 0.5f;
        }
    }
    
    private void a(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final TuSdkGestureRecognizer.StepData stepData) {
        ViewCompat.setRotation((View)this, this.mDegree = (360.0f + this.mDegree + stepData.stepDegree) % 360.0f);
        this.computerScale(stepData.stepSpace, this.getCenterOpposite());
        this.requestLayout();
    }
    
    protected void handleTurnAndScaleActionStart(final TuSdkImageButton tuSdkImageButton, final float n, final float n2) {
        this.mLastPoint.set(n, n2);
        if (this.mDelegate != null) {
            this.mDelegate.onStickerItemViewSelected(this);
        }
    }
    
    protected void handleTurnAndScaleActionMove(final TuSdkImageButton tuSdkImageButton, final float n, final float n2) {
        final PointF pointF = new PointF(n, n2);
        final PointF centerOpposite = this.getCenterOpposite();
        this.a(pointF, centerOpposite);
        this.c(pointF, centerOpposite);
        this.requestLayout();
        this.mLastPoint.set(pointF.x, pointF.y);
    }
    
    private void a(final PointF pointF, final PointF pointF2) {
        ViewCompat.setRotation((View)this, this.mDegree = (360.0f + this.mDegree + (this.b(pointF, pointF2) - this.b(this.mLastPoint, pointF2))) % 360.0f);
    }
    
    public void resetRotation() {
        ViewCompat.setRotation((View)this, 0.0f);
    }
    
    private float b(final PointF pointF, final PointF pointF2) {
        return RectHelper.computeAngle(new PointF(pointF.x - this.mParentFrame.left, pointF.y - this.mParentFrame.top), pointF2);
    }
    
    protected PointF getCenterOpposite() {
        return this.a(this.mTranslation);
    }
    
    private PointF a(final PointF pointF) {
        final PointF pointF2 = new PointF();
        pointF2.x = pointF.x + this.getWidth() * 0.5f;
        pointF2.y = pointF.y + this.getHeight() * 0.5f;
        return pointF2;
    }
    
    private void c(final PointF pointF, final PointF pointF2) {
        this.computerScale(RectHelper.getDistanceOfTwoPoints(pointF2, pointF) - RectHelper.getDistanceOfTwoPoints(pointF2, this.mLastPoint), pointF2);
    }
    
    protected void computerScale(final float n, final PointF pointF) {
        if (n == 0.0f) {
            return;
        }
        final float n2 = n / this.mCHypotenuse * 2.0f;
        if (!this.mHasExceededMaxSize || n2 < 0.0f) {
            this.mScale += n2;
        }
        if (this.mScale < this.mMinScale) {
            this.mScale = this.mMinScale;
        }
        else if (this.mScale > this.mMaxScale) {
            this.mScale = this.mMaxScale;
        }
        final TuSdkSize scaledSize = this.getScaledSize();
        if (!this.mEnableExpand) {
            final RectF minEnclosingRectangle = RectHelper.minEnclosingRectangle(pointF, scaledSize, this.mDegree);
            this.mTranslation.offset((this.getWidth() - scaledSize.width) * 0.5f, (this.getHeight() - scaledSize.height) * 0.5f);
            this.a(this.mTranslation, minEnclosingRectangle);
            ViewCompat.setTranslationX((View)this, this.mTranslation.x);
            ViewCompat.setTranslationY((View)this, this.mTranslation.y);
        }
        this.setViewSize((View)this, scaledSize.width, scaledSize.height);
    }
    
    public Point getCMargin() {
        return this.mCMargin;
    }
    
    public Point getCOffset() {
        return this.mCOffset;
    }
    
    public TuSdkSize getScaledSize() {
        return TuSdkSize.create((int)Math.ceil(this.mCSize.width * this.mScale + this.mCMargin.x), (int)Math.ceil(this.mCSize.height * this.mScale + this.mCMargin.y));
    }
    
    public void scaleSize(final float n) {
        this.mScale = ((this.mMaxScale * n < this.getMinScale()) ? this.getMinScale() : (this.mMaxScale * n));
        this.setViewSize((View)this, this.getScaledSize().width, this.getScaledSize().height);
        this.invalidate();
    }
}
