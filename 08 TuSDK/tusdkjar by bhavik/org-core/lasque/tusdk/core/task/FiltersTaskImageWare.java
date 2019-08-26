package org.lasque.tusdk.core.task;

import android.widget.ImageView;
import java.lang.ref.WeakReference;
import org.lasque.tusdk.core.utils.anim.AnimHelper;

public class FiltersTaskImageWare
{
  private WeakReference<ImageView> a;
  private String b;
  
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
  
  public String getFilterName()
  {
    return this.b;
  }
  
  public void setFilterName(String paramString)
  {
    this.b = paramString;
  }
  
  public FiltersTaskImageWare() {}
  
  public FiltersTaskImageWare(ImageView paramImageView, String paramString)
  {
    setImageView(paramImageView);
    this.b = paramString;
  }
  
  public boolean isEqualView(ImageView paramImageView)
  {
    ImageView localImageView = getImageView();
    if ((paramImageView == null) || (localImageView == null)) {
      return false;
    }
    return paramImageView.equals(localImageView);
  }
  
  public boolean setImageResult(FiltersTaskImageResult paramFiltersTaskImageResult)
  {
    ImageView localImageView = getImageView();
    if (localImageView == null) {
      return true;
    }
    if (!paramFiltersTaskImageResult.getFilterName().equalsIgnoreCase(this.b)) {
      return false;
    }
    localImageView.setImageBitmap(paramFiltersTaskImageResult.getImage());
    AnimHelper.alphaAnimator(localImageView, 200, 0.3F, 1.0F);
    return true;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\task\FiltersTaskImageWare.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */