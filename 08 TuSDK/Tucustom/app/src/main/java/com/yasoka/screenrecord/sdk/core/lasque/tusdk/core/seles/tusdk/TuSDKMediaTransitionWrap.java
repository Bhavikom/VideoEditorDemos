// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk;

//import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveZoomFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveStackUpFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFocusFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFlipFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFlashLightFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveSpreadInFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLivePullInFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFlyInFilter;
//import org.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFadeInFilter;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.SelesParameters;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFadeInFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFlashLightFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFlipFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFlyInFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveFocusFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLivePullInFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveSpreadInFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveStackUpFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.trans.TuSDKLiveZoomFilter;

public class TuSDKMediaTransitionWrap extends FilterWrap implements SelesParameters.FilterParameterInterface
{
    private TuSDKMediaTransitionType a;
    
    @Override
    public SelesParameters getParameter() {
        return this.mFilter.getParameter();
    }
    
    @Override
    public void setParameter(final SelesParameters selesParameters) {
    }
    
    @Override
    public void submitParameter() {
        this.mFilter.submitParameter();
    }
    
    public TuSDKMediaTransitionWrap(final TuSDKMediaTransitionType a) {
        this.a = a;
        final SelesOutInput a2 = this.a(a);
        this.mLastFilter = a2;
        this.mFilter = a2;
    }
    
    private SelesOutInput a(final TuSDKMediaTransitionType tuSDKMediaTransitionType) {
        switch (tuSDKMediaTransitionType.ordinal()) {
            case 1: {
                return new TuSDKLiveFadeInFilter();
            }
            case 2: {
                return new TuSDKLiveFlyInFilter();
            }
            case 3: {
                return new TuSDKLivePullInFilter(TuSDKLivePullInFilter.PullInDirection.RIGHT);
            }
            case 4: {
                return new TuSDKLivePullInFilter(TuSDKLivePullInFilter.PullInDirection.LEFT);
            }
            case 5: {
                return new TuSDKLivePullInFilter(TuSDKLivePullInFilter.PullInDirection.TOP);
            }
            case 6: {
                return new TuSDKLivePullInFilter(TuSDKLivePullInFilter.PullInDirection.BOTTOM);
            }
            case 7: {
                return new TuSDKLiveSpreadInFilter();
            }
            case 8: {
                return new TuSDKLiveFlashLightFilter();
            }
            case 9: {
                return new TuSDKLiveFlipFilter();
            }
            case 10: {
                return new TuSDKLiveFocusFilter(0.0f);
            }
            case 11: {
                return new TuSDKLiveFocusFilter(1.0f);
            }
            case 12: {
                return new TuSDKLiveStackUpFilter();
            }
            case 13: {
                return new TuSDKLiveZoomFilter();
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public FilterWrap clone() {
        return new TuSDKMediaTransitionWrap(this.a);
    }
    
    public enum TuSDKMediaTransitionType
    {
        TuSDKMediaTransitionTypeFadeIn, 
        TuSDKMediaTransitionTypeFlyIn, 
        TuSDKMediaTransitionTypePullInRight, 
        TuSDKMediaTransitionTypePullInLeft, 
        TuSDKMediaTransitionTypePullInTop, 
        TuSDKMediaTransitionTypePullInBottom, 
        TuSDKMediaTransitionTypeSpreadIn, 
        TuSDKMediaTransitionTypeFlashLight, 
        TuSDKMediaTransitionTypeFlip, 
        TuSDKMediaTransitionTypeFocusOut, 
        TuSDKMediaTransitionTypeFocusIn, 
        TuSDKMediaTransitionTypeStackUp, 
        TuSDKMediaTransitionTypeZoom;
    }
}
