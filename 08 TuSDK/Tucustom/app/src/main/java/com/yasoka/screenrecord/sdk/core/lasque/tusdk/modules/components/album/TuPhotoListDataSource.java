// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.album;

//import org.lasque.tusdk.core.utils.ArrayHelper;
import java.util.Hashtable;
//import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
//import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import android.content.Context;
//import org.lasque.tusdk.core.utils.TuSdkDate;
//import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ArrayHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkDate;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.listview.TuSdkIndexPath;

import java.util.ArrayList;
import java.util.List;
//import org.lasque.tusdk.core.view.listview.TuSdkIndexPath;

public abstract class TuPhotoListDataSource implements TuSdkIndexPath.TuSdkDataSource
{
    private List<TuSdkIndexPath> a;
    private ArrayList<ImageSqlInfo> b;
    private ArrayList<TuSdkDate> c;
    private List<List<ImageSqlInfo>> d;
    
    public TuPhotoListDataSource(final Context context, final AlbumSqlInfo albumSqlInfo, final boolean b) {
        if (albumSqlInfo == null) {
            return;
        }
        this.a(this.b = ImageSqlHelper.getPhotoList(context, albumSqlInfo.id));
    }
    
    private void a(final ArrayList<ImageSqlInfo> list) {
        if (this.b == null) {
            return;
        }
        final Hashtable<Long, ArrayList<ImageSqlInfo>> hashtable = new Hashtable<Long, ArrayList<ImageSqlInfo>>();
        final ArrayList<TuSdkDate> list2 = new ArrayList<TuSdkDate>();
        for (final ImageSqlInfo e : list) {
            final TuSdkDate beginingOfDay = TuSdkDate.create(e.createDate).beginingOfDay();
            ArrayList<ImageSqlInfo> value = hashtable.get(beginingOfDay.getTimeInMillis());
            if (value == null) {
                list2.add(beginingOfDay);
                value = new ArrayList<ImageSqlInfo>();
                hashtable.put(beginingOfDay.getTimeInMillis(), value);
            }
            value.add(e);
        }
        this.buildIndexPaths(hashtable, list2);
    }
    
    protected void buildIndexPaths(final Hashtable<Long, ArrayList<ImageSqlInfo>> hashtable, final ArrayList<TuSdkDate> c) {
        if (hashtable == null) {
            return;
        }
        final ArrayList<List<ImageSqlInfo>> d = new ArrayList<List<ImageSqlInfo>>();
        final ArrayList<TuSdkIndexPath> a = new ArrayList<TuSdkIndexPath>();
        int n = 0;
        for (final TuSdkDate tuSdkDate : c) {
            a.add(new TuSdkIndexPath(n, -1, 1));
            for (final List<ImageSqlInfo> list : ArrayHelper.splitForGroupsize(hashtable.get(tuSdkDate.getTimeInMillis()), 4)) {
                a.add(new TuSdkIndexPath(n, d.size(), 0));
                d.add(list);
            }
            ++n;
        }
        this.c = c;
        this.d = d;
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
        if (this.c != null) {
            return this.c.size();
        }
        return 1;
    }
    
    @Override
    public int rowCount(final int n) {
        if (this.d != null) {
            return this.d.get(n).size();
        }
        return 0;
    }
    
    @Override
    public int count() {
        if (this.b != null) {
            return this.b.size();
        }
        return 0;
    }
    
    @Override
    public Object getItem(final TuSdkIndexPath tuSdkIndexPath) {
        if (tuSdkIndexPath.viewType == 0 && this.d != null) {
            return this.d.get(tuSdkIndexPath.row);
        }
        if (tuSdkIndexPath.viewType == 1 && this.c != null) {
            return this.c.get(tuSdkIndexPath.section);
        }
        return null;
    }
}
