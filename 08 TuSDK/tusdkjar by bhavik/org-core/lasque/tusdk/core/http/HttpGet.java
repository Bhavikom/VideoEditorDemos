package org.lasque.tusdk.core.http;

import java.net.URL;

public class HttpGet
  extends HttpUriRequest
{
  public static final String METHOD_NAME = "GET";
  
  public HttpGet() {}
  
  public HttpGet(String paramString)
  {
    super(paramString);
  }
  
  public HttpGet(URL paramURL)
  {
    super(paramURL);
  }
  
  public boolean canOutput()
  {
    return false;
  }
  
  public boolean canUseCache()
  {
    return true;
  }
  
  public String getMethod()
  {
    return "GET";
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\HttpGet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */