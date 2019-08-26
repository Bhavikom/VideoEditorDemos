package org.lasque.tusdk.core.view.recyclerview;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.ViewGroup;
import java.util.List;

public class TuSdkAdapter<T>
  extends RecyclerView.Adapter<TuSdkViewHolder<T>>
{
  private int a;
  private List<T> b;
  private int c = -1;
  private TuSdkViewHolder.TuSdkViewHolderItemClickListener<T> d;
  
  public int getViewLayoutId()
  {
    return this.a;
  }
  
  public void setViewLayoutId(int paramInt)
  {
    this.a = paramInt;
  }
  
  public List<T> getModeList()
  {
    return this.b;
  }
  
  public void setModeList(List<T> paramList)
  {
    this.b = paramList;
  }
  
  public TuSdkViewHolder.TuSdkViewHolderItemClickListener<T> getItemClickListener()
  {
    return this.d;
  }
  
  public void setItemClickListener(TuSdkViewHolder.TuSdkViewHolderItemClickListener<T> paramTuSdkViewHolderItemClickListener)
  {
    this.d = paramTuSdkViewHolderItemClickListener;
  }
  
  public TuSdkAdapter()
  {
    this(0);
  }
  
  public TuSdkAdapter(int paramInt)
  {
    this(paramInt, null);
  }
  
  public TuSdkAdapter(int paramInt, List<T> paramList)
  {
    this.a = paramInt;
    this.b = paramList;
  }
  
  public T getItem(int paramInt)
  {
    if ((this.b == null) || (paramInt < 0) || (paramInt >= this.b.size())) {
      return null;
    }
    return (T)this.b.get(paramInt);
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public int getItemPosition(T paramT)
  {
    List localList = getModeList();
    if ((localList != null) && (localList.size() > 0)) {
      for (int i = 0; i < localList.size(); i++) {
        if (localList.get(i).equals(paramT)) {
          return i;
        }
      }
    }
    return -1;
  }
  
  public int getItemCount()
  {
    if (this.b == null) {
      return 0;
    }
    return this.b.size();
  }
  
  public TuSdkViewHolder<T> onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
  {
    TuSdkViewHolder localTuSdkViewHolder = TuSdkViewHolder.create(paramViewGroup, this.a);
    localTuSdkViewHolder.setItemClickListener(this.d);
    return localTuSdkViewHolder;
  }
  
  public void onBindViewHolder(TuSdkViewHolder<T> paramTuSdkViewHolder, int paramInt)
  {
    paramTuSdkViewHolder.setModel(getItem(paramInt), paramInt);
    paramTuSdkViewHolder.setSelectedPosition(this.c);
  }
  
  public void onViewAttachedToWindow(TuSdkViewHolder<T> paramTuSdkViewHolder)
  {
    super.onViewAttachedToWindow(paramTuSdkViewHolder);
    paramTuSdkViewHolder.setSelectedPosition(this.c);
  }
  
  public void onViewRecycled(TuSdkViewHolder<T> paramTuSdkViewHolder)
  {
    super.onViewRecycled(paramTuSdkViewHolder);
    paramTuSdkViewHolder.viewWillDestory();
  }
  
  public int getSelectedPosition()
  {
    return this.c;
  }
  
  public void setSelectedPosition(int paramInt)
  {
    this.c = paramInt;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\recyclerview\TuSdkAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */