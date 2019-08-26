// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.filter;

//import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorAdjustmentFilter;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import java.util.ArrayList;
import java.util.Iterator;
//import org.lasque.tusdk.core.seles.SelesParameters;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesParameters;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorAdjustmentFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuFilterResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;
//import org.lasque.tusdk.impl.activity.TuFilterResultFragment;

public abstract class TuMutibleEditAdjustFragmentBase extends TuFilterResultFragment {
    private int a = -1;

    public TuMutibleEditAdjustFragmentBase() {
    }

    protected abstract void setConfigViewShowState(boolean var1);

    protected abstract View buildActionButton(String var1, int var2);

    protected void loadView(ViewGroup var1) {
        StatisticsManger.appendComponent(ComponentActType.editAdjustFragment);
        this.setFilterWrap(this.a());
        super.loadView(var1);
        this.buildActionButtons();
    }

    protected void buildActionButtons() {
        SelesParameters var1 = this.getFilterParameter();
        if (var1 != null && var1.size() != 0) {
            int var2 = 0;

            for(Iterator var3 = var1.getArgKeys().iterator(); var3.hasNext(); ++var2) {
                String var4 = (String)var3.next();
                this.buildActionButton(var4, var2);
            }

        }
    }

    protected void handleConfigCompeleteButton() {
        this.setConfigViewShowState(false);
    }

    protected void handleAction(Integer var1) {
        this.a = var1;
        if (this.getConfigView() != null) {
            SelesParameters var2 = this.getFilterParameter();
            if (var2.size() > this.a) {
                String var3 = (String)var2.getArgKeys().get(this.a);
                if (var3 != null) {
                    ArrayList var4 = new ArrayList();
                    var4.add(var3);
                    ((ParameterConfigViewInterface)this.getConfigView()).setParams(var4, 0);
                    this.setConfigViewShowState(true);
                }
            }
        }
    }

    public int getCurrentAction() {
        return this.a;
    }

    public void onParameterConfigDataChanged(ParameterConfigViewInterface var1, int var2, float var3) {
        super.onParameterConfigDataChanged(var1, this.a, var3);
    }

    public void onParameterConfigRest(ParameterConfigViewInterface var1, int var2) {
        super.onParameterConfigRest(var1, this.a);
    }

    public float readParameterValue(ParameterConfigViewInterface var1, int var2) {
        return super.readParameterValue(var1, this.a);
    }

    private FilterWrap a() {
        FilterOption var1 = new FilterOption() {
            public SelesOutInput getFilter() {
                return new TuSDKColorAdjustmentFilter();
            }
        };
        var1.id = 9223372036854775807L;
        var1.canDefinition = true;
        var1.isInternal = true;
        FilterWrap var2 = FilterWrap.creat(var1);
        return var2;
    }
}
