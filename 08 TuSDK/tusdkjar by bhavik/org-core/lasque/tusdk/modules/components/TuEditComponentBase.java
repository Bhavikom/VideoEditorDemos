package org.lasque.tusdk.modules.components;

import android.app.Activity;
import org.lasque.tusdk.core.secret.StatisticsManger;

public abstract class TuEditComponentBase
  extends TuSdkInputComponent
{
  public TuEditComponentBase(Activity paramActivity)
  {
    super(paramActivity);
    StatisticsManger.appendComponent(ComponentActType.editComponent);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\TuEditComponentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */