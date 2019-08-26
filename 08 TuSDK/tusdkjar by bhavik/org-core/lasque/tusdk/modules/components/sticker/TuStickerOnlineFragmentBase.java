package org.lasque.tusdk.modules.components.sticker;

import android.view.ViewGroup;
import org.json.JSONObject;
import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.lasque.tusdk.core.network.TuSdkDownloadTask.DownloadTaskType;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.type.DownloadTaskStatus;
import org.lasque.tusdk.impl.activity.TuOnlineFragment;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;
import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage.StickerPackageDelegate;

public abstract class TuStickerOnlineFragmentBase
  extends TuOnlineFragment
{
  private StickerLocalPackage.StickerPackageDelegate a = new StickerLocalPackage.StickerPackageDelegate()
  {
    public void onStickerPackageStatusChanged(StickerLocalPackage paramAnonymousStickerLocalPackage, TuSdkDownloadItem paramAnonymousTuSdkDownloadItem, DownloadTaskStatus paramAnonymousDownloadTaskStatus)
    {
      TuStickerOnlineFragmentBase.a(TuStickerOnlineFragmentBase.this, paramAnonymousTuSdkDownloadItem);
    }
  };
  
  protected abstract void onHandleSelected(StickerData paramStickerData);
  
  protected abstract void onHandleDetail(long paramLong);
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    getWebview();
    setOnlineType(TuSdkDownloadTask.DownloadTaskType.TypeSticker.getAct());
    StatisticsManger.appendComponent(ComponentActType.editStickerOnlineFragment);
  }
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    super.viewDidLoad(paramViewGroup);
    StickerLocalPackage.shared().appenDelegate(this.a);
  }
  
  public void onDestroyView()
  {
    super.onDestroyView();
    StickerLocalPackage.shared().removeDelegate(this.a);
  }
  
  protected String getPageFinishedData()
  {
    return StickerLocalPackage.shared().getAllDatas().toString();
  }
  
  protected void onResourceDownload(long paramLong, String paramString1, String paramString2)
  {
    StickerLocalPackage.shared().download(paramLong, paramString1, paramString2);
  }
  
  protected void onResourceCancelDownload(long paramLong)
  {
    StickerLocalPackage.shared().cancelDownload(paramLong);
  }
  
  protected void handleSelected(String[] paramArrayOfString)
  {
    if (paramArrayOfString.length < 4) {
      return;
    }
    long l = Long.parseLong(paramArrayOfString[3]);
    StickerData localStickerData = StickerLocalPackage.shared().getSticker(l);
    if (localStickerData == null) {
      return;
    }
    onHandleSelected(localStickerData);
  }
  
  protected void handleDetail(String[] paramArrayOfString)
  {
    if (paramArrayOfString.length < 3) {
      return;
    }
    long l = Long.parseLong(paramArrayOfString[2]);
    onHandleDetail(l);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\sticker\TuStickerOnlineFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */