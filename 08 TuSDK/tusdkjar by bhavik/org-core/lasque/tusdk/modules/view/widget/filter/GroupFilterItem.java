package org.lasque.tusdk.modules.view.widget.filter;

import android.graphics.Color;
import org.lasque.tusdk.core.seles.tusdk.FilterGroup;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;

public class GroupFilterItem
{
  public static int Backgroud_History = Color.parseColor("#ff6633");
  public static int Backgroud_Online = Color.parseColor("#FF5534");
  public static int Backgroud_Orgin = Color.parseColor("#A1835D");
  public GroupFilterItemType type;
  public FilterGroup filterGroup;
  public FilterOption filterOption;
  public boolean isInActingType;
  
  public GroupFilterItem(GroupFilterItemType paramGroupFilterItemType)
  {
    this.type = paramGroupFilterItemType;
  }
  
  public String getFilterCode()
  {
    if (this.type != GroupFilterItemType.TypeFilter) {
      return null;
    }
    String str = "Normal";
    if (this.filterOption != null) {
      str = this.filterOption.code;
    }
    return str;
  }
  
  public static GroupFilterItem create(GroupFilterItemType paramGroupFilterItemType)
  {
    GroupFilterItem localGroupFilterItem = new GroupFilterItem(paramGroupFilterItemType);
    return localGroupFilterItem;
  }
  
  public static GroupFilterItem createWithGroup(FilterGroup paramFilterGroup)
  {
    GroupFilterItem localGroupFilterItem = create(GroupFilterItemType.TypeGroup);
    localGroupFilterItem.filterGroup = paramFilterGroup;
    return localGroupFilterItem;
  }
  
  public static GroupFilterItem createWithFilter(FilterOption paramFilterOption)
  {
    GroupFilterItem localGroupFilterItem = create(GroupFilterItemType.TypeFilter);
    localGroupFilterItem.filterOption = paramFilterOption;
    return localGroupFilterItem;
  }
  
  public static enum GroupFilterItemType
  {
    private GroupFilterItemType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\filter\GroupFilterItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */