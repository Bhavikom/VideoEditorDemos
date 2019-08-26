package org.lasque.tusdk.modules.view.widget.sticker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.lasque.tusdk.core.type.DownloadTaskStatus;
import org.lasque.tusdk.core.view.TuSdkLinearLayout;

public abstract class StickerBarViewBase
  extends TuSdkLinearLayout
{
  private StickerLocalPackage.StickerPackageDelegate a = new StickerLocalPackage.StickerPackageDelegate()
  {
    public void onStickerPackageStatusChanged(StickerLocalPackage paramAnonymousStickerLocalPackage, TuSdkDownloadItem paramAnonymousTuSdkDownloadItem, DownloadTaskStatus paramAnonymousDownloadTaskStatus)
    {
      if ((paramAnonymousTuSdkDownloadItem == null) || (paramAnonymousDownloadTaskStatus == null)) {
        return;
      }
      switch (StickerBarViewBase.2.a[paramAnonymousDownloadTaskStatus.ordinal()])
      {
      case 1: 
      case 2: 
        StickerBarViewBase.this.refreshCateDatas();
        break;
      }
    }
  };
  private List<StickerCategory> b;
  private int c = -1;
  
  protected abstract View buildCateButton(StickerCategory paramStickerCategory, int paramInt, LinearLayout.LayoutParams paramLayoutParams);
  
  protected abstract void selectCateButton(Integer paramInteger);
  
  protected abstract void refreshCateDatas();
  
  public StickerBarViewBase(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public StickerBarViewBase(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public StickerBarViewBase(Context paramContext)
  {
    super(paramContext);
  }
  
  public void loadView()
  {
    StickerLocalPackage.shared().appenDelegate(this.a);
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    StickerLocalPackage.shared().removeDelegate(this.a);
  }
  
  public void loadCategories(List<StickerCategory> paramList)
  {
    this.b = new ArrayList();
    StickerCategory localStickerCategory = new StickerCategory();
    localStickerCategory.name = "lsq_sticker_cate_all";
    localStickerCategory.extendType = StickerCategory.StickerCategoryExtendType.ExtendTypeAll;
    this.b.add(localStickerCategory);
    if ((paramList == null) || (paramList.size() == 0)) {
      paramList = StickerLocalPackage.shared().getCategories();
    }
    this.b.addAll(paramList);
    a();
    selectCateIndex(0);
  }
  
  private void a()
  {
    if (this.b == null) {
      return;
    }
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, -1, 1.0F);
    int i = 0;
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      StickerCategory localStickerCategory = (StickerCategory)localIterator.next();
      buildCateButton(localStickerCategory, i, localLayoutParams);
      i++;
    }
  }
  
  protected void selectCateIndex(int paramInt)
  {
    if ((this.c == paramInt) || (this.b == null) || (this.b.size() <= paramInt)) {
      return;
    }
    this.c = paramInt;
    selectCateButton(Integer.valueOf(paramInt));
    refreshCateDatas();
  }
  
  protected StickerCategory getCurrentCate()
  {
    if ((this.c < 0) || (this.b == null) || (this.b.size() <= this.c)) {
      return null;
    }
    return (StickerCategory)this.b.get(this.c);
  }
  
  protected List<StickerData> getStickerDatas(long paramLong)
  {
    StickerCategory localStickerCategory = StickerLocalPackage.shared().getCategory(paramLong);
    if ((localStickerCategory == null) || (localStickerCategory.datas == null)) {
      return null;
    }
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = localStickerCategory.datas.iterator();
    while (localIterator.hasNext())
    {
      StickerGroup localStickerGroup = (StickerGroup)localIterator.next();
      if (localStickerGroup.stickers != null) {
        localArrayList.addAll(localStickerGroup.stickers);
      }
    }
    return localArrayList;
  }
  
  protected List<StickerData> getAllStickerDatas()
  {
    if (this.b == null) {
      return null;
    }
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      StickerCategory localStickerCategory = (StickerCategory)localIterator.next();
      if (localStickerCategory.extendType == null)
      {
        List localList = getStickerDatas(localStickerCategory.id);
        if (localList != null) {
          localArrayList.addAll(localList);
        }
      }
    }
    return localArrayList;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\sticker\StickerBarViewBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */