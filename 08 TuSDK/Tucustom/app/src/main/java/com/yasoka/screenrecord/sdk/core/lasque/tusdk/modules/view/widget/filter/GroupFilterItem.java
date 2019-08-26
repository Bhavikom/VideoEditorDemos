// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.filter;

import android.graphics.Color;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterGroup;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.tusdk.FilterOption;
//import org.lasque.tusdk.core.seles.tusdk.FilterGroup;

public class
GroupFilterItem
{
    public static int Backgroud_History;
    public static int Backgroud_Online;
    public static int Backgroud_Orgin;
    public GroupFilterItemType type;
    public FilterGroup filterGroup;
    public FilterOption filterOption;
    public boolean isInActingType;
    
    public GroupFilterItem(final GroupFilterItemType type) {
        this.type = type;
    }
    
    public String getFilterCode() {
        if (this.type != GroupFilterItemType.TypeFilter) {
            return null;
        }
        String code = "Normal";
        if (this.filterOption != null) {
            code = this.filterOption.code;
        }
        return code;
    }
    
    public static GroupFilterItem create(final GroupFilterItemType groupFilterItemType) {
        return new GroupFilterItem(groupFilterItemType);
    }
    
    public static GroupFilterItem createWithGroup(final FilterGroup filterGroup) {
        final GroupFilterItem create = create(GroupFilterItemType.TypeGroup);
        create.filterGroup = filterGroup;
        return create;
    }
    
    public static GroupFilterItem createWithFilter(final FilterOption filterOption) {
        final GroupFilterItem create = create(GroupFilterItemType.TypeFilter);
        create.filterOption = filterOption;
        return create;
    }
    
    static {
        GroupFilterItem.Backgroud_History = Color.parseColor("#ff6633");
        GroupFilterItem.Backgroud_Online = Color.parseColor("#FF5534");
        GroupFilterItem.Backgroud_Orgin = Color.parseColor("#A1835D");
    }
    
    public enum GroupFilterItemType
    {
        TypeHolder, 
        TypeFilter, 
        TypeGroup, 
        TypeHistory, 
        TypeOnline;
    }
}
