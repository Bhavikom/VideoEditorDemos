// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.util.Random;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;

public class TuSDKLiveOldTVFilter extends SelesTwoInputFilter
{
    private int a;
    private int b;
    private int c;
    private float d;
    private float e;
    private float[] f;
    private float g;
    private int h;
    private int i;
    
    public TuSDKLiveOldTVFilter() {
        super("-slive11f");
        this.d = 0.0f;
        this.e = 0.0f;
        this.f = new float[] { 0.0f, 0.0f, 0.0f, 0.0f };
        this.g = 1.0f;
        this.h = 0;
        this.i = 0;
        this.disableSecondFrameCheck();
    }
    
    public TuSDKLiveOldTVFilter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null) {
            float n = 0.0f;
            float n2 = 0.0f;
            if (filterOption.args.containsKey("noiseX")) {
                final float float1 = Float.parseFloat(filterOption.args.get("noiseX"));
                if (float1 > 0.0f) {
                    n = float1;
                }
            }
            if (filterOption.args.containsKey("noiseY")) {
                final float float2 = Float.parseFloat(filterOption.args.get("noiseY"));
                if (float2 > 0.0f) {
                    n2 = float2;
                }
            }
            this.f = new float[] { n, n2, 0.0f, 0.0f };
            this.setNoise(new float[] { n, n2, 0.0f, 0.0f });
            if (filterOption.args.containsKey("animation")) {
                final float float3 = Float.parseFloat(filterOption.args.get("animation"));
                if (float3 > 0.0f) {
                    this.setAnimation(float3);
                }
            }
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.a = this.mFilterProgram.uniformIndex("screenPercent");
        this.b = this.mFilterProgram.uniformIndex("lineSpeed");
        this.c = this.mFilterProgram.uniformIndex("noise");
        this.setScreenPercent(this.d);
        this.setLineSpeed(this.e);
        this.setNoise(this.f);
    }
    
    @Override
    public void setInputRotation(final ImageOrientation imageOrientation, final int n) {
        super.setInputRotation(imageOrientation, n);
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long n) {
        this.a();
        super.informTargetsAboutNewFrame(n);
    }
    
    public float getScreenPercent() {
        return this.d;
    }
    
    public void setScreenPercent(final float d) {
        this.setFloat(this.d = d, this.a, this.mFilterProgram);
    }
    
    public float getLineSpeed() {
        return this.e;
    }
    
    public void setLineSpeed(final float e) {
        this.setFloat(this.e = e, this.b, this.mFilterProgram);
    }
    
    public float[] getNoise() {
        return this.f;
    }
    
    public void setNoise(final float[] f) {
        this.setVec4(this.f = f, this.c, this.mFilterProgram);
    }
    
    public void setNoiseX(final float n) {
        final float[] noise = this.getNoise();
        noise[0] = n;
        this.setNoise(noise);
    }
    
    public void setNoiseY(final float n) {
        final float[] noise = this.getNoise();
        noise[1] = n;
        this.setNoise(noise);
    }
    
    public void setNoiseType(final float n) {
        final float[] noise = this.getNoise();
        noise[2] = n;
        this.setNoise(noise);
    }
    
    public void setNoiseMixed(final float n) {
        final float[] noise = this.getNoise();
        noise[3] = n;
        this.setNoise(noise);
    }
    
    public float getAnimation() {
        return this.g;
    }
    
    public void setAnimation(final float g) {
        this.g = g;
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("screenPercent", this.getScreenPercent(), 0.0f, 1.0f);
        initParams.appendFloatArg("lineSpeed", this.getLineSpeed(), 0.0f, 1.0f);
        initParams.appendFloatArg("noiseX", this.getNoise()[0], -1.0f, 1.0f);
        initParams.appendFloatArg("noiseY", this.getNoise()[1], -1.0f, 1.0f);
        initParams.appendFloatArg("noiseType", this.getNoise()[2], 0.0f, 1.0f);
        initParams.appendFloatArg("noiseMixed", this.getNoise()[3], 0.0f, 1.0f);
        initParams.appendFloatArg("animation", this.getAnimation(), 0.0f, 1.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("screenPercent")) {
            this.setScreenPercent(filterArg.getValue());
        }
        else if (filterArg.equalsKey("lineSpeed")) {
            this.setLineSpeed(filterArg.getValue());
        }
        else if (filterArg.equalsKey("noiseX")) {
            this.setNoiseX(filterArg.getValue());
        }
        else if (filterArg.equalsKey("noiseY")) {
            this.setNoiseY(filterArg.getValue());
        }
        else if (filterArg.equalsKey("noiseType")) {
            this.setNoiseType(filterArg.getValue());
        }
        else if (filterArg.equalsKey("noiseMixed")) {
            this.setNoiseMixed(filterArg.getValue());
        }
        else if (filterArg.equalsKey("animation")) {
            this.setAnimation(filterArg.getValue());
        }
    }
    
    private void a() {
        final float[] array = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.35f, 0.4f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.15f, 0.25f, 0.45f, 0.65f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.55f, 0.15f, 0.25f, 0.3f, 0.35f, 0.3f, 0.28f, 0.25f, 0.25f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.3f, 0.35f, 0.3f };
        if (this.h == 103) {
            this.h = 0;
        }
        if (this.h < 102) {
            this.getParameter().setFilterArg("screenPercent", array[this.h]);
        }
        if (this.i >= 25) {
            this.i = 0;
        }
        final Random random = new Random();
        this.getParameter().setFilterArg("lineSpeed", this.i / 100.0f);
        this.getParameter().setFilterArg("noiseX", random.nextInt(100) / 100.0f);
        this.getParameter().setFilterArg("noiseY", random.nextInt(100) / 100.0f);
        this.getParameter().setFilterArg("noiseType", 0.41f);
        this.getParameter().setFilterArg("noiseMixed", 0.8f);
        ++this.h;
        ++this.i;
        this.submitParameter();
    }
}
