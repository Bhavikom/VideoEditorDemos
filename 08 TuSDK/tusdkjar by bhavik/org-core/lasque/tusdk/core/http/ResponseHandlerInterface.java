package org.lasque.tusdk.core.http;

import java.net.URL;
import java.util.List;

public abstract interface ResponseHandlerInterface
{
  public abstract void sendResponseMessage(HttpResponse paramHttpResponse);
  
  public abstract void sendStartMessage();
  
  public abstract void sendFinishMessage();
  
  public abstract void sendProgressMessage(long paramLong1, long paramLong2);
  
  public abstract void sendCancelMessage();
  
  public abstract void sendSuccessMessage(int paramInt, List<HttpHeader> paramList, byte[] paramArrayOfByte);
  
  public abstract void sendFailureMessage(int paramInt, List<HttpHeader> paramList, byte[] paramArrayOfByte, Throwable paramThrowable);
  
  public abstract void sendRetryMessage(int paramInt);
  
  public abstract URL getRequestURL();
  
  public abstract void setRequestURL(URL paramURL);
  
  public abstract List<HttpHeader> getRequestHeaders();
  
  public abstract void setRequestHeaders(List<HttpHeader> paramList);
  
  public abstract boolean getUseSynchronousMode();
  
  public abstract void setUseSynchronousMode(boolean paramBoolean);
  
  public abstract boolean getUsePoolThread();
  
  public abstract void setUsePoolThread(boolean paramBoolean);
  
  public abstract void onPreProcessResponse(ResponseHandlerInterface paramResponseHandlerInterface, HttpResponse paramHttpResponse);
  
  public abstract void onPostProcessResponse(ResponseHandlerInterface paramResponseHandlerInterface, HttpResponse paramHttpResponse);
  
  public abstract Object getTag();
  
  public abstract void setTag(Object paramObject);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\ResponseHandlerInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */