// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.view.widget.listview;

//import org.lasque.tusdk.core.view.TuSdkViewInterface;
//import org.lasque.tusdk.core.utils.ContextUtils;
import android.util.AttributeSet;
//import org.lasque.tusdk.core.view.listview.TuSdkCellViewInterface;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
import android.content.Context;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkCellLinearLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkCellViewInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//import org.lasque.tusdk.core.view.listview.TuSdkCellLinearLayout;

public class TuListGridCellView<T, V extends View & TuSdkCellViewInterface<T>> extends TuSdkCellLinearLayout<List<T>> {
    private ArrayList<V> a = new ArrayList();
    private TuListGridCellView.TuListGridCellViewDelegate<T, V> b;
    private OnClickListener c;

    public TuListGridCellView(Context var1) {
        super(var1);
        //this.c = new NamelessClass_1();
    }

    public TuListGridCellView(Context var1, AttributeSet var2) {
        super(var1, var2);
        //this.c = new NamelessClass_1();
    }

    public TuListGridCellView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);

        /*class NamelessClass_1 extends TuSdkViewHelper.OnSafeClickListener {
            NamelessClass_1() {
            }

            public void onSafeClick(View var1) {
                if (TuListGridCellView.this.b != null && var1 instanceof TuSdkCellViewInterface) {
                    TuListGridCellView.this.b.onGridItemClick(var1, ((TuSdkCellViewInterface)var1).getModel());
                }

            }
        }

        this.c = new NamelessClass_1();*/
    }

    public TuListGridCellView.TuListGridCellViewDelegate<T, V> getGridDelegate() {
        return this.b;
    }

    public void setGridDelegate(TuListGridCellView.TuListGridCellViewDelegate<T, V> var1) {
        this.b = var1;
    }

    protected void onLayouted() {
        int i = 0;
        int j = getChildCount();
        while (i < j)
        {
            View localView = getChildAt(i);
            if ((localView instanceof TuSdkCellViewInterface)) {
                a((V) localView);
            }
            i++;
        }
        setHeight(ContextUtils.getScreenSize(getContext()).width / this.a.size());
        super.onLayouted();
    }

    private void a(V var1) {
        ((TuSdkViewInterface)var1).loadView();
        var1.setOnClickListener(this.c);
        var1.setVisibility(INVISIBLE);
        this.a.add(var1);
    }

    protected void bindModel() {
        List var1 = (List)this.getModel();
        if (var1 != null) {
            int var2 = 0;
            int var3 = this.a.size();

            for(int var4 = var1.size(); var2 < var3; ++var2) {
                View var5 = (View)this.a.get(var2);
                if (var2 < var4) {
                    ((TuSdkCellViewInterface)var5).setModel(var1.get(var2));
                    this.showViewIn(var5, true);
                }
            }

        }
    }

    public void viewNeedRest() {
        super.viewNeedRest();
        Iterator var1 = this.a.iterator();

        while(var1.hasNext()) {
            View var2 = (View)var1.next();
            ((TuSdkViewInterface)var2).viewNeedRest();
            this.showViewIn(var2, false);
        }

    }

    public void viewWillDestory() {
        this.viewNeedRest();
        super.viewWillDestory();
    }

    public interface TuListGridCellViewDelegate<T, V extends View & TuSdkCellViewInterface<T>> {
        void onGridItemClick(V var1, T var2);
    }
}
