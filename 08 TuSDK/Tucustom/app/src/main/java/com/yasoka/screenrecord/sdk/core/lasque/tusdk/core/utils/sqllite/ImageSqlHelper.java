// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite;

import android.content.Intent;
import java.io.OutputStream;
//import org.lasque.tusdk.core.utils.FileHelper;
import java.io.FileNotFoundException;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.secret.TuSdkImageNative;
//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
//import org.lasque.tusdk.core.utils.ContextUtils;
//import org.lasque.tusdk.core.utils.image.AlbumHelper;
import android.annotation.TargetApi;
//import org.lasque.tusdk.core.utils.TuSdkDate;
import android.os.Build;
import android.content.ContentValues;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import java.util.ArrayList;
import android.database.Cursor;
import android.content.ContentResolver;
import java.io.File;
import android.net.Uri;
import android.content.Context;
import android.annotation.SuppressLint;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.TuSdkImageNative;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkDate;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.AlbumHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

public class ImageSqlHelper
{
    @SuppressLint({ "InlinedApi" })
    public static final String[] PHOTOJECTIONS_JELLY_BEAN;
    public static final String[] PHOTOJECTIONS_LOW;
    public static final String[] PHOTOJECTIONS_VIDEO;
    public static final String[] PHOTOJECTIONS;
    
    public static File getLocalImageFile(final Context context, final Uri uri) {
        final ImageSqlInfo imageInfo = getImageInfo(context, uri);
        if (imageInfo == null) {
            return null;
        }
        return new File(imageInfo.path);
    }
    
    public static ImageSqlInfo getImageInfo(final Context context, final Uri uri) {
        return getImageInfo(context, uri, ImageSqlHelper.PHOTOJECTIONS, null, null, null);
    }
    
    public static ImageSqlInfo getImageInfo(final ContentResolver contentResolver, final Uri uri) {
        if (contentResolver == null || uri == null) {
            return null;
        }
        return getImageInfo(contentResolver.query(uri, ImageSqlHelper.PHOTOJECTIONS, (String)null, (String[])null, (String)null));
    }
    
    public static ImageSqlInfo getVideoInfo(final ContentResolver contentResolver, final Uri uri) {
        if (contentResolver == null || uri == null) {
            return null;
        }
        return getImageInfo(contentResolver.query(uri, ImageSqlHelper.PHOTOJECTIONS_VIDEO, (String)null, (String[])null, (String)null));
    }
    
    public static ImageSqlInfo getImageInfo(final Context context, final Uri uri, final String[] array, final String s, final String[] array2, final String s2) {
        return getImageInfo(getWithCursorLoader(context, uri, array, s, array2, s2));
    }
    
    public static ImageSqlInfo getImageInfo(final Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        ImageSqlInfo imageSqlInfo = null;
        if (cursor.moveToFirst()) {
            imageSqlInfo = new ImageSqlInfo(cursor);
        }
        cursor.close();
        return imageSqlInfo;
    }
    
    public static Cursor getWithCursorLoader(final Context context, final Uri uri, final String[] array, final String s, final String[] array2, final String s2) {
        if (context == null || uri == null) {
            return null;
        }
        return context.getContentResolver().query(uri, array, s, (String[])null, s2);
    }
    
    public static ArrayList<AlbumSqlInfo> getAlbumList(final Context context) {
        if (context == null) {
            return null;
        }
        final Cursor withCursorLoader = getWithCursorLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { "bucket_id", "bucket_display_name", "COUNT(*) AS bucket_total" }, "1) GROUP BY bucket_id-- (", null, "bucket_display_name ASC");
        if (withCursorLoader == null || !withCursorLoader.moveToFirst()) {
            return null;
        }
        final ArrayList<AlbumSqlInfo> list = new ArrayList<AlbumSqlInfo>();
        while (!withCursorLoader.isAfterLast()) {
            final AlbumSqlInfo e = new AlbumSqlInfo(withCursorLoader);
            e.cover = getAlbumCoverInfo(context, e.id);
            list.add(e);
            withCursorLoader.moveToNext();
        }
        withCursorLoader.close();
        AlbumSqlInfo.sortTitle(list);
        return list;
    }
    
    public static ArrayList<ImageSqlInfo> getPhotoList(final Context context, final long n) {
        return getPhotoList(context, n, PhotoSortDescriptor.Date_Modified);
    }
    
    public static ArrayList<ImageSqlInfo> getPhotoList(final Context context, final long lng, final PhotoSortDescriptor photoSortDescriptor) {
        if (context == null) {
            return null;
        }
        return getPhotoList(getWithCursorLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ImageSqlHelper.PHOTOJECTIONS, "bucket_id=" + lng, null, photoSortDescriptor.key + (photoSortDescriptor.desc ? " DESC" : " ASC")));
    }
    
    public static ArrayList<ImageSqlInfo> getPhotoList(final ContentResolver contentResolver, final boolean b) {
        if (contentResolver == null) {
            return null;
        }
        return getPhotoList(contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ImageSqlHelper.PHOTOJECTIONS, (String)null, (String[])null, "date_modified" + (b ? " DESC" : " ASC")));
    }
    
    public static ArrayList<ImageSqlInfo> getPhotoList(final Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        final ArrayList<ImageSqlInfo> list = new ArrayList<ImageSqlInfo>();
        while (!cursor.isAfterLast()) {
            list.add(new ImageSqlInfo(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    
    public static ImageSqlInfo getAlbumCoverInfo(final Context context, final long lng) {
        if (context == null || lng == 0L) {
            return null;
        }
        return getImageInfo(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ImageSqlHelper.PHOTOJECTIONS, "bucket_id=" + lng, null, "date_modified DESC");
    }
    
    public static Bitmap getThumbnail(final Context context, final ImageSqlInfo imageSqlInfo, final int n) {
        if (context == null || imageSqlInfo == null || n == 0) {
            return null;
        }
        return BitmapHelper.imageRotaing(MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), imageSqlInfo.id, n, (BitmapFactory.Options)null), ImageOrientation.getValue(imageSqlInfo.orientation, false));
    }
    
    public static ContentValues getDefaultContentValues(final Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        final ContentValues commonContentValues = getCommonContentValues();
        if (Build.VERSION.SDK_INT > 15) {
            a(bitmap, commonContentValues);
        }
        return commonContentValues;
    }
    
    public static ContentValues getCommonContentValues() {
        final long timeInMillis = TuSdkDate.create().getTimeInMillis();
        final ContentValues contentValues = new ContentValues();
        contentValues.put("datetaken", Long.valueOf(timeInMillis));
        contentValues.put("date_modified", Long.valueOf(timeInMillis / 1000L));
        contentValues.put("date_added", Long.valueOf(timeInMillis / 1000L));
        return contentValues;
    }
    
    @TargetApi(16)
    private static void a(final Bitmap bitmap, final ContentValues contentValues) {
        contentValues.put("width", Integer.valueOf(bitmap.getWidth()));
        contentValues.put("height", Integer.valueOf(bitmap.getHeight()));
    }
    
    public static ContentValues build(final Bitmap bitmap, File albumFile, final String s) {
        final ContentValues defaultContentValues = getDefaultContentValues(bitmap);
        if (defaultContentValues == null) {
            return null;
        }
        if (albumFile == null && Build.VERSION.SDK_INT > 18) {
            albumFile = AlbumHelper.getAlbumFile();
        }
        if (albumFile != null) {
            defaultContentValues.put("_data", albumFile.getPath());
        }
        if (s != null) {
            defaultContentValues.put("description", s);
        }
        return defaultContentValues;
    }
    
    public static ImageSqlInfo saveJpgToAblum(final Context context, final Bitmap bitmap, final int n, final File file) {
        return saveJpgToAblum(context, bitmap, n, build(bitmap, file, ContextUtils.getAppName(context)));
    }
    
    public static ImageSqlInfo saveMp4ToAlbum(final Context context, final File file) {
        final String appName = ContextUtils.getAppName(context);
        final ContentValues commonContentValues = getCommonContentValues();
        if (commonContentValues == null) {
            return null;
        }
        if (file != null) {
            commonContentValues.put("_data", file.getPath());
        }
        if (appName != null) {
            commonContentValues.put("description", appName);
        }
        commonContentValues.put("mime_type", "video/mp4");
        return getVideoInfo(context.getContentResolver(), context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, commonContentValues));
    }
    
    public static ImageSqlInfo saveMp4ToAlbum(final Context context, final ContentValues contentValues) {
        final String appName = ContextUtils.getAppName(context);
        final ContentValues commonContentValues = getCommonContentValues();
        commonContentValues.put("title", appName);
        commonContentValues.put("mime_type", "video/mp4");
        if (contentValues != null) {
            commonContentValues.putAll(contentValues);
        }
        return getVideoInfo(context.getContentResolver(), context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, commonContentValues));
    }
    
    public static ImageSqlInfo saveJpgToAblum(final Context context, final Bitmap bitmap, final int n, final ContentValues contentValues) {
        if (context == null) {
            return null;
        }
        final Uri saveJpgToAblum = saveJpgToAblum(bitmap, context.getContentResolver(), n, contentValues);
        if (saveJpgToAblum == null) {
            return null;
        }
        return getImageInfo(context.getContentResolver(), saveJpgToAblum);
    }
    
    public static Uri saveJpgToAblum(final Bitmap bitmap, final ContentResolver contentResolver, final int n, final ContentValues contentValues) {
        if (bitmap == null || contentResolver == null) {
            return null;
        }
        if (TuSdkGPU.isSupporTurbo()) {
            return a(bitmap, contentResolver, n, contentValues);
        }
        return b(bitmap, contentResolver, n, contentValues);
    }
    
    private static Uri a(final Bitmap bitmap, final ContentResolver contentResolver, final int n, ContentValues defaultContentValues) {
        if (bitmap == null || contentResolver == null) {
            return null;
        }
        if (defaultContentValues == null) {
            defaultContentValues = getDefaultContentValues(bitmap);
        }
        defaultContentValues.put("mime_type", "image/jpeg");
        final Uri insert = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, defaultContentValues);
        if (insert == null) {
            return null;
        }
        OutputStream openOutputStream = null;
        try {
            openOutputStream = contentResolver.openOutputStream(insert);
            if (!TuSdkImageNative.imageCompress(bitmap, openOutputStream, n, true)) {
                TLog.e("saveJpgToAblum faild: %s", insert);
            }
        }
        catch (FileNotFoundException ex) {
            TLog.e(ex, "saveJpgToAblum: %s", insert);
        }
        finally {
            FileHelper.safeClose(openOutputStream);
        }
        return insert;
    }
    
    private static Uri b(final Bitmap bitmap, final ContentResolver contentResolver, final int n, final ContentValues contentValues) {
        final Uri insert = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (insert == null) {
            return null;
        }
        OutputStream openOutputStream = null;
        try {
            openOutputStream = contentResolver.openOutputStream(insert);
            bitmap.compress(Bitmap.CompressFormat.JPEG, n, openOutputStream);
        }
        catch (FileNotFoundException ex) {
            TLog.e(ex, "saveJpgToAblum: %s", insert);
        }
        finally {
            FileHelper.safeClose(openOutputStream);
        }
        return insert;
    }
    
    public static void notifyRefreshAblum(final Context context, final ImageSqlInfo imageSqlInfo) {
        if (context == null || imageSqlInfo == null || imageSqlInfo.path == null) {
            return;
        }
        final Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(imageSqlInfo.path)));
        context.sendBroadcast(intent);
    }
    
    static {
        PHOTOJECTIONS_JELLY_BEAN = new String[] { "_id", "orientation", "_data", "date_modified", "bucket_id", "_size", "width", "height" };
        PHOTOJECTIONS_LOW = new String[] { "_id", "orientation", "_data", "date_modified", "bucket_id", "_size" };
        PHOTOJECTIONS_VIDEO = new String[] { "_id", "_data", "date_modified", "bucket_id", "_size", "width", "height" };
        if (Build.VERSION.SDK_INT < 16) {
            PHOTOJECTIONS = ImageSqlHelper.PHOTOJECTIONS_LOW;
        }
        else {
            PHOTOJECTIONS = ImageSqlHelper.PHOTOJECTIONS_JELLY_BEAN;
        }
    }

    public static enum PhotoSortDescriptor {
        Date_Added("date_added", true),
        Date_Modified("date_modified", true),
        Customize;

        public String key;
        public boolean desc;

        private PhotoSortDescriptor(String var3, boolean var4) {
            this.key = var3;
            this.desc = var4;
        }

        private PhotoSortDescriptor() {
        }

        public ImageSqlHelper.PhotoSortDescriptor setKey(String var1) {
            this.key = var1;
            return this;
        }

        public ImageSqlHelper.PhotoSortDescriptor setDesc(boolean var1) {
            this.desc = var1;
            return this;
        }
    }
}
