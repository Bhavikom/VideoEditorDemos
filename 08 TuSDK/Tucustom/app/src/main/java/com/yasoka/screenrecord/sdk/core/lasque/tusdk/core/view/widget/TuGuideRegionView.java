// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.widget;

import android.graphics.PathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Canvas;
//import org.lasque.tusdk.core.struct.ViewSize;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.ViewTreeObserver;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.ViewSize;

public class TuGuideRegionView extends View
{
    protected boolean isLayouted;
    private ViewTreeObserver.OnPreDrawListener a;
    private RectF b;
    private boolean c;
    private int d;
    private int e;
    private float f;
    private float g;
    private Path h;
    private Paint i;
    
    public TuGuideRegionView(final Context context) {
        super(context);
        this.a = (ViewTreeObserver.OnPreDrawListener)new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                TuGuideRegionView.this.getViewTreeObserver().removeOnPreDrawListener(TuGuideRegionView.this.a);
                if (!TuGuideRegionView.this.isLayouted) {
                    TuGuideRegionView.this.isLayouted = true;
                }
                return false;
            }
        };
        this.c = false;
        this.d = -1711276033;
        this.e = 2;
        this.f = 8.0f;
        this.g = 4.0f;
        this.h = new Path();
        (this.i = new Paint(1)).setAntiAlias(true);
        this.initView();
    }
    
    public TuGuideRegionView(final Context context, final AttributeSet set) {
        super(context, set);
        this.a = (ViewTreeObserver.OnPreDrawListener)new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                TuGuideRegionView.this.getViewTreeObserver().removeOnPreDrawListener(TuGuideRegionView.this.a);
                if (!TuGuideRegionView.this.isLayouted) {
                    TuGuideRegionView.this.isLayouted = true;
                }
                return false;
            }
        };
        this.c = false;
        this.d = -1711276033;
        this.e = 2;
        this.f = 8.0f;
        this.g = 4.0f;
        this.h = new Path();
        (this.i = new Paint(1)).setAntiAlias(true);
        this.initView();
    }
    
    public TuGuideRegionView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.a = (ViewTreeObserver.OnPreDrawListener)new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                TuGuideRegionView.this.getViewTreeObserver().removeOnPreDrawListener(TuGuideRegionView.this.a);
                if (!TuGuideRegionView.this.isLayouted) {
                    TuGuideRegionView.this.isLayouted = true;
                }
                return false;
            }
        };
        this.c = false;
        this.d = -1711276033;
        this.e = 2;
        this.f = 8.0f;
        this.g = 4.0f;
        this.h = new Path();
        (this.i = new Paint(1)).setAntiAlias(true);
        this.initView();
    }
    
    protected void initView() {
        this.setLayerType(1, (Paint)null);
        this.a();
    }
    
    private void a() {
        this.getViewTreeObserver().addOnPreDrawListener(this.a);
    }
    
    public void setRegionPercent(final RectF b) {
        this.b = b;
        this.invalidate();
    }
    
    public RectF getRegionPercent() {
        if (this.b == null) {
            this.b = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        }
        return this.b;
    }
    
    public void setGuideLineViewState(final boolean c) {
        this.c = c;
        this.invalidate();
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        if (b) {
            this.setRegionPercent(this.getRegionPercent());
        }
    }
    
    private RectF b() {
        final ViewSize create = ViewSize.create(this);
        return new RectF(this.getRegionPercent().left * create.width, this.getRegionPercent().top * create.height, this.getRegionPercent().right * create.width, this.getRegionPercent().bottom * create.height);
    }
    
    public int getGuideLineWidth() {
        return this.e;
    }
    
    public void setGuideLineWidth(final int e) {
        this.e = e;
    }
    
    public float getGuideLineHeight() {
        return this.f;
    }
    
    public void setGuideLineHeight(final float f) {
        this.f = f;
    }
    
    public float getGuideLineOffset() {
        return this.g;
    }
    
    public void setGuideLineOffset(final float g) {
        this.g = g;
    }
    
    public int getGuideLineColor() {
        return this.d;
    }
    
    public void setGuideLineColor(final int d) {
        this.d = d;
    }
    
    protected void onDraw(final Canvas canvas) {
        this.a(canvas, this.b());
        super.onDraw(canvas);
    }
    
    private void a(final Canvas canvas, final RectF rectF) {
        if (rectF == null || canvas == null || !this.c) {
            return;
        }
        this.h.reset();
        this.h.addRect(rectF.left, rectF.top, rectF.right, rectF.bottom, Path.Direction.CW);
        this.h.close();
        this.i.setStyle(Paint.Style.STROKE);
        this.i.setColor(this.getGuideLineColor());
        this.i.setStrokeWidth((float)this.getGuideLineWidth());
        this.i.setPathEffect((PathEffect)new DashPathEffect(new float[] { this.getGuideLineHeight(), this.getGuideLineOffset() }, 2.0f));
        final float n = this.getGuideLineWidth() * 0.5f;
        canvas.drawLine(rectF.left + rectF.width() / 3.0f + n, rectF.top, rectF.left + rectF.width() / 3.0f + n, rectF.bottom, this.i);
        canvas.drawLine(rectF.left + rectF.width() * 2.0f / 3.0f + n, rectF.top, rectF.left + rectF.width() * 2.0f / 3.0f + n, rectF.bottom, this.i);
        canvas.drawLine(rectF.left, rectF.top + rectF.height() / 3.0f + n, rectF.right, rectF.top + rectF.height() / 3.0f + n, this.i);
        canvas.drawLine(rectF.left, rectF.top + rectF.height() * 2.0f / 3.0f + n, rectF.right, rectF.top + rectF.height() * 2.0f / 3.0f + n, this.i);
    }
}
