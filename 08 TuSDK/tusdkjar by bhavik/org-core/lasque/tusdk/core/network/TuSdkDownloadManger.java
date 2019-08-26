package org.lasque.tusdk.core.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.type.DownloadTaskStatus;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.TuSdkSharedPreferences;

public class TuSdkDownloadManger
{
  public static final TuSdkDownloadManger ins = new TuSdkDownloadManger();
  private List<TuSdkDownloadTask> a;
  private TuSdkDownloadTask b;
  private List<TuSdkDownloadMangerDelegate> c = new ArrayList();
  private TuSdkDownloadTask.TuSdkDownloadTaskDelegate d = new TuSdkDownloadTask.TuSdkDownloadTaskDelegate()
  {
    public void onDownloadTaskStatusChanged(TuSdkDownloadTask paramAnonymousTuSdkDownloadTask, DownloadTaskStatus paramAnonymousDownloadTaskStatus)
    {
      TuSdkDownloadManger.a(TuSdkDownloadManger.this, paramAnonymousTuSdkDownloadTask, paramAnonymousDownloadTaskStatus);
    }
  };
  
  public void appenDelegate(TuSdkDownloadMangerDelegate paramTuSdkDownloadMangerDelegate)
  {
    if ((paramTuSdkDownloadMangerDelegate == null) || (this.c.contains(paramTuSdkDownloadMangerDelegate))) {
      return;
    }
    this.c.add(paramTuSdkDownloadMangerDelegate);
  }
  
  public void removeDelegate(TuSdkDownloadMangerDelegate paramTuSdkDownloadMangerDelegate)
  {
    if (paramTuSdkDownloadMangerDelegate == null) {
      return;
    }
    this.c.remove(paramTuSdkDownloadMangerDelegate);
  }
  
  private TuSdkDownloadManger()
  {
    b();
    d();
  }
  
  public boolean isDownloading(TuSdkDownloadTask.DownloadTaskType paramDownloadTaskType, long paramLong)
  {
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      TuSdkDownloadTask localTuSdkDownloadTask = (TuSdkDownloadTask)localIterator.next();
      if ((localTuSdkDownloadTask.getItem().type == paramDownloadTaskType) && (localTuSdkDownloadTask.getItem().id == paramLong)) {
        return localTuSdkDownloadTask.getItem().getStatus() == DownloadTaskStatus.StatusDowning;
      }
    }
    return false;
  }
  
  public boolean containsTask(TuSdkDownloadTask.DownloadTaskType paramDownloadTaskType, long paramLong)
  {
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      TuSdkDownloadTask localTuSdkDownloadTask = (TuSdkDownloadTask)localIterator.next();
      if ((localTuSdkDownloadTask.getItem().type == paramDownloadTaskType) && (localTuSdkDownloadTask.getItem().id == paramLong)) {
        return (localTuSdkDownloadTask.getItem().getStatus() == DownloadTaskStatus.StatusInit) || (localTuSdkDownloadTask.getItem().getStatus() == DownloadTaskStatus.StatusDowning);
      }
    }
    return false;
  }
  
  private String a()
  {
    String str = String.format("%s-queue", new Object[] { getClass() });
    return str;
  }
  
  private void b()
  {
    this.a = new ArrayList();
    ArrayList localArrayList = (ArrayList)TuSdkContext.sharedPreferences().loadSharedCacheObject(a());
    if ((localArrayList == null) || (localArrayList.isEmpty())) {
      return;
    }
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkDownloadItem localTuSdkDownloadItem = (TuSdkDownloadItem)localIterator.next();
      TuSdkDownloadTask localTuSdkDownloadTask = new TuSdkDownloadTask(localTuSdkDownloadItem);
      if (localTuSdkDownloadTask.canRunTask()) {
        this.a.add(localTuSdkDownloadTask);
      } else {
        localTuSdkDownloadTask.clear();
      }
    }
    TLog.d("load download tasks: %s", new Object[] { Integer.valueOf(this.a.size()) });
  }
  
  private void c()
  {
    ArrayList localArrayList1 = new ArrayList(this.a);
    ArrayList localArrayList2 = new ArrayList(this.a.size());
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      TuSdkDownloadTask localTuSdkDownloadTask = (TuSdkDownloadTask)localIterator.next();
      if (localTuSdkDownloadTask.canRunTask()) {
        localArrayList2.add(localTuSdkDownloadTask.getItem());
      }
    }
    TuSdkContext.sharedPreferences().saveSharedCacheObject(a(), localArrayList2);
  }
  
  private List<TuSdkDownloadItem> a(TuSdkDownloadTask.DownloadTaskType paramDownloadTaskType)
  {
    if (paramDownloadTaskType == null) {
      return null;
    }
    ArrayList localArrayList1 = new ArrayList(this.a);
    ArrayList localArrayList2 = new ArrayList(this.a.size());
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      TuSdkDownloadTask localTuSdkDownloadTask = (TuSdkDownloadTask)localIterator.next();
      if (localTuSdkDownloadTask.getItem().type == paramDownloadTaskType) {
        localArrayList2.add(localTuSdkDownloadTask.getItem());
      }
    }
    return localArrayList2;
  }
  
  public JSONObject getAllDatas(TuSdkDownloadTask.DownloadTaskType paramDownloadTaskType, JSONArray paramJSONArray)
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.putOpt("fun", "init");
      localJSONObject.putOpt("type", paramDownloadTaskType.getAct());
      if (paramJSONArray != null) {
        localJSONObject.putOpt("locals", paramJSONArray);
      }
      JSONArray localJSONArray = new JSONArray();
      List localList = a(paramDownloadTaskType);
      if (localList != null)
      {
        Iterator localIterator = localList.iterator();
        while (localIterator.hasNext())
        {
          TuSdkDownloadItem localTuSdkDownloadItem = (TuSdkDownloadItem)localIterator.next();
          localJSONArray.put(localTuSdkDownloadItem.buildJson());
        }
        localJSONObject.putOpt("queues", localJSONArray);
      }
    }
    catch (JSONException localJSONException)
    {
      TLog.e(localJSONException, "StickerLocalPackage getAllDatas", new Object[0]);
    }
    return localJSONObject;
  }
  
  public void appenTask(TuSdkDownloadTask.DownloadTaskType paramDownloadTaskType, long paramLong, String paramString1, String paramString2)
  {
    if ((paramDownloadTaskType == null) || (paramString1 == null) || (paramLong < 1L)) {
      return;
    }
    cancelTask(paramDownloadTaskType, paramLong);
    TuSdkDownloadItem localTuSdkDownloadItem = new TuSdkDownloadItem();
    localTuSdkDownloadItem.type = paramDownloadTaskType;
    localTuSdkDownloadItem.id = paramLong;
    localTuSdkDownloadItem.key = paramString1;
    localTuSdkDownloadItem.fileId = paramString2;
    this.a.add(new TuSdkDownloadTask(localTuSdkDownloadItem));
    c();
    d();
  }
  
  public boolean cancelTask(TuSdkDownloadTask.DownloadTaskType paramDownloadTaskType, long paramLong)
  {
    ArrayList localArrayList = new ArrayList(this.a);
    boolean bool = false;
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkDownloadTask localTuSdkDownloadTask = (TuSdkDownloadTask)localIterator.next();
      if (localTuSdkDownloadTask.equals(paramDownloadTaskType, paramLong))
      {
        localTuSdkDownloadTask.cancel();
        this.a.remove(paramDownloadTaskType);
        bool = true;
      }
    }
    if (bool) {
      c();
    }
    return bool;
  }
  
  private void d()
  {
    if ((this.b != null) || (this.a.isEmpty())) {
      return;
    }
    ArrayList localArrayList = new ArrayList(this.a);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkDownloadTask localTuSdkDownloadTask = (TuSdkDownloadTask)localIterator.next();
      if (localTuSdkDownloadTask.canRunTask())
      {
        this.b = localTuSdkDownloadTask;
        this.b.setDelegate(this.d);
        this.b.start();
        break;
      }
      this.a.remove(localTuSdkDownloadTask);
    }
  }
  
  private void a(TuSdkDownloadTask paramTuSdkDownloadTask, DownloadTaskStatus paramDownloadTaskStatus)
  {
    if (!paramTuSdkDownloadTask.canRunTask())
    {
      this.b = null;
      this.a.remove(paramTuSdkDownloadTask);
      d();
    }
    c();
    b(paramTuSdkDownloadTask, paramDownloadTaskStatus);
  }
  
  private void b(TuSdkDownloadTask paramTuSdkDownloadTask, DownloadTaskStatus paramDownloadTaskStatus)
  {
    ArrayList localArrayList = new ArrayList(this.c);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      TuSdkDownloadMangerDelegate localTuSdkDownloadMangerDelegate = (TuSdkDownloadMangerDelegate)localIterator.next();
      localTuSdkDownloadMangerDelegate.onDownloadMangerStatusChanged(this, paramTuSdkDownloadTask.getItem(), paramDownloadTaskStatus);
    }
  }
  
  public static abstract interface TuSdkDownloadMangerDelegate
  {
    public abstract void onDownloadMangerStatusChanged(TuSdkDownloadManger paramTuSdkDownloadManger, TuSdkDownloadItem paramTuSdkDownloadItem, DownloadTaskStatus paramDownloadTaskStatus);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\TuSdkDownloadManger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */