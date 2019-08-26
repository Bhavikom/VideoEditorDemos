// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget;

import android.view.animation.Transformation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
//import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import android.graphics.Region;
import android.graphics.Canvas;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.utils.RectHelper;
//import org.lasque.tusdk.core.struct.ViewSize;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Rect;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.view.ViewTreeObserver;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.ViewSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;

public class TuMaskRegionView extends View
{
    protected boolean isLayouted;
    private ViewTreeObserver.OnPreDrawListener a;
    private TuSdkSize b;
    private float c;
    private Rect d;
    private int e;
    private int f;
    private int g;
    private boolean h;
    private RectF i;
    private Path j;
    private Paint k;
    private RegionChangeAnimation l;
    
    public TuMaskRegionView(final Context context) {
        super(context);
        this.a = (ViewTreeObserver.OnPreDrawListener)new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                TuMaskRegionView.this.getViewTreeObserver().removeOnPreDrawListener(TuMaskRegionView.this.a);
                if (!TuMaskRegionView.this.isLayouted) {
                    TuMaskRegionView.this.isLayouted = true;
                    TuMaskRegionView.this.onLayouted();
                }
                return false;
            }
        };
        this.c = 0.0f;
        this.e = 0;
        this.f = 0;
        this.i = new RectF();
        this.j = new Path();
        (this.k = new Paint(1)).setAntiAlias(true);
        this.initView();
    }
    
    public TuMaskRegionView(final Context context, final AttributeSet set) {
        super(context, set);
        this.a = (ViewTreeObserver.OnPreDrawListener)new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                TuMaskRegionView.this.getViewTreeObserver().removeOnPreDrawListener(TuMaskRegionView.this.a);
                if (!TuMaskRegionView.this.isLayouted) {
                    TuMaskRegionView.this.isLayouted = true;
                    TuMaskRegionView.this.onLayouted();
                }
                return false;
            }
        };
        this.c = 0.0f;
        this.e = 0;
        this.f = 0;
        this.i = new RectF();
        this.j = new Path();
        (this.k = new Paint(1)).setAntiAlias(true);
        this.initView();
    }
    
    public TuMaskRegionView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.a = (ViewTreeObserver.OnPreDrawListener)new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                TuMaskRegionView.this.getViewTreeObserver().removeOnPreDrawListener(TuMaskRegionView.this.a);
                if (!TuMaskRegionView.this.isLayouted) {
                    TuMaskRegionView.this.isLayouted = true;
                    TuMaskRegionView.this.onLayouted();
                }
                return false;
            }
        };
        this.c = 0.0f;
        this.e = 0;
        this.f = 0;
        this.i = new RectF();
        this.j = new Path();
        (this.k = new Paint(1)).setAntiAlias(true);
        this.initView();
    }
    
    protected void initView() {
        this.setLayerType(1, (Paint)null);
        this.a();
    }
    
    private void a() {
        this.getViewTreeObserver().addOnPreDrawListener(this.a);
    }
    
    protected void onLayouted() {
        if (this.h) {
            this.a(this.a(this.getRegionRatio()));
            this.h = false;
        }
    }
    
    public TuSdkSize getRegionSize() {
        return this.b;
    }
    
    public void setRegionSize(final TuSdkSize b) {
        this.b = b;
        if (b != null) {
            this.c = b.width / (float)b.height;
            this.autoShowForRegionRatio();
            this.a(this.a(this.c));
        }
    }
    
    public float getRegionRatio() {
        return this.c;
    }
    
    public Rect setRegionRatio(final float c) {
        this.c = c;
        final Rect a = this.a(this.c);
        if (this.isLayouted) {
            this.a(a);
        }
        else {
            this.h = true;
        }
        this.autoShowForRegionRatio();
        return a;
    }
    
    public void autoShowForRegionRatio() {
        if (this.c <= 0.0f) {
            ViewCompat.setAlpha((View)this, 0.0f);
        }
        else if (ViewCompat.getAlpha((View)this) == 0.0f) {
            ViewCompat.setAlpha((View)this, 1.0f);
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        if (b) {
            this.setRegionRatio(this.getRegionRatio());
        }
    }
    
    private void a(final Rect d) {
        this.d = d;
        this.invalidate();
    }
    
    private Rect a(final float n) {
        return RectHelper.computerCenter(ViewSize.create(this), n);
    }
    
    public Rect getRegionRect() {
        return this.d;
    }
    
    public int getEdgeMaskColor() {
        return this.e;
    }
    
    public void setEdgeMaskColor(final int e) {
        this.e = e;
    }
    
    public int getEdgeSideColor() {
        return this.f;
    }
    
    public void setEdgeSideColor(final int f) {
        this.f = f;
    }
    
    public int getEdgeSideWidth() {
        return this.g;
    }
    
    public void setEdgeSideWidth(final int g) {
        this.g = g;
    }
    
    public void setEdgeSideWidthDP(final int n) {
        this.g = ContextUtils.dip2px(this.getContext(), (float)n);
    }
    
    protected void onDraw(final Canvas canvas) {
        this.a(canvas, this.getRegionRect());
        super.onDraw(canvas);
    }
    
    private void a(final Canvas canvas, final Rect rect) {
        if (rect == null || canvas == null) {
            return;
        }
        this.i.set(0.0f, 0.0f, (float)this.getMeasuredWidth(), (float)this.getMeasuredHeight());
        if (this.getEdgeSideWidth() > 0) {
            this.k.setColor(this.getEdgeSideColor());
            this.k.setStrokeWidth((float)this.getEdgeSideWidth());
            this.k.setStyle(Paint.Style.STROKE);
            final float n = this.getEdgeSideWidth() * 0.5f;
            canvas.drawRect(new RectF(rect.left + n, rect.top + n, rect.right - n, rect.bottom - n), this.k);
        }
        this.j.reset();
        this.j.addRect((float)rect.left, (float)rect.top, (float)rect.right, (float)rect.bottom, Path.Direction.CW);
        this.j.close();
        canvas.clipPath(this.j, Region.Op.DIFFERENCE);
        this.k.setColor(this.getEdgeMaskColor());
        this.k.setStyle(Paint.Style.FILL);
        canvas.drawRect(this.i, this.k);
    }
    
    public Rect changeRegionRatio(final float c) {
        final Rect a = this.a(c);
        if (this.c == c) {
            return a;
        }
        if (c <= 0.0f) {
            ViewCompat.animate((View)this).alpha(0.0f).setDuration(260L).setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        }
        else if (ViewCompat.getAlpha((View)this) == 0.0f) {
            ViewCompat.animate((View)this).alpha(1.0f).setDuration(260L).setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        }
        this.b().startTo(a);
        this.startAnimation((Animation)this.b());
        this.c = c;
        return a;
    }
    
    private RegionChangeAnimation b() {
        if (this.l == null) {
            (this.l = new RegionChangeAnimation(this.getRegionRatio())).setDuration(260L);
            this.l.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        }
        this.l.cancel();
        this.l.reset();
        return this.l;
    }
    
    private class RegionChangeAnimation extends Animation
    {
        private Rect b;
        private Rect c;
        private Rect d;
        
        public RegionChangeAnimation(final float n) {
            this.b = TuMaskRegionView.this.a(n);
        }
        
        public void startTo(final Rect c) {
            this.c = c;
            this.d = new Rect(this.c.left - this.b.left, this.c.top - this.b.top, this.c.right - this.b.right, this.c.bottom - this.b.bottom);
        }
        
        protected void applyTransformation(float n, final Transformation transformation) {
            n = 1.0f - n;
            this.b.left = this.c.left - (int)(this.d.left * n);
            this.b.top = this.c.top - (int)(this.d.top * n);
            this.b.right = this.c.right - (int)(this.d.right * n);
            this.b.bottom = this.c.bottom - (int)(this.d.bottom * n);
            TuMaskRegionView.this.a(this.b);
        }
    }
}
