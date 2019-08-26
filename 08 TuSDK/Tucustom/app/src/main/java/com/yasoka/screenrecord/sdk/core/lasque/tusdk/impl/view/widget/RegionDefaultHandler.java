// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget;

import android.animation.TypeEvaluator;
//import org.lasque.tusdk.core.utils.anim.RectFEvaluator;
import android.animation.ValueAnimator;
import android.animation.TimeInterpolator;
//import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.RectFEvaluator;
//import org.lasque.tusdk.core.struct.TuSdkSize;

public class RegionDefaultHandler implements RegionHandler
{
    private float a;
    private float b;
    private TuSdkSize c;
    private RectF d;
    private RectF e;
    private RegionChangeAnimation f;
    
    public RegionDefaultHandler() {
        this.b = -1.0f;
        this.c = TuSdkSize.create(0, 0);
        this.d = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        this.e = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
    }
    
    @Override
    public void setRatio(final float a) {
        this.a = a;
        if (this.a < 0.0f) {
            this.a = 0.0f;
        }
        this.d = this.recalculate(this.a, this.c);
        this.e = this.recalculateCenterRectPercent(this.a, this.c);
    }
    
    @Override
    public float getRatio() {
        return this.a;
    }
    
    @Override
    public void setOffsetTopPercent(final float b) {
        this.b = b;
    }
    
    @Override
    public float getOffsetTopPercent() {
        return this.b;
    }
    
    @Override
    public void setWrapSize(final TuSdkSize c) {
        this.c = c;
        if (this.c == null) {
            this.c = TuSdkSize.create(0, 0);
        }
        this.d = this.recalculate(this.a, this.c);
    }
    
    @Override
    public TuSdkSize getWrapSize() {
        return this.c;
    }
    
    @Override
    public RectF getRectPercent() {
        return this.d;
    }
    
    @Override
    public RectF getCenterRectPercent() {
        return this.e;
    }
    
    protected RectF recalculateCenterRectPercent(final float n, final TuSdkSize tuSdkSize) {
        if (n == 0.0f || tuSdkSize == null || !tuSdkSize.isSize()) {
            return new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        }
        final TuSdkSize create = TuSdkSize.create(tuSdkSize);
        create.width = (int)(tuSdkSize.height * n);
        final Rect rectWithAspectRatioInsideRect = RectHelper.makeRectWithAspectRatioInsideRect(create, new Rect(0, 0, tuSdkSize.width, tuSdkSize.height));
        return new RectF(rectWithAspectRatioInsideRect.left / (float)tuSdkSize.width, rectWithAspectRatioInsideRect.top / (float)tuSdkSize.height, rectWithAspectRatioInsideRect.right / (float)tuSdkSize.width, rectWithAspectRatioInsideRect.bottom / (float)tuSdkSize.height);
    }
    
    protected RectF recalculate(final float n, final TuSdkSize tuSdkSize) {
        final RectF recalculateCenterRectPercent = this.recalculateCenterRectPercent(n, tuSdkSize);
        if (this.b >= 0.0f && this.b <= 1.0f) {
            final float n2 = recalculateCenterRectPercent.top - this.b;
            final RectF rectF = recalculateCenterRectPercent;
            rectF.top -= n2;
            final RectF rectF2 = recalculateCenterRectPercent;
            rectF2.bottom -= n2;
        }
        return recalculateCenterRectPercent;
    }
    
    @Override
    public RectF changeWithRatio(final float ratio, final RegionChangerListener regionChangerListener) {
        if (ratio == this.getRatio()) {
            return this.getRectPercent();
        }
        this.a().setCurrent(this.getRectPercent());
        this.setRatio(ratio);
        this.a().startTo(this.getRectPercent(), regionChangerListener);
        return this.getRectPercent();
    }
    
    private RegionChangeAnimation a() {
        if (this.f == null) {
            (this.f = new RegionChangeAnimation(this.getRectPercent())).setDuration(260);
            this.f.setInterpolator((TimeInterpolator)new AccelerateDecelerateInterpolator());
        }
        this.f.cancel();
        return this.f;
    }
    
    private class RegionChangeAnimation implements ValueAnimator.AnimatorUpdateListener
    {
        private RectF b;
        private RectF c;
        private RegionChangerListener d;
        private ValueAnimator e;
        private int f;
        private TimeInterpolator g;
        
        public RegionChangeAnimation(final RectF b) {
            this.b = b;
        }
        
        public void cancel() {
            if (this.e != null) {
                this.e.cancel();
            }
            this.e = null;
        }
        
        public void setInterpolator(final TimeInterpolator g) {
            this.g = g;
        }
        
        public void setDuration(final int f) {
            this.f = f;
        }
        
        public void setCurrent(final RectF b) {
            this.b = b;
        }
        
        public void startTo(final RectF c, final RegionChangerListener d) {
            this.c = c;
            this.d = d;
            this.a();
        }
        
        private void a() {
            this.cancel();
            (this.e = ValueAnimator.ofObject((TypeEvaluator)new RectFEvaluator(), new Object[] { new RectF(this.b), this.c })).setDuration((long)this.f);
            this.e.setInterpolator(this.g);
            this.e.addUpdateListener((ValueAnimator.AnimatorUpdateListener)this);
            this.e.start();
        }
        
        public void onAnimationUpdate(final ValueAnimator valueAnimator) {
            this.b = (RectF)valueAnimator.getAnimatedValue();
            if (this.d != null) {
                this.d.onRegionChanged(this.b);
            }
        }
    }
}
