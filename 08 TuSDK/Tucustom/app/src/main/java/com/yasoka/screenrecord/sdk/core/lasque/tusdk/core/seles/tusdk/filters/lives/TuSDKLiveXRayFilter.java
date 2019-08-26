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

public class TuSDKLiveXRayFilter extends SelesFilter
{
    private int b;
    private float c;
    int a;
    
    public TuSDKLiveXRayFilter() {
        super("-slive08f");
        this.c = 0.0f;
    }
    
    public TuSDKLiveXRayFilter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null && filterOption.args.containsKey("strength")) {
            final float float1 = Float.parseFloat(filterOption.args.get("strength"));
            if (float1 > 0.0f) {
                this.setStrength(float1);
            }
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.b = this.mFilterProgram.uniformIndex("strength");
        this.setStrength(this.c);
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long n) {
        this.a();
        super.informTargetsAboutNewFrame(n);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, floatBuffer2);
        this.checkGLError(this.getClass().getSimpleName());
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    }
    
    public float getStrength() {
        return this.c;
    }
    
    public void setStrength(final float c) {
        this.setFloat(this.c = c, this.b, this.mFilterProgram);
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("strength", this.getStrength(), 0.0f, 1.0f);
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
    }
    
    private void a() {
        if (this.a < 3) {
            this.getParameter().setFilterArg("strength", 0.0f);
        }
        else {
            this.getParameter().setFilterArg("strength", 1.0f);
        }
        ++this.a;
        if (this.a >= 6) {
            this.a = 0;
        }
        this.submitParameter();
    }
}
