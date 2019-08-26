// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.filter;

//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import android.graphics.Bitmap;
import java.util.List;
import android.app.Activity;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;

public interface
GroupFilterBarInterface
{
    void setGroupFilterCellWidth(final int p0);
    
    void setGroupTableCellLayoutId(final int p0);
    
    void setFilterTableCellLayoutId(final int p0);
    
    void setOnlineFragmentClazz(final Class<?> p0);
    
    void setHeight(final int p0);
    
    void setDelegate(final GroupFilterBarDelegate p0);
    
    void setAction(final GroupFilterItemViewInterface.GroupFilterAction p0);
    
    void setActivity(final Activity p0);
    
    void setEnableFilterConfig(final boolean p0);
    
    void setFilterGroup(final List<String> p0);
    
    void setThumbImage(final Bitmap p0);
    
    void setRenderFilterThumb(final boolean p0);
    
    boolean isRenderFilterThumb();
    
    void setAutoSelectGroupDefaultFilter(final boolean p0);
    
    void setSaveLastFilter(final boolean p0);
    
    void setEnableNormalFilter(final boolean p0);
    
    void setEnableOnlineFilter(final boolean p0);
    
    void setEnableHistory(final boolean p0);
    
    void loadFilters();
    
    void loadFilters(final FilterOption p0);
    
    void exitRemoveState();
    
    public interface GroupFilterBarDelegate
    {
        boolean onGroupFilterSelected(final GroupFilterBarInterface p0, final GroupFilterItemViewInterface p1, final GroupFilterItem p2);
    }
}
