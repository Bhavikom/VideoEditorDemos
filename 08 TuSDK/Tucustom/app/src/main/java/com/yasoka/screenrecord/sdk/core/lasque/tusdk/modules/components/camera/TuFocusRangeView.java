// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.camera;

import android.view.animation.Transformation;
import android.support.v4.view.ViewCompat;
//import org.lasque.tusdk.core.utils.anim.AnimHelper;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.graphics.PointF;
//import org.lasque.tusdk.core.utils.ColorUtils;
//import org.lasque.tusdk.core.struct.ViewSize;
import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.TuSdkContext;
import android.os.Handler;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.ViewSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ColorUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AnimHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public class TuFocusRangeView extends TuSdkRelativeLayout implements TuFocusRangeViewInterface
{
    public static final float FocusRangeScale = 0.6f;
    private View a;
    private View b;
    private int c;
    private int d;
    private int e;
    private TuSdkSize f;
    private TuSdkSize g;
    private TuSdkSize h;
    private Handler i;
    private Runnable j;
    
    public static int getLayoutId() {
        return TuSdkContext.getLayoutResId("tusdk_impl_component_camera_focus_range_view");
    }
    
    public TuFocusRangeView(final Context context) {
        super(context);
        this.i = new Handler();
        this.j = new Runnable() {
            @Override
            public void run() {
                TuFocusRangeView.this.showViewIn(false);
            }
        };
    }
    
    public TuFocusRangeView(final Context context, final AttributeSet set) {
        super(context, set);
        this.i = new Handler();
        this.j = new Runnable() {
            @Override
            public void run() {
                TuFocusRangeView.this.showViewIn(false);
            }
        };
    }
    
    public TuFocusRangeView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.i = new Handler();
        this.j = new Runnable() {
            @Override
            public void run() {
                TuFocusRangeView.this.showViewIn(false);
            }
        };
    }
    
    public TuSdkSize getMaxRangeSize() {
        if (this.f == null) {
            this.f = ViewSize.create((View)this);
            this.getMinCrosshairSize();
        }
        return this.f;
    }
    
    public void setMaxRangeSize(final TuSdkSize f) {
        this.f = f;
    }
    
    public TuSdkSize getMinRangeSize() {
        if (this.g == null) {
            final TuSdkSize maxRangeSize = this.getMaxRangeSize();
            this.g = new TuSdkSize((int)Math.floor(maxRangeSize.width * 0.6f), (int)Math.floor(maxRangeSize.height * 0.6f));
        }
        return this.g;
    }
    
    public void setMinRangeSize(final TuSdkSize g) {
        this.g = g;
    }
    
    public TuSdkSize getMinCrosshairSize() {
        if (this.h == null) {
            this.h = ViewSize.create(this.getFocusCrosshair());
        }
        return this.h;
    }
    
    public void setMinCrosshairSize(final TuSdkSize h) {
        this.h = h;
    }
    
    @Override
    protected void initView() {
        super.initView();
        this.c = TuSdkContext.getColor("lsq_focus_normal");
        this.d = TuSdkContext.getColor("lsq_focus_succeed");
        this.e = TuSdkContext.getColor("lsq_focus_failed");
    }
    
    public View getFocusOutView() {
        if (this.a == null) {
            this.a = this.getViewById("lsq_range_wrap");
        }
        return this.a;
    }
    
    public View getFocusCrosshair() {
        if (this.b == null) {
            this.b = this.getViewById("lsq_crosshair");
        }
        return this.b;
    }
    
    public int getNormalColor() {
        return this.c;
    }
    
    public void setNormalColor(final int c) {
        this.c = c;
    }
    
    public int getSucceedColor() {
        return this.d;
    }
    
    public void setSucceedColor(final int d) {
        this.d = d;
    }
    
    public int getFailedColor() {
        return this.e;
    }
    
    public void setFailedColor(final int e) {
        this.e = e;
    }
    
    public void setDisplayColor(final int n) {
        ColorUtils.setBackgroudImageColor(this.getFocusOutView(), n);
        ColorUtils.setBackgroudImageColor(this.getFocusCrosshair(), n);
    }
    
    @Override
    public void setFoucsState(final boolean b) {
        this.i.postDelayed(this.j, 500L);
        this.setDisplayColor(b ? this.d : this.e);
    }
    
    @Override
    public void setPosition(final PointF pointF) {
        if (pointF == null) {
            return;
        }
        this.a(pointF);
        final CameraFocusAnimation cameraFocusAnimation = new CameraFocusAnimation();
        cameraFocusAnimation.setDuration(200L);
        cameraFocusAnimation.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        this.startAnimation((Animation)cameraFocusAnimation);
    }
    
    private void a(final PointF pointF) {
        this.i.removeCallbacks(this.j);
        AnimHelper.clear((View)this);
        ViewCompat.setAlpha(this.getFocusCrosshair(), 0.0f);
        this.setDisplayColor(this.c);
        this.showViewIn(true);
        this.b(pointF);
    }
    
    private void b(final PointF pointF) {
        final TuSdkSize maxRangeSize = this.getMaxRangeSize();
        final ViewSize create = ViewSize.create((View)this.getParent());
        float n = pointF.x - maxRangeSize.width * 0.5f;
        float n2 = pointF.y - maxRangeSize.height * 0.5f;
        if (n < 0.0f) {
            n = 0.0f;
        }
        else if (n + maxRangeSize.width > create.width) {
            n = (float)(create.width - maxRangeSize.width);
        }
        if (n2 < 0.0f) {
            n2 = 0.0f;
        }
        else if (n2 + maxRangeSize.height > create.height) {
            n2 = (float)(create.height - maxRangeSize.height);
        }
        this.setMargin((int)Math.floor(n), (int)Math.floor(n2), 0, 0);
        this.setSize(this.getFocusOutView(), maxRangeSize);
        this.setSize(this.getFocusCrosshair(), maxRangeSize);
    }
    
    private class CameraFocusAnimation extends Animation
    {
        private TuSdkSize b;
        private TuSdkSize c;
        
        public CameraFocusAnimation() {
            final TuSdkSize maxRangeSize = TuFocusRangeView.this.getMaxRangeSize();
            final TuSdkSize minRangeSize = TuFocusRangeView.this.getMinRangeSize();
            final TuSdkSize minCrosshairSize = TuFocusRangeView.this.getMinCrosshairSize();
            this.b = new TuSdkSize();
            this.b.width = maxRangeSize.width - minRangeSize.width;
            this.b.height = maxRangeSize.height - minRangeSize.height;
            this.c = new TuSdkSize();
            this.c.width = maxRangeSize.width - minCrosshairSize.width;
            this.c.height = maxRangeSize.height - minCrosshairSize.height;
        }
        
        public boolean willChangeBounds() {
            return true;
        }
        
        protected void applyTransformation(final float n, final Transformation transformation) {
            ViewCompat.setAlpha(TuFocusRangeView.this.getFocusCrosshair(), n);
            final TuSdkSize maxRangeSize = TuFocusRangeView.this.getMaxRangeSize();
            final TuSdkSize tuSdkSize = new TuSdkSize();
            tuSdkSize.width = (int)(maxRangeSize.width - n * this.b.width);
            tuSdkSize.height = (int)(maxRangeSize.height - n * this.b.height);
            final TuSdkSize tuSdkSize2 = new TuSdkSize();
            tuSdkSize2.width = (int)(maxRangeSize.width - n * this.c.width);
            tuSdkSize2.height = (int)(maxRangeSize.height - n * this.c.height);
            TuFocusRangeView.this.setSize(TuFocusRangeView.this.getFocusOutView(), tuSdkSize);
            TuFocusRangeView.this.setSize(TuFocusRangeView.this.getFocusCrosshair(), tuSdkSize2);
        }
    }
}
