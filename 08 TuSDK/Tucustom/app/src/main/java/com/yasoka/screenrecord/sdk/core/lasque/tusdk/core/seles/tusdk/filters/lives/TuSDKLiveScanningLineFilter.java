// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives;

//import org.lasque.tusdk.core.seles.SelesParameters;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
//import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;

public class TuSDKLiveScanningLineFilter extends SelesTwoInputFilter
{
    private int a;
    private int b;
    private int c;
    private PointF d;
    private float e;
    private float f;
    private float g;
    private int h;
    private boolean i;
    
    public TuSDKLiveScanningLineFilter() {
        super("-slive13f");
        this.d = new PointF(0.0f, 0.0f);
        this.e = 0.0f;
        this.f = 0.0f;
        this.g = 1.0f;
        this.h = 0;
        this.i = false;
        this.disableSecondFrameCheck();
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.c = this.mFilterProgram.uniformIndex("curve");
        this.a = this.mFilterProgram.uniformIndex("screenPercent");
        this.b = this.mFilterProgram.uniformIndex("line");
        this.setScreenPercent(this.e);
        this.setLine(this.f);
        this.setCurve(this.d);
    }
    
    public float getScreenPercent() {
        return this.e;
    }
    
    public void setScreenPercent(final float e) {
        this.setFloat(this.e = e, this.a, this.mFilterProgram);
    }
    
    public float getLine() {
        return this.f;
    }
    
    public void setLine(final float f) {
        this.setFloat(this.f = f, this.b, this.mFilterProgram);
    }
    
    public PointF getCurve() {
        return this.d;
    }
    
    public void setCurve(final PointF d) {
        this.setPoint(this.d = d, this.c, this.mFilterProgram);
    }
    
    public void setCurveStrength(final float x) {
        final PointF curve = this.getCurve();
        curve.x = x;
        this.setCurve(curve);
    }
    
    public void setCurveTone(final float y) {
        final PointF curve = this.getCurve();
        curve.y = y;
        this.setCurve(curve);
    }
    
    public float getAnimation() {
        return this.g;
    }
    
    public void setAnimation(final float g) {
        this.g = g;
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long n) {
        this.a(n);
        super.informTargetsAboutNewFrame(n);
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("screenPercent", this.getScreenPercent(), -1.0f, 1.0f);
        initParams.appendFloatArg("line", this.getLine(), 0.0f, 1.0f);
        initParams.appendFloatArg("curveStrength", this.getCurve().x, -2.0f, 2.0f);
        initParams.appendFloatArg("curveTone", this.getCurve().y, 0.0f, 1.0f);
        initParams.appendFloatArg("animation", this.getAnimation(), 0.0f, 1.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("screenPercent")) {
            this.setScreenPercent(filterArg.getValue());
        }
        else if (filterArg.equalsKey("line")) {
            this.setLine(filterArg.getValue());
        }
        else if (filterArg.equalsKey("curveStrength")) {
            this.setCurveStrength(filterArg.getValue());
        }
        else if (filterArg.equalsKey("curveTone")) {
            this.setCurveTone(filterArg.getValue());
        }
        else if (filterArg.equalsKey("animation")) {
            this.setAnimation(filterArg.getValue());
        }
    }
    
    private void a(final long n) {
        if (this.getAnimation() < 0.5) {
            return;
        }
        final long n2 = n % 1000000000L;
        if (n / 1000000000L % 2L == 0L) {
            this.i = true;
        }
        else {
            this.i = false;
        }
        final long[] array = { 0L, 50000000L, 100000000L, 150000000L, 200000000L, 250000000L, 300000000L };
        final float[] array2 = { 0.0f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f };
        final float[] array3 = { 0.0f, 0.5f, 0.56f, 0.66f, 0.55f, 0.65f, 0.56f };
        final float[] array4 = { 0.0f, 0.0f, 0.2f, 0.4f, 0.2f, 0.4f, 0.2f };
        if (n2 > array[array.length - 1]) {
            this.getParameter().setFilterArg("screenPercent", 0.5f);
            this.getParameter().setFilterArg("curveStrength", 0.5f);
            this.getParameter().setFilterArg("curveTone", 0.0f);
            this.getParameter().setFilterArg("line", 0.0f);
        }
        for (int i = 0; i < array.length - 1; ++i) {
            if (array[i] < n2 && array[i + 1] >= n2) {
                this.getParameter().setFilterArg("screenPercent", 0.5f + (this.i ? 1.0f : -1.0f) * array2[i + 1]);
                this.getParameter().setFilterArg("curveStrength", array3[i + 1]);
                this.getParameter().setFilterArg("curveTone", array4[i + 1]);
                this.getParameter().setFilterArg("line", 1.0f);
            }
        }
        this.submitParameter();
    }
}
