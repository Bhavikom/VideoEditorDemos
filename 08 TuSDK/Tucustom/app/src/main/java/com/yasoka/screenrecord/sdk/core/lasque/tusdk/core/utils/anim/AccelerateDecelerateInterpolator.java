// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim;

import android.util.AttributeSet;
import android.content.Context;
import android.view.animation.Interpolator;

public class AccelerateDecelerateInterpolator implements Interpolator
{
    public AccelerateDecelerateInterpolator() {
    }
    
    public AccelerateDecelerateInterpolator(final Context context, final AttributeSet set) {
    }
    
    public float getInterpolation(final float n) {
        if (n < 0.4) {
            return n * n;
        }
        if (n >= 0.4 && n <= 0.6) {
            return (float)(3.4 * n - 1.2);
        }
        return 1.0f - (1.0f - n) * (1.0f - n);
    }
}
