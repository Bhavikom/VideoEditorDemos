package org.lasque.tusdk.impl.view.widget;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Rect;
import android.graphics.RectF;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import org.lasque.tusdk.core.utils.anim.RectFEvaluator;

public class RegionDefaultHandler
  implements RegionHandler
{
  private float a;
  private float b = -1.0F;
  private TuSdkSize c = TuSdkSize.create(0, 0);
  private RectF d = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
  private RectF e = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
  private RegionChangeAnimation f;
  
  public void setRatio(float paramFloat)
  {
    this.a = paramFloat;
    if (this.a < 0.0F) {
      this.a = 0.0F;
    }
    this.d = recalculate(this.a, this.c);
    this.e = recalculateCenterRectPercent(this.a, this.c);
  }
  
  public float getRatio()
  {
    return this.a;
  }
  
  public void setOffsetTopPercent(float paramFloat)
  {
    this.b = paramFloat;
  }
  
  public float getOffsetTopPercent()
  {
    return this.b;
  }
  
  public void setWrapSize(TuSdkSize paramTuSdkSize)
  {
    this.c = paramTuSdkSize;
    if (this.c == null) {
      this.c = TuSdkSize.create(0, 0);
    }
    this.d = recalculate(this.a, this.c);
  }
  
  public TuSdkSize getWrapSize()
  {
    return this.c;
  }
  
  public RectF getRectPercent()
  {
    return this.d;
  }
  
  public RectF getCenterRectPercent()
  {
    return this.e;
  }
  
  protected RectF recalculateCenterRectPercent(float paramFloat, TuSdkSize paramTuSdkSize)
  {
    if ((paramFloat == 0.0F) || (paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return new RectF(0.0F, 0.0F, 1.0F, 1.0F);
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramTuSdkSize);
    localTuSdkSize.width = ((int)(paramTuSdkSize.height * paramFloat));
    Rect localRect = RectHelper.makeRectWithAspectRatioInsideRect(localTuSdkSize, new Rect(0, 0, paramTuSdkSize.width, paramTuSdkSize.height));
    float f1 = localRect.left / paramTuSdkSize.width;
    float f2 = localRect.top / paramTuSdkSize.height;
    float f3 = localRect.right / paramTuSdkSize.width;
    float f4 = localRect.bottom / paramTuSdkSize.height;
    RectF localRectF = new RectF(f1, f2, f3, f4);
    return localRectF;
  }
  
  protected RectF recalculate(float paramFloat, TuSdkSize paramTuSdkSize)
  {
    RectF localRectF = recalculateCenterRectPercent(paramFloat, paramTuSdkSize);
    if ((this.b >= 0.0F) && (this.b <= 1.0F))
    {
      float f1 = localRectF.top - this.b;
      localRectF.top -= f1;
      localRectF.bottom -= f1;
    }
    return localRectF;
  }
  
  public RectF changeWithRatio(float paramFloat, RegionHandler.RegionChangerListener paramRegionChangerListener)
  {
    if (paramFloat == getRatio()) {
      return getRectPercent();
    }
    a().setCurrent(getRectPercent());
    setRatio(paramFloat);
    a().startTo(getRectPercent(), paramRegionChangerListener);
    return getRectPercent();
  }
  
  private RegionChangeAnimation a()
  {
    if (this.f == null)
    {
      this.f = new RegionChangeAnimation(getRectPercent());
      this.f.setDuration(260);
      this.f.setInterpolator(new AccelerateDecelerateInterpolator());
    }
    this.f.cancel();
    return this.f;
  }
  
  private class RegionChangeAnimation
    implements ValueAnimator.AnimatorUpdateListener
  {
    private RectF b;
    private RectF c;
    private RegionHandler.RegionChangerListener d;
    private ValueAnimator e;
    private int f;
    private TimeInterpolator g;
    
    public RegionChangeAnimation(RectF paramRectF)
    {
      this.b = paramRectF;
    }
    
    public void cancel()
    {
      if (this.e != null) {
        this.e.cancel();
      }
      this.e = null;
    }
    
    public void setInterpolator(TimeInterpolator paramTimeInterpolator)
    {
      this.g = paramTimeInterpolator;
    }
    
    public void setDuration(int paramInt)
    {
      this.f = paramInt;
    }
    
    public void setCurrent(RectF paramRectF)
    {
      this.b = paramRectF;
    }
    
    public void startTo(RectF paramRectF, RegionHandler.RegionChangerListener paramRegionChangerListener)
    {
      this.c = paramRectF;
      this.d = paramRegionChangerListener;
      a();
    }
    
    private void a()
    {
      cancel();
      this.e = ValueAnimator.ofObject(new RectFEvaluator(), new Object[] { new RectF(this.b), this.c });
      this.e.setDuration(this.f);
      this.e.setInterpolator(this.g);
      this.e.addUpdateListener(this);
      this.e.start();
    }
    
    public void onAnimationUpdate(ValueAnimator paramValueAnimator)
    {
      this.b = ((RectF)paramValueAnimator.getAnimatedValue());
      if (this.d != null) {
        this.d.onRegionChanged(this.b);
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\RegionDefaultHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */