package org.lasque.tusdk.core.http;

import android.content.Context;
import android.os.Looper;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.TLog;

public class ClearHttpClient
{
  public static final String LOG_TAG = "ClearHttpClient";
  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String HEADER_CONTENT_RANGE = "Content-Range";
  public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
  public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
  public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
  public static final String ENCODING_GZIP = "gzip";
  public static final int DEFAULT_MAX_CONNECTIONS = 10;
  public static final int DEFAULT_SOCKET_TIMEOUT = 10000;
  public static final int DEFAULT_MAX_RETRIES = 5;
  public static final int DEFAULT_RETRY_SLEEP_TIME_MILLIS = 1500;
  public static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
  private final Map<Context, List<RequestHandle>> a = Collections.synchronizedMap(new WeakHashMap());
  private final Map<String, String> b = new HashMap();
  private int c = 10;
  private int d = 10000;
  private int e = 10000;
  private ExecutorService f = getDefaultThreadPool();
  private boolean g = true;
  private final HttpManager h = new HttpManager();
  
  public int getMaxConnections()
  {
    return this.c;
  }
  
  public void setMaxConnections(int paramInt)
  {
    if (this.c < 1) {
      this.c = 10;
    }
    this.c = this.c;
    this.h.setMaxConnections(this.c);
  }
  
  public void setTimeout(int paramInt)
  {
    paramInt = paramInt < 1000 ? 10000 : paramInt;
    setConnectTimeout(paramInt);
    setResponseTimeout(paramInt);
  }
  
  public int getConnectTimeout()
  {
    return this.d;
  }
  
  public void setConnectTimeout(int paramInt)
  {
    this.d = (paramInt < 1000 ? 10000 : paramInt);
    this.h.setConnectTimeout(this.d);
  }
  
  public int getResponseTimeout()
  {
    return this.e;
  }
  
  public void setResponseTimeout(int paramInt)
  {
    this.e = (paramInt < 1000 ? 10000 : paramInt);
    this.h.setResponseTimeout(this.e);
  }
  
  protected ExecutorService getDefaultThreadPool()
  {
    return Executors.newCachedThreadPool();
  }
  
  public ExecutorService getThreadPool()
  {
    return this.f;
  }
  
  public void setThreadPool(ExecutorService paramExecutorService)
  {
    this.f = paramExecutorService;
  }
  
  public boolean isUrlEncodingEnabled()
  {
    return this.g;
  }
  
  public void setUrlEncodingEnabled(boolean paramBoolean)
  {
    this.g = paramBoolean;
  }
  
  public void setEnableRedirct(boolean paramBoolean)
  {
    this.h.setEnableRedirct(paramBoolean);
  }
  
  public HttpManager getHttpManager()
  {
    return this.h;
  }
  
  public ClearHttpClient()
  {
    this(80);
  }
  
  public ClearHttpClient(int paramInt)
  {
    this(paramInt, 443);
  }
  
