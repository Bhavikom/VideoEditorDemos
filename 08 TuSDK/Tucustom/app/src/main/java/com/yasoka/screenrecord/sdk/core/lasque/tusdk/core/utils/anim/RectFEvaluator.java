// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim;

import android.graphics.RectF;
import android.animation.TypeEvaluator;

public class RectFEvaluator implements TypeEvaluator<RectF>
{
    public RectF evaluate(final float n, final RectF rectF, final RectF rectF2) {
        return new RectF(rectF.left + (rectF2.left - rectF.left) * n, rectF.top + (rectF2.top - rectF.top) * n, rectF.right + (rectF2.right - rectF.right) * n, rectF.bottom + (rectF2.bottom - rectF.bottom) * n);
    }
}
