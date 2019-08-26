// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ArrayHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkIndexPath;

//import org.lasque.tusdk.core.utils.ArrayHelper;
import java.util.ArrayList;
import java.util.List;
//import org.lasque.tusdk.core.view.listview.TuSdkIndexPath;

public abstract class StickerListDataSource implements TuSdkIndexPath.TuSdkDataSource
{
    private List<TuSdkIndexPath> a;
    private List<StickerGroup> b;
    private List<List<StickerData>> c;
    private int d;
    
    public StickerListDataSource(final StickerCategory stickerCategory) {
        this.a(stickerCategory);
    }
    
    private void a(final StickerCategory stickerCategory) {
        if (stickerCategory == null || stickerCategory.datas == null) {
            return;
        }
        final List<StickerGroup> datas = stickerCategory.datas;
        final ArrayList<List<StickerData>> c = new ArrayList<List<StickerData>>();
        final ArrayList<TuSdkIndexPath> a = new ArrayList<TuSdkIndexPath>();
        this.d = 0;
        int n = 0;
        for (final StickerGroup stickerGroup : datas) {
            if (stickerGroup.stickers == null) {
                continue;
            }
            a.add(new TuSdkIndexPath(n, -1, 1));
            this.d += stickerGroup.stickers.size();
            for (final List<StickerData> list : ArrayHelper.splitForGroupsize(stickerGroup.stickers, 4)) {
                a.add(new TuSdkIndexPath(n, c.size(), 0));
                c.add(list);
            }
            ++n;
        }
        this.b = datas;
        this.c = c;
        this.a = a;
    }
    
    @Override
    public List<TuSdkIndexPath> getIndexPaths() {
        if (this.a == null) {
            this.a = new ArrayList<TuSdkIndexPath>(0);
        }
        return this.a;
    }
    
    @Override
    public TuSdkIndexPath getIndexPath(final int n) {
        if (this.a == null || n >= this.a.size() || n < 0) {
            return null;
        }
        return this.a.get(n);
    }
    
    @Override
    public int viewTypes() {
        return 2;
    }
    
    @Override
    public int sectionCount() {
        if (this.b != null) {
            return this.b.size();
        }
        return 1;
    }
    
    @Override
    public int rowCount(final int n) {
        if (this.c != null) {
            return this.c.get(n).size();
        }
        return 0;
    }
    
    @Override
    public int count() {
        return this.d;
    }
    
    @Override
    public Object getItem(final TuSdkIndexPath tuSdkIndexPath) {
        if (tuSdkIndexPath.viewType == 0 && this.c != null) {
            return this.c.get(tuSdkIndexPath.row);
        }
        if (tuSdkIndexPath.viewType == 1 && this.b != null) {
            return this.b.get(tuSdkIndexPath.section);
        }
        return null;
    }
}
