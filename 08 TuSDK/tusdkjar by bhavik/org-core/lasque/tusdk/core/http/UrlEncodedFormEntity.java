package org.lasque.tusdk.core.http;

import java.util.List;

public class UrlEncodedFormEntity
  extends StringEntity
{
  public UrlEncodedFormEntity(List<URLEncodedUtils.BasicNameValuePair> paramList, String paramString)
  {
    super(URLEncodedUtils.format(paramList, paramString), "application/x-www-form-urlencoded", paramString);
  }
  
  public UrlEncodedFormEntity(List<URLEncodedUtils.BasicNameValuePair> paramList)
  {
    this(paramList, null);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\UrlEncodedFormEntity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */