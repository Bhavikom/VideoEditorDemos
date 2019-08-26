// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task.ImageViewTask;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task.ImageViewTaskWare;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.DownloadTaskStatus;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import org.json.JSONArray;
import org.json.JSONObject;
//import org.lasque.tusdk.core.utils.FileHelper;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.TuSdkContext;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
//import org.lasque.tusdk.core.type.DownloadTaskStatus;
import java.util.List;
//import org.lasque.tusdk.core.task.ImageViewTask;
//import org.lasque.tusdk.core.task.ImageViewTaskWare;

public abstract class TuSdkDownloadAdapter<T extends ImageViewTaskWare> extends ImageViewTask<T>
{
    private List<TuSdkDownloadItem> a;
    private TuSdkDownloadManger.TuSdkDownloadMangerDelegate b;
    private TuSdkDownloadTask.DownloadTaskType c;
    private TuSdkDownloadManger.TuSdkDownloadMangerDelegate d;
    
    public TuSdkDownloadAdapter() {
        this.d = new TuSdkDownloadManger.TuSdkDownloadMangerDelegate() {
            @Override
            public void onDownloadMangerStatusChanged(final TuSdkDownloadManger tuSdkDownloadManger, final TuSdkDownloadItem tuSdkDownloadItem, final DownloadTaskStatus downloadTaskStatus) {
                if (tuSdkDownloadItem.type != TuSdkDownloadAdapter.this.getDownloadTaskType()) {
                    return;
                }
                TuSdkDownloadAdapter.this.a(tuSdkDownloadItem, downloadTaskStatus);
            }
        };
    }
    
    protected List<TuSdkDownloadItem> getDownloadItems() {
        return this.a;
    }
    
    protected void setDownloadItems(final List<TuSdkDownloadItem> a) {
        this.a = a;
    }
    
    protected TuSdkDownloadTask.DownloadTaskType getDownloadTaskType() {
        return this.c;
    }
    
    protected void setDownloadTaskType(final TuSdkDownloadTask.DownloadTaskType c) {
        this.c = c;
    }
    
    public TuSdkDownloadManger.TuSdkDownloadMangerDelegate getDownloadDelegate() {
        return this.b;
    }
    
    public void setDownloadDelegate(final TuSdkDownloadManger.TuSdkDownloadMangerDelegate b) {
        this.b = b;
    }
    
    public abstract boolean containsGroupId(final long p0);
    
    protected void asyncLoadDownloadDatas() {
        final Iterator<TuSdkDownloadItem> iterator = new ArrayList<TuSdkDownloadItem>(this.a).iterator();
        while (iterator.hasNext()) {
            this.appendDownload(iterator.next());
        }
    }
    
    private String a() {
        return String.format("%s-downloads", this.getClass());
    }
    
    protected void tryLoadTaskDataWithCache() {
        TuSdkDownloadManger.ins.appenDelegate(this.d);
        this.a = new ArrayList<TuSdkDownloadItem>();
        final ArrayList<TuSdkDownloadItem> list = TuSdkContext.sharedPreferences().loadSharedCacheObject(this.a());
        if (list == null || list.isEmpty()) {
            return;
        }
        for (final TuSdkDownloadItem tuSdkDownloadItem : list) {
            if (tuSdkDownloadItem.localDownloadPath().exists()) {
                this.a.add(tuSdkDownloadItem);
            }
        }
        TLog.d("download %s: %s", this.getDownloadTaskType().getAct(), list.size());
        this.trySaveTaskDataInCache();
    }
    
    protected void trySaveTaskDataInCache() {
        TuSdkContext.sharedPreferences().saveSharedCacheObject(this.a(), new ArrayList(this.a));
    }
    
    public void download(final long n, final String s, final String s2) {
        if (this.containsGroupId(n)) {
            return;
        }
        TuSdkDownloadManger.ins.appenTask(this.getDownloadTaskType(), n, s, s2);
    }
    
    public void cancelDownload(final long n) {
        if (!this.containsGroupId(n)) {
            return;
        }
        TuSdkDownloadManger.ins.cancelTask(this.getDownloadTaskType(), n);
    }
    
    public void removeDownloadWithIdt(final long var1) {
        if (this.containsGroupId(var1)) {
            this.removeDownloadData(var1);
            ArrayList var3 = new ArrayList(this.a);
            TuSdkDownloadItem var4 = null;
            Iterator var5 = var3.iterator();

            while(var5.hasNext()) {
                TuSdkDownloadItem var6 = (TuSdkDownloadItem)var5.next();
                if (var6.id == var1) {
                    var4 = var6;
                    this.a.remove(var6);
                    FileHelper.delete(var6.localDownloadPath());
                }
            }

            this.trySaveTaskDataInCache();
            if (var4 != null) {
                this.b(var4, DownloadTaskStatus.StatusRemoved);
            }
        }
    }
    
    protected abstract void removeDownloadData(final long p0);
    
    private void a(final TuSdkDownloadItem tuSdkDownloadItem, final DownloadTaskStatus downloadTaskStatus) {
        if (downloadTaskStatus == DownloadTaskStatus.StatusDowned && this.appendDownload(tuSdkDownloadItem)) {
            this.a.add(tuSdkDownloadItem);
            this.trySaveTaskDataInCache();
        }
        this.b(tuSdkDownloadItem, downloadTaskStatus);
    }
    
    private void b(final TuSdkDownloadItem tuSdkDownloadItem, final DownloadTaskStatus downloadTaskStatus) {
        if (this.getDownloadDelegate() == null) {
            return;
        }
        this.getDownloadDelegate().onDownloadMangerStatusChanged(null, tuSdkDownloadItem, downloadTaskStatus);
    }
    
    protected boolean appendDownload(final TuSdkDownloadItem tuSdkDownloadItem) {
        return tuSdkDownloadItem != null && tuSdkDownloadItem.localDownloadPath().exists() && !this.containsGroupId(tuSdkDownloadItem.id) && tuSdkDownloadItem.key != null;
    }
    
    protected abstract Collection<?> getAllGroupID();
    
    public JSONObject getAllDatas() {
        return TuSdkDownloadManger.ins.getAllDatas(this.getDownloadTaskType(), new JSONArray((Collection)this.getAllGroupID()));
    }
}
