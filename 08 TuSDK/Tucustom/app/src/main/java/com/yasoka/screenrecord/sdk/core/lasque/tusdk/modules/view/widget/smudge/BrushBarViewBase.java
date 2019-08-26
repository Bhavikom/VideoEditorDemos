// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge;

//import org.lasque.tusdk.core.TuSdkContext;
import java.util.Iterator;
import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.type.DownloadTaskStatus;
//import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.DownloadTaskStatus;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;

import java.util.List;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public abstract class BrushBarViewBase extends TuSdkRelativeLayout {
    private BrushLocalPackage.BrushLocalPackageDelegate a;
    private BrushTableViewInterface.BrushAction b;
    private List<String> c;
    private boolean d;

    public abstract <T extends View & BrushTableViewInterface> T getTableView();

    protected abstract void notifySelectedBrush(BrushData var1);

    protected abstract void refreshBrushDatas();

    public BrushBarViewBase(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        //this.a = new NamelessClass_1();
        this.d = true;
    }

    public BrushBarViewBase(Context var1, AttributeSet var2) {
        super(var1, var2);
        //this.a = new NamelessClass_1();
        this.d = true;
    }

    public BrushBarViewBase(Context var1) {
        super(var1);

        class NamelessClass_1 implements BrushLocalPackage.BrushLocalPackageDelegate {
            NamelessClass_1() {
            }

            public void onBrushPackageStatusChanged(BrushLocalPackage var1, TuSdkDownloadItem var2, DownloadTaskStatus var3) {
                if (var2 != null && var3 != null) {
                    switch(var3.ordinal()) {
                        case 1:
                        case 2:
                            BrushBarViewBase.this.refreshBrushDatas();
                        default:
                    }
                }
            }
        }

        this.a = new NamelessClass_1();
        this.d = true;
    }

    public void loadView() {
        BrushLocalPackage.shared().appenDelegate(this.a);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BrushLocalPackage.shared().removeDelegate(this.a);
    }

    public void loadBrushes() {
        if (!SdkValid.shared.smudgeEnabled()) {
            TLog.e("You are not allowed to use the smudge feature, please see http://tusdk.com", new Object[0]);
        } else {
            List var1;
            if (this.c != null && this.c.size() != 0) {
                var1 = BrushLocalPackage.shared().getBrushWithCodes(this.c);
                var1.add(0, BrushLocalPackage.shared().getEeaserBrush());
            } else {
                var1 = this.buildBrushItems();
            }

            BrushData var2 = this.a();
            int var3 = -1;
            if (var2 != null) {
                var3 = var1.indexOf(var2);
            }

            if ((var3 == -1 || var2.code.equals("Eraser")) && var1.size() > 1) {
                var2 = (BrushData)var1.get(1);
            }

            if (this.getTableView() != null) {
                ((BrushTableViewInterface)this.getTableView()).setModeList(var1);
                var3 = var1.indexOf(var2);
                ((BrushTableViewInterface)this.getTableView()).setSelectedPosition(var3, true);
                ((BrushTableViewInterface)this.getTableView()).scrollToPosition(var3);
            }

            this.notifySelectedBrush(var2);
        }
    }

    protected List<BrushData> buildBrushItems() {
        ArrayList var1 = new ArrayList();
        var1.add(BrushLocalPackage.shared().getEeaserBrush());
        List var2 = BrushLocalPackage.shared().getCodes();
        if (var2 != null && var2.size() > 0) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
                String var4 = (String)var3.next();
                var1.add(BrushLocalPackage.shared().getBrushWithCode(var4));
            }
        }

        return var1;
    }

    public void selectBrush(BrushData var1, BrushBarItemCellBase var2, int var3) {
        ((BrushTableViewInterface)this.getTableView()).changeSelectedPosition(var3);
        ((BrushTableViewInterface)this.getTableView()).smoothScrollByCenter(var2);
        if (var1 != null) {
            this.a(var1);
        }

    }

    private BrushData a() {
        if (!this.d) {
            return null;
        } else {
            String var1 = String.format("lsq_lastbrush_%s", this.b);
            String var2 = TuSdkContext.sharedPreferences().loadSharedCache(var1);
            return var2 != null ? BrushLocalPackage.shared().getBrushWithCode(var2) : null;
        }
    }

    private void a(BrushData var1) {
        if (var1 != null) {
            String var2 = var1.code;
            if (this.d) {
                String var3 = String.format("lsq_lastbrush_%s", this.b);
                TuSdkContext.sharedPreferences().saveSharedCache(var3, var2);
            }
        }
    }

    public BrushTableViewInterface.BrushAction getAction() {
        return this.b;
    }

    public void setAction(BrushTableViewInterface.BrushAction var1) {
        this.b = var1;
    }

    public List<String> getBrushGroup() {
        return this.c;
    }

    public void setBrushGroup(List<String> var1) {
        this.c = var1;
    }

    public boolean isSaveLastBrush() {
        return this.d;
    }

    public void setSaveLastBrush(boolean var1) {
        this.d = var1;
    }
}
