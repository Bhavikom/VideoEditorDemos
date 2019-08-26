// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.filter;

import java.util.ArrayList;
//import org.lasque.tusdk.core.seles.tusdk.filters.lights.TuSDKLightHolyFilter;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.lights.TuSDKLightHolyFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuFilterResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.impl.activity.TuFilterResultFragment;

public abstract class TuEditHolyLightFragmentBase extends TuFilterResultFragment
{
    @Override
    protected void loadView(final ViewGroup viewGroup) {
        super.loadView(viewGroup);
        StatisticsManger.appendComponent(ComponentActType.editHolyLightFragment);
        this.setFilterWrap(this.a());
    }
    
    private FilterWrap a() {
        final FilterOption filterOption = new FilterOption() {
            @Override
            public SelesOutInput getFilter() {
                return new TuSDKLightHolyFilter();
            }
        };
        filterOption.id = Long.MAX_VALUE;
        filterOption.canDefinition = true;
        filterOption.isInternal = true;
        final ArrayList<String> internalTextures = new ArrayList<String>();
        internalTextures.add("f8a6ed3ec939d6941c94a272aff1791b");
        filterOption.internalTextures = internalTextures;
        return FilterWrap.creat(filterOption);
    }
}
