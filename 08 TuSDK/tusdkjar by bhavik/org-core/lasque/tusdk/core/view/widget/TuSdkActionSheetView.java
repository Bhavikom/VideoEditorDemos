package org.lasque.tusdk.core.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.lasque.tusdk.core.type.ActivityAnimType;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class TuSdkActionSheetView
  extends TuSdkRelativeLayout
  implements Animation.AnimationListener
{
  private TuSdkActionSheetViewAnimation a;
  private int b;
  private boolean c;
  
  public TuSdkActionSheetView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkActionSheetView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkActionSheetView(Context paramContext)
  {
    super(paramContext);
  }
  
  public abstract View getMaskBg();
  
  public abstract LinearLayout getSheetTable();
  
  public abstract TextView getTitleView();
  
  public abstract Button getCancelButton();
  
  public abstract ActivityAnimType getAlphaAnimType();
  
  public abstract ActivityAnimType getTransAnimType();
  
  public TuSdkActionSheetViewAnimation getAnimationListener()
  {
    return this.a;
  }
  
  public void setAnimationListener(TuSdkActionSheetViewAnimation paramTuSdkActionSheetViewAnimation)
  {
    this.a = paramTuSdkActionSheetViewAnimation;
  }
  
  public void setDismissClickListener(View.OnClickListener paramOnClickListener)
  {
    if (getMaskBg() != null) {
      getMaskBg().setOnClickListener(paramOnClickListener);
    }
    if (getCancelButton() != null) {
      getCancelButton().setOnClickListener(paramOnClickListener);
    }
  }
  
  public void setTitle(String paramString)
  {
    if (getTitleView() != null) {
      getTitleView().setText(paramString);
    }
  }
  
  public void loadView()
  {
    super.loadView();
    showView(getTitleView(), false);
    showView(getCancelButton(), false);
  }
  
  public void runViewShowableAnim(boolean paramBoolean)
  {
    this.c = paramBoolean;
    ActivityAnimType localActivityAnimType1 = getTransAnimType();
    ActivityAnimType localActivityAnimType2 = getAlphaAnimType();
    if ((localActivityAnimType1 == null) && (localActivityAnimType2 == null))
    {
      onAnimationEnd(null);
      return;
    }
    LinearLayout localLinearLayout = getSheetTable();
    if ((localActivityAnimType1 != null) && (localLinearLayout != null))
    {
      localLinearLayout.clearAnimation();
      localObject = AnimationUtils.loadAnimation(getContext(), localActivityAnimType1.getAnim(paramBoolean));
      ((Animation)localObject).setAnimationListener(this);
      localLinearLayout.startAnimation((Animation)localObject);
    }
    Object localObject = getMaskBg();
    if ((localActivityAnimType2 != null) && (localObject != null))
    {
      ((View)localObject).clearAnimation();
      Animation localAnimation = AnimationUtils.loadAnimation(getContext(), localActivityAnimType2.getAnim(paramBoolean));
      localAnimation.setFillEnabled(true);
      localAnimation.setFillAfter(true);
      ((View)localObject).startAnimation(localAnimation);
    }
  }
  
  public void onAnimationStart(Animation paramAnimation)
  {
    this.b += 1;
  }
  
  public void onAnimationEnd(Animation paramAnimation)
  {
    this.b -= 1;
    if ((this.b > 0) || (this.a == null)) {
      return;
    }
    this.a.onAnimationEnd(this.c);
  }
  
  public void onAnimationRepeat(Animation paramAnimation) {}
  
  public static abstract interface TuSdkActionSheetViewAnimation
  {
    public abstract void onAnimationEnd(boolean paramBoolean);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\widget\TuSdkActionSheetView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */