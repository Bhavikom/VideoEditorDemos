package org.lasque.tusdk.core.view.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import java.util.List;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewInterface;

public class TuSdkListView
  extends TuSdkRefreshListView
  implements TuSdkListViewFlingAction.TuSdkListViewFlingActionDelegate
{
  private TuSdkListViewFlingAction a;
  private View b;
  private boolean c;
  private TuSdkListSelectableCellViewInterface d;
  private TuSdkListViewItemClickListener e;
  private String f;
  private TuSdkListTotalFootView g;
  private int h;
  private boolean i;
  private TuSdkListViewAdapter j;
  private TuSdkIndexPath.TuSdkDataSource k;
  private TuSdkListViewDeleagte l;
  
  public TuSdkListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuSdkListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuSdkListView(Context paramContext)
  {
    super(paramContext);
  }
  
  public TuSdkListViewFlingAction getFlingAction()
  {
    return this.a;
  }
  
  public void setFlingAction(TuSdkListViewFlingAction paramTuSdkListViewFlingAction)
  {
    this.a = paramTuSdkListViewFlingAction;
  }
  
  public void enableFlingAction()
  {
    if (this.a != null) {
      return;
    }
    this.a = new TuSdkListViewFlingAction(getContext());
    this.a.setDelegate(this);
  }
  
  public void resetFlingItem()
  {
    if (this.a != null) {
      this.a.resetDownView();
    }
  }
  
  @SuppressLint({"ClickableViewAccessibility"})
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.a != null)
    {
      View localView = null;
      if (paramMotionEvent.getAction() == 0) {
        localView = listViewAt(paramMotionEvent);
      }
      if (this.a.onTouchEvent(paramMotionEvent, localView)) {
        return true;
      }
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  @SuppressLint({"Recycle"})
  public void onFlingActionCancelItemClick(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent == null) {
      return;
    }
    MotionEvent localMotionEvent = MotionEvent.obtain(paramMotionEvent);
    localMotionEvent.setAction(0x3 | paramMotionEvent.getActionIndex() << 8);
    super.onTouchEvent(localMotionEvent);
  }
  
  public View getEmptyView()
  {
    return this.b;
  }
  
  public void setEmptyView(View paramView)
  {
    a();
    this.b = paramView;
  }
  
  public void setEmptyView(int paramInt)
  {
    if (paramInt == 0) {
      return;
    }
    setEmptyView(TuSdkViewHelper.buildView(getContext(), paramInt, getTopHolderView()));
  }
  
  private void a()
  {
    c();
    if (this.b == null) {
      return;
    }
    if (getTopHeight() > 0) {
      setTopHeight(getTopHeight());
    }
    getTopHolderView().removeView(this.b);
  }
  
  private void b()
  {
    if (getTopHeight() > 0) {
      setTopHeight(0);
    }
    addInTopHolderView(this.b);
    if (this.g != null) {
      if (this.i) {
        this.g.needShowFooter(false);
      } else {
        setTotalFooterViewInfo(this.g, false);
      }
    }
  }
  
  public void emptyNeedFullHeight()
  {
    TuSdkViewHelper.setViewHeight(getEmptyView(), getHeight());
  }
  
  public boolean ismDisableAutoDeselectCell()
  {
    return this.c;
  }
  
  public void setDisableAutoDeselectCell(boolean paramBoolean)
  {
    this.c = paramBoolean;
  }
  
  public TuSdkListSelectableCellViewInterface getSelectedCellView()
  {
    return this.d;
  }
  
  public void setSelectedCellView(TuSdkListSelectableCellViewInterface paramTuSdkListSelectableCellViewInterface)
  {
    this.d = paramTuSdkListSelectableCellViewInterface;
  }
  
  public void deselectCell()
  {
    if ((this.c) || (this.d == null)) {
      return;
    }
    this.d.onCellDeselected();
    this.d = null;
  }
  
  public TuSdkListViewItemClickListener getItemClickListener()
  {
    return this.e;
  }
  
  public void setItemClickListener(TuSdkListViewItemClickListener paramTuSdkListViewItemClickListener)
  {
    this.e = paramTuSdkListViewItemClickListener;
    if (this.e == null) {
      setOnItemClickListener(null);
    } else {
      setOnItemClickListener(buildOnItemClickListener());
    }
  }
  
  protected AdapterView.OnItemClickListener buildOnItemClickListener()
  {
    return new TuSdkOnItemClickListener();
  }
  
  protected void onListViewItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if ((this.e == null) || (this.k == null)) {
      return;
    }
    if ((!this.c) && ((paramView instanceof TuSdkListSelectableCellViewInterface)))
    {
      this.d = ((TuSdkListSelectableCellViewInterface)paramView);
      this.d.onCellSelected(paramInt);
    }
    TuSdkIndexPath localTuSdkIndexPath = this.k.getIndexPath(getDataIndex(paramInt));
    if (localTuSdkIndexPath == null) {
      return;
    }
    this.e.onListViewItemClick(this, paramView, localTuSdkIndexPath);
  }
  
  public String getTotalFooterFormater()
  {
    return this.f;
  }
  
  public void setTotalFooterFormater(String paramString)
  {
    this.f = paramString;
    if (this.g != null) {
      this.g.setTitleFormater(paramString);
    }
  }
  
  public TuSdkListTotalFootView getTotalFooterView()
  {
    if ((this.g == null) && (this.h != 0)) {
      this.g = ((TuSdkListTotalFootView)TuSdkViewHelper.buildView(getContext(), this.h));
    }
    return this.g;
  }
  
  public void setTotalFooterView(TuSdkListTotalFootView paramTuSdkListTotalFootView)
  {
    this.g = paramTuSdkListTotalFootView;
  }
  
  public int getTotalFooterViewId()
  {
    return this.h;
  }
  
  public void setTotalFooterViewId(int paramInt)
  {
    this.h = paramInt;
  }
  
  public boolean isAutoHiddenTotalFooterView()
  {
    return this.i;
  }
  
  public void setAutoHiddenTotalFooterView(boolean paramBoolean)
  {
    this.i = paramBoolean;
  }
  
  private void c()
  {
    if (this.g == null) {
      return;
    }
    this.g.needShowFooter(true);
    setTotalFooterViewInfo(this.g, true);
  }
  
  protected void setTotalFooterViewInfo(TuSdkListTotalFootView paramTuSdkListTotalFootView, boolean paramBoolean)
  {
    if (this.k != null) {
      this.g.setTotal(this.k.count());
    }
  }
  
  private void d()
  {
    this.g = getTotalFooterView();
    this.g.setTitleFormater(this.f);
    this.g.needShowFooter(!this.i);
    addFooterView(this.g);
  }
  
  public TuSdkListViewAdapter getSdkAdapter()
  {
    if (this.j == null) {
      this.j = buildSdkAdapter();
    }
    return this.j;
  }
  
  public void setSdkAdapter(TuSdkListViewAdapter paramTuSdkListViewAdapter)
  {
    this.j = paramTuSdkListViewAdapter;
  }
  
  protected TuSdkListViewAdapter buildSdkAdapter()
  {
    return new TuSdkListViewAdapter();
  }
  
  public void setAdapter(ListAdapter paramListAdapter)
  {
    if (getAdapter() != null) {
      return;
    }
    if ((paramListAdapter instanceof TuSdkListViewAdapter))
    {
      this.j = ((TuSdkListViewAdapter)paramListAdapter);
      preSetAdapter(this.j);
    }
    if ((this.f != null) && ((this.g != null) || (this.h != 0))) {
      d();
    }
    super.setAdapter(paramListAdapter);
  }
  
  protected void preSetAdapter(TuSdkListViewAdapter paramTuSdkListViewAdapter) {}
  
  public void reloadData(boolean paramBoolean)
  {
    if (getAdapter() == null) {
      setAdapter(getSdkAdapter());
    }
    if ((this.b == null) || (this.k == null) || (this.k.getIndexPaths().size() > 0)) {
      a();
    } else {
      b();
    }
    super.reloadData(paramBoolean);
  }
  
  public TuSdkIndexPath.TuSdkDataSource getDataSource()
  {
    return this.k;
  }
  
  public void setDataSource(TuSdkIndexPath.TuSdkDataSource paramTuSdkDataSource)
  {
    this.k = paramTuSdkDataSource;
  }
  
  public TuSdkListViewDeleagte getDeleagte()
  {
    return this.l;
  }
  
  public void setDeleagte(TuSdkListViewDeleagte paramTuSdkListViewDeleagte)
  {
    this.l = paramTuSdkListViewDeleagte;
    if (this.e == null) {
      setItemClickListener(paramTuSdkListViewDeleagte);
    }
  }
  
  protected void initView() {}
  
  protected void onDetachedFromWindow()
  {
    this.e = null;
    if (this.a != null)
    {
      this.a.onDestory();
      this.a = null;
    }
    super.onDetachedFromWindow();
  }
  
  protected View createCellView(TuSdkIndexPath paramTuSdkIndexPath, ViewGroup paramViewGroup)
  {
    View localView = null;
    if (this.l != null) {
      localView = this.l.onListViewItemCreate(this, paramTuSdkIndexPath, paramViewGroup);
    }
    return localView;
  }
  
  protected View createViewFromResource(TuSdkIndexPath paramTuSdkIndexPath, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null) {
      paramView = createCellView(paramTuSdkIndexPath, paramViewGroup);
    }
    paramView.setTag(paramTuSdkIndexPath);
    if ((paramView instanceof TuSdkViewInterface)) {
      ((TuSdkViewInterface)paramView).viewNeedRest();
    }
    if (this.k != null) {
      this.k.onViewBinded(paramTuSdkIndexPath, paramView);
    }
    return paramView;
  }
  
  protected class TuSdkListViewAdapter
    extends BaseAdapter
  {
    protected TuSdkListViewAdapter() {}
    
    public int getCount()
    {
      if (TuSdkListView.a(TuSdkListView.this) != null) {
        return TuSdkListView.a(TuSdkListView.this).getIndexPaths().size();
      }
      return 0;
    }
    
    public Object getItem(int paramInt)
    {
      if (TuSdkListView.a(TuSdkListView.this) != null) {
        return TuSdkListView.a(TuSdkListView.this).getItem(TuSdkListView.a(TuSdkListView.this).getIndexPath(paramInt));
      }
      return null;
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public int getItemViewType(int paramInt)
    {
      if (TuSdkListView.a(TuSdkListView.this) != null) {
        return ((TuSdkIndexPath)TuSdkListView.a(TuSdkListView.this).getIndexPaths().get(paramInt)).viewType;
      }
      return 0;
    }
    
    public int getViewTypeCount()
    {
      if (TuSdkListView.a(TuSdkListView.this) != null) {
        return TuSdkListView.a(TuSdkListView.this).viewTypes();
      }
      return 1;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      TuSdkIndexPath localTuSdkIndexPath = TuSdkListView.a(TuSdkListView.this).getIndexPath(paramInt);
      View localView = TuSdkListView.this.createViewFromResource(localTuSdkIndexPath, paramView, paramViewGroup);
      return localView;
    }
  }
  
  protected class TuSdkOnItemClickListener
    implements AdapterView.OnItemClickListener
  {
    protected TuSdkOnItemClickListener() {}
    
    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      TuSdkListView.this.onListViewItemClick(paramAdapterView, paramView, paramInt, paramLong);
    }
  }
  
  public static abstract interface TuSdkListViewDeleagte
    extends TuSdkListView.TuSdkListViewItemClickListener
  {
    public abstract View onListViewItemCreate(TuSdkListView paramTuSdkListView, TuSdkIndexPath paramTuSdkIndexPath, ViewGroup paramViewGroup);
  }
  
  public static abstract interface TuSdkListViewItemClickListener
  {
    public abstract void onListViewItemClick(TuSdkListView paramTuSdkListView, View paramView, TuSdkIndexPath paramTuSdkIndexPath);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\listview\TuSdkListView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */