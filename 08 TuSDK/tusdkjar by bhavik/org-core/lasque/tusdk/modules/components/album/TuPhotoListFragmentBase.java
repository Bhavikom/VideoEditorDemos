package org.lasque.tusdk.modules.components.album;

import android.view.ViewGroup;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.impl.activity.TuComponentFragment;
import org.lasque.tusdk.modules.components.ComponentActType;

public abstract class TuPhotoListFragmentBase
  extends TuComponentFragment
{
  private AlbumSqlInfo a;
  
  public AlbumSqlInfo getAlbumInfo()
  {
    return this.a;
  }
  
  public void setAlbumInfo(AlbumSqlInfo paramAlbumSqlInfo)
  {
    this.a = paramAlbumSqlInfo;
  }
  
  public abstract void notifySelectedPhoto(ImageSqlInfo paramImageSqlInfo);
  
  protected void loadView(ViewGroup paramViewGroup) {}
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.photoListFragment);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\album\TuPhotoListFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */