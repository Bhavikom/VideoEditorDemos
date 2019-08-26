// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.skins;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSurfaceBlurFilter;

//import org.lasque.tusdk.core.seles.sources.SelesPicture;
import java.util.List;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSurfaceBlurFilter;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;

public class TuSDKSkinColor2Filter extends SelesFilterGroup implements SelesParameters.FilterParameterInterface, SelesParameters.FilterTexturesInterface
{
    private float a;
    private float b;
    private float c;
    private float d;
    private float e;
    private float f;
    private TuSDKSurfaceBlurFilter g;
    private TuSDKSkinColor2MixedFilter h;
    
    public TuSDKSkinColor2Filter() {
        (this.g = new TuSDKSurfaceBlurFilter()).setScale(0.5f);
        this.addFilter(this.h = new TuSDKSkinColor2MixedFilter());
        this.g.addTarget(this.h, 1);
        this.setInitialFilters(this.g, this.h);
        this.setTerminalFilter(this.h);
        this.setSmoothing(1.0f);
        this.setMixed(0.5f);
        this.setBlurSize(this.g.getMaxBlursize());
        this.setThresholdLevel(this.g.getMaxThresholdLevel());
        this.setLightLevel(1.0f);
        this.setDetailLevel(0.18f);
    }
    
    public TuSDKSkinColor2Filter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null) {
            if (filterOption.args.containsKey("smoothing")) {
                final float float1 = Float.parseFloat(filterOption.args.get("smoothing"));
                if (float1 > 0.0f) {
                    this.setSmoothing(float1);
                }
            }
            if (filterOption.args.containsKey("mixied")) {
                final float float2 = Float.parseFloat(filterOption.args.get("mixied"));
                if (float2 > 0.0f) {
                    this.setMixed(float2);
                }
            }
            if (filterOption.args.containsKey("lightLevel")) {
                final float float3 = Float.parseFloat(filterOption.args.get("lightLevel"));
                if (float3 > 0.0f) {
                    this.setLightLevel(float3);
                }
            }
            if (filterOption.args.containsKey("detailLevel")) {
                final float float4 = Float.parseFloat(filterOption.args.get("detailLevel"));
                if (float4 > 0.0f) {
                    this.setDetailLevel(float4);
                }
            }
        }
    }
    
    public float getSmoothing() {
        return this.a;
    }
    
    public void setSmoothing(final float a) {
        this.a = a;
        this.h.setIntensity(this.a);
    }
    
    public float getMixed() {
        return this.b;
    }
    
    public void setMixed(final float b) {
        this.b = b;
        this.h.setMixed(this.b);
    }
    
    public float getBlurSize() {
        return this.c;
    }
    
    public void setBlurSize(final float c) {
        this.c = c;
        this.g.setBlurSize(this.c);
    }
    
    public float getThresholdLevel() {
        return this.d;
    }
    
    public void setThresholdLevel(final float d) {
        this.d = d;
        this.g.setThresholdLevel(this.d);
    }
    
    public void setLightLevel(final float e) {
        this.e = e;
        this.h.setLightLevel(this.e);
    }
    
    public float getLightLevel() {
        return this.e;
    }
    
    public void setDetailLevel(final float f) {
        this.f = f;
        this.h.setDetailLevel(this.f);
    }
    
    public float getDetailLevel() {
        return this.f;
    }
    
    @Override
    public void appendTextures(final List<SelesPicture> list) {
        if (list == null) {
            return;
        }
        int n = 2;
        for (final SelesPicture selesPicture : list) {
            selesPicture.processImage();
            selesPicture.addTarget(this.h, n);
            ++n;
        }
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("smoothing", this.a, 0.0f, 1.0f);
        initParams.appendFloatArg("mixied", this.getMixed());
        initParams.appendFloatArg("whitening", this.getLightLevel(), 1.0f, 0.5f);
        if (initParams.getDefaultArg("debug") > 0.0f) {
            initParams.appendFloatArg("blurSize", this.getBlurSize(), 0.0f, this.g.getMaxBlursize());
            initParams.appendFloatArg("thresholdLevel", this.getThresholdLevel(), 0.0f, this.g.getMaxThresholdLevel());
            initParams.appendFloatArg("detailLevel", this.getDetailLevel(), 0.0f, 1.0f);
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
        else if (filterArg.equalsKey("mixied")) {
            this.setMixed(filterArg.getValue());
        }
        else if (filterArg.equalsKey("blurSize")) {
            this.setBlurSize(filterArg.getValue());
        }
        else if (filterArg.equalsKey("thresholdLevel")) {
            this.setThresholdLevel(filterArg.getValue());
        }
        else if (filterArg.equalsKey("whitening")) {
            this.setLightLevel(filterArg.getValue());
        }
        else if (filterArg.equalsKey("detailLevel")) {
            this.setDetailLevel(filterArg.getValue());
        }
    }
}
