package org.lasque.tusdk.core.utils.anim;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3dAnimation
  extends Animation
{
  private final float a;
  private final float b;
  private final float c;
  private final float d;
  private final float e;
  private final boolean f;
  private Camera g;
  
  public Rotate3dAnimation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, boolean paramBoolean)
  {
    this.a = paramFloat1;
    this.b = paramFloat2;
    this.c = paramFloat3;
    this.d = paramFloat4;
    this.e = paramFloat5;
    this.f = paramBoolean;
  }
  
  public void initialize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.initialize(paramInt1, paramInt2, paramInt3, paramInt4);
    this.g = new Camera();
  }
  
  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    float f1 = this.a;
    float f2 = f1 + (this.b - f1) * paramFloat;
    float f3 = this.c;
    float f4 = this.d;
    Camera localCamera = this.g;
    Matrix localMatrix = paramTransformation.getMatrix();
    localCamera.save();
    if (this.f) {
      localCamera.translate(0.0F, 0.0F, this.e * paramFloat);
    } else {
      localCamera.translate(0.0F, 0.0F, this.e * (1.0F - paramFloat));
    }
    localCamera.rotateY(f2);
    localCamera.getMatrix(localMatrix);
    localCamera.restore();
    localMatrix.preTranslate(-f3, -f4);
    localMatrix.postTranslate(f3, f4);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\anim\Rotate3dAnimation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */