package org.lasque.tusdk.impl.components.widget.sticker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.FontUtils;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.view.TuSdkTextView;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData.StickerType;
import org.lasque.tusdk.modules.view.widget.sticker.StickerItemViewInterface.StickerItemViewDelegate;
import org.lasque.tusdk.modules.view.widget.sticker.StickerResult;
import org.lasque.tusdk.modules.view.widget.sticker.StickerText;

public final class StickerTextItemView
  extends StickerItemViewBase
{
  private TuSdkTextView a;
  private TuSdkImageButton b;
  private TuSdkImageButton c;
  private TuSdkImageButton d;
  private float e = 1.0F;
  protected View.OnTouchListener mOnResizeButtonTouchListener = new View.OnTouchListener()
  {
    public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
    {
      if (paramAnonymousMotionEvent.getPointerCount() > 1) {
        return false;
      }
      switch (paramAnonymousMotionEvent.getAction())
      {
      case 0: 
        StickerTextItemView.this.handleTurnAndScaleActionStart(null, paramAnonymousMotionEvent.getRawX(), paramAnonymousMotionEvent.getRawY());
        break;
      case 2: 
        StickerTextItemView.this.handleResizeActionMove(null, paramAnonymousMotionEvent);
        break;
      }
      return true;
    }
  };
  private int f;
  private boolean g;
  private View.OnClickListener h = new TuSdkViewHelper.OnSafeClickListener()
  {
    public void onSafeClick(View paramAnonymousView)
    {
      if (StickerTextItemView.this.equalViewIds(paramAnonymousView, StickerTextItemView.this.getCancelButton())) {
        StickerTextItemView.a(StickerTextItemView.this);
      }
    }
  };
  private int i;
  private int j;
  private float k;
  private int l;
  private boolean m;
  private String n;
  private String o;
  private String p;
  
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_sticker_text_item_view");
  }
  
  public StickerTextItemView(Context paramContext)
  {
    super(paramContext);
  }
  
  public StickerTextItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public StickerTextItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (this.mEnableExpand)
    {
      int i1 = (int)getTextView().getPaint().measureText(getTextView().getText().toString()) + 100 + this.l * 2;
      int i2 = View.MeasureSpec.makeMeasureSpec(getTextView().getWidth() > i1 ? getTextView().getWidth() : i1, Integer.MIN_VALUE);
      int i3 = View.MeasureSpec.getSize(paramInt2) + 20;
      int i4 = View.MeasureSpec.makeMeasureSpec(i3, View.MeasureSpec.getMode(paramInt2));
      super.onMeasure(i2, i4);
    }
    else
    {
      super.onMeasure(paramInt1, paramInt2);
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.mStickerType != StickerView.StickerType.Normal) && (this.mStickerType != this.mType)) {
      return false;
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public TuSdkTextView getTextView()
  {
    if (this.a == null)
    {
      this.a = ((TuSdkTextView)getViewById("lsq_sticker_textView"));
      this.a.getPaint().setAntiAlias(true);
      this.a.getPaint().setDither(true);
    }
    return this.a;
  }
  
  public final TuSdkImageButton getCancelButton()
  {
    if (this.b == null)
    {
      this.b = ((TuSdkImageButton)getViewById("lsq_sticker_cancelButton"));
      if (this.b != null) {
        this.b.setOnClickListener(this.h);
      }
    }
    return this.b;
  }
  
  public final TuSdkImageButton getResizeButton()
  {
    if (this.c == null)
    {
      this.c = ((TuSdkImageButton)getViewById("lsq_sticker_resizeButton"));
      if (this.c != null) {
        this.c.setOnTouchListener(this.mOnResizeButtonTouchListener);
      }
    }
    return this.c;
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  public final TuSdkImageButton getTurnButton()
  {
    if (this.d == null)
    {
      this.d = ((TuSdkImageButton)getViewById("lsq_sticker_turnButton"));
      if (this.d != null) {
        this.d.setOnTouchListener(this.mOnTouchListener);
      }
    }
    return this.d;
  }
  
  protected PointF getCenterOpposite()
  {
    PointF localPointF1 = super.getCenterOpposite();
    PointF localPointF2 = new PointF(localPointF1.x, localPointF1.y - TuSdkContext.dip2px(32.0F) * 0.5F);
    float f1 = (float)(RectHelper.computerPotintDistance(localPointF2, localPointF1) * Math.sin(this.mDegree * 3.141592653589793D / 180.0D));
    float f2 = (float)(RectHelper.computerPotintDistance(localPointF2, localPointF1) * Math.cos(this.mDegree * 3.141592653589793D / 180.0D));
    localPointF1.offset(f1, -f2);
    return localPointF1;
  }
  
  protected void handleTurnAndScaleActionStart(TuSdkImageButton paramTuSdkImageButton, float paramFloat1, float paramFloat2)
  {
    super.handleTurnAndScaleActionStart(paramTuSdkImageButton, paramFloat1, paramFloat2);
    this.f = getTextView().getWidth();
  }
  
  protected void handleResizeActionMove(TuSdkImageButton paramTuSdkImageButton, MotionEvent paramMotionEvent)
  {
    PointF localPointF1 = new PointF(paramMotionEvent.getRawX(), paramMotionEvent.getRawY());
    PointF localPointF2 = getCenterOpposite();
    a(localPointF1, localPointF2);
    requestLayout();
  }
  
  private int a(String paramString)
  {
    int i1 = 0;
    for (char c1 : paramString.toCharArray())
    {
      Rect localRect = FontUtils.getTextBoundsExcludeFontPadding(String.valueOf(c1), getTextView().getTextSize());
      if (localRect.width() > i1) {
        i1 = localRect.width();
      }
    }
    return i1;
  }
  
  private void a(PointF paramPointF1, PointF paramPointF2)
  {
    float f1 = (float)((paramPointF1.x - this.mLastPoint.x) / Math.cos(this.mDegree * 3.141592653589793D / 180.0D));
    int i1 = a(getTextView().getText().toString()) + this.l * 2;
    if (this.f + f1 < i1) {
      return;
    }
    if ((this.g) && (f1 < 0.0F)) {
      return;
    }
    StaticLayout localStaticLayout = new StaticLayout(getTextView().getText(), getTextView().getPaint(), getTextView().getWidth() - this.l * 2, Layout.Alignment.ALIGN_CENTER, this.e, 0.0F, false);
    int i2 = localStaticLayout.getHeight() + this.mCMargin.y;
    int i3 = this.f + this.mCMargin.x + (int)f1 - this.l * 2;
    if (i2 > this.mParentFrame.height())
    {
      this.g = true;
      i2 = this.mCSize.height + this.mCMargin.y;
      i3 = this.mCSize.width + this.mCMargin.x;
      setViewSize(this, i3, i2);
      return;
    }
    this.g = false;
    setViewSize(this, i3, i2);
    this.mSticker.width = TuSdkContext.px2dip(getTextView().getWidth() - this.l * 2);
    this.mSticker.height = TuSdkContext.px2dip(localStaticLayout.getHeight());
    this.mCSize = this.mSticker.sizePixies();
    this.mMaxScale /= this.mScale;
    this.mMinScale /= this.mScale;
    this.mScale = 1.0F;
    this.k = TuSdkContext.px2sp(getTextView().getTextSize());
    this.mCHypotenuse = RectHelper.getDistanceOfTwoPoints(0.0F, 0.0F, this.mCSize.width, this.mCSize.height);
  }
  
  public void loadView()
  {
    getTextView();
    getCancelButton();
    getTurnButton();
    getResizeButton();
  }
  
  private void a()
  {
    if (this.mDelegate == null) {
      return;
    }
    this.mDelegate.onStickerItemViewClose(this);
    if (this.mSticker != null) {
      this.mSticker.setImage(null);
    }
  }
  
  public void setStroke(int paramInt1, int paramInt2)
  {
    this.j = paramInt1;
    this.i = (paramInt2 > 0 ? paramInt2 : 0);
    if (getTextView() != null) {
      getTextView().setStroke(this.j, this.i);
    }
  }
  
  protected void onLayouted()
  {
    super.onLayouted();
    if (getTextView() == null) {
      return;
    }
    this.mCMargin.x = (getWidth() - getTextView().getWidth() + this.l * 2);
    this.mCMargin.y = (getHeight() - getTextView().getHeight() + this.l * 2);
    Rect localRect1 = TuSdkViewHelper.locationInWindow(this);
    Rect localRect2 = TuSdkViewHelper.locationInWindow(getTextView());
    this.mCOffset.x = (localRect2.left - localRect1.left);
    this.mCOffset.y = (localRect2.top - localRect1.top);
    initStickerPostion();
  }
  
  public void setSticker(StickerData paramStickerData)
  {
    if (paramStickerData == null) {
      return;
    }
    showViewIn(getTurnButton(), paramStickerData.getType() == StickerData.StickerType.TypeText);
    showViewIn(this, false);
    getTextView().post(new Runnable()
    {
      public void run()
      {
        StickerTextItemView.this.showViewIn(StickerTextItemView.this, true);
      }
    });
    a(paramStickerData);
    this.mSticker = paramStickerData;
    this.mCSize = this.mSticker.sizePixies();
    if (this.isLayouted) {
      initStickerPostion();
    }
  }
  
  private void a(StickerData paramStickerData)
  {
    if ((paramStickerData.texts == null) || (paramStickerData.texts.size() == 0) || (getTextView() == null)) {
      return;
    }
    StickerText localStickerText = (StickerText)paramStickerData.texts.get(0);
    getTextView().setText(localStickerText.content);
    getTextView().setTextSize(2, localStickerText.textSize);
    getTextView().setTextColor(Color.parseColor(localStickerText.color));
    this.l = TuSdkContext.dip2px(localStickerText.paddings);
    getTextView().setPadding(this.l, this.l, this.l, this.l);
    getTextView().setGravity(17);
    localStickerText.alignment = 1;
    this.k = localStickerText.textSize;
    this.n = localStickerText.color;
    getTextView().measure(0, 0);
    paramStickerData.width = TuSdkContext.px2dip(getTextView().getMeasuredWidth() - this.l * 2);
    paramStickerData.height = TuSdkContext.px2dip(getTextView().getMeasuredHeight() - this.l * 2);
    paramStickerData.width += 10;
  }
  
  public StickerResult getResult(Rect paramRect)
  {
    StickerResult localStickerResult = super.getResult(paramRect);
    ArrayList localArrayList = new ArrayList();
    Object localObject;
    for (int i1 = 0; i1 < getTextView().getLineCount(); i1++)
    {
      int i2 = getTextView().getLayout().getLineStart(i1);
      int i3 = getTextView().getLayout().getLineEnd(i1);
      localObject = getTextView().getText().subSequence(i2, i3).toString();
      localArrayList.add(localObject);
    }
    localStickerResult.text = localArrayList;
    StaticLayout localStaticLayout = new StaticLayout(getTextView().getText(), getTextView().getPaint(), getTextView().getWidth() - this.l * 2, Layout.Alignment.ALIGN_CENTER, this.e, 0.0F, false);
    float f1 = (getTextView().getHeight() - localStaticLayout.getHeight() - this.l * 2) * 1.0F / (2.0F * getTextView().getHeight());
    Iterator localIterator = localStickerResult.item.texts.iterator();
    while (localIterator.hasNext())
    {
      localObject = (StickerText)localIterator.next();
      ((StickerText)localObject).color = this.n;
      ((StickerText)localObject).backgroundColor = this.o;
      ((StickerText)localObject).shadowColor = this.p;
      ((StickerText)localObject).textSize = this.k;
      ((StickerText)localObject).rectLeft = 0.0F;
      ((StickerText)localObject).rectTop = Math.max(f1, 0.0F);
      ((StickerText)localObject).rectWidth = 1.0F;
      ((StickerText)localObject).rectHeight = Math.min((localStaticLayout.getHeight() + this.l * 2) * 1.0F / getTextView().getHeight(), 1.0F);
    }
    return localStickerResult;
  }
  
  public void updateText(String paramString, boolean paramBoolean)
  {
    if (paramString == null) {
      return;
    }
    this.m = paramBoolean;
    paramString = paramString.trim().replaceAll("\\s+\\n", "\n");
    getTextView().setText(paramString);
    int i1 = this.mEnableExpand ? TuSdkContext.getScreenSize().height : getTextView().getWidth() - this.l * 2;
    StaticLayout localStaticLayout = new StaticLayout(getTextView().getText(), getTextView().getPaint(), i1, Layout.Alignment.ALIGN_CENTER, this.e, 0.0F, false);
    int i2 = localStaticLayout.getHeight();
    int i3 = i2 + this.mCMargin.y;
    setViewSize(this, localStaticLayout.getWidth() + this.mCMargin.x, i3);
    this.mSticker.width = TuSdkContext.px2dip(this.mEnableExpand ? getTextView().getWidth() : localStaticLayout.getWidth() - this.l * 2);
    this.mSticker.height = TuSdkContext.px2dip(localStaticLayout.getHeight());
    this.mCSize = this.mSticker.sizePixies();
    this.mMaxScale /= this.mScale;
    this.mMinScale /= this.mScale;
    this.mScale = 1.0F;
    this.k = TuSdkContext.px2sp(getTextView().getTextSize());
    this.mCHypotenuse = RectHelper.getDistanceOfTwoPoints(0.0F, 0.0F, this.mCSize.width, this.mCSize.height);
  }
  
  public void onSelectedColorChanged(int paramInt, String paramString)
  {
    if (paramInt == 0) {
      this.n = paramString;
    } else if (paramInt == 1) {
      this.o = paramString;
    } else if (paramInt == 2) {
      this.p = paramString;
    }
    onSelectedColorChanged(paramInt, Color.parseColor(paramString));
  }
  
  public void onSelectedColorChanged(int paramInt1, int paramInt2)
  {
    if (paramInt1 == 0) {
      getTextView().setTextColor(paramInt2);
    } else if (paramInt1 == 1) {
      getTextView().setBackgroundColor(paramInt2);
    } else if (paramInt1 == 2) {
      getTextView().setShadowLayer(2.0F, 3.0F, 2.0F, paramInt2);
    }
  }
  
  public void toggleTextUnderlineStyle()
  {
    Iterator localIterator = this.mSticker.texts.iterator();
    while (localIterator.hasNext())
    {
      StickerText localStickerText = (StickerText)localIterator.next();
      localStickerText.underline = (!localStickerText.underline);
      getTextView().setUnderlineText(localStickerText.underline);
      getTextView().invalidate();
    }
  }
  
  public void setUnderline(boolean paramBoolean)
  {
    Iterator localIterator = this.mSticker.texts.iterator();
    while (localIterator.hasNext())
    {
      StickerText localStickerText = (StickerText)localIterator.next();
      localStickerText.underline = paramBoolean;
      getTextView().setUnderlineText(paramBoolean);
      getTextView().invalidate();
    }
  }
  
  protected void toggleTextReverse()
  {
    getTextView().setText(b(getTextView().getText().toString()));
  }
  
  public boolean isNeedReverse()
  {
    return this.m;
  }
  
  private String b(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    String[] arrayOfString = paramString.split("\n");
    for (int i1 = 0; i1 < arrayOfString.length; i1++)
    {
      localStringBuilder.append(new StringBuilder(arrayOfString[i1]).reverse());
      if (i1 < arrayOfString.length - 1) {
        localStringBuilder.append("\n");
      }
    }
    return localStringBuilder.toString();
  }
  
  public void changeTextAlignment(int paramInt)
  {
    if (getTextView() == null) {
      return;
    }
    getTextView().setGravity(paramInt);
    Iterator localIterator = this.mSticker.texts.iterator();
    while (localIterator.hasNext())
    {
      StickerText localStickerText = (StickerText)localIterator.next();
      localStickerText.alignment = a(paramInt);
    }
  }
  
  private int a(int paramInt)
  {
    switch (paramInt)
    {
    case 17: 
      return 1;
    case 8388613: 
      return 2;
    }
    return 0;
  }
  
  public void setSelected(boolean paramBoolean)
  {
    if (getTextView() != null)
    {
      int i1 = paramBoolean ? this.j : 0;
      getTextView().setStroke(i1, this.i);
    }
    showViewIn(getCancelButton(), paramBoolean);
    showViewIn(getTurnButton(), paramBoolean);
    showViewIn(getResizeButton(), paramBoolean);
  }
  
  public TuSdkSize getScaledSize()
  {
    TuSdkSize localTuSdkSize = super.getScaledSize();
    if (this.mEnableExpand)
    {
      this.mHasExceededMaxSize = false;
      if (getTextView() != null) {
        getTextView().setTextSize(2, this.k * this.mScale);
      }
    }
    else
    {
      if ((localTuSdkSize.width > this.mParentFrame.width()) || (localTuSdkSize.height > this.mParentFrame.height()))
      {
        this.mHasExceededMaxSize = true;
        localTuSdkSize.width = Math.min(getWidth(), this.mParentFrame.width());
        localTuSdkSize.height = Math.min(getHeight(), this.mParentFrame.height());
        return localTuSdkSize;
      }
      if ((localTuSdkSize.width < this.mParentFrame.width()) && (localTuSdkSize.height < this.mParentFrame.height()))
      {
        this.mHasExceededMaxSize = false;
        if (getTextView() != null) {
          getTextView().setTextSize(2, this.k * this.mScale);
        }
      }
    }
    return localTuSdkSize;
  }
  
  public void setTextFont(Typeface paramTypeface)
  {
    if (this.a == null) {
      return;
    }
    this.a.setTypeface(paramTypeface);
  }
  
  public void setTextStrokeWidth(int paramInt)
  {
    if (this.a == null) {
      return;
    }
    this.a.setTextStrokeWidth(paramInt);
  }
  
  public void setTextStrokeColor(int paramInt)
  {
    if (this.a == null) {
      return;
    }
    this.a.setTextStrokeColor(paramInt);
  }
  
  public void setTextSize(float paramFloat)
  {
    if (this.a == null) {
      return;
    }
    this.a.setTextSize(paramFloat);
    postInvalidate();
  }
  
  public void setLetterSpacing(float paramFloat)
  {
    if (this.a == null) {
      return;
    }
    this.a.setLetterSpacing(paramFloat);
    updateText(this.a.getText().toString(), this.m);
  }
  
  public void setLineSpacing(float paramFloat1, float paramFloat2)
  {
    if (this.a == null) {
      return;
    }
    this.e = paramFloat2;
    this.a.setLineSpacing(paramFloat1, paramFloat2);
    updateText(this.a.getText().toString(), this.m);
  }
  
  public void setTranslation(final float paramFloat1, final float paramFloat2)
  {
    post(new Runnable()
    {
      public void run()
      {
        StickerTextItemView.this.mTranslation.x = paramFloat1;
        StickerTextItemView.this.mTranslation.y = paramFloat2;
        ViewCompat.setTranslationX(StickerTextItemView.this, StickerTextItemView.this.mTranslation.x);
        ViewCompat.setTranslationY(StickerTextItemView.this, StickerTextItemView.this.mTranslation.y);
      }
    });
  }
  
  public void reRotate()
  {
    ViewCompat.setRotation(this, this.mDegree);
  }
  
  public PointF getTranslation()
  {
    return this.mTranslation;
  }
  
  public TuSdkSize getCSize()
  {
    return this.mCSize;
  }
  
  public StickerData getSticker()
  {
    return this.mSticker;
  }
  
  public void setVisibility(int paramInt)
  {
    super.setVisibility(paramInt);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\sticker\StickerTextItemView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */