// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image;

import android.graphics.Bitmap;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.utils.FileHelper;
import android.os.Environment;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;

import java.io.File;

public class AlbumHelper
{
    public static File getAblumPath() {
        return FileHelper.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    }
    
    public static File getAblumPath(final String child) {
        final File ablumPath = getAblumPath();
        if (ablumPath == null) {
            return null;
        }
        if (child == null) {
            return ablumPath;
        }
        final File file = new File(ablumPath, child);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }
    
    public static File getAlbumFile() {
        final File ablumPath = getAblumPath("Camera");
        if (ablumPath == null || !ablumPath.exists()) {
            return null;
        }
        return new File(ablumPath.getPath() + File.separator + getAlbumFileName());
    }
    
    public static File getAlbumFile(final String s) {
        final File ablumPath = getAblumPath(s);
        if (ablumPath == null) {
            return null;
        }
        return new File(ablumPath.getPath() + File.separator + getAlbumFileName());
    }
    
    public static String getAlbumFileName() {
        return "LSQ_" + StringHelper.timeStampString() + ".jpg";
    }
    
    public static File savePhotoToAlbum(final Bitmap bitmap, final String s) {
        if (bitmap == null) {
            return null;
        }
        final File albumFile = getAlbumFile(s);
        if (albumFile == null) {
            return null;
        }
        if (BitmapHelper.saveBitmap(albumFile, bitmap, 90)) {
            return albumFile;
        }
        return null;
    }
    
    public static File copyToAlbum(final File file, final String s) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        final File albumFile = getAlbumFile(s);
        if (albumFile == null) {
            return null;
        }
        return FileHelper.copyFile(file, albumFile) ? albumFile : null;
    }
    
    public static File getAlbumVideoFile() {
        final File ablumPath = getAblumPath("Camera");
        if (ablumPath == null || !ablumPath.exists()) {
            return null;
        }
        return new File(ablumPath.getPath() + File.separator + getAlbumVideoFileName());
    }
    
    public static File getAlbumVideoFile(final String s) {
        final File ablumPath = getAblumPath(s);
        if (ablumPath == null) {
            return null;
        }
        return new File(ablumPath.getPath() + File.separator + getAlbumVideoFileName());
    }
    
    public static String getAlbumVideoFileName() {
        return "LSQ_" + StringHelper.timeStampString() + ".mp4";
    }
}
