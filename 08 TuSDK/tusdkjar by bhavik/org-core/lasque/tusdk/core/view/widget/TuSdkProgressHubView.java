package org.lasque.tusdk.core.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.lasque.tusdk.core.utils.anim.AnimHelper;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkProgressHubView
  extends TuSdkRelativeLayout
  implements Animation.AnimationListener
{
  private boolean a;
  private TuSdkProgressHubViewDelegate b;
  
  public TuSdkProgressHubView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkProgressHubView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkProgressHubView(Context paramContext)
  {
    super(paramContext);
  }
  
  public abstract ProgressBar getProgressBar();
  
  public abstract ImageView getImageView();
  
  public abstract TextView getTitleView();
  
  public abstract LinearLayout getHubView();
  
  public abstract int getImageSucceedResId();
  
  public abstract int getImageFailedResId();
  
  public void setDelegate(TuSdkProgressHubViewDelegate paramTuSdkProgressHubViewDelegate)
  {
    this.b = paramTuSdkProgressHubViewDelegate;
  }
  
  public void setArgs(HubArgCache paramHubArgCache)
  {
    a(paramHubArgCache);
    if (paramHubArgCache.showType == null) {
      paramHubArgCache.showType = TuSdkHubViewShowType.TypeDefault;
    }
    switch (1.a[paramHubArgCache.showType.ordinal()])
    {
    case 1: 
      c(paramHubArgCache);
      break;
    case 2: 
      d(paramHubArgCache);
      break;
    case 3: 
      e(paramHubArgCache);
      break;
    default: 
      b(paramHubArgCache);
    }
  }
  
  private void a(HubArgCache paramHubArgCache)
  {
    TextView localTextView = getTitleView();
    if (localTextView == null) {
      return;
    }
    if (paramHubArgCache.text != null) {
      localTextView.setText(paramHubArgCache.text);
    } else if (paramHubArgCache.textResId != 0) {
      localTextView.setText(paramHubArgCache.textResId);
    }
  }
  
  private void b(HubArgCache paramHubArgCache)
  {
    a(true, false);
  }
  
  private void c(HubArgCache paramHubArgCache)
  {
    a(false, true);
    a(getImageSucceedResId());
  }
  
  private void d(HubArgCache paramHubArgCache)
  {
    a(false, true);
    a(getImageFailedResId());
  }
  
  private void e(HubArgCache paramHubArgCache)
  {
    a(false, true);
    a(paramHubArgCache.imageResId);
  }
  
  private void a(boolean paramBoolean1, boolean paramBoolean2)
  {
    showView(getProgressBar(), paramBoolean1);
    showView(getImageView(), paramBoolean2);
  }
  
  private void a(int paramInt)
  {
    ImageView localImageView = getImageView();
    if ((localImageView == null) || (paramInt == 0))
    {
      localImageView.setVisibility(8);
      return;
    }
    localImageView.setVisibility(0);
    localImageView.setImageResource(paramInt);
  }
  
  public void runViewShowableAnim(boolean paramBoolean)
  {
    this.a = paramBoolean;
    getHubView().clearAnimation();
    Animation localAnimation = AnimHelper.scaleAlphaAnimation(260, paramBoolean);
    localAnimation.setAnimationListener(this);
    getHubView().setAnimation(localAnimation);
  }
  
  public void onAnimationStart(Animation paramAnimation) {}
  
  public void onAnimationEnd(Animation paramAnimation)
  {
    if ((!this.a) && (this.b != null))
    {
      this.b.onDismissAnimEnded();
      this.b = null;
    }
  }
  
  public void onAnimationRepeat(Animation paramAnimation) {}
  
  public void viewWillDestory()
  {
    super.viewWillDestory();
    this.b = null;
  }
  
  public static class HubArgCache
  {
    public Context context;
    public String text;
    public int textResId;
    public long delay;
    public TuSdkProgressHubView.TuSdkHubViewShowType showType;
    public int imageResId;
    
    public HubArgCache(Context paramContext, TuSdkProgressHubView.TuSdkHubViewShowType paramTuSdkHubViewShowType, String paramString, int paramInt1, int paramInt2, long paramLong)
    {
      this.context = paramContext;
      this.showType = paramTuSdkHubViewShowType;
      this.text = paramString;
      this.imageResId = paramInt2;
      this.textResId = paramInt1;
      this.delay = paramLong;
    }
  }
  
  public static abstract interface TuSdkProgressHubViewDelegate
  {
    public abstract void onDismissAnimEnded();
  }
  
  public static enum TuSdkHubViewShowType
  {
    private TuSdkHubViewShowType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\TuSdkProgressHubView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */