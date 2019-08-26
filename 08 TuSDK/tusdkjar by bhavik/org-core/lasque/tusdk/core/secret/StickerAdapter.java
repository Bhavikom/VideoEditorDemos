package org.lasque.tusdk.core.secret;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkBundle;
import org.lasque.tusdk.core.TuSdkConfigs;
import org.lasque.tusdk.core.network.TuSdkDownloadAdapter;
import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.lasque.tusdk.core.network.TuSdkDownloadTask.DownloadTaskType;
import org.lasque.tusdk.core.sticker.StickerPositionInfo;
import org.lasque.tusdk.core.task.ImageViewTaskWare;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.json.JsonWrapper;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory.StickerCategoryType;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import org.lasque.tusdk.modules.view.widget.sticker.StickerText;

public class StickerAdapter
  extends TuSdkDownloadAdapter<ImageViewTaskWare>
{
  private List<StickerCategory> a;
  private Hashtable<Long, StickerGroup> b;
  private List<StickerGroup> c;
  private Hashtable<Long, StickerData> d;
  private TuSdkConfigs e;
  private boolean f;
  private int g;
  
  public boolean isInited()
  {
    return this.f;
  }
  
  public List<StickerCategory> getCategories()
  {
    if (this.a != null)
    {
      ArrayList localArrayList = new ArrayList(this.a.size());
      Iterator localIterator = this.a.iterator();
      while (localIterator.hasNext())
      {
        StickerCategory localStickerCategory = (StickerCategory)localIterator.next();
        localArrayList.add(localStickerCategory.copy());
      }
      return localArrayList;
    }
    return null;
  }
  
  public List<StickerCategory> getCategories(List<StickerCategory> paramList)
  {
    if ((paramList == null) || (paramList.isEmpty())) {
      return getCategories();
    }
    ArrayList localArrayList = new ArrayList(paramList.size());
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      StickerCategory localStickerCategory1 = (StickerCategory)localIterator.next();
      StickerCategory localStickerCategory2 = getCategory(localStickerCategory1.id);
      if (localStickerCategory2 != null) {
        localArrayList.add(localStickerCategory2);
      }
    }
    return localArrayList;
  }
  
  public List<StickerGroup> getGroupListByType(StickerCategory.StickerCategoryType paramStickerCategoryType)
  {
    if (paramStickerCategoryType == StickerCategory.StickerCategoryType.StickerCategorySmart) {
      return this.c;
    }
    return null;
  }
  
  public StickerCategory getCategory(long paramLong)
  {
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      StickerCategory localStickerCategory = (StickerCategory)localIterator.next();
      if (localStickerCategory.id == paramLong) {
        return localStickerCategory.copy();
      }
    }
    return null;
  }
  
  public StickerData getSticker(long paramLong)
  {
    StickerData localStickerData = (StickerData)this.d.get(Long.valueOf(paramLong));
    if (localStickerData != null) {
      return localStickerData.copy();
    }
    return null;
  }
  
  public StickerGroup getStickerGroup(long paramLong)
  {
    return (StickerGroup)this.b.get(Long.valueOf(paramLong));
  }
  
  public StickerAdapter(TuSdkConfigs paramTuSdkConfigs)
  {
    this.e = paramTuSdkConfigs;
    setDownloadTaskType(TuSdkDownloadTask.DownloadTaskType.TypeSticker);
    a();
  }
  
  private void a()
  {
    this.f = false;
    this.g = 0;
    this.a = new ArrayList();
    this.b = new Hashtable();
    this.c = new ArrayList();
    this.d = new Hashtable();
    if ((this.e != null) && (this.e.stickerCategories != null))
    {
      Iterator localIterator = this.e.stickerCategories.iterator();
      while (localIterator.hasNext())
      {
        StickerCategory localStickerCategory = (StickerCategory)localIterator.next();
        this.a.add(localStickerCategory.copy());
      }
    }
    tryLoadTaskDataWithCache();
    new Thread(new Runnable()
    {
      public void run()
      {
        StickerAdapter.this.asyncLoadLocalStickers();
      }
    }).start();
  }
  
  protected void asyncLoadLocalStickers()
  {
    asyncLoadDownloadDatas();
    if ((this.e != null) && (this.e.stickerGroups != null))
    {
      ArrayList localArrayList = new ArrayList(this.e.stickerGroups);
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        StickerGroup localStickerGroup = (StickerGroup)localIterator.next();
        a(localStickerGroup);
      }
    }
    new Handler(Looper.getMainLooper()).post(new Runnable()
    {
      public void run()
      {
        StickerAdapter.a(StickerAdapter.this, true);
        TLog.d("StickerAdapter inited: %s", new Object[] { Integer.valueOf(StickerAdapter.a(StickerAdapter.this)) });
      }
    });
  }
  
  private void a(StickerGroup paramStickerGroup)
  {
    if (paramStickerGroup.file == null) {
      return;
    }
    String str1 = TuSdkBundle.sdkBundleSticker(paramStickerGroup.file);
    String str2 = SdkValid.shared.loadStickerGroup(str1, null);
    if (str2 == null) {
      return;
    }
    StickerGroup localStickerGroup = (StickerGroup)JsonWrapper.deserialize(str2, StickerGroup.class);
    if ((localStickerGroup == null) || (this.b.containsKey(Long.valueOf(localStickerGroup.groupId)))) {
      return;
    }
    if ((localStickerGroup.stickers == null) || (localStickerGroup.stickers.isEmpty())) {
      return;
    }
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      StickerCategory localStickerCategory = (StickerCategory)localIterator.next();
      if (localStickerCategory.id == localStickerGroup.categoryId) {
        localStickerCategory.append(localStickerGroup);
      }
    }
    a(localStickerGroup, paramStickerGroup.file);
    this.b.put(Long.valueOf(localStickerGroup.groupId), localStickerGroup);
    a(paramStickerGroup, localStickerGroup);
  }
  
  private void a(StickerGroup paramStickerGroup1, StickerGroup paramStickerGroup2)
  {
    paramStickerGroup2.validKey = paramStickerGroup1.validKey;
    paramStickerGroup2.file = paramStickerGroup1.file;
    if (paramStickerGroup1.name != null) {
      paramStickerGroup2.name = paramStickerGroup1.name;
    }
    if ((paramStickerGroup1.stickers == null) || (paramStickerGroup1.stickers.isEmpty()))
    {
      localObject1 = paramStickerGroup2.stickers.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (StickerData)((Iterator)localObject1).next();
        this.d.put(Long.valueOf(((StickerData)localObject2).stickerId), localObject2);
        this.g += 1;
      }
      return;
    }
    Object localObject1 = new ArrayList(paramStickerGroup1.stickers.size());
    Object localObject2 = paramStickerGroup1.stickers.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      StickerData localStickerData1 = (StickerData)((Iterator)localObject2).next();
      StickerData localStickerData2 = paramStickerGroup2.getSticker(localStickerData1.stickerId);
      if (localStickerData2 != null)
      {
        ((ArrayList)localObject1).add(localStickerData2);
        this.d.put(Long.valueOf(localStickerData2.stickerId), localStickerData2);
        a(localStickerData1, localStickerData2);
        this.g += 1;
      }
    }
    paramStickerGroup2.stickers = ((ArrayList)localObject1);
  }
  
  private void a(StickerData paramStickerData1, StickerData paramStickerData2)
  {
    if (paramStickerData1.width > 0) {
      paramStickerData2.width = paramStickerData1.width;
    }
    if (paramStickerData1.height > 0) {
      paramStickerData2.height = paramStickerData1.height;
    }
    if ((paramStickerData1.texts == null) || (paramStickerData1.texts.isEmpty()) || (paramStickerData2.texts == null) || (paramStickerData2.texts.isEmpty())) {
      return;
    }
    Iterator localIterator = paramStickerData2.texts.iterator();
    while (localIterator.hasNext())
    {
      StickerText localStickerText1 = (StickerText)localIterator.next();
      StickerText localStickerText2 = paramStickerData1.getStickerText(localStickerText1.textId);
      if (localStickerText2 != null)
      {
        if (localStickerText2.content != null) {
          localStickerText1.content = localStickerText2.content;
        }
        if (localStickerText2.color != null) {
          localStickerText1.color = localStickerText2.color;
        }
      }
    }
  }
  
  public void loadThumb(StickerData paramStickerData, ImageView paramImageView)
  {
    if ((paramStickerData == null) || (paramImageView == null)) {
      return;
    }
    StickerData localStickerData = (StickerData)this.d.get(Long.valueOf(paramStickerData.stickerId));
    if (localStickerData == null) {
      return;
    }
    StickerThumbTaskImageWare localStickerThumbTaskImageWare = new StickerThumbTaskImageWare(paramImageView, localStickerData);
    loadImage(localStickerThumbTaskImageWare);
  }
  
  public void loadGroupThumb(StickerGroup paramStickerGroup, ImageView paramImageView)
  {
    if ((paramStickerGroup == null) || (paramImageView == null)) {
      return;
    }
    StickerGroup localStickerGroup = (StickerGroup)this.b.get(Long.valueOf(paramStickerGroup.groupId));
    if (localStickerGroup == null) {
      return;
    }
    StickerGroupThumbTaskImageWare localStickerGroupThumbTaskImageWare = new StickerGroupThumbTaskImageWare(paramImageView, localStickerGroup);
    loadImage(localStickerGroupThumbTaskImageWare);
  }
  
  public boolean loadStickerItem(StickerData paramStickerData)
  {
    if (paramStickerData == null) {
      return false;
    }
    paramStickerData.setImage(null);
    StickerGroup localStickerGroup = (StickerGroup)this.b.get(Long.valueOf(paramStickerData.groupId));
    if (localStickerGroup == null) {
      return false;
    }
    StickerData localStickerData = localStickerGroup.getSticker(paramStickerData.stickerId);
    if (localStickerData == null) {
      return false;
    }
    Bitmap localBitmap = SdkValid.shared.readSticker(localStickerData.groupId, localStickerData.stickerImageName);
    if (localBitmap == null) {
      return false;
    }
    paramStickerData.setImage(localBitmap);
    StatisticsManger.appendSticker(paramStickerData);
    return true;
  }
  
  public Bitmap loadSmartStickerItem(StickerData paramStickerData, String paramString)
  {
    if ((paramStickerData == null) || (StringHelper.isBlank(paramString))) {
      return null;
    }
    StickerGroup localStickerGroup = (StickerGroup)this.b.get(Long.valueOf(paramStickerData.groupId));
    if (localStickerGroup == null) {
      return null;
    }
    StickerData localStickerData = localStickerGroup.getSticker(paramStickerData.stickerId);
    if (localStickerData == null) {
      return null;
    }
    return SdkValid.shared.readSticker(localStickerData.groupId, paramString);
  }
  
  protected String getCacheKey(ImageViewTaskWare paramImageViewTaskWare)
  {
    if (paramImageViewTaskWare == null) {
      return null;
    }
    if ((paramImageViewTaskWare instanceof StickerGroupThumbTaskImageWare)) {
      return ((StickerGroupThumbTaskImageWare)paramImageViewTaskWare).data.previewName;
    }
    StickerData localStickerData = ((StickerThumbTaskImageWare)paramImageViewTaskWare).data;
    return TextUtils.isEmpty(localStickerData.previewName) ? localStickerData.stickerImageName : localStickerData.previewName;
  }
  
  protected Bitmap asyncTaskLoadImage(ImageViewTaskWare paramImageViewTaskWare)
  {
    Bitmap localBitmap = null;
    Object localObject;
    if ((paramImageViewTaskWare instanceof StickerGroupThumbTaskImageWare))
    {
      localObject = (StickerGroupThumbTaskImageWare)paramImageViewTaskWare;
      if (localObject != null) {
        localBitmap = SdkValid.shared.readSticker(((StickerGroupThumbTaskImageWare)localObject).data.groupId, ((StickerGroupThumbTaskImageWare)localObject).data.previewName);
      }
    }
    else
    {
      localObject = (StickerThumbTaskImageWare)paramImageViewTaskWare;
      if (localObject != null) {
        localBitmap = SdkValid.shared.readStickerThumb(((StickerThumbTaskImageWare)localObject).data.groupId, ((StickerThumbTaskImageWare)localObject).data.stickerId);
      }
    }
    return localBitmap;
  }
  
  public boolean containsGroupId(long paramLong)
  {
    return this.b.containsKey(Long.valueOf(paramLong));
  }
  
  protected void removeDownloadData(long paramLong)
  {
    StickerGroup localStickerGroup = (StickerGroup)this.b.get(Long.valueOf(paramLong));
    ArrayList localArrayList = new ArrayList(this.a);
    Iterator localIterator = localArrayList.iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = (StickerCategory)localIterator.next();
      if (((StickerCategory)localObject).id == localStickerGroup.categoryId) {
        ((StickerCategory)localObject).removeGroup(paramLong);
      }
    }
    this.b.remove(Long.valueOf(paramLong));
    if (localStickerGroup.stickers != null)
    {
      localIterator = localStickerGroup.stickers.iterator();
      while (localIterator.hasNext())
      {
        localObject = (StickerData)localIterator.next();
        this.d.remove(Long.valueOf(((StickerData)localObject).stickerId));
        this.g -= 1;
      }
    }
    SdkValid.shared.removeStickerGroup(paramLong);
    TLog.d("remove download stickers [%s]: %s | %s", new Object[] { Long.valueOf(paramLong), Integer.valueOf(this.g), Integer.valueOf(this.d.size()) });
  }
  
  protected boolean appendDownload(TuSdkDownloadItem paramTuSdkDownloadItem)
  {
    if (!super.appendDownload(paramTuSdkDownloadItem)) {
      return false;
    }
    String str = SdkValid.shared.loadStickerGroup(paramTuSdkDownloadItem.localDownloadPath().getAbsolutePath(), paramTuSdkDownloadItem.key);
    if (str == null) {
      return false;
    }
    StickerGroup localStickerGroup = (StickerGroup)JsonWrapper.deserialize(str, StickerGroup.class);
    if (localStickerGroup == null) {
      return false;
    }
    if ((localStickerGroup.stickers == null) || (localStickerGroup.stickers.isEmpty())) {
      return false;
    }
    localStickerGroup.file = paramTuSdkDownloadItem.localDownloadPath().getAbsolutePath();
    localStickerGroup.isDownload = true;
    b(localStickerGroup);
    return true;
  }
  
  public boolean addStickerGroupFile(File paramFile, long paramLong, String paramString)
  {
    if ((paramFile == null) || (!paramFile.exists()) || (paramFile.isDirectory()))
    {
      TLog.e("sticker file does not exist", new Object[0]);
      return false;
    }
    if ((paramString == null) || (TextUtils.isEmpty(paramString)))
    {
      TLog.e("Please enter a valid master", new Object[0]);
      return false;
    }
    if (containsGroupId(paramLong)) {
      return false;
    }
    String str1 = SdkValid.shared.stickerGroupValidKey(paramLong, paramString);
    String str2 = SdkValid.shared.loadStickerGroup(paramFile.getAbsolutePath(), str1);
    if (str2 == null) {
      return false;
    }
    StickerGroup localStickerGroup = (StickerGroup)JsonWrapper.deserialize(str2, StickerGroup.class);
    if (localStickerGroup == null) {
      return false;
    }
    if ((localStickerGroup.stickers == null) || (localStickerGroup.stickers.isEmpty())) {
      return false;
    }
    localStickerGroup.file = paramFile.getAbsolutePath();
    localStickerGroup.isDownload = true;
    b(localStickerGroup);
    return true;
  }
  
  private boolean b(StickerGroup paramStickerGroup)
  {
    if ((paramStickerGroup == null) || (containsGroupId(paramStickerGroup.groupId))) {
      return false;
    }
    Iterator localIterator = this.a.iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = (StickerCategory)localIterator.next();
      if (((StickerCategory)localObject).id == paramStickerGroup.categoryId) {
        ((StickerCategory)localObject).insertFirst(paramStickerGroup);
      }
    }
    a(paramStickerGroup, paramStickerGroup.file);
    this.b.put(Long.valueOf(paramStickerGroup.groupId), paramStickerGroup);
    localIterator = paramStickerGroup.stickers.iterator();
    while (localIterator.hasNext())
    {
      localObject = (StickerData)localIterator.next();
      this.d.put(Long.valueOf(((StickerData)localObject).stickerId), localObject);
      this.g += 1;
    }
    return true;
  }
  
  protected Collection<?> getAllGroupID()
  {
    return this.b.keySet();
  }
  
  private void a(StickerGroup paramStickerGroup, String paramString)
  {
    if (paramStickerGroup.categoryId != StickerCategory.StickerCategoryType.StickerCategorySmart.getValue()) {
      return;
    }
    this.c.add(paramStickerGroup);
    int i = paramString.lastIndexOf(".");
    if (i < 0) {
      return;
    }
    String str1 = TuSdk.getAppTempPath() + File.separator + paramString.substring(0, i);
    if (!new File(str1).isDirectory()) {
      return;
    }
    Iterator localIterator = paramStickerGroup.stickers.iterator();
    while (localIterator.hasNext())
    {
      StickerData localStickerData = (StickerData)localIterator.next();
      String str2 = str1 + File.separator + localStickerData.stickerId + ".json";
      String str3 = "";
      File localFile = new File(str2);
      if (localFile.exists())
      {
        str3 = new String(FileHelper.readFile(localFile));
        StickerPositionInfo localStickerPositionInfo = (StickerPositionInfo)JsonWrapper.deserialize(str3, StickerPositionInfo.class);
        localStickerData.positionInfo = localStickerPositionInfo;
      }
    }
  }
  
  public static class StickerGroupThumbTaskImageWare
    extends ImageViewTaskWare
  {
    public StickerGroup data;
    
    public StickerGroupThumbTaskImageWare(ImageView paramImageView, StickerGroup paramStickerGroup)
    {
      setImageView(paramImageView);
      this.data = paramStickerGroup;
      setImageCompress(0);
    }
  }
  
  public static class StickerThumbTaskImageWare
    extends ImageViewTaskWare
  {
    public StickerData data;
    
    public StickerThumbTaskImageWare(ImageView paramImageView, StickerData paramStickerData)
    {
      setImageView(paramImageView);
      this.data = paramStickerData;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\secret\StickerAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */