// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.OverScroller;
import android.widget.Scroller;

public class TouchImageView extends AppCompatImageView
{
    private float a;
    private Matrix b;
    private Matrix c;
    private State d;
    private float e;
    private float f;
    private float g;
    private float h;
    private float[] i;
    private Context j;
    private Fling k;
    private ScaleType l;
    private boolean m;
    private boolean n;
    private ZoomVariables o;
    private int p;
    private int q;
    private int r;
    private int s;
    private float t;
    private float u;
    private float v;
    private float w;
    private ScaleGestureDetector x;
    private GestureDetector y;
    private GestureDetector.OnDoubleTapListener z;
    private OnTouchListener A;
    private OnTouchImageViewListener B;
    
    public TouchImageView(final Context context) {
        super(context);
        this.z = null;
        this.A = null;
        this.B = null;
        this.a(context);
    }
    
    public TouchImageView(final Context context, final AttributeSet set) {
        super(context, set);
        this.z = null;
        this.A = null;
        this.B = null;
        this.a(context);
    }
    
    public TouchImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.z = null;
        this.A = null;
        this.B = null;
        this.a(context);
    }
    
    private void a(final Context j) {
        super.setClickable(true);
        this.j = j;
        this.x = new ScaleGestureDetector(j, (ScaleGestureDetector.OnScaleGestureListener)new ScaleListener());
        this.y = new GestureDetector(j, (GestureDetector.OnGestureListener)new GestureListener());
        this.b = new Matrix();
        this.c = new Matrix();
        this.i = new float[9];
        this.a = 1.0f;
        if (this.l == null) {
            this.l = ScaleType.FIT_CENTER;
        }
        this.e = 1.0f;
        this.f = 3.0f;
        this.g = 0.75f * this.e;
        this.h = 1.25f * this.f;
        this.setImageMatrix(this.b);
        this.setScaleType(ScaleType.MATRIX);
        this.a(State.NONE);
        this.n = false;
        super.setOnTouchListener((OnTouchListener)new PrivateOnTouchListener());
    }
    
    public void setOnTouchListener(final OnTouchListener a) {
        this.A = a;
    }
    
    public void setOnTouchImageViewListener(final OnTouchImageViewListener b) {
        this.B = b;
    }
    
    public void setOnDoubleTapListener(final GestureDetector.OnDoubleTapListener z) {
        this.z = z;
    }
    
    public void setImageResource(final int imageResource) {
        super.setImageResource(imageResource);
        this.a();
        this.f();
    }
    
    public void setImageBitmap(final Bitmap imageBitmap) {
        super.setImageBitmap(imageBitmap);
        this.a();
        this.f();
    }
    
    public void setImageDrawable(final Drawable imageDrawable) {
        super.setImageDrawable(imageDrawable);
        this.a();
        this.f();
    }
    
    public void setImageURI(final Uri imageURI) {
        super.setImageURI(imageURI);
        this.a();
        this.f();
    }
    
    public void setScaleType(final ScaleType l) {
        if (l == ScaleType.FIT_START || l == ScaleType.FIT_END) {
            throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");
        }
        if (l == ScaleType.MATRIX) {
            super.setScaleType(ScaleType.MATRIX);
        }
        else {
            this.l = l;
            if (this.n) {
                this.setZoom(this);
            }
        }
    }
    
    public ScaleType getScaleType() {
        return this.l;
    }
    
    public boolean isZoomed() {
        return this.a != 1.0f;
    }
    
    public RectF getZoomedRect() {
        if (this.l == ScaleType.FIT_XY) {
            throw new UnsupportedOperationException("getZoomedRect() not supported with FIT_XY");
        }
        if (this.getDrawable() == null) {
            return new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        }
        final PointF a = this.a(0.0f, 0.0f, true);
        final PointF a2 = this.a((float)this.p, (float)this.q, true);
        final float n = (float)this.getDrawable().getIntrinsicWidth();
        final float n2 = (float)this.getDrawable().getIntrinsicHeight();
        return new RectF(a.x / n, a.y / n2, a2.x / n, a2.y / n2);
    }
    
    private void a() {
        if (this.b != null && this.q != 0 && this.p != 0) {
            this.b.getValues(this.i);
            this.c.setValues(this.i);
            this.w = this.u;
            this.v = this.t;
            this.s = this.q;
            this.r = this.p;
        }
    }
    
    public Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putFloat("saveScale", this.a);
        bundle.putFloat("matchViewHeight", this.u);
        bundle.putFloat("matchViewWidth", this.t);
        bundle.putInt("viewWidth", this.p);
        bundle.putInt("viewHeight", this.q);
        this.b.getValues(this.i);
        bundle.putFloatArray("matrix", this.i);
        bundle.putBoolean("imageRendered", this.m);
        return (Parcelable)bundle;
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            final Bundle bundle = (Bundle)parcelable;
            this.a = bundle.getFloat("saveScale");
            this.i = bundle.getFloatArray("matrix");
            this.c.setValues(this.i);
            this.w = bundle.getFloat("matchViewHeight");
            this.v = bundle.getFloat("matchViewWidth");
            this.s = bundle.getInt("viewHeight");
            this.r = bundle.getInt("viewWidth");
            this.m = bundle.getBoolean("imageRendered");
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }
    
    protected void onDraw(final Canvas canvas) {
        this.n = true;
        this.m = true;
        if (this.o != null) {
            this.setZoom(this.o.scale, this.o.focusX, this.o.focusY, this.o.scaleType);
            this.o = null;
        }
        super.onDraw(canvas);
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.a();
    }
    
    public float getMaxZoom() {
        return this.f;
    }
    
    public void setMaxZoom(final float f) {
        this.f = f;
        this.h = 1.25f * this.f;
    }
    
    public float getMinZoom() {
        return this.e;
    }
    
    public float getCurrentZoom() {
        return this.a;
    }
    
    public void setMinZoom(final float e) {
        this.e = e;
        this.g = 0.75f * this.e;
    }
    
    public void resetZoom() {
        this.a = 1.0f;
        this.f();
    }
    
    public void setZoom(final float n) {
        this.setZoom(n, 0.5f, 0.5f);
    }
    
    public void setZoom(final float n, final float n2, final float n3) {
        this.setZoom(n, n2, n3, this.l);
    }
    
    public void setZoom(final float n, final float n2, final float n3, final ScaleType scaleType) {
        if (!this.n) {
            this.o = new ZoomVariables(n, n2, n3, scaleType);
            return;
        }
        if (scaleType != this.l) {
            this.setScaleType(scaleType);
        }
        this.resetZoom();
        this.a(n, (float)(this.p / 2), (float)(this.q / 2), true);
        this.b.getValues(this.i);
        this.i[2] = -(n2 * this.d() - this.p * 0.5f);
        this.i[5] = -(n3 * this.e() - this.q * 0.5f);
        this.b.setValues(this.i);
        this.b();
        this.setImageMatrix(this.b);
    }
    
    public void setZoom(final TouchImageView touchImageView) {
        final PointF scrollPosition = touchImageView.getScrollPosition();
        this.setZoom(touchImageView.getCurrentZoom(), scrollPosition.x, scrollPosition.y, touchImageView.getScaleType());
    }
    
    public PointF getScrollPosition() {
        final Drawable drawable = this.getDrawable();
        if (drawable == null) {
            return null;
        }
        final int intrinsicWidth = drawable.getIntrinsicWidth();
        final int intrinsicHeight = drawable.getIntrinsicHeight();
        final PointF a;
        final PointF pointF = a = this.a((float)(this.p / 2), (float)(this.q / 2), (boolean)(1 != 0));
        a.x /= intrinsicWidth;
        final PointF pointF2 = pointF;
        pointF2.y /= intrinsicHeight;
        return pointF;
    }
    
    public void setScrollPosition(final float n, final float n2) {
        this.setZoom(this.a, n, n2);
    }
    
    public void resetZoomToCenter() {
        this.b.getValues(this.i);
        this.compatPostOnAnimation(new DoubleTapZoom(this.e, this.d() * 0.5f + this.i[2], this.e() * 0.5f + this.i[5], false));
    }
    
    public void setImageBitmapWithAnim(final Bitmap imageBitmap) {
        super.setImageBitmap(imageBitmap);
        this.a();
        this.g();
        this.resetZoomToCenter();
    }
    
    private void b() {
        this.b.getValues(this.i);
        final float n = this.i[2];
        final float n2 = this.i[5];
        final float a = this.a(n, (float)this.p, this.d());
        final float a2 = this.a(n2, (float)this.q, this.e());
        if (a != 0.0f || a2 != 0.0f) {
            this.b.postTranslate(a, a2);
        }
    }
    
    private void c() {
        this.b();
        this.b.getValues(this.i);
        if (this.d() < this.p) {
            this.i[2] = (this.p - this.d()) / 2.0f;
        }
        if (this.e() < this.q) {
            this.i[5] = (this.q - this.e()) / 2.0f;
        }
        this.b.setValues(this.i);
    }
    
    private float a(final float n, final float n2, final float n3) {
        float n4;
        float n5;
        if (n3 <= n2) {
            n4 = 0.0f;
            n5 = n2 - n3;
        }
        else {
            n4 = n2 - n3;
            n5 = 0.0f;
        }
        if (n < n4) {
            return -n + n4;
        }
        if (n > n5) {
            return -n + n5;
        }
        return 0.0f;
    }
    
    private float b(final float n, final float n2, final float n3) {
        if (n3 <= n2) {
            return 0.0f;
        }
        return n;
    }
    
    private float d() {
        return this.t * this.a;
    }
    
    private float e() {
        return this.u * this.a;
    }
    
    protected void onMeasure(final int n, final int n2) {
        final Drawable drawable = this.getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
            this.setMeasuredDimension(0, 0);
            return;
        }
        final int intrinsicWidth = drawable.getIntrinsicWidth();
        final int intrinsicHeight = drawable.getIntrinsicHeight();
        final int size = MeasureSpec.getSize(n);
        final int mode = MeasureSpec.getMode(n);
        final int size2 = MeasureSpec.getSize(n2);
        final int mode2 = MeasureSpec.getMode(n2);
        this.p = this.a(mode, size, intrinsicWidth);
        this.q = this.a(mode2, size2, intrinsicHeight);
        this.setMeasuredDimension(this.p, this.q);
        this.f();
    }
    
    private void f() {
        this.g();
        this.setImageMatrix(this.b);
    }
    
    private void g() {
        final Drawable drawable = this.getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
            return;
        }
        if (this.b == null || this.c == null) {
            return;
        }
        final int intrinsicWidth = drawable.getIntrinsicWidth();
        final int intrinsicHeight = drawable.getIntrinsicHeight();
        float a = this.p / (float)intrinsicWidth;
        float b = this.q / (float)intrinsicHeight;
        switch (this.l.ordinal()) {
            case 1: {
                b = (a = 1.0f);
                break;
            }
            case 2: {
                b = (a = Math.max(a, b));
                break;
            }
            case 3: {
                b = (a = Math.min(1.0f, Math.min(a, b)));
            }
            case 4: {
                b = (a = Math.min(a, b));
                break;
            }
            case 5: {
                break;
            }
            default: {
                throw new UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END");
            }
        }
        final float n = this.p - a * intrinsicWidth;
        final float n2 = this.q - b * intrinsicHeight;
        this.t = this.p - n;
        this.u = this.q - n2;
        if (!this.isZoomed() && !this.m) {
            this.b.setScale(a, b);
            this.b.postTranslate(n / 2.0f, n2 / 2.0f);
            this.a = 1.0f;
        }
        else {
            if (this.v == 0.0f || this.w == 0.0f) {
                this.a();
            }
            this.c.getValues(this.i);
            this.i[0] = this.t / intrinsicWidth * this.a;
            this.i[4] = this.u / intrinsicHeight * this.a;
            final float n3 = this.i[2];
            final float n4 = this.i[5];
            this.a(2, n3, this.v * this.a, this.d(), this.r, this.p, intrinsicWidth);
            this.a(5, n4, this.w * this.a, this.e(), this.s, this.q, intrinsicHeight);
            this.b.setValues(this.i);
        }
        this.b();
    }
    
    private int a(final int n, final int b, final int a) {
        int min = 0;
        switch (n) {
            case 1073741824: {
                min = b;
                break;
            }
            case Integer.MIN_VALUE: {
                min = Math.min(a, b);
                break;
            }
            case 0: {
                min = a;
                break;
            }
            default: {
                min = b;
                break;
            }
        }
        return min;
    }
    
    private void a(final int n, final float a, final float n2, final float n3, final int n4, final int n5, final int n6) {
        if (n3 < n5) {
            this.i[n] = (n5 - n6 * this.i[0]) * 0.5f;
        }
        else if (a > 0.0f) {
            this.i[n] = -((n3 - n5) * 0.5f);
        }
        else {
            this.i[n] = -((Math.abs(a) + 0.5f * n4) / n2 * n3 - n5 * 0.5f);
        }
    }
    
    private void a(final State d) {
        this.d = d;
    }
    
    public boolean canScrollHorizontallyFroyo(final int n) {
        return this.canScrollHorizontally(n);
    }
    
    public boolean canScrollHorizontally(final int n) {
        this.b.getValues(this.i);
        final float a = this.i[2];
        return this.d() >= this.p && (a < -1.0f || n >= 0) && (Math.abs(a) + this.p + 1.0f < this.d() || n <= 0);
    }
    
    private void a(double n, final float n2, final float n3, final boolean b) {
        float a;
        float a2;
        if (b) {
            a = this.g;
            a2 = this.h;
        }
        else {
            a = this.e;
            a2 = this.f;
        }
        final float a3 = this.a;
        this.a *= (float)n;
        if (this.a > a2) {
            this.a = a2;
            n = a2 / a3;
        }
        else if (this.a < a) {
            this.a = a;
            n = a / a3;
        }
        this.b.postScale((float)n, (float)n, n2, n3);
        this.c();
    }
    
    private PointF a(final float n, final float n2, final boolean b) {
        this.b.getValues(this.i);
        final float b2 = (float)this.getDrawable().getIntrinsicWidth();
        final float b3 = (float)this.getDrawable().getIntrinsicHeight();
        final float n3 = this.i[2];
        final float n4 = this.i[5];
        float min = (n - n3) * b2 / this.d();
        float min2 = (n2 - n4) * b3 / this.e();
        if (b) {
            min = Math.min(Math.max(min, 0.0f), b2);
            min2 = Math.min(Math.max(min2, 0.0f), b3);
        }
        return new PointF(min, min2);
    }
    
    private PointF a(final float n, final float n2) {
        this.b.getValues(this.i);
        return new PointF(this.i[2] + this.d() * (n / this.getDrawable().getIntrinsicWidth()), this.i[5] + this.e() * (n2 / this.getDrawable().getIntrinsicHeight()));
    }
    
    @TargetApi(16)
    protected void compatPostOnAnimation(final Runnable runnable) {
        if (Build.VERSION.SDK_INT >= 16) {
            this.postOnAnimation(runnable);
        }
        else {
            this.postDelayed(runnable, 16L);
        }
    }
    
    public void printMatrixInfo() {
        final float[] array = new float[9];
        this.b.getValues(array);
        Log.d("DEBUG", "Scale: " + array[0] + " TransX: " + array[2] + " TransY: " + array[5]);
    }
    
    private class ZoomVariables
    {
        public float scale;
        public float focusX;
        public float focusY;
        public ScaleType scaleType;
        
        public ZoomVariables(final float scale, final float focusX, final float focusY, final ScaleType scaleType) {
            this.scale = scale;
            this.focusX = focusX;
            this.focusY = focusY;
            this.scaleType = scaleType;
        }
    }
    
    @TargetApi(9)
    private class CompatScroller
    {
        Scroller a;
        OverScroller b;
        boolean c;
        
        public CompatScroller(final Context context) {
            if (Build.VERSION.SDK_INT < 9) {
                this.c = true;
                this.a = new Scroller(context);
            }
            else {
                this.c = false;
                this.b = new OverScroller(context);
            }
        }
        
        public void fling(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
            if (this.c) {
                this.a.fling(n, n2, n3, n4, n5, n6, n7, n8);
            }
            else {
                this.b.fling(n, n2, n3, n4, n5, n6, n7, n8);
            }
        }
        
        public void forceFinished(final boolean b) {
            if (this.c) {
                this.a.forceFinished(b);
            }
            else {
                this.b.forceFinished(b);
            }
        }
        
        public boolean isFinished() {
            if (this.c) {
                return this.a.isFinished();
            }
            return this.b.isFinished();
        }
        
        public boolean computeScrollOffset() {
            if (this.c) {
                return this.a.computeScrollOffset();
            }
            this.b.computeScrollOffset();
            return this.b.computeScrollOffset();
        }
        
        public int getCurrX() {
            if (this.c) {
                return this.a.getCurrX();
            }
            return this.b.getCurrX();
        }
        
        public int getCurrY() {
            if (this.c) {
                return this.a.getCurrY();
            }
            return this.b.getCurrY();
        }
    }
    
    private class Fling implements Runnable
    {
        CompatScroller a;
        int b;
        int c;
        
        Fling(final int n, final int n2) {
            TouchImageView.this.a(State.FLING);
            this.a = new CompatScroller(TouchImageView.this.j);
            TouchImageView.this.b.getValues(TouchImageView.this.i);
            final int b = (int)TouchImageView.this.i[2];
            final int c = (int)TouchImageView.this.i[5];
            int n3;
            int n4;
            if (TouchImageView.this.d() > TouchImageView.this.p) {
                n3 = TouchImageView.this.p - (int)TouchImageView.this.d();
                n4 = 0;
            }
            else {
                n4 = (n3 = b);
            }
            int n5;
            int n6;
            if (TouchImageView.this.e() > TouchImageView.this.q) {
                n5 = TouchImageView.this.q - (int)TouchImageView.this.e();
                n6 = 0;
            }
            else {
                n6 = (n5 = c);
            }
            this.a.fling(b, c, n, n2, n3, n4, n5, n6);
            this.b = b;
            this.c = c;
        }
        
        public void cancelFling() {
            if (this.a != null) {
                TouchImageView.this.a(State.NONE);
                this.a.forceFinished(true);
            }
        }
        
        @Override
        public void run() {
            if (TouchImageView.this.B != null) {
                TouchImageView.this.B.onMove();
            }
            if (this.a.isFinished()) {
                this.a = null;
                return;
            }
            if (this.a.computeScrollOffset()) {
                final int currX = this.a.getCurrX();
                final int currY = this.a.getCurrY();
                final int n = currX - this.b;
                final int n2 = currY - this.c;
                this.b = currX;
                this.c = currY;
                TouchImageView.this.b.postTranslate((float)n, (float)n2);
                TouchImageView.this.b();
                TouchImageView.this.setImageMatrix(TouchImageView.this.b);
                TouchImageView.this.compatPostOnAnimation(this);
            }
        }
    }
    
    private enum State
    {
        NONE, 
        DRAG, 
        ZOOM, 
        FLING, 
        ANIMATE_ZOOM;
    }
    
    private class DoubleTapZoom implements Runnable
    {
        private long b;
        private float c;
        private float d;
        private float e;
        private float f;
        private boolean g;
        private AccelerateDecelerateInterpolator h;
        private PointF i;
        private PointF j;
        
        DoubleTapZoom(final float d, final float n, final float n2, final boolean g) {
            this.h = new AccelerateDecelerateInterpolator();
            TouchImageView.this.a(State.ANIMATE_ZOOM);
            this.b = System.currentTimeMillis();
            this.c = TouchImageView.this.a;
            this.d = d;
            this.g = g;
            final PointF a2 = TouchImageView.this.a(n, n2, false);
            this.e = a2.x;
            this.f = a2.y;
            this.i = TouchImageView.this.a(this.e, this.f);
            this.j = new PointF((float)(TouchImageView.this.p / 2), (float)(TouchImageView.this.q / 2));
        }
        
        @Override
        public void run() {
            final float a = this.a();
            TouchImageView.this.a(this.b(a), this.e, this.f, this.g);
            this.a(a);
            TouchImageView.this.c();
            TouchImageView.this.setImageMatrix(TouchImageView.this.b);
            if (TouchImageView.this.B != null) {
                TouchImageView.this.B.onMove();
            }
            if (a < 1.0f) {
                TouchImageView.this.compatPostOnAnimation(this);
            }
            else {
                TouchImageView.this.a(State.NONE);
            }
        }
        
        private void a(final float n) {
            final float n2 = this.i.x + n * (this.j.x - this.i.x);
            final float n3 = this.i.y + n * (this.j.y - this.i.y);
            final PointF a = TouchImageView.this.a(this.e, this.f);
            TouchImageView.this.b.postTranslate(n2 - a.x, n3 - a.y);
        }
        
        private float a() {
            return this.h.getInterpolation(Math.min(1.0f, (System.currentTimeMillis() - this.b) / 500.0f));
        }
        
        private double b(final float n) {
            return (this.c + n * (this.d - this.c)) / (double)TouchImageView.this.a;
        }
    }
    
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        public boolean onScaleBegin(final ScaleGestureDetector scaleGestureDetector) {
            TouchImageView.this.a(State.ZOOM);
            return true;
        }
        
        public boolean onScale(final ScaleGestureDetector scaleGestureDetector) {
            TouchImageView.this.a(scaleGestureDetector.getScaleFactor(), scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY(), true);
            if (TouchImageView.this.B != null) {
                TouchImageView.this.B.onMove();
            }
            return true;
        }
        
        public void onScaleEnd(final ScaleGestureDetector scaleGestureDetector) {
            super.onScaleEnd(scaleGestureDetector);
            TouchImageView.this.a(State.NONE);
            boolean b = false;
            float n = TouchImageView.this.a;
            if (TouchImageView.this.a > TouchImageView.this.f) {
                n = TouchImageView.this.f;
                b = true;
            }
            else if (TouchImageView.this.a < TouchImageView.this.e) {
                n = TouchImageView.this.e;
                b = true;
            }
            if (b) {
                TouchImageView.this.compatPostOnAnimation(new DoubleTapZoom(n, (float)(TouchImageView.this.p / 2), (float)(TouchImageView.this.q / 2), true));
            }
        }
    }
    
    private class PrivateOnTouchListener implements OnTouchListener
    {
        private PointF b;
        
        private PrivateOnTouchListener() {
            this.b = new PointF();
        }
        
        @SuppressLint({ "ClickableViewAccessibility" })
        public boolean onTouch(final View view, final MotionEvent motionEvent) {
            TouchImageView.this.x.onTouchEvent(motionEvent);
            TouchImageView.this.y.onTouchEvent(motionEvent);
            final PointF pointF = new PointF(motionEvent.getX(), motionEvent.getY());
            if (TouchImageView.this.d == State.NONE || TouchImageView.this.d == State.DRAG || TouchImageView.this.d == State.FLING) {
                switch (motionEvent.getAction()) {
                    case 0: {
                        this.b.set(pointF);
                        if (TouchImageView.this.k != null) {
                            TouchImageView.this.k.cancelFling();
                        }
                        TouchImageView.this.a(State.DRAG);
                        break;
                    }
                    case 2: {
                        if (TouchImageView.this.d == State.DRAG) {
                            TouchImageView.this.b.postTranslate(TouchImageView.this.b(pointF.x - this.b.x, (float)TouchImageView.this.p, TouchImageView.this.d()), TouchImageView.this.b(pointF.y - this.b.y, (float)TouchImageView.this.q, TouchImageView.this.e()));
                            TouchImageView.this.b();
                            this.b.set(pointF.x, pointF.y);
                            break;
                        }
                        break;
                    }
                    case 1:
                    case 6: {
                        TouchImageView.this.a(State.NONE);
                        break;
                    }
                }
            }
            TouchImageView.this.setImageMatrix(TouchImageView.this.b);
            if (TouchImageView.this.A != null) {
                TouchImageView.this.A.onTouch(view, motionEvent);
            }
            if (TouchImageView.this.B != null) {
                TouchImageView.this.B.onMove();
            }
            return true;
        }
    }
    
    public interface OnTouchImageViewListener
    {
        void onMove();
    }
    
    private class GestureListener extends GestureDetector.SimpleOnGestureListener
    {
        public boolean onSingleTapConfirmed(final MotionEvent motionEvent) {
            if (TouchImageView.this.z != null) {
                return TouchImageView.this.z.onSingleTapConfirmed(motionEvent);
            }
            return TouchImageView.this.performClick();
        }
        
        public void onLongPress(final MotionEvent motionEvent) {
            TouchImageView.this.performLongClick();
        }
        
        public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
            if (TouchImageView.this.k != null) {
                TouchImageView.this.k.cancelFling();
            }
            TouchImageView.this.k = new Fling((int)n, (int)n2);
            TouchImageView.this.compatPostOnAnimation(TouchImageView.this.k);
            return super.onFling(motionEvent, motionEvent2, n, n2);
        }
        
        public boolean onDoubleTap(final MotionEvent motionEvent) {
            boolean onDoubleTap = false;
            if (TouchImageView.this.z != null) {
                onDoubleTap = TouchImageView.this.z.onDoubleTap(motionEvent);
            }
            if (TouchImageView.this.d == State.NONE) {
                TouchImageView.this.compatPostOnAnimation(new DoubleTapZoom((TouchImageView.this.a == TouchImageView.this.e) ? TouchImageView.this.f : TouchImageView.this.e, motionEvent.getX(), motionEvent.getY(), false));
                onDoubleTap = true;
            }
            return onDoubleTap;
        }
        
        public boolean onDoubleTapEvent(final MotionEvent motionEvent) {
            return TouchImageView.this.z != null && TouchImageView.this.z.onDoubleTapEvent(motionEvent);
        }
    }
}
