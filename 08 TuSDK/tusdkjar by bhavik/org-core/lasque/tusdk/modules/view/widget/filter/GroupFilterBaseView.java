package org.lasque.tusdk.modules.view.widget.filter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import java.util.List;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.ViewSize;
import org.lasque.tusdk.core.utils.hardware.TuSdkCorePatch;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class GroupFilterBaseView
  extends TuSdkRelativeLayout
{
  private boolean a;
  private boolean b;
  private int c;
  private int d;
  private int e;
  private int f;
  private boolean g = true;
  private boolean h;
  private Class<?> i;
  private boolean j;
  private boolean k;
  private GroupFilterBarInterface.GroupFilterBarDelegate l = new GroupFilterBarInterface.GroupFilterBarDelegate()
  {
    public boolean onGroupFilterSelected(GroupFilterBarInterface paramAnonymousGroupFilterBarInterface, GroupFilterItemViewInterface paramAnonymousGroupFilterItemViewInterface, GroupFilterItem paramAnonymousGroupFilterItem)
    {
      return GroupFilterBaseView.this.onDispatchGroupFilterSelected(paramAnonymousGroupFilterBarInterface, paramAnonymousGroupFilterItemViewInterface, paramAnonymousGroupFilterItem);
    }
  };
  
  public GroupFilterBaseView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public GroupFilterBaseView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public GroupFilterBaseView(Context paramContext)
  {
    super(paramContext);
  }
  
  public boolean isStateHidden()
  {
    return this.a;
  }
  
  public void setStateHidden(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
  
  public int getGroupFilterCellWidth()
  {
    return this.c;
  }
  
  public void setGroupFilterCellWidth(int paramInt)
  {
    this.c = paramInt;
    if (getGroupFilterBar() != null) {
      ((GroupFilterBarInterface)getGroupFilterBar()).setGroupFilterCellWidth(getGroupFilterCellWidth());
    }
  }
  
  public int getGroupTableCellLayoutId()
  {
    return this.d;
  }
  
  public void setGroupTableCellLayoutId(int paramInt)
  {
    this.d = paramInt;
    if (getGroupFilterBar() != null) {
      ((GroupFilterBarInterface)getGroupFilterBar()).setGroupTableCellLayoutId(getGroupTableCellLayoutId());
    }
  }
  
  public int getFilterTableCellLayoutId()
  {
    return this.e;
  }
  
  public void setFilterTableCellLayoutId(int paramInt)
  {
    this.e = paramInt;
    if (getGroupFilterBar() != null) {
      ((GroupFilterBarInterface)getGroupFilterBar()).setFilterTableCellLayoutId(getFilterTableCellLayoutId());
    }
  }
  
  public int getFilterBarHeight()
  {
    return this.f;
  }
  
  public void setFilterBarHeight(int paramInt)
  {
    this.f = paramInt;
    if ((getFilterBarHeight() > 0) && (getGroupFilterBar() != null)) {
      ((GroupFilterBarInterface)getGroupFilterBar()).setHeight(getFilterBarHeight());
    }
  }
  
  public void setThumbImage(Bitmap paramBitmap)
  {
    if (getGroupFilterBar() == null) {
      return;
    }
    if (paramBitmap != null)
    {
      ViewSize localViewSize = ViewSize.create(getGroupFilterBar());
      if (this.f > 0) {
        localViewSize.height = this.f;
      }
      if (this.c > 0) {
        localViewSize.width = this.c;
      } else {
        localViewSize.width = localViewSize.height;
      }
      paramBitmap = BitmapHelper.imageResize(paramBitmap, localViewSize);
    }
    ((GroupFilterBarInterface)getGroupFilterBar()).setThumbImage(paramBitmap);
  }
  
  public void setRenderFilterThumb(boolean paramBoolean)
  {
    if (TuSdkCorePatch.applyThumbRenderPatch()) {
      paramBoolean = false;
    }
    if (getGroupFilterBar() != null) {
      ((GroupFilterBarInterface)getGroupFilterBar()).setRenderFilterThumb(paramBoolean);
    }
  }
  
  public boolean isEnableFilterConfig()
  {
    return this.b;
  }
  
  public void setEnableFilterConfig(boolean paramBoolean)
  {
    this.b = paramBoolean;
    if (getGroupFilterBar() == null) {
      return;
    }
    ((GroupFilterBarInterface)getGroupFilterBar()).setEnableFilterConfig(paramBoolean);
  }
  
  public boolean isEnableNormalFilter()
  {
    return this.g;
  }
  
  public void setEnableNormalFilter(boolean paramBoolean)
  {
    this.g = paramBoolean;
    if (getGroupFilterBar() == null) {
      return;
    }
    ((GroupFilterBarInterface)getGroupFilterBar()).setEnableNormalFilter(paramBoolean);
  }
  
  public boolean isEnableOnlineFilter()
  {
    return this.h;
  }
  
  public void setEnableOnlineFilter(boolean paramBoolean)
  {
    this.h = paramBoolean;
    if (getGroupFilterBar() == null) {
      return;
    }
    ((GroupFilterBarInterface)getGroupFilterBar()).setEnableOnlineFilter(paramBoolean);
  }
  
  public Class<?> getOnlineFragmentClazz()
  {
    return this.i;
  }
  
  public void setOnlineFragmentClazz(Class<?> paramClass)
  {
    this.i = paramClass;
    if (getGroupFilterBar() == null) {
      return;
    }
    ((GroupFilterBarInterface)getGroupFilterBar()).setOnlineFragmentClazz(paramClass);
  }
  
  public boolean isEnableHistory()
  {
    return this.j;
  }
  
  public void setEnableHistory(boolean paramBoolean)
  {
    this.j = paramBoolean;
    if (getGroupFilterBar() == null) {
      return;
    }
    ((GroupFilterBarInterface)getGroupFilterBar()).setEnableHistory(paramBoolean);
  }
  
  public boolean isDisplaySubtitles()
  {
    return this.k;
  }
  
  public void setDisplaySubtitles(boolean paramBoolean)
  {
    this.k = paramBoolean;
  }
  
  public void setActivity(Activity paramActivity)
  {
    if (getGroupFilterBar() == null) {
      return;
    }
    ((GroupFilterBarInterface)getGroupFilterBar()).setActivity(paramActivity);
  }
  
  public abstract <T extends View,  extends FilterSubtitleViewInterface> T getFilterTitleView();
  
  public abstract <T extends View,  extends GroupFilterBarInterface> T getGroupFilterBar();
  
  protected void configGroupFilterBar(GroupFilterBarInterface paramGroupFilterBarInterface, GroupFilterItemViewInterface.GroupFilterAction paramGroupFilterAction)
  {
    if (paramGroupFilterBarInterface == null) {
      return;
    }
    paramGroupFilterBarInterface.setAction(paramGroupFilterAction);
    paramGroupFilterBarInterface.setDelegate(this.l);
  }
  
  public void setFilterGroup(List<String> paramList)
  {
    if (getGroupFilterBar() != null) {
      ((GroupFilterBarInterface)getGroupFilterBar()).setFilterGroup(paramList);
    }
  }
  
  public void setAutoSelectGroupDefaultFilter(boolean paramBoolean)
  {
    if (getGroupFilterBar() != null) {
      ((GroupFilterBarInterface)getGroupFilterBar()).setAutoSelectGroupDefaultFilter(paramBoolean);
    }
  }
  
  public void setSaveLastFilter(boolean paramBoolean)
  {
    if (getGroupFilterBar() != null) {
      ((GroupFilterBarInterface)getGroupFilterBar()).setSaveLastFilter(paramBoolean);
    }
  }
  
  public void loadView()
  {
    super.loadView();
    getGroupFilterBar();
    showViewIn(getFilterTitleView(), false);
  }
  
  protected abstract boolean onDispatchGroupFilterSelected(GroupFilterBarInterface paramGroupFilterBarInterface, GroupFilterItemViewInterface paramGroupFilterItemViewInterface, GroupFilterItem paramGroupFilterItem);
  
  protected boolean notifyTitle(GroupFilterItemViewInterface paramGroupFilterItemViewInterface, GroupFilterItem paramGroupFilterItem)
  {
    if ((paramGroupFilterItemViewInterface == null) || (!paramGroupFilterItemViewInterface.isSelected()))
    {
      notifyTitle(paramGroupFilterItem);
      return true;
    }
    return false;
  }
  
  protected void notifyTitle(GroupFilterItem paramGroupFilterItem)
  {
    if ((getFilterTitleView() == null) || (!isDisplaySubtitles())) {
      return;
    }
    if (paramGroupFilterItem.type == GroupFilterItem.GroupFilterItemType.TypeFilter) {
      ((FilterSubtitleViewInterface)getFilterTitleView()).setFilter(paramGroupFilterItem.filterOption);
    }
  }
  
  public void loadFilters()
  {
    if (getGroupFilterBar() != null) {
      ((GroupFilterBarInterface)getGroupFilterBar()).loadFilters();
    }
  }
  
  public abstract void setDefaultShowState(boolean paramBoolean);
  
  public void exitRemoveState()
  {
    if (getGroupFilterBar() == null) {
      return;
    }
    ((GroupFilterBarInterface)getGroupFilterBar()).exitRemoveState();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\filter\GroupFilterBaseView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */