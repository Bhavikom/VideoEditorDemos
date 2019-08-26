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

public class TuSDKLiveLightningFilter extends SelesFilter
{
    private int c;
    private float d;
    int a;
    int b;
    
    public TuSDKLiveLightningFilter() {
        super("-slive07f");
        this.d = -0.5f;
    }
    
    public TuSDKLiveLightningFilter(final FilterOption filterOption) {
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
        this.c = this.mFilterProgram.uniformIndex("strength");
        this.setStrength(this.d);
        this.checkGLError("TuSDKLiveLightningFilter onInitOnGLThread");
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long n) {
        this.a();
        super.informTargetsAboutNewFrame(n);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, floatBuffer2);
        this.checkGLError("TuSDKLiveLightningFilter");
        this.captureFilterImage("TuSDKLiveLightningFilter", this.mInputTextureSize.width, this.mInputTextureSize.height);
    }
    
    public float getStrength() {
        return this.d;
    }
    
    public void setStrength(final float d) {
        this.setFloat(this.d = d, this.c, this.mFilterProgram);
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("strength", this.getStrength(), -0.5f, 0.7f);
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
        final int[] array = { 14, 15, 16, 18, 19, 21, 22, 23, 27, 30, 33, 34, 35, 37 };
        final float[] array2 = { -0.5f, 0.6f, 0.7f, -0.5f, 0.7f, -0.5f, 0.6f, 0.7f, -0.5f, 0.7f, 0.6f, 0.7f, 0.6f, 0.7f };
        if (this.a == 0) {
            this.b = 0;
        }
        ++this.a;
        if (this.a > array[this.b]) {
            ++this.b;
        }
        this.getParameter().setFilterArg("strength", array2[this.b]);
        if (this.a >= 37) {
            this.a = 0;
        }
        this.submitParameter();
    }
}
