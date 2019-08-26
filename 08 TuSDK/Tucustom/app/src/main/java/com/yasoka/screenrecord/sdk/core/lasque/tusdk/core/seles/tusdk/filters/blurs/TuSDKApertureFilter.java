// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.blurs;

//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurSevenRadiusFilter;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurSevenRadiusFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSelectiveFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSelectiveFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;

public class TuSDKApertureFilter extends SelesFilterGroup implements SelesParameters.FilterParameterInterface
{
    private final TuSDKGaussianBlurFiveRadiusFilter a;
    private final TuSDKSelectiveFilter b;
    private PointF c;
    private float d;
    private float e;
    private float f;
    private int g;
    private float h;
    private float i;
    private float j;
    
    public TuSDKApertureFilter() {
        this.c = new PointF(0.5f, 0.5f);
        this.d = 0.4f;
        this.e = 0.2f;
        this.g = -1;
        this.j = 1.0f;
        this.addFilter(this.a = TuSDKGaussianBlurSevenRadiusFilter.hardware(true));
        this.addFilter(this.b = new TuSDKSelectiveFilter());
        this.a.addTarget(this.b, 1);
        this.setInitialFilters(this.a, this.b);
        this.setTerminalFilter(this.b);
        this.a(this.d);
        this.setCenter(this.c);
        this.b(this.e);
        this.a(this.g);
        this.d(this.f);
        this.e(this.h);
        this.f(this.i);
        this.c(this.j);
    }
    
    private PointF a() {
        return this.c;
    }
    
    public void setCenter(final PointF pointF) {
        this.c = pointF;
        this.b.setCenter(pointF);
    }
    
    private float b() {
        return this.d;
    }
    
    private void a(final float n) {
        this.d = n;
        this.b.setRadius(n);
        this.b(n * 0.75f);
    }
    
    private float c() {
        return this.e;
    }
    
    private void b(final float n) {
        this.e = n;
        this.b.setExcessive(n);
    }
    
    private void a(final int n) {
        this.g = n;
        this.b.setMaskColor(n);
    }
    
    private float d() {
        return this.j;
    }
    
    private void c(final float n) {
        this.j = n;
        this.a.setBlurSize(n);
    }
    
    private float e() {
        return this.f;
    }
    
    private void d(final float n) {
        this.f = n;
        this.b.setMaskAlpha(n);
    }
    
    private float f() {
        return this.h;
    }
    
    private void e(final float n) {
        this.h = n;
        this.b.setDegree(n);
    }
    
    private float g() {
        return this.i;
    }
    
    private void f(final float n) {
        this.i = n;
        this.b.setSelective(n);
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("aperture", this.d(), 0.0f, 2.0f);
        initParams.appendFloatArg("centerX", this.a().x);
        initParams.appendFloatArg("centerY", this.a().y);
        initParams.appendFloatArg("radius", this.b());
        initParams.appendFloatArg("excessive", this.c());
        initParams.appendFloatArg("maskAlpha", this.e(), 0.0f, 0.7f);
        initParams.appendFloatArg("degree", this.f(), 0.0f, 360.0f);
        initParams.appendFloatArg("selective", this.g());
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("aperture")) {
            this.c(filterArg.getValue());
        }
        else if (filterArg.equalsKey("radius")) {
            this.a(filterArg.getValue());
        }
        else if (filterArg.equalsKey("excessive")) {
            this.b(filterArg.getValue());
        }
        else if (filterArg.equalsKey("maskAlpha")) {
            this.d(filterArg.getValue());
        }
        else if (filterArg.equalsKey("degree")) {
            this.e(filterArg.getValue());
        }
        else if (filterArg.equalsKey("selective")) {
            this.f(filterArg.getValue());
        }
        else if (filterArg.equalsKey("centerX")) {
            this.a().x = filterArg.getValue();
            this.setCenter(this.a());
        }
        else if (filterArg.equalsKey("centerY")) {
            this.a().y = filterArg.getValue();
            this.setCenter(this.a());
        }
    }
}
