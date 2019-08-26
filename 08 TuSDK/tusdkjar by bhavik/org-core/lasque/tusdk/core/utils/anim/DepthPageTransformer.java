package org.lasque.tusdk.core.utils.anim;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

public class DepthPageTransformer
  implements ViewPager.PageTransformer
{
  public void transformPage(View paramView, float paramFloat)
  {
    int i = paramView.getWidth();
    if (paramFloat < -1.0F)
    {
      ViewCompat.setAlpha(paramView, 0.0F);
    }
    else if (paramFloat <= 0.0F)
    {
      ViewCompat.setAlpha(paramView, 1.0F);
      ViewCompat.setTranslationX(paramView, 0.0F);
      ViewCompat.setScaleX(paramView, 1.0F);
      ViewCompat.setScaleY(paramView, 1.0F);
    }
    else if (paramFloat <= 1.0F)
    {
      ViewCompat.setAlpha(paramView, 1.0F - paramFloat);
      ViewCompat.setTranslationX(paramView, i * -paramFloat);
      float f = 0.75F + 0.25F * (1.0F - Math.abs(paramFloat));
      ViewCompat.setScaleX(paramView, f);
      ViewCompat.setScaleY(paramView, f);
    }
    else
    {
      ViewCompat.setAlpha(paramView, 0.0F);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\anim\DepthPageTransformer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */