package org.lasque.tusdk.modules.components.filter;

import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorAdjustmentFilter;
import org.lasque.tusdk.impl.activity.TuFilterResultFragment;
import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import org.lasque.tusdk.modules.components.ComponentActType;

public abstract class TuEditAdjustFragmentBase
  extends TuFilterResultFragment
{
  private int a = -1;
  
  protected abstract void setConfigViewShowState(boolean paramBoolean);
  
  protected abstract View buildActionButton(String paramString, int paramInt);
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.editAdjustFragment);
    setFilterWrap(a());
    super.loadView(paramViewGroup);
    buildActionButtons();
  }
  
  protected void buildActionButtons()
  {
    SelesParameters localSelesParameters = getFilterParameter();
    if ((localSelesParameters == null) || (localSelesParameters.size() == 0)) {
      return;
    }
    int i = 0;
    Iterator localIterator = localSelesParameters.getArgKeys().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      buildActionButton(str, i);
      i++;
    }
  }
  
  protected void handleConfigCompeleteButton()
  {
    setConfigViewShowState(false);
  }
  
  protected void handleAction(Integer paramInteger)
  {
    this.a = paramInteger.intValue();
    if (getConfigView() == null) {
      return;
    }
    SelesParameters localSelesParameters = getFilterParameter();
    if (localSelesParameters.size() <= this.a) {
      return;
    }
    String str = (String)localSelesParameters.getArgKeys().get(this.a);
    if (str == null) {
      return;
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(str);
    ((ParameterConfigViewInterface)getConfigView()).setParams(localArrayList, 0);
    setConfigViewShowState(true);
  }
  
  public int getCurrentAction()
  {
    return this.a;
  }
  
  public void onParameterConfigDataChanged(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt, float paramFloat)
  {
    super.onParameterConfigDataChanged(paramParameterConfigViewInterface, this.a, paramFloat);
  }
  
  public void onParameterConfigRest(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt)
  {
    super.onParameterConfigRest(paramParameterConfigViewInterface, this.a);
  }
  
  public float readParameterValue(ParameterConfigViewInterface paramParameterConfigViewInterface, int paramInt)
  {
    return super.readParameterValue(paramParameterConfigViewInterface, this.a);
  }
  
  private FilterWrap a()
  {
    FilterOption local1 = new FilterOption()
    {
      public SelesOutInput getFilter()
      {
        return new TuSDKColorAdjustmentFilter();
      }
    };
    local1.id = Long.MAX_VALUE;
    local1.canDefinition = true;
    local1.isInternal = true;
    FilterWrap localFilterWrap = FilterWrap.creat(local1);
    return localFilterWrap;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\filter\TuEditAdjustFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */