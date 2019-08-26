package org.lasque.tusdk.core.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.listview.TuSdkCellViewInterface;

public abstract class TuSdkTableView<T, V extends View>
  extends TuSdkRecyclerView
{
  private TuSdkTableViewItemClickDelegate<T, V> a;
  private TuSdkAdapter<T> b;
  private TuSdkLinearLayoutManager c;
  private int d;
  private List<T> e;
  private int f = 0;
  private boolean g;
  private int h = -1;
  protected TuSdkViewHolder.TuSdkViewHolderItemClickListener<T> mViewHolderItemClickListener = new TuSdkViewHolder.TuSdkViewHolderItemClickListener()
  {
    public void onViewHolderItemClick(TuSdkViewHolder<T> paramAnonymousTuSdkViewHolder)
    {
      if (TuSdkTableView.a(TuSdkTableView.this) == null) {
        return;
      }
      if ((paramAnonymousTuSdkViewHolder.itemView instanceof TuSdkCellViewInterface)) {
        TuSdkTableView.a(TuSdkTableView.this).onTableViewItemClick(paramAnonymousTuSdkViewHolder.getModel(), paramAnonymousTuSdkViewHolder.itemView, paramAnonymousTuSdkViewHolder.getPosition());
      }
    }
  };
  
  public TuSdkTableView(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuSdkTableView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkTableView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void setItemClickDelegate(TuSdkTableViewItemClickDelegate<T, V> paramTuSdkTableViewItemClickDelegate)
  {
    this.a = paramTuSdkTableViewItemClickDelegate;
    if (this.b == null) {
      return;
    }
    if (paramTuSdkTableViewItemClickDelegate == null) {
      this.b.setItemClickListener(null);
    } else {
      this.b.setItemClickListener(this.mViewHolderItemClickListener);
    }
  }
  
  public TuSdkTableViewItemClickDelegate<T, V> getItemClickDelegate()
  {
    return this.a;
  }
  
  public int getCellLayoutId()
  {
    return this.d;
  }
  
  public void setCellLayoutId(int paramInt)
  {
    this.d = paramInt;
    if ((paramInt > 0) && (this.b != null)) {
      this.b.setViewLayoutId(getCellLayoutId());
    }
  }
  
  public List<T> getModeList()
  {
    return this.e;
  }
  
  public void setModeList(List<T> paramList)
  {
    this.e = paramList;
    if (this.b != null) {
      this.b.setModeList(this.e);
    }
  }
  
  public TuSdkAdapter<T> getSdkAdapter()
  {
    if (this.b == null)
    {
      this.b = new TableViewAdapter(getCellLayoutId(), this.e);
      this.b.setSelectedPosition(this.h);
      if (this.a != null) {
        this.b.setItemClickListener(this.mViewHolderItemClickListener);
      }
    }
    return this.b;
  }
  
  public void setAdapter(RecyclerView.Adapter paramAdapter)
  {
    if ((paramAdapter instanceof TuSdkAdapter)) {
      this.b = ((TuSdkAdapter)paramAdapter);
    }
    super.setAdapter(paramAdapter);
  }
  
  public TuSdkLinearLayoutManager getSdkLayoutManager()
  {
    if (this.c == null) {
      this.c = new TuSdkLinearLayoutManager(getContext(), this.f, this.g);
    }
    return this.c;
  }
  
  public void setLayoutManager(RecyclerView.LayoutManager paramLayoutManager)
  {
    if ((paramLayoutManager instanceof TuSdkLinearLayoutManager)) {
      this.c = ((TuSdkLinearLayoutManager)paramLayoutManager);
    }
    super.setLayoutManager(paramLayoutManager);
  }
  
  public int getOrientation()
  {
    return this.f;
  }
  
  public void setOrientation(int paramInt)
  {
    this.f = paramInt;
    if (this.c != null) {
      this.c.setOrientation(this.f);
    }
  }
  
  public void scrollToPositionWithOffset(int paramInt1, int paramInt2)
  {
    if (this.c != null) {
      this.c.scrollToPositionWithOffset(paramInt1, paramInt2);
    }
  }
  
  public void smoothScrollByCenter(View paramView)
  {
    if (paramView == null) {
      return;
    }
    int i;
    if (this.f == 0)
    {
      i = TuSdkViewHelper.locationInWindowLeft(paramView) - TuSdkViewHelper.locationInWindowLeft(this);
      i -= (getWidth() - paramView.getWidth()) / 2;
      smoothScrollBy(i, 0);
    }
    else if (this.f == 1)
    {
      i = TuSdkViewHelper.locationInWindowTop(paramView) - TuSdkViewHelper.locationInWindowTop(this);
      i -= (getHeight() - paramView.getHeight()) / 2;
      smoothScrollBy(0, i);
    }
  }
  
  public int getSelectedPosition()
  {
    return this.h;
  }
  
  public void setSelectedPosition(int paramInt)
  {
    setSelectedPosition(paramInt, true);
  }
  
  public void setSelectedPosition(int paramInt, boolean paramBoolean)
  {
    this.h = paramInt;
    if (this.b == null) {
      return;
    }
    this.b.setSelectedPosition(paramInt);
    if (paramBoolean) {
      this.b.notifyDataSetChanged();
    }
  }
  
  public void changeSelectedPosition(int paramInt)
  {
    if ((this.b == null) || (this.c == null)) {
      return;
    }
    int i = this.b.getSelectedPosition();
    if (i == paramInt) {
      return;
    }
    this.h = paramInt;
    this.b.setSelectedPosition(paramInt);
    this.c.selectedPosition(i, false);
    this.c.selectedPosition(paramInt, true);
  }
  
  public boolean isReverseLayout()
  {
    return this.g;
  }
  
  public void setReverseLayout(boolean paramBoolean)
  {
    this.g = paramBoolean;
    if (this.c != null) {
      this.c.setReverseLayout(this.g);
    }
  }
  
  private void a()
  {
    if (getLayoutManager() == null) {
      setLayoutManager(getSdkLayoutManager());
    }
    if (getAdapter() == null) {
      setAdapter(getSdkAdapter());
    }
  }
  
  public void reloadData()
  {
    if (getAdapter() == null) {
      a();
    } else {
      getAdapter().notifyDataSetChanged();
    }
  }
  
  protected abstract void onViewCreated(V paramV, ViewGroup paramViewGroup, int paramInt);
  
  protected abstract void onViewBinded(V paramV, int paramInt);
  
  protected class TableViewAdapter
    extends TuSdkAdapter<T>
  {
    public TableViewAdapter() {}
    
    public TableViewAdapter(List<T> paramList)
    {
      super(localList);
    }
    
    public TableViewAdapter(int paramInt)
    {
      super();
    }
    
    public TuSdkViewHolder<T> onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
    {
      TuSdkViewHolder localTuSdkViewHolder = super.onCreateViewHolder(paramViewGroup, paramInt);
      if ((localTuSdkViewHolder.itemView instanceof TuSdkCellViewInterface)) {
        TuSdkTableView.this.onViewCreated(localTuSdkViewHolder.itemView, paramViewGroup, paramInt);
      }
      return localTuSdkViewHolder;
    }
    
    public void onBindViewHolder(TuSdkViewHolder<T> paramTuSdkViewHolder, int paramInt)
    {
      super.onBindViewHolder(paramTuSdkViewHolder, paramInt);
      if ((paramTuSdkViewHolder.itemView instanceof TuSdkCellViewInterface)) {
        TuSdkTableView.this.onViewBinded(paramTuSdkViewHolder.itemView, paramInt);
      }
    }
  }
  
  public static abstract interface TuSdkTableViewItemClickDelegate<T, V extends View>
  {
    public abstract void onTableViewItemClick(T paramT, V paramV, int paramInt);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\recyclerview\TuSdkTableView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */