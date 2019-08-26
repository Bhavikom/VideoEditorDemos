// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.DownloadTaskStatus;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.TuSdkContext;
import java.util.Iterator;
//import org.lasque.tusdk.core.type.DownloadTaskStatus;
import java.util.ArrayList;
import java.util.List;

public class TuSdkDownloadManger
{
    public static final TuSdkDownloadManger ins;
    private List<TuSdkDownloadTask> a;
    private TuSdkDownloadTask b;
    private List<TuSdkDownloadMangerDelegate> c;
    private TuSdkDownloadTask.TuSdkDownloadTaskDelegate d;
    
    public void appenDelegate(final TuSdkDownloadMangerDelegate tuSdkDownloadMangerDelegate) {
        if (tuSdkDownloadMangerDelegate == null || this.c.contains(tuSdkDownloadMangerDelegate)) {
            return;
        }
        this.c.add(tuSdkDownloadMangerDelegate);
    }
    
    public void removeDelegate(final TuSdkDownloadMangerDelegate tuSdkDownloadMangerDelegate) {
        if (tuSdkDownloadMangerDelegate == null) {
            return;
        }
        this.c.remove(tuSdkDownloadMangerDelegate);
    }
    
    private TuSdkDownloadManger() {
        this.c = new ArrayList<TuSdkDownloadMangerDelegate>();
        this.d = new TuSdkDownloadTask.TuSdkDownloadTaskDelegate() {
            @Override
            public void onDownloadTaskStatusChanged(final TuSdkDownloadTask tuSdkDownloadTask, final DownloadTaskStatus downloadTaskStatus) {
                TuSdkDownloadManger.this.a(tuSdkDownloadTask, downloadTaskStatus);
            }
        };
        this.b();
        this.d();
    }
    
    public boolean isDownloading(final TuSdkDownloadTask.DownloadTaskType downloadTaskType, final long n) {
        for (final TuSdkDownloadTask tuSdkDownloadTask : this.a) {
            if (tuSdkDownloadTask.getItem().type == downloadTaskType && tuSdkDownloadTask.getItem().id == n) {
                return tuSdkDownloadTask.getItem().getStatus() == DownloadTaskStatus.StatusDowning;
            }
        }
        return false;
    }
    
    public boolean containsTask(final TuSdkDownloadTask.DownloadTaskType downloadTaskType, final long n) {
        for (final TuSdkDownloadTask tuSdkDownloadTask : this.a) {
            if (tuSdkDownloadTask.getItem().type == downloadTaskType && tuSdkDownloadTask.getItem().id == n) {
                return tuSdkDownloadTask.getItem().getStatus() == DownloadTaskStatus.StatusInit || tuSdkDownloadTask.getItem().getStatus() == DownloadTaskStatus.StatusDowning;
            }
        }
        return false;
    }
    
    private String a() {
        return String.format("%s-queue", this.getClass());
    }
    
    private void b() {
        this.a = new ArrayList<TuSdkDownloadTask>();
        final ArrayList<TuSdkDownloadItem> list = TuSdkContext.sharedPreferences().loadSharedCacheObject(this.a());
        if (list == null || list.isEmpty()) {
            return;
        }
        final Iterator<TuSdkDownloadItem> iterator = list.iterator();
        while (iterator.hasNext()) {
            final TuSdkDownloadTask tuSdkDownloadTask = new TuSdkDownloadTask(iterator.next());
            if (tuSdkDownloadTask.canRunTask()) {
                this.a.add(tuSdkDownloadTask);
            }
            else {
                tuSdkDownloadTask.clear();
            }
        }
        TLog.d("load download tasks: %s", this.a.size());
    }
    
    private void c() {
        final ArrayList<TuSdkDownloadTask> list = new ArrayList<TuSdkDownloadTask>(this.a);
        final ArrayList<TuSdkDownloadItem> list2 = new ArrayList<TuSdkDownloadItem>(this.a.size());
        for (final TuSdkDownloadTask tuSdkDownloadTask : list) {
            if (tuSdkDownloadTask.canRunTask()) {
                list2.add(tuSdkDownloadTask.getItem());
            }
        }
        TuSdkContext.sharedPreferences().saveSharedCacheObject(this.a(), list2);
    }
    
    private List<TuSdkDownloadItem> a(final TuSdkDownloadTask.DownloadTaskType downloadTaskType) {
        if (downloadTaskType == null) {
            return null;
        }
        final ArrayList<TuSdkDownloadTask> list = new ArrayList<TuSdkDownloadTask>(this.a);
        final ArrayList<TuSdkDownloadItem> list2 = new ArrayList<TuSdkDownloadItem>(this.a.size());
        for (final TuSdkDownloadTask tuSdkDownloadTask : list) {
            if (tuSdkDownloadTask.getItem().type == downloadTaskType) {
                list2.add(tuSdkDownloadTask.getItem());
            }
        }
        return list2;
    }
    
    public JSONObject getAllDatas(final TuSdkDownloadTask.DownloadTaskType downloadTaskType, final JSONArray jsonArray) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("fun", (Object)"init");
            jsonObject.putOpt("type", (Object)downloadTaskType.getAct());
            if (jsonArray != null) {
                jsonObject.putOpt("locals", (Object)jsonArray);
            }
            final JSONArray jsonArray2 = new JSONArray();
            final List<TuSdkDownloadItem> a = this.a(downloadTaskType);
            if (a != null) {
                final Iterator<TuSdkDownloadItem> iterator = a.iterator();
                while (iterator.hasNext()) {
                    jsonArray2.put((Object)iterator.next().buildJson());
                }
                jsonObject.putOpt("queues", (Object)jsonArray2);
            }
        }
        catch (JSONException ex) {
            TLog.e((Throwable)ex, "StickerLocalPackage getAllDatas", new Object[0]);
        }
        return jsonObject;
    }
    
    public void appenTask(final TuSdkDownloadTask.DownloadTaskType type, final long id, final String key, final String fileId) {
        if (type == null || key == null || id < 1L) {
            return;
        }
        this.cancelTask(type, id);
        final TuSdkDownloadItem tuSdkDownloadItem = new TuSdkDownloadItem();
        tuSdkDownloadItem.type = type;
        tuSdkDownloadItem.id = id;
        tuSdkDownloadItem.key = key;
        tuSdkDownloadItem.fileId = fileId;
        this.a.add(new TuSdkDownloadTask(tuSdkDownloadItem));
        this.c();
        this.d();
    }
    
    public boolean cancelTask(final TuSdkDownloadTask.DownloadTaskType downloadTaskType, final long n) {
        final ArrayList<TuSdkDownloadTask> list = new ArrayList<TuSdkDownloadTask>(this.a);
        boolean b = false;
        for (final TuSdkDownloadTask tuSdkDownloadTask : list) {
            if (tuSdkDownloadTask.equals(downloadTaskType, n)) {
                tuSdkDownloadTask.cancel();
                this.a.remove(downloadTaskType);
                b = true;
            }
        }
        if (b) {
            this.c();
        }
        return b;
    }
    
    private void d() {
        if (this.b != null || this.a.isEmpty()) {
            return;
        }
        for (final TuSdkDownloadTask b : new ArrayList<TuSdkDownloadTask>(this.a)) {
            if (b.canRunTask()) {
                (this.b = b).setDelegate(this.d);
                this.b.start();
                break;
            }
            this.a.remove(b);
        }
    }
    
    private void a(final TuSdkDownloadTask tuSdkDownloadTask, final DownloadTaskStatus downloadTaskStatus) {
        if (!tuSdkDownloadTask.canRunTask()) {
            this.b = null;
            this.a.remove(tuSdkDownloadTask);
            this.d();
        }
        this.c();
        this.b(tuSdkDownloadTask, downloadTaskStatus);
    }
    
    private void b(final TuSdkDownloadTask tuSdkDownloadTask, final DownloadTaskStatus downloadTaskStatus) {
        final Iterator<TuSdkDownloadMangerDelegate> iterator = new ArrayList<TuSdkDownloadMangerDelegate>(this.c).iterator();
        while (iterator.hasNext()) {
            iterator.next().onDownloadMangerStatusChanged(this, tuSdkDownloadTask.getItem(), downloadTaskStatus);
        }
    }
    
    static {
        ins = new TuSdkDownloadManger();
    }
    
    public interface TuSdkDownloadMangerDelegate
    {
        void onDownloadMangerStatusChanged(final TuSdkDownloadManger p0, final TuSdkDownloadItem p1, final DownloadTaskStatus p2);
    }
}
