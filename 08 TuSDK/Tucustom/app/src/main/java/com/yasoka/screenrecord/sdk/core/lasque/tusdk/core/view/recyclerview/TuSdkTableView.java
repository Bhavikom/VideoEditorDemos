// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.recyclerview;

import android.view.ViewGroup;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
//import org.lasque.tusdk.core.view.listview.TuSdkCellViewInterface;
import android.content.Context;
import java.util.List;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkCellViewInterface;

public abstract class TuSdkTableView<T, V extends View> extends TuSdkRecyclerView
{
    private TuSdkTableViewItemClickDelegate<T, V> a;
    private TuSdkAdapter<T> b;
    private TuSdkLinearLayoutManager c;
    private int d;
    private List<T> e;
    private int f;
    private boolean g;
    private int h;
    protected TuSdkViewHolder.TuSdkViewHolderItemClickListener<T> mViewHolderItemClickListener;

    public TuSdkTableView(Context var1) {
        super(var1);
        //this.mViewHolderItemClickListener = new NamelessClass_1();
    }

    public TuSdkTableView(Context var1, AttributeSet var2) {
        super(var1, var2);
        //this.mViewHolderItemClickListener = new NamelessClass_1();
    }

    public TuSdkTableView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);

        class NamelessClass_1 implements TuSdkViewHolder.TuSdkViewHolderItemClickListener<T> {
            NamelessClass_1() {
            }

            public void onViewHolderItemClick(TuSdkViewHolder<T> var1) {
                if (TuSdkTableView.this.a != null) {
                    if (var1.itemView instanceof TuSdkCellViewInterface) {
                        TuSdkTableView.this.a.onTableViewItemClick(var1.getModel(), (V) var1.itemView, var1.getPosition());
                    }

                }
            }
        }

        this.mViewHolderItemClickListener = new NamelessClass_1();
    }
    
    public void setItemClickDelegate(final TuSdkTableViewItemClickDelegate<T, V> a) {
        this.a = a;
        if (this.b == null) {
            return;
        }
        if (a == null) {
            this.b.setItemClickListener(null);
        }
        else {
            this.b.setItemClickListener(this.mViewHolderItemClickListener);
        }
    }
    
    public TuSdkTableViewItemClickDelegate<T, V> getItemClickDelegate() {
        return this.a;
    }
    
    public int getCellLayoutId() {
        return this.d;
    }
    
    public void setCellLayoutId(final int d) {
        this.d = d;
        if (d > 0 && this.b != null) {
            this.b.setViewLayoutId(this.getCellLayoutId());
        }
    }
    
    public List<T> getModeList() {
        return this.e;
    }
    
    public void setModeList(final List<T> e) {
        this.e = e;
        if (this.b != null) {
            this.b.setModeList(this.e);
        }
    }
    
    public TuSdkAdapter<T> getSdkAdapter() {
        if (this.b == null) {
            (this.b = new TableViewAdapter(this.getCellLayoutId(), this.e)).setSelectedPosition(this.h);
            if (this.a != null) {
                this.b.setItemClickListener(this.mViewHolderItemClickListener);
            }
        }
        return this.b;
    }
    
    public void setAdapter(final RecyclerView.Adapter adapter) {
        if (adapter instanceof TuSdkAdapter) {
            this.b = (TuSdkAdapter<T>)adapter;
        }
        super.setAdapter(adapter);
    }
    
    public TuSdkLinearLayoutManager getSdkLayoutManager() {
        if (this.c == null) {
            this.c = new TuSdkLinearLayoutManager(this.getContext(), this.f, this.g);
        }
        return this.c;
    }
    
    public void setLayoutManager(final RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof TuSdkLinearLayoutManager) {
            this.c = (TuSdkLinearLayoutManager)layoutManager;
        }
        super.setLayoutManager(layoutManager);
    }
    
    public int getOrientation() {
        return this.f;
    }
    
    public void setOrientation(final int f) {
        this.f = f;
        if (this.c != null) {
            this.c.setOrientation(this.f);
        }
    }
    
    public void scrollToPositionWithOffset(final int n, final int n2) {
        if (this.c != null) {
            this.c.scrollToPositionWithOffset(n, n2);
        }
    }
    
    public void smoothScrollByCenter(final View view) {
        if (view == null) {
            return;
        }
        if (this.f == 0) {
            this.smoothScrollBy(TuSdkViewHelper.locationInWindowLeft(view) - TuSdkViewHelper.locationInWindowLeft((View)this) - (this.getWidth() - view.getWidth()) / 2, 0);
        }
        else if (this.f == 1) {
            this.smoothScrollBy(0, TuSdkViewHelper.locationInWindowTop(view) - TuSdkViewHelper.locationInWindowTop((View)this) - (this.getHeight() - view.getHeight()) / 2);
        }
    }
    
    public int getSelectedPosition() {
        return this.h;
    }
    
    public void setSelectedPosition(final int n) {
        this.setSelectedPosition(n, true);
    }
    
    public void setSelectedPosition(final int n, final boolean b) {
        this.h = n;
        if (this.b == null) {
            return;
        }
        this.b.setSelectedPosition(n);
        if (b) {
            this.b.notifyDataSetChanged();
        }
    }
    
    public void changeSelectedPosition(final int n) {
        if (this.b == null || this.c == null) {
            return;
        }
        final int selectedPosition = this.b.getSelectedPosition();
        if (selectedPosition == n) {
            return;
        }
        this.h = n;
        this.b.setSelectedPosition(n);
        this.c.selectedPosition(selectedPosition, false);
        this.c.selectedPosition(n, true);
    }
    
    public boolean isReverseLayout() {
        return this.g;
    }
    
    public void setReverseLayout(final boolean g) {
        this.g = g;
        if (this.c != null) {
            this.c.setReverseLayout(this.g);
        }
    }
    
    private void a() {
        if (this.getLayoutManager() == null) {
            this.setLayoutManager((RecyclerView.LayoutManager)this.getSdkLayoutManager());
        }
        if (this.getAdapter() == null) {
            this.setAdapter(this.getSdkAdapter());
        }
    }
    
    public void reloadData() {
        if (this.getAdapter() == null) {
            this.a();
        }
        else {
            this.getAdapter().notifyDataSetChanged();
        }
    }
    
    protected abstract void onViewCreated(final V p0, final ViewGroup p1, final int p2);
    
    protected abstract void onViewBinded(final V p0, final int p1);

    protected class TableViewAdapter extends TuSdkAdapter<T> {
        public TableViewAdapter() {
        }

        public TableViewAdapter(int var2, List<T> var3) {
            super(var2, var3);
        }

        public TableViewAdapter(int var2) {
            super(var2);
        }

        public TuSdkViewHolder<T> onCreateViewHolder(ViewGroup var1, int var2) {
            TuSdkViewHolder var3 = super.onCreateViewHolder(var1, var2);
            if (var3.itemView instanceof TuSdkCellViewInterface) {
                TuSdkTableView.this.onViewCreated((V) var3.itemView, var1, var2);
            }

            return var3;
        }

        public void onBindViewHolder(TuSdkViewHolder<T> var1, int var2) {
            super.onBindViewHolder(var1, var2);
            if (var1.itemView instanceof TuSdkCellViewInterface) {
                TuSdkTableView.this.onViewBinded((V) var1.itemView, var2);
            }

        }
    }
    
    public interface TuSdkTableViewItemClickDelegate<T, V extends View>
    {
        void onTableViewItemClick(final T p0, final V p1, final int p2);
    }
}
