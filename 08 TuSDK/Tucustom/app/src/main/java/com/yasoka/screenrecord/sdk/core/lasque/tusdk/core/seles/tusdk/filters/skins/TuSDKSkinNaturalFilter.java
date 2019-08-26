// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.skins;

//import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSurfaceBlurFilter;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSurfaceBlurFilter;

public class TuSDKSkinNaturalFilter extends SelesFilterGroup implements SelesParameters.FilterParameterInterface
{
    private float a;
    private float b;
    private float c;
    private float d;
    private float e;
    private float f;
    private float g;
    private TuSDKSurfaceBlurFilter h;
    private SelesFilter i;
    private _TuSDKSkinNaturalMixFilter j;
    
    public TuSDKSkinNaturalFilter() {
        (this.h = new TuSDKSurfaceBlurFilter()).setScale(0.8f);
        (this.i = new SelesFilter()).setScale(0.5f);
        this.addFilter(this.j = new _TuSDKSkinNaturalMixFilter());
        this.h.addTarget(this.j, 1);
        this.i.addTarget(this.j, 2);
        this.setInitialFilters(this.h, this.i, this.j);
        this.setTerminalFilter(this.j);
        this.setSmoothing(0.8f);
        this.b(this.h.getMaxBlursize());
        this.c(this.h.getMaxThresholdLevel());
        this.setFair(0.0f);
        this.a(0.0f);
        this.d(0.4f);
        this.e(0.2f);
    }
    
    private float a() {
        return this.a;
    }
    
    public void setSmoothing(final float n) {
        this.a = n;
        this.j.setIntensity(n);
    }
    
    private float b() {
        return this.b;
    }
    
    public void setFair(final float n) {
        this.b = n;
        this.j.setFair(n);
    }
    
    private float c() {
        return this.c;
    }
    
    private void a(final float n) {
        this.c = n;
        this.j.setRuddy(n);
    }
    
    private float d() {
        return this.d;
    }
    
    private void b(final float n) {
        this.d = n;
        this.h.setBlurSize(n);
    }
    
    private float e() {
        return this.e;
    }
    
    private void c(final float n) {
        this.e = n;
        this.h.setThresholdLevel(n);
    }
    
    private float f() {
        return this.f;
    }
    
    private void d(final float n) {
        this.f = n;
        this.j.setLight(n);
    }
    
    private float g() {
        return this.g;
    }
    
    private void e(final float n) {
        this.g = n;
        this.j.setDetail(n);
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("smoothing", this.a());
        initParams.appendFloatArg("whitening", this.b());
        initParams.appendFloatArg("ruddy", this.c());
        if (initParams.getDefaultArg("debug") > 0.0f) {
            initParams.appendFloatArg("blurSize", this.d(), 0.0f, this.h.getMaxBlursize());
            initParams.appendFloatArg("thresholdLevel", this.e(), 0.0f, this.h.getMaxThresholdLevel());
            initParams.appendFloatArg("lightLevel", this.f());
            initParams.appendFloatArg("detailLevel", this.g());
        }
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("smoothing")) {
            this.setSmoothing(filterArg.getValue());
        }
        else if (filterArg.equalsKey("whitening")) {
            this.setFair(filterArg.getValue());
        }
        else if (filterArg.equalsKey("ruddy")) {
            this.a(filterArg.getValue());
        }
        else if (filterArg.equalsKey("blurSize")) {
            this.b(filterArg.getValue());
        }
        else if (filterArg.equalsKey("thresholdLevel")) {
            this.c(filterArg.getValue());
        }
        else if (filterArg.equalsKey("lightLevel")) {
            this.d(filterArg.getValue());
        }
        else if (filterArg.equalsKey("detailLevel")) {
            this.e(filterArg.getValue());
        }
    }
    
    private class _TuSDKSkinNaturalMixFilter extends SelesThreeInputFilter
    {
        private int b;
        private int c;
        private int d;
        private int e;
        private int f;
        private float g;
        private float h;
        private float i;
        private float j;
        private float k;
        
        public _TuSDKSkinNaturalMixFilter() {
            super("-sscf4");
            this.g = 1.0f;
            this.h = 0.0f;
            this.i = 0.0f;
            this.j = 0.4f;
            this.k = 0.2f;
        }
        
        @Override
        protected void onInitOnGLThread() {
            super.onInitOnGLThread();
            this.b = this.mFilterProgram.uniformIndex("uIntensity");
            this.c = this.mFilterProgram.uniformIndex("uFair");
            this.d = this.mFilterProgram.uniformIndex("uRuddy");
            this.e = this.mFilterProgram.uniformIndex("uLight");
            this.f = this.mFilterProgram.uniformIndex("uDetail");
            this.setIntensity(this.g);
            this.setFair(this.h);
            this.setRuddy(this.i);
            this.setLight(this.j);
            this.setDetail(this.k);
        }
        
        public void setIntensity(final float g) {
            this.setFloat(this.g = g, this.b, this.mFilterProgram);
        }
        
        public void setFair(final float h) {
            this.setFloat(this.h = h, this.c, this.mFilterProgram);
        }
        
        public void setRuddy(final float i) {
            this.setFloat(this.i = i, this.d, this.mFilterProgram);
        }
        
        public void setLight(final float j) {
            this.setFloat(this.j = j, this.e, this.mFilterProgram);
        }
        
        public void setDetail(final float k) {
            this.setFloat(this.k = k, this.f, this.mFilterProgram);
        }
    }
}
