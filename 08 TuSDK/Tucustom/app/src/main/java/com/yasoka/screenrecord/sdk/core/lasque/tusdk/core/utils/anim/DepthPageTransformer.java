// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.support.v4.view.ViewPager;

public class DepthPageTransformer implements ViewPager.PageTransformer
{
    public void transformPage(final View view, final float a) {
        final int width = view.getWidth();
        if (a < -1.0f) {
            ViewCompat.setAlpha(view, 0.0f);
        }
        else if (a <= 0.0f) {
            ViewCompat.setAlpha(view, 1.0f);
            ViewCompat.setTranslationX(view, 0.0f);
            ViewCompat.setScaleX(view, 1.0f);
            ViewCompat.setScaleY(view, 1.0f);
        }
        else if (a <= 1.0f) {
            ViewCompat.setAlpha(view, 1.0f - a);
            ViewCompat.setTranslationX(view, width * -a);
            final float n = 0.75f + 0.25f * (1.0f - Math.abs(a));
            ViewCompat.setScaleX(view, n);
            ViewCompat.setScaleY(view, n);
        }
        else {
            ViewCompat.setAlpha(view, 0.0f);
        }
    }
}
