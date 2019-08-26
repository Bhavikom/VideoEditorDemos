package org.lasque.tusdk.modules.components;

import android.app.Activity;
import org.lasque.tusdk.core.secret.StatisticsManger;

public abstract class TuAlbumMultipleComponentBase
  extends TuSdkComponent
{
  public TuAlbumMultipleComponentBase(Activity paramActivity)
  {
    super(paramActivity);
    StatisticsManger.appendComponent(ComponentActType.albumMultipleComponent);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\TuAlbumMultipleComponentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */