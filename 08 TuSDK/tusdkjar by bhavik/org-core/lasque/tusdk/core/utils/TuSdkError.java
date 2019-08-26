package org.lasque.tusdk.core.utils;

public class TuSdkError
  extends Error
{
  private int a;
  
  public int getErrorCode()
  {
    return this.a;
  }
  
  public TuSdkError() {}
  
  public TuSdkError(String paramString)
  {
    super(paramString);
  }
  
  public TuSdkError(String paramString, int paramInt)
  {
    super(paramString);
    this.a = paramInt;
  }
  
  public TuSdkError(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
  
  public TuSdkError(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\TuSdkError.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */