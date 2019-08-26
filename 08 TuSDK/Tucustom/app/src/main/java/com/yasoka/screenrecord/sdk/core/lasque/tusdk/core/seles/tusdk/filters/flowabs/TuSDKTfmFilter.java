// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.flowabs;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBilateralFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;

//import org.lasque.tusdk.core.seles.sources.SelesPicture;
import java.util.List;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBilateralFilter;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;

public class TuSDKTfmFilter extends SelesFilterGroup implements SelesParameters.FilterParameterInterface, SelesParameters.FilterTexturesInterface
{
    private float h;
    private float i;
    private float j;
    private float k;
    private float l;
    private float m;
    private float n;
    private float o;
    private float p;
    SelesFilter a;
    TuSDKGaussianBilateralFilter b;
    TuSDKGaussianBlurFiveRadiusFilter c;
    TuSDKTfmEdgeFilter d;
    TuSDKTfmDogFilter e;
    TuSDKTfmLicFilter f;
    TuSDKTfmMixFilter g;
    
    public TuSDKTfmFilter() {
        this.h = 2.0f;
        this.i = 0.96f;
        this.j = 200.0f;
        this.k = 0.24f;
        this.l = 0.5f;
        this.m = 1.6f;
        this.n = 0.3f;
        this.o = 1.2f;
        this.p = 0.85f;
        (this.a = new SelesFilter()).setScale(0.4f);
        (this.c = new TuSDKGaussianBlurFiveRadiusFilter()).setScale(0.6f);
        this.e = new TuSDKTfmDogFilter();
        (this.d = new TuSDKTfmEdgeFilter()).setScale(0.6f);
        this.f = new TuSDKTfmLicFilter();
        this.b = new TuSDKGaussianBilateralFilter();
        (this.g = new TuSDKTfmMixFilter()).setScale(2.5f);
        this.addFilter(this.g);
        this.a.addTarget(this.d, 0);
        this.a.addTarget(this.c, 0);
        this.a.addTarget(this.e, 0);
        this.a.addTarget(this.b, 0);
        this.c.addTarget(this.e, 1);
        this.d.addTarget(this.e, 2);
        this.e.addTarget(this.f, 0);
        this.b.addTarget(this.g, 0);
        this.f.addTarget(this.g, 2);
        this.setInitialFilters(this.a);
        this.setTerminalFilter(this.g);
        this.setSst(this.h);
        this.setTau(this.i);
        this.setPhi(this.j);
        this.setDogBlur(this.k);
        this.setTfmEdge(this.l);
        this.setVectorSize(this.m);
        this.setVectorDist(this.n);
        this.setStepLength(this.o);
        this.setLightUp(this.p);
    }
    
    public void setSst(final float n) {
        this.h = n;
        this.e.setStepLength(n);
    }
    
    public void setTau(final float n) {
        this.i = n;
        this.e.setTau(n);
    }
    
    public void setPhi(final float n) {
        this.j = n;
        this.e.setPhi(n);
    }
    
    public void setDogBlur(final float n) {
        this.k = n;
        this.c.setBlurSize(n);
    }
    
    public void setTfmEdge(final float n) {
        this.l = n;
        this.d.setEdgeStrength(n);
    }
    
    public void setVectorSize(final float n) {
        this.m = n;
        this.b.setBlurSize(n);
    }
    
    public void setVectorDist(final float n) {
        this.n = n;
        this.b.setDistanceNormalizationFactor(n);
    }
    
    public void setStepLength(final float n) {
        this.o = n;
        this.f.setStepLength(n);
    }
    
    public void setLightUp(final float n) {
        this.p = n;
        this.g.setLightUp(n);
    }
    
    @Override
    public void appendTextures(final List<SelesPicture> list) {
        if (list == null) {
            return;
        }
        int n = 1;
        for (final SelesPicture selesPicture : list) {
            selesPicture.processImage();
            selesPicture.addTarget(this.g, n);
            ++n;
        }
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
        else if (filterArg.equalsKey("vectorSize")) {
            this.setVectorSize(filterArg.getValue());
        }
        else if (filterArg.equalsKey("vectorDist")) {
            this.setVectorDist(filterArg.getValue());
        }
        else if (filterArg.equalsKey("stepLength")) {
            this.setStepLength(filterArg.getValue());
        }
        else if (filterArg.equalsKey("lightUp")) {
            this.setLightUp(filterArg.getValue());
        }
    }

    @Override
    public SelesParameters getParameter() {
        return null;
    }

    @Override
    public void setParameter(SelesParameters p0) {

    }

    @Override
    public void submitParameter() {

    }
}
