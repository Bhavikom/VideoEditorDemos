package org.lasque.tusdk.core.media.codec.extend;

import android.os.Build;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.utils.hardware.HardwareHelper;

public class TuSdkMediaTimeSlicePatch
{
  private static String[] a = { "mt" };
  private static final Map<String, String> b = new HashMap();
  private static String c = Build.HARDWARE;
  private boolean d = false;
  private boolean e = false;
  
  private boolean a()
  {
    if ((c == null) || (c.isEmpty())) {
      return false;
    }
    for (String str : a) {
      if (c.startsWith(str)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean overview(TuSdkMediaTimeSliceEntity paramTuSdkMediaTimeSliceEntity, long paramLong1, long paramLong2)
  {
    if (paramTuSdkMediaTimeSliceEntity == null) {
      return false;
    }
    if ((!a()) || (b())) {
      return paramTuSdkMediaTimeSliceEntity.overview(paramLong2) > 0;
    }
    if ((paramLong1 < paramLong2) && (!paramTuSdkMediaTimeSliceEntity.isReverse()) && (!this.e)) {
      this.d = true;
    }
    return (this.d) || (paramTuSdkMediaTimeSliceEntity.overview(paramLong2) > 0);
  }
  
  public void switchSliced()
  {
    if (this.d)
    {
      this.d = false;
      this.e = true;
    }
  }
  
  public boolean isReturnFrame(long paramLong1, long paramLong2)
  {
    if ((this.e) && (a()))
    {
      if (paramLong1 > paramLong2)
      {
        this.d = false;
        this.e = false;
      }
      return true;
    }
    return false;
  }
  
  private boolean b()
  {
    boolean bool = false;
    Iterator localIterator = b.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      bool = HardwareHelper.isMatchDeviceModelAndManuFacturer((String)localEntry.getKey(), (String)localEntry.getValue());
      if (bool) {
        break;
      }
    }
    return bool;
  }
  
  static
  {
    b.put("MP1605", "Meitu");
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\extend\TuSdkMediaTimeSlicePatch.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */