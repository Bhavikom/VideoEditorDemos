package org.lasque.tusdk.impl.view.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.core.view.TuSdkViewInterface;
import org.lasque.tusdk.core.view.listview.TuSdkCellLinearLayout;
import org.lasque.tusdk.core.view.listview.TuSdkCellViewInterface;

public class TuListGridCellView<T, V extends View,  extends TuSdkCellViewInterface<T>>
  extends TuSdkCellLinearLayout<List<T>>
{
  private ArrayList<V> a = new ArrayList();
  private TuListGridCellViewDelegate<T, V> b;
  private View.OnClickListener c = new TuSdkViewHelper.OnSafeClickListener()
  {
    public void onSafeClick(View paramAnonymousView)
    {
      if ((TuListGridCellView.a(TuListGridCellView.this) != null) && ((paramAnonymousView instanceof TuSdkCellViewInterface)))
      {
        View localView = paramAnonymousView;
        TuListGridCellView.a(TuListGridCellView.this).onGridItemClick(localView, ((TuSdkCellViewInterface)localView).getModel());
      }
    }
  };
  
  public TuListGridCellView(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuListGridCellView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuListGridCellView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuListGridCellViewDelegate<T, V> getGridDelegate()
  {
    return this.b;
  }
  
  public void setGridDelegate(TuListGridCellViewDelegate<T, V> paramTuListGridCellViewDelegate)
  {
    this.b = paramTuListGridCellViewDelegate;
  }
  
  protected void onLayouted()
  {
    int i = 0;
    int j = getChildCount();
    while (i < j)
    {
      View localView = getChildAt(i);
      if ((localView instanceof TuSdkCellViewInterface)) {
        a(localView);
      }
      i++;
    }
    setHeight(ContextUtils.getScreenSize(getContext()).width / this.a.size());
    super.onLayouted();
  }
  
  private void a(V paramV)
  {
    ((TuSdkViewInterface)paramV).loadView();
    paramV.setOnClickListener(this.c);
    paramV.setVisibility(4);
    this.a.add(paramV);
  }
  
  protected void bindModel()
  {
    List localList = (List)getModel();
    if (localList == null) {
      return;
    }
    int i = 0;
    int j = this.a.size();
    int k = localList.size();
    while (i < j)
    {
      View localView = (View)this.a.get(i);
      if (i < k)
      {
        ((TuSdkCellViewInterface)localView).setModel(localList.get(i));
        showViewIn(localView, true);
      }
      i++;
    }
  }
  
  public void viewNeedRest()
  {
    super.viewNeedRest();
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      View localView = (View)localIterator.next();
      ((TuSdkViewInterface)localView).viewNeedRest();
      showViewIn(localView, false);
    }
  }
  
  public void viewWillDestory()
  {
    viewNeedRest();
    super.viewWillDestory();
  }
  
  public static abstract interface TuListGridCellViewDelegate<T, V extends View,  extends TuSdkCellViewInterface<T>>
  {
    public abstract void onGridItemClick(V paramV, T paramT);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\impl\view\widget\listview\TuListGridCellView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */