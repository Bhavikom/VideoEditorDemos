package org.lasque.tusdk.modules.view.widget.smudge;

import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdkConfigs;
import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.lasque.tusdk.core.network.TuSdkDownloadManger;
import org.lasque.tusdk.core.network.TuSdkDownloadManger.TuSdkDownloadMangerDelegate;
import org.lasque.tusdk.core.secret.BrushAdapter;
import org.lasque.tusdk.core.type.DownloadTaskStatus;

public class BrushLocalPackage
  implements TuSdkDownloadManger.TuSdkDownloadMangerDelegate
{
  public static final String EraserBrushCode = "Eraser";
  private static BrushLocalPackage a;
  private BrushAdapter b;
  private List<BrushLocalPackageDelegate> c = new ArrayList();
  
  public static BrushLocalPackage shared()
  {
    return a;
  }
  
  public static BrushLocalPackage init(TuSdkConfigs paramTuSdkConfigs)
  {
    if ((a == null) && (paramTuSdkConfigs != null)) {
      a = new BrushLocalPackage(paramTuSdkConfigs);
    }
    return a;
  }
  
  public void appenDelegate(BrushLocalPackageDelegate paramBrushLocalPackageDelegate)
  {
    if ((paramBrushLocalPackageDelegate == null) || (this.c.contains(paramBrushLocalPackageDelegate))) {
      return;
    }
    this.c.add(paramBrushLocalPackageDelegate);
  }
  
  public void removeDelegate(BrushLocalPackageDelegate paramBrushLocalPackageDelegate)
  {
    if (paramBrushLocalPackageDelegate == null) {
      return;
    }
    this.c.remove(paramBrushLocalPackageDelegate);
  }
  
  private BrushLocalPackage(TuSdkConfigs paramTuSdkConfigs)
  {
    this.b = new BrushAdapter(paramTuSdkConfigs);
    this.b.setDownloadDelegate(this);
  }
  
  public List<String> getCodes()
  {
    return this.b.getCodes();
  }
  
  public boolean isInited()
  {
    return this.b.isInited();
  }
  
  public List<String> verifyCodes(List<String> paramList)
  {
    return this.b.verifyCodes(paramList);
  }
  
  public BrushData getEeaserBrush()
  {
    return this.b.getEraserBrush();
  }
  
  public BrushData getBrushWithCode(String paramString)
  {
    return this.b.getBrushWithCode(paramString);
  }
  
  public List<BrushData> getBrushWithCodes(List<String> paramList)
  {
    return this.b.getBrushWithCodes(paramList);
  }
  
  public boolean loadBrushData(BrushData paramBrushData)
  {
    return this.b.loadBrushData(paramBrushData);
  }
  
  public void loadThumbWithBrush(ImageView paramImageView, BrushData paramBrushData)
  {
    this.b.loadThumbWithBrush(paramBrushData, paramImageView);
  }
  
  public void cancelLoadImage(ImageView paramImageView)
  {
    this.b.cancelLoadImage(paramImageView);
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
  
  public void onDownloadMangerStatusChanged(TuSdkDownloadManger paramTuSdkDownloadManger, TuSdkDownloadItem paramTuSdkDownloadItem, DownloadTaskStatus paramDownloadTaskStatus)
  {
    ArrayList localArrayList = new ArrayList(this.c);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      BrushLocalPackageDelegate localBrushLocalPackageDelegate = (BrushLocalPackageDelegate)localIterator.next();
      localBrushLocalPackageDelegate.onBrushPackageStatusChanged(this, paramTuSdkDownloadItem, paramDownloadTaskStatus);
    }
  }
  
  public static abstract interface BrushLocalPackageDelegate
  {
    public abstract void onBrushPackageStatusChanged(BrushLocalPackage paramBrushLocalPackage, TuSdkDownloadItem paramTuSdkDownloadItem, DownloadTaskStatus paramDownloadTaskStatus);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\smudge\BrushLocalPackage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */