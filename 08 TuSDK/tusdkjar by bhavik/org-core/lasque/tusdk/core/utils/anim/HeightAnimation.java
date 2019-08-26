package org.lasque.tusdk.core.utils.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import org.lasque.tusdk.core.struct.ViewSize;

public class HeightAnimation
  extends Animation
{
  private float a;
  private float b;
  private float c;
  private View d;
  
  public HeightAnimation(View paramView, float paramFloat)
  {
    this(paramView, -1.0F, paramFloat);
  }
  
  public HeightAnimation(View paramView, float paramFloat1, float paramFloat2)
  {
    this.d = paramView;
    this.a = paramFloat1;
    this.b = paramFloat2;
    if (this.a == -1.0F) {
      this.a = ViewSize.create(paramView).height;
    }
    this.c = (this.b - this.a);
  }
  
  public boolean willChangeBounds()
  {
    return true;
  }
  
  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    this.d.getLayoutParams().height = ((int)(this.a + this.c * paramFloat));
    this.d.requestLayout();
  }
  
  protected void finalize()
  {
    this.d = null;
    super.finalize();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\anim\HeightAnimation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */