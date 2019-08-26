package org.lasque.tusdk.core.type;

import android.annotation.SuppressLint;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public enum ClazzType
{
  private int a;
  @SuppressLint({"UseSparseArrays"})
  private static final Map<Integer, ClazzType> b;
  
  private ClazzType(int paramInt)
  {
    this.a = paramInt;
  }
  
  public int getFlag()
  {
    return this.a;
  }
  
  public static ClazzType getType(int paramInt)
  {
    return (ClazzType)b.get(Integer.valueOf(paramInt));
  }
  
  static
  {
    b = new HashMap();
    for (ClazzType localClazzType : values()) {
      b.put(Integer.valueOf(localClazzType.getFlag()), localClazzType);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\type\ClazzType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */