package org.lasque.tusdk.core.utils.anim;

import android.animation.TypeEvaluator;
import android.graphics.RectF;

public class RectFEvaluator
  implements TypeEvaluator<RectF>
{
  public RectF evaluate(float paramFloat, RectF paramRectF1, RectF paramRectF2)
  {
    return new RectF(paramRectF1.left + (paramRectF2.left - paramRectF1.left) * paramFloat, paramRectF1.top + (paramRectF2.top - paramRectF1.top) * paramFloat, paramRectF1.right + (paramRectF2.right - paramRectF1.right) * paramFloat, paramRectF1.bottom + (paramRectF2.bottom - paramRectF1.bottom) * paramFloat);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\anim\RectFEvaluator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */