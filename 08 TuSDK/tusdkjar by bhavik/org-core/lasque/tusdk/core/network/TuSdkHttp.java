package org.lasque.tusdk.core.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.http.BlackholeHttpResponseHandler;
import org.lasque.tusdk.core.http.ClearHttpClient;
import org.lasque.tusdk.core.http.HttpHeader;
import org.lasque.tusdk.core.http.RequestHandle;
import org.lasque.tusdk.core.http.RequestParams;
import org.lasque.tusdk.core.http.ResponseHandlerInterface;
import org.lasque.tusdk.core.utils.StringHelper;

public class TuSdkHttp
  extends ClearHttpClient
{
  public static final String Content_Disposition = "Content-Disposition";
  private ResponseHandlerInterface a = new BlackholeHttpResponseHandler();
  
  public static String attachmentFileName(List<HttpHeader> paramList)
  {
    if ((paramList == null) || (paramList.isEmpty())) {
      return null;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      HttpHeader localHttpHeader = (HttpHeader)localIterator.next();
      if (localHttpHeader.equalsName("Content-Disposition"))
      {
        ArrayList localArrayList = StringHelper.matchStrings(localHttpHeader.getValue(), "attachment; filename=(.*)$");
        if ((localArrayList == null) || (localArrayList.size() == 1)) {
          break;
        }
        String str = (String)localArrayList.get(1);
        if (str == null) {
          break;
        }
        return str.replace("\"", "");
      }
    }
    return null;
  }
  
  public TuSdkHttp(int paramInt)
  {
    super(paramInt);
  }
  
  public RequestHandle get(String paramString, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    if (paramResponseHandlerInterface == null) {
      paramResponseHandlerInterface = this.a;
    }
    return super.get(paramString, paramRequestParams, paramResponseHandlerInterface);
  }
  
  public RequestHandle post(String paramString, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    if (paramResponseHandlerInterface == null) {
      paramResponseHandlerInterface = this.a;
    }
    return super.post(paramString, paramRequestParams, paramResponseHandlerInterface);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\TuSdkHttp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */