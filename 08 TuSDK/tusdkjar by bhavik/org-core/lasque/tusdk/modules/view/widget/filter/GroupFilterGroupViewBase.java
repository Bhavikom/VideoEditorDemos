package org.lasque.tusdk.modules.view.widget.filter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnLongClickListener;
import org.lasque.tusdk.core.seles.tusdk.FilterGroup;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;

public abstract class GroupFilterGroupViewBase
  extends GroupFilterItemViewBase
{
  private GroupFilterGroupViewDelegate a;
  private OnClickOrLongClickListener b;
  
  protected abstract void dispatcherViewClick(View paramView);
  
  public GroupFilterGroupViewBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public GroupFilterGroupViewBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public GroupFilterGroupViewBase(Context paramContext)
  {
    super(paramContext);
  }
  
  public GroupFilterGroupViewDelegate getDelegate()
  {
    return this.a;
  }
  
  public void setDelegate(GroupFilterGroupViewDelegate paramGroupFilterGroupViewDelegate)
  {
    this.a = paramGroupFilterGroupViewDelegate;
    if (paramGroupFilterGroupViewDelegate == null)
    {
      setOnLongClickListener(null);
      setLongClickable(false);
    }
    else
    {
      setOnLongClickListener(cocListener());
    }
  }
  
  protected OnClickOrLongClickListener cocListener()
  {
    if (this.b == null) {
      this.b = new OnClickOrLongClickListener(null);
    }
    return this.b;
  }
  
  protected boolean canHiddenRemoveFlag()
  {
    return (getModel() == null) || (((GroupFilterItem)getModel()).filterGroup == null) || (!((GroupFilterItem)getModel()).filterGroup.isDownload) || (!((GroupFilterItem)getModel()).isInActingType);
  }
  
  protected boolean isInActingType()
  {
    return (getModel() != null) && (((GroupFilterItem)getModel()).isInActingType);
  }
  
  private class OnClickOrLongClickListener
    extends TuSdkViewHelper.OnSafeClickListener
    implements View.OnLongClickListener
  {
    private OnClickOrLongClickListener() {}
    
    public void onSafeClick(View paramView)
    {
      GroupFilterGroupViewBase.this.dispatcherViewClick(paramView);
    }
    
    public boolean onLongClick(View paramView)
    {
      if ((!((GroupFilterItem)GroupFilterGroupViewBase.this.getModel()).isInActingType) && (GroupFilterGroupViewBase.this.getDelegate() != null)) {
        GroupFilterGroupViewBase.this.getDelegate().onGroupFilterGroupViewLongClick(GroupFilterGroupViewBase.this);
      }
      return true;
    }
  }
  
  public static abstract interface GroupFilterGroupViewDelegate
  {
    public abstract void onGroupFilterGroupViewLongClick(GroupFilterGroupViewBase paramGroupFilterGroupViewBase);
    
    public abstract void onGroupFilterGroupViewRemove(GroupFilterGroupViewBase paramGroupFilterGroupViewBase);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\filter\GroupFilterGroupViewBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */