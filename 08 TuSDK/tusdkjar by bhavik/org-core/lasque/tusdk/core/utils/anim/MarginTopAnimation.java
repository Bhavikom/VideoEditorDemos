package org.lasque.tusdk.core.utils.anim;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class MarginTopAnimation
  extends Animation
{
  private float a;
  private float b;
  private float c;
  private View d;
  
  public MarginTopAnimation(View paramView, float paramFloat1, float paramFloat2)
  {
    this.d = paramView;
    this.a = paramFloat1;
    this.b = paramFloat2;
    this.c = (this.b - this.a);
  }
  
  public boolean willChangeBounds()
  {
    return true;
  }
  
  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    float f = this.a + this.c * paramFloat;
    setMarginTop((int)f);
  }
  
  protected void setMarginTop(int paramInt)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)this.d.getLayoutParams();
    if (localMarginLayoutParams == null) {
      return;
    }
    localMarginLayoutParams.topMargin = paramInt;
    this.d.requestLayout();
  }
  
  protected void finalize()
  {
    this.d = null;
    super.finalize();
  }
  
  public static void showTopView(View paramView, long paramLong, boolean paramBoolean)
  {
    if ((paramView == null) || (paramView.getLayoutParams() == null)) {
      return;
    }
    paramView.clearAnimation();
    int i = paramBoolean ? -paramView.getHeight() : 0;
    int j = -paramView.getHeight() - i;
    MarginTopAnimation localMarginTopAnimation = new MarginTopAnimation(paramView, i, j);
    localMarginTopAnimation.setDuration(paramLong);
    localMarginTopAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
    paramView.startAnimation(localMarginTopAnimation);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\anim\MarginTopAnimation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */