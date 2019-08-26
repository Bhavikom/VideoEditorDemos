package org.lasque.tusdk.core.utils.anim;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

public class ZoomOutPageTransformer
  implements ViewPager.PageTransformer
{
  public void transformPage(View paramView, float paramFloat)
  {
    int i = paramView.getWidth();
    int j = paramView.getHeight();
    if (paramFloat < -1.0F)
    {
      ViewCompat.setAlpha(paramView, 0.0F);
    }
    else if (paramFloat <= 1.0F)
    {
      float f1 = Math.max(0.85F, 1.0F - Math.abs(paramFloat));
      float f2 = j * (1.0F - f1) / 2.0F;
      float f3 = i * (1.0F - f1) / 2.0F;
      if (paramFloat < 0.0F) {
        ViewCompat.setTranslationX(paramView, f3 - f2 / 2.0F);
      } else {
        ViewCompat.setTranslationX(paramView, -f3 + f2 / 2.0F);
      }
      ViewCompat.setScaleX(paramView, f1);
      ViewCompat.setScaleY(paramView, f1);
      ViewCompat.setAlpha(paramView, 0.5F + (f1 - 0.85F) / 0.14999998F * 0.5F);
    }
    else
    {
      ViewCompat.setAlpha(paramView, 0.0F);
    }
  }
  
  protected void resetView(View paramView)
  {
    ViewCompat.setAlpha(paramView, 1.0F);
    ViewCompat.setTranslationX(paramView, 0.0F);
    ViewCompat.setScaleX(paramView, 1.0F);
    ViewCompat.setScaleY(paramView, 1.0F);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\anim\ZoomOutPageTransformer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */