package org.lasque.tusdk.core.secret;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.TuSdkBundle;
import org.lasque.tusdk.core.TuSdkConfigs;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.network.TuSdkDownloadAdapter;
import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.lasque.tusdk.core.network.TuSdkDownloadTask.DownloadTaskType;
import org.lasque.tusdk.core.task.ImageViewTaskWare;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.json.JsonWrapper;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
import org.lasque.tusdk.modules.view.widget.smudge.BrushData.BrushType;
import org.lasque.tusdk.modules.view.widget.smudge.BrushGroup;

public class BrushAdapter
  extends TuSdkDownloadAdapter<BrushThumbTaskImageWare>
{
  public static final String EraserBrushCode = "Eraser";
  private ArrayList<BrushGroup> a;
  private ArrayList<BrushData> b;
  private TuSdkConfigs c;
  private boolean d;
  private int e;
  
  public BrushAdapter(TuSdkConfigs paramTuSdkConfigs)
  {
    this.c = paramTuSdkConfigs;
    setDownloadTaskType(TuSdkDownloadTask.DownloadTaskType.TypeBrush);
    if (SdkValid.shared.smudgeEnabled()) {
      a();
    }
  }
  
  public boolean isInited()
  {
    return this.d;
  }
  
  public List<String> getCodes()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      BrushData localBrushData = (BrushData)localIterator.next();
      localArrayList.add(localBrushData.code);
    }
    return localArrayList;
  }
  
  public BrushData getEraserBrush()
  {
    BrushData localBrushData = new BrushData();
    localBrushData.setType(BrushData.BrushType.TypeEraser);
    localBrushData.code = "Eraser";
    return localBrushData;
  }
  
  public BrushData getBrushWithCode(String paramString)
  {
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      BrushData localBrushData = (BrushData)localIterator.next();
      if (localBrushData.code.equalsIgnoreCase(paramString)) {
        return localBrushData;
      }
    }
    return null;
  }
  
  public List<String> verifyCodes(List<String> paramList)
  {
    if ((!this.d) || (paramList == null) || (this.b == null) || (!SdkValid.shared.sdkValid())) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(paramList.size());
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      if (getBrushWithCode(str) != null) {
        localArrayList.add(str);
      }
    }
    return localArrayList;
  }
  
  public List<BrushData> getBrushWithCodes(List<String> paramList)
  {
    if ((!this.d) || (paramList == null) || (this.b == null) || (!SdkValid.shared.sdkValid())) {
      return null;
    }
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      BrushData localBrushData = getBrushWithCode(str);
      if (localBrushData != null) {
        localArrayList.add(localBrushData);
      }
    }
    return localArrayList;
  }
  
  private void a()
  {
    this.d = false;
    this.e = 0;
    this.a = new ArrayList();
    this.b = new ArrayList();
    tryLoadTaskDataWithCache();
    new Thread(new Runnable()
    {
      public void run()
      {
        BrushAdapter.this.asyncLoadLocalBrushes();
      }
    }).start();
  }
  
  protected void asyncLoadLocalBrushes()
  {
    asyncLoadDownloadDatas();
    if ((this.c != null) && (this.c.brushGroups != null))
    {
      ArrayList localArrayList = new ArrayList(this.c.brushGroups);
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        BrushGroup localBrushGroup = (BrushGroup)localIterator.next();
        a(localBrushGroup);
      }
    }
    new Handler(Looper.getMainLooper()).post(new Runnable()
    {
      public void run()
      {
        BrushAdapter.a(BrushAdapter.this, true);
        TLog.d("BrushAdapter inited: %s", new Object[] { Integer.valueOf(BrushAdapter.a(BrushAdapter.this)) });
      }
    });
  }
  
  private void a(BrushGroup paramBrushGroup)
  {
    if (paramBrushGroup.file == null) {
      return;
    }
    String str1 = TuSdkBundle.sdkBundleBrush(paramBrushGroup.file);
    String str2 = SdkValid.shared.loadBrushGroup(str1, null);
    if (str2 == null) {
      return;
    }
    BrushGroup localBrushGroup = (BrushGroup)JsonWrapper.deserialize(str2, BrushGroup.class);
    if ((localBrushGroup == null) || (localBrushGroup.brushes == null) || (localBrushGroup.brushes.isEmpty())) {
      return;
    }
    this.a.add(localBrushGroup);
    a(paramBrushGroup, localBrushGroup);
  }
  
  private void a(BrushGroup paramBrushGroup1, BrushGroup paramBrushGroup2)
  {
    paramBrushGroup2.validKey = paramBrushGroup1.validKey;
    if (paramBrushGroup1.name != null) {
      paramBrushGroup2.name = paramBrushGroup1.name;
    }
    if ((paramBrushGroup1.brushes == null) || (paramBrushGroup1.brushes.isEmpty()))
    {
      localObject1 = paramBrushGroup2.brushes.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (BrushData)((Iterator)localObject1).next();
        this.b.add(localObject2);
        this.e += 1;
      }
      return;
    }
    Object localObject1 = new ArrayList(paramBrushGroup1.brushes.size());
    Object localObject2 = paramBrushGroup1.brushes.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      BrushData localBrushData1 = (BrushData)((Iterator)localObject2).next();
      BrushData localBrushData2 = paramBrushGroup2.getBrush(localBrushData1.brushId);
      if (localBrushData2 != null)
      {
        ((ArrayList)localObject1).add(localBrushData2);
        this.b.add(localBrushData2);
        a(localBrushData1, localBrushData2);
        this.e += 1;
      }
    }
    paramBrushGroup2.brushes = ((ArrayList)localObject1);
  }
  
  private void a(BrushData paramBrushData1, BrushData paramBrushData2)
  {
    if (paramBrushData2.args == null)
    {
      paramBrushData2.args = paramBrushData1.args;
    }
    else if (paramBrushData1.args != null)
    {
      Iterator localIterator = paramBrushData1.args.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        paramBrushData2.args.put(localEntry.getKey(), localEntry.getValue());
      }
    }
    if (paramBrushData1.name != null) {
      paramBrushData2.name = paramBrushData1.name;
    }
    if (paramBrushData1.thumb != null) {
      paramBrushData2.thumb = paramBrushData1.thumb;
    }
  }
  
  public void loadThumbWithBrush(BrushData paramBrushData, ImageView paramImageView)
  {
    if ((paramBrushData == null) || (paramImageView == null)) {
      return;
    }
    BrushData localBrushData = getBrushWithCode(paramBrushData.code);
    if ((localBrushData == null) || (a(paramImageView, localBrushData.thumb))) {
      return;
    }
    BrushThumbTaskImageWare localBrushThumbTaskImageWare = new BrushThumbTaskImageWare(paramImageView, localBrushData);
    loadImage(localBrushThumbTaskImageWare);
  }
  
  private boolean a(ImageView paramImageView, String paramString)
  {
    if (paramString == null) {
      return false;
    }
    int i = TuSdkContext.getDrawableResId(paramString);
    if (i != 0)
    {
      paramImageView.setImageResource(i);
      return true;
    }
    return false;
  }
  
  public boolean loadBrushData(BrushData paramBrushData)
  {
    if (paramBrushData == null) {
      return false;
    }
    paramBrushData.setImage(null);
    if (paramBrushData.code.equals("Eraser"))
    {
      paramBrushData.setImage(BitmapHelper.createOvalImage(200, 200, -16777216));
      return true;
    }
    BrushGroup localBrushGroup = findGroupByID(Long.valueOf(paramBrushData.groupId));
    if (localBrushGroup == null) {
      return false;
    }
    BrushData localBrushData = localBrushGroup.getBrush(paramBrushData.brushId);
    if (localBrushData == null) {
      return false;
    }
    Bitmap localBitmap = SdkValid.shared.readBrush(localBrushData.groupId, localBrushData.brushImageKey);
    if (localBitmap == null) {
      return false;
    }
    paramBrushData.setImage(localBitmap);
    StatisticsManger.appendBrush(paramBrushData);
    return true;
  }
  
  public BrushGroup findGroupByID(Long paramLong)
  {
    if ((this.a != null) && (this.a.size() > 0))
    {
      Iterator localIterator = this.a.iterator();
      while (localIterator.hasNext())
      {
        BrushGroup localBrushGroup = (BrushGroup)localIterator.next();
        if (localBrushGroup.groupId == paramLong.longValue()) {
          return localBrushGroup;
        }
      }
    }
    return null;
  }
  
  protected String getCacheKey(BrushThumbTaskImageWare paramBrushThumbTaskImageWare)
  {
    return TextUtils.isEmpty(paramBrushThumbTaskImageWare.data.thumbKey) ? paramBrushThumbTaskImageWare.data.code : paramBrushThumbTaskImageWare.data.thumbKey;
  }
  
  protected Bitmap asyncTaskLoadImage(BrushThumbTaskImageWare paramBrushThumbTaskImageWare)
  {
    Bitmap localBitmap = null;
    if (paramBrushThumbTaskImageWare.data != null) {
      localBitmap = SdkValid.shared.readBrushThumb(paramBrushThumbTaskImageWare.data.groupId, paramBrushThumbTaskImageWare.data.brushId);
    }
    return localBitmap;
  }
  
  public boolean containsGroupId(long paramLong)
  {
    return findGroupByID(Long.valueOf(paramLong)) != null;
  }
  
  protected void removeDownloadData(long paramLong)
  {
    BrushGroup localBrushGroup = findGroupByID(Long.valueOf(paramLong));
    this.a.remove(localBrushGroup);
    if (localBrushGroup.brushes != null)
    {
      Iterator localIterator = localBrushGroup.brushes.iterator();
      while (localIterator.hasNext())
      {
        BrushData localBrushData = (BrushData)localIterator.next();
        this.b.remove(localBrushData);
        this.e -= 1;
      }
    }
    SdkValid.shared.removeBrushGroup(paramLong);
    TLog.d("remove download brushes [%s]: %s | %s", new Object[] { Long.valueOf(paramLong), Integer.valueOf(this.e), Integer.valueOf(this.b.size()) });
  }
  
  protected boolean appendDownload(TuSdkDownloadItem paramTuSdkDownloadItem)
  {
    if (!super.appendDownload(paramTuSdkDownloadItem)) {
      return false;
    }
    String str = SdkValid.shared.loadBrushGroup(paramTuSdkDownloadItem.localDownloadPath().getAbsolutePath(), paramTuSdkDownloadItem.key);
    if (str == null) {
      return false;
    }
    BrushGroup localBrushGroup = (BrushGroup)JsonWrapper.deserialize(str, BrushGroup.class);
    if (localBrushGroup == null) {
      return false;
    }
    if ((localBrushGroup.brushes == null) || (localBrushGroup.brushes.isEmpty())) {
      return false;
    }
    localBrushGroup.isDownload = true;
    this.a.add(localBrushGroup);
    Iterator localIterator = localBrushGroup.brushes.iterator();
    while (localIterator.hasNext())
    {
      BrushData localBrushData = (BrushData)localIterator.next();
      this.b.add(localBrushData);
      this.e += 1;
    }
    return true;
  }
  
  protected Collection<?> getAllGroupID()
  {
    ArrayList localArrayList = new ArrayList();
    if ((this.a != null) && (this.a.size() > 0))
    {
      Iterator localIterator = this.a.iterator();
      while (localIterator.hasNext())
      {
        BrushGroup localBrushGroup = (BrushGroup)localIterator.next();
        localArrayList.add(Long.valueOf(localBrushGroup.groupId));
      }
    }
    return localArrayList;
  }
  
  public static class BrushThumbTaskImageWare
    extends ImageViewTaskWare
  {
    public BrushData data;
    
    public BrushThumbTaskImageWare(ImageView paramImageView, BrushData paramBrushData)
    {
      setImageView(paramImageView);
      this.data = paramBrushData;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\secret\BrushAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */