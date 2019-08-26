package org.lasque.tusdk.modules.components.sticker;

import android.view.ViewGroup;
import java.util.List;
import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.type.DownloadTaskStatus;
import org.lasque.tusdk.impl.activity.TuComponentFragment;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;
import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage.StickerPackageDelegate;

public abstract class TuStickerChooseFragmentBase
  extends TuComponentFragment
{
  private List<StickerCategory> a;
  private StickerLocalPackage.StickerPackageDelegate b = new StickerLocalPackage.StickerPackageDelegate()
  {
    public void onStickerPackageStatusChanged(StickerLocalPackage paramAnonymousStickerLocalPackage, TuSdkDownloadItem paramAnonymousTuSdkDownloadItem, DownloadTaskStatus paramAnonymousDownloadTaskStatus)
    {
      if ((paramAnonymousTuSdkDownloadItem == null) || (paramAnonymousDownloadTaskStatus == null)) {
        return;
      }
      switch (TuStickerChooseFragmentBase.2.a[paramAnonymousDownloadTaskStatus.ordinal()])
      {
      case 1: 
      case 2: 
        TuStickerChooseFragmentBase.this.reloadStickers();
        break;
      }
    }
  };
  
  public List<StickerCategory> getCategories()
  {
    if (this.a == null) {
      this.a = StickerLocalPackage.shared().getCategories();
    }
    return this.a;
  }
  
  public void setCategories(List<StickerCategory> paramList)
  {
    this.a = paramList;
  }
  
  protected StickerCategory getCategory(int paramInt)
  {
    if ((getCategories() == null) || (paramInt < 0) || (paramInt >= this.a.size())) {
      return null;
    }
    return (StickerCategory)this.a.get(paramInt);
  }
  
  protected void loadView(ViewGroup paramViewGroup) {}
  
  protected void viewDidLoad(ViewGroup paramViewGroup)
  {
    StickerLocalPackage.shared().appenDelegate(this.b);
    StatisticsManger.appendComponent(ComponentActType.editStickerLocalFragment);
  }
  
  public void onDestroyView()
  {
    super.onDestroyView();
    StickerLocalPackage.shared().removeDelegate(this.b);
  }
  
  protected void removeStickerGroup(StickerGroup paramStickerGroup)
  {
    if (paramStickerGroup == null) {
      return;
    }
    StickerLocalPackage.shared().removeDownloadWithIdt(paramStickerGroup.groupId);
  }
  
  protected void reloadStickers()
  {
    setCategories(StickerLocalPackage.shared().getCategories(getCategories()));
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\sticker\TuStickerChooseFragmentBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */