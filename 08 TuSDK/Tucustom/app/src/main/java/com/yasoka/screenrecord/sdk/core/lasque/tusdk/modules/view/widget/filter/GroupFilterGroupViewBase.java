// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.filter;

//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;

public abstract class
GroupFilterGroupViewBase extends GroupFilterItemViewBase
{
    private GroupFilterGroupViewDelegate a;
    private OnClickOrLongClickListener b;
    
    protected abstract void dispatcherViewClick(final View p0);
    
    public GroupFilterGroupViewBase(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public GroupFilterGroupViewBase(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public GroupFilterGroupViewBase(final Context context) {
        super(context);
    }
    
    public GroupFilterGroupViewDelegate getDelegate() {
        return this.a;
    }
    
    public void setDelegate(final GroupFilterGroupViewDelegate a) {
        this.a = a;
        if (a == null) {
            this.setOnLongClickListener((View.OnLongClickListener)null);
            this.setLongClickable(false);
        }
        else {
            this.setOnLongClickListener((View.OnLongClickListener)this.cocListener());
        }
    }
    
    protected OnClickOrLongClickListener cocListener() {
        if (this.b == null) {
            this.b = new OnClickOrLongClickListener();
        }
        return this.b;
    }
    
    protected boolean canHiddenRemoveFlag() {
        return this.getModel() == null || this.getModel().filterGroup == null || !this.getModel().filterGroup.isDownload || !this.getModel().isInActingType;
    }
    
    protected boolean isInActingType() {
        return this.getModel() != null && this.getModel().isInActingType;
    }
    
    private class OnClickOrLongClickListener extends TuSdkViewHelper.OnSafeClickListener implements View.OnLongClickListener
    {
        @Override
        public void onSafeClick(final View view) {
            GroupFilterGroupViewBase.this.dispatcherViewClick(view);
        }
        
        public boolean onLongClick(final View view) {
            if (!GroupFilterGroupViewBase.this.getModel().isInActingType && GroupFilterGroupViewBase.this.getDelegate() != null) {
                GroupFilterGroupViewBase.this.getDelegate().onGroupFilterGroupViewLongClick(GroupFilterGroupViewBase.this);
            }
            return true;
        }
    }
    
    public interface GroupFilterGroupViewDelegate
    {
        void onGroupFilterGroupViewLongClick(final GroupFilterGroupViewBase p0);
        
        void onGroupFilterGroupViewRemove(final GroupFilterGroupViewBase p0);
    }
}
