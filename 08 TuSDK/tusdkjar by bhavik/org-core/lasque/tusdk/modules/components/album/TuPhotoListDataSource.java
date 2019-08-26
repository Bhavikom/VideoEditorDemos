package org.lasque.tusdk.modules.components.album;

import android.content.Context;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.utils.ArrayHelper;
import org.lasque.tusdk.core.utils.ArrayHelper.ArrayGroup;
import org.lasque.tusdk.core.utils.TuSdkDate;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.core.view.listview.TuSdkIndexPath;
import org.lasque.tusdk.core.view.listview.TuSdkIndexPath.TuSdkDataSource;

public abstract class TuPhotoListDataSource
  implements TuSdkIndexPath.TuSdkDataSource
{
  private List<TuSdkIndexPath> a;
  private ArrayList<ImageSqlInfo> b;
  private ArrayList<TuSdkDate> c;
  private List<List<ImageSqlInfo>> d;
  
  public TuPhotoListDataSource(Context paramContext, AlbumSqlInfo paramAlbumSqlInfo, boolean paramBoolean)
  {
    if (paramAlbumSqlInfo == null) {
      return;
    }
    this.b = ImageSqlHelper.getPhotoList(paramContext, paramAlbumSqlInfo.id);
    a(this.b);
  }
  
  private void a(ArrayList<ImageSqlInfo> paramArrayList)
  {
    if (this.b == null) {
      return;
    }
    Hashtable localHashtable = new Hashtable();
    ArrayList localArrayList1 = new ArrayList();
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      ImageSqlInfo localImageSqlInfo = (ImageSqlInfo)localIterator.next();
      TuSdkDate localTuSdkDate = TuSdkDate.create(localImageSqlInfo.createDate).beginingOfDay();
      ArrayList localArrayList2 = (ArrayList)localHashtable.get(Long.valueOf(localTuSdkDate.getTimeInMillis()));
      if (localArrayList2 == null)
      {
        localArrayList1.add(localTuSdkDate);
        localArrayList2 = new ArrayList();
        localHashtable.put(Long.valueOf(localTuSdkDate.getTimeInMillis()), localArrayList2);
      }
      localArrayList2.add(localImageSqlInfo);
    }
    buildIndexPaths(localHashtable, localArrayList1);
  }
  
  protected void buildIndexPaths(Hashtable<Long, ArrayList<ImageSqlInfo>> paramHashtable, ArrayList<TuSdkDate> paramArrayList)
  {
    if (paramHashtable == null) {
      return;
    }
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    int i = 0;
    Iterator localIterator1 = paramArrayList.iterator();
    while (localIterator1.hasNext())
    {
      TuSdkDate localTuSdkDate = (TuSdkDate)localIterator1.next();
      localArrayList2.add(new TuSdkIndexPath(i, -1, 1));
      ArrayList localArrayList3 = (ArrayList)paramHashtable.get(Long.valueOf(localTuSdkDate.getTimeInMillis()));
      ArrayHelper.ArrayGroup localArrayGroup = ArrayHelper.splitForGroupsize(localArrayList3, 4);
      Iterator localIterator2 = localArrayGroup.iterator();
      while (localIterator2.hasNext())
      {
        List localList = (List)localIterator2.next();
        localArrayList2.add(new TuSdkIndexPath(i, localArrayList1.size(), 0));
        localArrayList1.add(localList);
      }
      i++;
    }
    this.c = paramArrayList;
    this.d = localArrayList1;
    this.a = localArrayList2;
  }
  
  public List<TuSdkIndexPath> getIndexPaths()
  {
    if (this.a == null) {
      this.a = new ArrayList(0);
    }
    return this.a;
  }
  
  public TuSdkIndexPath getIndexPath(int paramInt)
  {
    if ((this.a == null) || (paramInt >= this.a.size()) || (paramInt < 0)) {
      return null;
    }
    return (TuSdkIndexPath)this.a.get(paramInt);
  }
  
  public int viewTypes()
  {
    return 2;
  }
  
  public int sectionCount()
  {
    if (this.c != null) {
      return this.c.size();
    }
    return 1;
  }
  
  public int rowCount(int paramInt)
  {
    if (this.d != null) {
      return ((List)this.d.get(paramInt)).size();
    }
    return 0;
  }
  
  public int count()
  {
    if (this.b != null) {
      return this.b.size();
    }
    return 0;
  }
  
  public Object getItem(TuSdkIndexPath paramTuSdkIndexPath)
  {
    if ((paramTuSdkIndexPath.viewType == 0) && (this.d != null)) {
      return this.d.get(paramTuSdkIndexPath.row);
    }
    if ((paramTuSdkIndexPath.viewType == 1) && (this.c != null)) {
      return this.c.get(paramTuSdkIndexPath.section);
    }
    return null;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\album\TuPhotoListDataSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */