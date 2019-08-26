// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view;

import android.graphics.Canvas;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.utils.ContextUtils;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;

public class TuSdkViewDrawer
{
    private View a;
    private int b;
    private boolean c;
    private int d;
    private int e;
    private boolean f;
    private RectF g;
    private Path h;
    private Paint i;
    
    public int getCornerRadius() {
        return this.b;
    }
    
    public void setCornerRadius(final int b) {
        this.c = (this.b != b);
        this.b = b;
    }
    
    public void setCornerRadiusDP(final int n) {
        this.setCornerRadius(ContextUtils.dip2px(this.a.getContext(), (float)n));
    }
    
    public int getStrokeWidth() {
        return this.d;
    }
    
    public void setStrokeWidth(final int d) {
        this.f = (this.d != d);
        this.d = d;
    }
    
    public void setStrokeWidthDP(final int n) {
        this.setStrokeWidth(ContextUtils.dip2px(this.a.getContext(), (float)n));
    }
    
    public int getStrokeColor() {
        return this.e;
    }
    
    public void setStrokeColor(final int e) {
        this.f = (this.e != e);
        this.e = e;
    }
    
    public void setStrokeColorRes(final int n) {
        this.setStrokeColor(ContextUtils.getResColor(this.a.getContext(), n));
    }
    
    public void setStrokeColorRes(final String s) {
        this.setStrokeColor(TuSdkContext.getColor(s));
    }
    
    public void setStroke(final int strokeWidth, final int strokeColor) {
        this.setStrokeWidth(strokeWidth);
        this.setStrokeColor(strokeColor);
    }
    
    public void setStrokeDP(final int strokeWidthDP, final int strokeColor) {
        this.setStrokeWidthDP(strokeWidthDP);
        this.setStrokeColor(strokeColor);
    }
    
    public TuSdkViewDrawer(final View a) {
        this.g = new RectF();
        this.h = new Path();
        (this.i = new Paint(1)).setAntiAlias(true);
        this.a = a;
        if (this.a != null) {
            this.a.setLayerType(1, (Paint)null);
        }
    }
    
    public void invalidate() {
        this.a.invalidate();
    }
    
    public void postInvalidate() {
        this.a.postInvalidate();
    }
    
    public void dispatchDrawBefore(final Canvas canvas) {
        this.a(canvas);
    }
    
    public void dispatchDrawAfter(final Canvas canvas) {
        this.b(canvas);
    }
    
    private void a(final Canvas canvas) {
        if (!this.c) {
            return;
        }
        this.g.set(0.0f, 0.0f, (float)this.a.getMeasuredWidth(), (float)this.a.getMeasuredHeight());
        this.i.setColor(0);
        canvas.drawRect(this.g, this.i);
        this.h.reset();
        this.h.moveTo(0.0f, 0.0f);
        this.h.addRoundRect(this.g, (float)this.b, (float)this.b, Path.Direction.CW);
        this.h.close();
        canvas.clipPath(this.h);
        this.i.setColor(0);
        canvas.drawRect(this.g, this.i);
    }
    
    private void b(final Canvas canvas) {
        if (!this.f) {
            return;
        }
        this.g.set(0.0f, 0.0f, (float)this.a.getMeasuredWidth(), (float)this.a.getMeasuredHeight());
        this.i.setColor(this.e);
        this.i.setStrokeWidth((float)(this.d * 2));
        this.i.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(this.g, (float)this.b, (float)this.b, this.i);
    }
}
