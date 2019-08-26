// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim;

import android.view.animation.Transformation;
//import org.lasque.tusdk.core.struct.ViewSize;
import android.view.View;
import android.view.animation.Animation;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.ViewSize;

public class WidthAnimation extends Animation
{
    private float a;
    private float b;
    private float c;
    private View d;
    
    public WidthAnimation(final View view, final float n) {
        this(view, -1.0f, n);
    }
    
    public WidthAnimation(final View d, final float a, final float b) {
        this.d = d;
        this.a = a;
        this.b = b;
        if (this.a == -1.0f) {
            this.a = (float) ViewSize.create(d).width;
        }
        this.c = this.b - this.a;
    }
    
    public boolean willChangeBounds() {
        return true;
    }
    
    protected void applyTransformation(final float n, final Transformation transformation) {
        this.d.getLayoutParams().width = (int)(this.a + this.c * n);
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
}
