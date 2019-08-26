package org.lasque.tusdk.modules.view.widget.smudge;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import java.util.List;
import org.lasque.tusdk.core.view.recyclerview.TuSdkTableView.TuSdkTableViewItemClickDelegate;

public abstract interface BrushTableViewInterface
{
  public abstract void setCellLayoutId(int paramInt);
  
  public abstract void setCellWidth(int paramInt);
  
  public abstract void setItemClickDelegate(TuSdkTableView.TuSdkTableViewItemClickDelegate<BrushData, BrushBarItemCellBase> paramTuSdkTableViewItemClickDelegate);
  
  public abstract void setAction(BrushAction paramBrushAction);
  
  public abstract void reloadData();
  
  public abstract void setModeList(List<BrushData> paramList);
  
  public abstract List<BrushData> getModeList();
  
  public abstract void setSelectedPosition(int paramInt);
  
  public abstract void setSelectedPosition(int paramInt, boolean paramBoolean);
  
  public abstract int getSelectedPosition();
  
  public abstract void changeSelectedPosition(int paramInt);
  
  public abstract void scrollToPosition(int paramInt);
  
  public abstract void scrollToPositionWithOffset(int paramInt1, int paramInt2);
  
  public abstract void smoothScrollByCenter(View paramView);
  
  public abstract RecyclerView.Adapter getAdapter();
  
  public static enum BrushAction
  {
    private BrushAction() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\smudge\BrushTableViewInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */