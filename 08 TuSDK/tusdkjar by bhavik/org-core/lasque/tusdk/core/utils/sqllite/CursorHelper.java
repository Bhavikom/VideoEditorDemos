package org.lasque.tusdk.core.utils.sqllite;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

public class CursorHelper
{
  public static ContentResolver getContentResolver(Context paramContext)
  {
    if (paramContext == null) {
      return null;
    }
    return paramContext.getContentResolver();
  }
  
  public static int getCursorIndex(Cursor paramCursor, String paramString)
  {
    if ((paramCursor == null) || (paramString == null)) {
      return -1;
    }
    return paramCursor.getColumnIndex(paramString);
  }
  
  public static String getCursorString(Cursor paramCursor, String paramString)
  {
    int i = getCursorIndex(paramCursor, paramString);
    if (i == -1) {
      return null;
    }
    return paramCursor.getString(i);
  }
  
  public static int getCursorInt(Cursor paramCursor, String paramString)
  {
    int i = getCursorIndex(paramCursor, paramString);
    if (i == -1) {
      return 0;
    }
    return paramCursor.getInt(i);
  }
  
  public static long getCursorLong(Cursor paramCursor, String paramString)
  {
    int i = getCursorIndex(paramCursor, paramString);
    if (i == -1) {
      return 0L;
    }
    return paramCursor.getLong(i);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\sqllite\CursorHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */