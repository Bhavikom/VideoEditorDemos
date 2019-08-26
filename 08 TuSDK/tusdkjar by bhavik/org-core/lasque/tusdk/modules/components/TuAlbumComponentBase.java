package org.lasque.tusdk.modules.components;

import android.app.Activity;
import org.lasque.tusdk.core.secret.StatisticsManger;

public abstract class TuAlbumComponentBase
  extends TuSdkComponent
{
  public TuAlbumComponentBase(Activity paramActivity)
  {
    super(paramActivity);
    if (getClass().getSimpleName().contains("vatar")) {
      StatisticsManger.appendComponent(ComponentActType.avatarComponent);
    } else {
      StatisticsManger.appendComponent(ComponentActType.albumComponent);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\TuAlbumComponentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */