package org.lasque.tusdk.core.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.Calendar;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.ViewSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import org.lasque.tusdk.core.utils.anim.HeightAnimation;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkRefreshListHeaderView
  extends TuSdkRelativeLayout
{
  public static final float DROG_RESISTANCE = 0.3F;
  public static final float FRESH_OFFSET_DISTANCE = 0.5F;
  private float a;
  private int b;
  private float c;
  private TuSdkRefreshState d;
  private Calendar e;
  
  public TuSdkRefreshListHeaderView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkRefreshListHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkRefreshListHeaderView(Context paramContext)
  {
    super(paramContext);
  }
  
  public abstract RelativeLayout getHeadWrap();
  
  public abstract ImageView getLoadIcon();
  
  public void loadView()
  {
    super.loadView();
    this.b = ViewSize.create(getHeadWrap()).height;
    this.c = (this.b * 0.5F);
  }
  
  public void viewDidLoad()
  {
    super.viewDidLoad();
    this.a = (ContextUtils.getScreenSize(getContext()).height * 0.3F);
    setVisiableHeight(0);
  }
  
  public Calendar getLastDate()
  {
    return this.e;
  }
  
  public void setLastDate(Calendar paramCalendar)
  {
    this.e = paramCalendar;
  }
  
  public void setState(TuSdkRefreshState paramTuSdkRefreshState)
  {
    this.d = paramTuSdkRefreshState;
  }
  
  public TuSdkRefreshState getState()
  {
    return this.d;
  }
  
  public int updateHeight(float paramFloat)
  {
    if (this.d == TuSdkRefreshState.StateLoading) {
      return 0;
    }
    a();
    getHeadWrap().clearAnimation();
    int i = a(paramFloat);
    if (i > this.c) {
      setState(TuSdkRefreshState.StateTriggered);
    } else {
      setState(TuSdkRefreshState.StateVisible);
    }
    setVisiableHeight(i);
    return i;
  }
  
  private void a()
  {
    if (getLastDate() == null) {
      return;
    }
    setLastDate(getLastDate());
  }
  
  public TuSdkRefreshState submitState()
  {
    if (this.d == TuSdkRefreshState.StateLoading) {
      return this.d;
    }
    int i = getVisiableHeight();
    if (i == 0) {
      return this.d;
    }
    if (i < this.c)
    {
      setState(TuSdkRefreshState.StateVisible);
      a(0);
    }
    else
    {
      refreshStart();
    }
    return this.d;
  }
  
  public void refreshStart()
  {
    if (this.d == TuSdkRefreshState.StateLoading) {
      return;
    }
    a(true);
    setState(TuSdkRefreshState.StateLoading);
    a(this.b);
  }
  
  public void firstStart()
  {
    a(true);
    setState(TuSdkRefreshState.StateLoading);
    setVisiableHeight(this.b);
  }
  
  public void refreshEnded()
  {
    if (this.d != TuSdkRefreshState.StateLoading) {
      return;
    }
    a(false);
    setState(TuSdkRefreshState.StateHidden);
    setLastDate(Calendar.getInstance());
    a(0);
  }
  
  private void a(int paramInt)
  {
    getHeadWrap().clearAnimation();
    HeightAnimation localHeightAnimation = new HeightAnimation(getHeadWrap(), paramInt);
    localHeightAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
    localHeightAnimation.setDuration(300L);
    getHeadWrap().startAnimation(localHeightAnimation);
  }
  
  public void setVisiableHeight(int paramInt)
  {
    ViewGroup.LayoutParams localLayoutParams = getHeadWrap().getLayoutParams();
    localLayoutParams.height = paramInt;
    getHeadWrap().setLayoutParams(localLayoutParams);
  }
  
  public int getVisiableHeight()
  {
    return getHeadWrap().getHeight();
  }
  
  private void a(boolean paramBoolean)
  {
    ImageView localImageView = getLoadIcon();
    if (localImageView == null) {
      return;
    }
    localImageView.clearAnimation();
    if (!paramBoolean) {
      return;
    }
    RotateAnimation localRotateAnimation = new RotateAnimation(0.0F, 360.0F, 1, 0.5F, 1, 0.5F);
    localRotateAnimation.setDuration(1266L);
    localRotateAnimation.setInterpolator(new LinearInterpolator());
    localRotateAnimation.setRepeatCount(-1);
    localImageView.startAnimation(localRotateAnimation);
  }
  
  private int a(float paramFloat)
  {
    int i = 0;
    if (paramFloat < 0.0F)
    {
      i = (int)paramFloat + getHeadWrap().getHeight();
      return i < 0 ? 0 : i;
    }
    float f = countResistance(getHeadWrap().getHeight(), this.a);
    i = (int)Math.ceil(paramFloat * f) + getHeadWrap().getHeight();
    return i;
  }
  
  public static float countResistance(float paramFloat1, float paramFloat2)
  {
    if (paramFloat2 == 0.0F) {
      return 0.0F;
    }
    float f = paramFloat1 / paramFloat2;
    if (f > 1.0F) {
      f = 1.0F;
    }
    return 1.0F - f;
  }
  
  public static enum TuSdkRefreshState
  {
    private TuSdkRefreshState() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\listview\TuSdkRefreshListHeaderView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */