// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lights;

//import org.lasque.tusdk.core.seles.SelesGLProgram;
//import org.lasque.tusdk.core.struct.TuSdkSizeF;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSizeF;

public class TuSDKSobelEdgeFilter extends SelesTwoInputFilter implements SelesParameters.FilterParameterInterface
{
    private int a;
    private int b;
    private int c;
    private int d;
    private float e;
    private float f;
    private float g;
    private float h;
    
    public TuSDKSobelEdgeFilter() {
        super("-ssev1", "-ssef1");
        this.e = 1.0f;
        this.f = 1.0f;
        this.g = 0.0f;
        this.h = 0.03f;
        this.disableSecondFrameCheck();
    }
    
    public TuSDKSobelEdgeFilter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null) {
            if (filterOption.args.containsKey("edgeStrength")) {
                final float float1 = Float.parseFloat(filterOption.args.get("edgeStrength"));
                if (float1 > 0.0f) {
                    this.a(float1);
                }
            }
            if (filterOption.args.containsKey("thresholdLevel")) {
                final float float2 = Float.parseFloat(filterOption.args.get("thresholdLevel"));
                if (float2 > 0.0f) {
                    this.b(float2);
                }
            }
            if (filterOption.args.containsKey("speed")) {
                final float float3 = Float.parseFloat(filterOption.args.get("speed"));
                if (float3 > 0.0f) {
                    this.d(float3);
                }
            }
            if (filterOption.args.containsKey("showType")) {
                final float float4 = Float.parseFloat(filterOption.args.get("showType"));
                if (float4 > 0.0f) {
                    this.c(float4);
                }
            }
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.a = this.mFilterProgram.uniformIndex("stepOffset");
        this.b = this.mFilterProgram.uniformIndex("edgeStrength");
        this.c = this.mFilterProgram.uniformIndex("thresholdLevel");
        this.d = this.mFilterProgram.uniformIndex("showType");
        this.a(this.e);
        this.b(this.f);
        this.c(this.g);
        this.d(this.h);
    }
    
    @Override
    public void setupFilterForSize(final TuSdkSize tuSdkSize) {
        super.setupFilterForSize(tuSdkSize);
        if (tuSdkSize == null || !tuSdkSize.isSize()) {
            return;
        }
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                TuSDKSobelEdgeFilter.this.setSize(TuSdkSizeF.create(1.0f / tuSdkSize.width, 1.0f / tuSdkSize.height), TuSDKSobelEdgeFilter.this.a, TuSDKSobelEdgeFilter.this.mFilterProgram);
                if (TuSDKSobelEdgeFilter.this.b() < 0.0f) {
                    TuSDKSobelEdgeFilter.this.f = 1.0f;
                }
                TuSDKSobelEdgeFilter.this.b(TuSDKSobelEdgeFilter.this.f - TuSDKSobelEdgeFilter.this.h);
            }
        });
    }
    
    private float a() {
        return this.e;
    }
    
    private void a(final float e) {
        this.setFloat(this.e = e, this.b, this.mFilterProgram);
    }
    
    private float b() {
        return this.f;
    }
    
    private void b(final float f) {
        this.setFloat(this.f = f, this.c, this.mFilterProgram);
    }
    
    private float c() {
        return this.g;
    }
    
    private void c(final float g) {
        this.setFloat(this.g = g, this.d, this.mFilterProgram);
    }
    
    private float d() {
        return this.h;
    }
    
    private void d(final float h) {
        if (h > 0.0f) {
            this.h = h;
        }
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("edgeStrength", this.a(), 0.0f, 4.0f);
        initParams.appendFloatArg("thresholdLevel", this.b(), 0.0f, 1.0f);
        initParams.appendFloatArg("speed", this.d(), 0.0f, 0.1f);
        initParams.appendFloatArg("showType", this.c(), 0.0f, 4.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("edgeStrength")) {
            this.a(filterArg.getValue());
        }
        else if (filterArg.equalsKey("thresholdLevel")) {
            this.b(filterArg.getValue());
        }
        else if (filterArg.equalsKey("speed")) {
            this.d(filterArg.getValue());
        }
        else if (filterArg.equalsKey("showType")) {
            this.c(filterArg.getValue());
        }
    }
}
