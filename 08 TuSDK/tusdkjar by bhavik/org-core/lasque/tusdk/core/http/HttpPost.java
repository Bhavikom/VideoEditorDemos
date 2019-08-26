package org.lasque.tusdk.core.http;

import java.net.URL;

public class HttpPost
  extends HttpUriRequest
{
  public static final String METHOD_NAME = "POST";
  
  public HttpPost() {}
  
  public HttpPost(String paramString)
  {
    super(paramString);
  }
  
  public HttpPost(URL paramURL)
  {
    super(paramURL);
  }
  
  public boolean canOutput()
  {
    return true;
  }
  
  public boolean canUseCache()
  {
    return false;
  }
  
  public String getMethod()
  {
    return "POST";
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\HttpPost.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */