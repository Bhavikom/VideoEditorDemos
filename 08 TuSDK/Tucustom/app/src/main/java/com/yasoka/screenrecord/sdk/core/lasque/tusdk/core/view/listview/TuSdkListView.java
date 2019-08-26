// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview;

import android.widget.BaseAdapter;
//import org.lasque.tusdk.core.view.TuSdkViewInterface;
import android.widget.ListAdapter;
import android.widget.AdapterView;
import android.view.ViewGroup;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewInterface;

public class TuSdkListView extends TuSdkRefreshListView implements TuSdkListViewFlingAction.TuSdkListViewFlingActionDelegate
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
    
    public TuSdkListView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuSdkListView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuSdkListView(final Context context) {
        super(context);
    }
    
    public TuSdkListViewFlingAction getFlingAction() {
        return this.a;
    }
    
    public void setFlingAction(final TuSdkListViewFlingAction a) {
        this.a = a;
    }
    
    public void enableFlingAction() {
        if (this.a != null) {
            return;
        }
        (this.a = new TuSdkListViewFlingAction(this.getContext())).setDelegate(this);
    }
    
    public void resetFlingItem() {
        if (this.a != null) {
            this.a.resetDownView();
        }
    }
    
    @SuppressLint({ "ClickableViewAccessibility" })
    @Override
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (this.a != null) {
            View listView = null;
            if (motionEvent.getAction() == 0) {
                listView = this.listViewAt(motionEvent);
            }
            if (this.a.onTouchEvent(motionEvent, listView)) {
                return true;
            }
        }
        return super.onTouchEvent(motionEvent);
    }
    
    @SuppressLint({ "Recycle" })
    @Override
    public void onFlingActionCancelItemClick(final MotionEvent motionEvent) {
        if (motionEvent == null) {
            return;
        }
        final MotionEvent obtain = MotionEvent.obtain(motionEvent);
        obtain.setAction(0x3 | motionEvent.getActionIndex() << 8);
        super.onTouchEvent(obtain);
    }
    
    public View getEmptyView() {
        return this.b;
    }
    
    public void setEmptyView(final View b) {
        this.a();
        this.b = b;
    }
    
    public void setEmptyView(final int n) {
        if (n == 0) {
            return;
        }
        this.setEmptyView(TuSdkViewHelper.buildView(this.getContext(), n, (ViewGroup)this.getTopHolderView()));
    }
    
    private void a() {
        this.c();
        if (this.b == null) {
            return;
        }
        if (this.getTopHeight() > 0) {
            this.setTopHeight(this.getTopHeight());
        }
        this.getTopHolderView().removeView(this.b);
    }
    
    private void b() {
        if (this.getTopHeight() > 0) {
            this.setTopHeight(0);
        }
        this.addInTopHolderView(this.b);
        if (this.g != null) {
            if (this.i) {
                this.g.needShowFooter(false);
            }
            else {
                this.setTotalFooterViewInfo(this.g, false);
            }
        }
    }
    
    public void emptyNeedFullHeight() {
        TuSdkViewHelper.setViewHeight(this.getEmptyView(), this.getHeight());
    }
    
    public boolean ismDisableAutoDeselectCell() {
        return this.c;
    }
    
    public void setDisableAutoDeselectCell(final boolean c) {
        this.c = c;
    }
    
    public TuSdkListSelectableCellViewInterface getSelectedCellView() {
        return this.d;
    }
    
    public void setSelectedCellView(final TuSdkListSelectableCellViewInterface d) {
        this.d = d;
    }
    
    public void deselectCell() {
        if (this.c || this.d == null) {
            return;
        }
        this.d.onCellDeselected();
        this.d = null;
    }
    
    public TuSdkListViewItemClickListener getItemClickListener() {
        return this.e;
    }
    
    public void setItemClickListener(final TuSdkListViewItemClickListener e) {
        this.e = e;
        if (this.e == null) {
            this.setOnItemClickListener((AdapterView.OnItemClickListener)null);
        }
        else {
            this.setOnItemClickListener(this.buildOnItemClickListener());
        }
    }
    
    protected AdapterView.OnItemClickListener buildOnItemClickListener() {
        return (AdapterView.OnItemClickListener)new TuSdkOnItemClickListener();
    }
    
    protected void onListViewItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
        if (this.e == null || this.k == null) {
            return;
        }
        if (!this.c && view instanceof TuSdkListSelectableCellViewInterface) {
            (this.d = (TuSdkListSelectableCellViewInterface)view).onCellSelected(n);
        }
        final TuSdkIndexPath indexPath = this.k.getIndexPath(this.getDataIndex(n));
        if (indexPath == null) {
            return;
        }
        this.e.onListViewItemClick(this, view, indexPath);
    }
    
    public String getTotalFooterFormater() {
        return this.f;
    }
    
    public void setTotalFooterFormater(final String s) {
        this.f = s;
        if (this.g != null) {
            this.g.setTitleFormater(s);
        }
    }
    
    public TuSdkListTotalFootView getTotalFooterView() {
        if (this.g == null && this.h != 0) {
            this.g = TuSdkViewHelper.buildView(this.getContext(), this.h);
        }
        return this.g;
    }
    
    public void setTotalFooterView(final TuSdkListTotalFootView g) {
        this.g = g;
    }
    
    public int getTotalFooterViewId() {
        return this.h;
    }
    
    public void setTotalFooterViewId(final int h) {
        this.h = h;
    }
    
    public boolean isAutoHiddenTotalFooterView() {
        return this.i;
    }
    
    public void setAutoHiddenTotalFooterView(final boolean i) {
        this.i = i;
    }
    
    private void c() {
        if (this.g == null) {
            return;
        }
        this.g.needShowFooter(true);
        this.setTotalFooterViewInfo(this.g, true);
    }
    
    protected void setTotalFooterViewInfo(final TuSdkListTotalFootView tuSdkListTotalFootView, final boolean b) {
        if (this.k != null) {
            this.g.setTotal(this.k.count());
        }
    }
    
    private void d() {
        (this.g = this.getTotalFooterView()).setTitleFormater(this.f);
        this.g.needShowFooter(!this.i);
        this.addFooterView((View)this.g);
    }
    
    public TuSdkListViewAdapter getSdkAdapter() {
        if (this.j == null) {
            this.j = this.buildSdkAdapter();
        }
        return this.j;
    }
    
    public void setSdkAdapter(final TuSdkListViewAdapter j) {
        this.j = j;
    }
    
    protected TuSdkListViewAdapter buildSdkAdapter() {
        return new TuSdkListViewAdapter();
    }
    
    @Override
    public void setAdapter(final ListAdapter adapter) {
        if (this.getAdapter() != null) {
            return;
        }
        if (adapter instanceof TuSdkListViewAdapter) {
            this.preSetAdapter(this.j = (TuSdkListViewAdapter)adapter);
        }
        if (this.f != null && (this.g != null || this.h != 0)) {
            this.d();
        }
        super.setAdapter(adapter);
    }
    
    protected void preSetAdapter(final TuSdkListViewAdapter tuSdkListViewAdapter) {
    }
    
    @Override
    public void reloadData(final boolean b) {
        if (this.getAdapter() == null) {
            this.setAdapter((ListAdapter)this.getSdkAdapter());
        }
        if (this.b == null || this.k == null || this.k.getIndexPaths().size() > 0) {
            this.a();
        }
        else {
            this.b();
        }
        super.reloadData(b);
    }
    
    public TuSdkIndexPath.TuSdkDataSource getDataSource() {
        return this.k;
    }
    
    public void setDataSource(final TuSdkIndexPath.TuSdkDataSource k) {
        this.k = k;
    }
    
    public TuSdkListViewDeleagte getDeleagte() {
        return this.l;
    }
    
    public void setDeleagte(final TuSdkListViewDeleagte tuSdkListViewDeleagte) {
        this.l = tuSdkListViewDeleagte;
        if (this.e == null) {
            this.setItemClickListener(tuSdkListViewDeleagte);
        }
    }
    
    @Override
    protected void initView() {
    }
    
    protected void onDetachedFromWindow() {
        this.e = null;
        if (this.a != null) {
            this.a.onDestory();
            this.a = null;
        }
        super.onDetachedFromWindow();
    }
    
    protected View createCellView(final TuSdkIndexPath tuSdkIndexPath, final ViewGroup viewGroup) {
        View onListViewItemCreate = null;
        if (this.l != null) {
            onListViewItemCreate = this.l.onListViewItemCreate(this, tuSdkIndexPath, viewGroup);
        }
        return onListViewItemCreate;
    }
    
    protected View createViewFromResource(final TuSdkIndexPath tag, View cellView, final ViewGroup viewGroup) {
        if (cellView == null) {
            cellView = this.createCellView(tag, viewGroup);
        }
        cellView.setTag((Object)tag);
        if (cellView instanceof TuSdkViewInterface) {
            ((TuSdkViewInterface)cellView).viewNeedRest();
        }
        if (this.k != null) {
            this.k.onViewBinded(tag, cellView);
        }
        return cellView;
    }
    
    protected class TuSdkListViewAdapter extends BaseAdapter
    {
        public int getCount() {
            if (TuSdkListView.this.k != null) {
                return TuSdkListView.this.k.getIndexPaths().size();
            }
            return 0;
        }
        
        public Object getItem(final int n) {
            if (TuSdkListView.this.k != null) {
                return TuSdkListView.this.k.getItem(TuSdkListView.this.k.getIndexPath(n));
            }
            return null;
        }
        
        public long getItemId(final int n) {
            return n;
        }
        
        public int getItemViewType(final int n) {
            if (TuSdkListView.this.k != null) {
                return TuSdkListView.this.k.getIndexPaths().get(n).viewType;
            }
            return 0;
        }
        
        public int getViewTypeCount() {
            if (TuSdkListView.this.k != null) {
                return TuSdkListView.this.k.viewTypes();
            }
            return 1;
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            return TuSdkListView.this.createViewFromResource(TuSdkListView.this.k.getIndexPath(n), view, viewGroup);
        }
    }
    
    protected class TuSdkOnItemClickListener implements AdapterView.OnItemClickListener
    {
        public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
            TuSdkListView.this.onListViewItemClick(adapterView, view, n, n2);
        }
    }
    
    public interface TuSdkListViewDeleagte extends TuSdkListViewItemClickListener
    {
        View onListViewItemCreate(final TuSdkListView p0, final TuSdkIndexPath p1, final ViewGroup p2);
    }
    
    public interface TuSdkListViewItemClickListener
    {
        void onListViewItemClick(final TuSdkListView p0, final View p1, final TuSdkIndexPath p2);
    }
}
