// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif.ExifInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif.ExifTag;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.exif.ExifTag;
import java.util.List;
//import org.lasque.tusdk.core.utils.TLog;
import java.io.File;
//import org.lasque.tusdk.core.exif.ExifInterface;

public class ExifHelper
{
    public static ExifInterface getExifInterface(final String pathname) {
        if (pathname == null) {
            return null;
        }
        return getExifInterface(new File(pathname));
    }
    
    public static ExifInterface getExifInterface(final File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        final ExifInterface exifInterface = new ExifInterface();
        exifInterface.readExif(file.getAbsolutePath(), 31);
        return exifInterface;
    }
    
    public static ExifInterface getExifInterface(final byte[] array) {
        if (array == null) {
            return null;
        }
        final ExifInterface exifInterface = new ExifInterface();
        exifInterface.readExif(array, 31);
        return exifInterface;
    }
    
    public static List<ExifTag> getAllTags(final File file) {
        final ExifInterface exifInterface = getExifInterface(file);
        if (exifInterface == null) {
            return null;
        }
        return exifInterface.getAllTags();
    }
    
    public static boolean writeExifInterface(final ExifInterface exifInterface, final File file) {
        return file != null && file.exists() && file.isFile() && writeExifInterface(exifInterface, file.getAbsolutePath());
    }
    
    public static boolean writeExifInterface(final ExifInterface exifInterface, final String s) {
        if (exifInterface == null || StringHelper.isBlank(s)) {
            return false;
        }
        exifInterface.writeExif(s);
        return true;
    }
    
    public static String getExifDescription(final String pathname) {
        return getExifDescription(new File(pathname));
    }
    
    public static String getExifDescription(final File file) {
        final ExifInterface exifInterface = getExifInterface(file);
        if (exifInterface == null) {
            return null;
        }
        final ExifTag tag = exifInterface.getTag(ExifInterface.TAG_IMAGE_DESCRIPTION);
        if (tag != null) {
            return tag.forceGetValueAsString();
        }
        return null;
    }
    
    public static void writeExif(final File file, final File file2) {
        final List<ExifTag> allTags = getAllTags(file);
        if (allTags == null) {
            return;
        }
        final TuSdkSize bitmapSize = BitmapHelper.getBitmapSize(file2);
        if (bitmapSize == null) {
            return;
        }
        final ExifInterface exifInterface = new ExifInterface();
        exifInterface.setTags(allTags);
        exifInterface.setTagValue(ExifInterface.TAG_IMAGE_WIDTH, bitmapSize.width);
        exifInterface.setTagValue(ExifInterface.TAG_IMAGE_LENGTH, bitmapSize.height);
        writeExifInterface(exifInterface, file2);
    }
    
    public static void log(final File file) {
        final List<ExifTag> allTags = getAllTags(file);
        if (allTags == null || allTags.size() == 0) {
            TLog.i("Exif info unexsit: %s", file);
            return;
        }
        for (final ExifTag exifTag : allTags) {
            TLog.i("exifTag (%s) %s: %s", exifTag.getTagId(), JpegExfiTag.getTagName(exifTag.getTagId()), exifTag.forceGetValueAsString());
        }
    }
    
    public static void log(final List<ExifTag> list) {
        if (list == null || list.size() == 0) {
            TLog.i("Exif info unexsit: %s", list);
            return;
        }
        for (final ExifTag exifTag : list) {
            TLog.i("exifTag (%s) %s: %s", exifTag.getTagId(), JpegExfiTag.getTagName(exifTag.getTagId()), exifTag.forceGetValueAsString());
        }
    }
    
    public interface Options extends ExifInterface.Options
    {
        public static final int OPTION_ALL_EXCLUED_THUMBNAIL = 31;
    }
}
