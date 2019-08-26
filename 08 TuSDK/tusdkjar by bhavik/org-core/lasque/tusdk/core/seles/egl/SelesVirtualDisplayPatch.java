package org.lasque.tusdk.core.seles.egl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.utils.hardware.HardwareHelper;

public class SelesVirtualDisplayPatch
{
  private static HashMap<String, String> a = new HashMap();
  
  public static boolean isNeedVirtualDisplayPatch()
  {
    Iterator localIterator = a.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (HardwareHelper.isMatchDeviceModelAndManuFacturer((String)localEntry.getKey(), (String)localEntry.getValue())) {
        return true;
      }
    }
    return false;
  }
  
  static
  {
    a.put("MP1605", "Meitu");
    a.put("MHA-AL00", "HUAWEI");
    a.put("BKL-AL00", "HUAWEI");
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\egl\SelesVirtualDisplayPatch.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */