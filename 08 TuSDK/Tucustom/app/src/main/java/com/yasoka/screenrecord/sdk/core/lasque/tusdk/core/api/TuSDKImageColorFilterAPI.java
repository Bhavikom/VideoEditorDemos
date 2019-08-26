// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api;

//import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorAdjustmentFilter;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorAdjustmentFilter;

public final class TuSDKImageColorFilterAPI extends TuSDKImageFilterAPI
{
    public static final String KEY_BRIGHTNESS = "brightness";
    public static final String KEY_CONTRAST = "contrast";
    public static final String KEY_SATURATION = "saturation";
    public static final String KEY_EXPOSURE = "exposure";
    public static final String KEY_SHADOWS = "shadows";
    public static final String KEY_HIGHLIGHTS = "highlights";
    public static final String KEY_TEMPERATURE = "temperature";
    private FilterWrap a;
    
    @Override
    protected FilterWrap getFilterWrap() {
        if (this.a == null) {
            final FilterOption filterOption = new FilterOption() {
                @Override
                public SelesOutInput getFilter() {
                    return new TuSDKColorAdjustmentFilter();
                }
            };
            filterOption.id = Long.MAX_VALUE;
            filterOption.canDefinition = true;
            filterOption.isInternal = true;
            this.a = FilterWrap.creat(filterOption);
        }
        return this.a;
    }
    
    public void setBrightnessPrecentValue(final float n) {
        this.setFilterArgPrecentValue("brightness", n);
    }
    
    public void setContrastPrecentValue(final float n) {
        this.setFilterArgPrecentValue("contrast", n);
    }
    
    public void setSaturationPrecentValue(final float n) {
        this.setFilterArgPrecentValue("saturation", n);
    }
    
    public void setExposurePrecentValue(final float n) {
        this.setFilterArgPrecentValue("exposure", n);
    }
    
    public void setShadowsPrecentValue(final float n) {
        this.setFilterArgPrecentValue("shadows", n);
    }
    
    public void setHighlightsPrecentValue(final float n) {
        this.setFilterArgPrecentValue("highlights", n);
    }
    
    public void setTemperaturePrecentValue(final float n) {
        this.setFilterArgPrecentValue("temperature", n);
    }
}
