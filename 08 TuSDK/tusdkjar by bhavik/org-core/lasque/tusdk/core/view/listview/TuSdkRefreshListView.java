package org.lasque.tusdk.core.view.listview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.anim.AnimHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewInterface;

public abstract class TuSdkRefreshListView
  extends ListView
  implements AbsListView.OnScrollListener, TuSdkViewInterface
{
  private TuSdkListViewRefreshListener a;
  private TuSdkListViewLoadMoreListener b;
  private int c;
  private int d;
  private TuSdkRefreshListHeaderView e;
  private TuSdkRefreshListFooterView f;
  private float g = -1.0F;
  private boolean h;
  private boolean i;
  private boolean j;
  private TuSdkRefreshListTopHolderView k;
  private int l;
  private BaseAdapter m;
  private AbsListView.OnScrollListener n;
  
  public TuSdkRefreshListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    initView();
  }
  
  public TuSdkRefreshListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initView();
  }
  
  public TuSdkRefreshListView(Context paramContext)
  {
    super(paramContext);
    initView();
  }
  
  public int getTopHeight()
  {
    return this.l;
  }
  
  public void setRefreshLayoutResId(int paramInt1, int paramInt2)
  {
    this.c = paramInt1;
    this.d = paramInt2;
  }
  
  protected abstract void initView();
  
  public void setRefreshListener(TuSdkListViewRefreshListener paramTuSdkListViewRefreshListener, TuSdkListViewLoadMoreListener paramTuSdkListViewLoadMoreListener)
  {
    this.a = paramTuSdkListViewRefreshListener;
    this.b = paramTuSdkListViewLoadMoreListener;
  }
  
  public TuSdkRefreshListHeaderView getPullRefreshView()
  {
    return this.e;
  }
  
  public TuSdkRefreshListFooterView getLoadMoreView()
  {
    return this.f;
  }
  
  public TuSdkRefreshListTopHolderView getTopHolderView()
  {
    if (this.k == null)
    {
      this.k = new TuSdkRefreshListTopHolderView(getContext());
      this.k.setOrientation(1);
      this.k.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
      this.k.loadView();
    }
    return this.k;
  }
  
  public void setTopDpHeight(int paramInt)
  {
    this.l = ContextUtils.dip2px(getContext(), paramInt);
    setTopHeight(this.l);
  }
  
  protected void setTopHeight(int paramInt)
  {
    TuSdkRefreshListTopHolderView localTuSdkRefreshListTopHolderView = getTopHolderView();
    localTuSdkRefreshListTopHolderView.setVisiableHeight(paramInt);
  }
  
  public void addInTopHolderView(View paramView)
  {
    if ((paramView == null) || (paramView.getParent() != null)) {
      return;
    }
    TuSdkRefreshListTopHolderView localTuSdkRefreshListTopHolderView = getTopHolderView();
    localTuSdkRefreshListTopHolderView.addView(paramView);
  }
  
  public void addInTopHolderView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    if ((paramView == null) || (paramView.getParent() != null)) {
      return;
    }
    TuSdkRefreshListTopHolderView localTuSdkRefreshListTopHolderView = getTopHolderView();
    localTuSdkRefreshListTopHolderView.addView(paramView, paramLayoutParams);
  }
  
  public void addInTopHolderView(View paramView, int paramInt1, int paramInt2)
  {
    if ((paramView == null) || (paramView.getParent() != null)) {
      return;
    }
    TuSdkRefreshListTopHolderView localTuSdkRefreshListTopHolderView = getTopHolderView();
    localTuSdkRefreshListTopHolderView.addView(paramView, paramInt1, paramInt2);
  }
  
  public void removeInTopHolderView(View paramView)
  {
    if (paramView == null) {
      return;
    }
    TuSdkRefreshListTopHolderView localTuSdkRefreshListTopHolderView = getTopHolderView();
    localTuSdkRefreshListTopHolderView.removeView(paramView);
  }
  
  public void loadView() {}
  
  public void viewDidLoad() {}
  
  public View getViewAt(int paramInt)
  {
    if ((paramInt == -1) || (paramInt < getFirstVisiblePosition()) || (paramInt > getLastVisiblePosition())) {
      return null;
    }
    View localView = getChildAt(paramInt - getFirstVisiblePosition());
    return localView;
  }
  
  public View listViewAt(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent == null) {
      return null;
    }
    int i1 = pointToPosition((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
    return getViewAt(i1);
  }
  
  public int viewInCellTop(int paramInt, View paramView)
  {
    if (paramView == null) {
      return 0;
    }
    View localView = getViewAt(paramInt);
    if (localView == null) {
      return 0;
    }
    return TuSdkViewHelper.locationInWindowTop(paramView) - TuSdkViewHelper.locationInWindowTop(localView);
  }
  
  private void a()
  {
    if ((this.a != null) && (this.c != 0))
    {
      this.e = ((TuSdkRefreshListHeaderView)TuSdkViewHelper.buildView(getContext(), this.c));
      this.e.viewDidLoad();
      addHeaderView(this.e);
    }
    addHeaderView(getTopHolderView());
    if ((this.b != null) && (this.d != 0)) {
      this.f = ((TuSdkRefreshListFooterView)TuSdkViewHelper.buildView(getContext(), this.d));
    }
  }
  
  public void viewWillDestory()
  {
    this.a = null;
    this.b = null;
    if (this.k != null)
    {
      TuSdkViewHelper.viewWillDestory(this.k);
      this.k = null;
    }
  }
  
  public void viewNeedRest() {}
  
  public void setAdapter(ListAdapter paramListAdapter)
  {
    if (getAdapter() != null) {
      return;
    }
    a();
    super.setOnScrollListener(this);
    if ((paramListAdapter instanceof BaseAdapter)) {
      this.m = ((BaseAdapter)paramListAdapter);
    } else {
      this.m = null;
    }
    if ((!this.j) && (this.f != null))
    {
      this.j = true;
      addFooterView(this.f);
    }
    super.setAdapter(paramListAdapter);
  }
  
  public void reloadData(boolean paramBoolean)
  {
    this.i = false;
    if (paramBoolean) {
      resetRefresh();
    }
    if (this.m != null) {
      this.m.notifyDataSetChanged();
    }
  }
  
  public void reloadData()
  {
    reloadData(true);
  }
  
  public void resetRefresh()
  {
    this.h = false;
    if (this.e != null) {
      this.e.refreshEnded();
    }
    if (this.f != null) {
      this.f.setViewShowed(false);
    }
  }
  
  public void refreshStart()
  {
    this.h = true;
    if (this.e != null) {
      this.e.refreshStart();
    }
  }
  
  public void firstStart()
  {
    this.h = true;
    if (this.e != null) {
      this.e.firstStart();
    }
  }
  
  public void setHasMore(boolean paramBoolean)
  {
    this.i = paramBoolean;
    if (this.f != null) {
      this.f.setViewShowed(paramBoolean);
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.g == -1.0F) {
      this.g = paramMotionEvent.getRawY();
    }
    switch (paramMotionEvent.getAction())
    {
    case 0: 
      this.g = paramMotionEvent.getRawY();
      break;
    case 2: 
      a(paramMotionEvent);
      break;
    default: 
      b(paramMotionEvent);
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  private void a(MotionEvent paramMotionEvent)
  {
    if (this.h) {
      return;
    }
    float f1 = paramMotionEvent.getRawY() - this.g;
    this.g = paramMotionEvent.getRawY();
    a(f1);
  }
  
  private void b(MotionEvent paramMotionEvent)
  {
    if (this.h) {
      return;
    }
    this.g = -1.0F;
    b();
  }
  
  private void a(float paramFloat)
  {
    if ((this.e == null) || (getFirstVisiblePosition() > 0)) {
      return;
    }
    if (this.e.updateHeight(paramFloat) > 0) {
      setSelection(0);
    }
  }
  
  private void b()
  {
    if ((this.e == null) || (getFirstVisiblePosition() > 0)) {
      return;
    }
    if (this.e.submitState() == TuSdkRefreshListHeaderView.TuSdkRefreshState.StateLoading)
    {
      this.h = true;
      if (this.a != null) {
        this.a.onListViewRefreshed(this);
      }
    }
  }
  
  private void c()
  {
    if (this.h) {
      return;
    }
    if ((!this.i) || (this.f == null)) {
      return;
    }
    this.h = true;
    if (this.b != null) {
      this.b.onListViewLoadedMore(this);
    }
  }
  
  public void setOnScrollListener(AbsListView.OnScrollListener paramOnScrollListener)
  {
    this.n = paramOnScrollListener;
  }
  
  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
    if (this.n != null) {
      this.n.onScrollStateChanged(paramAbsListView, paramInt);
    }
  }
  
  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
    if (getLastVisiblePosition() == paramInt3 - 1) {
      c();
    }
    if (this.n != null) {
      this.n.onScroll(paramAbsListView, paramInt1, paramInt2, paramInt3);
    }
  }
  
  public void skipToLastTop()
  {
    final int i1 = getHeaderViewsCount() + this.m.getCount() + getFooterViewsCount() - 1;
    post(new Runnable()
    {
      public void run()
      {
        TuSdkRefreshListView.this.setSelection(i1);
      }
    });
  }
  
  public void scrollToPosition(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramInt1 < 0) {
      return;
    }
    int i1 = getHeight() - paramInt2;
    if ((!paramBoolean) || (Build.VERSION.SDK_INT < 11)) {
      setSelectionFromTop(paramInt1, i1);
    } else {
      a(paramInt1, i1);
    }
  }
  
  public void scrollToAdapterPosition(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i1 = getPositionForIndex(paramInt1);
    scrollToPosition(i1, paramInt2, paramBoolean);
  }
  
  public int getDataIndex(int paramInt)
  {
    return paramInt - getHeaderViewsCount();
  }
  
  public int getPositionForIndex(int paramInt)
  {
    return paramInt + getHeaderViewsCount();
  }
  
  @TargetApi(11)
  private void a(int paramInt1, int paramInt2)
  {
    smoothScrollToPositionFromTop(paramInt1, paramInt2, 150);
  }
  
  protected void onRemoveViewAnimationEnd()
  {
    TLog.d("onRemoveViewAnimationEnd", new Object[0]);
  }
  
  public void removeViewWithAnim(int paramInt)
  {
    View localView = getViewAt(paramInt);
    AnimHelper.removeViewAnimtor(localView, new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        TuSdkRefreshListView.this.onRemoveViewAnimationEnd();
      }
    });
  }
  
  public static abstract interface TuSdkListViewLoadMoreListener
  {
    public abstract void onListViewLoadedMore(TuSdkRefreshListView paramTuSdkRefreshListView);
  }
  
  public static abstract interface TuSdkListViewRefreshListener
  {
    public abstract void onListViewRefreshed(TuSdkRefreshListView paramTuSdkRefreshListView);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\view\listview\TuSdkRefreshListView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */