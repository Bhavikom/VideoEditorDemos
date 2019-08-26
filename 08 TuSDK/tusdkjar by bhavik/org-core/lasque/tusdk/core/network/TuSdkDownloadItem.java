package org.lasque.tusdk.core.network;

import java.io.File;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.type.DownloadTaskStatus;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class TuSdkDownloadItem
  extends JsonBaseBean
  implements Serializable
{
  @DataBase("id")
  public long id;
  @DataBase("key")
  public String key;
  @DataBase("status")
  public int status;
  @DataBase("progress")
  public float progress;
  public String userId;
  public TuSdkDownloadTask.DownloadTaskType type;
  public String fileName;
  public String fileId;
  
  public DownloadTaskStatus getStatus()
  {
    return DownloadTaskStatus.getType(this.status);
  }
  
  public void setStatus(DownloadTaskStatus paramDownloadTaskStatus)
  {
    if (paramDownloadTaskStatus != null) {
      this.status = paramDownloadTaskStatus.getFlag();
    } else {
      this.status = 0;
    }
  }
  
  public File localDownloadPath()
  {
    if (this.fileName == null) {
      this.fileName = String.format("tusdk_%s_%s.gsce", new Object[] { this.type.getAct(), Long.valueOf(this.id) });
    }
    return new File(TuSdk.getAppDownloadPath(), this.fileName);
  }
  
  public File localTempPath()
  {
    return new File(TuSdk.getAppTempPath(), String.format("_download_tusdk_%s_%s.tmp", new Object[] { this.type.getAct(), Long.valueOf(this.id) }));
  }
  
  public JSONObject getStatusChangeData()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.putOpt("fun", "statusChange");
      localJSONObject.putOpt("type", this.type.getAct());
      localJSONObject.putOpt("item", buildJson());
    }
    catch (JSONException localJSONException)
    {
      TLog.e(localJSONException, "StickerLocalPackage getAllDatas", new Object[0]);
    }
    return localJSONObject;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\TuSdkDownloadItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */