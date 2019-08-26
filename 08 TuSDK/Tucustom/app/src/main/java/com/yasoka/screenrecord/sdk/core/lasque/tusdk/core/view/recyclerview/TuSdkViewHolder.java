// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.recyclerview;

//import org.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;
//import org.lasque.tusdk.core.view.listview.TuSdkCellViewInterface;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.view.ViewGroup;
import android.view.View;
//import org.lasque.tusdk.core.view.TuSdkViewInterface;
import android.support.v7.widget.RecyclerView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkCellViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkListSelectableCellViewInterface;

public class TuSdkViewHolder<T> extends RecyclerView.ViewHolder implements TuSdkViewInterface {
    private View.OnClickListener a = new TuSdkViewHolder.ViewHolderClickListener();
    private TuSdkViewHolder.TuSdkViewHolderItemClickListener<T> b;

    public static <T> TuSdkViewHolder<T> create(ViewGroup var0, int var1) {
        View var2 = TuSdkViewHelper.buildView(var0.getContext(), var1, var0);
        return create(var2);
    }

    public static <T> TuSdkViewHolder<T> create(View var0) {
        TuSdkViewHolder var1 = new TuSdkViewHolder(var0);
        return var1;
    }

    public TuSdkViewHolder.TuSdkViewHolderItemClickListener<T> getItemClickListener() {
        return this.b;
    }

    public void setItemClickListener(TuSdkViewHolder.TuSdkViewHolderItemClickListener<T> var1) {
        this.b = var1;
        if (this.b != null) {
            this.itemView.setOnClickListener(this.a);
        } else {
            this.itemView.setOnClickListener((View.OnClickListener)null);
        }

    }

    public TuSdkViewHolder(View var1) {
        super(var1);
        this.viewNeedRest();
    }

    public void setModel(T var1, int var2) {
        this.viewNeedRest();
        this.itemView.setTag(var2);
        if (this.itemView instanceof TuSdkCellViewInterface) {
            ((TuSdkCellViewInterface)this.itemView).setModel(var1);
        }

    }

    public T getModel() {
        return this.itemView instanceof TuSdkCellViewInterface ? (T) ((TuSdkCellViewInterface) this.itemView).getModel() : null;
    }

    public void loadView() {
        if (this.itemView instanceof TuSdkViewInterface) {
            ((TuSdkViewInterface)this.itemView).loadView();
        }

    }

    public void viewDidLoad() {
        if (this.itemView instanceof TuSdkViewInterface) {
            ((TuSdkViewInterface)this.itemView).viewDidLoad();
        }

    }

    public void viewNeedRest() {
        if (this.itemView instanceof TuSdkViewInterface) {
            ((TuSdkViewInterface)this.itemView).viewNeedRest();
        }

    }

    public void viewWillDestory() {
        if (this.itemView instanceof TuSdkViewInterface) {
            ((TuSdkViewInterface)this.itemView).viewWillDestory();
        }

    }

    protected void onViewHolderItemClick(View var1) {
        if (this.b != null) {
            this.b.onViewHolderItemClick(this);
        }
    }

    public void setSelectedPosition(int var1) {
        if (var1 >= 0) {
            if (this.itemView instanceof TuSdkListSelectableCellViewInterface) {
                boolean var2 = var1 == this.getPosition();
                if (var2) {
                    ((TuSdkListSelectableCellViewInterface)this.itemView).onCellSelected(var1);
                } else {
                    ((TuSdkListSelectableCellViewInterface)this.itemView).onCellDeselected();
                }
            }

        }
    }

    protected class ViewHolderClickListener implements View.OnClickListener {
        protected ViewHolderClickListener() {
        }

        public void onClick(View var1) {
            TuSdkViewHolder.this.onViewHolderItemClick(var1);
        }
    }

    public interface TuSdkViewHolderItemClickListener<T> {
        void onViewHolderItemClick(TuSdkViewHolder<T> var1);
    }
}
