package org.lasque.tusdk.core.utils.hardware;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TuSdkCorePatch
{
  private static final Map<String, String> a = new HashMap();
  private static final Map<String, String> b = new HashMap();
  
  public static boolean applyThumbRenderPatch()
  {
    boolean bool = false;
    Iterator localIterator = a.entrySet().iterator();
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
  
  public static boolean applyDeletedProgramPatch()
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
    a.put("V1732A", "VIVO");
    a.put("vivo Y71A", "VIVO");
    a.put("SM-J3300", "Samsung");
    b.put("Redmi 6", "XIAOMI");
    b.put("Redmi 6A", "XIAOMI");
    b.put("V1732A", "VIVO");
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\hardware\TuSdkCorePatch.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */