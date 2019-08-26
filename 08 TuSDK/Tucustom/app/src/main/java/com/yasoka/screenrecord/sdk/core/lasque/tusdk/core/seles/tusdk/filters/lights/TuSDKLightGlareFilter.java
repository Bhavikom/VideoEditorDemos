// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lights;

//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSDKLightGlareFilter extends SelesTwoInputFilter implements SelesParameters.FilterParameterInterface
{
    private float a;
    private float b;
    private int c;
    private int d;
    private ImageOrientation e;
    
    public TuSDKLightGlareFilter() {
        super("-sl1");
        this.a = 0.8f;
        this.e = ImageOrientation.Up;
        this.disableSecondFrameCheck();
    }
    
    public TuSDKLightGlareFilter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null && filterOption.args.containsKey("mixied")) {
            final float float1 = Float.parseFloat(filterOption.args.get("mixied"));
            if (float1 > 0.0f) {
                this.setMix(float1);
            }
        }
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.c = this.mFilterProgram.uniformIndex("mixturePercent");
        this.d = this.mFilterProgram.uniformIndex("aspectRatio");
        this.setMix(this.a);
        this.a();
    }
    
    private void a() {
        if (!this.mInputTextureSize.isSize() || this.e == null) {
            return;
        }
        if (this.e.isTransposed()) {
            this.a(this.mInputTextureSize.width / (float)this.mInputTextureSize.height);
        }
        else {
            this.a(this.mInputTextureSize.height / (float)this.mInputTextureSize.width);
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
    public void setInputRotation(final ImageOrientation e, final int n) {
        super.setInputRotation(e, n);
        if (n > 0 && this.e != e) {
            this.e = e;
            this.a();
        }
    }
    
    public float getMix() {
        return this.a;
    }
    
    public void setMix(final float a) {
        this.setFloat(this.a = a, this.c, this.mFilterProgram);
    }
    
    private void a(final float b) {
        this.b = b;
        if (this.b > 0.0f) {
            this.setFloat(this.b, this.d, this.mFilterProgram);
        }
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("mixied", this.a);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("mixied")) {
            this.setMix(filterArg.getValue());
        }
    }
}
