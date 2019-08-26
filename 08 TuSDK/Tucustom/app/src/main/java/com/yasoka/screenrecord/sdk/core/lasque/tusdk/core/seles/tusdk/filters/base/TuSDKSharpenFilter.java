// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base;

//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.seles.filters.image.SelesSharpenFilter;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.image.SelesSharpenFilter;

public class TuSDKSharpenFilter extends SelesSharpenFilter implements SelesParameters.FilterParameterInterface
{
    @Override
    protected SelesParameters initParams(SelesParameters initParams) {
        initParams = super.initParams(initParams);
        initParams.appendFloatArg("sharpness", this.getSharpness(), -1.0f, 1.0f);
        return initParams;
    }
    
    @Override
    protected void submitFilterArg(final SelesParameters.FilterArg filterArg) {
        if (filterArg == null) {
            return;
        }
        if (filterArg.equalsKey("sharpness")) {
            this.setSharpness(filterArg.getValue());
        }
    }
}
