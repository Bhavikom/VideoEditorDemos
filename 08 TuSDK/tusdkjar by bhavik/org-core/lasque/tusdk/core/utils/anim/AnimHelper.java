package org.lasque.tusdk.core.utils.anim;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import org.lasque.tusdk.core.listener.AnimationListenerAdapter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.ViewSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;

public class AnimHelper
{
  public static Animation getResAnima(Context paramContext, int paramInt)
  {
    if ((paramContext == null) || (paramInt == 0)) {
      return null;
    }
    Animation localAnimation = AnimationUtils.loadAnimation(paramContext, paramInt);
    return localAnimation;
  }
  
  public static void clearAnimation(View paramView)
  {
    if (paramView == null) {
      return;
    }
    paramView.clearAnimation();
  }
  
  public static Animation shakeAnimation(int paramInt)
  {
    TranslateAnimation localTranslateAnimation = new TranslateAnimation(0.0F, 10.0F, 0.0F, 0.0F);
    localTranslateAnimation.setInterpolator(new CycleInterpolator(paramInt));
    localTranslateAnimation.setDuration(300L);
    return localTranslateAnimation;
  }
  
  public static Animation heightAnimation(View paramView, int paramInt)
  {
    if (paramView == null) {
      return null;
    }
    HeightAnimation localHeightAnimation = new HeightAnimation(paramView, paramInt);
    localHeightAnimation.setDuration(260L);
    localHeightAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
    paramView.startAnimation(localHeightAnimation);
    return localHeightAnimation;
  }
  
  public static Animation alphAnimation(boolean paramBoolean, int paramInt)
  {
    float f1 = paramBoolean ? 1.0F : 0.0F;
    float f2 = 1.0F - f1;
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(f1, f2);
    localAlphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
    localAlphaAnimation.setDuration(paramInt);
    return localAlphaAnimation;
  }
  
  public static ViewPropertyAnimatorCompat alphaAnimator(View paramView, int paramInt, float paramFloat1, float paramFloat2)
  {
    if (paramView == null) {
      return null;
    }
    clearAnimation(paramView);
    ViewCompat.setAlpha(paramView, paramFloat1);
    return ViewCompat.animate(paramView).alpha(paramFloat2).setDuration(paramInt).setInterpolator(new AccelerateDecelerateInterpolator());
  }
  
  public static void alphaHidden(View paramView)
  {
    ViewPropertyAnimatorCompat localViewPropertyAnimatorCompat = alphaAnimator(paramView, 200, 1.0F, 0.0F);
    if (localViewPropertyAnimatorCompat == null) {
      return;
    }
    localViewPropertyAnimatorCompat.setListener(new ViewPropertyAnimatorListenerAdapter()
    {
      public void onAnimationEnd(View paramAnonymousView)
      {
        paramAnonymousView.setVisibility(4);
      }
    });
  }
  
  public static void alphaShow(View paramView)
  {
    paramView.setVisibility(0);
    ViewPropertyAnimatorCompat localViewPropertyAnimatorCompat = alphaAnimator(paramView, 200, 0.0F, 1.0F);
    if (localViewPropertyAnimatorCompat == null) {
      return;
    }
    localViewPropertyAnimatorCompat.setListener(null);
  }
  
  public static ViewPropertyAnimatorCompat rotateAnimation(View paramView, InterfaceOrientation paramInterfaceOrientation, int paramInt)
  {
    if ((paramView == null) || (paramInterfaceOrientation == null)) {
      return null;
    }
    int[] arrayOfInt = paramInterfaceOrientation.viewFromToDegree((int)ViewCompat.getRotation(paramView));
    ViewCompat.setRotation(paramView, arrayOfInt[0]);
    int i = paramInterfaceOrientation.viewDegree();
    ViewPropertyAnimatorCompat localViewPropertyAnimatorCompat = ViewCompat.animate(paramView).rotation(arrayOfInt[1]).setDuration(paramInt).setListener(new ViewPropertyAnimatorListenerAdapter()
    {
      public void onAnimationEnd(View paramAnonymousView)
      {
        ViewCompat.setRotation(paramAnonymousView, this.a);
      }
    });
    return localViewPropertyAnimatorCompat;
  }
  
  public static Animation offsetYPullAnimation(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    float f1 = paramBoolean1 ? 1.0F : 0.0F;
    float f2 = 1.0F - f1;
    float f3 = paramBoolean2 ? f1 - 1.0F : f2;
    float f4 = paramBoolean2 ? f2 - 1.0F : f1;
    AnimationSet localAnimationSet = new AnimationSet(true);
    localAnimationSet.setInterpolator(new AccelerateDecelerateInterpolator());
    localAnimationSet.setDuration(paramInt);
    localAnimationSet.setFillEnabled(true);
    localAnimationSet.setFillAfter(true);
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(f1, f2);
    localAnimationSet.addAnimation(localAlphaAnimation);
    TranslateAnimation localTranslateAnimation = new TranslateAnimation(1, 0.0F, 1, 0.0F, 1, f3, 1, f4);
    localAnimationSet.addAnimation(localTranslateAnimation);
    return localAnimationSet;
  }
  
  public static Animation scaleAlphaAnimation(int paramInt, boolean paramBoolean)
  {
    float f1 = paramBoolean ? 0.0F : 1.0F;
    float f2 = 1.0F - f1;
    AnimationSet localAnimationSet = new AnimationSet(true);
    localAnimationSet.setDuration(paramInt);
    localAnimationSet.setInterpolator(new AccelerateDecelerateInterpolator());
    localAnimationSet.setFillEnabled(true);
    localAnimationSet.setFillAfter(true);
    ScaleAnimation localScaleAnimation = new ScaleAnimation(f1, f2, f1, f2, 1, 0.5F, 1, 0.5F);
    localAnimationSet.addAnimation(localScaleAnimation);
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(f1, f2);
    localAnimationSet.addAnimation(localAlphaAnimation);
    return localAnimationSet;
  }
  
  public static void rotate3dView(View paramView, final int paramInt, final Animation.AnimationListener paramAnimationListener)
  {
    rotate3dView(paramView, paramInt / 2, 0.0F, 90.0F, true, new AnimationListenerAdapter()
    {
      public void onAnimationEnd(Animation paramAnonymousAnimation)
      {
        AnimHelper.rotate3dView(this.a, paramInt / 2, 90.0F, 180.0F, false, paramAnimationListener);
      }
    });
  }
  
  public static void rotate3dView(View paramView, int paramInt, float paramFloat1, float paramFloat2, boolean paramBoolean, Animation.AnimationListener paramAnimationListener)
  {
    if (paramView == null) {
      return;
    }
    Object localObject = ViewSize.create(paramView);
    float f1 = ((TuSdkSize)localObject).width * 0.5F;
    float f2 = ((TuSdkSize)localObject).height * 0.5F;
    if (((TuSdkSize)localObject).maxSide() <= 0) {
      localObject = ContextUtils.getScreenSize(paramView.getContext());
    }
    f1 = ((TuSdkSize)localObject).width / 2.0F;
    f2 = ((TuSdkSize)localObject).height / 2.0F;
    Rotate3dAnimation localRotate3dAnimation = new Rotate3dAnimation(paramFloat1, paramFloat2, f1, f2, f1 * 1.5F, paramBoolean);
    localRotate3dAnimation.setDuration(paramInt);
    localRotate3dAnimation.setFillEnabled(true);
    localRotate3dAnimation.setFillAfter(true);
    if (paramBoolean) {
      localRotate3dAnimation.setInterpolator(new AccelerateInterpolator(1.5F));
    } else {
      localRotate3dAnimation.setInterpolator(new DecelerateInterpolator(1.5F));
    }
    localRotate3dAnimation.setAnimationListener(paramAnimationListener);
    paramView.startAnimation(localRotate3dAnimation);
  }
  
  public static void rotate3dAnimtor(View paramView, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, Animator.AnimatorListener paramAnimatorListener)
  {
    if (paramView == null) {
      return;
    }
    AnimatorSet localAnimatorSet = new AnimatorSet();
    int i = paramInt / 3;
    ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(paramView, "scaleX", new float[] { paramFloat3, paramFloat4 }).setDuration(i);
    ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(paramView, "scaleY", new float[] { paramFloat3, paramFloat4 }).setDuration(i);
    ObjectAnimator localObjectAnimator3 = ObjectAnimator.ofFloat(paramView, "rotationY", new float[] { paramFloat1, paramFloat2 });
    localObjectAnimator3.setDuration(paramInt - i);
    localAnimatorSet.play(localObjectAnimator1).with(localObjectAnimator2);
    if (paramFloat3 > paramFloat4)
    {
      localAnimatorSet.play(localObjectAnimator3).after(localObjectAnimator2);
    }
    else
    {
      ViewCompat.setScaleX(paramView, paramFloat3);
      ViewCompat.setScaleY(paramView, paramFloat3);
      localAnimatorSet.play(localObjectAnimator3).before(localObjectAnimator1);
    }
    if (paramAnimatorListener != null) {
      localAnimatorSet.addListener(paramAnimatorListener);
    }
    localAnimatorSet.start();
  }
  
  public static void removeViewAnimtor(final View paramView, Animator.AnimatorListener paramAnimatorListener)
  {
    if (paramView == null)
    {
      if (paramAnimatorListener != null) {
        paramAnimatorListener.onAnimationEnd(null);
      }
      return;
    }
    final ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    final int i = paramView.getHeight();
    AnimatorSet localAnimatorSet = new AnimatorSet();
    ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(paramView, "translationX", new float[] { -paramView.getWidth() }).setDuration(200L);
    ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(paramView, "alpha", new float[] { 0.0F }).setDuration(150L);
    localAnimatorSet.play(localObjectAnimator1).with(localObjectAnimator2);
    ValueAnimator localValueAnimator = ValueAnimator.ofInt(new int[] { i, 0 }).setDuration(100L);
    localValueAnimator.addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        if (this.a != null) {
          this.a.onAnimationEnd(null);
        }
        ViewCompat.setAlpha(paramView, 1.0F);
        ViewCompat.setTranslationX(paramView, 0.0F);
        localLayoutParams.height = i;
        paramView.setLayoutParams(localLayoutParams);
      }
    });
    localValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        this.a.height = ((Integer)paramAnonymousValueAnimator.getAnimatedValue()).intValue();
        paramView.setLayoutParams(this.a);
      }
    });
    localAnimatorSet.play(localValueAnimator).after(localObjectAnimator2);
    localAnimatorSet.start();
  }
  
  public static void clear(View paramView)
  {
    if (paramView == null) {
      return;
    }
    Animation localAnimation = paramView.getAnimation();
    if (localAnimation != null) {
      localAnimation.setAnimationListener(null);
    }
    paramView.clearAnimation();
  }
  
  public static void startHeightAnim(View paramView, int paramInt1, int paramInt2)
  {
    if ((paramView == null) || (paramInt1 < 1) || (paramInt2 < 1)) {
      return;
    }
    paramView.clearAnimation();
    HeightAnimation localHeightAnimation = new HeightAnimation(paramView, paramInt1);
    localHeightAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
    localHeightAnimation.setDuration(paramInt2);
    paramView.startAnimation(localHeightAnimation);
  }
  
  public static class TuSdkViewAnimatorAdapter
    extends ViewPropertyAnimatorListenerAdapter
    implements ViewPropertyAnimatorUpdateListener
  {
    private boolean a;
    
    public void onAnimationEnd(View paramView, boolean paramBoolean) {}
    
    public void onAnimationEnd(View paramView)
    {
      onAnimationEnd(paramView, this.a);
    }
    
    public void onAnimationCancel(View paramView)
    {
      this.a = true;
    }
    
    public void onAnimationUpdate(View paramView) {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\anim\AnimHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */