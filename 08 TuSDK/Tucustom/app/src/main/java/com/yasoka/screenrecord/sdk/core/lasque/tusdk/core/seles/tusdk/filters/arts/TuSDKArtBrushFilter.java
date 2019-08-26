// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.arts;

//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.struct.TuSdkSizeF;
//import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurSevenRadiusFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSizeF;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

//import org.lasque.tusdk.core.seles.sources.SelesPicture;
import java.util.List;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurSevenRadiusFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKGaussianBlurFiveRadiusFilter;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;

public class TuSDKArtBrushFilter extends SelesFilterGroup implements SelesParameters.FilterParameterInterface, SelesParameters.FilterTexturesInterface
{
    private float a;
    private final TuSDKGaussianBlurFiveRadiusFilter b;
    private _TuSDKArtBrushFilter c;
    
    public TuSDKArtBrushFilter() {
        this.a = 0.0f;
        this.addFilter(this.b = TuSDKGaussianBlurSevenRadiusFilter.hardware(true));
        this.addFilter(this.c = new _TuSDKArtBrushFilter());
        this.b.addTarget(this.c, 0);
        this.setInitialFilters(this.b);
        this.setTerminalFilter(this.c);
        this.setMix(this.a);
    }
    
    public TuSDKArtBrushFilter(final FilterOption filterOption) {
        this();
        if (filterOption != null && filterOption.args != null && filterOption.args.containsKey("mixied")) {
            final float float1 = Float.parseFloat(filterOption.args.get("mixied"));
            if (float1 > 0.0f) {
                this.setMix(float1);
            }
        }
    }
    
    public float getMix() {
        return this.a;
    }
    
    public void setMix(final float n) {
        this.a = n;
        this.b.setBlurSize(n);
    }
    
    @Override
    public void appendTextures(final List<SelesPicture> list) {
        if (list == null) {
            return;
        }
        int n = 1;
        for (final SelesPicture selesPicture : list) {
            selesPicture.processImage();
            selesPicture.addTarget(this.c, n);
            ++n;
        }
    }
    
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("mixied", this.a, 0.0f, 3.0f);
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
    
    private static class _TuSDKArtBrushFilter extends SelesTwoInputFilter
    {
        private int a;
        
        public _TuSDKArtBrushFilter() {
            super("-sab1");
            this.disableSecondFrameCheck();
        }
        
        @Override
        protected void onInitOnGLThread() {
            super.onInitOnGLThread();
            this.a = this.mFilterProgram.uniformIndex("cropAspectRatio");
            this.a();
        }
        
        private void a() {
            if (!this.mInputTextureSize.isSize()) {
                return;
            }
            final TuSdkSizeF create = TuSdkSizeF.create(1.0f, 1.0f);
            if (this.mInputTextureSize.width > this.mInputTextureSize.height) {
                create.height = this.mInputTextureSize.height / (float)this.mInputTextureSize.width;
            }
            else {
                create.width = this.mInputTextureSize.width / (float)this.mInputTextureSize.height;
            }
            this.setSize(create, this.a, this.mFilterProgram);
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
            if (n == 0 && !copy.equals(this.mInputTextureSize)) {
                this.a();
            }
        }
        
        @Override
        public void setInputRotation(final ImageOrientation imageOrientation, final int n) {
            super.setInputRotation(imageOrientation, n);
            this.a();
        }
    }
}
