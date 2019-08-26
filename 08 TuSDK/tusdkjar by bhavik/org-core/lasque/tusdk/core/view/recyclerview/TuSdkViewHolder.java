package org.lasque.tusdk.core.view.recyclerview;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewInterface;
import org.lasque.tusdk.core.view.listview.TuSdkCellViewInterface;
import org.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;

public class TuSdkViewHolder<T>
  extends RecyclerView.ViewHolder
  implements TuSdkViewInterface
{
  private View.OnClickListener a = new ViewHolderClickListener();
  private TuSdkViewHolderItemClickListener<T> b;
  
  public static <T> TuSdkViewHolder<T> create(ViewGroup paramViewGroup, int paramInt)
  {
    View localView = TuSdkViewHelper.buildView(paramViewGroup.getContext(), paramInt, paramViewGroup);
    return create(localView);
  }
  
  public static <T> TuSdkViewHolder<T> create(View paramView)
  {
    TuSdkViewHolder localTuSdkViewHolder = new TuSdkViewHolder(paramView);
    return localTuSdkViewHolder;
  }
  
  public TuSdkViewHolderItemClickListener<T> getItemClickListener()
  {
    return this.b;
  }
  
  public void setItemClickListener(TuSdkViewHolderItemClickListener<T> paramTuSdkViewHolderItemClickListener)
  {
    this.b = paramTuSdkViewHolderItemClickListener;
    if (this.b != null) {
      this.itemView.setOnClickListener(this.a);
    } else {
      this.itemView.setOnClickListener(null);
    }
  }
  
  public TuSdkViewHolder(View paramView)
  {
    super(paramView);
    viewNeedRest();
  }
  
  public void setModel(T paramT, int paramInt)
  {
    viewNeedRest();
    this.itemView.setTag(Integer.valueOf(paramInt));
    if ((this.itemView instanceof TuSdkCellViewInterface)) {
      ((TuSdkCellViewInterface)this.itemView).setModel(paramT);
    }
  }
  
  public T getModel()
  {
    if ((this.itemView instanceof TuSdkCellViewInterface)) {
      return (T)((TuSdkCellViewInterface)this.itemView).getModel();
    }
    return null;
  }
  
  public void loadView()
  {
    if ((this.itemView instanceof TuSdkViewInterface)) {
      ((TuSdkViewInterface)this.itemView).loadView();
    }
  }
  
  public void viewDidLoad()
  {
    if ((this.itemView instanceof TuSdkViewInterface)) {
      ((TuSdkViewInterface)this.itemView).viewDidLoad();
    }
  }
  
  public void viewNeedRest()
  {
    if ((this.itemView instanceof TuSdkViewInterface)) {
      ((TuSdkViewInterface)this.itemView).viewNeedRest();
    }
  }
  
  public void viewWillDestory()
  {
    if ((this.itemView instanceof TuSdkViewInterface)) {
      ((TuSdkViewInterface)this.itemView).viewWillDestory();
    }
  }
  
  protected void onViewHolderItemClick(View paramView)
  {
    if (this.b == null) {
      return;
    }
    this.b.onViewHolderItemClick(this);
  }
  
  public void setSelectedPosition(int paramInt)
  {
    if (paramInt < 0) {
      return;
    }
    if ((this.itemView instanceof TuSdkListSelectableCellViewInterface))
    {
      int i = paramInt == getPosition() ? 1 : 0;
      if (i != 0) {
        ((TuSdkListSelectableCellViewInterface)this.itemView).onCellSelected(paramInt);
      } else {
        ((TuSdkListSelectableCellViewInterface)this.itemView).onCellDeselected();
      }
    }
  }
  
  protected class ViewHolderClickListener
    implements View.OnClickListener
  {
    protected ViewHolderClickListener() {}
    
    public void onClick(View paramView)
    {
      TuSdkViewHolder.this.onViewHolderItemClick(paramView);
    }
  }
  
  public static abstract interface TuSdkViewHolderItemClickListener<T>
  {
    public abstract void onViewHolderItemClick(TuSdkViewHolder<T> paramTuSdkViewHolder);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\recyclerview\TuSdkViewHolder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */