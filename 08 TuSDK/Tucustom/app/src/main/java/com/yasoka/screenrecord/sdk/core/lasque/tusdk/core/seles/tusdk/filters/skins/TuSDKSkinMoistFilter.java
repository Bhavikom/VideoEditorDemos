// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.skins;

//import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSurfaceBlurFilter;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSurfaceBlurFilter;

public class TuSDKSkinMoistFilter extends SelesFilterGroup implements SelesParameters.FilterParameterInterface
{
    private static String a;
    private float b;
    private float c;
    private float d;
    private float e;
    private float f;
    private float g;
    private float h;
    private TuSDKSurfaceBlurFilter i;
    private _TuSDKSkinMoistMixFilter j;
    
    public TuSDKSkinMoistFilter() {
        (this.i = new TuSDKSurfaceBlurFilter()).setScale(0.8f);
        this.addFilter(this.j = new _TuSDKSkinMoistMixFilter());
        this.i.addTarget(this.j, 1);
        this.setInitialFilters(this.i, this.j);
        this.setTerminalFilter(this.j);
        this.setSmoothing(0.8f);
        this.b(this.i.getMaxBlursize());
        this.c(this.i.getMaxThresholdLevel());
        this.setFair(0.0f);
        this.a(0.0f);
        this.d(1.0f);
        this.e(0.18f);
    }
    
    private float a() {
        return this.b;
    }
    
    public void setSmoothing(final float n) {
        this.b = n;
        this.j.setIntensity(n);
    }
    
    private float b() {
        return this.c;
    }
    
    public void setFair(final float n) {
        this.c = n;
        this.j.setFair(n);
    }
    
    private float c() {
        return this.d;
    }
    
    private void a(final float n) {
        this.d = n;
        this.j.setRuddy(n);
    }
    
    private float d() {
        return this.e;
    }
    
    private void b(final float n) {
        this.e = n;
        this.i.setBlurSize(n);
    }
    
    private float e() {
        return this.f;
    }
    
    private void c(final float n) {
        this.f = n;
        this.i.setThresholdLevel(n);
    }
    
    private float f() {
        return this.g;
    }
    
    private void d(final float n) {
        this.g = n;
        this.j.setLight(n);
    }
    
    private float g() {
        return this.h;
    }
    
    private void e(final float n) {
        this.h = n;
        this.j.setDetail(n);
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("smoothing", this.a());
        initParams.appendFloatArg("whitening", this.b());
        initParams.appendFloatArg("ruddy", this.c());
        if (initParams.getDefaultArg("debug") > 0.0f) {
            initParams.appendFloatArg("blurSize", this.d(), 0.0f, this.i.getMaxBlursize());
            initParams.appendFloatArg("thresholdLevel", this.e(), 0.0f, this.i.getMaxThresholdLevel());
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
    
    static {
        TuSDKSkinMoistFilter.a = "precision highp float;varying vec2 textureCoordinate;varying vec2 textureCoordinate2;uniform sampler2D inputImageTexture;uniform sampler2D inputImageTexture2;uniform float uIntensity;uniform float uFair;uniform float uRuddy;uniform float uLight;uniform float uDetail;float handleHardLight(float color){     if(color > 0.5){          color = 1.0 - pow(1.0 - color, 2.0) * 2.0;     }else{          color = color * color * 2.0;     }     return color;}vec3 handleHardLight3(vec3 color){     return vec3(handleHardLight(color.r), handleHardLight(color.g), handleHardLight(color.b));}const vec3 luminanceWeight = vec3(0.299,0.587,0.114);highp vec3 handleLightDarkBlend(highp vec3 base, highp vec3 overlayer){     vec3 highPass = base - overlayer + 0.5;     highPass = handleHardLight3(handleHardLight3(handleHardLight3(handleHardLight3(highPass))));     float lumance = dot(base, luminanceWeight);     vec3 smoothColor = base + (base - highPass) * lumance * uDetail;     smoothColor = clamp(smoothColor,0.0,1.0);     return smoothColor;}void main(){     vec3 sharpColor = texture2D(inputImageTexture, textureCoordinate).rgb;     vec3 surfaceColor = texture2D(inputImageTexture2, textureCoordinate2).rgb;     surfaceColor = handleLightDarkBlend(sharpColor, surfaceColor);     surfaceColor = mix(sharpColor, surfaceColor, uIntensity);     float dark = dot(surfaceColor, luminanceWeight);     float gb = 1.0 - 0.05 * uRuddy;     surfaceColor = mix(surfaceColor, vec3(1.0, gb, gb), dark * max(uRuddy, uFair));     gl_FragColor = vec4(surfaceColor, 1.0);}";
    }
    
    private class _TuSDKSkinMoistMixFilter extends SelesTwoInputFilter
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
        
        public _TuSDKSkinMoistMixFilter() {
            super("-sscf3");
            this.g = 1.0f;
            this.h = 0.0f;
            this.i = 0.0f;
            this.j = 1.0f;
            this.k = 0.18f;
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
