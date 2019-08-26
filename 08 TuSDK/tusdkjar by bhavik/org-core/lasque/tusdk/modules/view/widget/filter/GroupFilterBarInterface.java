package org.lasque.tusdk.modules.view.widget.filter;

import android.app.Activity;
import android.graphics.Bitmap;
import java.util.List;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;

public abstract interface GroupFilterBarInterface
{
  public abstract void setGroupFilterCellWidth(int paramInt);
  
  public abstract void setGroupTableCellLayoutId(int paramInt);
  
  public abstract void setFilterTableCellLayoutId(int paramInt);
  
  public abstract void setOnlineFragmentClazz(Class<?> paramClass);
  
  public abstract void setHeight(int paramInt);
  
  public abstract void setDelegate(GroupFilterBarDelegate paramGroupFilterBarDelegate);
  
  public abstract void setAction(GroupFilterItemViewInterface.GroupFilterAction paramGroupFilterAction);
  
  public abstract void setActivity(Activity paramActivity);
  
  public abstract void setEnableFilterConfig(boolean paramBoolean);
  
  public abstract void setFilterGroup(List<String> paramList);
  
  public abstract void setThumbImage(Bitmap paramBitmap);
  
  public abstract void setRenderFilterThumb(boolean paramBoolean);
  
  public abstract boolean isRenderFilterThumb();
  
  public abstract void setAutoSelectGroupDefaultFilter(boolean paramBoolean);
  
  public abstract void setSaveLastFilter(boolean paramBoolean);
  
  public abstract void setEnableNormalFilter(boolean paramBoolean);
  
  public abstract void setEnableOnlineFilter(boolean paramBoolean);
  
  public abstract void setEnableHistory(boolean paramBoolean);
  
  public abstract void loadFilters();
  
  public abstract void loadFilters(FilterOption paramFilterOption);
  
  public abstract void exitRemoveState();
  
  public static abstract interface GroupFilterBarDelegate
  {
    public abstract boolean onGroupFilterSelected(GroupFilterBarInterface paramGroupFilterBarInterface, GroupFilterItemViewInterface paramGroupFilterItemViewInterface, GroupFilterItem paramGroupFilterItem);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\filter\GroupFilterBarInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */