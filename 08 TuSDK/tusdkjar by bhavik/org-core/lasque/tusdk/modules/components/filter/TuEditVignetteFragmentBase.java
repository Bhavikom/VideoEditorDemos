package org.lasque.tusdk.modules.components.filter;

import android.view.ViewGroup;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.filters.lights.TuSDKLightVignetteFilter;
import org.lasque.tusdk.impl.activity.TuFilterResultFragment;
import org.lasque.tusdk.modules.components.ComponentActType;

public abstract class TuEditVignetteFragmentBase
  extends TuFilterResultFragment
{
  protected void loadView(ViewGroup paramViewGroup)
  {
    super.loadView(paramViewGroup);
    StatisticsManger.appendComponent(ComponentActType.editVignetteFragment);
    setFilterWrap(a());
  }
  
  private FilterWrap a()
  {
    FilterOption local1 = new FilterOption()
    {
      public SelesOutInput getFilter()
      {
        return new TuSDKLightVignetteFilter();
      }
    };
    local1.id = Long.MAX_VALUE;
    local1.canDefinition = true;
    local1.isInternal = true;
    FilterWrap localFilterWrap = FilterWrap.creat(local1);
    return localFilterWrap;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\filter\TuEditVignetteFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */