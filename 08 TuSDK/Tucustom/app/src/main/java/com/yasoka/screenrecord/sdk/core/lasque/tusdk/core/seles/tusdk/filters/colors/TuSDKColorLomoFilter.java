// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors;

import android.graphics.Color;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;

public class TuSDKColorLomoFilter extends SelesTwoInputFilter implements SelesParameters.FilterParameterInterface
{
    private float a;
    private int b;
    private int c;
    private PointF d;
    private int e;
    private float[] f;
    private int g;
    private float h;
    private int i;
    private float j;
    
    public TuSDKColorLomoFilter() {
        super("-sc2");
        this.a = 1.0f;
        this.d = new PointF(0.5f, 0.5f);
        this.f = new float[] { 0.0f, 0.0f, 0.0f };
        this.h = 0.25f;
        this.j = 1.0f;
        this.disableSecondFrameCheck();
    }
    
    public TuSDKColorLomoFilter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null) {
            if (filterOption.args.containsKey("mixied")) {
                final float float1 = Float.parseFloat(filterOption.args.get("mixied"));
                if (float1 > 0.0f) {
                    this.setMixed(float1);
                }
            }
            if (filterOption.args.containsKey("vignette")) {
                final float float2 = Float.parseFloat(filterOption.args.get("vignette"));
                if (float2 > 0.0f) {
                    this.a(float2);
                }
            }
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.b = this.mFilterProgram.uniformIndex("mixturePercent");
        this.c = this.mFilterProgram.uniformIndex("vignetteCenter");
        this.e = this.mFilterProgram.uniformIndex("vignetteColor");
        this.g = this.mFilterProgram.uniformIndex("vignetteStart");
        this.i = this.mFilterProgram.uniformIndex("vignetteEnd");
        this.setMixed(this.a);
        this.a(this.d);
        this.a(this.f);
        this.a(this.h);
        this.b(this.j);
    }
    
    public float getMixed() {
        return this.a;
    }
    
    public void setMixed(final float a) {
        this.setFloat(this.a = a, this.b, this.mFilterProgram);
    }
    
    private void a(final PointF d) {
        this.setPoint(this.d = d, this.c, this.mFilterProgram);
    }
    
    public void setVignetteColor(final int n) {
        this.a(new float[] { Color.red(n) / 255.0f, Color.green(n) / 255.0f, Color.blue(n) / 255.0f });
    }
    
    private void a(final float[] f) {
        this.setVec3(this.f = f, this.e, this.mFilterProgram);
    }
    
    private void a(final float h) {
        this.setFloat(this.h = h, this.g, this.mFilterProgram);
    }
    
    private void b(final float j) {
        this.setFloat(this.j = j, this.i, this.mFilterProgram);
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("mixied", this.getMixed());
        initParams.appendFloatArg("vignette", this.h, 1.0f, 0.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("mixied")) {
            this.setMixed(filterArg.getValue());
        }
        else if (filterArg.equalsKey("vignette")) {
            this.a(filterArg.getValue());
        }
    }
}
