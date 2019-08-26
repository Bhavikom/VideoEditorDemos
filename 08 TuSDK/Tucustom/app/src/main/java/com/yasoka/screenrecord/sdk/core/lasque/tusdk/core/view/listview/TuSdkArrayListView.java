// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview;

import android.view.ViewGroup;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import java.util.ArrayList;
import android.util.AttributeSet;
import android.content.Context;
import java.util.List;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;

public abstract class TuSdkArrayListView<T, V extends View> extends TuSdkListView
{
    private int a;
    private List<T> b;
    private ArrayListDataSource c;
    private ArrayListViewDeleagte d;
    private ArrayListViewItemClickListener<T, V> e;
    
    public TuSdkArrayListView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuSdkArrayListView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuSdkArrayListView(final Context context) {
        super(context);
    }
    
    public void setCellLayoutId(final int a) {
        this.a = a;
    }
    
    public int getCellLayoutId() {
        return this.a;
    }
    
    public void setModeList(final List<T> b) {
        this.b = b;
        if (this.c == null) {
            this.setDataSource(this.c = new ArrayListDataSource());
        }
        if (this.d == null) {
            this.setDeleagte(this.d = new ArrayListViewDeleagte());
        }
        this.c.refreshIndexPaths(b);
        this.reloadData();
    }
    
    public List<T> getModeList() {
        return this.b;
    }
    
    public T getModeItem(final int n) {
        if (this.b == null || this.b.size() <= n) {
            return null;
        }
        return this.b.get(n);
    }
    
    public void setItemClickListener(final ArrayListViewItemClickListener<T, V> e) {
        this.e = e;
    }
    
    protected abstract void onArrayListViewCreated(final V p0, final TuSdkIndexPath p1);
    
    protected abstract void onArrayListViewBinded(final V p0, final TuSdkIndexPath p1);
    
    private class ArrayListDataSource implements TuSdkIndexPath.TuSdkDataSource
    {
        private List<TuSdkIndexPath> b;
        
        public void refreshIndexPaths(final List<T> list) {
            if (TuSdkArrayListView.this.getModeList() == null) {
                if (this.b != null) {
                    this.b.clear();
                }
                return;
            }
            final ArrayList<TuSdkIndexPath> b = new ArrayList<TuSdkIndexPath>(list.size());
            for (int i = 0; i < TuSdkArrayListView.this.getModeList().size(); ++i) {
                b.add(new TuSdkIndexPath(0, i));
            }
            this.b = b;
        }
        
        @Override
        public List<TuSdkIndexPath> getIndexPaths() {
            if (this.b == null) {
                this.b = new ArrayList<TuSdkIndexPath>(0);
            }
            return this.b;
        }
        
        @Override
        public TuSdkIndexPath getIndexPath(final int n) {
            if (n < 0 || this.b == null || this.b.size() <= n) {
                return null;
            }
            return this.b.get(n);
        }
        
        @Override
        public int viewTypes() {
            return 1;
        }
        
        @Override
        public int sectionCount() {
            return 1;
        }
        
        @Override
        public int rowCount(final int n) {
            return this.count();
        }
        
        @Override
        public int count() {
            return this.getIndexPaths().size();
        }
        
        @Override
        public void onViewBinded(final TuSdkIndexPath tuSdkIndexPath, final View view) {
            if (view instanceof TuSdkCellViewInterface) {
                TuSdkArrayListView.this.onArrayListViewBinded((V) view, tuSdkIndexPath);
                ((TuSdkCellViewInterface)view).setModel(TuSdkArrayListView.this.getModeList().get(tuSdkIndexPath.row));
            }
        }
        
        @Override
        public Object getItem(final TuSdkIndexPath tuSdkIndexPath) {
            if (TuSdkArrayListView.this.getModeList() == null) {
                return null;
            }
            return TuSdkArrayListView.this.getModeList().get(tuSdkIndexPath.row);
        }
    }
    
    private class ArrayListViewDeleagte implements TuSdkListViewDeleagte
    {
        @Override
        public void onListViewItemClick(final TuSdkListView tuSdkListView, final View view, final TuSdkIndexPath tuSdkIndexPath) {
            if (tuSdkIndexPath == null || TuSdkArrayListView.this.e == null || TuSdkViewHelper.isFastDoubleClick(500L)) {
                return;
            }
            TuSdkArrayListView.this.e.onArrayListViewItemClick(TuSdkArrayListView.this.getModeItem(tuSdkIndexPath.row), (V) view, tuSdkIndexPath);
        }
        
        @Override
        public View onListViewItemCreate(final TuSdkListView tuSdkListView, final TuSdkIndexPath tuSdkIndexPath, final ViewGroup viewGroup) {
            final View buildView = TuSdkViewHelper.buildView(TuSdkArrayListView.this.getContext(), TuSdkArrayListView.this.getCellLayoutId(), viewGroup);
            TuSdkArrayListView.this.onArrayListViewCreated((V) buildView, tuSdkIndexPath);
            return buildView;
        }
    }
    
    public interface ArrayListViewItemClickListener<T, V>
    {
        void onArrayListViewItemClick(final T p0, final V p1, final TuSdkIndexPath p2);
    }
}
