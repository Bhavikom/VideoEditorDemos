package org.lasque.tusdk.core.network;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.task.ImageViewTask;
import org.lasque.tusdk.core.task.ImageViewTaskWare;
import org.lasque.tusdk.core.type.DownloadTaskStatus;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.TuSdkSharedPreferences;

public abstract class TuSdkDownloadAdapter<T extends ImageViewTaskWare>
  extends ImageViewTask<T>
{
  private List<TuSdkDownloadItem> a;
  private TuSdkDownloadManger.TuSdkDownloadMangerDelegate b;
  private TuSdkDownloadTask.DownloadTaskType c;
  private TuSdkDownloadManger.TuSdkDownloadMangerDelegate d = new TuSdkDownloadManger.TuSdkDownloadMangerDelegate()
  {
    public void onDownloadMangerStatusChanged(TuSdkDownloadManger paramAnonymousTuSdkDownloadManger, TuSdkDownloadItem paramAnonymousTuSdkDownloadItem, DownloadTaskStatus paramAnonymousDownloadTaskStatus)
    {
      if (paramAnonymousTuSdkDownloadItem.type != TuSdkDownloadAdapter.this.getDownloadTaskType()) {
        return;
      }
      TuSdkDownloadAdapter.a(TuSdkDownloadAdapter.this, paramAnonymousTuSdkDownloadItem, paramAnonymousDownloadTaskStatus);
    }
  };
  
  protected List<TuSdkDownloadItem> getDownloadItems()
  {
    return this.a;
  }
  
  protected void setDownloadItems(List<TuSdkDownloadItem> paramList)
  {
    this.a = paramList;
  }
  
  protected TuSdkDownloadTask.DownloadTaskType getDownloadTaskType()
  {
    return this.c;
  }
  
  protected void setDownloadTaskType(TuSdkDownloadTask.DownloadTaskType paramDownloadTaskType)
  {
    this.c = paramDownloadTaskType;
  }
  
  public TuSdkDownloadManger.TuSdkDownloadMangerDelegate getDownloadDelegate()
  {
    return this.b;
  }
  
  public void setDownloadDelegate(TuSdkDownloadManger.TuSdkDownloadMangerDelegate paramTuSdkDownloadMangerDelegate)
  {
    this.b = paramTuSdkDownloadMangerDelegate;
  }
  
  public abstract boolean containsGroupId(long paramLong);
  
  protected void asyncLoadDownloadDatas()
  {
    ArrayList localArrayList = new ArrayList(this.a);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkDownloadItem localTuSdkDownloadItem = (TuSdkDownloadItem)localIterator.next();
      appendDownload(localTuSdkDownloadItem);
    }
  }
  
  private String a()
  {
    String str = String.format("%s-downloads", new Object[] { getClass() });
    return str;
  }
  
  protected void tryLoadTaskDataWithCache()
  {
    TuSdkDownloadManger.ins.appenDelegate(this.d);
    this.a = new ArrayList();
    ArrayList localArrayList = (ArrayList)TuSdkContext.sharedPreferences().loadSharedCacheObject(a());
    if ((localArrayList == null) || (localArrayList.isEmpty())) {
      return;
    }
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkDownloadItem localTuSdkDownloadItem = (TuSdkDownloadItem)localIterator.next();
      if (localTuSdkDownloadItem.localDownloadPath().exists()) {
        this.a.add(localTuSdkDownloadItem);
      }
    }
    TLog.d("download %s: %s", new Object[] { getDownloadTaskType().getAct(), Integer.valueOf(localArrayList.size()) });
    trySaveTaskDataInCache();
  }
  
  protected void trySaveTaskDataInCache()
  {
    ArrayList localArrayList = new ArrayList(this.a);
    TuSdkContext.sharedPreferences().saveSharedCacheObject(a(), localArrayList);
  }
  
  public void download(long paramLong, String paramString1, String paramString2)
  {
    if (containsGroupId(paramLong)) {
      return;
    }
    TuSdkDownloadManger.ins.appenTask(getDownloadTaskType(), paramLong, paramString1, paramString2);
  }
  
  public void cancelDownload(long paramLong)
  {
    if (!containsGroupId(paramLong)) {
      return;
    }
    TuSdkDownloadManger.ins.cancelTask(getDownloadTaskType(), paramLong);
  }
  
  public void removeDownloadWithIdt(long paramLong)
  {
    if (!containsGroupId(paramLong)) {
      return;
    }
    removeDownloadData(paramLong);
    ArrayList localArrayList = new ArrayList(this.a);
    Object localObject = null;
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkDownloadItem localTuSdkDownloadItem = (TuSdkDownloadItem)localIterator.next();
      if (localTuSdkDownloadItem.id == paramLong)
      {
        localObject = localTuSdkDownloadItem;
        this.a.remove(localTuSdkDownloadItem);
        FileHelper.delete(localTuSdkDownloadItem.localDownloadPath());
      }
    }
    trySaveTaskDataInCache();
    if (localObject == null) {
      return;
    }
    b((TuSdkDownloadItem)localObject, DownloadTaskStatus.StatusRemoved);
  }
  
  protected abstract void removeDownloadData(long paramLong);
  
  private void a(TuSdkDownloadItem paramTuSdkDownloadItem, DownloadTaskStatus paramDownloadTaskStatus)
  {
    if ((paramDownloadTaskStatus == DownloadTaskStatus.StatusDowned) && (appendDownload(paramTuSdkDownloadItem)))
    {
      this.a.add(paramTuSdkDownloadItem);
      trySaveTaskDataInCache();
    }
    b(paramTuSdkDownloadItem, paramDownloadTaskStatus);
  }
  
  private void b(TuSdkDownloadItem paramTuSdkDownloadItem, DownloadTaskStatus paramDownloadTaskStatus)
  {
    if (getDownloadDelegate() == null) {
      return;
    }
    getDownloadDelegate().onDownloadMangerStatusChanged(null, paramTuSdkDownloadItem, paramDownloadTaskStatus);
  }
  
  protected boolean appendDownload(TuSdkDownloadItem paramTuSdkDownloadItem)
  {
    return (paramTuSdkDownloadItem != null) && (paramTuSdkDownloadItem.localDownloadPath().exists()) && (!containsGroupId(paramTuSdkDownloadItem.id)) && (paramTuSdkDownloadItem.key != null);
  }
  
  protected abstract Collection<?> getAllGroupID();
  
  public JSONObject getAllDatas()
  {
    return TuSdkDownloadManger.ins.getAllDatas(getDownloadTaskType(), new JSONArray(getAllGroupID()));
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\TuSdkDownloadAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */