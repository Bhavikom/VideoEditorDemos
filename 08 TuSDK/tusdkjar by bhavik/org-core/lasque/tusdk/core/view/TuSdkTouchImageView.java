package org.lasque.tusdk.core.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView.ScaleType;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.ViewSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.modules.components.ComponentActType;

public class TuSdkTouchImageView
  extends TouchImageView
  implements TuSdkTouchImageViewInterface
{
  private ImageOrientation a = ImageOrientation.Up;
  private boolean b;
  private View c;
  private TouchImageView.OnTouchImageViewListener d = null;
  private TouchImageView.OnTouchImageViewListener e = new TouchImageView.OnTouchImageViewListener()
  {
    public void onMove()
    {
      TuSdkTouchImageView.this.invalidateTargetView();
    }
  };
  
  public TuSdkTouchImageView(Context paramContext)
  {
    super(paramContext);
    initView();
  }
  
  public TuSdkTouchImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }
  
  public TuSdkTouchImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  protected void initView() {}
  
  public ImageOrientation getImageOrientation()
  {
    return this.a;
  }
  
  public boolean isInAnimation()
  {
    return this.b;
  }
  
  public void setInvalidateTargetView(View paramView)
  {
    this.c = paramView;
    super.setOnTouchImageViewListener(this.e);
  }
  
  public void setOnTouchImageViewListener(TouchImageView.OnTouchImageViewListener paramOnTouchImageViewListener)
  {
    this.d = paramOnTouchImageViewListener;
    super.setOnTouchImageViewListener(this.e);
  }
  
  public void invalidateTargetView()
  {
    if (this.d != null) {
      this.d.onMove();
    }
    if (this.c != null) {
      this.c.invalidate();
    }
  }
  
  public void setImageBitmap(Bitmap paramBitmap, ImageOrientation paramImageOrientation)
  {
    if (paramImageOrientation == null) {
      paramImageOrientation = ImageOrientation.Up;
    }
    this.a = paramImageOrientation;
    if (paramBitmap != null) {
      paramBitmap = BitmapHelper.imageRotaing(paramBitmap, this.a);
    }
    super.setImageBitmap(paramBitmap);
  }
  
  public void setImageBitmapWithAnim(Bitmap paramBitmap)
  {
    super.setImageBitmapWithAnim(paramBitmap);
  }
  
  public void changeImageAnimation(TuSdkTouchImageViewInterface.LsqImageChangeType paramLsqImageChangeType)
  {
    if ((paramLsqImageChangeType == null) || (this.b)) {
      return;
    }
    long l = 0L;
    switch (8.a[paramLsqImageChangeType.ordinal()])
    {
    case 1: 
      a(-90);
      l = ComponentActType.editCuter_action_trun_left;
      break;
    case 2: 
      a(90);
      l = ComponentActType.editCuter_action_trun_right;
      break;
    case 3: 
      a(true);
      l = ComponentActType.editCuter_action_mirror_horizontal;
      break;
    case 4: 
      a(false);
      l = ComponentActType.editCuter_action_mirror_vertical;
      break;
    }
    if (l != 0L) {
      StatisticsManger.appendComponent(l);
    }
  }
  
  private void a(int paramInt)
  {
    Bitmap localBitmap = BitmapHelper.getDrawableBitmap(this);
    if (localBitmap == null) {
      return;
    }
    this.b = true;
    float f = 1.0F;
    TuSdkSize localTuSdkSize = TuSdkSize.create(localBitmap);
    ViewSize localViewSize = ViewSize.create(this);
    if (getScaleType() == ImageView.ScaleType.FIT_CENTER)
    {
      if (localTuSdkSize.width > localTuSdkSize.height) {
        f = localViewSize.height / localViewSize.width;
      } else if (localViewSize.width < localViewSize.height) {
        f = localViewSize.width / localViewSize.height;
      }
    }
    else if (getScaleType() == ImageView.ScaleType.CENTER_CROP) {
      if (localTuSdkSize.width > localTuSdkSize.height) {
        f = localViewSize.width / localViewSize.height;
      } else if (localViewSize.width < localViewSize.height) {
        f = localViewSize.height / localViewSize.width;
      }
    }
    resetZoom();
    ViewCompat.animate(this).scaleX(f).setDuration(200L);
    ViewCompat.animate(this).scaleY(f).setDuration(200L);
    ViewCompat.animate(this).rotation(paramInt).setDuration(190L).setListener(new ViewPropertyAnimatorListenerAdapter()
    {
      public void onAnimationEnd(View paramAnonymousView)
      {
        TuSdkTouchImageView.a(TuSdkTouchImageView.this);
      }
    }).setUpdateListener(new ViewPropertyAnimatorUpdateListener()
    {
      public void onAnimationUpdate(View paramAnonymousView)
      {
        TuSdkTouchImageView.this.invalidateTargetView();
      }
    });
  }
  
  private void a()
  {
    Bitmap localBitmap = BitmapHelper.getDrawableBitmap(this);
    if (localBitmap == null) {
      return;
    }
    ImageOrientation localImageOrientation = ImageOrientation.getValue((int)ViewCompat.getRotation(this), false);
    localBitmap = BitmapHelper.imageRotaing(localBitmap, localImageOrientation);
    int i = this.a.getDegree() + localImageOrientation.getDegree();
    if (this.a.isMirrored()) {
      i -= 180;
    }
    this.a = ImageOrientation.getValue(i, this.a.isMirrored());
    ViewCompat.setScaleX(this, 1.0F);
    ViewCompat.setScaleY(this, 1.0F);
    ViewCompat.setRotation(this, 0.0F);
    setImageBitmap(localBitmap);
    invalidateTargetView();
    this.b = false;
  }
  
  private void a(final boolean paramBoolean)
  {
    Bitmap localBitmap = BitmapHelper.getDrawableBitmap(this);
    if (localBitmap == null) {
      return;
    }
    this.b = true;
    resetZoom();
    int[] arrayOfInt = null;
    if (paramBoolean) {
      arrayOfInt = new int[] { -1, 1 };
    } else {
      arrayOfInt = new int[] { 1, -1 };
    }
    ViewCompat.animate(this).scaleX(arrayOfInt[0]).setDuration(200L);
    ViewCompat.animate(this).scaleY(arrayOfInt[1]).setDuration(190L).setListener(new ViewPropertyAnimatorListenerAdapter()
    {
      public void onAnimationEnd(View paramAnonymousView)
      {
        TuSdkTouchImageView.a(TuSdkTouchImageView.this, paramBoolean);
      }
    }).setUpdateListener(new ViewPropertyAnimatorUpdateListener()
    {
      public void onAnimationUpdate(View paramAnonymousView)
      {
        TuSdkTouchImageView.this.invalidateTargetView();
      }
    });
  }
  
  private void b(boolean paramBoolean)
  {
    Bitmap localBitmap = BitmapHelper.getDrawableBitmap(this);
    if (localBitmap == null) {
      return;
    }
    ImageOrientation localImageOrientation = paramBoolean ? ImageOrientation.UpMirrored : ImageOrientation.DownMirrored;
    localBitmap = BitmapHelper.imageRotaing(localBitmap, localImageOrientation);
    int i = (localImageOrientation.getDegree() + this.a.getDegree()) % 360;
    this.a = ImageOrientation.getValue(i, !this.a.isMirrored());
    ViewCompat.setScaleX(this, 1.0F);
    ViewCompat.setScaleY(this, 1.0F);
    setImageBitmap(localBitmap);
    invalidateTargetView();
    this.b = false;
  }
  
  public void changeRegionRatio(final Rect paramRect, final float paramFloat)
  {
    Bitmap localBitmap = BitmapHelper.getDrawableBitmap(this);
    if ((localBitmap == null) || (this.b)) {
      return;
    }
    this.b = true;
    TuSdkSize localTuSdkSize = TuSdkSize.create(localBitmap);
    float f1 = 1.0F;
    if ((paramFloat > 0.0F) && (getScaleType() == ImageView.ScaleType.FIT_CENTER))
    {
      f1 = RectHelper.computerOutScale(paramRect, localTuSdkSize.getRatioFloat(), false);
    }
    else if ((paramFloat <= 0.0F) && (getScaleType() == ImageView.ScaleType.CENTER_CROP))
    {
      f1 = RectHelper.computerOutScale(paramRect, localTuSdkSize.getRatioFloat(), true);
    }
    else
    {
      float f2 = paramRect.width() / getWidth();
      float f3 = paramRect.height() / getHeight();
      float f4 = RectHelper.computerOutScale(TuSdkViewHelper.getViewRect(this), localTuSdkSize.getRatioFloat(), false);
      float f5 = RectHelper.computerOutScale(paramRect, localTuSdkSize.getRatioFloat(), false);
      if (f5 > f4) {
        f1 = Math.max(f2, f3);
      } else {
        f1 = Math.min(f2, f3);
      }
    }
    resetZoom();
    ViewCompat.animate(this).scaleX(f1).setDuration(200L);
    ViewCompat.animate(this).scaleY(f1).setDuration(190L).setListener(new ViewPropertyAnimatorListenerAdapter()
    {
      public void onAnimationEnd(View paramAnonymousView)
      {
        TuSdkTouchImageView.this.onRegionRatioAnimationEnd(paramRect, paramFloat);
      }
    }).setUpdateListener(new ViewPropertyAnimatorUpdateListener()
    {
      public void onAnimationUpdate(View paramAnonymousView)
      {
        TuSdkTouchImageView.this.invalidateTargetView();
      }
    });
  }
  
  protected void onRegionRatioAnimationEnd(Rect paramRect, float paramFloat)
  {
    ViewCompat.setScaleX(this, 1.0F);
    ViewCompat.setScaleY(this, 1.0F);
    if (paramFloat > 0.0F) {
      setScaleType(ImageView.ScaleType.CENTER_CROP);
    } else {
      setScaleType(ImageView.ScaleType.FIT_CENTER);
    }
    TuSdkViewHelper.setViewRect(this, paramRect);
    setZoom(1.0F);
    invalidateTargetView();
    this.b = false;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\TuSdkTouchImageView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */