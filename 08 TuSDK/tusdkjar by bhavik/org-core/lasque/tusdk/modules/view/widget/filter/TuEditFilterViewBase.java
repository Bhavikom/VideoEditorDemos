package org.lasque.tusdk.modules.view.widget.filter;

import android.content.Context;
import android.util.AttributeSet;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;

public abstract class TuEditFilterViewBase
  extends GroupFilterBaseView
{
  private FilterWrap a;
  
  protected abstract void setConfigViewParams(List<String> paramList);
  
  protected abstract void showConfigView(boolean paramBoolean);
  
  protected abstract boolean onFilterSelected(GroupFilterItem paramGroupFilterItem);
  
  public TuEditFilterViewBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuEditFilterViewBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuEditFilterViewBase(Context paramContext)
  {
    super(paramContext);
  }
  
  protected void handleCancelAction()
  {
    if (this.a == null) {
      return;
    }
    SelesParameters localSelesParameters = this.a.getFilterParameter();
    if (localSelesParameters == null) {
      return;
    }
    Iterator localIterator = localSelesParameters.getArgs().iterator();
    while (localIterator.hasNext())
    {
      SelesParameters.FilterArg localFilterArg = (SelesParameters.FilterArg)localIterator.next();
      localFilterArg.reset();
    }
    requestRender();
  }
  
  protected boolean onDispatchGroupFilterSelected(GroupFilterBarInterface paramGroupFilterBarInterface, GroupFilterItemViewInterface paramGroupFilterItemViewInterface, GroupFilterItem paramGroupFilterItem)
  {
    if (paramGroupFilterItem.type == GroupFilterItem.GroupFilterItemType.TypeFilter)
    {
      if (notifyTitle(paramGroupFilterItemViewInterface, paramGroupFilterItem)) {
        return onFilterSelected(paramGroupFilterItem);
      }
      if ((isEnableFilterConfig()) && (paramGroupFilterItem.filterOption != null) && (paramGroupFilterItem.filterOption.canDefinition)) {
        showConfigView(true);
      }
    }
    return true;
  }
  
  public final void setFilter(FilterWrap paramFilterWrap)
  {
    if ((paramFilterWrap == null) || (paramFilterWrap.equals(this.a))) {
      return;
    }
    this.a = paramFilterWrap;
    SelesParameters localSelesParameters = this.a.getFilterParameter();
    if ((localSelesParameters == null) || (localSelesParameters.size() == 0)) {
      return;
    }
    setConfigViewParams(localSelesParameters.getArgKeys());
  }
  
  protected void requestRender()
  {
    if (this.a != null) {
      this.a.submitFilterParameter();
    }
  }
  
  protected final SelesParameters.FilterArg getFilterArg(int paramInt)
  {
    if ((this.a == null) || (paramInt < 0)) {
      return null;
    }
    SelesParameters localSelesParameters = this.a.getFilterParameter();
    if ((localSelesParameters == null) || (paramInt >= localSelesParameters.size())) {
      return null;
    }
    return (SelesParameters.FilterArg)localSelesParameters.getArgs().get(paramInt);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\filter\TuEditFilterViewBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */