// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.List;
//import android.support.v7.widget.RecyclerView;

public class TuSdkAdapter<T> extends RecyclerView.Adapter<TuSdkViewHolder<T>>
{
    private int a;
    private List<T> b;
    private int c;
    private TuSdkViewHolder.TuSdkViewHolderItemClickListener<T> d;
    
    public int getViewLayoutId() {
        return this.a;
    }
    
    public void setViewLayoutId(final int a) {
        this.a = a;
    }
    
    public List<T> getModeList() {
        return this.b;
    }
    
    public void setModeList(final List<T> b) {
        this.b = b;
    }
    
    public TuSdkViewHolder.TuSdkViewHolderItemClickListener<T> getItemClickListener() {
        return this.d;
    }
    
    public void setItemClickListener(final TuSdkViewHolder.TuSdkViewHolderItemClickListener<T> d) {
        this.d = d;
    }
    
    public TuSdkAdapter() {
        this(0);
    }
    
    public TuSdkAdapter(final int n) {
        this(n, null);
    }
    
    public TuSdkAdapter(final int a, final List<T> b) {
        this.c = -1;
        this.a = a;
        this.b = b;
    }
    
    public T getItem(final int n) {
        if (this.b == null || n < 0 || n >= this.b.size()) {
            return null;
        }
        return this.b.get(n);
    }
    
    public long getItemId(final int n) {
        return n;
    }
    
    public int getItemPosition(final T obj) {
        final List<Object> modeList = (List<Object>)this.getModeList();
        if (modeList != null && modeList.size() > 0) {
            for (int i = 0; i < modeList.size(); ++i) {
                if (modeList.get(i).equals(obj)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public int getItemCount() {
        if (this.b == null) {
            return 0;
        }
        return this.b.size();
    }
    
    public TuSdkViewHolder<T> onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        final TuSdkViewHolder<T> create = TuSdkViewHolder.create(viewGroup, this.a);
        create.setItemClickListener(this.d);
        return create;
    }
    
    public void onBindViewHolder(final TuSdkViewHolder<T> tuSdkViewHolder, final int n) {
        tuSdkViewHolder.setModel(this.getItem(n), n);
        tuSdkViewHolder.setSelectedPosition(this.c);
    }
    
    public void onViewAttachedToWindow(final TuSdkViewHolder<T> tuSdkViewHolder) {
        super.onViewAttachedToWindow(tuSdkViewHolder);
        tuSdkViewHolder.setSelectedPosition(this.c);
    }
    
    public void onViewRecycled(final TuSdkViewHolder<T> tuSdkViewHolder) {
        super.onViewRecycled(tuSdkViewHolder);
        tuSdkViewHolder.viewWillDestory();
    }
    
    public int getSelectedPosition() {
        return this.c;
    }
    
    public void setSelectedPosition(final int c) {
        this.c = c;
    }
}
