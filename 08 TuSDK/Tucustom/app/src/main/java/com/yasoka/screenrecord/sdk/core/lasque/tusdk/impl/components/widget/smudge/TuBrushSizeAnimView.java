// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.smudge;

//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.view.animation.Transformation;
import android.support.v4.view.ViewPropertyAnimatorListener;
//import org.lasque.tusdk.core.utils.anim.AnimHelper;
import android.view.animation.Interpolator;
//import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import android.graphics.Canvas;
import android.view.animation.Animation;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.content.Context;
import android.view.ViewTreeObserver;
import android.graphics.Paint;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AnimHelper;

public class TuBrushSizeAnimView extends View
{
    protected boolean isLayouted;
    private boolean a;
    private int b;
    private Paint c;
    private int d;
    private int e;
    private ViewTreeObserver.OnPreDrawListener f;
    private SizeChangeAnimation g;
    private Runnable h;
    
    public TuBrushSizeAnimView(final Context context) {
        super(context);
        (this.c = new Paint(1)).setAntiAlias(true);
        this.d = -1;
        this.e = 2;
        this.f = (ViewTreeObserver.OnPreDrawListener)new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                TuBrushSizeAnimView.this.getViewTreeObserver().removeOnPreDrawListener(TuBrushSizeAnimView.this.f);
                if (!TuBrushSizeAnimView.this.isLayouted) {
                    TuBrushSizeAnimView.this.isLayouted = true;
                    TuBrushSizeAnimView.this.onLayouted();
                }
                return false;
            }
        };
        this.h = new Runnable() {
            @Override
            public void run() {
                TuBrushSizeAnimView.this.c();
            }
        };
        this.initView();
    }
    
    public TuBrushSizeAnimView(final Context context, final AttributeSet set) {
        super(context, set);
        (this.c = new Paint(1)).setAntiAlias(true);
        this.d = -1;
        this.e = 2;
        this.f = (ViewTreeObserver.OnPreDrawListener)new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                TuBrushSizeAnimView.this.getViewTreeObserver().removeOnPreDrawListener(TuBrushSizeAnimView.this.f);
                if (!TuBrushSizeAnimView.this.isLayouted) {
                    TuBrushSizeAnimView.this.isLayouted = true;
                    TuBrushSizeAnimView.this.onLayouted();
                }
                return false;
            }
        };
        this.h = new Runnable() {
            @Override
            public void run() {
                TuBrushSizeAnimView.this.c();
            }
        };
        this.initView();
    }
    
    public TuBrushSizeAnimView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        (this.c = new Paint(1)).setAntiAlias(true);
        this.d = -1;
        this.e = 2;
        this.f = (ViewTreeObserver.OnPreDrawListener)new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                TuBrushSizeAnimView.this.getViewTreeObserver().removeOnPreDrawListener(TuBrushSizeAnimView.this.f);
                if (!TuBrushSizeAnimView.this.isLayouted) {
                    TuBrushSizeAnimView.this.isLayouted = true;
                    TuBrushSizeAnimView.this.onLayouted();
                }
                return false;
            }
        };
        this.h = new Runnable() {
            @Override
            public void run() {
                TuBrushSizeAnimView.this.c();
            }
        };
        this.initView();
    }
    
    protected void initView() {
        this.setLayerType(1, (Paint)null);
        this.a();
    }
    
    private void a() {
        this.getViewTreeObserver().addOnPreDrawListener(this.f);
    }
    
    protected void onLayouted() {
        if (this.a) {
            this.a(this.getRadius());
            this.a = false;
        }
    }
    
    public int getBorderColor() {
        return this.d;
    }
    
    public void setBorderColor(final int d) {
        this.d = d;
    }
    
    public int getBorderWidth() {
        return this.e;
    }
    
    public void setBorderWidth(final int e) {
        this.e = e;
    }
    
    private void a(final int b) {
        this.b = b;
        this.invalidate();
    }
    
    public int getRadius() {
        return this.b;
    }
    
    public void changeRadius(final int b, final int b2) {
        if (this.b == b2) {
            return;
        }
        this.b = b;
        ViewCompat.setAlpha((View)this, 1.0f);
        this.b().start(this.b, b2);
        this.startAnimation((Animation)this.b());
        this.b = b2;
    }
    
    private void b(final int n) {
        this.a(n);
    }
    
    protected void onDraw(final Canvas canvas) {
        this.a(canvas, this.getRadius());
        super.onDraw(canvas);
    }
    
    private SizeChangeAnimation b() {
        if (this.g == null) {
            (this.g = new SizeChangeAnimation()).setDuration(260L);
            this.g.setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        }
        this.g.cancel();
        this.g.reset();
        return this.g;
    }
    
    private void c() {
        ViewCompat.animate((View)this).alpha(0.0f).setDuration(200L).setListener((ViewPropertyAnimatorListener)new AnimHelper.TuSdkViewAnimatorAdapter() {
            @Override
            public void onAnimationEnd(final View view, final boolean b) {
                if (b) {
                    return;
                }
                view.setVisibility(INVISIBLE);
            }
        });
    }
    
    private void a(final Canvas canvas, final int n) {
        if (canvas == null || n <= 0) {
            return;
        }
        final int measuredWidth = this.getMeasuredWidth();
        final int measuredHeight = this.getMeasuredHeight();
        this.c.setColor(this.getBorderColor());
        this.c.setStrokeWidth((float)this.getBorderWidth());
        this.c.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle((float)(measuredWidth / 2), (float)(measuredHeight / 2), (float)n, this.c);
    }
    
    private class SizeChangeAnimation extends Animation
    {
        private int b;
        private int c;
        
        public SizeChangeAnimation() {
        }
        
        public void start(final int n, final int b) {
            this.b = b;
            this.c = b - n;
        }
        
        protected void applyTransformation(float n, final Transformation transformation) {
            n = 1.0f - n;
            TuBrushSizeAnimView.this.b(this.b - (int)(this.c * n));
            if (n <= 0.0f) {
                TuBrushSizeAnimView.this.b().cancel();
                ThreadHelper.postDelayed(TuBrushSizeAnimView.this.h, 500L);
            }
        }
    }
}
