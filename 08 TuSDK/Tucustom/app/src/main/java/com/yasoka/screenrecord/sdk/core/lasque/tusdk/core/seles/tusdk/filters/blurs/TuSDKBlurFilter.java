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

public class TuSDKBlurFilter extends SelesFilterGroup implements SelesParameters.FilterParameterInterface
{
    private final TuSDKGaussianBlurFiveRadiusFilter a;
    private final TuSDKSelectiveFilter b;
    private PointF c;
    private float d;
    private float e;
    private float f;
    
    public TuSDKBlurFilter() {
        this.c = new PointF(0.5f, 0.5f);
        this.d = 0.2f;
        this.f = 1.0f;
        this.addFilter(this.a = TuSDKGaussianBlurSevenRadiusFilter.hardware(true));
        this.addFilter(this.b = new TuSDKSelectiveFilter());
        this.a.addTarget(this.b, 1);
        this.setInitialFilters(this.a, this.b);
        this.setTerminalFilter(this.b);
        this.b.setCenter(this.c);
        this.b.setExcessive(this.d);
        this.b.setDegree(this.e);
        this.b.setMaskAlpha(0.0f);
        this.b.setRadius(0.0f);
        this.b.setSelective(0.1f);
        if (this.a.getClass().equals(TuSDKGaussianBlurSevenRadiusFilter.class)) {
            this.f = 0.38f;
        }
        else {
            this.f = 40.0f;
        }
        this.a(this.f);
    }
    
    public void setCenter(final PointF pointF) {
        this.c = pointF;
        this.b.setCenter(pointF);
    }
    
    private float a() {
        return this.f;
    }
    
    private void a(final float n) {
        this.f = n;
        this.a.setBlurSize(n);
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        if (this.a.getClass().equals(TuSDKGaussianBlurSevenRadiusFilter.class)) {
            initParams.appendFloatArg("blurSize", this.a(), 0.0f, 2.0f);
        }
        else {
            initParams.appendFloatArg("blurSize", this.a(), 0.0f, 6.0f);
        }
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("blurSize")) {
            this.a(filterArg.getValue());
        }
    }
}
