package org.lasque.tusdk.core.type;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;

public enum DownloadTaskStatus
{
  private int a;
  @SuppressLint({"UseSparseArrays"})
  private static final Map<Integer, DownloadTaskStatus> b;
  
  private DownloadTaskStatus(int paramInt)
  {
    this.a = paramInt;
  }
  
  public int getFlag()
  {
    return this.a;
  }
  
  public static DownloadTaskStatus getType(int paramInt)
  {
    DownloadTaskStatus localDownloadTaskStatus = (DownloadTaskStatus)b.get(Integer.valueOf(paramInt));
    if (localDownloadTaskStatus == null) {
      localDownloadTaskStatus = StatusInit;
    }
    return localDownloadTaskStatus;
  }
  
  static
  {
    b = new HashMap();
    for (DownloadTaskStatus localDownloadTaskStatus : values()) {
      b.put(Integer.valueOf(localDownloadTaskStatus.getFlag()), localDownloadTaskStatus);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\type\DownloadTaskStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */