// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker;

//import org.lasque.tusdk.core.type.DownloadTaskStatus;
//import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.json.JSONObject;
import java.io.File;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadItem;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network.TuSdkDownloadManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StickerAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.DownloadTaskStatus;

import java.util.Iterator;
import java.util.ArrayList;
//import org.lasque.tusdk.core.TuSdkConfigs;
import java.util.List;
//import org.lasque.tusdk.core.secret.StickerAdapter;
//import org.lasque.tusdk.core.network.TuSdkDownloadManger;

public class StickerLocalPackage implements TuSdkDownloadManger.TuSdkDownloadMangerDelegate
{
    private static StickerLocalPackage a;
    private StickerAdapter b;
    private List<StickerPackageDelegate> c;
    
    public static StickerLocalPackage shared() {
        return StickerLocalPackage.a;
    }
    
    public static StickerLocalPackage init(final TuSdkConfigs tuSdkConfigs) {
        if (StickerLocalPackage.a == null && tuSdkConfigs != null) {
            StickerLocalPackage.a = new StickerLocalPackage(tuSdkConfigs);
        }
        return StickerLocalPackage.a;
    }
    
    public void appenDelegate(final StickerPackageDelegate stickerPackageDelegate) {
        if (stickerPackageDelegate == null || this.c.contains(stickerPackageDelegate)) {
            return;
        }
        this.c.add(stickerPackageDelegate);
    }
    
    public void removeDelegate(final StickerPackageDelegate stickerPackageDelegate) {
        if (stickerPackageDelegate == null) {
            return;
        }
        this.c.remove(stickerPackageDelegate);
    }
    
    public boolean isInited() {
        return this.b.isInited();
    }
    
    public List<StickerCategory> getCategories() {
        return this.b.getCategories();
    }
    
    public List<StickerCategory> getCategories(final List<StickerCategory> list) {
        return this.b.getCategories(list);
    }
    
    public StickerCategory getCategory(final long n) {
        return this.b.getCategory(n);
    }
    
    public StickerGroup getStickerGroup(final long n) {
        return this.b.getStickerGroup(n);
    }
    
    public StickerData getSticker(final long n) {
        return this.b.getSticker(n);
    }
    
    public boolean containsGroupId(final long n) {
        return this.b.containsGroupId(n);
    }
    
    public List<StickerGroup> getSmartStickerGroups() {
        return this.getSmartStickerGroups(true);
    }
    
    public List<StickerGroup> getSmartStickerGroups(final boolean b) {
        final List<StickerGroup> groupListByType = this.b.getGroupListByType(StickerCategory.StickerCategoryType.StickerCategorySmart);
        if (groupListByType == null) {
            return null;
        }
        final ArrayList<StickerGroup> list = new ArrayList<StickerGroup>();
        for (final StickerGroup stickerGroup : groupListByType) {
            if (b || (!b && !stickerGroup.requireFaceFeature())) {
                list.add(stickerGroup);
            }
        }
        return list;
    }
    
    private StickerLocalPackage(final TuSdkConfigs tuSdkConfigs) {
        this.c = new ArrayList<StickerPackageDelegate>();
        (this.b = new StickerAdapter(tuSdkConfigs)).setDownloadDelegate(this);
    }
    
    public void loadThumb(final StickerData stickerData, final ImageView imageView) {
        this.b.loadThumb(stickerData, imageView);
    }
    
    public void loadGroupThumb(final StickerGroup stickerGroup, final ImageView imageView) {
        this.b.loadGroupThumb(stickerGroup, imageView);
    }
    
    public boolean loadStickerItem(final StickerData stickerData) {
        return this.b.loadStickerItem(stickerData);
    }
    
    public Bitmap loadSmartStickerItem(final StickerData stickerData, final String s) {
        return this.b.loadSmartStickerItem(stickerData, s);
    }
    
    public boolean addStickerGroupFile(final File file, final long n, final String s) {
        return this.b.addStickerGroupFile(file, n, s);
    }
    
    public void download(final long n, final String s, final String s2) {
        this.b.download(n, s, s2);
    }
    
    public void cancelDownload(final long n) {
        this.b.cancelDownload(n);
    }
    
    public void removeDownloadWithIdt(final long n) {
        this.b.removeDownloadWithIdt(n);
    }
    
    public JSONObject getAllDatas() {
        return this.b.getAllDatas();
    }
    
    public void cancelLoadImage(final ImageView imageView) {
        this.b.cancelLoadImage(imageView);
    }
    
    @Override
    public void onDownloadMangerStatusChanged(final TuSdkDownloadManger tuSdkDownloadManger, final TuSdkDownloadItem tuSdkDownloadItem, final DownloadTaskStatus downloadTaskStatus) {
        final Iterator<StickerPackageDelegate> iterator = new ArrayList<StickerPackageDelegate>(this.c).iterator();
        while (iterator.hasNext()) {
            iterator.next().onStickerPackageStatusChanged(this, tuSdkDownloadItem, downloadTaskStatus);
        }
    }
    
    public interface StickerPackageDelegate
    {
        void onStickerPackageStatusChanged(final StickerLocalPackage p0, final TuSdkDownloadItem p1, final DownloadTaskStatus p2);
    }
}
