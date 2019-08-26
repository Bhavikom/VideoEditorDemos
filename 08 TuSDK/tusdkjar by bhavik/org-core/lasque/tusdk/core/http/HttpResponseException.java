package org.lasque.tusdk.core.http;

import java.io.IOException;

public class HttpResponseException
  extends IOException
{
  private final int a;
  
  public HttpResponseException(int paramInt, String paramString)
  {
    super(paramString);
    this.a = paramInt;
  }
  
  public int getStateCode()
  {
    return this.a;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\HttpResponseException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */