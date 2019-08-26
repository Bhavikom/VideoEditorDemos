// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKColorSelectiveFilter extends SelesFilter implements SelesParameters.FilterParameterInterface
{
    private float a;
    private float b;
    private int c;
    private int d;
    
    public TuSDKColorSelectiveFilter() {
        super("-sc4");
        this.a = 0.0f;
        this.b = 0.2f;
    }
    
    public TuSDKColorSelectiveFilter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null) {
            if (filterOption.args.containsKey("hue")) {
                final float float1 = Float.parseFloat(filterOption.args.get("hue"));
                if (float1 > 0.0f) {
                    this.a(float1);
                }
            }
            if (filterOption.args.containsKey("hueSpace")) {
                final float float2 = Float.parseFloat(filterOption.args.get("hueSpace"));
                if (float2 > 0.0f) {
                    this.b(float2);
                }
            }
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.c = this.mFilterProgram.uniformIndex("hue");
        this.d = this.mFilterProgram.uniformIndex("hueSpace");
        this.a(this.a);
        this.b(this.b);
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
        this.setFloat(this.a = a, this.c, this.mFilterProgram);
    }
    
    private float b() {
        return this.b;
    }
    
    private void b(final float b) {
        this.setFloat(this.b = b, this.d, this.mFilterProgram);
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("hue", this.a());
        initParams.appendFloatArg("hueSpace", this.b());
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("hue")) {
            this.a(filterArg.getValue());
        }
        else if (filterArg.equalsKey("hueSpace")) {
            this.b(filterArg.getValue());
        }
    }
}
