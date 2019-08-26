package org.lasque.tusdk.core.utils.sqllite;

import android.database.Cursor;
import java.io.Serializable;

public class SqlLiteInfo
  implements Serializable
{
  public SqlLiteInfo() {}
  
  public SqlLiteInfo(Cursor paramCursor)
  {
    setInfoWithCursor(paramCursor);
  }
  
  public void setInfoWithCursor(Cursor paramCursor)
  {
    if (paramCursor == null) {}
  }
  
  public String getCursorString(Cursor paramCursor, String paramString)
  {
    return CursorHelper.getCursorString(paramCursor, paramString);
  }
  
  public int getCursorInt(Cursor paramCursor, String paramString)
  {
    return CursorHelper.getCursorInt(paramCursor, paramString);
  }
  
  public long getCursorLong(Cursor paramCursor, String paramString)
  {
    return CursorHelper.getCursorLong(paramCursor, paramString);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\sqllite\SqlLiteInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */