// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview;

//import org.lasque.tusdk.core.utils.anim.AnimHelper;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
//import org.lasque.tusdk.core.utils.TLog;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.view.MotionEvent;
import android.view.View;
//import org.lasque.tusdk.core.utils.ContextUtils;
import android.view.ViewGroup;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.BaseAdapter;
//import org.lasque.tusdk.core.view.TuSdkViewInterface;
import android.widget.AbsListView;
import android.widget.ListView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.anim.AnimHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewInterface;

public abstract class TuSdkRefreshListView extends ListView implements AbsListView.OnScrollListener, TuSdkViewInterface
{
    private TuSdkListViewRefreshListener a;
    private TuSdkListViewLoadMoreListener b;
    private int c;
    private int d;
    private TuSdkRefreshListHeaderView e;
    private TuSdkRefreshListFooterView f;
    private float g;
    private boolean h;
    private boolean i;
    private boolean j;
    private TuSdkRefreshListTopHolderView k;
    private int l;
    private BaseAdapter m;
    private OnScrollListener n;
    
    public TuSdkRefreshListView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.g = -1.0f;
        this.initView();
    }
    
    public TuSdkRefreshListView(final Context context, final AttributeSet set) {
        super(context, set);
        this.g = -1.0f;
        this.initView();
    }
    
    public TuSdkRefreshListView(final Context context) {
        super(context);
        this.g = -1.0f;
        this.initView();
    }
    
    public int getTopHeight() {
        return this.l;
    }
    
    public void setRefreshLayoutResId(final int c, final int d) {
        this.c = c;
        this.d = d;
    }
    
    protected abstract void initView();
    
    public void setRefreshListener(final TuSdkListViewRefreshListener a, final TuSdkListViewLoadMoreListener b) {
        this.a = a;
        this.b = b;
    }
    
    public TuSdkRefreshListHeaderView getPullRefreshView() {
        return this.e;
    }
    
    public TuSdkRefreshListFooterView getLoadMoreView() {
        return this.f;
    }
    
    public TuSdkRefreshListTopHolderView getTopHolderView() {
        if (this.k == null) {
            (this.k = new TuSdkRefreshListTopHolderView(this.getContext())).setOrientation(LinearLayout.VERTICAL);
            this.k.setLayoutParams((ViewGroup.LayoutParams)new LayoutParams(-1, -2));
            this.k.loadView();
        }
        return this.k;
    }
    
    public void setTopDpHeight(final int n) {
        this.setTopHeight(this.l = ContextUtils.dip2px(this.getContext(), (float)n));
    }
    
    protected void setTopHeight(final int visiableHeight) {
        this.getTopHolderView().setVisiableHeight(visiableHeight);
    }
    
    public void addInTopHolderView(final View view) {
        if (view == null || view.getParent() != null) {
            return;
        }
        this.getTopHolderView().addView(view);
    }
    
    public void addInTopHolderView(final View view, final ViewGroup.LayoutParams layoutParams) {
        if (view == null || view.getParent() != null) {
            return;
        }
        this.getTopHolderView().addView(view, layoutParams);
    }
    
    public void addInTopHolderView(final View view, final int n, final int n2) {
        if (view == null || view.getParent() != null) {
            return;
        }
        this.getTopHolderView().addView(view, n, n2);
    }
    
    public void removeInTopHolderView(final View view) {
        if (view == null) {
            return;
        }
        this.getTopHolderView().removeView(view);
    }
    
    public void loadView() {
    }
    
    public void viewDidLoad() {
    }
    
    public View getViewAt(final int n) {
        if (n == -1 || n < this.getFirstVisiblePosition() || n > this.getLastVisiblePosition()) {
            return null;
        }
        return this.getChildAt(n - this.getFirstVisiblePosition());
    }
    
    public View listViewAt(final MotionEvent motionEvent) {
        if (motionEvent == null) {
            return null;
        }
        return this.getViewAt(this.pointToPosition((int)motionEvent.getX(), (int)motionEvent.getY()));
    }
    
    public int viewInCellTop(final int n, final View view) {
        if (view == null) {
            return 0;
        }
        final View view2 = this.getViewAt(n);
        if (view2 == null) {
            return 0;
        }
        return TuSdkViewHelper.locationInWindowTop(view) - TuSdkViewHelper.locationInWindowTop(view2);
    }
    
    private void a() {
        if (this.a != null && this.c != 0) {
            (this.e = TuSdkViewHelper.buildView(this.getContext(), this.c)).viewDidLoad();
            this.addHeaderView((View)this.e);
        }
        this.addHeaderView((View)this.getTopHolderView());
        if (this.b != null && this.d != 0) {
            this.f = TuSdkViewHelper.buildView(this.getContext(), this.d);
        }
    }
    
    public void viewWillDestory() {
        this.a = null;
        this.b = null;
        if (this.k != null) {
            TuSdkViewHelper.viewWillDestory((View)this.k);
            this.k = null;
        }
    }
    
    public void viewNeedRest() {
    }
    
    public void setAdapter(final ListAdapter adapter) {
        if (this.getAdapter() != null) {
            return;
        }
        this.a();
        super.setOnScrollListener((OnScrollListener)this);
        if (adapter instanceof BaseAdapter) {
            this.m = (BaseAdapter)adapter;
        }
        else {
            this.m = null;
        }
        if (!this.j && this.f != null) {
            this.j = true;
            this.addFooterView((View)this.f);
        }
        super.setAdapter(adapter);
    }
    
    public void reloadData(final boolean b) {
        this.i = false;
        if (b) {
            this.resetRefresh();
        }
        if (this.m != null) {
            this.m.notifyDataSetChanged();
        }
    }
    
    public void reloadData() {
        this.reloadData(true);
    }
    
    public void resetRefresh() {
        this.h = false;
        if (this.e != null) {
            this.e.refreshEnded();
        }
        if (this.f != null) {
            this.f.setViewShowed(false);
        }
    }
    
    public void refreshStart() {
        this.h = true;
        if (this.e != null) {
            this.e.refreshStart();
        }
    }
    
    public void firstStart() {
        this.h = true;
        if (this.e != null) {
            this.e.firstStart();
        }
    }
    
    public void setHasMore(final boolean b) {
        this.i = b;
        if (this.f != null) {
            this.f.setViewShowed(b);
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (this.g == -1.0f) {
            this.g = motionEvent.getRawY();
        }
        switch (motionEvent.getAction()) {
            case 0: {
                this.g = motionEvent.getRawY();
                break;
            }
            case 2: {
                this.a(motionEvent);
                break;
            }
            default: {
                this.b(motionEvent);
                break;
            }
        }
        return super.onTouchEvent(motionEvent);
    }
    
    private void a(final MotionEvent motionEvent) {
        if (this.h) {
            return;
        }
        final float n = motionEvent.getRawY() - this.g;
        this.g = motionEvent.getRawY();
        this.a(n);
    }
    
    private void b(final MotionEvent motionEvent) {
        if (this.h) {
            return;
        }
        this.g = -1.0f;
        this.b();
    }
    
    private void a(final float n) {
        if (this.e == null || this.getFirstVisiblePosition() > 0) {
            return;
        }
        if (this.e.updateHeight(n) > 0) {
            this.setSelection(0);
        }
    }
    
    private void b() {
        if (this.e == null || this.getFirstVisiblePosition() > 0) {
            return;
        }
        if (this.e.submitState() == TuSdkRefreshListHeaderView.TuSdkRefreshState.StateLoading) {
            this.h = true;
            if (this.a != null) {
                this.a.onListViewRefreshed(this);
            }
        }
    }
    
    private void c() {
        if (this.h) {
            return;
        }
        if (!this.i || this.f == null) {
            return;
        }
        this.h = true;
        if (this.b != null) {
            this.b.onListViewLoadedMore(this);
        }
    }
    
    public void setOnScrollListener(final OnScrollListener n) {
        this.n = n;
    }
    
    public void onScrollStateChanged(final AbsListView absListView, final int n) {
        if (this.n != null) {
            this.n.onScrollStateChanged(absListView, n);
        }
    }
    
    public void onScroll(final AbsListView absListView, final int n, final int n2, final int n3) {
        if (this.getLastVisiblePosition() == n3 - 1) {
            this.c();
        }
        if (this.n != null) {
            this.n.onScroll(absListView, n, n2, n3);
        }
    }
    
    public void skipToLastTop() {
        this.post((Runnable)new Runnable() {
            final /* synthetic */ int a = TuSdkRefreshListView.this.getHeaderViewsCount() + TuSdkRefreshListView.this.m.getCount() + TuSdkRefreshListView.this.getFooterViewsCount() - 1;
            
            @Override
            public void run() {
                TuSdkRefreshListView.this.setSelection(this.a);
            }
        });
    }
    
    public void scrollToPosition(final int n, final int n2, final boolean b) {
        if (n < 0) {
            return;
        }
        final int n3 = this.getHeight() - n2;
        if (!b || Build.VERSION.SDK_INT < 11) {
            this.setSelectionFromTop(n, n3);
        }
        else {
            this.a(n, n3);
        }
    }
    
    public void scrollToAdapterPosition(final int n, final int n2, final boolean b) {
        this.scrollToPosition(this.getPositionForIndex(n), n2, b);
    }
    
    public int getDataIndex(final int n) {
        return n - this.getHeaderViewsCount();
    }
    
    public int getPositionForIndex(final int n) {
        return n + this.getHeaderViewsCount();
    }
    
    @TargetApi(11)
    private void a(final int n, final int n2) {
        this.smoothScrollToPositionFromTop(n, n2, 150);
    }
    
    protected void onRemoveViewAnimationEnd() {
        TLog.d("onRemoveViewAnimationEnd", new Object[0]);
    }
    
    public void removeViewWithAnim(final int n) {
        AnimHelper.removeViewAnimtor(this.getViewAt(n), (Animator.AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                TuSdkRefreshListView.this.onRemoveViewAnimationEnd();
            }
        });
    }
    
    public interface TuSdkListViewLoadMoreListener
    {
        void onListViewLoadedMore(final TuSdkRefreshListView p0);
    }
    
    public interface TuSdkListViewRefreshListener
    {
        void onListViewRefreshed(final TuSdkRefreshListView p0);
    }
}
