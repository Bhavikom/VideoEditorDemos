package org.lasque.tusdk.modules.components.camera;

import android.view.ViewGroup;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.impl.activity.TuImageResultFragment;
import org.lasque.tusdk.modules.components.ComponentActType;

public abstract class TuCameraPreviewFragmentBase
  extends TuImageResultFragment
{
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.cameraPreviewFragment);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\camera\TuCameraPreviewFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */