package org.lasque.tusdk.modules.components.edit;

import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.impl.activity.TuImageResultFragment;
import org.lasque.tusdk.modules.components.ComponentActType;

public abstract class TuEditFragmentBase
  extends TuImageResultFragment
{
  private List<TuEditActionType> a;
  
  protected void notifyProcessing(TuSdkResult paramTuSdkResult) {}
  
  protected boolean asyncNotifyProcessing(TuSdkResult paramTuSdkResult)
  {
    return false;
  }
  
  protected void loadView(ViewGroup paramViewGroup) {}
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.multipleEditFragment);
  }
  
  private List<TuEditActionType> a()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(TuEditActionType.TypeCuter);
    localArrayList.add(TuEditActionType.TypeTurn);
    if (SdkValid.shared.wipeFilterEnabled()) {
      localArrayList.add(TuEditActionType.TypeWipeFilter);
    }
    localArrayList.add(TuEditActionType.TypeAperture);
    return localArrayList;
  }
  
  protected List<TuEditActionType> getModules()
  {
    if ((this.a == null) || (this.a.size() == 0)) {
      this.a = a();
    }
    List localList = a();
    localList.retainAll(this.a);
    return localList;
  }
  
  public void setModules(List<TuEditActionType> paramList)
  {
    this.a = paramList;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\edit\TuEditFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */