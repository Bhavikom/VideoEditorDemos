package org.lasque.tusdk.modules.components.album;

import android.view.ViewGroup;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.modules.components.ComponentActType;

public class TuAlbumMultiplePreviewFragmentBase
  extends TuFragment
{
  protected void loadView(ViewGroup paramViewGroup) {}
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.photoListPreviewFragment);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\album\TuAlbumMultiplePreviewFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */