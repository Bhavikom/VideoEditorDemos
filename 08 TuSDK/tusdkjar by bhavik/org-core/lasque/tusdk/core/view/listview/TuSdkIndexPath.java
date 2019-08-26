package org.lasque.tusdk.core.view.listview;

import android.view.View;
import java.util.List;

public class TuSdkIndexPath
{
  public int row;
  public int section;
  public int viewType;
  public boolean isHeader;
  
  public TuSdkIndexPath() {}
  
  public TuSdkIndexPath(int paramInt1, int paramInt2)
  {
    this(paramInt1, paramInt2, 0);
  }
  
  public TuSdkIndexPath(int paramInt1, int paramInt2, int paramInt3)
  {
    this.section = paramInt1;
    this.viewType = paramInt3;
    this.row = paramInt2;
    if (this.row == -1) {
      this.isHeader = true;
    }
  }
  
  public static abstract interface TuSdkDataSource
  {
    public abstract List<TuSdkIndexPath> getIndexPaths();
    
    public abstract TuSdkIndexPath getIndexPath(int paramInt);
    
    public abstract int viewTypes();
    
    public abstract int sectionCount();
    
    public abstract int rowCount(int paramInt);
    
    public abstract int count();
    
    public abstract void onViewBinded(TuSdkIndexPath paramTuSdkIndexPath, View paramView);
    
    public abstract Object getItem(TuSdkIndexPath paramTuSdkIndexPath);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\listview\TuSdkIndexPath.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */