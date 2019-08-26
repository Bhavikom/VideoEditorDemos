package org.lasque.tusdk.core.utils.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import org.lasque.tusdk.core.struct.ViewSize;

public class WidthAnimation
  extends Animation
{
  private float a;
  private float b;
  private float c;
  private View d;
  
  public WidthAnimation(View paramView, float paramFloat)
  {
    this(paramView, -1.0F, paramFloat);
  }
  
  public WidthAnimation(View paramView, float paramFloat1, float paramFloat2)
  {
    this.d = paramView;
    this.a = paramFloat1;
    this.b = paramFloat2;
    if (this.a == -1.0F) {
      this.a = ViewSize.create(paramView).width;
    }
    this.c = (this.b - this.a);
  }
  
  public boolean willChangeBounds()
  {
    return true;
  }
  
  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    this.d.getLayoutParams().width = ((int)(this.a + this.c * paramFloat));
    this.d.requestLayout();
  }
  
  protected void finalize()
  {
    this.d = null;
    super.finalize();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\anim\WidthAnimation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */