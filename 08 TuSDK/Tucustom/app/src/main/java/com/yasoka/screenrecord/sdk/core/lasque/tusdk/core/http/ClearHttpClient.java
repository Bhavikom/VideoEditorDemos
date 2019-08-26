// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http;

//import org.lasque.tusdk.core.utils.FileHelper;
import java.util.zip.GZIPInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.io.IOException;
import java.util.LinkedList;
import android.os.Looper;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Collections;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.List;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.Map;

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
    private final Map<Context, List<RequestHandle>> a;
    private final Map<String, String> b;
    private int c;
    private int d;
    private int e;
    private ExecutorService f;
    private boolean g;
    private final HttpManager h;
    
    public int getMaxConnections() {
        return this.c;
    }
    
    public void setMaxConnections(final int n) {
        if (this.c < 1) {
            this.c = 10;
        }
        this.c = this.c;
        this.h.setMaxConnections(this.c);
    }
    
    public void setTimeout(int n) {
        n = ((n < 1000) ? 10000 : n);
        this.setConnectTimeout(n);
        this.setResponseTimeout(n);
    }
    
    public int getConnectTimeout() {
        return this.d;
    }
    
    public void setConnectTimeout(final int n) {
        this.d = ((n < 1000) ? 10000 : n);
        this.h.setConnectTimeout(this.d);
    }
    
    public int getResponseTimeout() {
        return this.e;
    }
    
    public void setResponseTimeout(final int n) {
        this.e = ((n < 1000) ? 10000 : n);
        this.h.setResponseTimeout(this.e);
    }
    
    protected ExecutorService getDefaultThreadPool() {
        return Executors.newCachedThreadPool();
    }
    
    public ExecutorService getThreadPool() {
        return this.f;
    }
    
    public void setThreadPool(final ExecutorService f) {
        this.f = f;
    }
    
    public boolean isUrlEncodingEnabled() {
        return this.g;
    }
    
    public void setUrlEncodingEnabled(final boolean g) {
        this.g = g;
    }
    
    public void setEnableRedirct(final boolean enableRedirct) {
        this.h.setEnableRedirct(enableRedirct);
    }
    
    public HttpManager getHttpManager() {
        return this.h;
    }
    
    public ClearHttpClient() {
        this(80);
    }
    
    public ClearHttpClient(final int n) {
        this(n, 443);
    }
    
    private ClearHttpClient(final int httpPort, final int httpsPort) {
        this.c = 10;
        this.d = 10000;
        this.e = 10000;
        this.g = true;
        this.f = this.getDefaultThreadPool();
        this.a = Collections.synchronizedMap(new WeakHashMap<Context, List<RequestHandle>>());
        this.b = new HashMap<String, String>();
        (this.h = new HttpManager()).setMaxConnections(this.getMaxConnections());
        this.h.setConnectTimeout(this.getConnectTimeout());
        this.h.setResponseTimeout(this.getResponseTimeout());
        this.h.setSocketBufferSize(8192);
        this.h.setDefaultMaxRetries(5);
        this.h.setDefaultRetrySleepTimemillis(1500);
        this.h.setHttpPort(httpPort);
        this.h.setHttpsPort(httpsPort);
        this.h.addRequestInterceptor(new HttpManager.HttpRequestInterceptor() {
            @Override
            public void process(final HttpUriRequest httpUriRequest) {
                if (!httpUriRequest.containsHeader("Accept-Encoding")) {
                    httpUriRequest.addHeader("Accept-Encoding", "gzip");
                }
                for (final String s : ClearHttpClient.this.b.keySet()) {
                    if (httpUriRequest.containsHeader(s)) {
                        httpUriRequest.removeHeader(httpUriRequest.getFirstHeader(s));
                    }
                    httpUriRequest.addHeader(s, (String)ClearHttpClient.this.b.get(s));
                }
            }
        });
        this.h.addResponseInterceptor(new HttpManager.HttpResponseInterceptor() {
            @Override
            public void process(final HttpResponse httpResponse) {
                final HttpEntity entity = httpResponse.getEntity();
                if (entity == null) {
                    return;
                }
                final HttpHeader contentEncoding = entity.getContentEncoding();
                if (contentEncoding != null && contentEncoding.equalsValue("gzip")) {
                    httpResponse.setEntity(new InflatingEntity(entity));
                }
            }
        });
    }
    
    public void removeAllHeaders() {
        this.b.clear();
    }
    
    public void addHeader(final String s, final String s2) {
        this.b.put(s, s2);
    }
    
    public void removeHeader(final String s) {
        this.b.remove(s);
    }
    
    public void cancelRequests(final Context context, final boolean b) {
        if (context == null) {
            TLog.e("Passed null Context to cancelRequests", new Object[0]);
            return;
        }
        final List<RequestHandle> list = this.a.get(context);
        this.a.remove(context);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            this.f.submit(new Runnable() {
                @Override
                public void run() {
                    ClearHttpClient.this.a(list, b);
                }
            });
        }
        else {
            this.a(list, b);
        }
    }
    
    private void a(final List<RequestHandle> list, final boolean b) {
        if (list != null) {
            final Iterator<RequestHandle> iterator = list.iterator();
            while (iterator.hasNext()) {
                iterator.next().cancel(b);
            }
        }
    }
    
    public void cancelAllRequests(final boolean b) {
        for (final List<RequestHandle> list : this.a.values()) {
            if (list != null) {
                final Iterator<RequestHandle> iterator2 = list.iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().cancel(b);
                }
            }
        }
        this.a.clear();
    }
    
    public void cancelRequestsByTAG(final Object o, final boolean b) {
        if (o == null) {
            TLog.d("cancelRequestsByTAG, passed TAG is null, cannot proceed", new Object[0]);
            return;
        }
        for (final List<RequestHandle> list : this.a.values()) {
            if (list != null) {
                for (final RequestHandle requestHandle : list) {
                    if (o.equals(requestHandle.getTag())) {
                        requestHandle.cancel(b);
                    }
                }
            }
        }
    }
    
    public RequestHandle get(final String s, final ResponseHandlerInterface responseHandlerInterface) {
        return this.get(null, s, null, responseHandlerInterface);
    }
    
    public RequestHandle get(final String s, final RequestParams requestParams, final ResponseHandlerInterface responseHandlerInterface) {
        return this.get(null, s, requestParams, responseHandlerInterface);
    }
    
    public RequestHandle get(final Context context, final String s, final ResponseHandlerInterface responseHandlerInterface) {
        return this.get(context, s, null, responseHandlerInterface);
    }
    
    public RequestHandle get(final Context context, final String s, final RequestParams requestParams, final ResponseHandlerInterface responseHandlerInterface) {
        return this.sendRequest(this.h, new HttpGet(getUrlWithQueryString(this.g, s, requestParams)), null, responseHandlerInterface, context);
    }
    
    public RequestHandle get(final Context context, final String s, final List<HttpHeader> headers, final RequestParams requestParams, final ResponseHandlerInterface responseHandlerInterface) {
        final HttpGet httpGet = new HttpGet(getUrlWithQueryString(this.g, s, requestParams));
        if (headers != null) {
            httpGet.setHeaders(headers);
        }
        return this.sendRequest(this.h, httpGet, null, responseHandlerInterface, context);
    }
    
    public RequestHandle get(final Context context, final String s, final HttpEntity httpEntity, final String s2, final ResponseHandlerInterface responseHandlerInterface) {
        return this.sendRequest(this.h, this.a(new HttpGet(URLEncodedUtils.getURL(s)), httpEntity), s2, responseHandlerInterface, context);
    }
    
    public RequestHandle post(final String s, final ResponseHandlerInterface responseHandlerInterface) {
        return this.post(null, s, null, responseHandlerInterface);
    }
    
    public RequestHandle post(final String s, final RequestParams requestParams, final ResponseHandlerInterface responseHandlerInterface) {
        return this.post(null, s, requestParams, responseHandlerInterface);
    }
    
    public RequestHandle post(final Context context, final String s, final RequestParams requestParams, final ResponseHandlerInterface responseHandlerInterface) {
        return this.post(context, s, this.a(requestParams, responseHandlerInterface), null, responseHandlerInterface);
    }
    
    public RequestHandle post(final Context context, final String s, final HttpEntity httpEntity, final String s2, final ResponseHandlerInterface responseHandlerInterface) {
        return this.sendRequest(this.h, this.a(new HttpPost(URLEncodedUtils.getURL(s)), httpEntity), s2, responseHandlerInterface, context);
    }
    
    public RequestHandle post(final Context context, final String s, final List<HttpHeader> headers, final RequestParams requestParams, final String s2, final ResponseHandlerInterface responseHandlerInterface) {
        final HttpPost httpPost = new HttpPost(URLEncodedUtils.getURL(s));
        if (requestParams != null) {
            httpPost.setEntity(this.a(requestParams, responseHandlerInterface));
        }
        if (headers != null) {
            httpPost.setHeaders(headers);
        }
        return this.sendRequest(this.h, httpPost, s2, responseHandlerInterface, context);
    }
    
    public RequestHandle post(final Context context, final String s, final List<HttpHeader> headers, final HttpEntity httpEntity, final String s2, final ResponseHandlerInterface responseHandlerInterface) {
        final HttpUriRequest a = this.a(new HttpPost(URLEncodedUtils.getURL(s)), httpEntity);
        if (headers != null) {
            a.setHeaders(headers);
        }
        return this.sendRequest(this.h, a, s2, responseHandlerInterface, context);
    }
    
    protected ClearHttpRequest newClearHttpRequest(final HttpManager httpManager, final HttpUriRequest httpUriRequest, final String s, final ResponseHandlerInterface responseHandlerInterface, final Context context) {
        return new ClearHttpRequest(httpManager, httpUriRequest, responseHandlerInterface);
    }
    
    protected RequestHandle sendRequest(final HttpManager httpManager, final HttpUriRequest httpUriRequest, final String s, final ResponseHandlerInterface responseHandlerInterface, final Context context) {
        if (httpUriRequest == null) {
            throw new IllegalArgumentException("HttpUriRequest must not be null");
        }
        if (responseHandlerInterface == null) {
            throw new IllegalArgumentException("ResponseHandler must not be null");
        }
        if (responseHandlerInterface.getUseSynchronousMode() && !responseHandlerInterface.getUsePoolThread()) {
            throw new IllegalArgumentException("Synchronous ResponseHandler used in ClearHttpClient. You should create your response handler in a looper thread or use SyncHttpClient instead.");
        }
        if (s != null) {
            if (httpUriRequest.getEntity() != null && httpUriRequest.containsHeader("Content-Type")) {
                TLog.w("Passed contentType will be ignored because HttpEntity sets content type", new Object[0]);
            }
            else {
                httpUriRequest.setHeader("Content-Type", s);
            }
        }
        responseHandlerInterface.setRequestHeaders(httpUriRequest.getAllHeaders());
        responseHandlerInterface.setRequestURL(httpUriRequest.getURL());
        final ClearHttpRequest clearHttpRequest = this.newClearHttpRequest(httpManager, httpUriRequest, s, responseHandlerInterface, context);
        this.f.submit(clearHttpRequest);
        final RequestHandle requestHandle = new RequestHandle(clearHttpRequest);
        if (context != null) {
            List<RequestHandle> synchronizedList;
            synchronized (this.a) {
                synchronizedList = this.a.get(context);
                if (synchronizedList == null) {
                    synchronizedList = Collections.synchronizedList(new LinkedList<RequestHandle>());
                    this.a.put(context, synchronizedList);
                }
            }
            synchronizedList.add(requestHandle);
            final Iterator<RequestHandle> iterator = synchronizedList.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().shouldBeGarbageCollected()) {
                    iterator.remove();
                }
            }
        }
        return requestHandle;
    }
    
    private HttpUriRequest a(final HttpUriRequest httpUriRequest, final HttpEntity entity) {
        if (entity != null) {
            httpUriRequest.setEntity(entity);
        }
        return httpUriRequest;
    }
    
    private HttpEntity a(final RequestParams requestParams, final ResponseHandlerInterface responseHandlerInterface) {
        HttpEntity entity = null;
        if (requestParams != null) {
            entity = requestParams.getEntity(responseHandlerInterface);
        }
        return entity;
    }
    
    public static String getUrlWithQueryString(final boolean b, String str, final RequestParams requestParams) {
        if (str == null) {
            return null;
        }
        if (b) {
            try {
                final URL url = new URL(URLDecoder.decode(str, "UTF-8"));
                str = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef()).toASCIIString();
            }
            catch (Exception ex) {
                TLog.e("getUrlWithQueryString encoding URL", ex);
            }
        }
        if (requestParams != null) {
            final String trim = requestParams.getParamString().trim();
            if (!trim.equals("") && !trim.equals("?")) {
                str += (str.contains("?") ? "&" : "?");
                str += trim;
            }
        }
        return str;
    }
    
    public static boolean isInputStreamGZIPCompressed(final PushbackInputStream pushbackInputStream) {
        if (pushbackInputStream == null) {
            return false;
        }
        final byte[] array = new byte[2];
        int i = 0;
        try {
            while (i < 2) {
                final int read = pushbackInputStream.read(array, i, 2 - i);
                if (read < 0) {
                    return false;
                }
                i += read;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                pushbackInputStream.unread(array, 0, i);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return 0x8B1F == ((array[0] & 0xFF) | (array[1] << 8 & 0xFF00));
    }
    
    public static void endEntityViaReflection(final HttpEntity httpEntity) {
        if (!(httpEntity instanceof HttpEntityWrapper)) {
            return;
        }
        ((HttpEntityWrapper)httpEntity).consumeWrappedEntity();
    }
    
    public static void test() {
        TLog.d("test start", new Object[0]);
        final ClearHttpClient clearHttpClient = new ClearHttpClient(80);
        clearHttpClient.setEnableRedirct(true);
        clearHttpClient.get("http://www.tusdk.com/", new TextHttpResponseHandler() {
            @Override
            public void onFailure(final int i, final List<HttpHeader> list, final String s, final Throwable t) {
                TLog.d("onFailure: %s | %s | %s | %s", i, list, s, t);
            }
            
            @Override
            public void onSuccess(final int i, final List<HttpHeader> list, final String s) {
                TLog.d("onSuccess: %s | %s | %s", i, list, s);
            }
        });
    }
    
    private static class InflatingEntity extends HttpEntityWrapper
    {
        InputStream a;
        PushbackInputStream b;
        GZIPInputStream c;
        
        public InflatingEntity(final HttpEntity httpEntity) {
            super(httpEntity);
        }
        
        @Override
        public InputStream getContent() {
            this.a = this.mWrappedEntity.getContent();
            this.b = new PushbackInputStream(this.a, 2);
            if (ClearHttpClient.isInputStreamGZIPCompressed(this.b)) {
                try {
                    return this.c = new GZIPInputStream(this.b);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return this.b;
        }
        
        @Override
        public long getContentLength() {
            return (this.mWrappedEntity == null) ? 0L : this.mWrappedEntity.getContentLength();
        }
        
        @Override
        public void consumeContent() {
            FileHelper.safeClose(this.a);
            FileHelper.safeClose(this.b);
            FileHelper.safeClose(this.c);
            super.consumeContent();
        }
    }
}