  private ClearHttpClient(int paramInt1, int paramInt2)
  {
    this.h.setMaxConnections(getMaxConnections());
    this.h.setConnectTimeout(getConnectTimeout());
    this.h.setResponseTimeout(getResponseTimeout());
    this.h.setSocketBufferSize(8192);
    this.h.setDefaultMaxRetries(5);
    this.h.setDefaultRetrySleepTimemillis(1500);
    this.h.setHttpPort(paramInt1);
    this.h.setHttpsPort(paramInt2);
    this.h.addRequestInterceptor(new HttpManager.HttpRequestInterceptor()
    {
      public void process(HttpUriRequest paramAnonymousHttpUriRequest)
      {
        if (!paramAnonymousHttpUriRequest.containsHeader("Accept-Encoding")) {
          paramAnonymousHttpUriRequest.addHeader("Accept-Encoding", "gzip");
        }
        Iterator localIterator = ClearHttpClient.a(ClearHttpClient.this).keySet().iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          if (paramAnonymousHttpUriRequest.containsHeader(str))
          {
            HttpHeader localHttpHeader = paramAnonymousHttpUriRequest.getFirstHeader(str);
            paramAnonymousHttpUriRequest.removeHeader(localHttpHeader);
          }
          paramAnonymousHttpUriRequest.addHeader(str, (String)ClearHttpClient.a(ClearHttpClient.this).get(str));
        }
      }
    });
    this.h.addResponseInterceptor(new HttpManager.HttpResponseInterceptor()
    {
      public void process(HttpResponse paramAnonymousHttpResponse)
      {
        HttpEntity localHttpEntity = paramAnonymousHttpResponse.getEntity();
        if (localHttpEntity == null) {
          return;
        }
        HttpHeader localHttpHeader = localHttpEntity.getContentEncoding();
        if ((localHttpHeader != null) && (localHttpHeader.equalsValue("gzip"))) {
          paramAnonymousHttpResponse.setEntity(new ClearHttpClient.InflatingEntity(localHttpEntity));
        }
      }
    });
  }
  
  public void removeAllHeaders()
  {
    this.b.clear();
  }
  
  public void addHeader(String paramString1, String paramString2)
  {
    this.b.put(paramString1, paramString2);
  }
  
  public void removeHeader(String paramString)
  {
    this.b.remove(paramString);
  }
  
  public void cancelRequests(Context paramContext, final boolean paramBoolean)
  {
    if (paramContext == null)
    {
      TLog.e("Passed null Context to cancelRequests", new Object[0]);
      return;
    }
    final List localList = (List)this.a.get(paramContext);
    this.a.remove(paramContext);
    if (Looper.myLooper() == Looper.getMainLooper())
    {
      Runnable local3 = new Runnable()
      {
        public void run()
        {
          ClearHttpClient.a(ClearHttpClient.this, localList, paramBoolean);
        }
      };
      this.f.submit(local3);
    }
    else
    {
      a(localList, paramBoolean);
    }
  }
  
  private void a(List<RequestHandle> paramList, boolean paramBoolean)
  {
    if (paramList != null)
    {
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        RequestHandle localRequestHandle = (RequestHandle)localIterator.next();
        localRequestHandle.cancel(paramBoolean);
      }
    }
  }
  
  public void cancelAllRequests(boolean paramBoolean)
  {
    Iterator localIterator1 = this.a.values().iterator();
    while (localIterator1.hasNext())
    {
      List localList = (List)localIterator1.next();
      if (localList != null)
      {
        Iterator localIterator2 = localList.iterator();
        while (localIterator2.hasNext())
        {
          RequestHandle localRequestHandle = (RequestHandle)localIterator2.next();
          localRequestHandle.cancel(paramBoolean);
        }
      }
    }
    this.a.clear();
  }
  
  public void cancelRequestsByTAG(Object paramObject, boolean paramBoolean)
  {
    if (paramObject == null)
    {
      TLog.d("cancelRequestsByTAG, passed TAG is null, cannot proceed", new Object[0]);
      return;
    }
    Iterator localIterator1 = this.a.values().iterator();
    while (localIterator1.hasNext())
    {
      List localList = (List)localIterator1.next();
      if (localList != null)
      {
        Iterator localIterator2 = localList.iterator();
        while (localIterator2.hasNext())
        {
          RequestHandle localRequestHandle = (RequestHandle)localIterator2.next();
          if (paramObject.equals(localRequestHandle.getTag())) {
            localRequestHandle.cancel(paramBoolean);
          }
        }
      }
    }
  }
  
  public RequestHandle get(String paramString, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    return get(null, paramString, null, paramResponseHandlerInterface);
  }
  
  public RequestHandle get(String paramString, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    return get(null, paramString, paramRequestParams, paramResponseHandlerInterface);
  }
  
  public RequestHandle get(Context paramContext, String paramString, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    return get(paramContext, paramString, null, paramResponseHandlerInterface);
  }
  
  public RequestHandle get(Context paramContext, String paramString, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    return sendRequest(this.h, new HttpGet(getUrlWithQueryString(this.g, paramString, paramRequestParams)), null, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle get(Context paramContext, String paramString, List<HttpHeader> paramList, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    HttpGet localHttpGet = new HttpGet(getUrlWithQueryString(this.g, paramString, paramRequestParams));
    if (paramList != null) {
      localHttpGet.setHeaders(paramList);
    }
    return sendRequest(this.h, localHttpGet, null, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle get(Context paramContext, String paramString1, HttpEntity paramHttpEntity, String paramString2, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    return sendRequest(this.h, a(new HttpGet(URLEncodedUtils.getURL(paramString1)), paramHttpEntity), paramString2, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle post(String paramString, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    return post(null, paramString, null, paramResponseHandlerInterface);
  }
  
  public RequestHandle post(String paramString, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    return post(null, paramString, paramRequestParams, paramResponseHandlerInterface);
  }
  
  public RequestHandle post(Context paramContext, String paramString, RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    return post(paramContext, paramString, a(paramRequestParams, paramResponseHandlerInterface), null, paramResponseHandlerInterface);
  }
  
  public RequestHandle post(Context paramContext, String paramString1, HttpEntity paramHttpEntity, String paramString2, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    return sendRequest(this.h, a(new HttpPost(URLEncodedUtils.getURL(paramString1)), paramHttpEntity), paramString2, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle post(Context paramContext, String paramString1, List<HttpHeader> paramList, RequestParams paramRequestParams, String paramString2, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    HttpPost localHttpPost = new HttpPost(URLEncodedUtils.getURL(paramString1));
    if (paramRequestParams != null) {
      localHttpPost.setEntity(a(paramRequestParams, paramResponseHandlerInterface));
    }
    if (paramList != null) {
      localHttpPost.setHeaders(paramList);
    }
    return sendRequest(this.h, localHttpPost, paramString2, paramResponseHandlerInterface, paramContext);
  }
  
  public RequestHandle post(Context paramContext, String paramString1, List<HttpHeader> paramList, HttpEntity paramHttpEntity, String paramString2, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    HttpUriRequest localHttpUriRequest = a(new HttpPost(URLEncodedUtils.getURL(paramString1)), paramHttpEntity);
    if (paramList != null) {
      localHttpUriRequest.setHeaders(paramList);
    }
    return sendRequest(this.h, localHttpUriRequest, paramString2, paramResponseHandlerInterface, paramContext);
  }
  
  protected ClearHttpRequest newClearHttpRequest(HttpManager paramHttpManager, HttpUriRequest paramHttpUriRequest, String paramString, ResponseHandlerInterface paramResponseHandlerInterface, Context paramContext)
  {
    return new ClearHttpRequest(paramHttpManager, paramHttpUriRequest, paramResponseHandlerInterface);
  }
  
  protected RequestHandle sendRequest(HttpManager paramHttpManager, HttpUriRequest paramHttpUriRequest, String paramString, ResponseHandlerInterface paramResponseHandlerInterface, Context paramContext)
  {
    if (paramHttpUriRequest == null) {
      throw new IllegalArgumentException("HttpUriRequest must not be null");
    }
    if (paramResponseHandlerInterface == null) {
      throw new IllegalArgumentException("ResponseHandler must not be null");
    }
    if ((paramResponseHandlerInterface.getUseSynchronousMode()) && (!paramResponseHandlerInterface.getUsePoolThread())) {
      throw new IllegalArgumentException("Synchronous ResponseHandler used in ClearHttpClient. You should create your response handler in a looper thread or use SyncHttpClient instead.");
    }
    if (paramString != null) {
      if ((paramHttpUriRequest.getEntity() != null) && (paramHttpUriRequest.containsHeader("Content-Type"))) {
        TLog.w("Passed contentType will be ignored because HttpEntity sets content type", new Object[0]);
      } else {
        paramHttpUriRequest.setHeader("Content-Type", paramString);
      }
    }
    paramResponseHandlerInterface.setRequestHeaders(paramHttpUriRequest.getAllHeaders());
    paramResponseHandlerInterface.setRequestURL(paramHttpUriRequest.getURL());
    ClearHttpRequest localClearHttpRequest = newClearHttpRequest(paramHttpManager, paramHttpUriRequest, paramString, paramResponseHandlerInterface, paramContext);
    this.f.submit(localClearHttpRequest);
    RequestHandle localRequestHandle = new RequestHandle(localClearHttpRequest);
    if (paramContext != null)
    {
      List localList;
      synchronized (this.a)
      {
        localList = (List)this.a.get(paramContext);
        if (localList == null)
        {
          localList = Collections.synchronizedList(new LinkedList());
          this.a.put(paramContext, localList);
        }
      }
      localList.add(localRequestHandle);
      ??? = localList.iterator();
      while (((Iterator)???).hasNext()) {
        if (((RequestHandle)((Iterator)???).next()).shouldBeGarbageCollected()) {
          ((Iterator)???).remove();
        }
      }
    }
    return localRequestHandle;
  }
  
  private HttpUriRequest a(HttpUriRequest paramHttpUriRequest, HttpEntity paramHttpEntity)
  {
    if (paramHttpEntity != null) {
      paramHttpUriRequest.setEntity(paramHttpEntity);
    }
    return paramHttpUriRequest;
  }
  
  private HttpEntity a(RequestParams paramRequestParams, ResponseHandlerInterface paramResponseHandlerInterface)
  {
    HttpEntity localHttpEntity = null;
    try
    {
      if (paramRequestParams != null) {
        localHttpEntity = paramRequestParams.getEntity(paramResponseHandlerInterface);
      }
    }
    catch (IOException localIOException)
    {
      if (paramResponseHandlerInterface != null) {
        paramResponseHandlerInterface.sendFailureMessage(0, null, null, localIOException);
      } else {
        localIOException.printStackTrace();
      }
    }
    return localHttpEntity;
  }
  
  public static String getUrlWithQueryString(boolean paramBoolean, String paramString, RequestParams paramRequestParams)
  {
    if (paramString == null) {
      return null;
    }
    if (paramBoolean) {
      try
      {
        String str1 = URLDecoder.decode(paramString, "UTF-8");
        URL localURL = new URL(str1);
        URI localURI = new URI(localURL.getProtocol(), localURL.getUserInfo(), localURL.getHost(), localURL.getPort(), localURL.getPath(), localURL.getQuery(), localURL.getRef());
        paramString = localURI.toASCIIString();
      }
      catch (Exception localException)
      {
        TLog.e("getUrlWithQueryString encoding URL", new Object[] { localException });
      }
    }
    if (paramRequestParams != null)
    {
      String str2 = paramRequestParams.getParamString().trim();
      if ((!str2.equals("")) && (!str2.equals("?")))
      {
        paramString = paramString + (paramString.contains("?") ? "&" : "?");
        paramString = paramString + str2;
      }
    }
    return paramString;
  }
  
  public static boolean isInputStreamGZIPCompressed(PushbackInputStream paramPushbackInputStream)
  {
    if (paramPushbackInputStream == null) {
      return false;
    }
    byte[] arrayOfByte = new byte[2];
    int i = 0;
    try
    {
      while (i < 2)
      {
        j = paramPushbackInputStream.read(arrayOfByte, i, 2 - i);
        if (j < 0)
        {
          boolean bool = false;
          return bool;
        }
        i += j;
      }
    }
    finally
    {
      paramPushbackInputStream.unread(arrayOfByte, 0, i);
    }
    int j = arrayOfByte[0] & 0xFF | arrayOfByte[1] << 8 & 0xFF00;
    return 35615 == j;
  }
  
  public static void endEntityViaReflection(HttpEntity paramHttpEntity)
  {
    if (!(paramHttpEntity instanceof HttpEntityWrapper)) {
      return;
    }
    try
    {
      ((HttpEntityWrapper)paramHttpEntity).consumeWrappedEntity();
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "endEntityViaReflection: %s", new Object[] { paramHttpEntity });
    }
  }
  
  public static void test()
  {
    TLog.d("test start", new Object[0]);
    ClearHttpClient localClearHttpClient = new ClearHttpClient(80);
    localClearHttpClient.setEnableRedirct(true);
    localClearHttpClient.get("http://www.tusdk.com/", new TextHttpResponseHandler()
    {
      public void onFailure(int paramAnonymousInt, List<HttpHeader> paramAnonymousList, String paramAnonymousString, Throwable paramAnonymousThrowable)
      {
        TLog.d("onFailure: %s | %s | %s | %s", new Object[] { Integer.valueOf(paramAnonymousInt), paramAnonymousList, paramAnonymousString, paramAnonymousThrowable });
      }
      
      public void onSuccess(int paramAnonymousInt, List<HttpHeader> paramAnonymousList, String paramAnonymousString)
      {
        TLog.d("onSuccess: %s | %s | %s", new Object[] { Integer.valueOf(paramAnonymousInt), paramAnonymousList, paramAnonymousString });
      }
    });
  }
  
  private static class InflatingEntity
    extends HttpEntityWrapper
  {
    InputStream a;
    PushbackInputStream b;
    GZIPInputStream c;
    
    public InflatingEntity(HttpEntity paramHttpEntity)
    {
      super();
    }
    
    public InputStream getContent()
    {
      this.a = this.mWrappedEntity.getContent();
      this.b = new PushbackInputStream(this.a, 2);
      if (ClearHttpClient.isInputStreamGZIPCompressed(this.b))
      {
        this.c = new GZIPInputStream(this.b);
        return this.c;
      }
      return this.b;
    }
    
    public long getContentLength()
    {
      return this.mWrappedEntity == null ? 0L : this.mWrappedEntity.getContentLength();
    }
    
    public void consumeContent()
    {
      FileHelper.safeClose(this.a);
      FileHelper.safeClose(this.b);
      FileHelper.safeClose(this.c);
      super.consumeContent();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\ClearHttpClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */