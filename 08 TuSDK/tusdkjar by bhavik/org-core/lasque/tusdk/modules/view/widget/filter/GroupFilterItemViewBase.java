package org.lasque.tusdk.modules.view.widget.filter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.task.FilterTaskInterface;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;
import org.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;

public abstract class GroupFilterItemViewBase
  extends TuSdkCellRelativeLayout<GroupFilterItem>
  implements TuSdkListSelectableCellViewInterface, GroupFilterItemViewInterface
{
  private boolean a;
  private GroupFilterItemViewInterface.GroupFilterAction b = GroupFilterItemViewInterface.GroupFilterAction.ActionNormal;
  private boolean c;
  private FilterTaskInterface d;
  private Runnable e = new Runnable()
  {
    public void run()
    {
      GroupFilterItemViewBase.a(GroupFilterItemViewBase.this);
    }
  };
  
  public abstract ImageView getImageView();
  
  protected abstract void setSelectedIcon(GroupFilterItem paramGroupFilterItem, boolean paramBoolean);
  
  protected abstract void handleTypeHistory(GroupFilterItem paramGroupFilterItem);
  
  protected abstract void handleTypeOnlie(GroupFilterItem paramGroupFilterItem);
  
  public GroupFilterItemViewBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public GroupFilterItemViewBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public GroupFilterItemViewBase(Context paramContext)
  {
    super(paramContext);
  }
  
  public boolean isActivating()
  {
    return this.a;
  }
  
  public GroupFilterItemViewInterface.GroupFilterAction getAction()
  {
    if (this.b == null) {
      this.b = GroupFilterItemViewInterface.GroupFilterAction.ActionNormal;
    }
    return this.b;
  }
  
  public void setAction(GroupFilterItemViewInterface.GroupFilterAction paramGroupFilterAction)
  {
    this.b = paramGroupFilterAction;
  }
  
  public boolean isDisplaySelectionIcon()
  {
    return this.c;
  }
  
  public void setDisplaySelectionIcon(boolean paramBoolean)
  {
    this.c = paramBoolean;
  }
  
  public boolean isCameraAction()
  {
    return getAction() == GroupFilterItemViewInterface.GroupFilterAction.ActionCamera;
  }
  
  public void setFilterTask(FilterTaskInterface paramFilterTaskInterface)
  {
    this.d = paramFilterTaskInterface;
  }
  
  public FilterTaskInterface getFilterTask()
  {
    return this.d;
  }
  
  public boolean isRenderFilterThumb()
  {
    if (this.d == null) {
      return false;
    }
    return this.d.isRenderFilterThumb();
  }
  
  private void a(long paramLong)
  {
    a();
    this.a = true;
    ThreadHelper.postDelayed(this.e, paramLong);
  }
  
  private void a()
  {
    this.a = false;
    ThreadHelper.cancel(this.e);
  }
  
  private void b()
  {
    this.a = false;
  }
  
  protected void bindModel()
  {
    GroupFilterItem localGroupFilterItem = (GroupFilterItem)getModel();
    if ((localGroupFilterItem == null) || (localGroupFilterItem.type == null)) {
      return;
    }
    switch (2.a[localGroupFilterItem.type.ordinal()])
    {
    case 1: 
      setSelectedIcon(localGroupFilterItem, false);
      handleTypeGroup(localGroupFilterItem);
      break;
    case 2: 
      if (localGroupFilterItem.filterOption == null)
      {
        setSelectedIcon(localGroupFilterItem, isCameraAction());
        handleTypeOrgin(localGroupFilterItem);
      }
      else
      {
        setSelectedIcon(localGroupFilterItem, (isCameraAction()) || (localGroupFilterItem.filterOption.canDefinition));
        handleTypeFilter(localGroupFilterItem);
      }
      break;
    case 3: 
      setSelectedIcon(localGroupFilterItem, false);
      handleTypeHistory(localGroupFilterItem);
      break;
    case 4: 
      setSelectedIcon(localGroupFilterItem, false);
      handleTypeOnlie(localGroupFilterItem);
      break;
    }
  }
  
  protected void handleTypeOrgin(GroupFilterItem paramGroupFilterItem)
  {
    if ((getImageView() != null) && (isRenderFilterThumb())) {
      this.d.loadImage(getImageView(), "Normal");
    }
  }
  
  protected void handleTypeGroup(GroupFilterItem paramGroupFilterItem)
  {
    if ((paramGroupFilterItem == null) || (paramGroupFilterItem.filterGroup == null) || (getImageView() == null)) {
      return;
    }
    getImageView().setScaleType(ImageView.ScaleType.CENTER_CROP);
    if (this.d != null) {
      this.d.loadImage(getImageView(), FilterLocalPackage.shared().getGroupDefaultFilterCode(paramGroupFilterItem.filterGroup));
    } else {
      FilterLocalPackage.shared().loadGroupDefaultFilterThumb(getImageView(), paramGroupFilterItem.filterGroup);
    }
  }
  
  protected void handleTypeFilter(GroupFilterItem paramGroupFilterItem)
  {
    if ((paramGroupFilterItem == null) || (paramGroupFilterItem.filterOption == null) || (getImageView() == null)) {
      return;
    }
    getImageView().setScaleType(ImageView.ScaleType.CENTER_CROP);
    if (this.d != null) {
      this.d.loadImage(getImageView(), paramGroupFilterItem.filterOption.code);
    } else {
      FilterLocalPackage.shared().loadFilterThumb(getImageView(), paramGroupFilterItem.filterOption);
    }
  }
  
  protected void handleBlockView(int paramInt1, int paramInt2)
  {
    if ((getImageView() != null) && (paramInt2 != 0))
    {
      getImageView().setImageResource(paramInt2);
      getImageView().setScaleType(ImageView.ScaleType.CENTER);
    }
  }
  
  public void viewNeedRest()
  {
    super.viewNeedRest();
    a();
    setSelected(false);
    if (getImageView() != null)
    {
      getImageView().setImageBitmap(null);
      if (this.d != null) {
        this.d.cancelLoadImage(getImageView());
      } else {
        FilterLocalPackage.shared().cancelLoadImage(getImageView());
      }
    }
  }
  
  public void onCellSelected(int paramInt)
  {
    setSelected(true);
  }
  
  public void onCellDeselected()
  {
    setSelected(false);
    stopActivating();
  }
  
  public void waitInActivate(long paramLong)
  {
    if (this.a) {
      return;
    }
    a(paramLong);
  }
  
  public void stopActivating()
  {
    if (!this.a) {
      return;
    }
    a();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\filter\GroupFilterItemViewBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */