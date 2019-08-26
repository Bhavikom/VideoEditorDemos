// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim;

import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.view.ViewGroup;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.animation.Animator;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.struct.ViewSize;
//import org.lasque.tusdk.core.listener.AnimationListenerAdapter;
import android.view.animation.ScaleAnimation;
import android.view.animation.AnimationSet;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.animation.AlphaAnimation;
import android.view.animation.Interpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.listener.AnimationListenerAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.ViewSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;

public class AnimHelper
{
    public static Animation getResAnima(final Context context, final int n) {
        if (context == null || n == 0) {
            return null;
        }
        return AnimationUtils.loadAnimation(context, n);
    }
    
    public static void clearAnimation(final View view) {
        if (view == null) {
            return;
        }
        view.clearAnimation();
    }
    
    public static Animation shakeAnimation(final int n) {
        final TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 10.0f, 0.0f, 0.0f);
        ((Animation)translateAnimation).setInterpolator((Interpolator)new CycleInterpolator((float)n));
        ((Animation)translateAnimation).setDuration(300L);
        return (Animation)translateAnimation;
    }
    
    public static Animation heightAnimation(final View view, final int n) {
        if (view == null) {
            return null;
        }
        final HeightAnimation heightAnimation = new HeightAnimation(view, (float)n);
        heightAnimation.setDuration(260L);
        heightAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        view.startAnimation((Animation)heightAnimation);
        return heightAnimation;
    }
    
    public static Animation alphAnimation(final boolean b, final int n) {
        final float n2 = b ? 1.0f : 0.0f;
        final AlphaAnimation alphaAnimation = new AlphaAnimation(n2, 1.0f - n2);
        ((Animation)alphaAnimation).setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        ((Animation)alphaAnimation).setDuration((long)n);
        return (Animation)alphaAnimation;
    }
    
    public static ViewPropertyAnimatorCompat alphaAnimator(final View view, final int n, final float n2, final float n3) {
        if (view == null) {
            return null;
        }
        clearAnimation(view);
        ViewCompat.setAlpha(view, n2);
        return ViewCompat.animate(view).alpha(n3).setDuration((long)n).setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
    }
    
    public static void alphaHidden(final View view) {
        final ViewPropertyAnimatorCompat alphaAnimator = alphaAnimator(view, 200, 1.0f, 0.0f);
        if (alphaAnimator == null) {
            return;
        }
        alphaAnimator.setListener((ViewPropertyAnimatorListener)new ViewPropertyAnimatorListenerAdapter() {
            public void onAnimationEnd(final View view) {
                view.setVisibility(4);
            }
        });
    }
    
    public static void alphaShow(final View view) {
        view.setVisibility(0);
        final ViewPropertyAnimatorCompat alphaAnimator = alphaAnimator(view, 200, 0.0f, 1.0f);
        if (alphaAnimator == null) {
            return;
        }
        alphaAnimator.setListener((ViewPropertyAnimatorListener)null);
    }
    
    public static ViewPropertyAnimatorCompat rotateAnimation(final View view, final InterfaceOrientation interfaceOrientation, final int n) {
        if (view == null || interfaceOrientation == null) {
            return null;
        }
        final int[] viewFromToDegree = interfaceOrientation.viewFromToDegree((int)ViewCompat.getRotation(view));
        ViewCompat.setRotation(view, (float)viewFromToDegree[0]);
        return ViewCompat.animate(view).rotation((float)viewFromToDegree[1]).setDuration((long)n).setListener((ViewPropertyAnimatorListener)new ViewPropertyAnimatorListenerAdapter() {
            final /* synthetic */ int a = interfaceOrientation.viewDegree();
            
            public void onAnimationEnd(final View view) {
                ViewCompat.setRotation(view, (float)this.a);
            }
        });
    }
    
    public static Animation offsetYPullAnimation(final int n, final boolean b, final boolean b2) {
        final float n2 = b ? 1.0f : 0.0f;
        final float n3 = 1.0f - n2;
        final float n4 = b2 ? (n2 - 1.0f) : n3;
        final float n5 = b2 ? (n3 - 1.0f) : n2;
        final AnimationSet set = new AnimationSet(true);
        set.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        set.setDuration((long)n);
        set.setFillEnabled(true);
        set.setFillAfter(true);
        set.addAnimation((Animation)new AlphaAnimation(n2, n3));
        set.addAnimation((Animation)new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, n4, 1, n5));
        return (Animation)set;
    }
    
    public static Animation scaleAlphaAnimation(final int n, final boolean b) {
        final float n2 = b ? 0.0f : 1.0f;
        final float n3 = 1.0f - n2;
        final AnimationSet set = new AnimationSet(true);
        set.setDuration((long)n);
        set.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        set.setFillEnabled(true);
        set.setFillAfter(true);
        set.addAnimation((Animation)new ScaleAnimation(n2, n3, n2, n3, 1, 0.5f, 1, 0.5f));
        set.addAnimation((Animation)new AlphaAnimation(n2, n3));
        return (Animation)set;
    }
    
    public static void rotate3dView(final View view, final int n, final Animation.AnimationListener animationListener) {
        rotate3dView(view, n / 2, 0.0f, 90.0f, true, (Animation.AnimationListener)new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animation animation) {
                AnimHelper.rotate3dView(view, n / 2, 90.0f, 180.0f, false, animationListener);
            }
        });
    }
    
    public static void rotate3dView(final View view, final int n, final float n2, final float n3, final boolean b, final Animation.AnimationListener animationListener) {
        if (view == null) {
            return;
        }
        TuSdkSize tuSdkSize = ViewSize.create(view);
        final float n4 = tuSdkSize.width * 0.5f;
        final float n5 = tuSdkSize.height * 0.5f;
        if (tuSdkSize.maxSide() <= 0) {
            tuSdkSize = ContextUtils.getScreenSize(view.getContext());
        }
        final float n6 = tuSdkSize.width / 2.0f;
        final Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(n2, n3, n6, tuSdkSize.height / 2.0f, n6 * 1.5f, b);
        rotate3dAnimation.setDuration((long)n);
        rotate3dAnimation.setFillEnabled(true);
        rotate3dAnimation.setFillAfter(true);
        if (b) {
            rotate3dAnimation.setInterpolator((Interpolator)new AccelerateInterpolator(1.5f));
        }
        else {
            rotate3dAnimation.setInterpolator((Interpolator)new DecelerateInterpolator(1.5f));
        }
        rotate3dAnimation.setAnimationListener(animationListener);
        view.startAnimation((Animation)rotate3dAnimation);
    }
    
    public static void rotate3dAnimtor(final View view, final int n, final float n2, final float n3, final float n4, final float n5, final Animator.AnimatorListener animatorListener) {
        if (view == null) {
            return;
        }
        final AnimatorSet set = new AnimatorSet();
        final int n6 = n / 3;
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)view, "scaleX", new float[] { n4, n5 }).setDuration((long)n6);
        final ObjectAnimator setDuration2 = ObjectAnimator.ofFloat((Object)view, "scaleY", new float[] { n4, n5 }).setDuration((long)n6);
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)view, "rotationY", new float[] { n2, n3 });
        ofFloat.setDuration((long)(n - n6));
        set.play((Animator)setDuration).with((Animator)setDuration2);
        if (n4 > n5) {
            set.play((Animator)ofFloat).after((Animator)setDuration2);
        }
        else {
            ViewCompat.setScaleX(view, n4);
            ViewCompat.setScaleY(view, n4);
            set.play((Animator)ofFloat).before((Animator)setDuration);
        }
        if (animatorListener != null) {
            set.addListener(animatorListener);
        }
        set.start();
    }
    
    public static void removeViewAnimtor(final View view, final Animator.AnimatorListener animatorListener) {
        if (view == null) {
            if (animatorListener != null) {
                animatorListener.onAnimationEnd((Animator)null);
            }
            return;
        }
        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        final int height = view.getHeight();
        final AnimatorSet set = new AnimatorSet();
        final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)view, "translationX", new float[] { (float)(-view.getWidth()) }).setDuration(200L);
        final ObjectAnimator setDuration2 = ObjectAnimator.ofFloat((Object)view, "alpha", new float[] { 0.0f }).setDuration(150L);
        set.play((Animator)setDuration).with((Animator)setDuration2);
        final ValueAnimator setDuration3 = ValueAnimator.ofInt(new int[] { height, 0 }).setDuration(100L);
        setDuration3.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                if (animatorListener != null) {
                    animatorListener.onAnimationEnd((Animator)null);
                }
                ViewCompat.setAlpha(view, 1.0f);
                ViewCompat.setTranslationX(view, 0.0f);
                layoutParams.height = height;
                view.setLayoutParams(layoutParams);
            }
        });
        setDuration3.addUpdateListener((ValueAnimator.AnimatorUpdateListener)new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                layoutParams.height = (int)valueAnimator.getAnimatedValue();
                view.setLayoutParams(layoutParams);
            }
        });
        set.play((Animator)setDuration3).after((Animator)setDuration2);
        set.start();
    }
    
    public static void clear(final View view) {
        if (view == null) {
            return;
        }
        final Animation animation = view.getAnimation();
        if (animation != null) {
            animation.setAnimationListener((Animation.AnimationListener)null);
        }
        view.clearAnimation();
    }
    
    public static void startHeightAnim(final View view, final int n, final int n2) {
        if (view == null || n < 1 || n2 < 1) {
            return;
        }
        view.clearAnimation();
        final HeightAnimation heightAnimation = new HeightAnimation(view, (float)n);
        heightAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        heightAnimation.setDuration((long)n2);
        view.startAnimation((Animation)heightAnimation);
    }
    
    public static class TuSdkViewAnimatorAdapter extends ViewPropertyAnimatorListenerAdapter implements ViewPropertyAnimatorUpdateListener
    {
        private boolean a;
        
        public void onAnimationEnd(final View view, final boolean b) {
        }
        
        public void onAnimationEnd(final View view) {
            this.onAnimationEnd(view, this.a);
        }
        
        public void onAnimationCancel(final View view) {
            this.a = true;
        }
        
        public void onAnimationUpdate(final View view) {
        }
    }
}
