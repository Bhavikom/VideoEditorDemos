package org.lasque.tusdk.core.type;

import java.util.HashMap;
import java.util.Map;
import org.lasque.tusdk.core.utils.StringHelper;

public enum SoundType
{
  private String a;
  private int b;
  private static final Map<String, SoundType> c;
  
  private SoundType(String paramString, int paramInt)
  {
    this.a = paramString;
    this.b = paramInt;
  }
  
  public String getFlag()
  {
    return this.a;
  }
  
  public int getNum()
  {
    return this.b;
  }
  
  public static SoundType getType(String paramString)
  {
    SoundType localSoundType = null;
    if (StringHelper.isNotEmpty(paramString)) {
      localSoundType = (SoundType)c.get(paramString);
    }
    if (localSoundType == null) {
      localSoundType = TypeUnknown;
    }
    return localSoundType;
  }
  
  static
  {
    c = new HashMap();
    for (SoundType localSoundType : values()) {
      c.put(localSoundType.getFlag(), localSoundType);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\type\SoundType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */