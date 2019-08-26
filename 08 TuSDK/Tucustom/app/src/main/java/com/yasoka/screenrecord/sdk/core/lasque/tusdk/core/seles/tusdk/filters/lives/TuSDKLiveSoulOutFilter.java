// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives;

//import org.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKLiveSoulOutFilter extends SelesFilter
{
    private int a;
    private float b;
    private float c;
    
    public TuSDKLiveSoulOutFilter() {
        super("-slive03f");
        this.b = 0.0f;
        this.c = 1.0f;
    }
    
    public TuSDKLiveSoulOutFilter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null) {
            if (filterOption.args.containsKey("strength")) {
                final float float1 = Float.parseFloat(filterOption.args.get("strength"));
                if (float1 > 0.0f) {
                    this.setStrength(float1);
                }
            }
            if (filterOption.args.containsKey("animation")) {
                final float float2 = Float.parseFloat(filterOption.args.get("animation"));
                if (float2 > 0.0f) {
                    this.setAnimation(float2);
                }
            }
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.a = this.mFilterProgram.uniformIndex("strength");
        this.setStrength(this.b);
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long n) {
        this.a(n);
        super.informTargetsAboutNewFrame(n);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, floatBuffer2);
        this.checkGLError(this.getClass().getSimpleName());
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    }
    
    public float getStrength() {
        return this.b;
    }
    
    public void setStrength(final float b) {
        this.setFloat(this.b = b, this.a, this.mFilterProgram);
    }
    
    public float getAnimation() {
        return this.c;
    }
    
    public void setAnimation(final float c) {
        this.c = c;
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("strength", this.getStrength(), 0.0f, 0.25f);
        initParams.appendFloatArg("animation", this.getAnimation(), 0.0f, 1.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("strength")) {
            this.setStrength(filterArg.getValue());
        }
        else if (filterArg.equalsKey("animation")) {
            this.setAnimation(filterArg.getValue());
        }
    }
    
    private void a(final long n) {
        if (this.getAnimation() < 0.5f) {
            return;
        }
        float n2 = (int)(System.currentTimeMillis() % 500) / 360.0f;
        if (n2 > 1.0) {
            n2 = 0.0f;
        }
        this.getParameter().setFilterArg("strength", n2);
        this.submitParameter();
    }
}
