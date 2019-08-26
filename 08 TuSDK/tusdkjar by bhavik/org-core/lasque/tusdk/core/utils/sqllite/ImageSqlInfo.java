package org.lasque.tusdk.core.utils.sqllite;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build.VERSION;
import java.util.Calendar;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.DateHelper;

public class ImageSqlInfo
  extends SqlLiteInfo
{
  public long id;
  public long albumId;
  public int orientation;
  public Calendar createDate;
  public String path;
  public long length;
  public TuSdkSize size;
  public String name;
  
  public ImageSqlInfo() {}
  
  public ImageSqlInfo(Cursor paramCursor)
  {
    super(paramCursor);
  }
  
  public void setInfoWithCursor(Cursor paramCursor)
  {
    if (paramCursor == null) {
      return;
    }
    this.id = getCursorLong(paramCursor, "_id");
    this.path = getCursorString(paramCursor, "_data");
    this.orientation = getCursorInt(paramCursor, "orientation");
    this.createDate = DateHelper.parseCalendar(getCursorLong(paramCursor, "date_modified"));
    this.albumId = getCursorLong(paramCursor, "bucket_id");
    this.length = getCursorLong(paramCursor, "_size");
    if (Build.VERSION.SDK_INT > 15) {
      a(paramCursor);
    }
  }
  
  @TargetApi(16)
  private void a(Cursor paramCursor)
  {
    int i = getCursorInt(paramCursor, "width");
    int j = getCursorInt(paramCursor, "height");
    this.size = new TuSdkSize(i, j);
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof ImageSqlInfo)) {
      return this.id == ((ImageSqlInfo)paramObject).id;
    }
    return super.equals(paramObject);
  }
  
  public String toString()
  {
    String str = String.format("{id: %s, albumId: %s, orientation: %s, createDate: %s, length: %s, path: %s, name: %s, size: %s}", new Object[] { Long.valueOf(this.id), Long.valueOf(this.albumId), Integer.valueOf(this.orientation), this.createDate, Long.valueOf(this.length), this.path, this.name, this.size });
    return str;
  }
  
  public String identify()
  {
    return String.format("%s_%s_%s", new Object[] { this.path, Long.valueOf(this.id), Long.valueOf(this.albumId) });
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\sqllite\ImageSqlInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */