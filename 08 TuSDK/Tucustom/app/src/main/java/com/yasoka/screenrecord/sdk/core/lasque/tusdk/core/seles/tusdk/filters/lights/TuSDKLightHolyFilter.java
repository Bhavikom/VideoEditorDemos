// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lights;

//import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurSevenRadiusFilter;

//import org.lasque.tusdk.core.seles.sources.SelesPicture;
import java.util.List;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurSevenRadiusFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;

public class TuSDKLightHolyFilter extends SelesFilterGroup implements SelesParameters.FilterParameterInterface, SelesParameters.FilterTexturesInterface
{
    private float a;
    private float b;
    private TuSDKGaussianBlurFiveRadiusFilter c;
    private _TuSDKLightHolyFilter d;
    
    public TuSDKLightHolyFilter() {
        this.addFilter(this.c = TuSDKGaussianBlurSevenRadiusFilter.hardware(true));
        this.addFilter(this.d = new _TuSDKLightHolyFilter());
        this.c.addTarget(this.d, 1);
        this.setInitialFilters(this.c, this.d);
        this.setTerminalFilter(this.d);
        this.setHolyLight(0.3f);
        this.setBrightness(0.0f);
    }
    
    public float getHolyLight() {
        return this.a;
    }
    
    public void setHolyLight(final float a) {
        this.a = a;
        this.d.setIntensity(1.0f - a);
        this.d.setContrast(1.0f + a * 0.52f);
        this.c.setBlurSize(3.0f + a * 2.0f);
    }
    
    public float getBrightness() {
        return this.b;
    }
    
    public void setBrightness(final float n) {
        this.b = n;
        this.d.setMix(n);
    }
    
    @Override
    public void appendTextures(final List<SelesPicture> list) {
        if (list == null) {
            return;
        }
        int n = 2;
        for (final SelesPicture selesPicture : list) {
            selesPicture.processImage();
            selesPicture.addTarget(this.d, n);
            ++n;
        }
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("mixied", this.getHolyLight(), 0.0f, 0.6f);
        initParams.appendFloatArg("brightness", this.getBrightness());
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("mixied")) {
            this.setHolyLight(filterArg.getValue());
        }
        else if (filterArg.equalsKey("brightness")) {
            this.setBrightness(filterArg.getValue());
        }
    }
    
    private static class _TuSDKLightHolyFilter extends SelesThreeInputFilter
    {
        private int a;
        private int b;
        private int c;
        private float d;
        private float e;
        private float f;
        
        private _TuSDKLightHolyFilter() {
            super("-slh1");
            this.d = 1.0f;
            this.e = 1.0f;
            this.f = 0.5f;
            this.disableThirdFrameCheck();
        }
        
        @Override
        protected void onInitOnGLThread() {
            super.onInitOnGLThread();
            this.a = this.mFilterProgram.uniformIndex("intensity");
            this.b = this.mFilterProgram.uniformIndex("contrast");
            this.c = this.mFilterProgram.uniformIndex("mixturePercent");
            this.setIntensity(this.d);
            this.setContrast(this.e);
            this.setMix(this.f);
        }
        
        public void setIntensity(final float d) {
            this.setFloat(this.d = d, this.a, this.mFilterProgram);
        }
        
        public void setContrast(final float e) {
            this.setFloat(this.e = e, this.b, this.mFilterProgram);
        }
        
        public void setMix(final float f) {
            this.setFloat(this.f = f, this.c, this.mFilterProgram);
        }
    }
}
