package org.lasque.tusdk.modules.components.camera;

import android.content.Context;
import android.graphics.PointF;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.ViewSize;
import org.lasque.tusdk.core.utils.ColorUtils;
import org.lasque.tusdk.core.utils.anim.AnimHelper;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public class TuFocusRangeView
  extends TuSdkRelativeLayout
  implements TuFocusRangeViewInterface
{
  public static final float FocusRangeScale = 0.6F;
  private View a;
  private View b;
  private int c;
  private int d;
  private int e;
  private TuSdkSize f;
  private TuSdkSize g;
  private TuSdkSize h;
  private Handler i = new Handler();
  private Runnable j = new Runnable()
  {
    public void run()
    {
      TuFocusRangeView.this.showViewIn(false);
    }
  };
  
  public static int getLayoutId()
  {
    return TuSdkContext.getLayoutResId("tusdk_impl_component_camera_focus_range_view");
  }
  
  public TuFocusRangeView(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuFocusRangeView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuFocusRangeView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkSize getMaxRangeSize()
  {
    if (this.f == null)
    {
      this.f = ViewSize.create(this);
      getMinCrosshairSize();
    }
    return this.f;
  }
  
  public void setMaxRangeSize(TuSdkSize paramTuSdkSize)
  {
    this.f = paramTuSdkSize;
  }
  
  public TuSdkSize getMinRangeSize()
  {
    if (this.g == null)
    {
      TuSdkSize localTuSdkSize = getMaxRangeSize();
      this.g = new TuSdkSize((int)Math.floor(localTuSdkSize.width * 0.6F), (int)Math.floor(localTuSdkSize.height * 0.6F));
    }
    return this.g;
  }
  
  public void setMinRangeSize(TuSdkSize paramTuSdkSize)
  {
    this.g = paramTuSdkSize;
  }
  
  public TuSdkSize getMinCrosshairSize()
  {
    if (this.h == null) {
      this.h = ViewSize.create(getFocusCrosshair());
    }
    return this.h;
  }
  
  public void setMinCrosshairSize(TuSdkSize paramTuSdkSize)
  {
    this.h = paramTuSdkSize;
  }
  
  protected void initView()
  {
    super.initView();
    this.c = TuSdkContext.getColor("lsq_focus_normal");
    this.d = TuSdkContext.getColor("lsq_focus_succeed");
    this.e = TuSdkContext.getColor("lsq_focus_failed");
  }
  
  public View getFocusOutView()
  {
    if (this.a == null) {
      this.a = getViewById("lsq_range_wrap");
    }
    return this.a;
  }
  
  public View getFocusCrosshair()
  {
    if (this.b == null) {
      this.b = getViewById("lsq_crosshair");
    }
    return this.b;
  }
  
  public int getNormalColor()
  {
    return this.c;
  }
  
  public void setNormalColor(int paramInt)
  {
    this.c = paramInt;
  }
  
  public int getSucceedColor()
  {
    return this.d;
  }
  
  public void setSucceedColor(int paramInt)
  {
    this.d = paramInt;
  }
  
  public int getFailedColor()
  {
    return this.e;
  }
  
  public void setFailedColor(int paramInt)
  {
    this.e = paramInt;
  }
  
  public void setDisplayColor(int paramInt)
  {
    ColorUtils.setBackgroudImageColor(getFocusOutView(), paramInt);
    ColorUtils.setBackgroudImageColor(getFocusCrosshair(), paramInt);
  }
  
  public void setFoucsState(boolean paramBoolean)
  {
    this.i.postDelayed(this.j, 500L);
    setDisplayColor(paramBoolean ? this.d : this.e);
  }
  
  public void setPosition(PointF paramPointF)
  {
    if (paramPointF == null) {
      return;
    }
    a(paramPointF);
    CameraFocusAnimation localCameraFocusAnimation = new CameraFocusAnimation();
    localCameraFocusAnimation.setDuration(200L);
    localCameraFocusAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
    startAnimation(localCameraFocusAnimation);
  }
  
  private void a(PointF paramPointF)
  {
    this.i.removeCallbacks(this.j);
    AnimHelper.clear(this);
    ViewCompat.setAlpha(getFocusCrosshair(), 0.0F);
    setDisplayColor(this.c);
    showViewIn(true);
    b(paramPointF);
  }
  
  private void b(PointF paramPointF)
  {
    TuSdkSize localTuSdkSize = getMaxRangeSize();
    ViewSize localViewSize = ViewSize.create((View)getParent());
    float f1 = paramPointF.x - localTuSdkSize.width * 0.5F;
    float f2 = paramPointF.y - localTuSdkSize.height * 0.5F;
    if (f1 < 0.0F) {
      f1 = 0.0F;
    } else if (f1 + localTuSdkSize.width > localViewSize.width) {
      f1 = localViewSize.width - localTuSdkSize.width;
    }
    if (f2 < 0.0F) {
      f2 = 0.0F;
    } else if (f2 + localTuSdkSize.height > localViewSize.height) {
      f2 = localViewSize.height - localTuSdkSize.height;
    }
    setMargin((int)Math.floor(f1), (int)Math.floor(f2), 0, 0);
    setSize(getFocusOutView(), localTuSdkSize);
    setSize(getFocusCrosshair(), localTuSdkSize);
  }
  
  private class CameraFocusAnimation
    extends Animation
  {
    private TuSdkSize b;
    private TuSdkSize c;
    
    public CameraFocusAnimation()
    {
      TuSdkSize localTuSdkSize1 = TuFocusRangeView.this.getMaxRangeSize();
      TuSdkSize localTuSdkSize2 = TuFocusRangeView.this.getMinRangeSize();
      TuSdkSize localTuSdkSize3 = TuFocusRangeView.this.getMinCrosshairSize();
      this.b = new TuSdkSize();
      this.b.width = (localTuSdkSize1.width - localTuSdkSize2.width);
      this.b.height = (localTuSdkSize1.height - localTuSdkSize2.height);
      this.c = new TuSdkSize();
      this.c.width = (localTuSdkSize1.width - localTuSdkSize3.width);
      this.c.height = (localTuSdkSize1.height - localTuSdkSize3.height);
    }
    
    public boolean willChangeBounds()
    {
      return true;
    }
    
    protected void applyTransformation(float paramFloat, Transformation paramTransformation)
    {
      ViewCompat.setAlpha(TuFocusRangeView.this.getFocusCrosshair(), paramFloat);
      TuSdkSize localTuSdkSize1 = TuFocusRangeView.this.getMaxRangeSize();
      TuSdkSize localTuSdkSize2 = new TuSdkSize();
      localTuSdkSize2.width = ((int)(localTuSdkSize1.width - paramFloat * this.b.width));
      localTuSdkSize2.height = ((int)(localTuSdkSize1.height - paramFloat * this.b.height));
      TuSdkSize localTuSdkSize3 = new TuSdkSize();
      localTuSdkSize3.width = ((int)(localTuSdkSize1.width - paramFloat * this.c.width));
      localTuSdkSize3.height = ((int)(localTuSdkSize1.height - paramFloat * this.c.height));
      TuFocusRangeView.this.setSize(TuFocusRangeView.this.getFocusOutView(), localTuSdkSize2);
      TuFocusRangeView.this.setSize(TuFocusRangeView.this.getFocusCrosshair(), localTuSdkSize3);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\camera\TuFocusRangeView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */