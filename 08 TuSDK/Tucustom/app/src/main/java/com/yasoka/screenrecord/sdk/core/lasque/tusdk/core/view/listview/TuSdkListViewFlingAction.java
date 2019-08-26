// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview;

import android.view.ViewGroup;
import android.view.animation.Interpolator;
//import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import android.support.v4.view.ViewCompat;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.content.Context;
import android.graphics.PointF;
import android.view.VelocityTracker;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;

public class TuSdkListViewFlingAction
{
    private int a;
    private int b;
    private int c;
    private long d;
    private float e;
    private boolean f;
    private VelocityTracker g;
    private LasqueListViewFlingActionInterface h;
    private PointF i;
    private Context j;
    private TuSdkListViewFlingActionDelegate k;
    
    public void setDelegate(final TuSdkListViewFlingActionDelegate k) {
        this.k = k;
    }
    
    public TuSdkListViewFlingAction(final Context j) {
        this.d = 150L;
        this.j = j;
        this.a();
    }
    
    private void a() {
        final ViewConfiguration value = ViewConfiguration.get(this.j);
        this.a = value.getScaledTouchSlop();
        this.b = value.getScaledMinimumFlingVelocity() * 8;
        this.c = value.getScaledMaximumFlingVelocity();
    }
    
    public void onDestory() {
        this.b();
        this.h = null;
        this.k = null;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent, final View view) {
        switch (motionEvent.getAction()) {
            case 0: {
                this.a(motionEvent, view);
                break;
            }
            case 2: {
                return this.a(motionEvent);
            }
            case 1: {
                this.b(motionEvent);
                break;
            }
        }
        return false;
    }
    
    @SuppressLint({ "Recycle" })
    private void a(final MotionEvent motionEvent, final View obj) {
        this.e = 0.0f;
        if (this.h != null && !this.h.equals(obj)) {
            this.resetDownView();
        }
        if (obj == null || !(obj instanceof LasqueListViewFlingActionInterface) || !((LasqueListViewFlingActionInterface)obj).flingCanShow()) {
            this.h = null;
            this.b();
            return;
        }
        this.i = new PointF(motionEvent.getX(), motionEvent.getY());
        this.h = (LasqueListViewFlingActionInterface)obj;
        (this.g = VelocityTracker.obtain()).addMovement(motionEvent);
    }
    
    private boolean a(final MotionEvent motionEvent) {
        if (this.g == null || this.h == null) {
            return false;
        }
        final PointF pointF = new PointF(motionEvent.getX() - this.i.x, motionEvent.getY() - this.i.y);
        if (!this.f && Math.abs(pointF.x) > this.a && Math.abs(pointF.y) < this.a && this.k != null) {
            this.f = true;
            this.h.flingStateWillChange(true);
            this.e = ViewCompat.getTranslationX((View)this.h.flingCellWrap());
            this.k.onFlingActionCancelItemClick(motionEvent);
        }
        if (this.f) {
            final float n = pointF.x + this.e;
            ViewCompat.setTranslationX((View)this.h.flingCellWrap(), (n < 0.0f) ? n : 0.0f);
            return true;
        }
        return false;
    }
    
    private void b(final MotionEvent motionEvent) {
        if (this.g == null || this.h == null || !this.f) {
            this.resetDownView();
            this.b();
            return;
        }
        final float translationX = ViewCompat.getTranslationX((View)this.h.flingCellWrap());
        this.g.computeCurrentVelocity(1000);
        final float abs = Math.abs(this.g.getXVelocity());
        final float abs2 = Math.abs(this.g.getYVelocity());
        boolean b = false;
        if (Math.abs(translationX) > this.h.flingWrapWidth() * 0.75) {
            b = true;
        }
        else if (this.b <= abs && abs <= this.c && abs2 < abs) {
            b = true;
        }
        if (b) {
            this.h.flingStateWillChange(true);
            ViewCompat.animate((View)this.h.flingCellWrap()).translationX((float)(-this.h.flingWrapWidth())).
                    setDuration(this.d).setInterpolator((Interpolator)new AccelerateDecelerateInterpolator());
        }
        else {
            this.resetDownView();
        }
        this.b();
        this.f = false;
    }
    
    private void b() {
        if (this.g != null) {
            this.g.recycle();
            this.g = null;
        }
    }
    
    public void resetDownView() {
        if (this.h == null) {
            return;
        }
        this.h.flingStateWillChange(false);
        ViewCompat.animate((View)this.h.flingCellWrap()).translationX(0.0f).setDuration(this.d).setInterpolator((Interpolator)
                new AccelerateDecelerateInterpolator());
    }
    
    public interface TuSdkListViewFlingActionDelegate
    {
        void onFlingActionCancelItemClick(final MotionEvent p0);
    }
    
    public interface LasqueListViewFlingActionInterface
    {
        ViewGroup flingCellWrap();
        
        int flingWrapWidth();
        
        void flingStateWillChange(final boolean p0);
        
        boolean flingCanShow();
    }
}
