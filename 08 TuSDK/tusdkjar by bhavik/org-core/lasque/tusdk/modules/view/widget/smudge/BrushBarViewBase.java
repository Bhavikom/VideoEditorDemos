package org.lasque.tusdk.modules.view.widget.smudge;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.type.DownloadTaskStatus;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.TuSdkSharedPreferences;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class BrushBarViewBase
  extends TuSdkRelativeLayout
{
  private BrushLocalPackage.BrushLocalPackageDelegate a = new BrushLocalPackage.BrushLocalPackageDelegate()
  {
    public void onBrushPackageStatusChanged(BrushLocalPackage paramAnonymousBrushLocalPackage, TuSdkDownloadItem paramAnonymousTuSdkDownloadItem, DownloadTaskStatus paramAnonymousDownloadTaskStatus)
    {
      if ((paramAnonymousTuSdkDownloadItem == null) || (paramAnonymousDownloadTaskStatus == null)) {
        return;
      }
      switch (BrushBarViewBase.2.a[paramAnonymousDownloadTaskStatus.ordinal()])
      {
      case 1: 
      case 2: 
        BrushBarViewBase.this.refreshBrushDatas();
        break;
      }
    }
  };
  private BrushTableViewInterface.BrushAction b;
  private List<String> c;
  private boolean d = true;
  
  public abstract <T extends View,  extends BrushTableViewInterface> T getTableView();
  
  protected abstract void notifySelectedBrush(BrushData paramBrushData);
  
  protected abstract void refreshBrushDatas();
  
  public BrushBarViewBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public BrushBarViewBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public BrushBarViewBase(Context paramContext)
  {
    super(paramContext);
  }
  
  public void loadView()
  {
    BrushLocalPackage.shared().appenDelegate(this.a);
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    BrushLocalPackage.shared().removeDelegate(this.a);
  }
  
  public void loadBrushes()
  {
    if (!SdkValid.shared.smudgeEnabled())
    {
      TLog.e("You are not allowed to use the smudge feature, please see http://tusdk.com", new Object[0]);
      return;
    }
    List localList;
    if ((this.c == null) || (this.c.size() == 0))
    {
      localList = buildBrushItems();
    }
    else
    {
      localList = BrushLocalPackage.shared().getBrushWithCodes(this.c);
      localList.add(0, BrushLocalPackage.shared().getEeaserBrush());
    }
    BrushData localBrushData = a();
    int i = -1;
    if (localBrushData != null) {
      i = localList.indexOf(localBrushData);
    }
    if (((i == -1) || (localBrushData.code.equals("Eraser"))) && (localList.size() > 1)) {
      localBrushData = (BrushData)localList.get(1);
    }
    if (getTableView() != null)
    {
      ((BrushTableViewInterface)getTableView()).setModeList(localList);
      i = localList.indexOf(localBrushData);
      ((BrushTableViewInterface)getTableView()).setSelectedPosition(i, true);
      ((BrushTableViewInterface)getTableView()).scrollToPosition(i);
    }
    notifySelectedBrush(localBrushData);
  }
  
  protected List<BrushData> buildBrushItems()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(BrushLocalPackage.shared().getEeaserBrush());
    List localList = BrushLocalPackage.shared().getCodes();
    if ((localList != null) && (localList.size() > 0))
    {
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localArrayList.add(BrushLocalPackage.shared().getBrushWithCode(str));
      }
    }
    return localArrayList;
  }
  
  public void selectBrush(BrushData paramBrushData, BrushBarItemCellBase paramBrushBarItemCellBase, int paramInt)
  {
    ((BrushTableViewInterface)getTableView()).changeSelectedPosition(paramInt);
    ((BrushTableViewInterface)getTableView()).smoothScrollByCenter(paramBrushBarItemCellBase);
    if (paramBrushData != null) {
      a(paramBrushData);
    }
  }
  
  private BrushData a()
  {
    if (!this.d) {
      return null;
    }
    String str1 = String.format("lsq_lastbrush_%s", new Object[] { this.b });
    String str2 = TuSdkContext.sharedPreferences().loadSharedCache(str1);
    if (str2 != null) {
      return BrushLocalPackage.shared().getBrushWithCode(str2);
    }
    return null;
  }
  
  private void a(BrushData paramBrushData)
  {
    if (paramBrushData == null) {
      return;
    }
    String str1 = paramBrushData.code;
    if (!this.d) {
      return;
    }
    String str2 = String.format("lsq_lastbrush_%s", new Object[] { this.b });
    TuSdkContext.sharedPreferences().saveSharedCache(str2, str1);
  }
  
  public BrushTableViewInterface.BrushAction getAction()
  {
    return this.b;
  }
  
  public void setAction(BrushTableViewInterface.BrushAction paramBrushAction)
  {
    this.b = paramBrushAction;
  }
  
  public List<String> getBrushGroup()
  {
    return this.c;
  }
  
  public void setBrushGroup(List<String> paramList)
  {
    this.c = paramList;
  }
  
  public boolean isSaveLastBrush()
  {
    return this.d;
  }
  
  public void setSaveLastBrush(boolean paramBoolean)
  {
    this.d = paramBoolean;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\smudge\BrushBarViewBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */