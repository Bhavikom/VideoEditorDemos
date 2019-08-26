package org.lasque.tusdk.core.utils.image;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.exif.ExifInterface;
import org.lasque.tusdk.core.exif.ExifInterface.Options;
import org.lasque.tusdk.core.exif.ExifTag;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;

public class ExifHelper
{
  public static ExifInterface getExifInterface(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return getExifInterface(new File(paramString));
  }
  
  public static ExifInterface getExifInterface(File paramFile)
  {
    if ((paramFile == null) || (!paramFile.exists()) || (!paramFile.isFile())) {
      return null;
    }
    ExifInterface localExifInterface = new ExifInterface();
    try
    {
      localExifInterface.readExif(paramFile.getAbsolutePath(), 31);
      return localExifInterface;
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "getExifInterface", new Object[0]);
    }
    return null;
  }
  
  public static ExifInterface getExifInterface(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null) {
      return null;
    }
    ExifInterface localExifInterface = new ExifInterface();
    try
    {
      localExifInterface.readExif(paramArrayOfByte, 31);
      return localExifInterface;
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "getExifInterface", new Object[0]);
    }
    return null;
  }
  
  public static List<ExifTag> getAllTags(File paramFile)
  {
    ExifInterface localExifInterface = getExifInterface(paramFile);
    if (localExifInterface == null) {
      return null;
    }
    return localExifInterface.getAllTags();
  }
  
  public static boolean writeExifInterface(ExifInterface paramExifInterface, File paramFile)
  {
    if ((paramFile == null) || (!paramFile.exists()) || (!paramFile.isFile())) {
      return false;
    }
    return writeExifInterface(paramExifInterface, paramFile.getAbsolutePath());
  }
  
  public static boolean writeExifInterface(ExifInterface paramExifInterface, String paramString)
  {
    if ((paramExifInterface == null) || (StringHelper.isBlank(paramString))) {
      return false;
    }
    try
    {
      paramExifInterface.writeExif(paramString);
      return true;
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "writeExifInterface", new Object[0]);
    }
    return false;
  }
  
  public static String getExifDescription(String paramString)
  {
    return getExifDescription(new File(paramString));
  }
  
  public static String getExifDescription(File paramFile)
  {
    ExifInterface localExifInterface = getExifInterface(paramFile);
    if (localExifInterface == null) {
      return null;
    }
    ExifTag localExifTag = localExifInterface.getTag(ExifInterface.TAG_IMAGE_DESCRIPTION);
    if (localExifTag != null) {
      return localExifTag.forceGetValueAsString();
    }
    return null;
  }
  
  public static void writeExif(File paramFile1, File paramFile2)
  {
    List localList = getAllTags(paramFile1);
    if (localList == null) {
      return;
    }
    TuSdkSize localTuSdkSize = BitmapHelper.getBitmapSize(paramFile2);
    if (localTuSdkSize == null) {
      return;
    }
    ExifInterface localExifInterface = new ExifInterface();
    localExifInterface.setTags(localList);
    localExifInterface.setTagValue(ExifInterface.TAG_IMAGE_WIDTH, Integer.valueOf(localTuSdkSize.width));
    localExifInterface.setTagValue(ExifInterface.TAG_IMAGE_LENGTH, Integer.valueOf(localTuSdkSize.height));
    writeExifInterface(localExifInterface, paramFile2);
  }
  
  public static void log(File paramFile)
  {
    List localList = getAllTags(paramFile);
    if ((localList == null) || (localList.size() == 0))
    {
      TLog.i("Exif info unexsit: %s", new Object[] { paramFile });
      return;
    }
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      ExifTag localExifTag = (ExifTag)localIterator.next();
      TLog.i("exifTag (%s) %s: %s", new Object[] { Short.valueOf(localExifTag.getTagId()), JpegExfiTag.getTagName(localExifTag.getTagId()), localExifTag.forceGetValueAsString() });
    }
  }
  
  public static void log(List<ExifTag> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0))
    {
      TLog.i("Exif info unexsit: %s", new Object[] { paramList });
      return;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      ExifTag localExifTag = (ExifTag)localIterator.next();
      TLog.i("exifTag (%s) %s: %s", new Object[] { Short.valueOf(localExifTag.getTagId()), JpegExfiTag.getTagName(localExifTag.getTagId()), localExifTag.forceGetValueAsString() });
    }
  }
  
  public static abstract interface Options
    extends ExifInterface.Options
  {
    public static final int OPTION_ALL_EXCLUED_THUMBNAIL = 31;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\image\ExifHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */