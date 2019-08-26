// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.filter;

//import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewInterface;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewInterface;

public interface TuFilterOnlineFragmentInterface
{
    void setDelegate(final TuFilterOnlineFragmentDelegate p0);
    
    void setAction(final GroupFilterItemViewInterface.GroupFilterAction p0);
    
    void setDetailDataId(final long p0);
    
    public interface TuFilterOnlineFragmentDelegate
    {
        void onTuFilterOnlineFragmentSelected(final TuFilterOnlineFragmentInterface p0, final long p1);
    }
}
