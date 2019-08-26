package org.lasque.tusdk.modules.components.edit;

import android.graphics.Bitmap;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;

public class TuDraftImageWrap
{
  protected float mScreenSizeScale = 0.75F;
  private final List<File> a = new ArrayList();
  private final List<File> b = new ArrayList();
  private File c;
  private ImageSqlInfo d;
  private Bitmap e;
  
  public File getTempFilePath()
  {
    return this.c;
  }
  
  public void setTempFilePath(File paramFile)
  {
    this.c = paramFile;
  }
  
  public ImageSqlInfo getImageSqlInfo()
  {
    return this.d;
  }
  
  public void setImageSqlInfo(ImageSqlInfo paramImageSqlInfo)
  {
    this.d = paramImageSqlInfo;
  }
  
  public List<File> getHistories()
  {
    return this.a;
  }
  
  public void setHistories(List<File> paramList)
  {
    this.a.clear();
    this.a.addAll(paramList);
  }
  
  public List<File> getBrushies()
  {
    return this.b;
  }
  
  public void setBrushies(List<File> paramList)
  {
    this.b.clear();
    this.b.addAll(paramList);
  }
  
  public int getHistoriesSize()
  {
    return getHistories().size();
  }
  
  public int getBrushiesSize()
  {
    return getBrushies().size();
  }
  
  public Bitmap getImage()
  {
    return getImage(1);
  }
  
  public Bitmap getImage(int paramInt)
  {
    Bitmap localBitmap = null;
    TuSdkSize localTuSdkSize = new TuSdkSize(paramInt, paramInt);
    if (getLastSteps() != null) {
      localBitmap = BitmapHelper.getBitmap(getLastSteps(), true);
    }
    if (localBitmap == null) {
      localBitmap = BitmapHelper.getBitmap(getTempFilePath(), localTuSdkSize, true);
    }
    if (localBitmap == null) {
      localBitmap = BitmapHelper.getBitmap(getImageSqlInfo(), true, localTuSdkSize);
    }
    if (localBitmap == null) {
      return this.e;
    }
    return localBitmap;
  }
  
  public Bitmap getThumbImage(int paramInt1, int paramInt2)
  {
    TuSdkSize localTuSdkSize = new TuSdkSize(paramInt1, paramInt2);
    Bitmap localBitmap = null;
    if (getLastSteps() != null) {
      localBitmap = BitmapHelper.getBitmap(getLastSteps(), localTuSdkSize, true);
    }
    if (localBitmap == null) {
      localBitmap = BitmapHelper.getBitmap(getTempFilePath(), localTuSdkSize, true);
    }
    if (localBitmap == null) {
      localBitmap = BitmapHelper.getBitmap(getImageSqlInfo(), true, localTuSdkSize);
    }
    return localBitmap;
  }
  
  public void setImage(Bitmap paramBitmap)
  {
    this.e = paramBitmap;
  }
  
  public File getLastSteps()
  {
    if (this.a.size() == 0) {
      return null;
    }
    return (File)this.a.get(this.a.size() - 1);
  }
  
  protected File popLastSteps()
  {
    File localFile = getLastSteps();
    if (localFile != null) {
      this.a.remove(this.a.size() - 1);
    }
    return localFile;
  }
  
  protected ImageSqlInfo getOutputImageSqlInfo()
  {
    ImageSqlInfo localImageSqlInfo = new ImageSqlInfo();
    File localFile = popLastSteps();
    if (localFile == null) {
      localFile = getTempFilePath();
    }
    if (localFile == null) {
      return getImageSqlInfo();
    }
    localImageSqlInfo.path = localFile.getAbsolutePath();
    return localImageSqlInfo;
  }
  
  public TuSdkSize getImageDisplaySize()
  {
    TuSdkSize localTuSdkSize = ContextUtils.getScreenSize(TuSdkContext.context());
    if (localTuSdkSize != null)
    {
      localTuSdkSize.width = ((int)Math.floor(localTuSdkSize.width * this.mScreenSizeScale));
      localTuSdkSize.height = ((int)Math.floor(localTuSdkSize.height * this.mScreenSizeScale));
    }
    return localTuSdkSize;
  }
  
  public boolean isChanged()
  {
    return ((this.a != null) && (this.a.size() > 1)) || (isFromCarmera());
  }
  
  public boolean isFromCarmera()
  {
    return getImageSqlInfo() == null;
  }
  
  protected void clearAllSteps()
  {
    clearSteps(this.a);
    clearSteps(this.b);
  }
  
  protected void clearSteps(List<File> paramList)
  {
    if (paramList == null) {
      return;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      File localFile = (File)localIterator.next();
      TLog.d("clearSteps (%s): %s", new Object[] { Long.valueOf(localFile.length()), localFile });
      FileHelper.delete(localFile);
    }
    paramList.clear();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\edit\TuDraftImageWrap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */