package org.lasque.tusdk.core.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.view.TuSdkViewHelper;

public abstract class TuSdkArrayListView<T, V extends View>
  extends TuSdkListView
{
  private int a;
  private List<T> b;
  private TuSdkArrayListView<T, V>.ArrayListDataSource c;
  private TuSdkArrayListView<T, V>.ArrayListViewDeleagte d;
  private ArrayListViewItemClickListener<T, V> e;
  
  public TuSdkArrayListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkArrayListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkArrayListView(Context paramContext)
  {
    super(paramContext);
  }
  
  public void setCellLayoutId(int paramInt)
  {
    this.a = paramInt;
  }
  
  public int getCellLayoutId()
  {
    return this.a;
  }
  
  public void setModeList(List<T> paramList)
  {
    this.b = paramList;
    if (this.c == null)
    {
      this.c = new ArrayListDataSource(null);
      setDataSource(this.c);
    }
    if (this.d == null)
    {
      this.d = new ArrayListViewDeleagte(null);
      setDeleagte(this.d);
    }
    this.c.refreshIndexPaths(paramList);
    reloadData();
  }
  
  public List<T> getModeList()
  {
    return this.b;
  }
  
  public T getModeItem(int paramInt)
  {
    if ((this.b == null) || (this.b.size() <= paramInt)) {
      return null;
    }
    return (T)this.b.get(paramInt);
  }
  
  public void setItemClickListener(ArrayListViewItemClickListener<T, V> paramArrayListViewItemClickListener)
  {
    this.e = paramArrayListViewItemClickListener;
  }
  
  protected abstract void onArrayListViewCreated(V paramV, TuSdkIndexPath paramTuSdkIndexPath);
  
  protected abstract void onArrayListViewBinded(V paramV, TuSdkIndexPath paramTuSdkIndexPath);
  
  private class ArrayListDataSource
    implements TuSdkIndexPath.TuSdkDataSource
  {
    private List<TuSdkIndexPath> b;
    
    private ArrayListDataSource() {}
    
    public void refreshIndexPaths(List<T> paramList)
    {
      if (TuSdkArrayListView.this.getModeList() == null)
      {
        if (this.b != null) {
          this.b.clear();
        }
        return;
      }
      ArrayList localArrayList = new ArrayList(paramList.size());
      int i = 0;
      int j = TuSdkArrayListView.this.getModeList().size();
      while (i < j)
      {
        localArrayList.add(new TuSdkIndexPath(0, i));
        i++;
      }
      this.b = localArrayList;
    }
    
    public List<TuSdkIndexPath> getIndexPaths()
    {
      if (this.b == null) {
        this.b = new ArrayList(0);
      }
      return this.b;
    }
    
    public TuSdkIndexPath getIndexPath(int paramInt)
    {
      if ((paramInt < 0) || (this.b == null) || (this.b.size() <= paramInt)) {
        return null;
      }
      return (TuSdkIndexPath)this.b.get(paramInt);
    }
    
    public int viewTypes()
    {
      return 1;
    }
    
    public int sectionCount()
    {
      return 1;
    }
    
    public int rowCount(int paramInt)
    {
      return count();
    }
    
    public int count()
    {
      return getIndexPaths().size();
    }
    
    public void onViewBinded(TuSdkIndexPath paramTuSdkIndexPath, View paramView)
    {
      if ((paramView instanceof TuSdkCellViewInterface))
      {
        TuSdkArrayListView.this.onArrayListViewBinded(paramView, paramTuSdkIndexPath);
        ((TuSdkCellViewInterface)paramView).setModel(TuSdkArrayListView.this.getModeList().get(paramTuSdkIndexPath.row));
      }
    }
    
    public Object getItem(TuSdkIndexPath paramTuSdkIndexPath)
    {
      if (TuSdkArrayListView.this.getModeList() == null) {
        return null;
      }
      return TuSdkArrayListView.this.getModeList().get(paramTuSdkIndexPath.row);
    }
  }
  
  private class ArrayListViewDeleagte
    implements TuSdkListView.TuSdkListViewDeleagte
  {
    private ArrayListViewDeleagte() {}
    
    public void onListViewItemClick(TuSdkListView paramTuSdkListView, View paramView, TuSdkIndexPath paramTuSdkIndexPath)
    {
      if ((paramTuSdkIndexPath == null) || (TuSdkArrayListView.a(TuSdkArrayListView.this) == null) || (TuSdkViewHelper.isFastDoubleClick(500L))) {
        return;
      }
      TuSdkArrayListView.a(TuSdkArrayListView.this).onArrayListViewItemClick(TuSdkArrayListView.this.getModeItem(paramTuSdkIndexPath.row), paramView, paramTuSdkIndexPath);
    }
    
    public View onListViewItemCreate(TuSdkListView paramTuSdkListView, TuSdkIndexPath paramTuSdkIndexPath, ViewGroup paramViewGroup)
    {
      View localView = TuSdkViewHelper.buildView(TuSdkArrayListView.this.getContext(), TuSdkArrayListView.this.getCellLayoutId(), paramViewGroup);
      TuSdkArrayListView.this.onArrayListViewCreated(localView, paramTuSdkIndexPath);
      return localView;
    }
  }
  
  public static abstract interface ArrayListViewItemClickListener<T, V>
  {
    public abstract void onArrayListViewItemClick(T paramT, V paramV, TuSdkIndexPath paramTuSdkIndexPath);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\listview\TuSdkArrayListView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */