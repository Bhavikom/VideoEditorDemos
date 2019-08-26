package org.lasque.tusdk.core.type;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;

public enum OnlineCommandAction
{
  private int a;
  @SuppressLint({"UseSparseArrays"})
  private static final Map<Integer, OnlineCommandAction> b;
  
  private OnlineCommandAction(int paramInt)
  {
    this.a = paramInt;
  }
  
  public int getFlag()
  {
    return this.a;
  }
  
  public static OnlineCommandAction getType(int paramInt)
  {
    OnlineCommandAction localOnlineCommandAction = (OnlineCommandAction)b.get(Integer.valueOf(paramInt));
    if (localOnlineCommandAction == null) {
      localOnlineCommandAction = ActionUnknown;
    }
    return localOnlineCommandAction;
  }
  
  static
  {
    b = new HashMap();
    for (OnlineCommandAction localOnlineCommandAction : values()) {
      b.put(Integer.valueOf(localOnlineCommandAction.getFlag()), localOnlineCommandAction);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\type\OnlineCommandAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */