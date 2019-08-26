// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.flowabs;

//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;

public class TuSDKTfmInkFilter extends SelesFilterGroup implements SelesParameters.FilterParameterInterface
{
    private float g;
    private float h;
    private float i;
    private float j;
    private float k;
    SelesFilter a;
    SelesFilter b;
    TuSDKGaussianBlurFiveRadiusFilter c;
    TuSDKTfmEdgeFilter d;
    TuSDKTfmDogFilter e;
    TuSDKTfmLicFilter f;
    
    public TuSDKTfmInkFilter() {
        this.g = 2.0f;
        this.h = 1.0f;
        this.i = 160.0f;
        this.j = 0.25f;
        this.k = 1.5f;
        (this.a = new SelesFilter()).setScale(0.5f);
        (this.c = new TuSDKGaussianBlurFiveRadiusFilter()).setScale(0.5f);
        this.e = new TuSDKTfmDogFilter();
        (this.d = new TuSDKTfmEdgeFilter()).setScale(0.5f);
        this.f = new TuSDKTfmLicFilter();
        (this.b = new SelesFilter()).setScale(2.0f);
        this.addFilter(this.b);
        this.a.addTarget(this.d, 0);
        this.a.addTarget(this.c, 0);
        this.a.addTarget(this.e, 0);
        this.c.addTarget(this.e, 1);
        this.d.addTarget(this.e, 2);
        this.e.addTarget(this.f, 0);
        this.f.addTarget(this.b, 0);
        this.setInitialFilters(this.a);
        this.setTerminalFilter(this.b);
        this.setSst(this.g);
        this.setTau(this.h);
        this.setPhi(this.i);
        this.setDogBlur(this.j);
        this.setTfmEdge(this.k);
    }
    
    public void setSst(final float n) {
        this.g = n;
        this.e.setStepLength(n);
    }
    
    public void setTau(final float n) {
        this.h = n;
        this.e.setTau(n);
    }
    
    public void setPhi(final float n) {
        this.i = n;
        this.e.setPhi(n);
    }
    
    public void setDogBlur(final float n) {
        this.j = n;
        this.c.setBlurSize(n);
    }
    
    public void setTfmEdge(final float n) {
        this.k = n;
        this.d.setEdgeStrength(n);
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("tau")) {
            this.setTau(filterArg.getValue());
        }
        else if (filterArg.equalsKey("phi")) {
            this.setPhi(filterArg.getValue());
        }
        else if (filterArg.equalsKey("sst")) {
            this.setSst(filterArg.getValue());
        }
        else if (filterArg.equalsKey("dogBlur")) {
            this.setDogBlur(filterArg.getValue());
        }
        else if (filterArg.equalsKey("tfmEdge")) {
            this.setTfmEdge(filterArg.getValue());
        }
    }
}
