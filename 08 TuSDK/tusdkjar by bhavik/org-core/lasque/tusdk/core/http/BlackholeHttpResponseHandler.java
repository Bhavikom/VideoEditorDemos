package org.lasque.tusdk.core.http;

import java.util.List;

public class BlackholeHttpResponseHandler
  extends ClearHttpResponseHandler
{
  public void onSuccess(int paramInt, List<HttpHeader> paramList, byte[] paramArrayOfByte) {}
  
  public void onFailure(int paramInt, List<HttpHeader> paramList, byte[] paramArrayOfByte, Throwable paramThrowable) {}
  
  public void onProgress(long paramLong1, long paramLong2) {}
  
  public void onCancel() {}
  
  public void onFinish() {}
  
  public void onPostProcessResponse(ResponseHandlerInterface paramResponseHandlerInterface, HttpResponse paramHttpResponse) {}
  
  public void onPreProcessResponse(ResponseHandlerInterface paramResponseHandlerInterface, HttpResponse paramHttpResponse) {}
  
  public void onRetry(int paramInt) {}
  
  public void onStart() {}
  
  public void onUserException(Throwable paramThrowable) {}
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\BlackholeHttpResponseHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */