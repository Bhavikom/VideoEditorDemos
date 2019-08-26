// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base;

//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;

public class TuSDKGaussianBlurSevenRadiusFilter extends TuSDKGaussianBlurFiveRadiusFilter
{
    public TuSDKGaussianBlurSevenRadiusFilter() {
        super("-sgv7", "-sgf7");
    }
    
    public static TuSDKGaussianBlurFiveRadiusFilter hardware(final boolean b) {
        if (b && !TuSdkGPU.lowPerformance()) {
            return new TuSDKGaussianBlurSevenRadiusFilter();
        }
        return new TuSDKGaussianBlurFiveRadiusFilter(true);
    }
}
