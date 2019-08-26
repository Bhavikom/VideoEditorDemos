// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lights;

import java.nio.FloatBuffer;
import android.graphics.Color;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKLightVignetteFilter extends SelesFilter implements SelesParameters.FilterParameterInterface
{
    private int a;
    private PointF b;
    private int c;
    private float[] d;
    private int e;
    private float f;
    private int g;
    private float h;
    
    public TuSDKLightVignetteFilter() {
        super("-ss2");
        this.b = new PointF(0.5f, 0.5f);
        this.d = new float[] { 0.0f, 0.0f, 0.0f };
        this.f = 0.0f;
        this.h = 1.0f;
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.a = this.mFilterProgram.uniformIndex("vignetteCenter");
        this.c = this.mFilterProgram.uniformIndex("vignetteColor");
        this.e = this.mFilterProgram.uniformIndex("vignetteStart");
        this.g = this.mFilterProgram.uniformIndex("vignetteEnd");
        this.a(this.b);
        this.a(this.d);
        this.a(this.f);
        this.b(this.h);
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    private void a(final PointF b) {
        this.setPoint(this.b = b, this.a, this.mFilterProgram);
    }
    
    public void setVignetteColor(final int n) {
        this.a(new float[] { Color.red(n) / 255.0f, Color.green(n) / 255.0f, Color.blue(n) / 255.0f });
    }
    
    private void a(final float[] d) {
        this.setVec3(this.d = d, this.c, this.mFilterProgram);
    }
    
    private void a(final float f) {
        this.setFloat(this.f = f, this.e, this.mFilterProgram);
    }
    
    private void b(final float h) {
        this.setFloat(this.h = h, this.g, this.mFilterProgram);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, floatBuffer2);
        this.checkGLError(this.getClass().getSimpleName());
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("vignette", this.f, 1.0f, 0.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("vignette")) {
            this.a(filterArg.getValue());
        }
    }
}
