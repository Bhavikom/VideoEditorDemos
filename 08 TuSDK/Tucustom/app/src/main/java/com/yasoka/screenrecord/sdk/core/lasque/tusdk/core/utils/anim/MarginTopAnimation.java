// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim;

import android.view.animation.Interpolator;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.view.View;
import android.view.animation.Animation;

public class MarginTopAnimation extends Animation
{
    private float a;
    private float b;
    private float c;
    private View d;
    
    public MarginTopAnimation(final View d, final float a, final float b) {
        this.d = d;
        this.a = a;
        this.b = b;
        this.c = this.b - this.a;
    }
    
    public boolean willChangeBounds() {
        return true;
    }
    
    protected void applyTransformation(final float n, final Transformation transformation) {
        this.setMarginTop((int)(this.a + this.c * n));
    }
    
    protected void setMarginTop(final int topMargin) {
        final ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)this.d.getLayoutParams();
        if (marginLayoutParams == null) {
            return;
        }
        marginLayoutParams.topMargin = topMargin;
        this.d.requestLayout();
    }
    
    protected void finalize() {
        this.d = null;
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    public static void showTopView(final View view, final long duration, final boolean b) {
        if (view == null || view.getLayoutParams() == null) {
            return;
        }
        view.clearAnimation();
        final int n = b ? (-view.getHeight()) : 0;
        final MarginTopAnimation marginTopAnimation = new MarginTopAnimation(view, (float)n, (float)(-view.getHeight() - n));
        marginTopAnimation.setDuration(duration);
        marginTopAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        view.startAnimation((Animation)marginTopAnimation);
    }
}
