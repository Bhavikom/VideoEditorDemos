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

public class TuSDKLiveHeartbeatFilter extends SelesFilter
{
    private int c;
    private int d;
    private float e;
    private PointF f;
    private float g;
    int a;
    int b;
    
    public TuSDKLiveHeartbeatFilter() {
        super("-slive09f");
        this.e = 0.0f;
        this.f = new PointF(0.2f, 0.2f);
        this.g = 0.0f;
    }
    
    public TuSDKLiveHeartbeatFilter(final FilterOption filterOption) {
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
        this.c = this.mFilterProgram.uniformIndex("strength");
        this.d = this.mFilterProgram.uniformIndex("offset");
        this.setStrength(this.e);
        this.setOffset(this.f);
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
        return this.e;
    }
    
    public void setStrength(final float e) {
        this.setFloat(this.e = e, this.c, this.mFilterProgram);
    }
    
    public PointF getOffset() {
        return this.f;
    }
    
    public void setOffset(final PointF f) {
        this.setPoint(this.f = f, this.d, this.mFilterProgram);
    }
    
    public void setOffsetX(final float n) {
        this.setOffset(new PointF(n, this.f.y));
    }
    
    public void setOffsetY(final float n) {
        this.setOffset(new PointF(this.f.x, n));
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
    
    private void a() {
        final float[] array = { 0.15f, 0.08f, 0.02f };
        if (this.b == 0) {
            this.a = 0;
            this.b = 0;
        }
        if (this.b % 3 == 0) {
            this.a = 0;
        }
        ++this.b;
        if (this.b <= 9) {
            this.getParameter().setFilterArg("strength", array[this.a]);
            ++this.a;
        }
        else {
            this.getParameter().setFilterArg("strength", 0.0f);
        }
        if (this.b >= 19) {
            this.b = 0;
        }
        this.submitParameter();
    }
}
