// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.filter;

import android.util.AttributeSet;
import android.content.Context;

public abstract class
TuCameraFilterViewBase extends GroupFilterBaseView
{
    protected abstract long getCaptureActivateWaitMillis();
    
    protected abstract boolean onGroupFilterSelected(final GroupFilterItem p0, final boolean p1);
    
    public TuCameraFilterViewBase(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuCameraFilterViewBase(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuCameraFilterViewBase(final Context context) {
        super(context);
    }
    
    @Override
    protected boolean onDispatchGroupFilterSelected(final GroupFilterBarInterface groupFilterBarInterface, final GroupFilterItemViewInterface groupFilterItemViewInterface, final GroupFilterItem groupFilterItem) {
        boolean b = false;
        if (groupFilterItem.type == GroupFilterItem.GroupFilterItemType.TypeFilter && !this.notifyTitle(groupFilterItemViewInterface, groupFilterItem)) {
            if (this.notifyTitle(groupFilterItemViewInterface, groupFilterItem) || !groupFilterItemViewInterface.isActivating()) {
                groupFilterItemViewInterface.waitInActivate(this.getCaptureActivateWaitMillis());
                return true;
            }
            groupFilterItemViewInterface.stopActivating();
            b = true;
        }
        return this.onGroupFilterSelected(groupFilterItem, b);
    }
}
