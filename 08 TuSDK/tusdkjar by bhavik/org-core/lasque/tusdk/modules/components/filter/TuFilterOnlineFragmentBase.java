package org.lasque.tusdk.modules.components.filter;

import android.view.ViewGroup;
import org.json.JSONObject;
import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.lasque.tusdk.core.network.TuSdkDownloadTask.DownloadTaskType;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage.FilterLocalPackageDelegate;
import org.lasque.tusdk.core.type.DownloadTaskStatus;
import org.lasque.tusdk.impl.activity.TuOnlineFragment;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewInterface.GroupFilterAction;

public abstract class TuFilterOnlineFragmentBase
  extends TuOnlineFragment
{
  private GroupFilterItemViewInterface.GroupFilterAction a = GroupFilterItemViewInterface.GroupFilterAction.ActionNormal;
  private FilterLocalPackage.FilterLocalPackageDelegate b = new FilterLocalPackage.FilterLocalPackageDelegate()
  {
    public void onFilterPackageStatusChanged(FilterLocalPackage paramAnonymousFilterLocalPackage, TuSdkDownloadItem paramAnonymousTuSdkDownloadItem, DownloadTaskStatus paramAnonymousDownloadTaskStatus)
    {
      TuFilterOnlineFragmentBase.a(TuFilterOnlineFragmentBase.this, paramAnonymousTuSdkDownloadItem);
    }
  };
  
  protected abstract void onHandleSelected(long paramLong);
  
  protected abstract void onHandleDetail(long paramLong);
  
  public GroupFilterItemViewInterface.GroupFilterAction getAction()
  {
    if (this.a == null) {
      this.a = GroupFilterItemViewInterface.GroupFilterAction.ActionNormal;
    }
    return this.a;
  }
  
  public void setAction(GroupFilterItemViewInterface.GroupFilterAction paramGroupFilterAction)
  {
    this.a = paramGroupFilterAction;
  }
  
  protected void loadView(ViewGroup paramViewGroup)
  {
    getWebview();
    setOnlineType(TuSdkDownloadTask.DownloadTaskType.TypeFilter.getAct());
    setArgs("action=" + this.a.getValue());
    StatisticsManger.appendComponent(ComponentActType.editFilterOnlineFragment);
  }
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    super.viewDidLoad(paramViewGroup);
    FilterLocalPackage.shared().appenDelegate(this.b);
  }
  
  public void onDestroyView()
  {
    super.onDestroyView();
    FilterLocalPackage.shared().removeDelegate(this.b);
  }
  
  protected String getPageFinishedData()
  {
    return FilterLocalPackage.shared().getAllDatas().toString();
  }
  
  protected void onResourceDownload(long paramLong, String paramString1, String paramString2)
  {
    FilterLocalPackage.shared().download(paramLong, paramString1, paramString2);
  }
  
  protected void onResourceCancelDownload(long paramLong)
  {
    FilterLocalPackage.shared().cancelDownload(paramLong);
  }
  
  protected void handleSelected(String[] paramArrayOfString)
  {
    if (paramArrayOfString.length < 3) {
      return;
    }
    long l = Long.parseLong(paramArrayOfString[2]);
    onHandleSelected(l);
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


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\filter\TuFilterOnlineFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */