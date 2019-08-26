// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
//import org.lasque.tusdk.core.struct.ViewSize;
import android.util.AttributeSet;
import android.content.Context;
//import org.lasque.tusdk.core.TuSdkContext;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.ViewSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public class TuSeekBar extends TuSdkRelativeLayout
{
    private boolean a;
    private View b;
    private View c;
    private View d;
    private float e;
    private TuSeekBarDelegate f;
    private int g;
    private int h;
    private float i;
    private int j;
    
    public static int getLayoutId() {
        return TuSdkContext.getLayoutResId("tusdk_view_widget_seekbar");
    }
    
    public TuSeekBar(final Context context) {
        super(context);
        this.a = true;
        this.i = 1.2f;
        this.a = true;
    }
    
    public TuSeekBar(final Context context, final AttributeSet set) {
        super(context, set);
        this.a = true;
        this.i = 1.2f;
        this.a = true;
    }
    
    public TuSeekBar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.a = true;
        this.i = 1.2f;
        this.a = true;
    }
    
    public View getBottomView() {
        if (this.b == null) {
            this.b = this.getViewById("lsq_seekBottomView");
        }
        return this.b;
    }
    
    public void setBottomViewBackgroundResourceId(final int backgroundResource) {
        if (this.getBottomView() == null) {
            return;
        }
        this.getBottomView().setBackgroundResource(backgroundResource);
    }
    
    public View getTopView() {
        if (this.c == null) {
            this.c = this.getViewById("lsq_seekTopView");
        }
        return this.c;
    }
    
    public View getDragView() {
        if (this.d == null) {
            this.d = this.getViewById("lsq_seekDrag");
        }
        return this.d;
    }
    
    public void setDragViewBackgroundResourceId(final int backgroundResource) {
        if (this.getDragView() == null) {
            return;
        }
        this.getDragView().setBackgroundResource(backgroundResource);
    }
    
    public TuSeekBarDelegate getDelegate() {
        return this.f;
    }
    
    public void setDelegate(final TuSeekBarDelegate f) {
        this.f = f;
    }
    
    public float getProgress() {
        return this.e;
    }
    
    public void setProgress(float e) {
        if (e < 0.0f) {
            e = 0.0f;
        }
        else if (e > 1.0f) {
            e = 1.0f;
        }
        this.e = e;
        final int n = (int)Math.floor(this.j * this.e);
        this.setMarginLeft(this.getDragView(), n - this.g / 2 + this.h);
        this.setWidth(this.getTopView(), n);
    }
    
    @Override
    protected void onLayouted() {
        super.onLayouted();
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        this.a();
    }
    
    private void a() {
        this.g = ViewSize.create(this.getDragView()).width;
        if (this.g == 0) {
            return;
        }
        this.h = (int)(this.g * this.i / 2.0f);
        this.j = this.getMeasuredWidth() - this.h * 2;
        this.setMargin(this.getBottomView(), this.h, 0, this.h, 0);
        this.setMarginLeft(this.getTopView(), this.h);
        this.setProgress(this.e);
    }
    
    private void a(final float n) {
        final float progress = (n - this.h) / this.j;
        if (this.e == progress) {
            return;
        }
        this.setProgress(progress);
        if (this.f != null) {
            this.f.onTuSeekBarChanged(this, this.getProgress());
        }
    }
    
    @SuppressLint({ "ClickableViewAccessibility" })
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (!this.a) {
            return false;
        }
        if (motionEvent.getPointerCount() > 1) {
            return super.onTouchEvent(motionEvent);
        }
        switch (motionEvent.getAction()) {
            case 2: {
                this.a(motionEvent.getX());
                break;
            }
            case 0: {
                this.a(motionEvent.getX());
                ViewCompat.setScaleX(this.getDragView(), this.i);
                ViewCompat.setScaleY(this.getDragView(), this.i);
                break;
            }
            default: {
                ViewCompat.setScaleX(this.getDragView(), 1.0f);
                ViewCompat.setScaleY(this.getDragView(), 1.0f);
                break;
            }
        }
        return true;
    }
    
    public void setEnabled(final boolean a) {
        this.a = a;
    }
    
    public boolean getEnabled() {
        return this.a;
    }
    
    public interface TuSeekBarDelegate
    {
        void onTuSeekBarChanged(final TuSeekBar p0, final float p1);
    }
}
