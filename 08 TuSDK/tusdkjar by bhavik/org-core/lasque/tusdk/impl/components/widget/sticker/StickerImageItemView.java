package org.lasque.tusdk.impl.components.widget.sticker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.TuSdkImageView;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData.StickerType;
import org.lasque.tusdk.modules.view.widget.sticker.StickerItemViewInterface.StickerItemViewDelegate;

public final class StickerImageItemView
  extends StickerItemViewBase
{
  private TuSdkImageView a;
  private TuSdkImageButton b;
  private TuSdkImageButton c;
  private View.OnClickListener d = new TuSdkViewHelper.OnSafeClickListener()
  {
    public void onSafeClick(View paramAnonymousView)
    {
      if (StickerImageItemView.this.equalViewIds(paramAnonymousView, StickerImageItemView.this.getCancelButton())) {
        StickerImageItemView.a(StickerImageItemView.this);
      }
    }
  };
  private int e;
  private int f;
  
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_sticker_image_item_view");
  }
  
  public StickerImageItemView(Context paramContext)
  {
    super(paramContext);
  }
  
  public StickerImageItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public StickerImageItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkImageView getImageView()
  {
    if (this.a == null) {
      this.a = ((TuSdkImageView)getViewById("lsq_sticker_imageView"));
    }
    return this.a;
  }
  
  public final TuSdkImageButton getCancelButton()
  {
    if (this.b == null)
    {
      this.b = ((TuSdkImageButton)getViewById("lsq_sticker_cancelButton"));
      if (this.b != null) {
        this.b.setOnClickListener(this.d);
      }
    }
    return this.b;
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  public final TuSdkImageButton getTurnButton()
  {
    if (this.c == null)
    {
      this.c = ((TuSdkImageButton)getViewById("lsq_sticker_turnButton"));
      if (this.c != null) {
        this.c.setOnTouchListener(this.mOnTouchListener);
      }
    }
    return this.c;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.mStickerType != StickerView.StickerType.Normal) && (this.mStickerType != this.mType)) {
      return false;
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void loadView()
  {
    getImageView();
    getCancelButton();
    getTurnButton();
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
    this.f = paramInt1;
    this.e = (paramInt2 > 0 ? paramInt2 : 0);
    if (getImageView() != null) {
      getImageView().setStroke(this.f, this.e);
    }
  }
  
  protected void onLayouted()
  {
    super.onLayouted();
    if (getImageView() == null) {
      return;
    }
    this.mCMargin.x = (getWidth() - getImageView().getWidth());
    this.mCMargin.y = (getHeight() - getImageView().getHeight());
    Rect localRect1 = TuSdkViewHelper.locationInWindow(this);
    Rect localRect2 = TuSdkViewHelper.locationInWindow(getImageView());
    this.mCOffset.x = (localRect2.left - localRect1.left);
    this.mCOffset.y = (localRect2.top - localRect1.top);
    initStickerPostion();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt2);
    super.onMeasure(paramInt1, paramInt2);
  }
  
  public void setSticker(StickerData paramStickerData)
  {
    if (paramStickerData == null) {
      return;
    }
    showViewIn(getTurnButton(), paramStickerData.getType() == StickerData.StickerType.TypeImage);
    showViewIn(this, false);
    getImageView().post(new Runnable()
    {
      public void run()
      {
        if (StickerImageItemView.this.getWidth() == 0) {
          StickerImageItemView.this.postInvalidate();
        }
        StickerImageItemView.this.showViewIn(StickerImageItemView.this, true);
      }
    });
    this.mSticker = paramStickerData;
    this.mCSize = paramStickerData.sizePixies();
    if (getImageView() != null)
    {
      getImageView().setImageBitmap(paramStickerData.getImage());
      if ((paramStickerData.stickerId != 0L) && (paramStickerData.categoryId != 0L)) {
        paramStickerData.setImage(null);
      }
    }
    if (this.isLayouted) {
      initStickerPostion();
    }
  }
  
  public void setSelected(boolean paramBoolean)
  {
    if (getImageView() != null)
    {
      int i = paramBoolean ? this.f : 0;
      getImageView().setStroke(i, this.e);
    }
    showViewIn(getCancelButton(), paramBoolean);
    if ((this.mSticker.getType() == StickerData.StickerType.TypeImage) || (this.mSticker.getType() == StickerData.StickerType.TypeText)) {
      showViewIn(getTurnButton(), paramBoolean);
    }
  }
  
  public void setTranslation(final float paramFloat1, final float paramFloat2)
  {
    post(new Runnable()
    {
      public void run()
      {
        StickerImageItemView.this.mTranslation.x = paramFloat1;
        StickerImageItemView.this.mTranslation.y = paramFloat2;
        ViewCompat.setTranslationX(StickerImageItemView.this, StickerImageItemView.this.mTranslation.x);
        ViewCompat.setTranslationY(StickerImageItemView.this, StickerImageItemView.this.mTranslation.y);
      }
    });
  }
  
  public PointF getTranslation()
  {
    return this.mTranslation;
  }
  
  public StickerData getSticker()
  {
    return this.mSticker;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\components\widget\sticker\StickerImageItemView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */