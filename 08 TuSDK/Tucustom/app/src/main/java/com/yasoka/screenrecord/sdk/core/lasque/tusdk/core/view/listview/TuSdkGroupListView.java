// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview;

import android.view.ViewGroup;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;

public abstract class TuSdkGroupListView<T, V extends View, M, N extends View> extends TuSdkListView
{
    private GroupListViewItemClickListener<T, V> a;
    private GroupListViewHeaderClickListener<M, N> b;
    private GroupListViewDeleagte c;
    private int d;
    private int e;
    
    public TuSdkGroupListView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public TuSdkGroupListView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TuSdkGroupListView(final Context context) {
        super(context);
    }
    
    public void setCellLayoutId(final int d) {
        this.d = d;
    }
    
    public int getCellLayoutId() {
        return this.d;
    }
    
    public int getHeaderLayoutId() {
        return this.e;
    }
    
    public void setHeaderLayoutId(final int e) {
        this.e = e;
    }
    
    public void setItemClickListener(final GroupListViewItemClickListener<T, V> a) {
        this.a = a;
    }
    
    public void setHeaderClickListener(final GroupListViewHeaderClickListener<M, N> b) {
        this.b = b;
    }
    
    @Override
    public void setDataSource(final TuSdkIndexPath.TuSdkDataSource dataSource) {
        super.setDataSource(dataSource);
        if (this.c == null) {
            this.setDeleagte(this.c = new GroupListViewDeleagte());
        }
        this.reloadData();
    }
    
    protected abstract void onGroupListViewCreated(final V p0, final TuSdkIndexPath p1);
    
    protected abstract void onGroupListHeaderCreated(final N p0, final TuSdkIndexPath p1);

    private class GroupListViewDeleagte implements TuSdkListViewDeleagte {
        private GroupListViewDeleagte() {
        }

        public void onListViewItemClick(TuSdkListView var1, View var2, TuSdkIndexPath var3) {
            if (!TuSdkViewHelper.isFastDoubleClick(1000L)) {
                if (var3 != null) {
                    if (var3.viewType == 0 && TuSdkGroupListView.this.a != null) {
                        TuSdkGroupListView.this.a.onGroupItemClick(TuSdkGroupListView.this.getDataSource().getItem(var3), var2, var3);
                    } else if (var3.viewType == 1 && TuSdkGroupListView.this.b != null) {
                        TuSdkGroupListView.this.b.onGroupHeaderClick(TuSdkGroupListView.this.getDataSource().getItem(var3), var2, var3);
                    }

                }
            }
        }

        public View onListViewItemCreate(TuSdkListView var1, TuSdkIndexPath var2, ViewGroup var3) {
            View var4 = null;
            if (var2.viewType == 0) {
                var4 = TuSdkViewHelper.buildView(TuSdkGroupListView.this.getContext(), TuSdkGroupListView.this.getCellLayoutId(), var3);
                TuSdkGroupListView.this.onGroupListViewCreated((V) var4, var2);
            } else if (var2.viewType == 1) {
                var4 = TuSdkViewHelper.buildView(TuSdkGroupListView.this.getContext(), TuSdkGroupListView.this.getHeaderLayoutId(), var3);
                TuSdkGroupListView.this.onGroupListHeaderCreated((N) var4, var2);
            }

            return var4;
        }
    }
    
    public interface GroupListViewItemClickListener<T, V>
    {
        void onGroupItemClick(final Object p0, final View p1, final TuSdkIndexPath p2);
    }
    
    public interface GroupListViewHeaderClickListener<M, N>
    {
        void onGroupHeaderClick(final Object p0, final View p1, final TuSdkIndexPath p2);
    }
}
