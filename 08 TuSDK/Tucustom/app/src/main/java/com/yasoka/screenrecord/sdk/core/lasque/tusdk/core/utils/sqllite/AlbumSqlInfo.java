// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import android.database.Cursor;
//import org.lasque.tusdk.core.utils.image.AlbumHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.AlbumHelper;

import java.io.File;

public class AlbumSqlInfo extends SqlLiteInfo
{
    public static final String BUCKET_TOTAL = "bucket_total";
    public static final String CAMERA_FOLDER = "Camera";
    public long id;
    public String title;
    public int total;
    public ImageSqlInfo cover;
    
    public static File cameraFolder() {
        return AlbumHelper.getAblumPath("Camera");
    }
    
    public AlbumSqlInfo() {
    }
    
    public AlbumSqlInfo(final Cursor cursor) {
        super(cursor);
    }
    
    @Override
    public void setInfoWithCursor(final Cursor cursor) {
        if (cursor == null) {
            return;
        }
        this.id = this.getCursorLong(cursor, "bucket_id");
        this.title = this.getCursorString(cursor, "bucket_display_name");
        this.total = this.getCursorInt(cursor, "bucket_total");
    }
    
    @Override
    public String toString() {
        return String.format("{id: %s, title: %s, total: %s, cover: %s}", this.id, this.title, this.total, this.cover);
    }
    
    public static void sortTitle(final ArrayList<AlbumSqlInfo> list) {
        if (list == null) {
            return;
        }
        Collections.sort(list, new Comparator<AlbumSqlInfo>() {
            @Override
            public int compare(final AlbumSqlInfo albumSqlInfo, final AlbumSqlInfo albumSqlInfo2) {
                if (albumSqlInfo.title == null || albumSqlInfo2.title == null) {
                    return -1;
                }
                return albumSqlInfo.title.compareToIgnoreCase(albumSqlInfo2.title);
            }
        });
    }
}
