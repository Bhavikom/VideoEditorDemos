package org.lasque.tusdk.modules.components;

import org.lasque.tusdk.core.utils.TuSdkError;

public enum ComponentErrorType
{
  int a;
  String b;
  
  private ComponentErrorType(int paramInt, String paramString)
  {
    this.a = paramInt;
    this.b = paramString;
  }
  
  public int getErrorCode()
  {
    return this.a;
  }
  
  public String getMsg()
  {
    return this.b;
  }
  
  public TuSdkError getError(Object paramObject)
  {
    String str = String.format("Component Error %s(%s): %s", new Object[] { paramObject, Integer.valueOf(this.a), this.b });
    TuSdkError localTuSdkError = new TuSdkError(str, this.a);
    return localTuSdkError;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\ComponentErrorType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */