// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.support.v4.view.ViewPager;

public class ZoomOutPageTransformer implements ViewPager.PageTransformer
{
    public void transformPage(final View view, final float a) {
        final int width = view.getWidth();
        final int height = view.getHeight();
        if (a < -1.0f) {
            ViewCompat.setAlpha(view, 0.0f);
        }
        else if (a <= 1.0f) {
            final float max = Math.max(0.85f, 1.0f - Math.abs(a));
            final float n = height * (1.0f - max) / 2.0f;
            final float n2 = width * (1.0f - max) / 2.0f;
            if (a < 0.0f) {
                ViewCompat.setTranslationX(view, n2 - n / 2.0f);
            }
            else {
                ViewCompat.setTranslationX(view, -n2 + n / 2.0f);
            }
            ViewCompat.setScaleX(view, max);
            ViewCompat.setScaleY(view, max);
            ViewCompat.setAlpha(view, 0.5f + (max - 0.85f) / 0.14999998f * 0.5f);
        }
        else {
            ViewCompat.setAlpha(view, 0.0f);
        }
    }
    
    protected void resetView(final View view) {
        ViewCompat.setAlpha(view, 1.0f);
        ViewCompat.setTranslationX(view, 0.0f);
        ViewCompat.setScaleX(view, 1.0f);
        ViewCompat.setScaleY(view, 1.0f);
    }
}
