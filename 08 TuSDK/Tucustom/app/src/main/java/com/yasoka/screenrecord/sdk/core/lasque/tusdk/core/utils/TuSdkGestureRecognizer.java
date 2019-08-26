// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import android.view.MotionEvent;
import android.graphics.PointF;
import android.view.View;

public abstract class TuSdkGestureRecognizer implements View.OnTouchListener
{
    public static long MultipleStablizationDistance;
    private int a;
    private int b;
    private PointF c;
    private PointF d;
    private float e;
    private float f;
    private float g;
    private float h;
    private boolean i;
    private long j;
    private final StepData k;
    private final Runnable l;
    
    public StepData getStepData() {
        final StepData stepData = new StepData();
        stepData.stepPoint = this.d;
        stepData.stepSpace = this.f;
        stepData.stepDegree = this.h;
        return stepData;
    }
    
    public PointF getLastPoint() {
        return this.c;
    }
    
    public PointF getStepPoint() {
        return this.d;
    }
    
    public float getSpace() {
        return this.e;
    }
    
    public float getStepSpace() {
        return this.f;
    }
    
    public float getDegree() {
        return this.g;
    }
    
    public float getStepDegree() {
        return this.h;
    }
    
    public boolean isMultipleStablization() {
        return this.i;
    }
    
    public void setMultipleStablization(final boolean i) {
        this.i = i;
    }
    
    public long getMultipleStablizationDistance() {
        if (this.j < 50L) {
            this.j = TuSdkGestureRecognizer.MultipleStablizationDistance;
        }
        return this.j;
    }
    
    public void setMultipleStablizationDistance(final long j) {
        this.j = j;
    }
    
    public TuSdkGestureRecognizer() {
        this.a = -1;
        this.b = -1;
        this.l = new Runnable() {
            @Override
            public void run() {
                TuSdkGestureRecognizer.this.d();
                ThreadHelper.postDelayed(TuSdkGestureRecognizer.this.l, TuSdkGestureRecognizer.this.getMultipleStablizationDistance());
            }
        };
        this.k = new StepData();
    }
    
    private void a() {
        this.a = -1;
        this.b = -1;
        this.c = new PointF();
        this.d = new PointF();
        this.e = 0.0f;
        this.f = 0.0f;
        this.g = 0.0f;
        this.h = 0.0f;
        this.c();
        this.k.a();
    }
    
    private float a(final MotionEvent motionEvent) {
        if (this.a == -1 || this.b == -1) {
            return 0.0f;
        }
        final PointF a = this.a(motionEvent, this.a);
        final PointF a2 = this.a(motionEvent, this.b);
        final float n = a.x - a2.x;
        final float n2 = a.y - a2.y;
        return (float)Math.sqrt(n * n + n2 * n2);
    }
    
    private float b(final MotionEvent motionEvent) {
        if (this.a == -1 || this.b == -1) {
            return 0.0f;
        }
        final PointF a = this.a(motionEvent, this.a);
        final PointF a2 = this.a(motionEvent, this.b);
        return (float)Math.toDegrees(Math.atan2(a.y - a2.y, a.x - a2.x));
    }
    
    private int c(final MotionEvent motionEvent) {
        if (motionEvent == null) {
            return -1;
        }
        return motionEvent.getPointerId(motionEvent.getActionIndex());
    }
    
    private void d(final MotionEvent motionEvent) {
        if (this.c == null) {
            return;
        }
        this.c.set(motionEvent.getRawX(), motionEvent.getRawY());
    }
    
    private PointF a(final MotionEvent motionEvent, final int n) {
        if (motionEvent == null || n < 0) {
            return null;
        }
        final int pointerIndex = motionEvent.findPointerIndex(n);
        if (pointerIndex == -1) {
            return null;
        }
        return new PointF(motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex));
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        switch (motionEvent.getAction() & 0xFF) {
            case 0: {
                this.a(view, motionEvent);
                break;
            }
            case 2: {
                return this.e(view, motionEvent);
            }
            case 5: {
                this.d(view, motionEvent);
                break;
            }
            case 1:
            case 6: {
                this.b(view, motionEvent);
                break;
            }
        }
        return true;
    }
    
    private void a(final View view, final MotionEvent motionEvent) {
        this.a();
        this.a = this.c(motionEvent);
        this.c(view, motionEvent);
        this.onTouchBegin(this, view, motionEvent);
    }
    
    private void b(final View view, final MotionEvent motionEvent) {
        final int c = this.c(motionEvent);
        if (this.a != c && this.b != c) {
            return;
        }
        if (c == this.b) {
            this.b = -1;
            this.c(view, motionEvent);
        }
        else {
            this.onTouchEnd(this, view, motionEvent, this.getStepData());
            this.a();
        }
    }
    
    private void c(final View view, final MotionEvent motionEvent) {
        this.d(motionEvent);
        this.d = new PointF();
    }
    
    private void d(final View view, final MotionEvent motionEvent) {
        if (this.b != -1) {
            return;
        }
        this.b = motionEvent.getPointerId(motionEvent.getActionIndex());
        this.e = this.a(motionEvent);
        this.g = this.b(motionEvent);
        this.f = 0.0f;
        this.h = 0.0f;
        this.b();
        this.onTouchMultipleBegin(this, view, motionEvent);
    }
    
    private boolean e(final View view, final MotionEvent motionEvent) {
        if (this.a == -1) {
            return false;
        }
        if (this.b != -1) {
            return this.g(view, motionEvent);
        }
        return this.f(view, motionEvent);
    }
    
    private boolean f(final View view, final MotionEvent motionEvent) {
        if (this.c(motionEvent) != this.a) {
            return false;
        }
        this.d.set(motionEvent.getRawX() - this.c.x, motionEvent.getRawY() - this.c.y);
        this.d(motionEvent);
        this.onTouchSingleMove(this, view, motionEvent, this.getStepData());
        return true;
    }
    
    private boolean g(final View view, final MotionEvent motionEvent) {
        final int c = this.c(motionEvent);
        if (c != this.a && c != this.b) {
            return false;
        }
        final float a = this.a(motionEvent);
        final float b = this.b(motionEvent);
        this.f = a - this.e;
        this.h = b - this.g;
        this.e = a;
        this.g = b;
        this.a(this, view, motionEvent);
        return true;
    }
    
    private void b() {
        if (!this.i) {
            return;
        }
        ThreadHelper.postDelayed(this.l, this.getMultipleStablizationDistance());
    }
    
    private void c() {
        ThreadHelper.cancel(this.l);
    }
    
    private void a(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent) {
        final StepData stepData = this.getStepData();
        if (!this.i) {
            this.onTouchMultipleMove(tuSdkGestureRecognizer, view, motionEvent, stepData);
            return;
        }
        this.k.c(stepData);
    }
    
    private void d() {
        this.onTouchMultipleMoveForStablization(this, this.k.b());
        this.k.a();
    }
    
    public abstract void onTouchBegin(final TuSdkGestureRecognizer p0, final View p1, final MotionEvent p2);
    
    public void onTouchEnd(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent, final StepData stepData) {
    }
    
    public abstract void onTouchSingleMove(final TuSdkGestureRecognizer p0, final View p1, final MotionEvent p2, final StepData p3);
    
    public void onTouchMultipleMove(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent, final StepData stepData) {
    }
    
    public void onTouchMultipleMoveForStablization(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final StepData stepData) {
    }
    
    public void onTouchMultipleBegin(final TuSdkGestureRecognizer tuSdkGestureRecognizer, final View view, final MotionEvent motionEvent) {
    }
    
    static {
        TuSdkGestureRecognizer.MultipleStablizationDistance = 50L;
    }
    
    public static class StepData
    {
        public PointF stepPoint;
        public float stepSpace;
        public float stepDegree;
        private int a;
        
        private StepData() {
            this.stepPoint = new PointF();
        }
        
        private void a() {
            this.stepPoint = new PointF();
            this.stepSpace = 0.0f;
            this.stepDegree = 0.0f;
            this.a = 0;
        }
        
        private void c(final StepData stepData) {
            if (stepData == null) {
                return;
            }
            if (stepData.stepPoint != null || this.stepPoint != null) {
                this.stepPoint.x = (this.stepPoint.x + stepData.stepPoint.x) * 0.5f;
                this.stepPoint.y = (this.stepPoint.y + stepData.stepPoint.y) * 0.5f;
            }
            this.stepSpace = (this.stepSpace + stepData.stepSpace) * 0.5f;
            this.stepDegree = (this.stepDegree + stepData.stepDegree) * 0.5f;
            ++this.a;
        }
        
        private StepData b() {
            if (this.a < 1) {
                return this;
            }
            if (this.stepPoint != null) {
                final PointF stepPoint = this.stepPoint;
                stepPoint.x *= this.a;
                final PointF stepPoint2 = this.stepPoint;
                stepPoint2.y *= this.a;
            }
            this.stepSpace *= this.a;
            this.stepDegree *= this.a;
            return this;
        }
    }
}
