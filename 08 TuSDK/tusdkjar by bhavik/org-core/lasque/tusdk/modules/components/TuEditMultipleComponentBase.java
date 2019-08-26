package org.lasque.tusdk.modules.components;

import android.app.Activity;
import org.lasque.tusdk.core.secret.StatisticsManger;

public abstract class TuEditMultipleComponentBase
  extends TuSdkInputComponent
{
  public TuEditMultipleComponentBase(Activity paramActivity)
  {
    super(paramActivity);
    StatisticsManger.appendComponent(ComponentActType.editMultipleComponent);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\TuEditMultipleComponentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */