package org.lasque.tusdk.modules.view.widget.sticker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.utils.ArrayHelper;
import org.lasque.tusdk.core.utils.ArrayHelper.ArrayGroup;
import org.lasque.tusdk.core.view.listview.TuSdkIndexPath;
import org.lasque.tusdk.core.view.listview.TuSdkIndexPath.TuSdkDataSource;

public abstract class StickerListDataSource
  implements TuSdkIndexPath.TuSdkDataSource
{
  private List<TuSdkIndexPath> a;
  private List<StickerGroup> b;
  private List<List<StickerData>> c;
  private int d;
  
  public StickerListDataSource(StickerCategory paramStickerCategory)
  {
    a(paramStickerCategory);
  }
  
  private void a(StickerCategory paramStickerCategory)
  {
    if ((paramStickerCategory == null) || (paramStickerCategory.datas == null)) {
      return;
    }
    List localList1 = paramStickerCategory.datas;
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    this.d = 0;
    int i = 0;
    Iterator localIterator1 = localList1.iterator();
    while (localIterator1.hasNext())
    {
      StickerGroup localStickerGroup = (StickerGroup)localIterator1.next();
      if (localStickerGroup.stickers != null)
      {
        localArrayList2.add(new TuSdkIndexPath(i, -1, 1));
        this.d += localStickerGroup.stickers.size();
        ArrayHelper.ArrayGroup localArrayGroup = ArrayHelper.splitForGroupsize(localStickerGroup.stickers, 4);
        Iterator localIterator2 = localArrayGroup.iterator();
        while (localIterator2.hasNext())
        {
          List localList2 = (List)localIterator2.next();
          localArrayList2.add(new TuSdkIndexPath(i, localArrayList1.size(), 0));
          localArrayList1.add(localList2);
        }
        i++;
      }
    }
    this.b = localList1;
    this.c = localArrayList1;
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
    if (this.b != null) {
      return this.b.size();
    }
    return 1;
  }
  
  public int rowCount(int paramInt)
  {
    if (this.c != null) {
      return ((List)this.c.get(paramInt)).size();
    }
    return 0;
  }
  
  public int count()
  {
    return this.d;
  }
  
  public Object getItem(TuSdkIndexPath paramTuSdkIndexPath)
  {
    if ((paramTuSdkIndexPath.viewType == 0) && (this.c != null)) {
      return this.c.get(paramTuSdkIndexPath.row);
    }
    if ((paramTuSdkIndexPath.viewType == 1) && (this.b != null)) {
      return this.b.get(paramTuSdkIndexPath.section);
    }
    return null;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\sticker\StickerListDataSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */