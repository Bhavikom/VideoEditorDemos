// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.edit;

//import org.lasque.tusdk.core.secret.SdkValid;
import java.util.ArrayList;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.modules.components.ComponentActType;
import android.view.ViewGroup;
//import org.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity.TuImageResultFragment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentActType;

import java.util.List;
//import org.lasque.tusdk.impl.activity.TuImageResultFragment;

public abstract class TuEditFragmentBase extends TuImageResultFragment
{
    private List<TuEditActionType> a;
    
    @Override
    protected void notifyProcessing(final TuSdkResult tuSdkResult) {
    }
    
    @Override
    protected boolean asyncNotifyProcessing(final TuSdkResult tuSdkResult) {
        return false;
    }
    
    @Override
    protected void loadView(final ViewGroup viewGroup) {
    }
    
    @Override
    protected void viewDidLoad(final ViewGroup viewGroup) {
        StatisticsManger.appendComponent(ComponentActType.multipleEditFragment);
    }
    
    private List<TuEditActionType> a() {
        final ArrayList<TuEditActionType> list = new ArrayList<TuEditActionType>();
        list.add(TuEditActionType.TypeCuter);
        list.add(TuEditActionType.TypeTurn);
        if (SdkValid.shared.wipeFilterEnabled()) {
            list.add(TuEditActionType.TypeWipeFilter);
        }
        list.add(TuEditActionType.TypeAperture);
        return list;
    }
    
    protected List<TuEditActionType> getModules() {
        if (this.a == null || this.a.size() == 0) {
            this.a = this.a();
        }
        final List<TuEditActionType> a = this.a();
        a.retainAll(this.a);
        return a;
    }
    
    public void setModules(final List<TuEditActionType> a) {
        this.a = a;
    }
}
