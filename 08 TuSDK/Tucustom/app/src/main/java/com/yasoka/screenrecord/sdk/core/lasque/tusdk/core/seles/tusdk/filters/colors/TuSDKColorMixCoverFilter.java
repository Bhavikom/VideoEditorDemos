// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors;

//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.Color;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;

public class TuSDKColorMixCoverFilter extends SelesThreeInputFilter implements SelesParameters.FilterParameterInterface
{
    private int a;
    private int b;
    private int c;
    private int d;
    private PointF e;
    private int f;
    private float[] g;
    private int h;
    private float i;
    private int j;
    private float k;
    private float l;
    private float m;
    private float n;
    private ImageOrientation o;
    
    public TuSDKColorMixCoverFilter() {
        super("-scmc");
        this.e = new PointF(0.5f, 0.5f);
        this.g = new float[] { 0.0f, 0.0f, 0.0f };
        this.i = 0.25f;
        this.k = 1.0f;
        this.l = 1.0f;
        this.m = 1.0f;
        this.o = ImageOrientation.Up;
        this.disableSecondFrameCheck();
        this.disableThirdFrameCheck();
    }
    
    public TuSDKColorMixCoverFilter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null) {
            if (filterOption.args.containsKey("mixied")) {
                final float float1 = Float.parseFloat(filterOption.args.get("mixied"));
                if (float1 > 0.0f) {
                    this.setMixed(float1);
                }
            }
            if (filterOption.args.containsKey("vignette")) {
                final float float2 = Float.parseFloat(filterOption.args.get("vignette"));
                if (float2 > 0.0f) {
                    this.a(float2);
                }
            }
            if (filterOption.args.containsKey("texture")) {
                final float float3 = Float.parseFloat(filterOption.args.get("texture"));
                if (float3 > 0.0f) {
                    this.setCover(float3);
                }
            }
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.a = this.mFilterProgram.uniformIndex("mixturePercent");
        this.b = this.mFilterProgram.uniformIndex("coverPercent");
        this.d = this.mFilterProgram.uniformIndex("vignetteCenter");
        this.f = this.mFilterProgram.uniformIndex("vignetteColor");
        this.h = this.mFilterProgram.uniformIndex("vignetteStart");
        this.j = this.mFilterProgram.uniformIndex("vignetteEnd");
        this.c = this.mFilterProgram.uniformIndex("aspectRatio");
        this.setMixed(this.l);
        this.setCover(this.m);
        this.a(this.e);
        this.a(this.g);
        this.a(this.i);
        this.b(this.k);
        this.a();
    }
    
    public float getMixed() {
        return this.l;
    }
    
    public void setMixed(final float l) {
        this.setFloat(this.l = l, this.a, this.mFilterProgram);
    }
    
    public float getCover() {
        return this.m;
    }
    
    public void setCover(final float m) {
        this.setFloat(this.m = m, this.b, this.mFilterProgram);
    }
    
    private void a(final PointF e) {
        this.setPoint(this.e = e, this.d, this.mFilterProgram);
    }
    
    public void setVignetteColor(final int n) {
        this.a(new float[] { Color.red(n) / 255.0f, Color.green(n) / 255.0f, Color.blue(n) / 255.0f });
    }
    
    private void a(final float[] g) {
        this.setVec3(this.g = g, this.f, this.mFilterProgram);
    }
    
    private void a(final float i) {
        this.setFloat(this.i = i, this.h, this.mFilterProgram);
    }
    
    private void b(final float k) {
        this.setFloat(this.k = k, this.j, this.mFilterProgram);
    }
    
    private void c(final float n) {
        this.n = n;
        if (this.n > 0.0f) {
            this.setFloat(this.n, this.c, this.mFilterProgram);
        }
    }
    
    private void a() {
        if (!this.mInputTextureSize.isSize() || this.o == null) {
            return;
        }
        if (this.o.isTransposed()) {
            this.c(this.mInputTextureSize.width / (float)this.mInputTextureSize.height);
        }
        else {
            this.c(this.mInputTextureSize.height / (float)this.mInputTextureSize.width);
        }
    }
    
    @Override
    public void forceProcessingAtSize(final TuSdkSize tuSdkSize) {
        super.forceProcessingAtSize(tuSdkSize);
        this.a();
    }
    
    @Override
    public void setInputSize(final TuSdkSize tuSdkSize, final int n) {
        final TuSdkSize copy = this.mInputTextureSize.copy();
        super.setInputSize(tuSdkSize, n);
        if (n == 0 && !copy.equals(this.mInputTextureSize) && tuSdkSize.isSize()) {
            this.a();
        }
    }
    
    @Override
    public void setInputRotation(final ImageOrientation o, final int n) {
        super.setInputRotation(o, n);
        if (n > 0 && this.o != o) {
            this.o = o;
            this.a();
        }
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("mixied", this.getMixed());
        initParams.appendFloatArg("texture", this.getCover());
        initParams.appendFloatArg("vignette", this.i, 1.0f, 0.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("mixied")) {
            this.setMixed(filterArg.getValue());
        }
        else if (filterArg.equalsKey("texture")) {
            this.setCover(filterArg.getValue());
        }
        else if (filterArg.equalsKey("vignette")) {
            this.a(filterArg.getValue());
        }
    }
}
