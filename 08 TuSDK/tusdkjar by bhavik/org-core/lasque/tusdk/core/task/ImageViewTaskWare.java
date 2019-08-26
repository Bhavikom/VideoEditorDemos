package org.lasque.tusdk.core.task;

import android.graphics.Bitmap;
import android.widget.ImageView;
import java.lang.ref.WeakReference;
import org.lasque.tusdk.core.utils.anim.AnimHelper;

public abstract class ImageViewTaskWare
{
  private WeakReference<ImageView> a;
  private boolean b = true;
  private boolean c;
  private int d = 90;
  
  public int getImageCompress()
  {
    return this.d;
  }
  
  public void setImageCompress(int paramInt)
  {
    this.d = paramInt;
  }
  
  public void cancel()
  {
    this.c = true;
  }
  
  public boolean isCancel()
  {
    return this.c;
  }
  
  public boolean isSaveToDisk()
  {
    return this.b;
  }
  
  public void setSaveToDisk(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  public ImageView getImageView()
  {
    if (this.a != null) {
      return (ImageView)this.a.get();
    }
    return null;
  }
  
  public void setImageView(ImageView paramImageView)
  {
    if (paramImageView != null) {
      this.a = new WeakReference(paramImageView);
    } else {
      this.a = null;
    }
  }
  
  public boolean isEqualView(ImageView paramImageView)
  {
    ImageView localImageView = getImageView();
    if ((paramImageView == null) || (localImageView == null)) {
      return false;
    }
    return paramImageView.equals(localImageView);
  }
  
  public void imageLoaded(Bitmap paramBitmap, LoadType paramLoadType)
  {
    if ((paramBitmap == null) || (this.c)) {
      return;
    }
    ImageView localImageView = getImageView();
    if (localImageView == null) {
      return;
    }
    localImageView.setImageBitmap(paramBitmap);
    if (paramLoadType != LoadType.TypeMomery) {
      AnimHelper.alphaAnimator(localImageView, 120, 0.3F, 1.0F);
    }
  }
  
  public static enum LoadType
  {
    private LoadType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\task\ImageViewTaskWare.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */