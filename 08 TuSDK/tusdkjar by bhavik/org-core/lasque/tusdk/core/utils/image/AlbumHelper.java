package org.lasque.tusdk.core.utils.image;

import android.graphics.Bitmap;
import android.os.Environment;
import java.io.File;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.StringHelper;

public class AlbumHelper
{
  public static File getAblumPath()
  {
    return FileHelper.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
  }
  
  public static File getAblumPath(String paramString)
  {
    File localFile1 = getAblumPath();
    if (localFile1 == null) {
      return null;
    }
    if (paramString == null) {
      return localFile1;
    }
    File localFile2 = new File(localFile1, paramString);
    if (!localFile2.exists()) {
      localFile2.mkdir();
    }
    return localFile2;
  }
  
  public static File getAlbumFile()
  {
    File localFile1 = getAblumPath("Camera");
    if ((localFile1 == null) || (!localFile1.exists())) {
      return null;
    }
    File localFile2 = new File(localFile1.getPath() + File.separator + getAlbumFileName());
    return localFile2;
  }
  
  public static File getAlbumFile(String paramString)
  {
    File localFile1 = getAblumPath(paramString);
    if (localFile1 == null) {
      return null;
    }
    File localFile2 = new File(localFile1.getPath() + File.separator + getAlbumFileName());
    return localFile2;
  }
  
  public static String getAlbumFileName()
  {
    String str = StringHelper.timeStampString();
    return "LSQ_" + str + ".jpg";
  }
  
  public static File savePhotoToAlbum(Bitmap paramBitmap, String paramString)
  {
    if (paramBitmap == null) {
      return null;
    }
    File localFile = getAlbumFile(paramString);
    if (localFile == null) {
      return null;
    }
    if (BitmapHelper.saveBitmap(localFile, paramBitmap, 90)) {
      return localFile;
    }
    return null;
  }
  
  public static File copyToAlbum(File paramFile, String paramString)
  {
    if ((paramFile == null) || (!paramFile.exists()) || (!paramFile.isFile())) {
      return null;
    }
    File localFile = getAlbumFile(paramString);
    if (localFile == null) {
      return null;
    }
    boolean bool = FileHelper.copyFile(paramFile, localFile);
    return bool ? localFile : null;
  }
  
  public static File getAlbumVideoFile()
  {
    File localFile1 = getAblumPath("Camera");
    if ((localFile1 == null) || (!localFile1.exists())) {
      return null;
    }
    File localFile2 = new File(localFile1.getPath() + File.separator + getAlbumVideoFileName());
    return localFile2;
  }
  
  public static File getAlbumVideoFile(String paramString)
  {
    File localFile1 = getAblumPath(paramString);
    if (localFile1 == null) {
      return null;
    }
    File localFile2 = new File(localFile1.getPath() + File.separator + getAlbumVideoFileName());
    return localFile2;
  }
  
  public static String getAlbumVideoFileName()
  {
    String str = StringHelper.timeStampString();
    return "LSQ_" + str + ".mp4";
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\image\AlbumHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */