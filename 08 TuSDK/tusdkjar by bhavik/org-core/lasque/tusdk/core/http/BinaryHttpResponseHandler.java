package org.lasque.tusdk.core.http;

import android.os.Looper;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.lasque.tusdk.core.utils.TLog;

public abstract class BinaryHttpResponseHandler
  extends ClearHttpResponseHandler
{
  private String[] a = { "application/octet-stream", "image/jpeg", "image/png", "image/gif" };
  
  public BinaryHttpResponseHandler() {}
  
  public BinaryHttpResponseHandler(String[] paramArrayOfString)
  {
    if (paramArrayOfString != null) {
      this.a = paramArrayOfString;
    } else {
      TLog.e("Constructor passed allowedContentTypes was null !", new Object[0]);
    }
  }
  
  public BinaryHttpResponseHandler(String[] paramArrayOfString, Looper paramLooper)
  {
    super(paramLooper);
    if (paramArrayOfString != null) {
      this.a = paramArrayOfString;
    } else {
      TLog.e("Constructor passed allowedContentTypes was null !", new Object[0]);
    }
  }
  
  public String[] getAllowedContentTypes()
  {
    return this.a;
  }
  
  public abstract void onSuccess(int paramInt, List<HttpHeader> paramList, byte[] paramArrayOfByte);
  
  public abstract void onFailure(int paramInt, List<HttpHeader> paramList, byte[] paramArrayOfByte, Throwable paramThrowable);
  
  public final void sendResponseMessage(HttpResponse paramHttpResponse)
  {
    List localList = paramHttpResponse.getHeaders("Content-Type");
    if ((localList == null) || (localList.size() != 1))
    {
      sendFailureMessage(paramHttpResponse.getResponseCode(), paramHttpResponse.getAllHeaders(), null, new HttpResponseException(paramHttpResponse.getResponseCode(), "None, or more than one, Content-Type Header found!"));
      return;
    }
    HttpHeader localHttpHeader = (HttpHeader)localList.get(0);
    int i = 0;
    for (String str : getAllowedContentTypes()) {
      try
      {
        if (Pattern.matches(str, localHttpHeader.getValue())) {
          i = 1;
        }
      }
      catch (PatternSyntaxException localPatternSyntaxException)
      {
        TLog.e("Given pattern is not valid (%s): %s", new Object[] { str, localPatternSyntaxException });
      }
    }
    if (i == 0)
    {
      sendFailureMessage(paramHttpResponse.getResponseCode(), paramHttpResponse.getAllHeaders(), null, new HttpResponseException(paramHttpResponse.getResponseCode(), "Content-Type (" + localHttpHeader.getValue() + ") not allowed!"));
      return;
    }
    super.sendResponseMessage(paramHttpResponse);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\BinaryHttpResponseHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */