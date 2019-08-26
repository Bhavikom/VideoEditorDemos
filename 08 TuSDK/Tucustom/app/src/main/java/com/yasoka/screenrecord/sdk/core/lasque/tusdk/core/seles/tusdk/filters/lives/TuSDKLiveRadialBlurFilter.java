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

public class TuSDKLiveRadialBlurFilter extends SelesFilter
{
    private int a;
    private int b;
    private float c;
    private float d;
    private float e;
    private int f;
    
    public TuSDKLiveRadialBlurFilter() {
        super("-slive15f");
        this.c = 0.0f;
        this.d = 0.0f;
        this.e = 1.0f;
    }
    
    public TuSDKLiveRadialBlurFilter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null && filterOption.args.containsKey("animation")) {
            final float float1 = Float.parseFloat(filterOption.args.get("animation"));
            if (float1 > 0.0f) {
                this.setAnimation(float1);
            }
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.a = this.mFilterProgram.uniformIndex("radialBlur");
        this.b = this.mFilterProgram.uniformIndex("scale");
        this.setRadialBlur(this.c);
        this.setSunkenScale(this.d);
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    public float getRadialBlur() {
        return this.c;
    }
    
    public void setRadialBlur(final float c) {
        this.setFloat(this.c = c, this.a, this.mFilterProgram);
    }
    
    public float getSunkenScale() {
        return this.d;
    }
    
    public void setSunkenScale(final float d) {
        this.setFloat(this.d = d, this.b, this.mFilterProgram);
    }
    
    public float getAnimation() {
        return this.e;
    }
    
    public void setAnimation(final float e) {
        this.e = e;
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
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("radialBlur", this.getRadialBlur(), 0.0f, 60.0f);
        initParams.appendFloatArg("scale", this.getSunkenScale(), 0.0f, 1.0f);
        initParams.appendFloatArg("animation", this.getAnimation(), 0.0f, 1.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("radialBlur")) {
            this.setRadialBlur(filterArg.getValue());
        }
        else if (filterArg.equalsKey("scale")) {
            this.setSunkenScale(filterArg.getValue());
        }
        else if (filterArg.equalsKey("animation")) {
            this.setAnimation(filterArg.getValue());
        }
    }
    
    private void a(final long n) {
        if (this.getAnimation() < 0.5) {
            return;
        }
        final long n2 = n % 1000000000L;
        final long[] array = { 0L, 66666666L, 133333333L, 200000000L, 266666666L, 333333333L, 399999999L };
        final float[] array2 = { 0.0f, 0.3f, 0.26f, 0.22f, 0.17f, 0.1f, 0.05f };
        final float[] array3 = { 0.0f, 0.35f, 0.28f, 0.21f, 0.14f, 0.07f, 0.03f };
        if (n2 > array[array.length - 1]) {
            this.getParameter().setFilterArg("radialBlur", 0.0f);
            this.getParameter().setFilterArg("scale", 0.0f);
        }
        for (int i = 1; i < array.length; ++i) {
            if (n2 > array[i - 1] && n2 <= array[i]) {
                this.getParameter().setFilterArg("radialBlur", (array2[i] > 0.05) ? (array2[i] - ((this.f == i) ? 0.01f : 0.0f)) : array2[i]);
                this.getParameter().setFilterArg("scale", array3[i] - ((this.f == i) ? 0.02f : 0.0f));
                this.f = i;
                break;
            }
        }
        this.submitParameter();
    }
}
