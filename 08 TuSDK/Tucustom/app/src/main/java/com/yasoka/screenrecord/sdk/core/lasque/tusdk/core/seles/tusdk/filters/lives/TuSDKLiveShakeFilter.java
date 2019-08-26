// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lives;

//import org.lasque.tusdk.core.seles.SelesParameters;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class TuSDKLiveShakeFilter extends SelesFilter
{
    private int f;
    private int g;
    private float h;
    private PointF i;
    private float j;
    int a;
    long b;
    long c;
    int d;
    int e;
    
    public TuSDKLiveShakeFilter() {
        super("-slive01f");
        this.h = 0.0f;
        this.i = new PointF(0.2f, 0.2f);
        this.j = 0.0f;
    }
    
    public TuSDKLiveShakeFilter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null) {
            if (filterOption.args.containsKey("strength")) {
                final float float1 = Float.parseFloat(filterOption.args.get("strength"));
                if (float1 > 0.0f) {
                    this.setStrength(float1);
                }
            }
            float n = 0.2f;
            float n2 = 0.2f;
            if (filterOption.args.containsKey("offsetX")) {
                final float float2 = Float.parseFloat(filterOption.args.get("offsetX"));
                if (float2 > 0.0f) {
                    n = float2;
                }
            }
            if (filterOption.args.containsKey("offsetY")) {
                final float float3 = Float.parseFloat(filterOption.args.get("offsetY"));
                if (float3 > 0.0f) {
                    n2 = float3;
                }
            }
            this.setOffset(new PointF(n, n2));
            if (filterOption.args.containsKey("animation")) {
                this.setAnimation(Math.max(Float.parseFloat(filterOption.args.get("animation")), 0.0f));
            }
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.f = this.mFilterProgram.uniformIndex("strength");
        this.g = this.mFilterProgram.uniformIndex("offset");
        this.setStrength(this.h);
        this.setOffset(this.i);
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    @Override
    protected void informTargetsAboutNewFrame(final long n) {
        this.a(System.currentTimeMillis());
        super.informTargetsAboutNewFrame(n);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, floatBuffer2);
        this.checkGLError(this.getClass().getSimpleName());
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    }
    
    public float getStrength() {
        return this.h;
    }
    
    public void setStrength(final float h) {
        this.setFloat(this.h = h, this.f, this.mFilterProgram);
    }
    
    public PointF getOffset() {
        return this.i;
    }
    
    public void setOffset(final PointF i) {
        this.setPoint(this.i = i, this.g, this.mFilterProgram);
    }
    
    public void setOffsetX(final float n) {
        this.setOffset(new PointF(n, this.i.y));
    }
    
    public void setOffsetY(final float n) {
        this.setOffset(new PointF(this.i.x, n));
    }
    
    public float getAnimation() {
        return this.j;
    }
    
    public void setAnimation(final float j) {
        this.j = j;
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("strength", this.getStrength(), 0.0f, 0.5f);
        initParams.appendFloatArg("offsetX", this.getOffset().x, -1.0f, 1.0f);
        initParams.appendFloatArg("offsetY", this.getOffset().y, -1.0f, 1.0f);
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
        else if (filterArg.equalsKey("offsetX")) {
            this.setOffsetX(filterArg.getValue());
        }
        else if (filterArg.equalsKey("offsetY")) {
            this.setOffsetY(filterArg.getValue());
        }
        else if (filterArg.equalsKey("animation")) {
            this.setAnimation(filterArg.getValue());
        }
    }
    
    private void a(final long n) {
        final long n2 = n / 50L;
        if (n2 == this.b) {
            return;
        }
        this.b = n2;
        final int[] array = { 0, 6, 12 };
        final float[] array2 = { 0.038f, 0.0f };
        final float[] array3 = { 0.0f, 0.0f };
        if (this.d == 0 || this.d >= array[array.length - 1] - 1) {
            this.d = 0;
            this.a = 0;
            this.b = -1L;
            this.e = 0;
            this.c = n2;
        }
        ++this.d;
        if (this.d >= array[this.a + 1] || this.d == 0) {
            if (this.d != 0) {
                ++this.a;
            }
            this.e = 0;
            this.getParameter().setFilterArg("strength", array3[this.a]);
        }
        else {
            ++this.e;
            this.getParameter().setFilterArg("strength", array2[this.a] * this.e + array3[this.a]);
        }
        this.submitParameter();
    }
}
