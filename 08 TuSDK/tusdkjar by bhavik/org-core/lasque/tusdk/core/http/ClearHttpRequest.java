package org.lasque.tusdk.core.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.lasque.tusdk.core.utils.ReflectUtils;
import org.lasque.tusdk.core.utils.TLog;

public class ClearHttpRequest
  implements Runnable
{
  private final HttpManager a;
  private final HttpUriRequest b;
  private final ResponseHandlerInterface c;
  private final AtomicBoolean d = new AtomicBoolean();
  private int e;
  private boolean f;
  private volatile boolean g;
  private boolean h;
  
  public ClearHttpRequest(HttpManager paramHttpManager, HttpUriRequest paramHttpUriRequest, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    this.a = ((HttpManager)ReflectUtils.notNull(paramHttpManager, "httpManager"));
    this.b = ((HttpUriRequest)ReflectUtils.notNull(paramHttpUriRequest, "uriRequest"));
    this.c = ((ResponseHandlerInterface)ReflectUtils.notNull(paramResponseHandlerInterface, "responseHandler"));
  }
  
  public void onPreProcessRequest(ClearHttpRequest paramClearHttpRequest) {}
  
  public void onPostProcessRequest(ClearHttpRequest paramClearHttpRequest) {}
  
  public void run()
  {
    if (isCancelled()) {
      return;
    }
    if (!this.h)
    {
      this.h = true;
      onPreProcessRequest(this);
    }
    if (isCancelled()) {
      return;
    }
    this.c.sendStartMessage();
    if (isCancelled()) {
      return;
    }
    try
    {
      b();
    }
    catch (IOException localIOException)
    {
      if (!isCancelled()) {
        this.c.sendFailureMessage(0, null, null, localIOException);
      } else {
        TLog.e("makeRequestWithRetries returned error", new Object[] { localIOException });
      }
    }
    if (isCancelled()) {
      return;
    }
    this.c.sendFinishMessage();
    if (isCancelled()) {
      return;
    }
    onPostProcessRequest(this);
    this.g = true;
  }
  
  private void a()
  {
    if (isCancelled()) {
      return;
    }
    if (this.b.getURL().getProtocol() == null) {
      throw new MalformedURLException("No valid URI scheme was provided");
    }
    if ((this.c instanceof RangeFileHttpResponseHandler)) {
      ((RangeFileHttpResponseHandler)this.c).updateRequestHeaders(this.b);
    }
    HttpResponse localHttpResponse = this.a.execute(this.b);
    if (isCancelled()) {
      return;
    }
    this.c.onPreProcessResponse(this.c, localHttpResponse);
    if (isCancelled()) {
      return;
    }
    this.a.executeResponse(localHttpResponse, this.b);
    if (isCancelled()) {
      return;
    }
    this.c.sendResponseMessage(localHttpResponse);
    if (isCancelled()) {
      return;
    }
    this.c.onPostProcessResponse(this.c, localHttpResponse);
    localHttpResponse.disconnect();
  }
  
  private void b()
  {
    boolean bool = true;
    Object localObject = null;
    try
    {
      while (bool)
      {
        try
        {
          a();
          return;
        }
        catch (UnknownHostException localUnknownHostException)
        {
          localObject = new IOException("UnknownHostException exception: " + localUnknownHostException.getMessage());
          bool = (this.e > 0) && (this.a.retryRequest(localUnknownHostException, ++this.e));
        }
        catch (NullPointerException localNullPointerException)
        {
          localObject = new IOException("NPE in HttpClient: " + localNullPointerException.getMessage());
          bool = this.a.retryRequest((IOException)localObject, ++this.e);
        }
        catch (IOException localIOException)
        {
          if (isCancelled()) {
            return;
          }
          localObject = localIOException;
          bool = this.a.retryRequest((IOException)localObject, ++this.e);
        }
        if (bool) {
          this.c.sendRetryMessage(this.e);
        }
      }
    }
    catch (Exception localException)
    {
      TLog.e("Unhandled exception origin cause", new Object[] { localException });
      localObject = new IOException("Unhandled exception: " + localException.getMessage());
    }
    throw ((Throwable)localObject);
  }
  
  public boolean isCancelled()
  {
    boolean bool = this.d.get();
    if (bool) {
      c();
    }
    return bool;
  }
  
  private synchronized void c()
  {
    if ((!this.g) && (this.d.get()) && (!this.f))
    {
      this.f = true;
      this.c.sendCancelMessage();
    }
  }
  
  public boolean isDone()
  {
    return (isCancelled()) || (this.g);
  }
  
  public boolean cancel(boolean paramBoolean)
  {
    this.d.set(true);
    this.b.abort();
    return isCancelled();
  }
  
  public ClearHttpRequest setRequestTag(Object paramObject)
  {
    this.c.setTag(paramObject);
    return this;
  }
  
  public Object getTag()
  {
    return this.c.getTag();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\ClearHttpRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */