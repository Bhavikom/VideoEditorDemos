// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.paintdraw;

import android.graphics.Color;
import java.util.ArrayList;
import java.util.Iterator;
//import org.lasque.tusdk.core.TuSdkContext;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;

import java.util.List;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class PaintBarViewBase extends TuSdkRelativeLayout {
    private List<PaintData> a;
    private List<String> b;
    private boolean c = true;

    public abstract <T extends View & PaintTableViewInterface> T getTableView();

    protected abstract void notifySelectedPaint(PaintData var1);

    protected abstract void refreshPaintDatas();

    public PaintBarViewBase(Context var1) {
        super(var1);
    }

    public PaintBarViewBase(Context var1, AttributeSet var2) {
        super(var1, var2);
    }

    public PaintBarViewBase(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    public void loadPaints() {
        List var1 = null;
        if (this.a == null || this.a.size() == 0) {
            var1 = this.buildPaintItems();
            this.a = var1;
        }

        PaintData var2 = null;
        int var3 = this.a(PaintData.PaintType.Color);
        if (var3 == -1) {
            if (!var1.isEmpty()) {
                var2 = (PaintData)var1.get(1);
            }
        } else {
            var2 = (PaintData)var1.get(var3);
        }

        if (this.getTableView() != null) {
            ((PaintTableViewInterface)this.getTableView()).setModeList(var1);
            var3 = var1.indexOf(var2);
            ((PaintTableViewInterface)this.getTableView()).setSelectedPosition(var3, true);
            ((PaintTableViewInterface)this.getTableView()).scrollToPosition(var3);
        }

        this.notifySelectedPaint(var2);
    }

    private int a(PaintData.PaintType var1) {
        int var2 = -1;
        if (this.c) {
            String var3 = String.format("lsq_lastpaint_%s", var1);
            String var4 = TuSdkContext.sharedPreferences().loadSharedCache(var3);
            if (var4 != null) {
                Iterator var5 = this.a.iterator();

                while(var5.hasNext()) {
                    PaintData var6 = (PaintData)var5.next();
                    if (((Integer)var6.getData()).equals(Integer.valueOf(var4))) {
                        var2 = this.a.indexOf(var6);
                        break;
                    }
                }
            }
        }

        return var2;
    }

    protected List<PaintData> buildPaintItems() {
        ArrayList var1 = new ArrayList();
        if (this.b != null && !this.b.isEmpty()) {
            try {
                Iterator var2 = this.b.iterator();

                while(var2.hasNext()) {
                    String var3 = (String)var2.next();
                    var1.add(new PaintData(Color.parseColor(var3), PaintData.PaintType.Color));
                }
            } catch (Exception var4) {
                var1.clear();
                this.b.clear();
                this.a((List)var1);
            }
        } else {
            this.a((List)var1);
        }

        return var1;
    }

    private void a(List<PaintData> var1) {
        var1.add(new PaintData(Color.parseColor("#f9f9f9"), PaintData.PaintType.Color));
        var1.add(new PaintData(Color.parseColor("#2b2b2b"), PaintData.PaintType.Color));
        var1.add(new PaintData(Color.parseColor("#ff1d12"), PaintData.PaintType.Color));
        var1.add(new PaintData(Color.parseColor("#fbf606"), PaintData.PaintType.Color));
        var1.add(new PaintData(Color.parseColor("#14e213"), PaintData.PaintType.Color));
        var1.add(new PaintData(Color.parseColor("#199bff"), PaintData.PaintType.Color));
        var1.add(new PaintData(Color.parseColor("#8c06ff"), PaintData.PaintType.Color));
    }

    protected void addColorItem(PaintData var1) {
        this.a.add(var1);
        this.refreshPaintDatas();
    }

    protected void addBrushGroup(List<String> var1) {
        this.b = var1;
    }

    public void clearColors() {
        if (this.a != null) {
            this.a.clear();
            this.refreshPaintDatas();
        }
    }

    public void selectPaint(PaintData var1, PaintDrawBarItemCellBase var2, int var3) {
        ((PaintTableViewInterface)this.getTableView()).changeSelectedPosition(var3);
        ((PaintTableViewInterface)this.getTableView()).smoothScrollByCenter(var2);
        if (var1 != null) {
            this.a(var1);
        }

    }

    private void a(PaintData var1) {
        if (var1 != null) {
            String var2 = String.valueOf(var1.getData());
            if (this.c) {
                String var3 = String.format("lsq_lastpaint_%s", var1.getPaintType());
                TuSdkContext.sharedPreferences().saveSharedCache(var3, var2);
            }
        }
    }

    public List<PaintData> getCurrentColors() {
        return this.a;
    }

    public boolean isSaveLastPaint() {
        return this.c;
    }

    public void setSaveLastPaint(boolean var1) {
        this.c = var1;
    }
}
