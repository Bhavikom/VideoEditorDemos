// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.filter;

//import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSharpenFilter;
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
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKSharpenFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuFilterResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.impl.activity.TuFilterResultFragment;

public abstract class TuEditSharpnessFragmentBase extends TuFilterResultFragment
{
    @Override
    protected void loadView(final ViewGroup viewGroup) {
        super.loadView(viewGroup);
        StatisticsManger.appendComponent(ComponentActType.editSharpnessFragment);
        this.setFilterWrap(this.a());
    }
    
    private FilterWrap a() {
        final FilterOption filterOption = new FilterOption() {
            @Override
            public SelesOutInput getFilter() {
                return new TuSDKSharpenFilter();
            }
        };
        filterOption.id = Long.MAX_VALUE;
        filterOption.canDefinition = true;
        filterOption.isInternal = true;
        return FilterWrap.creat(filterOption);
    }
}
