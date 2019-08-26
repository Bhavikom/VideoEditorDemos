package org.lasque.tusdk.core.http;

import android.os.SystemClock;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLException;

public class HttpManager
{
  private static final HashSet<Class<?>> a = new HashSet();
  private static final HashSet<Class<?>> b = new HashSet();
  private int c;
  private int d;
  private int e;
  private int f;
  private int g;
  private int h;
  private int i;
  private int j;
  private boolean k;
  private List<HttpRequestInterceptor> l = new ArrayList();
  private List<HttpResponseInterceptor> m = new ArrayList();
  
  public int getMaxConnections()
  {
    return this.c;
  }
  
  public void setMaxConnections(int paramInt)
  {
    this.c = paramInt;
  }
  
  public int getConnectTimeout()
  {
    return this.d;
  }
  
  public void setConnectTimeout(int paramInt)
  {
    this.d = paramInt;
  }
  
  public int getResponseTimeout()
  {
    return this.e;
  }
  
  public void setResponseTimeout(int paramInt)
  {
    this.e = paramInt;
  }
  
  public int getSocketBufferSize()
  {
    return this.f;
  }
  
  public void setSocketBufferSize(int paramInt)
  {
    this.f = paramInt;
  }
  
  public int getDefaultMaxRetries()
  {
    return this.g;
  }
  
  public void setDefaultMaxRetries(int paramInt)
  {
    this.g = paramInt;
  }
  
  public int getDefaultRetrySleepTimemillis()
  {
    return this.h;
  }
  
  public void setDefaultRetrySleepTimemillis(int paramInt)
  {
    this.h = paramInt;
  }
  
  public int getHttpPort()
  {
    return this.i;
  }
  
  public void setHttpPort(int paramInt)
  {
    this.i = paramInt;
  }
  
  public int getHttpsPort()
  {
    return this.j;
  }
  
  public void setHttpsPort(int paramInt)
  {
    this.j = paramInt;
  }
  
  public boolean isEnableRedirct()
  {
    return this.k;
  }
  
  public void setEnableRedirct(boolean paramBoolean)
  {
    this.k = paramBoolean;
  }
  
  public void addRequestInterceptor(HttpRequestInterceptor paramHttpRequestInterceptor)
  {
    if (paramHttpRequestInterceptor == null) {
      return;
    }
    this.l.add(paramHttpRequestInterceptor);
  }
  
  public void addResponseInterceptor(HttpResponseInterceptor paramHttpResponseInterceptor)
  {
    if (paramHttpResponseInterceptor == null) {
      return;
    }
    this.m.add(paramHttpResponseInterceptor);
  }
  
  public boolean retryRequest(IOException paramIOException, int paramInt)
  {
    boolean bool = true;
    if (paramInt > this.g) {
      bool = false;
    } else if (a(a, paramIOException)) {
      bool = true;
    } else if (a(b, paramIOException)) {
      bool = false;
    }
    if ((!bool) || (bool)) {
      SystemClock.sleep(this.h);
    } else {
      paramIOException.printStackTrace();
    }
    return bool;
  }
  
  private boolean a(HashSet<Class<?>> paramHashSet, Throwable paramThrowable)
  {
    Iterator localIterator = paramHashSet.iterator();
    while (localIterator.hasNext())
    {
      Class localClass = (Class)localIterator.next();
      if (localClass.isInstance(paramThrowable)) {
        return true;
      }
    }
    return false;
  }
  
  public HttpResponse execute(HttpUriRequest paramHttpUriRequest)
  {
    a(paramHttpUriRequest);
    HttpURLConnection localHttpURLConnection = paramHttpUriRequest.openConnection();
    localHttpURLConnection.setConnectTimeout(getConnectTimeout());
    localHttpURLConnection.setReadTimeout(getResponseTimeout());
    localHttpURLConnection.setInstanceFollowRedirects(isEnableRedirct());
    localHttpURLConnection.connect();
    HttpResponse localHttpResponse = new HttpResponse(localHttpURLConnection);
    if (paramHttpUriRequest.canOutput())
    {
      localHttpResponse.openOutputStream();
      paramHttpUriRequest.getEntity().writeTo(localHttpResponse.getOutputStream());
    }
    return localHttpResponse;
  }
  
  private void a(HttpUriRequest paramHttpUriRequest)
  {
    Iterator localIterator = this.l.iterator();
    while (localIterator.hasNext())
    {
      HttpRequestInterceptor localHttpRequestInterceptor = (HttpRequestInterceptor)localIterator.next();
      localHttpRequestInterceptor.process(paramHttpUriRequest);
    }
  }
  
  public void executeResponse(HttpResponse paramHttpResponse, HttpUriRequest paramHttpUriRequest)
  {
    paramHttpResponse.openInputStream();
    a(paramHttpResponse);
  }
  
  private void a(HttpResponse paramHttpResponse)
  {
    Iterator localIterator = this.m.iterator();
    while (localIterator.hasNext())
    {
      HttpResponseInterceptor localHttpResponseInterceptor = (HttpResponseInterceptor)localIterator.next();
      localHttpResponseInterceptor.process(paramHttpResponse);
    }
  }
  
  static
  {
    a.add(UnknownHostException.class);
    a.add(SocketException.class);
    b.add(InterruptedIOException.class);
    b.add(SSLException.class);
  }
  
  public static abstract interface HttpResponseInterceptor
  {
    public abstract void process(HttpResponse paramHttpResponse);
  }
  
  public static abstract interface HttpRequestInterceptor
  {
    public abstract void process(HttpUriRequest paramHttpUriRequest);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\HttpManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */