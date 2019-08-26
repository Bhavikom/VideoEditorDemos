package org.lasque.tusdk.modules.components.album;

import android.view.ViewGroup;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.task.AlbumTaskManager;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.impl.activity.TuComponentFragment;
import org.lasque.tusdk.modules.components.ComponentActType;

public abstract class TuAlbumListFragmentBase
  extends TuComponentFragment
{
  private boolean a;
  private String b;
  
  public boolean isDisableAutoSkipToPhotoList()
  {
    return this.a;
  }
  
  public void setDisableAutoSkipToPhotoList(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
  
  public String getSkipAlbumName()
  {
    return this.b;
  }
  
  public void setSkipAlbumName(String paramString)
  {
    this.b = paramString;
    if (paramString != null) {
      setDisableAutoSkipToPhotoList(false);
    }
  }
  
  public abstract List<AlbumSqlInfo> getGroups();
  
  public abstract void notifySelectedGroup(AlbumSqlInfo paramAlbumSqlInfo);
  
  protected void loadView(ViewGroup paramViewGroup) {}
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.albumListFragment);
  }
  
  public void onDestroyView()
  {
    AlbumTaskManager.shared.resetQueues();
    super.onDestroyView();
  }
  
  protected void autoSelectedAblumGroupAction(List<AlbumSqlInfo> paramList)
  {
    if ((isDisableAutoSkipToPhotoList()) || (paramList == null)) {
      return;
    }
    Object localObject = null;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      AlbumSqlInfo localAlbumSqlInfo = (AlbumSqlInfo)localIterator.next();
      if (localAlbumSqlInfo.total >= 1)
      {
        if ((getSkipAlbumName() != null) && (localAlbumSqlInfo.title.equalsIgnoreCase(getSkipAlbumName())))
        {
          localObject = localAlbumSqlInfo;
          break;
        }
        if (("Camera".equalsIgnoreCase(localAlbumSqlInfo.title)) && ((localObject == null) || (((AlbumSqlInfo)localObject).total < localAlbumSqlInfo.total))) {
          localObject = localAlbumSqlInfo;
        }
      }
    }
    if (localObject != null) {
      notifySelectedGroup((AlbumSqlInfo)localObject);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\album\TuAlbumListFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */