// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKColorAdjustmentFilter extends SelesFilter implements SelesParameters.FilterParameterInterface
{
    private float a;
    private float b;
    private float c;
    private float d;
    private float e;
    private float f;
    private float g;
    private int h;
    private int i;
    private int j;
    private int k;
    private int l;
    private int m;
    private int n;
    
    public TuSDKColorAdjustmentFilter() {
        super("-sc5");
        this.a = 0.0f;
        this.b = 1.0f;
        this.c = 1.0f;
        this.d = 0.0f;
        this.e = 0.0f;
        this.f = 1.0f;
        this.g = 5000.0f;
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.h = this.mFilterProgram.uniformIndex("brightness");
        this.i = this.mFilterProgram.uniformIndex("contrast");
        this.j = this.mFilterProgram.uniformIndex("saturation");
        this.k = this.mFilterProgram.uniformIndex("exposure");
        this.l = this.mFilterProgram.uniformIndex("shadows");
        this.m = this.mFilterProgram.uniformIndex("highlights");
        this.n = this.mFilterProgram.uniformIndex("temperature");
        this.a(this.a);
        this.b(this.b);
        this.c(this.c);
        this.d(this.d);
        this.e(this.e);
        this.f(this.f);
        this.g(this.g);
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, floatBuffer2);
        this.checkGLError(this.getClass().getSimpleName());
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    }
    
    private float a() {
        return this.a;
    }
    
    private void a(final float a) {
        this.setFloat(this.a = a, this.h, this.mFilterProgram);
    }
    
    private float b() {
        return this.b;
    }
    
    private void b(final float b) {
        this.setFloat(this.b = b, this.i, this.mFilterProgram);
    }
    
    private float c() {
        return this.c;
    }
    
    private void c(final float c) {
        this.setFloat(this.c = c, this.j, this.mFilterProgram);
    }
    
    private float d() {
        return this.d;
    }
    
    private void d(final float d) {
        this.setFloat(this.d = d, this.k, this.mFilterProgram);
    }
    
    private float e() {
        return this.e;
    }
    
    private void e(final float e) {
        this.setFloat(this.e = e, this.l, this.mFilterProgram);
    }
    
    private float f() {
        return this.f;
    }
    
    private void f(final float f) {
        this.setFloat(this.f = f, this.m, this.mFilterProgram);
    }
    
    private float g() {
        return this.g;
    }
    
    private void g(final float g) {
        this.g = g;
        this.setFloat((g < 5000.0f) ? ((float)(4.0E-4 * (g - 5000.0))) : ((float)(6.0E-5 * (g - 5000.0))), this.n, this.mFilterProgram);
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("brightness", this.a(), -0.4f, 0.5f);
        initParams.appendFloatArg("contrast", this.b(), 0.6f, 1.8f);
        initParams.appendFloatArg("saturation", this.c(), 0.0f, 2.0f);
        initParams.appendFloatArg("exposure", this.d(), -2.5f, 2.0f);
        initParams.appendFloatArg("shadows", this.e());
        initParams.appendFloatArg("highlights", this.f());
        initParams.appendFloatArg("temperature", this.g(), 3500.0f, 7500.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("brightness")) {
            this.a(filterArg.getValue());
        }
        else if (filterArg.equalsKey("contrast")) {
            this.b(filterArg.getValue());
        }
        else if (filterArg.equalsKey("saturation")) {
            this.c(filterArg.getValue());
        }
        else if (filterArg.equalsKey("exposure")) {
            this.d(filterArg.getValue());
        }
        else if (filterArg.equalsKey("shadows")) {
            this.e(filterArg.getValue());
        }
        else if (filterArg.equalsKey("highlights")) {
            this.f(filterArg.getValue());
        }
        else if (filterArg.equalsKey("temperature")) {
            this.g(filterArg.getValue());
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
