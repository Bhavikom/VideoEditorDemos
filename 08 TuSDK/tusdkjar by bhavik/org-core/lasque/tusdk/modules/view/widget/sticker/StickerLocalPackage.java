package org.lasque.tusdk.modules.view.widget.sticker;

import android.graphics.Bitmap;
import android.widget.ImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdkConfigs;
import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.lasque.tusdk.core.network.TuSdkDownloadManger;
import org.lasque.tusdk.core.network.TuSdkDownloadManger.TuSdkDownloadMangerDelegate;
import org.lasque.tusdk.core.secret.StickerAdapter;
import org.lasque.tusdk.core.type.DownloadTaskStatus;

public class StickerLocalPackage
  implements TuSdkDownloadManger.TuSdkDownloadMangerDelegate
{
  private static StickerLocalPackage a;
  private StickerAdapter b;
  private List<StickerPackageDelegate> c = new ArrayList();
  
  public static StickerLocalPackage shared()
  {
    return a;
  }
  
  public static StickerLocalPackage init(TuSdkConfigs paramTuSdkConfigs)
  {
    if ((a == null) && (paramTuSdkConfigs != null)) {
      a = new StickerLocalPackage(paramTuSdkConfigs);
    }
    return a;
  }
  
  public void appenDelegate(StickerPackageDelegate paramStickerPackageDelegate)
  {
    if ((paramStickerPackageDelegate == null) || (this.c.contains(paramStickerPackageDelegate))) {
      return;
    }
    this.c.add(paramStickerPackageDelegate);
  }
  
  public void removeDelegate(StickerPackageDelegate paramStickerPackageDelegate)
  {
    if (paramStickerPackageDelegate == null) {
      return;
    }
    this.c.remove(paramStickerPackageDelegate);
  }
  
  public boolean isInited()
  {
    return this.b.isInited();
  }
  
  public List<StickerCategory> getCategories()
  {
    return this.b.getCategories();
  }
  
  public List<StickerCategory> getCategories(List<StickerCategory> paramList)
  {
    return this.b.getCategories(paramList);
  }
  
  public StickerCategory getCategory(long paramLong)
  {
    return this.b.getCategory(paramLong);
  }
  
  public StickerGroup getStickerGroup(long paramLong)
  {
    return this.b.getStickerGroup(paramLong);
  }
  
  public StickerData getSticker(long paramLong)
  {
    return this.b.getSticker(paramLong);
  }
  
  public boolean containsGroupId(long paramLong)
  {
    return this.b.containsGroupId(paramLong);
  }
  
  public List<StickerGroup> getSmartStickerGroups()
  {
    return getSmartStickerGroups(true);
  }
  
  public List<StickerGroup> getSmartStickerGroups(boolean paramBoolean)
  {
    List localList = this.b.getGroupListByType(StickerCategory.StickerCategoryType.StickerCategorySmart);
    if (localList == null) {
      return null;
    }
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      StickerGroup localStickerGroup = (StickerGroup)localIterator.next();
      if ((paramBoolean) || ((!paramBoolean) && (!localStickerGroup.requireFaceFeature()))) {
        localArrayList.add(localStickerGroup);
      }
    }
    return localArrayList;
  }
  
  private StickerLocalPackage(TuSdkConfigs paramTuSdkConfigs)
  {
    this.b = new StickerAdapter(paramTuSdkConfigs);
    this.b.setDownloadDelegate(this);
  }
  
  public void loadThumb(StickerData paramStickerData, ImageView paramImageView)
  {
    this.b.loadThumb(paramStickerData, paramImageView);
  }
  
  public void loadGroupThumb(StickerGroup paramStickerGroup, ImageView paramImageView)
  {
    this.b.loadGroupThumb(paramStickerGroup, paramImageView);
  }
  
  public boolean loadStickerItem(StickerData paramStickerData)
  {
    return this.b.loadStickerItem(paramStickerData);
  }
  
  public Bitmap loadSmartStickerItem(StickerData paramStickerData, String paramString)
  {
    return this.b.loadSmartStickerItem(paramStickerData, paramString);
  }
  
  public boolean addStickerGroupFile(File paramFile, long paramLong, String paramString)
  {
    return this.b.addStickerGroupFile(paramFile, paramLong, paramString);
  }
  
  public void download(long paramLong, String paramString1, String paramString2)
  {
    this.b.download(paramLong, paramString1, paramString2);
  }
  
  public void cancelDownload(long paramLong)
  {
    this.b.cancelDownload(paramLong);
  }
  
  public void removeDownloadWithIdt(long paramLong)
  {
    this.b.removeDownloadWithIdt(paramLong);
  }
  
  public JSONObject getAllDatas()
  {
    return this.b.getAllDatas();
  }
  
  public void cancelLoadImage(ImageView paramImageView)
  {
    this.b.cancelLoadImage(paramImageView);
  }
  
  public void onDownloadMangerStatusChanged(TuSdkDownloadManger paramTuSdkDownloadManger, TuSdkDownloadItem paramTuSdkDownloadItem, DownloadTaskStatus paramDownloadTaskStatus)
  {
    ArrayList localArrayList = new ArrayList(this.c);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      StickerPackageDelegate localStickerPackageDelegate = (StickerPackageDelegate)localIterator.next();
      localStickerPackageDelegate.onStickerPackageStatusChanged(this, paramTuSdkDownloadItem, paramDownloadTaskStatus);
    }
  }
  
  public static abstract interface StickerPackageDelegate
  {
    public abstract void onStickerPackageStatusChanged(StickerLocalPackage paramStickerLocalPackage, TuSdkDownloadItem paramTuSdkDownloadItem, DownloadTaskStatus paramDownloadTaskStatus);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\sticker\StickerLocalPackage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */