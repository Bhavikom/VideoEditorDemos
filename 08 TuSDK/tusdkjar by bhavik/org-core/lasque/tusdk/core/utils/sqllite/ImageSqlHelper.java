package org.lasque.tusdk.core.utils.sqllite;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.Video.Media;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;
import org.lasque.tusdk.core.secret.TuSdkImageNative;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.TuSdkDate;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.image.AlbumHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class ImageSqlHelper
{
  @SuppressLint({"InlinedApi"})
  public static final String[] PHOTOJECTIONS_JELLY_BEAN = { "_id", "orientation", "_data", "date_modified", "bucket_id", "_size", "width", "height" };
  public static final String[] PHOTOJECTIONS_LOW = { "_id", "orientation", "_data", "date_modified", "bucket_id", "_size" };
  public static final String[] PHOTOJECTIONS_VIDEO = { "_id", "_data", "date_modified", "bucket_id", "_size", "width", "height" };
  public static final String[] PHOTOJECTIONS;
  
  public static File getLocalImageFile(Context paramContext, Uri paramUri)
  {
    ImageSqlInfo localImageSqlInfo = getImageInfo(paramContext, paramUri);
    if (localImageSqlInfo == null) {
      return null;
    }
    File localFile = new File(localImageSqlInfo.path);
    return localFile;
  }
  
  public static ImageSqlInfo getImageInfo(Context paramContext, Uri paramUri)
  {
    ImageSqlInfo localImageSqlInfo = getImageInfo(paramContext, paramUri, PHOTOJECTIONS, null, null, null);
    return localImageSqlInfo;
  }
  
  public static ImageSqlInfo getImageInfo(ContentResolver paramContentResolver, Uri paramUri)
  {
    if ((paramContentResolver == null) || (paramUri == null)) {
      return null;
    }
    Cursor localCursor = paramContentResolver.query(paramUri, PHOTOJECTIONS, null, null, null);
    return getImageInfo(localCursor);
  }
  
  public static ImageSqlInfo getVideoInfo(ContentResolver paramContentResolver, Uri paramUri)
  {
    if ((paramContentResolver == null) || (paramUri == null)) {
      return null;
    }
    Cursor localCursor = paramContentResolver.query(paramUri, PHOTOJECTIONS_VIDEO, null, null, null);
    return getImageInfo(localCursor);
  }
  
  public static ImageSqlInfo getImageInfo(Context paramContext, Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    Cursor localCursor = getWithCursorLoader(paramContext, paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
    return getImageInfo(localCursor);
  }
  
  public static ImageSqlInfo getImageInfo(Cursor paramCursor)
  {
    if (paramCursor == null) {
      return null;
    }
    ImageSqlInfo localImageSqlInfo = null;
    if (paramCursor.moveToFirst()) {
      localImageSqlInfo = new ImageSqlInfo(paramCursor);
    }
    paramCursor.close();
    return localImageSqlInfo;
  }
  
  public static Cursor getWithCursorLoader(Context paramContext, Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    if ((paramContext == null) || (paramUri == null)) {
      return null;
    }
    return paramContext.getContentResolver().query(paramUri, paramArrayOfString1, paramString1, null, paramString2);
  }
  
  public static ArrayList<AlbumSqlInfo> getAlbumList(Context paramContext)
  {
    if (paramContext == null) {
      return null;
    }
    Uri localUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String[] arrayOfString = { "bucket_id", "bucket_display_name", "COUNT(*) AS bucket_total" };
    String str1 = "1) GROUP BY bucket_id-- (";
    String str2 = "bucket_display_name ASC";
    Cursor localCursor = getWithCursorLoader(paramContext, localUri, arrayOfString, str1, null, str2);
    if ((localCursor == null) || (!localCursor.moveToFirst())) {
      return null;
    }
    ArrayList localArrayList = new ArrayList();
    while (!localCursor.isAfterLast())
    {
      AlbumSqlInfo localAlbumSqlInfo = new AlbumSqlInfo(localCursor);
      localAlbumSqlInfo.cover = getAlbumCoverInfo(paramContext, localAlbumSqlInfo.id);
      localArrayList.add(localAlbumSqlInfo);
      localCursor.moveToNext();
    }
    localCursor.close();
    AlbumSqlInfo.sortTitle(localArrayList);
    return localArrayList;
  }
  
  public static ArrayList<ImageSqlInfo> getPhotoList(Context paramContext, long paramLong)
  {
    return getPhotoList(paramContext, paramLong, PhotoSortDescriptor.Date_Modified);
  }
  
  public static ArrayList<ImageSqlInfo> getPhotoList(Context paramContext, long paramLong, PhotoSortDescriptor paramPhotoSortDescriptor)
  {
    if (paramContext == null) {
      return null;
    }
    Uri localUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String str1 = "bucket_id=" + paramLong;
    String str2 = paramPhotoSortDescriptor.key + (paramPhotoSortDescriptor.desc ? " DESC" : " ASC");
    Cursor localCursor = getWithCursorLoader(paramContext, localUri, PHOTOJECTIONS, str1, null, str2);
    ArrayList localArrayList = getPhotoList(localCursor);
    return localArrayList;
  }
  
  public static ArrayList<ImageSqlInfo> getPhotoList(ContentResolver paramContentResolver, boolean paramBoolean)
  {
    if (paramContentResolver == null) {
      return null;
    }
    Uri localUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String str = "date_modified" + (paramBoolean ? " DESC" : " ASC");
    Cursor localCursor = paramContentResolver.query(localUri, PHOTOJECTIONS, null, null, str);
    ArrayList localArrayList = getPhotoList(localCursor);
    return localArrayList;
  }
  
  public static ArrayList<ImageSqlInfo> getPhotoList(Cursor paramCursor)
  {
    if ((paramCursor == null) || (!paramCursor.moveToFirst())) {
      return null;
    }
    ArrayList localArrayList = new ArrayList();
    while (!paramCursor.isAfterLast())
    {
      ImageSqlInfo localImageSqlInfo = new ImageSqlInfo(paramCursor);
      localArrayList.add(localImageSqlInfo);
      paramCursor.moveToNext();
    }
    paramCursor.close();
    return localArrayList;
  }
  
  public static ImageSqlInfo getAlbumCoverInfo(Context paramContext, long paramLong)
  {
    if ((paramContext == null) || (paramLong == 0L)) {
      return null;
    }
    Uri localUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String str1 = "bucket_id=" + paramLong;
    String str2 = "date_modified DESC";
    ImageSqlInfo localImageSqlInfo = getImageInfo(paramContext, localUri, PHOTOJECTIONS, str1, null, str2);
    return localImageSqlInfo;
  }
  
  public static Bitmap getThumbnail(Context paramContext, ImageSqlInfo paramImageSqlInfo, int paramInt)
  {
    if ((paramContext == null) || (paramImageSqlInfo == null) || (paramInt == 0)) {
      return null;
    }
    Bitmap localBitmap = MediaStore.Images.Thumbnails.getThumbnail(paramContext.getContentResolver(), paramImageSqlInfo.id, paramInt, null);
    localBitmap = BitmapHelper.imageRotaing(localBitmap, ImageOrientation.getValue(paramImageSqlInfo.orientation, false));
    return localBitmap;
  }
  
  public static ContentValues getDefaultContentValues(Bitmap paramBitmap)
  {
    if (paramBitmap == null) {
      return null;
    }
    ContentValues localContentValues = getCommonContentValues();
    if (Build.VERSION.SDK_INT > 15) {
      a(paramBitmap, localContentValues);
    }
    return localContentValues;
  }
  
  public static ContentValues getCommonContentValues()
  {
    long l = TuSdkDate.create().getTimeInMillis();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("datetaken", Long.valueOf(l));
    localContentValues.put("date_modified", Long.valueOf(l / 1000L));
    localContentValues.put("date_added", Long.valueOf(l / 1000L));
    return localContentValues;
  }
  
  @TargetApi(16)
  private static void a(Bitmap paramBitmap, ContentValues paramContentValues)
  {
    paramContentValues.put("width", Integer.valueOf(paramBitmap.getWidth()));
    paramContentValues.put("height", Integer.valueOf(paramBitmap.getHeight()));
  }
  
  public static ContentValues build(Bitmap paramBitmap, File paramFile, String paramString)
  {
    ContentValues localContentValues = getDefaultContentValues(paramBitmap);
    if (localContentValues == null) {
      return null;
    }
    if ((paramFile == null) && (Build.VERSION.SDK_INT > 18)) {
      paramFile = AlbumHelper.getAlbumFile();
    }
    if (paramFile != null) {
      localContentValues.put("_data", paramFile.getPath());
    }
    if (paramString != null) {
      localContentValues.put("description", paramString);
    }
    return localContentValues;
  }
  
  public static ImageSqlInfo saveJpgToAblum(Context paramContext, Bitmap paramBitmap, int paramInt, File paramFile)
  {
    String str = ContextUtils.getAppName(paramContext);
    ContentValues localContentValues = build(paramBitmap, paramFile, str);
    return saveJpgToAblum(paramContext, paramBitmap, paramInt, localContentValues);
  }
  
  public static ImageSqlInfo saveMp4ToAlbum(Context paramContext, File paramFile)
  {
    String str = ContextUtils.getAppName(paramContext);
    ContentValues localContentValues = getCommonContentValues();
    if (localContentValues == null) {
      return null;
    }
    if (paramFile != null) {
      localContentValues.put("_data", paramFile.getPath());
    }
    if (str != null) {
      localContentValues.put("description", str);
    }
    localContentValues.put("mime_type", "video/mp4");
    Uri localUri = paramContext.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
    return getVideoInfo(paramContext.getContentResolver(), localUri);
  }
  
  public static ImageSqlInfo saveMp4ToAlbum(Context paramContext, ContentValues paramContentValues)
  {
    String str = ContextUtils.getAppName(paramContext);
    ContentValues localContentValues = getCommonContentValues();
    localContentValues.put("title", str);
    localContentValues.put("mime_type", "video/mp4");
    if (paramContentValues != null) {
      localContentValues.putAll(paramContentValues);
    }
    Uri localUri = paramContext.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
    return getVideoInfo(paramContext.getContentResolver(), localUri);
  }
  
  public static ImageSqlInfo saveJpgToAblum(Context paramContext, Bitmap paramBitmap, int paramInt, ContentValues paramContentValues)
  {
    if (paramContext == null) {
      return null;
    }
    Uri localUri = saveJpgToAblum(paramBitmap, paramContext.getContentResolver(), paramInt, paramContentValues);
    if (localUri == null) {
      return null;
    }
    return getImageInfo(paramContext.getContentResolver(), localUri);
  }
  
  public static Uri saveJpgToAblum(Bitmap paramBitmap, ContentResolver paramContentResolver, int paramInt, ContentValues paramContentValues)
  {
    if ((paramBitmap == null) || (paramContentResolver == null)) {
      return null;
    }
    if (TuSdkGPU.isSupporTurbo()) {
      return a(paramBitmap, paramContentResolver, paramInt, paramContentValues);
    }
    return b(paramBitmap, paramContentResolver, paramInt, paramContentValues);
  }
  
  private static Uri a(Bitmap paramBitmap, ContentResolver paramContentResolver, int paramInt, ContentValues paramContentValues)
  {
    if ((paramBitmap == null) || (paramContentResolver == null)) {
      return null;
    }
    if (paramContentValues == null) {
      paramContentValues = getDefaultContentValues(paramBitmap);
    }
    paramContentValues.put("mime_type", "image/jpeg");
    Uri localUri = paramContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, paramContentValues);
    if (localUri == null) {
      return null;
    }
    OutputStream localOutputStream = null;
    try
    {
      localOutputStream = paramContentResolver.openOutputStream(localUri);
      if (!TuSdkImageNative.imageCompress(paramBitmap, localOutputStream, paramInt, true)) {
        TLog.e("saveJpgToAblum faild: %s", new Object[] { localUri });
      }
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      TLog.e(localFileNotFoundException, "saveJpgToAblum: %s", new Object[] { localUri });
    }
    finally
    {
      FileHelper.safeClose(localOutputStream);
    }
    return localUri;
  }
  
  private static Uri b(Bitmap paramBitmap, ContentResolver paramContentResolver, int paramInt, ContentValues paramContentValues)
  {
    Uri localUri = paramContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, paramContentValues);
    if (localUri == null) {
      return null;
    }
    OutputStream localOutputStream = null;
    try
    {
      localOutputStream = paramContentResolver.openOutputStream(localUri);
      paramBitmap.compress(Bitmap.CompressFormat.JPEG, paramInt, localOutputStream);
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      TLog.e(localFileNotFoundException, "saveJpgToAblum: %s", new Object[] { localUri });
    }
    finally
    {
      FileHelper.safeClose(localOutputStream);
    }
    return localUri;
  }
  
  public static void notifyRefreshAblum(Context paramContext, ImageSqlInfo paramImageSqlInfo)
  {
    if ((paramContext == null) || (paramImageSqlInfo == null) || (paramImageSqlInfo.path == null)) {
      return;
    }
    Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
    Uri localUri = Uri.fromFile(new File(paramImageSqlInfo.path));
    localIntent.setData(localUri);
    paramContext.sendBroadcast(localIntent);
  }
  
  static
  {
    if (Build.VERSION.SDK_INT < 16) {
      PHOTOJECTIONS = PHOTOJECTIONS_LOW;
    } else {
      PHOTOJECTIONS = PHOTOJECTIONS_JELLY_BEAN;
    }
  }
  
  public static enum PhotoSortDescriptor
  {
    public String key;
    public boolean desc;
    
    private PhotoSortDescriptor(String paramString, boolean paramBoolean)
    {
      this.key = paramString;
      this.desc = paramBoolean;
    }
    
    private PhotoSortDescriptor() {}
    
    public PhotoSortDescriptor setKey(String paramString)
    {
      this.key = paramString;
      return this;
    }
    
    public PhotoSortDescriptor setDesc(boolean paramBoolean)
    {
      this.desc = paramBoolean;
      return this;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\sqllite\ImageSqlHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */