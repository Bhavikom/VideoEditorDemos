// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite;

import android.annotation.TargetApi;
import android.os.Build;
//import org.lasque.tusdk.core.utils.DateHelper;
import android.database.Cursor;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.DateHelper;

import java.util.Calendar;

public class ImageSqlInfo extends SqlLiteInfo
{
    public long id;
    public long albumId;
    public int orientation;
    public Calendar createDate;
    public String path;
    public long length;
    public TuSdkSize size;
    public String name;
    
    public ImageSqlInfo() {
    }
    
    public ImageSqlInfo(final Cursor cursor) {
        super(cursor);
    }
    
    @Override
    public void setInfoWithCursor(final Cursor cursor) {
        if (cursor == null) {
            return;
        }
        this.id = this.getCursorLong(cursor, "_id");
        this.path = this.getCursorString(cursor, "_data");
        this.orientation = this.getCursorInt(cursor, "orientation");
        this.createDate = DateHelper.parseCalendar(this.getCursorLong(cursor, "date_modified"));
        this.albumId = this.getCursorLong(cursor, "bucket_id");
        this.length = this.getCursorLong(cursor, "_size");
        if (Build.VERSION.SDK_INT > 15) {
            this.a(cursor);
        }
    }
    
    @TargetApi(16)
    private void a(final Cursor cursor) {
        this.size = new TuSdkSize(this.getCursorInt(cursor, "width"), this.getCursorInt(cursor, "height"));
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ImageSqlInfo) {
            return this.id == ((ImageSqlInfo)obj).id;
        }
        return super.equals(obj);
    }
    
    @Override
    public String toString() {
        return String.format("{id: %s, albumId: %s, orientation: %s, createDate: %s, length: %s, path: %s, name: %s, size: %s}", this.id, this.albumId, this.orientation, this.createDate, this.length, this.path, this.name, this.size);
    }
    
    public String identify() {
        return String.format("%s_%s_%s", this.path, this.id, this.albumId);
    }
}
