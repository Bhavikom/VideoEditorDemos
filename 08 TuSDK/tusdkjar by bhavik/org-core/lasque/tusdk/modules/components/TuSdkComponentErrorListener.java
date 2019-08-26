package org.lasque.tusdk.modules.components;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.impl.activity.TuFragment;

public abstract interface TuSdkComponentErrorListener
{
  public abstract void onComponentError(TuFragment paramTuFragment, TuSdkResult paramTuSdkResult, Error paramError);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\TuSdkComponentErrorListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */