package org.lasque.tusdk.core.utils.sqllite;

import android.database.Cursor;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.lasque.tusdk.core.utils.image.AlbumHelper;

public class AlbumSqlInfo
  extends SqlLiteInfo
{
  public static final String BUCKET_TOTAL = "bucket_total";
  public static final String CAMERA_FOLDER = "Camera";
  public long id;
  public String title;
  public int total;
  public ImageSqlInfo cover;
  
  public static File cameraFolder()
  {
    return AlbumHelper.getAblumPath("Camera");
  }
  
  public AlbumSqlInfo() {}
  
  public AlbumSqlInfo(Cursor paramCursor)
  {
    super(paramCursor);
  }
  
  public void setInfoWithCursor(Cursor paramCursor)
  {
    if (paramCursor == null) {
      return;
    }
    this.id = getCursorLong(paramCursor, "bucket_id");
    this.title = getCursorString(paramCursor, "bucket_display_name");
    this.total = getCursorInt(paramCursor, "bucket_total");
  }
  
  public String toString()
  {
    String str = String.format("{id: %s, title: %s, total: %s, cover: %s}", new Object[] { Long.valueOf(this.id), this.title, Integer.valueOf(this.total), this.cover });
    return str;
  }
  
  public static void sortTitle(ArrayList<AlbumSqlInfo> paramArrayList)
  {
    if (paramArrayList == null) {
      return;
    }
    Collections.sort(paramArrayList, new Comparator()
    {
      public int compare(AlbumSqlInfo paramAnonymousAlbumSqlInfo1, AlbumSqlInfo paramAnonymousAlbumSqlInfo2)
      {
        if ((paramAnonymousAlbumSqlInfo1.title == null) || (paramAnonymousAlbumSqlInfo2.title == null)) {
          return -1;
        }
        return paramAnonymousAlbumSqlInfo1.title.compareToIgnoreCase(paramAnonymousAlbumSqlInfo2.title);
      }
    });
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\sqllite\AlbumSqlInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */