package org.lasque.tusdk.core.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import org.lasque.tusdk.core.view.TuSdkViewHelper;

public abstract class TuSdkGroupListView<T, V extends View, M, N extends View>
  extends TuSdkListView
{
  private GroupListViewItemClickListener<T, V> a;
  private GroupListViewHeaderClickListener<M, N> b;
  private TuSdkGroupListView<T, V, M, N>.GroupListViewDeleagte c;
  private int d;
  private int e;
  
  public TuSdkGroupListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkGroupListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkGroupListView(Context paramContext)
  {
    super(paramContext);
  }
  
  public void setCellLayoutId(int paramInt)
  {
    this.d = paramInt;
  }
  
  public int getCellLayoutId()
  {
    return this.d;
  }
  
  public int getHeaderLayoutId()
  {
    return this.e;
  }
  
  public void setHeaderLayoutId(int paramInt)
  {
    this.e = paramInt;
  }
  
  public void setItemClickListener(GroupListViewItemClickListener<T, V> paramGroupListViewItemClickListener)
  {
    this.a = paramGroupListViewItemClickListener;
  }
  
  public void setHeaderClickListener(GroupListViewHeaderClickListener<M, N> paramGroupListViewHeaderClickListener)
  {
    this.b = paramGroupListViewHeaderClickListener;
  }
  
  public void setDataSource(TuSdkIndexPath.TuSdkDataSource paramTuSdkDataSource)
  {
    super.setDataSource(paramTuSdkDataSource);
    if (this.c == null)
    {
      this.c = new GroupListViewDeleagte(null);
      setDeleagte(this.c);
    }
    reloadData();
  }
  
  protected abstract void onGroupListViewCreated(V paramV, TuSdkIndexPath paramTuSdkIndexPath);
  
  protected abstract void onGroupListHeaderCreated(N paramN, TuSdkIndexPath paramTuSdkIndexPath);
  
  private class GroupListViewDeleagte
    implements TuSdkListView.TuSdkListViewDeleagte
  {
    private GroupListViewDeleagte() {}
    
    public void onListViewItemClick(TuSdkListView paramTuSdkListView, View paramView, TuSdkIndexPath paramTuSdkIndexPath)
    {
      if (TuSdkViewHelper.isFastDoubleClick(1000L)) {
        return;
      }
      if (paramTuSdkIndexPath == null) {
        return;
      }
      if ((paramTuSdkIndexPath.viewType == 0) && (TuSdkGroupListView.a(TuSdkGroupListView.this) != null)) {
        TuSdkGroupListView.a(TuSdkGroupListView.this).onGroupItemClick(TuSdkGroupListView.this.getDataSource().getItem(paramTuSdkIndexPath), paramView, paramTuSdkIndexPath);
      } else if ((paramTuSdkIndexPath.viewType == 1) && (TuSdkGroupListView.b(TuSdkGroupListView.this) != null)) {
        TuSdkGroupListView.b(TuSdkGroupListView.this).onGroupHeaderClick(TuSdkGroupListView.this.getDataSource().getItem(paramTuSdkIndexPath), paramView, paramTuSdkIndexPath);
      }
    }
    
    public View onListViewItemCreate(TuSdkListView paramTuSdkListView, TuSdkIndexPath paramTuSdkIndexPath, ViewGroup paramViewGroup)
    {
      View localView = null;
      if (paramTuSdkIndexPath.viewType == 0)
      {
        localView = TuSdkViewHelper.buildView(TuSdkGroupListView.this.getContext(), TuSdkGroupListView.this.getCellLayoutId(), paramViewGroup);
        TuSdkGroupListView.this.onGroupListViewCreated(localView, paramTuSdkIndexPath);
      }
      else if (paramTuSdkIndexPath.viewType == 1)
      {
        localView = TuSdkViewHelper.buildView(TuSdkGroupListView.this.getContext(), TuSdkGroupListView.this.getHeaderLayoutId(), paramViewGroup);
        TuSdkGroupListView.this.onGroupListHeaderCreated(localView, paramTuSdkIndexPath);
      }
      return localView;
    }
  }
  
  public static abstract interface GroupListViewHeaderClickListener<M, N>
  {
    public abstract void onGroupHeaderClick(M paramM, N paramN, TuSdkIndexPath paramTuSdkIndexPath);
  }
  
  public static abstract interface GroupListViewItemClickListener<T, V>
  {
    public abstract void onGroupItemClick(T paramT, V paramV, TuSdkIndexPath paramTuSdkIndexPath);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\listview\TuSdkGroupListView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */