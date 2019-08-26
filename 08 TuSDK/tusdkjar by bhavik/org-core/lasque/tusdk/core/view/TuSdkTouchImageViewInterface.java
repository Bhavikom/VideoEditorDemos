package org.lasque.tusdk.core.view;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView.ScaleType;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public abstract interface TuSdkTouchImageViewInterface
{
  public abstract void setInvalidateTargetView(View paramView);
  
  public abstract void changeRegionRatio(Rect paramRect, float paramFloat);
  
  public abstract void setImageBitmap(Bitmap paramBitmap, ImageOrientation paramImageOrientation);
  
  public abstract void setImageBitmap(Bitmap paramBitmap);
  
  public abstract void setImageBitmapWithAnim(Bitmap paramBitmap);
  
  public abstract void setScaleType(ImageView.ScaleType paramScaleType);
  
  public abstract void setZoom(float paramFloat);
  
  public abstract void setZoom(float paramFloat1, float paramFloat2, float paramFloat3);
  
  public abstract float getCurrentZoom();
  
  public abstract RectF getZoomedRect();
  
  public abstract boolean isInAnimation();
  
  public abstract void changeImageAnimation(LsqImageChangeType paramLsqImageChangeType);
  
  public abstract ImageOrientation getImageOrientation();
  
  public static enum LsqImageChangeType
  {
    private LsqImageChangeType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\TuSdkTouchImageViewInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */