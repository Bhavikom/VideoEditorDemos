// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker;

import java.util.Iterator;
import java.util.ArrayList;
//import org.lasque.tusdk.core.type.DownloadTaskStatus;
//import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.DownloadTaskStatus;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkLinearLayout;

import java.util.List;
//import org.lasque.tusdk.core.view.TuSdkLinearLayout;

public abstract class StickerBarViewBase extends TuSdkLinearLayout
{
    private StickerLocalPackage.StickerPackageDelegate a;
    private List<StickerCategory> b;
    private int c;
    
    protected abstract View buildCateButton(final StickerCategory p0, final int p1, final LinearLayout.LayoutParams p2);
    
    protected abstract void selectCateButton(final Integer p0);
    
    protected abstract void refreshCateDatas();
    
    public StickerBarViewBase(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.a = new StickerLocalPackage.StickerPackageDelegate() {
            @Override
            public void onStickerPackageStatusChanged(final StickerLocalPackage stickerLocalPackage, final TuSdkDownloadItem tuSdkDownloadItem, final DownloadTaskStatus downloadTaskStatus) {
                if (tuSdkDownloadItem == null || downloadTaskStatus == null) {
                    return;
                }
                switch (downloadTaskStatus.ordinal()) {
                    case 1:
                    case 2: {
                        StickerBarViewBase.this.refreshCateDatas();
                        break;
                    }
                }
            }
        };
        this.c = -1;
    }
    
    public StickerBarViewBase(final Context context, final AttributeSet set) {
        super(context, set);
        this.a = new StickerLocalPackage.StickerPackageDelegate() {
            @Override
            public void onStickerPackageStatusChanged(final StickerLocalPackage stickerLocalPackage, final TuSdkDownloadItem tuSdkDownloadItem, final DownloadTaskStatus downloadTaskStatus) {
                if (tuSdkDownloadItem == null || downloadTaskStatus == null) {
                    return;
                }
                switch (downloadTaskStatus.ordinal()) {
                    case 1:
                    case 2: {
                        StickerBarViewBase.this.refreshCateDatas();
                        break;
                    }
                }
            }
        };
        this.c = -1;
    }
    
    public StickerBarViewBase(final Context context) {
        super(context);
        this.a = new StickerLocalPackage.StickerPackageDelegate() {
            @Override
            public void onStickerPackageStatusChanged(final StickerLocalPackage stickerLocalPackage, final TuSdkDownloadItem tuSdkDownloadItem, final DownloadTaskStatus downloadTaskStatus) {
                if (tuSdkDownloadItem == null || downloadTaskStatus == null) {
                    return;
                }
                switch (downloadTaskStatus.ordinal()) {
                    case 1:
                    case 2: {
                        StickerBarViewBase.this.refreshCateDatas();
                        break;
                    }
                }
            }
        };
        this.c = -1;
    }
    
    @Override
    public void loadView() {
        StickerLocalPackage.shared().appenDelegate(this.a);
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        StickerLocalPackage.shared().removeDelegate(this.a);
    }
    
    public void loadCategories(List<StickerCategory> categories) {
        this.b = new ArrayList<StickerCategory>();
        final StickerCategory stickerCategory = new StickerCategory();
        stickerCategory.name = "lsq_sticker_cate_all";
        stickerCategory.extendType = StickerCategory.StickerCategoryExtendType.ExtendTypeAll;
        this.b.add(stickerCategory);
        if (categories == null || categories.size() == 0) {
            categories = StickerLocalPackage.shared().getCategories();
        }
        this.b.addAll(categories);
        this.a();
        this.selectCateIndex(0);
    }
    
    private void a() {
        if (this.b == null) {
            return;
        }
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -1, 1.0f);
        int n = 0;
        final Iterator<StickerCategory> iterator = this.b.iterator();
        while (iterator.hasNext()) {
            this.buildCateButton(iterator.next(), n, layoutParams);
            ++n;
        }
    }
    
    protected void selectCateIndex(final int n) {
        if (this.c == n || this.b == null || this.b.size() <= n) {
            return;
        }
        this.c = n;
        this.selectCateButton(n);
        this.refreshCateDatas();
    }
    
    protected StickerCategory getCurrentCate() {
        if (this.c < 0 || this.b == null || this.b.size() <= this.c) {
            return null;
        }
        return this.b.get(this.c);
    }
    
    protected List<StickerData> getStickerDatas(final long n) {
        final StickerCategory category = StickerLocalPackage.shared().getCategory(n);
        if (category == null || category.datas == null) {
            return null;
        }
        final ArrayList<StickerData> list = new ArrayList<StickerData>();
        for (final StickerGroup stickerGroup : category.datas) {
            if (stickerGroup.stickers == null) {
                continue;
            }
            list.addAll(stickerGroup.stickers);
        }
        return (List<StickerData>)list;
    }
    
    protected List<StickerData> getAllStickerDatas() {
        if (this.b == null) {
            return null;
        }
        final ArrayList<StickerData> list = new ArrayList<StickerData>();
        for (final StickerCategory stickerCategory : this.b) {
            if (stickerCategory.extendType != null) {
                continue;
            }
            final List<StickerData> stickerDatas = this.getStickerDatas(stickerCategory.id);
            if (stickerDatas == null) {
                continue;
            }
            list.addAll(stickerDatas);
        }
        return (List<StickerData>)list;
    }
}
