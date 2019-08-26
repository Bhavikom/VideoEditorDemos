package org.lasque.tusdk.modules.view.widget.filter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import org.lasque.tusdk.core.task.FilterTaskInterface;
import org.lasque.tusdk.core.view.recyclerview.TuSdkTableView;

public class GroupFilterTableView
  extends TuSdkTableView<GroupFilterItem, GroupFilterItemViewBase>
  implements GroupFilterTableViewInterface
{
  private GroupFilterItemViewInterface.GroupFilterAction a;
  private boolean b;
  private int c;
  private GroupFilterGroupViewBase.GroupFilterGroupViewDelegate d;
  private FilterTaskInterface e;
  
  public GroupFilterTableView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public GroupFilterTableView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public GroupFilterTableView(Context paramContext)
  {
    super(paramContext);
  }
  
  public int getGroupFilterCellWidth()
  {
    return this.c;
  }
  
  public void setGroupFilterCellWidth(int paramInt)
  {
    this.c = paramInt;
  }
  
  public GroupFilterItemViewInterface.GroupFilterAction getAction()
  {
    return this.a;
  }
  
  public void setAction(GroupFilterItemViewInterface.GroupFilterAction paramGroupFilterAction)
  {
    this.a = paramGroupFilterAction;
  }
  
  public boolean isDisplaySelectionIcon()
  {
    return this.b;
  }
  
  public void setDisplaySelectionIcon(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  public GroupFilterGroupViewBase.GroupFilterGroupViewDelegate getGroupDelegate()
  {
    return this.d;
  }
  
  public void setGroupDelegate(GroupFilterGroupViewBase.GroupFilterGroupViewDelegate paramGroupFilterGroupViewDelegate)
  {
    this.d = paramGroupFilterGroupViewDelegate;
  }
  
  public void setFilterTask(FilterTaskInterface paramFilterTaskInterface)
  {
    this.e = paramFilterTaskInterface;
  }
  
  public void loadView()
  {
    super.loadView();
    setHasFixedSize(true);
  }
  
  protected void onViewCreated(GroupFilterItemViewBase paramGroupFilterItemViewBase, ViewGroup paramViewGroup, int paramInt)
  {
    paramGroupFilterItemViewBase.setAction(getAction());
    paramGroupFilterItemViewBase.setDisplaySelectionIcon(isDisplaySelectionIcon());
    paramGroupFilterItemViewBase.setFilterTask(this.e);
    if (getGroupFilterCellWidth() > 0) {
      paramGroupFilterItemViewBase.setWidth(getGroupFilterCellWidth());
    }
    if ((paramGroupFilterItemViewBase instanceof GroupFilterGroupViewBase)) {
      ((GroupFilterGroupViewBase)paramGroupFilterItemViewBase).setDelegate(getGroupDelegate());
    }
  }
  
  protected void onViewBinded(GroupFilterItemViewBase paramGroupFilterItemViewBase, int paramInt) {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\filter\GroupFilterTableView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */