package org.lasque.tusdk.core.secret;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import org.lasque.tusdk.core.http.HttpHeader;
import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.lasque.tusdk.core.network.TuSdkDownloadManger;
import org.lasque.tusdk.core.network.TuSdkDownloadTask.DownloadTaskType;
import org.lasque.tusdk.core.network.TuSdkHttpEngine;
import org.lasque.tusdk.core.network.TuSdkHttpHandler;
import org.lasque.tusdk.core.network.TuSdkHttpParams;
import org.lasque.tusdk.core.type.DownloadTaskStatus;
import org.lasque.tusdk.core.utils.json.JsonHelper;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;
import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage.StickerPackageDelegate;

public class TuSDKOnlineStickerDownloader
  implements StickerLocalPackage.StickerPackageDelegate
{
  private Set<Long> a = new HashSet(10);
  private TuSDKOnlineStickerDownloaderDelegate b;
  
  public TuSDKOnlineStickerDownloader setDelegate(TuSDKOnlineStickerDownloaderDelegate paramTuSDKOnlineStickerDownloaderDelegate)
  {
    this.b = paramTuSDKOnlineStickerDownloaderDelegate;
    if (paramTuSDKOnlineStickerDownloaderDelegate == null) {
      StickerLocalPackage.shared().removeDelegate(this);
    } else {
      StickerLocalPackage.shared().appenDelegate(this);
    }
    return this;
  }
  
  public TuSDKOnlineStickerDownloaderDelegate getDelegate()
  {
    return this.b;
  }
  
  public final void downloadStickerGroup(StickerGroup paramStickerGroup)
  {
    if (!SdkValid.shared.isVaild()) {
      return;
    }
    if ((paramStickerGroup == null) || (isDownloaded(paramStickerGroup.groupId))) {
      return;
    }
    a(paramStickerGroup);
  }
  
  public final void removeStickerGroup(long paramLong)
  {
    if (!SdkValid.shared.isVaild()) {
      return;
    }
    StickerLocalPackage.shared().removeDownloadWithIdt(paramLong);
  }
  
  public final boolean isDownloaded(long paramLong)
  {
    if (!SdkValid.shared.isVaild()) {
      return false;
    }
    return StickerLocalPackage.shared().containsGroupId(paramLong);
  }
  
  public final boolean isDownloading(long paramLong)
  {
    if (!SdkValid.shared.isVaild()) {
      return false;
    }
    return TuSdkDownloadManger.ins.isDownloading(TuSdkDownloadTask.DownloadTaskType.TypeSticker, paramLong);
  }
  
  public final boolean containsTask(long paramLong)
  {
    if (!SdkValid.shared.isVaild()) {
      return false;
    }
    if (this.a.contains(Long.valueOf(paramLong))) {
      return true;
    }
    return TuSdkDownloadManger.ins.containsTask(TuSdkDownloadTask.DownloadTaskType.TypeSticker, paramLong);
  }
  
  private void a(final StickerGroup paramStickerGroup)
  {
    TuSdkHttpHandler local1 = new TuSdkHttpHandler()
    {
      public void onSuccess(int paramAnonymousInt, List<HttpHeader> paramAnonymousList, String paramAnonymousString)
      {
        TuSDKOnlineStickerDownloader.a(TuSDKOnlineStickerDownloader.this).remove(Long.valueOf(paramStickerGroup.groupId));
        JSONObject localJSONObject = JsonHelper.json(paramAnonymousString);
        if (localJSONObject == null) {
          return;
        }
        try
        {
          StickerLocalPackage.shared().download(localJSONObject.getLong("id"), localJSONObject.getString("key"), localJSONObject.getString("dl_key"));
        }
        catch (JSONException localJSONException)
        {
          localJSONException.printStackTrace();
          TuSDKOnlineStickerDownloader.a(TuSDKOnlineStickerDownloader.this, paramStickerGroup.groupId);
        }
      }
      
      protected void onRequestedSucceed(TuSdkHttpHandler paramAnonymousTuSdkHttpHandler)
      {
        TuSDKOnlineStickerDownloader.a(TuSDKOnlineStickerDownloader.this).remove(Long.valueOf(paramStickerGroup.groupId));
      }
      
      protected void onRequestedFailed(TuSdkHttpHandler paramAnonymousTuSdkHttpHandler)
      {
        TuSDKOnlineStickerDownloader.a(TuSDKOnlineStickerDownloader.this, paramStickerGroup.groupId);
        TuSDKOnlineStickerDownloader.a(TuSDKOnlineStickerDownloader.this).remove(Long.valueOf(paramStickerGroup.groupId));
      }
    };
    this.a.add(Long.valueOf(paramStickerGroup.groupId));
    TuSdkHttpParams localTuSdkHttpParams = new TuSdkHttpParams();
    localTuSdkHttpParams.add("id", String.valueOf(paramStickerGroup.groupId));
    localTuSdkHttpParams.add("devid", TuSdkHttpEngine.shared().getDevId());
    TuSdkHttpEngine.webAPIEngine().post("/sticker/validKey", localTuSdkHttpParams, true, local1);
  }
  
  public void onStickerPackageStatusChanged(StickerLocalPackage paramStickerLocalPackage, TuSdkDownloadItem paramTuSdkDownloadItem, DownloadTaskStatus paramDownloadTaskStatus)
  {
    if (this.b == null) {
      return;
    }
    this.b.onDownloadProgressChanged(paramTuSdkDownloadItem.id, paramTuSdkDownloadItem.progress, paramDownloadTaskStatus);
  }
  
  private void a(long paramLong)
  {
    if (this.b == null) {
      return;
    }
    this.b.onDownloadProgressChanged(paramLong, 0.0F, DownloadTaskStatus.StatusDownFailed);
  }
  
  public void destroy()
  {
    this.b = null;
  }
  
  public static abstract interface TuSDKOnlineStickerDownloaderDelegate
  {
    public abstract void onDownloadProgressChanged(long paramLong, float paramFloat, DownloadTaskStatus paramDownloadTaskStatus);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\secret\TuSDKOnlineStickerDownloader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */