// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff;
import android.view.View;
import android.graphics.Color;
import android.annotation.TargetApi;
import android.graphics.drawable.ColorDrawable;

public class ColorUtils
{
    public static void setColorFilter(final ColorDrawable colorDrawable, final float[] array) {
        if (colorDrawable == null) {
            return;
        }
        setColor(colorDrawable, filterColor(array, getColor(colorDrawable)));
    }
    
    @TargetApi(11)
    public static void setColor(final ColorDrawable colorDrawable, final int color) {
        if (colorDrawable == null) {
            return;
        }
        colorDrawable.setColor(color);
    }
    
    @TargetApi(11)
    public static int getColor(final ColorDrawable colorDrawable) {
        if (colorDrawable == null) {
            return 0;
        }
        return colorDrawable.getColor();
    }
    
    public static int filterColor(final float[] array, final int i) {
        if (array.length != 20) {
            return i;
        }
        final String format = String.format("%#010x", i);
        return Color.parseColor(String.format("#%s%02x%02x%02x", format.substring(2, 4), mergerColor(Integer.parseInt(format.substring(4, 6), 16), (int)array[4]), mergerColor(Integer.parseInt(format.substring(6, 8), 16), (int)array[9]), mergerColor(Integer.parseInt(format.substring(8, 10), 16), (int)array[14])));
    }
    
    public static int mergerColor(int n, final int n2) {
        n += n2;
        if (n < 0) {
            n = 0;
        }
        else if (n > 255) {
            n = 255;
        }
        return n;
    }
    
    public static void setBackgroudImageColor(final View view, final int n) {
        if (view == null || view.getBackground() == null) {
            return;
        }
        final Drawable background = view.getBackground();
        background.clearColorFilter();
        background.setColorFilter(n, PorterDuff.Mode.SRC_IN);
    }
    
    public static int alphaEvaluator(final float n, final int n2) {
        return (int)(255.0f * n) << 24 | (n2 >> 16 & 0xFF) << 16 | (n2 >> 8 & 0xFF) << 8 | (n2 & 0xFF);
    }
}
