package org.lasque.tusdk.modules.components.album;

import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.task.AlbumTaskManager;
import org.lasque.tusdk.core.utils.ReflectUtils;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.impl.activity.TuComponentFragment;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.modules.components.ComponentActType;

public abstract class TuAlbumMultipleListFragmentBase
  extends TuComponentFragment
{
  private String a;
  
  public String getSkipAlbumName()
  {
    return this.a;
  }
  
  public void setSkipAlbumName(String paramString)
  {
    this.a = paramString;
  }
  
  public abstract List<AlbumSqlInfo> getGroups();
  
  public abstract void notifySelectedGroup(AlbumSqlInfo paramAlbumSqlInfo);
  
  public abstract Class<?> getPreviewFragmentClazz();
  
  public abstract int getPreviewFragmentLayoutId();
  
  protected void loadView(ViewGroup paramViewGroup) {}
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    StatisticsManger.appendComponent(ComponentActType.albumMultipleListFragment);
  }
  
  public void onDestroyView()
  {
    AlbumTaskManager.shared.resetQueues();
    super.onDestroyView();
  }
  
  public void autoSelectedAblumGroupAction(ArrayList<AlbumSqlInfo> paramArrayList)
  {
    if (paramArrayList == null) {
      return;
    }
    Object localObject = null;
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      AlbumSqlInfo localAlbumSqlInfo = (AlbumSqlInfo)localIterator.next();
      if (localAlbumSqlInfo.total >= 1) {
        if ((this.a != null) && (localAlbumSqlInfo.title.equalsIgnoreCase(this.a)))
        {
          localObject = localAlbumSqlInfo;
          break;
        }
      }
    }
    if ((localObject == null) && (paramArrayList.size() > 0)) {
      localObject = (AlbumSqlInfo)paramArrayList.get(0);
    }
    notifySelectedGroup((AlbumSqlInfo)localObject);
  }
  
  protected <T extends TuFragment> T getPreviewFragmentInstance()
  {
    TuFragment localTuFragment = (TuFragment)ReflectUtils.classInstance(getPreviewFragmentClazz());
    if (localTuFragment == null) {
      return null;
    }
    localTuFragment.setRootViewLayoutId(getPreviewFragmentLayoutId());
    return localTuFragment;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\album\TuAlbumMultipleListFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */