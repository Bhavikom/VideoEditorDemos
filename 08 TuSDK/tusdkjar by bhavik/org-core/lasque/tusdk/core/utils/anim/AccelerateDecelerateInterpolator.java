package org.lasque.tusdk.core.utils.anim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class AccelerateDecelerateInterpolator
  implements Interpolator
{
  public AccelerateDecelerateInterpolator() {}
  
  public AccelerateDecelerateInterpolator(Context paramContext, AttributeSet paramAttributeSet) {}
  
  public float getInterpolation(float paramFloat)
  {
    if (paramFloat < 0.4D) {
      return paramFloat * paramFloat;
    }
    if ((paramFloat >= 0.4D) && (paramFloat <= 0.6D)) {
      return (float)(3.4D * paramFloat - 1.2D);
    }
    return 1.0F - (1.0F - paramFloat) * (1.0F - paramFloat);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\anim\AccelerateDecelerateInterpolator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */